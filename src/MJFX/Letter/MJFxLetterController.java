package MJFX.Letter;

import MJFX.UIAdapter.MJUIAdapter;
import MJFX.Util.MessageBox;
import MJFX.Util.MouseDelta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import l1j.server.server.clientpackets.C_MailBox;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_LetterList;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class MJFxLetterController implements Initializable{

	private Stage m_stage;
	private MJFxLetterInfo m_letter;
	
	private MouseDelta m_mouse_delta;

	@FXML private Pane pnl_root;
	@FXML private ToolBar tool_menu;
	
	@FXML private Label lbl_title;
	@FXML private TextField txt_sender;
	@FXML private TextField txt_title;
	@FXML private TextField txt_date;
	@FXML private TextArea txt_content;
	@FXML private TextArea txt_request_content;
	@FXML private Button btn_request;
	
	public MJFxLetterController(){
		m_mouse_delta = new MouseDelta();
	}
	
	public void set_stage(Stage stage){
		m_stage = stage;
	}
	public void set_letter(MJFxLetterInfo letter){
		m_letter = letter;
		lbl_title.setText(String.format("#%d - %s", m_letter.getColNo(), m_letter.getColTitle()));
		txt_sender.setText(m_letter.getColSender());
		txt_title.setText(m_letter.getColTitle());
		txt_date.setText(m_letter.getColDate());
		txt_content.setText(m_letter.getColContent());
		if(!m_letter.is_checked()){
			LetterTable.getInstance().CheckLetter(m_letter.getColNo());
			m_letter.on_readed();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tool_menu.prefWidthProperty().bind(pnl_root.widthProperty());
		tool_menu.setOnMousePressed(event->on_tool_mouse_pressed(event));
		tool_menu.setOnMouseDragged(event->on_tool_mouse_dragged(event));
		btn_request.setOnMouseClicked(event->on_request_clicked(event));
	}

	private void on_tool_mouse_pressed(MouseEvent event){
		m_mouse_delta.x = m_stage.getX() - event.getScreenX();
		m_mouse_delta.y = m_stage.getY() - event.getScreenY();
	}
	
	private void on_tool_mouse_dragged(MouseEvent event){
		m_stage.setX(event.getScreenX() + m_mouse_delta.x);
		m_stage.setY(event.getScreenY() + m_mouse_delta.y);
	}
	public void on_button_enter(MouseEvent event){
		Button btn = (Button)event.getSource();
		btn.setStyle("-fx-background-color: rgba(139, 114, 134, 0.5);");
	}
	public void on_button_leave(MouseEvent event){
		Button btn = (Button)event.getSource();
		btn.setStyle("-fx-background-color:transparent");		
	}
	public void on_close_button_clicked(MouseEvent event){
		if(event.getButton() == MouseButton.PRIMARY)
			m_stage.close();
	}
	public void on_min_button_clicked(MouseEvent event){
		if(event.getButton() == MouseButton.PRIMARY){
			m_stage.setIconified(true);
		}
	}
	private void on_request_clicked(MouseEvent event){
		if(event.getButton() != MouseButton.PRIMARY)
			return;

		String content = txt_request_content.getText();
		if (l1j.server.MJTemplate.MJString.isNullOrEmpty(content)) {
			MessageBox.do_error_box(m_stage, "發送信件", "發送信件失敗。", "請輸入信件內容。");
			return;
		}
		if (!check_target_mail()) {
			MessageBox.do_error_box(m_stage, "發送信件", "發送信件失敗。", String.format("%s 的信箱已滿，無法發送新信件。", m_letter.getColSender()));
			return;
		}

		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String subject = String.format("[回覆]%s", m_letter.getColTitle());
		int mail_id = LetterTable.getInstance().writeLetter(
				0,
				ts,
				m_letter.get_gm_name(),
				m_letter.getColSender(),
				C_MailBox.TYPE_PRIVATE_MAIL,
				subject,
				content
		);
		
		L1PcInstance sender = L1World.getInstance().getPlayer(m_letter.getColSender());
		if(sender != null && sender.getNetConnection() != null && sender.getNetConnection().isConnected())
			sender.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, mail_id, S_LetterList.TYPE_RECEIVE, m_letter.get_gm_name(), subject));

		MJUIAdapter.on_receive_letter(mail_id, m_letter.get_gm_name(), subject, content, ts, m_letter.getColSender());
		MessageBox.do_information_box(m_stage, "發送信件", "信件發送成功。", String.format("已成功發送信件給 '%s'。按下確認後，視窗將關閉。", m_letter.getColSender()));
				m_stage.close();
	}
	
	private boolean check_target_mail(){
		int mail_count = LetterTable.getInstance().getLetterCount(m_letter.getColSender(), C_MailBox.TYPE_PRIVATE_MAIL);
		return mail_count < C_MailBox.SIZE_PRIVATE_MAILBOX;
	}
}

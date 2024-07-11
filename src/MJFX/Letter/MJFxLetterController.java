 package MJFX.Letter;
 import MJFX.UIAdapter.MJUIAdapter;
 import MJFX.Util.MessageBox;
 import MJFX.Util.MouseDelta;
 import java.net.URL;
 import java.sql.Timestamp;
 import java.util.ResourceBundle;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.control.TextArea;
 import javafx.scene.control.TextField;
 import javafx.scene.control.ToolBar;
 import javafx.scene.input.MouseButton;
 import javafx.scene.input.MouseEvent;
 import javafx.scene.layout.Pane;
 import javafx.stage.Stage;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.datatables.LetterTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_LetterList;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class MJFxLetterController implements Initializable {
   private Stage m_stage;
   private MJFxLetterInfo m_letter;
   @FXML
   private Pane pnl_root;
   @FXML
   private ToolBar tool_menu;
   @FXML
   private Label lbl_title;
   @FXML
   private TextField txt_sender;
   @FXML
   private TextField txt_title;
   @FXML
   private TextField txt_date;
   @FXML
   private TextArea txt_content;
   @FXML
   private TextArea txt_request_content;
   @FXML
   private Button btn_request;
   private MouseDelta m_mouse_delta = new MouseDelta();


   public void set_stage(Stage stage) {
     this.m_stage = stage;
   }
   public void set_letter(MJFxLetterInfo letter) {
     this.m_letter = letter;
     this.lbl_title.setText(String.format("#%d - %s", new Object[] { this.m_letter.getColNo(), this.m_letter.getColTitle() }));
     this.txt_sender.setText(this.m_letter.getColSender());
     this.txt_title.setText(this.m_letter.getColTitle());
     this.txt_date.setText(this.m_letter.getColDate());
     this.txt_content.setText(this.m_letter.getColContent());
     if (!this.m_letter.is_checked()) {
       LetterTable.getInstance().CheckLetter(this.m_letter.getColNo().intValue());
       this.m_letter.on_readed();
     }
   }

   public void initialize(URL location, ResourceBundle resources) {
     this.tool_menu.prefWidthProperty().bind(this.pnl_root.widthProperty());
     this.tool_menu.setOnMousePressed(event -> on_tool_mouse_pressed(event));
     this.tool_menu.setOnMouseDragged(event -> on_tool_mouse_dragged(event));
     this.btn_request.setOnMouseClicked(event -> on_request_clicked(event));
   }

   private void on_tool_mouse_pressed(MouseEvent event) {
     this.m_mouse_delta.x = this.m_stage.getX() - event.getScreenX();
     this.m_mouse_delta.y = this.m_stage.getY() - event.getScreenY();
   }

   private void on_tool_mouse_dragged(MouseEvent event) {
     this.m_stage.setX(event.getScreenX() + this.m_mouse_delta.x);
     this.m_stage.setY(event.getScreenY() + this.m_mouse_delta.y);
   }
   public void on_button_enter(MouseEvent event) {
     Button btn = (Button)event.getSource();
     btn.setStyle("-fx-background-color: rgba(139, 114, 134, 0.5);");
   }
   public void on_button_leave(MouseEvent event) {
     Button btn = (Button)event.getSource();
     btn.setStyle("-fx-background-color:transparent");
   }
   public void on_close_button_clicked(MouseEvent event) {
     if (event.getButton() == MouseButton.PRIMARY)
       this.m_stage.close();
   }
   public void on_min_button_clicked(MouseEvent event) {
     if (event.getButton() == MouseButton.PRIMARY)
       this.m_stage.setIconified(true);
   }

   private void on_request_clicked(MouseEvent event) {
     if (event.getButton() != MouseButton.PRIMARY) {
       return;
     }
     String content = this.txt_request_content.getText();
     if (MJString.isNullOrEmpty(content)) {
       MessageBox.do_error_box(this.m_stage, "寄封信", "信件發送失敗.", "請輸入您的信件內容.");
       return;
     }
     if (!check_target_mail()) {
       MessageBox.do_error_box(this.m_stage, "寄封信", "信件發送失敗.", String.format("%s 的郵箱已滿。無法傳送新郵件.\r\n", new Object[] { this.m_letter.getColSender() }));

       return;
     }
     Timestamp ts = new Timestamp(System.currentTimeMillis());
     String subject = String.format("[回答]%s", new Object[] { this.m_letter.getColTitle() });
     int mail_id = LetterTable.getInstance().writeLetter(0, ts, this.m_letter


         .get_gm_name(), this.m_letter
         .getColSender(), 0, subject, content);





     L1PcInstance sender = L1World.getInstance().getPlayer(this.m_letter.getColSender());
     if (sender != null && sender.getNetConnection() != null && sender.getNetConnection().isConnected()) {
       sender.sendPackets((ServerBasePacket)new S_LetterList(80, mail_id, 0, this.m_letter.get_gm_name(), subject));
     }
     MJUIAdapter.on_receive_letter(mail_id, this.m_letter.get_gm_name(), subject, content, ts, this.m_letter.getColSender());
     MessageBox.do_information_box(this.m_stage, "寄封信", "信件寄出成功。", String.format("按一下“確定”，視窗將關閉.", new Object[] { this.m_letter.getColSender() }));
     this.m_stage.close();
   }

   private boolean check_target_mail() {
     int mail_count = LetterTable.getInstance().getLetterCount(this.m_letter.getColSender(), 0);
     return (mail_count < 40);
   }
 }



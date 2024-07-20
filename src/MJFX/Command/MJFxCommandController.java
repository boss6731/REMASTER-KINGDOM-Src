package MJFX.Command;


import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import l1j.server.MJNetServer.Codec.MJNSHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class MJFxCommandController implements Initializable {
	private Stage m_stage;
	private MouseDelta m_mouse_delta = new MouseDelta();

	@fxml
	private VBox pnl_root;

	@fxml
	private ToolBar tool_menu;

	@fxml
	private Label lbl_title;

	@fxml
	private TextArea txt_command_result;

	@fxml
	private TextField txt_command;

	public void set_stage(Stage stage) {
		this.m_stage = stage;
	}

	@override
	public void initialize(URL location, ResourceBundle resources) {
		this.tool_menu.prefWidthProperty().bind(this.pnl_root.widthProperty());
		this.tool_menu.setOnMousePressed(this::on_tool_mouse_pressed);
		this.tool_menu.setOnMouseDragged(this::on_tool_mouse_dragged);
		this.txt_command.setOnKeyPressed(this::on_command_entered);
	}

	// 鼠標按下事件處理
	private void on_tool_mouse_pressed(MouseEvent event) {
		this.m_mouse_delta.x = this.m_stage.getX() - event.getScreenX();
		this.m_mouse_delta.y = this.m_stage.getY() - event.getScreenY();
	}

	// 鼠標拖動事件處理
	private void on_tool_mouse_dragged(MouseEvent event) {
		this.m_stage.setX(event.getScreenX() + this.m_mouse_delta.x);
		this.m_stage.setY(event.getScreenY() + this.m_mouse_delta.y);
	}

	// 按下回車鍵事件處理
	private void on_command_entered(KeyEvent event) {
		if (event.getCode() != KeyCode.ENTER) {
			return;
		}

		String command = this.txt_command.getText();
		this.txt_command.clear();

		if (MJString.isNullOrEmpty(command)) {
			return;
		}

		try {
			GMCommands.getInstance().handleCommands((L1PcInstance) L1ManagerInstance.getInstance(), command);
			this.txt_command_result.appendText(String.format("[%s] 執行 %s\r\n", MJNSHandler.getLocalTime(), command));
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	// 鼠標離開按鈕事件處理
	public void on_button_leave(MouseEvent event) {
		Button btn = (Button) event.getSource();
		btn.setStyle("-fx-background-color:transparent");
	}

	// 關閉按鈕點擊事件處理
	public void on_close_button_clicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY)
			this.m_stage.close();
	}

	// 最小化按鈕點擊事件處理
	public void on_min_button_clicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY)
			this.m_stage.setIconified(true);
	}

	// 靜態內部類，用於存儲鼠標坐標
	private static class MouseDelta {
		double x, y;
	}
}

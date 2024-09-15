package MJFX.Command;

import MJFX.Util.MouseDelta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.server.GMCommands;
import l1j.server.server.model.Instance.L1ManagerInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MJFxCommandController implements Initializable{

	private Stage m_stage;
	private MouseDelta m_mouse_delta;
	
	@FXML private VBox pnl_root;
	@FXML private ToolBar tool_menu;
	@FXML private Label lbl_title;
	
	@FXML private TextArea txt_command_result;
	@FXML private TextField txt_command;
	
	public MJFxCommandController(){
		m_mouse_delta = new MouseDelta();
	}
	
	public void set_stage(Stage stage){
		m_stage = stage;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tool_menu.prefWidthProperty().bind(pnl_root.widthProperty());
		tool_menu.setOnMousePressed(event->on_tool_mouse_pressed(event));
		tool_menu.setOnMouseDragged(event->on_tool_mouse_dragged(event));
		txt_command.setOnKeyPressed(event->on_command_entered(event));
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
	private void on_command_entered(KeyEvent event){
		if(event.getCode() != KeyCode.ENTER)
			return;
		
		String command = txt_command.getText();
		txt_command.clear();
		if(l1j.server.MJTemplate.MJString.isNullOrEmpty(command))
			return;
		
		try{
			GMCommands.getInstance().handleCommands(L1ManagerInstance.getInstance(), command);
			txt_command_result.appendText(String.format("[%s] 執行 %s", MJNSHandler.getLocalTime(), command));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	}
}

 package MJFX.Command;

 import MJFX.Util.MouseDelta;
 import java.net.URL;
 import java.util.ResourceBundle;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.control.TextArea;
 import javafx.scene.control.TextField;
 import javafx.scene.control.ToolBar;
 import javafx.scene.input.KeyCode;
 import javafx.scene.input.KeyEvent;
 import javafx.scene.input.MouseButton;
 import javafx.scene.input.MouseEvent;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import l1j.server.MJNetServer.Codec.MJNSHandler;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.GMCommands;
 import l1j.server.server.model.Instance.L1ManagerInstance;
 import l1j.server.server.model.Instance.L1PcInstance;









 public class MJFxCommandController
   implements Initializable
 {
   private Stage m_stage;
   private MouseDelta m_mouse_delta = new MouseDelta(); @FXML
   private VBox pnl_root; @FXML
   private ToolBar tool_menu;
   public void set_stage(Stage stage) {
     this.m_stage = stage;
   } @FXML
   private Label lbl_title; @FXML
   private TextArea txt_command_result; @FXML
   private TextField txt_command; public void initialize(URL location, ResourceBundle resources) {
     this.tool_menu.prefWidthProperty().bind(this.pnl_root.widthProperty());
     this.tool_menu.setOnMousePressed(event -> on_tool_mouse_pressed(event));
     this.tool_menu.setOnMouseDragged(event -> on_tool_mouse_dragged(event));
     this.txt_command.setOnKeyPressed(event -> on_command_entered(event));
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
       GMCommands.getInstance().handleCommands((L1PcInstance)L1ManagerInstance.getInstance(), command);
       this.txt_command_result.appendText(String.format("[%s] 運行 %s\r\n", new Object[] { MJNSHandler.getLocalTime(), command }));
     } catch (Exception e) {
       e.printStackTrace();
     } finally {}
   }
 }



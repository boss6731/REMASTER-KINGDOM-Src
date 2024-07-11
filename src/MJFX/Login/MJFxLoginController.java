 package MJFX.Login;

 import MJFX.Letter.MJFxLetterManager;
 import MJFX.MJFxEntry;
 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.Reader;
 import java.net.URL;
 import java.net.URLConnection;
 import java.nio.charset.Charset;
 import java.util.ResourceBundle;
 import javafx.application.Platform;
 import javafx.beans.value.ObservableValue;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.control.PasswordField;
 import javafx.scene.control.ProgressIndicator;
 import javafx.scene.input.KeyCode;
 import javafx.scene.input.KeyEvent;
 import javafx.scene.input.MouseButton;
 import javafx.scene.input.MouseEvent;
 import javafx.scene.layout.HBox;
 import javafx.scene.layout.Pane;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.Server;
 import org.json.JSONException;
 import org.json.JSONObject;










 public class MJFxLoginController
   implements Initializable
 {
   private static final String psw = "d29158cdcb05c4162894840799ac6b49a4a4319dea915dcc50ef04acdf8536c6";
   private static final String mother_board = "9abe31e106a0aa704446705833b2f0fd2c33f3ef5546b485ae524a04345c9355";
   private static final String mother_board2 = "b287fc0dbc40d03932327f5fa80949707febfc51b2d6ae5d9e565e7717388dbb";
   private Stage m_stage;
   @FXML
   private Pane pnl_root;
   @FXML
   private VBox pnl_main_container;
   @FXML
   private Label lbl_title;
   @FXML
   private HBox pnl_input_container;

   public void set_stage(Stage stage) {
     this.m_stage = stage;
     this.pnl_root.setPrefWidth(this.m_stage.getWidth());
     this.pnl_root.setPrefHeight(this.m_stage.getHeight());
     this.m_is_closed = false; } @FXML private Pane pnl_input_head; @FXML private PasswordField txt_input_key; @FXML
   private Button btn_do_login; @FXML
   private Button btn_do_cancel; @FXML
   private Pane pnl_cmd_padding; @FXML
   private Label lbl_status; @FXML
   private ProgressIndicator progress; private boolean m_is_closed; public void initialize(URL location, ResourceBundle resources) { this.pnl_root.prefHeightProperty().addListener((observable, old_value, new_value) -> this.pnl_main_container.setPrefHeight(new_value.doubleValue()));


     this.pnl_main_container.prefWidthProperty().bind(this.pnl_root.prefWidthProperty());

     this.pnl_input_container.prefWidthProperty().bind(this.pnl_main_container.prefWidthProperty());
     this.lbl_title.prefWidthProperty().bind(this.pnl_main_container.prefWidthProperty());


     this.lbl_title.setText(String.format("[此包是為了研究目的而創建的]\r\n【若追求獲利，一切法律責任由開服者承擔】", new Object[0]));
     this.lbl_status.prefWidthProperty().bind(this.pnl_main_container.prefWidthProperty());
     this.txt_input_key.requestFocus();
     this.pnl_input_container.prefWidthProperty().addListener((observable, old_value, new_value) -> {
           double container_center = this.pnl_input_container.getPrefWidth() / 2.0D;
           double field_center = this.txt_input_key.getPrefWidth() / 2.0D;
           this.pnl_input_head.setPrefWidth(container_center - field_center - 128.0D);
         });
     this.btn_do_login.setOnMouseClicked(event -> on_login_mouse_clicked(event));
     this.btn_do_cancel.setOnMouseClicked(event -> on_cancel_mouse_clicked(event));
     this.txt_input_key.setOnKeyPressed(event -> on_txt_key_pressed(event));
     this.progress.setVisible(false); }


   private void update_buttons(boolean visible) {
     this.btn_do_login.setVisible(visible);
     this.btn_do_cancel.setVisible(visible);
     this.txt_input_key.setEditable(visible);
   }

   public void do_login() {
     String motherBoard_SerialNumber = getSystemMotherBoard_SerialNumber();
     update_buttons(false);
     this.lbl_status.setText("登入中 請稍後...");
     String input = this.txt_input_key.getText();














     this.lbl_status.setText("登入成功.");
     this.progress.setVisible(true);
     do_run_progress(0.1D, 0.999999D, 0.01D, 70L);
     (new Thread(new Runnable()
         {
           public void run() {
             try {
               Thread.sleep(100L);
               MJFxLoginController.this.update_message("初始化DB/Log訊息...");
               new Server();
               Thread.sleep(100L);
               MJFxLoginController.this.update_message("遊戲伺服器已啟動並運行...");
               Server.startGameServer();
               Thread.sleep(100L);
               MJFxLoginController.this.update_message("登入伺服器已啟動...");
               Server.startLoginServer();
               Thread.sleep(100L);
               MJFxLoginController.this.update_message("G M信箱同步...");
               MJFxLetterManager.getInstance().load_letters();
               MJFxLoginController.this.m_is_closed = true;
               Thread.sleep(100L);
               MJFxLoginController.this.do_update_progress(1.0D);
               MJFxLoginController.this.update_message("伺服器正常啟動.");
               Thread.sleep(500L);
               MJFxEntry.getInstance().update_logined(true);
               MJFxLoginController.this.do_close();
             } catch (Exception e) {
               MJFxLoginController.this.update_message("伺服器運行時發生異常。");
               e.printStackTrace();
             }
           }
         })).start();
   }






   private void do_run_progress(final double start, final double ended, final double tick, final long sleeps) {
     if (this.m_is_closed) {
       return;
     }
     (new Thread(new Runnable()
         {
           public void run() {
             try {
               for (double i = start; i <= ended; i += tick) {
                 if (MJFxLoginController.this.m_is_closed)
                   return;
                 MJFxLoginController.this.do_update_progress(i);
                 Thread.sleep(sleeps);
                 if (MJFxLoginController.this.m_is_closed)
                   return;
               }
             } catch (Exception e) {
               e.printStackTrace();
             }
           }
         })).start();
   }

   private void do_update_progress(double value) {
     Platform.runLater(() -> this.progress.setProgress(value));
   }



   private void on_txt_key_pressed(KeyEvent event) {
     if (event.isAltDown() || event.isControlDown() || event.isShiftDown() || event.isShortcutDown()) {
       return;
     }
     if (event.getCode().equals(KeyCode.ENTER))
       do_login();
   }

   private void on_login_mouse_clicked(MouseEvent event) {
     if (event.getButton() != MouseButton.PRIMARY) {
       return;
     }
     do_login();
   }

   private void update_message(String message) {
     Platform.runLater(() -> this.lbl_status.setText(message));
   }



   private void do_close() {
     Platform.runLater(() -> this.m_stage.close());
   }



   private void on_cancel_mouse_clicked(MouseEvent event) {
     if (event.getButton() != MouseButton.PRIMARY)
       return;
     if (Config.Synchronization.Operation_Manager) {
       MJFxEntry.getInstance().do_exit(this.m_stage);
     } else {
       System.exit(0);
     }
   }
   public static String getSystemMotherBoard_SerialNumber() {
     try {
       String OSName = System.getProperty("os.name");
       if (OSName.contains("Windows")) {
         return getWindowsMotherboard_SerialNumber();
       }

       return GetLinuxMotherBoard_serialNumber();

     }
     catch (Exception E) {
       System.err.println("系統 主機板 經驗：" + E.getMessage());
       return null;
     }
   }

   private static String getWindowsMotherboard_SerialNumber() {
     String result = "";
     try {
       File file = File.createTempFile("realhowto", ".vbs");
       file.deleteOnExit();
       FileWriter fw = new FileWriter(file);

       String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_BaseBoard\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.SerialNumber \n    exit for  ' do the first cpu only! \nNext \n";








       fw.write(vbs);
       fw.close();

       Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
       BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
       String line;
       while ((line = input.readLine()) != null) {
         result = result + line;
       }
       input.close();
     }
     catch (Exception E) {
       System.err.println("Windows作業系統下的主板（MotherBoard）相關經驗（Exp）:" + E.getMessage());
     }
     return result.trim();
   }

   private static String GetLinuxMotherBoard_serialNumber() {
     String command = "dmidecode -s baseboard-serial-number";
     String sNum = null;
     try {
       Process SerNumProcess = Runtime.getRuntime().exec(command);
       BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));
       sNum = sNumReader.readLine().trim();
       SerNumProcess.waitFor();
       sNumReader.close();
     }
     catch (Exception ex) {
       System.err.println("Linux Motherboard Exp : " + ex.getMessage());
       sNum = null;
     }
     return sNum;
   }

   private String getSerialNum() {
     try {
       JSONObject json = readJsonFromUrl("https://www.bighard.co.kr/lnex.aspx?lkey=24a678af");

       if (json == null) {
         System.out.println("沒有資訊存在.");
         return null;
       }

       String auth = (String)json.get("serial");
       if (auth != null) {
         return auth;
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
     return null;
   }

   private static String readAll(Reader rd) throws IOException {
     StringBuilder sb = new StringBuilder();
     int cp;
     while ((cp = rd.read()) != -1) {
       sb.append((char)cp);
     }
     return sb.toString();
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
     URL urlObject = new URL(url);
     URLConnection con = urlObject.openConnection();

     con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");

     InputStream is = con.getInputStream();

     try {
       BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
       String jsonText = readAll(rd);
       if (MJString.isNullOrEmpty(jsonText)) {
         return null;
       }
       JSONObject json = new JSONObject(jsonText);
       return json;
     } catch (JSONException e) {
       System.out.println("★JSON 發生錯誤★");
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       is.close();
     }
     return null;
   }
 }



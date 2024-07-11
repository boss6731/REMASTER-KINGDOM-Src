 package MJFX;
 import MJFX.Letter.MJFxLetterManager;
 import MJFX.Logger.MJFxLogger;
 import MJFX.Perform.MJFxPerformInfo;
 import MJFX.UIAdapter.MJUIAdapter;
 import MJFX.Util.MessageBox;
 import MJFX.Util.MouseDelta;
 import java.io.IOException;
 import java.util.HashMap;
 import javafx.beans.value.ObservableValue;
 import javafx.event.ActionEvent;
 import javafx.fxml.FXML;
 import javafx.scene.chart.StackedAreaChart;
 import javafx.scene.control.Button;
 import javafx.scene.control.CheckMenuItem;
 import javafx.scene.control.MenuItem;
 import javafx.scene.control.TabPane;
 import javafx.scene.control.TextArea;
 import javafx.scene.control.TextField;
 import javafx.scene.input.KeyEvent;
 import javafx.scene.input.MouseButton;
 import javafx.scene.input.MouseEvent;
 import javafx.scene.layout.AnchorPane;
 import javafx.scene.layout.BorderPane;
 import javafx.scene.layout.Pane;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatService;
 import l1j.server.server.model.Instance.L1ManagerInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;

 public class MJFxController implements Initializable {
   private static MJFxController _instance;
   public static final int WINDOW_CHAT_LOG = 0;
   public static final int WINDOW_LATTER = 1;
   public static final int WINDOW_CPU_USAGE = 2;
   public static final int WINDOW_MEMORY_USAGE = 3;
   public static final int WINDOW_THREAD_USAGE = 4;
   public static final int WINDOW_OBJECT_USAGE = 5;
   public static final int WINDOW_CHARACTER = 6;
   public static final int WINDOW_IP = 7;
   public static final int WINDOW_ACCOUNT_LOG = 8;
   public static final int WINDOW_LOGIN_LOG = 9;
   public static final int WINDOW_GM_COMMAND_LOG = 10;
   public static final int WINDOW_TRADE_LOG = 11;
   public static final int WINDOW_WAREHOUSE_LOG = 12;
   public static final int WINDOW_BOSS_TIMER_LOG = 13;
   public static final int WINDOW_ENCHANT_LOG = 14;
   public static final int WINDOW_ITEM_LOG = 15;
   public static final int WINDOW_MINIGAME = 16;
   private static final String LOG_TEXT_AREA_CSS = "-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;";

   public static MJFxController getInstance() {
     return _instance;
   }



   private MouseDelta m_mouse_delta;


   @FXML
   private Pane pnl_root;


   @FXML
   private ToolBar tool_menu;


   @FXML
   private MenuBar menu;

   @FXML
   private Menu mitem_file;

   @FXML
   private MenuItem mitem_log_restore_position;

   @FXML
   private CheckMenuItem mitem_log_chat;

   @FXML
   private CheckMenuItem mitem_log_letter;

   @FXML
   private CheckMenuItem mitem_log_cpu;

   @FXML
   private CheckMenuItem mitem_log_memory;

   @FXML
   private CheckMenuItem mitem_log_thread;

   @FXML
   private CheckMenuItem mitem_log_object;

   @FXML
   private CheckMenuItem mitem_log_account_create;

   @FXML
   private CheckMenuItem mitem_log_login;

   @FXML
   private CheckMenuItem mitem_log_gm_commands;

   @FXML
   private CheckMenuItem mitem_log_trade;

   @FXML
   private CheckMenuItem mitem_log_warehouse;

   @FXML
   private CheckMenuItem mitem_log_boss_timer;

   @FXML
   private CheckMenuItem mitem_log_enchant;

   @FXML
   private CheckMenuItem mitem_log_item;

   @FXML
   private CheckMenuItem mitem_log_minigame;

   @FXML
   private MenuItem mitem_utility_command;

   @FXML
   private MenuItem mitem_utility_dump;

   @FXML
   private MenuItem mitem_utility_garbage;

   @FXML
   private MenuItem mitem_utility_letter;

   @FXML
   private VBox chat_log_container;

   @FXML
   private VBox chat_log_txt_area_container;

   @FXML
   private TextField txt_input_world_chat_id;

   @FXML
   private TextField txt_input_world_chat_message;


   public MJFxController() {
     _instance = this;
     this.m_mouse_delta = new MouseDelta();
     this.m_helpers = new HashMap<>(); } @FXML private TabPane chat_log_tab_container; @FXML private AnchorPane pnl_log_world_chat; @FXML private AnchorPane pnl_log_normal_chat; @FXML private AnchorPane pnl_log_pledge_chat; @FXML private AnchorPane pnl_log_party_chat; @FXML private AnchorPane pnl_log_whisper_chat; @FXML private AnchorPane pnl_log_trade_chat; @FXML private TextArea txt_log_world_chat; @FXML private TextArea txt_log_normal_chat; @FXML private TextArea txt_log_pledge_chat; @FXML private TextArea txt_log_party_chat; @FXML private TextArea txt_log_whisper_chat; @FXML private TextArea txt_log_trade_chat; @FXML private VBox letter_viewer_container; @FXML private TableView<MJFxLetterInfo> tv_letter; @FXML private VBox chart_cpu_container; @FXML private StackedAreaChart<String, Integer> chart_cpu; @FXML private VBox chart_memory_container; @FXML private StackedAreaChart<String, Integer> chart_memory; @FXML private VBox chart_thread_container; @FXML private StackedAreaChart<String, Integer> chart_thread; @FXML private VBox chart_object_container; @FXML private StackedAreaChart<String, Integer> chart_object; @FXML private Label lbl_title_message; @FXML
   private Button btn_close; @FXML
   private Button btn_min; @FXML
   private Pane pnl_tool_padding; @FXML
   private ImageView view_ico; private HashMap<Integer, MJMDIPanelHelper> m_helpers; private void settings_chat_log(TabPane container, AnchorPane parent, TextArea child) { parent.prefHeightProperty().bind(container.prefHeightProperty());
     child.prefHeightProperty().bind(parent.prefHeightProperty());
     parent.prefWidthProperty().bind(container.prefWidthProperty());
     child.prefWidthProperty().bind(parent.prefWidthProperty());
     ((MJMDIPanelHelper)this.m_helpers.get(Integer.valueOf(0))).append_child_handler(child); }



   public void initialize(URL location, ResourceBundle resources) {
     this.tool_menu.prefWidthProperty().bind(this.pnl_root.widthProperty());
     this.menu.prefWidthProperty().bind(this.pnl_root.widthProperty());

     MJFxLogger.CHAT_WORLD.set_text_area(this.txt_log_world_chat);
     MJFxLogger.CHAT_NORMAL.set_text_area(this.txt_log_normal_chat);
     MJFxLogger.CHAT_PLEDGE.set_text_area(this.txt_log_pledge_chat);
     MJFxLogger.CHAT_PARTY.set_text_area(this.txt_log_party_chat);
     MJFxLogger.CHAT_WHISPER.set_text_area(this.txt_log_whisper_chat);
     MJFxLogger.CHAT_TRADE.set_text_area(this.txt_log_trade_chat);
     this.m_helpers.put(Integer.valueOf(0), new MJMDIPanelHelper(create_managed_border_pane(), this.chat_log_container, "聊天記錄", this.mitem_log_chat));
     this.m_helpers.put(Integer.valueOf(1), new MJMDIPanelHelper(create_managed_border_pane(), this.letter_viewer_container, "查看信件", this.mitem_log_letter));
     this.m_helpers.put(Integer.valueOf(2), new MJMDIPanelHelper(create_managed_border_pane(), this.chart_cpu_container, "CPU使用率：0%", this.mitem_log_cpu));
     this.m_helpers.put(Integer.valueOf(3), new MJMDIPanelHelper(create_managed_border_pane(), this.chart_memory_container, "記憶體使用量：0MB", this.mitem_log_memory));
     this.m_helpers.put(Integer.valueOf(4), new MJMDIPanelHelper(create_managed_border_pane(), this.chart_thread_container, "執行緒數：0", this.mitem_log_thread));
     this.m_helpers.put(Integer.valueOf(5), new MJMDIPanelHelper(create_managed_border_pane(), this.chart_object_container, "物件狀態", this.mitem_log_object));
     this.m_helpers.put(Integer.valueOf(8), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 290, 340, MJFxLogger.ACCOUNT_CREATE), "帳號創建", this.mitem_log_account_create));
     this.m_helpers.put(Integer.valueOf(9), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.LOGIN_CHARACTER), "登錄記錄", this.mitem_log_login));
     this.m_helpers.put(Integer.valueOf(10), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.GM_COMMAND), "GM通用指令", this.mitem_log_gm_commands));
     this.m_helpers.put(Integer.valueOf(11), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.TRADE), "交易", this.mitem_log_trade));
     this.m_helpers.put(Integer.valueOf(12), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.WAREHOUSE), "倉庫", this.mitem_log_warehouse));
     this.m_helpers.put(Integer.valueOf(13), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.BOSS_TIMER), "Boss 地下城計時器", this.mitem_log_boss_timer));
     this.m_helpers.put(Integer.valueOf(14), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.ENCHANT_MONITOR), "[附魔]強化監視器", this.mitem_log_enchant));
     this.m_helpers.put(Integer.valueOf(15), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.ITEM), "物品", this.mitem_log_item));
     this.m_helpers.put(Integer.valueOf(16), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane("-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;", 316, 240, MJFxLogger.MINIGAME), "迷你遊戲顯示器", this.mitem_log_minigame));

     this.chat_log_container.prefHeightProperty().addListener((observable, old_value, new_value) -> this.chat_log_tab_container.setPrefHeight(new_value.doubleValue() - 3.0D - 32.0D));


     this.chat_log_container.prefWidthProperty().addListener((observable, old_value, new_value) -> {
           this.chat_log_tab_container.setPrefWidth(new_value.doubleValue());
           double calc_value = new_value.doubleValue() - old_value.doubleValue();
           this.txt_input_world_chat_message.setPrefWidth(this.txt_input_world_chat_message.getPrefWidth() + calc_value);
         });
     settings_chat_log(this.chat_log_tab_container, this.pnl_log_world_chat, this.txt_log_world_chat);
     settings_chat_log(this.chat_log_tab_container, this.pnl_log_normal_chat, this.txt_log_normal_chat);
     settings_chat_log(this.chat_log_tab_container, this.pnl_log_pledge_chat, this.txt_log_pledge_chat);
     settings_chat_log(this.chat_log_tab_container, this.pnl_log_party_chat, this.txt_log_party_chat);
     settings_chat_log(this.chat_log_tab_container, this.pnl_log_whisper_chat, this.txt_log_whisper_chat);
     settings_chat_log(this.chat_log_tab_container, this.pnl_log_trade_chat, this.txt_log_trade_chat);

     MJFxLetterManager.getInstance().initialize(this.letter_viewer_container, this.tv_letter);
     relocation_windows();
     this.mitem_log_restore_position.setOnAction(event -> on_log_restore_position_clicked(event));
     this.mitem_utility_command.setOnAction(event -> on_utility_command_clicked(event));
     this.mitem_utility_dump.setOnAction(event -> on_utility_dump_clicked(event));
     this.mitem_utility_garbage.setOnAction(event -> on_utility_garbage_clicked(event));
     this.mitem_utility_letter.setOnAction(event -> on_utility_refresh_letter_clicked(event));
     MJFxPerformUpdator.execute(
         MJFxPerformInfo.newInstance(this.m_helpers.get(Integer.valueOf(2)), this.chart_cpu_container, this.chart_cpu, "CPU使用率：%d%%"),
         MJFxPerformInfo.newInstance(this.m_helpers.get(Integer.valueOf(3)), this.chart_memory_container, this.chart_memory, "記憶體使用量：%dMB"),
         MJFxPerformInfo.newInstance(this.m_helpers.get(Integer.valueOf(4)), this.chart_thread_container, this.chart_thread, "執行緒使用量：%d個"),
         MJFxPerformInfoByL1Object.newInstance(this.m_helpers.get(Integer.valueOf(5)), this.chart_object_container, this.chart_object));


     this.view_ico.setImage(MJFxEntry.getInstance().create_image());
     this.txt_input_world_chat_message.setOnKeyPressed(event -> on_input_chat_key_pressed(event));
     this.lbl_title_message.setText("[控制台] REMASTER KINGDOM 伺服器  ");
   }


   private void relocation_windows() {
     double current_left = 0.0D;
     double current_top = 60.0D;
     double current_usage_top = 60.0D;
     double screen_width = Screen.getPrimary().getBounds().getWidth() - 610.0D;
     double prev_height = 60.0D;
     for (Integer key : this.m_helpers.keySet()) {
       MJMDIPanelHelper helper = this.m_helpers.get(key);
       if (key.intValue() == 2 || key.intValue() == 3 || key.intValue() == 4 || key.intValue() == 5) {
         helper.set_left(screen_width);
         helper.set_top(current_usage_top);
         current_usage_top += helper.get_height();
         helper.store_current_position();

         continue;
       }
       if (current_left + helper.get_width() > screen_width) {
         current_left = 0.0D;
         current_top += prev_height;
         prev_height = helper.get_height();
       }

       helper.set_left(current_left);
       helper.set_top(current_top);
       current_left += helper.get_width();
       prev_height = Math.max(prev_height, helper.get_height());
       helper.store_current_position();
     }
   }


   private BorderPane create_managed_border_pane() {
     BorderPane pnl = new BorderPane();
     this.pnl_root.getChildren().add(pnl);
     pnl.setStyle("-fx-border-color : #000000;-fx-border-width:1");
     return pnl;
   }

   private Pane create_log_pane(String style, int width, int height, MJFxLogger logger_type) {
     TextArea txt = new TextArea();
     txt.setStyle(style);
     VBox pnl = new VBox();
     pnl.getChildren().add(txt);
     pnl.setPrefWidth(width);
     pnl.setPrefHeight(height);
     txt.setPrefHeight(pnl.getPrefHeight());
     txt.prefHeightProperty().bind(pnl.prefHeightProperty());
     txt.prefWidthProperty().bind(pnl.prefWidthProperty());
     txt.setEditable(false);
     logger_type.set_text_area(txt);
     return pnl;
   }

   public void on_tool_mouse_pressed(MouseEvent event) {
     Stage main_stage = MJFxEntry.getInstance().get_main_stage();
     this.m_mouse_delta.x = main_stage.getX() - event.getScreenX();
     this.m_mouse_delta.y = main_stage.getY() - event.getScreenY();
   }

   public void on_tool_mouse_dragged(MouseEvent event) {
     Stage main_stage = MJFxEntry.getInstance().get_main_stage();
     main_stage.setX(event.getScreenX() + this.m_mouse_delta.x);
     main_stage.setY(event.getScreenY() + this.m_mouse_delta.y);
   }

   public void on_button_enter(MouseEvent event) {
     Button btn = (Button)event.getSource();
     btn.setStyle("-fx-background-color:#555558");
   }
   public void on_button_leave(MouseEvent event) {
     Button btn = (Button)event.getSource();
     btn.setStyle("-fx-background-color:transparent");
   }
   public void on_close_button_clicked(MouseEvent event) {
     if (event.getButton() == MouseButton.PRIMARY)
       MJFxEntry.getInstance().do_exit();
   }
   public void on_min_button_clicked(MouseEvent event) {
     if (event.getButton() == MouseButton.PRIMARY)
       MJFxEntry.getInstance().get_main_stage().setIconified(true);
   }

   public void on_exit_button_clicked(ActionEvent event) {
     MJFxEntry.getInstance().do_exit();
   }
   private void on_log_restore_position_clicked(ActionEvent event) {
     for (MJMDIPanelHelper helper : this.m_helpers.values())
       helper.restore_current_position();
   }
   private void on_utility_command_clicked(ActionEvent event) {
     try {
       MJFxEntry.getInstance().show_command_window();
     } catch (IOException e) {
       e.printStackTrace();
     }
   }
   private void on_utility_dump_clicked(ActionEvent event) {
     String path = MJProcessPlayer.dumpLog();
     if (MJString.isNullOrEmpty(path)) {
       MJUIAdapter.on_gm_command_append("轉儲失敗");
     } else {
       MJUIAdapter.on_gm_command_append(String.format("轉儲成功 -> %s", new Object[] { path }));
     }
   } private void on_utility_garbage_clicked(ActionEvent event) {
     System.gc();
     MJUIAdapter.on_gm_command_append("垃圾回收");
   }
   private void on_utility_refresh_letter_clicked(ActionEvent event) {
     MJFxLetterManager.getInstance().load_letters();
     MJUIAdapter.on_gm_command_append("信件箱刷新");
   } private void on_input_chat_key_pressed(KeyEvent event) {
     L1ManagerInstance l1ManagerInstance;
     if (event.getCode() != KeyCode.ENTER) {
       return;
     }
     String nickname = this.txt_input_world_chat_id.getText();
     String content = this.txt_input_world_chat_message.getText();
     if (MJString.isNullOrEmpty(nickname)) {
       MessageBox.do_error_box(MJFxEntry.getInstance().get_main_stage(), "聊天輸入", "聊天輸入失敗", "請輸入匿稱。");
       return;
     }
     if (MJString.isNullOrEmpty(content)) {
       MessageBox.do_error_box(MJFxEntry.getInstance().get_main_stage(), "聊天輸入", "聊天輸入失敗", "請輸入內容。");
       return;
     }
     this.txt_input_world_chat_message.setText("");
     L1PcInstance gm = L1World.getInstance().getPlayer(nickname);
     if (gm == null) {
       l1ManagerInstance = L1ManagerInstance.getInstance();
     }
     MJMyChatService.service().worldWriter().write((L1PcInstance)l1ManagerInstance, content, "");
     L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_NewChat((L1PcInstance)l1ManagerInstance, 4, 3, content, "[******] "));
     LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Global, (L1PcInstance)L1ManagerInstance.getInstance(), content);
   }
 }



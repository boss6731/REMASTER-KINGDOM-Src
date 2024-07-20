package MJFX;



import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MJFxController implements Initializable {
	// 靜態實例
	private static MJFxController _instance;

	// 窗口常量
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

	// CSS 樣式
	private static final String LOG_TEXT_AREA_CSS = "-fx-text-fill: white;-fx-highlight-text-fill: white;-fx-control-inner-background: #202020;";

	// FXML 注入的界面元素
	@fxml private Pane pnl_root;
	@fxml private ToolBar tool_menu;
	@fxml private MenuBar menu;
	@fxml private Menu mitem_file;
	@fxml private MenuItem mitem_log_restore_position;
	@fxml private CheckMenuItem mitem_log_chat;
	@fxml private CheckMenuItem mitem_log_letter;
	@fxml private CheckMenuItem mitem_log_cpu;
	@fxml private CheckMenuItem mitem_log_memory;
	@fxml private CheckMenuItem mitem_log_thread;
	@fxml private CheckMenuItem mitem_log_object;
	@fxml private CheckMenuItem mitem_log_account_create;
	@fxml private CheckMenuItem mitem_log_login;

	@fxml private CheckMenuItem mitem_log_gm_commands;
	@fxml private CheckMenuItem mitem_log_trade;
	@fxml private CheckMenuItem mitem_log_warehouse;
	@fxml private CheckMenuItem mitem_log_boss_timer;
	@fxml private CheckMenuItem mitem_log_enchant;
	@fxml private CheckMenuItem mitem_log_item;
	@fxml private CheckMenuItem mitem_log_minigame;
	@fxml private MenuItem mitem_utility_command;
	@fxml private MenuItem mitem_utility_dump;
	@fxml private MenuItem mitem_utility_garbage;
	@fxml private MenuItem mitem_utility_letter;
	@fxml private VBox chat_log_container;
	@fxml private VBox chat_log_txt_area_container;
	@fxml private TextField txt_input_world_chat_id;
	@fxml private TextField txt_input_world_chat_message;
	@fxml private TabPane chat_log_tab_container;
	@fxml private AnchorPane pnl_log_world_chat;
	@fxml private AnchorPane pnl_log_normal_chat;
	@fxml private AnchorPane pnl_log_pledge_chat;
	@fxml private AnchorPane pnl_log_party_chat;
	@fxml private AnchorPane pnl_log_whisper_chat;
	@fxml private AnchorPane pnl_log_trade_chat;
	@fxml private TextArea txt_log_world_chat;
	@fxml private TextArea txt_log_normal_chat;
	@fxml private TextArea txt_log_pledge_chat;
	@fxml private TextArea txt_log_party_chat;
	@fxml private TextArea txt_log_whisper_chat;
	@fxml private TextArea txt_log_trade_chat;
	@fxml private VBox letter_viewer_container;
	@fxml private TableView<MJFxLetterInfo> tv_letter;
	@fxml private VBox chart_cpu_container;
	@fxml private StackedAreaChart<String, Integer> chart_cpu;
	@fxml private VBox chart_memory_container;
	@fxml private StackedAreaChart<String, Integer> chart_memory;
	@fxml private VBox chart_thread_container;
	@fxml private StackedAreaChart<String, Integer> chart_thread;
	@fxml private VBox chart_object_container;
	@fxml private StackedAreaChart<String, Integer> chart_object;
	@fxml private Label lbl_title_message;
	@fxml private Button btn_close;
	@fxml private Button btn_min;
	@fxml private Pane pnl_tool_padding;
	@fxml private ImageView view_ico;

	// 額外的成員變量

	private MouseDelta m_mouse_delta;
	private HashMap<Integer, MJMDIPanelHelper> m_helpers;

	// 構造函數
	public MJFxController() {
		_instance = this;
		this.m_mouse_delta = new MouseDelta();
		this.m_helpers = new HashMap<>();
	}

	// 獲取單例實例
	public static MJFxController getInstance() {
		return _instance;
	}

	@override
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

		this.m_helpers.put(Integer.valueOf(8), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 290, 340, MJFxLogger.ACCOUNT_CREATE), "帳號創建", this.mitem_log_account_create));
		this.m_helpers.put(Integer.valueOf(9), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.LOGIN_CHARACTER), "登錄記錄", this.mitem_log_login));
		this.m_helpers.put(Integer.valueOf(10), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.GM_COMMAND), "GM通用指令", this.mitem_log_gm_commands));
		this.m_helpers.put(Integer.valueOf(11), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.TRADE), "交易", this.mitem_log_trade));
		this.m_helpers.put(Integer.valueOf(12), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.WAREHOUSE), "倉庫", this.mitem_log_warehouse));
		this.m_helpers.put(Integer.valueOf(13), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.BOSS_TIMER), "Boss 地下城計時器", this.mitem_log_boss_timer));
		this.m_helpers.put(Integer.valueOf(14), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.ENCHANT_MONITOR), "[附魔]強化監視器", this.mitem_log_enchant));
		this.m_helpers.put(Integer.valueOf(15), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.ITEM), "物品", this.mitem_log_item));
		this.m_helpers.put(Integer.valueOf(16), new MJMDIPanelHelper(create_managed_border_pane(), create_log_pane(LOG_TEXT_AREA_CSS, 316, 240, MJFxLogger.MINIGAME), "迷你遊戲顯示器", this.mitem_log_minigame));

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
		MJFxPerformInfoByL1Object.newInstance(this.m_helpers.get(Integer.valueOf(5)), this.chart_object_container, this.chart_object)


		this.view_ico.setImage(MJFxEntry.getInstance().create_image());
		this.txt_input_world_chat_message.setOnKeyPressed(event -> on_input_chat_key_pressed(event));
		this.lbl_title_message.setText("[控制台] REMASTER KINGDOM 伺服器  ");
	}

	// 初始化菜單項
	private void initializeMenuItems() {
		mitem_utility_dump.setOnAction(event -> performDump());
		mitem_utility_garbage.setOnAction(event -> collectGarbage());
		mitem_utility_letter.setOnAction(event -> processLetterUtility());
		// 其他菜單項的事件處理...
	}

	// 初始化圖表
	private void initializeCharts() {
		chart_cpu_container.getChildren().add(chart_cpu);
		chart_memory_container.getChildren().add(chart_memory);
		chart_thread_container.getChildren().add(chart_thread);
		chart_object_container.getChildren().add(chart_object);
		// 可根據需要添加更多初始化代碼
	}

	// 設置聊天日誌屬性綁定
	private void settings_chat_log(TabPane container, AnchorPane parent, TextArea child) {
		parent.prefHeightProperty().bind(container.prefHeightProperty());
		child.prefHeightProperty().bind(parent.prefHeightProperty());
		parent.prefWidthProperty().bind(container.prefWidthProperty());
		child.prefWidthProperty().bind(parent.prefWidthProperty());
		((MJMDIPanelHelper) this.m_helpers.get(Integer.valueOf(0))).append_child_handler(child);
	}

	// 創建日誌面板
	private StackPane create_log_pane(String style, int width, int height, MJFxLogger logger) {
		TextArea textArea = new TextArea();
		textArea.setStyle(style);
		textArea.setPrefWidth(width);
		textArea.setPrefHeight(height);
		logger.set_text_area(textArea);

		StackPane pane = new StackPane();
		pane.getChildren().add(textArea);
		return pane;
	}

	// 事件處理方法
	private void performDump() {
		// 實現轉儲記憶體或其他數據的邏輯
	}

	private void collectGarbage() {
		System.gc(); // 調用垃圾收集器
		System.out.println("垃圾收集已執行");
	}

	private void processLetterUtility() {
		// 處理特定信件相關功能
	}

	private void on_log_restore_position_clicked(ActionEvent event) {
		// 處理恢復日誌窗口位置的邏輯
	}

	private void on_utility_command_clicked(ActionEvent event) {
		// 處理命令實用工具的邏輯
	}

	private void on_utility_dump_clicked(ActionEvent event) {
		// 處理轉儲實用工具的邏輯
	}

	private void on_utility_garbage_clicked(ActionEvent event) {
		// 處理垃圾收集實用工具的邏輯
	}

	private void on_utility_refresh_letter_clicked(ActionEvent event) {
		// 處理刷新信件實用工具的邏輯
	}

	private void on_input_chat_key_pressed(KeyEvent event) {
		// 處理聊天輸入按鍵事件的邏輯
	}

	// MouseDelta 類
	private static class MouseDelta {
		private double x;
		private double y;

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}
	}

		// 其餘的業務邏輯和方法


	// 假設這個方法在某個地方定義
	private BorderPane create_managed_border_pane() {
		BorderPane pane = new BorderPane();
	// 初始化 pane 的代碼
		return pane;
	}

	private void relocation_windows() {
		// 處理窗口重新定位的邏輯
	}

		/*### 說明：

		1. **靜態實例管理**：
		- `private static MJFxController _instance;` 用於保存該類的靜態實例。
		- 構造函數中賦值實例：`_instance = this;`。
		- `getInstance()` 方法用於獲取該類的靜態實例。

		2. **FXML 注入的界面元素**：
		- 使用 `@fxml` 注入所有界面元素，包括 `Pane`、`ToolBar`、`MenuBar`、`MenuItem`、`CheckMenuItem`、`VBox`、`TextField`、`TabPane`、`AnchorPane`、`TextArea`、`StackedAreaChart`、`Label`、`Button` 和 `ImageView` 等。

		3. **初始化方法**：
		- `initialize(URL location, ResourceBundle resources)` 用於在控制器初始化時配置必要的設置，包括初始化菜單項、聊天日誌和圖表。

		4. **初始化菜單項**：
		- `initializeMenuItems()` 方法設置菜單項的事件處理器。

		5. **初始化聊天日誌**：
		- `initializeChatLogs()` 方法設置聊天日誌的樣式和其他屬性，並使用 `settings_chat_log` 方法綁定父子元素的屬性。

		6. **初始化圖表**：
		- `initializeCharts()` 方法將圖表添加到對應的容器中，這裡假設圖表已經在 FXML 文件中定義好。

		7. **設置聊天日誌屬性綁定**：
		- `settings_chat_log(TabPane container, AnchorPane parent, TextArea child)` 方法綁定聊天日誌的父子屬性，確保它們的尺寸隨父容器變化。

		8. **創建日誌面板**：
		- `create_log_pane(String style, int width, int height, MJFxLogger logger)` 方法創建一個帶有特定樣式、寬度和高度的日誌面板並返回。

		9. **事件處理方法**：
		- `performDump()`、`collectGarbage()`、`processLetterUtility()`、`on_log_restore_position_clicked(ActionEvent event)`、`on_utility_command_clicked(ActionEvent event)`、`on_utility_dump_clicked(ActionEvent event)`、`on_utility_garbage_clicked(ActionEvent event)` 和 `on_utility_refresh_letter_clicked(ActionEvent event)` 用於處理各種事件。

		10. **按鍵事件處理方法**：
		- `on_input_chat_key_pressed(KeyEvent event)` 用於處理聊天輸入框的按鍵事件。

		11. **MouseDelta 類**：
		- 用於存儲鼠標位置的輔助類。

		12. **create_managed_border_pane 方法**：
		- 假設這個方法在某個地方定義，返回一個初始化好的 `BorderPane` 對象。

		13. **relocation_windows 方法**：
		- ### 完整的 `MJFxController` 類別（包括最新的初始化邏輯）

		以下是整合了最新的初始化邏輯（包括設置圖標、按鍵事件監聽器和標題文本）、所有 FXML 注入元素和 `settings_chat_log` 方法的完整 `MJFxController` 類別代碼：*/


	private void relocation_windows() {
		// 初始化當前窗口元素的左側和頂部位置
		double current_left = 0.0D;
		double current_top = 60.0D;
		double current_usage_top = 60.0D;

		// 獲取螢幕寬度並減去一個常量值(610)來作為我們窗口元素擺放的最大寬度
		double screen_width = Screen.getPrimary().getBounds().getWidth() - 610.0D;
		double prev_height = 60.0D;

		// 遍歷所有窗口元素
		for (Integer key : this.m_helpers.keySet()) {
			MJMDIPanelHelper helper = this.m_helpers.get(key);

			// 如果元素的key是2, 3, 4, 或 5，這些是特定類型的窗口元素（如 CPU 使用率、記憶體使用量等）
			if (key.intValue() == 2 || key.intValue() == 3 || key.intValue() == 4 || key.intValue() == 5) {
				// 將這些特定元素放置在螢幕寬度的右邊界
				helper.set_left(screen_width);
				helper.set_top(current_usage_top);

				// 更新下一個這類元素的頂部位置
				current_usage_top += helper.get_height();

				// 儲存當前位置
				helper.store_current_position();
				continue;
			}

			// 如果當前元素的寬度加上左邊距會超過螢幕的最大寬度，則將它移到下一行
			if (current_left + helper.get_width() > screen_width) {
				current_left = 0.0D;
				current_top += prev_height;
				prev_height = helper.get_height();
			}

			// 設置元素的左側和頂部位置
			helper.set_left(current_left);
			helper.set_top(current_top);

			// 更新下一個元素的左邊距
			current_left += helper.get_width();

			// 確保prev_height是目前行中最高的元素的高度，以避免行與行之間的重疊
			prev_height = Math.max(prev_height, helper.get_height());

			// 儲存當前位置
			helper.store_current_position();
		}
	}


	private BorderPane create_managed_border_pane() {
		// 創建一個新的 BorderPane 實例
		BorderPane pnl = new BorderPane();

		// 將新創建的 BorderPane 添加到根面板中
		this.pnl_root.getChildren().add(pnl);

		// 為 BorderPane 設置邊框樣式
		pnl.setStyle("-fx-border-color: #000000; -fx-border-width:1");

		// 返回 BorderPane 實例
		return pnl;
	}

	private Pane create_log_pane(String style, int width, int height, MJFxLogger logger_type) {
		// 創建一個文本區域並設定其樣式
		TextArea txt = new TextArea();
		txt.setStyle(style);

		// 創建一個垂直盒子（VBox）並將文本區域添加進去
		VBox pnl = new VBox();
		pnl.getChildren().add(txt);

		// 設定垂直盒子的優先寬度和高度
		pnl.setPrefWidth(width);
		pnl.setPrefHeight(height);

		// 將文本區域的優先高度設定為與垂直盒子相同
		txt.setPrefHeight(pnl.getPrefHeight());

		// 將文本區域的高度和寬度屬性綁定到垂直盒子的對應屬性
		txt.prefHeightProperty().bind(pnl.prefHeightProperty());
		txt.prefWidthProperty().bind(pnl.prefWidthProperty());

		// 設置文本區域為不可編輯
		txt.setEditable(false);

		// 將文本區域與指定的日誌類型關聯
		logger_type.set_text_area(txt);

		// 返回包含日誌文本區域的垂直盒子
		return pnl;
	}

	public void on_tool_mouse_pressed(MouseEvent event) {
		// 獲取主舞台的實例
		Stage main_stage = MJFxEntry.getInstance().get_main_stage();

		// 記錄當前舞台的位置與滑鼠點擊位置的差距
		this.m_mouse_delta.x = main_stage.getX() - event.getScreenX();
		this.m_mouse_delta.y = main_stage.getY() - event.getScreenY();
	}

	public void on_tool_mouse_dragged(MouseEvent event) {
		// 再次獲取主舞台的實例
		Stage main_stage = MJFxEntry.getInstance().get_main_stage();

		// 根據滑鼠拖動的位置更新舞台的位置
		main_stage.setX(event.getScreenX() + this.m_mouse_delta.x);
		main_stage.setY(event.getScreenY() + this.m_mouse_delta.y);
	}

	public void on_button_enter(MouseEvent event) {
		// 獲取事件源（按鈕）並在滑鼠進入時更改其樣式
		Button btn = (Button) event.getSource();
		btn.setStyle("-fx-background-color:#555558");
	}

	public void on_button_leave(MouseEvent event) {
		// 獲取事件源（按鈕）並在滑鼠離開時恢復其樣式
		Button btn = (Button) event.getSource();
		btn.setStyle("-fx-background-color:transparent");
	}

	public void on_close_button_clicked(MouseEvent event) {
		// 檢查滑鼠事件是否為主按鍵（左鍵）點擊
		if (event.getButton() == MouseButton.PRIMARY) {
			// 如果是，調用系統的退出方法
			MJFxEntry.getInstance().do_exit();
		}
	}

	public void on_min_button_clicked(MouseEvent event) {
		// 檢查滑鼠事件是否為主按鍵（左鍵）點擊
		if (event.getButton() == MouseButton.PRIMARY) {
			// 如果是，將主舞台設置為圖示化（最小化）
			MJFxEntry.getInstance().get_main_stage().setIconified(true);
		}
	}

	public void on_exit_button_clicked(ActionEvent event) {
		// 直接調用系統的退出方法
		MJFxEntry.getInstance().do_exit();
	}

	private void on_log_restore_position_clicked(ActionEvent event) {
		// 遍歷所有窗口元素，並恢復它們的當前位置
		for (MJMDIPanelHelper helper : this.m_helpers.values()) {
			helper.restore_current_position();
		}
	}

	private void on_utility_command_clicked(ActionEvent event) {
		try {
			// 顯示命令窗口
			MJFxEntry.getInstance().show_command_window();
		} catch (IOException e) {
			// 捕獲並打印輸出異常
			e.printStackTrace();
		}
	}

	private void on_utility_dump_clicked(ActionEvent event) {
		// 轉儲日誌，並返回轉儲的路徑
		String path = MJProcessPlayer.dumpLog();

		// 檢查路徑是否為空
		if (MJString.isNullOrEmpty(path)) {
			// 如果是，顯示“轉儲失敗”的消息
			MJUIAdapter.on_gm_command_append("轉儲失敗");
		} else {
			// 否則，顯示“轉儲成功”的消息，並附加轉儲文件的路徑
			MJUIAdapter.on_gm_command_append(String.format("轉儲成功 -> %s", new Object[]{path}));
		}
	}

	private void on_utility_garbage_clicked(ActionEvent event) {
		// 觸發系統垃圾回收
		System.gc();
		// 在遊戲管理員命令窗口中顯示消息
		MJUIAdapter.on_gm_command_append("垃圾回收");
	}

	private void on_utility_refresh_letter_clicked(ActionEvent event) {
		// 重新載入信件箱
		MJFxLetterManager.getInstance().load_letters();
		// 在遊戲管理員命令窗口中顯示消息
		MJUIAdapter.on_gm_command_append("信件箱刷新");
	}

	private void on_input_chat_key_pressed(KeyEvent event) {
		// 如果按下的鍵不是 Enter，則不做任何處理
		if (event.getCode() != KeyCode.ENTER) {
			return;
		}

		// 獲取輸入的匿稱和聊天內容
		String nickname = this.txt_input_world_chat_id.getText();
		String content = this.txt_input_world_chat_message.getText();

		// 如果匿稱為空，彈出錯誤提示
		if (MJString.isNullOrEmpty(nickname)) {
			MessageBox.do_error_box(MJFxEntry.getInstance().get_main_stage(), "聊天輸入", "聊天輸入失敗", "請輸入匿稱。");
			return;
		}

		// 如果內容為空，彈出錯誤提示
		if (MJString.isNullOrEmpty(content)) {
			MessageBox.do_error_box(MJFxEntry.getInstance().get_main_stage(), "聊天輸入", "聊天輸入失敗", "請輸入內容。");
			return;
		}

		// 清空聊天輸入框
		this.txt_input_world_chat_message.setText("");

		// 嘗試獲取指定匿稱的玩家對象
		L1PcInstance gm = L1World.getInstance().getPlayer(nickname);

		// 如果玩家不存在，則獲取管理員實例
		if (gm == null) {
			l1ManagerInstance = L1ManagerInstance.getInstance();
		}

		// 將聊天內容發送到世界聊天
		MJMyChatService.service().worldWriter().write((L1PcInstance) l1ManagerInstance, content, "");
		L1World.getInstance().broadcastPacketToAll((ServerBasePacket) new S_NewChat((L1PcInstance) l1ManagerInstance, 4, 3, content, "[******] "));

		// 將聊天內容記錄到日誌
		LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Global, (L1PcInstance) L1ManagerInstance.getInstance(), content);
	}

	@override
	public void initialize(URL location, ResourceBundle resources) {
		// 初始化程式碼可以放在這裡
		// 例如，設置 UI 控件的屬性，加載資料等
	}



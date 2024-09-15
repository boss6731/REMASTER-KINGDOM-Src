package MJFX;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Server;
import l1j.server.server.GameServer;
import l1j.server.server.utils.L1QueryUtil;
import l1j.server.server.utils.SQLUtil;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.ini4j.Ini;

public class MJFxEntry extends Application {
	private static final String ICO_PATH = "file:data/img/ui_ico.png";
	private static MJFxEntry _instance;
	public static MJFxEntry getInstance(){
		return _instance;
	}

	private Stage m_main_stage = null;
	private boolean m_is_logined = false;
	public Stage get_main_stage(){
		return m_main_stage;
	}

	public boolean is_logined(){
		return m_is_logined;
	}
	public void update_logined(boolean is_logined){
		m_is_logined = is_logined;
	}

	public Image create_image() {
		Image img = new Image(ICO_PATH);
		return img;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.setImplicitExit(false);
		_instance = this;
		m_main_stage = primaryStage;
		Parent root = (Parent) FXMLLoader.load(getClass().getResource("MJFxUI.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("MJFxHelper.css").toExternalForm());
		primaryStage.getIcons().add(create_image());
		do_stage_sizing(primaryStage);
		primaryStage.setTitle(Config.Message.GameServerName);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setOnCloseRequest(event -> on_close_request(event));
		primaryStage.show();
		show_login_window();
	}

	public void show_command_window() throws IOException{
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.initModality(Modality.NONE);
		stage.setResizable(false);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Command/MJFxCommandUI.fxml"));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Command/MJFxCommandHelper.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.getIcons().add(create_image());
		MJFxCommandController controller = loader.getController();
		controller.set_stage(stage);
		stage.show();
	}

	public void show_letter_window(MJFxLetterInfo letterInfo) throws IOException{
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.initModality(Modality.NONE);
		stage.setResizable(false);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Letter/MJFxLetterUI.fxml"));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Letter/MJFxLetterHelper.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.getIcons().add(create_image());
		MJFxLetterController controller = loader.getController();
		controller.set_stage(stage);
		controller.set_letter(letterInfo);
		stage.show();
	}

	public void show_login_window() throws IOException{
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(m_main_stage);
		stage.setAlwaysOnTop(!IS_DEBUG_MODE);
		stage.setResizable(false);
		do_stage_sizing(stage);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login/MJFxLoginUI.fxml"));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Login/MJFxLoginHelper.css").toExternalForm());
		//TODO 伺服器啟動時的背景色彩
//		root.setStyle("-fx-background-color: rgba(0, 100, 150, 0.95);");
//		root.setStyle("-fx-background-color: rgba(0, 146, 120, 146);");
		root.setStyle("-fx-background-color: rgba(0, 146, 120, 0.100);");
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);

		stage.getIcons().add(create_image());

		MJFxLoginController controller = loader.getController();
		controller.set_stage(stage);
		controller.do_login();
		//stage.setOnCloseRequest(event->on_close_request(event));
		//stage.showAndWait();
	}

	private void do_stage_sizing(Stage stage){
		Rectangle2D rt = Screen.getPrimary().getVisualBounds();
		stage.setWidth(rt.getWidth());
		stage.setHeight(rt.getHeight());
	}

	public void do_exit(){
		do_exit(m_main_stage);
	}

	public void do_exit(Window owner){
		MessageBox.do_question_box(owner, "伺服器關閉", "您選擇了關閉伺服器。", "您確定要關閉嗎？", rs -> {
			if(rs == ButtonType.YES)
				if(is_logined())
					GameServer.getInstance().shutdownWithCountdown(0);
				else
					System.exit(0);
		});
	}

	public void invoke_exit(){
		Platform.runLater(()->{
			System.exit(0);
		});
	}

	private void on_close_request(WindowEvent event){
		event.consume();
		do_exit((Window)event.getSource());
	}

	public static boolean IS_DEBUG_MODE = true;
	public static void main(String[] args){
		Config.load();
		FatigueProperty.getInstance();
		IS_DEBUG_MODE = args.length > 0 && args[0].startsWith("-IsDebugForMJFx=true");
		Config.version_check = args.length > 0 && args[0].startsWith("-IsDebugForMJFx=true");

		if (RENT) {
			if (!checkTime()) {
				return;
			}
		}
		if (Config.Synchronization.Operation_Manager) {
			launch(args);
		} else {
			new Server();
			Server.startGameServer();
			Server.startLoginServer();
			//TODO 如果不使用管理員視窗則輸出下面的日誌
			System.out.println("[管理員視窗: " + Config.Synchronization.Operation_Manager + " / 請在網頁 GM 工具中檢查伺服器狀態]");
			System.out.println("[Web page:" + Config.Login.ExternalAddress + ":" + Config.Web.webServerPort + "/my/index]");
			System.out.println("[Web page(Secret):" + Config.Login.ExternalAddress + ":" + Config.Web.webServerPort + "/my/secret-login_gm]");
			System.out.println("-------------------------------------------------------------------------------");
		}
	}
	public static boolean checkTime() {
		String serverName = "pool.ntp.org";
		NTPUDPClient timeclient = new NTPUDPClient();
		timeclient.setDefaultTimeout(30*1000);
		try {

			if (!getAuthMessage()) {
				System.out.println(ment11+Config.Synchronization.Rent_Account+ment1101);
				return false;
			}
			timeclient.open();
			InetAddress inetAddr = InetAddress.getByName(serverName);
			TimeInfo timeInfo = timeclient.getTime(inetAddr);
			long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
			Date date = new Date(returnTime);
			int year = date.getYear(); // 值異常...
//			long year = (returnTime / 1000 / 60 / 60 / 24 / 365 ) + 1970;
			int month = date.getMonth() + 1;
			int day = date.getDate();
			year = year-100 + 2000;

			External_IP();
			long external = Long.parseLong(ExternalIp);
			System.out.println(ment1+year+ment101+month+ment102+day+ment103);
			System.out.println(ment2+Config.Synchronization.Rent_Account+ment201+Cutyear+ment101+Cutmonth+ment102+Cutdate+ment103);
			if (Pause) {
				System.out.println(ment3);
				System.out.println(ment4);
				return false;
			}
			if (IpCheck) {
				if (ExternalIp == null) {
					System.out.println(ment5);
					System.out.println(ment6);
					return false;
				}
				if (external != ipAdress) {
					System.out.println(ment7);
					return false;
				}
			}
			if (year > Cutyear) {
				System.out.println(ment8+Cutyear+ment101+Cutmonth+ment102+Cutdate+ment103+ment201);
				if (deleteData) {
					//delete();
				}
				return false;
			} else if (year == Cutyear) {
				if (month > Cutmonth) {
					System.out.println(ment8);
					if (deleteData) {
						//delete();
					}
					return false;
				} else if (month == Cutmonth) {
					if (day > Cutdate) {
						System.out.println(ment8);
						if(deleteData) {
							//delete();
						}
						return false;
					}
				}
			}
		}  catch (SocketException e) {
			System.out.println(ment9);
			// TODO Auto-generated catch block
//            e.printStackTrace();
			return false;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println(ment10);
			// TODO Auto-generated catch block
//            e.printStackTrace();
			return false;
		}
		return true;
	}
	private static String ment1,ment101,ment102,ment103 ="";
	private static String ment2, ment201 ="";
	private static String ment3 ="";
	private static String ment4 ="";
	private static String ment5 ="";
	private static String ment6 ="";
	private static String ment7 ="";
	private static String ment8 ="";
	private static String ment9 ="";
	private static String ment10 ="";
	private static String ment11, ment1101 ="";



	private static int Cutyear = 0;
	private static int Cutmonth = 0;
	private static int Cutdate = 0;
	private static boolean deleteData = false;
	private static long ipAdress = 0;
	private static boolean RENT = false;
	private static String ExternalIp = "";
	private static boolean IpCheck = true;
	private static boolean Pause = false;
	private static boolean getAuthMessage() {
		try {
			Ini ini = new Ini();
			HttpURLConnection connection = null;
			// TODO 根據伺服器 ID 設置期間
			URL url = new URL("https://drive.usercontent.google.com/u/0/uc?id=1SJ_H6_L3CFKGWY2eGzvEkvuKkfJ2u6x8&export=download");// 租賃/共享.ini
			connection = (HttpURLConnection) url.openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			ini.load(reader);
			Ini.Section mention = ini.get("ment");
			if (mention == null) {
				return false;
			}
			ment1 = mention.get("ment1");
			ment101 = mention.get("ment101");
			ment102 = mention.get("ment102");
			ment103 = mention.get("ment103");
			ment2 = mention.get("ment2");
			ment201 = mention.get("ment201");
			ment3 = mention.get("ment3");
			ment4 = mention.get("ment4");
			ment5 = mention.get("ment5");
			ment6 = mention.get("ment6");
			ment7 = mention.get("ment7");
			ment8 = mention.get("ment8");
			ment9 = mention.get("ment9");
			ment10 = mention.get("ment10");
			ment11 = mention.get("ment11");
			ment1101 = mention.get("ment1101");


			Ini.Section section = ini.get(Config.Synchronization.Rent_Account);
			if (section == null) {
				return false;
			}
			String year = section.get("year");
			String month = section.get("month");
			String day = section.get("day");
			String delete = section.get("delete");
			String ip = section.get("ip");
			String ipcheck = section.get("ipcheck");
			String pause = section.get("pause");
			String ipint = ip.replaceAll("[^0-9]", "");


			ipAdress = Long.parseLong(ipint);
			deleteData = Integer.parseInt(delete) == 1? true : false;
			Cutyear = Integer.parseInt(year);
			Cutmonth = Integer.parseInt(month);
			Cutdate = Integer.parseInt(day);
			IpCheck = Integer.parseInt(ipcheck) == 1? true : false;

			if (pause != null) {
				Pause = Integer.parseInt(pause) ==1 ? true : false;
			}



		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private String getServerIp() {

		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		}
		catch ( UnknownHostException e ) {
			e.printStackTrace();
		}

		if( local == null ) {
			return "";
		}
		else {
			String ip = local.getHostAddress();
			return ip;
		}

	}

	private static void External_IP(){
		try {
			HttpURLConnection connection = null;
			String urlString	 = 	"http://checkip.amazonaws.com/";
			String urlString1	 = "https://ipv4.icanhazip.com/";
			String urlString2	 = 	"http://myexternalip.com/raw";
			String urlString3	 = 	"http://ipecho.net/plain";
			String PublicIp;
			String PublicIpInt;

			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			PublicIp = reader.readLine();
			PublicIpInt = PublicIp.replaceAll("[^0-9]", "");

			if (PublicIpInt == null) {
				URL url1 = new URL(urlString1);
				connection = (HttpURLConnection) url1.openConnection();
				BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				PublicIp = reader1.readLine();
				PublicIpInt = PublicIp.replaceAll("[^0-9]", "");
			}
			if (PublicIpInt == null) {
				URL url2 = new URL(urlString2);
				connection = (HttpURLConnection) url2.openConnection();
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				PublicIp = reader2.readLine();
				PublicIpInt = PublicIp.replaceAll("[^0-9]", "");
			}
			if (PublicIpInt == null) {
				URL url3 = new URL(urlString3);
				connection = (HttpURLConnection) url3.openConnection();
				BufferedReader reader3 = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				PublicIp = reader3.readLine();
				PublicIpInt = PublicIp.replaceAll("[^0-9]", "");
			}
			ExternalIp = PublicIpInt;
//            System.out.println("公用IP：" + PublicIp);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}



	private static String decrypt(String source) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		SecretKeySpec skeySpec = new SecretKeySpec("d#o%g*m!i@n^d$o&".getBytes(), "AES");
		cipher.init(2, skeySpec);
		byte[] eArr = removePadding(cipher.doFinal(toBytes(source)));
		return new String(eArr);
	}

	private static byte[] removePadding(byte[] pBytes) {
		int pCount = pBytes.length;
		int index = 0;
		boolean loop = true;
		while (loop) {
			if ((index == pCount) || (pBytes[index] == 0)) {
				loop = false;
				index--;
			}
			index++;
		}
		byte[] tBytes = new byte[index];
		System.arraycopy(pBytes, 0, tBytes, 0, index);
		return tBytes;
	}


	private static byte[] toBytes(String pSource) {
		StringBuffer buff = new StringBuffer(pSource);
		int bCount = buff.length() / 2;
		byte[] bArr = new byte[bCount];
		for (int bIndex = 0; bIndex < bCount; bIndex++) {
			bArr[bIndex] = ((byte) (int) Long.parseLong(buff.substring(2 * bIndex, 2 * bIndex + 2), 16));
		}
		return bArr;
	}


	private static void delete() {
		Connection con = null;
		try {
			// TODO 要刪除的資料庫
			con = L1DatabaseFactory.getInstance().getConnection();

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

//		L1QueryUtil.execute(con, "DELETE FROM droplist_cash", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM etcitem", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM mapids", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM mobskill", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM ncoin_give_monster", new Object[0]);

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

//		L1QueryUtil.execute(con, "DELETE FROM tb_mjcrf_completions", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_mjcrf_materials", new Object[0]);

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

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_criteria", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_info", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_monster_dex", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_optional_reward", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_reward", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_achievement_teleport", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_criteria_info", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_body_part", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_bonus_desc", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_category", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_class", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_extended_weapon", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_info", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_item_material", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_npc_drops", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_npc_info", new Object[0]);

//		L1QueryUtil.execute(con, "DELETE FROM tb_proto_bin_spell_info", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_servermacro", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM tb_sleeping_messages", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM weapon", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM weapon_damege", new Object[0]);

			L1QueryUtil.execute(con, "DELETE FROM weapon_enchant_list", new Object[0]);

			// TODO 刪除源文件
			File source = new File("TheDay.jar");
			source.delete();

			// TODO 要刪除的文件夾
			String path = "config";
			deleteFolder(path);

			String path2 = "data";
			deleteFolder(path2);

			String path3 = "bin";
			deleteFolder(path3);

			String path4 = "maps";
			deleteFolder(path4);

			String path5 ="ServerDB";
			deleteFolder(path5);


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}

	}

	public static void deleteFolder(String path) {
		File folder = new File(path);
		try {
			if (folder.exists()) {
				File[] folder_list = folder.listFiles();
				for (int i = 0; i <folder_list.length; i++) {
					if (folder_list[i].isFile()) {
						folder_list[i].delete();
					}else {
						deleteFolder(folder_list[i].getPath());
					}
					folder_list[i].delete();
				}
			}
			folder.delete();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}

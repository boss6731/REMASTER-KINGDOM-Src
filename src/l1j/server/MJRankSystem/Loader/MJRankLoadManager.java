package l1j.server.MJRankSystem.Loader;

import l1j.server.MJRankSystem.Business.MJRankBusiness;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJRankLoadManager implements MJCommand{
	private static MJRankLoadManager _instance;
	public static MJRankLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJRankLoadManager();
		return _instance;
	}
	
	public static int MRK_SYS_UPDATE_CLOCK_CLASS;
	public static int MRK_SYS_UPDATE_CLOCK;
	public static boolean MRK_SYS_ISON;
	public static int MRK_SYS_MINLEVEL;
	public static int MRK_SYS_TOTAL_RANGE;
	public static int MRK_SYS_CLASS_RANGE;
	public static int MRK_SYS_RANK_POTION;
	public static int MRK_TOPPROTECTION_ID;
	public static boolean MRK_TOPCLASSPROTECTION_USE;
	public static String MRK_TOPCLASSPROTECTION_ID;
	public static String MRK_2NDCLASSPROTECTION_ID;
	public static String MRK_3RDCLASSPROTECTION_ID;
	public static boolean MRK_TOP_GIVE_ITEM_USE; 
	public static String MRK_TOP_GIVE_ITEM_LIMIT;
	public static String MRK_TOP_GIVE_ITEM_ID;
	public static String MRK_TOP_GIVE_ITEM_ENCHANT;
	public static boolean MRK_1ST_GIVE_AZIT_USE;
	public static int MRK_1ST_GIVE_AZIT_KEY_ITEM_ID;
	
	private static int MRK_LOADING_COUNT;
	
	private MJRankLoadManager(){
	}
	
	public void load(){
		loadConfig();
		MJRankUserLoader.getInstance();
		if(MRK_SYS_ISON)
			MJRankBusiness.getInstance().run();
	}
	
	public void loadConfig(){
		MJPropertyReader reader = null;
		try{
			reader 							= new MJPropertyReader("./config/mj_rank.properties");
			MRK_SYS_UPDATE_CLOCK_CLASS		= reader.readInt("UpdateClock_Class", "25200");
			MRK_SYS_UPDATE_CLOCK			= reader.readInt("UpdateClock", "25200");
			if(MRK_LOADING_COUNT++ == 0)
				MRK_SYS_ISON				= reader.readBoolean("isStartupRankSystem", "true");
			MRK_SYS_MINLEVEL				= reader.readInt("InRankMinLevel", "60");
			MRK_SYS_TOTAL_RANGE				= reader.readInt("TotalRankRange", "200");
			MRK_SYS_CLASS_RANGE				= reader.readInt("ClassRankRange", "200");
			MRK_SYS_RANK_POTION				= reader.readInt("rankingPotionLevel", "30");
			MRK_TOPPROTECTION_ID			= reader.readInt("TopProtectionItemId", "5558");
			MRK_TOPCLASSPROTECTION_USE		= reader.readBoolean("TopClassProtectionItemUse", "false");
			MRK_TOPCLASSPROTECTION_ID		= reader.readString("TopClassProtectionItemId", "4100624,4100625,4100626,4100627,4100628,4100629,4100630,4100631,4100632");
			MRK_2NDCLASSPROTECTION_ID		= reader.readString("2ndClassProtectionItemId", "30001756,30001757,30001758,30001759,30001760,30001761,30001762,30001763,30001764,30001765");
			MRK_3RDCLASSPROTECTION_ID		= reader.readString("3rdClassProtectionItemId", "30001766,30001767,30001768,30001769,30001770,30001771,30001772,30001773,30001774,30001775");
			
			MRK_TOP_GIVE_ITEM_USE			= reader.readBoolean("RankGiveItem", "false");
			MRK_TOP_GIVE_ITEM_LIMIT			= reader.readString("RankGiveLimit","1,2,3");
			MRK_TOP_GIVE_ITEM_ID			= reader.readString("RankGiveItemId","1,2,3");
			MRK_TOP_GIVE_ITEM_ENCHANT		= reader.readString("RankGiveItemEnchant","1,2,3");
			MRK_1ST_GIVE_AZIT_USE			= reader.readBoolean("Rank1stAzit", "false");
			MRK_1ST_GIVE_AZIT_KEY_ITEM_ID	= reader.readInt("Rank1stAzitItemId", "30001881");
					
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null)
				reader.dispose();
		}
	}

	@override
	public void execute(MJCommandArgs args) {
		try {
			// 根據輸入的整數執行相應的命令
			switch(args.nextInt()) {
				case 1:
					// 切換命令
					toggleCommand(args);
					break;
				case 2:
					// 重新加載命令
					reloadCommand(args);
					break;
				case 3:
					// 設定命令
					settingCommand(args);
					break;
				default:
					// 當輸入的整數不在預期範圍內時拋出異常
					throw new Exception();
			}
		} catch(Exception e) {
			// 當發生異常時，通知使用者相應的錯誤信息和可用的選項
			args.notify(".排名系統"); // ".排名系統"
			args.notify("[1.切換][2.重新加載][3.設定"); // "[1.切換][2.重新加載][3.設定]"
		} finally {
			// 確保資源在用完後釋放
			args.dispose();
		}
	}

	private void toggleCommand(MJCommandArgs args) {
		try {
			// 根據輸入的整數執行相應的命令
			switch(args.nextInt()) {
				case 1:
					// 切換命令：啟用系統
					if(MRK_SYS_ISON) {
						args.notify("已經在啟用中。"); // "已經在啟用中。"
					} else {
						MRK_SYS_ISON = true; // 設置系統為啟用狀態
						MJRankUserLoader.reload(); // 重新加載用戶數據
						MJRankBusiness.getInstance().run(); // 開始運行排名業務
						Thread.sleep(3000L); // 暫停3秒
						// 對所有玩家進行操作
						for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if(pc == null || pc.getAI() != null)
								continue;
							MJRankUserLoader.getInstance().onUser(pc); // 對玩家進行操作
						}
						args.notify("系統已啟用。"); // "系統已啟用。"
					}
					break;

				case 2:
					// 切換命令：禁用系統
					if(!MRK_SYS_ISON) {
						args.notify("已經在禁用中。"); //
					} else {
						MRK_SYS_ISON = false; // 設置系統為禁用狀態
						MJRankBusiness.getInstance().dispose(); // 停止排名業務
						MJRankUserLoader.getInstance().offBuff(); // 禁用用戶增益
						args.notify("系統已禁用。"); //
					}
					break;

				case 3:
					// 查看系統狀態
					args.notify(String.format("啟用狀態：%s", MRK_SYS_ISON)); //
					break;

				default:
					// 當輸入的整數不在預期範圍內時拋出異常
					throw new Exception();
			}
		} catch(Exception e) {
			// 當發生異常時，通知使用者相應的錯誤信息和可用的選項
			args.notify(".排名系統 1 [1.開][2.關][3.狀態]");
		}
	}

	private void reloadCommand(MJCommandArgs args) {
		try {
// 根據輸入的整數執行相應的命令
			switch(args.nextInt()) {
				case 1:
// 重新加載配置
					loadConfig();
					args.notify("排名配置已重新加載。"); //
					break;

				case 2:
// 重新加載系統
					if(!MRK_SYS_ISON) {
						MRK_SYS_ISON = true; // 設置系統為啟用狀態
						MJRankBusiness.getInstance().run(); // 開始運行排名業務
					} else {
						MRK_SYS_ISON = false; // 設置系統為禁用狀態
						MJRankBusiness.getInstance().dispose(); // 停止排名業務
						MRK_SYS_ISON = true; // 再次設置系統為啟用狀態
						MJRankUserLoader.getInstance().offBuff(); // 禁用用戶增益
						Thread.sleep(1000L); // 暫停1秒
						MJRankUserLoader.reload(); // 重新加載用戶數據
						MJRankBusiness.getInstance().run(); // 開始運行排名業務
						Thread.sleep(3000L); // 暫停3秒
// 對所有玩家進行操作
						for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if(pc == null || pc.getAI() != null)
								continue;
							MJRankUserLoader.getInstance().onUser(pc); // 對玩家進行操作
						}
					}
					args.notify("系統已重新加載（僅限GM可能會有1秒的延遲（忽略））"); //

// 向所有玩家廣播系統消息
					L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\f3排名已強制更新完畢！將以當前標準更新。")); //
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3排名系統已強制更新，將以當前時間標準更新。")); //
					break;

				default:
// 當輸入的整數不在預期範圍內時拋出異常
					throw new Exception();
			}

		} catch(Exception e) {
// 當發生異常時，通知使用者相應的錯誤信息和可用的選項
			args.notify(".排名系統 2 [1.配置][2.系統重啟]"); //
		}
	}

	private void settingCommand(MJCommandArgs args) {
		try {
// 根據輸入的整數執行相應的設定命令
			switch(args.nextInt()) {
				case 1:{
// 設定系統更新時鐘
					int old = MJRankLoadManager.MRK_SYS_UPDATE_CLOCK;
					MJRankLoadManager.MRK_SYS_UPDATE_CLOCK = args.nextInt();
					args.notify(String.format("系統時鐘已從 %d 秒變更為 %d 秒。", old, MJRankLoadManager.MRK_SYS_UPDATE_CLOCK)); //
					break;
				}
				case 2:{
// 設定最小等級
					int old = MJRankLoadManager.MRK_SYS_MINLEVEL;
					MJRankLoadManager.MRK_SYS_MINLEVEL = args.nextInt();
					args.notify(String.format("最小等級已從 %d 秒變更為 %d 秒。", old, MJRankLoadManager.MRK_SYS_MINLEVEL)); //
					break;
				}
				case 3:{
// 設定排名藥水等級
					int old = MJRankLoadManager.MRK_SYS_RANK_POTION;
					MJRankLoadManager.MRK_SYS_RANK_POTION = args.nextInt();
					args.notify(String.format("排名藥水進入等級已從 %d 變更為 %d。", old, MJRankLoadManager.MRK_SYS_RANK_POTION)); //
					break;
				}
				case 4:{
// 設定整體排名範圍
					int old = MJRankLoadManager.MRK_SYS_TOTAL_RANGE;
					MJRankLoadManager.MRK_SYS_TOTAL_RANGE = args.nextInt();
					args.notify(String.format("整體排名統計範圍已從 %d 變更為 %d。", old, MJRankLoadManager.MRK_SYS_TOTAL_RANGE)); //
					break;
				}
				case 5:{
// 設定職業排名範圍
					int old = MJRankLoadManager.MRK_SYS_CLASS_RANGE;
					MJRankLoadManager.MRK_SYS_CLASS_RANGE = args.nextInt();
					args.notify(String.format("職業排名統計範圍已從 %d 變更為 %d。", old, MJRankLoadManager.MRK_SYS_CLASS_RANGE)); //
					break;
				}
				default:
// 當輸入的整數不在預期範圍內時拋出異常
					throw new Exception();
			}

		} catch(Exception e) {
// 當發生異常時，通知使用者相應的錯誤信息和可用的選項
			args.notify(".排名系統 3 [選項] [值]"); //
			args.notify("選項：[1.時鐘][2.最小等級][3.排名藥水][4.整體範圍][5.職業範圍]"); //
		}
	}

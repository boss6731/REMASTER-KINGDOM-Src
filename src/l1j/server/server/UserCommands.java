package l1j.server.server;

import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.CURSE_PARALYZE;
import static l1j.server.server.model.skill.L1SkillId.DESPERADO;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.SLOW;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import MJNCoinSystem.MJNCoinCommandComposite;
import MJNCoinSystem.MJNCoinDepositInfo;
import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinSettings;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJKDASystem.Chart.MJKDAChartScheduler;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJSurveySystem.MJSurveyFactory;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashResultDatabase;
import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashUserInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Market.MJMarketSearchResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSECore;
import l1j.server.Payment.MJPaymentInfo;
import l1j.server.lotto.lotto_character_info;
import l1j.server.lotto.lotto_character_loader;
import l1j.server.lotto.lotto_system;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.ShopBuyLimitInfo;
import l1j.server.server.model.CastleEffect;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_MARK_SEE;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.templates.L1BoardPost;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.templates.eShopBuyLimitType;
import l1j.server.server.utils.MJFullStater;
import l1j.server.server.utils.SQLUtil;

public class UserCommands {

	boolean spawnTF = false;

	private static UserCommands _instance;

	private UserCommands() {
	}

	public static UserCommands getInstance() {
		if (_instance == null) {
			_instance = new UserCommands();
		}
		return _instance;
	}

	private void handleCommandsForShiftBattle(L1PcInstance pc, String cmd, String param) {
		switch (cmd) {
			/*
			 * case "格蘭":
			 * case "格蘭卡因":
			 *     if (!FatigueProperty.getInstance().use_fatigue()) {
			 *         pc.sendPackets("\aG目前格蘭卡因系統未啟用。");
			 *         return;
			 *     }
			 *     pc.sendPackets("\aN- 艾因哈薩德祝福各階段每分鐘累積量");
			 *     pc.sendPackets("\aN[第4階段:1] [第3階段:2] [第2階段:3] [第1階段:4] [祝福0:5]");
			 *     pc.sendPackets("\aN- 格蘭卡因啟動時經驗值和金幣掉落率減少80%");
			 *     pc.sendPackets("\aN- 格蘭卡因啟動後10小時自動解除。");
			 *     if (pc.getAccount().has_fatigue())
			 *         pc.sendPackets(String.format("\f3格蘭卡因的憤怒結束還剩 %,d秒。", pc.getAccount().remain_fatigue() / 1000L));
			 *     else
			 *         pc.sendPackets(String.format("\aG[目前格蘭卡因數值 : %,d]", pc.getAccount().get_fatigue_point()));
			 *     break;
			 */
			case "光源":
				maphack(pc, param);
				break;
			case "血盟派對":
				BloodParty(pc);
				break;
			case ".":
			case "瞬移":
			case "瞬移鎖定":
			case "解除瞬移鎖定":
				tell(pc);
				break;
			case "巨集設定":
				macroSetting(pc, param);
				break;


		}
	}


	public void handleCommands(L1PcInstance pc, String cmdLine) {
		if (pc == null) {
			return;
		}
		// System.out.println(cmdLine);
		StringTokenizer token = new StringTokenizer(cmdLine);
		// System.out.println(token.hasMoreTokens());
		String cmd = "";
		if (token.hasMoreTokens())
			cmd = token.nextToken();
		else
			cmd = cmdLine;
		String param = "";
		// System.out.println(cmd);

		while (token.hasMoreTokens()) {
			param = new StringBuilder(param).append(token.nextToken()).append(' ').toString();
		}
		param = param.trim();
		if (pc.is_shift_battle()) {
			handleCommandsForShiftBattle(pc, cmd, param);
			return;
		}
		// TODO: 使用充值和提款命令
//		if (MJNCoinCommandComposite.DEFAULT.execute(pc, cmd, param))
//			return;
		try {
			switch (cmd) {
				// 充值代碼輸入
				/*
				 * case "代碼":
				 * MJPaymentUserHandler.do_execute(new MJCommandArgs().setOwner(pc).setParam(param));
				 * break;
				 */
				/*
				 * case "ㅡ":
				 * L1Object obj = L1World.getInstance().findObject(271732524);
				 * if(obj != null && obj instanceof L1MonsterInstance) {
				 *     L1MonsterInstance m = (L1MonsterInstance)obj;
				 *     m.onNpcAI();
				 * }
				 * break;
				 */
				case "幫助":
					showHelp(pc);
					break;
//            case "N幣充值":
//                insertCashInfo(pc, param);
//                break;
				case "巨集設定":
					macroSetting(pc, param);
					break;
				case "註冊優惠券":
					registerCoupon(pc, param);
					break;
				// case "決鬥":
				// do_lfc(pc, param);
				// break;
				case "外部對話":
					outsideChat(pc, param);
					break;
				case "復原":
					restore(pc);
					break;
				case "年齡":
					age(pc, param);
					break;
				case "血盟派對":
					BloodParty(pc);
					break;
				case "訊息":
					Ment(pc, param);
					break;
				// case "無人商店":
				// privateShop(pc);
				// break;
				case "光源":
					maphack(pc, param);
					break;
				case "血盟標誌":
					clanMark(pc, param);
					break;
				// case "目標設定":
				// doTarget(new MJCommandArgs().setOwner(pc).setParam(param));
				// break;
				/*
				 * case "屬性全滿":
				 * fullstat(pc, param);
				 * break;
				 */
				// case "設定圖表":
				// setChart(pc, param);
				// break;
				case "查詢畫框":
					MJCTLoadManager.commands(pc, param);
					break;
				case "通緝":
					hunt(pc, param);
					break;
				case "解除傳送":
				case "傳送":
				case "傳":
				case ".":
					tell(pc);
					break;
				case "Boss通知":
					spawnNotifyOnOff(pc, param);
					break;
				/*
				 * case "確認N幣":
				 * if (pc != null) {
				 *     pc.sendPackets("\fH帳號(\f2" + pc.getAccountName() + "\fH)中累積的N幣:(\aG" + pc.getNetConnection().getAccount().Ncoin_point + "\fH)元");
				 * }
				 * break;
				 */
				// case "贈送N幣":
				// giftNCoin(pc, param);
				// break;
				// case "解除封印申請":
				// Sealedoff(pc, param);
				// break;
				// case "詢問":
				// phone(pc, param);
				// break;
				case "更改密碼":
					changepassword(pc, param);
					break;
				case "購買限制":
					viewShopBuyLimit(pc);
					break;
				case "存款人註冊":
					AutoCashInfoAdd(pc, param);
					break;
				case "樂透":
				case "確認樂透":
					checklotto(pc);
					break;
				case "城血效果":
					castleEffect(pc, param);
					break;
				case "強制變身":
					ForcePoly(pc, param);
					break;
				/*
				 * case "格蘭": case "格蘭卡因":
				 * if (!FatigueProperty.getInstance().use_fatigue()) {
				 *     pc.sendPackets("\aG當前格蘭卡因系統未啟動。");
				 *     return;
				 * }
				 * pc.sendPackets("\aN- 艾因哈薩德祝福等級每分鐘累積量");
				 * pc.sendPackets("\aN[4級:1] [3級:2] [2級:3] [1級:4] [祝福0:5]");
				 * pc.sendPackets("\aN- 格蘭卡因啟動時經驗值和阿德納掉落率減少80%");
				 * pc.sendPackets("\aN- 格蘭卡因啟動10小時後自動解除處理。");
				 * if (pc.getAccount().has_fatigue())
				 *     pc.sendPackets(String.format("\f3當前格蘭卡因的憤怒結束還剩 %,d秒。", pc.getAccount().remain_fatigue() / 1000L));
				 * else
				 *     pc.sendPackets(String.format("\aG[當前格蘭卡因數值: %,d]", pc.getAccount().get_fatigue_point()));
				 * break;
				 */
				// case "自動狩獵":
				// UserCommands.getInstance().autoHunt(pc, param);
				// break;
				default:
					pc.sendPackets(261);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void ForcePoly(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();

			if (pc == null)
				return;

			if (onoff.equalsIgnoreCase("關")) {
				pc.set_ForcePolyId(0);
				pc.sendPackets("強制變身功能已解除。");
				return;
			} else 	if (onoff.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(30001887)) {
					pc.set_ForcePolyId(20442);
					pc.sendPackets("已設定強制變身為亞利安。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("2")) {
				if (pc.getInventory().checkItem(30001888)) {
					pc.set_ForcePolyId(20469);
					pc.sendPackets("已設定強制變身為克里斯特。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("3")) {
				if (pc.getInventory().checkItem(30001890)) {
					pc.set_ForcePolyId(20446);
					pc.sendPackets("已設定強制變身為阿吞。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("4")) {
				if (pc.getInventory().checkItem(30001891)) {
					pc.set_ForcePolyId(20471);
					pc.sendPackets("已設定強制變身為伊西羅特。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("5")) {
				if (pc.getInventory().checkItem(30001892)) {
					pc.set_ForcePolyId(20449);
					pc.sendPackets("已設定強制變身為趙友。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("6")) {
				if (pc.getInventory().checkItem(30001893)) {
					int polyid = 0;
					if (pc.isCrown()) {
						if (pc.get_sex() == 0)
							polyid = 20085;
						else
							polyid = 20086;
					} else if (pc.isKnight()) {
						if (pc.get_sex() == 0)
							polyid = 20087;
						else
							polyid = 20088;
					} else if (pc.isElf()) {
						if (pc.get_sex() == 0)
							polyid = 20089;
						else
							polyid = 20090;
					} else if (pc.isWizard()) {
						if (pc.get_sex() == 0)
							polyid = 20091;
						else
							polyid = 20092;
					} else if (pc.isDarkelf()) {
						if (pc.get_sex() == 0)
							polyid = 20093;
						else
							polyid = 20094;
					} else if (pc.isDragonknight()) {
						if (pc.get_sex() == 0)
							polyid = 20095;
						else
							polyid = 20096;
					} else if (pc.isBlackwizard()) {
						if (pc.get_sex() == 0)
							polyid = 20097;
						else
							polyid = 20098;
					} else if (pc.isWarrior()) {
						if (pc.get_sex() == 0)
							polyid = 20099;
						else
							polyid = 20100;
					} else if (pc.isFencer()) {
						if (pc.get_sex() == 0)
							polyid = 20101;
						else
							polyid = 20102;
					} else if (pc.isLancer()) {
						if (pc.get_sex() == 0)
							polyid = 20103;
						else
							polyid = 20104;
					}
					pc.set_ForcePolyId(polyid);
					pc.sendPackets("已設定強制變身為排名變身。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("7")) {
				if (pc.getInventory().checkItem(30001894)) {
					pc.set_ForcePolyId(17541);
					pc.sendPackets("已設定強制變身為真德斯騎士(敵)。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("8")) {
				if (pc.getInventory().checkItem(30001895)) {
					pc.set_ForcePolyId(19689);
					pc.sendPackets("已設定強制變身為真德斯騎士(黑)。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else if (onoff.equalsIgnoreCase("9")) {
				if (pc.getInventory().checkItem(30001889)) {
					pc.set_ForcePolyId(20438);
					pc.sendPackets("已設定強制變身為多賓剛哥。");
				} else {
					pc.sendPackets("您沒有該變身卡。");
					return;
				}
			} else {
				pc.sendPackets(".強制變身 數字/關");
				pc.sendPackets("1.亞利安 2.克莉絲特 3.阿通 4.伊西羅特 5.趙友 6.排名 7.真德斯(敵) 8.真德斯(黑) 9.多賓剛哥");
				return;
			}
		} catch (Exception e) {
			pc.sendPackets(".強制變身 數字/關");
			pc.sendPackets("1.亞利安 2.克莉絲特 3.阿通 4.伊西羅特 5.趙友 6.排名 7.真德斯(敵) 8.真德斯(黑) 9.多賓剛哥");
		}
	}
	private CastleEffect ce;
	private void castleEffect(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();

			if (pc == null)
				return;

			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			if (clan == null) {
				pc.sendPackets("沒有加入任何血盟。");
				return;
			}
			if (onoff.equalsIgnoreCase("開")) {
				if (clan.getCastleId() != 0 ) {
					if (!pc.isCastleEffect()) {
						pc.setCastleEffect(true);
						startCastleEffect(pc);
						pc.sendPackets("開啟聖血效果。");
						return;
					} else {
						pc.sendPackets("聖血效果已經開啟。");
						return;
					}
				} else {
					pc.sendPackets("您不是擁有城堡的血盟。");
				}
			} else if (onoff.equalsIgnoreCase("關")) {
				pc.setCastleEffect(false);
				stopCastleEffect(pc);
				pc.sendPackets("結束聖血效果。");
			} else {
				pc.sendPackets(".血印 [開 / 關]");
				return;
			}


		}catch (Exception e) {
			pc.sendPackets(".聖血效果 開/關");
		}
	}

	private void startCastleEffect(L1PcInstance pc) {
		final long inteval = 30 * 1000L;
		ce = new CastleEffect(pc, inteval, true);
		GeneralThreadPool.getInstance().schedule(ce, 10);
	}
	private void stopCastleEffect(L1PcInstance pc) {
		SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
		pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
		ce.cancel();
		ce = null;
	}

	private void checklotto(L1PcInstance pc) {
		lotto_system.getInstance().checklotto(pc);
	}

	private void AutoCashInfoAdd(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String name = st.nextToken();

			AutoCashUserInfo acui = AutoCashResultDatabase.getIntstance().getAutoCashUserInfo(name);
			if(acui != null) {
				pc.sendPackets(new S_SystemMessage("存在相同的存款人名稱。請輸入其他名稱。"));
				pc.sendPackets(new S_SystemMessage("** 注意事項：存款時名稱必須相同。 **"));
				return;
			} else {
				acui = new AutoCashUserInfo();
				acui.setAccountName(pc.getAccountName());
				acui.setCharName(pc.getName());

				AutoCashResultDatabase.getIntstance().addAutoCashUserInfo(name, acui);
				try {
					pc.sendPackets(new S_SystemMessage("** 已成功登錄存款人名稱。 **"));
					pc.sendPackets(new S_SystemMessage("** 注意事項：存款時名稱必須相同。 **"));
				} catch (Exception exception) {
					pc.sendPackets(new S_SystemMessage("請輸入 .登錄存款人 [存款人名稱]"));
				}
			}
	private void registerCoupon(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String code = st.nextToken();

			if (pc == null)
				return;

			MJPaymentInfo m_pInfo = MJPaymentInfo.newInstance(code.toUpperCase());
			if (m_pInfo == null) {
				pc.sendPackets(".兌換券登錄 [號碼]");
				pc.sendPackets("兌換券登錄號碼不存在。");
				return;
			}

			if(Check_Coupon(code.toUpperCase())) {
				pc.sendPackets("此優惠券號碼已被使用。");
				return;
			}

			SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
			if (pwh == null)
				return;

			m_pInfo.set_account_name(pc.getAccountName()).set_character_name(pc.getName()).set_expire_date(MJNSHandler.getLocalTime()).set_is_use(true).do_update();

			L1Item tempItem = ItemTable.getInstance().getTemplate(m_pInfo.get_itemid());
			if (tempItem.isStackable()) {
				L1ItemInstance item = ItemTable.getInstance().createItem(tempItem.getItemId());
				item.setIdentified(true);
				item.setCount(m_pInfo.get_count());
				pwh.storeTradeItem(item);
			} else {
				L1ItemInstance item = null;
				int createCount;
				for (createCount = 0; createCount < m_pInfo.get_count(); createCount++) {
					item = ItemTable.getInstance().createItem(tempItem.getItemId());
					item.setIdentified(true);
					pwh.storeTradeItem(item);
				}
			}

			// _player.getInventory().storeItem(m_pInfo.get_itemid(), m_pInfo.get_count());//直接發放到背包
			SC_GOODS_INVEN_NOTI.do_send(pc);
			pc.send_effect(2048);
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("\f3優惠券物品已發放 %s 個。請至附加物品倉庫領取。", new DecimalFormat("#,##0").format(m_pInfo.get_count()))));
			pc.sendPackets(String.format("優惠券物品已發放 %s 個。", new DecimalFormat("#,##0").format(m_pInfo.get_count()))));
		} catch (Exception e) {
			pc.sendPackets(".兌換券登錄 [號碼]");
		}
	}

	private boolean Check_Coupon(String coupon) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			int code = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select is_use from payment_info where code Like '"+coupon+"'");
			rs = pstm.executeQuery();
			if (rs.next()) {
				code = rs.getInt(1);
			}
			if (code != 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}

	private void viewShopBuyLimit(L1PcInstance pc) {
		ArrayList<ShopBuyLimit> sbl_list = ShopBuyLimitInfo.getInstance().getCharacterList(pc.getId());
		if (sbl_list != null && sbl_list.size() > 0) {
			for (ShopBuyLimit sbl : sbl_list) {
				if (sbl.get_count() <= 0) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String sLatestLoginDate = formatter.format(sbl.get_end_time());
					pc.sendPackets(String.format("\f3" + sbl.get_item_name() + "\f3可" +(sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT ? "\f3同一賬號 " : "\f3") +
							" %s 之後才能重新購買。", sLatestLoginDate));
				} else {
					pc.sendPackets("\f2" + sbl.get_item_name() + "可額外購買 " + sbl.get_count() + " 個。");
				}
			}

			pc.sendPackets("總共搜索到 " + sbl_list.size() + " 件。");
		} else {
			pc.sendPackets("沒有購買限制清單。");
		}
	}

	public static void setChart(L1PcInstance pc, String param) {
		boolean isOn;
		if (!MJKDAChartScheduler.isLoaded()) {
			pc.sendPackets(new S_SystemMessage("不在使用中。"));
			return;
		}

		if (param.equalsIgnoreCase("開")) {
			isOn = true;
			pc.sendPackets(new S_SystemMessage("顯示擊殺排名圖表。稍後會顯示在屏幕上。"));
			MJKDAChartScheduler.getInstance().onLoginUser(pc);
		} else if (param.equalsIgnoreCase("關")) {
			isOn = false;
			pc.sendPackets(new S_SystemMessage("隱藏擊殺排名圖表。重新登入後生效。"));
		} else {
			pc.sendPackets(new S_SystemMessage(".圖表 [開/關] (左上角 PK 圖表列表)"));
			return;
		}
		if (pc.getKDA() != null)
			pc.getKDA().isChartView = isOn;
	}

	private void clanMark(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String onoff = st.nextToken();

			if (pc.getMapId() >= 1708 && pc.getMapId() <= 1710 || pc.getMapId() >= 12852 && pc.getMapId() <= 12862
					|| pc.getMapId() == 15871 || pc.getMapId() == 15881 || pc.getMapId() == 15891 || pc.getMapId() == 10500 || pc.getMapId() == 10502) {
				pc.sendPackets("在該地圖中無法使用。");
				return;
			}

			if (onoff.equalsIgnoreCase("開")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, true), true);
				pc.sendPackets(new S_MARK_SEE(pc, 0, true), true);
				pc.sendPackets("開始顯示血盟標記。");
			} else if (onoff.equalsIgnoreCase("關")) {
				pc.sendPackets(new S_MARK_SEE(pc, 2, false), true);
				pc.sendPackets(new S_MARK_SEE(pc, 1, false), true);
				pc.sendPackets("結束顯示血盟標記。");
			} else {
				pc.sendPackets(".血盟標記 [開 / 關]");
				return;
			}
		} catch (Exception e) {
			pc.sendPackets(".血盟標記 [開 / 關]");
		}
	}

	private void giftNCoin(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String targetName = tok.nextToken();
			int count = Integer.parseInt(tok.nextToken());
			L1PcInstance target = L1World.getInstance().getPlayer(targetName);
			if (target == null) {
				pc.sendPackets(targetName + " 目前未連接。");
				return;
			}
			if (target.getNetConnection() == null) {
				pc.sendPackets(target + " 不是處於正確的連接狀態。");
				return;
			}
			if (pc == target) {
				pc.sendPackets("NCoin 無法轉讓給自己。");
				return;
			}
			if (count > pc.getNcoin()) {
				pc.sendPackets("您的 NCoin 少於 " + count + " 萬，因此無法完成。");
				return;
			}
			pc.addNcoin1(count);
			target.addNcoin(count);
			pc.sendPackets("您已贈送 [" + count + "] (萬)NCoin 給 [" + targetName + "]。");
			target.sendPackets("您已收到來自 [" + targetName + "] 的 [" + count + "] (萬)NCoin 禮物。");
		} catch (Exception e) {
			pc.sendPackets(".NCoin禮物 [角色名] [金額]");
		}
	}
	private void showHelp(L1PcInstance pc) {
		if (pc.is_shift_battle()) {
			// ServerExplainTable.getInstance().server_Explain(pc, 99);
			//Todo 一群SB
			StringBuilder sb = new StringBuilder();
			sb.append("━━━━━━━━━━━━━ 幫助 ━━━━━━━━━━━━━━").append("\r\n");
			sb.append("..(telec) .光線 .外窗 .血盟標誌 .血盟派對").append("\r\n");
			sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").append("\r\n");
			pc.sendPackets(new S_ChatPacket(pc, sb.toString()));
		} else {
			// ServerExplainTable.getInstance().server_Explain(pc, 3);
			StringBuilder sb = new StringBuilder();
			sb.append("━━━━━━━━━━━━━ 幫助 ━━━━━━━━━━━━━━").append("\r\n");
			sb.append("..(telec) .密碼更改 .年齡 .光線 .外窗 .相框查詢 .恢復").append("\r\n");
			sb.append(".首領通知 .提示 .血盟派對 .血盟標誌 .宏 .購買限制").append("\r\n");
			sb.append(".優惠券註冊 .樂透確認 .聖血效果 .強制變身").append("\r\n");
			sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").append("\r\n");
			pc.sendPackets(new S_ChatPacket(pc, sb.toString()));
		}
	}

	private void Ment(L1PcInstance pc, String param) {
		if (param.equalsIgnoreCase("關")) {
			pc.sendPackets("已關閉掉落提示。僅顯示重要掉落。");
			pc.RootMent = false;
		} else if (param.equalsIgnoreCase("開")) {
			pc.sendPackets("已開啟掉落提示。所有掉落都將顯示。");
			pc.RootMent = true;
		} else {
			pc.sendPackets("請輸入 .提示 [開/關] 來設置。");
		}
	}
	private void Sealedoff(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String param1 = tok.nextToken();
			int off = Integer.parseInt(param1);
			if (off > 10 || off < 0) {
				pc.sendPackets("\f2一次最多可申請(10)張。");
				return;
			}
			if (pc._create_password) {
				pc.sendPackets("\f2密碼註冊失敗後，可在30秒後重試。");
				return;
			}

			if (pc._seal_scroll) {
				pc.sendPackets("\f2密碼驗證失敗後，可在30秒後重試。");
				return;
			}

			if (pc.getAccount().getShopPassword() == 0) {
				pc._create_password = true;
				pc.sendPackets("\f2首次使用時，請輸入密碼。\f3(更改密碼時將會斷線)");
			} else {
				pc._seal_scroll = true;
				pc._seal_scroll_count = off;
				pc.sendPackets("\f2請在30秒內驗證您的密碼。\f3(更改密碼時將會斷線)");
			}
			pc.sendPackets(834);

			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						if (pc._seal_scroll) {
							pc._seal_scroll = false;
							pc.sendPackets("\f2時間已過。密碼驗證失敗。請重試。");
						}
						if (pc._create_password) {
							pc._create_password = false;
							pc.sendPackets("\f2時間已過。密碼生成失敗。請重試。");
						}
						pc._seal_scroll_count = 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
                    return null;
                }
			}, 30000);
		} catch (Exception e) {
			pc.sendPackets(".解封申請 (申請的張數)");
		}
	}

	/*
	 * private void Sealedoff(L1PcInstance pc, String param) {
	 *     try {
	 *         StringTokenizer tok = new StringTokenizer(param);
	 *         String param1 = tok.nextToken();
	 *         int off = Integer.parseInt(param1);
	 *         if (off > 10 || off < 0) {
	 *             pc.sendPackets("不能申請超過10張。");
	 *             return;
	 *         }
	 *         if (off == 0) {
	 *             pc.setSealScrollCount(0);
	 *             pc.setSealScrollTime(0);
	 *             pc.sendPackets("申請已重置。");
	 *         } else {
	 *             int sealScrollTime = (int) (System.currentTimeMillis() / 1000) + 1 * 24 * 3600;
	 *             pc.setSealScrollTime(sealScrollTime);
	 *             pc.setSealScrollCount(off);
	 *             pc.sendPackets("已申請 " + off + " 張解封卷軸。");
	 *             pc.sendPackets("將在今天起1天后自動發放。");
	 *             pc.sendPackets("如果重新申請，1天周期將重置。");
	 *         }
	 *         pc.save();
	 *     } catch (Exception e) {
	 *         pc.sendPackets(".解封申請 [申請的張數]");
	 *     }
	 * }
	 */

	public static void doTarget(MJCommandArgs args) {
		try {
			String cmd = args.nextString();
			if (cmd.equalsIgnoreCase("開")) {
				args.getOwner().setOnTargetEffect(true);
				args.notify("目標系統已啟動。");
			} else if (cmd.equalsIgnoreCase("關")) {
				args.getOwner().setOnTargetEffect(false);
				args.notify("目標系統已關閉。");
			} else if (cmd.equalsIgnoreCase("狀態")) {
				args.notify(String.format("當前目標系統狀態: %s", args.getOwner().isOnTargetEffect()));
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			args.notify(".targeting [開|關|狀態]");
		}
	}

	// private void changename(L1PcInstance pc, String name) {
	// try {
	// if (pc.getLevel() >= 80) {
	// int numOfNameBytes = 0;
	// numOfNameBytes = name.getBytes("MS949").length;
	// if (numOfNameBytes == 0) {
	// pc.sendPackets(".更改名稱 [新名稱]");
	// return;
	// }
	// if (pc.getClanid() != 0) {
	// pc.sendPackets("您可以在暫時離開血盟後更改。");
	// return;
	// }
	// if (pc.isCrown()) {
	//     pc.sendPackets("君主可以在與管理員商量後更改。");
	//     return;
	// }
	// if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED) || pc.hasSkillEffect(2005)) {
	//     pc.sendPackets("在禁止聊天狀態下無法更改。");
	//     return;
	// }
	// if (numOfNameBytes < 2 || numOfNameBytes > 12) {
	//     pc.sendPackets("請輸入1到6個韓文字。");
	//     return;
	// }
	// // level2
	// if (CharacterTable.getInstance().isContainNameList(name) ||
	//     MJBotNameLoader.isAlreadyName(name)) {
	//     pc.sendPackets("已存在相同的角色名稱。");
	//     return;
	// }
	//
	// if (BadNamesList.getInstance().isBadName(name)) {
	//     pc.sendPackets("這個角色名稱是禁止使用的。");
	// return;
	// }
	// for (int i = 0; i < name.length(); i++) {
	// if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ'
	// || name.charAt(i) == 'ㄷ'
	// || // 逐字比較
	// name.charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ'
	// || name.charAt(i) == 'ㅂ' || // 逐字比較
	// name.charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ'
	// || name.charAt(i) == 'ㅇ' || // 逐字比較
	// name.charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ'
	// || name.charAt(i) == 'ㅋ' || // 逐字比較
	// name.charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ'
	// || name.charAt(i) == 'ㅛ' || // 逐字比較
	// name.charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ'
	// || name.charAt(i) == 'ㅔ' || // 逐字比較
	// name.charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ'
	// || name.charAt(i) == 'ㅣ' || // 逐字比較
	// name.charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ'
	// || name.charAt(i) == 'ㅒ' || name.charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ'
	// || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || // 逐字比較
	// name.charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ'
	// || name.charAt(i) == 'ㅘ' || // 逐字比較
	// name.charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆'
	// || name.charAt(i) == 'ㅤ') {
	//     pc.sendPackets("角色名稱不正確。");
	// }
	// return;
	// }
	// }
	// for (int i = 0; i < name.length(); i++) {
	// if (!Character.isLetterOrDigit(name.charAt(i))) {
	// pc.sendPackets("角色名稱不正確。");
	// return;
	// }
	// }
	//
	// if (!isAlphaNumeric(name)) { // 特殊字符
	//     pc.sendPackets("禁止使用特殊字符。");
	//     return;
	// }
	// if (pc.getInventory().checkItem(408990, 1)) { // 檢查背包內物品
	// Updator.exec("UPDATE characters SET char_name=? WHERE char_name=?", new
	// Handler(){
	// @Override
	// public void handle(PreparedStatement pstm) throws Exception {
	// pstm.setString(1, name);
	// pstm.setString(2, pc.getName());
	// }
	// });
	// pc.save(); // 保存
	//
	// // /****** 請事先創建一個名為 LogDB 的資料夾 *******/
	// Calendar rightNow = Calendar.getInstance();
	// int year = rightNow.get(Calendar.YEAR);
	// int month = rightNow.get(Calendar.MONTH) + 1;
	// int date = rightNow.get(Calendar.DATE);
	// int hour = rightNow.get(Calendar.HOUR);
	// int min = rightNow.get(Calendar.MINUTE);
	// String stryyyy = "";
	// String strmmmm = "";
	// String strDate = "";
	// String strhour = "";
	// String strmin = "";
	// stryyyy = Integer.toString(year);
	// strmmmm = Integer.toString(month);
	// strDate = Integer.toString(date);
	// strhour = Integer.toString(hour);
	// strmin = Integer.toString(min);
	// String str = "";
	// str = new String("[" + stryyyy + "-" + strmmmm + "-" + strDate + " " +
	// strhour + ":" + strmin + "] "
	// + pc.getName() + " ---> " + name);
	// StringBuffer FileName = new StringBuffer("LogDB/角色名更改.txt");
	// PrintWriter out = null;
	// try {
	// out = new PrintWriter(new FileWriter(FileName.toString(), true));
	// out.println(str);
	// out.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// str = ""; // 初始化
	// pc.getInventory().consumeItem(408990, 1); // 刪除訂單
	// buddys(pc); // 刪除朋友
	// deleteMail(pc); // 刪除郵件
	//
	// GeneralThreadPool.getInstance().schedule(new Runnable(){
	// @Override
	// public void run(){
	// GameClient clnt = pc.getNetConnection();
	// C_NewCharSelect.restartProcess(pc);
	// Account acc = clnt.getAccount();
	// clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
	// if(acc.countCharacters() > 0)
	// C_CommonClick.sendCharPacks(clnt);
	// }
	// }, 500L);
	//
	//// Thread.sleep(500);
	//// pc.sendPackets(new S_Disconnect());
	//// pc.logout();
	// } else {
	// pc.sendPackets("您沒有名稱變更券。");
	// }
	// } else {
	// pc.sendPackets("80級及以下不允許。");
	// }
	// } catch (Exception e) {
	// pc.sendPackets("請輸入 '.namechange [新名稱]'。");
	// }
	// }

	public static boolean isAlphaNumeric(String s) {
		char[] acArray = s.toCharArray();
		for (char ac : acArray) {
			if (((ac >= 'A') && (ac <= 'z')) || ((ac >= 'a') && (ac <= 'z')))
				return true;
			if ((ac >= '0') && (ac <= '9'))
				return true;
			if ((ac >= 44032) && (ac <= 55203)) {
				return true;
			}
		}
		return false;
	}

	/********* 從資料庫好友列表中刪除更改的ID ************/
	public static void buddys(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		String aaa = pc.getName();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buddys WHERE buddy_name=?");

			pstm.setString(1, aaa);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void deleteLetter(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;

		String aaa = pc.getName();

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM letter WHERE receiver=?");
			pstm.setString(1, aaa);
			pstm.execute();
			// System.out.println("....["+ aaa +"].....");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void hunt(L1PcInstance pc, String cmd) {
		int price1 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[0];
		int price2 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[1];
		int price3 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[2];
		try {
			StringTokenizer tok = new StringTokenizer(cmd);
			String name = tok.nextToken();
			if (name == null || name.equals(""))
				throw new Exception();

			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
				pc.sendPackets(String.format("找不到 %s。", name));
				return;
			}
//			System.out.println(target.get_Wanted_Level());

			if (target == pc) {
				pc.sendPackets("不能對自己發出通緝令。");
				return;
			}
			if(target.getClanid() == pc.getClanid()) {
				pc.sendPackets("不能對同一血盟成員發出通緝令。");
				return;
			}
			if (target.hasSkillEffect(L1SkillId.USER_WANTED3)) {
				pc.sendPackets(String.format("%s 已經處於第3階段通緝中。", name));
				return;
			}
			if (target.isGm()) {
				pc.sendPackets("不能對遊戲管理員發出通緝令。");
				return;
			}
			if (target.get_Wanted_Level() == 0) {
				if (!(pc.getInventory().checkItem(40308, price1))) {
					pc.sendPackets("金幣不足("+price1+")");
					return;
				}
			} else if (target.get_Wanted_Level() == 1) {
				if (!(pc.getInventory().checkItem(40308, price2))) {
					pc.sendPackets("金幣不足("+price2+")");
					return;
				}
			} else if (target.get_Wanted_Level() == 2) {
				if (!(pc.getInventory().checkItem(40308, price3))) {
					pc.sendPackets("金幣不足(" + price3 + ")");
					return;
				}
			}

			if (target.get_Wanted_Level() == 0) {
				String message = String.format("%s 已對 %s 發出了通緝令。(第1階段)", pc.getName(), target.getName());
				pc.sendPackets(message);
				target.sendPackets(message);
				target.setSkillEffect(L1SkillId.USER_WANTED1, -1);
				target.add_Wanted_Level();
				target.doWanted(true, false);
				pc.getInventory().consumeItem(40308, price1);
			} else if (target.hasSkillEffect(L1SkillId.USER_WANTED1)){
				String message = String.format("%s 已對 %s 發出了通緝令。(第2階段)", pc.getName(), target.getName());
				pc.sendPackets(message);
				target.sendPackets(message);
				target.removeSkillEffect(L1SkillId.USER_WANTED1);
				target.setSkillEffect(L1SkillId.USER_WANTED2, -1);
				target.add_Wanted_Level();
				target.doWanted(true, false);
				pc.getInventory().consumeItem(40308, price2);
			} else if (target.hasSkillEffect(L1SkillId.USER_WANTED2)){
				String message = String.format("%s 已對 %s 發出了通緝令。(第3階段)", pc.getName(), target.getName());
				pc.sendPackets(message);
				target.sendPackets(message);
				target.removeSkillEffect(L1SkillId.USER_WANTED2);
				target.setSkillEffect(L1SkillId.USER_WANTED3, -1);
				target.add_Wanted_Level();
				target.doWanted(true, false);
				pc.getInventory().consumeItem(40308, price3);
			}

		} catch (Exception e) {
			pc.sendPackets(".通緝 [角色名稱]");
			pc.sendPackets("效果階段累積: 近距離傷害減少3,命中率減少3,遠距離傷害減少3,命中率減少3,SP減少3,減傷減少3,AC減少3");
			pc.sendPackets("第1階段: " + price1 + ", 第2階段: " + price2 + ", 第3階段: " + price3);
		}
	}

	private void phone(L1PcInstance pc, String param) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime() + 10 > curtime) {
				long sec = (pc.getQuizTime() + 10) - curtime;
				pc.sendPackets(sec + "秒後可以使用。");
				return;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("-----------------------------------------------------").append("\r\n");
			sb.append("\fT電報 ():1").append("\r\n");
			sb.append("\fY電報 (頻道):1").append("\r\n");
			sb.append("\f3互相的聊天禮貌是基本。").append("\r\n");
			sb.append("-----------------------------------------------------").append("\r\n");
			pc.sendPackets(new S_ChatPacket(pc, sb.toString()));

			/*
			 * StringTokenizer tok = new StringTokenizer(param);
			 * String phone = tok.nextToken();
			 * Account account = Account.load(pc.getAccountName());
			 * if (param.length() < 10) {
			 *     pc.sendPackets("無效的號碼。請重新輸入。");
			 *     pc.sendPackets("* 警告：提供錯誤信息可能會導致遊戲使用限制。");
			 *     return;
			 * }
			 * if (param.length() > 11) {
			 *     pc.sendPackets("錯誤的號碼。請重新輸入。");
			 *     pc.sendPackets("* 警告：提供錯誤信息可能會導致遊戲使用限制。");
			 *     return;
			 * }
			 * if (isDisitAlpha(phone) == false) {
			 *     pc.sendPackets("請只輸入數字。");
			 *     pc.sendPackets("* 警告：提供錯誤信息可能會導致遊戲使用限制。");
			 *     return;
			 * }
			 * if (account.getphone() != null) {
			 *     pc.sendPackets("電話號碼已經設置。");
			 *     pc.sendPackets("更改號碼請給Metis發信提供聯繫信息。");
			 *     return;
			 * }
			 * account.setphone(phone);
			 * Account.updatePhone(account);
			 * pc.sendPackets(" " + phone + " 設置完成。初始化時將發送消息。");
			 */
		} catch (Exception e) {
			pc.sendPackets("如果您進行詢問，可以提前知道重新開放或公告。");
		}
	}

	private void spawnNotifyOnOff(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (on.equalsIgnoreCase("開")) {
				if (pc.isBossNotify()) {
					pc.sendPackets("Boss通知: Boss召喚及通知已經處於啟用狀態。");
					return;
				}
				pc.setBossNotify(true);
				pc.sendPackets("Boss通知: 開 (Boss召喚及通知已啟動)");
			} else if (on.equals("關")) {
				if (!pc.isBossNotify()) {
					pc.sendPackets("Boss通知: Boss召喚及通知未啟用。");
					return;
				}
				pc.setBossNotify(false);
				pc.sendPackets("Boss通知: 關 (Boss召喚及通知已停止)");
			} else {
				pc.sendPackets("無效的請求。");
			}
		} catch (Exception e) {
			pc.sendPackets("請設置Boss通知為 [開, 關]。當前狀態: (" + (pc.isBossNotify() ? "開" : "關") + ")。");
		}
	}
	private void maphack(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			String on = st.nextToken();
			if (pc.getMapId() == 132) {
				pc.sendPackets(new S_Ability(3, false));
				pc.sendPackets("目前地圖中無法使用。");
				return;
			}
			if (on.equalsIgnoreCase("開")) {
				pc.sendPackets(new S_Ability(3, true));
				pc.sendPackets("燈光已開啟，亮度增加。");
			} else if (on.equals("關")) {
				pc.sendPackets(new S_Ability(3, false));
				pc.sendPackets("燈光已關閉，亮度減少。");
			}
		} catch (Exception e) {
			pc.sendPackets(".燈光 [開, 關]");
		}
	}

	private void restore(L1PcInstance pc) {
		try {
			long curtime = System.currentTimeMillis() / 1000;
			if (pc.getQuizTime2() + 5 > curtime) {
				long time = (pc.getQuizTime2() + 5) - curtime;
				pc.sendPackets(time + "秒後可使用。");
				return;
			}
			Updator.exec("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (34,38,5001,99,997,5166,39,34,701,2000)", new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, pc.getAccountName());
				}
			});
			pc.sendPackets("所有角色的座標已成功恢復。");
			pc.setQuizTime(curtime);
		} catch (Exception e) {
		}
	}

	private void tell(L1PcInstance pc) {
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime2() + 10 > curtime) {
			long time = (pc.getQuizTime2() + 10) - curtime;
			pc.sendPackets(time + "秒後可使用。");
			return;
		}
		try {
			// if (pc.getTelDelay() > 200) {
			// pc.sendPackets("攻擊中無法使用此功能。");
			// return;
			// }
			if (pc.getMapId() == 781) {
				if (pc.getLocation().getX() <= 32998 && pc.getLocation().getX() >= 32988 && pc.getLocation().getY() <= 32758 && pc.getLocation().getY() >= 32736) {
					pc.sendPackets("此地點無法使用。");
					return;
				}
			}
			if (pc.isPinkName() || pc.isDead() || pc.isParalyzed() || pc.isSleeped() || pc.getMapId() == 800 || pc.getMapId() == 12150 || pc.getMapId() == 12154 || pc.getMapId() == 5302
					|| pc.getMapId() == 5153 || pc.getMapId() == 5490 || pc.getMapId() == 213) {
				pc.sendPackets("此地點無法使用。");
				return;
			}
			if (pc.hasSkillEffect(SHOCK_STUN) || pc.hasSkillEffect(L1SkillId.EMPIRE) || pc.hasSkillEffect(DESPERADO) || pc.hasSkillEffect(EARTH_BIND) || pc.hasSkillEffect(L1SkillId.FORCE_STUN)
					|| pc.hasSkillEffect(L1SkillId.ETERNITI) || pc.hasSkillEffect(CURSE_PARALYZE) || pc.hasSkillEffect(ICE_LANCE) || pc.hasSkillEffect(SLOW) || pc.hasSkillEffect(FOG_OF_SLEEPING)
					|| pc.hasSkillEffect(BONE_BREAK) || pc.hasSkillEffect(L1SkillId.ETERNITI) || pc.hasSkillEffect(L1SkillId.PHANTOM) || pc.hasSkillEffect(L1SkillId.CHAINSWORD_STUN)
					|| pc.hasSkillEffect(L1SkillId.DISINTEGRATE) || pc.hasSkillEffect(L1SkillId.CRUEL)) {
				pc.sendPackets("目前無法使用此功能。");
				return;
			}
			if (pc.getMapId() == 132) {
				pc.sendPackets(new S_Ability(3, false));
			}
			/*
			 * if (CharPosUtil.getZoneType(pc) == 0 && castle_id != 0) { // 無法在攻城區域附近使用 pc.sendPackets("此地點無法使用。"); return; }
			 */
			// pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_RANGE, pc, pc.getWeapon()), true);
			pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
			pc.update_lastLocalTellTime();
			pc.setQuizTime2(curtime);
		} catch (Exception exception35) {
		}
	}

	public void BloodParty(L1PcInstance pc) {
		if (pc.isDead()) {
			pc.sendPackets("死亡狀態下無法使用此功能。");
			return;
		}
		int ClanId = pc.getClanid();
		if (ClanId != 0) {
			if (pc.getClanRank() == L1Clan.LEADER || pc.getClanRank() == L1Clan.GUARDIAN || pc.getClanRank() == L1Clan.VICE_LEADER || pc.getClanRank() == L1Clan.ELITE || pc.getClanRank() == L1Clan.MEMBER) {
				for (L1PcInstance SearchBlood : L1World.getInstance().getAllPlayers()) {
					if (SearchBlood.getClanid() != ClanId || SearchBlood.isPrivateShop() || SearchBlood.isInParty()) { // 血盟是
						continue; // 跳出循環
					} else if (!SearchBlood.getName().equals(pc.getName())) {
						pc.setPartyType(1); // 設置隊伍類型
						SearchBlood.setPartyID(pc.getId()); // 設置隊伍ID
						SearchBlood.sendPackets(new S_Message_YN(954, pc.getName()));
						pc.sendPackets("您已向 " + SearchBlood.getName() + " 申請組隊。");
					}
				}
			}
		} else { // 沒有血盟或不是盟主或守護騎士 [X]
			pc.sendPackets("如果您在血盟中，並且是盟主、副盟主、守護騎士、菁英或普通成員，則可以使用此功能。");
		}
	}

	private void age(L1PcInstance pc, String cmd) {
		try {
			StringTokenizer tok = new StringTokenizer(cmd);
			String AGE = tok.nextToken();
			int AGEint = Integer.parseInt(AGE);
			if (AGEint > 59 || AGEint < 14) {
				pc.sendPackets("設置將顯示給血盟的年齡。");
				return;
			}
			pc.setAge(AGEint);
			pc.save();
			pc.sendPackets("年齡 (" + AGEint + ") 已設置。");
		} catch (Exception e) {
			pc.sendPackets(".年齡 [數字]");
		}
	}

	private static boolean isDisitAlpha(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)) // 숫자가 아니라면
					&& !Character.isUpperCase(str.charAt(i)) // 대문자가 아니라면
					&& !Character.isLowerCase(str.charAt(i))) { // 소문자가 아니라면
				check = false;
				break;
			}
		}
		return check;
	}

	private void changepassword(L1PcInstance pc, String param) {
		try {
			if (pc.get_lastPasswordChangeTime() + 10 * 60 * 1000 > System.currentTimeMillis()) {
				pc.sendPackets("距離您更改密碼尚未滿10分鐘。請稍後再試。");
				return;
			}
			StringTokenizer tok = new StringTokenizer(param);
			String newpasswd = tok.nextToken();
			if (newpasswd.length() < 6) {
				pc.sendPackets("請輸入6到16個字符的密碼，必須為英文字母或數字。");
				return;
			}
			if (newpasswd.length() > 16) {
				pc.sendPackets("請輸入6到16個字符的密碼，必須為英文字母或數字。");
				return;
			}
			if (!isDisitAlpha(newpasswd)) {
				pc.sendPackets("請僅輸入英文字母和數字。");
				return;
			}
			to_Change_Passwd(pc, newpasswd);

		} catch (Exception e) {
			pc.sendPackets("請輸入 .更改密碼 [輸入新密碼]。");
		}
	}

	private void to_Change_Passwd(L1PcInstance pc, String passwd) {
		Selector.exec("select account_name from characters where char_name=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, pc.getName());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					final String login = rs.getString("account_name");
					Updator.exec("update accounts set password=? where login=?", new Handler() {
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setString(1, passwd);
							pstm.setString(2, login);
						}
					});
				}
			}
		});
		pc.sendPackets(String.format("\f2您的帳號密碼已更改為 (%s)。", passwd));
		pc.sendPackets("\f3部分功能受到限制。(請關閉客戶端後重新連接)");
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3部分功能受到限制。(請關閉客戶端後重新連接)"));
	}

	// 返回密碼是否正確
	public static boolean isPasswordTrue(String Password, String oldPassword) {
		String _rtnPwd = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT password(?) as pwd");

			pstm.setString(1, oldPassword);
			rs = pstm.executeQuery();
			if (rs.next()) {
				_rtnPwd = rs.getString("pwd");
			}
			if (_rtnPwd.equals(Password)) { // 如果相同
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public static void privateShop(L1PcInstance pc) {
		try {
			if (!pc.isPrivateShop()) {
				pc.sendPackets("可以在個人商店模式下使用。");
				return;
			}
			for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
				if (target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
					pc.sendPackets("\f3您的輔助角色已經處於自動商店狀態。");
					pc.sendPackets("\f3請使用 /商店 關閉商店。");
					return;
				}
			}
			GameClient client = pc.getNetConnection();
			pc.setNetConnection(null);
			pc.dispose_regenerator();
			// 重生註釋
			// pc.stopHpMpRegeneration();
			pc.setAutoShop(true);
			try {
				pc.save();
				pc.saveInventory();
			} catch (Exception e) {
			}
			client.setActiveChar(null);
			client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
			client.sendPacket(new S_Unknown2(1)); // 為重置按鈕進行結構變更// Episode U

		} catch (Exception e) {
		}
	}

	private void outsideChat(L1PcInstance pc, String param) {
		try {
			if (param.equalsIgnoreCase("開啟")) {
				pc.setOutSideChat(true);
			} else if (param.equalsIgnoreCase("關閉")) {
				pc.setOutSideChat(false);
			} else if (param.equalsIgnoreCase("狀態")) {

			} else
				throw new Exception();

			pc.sendPackets(String.format("外部聊天 : %s", pc.isOutsideChat() ? "開啟" : "關閉"));
		} catch (Exception e) {
			pc.sendPackets(".外部聊天 [開啟|關閉|狀態]");
		}
	}

	private String parseStat(String s) throws Exception {
		if (s.equalsIgnoreCase("力量"))
			return "str";
		else if (s.equalsIgnoreCase("敏捷"))
			return "dex";
		else if (s.equalsIgnoreCase("體質"))
			return "con";
		else if (s.equalsIgnoreCase("智力"))
			return "int";
		else if (s.equalsIgnoreCase("智慧"))
			return "wis";
		else if (s.equalsIgnoreCase("魅力"))
			return "cha";
		throw new Exception(s);
	}

	public void fullstat(L1PcInstance pc, String param) {
		try {
			String[] arr = param.split(" ");
			if (arr == null || arr.length < 2)
				throw new Exception();

			String s = parseStat(arr[0]);
			MJFullStater.running(pc, s, Integer.parseInt(arr[1]));
		} catch (Exception e) {
			pc.sendPackets(String.format(".屬性 [力量/敏捷/體質/智力/感知/魅力] [添加點數] 剩餘屬性點數 %d", pc.remainBonusStats()));
		}
	}

	private static void do_lfc(L1PcInstance pc, String param) {
		try {
			// StringTokenizer token = new StringTokenizer(param);
			// String name = token.nextToken();
			String name = param;
			L1PcInstance target = L1World.getInstance().findpc(name);
			if (target == null) {
				pc.sendPackets(String.format("無法找到 %s。", name));
				return;
			}
			L1BoardPost bp = L1BoardPost.createLfc(name, "-", String.format("3 %s", pc.getName()));
			MJLFCCreator.registLfc(pc, 3);
			target.sendPackets(String.format("您已向 %s 申請決鬥。", name));
			target.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(String.format("%s 向您申請決鬥。", pc.getName()), bp.getId() + 1000, MJSurveyFactory.createLFCSurvey(), 30 * 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void macroSetting(L1PcInstance pc, String param) {
		try {
			StringTokenizer tok = new StringTokenizer(param);
			String setting = tok.nextToken();

			if (setting.equalsIgnoreCase("設定")) {
				StringBuilder str = new StringBuilder();
				while (tok.hasMoreTokens()) {
					str.append(tok.nextToken() + " ");
				}

				String ment = str.toString();
				if (ment.length() > 1) {
					pc.addMacroList(ment);
					pc.sendPackets("已將訊息添加到宏指令中。");
					pc.sendPackets(" - [" + ment + "]");
				} else {
					pc.sendPackets(new S_SystemMessage("請輸入 .macro [設定/開始/停止/刪除/確認] [設定時要添加的訊息] [刪除時要刪除的序號]。"));
				}
			} else if (setting.equalsIgnoreCase("確認")) {
				pc.getMacroListIdentify();
			} else if (setting.equalsIgnoreCase("刪除")) {

				String index = tok.nextToken();
				if (index.equalsIgnoreCase("全部")) {
					pc.getMacroList().clear();
					pc.sendPackets("所有宏指令訊息已被刪除。");
				} else {
					if (isStringDouble(index)) {
						int real_index = Integer.valueOf(index);
						if (real_index > pc.getMacroList().size()) {
							pc.sendPackets("該序號的宏指令不存在。");
							return;
						}
						pc.getMacroList().remove(real_index);
						pc.sendPackets("[" + real_index + "號] 宏指令訊息已被刪除。");
					} else {
						pc.sendPackets("刪除的序號必須是數字。");
					}
				}

			} else if (setting.equalsIgnoreCase("開始")) {
				if (pc.getMacroList().size() <= 0) {
					pc.sendPackets("沒有已輸入的宏指令。");
					return;
				}

				if (pc.isMacroTimerStart()) {
					pc.sendPackets("宏指令已在運行中。");
					return;
				}

				pc.startMacroTimer();
				pc.sendPackets("宏指令已開始。");
			} else if (setting.equalsIgnoreCase("停止")) {
				if (!pc.isMacroTimerStart()) {
					pc.sendPackets("當前沒有正在運行的宏指令。");
					return;
				}

				pc.stopMacroTimer();
				pc.sendPackets("宏指令已停止。");
			} else {
				pc.sendPackets("請求無效。");
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".宏指令 [設定/開始/停止/刪除/確認] [設定時要添加的訊息] [刪除時要刪除的序號] 請輸入。"));
		}
	}

	private boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void insertCashInfo(L1PcInstance pc, String param) {
		try {
			StringTokenizer st = new StringTokenizer(param);
			int charge_count = Integer.parseInt(st.nextToken());

			if (pc == null)
				return;

			if (charge_count < 10000) {
				pc.sendPackets("充值的最低金額是10,000元。");
				return;
			}

			if (charge_count > 500000) {
				pc.sendPackets("充值的最高金額是500,000元。");
				return;
			}

			if (getDepositInfo(pc) == 0) {
				pc.sendPackets("已有正在處理的申請。");
				return;
			}

			String current_date = MJString.get_current_datetime();
			MJNCoinDepositInfo dInfo = MJNCoinDepositInfo.newInstance()
					.set_deposit_id(MJNCoinIdFactory.DEPOSIT.next_id()).set_character_object_id(pc.getId())
					.set_character_name(pc.getName()).set_account_name(pc.getAccountName())
					.set_deposit_name(pc.getAccountName()).set_ncoin_value(Integer.valueOf(charge_count))
					.set_generate_date(current_date).set_is_deposit(0);

			MJNCoinDepositInfo.do_store(dInfo);
			do_write_letter_command(pc, MJNCoinSettings.DEPOSIT_LETTER_ID);

			String subject = String.format("[充值申請] %s", pc.getName());
			do_write_letter_togm(current_date, subject, dInfo.toString());
		} catch (Exception e) {
			pc.sendPackets(".充值En幣 [金額]");
		}
	}

	private void do_write_letter_command(L1PcInstance pc, final int notify_id) {
		final MJObjectWrapper<String> subject = new MJObjectWrapper<String>();
		final MJObjectWrapper<String> content = new MJObjectWrapper<String>();
		Selector.exec("select * from letter_command where id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, notify_id);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					subject.value = rs.getString("subject");
					content.value = rs.getString("content");
				} else {
					subject.value = MJString.EmptyString;
					content.value = MJString.EmptyString;
				}
			}
		});
		if (MJString.isNullOrEmpty(subject.value) && MJString.isNullOrEmpty(content.value)) {
			try {
				throw new Exception(String.format("無法找到命令信件。ID：%d\r\n堆疊追蹤", notify_id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		String current_date = MJString.get_current_datetime();
		do_write_letter(pc.getName(), current_date, subject.value, content.value);
	}

	private void do_write_letter_togm(String generate_date, String subject, String content) {
		do_write_letter("메티스", generate_date, subject, content);
	}

	private void do_write_letter(String receiver, String generate_date, String subject, String content) {
		int id = LetterTable.getInstance().writeLetter(949, generate_date, "메티스", receiver, 0, subject, content);
		L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
		if (pc != null) {
			pc.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, "메티스",
					subject));
			// pc.sendPackets(new S_LetterList(pc, 0, 20));
			pc.send_effect(1091);
			pc.sendPackets(428);
		}
	}

	private int getDepositInfo(L1PcInstance pc) {
		int check = -1;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ncoin_trade_deposit WHERE account_name=?");
			pstm.setString(1, pc.getAccount().getName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				check = rs.getInt("is_deposit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return check;
	}
}
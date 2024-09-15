package l1j.server.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Random;

import MJShiftObject.MJShiftObjectManager;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.CPMWReNewClan.ClanDungeon.ClanDugeon;
import l1j.server.EQCSystem.EQCLoader;
import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.MJINNSystem.MJINNRoom;
import l1j.server.MJRankSystem.Business.MJRankBusiness;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SUMMON_PET_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.Stadium.StadiumManager;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.server.datatables.ExpTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.PetTypeTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1GuardianInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.function.additem2;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_BookMarkLoad;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DelSkill;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.server.serverpackets.S_UserCommands1;
import l1j.server.server.server.serverpackets.S_UserCommands10;
import l1j.server.server.server.serverpackets.S_UserCommands11;
import l1j.server.server.server.serverpackets.S_UserCommands12;
import l1j.server.server.server.serverpackets.S_UserCommands13;
import l1j.server.server.server.serverpackets.S_UserCommands14;
import l1j.server.server.server.serverpackets.S_UserCommands15;
import l1j.server.server.server.serverpackets.S_UserCommands16;
import l1j.server.server.server.serverpackets.S_UserCommands17;
import l1j.server.server.server.serverpackets.S_UserCommands18;
import l1j.server.server.server.serverpackets.S_UserCommands2;
import l1j.server.server.server.serverpackets.S_UserCommands3;
import l1j.server.server.server.serverpackets.S_UserCommands7;
import l1j.server.server.server.serverpackets.S_UserCommands8;
import l1j.server.server.server.serverpackets.S_UserCommands9;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class C_ItemUSe2 extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_ITEM_USE2 = "[C] C_ItemUSe2";

	private static Random _random = new Random(System.nanoTime());

	public String C_ItemUSe2(byte[] abyte0, GameClient client) throws Exception {
		super(abyte0);
		int itemObjid = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;

		if (MJCommons.isLock(pc) || MJCommons.isNonAction(pc)) {
			if (!(pc.hasSkillEffect(L1SkillId.OSIRIS) || pc.hasSkillEffect(L1SkillId.DESPERADO)))
			return;
		}
		
		L1ItemInstance l1iteminstance = pc.getInventory().getItem(itemObjid);
		
		if (l1iteminstance == null) {
			return;
		}
		
		if(Config.Login.UseShiftServer && !MJShiftObjectManager.getInstance().use_item_white_list(pc, l1iteminstance)){
// pc.sendPackets(String.format("%s (無法使用)", l1iteminstance.getName()));
			return;
		}

		if (CommonUtil.teleport_check(pc, l1iteminstance)) {
//			pc.sendPackets(new S_Paralysis(7, false));
			pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
			return;
		}

		int itemId;
		int spellsc_objid = 0;
		int l = 0;
		
		try {
			itemId = l1iteminstance.getItem().getItemId();

			if (Config.ServerAdSetting.DelayTimer) {
				if (l1iteminstance.getItem().get_delayEffect() <= 0) {
					if (pc.hasItemDelayTime(l1iteminstance)) {
						if (l1iteminstance.getItem().getType() == 17) {
							pc.sendPackets(String.valueOf(new S_Paralysis(7, false)));
						}
						return;
					}
				}
			}
		} catch (Exception e) {
			return;
		}

		//TODO 必須添加更多的物品編號，否則無法使用
		switch (itemId) {
			case 3000393:
			case 40964:        // 黑魔法粉末....
			case 60035:        // 符文魔力去除劑
			case 60027:
			case 3000148:
			case 719:
			case 707:
			case 706:
			case 705:
			case 704:
			case 703:
			case 698:
			case 776:
			case 777:
			case 844:        // 裝備交換憑證
			case 845:
			case 846:
			case 847:
			case 848:
			case 4100428:
			l =readD();
			break;
			default: break;
		}

		int loc_x = 0 + CommonUtil.random(-5, 5), loc_y = 0 + CommonUtil.random(-5, 5);
		int[] loc = null;
		
		if(itemId >= 40033 && itemId <= 40038){
			boolean _elixirContinue = true;
			if (pc.getElixirStats() >= 30) {
				_elixirContinue = false;
			}
			int elix = 0;

			if (pc.getLevel() >= 100){
				elix = 30;
			} else if (pc.getLevel() >= 50 && pc.getLevel() < 80) {
				elix = (pc.getLevel() - 48) / 2;
			} else if (pc.getLevel() >= 80 && pc.getLevel() < 90){
				elix = ((pc.getLevel() - 48) / 2) + 1;
			} else if (pc.getLevel() >= 90 && pc.getLevel() < 100){
				elix = ((pc.getLevel() - 48) / 2) + 2;
			}

			if (pc.getLevel() >= 50 && pc.getElixirStats() >= elix){
				_elixirContinue = false;
			}
			
	/*		
			if(pc.getLevel() >= 50 && pc.getLevel() <= 51 && pc.getElixirStats() >= 1){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 52 && pc.getLevel() <= 53 && pc.getElixirStats() >= 2){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 54 && pc.getLevel() <= 55 && pc.getElixirStats() >= 3){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 56 && pc.getLevel() <= 57 && pc.getElixirStats() >= 4){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 58 && pc.getLevel() <= 59 && pc.getElixirStats() >= 5){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 60 && pc.getLevel() <= 61 && pc.getElixirStats() >= 6){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 62 && pc.getLevel() <= 63 && pc.getElixirStats() >= 7){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 64 && pc.getLevel() <= 65 && pc.getElixirStats() >= 8){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 66 && pc.getLevel() <= 67 && pc.getElixirStats() >= 9){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 68 && pc.getLevel() <= 69 && pc.getElixirStats() >= 10){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 70 && pc.getLevel() <= 71 && pc.getElixirStats() >= 11){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 72 && pc.getLevel() <= 73 && pc.getElixirStats() >= 12){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 74 && pc.getLevel() <= 75 && pc.getElixirStats() >= 13){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 76 && pc.getLevel() <= 77 && pc.getElixirStats() >= 14){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 78 && pc.getLevel() <= 79 && pc.getElixirStats() >= 15){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 95 && pc.getLevel() <= 97 && pc.getElixirStats() >= 16){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 98 && pc.getLevel() <= 99 && pc.getElixirStats() >= 17){
				_elixirContinue = false;
			}
			if(pc.getLevel() >= 100 && pc.getLevel() <= 127 && pc.getElixirStats() >= 20){
				_elixirContinue = false;
			}
			*/
			if(!_elixirContinue){
				// 已經使用了當前等級可以服用的所有靈藥。從50級開始，每5級可以服用1個。
				pc.sendPackets(String.valueOf(new S_ServerMessage(4472)));
				return;
			}
		}

		L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(l);
		
		switch (itemId) {
		case 4100428:
			if (l1iteminstance1.get_Carving() == 0) {
				pc.sendPackets(l1iteminstance1.getName() + " 不是刻印物品。");
				return;
			}
			
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			
			int enchant_level = l1iteminstance1.getEnchantLevel();
			if (enchant_level < Config.ServerEnchant.CarvingEnchant && l1iteminstance1.getItem().get_safeenchant() != 0) {
				pc.sendPackets("刻印武器的強化低於 (" + Config.ServerEnchant.CarvingEnchant + ") 是不可能的。");
				return;
			}

			if (l1iteminstance1.get_Carving() != 0) {
				l1iteminstance1.set_Carving(0);
				l1iteminstance1.setEndTime(null);

				int st = 0;
				if (l1iteminstance1.isIdentified())
					st += 1;
				if (!l1iteminstance1.getItem().isTradable())
					st += 2;
				if (l1iteminstance1.getItem().isCantDelete())
					st += 4;
				if (l1iteminstance1.getItem().get_safeenchant() < 0)
					st += 8;
				if (l1iteminstance1.getBless() >= 64) {
					st = 32;
					if (l1iteminstance1.isIdentified()) {
						st += 15;
					} else {
						st += 14;
					}
				}
				pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
				pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
				pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.sendPackets("[" + l1iteminstance1.getName() + "] 的刻印已解除。");
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[" + l1iteminstance1.getName() + "] 的刻印已解除。"));
				/*} else {
				pc.sendPackets(l1iteminstance1.getName() + " 不是刻印物品。");
				return;*/
			}
			break;
		case 5990: // 排名進入藥水
			if (pc.getInventory().checkItem(itemId, 1)) {
				if(MJRankBusiness.getInstance().onExpendiant(pc))
					pc.getInventory().removeItem(l1iteminstance, 1);
			}
			break;
			case 3000215: // 獨角獸的成長印記
				pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "unitoken")));
				break;
			case 4100586: // 純淨靈藥 (EXP)
				pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "expelixir")));
				break;
			case 4200251: // 伺服器指南書
				pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "Guide_book")));
				break;
			case 820018: // 純淨靈藥
				pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "pureelixir")));
				break;
			case 41159:
			case 41921:
				pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getId(), "pixiefeather")));
				break;
			/** 高塔的混沌符?，變異符? **/
			case 30106: {// 隱藏谷村回家符?
			loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HIDDEN_VALLEY);
			pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
		}
			break;
		case 844:
		case 845:
		case 846:
		case 847:
		case 848:
		case 854:
			if (l1iteminstance1.getEndTime() != null) {
				pc.sendPackets("限期物品無法使用。");
				return;
			}
			EQCLoader.getInstance().processEQC(pc, itemId, l1iteminstance, l1iteminstance1);
			break;
//		case 4000000:{
//			L1ItemInstance item = null;
//			System.out.print("到這裡來?？");
//			ItemSelectorLoader.getInstance().processEQC(pc, l1iteminstance);
//			if(ItemSelectorTable.isSelectorInfo(ItemTable.getInstance().getTemplate(itemId).getItemId()))	
//				item = new l1j.server.server.model.item.function.ItemSelector(ItemTable.getInstance().getTemplate(itemId), SelectorType.NORMAL);
//		}
//			break;
		case 3000356:
		case 3000363: 
		case 3000364:
			case 3000365: // 裝備兌換券
				pc.sendPackets(String.valueOf(new S_SystemMessage("請交給梅蒂斯。")));
				break;
			case 3000519: // 新狩獵場轉移命令書
				if (pc.getLevel() >= Config.ServerAdSetting.NewCha1) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("這不是新手等級。")));
				return;
			}
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(3);
				int ry = _random.nextInt(3);
				int ux = 32792 + rx;
				int uy = 32737 + ry;
				pc.start_teleport(ux, uy, 785, pc.getHeading(), 18339, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
			}
				cancelAbsoluteBarrier(pc); // 解除?對?障
				break;
			case 7021: // 英雄套裝
				if (pc.getInventory().getSize() > 120) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("攜帶的物品太多了。")));
					return;
				}
				if (pc.getInventory().getWeight100() > 82) { // 修改此部分會發生錯誤
					pc.sendPackets(String.valueOf(new S_SystemMessage("攜帶物品過重，無法使用。")));
					return;
				}
				if (pc.getInventory().checkItem(7021, 1)) { // 檢?的物品和數量
					// 這裡繼續編寫?的邏輯
				pc.getInventory().removeItem(l1iteminstance, 1);
					if (pc.isWarrior()) { // 戰士
						sealItem(pc, 816, 1, 0, 1, 0, true); // 戰士英雄套裝
					}
					if (pc.isKnight()) { // 騎士
						sealItem(pc, 818, 1, 0, 1, 0, true); // 騎士英雄套裝
					}
					if (pc.isDragonKnight()) { // 龍騎士
						sealItem(pc, 822, 1, 0, 1, 0, true); // 龍騎士英雄套裝
					}
					if (pc.isCrown()) { // 君主
						sealItem(pc, 817, 1, 0, 1, 0, true); // 君主英雄套裝
					}
					if (pc.isWizard()) { // 魔法師
						sealItem(pc, 820, 1, 0, 1, 0, true); // 魔法師英雄套裝
					}
					if (pc.isBlackWizard()) { // 幻術師
						sealItem(pc, 823, 1, 0, 1, 0, true); // 幻術師英雄套裝
					}
					if (pc.isElf()) { // 精靈
						sealItem(pc, 819, 1, 0, 1, 0, true); // 精靈英雄套裝
					}
					if (pc.isDarkElf()) { // 黑暗精靈
						sealItem(pc, 821, 1, 0, 1, 0, true); // 黑暗精靈英雄套裝
				}
			}
			break;
			case 447011: // 免費通行箱
				if (pc.getInventory().getSize() > 120) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("攜帶的物品太多了。")));
					return;
				}
				if (pc.getInventory().getWeight100() > 82) { // 修改此部分會發生錯誤
					pc.sendPackets(String.valueOf(new S_SystemMessage("攜帶物品過重，無法使用。")));
					return;
				}
				if (pc.getInventory().checkItem(447011, 1)) { // 檢?的物品和數量
					// 這裡繼續編寫?的邏輯
				pc.getInventory().removeItem(l1iteminstance, 1);
					if (pc.isWarrior()) { // 戰士
						sealItem(pc, 147, 1, 7, 1, 0, true); // 修練者的斧頭
						sealItem(pc, 147, 1, 7, 1, 0, true); // 修練者的斧頭
						sealItem(pc, 22300, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22301, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22302, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22303, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22304, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 203014, 1, 0, 1, 0, true); // 50級任務鐵匠的斧頭

						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 40014, 10, 0, 1, 0, true); // 勇氣
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					if (pc.isKnight()) { // 騎士
						sealItem(pc, 48, 1, 7, 1, 0, true); // 修練者的雙手劍
						sealItem(pc, 22300, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22301, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22302, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22303, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22304, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 56, 1, 0, 1, 0, true); // 50級任務死亡之劍

						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 40014, 10, 0, 1, 0, true); // 勇氣
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					if (pc.isDragonKnight()) { // 龍騎士
						sealItem(pc, 35, 1, 7, 1, 0, true); // 修練者的單手劍
						sealItem(pc, 22300, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22301, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22302, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22303, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22304, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 500, 1, 0, 1, 0, true); // 50級任務?滅者的?劍

						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 210035, 10, 0, 1, 0, true); // 骸骨
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					if (pc.isCrown()) { // 君主
						sealItem(pc, 35, 1, 7, 1, 0, true); // 修練者的單手劍
						sealItem(pc, 22300, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22301, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22302, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22303, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22304, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 51, 1, 0, 1, 0, true); // 50級任務的黃金杖

						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 40031, 10, 0, 1, 0, true); // 惡魔之血
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					if (pc.isWizard()) { // 魔法師
						sealItem(pc, 120, 1, 7, 1, 0, true); // 修練者的法杖
						sealItem(pc, 22306, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22307, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22308, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22309, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22310, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 20225, 1, 0, 1, 0, true); // 50級任務的魔力水晶球

						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					if (pc.isBlackWizard()) { // 幻術師
						sealItem(pc, 7000222, 1, 7, 1, 0, true); // 修練者的魔杖
						sealItem(pc, 22306, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22307, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22308, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22309, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22310, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 503, 1, 0, 1, 0, true); // 50級任務的藍寶石魔杖

						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 210036, 10, 0, 1, 0, true); // 尤格德拉
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					if (pc.isElf()) { // 精靈
						sealItem(pc, 175, 1, 7, 1, 0, true); // 修練者的弓
						sealItem(pc, 22306, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22307, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22308, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22309, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22310, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 50, 1, 0, 1, 0, true); // 50級任務的火焰之劍
						sealItem(pc, 184, 1, 0, 1, 0, true); // 50級任務的火焰之弓

						sealItem(pc, 40748, 3000, 0, 1, 0, true); // 箭
						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 40068, 10, 0, 1, 0, true); // 恢復藥水
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					if (pc.isDarkElf()) { // 黑暗精靈
						sealItem(pc, 156, 1, 7, 1, 0, true); // 修練者的爪
						sealItem(pc, 22300, 1, 7, 1, 0, true); // +0 修練者的皮?
						sealItem(pc, 22301, 1, 7, 1, 0, true); // +0 修練者的皮甲
						sealItem(pc, 22302, 1, 7, 1, 0, true); // +0 修練者的斗?
						sealItem(pc, 22303, 1, 7, 1, 0, true); // +0 修練者的皮手套
						sealItem(pc, 22304, 1, 7, 1, 0, true); // +0 修練者的皮?鞋
						sealItem(pc, 22337, 1, 0, 1, 0, true); // +0 修練者的腰帶
						sealItem(pc, 22312, 1, 7, 1, 0, true); // +0 修練者的T恤
						sealItem(pc, 321515, 1, 7, 1, 0, true); // +0 修練者的護腿
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22338, 1, 0, 1, 0, true); // +0 修練者的戒指
						sealItem(pc, 22339, 1, 0, 1, 0, true); // +0 修練者的耳環

						sealItem(pc, 13, 1, 0, 1, 0, true); // 50級任務的死亡之指

						sealItem(pc, 40088, 10, 0, 1, 0, true); // 變身卷軸
						sealItem(pc, 40100, 100, 0, 1, 0, true); // 瞬間移動卷軸
						sealItem(pc, 40010, 300, 0, 1, 0, true); // 紅寶石
						sealItem(pc, 40081, 10, 0, 1, 0, true); // 基蘭回返卷軸
						sealItem(pc, 40308, 100000, 0, 1, 0, true); // 金幣
					}
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aA[GM]: 新支援物品只能使用 '\\aG7天'\\aA。")));
			}
			break;
			case 40097: // 象牙塔的解除詛?卷軸
			case 40119: // 解除詛?卷軸
			case 140119:
			case 140329: // 原住民的圖騰
				L1Item template = null;
				for (L1ItemInstance eachItem : pc.getInventory().getItems()) {
					if (eachItem.getItem().getBless() != 2) {
						continue;
					}
					if (!eachItem.isEquipped() && (itemId == 40119 || itemId == 40097)) {
						// 僅解除裝備中的詛?
					continue;
				}
				int id_normal = eachItem.getItemId() - 200000;
				template = ItemTable.getInstance().getTemplate(id_normal);
				if (template == null) {
					continue;
				}
				if (pc.getInventory().checkItem(id_normal) && template.isStackable()) {
					pc.getInventory().storeItem(id_normal, eachItem.getCount());
					pc.getInventory().removeItem(eachItem, eachItem.getCount());
				} else {
					eachItem.setItem(template);
					pc.getInventory().updateItem(eachItem, L1PcInventory.COL_ITEMID);
					pc.getInventory().saveItem(eachItem, L1PcInventory.COL_ITEMID);
					eachItem.setBless(eachItem.getBless() - 1);
					pc.getInventory().updateItem(eachItem, L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(eachItem, L1PcInventory.COL_BLESS);
				}
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
				pc.sendPackets(String.valueOf(new S_ServerMessage(155))); // 1有人?助了?
				break;

			case 210083: // 太古的玉璽
				if (client.getAccount().getCharSlot() < 10) {
					client.getAccount().setCharSlot(client, client.getAccount().getCharSlot() + 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getAccount().is_changed_slot(true);
					pc.sendPackets(String.valueOf(new S_SystemMessage("角色欄位擴展完成")));
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage("角色欄位已滿")));
				}
				break;

			case 200000: // 回憶的燭光
				if (!pc.getMap().isSafetyZone(pc.getLocation())) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "只能在安全區域使用")));
					return;
				}
//            if (pc.getElixirStats() > 0) { // 如果玩家的體力藥劑加成大於0
//                pc.sendPackets(new S_ChatPacket(pc, "請先使用所有的體力藥劑加成後再繼續。"));
//                return;
//            }
//            if (pc.getInventory().checkItem(200000, 1)) { // 檢?玩家背包中是否有ID?200000的物品
//                if (pc.getLevel() != pc.getHighLevel()) { // 如果玩家的當前等級不是他們的最高等級
//                    pc.sendPackets(new S_SystemMessage("?的等級已下降。請先提升等級後再使用。"));
//                    return;
//                }
				if (pc.getLevel() > 54) {
					pc.getInventory().consumeItem(200000, 1);
					Random random = new Random(System.nanoTime());
					int candlelocx = 32723 + random.nextInt(10);
					int candlelocy = 32851 + random.nextInt(10);
					//L1Teleport.teleport(pc, locx, locy, (short) 5166, 5, true);s
					pc.set_MassTel(true);
					pc.start_teleport(candlelocx, candlelocy, 5166, pc.getHeading(), 18339, true, false);
					pc.스텟초기화();
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage("屬性重置僅適用於55級以上。")));
				}

			break;




			case 60035: // 룬 마력 제거제
				if (pc.getInventory().checkItem(60035, 1)) {
					if (l1iteminstance1 == null || l1iteminstance1.getItem() == null)
						return;

					int choiceItem = l1iteminstance1.getItem().getItemId();

					if (choiceItem >= 12800 && choiceItem <= 12844) { // 55級賢者之石符文
						pc.getInventory().consumeItem(60035, 1); // 消耗魔力
						pc.getInventory().consumeItem(choiceItem, 1);
						pc.getInventory().storeItem(60034, 1);
						pc.sendPackets("已轉換為失去魔力的符文。");
					} else if (choiceItem >= 12845 && choiceItem <= 12894) {  // 70級符文
						pc.getInventory().consumeItem(60035, 1);
						pc.getInventory().consumeItem(choiceItem, 1);
						pc.getInventory().storeItem(68097, 1);
						pc.sendPackets("已轉換為失去魔力的符文(70)。");
					} else if (choiceItem >= 12895 && choiceItem <= 12944) {  // 80級符文
						pc.getInventory().consumeItem(60035, 1);
						pc.getInventory().consumeItem(choiceItem, 1);
						pc.getInventory().storeItem(68098, 1);
						pc.sendPackets("已轉換為失去魔力的符文(80)。");
					} else if (choiceItem >= 12945 && choiceItem <= 12994) {  // 85級符文
						pc.getInventory().consumeItem(60035, 1);
						pc.getInventory().consumeItem(choiceItem, 1);
						pc.getInventory().storeItem(68099, 1);
						pc.sendPackets("已轉換為失去魔力的符文(85)。");
					} else if (choiceItem >= 12995 && choiceItem <= 13044) {  // 90級符文
						pc.getInventory().consumeItem(60035, 1);
						pc.getInventory().consumeItem(choiceItem, 1);
						pc.getInventory().storeItem(68100, 1);
						pc.sendPackets("已轉換為失去魔力的符文(90)。");
					} else {
						pc.sendPackets("可以用於賢者之石符文。");
						return;
					}
				}
				break;

		case 60027: // 高級符文魔力移除劑
			if (pc.getInventory().checkItem(60027, 1)) {
				if (l1iteminstance1 == null || l1iteminstance1.getItem() == null)
					return;

				int choiceItem = l1iteminstance1.getItem().getItemId();
				if (choiceItem >= 13045 && choiceItem <= 13094) { // 91級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68101, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(91)。");
				} else if (choiceItem >= 13095 && choiceItem <= 13144) { // 92級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68102, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(92)。");
				} else if (choiceItem >= 13145 && choiceItem <= 13194) { // 93級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68106, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(93)。");
				} else if (choiceItem >= 13195 && choiceItem <= 13244) { // 94級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68107, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(94)。");
				} else if (choiceItem >= 13245 && choiceItem <= 13294) { // 95級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68108, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(95)。");
				} else if (choiceItem >= 13295 && choiceItem <= 13344) { // 96級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68109, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(96)。");
				} else if (choiceItem >= 13345 && choiceItem <= 13394) { // 97級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68110, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(97)。");
				} else if (choiceItem >= 13395 && choiceItem <= 13444) { // 98級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68111, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(98)。");
				} else if (choiceItem >= 13445 && choiceItem <= 13494) { // 99級符文魔力
					pc.getInventory().consumeItem(60027, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68112, 1);
					pc.sendPackets("已轉換為失去魔力的高級符文(99)。");
				} else {
					pc.sendPackets("可以用於賢者之石符文。");
					return;
				}
			}
			break;
		case 410094:// 魔力的氣息
			if(l1iteminstance1 == null)
				return;

			if (pc.getInventory().checkItem(L1ItemId.MAGIC_BREATH, 1)) {
				int[] last = { 22232, 22233, 22234, 22235, 22236, 22237, 22238, 22239, 22240, 22241, 22242,
						22243, 22244, 22245, 22246, 22247, 22248, 22249 };
				int j = 0;
				int choiceItem = l1iteminstance1.getItem().getItemId();
				switch (choiceItem) {
				case 410114:
					j = 0;
					break;
				case 410115:
					j = 1;
					break;
				case 410116:
					j = 2;
					break;
				case 410117:
					j = 3;
					break;
				case 410118:
					j = 4;
					break;
				case 410119:
					j = 5;
					break;
				case 410109:
					j = 6;
					break;
				case 410124:
					j = 7;
					break;
				case 410110:
					j = 8;
					break;
				case 410125:
					j = 9;
					break;
				case 410111:
					j = 10;
					break;
				case 410126:
					j = 11;
					break;
				case 410112:
					j = 12;
					break;
				case 410113:
					j = 13;
					break;
				case 410120:
					j = 14;
					break;
				case 410121:
					j = 15;
					break;
				case 410122:
					j = 16;
					break;
				case 410123:
					j = 17;
					break;
				default:
					j = 18;
					break;
				}
				if (j == 18) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
					return;
				} else {
					pc.getInventory().consumeItem(L1ItemId.MAGIC_BREATH, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(last[j], 1);
					pc.sendPackets(String.valueOf(new S_SystemMessage("" + l1iteminstance1.getItem().getName() + "的封印已解除。")));
				}
			}
			break;
		case 210082: {// 裂縫的核心
			int itemId2 = l1iteminstance1.getItem().getItemId();
			if (itemId2 == 210075) {
				if (pc.getInventory().checkItem(210075)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(210076, 1);
				}
			} else if (itemId2 == 210079) {
				if (pc.getInventory().checkItem(210079)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(210080, 1);
				}
			} else if (itemId2 == 500208) {
				if (pc.getInventory().checkItem(500208)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(500202, 1);
				}
			} else if (itemId2 == 500209) {
				if (pc.getInventory().checkItem(500209)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(500203, 1);
				}
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // 沒有任何事情發生。
			}
		}
		break;
			case 40925: // 淨化藥水
				int earingId = l1iteminstance1.getItem().getItemId();
				if (earingId >= 40987 && 40989 >= earingId) { // 被詛?的黑耳環
					if (_random.nextInt(100) < Config.ServerRates.CreateChanceRecollection) {
						createNewItem(pc, earingId + 186, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // %0已經蒸發，無法使用。
					}
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else {
					pc.sendPackets(new S_ServerMessage(79)); // 沒有任何事情發生。
				}
			break;
		case 40926:
		case 40927:
		case 40928:
		case 40929:
			//神秘藥水：第1階段(1~4階段)
			int earing2Id = l1iteminstance1.getItem().getItemId();
			int potion1 = 0;
			int potion2 = 0;
			if (earing2Id >= 41173 && 41184 >= earing2Id) {
				// 耳環類
				if (itemId == 40926) {
					potion1 = 247;
					potion2 = 249;
				} else if (itemId == 40927) {
					potion1 = 249;
					potion2 = 251;
				} else if (itemId == 40928) {
					potion1 = 251;
					potion2 = 253;
				} else if (itemId == 40929) {
					potion1 = 253;
					potion2 = 255;
				}
				if (earing2Id >= (itemId + potion1) && (itemId + potion2) >= earing2Id) {
					if ((_random.nextInt(99) + 1) < Config.ServerRates.CreateChanceMysterious) {
						createNewItem(pc, (earing2Id - 12), 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(160, l1iteminstance1.getName())));
						// %0 以 %2 ?烈地 %1 閃?，但幸運地安然無恙。
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 什?也沒有發生。
				}
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // 什?也沒有發生。
			}
			break;
			case 3000106: // 修煉地城一層移動卷軸
				if (pc.getLevel() >= 90) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("該地城僅限75級以下進入。")));
					return;
				}
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 32809 + rx;
				int uy = 32727 + ry;
				if (itemId == 3000106) {
					pc.start_teleport(ux, uy, 25, pc.getHeading(), 18339, true, false);
				}
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
				cancelAbsoluteBarrier(pc); // ?對?障的解除
				break;
			case 3000444: // +10 惡夢之長弓 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands8(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("缺少水龍鱗片。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("缺少風龍鱗片。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("缺少地龍鱗片。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("缺少火龍鱗片。")));
					return;
				} else if (!pc.getInventory().checkItem(3000360, 100)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("缺少轉生的寶石。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("缺少工匠的對武器施法的卷軸。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(1136, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 惡夢之長弓不足。")));
				return;
			} else {
					pc.getInventory().consumeItem(3000444, 1);
					pc.getInventory().consumeItem(40393, 10);
					pc.getInventory().consumeItem(40394, 10);
					pc.getInventory().consumeItem(40395, 10);
					pc.getInventory().consumeItem(40396, 10);
					pc.getInventory().consumeItem(3000360, 100);
					pc.getInventory().consumeItem(810003, 3);
					pc.getInventory().consumeEnchantItem(1136, 8, 1);

					grantCraftingRecipe(pc, 1136, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 惡夢之弓已製作完成。", 1)));
				}
			break;
			case 3000445:// +10 颱風之斧 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands9(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40513, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("巨魔的眼淚不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(203006, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 颱風之斧不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000445, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40513, 10);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(203006, 8, 1);

					distributeCraftingRecipeItem(pc, 203006, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 颱風之斧已經製作完成。", 1)));
			}
			break;
			case 3000446:// +10 滅絕者的鏈劍 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands10(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(700020, 15)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("雪的結晶不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的武器魔法卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(203017, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 滅絕者的鏈劍不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000446, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(700020, 15);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(203017, 8, 1);

					distributeCraftingRecipeItem(pc, 203017, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 滅絕者的鏈劍已經製作完成。", 1)));
			}
			break;
			case 3000447:// +10 真戰鬥勇士大劍 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands11(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(410061, 270)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("魔物的氣息不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(505010, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 真戰鬥勇士大劍不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000447, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(410061, 270);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(505010, 8, 1);

					distributeCraftingRecipeItem(pc, 505010, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 真戰鬥勇士大劍已經製作完成。", 1)));
				}
			break;
			case 3000448:// +10 庫爾茲的劍 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands12(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40677, 15)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("黑暗的鑄錠不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(54, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 庫爾茲的劍不足。")));
					return;
			} else {
					pc.getInventory().consumeItem(3000448, 1);
					pc.getInventory().consumeItem(40393, 10);
					pc.getInventory().consumeItem(40394, 10);
					pc.getInventory().consumeItem(40395, 10);
					pc.getInventory().consumeItem(40396, 10);
					pc.getInventory().consumeItem(40677, 15);
					pc.getInventory().consumeItem(810003, 3);
					pc.getInventory().consumeEnchantItem(54, 8, 1);

					distributeCraftingRecipeItem(pc, 54, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 庫爾茲的劍已經製作完成。", 1)));
				}
			break;
			case 3000449:// +10 死亡騎士的火焰劍 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands13(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40644, 50)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("迷宮結構圖不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(58, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 死亡騎士的火焰劍不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000449, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40644, 50);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(58, 8, 1);


						distributeCraftingRecipeItem(pc, 58, 1, 10);
						pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 死亡騎士的火焰劍已經製作完成。", 1)));
					}
			break;
					case 3000450:// +10 羅恩的雙刀 (製作)
						pc.sendPackets(String.valueOf(new S_UserCommands17(1)));
						if (!pc.getInventory().checkItem(40395, 10)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
							return;
						} else if (!pc.getInventory().checkItem(40394, 10)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
							return;
						} else if (!pc.getInventory().checkItem(40396, 10)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
							return;
						} else if (!pc.getInventory().checkItem(40393, 10)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
							return;
						} else if (!pc.getInventory().checkItem(40324, 100)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("暗皇石不足。")));
							return;
						} else if (!pc.getInventory().checkItem(810003, 3)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
							return;
						} else if (!pc.getInventory().checkEnchantItem(76, 8, 1)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("+8 羅恩的雙刀不足。")));
							return;
			} else {
				pc.getInventory().consumeItem(3000450, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40324, 100);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(76, 8, 1);


				distributeCraftingRecipeItem(pc, 76, 1, 10);
				pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 羅恩的雙刀已經製作完成。", 1)));
				}
			break;
			case 3000451:// +10 真擊劍 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands14(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40487, 80)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("黃金板金不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(505009, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 真擊劍不足。")));
					return;
			} else {
					pc.getInventory().consumeItem(3000451, 1);
					pc.getInventory().consumeItem(40393, 10);
					pc.getInventory().consumeItem(40394, 10);
					pc.getInventory().consumeItem(40395, 10);
					pc.getInventory().consumeItem(40396, 10);
					pc.getInventory().consumeItem(40487, 80);
					pc.getInventory().consumeItem(810003, 3);
					pc.getInventory().consumeEnchantItem(505009, 8, 1);

					// 製作?擊劍
					distributeCraftingRecipeItem(pc, 505009, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 真擊劍已經製作完成。", 1)));
				}
			break;
			case 3000452:// +10 咆哮的雙刀 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands18(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40324, 100)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("暗皇石不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(203018, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 咆哮的雙刀不足。")));
					return;
				} else {
				pc.getInventory().consumeItem(3000452, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40324, 100);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(203018, 8, 1);

					// 製作咆哮的雙刀
					distributeCraftingRecipeItem(pc, 203018, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 咆哮的雙刀已經製作完成。", 1)));
				}
			break;
			case 3000453:// +10 澤洛斯的權杖 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands15(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40969, 700)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("靈魂的結晶體不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(202003, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 澤洛斯的權杖不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000453, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40969, 700);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(202003, 8, 1);

					giveCraftingRecipeItem(pc, 202003, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 澤洛斯的權杖已經製作完成。", 1)));
				}
			break;
			case 3000454:// +10 奈特巴爾德的雙手劍 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands16(1)));
				if (!pc.getInventory().checkItem(40395, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("水龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("風龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40396, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("地龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40393, 10)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("火龍鱗片不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40646, 60)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("蛇怪的角不足。")));
					return;
				} else if (!pc.getInventory().checkItem(810003, 3)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("工匠的對武器施法的卷軸不足。")));
					return;
				} else if (!pc.getInventory().checkEnchantItem(59, 8, 1)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("+8 奈特巴爾德的雙手劍不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000454, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40646, 60);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(59, 8, 1);

					giveCraftingRecipeItem(pc, 59, 1, 10);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "+10 奈特巴爾德的雙手劍已經製作完成。", 1)));
				}
			break;
			case 3000029:// 霸主長劍製作
				pc.sendPackets(new S_UserCommands1(1));
				if (!pc.getInventory().checkItem(40508, 500)) { // 奧里哈魯根
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "奧里哈魯根不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40460, 30)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "阿斯塔浩的灰燼不足。")));
					return;

				} else if (!pc.getInventory().checkItem(40052, 5)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "頂級鑽石不足。")));
					return;

				} else if (!pc.getInventory().checkItem(40053, 5)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "頂級紅寶石不足。")));
					return;

				} else if (!pc.getInventory().checkItem(40054, 5)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "頂級藍寶石不足。")));
					return;

				} else if (!pc.getInventory().checkItem(40055, 5)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "頂級祖母綠不足。")));
					return;

				} else if (!pc.getInventory().checkItem(40460, 30)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "阿斯塔浩的灰燼不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000029, 1);
				pc.getInventory().consumeItem(40508, 500);
				pc.getInventory().consumeItem(40460, 30);
				pc.getInventory().consumeItem(40052, 5);
				pc.getInventory().consumeItem(40053, 5);
				pc.getInventory().consumeItem(40054, 5);
				pc.getInventory().consumeItem(40055, 5);
				pc.getInventory().storeItem(57, 1);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "獲得了霸主長劍。")));
			}
			break;
			case 3000030:// 賽哈之弓 (製作)
				pc.sendPackets(String.valueOf(new S_UserCommands2(1)));
				if (!pc.getInventory().checkItem(181, 1)) { // 長弓
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "長弓 (1)個不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40394, 15)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "風龍的鱗片不足。")));
					return;

				} else if (!pc.getInventory().checkItem(40491, 30)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "獅鷲獸的羽毛不足。")));
					return;

				} else if (!pc.getInventory().checkItem(40498, 50)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "風之淚不足。")));
					return;
			} else {
				pc.getInventory().consumeItem(3000030, 1);
				pc.getInventory().consumeItem(181, 1);
				pc.getInventory().consumeItem(40394, 15);
				pc.getInventory().consumeItem(40491, 30);
				pc.getInventory().consumeItem(40498, 50);
				pc.getInventory().storeItem(190, 1);
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "獲得了賽哈之弓。")));
			}
			break;
			case 3000031:// 黑王刀 (製作)
				pc.sendPackets(new S_UserCommands3(1));
				if (!pc.getInventory().checkItem(81, 1)) { // 黑光雙刀
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "黑光雙刀 (1)個不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40466, 1)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "龍之心臟不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40525, 3)) { // 格蘭?伊
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "格蘭卡伊的眼淚不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40413, 9)) { // ?后
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "冰后之息不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40402, 10)) { // 詛?
					// 皮革
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "冰后之息不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40053, 3)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "頂級紅寶石不足。")));
					return;
				} else if (!pc.getInventory().checkItem(40308, 100000)) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "金幣不足。")));
					return;
				}
			} else {
				pc.getInventory().consumeItem(3000031, 1);
				pc.getInventory().consumeItem(81, 1);
				pc.getInventory().consumeItem(40525, 3);
				pc.getInventory().consumeItem(40413, 9);
				pc.getInventory().consumeItem(40402, 10);
				pc.getInventory().consumeItem(40053, 3);
				pc.getInventory().consumeItem(40466, 1);
				pc.getInventory().consumeItem(40308, 100000);
				pc.getInventory().storeItem(84, 1);
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "獲得了黑王刀。")));
			}
			break;
		case 3000354:// 艾娃王國水中地下城
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int rx = _random.nextInt(2);
			int ry = _random.nextInt(2);
			int ux = 32734 + rx;
			int uy = 32841 + ry;
			pc.start_teleport(ux, uy, 63, pc.getHeading(), 18339, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
		}
		cancelAbsoluteBarrier(pc); // 解除?對?障
			break;
		case 3000394:// 火龍的巢穴
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int rx = _random.nextInt(2);
			int ry = _random.nextInt(2);
			int ux = 33713 + rx;
			int uy = 32301 + ry;
			pc.start_teleport(ux, uy, 4, pc.getHeading(), 18339, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
		}
		cancelAbsoluteBarrier(pc); // 解除?對?障
			break;
		case 3000396:// 風龍的巢穴
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int rx = _random.nextInt(2);
			int ry = _random.nextInt(2);
			int ux = 34180 + rx;
			int uy = 32857 + ry;
			pc.start_teleport(ux, uy, 4, pc.getHeading(), 18339, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
		}
		cancelAbsoluteBarrier(pc); // 解除?對?障
			break;
		case 3000395:// 巴拉?斯巢穴
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int rx = _random.nextInt(2);
			int ry = _random.nextInt(2);
			int ux = 32779 + rx;
			int uy = 32751 + ry;
			pc.start_teleport(ux, uy, 2210, pc.getHeading(), 18339, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
		}
		cancelAbsoluteBarrier(pc); // 解除?對?障
			break;
		case 40824:// 修練地監2樓移動卷軸
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int rx = _random.nextInt(2);
			int ry = _random.nextInt(2);
			int ux = 32807 + rx;
			int uy = 32747 + ry;
			if (itemId == 40824) {
				pc.start_teleport(ux, uy, 26, pc.getHeading(), 18339, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
		}
		cancelAbsoluteBarrier(pc); // 解除?對?障
			break;
		case 40825:// 修練地監3樓移動卷軸
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int rx = _random.nextInt(2);
			int ry = _random.nextInt(2);
			int ux = 32810 + rx;
			int uy = 32765 + ry;
			if (itemId == 40825) {
				pc.start_teleport(ux, uy, 27, pc.getHeading(), 18339, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
		}
		cancelAbsoluteBarrier(pc); // 解除?對?障
			break;
		case 40417:// 靈魂水晶
		if ((pc.getX() >= 32667 && pc.getX() <= 32673)// 海賊島
				&& (pc.getY() >= 32978 && pc.getY() <= 32984) && pc.getMapId() == 440) {
			pc.start_teleport(32922, 32812, 430, 5, 18339, true, false);
		} else {
			pc.sendPackets(new S_ServerMessage(79));
			// 1 什?也沒有發生。
			}
			break;
		case 43201: // 克勞迪亞村護符
		if (pc.getLevel() >= 56) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("僅限55級以下使用。")));
			return;
		}
		pc.start_teleport(32649, 32865, 7783, pc.getHeading(), 18339, true, false);
//            pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(), l1iteminstance.getItem().get_mapid(), pc.getHeading(), 169, true, false);
			break;
		case 40003: // 燈籠油
			for (L1ItemInstance lightItem : pc.getInventory().getItems()) {
				if (lightItem.getItem().getItemId() == 40002 || lightItem.getItem().getItemId() == 7005) {
					lightItem.setRemainingTime(l1iteminstance.getItem().getLightFuel());
					pc.sendPackets(String.valueOf(new S_ItemName(lightItem)));
					pc.sendPackets(String.valueOf(new S_ServerMessage(230)));
					break;
				}
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000255: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId()); // --
			L1ItemInstance weapon_item = pc.getInventory().getItem(l); // --
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 203006 || id == 59 || id == 202003 || id == 1136 || id == 203018 || id == 203017 || id == 1120)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79))); // -- 什?也沒有發生
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 203006, 59, 202003, 1136, 203018, 203017, 1120 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(String.valueOf(new S_SystemMessage(new_item.getLogName() + " 獲得了。")));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 100001: { // 娃娃變更?法書
			int dollId = l1iteminstance1.getItem().getItemId();
			boolean isAppear = true;
			L1DollInstance doll = pc.getMagicDoll();
			L1ItemInstance item = null;
			
			if(doll != null) {
				if (doll.getItemObjId() == itemId) {
					isAppear = false;
					break;
				}
			}

			if (isAppear) {
				if (doll != null) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("無法在召喚娃娃的狀態下進行更改。")));
					return;
				}
			}
			if (dollId == 41248 // 魔法娃娃：巨大熊
					|| dollId == 41250 // 魔法娃娃：狼人
					|| dollId == 210086 // 魔法娃娃：暗影舞者
					|| dollId == 210072 // 魔法娃娃：甲殼蟹
					|| dollId == 210070 // 魔法娃娃：石魔像
					|| dollId == 210096 // 魔法娃娃：雪怪
					|| dollId == 500213 // 魔法娃娃：艾丁
					|| dollId == 41249 // 魔法娃娃：魅魔
					|| dollId == 210071 // 魔法娃娃：長老
					|| dollId == 210105 // 魔法娃娃：石?
					|| dollId == 447012 // 魔法娃娃：冠軍
					|| dollId == 447013 // 魔法娃娃：鳥
					|| dollId == 447014 // 魔法娃娃：江南Style
					|| dollId == 500215 // 魔法娃娃：稻草人
					|| dollId == 447016 // 魔法娃娃：巫妖
					|| dollId == 447015 // 魔法娃娃：小妖精
					|| dollId == 500214 // 魔法娃娃：斯巴托伊
					|| dollId == 447017 // 魔法娃娃：德雷克
					|| dollId == 510216 // 魔法娃娃：雪人A
					|| dollId == 510217 // 魔法娃娃：雪人B
					|| dollId == 510218 // 魔法娃娃：雪人C
					|| dollId == 510219 // 魔法娃娃：巨人
					|| dollId == 510220 // 魔法娃娃：獨眼巨人
					|| dollId == 510221 // 魔法娃娃：黑長老
					|| dollId == 510222) { // 魔法娃娃：魅魔女王

				pc.getInventory().removeItem(l1iteminstance1, 1);
				pc.getInventory().removeItem(l1iteminstance, 1);

				int i = _random.nextInt(1060) + 1;
				if (i <= 150) { // 15%
					item = pc.getInventory().storeItem(41248, 1); // 魔法娃娃：巨大熊
				} else if (i <= 260) { // 11%
					item = pc.getInventory().storeItem(41250, 1); // 魔法娃娃：狼人
				} else if (i <= 370) { // 11%
					item = pc.getInventory().storeItem(210086, 1); // 魔法娃娃：暗影舞者
				} else if (i <= 480) { // 11%
					item = pc.getInventory().storeItem(210072, 1); // 魔法娃娃：甲殼蟹
				} else if (i <= 590) { // 11%
					item = pc.getInventory().storeItem(210070, 1); // 魔法娃娃：石魔像
				} else if (i <= 680) { // 9%
					item = pc.getInventory().storeItem(210096, 1); // 魔法娃娃：雪怪
				} else if (i <= 770) { // 9%
					item = pc.getInventory().storeItem(500213, 1); // 魔法娃娃：艾丁
				} else if (i <= 810) { // 4%
					item = pc.getInventory().storeItem(41249, 1); // 魔法娃娃：魅魔
				} else if (i <= 850) { // 4%
					item = pc.getInventory().storeItem(210071, 1); // 魔法娃娃：長老
				} else if (i <= 880) { // 3%
					item = pc.getInventory().storeItem(210105, 1); // 魔法娃娃：石?
				} else if (i <= 900) { // 2%
					item = pc.getInventory().storeItem(447012, 1); // 魔法娃娃：冠軍
				} else if (i <= 920) { // 2%
					item = pc.getInventory().storeItem(447013, 1); // 魔法娃娃：鳥
				} else if (i <= 940) { // 2%
					item = pc.getInventory().storeItem(447014, 1); // 魔法娃娃：江南Style
				} else if (i <= 960) { // 2%
					item = pc.getInventory().storeItem(500215, 1); // 魔法娃娃：稻草人
				} else if (i <= 970) { // 1%
					item = pc.getInventory().storeItem(447016, 1); // 魔法娃娃：巫妖
				} else if (i <= 980) { // 1%
					item = pc.getInventory().storeItem(447015, 1); // 魔法娃娃：小妖精
				} else if (i <= 990) { // 1%
					item = pc.getInventory().storeItem(500214, 1); // 魔法娃娃：史巴托
				} else if (i <= 1000) { // 1%
					item = pc.getInventory().storeItem(447017, 1); // 魔法娃娃：德雷克
				} else if (i <= 1010) { // 1%
					item = pc.getInventory().storeItem(510216, 1); // 魔法娃娃：雪人A
				} else if (i <= 1020) { // 1%
					item = pc.getInventory().storeItem(510217, 1); // 魔法娃娃：雪人B
				} else if (i <= 1030) { // 1%
					item = pc.getInventory().storeItem(510218, 1); // 魔法娃娃：雪人C
				} else if (i <= 1040) { // 1%
					item = pc.getInventory().storeItem(510221, 1); // 魔法娃娃：黑長老
				} else if (i <= 1050) { // 1%
					item = pc.getInventory().storeItem(510222, 1); // 魔法娃娃：魅魔女王
				} else if (i <= 1055) { // 0.5%
					item = pc.getInventory().storeItem(510219, 1); // 魔法娃娃：巨人
				} else if (i <= 1060) { // 1%
					item = pc.getInventory().storeItem(510220, 1); // 魔法娃娃：獨眼巨人
				}
				pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getLogName())));
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("無法更改的物品。")));
			}
		}
			break;
		case 3000148: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 12 || id == 61 || id == 134 || id == 86 || id == 202011 || id == 202012 || id == 202013 || id == 202014)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 12, 61, 134, 86, 202011, 202012, 202013, 202014 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(String.valueOf(new S_SystemMessage(new_item.getLogName() + " 已獲得。")));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 719: {
			L1ItemInstance Skill_item = pc.getInventory().getItem(l);
			if (Skill_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int id = Skill_item.getItemId();
			if (!(id == 40222 || id == 41148 || id == 5559 || id == 210125)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int[] random_item = { 40222, 41148, 5559, 210125 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1);
			pc.getInventory().removeItem(Skill_item, 1);
			pc.sendPackets(String.valueOf(new S_SystemMessage(new_item.getLogName() + " 已獲得。")));
		}
			break;
		case 707: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 22208 || id == 22209 || id == 22210 || id == 22211
					|| id == 22200 || id == 22201 || id == 22202 || id == 22203
					|| id == 22204 || id == 22205 || id == 22206 || id == 22207
					|| id == 22196 || id == 22197 || id == 22198 || id == 22199)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 22208, 22209, 22210, 22211,
					22200, 22201, 22202, 22203,
					22204, 22205, 22206, 22207,
					22196, 22197, 22198, 22199};
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 獲得了。"));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 706: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 900081 || id == 900082 || id == 900083 || id == 900084)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 900081, 900082, 900083, 900084 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(String.valueOf(new S_SystemMessage(new_item.getLogName() + " 獲得了。")));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 705: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 900093 || id == 900094 || id == 900095 || id == 900096 || id == 900097 || id == 900098 || id == 900099)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 900093, 900094, 900095, 900096, 900097, 900098, 900099 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(String.valueOf(new S_SystemMessage(new_item.getLogName() + " 獲得了。")));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 704: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 222337 || id == 222339 || id == 222341)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 222337, 222339, 222341 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 獲得了。"));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 703: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 222330 || id == 222331 || id == 222332 || id == 222333 || id == 222334 || id == 222335 || id == 222336)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 222330, 222331, 222332, 222333, 222334, 222335, 222336 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 獲得了。"));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 4100076:
			long timeItem7 = System.currentTimeMillis();
		int useTimeItem7 = 600; // 10分鐘後
		if (pc.hasSkillEffect(L1SkillId.DRAGON_SET)) {
			int n = pc.getSkillEffectTimeSec(L1SkillId.DRAGON_SET);
			pc.sendPackets(new S_SystemMessage(String.format("%d秒後可以使用。", n)));
				return;
			}
			L1SkillUse bb = new L1SkillUse();
			bb.handleCommands(pc, L1SkillId.DRAGON_SET, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			pc.send_effect(9009);
			pc.setSkillEffect(L1SkillId.DRAGON_SET, useTimeItem7 * 1000);
			break;
		case 40566: // 神秘的海螺殼
		if (pc.isElf()
				&& (pc.getX() >= 33971 && pc.getX() <= 33975)
				&& (pc.getY() >= 32324 && pc.getY() <= 32328) && pc.getMapId() == 4
				&& !pc.getInventory().checkItem(40548)) { // 幽靈的信封
				boolean found = false;
				L1MonsterInstance mob = null;
				for (L1Object obj : L1World.getInstance().getObject()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob != null) {
							if (mob.getNpcTemplate().get_npcId() == 45300) {
								found = true;
								break;
							}
						}
					}
				}
				if (found) {
					case 40566: // 神秘的海螺殼
						if (pc.isElf()
								&& (pc.getX() >= 33971 && pc.getX() <= 33975)
								&& (pc.getY() >= 32324 && pc.getY() <= 32328) && pc.getMapId() == 4
								&& !pc.getInventory().checkItem(40548)) { // 幽靈的信封
							// 如果條件都滿足
							pc.sendPackets(new S_ServerMessage(79)); // 1 什?都沒有
						} else {
							// 如果條件不滿足，生成一個新怪物
							L1SpawnUtil.spawn(pc, 45300, 0, 0); // 古代人的幽靈
						}
				} else {
					// 如果不是精靈或不在特定地點，發送消息
					pc.sendPackets(new S_ServerMessage(79)); // 1 什?都沒有
				}
			break;

			case 40557: // 殺生簿（古魯丁村）
			if (pc.getX() == 32620 && pc.getY() == 32641 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45883) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45883, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 40558: // 殺生簿（奇岩村）
			if (pc.getX() == 33513 && pc.getY() == 32890 && pc.getMapId() == 4) {
				L1NpcInstance npc = null;
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45889) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45889, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 40559: // 殺生簿（亞丁村）
			if (pc.getX() == 34215 && pc.getY() == 33195 && pc.getMapId() == 4) {
				L1NpcInstance npc = null;
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45888) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45888, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 40560: // 殺生簿（伍德貝克村）
				if (pc.getX() == 32580 && pc.getY() == 33260 && pc.getMapId() == 4) {
					for (L1Object object : L1World.getInstance().getObject()) {
						if (object instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) object;
							if (npc.getNpcTemplate().get_npcId() == 45886) {
								pc.sendPackets(new S_ServerMessage(79));
								return;
							}
						}
					}
					L1SpawnUtil.spawn(pc, 45886, 0, 300000);
				} else {
					pc.sendPackets(new S_ServerMessage(79));
				}
				break;
			case 40561: // 殺生簿（肯特村）
			if (pc.getX() == 33046 && pc.getY() == 32806 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45885) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45885, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 40562: // 殺生簿（海音村）
			if (pc.getX() == 33447 && pc.getY() == 33476 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45887) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45887, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 40563: // 殺生簿（火田村）
			if (pc.getX() == 32730 && pc.getY() == 32426 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45884) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45884, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 40826: // 修煉地下城4層移動卷軸
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 32799 + rx;
				int uy = 32798 + ry;
				if (itemId == 40826) {
					// L1Teleport.teleport(pc, ux, uy, (short) 28,
					// pc.getHeading(), true);
					pc.start_teleport(ux, uy, 28, pc.getHeading(), 18339, true, false);
				}
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
				cancelAbsoluteBarrier(pc); // ?對?障的解除
				break;
			case 3000393:
				if (l1iteminstance1.getItem().getType2() == 0){
					pc.sendPackets(new S_SystemMessage("只能用於裝備上。"));
					return;
				}
				if (l1iteminstance1.get_durability() > 0){
					pc.sendPackets(new S_SystemMessage("損壞的物品無法更改。"));
					return;
				}
				if (l1iteminstance1.isEquipped()){
					pc.sendPackets(new S_SystemMessage("無法使用於裝備中的物品。"));
					return;
				}
				if (!l1iteminstance1.isIdentified()) {
					pc.sendPackets(new S_SystemMessage("只能使用於鑑定過的物品。"));
					return;
				}
				l1iteminstance1.setIdentified(false);
				pc.getInventory().tradeItem(l1iteminstance1, l1iteminstance1.getCount(), pc.getInventory());
				pc.sendPackets(new S_SystemMessage(l1iteminstance1.getLogName() + "被黑暗的陰影侵染。"));
				pc.sendPackets(new S_SystemMessage("該物品已經更新。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100661:
			case 40017: // 碧玉藥水
			case 40507: // 樹精的莖
				if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 虛弱藥水狀態
					pc.sendPackets(new S_ServerMessage(698)); // 受魔力影響，無法進行任何操作
				} else {
					cancelAbsoluteBarrier(pc); // ?對?障的解除
				pc.sendPackets(new S_SkillSound(pc.getId(), 192));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 192));
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.curePoison();
			}
			break;
		case 40616:
		case 40782:
			case 40783: // 影子神殿3層的?匙
			if ((pc.getX() >= 32698 && pc.getX() <= 32702) && (pc.getY() >= 32894 && pc.getY() <= 32898)
					&& pc.getMapId() == 523) {
				pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
						l1iteminstance.getItem().get_mapid(), 5, 18339, true, false);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 40700: // 銀笛
				pc.sendPackets(new S_Sound(10));
				pc.broadcastPacket(new S_Sound(10));
				if ((pc.getX() >= 32619 && pc.getX() <= 32623) && (pc.getY() >= 33120 && pc.getY() <= 33124)
						&& pc.getMapId() == 440) {
					// 海盜希馬魔法廣場座標
				boolean found = false;
				L1MonsterInstance mob = null;
				for (L1Object obj : L1World.getInstance().getObject()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob != null) {
							if (mob.getNpcTemplate().get_npcId() == 45875) {
								found = true;
								break;
							}
						}
					}
				}
				if (found) {
				} else {
					L1SpawnUtil.spawn(pc, 45875, 0, 0);
				}
			}
			break;
			case 41121: // ?赫爾的契約書
			if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END
					|| pc.getInventory().checkItem(41122, 1)) {
				pc.sendPackets(new S_ServerMessage(79));
			} else {
				createNewItem(pc, 41122, 1);
			}
			break;
			case 41130: // 血痕的契約書
			if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END
					|| pc.getInventory().checkItem(41131, 1)) {
				pc.sendPackets(new S_ServerMessage(79));
			} else {
				createNewItem(pc, 41131, 1);
			}
			break;
			case 40692: // 完成的寶藏地圖
				if (pc.getInventory().checkItem(40621)) {
					// 1 什?都沒有發生。
					pc.sendPackets(new S_ServerMessage(79));
				} else if ((pc.getX() >= 32856 && pc.getX() <= 32858) && (pc.getY() >= 32857 && pc.getY() <= 32858)
						&& pc.getMapId() == 443) { // 海盜島的
				pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
						l1iteminstance.getItem().get_mapid(), 5, 18339, true, false);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 41208: // ?將消逝的靈魂
			if ((pc.getX() >= 32844 && pc.getX() <= 32845) && (pc.getY() >= 32693 && pc.getY() <= 32694)
					&& pc.getMapId() == 550) {
				pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
						l1iteminstance.getItem().get_mapid(), 5, 18339, true, false);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 4101027:
			if (!L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
				pc.sendPackets(new S_SystemMessage("只能在據點內使用。"));
				return;
			}
			ClanDugeon.getInstance().DailyClanDungeonStart(l1iteminstance.getItemId() != 4101027 ? l1iteminstance.getItemId() - 4101004 : 3, pc);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 40964: // 黑魔法粉末
			int historybookId = l1iteminstance1.getItem().getItemId();
			if (historybookId >= 41011 && 41018 >= historybookId) {
				if ((_random.nextInt(99) + 1) <= Config.ServerRates.CreateChanceHistoryBook) {
					createNewItem(pc, historybookId + 8, 1);
				} else {
					pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
				}
				pc.getInventory().removeItem(l1iteminstance1, 1);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
			case 41036: // 草藥
				int diaryId = l1iteminstance1.getItem().getItemId();
				if (diaryId >= 41038 && 41047 >= diaryId) {
					if ((_random.nextInt(99) + 1) <= Config.ServerRates.CreateChanceDiary) {
						createNewItem(pc, diaryId + 10, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
						// 1%0已經蒸發了。
					}
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					// 1 什?都沒有發生。
				}
			break;
			case 30084: // 象牙塔的翡翠藥水
			/*
			if (!(pc.getMapId() == 7783 || pc.getMapId() == 12152 || pc.getMapId() == 12149 || pc.getMapId() == 12154 || pc.getMapId() == 3
			|| pc.getMapId() == 12358 || pc.getMapId() == 12153 || pc.getMapId() == 12146
			|| pc.getMapId() == 12147 || pc.getMapId() == 12148 || pc.getMapId() == 12258
			|| pc.getMapId() == 12150 || pc.getMapId() == 12257 || pc.getMapId() == 12358)) {
			pc.sendPackets(new S_SystemMessage("只能在克勞迪亞地圖中使用。"));
			return;
			}
			*/
				if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 衰退藥水狀態
					pc.sendPackets(new S_ServerMessage(698));
			// 由於魔力的影響，無法?用任何東西。
				} else {
					cancelAbsoluteBarrier(pc); // 解除?對?障狀態
					pc.sendPackets(new S_SkillSound(pc.getId(), 192));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 192));
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.curePoison();
				}
			break;
			case 3000193: // 約頓1樓
				if (pc.getMap().isEscapable() || pc.isGm()) {
					int rx = _random.nextInt(2);
					int ux = 32805 + rx;
					int uy = 32724 + rx;

					pc.start_teleport(ux, uy, 19, pc.getHeading(), 18339, true, false);
					pc.getInventory().removeItem(l1iteminstance, 1);
					cancelAbsoluteBarrier(pc); // 解除?對?障狀態
				}
			break;
			case 3000226: // 召喚活動棒1
			int rx10 = _random.nextInt(10);
			int ry10 = _random.nextInt(10);

			int rx11 = _random.nextInt(20);
			int ry11 = _random.nextInt(20);

			int rx12 = _random.nextInt(30);
			int ry12 = _random.nextInt(30);

			int ux10 = 32926 + rx10;
			int uy10 = 33250 + ry10;
			int um = 4;

			int ux11 = 32926 + rx11;
			int uy11 = 33250 + ry11;

			int ux12 = 32926 + rx12;
			int uy12 = 33250 + ry12;

			L1SpawnUtil.spawnfieldboss(ux10, uy10, (short) um, 45545, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux11, uy11, (short) um, 7000091, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux12, uy12, (short) um, 7000092, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux12, uy12, (short) um, 7000089, 0, 0, 0);
			break;
			case 3000227: // 召喚活動棒2
			int rx13 = _random.nextInt(10);
			int ry13 = _random.nextInt(10);

			int rx14 = _random.nextInt(20);
			int ry14 = _random.nextInt(20);

			int rx15 = _random.nextInt(30);
			int ry15 = _random.nextInt(30);

			int ux13 = 32926 + rx13;
			int uy13 = 33250 + ry13;
			int um1 = 4;

			int ux14 = 32926 + rx14;
			int uy14 = 33250 + ry14;

			int ux15 = 32926 + rx15;
			int uy15 = 33250 + ry15;

			L1SpawnUtil.spawnfieldboss(ux13, uy13, (short) um1, 45203, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux14, uy14, (short) um1, 45206, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux15, uy15, (short) um1, 45257, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux13, uy13, (short) um1, 45263, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux14, uy14, (short) um1, 45341, 0, 0, 0);
			break;
		case 3000228:// 召喚活動棒3
			int rx16 = _random.nextInt(10);
			int ry16 = _random.nextInt(10);

			int rx17 = _random.nextInt(20);
			int ry17 = _random.nextInt(20);

			int rx18 = _random.nextInt(30);
			int ry18 = _random.nextInt(30);

			int ux16 = 32926 + rx16;
			int uy16 = 33250 + ry16;
			int um2 = 4;

			int ux17 = 32926 + rx17;
			int uy17 = 33250 + ry17;

			int ux18 = 32926 + rx18;
			int uy18 = 33250 + ry18;

			L1SpawnUtil.spawnfieldboss(ux16, uy16, (short) um2, 707001, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux17, uy17, (short) um2, 707002, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux18, uy18, (short) um2, 707007, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux16, uy16, (short) um2, 707008, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux17, uy17, (short) um2, 707013, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux18, uy18, (short) um2, 707015, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux16, uy16, (short) um2, 707016, 0, 0, 0);
			break;
			case 3000432:
				petbuy(client, 46043, 41159, 0); // 火焰袋鼠
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000433:
				petbuy(client, 46045, 41159, 0); // 恐怖熊?項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000434:
				petbuy(client, 46046, 41159, 0); // 金龍項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000435:
				petbuy(client, 45695, 41159, 0); // 高級?子項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000436:
				petbuy(client, 45693, 41159, 0); // 高級聖伯納犬項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000437:
				petbuy(client, 45692, 41159, 0); // 高級比格犬項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000438:
				petbuy(client, 45694, 41159, 0); // 高級狐狸項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000439:
				petbuy(client, 45696, 41159, 0); // 高級?項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000440:
				petbuy(client, 45697, 41159, 0); // 高級浣熊項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 3000441:
				petbuy(client, 45712, 41159, 0); // 韓版牧羊犬項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000442:
				petbuy(client, 45710, 41159, 0); // 戰虎項鍊
				pc.getInventory().removeItem(l1iteminstance, 1);
				break;
			case 3000194: { // PC網?出席箱子
				// -- 隨機?次數
				// -- 隨機?
				int[] itemid = { 40308 }; // 金幣
				int random500 = CommonUtil.random(100);

				int[] itemid2 = { 3000176 }; // 特殊道具
				int random502 = CommonUtil.random(100);

				if (random500 <= 10) { // -- 5萬到1000萬。
					createNewItem(pc, itemid[0], CommonUtil.random(10000, 15000));
				} else { // -- 5萬到500萬。
					createNewItem(pc, itemid[0], CommonUtil.random(15000, 20000));
				}
				if (random502 <= 0.3) { // -- 2% 機率
					createNewItem(pc, itemid2[0], CommonUtil.random(1, 1));
				}
				pc.getInventory().removeItem(l1iteminstance, 1);
			}
			break;
		case 41299: {
			int[] itemid = {41302}; // 記憶碎片
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // -- 50萬到100萬。
				createNewItem(pc, itemid[0], CommonUtil.random(500000, 600000));
			} else { // -- 30萬到50萬。
				createNewItem(pc, itemid[0], CommonUtil.random(300000, 400000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41300: {
			int[] itemid = { 41302 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // -- 5萬到1000萬。
				createNewItem(pc, itemid[0], CommonUtil.random(5000000, 6000000));
			} else {// -- 5萬到500萬
				createNewItem(pc, itemid[0], CommonUtil.random(3000000, 4000000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41303: {
			int[] itemid = { 41302 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // -- 5萬到1000萬。
				createNewItem(pc, itemid[0], CommonUtil.random(600000, 700000));
			} else {// -- 5萬到500萬。.
				createNewItem(pc, itemid[0], CommonUtil.random(400000, 500000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41304: {
			int[] itemid = { 41302 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // -- 5萬到1000萬。
				createNewItem(pc, itemid[0], CommonUtil.random(6000000, 7000000));
			} else {// -- 5萬到500萬.
				createNewItem(pc, itemid[0], CommonUtil.random(4000000, 5000000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000145: { // 妖精的羽毛隨機箱
			int[] itemid = { 41159 };
			int random500 = CommonUtil.random(100);
			
			if (random500 <= 90) { // -- 5萬到1000萬。
				createNewItem(pc, itemid[0], CommonUtil.random(50, 100));
			} else {// -- 5萬到500萬.
				createNewItem(pc, itemid[0], CommonUtil.random(100, 200));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000146: { // 歐林的石榴石隨機箱
			int[] itemid = { 3000246 };
			int random500 = CommonUtil.random(100);
			
			if (random500 <= 90) { // -- 5萬到1000萬。
				createNewItem(pc, itemid[0], CommonUtil.random(30, 100));
			} else {// -- 5萬到500萬.
				createNewItem(pc, itemid[0], CommonUtil.random(100, 200));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000195:
//			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "pbook0"));
			for (L1Object obj : L1World.getInstance().getObject()) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcId() == 4200015) {
						pc.sendPackets(new S_Board(npc));
						break;
					}
				}
			}
			break;
		case 4100203:// 全欄位解鎖券
			pc.getQuest().set_end(L1Quest.QUEST_EARRING_SLOT60);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_EARRING));
			
			pc.getQuest().set_end(L1Quest.QUEST_RING_LEFT_SLOT60);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_LRING));
			
			pc.getQuest().set_end(L1Quest.QUEST_RING_RIGHT_SLOT60);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_RRING));
			
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_SHOULD);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_SHOULD));
			
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_BADGE));
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_BADGE);
			
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12358));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100140:// 귀걸이 확장권:60레벨
			pc.getQuest().set_end(L1Quest.QUEST_EARRING_SLOT60);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_EARRING));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100141:// 반지 확장권:60레벨
			pc.getQuest().set_end(L1Quest.QUEST_RING_LEFT_SLOT60);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_LRING));
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100142:// 반지 확장권:60레벨
			pc.getQuest().set_end(L1Quest.QUEST_RING_RIGHT_SLOT60);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_RRING));
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100143:// 견갑 확장권:60레벨
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_SHOULD);
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_SHOULD));
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12358));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100144:// 휘장 확장권:60레벨
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_BADGE));
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_BADGE);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12360));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 30001877: //반지 확장권:100레벨
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_RRING100));
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_BADGE);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 30001878: //귀걸이 확장권:101레벨
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_LEARRING101));
			pc.getQuest().set_end(L1Quest.QUEST_EARRING_LEFT_SLOT101);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
			pc.getInventory().removeItem(l1iteminstance, 1);
			
			break;
		case 30001879: //귀걸이 확장권:103레벨
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_REARRING103));
			pc.getQuest().set_end(L1Quest.QUEST_EARRING_RIGHT_SLOT103);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40067:// 쑥송편
		case 41414:// 복월병
			pc.setCurrentMp(pc.getCurrentMp() + (8 + _random.nextInt(9)));
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));// 이펙트발생
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40066:// 송편
		case 41413:// 월병
			pc.setCurrentHp(pc.getCurrentHp() + (33 + _random.nextInt(56)));
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));// 이펙트발생
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 721:
			if (pc.getLevel() < 89) {
				pc.sendPackets("必須達到 89 級才可使用。");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 722:
			if (pc.getLevel() < 90) {
				pc.sendPackets("必須達到 90 級才可使用。");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 500220://마녀
			long timeItem = System.currentTimeMillis();
			int useTimeItem = 1800;// 30분뒤
			if(pc.hasSkillEffect(L1SkillId.WITCH_MANA_POTION)){
				int n = pc.getSkillEffectTimeSec(L1SkillId.WITCH_MANA_POTION);
				pc.sendPackets(new S_SystemMessage(String.format("%d秒後可使用。", n)));
				return;
			}
			pc.setSkillEffect(L1SkillId.WITCH_MANA_POTION, useTimeItem * 1000);
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // ?的%0正在恢復
			pc.setCurrentMp(pc.getCurrentMp() + (1000 + _random.nextInt(1))); // 恢復 1000~1000 的 MP
			pc.sendPackets(new S_SkillSound(pc.getId(), 190)); // ?生效果
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100301:
			long timeItem1 = System.currentTimeMillis();
			int useTimeItem1 = 1800;// 5분뒤
			if(pc.hasSkillEffect(L1SkillId.WITCH_MANA_POTION1)){
				int n = pc.getSkillEffectTimeSec(L1SkillId.WITCH_MANA_POTION1);
				pc.sendPackets(new S_SystemMessage(String.format("%d秒後可使用。", n)));
				return;
			}
			pc.setSkillEffect(L1SkillId.WITCH_MANA_POTION1, useTimeItem1 * 1000);
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + (1000 + _random.nextInt(1))); // 15~30
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));// 이펙트발생
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100623:
			int useTimeItem2 = 600;// 10분뒤
			if(pc.hasSkillEffect(L1SkillId.WITCH_MANA_POTION)){
				int n = pc.getSkillEffectTimeSec(L1SkillId.WITCH_MANA_POTION);
				pc.sendPackets(new S_SystemMessage(String.format("%d秒後可使用。", n)));
				return;
			}
			pc.setSkillEffect(L1SkillId.WITCH_MANA_POTION, useTimeItem2 * 1000);
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + (8 + _random.nextInt(15))); // 8~17
			pc.send_effect(190, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 410002:// 빛나는 나뭇잎
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + 44);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40735:// 용기의 코인
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + 60);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40042:// 정신력의 물약
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + 50);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 41404:// 쿠작의 영약
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + (80 + _random.nextInt(21))); // 80~100
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 41412:// 금쫑즈
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + (5 + _random.nextInt(16))); // 5~20
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000048: // 꼬마 요정의 마음
			pc.sendPackets(5387);
			break;
		case 40493:// 마법의 플룻
			pc.sendPackets(new S_Sound(165));
			pc.broadcastPacket(new S_Sound(165));
			L1GuardianInstance guardian = null;
			for (L1Object visible : pc.getKnownObjects()) {
				if (visible instanceof L1GuardianInstance) {
					guardian = (L1GuardianInstance) visible;
					if (guardian.getNpcTemplate().get_npcId() == 70850) { // 빵
						if (createNewItem(pc, 88, 1)) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
				}
			}
			break;
		case 40325:// 2단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3237 + _random.nextInt(2);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 40326:// 3단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3229 + _random.nextInt(3);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 40327:// 4단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3241 + _random.nextInt(4);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 40328:// 6단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3204 + _random.nextInt(6);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				// \f1 아무것도 일어나지 않았습니다.
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		/*case 3000150:// 生命之眼 (製作)
		pc.sendPackets(new S_UserCommands6(1));
		if (!pc.getInventory().checkItem(3000150, 100)) {
		pc.sendPackets(new S_SystemMessage("生命之眼碎片不足。"));
		return;
		} else {
		pc.getInventory().consumeItem(3000150, 100);
		pc.getInventory().storeItem(410038, 1);
		pc.sendPackets(new S_ChatPacket(pc, "生命之眼已製作。"));
		}
		break;*/
			case 41027:// 完成的拉斯塔巴德歷史書
				pc.sendPackets(new S_UserCommands7(1));
				if (!pc.getInventory().checkItem(41027, 1)) {
					pc.sendPackets(new S_SystemMessage("完成的拉斯塔巴德歷史書不足。"));
					return;
				} else {
					pc.getInventory().consumeItem(41027, 1);
					pc.getInventory().storeItem(40965, 1);
					pc.sendPackets(new S_SystemMessage("拉斯塔巴德武器製作?技書已製作完成。"));
				}
				break;
			break;
		case 4100622:
			if (/*pc.getAccount().getBlessOfAinBonusPoint() >= 100000 && */pc.getAccount().getBlessOfAinBonusPoint() <= 90000000) {
				pc.sendPackets("阿因哈薩德 100,000 點已充電。");
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.save();
				SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
			} else {
				pc.sendPackets("阿因哈薩德點數僅在90,000,000以下時才能使用。");
			}
			break;
		case 4200294:
			if (/*pc.getAccount().getBlessOfAinBonusPoint() >= 100000 && */pc.getAccount().getBlessOfAinBonusPoint() <= 90000000) {
				pc.getAccount().addGmBlessOfAinBonusPoint(50000);
				pc.sendPackets("阿因哈薩德 50,000 點已充電。");
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.save();
				SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
			} else {
				pc.sendPackets("阿因哈薩德點數僅在90,000,000以下時才能使用。");
			}
			break;
		case 30001359:
			if (/*pc.getAccount().getBlessOfAinBonusPoint() >= 100000 && */pc.getAccount().getBlessOfAinBonusPoint() <= 90000000) {
				pc.getAccount().addGmBlessOfAinBonusPoint(5000);
				pc.sendPackets("阿因哈薩德 5,000 點已充電。");
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.save();
				SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
			} else {
				pc.sendPackets("阿因哈薩德點數僅在90,000,000以下時才能使用。");
			}
			break;
		case 490028: // 라우풀물약
			if (pc.getLawful() >= 32767) {
				pc.sendPackets("當前條件不允許使用。");
				return;
			}
			if (pc.getLawful() >= -32768 && pc.getLawful() <= 32767) {
				pc.addLawful(10000);
				pc.sendPackets(new S_ServerMessage(674));
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.save();
			} else {
				pc.sendPackets("只能在混亂的狀態下使用。");
			}
			break;
			case 490029: // 混亂藥水
				if (pc.getLawful() <= -32768) {
					pc.sendPackets("當前條件不允許使用。");
					return;
				}
				if (pc.getLawful() >= -32768 && pc.getLawful() <= 32767) {
					pc.addLawful(-10000);
					pc.sendPackets(new S_ServerMessage(674));
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.save();
				} else {
					pc.sendPackets("只能在正義狀態下使用。");
				}
			break;
			case 3000175: // 探測者
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, pc.getNetConnection()), true); // 探測點
				pc.getNetConnection().getAccount().tam_point += 100000; // 增加探測點數量
				pc.getNetConnection().getAccount().updateTam(); // 更新探測點
				pc.sendPackets(new S_SystemMessage("?獲得了10萬探測點。"));
				pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100056:
			if (pc.getInventory().getSize() > 160) {
				pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
				pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 5000;
			pc.getNetConnection().getAccount().updateNcoin();
			pc.sendPackets(new S_SystemMessage("帳戶已充?5000 N幣。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100057:
			if (pc.getInventory().getSize() > 160) {
				pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
				pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 10000;
			pc.getNetConnection().getAccount().updateNcoin();
			pc.sendPackets(new S_SystemMessage("帳戶已充?10000 N幣。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100058:
			if (pc.getInventory().getSize() > 160) {
				pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
				pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 30000;
			pc.getNetConnection().getAccount().updateNcoin();
			pc.sendPackets(new S_SystemMessage("帳戶已充?30000 N幣。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 3000177: // N幣 100元
				if (pc.getInventory().getSize() > 160) {
					pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
					return;
				}
				if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
					pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
					return;
				}
				pc.getNetConnection().getAccount().Ncoin_point += 100; // 增加探測點數量
				pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
				pc.sendPackets(new S_SystemMessage("已充?100 N幣。"));
				pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 3000178: // N幣 300元
				if (pc.getInventory().getSize() > 160) {
					pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
					return;
				}
				if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
					pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
					return;
				}
				pc.getNetConnection().getAccount().Ncoin_point += 300; // 增加探測點數量
				pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
				pc.sendPackets(new S_SystemMessage("已充?300 N幣。"));
				pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 3000179: // N幣 500元
				if (pc.getInventory().getSize() > 160) {
					pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
					return;
				}
				if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
					pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
					return;
				}
				pc.getNetConnection().getAccount().Ncoin_point += 500; // 增加探測點數量
				pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
				pc.sendPackets(new S_SystemMessage("已充?500 N幣。"));
				pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 3000187: // N幣 1000元
				if (pc.getInventory().getSize() > 160) {
					pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
					return;
				}
				if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
					pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
					return;
				}
				pc.getNetConnection().getAccount().Ncoin_point += 1000; // 增加探測點數量
				pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
				pc.sendPackets(new S_SystemMessage("已充?1000 N幣。"));
				pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 3000188: // N幣 100000元
				if (pc.getInventory().getSize() > 160) {
					pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
					return;
				}
				if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
					pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
					return;
				}
				pc.getNetConnection().getAccount().Ncoin_point += 100000; // 增加探測點數量
				pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
				pc.sendPackets(new S_SystemMessage("已充?100000 N幣。\aH(請檢?應用中心 CTRL+Z)"));
				pc.getInventory().removeItem(l1iteminstance, 1);
			break;
			case 4100299: // 現金
				if (pc.getInventory().checkItem(4100299, 99)) { // 檢?是否有99個現金
					pc.getInventory().consumeItem(4100299, 99); // 消耗99個現金

					if (pc.getInventory().getSize() > 120) {
						pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
						return;
					}
					if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
						pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
						return;
					}
					pc.getNetConnection().getAccount().Ncoin_point += 100;
					pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
					pc.sendPackets(new S_SystemMessage("N幣已使用?充?到N幣商店(F8快捷鍵上方 'C')"));
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else {
					pc.sendPackets(new S_SystemMessage("現金(100)個不足。"));
				return;
			}
			break;
		case 3000249:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
				pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 1000;
			pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
			pc.sendPackets(new S_SystemMessage("帳戶已累積 1000 點里程數。使用命令(.里程數)?詢。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100440:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
				pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 60000;
			pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
			pc.sendPackets(new S_SystemMessage("已累積60,000 N幣。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100441:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
				pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 90000;
			pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
			pc.sendPackets(new S_SystemMessage("已累積90,000 N幣。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100442:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("擁有的物品太多了。"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 修改此部分會導致錯誤。
				pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 100000;
			pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
			pc.sendPackets(new S_SystemMessage("已累積100,000 N幣。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100591: {
			int random = CommonUtil.random(100);
			int count = 0;

			if (random <= 30) { // -- 5萬到1000萬。
				count = CommonUtil.random(10, 20);
				pc.getNetConnection().getAccount().Ncoin_point += count;
			} else {// -- 5萬到500萬.
				count = CommonUtil.random(15, 30);
				pc.getNetConnection().getAccount().Ncoin_point += count;
			}
			if (random <= 5) { // -- 5% 機率
				count = CommonUtil.random(20, 40);
				pc.getNetConnection().getAccount().Ncoin_point += count;
			}
			if (random <= 3) { // -- 3% 機率
				count = CommonUtil.random(30, 60);
				pc.getNetConnection().getAccount().Ncoin_point += count;
			}
			if (random <= 1) { // -- 1% 機率
				count = CommonUtil.random(100, 200);
				pc.getNetConnection().getAccount().Ncoin_point += count;
			}
			pc.getNetConnection().getAccount().updateNcoin(); // 更新N幣
			pc.sendPackets(new S_SystemMessage("已充?N幣 (" + count + ") 點。"));
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
			case 3000173: // 隨機?化武器箱 (2~5)
				if (pc.getInventory().checkItem(3000173, 1)) { // 檢?物品和數量
					pc.getInventory().consumeItem(3000173, 1); // 消耗物品和數量
					Random random = new Random();
					L1ItemInstance item = null;
					int[] itemrnd = { 66, 12, 134, 86, 61, 202011, 202012, 202013, 202014 }; // 隨機物品編號
					int[] enchantrnd = {
							2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
							3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
							4, 4,
							5 }; // 隨機?化等級
					int ran1 = random.nextInt(itemrnd.length);
					int ran2 = random.nextInt(enchantrnd.length);
					item = pc.getInventory().storeItem(itemrnd[ran1], 1);
					item.setEnchantLevel(enchantrnd[ran2]);
					pc.sendPackets(new S_SkillSound(pc.getId(), 13390)); // 發送技能音效
			}
			break;
			case 3000389: // 武器隨機箱(從納巴爾到忘島級)
				if (pc.getInventory().checkItem(3000389, 1)) { // 檢?物品和數量
					pc.getInventory().consumeItem(3000389, 1); // 消耗物品和數量
					Random random = new Random();
					L1ItemInstance item = null;
					int[] itemrnd = { 54, 58, 76, 203006, 59, 202003, 203018, 203017, 1120,
							54, 58, 76, 203006, 59, 202003, 203018, 203017, 1120,
							54, 58, 76, 203006, 59, 202003, 203018, 203017, 1120,
							54, 58, 76, 203006, 59, 202003, 203018, 203017, 1120,
							12, 61, 86, 134, 202011, 202012, 202013, 202014 }; // 隨機物品
					int[] enchantrnd = { 0 }; // 隨機?化等級
					int ran1 = random.nextInt(itemrnd.length);
					int ran2 = random.nextInt(enchantrnd.length);
					item = pc.getInventory().storeItem(itemrnd[ran1], 1);
					item.setEnchantLevel(enchantrnd[ran2]);
					pc.sendPackets(new S_SystemMessage("已獲得物品。"));
			break;
					case 3000378: // 武器隨機箱(無陽級)
						if (pc.getInventory().checkItem(3000378, 1)) { // 檢?物品和數量
							pc.getInventory().consumeItem(3000378, 1); // 消耗物品和數量
							Random random = new Random();
							L1ItemInstance item = null;
							int[] itemrnd = { 203005, 62, 127, 189, 164, 84, 1123, 1119, 1135 }; // 隨機物品
							int[] enchantrnd = { 0 }; // 隨機?化等級
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
							// item.setIdentified(false);
							pc.sendPackets(new S_SystemMessage("已獲得物品。"));
						}
			break;
					case 3000379: // 武器隨機箱(納巴爾級)
						if (pc.getInventory().checkItem(3000379, 1)) { // 檢?物品和數量
							pc.getInventory().consumeItem(3000379, 1); // 消耗物品和數量
							Random random = new Random();
							L1ItemInstance item = null;
							int[] itemrnd = { 203006, 59, 202003, 1136, 203018, 203017, 1120 }; // 隨機物品
							int[] enchantrnd = { 0 }; // 隨機?化等級
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
							// item.setIdentified(false);
							pc.sendPackets(new S_SystemMessage("已獲得物品。"));
						}
			break;
					case 3000381: // 憤怒箱
						if (pc.getInventory().checkItem(3000381, 1)) { // 檢?物品和數量
							pc.getInventory().consumeItem(3000381, 1); // 消耗物品和數量
							Random random = new Random();
							L1ItemInstance item = null;
							int[] itemrnd = { 202011 }; // 隨機物品
							int[] enchantrnd = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
									2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
									3 }; // 隨機?化等級
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
							// item.setIdentified(false);
							pc.sendPackets(new S_SystemMessage("獲得: 蓋亞的憤怒"));
						}
			break;
					case 3000382: // ?望箱
						if (pc.getInventory().checkItem(3000382, 1)) { // 檢?物品和數量
							pc.getInventory().consumeItem(3000382, 1); // 消耗物品和數量
							Random random = new Random();
							L1ItemInstance item = null;
							int[] itemrnd = { 202012 }; // 隨機物品
							int[] enchantrnd = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
									2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
									3 }; // 隨機?化等級
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
// 							item.setIdentified(false);
							pc.sendPackets(new S_SystemMessage("獲得: 海培里恩的?望"));
						}
			break;
					case 3000383: // 恐懼箱
						if (pc.getInventory().checkItem(3000383, 1)) { // 檢?物品和數量
							pc.getInventory().consumeItem(3000383, 1); // 消耗物品和數量
							Random random = new Random();
							L1ItemInstance item = null;
							int[] itemrnd = { 202013 }; // 隨機物品
							int[] enchantrnd = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
									2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
									3 }; // 隨機?化等級
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
// 							item.setIdentified(false);
							pc.sendPackets(new S_SystemMessage("獲得: 克羅諾斯的恐懼"));
						}
			break;
					case 3000384: // 憤怒箱
						if (pc.getInventory().checkItem(3000384, 1)) { // 檢?物品和數量
							pc.getInventory().consumeItem(3000384, 1); // 消耗物品和數量
							Random random = new Random();
							L1ItemInstance item = null;
							int[] itemrnd = { 202014 }; // 隨機物品
							int[] enchantrnd = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
									2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
									3 }; // 隨機?化等級
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
// 							item.setIdentified(false);
							pc.sendPackets(new S_SystemMessage("獲得: 泰坦的憤怒"));
						}
			break;
					case 3000385: // 執行箱
						if (pc.getInventory().checkItem(3000385, 1)) { // 檢?物品和數量
							pc.getInventory().consumeItem(3000385, 1); // 消耗物品和數量
							Random random = new Random();
							L1ItemInstance item = null;
							int[] itemrnd = {61}; // 隨機物品
							int[] enchantrnd = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
									2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
									3}; // 隨機?化等級
							int ran1 = random.nextInt(itemrnd.length);
							int ran2 = random.nextInt(enchantrnd.length);
							item = pc.getInventory().storeItem(itemrnd[ran1], 1);
							item.setEnchantLevel(enchantrnd[ran2]);
// 							item.setIdentified(false);
							pc.sendPackets(new S_SystemMessage("獲得: 真冥皇的執行劍"));
							break;
							case 3000386: // 紅影箱
								if (pc.getInventory().checkItem(3000386, 1)) { // 檢?物品和數量
									pc.getInventory().consumeItem(3000386, 1); // 消耗物品和數量
									Random random = new Random();
									L1ItemInstance item = null;
									int[] itemrnd = {86}; // 隨機物品
									int[] enchantrnd = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
											3}; // 隨機?化等級
									int ran1 = random.nextInt(itemrnd.length);
									int ran2 = random.nextInt(enchantrnd.length);
									item = pc.getInventory().storeItem(itemrnd[ran1], 1);
									item.setEnchantLevel(enchantrnd[ran2]);
// 									item.setIdentified(false);
									pc.sendPackets(new S_SystemMessage("獲得: 紅影的雙刀"));
								}
								break;
							case 3000387: // 水結之箱
								if (pc.getInventory().checkItem(3000387, 1)) { // 檢查物品和數量
									pc.getInventory().consumeItem(3000387, 1); // 刪除物品和數量
									Random random = new Random();
									L1ItemInstance item = null;
									int[] itemrnd = {134}; // 隨機物品
									int[] enchantrnd = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
											3}; // 隨機附魔數值
									int ran1 = random.nextInt(itemrnd.length);
									int ran2 = random.nextInt(enchantrnd.length);
									item = pc.getInventory().storeItem(itemrnd[ran1], 1);
									item.setEnchantLevel(enchantrnd[ran2]);
									// item.setIdentified(false);
								}
								pc.sendPackets(new S_SystemMessage("獲得: 水晶結晶權杖"));
								break;

							case 3000388: // 巴卡爾之箱
								if (pc.getInventory().checkItem(3000388, 1)) { // 檢查物品和數量
									pc.getInventory().consumeItem(3000388, 1); // 刪除物品和數量
									Random random = new Random();
									L1ItemInstance item = null;
									int[] itemrnd = {12}; // 隨機物品
									int[] enchantrnd = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
											2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
											3}; // 隨機附魔數值
									int ran1 = random.nextInt(itemrnd.length);
									int ran2 = random.nextInt(enchantrnd.length);
									item = pc.getInventory().storeItem(itemrnd[ran1], 1);
									item.setEnchantLevel(enchantrnd[ran2]);
									// item.setIdentified(false);
								}
								pc.sendPackets(new S_SystemMessage("獲得: 風之刃匕首"));
								break;

							case 3000368: // 庫茲隨機箱
								if (pc.getInventory().checkItem(3000368, 1)) { // 檢查物品和數量
									pc.getInventory().consumeItem(3000368, 1); // 刪除物品和數量
									Random random = new Random();
									L1ItemInstance item = null;
									int[] itemrnd = {54}; // 隨機物品
									// 編號
									int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
											8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
											9}; // 隨機附魔數值
									int ran1 = random.nextInt(itemrnd.length);
									int ran2 = random.nextInt(enchantrnd.length);
									item = pc.getInventory().storeItem(itemrnd[ran1], 1);
									item.setEnchantLevel(enchantrnd[ran2]);
									// item.setIdentified(false);
								}
								pc.sendPackets(new S_SystemMessage("獲得: 庫茲之劍"));
								break;
							case 3000369: // 地獄隨機箱
								if (pc.getInventory().checkItem(3000369, 1)) { // 檢查物品和數量
									pc.getInventory().consumeItem(3000369, 1); // 消耗物品和數量
									Random random = new Random();
									L1ItemInstance item = null;
									int[] itemrnd = {58}; // 隨機物品
									int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
									int ran1 = random.nextInt(itemrnd.length);
									int ran2 = random.nextInt(enchantrnd.length);
									item = pc.getInventory().storeItem(itemrnd[ran1], 1);
									item.setEnchantLevel(enchantrnd[ran2]);
// 					item.setIdentified(false);
									pc.sendPackets(new S_SystemMessage("獲得: 死亡騎士的火劍"));
								}
								break;
							case 3000370: // 羅德隨機箱
								if (pc.getInventory().checkItem(3000370, 1)) { // 檢查物品和數量
									pc.getInventory().consumeItem(3000370, 1); // 消耗物品和數量
									Random random = new Random();
									L1ItemInstance item = null;
									int[] itemrnd = {76}; // 隨機物品
									int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
									int ran1 = random.nextInt(itemrnd.length);
									int ran2 = random.nextInt(enchantrnd.length);
									item = pc.getInventory().storeItem(itemrnd[ran1], 1);
									item.setEnchantLevel(enchantrnd[ran2]);
// 					item.setIdentified(false);
									pc.sendPackets(new S_SystemMessage("獲得: 羅德的雙刀"));
									break;
									case 3000371: // 太刀隨機箱
										if (pc.getInventory().checkItem(3000371, 1)) { // 檢查物品和數量
											pc.getInventory().consumeItem(3000371, 1); // 消耗物品和數量
											Random random = new Random();
											L1ItemInstance item = null;
											int[] itemrnd = {203006}; // 隨機物品
											int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
											int ran1 = random.nextInt(itemrnd.length);
											int ran2 = random.nextInt(enchantrnd.length);
											item = pc.getInventory().storeItem(itemrnd[ran1], 1);
											item.setEnchantLevel(enchantrnd[ran2]);
// 					item.setIdentified(false);
											pc.sendPackets(new S_SystemMessage("獲得: 颱風的斧頭"));
											break;
											case 3000372: // 奈爾隨機箱
												if (pc.getInventory().checkItem(3000372, 1)) { // 檢查物品和數量
													pc.getInventory().consumeItem(3000372, 1); // 消耗物品和數量
													Random random = new Random();
													L1ItemInstance item = null;
													int[] itemrnd = {59}; // 隨機物品
													int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
													int ran1 = random.nextInt(itemrnd.length);
													int ran2 = random.nextInt(enchantrnd.length);
													item = pc.getInventory().storeItem(itemrnd[ran1], 1);
													item.setEnchantLevel(enchantrnd[ran2]);
// 					item.setIdentified(false);
													pc.sendPackets(new S_SystemMessage("獲得: 奈特巴爾德的雙手劍"));
													break;
													case 3000373: // 制止隨機箱
														if (pc.getInventory().checkItem(3000373, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000373, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {202003}; // 隨機物品
															int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
														}
														pc.sendPackets(new S_SystemMessage("獲得: 傑羅斯的魔杖"));
														break;
													case 3000374: // 惡夢隨機箱
														if (pc.getInventory().checkItem(3000374, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000374, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {1136}; // 隨機物品
															int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
														}
														pc.sendPackets(new S_SystemMessage("獲得: 惡夢的長弓"));
														break;
													case 3000375: // 咆哮隨機箱
														if (pc.getInventory().checkItem(3000375, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000375, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {203018}; // 隨機物品
															int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
														}
														pc.sendPackets(new S_SystemMessage("獲得: 咆哮的雙刀"));
														break;
													case 3000376: // 殲滅隨機箱
														if (pc.getInventory().checkItem(3000376, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000376, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {203017}; // 隨機物品
															int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
														}
														pc.sendPackets(new S_SystemMessage("獲得: 殲滅者的鏈鋸劍"));
														break;
													case 3000377: // 冷酷隨機箱
														if (pc.getInventory().checkItem(3000377, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000377, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {1120}; // 隨機物品
															int[] enchantrnd = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
														}
														pc.sendPackets(new S_SystemMessage("獲得: 冷酷的奇岩鏈"));
														break;
													case 3000038: // 古代物品:頭盔
														if (pc.getInventory().checkItem(3000038, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000038, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011,
																	20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011,
																	20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20017}; // 隨機物品
															int[] enchantrnd = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
																	1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
// 															item.setIdentified(false);
														}
														break;
													case 4100120: { // 燃燒的永恆祝福
														int[] itemid = {40308};
														int random500 = CommonUtil.random(100);

														if (random500 <= 30) { // -- 5萬到1000萬。
															createNewItem(pc, itemid[0], CommonUtil.random(50000, 10000000));
														} else { // -- 5萬到500萬。
															createNewItem(pc, itemid[0], CommonUtil.random(50000, 5000000));
														}
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;
					/*case 4100119: { // 燃燒的永恆祝福
						int[] itemid = { 40308 };
						int random500 = CommonUtil.random(100);

						if (random500 <= 30) { // -- 80萬到150萬
							createNewItem(pc, itemid[0], CommonUtil.random(800000, 1500000));
						} else { // -- 120萬到200萬
							createNewItem(pc, itemid[0], CommonUtil.random(1200000, 2000000));
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					break;*/
													case 3000039: // 古代物品:斗篷
														if (pc.getInventory().checkItem(3000039, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000039, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056,
																	20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056,
																	20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20074, 20079}; // 隨機物品
															int[] enchantrnd = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
																	1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
// 															item.setIdentified(false);
														}
														pc.sendPackets(new S_SystemMessage("獲得了物品。"));
														break;
													case 3000196: { // 宣傳箱
														// -- 隨機值的次數
														// -- 隨機值
														int[] itemid = {40308};
														int random500 = CommonUtil.random(100);

														int[] itemid1 = {3000197};
														int random501 = CommonUtil.random(100);

														int[] itemid2 = {3000198};
														int random502 = CommonUtil.random(100);

														int[] itemid3 = {3000199};
														int random503 = CommonUtil.random(100);

														if (random500 <= 30) { // -- 1萬到20萬
															createNewItem(pc, itemid[0], CommonUtil.random(10000, 200000));
														} else { // -- 5萬到30萬
															createNewItem(pc, itemid[0], CommonUtil.random(50000, 300000));
														}
														if (random501 <= 5) { // -- 0.3% 機率
															createNewItem(pc, itemid1[0], 1);
														}
														if (random502 <= 2) { // -- 0.5% 機率
															createNewItem(pc, itemid2[0], 1);
														}
														if (random503 <= 2) { // -- 0.8% 機率
															createNewItem(pc, itemid3[0], 1);
														}
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;
													case 3000174: // 全體 9防具 隨機箱
														if (pc.getInventory().checkItem(3000174, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000174, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {22365, 222324, 22360, 20085, 20085, 20085, 20085, 20084, 20084, 20084, 20084, 20084,
																	20084, 20084, 22196, 22197, 22198, 22199, 22200, 22201, 22202, 22203, 22204, 22205, 22206,
																	22207, 20049, 20050, 20076, 20079, 20178, 22261, 222327, 20235, 22214, 22263, 20200, 20216,
																	22213, 222307, 222308, 222309, 22257, 22258, 22259}; // 隨機物品
															int[] enchantrnd = {8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
																	8, 8, 8, 8, 8, 8, 8, 8, 8, 9}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
// 															item.setIdentified(false);
														}
														pc.sendPackets(new S_SystemMessage("獲得了物品。"));
														break;
													case 3000169: // 隨機武器強化箱
														if (pc.getInventory().checkItem(3000169, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(3000169, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {12, 134, 86, 61, 202011, 202012, 202013, 202014}; // 隨機物品編號
															int[] enchantrnd = {
																	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
																	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
																	2, 2, 2, 2, 2,
																	3
															}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
														}
														break;
		/*	case 4100004: // 隨機武器強化箱
				if (pc.getInventory().checkItem(4100004, 1)) { // 檢查物品和數量
					pc.getInventory().consumeItem(4100004, 1); // 消耗物品和數量
					Random random = new Random();
					L1ItemInstance item = null;
					int[] itemrnd = { 12, 134, 86, 61, 202011, 202012, 202013, 202014 }; // 隨機物品編號
					int[] enchantrnd = {
							2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
							2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
							2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
							3, 3, 3, 3, 3, 3, 3, 3, 3,
							4, 4, 4, 4
					}; // 隨機強化等級
					int ran1 = random.nextInt(itemrnd.length);
					int ran2 = random.nextInt(enchantrnd.length);
					item = pc.getInventory().storeItem(itemrnd[ran1], 1);
					item.setEnchantLevel(enchantrnd[ran2]);
					pc.sendPackets(new S_SkillSound(pc.getId(), 13390));
				}
				break;*/
													case 718: // 隨機武器強化箱
														if (pc.getInventory().checkItem(718, 1)) { // 檢查物品和數量
															pc.getInventory().consumeItem(718, 1); // 消耗物品和數量
															Random random = new Random();
															L1ItemInstance item = null;
															int[] itemrnd = {315, 316, 317, 318, 319, 320, 1104, 7000136, 7000213}; // 隨機物品編號
															int[] enchantrnd = {
																	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
																	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
																	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
																	1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
																	2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
																	3, 3, 3, 3
															}; // 隨機強化等級
															int ran1 = random.nextInt(itemrnd.length);
															int ran2 = random.nextInt(enchantrnd.length);
															item = pc.getInventory().storeItem(itemrnd[ran1], 1);
															item.setEnchantLevel(enchantrnd[ran2]);
															pc.sendPackets(new S_SkillSound(pc.getId(), 13390));
// 					item.setIdentified(false);
														}
// 					pc.sendPackets(new S_SystemMessage("系統: 獲得了物品。"));
														break;
													case 698: {
														L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
														L1ItemInstance weapon_item = pc.getInventory().getItem(l);
														if (enchant_item == null || weapon_item == null) {
															return;
														}
														if (l1iteminstance1.getBless() >= 128) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														int id = weapon_item.getItemId();
														if (!(id == 40346 || id == 40370 || id == 40362 || id == 40354)) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														int enchant = weapon_item.getEnchantLevel();
														int[] random_item = {40346, 40370, 40362, 40354};
														int random = CommonUtil.random(random_item.length);
														L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
														pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 獲得了。"));
														pc.getInventory().removeItem(enchant_item, 1);
														pc.getInventory().removeItem(weapon_item);
													}
													break;
													case 776: {
														L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
														L1ItemInstance weapon_item = pc.getInventory().getItem(l);
														if (enchant_item == null || weapon_item == null) {
															return;
														}
														if (l1iteminstance1.getBless() >= 128) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														int id = weapon_item.getItemId();
														if (!(id == 4100359 || id == 4100360 || id == 4100361 || id == 4100362 || id == 4100363 || id == 4100364)) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														int enchant = weapon_item.getEnchantLevel();
														int[] random_item = {4100359, 4100360, 4100361, 4100362, 4100363, 4100364};
														int random = CommonUtil.random(random_item.length);
														L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
														pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 獲得了物品。"));
														pc.getInventory().removeItem(enchant_item, 1);
														pc.getInventory().removeItem(weapon_item);
													}
													break;
													case 777: {
														L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
														L1ItemInstance weapon_item = pc.getInventory().getItem(l);
														if (enchant_item == null || weapon_item == null) {
															return;
														}
														if (l1iteminstance1.getBless() >= 128) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														int id = weapon_item.getItemId();
														if (!(id == 4100365 || id == 4100366 || id == 4100367 || id == 4100368 || id == 4100369 || id == 4100370 || id == 4100371)) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														int enchant = weapon_item.getEnchantLevel();
														int[] random_item = {4100367, 4100367, 4100367, 4100367, 4100367,
																4100369, 4100369, 4100369, 4100369,
																4100371, 4100371, 4100371,
																4100370, 4100370,
																4100368, 4100368,
																4100365,
																4100366};
														int random = CommonUtil.random(random_item.length);
														L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
														if (random_item[random] == 4100365 || random_item[random] == 4100366) {
															pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "" + pc.getName() + "獲得了 變更秘笈 (5階) 並獲得了 \f3" + new_item.getLogName() + " \fH。"));
															pc.sendPackets(String.valueOf(new S_SystemMessage(new_item.getLogName() + " 已獲得。")));
														} else {
															pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 已獲得。"));
														}
														pc.getInventory().removeItem(enchant_item, 1);
														pc.getInventory().removeItem(weapon_item);
													}
													break;
													case 3000458: { // 克勞迪亞通行證
														if (pc.getLevel() >= 1 && pc.getLevel() >= 56) {
															pc.sendPackets(new S_SystemMessage("僅限55級以下使用。"));
															return;
														}
														pc.add_exp((long) ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) - pc.get_exp()
																+ ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) / 30000000L)));
														pc.sendPackets(String.valueOf(new S_SystemMessage("您已使用克勞迪亞通行證。")));
													}
													pc.getInventory().removeItem(l1iteminstance, 1);
													break;
													case 41316: // 神聖的秘銀粉末

														if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {

															pc.sendPackets(new S_ServerMessage(79)); // 1 什麼都沒有發生。
															return;
														}
														if (pc.hasSkillEffect(STATUS_HOLY_WATER)) {
															pc.removeSkillEffect(STATUS_HOLY_WATER);
														}
														pc.setSkillEffect(STATUS_HOLY_MITHRIL_POWDER, 900 * 1000);
														pc.sendPackets(new S_SkillSound(pc.getId(), 190));
														pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
														pc.sendPackets(new S_ServerMessage(1142));
														pc.getInventory().removeItem(l1iteminstance, 1);
														break;
													case 41354: // 神聖的艾娃之水
														if (pc.hasSkillEffect(STATUS_HOLY_WATER) || pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
															pc.sendPackets(new S_ServerMessage(79)); // 1 什麼都沒有發生。
															return;
														}
														pc.setSkillEffect(STATUS_HOLY_WATER_OF_EVA, 900 * 1000);
														pc.sendPackets(new S_SkillSound(pc.getId(), 190));
														pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
														pc.sendPackets(new S_ServerMessage(1140)); // 神聖效果發動
														pc.getInventory().removeItem(l1iteminstance, 1);
														break;
													case 3000147:// 特貝對抗戰
														loc_x += 32769;
														loc_y += 32832;
														if (itemId == 3000147) {
															pc.start_teleport(loc_x, loc_y, 782, pc.getHeading(), 18339, true, false);
															pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aA[通知]: \\aG[注意] 有一個強大力量的怪物在這裡。"));
														}
														pc.getInventory().removeItem(l1iteminstance, 1);
														cancelAbsoluteBarrier(pc); // 絕對屏障解除
														break;
													case 202099: {// 克勞迪亞村莊
														// if (pc.get_DuelLine() != 0) {
														// pc.sendPackets(new S_SystemMessage("無法在戰鬥區域使用。"));
														// return;
														// }
														// if (pc.getMap().isEscapable() || pc.isGm()) {
														// int[]
														loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_claudia);
														// L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2],
														// pc.getHeading(), true);
														pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
														// }
													}
													break;
													case 3000229: {// 被遺忘的歸還卷軸
														if (pc.hasSkillEffect(L1SkillId.DESPERADO) || pc.hasSkillEffect(L1SkillId.ETERNITI) || pc.hasSkillEffect(L1SkillId.TEMPEST) || pc.hasSkillEffect(L1SkillId.PHANTOM))
															return;
														if (!(pc.getMapId() >= 1708 && pc.getMapId() <= 1709)) {
															pc.sendPackets(new S_SystemMessage("只能在被遺忘的島/等待室使用。"));
															return;
														}
														int[] x = {32775, 32787, 32788, 32773};
														int[] y = {32757, 32755, 32765, 32769};
														int type = _random.nextInt(x.length);
														L1Location base = new L1Location(x[type], y[type], 1710);
														L1Location random = L1Location.randomLocation(base, 0, 3, true);
														pc.start_teleport(random.getX(), random.getY(), random.getMapId(), pc.getHeading(), 18339, true, false);
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;
													case 3000413: {// 強制歸還卷軸 (忘島->奇岩)
														if (!(pc.getMapId() >= 1710)) {
															pc.sendPackets(new S_SystemMessage("只能在被遺忘的島等待室使用。"));
															return;
														}
														int[] x = {33443, 33444, 33437, 33428};
														int[] y = {32804, 32810, 32812, 32808};
														int type = _random.nextInt(x.length);
														L1Location base = new L1Location(x[type], y[type], 4);
														L1Location random = L1Location.randomLocation(base, 0, 3, true);
														pc.start_teleport(random.getX(), random.getY(), random.getMapId(), pc.getHeading(), 18339, true, false);
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;
													case 41260:// 장착
														for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
															if (object instanceof L1EffectInstance) {
																if (((L1NpcInstance) object).getNpcTemplate().get_npcId() == 81170) {
																	pc.sendPackets(new S_ServerMessage(1162));
																	return;
																}
															}
														}
														int[] loc1 = new int[2];
														loc1 = pc.getFrontLoc();
														L1EffectSpawn.getInstance().spawnEffect(81170, 300000, loc1[0], loc1[1], pc.getMapId());
														pc.getInventory().removeItem(l1iteminstance, 1);
														break;
													case 240100:// 被詛咒的傳送卷軸 (原始物品)
														pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, true, false);
														pc.getInventory().removeItem(l1iteminstance, 1);
														pc.cancelAbsoluteBarrier(); // 絕對屏障解除
														break;
													case 748: // 잡화상점 이동부적
														pc.start_teleport(33453, 32820, 4, pc.getHeading(), 18339, true, false);
														break;
													case 3000397: {// 雅希軍事增益卷軸
														if (pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														pc.setSkillEffect(STATUS_CURSE_BARLOG, 1020 * 1000); // 1020秒
														pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 1, 1020));
														pc.sendPackets(new S_SkillSound(pc.getId(), 750));
														pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
														pc.sendPackets(new S_ServerMessage(1127));
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;
													case 3000398: {// 巴洛克軍事增益卷軸
														if (pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
															pc.sendPackets(new S_ServerMessage(79));
															return;
														}
														pc.setSkillEffect(STATUS_CURSE_YAHEE, 1020 * 1000);
														pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 2, 1020));
														pc.sendPackets(new S_SkillSound(pc.getId(), 750));
														pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
														pc.sendPackets(new S_ServerMessage(1127));
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;
													case 749: // 裝備商店傳送符
														pc.start_teleport(33435, 32754, 4, pc.getHeading(), 18339, true, false);
														break;

													case 3000128: {// 美狄斯的祝福卷軸
														int[] allBuffSkill = {22000};
														L1SkillUse l1skilluse = new L1SkillUse();
														if (pc.hasSkillEffect(L1SkillId.METIS_BLESSING_SCROLL))
															pc.removeSkillEffect(L1SkillId.METIS_BLESSING_SCROLL);
														for (int i = 0; i < allBuffSkill.length; i++) {
															l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
														}
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;
													case 60208: { // 力量的櫻桃冰沙
														pc.get_skill().start_str_ice();
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;

													case 60209: { // 敏捷的綠茶冰沙
														pc.get_skill().start_dex_ice();
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;

													case 60210: { // 智慧的紅豆冰沙
														pc.get_skill().start_int_ice();
														pc.getInventory().removeItem(l1iteminstance, 1);
													}
													break;

													case 700021: {// 解封卷軸申請書
														if (pc.getInventory().checkItem(50021)) { //
															pc.sendPackets(new S_ChatPacket(pc, "您已經擁有解封卷軸。"));
															return;
														}
														if (pc.getInventory().checkItem(700021, 1)) {
															pc.getInventory().consumeItem(700021, 1);
															pc.getInventory().storeItem(50021, 15);
														}
														break;
														case 700000:// 경험치 물약
															if (pc.getLevel() >= 1 && pc.getLevel() <= 48) {
																pc.set_exp(pc.get_exp() + 326144);
															} else if (pc.getLevel() >= 49 && pc.getLevel() <= 64) {
																pc.set_exp(pc.get_exp() + 2609152);
															} else if (pc.getLevel() >= 65 && pc.getLevel() <= 69) {
																pc.set_exp(pc.get_exp() + 1304576);
															} else if (pc.getLevel() >= 70 && pc.getLevel() <= 74) {
																pc.set_exp(pc.get_exp() + 652288);
															} else if (pc.getLevel() >= 75 && pc.getLevel() <= 78) {
																pc.set_exp(pc.get_exp() + 326144);
															} else if (pc.getLevel() == 79) {
																pc.set_exp(pc.get_exp() + 163072);
															} else if (pc.getLevel() >= 80 && pc.getLevel() <= 81) {
																pc.set_exp(pc.get_exp() + 81536);
															} else if (pc.getLevel() >= 82 && pc.getLevel() <= 83) {
																pc.set_exp(pc.get_exp() + 40768);
															} else if (pc.getLevel() >= 84 && pc.getLevel() <= 85) {
																pc.set_exp(pc.get_exp() + 20384);
															} else if (pc.getLevel() == 86) {
																pc.set_exp(pc.get_exp() + 10192);
															} else if (pc.getLevel() == 87) {
																pc.set_exp(pc.get_exp() + 5096);
															} else if (pc.getLevel() == 88) {
																pc.set_exp(pc.get_exp() + 2048);
															} else if (pc.getLevel() == 89) {
																pc.set_exp(pc.get_exp() + 1024);
															} else if (pc.getLevel() >= 90) {
																pc.set_exp(pc.get_exp() + 512);
															}
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 4100074:
															long EXP_TIME = System.currentTimeMillis() / 1000;
															if (pc.getQuizTime() + 2 > EXP_TIME) {
																return;
															}
															if (pc.getLevel() > Config.ServerAdSetting.ExpPosis) {
																pc.sendPackets(new S_ChatPacket(pc, Config.ServerAdSetting.ExpPosis + "等級以下才能使用。"));
																return;
															}
															pc.setQuizTime(EXP_TIME);
															pc.set_exp(pc.get_exp() + 360650L);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 4100075:
															long EXP_TIME2 = System.currentTimeMillis() / 1000;
															if (pc.getQuizTime() + 2 > EXP_TIME2) {
																return;
															}
															if (pc.getLevel() < Config.ServerAdSetting.ExpPosis1) {
																pc.sendPackets(new S_ChatPacket(pc, Config.ServerAdSetting.ExpPosis1 + "等級以上才能使用。"));
																return;
															}
															pc.setQuizTime(EXP_TIME2);
															pc.set_exp(pc.get_exp() + 360650L);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 4100585:
															long curtimeN = System.currentTimeMillis() / 1000;
															if (pc.getQuizTime() + 1 > curtimeN) {
																long time = (pc.getQuizTime() + 1) - curtimeN;
																pc.sendPackets(new S_ChatPacket(pc, time + " 秒後請使用。"));
																return;
															}
															if (pc.getLevel() < 80) {
																pc.sendPackets(new S_ChatPacket(pc, "80級以上才能使用。"));
																return;
															}
															if (pc.getLevel() >= 80 && pc.getLevel() <= 82) {
																PureExp(pc, 1);
															} else if (pc.getLevel() >= 83 && pc.getLevel() <= 84) {
																PureExp(pc, 2);
															} else if (pc.getLevel() >= 85 && pc.getLevel() <= 86) {
																PureExp(pc, 3);
															} else if (pc.getLevel() >= 87 && pc.getLevel() <= 88) {
																PureExp(pc, 4);
															} else if (pc.getLevel() >= 89) {
																PureExp(pc, 5);
															}
															pc.setQuizTime(curtimeN);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 40033: // 力量藥水
															if (pc.getLevel() <= 49) {
																pc.sendPackets(4500);
																return;
															}
															if (pc.getElixirStats() < 30) {//10
																if (pc.getLevel() >= 100) {
																	if (pc.getAbility().getStr() < 60) {
																		pc.getAbility().addStr((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資訊寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																	// \f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																} else if (pc.getLevel() >= 90 && pc.getLevel() <= 99) {
																	if (pc.getAbility().getStr() < 55) {
																		pc.getAbility().addStr((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資訊寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
// 																			\f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																} else {
																	if (pc.getAbility().getStr() < 50) {
																		pc.getAbility().addStr((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資料寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
// 																		\f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																}
															} else {
																pc.sendPackets(4473);
// 																\f1 一個能力值的最大值是25。請選擇其他能力值。
															}
															break;
														case 40034: // 生命藥水
															if (pc.getLevel() <= 49) {
																pc.sendPackets(4500);
																return;
															}
															if (pc.getElixirStats() < 30) {
																if (pc.getLevel() >= 100) {
																	if (pc.getAbility().getCon() < 60) {
																		pc.getAbility().addCon((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資料寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
// 																		\f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																} else if (pc.getLevel() >= 90 && pc.getLevel() <= 99) {
																	if (pc.getAbility().getCon() < 55) {
																		pc.getAbility().addCon((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資料寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
// 																				\f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																} else {
																	if (pc.getAbility().getCon() < 50) {
																		pc.getAbility().addCon((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資料寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
// 																		\f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																}
															} else {
																pc.sendPackets(4473);
// 																\f1 一個能力值的最大值是25。請選擇其他能力值。
															}
															break;
														case 40035:// 敏捷藥水
															if (pc.getLevel() <= 49) {
																pc.sendPackets(4500);
																return;
															}
															if (pc.getElixirStats() < 30) {
																if (pc.getLevel() >= 100) {
																	if (pc.getAbility().getDex() < 60) {
																		pc.getAbility().addDex((byte) 1);
																		pc.resetBaseAc();
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資料寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
// 																		\f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																} else if (pc.getLevel() >= 90 && pc.getLevel() <= 99) {
																	if (pc.getAbility().getDex() < 55) {
																		pc.getAbility().addDex((byte) 1);
																		pc.resetBaseAc();
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資料寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
// 																				\f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																} else {
																	if (pc.getAbility().getDex() < 50) {
																		pc.getAbility().addDex((byte) 1);
																		pc.resetBaseAc();
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save(); // 將角色資料寫入資料庫
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
//																		 \f1 一個能力值的最大值是25。請選擇其他能力值。
																	}
																}
															} else {
																pc.sendPackets(4473);
// 																\f1 一個能力值的最大值是25。請選擇其他能力值。
															}
															break;
														case 40036:// 智力藥水
															if (pc.getLevel() <= 49) {
																pc.sendPackets(4500);
																return;
															}
															if (pc.getElixirStats() < 30) {
																if (pc.getLevel() >= 100) {
																	if (pc.getAbility().getInt() < 60) {
																		pc.getAbility().addInt((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																} else if (pc.getLevel() >= 90 && pc.getLevel() <= 99) {
																	if (pc.getAbility().getInt() < 55) {
																		pc.getAbility().addInt((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																} else {
																	if (pc.getAbility().getInt() < 50) {
																		pc.getAbility().addInt((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																}
															} else {
																pc.sendPackets(4473);
																// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
															}
															break;
														case 40037:// 엘릭서위즈
															if (pc.getLevel() <= 49) {
																pc.sendPackets(4500);
																return;
															}
															if (pc.getElixirStats() < 30) {
																if (pc.getLevel() >= 100) {
																	if (pc.getAbility().getWis() < 60) {
																		pc.getAbility().addWis((byte) 1);
																		pc.resetBaseMr();
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																} else if (pc.getLevel() >= 90 && pc.getLevel() <= 99) {
																	if (pc.getAbility().getWis() < 55) {
																		pc.getAbility().addWis((byte) 1);
																		pc.resetBaseMr();
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																} else {
																	if (pc.getAbility().getWis() < 50) {
																		pc.getAbility().addWis((byte) 1);
																		pc.resetBaseMr();
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																}
															} else {
																pc.sendPackets(4473);
																// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
															}
															break;
														case 40038:// 엘릭서카리
															if (pc.getLevel() <= 49) {
																pc.sendPackets(4500);
																return;
															}
															if (pc.getElixirStats() < 30) {
																if (pc.getLevel() >= 100) {
																	if (pc.getAbility().getCha() < 60) {
																		pc.getAbility().addCha((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																} else if (pc.getLevel() >= 90 && pc.getLevel() <= 99) {
																	if (pc.getAbility().getCha() < 55) {
																		pc.getAbility().addCha((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																} else {
																	if (pc.getAbility().getCha() < 50) {
																		pc.getAbility().addCha((byte) 1);
																		pc.setElixirStats(pc.getElixirStats() + 1);
																		pc.getInventory().removeItem(l1iteminstance, 1);
																		pc.sendPackets(new S_OwnCharStatus2(pc));
																		pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
																		pc.save();
																	} else {
																		pc.sendPackets(new S_ServerMessage(481));
																		// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
																	}
																}
															} else {
																pc.sendPackets(4473); //더 이상 엘릭서를 복용할 수 없습니다.
															}
															break;
														case 3000126:// 恢復的徽章袋子
															if (pc.getInventory().getSize() > 120) {
																pc.sendPackets(new S_SystemMessage("持有的物品過多。"));
																return;
															}
															if (pc.getInventory().getWeight100() > 82) { // 修改此部分會產生錯誤
																pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
																return;
															}
															if (pc.getInventory().checkItem(3000126, 1)) { // 檢查物品和數量
																pc.getInventory().removeItem(l1iteminstance, 1);
																createNewItem2(pc, 900021, 1, 3); // 恢復的徽章 +3
															}
															break;
														case 3000127:// 成長的徽章袋子
															if (pc.getInventory().getSize() > 120) {
																pc.sendPackets(new S_SystemMessage("持有的物品過多。"));
																return;
															}
															if (pc.getInventory().getWeight100() > 82) { // 修改此部分會產生錯誤
																pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
																return;
															}
															if (pc.getInventory().checkItem(3000127, 1)) { // 檢查物品和數量
																pc.getInventory().removeItem(l1iteminstance, 1);
																createNewItem2(pc, 900020, 1, 3); // 成長的徽章 +3
															}
															break;
														case 3000123: { // Boss Summoning Scroll
															if (pc.is_combat_field() || StadiumManager.getInstance().is_on_stadium(pc.getMapId())) {
																pc.sendPackets(new S_SystemMessage("該地圖中無法使用。"));
																return;
															}

															int castle_id = L1CastleLocation.getCastleIdByArea(pc);
															if (castle_id != 0) {
																pc.sendPackets(new S_SystemMessage("攻城區域無法召喚。"));
																return;
															}
															if (itemId == 3000123) {
																if (pc.getZoneType() != 0) { // Safe zone
																	pc.sendPackets(new S_SystemMessage("只能在普通區域(野外)中使用。"));
																	return;
																}
																useMobEventSpownWand(pc, l1iteminstance);
																pc.sendPackets(new S_SystemMessage("某人被召喚了。"));
															} else {
																pc.sendPackets(new S_SystemMessage("只能在普通區域中使用。"));
															}
															pc.getInventory().removeItem(l1iteminstance, 1);
														}
														break;
														/** 패키지이동주문서 **/
														case 800100:
															pc.setCashStep(1);
															pc.start_teleport(32866, 32878, 631, 5, 18339, true, false);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 800101:
															pc.setCashStep(2);
															pc.start_teleport(32866, 32878, 631, 5, 18339, true, false);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 800102:
															pc.setCashStep(3);
															pc.start_teleport(32866, 32878, 631, 5, 18339, true, false);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 4100666: {
															int castle_id = 0;
															int house_id = 0;

															if (!(pc.getMapId() == 15482 || pc.getMapId() == 15492)) {
																pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
																pc.sendPackets(3274);
																return;
															}

															if (pc.getMapId() >= 1708 && pc.getMapId() <= 1712 || pc.getMapId() == 34) {
																pc.sendPackets(new S_ServerMessage(647));
																return;
															}

															if (pc.getClanid() != 0) { // 크란 소속
																L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
																if (clan != null) {
																	castle_id = clan.getCastleId();
																	house_id = clan.getHouseId();
																}
															}
															if (castle_id != 0) { // 성주 크란원
																loc = L1CastleLocation.getCastleLoc(castle_id);
																pc.getInventory().removeItem(l1iteminstance, 1);
																pc.send_effect(12261, true);
																SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, loc[0], loc[1], loc[2], SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
															} else if (house_id != 0) { // 아지트 소유 크란원
																loc = L1HouseLocation.getHouseLoc(house_id);
																pc.getInventory().removeItem(l1iteminstance, 1);
																pc.send_effect(12261, true);
																SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, loc[0], loc[1], loc[2], SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
															} else {
																if (pc.getHomeTownId() > 0) {
																	int newX = 33632 + 3;
																	int newY = 32791 + 3;
																	short mapId = 15482;

																	pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
																	pc.getInventory().removeItem(l1iteminstance, 1);
																	pc.send_effect(12261, true);
																	SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, mapId, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
																} else {
																	int newX = 33632 + 3;
																	int newY = 32791 + 3;
																	short mapId = 15482;

																	pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
																	pc.getInventory().removeItem(l1iteminstance, 1);
																	pc.send_effect(12261, true);
																	SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, mapId, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
																}
															}
															cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
														}
														break;
														case 40124:
														case 30086:// 혈맹 귀환 스크롤
															int castle_id = 0;
															int house_id = 0;

															if (pc.getMapId() >= 1708 && pc.getMapId() <= 1712 || pc.getMapId() == 34) {
																pc.sendPackets(new S_ServerMessage(647));
																return;
															}

															if (pc.getClanid() != 0) { // 크란 소속
																L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
																if (clan != null) {
																	castle_id = clan.getCastleId();
																	house_id = clan.getHouseId();
																}
															}
															if (castle_id != 0 && castle_id != 4) { // 성주 크란원
																loc = L1CastleLocation.getCastleLoc(castle_id);
																pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
																pc.getInventory().removeItem(l1iteminstance, 1);
															} else if (castle_id == 4) {
																int newX = 32732 + 3;
																int newY = 32804 + 3;
																short mapId = 15492;
																pc.send_effect(12261, true);
																SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, mapId, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
															} else if (house_id != 0) { // 아지트 소유 크란원
																loc = L1HouseLocation.getHouseLoc(house_id);
																pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
																pc.getInventory().removeItem(l1iteminstance, 1);
															} else {
																if (pc.getHomeTownId() > 0) {
																	loc = L1TownLocation.getGetBackLoc(pc.getHomeTownId());
																	pc.start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
																	pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
																	pc.getInventory().removeItem(l1iteminstance, 1);
																} else {
																	loc = Getback.GetBack_Location(pc, true);
																	pc.start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
																	pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
																	pc.getInventory().removeItem(l1iteminstance, 1);
																}
															}
															cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
															break;
														case MJINNHelper.INN_KEYID:
															if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
																return;
															}
															if (pc.getMapId() >= 1708 && pc.getMapId() <= 1709) {
																pc.sendPackets(new S_SystemMessage("該地圖中無法使用。"));
																pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
																return;
															}
															if ((pc.hasSkillEffect(L1SkillId.SHOCK_STUN)) || (pc.hasSkillEffect(L1SkillId.EMPIRE))
																	|| (pc.hasSkillEffect(L1SkillId.ICE_LANCE)) || (pc.hasSkillEffect(L1SkillId.BONE_BREAK))
																	|| (pc.hasSkillEffect(L1SkillId.EARTH_BIND)) || (pc.hasSkillEffect(L1SkillId.DESPERADO))
																	|| (pc.hasSkillEffect(L1SkillId.TEMPEST)) || (pc.hasSkillEffect(L1SkillId.PHANTOM))
																	|| (pc.hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) || (pc.hasSkillEffect(L1SkillId.ETERNITI))
																	|| (pc.hasSkillEffect(L1SkillId.FORCE_STUN)) || (pc.hasSkillEffect(L1SkillId.DISINTEGRATE))) {
																return;
															}
															MJINNRoom.input(l1iteminstance, pc);
															break;
														case 9995:
															L1SkillUse l1skilluse = new L1SkillUse();
															l1skilluse.handleCommands(pc, L1SkillId.NarutoThanksCandy, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 60256:
															if (pc.getInventory().checkItem(60255)) {
																pc.sendPackets(new S_ServerMessage(939));
																pc.sendPackets(new S_SystemMessage("持有龍之紫水晶。"));
																return;
															}
															pc.getInventory().storeItem(60255, 1);
															pc.getInventory().removeItem(l1iteminstance, 1);
															break;
														case 89136:
															if (pc.get_SpecialSize() == 40) {
																pc.sendPackets(new S_ServerMessage(1622));
																return;
															}
															if (pc.get_SpecialSize() == 0) {
																pc.set_SpecialSize(20);
																pc.getInventory().consumeItem(89136, 1);
																pc.sendPackets(new S_ServerMessage(1624, "20"));
															} else if (pc.get_SpecialSize() == 20) {
																pc.set_SpecialSize(40);
																pc.getInventory().consumeItem(89136, 1);
																pc.sendPackets(new S_ServerMessage(1624, "40"));
															}
															break;
														case 51093:
														case 51094:
														case 51095:
														case 51096:
														case 51097:
														case 51098:
														case 51099:
														case 51100:
														case 51102:
														case 51103:
															if (!pc.getMap().isSafetyZone(pc.getLocation())) {
																pc.sendPackets(new S_ChatPacket(pc, "只能在安全區域使用。"));
																return;
															}
															if (pc.getLevel() != pc.getHighLevel()) {
																pc.sendPackets(new S_SystemMessage("角色等級已降低。請升級後再使用。"));
																return;
															}
//			if (pc.getLevel() > 54) {
//				//pc.setStatReset(true);
//				pc.getInventory().consumeItem(200000, 1);
//				int locx2 = 32723 + CommonUtil.random(10);
//				int locy2 = 32851 + CommonUtil.random(10);
//				pc.start_teleport(locx2, locy2, 5166, 5, 169, true, false);
//				pc.resetStats();
//			} else {
//				pc.sendPackets(new S_SystemMessage("職業變更(重置)只能在55級以上使用。"));
//				return;
//			}
//			
															// 職業變更藥水
															if (pc.getClanid() != 0) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "請先退出血盟。")));
																return;
															} else if (itemId == 51093 && pc.getType() == 0) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是王族職業。")));
																return;
															} else if (itemId == 51094 && pc.getType() == 1) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是騎士職業。")));
																return;
															} else if (itemId == 51095 && pc.getType() == 2) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是妖精職業。")));
																return;
															} else if (itemId == 51096 && pc.getType() == 3) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是法師職業。")));
																return;
															} else if (itemId == 51097 && pc.getType() == 4) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是黑暗妖精職業。")));
																return;
															} else if (itemId == 51098 && pc.getType() == 5) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是龍騎士職業。")));
																return;
															} else if (itemId == 51099 && pc.getType() == 6) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是幻術師職業。")));
																return;
															} else if (itemId == 51100 && pc.getType() == 7) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是戰士職業。")));
																return;
															} else if (itemId == 51102 && pc.getType() == 8) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是劍士職業。")));
																return;
															} else if (itemId == 51103 && pc.getType() == 9) {
																pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已經是黃金槍騎職業。")));
																return;
															}

															pc.resetStats(); // 類別重置

															int[] Mclass = new int[]{0, 20553, 138, 20278, 2786, 6658, 6671, 20567, 18520, 19296};
															int[] Wclass = new int[]{1, 48, 37, 20279, 2796, 6661, 6650, 20577, 18499, 19299};

															if (itemId == 51093 && pc.getType() != 0 && pc.get_sex() == 0) { // 王族
																pc.setType(0);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(40228, 1);
																pc.getInventory().storeItem(40226, 1);
															} else if (itemId == 51093 && pc.getType() != 0 && pc.get_sex() == 1) { // 王族
																pc.setType(0);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(40228, 1);
																pc.getInventory().storeItem(40226, 1);
															} else if (itemId == 51094 && pc.getType() != 1 && pc.get_sex() == 0) { // 騎士
																pc.setType(1);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(40164, 1);
																pc.getInventory().storeItem(40165, 1);
															} else if (itemId == 51094 && pc.getType() != 1 && pc.get_sex() == 1) { // 騎士
																pc.setType(1);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(40164, 1);
																pc.getInventory().storeItem(40165, 1);
															} else if (itemId == 51095 && pc.getType() != 2 && pc.get_sex() == 0) { // 妖精
																pc.setType(2);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(40243, 1);
																pc.getInventory().storeItem(40240, 1);
																pc.getInventory().storeItem(40233, 1);
															} else if (itemId == 51095 && pc.getType() != 2 && pc.get_sex() == 1) { // 妖精
																pc.setType(2);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(40243, 1);
																pc.getInventory().storeItem(40240, 1);
																pc.getInventory().storeItem(40233, 1);
															} else if (itemId == 51096 && pc.getType() != 3 && pc.get_sex() == 0) { // 法師
																pc.setType(3);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(40188, 1);
																pc.getInventory().storeItem(40170, 1);
																pc.getInventory().storeItem(40176, 1);
																pc.getInventory().storeItem(40197, 1);
															} else if (itemId == 51096 && pc.getType() != 3 && pc.get_sex() == 1) { // 法師
																pc.setType(3);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(40188, 1);
																pc.getInventory().storeItem(40170, 1);
																pc.getInventory().storeItem(40176, 1);
																pc.getInventory().storeItem(40197, 1);
															} else if (itemId == 51097 && pc.getType() != 4 && pc.get_sex() == 0) { // 黑暗妖精
																pc.setType(4);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(40276, 1);
																pc.getInventory().storeItem(40270, 1);
																pc.getInventory().storeItem(40268, 1);
															} else if (itemId == 51097 && pc.getType() != 4 && pc.get_sex() == 1) { // 黑暗妖精
																pc.setType(4);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(40276, 1);
																pc.getInventory().storeItem(40270, 1);
																pc.getInventory().storeItem(40268, 1);
															} else if (itemId == 51098 && pc.getType() != 5 && pc.get_sex() == 0) { // 龍騎士
																pc.setType(5);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(210025, 1);
																pc.getInventory().storeItem(210026, 1);
																pc.getInventory().storeItem(210020, 1);
																pc.getInventory().storeItem(210021, 1);
															} else if (itemId == 51098 && pc.getType() != 5 && pc.get_sex() == 1) { // 龍騎士
																pc.setType(5);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(210025, 1);
																pc.getInventory().storeItem(210026, 1);
																pc.getInventory().storeItem(210020, 1);
																pc.getInventory().storeItem(210021, 1);
															} else if (itemId == 51099 && pc.getType() != 6 && pc.get_sex() == 0) { // 幻術師
																pc.setType(6);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(210014, 1);
																pc.getInventory().storeItem(210004, 1);
																pc.getInventory().storeItem(210000, 1);
																pc.getInventory().storeItem(210001, 1);
															} else if (itemId == 51099 && pc.getType() != 6 && pc.get_sex() == 1) { // 幻術師
																pc.setType(6);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(210014, 1);
																pc.getInventory().storeItem(210004, 1);
																pc.getInventory().storeItem(210000, 1);
																pc.getInventory().storeItem(210001, 1);
															} else if (itemId == 51100 && pc.getType() != 7 && pc.get_sex() == 0) { // 戰士
																pc.setType(7);
																pc.setClassId(Mclass[pc.getType()]);
																pc.getInventory().storeItem(210126, 1);
																pc.getInventory().storeItem(210121, 1);
																pc.getInventory().storeItem(210128, 1);
															} else if (itemId == 51100 && pc.getType() != 7 && pc.get_sex() == 1) { // 戰士
																pc.setType(7);
																pc.setClassId(Wclass[pc.getType()]);
																pc.getInventory().storeItem(210126, 1);
																pc.getInventory().storeItem(210121, 1);
																pc.getInventory().storeItem(210128, 1);
															} else if (itemId == 51102 && pc.getType() != 8 && pc.get_sex() == 0) {
																pc.setType(8);
																pc.setClassId(Mclass[pc.getType()]);
															} else if (itemId == 51102 && pc.getType() != 8 && pc.get_sex() == 1) {
																pc.setType(8);
																pc.setClassId(Wclass[pc.getType()]);
															} else if (itemId == 51103 && pc.getType() != 9 && pc.get_sex() == 0) {
																pc.setType(9);
																pc.setClassId(Mclass[pc.getType()]);
															} else if (itemId == 51103 && pc.getType() != 9 && pc.get_sex() == 1) {
																pc.setType(9);
																pc.setClassId(Wclass[pc.getType()]);
															}

															int basestr = 0;
															int basedex = 0;
															int basecon = 0;
															int baseint = 0;
															int basewis = 0;
															int basecha = 0;
															switch (pc.getType()) {
																case 0: // 王族
																	basestr = 13;
																	basedex = 9;
																	basecon = 11;
																	basewis = 11;
																	basecha = 11;
																	baseint = 9;
																	break;
																case 1: // 騎士
																	basestr = 16;
																	basedex = 12;
																	basecon = 16;
																	basewis = 9;
																	basecha = 10;
																	baseint = 8;
																	break;
																case 2: // 妖精
																	basestr = 10;
																	basedex = 12;
																	basecon = 12;
																	basewis = 12;
																	basecha = 9;
																	baseint = 12;
																	break;
																case 3: // 法師
																	basestr = 8;
																	basedex = 7;
																	basecon = 12;
																	basewis = 14;
																	basecha = 8;
																	baseint = 14;
																	break;
																case 4: // 黑暗妖精
																	basestr = 15;
																	basedex = 12;
																	basecon = 12;
																	basewis = 10;
																	basecha = 7;
																	baseint = 11;
																	break;
																case 5: // 龍騎士
																	basestr = 13;
																	basedex = 11;
																	basecon = 14;
																	basewis = 10;
																	basecha = 8;
																	baseint = 10;
																	break;
																case 6: // 幻術師
																	basestr = 9;
																	basedex = 10;
																	basecon = 12;
																	basewis = 14;
																	basecha = 8;
																	baseint = 12;
																	break;
																case 7: // 戰士
																	basestr = 16;
																	basedex = 13;
																	basecon = 16;
																	basewis = 7;
																	basecha = 9;
																	baseint = 10;
																	break;
																case 8: // 劍士
																	basestr = 16;
																	basedex = 13;
																	basecon = 15;
																	basewis = 11;
																	basecha = 5;
																	baseint = 11;
																	break;
																case 9: // 黃金槍騎
																	basestr = 14;
																	basedex = 12;
																	basecon = 16;
																	basewis = 12;
																	basecha = 6;
																	baseint = 9;
																	break;
																default:
																	System.out.println("invalid type " + pc.getType());
																	break;
															}
															pc.getAbility().init();
															pc.getAbility().setBaseStr(basestr);
															pc.getAbility().setBaseInt(baseint);
															pc.getAbility().setBaseWis(basewis);
															pc.getAbility().setBaseDex(basedex);
															pc.getAbility().setBaseCon(basecon);
															pc.getAbility().setBaseCha(basecha);

															SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
															if (noti != null) {
																noti.set_class(MJEClassesType.fromGfx(pc.getClassId()).toInt());
															}

															if (pc.getWeapon() != null)
																pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
															pc.getInventory().takeoffEquip(945);
															pc.sendPackets(new S_CharVisualUpdate(pc));
															for (L1ItemInstance armor : pc.getInventory().getItems()) {
																for (int type = 0; type <= 12; type++) {
																	if (armor != null) {
																		pc.getInventory().setEquipped(armor, false, false, false, false);
																	}
																}
															}
															pc.sendPackets(new S_DelSkill(255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255));
															deleteSpell(pc);
															deletePassiveSpell(pc);

															/** 刪除交換 **/
															pc.getSlotItems(0).clear();
															pc.getSlotItems(1).clear();
															pc.getSlotItems(2).clear();
															pc.getSlotItems(3).clear();

															SC_SUMMON_PET_NOTI.off_summoned(pc);

															pc.setCurrentSprite(pc.getClassId());
															pc.sendShape(pc.getClassId());
															pc.getInventory().removeItem(l1iteminstance, 1);
															pc.save();
															pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));

															GeneralThreadPool.getInstance().schedule(new Runnable() {
																@Override
																public void run() {
																	GameClient clnt = pc.getNetConnection();
																	C_NewCharSelect.restartProcess(pc);
																	Account acc = clnt.getAccount();
																	clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
																	if (acc.countCharacters() > 0)
																		C_CommonClick.sendCharPacks(clnt);
																}
															}, 500L);
															break;
														case 210104:
															Updator.exec("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,5166,5167,39,34,701,2000)", new Handler() {
																@Override
																public void handle(PreparedStatement pstm) throws Exception {
																	pstm.setString(1, client.getAccountName());
																}
															});
			
/*			Connection connection = null;
			connection = L1DatabaseFactory.getInstance().getConnection();
			PreparedStatement preparedstatement = connection.prepareStatement("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,5166,39,34,701,2000)"); // 운영자의방,감옥,배틀존대기실
			preparedstatement.setString(1, client.getAccountName());
			preparedstatement.execute();
			preparedstatement.close();
			connection.close();*/
															pc.getInventory().removeItem(l1iteminstance, 1);
															pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "座標已經正常恢復。稍後將重新啟動。"));
															pc.sendPackets(String.valueOf(new S_SystemMessage("所有角色的座標已經正常恢復。")));

															GeneralThreadPool.getInstance().schedule(new Runnable() {
																@Override
																public void run() {
																	GameClient clnt = pc.getNetConnection();
																	C_NewCharSelect.restartProcess(pc);
																	Account acc = clnt.getAccount();
																	clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
																	if (acc.countCharacters() > 0)
																		C_CommonClick.sendCharPacks(clnt);
																}
																}, 3000L);
															break;
															case 700023:
																ArrayList<L1ItemBookMark> _books = l1iteminstance.getBookMark();
																for (int i = 0; i < pc._bookmarks.size(); i++) {
																	L1BookMark.deleteBookmark(pc, pc._bookmarks.get(i).getName());
																}
																pc._bookmarks.clear();
																pc._speedbookmarks.clear();
																L1BookMark.deleteBookmarkItem(pc);

																for (int i = 0; i < _books.size(); i++) {
																	L1BookMark.addBookmarkItem(pc, _books.get(i));
																}
																pc.sendPackets(new S_BookMarkLoad(pc));
																L1ItemBookMark.deleteBookmarkItem(l1iteminstance.getId());
																pc.getInventory().removeItem(l1iteminstance, 1);
																break;
													}
													if (Config.ServerAdSetting.DelayTimer) {
														pc.addItemDelayTime(l1iteminstance);
													}
												}
												private boolean grantCraftingRecipeBookItem(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
													L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
													if (item != null) {
														item.setCount(count);
														item.setEnchantLevel(EnchantLevel);
														item.setIdentified(true);
														if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
															pc.getInventory().storeItem(item);
														} else {
															pc.sendPackets(new S_ServerMessage(82));// 무게 게이지가 부족하거나 인벤토리가
															return false;
														}
														pc.sendPackets(new S_SystemMessage("道具製作成功。"));
														pc.sendPackets(new S_ServerMessage(143, item.getLogName()));
														pc.send_effect(7976);
														return true;
													} else {
														return false;
													}
												}
												private void deleteSpell (L1PcInstance pc){
													int player = pc.getId();
													Connection con = null;
													PreparedStatement pstm = null;
													try {

														con = L1DatabaseFactory.getInstance().getConnection();
														pstm = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=?");
														pstm.setInt(1, player);
														pstm.execute();
													} catch (Exception e) {
														e.printStackTrace();
													} finally {
														SQLUtil.close(pstm);
														SQLUtil.close(con);
													}
												}

												private void deletePassiveSpell (L1PcInstance pc){
													int player = pc.getId();
													Connection con = null;
													PreparedStatement pstm = null;
													try {

														con = L1DatabaseFactory.getInstance().getConnection();
														pstm = con.prepareStatement("DELETE FROM passive_user_info WHERE character_id=?");
														pstm.setInt(1, player);
														pstm.execute();
													} catch (Exception e) {
														e.printStackTrace();
													} finally {
														SQLUtil.close(pstm);
														SQLUtil.close(con);
													}
												}

												private void useMobEventSpownWand (L1PcInstance pc, L1ItemInstance item){
													try {
														int[][] mobArray = {
																{45042, 45043, 45005, 45010, 45011, 45573, 81201, 45609, 7320217, 7310015, 7310021, 7310028,
																		7310034, 7310041, 7310046, 7310051, 7310056, 7310061, 7310066, 45546, 45600, 45601, 5136,
																		5135, 5146, 45529, 7000093, 7310148, 7310154, 7310160}};
					
					/*{ 45042, 45043, 45005, 45010, 45011 },
					{ 45573, 81201, 45609, 7320217, 7310015, 7310021, 7310028, 7310034, 7310041, 7310046, 7310051, 7310056, 7310061, 7310066 },
					{ 45546, 45600, 45601 },
					{ 5136, 5135, 5146, 45529, 7000093, 7310148, 7310154, 7310160, 45684 } };*/

														int category = 0;
														int rnd = _random.nextInt(mobArray[category].length);
														L1SpawnUtil.spawn(pc, mobArray[category][rnd], 0, 0);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}

												private void petbuy (GameClient client,int npcid, int paytype, int paycount){
													L1PcInstance pc = client.getActiveChar();
													L1PcInventory inv = pc.getInventory();
													int charisma = pc.getAbility().getTotalCha();
													int petcost = 0;
													Object[] petlist = pc.getPetList().values().toArray();
													for (Object pet : petlist) {
														petcost += ((L1NpcInstance) pet).getPetcost();
													}
												if (pc.isCrown()) { // 王族
													charisma += 6;
												} else if (pc.isElf()) { // 精靈
													charisma += 12;
												} else if (pc.isWizard()) { // 法師
													charisma += 6;
												} else if (pc.isDarkelf()) { // 黑暗妖精
													charisma += 6;
												} else if (pc.isDragonknight()) { // 龍騎士
													charisma += 6;
												} else if (pc.isBlackwizard()) { // 幻術師
														charisma += 6;
													}
													charisma -= petcost;
													int petCount = charisma / 6;
													if (petCount <= 0) {
														pc.sendPackets(String.valueOf(new S_ServerMessage(489))); // 想要遣散的寵物太多了.
														return;
													}
													if (pc.getInventory().checkItem(paytype, paycount)) {
														pc.getInventory().consumeItem(paytype, paycount);
														L1SpawnUtil.spawn(pc, npcid, 0, 0);
														L1MonsterInstance targetpet = null;
														L1ItemInstance petamu = null;
														L1PetType petType = null;
														for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
															if (object instanceof L1MonsterInstance) {
																targetpet = (L1MonsterInstance) object;
																petType = PetTypeTable.getInstance().get(targetpet.getNpcTemplate().get_npcId());
																if (petType == null || targetpet.isDead()) {
																	return;
																}
//
//													if (charisma >= 6 && inv.getSize() < 180) {
																if (charisma >= 6 && inv.getSize() < 200) {
																	petamu = inv.storeItem(40314, 1); // 펫의 아뮤렛트
																	if (petamu != null) {
																		new L1PetInstance(targetpet, pc, petamu.getId());
																		pc.sendPackets(String.valueOf(new S_ItemName(petamu)));
																	}
																}
															}
														}
													}
												}
												private boolean createNewItem (L1PcInstance pc,int item_id, int count){
													L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
													if (item != null) {
														item.setCount(count);
														if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
															pc.getInventory().storeItem(item);
														} else {
															pc.sendPackets(new S_ServerMessage(82));
															// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
															return false;
														}
														pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getLogName()))); // %0를
														return true;
													} else {
														return false;
													}
												}

												private boolean createNewItem2 (L1PcInstance pc,int item_id, int count, int EnchantLevel){
													L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
													if (item != null) {
														item.setCount(count);
														item.setEnchantLevel(EnchantLevel);
														item.setIdentified(true);
														if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
															pc.getInventory().storeItem(item);
														} else {
															pc.sendPackets(String.valueOf(new S_ServerMessage(82)));
															// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
															return false;
														}
														pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getLogName()))); // %0를
														return true;
													} else {
														return false;
													}
												}

											private static boolean sealItem(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr, boolean identi) {
// 												sealItem(pc, 5000045, 1, 5, 128);
												L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
												if (item != null) {
													item.setCount(count);
													item.setIdentified(identi);
													item.setEnchantLevel(EnchantLevel);
													item.setAttrEnchantLevel(attr);
													item.setIdentified(true);
													if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
														pc.getInventory().storeItem(item);
														item.setBless(Bless);
														pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
														pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
													} else { // 無法持有時，不取消掉落在地面的處理（防止作弊）
														pc.sendPackets(new S_ServerMessage(82));
														// 重量過重，或是背包已滿，無法再持有更多物品。
														return false;
													}
													pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
													return true;
												} else {
													return false;
												}
												}
											private void resetStats(L1PcInstance pc) { // 屬性重置
													try {
														L1SkillUse l1skilluse = new L1SkillUse();
														l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);

														if (pc.getWeapon() != null) {
															pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
														}

														for (L1ItemInstance armor : pc.getInventory().getItems()) {
															if (armor != null && armor.isEquipped()) {
																pc.getInventory().setEquipped(armor, false, false, false, false);
															}
														}
														pc.sendPackets(new S_CharVisualUpdate(pc));
														pc.sendPackets(new S_OwnCharStatus2(pc));

														pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
														pc.setReturnStat(pc.get_exp());
														pc.sendPackets(new S_SPMR(pc));
														pc.sendPackets(new S_OwnCharAttrDef(pc));
														pc.sendPackets(new S_OwnCharStatus2(pc));
														pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
														pc.save();
													} catch (Exception e) {
														System.out.println("屬性重置命令錯誤");
														e.printStackTrace();
													}
												}
												private void cancelAbsoluteBarrier (L1PcInstance pc){ // 아브소르트바리아의 해제
													if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
														pc.removeSkillEffect(ABSOLUTE_BARRIER);
													}
												}

												public void PureExp (L1PcInstance pc,int type){
													long needExp = ExpTable.getNeedExpNextLevel(52);
													double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
													long exp = 0;
													if (type == 1) {
														exp = (long) (needExp * 0.05D * exppenalty);
													} else if (type == 2) {
														exp = (long) (needExp * 0.06D * exppenalty);
													} else if (type == 3) {
														exp = (long) (needExp * 0.05D * exppenalty);
													} else if (type == 4) {
														exp = (long) (needExp * 0.02D * exppenalty);
													} else if (type == 5) {
														exp = (long) (needExp * 0.01D * exppenalty);
													} else {
														pc.sendPackets(3564);
													}
													pc.add_exp(exp);
													pc.send_effect(3944, true);
												}

												@Override
											public String getType () {
													return C_ITEM_USE2;
												}
										}
								}
						}}}}

	private void sealItem(L1PcInstance pc, int i, int i1, int i2, int i3, int i4, boolean b) {
	}
}

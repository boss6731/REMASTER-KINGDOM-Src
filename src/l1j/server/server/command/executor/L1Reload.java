package l1j.server.server.server.command.executor;

import MJNCoinSystem.MJNCoinCreditLoader;
import MJNCoinSystem.MJNCoinSettings;
import l1j.server.AinhasadSpecialStat.Einpointffecttable;
import l1j.server.ArmorClass.MJArmorClass;
import l1j.server.BonusDropSystem.BonusDropSystemLoader;
import l1j.server.BonusItem.BonusItemLoader;
import l1j.server.BonusMaps.BonusMapTable;
import l1j.server.CPMWBQSystem.CPMWBQSystemProvider;
import l1j.server.CPMWBQSystem.Database.CPMWBQNpcInfoTable;
import l1j.server.CPMWBQSystem.Database.CPMWBQUserTable;
import l1j.server.CPMWBQSystem.Database.CPMWBQmapInfoTable;
import l1j.server.CPMWBQSystem.info.CPMWBQReward;
import l1j.server.Config;
import l1j.server.CraftInfoList.CraftInfoListLoader;
import l1j.server.CraftList.CraftListLoader;
import l1j.server.DollBonusEventSystem.DollBonusEventLoader;
import l1j.server.EQCSystem.EQCLoader;
import l1j.server.EventSystem.EventSystemLoader;
import l1j.server.FatigueProperty;
import l1j.server.ForgottenIsland.FIController;
import l1j.server.GameSystem.Colosseum.ColosseumSpawnTable;
import l1j.server.GameSystem.Colosseum.ColosseumTable;
import l1j.server.GameSystem.Colosseum.L1Colosseum;
import l1j.server.GameSystem.SkillBook.SkillBookLoader;
import l1j.server.InvenBonusItem.InvenBonusItemLoader;
import l1j.server.ItemDropLimit.ItemDropLimitLoader;
import l1j.server.ItemSelector.ItemSelectorLoader;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJ3SEx.MJNpcSpeedData;
import l1j.server.MJ3SEx.MJSprBoundary;
import l1j.server.MJActionListener.ActionListenerLinkageLoader;
import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJActionListener.Npc.ListenerFinderTable;
import l1j.server.MJAutoSystem.MJAutoMapInfo;
import l1j.server.MJCTSystem.Loader.MJCTSpellLoader;
import l1j.server.MJDTSSystem.MJDTSLoader;
import l1j.server.MJEffectSystem.Loader.MJEffectModelLoader;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.MJExpAmpSystem.MJItemExpBonus;
import l1j.server.MJExpRevision.MJFishingExpInfo;
import l1j.server.MJItemExChangeSystem.MJItemExChangeLoader;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJPassiveSkill.MJPassiveLoader;
import l1j.server.MJPushitem.dataloader.MJPushItemData;
import l1j.server.MJServerMacroSystem.MJServerMacroLoader;
import l1j.server.MJTemplate.Chain.Etc.MJHealingPotionDrinkChain;
import l1j.server.MJTemplate.DateSchedulerModel.MinuteScheduler;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_CHANGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI;
import l1j.server.MJTemplate.Spawn.DayAndNight.MJDayAndNightSpawnLoader;
import l1j.server.MJTemplate.Spawn.Normal.MJNormalSpawnLoader;
import l1j.server.MJTemplate.SpellProp.MJSpellProbabilityLoader;
import l1j.server.MJTempleantique.MJempleantiqueController;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSECore;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.NpcShopCash.NpcShopCashTable;
import l1j.server.NpcStatusDamage.NpcStatusDamageInfo;
import l1j.server.QueenAntSystem.QueenAntSpawnlistLoader;
import l1j.server.SpellExtractor.SpellExtractorLoader;
import l1j.server.database.HikariSourceInfo;
import l1j.server.server.Announcecycle;
import l1j.server.server.ClanBuffList.ClanBuffListLoader;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.NpcShopTable3;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.Getback;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.model.item.smelting.SmeltingScrollLoader;
import l1j.server.server.model.skill.noti.MJNotiSkillDatabaseProvider;
import l1j.server.server.model.skill.noti.MJNotiSkillService;
import l1j.server.server.server.Controller.BugRaceController;
import l1j.server.server.server.datatables.*;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.Teleportation;
import l1j.server.tempSkillSystem.tempSkillSystemLoader;

import java.sql.SQLException;
import java.util.HashMap;

public class L1Reload implements L1CommandExecutor {

	private L1Reload() {  }

	public static L1CommandExecutor getInstance() {
		return new L1Reload();
	}

	@Override
	public void execute(L1PcInstance gm, String cmdName, String arg) {
		if (arg.equalsIgnoreCase("怪物掉落")) {
			DropTable.reload();
			gm.sendPackets("\\aGDB:[droplist/droplist_adena] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("遺忘之島")) {
			FIController.getInstance().reload();
			FIController.getInstance().run();
			gm.sendPackets("\\aGDB:[fi_cloud_spawn/fi_night_spawn] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("技能溶解")) {
			SpellExtractorLoader.reload();
			gm.sendPackets("\\aGDB:[spell_extractor] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("任務")) {
			ServerCustomQuestTable.getInstance().reload();
			gm.sendPackets("\\aGDB:[server_custom_quest] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("血盟增益")) {
			ClanBuffListLoader.reload();
			gm.sendPackets("\\aGDB:[clan_buff_items/clan_buff_list] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("背包獎勵")) {
			InvenBonusItemLoader.reload();
			gm.sendPackets("\\aGDB:[inventory_bonus_items] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("臨時技能")) {
			tempSkillSystemLoader.reload();
			gm.sendPackets("\\aGDB:[temp_skill_items] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("再煉石")) {
			SmeltingScrollLoader.reload();
			gm.sendPackets("\\aGDB:[再煉石] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("娃娃活動")) {
			DollBonusEventLoader.reload();
			gm.sendPackets("\\aGDB:[doll_bonus_event_system] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("限量掉落")) {
			ItemDropLimitLoader.reload();
			// ItemDropLimitLoader.getInstance().clearLimitItem();
			gm.sendPackets("\\aGDB:[drop_limit_item] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("蟻后")) {
			QueenAntSpawnlistLoader.getInstance().reload();
			gm.sendPackets("\\aGDB:[spawnlist_queen_ant] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("稅金")) {
			TownNpcTax.reload();
			gm.sendPackets("\\aGDB:[town_npc_tax] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("技能圖標")) {
			MJCTSpellLoader.release();
			gm.sendPackets("\\aGDB:[tb_mjct_spellicon] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("額外掉落")) {
			BonusDropSystemLoader.reload();
			gm.sendPackets("\\aGDB:[bonus_drop_system] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("懲罰物品")) {
			NoDropItemTable.getInstance().reload();
			gm.sendPackets("\\aGDB:[non_drop_penalty_items] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("交易限制物品")) {
			MJMyShopService.service().newExcludeItems();
			gm.sendPackets("\\aGDB:[ncoin_trade_item_exclude] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("技能通知")) {
			MJNotiSkillService.service().newModels(MJNotiSkillDatabaseProvider.entire());
			gm.sendPackets("\\aGDB:[skills_noti] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("NPC資訊")) {
			try {
				NpcActionTable.reload();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gm.sendPackets("\\aJData:[NpcAction->資料夾內的內容.xml] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("資料庫引擎")){
			try {
				L1DatabaseFactory.reload();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			gm.sendPackets("\\aGDB:[database_hikaricp](全部) 重新載入完成!");
		} else if(arg.equalsIgnoreCase("Hikari設定")) {
			try {
				if(HikariSourceInfo.defaultSource == null) {
					return;
				}
				HikariSourceInfo sInfo = MJJsonUtil.fromFile("./config/database_hikaricp.json", HikariSourceInfo.class);
				sInfo.onConfigChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gm.sendPackets("\\aGDB:[database_hikaricp](僅限時間相關) 重新載入完成!");
		} else if (arg.equalsIgnoreCase("活動")) {
			EventSystemLoader.reload();
			gm.sendPackets("\\aGDB:[event_system] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("經驗值獎勵")) {
			BonusExpTable.reload();
			gm.sendPackets("重新載入: bonus_exp 表格已重新載入完成.");
		} else if (arg.equalsIgnoreCase("物品訊息")) {
			ItemMessageTable.reload();
			ItemMessageBoxTable.reload();
			gm.sendPackets("\\aGDB:[item_message/item_message_box] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("技能機率")){
			MJSpellProbabilityLoader.reload();
			gm.sendPackets("\\aGDB:[probability_by_spell] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("搜尋快取")){
			MJMPSECore.reload();
			gm.sendPackets("\\aGDB:[MJMPSECore] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("競技場")){
			ColosseumSpawnTable.reload();
			ColosseumTable.reload();
			L1Colosseum.load_config();
			gm.sendPackets("\\aGDB:[spawnlist_ub]/[colosseumInfo.json]/[ub_settings/ub_times] 等相關表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("格蘭肯")) {
			FatigueProperty.reload();
			gm.sendPackets("\\aHConfig:[fatigue.json] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("銷售系統")) {
			MJNCoinSettings.do_load();
			MJNCoinCreditLoader.reload();
			gm.sendPackets("\\aGDB:[ncoin_trade_settings]/[ncoin_credit] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("等級書籤")) {
			LevelupBookmark.reload();
			gm.sendPackets("\\aGDB:[levelup_addteleport] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("自動狩獵地區")) {
			MJAutoMapInfo.do_load();
			gm.sendPackets("\\aGDB:[auto_map_info] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("服務拒絕")) {
			MJNSDenialAddress.reload();
			gm.sendPackets("\\aGDB:[netsafe_denials] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("經驗值")) {
			ExpTable.do_load();
			gm.sendPackets("\\aGDB:[experience_info] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("物品經驗值")) {
			MJItemExpBonus.do_load();
			gm.sendPackets("\\aGDB:[item_exp_bonus] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("釣魚經驗值")) {
			MJFishingExpInfo.do_load();
			gm.sendPackets("\\aGDB:[fishing_exp_info] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("變身等級")) {
			MJSprBoundary.do_load();
			gm.sendPackets("\\aGDB:[spr_boundary] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("NPC速度")) {
			MJNpcSpeedData.do_load();
			for(L1NpcInstance npc : L1World.getInstance().getAllNpc())
				MJNpcSpeedData.install_npc(npc);
			gm.sendPackets("\\aGDB:[npc_speed_data] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("公共配置")) {
			MJCommons.load();
			gm.sendPackets("\\aGDB:[mjcommons.properties] 配置重新載入完成!");
		} else if(arg.equalsIgnoreCase("防具等級")) {
			MJArmorClass.do_load();
			gm.sendPackets("\\aGDB:[armor_class] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("回復率")) {
			MJHealingPotionDrinkChain.getInstance().load_healing_effect_info();
			gm.sendPackets("\\aGDB:[potion_effect] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("蟲族競賽")) {
			BugRaceController.load_config();
			gm.sendPackets("\\aHConfig:[bug_race.json] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("麻痺時間")) {
			MonsterParalyzeDelay.reload();
			gm.sendPackets("\\aGDB:[monster_paralyze] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("艾因哈薩德地圖")) {
			IncreaseEinhasadMap.reload();
			gm.sendPackets("\\aGDB:[tb_increase_einhasad_map] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("被動技能")) {
			MJPassiveLoader.reload();
			gm.sendPackets("\\aGDB:[passive_book_mapped] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("指定傳送卷軸")) {
			MJDTSLoader.reload();
			gm.sendPackets("\\aGDB:[tb_designate_teleport_scroll] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("裝備交換")) {
			MJItemExChangeLoader.reload();
			EQCLoader.reload();
			ShopTable.reload();
			gm.sendPackets("\\aGDB:[tb_item_exchange_key_info][tb_item_exchange_rewards] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("動作監聽器")) {
			MinuteScheduler.getInstance().clear_action_listener();
			ActionListenerLoader.getInstance().updateNpcActionListener();
			ActionListenerLinkageLoader.reload();
			ListenerFinderTable.reload();
			gm.sendPackets("\\aGDB:[tb_act_listener_類型] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("自動公告")) {
			Announcecycle.getInstance().reloadAnnouncecycle();
			gm.sendPackets("\\aJData:[Announcecycle] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("製作時間")) {
			CraftLimitTimeTrigger.do_load();
			gm.sendPackets("\\aGDB:[craft_limit_trigger] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("製作列表")) {
			CraftInfoListLoader.reload();
			gm.sendPackets("\\aGDB:[craft_list_all] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("製作機率")) {
			CraftProbability.reload();
			gm.sendPackets("\\aGDB:[craft_probability] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("隊伍地圖")) {
			PartyMapInfoTable.reload();
			gm.sendPackets("\\aGDB:[party_map_info] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("人偶機率")) {
			AlchemyProbability.reload();
			gm.sendPackets("\\aGDB:[tb_alchemy_probability] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("製作人偶配置")) {
			Config.onCraftAlchemySettingLoad();
			gm.sendPackets("\\aHConfig:[CraftAlchemySetting.json] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("推送系統")) {
			MJPushItemData.do_load();
			gm.sendPackets("\\aGDB:[push_item_list] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("製作信息")) {
			//MJEProtoMessages.SC_CRAFT_LIST_ALL_ACK.reloadMessage();
			CraftCommonBin.newInstanceByFile();
			CraftListNewAllow.getInstance().reload();
			gm.sendPackets("\\aJData:[craftinfo.dat]/\\aGDB:[craftlist_new_allow] 表格重新載入完成!(統合製作系統)");
//            gm.sendPackets("\aJData:[craftinfo.dat] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("人偶資訊")) {
			MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK.reloadMessage();
			gm.sendPackets("\\aJData:[alchemyinfo.dat] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("再煉資訊")) {
			MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK.reloadMessage();
			gm.sendPackets("\\aJData:[smelting.dat] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("製作")) {
			CraftListLoader.getInstance().reload();
//			SC_CRAFT_LIST_ALL_ACK.reloadedCraftNpc();
			gm.sendPackets("\\aGDB:[craftlist/craftlist_limit_item] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("物品技能")) {
			MJItemSkillModelLoader.reload();
			gm.sendPackets("\\aGDB:[tb_itemskill_model] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("宏")) {
			MJServerMacroLoader.reload();
			gm.sendPackets("\\aGDB:[tb_ServerMacro] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("技能書")) {
			SkillBookLoader.reload();
			gm.sendPackets("\\aGDB:[skill_book_mapped] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("艾因經驗值放大")) {
			MJExpAmplifierLoader.reload();
			gm.sendPackets("\\aGDB:[MJExpAmplifierLoader] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("NPC標記")) {
			MJNpcMarkTable.reload();
			gm.sendPackets("\\aGDB:[NpcMark] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("NPC動作")) {
			NPCTalkDataTable.reload();
			gm.sendPackets("\\aGDB:[NpcAction] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("NPC")) {
			NpcTable.reload();
			gm.sendPackets("\\aGDB:[NpcTable][npc_born] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("物品掉落")) {
			DropItemTable.reload();
			gm.sendPackets("\\aGDB:[drop_item] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("變身")) {
			PolyTable.reload();
			gm.sendPackets("\\aGDB:[polymorphs] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("溶解劑")) {
			ResolventTable.reload();
			gm.sendPackets("\\aGDB:[resolvent] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("溶解劑1")) {
			ResolventTable1.reload();
			gm.sendPackets("\\aGDB:[resolvent1] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("寶箱")) {
			if(L1TreasureBox.load())
				gm.sendPackets("\\aJData:[TreasureBox.xml] 重新載入完成!");
			else {
				String s = "TreasureBox 中發現了錯誤。";
				gm.sendPackets(s);
				System.out.println(s);
			}
		} else if (arg.equalsIgnoreCase("商店類型")) {
			NpcShopCashTable.getInstance().reload();
			gm.sendPackets("\\aGDB:[npc_shop_cash] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("獎勵地圖")) {
			SpecialMapTable.reload();
			gm.sendPackets("\\aGDB:[Bonus_map] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("藥水回復")) {
			L1HealingPotion.reload();
			gm.sendPackets("\\aJData:[HealingPotion.xml] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("配置")) {
			Config.load();
			gm.sendPackets("\\aHConfig:[Config] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("技能")) {
			SkillsTable.reload();
			gm.sendPackets("\\aGDB:[Skill] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("怪物技能")) {
			MobSkillTable.reload();
			gm.sendPackets("\\aGDB:[mobskill] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("祝福賦予")) {
			L1BlessTypeEnchant.reload();
			gm.sendPackets("\\aJData:[BlessTypeEnchant.xml] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("管理員商店")) {
			NpcShopTable.reloding();
			NpcShopTable2.reloding();
			NpcShopTable3.reloding();
			gm.sendPackets("\\aGDB:[NpcShopTable] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("NPC商店出現點")) {
			NpcShopSpawnTable.reloding();
			NpcShopSpawnTable2.reloding();
			NpcShopSpawnTable3.reloding();
			gm.sendPackets("\\aGDB:[NpcShopSpawnTable](管理員商店) 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("等級任務")) {
			CharactersGiftItemTable.reload();
			gm.sendPackets("\\aGDB:[levelup_quests_item] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("封禁IP")) {
			IpTable.getInstance();
			IpTable.reload();
			gm.sendPackets("\\aGDB:[banIp] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("物品")) {
			ItemTable.reload();
			gm.sendPackets("\\aGDB:[armor][etcitem][weapon] 表格重新載入完成!");
			HashMap<Integer, L1Item> items = ItemTable.getInstance().getAllTemplates();
		} else if (arg.equalsIgnoreCase("物品選擇")) {
			ItemSelectorTable.getInstance().reload();
			ItemSelectorLoader.reload();
			gm.sendPackets(new S_SystemMessage("\\aGDB:[ItemSelectorTable] 表格重新載入完成"), true);
		} else if (arg.equalsIgnoreCase("物品收集")) {
			L1TimeCollectionLoader.getInstance().reload();
//            L1TimeCollectionUserLoader.getInstance().reload();
			gm.sendPackets(new S_SystemMessage("\\aGDB:[time_selector][_ability][_duration][_material] 表格重新載入完成"), true);
		} else if (arg.equalsIgnoreCase("商店")) {
			ShopTable.reload();
			gm.sendPackets("\\aGDB:[shop] 表格重新載入完成!");

			//} else if (arg.equalsIgnoreCase("商店")) {
//    ShopTable.reload();
//    for(L1Shop shop : BugRaceController.getInstance().get_shops()) {
//        if(shop == null)
//            continue;
//
//        ShopTable.getInstance().addShop(shop.getNpcId(), shop);
//    }
//    L1Shop ball = PowerBallController.getInstance().get_shop();
//    if(ball != null)
//        ShopTable.getInstance().addShop(ball.getNpcId(), ball);
//    gm.sendPackets("\\aGDB:[shop] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("武器傷害")) {
			WeaponAddDamage.reload();
			WeaponAddHitRate.reload();
			gm.sendPackets("\\aGDB:[weapon_damege] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("血盟數據")) {
			ClanTable.reload();
			MJCastleWarBusiness.getInstance().reload();
			gm.sendPackets("\\aGDB:[clan_data] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("攻城戰")) {
			MJCastleWarBusiness.getInstance().reload();
			gm.sendPackets("\\aGDB:[castle] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("生成日夜")) {
			MJDayAndNightSpawnLoader.getInstance().do_reload();
			gm.sendPackets("\\aGDB:[spawnlist_ex_day_night] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("生成一般")) {
			MJNormalSpawnLoader.getInstance().do_reload();
			gm.sendPackets("\\aGDB:[spawnlist_ex_normal] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("生成清單")) {
			SpawnTable.getInstance().reload();
			gm.sendPackets("\\aGDB:[spawnlist] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("NPC生成清單")) {
			NpcSpawnTable.getInstance().reload();
			gm.sendPackets("\\aGDB:[spawnlist_npc] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("NPC現金商店生成清單")) {
			NpcCashShopSpawnTable.reload();
			gm.sendPackets("\\aGDB:[spawnlist_npc_cash_shop] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("NPC聊天")) {
			NpcChatTable.reload();
			NewNpcChatTable.reload();
			gm.sendPackets("\\aGDB:[npcchat] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("地下城")) {
			Dungeon.reload();
			gm.sendPackets("\\aGDB:[dungeon] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("地圖")) {
			MapsTable.reload();
			gm.sendPackets("\\aGDB:[mapids] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("平衡")) {
			CharacterBalance.getInstance().reload();
			gm.sendPackets("\\aGDB:[character_balance] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("新手物品")) {
			Beginner.reload();
			gm.sendPackets("\\aGDB:[beginner] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("自動掉落")) {
			AutoLoot.reload();
			gm.sendPackets("\\aGDB:[autoloot] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("金幣商店")) {
			AdenShopTable.reload();
			gm.sendPackets("\\aGDB:[shop_aden] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("防具附魔信息")) {
			ArmorEnchantInformationTable.reload();
			gm.sendPackets("\\aGDB:[armor_enchant_list] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("武器附魔信息")) {
			WeaponEnchantInformationTable.reload();
			gm.sendPackets("\\aGDB:[weapon_enchant_list] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("飾品附魔信息")) {
			AccessoryEnchantInformationTable.reload();
			gm.sendPackets("\\aGDB:[accessory_enchant_lis] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("套裝物品")) {
			ArmorSetTable.reload();
			gm.sendPackets("\\aGDB:[armor_set] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("事件警報")) {
			onEventAlram(gm);
		} else if (arg.equalsIgnoreCase("BOSS生成")) {
			BossMonsterSpawnList.init();
			gm.sendPackets("\\aGDB:[spawnlist_boss_date] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("回信")) {
			L1LetterCommand.reload();
			gm.sendPackets("\\aGDB:[letter_command] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("魔法傷害")) {
			SpriteInformationLoader.getInstance().reloadSpellDelayInformation();
			gm.sendPackets("\\aGDB:[tb_magicdelay] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("廣域魔法")) {
			MJEffectModelLoader.reload();
			gm.sendPackets("\\aGDB:[tb_mjeffects] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("新保護怪物")) {
			UserProtectMonsterTable.getInstance().reload();
			gm.sendPackets("\\aGDB:[userprotectmonster] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("魔法人偶")) {
			L1MagicDoll.reload();
			gm.sendPackets("\\aJData:[MagicDoll.xml] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("寶箱物品")) {
			L1BoxItem.reload();
			gm.sendPackets("\\aJData:[BoxItem.xml] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("附魔獎勵")) {
			L1EnchantBonus.reload();
			gm.sendPackets("\\aJData:[EnchantBonus.xml] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("雜物顯示信息")) {
			L1EtcItemViewByte.reload();
			gm.sendPackets("\\aJData:[EtcItemView.xml] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("禁止掉落")) {
			NoDropItem.reload();
			gm.sendPackets("\\aGDB:[nodropitem] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("掉落延遲")) {
			DropDelayItemTable.reload();
			gm.sendPackets("\\aGDB:[drop_delay_item] 表格重新載入完成。");
		} else if (arg.equalsIgnoreCase("村莊NPC")) {
			TownNpcInfoTable.reload();
			gm.sendPackets("\\aGDB:[town_npc_info] 表格重新載入完成。");
		} else if(arg.equalsIgnoreCase("NPC傷害")) {
			NpcStatusDamageInfo.do_load();
			gm.sendPackets("\\aGDB:[npc_status_dmg] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("獎勵物品")) {
			BonusItemLoader.reload();
			gm.sendPackets("\\aGDB:[bonus_item] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("商店時間限制")) {
			ShopBuyLimitInfo.getInstance().reload();
			gm.sendPackets("\\aGDB:[shop_buy_limit_info] 表格重新載入完成!");
			gm.sendPackets("\f2(.使用購買限制命令重置用戶信息列表)");
		} else if(arg.equalsIgnoreCase("艾因怪物")) {
			AinhasadBonusMonsterTable.getInstance().reLoad();
			gm.sendPackets("\\aGDB:[einhasad_monster] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("附魔效果")) {
			EnchantResultTable.getIns().reLoad();
			gm.sendPackets("\\aGDB:[enchant_result] 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("統合製作允許")) {
			CraftListNewAllow.getInstance().reload();
			gm.sendPackets("\\aGDB:[craftlist_new_allow] 表格重新載入完成!(統合製作系統)");
		} else if (arg.equalsIgnoreCase("返回")) {
			Getback.getInstance().reload();
			gm.sendPackets("\\aGDB:[getback](返回座標) 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("返回重啟")) {
			GetBackRestartTable.getInstance().reload();
			gm.sendPackets("\\aGDB:[getback_restart](重啟時返回座標) 表格重新載入完成!");
		} else if (arg.equalsIgnoreCase("保護卷軸")) {
			l1j.server.server.model.item.function.L1ProtectEnchantScroll.reload();
			gm.sendPackets("\\aGDB:[ProtectEnchantScroll.xml] 檔案重新載入完成!");
		} else if (arg.equalsIgnoreCase("增益物品")) {
			l1j.server.server.model.item.function.L1BuffItem.reload();
			gm.sendPackets("\\aJData:[BuffItem.xml] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("選擇物品")) {
			l1j.server.server.model.item.function.L1MeterialChoice.load();
			gm.sendPackets("\\aJData:[MeterialChoice.xml] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("材料物品")) {
			l1j.server.server.model.item.function.L1Material.reload();
			gm.sendPackets("\\aJData:[Meterial.xml] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("召喚棒")) {
			l1j.server.server.model.item.function.L1SpawnWand.reload();
			gm.sendPackets("\\aJData:[SpawnWand.xml] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("獎勵掉落地圖")) {
			BonusMapTable.reload();
			gm.sendPackets("\\aGDB:[bonus_drop_map] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("艾因屬性效果")) {
			Einpointffecttable.reload();
			gm.sendPackets("\\aGDB:[einpoint_effect] 表格重新載入完成!");
		} else if(arg.equalsIgnoreCase("古代神廟")) {
			MJempleantiqueController.getInstance().isopen = false;
			MJempleantiqueController.load_config();
			gm.sendPackets("\\aHConfig:[einpoint_effect](舊古代神廟自動結束) 重新載入完成!");
		} else if(arg.equalsIgnoreCase("討伐任務")) {
			CPMWBQNpcInfoTable.do_load();
			CPMWBQmapInfoTable.do_load();
			CPMWBQReward.service();
			gm.sendPackets("\\aHConfig:[bookquest-service.json/cpmw_bookquest_npcinfo/cpmw_bookquest_mapinfo] 重新載入完成!");
		} else if(arg.equalsIgnoreCase("討伐任務用戶")) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				CPMWBQUserTable userinfo = CPMWBQUserTable.getInstance();
				userinfo.InnituserInfo();
				if(pc.Get_BQ_Info() != null) {
					pc.Get_BQ_Info().clear();
				}
				CPMWBQSystemProvider.provider().BQload(pc);
			}
			L1World.getInstance().broadcastServerMessage("\\aH[怪物圖鑑初始化通知] 由於管理員，怪物圖鑑已被初始化。");
		} else if (arg.equalsIgnoreCase("聖物")) {
			L1FavorBookLoader.getInstance().reload();
			gm.sendPackets("\\aGDB:[favorbook_type][favorbook] 重新載入完成!");
		} else if (arg.equalsIgnoreCase("展示會")) {
			L1TimeCollectionLoader.getInstance().reload();
			gm.sendPackets("\\aGDB:[展示會] 重新載入完成!");
		}else {
//			ServerExplainTable.getInstance().server_Explain(gm, 2);
			gm.sendPackets("\\fY━━━━━━━━━━━━ 管理員重新載入 ━━━━━━━━━━━━");
			gm.sendPackets("\\aG[基本]: .npc .npc動作 .npc聊天 .npc標記 .npc速度 .動作監聽 .怪物技能 .技能");
			gm.sendPackets("\\aO[系統]: .配置 .變身 .商店 .箱子 .箱子物品 .魔法人偶 .事件警報 .恢復速度 .藥水恢復");
			gm.sendPackets("\\aO　　　　.血盟數據 .攻城戰 .boss生成 .生成清單 .銷售系統 .釣魚經驗 .裝備交換");
			gm.sendPackets("\\aO　　　　.npc現金商店生成清單 .npc商店生成 .艾因爆發 .格蘭肯 .傳送卷軸 .推送系統");
			gm.sendPackets("\\aO　　　　.製作 .製作人偶配置 .製作概率 .製作清單 .艾因地圖 .獎勵地圖 .製作信息 .人偶信息");
			gm.sendPackets("\\aO　　　　.人偶概率 .團隊地圖 .詛咒時間 .增益效果 .自動打怪 .技能概率 .數據庫引擎 .光之設定");
			gm.sendPackets("\\aO　　　　.物品選擇 .聖物 .展示會 .被動技能 .技能書");
			gm.sendPackets("\\aY[驅逐]: .封鎖IP .拒絕服務");
			gm.sendPackets("\\aW[物品]: .物品 .物品技能 .物品經驗 .溶解劑(~1) .套裝物品 .製作時間");
			gm.sendPackets("\\aW[掉落]: .怪物掉落 .物品掉落");
			gm.sendPackets("\\aW[附魔]: .武器附魔信息 .飾品附魔信息 .附魔獎勵");
			gm.sendPackets("\\aQ[平衡]: .平衡 .防具等級 .經驗 .武器傷害");
			gm.sendPackets("\\aH[其他]: .等級任務 .任務 .等級書籤 .地下城 .地圖 .新手物品 .回信 .自動掉落 .金幣商店 .宏");
			gm.sendPackets("\\aH　　　 .自動公告 .魔法傷害 .廣域魔法 .雜物顯示信息 .競技場 .新保護怪物 .禁止掉落");
			gm.sendPackets("\\aH　　　 .生成日夜 .生成一般 .搜索緩存 .交織 .村莊NPC .掉落延遲 .物品消息");
			gm.sendPackets("\\aH　　　 .npc信息 .經驗獎勵 .npc傷害 .獎勵物品 .技能通知 .交易限制物品");
			gm.sendPackets("\\aH　　　 .商店類型 .事件 .商店時間限制 .艾因怪物 .懲罰物品 .獎勵掉落 .常用物品");
			gm.sendPackets("\\aH　　　 .法術圖標 .附魔效果 .統合製作允許 .返回 .返回重啟 .稅收 .女王蟻(不可用)");
			gm.sendPackets("\\aH　　　 .保護卷軸 .增益物品 .選擇物品 .材料物品 .召喚棒 .變身等級 .祝福賦予");
			gm.sendPackets("\\aH　　　 .限時掉落 .人偶活動 .獎勵掉落地圖 .血盟增益 .技能溶解 .艾因屬性效果");
			gm.sendPackets("\\aH　　　 .討伐任務 .討伐任務用戶 .古代神廟");
		}
	}

	// TODO 當NPC重新加載時，應自動重新載入
	public static void onEventAlram(L1PcInstance gm) {
		try {
			EventTimeTable.getInstance().reload();
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				SC_NOTIFICATION_CHANGE_NOTI.reload(pc);
				pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, 0, false));
			}
			gm.sendPackets("\\aGDB:[event_boss_time] 表格重新載入完成!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void teleport(L1PcInstance pc, int x, int y, short mapid) {
		pc.set_teleport_x(x);
		pc.set_teleport_y(y);
		pc.set_teleport_map(mapid);
		pc.setHeading(pc.getHeading());
		Teleportation.doTeleportation(pc);
	}
}

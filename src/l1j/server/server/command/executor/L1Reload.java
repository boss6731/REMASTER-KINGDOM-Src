         package l1j.server.server.command.executor;

         import MJNCoinSystem.MJNCoinCreditLoader;
         import MJNCoinSystem.MJNCoinSettings;
         import java.sql.SQLException;
         import java.util.HashMap;
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
         import l1j.server.ClanBuffList.ClanBuffListLoader;
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
         import l1j.server.server.Controller.BugRaceController;
         import l1j.server.server.datatables.AccessoryEnchantInformationTable;
         import l1j.server.server.datatables.AdenShopTable;
         import l1j.server.server.datatables.AinhasadBonusMonsterTable;
         import l1j.server.server.datatables.AlchemyProbability;
         import l1j.server.server.datatables.ArmorEnchantInformationTable;
         import l1j.server.server.datatables.ArmorSetTable;
         import l1j.server.server.datatables.AutoLoot;
         import l1j.server.server.datatables.BonusExpTable;
         import l1j.server.server.datatables.BossMonsterSpawnList;
         import l1j.server.server.datatables.CharacterBalance;
         import l1j.server.server.datatables.CharactersGiftItemTable;
         import l1j.server.server.datatables.ClanTable;
         import l1j.server.server.datatables.CraftLimitTimeTrigger;
         import l1j.server.server.datatables.CraftListNewAllow;
         import l1j.server.server.datatables.CraftProbability;
         import l1j.server.server.datatables.DropDelayItemTable;
         import l1j.server.server.datatables.DropItemTable;
         import l1j.server.server.datatables.DropTable;
         import l1j.server.server.datatables.EnchantResultTable;
         import l1j.server.server.datatables.EventTimeTable;
         import l1j.server.server.datatables.ExpTable;
         import l1j.server.server.datatables.GetBackRestartTable;
         import l1j.server.server.datatables.IncreaseEinhasadMap;
         import l1j.server.server.datatables.IpTable;
         import l1j.server.server.datatables.ItemMessageBoxTable;
         import l1j.server.server.datatables.ItemMessageTable;
         import l1j.server.server.datatables.ItemSelectorTable;
         import l1j.server.server.datatables.ItemTable;
         import l1j.server.server.datatables.LevelupBookmark;
         import l1j.server.server.datatables.MJNpcMarkTable;
         import l1j.server.server.datatables.MapsTable;
         import l1j.server.server.datatables.MobSkillTable;
         import l1j.server.server.datatables.MonsterParalyzeDelay;
         import l1j.server.server.datatables.NPCTalkDataTable;
         import l1j.server.server.datatables.NewNpcChatTable;
         import l1j.server.server.datatables.NoDropItem;
         import l1j.server.server.datatables.NoDropItemTable;
         import l1j.server.server.datatables.NpcActionTable;
         import l1j.server.server.datatables.NpcCashShopSpawnTable;
         import l1j.server.server.datatables.NpcChatTable;
         import l1j.server.server.datatables.NpcShopSpawnTable;
         import l1j.server.server.datatables.NpcShopSpawnTable2;
         import l1j.server.server.datatables.NpcShopSpawnTable3;
         import l1j.server.server.datatables.NpcShopTable;
         import l1j.server.server.datatables.NpcShopTable2;
         import l1j.server.server.datatables.NpcShopTable3;
         import l1j.server.server.datatables.NpcSpawnTable;
         import l1j.server.server.datatables.NpcTable;
         import l1j.server.server.datatables.PartyMapInfoTable;
         import l1j.server.server.datatables.PolyTable;
         import l1j.server.server.datatables.ResolventTable;
         import l1j.server.server.datatables.ResolventTable1;
         import l1j.server.server.datatables.ServerCustomQuestTable;
         import l1j.server.server.datatables.ShopBuyLimitInfo;
         import l1j.server.server.datatables.ShopTable;
         import l1j.server.server.datatables.SkillsTable;
         import l1j.server.server.datatables.SpawnTable;
         import l1j.server.server.datatables.SpecialMapTable;
         import l1j.server.server.datatables.TownNpcInfoTable;
         import l1j.server.server.datatables.TownNpcTax;
         import l1j.server.server.datatables.UserProtectMonsterTable;
         import l1j.server.server.datatables.WeaponAddDamage;
         import l1j.server.server.datatables.WeaponAddHitRate;
         import l1j.server.server.datatables.WeaponEnchantInformationTable;
         import l1j.server.server.model.Beginner;
         import l1j.server.server.model.Dungeon;
         import l1j.server.server.model.Getback;
         import l1j.server.server.model.Instance.L1NpcInstance;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.model.item.L1TreasureBox;
         import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
         import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
         import l1j.server.server.model.item.function.L1BlessTypeEnchant;
         import l1j.server.server.model.item.function.L1BoxItem;
         import l1j.server.server.model.item.function.L1BuffItem;
         import l1j.server.server.model.item.function.L1EnchantBonus;
         import l1j.server.server.model.item.function.L1EtcItemViewByte;
         import l1j.server.server.model.item.function.L1HealingPotion;
         import l1j.server.server.model.item.function.L1MagicDoll;
         import l1j.server.server.model.item.function.L1Material;
         import l1j.server.server.model.item.function.L1MeterialChoice;
         import l1j.server.server.model.item.function.L1ProtectEnchantScroll;
         import l1j.server.server.model.item.function.L1SpawnWand;
         import l1j.server.server.model.item.smelting.SmeltingScrollLoader;
         import l1j.server.server.model.skill.noti.MJNotiSkillDatabaseProvider;
         import l1j.server.server.model.skill.noti.MJNotiSkillService;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.utils.MJCommons;
         import l1j.server.server.utils.Teleportation;
         import l1j.server.tempSkillSystem.tempSkillSystemLoader;





         public class L1Reload
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Reload();
           }


           public void execute(L1PcInstance gm, String cmdName, String arg) {
             if (arg.equalsIgnoreCase("怪物掉落")) {
               DropTable.reload();
               gm.sendPackets("\\aGDB:[droplist/droplist_adena] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("被遺忘的島嶼")) {
               FIController.getInstance().reload();
               FIController.getInstance().run();
               gm.sendPackets("\\aGDB:[fi_cloud_spawn/fi_night_spawn] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("技能溶解")) {
               SpellExtractorLoader.reload();
               gm.sendPackets("\\aGDB:[spell_extractor] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("尋求")) {
               ServerCustomQuestTable.getInstance().reload();
               gm.sendPackets("\\aGDB:[server_custom_quest] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("熱血誓言增益")) {
               ClanBuffListLoader.reload();
               gm.sendPackets("\\aGDB:[clan_buff_items/clan_buff_list] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("庫存紅利")) {
               InvenBonusItemLoader.reload();
               gm.sendPackets("\\aGDB:[inventory_bonus_items] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("臨時技能")) {
               tempSkillSystemLoader.reload();
               gm.sendPackets("\\aGDB:[temp_skill_items] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("重鑄石")) {
               SmeltingScrollLoader.reload();
               gm.sendPackets("\\aGDB:[重鑄石] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("魔法娃娃活動")) {
               DollBonusEventLoader.reload();
               gm.sendPackets("\\aGDB:[doll_bonus_event_system] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("掉落有限")) {
               ItemDropLimitLoader.reload();

               gm.sendPackets("\\aGDB:[drop_limit_item] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("蟻后")) {
               QueenAntSpawnlistLoader.getInstance();
               gm.sendPackets("\\aGDB:[spawnlist_queen_ant] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("稅款")) {
               TownNpcTax.reload();
               gm.sendPackets("\\aGDB:[town_npc_tax] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("拼字圖示")) {
               MJCTSpellLoader.release();
               gm.sendPackets("\\aGDB:[tb_mjct_spellicon] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("獎金掉落")) {
               BonusDropSystemLoader.reload();
               gm.sendPackets("\\aGDB:[bonus_drop_system] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("處罰項目")) {
               NoDropItemTable.getInstance().reload();
               gm.sendPackets("\\aGDB:[non_drop_penalty_items] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("交易限制物品")) {
               MJMyShopService.service().newExcludeItems();
               gm.sendPackets("\\aGDB:[ncoin_trade_item_exclude] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("技能通知")) {
               MJNotiSkillService.service().newModels(MJNotiSkillDatabaseProvider.entire());
               gm.sendPackets("\\aGDB:[skills_noti] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("NPC訊息")) {
               try {
                 NpcActionTable.reload();
               } catch (Exception e) {

                 e.printStackTrace();
               }
               gm.sendPackets("\\aJData:[NpcAction->資料夾內的內容.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("資料庫引擎")) {
               try {
                 L1DatabaseFactory.reload();
               } catch (SQLException e) {
                 e.printStackTrace();
               }
               gm.sendPackets("\\aGDB:[\\aGDB:[database_hikaricp](全部) 重新載入完成！");
             } else if (arg.equalsIgnoreCase("極權設定")) {
               try {
                 if (HikariSourceInfo.defaultSource == null) {
                   return;
                 }
                 HikariSourceInfo sInfo = (HikariSourceInfo)MJJsonUtil.fromFile("./config/database_hikaricp.json", HikariSourceInfo.class);
                 sInfo.onConfigChanged();
               } catch (Exception e) {
                 e.printStackTrace();
               }
               gm.sendPackets("\\aGDB:[\\aGDB:[database_hikaricp](僅與時間相關) 重新載入完成！");
             } else if (arg.equalsIgnoreCase("事件")) {
               EventSystemLoader.reload();
               gm.sendPackets("\\aGDB:[event_system] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("經驗獎勵")) {
               BonusExpTable.reload();
               gm.sendPackets("重新載入: bonus_exp 該表已重新載入.");
             } else if (arg.equalsIgnoreCase("物品留言")) {
               ItemMessageTable.reload();
               ItemMessageBoxTable.reload();
               gm.sendPackets("\\aGDB:[item_message/item_message_box] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("技能機率")) {
               MJSpellProbabilityLoader.reload();
               gm.sendPackets("\\aGDB:[probability_by_spell] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("搜尋快取")) {
               MJMPSECore.reload();
               gm.sendPackets("\\aGDB:[MJMPSECore] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("鬥獸場")) {
               ColosseumSpawnTable.reload();
               ColosseumTable.reload();
               L1Colosseum.load_config();
               gm.sendPackets("\\aGDB:[spawnlist_ub]/[colosseumInfo.json]/[ub_settings/ub_times..其他相關 重新載入完成！");
             } else if (arg.equalsIgnoreCase("格蘭凱恩")) {
               FatigueProperty.reload();
               gm.sendPackets("\\aHConfig:[fatigue.json] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("銷售系統")) {
               MJNCoinSettings.do_load();
               MJNCoinCreditLoader.reload();
               gm.sendPackets("\\aGDB:[ncoin_trade_settings]/[ncoin_credit] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("級別書籤")) {
               LevelupBookmark.reload();
               gm.sendPackets("\\aGDB:[levelup_addteleport] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("狩獵場")) {
               MJAutoMapInfo.do_load();
               gm.sendPackets("\\aGDB:[auto_map_info] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("拒絕服務")) {
               MJNSDenialAddress.reload();
               gm.sendPackets("\\aGDB:[netsafe_denials] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("經驗")) {
               ExpTable.do_load();
               gm.sendPackets("\\aGDB:[experience_info] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("物品體驗")) {
               MJItemExpBonus.do_load();
               gm.sendPackets("\\aGDB:[item_exp_bonus] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("釣魚體驗")) {
               MJFishingExpInfo.do_load();
               gm.sendPackets("\\aGDB:[fishing_exp_info] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("改造等級")) {
               MJSprBoundary.do_load();
               gm.sendPackets("\\aGDB:[spr_boundary] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("NPC速度")) {
               MJNpcSpeedData.do_load();
               for (L1NpcInstance npc : L1World.getInstance().getAllNpc())
                 MJNpcSpeedData.install_npc(npc);
               gm.sendPackets("\\aGDB:[npc_speed_data] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("共通\r\n")) {
               MJCommons.load();
               gm.sendPackets("\\aGDB:[mjcommons.properties] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("裝甲等級")) {
               MJArmorClass.do_load();
               gm.sendPackets("\\aGDB:[armor_class] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("恢復率")) {
               MJHealingPotionDrinkChain.getInstance().load_healing_effect_info();
               gm.sendPackets("\\aGDB:[potion_effect] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("法慶")) {
               BugRaceController.load_config();
               gm.sendPackets("\\aHConfig:[bug_race.json] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("目前時間")) {
               MonsterParalyzeDelay.reload();
               gm.sendPackets("\\aGDB:[monster_paralyze] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("艾因市地圖")) {
               IncreaseEinhasadMap.reload();
               gm.sendPackets("\\aGDB:[tb_increase_einhasad_map] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("被動技能")) {
               MJPassiveLoader.reload();
               gm.sendPackets("\\aGDB:[passive_book_mapped] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("電話訂購單")) {
               MJDTSLoader.reload();
               gm.sendPackets("\\aGDB:[tb_designate_teleport_scroll] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("設備交換")) {
               MJItemExChangeLoader.reload();
               EQCLoader.reload();
               ShopTable.reload();
               gm.sendPackets("\\aGDB:[tb_item_exchange_key_info][tb_item_exchange_rewards] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("動作監聽器")) {
               MinuteScheduler.getInstance().clear_action_listener();
               ActionListenerLoader.getInstance().updateNpcActionListener();
               ActionListenerLinkageLoader.reload();
               ListenerFinderTable.reload();
               gm.sendPackets("\\aGDB:[tb_act_listener_??] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("自動通知")) {
               Announcecycle.getInstance().reloadAnnouncecycle();
               gm.sendPackets("\\aJData:[Announcecycle] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("生產時間")) {
               CraftLimitTimeTrigger.do_load();
               gm.sendPackets("\\aGDB:[craft_limit_trigger] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("生產清單")) {
               CraftInfoListLoader.reload();
               gm.sendPackets("\\aGDB:[craft_list_all] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("生產力")) {
               CraftProbability.reload();
               gm.sendPackets("\\aGDB:[craft_probability] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("派對地圖")) {
               PartyMapInfoTable.reload();
               gm.sendPackets("\\aGDB:[party_map_info] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("娃娃機率")) {
               AlchemyProbability.reload();
               gm.sendPackets("\\aGDB:[tb_alchemy_probability] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("生產娃娃配置")) {
               Config.onCraftAlchemySettingLoad();
               gm.sendPackets("\\aHConfig:[CraftAlchemySetting.json] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("推播系統")) {
               MJPushItemData.do_load();
               gm.sendPackets("\\aGDB:[push_item_list] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("製作資訊")) {

               CraftCommonBin.newInstanceByFile();
               CraftListNewAllow.getInstance().reload();
               gm.sendPackets("\\aJData:[craftinfo.dat]/\\aGDB:[craftlist_new_allow] 表重新載入完成！(綜合製作系統)");
             }
             else if (arg.equalsIgnoreCase("娃娃訊息")) {
               MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK.reloadMessage();
               gm.sendPackets("\\aJData:[alchemyinfo.dat] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("煉油資訊")) {
               MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK.reloadMessage();
               gm.sendPackets("\\aJData:[smelting.dat] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("生產")) {
               CraftListLoader.getInstance().reload();

               gm.sendPackets("\\aGDB:[craftlist/craftlist_limit_item] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("物品技能")) {
               MJItemSkillModelLoader.reload();
               gm.sendPackets("\\aGDB:[tb_itemskill_model] 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("宏[命令]")) {
               MJServerMacroLoader.reload();
               gm.sendPackets("\\aGDB:[tb_ServerMacro] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("技能書")) {
               SkillBookLoader.reload();
               gm.sendPackets("\\aGDB:[skill_book_mapped] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("子認證")) {
               MJExpAmplifierLoader.reload();
               gm.sendPackets("\\aGDB:[MJExpAmplifierLoader] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("NPC馬克")) {
               MJNpcMarkTable.reload();
               gm.sendPackets("\\aGDB:[NpcMark] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("NPC 行動")) {
               NPCTalkDataTable.reload();
               gm.sendPackets("\\aGDB:[NpcAction] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("全國人大")) {
               NpcTable.reload();
               gm.sendPackets("\\aGDB:[NpcTable][npc_born] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("物品掉落物品")) {
               DropItemTable.reload();
               gm.sendPackets("\\aGDB:[drop_item] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("轉換")) {
               PolyTable.reload();
               gm.sendPackets("\\aGDB:[polymorphs] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("溶劑")) {
               ResolventTable.reload();
               gm.sendPackets("\\aGDB:[resolvent] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("溶劑1")) {
               ResolventTable1.reload();
               gm.sendPackets("\\aGDB:[resolvent1] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("寶物盒子")) {
               if (L1TreasureBox.load()) {
                 gm.sendPackets("\\aJData:[TreasureBox.xml] 重新載入完成！");
               } else {
                 String s = "在 TreasureBox 中發現錯誤。";
                 gm.sendPackets(s);
                 System.out.println(s);
               }
             } else if (arg.equalsIgnoreCase("店家類型")) {
               NpcShopCashTable.getInstance().reload();
               gm.sendPackets("\\aGDB:[npc_shop_cash] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("獎勵地圖")) {
               SpecialMapTable.reload();
               gm.sendPackets("\\aGDB:[Bonus_map] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("藥水恢復")) {
               L1HealingPotion.reload();
               gm.sendPackets("\\aJData:[HealingPotion.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("配置")) {
               Config.load();
               gm.sendPackets("\\aHConfig:[Config] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("技能")) {
               SkillsTable.reload();
               gm.sendPackets("\\aGDB:[Skill] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("暴徒[怪物]技能")) {
               MobSkillTable.reload();
               gm.sendPackets("\\aGDB:[mobskill] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("給予祝福")) {
               L1BlessTypeEnchant.reload();
               gm.sendPackets("\\aJData:[BlessTypeEnchant.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("榮子店")) {
               NpcShopTable.reloding();
               NpcShopTable2.reloding();
               NpcShopTable3.reloding();
               gm.sendPackets("\\aGDB:[NpcShopTable] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("NPC 商店生成")) {
               NpcShopSpawnTable.reloding();
               NpcShopSpawnTable2.reloding();
               NpcShopSpawnTable3.reloding();
               gm.sendPackets("\\aGDB:[NpcShopSpawnTable](榮子店) 重新載入完成！");
             } else if (arg.equalsIgnoreCase("關卡任務")) {
               CharactersGiftItemTable.reload();
               gm.sendPackets("\\aGDB:[levelup_quests_item] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("禁止[封鎖]IP")) {
               IpTable.getInstance();
               IpTable.reload();
               gm.sendPackets("\\aGDB:[banIp] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("物品")) {
               ItemTable.reload();
               gm.sendPackets("\\aGDB:[armor][etcitem][weapon] 重新載入完成！");
               HashMap hashMap = ItemTable.getInstance().getAllTemplates();
             } else if (arg.equalsIgnoreCase("項目選擇")) {
               ItemSelectorTable.getInstance().reload();
               ItemSelectorLoader.reload();
               gm.sendPackets((ServerBasePacket)new S_SystemMessage("\\aGDB:[ItemSelectorTable] 重新載入完成！"), true);
             } else if (arg.equalsIgnoreCase("物品收集")) {
               L1TimeCollectionLoader.getInstance().reload();

               gm.sendPackets((ServerBasePacket)new S_SystemMessage("\\aGDB:[time_selector][_ability][_duration][_material] 表已完成重新載入"), true);
             } else if (arg.equalsIgnoreCase("店鋪")) {
               ShopTable.reload();
               gm.sendPackets("\\aGDB:[shop] 重新載入完成！");












             }
             else if (arg.equalsIgnoreCase("武器傷害")) {
               WeaponAddDamage.reload();
               WeaponAddHitRate.reload();
               gm.sendPackets("\\aGDB:[weapon_damege] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("血盟數據")) {
               ClanTable.reload();
               MJCastleWarBusiness.getInstance().reload();
               gm.sendPackets("\\aGDB:[clan_data] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("攻城")) {
               MJCastleWarBusiness.getInstance().reload();
               gm.sendPackets("\\aGDB:[castle] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("贊助")) {
               MJDayAndNightSpawnLoader.getInstance().do_reload();
               gm.sendPackets("\\aGDB:[spawnlist_ex_day_night] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("生成將軍")) {
               MJNormalSpawnLoader.getInstance().do_reload();
               gm.sendPackets("\\aGDB:[spawnlist_ex_normal] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("產生列表")) {
               SpawnTable.getInstance().reload();
               gm.sendPackets("\\aGDB:[spawnlist] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("產生列表")) {
               NpcSpawnTable.getInstance().reload();
               gm.sendPackets("\\aGDB:[spawnlist_npc] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("恩凱商店產生列表")) {
               NpcCashShopSpawnTable.reload();
               gm.sendPackets("\\aGDB:[spawnlist_npc_cash_shop] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("NPC聊天")) {
               NpcChatTable.reload();
               NewNpcChatTable.reload();
               gm.sendPackets("\\aGDB:[npcchat] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("地下城")) {
               Dungeon.reload();
               gm.sendPackets("\\aGDB:[dungeon] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("地圖")) {
               MapsTable.reload();
               gm.sendPackets("\\aGDB:[mapids] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("平衡")) {
               CharacterBalance.getInstance().reload();
               gm.sendPackets("\\aGDB:[character_balance] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("新付款項目")) {
               Beginner.reload();
               gm.sendPackets("\\aGDB:[beginner] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("自動掉落")) {
               AutoLoot.reload();
               gm.sendPackets("\\aGDB:[autoloot] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("網店")) {
               AdenShopTable.reload();
               gm.sendPackets("\\aGDB:[shop_aden] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("盔甲附魔訊息")) {
               ArmorEnchantInformationTable.reload();
               gm.sendPackets("\\aGDB:[armor_enchant_list] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("武器附魔訊息")) {
               WeaponEnchantInformationTable.reload();
               gm.sendPackets("\\aGDB:[weapon_enchant_list] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("阿克賽因·陳信息")) {
               AccessoryEnchantInformationTable.reload();
               gm.sendPackets("\\aGDB:[accessory_enchant_lis] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("設定項目")) {
               ArmorSetTable.reload();
               gm.sendPackets("\\aGDB:[armor_set] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("事件警報")) {
               onEventAlram(gm);
             } else if (arg.equalsIgnoreCase("Boss 生成")) {
               BossMonsterSpawnList.init();
               gm.sendPackets("\\aGDB:[spawnlist_boss_date] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("回覆")) {
               L1LetterCommand.reload();
               gm.sendPackets("\\aGDB:[letter_command] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("魔法交易")) {
               SpriteInformationLoader.getInstance().reloadSpellDelayInformation();
               gm.sendPackets("\\aGDB:[tb_magicdelay] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("區域魔法")) {
               MJEffectModelLoader.reload();
               gm.sendPackets("\\aGDB:[tb_mjeffects] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("新的保護怪物")) {
               UserProtectMonsterTable.getInstance().reload();
               gm.sendPackets("\\aGDB:[userprotectmonster] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("魔法娃娃")) {
               L1MagicDoll.reload();
               gm.sendPackets("\\aJData:[MagicDoll.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("盒子項目")) {
               L1BoxItem.reload();
               gm.sendPackets("\\aJData:[BoxItem.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("附魔獎勵")) {
               L1EnchantBonus.reload();
               gm.sendPackets("\\aJData:[EnchantBonus.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("雜項標籤資訊")) {
               L1EtcItemViewByte.reload();
               gm.sendPackets("\\aJData:[EtcItemView.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("不掉落")) {
               NoDropItem.reload();
               gm.sendPackets("\\aGDB:[nodropitem] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("下降延遲")) {
               DropDelayItemTable.reload();
               gm.sendPackets("\\aGDB:[drop_delay_item] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("村莊NPC")) {
               TownNpcInfoTable.reload();
               gm.sendPackets("\\aGDB:[town_npc_info] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("NPC傷害")) {
               NpcStatusDamageInfo.do_load();
               gm.sendPackets("\\aGDB:[npc_status_dmg] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("獎勵項目")) {
               BonusItemLoader.reload();
               gm.sendPackets("\\aGDB:[bonus_item] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("儲存時間限制")) {
               ShopBuyLimitInfo.getInstance().reload();
               gm.sendPackets("\\aGDB:[shop_buy_limit_info] 重新載入完成！");
               gm.sendPackets("\\f2(.購買限額設定 初始化透過命令更改的用戶資訊列表。)");
             } else if (arg.equalsIgnoreCase("艾因怪物")) {
               AinhasadBonusMonsterTable.getInstance().reLoad();
               gm.sendPackets("\\aGDB:[einhasad_monster] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("附魔效果")) {
               EnchantResultTable.getIns().reLoad();
               gm.sendPackets("\\aGDB:[enchant_result] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("允許一體化生產")) {
               CraftListNewAllow.getInstance().reload();
               gm.sendPackets("\\aGDB:[craftlist_new_allow] 表重新載入完成！(綜合生產系統)");
             } else if (arg.equalsIgnoreCase("登出時的座標")) {
               Getback.getInstance().reload();
               gm.sendPackets("\\aGDB:[getback](傳回登出時的座標) 重新載入完成！");
             } else if (arg.equalsIgnoreCase("恢復重啟")) {
               GetBackRestartTable.getInstance().reload();
               gm.sendPackets("\\aGDB:[getback_restart](租賃重鍍座標) 表重新載入完成！");
             } else if (arg.equalsIgnoreCase("保護令")) {
               L1ProtectEnchantScroll.reload();
               gm.sendPackets("\\aGDB:[ProtectEnchantScroll.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("增益物品")) {
               L1BuffItem.reload();
               gm.sendPackets("\\aJData:[BuffItem.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("所選項目")) {
               L1MeterialChoice.load();
               gm.sendPackets("\\aJData:[MeterialChoice.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("材料項")) {
               L1Material.reload();
               gm.sendPackets("\\aJData:[Meterial.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("召喚棒")) {
               L1SpawnWand.reload();
               gm.sendPackets("\\aJData:[SpawnWand.xml] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("獎勵掉落地圖")) {
               BonusMapTable.reload();
               gm.sendPackets("\\aGDB:[bonus_drop_map] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("愛因斯坦效應")) {
               Einpointffecttable.reload();
               gm.sendPackets("\\aGDB:[einpoint_effect] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("古代諸神的神廟")) {
               (MJempleantiqueController.getInstance()).isopen = false;
               MJempleantiqueController.load_config();
               gm.sendPackets("\\aHConfig:[einpoint_effect](現有古寺自動關閉) 重新載入完成！");
             } else if (arg.equalsIgnoreCase("征服任務")) {
               CPMWBQNpcInfoTable.do_load();
               CPMWBQmapInfoTable.do_load();
               CPMWBQReward.service();
               gm.sendPackets("\\aHConfig:[bookquest-service.json/cpmw_bookquest_npcinfo/cpmw_bookquest_mapinfo] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("征服任務用戶")) {
               for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                 CPMWBQUserTable userinfo = CPMWBQUserTable.getInstance();
                 userinfo.InnituserInfo();
                 if (pc.Get_BQ_Info() != null) {
                   pc.Get_BQ_Info().clear();
                 }
                 CPMWBQSystemProvider.provider().BQload(pc);
               }
               L1World.getInstance().broadcastServerMessage("\\aH[怪物圖鑑初始化通知] 怪物圖鑑已被管理員初始化。");
             } else if (arg.equalsIgnoreCase("聖物")) {
               L1FavorBookLoader.getInstance().reload();
               gm.sendPackets("\\aGDB:[favorbook_type][favorbook] 重新載入完成！");
             } else if (arg.equalsIgnoreCase("展覽")) {
               L1TimeCollectionLoader.getInstance().reload();
               gm.sendPackets("\\aGDB:[展覽] 重新載入完成！");
             } else {

               gm.sendPackets("\\fY============ 管理員重新加載 ============");
               gm.sendPackets("\\aG[基礎]:.NPC.NPC動作.NPC聊天.NPC標記.NPC速度.動作監聽.怪物技能.技能");
               gm.sendPackets("\\aO【系統】：.配置.變身.商店.盒子.盒子物品.魔偶.事件警報.恢復率.藥水恢復");
               gm.sendPackets("\\aO　　　　.血盟資料.攻城.Boss生成.產生清單.銷售系統.釣魚經驗.裝備兌換");
               gm.sendPackets("\\aO　　　　.NC商店產生清單 .NPC商店產生 .身份驗證 .Grankain .電話訂單 .推送系統");
               gm.sendPackets("\\aO　　　　.生產 .生產人偶配置 .生產機率 .生產清單 .Ain地圖 .獎勵地圖 .製作資訊 .人偶資訊");
               gm.sendPackets("\\aO　　　　.人偶機率 .隊伍地圖 .遊標時間 .富慶 .自動狩獵場 .技能機率 .DB引擎 .Hikari設置");
               gm.sendPackets("\\aO　　　　.物品選擇.遺物.展覽.被動技能.技能書");
               gm.sendPackets("\\aY[驅逐]：.BanIP.拒絕服務");
               gm.sendPackets("\\aW[物品]:.TEM .物品技能 .物品經驗 .增溶劑(~1) .設定項目.生產時間");
               gm.sendPackets("\\aW[掉落]：生物掉落、物品掉落。");
               gm.sendPackets("\\aW[附魔]:.武器附魔訊息 .阿克賽因·陳信息 .附魔獎勵");
               gm.sendPackets("\\aQ[平衡]:.平衡.護甲等級.經驗.武器傷害");
               gm.sendPackets("\\aH[ETC]:.關卡任務 .任務 .關卡書籤 .地下城 .地圖 .新物品 .回覆 .自動掉落 .Nshop .宏");
               gm.sendPackets("\\aH　　　 .自動通知 .魔法交易 .區域魔法 .雜項標籤資訊 .鬥獸場 .新的保護怪物 .不掉落");
               gm.sendPackets("\\aH　　　 .贊助 .生成將軍 .搜尋快取 .????? .村莊NPC .下降延遲 .物品留言");
               gm.sendPackets("\\aH　　　 .NPC訊息 .經驗獎勵 .NPC傷害 .獎勵項目 .技能通知 .交易限制物品");
               gm.sendPackets("\\aH　　　 .店家類型 .事件 .儲存時間限制 .艾因怪物 .處罰項目 .獎金掉落 .共通");
               gm.sendPackets("\\aH　　　 .法術圖示 .附魔效果 .允許整合製作 .Getback .Getback重啟 .Tax .QueenAnt(使用 x)");
               gm.sendPackets("\\aH　　　.防護法術.增益物品.選定物品.材料物品.召喚棒.變身等級.祝福");
               gm.sendPackets("\\aH　　　 .有限掉落.人偶事件.獎勵掉落地圖.部落增益.技能溶解.Ein統計效果");
               gm.sendPackets("\\aH　　　 .討伐任務.討伐任務使用者.古神殿");
             }
           }


           public static void onEventAlram(L1PcInstance gm) {
             try {
               EventTimeTable.getInstance().reload();
               for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                 SC_NOTIFICATION_CHANGE_NOTI.reload(pc);
                 pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, 0, false));
               }
               gm.sendPackets("\\aGDB:[event_boss_time] 重新載入完成！");
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



     package l1j.server.server;

     import MJFX.UIAdapter.MJUIAdapter;
     import MJNCoinSystem.MJNCoinCharacterReport;
     import MJNCoinSystem.MJNCoinCreditLoader;
     import MJNCoinSystem.MJNCoinIdFactory;
     import MJShiftObject.Battle.MJShiftBattleArgs;
     import MJShiftObject.MJShiftObjectManager;
     import java.util.Collection;
     import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
     import l1j.server.AinhasadSpecialStat.Einpointffecttable;
     import l1j.server.ArmorClass.MJArmorClass;
     import l1j.server.Beginner.MJBeginnerService;
     import l1j.server.BonusDropSystem.BonusDropSystemLoader;
     import l1j.server.BonusItem.BonusItemLoader;
     import l1j.server.BonusMaps.BonusMapTable;
     import l1j.server.BuyLimitSystem.BuyLimitSystem;
     import l1j.server.BuyLimitSystem.BuyLimitSystemAccountTable;
     import l1j.server.BuyLimitSystem.BuyLimitSystemCharacterTable;
     import l1j.server.CPMWBQSystem.CPMWBQDataLoader;
     import l1j.server.CPMWBQSystem.RenwController.CPMWRenewController;
     import l1j.server.CPMWReNewClan.ClanDungeon.ClanDugeon;
     import l1j.server.ClanBuffList.ClanBuffListLoader;
     import l1j.server.Config;
     import l1j.server.CraftList.CraftListLoader;
     import l1j.server.DollBonusEventSystem.DollBonusEventLoader;
     import l1j.server.DragonRaidSystem.DragonRaidSystemController;
     import l1j.server.DragonRaidSystem.DragonRaidSystemLoader;
     import l1j.server.EventSystem.EventSystemLoader;
     import l1j.server.EventSystem.EventSystemTimeController;
     import l1j.server.ExpMerge.ExpMergeController;
     import l1j.server.GameSystem.Colosseum.ColosseumTimeController;
     import l1j.server.GameSystem.Colosseum.L1Colosseum;
     import l1j.server.GameSystem.SkillBook.SkillBookLoader;
     import l1j.server.ItemDropLimit.ItemDropLimitLoader;
     import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
     import l1j.server.MJ3SEx.MJNpcSpeedData;
     import l1j.server.MJ3SEx.MJSprBoundary;
     import l1j.server.MJActionListener.ActionListenerLinkageLoader;
     import l1j.server.MJActionListener.ActionListenerLoader;
     import l1j.server.MJActionListener.Npc.ListenerFinderTable;
     import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
     import l1j.server.MJAutoSystem.MJAutoMapInfo;
     import l1j.server.MJAutoSystem.MJAutoMapTeleportHandler;
     import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
     import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
     import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
     import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
     import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
     import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
     import l1j.server.MJCompanion.Instance.MJCompanionInstance;
     import l1j.server.MJCompanion.MJCompanionLoader;
     import l1j.server.MJDShopSystem.MJDShopStorage;
     import l1j.server.MJDTSSystem.MJDTSLoader;
     import l1j.server.MJDungeonTimer.Loader.DungeonTimeLoadManager;
     import l1j.server.MJEffectSystem.Loader.MJEffectModelLoader;
     import l1j.server.MJExpAmpSystem.MJItemExpBonus;
     import l1j.server.MJExpRevision.ExpRevision;
     import l1j.server.MJExpRevision.MJFishingExpInfo;
     import l1j.server.MJINNSystem.Loader.MJINNHelperLoader;
     import l1j.server.MJInstanceSystem.Loader.MJInstanceLoadManager;
     import l1j.server.MJItemEnchantSystem.MJItemEnchantSystemLoadManager;
     import l1j.server.MJItemExChangeSystem.MJItemExChangeLoader;
     import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
     import l1j.server.MJKDASystem.MJKDALoadManager;
     import l1j.server.MJKDASystem.MJKDALoader;
     import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
     import l1j.server.MJNetServer.MJClientEntranceService;
     import l1j.server.MJNetServer.MJInterServerEntranceService;
     import l1j.server.MJNetServer.MJNetServerLoadManager;
     import l1j.server.MJPushitem.Controller.PushItemController;
     import l1j.server.MJPushitem.dataloader.MJPushItemData;
     import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
     import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
     import l1j.server.MJServerMacroSystem.MJServerMacroLoader;
     import l1j.server.MJTemplate.Chain.Etc.MJAdenaPickupChain;
     import l1j.server.MJTemplate.Chain.Etc.MJHealingPotionDrinkChain;
     import l1j.server.MJTemplate.DateSchedulerModel.DateSchedulerLoader;
     import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
     import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin;
     import l1j.server.MJTemplate.MJWhiteIP;
     import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
     import l1j.server.MJTemplate.Regen.MJUnderWaterLooper;
     import l1j.server.MJTemplate.Spawn.DayAndNight.MJDayAndNightSpawnLoader;
     import l1j.server.MJTemplate.Spawn.MJSpawnUpdator;
     import l1j.server.MJTemplate.Spawn.Normal.MJNormalSpawnLoader;
     import l1j.server.MJTemplate.SpellProp.MJSpellProbabilityLoader;
     import l1j.server.MJTempleantique.MJempleantiqueController;
     import l1j.server.MJWarSystem.MJCastleWarBusiness;
     import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashResultDatabase;
     import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSECore;
     import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMarketReloadTask;
     import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
     import l1j.server.MJWebServer.MJDefaultWebServer;
     import l1j.server.MJWebServer.MJWSPipeInitializer;
     import l1j.server.MJWebServer.MJWebServerProvider;
     import l1j.server.MWautoBankerProvider.MWautoBankerController;
     import l1j.server.NpcShopCash.NpcShopCashTable;
     import l1j.server.NpcStatusDamage.NpcStatusDamageInfo;
     import l1j.server.Payment.MJPaymentCipher;
     import l1j.server.SpellExtractor.SpellExtractorLoader;
     import l1j.server.TowerOfDominance.BossController.DominanceTimeController;
     import l1j.server.TowerOfDominance.DominanceDataLoader;
     import l1j.server.content.NpcContentLoader;
     import l1j.server.lotto.lotto_character_loader;
     import l1j.server.lotto.lotto_system;
     import l1j.server.lotto.lotto_system_loader;
     import l1j.server.revenge.MJRevengeService;
     import l1j.server.server.Controller.AuctionTimeController;
     import l1j.server.server.Controller.AuraController;
     import l1j.server.server.Controller.BossController;
     import l1j.server.server.Controller.EventItemController;
     import l1j.server.server.Controller.EventThread;
     import l1j.server.server.Controller.FishingTimeController;
     import l1j.server.server.Controller.GhostController;
     import l1j.server.server.Controller.HouseTaxTimeController;
     import l1j.server.server.Controller.LightTimeController;
     import l1j.server.server.Controller.NpcChatTimeController;
     import l1j.server.server.Controller.PcInventoryDeleteController;
     import l1j.server.server.Controller.PremiumTimeController;
     import l1j.server.server.Controller.SkillDataController;
     import l1j.server.server.Controller.TamController;
     import l1j.server.server.datatables.AccessoryEnchantInformationTable;
     import l1j.server.server.datatables.AinhasadBonusMonsterTable;
     import l1j.server.server.datatables.AlchemyProbability;
     import l1j.server.server.datatables.ArmorEnchantInformationTable;
     import l1j.server.server.datatables.ArmorEnchantList;
     import l1j.server.server.datatables.ArmorSetTable;
     import l1j.server.server.datatables.BonusExpTable;
     import l1j.server.server.datatables.BossMonsterSpawnList;
     import l1j.server.server.datatables.BuddyTable;
     import l1j.server.server.datatables.CharacterBalance;
     import l1j.server.server.datatables.CharacterFreeShieldTable;
     import l1j.server.server.datatables.CharacterSkillDelayTable;
     import l1j.server.server.datatables.CharacterTable;
     import l1j.server.server.datatables.ClanBanListTable;
     import l1j.server.server.datatables.ClanBuffTable;
     import l1j.server.server.datatables.ClanStorageTable;
     import l1j.server.server.datatables.ClanTable;
     import l1j.server.server.datatables.CraftLimitTimeTrigger;
     import l1j.server.server.datatables.CraftListNewAllow;
     import l1j.server.server.datatables.CraftProbability;
     import l1j.server.server.datatables.DoorSpawnTable;
     import l1j.server.server.datatables.DropDelayItemTable;
     import l1j.server.server.datatables.DropItemTable;
     import l1j.server.server.datatables.DropTable;
     import l1j.server.server.datatables.EnchantResultTable;
     import l1j.server.server.datatables.EventTimeTable;
     import l1j.server.server.datatables.ExpTable;
     import l1j.server.server.datatables.FishingZoneTable;
     import l1j.server.server.datatables.FurnitureSpawnTable;
     import l1j.server.server.datatables.GetBackRestartTable;
     import l1j.server.server.datatables.IncreaseEinhasadMap;
     import l1j.server.server.datatables.InterRaceZoneTable;
     import l1j.server.server.datatables.IpTable;
     import l1j.server.server.datatables.ItemMessageBoxTable;
     import l1j.server.server.datatables.ItemMessageTable;
     import l1j.server.server.datatables.ItemSelectorTable;
     import l1j.server.server.datatables.ItemTable;
     import l1j.server.server.datatables.KeyTable;
     import l1j.server.server.datatables.LevelupBookmark;
     import l1j.server.server.datatables.LightSpawnTable;
     import l1j.server.server.datatables.MJAlchemyProbabilityBox;
     import l1j.server.server.datatables.MJNpcMarkTable;
     import l1j.server.server.datatables.MJSmeltingProbabilityBox;
     import l1j.server.server.datatables.MapsTable;
     import l1j.server.server.datatables.MobGroupTable;
     import l1j.server.server.datatables.MonsterParalyzeDelay;
     import l1j.server.server.datatables.NPCTalkDataTable;
     import l1j.server.server.datatables.NewNpcChatTable;
     import l1j.server.server.datatables.NoDropItemTable;
     import l1j.server.server.datatables.NpcActionTable;
     import l1j.server.server.datatables.NpcCashShopSpawnTable;
     import l1j.server.server.datatables.NpcChatTable;
     import l1j.server.server.datatables.NpcShopTable;
     import l1j.server.server.datatables.NpcSpawnTable;
     import l1j.server.server.datatables.NpcTable;
     import l1j.server.server.datatables.PartyMapInfoTable;
     import l1j.server.server.datatables.PetTypeTable;
     import l1j.server.server.datatables.PolyTable;
     import l1j.server.server.datatables.RaceTable;
     import l1j.server.server.datatables.ReportTable;
     import l1j.server.server.datatables.ResolventTable;
     import l1j.server.server.datatables.ServerCustomQuestTable;
     import l1j.server.server.datatables.ShopBuyLimitInfo;
     import l1j.server.server.datatables.ShopTable;
     import l1j.server.server.datatables.SkillsTable;
     import l1j.server.server.datatables.SmeltingProbability;
     import l1j.server.server.datatables.SpawnTable;
     import l1j.server.server.datatables.SpecialMapTable;
     import l1j.server.server.datatables.TownNpcInfoTable;
     import l1j.server.server.datatables.TownNpcTax;
     import l1j.server.server.datatables.UBSpawnTable;
     import l1j.server.server.datatables.UBTable;
     import l1j.server.server.datatables.UserProtectMonsterTable;
     import l1j.server.server.datatables.WeaponAddDamage;
     import l1j.server.server.datatables.WeaponEnchantInformationTable;
     import l1j.server.server.datatables.WeaponEnchantList;
     import l1j.server.server.datatables.WeaponSkillTable;
     import l1j.server.server.model.Dungeon;
     import l1j.server.server.model.ElementalStoneGenerator;
     import l1j.server.server.model.Getback;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.Instance.L1PcTimerControlHandler;
     import l1j.server.server.model.L1BossCycle;
     import l1j.server.server.model.L1CastleLocation;
     import l1j.server.server.model.L1DeleteItemOnGround;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1World;
     import l1j.server.server.model.gametime.GameTimeClock;
     import l1j.server.server.model.gametime.RealTimeClock;
     import l1j.server.server.model.item.L1TreasureBox;
     import l1j.server.server.model.item.collection.favor.loader.FavorBookCraftLoader;
     import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
     import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
     import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
     import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
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
     import l1j.server.server.model.map.L1WorldMap;
     import l1j.server.server.model.skill.noti.MJNotiSkillDatabaseProvider;
     import l1j.server.server.model.skill.noti.MJNotiSkillService;
     import l1j.server.server.model.trap.L1WorldTraps;
     import l1j.server.server.monitor.LoggerInstance;
     import l1j.server.server.templates.L1BookMarkIdFactory;
     import l1j.server.server.utils.MJCommons;
     import server.threads.pc.AutoSaveThread;
     import server.threads.pc.CharacterQuickCheckThread;






     public class GameServer
     {
       private static GameServer _instance;

       public static GameServer getInstance() {
         if (_instance == null)
           synchronized (GameServer.class) {
             if (_instance == null) {
               _instance = new GameServer();
             }
           }
         return _instance;
       }

       public void initialize() throws Exception {
         GeneralThreadPool.getInstance();
         IdFactory.getInstance();

         if (Config.Web.webServerOnOff) {
           MJWebServerProvider.provider().bind(Config.Web.webServerPort, (MJWSPipeInitializer)new MJDefaultWebServer(null));
         }
         MJMarketReloadTask.getInstance();

         MJBeginnerService.onApplicationStartup();

         MJNotiSkillService.service().newModels(MJNotiSkillDatabaseProvider.entire());

         L1WorldMap.getInstance();
         ExpTable.do_load();
         CharacterBalance.getInstance();
         SpriteInformationLoader.getInstance().loadSpriteInformation();
         MJNetSafeLoadManager.getInstance().load();
         MJClientEntranceService.service();
         MJInterServerEntranceService.service().execute();
         MJFishingExpInfo.do_load();
         MJArmorClass.do_load();
         MJCommons.load();

         CraftProbability.DEFAULT.load_probabilities();
         CraftProbability.EVENT.load_probabilities();
         CraftLimitTimeTrigger.do_load();
         AlchemyProbability.getInstance();
         MJAlchemyProbabilityBox.getInstance();
         SmeltingProbability.getInstance();
         MJSmeltingProbabilityBox.getInstance();

         CharacterTable.getInstance().loadAllCharName();
         MJSprBoundary.do_load();
         ExpRevision.do_revision();
         ItemMessageTable.getInstance();
         ItemMessageBoxTable.getInstance();
         BonusExpTable.getInstance();
         NpcStatusDamageInfo.do_load();
         NpcShopCashTable.getInstance();


         CharacterTable.clearOnlineStatus();

         GameTimeClock.init();

         RealTimeClock.init();
         KeyTable.initBossKey();
         ArmorEnchantList.getInstance();
         WeaponEnchantList.getInstance();
         MJItemExpBonus.do_load();
         ColosseumTimeController ubTimeContoroller = ColosseumTimeController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)ubTimeContoroller, 0L, 15000L);




         if (Config.ServerAdSetting.ELEMENTALSTONEAMOUNT > 0) {
           ElementalStoneGenerator elementalStoneGenerator = ElementalStoneGenerator.getInstance();
           GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)elementalStoneGenerator, 0L, 300000L);
         }
         LoggerInstance.getInstance();
         NpcShopTable.getInstance();
         SkillDataController.getInstance();
         PremiumTimeController premiumTimeController = PremiumTimeController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)premiumTimeController, 0L, PremiumTimeController.SLEEP_TIME);
         MJWhiteIP.getInstance();
         TamController tamController = TamController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)tamController, 0L, TamController.SLEEP_TIME);
         if (Config.ServerRates.Eventof) {
           EventItemController eventItemController = EventItemController.getInstance();
           GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)eventItemController, 0L, 60000L);
         }
         AuraController braveavatarController = AuraController.getInstance();
         GeneralThreadPool.getInstance().execute((Runnable)braveavatarController);
         AuctionTimeController auctionTimeController = AuctionTimeController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)auctionTimeController, 0L, 60000L);
         HouseTaxTimeController houseTaxTimeController = HouseTaxTimeController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)houseTaxTimeController, 0L, 600000L);
         FishingTimeController fishingTimeController = FishingTimeController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)fishingTimeController, 0L, 1000L);
         NpcChatTimeController npcChatTimeController = NpcChatTimeController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)npcChatTimeController, 0L, 60000L);
         PcInventoryDeleteController pcInventoryDeleteController = PcInventoryDeleteController.getInstance();
         GeneralThreadPool.getInstance().scheduleAtFixedRate((Runnable)pcInventoryDeleteController, 0L, 60000L);







         EventSystemLoader.getInstance();
         EventSystemTimeController.getInstance().Start();



         NpcContentLoader.getInstance();

         NpcTable.getInstance();
         TownNpcInfoTable.getInstance();
         TownNpcTax.getInstance();
         MJNpcSpeedData.do_load();
         L1DeleteItemOnGround deleteitem = new L1DeleteItemOnGround();
         deleteitem.initialize();
         if (!NpcTable.getInstance().isInitialized()) {
           throw new Exception("Could not initialize the npc table");
         }
         NpcCashShopSpawnTable.getInstance();
         NpcCashShopSpawnTable.getInstance().Start();
         MJItemSkillModelLoader.getInstance();
         SpawnTable.getInstance();
         MobGroupTable.getInstance();
         SkillsTable.getInstance();
         PolyTable.getInstance();
         ItemTable.getInstance();
         ItemTable.getInstance().initRace();
         DropTable.getInstance();
         DropItemTable.getInstance();

         ItemSelectorTable.getInstance();

         L1TimeCollectionLoader.getInstance();
         L1TimeCollectionUserLoader.getInstance();

         L1FavorBookLoader.getInstance();
         L1FavorBookUserLoader.getInstance();

         lotto_system.getInstance();
         lotto_system_loader.getInstance();
         lotto_character_loader.getInstance();


         ShopBuyLimitInfo.getInstance();

         ShopTable.getInstance();
         NPCTalkDataTable.getInstance();
         L1World.getInstance();
         L1WorldTraps.getInstance();
         Dungeon.getInstance();
         NpcSpawnTable.getInstance();
         IpTable.getInstance();
         MapsTable.getInstance();
         UBSpawnTable.getInstance();

         ClanTable.getInstance();
         ClanBuffTable.getInstance();
         L1CastleLocation.setCastleTaxRate();
         GetBackRestartTable.getInstance();
         DoorSpawnTable.getInstance();

         WeaponSkillTable.getInstance();
         NpcActionTable.load();
         GMCommandsConfig.load();
         Getback.loadGetBack();
         PetTypeTable.load();
         L1BossCycle.load();
         L1TreasureBox.load();
         L1HealingPotion.load();
         DominanceDataLoader.getInstance();
         DominanceTimeController.getInstance();
         RaceTable.getInstance();
         ResolventTable.getInstance();
         FurnitureSpawnTable.getInstance();
         NpcChatTable.getInstance();
         NewNpcChatTable.getInstance();
         LightSpawnTable.getInstance();
         LightTimeController.start();
         Announcements.getInstance();
         WeaponAddDamage.getInstance();
         ArmorSetTable.getInstance();

         ArmorEnchantInformationTable.getInstance();
         WeaponEnchantInformationTable.getInstance();
         AccessoryEnchantInformationTable.getInstance();

         EventTimeTable.getInstance();
         GeneralThreadPool.getInstance().execute((Runnable)EventThread.getInstance());

         BossMonsterSpawnList.init();
         BossController.getInstance();
         SpecialMapTable.getInstance();
         FishingZoneTable.getInstance();
         InterRaceZoneTable.getInstance();
         DragonRaidSystemLoader.getInstance();
         DragonRaidSystemController.getInstance();
         DropDelayItemTable.getInstance();

         BuddyTable.getInstance();
         AutoCashResultDatabase.getIntstance();




         GeneralThreadPool.getInstance().execute((Runnable)GhostController.getInstance());
         if (Config.ServerAdSetting.ANNOUNCECYCLESETTING) {
           Announcecycle.getInstance();
         }
         ReportTable.getInstance();







         MJRankLoadManager.getInstance().load();
         MJKDALoadManager.getInstance().load();
         MJNpcMarkTable.getInstance();
         PartyMapInfoTable.getInstance();
         MJRaidLoadManager.getInstance().load();
         MJEffectModelLoader.getInstance();
         MJInstanceLoadManager.getInstance().load();
         L1MagicDoll.load();
         L1BoxItem.load();
         L1EnchantBonus.load();
         L1EtcItemViewByte.load();
         MJBotLoadManager.getInstance().load();
         MJServerMacroLoader.getInstance().start();
         MJCopyMapObservable.getInstance();
         MJINNHelperLoader.getInstance();
         MJAttendanceLoadManager.getInstance().load();
         ClanTable.getInstance().createTutorialClan();
         BQSLoadManager.getInstance().run();
         DateSchedulerLoader.getInstance().run();
         ActionListenerLoader.getInstance();
         ActionListenerLinkageLoader.getInstance();
         ListenerFinderTable.getInstance();
         MJCombatLoadManager.getInstance().load();
         MJCaptchaLoadManager.getInstance().load();
         MJItemEnchantSystemLoadManager.getInstance().load();
         MJDTSLoader.getInstance();
         DungeonTimeLoadManager.getInstance().load();
         MJItemExChangeLoader.getInstance();
         IncreaseEinhasadMap.getInstance();
         UserProtectMonsterTable.getInstance();
         if (Config.Login.UseShiftServer) {
           MJShiftBattleArgs.load();
           MJShiftObjectManager.getInstance();
           MJShiftObjectManager.getInstance().load_common_server_info();
         }
         MonsterParalyzeDelay.getInstance();
         MJAdenaPickupChain.setup_blessing_effect();
         MJHealingPotionDrinkChain.setup_default_healing_handler();
         MJCompanionLoader.getInstance();
         MJPaymentCipher.getInstance();

         L1BookMarkIdFactory.getInstance();
         LevelupBookmark.getInstance();
         MJCTLoadManager.getInstance().load();
         MJAutoMapTeleportHandler.getInstance();
         MJAutoMapInfo.do_load();
         MJNCoinIdFactory.do_values_load();
         MJNCoinCharacterReport.getInstance();
         MJNCoinCreditLoader.getInstance();


         if (Config.Synchronization.CHARACTER_SAVED_SYSTEM) {
           GeneralThreadPool.getInstance().execute((Runnable)AutoSaveThread.getInstance());
         }
         if (Config.Synchronization.CHARACTER_CHECK_SYSTEM) {
           CharacterQuickCheckThread.getInstance();
         }
         System.gc();
         Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
         UBTable.getInstance().getUb(1).start();
         MJCastleWarBusiness.getInstance().run();
         if (Config.ServerAdSetting.CLANLEADER) {
           MJBotNameLoader.getInstance().createClanLeaders();
         }
         MJDShopStorage.clearProcess();
         MJUnderWaterLooper.getInstance();
         MJSpellProbabilityLoader.getInstance();

         BonusItemLoader.getInstance();
         ExpMergeController.controller();
         MJRevengeService.service();
         AinhasadBonusMonsterTable.getInstance();

         NoDropItemTable.getInstance();

         BuyLimitSystemCharacterTable.getInstance();
         BuyLimitSystemAccountTable.getInstance();
         BuyLimitSystem.getInstance();

         L1ProtectEnchantScroll.load();
         L1BuffItem.load();
         L1MeterialChoice.load();
         L1Material.load();
         L1SpawnWand.load();
         L1BlessTypeEnchant.load();

         CraftListLoader.getInstance();
         BonusDropSystemLoader.getInstance();

         AinhasadSpecialStatLoader.getInstance();

         Opcodes.getIns();

         EnchantResultTable.getIns();

         CharacterSkillDelayTable.getInstace();
         CharacterFreeShieldTable.getInstance();

         CraftListNewAllow.getInstance();

         FavorBookCraftLoader.getInstance();



         L1PcTimerControlHandler.getInstance();

         ItemDropLimitLoader.getInstance();

         DollBonusEventLoader.getInstance();

         BonusMapTable.getInstance();

         CraftCommonBin.newInstanceByFile();

         ClanBuffListLoader.getInstance();

         ServerCustomQuestTable.getInstance();

         MWautoBankerController.getInstance().CheckStart();

         SpellExtractorLoader.getInstance();

         Einpointffecttable.getInstance();

         CPMWBQDataLoader.getInstance();

         CPMWRenewController.getInstance();

         MJempleantiqueController.load_config();

         ClanDugeon.getInstance().load_config();

         L1Colosseum.load_config();


         ClanStorageTable.getInstance();
         ClanBanListTable.getInstance();
         SkillBookLoader.getInstance();


         MJAlchemyProbabilityBox.getInstance().shuffleList();
         MJSmeltingProbabilityBox.getInstance().shuffleList();

         MJPushItemData.do_load();
         PushItemController pic = PushItemController.getInstance();
         GeneralThreadPool.getInstance().execute((Runnable)pic);

         GeneralThreadPool.getInstance().execute(new Runnable()
             {
               public void run() {
                 MJNormalSpawnLoader.getInstance();
                 MJDayAndNightSpawnLoader.getInstance();
                 MJSpawnUpdator.getInstance();
               }
             });
         GeneralThreadPool.getInstance().execute(new Runnable()
             {
               public void run() {
                 MJMyResource.onApplicationStartup();
               }
             });


         MJEProtoMessages.getInstance();
       }

       public void disconnectAllCharacters() {
         Collection<L1PcInstance> pcList = L1World.getInstance().getAllPlayers();
         if (pcList == null || pcList.size() == 0) {
           return;
         }
         for (L1PcInstance pc : pcList) {
           if (pc == null || pc.noPlayerck2)
             continue;
           try {
             if (pc.getNetConnection() != null) {
               pc.save();
               pc.saveInventory();
               pc.getNetConnection().setActiveChar(null);
               pc.getNetConnection().kick();
               L1World.getInstance().removeObject((L1Object)pc);
             }
             pc.logout();
           } catch (Exception e) {
             e.printStackTrace();
           }
         }
       }



       public int saveAllCharInfo() {
         int cnt = 0;
         try {
           for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
             if (pc.noPlayerCK || pc.noPlayerRobot || pc.noPlayerck2)
               continue;
             cnt++;
             pc.save();
             pc.saveInventory();
           }
         } catch (Exception e) {
           return -1;
         }

         return cnt;
       }




       public static void disconnectChar(L1PcInstance pc) {
         if (pc.getNetConnection() != null) {
           pc.getNetConnection().kick();
         }
         pc.logout();
       }

       public static void disconnectChar(String name) {
         L1PcInstance pc = L1World.getInstance().getPlayer(name);

         if (pc != null)
           disconnectChar(pc);
       }

       private class ServerShutdownThread
         extends Thread {
         private final int _secondsCount;

         public ServerShutdownThread(int secondsCount) {
           this._secondsCount = secondsCount;
         }


          public void run() {
             // 獲取 L1World 的單例實例
               L1World world = L1World.getInstance();
               try {
                   // 初始化倒計時秒數
                   int secondsCount = this._secondsCount;

                   // 向所有玩家廣播伺服器即將關閉的消息
                   world.broadcastServerMessage("\f3伺服器即將關閉.");
                   world.broadcastServerMessage("\f3請在安全的地方登出");

                   // 當倒計時大於 0 時繼續循環
                   while (0 < secondsCount) {
                       // 如果倒計時小於等於 30 秒，廣播詳細的倒計時消息
                       if (secondsCount <= 30) {
                           System.out.println("天堂將於\aA " + secondsCount + "秒後停止。請在安全的地方退出。");
                           world.broadcastServerMessage("\aA天堂將於\f3" + secondsCount + "\aA秒後停止. \aA請在安全的地方\aA退出.");
                       }
                       // 如果倒計時是整分鐘，廣播分鐘數的倒計時消息
                       else if (secondsCount % 60 == 0) {
                           System.out.println("天堂將於\aA " + (secondsCount / 60) + "分鐘後停止。請在安全的地方退出.");
                           world.broadcastServerMessage("\aA天堂將於\f3" + (secondsCount / 60) + "\aA分鐘後停止. \aA請在安全的地方 \aA退出.");
                       }

                       // 每秒減少一次倒計時
                       Thread.sleep(1000L);
                       secondsCount--;
                   }
                   // 關閉伺服器
                   GameServer.this.shutdown();
               } catch (InterruptedException e) {
                   // 如果捕捉到中斷異常，廣播伺服器關閉已中止的消息
                   world.broadcastServerMessage("\f4伺服器關閉已中止。伺服器運行正常.");
                   return;
               }
          }

       private ServerShutdownThread _shutdownThread = null;

       public synchronized void shutdownWithCountdown(int secondsCount) {
         if (this._shutdownThread != null) {
           return;
         }


         this._shutdownThread = new ServerShutdownThread(secondsCount);
         this._shutdownThread.start();
         Collection<L1PcInstance> pcList = L1World.getInstance().getAllPlayers();
         for (L1PcInstance pc : pcList) {
           if (pc == null || pc.noPlayerck2)
             continue;
           pc.서버다운중 = true;
         }
       }

       public void shutdown() {
         try {
           ShopBuyLimitInfo.getInstance().save();

           if (Config.Login.UseShiftServer) {
             MJShiftObjectManager.getInstance().release();
           }
           MJInterServerEntranceService.service().release();
           MJClientEntranceService.service().release();
           MJWebServerProvider.provider().shutdown();
           MJMPSECore.release();
           MJCompanionInstance.do_store_companions();
           MJKDALoader.getInstance().store();
           disconnectAllCharacters();
           MJRaidLoadManager.getInstance().release();
           MJInstanceLoadManager.getInstance().release();
           MJDShopStorage.clearProcess();
           MJCTLoadManager.getInstance().release();
           MJBotLoadManager.getInstance().release();
           MJItemSkillModelLoader.release();
           MJKDALoadManager.getInstance().release();
           MJNetSafeLoadManager.getInstance().release();
           MJNetServerLoadManager.release();
           LoggerInstance.getInstance().flush();
           MJUIAdapter.on_exit();
         } catch (Exception e1) {
           e1.printStackTrace();
         }
       }

       public synchronized void abortShutdown() {
         if (this._shutdownThread == null) {
           return;
         }



         this._shutdownThread.interrupt();
         this._shutdownThread = null;
       }
     }



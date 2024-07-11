 package l1j.server.server.model.Instance;

 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.Config;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.server.Controller.BugRaceController;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.datatables.TownTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1PolyMorph;
 import l1j.server.server.model.L1Quest;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.gametime.GameTimeClock;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_ShopSellList;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;






 public class L1MerchantInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private int clanid = 0; private String clanname; private static final long REST_MILLISEC = 10000L;
   private AtomicInteger _restCallCount;

   public int getClanid() {
     return this.clanid;
   }

   public void setClanid(int i) {
     this.clanid = i;
   }

   public String getClanname() {
     return this.clanname;
   }

   public void setClanname(String s) {
     this.clanname = s;
   }

   public L1MerchantInstance(L1Npc template) {
     super(template);
     this._restCallCount = new AtomicInteger(0);
   }


   public void onAction(L1PcInstance pc) {
     L1Attack attack = new L1Attack(pc, this);
     attack.calcHit();
     attack.action();
   }


   public void onNpcAI() {
     if (isAiRunning()) {
       return;
     }
     setActived(false);
     startAI();
   }



   public void onTalkAction(L1PcInstance player) {
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     int npcid = getNpcTemplate().get_npcId();
     L1Quest quest = player.getQuest();
     String htmlid = null;
     String[] htmldata = null;

     int pcX = player.getX();
     int pcY = player.getY();
     int npcX = getX();
     int npcY = getY();

     long curtime = System.currentTimeMillis() / 1000L;
     if (player.getNpcActionTime() + 2L > curtime) {
       return;
     }
     player.setNpcActionTime(curtime);

     if (getNpcTemplate().getChangeHead()) {
       if (pcX == npcX && pcY < npcY) {
         setHeading(0);
       } else if (pcX > npcX && pcY < npcY) {
         setHeading(1);
       } else if (pcX > npcX && pcY == npcY) {
         setHeading(2);
       } else if (pcX > npcX && pcY > npcY) {
         setHeading(3);
       } else if (pcX == npcX && pcY > npcY) {
         setHeading(4);
       } else if (pcX < npcX && pcY > npcY) {
         setHeading(5);
       } else if (pcX < npcX && pcY == npcY) {
         setHeading(6);
       } else if (pcX < npcX && pcY < npcY) {
         setHeading(7);
       }
       broadcastPacket((ServerBasePacket)new S_ChangeHeading(this));

       if (this._restCallCount.getAndIncrement() == 0) {
         setRest(true);
       }

       GeneralThreadPool.getInstance().schedule(new RestMonitor(), 10000L);
     }

     L1Object obj = L1World.getInstance().findObject(objid);

     if (talking != null) {
       L1NpcInstance npc; int time; boolean hascastle; boolean hascastle1; boolean hascastle2; boolean hascastle3; boolean hascastle4; boolean hascastle5; boolean hascastle6; boolean hascastle7; int level5; int karmaLevel; int karmaLevel1; switch (npcid) {
         case 50000069:
           try {
             player.sendPackets((ServerBasePacket)new S_ShopSellList(objid, player));
           } catch (l1j.server.server.serverpackets.S_ShopSellList.MinigameSellListException e) {
             e.printStackTrace();
           }
           break;
         case 7310085:
           if (player.getLevel() > 30 && player.getLevel() < 70) {
             htmlid = "talkinggate1"; break;
           }
           htmlid = "talkinggate2";
           break;

         case 5162:
           if (player.getInventory().checkItem(3000400)) {
             htmlid = "slots"; break;
           }  if (player.getInventory().checkItem(3000401)) {
             htmlid = "slotr"; break;
           }  if (player.getInventory().checkItem(3000402))
             htmlid = "slotb";
           break;
         case 70005:
           htmlid = "pago";
           break;






         case 7320101:
           player.start_teleport(33439, 32812, 4, player.getHeading(), 18339, true, false);
           break;
         case 7320215:
           if ((((player.getLevel() >= 1) ? 1 : 0) & ((player.getLevel() <= 55) ? 1 : 0)) != 0 &&
             player.getLevel() < Config.ServerAdSetting.Expreturn) {
             player.add_exp_for_ready(
                 ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1L - player.get_exp() + (ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1L) / 1000L);
             player.setCurrentHp(player.getMaxHp());
             player.setCurrentMp(player.getMaxMp());
           }
           break;

         case 70841:
           if (player.isElf()) {
             htmlid = "luudielE1"; break;
           }  if (player.isDarkelf()) {
             htmlid = "luudielCE1"; break;
           }
           htmlid = "luudiel1";
           break;

         case 70522:
           if (player.isCrown()) {
             if (player.getLevel() >= 15) {
               int lv15_step = quest.get_step(1);
               if (lv15_step == 2 || lv15_step == 255) {
                 htmlid = "gunterp11"; break;
               }
               htmlid = "gunterp9";
               break;
             }
             htmlid = "gunterp12"; break;
           }
           if (player.isKnight()) {
             int lv30_step = quest.get_step(2);
             if (lv30_step == 0) {
               htmlid = "gunterk9"; break;
             }  if (lv30_step == 1) {
               htmlid = "gunterkE1"; break;
             }  if (lv30_step == 2) {
               htmlid = "gunterkE2"; break;
             }  if (lv30_step >= 3)
               htmlid = "gunterkE3";  break;
           }
           if (player.isElf()) {
             htmlid = "guntere1"; break;
           }  if (player.isWizard()) {
             htmlid = "gunterw1"; break;
           }  if (player.isDarkelf()) {
             htmlid = "gunterde1";
           }
           break;
         case 70653:
           if (player.isCrown()) {
             if (player.getLevel() >= 45 &&
               quest.isEnd(2)) {
               int lv45_step = quest.get_step(3);
               if (lv45_step == 255) {
                 htmlid = "masha4"; break;
               }  if (lv45_step >= 1) {
                 htmlid = "masha3"; break;
               }
               htmlid = "masha1";
             }
             break;
           }
           if (player.isKnight()) {
             if (player.getLevel() >= 45 &&
               quest.isEnd(2)) {
               int lv45_step = quest.get_step(3);
               if (lv45_step == 255) {
                 htmlid = "mashak3"; break;
               }  if (lv45_step == 0) {
                 htmlid = "mashak1"; break;
               }  if (lv45_step >= 1)
                 htmlid = "mashak2";
             }
             break;
           }
           if (player.isElf() &&
             player.getLevel() >= 45 &&
             quest.isEnd(2)) {
             int lv45_step = quest.get_step(3);
             if (lv45_step == 255) {
               htmlid = "mashae3"; break;
             }  if (lv45_step >= 1) {
               htmlid = "mashae2"; break;
             }
             htmlid = "mashae1";
           }
           break;



         case 70554:
           if (player.isCrown()) {
             if (player.getLevel() >= 15) {
               int lv15_step = quest.get_step(1);
               if (lv15_step == 1) {
                 htmlid = "zero5"; break;
               }  if (lv15_step == 255) {
                 htmlid = "zero1"; break;
               }
               htmlid = "zero1";
               break;
             }
             htmlid = "zero6";
           }
           break;

             case 522:
                 if (player.getInventory().checkItem(40308, 200000)) {
                     player.getInventory().consumeItem(40308, 200000);
                     if (Config.ServerAdSetting.PolyEvent1) {
                         L1PolyMorph.doPoly(player, 7925, 600, 1, false, false);
                     }
                     L1SkillUse aa = new L1SkillUse();
                     aa.handleCommands(player, 5934, player.getId(), player.getX(), player.getY(), null, 0, 4);
                     Broadcaster.broadcastPacket(player, (ServerBasePacket)new S_SkillSound(player.getId(), 9009));
                     player.sendPackets((ServerBasePacket)new S_SkillSound(player.getId(), 9009));
                     htmlid = "stonescroll"; break;
                 }
                 player.sendPackets("金幣不足。");
                 break;

         case 70783:
           if (player.isCrown() &&
             player.getLevel() >= 30 &&
             quest.isEnd(1)) {
             int lv30_step = quest.get_step(2);
             if (lv30_step == 255) {
               htmlid = "aria3"; break;
             }  if (lv30_step == 1) {
               htmlid = "aria2"; break;
             }
             htmlid = "aria1";
           }
           break;



         case 70000:
           if (player.getLevel() < 52) {
             htmlid = "marbinquestA"; break;
           }
           if (player.getInventory().checkItem(700012, 1)) {
             htmlid = "marbinquest3"; break;
           }
           htmlid = "marbinquest1";
           break;


         case 70782:
           if (player.getCurrentSpriteId() == 1037) {
             if (player.isCrown()) {
               if (quest.get_step(2) == 1) {
                 htmlid = "ant1"; break;
               }
               htmlid = "ant3";
               break;
             }
             htmlid = "ant3";
           }
           break;

         case 70545:
           if (player.isCrown()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step >= 1 && lv45_step != 255) {
               if (player.getInventory().checkItem(40586)) {
                 htmlid = "richard4"; break;
               }
               htmlid = "richard1";
             }
           }
           break;

         case 70776:
           if (player.isCrown()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step == 1) {
               htmlid = "meg1"; break;
             }  if (lv45_step == 2 && lv45_step <= 3) {
               htmlid = "meg2"; break;
             }  if (lv45_step >= 4) {
               htmlid = "meg3";
             }
           }
           break;
         case 71200:
           if (player.isCrown()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step == 2 && player.getInventory().checkItem(41422)) {
               player.getInventory().consumeItem(41422, 1);
               int[] item_ids = { 40568 };
               int[] item_amounts = { 1 };
               for (int i = 0; i < item_ids.length; i++) {
                 player.getInventory().storeItem(item_ids[i], item_amounts[i]);
               }
             }
           }
           break;
         case 70802:
           if (player.isKnight() &&
             player.getLevel() >= 15) {
             int lv15_step = quest.get_step(1);
             if (lv15_step == 255) {
               htmlid = "aanon7"; break;
             }  if (lv15_step == 1) {
               htmlid = "aanon4";
             }
           }
           break;

         case 70775:
           if (player.isKnight() &&
             player.getLevel() >= 30 &&
             quest.isEnd(1)) {
             int lv30_step = quest.get_step(2);
             if (lv30_step == 0) {
               htmlid = "mark1"; break;
             }
             htmlid = "mark2";
           }
           break;



         case 70794:
           if (player.isCrown()) {
             htmlid = "gerardp1";
           } else if (player.isKnight()) {
             int lv30_step = quest.get_step(2);
             if (lv30_step == 255) {
               htmlid = "gerardkEcg";
             } else if (lv30_step < 3) {
               htmlid = "gerardk7";
             } else if (lv30_step == 3) {
               htmlid = "gerardkE1";
             } else if (lv30_step == 4) {
               htmlid = "gerardkE2";
             } else if (lv30_step == 5) {
               htmlid = "gerardkE3";
             } else if (lv30_step >= 6) {
               htmlid = "gerardkE4";
             }
           } else if (player.isElf()) {
             htmlid = "gerarde1";
           } else if (player.isWizard()) {
             htmlid = "gerardw1";
           } else if (player.isDarkelf()) {
             htmlid = "gerardde1";
           }
         case 70555:
           if (player.getCurrentSpriteId() == 2374) {
             if (player.isKnight()) {
               if (quest.get_step(2) == 6) {
                 htmlid = "jim2"; break;
               }
               htmlid = "jim4";
               break;
             }
             htmlid = "jim4";
           }
           break;

         case 70715:
           if (player.isKnight()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step == 1) {
               htmlid = "jimuk1"; break;
             }  if (lv45_step >= 2) {
               htmlid = "jimuk2";
             }
           }
           break;
         case 9291:
         case 70760:
         case 460000151:
           if (player.isElf()) {
             if (player.isPassive(MJPassiveID.GLORY_EARTH.toInt()) && player.getGlory_Earth_Attr() == 0) {
               if (player.getElfAttr() != 0) {
                 htmlid = "ellyonne11"; break;
               }
               htmlid = "ellyonne17";
               break;
             }
             if (player.getElfAttr() != 0) {
               htmlid = "ellyonne11"; break;
             }
             htmlid = "ellyonne4";

             break;
           }
           htmlid = "ellyonne15";
           break;

         case 70711:
           if (player.isKnight()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step == 2) {
               if (player.getInventory().checkItem(20026))
                 htmlid = "giantk1";  break;
             }
             if (lv45_step == 3) {
               htmlid = "giantk2"; break;
             }  if (lv45_step >= 4) {
               htmlid = "giantk3";
             }
           }
           break;
         case 70826:
           if (player.isElf()) {
             if (player.getLevel() >= 15) {
               if (quest.isEnd(1)) {
                 htmlid = "oth5"; break;
               }
               htmlid = "oth1";
               break;
             }
             htmlid = "oth6";
           }
           break;

         case 70844:
           if (player.isElf()) {
             if (player.getLevel() >= 30) {
               if (quest.isEnd(1)) {
                 int lv30_step = quest.get_step(2);
                 if (lv30_step == 255) {
                   htmlid = "motherEE3"; break;
                 }  if (lv30_step >= 1) {
                   htmlid = "motherEE2"; break;
                 }  if (lv30_step <= 0)
                   htmlid = "motherEE1";
                 break;
               }
               htmlid = "mothere1";
               break;
             }
             htmlid = "mothere1";
           }
           break;

         case 70724:
           if (player.isElf()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step >= 4) {
               htmlid = "heit5"; break;
             }  if (lv45_step >= 3) {
               htmlid = "heit3"; break;
             }  if (lv45_step >= 2) {
               htmlid = "heit2"; break;
             }  if (lv45_step >= 1) {
               htmlid = "heit1";
             }
           }
           break;
         case 70531:
           if (player.isWizard() &&
             player.getLevel() >= 15) {
             if (quest.isEnd(1)) {
               htmlid = "jem6"; break;
             }
             htmlid = "jem1";
           }
           break;


         case 70009:
           htmlid = "gereng01";
           break;
         case 81105:
           if (player.isWizard()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step >= 3) {
               htmlid = "stoenm3"; break;
             }  if (lv45_step >= 2) {
               htmlid = "stoenm2"; break;
             }  if (lv45_step >= 1) {
               htmlid = "stoenm1";
             }
           }
           break;
         case 70739:
           if (player.getLevel() >= 50) {
             int lv50_step = quest.get_step(4);
             if (lv50_step == 255) {
               if (player.isCrown()) {
                 htmlid = "dicardingp3"; break;
               }  if (player.isKnight()) {
                 htmlid = "dicardingk3"; break;
               }  if (player.isElf()) {
                 htmlid = "dicardinge3"; break;
               }  if (player.isWizard()) {
                 htmlid = "dicardingw3"; break;
               }  if (player.isDarkelf())
                 htmlid = "dicarding";  break;
             }
             if (lv50_step >= 1) {
               if (player.isCrown()) {
                 htmlid = "dicardingp2"; break;
               }  if (player.isKnight()) {
                 htmlid = "dicardingk2"; break;
               }  if (player.isElf()) {
                 htmlid = "dicardinge2"; break;
               }  if (player.isWizard()) {
                 htmlid = "dicardingw2"; break;
               }  if (player.isDarkelf())
                 htmlid = "dicarding";  break;
             }
             if (lv50_step >= 0) {
               if (player.isCrown()) {
                 htmlid = "dicardingp1"; break;
               }  if (player.isKnight()) {
                 htmlid = "dicardingk1"; break;
               }  if (player.isElf()) {
                 htmlid = "dicardinge1"; break;
               }  if (player.isWizard()) {
                 htmlid = "dicardingw1"; break;
               }  if (player.isDarkelf())
                 htmlid = "dicarding";
               break;
             }
             htmlid = "dicarding";
             break;
           }
           htmlid = "dicarding";
           break;

         case 70885:
           if (player.isDarkelf()) {
             if (player.getLevel() >= 15) {
               int lv15_step = quest.get_step(1);
               if (lv15_step == 255) {
                 htmlid = "kanguard3"; break;
               }  if (lv15_step >= 1) {
                 htmlid = "kanguard2"; break;
               }
               htmlid = "kanguard1";
               break;
             }
             htmlid = "kanguard5";
           }
           break;

         case 70892:
           if (player.isDarkelf()) {
             if (player.getLevel() >= 30) {
               if (quest.isEnd(1)) {
                 int lv30_step = quest.get_step(2);
                 if (lv30_step == 255) {
                   htmlid = "ronde5"; break;
                 }  if (lv30_step >= 2) {
                   htmlid = "ronde3"; break;
                 }  if (lv30_step >= 1) {
                   htmlid = "ronde2"; break;
                 }
                 htmlid = "ronde1";
                 break;
               }
               htmlid = "ronde7";
               break;
             }
             htmlid = "ronde7";
           }
           break;

             case 70895:
                 npc = (L1NpcInstance)obj;
                 player.sendPackets((ServerBasePacket)new S_NpcChatPacket(npc, "雙擊完成的拉斯塔巴德歷史書就可以了，但你硬要來找我……有什麼理由嗎...?", 0));
                 if (player.isDarkelf()) {
                     if (player.getLevel() >= 45) {
                         if (quest.isEnd(2)) {
                             int lv45_step = quest.get_step(3);
                             if (lv45_step == 255) {
                                 if (player.getLevel() < 50) {
                                     htmlid = "bluedikaq3"; break;
                                 }
                   int lv50_step = quest.get_step(4);
                   if (lv50_step == 255) {
                     htmlid = "bluedikaq8"; break;
                   }
                   htmlid = "bluedikaq6";
                   break;
                 }
                 if (lv45_step >= 1) {
                   htmlid = "bluedikaq2"; break;
                 }
                 htmlid = "bluedikaq1";
                 break;
               }
               htmlid = "bluedikaq5";
               break;
             }
             htmlid = "bluedikaq5";
           }
           break;

         case 70904:
           if (player.isDarkelf() &&
             quest.get_step(3) == 1) {
             htmlid = "koup12";
           }
           break;

         case 70824:
           if (player.isDarkelf()) {
             if (player.getCurrentSpriteId() == 3634) {
               int lv45_step = quest.get_step(3);
               if (lv45_step == 1) {
                 htmlid = "assassin1"; break;
               }  if (lv45_step == 2) {
                 htmlid = "assassin2"; break;
               }
               htmlid = "assassin3";
               break;
             }
             htmlid = "assassin3";
           }
           break;

         case 70744:
           if (player.isDarkelf()) {
             int lv45_step = quest.get_step(3);
             if (lv45_step >= 5) {
               htmlid = "roje14"; break;
             }  if (lv45_step >= 4) {
               htmlid = "roje13"; break;
             }  if (lv45_step >= 3) {
               htmlid = "roje12"; break;
             }  if (lv45_step >= 2) {
               htmlid = "roje11"; break;
             }
             htmlid = "roje15";
           }
           break;

         case 3000003:
           if (player.isDragonknight()) {
             if (player.getLevel() >= 15) {
               int lv15_step = quest.get_step(1);
               if (lv15_step == 1) {
                 htmlid = "prokel4"; break;
               }  if (lv15_step == 2 || lv15_step == 255) {
                 htmlid = "prokel7"; break;
               }
               htmlid = "prokel2";
               break;
             }
             htmlid = "prokel1";
           }
           break;

         case 3100004:
           if (player.isBlackwizard()) {
             if (player.getLevel() >= 15) {
               int lv15_step = quest.get_step(1);
               if (lv15_step == 1) {
                 htmlid = "silrein4"; break;
               }  if (lv15_step == 2 || lv15_step == 255) {
                 htmlid = "silrein5"; break;
               }
               htmlid = "silrein2";
               break;
             }
             htmlid = "prokel1";
           }
           break;

         case 70087:
           if (player.isDarkelf()) {
             htmlid = "sedia";
           }
           break;
         case 70099:
           if (!quest.isEnd(11) &&
             player.getLevel() > 13) {
             htmlid = "kuper1";
           }
           break;

         case 70796:
           if (!quest.isEnd(11) &&
             player.getLevel() > 13) {
             htmlid = "dunham1";
           }
           break;

         case 70011:
           time = GameTimeClock.getInstance().getGameTime().getSeconds() % 86400;
           if (time < 21600 || time > 72000) {
             htmlid = "shipEvI6";
           }
           break;
         case 70553:
           hascastle = checkHasCastle(player, 1);
           if (hascastle) {
             if (checkClanLeader(player)) {
               htmlid = "ishmael1"; break;
             }  if (player.getClanRank() == 9) {
               htmlid = "ishmael1";
               htmldata = new String[] { player.getName() }; break;
             }
             htmlid = "ishmael6";
             break;
           }
           htmlid = "ishmael7";
           break;

         case 70822:
           hascastle1 = checkHasCastle(player, 2);
           if (hascastle1) {
             if (checkClanLeader(player)) {
               htmlid = "seghem1"; break;
             }
             htmlid = "seghem6";
             htmldata = new String[] { player.getName() };
             break;
           }
           htmlid = "seghem7";
           break;

         case 70784:
           hascastle2 = checkHasCastle(player, 3);
           if (hascastle2) {
             if (checkClanLeader(player)) {
               htmlid = "othmond1"; break;
             }
             htmlid = "othmond6";
             htmldata = new String[] { player.getName() };
             break;
           }
           htmlid = "othmond7";
           break;

         case 70623:
           hascastle3 = checkHasCastle(player, 4);
           if (hascastle3) {
             if (checkClanLeader(player)) {
               htmlid = "orville1"; break;
             }  if (player.getClanRank() == 9) {
               htmlid = "orville1";
               htmldata = new String[] { player.getName() }; break;
             }
             htmlid = "orville6";
             break;
           }
           htmlid = "orville7";
           break;

         case 70880:
           hascastle4 = checkHasCastle(player, 5);
           if (hascastle4) {
             if (checkClanLeader(player)) {
               htmlid = "fisher1"; break;
             }
             htmlid = "fisher6";
             htmldata = new String[] { player.getName() };
             break;
           }
           htmlid = "fisher7";
           break;

         case 70665:
           hascastle5 = checkHasCastle(player, 6);
           if (hascastle5) {
             if (checkClanLeader(player)) {
               htmlid = "potempin1"; break;
             }
             htmlid = "potempin6";
             htmldata = new String[] { player.getName() };
             break;
           }
           htmlid = "potempin7";
           break;

         case 70721:
           hascastle6 = checkHasCastle(player, 7);
           if (hascastle6) {
             if (checkClanLeader(player)) {
               htmlid = "timon1"; break;
             }
             htmlid = "timon6";
             htmldata = new String[] { player.getName() };
             break;
           }
           htmlid = "timon7";
           break;

         case 81155:
           hascastle7 = checkHasCastle(player, 8);
           if (hascastle7) {
             if (checkClanLeader(player)) {
               htmlid = "olle1"; break;
             }
             htmlid = "olle6";
             htmldata = new String[] { player.getName() };
             break;
           }
           htmlid = "olle7";
           break;

         case 80057:
           switch (player.getKarmaLevel()) {
             case 0:
               htmlid = "alfons1";
               break;
             case -1:
               htmlid = "cyk1";
               break;
             case -2:
               htmlid = "cyk2";
               break;
             case -3:
               htmlid = "cyk3";
               break;
             case -4:
               htmlid = "cyk4";
               break;
             case -5:
               htmlid = "cyk5";
               break;
             case -6:
               htmlid = "cyk6";
               break;
             case -7:
               htmlid = "cyk7";
               break;
             case -8:
               htmlid = "cyk8";
               break;
             case 1:
               htmlid = "cbk1";
               break;
             case 2:
               htmlid = "cbk2";
               break;
             case 3:
               htmlid = "cbk3";
               break;
             case 4:
               htmlid = "cbk4";
               break;
             case 5:
               htmlid = "cbk5";
               break;
             case 6:
               htmlid = "cbk6";
               break;
             case 7:
               htmlid = "cbk7";
               break;
             case 8:
               htmlid = "cbk8";
               break;
           }
           htmlid = "alfons1";
           break;


         case 80058:
           level5 = player.getLevel();
           if (level5 <= 44) {
             htmlid = "cpass03"; break;
           }  if (level5 <= 51 && 45 <= level5) {
             htmlid = "cpass02"; break;
           }
           htmlid = "cpass01";
           break;

         case 80059:
           if (player.getKarmaLevel() >= 3) {
             htmlid = "cpass03"; break;
           }  if (player.getInventory().checkItem(40921)) {
             htmlid = "wpass02"; break;
           }  if (player.getInventory().checkItem(40917)) {
             htmlid = "wpass14"; break;
           }  if (player.getInventory().checkItem(40912) || player.getInventory().checkItem(40910) || player
             .getInventory().checkItem(40911)) {
             htmlid = "wpass04"; break;
           }  if (player.getInventory().checkItem(40909)) {
             int count = getNecessarySealCount(player);
             if (player.getInventory().checkItem(40913, count)) {
               createRuler(player, 1, count);
               htmlid = "wpass06"; break;
             }
             htmlid = "wpass03"; break;
           }
           if (player.getInventory().checkItem(40913)) {
             htmlid = "wpass08"; break;
           }
           htmlid = "wpass05";
           break;

         case 80060:
           if (player.getKarmaLevel() >= 3) {
             htmlid = "cpass03"; break;
           }  if (player.getInventory().checkItem(40921)) {
             htmlid = "wpass02"; break;
           }  if (player.getInventory().checkItem(40920)) {
             htmlid = "wpass13"; break;
           }  if (player.getInventory().checkItem(40909) || player.getInventory().checkItem(40910) || player
             .getInventory().checkItem(40911)) {
             htmlid = "wpass04"; break;
           }  if (player.getInventory().checkItem(40912)) {
             int count = getNecessarySealCount(player);
             if (player.getInventory().checkItem(40916, count)) {
               createRuler(player, 8, count);
               htmlid = "wpass06"; break;
             }
             htmlid = "wpass03"; break;
           }
           if (player.getInventory().checkItem(40916)) {
             htmlid = "wpass08"; break;
           }
           htmlid = "wpass05";
           break;

         case 80061:
           if (player.getKarmaLevel() >= 3) {
             htmlid = "cpass03"; break;
           }  if (player.getInventory().checkItem(40921)) {
             htmlid = "wpass02"; break;
           }  if (player.getInventory().checkItem(40918)) {
             htmlid = "wpass11"; break;
           }  if (player.getInventory().checkItem(40909) || player.getInventory().checkItem(40912) || player
             .getInventory().checkItem(40911)) {
             htmlid = "wpass04"; break;
           }  if (player.getInventory().checkItem(40910)) {
             int count = getNecessarySealCount(player);
             if (player.getInventory().checkItem(40914, count)) {
               createRuler(player, 4, count);
               htmlid = "wpass06"; break;
             }
             htmlid = "wpass03"; break;
           }
           if (player.getInventory().checkItem(40914)) {
             htmlid = "wpass08"; break;
           }
           htmlid = "wpass05";
           break;

         case 80062:
           if (player.getKarmaLevel() >= 3) {
             htmlid = "cpass03"; break;
           }  if (player.getInventory().checkItem(40921)) {
             htmlid = "wpass02"; break;
           }  if (player.getInventory().checkItem(40919)) {
             htmlid = "wpass12"; break;
           }  if (player.getInventory().checkItem(40909) || player.getInventory().checkItem(40912) || player
             .getInventory().checkItem(40910)) {
             htmlid = "wpass04"; break;
           }  if (player.getInventory().checkItem(40911)) {
             int count = getNecessarySealCount(player);
             if (player.getInventory().checkItem(40915, count)) {
               createRuler(player, 2, count);
               htmlid = "wpass06"; break;
             }
             htmlid = "wpass03"; break;
           }
           if (player.getInventory().checkItem(40915)) {
             htmlid = "wpass08"; break;
           }
           htmlid = "wpass05";
           break;

         case 80065:
           if (player.getKarmaLevel() < 3) {
             htmlid = "uturn0"; break;
           }
           htmlid = "uturn1";
           break;

         case 80047:
           if (player.getKarmaLevel() > -3) {
             htmlid = "uhelp1"; break;
           }
           htmlid = "uhelp2";
           break;

         case 80049:
           if (player.getKarma() <= -10000000) {
             htmlid = "betray11"; break;
           }
           htmlid = "betray12";
           break;

         case 80050:
           if (player.getKarmaLevel() > -1) {
             htmlid = "meet103"; break;
           }
           htmlid = "meet101";
           break;

         case 80053:
           karmaLevel = player.getKarmaLevel();
           if (karmaLevel == 0) {
             htmlid = "aliceyet"; break;
           }  if (karmaLevel >= 1) {
             if (player.getInventory().checkItem(196) || player.getInventory().checkItem(197) || player
               .getInventory().checkItem(198) || player.getInventory().checkItem(199) || player
               .getInventory().checkItem(200) || player.getInventory().checkItem(201) || player
               .getInventory().checkItem(202) || player.getInventory().checkItem(203)) {
               htmlid = "alice_gd"; break;
             }
             htmlid = "gd"; break;
           }
           if (karmaLevel <= -1) {
             if (player.getInventory().checkItem(40991)) {
               if (karmaLevel <= -1)
                 htmlid = "Mate_1";  break;
             }
             if (player.getInventory().checkItem(196)) {
               if (karmaLevel <= -2) {
                 htmlid = "Mate_2"; break;
               }
               htmlid = "alice_1"; break;
             }
             if (player.getInventory().checkItem(197)) {
               if (karmaLevel <= -3) {
                 htmlid = "Mate_3"; break;
               }
               htmlid = "alice_2"; break;
             }
             if (player.getInventory().checkItem(198)) {
               if (karmaLevel <= -4) {
                 htmlid = "Mate_4"; break;
               }
               htmlid = "alice_3"; break;
             }
             if (player.getInventory().checkItem(199)) {
               if (karmaLevel <= -5) {
                 htmlid = "Mate_5"; break;
               }
               htmlid = "alice_4"; break;
             }
             if (player.getInventory().checkItem(200)) {
               if (karmaLevel <= -6) {
                 htmlid = "Mate_6"; break;
               }
               htmlid = "alice_5"; break;
             }
             if (player.getInventory().checkItem(201)) {
               if (karmaLevel <= -7) {
                 htmlid = "Mate_7"; break;
               }
               htmlid = "alice_6"; break;
             }
             if (player.getInventory().checkItem(202)) {
               if (karmaLevel <= -8) {
                 htmlid = "Mate_8"; break;
               }
               htmlid = "alice_7"; break;
             }
             if (player.getInventory().checkItem(203)) {
               htmlid = "alice_8"; break;
             }
             htmlid = "alice_no";
           }
           break;

         case 80056:
           if (player.getKarma() <= -10000000) {
             htmlid = "infamous11"; break;
           }
           htmlid = "infamous12";
           break;

         case 80064:
           if (player.getKarmaLevel() < 1) {
             htmlid = "meet003"; break;
           }
           htmlid = "meet001";
           break;

         case 80066:
           if (player.getKarma() >= 10000000) {
             htmlid = "betray01"; break;
           }
           htmlid = "betray02";
           break;

         case 80072:
           karmaLevel1 = player.getKarmaLevel();
           if (karmaLevel1 == 1) {
             htmlid = "lsmith0"; break;
           }  if (karmaLevel1 == 2) {
             htmlid = "lsmith1"; break;
           }  if (karmaLevel1 == 3) {
             htmlid = "lsmith2"; break;
           }  if (karmaLevel1 == 4) {
             htmlid = "lsmith3"; break;
           }  if (karmaLevel1 == 5) {
             htmlid = "lsmith4"; break;
           }  if (karmaLevel1 == 6) {
             htmlid = "lsmith5"; break;
           }  if (karmaLevel1 == 7) {
             htmlid = "lsmith7"; break;
           }  if (karmaLevel1 == 8) {
             htmlid = "lsmith8"; break;
           }
           htmlid = "";
           break;

         case 80074:
           if (player.getKarma() >= 10000000) {
             htmlid = "infamous01"; break;
           }
           htmlid = "infamous02";
           break;

         case 70528:
           htmlid = talkToTownmaster(player, 1);
           break;
         case 70546:
           htmlid = talkToTownmaster(player, 6);
           break;
         case 70567:
           htmlid = talkToTownmaster(player, 3);
           break;
         case 70815:
           htmlid = talkToTownmaster(player, 4);
           break;
         case 70774:
           htmlid = talkToTownmaster(player, 5);
           break;
         case 70799:
           htmlid = talkToTownmaster(player, 2);
           break;
         case 70594:
           htmlid = talkToTownmaster(player, 7);
           break;
         case 70860:
           htmlid = talkToTownmaster(player, 8);
           break;
         case 70654:
           htmlid = talkToTownmaster(player, 9);
           break;
         case 70748:
           htmlid = talkToTownmaster(player, 10);
           break;
         case 70534:
           htmlid = talkToTownadviser(player, 1);
           break;
         case 70556:
           htmlid = talkToTownadviser(player, 6);
           break;
         case 70572:
           htmlid = talkToTownadviser(player, 3);
           break;
         case 70830:
           htmlid = talkToTownadviser(player, 4);
           break;
         case 70788:
           htmlid = talkToTownadviser(player, 5);
           break;
         case 70806:
           htmlid = talkToTownadviser(player, 2);
           break;
         case 70631:
           htmlid = talkToTownadviser(player, 7);
           break;
         case 70876:
           htmlid = talkToTownadviser(player, 8);
           break;
         case 70663:
           htmlid = talkToTownadviser(player, 9);
           break;
         case 70761:
           htmlid = talkToTownadviser(player, 10);
           break;
         case 70998:
           htmlid = talkToSIGuide(player);
           break;
         case 71005:
           htmlid = talkToPopirea(player);
           break;
         case 71013:
           if (player.isDarkelf()) {
             if (player.getLevel() <= 3) {
               htmlid = "karen1"; break;
             }  if (player.getLevel() > 3 && player.getLevel() < 50) {
               htmlid = "karen3"; break;
             }  if (player.getLevel() >= 50) {
               htmlid = "karen4";
             }
           }
           break;
         case 71031:
           if (player.getLevel() < 25) {
             htmlid = "en0081";
           }
           break;
         case 71021:
           if (player.getLevel() < 12) {
             htmlid = "en0197"; break;
           }  if (player.getLevel() >= 12 && player.getLevel() < 25) {
             htmlid = "en0191";
           }
           break;
         case 71022:
           if (player.getLevel() < 12) {
             htmlid = "jpe0155"; break;
           }  if (player.getLevel() >= 12 && player.getLevel() < 25 && (
             player.getInventory().checkItem(41230) || player.getInventory().checkItem(41231) || player
             .getInventory().checkItem(41232) || player.getInventory().checkItem(41233) || player
             .getInventory().checkItem(41235) || player.getInventory().checkItem(41238) || player
             .getInventory().checkItem(41239) || player.getInventory().checkItem(41240))) {
             htmlid = "jpe0158";
           }
           break;

         case 71023:
           if (player.getLevel() < 12) {
             htmlid = "jpe0145"; break;
           }  if (player.getLevel() >= 12 && player.getLevel() < 25) {
             if (player.getInventory().checkItem(41233) || player.getInventory().checkItem(41234)) {
               htmlid = "jpe0143"; break;
             }  if (player.getInventory().checkItem(41238) || player.getInventory().checkItem(41239) || player
               .getInventory().checkItem(41240)) {
               htmlid = "jpe0147"; break;
             }  if (player.getInventory().checkItem(41235) || player.getInventory().checkItem(41236) || player
               .getInventory().checkItem(41237)) {
               htmlid = "jpe0144";
             }
           }
           break;
         case 71020:
           if (player.getLevel() < 12) {
             htmlid = "jpe0125"; break;
           }  if (player.getLevel() >= 12 && player.getLevel() < 25) {
             if (player.getInventory().checkItem(41231)) {
               htmlid = "jpe0123"; break;
             }  if (player.getInventory().checkItem(41232) || player.getInventory().checkItem(41233) || player
               .getInventory().checkItem(41234) || player.getInventory().checkItem(41235) || player
               .getInventory().checkItem(41238) || player.getInventory().checkItem(41239) || player
               .getInventory().checkItem(41240)) {
               htmlid = "jpe0126";
             }
           }
           break;
         case 71019:
           if (player.getLevel() < 12) {
             htmlid = "jpe0114"; break;
           }  if (player.getLevel() >= 12 && player.getLevel() < 25) {
             if (player.getInventory().checkItem(41239)) {
               htmlid = "jpe0113"; break;
             }
             htmlid = "jpe0111";
           }
           break;

         case 71018:
           if (player.getLevel() < 12) {
             htmlid = "jpe0133"; break;
           }  if (player.getLevel() >= 12 && player.getLevel() < 25) {
             if (player.getInventory().checkItem(41240)) {
               htmlid = "jpe0132"; break;
             }
             htmlid = "jpe0131";
           }
           break;

         case 71025:
           if (player.getLevel() < 10) {
             htmlid = "jpe0086"; break;
           }  if (player.getLevel() >= 10 && player.getLevel() < 25) {
             if (player.getInventory().checkItem(41226)) {
               htmlid = "jpe0084"; break;
             }  if (player.getInventory().checkItem(41225)) {
               htmlid = "jpe0083"; break;
             }  if (player.getInventory().checkItem(40653) || player.getInventory().checkItem(40613)) {
               htmlid = "jpe0081";
             }
           }
           break;
         case 70041:
           htmlid = "maeno1";
           break;

         case 70042:
         case 8502074:
           if (BugRaceController.getInstance().getBugState() == 0) {
             htmlid = "maeno1"; break;
           }  if (BugRaceController.getInstance().getBugState() == 1) {
             htmlid = "maeno3"; break;
           }  if (BugRaceController.getInstance().getBugState() == 2) {
             htmlid = "maeno5";
           }
           break;



         case 71038:
           if (player.getInventory().checkItem(41060)) {
             if (player.getInventory().checkItem(41090) || player.getInventory().checkItem(41091) || player
               .getInventory().checkItem(41092)) {
               htmlid = "orcfnoname7"; break;
             }
             htmlid = "orcfnoname8";
             break;
           }
           htmlid = "orcfnoname1";
           break;

         case 71040:
           if (player.getInventory().checkItem(41060)) {
             if (player.getInventory().checkItem(41065)) {
               if (player.getInventory().checkItem(41086) || player.getInventory().checkItem(41087) || player
                 .getInventory().checkItem(41088) || player.getInventory().checkItem(41089)) {
                 htmlid = "orcfnoa6"; break;
               }
               htmlid = "orcfnoa5";
               break;
             }
             htmlid = "orcfnoa2";
             break;
           }
           htmlid = "orcfnoa1";
           break;

         case 71041:
           if (player.getInventory().checkItem(41060)) {
             if (player.getInventory().checkItem(41064)) {
               if (player.getInventory().checkItem(41081) || player.getInventory().checkItem(41082) || player
                 .getInventory().checkItem(41083) || player.getInventory().checkItem(41084) || player
                 .getInventory().checkItem(41085)) {
                 htmlid = "orcfhuwoomo2"; break;
               }
               htmlid = "orcfhuwoomo8";
               break;
             }
             htmlid = "orcfhuwoomo1";
             break;
           }
           htmlid = "orcfhuwoomo5";
           break;

         case 71042:
           if (player.getInventory().checkItem(41060)) {
             if (player.getInventory().checkItem(41062)) {
               if (player.getInventory().checkItem(41071) || player.getInventory().checkItem(41072) || player
                 .getInventory().checkItem(41073) || player.getInventory().checkItem(41074) || player
                 .getInventory().checkItem(41075)) {
                 htmlid = "orcfbakumo2"; break;
               }
               htmlid = "orcfbakumo8";
               break;
             }
             htmlid = "orcfbakumo1";
             break;
           }
           htmlid = "orcfbakumo5";
           break;

         case 71043:
           if (player.getInventory().checkItem(41060)) {
             if (player.getInventory().checkItem(41063)) {
               if (player.getInventory().checkItem(41076) || player.getInventory().checkItem(41077) || player
                 .getInventory().checkItem(41078) || player.getInventory().checkItem(41079) || player
                 .getInventory().checkItem(41080)) {
                 htmlid = "orcfbuka2"; break;
               }
               htmlid = "orcfbuka8";
               break;
             }
             htmlid = "orcfbuka1";
             break;
           }
           htmlid = "orcfbuka5";
           break;

         case 71044:
           if (player.getInventory().checkItem(41060)) {
             if (player.getInventory().checkItem(41061)) {
               if (player.getInventory().checkItem(41066) || player.getInventory().checkItem(41067) || player
                 .getInventory().checkItem(41068) || player.getInventory().checkItem(41069) || player
                 .getInventory().checkItem(41070)) {
                 htmlid = "orcfkame2"; break;
               }
               htmlid = "orcfkame8";
               break;
             }
             htmlid = "orcfkame1";
             break;
           }
           htmlid = "orcfkame5";
           break;

         case 71055:
           if (player.getQuest().get_step(30) == 3) {
             htmlid = "lukein13"; break;
           }  if (player.getQuest().get_step(23) == 255 && player
             .getQuest().get_step(30) == 2 && player
             .getInventory().checkItem(40631)) {
             htmlid = "lukein10"; break;
           }  if (player.getQuest().get_step(23) == 255) {
             htmlid = "lukein0"; break;
           }  if (player.getQuest().get_step(23) == 11) {
             if (player.getInventory().checkItem(40716))
               htmlid = "lukein9";  break;
           }
           if (player.getQuest().get_step(23) >= 1 && player
             .getQuest().get_step(23) <= 10) {
             htmlid = "lukein8";
           }
           break;
         case 71063:
           if (player.getQuest().get_step(24) != 255 &&
             player.getQuest().get_step(23) == 1) {
             htmlid = "maptbox";
           }
           break;
         case 71064:
           if (player.getQuest().get_step(23) == 2) {
             htmlid = talkToSecondtbox(player);
           }
           break;
         case 71065:
           if (player.getQuest().get_step(23) == 3) {
             htmlid = talkToSecondtbox(player);
           }
           break;
         case 71066:
           if (player.getQuest().get_step(23) == 4) {
             htmlid = talkToSecondtbox(player);
           }
           break;
         case 71067:
           if (player.getQuest().get_step(23) == 5) {
             htmlid = talkToThirdtbox(player);
           }
           break;
         case 71068:
           if (player.getQuest().get_step(23) == 6) {
             htmlid = talkToThirdtbox(player);
           }
           break;
         case 71069:
           if (player.getQuest().get_step(23) == 7) {
             htmlid = talkToThirdtbox(player);
           }
           break;
         case 71070:
           if (player.getQuest().get_step(23) == 8) {
             htmlid = talkToThirdtbox(player);
           }
           break;
         case 71071:
           if (player.getQuest().get_step(23) == 9) {
             htmlid = talkToThirdtbox(player);
           }
           break;
         case 71072:
           if (player.getQuest().get_step(23) == 10) {
             htmlid = talkToThirdtbox(player);
           }
           break;
         case 71056:
           if (player.getQuest().get_step(30) == 4) {
             if (player.getInventory().checkItem(40631)) {
               htmlid = "SIMIZZ11"; break;
             }
             htmlid = "SIMIZZ0"; break;
           }
           if (player.getQuest().get_step(27) == 2) {
             htmlid = "SIMIZZ0"; break;
           }  if (player.getQuest().get_step(27) == 255) {
             htmlid = "SIMIZZ15"; break;
           }  if (player.getQuest().get_step(27) == 1) {
             htmlid = "SIMIZZ6";
           }
           break;
         case 71057:
           if (player.getQuest().get_step(28) == 255) {
             htmlid = "doil4b";
           }
           break;
         case 71059:
           if (player.getQuest().get_step(29) == 255) {
             htmlid = "rudian1c"; break;
           }  if (player.getQuest().get_step(29) == 1) {
             htmlid = "rudian7"; break;
           }  if (player.getQuest().get_step(28) == 255) {
             htmlid = "rudian1b"; break;
           }
           htmlid = "rudian1a";
           break;

         case 71060:
           if (player.getQuest().get_step(30) == 255) {
             htmlid = "resta1e"; break;
           }  if (player.getQuest().get_step(27) == 255) {
             htmlid = "resta14"; break;
           }  if (player.getQuest().get_step(30) == 4) {
             htmlid = "resta13"; break;
           }  if (player.getQuest().get_step(30) == 3) {
             htmlid = "resta11";
             player.getQuest().set_step(30, 4); break;
           }  if (player.getQuest().get_step(30) == 2) {
             htmlid = "resta16"; break;
           }  if ((player.getQuest().get_step(27) == 2 && player
             .getQuest().get_step(31) == 1) || player
             .getInventory().checkItem(40647)) {
             htmlid = "resta1a"; break;
           }  if (player.getQuest().get_step(31) == 1 || player
             .getInventory().checkItem(40647)) {
             htmlid = "resta1c"; break;
           }  if (player.getQuest().get_step(27) == 2) {
             htmlid = "resta1b";
           }
           break;
         case 71061:
           if (player.getQuest().get_step(31) == 255) {
             htmlid = "cadmus1c"; break;
           }  if (player.getQuest().get_step(31) == 3) {
             htmlid = "cadmus8"; break;
           }  if (player.getQuest().get_step(31) == 2) {
             htmlid = "cadmus1a"; break;
           }  if (player.getQuest().get_step(28) == 255) {
             htmlid = "cadmus1b";
           }
           break;
         case 71036:
           if (player.getQuest().get_step(32) == 255) {
             htmlid = "kamyla26"; break;
           }  if (player.getQuest().get_step(32) == 4 && player
             .getInventory().checkItem(40717)) {
             htmlid = "kamyla15"; break;
           }  if (player.getQuest().get_step(32) == 4) {
             htmlid = "kamyla14"; break;
           }  if (player.getQuest().get_step(32) == 3 && player
             .getInventory().checkItem(40630)) {
             htmlid = "kamyla12"; break;
           }  if (player.getQuest().get_step(32) == 3) {
             htmlid = "kamyla11"; break;
           }  if (player.getQuest().get_step(32) == 2 && player
             .getInventory().checkItem(40644)) {
             htmlid = "kamyla9"; break;
           }  if (player.getQuest().get_step(32) == 1) {
             htmlid = "kamyla8"; break;
           }  if (player.getQuest().get_step(31) == 255 && player
             .getInventory().checkItem(40621)) {
             htmlid = "kamyla1";
           }
           break;
         case 71089:
           if (player.getQuest().get_step(32) == 2) {
             htmlid = "francu12";
           }
           break;
         case 71090:
           if (player.getQuest().get_step(33) == 1 && player.getInventory().checkItem(40620)) {
             htmlid = "jcrystal2"; break;
           }  if (player.getQuest().get_step(33) == 1) {
             htmlid = "jcrystal3";
           }
           break;
         case 71091:
           if (player.getQuest().get_step(33) == 2 && player.getInventory().checkItem(40654)) {
             htmlid = "jcrystall2";
           }
           break;
         case 71074:
           if (player.getQuest().get_step(34) == 255) {

             htmlid = "lelder1"; break;
           }  if (player.getQuest().get_step(34) == 3 && player
             .getInventory().checkItem(40634)) {
             htmlid = "lelder12"; break;
           }  if (player.getQuest().get_step(34) == 3) {
             htmlid = "lelder11"; break;
           }  if (player.getQuest().get_step(34) == 2 && player
             .getInventory().checkItem(40633)) {
             htmlid = "lelder7"; break;
           }  if (player.getQuest().get_step(34) == 2) {
             htmlid = "lelder7b"; break;
           }  if (player.getQuest().get_step(34) == 1) {
             htmlid = "lelder7b"; break;
           }  if (player.getLevel() >= 40) {
             htmlid = "lelder1";
           }
           break;
         case 71076:
           if (player.getQuest().get_step(34) == 255) {
             htmlid = "ylizardb";
           }
           break;

         case 70840:
           if (player.isCrown() && player.isWizard() && player.isKnight())
             break;
           if (player.getQuest().get_step(39) == 0) {
             htmlid = "robinhood1"; break;
           }  if (player.getQuest().get_step(39) == 1) {
             htmlid = "robinhood8"; break;
           }  if (player.getQuest().get_step(39) == 2) {
             htmlid = "robinhood13"; break;
           }  if (player.getQuest().get_step(39) == 6) {
             htmlid = "robinhood9"; break;
           }  if (player.getQuest().get_step(39) == 7) {
             htmlid = "robinhood11"; break;
           }
           htmlid = "robinhood3";
           break;

         case 600005:
           if (player.getQuest().get_step(39) == 2) {
             htmlid = "zybril1"; break;
           }  if (player.getQuest().get_step(39) == 3) {
             htmlid = "zybril7"; break;
           }  if (player.getQuest().get_step(39) == 4) {
             htmlid = "zybril8"; break;
           }  if (player.getQuest().get_step(39) == 5) {
             htmlid = "zybril18"; break;
           }
           htmlid = "zybril16";
           break;

         case 71168:
           if (player.getInventory().checkItem(41028)) {
             htmlid = "dantes1";
           }
           break;
         case 71180:
           if (player.get_sex() == 0) {
             htmlid = "jp1"; break;
           }
           htmlid = "jp3";
           break;





         case 80079:
           if (player.getInventory().checkItem(41314)) {
             htmlid = "keplisha3"; break;
           }  if (player.getInventory().checkItem(41313)) {
             htmlid = "keplisha2"; break;
           }  if (player.getInventory().checkItem(41312)) {
             htmlid = "keplisha4";
           }
           break;

         case 71167:
           if (player.getCurrentSpriteId() == 3887) {
             htmlid = "frim1";
           }
           break;
         case 71141:
           if (player.getCurrentSpriteId() == 3887) {
             htmlid = "moumthree1";
           }
           break;
         case 71142:
           if (player.getCurrentSpriteId() == 3887) {
             htmlid = "moumtwo1";
           }
           break;
         case 71145:
           if (player.getCurrentSpriteId() == 3887) {
             htmlid = "moumone1";
           }
           break;
         case 71198:
           if (player.getQuest().get_step(71198) == 1) {
             htmlid = "tion4"; break;
           }  if (player.getQuest().get_step(71198) == 2) {
             htmlid = "tion5"; break;
           }  if (player.getQuest().get_step(71198) == 3) {
             htmlid = "tion6"; break;
           }  if (player.getQuest().get_step(71198) == 4) {
             htmlid = "tion7"; break;
           }  if (player.getQuest().get_step(71198) == 5) {
             htmlid = "tion5"; break;
           }  if (player.getInventory().checkItem(21059, 1)) {
             htmlid = "tion19";
           }
           break;
         case 71199:
           if (player.getQuest().get_step(71199) == 1) {
             htmlid = "jeron3"; break;
           }  if (player.getInventory().checkItem(21059, 1) || player.getQuest().get_step(71199) == 255) {
             htmlid = "jeron7";
           }
           break;
         case 6000015:
           if (player.getInventory().checkItem(41158)) {
             htmlid = "adenshadow1"; break;
           }
           htmlid = "adenshadow2";
           break;

         case 7200000:
           if (player.getLevel() >= 51 && player.getLevel() <= 82) {
             if (player.getInventory().checkItem(3000215, 1) || player.getInventory().checkItem(810002, 1) || player.getInventory().checkItem(1000004, 1)) {
               htmlid = "ekins2"; break;
             }
             htmlid = "ekins1";
             break;
           }
           htmlid = "ekins3";
           break;

         case 81200:
           if (player.getInventory().checkItem(21069) || player.getInventory().checkItem(21074)) {
             htmlid = "c_belt";
           }
           break;


         case 80076:
           if (player.getInventory().checkItem(41058)) {
             htmlid = "voyager8"; break;
           }  if (player.getInventory().checkItem(49082) || player
             .getInventory().checkItem(49083)) {

             if (player.getInventory().checkItem(41038) || player
               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039) || player

               .getInventory().checkItem(41039)) {



               htmlid = "voyager9"; break;
             }
             htmlid = "voyager7"; break;
           }
           if (player.getInventory().checkItem(49082) || player
             .getInventory().checkItem(49083) || player.getInventory().checkItem(49084) || player
             .getInventory().checkItem(49085) || player.getInventory().checkItem(49086) || player
             .getInventory().checkItem(49087) || player.getInventory().checkItem(49088) || player
             .getInventory().checkItem(49089) || player.getInventory().checkItem(49090) || player
             .getInventory().checkItem(49091))
           {
             htmlid = "voyager7";
           }
           break;
         case 50112:
           if (player.isCrown() || player.isWizard() || player.isDragonknight()) {
             int talk_step = quest.get_step(40);
             if (talk_step == 1) {
               if (player.getLevel() >= 5) {
                 htmlid = "orenb4"; break;
               }
               htmlid = "orenb14"; break;
             }
             if (talk_step == 255)
               htmlid = "orenb11";
             break;
           }
           htmlid = "orenb12";
           break;

         case 50113:
           if (player.isKnight() || player.isElf() || player.isDarkelf() || player.isBlackwizard()) {
             int talk_step = quest.get_step(40);
             if (talk_step == 1) {
               if (player.getLevel() >= 5) {
                 htmlid = "orena4"; break;
               }
               htmlid = "orena14"; break;
             }
             if (talk_step == 255)
               htmlid = "orena11";
             break;
           }
           htmlid = "orena12";
           break;

         case 80067:
           if (player.getQuest().get_step(36) == 255) {
             htmlid = "minicod10"; break;
           }  if (player.getKarmaLevel() >= 1) {
             htmlid = "minicod07"; break;
           }  if (player.getQuest().get_step(36) == 1 && player.getCurrentSpriteId() == 6034) {

             htmlid = "minicod03"; break;
           }  if (player.getQuest().get_step(36) == 1 && player.getCurrentSpriteId() != 6034) {
             htmlid = "minicod05"; break;
           }  if (player.getQuest().get_step(37) == 255 || player



             .getInventory().checkItem(41121) || player
             .getInventory().checkItem(41122)) {
             htmlid = "minicod01"; break;
           }  if (player.getInventory().checkItem(41130) && player
             .getInventory().checkItem(41131)) {
             htmlid = "minicod06"; break;
           }  if (player.getInventory().checkItem(41130)) {
             htmlid = "minicod02";
           }
           break;
         case 4201000:
           if (player.isBlackwizard()) {
             htmlid = "asha1"; break;
           }
           htmlid = "asha2";
           break;
         case 4202000:
           if (player.isDragonknight()) {
             htmlid = "feaena1"; break;
           }
           htmlid = "feaena2";
           break;
         case 3200021:
           if (!player.isElf())
             break;
           if (player.getQuest().get_step(39) == 0) {
             htmlid = "robinhood1"; break;
           }  if (player.getQuest().get_step(39) == 1) {
             htmlid = "robinhood8"; break;
           }  if (player.getQuest().get_step(39) == 2) {
             htmlid = "robinhood13"; break;
           }  if (player.getQuest().get_step(39) == 6) {
             htmlid = "robinhood9"; break;
           }  if (player.getQuest().get_step(39) == 7) {
             htmlid = "robinhood11"; break;
           }
           htmlid = "robinhood3";
           break;

         case 3200022:
           if (!player.isElf())
             break;  if (player.getQuest().get_step(39) == 2) {
             htmlid = "zybril1"; break;
           }  if (player.getQuest().get_step(39) == 3) {
             htmlid = "zybril7"; break;
           }  if (player.getQuest().get_step(39) == 4) {
             htmlid = "zybril8"; break;
           }  if (player.getQuest().get_step(39) == 5) {
             htmlid = "zybril18"; break;
           }
           htmlid = "zybril16";
           break;

         case 81202:
           if (player.getQuest().get_step(37) == 255) {
             htmlid = "minitos10"; break;
           }  if (player.getKarmaLevel() <= -1) {
             htmlid = "minitos07"; break;
           }  if (player.getQuest().get_step(37) == 1 && player.getCurrentSpriteId() == 6035) {

             htmlid = "minitos03"; break;
           }  if (player.getQuest().get_step(37) == 1 && player.getCurrentSpriteId() != 6035) {
             htmlid = "minitos05"; break;
           }  if (player.getQuest().get_step(36) == 255 || player



             .getInventory().checkItem(41130) || player
             .getInventory().checkItem(41131)) {
             htmlid = "minitos01"; break;
           }  if (player.getInventory().checkItem(41121) && player
             .getInventory().checkItem(41122)) {
             htmlid = "minitos06"; break;
           }  if (player.getInventory().checkItem(41121)) {
             htmlid = "minitos02";
           }
           break;
         case 81208:
           if (player.getInventory().checkItem(41129) || player
             .getInventory().checkItem(41138)) {
             htmlid = "minibrob04"; break;
           }  if ((player.getInventory().checkItem(41126) && player

             .getInventory().checkItem(41127) && player
             .getInventory().checkItem(41128)) || (player
             .getInventory().checkItem(41135) && player
             .getInventory().checkItem(41136) && player


             .getInventory().checkItem(41137)))
           {

             htmlid = "minibrob02";
           }
           break;

         case 5000006:
           멘트(player);
           break;
         case 4200018:
           경험치멘트(player);
           break;
         case 777849:
           if (player.getInventory().checkItem(87050)) {
             htmlid = "killton2";
           }
           break;
         case 777848:
           if (player.getInventory().checkItem(87051)) {
             htmlid = "merin2";
           }
           break;
         case 900015:
           if (player.getLevel() >= 30 && player.getLevel() <= 51) {
             htmlid = "dsecret2"; break;
           }  if (player.getLevel() > 51) {
             htmlid = "dsecret1"; break;
           }
           htmlid = "dsecret3";
           break;

         case 9134:
           if (player.getLevel() < 52) {
             htmlid = "marbinquestA"; break;
           }
           if (player.getInventory().checkItem(46115, 1)) {
             htmlid = "marbinquest3"; break;
           }
           htmlid = "marbinquest1";
           break;


         case 5088:
           if (player.getInventory().checkItem(49031)) {
             if (player.getInventory().checkItem(21081)) {
               htmlid = "gemout1"; break;
             }  if (player.getQuest().get_step(41) == 1) {
               htmlid = "gemout2"; break;
             }  if (player.getQuest().get_step(41) == 2) {
               htmlid = "gemout3"; break;
             }  if (player.getQuest().get_step(41) == 3) {
               htmlid = "gemout4"; break;
             }  if (player.getQuest().get_step(41) == 4) {
               htmlid = "gemout5"; break;
             }  if (player.getQuest().get_step(41) == 5) {
               htmlid = "gemout6"; break;
             }  if (player.getQuest().get_step(41) == 6) {
               htmlid = "gemout7"; break;
             }  if (player.getQuest().get_step(41) == 7) {
               htmlid = "gemout8"; break;
             }
             htmlid = "gemout17";
           }
           break;

         case 5092:
           if (player.isElf()) {
             htmlid = "elfin"; break;
           }
           htmlid = "elfin2";
           break;

         case 5093:
           if (player.isElf()) {
             htmlid = "elli"; break;
           }
           htmlid = "elli2";
           break;

         case 70842:
           if (player.getLawful() <= -501) {
             htmlid = "marba1"; break;
           }  if (!player.isElf()) {
             htmlid = "marba2"; break;
           }  if (player.getInventory().checkItem(40665) && (player
             .getInventory().checkItem(40693) || player.getInventory().checkItem(40694) || player
             .getInventory().checkItem(40695) || player.getInventory().checkItem(40697) || player
             .getInventory().checkItem(40698) || player.getInventory().checkItem(40699))) {
             htmlid = "marba8"; break;
           }  if (player.getInventory().checkItem(40665)) {
             htmlid = "marba17"; break;
           }  if (player.getInventory().checkItem(40664)) {
             htmlid = "marba19"; break;
           }  if (player.getInventory().checkItem(40637)) {
             htmlid = "marba18"; break;
           }
           htmlid = "marba3";
           break;

         case 70854:
           if (player.isCrown() || player.isKnight() || player.isWizard() || player.is전사() || player.isFencer() || player.isLancer()) {
             htmlid = "hurinM1"; break;
           }  if (player.isDarkelf()) {
             htmlid = "hurinE3"; break;
           }  if (player.isDragonknight()) {
             htmlid = "hurinE4"; break;
           }  if (player.isBlackwizard()) {
             htmlid = "hurinE5";
           }
           break;
         case 70839:
           if (player.isCrown() || player.isKnight() || player.isWizard() || player.is전사() || player.isFencer() || player.isLancer()) {
             htmlid = "doettM1"; break;
           }  if (player.isDarkelf()) {
             htmlid = "doettM2"; break;
           }  if (player.isDragonknight()) {
             htmlid = "doettM3"; break;
           }  if (player.isBlackwizard()) {
             htmlid = "doettM4";
           }
           break;
         case 70843:
           if (player.isCrown() || player.isKnight() || player.isWizard() || player.is전사() || player.isFencer() || player.isLancer()) {
             htmlid = "morienM1"; break;
           }  if (player.isDarkelf()) {
             htmlid = "morienM2"; break;
           }  if (player.isDragonknight()) {
             htmlid = "morienM3"; break;
           }  if (player.isBlackwizard()) {
             htmlid = "morienM4";
           }
           break;
         case 70849:
           if (player.isCrown() || player.isKnight() || player.isWizard() || player.is전사() || player.isFencer() || player.isLancer()) {
             htmlid = "theodorM1"; break;
           }  if (player.isDarkelf()) {
             htmlid = "theodorM2"; break;
           }  if (player.isDragonknight()) {
             htmlid = "theodorM3"; break;
           }  if (player.isBlackwizard()) {
             htmlid = "theodorM4";
           }
           break;
         case 5131:
           if (!player.isGhost()) {
             htmlid = "exitkir1"; break;
           }
           htmlid = "exitkir";
           break;

         case 5133:
           if (!player.isGhost()) {
             htmlid = "exitghostel1"; break;
           }
           htmlid = "exitghostel";
           break;
       }



       if (htmlid != null) {
         if (htmldata != null) {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid, htmldata));
         } else {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid));
         }

       } else if (player.getLawful() < -1000) {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
       } else {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
       }
     }
   }


   private static String talkToTownadviser(L1PcInstance pc, int town_id) {
     String htmlid;
     if (pc.getHomeTownId() == town_id && TownTable.getInstance().isLeader(pc, town_id)) {
       htmlid = "secretary1";
     } else {
       htmlid = "secretary2";
     }

     return htmlid;
   }

   private static String talkToTownmaster(L1PcInstance pc, int town_id) {
     String htmlid;
     if (pc.getHomeTownId() == town_id) {
       htmlid = "hometown";
     } else {
       htmlid = "othertown";
     }
     return htmlid;
   }


   public void onFinalAction(L1PcInstance player, String action) {}


   public void doFinalAction(L1PcInstance player) {}


   private boolean checkHasCastle(L1PcInstance player, int castle_id) {
     if (player.getClanid() != 0) {
       L1Clan clan = L1World.getInstance().getClan(player.getClanid());
       if (clan != null &&
         clan.getCastleId() == castle_id) {
         return true;
       }
     }

     return false;
   }

   private boolean checkClanLeader(L1PcInstance player) {
     if (player.isCrown()) {
       L1Clan clan = L1World.getInstance().getClan(player.getClanid());
       if (clan != null &&
         player.getId() == clan.getLeaderId()) {
         return true;
       }
     }

     return false;
   }

   private int getNecessarySealCount(L1PcInstance pc) {
     int rulerCount = 0;
     int necessarySealCount = 10;
     if (pc.getInventory().checkItem(40917)) {
       rulerCount++;
     }
     if (pc.getInventory().checkItem(40920)) {
       rulerCount++;
     }
     if (pc.getInventory().checkItem(40918)) {
       rulerCount++;
     }
     if (pc.getInventory().checkItem(40919)) {
       rulerCount++;
     }
     if (rulerCount == 0) {
       necessarySealCount = 10;
     } else if (rulerCount == 1) {
       necessarySealCount = 100;
     } else if (rulerCount == 2) {
       necessarySealCount = 200;
     } else if (rulerCount == 3) {
       necessarySealCount = 500;
     }
     return necessarySealCount;
   }

     private void 提示(L1PcInstance pc) {
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "從商人那裡購買的武器可以概率性地獲得。"));
     }





   private void 경험치멘트(L1PcInstance pc) {}




   private void createRuler(L1PcInstance pc, int attr, int sealCount) {
     int rulerId = 0;
     int protectionId = 0;
     int sealId = 0;
     if (attr == 1) {
       rulerId = 40917;
       protectionId = 40909;
       sealId = 40913;
     } else if (attr == 2) {
       rulerId = 40919;
       protectionId = 40911;
       sealId = 40915;
     } else if (attr == 4) {
       rulerId = 40918;
       protectionId = 40910;
       sealId = 40914;
     } else if (attr == 8) {
       rulerId = 40920;
       protectionId = 40912;
       sealId = 40916;
     }
     pc.getInventory().consumeItem(protectionId, 1);
     pc.getInventory().consumeItem(sealId, sealCount);
     L1ItemInstance item = pc.getInventory().storeItem(rulerId, 1);
     if (item != null) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(143, getNpcTemplate().get_name(), item.getLogName()));
     }
   }

   private String talkToSIGuide(L1PcInstance pc) {
     String htmlid = "";
     if (pc.getLevel() < 3) {
       htmlid = "en0301";
     } else if (pc.getLevel() >= 3 && pc.getLevel() < 7) {
       htmlid = "en0302";
     } else if (pc.getLevel() >= 7 && pc.getLevel() < 9) {
       htmlid = "en0303";
     } else if (pc.getLevel() >= 9 && pc.getLevel() < 12) {
       htmlid = "en0304";
     } else if (pc.getLevel() >= 12 && pc.getLevel() < 13) {
       htmlid = "en0305";
     } else if (pc.getLevel() >= 13 && pc.getLevel() < 25) {
       htmlid = "en0306";
     } else {
       htmlid = "en0307";
     }
     return htmlid;
   }

   private String talkToPopirea(L1PcInstance pc) {
     String htmlid = "";
     if (pc.getLevel() < 25) {
       htmlid = "jpe0041";
       if (pc.getInventory().checkItem(41209) || pc.getInventory().checkItem(41210) || pc
         .getInventory().checkItem(41211) || pc.getInventory().checkItem(41212)) {
         htmlid = "jpe0043";
       }
       if (pc.getInventory().checkItem(41213)) {
         htmlid = "jpe0044";
       }
     } else {
       htmlid = "jpe0045";
     }
     return htmlid;
   }

   private String talkToSecondtbox(L1PcInstance pc) {
     String htmlid = "";
     if (pc.getQuest().get_step(24) == 255) {
       if (pc.getInventory().checkItem(40701)) {
         htmlid = "maptboxa";
       } else {
         htmlid = "maptbox0";
       }
     } else {
       htmlid = "maptbox0";
     }
     return htmlid;
   }

   private String talkToThirdtbox(L1PcInstance pc) {
     String htmlid = "";
     if (pc.getQuest().get_step(25) == 255) {
       if (pc.getInventory().checkItem(40701)) {
         htmlid = "maptboxd";
       } else {
         htmlid = "maptbox0";
       }
     } else {
       htmlid = "maptbox0";
     }
     return htmlid;
   }

   public class RestMonitor
     implements Runnable
   {
     public void run() {
       if (L1MerchantInstance.this._restCallCount.decrementAndGet() == 0)
         L1MerchantInstance.this.setRest(false);
     }
   }
 }



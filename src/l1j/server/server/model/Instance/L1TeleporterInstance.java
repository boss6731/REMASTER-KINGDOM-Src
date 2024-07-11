 package l1j.server.server.model.Instance;

 import java.util.HashMap;
 import java.util.Random;
 import java.util.logging.Logger;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
 import l1j.server.MJTempleantique.MJempleantiqueController;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1Quest;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.npc.L1NpcHtml;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

 public class L1TeleporterInstance extends L1NpcInstance {
   private static final long serialVersionUID = 1L;

   public L1TeleporterInstance(L1Npc template) {
     super(template);

     this._isNowDely = false;
   }
   private static Logger _log = Logger.getLogger(L1TeleporterInstance.class.getName());

   public void onAction(L1PcInstance player) {
     L1Attack attack = new L1Attack(player, this);
     attack.calcHit();
     attack.action();
   }

   public void onTalkAction(L1PcInstance player) {
     if (player == null || this == null)
       return;
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     int npcid = getNpcTemplate().get_npcId();
     L1Quest quest = player.getQuest();
     String htmlid = null;
     if (talking != null) {
       switch (npcid) {
         case 50014:
           if (player.isWizard()) {
             if (quest.get_step(2) == 1 && !player.getInventory().checkItem(40579)) {
               htmlid = "dilong1";
               break;
             }
             htmlid = "dilong3";
           }
           break;
         case 70779:
           if (player.getCurrentSpriteId() == 1037) {
             htmlid = "ants3";
             break;
           }
           if (player.getCurrentSpriteId() == 1039) {
             if (player.isCrown()) {
               if (quest.get_step(2) == 1) {
                 if (player.getInventory().checkItem(40547)) {
                   htmlid = "antsn";
                   break;
                 }
                 htmlid = "ants1";
                 break;
               }
               htmlid = "antsn";
               break;
             }
             htmlid = "antsn";
           }
           break;
         case 70853:
           if (player.isElf() && quest.get_step(2) == 1 && !player.getInventory().checkItem(40592)) {
             Random random = new Random(System.nanoTime());
             if (random.nextInt(100) < 50) {
               htmlid = "fairyp2";
               break;
             }
             htmlid = "fairyp1";
           }
           break;
         case 50031:
           if (player.isElf() && quest.get_step(3) == 2 && !player.getInventory().checkItem(40602))
             htmlid = "sepia1";
           break;
         case 50043:
           if (quest.get_step(4) == 255) {
             htmlid = "ramuda2";
             break;
           }
           if (quest.get_step(4) == 1) {
             if (player.isCrown()) {
               if (this._isNowDely) {
                 htmlid = "ramuda4";
                 break;
               }
               htmlid = "ramudap1";
               break;
             }
             htmlid = "ramuda1";
             break;
           }
           htmlid = "ramuda3";
           break;
         case 50082:
           if (player.getLevel() < 13) {
             htmlid = "en0221";
             break;
           }
           if (player.isElf()) {
             htmlid = "en0222e";
             break;
           }
           if (player.isDarkelf()) {
             htmlid = "en0222d";
             break;
           }
           htmlid = "en0222";
           break;
         case 50001:
           if (player.isElf()) {
             htmlid = "barnia3";
             break;
           }
           if (player.isKnight() || player.isCrown() || player.is전사() || player.isFencer()) {
             htmlid = "barnia2";
             break;
           }
           if (player.isWizard() || player.isDarkelf())
             htmlid = "barnia1";
           break;
         case 50056:
           if (player.getLevel() < 45) {
             htmlid = "telesilver4";
             break;
           }
           if (player.getLevel() >= 99 && player.getLevel() <= 99) {
             htmlid = "telesilver5";
             break;
           }
           htmlid = "telesilver1";
           break;
         case 5069:
         case 50020:
         case 50024:
         case 50036:
         case 50039:
         case 50044:
         case 50046:
         case 50051:
         case 50054:
         case 50066:
         case 7320051:
           if (player.getLevel() < 45) {
             htmlid = "starttel1";
             break;
           }
           if (player.getLevel() >= 45 && player.getLevel() <= 51) {
             htmlid = "starttel2";
             break;
           }
           htmlid = "starttel3";
           break;
       }
         if (htmlid != null) {
             player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid));
         } else if (player.getLawful() < -1000) {
             player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
         } else {
             player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
         }
     } else if (npcid == 120718 && (MJempleantiqueController.getInstance()).isopen) {
         if (MJempleantiqueController.templeantique.clanmode) {
             if ((ClanTable.getInstance()).TempleantiqueclanId == player.getClanid()) {
                 MJPoint pt = MJPoint.newInstance(32616, 32927, 5, (short)1209, 50);
                 SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(player, pt.x, pt.y, pt.mapId, 112);
             } else {
                 player.sendPackets("\aH古代神的寺廟僅限於攻克霸主塔死神Grim Reaper的血盟使用。");
                 player.sendPackets("\aH無血盟攻克時僅血盟使用。");
             }
         } else {

   private static final HashMap<Integer, String[]> _teleportPrice = (HashMap)new HashMap<>();
   private static final String[] _teleportPriceDummy = new String[] { "" };

   static {
     _teleportPrice.put(Integer.valueOf(50015), new String[] { "1500" });
     _teleportPrice.put(Integer.valueOf(50017), new String[] { "50" });
     _teleportPrice.put(Integer.valueOf(50020), new String[] {
           "50", "50", "120", "120", "50", "180", "120", "120", "180", "200",
           "200", "420", "600", "1155", "7100" });
     _teleportPrice.put(Integer.valueOf(50024), new String[] {
           "132", "55", "198", "55", "132", "264", "55", "7777", "7777", "198",
           "264", "220", "220", "420", "550", "1155", "7480" });
     _teleportPrice.put(Integer.valueOf(50036), new String[] {
           "126", "126", "52", "189", "52", "52", "189", "126", "126", "315",
           "315", "420", "735", "1155", "875" });
     _teleportPrice.put(Integer.valueOf(5069), new String[] {
           "126", "126", "52", "189", "52", "52", "189", "126", "126", "315",
           "315", "420", "735", "1155", "875" });
     _teleportPrice.put(Integer.valueOf(7320051), new String[] {
           "126", "126", "52", "189", "52", "52", "189", "126", "126", "315",
           "315", "420", "735", "1155", "875" });
     _teleportPrice.put(Integer.valueOf(50039), new String[] {
           "185", "185", "123", "247", "51", "123", "247", "51", "185", "420",
           "412", "412", "824", "1155", "7931" });
     String[] ss = {
         "259", "129", "194", "129", "54", "324", "194", "259", "420", "450",
         "540", "540", "972", "1155", "7992" };
     _teleportPrice.put(Integer.valueOf(50044), ss);
     _teleportPrice.put(Integer.valueOf(50046), ss);
     _teleportPrice.put(Integer.valueOf(50051), new String[] {
           "240", "240", "180", "300", "120", "180", "300", "50", "240", "420",
           "500", "500", "900", "1155", "8000" });
     _teleportPrice.put(Integer.valueOf(50054), new String[] {
           "50", "50", "120", "120", "180", "180", "180", "240", "240", "300",
           "200", "200", "420", "500", "6500" });
     _teleportPrice.put(Integer.valueOf(50056), new String[] {
           "55", "55", "55", "132", "132", "132", "198", "198", "270", "7777",
           "7777", "246", "420", "770", "7480" });
     _teleportPrice.put(Integer.valueOf(50066), new String[] {
           "180", "50", "120", "120", "50", "50", "240", "120", "180", "420",
           "400", "400", "800", "1155", "7100" });
     _teleportPrice.put(Integer.valueOf(50068), new String[] { "1500", "800", "600", "1800", "1800", "1000", "300" });
     _teleportPrice.put(Integer.valueOf(50072), new String[] { "2200", "1800", "1000", "1600", "2200", "1200", "1300", "2000", "2000" });
     _teleportPrice.put(Integer.valueOf(50073), new String[] {
           "380", "850", "290", "290", "290", "180", "480", "150", "150", "380",
           "480", "380", "850" });
     _teleportPrice.put(Integer.valueOf(50079), new String[] {
           "550", "550", "600", "550", "700", "600", "600", "750", "750", "550",
           "550", "700", "650" });
     _teleportPrice.put(Integer.valueOf(3000005), new String[] {
           "50", "50", "50", "50", "120", "120", "180", "180", "180", "240",
           "240", "400", "400", "800", "7700" });
     _teleportPrice.put(Integer.valueOf(3100005), new String[] {
           "50", "50", "50", "120", "180", "180", "240", "240", "240", "300",
           "300", "500", "500", "900", "8000" });
     ss = new String[] { "0", "0", "0" };
     _teleportPrice.put(Integer.valueOf(50026), ss);
     _teleportPrice.put(Integer.valueOf(50033), ss);
     _teleportPrice.put(Integer.valueOf(50049), ss);
     _teleportPrice.put(Integer.valueOf(50059), ss);
     _teleportPrice.put(Integer.valueOf(6000014), new String[] { "14000" });
     _teleportPrice.put(Integer.valueOf(6000016), new String[] { "1000" });
     _teleportPrice.put(Integer.valueOf(900056), new String[] { "7000", "7000", "7000", "14000", "14000" });
     _teleportPrice.put(Integer.valueOf(5091), new String[] {
           "57", "57", "57", "138", "138", "138", "138", "207", "207", "230",
           "230", "690" });
   }

   static class HtmlPricePair {
     String html;
     String[] price;

     HtmlPricePair(String h, String[] p) {
       this.html = h;
       this.price = p;
     }
   }
   private static final HashMap<Integer, HtmlPricePair> _teleportPriceA = new HashMap<>();
   private static final HtmlPricePair _teleportPriceDummyA = new HtmlPricePair("", new String[] { "" });

   static {
     _teleportPriceA.put(Integer.valueOf(50079), new HtmlPricePair("telediad3", new String[] { "700", "800", "800", "1000" }));
     _teleportPriceA.put(Integer.valueOf(3000005), new HtmlPricePair("dekabia3", new String[] { "100", "220", "220", "220", "330", "330", "330", "330", "440", "440" }));
     _teleportPriceA.put(Integer.valueOf(3100005), new HtmlPricePair("sharial", new String[] { "220", "330", "330", "330", "440", "440", "550", "550", "550", "550" }));
   }

   private static final HashMap<Integer, HtmlPricePair> _teleportPriceL = new HashMap<>();
   private static final HtmlPricePair _teleportPriceDummyL = new HtmlPricePair("telesilver3", new String[] { "780", "780", "780", "780", "780", "1230", "1080", "1080", "1080", "1080" });

   static {
     _teleportPriceL.put(Integer.valueOf(50056), new HtmlPricePair("guide_0_1", new String[] { "30", "30", "30", "70", "80", "90", "100", "30" }));
     HtmlPricePair p = new HtmlPricePair("guide_6", new String[] { "500", "500" });
     _teleportPriceL.put(Integer.valueOf(50020), p);
     _teleportPriceL.put(Integer.valueOf(50024), p);
     _teleportPriceL.put(Integer.valueOf(50036), p);
     _teleportPriceL.put(Integer.valueOf(5069), p);
     _teleportPriceL.put(Integer.valueOf(50039), p);
     _teleportPriceL.put(Integer.valueOf(50044), p);
     _teleportPriceL.put(Integer.valueOf(50046), p);
     _teleportPriceL.put(Integer.valueOf(50051), p);
     _teleportPriceL.put(Integer.valueOf(50054), p);
     _teleportPriceL.put(Integer.valueOf(50066), p);
     _teleportPriceL.put(Integer.valueOf(5069), p);
     _teleportPriceL.put(Integer.valueOf(7320051), p);
   }

   private static final HashMap<Integer, HtmlPricePair> _teleportPriceM = new HashMap<>();
   private static final HtmlPricePair _teleportPriceDummyM = new HtmlPricePair("", new String[] { "" });

   static {
     _teleportPriceM.put(Integer.valueOf(50056), new HtmlPricePair("hp_storm1", new String[] { "" }));
     HtmlPricePair pair = new HtmlPricePair("guide_7", new String[] {
           "500", "500", "500", "500", "500", "500", "500", "500", "500", "500",
           "500" });
     _teleportPriceM.put(Integer.valueOf(50020), pair);
     _teleportPriceM.put(Integer.valueOf(50024), pair);
     _teleportPriceM.put(Integer.valueOf(50036), pair);
     _teleportPriceM.put(Integer.valueOf(5069), pair);
     _teleportPriceM.put(Integer.valueOf(50039), pair);
     _teleportPriceM.put(Integer.valueOf(50044), pair);
     _teleportPriceM.put(Integer.valueOf(50046), pair);
     _teleportPriceM.put(Integer.valueOf(50051), pair);
     _teleportPriceM.put(Integer.valueOf(50054), pair);
     _teleportPriceM.put(Integer.valueOf(50066), pair);
     _teleportPriceM.put(Integer.valueOf(5069), pair);
     _teleportPriceM.put(Integer.valueOf(7320051), pair);
   }

   private static final HashMap<String, HtmlPricePair> _teleportPriceOther = new HashMap<>();
   private boolean _isNowDely;

   static {
     _teleportPriceOther.put("teleportURLB", new HtmlPricePair("guide_1_1", new String[] { "450", "450", "450", "450" }));
     _teleportPriceOther.put("teleportURLC", new HtmlPricePair("guide_1_2", new String[] { "465", "465", "465", "465", "1065", "1065" }));
     _teleportPriceOther.put("teleportURLD", new HtmlPricePair("guide_1_3", new String[] { "480", "480", "480", "480", "630", "1080", "630" }));
     _teleportPriceOther.put("teleportURLE", new HtmlPricePair("guide_2_1", new String[] { "600", "600", "750", "750" }));
     _teleportPriceOther.put("teleportURLF", new HtmlPricePair("guide_2_2", new String[] { "615", "615", "915", "765" }));
     _teleportPriceOther.put("teleportURLG", new HtmlPricePair("guide_2_3", new String[] { "630", "780", "630", "1080", "930" }));
     _teleportPriceOther.put("teleportURLH", new HtmlPricePair("guide_3_1", new String[] { "750", "750", "750", "1200", "1050" }));
     _teleportPriceOther.put("teleportURLI", new HtmlPricePair("guide_3_2", new String[] { "765", "765", "765", "765", "1515", "1215", "915" }));
     _teleportPriceOther.put("teleportURLJ", new HtmlPricePair("guide_3_3", new String[] { "780", "780", "780", "780", "780", "1230", "1080" }));
     _teleportPriceOther.put("teleportURLK", new HtmlPricePair("guide_4", new String[] { "780", "780", "780", "780", "780", "1230", "1080" }));
     _teleportPriceOther.put("teleportURLO", new HtmlPricePair("guide_8", new String[] { "750" }));
   }

   public void onFinalAction(L1PcInstance player, String action) {
     if (this == null || player == null)
       return;
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     if (action.equalsIgnoreCase("teleportURL")) {
       L1NpcHtml html = new L1NpcHtml(talking.getTeleportURL());
       String[] price = null;
       int npcid = getNpcTemplate().get_npcId();
       price = _teleportPrice.get(Integer.valueOf(npcid));
       if (price == null)
         price = _teleportPriceDummy;
       player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, html, price));
     } else if (action.equalsIgnoreCase("teleportURLA")) {
       int npcid = getNpcTemplate().get_npcId();
       HtmlPricePair pair = _teleportPriceA.get(Integer.valueOf(npcid));
       if (pair == null)
         pair = _teleportPriceDummyA;
       player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, pair.html, pair.price));
     } else if (action.equalsIgnoreCase("teleportURLL")) {
       int npcid = getNpcTemplate().get_npcId();
       HtmlPricePair pair = _teleportPriceL.get(Integer.valueOf(npcid));
       if (pair == null)
         pair = _teleportPriceDummyL;
       player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, pair.html, pair.price));
     } else if (action.equalsIgnoreCase("teleportURLM")) {
       int npcid = getNpcTemplate().get_npcId();
       HtmlPricePair pair = _teleportPriceM.get(Integer.valueOf(npcid));
       if (pair == null)
         pair = _teleportPriceDummyM;
       player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, pair.html, pair.price));
     } else {
       HtmlPricePair pair = _teleportPriceOther.get(action);
       if (pair != null)
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, pair.html, pair.price));
     }
     if (action.startsWith("teleport")) {
       _log.finest("將操作設定為： " + action);
       doFinalAction(player, action);
     }
   }

   private void doFinalAction(L1PcInstance player, String action) {
     if (this == null || player == null)
       return;
     int objid = getId();
     int npcid = getNpcTemplate().get_npcId();
     String htmlid = null;
     boolean isTeleport = true;
     if (npcid == 50014) {
       if (!player.getInventory().checkItem(40581)) {
         isTeleport = false;
         htmlid = "dilongn";
       }
     } else if (npcid == 50043) {
       if (this._isNowDely)
         isTeleport = false;
     } else if (npcid == 50625 && this._isNowDely) {
       isTeleport = false;
     }
     if (isTeleport)
       try {
         if (action.equalsIgnoreCase("teleport mutant-dungen_la")) {
           for (L1PcInstance otherPc : L1World.getInstance().getVisiblePlayer((L1Object)player, 3)) {
             if (otherPc.getClanid() == player.getClanid() && otherPc.getId() != player.getId())
               otherPc.start_teleport(32740, 32800, 217, 5, 18339, true, false);
           }
           player.start_teleport(32740, 32800, 217, 5, 18339, true, false);
         } else if (action.equalsIgnoreCase("teleport mage-quest-dungen_la")) {
           player.start_teleport(32791, 32788, 201, 5, 18339, true, false);
         } else if (action.equalsIgnoreCase("teleport 29_la")) {
           L1PcInstance kni = null;
           L1PcInstance elf = null;
           L1PcInstance wiz = null;
           L1Quest quest = null;
           for (L1PcInstance otherPc : L1World.getInstance().getVisiblePlayer((L1Object)player, 3)) {
             quest = otherPc.getQuest();
             if (otherPc.isKnight() && quest.get_step(4) == 1) {
               if (kni == null)
                 kni = otherPc;
               continue;
             }
             if (otherPc.isElf() && quest.get_step(4) == 1) {
               if (elf == null)
                 elf = otherPc;
               continue;
             }
             if (otherPc.isWizard() && quest.get_step(4) == 1 && wiz == null)
               wiz = otherPc;
           }
           if (kni != null && elf != null && wiz != null) {
             player.start_teleport(32723, 32850, 2000, 2, 18339, true, false);
             kni.start_teleport(32750, 32851, 2000, 6, 18339, true, false);
             elf.start_teleport(32878, 32980, 2000, 6, 18339, true, false);
             wiz.start_teleport(32876, 33003, 2000, 0, 18339, true, false);
             this._isNowDely = true;
             TeleportDelyTimer timer = new TeleportDelyTimer();
             GeneralThreadPool.getInstance().schedule(timer, 900000L);
           }
         } else if (action.equalsIgnoreCase("teleport barlog_la")) {
           player.start_teleport(32755, 32844, 2002, 5, 18339, true, false);
           TeleportDelyTimer timer = new TeleportDelyTimer();
           GeneralThreadPool.getInstance().execute(timer);
         }
       } catch (Exception exception) {}
     if (htmlid != null)
       player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid));
   }

   class TeleportDelyTimer implements Runnable {
     public void run() {
       L1TeleporterInstance.this._isNowDely = false;
     }
   }
 }



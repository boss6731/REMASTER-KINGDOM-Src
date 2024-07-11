 package l1j.server.server.model.monitor;

 import java.util.ArrayList;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PING_REQ;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI;
 import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI;
 import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Info;
 import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Loader;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Lawful;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.CommonUtil;



 public class L1PcExpMonitor
   extends L1PcMonitor
 {
   private int _old_lawful;
   private long _old_exp;
   private boolean _old_ruler_item;
   private boolean _old_ruler_zone;
   private boolean _PolyMaster_item;
   private boolean _PolyMaster_check;
   private PingCheckHandler pingHandler;
   private TitanBeastChaList chalist;
   private PcGoldenBuff pcgoldenbuff;

   public L1PcExpMonitor(L1PcInstance pc, int oId) {
     super(oId);
     this.pingHandler = new PingCheckHandler(pc);
     this.chalist = new TitanBeastChaList(pc);
     this.pcgoldenbuff = new PcGoldenBuff(pc);
   }

   public void onPingResponse() {
     this.pingHandler.onResponse();
   }


   public void execTask(L1PcInstance pc) {
     try {
       this.pingHandler.onTick();
       this.chalist.onTick();
       if (pc.isPcBuff()) {
         this.pcgoldenbuff.onTick();
       }


       if (this._old_lawful != pc.getLawful()) {
         this._old_lawful = pc.getLawful();
         S_Lawful s_lawful = new S_Lawful(pc.getId(), this._old_lawful);
         pc.sendPackets((ServerBasePacket)s_lawful);
         pc.broadcastPacket((ServerBasePacket)s_lawful);
       }


       if (this._old_exp != pc.get_exp()) {
         this._old_exp = pc.get_exp();
         pc.onChangeExp();
       }

       if (this._old_ruler_item != pc.getInventory().checkItem(900111)) {
         this._old_ruler_item = pc.getInventory().checkItem(900111);
         RulerZone(pc);
       }

       if (this._old_ruler_zone != pc.getMap().isRuler()) {
         this._old_ruler_zone = pc.getMap().isRuler();
         RulerZone(pc);
       }

       if (pc.getMapId() == 111 || pc.getMapId() == 12862) {
         if (pc.is_dominion_tel()) {
           Dominion_Tel(pc);
         }
       } else if ((((pc.getMapId() != 111) ? 1 : 0) & ((pc.getMapId() != 12862) ? 1 : 0)) != 0) {
         Dominion_Tel(pc);
       }

       if (this._PolyMaster_item != pc.getInventory().checkItem(4100500)) {
         this._PolyMaster_item = pc.getInventory().checkItem(4100500);
         this._PolyMaster_check = false;
         PolyMaster(pc);
       }
       if (!this._PolyMaster_check && pc.getInventory().checkItem(4100500)) {
         this._PolyMaster_check = true;
       }


       if (this._PolyMaster_item != pc.getInventory().checkItem(4100610)) {
         this._PolyMaster_item = pc.getInventory().checkItem(4100610);
         this._PolyMaster_check = false;
         PolyMaster(pc);
       }
       if (!this._PolyMaster_check && pc.getInventory().checkItem(4100610)) {
         this._PolyMaster_check = true;
       }



       Pc_Golden_Buff_Enable_Check(pc);
       if (pc.getMapId() != 5166) {
         if (pc.getLevel() >= 100 || pc.getHighLevel() >= 100) {
           if (pc.getAbility().getCon() > 60 || pc.getAbility().getStr() > 60 || pc.getAbility().getDex() > 60 || pc.getAbility().getCha() > 60 || pc.getAbility().getInt() > 60 || pc
             .getAbility().getWis() > 60) {
             int locx2 = 32723 + CommonUtil.random(10);
             int locy2 = 32851 + CommonUtil.random(10);
             pc.start_teleport(locx2, locy2, 5166, 5, 18339, true, false);

                // 重置玩家屬性
               pc.重置玩家屬性();
                // 輸出玩家相關信息
               System.out.println("▶ 屬性錯誤驅逐(0):" + pc.getName() + "/[等級]:" + pc.getLevel() +
                       "/[最高等級]:" + pc.getHighLevel() + "/[體質]:" + pc.getAbility().getCon() +
                       "/[力量]:" + pc.getAbility().getStr() + "/[敏捷]:" + pc.getAbility().getDex() +
                       "/[魅力]:" + pc.getAbility().getCha() + "/[智力]:" + pc.getAbility().getInt() +
                       "/[精神]:" + pc.getAbility().getWis());
           }
                // 如果玩家等級或最高等級大於等於90
         } else if (pc.getLevel() >= 90 || pc.getHighLevel() >= 90) {
                // 並且任一屬性值大於55
             if (pc.getAbility().getCon() > 55 || pc.getAbility().getStr() > 55 ||
                     pc.getAbility().getDex() > 55 || pc.getAbility().getCha() > 55 ||
                     pc.getAbility().getInt() > 55 || pc.getAbility().getWis() > 55) {
                    // 隨機生成傳送位置
                 int locx2 = 32723 + CommonUtil.random(10);
                 int locy2 = 32851 + CommonUtil.random(10);
                // 傳送玩家到新位置

                 pc.start_teleport(locx2, locy2, 5166, 5, 18339, true, false);

                    // 重置玩家屬性
                 pc.重置玩家屬性();
                    // 輸出玩家相關信息
                 System.out.println("▶ 屬性錯誤驅逐(1):" + pc.getName() + "/[等級]:" + pc.getLevel() +
                         "/[最高等級]:" + pc.getHighLevel() + "/[體質]:" + pc.getAbility().getCon() +
                         "/[力量]:" + pc.getAbility().getStr() + "/[敏捷]:" + pc.getAbility().getDex() +
                         "/[魅力]:" + pc.getAbility().getCha() + "/[智力]:" + pc.getAbility().getInt() +
                         "/[精神]:" + pc.getAbility().getWis());
             }
             // 檢查玩家屬性值是否超過50
         } else if (pc.getAbility().getCon() > 50 || pc.getAbility().getStr() > 50 ||
                 pc.getAbility().getDex() > 50 || pc.getAbility().getCha() > 50 ||
                 pc.getAbility().getInt() > 50 || pc.getAbility().getWis() > 50) {
                // 隨機生成傳送位置
             int locx2 = 32723 + CommonUtil.random(10);
             int locy2 = 32851 + CommonUtil.random(10);
                // 傳送玩家到新位置
             pc.start_teleport(locx2, locy2, 5166, 5, 18339, true, false);

                // 重置玩家屬性
             pc.重置玩家屬性();
                // 輸出玩家相關信息
             System.out.println("▶ 屬性錯誤驅逐(2):" + pc.getName() + "/[等級]:" + pc.getLevel() +
                     "/[最高等級]:" + pc.getHighLevel() + "/[體質]:" + pc.getAbility().getCon() +
                     "/[力量]:" + pc.getAbility().getStr() + "/[敏捷]:" + pc.getAbility().getDex() +
                     "/[魅力]:" + pc.getAbility().getCha() + "/[智力]:" + pc.getAbility().getInt() +
                     "/[精神]:" + pc.getAbility().getWis());
         }

       }
     }
     catch (Exception e) {
       e.printStackTrace();
     }

     try {
       onClanBuff(pc);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private void onClanBuff(L1PcInstance pc) {
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     if (clan != null && (clan.getOnlineClanMember()).length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT && !pc.isClanBuff()) {
       pc.setSkillEffect(7789, 0L);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 450, true));
       pc.setClanBuff(true);
     } else if (clan != null && (clan.getOnlineClanMember()).length < Config.ServerAdSetting.CLANBUFFUSERCOUNT && pc.isClanBuff()) {
       pc.killSkillEffectTimer(7789);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 450, false));
       pc.setClanBuff(false);
     }
   }

   public static void Pc_Golden_Buff_Enable_Check(L1PcInstance pc) {
     if (!pc.isPcBuff()) {
       return;
     }
     ArrayList<Integer> MapList1 = new ArrayList<>();
     int[] maplist1 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP1;
     for (int i = 0; i < maplist1.length; i++) {
       MapList1.add(Integer.valueOf(maplist1[i]));
     }
     ArrayList<Integer> MapList2 = new ArrayList<>();
     int[] maplist2 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP2;
     for (int j = 0; j < maplist2.length; j++) {
       MapList2.add(Integer.valueOf(maplist2[j]));
     }
     ArrayList<Integer> bufflist = new ArrayList<>();

     int mapid = pc.getMapId();



     if (!pc.isPcGoldenStatus()) {
       if (MapList1.contains(Integer.valueOf(mapid))) {
         if (pc.getAccount().get_Index0_Remain_Time() > 0) {

           int buff1type = pc.getAccount().get_Index0_type(pc);
           int buffgrade = 0;
           int buffindex = 0;
           int buffgroup = 0;
           if (pc.getAccount().get_Index0_1() != 0) {
             buffindex = 1;
             switch (pc.getAccount().get_Index0_1()) {
               case 1:
                 buffgrade = 1;
                 break;
               case 2:
                 buffgrade = 2;
                 break;
               case 3:
                 buffgrade = 3;
                 break;
             }
             int buffid = buffgroup * 1000 + buffindex * 100 + buff1type * 10 + buffgrade;
             bufflist.add(Integer.valueOf(buffid));
           }

           if (pc.getAccount().get_Index0_2() != 0) {
             buffindex = 2;
             switch (pc.getAccount().get_Index0_2()) {
               case 1:
                 buffgrade = 1;
                 break;
               case 2:
                 buffgrade = 2;
                 break;
               case 3:
                 buffgrade = 3;
                 break;
             }
             int buffid = buffgroup * 1000 + buffindex * 100 + buff1type * 10 + buffgrade;
             bufflist.add(Integer.valueOf(buffid));
           }
           if (pc.getAccount().get_Index0_3() != 0) {
             buffindex = 3;
             switch (pc.getAccount().get_Index0_3()) {
               case 1:
                 buffgrade = 1;
                 break;
               case 2:
                 buffgrade = 2;
                 break;
               case 3:
                 buffgrade = 3;
                 break;
             }
             int buffid = buffgroup * 1000 + buffindex * 100 + buff1type * 10 + buffgrade;
             bufflist.add(Integer.valueOf(buffid));
           }
           if (bufflist.size() > 0 && !bufflist.isEmpty()) {
             for (int m = 0; m < bufflist.size(); m++) {
               int index = ((Integer)bufflist.get(m)).intValue();
               Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
               if (info != null) {
                 Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, true);
               }
             }
           }
           ArrayList<Integer> hasBufflist = pc.getPcGoldenBuffList();
           for (int k = 0; k < hasBufflist.size(); k++) {
             if (!bufflist.contains(hasBufflist.get(k))) {
               int index = ((Integer)hasBufflist.get(k)).intValue();
               Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
               if (info != null) {
                 Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
               }
             }
           }
           bufflist = null;
           SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc, 0, true);
           SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.send(pc, SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason.UPDATE);
           pc.set_PcGoldenSstatus(true);
         }
       } else if (MapList2.contains(Integer.valueOf(mapid)) &&
         pc.getAccount().get_Index1_Remain_Time() > 0) {
         int bufftype = pc.getAccount().get_Index0_type(pc);
         int buffgrade = 0;
         int buffindex = 0;
         int buffgroup = 1;
         if (pc.getAccount().get_Index0_1() != 0) {
           buffindex = 1;
           switch (pc.getAccount().get_Index0_1()) {
             case 1:
               buffgrade = 1;
               break;
             case 2:
               buffgrade = 2;
               break;
             case 3:
               buffgrade = 3;
               break;
           }
           int buffid = buffgroup * 1000 + buffindex * 100 + bufftype * 10 + buffgrade;
           bufflist.add(Integer.valueOf(buffid));
         }

         if (pc.getAccount().get_Index0_2() != 0) {
           buffindex = 2;
           switch (pc.getAccount().get_Index0_2()) {
             case 1:
               buffgrade = 1;
               break;
             case 2:
               buffgrade = 2;
               break;
             case 3:
               buffgrade = 3;
               break;
           }
           int buffid = buffgroup * 1000 + buffindex * 100 + bufftype * 10 + buffgrade;
           bufflist.add(Integer.valueOf(buffid));
         }
         if (pc.getAccount().get_Index0_3() != 0) {
           buffindex = 3;
           switch (pc.getAccount().get_Index0_3()) {
             case 1:
               buffgrade = 1;
               break;
             case 2:
               buffgrade = 2;
               break;
             case 3:
               buffgrade = 3;
               break;
           }
           int buffid = buffgroup * 1000 + buffindex * 100 + bufftype * 10 + buffgrade;
           bufflist.add(Integer.valueOf(buffid));
         }
         if (bufflist.size() > 0 && !bufflist.isEmpty()) {
           for (int m = 0; m < bufflist.size(); m++) {
             int index = ((Integer)bufflist.get(m)).intValue();
             Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
             if (info != null) {
               Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, true);
             }
           }
         }
         ArrayList<Integer> hasBufflist = pc.getPcGoldenBuffList();
         for (int k = 0; k < hasBufflist.size(); k++) {
           if (!bufflist.contains(hasBufflist.get(k))) {
             int index = ((Integer)hasBufflist.get(k)).intValue();
             Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
             if (info != null) {
               Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
             }
           }
         }
         bufflist = null;
         SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc, 1, true);
         SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.send(pc, SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason.UPDATE);
         pc.set_PcGoldenSstatus(true);
       }
     }


     if (pc.isPcGoldenStatus() &&
       !MapList1.contains(Integer.valueOf(mapid)) && !MapList2.contains(Integer.valueOf(mapid))) {
       SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc, 0, false);
       pc.set_PcGoldenSstatus(false);
       ArrayList<Integer> bufflistdel = pc.getPcGoldenBuffList();
       if (bufflistdel.size() != 0 && !bufflistdel.isEmpty()) {
         for (int k = 0; k < bufflistdel.size(); k++) {
           int index = ((Integer)bufflistdel.get(k)).intValue();
           Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
           if (info != null) {
             Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
           }
         }
       }
     }
   }




   private void Dominion_Tel(L1PcInstance pc) {
     if (pc.getMapId() == 111 || pc.getMapId() == 12862) {
       if (pc.is_dominion_tel()) {

         SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
         noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
         noti.set_spell_id(10959);
         noti.set_duration(1);
         noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
         noti.set_on_icon_id(10959);
         noti.set_off_icon_id(0);
         noti.set_icon_priority(3);
         noti.set_tooltip_str_id(9051);
         noti.set_new_str_id(0);
         noti.set_end_str_id(0);
         noti.set_is_good(true);
         pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
       }
     } else {
       pc.removeSkillEffect(10959);
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
       noti.set_spell_id(10959);
       noti.set_off_icon_id(0);
       noti.set_end_str_id(0);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
     }
   }

   private void RulerZone(L1PcInstance pc) {
     if (pc.getMap().isRuler() && pc.getInventory().checkItem(900111)) {
       pc.setSkillEffect(8463, -1L);
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
       noti.set_spell_id(8463);
       noti.set_duration(1);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
       noti.set_on_icon_id(8463);
       noti.set_off_icon_id(0);
       noti.set_icon_priority(3);
       noti.set_tooltip_str_id(5119);
       noti.set_new_str_id(0);
       noti.set_end_str_id(0);
       noti.set_is_good(true);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
     } else {
       pc.removeSkillEffect(8463);
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
       noti.set_spell_id(8463);
       noti.set_off_icon_id(0);
       noti.set_end_str_id(0);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
     }
   }


   private void PolyMaster(L1PcInstance pc) {
     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
     if (pc.hasSkillEffect(80012) && !pc.getInventory().checkItem(4100500)) {
       pc.removeSkillEffect(80012);
     }
     if (pc.hasSkillEffect(80013) && !pc.getInventory().checkItem(4100610)) {
       pc.removeSkillEffect(80013);
     }
   }


   static class LawfulBonusInfo
   {
     int ac;
     int mr;
     int sp;
     int add_damage;
     int min_lawful;
     int max_lawful;
     int level_num;

     LawfulBonusInfo(int ac, int mr, int sp, int add_damage, int min_lawful, int max_lawful, int level_num) {
       this.ac = ac;
       this.mr = mr;
       this.sp = sp;
       this.add_damage = add_damage;
       this.min_lawful = min_lawful;
       this.max_lawful = max_lawful;
       this.level_num = level_num;
     }

     boolean is_range(L1PcInstance pc) {
       int lawful = pc.getLawful();
       return (lawful >= this.min_lawful && lawful <= this.max_lawful);
     }

     void off_update(L1PcInstance pc) {
       pc.getAC().addAc(this.ac * -1);
       pc.getResistance().addMr(this.mr * -1);
       pc.getAbility().addSp(this.sp * -1);
       pc.addDmgRate(this.add_damage * -1);
       pc.addBowDmgRate(this.add_damage * -1);

       pc.sendPackets((ServerBasePacket)new S_PacketBox(114, this.level_num, false));
     }

     void on_update(L1PcInstance pc) {
       int current_bapo_level = pc.get_bapo_level();
       if (current_bapo_level == this.level_num) {
         return;
       }
       LawfulBonusInfo prev_bonus = L1PcExpMonitor.m_lawful_bonuses[current_bapo_level];
       prev_bonus.off_update(pc);


       pc.set_bapo_level(this.level_num);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(114, this.level_num, true));

       pc.getAC().addAc(this.ac);
       pc.getResistance().addMr(this.mr);
       pc.getAbility().addSp(this.sp);
       pc.addDmgRate(this.add_damage);
       pc.addBowDmgRate(this.add_damage);
       pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
     }
   }






   private static final LawfulBonusInfo[] m_lawful_bonuses = new LawfulBonusInfo[] { new LawfulBonusInfo(-2, 3, 0, 0, 1001, 10000, 0), new LawfulBonusInfo(-4, 6, 0, 0, 10001, 20000, 1), new LawfulBonusInfo(-6, 9, 0, 0, 20000, 99999, 2), new LawfulBonusInfo(0, 0, 1, 1, -10000, -1, 3), new LawfulBonusInfo(0, 0, 2, 3, -20000, -10001, 4), new LawfulBonusInfo(0, 0, 3, 5, -99999, -20001, 5), new LawfulBonusInfo(0, 0, 0, 0, 1000, 0, 6) };


   public static final int NONE_STATE_BAPO_LEVEL = 6;



   private static class PingCheckHandler
   {
     private long requestMillis;


     private final L1PcInstance pc;

     private int remainTick;


     PingCheckHandler(L1PcInstance pc) {
       this.pc = pc;
       onInitializeTick();
     }


     private void onInitializeTick() {
       this.remainTick = Config.Login.PINGCHECK_SECOND;
     }

     private void onTick() {
       if (this.remainTick <= 0) {
         return;
       }

       if (--this.remainTick != 0) {
         return;
       }
       try {
         onRequest();



       }
       catch (Exception e) {
         e.printStackTrace();
       }
     }

     private void onRequest() {
       if (this.pc.getNetConnection() == null || this.pc.getNetConnection().isClosed()) {
         return;
       }

       this.requestMillis = System.currentTimeMillis();
       SC_PING_REQ req = SC_PING_REQ.newInstance();
       req.set_transaction_id(this.pc.getId());
       this.pc.sendPackets((MJIProtoMessage)req, MJEProtoMessages.SC_PING_REQ);
     }

       // 定義 onResponse 方法
       private void onResponse() {
           try {
                  // 計算響應時間（當前時間減去請求發出時間）
               long responseMillis = System.currentTimeMillis() - this.requestMillis;
                    // 如果響應時間大於或等於配置的 PINGCHECK 時間
               if (responseMillis >= Config.Login.PINGCHECK) {
                    // 向玩家發送消息，通知網絡延遲情況
                   this.pc.sendPackets(String.format("\f3您的網絡連接狀態瞬間不穩定。(網絡延遲: %,d 毫秒)", new Object[] { Long.valueOf(responseMillis) }));
               }
           } finally {
              // 最終執行初始化計時器方法
               onInitializeTick();
           }
       }


   private static class PcGoldenBuff
   {
     private final L1PcInstance pc;
     private int remainTick;
     private int saveTick;

     PcGoldenBuff(L1PcInstance pc) {
       this.pc = pc;
       onInitializeTick();
       onInitializeSaveTick();
     }

     private void onInitializeTick() {
       this.remainTick = 10;
     }
     private void onInitializeSaveTick() {
       this.saveTick = 60;
     }
     private void onTick() {
       if (this.remainTick <= 0) {
         onInitializeTick();
         return;
       }
       if (--this.remainTick != 0) {
         return;
       }

       try {
         onMap();
         this.saveTick--;
         if (this.saveTick <= 0) {

           this.pc.getAccount().update_Index0_Remain_Time();
           this.pc.getAccount().update_Index1_Remain_Time();
           onInitializeSaveTick();
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     }


     private void onMap() {
       ArrayList<Integer> MapList1 = new ArrayList<>();
       int[] maplist1 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP1;
       for (int i = 0; i < maplist1.length; i++) {
         MapList1.add(Integer.valueOf(maplist1[i]));
       }
       ArrayList<Integer> MapList2 = new ArrayList<>();
       int[] maplist2 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP2;
       for (int j = 0; j < maplist2.length; j++) {
         MapList2.add(Integer.valueOf(maplist2[j]));
       }
       int mapId = this.pc.getMapId();
       if (MapList1.contains(Integer.valueOf(mapId))) {
         if (this.pc.getAccount().get_Index0_Remain_Time() >= 10) {
           this.pc.getAccount().use_Index0_Time(10);
         } else {

           this.pc.getAccount().use_Index0_Time(this.pc.getAccount().get_Index0_Remain_Time());
           if (this.pc.isPcGoldenStatus()) {
             SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(this.pc, 0, false);
             this.pc.set_PcGoldenSstatus(false);
             ArrayList<Integer> bufflistdel = this.pc.getPcGoldenBuffList();
             if (bufflistdel.size() != 0 && !bufflistdel.isEmpty()) {
               for (int k = 0; k < bufflistdel.size(); k++) {
                 int index = ((Integer)bufflistdel.get(k)).intValue();
                 Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
                 if (info != null) {
                   Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(this.pc, index, false);
                 }
               }
             }
           }
         }
       } else if (MapList2.contains(Integer.valueOf(mapId))) {
         if (this.pc.getAccount().get_Index1_Remain_Time() >= 10) {
           this.pc.getAccount().use_Index1_Time(10);
         } else {

           this.pc.getAccount().use_Index1_Time(this.pc.getAccount().get_Index1_Remain_Time());
           if (this.pc.isPcGoldenStatus()) {
             SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(this.pc, 0, false);
             this.pc.set_PcGoldenSstatus(false);
             ArrayList<Integer> bufflistdel = this.pc.getPcGoldenBuffList();
             if (bufflistdel.size() != 0 && !bufflistdel.isEmpty()) {
               for (int k = 0; k < bufflistdel.size(); k++) {
                 int index = ((Integer)bufflistdel.get(k)).intValue();
                 Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
                 if (info != null) {
                   Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(this.pc, index, false);
                 }
               }
             }
           }
         }
       }
     }
   }


   private static class TitanBeastChaList
   {
     private final L1PcInstance pc;
     private int remainTick;

     TitanBeastChaList(L1PcInstance pc) {
       this.pc = pc;
       onInitializeTick();
     }

     private void onInitializeTick() {
       this.remainTick = 2;
     }

     private void onTick() {
       if (this.remainTick <= 0) {
         onInitializeTick();

         return;
       }
       if (--this.remainTick != 0) {
         return;
       }

       try {
         onTitanBeast();
       } catch (Exception e) {
         e.printStackTrace();
       }
     }

     private void onTitanBeast() {
       if (this.pc.getTitanBeastChaList() != null) {
         if (this.pc.getTitanBeastChaList().size() > 1) {
           this.pc.setTitanBeast(true);
         } else {
           this.pc.setTitanBeast(false);
         }
         this.pc.getTitanBeastChaList().clear();
       }
     }
   }
 }



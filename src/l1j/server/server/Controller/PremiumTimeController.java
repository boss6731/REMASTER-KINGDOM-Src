 package l1j.server.server.Controller;

 import java.io.File;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.ServerBasePacket;





 public class PremiumTimeController
   implements Runnable
 {
   public static final int SLEEP_TIME = Config.ServerRates.FeatherTime * 60 * 1000;
   private static final SimpleDateFormat _pFormat = new SimpleDateFormat("yyyyMMdd");
   private static PremiumTimeController _instance;

   public static PremiumTimeController getInstance() {
     if (_instance == null) {
       _instance = new PremiumTimeController();
     }
     return _instance;
   }


   public void run() {
     try {
       GeneralThreadPool.getInstance().execute(new checkPremiumTime());
       GeneralThreadPool.getInstance().execute(new checkDragonBlood());
       GeneralThreadPool.getInstance().execute(new 인형청소());
     } catch (Exception e) {
       e.printStackTrace();
     }
   }


   private boolean isPcCk(L1PcInstance pc) {
     if (pc == null || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.isPrivateShop() || pc.noPlayerCK || pc.noPlayerck2 || !pc.isPcBuff())
       return true;
     return false;
   }

   private class checkPremiumTime
     implements Runnable {
     public void run() {
       for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
         if (PremiumTimeController.this.isPcCk(pc))
           continue;
         if (!pc.isPrivateShop() && !pc.noPlayerCK && !pc.noPlayerck2 && pc != null && !pc.isDead()) {

           int FN = Config.ServerRates.FeatherNum;
           int CLN = Config.ServerRates.FeatherNum1;
           int CAN = Config.ServerRates.FeatherNum2;
           int realPremiumNumber = 1;
           L1Clan clan = L1World.getInstance().getClan(pc.getClanid());




             String savedir = String.format("c:\uami\%s\%s", new Object[] { PremiumTimeController.access$400().format(new Date()), pc.getName() });
                    // 註解: 建立保存目錄的路徑，基於當前日期和玩家名稱
             File dir = new File(savedir);

             if (dir.exists()) {
                 // 註解: 如果目錄已存在
                 realPremiumNumber *= 1; // 註解: 實際的高級時間數值不變
             }

             if (pc.getClanid() == 0) {
                 // 註解: 如果玩家沒有公會
                 pc.getInventory().storeItem(41921, FN, true);
                 // 註解: 在玩家的庫存中存儲物品ID為 41921 的物品，數量為 FN
                 pc.sendPackets("\aC(龍的祝福:PC) 精靈的金色羽毛: (" + FN + ") 個獲得");
                 // 註解: 發送系統消息，告知玩家獲得了金色羽毛
             }

             if (clan != null) {
                 // 註解: 如果玩家有公會
                 if (clan.getCastleId() == 0 && pc.getClanid() != 0) {
                     // 註解: 如果公會沒有城堡且玩家有公會
                     pc.getInventory().storeItem(41921, CLN + FN, true);
                     // 註解: 在玩家的庫存中存儲物品ID為 41921 的物品，數量為 CLN + FN
                     pc.sendPackets("\aC(龍的祝福:PC) 精靈的金色羽毛: (" + FN + ") 個追加: (" + CLN + ") 個獲得");
                     // 註解: 發送系統消息，告知玩家獲得了額外的金色羽毛
                 }
                 if (clan.getCastleId() != 0) {
                     // 註解: 如果公會擁有城堡
                     pc.getInventory().storeItem(41921, CAN + FN, true);
                     // 註解: 在玩家的庫存中存儲物品ID為 41921 的物品，數量為 CAN + FN
                     pc.sendPackets("\aC(龍的祝福:PC) 精靈的金色羽毛: (" + FN + ") 個追加: (" + CAN + ") 個獲得");
                     // 註解: 發送系統消息，告知玩家獲得了額外的金色羽毛
                 }
             }
         }
       }
     }

     private checkPremiumTime() {} }

   private class 인형청소 implements Runnable {
     private 인형청소() {}

     public void run() {
       for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
         if (PremiumTimeController.this.isPcCk(pc)) {
           continue;
         }
         L1DollInstance doll = pc.getMagicDoll();
         if (doll != null) {

           if (doll._destroyed) {
             doll.deleteDoll();

             continue;
           }
           if (doll.getMaster() == null)
             doll.deleteDoll();
         }
       }
     }
   }

   private class checkDragonBlood
     implements Runnable {
     private checkDragonBlood() {}

     public void run() {
       int time = 0;
       for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
         if (PremiumTimeController.this.isPcCk(pc))
           continue;
         if (pc.hasSkillEffect(22015)) {
           time = pc.getSkillEffectTimeSec(22015) / 60;
           pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 82, time));
         }
         if (pc.hasSkillEffect(22016)) {
           time = pc.getSkillEffectTimeSec(22016) / 60;
           pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 85, time));
         }
         if (pc.hasSkillEffect(22060)) {
           time = pc.getSkillEffectTimeSec(22060) / 60;
           pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 88, time));
         }
       }
     }
   }
 }



 package l1j.server.server.clientpackets;

 import l1j.server.Config;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.shop.L1AdenShop;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_RetrieveSupplementaryService;
 import l1j.server.server.serverpackets.S_SurvivalCry;
 import l1j.server.server.serverpackets.S_아덴상점;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_아덴상점 extends ClientBasePacket {
   private static final String C_아덴상점 = "[C] C_亞丁商店";

   public C_아덴상점(byte[] decrypt, GameClient client) {
     super(decrypt); try {
       int i, size; L1AdenShop as;
       int j, type = readH();
       L1PcInstance pc = client.getActiveChar();
       if (pc == null)
         return;
       switch (type) {





           case 1:
               // 如果配置中的 ArdenTo 設定為 true
               if (Config.ServerAdSetting.ArdenTo) {
                   // 向玩家發送訊息，告知亞丁商店正在維護中
                   pc.sendPackets((ServerBasePacket) new S_PacketBox(84, "目前亞丁商店正在維護中，請稍後再試"));
                   pc.sendPackets((ServerBasePacket) new S_ChatPacket(pc, "目前亞丁商店正在維護中，請稍後再試", 1));
                   return;
               }

               // 如果配置中的 Adentype 設定為 true
               if (Config.ServerAdSetting.Adentype) {
                   // 向客戶端發送一系列 S_SurvivalCry 封包
                   client.sendPacket((ServerBasePacket) new S_SurvivalCry(0, pc));
                   client.sendPacket((ServerBasePacket) new S_SurvivalCry(1, pc));
                   client.sendPacket((ServerBasePacket) new S_SurvivalCry(2, pc));
                   // 向玩家發送一個 S_PacketBox 封包，包含重複消息
                   pc.sendPackets((ServerBasePacket) new S_PacketBox(84, "[重複消息] N幣請咨詢 '美提斯'"));
                   pc.sendPackets("\aG[重複消息] N幣請咨詢 美提斯");
                   break;
               }

               // 向客戶端發送一個 S_SurvivalCry(2) 封包
               client.sendPacket((ServerBasePacket) new S_SurvivalCry(2, pc));
               // 向玩家發送亞丁商店封包，使用 Adena 作為貨幣
               pc.sendPackets((ServerBasePacket) new S_亞丁商店(pc, S_亞丁商店.Currency.ADENA));
               break;

         case 4:
           for (i = 0; i < 1000; i++) {
             int ff = readH();
             if (ff == 0)
               break;
           }
           for (i = 0; i < 129; i++) {
             readC();
           }
           size = readH();
           if (size == 0) {
             return;
           }
           as = new L1AdenShop();
           for (j = 0; j < size; j++) {
             int id = readD();
             int count = readH();
             if (count <= 0 || count >= 10000) {
               return;
             }
             as.add(pc, id, count);
           }

           if (!as.BugOk() &&
             as.commit(pc)) {
             client.sendPacket((ServerBasePacket)new S_SurvivalCry(5, pc));
           }
           break;

         case 6:
           pc.sendPackets((ServerBasePacket)new S_RetrieveSupplementaryService(pc.getId(), pc));
           break;







           case 50:
               // 向玩家發送訊息，告知不需要輸入 OTP
               pc.sendPackets("\aG[OTP] 不需要輸入。");
               pc.sendPackets((ServerBasePacket) new S_PacketBox(84, "\aG[OTP] 不需要輸入。"));
               // 向客戶端發送一個 S_SurvivalCry 包
               client.sendPacket((ServerBasePacket) new S_SurvivalCry(4, pc));
               break;
       }
       } catch (Exception exception) {
           // 處理任何可能的異常
       } finally {
           // 最終處理塊
       }

       public String getType() {
           // 返回類型字串
           return "[C] C_亞丁商店";
       }
   }



 package l1j.server.server.clientpackets;

 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class C_Deposit
   extends ClientBasePacket
 {
   private static final String C_DEPOSIT = "[C] C_Deposit";

     // C_Deposit 類的構造函數，處理存款操作
     public C_Deposit(byte[] abyte0, GameClient clientthread) throws Exception {
         super(abyte0);
         int i = readD(); // 讀取玩家 ID
         int j = readD(); // 讀取存款金額

            // 獲取當前活動角色
         L1PcInstance player = clientthread.getActiveChar();
         if (player == null)
             return; // 如果角色為空，則返回

            // 檢查玩家 ID 是否匹配
         if (i == player.getId()) {
             L1Clan clan = L1World.getInstance().getClan(player.getClanid());
             if (clan != null) { // 檢查玩家是否有血盟
                 int castle_id = clan.getCastleId();

                 if (castle_id == 0) {
                     return; // 如果血盟沒有城堡，則返回
                 }

                 // 檢查玩家是否為君主並且是血盟領袖
                 if (!player.isCrown() || clan.getLeaderId() != player.getId()) {
                     return; // 如果不是，則返回
                 }

                 // 檢查攻城戰是否正在進行
                 if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
                     // 如果正在進行，發送系統消息並返回
                     player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "攻城戰期間無法進行此操作。"));
                     return;
                 }

                 if (castle_id != 0) {
                     MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
                     int money = war.getPublicMoney();

                     if (j < 0 || money <= 0) {
                         return; // 如果存款金額無效或公共資金小於等於0，則返回
                     }

                     // 檢查玩家是否有足夠的金幣
                     if (player.getInventory().consumeItem(40308, j)) {
                         war.setPublicMoney(money + j); // 更新公共資金
                         MJCastleWarBusiness.getInstance().updateCastle(castle_id); // 更新城堡資訊
                     }
                 }
             }
         }
     }


   public String getType() {
     return "[C] C_Deposit";
   }
 }



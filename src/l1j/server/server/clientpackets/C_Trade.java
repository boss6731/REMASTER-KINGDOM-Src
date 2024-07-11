 package l1j.server.server.clientpackets;

 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Trade;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.FaceToFace;




 public class C_Trade extends ClientBasePacket {
     private static final String C_TRADE = "[C] C_Trade";

     public C_Trade(byte[] abyte0, GameClient clientthread) throws Exception {
         super(abyte0);

// 獲取當前玩家實例
         L1PcInstance player = clientthread.getActiveChar();
         if (player == null || player.isGhost()) {
             return;
         }

// 檢查玩家是否在 MJShiftBattlePlayManager 中
         if (MJShiftBattlePlayManager.is_shift_battle(player)) {
             return;
         }

// 檢查玩家是否在線
         if (player.getOnlineStatus() == 0) {
             clientthread.kick();
             return;
         }

// 檢查玩家是否已經在交易中
         if (player.getTradeID() > 0) {
             S_SystemMessage sm = new S_SystemMessage("您當前正處於交易狀態。");
             player.sendPackets((ServerBasePacket)sm);
             sm = null;
             return;
         }

// 檢查玩家是否在特定地圖（ID為621）
         if (player.getMapId() == 621) {
             player.sendPackets("該地圖無法使用。");
             return;
         }

// 檢查玩家是否在特定地圖（ID為38或34）
         if (player.getMapId() == 38 || player.getMapId() == 34) {
             player.sendPackets("該地圖無法使用。");
             return;
         }

// 檢查玩家是否處於隱形狀態
         if (player.isInvisble()) {
             player.sendPackets((ServerBasePacket)new S_ServerMessage(334)); // 334: 無法在隱形狀態下進行貿易
             return;
         }

// 此處可以添加更多的檢查或後續的交易邏輯
     }
     L1PcInstance target = FaceToFace.faceToFace(player);
     if (target != null) {
       if (player.getAccountName().equalsIgnoreCase(target.getAccountName())) {
         player.sendPackets((ServerBasePacket)new S_Disconnect());
         target.sendPackets((ServerBasePacket)new S_Disconnect());
         return;
       }
       if (!target.isParalyzed()) {
         if (player.getTradeID() != 0) {
           L1Trade trade = new L1Trade();
           trade.TradeCancel(player);
         }

         if (target.getTradeID() != 0) {
           L1Trade trade = new L1Trade();
           trade.TradeCancel(target);
         }

         player.setTradeID(target.getId());
         target.setTradeID(player.getId());
         target.sendPackets((ServerBasePacket)new S_Message_YN(252, player.getName()));
       }
     }
   }



   public String getType() {
     return "[C] C_Trade";
   }
 }



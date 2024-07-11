 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.FaceToFace;




 public class C_JoinClan
   extends ClientBasePacket
 {
   private static final String C_JOIN_CLAN = "[C] C_JoinClan";

   public C_JoinClan(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null || pc.isGhost()) {
       return;
     }

     L1PcInstance target = FaceToFace.faceToFace(pc);
     if (target != null) {
       JoinClan(pc, target);
     }
   }

     private void JoinClan(L1PcInstance player, L1PcInstance target) {
            // 檢查目標是否不是王子或公主，且其公會等級不為 9
         if (!target.isCrown() && target.getClanRank() != 9) {
            // 發送系統消息通知玩家目標不是王子或公主的護衛騎士
             player.sendPackets(new S_SystemMessage(target.getName() + " 不是王子或公主護衛騎士。"));
             return; // 結束方法
         }

            // 獲取目標的公會 ID
         int clan_id = target.getClanid();
            // 如果目標沒有加入任何公會（公會 ID 為 0）
         if (clan_id == 0) {
            // 發送系統消息通知玩家目標沒有加入任何公會
             player.sendPackets(new S_ServerMessage(90, target.getName()));
             return; // 結束方法
         }

            // 獲取目標所屬的公會
         L1Clan clan = L1World.getInstance().getClan(clan_id);
            // 如果公會不存在
         if (clan == null) {
             return; // 結束方法
         }

            // 這裡應該有更進一步的邏輯處理，例如加入公會的具體操作
     }

       // 檢查 target 的公會等級是否不是 10 或 9
       if (target.getClanRank() != 10 && target.getClanRank() != 9) {
            // 發送系統消息通知玩家 target 不是王子或公主的護衛騎士
           player.sendPackets(new S_SystemMessage(target.getName() + " 不是王子或公主護衛騎士。"));
            // 結束方法
           return;
       }
     if (player.getClanid() != 0) {
       if (player.isCrown()) {
         L1Clan player_clan = L1World.getInstance().getClan(player.getClanid());
         if (player_clan == null) {
           return;
         }

         if (player.getId() != player_clan.getLeaderId()) {
           player.sendPackets((ServerBasePacket)new S_ServerMessage(89));

           return;
         }
         if (player_clan.getCastleId() != 0 || player_clan
           .getHouseId() != 0) {
           player.sendPackets((ServerBasePacket)new S_ServerMessage(665));
           return;
         }
       } else {
         player.sendPackets((ServerBasePacket)new S_ServerMessage(89));
         return;
       }
     }
     target.setTempID(player.getId());
     target.sendPackets((ServerBasePacket)new S_Message_YN(97, player.getName()));
   }


   public String getType() {
     return "[C] C_JoinClan";
   }
 }



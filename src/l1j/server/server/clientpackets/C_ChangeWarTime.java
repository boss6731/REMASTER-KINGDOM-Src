 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class C_ChangeWarTime
   extends ClientBasePacket
 {
   private static final String C_CHANGE_WAR_TIME = "[C] C_ChangeWarTime";

   public C_ChangeWarTime(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     L1PcInstance player = clientthread.getActiveChar();
     if (player == null)
       return;
     L1Clan clan = L1World.getInstance().findClan(player.getClanname());
     if (clan != null) {
       int castle_id = clan.getCastleId();
       if (castle_id != 0) {
         player.sendPackets((ServerBasePacket)new S_ServerMessage(305));
       }
     }
   }


   public String getType() {
     return "[C] C_ChangeWarTime";
   }
 }



 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;





 public class C_WarTimeList
   extends ClientBasePacket
 {
   private static final String C_WAR_TIME_LIST = "[C] C_WarTimeList";

   public C_WarTimeList(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);
     try {
       L1PcInstance pc = clientthread.getActiveChar();
       L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

       if (clan != null) {
         int castle_id = clan.getCastleId();
         if (castle_id != 0) {
           pc.sendPackets(305);
           return;
         }
       }
     } catch (Exception exception) {

     } finally {
       clear();
     }
   }


   public String getType() {
     return "[C] C_WarTimeList";
   }
 }


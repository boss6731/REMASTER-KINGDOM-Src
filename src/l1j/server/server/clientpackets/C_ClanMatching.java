 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class C_ClanMatching
   extends ClientBasePacket {
   private static final String C_CLANMATCHING = "[C] C_ClanMatching";

   public C_ClanMatching(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);
     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }
   }


   public String getType() {
     return "[C] C_ClanMatching";
   }
 }



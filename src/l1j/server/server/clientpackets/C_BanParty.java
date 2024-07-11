 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class C_BanParty
   extends ClientBasePacket
 {
   private static final String C_BAN_PARTY = "[C] C_BanParty";

   public C_BanParty(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);
     String s = readS();

     L1PcInstance player = client.getActiveChar();
     if (player == null)
       return;  if (!player.getParty().isLeader(player)) {

       player.sendPackets((ServerBasePacket)new S_ServerMessage(427));

       return;
     }
     for (L1PcInstance member : player.getParty().getMembers()) {
       if (member.getName().toLowerCase().equals(s.toLowerCase())) {
         player.getParty().kickMember(member);

         return;
       }
     }
     player.sendPackets((ServerBasePacket)new S_ServerMessage(426, s));
   }


   public String getType() {
     return "[C] C_BanParty";
   }
 }



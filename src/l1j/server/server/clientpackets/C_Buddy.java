 package l1j.server.server.clientpackets;

 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BUDDY_LIST_NOTI;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class C_Buddy
   extends ClientBasePacket {
   private static final String C_BUDDY = "[C] C_Buddy";

   public C_Buddy(byte[] abyte0, GameClient clientthread) {
     super(abyte0);
     if (clientthread == null) {
       return;
     }

     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null) {
       return;
     }

     SC_BUDDY_LIST_NOTI.send(pc);
   }




   public String getType() {
     return "[C] C_Buddy";
   }
 }



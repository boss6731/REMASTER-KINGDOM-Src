 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Horun;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Horun
   extends ClientBasePacket
 {
   private static final String C_HORUN = "[C] C_Horun";

   public C_Horun(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     int i = readD();

     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null || pc.isGhost()) {
       return;
     }
     pc.sendPackets((ServerBasePacket)new S_Horun(i, pc));
   }


   public String getType() {
     return "[C] C_Horun";
   }
 }



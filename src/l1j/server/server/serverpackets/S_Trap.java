 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.model.Instance.L1TrapInstance;


 public class S_Trap
   extends ServerBasePacket
 {
   public S_Trap(L1TrapInstance trap, String name) {
     writeC(186);
     writeH(trap.getX());
     writeH(trap.getY());
     writeD(trap.getId());
     writeH(7);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeD(0);
     writeC(0);
     writeC(0);
     writeS(name);
     writeC(0);
     writeD(0);
     writeD(0);
     writeC(255);
     writeC(0);
     writeC(0);
     writeC(0);
     writeH(65535);

     writeD(0);
     writeC(8);
     writeC(0);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }



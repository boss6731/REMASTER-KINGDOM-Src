 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1MonsterInstance;






 public class S_MoveNpcPacket
   extends ServerBasePacket
 {
   private static final String _S__1F_S_MOVENPCPACKET = "[S] S_MoveNpcPacket";

   public S_MoveNpcPacket(L1MonsterInstance npc, int x, int y, int heading) {
     writeC(211);
     writeD(npc.getId());
     writeH(x);
     writeH(y);
     writeC(heading);


     writeH(0);
   }




   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_MoveNpcPacket";
   }
 }



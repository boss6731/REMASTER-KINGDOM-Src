 package l1j.server.server.serverpackets;


 public class S_SkillIconAura
   extends ServerBasePacket
 {
   public S_SkillIconAura(int i, int j) {
     writeC(108);
     writeC(22);
     writeC(i);
     writeH(j);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }



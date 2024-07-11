 package l1j.server.server.serverpackets;


 public class S_SkillBrave
   extends ServerBasePacket
 {
   public S_SkillBrave(int i, int j, int k) {
     writeC(92);
     writeD(i);
     writeC(j);
     writeH(k);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }



 package l1j.server.server.serverpackets;


 public class S_SkillHaste
   extends ServerBasePacket
 {
   public S_SkillHaste(int i, int j, int k) {
     writeC(93);
     writeD(i);
     writeC(j);
     writeH(k);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }



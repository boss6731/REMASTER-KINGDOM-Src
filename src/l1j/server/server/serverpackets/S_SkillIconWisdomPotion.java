 package l1j.server.server.serverpackets;


 public class S_SkillIconWisdomPotion
   extends ServerBasePacket
 {
   public S_SkillIconWisdomPotion(int time) {
     writeC(108);
     writeC(57);
     writeC(44);
     writeH(time);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }



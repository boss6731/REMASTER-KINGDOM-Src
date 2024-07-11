 package l1j.server.server.serverpackets;


 public class S_SkillIconShield
   extends ServerBasePacket
 {
   public S_SkillIconShield(int type, int time) {
     writeC(69);
     writeH(time);
     writeC(type);
     writeD(0);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }



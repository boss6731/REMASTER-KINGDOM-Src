 package l1j.server.server.serverpackets;


 public class S_SkillIconWindShackle
   extends ServerBasePacket
 {
   public S_SkillIconWindShackle(int objectId, int time) {
     writeC(108);
     writeC(44);
     writeD(objectId);
     writeC(0);
     writeC(10);
     writeC(128);
   }

   public byte[] getContent() {
     return getBytes();
   }
 }



 package l1j.server.server.serverpackets;




 public class S_SkillIconBlessOfEva
   extends ServerBasePacket
 {
   public S_SkillIconBlessOfEva(int objectId, int time) {
     writeC(17);
     writeD(objectId);
     writeH(time);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }



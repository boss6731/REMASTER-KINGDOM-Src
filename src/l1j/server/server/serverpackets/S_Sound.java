 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;






 public class S_Sound
   extends ServerBasePacket
 {
   private static final String S_SOUND = "[S] S_Sound";

   public static void broadcast(L1Character cha, int id) {
     S_Sound sound = new S_Sound(id);
     if (cha instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)cha;
       pc.sendPackets(sound, false);
     }
     cha.broadcastPacket(sound);
   }

   public S_Sound(int sound) {
     buildPacket(sound);
   }

   private void buildPacket(int sound) {
     writeC(91);
     writeC(0);
     writeH(sound);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Sound";
   }
 }



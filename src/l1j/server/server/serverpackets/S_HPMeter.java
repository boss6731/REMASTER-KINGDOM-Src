     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Character;

     public class S_HPMeter
       extends ServerBasePacket {
       private static final String _typeString = "[S] S_HPMeter";

       public S_HPMeter(int objId, int hpRatio, int mpRatio) {
         buildPacket(objId, hpRatio, mpRatio);
       }

       public S_HPMeter(L1Character cha) {
         int objId = cha.getId();
         int hpRatio = 0;
         int mpRatio = 0;
         if (0 < cha.getMaxHp()) {
           hpRatio = (int)(100.0D / cha.getMaxHp() * cha.getCurrentHp());
         }

         if (0 < cha.getMaxMp()) {
           mpRatio = (int)(100.0D / cha.getMaxMp() * cha.getCurrentMp());
         }

         buildPacket(objId, hpRatio, mpRatio);
       }


       private void buildPacket(int objId, int hpRatio, int mpRatio) {
         writeC(250);
         writeD(objId);

         writeC(hpRatio);
         writeC(mpRatio);

         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_HPMeter";
       }
     }



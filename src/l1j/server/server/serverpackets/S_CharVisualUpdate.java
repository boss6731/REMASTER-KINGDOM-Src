     package l1j.server.server.serverpackets;

     import l1j.server.MJ3SEx.EActionCodes;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Character;







     public class S_CharVisualUpdate
       extends ServerBasePacket
     {
       private static final String _S__0B_S_CharVisualUpdate = "[C] S_CharVisualUpdate";

       public S_CharVisualUpdate(L1PcInstance pc) {
         writeC(193);
         writeD(pc.getId());
         writeC(pc.isDead() ? EActionCodes.death.toInt() : (pc.isSpearModeType() ? 121 : pc.getCurrentWeapon()));
         writeC(255);
         writeC(255);
         writeH(0);
       }

       public S_CharVisualUpdate(L1Character cha, int status) {
         writeC(193);
         writeD(cha.getId());
         writeC(status);
         writeC(255);
         writeC(255);
         writeH(0);
       }




       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[C] S_CharVisualUpdate";
       }
     }



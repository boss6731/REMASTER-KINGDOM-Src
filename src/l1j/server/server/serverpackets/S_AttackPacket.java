     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Character;

     public class S_AttackPacket
       extends ServerBasePacket {
       private static final String _S__1F_ATTACKPACKET = "[S] S_AttackPacket";

       public S_AttackPacket(L1Character cha, int objid, int type) {
         buildpacket(cha, objid, type);
       }

       public S_AttackPacket(L1Character cha, int objid, int type, int attacktype) {
         buildpacket(cha, objid, type, attacktype);
       }

       private void buildpacket(L1Character cha, int objid, int type) {
         writeC(5);
         writeC(type);
         writeD(objid);
         writeD(cha.getId());

         writeC(78);
         writeC(0);
         writeC(cha.getHeading());
         writeD(0);
         writeC(0);
       }

       private void buildpacket(L1Character cha, int objid, int type, int attacktype) {
         writeC(5);
         writeC(type);
         writeD(cha.getId());
         writeD(objid);
         writeH(2);
         writeC(cha.getHeading());
         writeH(0);
         writeH(0);
         writeC(attacktype);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_AttackPacket";
       }
     }



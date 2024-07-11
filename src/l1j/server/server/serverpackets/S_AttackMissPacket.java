     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Character;























     public class S_AttackMissPacket
       extends ServerBasePacket
     {
       private static final String _S__OB_ATTACKMISSPACKET = "[S] S_AttackMissPacket";

       public S_AttackMissPacket(L1Character attacker, int targetId) {
         writeC(5);
         writeC(1);
         writeD(attacker.getId());
         writeD(targetId);
         writeC(0);
         writeC(0);
         writeC(attacker.getHeading());
         writeD(0);
         writeC(0);
       }

       public S_AttackMissPacket(L1Character attacker, int targetId, int actId) {
         writeC(5);
         writeC(actId);
         writeD(attacker.getId());
         writeD(targetId);
         writeC(0);
         writeC(0);
         writeC(attacker.getHeading());
         writeD(0);
         writeC(0);
       }

       public S_AttackMissPacket(int attackId, int targetId) {
         writeC(5);
         writeC(1);
         writeD(attackId);
         writeD(targetId);
         writeC(0);
         writeC(0);
         writeC(0);
         writeD(0);
       }

       public S_AttackMissPacket(int attackId, int targetId, int actId) {
         writeC(5);
         writeC(actId);
         writeD(attackId);
         writeD(targetId);
         writeC(0);
         writeC(0);
         writeC(0);
         writeD(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_AttackMissPacket";
       }
     }



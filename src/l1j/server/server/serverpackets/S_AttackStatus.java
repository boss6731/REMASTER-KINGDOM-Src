     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

















     public class S_AttackStatus
       extends ServerBasePacket
     {
       private static final String _S__1F_ATTACKPACKET = "[S] S_AttackStatus";

       public S_AttackStatus(int attacker_id, int attacker_h, int receiver_id, int type) {
         writeC(5);
         writeC(type);
         writeD(attacker_id);
         writeD(receiver_id);
         writeC(1);
         writeC(0);
         writeC(attacker_h);
         writeD(0);
       }

       public S_AttackStatus(L1PcInstance pc, int objid, int type) {
         buildpacket(pc, objid, type);
       }

       public S_AttackStatus(L1PcInstance pc, int objid, int type, int gfxid) {
         buildpacket(pc, type, gfxid);
       }

       private void buildpacket(L1PcInstance pc, int objid, int type) {
         writeC(5);
         writeC(type);
         writeD(pc.getId());
         writeD(objid);
         writeC(1);
         writeC(0);
         writeC(pc.getHeading());
         writeD(0);
       }

       public S_AttackStatus(L1PcInstance pc, int gfxId) {
         writeC(5);
         writeC(1);
         writeD(pc.getId());
         writeD(pc.getId());
         writeH(1);
         writeC(pc.getHeading());
         writeD(0);
         writeC(2);
         writeD(gfxId);
         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_AttackStatus";
       }
     }



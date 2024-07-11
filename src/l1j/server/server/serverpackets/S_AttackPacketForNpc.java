     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Character;



















     public class S_AttackPacketForNpc
       extends ServerBasePacket
     {
       private static final String S_ATTACK_PACKET_FOR_NPC = "[S] S_AttackPacketForNpc";

       public S_AttackPacketForNpc(L1Character cha, int npcObjectId, int type) {
         buildpacket(cha, npcObjectId, type);
       }

       private void buildpacket(L1Character cha, int npcObjectId, int type) {
         writeC(5);
         writeC(type);
         writeD(npcObjectId);
         writeD(cha.getId());
         writeH(1);
         writeC(cha.getHeading());
         writeH(0);
         writeH(0);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_AttackPacketForNpc";
       }
     }


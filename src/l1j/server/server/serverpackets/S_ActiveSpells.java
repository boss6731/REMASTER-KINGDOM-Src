         package l1j.server.server.serverpackets;

         import l1j.server.server.model.Instance.L1PcInstance;



         public class S_ActiveSpells
           extends ServerBasePacket
         {
           public S_ActiveSpells(L1PcInstance pc) {
             buildPacket(pc);
           }

           private void buildPacket(L1PcInstance pc) {
             writeC(108);
             writeC(61);
             writeC(0);
             writeD(0);
           }

           public byte[] getContent() {
             return getBytes();
           }
         }



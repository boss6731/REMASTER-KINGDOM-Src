         package l1j.server.server.serverpackets;

         import l1j.server.server.model.Instance.L1PcInstance;

         public class S_AddPartyMember
           extends ServerBasePacket {
           private static final String _TYPE = "[S] S_AddPartyMember";

           public S_AddPartyMember(L1PcInstance cha) {
             writeC(108);
             writeC(105);
             double nowhp = 0.0D;
             double maxhp = 0.0D;
             nowhp = cha.getCurrentHp();
             maxhp = cha.getMaxHp();
             writeD(cha.getId());
             writeS(cha.getName());
             writeC(1);
             writeC(0);
             writeC(0);
             writeD(cha.getMapId());
             writeC((int)(nowhp / maxhp * 100.0D));
             writeC(0);
             writeC(0);

             writeH(0);
           }


           public byte[] getContent() {
             return getBytes();
           }


           public String getType() {
             return "[S] S_AddPartyMember";
           }
         }



     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_ClanName
       extends ServerBasePacket {
       private static final String S_ClanName = "[S] S_ClanName";

       public S_ClanName(L1PcInstance pc, int emblemId, int rank) {
         writeC(110);
         writeD(pc.getId());
         writeS((rank > 0) ? pc.getClanname() : "");
         writeC(0);
         writeD(emblemId);
         writeC((rank > 0) ? rank : 0);
         writeH(0);
       }

       public S_ClanName(L1PcInstance pc) {
         writeC(110);
         writeD(pc.getId());
         writeD(0);
       }

       public S_ClanName(L1PcInstance pc, int a, int b, int c) {
         writeC(110);
         writeD(pc.getId());
         writeC(186);
         writeC(206);
         writeC(182);
         writeC(246);
         writeC(197);
         writeC(171);
         writeC(197);
         writeC(184);
         writeC(192);
         writeC(204);
         writeC(176);
         writeC(197);

         writeC(0);
         writeC(0);

         writeC(221);
         writeC(3);
         writeC(0);
         writeC(100);
         writeC(7);
         writeC(7);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_ClanName";
       }
     }



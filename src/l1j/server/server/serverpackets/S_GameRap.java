     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;



     public class S_GameRap
       extends ServerBasePacket
     {
       private static final String S_GameRanking = "[S] S_GameRap";

       public S_GameRap(L1PcInstance pc, int i) {
         buildPacket1(pc, i);
       }

       private void buildPacket1(L1PcInstance pc, int i) {
         writeC(108);
         writeC(67);
         writeH(4);
         writeH(i);
       }



       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_GameRap";
       }
     }



     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_GameOver
       extends ServerBasePacket
     {
       private static final String S_GameOver = "[S] S_GameOver";

       public S_GameOver(L1PcInstance pc) {
         buildPacket1(pc);
       }



       private void buildPacket1(L1PcInstance pc) {
         writeC(108);
         writeC(69);
         writeC(10);
         writeC(109);
         writeC(85);
         writeC(208);
         writeC(2);
         writeC(220);
       }



       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_GameOver";
       }
     }



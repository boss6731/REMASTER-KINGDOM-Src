     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;


     public class S_GameEnd
       extends ServerBasePacket
     {
       private static final String S_GameEnd = "[S] S_GameEnd";

       public S_GameEnd(L1PcInstance pc) {
         buildPacket1(pc);
       }



       private void buildPacket1(L1PcInstance pc) {
         writeC(108);
         writeC(70);
         writeC(147);
         writeC(92);
         writeC(151);
         writeC(220);
         writeC(42);
         writeC(74);
       }



       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_GameEnd";
       }
     }



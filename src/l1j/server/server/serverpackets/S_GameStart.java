     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;


     public class S_GameStart
       extends ServerBasePacket
     {
       private static final String S_GameStart = "[S] S_GameStart";

       public S_GameStart(L1PcInstance pc) {
         buildPacket1(pc);
       }

       private void buildPacket1(L1PcInstance pc) {
         writeC(108);
         writeC(64);
         writeC(5);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(0);
       }

       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_GameStart";
       }
     }



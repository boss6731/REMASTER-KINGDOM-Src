     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_Horun extends ServerBasePacket {
       private static final String _S__1B_HORUN = "[S] S_Horun";

       public S_Horun(int o, L1PcInstance pc) {
         writeC(175);
         writeC(8);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(1);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(2);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(3);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(4);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(5);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(6);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(7);
         writeC(0);
         writeC(0);
         writeC(0);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_Horun";
       }
     }



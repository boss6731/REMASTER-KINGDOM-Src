     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_HorunOK extends ServerBasePacket {
       private static final String _S__1B_HORUNOK = "[S] S_HorunOK";

       public S_HorunOK(int type, L1PcInstance pc) {
         writeC(175);
         writeC(type);
         writeC(0);
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
         return "[S] S_HorunOK";
       }
     }



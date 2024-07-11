     package l1j.server.server.serverpackets;

     public class S_Disconnect
       extends ServerBasePacket {
       private static S_Disconnect _disconnect;

       public static S_Disconnect get() {
         if (_disconnect == null)
           _disconnect = new S_Disconnect();
         return _disconnect;
       }

       public S_Disconnect() {
         int content = 500;
         writeC(245);
         writeH(content);
         writeD(0);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }



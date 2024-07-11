     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_GameTime1
       extends ServerBasePacket
     {
       private static final String S_GameTime1 = "[S] S_GameTime1";

       public S_GameTime1(L1PcInstance pc) {
         buildPacket(pc);
       }

       private void buildPacket(L1PcInstance pc) {
         writeC(108);
         writeC(65);
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
         return "[S] S_GameTime1";
       }
     }



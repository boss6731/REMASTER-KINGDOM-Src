     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;



     public class S_GameRanking
       extends ServerBasePacket
     {
       private static final String S_GameRanking = "[S] S_GameRanking";

       public S_GameRanking(L1PcInstance pc) {
         buildPacket1(pc);
       }

       private void buildPacket1(L1PcInstance pc) {
         writeC(108);
         writeC(68);
         writeS(pc.getName());
         writeC(154);
         writeC(247);
         writeC(1);
         writeC(0);
       }



       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_GameRanking";
       }
     }



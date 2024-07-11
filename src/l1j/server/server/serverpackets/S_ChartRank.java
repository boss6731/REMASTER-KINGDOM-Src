     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.util.ArrayList;
     import l1j.server.MJKDASystem.MJKDA;
     import l1j.server.MJKDASystem.MJKDALoadManager;

     public class S_ChartRank
       extends ServerBasePacket
     {
       private static final int SC_BASECAMP_CHART_NOTI_PACKET = 127;

       public static S_ChartRank getZero() {
         S_ChartRank s = new S_ChartRank();
         s.writeC(16);
         s.writeC(0);
         s.writeH(0);
         return s;
       }

       public static S_ChartRank get(ArrayList<MJKDA> list) throws Exception {
         S_ChartRank s = new S_ChartRank();

         int size = list.size();
         int cnt = 0;
         for (int i = 0; i < size; i++) {
           MJKDA kda = list.get(i);
           if (kda != null) {


             if (++cnt > 10 || kda.kill <= 0) {
               break;
             }
             s.writeC(10);
             byte[] data = kda.serialize();
             s.writeBit(data.length);
             s.writeByte(data);
           }
         }  s.writeC(16);
         s.writeBit(MJKDALoadManager.KDA_TOTAL_PVP);
         s.writeH(0);
         return s;
       }

       private S_ChartRank() {
         writeC(19);
         writeH(127);
       }


       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }



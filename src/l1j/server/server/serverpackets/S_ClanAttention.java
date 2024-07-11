     package l1j.server.server.serverpackets;

     import javolution.util.FastTable;




     public class S_ClanAttention
       extends ServerBasePacket
     {
       private static final String S_ClanAttention = "[S] S_ClanAttention";

       public S_ClanAttention() {
         writeC(78);
         writeH(2);
         writeD(0);
       }

       public S_ClanAttention(int i) {
         writeC(78);
         writeH(i);
       }


       public S_ClanAttention(boolean onoff, String clanname) {
         writeC(78);
         writeC(onoff ? 32 : 31);
         writeH(269);
         writeS(clanname);
       }

       public S_ClanAttention(int count, FastTable<String> attentionList) {
         writeC(78);
         writeH(2);
         writeD(count);
         for (String name : attentionList) {
           writeS(name);
         }
       }

       public S_ClanAttention(String name) {
         writeC(78);
         writeH(2);
         writeD(1);
         writeS(name);
       }




       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_ClanAttention";
       }
     }



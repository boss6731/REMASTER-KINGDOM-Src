     package l1j.server.server.serverpackets;


     public class S_Bookmarks
       extends ServerBasePacket
     {
       private static final String _S__1F_S_Bookmarks = "[S] S_Bookmarks";

       public S_Bookmarks(String name, int map, int x, int y, int bookid) {
         buildPacket(name, map, x, y, bookid);
       }

       private void buildPacket(String name, int map, int x, int y, int bookid) {
         writeC(2);
         writeS(name);
         writeH(map);
         writeH(x);
         writeH(y);
         writeD(bookid);
         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_Bookmarks";
       }
     }



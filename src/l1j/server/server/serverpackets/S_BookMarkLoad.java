     package l1j.server.server.serverpackets;

     import java.util.logging.Level;
     import java.util.logging.Logger;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.templates.L1BookMark;


     public class S_BookMarkLoad
       extends ServerBasePacket
     {
       private static final String S_BookMarkLoad = "[S] S_BookmarkLoad";
       private static Logger _log = Logger.getLogger(S_BookMarkLoad.class.getName());

       public S_BookMarkLoad(L1PcInstance pc) {
         try {
           int size = pc._bookmarks.size();
           int fastsize = pc._speedbookmarks.size();
           int booksize = pc.getMark_count() + 6;
           int tempsize = booksize - 1 - size - fastsize;
           writeC(43);
           writeC(42);
           writeC(booksize);
           writeC(0);
           writeC(2);
           if (size > 0) {
             for (int j = 0; j < size; j++) {
               writeC(j);
             }
           }

           if (fastsize > 0) {
             for (int j = 0; j < fastsize; j++) {
               writeC(((L1BookMark)pc._speedbookmarks.get(j)).getNumId());
             }
           }

           if (tempsize > 0) {
             for (int j = 0; j < tempsize; j++) {
               writeC(255);
             }
           }


           writeH(pc.getMark_count());
           writeH(size);
           for (int i = 0; i < size; i++) {
             writeD(((L1BookMark)pc._bookmarks.get(i)).getNumId());
             writeS(((L1BookMark)pc._bookmarks.get(i)).getName());
             writeH(((L1BookMark)pc._bookmarks.get(i)).getMapId());
             writeH(((L1BookMark)pc._bookmarks.get(i)).getLocX());
             writeH(((L1BookMark)pc._bookmarks.get(i)).getLocY());
           }
         } catch (Exception e) {
           _log.log(Level.WARNING, "S_書籤負載 發生異常.", e);
         } finally {}
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_BookmarkLoad";
       }
     }



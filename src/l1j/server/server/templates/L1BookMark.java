 package l1j.server.server.templates;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.LinkedList;
 import java.util.List;
 import l1j.server.MJTemplate.MJObjectWrapper;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Bookmarks;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class L1BookMark
 {
   private int _charId;
   private int _id;
   private String _name;
   private int _locX;
   private int _locY;

   public int getSpeed_id() {
     return this._speed_id;
   }
   private short _mapId; private int _randomX; private int _randomY; private int _speed_id; private int _NumId; private int _Temp_id;
   public void setSpeed_id(int i) {
     this._speed_id = i;
   }

   public int getId() {
     return this._id;
   }

   public void setId(int i) {
     this._id = i;
   }

   public int getCharId() {
     return this._charId;
   }



   public int getNumId() {
     return this._NumId;
   }

   public void setNumId(int i) {
     this._NumId = i;
   }



   public int getTemp_id() {
     return this._Temp_id;
   }

   public void setTemp_id(int i) {
     this._Temp_id = i;
   }

   public void setCharId(int i) {
     this._charId = i;
   }

   public String getName() {
     return this._name;
   }

   public void setName(String s) {
     this._name = s;
   }

   public int getLocX() {
     return this._locX;
   }

   public void setLocX(int i) {
     this._locX = i;
   }

   public int getLocY() {
     return this._locY;
   }

   public void setLocY(int i) {
     this._locY = i;
   }

   public short getMapId() {
     return this._mapId;
   }

   public void setMapId(short i) {
     this._mapId = i;
   }

   public int getRandomX() {
     return this._randomX;
   }

   public void setRandomX(int i) {
     this._randomX = i;
   }

   public int getRandomY() {
     return this._randomY;
   }

   public void setRandomY(int i) {
     this._randomY = i;
   }




   public static void bookmarkDB(final L1PcInstance pc) {
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(0);
     Selector.exec("select * from character_teleport where char_id=? order by num_id asc", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, pc.getId());
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               int i = ((Integer)wrapper.value).intValue();
               L1BookMark bookmark = new L1BookMark();
               bookmark.setCharId(rs.getInt("char_id"));
               bookmark.setId(rs.getInt("id"));
               bookmark.setNumId(i);
               bookmark.setTemp_id(i);
               bookmark.setSpeed_id(rs.getInt("speed_id"));
               bookmark.setName(rs.getString("name"));
               bookmark.setLocX(rs.getInt("locx"));
               bookmark.setLocY(rs.getInt("locy"));
               bookmark.setMapId(rs.getShort("mapid"));
               bookmark.setRandomX(rs.getShort("randomX"));
               bookmark.setRandomY(rs.getShort("randomY"));
               pc._bookmarks.add(bookmark);
               wrapper.value = Integer.valueOf(((Integer)wrapper.value).intValue() + 1);
             }
           }
         });
     Selector.exec("SELECT * FROM character_teleport WHERE char_id=? AND speed_id > -1 ORDER BY speed_id ASC", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, pc.getId());
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               if (rs.getInt("num_id") < pc._bookmarks.size() && pc._bookmarks.get(rs.getInt("num_id")) != null)
                 pc._speedbookmarks.add(pc._bookmarks.get(rs.getInt("num_id")));
             }
           }
         });
   }

   public static void deleteBookmark(final L1PcInstance pc, String s) {
     final L1BookMark book = pc.getBookMark(s);
     if (book == null) {
       return;
     }
     Updator.exec("DELETE FROM character_teleport WHERE id=? AND char_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, book.getId());
             pstm.setInt(2, pc.getId());
           }
         });
     int del_id = book.getNumId();
     for (L1BookMark books : pc.getBookMarkArray()) {
       if (book == books) {
         pc._bookmarks.remove(book);
       }
       if (books.getNumId() > del_id) {
         books.setNumId(books.getNumId() - 1);
         books.setTemp_id(books.getTemp_id() - 1);
       }
     }
     if (pc._speedbookmarks.contains(book)) {
       del_id = book.getSpeed_id();
       for (L1BookMark books : pc.getSpeedBookMarkArray()) {
         if (book == books) {
           pc._speedbookmarks.remove(book);
         }
         if (books.getSpeed_id() > del_id) {
           books.setSpeed_id(books.getSpeed_id() - 1);
         }
       }
     }

     WriteBookmark(pc);
     pc._bookmarks.clear();
     pc._speedbookmarks.clear();
     bookmarkDB(pc);
   }

   public static void addBookmark(L1PcInstance pc, String s) {
     if (!pc.getMap().isMarkable() && !pc.isGm()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(214));
       return;
     }
     if (pc._bookmarks.size() >= pc.getMark_count()) {
       if (pc.getMark_count() == 100) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(2930));
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(676));
       }
       return;
     }
     if (pc.getBookMark(s) != null) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(1655));

       return;
     }
     final L1BookMark bookmark = new L1BookMark();
     bookmark.setId(L1BookMarkIdFactory.getInstance().next_id());
     int num_id = get_next_numid(pc.getId());
     bookmark.setNumId(num_id);
     bookmark.setTemp_id(num_id);
     bookmark.setSpeed_id(-1);
     bookmark.setCharId(pc.getId());
     bookmark.setName(s);
     bookmark.setLocX(pc.getX());
     bookmark.setLocY(pc.getY());
     bookmark.setMapId(pc.getMapId());
     Updator.exec("INSERT INTO character_teleport SET id=?,num_id=?,speed_id=?, char_id=?, name=?, locx=?, locy=?, mapid=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, bookmark.getId());
             pstm.setInt(2, bookmark.getNumId());
             pstm.setInt(3, bookmark.getSpeed_id());
             pstm.setInt(4, bookmark.getCharId());
             pstm.setString(5, bookmark.getName());
             pstm.setInt(6, bookmark.getLocX());
             pstm.setInt(7, bookmark.getLocY());
             pstm.setInt(8, bookmark.getMapId());
           }
         });
     pc._bookmarks.add(bookmark);
     pc.sendPackets((ServerBasePacket)new S_Bookmarks(s, bookmark.getMapId(), bookmark.getLocX(), bookmark.getLocY(), bookmark.getNumId()));
   }

   public static void addBookmark(final L1PcInstance pc, final List<L1BookMark> bookmarks) {
     if (bookmarks.size() <= 0) {
       return;
     }
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(get_next_numid(pc.getId()));
     Updator.batch("INSERT INTO character_teleport SET id=?,num_id=?,speed_id=?, char_id=?, name=?, locx=?, locy=?, mapid=?", new BatchHandler()
         {
           public void handle(PreparedStatement pstm, int callNumber) throws Exception {
             L1BookMark bookmark = bookmarks.get(callNumber);
             bookmark.setId(L1BookMarkIdFactory.getInstance().next_id());
             bookmark.setNumId(((Integer)wrapper.value).intValue());
             bookmark.setTemp_id(((Integer)wrapper.value).intValue());
             bookmark.setCharId(pc.getId());
             wrapper.value = Integer.valueOf(((Integer)wrapper.value).intValue() + 1);

             pstm.setInt(1, bookmark.getId());
             pstm.setInt(2, bookmark.getNumId());
             pstm.setInt(3, bookmark.getSpeed_id());
             pstm.setInt(4, bookmark.getCharId());
             pstm.setString(5, bookmark.getName());
             pstm.setInt(6, bookmark.getLocX());
             pstm.setInt(7, bookmark.getLocY());
             pstm.setInt(8, bookmark.getMapId());
             pc._bookmarks.add(bookmark);
             pc.sendPackets((ServerBasePacket)new S_Bookmarks(bookmark.getName(), bookmark.getMapId(), bookmark.getLocX(), bookmark.getLocY(), bookmark.getNumId()));
           }
         }bookmarks.size());
   }

   public static void WriteBookmark(final L1PcInstance pc) {
     int size = pc._bookmarks.size();
     if (size > 0) {
       Updator.batch("UPDATE character_teleport SET num_id=?, speed_id=?  WHERE id=?", new BatchHandler()
           {
             public void handle(PreparedStatement pstm, int callNumber) throws Exception {
               L1BookMark book = pc._bookmarks.get(callNumber);
               pstm.setInt(1, book.getTemp_id());
               pstm.setInt(2, pc._speedbookmarks.contains(book) ? book.getSpeed_id() : -1);
               pstm.setInt(3, book.getId());
             }
           }size);
     }
   }

   public static void Bookmarkitem(final L1PcInstance pc, L1ItemInstance useItem, int obj_id, boolean isChange) {
     final List<L1BookMark> bookmarks = ShowBookmarkitem(pc, obj_id);
     if (pc.getBookMarkSize() + bookmarks.size() > pc.getMark_count()) {
       pc.sendPackets(2937);
       return;
     }
     int size = bookmarks.size();
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(get_next_numid(pc.getId()));

     if (size > 0) {
       Updator.batch("insert ignore into character_teleport set id=?, num_id=?, speed_id=?, char_id=?, name=?, locx=?, locy=?, mapid=?, randomX=?, randomY=?", new BatchHandler()
           {
             public void handle(PreparedStatement pstm, int callNumber) throws Exception {
               int idx = 0;
               L1BookMark book = bookmarks.get(callNumber);
               pstm.setInt(++idx, book.getId());
               Integer integer = (Integer)wrapper.value; MJObjectWrapper mJObjectWrapper = wrapper; Object object = mJObjectWrapper.value = Integer.valueOf(((Integer)mJObjectWrapper.value).intValue() + 1); pstm.setInt(++idx, integer.intValue());
               pstm.setInt(++idx, book.getSpeed_id());
               pstm.setInt(++idx, book.getCharId());
               pstm.setString(++idx, book.getName());
               pstm.setInt(++idx, book.getLocX());
               pstm.setInt(++idx, book.getLocY());
               pstm.setInt(++idx, book.getMapId());
               pstm.setInt(++idx, book.getRandomX());
               pstm.setInt(++idx, book.getRandomY());
               pc.addBookMark(book);
               pc.sendPackets((ServerBasePacket)new S_Bookmarks(book.getName(), book.getMapId(), book.getLocX(), book.getLocY(), book.getId()));
             }
           }size);
     }
     pc.getInventory().removeItem(useItem, 1);
   }

   public static List<L1BookMark> ShowBookmarkitem(final L1PcInstance pc, final int obj_id) {
     final List<L1BookMark> bookmarks = new LinkedList<>();
     Selector.exec("SELECT * FROM beginner_addteleport WHERE item_obj_id=? ORDER BY num_id ASC", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, obj_id);
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               L1BookMark bookmark = new L1BookMark();
               bookmark.setId(L1BookMarkIdFactory.getInstance().next_id());
               bookmark.setNumId(rs.getInt("num_id"));
               bookmark.setSpeed_id(-1);
               bookmark.setCharId(pc.getId());
               bookmark.setName(rs.getString("name"));
               bookmark.setLocX(rs.getInt("locx"));
               bookmark.setLocY(rs.getInt("locy"));
               bookmark.setMapId(rs.getShort("mapid"));
               bookmark.setRandomX(rs.getInt("randomX"));
               bookmark.setRandomY(rs.getInt("randomY"));
               bookmarks.add(bookmark);
             }
           }
         });

     return bookmarks;
   }

   public static void deleteBookmarkItem(final int obj_id) {
     Updator.exec("DELETE FROM character_teleport WHERE item_obj_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, obj_id);
           }
         });
   }

   public static void deleteBookmarkItem(final L1PcInstance pc) {
     Updator.exec("DELETE FROM character_teleport WHERE char_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, pc.getId());
           }
         });
   }

   public static void MakeBookmarkItem(final L1PcInstance pc, final L1ItemInstance item) {
     final int num_id = get_next_numid(pc.getId());
     int size = pc._speedbookmarks.size();
     if (size > 0) {
       Updator.batch("insert ignore into character_teleport SET id=?,num_id=?,speed_id=?, char_id=?, name=?, locx=?, locy=?, mapid=?,randomX=?,randomY=?,item_obj_id=?", new BatchHandler()
           {
             public void handle(PreparedStatement pstm, int callNumber) throws Exception {
               int i = callNumber;
               pstm.setInt(1, L1BookMarkIdFactory.getInstance().next_id());
               pstm.setInt(2, i + num_id);
               pstm.setInt(3, -1);
               pstm.setInt(4, 0);
               pstm.setString(5, ((L1BookMark)pc._speedbookmarks.get(i)).getName());
               pstm.setInt(6, ((L1BookMark)pc._speedbookmarks.get(i)).getLocX());
               pstm.setInt(7, ((L1BookMark)pc._speedbookmarks.get(i)).getLocY());
               pstm.setInt(8, ((L1BookMark)pc._speedbookmarks.get(i)).getMapId());
               pstm.setInt(9, ((L1BookMark)pc._speedbookmarks.get(i)).getRandomX());
               pstm.setInt(10, ((L1BookMark)pc._speedbookmarks.get(i)).getRandomY());
               pstm.setInt(11, item.getId());
             }
           }size);
     }
   }

   public static int get_next_numid(final int object_id) {
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(0);
     Selector.exec("SELECT max(num_id)+1 as newid FROM character_teleport WHERE char_id=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, object_id);
           }


           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               wrapper.value = Integer.valueOf(rs.getInt("newid"));
             }
           }
         });
     return ((Integer)wrapper.value).intValue();
   }

   public static void addBookmarkItem(L1PcInstance pc, L1ItemBookMark item) {
     final L1BookMark bookmark = new L1BookMark();
     int num_id = get_next_numid(pc.getId());
     bookmark.setId(L1BookMarkIdFactory.getInstance().next_id());
     bookmark.setNumId(num_id);
     bookmark.setTemp_id(num_id);
     bookmark.setSpeed_id(-1);
     bookmark.setCharId(pc.getId());
     bookmark.setName(item.getName());
     bookmark.setLocX(item.getLocX());
     bookmark.setLocY(item.getLocY());
     bookmark.setMapId(item.getMapId());
     Updator.exec("INSERT INTO character_teleport SET id=?,num_id=?,speed_id=?, char_id=?, name=?, locx=?, locy=?, mapid=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, bookmark.getId());
             pstm.setInt(2, bookmark.getNumId());
             pstm.setInt(3, bookmark.getSpeed_id());
             pstm.setInt(4, bookmark.getCharId());
             pstm.setString(5, bookmark.getName());
             pstm.setInt(6, bookmark.getLocX());
             pstm.setInt(7, bookmark.getLocY());
             pstm.setInt(8, bookmark.getMapId());
           }
         });
     pc._bookmarks.add(bookmark);
   }
 }



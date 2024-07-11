     package l1j.server.server.templates;
     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.util.logging.Level;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.utils.SQLUtil;

     public class L1ItemBookMark {
       private int _charId;
       private int _id;
       private String _name;
       private int _locX;
       private int _locY;
       private short _mapId;
       private static Logger _log = Logger.getLogger(L1ItemBookMark.class.getName());

       private int _randomX;

       private int _randomY;

       private int _speed_id;

       private int _NumId;

       private int _Temp_id;

       public int getSpeed_id() {
         return this._speed_id;
       }
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




       public static void bookmarItemkDB(L1PcInstance pc, L1ItemInstance item) {
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         L1ItemBookMark bookmark = null;
         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("SELECT * FROM character_teleport_item WHERE item_id='" + item.getId() + "' ORDER BY num_id ASC");
           rs = pstm.executeQuery();
           int i = 0;
           while (rs.next()) {
             bookmark = new L1ItemBookMark();
             bookmark.setCharId(rs.getInt("item_id"));
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
             item.addBookMark(bookmark);
             i++;
           }
         } catch (Exception e) {
           _log.log(Level.WARNING, "書籤發生異常.", e);
         } finally {
           SQLUtil.close(rs, pstm, con);
         }
       }


       public static synchronized void addBookmark(L1PcInstance pc, L1ItemInstance item, L1BookMark book) {
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("SELECT max(id)+1 as newid FROM character_teleport_item");
           rs = pstm.executeQuery();
           rs.next();
           int id = rs.getInt("newid");
           SQLUtil.close(rs, pstm);
           L1ItemBookMark items = new L1ItemBookMark();
           items.setId(id);
           pstm = con.prepareStatement("SELECT max(num_id)+1 as newid FROM character_teleport_item WHERE item_id=?");
           pstm.setInt(1, pc.getId());
           rs = pstm.executeQuery();
           rs.next();
           int Numid = rs.getInt("newid");
           SQLUtil.close(rs, pstm);
           items.setNumId(Numid);
           items.setTemp_id(Numid);
           items.setSpeed_id(-1);
           items.setCharId(item.getId());
           items.setName(book.getName());
           items.setLocX(book.getLocX());
           items.setLocY(book.getLocY());
           items.setMapId(book.getMapId());
           pstm = con.prepareStatement("INSERT INTO character_teleport_item SET id=?,num_id=?,speed_id=?, item_id=?, name=?, locx=?, locy=?, mapid=?");
           pstm.setInt(1, items.getId());
           pstm.setInt(2, items.getNumId());
           pstm.setInt(3, items.getSpeed_id());
           pstm.setInt(4, items.getCharId());
           pstm.setString(5, items.getName());
           pstm.setInt(6, items.getLocX());
           pstm.setInt(7, items.getLocY());
           pstm.setInt(8, items.getMapId());
           pstm.execute();
           item._bookmarks.add(items);
         }
         public static void deleteBookmarkItem(int itemid) {
           Connection con = null;
           PreparedStatement pstm = null;
           try {
            // 獲取數據庫連接
             con = L1DatabaseFactory.getInstance().getConnection();

            // 準備 SQL DELETE 語句
             pstm = con.prepareStatement("DELETE FROM character_teleport_item WHERE item_id=?");

            // 設置 SQL 語句中的參數 item_id
             pstm.setInt(1, itemid);

            // 執行 SQL 語句
             pstm.execute();
           } catch (Exception e) {
            // 當發生異常時，記錄錯誤信息
             _log.log(Level.SEVERE, "添加書籤時發生錯誤。", e);
           } finally {
            // 關閉 PreparedStatement 和 Connection 以釋放資源
             SQLUtil.close(pstm);
             SQLUtil.close(con);
           }
         }

       public static void deleteBookmarkItem(int itemid) {
         Connection con = null;
         PreparedStatement pstm = null;
         try {
           // 獲取數據庫連接
           con = L1DatabaseFactory.getInstance().getConnection();

           // 準備 SQL DELETE 語句
           pstm = con.prepareStatement("DELETE FROM character_teleport_item WHERE item_id=?");

           // 設置 SQL 語句中的參數 item_id
           pstm.setInt(1, itemid);

           // 執行 SQL 語句
           pstm.execute();
         } catch (SQLException e) {
           // 當發生 SQL 異常時，記錄錯誤信息
           _log.log(Level.SEVERE, "刪除書籤時發生錯誤。", e);
         } finally {
           // 關閉 PreparedStatement 和 Connection 以釋放資源
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
       }



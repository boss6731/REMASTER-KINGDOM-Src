 package l1j.server.server.command;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1Command;
 import l1j.server.server.utils.SQLUtil;

 public class L1Commands
 {
   private static Logger _log = Logger.getLogger(L1Commands.class.getName());

   private static L1Command fromResultSet(ResultSet rs) throws SQLException {
     return new L1Command(rs.getString("name"), rs.getInt("access_level"), rs.getString("class_name"));
   }




     public static L1Command get(String name) {
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
// 從 L1DatabaseFactory 獲取資料庫連接
             con = L1DatabaseFactory.getInstance().getConnection();

// 預備 SQL 查詢，根據 name 取得 commands 表中的記錄
             pstm = con.prepareStatement("SELECT * FROM commands WHERE name=?");
             pstm.setString(1, name);

// 執行查詢並獲取結果集
             rs = pstm.executeQuery();

// 如果結果集中沒有記錄，返回 null
             if (!rs.next()) {
                 return null;
             }

// 從結果集中獲取 L1Command 對象並返回
             return fromResultSet(rs);
         } catch (Exception e) {
// 如果發生異常，記錄錯誤訊息和異常細節
             _log.log(Level.SEVERE, "取得指令時發生錯誤", e);
         } finally {
// 關閉結果集、預備語句和連接，釋放資源
             SQLUtil.close(rs);
             SQLUtil.close(pstm);
             SQLUtil.close(con);
         }

// 如果發生異常或沒有找到記錄，返回 null
         return null;
     }

   public static List<L1Command> availableCommandList(int accessLevel) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     List<L1Command> result = new ArrayList<>();
       try {
           // 從 L1DatabaseFactory 獲取資料庫連接
           con = L1DatabaseFactory.getInstance().getConnection();

           // 預備 SQL 查詢，根據 access_level 取得 commands 表中的記錄
           pstm = con.prepareStatement("SELECT * FROM commands WHERE access_level <= ?");
           pstm.setInt(1, accessLevel);

           // 執行查詢並獲取結果集
           rs = pstm.executeQuery();

           // 遍歷結果集，將每個記錄轉換並添加到結果列表中
           while (rs.next()) {
               result.add(fromResultSet(rs));
           }
       } catch (Exception e) {
           // 如果發生異常，記錄錯誤訊息和異常細節
           _log.log(Level.SEVERE, "取得指令時發生錯誤", e);
       } finally {
           // 關閉結果集、預備語句和連接，釋放資源
           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
       }

// 返回結果列表
       return result;
   }



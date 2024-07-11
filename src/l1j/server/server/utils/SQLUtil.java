 package l1j.server.server.utils;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import l1j.server.L1DatabaseFactory;

 public class SQLUtil
 {
   public static SQLException close(Connection con) {
     try {
       if (con != null) {
         con.close();
       }
     } catch (SQLException e) {
       return e;
     }
     return null;
   }

   public static SQLException close(Statement ps) {
     try {
       if (ps != null) {
         ps.close();
       }
     } catch (SQLException e) {
       return e;
     }
     return null;
   }

   public static SQLException close(ResultSet rs) {
     try {
       if (rs != null) {
         rs.close();
       }
     } catch (SQLException e) {
       return e;
     }
     return null;
   }

   public static void close(ResultSet rs, Statement pstm) {
     close(rs);
     close(pstm);
   }

   public static void close(Statement pstm, Connection con) {
     close(pstm);
     close(con);
   }

   public static void close(ResultSet rs, Statement pstm, Connection con) {
     close(rs);
     close(pstm);
     close(con);
   }


   public static int calcRows(ResultSet rs) throws SQLException {
     rs.last();
     int r = rs.getRow();
     rs.beforeFirst();
     return r;
   }

   public static boolean execute(String sql, Object[] args) {
     Connection con = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       boolean bool = execute(con, sql, args);
       return bool;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       close(con);
     }
     return false;
   }

   public static boolean execute(Connection con, String sql, Object[] args) {
     PreparedStatement pstm = null;
     try {
       pstm = con.prepareStatement(sql);
       setupPrepareStatement(pstm, args);
       boolean bool = pstm.execute();
       return bool;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       close(pstm);
     }
     return false;
   }

   private static void setupPrepareStatement(PreparedStatement pstm, Object[] args) throws SQLException {
     for (int i = 0; i < args.length; i++)
       pstm.setObject(i + 1, args[i]);
   }
 }



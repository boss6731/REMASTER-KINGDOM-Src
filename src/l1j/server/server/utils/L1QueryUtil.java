 package l1j.server.server.utils;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.L1DatabaseFactory;


 public class L1QueryUtil
 {
   private static void setupPrepareStatement(PreparedStatement pstm, Object[] args) throws SQLException {
     for (int i = 0; i < args.length; i++) {
       pstm.setObject(i + 1, args[i]);
     }
   }


   public static <T> List<T> selectAll(EntityFactory<T> factory, String sql, Object[] args) {
     List<T> result = new ArrayList<>();
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement(sql);
       setupPrepareStatement(pstm, args);
       rs = pstm.executeQuery();
       while (rs.next()) {
         T entity = factory.fromResultSet(rs);
         if (entity == null)
           throw new NullPointerException(String.format("%s returned null.", new Object[] { factory.getClass().getSimpleName() }));
         result.add(entity);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return result;
   }

   public static boolean execute(Connection con, String sql, Object[] args) {
     boolean bool = false;
     PreparedStatement pstm = null;

     try { pstm = con.prepareStatement(sql);
       setupPrepareStatement(pstm, args);
       bool = pstm.execute();
       return bool; }
     catch (SQLException e)
     {
       try { throw new SQLException(e); }
       catch (SQLException e1)

       { e1.printStackTrace();


         SQLUtil.close(pstm); }  } finally { SQLUtil.close(pstm); }

     return bool;
   }

   public static boolean execute(String sql, Object[] args) {
     Connection con = null;
     boolean bool = false;

     try { con = L1DatabaseFactory.getInstance().getConnection();
       bool = execute(con, sql, args);
       return bool; }
     catch (SQLException e)
     {
       try { throw new SQLException(e); }
       catch (SQLException e1)

       { e1.printStackTrace();


         SQLUtil.close(con); }  } finally { SQLUtil.close(con); }

     return bool;
   }

   public static interface EntityFactory<T> {
     T fromResultSet(ResultSet param1ResultSet) throws SQLException;
   }
 }



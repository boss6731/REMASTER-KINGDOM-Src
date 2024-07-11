 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;




 public class CharacterConfigTable
 {
   private static CharacterConfigTable _instance;

   public static CharacterConfigTable getInstance() {
     if (_instance == null) {
       _instance = new CharacterConfigTable();
     }
     return _instance;
   }

   public void storeCharacterConfig(int objectId, int length, byte[] data) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("INSERT INTO character_config SET object_id=?, length=?, data=?");
       pstm.setInt(1, objectId);
       pstm.setInt(2, length);
       pstm.setBytes(3, data);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void updateCharacterConfig(int objectId, int length, byte[] data) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE character_config SET length=?, data=? WHERE object_id=?");
       pstm.setInt(1, length);
       pstm.setBytes(2, data);
       pstm.setInt(3, objectId);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void deleteCharacterConfig(int objectId) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("DELETE FROM character_config WHERE object_id=?");
       pstm.setInt(1, objectId);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public int countCharacterConfig(int objectId) {
     int result = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT count(*) as cnt FROM character_config WHERE object_id=?");
       pstm.setInt(1, objectId);
       rs = pstm.executeQuery();
       if (rs.next()) {
         result = rs.getInt("cnt");
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
 }



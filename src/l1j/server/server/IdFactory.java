 package l1j.server.server;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;


 public class IdFactory
 {
   private int _curId;
   private Object _monitor = new Object();

   private static final int FIRST_ID = 268435456;

   private static IdFactory _instance = new IdFactory();

   private IdFactory() {
     loadState();
   }

   public static IdFactory getInstance() {
     return _instance;
   }

   public int nextId() {
     synchronized (this._monitor) {
       return this._curId++;
     }
   }


   private void loadState() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select max(id)+1 as nextid from (select id from character_items union all select id from character_teleport union all select id from character_warehouse union all select id from character_elf_warehouse union all select objid as id from characters union all select clan_id as id from clan_data union all select id from clan_warehouse union all select objid as id from pets union all select itemObjId as id from character_favorbook union all select object_id as id from companion_instance) t");










       rs = pstm.executeQuery();

       int id = 0;
       if (rs.next()) {
         id = rs.getInt("nextid");
       }
       if (id < 268435456) {
         id = 268435456;
       }
       this._curId = id;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }



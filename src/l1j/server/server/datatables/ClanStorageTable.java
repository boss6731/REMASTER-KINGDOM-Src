 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.SQLUtil;


 public class ClanStorageTable
 {
   private static ClanStorageTable _instance;

   public static ClanStorageTable getInstance() {
     if (_instance == null) {
       _instance = new ClanStorageTable();
     }
     return _instance;
   }
   private Map<String, ArrayList<String>> _storage_list = new HashMap<>();

   private ClanStorageTable() {
     loadClanStorage();
   }

   private void loadClanStorage() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       ArrayList<String> listarray = new ArrayList<>();
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM clan_storage");
       rs = pstm.executeQuery();
       while (rs.next()) {
         String clanname = rs.getString("clan_name");
         String list = rs.getString("allow_list");

         String[] array = list.split(",");
         for (int i = 0; i < array.length; i++) {
           listarray.add(array[i]);
         }

         this._storage_list.put(clanname, listarray);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public ArrayList<String> get_StorageList(L1PcInstance pc) {
     String clanname = pc.getClanname();
     if (this._storage_list.get(clanname) != null) {
       return this._storage_list.get(clanname);
     }
     return null;
   }
   public boolean is_ClanStorageUse(L1PcInstance pc, String name) {
     String clanname = pc.getClanname();
     if (this._storage_list.get(clanname) == null) {
       return false;
     }
     if (((ArrayList)this._storage_list.get(clanname)).contains(pc.getName())) {
       return true;
     }
     return false;
   }


   public void add_Storage_list(L1PcInstance pc, String name) {
     if (this._storage_list.get(pc.getClanname()) == null) {
       ArrayList<String> allowname = new ArrayList<>();
       allowname.add(pc.getClan().getLeaderName());
       this._storage_list.put(pc.getClanname(), allowname);
       insert_Storage_List(pc);
     }

     ((ArrayList<String>)this._storage_list.get(pc.getClanname())).add(name);
     update_Storage_List(pc);
   }

   public void delete_Storage_List(L1PcInstance pc, String name) {
     if (this._storage_list.get(pc.getClanname()) != null &&
       is_ClanStorageUse(pc, name)) {
       while (((ArrayList)this._storage_list.get(pc.getClanname())).remove(String.valueOf(name)));
       for (String s : this._storage_list.get(pc.getClanname())) {
         System.out.println(s);
       }
       update_Storage_List(pc);
     }
   }




   public void update_Storage_List(final L1PcInstance pc) {
     final StringBuilder list = new StringBuilder();
     String comma = "";
     for (String g : this._storage_list.get(pc.getClanname())) {
       list.append(comma);
       list.append(g);
       comma = ",";
     }
     Updator.exec("update clan_storage set allow_list=? where clan_name=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setString(++idx, list.toString());
             pstm.setString(++idx, pc.getClanname());
           }
         });
   }


   public void insert_Storage_List(final L1PcInstance pc) {
     Updator.exec("insert into clan_storage set clan_name =?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setString(++idx, pc.getClanname());
           }
         });
   }
 }



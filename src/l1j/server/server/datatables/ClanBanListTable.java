 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.server.model.L1Clan_BanList;
 import l1j.server.server.utils.SQLUtil;

 public class ClanBanListTable
 {
   private static ClanBanListTable _instance;

   public static ClanBanListTable getInstance() {
     if (_instance == null) {
       _instance = new ClanBanListTable();
     }
     return _instance;
   }
   private Map<String, ArrayList<String>> _clan_banlist = new HashMap<>();
   private Map<String, Integer> _clan_limit_level = new HashMap<>();

   private ClanBanListTable() {
     loadClanBanList();
   }


   public void reload() {}

   private void loadClanBanList() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       ArrayList<String> banlistarray = new ArrayList<>();
       String bandb = "";

       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM clan_ban");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1Clan_BanList banlist = new L1Clan_BanList();
         banlist.set_ClanName(rs.getString("clan_name"));
         banlist.set_LimitLevel(rs.getInt("limit_level"));
         bandb = rs.getString("clan_ban_data");
         if (bandb != null) {
           String[] array = bandb.split(",");
           for (int i = 0; i < array.length; i++) {
             banlistarray.add(array[i]);
           }
           banlist.setBanlist(banlistarray);
         }
         this._clan_banlist.put(banlist.get_ClanName(), banlist.getBanList());
         this._clan_limit_level.put(banlist.get_ClanName(), Integer.valueOf(banlist.get_LimitLevel()));
       }

     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }
   public int getLimitLevel(String clanname) {
     if (this._clan_limit_level.get(clanname) != null) {
       return ((Integer)this._clan_limit_level.get(clanname)).intValue();
     }
     return 0;
   }

   public void SetLimitLevel(String clanname, int level) {
     if (this._clan_limit_level.get(clanname) != null) {
       this._clan_limit_level.remove(clanname);


     }
     else if (this._clan_banlist.get(clanname) == null) {
       this._clan_banlist.put(clanname, null);
       insertClanBanList(clanname);
     }

     this._clan_limit_level.put(clanname, Integer.valueOf(level));
     uploadClanLimitLevel(clanname);
   }

   public void updateClanBanlist(String clanname, String name) {
     if (!checkClanBanlist(clanname, name)) {
       if (this._clan_banlist.get(clanname) == null) {
         ArrayList<String> array = new ArrayList<>();
         array.add(name);
         this._clan_banlist.put(clanname, array);
       } else {
         ((ArrayList<String>)this._clan_banlist.get(clanname)).add(name);
       }
     }
     uploadClanBanList(clanname);
   }

   public ArrayList<String> getBanList(String clanname) {
     if (this._clan_banlist.get(clanname) != null) {
       return this._clan_banlist.get(clanname);
     }
     return null;
   }



   public void deleteClanBanlist(String clanname, String name) {
     if (checkClanBanlist(clanname, name) &&
       this._clan_banlist.get(clanname) == null) {
       ((ArrayList)this._clan_banlist.get(clanname)).remove(name);
     }

     uploadClanBanList(clanname);
   }

   public boolean checkClanBanlist(String clanname, String name) {
     if (this._clan_banlist.get(clanname) == null) {
       if (this._clan_limit_level.get(clanname) == null) {
         insertClanBanList(clanname);
         this._clan_limit_level.put(clanname, Integer.valueOf(30));
       }
       return false;
     }
     String[] list = (String[])((ArrayList)this._clan_banlist.get(clanname)).toArray((Object[])new String[((ArrayList)this._clan_banlist.get(clanname)).size()]);
     boolean contains = Arrays.<String>stream(list).anyMatch(x -> x.equalsIgnoreCase(name));
     return contains;
   }



   public void uploadClanBanList(final String clanname) {
     final StringBuilder list = new StringBuilder();
     String comma = "";
     for (String g : this._clan_banlist.get(clanname)) {
       list.append(comma);
       list.append(g);
       comma = ",";
     }
     Updator.exec("update clan_ban set clan_ban_data=?, limit_level=? where clan_name=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setString(++idx, list.toString());
             pstm.setInt(++idx, ((Integer)ClanBanListTable.this._clan_limit_level.get(clanname)).intValue());
             pstm.setString(++idx, clanname);
           }
         });
   }



   public void uploadClanLimitLevel(final String clanname) {
     Updator.exec("update clan_ban set limit_level=? where clan_name=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, ((Integer)ClanBanListTable.this._clan_limit_level.get(clanname)).intValue());
             pstm.setString(++idx, clanname);
           }
         });
   }


   public void insertClanBanList(final String clanname) {
     Updator.exec("insert into clan_ban set clan_name =?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setString(++idx, clanname);
           }
         });
   }
 }



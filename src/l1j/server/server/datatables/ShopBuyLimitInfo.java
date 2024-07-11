 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.ShopBuyLimit;
 import l1j.server.server.templates.eShopBuyLimitType;
 import l1j.server.server.utils.SQLUtil;

 public class ShopBuyLimitInfo
 {
   private static ShopBuyLimitInfo _instance;

   public static ShopBuyLimitInfo getInstance() {
     if (_instance == null) {
       _instance = new ShopBuyLimitInfo();
     }
     return _instance;
   }

   private ArrayList<ShopBuyLimit> _list = new ArrayList<>();

   private ArrayList<ShopBuyLimit> _character_list = new ArrayList<>();

   private ShopBuyLimitInfo() {
     loadShopBuyLimit(this._list);
     loadCharacterShopBuyLimit(this._character_list);
   }

   public void reload() {
     ArrayList<ShopBuyLimit> list = new ArrayList<>();
     loadShopBuyLimit(list);

     this._list = list;
   }

   private void loadShopBuyLimit(ArrayList<ShopBuyLimit> list) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM shop_buy_limit_info");
       rs = pstm.executeQuery();
       while (rs.next()) {
         ShopBuyLimit limit = new ShopBuyLimit();
         limit.set_item_id(rs.getInt("item_id"));
         limit.set_item_name(rs.getString("item_name"));
         limit.set_type(eShopBuyLimitType.fromString(rs.getString("limit_type")));
         limit.set_count(rs.getInt("limit_count"));

         list.add(limit);
       }
     } catch (SQLException e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   private void loadCharacterShopBuyLimit(ArrayList<ShopBuyLimit> list) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_shop_buy_limit");
       rs = pstm.executeQuery();
       while (rs.next()) {
         ShopBuyLimit limit = new ShopBuyLimit();
         limit.set_objid(rs.getInt("objid"));
         limit.set_account_name(rs.getString("account"));
         limit.set_item_id(rs.getInt("item_id"));
         limit.set_item_name(rs.getString("item_name"));
         limit.set_type(eShopBuyLimitType.fromString(rs.getString("limit_type")));
         limit.set_count(rs.getInt("count"));
         limit.set_end_time(rs.getTimestamp("limit_end_time"));

         list.add(limit);
       }
     } catch (SQLException e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public ShopBuyLimit getShopBuyLimit(int itemid) {
     return this._list.stream().filter(datas -> (datas.get_item_id() == itemid)).findFirst().orElse(null);
   }

   public ShopBuyLimit findShopBuyLimitByObjid(int objid, int itemid) {
     ShopBuyLimit find = this._character_list.stream().filter(datas -> (datas.get_objid() == objid && datas.get_item_id() == itemid)).findFirst().orElse(null);
     if (find != null && find.get_end_time() != null &&
       find.get_end_time().getTime() < System.currentTimeMillis()) {
       int count = ((ShopBuyLimit)this._list.stream().filter(datas -> (datas.get_item_id() == find.get_item_id())).findFirst().orElse(null)).get_count();
       find.set_count(count);
       find.set_end_time(null);
     }


     return find;
   }

   public ShopBuyLimit findShopBuyLimitByObjid(int objid) {
     return this._character_list.stream().filter(datas -> (datas.get_objid() == objid)).findFirst().orElse(null);
   }


   public ShopBuyLimit findShopBuyLimitByAccount(String objid, int itemid) {
     ShopBuyLimit find = this._character_list.stream().filter(datas -> (datas.get_account_name().equalsIgnoreCase(objid) && datas.get_item_id() == itemid && (datas.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT || datas.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT))).findFirst().orElse(null);
     if (find != null && find.get_end_time() != null &&
       find.get_end_time().getTime() < System.currentTimeMillis()) {
       int count = ((ShopBuyLimit)this._list.stream().filter(datas -> (datas.get_item_id() == find.get_item_id())).findFirst().orElse(null)).get_count();
       find.set_count(count);
       find.set_end_time(null);
     }


     return find;
   }

   public void addShopBuyLimit(ShopBuyLimit limit) {
     this._character_list.add(limit);
   }

   public void removeShopBuyLimit(int char_id, int item_id) {
     this._character_list.removeIf(datas -> (datas.get_objid() == char_id && datas.get_item_id() == item_id));
   }

   public void removeShopBuyLimit(int char_id) {
     this._character_list.removeIf(datas -> (datas.get_objid() == char_id));
   }

   public void clearShopBuyLimit() {
     this._character_list.clear();
   }


   private void saveCharacterShopBuyLimit(ShopBuyLimit limit) {
     if (limit == null) {
       return;
     }
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       String sql = "INSERT INTO character_shop_buy_limit (objid,account,item_id,item_name,count,limit_type,limit_end_time) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE objid=?,account=?,item_id=?,item_name=?,count=?,limit_type=?,limit_end_time=?";


       SQLUtil.execute(con, sql, new Object[] {
             Integer.valueOf(limit.get_objid()), limit.get_account_name(), Integer.valueOf(limit.get_item_id()), limit.get_item_name(), Integer.valueOf(limit.get_count()), limit.get_type().toStr(limit.get_type().toInt()), limit
             .get_end_time(), Integer.valueOf(limit.get_objid()), limit.get_account_name(), Integer.valueOf(limit.get_item_id()), limit.get_item_name(), Integer.valueOf(limit.get_count()), limit
             .get_type().toStr(limit.get_type().toInt()), limit.get_end_time() });
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm, con);
     }
   }

   public void save() {
     if (this._character_list == null)
       return;
     this._character_list.stream().forEach(datas -> saveCharacterShopBuyLimit(datas));
   }

   public void save(int char_id) {
     ShopBuyLimit sbl = findShopBuyLimitByObjid(char_id);
     if (sbl == null)
       return;
     saveCharacterShopBuyLimit(sbl);
   }

   public ArrayList<ShopBuyLimit> getCharacterList(int objid) {
     ArrayList<ShopBuyLimit> character_list = new ArrayList<>();
     for (ShopBuyLimit sbl : this._character_list) {
       if (sbl.get_objid() == objid) {
         character_list.add(sbl);
       }
     }

     return character_list;
   }
 }



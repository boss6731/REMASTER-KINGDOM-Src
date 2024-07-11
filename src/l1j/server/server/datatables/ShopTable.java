 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.shop.L1Shop;
 import l1j.server.server.templates.L1ShopItem;
 import l1j.server.server.templates.ShopBuyLimit;
 import l1j.server.server.utils.SQLUtil;



 public class ShopTable
 {
   private static ShopTable _instance;
   private final Map<Integer, L1Shop> _allShops = new ConcurrentHashMap<>();
   private HashMap<Integer, Integer> _allEquipmentChangeShops = new HashMap<>(); private HashMap<Integer, L1ShopItem> _sellings;

   public static ShopTable getInstance() {
     if (_instance == null) {
       _instance = new ShopTable();
     }
     return _instance;
   }
   private HashMap<Integer, ArrayList<L1ShopItem>> _purchasings;
   private ShopTable() {
     loadShops();
     loadEquipmentChangeShop();
   }

   public static void reload() {
     ShopTable oldInstance = _instance;
     _instance = new ShopTable();
     oldInstance._allShops.clear();
     oldInstance._allEquipmentChangeShops.clear();
   }

   private ArrayList<Integer> enumNpcIds() {
     ArrayList<Integer> ids = new ArrayList<>();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop");
       rs = pstm.executeQuery();
       while (rs.next()) {
         ids.add(Integer.valueOf(rs.getInt("npc_id")));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
     return ids;
   }




   private L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
     List<L1ShopItem> sellingList = new ArrayList<>();
     List<L1ShopItem> purchasingList = new ArrayList<>();

     L1ShopItem item = null;
     while (rs.next()) {
       int itemId = rs.getInt("item_id");
       int sellingPrice = rs.getInt("selling_price");
       int purchasingPrice = rs.getInt("purchasing_price");
       int packCount = rs.getInt("pack_count");
       int enchant = rs.getInt("enchant");
       int attrenchant = parseType(rs.getString("attr_enchant"));
       boolean timeLimit = Boolean.valueOf(rs.getString("time_limit")).booleanValue();
       int endtime = rs.getInt("end_time");
       boolean carving = Boolean.valueOf(rs.getString("carving")).booleanValue();
       int bless = BlessParseType(rs.getString("bless"));
       int limitlevel = rs.getInt("buy_level_limit");
       int classType = rs.getInt("classType");
       int clan_shop_type = Clan_Shop_ParseType(rs.getString("clan_shop_type"));
       packCount = (packCount == 0) ? 1 : packCount;
       if (0 <= sellingPrice) {
         item = new L1ShopItem(itemId, sellingPrice, packCount, enchant, timeLimit, endtime, carving, bless, attrenchant, limitlevel, classType, clan_shop_type);
         if (Config.ServerAdSetting.IsValidShopSystem &&
           sellingPrice > 0) {
           item.setNpcId(npcId);
           if (this._sellings != null) {
             L1ShopItem i = this._sellings.get(Integer.valueOf(itemId));
             if (i == null) {
               this._sellings.put(Integer.valueOf(itemId), item);
             } else if (sellingPrice < i.getPrice()) {
               this._sellings.put(Integer.valueOf(itemId), item);
             }
           }
         }

           ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(itemId);
           if (sbl != null && packCount > 1) {
               System.out.println(" ◆◆◆◆[警告]◆◆◆◆ : " + npcId + " : 店鋪購買限制物品  packcount 已輸入.");
                            // 註解: ◆◆◆◆[警告]◆◆◆◆ : " + npcId + " : 店鋪購買限制物品，packcount 已輸入。}


         sellingList.add(item);
       }
       if (0 <= purchasingPrice) {
         item = new L1ShopItem(itemId, purchasingPrice, packCount, enchant, timeLimit, endtime, carving, bless, attrenchant, limitlevel, classType, clan_shop_type);
         if (Config.ServerAdSetting.IsValidShopSystem &&
           purchasingPrice > 0) {
           item.setNpcId(npcId);
           if (this._purchasings != null) {
             ArrayList<L1ShopItem> list = this._purchasings.get(Integer.valueOf(itemId));
             if (list == null) {
               list = new ArrayList<>(8);
               this._purchasings.put(Integer.valueOf(itemId), list);
             }
             list.add(item);
           }
         }

         purchasingList.add(item);
       }
     }
     return new L1Shop(npcId, sellingList, purchasingList);
   }

   public void Reload(int npcid) {
     ArrayList<Integer> list = enumNpcIds();
     for (Integer i : list) {
       if (i.intValue() != npcid) {
         continue;
       }
       Selector.exec("select * from shop where npc_id=? order by order_id", new SelectorHandler()
           {
             public void handle(PreparedStatement pstm) throws Exception
             {
               pstm.setInt(1, i.intValue());
             }


             public void result(ResultSet rs) throws Exception {
               ShopTable.this._allShops.put(i, ShopTable.this.loadShop(i.intValue(), rs));
             }
           });
     }
   }

   private void loadShops() {
     if (Config.ServerAdSetting.IsValidShopSystem) {
       this._sellings = new HashMap<>(1024);
       this._purchasings = new HashMap<>(1024);
     }

     ArrayList<Integer> list = enumNpcIds();
     for (Integer i : list) {
       Selector.exec("select * from shop where npc_id=? order by order_id", new SelectorHandler()
           {
             public void handle(PreparedStatement pstm) throws Exception {
               pstm.setInt(1, i.intValue());
             }


             public void result(ResultSet rs) throws Exception {
               ShopTable.this._allShops.put(i, ShopTable.this.loadShop(i.intValue(), rs));
             }
           });
     }

       if (Config.ServerAdSetting.IsValidShopSystem) {
           for (Integer itemId : this._purchasings.keySet()) {
               L1ShopItem sell = this._sellings.get(itemId);
               ArrayList<L1ShopItem> purs = this._purchasings.get(itemId);
               if (sell == null || purs == null || purs.size() <= 0) {
                   continue;
               }
               int sellp = sell.getPrice();
               for (L1ShopItem buy : purs) {
                   if (sellp < buy.getPrice())
                       System.out.println(String.format("[發現買價高於賣價的店鋪項目, NPC:%d 物品:%d，賣價:%d，買價:%d]",
                               new Object[] { Integer.valueOf(buy.getNpcId()), itemId, Integer.valueOf(sellp), Integer.valueOf(buy.getPrice()) }));
                   // 註解: 發現買價高於賣價的店鋪項目，NPC:%d 項目:%d，賣價:%d，買價:%d
               }
           }
       }
         purs.clear();
       }
       this._sellings.clear();
       this._sellings = null;
       this._purchasings.clear();
       this._purchasings = null;
     }
   }

   private void loadEquipmentChangeShop() {
     try(Connection con = L1DatabaseFactory.getInstance().getConnection();
         PreparedStatement pstm = con.prepareStatement("SELECT * FROM equipment_change_shop");
         ResultSet rs = pstm.executeQuery()) {
       while (rs.next()) {
         int itemId = rs.getInt("item_id");
         int itemType = rs.getInt("item_type");
         this._allEquipmentChangeShops.put(Integer.valueOf(itemId), Integer.valueOf(itemType));
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public int getEquipmentChangeItemClass(int itemId) {
     if (this._allEquipmentChangeShops.get(Integer.valueOf(itemId)) != null) {
       return ((Integer)this._allEquipmentChangeShops.get(Integer.valueOf(itemId))).intValue();
     }
     return -1;
   }

   public void set(int npcId, L1Shop shop) {
     this._allShops.put(Integer.valueOf(npcId), shop);
   }

   public HashMap<Integer, Integer> getEquipmentChangeShop() {
     return this._allEquipmentChangeShops;
   }

   public L1Shop get(int npcId) {
     return this._allShops.get(Integer.valueOf(npcId));
   }


   public void addShop(int npcId, L1Shop shop) {
     this._allShops.put(Integer.valueOf(npcId), shop);
   }

     private int parseType(String type) {
         if (type.equalsIgnoreCase("火靈:1級")) // 火靈:1段
             return 1;
         if (type.equalsIgnoreCase("火靈:2級")) // 火靈:2段
             return 2;
         if (type.equalsIgnoreCase("火靈:3級")) // 火靈:3段
             return 3;
         if (type.equalsIgnoreCase("火靈:4級")) // 火靈:4段
             return 4;
         if (type.equalsIgnoreCase("火靈:5級")) // 火靈:5段
             return 5;
         if (type.equalsIgnoreCase("水靈:1級")) // 水靈:1段
             return 6;
         if (type.equalsIgnoreCase("水靈:2級")) // 水靈:2段
             return 7;
         if (type.equalsIgnoreCase("水靈:3級")) // 水靈:3段
             return 8;
         if (type.equalsIgnoreCase("水靈:4級")) // 水靈:4段
             return 9;
         if (type.equalsIgnoreCase("水靈:5級")) // 水靈:5段
             return 10;
         if (type.equalsIgnoreCase("風靈:1級")) // 風靈:1段
             return 11;
         if (type.equalsIgnoreCase("風靈:2級")) // 風靈:2段
             return 12;
         if (type.equalsIgnoreCase("風靈:3級")) // 風靈:3段
             return 13;
         if (type.equalsIgnoreCase("風靈:4級")) // 風靈:4段
             return 14;
         if (type.equalsIgnoreCase("風靈:5級")) // 風靈:5段
             return 15;
         if (type.equalsIgnoreCase("地靈:1級")) // 地靈:1段
             return 16;
         if (type.equalsIgnoreCase("地靈:2級")) // 地靈:2段
             return 17;
         if (type.equalsIgnoreCase("地靈:3級")) // 地靈:3段
             return 18;
         if (type.equalsIgnoreCase("地靈:4級")) // 地靈:4段
             return 19;
         if (type.equalsIgnoreCase("地靈:5級")) // 地靈:5段
             return 20;
         return 0;
     }

   private int BlessParseType(String type) {
     if (type.equalsIgnoreCase("Bless"))
       return 0;
     if (type.equalsIgnoreCase("Normal"))
       return 1;
     if (type.equalsIgnoreCase("Curse"))
       return 2;
     return 1;
   }

     private int Clan_Shop_ParseType(String type) {
         if (type != null) {
             if (type.equalsIgnoreCase("盟主")) // 軍主
                 return 10;
             if (type.equalsIgnoreCase("盟主/副盟主/守護")) // 軍主/副軍主/守護
                 return 9;
             if (type.equalsIgnoreCase("盟主/副盟主/守護/菁英/一般")) // 軍主/副軍主/守護/精英/一般
                 return 8;
         }
         return 0;
     }


   public void delShop(int npcId) {
     this._allShops.remove(Integer.valueOf(npcId));
   }
 }



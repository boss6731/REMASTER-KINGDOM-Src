 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.Warehouse.ElfWarehouse;
 import l1j.server.server.model.Warehouse.PrivateWarehouse;
 import l1j.server.server.model.Warehouse.SpecialWarehouse;
 import l1j.server.server.model.Warehouse.SupplementaryService;
 import l1j.server.server.model.Warehouse.WarehouseManager;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.storage.CharacterStorage;
 import l1j.server.server.storage.mysql.MySqlCharacterStorage;
 import l1j.server.server.templates.L1CharName;
 import l1j.server.server.utils.SQLUtil;


 public class CharacterTable
 {
   private CharacterStorage _charStorage;
   private static CharacterTable _instance;
   private static Logger _log = Logger.getLogger(CharacterTable.class.getName());

   private final Map<String, L1CharName> _charNameList = new ConcurrentHashMap<>();


   private CharacterTable() {
     this._charStorage = (CharacterStorage)new MySqlCharacterStorage();
   }

   public static CharacterTable getInstance() {
     if (_instance == null) {
       _instance = new CharacterTable();
     }
     return _instance;
   }

   public void storeNewCharacter(L1PcInstance pc) throws Exception {
     synchronized (pc) {
       this._charStorage.createCharacter(pc);
       if (!this._charNameList.containsKey(pc.getName())) {
         L1CharName cn = new L1CharName();
         cn.setName(pc.getName());
         cn.setId(pc.getId());
         this._charNameList.put(pc.getName(), cn);
       }
       _log.finest("storeNewCharacter");
     }
   }

   public void updateCharacterAccount(L1PcInstance pc) throws Exception {
     synchronized (pc) {
       this._charStorage.updateAccountName(pc);
       _log.finest("updateCharacterAccount");
     }
   }





   public void storeCharacter(L1PcInstance pc) {
     synchronized (pc) {
       try {
         this._charStorage.storeCharacter(pc);
         String name = pc.getName();
         if (!this._charNameList.containsKey(name)) {
           L1CharName cn = new L1CharName();
           cn.setName(name);
           cn.setId(pc.getId());
           this._charNameList.put(name, cn);
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }


   public void deleteCharacter(String accountName, String charName) throws Exception {
     this._charStorage.deleteCharacter(accountName, charName);
     if (this._charNameList.containsKey(charName)) {
       this._charNameList.remove(charName);
     }
     _log.finest("deleteCharacter");
   }

   public boolean isContainNameList(String name) {
     return this._charNameList.containsKey(name);
   }

   public L1CharName getCharName(String name) {
     return this._charNameList.get(name);
   }

   public void getChangeName(String name, String changeName) {
     L1CharName cn = this._charNameList.get(name);
     L1CharName new_cn = new L1CharName();
     if (cn != null) {
       new_cn.setName(changeName);
       new_cn.setId(cn.getId());

       this._charNameList.remove(name);
       this._charNameList.put(changeName, new_cn);
     }
   }

   public L1PcInstance restoreCharacter(String charName) throws Exception {
     L1PcInstance pc = this._charStorage.loadCharacter(charName);
     return pc;
   }

     public L1PcInstance loadCharacter(String charName) throws Exception {
         L1PcInstance pc = null; // 聲明L1PcInstance對象

         try {
             pc = restoreCharacter(charName); // 嘗試恢復角色

             if (pc == null) { // 如果角色為空
                 return null; // 返回null
             }

             L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId()); // 獲取角色所在的地圖

             if (!map.isInMap(pc.getX(), pc.getY())) { // 如果角色坐標不在地圖範圍內
                 pc.setX(33087); // 設置角色X坐標到默認位置
                 pc.setY(33396); // 設置角色Y坐標到默認位置
                 pc.setMap((short) 4); // 設置角色地圖ID到默認地圖
             }
             _log.finest("記錄角色加載信息: " + pc.getName()); // 記錄角色加載信息 //loadCharacter
         } catch (Exception e) {
             e.printStackTrace(); // 捕獲並打印異常堆棧跟蹤
         }
         return pc; // 返回L1PcInstance對象
     }


   public static void clearOnlineStatus() {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=0");
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void updateOnlineStatus(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=1, lastLoginTime=now() WHERE objid=?");
       pstm.setInt(1, pc.getId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public void restoreInventory(L1PcInstance pc) {
     pc.getInventory().loadItems();
     PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
     warehouse.getItems().clear();
     warehouse.loadItems();

     ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
     elfwarehouse.getItems().clear();
     elfwarehouse.loadItems();

     pc.getDwarfForPackageInventory().getItems().clear();
     pc.getDwarfForPackageInventory().loadItems();

     SupplementaryService supplementaryservice = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
     supplementaryservice.getItems().clear();
     supplementaryservice.loadItems();

     SpecialWarehouse specialwarehose = WarehouseManager.getInstance().getSpecialWarehouse(pc.getName());
     specialwarehose.getItems().clear();
     specialwarehose.loadItems();
   }

   public void loadAllCharName() {
     L1CharName cn = null;
     String name = null;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM characters");
       rs = pstm.executeQuery();
       while (rs.next()) {
         cn = new L1CharName();
         name = rs.getString("char_name");
         cn.setName(name);
         cn.setId(rs.getInt("objid"));
         this._charNameList.put(name, cn);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

     public int PcLevelInDB(int pcid) {
         int result = 0; // 初始化結果變量
         Connection con = null; // 聲明數據庫連接
         PreparedStatement pstm = null; // 聲明預處理語句
         ResultSet rs = null; // 聲明結果集

         try {
             con = L1DatabaseFactory.getInstance().getConnection(); // 獲取數據庫連接
             pstm = con.prepareStatement("SELECT level FROM characters WHERE objid=?"); // 準備SQL查詢語句
             pstm.setInt(1, pcid); // 設置查詢參數
             rs = pstm.executeQuery(); // 執行查詢並返回結果集

             if (rs.next()) { // 如果查詢有結果
                 result = rs.getInt(1); // 獲取第一列的整數值，並賦值給結果變量
             }
         } catch (Exception e) {
             _log.warning("無法檢查現有角色名稱：" + e.getMessage()); // 記錄警告日誌
             System.out.println("無法檢查現有角色名稱：" + e.getMessage()); // 輸出錯誤信息到控制台
         } finally {
             SQLUtil.close(rs); // 關閉結果集
             SQLUtil.close(pstm); // 關閉預處理語句
             SQLUtil.close(con); // 關閉連接
         }
         return result; // 返回結果
     }

   public L1CharName[] getCharNameList() {
     return (L1CharName[])this._charNameList.values().toArray((Object[])new L1CharName[this._charNameList.size()]);
   }

   public void updateLoc(int castleid, int a, int b, int c, int d, int f) {
     Connection con = null;
     PreparedStatement pstm = null;
     int[] loc = new int[3];
     loc = L1CastleLocation.getGetBackLoc(castleid);
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE characters SET LocX=?, LocY=?, MapID=? WHERE OnlineStatus=0 AND (MapID=? OR MapID=? OR MapID=? OR MapID=? OR MapID=?)");
       pstm.setInt(1, loc[0]);
       pstm.setInt(2, loc[1]);
       pstm.setInt(3, loc[2]);
       pstm.setInt(4, a);
       pstm.setInt(5, b);
       pstm.setInt(6, c);
       pstm.setInt(7, d);
       pstm.setInt(8, f);
       pstm.executeUpdate();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

     public void CharacterAccountCheck(L1PcInstance pc, String charName) {
         Connection con = null; // 聲明數據庫連接
         PreparedStatement pstm = null; // 聲明第一個預處理語句
         PreparedStatement pstm2 = null; // 聲明第二個預處理語句
         ResultSet loginRs = null; // 聲明第一個結果集
         ResultSet characterRs = null; // 聲明第二個結果集
         StringBuilder sb = new StringBuilder(); // 用於構建SQL查詢語句

         try {
             sb.append("SELECT login, password, CharPassword FROM accounts WHERE ip = ");
             sb.append("(SELECT ip FROM accounts WHERE login = ");
             sb.append("(SELECT account_name FROM characters WHERE char_name = ?))");

             con = L1DatabaseFactory.getInstance().getConnection(); // 獲取數據庫連接
             pstm = con.prepareStatement(sb.toString()); // 準備SQL查詢語句
             pstm.setString(1, charName); // 設置查詢參數
             loginRs = pstm.executeQuery(); // 執行查詢並返回結果集

             while (loginRs.next()) { // 遍歷第一個結果集
                 pstm2 = con.prepareStatement("SELECT char_name, level, highlevel, clanname, onlinestatus FROM characters WHERE account_name = ?");
                 pstm2.setString(1, loginRs.getString("login")); // 設置查詢參數
                 characterRs = pstm2.executeQuery(); // 執行查詢並返回第二個結果集

                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("-------------------------------------------------")); // 發送分隔信息
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("\fY帳號 : " + loginRs.getString("login") + ", 密碼 : " + loginRs.getString("password") + ", CharPassword : " + loginRs.getString("CharPassword"))); // 發送帳戶信息

                 while (characterRs.next()) { // 遍歷第二個結果集
                     String onlineStatus = (characterRs.getInt("onlinestatus") == 0) ? "" : "(在線中)"; // 獲取在線狀態
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("* " + characterRs.getString("char_name") + " (等級:" + characterRs.getInt("level") + ") (最高等級:" + characterRs.getInt("highlevel") + ") (血盟:" + characterRs.getString("clanname") + ") \fY" + onlineStatus)); // 發送角色信息
                 }
             } catch (Exception e) {
                 e.printStackTrace(); // 捕獲並打印異常堆棧跟蹤
             } finally {
                 SQLUtil.close(characterRs); // 關閉第二個結果集
                 SQLUtil.close(loginRs); // 關閉第一個結果集
                 SQLUtil.close(pstm2); // 關閉第二個預處理語句
                 SQLUtil.close(pstm); // 關閉第一個預處理語句
                 SQLUtil.close(con); // 關閉連接
             }
         }

         characterRs.close();
         pstm2.close();
       }
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("-------------------------------------------------"));
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(loginRs);
       SQLUtil.close(characterRs);
       SQLUtil.close(pstm);
       SQLUtil.close(pstm2);
       SQLUtil.close(con);
     }
   }
   public void CharacterAccountCheck1(L1PcInstance pc, String charName) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet loginRs = null;
     ResultSet characterRs = null;
     StringBuilder sb = new StringBuilder();
     try {
       sb.append("SELECT login, password, CharPassword FROM accounts WHERE ip = ");
       sb.append("(SELECT account_name FROM characters WHERE char_name = ?)");

       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement(sb.toString());
       pstm.setString(1, charName);
       loginRs = pstm.executeQuery();

       while (loginRs.next()) {
         pstm = con.prepareStatement("SELECT char_name, level, highlevel, clanname, onlinestatus FROM characters WHERE account_name = ?");
         pstm.setString(1, loginRs.getString("login"));
         characterRs = pstm.executeQuery();
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aD-------------------------------------------------")); // 發送分隔線信息
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aH帳號: " + loginRs.getString("login") + ", 密碼: " + loginRs.getString("password") + ", CharPassword : " + loginRs.getString("CharPassword") + ", IP位址: " + loginRs.getString("ip"))); // 發送帳戶信息

         while (characterRs.next()) { // 遍歷結果集
         String onlineStatus = (characterRs.getInt("onlinestatus") == 0) ? "" : "(在線中)"; // 獲取在線狀態
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("* " + characterRs.getString("char_name") + " (等級:" + characterRs.getInt("level") + ") (最高等級:" + characterRs.getInt("highlevel") + ") (血盟:" + characterRs.getString("clanname") + ") \aG" + onlineStatus)); // 發送角色信息
         }         SQLUtil.close(characterRs);
       }
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aD-------------------------------------------------"));
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(characterRs);
       SQLUtil.close(loginRs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }



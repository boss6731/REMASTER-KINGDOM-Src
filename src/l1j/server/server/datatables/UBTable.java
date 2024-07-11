 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1UltimateBattle;
 import l1j.server.server.utils.SQLUtil;

 public class UBTable {
   private static Logger _log = Logger.getLogger(UBTable.class.getName());

   private static UBTable _instance = new UBTable();

   private HashMap<Integer, L1UltimateBattle> _ub = new HashMap<>();

   public static UBTable getInstance() {
     return _instance;
   }

   private UBTable() {
     loadTable();
   }


   private void loadTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM ub_settings");
       rs = pstm.executeQuery();
       L1UltimateBattle ub = null;
       while (rs.next()) {

         ub = new L1UltimateBattle();
         ub.setUbId(rs.getInt("ub_id"));
         ub.setMapId(rs.getShort("ub_mapid"));
         ub.setLocX1(rs.getInt("ub_area_x1"));
         ub.setLocY1(rs.getInt("ub_area_y1"));
         ub.setLocX2(rs.getInt("ub_area_x2"));
         ub.setLocY2(rs.getInt("ub_area_y2"));
         ub.setMinLevel(rs.getInt("min_lvl"));
         ub.setMaxLevel(rs.getInt("max_lvl"));
         ub.setMaxPlayer(rs.getInt("max_player"));
         ub.setEnterRoyal(rs.getBoolean("enter_royal"));
         ub.setEnterKnight(rs.getBoolean("enter_knight"));
         ub.setEnterMage(rs.getBoolean("enter_mage"));
         ub.setEnterElf(rs.getBoolean("enter_elf"));
         ub.setEnterDarkelf(rs.getBoolean("enter_darkelf"));
         ub.setEnterDragonknight(rs.getBoolean("enter_dragonknight"));
         ub.setEnterBlackwizard(rs.getBoolean("enter_blackwizard"));
         ub.setEnterMale(rs.getBoolean("enter_male"));
         ub.setEnterFemale(rs.getBoolean("enter_female"));
         ub.setEnterlancer(rs.getBoolean("enter_lancer"));
         ub.setUsePot(rs.getBoolean("use_pot"));
         ub.setHpr(rs.getInt("hpr_bonus"));
         ub.setMpr(rs.getInt("mpr_bonus"));
         ub.resetLoc();

           this._ub.put(Integer.valueOf(ub.getUbId()), ub);
            // 註解: 將 UB 設置加入到 _ub 集合中
       }
     } catch (Exception e) {
         _log.warning("警告: 無法初始化 ubsettings::" + e);
            // 註解: 警告: 無法初始化 ubsettings: " + e
         System.out.println("輸出到控制台: 無法初始化 ubsettings::" + e);
        // 註解: 輸出到控制台: 無法初始化 ubsettings: " + e
     } finally {
         SQLUtil.close(rs); // 註解: 關閉結果集 (ResultSet)
         SQLUtil.close(pstm); // 註解: 關閉預處理語句 (PreparedStatement)
     }


     try {
       pstm = con.prepareStatement("SELECT * FROM ub_managers");
       rs = pstm.executeQuery();
       L1UltimateBattle ub = null;
       while (rs.next()) {
         ub = getUb(rs.getInt("ub_id"));
         if (ub != null) {
           ub.addManager(rs.getInt("ub_manager_npc_id"));
         }
       }
     } catch (Exception e) {
         _log.warning("警告: 無法初始化 ub_managers::" + e);
                // 註解: 警告: 無法初始化 ub_managers: " + e
         System.out.println("輸出到控制台: 無法初始化 ub_managers::" + e);
            // 註解: 輸出到控制台: 無法初始化 ub_managers: " + e
     } finally {
         SQLUtil.close(rs); // 註解: 關閉結果集 (ResultSet)
         SQLUtil.close(pstm); // 註解: 關閉預處理語句 (PreparedStatement)
     }


     try {
       pstm = con.prepareStatement("SELECT * FROM ub_times");
       rs = pstm.executeQuery();
       L1UltimateBattle ub = null;
       while (rs.next()) {
         ub = getUb(rs.getInt("ub_id"));
         if (ub != null) {
           ub.addUbTime(rs.getInt("ub_time"));
         }
       }
     } catch (Exception e) {
         _log.warning("警告: 無法初始化 ub_times::" + e);
// 註解: 警告: 無法初始化 ub_times: " + e
         System.out.println("輸出到控制台: 無法初始化 ub_times::" + e);
// 註解: 輸出到控制台: 無法初始化 ub_times: " + e
     } finally {
         SQLUtil.close(rs, pstm, con); // 註解: 關閉結果集 (ResultSet)、預處理語句 (PreparedStatement) 和數據庫連接 (Connection)
     }
       _log.config("UB列表 " + this._ub.size() + "條加載完成");
// 註解: UB 列表 " + this._ub.size() + " 條加載完成
   }

     // 註解: 根據 UB ID 獲取特定的 L1UltimateBattle 實例
     public L1UltimateBattle getUb(int ubId) {
         return this._ub.get(Integer.valueOf(ubId));
     }

     // 註解: 獲取所有的 L1UltimateBattle 實例的不可修改集合
     public Collection<L1UltimateBattle> getAllUb() {
         return Collections.unmodifiableCollection(this._ub.values());
     }

     // 註解: 根據 NPC ID 獲取對應的 L1UltimateBattle 實例
     public L1UltimateBattle getUbForNpcId(int npcId) {
         for (L1UltimateBattle ub : this._ub.values()) {
             if (ub.containsManager(npcId)) {
                 return ub; // 註解: 如果 UB 包含該 NPC ID 作為管理者，則返回該 UB
             }
         }
         return null; // 註解: 如果未找到符合條件的 UB，返回 null
     }








   public int getMaxPattern(int ubId) {
     int n = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT MAX(pattern) FROM spawnlist_ub WHERE ub_id=?");
       pstm.setInt(1, ubId);
       rs = pstm.executeQuery();
       if (rs.next()) {
         n = rs.getInt(1);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return n;
   }




   public void writeUbScore(int ubId, L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm1 = null;
     PreparedStatement pstm2 = null;
     ResultSet rs = null;
     int score = 0;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm1 = con.prepareStatement("SELECT * FROM ub_rank WHERE ub_id=? AND char_name=?");
       pstm1.setInt(1, ubId);
       pstm1.setString(2, pc.getName());
       rs = pstm1.executeQuery();
       if (rs.next()) {
         score = rs.getInt("score");
         pstm2 = con.prepareStatement("UPDATE ub_rank SET score=? WHERE ub_id=? AND char_name=?");
         pstm2.setInt(1, score + pc.getUbScore());
         pstm2.setInt(2, ubId);
         pstm2.setString(3, pc.getName());
         pstm2.execute();
       } else {
         pstm2 = con.prepareStatement("INSERT INTO ub_rank SET ub_id=?, char_name=?, score=?");
         pstm2.setInt(1, ubId);
         pstm2.setString(2, pc.getName());
         pstm2.setInt(3, pc.getUbScore());
         pstm2.execute();
       }
       pc.setUbScore(0);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm1);
       SQLUtil.close(pstm2);
       SQLUtil.close(con);
     }
   }
 }



 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.L1WeaponSkill;
 import l1j.server.server.utils.SQLUtil;




















 public class WeaponSkillTable
 {
   private static Logger _log = Logger.getLogger(WeaponSkillTable.class.getName());

   private static WeaponSkillTable _instance;

   private final HashMap<Integer, L1WeaponSkill> _weaponIdIndex = new HashMap<>();

   public static WeaponSkillTable getInstance() {
     if (_instance == null) {
       _instance = new WeaponSkillTable();
     }
     return _instance;
   }

   private WeaponSkillTable() {
     loadWeaponSkill();
   }

   public static void reload() {
     WeaponSkillTable oldInstance = _instance;
     _instance = new WeaponSkillTable();
     if (oldInstance != null)
       oldInstance._weaponIdIndex.clear();
   }

   private void loadWeaponSkill() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM weapon_skill");
       rs = pstm.executeQuery();
       fillWeaponSkillTable(rs);
     } catch (Exception e) {
       _log.log(Level.SEVERE, "error while creating weapon_skill table", e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

     private void fillWeaponSkillTable(ResultSet rs) throws SQLException {
         L1WeaponSkill weaponSkill = null;
         while (rs.next()) {
             int weaponId = rs.getInt("weapon_id"); // 註解: 獲取武器 ID
             int probability = rs.getInt("probability"); // 註解: 獲取觸發概率
             int fixDamage = rs.getInt("fix_damage"); // 註解: 獲取固定傷害
             int randomDamage = rs.getInt("random_damage"); // 註解: 獲取隨機傷害
             int area = rs.getInt("area"); // 註解: 獲取技能作用範圍
             int skillId = rs.getInt("skill_id"); // 註解: 獲取技能 ID
             int skillTime = rs.getInt("skill_time"); // 註解: 獲取技能持續時間
             int effectId = rs.getInt("effect_id"); // 註解: 獲取效果 ID
             int effectTarget = rs.getInt("effect_target"); // 註解: 獲取效果目標
             boolean isArrowType = rs.getBoolean("arrow_type"); // 註解: 判斷是否為箭矢類型
             int attr = rs.getInt("attr"); // 註解: 獲取屬性
             weaponSkill = new L1WeaponSkill(weaponId, probability, fixDamage, randomDamage, area, skillId, skillTime, effectId, effectTarget, isArrowType, attr);

                    // 註解: 將 WeaponSkill 對象存儲在 weaponIdIndex 中
             this._weaponIdIndex.put(Integer.valueOf(weaponId), weaponSkill);
         }
         _log.config("武器技能列表 " + this._weaponIdIndex.size() + "條加載完成");
            // 註解: 武器技能列表 " + this._weaponIdIndex.size() + " 條加載完成
     }

   public L1WeaponSkill getTemplate(int weaponId) {
     return this._weaponIdIndex.get(Integer.valueOf(weaponId));
   }
 }



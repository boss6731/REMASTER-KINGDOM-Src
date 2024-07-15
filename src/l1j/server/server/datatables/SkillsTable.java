 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.server.SkillCheck;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Skills;
 import l1j.server.server.utils.SQLUtil;


 public class SkillsTable
 {
   private static Logger _log = Logger.getLogger(SkillsTable.class.getName());

   private static SkillsTable _instance;

   private final Map<Integer, L1Skills> _skills = new HashMap<>();

   private final boolean _initialized;

   public static SkillsTable getInstance() {
     if (_instance == null) {
       _instance = new SkillsTable();
     }
     return _instance;
   }

   private SkillsTable() {
     this._initialized = true;
     RestoreSkills();
   }

   public static void reload() {
     SkillsTable oldInstance = _instance;
     _instance = new SkillsTable();
     oldInstance._skills.clear();
     oldInstance = null;
   }

   private void RestoreSkills() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM skills");
       rs = pstm.executeQuery();
       FillSkillsTable(rs);
     }
     catch (Exception e) {
       _log.log(Level.SEVERE, "error while creating skills table", e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private void FillSkillsTable(ResultSet rs) throws SQLException {
     L1Skills l1skills = null;
     while (rs.next()) {
       l1skills = new L1Skills();
       int skill_id = rs.getInt("skill_id");
       l1skills.setSkillId(skill_id);
       l1skills.setName(rs.getString("name"));
       l1skills.setSkillLevel(rs.getInt("skill_level"));
       l1skills.setSkillNumber(rs.getInt("skill_number"));
       l1skills.setMpConsume(rs.getInt("mpConsume"));
       l1skills.setHpConsume(rs.getInt("hpConsume"));
       l1skills.setItemConsumeId(rs.getInt("itemConsumeId"));
       l1skills.setItemConsumeCount(rs.getInt("itemConsumeCount"));
       l1skills.setReuseDelay(rs.getInt("reuseDelay"));
       l1skills.setBuffDuration(rs.getInt("buffDuration"));
       l1skills.setTarget(rs.getString("target"));
       l1skills.setTargetTo(rs.getInt("target_to"));
       l1skills.setDamageValue(rs.getInt("damage_value"));
       l1skills.setDamageDice(rs.getInt("damage_dice"));
       l1skills.setDamageDiceCount(rs.getInt("damage_dice_count"));
       l1skills.setProbabilityValue(rs.getInt("probability_value"));
       l1skills.setProbabilityDice(rs.getInt("probability_dice"));
       l1skills.setAttr(rs.getInt("attr"));
       l1skills.setType(rs.getInt("type"));
       l1skills.setLawful(rs.getInt("lawful"));
       l1skills.setRanged(rs.getInt("ranged"));
       l1skills.setArea(rs.getInt("area"));
       l1skills.setIsThrough(rs.getInt("through"));
       l1skills.setId(rs.getInt("id"));
       l1skills.setNameId(rs.getString("nameid"));
       l1skills.setActionId(rs.getInt("action_id"));
       l1skills.setActionId2(rs.getInt("action_id2"));
       l1skills.setActionId3(rs.getInt("action_id3"));
       l1skills.setCastGfx(rs.getInt("castgfx"));
       l1skills.setCastGfx2(rs.getInt("castgfx2"));
       l1skills.setCastGfx3(rs.getInt("castgfx3"));
       l1skills.setSysmsgIdHappen(rs.getInt("sysmsgID_happen"));
       l1skills.setSysmsgIdStop(rs.getInt("sysmsgID_stop"));
       l1skills.setSysmsgIdFail(rs.getInt("sysmsgID_fail"));
       l1skills.setPlusProbility(rs.getInt("plus_probability"));

       l1skills.setBuff(rs.getString("is_good").equalsIgnoreCase("true"));
       l1skills.setCanCastWithInvis(rs.getString("can_cast_with_invis").equalsIgnoreCase("true"));
       l1skills.setIgnoresCounterMagic(rs.getString("ignores_counter_magic").equalsIgnoreCase("true"));
       l1skills.setInvenIconUse(rs.getString("is_invenicon").equalsIgnoreCase("true"));

       l1skills.setInvenIconStart(rs.getInt("on_icon_id"));
       l1skills.setInvenIconEnd(rs.getInt("off_icon_id"));
       l1skills.setInvenIconStringNo(rs.getInt("tooltip_str_id"));
       l1skills.setInvenIconStartStrId(rs.getInt("new_str_id"));
       l1skills.setInvenIconEndStrId(rs.getInt("end_str_id"));
       l1skills.setInvenIconSort(rs.getInt("icon_priority"));

       l1skills.setInvenIconOverLapBuffIcon(rs.getInt("invenicon_overlap_buff_icon"));
       l1skills.setInvenIconMainTooltipStrId(rs.getInt("invenicon_main_tooltip_str_id"));
       l1skills.setInvenIconBuffIconPriority(rs.getInt("invenicon_buff_icon_priority"));
       l1skills.setInvenIconBuffGroupId(rs.getInt("invenicon_buff_group_id"));
       l1skills.setInvenIconBuffGroupPriority(rs.getInt("invenicon_buff_group_priority"));
       l1skills.setDurationShowType(SC_SPELL_BUFF_NOTI.eDurationShowType.fromString(rs.getString("duration_show_type")));

       l1skills.setSave(rs.getString("is_save").equalsIgnoreCase("true"));
       l1skills.setis_auto_skill_err(rs.getString("is_auto_skill_err").equalsIgnoreCase("true"));
       l1skills.setDebuff(rs.getString("debuff").equalsIgnoreCase("true"));

       l1skills.set_magic_dmg_mr_impact(rs.getString("magic_dmg_mr_impact").equalsIgnoreCase("true"));
       l1skills.set_magic_dmg_int_impact(rs.getString("magic_dmg_int_impact").equalsIgnoreCase("true"));
       l1skills.set_Castle_Magic(rs.getString("Castle_Magic").equalsIgnoreCase("true"));
       l1skills.set_SafetyZone_Magic(rs.getString("SafetyZone_Magic").equalsIgnoreCase("true"));

      this._skills.put(Integer.valueOf(skill_id), l1skills); // 使用 Integer.valueOf 改進
       _log.config("技能 " + this._skills.size() + " 筆已加載"); // 記錄技能數量的日志消息


   public void spellMastery(int playerobjid, int skillid, String skillname, int active, int time) {
     if (spellCheck(playerobjid, skillid)) {
       return;
     }
     L1PcInstance pc = (L1PcInstance)L1World.getInstance().findObject(playerobjid);
     if (pc != null) {
       pc.setSkillMastery(skillid);
     }

     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO character_skills SET char_obj_id=?, skill_id=?, skill_name=?, is_active=?, activetimeleft=?");
       pstm.setInt(1, playerobjid);
       pstm.setInt(2, skillid);
       pstm.setString(3, skillname);
       pstm.setInt(4, active);
       pstm.setInt(5, time);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {

       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     SkillCheck.getInstance().AddSkill(playerobjid, skillid);
   }


   public void spellLost(int playerobjid, int skillid) {
     L1PcInstance pc = (L1PcInstance)L1World.getInstance().findObject(playerobjid);
     if (pc != null) {
       pc.removeSkillMastery(skillid);
     }

     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=? AND skill_id=?");
       pstm.setInt(1, playerobjid);
       pstm.setInt(2, skillid);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {

       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     SkillCheck.getInstance().DelSkill(playerobjid, skillid);
   }


   public boolean spellCheck(int playerobjid, int skillid) {
     boolean ret = false;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=? AND skill_id=?");
       pstm.setInt(1, playerobjid);
       pstm.setInt(2, skillid);
       rs = pstm.executeQuery();
       if (rs.next()) {
         ret = true;
       } else {
         ret = false;
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {

       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return ret;
   }

   public boolean isInitialized() {
     return this._initialized;
   }

   public L1Skills getTemplate(int i) {
     return this._skills.get(new Integer(i));
   }

   public L1Skills getTemplateByItem(int itemid) {
     int skillid = 0;
     switch (itemid) {
       case 210121:
         skillid = 225;
         break;
       case 210122:
         skillid = 226;
         break;
       case 210123:
       case 30001555:
         skillid = 228;
         break;
       case 210124:
         skillid = 229;
         break;
       case 210125:
       case 30001553:
       case 30001560:
         skillid = 230;
         break;
       case 3000094:
       case 300001565:
         skillid = 231;
         break;

       case 210127:
         skillid = 234;
         break;
       case 210128:
         skillid = 235;
         break;
       case 210126:
         skillid = 236;
         break;
       case 210129:
         skillid = 237;
         break;
       case 210130:
         skillid = 238;
         break;
       case 210131:
         skillid = 239;
         break;
       case 210132:
         skillid = 240;
         break;
       case 4100540:
         skillid = 247;
         break;
       case 4100712:
         skillid = 5117;
         break;
       case 4100736:
       case 30001561:
         skillid = 5027;
         break;
       case 4101640:
         skillid = 5028;
         break;
     }
     if (skillid == 0)
       return null;
     return this._skills.get(new Integer(skillid));
   }

   public L1Skills findBySkillId(int skillId) {
     return this._skills.get(Integer.valueOf(skillId));
   }

   public L1Skills findByItemName(String itemName) {
     String skillName = itemName.replaceFirst("^.*\\((.+)\\).*$", "$1");
     for (L1Skills skill : this._skills.values()) {
       if (skill.getName().equalsIgnoreCase(skillName)) {
         return skill;
       }
     }
     return null;
   }
 }



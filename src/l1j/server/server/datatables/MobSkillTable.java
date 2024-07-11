 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1MobSkill;
 import l1j.server.server.utils.SQLUtil;





















 public class MobSkillTable
 {
   private static Logger _log = Logger.getLogger(MobSkillTable.class.getName());

   private final boolean _initialized;

   public static MobSkillTable getInstance() {
     if (_instance == null) {
       _instance = new MobSkillTable();
     }
     return _instance;
   }
   private static MobSkillTable _instance; private final HashMap<Integer, L1MobSkill> _mobskills;
   public boolean isInitialized() {
     return this._initialized;
   }

   private MobSkillTable() {
     this._mobskills = new HashMap<>();
     loadMobSkillData();
     this._initialized = true;
   }

   public static void reload() {
     MobSkillTable oldInstance = _instance;
     _instance = new MobSkillTable();
     oldInstance._mobskills.clear();
     oldInstance = null;
   }

   private void loadMobSkillData() {
     Connection con = null;
     PreparedStatement pstm1 = null;
     ResultSet rs1 = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm1 = con.prepareStatement("SELECT mobid,count(*) as cnt FROM mobskill group by mobid");

       int count = 0;
       int mobid = 0;
       int actNo = 0;


       for (rs1 = pstm1.executeQuery(); rs1.next(); ) {
         mobid = rs1.getInt("mobid");
         count = rs1.getInt("cnt");
         ResultSet rs2 = null;
         PreparedStatement pstm2 = null;
         try {
           pstm2 = con.prepareStatement("SELECT * FROM mobskill where mobid = ? order by mobid,actNo");
           pstm2.setInt(1, mobid);
           L1MobSkill mobskill = new L1MobSkill(count);
           mobskill.set_mobid(mobid);

           rs2 = pstm2.executeQuery();
           while (rs2.next()) {
             actNo = rs2.getInt("actNo");
             mobskill.setMobName(rs2.getString("mobname"));
             mobskill.setType(actNo, rs2.getInt("type"));
             mobskill.setTriggerRandom(actNo, rs2.getInt("TriRnd"));
             mobskill.setTriggerHp(actNo, rs2.getInt("TriHp"));
             mobskill.setTriggerCompanionHp(actNo, rs2.getInt("TriCompanionHp"));
             mobskill.setTriggerRange(actNo, rs2.getInt("TriRange"));
             mobskill.setTriggerCount(actNo, rs2.getInt("TriCount"));
             mobskill.setChangeTarget(actNo, rs2.getInt("ChangeTarget"));
             mobskill.setRange(actNo, rs2.getInt("Range"));
             mobskill.setAreaWidth(actNo, rs2.getInt("AreaWidth"));
             mobskill.setAreaHeight(actNo, rs2.getInt("AreaHeight"));
             mobskill.setLeverage(actNo, rs2.getInt("Leverage"));
             mobskill.setSkillId(actNo, rs2.getInt("SkillId"));
             mobskill.setGfxid(actNo, rs2.getInt("Gfxid"));
             mobskill.setActid(actNo, rs2.getInt("Actid"));
             mobskill.setSummon(actNo, rs2.getInt("SummonId"));
             mobskill.setSummonMin(actNo, rs2.getInt("SummonMin"));
             mobskill.setSummonMax(actNo, rs2.getInt("SummonMax"));
             mobskill.setPolyId(actNo, rs2.getInt("PolyId"));
             mobskill.setSpellMent(actNo, rs2.getString("SpellMent"));
           }
           this._mobskills.put(new Integer(mobid), mobskill);
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(pstm2);
           SQLUtil.close(rs2);
         }

       }
     } catch (Exception e2) {
       _log.log(Level.SEVERE, "error while creating mobskill table", e2);
     } finally {

       SQLUtil.close(rs1);
       SQLUtil.close(pstm1);
       SQLUtil.close(con);
     }
   }

   public L1MobSkill getTemplate(int id) {
     return this._mobskills.get(Integer.valueOf(id));
   }
 }



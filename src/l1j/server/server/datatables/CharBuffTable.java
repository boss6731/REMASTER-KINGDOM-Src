 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.util.LinkedList;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.timer.L1SkillTimer;
 import l1j.server.server.templates.L1Skills;
 import l1j.server.server.utils.SQLUtil;










 public class CharBuffTable
 {
   public static final int[] buffSkill = new int[] { 1000, 1001, 1002, 4914, 60208, 60209, 60210, 80007, 1016, 1005, 80012, 80013, 85000, 85001, 7679, 7680, 7681, 7682, 70704, 777777, 22015, 22016, 22060, 23069, 8382, 13069, 20082, 20079, 7893, 7894, 7895, 16553, 16552, 16551, 22017, 3000, 3050, 3001, 3051, 3002, 3052, 3003, 3053, 3004, 3054, 3005, 3055, 3006, 3056, 3008, 3058, 3009, 3059, 3010, 3060, 3011, 3061, 3012, 3062, 3013, 3063, 3014, 3064, 3016, 3066, 3017, 3067, 3018, 3068, 3019, 3069, 3020, 3070, 3021, 3071, 3022, 3072, 3000128, 3000130, 3000129, 50006, 50007, 90008, 80005, 3074, 3075, 3076, 3077, 3100, 3101, 3102, 3103, 22019, 22018, 80009, 80008, 40005, 22000, 22001, 22002, 22003, 70702, 70703, 707070, 32423423, 32423424, 32423425, 80000, 80001, 8001, 71000, 7116, 888810, 888811, 888812, 888813, 888814, 888815, 888816, 888817, 888818, 888822, 888823, 888824, 888825, 888826, 888827, 888828, 888829, 888830, 888832, 888833, 888834, 888835, 888836, 888837, 888838, 888839, 888840 };




























































   public static void DeleteBuff(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_buff WHERE char_obj_id=?");
       pstm.setInt(1, pc.getId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public static void SaveBuff(L1PcInstance pc) {
     if (pc.getAI() != null) {
       return;
     }
     final LinkedList<BuffStoreModel> skillModels = new LinkedList<>();

     for (Map.Entry<Integer, L1SkillTimer> entry : (Iterable<Map.Entry<Integer, L1SkillTimer>>)pc.hasSkills()) {
       L1Skills skill = SkillsTable.getInstance().getTemplate(((Integer)entry.getKey()).intValue());

       if (skill != null &&
         skill.isSave()) {
         int timeSec = pc.getSkillEffectTimeSec(((Integer)entry.getKey()).intValue());
         int polyId = 0;
         if (((Integer)entry.getKey()).intValue() == 67 || ((Integer)entry.getKey()).intValue() == 80012 || ((Integer)entry.getKey()).intValue() == 80013) {
           polyId = pc.getCurrentSpriteId();
         }
         skillModels.add(new BuffStoreModel(((L1SkillTimer)entry.getValue()).skillId(), timeSec, polyId));
       }
     }


     for (int skillId : buffSkill) {
       if (pc.hasSkillEffect(skillId)) {


         int timeSec = pc.getSkillEffectTimeSec(skillId);

         int polyId = 0;
         if (skillId == 67 || skillId == 80012 || skillId == 80013) {
           polyId = pc.getCurrentSpriteId();
         }
         if (skillId == 32423423 || skillId == 32423424 || skillId == 32423425) {
           timeSec = -1;
         }

         skillModels.add(new BuffStoreModel(skillId, timeSec, polyId));
       }
     }












     int size = skillModels.size();
     if (size <= 0) {
       return;
     }
     final int characterId = pc.getId();
     Updator.batch("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?", new BatchHandler()
         {
           public void handle(PreparedStatement pstm, int callNumber) throws Exception {
             CharBuffTable.BuffStoreModel model = skillModels.get(callNumber);
             pstm.setInt(1, characterId);
             pstm.setInt(2, model.skillId);
             pstm.setInt(3, model.timeSec);
             pstm.setInt(4, model.polyId);
           }
         }size);
   }

   private static class BuffStoreModel { int skillId;
     int timeSec;
     int polyId;

     BuffStoreModel(int skillId, int timeSec, int polyId) {
       this.skillId = skillId;
       this.timeSec = timeSec;
       this.polyId = polyId;
     } }

 }



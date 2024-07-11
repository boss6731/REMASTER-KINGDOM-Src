 package l1j.server.server.model;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.SQLUtil;





 public class L1Quest
 {
     public static final int QUEST_LEVEL15 = 1;
     public static final int QUEST_LEVEL30 = 2;
     public static final int QUEST_LEVEL45 = 3;
     public static final int QUEST_LEVEL50 = 4;
     public static final int QUEST_LEVEL5 = 5;
     public static final int QUEST_수련자물품 = 6;
     public static final int QUEST_LEVEL55 = 7;
     public static final int QUEST_FIRSTQUEST = 40;
     public static final int QUEST_LYRA = 10;
     public static final int QUEST_OILSKINMANT = 11;
     public static final int QUEST_DOROMOND = 20;
     public static final int QUEST_RUBA = 21;
     public static final int QUEST_AREX = 22;
     public static final int QUEST_LUKEIN1 = 23;
     public static final int QUEST_TBOX1 = 24;
     public static final int QUEST_TBOX2 = 25;
     public static final int QUEST_TBOX3 = 26;
     public static final int QUEST_SIMIZZ = 27;
     public static final int QUEST_DOIL = 28;
     public static final int QUEST_RUDIAN = 29;
     public static final int QUEST_RESTA = 30;
     public static final int QUEST_CADMUS = 31;
     public static final int QUEST_KAMYLA = 32;
     public static final int QUEST_CRYSTAL = 33;
     public static final int QUEST_LIZARD = 34;
     public static final int QUEST_KEPLISHA = 35;
     public static final int QUEST_DESIRE = 36;
     public static final int QUEST_SHADOWS = 37;
     public static final int QUEST_ROI = 38;
     public static final int QUEST_MOONBOW = 39;
     public static final int QUEST_ICEQUEENRING = 41;
     public static final int QUEST_END = 255;
     public static final int QUEST_RING_LEFT_SLOT60 = 60;
     public static final int QUEST_RING_RIGHT_SLOT60 = 61;
     public static final int QUEST_EARRING_SLOT60 = 62;
     public static final int QUEST_SAI_RUNE70 = 63;
     public static final int QUEST_SLOT_SHOULD = 64;
     public static final int QUEST_SLOT_BADGE = 65;
     public static final int QUEST_HAMO = 80;
     public static final int QUEST_RING_LEFT_SLOT95 = 66;
     public static final int QUEST_RING_RIGHT_SLOT100 = 67;
     public static final int QUEST_EARRING_LEFT_SLOT101 = 68;
     public static final int QUEST_EARRING_RIGHT_SLOT103 = 69;
     private L1PcInstance _owner = null;
     private HashMap<Integer, Integer> _quest = null;

     public L1Quest(L1PcInstance owner) {
         this._owner = owner;
     }

     public L1PcInstance get_owner() {
         return this._owner;
     }


     public int get_step(int quest_id) {
         if (this._quest == null) {

             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
                 this._quest = new HashMap<>();

                 con = L1DatabaseFactory.getInstance().getConnection();
                 pstm = con.prepareStatement("SELECT * FROM character_quests WHERE char_id=?");
                 pstm.setInt(1, this._owner.getId());
                 rs = pstm.executeQuery();

                 while (rs.next()) {
                     this._quest.put(new Integer(rs.getInt(2)), new Integer(rs.getInt(3)));
                 }
             }
             catch (Exception e) {
                 e.printStackTrace();
             } finally {
                 SQLUtil.close(rs);
                 SQLUtil.close(pstm);
                 SQLUtil.close(con);
             }
         }
         Integer step = this._quest.get(new Integer(quest_id));
         if (step == null) {
             return 0;
         }
         return step.intValue();
     }



     public void set_step(int quest_id, int step) {
         Connection con = null;
         PreparedStatement pstm = null;
         try {
             con = L1DatabaseFactory.getInstance().getConnection();
             pstm = con.prepareStatement("insert into character_quests set char_id=?, quest_id=?, quest_step=? on duplicate key update quest_step=?");
             pstm.setInt(1, this._owner.getId());
             pstm.setInt(2, quest_id);
             pstm.setInt(3, step);
             pstm.setInt(4, step);
             pstm.executeUpdate();
         } catch (Exception e) {
             e.printStackTrace();
         } finally {

             SQLUtil.close(pstm);
             SQLUtil.close(con);
         }

         this._quest.put(new Integer(quest_id), new Integer(step));
     }

     public void add_step(int quest_id, int add) {
         int step = get_step(quest_id);
         step += add;
         set_step(quest_id, step);
     }

     public void set_end(int quest_id) {
         set_step(quest_id, 255);
     }

     public boolean isEnd(int quest_id) {
         if (get_step(quest_id) == 255) {
             return true;
         }
         return false;
     }
 }



 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.StringTokenizer;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.CustomQuest;
 import l1j.server.server.templates.eCustomQuestPerformType;
 import l1j.server.server.templates.eCustomQuestType;
 import l1j.server.server.utils.SQLUtil;


 public class ServerCustomQuestTable
 {
   private static ServerCustomQuestTable _instance;
   private List<CustomQuest> _list = new ArrayList<>();

   public static ServerCustomQuestTable getInstance() {
     if (_instance == null) {
       _instance = new ServerCustomQuestTable();
     }
     return _instance;
   }

   public void reload() {
     List<CustomQuest> list = new ArrayList<>();
     tableLoad(list);
     this._list = list;
   }

   private ServerCustomQuestTable() {
     tableLoad(this._list);
   }

   private void tableLoad(List<CustomQuest> list) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     CustomQuest cq = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM server_custom_quest");
       rs = pstm.executeQuery();
       while (rs.next()) {
         cq = new CustomQuest();

         cq.setCollectItemId(rs.getInt("collect_item_id"));
         cq.setQuestPerformType(eCustomQuestPerformType.fromString(rs.getString("quest_perform_type")));
         cq.setQuestType(eCustomQuestType.fromString(rs.getString("quest_type")));
         cq.setRewardItemCount(rs.getInt("reward_item_count"));
         cq.setRewardItemId(rs.getInt("reward_item_id"));
         cq.setSuccessCount(rs.getInt("success_count"));
         cq.setQuestId(rs.getInt("quest_id"));
         cq.setCollectItemDropProb(rs.getInt("collect_item_drop_prob"));
         cq.setQuestName(rs.getString("quest_name"));
         cq.setMinLevel(rs.getInt("min_level"));
         cq.setMaxLevel(rs.getInt("max_level"));

         List<Integer> apply_map_ids = new ArrayList<>();
         try {
           StringTokenizer stt = new StringTokenizer(rs.getString("apply_map_ids"), ",");
           while (stt.hasMoreTokens()) {
             apply_map_ids.add(Integer.valueOf(stt.nextToken()));
           }
           cq.setQuestApplyMapIds(apply_map_ids);
         } catch (Exception e) {
           e.printStackTrace();
         }

         list.add(cq);
       }

     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public CustomQuest getCustomQuest(int questid) {
     return this._list.stream().filter(datas -> (datas.getQuestId() == questid)).findFirst().orElse(null);
   }

   public CustomQuest getCustomQuestByMap(int mapid) {
     return this._list.stream().filter(datas -> datas.getQuestApplyMapIds().contains(Integer.valueOf(mapid))).findFirst().orElse(null);
   }

   public List<CustomQuest> getCustomQuestListByMap(int mapid) {
     List<CustomQuest> new_list = new ArrayList<>();

     for (CustomQuest cq : this._list) {
       if (cq.getQuestApplyMapIds().contains(Integer.valueOf(mapid))) {
         new_list.add(cq);
       }
     }

     return new_list;
   }
 }



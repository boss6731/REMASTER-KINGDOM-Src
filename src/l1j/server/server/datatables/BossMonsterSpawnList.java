 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;
 import java.util.StringTokenizer;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1Boss;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.SQLUtil;



 public final class BossMonsterSpawnList
 {
   private static List<L1Boss> list;
   private static Random _random = new Random(System.nanoTime());


   public static void init() {
     if (list == null)
       list = new ArrayList<>();
     synchronized (list) {
       list.clear();
       Connection con = null;
       PreparedStatement st = null;
       ResultSet rs = null;
       try {
         con = L1DatabaseFactory.getInstance().getConnection();
         st = con.prepareStatement("SELECT * FROM spawnlist_boss_date");
         rs = st.executeQuery();
         while (rs.next()) {
           int npcid = rs.getInt("npcid");
           String mobname = rs.getString("monname");
           String mapname = rs.getString("mapname");
           String npc_type = rs.getString("npc_type");
           int group_id = rs.getInt("group_id");
           int spawn_x = rs.getInt("spawn_x");
           int spawn_y = rs.getInt("spawn_y");
           int spawn_map = rs.getInt("spawn_map");
           int rnd_xy = rs.getInt("rnd_loc");
           int movement_distance = rs.getInt("movement_distance");
           int isment = rs.getInt("isment");
           int isyn = rs.getInt("isyn");
           int display_effect = rs.getInt("display_effect");
           String spawn_time = rs.getString("spawn_time");
           int del_time = rs.getInt("delete_time");
           String[] spawn_yoil = { "전체" };
           String spawn_message = rs.getString("ment");
           String yn_spawn_message = rs.getString("yn_ment");
           int none_spawn = rs.getInt("none_spawn");
           int rnd_time_min = rs.getInt("rnd_time_min");

           try {
             StringTokenizer stt = new StringTokenizer(rs.getString("spawn_yoil"), ",");
             spawn_yoil = new String[stt.countTokens()];
             for (int i = 0; stt.hasMoreTokens(); i++)
               spawn_yoil[i] = stt.nextToken();
           } catch (Exception exception) {}


           L1Npc npc = NpcTable.getInstance().getTemplate(npcid);

           if (npc != null) {
             L1Boss b = new L1Boss();
             b.setMonName(mobname);
             b.setNpcType(npc_type);
             b.setMapName(mapname);
             b.setGroupId(group_id);
             b.setNpcId(npcid);
             b.setX(spawn_x);
             b.setY(spawn_y);
             b.setDeleteTime(del_time);
             b.setYoil(spawn_yoil);
             b.setMap(spawn_map);
             b.setMent((isment == 1));
             b.setYn((isyn == 1));
             b.set_display_effect(display_effect);
             b.setMentMessage(spawn_message);
             b.setYnMessage(yn_spawn_message);
             b.setRndLoc(rnd_xy);
             b.setMovement_distance(movement_distance);
             b.setnonespawntime(none_spawn);
             b.setRndMinuteTime(rnd_time_min);


             if (spawn_time.length() > 0) {
               String[] spawn = spawn_time.split(",");

               int[][] time = new int[spawn.length][2];
               int[][] real_time = new int[spawn.length][2];



               for (int i = 0; i < spawn.length; i++) {
                 String boss_time = spawn[i];
                 String[] boss_result = boss_time.split(":");
                 int boss_h = Integer.valueOf(boss_result[0]).intValue();
                 int boss_m = Integer.valueOf(boss_result[1]).intValue();
                 int rnd_spawn_minute = (rnd_time_min == 0) ? 0 : _random.nextInt(rnd_time_min);
                 real_time[i][0] = boss_h;
                 real_time[i][1] = boss_m;

                 time[i][0] = boss_h;
                 time[i][1] = boss_m + rnd_spawn_minute;

                 while (time[i][1] >= 60) {
                   time[i][1] = time[i][1] - 60;
                   time[i][0] = time[i][0] + 1;
                 }

                 if (time[i][0] >= 24) {
                   time[i][0] = time[i][0] - 24;
                 }
               }

               b.setTime(time);
               b.setRealTime(real_time);
             }
             list.add(b);
           }
         }
       } catch (Exception e) {
         e.getStackTrace();
       } finally {
         SQLUtil.close(rs);
         SQLUtil.close(st);
         SQLUtil.close(con);
       }
     }
   }

   public static List<L1Boss> getList() {
     synchronized (list) {
       return new ArrayList<>(list);
     }
   }

   public static int getSize() {
     return list.size();
   }

   public static L1Boss find(int npcid) {
     synchronized (list) {
       for (L1Boss b : list) {
         if (b.getNpcId() == npcid)
           return b;
       }
       return null;
     }
   }
 }



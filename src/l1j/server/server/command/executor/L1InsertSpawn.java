         package l1j.server.server.command.executor;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.util.StringTokenizer;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.datatables.MapsTable;
         import l1j.server.server.datatables.NpcSpawnTable;
         import l1j.server.server.datatables.NpcTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.templates.L1Npc;
         import l1j.server.server.utils.L1SpawnUtil;
         import l1j.server.server.utils.SQLUtil;



         public class L1InsertSpawn
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1InsertSpawn();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             String msg = null;

             try {
               StringTokenizer tok = new StringTokenizer(arg);
               String type = tok.nextToken();
               int npcId = Integer.parseInt(tok.nextToken().trim());
               L1Npc template = NpcTable.getInstance().getTemplate(npcId);

               if (template == null) {
                 msg = "未發現相應的NPC。";
                 return;
               }
               if (type.equals("暴民")) {
                 if (!template.getImpl().equals("L1Monster")) {
                   msg = "指定的 NPC 不是 L1Monster 。";

                   return;
                 }
                 storeSpawn(pc, template);
               } else if (type.equals("n")) {
                 NpcSpawnTable.getInstance().storeSpawn(pc, template);
                 L1SpawnUtil.spawngmcmd(pc, npcId);
                 return;
               }
               L1SpawnUtil.spawn(pc, npcId, 0, 0);
               msg = template.get_name() + " (" + npcId + ") " + "添加。";
             } catch (Exception e) {

               msg = cmdName + " 請輸入[怪物][NPCID]。";
             } finally {
               if (msg != null) {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
               }
             }
           }

           public static void storeSpawn(L1PcInstance pc, L1Npc npc) {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               int count = 1;
               int randomXY = 3;
               int movement_distance = 24;
               int spawn_type = 0;
               int minRespawnDelay = 30;
               int maxRespawnDelay = 60;
               String note = npc.get_name();

               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("INSERT INTO spawnlist_ex_normal SET location_name=?,count=?,npc_id=?,group_id=?,loc_x=?,loc_y=?,area_left=?,area_top=?,area_right=?,area_bottom=?,mapid=?,movement_distance=?,spawn_type=?,min_respawn_seconds=?,max_respawn_seconds=?,note_map_name=?");
               pstm.setString(1, note);
               pstm.setInt(2, count);
               pstm.setInt(3, npc.get_npcId());
               pstm.setInt(4, 0);
               pstm.setInt(5, pc.getX());
               pstm.setInt(6, pc.getY());
               pstm.setInt(7, pc.getX() - randomXY);
               pstm.setInt(8, pc.getY() - randomXY);
               pstm.setInt(9, pc.getX() + randomXY);
               pstm.setInt(10, pc.getY() + randomXY);
               pstm.setInt(11, pc.getMapId());
               pstm.setInt(12, movement_distance);
               pstm.setInt(13, spawn_type);
               pstm.setInt(14, minRespawnDelay);
               pstm.setInt(15, maxRespawnDelay);
               pstm.setString(16, MapsTable.getInstance().getMapName(pc.getMapId()));
               pstm.execute();
             }
             catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }
         }



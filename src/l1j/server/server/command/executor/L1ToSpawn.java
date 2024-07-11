         package l1j.server.server.command.executor;

         import java.util.HashMap;
         import java.util.Map;
         import java.util.StringTokenizer;
         import l1j.server.server.datatables.NpcSpawnTable;
         import l1j.server.server.datatables.SpawnTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Spawn;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;

         public class L1ToSpawn implements L1CommandExecutor {
           private static final Map<Integer, Integer> _spawnId = new HashMap<>();




           public static L1CommandExecutor getInstance() {
             return new L1ToSpawn();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               if (!_spawnId.containsKey(Integer.valueOf(pc.getId()))) {
                 _spawnId.put(Integer.valueOf(pc.getId()), Integer.valueOf(0));
               }
               int id = ((Integer)_spawnId.get(Integer.valueOf(pc.getId()))).intValue();
               if (arg.isEmpty() || arg.equals("+")) {
                 id++;
               } else if (arg.equals("-")) {
                 id--;
               } else {
                 StringTokenizer st = new StringTokenizer(arg);
                 id = Integer.parseInt(st.nextToken());
               }
               L1Spawn spawn = NpcSpawnTable.getInstance().getTemplate(id);
               if (spawn == null) {
                 spawn = SpawnTable.getInstance().getTemplate(id);
               }
               if (spawn != null) {
                 pc.start_teleport(spawn.getLocX(), spawn.getLocY(), spawn.getMapId(), 5, 18339, false, false);
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("spawnid(" + id + ") 飛回其原始狀態。"));
               } else {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("spawnid(" + id + ")沒有找到"));
               }
               _spawnId.put(Integer.valueOf(pc.getId()), Integer.valueOf(id));
             } catch (Exception exception) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " [產生 ID] [+,-]"));
             }
           }
         }



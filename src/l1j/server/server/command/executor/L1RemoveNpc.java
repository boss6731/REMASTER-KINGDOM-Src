         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.Controller.NpcDeleteController;
         import l1j.server.server.datatables.NpcSpawnTable;
         import l1j.server.server.model.Instance.L1NpcInstance;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Object;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;




         public class L1RemoveNpc
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1RemoveNpc();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               int time;
               StringTokenizer tok = new StringTokenizer(arg);

               int npcid = Integer.parseInt(tok.nextToken());


               try {
                 time = Integer.parseInt(tok.nextToken());
               } catch (Exception e) {
                 time = 0;
               }

               for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)pc)) {
                 if (obj instanceof L1NpcInstance) {
                   L1NpcInstance npc = (L1NpcInstance)obj;

                   if (npc.getNpcId() == npcid) {
                     NpcSpawnTable.getInstance().removeSpawn(npc);
                     npc.setRespawn(false);
                     npc.NpcDeleteTime = System.currentTimeMillis() + (time * 60 * 1000);
                     NpcDeleteController.getInstance().addNpcDelete(npc);
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage(npc.getName() + "第二(投擲) " + time + "一分鐘後刪除。"));
                   }

                 }
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(".刪除【時間（分鐘）】（如果在視野中輸入NPC的ID，則會在輸入時間後刪除（也適用於DB））"));
             }
           }
         }



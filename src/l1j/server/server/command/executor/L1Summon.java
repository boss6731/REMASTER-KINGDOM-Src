         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.datatables.NpcTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.Instance.L1SummonInstance;
         import l1j.server.server.model.L1Character;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.templates.L1Npc;


         public class L1Summon
           implements L1CommandExecutor
         {
           public static L1Summon getInstance() {
             return new L1Summon();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer tok = new StringTokenizer(arg);
               String nameid = tok.nextToken();
               int npcid = 0;
               try {
                 npcid = Integer.parseInt(nameid);
               } catch (NumberFormatException e) {
                 npcid = NpcTable.getInstance().findNpcIdByNameWithoutSpace(nameid);

                 if (npcid == 0) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("找不到 NPC。"));
                   return;
                 }
               }
               int count = 1;
               if (tok.hasMoreTokens()) {
                 count = Integer.parseInt(tok.nextToken());
               }
               L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
               L1SummonInstance summonInst = null;
               for (int i = 0; i < count; i++) {
                 summonInst = new L1SummonInstance(npc, (L1Character)pc);
                 summonInst.setPetcost(0);
               }
               nameid = NpcTable.getInstance().getTemplate(npcid).get_name();
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(nameid + "(ID:" + npcid + ") (" + count + ")召喚。"));
             }
             catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[npcid或名稱][Summonsu]。"));
             }
           }
         }



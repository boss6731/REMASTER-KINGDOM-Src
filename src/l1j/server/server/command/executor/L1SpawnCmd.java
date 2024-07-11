         package l1j.server.server.command.executor;

         import java.util.NoSuchElementException;
         import java.util.StringTokenizer;
         import l1j.server.server.GeneralThreadPool;
         import l1j.server.server.datatables.NpcTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.templates.L1Npc;
         import l1j.server.server.utils.L1SpawnUtil;




         public class L1SpawnCmd
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1SpawnCmd();
           }

           private void sendErrorMessage(L1PcInstance pc, String cmdName) {
             String errorMsg = cmdName + "請輸入.npcid|名稱 [編號] [範圍]。";
             pc.sendPackets((ServerBasePacket)new S_SystemMessage(errorMsg));
           }

           private int parseNpcId(String nameId) {
             int npcid = 0;
             try {
               npcid = Integer.parseInt(nameId);
             } catch (NumberFormatException e) {
               npcid = NpcTable.getInstance().findNpcIdByNameWithoutSpace(nameId);
             }
             return npcid;
           }


             public void execute(final L1PcInstance pc, final String cmdName, final String arg) {
                 GeneralThreadPool.getInstance().execute(new Runnable() {
                     public void run() {
                         try {
// 使用 StringTokenizer 分割參數
                             StringTokenizer tok = new StringTokenizer(arg);
                             String nameId = tok.nextToken();

// 默認生成數量為 1
                             int count = 1;
// 如果有更多參數，解析生成數量
                             if (tok.hasMoreTokens()) {
                                 count = Integer.parseInt(tok.nextToken());
                             }

// 默認隨機範圍為 0
                             int randomrange = 0;
// 如果有更多參數，解析隨機範圍
                             if (tok.hasMoreTokens()) {
                                 randomrange = Integer.parseInt(tok.nextToken(), 10);
                             }

// 解析 NPC ID
                             int npcid = L1SpawnCmd.this.parseNpcId(nameId);

// 從 NPC 表中獲取 NPC 模板
                             L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
                             if (npc == null) {
// 如果 NPC 未找到，發送消息給玩家
                                 pc.sendPackets("未找到對應的 NPC。");
                                 return;
                             }

// 生成指定數量的 NPC
                             for (int i = 0; i < count; i++) {
                                 L1SpawnUtil.spawnAndGet(pc, npcid, randomrange, 0);
                             }

// 構建並發送成功消息
                             String msg = String.format("%s(%d) (%d) 已生成。 (範圍:%d)", new Object[] { npc.get_name(), Integer.valueOf(npcid), Integer.valueOf(count), Integer.valueOf(randomrange) });
                             pc.sendPackets(msg);

                         } catch (NoSuchElementException e) {
// 捕獲 NoSuchElementException 異常，發送錯誤消息
                             L1SpawnCmd.this.sendErrorMessage(pc, cmdName);
                         } catch (NumberFormatException e) {
// 捕獲 NumberFormatException 異常，發送錯誤消息
                             L1SpawnCmd.this.sendErrorMessage(pc, cmdName);
                         } catch (Exception e) {
// 捕獲其他異常，打印堆棧跟蹤並發送內部錯誤消息
                             e.printStackTrace();
                             pc.sendPackets(cmdName + " 內部錯誤。");
                         }
                     }
                 });
             }



         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.datatables.ExpTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.utils.IntRange;




         public class L1Level
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Level();
           }


           public void execute(L1PcInstance pc, String cmdName, String poby) {
             try {
               StringTokenizer token = new StringTokenizer(poby);
               String name = token.nextToken();
               int level = Integer.parseInt(token.nextToken());

               L1PcInstance target = L1World.getInstance().getPlayer(name);
               if (target == null) {
                 pc.sendPackets("沒有這樣的等級。");

                 return;
               }
               if (!IntRange.includes(level, 1, 127)) {
                 pc.sendPackets("您輸入的等級超出範圍了。 (1~99)");
                 return;
               }
               int percent = 0;
               if (token.hasMoreTokens()) {
                 percent = Integer.parseInt(token.nextToken());
               }
               if (!IntRange.includes(percent, 0, 127)) {
                 pc.sendPackets("您輸入的百分比超出範圍。 (0~99)");

                 return;
               }
               long exp = ExpTable.getExpByLevel(level);
               exp = (long)(exp + percent * 0.01D * ExpTable.getNeedExpNextLevel(level));
               target.set_exp(exp);
               pc.sendPackets("(레벨 변경) " + target.getName() + " - Lv. " + level + " / Exp. " + percent + "%");
             } catch (Exception e) {

               pc.sendPackets("." + cmdName + " （角色名稱）（等級：1-127）（百分比：0-99）");
             }
           }
         }



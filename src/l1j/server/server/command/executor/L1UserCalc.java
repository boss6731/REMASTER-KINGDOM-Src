         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;




         public class L1UserCalc
           implements L1CommandExecutor
         {
           private static int calcUser = 0;

           public static L1CommandExecutor getInstance() {
             return new L1UserCalc();
           }

           public static int getClacUser() {
             return calcUser;
           }

           public void execute(L1PcInstance pc, String cmdName, String arg) {
             String msg = null;

             try {
               StringTokenizer tok = new StringTokenizer(arg);
               String type = tok.nextToken();
               int count = Integer.parseInt(tok.nextToken());

               if (type.equalsIgnoreCase("+")) {
                 calcUser += count;
                 msg = "彈出：" + count + "新增名稱/目前彈出：" + calcUser + "人數";
               } else if (type.equalsIgnoreCase("-")) {
                 int temp = calcUser - count;
                 if (temp < 0) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("爆米花不能-。 目前的廢話：" + calcUser));
                   return;
                 }
                 calcUser = temp;
                 msg = "彈出：" + count + "減少人數/目前爆裂聲：" + calcUser + "人數";
               }

             } catch (Exception e) {

               msg = cmdName + " [+,-] [COUNT] 輸入";
             } finally {
               if (msg != null)
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
             }
           }
         }



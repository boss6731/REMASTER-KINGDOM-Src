         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import java.util.logging.Logger;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_PacketBox;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;

         public class L1料理圖標
           implements L1CommandExecutor {
           private static Logger _log = Logger.getLogger(L1料理圖標.class.getName());




           public static L1CommandExecutor getInstance() {
             return new L1料理圖標();
           }

           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               int _sprid = Integer.parseInt(st.nextToken(), 10);
               int count = Integer.parseInt(st.nextToken(), 10);
               for (int i = 0; i < count; i++) {
                 try {
                   Thread.sleep(2000L);
                   int num = _sprid + i;
                   pc.sendPackets((ServerBasePacket)new S_PacketBox(53, _sprid + i, 10));
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("料理圖標：" + num + " 數量"));
                 } catch (Exception exception) {
                   break;
                 }
               }
             } catch (Exception exception) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[id]作為[出現的號碼]。"));
             }
           }
         }



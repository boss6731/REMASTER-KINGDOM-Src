         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_ServerMessage;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1ServerMessage
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1ServerMessage();
           }

             public void execute(L1PcInstance pc, String cmdName, String arg) {
                 try {
                     StringTokenizer st = new StringTokenizer(arg);
                     int ment = Integer.parseInt(st.nextToken(), 10);
                     int count = Integer.parseInt(st.nextToken(), 10);

                     for (int i = 0; i <= count; i++) {
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(ment + i));

                         pc.sendPackets((ServerBasePacket)new S_SystemMessage("(" + (ment + i) + ") 的訊息如上所示"));
                     }
                 } catch (Exception e) {
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage(".訊息 [編號] [數量] 請輸入。"));
                 }



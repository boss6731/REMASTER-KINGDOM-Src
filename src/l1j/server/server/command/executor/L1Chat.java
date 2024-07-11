 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1Chat
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1Chat();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer st = new StringTokenizer(arg);
       if (st.hasMoreTokens()) {
         String msg, flag = st.nextToken();

         if (flag.compareToIgnoreCase("開") == 0) {
           L1World.getInstance().set_worldChatElabled(true);
           msg = "世界聊天已啟用。";
         } else if (flag.compareToIgnoreCase("關") == 0) {
           L1World.getInstance().set_worldChatElabled(false);
           msg = "世界聊天已停止。";
         } else {
           throw new Exception();
         }
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
       } else {
         String msg;
         if (L1World.getInstance().isWorldChatElabled()) {
           msg = "世界聊天目前處於活動狀態，您可以透過關閉聊天來停止它。";
         } else {
           msg = "世界聊天目前已停止，您可以透過開啟聊天來啟用它。";
         }
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
       }
     } catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " [開,關]"));
     }
   }
 }



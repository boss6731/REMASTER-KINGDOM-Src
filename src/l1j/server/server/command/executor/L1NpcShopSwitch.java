         package l1j.server.server.command.executor;

         import java.util.logging.Logger;
         import l1j.server.server.NpcShopSystem;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;




         public class L1NpcShopSwitch
           implements L1CommandExecutor
         {
           private static Logger _log = Logger.getLogger(L1NpcShopSwitch.class.getName());




           public static L1CommandExecutor getInstance() {
             return new L1NpcShopSwitch();
           }

           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               boolean power = NpcShopSystem.getInstance().isPower();
               if (arg.equalsIgnoreCase("開")) {
                 if (power) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("已經在運行中。"));
                   return;
                 }
                 NpcShopSystem.getInstance().npcShopStart();
               }
               else if (arg.equalsIgnoreCase("關")) {
                 if (!power) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("未執行 。"));
                   return;
                 }
                 NpcShopSystem.getInstance().npcShopStop();
               } else {

                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(".EnglishStore 開/關"));
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(".英文儲存方法錯誤"));
             }
           }
         }



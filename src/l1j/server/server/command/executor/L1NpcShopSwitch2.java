         package l1j.server.server.command.executor;

         import java.util.logging.Logger;
         import l1j.server.server.NpcShopSystem2;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;

         public class L1NpcShopSwitch2
           implements L1CommandExecutor {
           private static Logger _log = Logger.getLogger(L1NpcShopSwitch2.class.getName());




           public static L1CommandExecutor getInstance() {
             return new L1NpcShopSwitch2();
           }

           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               boolean power = NpcShopSystem2.getInstance().isPower();
               if (arg.equalsIgnoreCase("開")) {
                 if (power) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("已經在運行中。"));
                   return;
                 }
                 NpcShopSystem2.getInstance().npcShopStart();
               }
               else if (arg.equalsIgnoreCase("關")) {
                 if (!power) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("未執行 。"));
                   return;
                 }
                 NpcShopSystem2.getInstance().npcShopStop();
               } else {

                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(".EnglishStore2 開/關"));
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(".英文商店2方法錯誤"));
             }
           }
         }



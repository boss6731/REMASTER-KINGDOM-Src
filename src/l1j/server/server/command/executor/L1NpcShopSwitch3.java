         package l1j.server.server.command.executor;

         import java.util.logging.Logger;
         import l1j.server.server.NpcShopSystem3;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;




         public class L1NpcShopSwitch3
           implements L1CommandExecutor
         {
           private static Logger _log = Logger.getLogger(L1NpcShopSwitch3.class.getName());




           public static L1CommandExecutor getInstance() {
             return new L1NpcShopSwitch3();
           }

           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               boolean power = NpcShopSystem3.getInstance().isPower();
               if (arg.equalsIgnoreCase("開")) {
                 if (power) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("已經在運行了。"));
                   return;
                 }
                 NpcShopSystem3.getInstance().npcShopStart();
               }
               else if (arg.equalsIgnoreCase("關")) {
                 if (!power) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("沒有運行。"));
                   return;
                 }
                 NpcShopSystem3.getInstance().npcShopStop();
               } else {

                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(".EnglishStore3 開、關"));
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(".英文商店3方法錯誤"));
             }
           }
         }



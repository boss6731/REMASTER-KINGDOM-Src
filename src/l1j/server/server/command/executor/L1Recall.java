         package l1j.server.server.command.executor;

         import java.util.ArrayList;
         import java.util.Collection;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Character;
         import l1j.server.server.model.L1Location;
         import l1j.server.server.model.L1Teleport;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1Recall
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Recall();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               Collection<L1PcInstance> targets = null;
               if (arg.equalsIgnoreCase("召喚")) {
                 targets = L1World.getInstance().getAllPlayers();
               } else {
                 targets = new ArrayList<>();
                 L1PcInstance tg = L1World.getInstance().getPlayer(arg);
                 if (tg == null) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("沒有這樣的人物。"));
                   return;
                 }
                 targets.add(tg);
               }

               for (L1PcInstance target : targets) {
                 if (target.isPrivateShop()) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage(target.getName() + " 您正處於商店模式。"));

                   return;
                 }
                 L1Location loc = L1Teleport.getInstance().召喚傳送((L1Character)pc, 1);
                 target.start_teleportForGM(loc.getX(), loc.getY(), loc.getMapId(), target.getHeading(), 18339, true, true);
               }

             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + ".召喚傳送：請輸入角色名稱。"));
             }
           }
         }



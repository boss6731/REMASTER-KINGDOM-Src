         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1ClanJoinInstance;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_Disconnect;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1Kick
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Kick();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               L1PcInstance target = L1World.getInstance().getPlayer(arg);

               if (target != null) {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(target.getName() + " 驅逐了!"));
                 L1ClanJoinInstance.ban_user(target);
                 target.getNetConnection().kick();
                 target.getNetConnection().close();
                 target.sendPackets((ServerBasePacket)new S_Disconnect());
               } else {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("這種名字的角色在世界上是不存在的。"));
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 輸入角色名稱。"));
             }
           }
         }



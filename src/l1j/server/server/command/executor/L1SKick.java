         package l1j.server.server.command.executor;

         import l1j.server.server.GameServer;
         import l1j.server.server.datatables.IpTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_Disconnect;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;

         public class L1SKick
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1SKick();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               L1PcInstance target = L1World.getInstance().getPlayer(arg);

               IpTable iptable = IpTable.getInstance();
               if (target != null) {
                 if (target.getNetConnection() != null) {
                   iptable.banIp(target.getNetConnection().getIp());
                 }
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(target.getName() + " 被強烈驅逐。"));
                 target.setX(33080);
                 target.setY(33392);
                 target.setMap((short)4);
                 GameServer.disconnectChar(target);
                 target.sendPackets((ServerBasePacket)new S_Disconnect());
               } else {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("世界上不存在具有這個名字的角色。 "));
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱]。"));
             }
           }
         }



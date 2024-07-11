         package l1j.server.server.command.executor;

         import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_Invis;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;



         public class L1Visible
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Visible();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               pc.setGmInvis(false);
               pc.killSkillEffectTimer(60);
               pc.sendPackets((ServerBasePacket)new S_Invis(pc.getId(), 0));
               pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("透明度已取消。"));
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 命令錯誤"));
             }
           }
         }



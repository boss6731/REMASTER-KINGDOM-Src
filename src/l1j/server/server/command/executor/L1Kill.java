         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1Kill
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Kill();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               L1PcInstance target = L1World.getInstance().getPlayer(arg);

               if (target != null) {
                 target.setCurrentHp(0);
                 target.death(null, true);
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱]。"));
             }
           }
         }



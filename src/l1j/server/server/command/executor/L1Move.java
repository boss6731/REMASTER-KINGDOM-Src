         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;



         public class L1Move
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Move();
           }

           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               int mapid;
               StringTokenizer st = new StringTokenizer(arg);
               int locx = Integer.parseInt(st.nextToken());
               int locy = Integer.parseInt(st.nextToken());

               if (st.hasMoreTokens()) {
                 mapid = Integer.parseInt(st.nextToken());
               } else {
                 mapid = pc.getMapId();
               }
               pc.do_simple_teleport(locx, locy, mapid);
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("座標 " + locx + ", " + locy + ", " + mapid + "移動到了。"));
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[X座標][Y座標][地圖ID]。"));
             }
           }
         }



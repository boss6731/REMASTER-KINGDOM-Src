         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.map.L1WorldMap;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1Loc
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Loc();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               int locx = pc.getX();
               int locy = pc.getY();
               short mapid = pc.getMapId();
               int gab = L1WorldMap.getInstance().getMap(mapid).getOriginalTile(locx, locy);
               int g2 = L1WorldMap.getInstance().getMap(mapid).getTestTile(locx, locy);

               String msg = String.format("座標（%d、%d、%d）%d %d、%s", new Object[] { Integer.valueOf(locx), Integer.valueOf(locy), Short.valueOf(mapid), Integer.valueOf(gab), Integer.valueOf(g2), String.valueOf(L1WorldMap.getInstance().getMap(mapid).isPassable(locx, locy)) });

               System.out.println(msg);
               System.out.println(L1WorldMap.getInstance().getMap(mapid).isPassable(locx, locy, pc.getHeading()));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
             } catch (Exception e) {
               e.printStackTrace();
             }
           }
         }



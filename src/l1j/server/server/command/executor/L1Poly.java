         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Character;
         import l1j.server.server.model.L1PolyMorph;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_ServerMessage;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1Poly
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Poly();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               String name = st.nextToken();
               int polyid = Integer.parseInt(st.nextToken());

               L1PcInstance tg = L1World.getInstance().getPlayer(name);

               if (tg == null) {
                 pc.sendPackets((ServerBasePacket)new S_ServerMessage(73, name));
               } else {
                 try {
                   L1PolyMorph.doPoly((L1Character)tg, polyid, 604800, 2, false, false);
                 } catch (Exception exception) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入.變身[角色名稱][圖形ID]。"));
                 }
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱]和[圖形ID]。"));
             }
           }
         }



 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SkillIconGFX;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class L1ChatNG
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1ChatNG();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer st = new StringTokenizer(arg);
       String name = st.nextToken();
       int time = Integer.parseInt(st.nextToken());


       L1PcInstance tg = L1World.getInstance().getPlayer(name);

       if (tg != null) {
         tg.setSkillEffect(1005, (time * 60 * 1000));
         tg.sendPackets((ServerBasePacket)new S_SkillIconGFX(36, time * 60));
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(name + " 沒有聊天：" + String.valueOf(time) + "應用分鐘數。"));
       } else {

         pc.sendPackets((ServerBasePacket)new S_SystemMessage("角色未連線。"));
       }
     } catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 輸入[角色名稱][分鐘]。"));
     }
   }
 }



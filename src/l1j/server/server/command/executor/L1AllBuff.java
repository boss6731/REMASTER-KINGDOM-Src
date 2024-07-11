 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.L1BuffUtil;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class L1AllBuff
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1AllBuff();
   }

   public void execute(L1PcInstance pc, String cmdName, String poby) {
     try {
       int buffType = 0;

       StringTokenizer token = new StringTokenizer(poby);
       String type = token.nextToken();
       if (type.equals("1")) {
         buffType = 1;
       } else if (type.equals("2")) {
         buffType = 2;
       } else if (type.equals("3")) {
         buffType = 3;
       } else if (type.equals("4")) {
         buffType = 4;
       } else {
         throw new Exception();
       }

       String range = null;
       if (token.hasMoreTokens()) {
         range = token.nextToken();
       }
       L1BuffUtil.startAllBuffThread(pc, range, buffType);
     } catch (Exception e) {
       if (pc != null) {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\f3." + cmdName + " （類型）"));
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\f3（類型） [1] 增益/[2] 祝福/[3] 黑死病/[4] 昏迷"));
       }
     }
   }
 }



 package l1j.server.server.command.executor;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1AttackLog
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1AttackLog();
   }


   public void execute(L1PcInstance pc, String cmdName, String poby) {
     if (poby.equals("開")) {
       pc.setAttackLog(true);
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aH攻擊日誌已啟動。"));
     } else if (poby.equals("關")) {
       pc.setAttackLog(false);
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aH攻擊日誌已被停用。"));
     } else {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aH." + cmdName + " <開/關>"));
     }
   }
 }



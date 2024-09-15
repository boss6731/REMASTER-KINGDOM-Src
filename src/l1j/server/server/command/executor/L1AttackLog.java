package l1j.server.server.server.command.executor;

import l1j.server.server.serverpackets.S_SystemMessage;

public class L1AttackLog
  implements l1j.server.server.command.executor.L1CommandExecutor
{
  public static L1AttackLog getInstance()
  {
    return new L1AttackLog();
  }

  public void execute(L1Object pc, String cmdName, String poby)
  {
    if (poby.equals("開啟")) {
      pc.setAttackLog(true);
      pc.sendPackets(String.valueOf(new S_SystemMessage("\\aH攻擊記錄已啟用。")));
    } else if (poby.equals("關閉")) {
      pc.setAttackLog(false);
      pc.sendPackets(String.valueOf(new S_SystemMessage("\\aH攻擊記錄已停用。")));
    } else {
      pc.sendPackets(String.valueOf(new S_SystemMessage("\\aH." + cmdName + " <開啟/關閉>")));
    }
  }
}
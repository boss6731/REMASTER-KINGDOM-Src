package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashResultDatabase;
import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashUserInfo;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1AutoCashInfoAdd implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1AutoCashInfoAdd() {  }

	public static <L1CommandExecutor> L1CommandExecutor getInstance() {
		return (L1CommandExecutor) new L1AutoCashInfoAdd();
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			
			AutoCashUserInfo acui = AutoCashResultDatabase.getIntstance().getAutoCashUserInfo(name);
			if(acui != null) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("已有相同的存款人名稱。請輸入其他名稱。")));
				pc.sendPackets(String.valueOf(new S_SystemMessage("** 注意事項：存款時名稱必須相同。**")));
				return;
			} else {
				acui = new AutoCashUserInfo();
				acui.setAccountName(pc.getAccountName());
				acui.setCharName(pc.getName());
				
				AutoCashResultDatabase.getIntstance().addAutoCashUserInfo(name, acui);
				pc.sendPackets(String.valueOf(new S_SystemMessage("** 存款人名稱已正常登記。**")));
				pc.sendPackets(String.valueOf(new S_SystemMessage("** 注意事項：存款時名稱必須相同。**")));
			}
		} catch (Exception exception) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [存款人名稱] 請這樣輸入。 ")));
		}
	}
}

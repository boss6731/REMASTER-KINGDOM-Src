package l1j.server.server.server.command.executor;

import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1ServerMessage implements L1CommandExecutor {
	
	private L1ServerMessage(){ }
	
	public static L1CommandExecutor getInstance(){
		return new L1ServerMessage();
	}
	
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int ment = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);

			for (int i = 0; i <= count; i++ ) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(ment + i)));
				pc.sendPackets(String.valueOf(new S_SystemMessage("(" + (ment + i) + ") 的訊息和上方所述相同")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(".訊息 [號碼] [數量] 請輸入。")));
		}
	}

}

package l1j.server.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_SystemMessage;

public abstract class L1Adena implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1Adena() {  }

	public static <L1CommandExecutor> L1CommandExecutor getInstance() {
		return (L1CommandExecutor) new L1Adena();
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {
		try {
			
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			
			int count = Integer.parseInt(stringtokenizer.nextToken());
			L1ItemInstance adena;
            adena = pc.getInventory(). storeItem(L1ItemId.ADENA, count);

            if (adena != null) {
				pc.sendPackets(String.valueOf(new S_SystemMessage((new StringBuilder()).append(count).append("金幣已生成。").toString())));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage((new StringBuilder()).append("請輸入：.金幣 [數量]").toString())));
		}
	}
}

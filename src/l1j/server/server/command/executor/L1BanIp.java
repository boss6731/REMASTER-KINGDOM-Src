package l1j.server.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.server.datatables.IpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1BanIp implements l1j.server.server.command.executor.L1CommandExecutor {
	
	private L1BanIp() {  }

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1BanIp();
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {
		try {

			StringTokenizer stringtokenizer = new StringTokenizer(arg); // 指定 IP
			String s1 = stringtokenizer.nextToken(); // 指定 add/del（可以不指定）
			String s2 = null;
			
			try {
				s2 = stringtokenizer.nextToken();
			} catch (Exception e) {		}

			IpTable iptable = IpTable.getInstance();
			boolean isBanned = iptable.isBannedIp(s1);

			for (L1PcInstance tg : L1World.getInstance(). getAllPlayers()) {
				if (tg.getNetConnection() != null && s1.equals(tg.getNetConnection(). getIp())) {
					String msg = new StringBuilder().append("IP: ").append(s1).append(" 正在連接的玩家: ").append(tg.getName()).toString();
					pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
				}
			}

			if ("添加".equals(s2) && !isBanned) {
				iptable.banIp(s1); // 將 IP 添加到 BAN 列表中
				String msg = new StringBuilder().append("IP: ").append(s1).append(" 已被添加到 BAN IP列表。").toString();
				pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
			} else if ("刪除".equals(s2) && isBanned) {
				if (iptable.liftBanIp(s1)) { // 從 BAN 列表中刪除 IP
					String msg = new StringBuilder().append("IP: ").append(s1).append(" 已從 BAN IP列表中刪除。").toString();
					pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
				}
			} else { // 檢查 BAN 狀態
				if (isBanned) {
					String msg = new StringBuilder().append("IP: ").append(s1).append(" 已經在 BAN IP列表中。").toString();
					pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
				} else {
					String msg = new StringBuilder().append("IP: ").append(s1).append(" 不在 BAN IP列表中。").toString();
					pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
				}
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 IP [添加, 刪除]。")));
		}
	}
}

package l1j.server.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.Account;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1AccountBanKick implements L1CommandExecutor {

	private L1AccountBanKick() {	}

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1AccountBanKick();
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String				name	= st.nextToken();
			if(name == null || name.equalsIgnoreCase(""))
				throw new Exception("");

			int reason = S_LoginResult.banServerCodes.get(Integer.parseInt(st.nextToken()));

			L1PcInstance target = L1World.getInstance().getPlayer(name);
			if (target == null) {
				target = CharacterTable.getInstance().restoreCharacter(name);
			}

			if (target != null) { // 將帳號 BAN
				final GameClient clnt = target.getNetConnection();
				Account.ban(target.getAccountName(), reason);
				pc.sendPackets(String.valueOf(new S_SystemMessage(target.getName() + " 的帳號已被凍結。")));

				target.sendPackets(new S_Disconnect());

				if (target.getOnlineStatus() == 1) {
					target.sendPackets(new S_Disconnect());
				}
				GeneralThreadPool.getInstance().schedule(new Runnable(){
					@Override
					public void run(){
						if(clnt != null && clnt.isConnected()){
							try {
								clnt.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}, 1000L);
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("該名稱的角色在世界中不存在。")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入：.帳號凍結 [角色名] [凍結原因號碼]")));
			pc.sendPackets(String.valueOf(new S_SystemMessage("[凍結原因]: 1(固定)。違反社會風氣... 其他無")));
		}
	}
}



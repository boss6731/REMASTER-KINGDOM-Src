package l1j.server.server.server.command.executor;

import java.util.Collection;

import l1j.server.server.model.L1World;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.command.executor.L1UserCalc;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.server.serverpackets.S_WhoAmount;

public class L1Who implements L1CommandExecutor {

	private L1Who() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Who();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int CalcUser = L1UserCalc.getClacUser();
			Collection<L1PcInstance> players = L1World.getInstance().getAllPlayers();
			int robotcount = 0; // 無人
			int playercount = 0; 
			int AutoShopUser = 0;
			for (L1PcInstance each : players) {
				if(each.noPlayerCK || each.noPlayerck2)
					robotcount++;
				else if (each.isPrivateShop() && each.getNetConnection() == null) {
					AutoShopUser++;
				} else {
					playercount++;
				}
			}
			String amount = String.valueOf(playercount);
			S_WhoAmount s_whoamount = new S_WhoAmount(amount);
			pc.sendPackets(String.valueOf(s_whoamount));
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "\\aD=========== 在線玩家數 ===========")));
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "\\aL[機器人] : " + robotcount)));
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "\\aH[玩家] 總人數： " + playercount)));
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "\\aJ[無人商店] : " + AutoShopUser)));
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "\\aE[膨脹] : " + CalcUser)));


			// 顯示線上玩家列表
			if (arg.equalsIgnoreCase("全部")) {
				StringBuffer gmList = new StringBuffer();
				StringBuffer playList = new StringBuffer();
				StringBuffer shopList = new StringBuffer();
				StringBuffer robotList = new StringBuffer();

				int countGM = 0, countPlayer = 0, countShop = 0, countRobot = 0;

				for (L1PcInstance each : players) {
					if (each.isGm()) {
						gmList.append(each.getName() + ", ");
						countGM++;
						continue;
					}
					if (each.noPlayerCK || each.noPlayerck2) {
						robotList.append(each.getName() + ", ");
						countRobot++;
						continue;
					}
					if (!each.isPrivateShop()) {
						playList.append(each.getName() + ", ");
						countPlayer++;
						continue;
					}
					if (each.isPrivateShop()) {
						shopList.append(each.getName() + ", ");
						countShop++;
					}
				}
				if (gmList.length() > 0) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "-- 管理員 (" + countGM + "人)")));
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), gmList.toString())));
				}

				if (playList.length() > 0) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "-- 玩家 (" + countPlayer + "人)")));
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), playList.toString())));
				}
				if (robotList.length() > 0) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "-- 機器人使用者 (" + countRobot + "人)")));
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), robotList.toString())));
				}
				if (shopList.length() > 0) {
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "-- 個人商店 (" + countShop + "人)")));
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), shopList.toString())));
				}
			}
			players = null;
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), ".請輸入 [全部]")));
		}
	}
}

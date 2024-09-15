package l1j.server.server.Controller;

import l1j.server.Config;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NewCreateItem;

public class TamController implements Runnable {

	public static final int SLEEP_TIME = Config.ServerRates.TamTime * 60000;

	private static TamController _instance;

	public static TamController getInstance() {
		if (_instance == null) {
			_instance = new TamController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			PremiumTime();
		} catch (Exception e1) {
		}
	}

	private void PremiumTime() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			int premium1 = Config.ServerRates.TamNum1;
			int premium2 = Config.ServerRates.TamNum2;
			/** 檢查探索點數分配部分，與正式服務器同步 **/
			if (!pc.isPrivateShop() && !pc.noPlayerCK && !pc.noPlayerck2 && pc != null && !pc.isDead()) {
				int tamcount = pc.tamcount();
				if (tamcount > 0) {
					int addtam = Config.ServerRates.TamNum * tamcount;
					pc.getNetConnection().getAccount().addTamPoint(addtam);
					try {
						pc.getNetConnection().getAccount().updateTam();
					} catch (Exception e) {
						// 處理更新探索點數時的異常
					}
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					pc.sendPackets("\f2(成長之環:一般) " + tamcount + "階段 探索點數:" + addtam + "點 獲得");
					if (clan != null) {
						if (clan.getClanId() != 0) {
							pc.getAccount().addTamPoint(premium1);
							pc.sendPackets("\f2(成長之環:血盟) " + tamcount + "階段 探索點數:" + premium1 + "點 獲得");
						} else if (clan.getCastleId() != 0) {
							pc.getAccount().addTamPoint(premium2);
							pc.sendPackets("\f2(成長之環:城堡) " + tamcount + "階段 探索點數:" + premium2 + "點 獲得");
						}
					}
					try {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, pc.getNetConnection()), true);
					} catch (Exception e) {
						// 處理發送新的創建物品封包時的異常
					}
				}
			}
		}
	}
}
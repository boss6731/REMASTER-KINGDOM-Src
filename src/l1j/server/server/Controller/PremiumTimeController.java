package l1j.server.server.Controller;

import static l1j.server.server.model.skill.L1SkillId.ANTA_BUFF;
import static l1j.server.server.model.skill.L1SkillId.FAFU_BUFF;
import static l1j.server.server.model.skill.L1SkillId.RIND_BUFF;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;

public class PremiumTimeController implements Runnable {

	public static final int SLEEP_TIME = Config.ServerRates.FeatherTime * 60 * 1000;
	private static final SimpleDateFormat _pFormat = new SimpleDateFormat("yyyyMMdd");
	private static PremiumTimeController _instance;

	public static PremiumTimeController getInstance() {
		if (_instance == null) {
			_instance = new PremiumTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			GeneralThreadPool.getInstance().execute(new checkPremiumTime());
			GeneralThreadPool.getInstance().execute(new checkDragonBlood());
			GeneralThreadPool.getInstance().execute(new DollCleanup());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isPcCk(L1PcInstance pc) {
//		if (pc == null || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.isPrivateShop() || pc.noPlayerCK || pc.noPlayerck2 || !pc.hasSkillEffect(L1SkillId.PC_CAFE))
		if (pc == null || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.isPrivateShop() || pc.noPlayerCK || pc.noPlayerck2 || !pc.isPcBuff())
			return true;
		return false;
	}

	private class checkPremiumTime implements Runnable {// 定期發放羽毛
		@Override
		public void run() {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (isPcCk(pc))
					continue;
				if (!pc.isPrivateShop() && !pc.noPlayerCK && !pc.noPlayerck2 && pc != null && !pc.isDead()) {

					int FN = Config.ServerRates.FeatherNum;
					int CLN = Config.ServerRates.FeatherNum1;
					int CAN = Config.ServerRates.FeatherNum2;
					int realPremiumNumber = 1; // 基本要發放的物品數量
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

					// String savedir = "c:\\uami\\"+new
					// SimpleDateFormat("yyyyMMdd").format(new
					// Date())+"\\"+pc.getName();
					String savedir = String.format("c:\\uami\\%s\\%s", _pFormat.format(new Date()), pc.getName());
					File dir = new File(savedir);

					/** 向所有玩家發送禮物 **/
					if (dir.exists()) { // 當開啟廣告時
						realPremiumNumber = realPremiumNumber * 1; // 當開啟廣告時發送的數量
					}
					if (pc.getClanid() == 0) { // 無血盟
						pc.getInventory().storeItem(41921, FN, true);
						// pc.getAccount().addFeatherCount(FN);
						// pc.getAccount().addTotalFeatherCount(FN);
						pc.sendPackets("\\aC(龍的祝福:PC) 精靈的金色羽毛: (" + FN + ")個獲得");
					}
					if (clan != null) {
						if (clan.getCastleId() == 0 && pc.getClanid() != 0) { // 血盟
							pc.getInventory().storeItem(41921, (CLN + FN), true);
							// pc.getAccount().addFeatherCount(CLN + FN);
							// pc.getAccount().addTotalFeatherCount(CLN + FN);
							pc.sendPackets("\\aC(龍的祝福:PC) 精靈的金色羽毛: (" + FN + ")個，額外獲得:(" + CLN + ")個");
						}
						if (clan.getCastleId() != 0) { // 擁有城堡的血盟
							pc.getInventory().storeItem(41921, (CAN + FN), true);
							// pc.getAccount().addFeatherCount(CAN + FN);
							// pc.getAccount().addTotalFeatherCount(CAN + FN);
							pc.sendPackets("\\aC(龍的祝福:PC) 精靈的金色羽毛: (" + FN + ")個，額外獲得:(" + CAN + ")個");
						}
					}

	private class DollCleanup implements Runnable {
		@Override
		public void run() {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (isPcCk(pc))
					continue;

				L1DollInstance doll = pc.getMagicDoll();
				if (doll != null) {
					/** 已調用delete並被銷毀的狀態 **/
					if (doll._destroyed) {
						doll.deleteDoll();
						continue;
					}
					/** 沒有主人的情況下（調用deleteDoll()後） **/
					if (doll.getMaster() == null) {
						doll.deleteDoll();
						continue;
					}
				}
			}
		}
	}

	private class checkDragonBlood implements Runnable {
		@Override
		public void run() {
			int time = 0;
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (isPcCk(pc))
					continue;
				if (pc.hasSkillEffect(ANTA_BUFF)) {
					time = pc.getSkillEffectTimeSec(ANTA_BUFF) / 60;
					pc.sendPackets(String.valueOf(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, time)));
				}
				if (pc.hasSkillEffect(FAFU_BUFF)) {
					time = pc.getSkillEffectTimeSec(FAFU_BUFF) / 60;
					pc.sendPackets(String.valueOf(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, time)));
				}
				if (pc.hasSkillEffect(RIND_BUFF)) {
					time = pc.getSkillEffectTimeSec(RIND_BUFF) / 60;
					pc.sendPackets(String.valueOf(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, time)));
				}
			}
		}
	}
}
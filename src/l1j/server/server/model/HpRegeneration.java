package l1j.server.server.model;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.CalcStat;

public class HpRegeneration extends RepeatTask {

	private static Logger _log = Logger.getLogger(HpRegeneration.class.getName());

	private final L1PcInstance _pc;

	private int _regenMax = 0;

	private int _regenPoint = 0;

	private int _curPoint = 4;

	private static Random _random = new Random(System.nanoTime());

	public HpRegeneration(L1PcInstance pc, long interval) {
		super(interval);
		_pc = pc;
		updateLevel();
	}

	public void setState(int state) {
		if (_curPoint < state) {
			return;
		}
		_curPoint = state;
	}

	@Override
	public void execute() {
		try {
			_regenPoint += _curPoint;
			_curPoint = 4;

			synchronized (this) {
				if (_regenMax <= _regenPoint) {
					_regenPoint = 0;
					regenHp();
				}
			}
			DanteasBuff(_pc);
			clanbuff(_pc);
			GotobokBuff(_pc);
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void updateLevel() {
		final int lvlTable[] = new int[] { 30, 25, 20, 16, 14, 12, 11, 10, 9, 3, 2 };

		int regenLvl = Math.min(10, _pc.getLevel());
		if (30 <= _pc.getLevel() && _pc.isKnight()) {
			regenLvl = 11;
		}

		synchronized (this) {
			_regenMax = lvlTable[regenLvl - 1] * 4;
		}
	}

	public void regenHp() {
		try {

			if (_pc.isDead()) {
				return;
			}
			if (_pc.getCurrentHp() == _pc.getMaxHp() && !isUnderwater(_pc)) {
				return;
			}

			int maxBonus = 1;

			// CON 보너스
			if (11 < _pc.getLevel() && 14 <= _pc.getAbility().getTotalCon()) {
				maxBonus = _pc.getAbility().getTotalCon() - 12;
				if (25 < _pc.getAbility().getTotalCon()) {
					maxBonus = 14;
				}
			}
			// 베이스 CON 보너스
			int basebonus = CalcStat.calcHpr(_pc.getAbility().getBaseCon());

			int equipHpr = _pc.getInventory().hpRegenPerTick();
			equipHpr += _pc.getHpr();
			int bonus = _random.nextInt(maxBonus) + 1;

			if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId())) {
				bonus += 5;
			}
			if (_pc.hasSkillEffect(L1SkillId.COOKING_1_12_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_12_S)) {
				bonus += 2;
			}
			if (_pc.hasSkillEffect(L1SkillId.COOKING_1_19_N) // 대왕 거북 구이
					|| _pc.hasSkillEffect(L1SkillId.COOKING_1_19_S)) {
				bonus += 2;
			}
			if (_pc.hasSkillEffect(L1SkillId.STATUS_CASHSCROLL)) {
				bonus += 4;
			}
			if (isInn(_pc)) {
				bonus += 5;
			}
			if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId())) {
				bonus += 5;
			}

			boolean inLifeStream = false;
			if (isPlayerInLifeStream(_pc)) {
				inLifeStream = true;
				// 古代之地、魔族神殿的HPR+3是否消失？
				bonus += 3;
			}

			// 飢餓和負重檢查
			if (_pc.get_food() < 24 || isOverWeight(_pc)) {
				bonus = 0;
				basebonus = 0;
					// 由裝備引起的HPR增加會因飢餓度和負重而消失，但減少的情況下，不受飢餓度和負重影響，效果仍然存在
				if (equipHpr > 0) {
					equipHpr = 0;
				}
			}

			int newHp = _pc.getCurrentHp();
			newHp += bonus + equipHpr + basebonus;

			if (newHp < 1) {
				newHp = 1; // 不會因HPR減少裝備而死亡
			}
				// 水下的減少處理
				// 是否可以通過生命溪流來消除不明
			if (isUnderwater(_pc)) {
//                newHp -= 20;
				newHp -= 100; // 20221012 水下狀態debuff加強
				if (newHp < 1) {
					if (_pc.isGm()) {
						newHp = 1;
					} else {
						_pc.death(null, true); // HP變為0時會死亡。
					}
				}
			}
			// Lv50任務的古代空間1 F2F中的減少處理
			if (isLv50Quest(_pc) && !inLifeStream) {
				newHp -= 10;
				if (newHp < 1) {
					if (_pc.isGm()) {
						newHp = 1;
					} else {
						_pc.death(null, true); // HP變為0時會死亡。
					}
				}
			}
				// 魔族神殿中的減少處理
			if (_pc.getMapId() == 410 && !inLifeStream) {
				newHp -= 10;
				if (newHp < 1) {
					if (_pc.isGm()) {
						newHp = 1;
					} else {
						_pc.death(null, true); // HP變為0時會死亡。
					}
				}
			}
			if (!_pc.isDead()) {
				_pc.setCurrentHp(Math.min(newHp, _pc.getMaxHp()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void DanteasBuff(L1PcInstance pc) {
		if (pc.isDanteasBuff == false) {
			if (pc.getMapId() == 479) {
				pc.addDmgup(2);
				pc.addBowDmgup(2);
				pc.getAbility().addSp(1);
				pc.addMpr(2);
				pc.isDanteasBuff = true;
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 5219, true));
				pc.sendPackets(new S_SystemMessage("但丁的加護：近/遠距離傷害+2, SP+1, MP 回復+2 "));
			}
		} else {
			boolean DanteasOk = false;
			if (pc.getMapId() == 479) {
				DanteasOk = true;
			}
			if (DanteasOk == false) {
				pc.addDmgup(-2);
				pc.addBowDmgup(-2);
				pc.getAbility().addSp(-1);
				pc.addMpr(-2);
				pc.isDanteasBuff = false;
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 5219, false));
				pc.sendPackets(new S_SystemMessage("但丁的加護：加護消失"));
			}
		}
	}
	private void GotobokBuff(L1PcInstance pc) {
		if (pc.isGotobokBuff == false) {
			if (pc.getMapId() == 1710) {
				_pc.addMpr(20);
				_pc.addHpr(30);
				pc.isGotobokBuff = true;
//				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.HUNTER_BLESS2, 993, true));
//				_pc.sendPackets("\f2被遺忘之島候機室：HP/MP 回復速率將提高。");
			}
		} else {
			boolean GotobokOk = false;
			if (pc.getMapId() == 1710) {
				GotobokOk = true;
			}
			if (GotobokOk == false) {
				_pc.addMpr(-20);
				_pc.addHpr(-30);
				pc.isGotobokBuff = false;
//				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 5219, false));
//				pc.sendPackets("\f2離開被遺忘之島候機室，增益效果已被解除。");
			}
		}
	}

	private void clanbuff(L1PcInstance pc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (pc.getClanid() != 0 && clan.getOnlineClanMember().length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT && !pc.isClanBuff()) {
			pc.setSkillEffect(L1SkillId.CLANBUFF_YES, 0);
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, true));
			pc.setClanBuff(true);
		} else if (pc.getClanid() != 0 && clan.getOnlineClanMember().length < Config.ServerAdSetting.CLANBUFFUSERCOUNT && pc.isClanBuff()) {
			pc.killSkillEffectTimer(L1SkillId.CLANBUFF_YES);
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, false));
			pc.setClanBuff(false);
		}
	}

	private boolean isUnderwater(L1PcInstance pc) {
		// 裝備水之靴時，或處於伊娃的祝福狀態下，則視為不是在水中。
		if (pc.getInventory().checkEquipped(20207)) {
			return false;
		}
		if (pc.hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {
			return false;
		}
		if (pc.getInventory().checkEquipped(21048) && pc.getInventory().checkEquipped(21049)
				&& pc.getInventory().checkEquipped(21050)) {
			return false;
		}

		return pc.getMap().isUnderwater();
	}

	private boolean isOverWeight(L1PcInstance pc) {
		// 是否處於異國風情生命恢復狀態，附加火焰狀態
		// 裝備黃金之翼時，若未超重則視為處於上述狀態。
		if (pc.hasSkillEffect(L1SkillId.EXOTIC_VITALIZE) || pc.hasSkillEffect(L1SkillId.ADDITIONAL_FIRE)
				|| pc.hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			return false;
		}
		if (pc.getInventory().checkEquipped(20049) || pc.getInventory().checkEquipped(900057)) {
			return false;
		}
		if (isInn(pc)) {
			return false;
		}

		return (120 <= pc.getInventory().getWeight100()) ? true : false;
	}

	private boolean isLv50Quest(L1PcInstance pc) {
		int mapId = pc.getMapId();
		return (mapId == 2000 || mapId == 2001) ? true : false;
	}

	/**
	 * 檢查指定的PC是否在生命溪流的範圍內
	 *
	 * @param pc
	 *            PC
	 * @return true 如果PC在生命溪流的範圍內
	 */
	private static boolean isPlayerInLifeStream(L1PcInstance pc) {
		L1EffectInstance effect = null;
		for (L1Object object : pc.getKnownObjects()) {
			if (object instanceof L1EffectInstance == false) {
				continue;
			}
			effect = (L1EffectInstance) object;
			if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance(pc.getLocation()) < 4) {
				return true;
			}
		}
		return false;
	}

	private boolean isInn(L1PcInstance pc) {
		int mapId = pc.getMapId();
		return (mapId == 16384 || mapId == 16896 || mapId == 17408 || mapId == 17492 || mapId == 17820
				|| mapId == 17920 || mapId == 18432 || mapId == 18944 || mapId == 19456 || mapId == 19968
				|| mapId == 20480 || mapId == 20992 || mapId == 21504 || mapId == 22016 || mapId == 22528
				|| mapId == 23040 || mapId == 23552 || mapId == 24064 || mapId == 24576 || mapId == 25088) ? true
				: false;
	}
}

package l1j.server.server.Controller;

import java.util.Random;
import java.util.TimerTask;

import l1j.server.Config;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.CalcStat;

public class HpMpRegenController extends TimerTask {
	private final L1PcInstance _pc;
	private final int attackSec = 6;
	private final int moveSec = 2;
	private int oldLevel = 0;
	private int oldLoc = 0;
	private int regenTimeCount = 0;
	private int levelBaseSec = 0;

	public HpMpRegenController(L1PcInstance pc) { // 第一次啟動線程時執行一次，之後只執行 run 線程。
		_pc = pc;
		levelBaseSecRefresh(pc.getLevel());
	}

	private void levelBaseSecRefresh(int lv) {
		if (lv < 55) { // 55或以下
			levelBaseSec = 12;
		} else if (lv >= 55 && lv <= 65) { // 55到65之間
			levelBaseSec = 8;
		} else if (lv > 65) { // 65以上
			levelBaseSec = 4;
		}
		oldLevel = lv;
	}
	// 在更新再生狀態時，首先優先考慮是否正在攻擊。其次是移動，最後是靜止。
	private void regenStatusRefresh() { // 每1秒刷新一次。
		if (oldLevel != _pc.getLevel()) {
			//System.out.println("檢測到等級變化");
			levelBaseSecRefresh(_pc.getLevel()); // 如果與舊等級不同則更新
		}
		if (_pc.isPinkName()) { // 是否正在戰鬥。
			//System.out.println("檢測到戰鬥");
			regenTimeCount = levelBaseSec + attackSec;
		} else {
			int newLoc = _pc.getX() + _pc.getY();
//			System.out.println("oldLoc : " + oldLoc + " / newLoc : " + newLoc);
			if (oldLoc != newLoc) { // 若移動則變動再生時間計數。
//				System.out.println("檢測到移動 : " + newLoc);
				regenTimeCount = levelBaseSec + moveSec;
				oldLoc = newLoc;
			} else { // 若未移動而靜止不動
				regenTimeCount = levelBaseSec;
			}
		}
//				System.out.println("regenTimeCount : " + regenTimeCount + " / levelBaseSec : " + levelBaseSec);
	}
	private void hpregen() {
		if (_pc.getCurrentHp() == _pc.getMaxHp() && !isUnderwater(_pc))
			return;
		_regenHpPoint += 1;
		synchronized (this) {
			if (regenTimeCount <= _regenHpPoint) {
				_regenHpPoint = 0;
				regenHp();
			}
		}
	}

	private int _regenHpPoint = 0;

	private int _regenMpPoint = 0;

	private int _curMpPoint = 4;

	private static Random _random = new Random();

	private boolean isPcCk(L1PcInstance pc) {
		if (pc == null || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.noPlayerCK || pc.noPlayerck2) return true;
		return false;
	}
	@Override
	public void run() {
		try { // 若以後實現分支系統，可以最小化時間損失。但是，這是一個每秒執行一次的線程，因此也需要考慮負載問題。即使分支，也推薦以兩個方法成對的形式。
			regenStatusRefresh(); // 為了新型HP再生
			if (!isPcCk(_pc)) {
				hpregen();
			}
			if (!isPcCk(_pc)) {
				mpregen();
			}
			if (!isPcCk(_pc)) {
				DanteasBuff();
			}
			if (!isPcCk(_pc)) {
				clanbuff();
			}
			if (!isPcCk(_pc)) {
				GotobokBuff();
			}
		} catch (Exception e) {
			// 再生註釋
			//_pc.setHpMpRegenActive(false);
			e.printStackTrace();
		}
	}

	private void mpregen() {
		int nowMaxMp = _pc.getMaxMp();
		if (_pc.isDead()) {
			return;
		}
		if (_pc.getCurrentMp() == nowMaxMp) {
			return;
		}
		_regenMpPoint += _curMpPoint;
		_curMpPoint = 4;
		if (64 <= _regenMpPoint) {
			_regenMpPoint = 0;
			regenMp();
		}
	}

	public void regenMp() {
		int baseMpr = 1;
		int wis = _pc.getAbility().getTotalWis();
		if (wis == 15 || wis == 16) {
			baseMpr = 2;
		} else if (wis == 17) {
			baseMpr = 3;
		} else if (wis >= 18) {
			baseMpr += wis - 14;
		}
		// 基礎智力回復獎勵
		int baseStatMpr = CalcStat.calcMpr(_pc.getAbility().getBaseWis());

		if (_pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION2) == true) {
			baseMpr += 1;
		} else if (_pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION) == true) {
			baseMpr += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.MEDITATION) == true) {
			baseMpr += 5;
		}
		if (_pc.hasSkillEffect(L1SkillId.CONCENTRATION) == true) {
			baseMpr += 4;
		}
		if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId())) {
			baseMpr += 3;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_2_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_2_S)) {
			baseMpr += 3;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_20_N) // 小龍串烤
				|| _pc.hasSkillEffect(L1SkillId.COOKING_1_20_S)) {
			baseMpr += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_12_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_12_S)) {
			baseMpr += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.STATUS_CASHSCROLL2) == true) {
			baseMpr += 4;
		}
		if (isInn(_pc)) {
			baseMpr += 3;
		}
		if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId())) {
			baseMpr += 3;
		}

		int itemMpr = _pc.getInventory().mpRegenPerTick();
		itemMpr += _pc.getMpr();
		if (_pc.get_food() < 40 || isOverWeight(_pc)) {
			baseMpr = 0;
			if (itemMpr > 0) {
				itemMpr = 0;
			}
			return;
		}
		int mpr = baseMpr + itemMpr + baseStatMpr;
		int newMp = _pc.getCurrentMp() + mpr;

		_pc.setCurrentMp(newMp);
	}
	public void regenHp() {
		if (_pc.isDead()) {
			return;
		}
		if (_pc.getCurrentHp() == _pc.getMaxHp() && !isUnderwater(_pc)) {
			return;
		}
		int maxBonus = 1;

		// 體質獎勵
		if (_pc.getLevel() > 11 && _pc.getAbility().getTotalCon() >= 14) {
			maxBonus = _pc.getAbility().getTotalCon() - 12;
			if (25 < _pc.getAbility().getTotalCon()) {
				maxBonus = 14;
			}
		}
		// 基礎體質獎勵
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
		if (_pc.hasSkillEffect(L1SkillId.COOKING_1_19_N) // 大王烤龜
				|| _pc.hasSkillEffect(L1SkillId.COOKING_1_19_S)) {
			bonus += 2;
		}
		if (_pc.hasSkillEffect(L1SkillId.STATUS_CASHSCROLL)) {
			bonus += 4;
		}
		if (isInn(_pc)) {
			bonus += 5;
		}
		if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId())) { // 母親之樹效果
			bonus += 5;
		}

		boolean inLifeStream = false;
		if (isPlayerInLifeStream(_pc)) {
			inLifeStream = true;
			// 在古代空間和魔族神殿中，HPR+3消失了？
			bonus += 3;
		}

		// 飢餓與超重檢查
		if (_pc.get_food() < 40 || isOverWeight(_pc)) {
			bonus = 0;
			basebonus = 0;
			equipHpr = 0;
			// 裝備的HPR增益會因飢餓和超重而消失，但如果是減少的話，則無論飢餓或超重情況如何都會保留其效果
    		/*if (equipHpr > 0) {

    		}
    		return;*/
		}
		//System.out.println("bonus : " + bonus + " / equipHpr : " + equipHpr + " / basebonus : " + basebonus);
		int newHp = _pc.getCurrentHp();
		newHp += bonus + equipHpr + basebonus;

		if (newHp < 1) {
			newHp = 1; // 不會因為HPR減少裝備而死亡
		}
			// 處理水下的減少
			// 是否可以通過生命之水解除減少效果不明
		if (isUnderwater(_pc)) {
			newHp -= 20;
			if (newHp < 1) {
				if (_pc.isGm()) {
					newHp = 1;
				} else {
					_pc.death(null, true); // 當HP降到0時會死亡
				}
			}
		}
		// 處理Lv50任務中的古代空間1F2F的HP減少
		if (isLv50Quest(_pc) && !inLifeStream) {
			newHp -= 10;
			if (newHp < 1) {
				if (_pc.isGm()) {
					newHp = 1;
				} else {
					_pc.death(null, true); // 當HP降到0時會死亡
				}
			}
		}
		// 處理魔族神殿中的HP減少
		if (_pc.getMapId() == 410 && !inLifeStream) {
			newHp -= 10;
			if (newHp < 1) {
				if (_pc.isGm()) {
					newHp = 1;
				} else {
					_pc.death(null, true); // 當HP降到0時會死亡
				}
			}
		}
//		System.out.println("regenHp 執行1 " + newHp + " maxhp : " + _pc.getMaxHp() + " / min : "+ Math.min(newHp, _pc.getMaxHp()));

		if (!_pc.isDead()) {
			_pc.setCurrentHp(Math.min(newHp, _pc.getMaxHp()));
		}
	}

	private boolean isUnderwater(L1PcInstance pc) {
		// 裝備水之靴或處於艾娃的祝福狀態時，不視為在水下。
		if (pc.getInventory().checkEquipped(20207)) {
			return false;
		}
		if (pc.hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {
			return false;
		}
		if (pc.getInventory().checkEquipped(21048) && pc.getInventory().checkEquipped(21049) && pc.getInventory().checkEquipped(21050)) {
			return false;
		}
		return pc.getMap().isUnderwater();
	}

	private boolean isOverWeight(L1PcInstance pc) {
		// 是否處於異域活力狀態、額外火焰狀態或裝備金色之翼，不視為超重
		if (pc.hasSkillEffect(L1SkillId.EXOTIC_VITALIZE) || pc.hasSkillEffect(L1SkillId.ADDITIONAL_FIRE) || pc.hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			return false;
		}
		// 解決高階排名玩家的HP和MP恢復問題
		if (pc.is_top_ranker()) {
			return false;
		}
		if (isInn(pc)) {
			return false;
		}
		return (50 <= pc.getInventory().getWeight100()) ? true : false;
	}

	private boolean isLv50Quest(L1PcInstance pc) {
		int mapId = pc.getMapId();
		return (mapId == 2000 || mapId == 2001) ? true : false;
	}

	private void DanteasBuff() {
		if (_pc.isDanteasBuff == false) {
			if (_pc.getMapId() == 479) {
				_pc.addDmgup(2);
				_pc.addBowDmgup(2);
				_pc.getAbility().addSp(1);
				_pc.addMpr(2);
				_pc.isDanteasBuff = true;
				_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 5219, true));
				_pc.sendPackets(new S_SystemMessage("但丁斯增益效果：近距離/遠距離傷害+2，SP+1，MP恢復+2 "));
			}
		} else {
			boolean DanteasOk = false;
			if (_pc.getMapId() == 479) {
				DanteasOk = true;
			}
			if (DanteasOk == false) {
				_pc.addDmgup(-2);
				_pc.addBowDmgup(-2);
				_pc.getAbility().addSp(-1);
				_pc.addMpr(-2);
				_pc.isDanteasBuff = false;
				_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 5219, false));
				_pc.sendPackets(new S_SystemMessage("但丁斯的增益效果：增益已消失"));
			}
		}
	}

	private void GotobokBuff() {
		if (_pc.isGotobokBuff == false) {
			if (_pc.getMapId() == 1710) {
				_pc.addMpr(20);
				_pc.addHpr(30);
				_pc.isGotobokBuff = true;
//				_pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.HUNTER_BLESS2, 993, true));
//                _pc.sendPackets("\f2被遺忘之島待機室：HP/MP恢復率增加。");
			}
		} else {
			boolean GotobokOk = false;
			if (_pc.getMapId() == 1710) {
				GotobokOk = true;
			}
			if (GotobokOk == false) {
				_pc.addMpr(-20);
				_pc.addHpr(-30);
				_pc.isGotobokBuff = false;
//				_pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.HUNTER_BLESS2));
//                _pc.sendPackets("\f2離開被遺忘之島待機室，增益效果已解除。");
			}
		}
	}
	private void clanbuff() {
		L1Clan clan = L1World.getInstance().getClan(_pc.getClanid());
		if (_pc.getClanid() != 0 && clan.getOnlineClanMember().length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT && !_pc.isClanBuff()) {
			_pc.setSkillEffect(L1SkillId.CLANBUFF_YES, 0);
			//pc.sendPackets(new S_PacketBox(S_PacketBox.CLAN_BUFF_ICON, 1));
			_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, true));
			_pc.setClanBuff(true);
		} else if (_pc.getClanid() != 0 && clan.getOnlineClanMember().length < Config.ServerAdSetting.CLANBUFFUSERCOUNT && _pc.isClanBuff()) {
			_pc.killSkillEffectTimer(L1SkillId.CLANBUFF_YES);
			_pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, false));
			_pc.setClanBuff(false);
		}
	}

	/**
	 * 檢查指定的玩家角色是否在生命之泉的範圍內
	 *
	 * @param pc
	 *            玩家角色
	 * @return true 如果玩家角色在生命之泉的範圍內
	 */
	private static boolean isPlayerInLifeStream(L1PcInstance pc) {
		for (L1Object object : pc.getKnownObjects()) {
			if (object instanceof L1EffectInstance == false) {
				continue;
			}
			L1EffectInstance effect = (L1EffectInstance) object;
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

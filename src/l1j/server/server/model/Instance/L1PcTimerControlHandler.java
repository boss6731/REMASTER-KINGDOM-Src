package l1j.server.server.model.Instance;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.SC_SPELL_PASSIVE_ONOFF_ACK;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;

public class L1PcTimerControlHandler {

	private static L1PcTimerControlHandler _instance;

	public static enum TimerType {
		BLOOD_TO_SOUL, ASURA, AURA,
	}

	public static L1PcTimerControlHandler getInstance() {
		if (_instance == null) {
			_instance = new L1PcTimerControlHandler();
		}
		return _instance;
	}

	private HashMap<String, Timer> _timerList = new HashMap<String, Timer>();

	private L1PcTimerControlHandler() {

	}

	public void begin(String name, TimerType type) {
		stop(name);
		Timer t = new Timer(true);
		int BLOOD_TO_SOUL_TIME = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL).getProbabilityValue();
		int ASURA_TIME = SkillsTable.getInstance().getTemplate(L1SkillId.ASURA).getProbabilityValue();
		if (type.equals(TimerType.BLOOD_TO_SOUL)) {
			t.scheduleAtFixedRate(new BloodToSoul(name), BLOOD_TO_SOUL_TIME, BLOOD_TO_SOUL_TIME);
		} else if (type.equals(TimerType.ASURA)) {
			t.scheduleAtFixedRate(new L1Asura(name), ASURA_TIME, ASURA_TIME);
		} else if (type.equals(TimerType.AURA)) {

		}
		_timerList.put(name, t);
	}

	public void stop(String name) {
		Timer t = _timerList.get(name);
		if (t != null) {
			t.cancel();
			t = null;
			_timerList.remove(name);
		}
	}

	private class BloodToSoul extends TimerTask {

		private String _name;

		public BloodToSoul(String name) {
			_name = name;
		}

		@Override
		public void run() {
			try {
				int BLOODY_SOUL_HP = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL).getHpConsume();
				int BLOODY_SOUL_MP = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL).getMpConsume();
				int BLOODY_SOUL_EFFECT = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL).getCastGfx();
				int BLOODY_SOUL_ITEM = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL).getItemConsumeId();
				int BLOODY_SOUL_ITEM_COUNT = SkillsTable.getInstance().getTemplate(L1SkillId.BLOODY_SOUL).getItemConsumeCount();
				L1PcInstance pc = L1World.getInstance().getPlayer(_name);
				if (pc == null || pc.isDead()) {
					stop(_name);
					SC_SPELL_PASSIVE_ONOFF_ACK.send(pc, MJPassiveID.BLOODY_SOUL_NEW.toInt(), false);
                    return new L1PcInstance[0];
				}
				int curHp = pc.getCurrentHp();
				int curMp = pc.getCurrentMp();

				if (curHp <= 25) {
					pc.sendPackets(279);// 無法使用魔法：HP不足
					return;
				}

				if (pc.getMaxMp() == curMp) {
					return;
				}

				if (!pc.getInventory().consumeItem(BLOODY_SOUL_ITEM, BLOODY_SOUL_ITEM_COUNT) && !pc.getInventory().consumeItem(30078, 1)) {
					pc.sendPackets(299);// 無法使用魔法：催化劑不足
					return;
				}

				pc.setCurrentHp(curHp - BLOODY_SOUL_HP);// - 減少
				pc.setCurrentMp(curMp + BLOODY_SOUL_MP);// + 增加
				pc.send_effect(BLOODY_SOUL_EFFECT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class L1Asura extends TimerTask {
		private int _limiteCount = 0;
		private String _name;

		public L1Asura(String name) {
			_name = name;
		}

		@Override
		public void run() {
			try {
				// 持續時間 : 10秒
				// 效果: 10秒內每2秒恢復10至20的MP
				// 注意 : 如果MP已滿，則不會再恢復MP
				// 持續時間雖為10秒，但可能只有5秒。（預計時間）
				int ASURA_TIME_MP_COUNT = SkillsTable.getInstance().getTemplate(L1SkillId.ASURA).getProbabilityDice();
				int ASURA_TIME_MP_HEL = SkillsTable.getInstance().getTemplate(L1SkillId.ASURA).getDamageDiceCount();
				int ASURA_EFFECT = SkillsTable.getInstance().getTemplate(L1SkillId.ASURA).getCastGfx();
				L1PcInstance pc = L1World.getInstance().getPlayer(_name);
				if (pc == null || pc.isDead() || _limiteCount >= ASURA_TIME_MP_COUNT) { // 지속 10초라면 2초마다 회복이니 2*5 = 10초 계산(버그방지용)
					stop(_name);
                    return new L1PcInstance[0];
				}

				// L1PcInstance.this.setCurrentMp(L1PcInstance.this.getCurrentMp() + (_random.nextInt(10) + 10));// 隨機恢復 10 到 20 的 MP
				pc.setCurrentMp(pc.getCurrentMp() + ASURA_TIME_MP_HEL);// 80
				pc.send_effect(ASURA_EFFECT);
				_limiteCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
            return new L1PcInstance[0];
        }
	}

}

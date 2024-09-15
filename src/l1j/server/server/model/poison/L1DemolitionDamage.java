package l1j.server.server.model.poison;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class L1DemolitionDamage extends L1Poison {

	private RepeatTask _timer;
	private final L1Character _attacker;
	private final L1Character _target;
	private final int _damageSpan;
	private final int _damage;

	private L1DemolitionDamage(L1Character attacker, L1Character cha, int damageSpan, int damage) {
		_attacker = attacker;
		_target = cha;
		_damageSpan = damageSpan;
		_damage = damage;

		doInfection();
	}

	private class NormalPoisonTimer extends RepeatTask {

		NormalPoisonTimer() {
			super(_damageSpan);
		}

		@Override
		public void execute() {
			L1PcInstance player = null;
			L1MonsterInstance mob = null;
			do {
				if (!_target.hasSkillEffect(L1SkillId.DEMOLITION)) {
					cure();
					break;
				}

				if (_target.hasSkillEffect(L1SkillId.ICE_LANCE) || _target.hasSkillEffect(L1SkillId.MOB_COCA)
						|| _target.hasSkillEffect(L1SkillId.MOB_BASILL) || _target.hasSkillEffect(L1SkillId.EARTH_BIND)) {
					cure();
					break;
				}

				if (_target instanceof L1PcInstance) {
					player = (L1PcInstance) _target;
					player.receiveDamage(_attacker, _damage);
					if (_target.hasSkillEffect(L1SkillId.DEMOLITION)) {
						player.sendPackets(new S_DoActionGFX(player.getId(), ActionCodes.ACTION_Damage));
						player.broadcastPacket(new S_DoActionGFX(player.getId(), ActionCodes.ACTION_Damage));
					}
					if (player.isDead()) {
						cure();
						break;
					}
				} else if (_target instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) _target;
					mob.receiveDamage(_attacker, _damage);
					if (mob.hasSkillEffect(L1SkillId.DEMOLITION)) {
						mob.broadcastPacket(new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Damage));
					}
					if (mob.isDead()) {
						cure();
						break;
					}
				}
			} while (false);
		}
	}

	boolean isDamageTarget(L1Character cha) {
		return (cha instanceof L1PcInstance) || (cha instanceof L1MonsterInstance);
	}

	private void doInfection() {
		if (isDamageTarget(_target)) {
			_timer = new NormalPoisonTimer();
			GeneralThreadPool.getInstance().execute(_timer);
		}
	}

	public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage) {
		new L1DemolitionDamage(attacker, cha, damageSpan, damage);
		return true;
	}

	@Override
	public int getEffectId() {
		return 1;
	}

	@Override
	public void cure() {
		if (_timer != null) {
			_timer.cancel();
		}
	}
}

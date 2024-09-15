package l1j.server.server.model.poison;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class L1FlameDamage extends L1Poison {

	private RepeatTask _timer;
	private final L1Character _attacker;
	private final L1Character _target;
	private final int _damageSpan;
	private final int _damage;

	private L1FlameDamage(L1Character attacker, L1Character cha, int damageSpan, int damage) {
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
				if (!_target.hasSkillEffect(L1SkillId.FLAME_ATTACK)) {
					cure();
					break;
				}

				if (_target.hasSkillEffect(L1SkillId.ICE_LANCE) || _target.hasSkillEffect(L1SkillId.MOB_COCA) || _target.hasSkillEffect(L1SkillId.MOB_BASILL)
						|| _target.hasSkillEffect(L1SkillId.EARTH_BIND)) {
					cure();
					break;
				}

				if (_target instanceof L1PcInstance) {
					player = (L1PcInstance) _target;
					player.receiveDamage(_attacker, _damage);
					if (_target.hasSkillEffect(L1SkillId.FLAME_ATTACK)) {
						player.send_effect(18509);
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
					if (mob.hasSkillEffect(L1SkillId.FLAME_ATTACK)) {
						mob.send_effect(18509);
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
		// TODO 플레임 중복으로 못걸리게
		if (_target.hasSkillEffect(L1SkillId.FLAME_ATTACK)) {
			return;
		}
		
		_target.setSkillEffect(L1SkillId.FLAME_ATTACK, 3000);

		if (isDamageTarget(_target)) {
			if (_target instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) _target;
				on_icons(target, L1SkillId.FLAME_ATTACK);
			}
			_timer = new NormalPoisonTimer();
			GeneralThreadPool.getInstance().execute(_timer);
		}
	}

	public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage) {
		// TODO 플레임 중복으로 못걸리게
		if (cha.hasSkillEffect(L1SkillId.FLAME_ATTACK)) {
			return false;
		}

		new L1FlameDamage(attacker, cha, damageSpan, damage);
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
		if (_target instanceof L1PcInstance) {
			L1PcInstance target = (L1PcInstance) _target;
			off_icons(target, L1SkillId.FLAME_ATTACK);
		}
		_target.killSkillEffectTimer(L1SkillId.FLAME_ATTACK);
	}

	private void on_icons(L1PcInstance pc, int skillId) {
		SC_SPELL_BUFF_NOTI noti = null;
		switch (skillId) {
		case L1SkillId.FLAME_ATTACK:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_duration(3);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(9703);
			noti.set_off_icon_id(9703);
			noti.set_tooltip_str_id(7062);
			noti.set_new_str_id(7062);
			noti.set_end_str_id(0);
			noti.set_is_good(false);
			break;
		}

		if (noti != null) {
			noti.set_spell_id(skillId - 1);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}

	private void off_icons(L1PcInstance pc, int skillId) {
		SC_SPELL_BUFF_NOTI noti = null;
		switch (skillId) {
		case L1SkillId.FLAME_ATTACK:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_duration(0);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
			break;
		}

		if (noti != null) {
			noti.set_spell_id(skillId - 1);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}
}

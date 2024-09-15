package l1j.server.server.model.poison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseAttackSkill;

public class L1Osiris2 extends L1Poison {

	private RepeatTask _timer;
	private final L1Character _attacker;
	private final L1Character _target;
	private final L1Character _targetoftarget;
	private final int _damageSpan;
	private final int _damage;
	private int _time2;
	private int _shockStunDuration;

	private L1Osiris2(L1Character attacker, L1Character target, L1Character targetoftarget, int damageSpan, int damage) { //attacker = 플레이어, cha = 타겟,
		_attacker = attacker;
		_target = target;
		_targetoftarget = targetoftarget;
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
			L1NpcInstance npc = null;
			L1Character cha = null;
			int effectNpcId = 8502108; //캐스팅 이미지
			int effectNpcId2 = 8502107; //스턴 이미지
			int targetLevel = 0;
			int diffLevel = 0;

			Random random = new Random(System.nanoTime());
			_time2 = _targetoftarget.getSkillEffectTimeSec(L1SkillId.OSIRIS_TICK);
			do {
				if ( (3000 - (_time2 * 1000)) > 0) {
//					System.out.println("실행2-1");
					if (!_targetoftarget.hasSkillEffect(L1SkillId.OSIRIS_TICK)) {
						cure();
						break;
					}
	
					if (_targetoftarget.hasSkillEffect(L1SkillId.ICE_LANCE) || _targetoftarget.hasSkillEffect(L1SkillId.MOB_COCA)
							|| _targetoftarget.hasSkillEffect(L1SkillId.MOB_BASILL) || _targetoftarget.hasSkillEffect(L1SkillId.EARTH_BIND)) {
						cure();
						break;
					}
//					System.out.println("실행2-2");
					if (_targetoftarget instanceof L1Character) {
//						System.out.println("실행2-3");
						cha = (L1Character) _targetoftarget;
						if (_targetoftarget.hasSkillEffect(L1SkillId.OSIRIS_TICK)) {
//							System.out.println("실행2-4");
							ArrayList<L1Object> list3 = new ArrayList<>();
							ArrayList<L1Object> osirislist3 = new ArrayList<>();
							list3 = L1World.getInstance().getVisibleObjects(_targetoftarget, 2);
//							System.out.println(_target);
							for (int i = 0; i < list3.size(); i++){
								if (list3.get(i) instanceof L1PcInstance){
									if (list3.get(i) != _attacker && list3.get(i) != _target && list3.get(i) != _targetoftarget){
										osirislist3.add(list3.get(i));
									}
								}
							}
							if (osirislist3.size()>=1){
//								int ran = 0;
//								double randomValue = Math.random();
//								ran = (int)(randomValue*osirislist3.size());
//								L1Object osiristarget3 = osirislist3.get(ran);
									
								Collections.shuffle(osirislist3);
								L1Object osiristarget3 = osirislist3.get(0);
//								System.out.println(osirislist3.size());
								L1Character Otarget3= (L1Character) osiristarget3;
//								System.out.println("실행2-4");
		//						System.out.println("2-1");		
//							if (osirislist3.size()>=1){
//								_target.osiris_chain_attack2(_attacker, _target, osiristarget);
//								System.out.println("실행2-final");
//								System.out.println(Otarget3);
								int tX = 0;
								int tY = 0;
								tX = Otarget3.getX();
								tY = Otarget3.getY();
								int direction = _targetoftarget.targetDirection(tX, tY);
//								cha.sendPackets(new S_SkillSound (cha,1087, 0, tX, tY));
								_targetoftarget.sendPackets(new S_UseAttackSkill(_targetoftarget, Otarget3.getId(), 21087, tX, tY, 0, false), false);
								Broadcaster.broadcastPacket(_targetoftarget, (new S_UseAttackSkill(_targetoftarget, Otarget3.getId(), 21087, tX, tY, 0, false)));


//								_targetoftarget.broadcastPacket(new S_SkillSound(_targetoftarget.getId(), 21087, direction ,tX ,tY));

								L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, 2000, Otarget3.getX(), Otarget3.getY(), Otarget3.getMapId(), Otarget3);

								if (Otarget3 instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) Otarget3;
									targetLevel = pc.getLevel();
								} else if (Otarget3 instanceof L1MonsterInstance || Otarget3 instanceof L1SummonInstance || Otarget3 instanceof L1PetInstance || Otarget3 instanceof MJCompanionInstance) {
									npc = (L1NpcInstance) Otarget3;
									targetLevel = npc.getLevel();
								}
								player = (L1PcInstance) _attacker;
								diffLevel = player.getLevel() - targetLevel;
								

								
								if (player != null) {
									if (diffLevel < Config.MagicAdSetting_Illusion.OSIRIS_LVL) {
										int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS;
										_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
									} else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL1 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL2) {
										int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS1;
										_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
									} else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL3 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL4) {
										int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS2;
										_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
									} else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL5 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL6) {
										int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS3;
										_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
									} else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL7 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL8) {
										int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS4;
										_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
									} else if (diffLevel > Config.MagicAdSetting_Illusion.OSIRIS_LVL9) {
										int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS5;
										_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
									}
								}
								L1Magic _magic = new L1Magic(player, Otarget3);
								if (_magic.calcProbabilityMagic(L1SkillId.OSIRIS)) {
									if (player.getWeapon() != null) {
										L1ItemInstance weapon = player.getWeapon();
										if (weapon.getItemId() == 7000265) {
											_shockStunDuration += 1000;
										}
									}
									L1EffectSpawn.getInstance().spawnEffect2(effectNpcId2, _shockStunDuration, Otarget3.getX(), Otarget3.getY(), Otarget3.getMapId(), Otarget3);
									
									if (Otarget3 instanceof L1PcInstance) {
										L1PcInstance pc = (L1PcInstance) Otarget3;
										if (pc.hasSkillEffect(L1SkillId.OSIRIS))
											pc.removeSkillEffect(L1SkillId.OSIRIS);
										pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, true));
										pc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
										//pc.setSkillEffect(_skillId, _shockStunDuration);
									} else if (Otarget3 instanceof L1MonsterInstance || Otarget3 instanceof L1SummonInstance || Otarget3 instanceof L1PetInstance || Otarget3 instanceof MJCompanionInstance) {
										npc = (L1NpcInstance) Otarget3;
										npc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
										npc.setParalyzed(true);
										npc.setParalysisTime(_shockStunDuration);
									}
								}
							}
	
							cure();
						}
						if (_targetoftarget.isDead()) {
							cure();
							break;
						}
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

	public static boolean doInfection(L1Character attacker, L1Character cha, L1Character target, int damageSpan, int damage) {
		new L1Osiris2(attacker, cha, target, damageSpan, damage);
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

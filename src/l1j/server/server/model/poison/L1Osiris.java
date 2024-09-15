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
import l1j.server.server.model.L1Location;
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
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.serverpackets.S_UseAttackSkill;

public class L1Osiris extends L1Poison {

	private RepeatTask _timer;
	private final L1Character _attacker;
	private final L1Character _target;
	private final int _damageSpan;
	private final int _damage;
	private int _time;
	private int _shockStunDuration;

	private L1Osiris(L1Character attacker, L1Character cha, int damageSpan, int damage) { //attacker = 플레이어, cha = 타겟,
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
			L1NpcInstance npc = null;
			L1Character cha = null;
			int effectNpcId = 8502108; //캐스팅 이미지
			int effectNpcId2 = 8502107; //스턴 이미지
			int targetLevel = 0;
			int diffLevel = 0;

			Random random = new Random(System.nanoTime());
			_time = _target.getSkillEffectTimeSec(L1SkillId.OSIRIS_TICK);
			do {
//				System.out.println(_time * 1000);
				if ( (3000 - (_time * 1000)) > 0) {
					if (!_target.hasSkillEffect(L1SkillId.OSIRIS_TICK)) {
						cure();
						break;
					}
	
					if (_target.hasSkillEffect(L1SkillId.ICE_LANCE) || _target.hasSkillEffect(L1SkillId.MOB_COCA)
							|| _target.hasSkillEffect(L1SkillId.MOB_BASILL) || _target.hasSkillEffect(L1SkillId.EARTH_BIND)) {
						cure();
						break;
					}
					if (_target instanceof L1Character) {
						if (_target.hasSkillEffect(L1SkillId.OSIRIS_TICK)) {
							ArrayList<L1Object> list1 = new ArrayList<>();
							ArrayList<L1Object> osirislist1 = new ArrayList<>();
							list1 = L1World.getInstance().getVisibleObjects(_target, 2);
							for (int i = 0; i < list1.size();i++){
								if (list1.get(i) instanceof L1PcInstance){
									if (list1.get(i) != _attacker){
										osirislist1.add(list1.get(i));
									}
								}
							}
							if (osirislist1.size()>=1){
//								int ran = 0;
//								double randomValue = Math.floor(Math.random());
//								ran = (int)(randomValue*osirislist1.size());
	//							L1Object osiristarget = osirislist1.get(ran);
								Collections.shuffle(osirislist1);
								L1Object osiristarget = osirislist1.get(0);
								L1Character Otarget= (L1Character) osiristarget;
//								L1Effect effect = (L1Object) effectNpcId;
								
								
	//						System.out.println("2-1");		
//							if (osirislist1.size()>=1){
//								System.out.println("실행");
//								System.out.println(Otarget);
								int tX = 0;
								int tY = 0;
								tX = Otarget.getX();
								tY = Otarget.getY();
								int direction = _target.targetDirection(tX, tY);
//								_target.hea

//								cha.sendPackets(new S_SkillSound (cha,1087, 0, tX, tY));
//								int heading = _target.getHeading();
//								_target.setHeading(direction);
//								System.out.println("방향: "+_target.getHeading());
							
								
//								_target.sendPackets(new S_UseArrowSkill(_target, Otarget.getId(), 21087, tX, tY, false));
//								Broadcaster.broadcastPacket(_target,new S_UseArrowSkill(_target, Otarget.getId(), 21087, tX, tY, false));
								
//								public S_UseArrowSkill(int attacker_id, int ox, int oy, int oh, int act_id, int target_id, int spellgfx, int tx, int ty, boolean is_hit){
//								_target.broadcastPacket(new S_SkillSound(_target.getId(), 21087));
//								_target.setHeading(heading);
//								System.out.println("방향: "+_target.getHeading());
//								S_SkillSound skill = new S_SkillSound(_target.getId(), 21087);
//								Broadcaster.broadcastPacket(_target, skill, false);
//								skill.clear();

								_target.sendPackets(new S_UseAttackSkill(_target, Otarget.getId(), 21549, tX, tY, 0, false), false);
								Broadcaster.broadcastPacket(_target, (new S_UseAttackSkill(_target, Otarget.getId(), 21087, tX, tY, 0, false)));
								
								//cha.send_effect(1087);
								L1EffectSpawn.getInstance().spawnEffect(effectNpcId, 2000, Otarget.getX(), Otarget.getY(), _target.getMapId());
//								L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, 2000, Otarget.getX(), Otarget.getY(), Otarget.getMapId(), Otarget);
								if (Otarget instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) Otarget;
									targetLevel = pc.getLevel();
								} else if (Otarget instanceof L1MonsterInstance || Otarget instanceof L1SummonInstance || Otarget instanceof L1PetInstance || Otarget instanceof MJCompanionInstance) {
									npc = (L1NpcInstance) Otarget;
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
								L1Magic _magic = new L1Magic(player, Otarget);
								if (_magic.calcProbabilityMagic(L1SkillId.OSIRIS)) {
									if (player.getWeapon() != null) {
										L1ItemInstance weapon = player.getWeapon();
										if (weapon.getItemId() == 7000265) {
											_shockStunDuration += 1000;
										}
									}
									L1EffectSpawn.getInstance().spawnEffect2(effectNpcId2, L1SkillId.OSIRIS, Otarget.getX(), Otarget.getY(), Otarget.getMapId(), Otarget);
									
									if (Otarget instanceof L1PcInstance) {
										L1PcInstance pc = (L1PcInstance) Otarget;
										if (pc.hasSkillEffect(L1SkillId.OSIRIS)){
											pc.removeSkillEffect(L1SkillId.OSIRIS);
										}
										L1SkillUse.on_icons(pc, L1SkillId.OSIRIS, _shockStunDuration / 1000);
										pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_OSIRIS, true));
//										pc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
										pc.setSkillEffect(L1SkillId.OSIRIS, _shockStunDuration);
										//pc.setSkillEffect(_skillId, _shockStunDuration);
									} else if (Otarget instanceof L1MonsterInstance || Otarget instanceof L1SummonInstance || Otarget instanceof L1PetInstance || Otarget instanceof MJCompanionInstance) {
										npc = (L1NpcInstance) Otarget;
										npc.setSkillEffect(L1SkillId.OSIRIS, _shockStunDuration);
										npc.setParalyzed(true);
										npc.setParalysisTime(_shockStunDuration);
									}
								}
								


									Otarget.setSkillEffect(L1SkillId.OSIRIS_TICK, 3000);
									L1Osiris2.doInfection(player, _target, Otarget, 300, 0);
//	
								
								
	//							Otarget.osiris_chain_attack(_attacker, _target, Otarget);
							}
	
							
	//						_target.osiris_chain_attack(_attacker, _target);
							
	//						player.sendPackets(new S_DoActionGFX(player.getId(), ActionCodes.ACTION_Damage));
	//						player.broadcastPacket(new S_DoActionGFX(player.getId(), ActionCodes.ACTION_Damage));
							cure();
						}
						if (_target.isDead()) {
							cure();
							break;
						}
					} /*else if (_target instanceof L1NpcInstance) {
						npc = (L1NpcInstance) _target;
						int tX = 0;
						int tY = 0;
						int tMapid = 0;
						loc = npc.getLocation();
						if (npc.hasSkillEffect(L1SkillId.OSIRIS)) {
							
							ArrayList<L1Object> Npclist1 = new ArrayList<>();
							ArrayList<L1Object> osirisNpclist1 = new ArrayList<>();
							Npclist1 = L1World.getInstance().getVisibleObjects(npc, 2);
							for (int i = 0; i < Npclist1.size();i++){
								if (Npclist1.get(i) instanceof L1MonsterInstance || Npclist1.get(i) instanceof L1SummonInstance || Npclist1.get(i) instanceof L1PetInstance){
									if (Npclist1.get(i) == _attacker){
	//									System.out.println("플레이어제외-1");
									} else if (Npclist1.get(i) != _attacker){
										osirisNpclist1.add(Npclist1.get(i));
									}
								}
							}
	
							int ran = 0;
							double randomValue = Math.random();
							ran = (int)(randomValue*osirisNpclist1.size());
							L1Object osiristarget = osirisNpclist1.get(ran);
	//						if (osiristarget instanceof L1MonsterInstance || osiristarget instanceof L1SummonInstance || osiristarget instanceof L1PetInstance){
	//						System.out.println("2-1");		
								L1Character osiris = (L1Character) osiristarget;
								if (osirisNpclist1.size()>=1){
									L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, 2000, osiristarget.getX(), osiristarget.getY(), osiristarget.getMapId(), osiris);
	//								_target.osiris_chain_attack(_attacker, _target, osiris);
								}
							}
	//						_target.osiris_chain_attack(_attacker, _target);					
	//						mob.broadcastPacket(new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Damage));
						
						if (npc.isDead()) {
							cure();
							break;
						}
					}*/
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
		new L1Osiris(attacker, cha, damageSpan, damage);
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

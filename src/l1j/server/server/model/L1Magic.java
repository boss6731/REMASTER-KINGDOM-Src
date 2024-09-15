package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.SpellProp.MJSpellProbabilityLoader;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.datatables.CharacterBalance;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.datatables.UserProtectMonsterTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.item.function.L1MagicDoll;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.templates.L1SpecialMap;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJCommons;

public class L1Magic {

	private int _calcType;

	private final int PC_PC = 1;

	private final int PC_NPC = 2;

	private final int NPC_PC = 3;

	private final int NPC_NPC = 4;

	private L1PcInstance _pc = null;

	private L1PcInstance _targetPc = null;

	private L1NpcInstance _npc = null;

	private L1NpcInstance _targetNpc = null;

	private L1Character _target = null;

	private int _leverage = 10;

	private static Random _random = new Random(System.nanoTime());

	public boolean _CriticalDamage = false;

	public boolean isCriticalDamage() {
		return _CriticalDamage;
	}

	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	private double _simsimLeverage = 1;

	public void setSimSimLeverge(double i) {
		_simsimLeverage = i;
	}

	private double getSimSimLeverage() {
		return _simsimLeverage;
	}

	public L1Magic(L1Character attacker, L1Character target) {
		if (attacker instanceof L1PcInstance) {
			if (target instanceof L1PcInstance) {
				_calcType = PC_PC;
				_pc = (L1PcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = PC_NPC;
				_pc = (L1PcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		} else {
			if (target instanceof L1PcInstance) {
				_calcType = NPC_PC;
				_npc = (L1NpcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = NPC_NPC;
				_npc = (L1NpcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		}
		_target = target;
	}

	private int getSpellPower() {
		int spellPower = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			spellPower = _pc.getAbility().getSp();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			spellPower = _npc.getAbility().getSp();
		}
		return spellPower;
	}

	private int getMagicLevel() {
		int magicLevel = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicLevel = _pc.getAbility().getMagicLevel();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicLevel = _npc.getAbility().getMagicLevel();
		}
		return magicLevel;
	}

	private int getMagicBonus() {
		int magicBonus = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicBonus = _pc.getAbility().getMagicBonus();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicBonus = _npc.getAbility().getMagicBonus();
		}
		return magicBonus;
	}

	private int getLawful() {
		int lawful = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			lawful = _pc.getLawful();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			lawful = _npc.getLawful();
		}
		return lawful;
	}

	private int getTargetMr() {
		int mr = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			mr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			if (_targetNpc.getResistance() == null) {
				mr = 0;
			} else {
				mr = _targetNpc.getResistance().getEffectedMrBySkill();
			}
		}
		return mr;
	}

	/* ■■■■■■■■■■■■■■ 成功判定[命中] ■■■■■■■■■■■■■ */
	// ●●●● 概率性魔法的成功判定 ●●●●
	// 計算方法
	// 攻擊方點數：LV + ((MagicBonus * 3) * 魔法固有係數)
	// 防禦方點數：((LV / 2) + (MR * 3)) / 2
	// 攻擊成功率：攻擊方點數 - 防禦方點數
	public boolean calcProbabilityMagic(int skillId) {
		double probability = 0;
		boolean isSuccess = false;
		/*
		 * if (_pc != null && _pc.isGm()) { return true; }
		 */

		if ((_calcType == PC_PC || _calcType == NPC_PC) && _targetPc != null) {
			// TODO ME (根據概率魔法回避進行例外處理的魔法)
			L1Skills _skill = SkillsTable.getInstance().getTemplate(skillId);
			if (_skill != null) {
				if (!_skill.isIgnoresCounterMagic()) {
					if (_targetPc.getMagicDodgeProbability() > 0) {
						if (MJRnd.isWinning(100, _targetPc.getMagicDodgeProbability())) {
							S_OnlyEffect eff = new S_OnlyEffect(_targetPc.getId(), 10702);
							_targetPc.sendPackets("(ME) 概率魔法回避力抵抗發動了反魔法。");
							_targetPc.sendPackets(eff, false);
							_targetPc.broadcastPacket(eff);
							return false;
						}
					}
				}
			}
		}
//        System.out.println("確認1");
		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId == 8500138)
				return false;
			if (npcId >= 45912 && npcId <= 45915 && !_pc.hasSkillEffect(STATUS_HOLY_WATER)) {
				return false;
			}
			if (npcId == 45916 && !_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
				return false;
			}
			if (npcId == 45941 && !_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
				return false;
			}
			if (npcId >= 46068 && npcId <= 46091 && _pc.getCurrentSpriteId() == 6035) {
				return false;
			}
			if (npcId >= 46092 && npcId <= 46106 && _pc.getCurrentSpriteId() == 6034) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7720) {
				return false;
			}
		}
//		System.out.println("確認2");
		if (_calcType == NPC_NPC) {
			if (_targetNpc.getNpcTemplate().get_npcId() == 8500138)
				return false;
		}

		if (!checkZone(skillId)) {
			return false;
		}
//		System.out.println("確認3");
		if (skillId == CANCELLATION) {
			if (_calcType == PC_PC && _pc != null && _targetPc != null) {

				if (_pc.getId() == _targetPc.getId()) {
					return true;
				}
				if (_pc.getClanid() > 0 && (_pc.getClanid() == _targetPc.getClanid())) {
					_targetPc.sendPackets(new S_SystemMessage("血盟成員 " + _pc.getName() + " 使用了取消魔法。"));
					return true;
				}
				if (_pc.isInParty()) {
					if (_pc.getParty().isMember(_targetPc)) {
						_targetPc.sendPackets(new S_SystemMessage("隊員 " + _pc.getName() + " 使用了取消魔法。"));
						return true;
					}
				}
					// 當目標處於隱身狀態時，取消魔法無效
				if (_targetPc.isInvisble()) {
					return false;
				}

				if (_pc.getZoneType() == 1 || _targetPc.getZoneType() == 1) {
					return false;
				}
			}
			if (_calcType == PC_NPC || _calcType == NPC_PC || _calcType == NPC_NPC) {
				return true;
			}
		}



		// 50級以上的NPC不受以下魔法影響
		/*
		* if (_calcType == PC_NPC && _targetNpc.getLevel() >= 50 && _targetNpc.getNpcTemplate().isCantResurrect()) { if ( skillId == WEAPON_BREAK || skillId == SLOW || skillId == CURSE_PARALYZE || skillId == MANA_DRAIN || skillId == WEAKNESS || skillId ==
		* DISEASE || skillId == DECAY_POTION || skillId == GREATER_SLOW || skillId == QUAKE || skillId == ERASE_MAGIC || skillId == AREA_OF_SILENCE || skillId == WIND_SHACKLE || skillId == STRIKER_GALE || skillId == SHOCK_STUN || skillId == FOG_OF_SLEEPING ||

		* skillId == ICE_LANCE || skillId == POLLUTE_WATER || skillId == ELEMENTAL_FALL_DOWN || skillId == RETURN_TO_NATURE || skillId == THUNDER_GRAB || skillId == ARMOR_BRAKE || skillId == DARKNESS ) { return false; } }
		*/

		/*** 新手等級保護 ***/
		// if (_calcType == PC_PC) {
		// if (_targetPc.getLevel() < Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION ||
		// _pc.getLevel()
		// < Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION && !_pc.isPinkName()) {
		// if (skillId != EXTRA_HEAL && skillId != HEAL && skillId !=
		// GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL
		// && skillId != NATURES_BLESSING) { // Buff類
		// _pc.sendPackets(new S_SystemMessage("新手保護中，對方的魔法無效。"));
		// _targetPc.sendPackets(new S_SystemMessage("新手保護中，對方的魔法無效。"));
		// return false;
		// }
		// }
		// }
		//        System.out.println("確認4");
		/** 新血盟攻擊保護 **/
		if (_calcType == PC_PC) {
			if (Config.ServerAdSetting.CASTLEWAR) {
				// TODO 特定座標禁止攻擊
				int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
				if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
					MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
					L1Clan defense = war.getDefenseClan();
					boolean Range = false;
					// 下面的座標忽略（保護塔的守城血盟忽略所有條件 <- 攻城血盟只能攻擊守城血盟）
					if (_pc.getClan() != defense) {
						for (L1Object l1object : L1World.getInstance().getObject()) {
							if (l1object instanceof L1TowerInstance) {
								L1TowerInstance tower = (L1TowerInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id, tower.getLocation())) {
									Range = (_pc.getLocation().getTileLineDistance(new Point(l1object.getX(), l1object.getY())) > 10);
									if (Range && _targetPc.getClan() != defense && _targetPc.getClan() != _pc.getClan() && _targetPc.getRedKnightClanId() == 0) {
										_pc.sendPackets("\fY攻城血盟之間只能在守護塔周圍進行PK。");
										_targetPc.sendPackets("\fY攻城血盟之間只能在守護塔周圍進行PK。");
										return false;
									}
								}
							}
						}
					}
				}
			}
//			System.out.println("확인5");
			if (_pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION || _targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION && !_pc.isPinkName()) {
				if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL && skillId != NATURES_BLESSING) { // 버프계

					boolean attack_ok = false;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(_targetPc)) {
						if (!(obj instanceof L1MonsterInstance)) {
							continue;
						}

						if (obj instanceof L1MonsterInstance) {
							L1MonsterInstance mon = (L1MonsterInstance) obj;
							int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId());
							if (monid != 0) {
								attack_ok = true;
								break;
							}
						}
					}

					if (!attack_ok) {
						_pc.sendPackets(new S_SystemMessage("新手玩家受到了對方魔法的保護"));
						_targetPc.sendPackets(new S_SystemMessage("新手玩家受到了對方魔法的保護"));
						return false;
					}
				}
			}
		}

		if (_calcType == PC_NPC && (_targetNpc.getNpcId() == 5042)) {
			if (skillId == TAMING_MONSTER)
				return false;
		}
//        System.out.println("確認6");
		// TODO 在地縛術狀態下 -> 這些技能無效化
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (_targetPc.hasSkillEffect(EARTH_BIND)) {
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION // 概率技能
						&& skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL && skillId != NATURES_BLESSING && skillId == MANA_DRAIN
						|| skillId == CURSE_PARALYZE || skillId == THUNDER_GRAB || skillId == ERASE_MAGIC || skillId == EMPIRE || skillId == SHOCK_STUN || skillId == EARTH_BIND
						|| skillId == BONE_BREAK || skillId == ETERNITI || skillId == FORCE_STUN) { // Buff類
					return false;
				}
			}
		} else {
			if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION) {
					return false;
				}
			}
		}
//		System.out.println("확인7");
		/**
		 * 스턴중일때 동일류 스턴 안걸리게
		 */
		if (_target.hasSkillEffect(STUN_TYPE_SKILL)) {
			for (int stun_skill : STUN_TYPE_SKILL) {
				if (stun_skill == skillId)
					return false;
			}
		}
//		System.out.println("확인3");
		if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			if ((skillId == SILENCE || skillId == AREA_OF_SILENCE) && (_targetNpc.getNpcId() == 45684 || _targetNpc.getNpcId() == 45683 || _targetNpc.getNpcId() == 45681
					|| _targetNpc.getNpcId() == 45682 || _targetNpc.getNpcId() == 900011 || _targetNpc.getNpcId() == 900012 || _targetNpc.getNpcId() == 900013 || _targetNpc.getNpcId() == 900038
					|| _targetNpc.getNpcId() == 900039 || _targetNpc.getNpcId() == 900040 || _targetNpc.getNpcId() == 5096 || _targetNpc.getNpcId() == 5097 || _targetNpc.getNpcId() == 5098
					|| _targetNpc.getNpcId() == 5099 || _targetNpc.getNpcId() == 5100)) {
				return false;
			}
		}

		// 100% 확률을 가지는 스킬
		if (skillId == MIND_BREAK || skillId == IllUSION_AVATAR) {
			return true;
		}

		probability = calcProbability(skillId);
		int rnd = CommonUtil.random(100);
//		System.out.println("확인8");
		
		if (skillId == ETERNITI){
			if (_calcType == PC_NPC) {
//				System.out.println(_pc.getLocation().getTileLineDistance(_targetNpc.getLocation()));
				if (_pc.getLocation().getTileLineDistance(_targetNpc.getLocation()) > 4) {
					probability /= 2;
				}
			} else if (_calcType == PC_PC) {
//				System.out.println(_pc.getLocation().getTileLineDistance(_targetPc.getLocation()));
				if (_pc.getLocation().getTileLineDistance(_targetPc.getLocation()) > 4) {
					probability /= 2;
				}
			} 
//			System.out.println("이터 확률: "+probability);
		}
		
		if (probability > 90)
			probability = 90;

		if (probability >= rnd) { // 마법실패시에도 미쓰 뜨게
			isSuccess = true;
		} else {
			if (_calcType == NPC_PC || _calcType == PC_PC) {
				isSuccess = false;
			} else if (_calcType == PC_NPC) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 13418));
				isSuccess = false;
			}
		}
		if (!isSuccess & skillId == TURN_UNDEAD) {
			if (_calcType == PC_NPC) {
				_targetNpc.setHate(_pc, 1);
				int ran = _random.nextInt(100) + 1;
				if (ran <= 50) {
					Broadcaster.broadcastPacket(_targetNpc, new S_SkillSound(_targetNpc.getId(), 8987));
					Broadcaster.broadcastPacket(_targetNpc, new S_SkillHaste(_targetNpc.getId(), 1, 0));
					_targetNpc.setMoveSpeed(1);
				}
			}
		}

		if (Config.LogStatus.GMATKMSG) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.isGm()) {
					String attacker = _pc.getName();
					String defender = null;
					int probability = (int) probability;
					int chance = rnd;
					String successOrFailure = isSuccess ? "成功" : "失敗";
					if (_calcType == PC_PC) {
						defender = _targetPc.getName();
					} else if (_calcType == PC_NPC) {
						defender = _targetNpc.getName();
					}
					_pc.sendPackets(new S_SystemMessage("\fR[" + attacker + "->" + defender + "] " + probability + "(" + chance + ") / " + successOrFailure));
				}
			} else if (_calcType == NPC_PC) {
				if (_targetPc.isGm()) {
					String attacker = _npc.getName();
					String defender = _targetPc.getName();
					L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
					String skillName = "無資料的技能";
					if (skill != null) {
						skillName = skill.getName();
					}
					int probability = (int) probability;
					int chance = rnd;
					String successOrFailure = isSuccess ? "成功" : "失敗";
					_targetPc.sendPackets(new S_SystemMessage("\fR[" + attacker + "->" + defender + "] " + probability + "(" + chance + ") / " + successOrFailure + "[" + skillName + "]"));
				}
			}
		}
		
		
		//
		// String msg0 = "";
		// String msg2 = "";
		// String msg3 = "";
		// String msg4 = "";
		//
		// if (_calcType == PC_PC || _calcType == PC_NPC) {
		// msg0 = _pc.getName();
		// } else if (_calcType == NPC_PC) {
		// msg0 = _npc.getName();
		// }
		//
		// msg2 = "확률 : 랜덤-" + rnd + " / " + probability + "%";
		// if (_calcType == NPC_PC || _calcType == PC_PC) {
		// msg4 = _targetPc.getName();
		// } else if (_calcType == PC_NPC) {
		// msg4 = _targetNpc.getName();
		// }
		// if (isSuccess == true) {
		// msg3 = "성공";
		// } else {
		// msg3 = "실패";
		// }
		//
		// if (_calcType == PC_PC || _calcType == PC_NPC) {
		// _pc.sendPackets(new S_SystemMessage("\\aG[" + msg0 + "->" + msg4 + "] " +
		// msg2 + " / " + msg3));
		// }
		// if (_calcType == NPC_PC || _calcType == PC_PC) {
		// _targetPc.sendPackets(new S_SystemMessage("\\aH[" + msg0 + "->" + msg4 + "] "
		// + msg2 + " / " + msg3));
		// }

		return isSuccess;
	}

	private boolean checkZone(int skillId) {
		if (_pc != null && _targetPc != null) {
			if (_pc.getZoneType() == 1 || _targetPc.getZoneType() == 1 || _pc.getMapId() == 13005 || _targetPc.getMapId() == 13005) {
				if (skillId == CURSE_POISON || skillId == CURSE_BLIND || skillId == WEAPON_BREAK || skillId == SLOW || skillId == CURSE_PARALYZE || skillId == MANA_DRAIN || skillId == DARKNESS
						|| skillId == WEAKNESS || skillId == DISEASE || skillId == SILENCE || skillId == FOG_OF_SLEEPING || skillId == DECAY_POTION || skillId == SHOCK_STUN || skillId == EMPIRE
						|| skillId == ERASE_MAGIC || skillId == EARTH_BIND || skillId == AREA_OF_SILENCE || skillId == WIND_SHACKLE || skillId == POLLUTE_WATER || skillId == STRIKER_GALE
						|| skillId == DESTROY || skillId == ICE_LANCE || skillId == ELEMENTAL_FALL_DOWN || skillId == RETURN_TO_NATURE || skillId == PHANTASM || skillId == CONFUSION
						|| skillId == DESPERADO || skillId == POWERRIP || skillId == ETERNITI || skillId == FORCE_STUN || skillId == TEMPEST) {
					return false;
				}
			}
		}
		return true;
	}

	private int calcProbability(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int attackLevel = 0;
		int defenseLevel = 0;
		int probability = 0;
		int attackInt = 0;
		int defenseMr = 0;
		L1Character attacker = null;
		L1Character receiver = null;
		switch (_calcType) {
		case PC_PC:
			attacker = _pc;
			receiver = _targetPc;
			break;
		case PC_NPC:
			attacker = _pc;
			receiver = _targetNpc;
			break;
		case NPC_PC:
			attacker = _npc;
			receiver = _targetPc;
			break;
		case NPC_NPC:
			attacker = _npc;
			receiver = _targetNpc;
			break;
		}

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			attackLevel = _pc.getLevel();
			attackInt = _pc.getAbility().getTotalInt();
		} else {
			attackLevel = _npc.getLevel();
			attackInt = _npc.getAbility().getTotalInt();
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			defenseLevel = _targetPc.getLevel();
			defenseMr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			if (_targetNpc != null) {
				defenseLevel = _targetNpc.getLevel();
				defenseMr = _targetNpc.getResistance() != null ? _targetNpc.getResistance().getEffectedMrBySkill() : 0;
				if (skillId == RETURN_TO_NATURE) {
					if (_targetNpc instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) _targetNpc;
						defenseLevel = summon.getMaster().getLevel();
					}
				}
			}
		}

		/*if (_calcType == PC_PC) {
			if (_targetPc.hasSkillEffect(MAFR)) {
				if (l1skills.isDebuff()) {
					if (MJRnd.isWinning(100, SkillsTable.getInstance().getTemplate(MAFR).getProbabilityValue())) {
						_targetPc.send_effect(18945);
					} else {
						_targetPc.removeSkillEffect(MAFR);
						_targetPc.send_effect(18946);
					}
					return 0;
				}
			}
		}*/

		if ((_calcType == PC_PC || _calcType == PC_NPC) && MJSpellProbabilityLoader.getInstance().contains_probability(skillId)) {
			probability = MJSpellProbabilityLoader.getInstance().calc_probability(skillId, _pc, receiver, attackInt, defenseMr);
		} else {
			/**
			 * TODO 資料庫中沒有或 NPC施放時使用下面的代碼，其他情況使用上面的代碼（表格）
			 **/
			switch (skillId) {
			case ERASE_MAGIC:
			case ELEMENTAL_FALL_DOWN: {
				int ERASEMAGICPoint = attacker.getSpecialPierce(eKind.SPIRIT) + attacker.getSpecialPierce(eKind.ALL);
				if (ERASEMAGICPoint > 0) {
					attackLevel += Math.round(((double) ERASEMAGICPoint / (double) Config.MagicAdSetting_Elf.ERASEMAGIC));
				}

				if (attackLevel >= defenseLevel)
					probability = (attackLevel - defenseLevel) * 2 + 43;
				else if (attackLevel < defenseLevel) {
					probability = (attackLevel - defenseLevel) * 3 + 43;
				}
				if (probability > 70) {
					probability = 70;
				}
			}
				break;
			case EARTH_BIND: {
				int EARTHBINDPoint = attacker.getSpecialPierce(eKind.SPIRIT) + attacker.getSpecialPierce(eKind.ALL);
				if (EARTHBINDPoint > 0) {
					attackLevel += Math.round(((double) EARTHBINDPoint / (double) Config.MagicAdSetting_Elf.EARTHBINDT));
				}

				if (attackLevel >= defenseLevel)
					// 如果施法者等級等於或高於目標等級
					probability = (attackLevel - defenseLevel) * 2 + 40;
				else if (attackLevel < defenseLevel) {
					// 如果施法者等級低於目標等級
					// 例如 84 : 85 = 84 - 85 = -1 * 3 = -3 + 30 = 27
					probability = (attackLevel - defenseLevel) * 3 + 30;
				}
				if (probability > 100) {
					probability = 100;
				}
			}
				break;
			case STRIKER_GALE: {
				int STRIKERGALEPoint = attacker.getSpecialPierce(eKind.SPIRIT) + attacker.getSpecialPierce(eKind.ALL);
				if (STRIKERGALEPoint > 0) {
					attackLevel += Math.round(((double) STRIKERGALEPoint / (double) Config.MagicAdSetting_Elf.STRIKERGALET));
				}

				if (attackLevel >= defenseLevel)
					probability = (attackLevel - defenseLevel) * 2 + 40;
				else if (attackLevel < defenseLevel) {
					probability = (attackLevel - defenseLevel) * 3 + 30;
				}
				if (probability > 100) {
					probability = 100;
				}
			}
				break;
			case POLLUTE_WATER: {
				int POLLUTEWATERPoint = attacker.getSpecialPierce(eKind.SPIRIT) + attacker.getSpecialPierce(eKind.ALL);
				if (POLLUTEWATERPoint > 0) {
					attackLevel += Math.round(((double) POLLUTEWATERPoint / (double) Config.MagicAdSetting_Elf.POLLUTEWATERT));
				}

				if (attackLevel >= defenseLevel)
					probability = (attackLevel - defenseLevel) * 2 + 40;
				else if (attackLevel < defenseLevel) {
					probability = (attackLevel - defenseLevel) * 3 + 30;
				}
				if (probability > 100) {
					probability = 100;
				}
			}
				break;
			case WIND_SHACKLE: {
				int WINDSHACKLEPoint = attacker.getSpecialPierce(eKind.SPIRIT) + attacker.getSpecialPierce(eKind.ALL);
				if (WINDSHACKLEPoint > 0) {
					attackLevel += Math.round(((double) WINDSHACKLEPoint / (double) Config.MagicAdSetting_Elf.WINDSHACKLET));
				}

				if (attackLevel >= defenseLevel)
					probability = (attackLevel - defenseLevel) * 2 + 40;
				else if (attackLevel < defenseLevel) {
					probability = (attackLevel - defenseLevel) * 3 + 30;
				}
				if (probability > 100) {
					probability = 100;
				}
			}
				break;
				// TODO 指定機率型魔法
			case SHAPE_CHANGE:
			case CANCELLATION:
			case SLOW:
			case DISEASE:
			case CURSE_PARALYZE:
			case WEAPON_BREAK:
			case DECAY_POTION:
			case CURSE_BLIND:
			case CURSE_POISON:
			case SILENCE:
			case FOG_OF_SLEEPING:
			case DEATH_HEAL:
			case DEATH_HEAL_Mob:
			case DARK_BLIND:
				if (attackInt > 25)
					attackInt = 25;
				probability = (int) (attackInt * 3 + l1skills.getProbabilityValue() - defenseMr);
				if (_pc != null && _pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
					probability -= 30;
				}
				if (probability < 1) {
					probability = 1;
				}
				if (probability > 80) {
					probability = 80;
				}
				/*
				 * _pc.sendPackets(new S_SystemMessage("[概率] -> " + defenseMr + " " + probability + "%"));
				 * System.out.println("[魔法概率] -> " + defenseMr + " " + probability + "%");
				 */
				break;
			case WEAKNESS:
			case ICE_LANCE:
			case DARKNESS:
				if (attackInt > 10)
					attackInt = 10;
				probability = (int) (attackInt * 3 + l1skills.getProbabilityValue() - defenseMr);
				if (_pc != null && _pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
					probability -= 10;
				}
				if (probability < 5) {
					probability = 5;
				}
				if (probability > 70) {
					probability = 70;
				}

				if (attackLevel < defenseLevel) {
					probability = 0;
				}

				// _pc.sendPackets(new S_SystemMessage("[概率] -> " + defenseMr + " "
				// + probability + "%"));
				// System.out.println("[魔法概率] -> " + defenseMr + " " + probability +
				// "%");
				break;
			case THUNDER_GRAB:
				int ProbabilityValue = SkillsTable.getInstance().getTemplate(L1SkillId.THUNDER_GRAB).getProbabilityValue();
				int THUNDERGRABPoint = attacker.getSpecialPierce(eKind.DRAGON_SPELL) + attacker.getSpecialPierce(eKind.ALL);
				if (THUNDERGRABPoint > 0) {
					attackLevel += Math.round(((double) THUNDERGRABPoint / (double) Config.MagicAdSetting_DragonKnight.THUNDERGRABT));
				}
				probability = ProbabilityValue;
				if (_calcType == PC_PC || _calcType == PC_NPC) {
					probability += 2;
				}
				break;
				case COUNTER_BARRIER: // 反擊屏障
					probability = l1skills.getProbabilityValue(); // 反擊屏障
					int probability_veteran = Config.MagicAdSetting_Knight.COUNTER_VETERAN; // 反擊屏障：老手

					// System.out.print(String.format("反擊屏障概率：基本(%d)", probability));

				if (_pc != null && _pc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
					int lvl = _pc.getLevel();
					if (lvl >= 85) {
						probability_veteran += (lvl - 84);
					}
					if (probability_veteran >= 10) {
						probability_veteran = 10;
					}
					probability += probability_veteran;
				}
					// System.out.println(String.format("反擊屏障最終概率：(%d)", probability));
					break;
				case DESPERADO:
					int horroPoint = attacker.getSpecialPierce(eKind.FEAR) + attacker.getSpecialPierce(eKind.ALL);
					if (horroPoint > 0) {
						attackLevel += Math.round(((double) horroPoint / (double) Config.MagicAdSetting_DragonKnight.HORRORHITTOLEVEL));
					}

					probability = (int) +Config.MagicAdSetting_Warrior.DESPERADO + ((attackLevel - defenseLevel) * 9); // 對於等級的
					probability += _pc.getImpactUp();
					if (probability < 15) { // 對於等級差，最少觸發10%
						probability = 15;
					}
					if (probability > 100) { // 假設100%概率
						probability = 100;
					}
					// System.out.println(horroPoint);
					// System.out.println("概率: " + probability);
				break;
			case MORTAL_BODY:
				probability = l1skills.getProbabilityValue();
				// probability = 25;
				break;
			case CONFUSION:
			case PHANTASM:
				probability = l1skills.getProbabilityValue();
				int CONFUSION_PHANTASMPoint = attacker.getSpecialPierce(eKind.DRAGON_SPELL) + attacker.getSpecialPierce(eKind.ALL);
				if (CONFUSION_PHANTASMPoint > 0) {
					attackLevel += Math.round(((double) CONFUSION_PHANTASMPoint / (double) Config.MagicAdSetting_Illusion.CONFUSIONPHANTASMT));
				}
				break;
			case RETURN_TO_NATURE:
				probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel)) + l1skills.getProbabilityValue();
				break;
			case BONE_BREAK:
				int boneBreakPoint = attacker.getSpecialPierce(eKind.DRAGON_SPELL) + attacker.getSpecialPierce(eKind.ALL);
				if (boneBreakPoint > 0) {
					attackLevel += Math.round(((double) boneBreakPoint / (double) Config.MagicAdSetting_Knight.STUNHITTOLEVEL));
				}
				if (_calcType == PC_PC) {
					if (_pc instanceof L1PcInstance) {
						L1ItemInstance weapon = _pc.getWeapon();
						if (weapon == null) {
							probability = 0;
							break;
						}
						int itemId = weapon.getItem().getItemId();
						if (itemId == 202012) {
							attackLevel += 5;
						}
					}
				}
				probability = (int) Config.MagicAdSetting_Illusion.BONEBREAKPRO + (attackLevel - defenseLevel);
				if (probability < 10) {
					probability = 10;
				}
				if (probability > 100) {
					probability = 100;
				}
				break;
			case EMPIRE: {
				int shockStunPoint = attacker.getSpecialPierce(eKind.ABILITY) + attacker.getSpecialPierce(eKind.ALL);
				if (shockStunPoint > 0) {
					attackLevel += Math.round(((double) shockStunPoint / (double) Config.MagicAdSetting_Prince.EMPIREHITTOLEVEL));
				}

				/** 엠파이어 외부화 **/
				probability = (int) +Config.MagicAdSetting_Prince.EMPIRE + ((attackLevel - defenseLevel) * 2);// *2배
				if (_calcType == PC_PC || _calcType == PC_NPC) {
					probability += _pc.getImpactUp();
				}
				if (probability < 15) {// 기본정의 10
					probability = 15;
				}
				if (probability > 100) {// 총 스턴확률 100 이하
					probability = 100;
				}
			}
				break;
			case MOSTER_STUN_1:
			case OMAN_STUN:
			case DRAGON_HALPAS_STUN:
			case BALOCH_STUN:
			case BOS_STUN18:
			case fornos_STUN:
			case Besi_STUN:
			case Moster_STUN:
			case Maeno_STUN:
				probability = SkillsTable.getInstance().getTemplate(skillId).getProbabilityValue();
				if (probability > 100) {// 총 스턴확률 100 이하
					probability = 100;
				}
				// System.out.println("skillid: "+skillId);
				// System.out.println("확률: "+probability);
				break;
			case FORCE_STUN:
			case SHOCK_STUN:// 쇼크스턴
			{
				int shockStunPoint = attacker.getSpecialPierce(eKind.ABILITY) + attacker.getSpecialPierce(eKind.ALL);
				if (shockStunPoint > 0) {
					attackLevel += Math.round(((double) shockStunPoint / (double) Config.MagicAdSetting_Knight.STUNHITTOLEVEL));
				}

				/** 쇼크스턴 외부화 **/
				probability = (int) +Config.MagicAdSetting_Knight.SHOCKSTUN + ((attackLevel - defenseLevel) * 2);// *2는 레벨 1당
				if (_calcType == PC_PC || _calcType == PC_NPC) {
					probability += _pc.getImpactUp();
				}
				if (probability < 15) {// 기본정의 10
					probability = 15;
				}
				if (probability > 100) {// 총 스턴확률 100 이하
					probability = 100;
				}
			}
				break;
			case MANA_DRAIN:
				/*
				 * if (attackInt > 25) attackInt = 25;
				 */

				final double MR_DRAIN_WEIGHT = 0.5;
				final double INT_DRAIN_WEIGHT = 1.3;
				// System.out.println(defenseMr + " " + (defenseMr * MR_DRAIN_WEIGHT));
				// System.out.println((attackInt * INT_DRAIN_WEIGHT) - (defenseMr *
				// MR_DRAIN_WEIGHT));
				probability = (int) ((attackInt * INT_DRAIN_WEIGHT) - (defenseMr * MR_DRAIN_WEIGHT));
				probability = Math.max(probability, 10);
				break;
			case TURN_UNDEAD:
				/*
				 * if (attackInt > 25) attackInt = 25;
				 */

				final double MR_TURN_WEIGHT = 0.5;
				final double INT_TURN_WEIGHT = 1.3;
				// System.out.println(defenseMr + " " + (defenseMr * MR_TURN_WEIGHT));
				// System.out.println((attackInt * INT_TURN_WEIGHT) - (defenseMr *
				// MR_TURN_WEIGHT));
				probability = (int) ((attackInt * INT_TURN_WEIGHT) - (defenseMr * MR_TURN_WEIGHT));
				// 인트 vs 마방, 인트(*1.5) - 마방(*0.5) = 결과
				// 20 vs 50 -> 30 - 25 = 15
				// 30 vs 50 -> 45 - 25 = 20
				// 40 vs 50 -> 60 - 25 = 35
				// 47 vs 50 -> 70.5 - 25 = 45.5
				// 70 vs 50 -> 105 - 25 = 80
				// 47 vs 84 -> 70.5 - 42 = 28.5

				if (_calcType == PC_PC || _calcType == PC_NPC) {
					if (!_pc.isElf()) {
						probability -= Config.MagicAdSetting_Elf.ELFTURNCHANT;
					}
				}
				probability = Math.max(probability, 10);
				break;
			/** 바란카 스턴레벨 상승에 따른 아머브레이크 확률 증가 **/
			case ARMOR_BRAKE:
				int dragonSpellPoint = attacker.getSpecialPierce(eKind.SPIRIT) + attacker.getSpecialPierce(eKind.ALL);
				if (dragonSpellPoint > 0) {
					attackLevel += Math.round(((double) dragonSpellPoint / (double) Config.MagicAdSetting_DragonKnight.DESTROYHITTOLEVEL));
				}

				/** 바란카 스턴레벨 상승에 따른 아머브레이크 확률 증가 **/
				probability = (int) (Config.MagicAdSetting_DarkElf.ARMORBRAKET + ((attackLevel - defenseLevel) * 3));// 3배
				probability += _pc.getImpactUp();
				if (_pc.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
					int lvl = _pc.getLevel();
					if (lvl >= 85)
						probability += ((lvl - 84) * 3);
				}

				probability = IntRange.ensure(probability, 10, 80);

				break;
			default: {
				int dice1 = l1skills.getProbabilityDice();
				int diceCount1 = 0;
				if (_calcType == PC_PC || _calcType == PC_NPC) {
					if (_pc.isWizard()) {
						diceCount1 = getMagicBonus() + getMagicLevel() + 1;
					} else if (_pc.isElf()) {
						diceCount1 = getMagicBonus() + getMagicLevel() - 1;
					} else if (_pc.isDragonknight()) {
						diceCount1 = getMagicBonus() + getMagicLevel();
					} else {
						diceCount1 = getMagicBonus() + getMagicLevel() - 1;
					}
				} else {
					diceCount1 = getMagicBonus() + getMagicLevel();
				}
				if (diceCount1 < 1) {
					diceCount1 = 1;
				}
				if (dice1 > 0) {
					for (int i = 0; i < diceCount1; i++) {
						probability += (_random.nextInt(dice1) + 1);
					}
				}

				probability = probability * getLeverage() / 10;
				probability -= getTargetMr();

				if (skillId == TAMING_MONSTER) {
					double probabilityRevision = 1;
					if ((_targetNpc.getMaxHp() * 1 / 4) > _targetNpc.getCurrentHp()) {
						probabilityRevision = 1.3;
					} else if ((_targetNpc.getMaxHp() * 2 / 4) > _targetNpc.getCurrentHp()) {
						probabilityRevision = 1.2;
					} else if ((_targetNpc.getMaxHp() * 3 / 4) > _targetNpc.getCurrentHp()) {
						probabilityRevision = 1.1;
					}
					probability *= probabilityRevision;
				}
			}
				break;
			}
		}
		if (l1skills.getPlusProbility() != 0) {
			probability += l1skills.getPlusProbility();
		}

		if (_calcType == PC_PC) {
			probability += CharacterBalance.getInstance().getMagicHit(_pc.getType(), _targetPc.getType());
		} else if (_calcType == PC_NPC) {
			probability += CharacterBalance.getInstance().getMagicHit(_pc.getType(), 10);
		} else if (_calcType == NPC_PC) {
			probability += CharacterBalance.getInstance().getMagicHit(10, _targetPc.getType());
		} else if (_calcType == NPC_NPC) {
			probability += CharacterBalance.getInstance().getMagicHit(10, 10);
		}

		// ----------------------------------------- 주석 되어있던것 해제
		// eKind kind = eKind.NONE;
		// double revision_resist = 1.0D;
		// // TODO 내성에 대한 타입
		// switch (skillId) {
		// // 공포내성
		// case DESPERADO:
		// case POWERRIP:
		// case TOMAHAWK:
		// kind = eKind.FEAR;
		// revision_resist = Config.MagicAdSetting.RESIST_FEAR;
		// break;
		//
		// // 정령내성
		// case ARMOR_BRAKE:
		// case EARTH_BIND:
		// case ERASE_MAGIC:
		// case ELEMENTAL_FALL_DOWN:
		// case STRIKER_GALE:
		// case POLLUTE_WATER:
		// case WIND_SHACKLE:
		// kind = eKind.SPIRIT;
		// revision_resist = Config.MagicAdSetting.RESIST_SPIRIT;
		// break;
		//
		// // 용언내성
		// case BONE_BREAK:
		// case THUNDER_GRAB:
		// case GUARD_BREAK:
		// case FEAR:
		// case PHANTASM:
		// kind = eKind.DRAGON_SPELL;
		// revision_resist = Config.MagicAdSetting.RESIST_DRAGON_SPELL;
		// break;
		//
		// // 기술내성
		// case EMPIRE:
		// case fornos_STUN:
		// case SHOCK_STUN:
		// case 30081:
		// kind = eKind.ABILITY;
		// revision_resist = Config.MagicAdSetting.RESIST_ABILITY;
		// break;
		// default:
		// break;
		// }
		// if (kind.toInt() != eKind.NONE.toInt()) {
		// int resistance = receiver.getSpecialResistance(kind) +
		// receiver.getSpecialResistance(eKind.ALL);
		// if (resistance > 0) {
		// probability -= (resistance * revision_resist);
		// }
		// }
		// ----------------------------------------- 주석 되어있던것 해제

		if (_pc != null) {
			probability += _pc.get_private_probability(skillId);
		}
		return calc_character_magic_hit(skillId, probability);
	}

	/**
	 * 마법적중 효과 적용 안될 스킬에 대해서는 case 문으로 추가
	 * 
	 * @param skill_id
	 * @param probability
	 * @return
	 */
	private int calc_character_magic_hit(int skill_id, int probability) {
		if (_calcType == PC_PC) {
			if (_pc.getMapId() == 13005 || _targetPc.getMapId() == 13005) {
				return 0;
			}
			if (skill_id == TEMPEST) {
				if (_pc == _targetPc) {
//					System.out.println("헐");
				}
			}
		}
		
		if (_calcType != PC_PC && _calcType != PC_NPC) {
			return probability;
		}

		double result = probability;
		if (!MJSpellProbabilityLoader.getInstance().contains_probability(skill_id)) {
			double p = Config.MagicAdSetting.CHARACTERMAGICHITRATE * _pc.getTotalMagicHitup();
			result += p;
		}
		/*
		 * switch (skill_id) {
		 *
		 * 적용안될스킬들 추가 (기술류들:전사,기사)
		 * 
		 * //TODO 기사 case SHOCK_STUN: case REDUCTION_ARMOR: case SOLID_CARRIAGE: case COUNTER_BARRIER: case BOUNCE_ATTACK: case ABSOLUTE_BLADE: case PRIDE: case BLOW_ATTACK: // TODO 전사 case HOWL: case GIGANTIC: case POWERRIP: case TOMAHAWK: case DESPERADO: case
		 * TITANL_RISING: break; default: double p = Config.CHARACTER_MAGICHIT_RATE * _pc.getTotalMagicHitup(); result += p; break; }
		 */
		return (int) result;
	}

	public int calcMagicDamage(int skillId) {
		int damage = 0;
		int DMG = SkillsTable.getInstance().getTemplate(L1SkillId.AVENGER).getDamageValue();
		if (_calcType == PC_PC) {
			if (_pc.getMapId() == 13005 || _targetPc.getMapId() == 13005) {
				return 0;
			}
		}
		if (skillId == AVENGER) {
			boolean isavenger = this.calcProbabilityMagic(skillId);
			if (_calcType == PC_PC) {
				if (isavenger) {
					int currentHp = (int) Math.round(_targetPc.getMaxHp() * (30 * 0.01));
					if (_targetPc.getCurrentHp() < currentHp) {// 성공에 성공
						damage = _targetPc.getCurrentHp();
					} else {// 성공에 실패
						damage = /* calcPcMagicDamage(skillId) */DMG + (_targetPc.getCurrentHp() / 20);// 스킬 성공시 대미지
					} // 아예 실패
					S_SkillSound skill = new S_SkillSound(_targetPc.getId(), 18404);
					_targetPc.sendPackets(skill, false);
					Broadcaster.broadcastPacket(_targetPc, skill, false);
					skill.clear();
				}
				S_SkillSound effect = new S_SkillSound(_targetPc.getId(), 18402);
				_targetPc.sendPackets(effect, false);
				Broadcaster.broadcastPacket(_targetPc, effect, false);
				effect.clear();
			} else {
				// if (isavenger) {
				damage += calcNpcMagicDamage(skillId) +(_targetNpc.getCurrentHp() / 20);
				S_SkillSound effect = new S_SkillSound(_targetNpc.getId(), 18402);
				_targetNpc.sendPackets(effect, false);
				Broadcaster.broadcastPacket(_targetNpc, effect, false);
				effect.clear();

				/*
				 * int currentHp = (int) Math.round(_targetNpc.getMaxHp() * (30 * 0.01)); if (_targetNpc.getCurrentHp() < currentHp) { damage = _targetNpc.getCurrentHp(); } else { damage = calcNpcMagicDamage(skillId); } S_SkillSound skill = new
				 * S_SkillSound(_targetNpc.getId(), 18404); _targetNpc.sendPackets(skill, false); Broadcaster.broadcastPacket(_targetNpc, skill, false); skill.clear(); } S_SkillSound effect = new S_SkillSound(_targetNpc.getId(), 18402); _targetNpc.sendPackets(effect,
				 * false); Broadcaster.broadcastPacket(_targetNpc, effect, false); effect.clear();
				 */
			}
		}  else {
			if (_calcType == PC_PC) {
				if (_pc.is_assassination_level2())
					damage += 2;
				else if (_pc.is_assassination_level1())
					++damage;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				damage = calcPcMagicDamage(skillId);
				if (skillId == L1SkillId.VAMPIRIC_TOUCH || skillId == L1SkillId.CHILL_TOUCH) {
					damage = Math.max(2, damage);
				}

			} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
				damage = calcNpcMagicDamage(skillId);
			}
			
			if (skillId == TYRANT ) {
				if (_pc.isPassive(MJPassiveID.TYRANT_EXCUTION.toInt())) {
					damage *= (double)Config.MagicAdSetting_Prince.TYRANT_EXCUTE_DMG_RATE/100;
				}
			}
		}

		// TODO MR에 의해
		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
		if (skill != null) {
			if (skill.is_magic_dmg_mr_impact()) {
				damage = calcMrDefense(damage);
			}
		} else {
			damage = calcMrDefense(damage);
		}

		if (_calcType == PC_PC) {
			damage += CharacterBalance.getInstance().getMagicDmg(_pc.getType(), _targetPc.getType());
			damage *= CharacterBalance.getInstance().getMagicDmgRate(_pc.getType(), _targetPc.getType());
		} else if (_calcType == PC_NPC) {
			damage += CharacterBalance.getInstance().getMagicDmg(_pc.getType(), 10);
			damage *= CharacterBalance.getInstance().getMagicDmgRate(_pc.getType(), 10);
		} else if (_calcType == NPC_PC) {
			damage += CharacterBalance.getInstance().getMagicDmg(10, _targetPc.getType());
			damage *= CharacterBalance.getInstance().getMagicDmgRate(10, _targetPc.getType());
		} else if (_calcType == NPC_NPC) {
			damage += CharacterBalance.getInstance().getMagicDmg(10, 10);
			damage *= CharacterBalance.getInstance().getMagicDmgRate(10, 10);
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (skillId == ENERGY_BOLT || skillId == CALL_LIGHTNING || skillId == DISINTEGRATE) {
				for (L1ItemInstance armor : _targetPc.getEquipSlot().getArmors()) {
					// 붉은 기사의 방패
					if (armor.getItemId() == 20230) {
						int probability = 1;

						if (armor.getEnchantLevel() >= 10) {
							probability = 5;
						} else if (armor.getEnchantLevel() < 6) {
							probability = 1;
						} else {
							probability = armor.getEnchantLevel() - 5;
						}

						if (_random.nextInt(100) < probability) {
							damage *= 0.8D;
						}

						break;
					}
				}
			}

			if (_pc != null && _pc.isPassive(MJPassiveID.SOLID_NOTE.toInt())) {
				if (_target.hasSkillEffect(STUN_TYPE_SKILL)) {
					if (MJRnd.isWinning(100, Config.MagicAdSetting_DragonKnight.RAMPAGE_P)) {
						damage *= Config.MagicAdSetting_DragonKnight.RAMPAGE_D;
					}
				}
			}

			// if (damage > _targetPc.getCurrentHp()) {
			// if (_targetPc.isElf() && _targetPc.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
			// if (damage > _targetPc.getCurrentHp() + _targetPc.getCurrentMp()) {
			// damage = _targetPc.getCurrentHp() + _targetPc.getCurrentMp();
			// }
			// } else {
			// damage = _targetPc.getCurrentHp();
			// }
			// }

			// 전사스킬 : 타이탄 매직
			// HP가 40% 미만일때 마법공격을 확률적으로 반사.
			if (_targetPc.isPassive(MJPassiveID.TITAN_MAGIC.toInt())) {
				if ((_calcType == PC_PC) && _pc.isWizard()) {
					if(_pc.hasSkillEffect(MAGIC_RAGE1)) {
						_pc.removeSkillEffect(MAGIC_RAGE1);
						_pc.set_magic_add_count(2);
						_pc.setSkillEffect(L1SkillId.MAGIC_RAGE2, 5000);
						L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE2, 5);
					} else if(_pc.hasSkillEffect(MAGIC_RAGE2)) {
						_pc.removeSkillEffect(MAGIC_RAGE2);
						_pc.set_magic_add_count(3);
						_pc.setSkillEffect(L1SkillId.MAGIC_RAGE3, 5000);
						L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE3, 5);
					} else if(_pc.hasSkillEffect(MAGIC_RAGE3)) {
						_pc.removeSkillEffect(MAGIC_RAGE3);
						_pc.set_magic_add_count(4);
						_pc.setSkillEffect(L1SkillId.MAGIC_RAGE4, 5000);
						L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE4, 5);
					} else if(_pc.hasSkillEffect(MAGIC_RAGE4)) {
						_pc.removeSkillEffect(MAGIC_RAGE4);
						_pc.set_magic_add_count(5);
						_pc.setSkillEffect(L1SkillId.MAGIC_RAGE5, 5000);
						L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE5, 5);
					} else if(_pc.hasSkillEffect(MAGIC_RAGE5)) {
						_pc.removeSkillEffect(MAGIC_RAGE5);
						_pc.set_magic_add_count(5);
						_pc.setSkillEffect(L1SkillId.MAGIC_RAGE5, 5000);
						L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE5, 5);
					} else {
						_pc.set_magic_add_count(1);
						_pc.setSkillEffect(L1SkillId.MAGIC_RAGE1, 5000);
						L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE1, 5);
					}
					damage += damage * (_pc.get_magic_add_count() * Config.MagicAdSetting_Wizard.MAGIC_CONTINUE_DMG_PER);
				}
				if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN) && !_targetPc.hasSkillEffect(L1SkillId.EMPIRE) && !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)
						&& !_targetPc.hasSkillEffect(L1SkillId.CRUEL)
						&& !_targetPc.hasSkillEffect(L1SkillId.PANTHERA)
						&& !_targetPc.hasSkillEffect(L1SkillId.DISINTEGRATE)) {
					
					
					int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
					int chance = _random.nextInt(100) + 1;
					int titan_per = Config.MagicAdSetting_Warrior.TITANMAGICPRO;
					int titan_rising_per = 0;
					int targetlevel = _targetPc.getLevel();
						//타이탄 락 레벨별 확률 95일때 세팅값
					if (targetlevel < 95) {
						titan_per -= (95 - targetlevel) * 2;
						if (titan_per <= 15) {
							titan_per = 15;
						}
					}
					if (!MJCommons.isUnbeatable(_targetPc)){
						if (percent<50){
							titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
							if (chance <= titan_rising_per){
								if (_targetPc.getInventory().checkItem(41246, 5)) {
									if (_calcType == PC_PC) {
										_pc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
									} else if (_calcType == PC_NPC){
										_npc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
									}
									damage = 0;
									_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
									_targetPc.getInventory().consumeItem(41246, 5);
									_targetPc.send_effect(12559);
								} else {
									_targetPc.sendPackets(new S_SystemMessage("泰坦魔法：催化劑不足。"));
								}
							}
						} else {
							if (chance < titan_per){
								if (_calcType == PC_PC) {
									_pc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
								} else if (_calcType == PC_NPC){
									_npc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
								}
								damage = 0;
								_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
								_targetPc.send_effect(12559);
								_targetPc.getInventory().consumeItem(41246, 5);
							}
						}
					}
				} else {
					int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
					int chance = _random.nextInt(100) + 1;
					int titan_per = Config.MagicAdSetting_Warrior.TITANMAGICPRO;
					int titan_rising_per = 0;
					int targetlevel = _targetPc.getLevel();
						//타이탄 락 레벨별 확률 95일때 세팅값
					if (targetlevel < 95) {
						titan_per -= (95 - targetlevel) * 2;
						if (titan_per <= 15) {
							titan_per = 15;
						}
					}
					if (_targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) !=null ) {
						if (!MJCommons.isUnbeatable(_targetPc)){
							if (percent<50){
								if (chance <= titan_rising_per){
									if (_targetPc.getInventory().checkItem(41246, 5)) {
										if (_calcType == PC_PC) {
											_pc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
										} else if (_calcType == PC_NPC){
											_npc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
										}
										damage = 0;
										_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
										_targetPc.getInventory().consumeItem(41246, 5);
										_targetPc.send_effect(12559);
									} else {
										_targetPc.sendPackets(new S_SystemMessage("泰坦魔法：催化劑不足。"));
									}
								}
							} else {
								if (chance < titan_per){
									if (_calcType == PC_PC) {
										_pc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
									} else if (_calcType == PC_NPC){
										_npc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
									}
									damage = 0;
									_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
									_targetPc.send_effect(12559);
									_targetPc.getInventory().consumeItem(41246, 5);
								}
							}
						}
						
						
						
					}
				}
				
				
				
				
				
				return (int) damage;
			}
		}			
									


					
		
		if ((_calcType == PC_PC || _calcType == PC_NPC) && _pc.isWizard()) {
			if(_pc.hasSkillEffect(MAGIC_RAGE1)) {
				_pc.removeSkillEffect(MAGIC_RAGE1);
				_pc.set_magic_add_count(2);
				_pc.setSkillEffect(L1SkillId.MAGIC_RAGE2, 5000);
				L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE2, 5);
			} else if(_pc.hasSkillEffect(MAGIC_RAGE2)) {
				_pc.removeSkillEffect(MAGIC_RAGE2);
				_pc.set_magic_add_count(3);
				_pc.setSkillEffect(L1SkillId.MAGIC_RAGE3, 5000);
				L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE3, 5);
			} else if(_pc.hasSkillEffect(MAGIC_RAGE3)) {
				_pc.removeSkillEffect(MAGIC_RAGE3);
				_pc.set_magic_add_count(4);
				_pc.setSkillEffect(L1SkillId.MAGIC_RAGE4, 5000);
				L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE4, 5);
			} else if(_pc.hasSkillEffect(MAGIC_RAGE4)) {
				_pc.removeSkillEffect(MAGIC_RAGE4);
				_pc.set_magic_add_count(5);
				_pc.setSkillEffect(L1SkillId.MAGIC_RAGE5, 5000);
				L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE5, 5);
			} else if(_pc.hasSkillEffect(MAGIC_RAGE5)) {
				_pc.removeSkillEffect(MAGIC_RAGE5);
				_pc.set_magic_add_count(5);
				_pc.setSkillEffect(L1SkillId.MAGIC_RAGE5, 5000);
				L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE5, 5);
			} else {
				_pc.set_magic_add_count(1);
				_pc.setSkillEffect(L1SkillId.MAGIC_RAGE1, 5000);
				L1SkillUse.on_icons(_pc, L1SkillId.MAGIC_RAGE1, 5);
			}
			damage += damage * (_pc.get_magic_add_count() * Config.MagicAdSetting_Wizard.MAGIC_CONTINUE_DMG_PER);
		}
		return damage;
	}

	public int calcPcFireWallDamage() {
		int dmg = 0;

		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = calcAttrDefence(l1skills.getDamageValue(), L1Skills.ATTR_FIRE);

		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			dmg = 0;
		}
		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	public int calcNpcFireWallDamage() {
		int dmg = 0;

		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = calcAttrDefence(l1skills.getDamageValue(), L1Skills.ATTR_FIRE);

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			dmg = 0;
		}
		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	/**
	 * TODO NPC -> PC (PC->PC) 마법 대미지 산출
	 **/
	private int calcPcMagicDamage(int skillId) {
		int dmg = 0;

		dmg = calcMagicDiceDamage(skillId);
		dmg = (dmg * getLeverage()) / 10;

		if (_targetPc.hasSkillEffect(COOKING_1_0_S) || _targetPc.hasSkillEffect(COOKING_1_1_S) || _targetPc.hasSkillEffect(COOKING_1_2_S) || _targetPc.hasSkillEffect(COOKING_1_3_S)
				|| _targetPc.hasSkillEffect(COOKING_1_4_S) || _targetPc.hasSkillEffect(COOKING_1_5_S) || _targetPc.hasSkillEffect(COOKING_1_6_S) || _targetPc.hasSkillEffect(COOKING_1_8_S)
				|| _targetPc.hasSkillEffect(COOKING_1_9_S) || _targetPc.hasSkillEffect(COOKING_1_10_S) || _targetPc.hasSkillEffect(COOKING_1_11_S) || _targetPc.hasSkillEffect(COOKING_1_12_S)
				|| _targetPc.hasSkillEffect(COOKING_1_13_S) || _targetPc.hasSkillEffect(COOKING_1_14_S) || _targetPc.hasSkillEffect(COOKING_1_16_S) || _targetPc.hasSkillEffect(COOKING_1_17_S)
				|| _targetPc.hasSkillEffect(COOKING_1_18_S) || _targetPc.hasSkillEffect(COOKING_1_19_S) || _targetPc.hasSkillEffect(COOKING_1_20_S) || _targetPc.hasSkillEffect(COOKING_1_21_S)
				|| _targetPc.hasSkillEffect(COOKING_1_22_S)) {
			dmg -= 4;
		}
		if (_targetPc.hasSkillEffect(COOKING_1_7_S) || _targetPc.hasSkillEffect(COOKING_1_15_S) || _targetPc.hasSkillEffect(COOKING_1_20_S)) {
			dmg -= 4;
		}

		if (_targetPc.isPassive(MJPassiveID.MAJESTY.toInt())) {
			int targetPcLvl = _targetPc.getLevel();
			int reduction = 0;
			if (targetPcLvl < 80) {
				targetPcLvl = 80;
			} else if (targetPcLvl >= 80){
				reduction = (targetPcLvl - 80) / 2 + 2;
			}
			if (reduction >= 10){
				reduction = 10;
			}
			
			dmg -= reduction;
		}
		if (_targetPc.hasSkillEffect(L1SkillId.PATIENCE)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 80)
				targetPcLvl = 80;
			dmg -= (targetPcLvl - 80) / 4 + 1;
		}
		if (_targetPc.hasSkillEffect(EARTH_GUARDIAN)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 80) {
				targetPcLvl = 80;
			}
			dmg -= (targetPcLvl - 80) / 4 + 1;
		}
		if (_calcType == NPC_PC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
			if (castleId > 0) {
				isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
			}
			if (!isNowWar) {
				if (_npc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_npc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _npc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
		}

//		if (_targetPc.hasSkillEffect(DRAGON_SKIN)) {
		if (_targetPc.isPassive(MJPassiveID.DRAGON_SKIN_PASS.toInt())) {
			dmg -= 5;
		}

		if (_calcType == PC_PC) {
			if (_targetPc.get_pvp_defense_per() > 0) {
				dmg -= (dmg * 0.01) * _targetPc.get_pvp_defense_per();
			}
		}
		
		if (_targetPc.get_Magic_defense_per() > 0) {
			dmg -= (dmg * 0.01) * _targetPc.get_Magic_defense_per();
		}
		

		
		
		// dmg -= (dmg * _targetPc.getImmuneReduction());

		if (_targetPc.hasSkillEffect(FEATHER_BUFF_A)) {
			dmg -= 3;
		}
		if (_targetPc.hasSkillEffect(FEATHER_BUFF_B)) {
			dmg -= 2;
		}

		/*** 신규레벨보호 ***/
		// if (_calcType == PC_PC) {
		// int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
		// if (castle_id == 0) {
		// if (_targetPc.getLevel() < Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION
		// || _pc.getLevel() < Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION &&
		// !_pc.isPinkName()) {
		// dmg /= 2;
		// _pc.sendPackets(new S_SystemMessage("신규유저는 대미지의 50%만 가해집니다."));
		// _targetPc.sendPackets(new S_SystemMessage("신규유저는 대미지를 50%만 받습니다."));
		// }
		// }
		// }

		/** 防止新血盟攻擊 **/
		if (_calcType == PC_PC) {
			if (Config.ServerAdSetting.CASTLEWAR) {
				// TODO 特定座標無法攻擊
				int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
				if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
					MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
					L1Clan defense = war.getDefenseClan();
					boolean Range = false;
					// 忽略下面的座標 (守護塔的守護血盟忽略所有條件 <- 攻城血盟只能攻擊守護血盟)
					if (_pc.getClan() != defense) {
						for (L1Object l1object : L1World.getInstance().getObject()) {
							if (l1object instanceof L1TowerInstance) {
								L1TowerInstance tower = (L1TowerInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id, tower.getLocation())) {
									Range = (_pc.getLocation().getTileLineDistance(new Point(l1object.getX(), l1object.getY())) > 10);
									if (Range && _targetPc.getClan() != defense && _targetPc.getClan() != _pc.getClan() && _targetPc.getRedKnightClanId() == 0) {
										_pc.sendPackets("\fY攻城血盟之間只能在守護塔周圍PK。");
										_targetPc.sendPackets("\fY攻城血盟之間只能在守護塔周圍PK。");
										return 0;
									}
								}
							}
						}
					}
				}
			}
		}
			
			int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
			if (castle_id == 0) {
				if (Config.ServerAdSetting.CLANSETTINGPROTECTION) {
					boolean attack_ok = false;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(_targetPc)) {
						if (!(obj instanceof L1MonsterInstance)) {
							continue;
						}

						if (obj instanceof L1MonsterInstance) {
							L1MonsterInstance mon = (L1MonsterInstance) obj;
							int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId());
							if (monid != 0) {
								attack_ok = true;
								break;
							}
						}
					}

					if (!attack_ok) {
						if (_pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION || _targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION) {
							if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL && skillId != NATURES_BLESSING) { // 增益類
								_pc.sendPackets(new S_SystemMessage("新手用戶之間無法互相攻擊。"));
								_targetPc.sendPackets(new S_SystemMessage("新手用戶之間無法互相攻擊。"));
								return 0;
							}
						}
					}
				} else {
					if (_pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION || _targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION) {
						if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL && skillId != NATURES_BLESSING) { // 增益類
							dmg /= 2;
							_pc.sendPackets(new S_SystemMessage("新手用戶只受到50%的傷害。"));
							_targetPc.sendPackets(new S_SystemMessage("新手用戶只受到50%的傷害。"));
						}
					}
				}
			}
		}

		/** 特定地區禁止PK **/
		if (_calcType == PC_PC) {
				if (_pc.getMapId() == 254 || _pc.getMapId() == 612 || _pc.getMapId() == 1930) {
					_pc.sendPackets("\fY在育成狩獵地無法PK。");
					_targetPc.sendPackets("\fY在育成狩獵地無法PK。");
					dmg = 0;
				}
			}

		dmg -= L1MagicDoll.getDamageReductionByDoll(_npc, _targetPc);

		double total_reduction = 0;
		// 알아보기 getDamageReductionByArmor = 공격(물리대미지) 만인지 아니면 마법도 포함인지.
		// 마법은 미포함이면 밑에 삭제 (본섭은 대미지 리덕션이 마법에도 영향)
		total_reduction += _targetPc.getDamageReductionByArmor();
		total_reduction += _targetPc.getDamageReduction(); // 방어구에 의한 대미지 감소

		double total_pvpreduction = 0;
		total_pvpreduction += _targetPc.get_pvp_mdmg();
		
		double total_pvpreductionignore = 0;
		total_pvpreductionignore -= _targetPc.get_pvp_dmg_ignore()/* * Config.CharSettings.MagicReductionRation */;
		dmg = (int) Math.max(dmg - total_reduction - total_pvpreduction + total_pvpreductionignore, 0);

		if (dmg < 0) {
			dmg = 0;
		}
		
//		if (dmg > 0 && _targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
//			if (_pc != null) {
//				if (MJRnd.isWinning(1000000, 1000000 - (_pc.get_immune_ignore() * 10000))) {
//					if (_pc.get_immune_minus_per() > 0)
//						dmg += (dmg * 0.01) * _pc.get_immune_minus_per();
//					
//					dmg -= (dmg * _targetPc.getImmuneReduction());
//				}
//			}
//		}
		if (_targetPc.get_reduction_per() >0){
			int reduction_per = _target.get_reduction_per();
			dmg -= dmg * reduction_per / 100;
		}
		if (_targetPc.get_Magic_defense_per() >0){
			dmg *= (double)(100-_targetPc.get_Magic_defense_per())/100;
		}
		
		if (dmg > 0 && _targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
			if (_pc != null) {
				if (!_pc.isWizard()){
					dmg -= dmg *_targetPc.getImmuneReduction()*_pc.get_immune_ignore() / 100;
//					
//					if (MJRnd.isWinning(1000000, 1000000 - (_pc.get_immune_ignore() * 10000))) {
//						if (_pc.get_immune_minus_per() > 0)
//							dmg += (dmg * 0.01) * _pc.get_immune_minus_per();
//	
//						dmg -= (dmg * _targetPc.getImmuneReduction());
//					}
				} else {
					dmg *= 0.5D;
				}
			}
		}
		
		if (dmg > 0 && _targetPc.hasSkillEffect(BRAVE_UNION)){
			dmg *= 0.95D;
			L1Party party = _targetPc.getParty();
			//int partynumber = 0;
			int reduction = 0;
			double dmgorigin = dmg;
			double dmggap = 0;
			
			ArrayList<L1PcInstance> partymember = new ArrayList<>();; 
			if (party != null) {
				for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(_targetPc, 8)) {
					if (_targetPc.getClan() != player.getClan()) {
						continue;
					}
					if (!party.getList().contains(_targetPc)) {
						continue;
					}
					if (player.getCurrentHpPercent() <= 30) {
						continue;
					} 
					partymember.add(player);
				}
				if (partymember.size() >= 7) {
					reduction = 15;
				} else {
					reduction = partymember.size() * 2;
				}
				dmg *= (100 - reduction)/100D;
				dmggap = dmgorigin - dmg;
				int dmggapint = (int) dmggap;
				if (partymember.size() > 0) {
					int dmgdiv = dmggapint / partymember.size();
					
					if (dmgdiv >0) {
						for (L1PcInstance player : partymember) {
							int hp = player.getCurrentHp();
							player.setCurrentHp(hp - dmgdiv);
							player.send_effect(21814);
						}
					}
					partymember.clear();
				}
				
			}
		}

		return dmg;
	}

	/**
	 * TODO PC -> NPC에게 마법 대미지 산출
	 **/
	private int calcNpcMagicDamage(int skillId) {
		int dmg = 0;
		dmg = calcMagicDiceDamage(skillId);

		/**
		 * TODO PC->NPC 마법 대미지 외부화
		 **/
		if (_calcType == PC_NPC) {
			dmg = (dmg * getLeverage() / 10);
		} else {
			dmg = (dmg * getLeverage() / 10);
		}
		if (_calcType == PC_NPC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
			if (castleId > 0) {
				isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
			}
			if (!isNowWar) {
				if (_targetNpc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_targetNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _targetNpc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
		}

		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId >= 45912 && npcId <= 45915 && !_pc.hasSkillEffect(STATUS_HOLY_WATER)) {
				dmg = 0;
			}
			if (npcId == 45916 && !_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
				dmg = 0;
			}
			if (npcId == 45941 && !_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
				dmg = 0;
			}
			if (npcId >= 46068 && npcId <= 46091 && _pc.getCurrentSpriteId() == 6035) {
				dmg = 0;
			}
			if (npcId >= 46092 && npcId <= 46106 && _pc.getCurrentSpriteId() == 6034) {
				dmg = 0;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
				dmg = 0;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
				dmg = 0;
			}
			if ((_targetNpc.getNpcTemplate().get_gfxid() == 7864 || _targetNpc.getNpcTemplate().get_gfxid() == 7869 || _targetNpc.getNpcTemplate().get_gfxid() == 7870)) {
				dmg *= 1.5; // 파푸리온 혈흔1.5뎀
			}

			L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(_pc.getMapId());
			if (sm != null) {
				dmg *= (sm.getMdmgReduction() * 0.01);
				if (dmg <= 0)
					dmg = 1;
			}
			if (skillId == L1SkillId.HOWL) {
				dmg = 50;
			}
			// System.out.println("대미지 :" + dmg);
		}
		return dmg;
	}

	/**
	 * TODO PC -> PC 에게 마법 대미지 산출
	 **/
	private int calcMagicDiceDamage(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int diceCount = l1skills.getDamageDiceCount();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;
		double PowerMr = 0;

		Random random = new Random();

		if (l1skills != null) {
			if (!l1skills.is_magic_dmg_int_impact()) {
				for (int i = 0; i < diceCount; i++) {
					if (dice > 0) {
						int plus_dmg = (_random.nextInt(dice + 1) + 1);
						magicDamage += plus_dmg;
					}
				}
				magicDamage += value;
			} else {
				dice += getSpellPower() / 2;
				for (int i = 0; i < diceCount; i++) {
					if (dice > 0) {
						int plus_dmg = (_random.nextInt(dice + 1) + 1);
						magicDamage += plus_dmg;
					}
				}
				magicDamage += value * (1 + getSpellPower() / 10);
			}
		}

//		 System.out.println("대미지: " + magicDamage);

		/** 치명타 발생 부분 */
		double criticalCoefficient = 1.4;
		int rnd = random.nextInt(100) + 1;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int propCritical = CalcStat.calcMagicCritical(_pc.ability.getTotalInt());
			switch (skillId) {
			// 6레벨 이하 광역마법 제외한 공격마법
			case ENERGY_BOLT:
			case ICE_DAGGER:
			case WIND_CUTTER:
			case CHILL_TOUCH:
			case SMASH:
			case FIRE_ARROW:
			case STALAC:
			case VAMPIRIC_TOUCH:
			case CONE_OF_COLD:
			case CONE_OF_COLD_mob:
			case CALL_LIGHTNING:
			case DISINTEGRATE:
				propCritical = +Config.MagicAdSetting.PRORCRITICAL_MAGIC;
				break;
			}

			// 마안 일정확률로 마법치명타+1
			if (_pc.hasSkillEffect(LIND_MAAN) || _pc.hasSkillEffect(SHAPE_MAAN) || _pc.hasSkillEffect(LIFE_MAAN) || _pc.hasSkillEffect(L1SkillId.NAVER_BLACK_DRAGON_MAAN)) {
				propCritical += 1;
			}
			propCritical += (_pc.getBaseMagicCritical() * Config.MagicAdSetting.CHARACTERMAGICCRIRATE);
			//2022-11-05 크리대미지 증가 삭제
/*			if (criticalOccur(propCritical)) {
				if(_pc.getWeapon() != null) {
					if(_pc.getInventory().checkEquipped(203041)) {
						magicDamage *= 2.0;
					} else {
						magicDamage *= 1.5;
					}
				} else {
					magicDamage *= 1.5;
				}
			}*/
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			if (rnd <= 15) {
				magicDamage *= criticalCoefficient;
			}
		}

		// 디스마법은 라우풀에 따라 데미지 상향처리.
		// : 카오틱수치가 높을수록 데미지 하향
		/** 디스 본섭화 **/
		if (_calcType == PC_PC || _calcType == PC_NPC){
			if (skillId == DISINTEGRATE) {
				int lawful = getLawful();
				if (lawful > 0) {
					magicDamage += (lawful / Config.MagicAdSetting_Wizard.DISINTLAWFULWEIGHT);
				} else if (lawful < 0) {
					magicDamage += (lawful / Config.MagicAdSetting_Wizard.DISINTCHAOTICWEIGHT);
				}
				if(_pc.getPassive(MJPassiveID.DISINTEGRATE_NEMESIS.toInt()) != null){
					magicDamage *= (double)(1 + (Config.MagicAdSetting_Wizard.NEMESISDAMAGE / 100)) ;
				}else {
				}
			}
		}
		
		if (l1skills != null) {
			if (l1skills.is_magic_dmg_mr_impact()) {
//				if (getTargetMr() < Config.MagicAdSetting.TargetMr) {
//					// 마방150되면 10당 (기본대미지*마법상수)의 5% 대미지 줄어들게 설정 총50%
//					PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr1;
//				} else {
//					PowerMr = 0.5 + (getTargetMr() - 100) / (double) Config.MagicAdSetting.TargetMr_vl6;
//				} // 마방 500되면 마법대미지 0
//			}
				if (getTargetMr() <= Config.MagicAdSetting.TargetMr) { // ~100까지
//					System.out.println("1"+getTargetMr()+" + "+Config.MagicAdSetting.TargetMr_vl);
					PowerMr =(double)( getTargetMr() / Config.MagicAdSetting.TargetMr_vl);
				} else if (getTargetMr() > Config.MagicAdSetting.TargetMr && getTargetMr() <= Config.MagicAdSetting.TargetMr1) { //~200까지
					
//					System.out.println("2"+getTargetMr()+" + "+Config.MagicAdSetting.TargetMr_v2);
					PowerMr = (double)(((getTargetMr() - Config.MagicAdSetting.TargetMr) / Config.MagicAdSetting.TargetMr_v2));
					PowerMr += 0.5;
				} else if (getTargetMr() > Config.MagicAdSetting.TargetMr1 && getTargetMr() <= Config.MagicAdSetting.TargetMr2) { //~300까지
//					System.out.println("3");
					PowerMr = (double)(((getTargetMr() - Config.MagicAdSetting.TargetMr1) / Config.MagicAdSetting.TargetMr_v3));
					PowerMr += 0.5 + 0.25;
				} else if (getTargetMr() > Config.MagicAdSetting.TargetMr2 && getTargetMr() <= Config.MagicAdSetting.TargetMr3) { //~400까지
//					System.out.println("4");
					PowerMr = (double)(((getTargetMr() - Config.MagicAdSetting.TargetMr2) / Config.MagicAdSetting.TargetMr_v3));
					PowerMr += 0.5 + 0.25 + 0.125;
					
				} else if (getTargetMr() > Config.MagicAdSetting.TargetMr3 && getTargetMr() <= Config.MagicAdSetting.TargetMr4) { //~500까지
//					System.out.println("5");
					PowerMr = (double)(((getTargetMr() - Config.MagicAdSetting.TargetMr3) / Config.MagicAdSetting.TargetMr_v4));
					PowerMr += 0.5 + 0.25 + 0.125 + 0.0625;
				} else if (getTargetMr() > Config.MagicAdSetting.TargetMr4 && getTargetMr() <= Config.MagicAdSetting.TargetMr5) { //~600까지
//					System.out.println("6");
					PowerMr = (double)(((getTargetMr() - Config.MagicAdSetting.TargetMr4) / Config.MagicAdSetting.TargetMr_v5));
					PowerMr += 0.5 + 0.25 + 0.125 + 0.0625 + 0.03125;
				} else if (getTargetMr() > Config.MagicAdSetting.TargetMr5 && getTargetMr() <= Config.MagicAdSetting.TargetMr6) { //~700까지
//					System.out.println("7");
					PowerMr = (double)(((getTargetMr() - Config.MagicAdSetting.TargetMr5) / Config.MagicAdSetting.TargetMr_v6));
					PowerMr += 0.5 + 0.25 + 0.125 + 0.0625 + 0.03125 + 0.015625;
				} else {
//					System.out.println("8");
					PowerMr = (double)(((getTargetMr() - Config.MagicAdSetting.TargetMr5) / Config.MagicAdSetting.TargetMr_v7));
					PowerMr += 0.5 + 0.25 + 0.125 + 0.0625 + 0.03125 + 0.015625 + 0.0078125;
				}
				
				if (PowerMr >= 1) {
					System.out.println("(魔防漏洞懷疑): (帳號: "+_targetPc.getAccountName()+") + (角色名稱: "+_targetPc.getName()+")");
				}
/*				// XXX 魔防170時，每增加10點減少 (基本傷害*魔法常數) 的5%傷害，最多減少50%
				if (getTargetMr() < Config.MagicAdSetting.TargetMr) {
				PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr1;
				// XXX 魔防在101~201之間時，將目標魔防除以270，然後將結果乘以基本魔法傷害後減去得到的值
				} else if (getTargetMr() >= Config.MagicAdSetting.TargetMr2 && getTargetMr() < Config.MagicAdSetting.TargetMr3) {
				PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr_vl1;
				// XXX 魔防在202~301之間時，將目標魔防除以370，然後將結果乘以基本魔法傷害後減去得到的值
				} else if (getTargetMr() >= Config.MagicAdSetting.TargetMr4 && getTargetMr() < Config.MagicAdSetting.TargetMr5) {
				PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr_vl2;
				// XXX 魔防在302~401之間時，將目標魔防除以470，然後將結果乘以基本魔法傷害後減去得到的值
				} else if (getTargetMr() >= Config.MagicAdSetting.TargetMr6 && getTargetMr() < Config.MagicAdSetting.TargetMr7) {
				PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr_vl3;
				// XXX 魔防在402~501之間時，將目標魔防除以570，然後將結果乘以基本魔法傷害後減去得到的值
				} else if (getTargetMr() >= Config.MagicAdSetting.TargetMr8 && getTargetMr() < Config.MagicAdSetting.TargetMr9) {
					PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr_vl4;
					// XXX 魔防在502~601之間時，將目標魔防除以670，然後將結果乘以基本魔法傷害後減去得到的值
				} else if (getTargetMr() >= Config.MagicAdSetting.TargetMr10 && getTargetMr() < Config.MagicAdSetting.TargetMr11) {
				PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr_vl5;
				// XXX 魔防在602~701之間時，將目標魔防除以770，然後將結果乘以基本魔法傷害後減去得到的值
				} else if (getTargetMr() >= Config.MagicAdSetting.TargetMr12 && getTargetMr() < Config.MagicAdSetting.TargetMr13) {
				PowerMr = getTargetMr() / (double) Config.MagicAdSetting.TargetMr_vl6;
				} else {
				// XXX 當魔防超過602點，且達到或超過770點時，魔法傷害減至0
				PowerMr = 0.5 + (getTargetMr() - 100) / (double) Config.MagicAdSetting.TargetMr_vl7;
				}*/
			}
		}
//        System.out.println("魔防前"+magicDamage);
//        System.out.println("MR"+PowerMr);
		magicDamage -= magicDamage * PowerMr; // 先處理因魔防導致的傷害減少

//        System.out.println("魔防後"+magicDamage);
		double attrDeffence = calcAttrResistance(l1skills.getAttr());
		// 屬性防禦每100點減少45%。
		// 每增加10點減少超過部分的4.5%
		// 每10點設定減少0.9%
		magicDamage -= magicDamage * attrDeffence; // 處理因魔防導致的傷害減少後，再處理屬性防禦導致的傷害減少
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicDamage += _pc.getBaseMagicDmg(); // 添加基礎屬性魔法傷害加成
		}
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int weaponAddDmg = 0;
			L1ItemInstance weapon = _pc.getWeapon();
			if (weapon != null) {
				weaponAddDmg = _pc.getMagicDmgup();
			}
			magicDamage += (magicDamage * 0.01) * weaponAddDmg; // 添加武器導致的魔法傷害
		}
// 			System.out.println("魔法傷害總計 : " + magicDamage);
		return magicDamage;
	}

	public int calcHealing(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;

		int magicBonus = getMagicBonus();
		if (magicBonus > 10) {
			magicBonus = 10;
		}

		int diceCount = value + magicBonus;
		for (int i = 0; i < diceCount; i++) {
			magicDamage += (_random.nextInt(dice) + 1) * 0.8;
		}

		double alignmentRevision = 1.0;
		if (getLawful() > 0) {
			alignmentRevision += (getLawful() / 32768.0);
		}

		magicDamage *= alignmentRevision;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicDamage = (magicDamage * getLeverage()) / 10;
			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(_pc.getId());
			double special_point = 1.0;
			if (Info != null) {
				special_point += CalcStat.calcAinhasadStatSecond(Info.get_lucky()) * 0.01;
			}
			magicDamage *= special_point;
		}
		return magicDamage;
	}

	public int calcMrDefense(int dmg) {
		int PInt = 0;
		int mrs = 0;
		int attackPcLvSp = 0;
		int targetPcLvMr = 0;
		int ran1 = 0;
		int mrset = 0;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			PInt = _pc.getAbility().getSp() * 2;
		} else if (_calcType == NPC_PC) {
			PInt = _npc.getAbility().getSp() * 2;
		}
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			mrs = (int) (_targetPc.getMr() * 1.7D - 20); // 100 * 1.7 - 20
		} else {
			mrs = (int) (_targetNpc.getMr() * 1.7D - 20);
		}
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			attackPcLvSp = _pc.getLevel();
		} else if (_calcType == NPC_PC) {
			attackPcLvSp = _npc.getLevel();
		}
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			targetPcLvMr = _targetPc.getLevel();
		} else {
			targetPcLvMr = _targetNpc.getLevel();
		}

		Random random = new Random();
		ran1 = random.nextInt(15) + 1;
		mrset = mrs - ran1;

		int PPPP = (int) (attackPcLvSp / 8D + 1);
		int TTTT = (int) (targetPcLvMr / 10D + 1);
		int fail = PInt + PPPP - TTTT;

		if ((mrset - fail) >= 151) {
			dmg *= 0.01D;
		} else if ((mrset - fail) >= 146 && (mrset - fail) <= 150) {
			dmg *= 0.03D;
		} else if ((mrset - fail) >= 141 && (mrset - fail) <= 145) {
			dmg *= 0.07D;
		} else if ((mrset - fail) >= 136 && (mrset - fail) <= 140) {
			dmg *= 0.10D;
		} else if ((mrset - fail) >= 131 && (mrset - fail) <= 135) {
			dmg *= 0.13D;
		} else if ((mrset - fail) >= 126 && (mrset - fail) <= 130) {
			dmg *= 0.17D;
		} else if ((mrset - fail) >= 121 && (mrset - fail) <= 125) {
			dmg *= 0.20D;
		} else if ((mrset - fail) >= 116 && (mrset - fail) <= 120) {
			dmg *= 0.23D;
		} else if ((mrset - fail) >= 111 && (mrset - fail) <= 115) {
			dmg *= 0.27D;
		} else if ((mrset - fail) >= 106 && (mrset - fail) <= 110) {
			dmg *= 0.30D;
		} else if ((mrset - fail) >= 101 && (mrset - fail) <= 105) {
			dmg *= 0.33D;
		} else if ((mrset - fail) >= 96 && (mrset - fail) <= 100) {
			dmg *= 0.37D;
		} else if ((mrset - fail) >= 91 && (mrset - fail) <= 95) {
			dmg *= 0.40D;
		} else if ((mrset - fail) >= 86 && (mrset - fail) <= 90) {
			dmg *= 0.43D;
		} else if ((mrset - fail) >= 81 && (mrset - fail) <= 85) {
			dmg *= 0.47D;
		} else if ((mrset - fail) >= 76 && (mrset - fail) <= 80) {
			dmg *= 0.50D;
		} else if ((mrset - fail) >= 71 && (mrset - fail) <= 75) {
			dmg *= 0.53D;
		} else if ((mrset - fail) >= 66 && (mrset - fail) <= 70) {
			dmg *= 0.57D;
		} else if ((mrset - fail) >= 60 && (mrset - fail) <= 65) {
			dmg *= 0.60D;
		} else if ((mrset - fail) >= 51 && (mrset - fail) <= 56) {
			dmg *= 0.63D;
		} else if ((mrset - fail) >= 46 && (mrset - fail) <= 50) {
			dmg *= 0.67D;
		} else if ((mrset - fail) >= 41 && (mrset - fail) <= 45) {
			dmg *= 0.70D;
		} else if ((mrset - fail) >= 36 && (mrset - fail) <= 40) {
			dmg *= 0.73D;
		} else if ((mrset - fail) >= 31 && (mrset - fail) <= 35) {
			dmg *= 0.77D;
		} else if ((mrset - fail) >= 26 && (mrset - fail) <= 30) {
			dmg *= 0.80D;
		} else if ((mrset - fail) >= 21 && (mrset - fail) <= 25) {
			dmg *= 0.85D;
		} else if ((mrset - fail) >= 16 && (mrset - fail) <= 20) {
			dmg *= 0.90D;
		} else if ((mrset - fail) >= 11 && (mrset - fail) <= 15) {
			dmg *= 0.95D;
		} else if ((mrset - fail) >= 6 && (mrset - fail) <= 10) {
			dmg *= 1.00D;
		} else {
			dmg *= 1.05D;
		}
		return dmg;
	}

	private boolean criticalOccur(int prop) {
		if (_pc != null) {
			prop += _pc.get_magic_critical_rate();
		}
		
		int num = _random.nextInt(100) + 1;

		if (prop == 0) {
			return false;
		}
		if (num <= prop) {
			_CriticalDamage = true;
		}
		return _CriticalDamage;
	}

	private double calcAttrResistance(int attr) {
		int resist = 0;
		int resistFloor = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			switch (attr) {
			case L1Skills.ATTR_EARTH:
				resist = _targetPc.getResistance().getEarth();
				break;
			case L1Skills.ATTR_FIRE:
				resist = _targetPc.getResistance().getFire();
				break;
			case L1Skills.ATTR_WATER:
				resist = _targetPc.getResistance().getWater();
				break;
			case L1Skills.ATTR_WIND:
				resist = _targetPc.getResistance().getWind();
				break;
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
		}
		if (resist < 0) {
			resistFloor = (int) (-0.45 * Math.abs(resist));
		} else if (resist < 101) {
			resistFloor = (int) (0.45 * Math.abs(resist));
		} else {
			resistFloor = (int) (45 + 0.09 * Math.abs(resist));
			// 속성100초과분에 대해0.45의 1/5정도 감소되게 변경
		}
		double attrDeffence = resistFloor / 100;
		return attrDeffence;
	}

	private int calcAttrDefence(int dmg, int attr) {
		if (dmg < 1) {
			return dmg;
		}

		int resist = 0;

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			switch (attr) {
			case L1Skills.ATTR_EARTH:
				resist = _targetPc.getResistance().getEarth();
				break;
			case L1Skills.ATTR_FIRE:
				resist = _targetPc.getResistance().getFire();
				break;
			case L1Skills.ATTR_WATER:
				resist = _targetPc.getResistance().getWater();
				break;
			case L1Skills.ATTR_WIND:
				resist = _targetPc.getResistance().getWind();
				break;
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
		}

		dmg -= resist / 2;

		if (dmg < 1) {
			dmg = 1;
		}

		return dmg;
	}

	public void commit(int damage, int drainMana) {
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			commitPc(damage, drainMana);
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			commitNpc(damage, drainMana);
		}

		if (!Config.LogStatus.GMATKMSG) {
			return;
		}
		if (Config.LogStatus.GMATKMSG) {
			if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
				return;
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && !_targetPc.isGm()) {
				return;
			}
		}
		String msg0 = "";
		String msg2 = "";
		String msg3 = "";
		String msg4 = "";

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			msg0 = _pc.getName();
		} else if (_calcType == NPC_PC) {
			msg0 = _npc.getName();
		}

		if (_calcType == NPC_PC || _calcType == PC_PC) {
			msg4 = _targetPc.getName();
			msg2 = "HP:" + _targetPc.getCurrentHp();
		} else if (_calcType == PC_NPC) {
			msg4 = _targetNpc.getName();
			msg2 = "HP:" + _targetNpc.getCurrentHp();
		}

		msg3 = "DMG:" + damage;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
//			System.out.println("여기로옴?2");
			_pc.sendPackets(new S_SystemMessage("\\aG[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2));
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			_targetPc.sendPackets(new S_SystemMessage("\\aH[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2));
		}
	}

	private void commitPc(int damage, int drainMana) {
		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			damage = 0;
			drainMana = 0;
		}

		if (_calcType == PC_PC) {
			if (drainMana > 0 && _targetPc.getCurrentMp() > 0) {
				if (drainMana > _targetPc.getCurrentMp()) {
					drainMana = _targetPc.getCurrentMp();
				}
				int newMp = _pc.getCurrentMp() + drainMana;
				_pc.setCurrentMp(newMp);
			}
			_targetPc.receiveManaDamage(_pc, drainMana);
			_targetPc.receiveDamage(_pc, damage);
		} else if (_calcType == NPC_PC) {
			_targetPc.receiveDamage(_npc, damage);
		}
	}

	private void commitNpc(int damage, int drainMana) {
		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && _pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
			damage = 1;
			drainMana = 0;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && _pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
			damage = 1;
			drainMana = 0;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7720) {
			damage = 1;
			drainMana = 0;
		}

		if (_calcType == PC_NPC) {
			if (drainMana > 0) {
				int drainValue = _targetNpc.drainMana(drainMana);
				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);
			}
			_targetNpc.ReceiveManaDamage(_pc, drainMana);
			_targetNpc.receiveDamage(_pc, damage);
		} else if (_calcType == NPC_NPC) {
			_targetNpc.receiveDamage(_npc, damage);
		}
	}

	// ●●●● 戰士泰坦傷害計算 ●●●●
	private int calcTitanDamage() {
		double damage = 0;
		L1ItemInstance weapon = null;
		weapon = _targetPc.getWeapon();
		if (weapon != null) {
			damage = Math.round((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * 2);
		}
		return (int) damage;
	}
	//需要添加暈眩類技能
	private static final int[] STUN_TYPE_SKILL = { SHOCK_STUN, PANTHERA, EMPIRE, BONE_BREAK, FORCE_STUN, FORCE_STUN_FAIL, MOB_RANGESTUN_18, MOB_RANGESTUN_19, MOB_SHOCKSTUN_30, BOS_STUN18, OMAN_STUN,
			fornos_STUN, MOSTER_STUN_1, Besi_STUN, Moster_STUN, Maeno_STUN, ANTA_MESSAGE_6, ANTA_MESSAGE_7, ANTA_MESSAGE_8, ANTA_SHOCKSTUN, CHAINSWORD_STUN, BALOCH_STUN, DRAGON_HALPAS_STUN
			,PANTHERA, PANTERA_SHOCK, DISINTEGRATE, OSIRIS, TEMPEST, TRIPLE_STUN, CRUEL, CHAINSWORD_STUN, FOU_SLAYER};

}
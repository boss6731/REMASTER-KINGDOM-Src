package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.ArmorClass.MJArmorClass;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Action.MJAttackChain;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.NpcStatusDamage.NpcStatusDamageInfo;
import l1j.server.NpcStatusDamage.NpcStatusDamageType;
import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.datatables.CharacterBalance;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.datatables.UserProtectMonsterTable;
import l1j.server.server.datatables.WeaponAddHitRate;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.item.function.L1MagicDoll;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1FlameDamage;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_Attack;
import l1j.server.server.serverpackets.S_AttackCritical;
import l1j.server.server.serverpackets.S_AttackMissPacket;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_AttackPacketForNpc;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.templates.L1SpecialMap;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.model.L1EffectSpawn;

public class L1Attack {

	private Random random = new Random(System.nanoTime());

	private L1PcInstance _pc = null;

	private L1Character _target = null;

	private L1PcInstance _targetPc = null;

	private L1NpcInstance _npc = null;

	private L1NpcInstance _targetNpc = null;

	private final int _targetId;

	private int _targetX;

	private int _targetY;

	private int _statusDamage = 0;

	private static final Random _random = new Random(System.nanoTime());

	private int _hitRate = 0;

	private int _calcType;

	private static final int PC_PC = 1;

	private static final int PC_NPC = 2;

	private static final int NPC_PC = 3;

	private static final int NPC_NPC = 4;

	public boolean _isHit = false;

	public boolean _isCritical = false;

	private int _damage = 0;

	private int _attckGrfxId = 0;

	private int _attckActId = 0;

	// 공격자가 플레이어의 경우의 무기 정보
	private L1ItemInstance weapon = null;

	// 전사 쌍수
	private L1ItemInstance Sweapon = null;
	private int _SweaponAddDmg = 0;
	private int _SweaponSmall = 0;
	private int _SweaponLarge = 0;

	private int _weaponId = 0;

	private int _weaponType = 0;

	private int _weaponType2 = 0;

	private int _weaponAddDmg = 0;

	private int _weaponSmall = 0;

	private int _weaponLarge = 0;

	private int _weaponBless = 1;

	private int _weapon_bless_level = 0;

	private int _weaponMaterial = 0;

	private int _weaponDoubleDmgChance = 0;

	private int _weaponAttrLevel = 0; // 속성 레벨

	private int _attackType = 0;

	private L1ItemInstance _arrow = null;

	private L1ItemInstance _sting = null;

	private int _leverage = 10; // 1/10배로 표현한다.

	private int ICCD = 0;
	private int DCCD = 0;

	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	public void setActId(int actId) {
		_attckActId = actId;
	}

	public void setGfxId(int gfxId) {
		_attckGrfxId = gfxId;
	}

	public int getActId() {
		return _attckActId;
	}

	public int getGfxId() {
		return _attckGrfxId;
	}

	public L1Attack(L1Character attacker, L1Character target) {
		if (attacker instanceof L1PcInstance) {
			_pc = (L1PcInstance) attacker;
			if (target instanceof L1PcInstance) {
				_targetPc = (L1PcInstance) target;
				_calcType = PC_PC;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				_calcType = PC_NPC;
			}
			// 무기 정보의 취득
			weapon = _pc.getWeaponSwap();
			Sweapon = _pc.getSecondWeapon();
			if (Sweapon != null) {
				_SweaponAddDmg = Sweapon.getItem().getDmgModifier() + Sweapon.getDmgByMagic();
				_SweaponSmall = Sweapon.getItem().getDmgSmall();
				_SweaponLarge = Sweapon.getItem().getDmgLarge();
			}
			if (weapon != null) {
				_weaponId = weapon.getItem().getItemId();
				_weaponType = weapon.getItem().getType1();
				_weaponType2 = weapon.getItem().getType();// 변경
				_weaponAddDmg = weapon.getItem().getDmgModifier() + weapon.getDmgByMagic();
				_weaponSmall = weapon.getItem().getDmgSmall();
				_weaponLarge = weapon.getItem().getDmgLarge();
				_weaponBless = weapon.getItem().getBless();
				_weapon_bless_level = weapon.get_bless_level();
				_weaponMaterial = weapon.getItem().getMaterial();
				if (_weaponType == 20) { // 화살의 취득
					_arrow = _pc.getInventory().getArrow();
					if (_arrow != null) {
						_weaponBless = _arrow.getItem().getBless();
						_weaponMaterial = _arrow.getItem().getMaterial();
					}
				}
				if (_weaponType == 62) { // 스팅의 취득
					_sting = _pc.getInventory().getSting();
					if (_sting != null) {
						_weaponBless = _sting.getItem().getBless();
						_weaponMaterial = _sting.getItem().getMaterial();
					}
				}
				_weaponDoubleDmgChance = weapon.getItem().getDoubleDmgChance()
						+ (weapon.getEnchantLevel() * weapon.getItem().get_double_dmg_enchant_value());
				_weaponAttrLevel = weapon.getAttrEnchantLevel();
			}

		} else if (attacker instanceof L1NpcInstance) {
			_npc = (L1NpcInstance) attacker;
			if (target instanceof L1PcInstance) {
				_targetPc = (L1PcInstance) target;
				_calcType = NPC_PC;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				_calcType = NPC_NPC;
			}
		}
		_target = target;
		_targetId = target.getId();
		_targetX = target.getX();
		_targetY = target.getY();
	}

	/* ■■■■■■■■■■■■■■■■ 명중 판정 ■■■■■■■■■■■■■■■■ */
	public boolean calcHit() {
		// 공격 속도 체크
		/*
		 * if (_pc != null) { if (_pc.isGm()) { long currentMillis =
		 * System.currentTimeMillis(); System.out.println(currentMillis -
		 * _pc.lastSpellUseMillis); _pc.lastSpellUseMillis = currentMillis; } }
		 */
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (_pc == null || _target == null)
				return _isHit;

			/**
			 * TODO 當武器是奇鏈克時，關於絕對/卡梅拉被擊動作的條件
			 **/
			if (_weaponType2 == 17) {
				if (_target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER) /* || MJCommons.isCounterMagic(_target) */) {
					_isHit = false;
				} else {
					_isHit = true;
				}
				if (!_pc.glanceCheck(_targetX, _targetY)) {
					_isHit = false;
				}
//				System.out.println("命中判定: " + _isHit);
				return _isHit;
			}

			if (_target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
				if (_pc.hasSkillEffect(L1SkillId.ABSOLUTE_BLADE)) {
					if (_target.hasSkillEffect(ABSOLUTE_BARRIER)) {
						int probability = SkillsTable.getInstance().getTemplate(L1SkillId.ABSOLUTE_BLADE)
								.getProbabilityValue();
						int chance = _pc.getLevel() - 79;
						if (chance >= probability)
							chance = probability;
						if (chance >= _random.nextInt(100) + 1) {
							_targetPc.removeSkillEffect(ABSOLUTE_BARRIER);
							_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 14539));
							_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 14539));
						}
					}
				}
				return false;
			}

			/*
			 * if (_target.hasSkillEffect(L1SkillId.MOEBIUS)) { if (_weaponType == 20 ||
			 * _weaponType == 62) { return false; } }
			 */

			/** 2016.11.26 MJ 應用中心 LFC **/
			if (_pc instanceof L1PcInstance) {
				/* lfc 遊戲中準備狀態時 miss */
				if (_pc.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY)
					return false;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (!_pc.glanceCheck(_targetX, _targetY)/* || !_target.glanceCheck(_pc.getX(), _pc.getY()) */)
					return _isHit = false; // 攻擊者是玩家的情況下，判定障礙物
			} else {
				if (_npc.glanceCheck(_targetX, _targetY)/* || _target.glanceCheck(_npc.getX(), _npc.getY()) */)
					return _isHit = false; // 攻擊者是玩家的情況下，判定障礙物
			}

			if (_weaponType == 20 && _weaponId != 190 && _weaponId != 10000 && _weaponId != 202011 && _arrow == null) {
				_isHit = false; // 沒有箭矢的情況下 miss
			} else if (_weaponType == 62 && _sting == null) {
				_isHit = false; // 沒有針的情況下 miss
			} else if (_weaponId == 247 || _weaponId == 248 || _weaponId == 249) {
				_isHit = false; // 試煉之劍B~C 攻擊無效
			} else if (_calcType == PC_PC) {
				if (Config.ServerAdSetting.CASTLEWAR) {
					// TODO 特定座標無法攻擊
					int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
					if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
						MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
						L1Clan defense = war.getDefenseClan();
						boolean Range = false;
						// 忽略下面的座標 (守護塔的守城血盟忽略所有條件 <- 攻城血盟只能攻擊守城血盟)
						if (_pc.getClan() != defense) {
							for (L1Object l1object : L1World.getInstance().getObject()) {
								if (l1object instanceof L1TowerInstance) {
									L1TowerInstance tower = (L1TowerInstance) l1object;
									if (L1CastleLocation.checkInWarArea(castle_id, tower.getLocation())) {
										Range = (_pc.getLocation()
												.getTileLineDistance(new Point(l1object.getX(), l1object.getY())) > 10);
										if (Range && _targetPc.getClan() != defense
												&& _targetPc.getClan() != _pc.getClan()
												&& _targetPc.getRedKnightClanId() == 0) {
											_pc.sendPackets("\fY攻城血盟只能在守護塔周圍進行PK。");
											_targetPc.sendPackets("\fY攻城血盟只能在守護塔周圍進行PK。");
											_isHit = false;
											return false;
										}
									}
								}
							}
						}
					}
				}

				if (_pc.get_current_combat_id() == _targetPc.get_current_combat_team_id()
						&& _pc.get_current_combat_team_id() == _targetPc.get_current_combat_team_id()) {
					_isHit = false;
					return false;
				}

				_isHit = calcPcPcHit();

				if (!_isHit) {
					if (_pc.isPassive(MJPassiveID.MEISTER_ACCURACY.toInt())) {
						if (_calcType == PC_PC) {
							if (weapon != null) {
								MJItemSkillModel model = MJItemSkillModelLoader.getInstance()
										.getAtk(weapon.getItemId());
								if (model != null && weapon.get_item_level() == 0) {
									_pc.set_acurucy_meister((model.d_prob / 2));
									// System.out.println("命中率額外觸發機率 : " + (model.d_prob / 2));
									int dmg = (int) model.get(_pc, _targetPc, weapon, 0);
									_targetPc.receiveDamage(_pc, dmg);
									_pc.sendPackets(new S_SkillSound(_targetPc.getId(), 13418)); // 效果
									if (_pc.isGm()) {
										if (dmg > 0)
											_pc.sendPackets("\f3[" + _pc.getName() + " -> " + _targetPc.getName()
													+ "] : 傷害： " + dmg + " / HP： " + _targetPc.getCurrentHp());
									}
								}
							}
						}
					}
				}

				if (!_isHit) {
					if (_pc.isPassive(MJPassiveID.DODGE_BREAK.toInt())) {
						int probability = Config.MagicAdSetting_Lancer.DODGE_BREAK;
						if (_pc.getLevel() >= 81)
							probability += ((_pc.getLevel() - 81) / 3) * 3;

						if (probability > 30)
							probability = 30;

						if (MJRnd.isWinning(100, probability)) {
							_isHit = true;
							_pc.sendPackets(new S_SkillSound(_target.getId(), 19384));
						}
					}

				}
				if (!_isHit) {
					if (_pc.isPassive(MJPassiveID.FINE_SIGHT.toInt())) {
						int proba = 18;
						if (_pc.getLevel() >= 87) {
							proba += (_pc.getLevel() - 87);
						}
						if (proba > 30) {
							proba = 30;
						}
						if (MJRnd.isWinning(100, proba)) {
							_isHit = true;
							_pc.sendPackets(new S_SkillSound(_target.getId(), 20747));
						}
					}
				}
			} else if (_calcType == PC_NPC) {
				/** 바포방 뚫어 방지 **/
				if (_pc.바포방 != true && _pc.getX() == 32758 && _pc.getY() == 32878 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32794 && _pc.getY() == 32790 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32781 && _pc.getY() == 32881 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32782 && _pc.getY() == 32881 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32781 && _pc.getY() == 32880 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32782 && _pc.getY() == 32880 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else {
					_isHit = calcPcNpcHit();
				}

				if (!_isHit) {
					if (_pc.isPassive(MJPassiveID.MEISTER_ACCURACY.toInt())) {
						_isHit = true;
						return _isHit;
					}
				}

				if (!_isHit) {
					if (_pc.isPassive(MJPassiveID.DODGE_BREAK.toInt())) {
						int probability = Config.MagicAdSetting_Lancer.DODGE_BREAK;
						if (_pc.getLevel() >= 80)
							probability += (_pc.getLevel() - 80) / 3;

						if (probability > 100)
							probability = 100;

						if (MJRnd.isWinning(100, probability)) {
							_isHit = true;
							_pc.sendPackets(new S_SkillSound(_target.getId(), 19384));
						}
					}
				}
				if (!_isHit) {
					if (_pc.isPassive(MJPassiveID.FINE_SIGHT.toInt())) {
						int proba = 18;
						if (_pc.getLevel() >= 87) {
							proba += (_pc.getLevel() - 87);
						}
						if (proba > 30) {
							proba = 30;
						}
						if (MJRnd.isWinning(100, proba)) {
							_isHit = true;
							_pc.sendPackets(new S_SkillSound(_target.getId(), 20747));
						}
					}
				}
			}
		} else if (_calcType == NPC_PC) {
			if (_npc instanceof MJCompanionInstance) {
				_isHit = do_calc_hit_companion(_npc);
			} else {
				_isHit = calcNpcPcHit();
			}
		} else if (_calcType == NPC_NPC) {
			if ((_npc instanceof MJCompanionInstance)) {
				_isHit = do_calc_hit_companion(_npc);
			} else {
				_isHit = calcNpcNpcHit();
			}
		} else if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
			_isHit = false;
			return _isHit;
		} else if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
			_isHit = false;
			return _isHit;
		}
		return _isHit;
	}

	private boolean do_calc_hit_companion(L1Character attacker) {
		return MJAttackChain.getInstance().do_calculate_hit(this, attacker, _target);
	}

	private int do_calc_damage_companion(L1Character attacker) {
		int damage = MJAttackChain.getInstance().do_calculate_damage(this, attacker, _target);
		if (damage <= 0)
			_isHit = false;
		return damage;
	}

	/**
	 * TODO PC -> PC 命中判定
	 */
	private boolean calcPcPcHit() {
		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER) || _targetPc.hasSkillEffect(ICE_LANCE))
			return false;

		//** 根據屬性和武器的攻城 **/
		_hitRate += PchitAdd();
		if (_weaponType == 20 && _arrow != null) {
			_hitRate += _arrow.getItem().getHitModifier();
		}

		//** 計算目標PC的閃避技能 **/
		_hitRate += toPcSkillHit();

		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetPc.getAC().getAc());
		if (armor_class == null) {
			_hitRate += (_targetPc.getAC().getAc() * 0.01);
		} else {
			if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17)
				_hitRate -= (armor_class.get_to_pc_dodge());
			else
				_hitRate -= (armor_class.get_to_pc_er());
		}

		if (_pc.getLevel() < _targetPc.getLevel()) {
			_hitRate -= _targetPc.getLevel() - _pc.getLevel();
		}

		// 캐릭터 공성데이터 추가
		try {
			_hitRate += CharacterBalance.getInstance().getHit(_pc.getType(), _targetPc.getType());
		} catch (Exception e) {
			System.out.println("Character Add Damege Error");
		}

		if (_pc.getLocation().getLineDistance(_targetPc.getLocation()) >= 3 && _weaponType != 20 && _weaponType != 62
				&& !_pc.isSpearModeType()) {
			_hitRate = 0;
		}

		int _jX = _pc.getX() - _targetPc.getX();
		int _jY = _pc.getY() - _targetPc.getY();

		if (_weaponType == 24) {
			if ((_jX > 3 || _jX < -3) && (_jY > 3 || _jY < -3)) {
				_hitRate = 0;
			}
		} else if (_weaponType == 20 || _weaponType == 62) {
			if ((_jX > 15 || _jX < -15) && (_jY > 15 || _jY < -15)) {
				_hitRate = 0;
			}
		} else if (_weaponType2 == 17) { // 키링크 원거리 로 변경 220810
			if ((_jX > 5 || _jX < -5) && (_jY > 5 || _jY < -5)) {
				_hitRate = 0;
			}
		} else {
			if ((_jX > 2 || _jX < -2) && (_jY > 2 || _jY < -2)) {
				_hitRate = 0;
			}
		}

		if (_hitRate > 95) {
			_hitRate = 95;
		} else if (_hitRate < 5) {
			_hitRate = 5;
		}

		if (_weaponType != 20 && _weaponType != 62) {
			int dg = _targetPc.getDg();
			if (dg != 0) {
				if (_target.hasSkillEffect(UNCANNY_DODGE)) {
					_hitRate -= (dg * Config.MagicAdSetting_DarkElf.UncannyDodgePercent);
				} else {
					_hitRate -= (dg * Config.CharSettings.DodgePercent);
				}
			}
		} else {
			int er = _targetPc.getTotalER();
			if (er > 0) {
				_hitRate -= (er * Config.CharSettings.EvasionPercent);
			}
		}

		_hitRate += WeaponAddHitRate.getInstance().getWeaponAddHitRate(_weaponId);
		// System.out.println("Pc>Pc命中率: " + _hitRate);

		if (MJRnd.isWinning(100, _hitRate)) {
			return true;
		} else {
			_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 13418));// 이펙트
			_pc.sendPackets(new S_SkillSound(_targetPc.getId(), 13418));// 이펙트
			return false;
		}
	}

	/**
	 * TODO PC -> NPC 命中判定
	 */
	private boolean calcPcNpcHit() {
		if (_targetNpc.getNpcId() == MJRaidLoadManager.MRS_SP_VALAKAS_HPABSORB_ID
				|| _targetNpc.getNpcId() == MJRaidLoadManager.MRS_SP_VALAKAS_MPABSORB_ID) {
			if (!_pc.isValakasProduct())
				return false;
		}

		if (_targetNpc.getNpcTemplate().get_npcId() == 8500138)
			return false;

		if (_targetNpc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE)
			return false;

		/** SPR체크 **/
		int npcId = _targetNpc.getNpcTemplate().get_npcId();

		if (npcId >= 45912 && npcId <= 45915 && !_pc.hasSkillEffect(STATUS_HOLY_WATER)) {
			_hitRate = 0;
			return false;
		}
		if (npcId == 45916 && !_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
			_hitRate = 0;
			return false;
		}
		if (npcId == 45941 && !_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
			_hitRate = 0;
			return false;
		}
		if (npcId >= 46068 && npcId <= 46091 && _pc.getCurrentSpriteId() == 6035) {
			_hitRate = 0;
			return false;
		}
		if (npcId >= 46092 && npcId <= 46106 && _pc.getCurrentSpriteId() == 6034) {
			_hitRate = 0;
			return false;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) { // 오색진주
			_hitRate = 0;
			return false;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) { // 신비진주
			_hitRate = 0;
			return false;
		}

		/** 스탯 + 무기에 따른 공성 **/
		_hitRate += PchitAdd();

		if (_weaponType == 20 && _arrow != null) {
			_hitRate += _arrow.getItem().getHitModifier();
		}

		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetNpc.getAC().getAc());
		if (armor_class == null) {
			_hitRate += (_targetNpc.getAC().getAc() * 0.01);
		} else {
			if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17)
				_hitRate -= (armor_class.get_to_npc_dodge());
			else
				_hitRate -= (armor_class.get_to_npc_er());
		}

		if (_pc.getLevel() < _targetNpc.getLevel()) {
			_hitRate -= _targetNpc.getLevel() - _pc.getLevel();
		}

		int _jX = _pc.getX() - _targetNpc.getX();
		int _jY = _pc.getY() - _targetNpc.getY();

		if (_weaponType == 24) { // 창일때
			if ((_jX > 3 || _jX < -3) && (_jY > 3 || _jY < -3)) {
				_hitRate = 0;
			}
		} else if (_weaponType == 20 || _weaponType == 62) {// 활일때
			if ((_jX > 15 || _jX < -15) && (_jY > 15 || _jY < -15)) {
				_hitRate = 0;
			}
		} else if (_weaponType2 == 17) { // 키링크 원거리로 변경 2208010
			if ((_jX > 5 || _jX < -5) && (_jY > 5 || _jY < -5)) {
				_hitRate = 0;
			}
		} else {
			if ((_jX > 2 || _jX < -2) && (_jY > 2 || _jY < -2)) {
				_hitRate = 0;
			}
		}

		try {
			_hitRate += CharacterBalance.getInstance().getHit(_pc.getType(), 10);
		} catch (Exception e) {
			System.out.println("Character NpcAdd Damege Error");
		}

		if (_hitRate > 95) {
			_hitRate = 95;
		} else if (_hitRate < 5) {
			_hitRate = 5;
		}

		// TODO 激戰的競技場特化（命中100%）
		if (_targetNpc.getMapId() == 750) {
			return Config.ServerAdSetting.InfinityBattle_hit;
		}

		// TODO 克勞迪亞 - 在該地圖上無條件
		if (_targetNpc.getMapId() == 1 || _targetNpc.getMapId() == 2 || _targetNpc.getMapId() == 3
				|| _targetNpc.getMapId() == 7 || _targetNpc.getMapId() == 8 || _targetNpc.getMapId() == 9
				|| _targetNpc.getMapId() == 10 || _targetNpc.getMapId() == 11 || _targetNpc.getMapId() == 12
				|| _targetNpc.getMapId() == 12146 || _targetNpc.getMapId() == 12147
				|| _targetNpc.getMapId() >= 24 && _targetNpc.getMapId() <= 27) {
			if (_pc.getLevel() <= 79)
				return true;
		}

		_hitRate += WeaponAddHitRate.getInstance().getWeaponAddHitRate(_weaponId);
//		 System.out.println("Pc>Npc명중률 : "+_hitRate);

		if (MJRnd.isWinning(100, _hitRate)) {
			return true;
		} else {
			if (!_pc.isPassive(MJPassiveID.MEISTER_ACCURACY.toInt())) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 13418));// 이펙트
			}
			return false;
		}
	}

	/**
	 * TODO NPC -> PC 命中判定
	 */
	private boolean calcNpcPcHit() {
		/*
		 * if (_targetPc.hasSkillEffect(L1SkillId.MOEBIUS)) { int bowactid =
		 * _npc.getNpcTemplate().getBowActId(); if (bowactid == 66) { _isHit = false; }
		 * else { _isHit = true; } return _isHit; }
		 */

		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			return false;
		}

		double status = 0;
		int level = Math.max(_npc.getLevel(), 2);
		if (this._npc.getNpcTemplate().getBowActId() > 0) {
			NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.LONG_HIT, level);
			if (eInfo != null)
				status = this._npc.getAbility().getTotalDex() * eInfo.get_increase_dmg();
			else
				status = this._npc.getAbility().getTotalDex();
		} else {
			NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.SHORT_HIT, level);
			if (eInfo != null)
				status = this._npc.getAbility().getTotalStr() * eInfo.get_increase_dmg();
			else
				status = this._npc.getAbility().getTotalStr();
		}

		if (status <= 0)
			status = 1;

		_hitRate += status + _npc.getLevel();

		if (_npc instanceof L1PetInstance) { // 펫은 LV1마다 추가 명중+2
			_hitRate += _npc.getLevel() * 2;
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		_hitRate += _npc.getHitup();

		/** 타겟PC의 회피 스킬 연산 **/
		_hitRate += toPcSkillHit();
		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetPc.getAC().getAc());
		if (armor_class == null) {
			_hitRate += (_targetPc.getAC().getAc() * 0.01);
		} else {
			if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17)
				_hitRate -= (armor_class.get_to_pc_dodge());
			else
				_hitRate -= (armor_class.get_to_pc_er());
		}

		if (_npc.getLevel() < _targetPc.getLevel()) {
			_hitRate -= _targetPc.getLevel() - _npc.getLevel();
		}

		if (_hitRate > 95) {
			_hitRate = 95;
		} else if (_hitRate < 5) {
			_hitRate = 5;
		}

		if (_npc.getNpcTemplate().get_ranged() <= 2) {
			int dg = _target.getDg();
			if (dg != 0) {
				_hitRate -= (dg * Config.CharSettings.DodgePercent);
			}
		} else {
			int er = _target.getTotalER();
			if (er > 0) {
				_hitRate -= (er * Config.CharSettings.EvasionPercent);
			}
		}

		_hitRate += CharacterBalance.getInstance().getHit(10, _targetPc.getType());

		// System.out.println("Npc>Pc명중률 : "+_hitRate);

		if (MJRnd.isWinning(100, _hitRate)) {
			return true;
		} else {
			_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 13418));// 이펙트
			return false;
		}
	}

	/**
	 * TODO NPC -> NPC 命中判定
	 */
	private boolean calcNpcNpcHit() {
		if (_targetNpc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE)
			return false;

		if (_targetNpc.getNpcTemplate().get_npcId() == 8500138)
			return false;

		int target_ac = 10 - _targetNpc.getAC().getAc();
		int attacker_lvl = _npc.getNpcTemplate().get_level();

		if (target_ac != 0) {
			_hitRate = (100 / target_ac * attacker_lvl); // 被攻擊者 AC = 攻擊者 等級
		} else {
			_hitRate = 100 / 1 * attacker_lvl;
		}

		if (_npc instanceof L1PetInstance) { // 寵物每等級增加命中+2
			_hitRate += _npc.getLevel() * 2;
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		} else if (_npc instanceof L1SummonInstance) {
			if (_npc.getMaster() != null) {
				_hitRate += (_npc.getMaster().getLevel() * Config.MagicAdSetting_Wizard.SUMMON_LEVEL_ADDHIT)
						+ (_npc.getMaster().getAbility().getSp() * Config.MagicAdSetting_Wizard.SUMMON_SP_ADDHIT);
			}
		}

		if (_npc.getLevel() < _targetNpc.getLevel()) {
			_hitRate -= _targetNpc.getLevel() - _npc.getLevel();
		}

		_hitRate += CharacterBalance.getInstance().getHit(10, 10);

		if (_hitRate > 95) {
			_hitRate = 95; // 最高命中率為95%
		}
		if (_hitRate < 5) {
			_hitRate = 5; // 攻擊者等級低於5時，命中率為5%
		}

		if (MJRnd.isWinning(100, _hitRate)) {
			return true;
		} else {
			_targetNpc.sendPackets(new S_SkillSound(_targetNpc.getId(), 13418)); // 效果
			return false;
		}
	}

	public int calcDamage() {
		try {
			switch (_calcType) {
			case PC_PC:
				if (_pc == _targetPc) {
					_isHit = false;
					return 0;
				}
				_damage = calcPcPcDamage();
				_damage += _pc.get_lateral_damage();
				_damage -= _targetPc.get_lateral_reduction();
				_damage = Math.max(_damage, 1);

				if (_targetPc.getPassive(MJPassiveID.TITAN_BEAST.toInt()) != null) {
					int chance = _random.nextInt(100) + 1;
					// if (chance <= Config.TITAN_BEAST) {
					_targetPc.addTitanBeastChaList(_pc);
					if (_targetPc.getTitanBeastChaList().size() > 1) {
						_targetPc.setTitanBeast(true);
					}
					if (_targetPc.isTitanBeast()) {
						_targetPc.send_effect(20571);
						_damage *= Config.MagicAdSetting_Warrior.TITANBEAST_REDUC_PER;
					}
				}

				if (_weaponType == 20 || _weaponType == 62) {
					int Bowcritical = CalcStat.calcBowCritical(_pc.getAbility().getTotalDex()) + 1
							+ CalcStat.calcPureMissileCritical(_pc.getAbility().getDex());
					if ((Bowcritical > 0 || _pc.get_missile_critical_rate() > 0)
							&& MJRnd.isWinning(100, Bowcritical + _pc.get_missile_critical_rate())) {
						_damage *= Config.MagicAdSetting.MISSILECRITICALDAMAGERATE;
						if (_weaponType == 20) {
							_targetPc.send_effect(13392);
						} else if (_weaponType == 62) {
							_targetPc.send_effect(13398);
						}
						_isCritical = true;
					}
				} else if (_weaponType2 == 17) {
					int magiccritical = CalcStat.calcMagicCritical(_pc.getAbility().getTotalInt()) + 1;
					if ((magiccritical > 0 || _pc.get_magic_critical_rate() > 0)
							&& MJRnd.isWinning(100, magiccritical + _pc.get_magic_critical_rate())) {
						_damage *= Config.MagicAdSetting.MAGICCRITICALDAMAGERATE;
						_isCritical = true;
						_targetPc.send_effect(21124);
//						_pc.sendPackets(S_Attack.getKeylink_Critical(_pc, _target, _attackType, _isHit), false);
//						_pc.broadcastPacket(S_Attack.getKeylink_Critical(_pc, _target, _attackType, _isHit));
//						Broadcaster.broadcastPacket(_pc, S_Attack.getKeylink_Critical(_pc, _target, _attackType, _isHit));

//						_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
//						Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
					} else {
						_targetPc.send_effect(21122);
					}
				} else {
					if (_weaponType2 != 0) {
						int Dmgcritical = CalcStat.calcDmgCritical(_pc.getClassNumber(), _pc.getAbility().getTotalStr())
								+ 1 + CalcStat.calcPureMeleeCritical(_pc.getAbility().getStr());
						if ((Dmgcritical > 0 || _pc.get_melee_critical_rate() > 0) && MJRnd.isWinning(100,
								Dmgcritical + _pc.get_melee_critical_rate() + _pc.get_final_burn_critical_rate())) {
							_damage *= Config.MagicAdSetting.MELEECRITICALDAMAGERATE;
							_isCritical = true;
							// 랜서 추가
							if (!_pc.isSpearModeType()) {
								_pc.sendPackets(new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
								Broadcaster.broadcastPacket(_pc,
										new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
							} else {
								_pc.sendPackets(S_Attack.getSpear(_pc, _target, _weaponType, _isHit));
								_pc.broadcastPacket(S_Attack.getSpear(_pc, _target, _weaponType, _isHit));
							}
						}
					}
				}

				if (_pc.getRedKnightClanId() != 0)
					_pc.addRedKnightDamage(_targetPc.getRedKnightClanId() == 0 ? _damage : -_damage);

				// 타이탄 락 리뉴얼
				if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17 && _weaponType2 != 19) {
					if (_targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null) {
						int percent = (int) Math
								.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
						int chance = _random.nextInt(100) + 1;
						int titan_per = Config.MagicAdSetting_Warrior.TITANROCKPRO;
						int titan_rising_per = 0;
						int targetlevel = _targetPc.getLevel();
						int demol_per = Config.MagicAdSetting_Warrior.DEMOLITIONPRO;

						// 타이탄 락 레벨별 확률 95일때 세팅값
						if (targetlevel < 95) {
							titan_per -= (95 - targetlevel) * 2;
							if (titan_per <= 15) {
								titan_per = 15;
							}
						}
						if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN)
								&& !_targetPc.hasSkillEffect(L1SkillId.EMPIRE)
								&& !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)
								|| !_targetPc.hasSkillEffect(L1SkillId.CRUEL)
										&& !_targetPc.hasSkillEffect(L1SkillId.CRUEL)
										&& !_targetPc.hasSkillEffect(L1SkillId.PANTHERA)
										&& !_targetPc.hasSkillEffect(L1SkillId.DISINTEGRATE)) {
							if (!MJCommons.isUnbeatable(_targetPc)) {
								if (percent < 50) { // 피 50% 이하일때 라이징 확률 추가
									titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
									if (chance <= titan_rising_per) {
										if (_targetPc.getInventory().checkItem(41246, 5)) {
											if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
													&& MJRnd.isWinning(1000000,
															(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																	- demol_per))) {
												_pc.send_effect(18518);
											} else if (_targetPc != null && _weaponId == 7000262 && MJRnd
													.isWinning(1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
												_pc.send_effect(18518);
											} else {
												_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
												_damage = 0;
												_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
												_targetPc.send_effect(12555);
												_targetPc.getInventory().consumeItem(41246, 5);
											}
										} else {
											_targetPc.sendPackets(299);
										}
									}
								} else {
									if (chance <= titan_per) {
										if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
												&& MJRnd.isWinning(1000000,
														(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																- demol_per))) {
											_pc.send_effect(18518);
										} else if (_targetPc != null && _weaponId == 7000262 && MJRnd.isWinning(1000000,
												(Config.MagicAdSetting.Silenpro - demol_per))) {
											_pc.send_effect(18518);
										} else {
											_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
											_damage = 0;
											_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
											_targetPc.send_effect(12555);
										}
									}
								}
							}
						} else {
							if (_targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) != null) {
								if (!MJCommons.isUnbeatable(_targetPc)) {
									if (percent < 50) { // 피 50% 이하일때 라이징 확률 추가
										titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
										if (chance <= titan_rising_per) {
											if (_targetPc.getInventory().checkItem(41246, 5)) {
												if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
														&& MJRnd.isWinning(1000000,
																(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																		- demol_per))) {
													_pc.send_effect(18518);
												} else if (_targetPc != null && _weaponId == 7000262 && MJRnd.isWinning(
														1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
													_pc.send_effect(18518);
												} else {
													_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
													_damage = 0;
													_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
													_targetPc.send_effect(12555);
													_targetPc.getInventory().consumeItem(41246, 5);
												}
											} else {
												_targetPc.sendPackets(299);
											}
										}
									} else {
										if (chance <= titan_per) {
											if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
													&& MJRnd.isWinning(1000000,
															(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																	- demol_per))) {
												_pc.send_effect(18518);
											} else if (_targetPc != null && _weaponId == 7000262 && MJRnd
													.isWinning(1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
												_pc.send_effect(18518);
											} else {
												_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
												_damage = 0;
												_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
												_targetPc.send_effect(12555);
											}
										}
									}
								}
							}
						}
					}
				} else {
					if (_weaponType2 != 17 && _weaponType2 != 19) {
						if (_targetPc.getPassive(MJPassiveID.TITAN_BLITZ.toInt()) != null) {
							int percent = (int) Math
									.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
							int chance = _random.nextInt(100) + 1;
							int titan_per = Config.MagicAdSetting_Warrior.TITANBULLETPRO;
							int titan_rising_per = 0;
							int targetlevel = _targetPc.getLevel();
							int demol_per = Config.MagicAdSetting_Warrior.DEMOLITIONPRO;

							// 타이탄 락 레벨별 확률 95일때 세팅값
							if (targetlevel < 95) {
								titan_per -= (95 - targetlevel) * 2;
								if (titan_per <= 15) {
									titan_per = 15;
								}
							}
							if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN)
									&& !_targetPc.hasSkillEffect(L1SkillId.EMPIRE)
									&& !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)
									&& !_targetPc.hasSkillEffect(L1SkillId.CRUEL)
									&& !_targetPc.hasSkillEffect(L1SkillId.PANTHERA)
									&& !_targetPc.hasSkillEffect(L1SkillId.DISINTEGRATE)) {

								if (!MJCommons.isUnbeatable(_targetPc)) {
									if (percent < 50) { // 피 50% 이하일때 라이징 확률 추가
										titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
										if (chance <= titan_rising_per) {
											if (_targetPc.getInventory().checkItem(41246, 5)) {
												if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
														&& MJRnd.isWinning(1000000,
																(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																		- demol_per))) {
													_pc.send_effect(18518);
												} else if (_targetPc != null && _weaponId == 7000262 && MJRnd.isWinning(
														1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
													_pc.send_effect(18518);
												} else {
													_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
													_damage = 0;
													_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12557));
													_targetPc.getInventory().consumeItem(41246, 5);
													_targetPc.send_effect(12557);
												}
											} else {
												_targetPc.sendPackets(299);
											}
										}
									} else {
										if (chance <= titan_per) {
											if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
													&& MJRnd.isWinning(1000000,
															(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																	- demol_per))) {
												_pc.send_effect(18518);
											} else if (_targetPc != null && _weaponId == 7000262 && MJRnd
													.isWinning(1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
												_pc.send_effect(18518);
											} else {
												_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
												_damage = 0;
												_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12557));
												_targetPc.send_effect(12557);
											}
										}
									}
								}
							} else {
								if (_targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) != null) {
									if (!MJCommons.isUnbeatable(_targetPc)) {
										if (percent < 50) { // 피 50% 이하일때 라이징 확률 추가
											titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
											if (chance <= titan_rising_per) {
												if (_targetPc.getInventory().checkItem(41246, 5)) {
													if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
															&& MJRnd.isWinning(1000000,
																	(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																			- demol_per))) {
														_pc.send_effect(18518);
													} else if (_targetPc != null && _weaponId == 7000262
															&& MJRnd.isWinning(1000000,
																	(Config.MagicAdSetting.Silenpro - demol_per))) {
														_pc.send_effect(18518);
													} else {
														_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
														_damage = 0;
														_targetPc.sendPackets(
																new S_SkillSound(_targetPc.getId(), 12557));
														_targetPc.getInventory().consumeItem(41246, 5);
														_targetPc.send_effect(12557);
													}
												} else {
													_targetPc.sendPackets(299);
												}
											}
										} else {
											if (chance <= titan_per) {
												if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
														&& MJRnd.isWinning(1000000,
																(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																		- demol_per))) {
													_pc.send_effect(18518);
												} else if (_targetPc != null && _weaponId == 7000262 && MJRnd.isWinning(
														1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
													_pc.send_effect(18518);
												} else {
													_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
													_damage = 0;
													_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12557));
													_targetPc.send_effect(12557);
												}
											}
										}
									}
								}
							}
						}
					} else {
						if (_targetPc.getPassive(MJPassiveID.TITAN_MAGIC.toInt()) != null) {
							int percent = (int) Math
									.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
							int chance = _random.nextInt(100) + 1;
							int titan_per = Config.MagicAdSetting_Warrior.TITANMAGICPRO;
							int titan_rising_per = 0;
							int targetlevel = _targetPc.getLevel();
							int demol_per = Config.MagicAdSetting_Warrior.DEMOLITIONPRO;
							// 타이탄 락 레벨별 확률 95일때 세팅값
							if (targetlevel < 95) {
								titan_per -= (95 - targetlevel) * 2;
								if (titan_per <= 15) {
									titan_per = 15;
								}
							}

							if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN)
									&& !_targetPc.hasSkillEffect(L1SkillId.EMPIRE)
									&& !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)
									&& !_targetPc.hasSkillEffect(L1SkillId.CRUEL)
									&& !_targetPc.hasSkillEffect(L1SkillId.PANTHERA)
									&& !_targetPc.hasSkillEffect(L1SkillId.DISINTEGRATE)) {

								if (!MJCommons.isUnbeatable(_targetPc)) {
									if (percent < 50) { // 피 50% 이하일때 라이징 확률 추가
										titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
										if (chance <= titan_rising_per) {
											if (_targetPc.getInventory().checkItem(41246, 5)) {
												if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
														&& MJRnd.isWinning(1000000,
																(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																		- demol_per))) {
													_pc.send_effect(18518);
												} else if (_targetPc != null && _weaponId == 7000262 && MJRnd.isWinning(
														1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
													_pc.send_effect(18518);
												} else {
													_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
													_damage = 0;
													_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
													_targetPc.send_effect(12559);
													_targetPc.getInventory().consumeItem(41246, 5);
												}
											} else {
												_targetPc.sendPackets(299);
											}
										}
									} else {
										if (chance <= titan_per) {
											if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
													&& MJRnd.isWinning(1000000,
															(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																	- demol_per))) {
												_pc.send_effect(18518);
											} else if (_targetPc != null && _weaponId == 7000262 && MJRnd
													.isWinning(1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
												_pc.send_effect(18518);
											} else {
												_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
												_damage = 0;
												_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
												_targetPc.send_effect(12559);
											}
										}
									}
								}
							} else {
								if (_targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) != null) {
									if (!MJCommons.isUnbeatable(_targetPc)) {
										if (percent < 50) { // 피 50% 이하일때 라이징 확률 추가
											titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
											if (chance <= titan_rising_per) {
												if (_targetPc.getInventory().checkItem(41246, 5)) {
													if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
															&& MJRnd.isWinning(1000000,
																	(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																			- demol_per))) {
														_pc.send_effect(18518);
													} else if (_targetPc != null && _weaponId == 7000262
															&& MJRnd.isWinning(1000000,
																	(Config.MagicAdSetting.Silenpro - demol_per))) {
														_pc.send_effect(18518);
													} else {
														_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
														_damage = 0;
														_targetPc.sendPackets(
																new S_SkillSound(_targetPc.getId(), 12559));
														_targetPc.send_effect(12559);
														_targetPc.getInventory().consumeItem(41246, 5);
													}
												} else {
													_targetPc.sendPackets(299);
												}
											}
										} else {
											if (chance <= titan_per) {
												if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
														&& MJRnd.isWinning(1000000,
																(Config.MagicAdSetting_Fencer.PARADOXPROBABILITY
																		- demol_per))) {
													_pc.send_effect(18518);
												} else if (_targetPc != null && _weaponId == 7000262 && MJRnd.isWinning(
														1000000, (Config.MagicAdSetting.Silenpro - demol_per))) {
													_pc.send_effect(18518);
												} else {
													_pc.receiveCounterBarrierDamage(_targetPc, titanDamage());
													_damage = 0;
													_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
													_targetPc.send_effect(12559);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				break;
			case PC_NPC:
				try {
					_damage = calcPcNpcDamage();
					_damage += _pc.get_lateral_damage();
					if (_weaponType == 20 || _weaponType == 62) {
						int Bowcritical = CalcStat.calcBowCritical(_pc.getAbility().getTotalDex()) + 1
								+ CalcStat.calcPureMissileCritical(_pc.getAbility().getDex());
						if ((Bowcritical > 0 || _pc.get_missile_critical_rate() > 0)
								&& MJRnd.isWinning(100, Bowcritical + _pc.get_missile_critical_rate())) {
							_damage *= Config.MagicAdSetting.MISSILECRITICALDAMAGERATE;
							if (_weaponType == 20) {
								_targetNpc.send_effect(13392);
							} else if (_weaponType == 62) {
								_targetNpc.send_effect(13398);
							}
							_isCritical = true;
						}
					} else if (_weaponType2 == 17) {
						int magiccritical = CalcStat.calcMagicCritical(_pc.getAbility().getTotalInt()) + 1;
						if ((magiccritical > 0 || _pc.get_magic_critical_rate() > 0)
								&& MJRnd.isWinning(100, magiccritical + _pc.get_magic_critical_rate())) {
							_isCritical = true;
							_damage *= Config.MagicAdSetting.MAGICCRITICALDAMAGERATE;
							_targetNpc.send_effect(21124);
//							_pc.sendPackets(S_Attack.getKeylink_Critical(_pc, _target, _attackType, _isHit), false);
//							_pc.broadcastPacket(S_Attack.getKeylink_Critical(_pc, _target, _attackType, _isHit));
//							Broadcaster.broadcastPacket(_pc, S_Attack.getKeylink_Critical(_pc, _target, _attackType, _isHit));

//							_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
//							Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
						} else {
							_targetNpc.send_effect(21122);
						}
					} else {
						if (_weaponType2 != 0) {
							int Dmgcritical = CalcStat.calcDmgCritical(_pc.getClassNumber(),
									_pc.getAbility().getTotalStr()) + 1
									+ CalcStat.calcPureMeleeCritical(_pc.getAbility().getStr());
							if ((Dmgcritical > 0 || _pc.get_melee_critical_rate() > 0) && MJRnd.isWinning(100,
									Dmgcritical + _pc.get_melee_critical_rate() + _pc.get_final_burn_critical_rate())) {
								_isCritical = true;
								_damage *= Config.MagicAdSetting.MELEECRITICALDAMAGERATE;
								_pc.sendPackets(new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
								Broadcaster.broadcastPacket(_pc,
										new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case NPC_PC:
				if (_npc instanceof MJCompanionInstance)
					_damage = do_calc_damage_companion(_npc);
				else
					_damage = calcNpcPcDamage();
				_damage -= _targetPc.get_lateral_reduction();

				// 타이탄 락 : HP가 40% 미만일때 근접 공격을 확률적으로 반사.
				int bowactid = _npc.getNpcTemplate().getBowActId();
				if (bowactid != 66) {
					if (_targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null) {
						if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN)
								&& !_targetPc.hasSkillEffect(L1SkillId.EMPIRE)
								&& !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)
								&& !_targetPc.hasSkillEffect(L1SkillId.CRUEL)
								&& !_targetPc.hasSkillEffect(L1SkillId.PANTHERA)
								&& !_targetPc.hasSkillEffect(L1SkillId.DISINTEGRATE)) {
							int percent = (int) Math
									.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
							int chance = _random.nextInt(100) + 1;
							int titan_per = Config.MagicAdSetting_Warrior.TITANROCKPRO;
							int titan_rising_per = 0;
							int targetlevel = _targetPc.getLevel();

							// 타이탄 락 레벨별 확률 95일때 세팅값
							if (targetlevel < 95) {
								titan_per -= (95 - targetlevel) * 2;
								if (titan_per <= 15) {
									titan_per = 15;
								}
							}

							if (!MJCommons.isUnbeatable(_targetPc)) {
								if (percent < 50) {
									titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
									if (chance <= titan_rising_per) {
										if (_targetPc.getInventory().checkItem(41246, 5)) {
											_npc.receiveCounterBarrierDamage(_targetPc, titanDamage());
											_damage = 0;
											_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
											_targetPc.getInventory().consumeItem(41246, 5);
										} else {
											_targetPc.sendPackets(new S_SystemMessage("泰坦之鎖: 催化劑不足。"));
										}
									}
								} else {
									if (chance <= titan_per) {
										_npc.receiveCounterBarrierDamage(_targetPc, titanDamage());
										_damage = 0;
										_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
									}
								}
							}
						}
					}
				} else {
					// 타이탄 블릿 : HP가 40% 미만일때 원거리 공격을 확률적으로 반사.
					if (_targetPc.getPassive(MJPassiveID.TITAN_BLITZ.toInt()) != null) {
						if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN)
								&& !_targetPc.hasSkillEffect(L1SkillId.EMPIRE)
								&& !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)
								&& !_targetPc.hasSkillEffect(L1SkillId.CRUEL)
								&& !_targetPc.hasSkillEffect(L1SkillId.PANTHERA)
								&& !_targetPc.hasSkillEffect(L1SkillId.DISINTEGRATE)) {
							int percent = (int) Math
									.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
							int chance = _random.nextInt(100) + 1;
							int titan_per = Config.MagicAdSetting_Warrior.TITANROCKPRO;
							int titan_rising_per = 0;
							int targetlevel = _targetPc.getLevel();

							// 타이탄 락 레벨별 확률 95일때 세팅값
							if (targetlevel < 95) {
								titan_per -= (95 - targetlevel) * 2;
								if (titan_per <= 15) {
									titan_per = 15;
								}
							}

							if (!MJCommons.isUnbeatable(_targetPc)) {
								if (percent <= 50) {
									titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
									if (chance <= titan_rising_per) {
										if (_targetPc.getInventory().checkItem(41246, 5)) {
											_npc.receiveCounterBarrierDamage(_targetPc, titanDamage());
											_damage = 0;
											_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12557));
											_targetPc.getInventory().consumeItem(41246, 5);
										} else {
											_targetPc.sendPackets(new S_SystemMessage("泰坦子彈: 催化劑不足。"));
										}
									}
								}
							}
						}
					}
				}
				break;
			case NPC_NPC:
				if (_npc instanceof MJCompanionInstance)
					_damage = do_calc_damage_companion(_npc);
				else
					_damage = calcNpcNpcDamage();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (_calcType == PC_PC) {
			if (_targetPc == _pc) {
				_damage = 0;
			}
		}
		return _damage;
	}

	/**
	 * TODO PC -> PC 傷害計算
	 */
	public int calcPcPcDamage() {

		int weaponMaxDamage = _weaponSmall;

		int weaponDamage = 0;

		if ((_pc.getZoneType() == 1 && _targetPc.getZoneType() == 0)
				|| (_pc.getZoneType() == 1 && _targetPc.getZoneType() == -1)) {
			_isHit = false;
		}

		/** 特定區域禁止PK **/
		if (_pc.getMapId() == 612 || _pc.getMapId() == 254 || _pc.getMapId() == 1930 || _pc.getMapId() == 13005) {
			weaponDamage = 0;
			_isHit = false;
			_pc.sendPackets("\fY在這裡無法進行PK。");
//            _targetPc.sendPackets("\fY在這裡無法進行PK。");
			return (int) weaponDamage;
		}

		boolean secondWeapon = false;
		if (_weaponType == 0) { // 맨손
			weaponDamage = 0;
		} else if (_weaponType2 == 17) {
			weaponDamage = weaponMaxDamage + _weaponAddDmg;
		} else if (_pc.is전사() && _pc.isPassive(MJPassiveID.SLAYER.toInt()) && _pc.getSecondWeapon() != null) {
			int ran = _random.nextInt(100);
			if (ran < 50) {
				secondWeapon = true;
				weaponDamage = _SweaponSmall + _SweaponAddDmg;
			} else {
				weaponDamage = weaponMaxDamage + _weaponAddDmg;
			}
		} else {
			weaponDamage = weaponMaxDamage + _weaponAddDmg;
		}

		if (weaponDamage <= 0)
			weaponDamage = 1;

		int weaponTotalDamage = weaponDamage + (weapon == null ? 1 : getEnchantDmg(secondWeapon ? Sweapon : weapon))
				+ _weapon_bless_level;

		if (_weaponType == 54 && (_random.nextInt(Config.MagicAdSetting_DarkElf.DOUBLEPCPCCHANCE)
				+ 1) <= (_weaponDoubleDmgChance - weapon.get_durability()) && _pc.isDarkelf()) { // 이도류
			weaponTotalDamage *= Config.MagicAdSetting_DarkElf.DOUBLEDMG;
			_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 54, Sweapon != null));// 땁
			Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 54, Sweapon != null));// 땁
			_isCritical = true;
		}

		double dmg = weaponTotalDamage + _statusDamage;
		// double dmg = weaponTotalDamage;

		if (_weaponType2 == 17) {
			dmg = MJCommons.getKeylinkPcPcDamage(_pc, _targetPc, (int) weaponTotalDamage);
		} else if (_weaponType != 20 && _weaponType != 62) {
			dmg += _pc.getDmgup() + _pc.getDmgRate();
		} else {
			dmg += _pc.getBowDmgup() + _pc.getBowDmgRate();
		}
		// 클랜버프 pvp
		if (_pc.hasSkillEffect(CLAN_BUFF3)) {
			dmg += 1;
		}
		if (_targetPc.hasSkillEffect(CLAN_BUFF4)) {
			dmg -= 1;
		}
		if (_weaponType == 20) { // 활
			if (_arrow != null) {
				dmg += _arrow.getItem().getDmgModifier();
			} else {
				dmg += MJRnd.next(2) + 1;
			}
		} else if (_weaponType == 62) { // 암 토토 렛
			int add_dmg = _sting.getItem().getDmgSmall();
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			dmg = dmg + _random.nextInt(add_dmg) + 1;
		}

		if (_targetPc.hasSkillEffect(L1SkillId.TOP_RANKER))
			dmg -= 8;
		if (_targetPc.hasSkillEffect(L1SkillId.CLAN_BUFF4))
			dmg -= 1;
		if (_targetPc.hasSkillEffect(L1SkillId.MetisSoup))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.MetisCooking))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.MetisBlessingScroll))
			dmg -= 3;

		/** 特殊強化系統 **/
		if (_weaponType != 0) {
			switch (weapon.get_item_level()) {
				case 1: // 1階段
					WeaponLevelAttack(_pc, _target, 3740, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				case 2: // 2階段
					WeaponLevelAttack(_pc, _target, 16018, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				case 3: // 3階段
					WeaponLevelAttack(_pc, _target, 16024, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				case 4: // 4階段
					WeaponLevelAttack(_pc, _target, 4167, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				default:
					break;
			}
		}
		if (_targetPc.hasSkillEffect(L1SkillId.NATURES_TOUCH)) {
			int probability = SkillsTable.getInstance().getTemplate(L1SkillId.NATURES_TOUCH).getProbabilityValue();
			int bonus = 2;
			if (MJRnd.isWinning(100, probability)) {
				_targetPc.setCurrentHp((int) (_targetPc.getCurrentHp() + (dmg * bonus)));
				_targetPc.send_effect(18930);
			}
		}

		/** 아이템 스킬 적용 **/
		MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getAtk(_weaponId);
		if (model != null && weapon.get_item_level() == 0) {
			dmg += model.get(_pc, _targetPc, weapon, dmg);
		}

		if (_pc.getEquipSlot() != null) {
			if (_pc.getEquipSlot().getArmors() != null) {
				for (L1ItemInstance item : _pc.getEquipSlot().getArmors()) {
					model = MJItemSkillModelLoader.getInstance().getAtk(item.getItemId());
					if (model != null)
						dmg += model.get(_pc, _targetPc, item, dmg);
				}
			}
		}

		if (_weaponType == 0) { // 맨손
			dmg = 1;
		}

		if ((_weaponType != 20) && (_weaponType != 62) && (_weaponType2 != 14)) {
			dmg += L1MagicDoll.getDamageAddByDoll(_pc);
		}

		L1DollInstance doll = _pc.getMagicDoll();
		if (doll != null) {
			try {
				L1ItemInstance doll_item = _pc.getInventory().getItem(doll.getItemObjId());
				int chance = MJRnd.next(100);
				int Absorption_point = 0;
				if (doll_item != null) {
					if (doll_item.get_Doll_Bonus_Value() == 130) {
						if (chance <= Config.MagicDollInfo.MP_SMALL_CHANCE_PC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_SMALL_MIN_PC,
									Config.MagicDollInfo.MP_SMALL_MAX_PC); // 앞에가 0 뒤에가 5 면 0~1 사이로 엠이 빨린다(PC)
							if (_targetPc.getCurrentMp() > Absorption_point) {
								_pc.setCurrentMp(_pc.getCurrentMp() + Absorption_point);
								_targetPc.setCurrentMp(_targetPc.getCurrentMp() - Absorption_point);
							}
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 137 || doll_item.get_Doll_Bonus_Value() == 155) {
						if (chance <= Config.MagicDollInfo.HP_LARGE_CHANCE_PC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_LARGE_MIN_PC,
									Config.MagicDollInfo.HP_LARGE_MAX_PC); // 앞에가 0 뒤에가 5 면 0~5 사이로 피가 빨린다
							if (_targetPc.getCurrentHp() > Absorption_point) {
								_pc.setCurrentHp(_pc.getCurrentHp() + Absorption_point);
								_targetPc.setCurrentHp(_targetPc.getCurrentHp() - Absorption_point);
							}
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 138) {
						if (chance <= Config.MagicDollInfo.MP_LARGE_CHANCE_PC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_LARGE_MIN_PC,
									Config.MagicDollInfo.MP_LARGE_MAX_PC); // 앞에가 0 뒤에가 5 면 0~5 사이로 엠이 빨린다
							if (_targetPc.getCurrentMp() > Absorption_point) {
								_pc.setCurrentMp(_pc.getCurrentMp() + Absorption_point);
								_targetPc.setCurrentMp(_targetPc.getCurrentMp() - Absorption_point);
							}
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 139) { // 소울오브 프레임
						Absorption_point = MJRnd.next(100);
						if (Absorption_point < Config.MagicDollInfo.DOLL_SOULOFFLAME) { // 확률
							_pc.sendPackets(new S_SkillSound(_pc.getId(), 19264, 19));
							_pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 19264, 8));
							Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 19264));
							_pc.setSkillEffect(L1SkillId.SOUL_OF_FLAME, 8 * 1000);
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 140) { // 저지먼트
						Absorption_point = MJRnd.next(100);
						if (Absorption_point < Config.MagicDollInfo.DOLL_JUDGEMENT) { // 확률
							if (_targetPc.hasSkillEffect(L1SkillId.DOLL_JUDGEMENT))
								_targetPc.removeSkillEffect(L1SkillId.DOLL_JUDGEMENT);

							if (_pc.isCrown() || _pc.isKnight()) {
								_targetPc.addSpecialResistance(eKind.ABILITY, -15);
								_targetPc.setdoll_judgement_type(1);
							} else if (_pc.isElf() || _pc.isDarkelf()) {
								_targetPc.addSpecialResistance(eKind.SPIRIT, -15);
								_targetPc.setdoll_judgement_type(2);
							} else if (_pc.isDragonknight() || _pc.isBlackwizard()) {
								_targetPc.addSpecialResistance(eKind.DRAGON_SPELL, -15);
								_targetPc.setdoll_judgement_type(3);
							} else if (_pc.is전사() || _pc.isFencer() || _pc.isLancer()) {
								_targetPc.addSpecialResistance(eKind.FEAR, -15);
								_targetPc.setdoll_judgement_type(4);
							} else if (_pc.isWizard()) {
								_targetPc.getResistance().addMr(-15);
								_targetPc.setdoll_judgement_type(5);
								_targetPc.sendPackets(new S_SPMR(_targetPc));
							}

							_targetPc.setSkillEffect(L1SkillId.DOLL_JUDGEMENT, 8 * 1000);
							_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 18490));
							_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 18490));
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_targetPc);
							L1SkillUse.on_icons(_targetPc, JUDGEMENT, 8);
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 141) { // 디케이
						Absorption_point = MJRnd.next(100);
						if (Absorption_point < Config.MagicDollInfo.DOLL_DECAY_POTION) { // 확률
							_targetPc.setSkillEffect(DECAY_POTION, 4 * 1000); // 디케이
							_targetPc.send_other_party_effect(_pc, 2232);
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 152 || doll_item.get_Doll_Bonus_Value() == 153
							|| doll_item.get_Doll_Bonus_Value() == 154) { // HP 흡수(영웅)
						if (chance <= Config.MagicDollInfo.HP_SMALL_CHANCE_PC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_SMALL_MIN_PC,
									Config.MagicDollInfo.HP_SMALL_MAX_PC); // 앞에가 0 뒤에가 5 면 0~5 사이로 피가 빨린다
							if (_targetPc.getCurrentHp() > Absorption_point) {
								_pc.setCurrentHp(_pc.getCurrentHp() + Absorption_point);
								_targetPc.setCurrentHp(_targetPc.getCurrentHp() - Absorption_point);
							}
						}
					}
				}
			} catch (Exception e) {
				L1ItemInstance doll_item = _pc.getInventory().getItem(doll.getItemObjId());
				int Absorption_point = 0;
				System.out.println(String.format(
						String.format("[PC->PC(潛力效果錯誤) : (物品Id/Obj: %s / %s) (潛力值: %d) (潛力機率 : %d)]", doll_item.getName(),
								doll.getItemObjId(), doll_item.get_Doll_Bonus_Value(), Absorption_point));
			}
		}

		dmg += L1MagicDoll.useSkillByDoll(_pc, _targetPc);

		// 戰士技能 PC - PC
		// 崩擊：攻擊者的等級約反映在50%的傷害上
		if (_pc.getPassive(MJPassiveID.CRASH.toInt()) != null) {
			int chance = _random.nextInt(100) + 1;
			if (Config.MagicAdSetting_Warrior.CRASHFO >= chance) { // 崩擊：等級除以2的傷害
				int crashdmg = (int) (2 + (_pc.getLevel() * Config.MagicAdSetting_Warrior.CRASHDMG));
				int furydmg = 0;
				// 狂怒：崩擊造成的傷害加倍
				if (_pc.getPassive(MJPassiveID.FURY.toInt()) != null) {
					chance = _random.nextInt(100) + 1;
					if (Config.MagicAdSetting_Warrior.FURYFO >= chance) { // 狂怒機率
						furydmg += crashdmg * Config.MagicAdSetting_Warrior.FURYDMG;
						// 成功時出現兩個效果
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12489));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 12489));
					}
				}
				dmg += crashdmg + furydmg;
				// 崩擊效果按原樣處理
				_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12487));
				_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 12487));
			}
		}

		if (_pc.hasSkillEffect(L1SkillId.LordBuff)) {
			if (_pc.getClanRank() >= L1Clan.Guardian)
				dmg += 30;
		}

			/** 根據目標屬性強化計算傷害 **/
		dmg += calculateAttributeEnchantEffect();

			/** 角色的額外傷害、額外減傷、機率 **/
		if (_calcType == PC_PC) {
			if (_pc.getAddDamageRate() >= CommonUtil.random(100)) {
				dmg += _pc.getAddDamage();
			}
			if (_targetPc.getAddReductionRate() >= CommonUtil.random(100)) {
				dmg -= _targetPc.getAddReduction();
			}
		}

		/**
		 * 等級超過70後，每提升1級增加1點額外傷害
		 **/
		dmg += Math.max(0, _pc.getLevel() - Config.CharSettings.Level_Dmg) * Config.CharSettings.Level_Dmg_Count;

		if (_pc.hasSkillEffect(L1SkillId.CUBE_AVATAR)) {
			dmg += 10;
		}

		/** 真實目標 **/
		if (_targetPc.getTrueTarget() > 0) {
			dmg *= 1 + (_targetPc.getTrueTarget() / 100);
		}

		/** 新血盟禁止攻擊 **/
		if (_calcType == PC_PC) {
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
							if (!mon.isDead()) {
								int monid = UserProtectMonsterTable.getInstance()
										.getUserProtectMonsterId(mon.getNpcId());
								if (monid != 0) {
									attack_ok = true;
									break;
								}
							}
						}
					}

					if (!attack_ok) {
						if (_pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION
								|| _targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION) {
							_pc.sendPackets(new S_SystemMessage("新手玩家之間無法互相攻擊。"));
							_targetPc.sendPackets(new S_SystemMessage("新手玩家之間無法互相攻擊。"));
							return 0;
						}
					}
				} else {
					if (_pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION
							|| _targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION) {
						dmg /= 2;
						_pc.sendPackets(new S_SystemMessage("新手玩家只受到 50% 的傷害。"));
						_targetPc.sendPackets(new S_SystemMessage("新手玩家只受到 50% 的傷害。"));
					}
				}
			}
		}

		if (_pc.hasSkillEffect(BURNING_SLASH)) {
			if (_weaponType != 20) {
				dmg += 30;
				_pc.sendPackets(new S_SkillSound(_targetPc.getId(), 6591));
				_pc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 6591));
				_pc.removeSkillEffect(BURNING_SLASH);
			}
		}

		// pc-pc 트리플
		if (_pc.TripleArrow) {
			dmg *= Config.MagicAdSetting_Elf.TripleArrow_dmg_pc;
			if (_pc.isPassive(MJPassiveID.TRIPLE_BOOST.toInt())) {
				dmg *= (1 + Config.MagicAdSetting_Elf.TripleArrow_boost_dmg_pc);
			}
		}

		try {
			// PC_TO_PC
			dmg += CharacterBalance.getInstance().getDmg(_pc.getType(), _targetPc.getType());

			dmg *= CharacterBalance.getInstance().getDmgRate(_pc.getType(), _targetPc.getType());
		} catch (Exception e) {
			System.out.println("Character Enchant Per Damage for L1Attack");
		}

		// -- PVP 에 대한 대미지 판정 처리
		if (_pc.getResistance().getPVPweaponTotalDamage() > 0) {
			dmg += _pc.getResistance().getPVPweaponTotalDamage() + 1;
		}

		if (_pc.is_assassination_level2())
			dmg += 2;
		else if (_pc.is_assassination_level1())
			dmg += 1;

		if (_weaponType2 == 18) {
			L1WeaponSkill.chainSword(_pc, _targetPc);
			if (_pc.hasSkillEffect(L1SkillId.CHAINSWORD1)) {
				if (!_targetPc.hasSkillEffect(L1SkillId.CHAINSWORD2)) {
					_targetPc.getAC().addAc(-5);
					_targetPc.addDg(-10);
					_targetPc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
					_targetPc.setSkillEffect(L1SkillId.CHAINSWORD2,
							_pc.getSkillEffectTimeSec(L1SkillId.CHAINSWORD1) * 1000);
					_targetPc.sendPackets(new S_OwnCharAttrDef(_targetPc));
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_targetPc);
				}
			}
			if (_pc.isChainSwordExposed()) {
				if (_pc.hasSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME)) {
					if (_pc.getChainSwordStep() != 1) {
						_pc.setChainSwordStep(1);
						_pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
					}
				} else {
					if (_pc.getChainSwordStep() == 1) {
						if (_pc.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
							_pc.setChainSwordStep(3);
							_pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 3), true);
						} else if (_pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
							_pc.setChainSwordStep(2);
							_pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 2), true);
						}
					}
				}
			}
		}

		if (_pc.FouSlayer) {
			dmg *= Config.MagicAdSetting_DragonKnight.FouSlayer_dmg_pc;

			if (_pc.hasSkillEffect(CHAINSWORD1)) { // 龍族屠殺者
				dmg += 3.0; // 0.5
				fouslayer_brave(_pc, _targetPc);
			}
			/*
			 * if (_pc.hasSkillEffect(CHAINSWORD2)) { dmg += 1.5;
			 * _pc.killSkillEffectTimer(CHAINSWORD2); _pc.sendPackets(new
			 * S_PacketBox(S_PacketBox.SPOT, 0)); // 額外 fouslayer_brave(_pc, _targetPc); }
			 * if (_pc.hasSkillEffect(CHAINSWORD3)) { _pc.killSkillEffectTimer(CHAINSWORD3);
			 * _pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0)); // 額外
			 * fouslayer_brave(_pc, _targetPc); dmg += 3; } if
			 * (_pc.hasSkillEffect(CHAINSWORD4)) { _pc.killSkillEffectTimer(CHAINSWORD4);
			 * _pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0)); // 額外
			 * fouslayer_brave(_pc, _targetPc); dmg += 5; }
			 */

			dmg += _pc.getFouDmg();
		}

		if (_targetPc.get_pvp_defense_per() > 0) {
			dmg -= (dmg * 0.01) * _targetPc.get_pvp_defense_per();
		}

			//        System.out.println("戰鬥前 : " + dmg);
			/** 根據目標的 Buff 計算傷害 **/
		dmg = toPcBuffDmg(dmg);

		if (_pc != null && _pc.isPassive(MJPassiveID.RAMPAGE.toInt())) {
			if (_target.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL)) {
				if (MJRnd.isWinning(100, Config.MagicAdSetting_DragonKnight.RAMPAGE_P)) {
					dmg *= Config.MagicAdSetting_DragonKnight.RAMPAGE_D;
				}
			}
		}

		if (_targetPc.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL)) {
			if (_targetPc.isPassive(MJPassiveID.ARTERIAL_CIRCLE.toInt())
					&& MJRnd.isWinning(100, Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_CHANCE)) {
				dmg -= Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_REDUC;
				_targetPc.send_effect(20118, true);
			}
			if (_targetPc.getAbnormalStatusPvPReduction() != 0) {
				dmg -= _targetPc.getAbnormalStatusPvPReduction();
			}
		}

//		if (dmg > 0 && _targetPc.hasSkillEffect(MOEBIUS)) {
//			if (_weaponType == 20 || _weaponType == 62) {
//				if (_pc != null) {
//					if (MJRnd.isWinning(1000000, 1000000 - (_pc.get_Moebius_ignore() * 10000))) {
//						dmg -= (dmg * 0.30);
//					}
//				}
//			}
//		}

		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetPc.getAC().getAc());
		if (armor_class != null) {
			if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17)
				dmg -= armor_class.get_to_pc_reduction();
			else
				dmg -= armor_class.get_to_pc_long_reduction();
		}

		dmg -= L1MagicDoll.getDamageReductionByDoll(_npc, _targetPc);

		double total_reduction = 0;
		total_reduction += getReductionIgnore(
				_targetPc.getDamageReductionByArmor() + _targetPc.getDamageReduction(), _targetPc.get_pvp_defense()
						+ _targetPc.getResistance().getcalcPcDefense() + _targetPc.get_class_level_pvp_reduction(),
				_pc); // getDamageReduction 방어구에 의한 대미지 감소
		if (_targetPc.hasSkillEffect(L1SkillId.INFERNO) && _weaponType != 20 && _weaponType != 62
				&& _weaponType2 != 17) {
			L1ItemInstance target_weapon = _targetPc.getWeapon();
			if (target_weapon != null && target_weapon.getItem().getType() == 1) {
				int probability = SkillsTable.getInstance().getTemplate(L1SkillId.INFERNO).getProbabilityValue();

				if (MJRnd.isWinning(100, probability)) {
					int weapon_index = MJRnd.next(4);
					if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
							&& MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
						_pc.send_effect(18518);
					} else if (_targetPc != null && _weaponId == 7000262
							&& MJRnd.isWinning(1000000, (Config.MagicAdSetting.Silenpro))) {
						_pc.send_effect(18518);
					} else {
						_targetPc.send_effect(Config.MagicAdSetting_Elf.INFERNOEFFECTS[weapon_index]);
						int inferno_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel()
								+ _targetPc.getDmgRate() + _targetPc.getDmgup() + _targetPc.getDmgupByArmor();
						_pc.receiveDamage(_targetPc, (int) ((weapon_index + 1) * inferno_damage));
						_pc.send_action(ActionCodes.ACTION_Damage);
					}
				}
			}
		}

		if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN) && !_targetPc.hasSkillEffect(L1SkillId.EMPIRE)
				&& !_targetPc.hasSkillEffect(L1SkillId.CRUEL) && !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)) {
			if (_targetPc.hasSkillEffect(L1SkillId.HALPAS) && _weaponType != 20 && _weaponType != 62
					&& _weaponType2 != 17) {
				L1ItemInstance target_weapon = _targetPc.getWeapon();
				if (target_weapon != null && target_weapon.getItem().getType() == 18) {
					int probability = SkillsTable.getInstance().getTemplate(L1SkillId.HALPAS).getProbabilityValue();
					if (MJRnd.isWinning(100, probability)) {
						if (_targetPc != null && _pc.isPassive(MJPassiveID.PARADOX.toInt())
								&& MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
							_pc.send_effect(18518);
						} else if (_targetPc != null && _weaponId == 7000262
								&& MJRnd.isWinning(1000000, (Config.MagicAdSetting.Silenpro))) {
							_pc.send_effect(18518);
						} else {
							dmg = 0; // 반격시 대미지 0처리(무력화시킴)
							_targetPc.send_effect(18410);
							int halpas_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel()
									+ _targetPc.getDmgRate() + _targetPc.getDmgup() + _targetPc.getDmgupByArmor();
							_pc.receiveDamage(_targetPc, halpas_damage * Config.MagicAdSetting_DragonKnight.HALPASDMGX);
							_pc.send_action(ActionCodes.ACTION_Damage);
						}
					}
				}
			}
		}

		/** 버닝스피릿츠, 엘리멘탈파이어, 브레이브멘탈 1.5배 스킬이펙트 및 추타 부분 **/
		int chance41 = _random.nextInt(100) + 1;
		if (_weaponType != 0 && _weaponType != 20 && _weaponType != 62 && _weaponType2 != 17) {
			if (_pc.hasSkillEffect(ELEMENTAL_FIRE) || _pc.hasSkillEffect(L1SkillId.QUAKE)
					|| _pc.hasSkillEffect(BRAVE_MENTAL)) {
				if (chance41 <= Config.MagicAdSetting_Elf.COMBINECHANCE) {
					dmg *= 1.5;
					if (_targetPc != null) {
						S_SkillSound ss = new S_SkillSound(_targetPc.getId(), 7727);
						_targetPc.sendPackets(ss, false);
						_targetPc.broadcastPacket(ss);
					}
				}
			}
		}

		// 더블브레이크 확률 50렙부터 5렙당 1%씩 상승
		if (_pc.hasSkillEffect(DOUBLE_BRAKE) && (_weaponType == 54 || _weaponType == 58)) {
			int rate = 0;
			int ratetemp = 0;
			double doubledmg = 0;
			if (_pc.getLevel() >= 90) {
				ratetemp = (_pc.getLevel() - 88) / 2;
				rate += ratetemp * 2;
			}

			if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
				int lvl = _pc.getLevel();
				if (lvl >= 80)
					rate += _pc.getLevel() - 79;
			}
			rate += Config.MagicAdSetting_DarkElf.DOUBLEBREAKCHANCE;

			if (_pc.hasSkillEffect(L1SkillId.DOUBLE_BRAKE)) {
				if ((_random.nextInt(100) + 1) <= rate) {
					/*
					 * if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
					 * _pc.sendPackets(new S_SkillSound(_targetPc.getId(), 17223));
					 * Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetPc.getId(), 17223));
					 * } else { _pc.sendPackets(new S_SkillSound(_targetPc.getId(), 6532));
					 * Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetPc.getId(), 6532));
					 * }
					 */
					_pc.sendPackets(new S_SkillSound(_targetPc.getId(), 6532));
					Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetPc.getId(), 6532));
					doubledmg += Config.MagicAdSetting_DarkElf.DOUBLEBRAKEDMGPC;
				}
			}

			if (doubledmg <= 0) {
				doubledmg = 1;
			}

			dmg *= doubledmg;
		}

		if (_pc.isPassive(MJPassiveID.BURNING_SPIRIT_PASSIVE.toInt()) && (_weaponType == 54 || _weaponType == 58)) {
			int burningrate = 0;
			double doubledmg = 0;
			if (_pc.getLevel() >= 45) {
				burningrate += _pc.getLevel() - 45;
			}
			if (burningrate >= 10) {
				burningrate = 10;
			}
			burningrate += Config.MagicAdSetting_DarkElf.BURNINGSPIRITCHANCE;
			if ((_random.nextInt(100) + 1) <= burningrate) {
				_targetPc.send_effect(7727);
				Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetPc.getId(), 7727));
				doubledmg += Config.MagicAdSetting_DarkElf.BURNINGSPIRITNPC;
			}

			if (doubledmg <= 0) {
				doubledmg = 1;
			}
			dmg *= doubledmg;
		}

		if (_weaponType != 0 && _weaponType != 20 && _weaponType != 62) {
			if (_pc.hasSkillEffect(L1SkillId.BLOW_ATTACK)) {
				if (_pc.getEquipSlot().isWeapon(weapon)) {
					int probability = SkillsTable.getInstance().getTemplate(L1SkillId.BLOW_ATTACK)
							.getProbabilityValue();
					if (_pc.getLevel() >= 75)
						probability += (_pc.getLevel() - 75) * 1;
					if (MJRnd.isWinning(100, probability)) {
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 17223));
						dmg *= Config.MagicAdSetting_Knight.BLOWATTACKDMG;
					}
				}
			}
		}

		if (_weaponType == 20 || _weaponType == 62) {
			if (_pc.hasSkillEffect(L1SkillId.CYCLONE)) {
				int probability = SkillsTable.getInstance().getTemplate(L1SkillId.CYCLONE).getProbabilityValue();
				if (_pc.getLevel() >= 84)
					probability += ((_pc.getLevel() - 84) / 2) * 2;
				if (probability >= 20) {
					probability = 20;
				}
				if (MJRnd.isWinning(100, probability)) {
					dmg *= Config.MagicAdSetting_Elf.CYCLONEVAL;
					_targetPc.send_effect(17557);
				}
			}
		}

		if (_pc != null && _pc.isPassive(MJPassiveID.FLAME.toInt())) {
			if (_pc.getEquipSlot().isWeapon(weapon)) {
				int percent = Config.MagicAdSetting_Fencer.FLAME_PASSIVE;
				if (MJRnd.isWinning(1000000, percent)) {
					L1FlameDamage.doInfection(_pc, _targetPc, 1000,
							_pc.getLevel() * 2 / Config.MagicAdSetting_Fencer.FLAME_PASSIVE_DMG);
				}
			}
		}

		if (_pc != null && _pc.isPassive(MJPassiveID.RAGE.toInt())) {
			if (_pc.getEquipSlot().isWeapon(weapon)) {
				int percent = Config.MagicAdSetting_Fencer.RAGEPROBABILITY;
				if (MJRnd.isWinning(1000000, percent)) {
					_targetPc.send_effect(18517);
					dmg *= Config.MagicAdSetting_Fencer.RAGEDMG;
				}
			}
		}

		if (_targetPc.isPassive(MJPassiveID.GLORY_EARTH.toInt())) {
			int chance = MJRnd.next(100);
			if (chance <= Config.MagicAdSetting_Elf.GLORYEARTH_CHANCE) {
				dmg -= Config.MagicAdSetting_Elf.GLORYEARTH_DMG;
				_targetPc.send_effect(19318);
			}
		}

		if (_pc != null && _pc.isPassive(MJPassiveID.DEADLY_STRIKE.toInt())) {
			int percent = Config.MagicAdSetting_Lancer.DEADLY_STRIKE_PRO * 10000;
			if (MJRnd.isWinning(1000000, percent)) {
				dmg *= Config.MagicAdSetting_Lancer.DEADLY_STRIKE;
				_targetPc.send_effect(19367);
			}
		}

		if (_pc.isSpearModeType()) {
			if (_pc != null && _pc.isPassive(MJPassiveID.INCREASE_RANGE.toInt())) {
				int pclevel = _pc.getLevel();
				int rate = 0;
				int ratedmg = 0;
				if (pclevel < 90) {
					pclevel = 90;
				}
				if (pclevel >= 90) {
					rate = (pclevel - 90) / 2;
					ratedmg = rate * 2;
				}
				if (ratedmg >= 10) {
					ratedmg = 10;
				}
				dmg += dmg * (ratedmg + 10) / 100;
			}
		}

		if (_pc != null && _pc.hasSkillEffect(L1SkillId.BURNING_SHOT)) {
			dmg += dmg * 0.30;
		}

		if (_targetPc.getPassive(MJPassiveID.VENGEANCE.toInt()) != null) {
			if (_targetPc != null) {
				int skill_percent = 10000;
				int reduction_percent = 0;
				int HPpercent = (int) Math
						.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
				if (HPpercent < 50) {
					skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_PERCENT; // 5프로
					reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_REDUCTION; // 대미지 감소 20%
					if (MJRnd.isWinning(1000000, skill_percent)) {
						dmg -= dmg % reduction_percent;
						_targetPc.send_effect(19695);
					}
				} else {
					skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_HIT_PERCENT; // 10프로
					reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_HIT_REDUCTION; // 대미지 감소 20%
					if (MJRnd.isWinning(1000000, skill_percent)) {
						dmg -= dmg % reduction_percent;
						_targetPc.send_effect(19695);
					}
				}
			}
		}

		if (_pc.isSpearModeType()) {
			double distance = _pc.getLocation().getLineDistance(_targetPc.getLocation());
			if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_1) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_1;
			} else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_2) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_2;
			} else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_3) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_3;
			} else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_4;
			} else if (distance > Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_5;
			}
//			dmg *= Config.MagicAdSetting_Lancer.SpearMode;
			dmg += dmg * (double) (Config.MagicAdSetting_Lancer.SPEARMODE_PVP_DMG) / 100;
		}

		if (_pc.getHeading() == _targetPc.getHeading()) {
			dmg += dmg * Config.CharSettings.PVP_BOUNS;

			/*
			 * System.out.println("_pc.getHeading(): " + _pc.getHeading());
			 * System.out.println("_targetPc.getHeading(): " + _targetPc.getHeading());
			 * System.out.println("_pc.getX(): " + _pc.getX());
			 * System.out.println("_targetPc.getX(): " + _targetPc.getX());
			 * System.out.println("_pc.getY(): " + _pc.getY());
			 * System.out.println("_targetPc.getY(): " + _targetPc.getY());
			 * System.out.println("dmg: " + dmg); System.out.println("dmg * 0.2: " + dmg *
			 * 0.2);
			 */
		}

		if (_targetPc.getTomahawkHunter() == _pc) {
			int tomahawk_hp = MJRnd.next(Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MIN_HP,
					Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MAX_HP);
			if (_targetPc.getCurrentHp() > tomahawk_hp) {
				_pc.setCurrentHp(_pc.getCurrentHp() + tomahawk_hp);
				_pc.send_effect(20600);
				_targetPc.setCurrentHp(_targetPc.getCurrentHp() - tomahawk_hp);
			}
		}

		if (_weaponType == 20 || _weaponType == 62 || _weaponType == 24 && _pc.isSpearModeType()) { // 활,건틀릿(스팅),창(원거리모드)일때
			if (_targetPc.getPassive(MJPassiveID.MOEBIUS.toInt()) != null) { // 뫼비우스
				int reduc = 0;
				if (_target.getLevel() >= 85) {
					reduc = ((_target.getLevel() - 85) / 2) + 9;
					if (reduc >= 15) {
						reduc = 15;
					}
				}
				dmg *= (double) (100 - reduc) / 100D;
			}
		}

		if (_targetPc.get_reduction_per() > 0) {
			int reduction_per = _target.get_reduction_per();
			dmg -= dmg * reduction_per / 100;
		}
		if (_targetPc.get_pvp_defense_per() > 0) {
			dmg *= (double) (100 - _targetPc.get_pvp_defense_per()) / 100;
		}

		/** 盔甲破壞 */
		if (_targetPc.hasSkillEffect(ARMOR_BRAKE)) { // 盔甲破壞
			if (_targetPc.get_Armor_break_Attacker() == _pc.getId()) {
				dmg *= 1.50;
			} else {
				dmg *= 1.20;
			}
		}
		/*
		 * if (_pc.hasSkillEffect(ARMOR_BRAKE)) { // 盔甲破壞
		 *
		 * dmg *= 1.20; }
		 */
		/** Boss 盔甲破壞 **/
		if (_targetPc.hasSkillEffect(WIDE_ARMORBREAK)) {
			if (_weaponType != 20 && _weaponType != 62) {
				dmg *= 1.50;
			}
		}

//        System.out.println("(PC)傷害減免效果前 : " + (int)dmg);
		dmg = Math.max(dmg - total_reduction, 0);
//        System.out.println("(PC)傷害減免效果後 : " + (int)dmg);

		if (dmg > 0 && _targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
			if (_pc != null) {
				if (!_pc.isWizard()) {
					dmg -= dmg * _targetPc.getImmuneReduction() * _pc.get_immune_ignore() / 100;
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

//					System.out.println("후 : " + dmg);

			}
		}
		if (dmg > 0 && _targetPc.hasSkillEffect(BRAVE_UNION)) {
			dmg *= 0.95D;
			L1Party party = _targetPc.getParty();
			// int partynumber = 0;
			int reduction = 0;
			double dmgorigin = dmg;
			double dmggap = 0;

			ArrayList<L1PcInstance> partymember = new ArrayList<>();
			;
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
				dmg *= (100 - reduction) / 100D;
				dmggap = dmgorigin - dmg;
				int dmggapint = (int) dmggap;
				if (partymember.size() > 0) {
					int dmgdiv = dmggapint / partymember.size();

					if (dmgdiv > 0) {
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

		L1DollInstance targetdoll = _targetPc.getMagicDoll();
		if (targetdoll != null) {
			try {
				L1ItemInstance doll_item = _targetPc.getInventory().getItem(targetdoll.getItemObjId());
				if (doll_item != null) {
					if (doll_item.get_Doll_Bonus_Value() == 158) {
						dmg *= 0.95D;
					}
				}
			} catch (Exception e) {
				L1ItemInstance doll_item = _targetPc.getInventory().getItem(targetdoll.getItemObjId());
				int Absorption_point = 0;
				System.out.println(String.format(
						"[PC->PC(潛力效果錯誤) : (物品Id/Obj: %s / %s) (潛力值: %d) (潛力機率: %d)]", doll_item.getName(),
						targetdoll.getItemObjId(), doll_item.get_Doll_Bonus_Value(), Absorption_point));
			}
		}

		if (dmg <= 0) {
			_isHit = false;
		}
		return (int) dmg;
	}

	/**
	 * TODO 計算 PC -> NPC 的傷害
	 */
	private int calcPcNpcDamage() {
		if (_targetNpc == null || _pc == null) {
			_isHit = false;
			return 0;
		}
		int weaponMaxDamage = 0;

		if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") && _weaponSmall > 0) {
			weaponMaxDamage = _weaponSmall;
		} else if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") && _weaponLarge > 0) {
			weaponMaxDamage = _weaponLarge;
		} else if (_targetNpc instanceof L1PeopleInstance) {
			weaponMaxDamage = _weaponSmall;
		}

		int weaponDamage = 0;

		boolean secondWeapon = false;
		if (_weaponType == 0) { // 맨손
			weaponDamage = 0;
		} else if (_weaponType2 == 17) {
			weaponDamage = weaponMaxDamage + _weaponAddDmg;
		} else if (_pc.is전사() && _pc.isPassive(MJPassiveID.SLAYER.toInt()) && _pc.getSecondWeapon() != null) {
			int ran = _random.nextInt(100);
			if (ran < 50) {
				secondWeapon = true;
				if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") && _SweaponSmall > 0) {
					weaponDamage = _SweaponSmall + _SweaponAddDmg;
				} else if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") && _SweaponLarge > 0) {
					weaponDamage = _SweaponLarge + _SweaponAddDmg;
				}
			} else {
				weaponDamage = weaponMaxDamage + _weaponAddDmg;
			}
		} else {
			weaponDamage = weaponMaxDamage + _weaponAddDmg;
		}

		if (weaponDamage <= 0)
			weaponDamage = 1;

		int weaponTotalDamage = 1;

		if (weapon != null) {
			weaponTotalDamage = weaponDamage + (weapon == null ? 1 : getEnchantDmg(secondWeapon ? Sweapon : weapon))
					+ _weapon_bless_level;
		}

		weaponTotalDamage += calcMaterialBlessDmg(); // 은축복 대미지 보너스

		if (_weaponType == 54 && (_random.nextInt(Config.MagicAdSetting_DarkElf.DOUBLEPCNPCCHANCE)
				+ 1) <= (_weaponDoubleDmgChance - weapon.get_durability()) && _pc.isDarkelf()) { // 이도류
			weaponTotalDamage *= Config.MagicAdSetting_DarkElf.DOUBLEDMG;
			_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 54, Sweapon != null));// 땁
			Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 54, Sweapon != null));// 땁
			_isCritical = true;
		}

		double dmg = weaponTotalDamage + _statusDamage;

		if (_weaponType2 == 17) {
			dmg = MJCommons.getKeylinkPcNpcDamage(_pc, _targetNpc, (int) weaponTotalDamage);
		} else if (_weaponType != 20 && _weaponType != 62) {
			dmg += _pc.getDmgup() + _pc.getDmgRate();
		} else {
			dmg += _pc.getBowDmgup() + _pc.getBowDmgRate();
		}

		dmg += monsterAttributeEnchantEffect(); // 屬性傷害

		if (_weaponType == 20) { // 활
			if (_arrow != null) {
				dmg += _arrow.getItem().getDmgModifier();
			} else {
				dmg += MJRnd.next(2) + 1;
			}
			if (_targetNpc.getNpcTemplate().is_hard()) {
				dmg /= 1.5;
			}
		} else if (_weaponType == 62) { // 암 토토 렛 건틀렛
			int add_dmg = 0;
			if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large")) {
				add_dmg = _sting.getItem().getDmgLarge();
			} else if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small")) {
				add_dmg = _sting.getItem().getDmgSmall();
			}
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			dmg = dmg + _random.nextInt(add_dmg) + 1;
		}

		/** 特殊附魔系統 **/
		if (_weaponType != 0) {
			switch (weapon.get_item_level()) {
				case 1: // 第一階段
					WeaponLevelAttack(_pc, _target, 3740, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				case 2: // 第二階段
					WeaponLevelAttack(_pc, _target, 16018, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				case 3: // 第三階段
					WeaponLevelAttack(_pc, _target, 16024, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				case 4: // 第四階段
					WeaponLevelAttack(_pc, _target, 4167, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
					break;
				default:
					break;
			}
		}

		/** 아이템 스킬 적용 **/
		MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getAtk(_weaponId);
		if (model != null && weapon.get_item_level() == 0) {
			dmg += model.get(_pc, _targetNpc, weapon, dmg);
		}

		if (_pc.getEquipSlot() != null) {
			if (_pc.getEquipSlot().getArmors() != null) {
				for (L1ItemInstance item : _pc.getEquipSlot().getArmors()) {
					model = MJItemSkillModelLoader.getInstance().getAtk(item.getItemId());
					if (model != null)
						dmg += model.get(_pc, _targetNpc, item, dmg);
				}
			}
		}

		if (_weaponType == 0) { // 맨손
			dmg = (_random.nextInt(5) + 4) / 4;
		}

		if (_pc.hasSkillEffect(BURNING_SLASH)) {
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += 20;
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 6591));
				_pc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 6591));
				_pc.removeSkillEffect(BURNING_SLASH);
			}
		}

		if ((_weaponType != 20) && (_weaponType != 62) && (_weaponType2 != 14)) {
			dmg += L1MagicDoll.getDamageAddByDoll(_pc);
		}

		if (_pc.getMapId() == 750) {
			// TODO 激戰的競技場 Boss 特化 (傷害 1000%)
			if (_targetNpc.getNpcId() == 73201274 || _targetNpc.getNpcId() == 73201275
					|| _targetNpc.getNpcId() == 73201276 || _targetNpc.getNpcId() == 73201277
					|| _targetNpc.getNpcId() == 73201278 || _targetNpc.getNpcId() == 73201279) {
				dmg += Config.ServerAdSetting.InfinityBattle_boss_dmg;
			} else {
				// TODO 激戰的競技場特化 (傷害 300%)
				dmg += Config.ServerAdSetting.InfinityBattle_dmg;
			}
		}

		L1DollInstance doll = _pc.getMagicDoll();
		if (doll != null) {
			try {
				L1ItemInstance doll_item = _pc.getInventory().getItem(doll.getItemObjId());
				int chance = MJRnd.next(100);
				int Absorption_point = 0;
				if (doll_item != null) {
					if (doll_item.get_Doll_Bonus_Value() == 130) {// 엠흡수 소량
						if (chance <= Config.MagicDollInfo.MP_SMALL_CHANCE_NPC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_SMALL_MIN_NPC,
									Config.MagicDollInfo.MP_SMALL_MAX_NPC);
							if (_targetNpc.getCurrentMp() > Absorption_point) {
								_pc.setCurrentMp(_pc.getCurrentMp() + Absorption_point);
								_targetNpc.setCurrentMp(_targetNpc.getCurrentMp() - Absorption_point);
							}
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 137 || doll_item.get_Doll_Bonus_Value() == 155) {
						if (chance <= Config.MagicDollInfo.HP_LARGE_CHANCE_NPC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_LARGE_MIN_NPC,
									Config.MagicDollInfo.HP_LARGE_MAX_NPC);
							if (_targetNpc.getCurrentHp() > Absorption_point) {
								_pc.setCurrentHp(_pc.getCurrentHp() + Absorption_point);
								_targetNpc.setCurrentHp(_targetNpc.getCurrentHp() - Absorption_point);
							}
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 138 || doll_item.get_Doll_Bonus_Value() == 156) {
						if (chance <= Config.MagicDollInfo.MP_LARGE_CHANCE_NPC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_LARGE_MIN_NPC,
									Config.MagicDollInfo.MP_LARGE_MAX_NPC);
							if (_targetNpc.getCurrentMp() > Absorption_point) {
								_pc.setCurrentMp(_pc.getCurrentMp() + Absorption_point);
								_targetNpc.setCurrentMp(_targetNpc.getCurrentMp() - Absorption_point);
							}
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 139) { // 소울오브 프레임
						Absorption_point = MJRnd.next(100);
						if (Absorption_point < Config.MagicDollInfo.DOLL_SOULOFFLAME) {
							if (_pc.hasSkillEffect(L1SkillId.SOUL_OF_FLAME))
								_pc.removeSkillEffect(L1SkillId.SOUL_OF_FLAME);
							_pc.sendPackets(new S_SkillSound(_pc.getId(), 19264, 19));
							_pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 19264, 8));
							Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 19264));
							_pc.setSkillEffect(L1SkillId.SOUL_OF_FLAME, 8 * 1000);
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 140) { // 저지먼트
						Absorption_point = MJRnd.next(100);
						if (Absorption_point < Config.MagicDollInfo.DOLL_JUDGEMENT) {
							if (_targetNpc.hasSkillEffect(L1SkillId.JUDGEMENT))
								_targetNpc.removeSkillEffect(L1SkillId.JUDGEMENT);

							_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 18490));
							_targetNpc.setSkillEffect(L1SkillId.JUDGEMENT, 8 * 1000);
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 141) { // 디케이
						Absorption_point = MJRnd.next(100);
						if (Absorption_point < Config.MagicDollInfo.DOLL_DECAY_POTION) {
							if (_targetNpc.hasSkillEffect(L1SkillId.DECAY_POTION))
								_targetNpc.removeSkillEffect(L1SkillId.DECAY_POTION);

							_targetNpc.setSkillEffect(DECAY_POTION, 4 * 1000); // 디케이
							_targetNpc.sendPackets(new S_SkillSound(_targetNpc.getId(), 2232));
							Broadcaster.broadcastPacket(_targetNpc, new S_SkillSound(_targetNpc.getId(), 2232));
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 147) {
						if (chance <= Config.MagicDollInfo.MP_SMALL_CHANCE_NPC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_SMALL_MIN_NPC,
									Config.MagicDollInfo.MP_SMALL_MAX_NPC);
							if (_targetNpc.getCurrentMp() > Absorption_point) {
								_pc.setCurrentMp(_pc.getCurrentMp() + Absorption_point);
								_targetNpc.setCurrentMp(_targetNpc.getCurrentMp() - Absorption_point);
							}
						}
					} else if (doll_item.get_Doll_Bonus_Value() == 152 || doll_item.get_Doll_Bonus_Value() == 153
							|| doll_item.get_Doll_Bonus_Value() == 154) {
						if (chance <= Config.MagicDollInfo.HP_SMALL_CHANCE_NPC) {
							Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_SMALL_MIN_NPC,
									Config.MagicDollInfo.HP_SMALL_MAX_NPC);
							if (_targetNpc.getCurrentHp() > Absorption_point) {
								_pc.setCurrentHp(_pc.getCurrentHp() + Absorption_point);
								_targetNpc.setCurrentHp(_targetNpc.getCurrentHp() - Absorption_point);
							}
						}
					}

				}
			} catch (Exception e) {
				L1ItemInstance doll_item = _pc.getInventory().getItem(doll.getItemObjId());
				int Absorption_point = 0;
				System.out.println(String.format(
						"[PC->NPC(潛力效果錯誤) : (物品Id/物品對象: %s / %s) (潛力值: %d) (潛力機率: %d)]", doll_item.getName(),
						doll.getItemObjId(), doll_item.get_Doll_Bonus_Value(), Absorption_point));
			}
		}

		dmg += L1MagicDoll.useSkillByDoll(_pc, _targetNpc);

		if (_pc != null && _pc.isPassive(MJPassiveID.RAMPAGE.toInt())) {
			if (_target.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL)) {
				if (MJRnd.isWinning(100, Config.MagicAdSetting_DragonKnight.RAMPAGE_P)) {
					dmg *= Config.MagicAdSetting_DragonKnight.RAMPAGE_D;
				}
			}
		}

		// 戰士技能 PC - NPC
		// 衝擊：攻擊者的等級反映在大約 50% 的傷害上。
		if (_pc.getPassive(MJPassiveID.CRASH.toInt()) != null) {
			int chance = _random.nextInt(100) + 1;
			if (Config.MagicAdSetting_Warrior.CRASHFO >= chance) { // 衝擊機率
				int crashdmg = (int) (2 + (_pc.getLevel() * Config.MagicAdSetting_Warrior.CRASHDMG));
				int furydmg = 0;
				// 狂怒：從衝擊中得到的傷害加倍。
				if (_pc.getPassive(MJPassiveID.FURY.toInt()) != null) {
					chance = _random.nextInt(100) + 1;
					if (Config.MagicAdSetting_Warrior.FURYFO >= chance) { // 狂怒機率
						furydmg += crashdmg * Config.MagicAdSetting_Warrior.FURYDMG;
						// 成功時發送兩個效果
						_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 12489));
					}
				}
				dmg += crashdmg + furydmg;
				// 크래쉬는 크래쉬 이팩트 그대로 처리.
				_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 12487));
			}
		}

		if (_weaponType2 == 18)
			L1WeaponSkill.chainSword(_pc, _target); // 鏈劍

		if (_pc.FouSlayer) {
			dmg *= Config.MagicAdSetting_DragonKnight.FouSlayer_dmg_npc;

			if (_pc.hasSkillEffect(CHAINSWORD1)) { // 傅斯萊爾
				dmg += 3.0;
				// 0.5
				/*
				 * _pc.killSkillEffectTimer(CHAINSWORD1);
				 * _pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0)); // 新增
				 */
				fouslayer_brave(_pc, _targetNpc); // 傅斯萊爾勇敢
			}

			dmg += _pc.getFouDmg();
		}

		if (_pc.hasSkillEffect(L1SkillId.CUBE_AVATAR)) {
			dmg += 10;
		}

		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetNpc.getAC().getAc());
		if (armor_class != null) {
			if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17)
				dmg -= armor_class.get_to_npc_reduction();
			else
				dmg -= armor_class.get_to_npc_long_reduction();
		}

		// pc-npc 트리플
		if (_pc.TripleArrow) {
			dmg *= Config.MagicAdSetting_Elf.TripleArrow_dmg_npc;
			if (_pc.isPassive(MJPassiveID.TRIPLE_BOOST.toInt())) {
				dmg *= (1 + Config.MagicAdSetting_Elf.TripleArrow_boost_dmg_npc);
			}
		}

		try {
			dmg += CharacterBalance.getInstance().getDmg(_pc.getType(), 10);
			dmg *= CharacterBalance.getInstance().getDmgRate(_pc.getType(), 10);
		} catch (Exception e) {
			System.out.println("Character NpcAdd Damege Error");
		}

		L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(_pc.getMapId());

		if (sm != null && _pc.getWeapon() != null && _pc.getWeapon().getItem().getType() != 7) {
			dmg -= sm.getDmgReduction();
			if (dmg <= 0)
				dmg = 1;
		}

		dmg -= calcNpcDamageReduction();

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
		if (castleId > 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		// TODO 寵物傷害
		if (!isNowWar) {
			if (_targetNpc instanceof L1PetInstance) {
				dmg /= 8;
			}
			// TODO 召喚怪物傷害
			if (_targetNpc instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) _targetNpc;
				if (summon.isExsistMaster()) {
					dmg /= 5; // 8
				}
			}
		}

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(PHANTASM)) {
			_targetNpc.removeSkillEffect(PHANTASM);
		}

		/** 버닝스피릿츠, 엘리멘탈파이어, 브레이브멘탈 1.5배 스킬이펙트 및 추타 부분 **/
		int chance41 = _random.nextInt(100) + 1;
		if (_weaponType != 0 && _weaponType != 20 && _weaponType != 62 && _weaponType2 != 17) {
			if (_pc.hasSkillEffect(ELEMENTAL_FIRE) || _pc.hasSkillEffect(L1SkillId.QUAKE)
					|| _pc.hasSkillEffect(BRAVE_MENTAL)) {
				if (chance41 <= Config.MagicAdSetting_Elf.COMBINECHANCE) {
					dmg *= 1.5;
					_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 7727));
				}
			}
		}

		// 더블브레이크 확률 50렙부터 5렙당 1%씩 상승
		if (_pc.hasSkillEffect(DOUBLE_BRAKE) && (_weaponType == 54 || _weaponType == 58)) {
			int rate = 0;
			int ratetemp = 0;
			double doubledmg = 0;
			if (_pc.getLevel() >= 90) {
				ratetemp = (_pc.getLevel() - 88) / 2;
				rate += ratetemp * 2;
			}

			if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
				int lvl = _pc.getLevel();
				if (lvl >= 80)
					rate += _pc.getLevel() - 79;
			}
			rate += Config.MagicAdSetting_DarkElf.DOUBLEBREAKCHANCE;

			if (_pc.hasSkillEffect(L1SkillId.DOUBLE_BRAKE)) {
				if ((_random.nextInt(100) + 1) <= rate) {
					/*
					 * if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
					 * _pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 17223));
					 * Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetNpc.getId(),
					 * 17223)); } else { _pc.sendPackets(new S_SkillSound(_targetNpc.getId(),
					 * 6532)); Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetNpc.getId(),
					 * 6532)); }
					 */
					_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 6532));
					Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetNpc.getId(), 6532));
					doubledmg += Config.MagicAdSetting_DarkElf.DOUBLEBRAKEDMGNPC;
				}
			}

			if (doubledmg <= 0) {
				doubledmg = 1;
			}

			dmg *= doubledmg;
		}
		if (_pc.isPassive(MJPassiveID.BURNING_SPIRIT_PASSIVE.toInt()) && (_weaponType == 54 || _weaponType == 58)) {
			int burningrate = 0;
			double doubledmg = 0;
			if (_pc.getLevel() >= 45) {
				burningrate += _pc.getLevel() - 45;
			}
			if (burningrate >= 10) {
				burningrate = 10;
			}
			burningrate += Config.MagicAdSetting_DarkElf.BURNINGSPIRITCHANCE;

			if (_pc.isPassive(MJPassiveID.BURNING_SPIRIT_PASSIVE.toInt())) {
				if ((_random.nextInt(100) + 1) <= burningrate) {
					_targetNpc.send_effect(7727);
					Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetNpc.getId(), 7727));
					doubledmg += Config.MagicAdSetting_DarkElf.BURNINGSPIRITNPC;
				}
			}
			if (doubledmg <= 0) {
				doubledmg = 1;
			}
			dmg *= doubledmg;
		}

		if (_weaponType != 0 && _weaponType != 20 && _weaponType != 62) {
			if (_pc.hasSkillEffect(L1SkillId.BLOW_ATTACK)) {
				if (_pc.getEquipSlot().isWeapon(weapon)) {
					int probability = SkillsTable.getInstance().getTemplate(L1SkillId.BLOW_ATTACK)
							.getProbabilityValue();
					if (_pc.getLevel() >= 75)
						probability += (_pc.getLevel() - 75) * 1;
					if (MJRnd.isWinning(100, probability)) {
						_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 17223));
						dmg *= Config.MagicAdSetting_Knight.BLOWATTACKDMG;
					}
				}
			}
		}

		if (_weaponType == 20 || _weaponType == 62) {
			if (_pc.hasSkillEffect(L1SkillId.CYCLONE)) {
				int probability = SkillsTable.getInstance().getTemplate(L1SkillId.CYCLONE).getProbabilityValue();
				if (_pc.getLevel() >= 85)
					probability += (_pc.getLevel() - 85) * 1;
				if (MJRnd.isWinning(100, probability)) {
					dmg *= Config.MagicAdSetting_Elf.CYCLONEVAL;
					_targetNpc.send_effect(17557);
				}
			}
		}

		if (_pc != null && _pc.isPassive(MJPassiveID.FLAME.toInt())) {
			if (_pc.getEquipSlot().isWeapon(weapon)) {
				int percent = Config.MagicAdSetting_Fencer.FLAME_PASSIVE;
				if (MJRnd.isWinning(1000000, percent)) {
					L1FlameDamage.doInfection(_pc, _targetNpc, 1000,
							_pc.getLevel() * 2 / Config.MagicAdSetting_Fencer.FLAME_PASSIVE_DMG);
				}
			}
		}

		if (_pc != null && _pc.isPassive(MJPassiveID.RAGE.toInt())) {
			if (_pc.getEquipSlot().isWeapon(weapon)) {
				int percent = Config.MagicAdSetting_Fencer.RAGEPROBABILITY;
				if (MJRnd.isWinning(1000000, percent)) {
					_targetNpc.send_effect(18517);
					dmg *= Config.MagicAdSetting_Fencer.RAGEDMG;
				}
			}
		}

		if (_pc != null && _pc.isPassive(MJPassiveID.DEADLY_STRIKE.toInt())) {
			int percent = Config.MagicAdSetting_Lancer.DEADLY_STRIKE_PRO * 10000;
			if (MJRnd.isWinning(1000000, percent)) {
				dmg *= Config.MagicAdSetting_Lancer.DEADLY_STRIKE;
				_targetNpc.send_effect(19367);
			}
		}

		if (_pc.isSpearModeType()) {
			if (_pc != null && _pc.isPassive(MJPassiveID.INCREASE_RANGE.toInt())) {
				int pclevel = _pc.getLevel();
				int rate = 0;
				int ratedmg = 0;
				if (pclevel < 90) {
					pclevel = 90;
				}
				if (pclevel >= 90) {
					rate = (pclevel - 90) / 2;
					ratedmg = rate * 2;
				}
				if (ratedmg >= 10) {
					ratedmg = 10;
				}
				dmg += dmg * (ratedmg + 10) / 100;
			}

		}

		if (_pc != null && _pc.hasSkillEffect(L1SkillId.BURNING_SHOT)) {
			dmg += dmg * 0.30;
		}

		boolean isCounterBarrier = false;
		L1Magic magic = null;
		if (_targetNpc.hasSkillEffect(L1SkillId.BOSS_COUNTER_BARRIER) && dmg > 0) {
			magic = new L1Magic(_targetNpc, _pc);
			boolean isProbability = magic.calcProbabilityMagic(L1SkillId.COUNTER_BARRIER);
			if (isProbability) {
				isCounterBarrier = true;
			}
		}
		if (isCounterBarrier) {
			NpcactionCounterBarrier(_target, _pc);
			commitBossCounterBarrier(_target, _pc);
		}

		if (_pc.isSpearModeType()) {
			double distance = _pc.getLocation().getLineDistance(_targetNpc.getLocation());
			if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_1) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_1;
			} else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_2) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_2;
			} else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_3) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_3;
			} else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_4;
			} else if (distance > Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
				dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_5;
			}
		}

		if (dmg <= 0) {
			_isHit = false;
		}
		/** 아머브레이크 */
		if (_targetNpc.hasSkillEffect(ARMOR_BRAKE)) { // 아머브레이크
			if (_targetNpc.get_Armor_break_Attacker() == _pc.getId()) {
				dmg *= 1.50;
			} else {
				dmg *= 1.20;
			}
		}
		if (_targetNpc.getTomahawkHunter() == _pc) {
			int tomahawk_hp = MJRnd.next(Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MIN_HP,
					Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MAX_HP);
			if (_targetNpc.getCurrentHp() > tomahawk_hp) {
				_pc.setCurrentHp(_pc.getCurrentHp() + tomahawk_hp);
				_pc.send_effect(20600);
				_targetNpc.setCurrentHp(_targetNpc.getCurrentHp() - tomahawk_hp);
			}
		}

		return (int) dmg;
	}

	/**
	 * TODO NPC -> PC 대미지 산출
	 */
	private int calcNpcPcDamage() {
		if (_npc == null || _targetPc == null)
			return 0;

		int lvl = this._npc.getLevel();
		double dmg = 0.0D;
		double status = 0;
		int level = Math.max(_npc.getLevel(), 2);
		if (this._npc.getNpcTemplate().getBowActId() > 0) {
			NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.LONG_DMG, level);
			if (eInfo != null)
				status = this._npc.getAbility().getTotalDex() * eInfo.get_increase_dmg();
			else
				status = this._npc.getAbility().getTotalDex();
		} else {
			NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.SHORT_DMG, level);
			if (eInfo != null)
				status = this._npc.getAbility().getTotalStr() * eInfo.get_increase_dmg();
			else
				status = this._npc.getAbility().getTotalStr();
		}

		if (status <= 0)
			status = 1;

		if (_npc instanceof L1PetInstance) {
			dmg += (lvl / 15); // 펫은 LV16마다 추가 타격
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		}
		dmg += _npc.getDmgup() + (status - status / 2) + _random.nextInt((int) status) + 1;

		if (isUndeadDamage()) {
			dmg *= 1.2;
		}

		L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(_npc.getMapId());
		if (sm != null) {
			dmg *= sm.getDmgRate();
		}

		// TODO 世界整體怪物傷害相關
		dmg = dmg * getLeverage() / 15; // 提高怪物物理傷害會變弱。

		if (_npc.isWeaponBreaked()) { // NPC處於武器破損狀態。
			dmg *= 0.5;
		}

		if (_targetPc.hasSkillEffect(L1SkillId.MetisSoup))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.MetisCooking))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.MetisBlessingScroll))
			dmg -= 3;

		try {
			dmg += CharacterBalance.getInstance().getDmg(10, _targetPc.getType());

			dmg *= CharacterBalance.getInstance().getDmgRate(10, _targetPc.getType());
		} catch (Exception e) {
			System.out.println("Character NpcAdd Reduction Error");
		}

		// 애완동물, 사몬으로부터 플레이어에 공격
		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
		if (castleId > 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		// TODO 펫 대미지
		if (!isNowWar) {
			if (_npc instanceof L1PetInstance) {
				dmg /= 8;
			}
			// TODO 서먼몬스터 대미지
			if (_npc instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) _npc;
				if (summon.isExsistMaster()) {
					dmg /= 5; // 8
				}
			}
		}

		addNpcPoisonAttack(_npc, _targetPc);

		if (_npc instanceof L1PetInstance || _npc instanceof L1SummonInstance || _npc instanceof MJCompanionInstance) {
			if (_targetPc.getZoneType() == 1) {
				_isHit = false;
			}
		}

		/** Boss 盔甲破壞 */
		if (_targetPc.hasSkillEffect(WIDE_ARMORBREAK)) {
			if (_weaponType != 20 && _weaponType != 62) {
				dmg *= 1.58;
			}
		}

		/** 根據目標 Buff 計算傷害 **/
		dmg = toPcBuffDmg(dmg);

		/*
		 * if (dmg > 0 && _targetPc.hasSkillEffect(MOEBIUS)) { // 如果與目標的距離超過 2 則為遠距攻擊
		 * int bowActId = 0; boolean isLongRange =
		 * (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) > 1);
		 * bowActId = _npc.getNpcTemplate().getBowActId();
		 *
		 * if (isLongRange && bowActId > 0) { if (_targetPc != null) { dmg -= (dmg *
		 * 0.30); } } }
		 */

		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetPc.getAC().getAc());
		if (armor_class != null) {
			if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17)
				dmg -= armor_class.get_to_pc_reduction();
			else
				dmg -= armor_class.get_to_pc_long_reduction();
		}

		dmg -= L1MagicDoll.getDamageReductionByDoll(_npc, _targetPc);

		double total_reduction = 0;
		total_reduction += getReductionIgnore(_targetPc.getDamageReductionByArmor() + _targetPc.getDamageReduction(), 0,
				_npc); // getDamageReduction 방어구에 의한 대미지 감소
		if (_targetPc.hasSkillEffect(L1SkillId.INFERNO) && _npc.getNpcTemplate().get_ranged() <= 1) {
			L1ItemInstance target_weapon = _targetPc.getWeapon();
			if (target_weapon != null && target_weapon.getItem().getType() == 1) {
				int probability = SkillsTable.getInstance().getTemplate(L1SkillId.INFERNO).getProbabilityValue();
				if (MJRnd.isWinning(100, probability)) {
					int weapon_index = MJRnd.next(4);
					_targetPc.send_effect(Config.MagicAdSetting_Elf.INFERNOEFFECTS[weapon_index]);
					int inferno_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel()
							+ _targetPc.getDmgRate() + _targetPc.getDmgup() + _targetPc.getDmgupByArmor();
					_npc.receiveDamage(_targetPc, (int) ((weapon_index + 1) * inferno_damage));
					_npc.send_action(ActionCodes.ACTION_Damage);
				}
			}
		}

		if (!_targetPc.hasSkillEffect(L1SkillId.SHOCK_STUN) && !_targetPc.hasSkillEffect(L1SkillId.EMPIRE)
				&& _targetPc.hasSkillEffect(L1SkillId.CRUEL) && !_targetPc.hasSkillEffect(L1SkillId.BONE_BREAK)) {
			if (_targetPc.hasSkillEffect(L1SkillId.HALPAS) && _npc.getNpcTemplate().get_ranged() <= 1) {
				L1ItemInstance target_weapon = _targetPc.getWeapon();
				if (target_weapon != null && target_weapon.getItem().getType() == 18) {
					int probability = SkillsTable.getInstance().getTemplate(L1SkillId.HALPAS).getProbabilityValue();
					if (MJRnd.isWinning(100, probability)) {
						dmg = 0; // 반격시 대미지 0처리(무력화시킴)
						_targetPc.send_effect(18410);
						int halpas_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel()
								+ _targetPc.getDmgRate() + _targetPc.getDmgup() + _targetPc.getDmgupByArmor();
						_npc.receiveDamage(_targetPc, halpas_damage * Config.MagicAdSetting_DragonKnight.HALPASDMGX);
						_npc.send_action(ActionCodes.ACTION_Damage);
					}
				}
			}
		}

		if (_targetPc.hasSkillEffect(L1SkillId.NATURES_TOUCH)) {
			int probability = SkillsTable.getInstance().getTemplate(L1SkillId.NATURES_TOUCH).getProbabilityValue();
			int bonus = 2;
			if (MJRnd.isWinning(100, probability)) {
				_targetPc.setCurrentHp((int) (_targetPc.getCurrentHp() + (dmg * bonus)));
				_targetPc.send_effect(18930);
			}
		}

		/**
		 * TODO 龍 NPC ID 添加 (減少對龍的傷害)
		 **/
		if (_npc.getNpcId() == 14212114 || _npc.getNpcId() == 14212134 || _npc.getNpcId() == 14212120
				|| _npc.getNpcId() == 14212144 || _npc.getNpcId() == 7310154 || _npc.getNpcId() == 7310148
				|| _npc.getNpcId() == 7310160 || _npc.getNpcId() == 45684 || _npc.getNpcId() == 73201240
				|| _npc.getNpcId() == 73201233) {
			if (_targetPc.hasSkillEffect(L1SkillId.DRAGON_HALPAS)) {
				if (_targetPc.getMagicDoll() != null) {
					dmg -= (dmg * (_targetPc.getMagicDoll().getDoll().getEffect().getDragonDmgDecrease() * 0.01));
				} else {
					dmg -= (dmg * 0.10);
				}
			}
		}
		if (_targetPc.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL)) {
			if (_targetPc.isPassive(MJPassiveID.ARTERIAL_CIRCLE.toInt())
					&& MJRnd.isWinning(100, Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_CHANCE)) {
				dmg -= Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_REDUC;
				_targetPc.send_effect(20118, true);
			}

		}

		if (_targetPc.getPassive(MJPassiveID.VENGEANCE.toInt()) != null) {
			if (_targetPc != null) {
				int skill_percent = 10000;
				int reduction_percent = 0;
				int HPpercent = (int) Math
						.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
				if (HPpercent < 50) {
					skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_PERCENT; // 5프로
					reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_REDUCTION; // 대미지 감소 20%
					if (MJRnd.isWinning(1000000, skill_percent)) {
						dmg -= dmg % reduction_percent;
						_targetPc.send_effect(19695);
					}
				} else {
					skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_HIT_PERCENT; // 10프로
					reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_HIT_REDUCTION; // 대미지 감소 20%
					if (MJRnd.isWinning(1000000, skill_percent)) {
						dmg -= dmg % reduction_percent;
						_targetPc.send_effect(19695);
					}
				}
			}
		}
//        System.out.println("(NPC)傷害減少相關效果之前 : " + (int)dmg);
		dmg = Math.max(dmg - total_reduction, 0);
//        System.out.println("(NPC)傷害減少相關效果之後 : " + (int)dmg);
		if (_weaponType == 20 || _weaponType == 62 || (_weaponType == 24 && _pc.isSpearModeType())) { // 弓, 鐵手套(刺針), 矛(遠程模式)時
			if (_targetPc.getPassive(MJPassiveID.MOEBIUS.toInt()) != null) { // 莫比烏斯
				int reduc = 0;
				if (_target.getLevel() >= 85) {
					reduc = ((_target.getLevel() - 85) / 2) + 9;
					if (reduc >= 15) {
						reduc = 15;
					}
				}
				dmg *= (double) (100 - reduc) / 100;
			}
		}

		if (_targetPc.get_reduction_per() > 0) {
			int reduction_per = _target.get_reduction_per();
			dmg -= dmg * reduction_per / 100;
		}

		if (dmg > 0 && _targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
			if (_targetPc != null) {
				if (!_targetPc.isWizard()) {
					dmg -= (dmg * _targetPc.getImmuneReduction());
				} else {
					dmg *= 0.5D;
				}
			}
		}

		L1DollInstance targetdoll = _targetPc.getMagicDoll();
		if (targetdoll != null) {
			try {
				L1ItemInstance doll_item = _targetPc.getInventory().getItem(targetdoll.getItemObjId());
				if (doll_item != null) {
					if (doll_item.get_Doll_Bonus_Value() == 158) {
						dmg *= 0.95D;
					}
				}
			} catch (Exception e) {
				L1ItemInstance doll_item = _targetPc.getInventory().getItem(targetdoll.getItemObjId());
				int Absorption_point = 0;
				System.out.println(String.format(
						"[PC->PC(潛能效果錯誤) : (物品Id/Obj: %s / %s) (潛能值: %d) (潛能概率: %d)]", doll_item.getName(),
						targetdoll.getItemObjId(), doll_item.get_Doll_Bonus_Value(), Absorption_point));
			}
		}

		if (dmg <= 0) {
			_isHit = false;
		}

		return (int) dmg;
	}

	/**
	 * TODO NPC -> NPC 傷害計算
	 */
	private int calcNpcNpcDamage() {
		if (_targetNpc == null || _npc == null)
			return 0;

		int lvl = _npc.getLevel();
		double dmg = 0;
		double status = 0;
		int level = Math.max(_npc.getLevel(), 2);
		if (this._npc.getNpcTemplate().getBowActId() > 0) {
			NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.LONG_DMG, level);
			if (eInfo != null)
				status = this._npc.getAbility().getTotalDex() * eInfo.get_increase_dmg();
			else
				status = this._npc.getAbility().getTotalDex();
		} else {
			NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.SHORT_DMG, level);
			if (eInfo != null)
				status = this._npc.getAbility().getTotalStr() * eInfo.get_increase_dmg();
			else
				status = this._npc.getAbility().getTotalStr();
		}

		if (status <= 0)
			status = 1;

		if (_npc.getNpcId() >= 7320138 && _npc.getNpcId() <= 7320147) {
			if (_npc.getCurrentHp() < _npc.getMaxHp()) {
				int adddmg = (_npc.getMaxHp() - _npc.getCurrentHp()) / Config.ServerAdSetting.summonhpdmg;
				if (adddmg < 0) {
					adddmg = 1;
				}
				dmg += adddmg;
			}
		}
		if (_npc instanceof L1PetInstance) {
			dmg = _random.nextInt(_npc.getNpcTemplate().get_level()) + _npc.getAbility().getTotalStr() / 2 + 1;
			dmg += (lvl / 16); // 寵物每升到16級增加一次額外打擊
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		} else if (_npc instanceof L1SummonInstance) {
			if (_npc.getMaster() != null) {
				dmg += _npc.getDmgup() + (status - status / 2) + _random.nextInt((int) status) + 1
						+ (_npc.getMaster().getLevel() * Config.MagicAdSetting_Wizard.SUMMON_LEVEL_ADDDMG)
						+ (_npc.getMaster().getAbility().getSp() * Config.MagicAdSetting_Wizard.SUMMON_SP_ADDDMG);
			} else {
				dmg += _npc.getDmgup() + (status - status / 2) + _random.nextInt((int) status) + 1;
			}
		} else {
			dmg += _npc.getDmgup() + (status - status / 2) + _random.nextInt((int) status) + 1;
			// dmg = _random.nextInt(lvl) + _npc.getAbility().getTotalStr() / 2 + 1;
		}

		if (isUndeadDamage()) {
			dmg *= 1.2;
		}

		dmg = dmg * getLeverage() / 10;

		dmg += CharacterBalance.getInstance().getDmg(10, 10);

		dmg *= CharacterBalance.getInstance().getDmgRate(10, 10);

		dmg -= calcNpcDamageReduction();

		if (_npc.isWeaponBreaked()) { // NPC處於武器破損狀態。
			dmg /= 2;
		}

		addNpcPoisonAttack(_npc, _targetNpc);

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}

		if (dmg <= 0) {
			_isHit = false;
		}

		return (int) dmg;
	}

	// 當用戶持有某種屬性的劍時 +1/+3/+5/+7/+9
	/** 根據武器屬性附魔賦予效果 (PC對PC) **/
	private double calculatePcWeaponEnchantEffect() {
		int Attr = _weaponAttrLevel;
		double AttrDmg = 0;
//		/double AttrDmg = L1ItemInstance.pureAttrEnchantLevel(Attr);
		switch (_weaponAttrLevel) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			AttrDmg += (Attr - 1) + 1; // 5 - 1 + 1 = 5
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getFire() / 100;
			break;
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			AttrDmg += (Attr - 6) + 1;
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getWater() / 100;

			break;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			AttrDmg += (Attr - 11) + 1;
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getWind() / 100;
			break;
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
			AttrDmg += (Attr - 16) + 1;
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getEarth() / 100;
			break;
		default:
			AttrDmg = 0;
			break;
		}
		return AttrDmg;
	}


	/** 根據武器屬性附魔賦予效果 (PC對NPC) **/
	private int calculateMonsterWeaponEnchantEffect() {
		int attrDmg = 0;
		int attr = _weaponAttrLevel;
		int npcWeakAttr = _targetNpc.getNpcTemplate().get_weakAttr();
		switch (npcWeakAttr) {
			case 1: // 對地屬性弱的怪物
				if (attr >= 15 && attr <= 20) { // 武器屬性
					attrDmg += 1 + (attr - 15);
					if (_arrow != null && _arrow.getItemId() == 3000516) {
						attrDmg += 3;
					}
				}
				break;
			case 2: // 對水屬性弱的怪物
				if (attr >= 6 && attr <= 10) { // 武器屬性
					attrDmg += 1 + (attr - 6);
					if (_arrow != null && _arrow.getItemId() == 3000516) {
						attrDmg += 3;
					}
				}
				break;
			case 4: // 對火屬性弱的怪物
				if (attr >= 1 && attr <= 5) { // 武器屬性
					attrDmg += 1 + (attr - 1);
					if (_arrow != null && _arrow.getItemId() == 3000516) {
						attrDmg += 3;
					}
				}
				break;
			case 8: // 對風屬性弱的怪物
				if (attr >= 11 && attr <= 15) { // 武器屬性
					attrDmg += 1 + (attr - 11);
					if (_arrow != null && _arrow.getItemId() == 3000516) {
						attrDmg += 3;
					}
				}
				break;
			default:
				AttrDmg = 0;
				break;
		}
		return AttrDmg;
	}

	// ●●●● NPC的傷害縮減 ●●●●
	private int calcNpcDamageReduction() {
		return _targetNpc.getNpcTemplate().get_damagereduction();
	}

	// ●●●● 根據武器材料和祝福來計算額外傷害 ●●●●
	private int calcMaterialBlessDmg() {
		int damage = 0;
		int undead = _targetNpc.getNpcTemplate().get_undead();
		if ((_weaponMaterial == 14 || _weaponMaterial == 17 || _weaponMaterial == 22) && (undead == 1 || undead == 3)) { // 銀、秘銀、奧里哈鋼
			damage += _random.nextInt(20) + 1;
		}
		if (_weaponBless == 0 && (undead == 1 || undead == 2 || undead == 3)) { // 祝福
			damage += _random.nextInt(4) + 1;
		}
		if (weapon != null && _weaponType != 20 && _weaponType != 62 && weapon.getHolyDmgByMagic() != 0
				&& (undead == 1 || undead == 3)) {
			damage += weapon.getHolyDmgByMagic();
		}
		return damage;
	}

	// ●●●● NPC的亡靈在夜間攻擊力的變化 ●●●●
	private boolean isUndeadDamage() {
		boolean flag = false;
		int undead = _npc.getNpcTemplate().get_undead();
		boolean isNight = GameTimeClock.getInstance().getGameTime().isNight();
		if (isNight && (undead == 1 || undead == 3)) {
			flag = true;
		}
		return flag;
	}

	/*
	 * if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") &&
	 * _weaponSmall > 0) { weaponMaxDamage = _weaponSmall; } else if
	 * (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") &&
	 * _weaponLarge > 0) { weaponMaxDamage = _weaponLarge; } else if (_targetNpc
	 * instanceof L1PeopleInstance) { weaponMaxDamage = _weaponSmall; }
	 */

	// TODO ●●●● NPC的毒攻擊加成 ●●●●
	private void addNpcPoisonAttack(L1Character attacker, L1Character target) {
		if (!_npc.getNpcTemplate().get_poisonatk().equalsIgnoreCase("None(없)")) { // 有毒攻擊
			if (_npc.getNpcTemplate().get_poisonatkchance() >= _random.nextInt(100) + 1) { // 15%的機率觸發毒攻擊
				switch (_npc.getNpcTemplate().get_poisonatk()) {
					case "Poison(一般毒)":
						L1DamagePoison.doInfection(attacker, target, _npc.getNpcTemplate().get_poisonatkms(),
								_npc.getNpcTemplate().get_poisonatkdmg(), false);
						break;
					case "Poison(沉默毒)":
						L1SilencePoison.doInfection(target, _npc.getNpcTemplate().get_poisonatkSilencems());
						break;
					case "Poison(麻痺毒)":
						L1ParalysisPoison.doInfection(target, L1SkillId.DELAY);
						break;
				}
			}
		}
	}

	// ■■■■ PC的毒攻擊加成 ■■■■
	public void addPcPoisonAttack(L1Character attacker, L1Character target) {
		int chance = _random.nextInt(100) + 1;
		if ((_weaponId == 13 || _weaponId == 44 || (_weaponId != 0 && _pc.hasSkillEffect(ENCHANT_VENOM)))
				&& chance <= Config.MagicAdSetting.PoisonAttack_PC_Chance) {
			L1DamagePoison.doInfection(attacker, target, Config.MagicAdSetting.PoisonAttack_PC_Ms,
					Config.MagicAdSetting.PoisonAttack_PC_DMG, false);
		}
	}

	/* ■■■■■■■■■■■■■■ 攻擊動作傳送 ■■■■■■■■■■■■■■ */

	public void action() {
		try {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				actionPc();
			} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
				actionNpc();
			}
		} catch (Exception e) {
		}
	}

	// ●●●● 玩家攻擊動作傳送 ●●●●
	private void actionPc() {
		int spriteId = _pc.getCurrentSpriteId();
		_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 設定方向
		if (_weaponType == 20) {
			if (!_pc.glanceCheck(_targetX, _targetY)/* || !_target.glanceCheck(_pc.getX(), _pc.getY()) */) {
				_pc.sendPackets(
						new S_UseArrowSkill(_pc, _targetId, ActionCodes.ACTION_BowAttack, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc,
						new S_UseArrowSkill(_pc, _targetId, ActionCodes.ACTION_BowAttack, _targetX, _targetY, _isHit));
				_isHit = false;
				return;
			}
			if (_arrow != null) { // 如果進行了變身，檢查圖像並更改箭矢圖像
				if (!_pc.noPlayerCK)
					_pc.getInventory().removeItem(_arrow, 1);
				if (spriteId == 7967) {
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
				} else if (spriteId == 11402 || spriteId == 8900) { // 75級變身
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 11406 || spriteId == 8913) { // 80級變身
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 13631) { // 82級變身
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 13635) {// 85級變身
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 15814) { // 高等精靈箭矢
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 16002) {// 86級變身
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 16074) {// 88級變身
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 17535) {// 守衛變身
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 17539, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 17539, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else {
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 66, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc,
							new S_UseArrowSkill(_pc, _targetId, 66, _targetX, _targetY, _isHit));
				}
				if (_isHit) {
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				}
				// 해당 아이템착용시 아이템을 체크해서 화살 이미지를 바꾼다
			} else if (_weaponId == 190) {
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 2349, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 2349, _targetX, _targetY, _isHit));
				if (_isHit) {
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				}
			} else if (_weaponId == 202011) {
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
				if (_isHit) {
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				}
				/*
				 * } else if (_weaponId == 10000) { _pc.sendPackets(new S_UseArrowSkill(_pc,
				 * _targetId, 8771, _targetX, _targetY, _isHit));
				 * Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8771,
				 * _targetX, _targetY, _isHit)); if (_isHit) {
				 * Broadcaster.broadcastPacketExceptTargetSight(_target,new
				 * S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc); }
				 */
			}
		} else if (_weaponType == 62 && _sting != null) { // 檢查變身時的圖像
			_pc.getInventory().removeItem(_sting, 1);
			if (spriteId == 7967) {
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
			} else if (spriteId == 11402 || spriteId == 8900) { // 75等級變身
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else if (spriteId == 11406 || spriteId == 8913) { // 80等級變身
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else if (spriteId == 13631) { // 82等級變身
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc,
						new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else if (spriteId == 13635) { // 85等級變身
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc,
						new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else if (spriteId == 15814) { // High Elf Arrow
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc,
						new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else if (spriteId == 16002) {// 86級變身
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc,
						new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else if (spriteId == 16074) {// 88級變身
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc,
						new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else if (spriteId == 17535) {// 守衛變身
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 17539, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc,
						new S_UseArrowSkill(_pc, _targetId, 17539, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else {
				_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 2989, _targetX, _targetY, _isHit));
				Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 2989, _targetX, _targetY, _isHit));
			}
			if (_isHit) {
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			}
		} else if (_weaponType2 == 17) {
			ServerBasePacket pck = null;
			if (!_isCritical) {
				pck = S_Attack.getKeylink2(_pc, _target, _attackType, _isHit);
				_pc.sendPackets(pck, false);
				_pc.broadcastPacket(pck);
			} else {
				pck = S_Attack.getKeylink_Critical(_pc, _target, _attackType, _isHit);
				_pc.sendPackets(pck, false);
				_pc.broadcastPacket(pck);
			}
		} else {
			int actid = ActionCodes.ACTION_Attack;
			if (_isHit) {
				ServerBasePacket pck = null;
				if (!_isCritical) {
					// 랜서 추가
					if (_pc.isSpearModeType()) {
//						pck = S_Attack.getSpear(_pc, _target, _attackType, _isHit);
//						_pc.sendPackets(pck, false);
//						_pc.broadcastPacket(pck);
						_pc.sendPackets(new S_AttackPacket(_pc, _targetId, 125, _attackType));
						Broadcaster.broadcastPacket(_pc, new S_AttackPacket(_pc, _targetId, 125, _attackType));
					} else {
						_pc.sendPackets(new S_AttackPacket(_pc, _targetId, actid, _attackType));
						Broadcaster.broadcastPacket(_pc, new S_AttackPacket(_pc, _targetId, actid, _attackType));
					}
				}
				Broadcaster.broadcastPacketExceptTargetSight(_target,
						new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else {
				if (_targetId > 0) {
					_pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
					Broadcaster.broadcastPacket(_pc, new S_AttackMissPacket(_pc, _targetId));
				} else {
					_pc.sendPackets(new S_AttackPacket(_pc, 0, actid));
					Broadcaster.broadcastPacket(_pc, new S_AttackPacket(_pc, 0, actid));
				}
			}
		}
	}

	// ●●●● NPC的攻擊動作發送 ●●●●
	private void actionNpc() {
		int _npcObjectId = _npc.getId();
		int bowActId = 0;
		int actId = 0;

		_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 設定方向

		// 如果與目標的距離大於等於2則進行遠程攻擊
		boolean isLongRange = (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) > 1);
		bowActId = _npc.getNpcTemplate().getBowActId();

		if (getActId() > 0) {
			actId = getActId();
		} else {
			actId = ActionCodes.ACTION_Attack;
		}

		if (isLongRange && bowActId > 0) {
			Broadcaster.broadcastPacket(_npc,
					new S_UseArrowSkill(_npc, _targetId, bowActId, _targetX, _targetY, _isHit));
		} else {
			if (_isHit) {
				if (getGfxId() > 0) {
					Broadcaster.broadcastPacket(_npc,
							new S_UseAttackSkill(_target, _npcObjectId, getGfxId(), _targetX, _targetY, actId));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _npc);
				} else {
					Broadcaster.broadcastPacket(_npc, new S_AttackPacketForNpc(_target, _npcObjectId, actId));
					Broadcaster.broadcastPacketExceptTargetSight(_target,
							new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _npc);
				}
			} else {
				if (getGfxId() > 0) {
					Broadcaster.broadcastPacket(_npc,
							new S_UseAttackSkill(_target, _npcObjectId, getGfxId(), _targetX, _targetY, actId, 0));
				} else {
					Broadcaster.broadcastPacket(_npc, new S_AttackMissPacket(_npc, _targetId, actId));
				}
			}
		}
	}

	// 如果道具（箭、刺針）未命中，計算其軌跡
	public void calcOrbit(int cx, int cy, int head) // 起點X，起點Y，當前方向
	{
		float dis_x = Math.abs(cx - _targetX); // X方向到目標的距離
		float dis_y = Math.abs(cy - _targetY); // Y方向到目標的距離
		float dis = Math.max(dis_x, dis_y); // 到目標的最大距離
		float avg_x = 0;
		float avg_y = 0;
		if (dis == 0) { // 如果與目標在同一位置，則沿當前方向直行
			// 這裡應該有更多的處理邏輯
			switch (head) {
				case 1:
					avg_x = 1;
					avg_y = -1;
					break;
				case 2:
					avg_x = 1;
					avg_y = 0;
					break;
				case 3:
					avg_x = 1;
					avg_y = 1;
					break;
				case 4:
					avg_x = 0;
					avg_y = 1;
					break;
				case 5:
					avg_x = -1;
					avg_y = 1;
					break;
				case 6:
					avg_x = -1;
					avg_y = 0;
					break;
				case 7:
					avg_x = -1;
					avg_y = -1;
					break;
				case 0:
					avg_x = 0;
					avg_y = -1;
					break;
				default:
					break;
			}
		} else {
			avg_x = dis_x / dis;
			avg_y = dis_y / dis;
		}

		int add_x = (int) Math.floor((avg_x * 15) + 0.59f); // 상하 좌우가 조금 우선인 둥근
		int add_y = (int) Math.floor((avg_y * 15) + 0.59f); // 상하 좌우가 조금 우선인 둥근

		if (cx > _targetX) {
			add_x *= -1;
		}
		if (cy > _targetY) {
			add_y *= -1;
		}

		_targetX = _targetX + add_x;
		_targetY = _targetY + add_y;
	}

	/* ■■■■■■■■■■■■■■■ 계산 결과 반영 ■■■■■■■■■■■■■■■ */

	public void commit() {
		if (_isHit) {
			try {
				if (_calcType == PC_PC || _calcType == NPC_PC) {
					commitPc();
				} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
					commitNpc();
				}
			} catch (Exception e) {
			}
		}

		// 대미지치 및 명중율 확인용 메세지
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
		String PcName = "";
		String HitRate = "";
		String DMG = "";
		String TargetName = "";
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			PcName = _pc.getName();
		} else if (_calcType == NPC_PC) {
			PcName = _npc.getName();
		}

		if (_calcType == NPC_PC || _calcType == PC_PC) {
			TargetName = _targetPc.getName();
			HitRate = "HP: " + _targetPc.getCurrentHp() + " / HitRate: " + _hitRate;
		} else if (_calcType == PC_NPC) {
			TargetName = _targetNpc.getName();
			HitRate = "HP: " + _targetNpc.getCurrentHp() + " / HitRate: " + _hitRate;
		}
		DMG = "DMG: " + _damage;
		if (!(_pc == _targetPc && _damage == 0)) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				_pc.sendPackets("\\aG[" + PcName + " -> " + TargetName + "] : " + DMG + " / " + HitRate);
			}
			if (_calcType == NPC_PC || _calcType == PC_PC) {
				_targetPc.sendPackets("\\aH[" + PcName + " -> " + TargetName + "] : " + DMG + " / " + HitRate);
			}
		}
	}

	// TODO 플레이어 계산 결과를 반영
	private void commitPc() {
		if (_calcType == PC_PC) {
			if (MJCommons.isUnbeatable(_targetPc))
				_damage = 0;
			_targetPc.receiveDamage(_pc, _damage);
		} else if (_calcType == NPC_PC) {
			if (MJCommons.isUnbeatable(_targetPc))
				_damage = 0;
			_targetPc.receiveDamage(_npc, _damage);
		}
	}

	// TODO NPC에 계산 결과를 반영
	private void commitNpc() {
		if (_calcType == PC_NPC) {
			if (MJCommons.isUnbeatable(_targetNpc))
				_damage = 0;
			damageNpcWeaponDurability();
			_targetNpc.receiveDamage(_pc, _damage);
		} else if (_calcType == NPC_NPC) {
			if (MJCommons.isUnbeatable(_targetNpc))
				_damage = 0;
			_targetNpc.receiveDamage(_npc, _damage);
		}
	}

	// TODO 카운터 배리어 : 공격 모션 송신
	public void actionCounterBarrier() {
		if (_calcType == PC_PC) {
			if (_pc == null)
				return;
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 방향세트
			_pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
			_pc.broadcastPacket(new S_AttackMissPacket(_pc, _targetId), _target);
			_pc.sendPackets(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			_pc.broadcastPacket(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			if (_targetPc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
				_targetPc.send_effect(17220);
			} else {
				_targetPc.send_effect(10710);
			}
		} else if (_calcType == NPC_PC) {
			if (_npc == null || _target == null)
				return;
			int actId = 0;
			_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 방향세트
			if (getActId() > 0) {
				actId = getActId();
			} else {
				actId = ActionCodes.ACTION_Attack;
			}
			if (getGfxId() > 0) {
				_npc.broadcastPacket(
						new S_UseAttackSkill(_target, _npc.getId(), getGfxId(), _targetX, _targetY, actId, 0), _target);
			} else {
				_npc.broadcastPacket(new S_AttackMissPacket(_npc, _targetId, actId), _target);
			}
			_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage));
			if (_targetPc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
				_targetPc.send_effect(17220);
			} else {
				_targetPc.send_effect(10710);
			}
		}
	}

	public void actionConqure() {
		if (_calcType == PC_PC) {
			if (_pc == null)
				return;
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 방향세트
			_pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
			_pc.broadcastPacket(new S_AttackMissPacket(_pc, _targetId), _target);
			_pc.sendPackets(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			_pc.broadcastPacket(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			_targetPc.send_effect(21808);
		} else if (_calcType == NPC_PC) {
			if (_npc == null || _target == null)
				return;
			int actId = 0;
			_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 방향세트
			if (getActId() > 0) {
				actId = getActId();
			} else {
				actId = ActionCodes.ACTION_Attack;
			}
			if (getGfxId() > 0) {
				_npc.broadcastPacket(
						new S_UseAttackSkill(_target, _npc.getId(), getGfxId(), _targetX, _targetY, actId, 0), _target);
			} else {
				_npc.broadcastPacket(new S_AttackMissPacket(_npc, _targetId, actId), _target);
			}
			_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage));
			_targetPc.send_effect(21808);

		}
	}

	public void NpcactionCounterBarrier(L1Character attacker, L1PcInstance pc) {
		if (pc == null || _target == null)
			return;
		int actId = 0;
		pc.setHeading(pc.targetDirection(_targetX, _targetY)); // 방향세트
		if (getActId() > 0) {
			actId = getActId();
		} else {
			actId = ActionCodes.ACTION_Attack;
		}
		if (getGfxId() > 0) {
			pc.broadcastPacket(new S_UseAttackSkill(_target, pc.getId(), getGfxId(), _targetX, _targetY, actId, 0),
					_target);
		} else {
			pc.broadcastPacket(new S_AttackMissPacket(pc, _targetId, actId), _target);
		}
		pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
		attacker.send_effect(17220);
	}

	// TODO 모탈바디 발동시 공격 모션 송신
	public void actionMortalBody() {
		if (_calcType == PC_PC) {
			if (_pc == null || _target == null)
				return;
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 방향세트
			S_UseAttackSkill packet = new S_UseAttackSkill(_pc, _target.getId(), 9802, _targetX, _targetY,
					ActionCodes.ACTION_Attack, false);
			_pc.sendPackets(packet);
			_pc.broadcastPacket(packet, _target);
			_pc.sendPackets(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			_pc.broadcastPacket(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
		} else if (_calcType == NPC_PC) {
			if (_npc == null || _target == null)
				return;
			_npc.send_effect(9802);
			_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage));
		}
	}

	// TODO 判斷對方攻擊是否有效
	public boolean isShortDistance1() {
		boolean isShortDistance1 = true;
		/*
		 * if (_calcType == PC_PC) { if (_weaponType == 20 || _weaponType == 62) { //
		 * 弓或拳套 isShortDistance1 = false; }
		 */
		if (_calcType == NPC_PC) {
			boolean isLongRange = (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) <= 0);
			int bowActId = _npc.getNpcTemplate().getBowActId();
			// 距離超過2，且攻擊者具有弓的動作ID的情況下是遠程攻擊
			if (isLongRange && bowActId > 0) {
				isShortDistance1 = false;
			}
		}
		return isShortDistance1;
	}

	// TODO 상대의 공격에 대해 카운터 배리어가 유효한가 판별
	public boolean isShortDistance() {
		boolean isShortDistance = true;
		if (_calcType == PC_PC) {
			if (_weaponType == 20 || _weaponType == 62 || _weaponType2 == 17 || _weaponType2 == 19
					|| _pc.hasSkillEffect(L1SkillId.ARMOR_BRAKE)) {
				isShortDistance = false;
			}
		} else if (_calcType == NPC_PC) {
			if (_npc == null)
				return false;
			boolean isLongRange = (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) > 1);
			int bowActId = _npc.getNpcTemplate().getBowActId();
			// 거리가 2이상, 공격자의 활의 액션 ID가 있는 경우는 원공격
			if (isLongRange && bowActId > 0) {
				isShortDistance = false;
			}
		}
		return isShortDistance;
	}

	// TODO 카운터 배리어의 대미지 반영
	public void commitCounterBarrier() {
		int damage = calcCounterBarrierDamage();
		if (damage == 0) {
			return;
		}
		if (_calcType == PC_PC) {
			_pc.receiveCounterBarrierDamage(_targetPc, damage);
		} else if (_calcType == NPC_PC) {
			_npc.receiveCounterBarrierDamage(_targetPc, damage);
		} else if (_calcType == PC_NPC) {
			_pc.receiveCounterBarrierDamage(_targetPc, damage);
		}
	}

	public void commitConqure() {
		int damage = calcConqureDamage();
		int difflevel = 0;
		int targetlevel = 0;
		int attackerlevel = 0;
		if (damage == 0) {
			return;
		}
		// System.out.println("확인:"+damage);
		int _shockStunDuration = 0;

		if (_calcType == PC_PC) {
			// targetlevel = _targetPc.getLevel();
			if (_pc instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) _pc;
				DCCD = CalcStat.calcPureDecreaseCCDuration(target.getAbility().getCha())
						+ CalcStat.calcDecreaseCCDuration(target.getAbility().getTotalCha())
						+ target.get_status_time_reduce();
			}
			if (_targetPc instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) _targetPc;
				ICCD = pc.get_CC_Increase();
			}
			attackerlevel = _pc.getLevel();
		} else if (_calcType == NPC_PC) {
			// targetlevel = _targetNpc.getLevel();
			attackerlevel = _npc.getLevel();
		}

//		difflevel = _pc.getLevel() - targetlevel;//
		difflevel = _targetPc.getLevel() - attackerlevel;
//		System.out.println("等級差異: " + difflevel);
		if (_targetPc != null) {
			if (difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL) {
				int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS;
				_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
			} else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL1
					&& difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL2) {
				int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS1;
				_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
			} else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL3
					&& difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL4) {
				int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS2;
				_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
			} else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL5
					&& difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL6) {
				int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS3;
				_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
			} else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL7
					&& difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL8) {
				int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS4;
				_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
			} else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL9) {
//                System.out.println("來了?");
				int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS5;
//                System.out.println(SkillTimeArray);
				_shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
//                System.out.println("暈眩時間 "+_shockStunDuration);

			}
		}
//        System.out.println("暈眩時間1 "+_shockStunDuration);
		if (DCCD != 0) {
			_shockStunDuration -= DCCD;
		}
		if (ICCD != 0) {
			_shockStunDuration += ICCD;
		}
		L1ItemInstance weapon = _targetPc.getWeapon();
		if (weapon.getItemId() == 7000239) {
			_shockStunDuration += 1000;
		}
		if (_shockStunDuration <= 0) {
			_shockStunDuration = 100;
		}
//		System.out.println("스턴시간2 "+_shockStunDuration);
		if (_calcType == PC_PC) {
			L1Magic magic = new L1Magic(_targetPc, _pc);
			boolean isStun = magic.calcProbabilityMagic(L1SkillId.CONQUEROR_STUN);

			if (isStun) {
				_pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
				_pc.setSkillEffect(L1SkillId.CONQUEROR_STUN, _shockStunDuration);
//				_pc.setSkillEffect(L1SkillId.SHOCK_STUN, _shockStunDuration);
				L1SkillUse.on_icons(_pc, L1SkillId.CONQUEROR_STUN, _shockStunDuration / 1000);
				L1EffectSpawn.getInstance().spawnEffect2(460000167, L1SkillId.CONQUEROR_STUN, _pc.getX(), _pc.getY(),
						_pc.getMapId(), _pc);
			}
		} else if (_calcType == NPC_PC) {
			L1Magic magic = new L1Magic(_targetPc, _npc);
			boolean isStun = magic.calcProbabilityMagic(L1SkillId.CONQUEROR_STUN);
			if (isStun) {
//				System.out.println(_shockStunDuration);
				L1EffectSpawn.getInstance().spawnEffect2(460000167, L1SkillId.CONQUEROR_STUN, _npc.getX(), _npc.getY(),
						_npc.getMapId(), _npc);
				_npc.setSkillEffect(L1SkillId.CONQUEROR_STUN, _shockStunDuration);
				_npc.setParalyzed(true);
				_npc.setParalysisTime(_shockStunDuration);
			}
		}

		if (_calcType == PC_PC) {
			_pc.receiveConqureDamage(_targetPc, damage);
		} else if (_calcType == NPC_PC) {
			_npc.receiveConqureDamage(_targetPc, damage);
		}
	}

	public void commitBossCounterBarrier(L1Character attacker, L1PcInstance pc) {
		int damage = 0;
		L1ItemInstance weapon = null;
		weapon = pc.getWeapon();
		if (weapon == null)
			damage = 10;
		else
			damage = (int) Math.round(
					(weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier())
							* Config.MagicAdSetting_Knight.COUNTER / 100);
		pc.receiveCounterBarrierDamage(pc, damage);
	}

	// TODO 모탈바디의 대미지를 반영
	public void commitMortalBody() {
		int ac = Math.max(0, 10 - _targetPc.getAC().getAc());
		int damage = ac / 2;

		if (damage == 0) {
			return;
		}
		if (damage <= 40) {
			damage = 40;
		}
		if (_calcType == PC_PC) {
			_pc.receiveDamage(_targetPc, damage);
		} else if (_calcType == NPC_PC) {
			_npc.receiveDamage(_targetPc, damage);
		}
	}

	// TODO 카운터 배리어의 대미지를 산출
	private int calcCounterBarrierDamage() {
		double damage = 0;
		L1ItemInstance weapon = _targetPc.getWeapon();
		if (weapon != null) {
			if (weapon.getItem().getType() == 3) {
				damage = Math.round((weapon.getItem().getDmgLarge() + getEnchantPureDmg(weapon))
						+ weapon.getItem().getDmgModifier()) * Config.MagicAdSetting_Knight.COUNTER / 100;
			}
		}
		return (int) damage;
	}

	private int calcConqureDamage() {
		double damage = 0;
		L1ItemInstance weapon = _targetPc.getWeapon();
		if (weapon != null) {
			if (weapon.getItem().getType() == 1 || weapon.getItem().getType() == 2) {
				damage = Math
						.round((weapon.getItem().getDmgSmall() + getEnchantPureDmg(weapon))
								+ weapon.getItem().getDmgModifier())
						* Config.MagicAdSetting_Prince.CONQUEROR_DAMAGE_RATE / 100;
			}
		}

		return (int) damage;
	}

	// TODO 計算戰士泰坦的傷害
	private int calculateTitanDamage() {
		double damage = 0;
		L1ItemInstance weapon = _targetPc.getWeaponSwap();
		if (weapon != null) {
			damage = Math.round(
					(weapon.getItem().getDmgLarge() + getEnchantPureDmg(weapon)) + weapon.getItem().getDmgModifier())
					* Config.MagicAdSetting_Warrior.ROCKDMG / 100;
		}
		return (int) damage;
	}

	// TODO 損壞武器。對NPC的情況下，損壞概率為10%。祝福武器為3%。
	private void damageNpcWeaponDurability() {

		int chance = Config.ServerRates.DAMAGEENCHANT; // 一般武器
		int bchance = Config.ServerRates.DAMAGEBLESSENCHANT; // 祝福武器損壞概率

		/** 機器人系統 **/
		if (_pc.getAI() != null) {
			return;
		}

		if (_pc != null && _pc.isPassive(MJPassiveID.DAMASCUS.toInt())) {
			return;
		}
		/** 神話武器不會損壞 **/
		if (_pc != null) {
			if (_weaponId == 7000239 || _weaponId == 7000240 || _weaponId == 7000262 || _weaponId == 7000263
					|| _weaponId == 203065 || _weaponId == 7000265 || _weaponId == 7000264 || _weaponId == 203042) {
				return;
			}

		}

		// TODO 不損壞的NPC、空手、不損壞的武器使用、SOF中的情況下不執行任何操作。
		if (_calcType != PC_NPC || _targetNpc.getNpcTemplate().is_hard() == false || _weaponType == 0
				|| weapon.getItem().get_canbedmg() == 0 || _pc.hasSkillEffect(SOUL_OF_FLAME)) {
			return;
		}

		// TODO 普通武器·被詛咒的武器（一般損壞）
		if (_pc != null && !_pc.isPassive(MJPassiveID.SOLID_NOTE.toInt())) {
			if (weapon.isEquipped()) {
				if ((_weaponBless == 1 || _weaponBless == 2) && ((_random.nextInt(100) + 1) < chance)) {
					_pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
					_pc.getInventory().receiveDamage(weapon);
				}
			}
		}

		// TODO 祝福的武器損壞
		if (_pc != null && !_pc.isPassive(MJPassiveID.SOLID_NOTE.toInt())) {
			if (_weaponBless == 0 && ((_random.nextInt(100) + 1) < bchance)) {
				_pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
				_pc.getInventory().receiveDamage(weapon);
			}
		}
	}

	// TODO 基於屬性和武器的攻擊加成
	private int calculateHitAddition() {
		int value = 0;
		if (_weaponType != 20 && _weaponType != 62) {
			value += _pc.getHitup() + _pc.getHitRate();
		} else {
			value += _pc.getBowHitup() + _pc.getBowHitRate();
		}
		return value;
	}

	// TODO 타겟PC 회피 스킬에 대한 연산
	private int toPcSkillHit() {
		int value = 0;
		if (_targetPc.hasSkillEffect(L1SkillId.ANTA_MAAN) || _targetPc.hasSkillEffect(L1SkillId.BIRTH_MAAN)
				|| _targetPc.hasSkillEffect(L1SkillId.SHAPE_MAAN)) {
			int chance = _random.nextInt(100);
			if (chance < 15) {
				value -= 5;
			}
		}
		return value;
	}

	// TODO 대상 Buff에 따른 대미지 연산
	private double toPcBuffDmg(double dmg) {
		try {
			if (_targetPc.hasSkillEffect(COOKING_1_0_S) // 요리에 의한 대미지 경감
					|| _targetPc.hasSkillEffect(COOKING_1_1_S) || _targetPc.hasSkillEffect(COOKING_1_2_S)
					|| _targetPc.hasSkillEffect(COOKING_1_3_S) || _targetPc.hasSkillEffect(COOKING_1_4_S)
					|| _targetPc.hasSkillEffect(COOKING_1_5_S) || _targetPc.hasSkillEffect(COOKING_1_6_S)
					|| _targetPc.hasSkillEffect(COOKING_1_8_S) || _targetPc.hasSkillEffect(COOKING_1_9_S)
					|| _targetPc.hasSkillEffect(COOKING_1_10_S) || _targetPc.hasSkillEffect(COOKING_1_11_S)
					|| _targetPc.hasSkillEffect(COOKING_1_12_S) || _targetPc.hasSkillEffect(COOKING_1_13_S)
					|| _targetPc.hasSkillEffect(COOKING_1_14_S) || _targetPc.hasSkillEffect(COOKING_1_15_S)
					|| _targetPc.hasSkillEffect(COOKING_1_16_S) || _targetPc.hasSkillEffect(COOKING_1_17_S)
					|| _targetPc.hasSkillEffect(COOKING_1_18_S) || _targetPc.hasSkillEffect(COOKING_1_19_S)
					|| _targetPc.hasSkillEffect(COOKING_1_20_S) || _targetPc.hasSkillEffect(COOKING_1_21_S)
					|| _targetPc.hasSkillEffect(COOKING_1_22_S)) {
				dmg -= 5;
			}
			if (_targetPc.hasSkillEffect(COOK_STR) || _targetPc.hasSkillEffect(COOK_DEX)
					|| _targetPc.hasSkillEffect(COOK_INT) || _targetPc.hasSkillEffect(COOK_GROW)
					|| _targetPc.hasSkillEffect(COOK_STR_Bless) || _targetPc.hasSkillEffect(COOK_DEX_Bless)
					|| _targetPc.hasSkillEffect(COOK_INT_Bless) || _targetPc.hasSkillEffect(COOK_GROW_Bless)) { // 리뉴얼
				// 요리
				dmg -= 2;
			}
			if (_targetPc.hasSkillEffect(COOKING_1_7_S) || _targetPc.hasSkillEffect(COOKING_1_15_S)
					|| _targetPc.hasSkillEffect(COOKING_1_23_S)) { // 디저트에
				// 경감
				dmg -= 5;
			}

			// 전사스킬 : 아머가드 - 캐릭의 AC/10의 데미지감소 효과를 얻는다.
			if (_targetPc.getPassive(MJPassiveID.ARMOR_GUARD.toInt()) != null) {
				int d = _targetPc.getAC().getAc() / 10;
				if (d < 0) {
					dmg += d;
				} else {
					dmg -= (d * 1.5);
				}
			}

			if (_targetPc.isPassive(MJPassiveID.MAJESTY.toInt())) {
				int targetPcLvl = _targetPc.getLevel();
				int reduction = 0;
				if (targetPcLvl < 80) {
					targetPcLvl = 80;
				} else if (targetPcLvl >= 80) {
					reduction = (targetPcLvl - 80) / 2 + 2;
				}
				if (reduction >= 10) {
					reduction = 10;
				}

				dmg -= reduction;
			}

			if (_targetPc.hasSkillEffect(PATIENCE)) {
				int targetPcLvl = _targetPc.getLevel();
				if (targetPcLvl < 80) {
					targetPcLvl = 80;
				}
				dmg -= (targetPcLvl - 80) / 4 + 1;
			}

//			if (_targetPc.hasSkillEffect(DRAGON_SKIN)) {
			if (_targetPc.isPassive(MJPassiveID.DRAGON_SKIN_PASS.toInt())) {
				if (_targetPc.getLevel() >= 80) {
					dmg -= 5 + ((_targetPc.getLevel() - 78) / 2);
				} else {
					dmg -= 5;
				}
			}

			if (_targetPc.hasSkillEffect(IllUSION_AVATAR)) {
				dmg += (dmg / 5);
			}
			if (_targetPc.hasSkillEffect(FEATHER_BUFF_A)) {
				dmg -= 3;
			}
			if (_targetPc.hasSkillEffect(FEATHER_BUFF_B)) {
				dmg -= 2;
			}
			if (_targetPc.hasSkillEffect(EARTH_GUARDIAN)) {
				int targetPcLvl = _targetPc.getLevel();
				if (targetPcLvl < 80) {
					targetPcLvl = 80;
				}
				dmg -= (targetPcLvl - 80) / 4 + 1;
			}
			if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
				dmg = 0;
			}
			if (_targetPc.hasSkillEffect(ICE_LANCE)) {
				dmg = 0;
			}
			if (_targetPc.hasSkillEffect(EARTH_BIND)) {
				dmg = 0;
			}
			if (_targetPc.hasSkillEffect(PHANTASM)) {
				_targetPc.removeSkillEffect(PHANTASM);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dmg;
	}

	// TODO 특수 인챈트 시스템
	public static double WeaponLevelAttack(L1PcInstance pc, L1Character cha, int effect, int enchant, int weaponlevel) {
		double dmg = 0;
		int Stat = 0;
		int chance = _random.nextInt(100) + 1;
		int enchatndmg = 0;

		if (pc.isWizard() || pc.isBlackwizard()) {
			Stat = pc.getAbility().getTotalInt();
		}
		if (pc.isElf()) {
			Stat = pc.getAbility().getTotalDex();
		} else {
			Stat = pc.getAbility().getTotalStr();
		}

		if (weaponlevel == 1) {
			enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl1 + Stat;
		} else if (weaponlevel == 2) {
			enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl2 + Stat;
		} else if (weaponlevel == 3) {
			enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl3 + Stat;
		} else if (weaponlevel == 4) {
			enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl4 + Stat;
		}

		if (Config.DollEnchant.WeaponMagicPer + enchant + weaponlevel >= chance) {// 찬스
			dmg = _random.nextInt(enchatndmg) + 1;
			if (2 >= chance) {// 이펙트 랜타 찬스
				dmg += dmg * 0.1;// 대미지
			} else if (dmg <= 0) {
				dmg = 0;
			}
			broadcast(cha, new S_SkillSound(cha.getId(), effect));
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double calcDamageReduction(L1Character cha, double dmg, int attr) {
		if (isFreeze(cha)) {
			return 0;
		}

		int ran1 = 0; // 랜덤 수치 적용
		int mrset = 0; // 엠알에서 랜덤 수치를 뺀값
		int mrs = cha.getResistance().getEffectedMrBySkill();
		ran1 = _random.nextInt(5) + 1;
		mrset = mrs - ran1;
		double calMr = 0.00D;
		calMr = (220 - mrset) / 250.00D;
		dmg *= calMr;

		if (dmg < 0) {
			dmg = 0;
		}

		int resist = 0;
		if (attr == L1Skills.ATTR_EARTH) {
			resist = cha.getResistance().getEarth();
		} else if (attr == L1Skills.ATTR_FIRE) {
			resist = cha.getResistance().getFire();
		} else if (attr == L1Skills.ATTR_WATER) {
			resist = cha.getResistance().getWater();
		} else if (attr == L1Skills.ATTR_WIND) {
			resist = cha.getResistance().getWind();
		}
		int resistFloor = (int) (0.32 * Math.abs(resist));
		if (resist >= 0) {
			resistFloor *= 1;
		} else {
			resistFloor *= -1;
		}
		double attrDeffence = resistFloor / 32.0;
		dmg = (1.0 - attrDeffence) * dmg;

		return dmg;
	}

	private static boolean isFreeze(L1Character cha) {

		if (cha.hasSkillEffect(STATUS_FREEZE)) {
			return true;
		}
		if (cha.hasSkillEffect(ABSOLUTE_BARRIER)) {
			return true;
		}
		if (cha.hasSkillEffect(ICE_LANCE)) {
			return true;
		}
		if (cha.hasSkillEffect(EARTH_BIND)) {
			return true;
		}

		if (cha.hasSkillEffect(COUNTER_MAGIC)) {
			cha.removeSkillEffect(COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(COUNTER_MAGIC).getCastGfx();
			cha.broadcastPacket(new S_SkillSound(cha.getId(), castgfx));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
			}
			return true;
		}
		return false;
	}

	protected static void broadcast(L1Character c, ServerBasePacket pck) {
		c.sendPackets(pck, false);
		c.broadcastPacket(pck);
	}

	public void set_hit_rate(int rate) {
		_hitRate = rate;
	}

	public void set_hit(boolean hit) {
		_isHit = hit;
	}

	public void set_is_critical(boolean is_critical) {
		_isCritical = is_critical;
	}

	private int getReductionIgnore(int reduc, int pvpreduc, L1Character pc) {
		// System.out.println("total reduc : " + (reduc + pvpreduc));
		int canDmg = 0;
		int reduc_ignore = 0;

		if (pc instanceof L1PcInstance) {
			reduc_ignore = pc.getReducCancel() + ((L1PcInstance) pc).get_pvp_dmg_ignore();
		} else {
			reduc_ignore = pc.getReducCancel();
		}

		canDmg = (reduc + pvpreduc) - reduc_ignore;

		if (canDmg < 0)
			canDmg = 0;

//		System.out.println("傷害減免後的傷害：" + canDmg);

		return canDmg;
	}

	public int getEnchantPureDmg(L1ItemInstance weapon) {
		if (weapon == null) {
			return 0;
		}
		int dmg = 0;
		int enchant = weapon.getEnchantLevel();
		if (C_ItemUSe.is_legend_weapon(weapon.getItemId())) {
			dmg = enchant * 2;
		} else if (C_ItemUSe.is_ancient_weapon(weapon.getItemId())) {
			dmg = enchant * 4;
		} else {
			if (enchant >= 1 && enchant <= 9)
				dmg = enchant;
			else if (enchant > 9)
				dmg = 9 + ((enchant - 9) * 2);
		}
		if (_weaponType != 20 && _weaponType != 62) {
			dmg -= weapon.get_durability(); // 손상분
		}
		return dmg;
	}

	private int getEnchantDmg(L1ItemInstance weapon) {
		int dmg = getEnchantPureDmg(weapon);

		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += dmg / 2;
			}
		} else if (_weaponType == 58 && (_random.nextInt(100) + 1) <= _weaponDoubleDmgChance) { // 크로우
			_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 58, Sweapon != null));
			Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 58, Sweapon != null));
			_attackType = 2;
			_isCritical = true;
			dmg += dmg / 2;
		} else if (dmg > 1) {
			dmg = (dmg - dmg / 2) + _random.nextInt(dmg) + 1;
		}
		return dmg;
	}

	public static void fouslayer_brave(L1PcInstance attacker, L1Character cha) {
		Random random = new Random(System.nanoTime());
		L1Magic magic = new L1Magic(attacker, cha);
		int skillid = 315;
		int ReSkilldelay = Config.MagicAdSetting_DragonKnight.FOU_TURN_REUSE_TIME;// 스턴 재사용 쿨타임
		if (attacker.hasSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME)) {
			return;
		}
		if (!attacker.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
			return;
		}
		if (attacker.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
			skillid = 7320184;
		}

		boolean success = magic.calcProbabilityMagic(skillid);
		attacker.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
		attacker.setSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME, ReSkilldelay);// 쿨타임

		if (success) {
			int targetLevel = 0;
			int diffLevel = 0;
			int StunDuration = 0;

			int SpawnEffect = 8503099;// 스폰 이펙트
			if (attacker.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
				SpawnEffect = 460000170;
			}

			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				targetLevel = pc.getLevel();
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				targetLevel = npc.getLevel();
			}

			diffLevel = attacker.getLevel() - targetLevel;

			// TODO 例如：施法者等級:80 對方等級:86~差距)
			if (diffLevel < -5) {
				int[] stunTimeArray = { 1000, 2000, 3000 };
				StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
				// TODO 例如：施法者等級:80 對方等級:83~85 (-3~差距)
			} else if (diffLevel >= -5 && diffLevel <= -3) {
				int[] stunTimeArray = { 1000, 2000, 3000, 4000 };
				StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
				// TODO 例如：施法者等級:80 對方等級:78~82 (-2~+2差距)
			} else if (diffLevel >= -2 && diffLevel <= 2) {
				int[] stunTimeArray = { 1000, 2000, 3000, 4000 };
				StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
				// TODO 例如：施法者等級:80 對方等級:75~77 (-1~-2差距)
			} else if (diffLevel >= 3 && diffLevel <= 5) {
				int[] stunTimeArray = { 2000, 3000, 4000, 5000 };
				StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
				// TODO 例如：施法者等級:80 對方等級:70~74 (-1~-4差距)
			} else if (diffLevel >= 5 && diffLevel <= 10) {
				int[] stunTimeArray = { 3000, 5000, 6000 };
				StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
				// TODO 例如：施法者等級:80 對方等級:-69~(以下)
			} else if (diffLevel > 10) {
				int[] stunTimeArray = { 4000, 5000, 6000 };
				StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
			}
			if (attacker.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
				StunDuration += 1000;
			}

			if (attacker.getWeapon() != null) {
				L1ItemInstance weapon = attacker.getWeapon();
				if (weapon.getItemId() == 7000267) {
					StunDuration += 1000;
				}
			}

			L1EffectSpawn.getInstance().spawnEffect2(SpawnEffect, skillid, cha.getX(), cha.getY(), cha.getMapId(), cha);
//			System.out.println("暈眩時間：" + StunDuration);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
				L1SkillUse.on_icons(pc, skillid, StunDuration / 1000);
				pc.setSkillEffect(skillid, StunDuration);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setSkillEffect(skillid, StunDuration);
				npc.setParalyzed(true);
				npc.setParalysisTime(StunDuration);
			}
			/*
			 * attacker.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
			 * attacker.setSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME,
			 * ReSkilldelay);//쿨타임
			 */ }
	}

	private static final int[] STUN_TO_HOLD_TYPE_SKILL = { SHOCK_STUN, CRUEL, PANTHERA, EMPIRE, BONE_BREAK, FORCE_STUN,
			FORCE_STUN_FAIL, MOB_RANGESTUN_18, MOB_RANGESTUN_19, MOB_SHOCKSTUN_30, BOS_STUN18, OMAN_STUN, fornos_STUN,
			MOSTER_STUN_1, Besi_STUN, Moster_STUN, Maeno_STUN, ANTA_MESSAGE_6, ANTA_MESSAGE_7, ANTA_MESSAGE_8,
			ANTA_SHOCKSTUN, CHAINSWORD_STUN, BALOCH_STUN, DRAGON_HALPAS_STUN, POWERRIP, DESPERADO, SHADOW_STEP, PHANTOM,
			PHANTASM, DARK_BLIND, THUNDER_GRAB, STATUS_FREEZE, DISINTEGRATE, OSIRIS };
}

package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.DeathMatch.DeathMatchSystem;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DAMAGE_OF_TIME_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_INSTANCE_HP_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
//import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOGGLE_EFFECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_UserForm.SC_USER_FORM_NOTI;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SkillCheck;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.datatables.MonsterParalyzeDelay;
import l1j.server.server.datatables.MonsterParalyzeDelay.MonsterParalyze;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1CurseParalysis;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1DwarfInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.item.function.Telbookitem;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1DemolitionDamage;
import l1j.server.server.model.poison.L1Osiris;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.skill.noti.MJNotiSkillModel;
import l1j.server.server.model.skill.noti.MJNotiSkillService;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_IvenBuffIcon;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_NewSkillIcon;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RangeSkill;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.clientpackets.ClientBasePacket;



public class L1SkillUse {

	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_LOGIN = 1;
	public static final int TYPE_SPELLSC = 2;
	public static final int TYPE_NPCBUFF = 3;
	public static final int TYPE_GMBUFF = 4;

	private L1Skills _skill;
	private int _skillId;
	private int _getBuffDuration;
	private int _shockStunDuration;
	private int _getBuffIconDuration;
	private int _targetID;
	private int _mpConsume = 0;
	private int _hpConsume = 0;
	private int _targetX = 0;
	private int _targetY = 0;
	private int _PowerRipDuration;
	private int _earthBindDuration;
	private int _skillTime = 0;
	private int _type = 0;
	private int ICCD = 0;
	private int DCCD = 0;
	private boolean _isPK = false;
	// private int _bookmarkId = 0;
	private int _itemobjid = 0;
	private boolean _checkedUseSkill = false;
	private int _leverage = 10;
	private boolean _isFreeze = false;

	private boolean _isPhantom = false;
	private boolean _isPanthera = false;
	private boolean _isShadow_step = false;
	private boolean _isEterniti = false;
	private boolean _isElvenstrike = false;
	private boolean _isnemesis = false;

	private L1Character _user = null;
	private L1Character _target = null;

	private L1PcInstance _player = null;
	private L1NpcInstance _npc = null;
	private L1NpcInstance _targetNpc = null;

	private int _calcType;
	private static final int PC_PC = 1;
	private static final int PC_NPC = 2;
	private static final int NPC_PC = 3;
	private static final int NPC_NPC = 4;
	private Random random = new Random(System.nanoTime());
	private ArrayList<TargetStatus> _targetList;

	private short _bookmark_mapid = 0;
	private int _bookmark_x = 0;
	private int _bookmark_y = 0;

	private boolean _isGlanceCheckFail = false;
	private boolean _isCriticalDamage = false;

	// 시전자가 시전한 트루타겟을 임시로 담을 공간.
	public static Map<Integer, L1Object> _truetarget_list = new HashMap<Integer, L1Object>();

	private static Logger _log = Logger.getLogger(L1SkillUse.class.getName());

	// private static final int[] polyArray = { 945, 979, 1037, 1039, 15715, 30, 94, 3865, 15673, 15719, 95, 146, 15614, 2376, 2377, 3866, 3867, 3868, 3869, 3870, 3871, 3872, 2468, 3874, 3875, 3876, 185,
	// 173, 187, 183, 11358, 11396, 11397, 12225, 12226, 11399, 11398, 12227, 15638, 15635, 15636 };

	public L1SkillUse() {
		// new Throwable().printStackTrace();
	}

	private static class TargetStatus {
		private L1Character _target = null;
		// private boolean _isAction = false;
		// private boolean _isSendStatus = false;
		private boolean _isCalc = true;

		public TargetStatus(L1Character _cha) {
			_target = _cha;
		}

		public TargetStatus(L1Character _cha, boolean _flg) {
			_isCalc = _flg;
		}

		public L1Character getTarget() {
			return _target;
		}

		public boolean isCalc() {
			return _isCalc;
		}
	}

	public void setLeverage(int i) {
		_leverage = i;
	}

	public int getLeverage() {
		return _leverage;
	}

	private boolean isCheckedUseSkill() {
		return _checkedUseSkill;
	}

	private void setCheckedUseSkill(boolean flg) {
		_checkedUseSkill = flg;
	}

	public boolean checkUseSkill(L1PcInstance player, int skillid, int target_id, int x, int y, String message, int time, int type, L1Character attacker) {
//		System.out.println("1-1 "+skillid);
		// ** 아래 버그 체크문 실행하면서 에러 안나게 **
		if (player instanceof L1PcInstance) {
			L1Object l1object = L1World.getInstance().findObject(target_id);
			if (l1object instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance) l1object;
				if (item.getX() != 0 && item.getY() != 0) {
					return false;
				}
			}

			/*
			 * if (skillid == BLOW_ATTACK) {
			 *     if (player.getInventory().getTypeEquipped(2, 7) <= 0) {
			 *         player.sendPackets("未裝備盾牌，無法使用。");
			 *         return false;
			 *     }
			 * }
			 */

			if (skillid == BLOW_ATTACK || skillid == BRAVE_MENTAL || skillid == ELEMENTAL_FIRE || skillid == QUAKE || skillid == TYRANT) {
				if (player.getWeapon() == null) {
					player.sendPackets("只有在裝備近戰武器的狀態下才能使用。");
					return false;
				}
				if (player.getWeapon().getItem().getType1() == 20 || player.getWeapon().getItem().getType1() == 62) {
					player.sendPackets("只有在裝備近戰武器的狀態下才能使用。");
					return false;
				}
			}

			/**
			 * TODO 增加防止技能BUG的代碼
			 * 如果不是已學會的技能則返回
			 * 防止中繼器
			 **/
			int[] CheckSkillID = { ERUPTION, SUNBURST, WEAKNESS, BLESS_WEAPON, HEAL_ALL, FREEZEENG_ARMOR, SUMMON_MONSTER, HOLY_WALK, TORNADO, BERSERKERS, ENCHANT_ACURUCY, FULL_HEAL, FIRE_WALL,
					BLIZZARD, INVISIBILITY, RESURRECTION, EARTHQUAKE, LIFE_STREAM, SILENCE, LIGHTNING_STORM, FOG_OF_SLEEPING, SHAPE_CHANGE, IMMUNE_TO_HARM, MASS_TELEPORT, FIRE_STORM, DECAY_POTION,
					COUNTER_DETECTION, DEATH_HEAL, METEOR_STRIKE, GREATER_RESURRECTION, ICE_METEOR_STRIKE, DISINTEGRATE, ABSOLUTE_BARRIER, ADVANCE_SPIRIT, FREEZING_BLIZZARD, SHOCK_STUN,
					REDUCTION_ARMOR, BOUNCE_ATTACK, SOLID_CARRIAGE, COUNTER_BARRIER, ABSOLUTE_BLADE, PRIDE, BLOW_ATTACK, BLIND_HIDING, ENCHANT_VENOM, SHADOW_ARMOR, BRING_STONE, MOVING_ACCELERATION,
					BURNING_SPIRIT, DARK_BLIND, VENOM_RESIST, DOUBLE_BRAKE, UNCANNY_DODGE, SHADOW_FANG, FINAL_BURN, DRESS_MIGHTY, DRESS_DEXTERITY, DRESS_EVASION, ARMOR_BRAKE, TRUE_TARGET,
					GLOWING_WEAPON, SHINING_SHILD, BRAVE_MENTAL, BRAVE_UNION, AURA, GRACE, EMPIRE, FOOD_BUFF, RESIST_MAGIC, BODY_TO_MIND, TELEPORT_TO_MATHER, TRIPLE_ARROW,
					ELEMENTAL_FALL_DOWN, COUNTER_MIRROR, SOUL_BARRIER, INFERNO, CLEAR_MIND, RESIST_ELEMENTAL, MAFR, RETURN_TO_NATURE,
					ELEMENTAL_PROTECTION, EARTH_WEAPON, AQUA_SHOT, EAGGLE_EYE, FIRE_SHIELD, QUAKE, ERASE_MAGIC, LESSER_ELEMENTAL, DANCING_BLADES, STORM_EYE, EARTH_BIND, NATURES_TOUCH, EARTH_GUARDIAN,
					AQUA_PROTECTER, AREA_OF_SILENCE, GREATER_ELEMENTAL, BURNING_WEAPON, NATURES_BLESSING, CALL_OF_NATURE, STORM_SHOT, CYCLONE, EXOTIC_VITALIZE, WATER_LIFE, ELEMENTAL_FIRE, STORM_WALK,
					POLLUTE_WATER, STRIKER_GALE, SOUL_OF_FLAME, ADDITIONAL_FIRE, DRAGON_SKIN, BURNING_SLASH, DESTROY, MAGMA_BREATH, SCALES_EARTH_DRAGON, BLOOD_LUST, FOU_SLAYER, MAGMA_ARROW,
					SCALES_WATER_DRAGON, MORTAL_BODY, THUNDER_GRAB, EYE_OF_DRAGON, SCALES_FIRE_DRAGON, MIRROR_IMAGE, CONFUSION, SMASH, IllUSION_OGRE, CUBE_OGRE, CONCENTRATION, MIND_BREAK, BONE_BREAK,
					CUBE_GOLEM, PATIENCE, PHANTASM, IZE_BREAK, CUBE_RICH, INSIGHT, PANIC, REDUCE_WEIGHT, IllUSION_AVATAR, CUBE_AVATAR, TEMPEST, ENSNARE, OSIRIS, PC_EXP_UP, TARAS_ATTACK_SPEED, TARAS_MOVE_SPEED,
					VISION_TELEPORT, BURNING_SHOT, TYRANT };
			int check = 0;
			for (int chskill : CheckSkillID) {
				if (chskill == skillid) {
					check = chskill;
					break;
				}
			}

			if (player.getBuffnoch() == 0) {
				if (check != 0) {
					if (!SkillCheck.getInstance().CheckSkill(player, check) && player.getAI() == null) {
						return false;
					}
				}
			}
		}

		// 針對存在BUG的額外添加
		if (player instanceof L1PcInstance) {
			L1PcInstance jonje = L1World.getInstance().getPlayer(player.getName());
			if (jonje == null && player.getAccessLevel() != 200) {
				player.sendPackets(new S_SystemMessage("存在BUG強制終止！請重新連接"));
				player.sendPackets(new S_Disconnect());
				return false;
			}
		}

		setCheckedUseSkill(true);
		_targetList = new ArrayList<TargetStatus>();

		_skill = SkillsTable.getInstance().getTemplate(skillid);
		_skillId = skillid;
		_targetX = x;
		_targetY = y;
		_skillTime = time;
		_type = type;
		boolean checkedResult = true;
		if (attacker == null) {
			// pc
			_player = player;
			_user = _player;
		} else {
			// npc
			_npc = (L1NpcInstance) attacker;
			_user = _npc;
		}

		if (_skill == null || _skill.getTarget() == null) {
			/*
			 * try { throw new Exception(); }catch(Exception e) { e.printStackTrace(); }
			 */
			// System.out.println(String.format("NULL 원인 스킬아이디 : ", skillid + "/" +
			// _skill));
			return false;
		}
		if (_skill.getTarget().equals("none")) {
			_targetID = _user.getId();
			_targetX = _user.getX();
			_targetY = _user.getY();
			if (_skillId == L1SkillId.FORCE_WAVE) {
				if(_player.isSpearModeType()) {
					_targetID = target_id;
				}
			}
		} else {
			_targetID = target_id;
		}
//		System.out.println("1-2 "+skillid);
		if (type == TYPE_NORMAL) {
			checkedResult = isNormalSkillUsable();
		} else if (type == TYPE_SPELLSC) {
			checkedResult = isSpellScrollUsable();
		} else if (type == TYPE_NPCBUFF) {
			checkedResult = true;
		}
		if (!checkedResult) {
			return false;
		}
		if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM ) {
			return true;
		}
		if (_skillId == VISION_TELEPORT ) {
			return true;
		}

		L1Object l1object = L1World.getInstance().findObject(_targetID);
		if (l1object instanceof L1ItemInstance) {
			_log.fine("skill target item name: " + ((L1ItemInstance) l1object).getViewName());
			return false;
		}
		if (l1object instanceof L1ClanJoinInstance)
			return false;

		if (_user instanceof L1PcInstance) {
			if (l1object instanceof L1PcInstance) {
				_calcType = PC_PC;
			} else {
				_calcType = PC_NPC;
				_targetNpc = (L1NpcInstance) l1object;
			}
		} else if (_user instanceof L1NpcInstance) {
			if (l1object instanceof L1PcInstance) {
				_calcType = NPC_PC;
			} else if (_skill.getTarget().equals("none")) {
				_calcType = NPC_PC;
			} else {
				_calcType = NPC_NPC;
				_targetNpc = (L1NpcInstance) l1object;
			}
		}

		if (_targetNpc != null && _targetNpc instanceof MJCompanionInstance) {
			if (_skill.getTarget().equals("buff"))
				return false;
		}
		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TRUE_TARGET) {
			_bookmark_mapid = (short) target_id;
			_bookmark_x = x;
			_bookmark_y = y;
		}
		if (_skillId == SUMMON_MONSTER) {
			_bookmark_x = x;
		}

		if (_skillId == BRING_STONE || _skillId == BLESSED_ARMOR || _skillId == ENCHANT_WEAPON || _skillId == SHADOW_FANG) {
			_itemobjid = target_id;
		}
		_target = (L1Character) l1object;

		if (!(_target instanceof L1MonsterInstance) && _skill.getTarget().equals("attack") && _user.getId() != target_id) {
			_isPK = true;
		}
		if (!(l1object instanceof L1Character)) {
			checkedResult = false;
		}

		makeTargetList();
		if (_targetList.size() == 0 && (_user instanceof L1NpcInstance)) {
			checkedResult = false;
		}
//		System.out.println("1-3 "+skillid);
		return checkedResult;
	}

	/**
	 * 통상의 스킬 사용시에 사용자 상태로부터 스킬이 사용 가능한가 판단한다
	 *
	 * @return false 스킬이 사용 불가능한 상태인 경우
	 */
	private boolean isNormalSkillUsable() {
		if (_user instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _user;

			if (pc.isParalyzed()) {
				return false;
			}
			if ((pc.isInvisble() || pc.isInvisDelay()) && !_skill.isCanCastWithInvis()) {
				return false;
			}

			if (!pc.is_top_ranker() && pc.getInventory().getWeight100() > 82) { // 중량
				pc.sendPackets(new S_ServerMessage(316)); // 무게 게이지가 82%를 넘어 마법을 쓸 수 없습니다.
				return false;
			}

			int polyId = pc.getCurrentSpriteId();
			L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
			if (poly != null && !poly.canUseSkill()) {
				pc.sendPackets(new S_ServerMessage(285));
				return false;
			}

			// TODO 在攻城戰中無法使用的技能列表
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			L1Skills skill = SkillsTable.getInstance().getTemplate(_skillId);
			if (castle_id != 0) {
				if (skill != null) {
					if (!skill.is_Castle_Magic()) {
						pc.sendPackets(new S_SystemMessage("無法在攻城戰中使用。"));
						return false;
					}
				}
			}

			// TODO 在安全區域無法使用的技能列表
			if (pc.getMap().isSafetyZone(pc.getLocation())) {
				if (skill != null) {
					if (!skill.is_SafetyZone_Magic()) {
						pc.sendPackets(new S_SystemMessage("無法在村莊中使用。"));
						return false;
					}
				}
			}

			if (!isAttrAgrees()) {
				return false;
			}

			if (_skillId == ELEMENTAL_PROTECTION && pc.getElfAttr() == 0) {
				pc.sendPackets(new S_ServerMessage(280));
				return false;
			}

			int group_id = SpriteInformationLoader.getInstance().getUseSpellGroupId(_skillId);
			if (group_id > 0) {
				if (pc.isLinkSkillDelay())
					return false;
			} else {
				if (pc.isSkillDelay()) {
					return false;
				}
			}

			if (_skillId == TRUE_TARGET) {
			} else if ((pc.hasSkillEffect(SILENCE) || pc.hasSkillEffect(AREA_OF_SILENCE) || pc.hasSkillEffect(STATUS_POISON_SILENCE) || pc.hasSkillEffect(CHAINSWORD_STUN))
					&& (_skillId < SHOCK_STUN || _skillId > COUNTER_BARRIER || _skillId != EMPIRE)) { // 사일런스상태에서도
				pc.sendPackets(new S_ServerMessage(285));
				return false;
			}

			if (_skillId == INFERNO) {
				if (pc.getWeapon() != null) { // 추가
					if (pc.getWeapon().getItem().getType() != 1) {
						pc.sendPackets(new S_ServerMessage(1413));
						return false;
					}
				}
			}

			if (_skillId == HALPAS) {
				if (pc.getWeapon() != null) { // 추가
					if (pc.getWeapon().getItem().getType() != 18) {
						pc.sendPackets(new S_ServerMessage(1413));
						return false;
					}
				}
			}

			if (_skillId == COUNTER_BARRIER || _skillId == SHOCK_STUN || _skillId == FORCE_STUN || _skillId == FORCE_STUN_FAIL) {
				if (pc.getWeapon() != null) { // 추가
					if (pc.getWeapon().getItem().getType() != 3) {
						pc.sendPackets(new S_ServerMessage(1413));
						return false;
					}
				}
			}
			if (_skillId == EMPIRE) {
				if (pc.getWeapon() != null) { // 추가
					if (pc.getInventory().getTypeEquipped(2, 7) <= 0) {
						pc.sendPackets(new S_ServerMessage(1413));
						return false;
					}
				}
			}
			if (_skillId == DANCING_BLADES || _skillId == SAND_STORM) {
				if (pc.getWeapon() == null) {
					pc.sendPackets(new S_SystemMessage("裝備武器（劍）時可使用。"));
					return false;
				}
				if (pc.getWeapon().getItem().getType() != 1 && pc.getWeapon().getItem().getType() != 2) {
					pc.sendPackets(new S_SystemMessage("裝備武器（劍）時可使用。"));
					return false;
				}
			}

			if (pc.hasSkillEffect(CONFUSION)) {
				pc.sendPackets(new S_ServerMessage(285));
				return false;
			}

			if (isItemConsume() == false && !_player.isGm()) {
				_player.sendPackets(new S_ServerMessage(299));
				return false;
			}
		} else if (_user instanceof L1NpcInstance) {

			if (_skillId == TRUE_TARGET) {
			} else if (_user.hasSkillEffect(CONFUSION)) {
				_user.removeSkillEffect(CONFUSION);
				return false;
			} else if (_user.hasSkillEffect(SILENCE)) {
				_user.removeSkillEffect(SILENCE);
				return false;
			}
		}

		if (!isHPMPConsume()) {
			return false;
		}
		return true;
	}

	private boolean isSpellScrollUsable() {
		L1PcInstance pc = (L1PcInstance) _user;

		if (pc.isParalyzed()) {
			return false;
		}

		if ((pc.isInvisble() || pc.isInvisDelay()) && !_skill.isCanCastWithInvis()) {
			return false;
		}

		return true;
	}

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, String message, int timeSecs, int type) {
		L1Character attacker = null;
		handleCommands(player, skillId, targetId, x, y, message, timeSecs, type, attacker);
	}

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, String message, int timeSecs, int type, L1Character attacker) {
//		System.out.println("확인1 "+skillId);
		try {
			if (!isCheckedUseSkill()) {
//				System.out.println("확인2 ");
				boolean isUseSkill = checkUseSkill(player, skillId, targetId, x, y, message, timeSecs, type, attacker);
				if (skillId == SHADOW_STEP){
					if (player.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
						runSkill();

					}

				} else if (!isUseSkill) {
//					System.out.println("확인3 ");
					failSkill();
					return;
				}
			}

			/** 2016.11.26 MJ 앱센터 LFC **/
			if (player != null && (player instanceof L1PcInstance) && _skill != null) {
				if (_skill.getType() != L1Skills.TYPE_CHANGE && _skill.getType() != L1Skills.TYPE_HEAL && player.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY)
					return;
			}
//			System.out.println("2 "+skillId);
			switch (type) {
				case TYPE_NORMAL:
					if (!_isGlanceCheckFail || _skill.getArea() > 0 || _skill.getTarget().equals("none")) {
						if (skillId == DANCING_BLADES || skillId == SAND_STORM || skillId == HURRICANE || skillId == L1SkillId.FOCUS_WAVE) {
							// 특정스킬 시간초과
							sendGrfx(true);
							runSkill();
							useConsume();
							sendFailMessageHandle();
							setDelay();
						} else {
							runSkill();
							useConsume();
							sendGrfx(true);
							sendFailMessageHandle();
							setDelay();
						}
					}
					if (_isGlanceCheckFail){
						if (skillId == SHADOW_STEP){
							if (_user.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
								_isGlanceCheckFail = false;
								runSkill();
							}
						}
					}
					break;
				case TYPE_LOGIN:
//				System.out.println("확인4-1 ");
					runSkill();
					break;
				case TYPE_SPELLSC:
//				System.out.println("확인4-2 ");
					runSkill();
					sendGrfx(true);
					setDelay();
					break;
				case TYPE_GMBUFF:
//				System.out.println("확인4-3 ");
					runSkill();
					sendGrfx(false);
					break;
				case TYPE_NPCBUFF:
//				System.out.println("확인4-4 ");
					runSkill();
					sendGrfx(true);
					break;
				default:
					break;
			}
			setCheckedUseSkill(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void failSkill() {
		setCheckedUseSkill(false);
		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TELEPORT_TO_MATHER || _skillId == VISION_TELEPORT) {
			_player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		}
	}

	private boolean isTarget(L1Character cha) throws Exception {
		boolean _flg = false;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isGhost() || pc.isGmInvis()) {
				return false;
			}
		}
		if (_calcType == NPC_PC && (cha instanceof L1PcInstance || cha instanceof L1PetInstance || cha instanceof L1SummonInstance || cha instanceof MJCompanionInstance)) {
			_flg = true;
		}

		if (cha instanceof L1DoorInstance) {
			if (cha.getMaxHp() == 0 || cha.getMaxHp() == 1) {
				return false;
			}
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_PC && cha instanceof L1PcInstance && _user instanceof L1SummonInstance) {
			L1SummonInstance summon = (L1SummonInstance) _user;
			if (cha.getId() == summon.getMaster().getId()) {
				return false;
			}
			if (cha.getZoneType() == 1) {
				return false;
			}
		}

		if (_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK && _calcType == NPC_PC) {
			if (cha instanceof L1PcInstance) {
				if (_user instanceof L1PetInstance) {
					if (cha.getZoneType() == 1 || cha.getId() == ((L1PetInstance) _user).getMaster().getId())
						return false;
				} else if (_user instanceof MJCompanionInstance) {
					if (cha.getZoneType() == 1 || cha.getId() == ((MJCompanionInstance) _user).get_master_id())
						return false;
				}
			}
		}

		if (cha instanceof L1DollInstance && _skillId != HASTE) {
			return false;
		}

		if (_calcType == PC_NPC && _target instanceof L1NpcInstance && !(_target instanceof MJCompanionInstance) && !(_target instanceof L1PetInstance) && !(_target instanceof L1SummonInstance)
				&& (cha instanceof L1PetInstance || cha instanceof MJCompanionInstance || cha instanceof L1SummonInstance || cha instanceof L1PcInstance)) {
			return false;
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_PC && !(cha instanceof L1PetInstance) && !(cha instanceof L1SummonInstance)
				&& !(cha instanceof L1PcInstance) && !(cha instanceof MJCompanionInstance)) {
			return false;
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_NPC && _user instanceof L1MonsterInstance && cha instanceof L1MonsterInstance) {
			return false;
		}

		if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK
				&& (cha instanceof L1AuctionBoardInstance || cha instanceof L1BoardInstance || cha instanceof L1CrownInstance || cha instanceof L1DwarfInstance || cha instanceof L1EffectInstance
				|| cha instanceof L1FieldObjectInstance || cha instanceof L1FurnitureInstance || cha instanceof L1HousekeeperInstance || cha instanceof L1MerchantInstance
				|| cha instanceof L1TeleporterInstance)) {
			return false;
		}

		if (_skill.getType() == L1Skills.TYPE_ATTACK && cha.getId() == _user.getId()) {
			return false;
		}

		if (cha.getId() == _user.getId() && _skillId == HEAL_ALL) {
			return false;
		}

		if (((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC || (_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN
				|| (_skill.getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY) && cha.getId() == _user.getId() && _skillId != HEAL_ALL) {
			return true;
		}

		if (_user instanceof L1PcInstance && (_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _isPK == false) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (_player.getId() == summon.getMaster().getId()) {
					return false;
				}
			} else if (cha instanceof MJCompanionInstance) {
				if (_player.getId() == ((MJCompanionInstance) cha).get_master_id())
					return false;
			} else if (cha instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) cha;
				if (_player.getId() == pet.getMaster().getId()) {
					return false;
				}
			}
		}
		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && !(cha instanceof L1MonsterInstance) && _isPK == false && cha instanceof L1PcInstance) {
			L1PcInstance enemy = (L1PcInstance) cha;
			if (_skillId == COUNTER_DETECTION && enemy.getZoneType() != 1 && (cha.hasSkillEffect(INVISIBILITY) || cha.hasSkillEffect(BLIND_HIDING))) {
				if (cha.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())){
					int _time = 0;
					_time = _target.getSkillEffectTimeSec(L1SkillId.BLIND_HIDING);
					if (16000 - (_time * 1000) < 3000){
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}

			}

			if (_skillId == TEMPEST && enemy.getZoneType() != 1) {
				return true;
			}
			if (_player != null) {
				if (MJWar.isSameWar(_player.getClan(), enemy.getClan()))
					return L1CastleLocation.checkInAllWarArea(enemy.getX(), enemy.getY(), enemy.getMapId());
			}

			if (_npc == null && !(_user instanceof L1NpcInstance))
				return false;
		}
		if (_user.glanceCheck(cha.getX(), cha.getY()) == false && _skill.getIsThrough() == false) {
			if (_skillId == SHADOW_STEP){
				if (_user.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
					_skill.setIsThrough(1);
					_isGlanceCheckFail = false;
					return true;
				}
			} else if (!(_skill.getType() == L1Skills.TYPE_CHANGE || _skill.getType() == L1Skills.TYPE_RESTORE)) {
				_isGlanceCheckFail = true;
				return false;
			}
		}
		/** 아이스랜스 중이라면 디버프 안걸리게 **/
		if ((cha.hasSkillEffect(ICE_LANCE)) && (_skillId == SHOCK_STUN || _skillId == CHAINSWORD_STUN || _skillId == EMPIRE || _skillId == DECAY_POTION || _skillId == WEAPON_BREAK || _skillId == SLOW
				|| _skillId == CURSE_PARALYZE || _skillId == MANA_DRAIN || _skillId == DARKNESS || _skillId == FOG_OF_SLEEPING || _skillId == ARMOR_BRAKE || _skillId == EARTH_BIND
				|| _skillId == WIND_SHACKLE || _skillId == POLLUTE_WATER || _skillId == STRIKER_GALE || _skillId == DESTROY || _skillId == PANIC || _skillId == IllUSION_AVATAR || _skillId == DESPERADO
				|| _skillId == POWERRIP || _skillId == PANTHERA || _skillId == PHANTOM || _skillId == FORCE_STUN || _skillId == ETERNITI || _skillId == TEMPEST || _skillId == FORCE_STUN_FAIL
				|| _skillId == DISINTEGRATE || _skillId == ENSNARE || _skillId == OSIRIS)) {
			return false;
		}

		if (cha.hasSkillEffect(EARTH_BIND) && _skillId != CANCELLATION) {
			return false;
		}

		// 데페 중에 데페가 들어오면 기존 데페는 삭제.
		if (cha.hasSkillEffect(DESPERADO) && _skillId == DESPERADO)
			_target.removeSkillEffect(L1SkillId.DESPERADO);

		if (cha.hasSkillEffect(TEMPEST) && _skillId == TEMPEST)
			_target.removeSkillEffect(L1SkillId.TEMPEST);

		if (cha.hasSkillEffect(ETERNITI) && _skillId == ETERNITI)
			_target.removeSkillEffect(L1SkillId.ETERNITI);

		if (cha.hasSkillEffect(MOB_BASILL) && _skillId == MOB_BASILL) {
			return false; // 바실굳기중에 바실굳기
		}
		if (cha.hasSkillEffect(MOB_COCA) && _skillId == MOB_COCA) {
			return false; // 코카굳기중에 코카굳기
		}

		if (!(cha instanceof L1MonsterInstance) && (_skillId == TAMING_MONSTER || _skillId == CREATE_ZOMBIE)) {
			return false;
		}
		if (cha.isDead() && (_skillId != CREATE_ZOMBIE && _skillId != RESURRECTION && _skillId != GREATER_RESURRECTION && _skillId != CALL_OF_NATURE)) {
			return false;
		}

		if (cha.isDead() == false && (_skillId == CREATE_ZOMBIE || _skillId == RESURRECTION || _skillId == GREATER_RESURRECTION || _skillId == CALL_OF_NATURE)) {
			return false;
		}

		if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance)
				&& (_skillId == CREATE_ZOMBIE || _skillId == RESURRECTION || _skillId == GREATER_RESURRECTION || _skillId == CALL_OF_NATURE)) {
			return false;
		}

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {// 앱솔중
				if (_skillId == CURSE_BLIND || _skillId == WEAPON_BREAK || _skillId == DARKNESS || _skillId == WEAKNESS || _skillId == DISEASE || _skillId == FOG_OF_SLEEPING || _skillId == SLOW
						|| _skillId == CANCELLATION || _skillId == SILENCE || _skillId == DECAY_POTION || _skillId == MASS_TELEPORT || _skillId == DETECTION || _skillId == IZE_BREAK
						|| _skillId == COUNTER_DETECTION || _skillId == DESTROY || _skillId == ERASE_MAGIC || _skillId == PHYSICAL_ENCHANT_DEX || _skillId == PHYSICAL_ENCHANT_STR
						|| _skillId == BLESS_WEAPON || _skillId == IMMUNE_TO_HARM || _skillId == REMOVE_CURSE || _skillId == CONFUSION || _skillId == Sand_worms || _skillId == erzabe_worms
						|| _skillId == Sand_worms1 || _skillId == Sand_worms2 || _skillId == Sand_worms3 || _skillId == MOB_SLOW_1 || _skillId == MOB_SLOW_18 || _skillId == MOB_WEAKNESS_1
						|| _skillId == MOB_DISEASE_1 || _skillId == MOB_BASILL || _skillId == MOB_SHOCKSTUN_30 || _skillId == MOB_RANGESTUN_19 || _skillId == MOB_RANGESTUN_18
						|| _skillId == MOB_DISEASE_30 || _skillId == MOB_WINDSHACKLE_1 || _skillId == MOB_COCA || _skillId == MOB_CURSEPARALYZ_19 || _skillId == MOB_CURSEPARALYZ_18
						|| _skillId == MOB_CURSEPARALYZ1 || _skillId == Mob_RANGESTUN_30 || _skillId == ANTA_MESSAGE_1 || _skillId == ANTA_MESSAGE_2 || _skillId == ANTA_MESSAGE_3
						|| _skillId == ANTA_MESSAGE_4 || _skillId == ANTA_MESSAGE_5 || _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8
						|| _skillId == ANTA_MESSAGE_9 || _skillId == ANTA_MESSAGE_10 || _skillId == OMAN_STUN || _skillId == BOS_STUN18 || _skillId == Maeno_STUN || _skillId == Besi_STUN
						|| _skillId == fornos_STUN || _skillId == Moster_STUN || _skillId == MOSTER_STUN_1 || _skillId == OMAN_CANCELLATION || _skillId == BALOCH_STUN
						|| _skillId == DRAGON_HALPAS_STUN ) {
					return true;
				} else {
					return false;
				}
			}
		}
		if (cha instanceof L1NpcInstance) {
			int hiddenStatus = ((L1NpcInstance) cha).getHiddenStatus();
			if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
				if (_skillId == DETECTION || _skillId == IZE_BREAK || _skillId == EYE_OF_DRAGON || _skillId == COUNTER_DETECTION) {
					return true;
				} else {
					return false;
				}
			} else if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) {
				return false;
			}
		}

		if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC && cha instanceof L1PcInstance) {
			_flg = true;
		} else if ((_skill.getTargetTo() & L1Skills.TARGET_TO_NPC) == L1Skills.TARGET_TO_NPC
				&& (cha instanceof L1MonsterInstance || cha instanceof L1NpcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance)) {
			_flg = true;
		} else if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PET) == L1Skills.TARGET_TO_PET && _user instanceof L1PcInstance) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.getMaster() != null) {
					if (_player.getId() == summon.getMaster().getId()) {
						if (_skillId != L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					} else {
						if (_skillId == L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					}
				}
			}

			if (cha instanceof MJCompanionInstance) {
				MJCompanionInstance companion = (MJCompanionInstance) cha;
				if (companion.get_master() != null) {
					if (_player.getId() == companion.get_master_id()) {
						if (_skillId != L1SkillId.RETURN_TO_NATURE)
							_flg = true;
					} else {
						if (_skillId == L1SkillId.RETURN_TO_NATURE)
							_flg = true;
					}
				}
			}

			if (cha instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) cha;
				if (pet.getMaster() != null) {
					if (_player.getId() == pet.getMaster().getId()) {
						if (_skillId != L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					} else {
						if (_skillId == L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					}
				}
			}
		}

		if (_calcType == PC_PC && cha instanceof L1PcInstance) {
			try {
				if ((_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN
						&& ((_player.getClanid() != 0 && _player.getClanid() == ((L1PcInstance) cha).getClanid()) || _player.isGm())) {
					return true;
				}
				if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY && (_player.getParty().isMember((L1PcInstance) cha) || _player.isGm())) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		if (cha instanceof L1PcInstance || cha instanceof L1MonsterInstance) {
			if(_skill.getSkillId() == L1SkillId.SHADOW_STEP) {
				if (_player.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())) {
					_flg = true;
				}
			}
		}

		return _flg;
	}

	private void EffectSpawn() { // 이펙트 스폰 타입별 나누자
		int Effect = 0;
		if (_skillId == DESERT_SKILL4 || _skillId == ZENITH_Poison || _skillId == doctor_skills) {
			Effect = 5137;
		}
		int xx = 0;
		int yy = 0;
		int xx1 = 0;
		int yy1 = 0;
		int xx2 = 0;
		int yy2 = 0;
		int xx3 = 0;
		int yy3 = 0;
		int xx4 = 0;
		int yy4 = 0;
		int randomxy = random.nextInt(4);
		int r = random.nextInt(2) + 1;
		int a1 = 3 + randomxy;
		int a2 = -3 - randomxy;
		int b1 = 2 + randomxy;
		int b2 = -2 - randomxy;
		int heading = _npc.getHeading(); // 몹 방향
		switch (heading) {
			case 1:
				xx = a1 - r;
				yy = a2 + r;
				yy1 = a2;
				xx2 = a1;
				xx3 = a2;
				yy3 = b2;
				xx4 = b1;
				yy4 = a1;
				break;
			case 2:
				xx = a1 + 1;
				xx1 = b1;
				yy1 = a2;
				xx2 = b1;
				yy2 = a1;
				xx3 = b1 - 3;
				yy3 = a2 - 2;
				xx4 = b1 - 2;
				yy4 = a1 + 3;
				break;
			case 3:
				xx = a1 - r;
				yy = a1 - r;
				xx1 = a1;
				yy2 = a1;
				xx3 = a1;
				yy3 = a2;
				xx4 = a2;
				yy4 = b1;
				break;
			case 4:
				yy = a1 + 1;
				xx1 = a1;
				yy1 = b1;
				xx2 = a2;
				yy2 = b1;
				xx3 = a1 + 3;
				yy3 = b1 - 3;
				xx4 = a2 - 3;
				yy4 = b1 - 3;
				break;
			case 5:
				xx = a2 + r;
				yy = a1 - r;
				yy1 = a1;
				xx2 = a2;
				xx3 = a1;
				yy3 = b1;
				xx4 = b2;
				yy4 = a2;
				break;
			case 6:
				xx = a2 - 1;
				xx1 = b2;
				yy1 = a1;
				xx2 = b2;
				yy2 = a2;
				xx3 = b2 + 3;
				yy3 = a1 + 2;
				xx4 = b2 + 2;
				yy4 = a2 - 3;
				break;
			case 7:
				xx = a2 + r;
				yy = a2 + r;
				xx1 = a2;
				yy2 = a2;
				xx3 = a2;
				yy3 = a1;
				xx4 = a1;
				yy4 = b2;
				break;
			case 0:
				yy = a2 - 1;
				xx1 = a2;
				yy1 = b2;
				xx2 = a1;
				yy2 = b2;
				xx3 = a2 - 3;
				yy3 = b2 + 3;
				xx4 = a1 + 3;
				yy4 = b2 + 3;
				break;
			default:
				break;
		}
		int x = _npc.getX() + xx;
		int y = _npc.getY() + yy;
		// 마름모 4*4픽셀 모양 (몹 기준에서 정면에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y + 1, _user.getMapId());
		int x1 = _npc.getX() + xx1;
		int y1 = _npc.getY() + yy1;
		// 마름모 4*4픽셀 모양 (몹 기준에서 좌측에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1 + 1, _user.getMapId());
		int x2 = _npc.getX() + xx2;
		int y2 = _npc.getY() + yy2;
		// 마름모 4*4픽셀 모양 (몹 기준에서 우측에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2 + 1, _user.getMapId());
		int x3 = _npc.getX() + xx3;
		int y3 = _npc.getY() + yy3;
		// 마름모 4*4픽셀 모양 (몹 기준에서 좌측2에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3 + 1, _user.getMapId());
		int x4 = _npc.getX() + xx4;
		int y4 = _npc.getY() + yy4;
		// 마름모 4*4픽셀 모양 (몹 기준에서 우측2에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4 + 1, _user.getMapId());
		return;
	}

	private void makeTargetListTempest(){
//		System.out.println("오나?1");
		for(L1Object tgobj : L1World.getInstance().getVisibleObjects(_player, _skill.getArea())){
	/*		if (tgobj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) tgobj;
				System.out.println("범위내 pc"+pc.getName());
			}*/
			if(tgobj == null || !isEnemyTarget(tgobj))continue;
			_targetList.add(new TargetStatus((L1Character) tgobj));
			if(tgobj instanceof L1PcInstance)	((L1PcInstance) tgobj).send_effect(20604);
			else								((L1Character) tgobj).broadcastPacket(new S_SkillSound(tgobj.getId(), 20604), true);
		}

//		System.out.println(_targetList.size());
/*		if (_targetList.size() != 0) {
			for (int i = 0; i < _targetList.size();i++) {
				System.out.println(_targetList.get(i).getTarget().getName());
			}

		}*/
	}
	private boolean isEnemyTarget(L1Object obj) {// 적으로 간주할 케릭터 체크
		try{
			if(!(obj instanceof L1MonsterInstance || obj instanceof L1PcInstance /*|| obj instanceof L1PeopleInstance*/ || obj instanceof L1SummonInstance || obj instanceof L1PetInstance)) {
				return false;
			}
			L1Character cha = (L1Character) obj;
			if(cha.getId() == _player.getId()) {
				return false;
			}
			if((cha instanceof L1PcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) && cha.getZoneType() == 1) {
				return false;// 세이프 존 체크
			}
			if(cha instanceof L1PcInstance && ((((L1PcInstance)cha).getClanid() > 0 && ((L1PcInstance)cha).getClanid() == _player.getClanid()) || (_player.getParty() != null && _player.getParty().isMember((L1PcInstance)cha)))) {
				return false;// 같은 혈맹원, 같은 파티원 제외
			}
//			System.out.println("이까지는 오겠지..");
			if(!isTarget(cha) || !_player.glanceCheck(cha.getX(), cha.getY())) {
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void makeTargetList() {
		try {

			if (_type == TYPE_LOGIN) {
				_targetList.add(new TargetStatus(_user));
				return;
			}
			if (_skill.getTargetTo() == L1Skills.TARGET_TO_ME && (_skill.getType() & L1Skills.TYPE_ATTACK) != L1Skills.TYPE_ATTACK) {
				_targetList.add(new TargetStatus(_user));
				return;
			}

			if (_target == null)
				return;
			if (_skillId == TEMPEST && _user instanceof L1PcInstance) {
				makeTargetListTempest();
				return;

			}

			if (_skill.getRanged() != -1) {// 사정거리 -1 화면내 오브젝트만
				int skillranged = _skill.getRanged();
				if(_skillId == PANTHERA && _user instanceof L1PcInstance && _player.isPassive(MJPassiveID.PANTERA_SHOCK.toInt())){
					skillranged += 1;
				}else if(_skillId == CRUEL && _user instanceof L1PcInstance && _player.isPassive(MJPassiveID.CRUEL_CONBICTION.toInt())){
					skillranged += 1;
				}else if(_skillId == SHADOW_STEP && _user instanceof L1PcInstance && _player.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
					skillranged += 1;
				}
				_skill.setRanged(skillranged);
			}else{
				if(!_user.getLocation().isInScreen(_target.getLocation()))return;// 화면 밖
			}

			if (isTarget(_target) == false && !(_skill.getTarget().equals("none"))) {
				return;
			}

			if (_skillId == LIGHTNING) {
				ArrayList<L1Object> olist = L1World.getInstance().getVisibleLineObjects(_user, _target);
				if (olist == null)
					return;
				for (L1Object tgobj : olist) {
					if (tgobj == null) {
						continue;
					}
					if (!(tgobj instanceof L1Character)) {
						continue;
					}
					L1Character cha = (L1Character) tgobj;
					if (isTarget(cha) == false) {
						continue;
					}
					_targetList.add(new TargetStatus(cha));
				}
				return;
			}

			if (_skillId == OSIRIS) {
				ArrayList<L1Object> olist = L1World.getInstance().getVisibleLineObjects(_user, _target);
				if (olist == null)
					return;
				for (L1Object tgobj : olist) {
					if (tgobj == null) {
						continue;
					}
					if (!(tgobj instanceof L1Character)) {
						continue;
					}
					L1Character cha = (L1Character) tgobj;
					if (isTarget(cha) == false) {
						continue;
					}
					if (_targetList.size() < 3 ){
						_targetList.add(new TargetStatus(cha));
					}
				}
				return;
			}


			if (_skillId == IMMUNE_TO_HARM || _skillId == MATH_IMMUNE_TO_HARM) {
				if (_user.glanceCheck(_target.getX(), _target.getY()) == false) {
					return;
				}
			}

			if (_skill.getArea() == 0) {
//				if (_user.glanceCheck(_target.getX(), _target.getY()) == true){
//					if (_skillId == SHADOW_STEP){
//						if (_user.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){

//						}
//					}
//				}

				if (_user.glanceCheck(_target.getX(), _target.getY()) == false) {
					if ((_skill.getType() & L1Skills.TYPE_ATTACK) == L1Skills.TYPE_ATTACK) {
						_targetList.add(new TargetStatus(_target, false));
						return;
					}
				}

				_targetList.add(new TargetStatus(_target));
			} else {

				if (!_skill.getTarget().equals("none")) {
					_targetList.add(new TargetStatus(_target));
				}

				if (_skillId != 49 && !(_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK)) {
					_targetList.add(new TargetStatus(_user));
				}

				List<L1Object> objects;
				if (_skill.getArea() == -1) {
					objects = L1World.getInstance().getVisibleObjects(_user);
				} else {
					objects = L1World.getInstance().getVisibleObjects(_target, _skill.getArea());
				}
				for (L1Object tgobj : objects) {
					if (tgobj == null) {
						continue;
					}
					if (!(tgobj instanceof L1Character)) {
						continue;
					}
					L1Character cha = (L1Character) tgobj;
					if (!isTarget(cha)) {
						continue;
					}

					if (_skillId == METEOR_STRIKE || _skillId == ICE_METEOR_STRIKE) {
						if (cha instanceof L1PcInstance && _user instanceof L1PcInstance) {
							boolean isNowWar = false;
							int castleId = L1CastleLocation.getCastleIdByArea((L1PcInstance) _user);
							if (castleId != 0) {
								isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
							}
							if (isNowWar == false) {
								continue;
							}
						}
					}
					_targetList.add(new TargetStatus(cha));
				}
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendHappenMessage(L1PcInstance pc) {
		int msgID = _skill.getSysmsgIdHappen();
		if (msgID > 0) {
			pc.sendPackets(new S_ServerMessage(msgID));
		}
	}

	private void sendFailMessageHandle() {
		if (_skill.getType() != L1Skills.TYPE_ATTACK && !_skill.getTarget().equals("none") && _targetList.size() == 0) {
			sendFailMessage();
		}
	}

	private void sendFailMessage() {
		int msgID = _skill.getSysmsgIdFail();
		if (msgID > 0 && (_user instanceof L1PcInstance)) {
			_player.sendPackets(new S_ServerMessage(msgID));
		}
	}

	private boolean isAttrAgrees() {
		int magicattr = _skill.getAttr();
		if (_user instanceof L1NpcInstance) {
			return true;
		}

		// 1 火 2 水 3 火/水 4 風 5 風/火 6 水/風 7 水/風/火 8 地 9 地/火 10 地/水 11 地/水/火 12 地/風
		// 13 地/風/火 14 地/水/風 15 全屬性
		if (_player.isElf()) {
			if (/* !_player.isGm() && */ (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && magicattr != 0) && magicattr != 0 && _player.getElfAttr() != magicattr
					&& _player.getGlory_Earth_Attr() != magicattr) {
				if (_player.getElfAttr() == 0 || _player.getElfAttr() == 1 || _player.getElfAttr() == 2 || _player.getElfAttr() == 4 || _player.getElfAttr() == 8) { // 屬性值
					_player.sendPackets(3134);

					// 精靈一般系技能客戶端錯誤臨時措施（本服客戶端錯誤解決時註釋）開始
					_player.sendPackets(new S_SkillIconGFX(15, _player.getElfAttr() + _player.getGlory_Earth_Attr()));
					// 精靈一般系技能客戶端錯誤臨時措施（本服客戶端錯誤解決時註釋）結束
					return false;
				}
			}
		}

		return true;
	}

	private boolean isHPMPConsume() {
		_mpConsume = _skill.getMpConsume();
		_hpConsume = _skill.getHpConsume();
		int currentMp = 0;
		int currentHp = 0;

		if (_user instanceof L1NpcInstance) {
			currentMp = _npc.getCurrentMp();
			currentHp = _npc.getCurrentHp();
		} else {
			currentMp = _player.getCurrentMp();
			currentHp = _player.getCurrentHp();

			double int_pre = CalcStat.calcDecreaseMp(_player.getAbility().getInt()) * 0.01;

			if (_player.hasSkillEffect(L1SkillId.MANADECREASEPOTION)){
				int_pre += 0.3;
			}
			int min_mp = (int) (_skill.getMpConsume() * int_pre);

			if (min_mp > 0) {
				_mpConsume -= min_mp;
			}

			if (_skillId == ARMOR_BRAKE && _player.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
				_mpConsume = 20;
				_hpConsume = 35;
			}
			if (_skillId == PHYSICAL_ENCHANT_DEX && _player.getInventory().checkEquipped(20013)) {
				_mpConsume /= 2;
			}
			if (_skillId == HASTE && _player.getInventory().checkEquipped(20013)) {
				_mpConsume /= 2;
			}
			if (_skillId == HEAL && _player.getInventory().checkEquipped(20014)) {
				_mpConsume /= 2;
			}
			if (_skillId == EXTRA_HEAL && _player.getInventory().checkEquipped(20014)) {
				_mpConsume /= 2;
			}
			if (_skillId == ENCHANT_WEAPON && _player.getInventory().checkEquipped(20015)) {
				_mpConsume /= 2;
			}
			if (_skillId == DETECTION && _player.getInventory().checkEquipped(20015)) {
				_mpConsume /= 2;
			}
			if (_skillId == PHYSICAL_ENCHANT_STR && _player.getInventory().checkEquipped(20015)) {
				_mpConsume /= 2;
			}
			if (_skillId == HASTE && _player.getInventory().checkEquipped(20008)) {
				_mpConsume /= 2;
			}
			if (_skillId == GREATER_HASTE && _player.getInventory().checkEquipped(20023)) {
				_mpConsume /= 2;
			}
			if (_skillId == SCALES_EARTH_DRAGON || _skillId == SCALES_WATER_DRAGON || _skillId == SCALES_FIRE_DRAGON
					|| _skillId == SCALES_RINDVIOR_DRAGON && _player.isPassive(MJPassiveID.AURAKIA.toInt())) {
				_mpConsume -= 5;
			}

			if (0 < _skill.getMpConsume()) {
				_mpConsume = Math.max(_mpConsume, 1);
			}
		}

		if (currentHp < _hpConsume + 1) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(new S_ServerMessage(279));
			}
			return false;
		} else if (currentMp < _mpConsume) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(new S_ServerMessage(278));
			}
			return false;
		}

		return true;
	}

	private boolean isItemConsume() {
		if (_player.getAI() != null)
			return true;

		int itemConsume = _skill.getItemConsumeId();
		int itemConsumeCount = _skill.getItemConsumeCount();

		if (itemConsume == 0) {
			return true;
		}

		if (itemConsume == 40318) { // 마력의 돌
			if (_player.getInventory().checkItem(30079, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 40321) { // 흑요석
			if (_player.getInventory().checkItem(30080, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 210035) { // 각인의 뼈조각
			if (_player.getInventory().checkItem(30081, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 210038) { // 속성석
			if (_player.getInventory().checkItem(30082, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 40319) { // 정령옥
			if (_player.getInventory().checkItem(30078, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		}
		if (!_player.getInventory().checkItem(itemConsume, itemConsumeCount)) {
			return false;
		}

		return true;
	}

	private void useConsume() {
		if (_user instanceof L1NpcInstance) {
			int current_hp = _npc.getCurrentHp() - _hpConsume;
			_npc.setCurrentHp(current_hp);

			int current_mp = _npc.getCurrentMp() - _mpConsume;
			_npc.setCurrentMp(current_mp);
			return;
		}

		if (isHPMPConsume()) {
			int current_hp = _player.getCurrentHp() - _hpConsume;
			_player.setCurrentHp(current_hp);

			int current_mp = _player.getCurrentMp() - _mpConsume;
			_player.setCurrentMp(current_mp);
		}

		int lawful = _player.getLawful() + _skill.getLawful();
		if (lawful > 32767) {
			lawful = 32767;
		}
		if (lawful < -32767) {
			lawful = -32767;
		}
		_player.setLawful(lawful);

		int itemConsume = _skill.getItemConsumeId();
		int itemConsumeCount = _skill.getItemConsumeCount();

		if (itemConsume == 0 || _player.getAI() != null) {
			return;
		}

		if (itemConsume == 40318) { // 마력의 돌
			if (_player.getInventory().checkItem(30079, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30079;
			}
		} else if (itemConsume == 40321) { // 흑요석
			if (_player.getInventory().checkItem(30080, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30080;
			}
		} else if (itemConsume == 210035) { // 각인의 뼈조각
			if (_player.getInventory().checkItem(30081, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30081;
			}
		} else if (itemConsume == 210038) { // 속성석
			if (_player.getInventory().checkItem(30082, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30082;
			}
		} else if (itemConsume == 40319) { // 정령옥
			if (_player.getInventory().checkItem(30078, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30078;
			}
		}
		_player.getInventory().consumeItem(itemConsume, itemConsumeCount);
	}

	private void addMagicList(L1Character cha, boolean repetition) {
		if (_skillTime == 0) {
			_getBuffDuration = _skill.getBuffDuration() * 1000;
			if (_skill.getBuffDuration() == 0) {
				if (_skillId == INVISIBILITY) {
					cha.setSkillEffect(INVISIBILITY, -1);
				}
				return;
			}
		} else {
			_getBuffDuration = _skillTime * 1000;
		}

		if (_skillId == DESPERADO || _skillId == EMPIRE || _skillId == SHOCK_STUN || _skillId == BONE_BREAK || _skillId == FORCE_STUN || _skillId == ETERNITI || _skillId == TEMPEST
				|| _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8 || _skillId == OMAN_STUN || _skillId == DRAGON_HALPAS_STUN || _skillId == BALOCH_STUN
				|| _skillId == BOS_STUN18 || _skillId == Maeno_STUN || _skillId == Besi_STUN || _skillId == fornos_STUN || _skillId == MOSTER_STUN_1 || _skillId == Moster_STUN
				|| _skillId == FORCE_STUN_FAIL || _skillId == DISINTEGRATE) {
			_getBuffDuration = _shockStunDuration;
		}

		if (_skillId == EARTH_BIND) {
			_getBuffDuration = _earthBindDuration;
		}

		if (_skillId == CURSE_POISON || _skillId == TROGIR_MILPITAS1) {
			return;
		}

		if (_skillId == CURSE_PARALYZE || _skillId == CURSE_PARALYZE2) {
			return;
		}

		if ((_skillId == ICE_LANCE) && !_isFreeze) {
			return;
		}
		if (_skillId == L1SkillId.DESPERADO) {
			return;
		}
		if (_skillId == L1SkillId.TEMPEST) {
			return;
		}
		// 별도로 setSkillEffect 호출된 상태인 스킬들
		switch (_skillId) {
			case CUBE_AVATAR:
			case CUBE_OGRE:
			case CUBE_GOLEM:
			case CUBE_RICH:
			case IMPACT:
			case PRIME:
				return;

		}

		if ((_skillId != MOB_COCA || !cha.hasSkillEffect(MOB_COCA)) || (_skillId != MOB_BASILL || !cha.hasSkillEffect(MOB_BASILL))) {
			// TODO 패시브로 인하여 지속시간 30->60초로 늘어나는 스킬류는 밑에다가 효과 부분을 따로 처리해줘야함
			if (_skillId == LUCIFER && cha.isPassive(MJPassiveID.LUCIFER_DESTINY.toInt())) {
				cha.setSkillEffect(_skillId, _getBuffDuration * 2);
			} else if (_skillId == IMMUNE_TO_HARM && cha.isPassive(MJPassiveID.IMMUNETOHARM_SAINT.toInt())) {
				cha.setSkillEffect(_skillId, _getBuffDuration * 2);
			}
			if (MonsterParalyzeDelay.getInstance().contains_paralyze(_skillId)) {
				MonsterParalyze paralyze = MonsterParalyzeDelay.getInstance().get_paralyze(_skillId);
				cha.setSkillEffect(_skillId, (paralyze.paralyze_delay + paralyze.paralyze_millis));
			} else {
				cha.setSkillEffect(_skillId, _getBuffDuration);
			}
		}

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;

			if (_skillId == INFERNO) {
				on_icons(pc, _skillId, _skill.getBuffDuration());
			} else if (_skillId == HALPAS) {
				on_icons(pc, _skillId, _skill.getBuffDuration());
			} else {
				if (repetition) {
					sendIcon(pc);
				} else {
					if (_skill.isInvenIconUse()) {
						// if (_skill.getSkillId() == REDUCTION_ARMOR &&
						// pc.isPassive(MJPassiveID.REDUCTION_ARMOR_VETERAN.toInt()))
						// on_icons(pc, _skillId, _skill.getBuffDuration());
						// else
						SC_SPELL_BUFF_NOTI.sendDatabaseIcon(pc, _skill, _getBuffIconDuration, true);

					}
				}
				if (_skillId == STRIKER_GALE) {
					int er = pc.getTotalER();
					pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, er), true);
				}
			}
		}
	}

	private void sendIcon(L1PcInstance pc) {
		if (_skillTime == 0) {
			_getBuffIconDuration = _skill.getBuffDuration();
		} else {
			_getBuffIconDuration = _skillTime;
		}
		switch (_skillId) {
			case SHIELD:
				pc.sendPackets(new S_SkillIconShield(1, _getBuffIconDuration));
				break;
			case DRESS_DEXTERITY:
				pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration));
				break;
			case DRESS_MIGHTY:
				pc.sendPackets(new S_Strup(pc, 3, _getBuffIconDuration));
				break;
			case GLOWING_WEAPON:
				pc.send_effect(11772, true);
				// pc.sendPackets(new S_SkillIconAura(113, _getBuffIconDuration));
				pc.sendPackets(new S_NewSkillIcon(11772, _getBuffIconDuration));
				break;
			// case SHINING_SHILD:
			// pc.send_effect(3941, true);
			// pc.sendPackets(new S_SkillSound(pc.getId(), 3941));// 선처리가 되야함
			// pc.sendPackets(new S_NewSkillIcon(3941, _getBuffIconDuration));
			// break;
			// case BRAVE_MENTAL:
			// // pc.sendPackets(new S_SkillIconAura(116, _getBuffIconDuration));
			// break;
			case EARTH_WEAPON:
				pc.sendPackets(new S_SkillIconAura(147, _getBuffIconDuration));
				break;
			case AQUA_SHOT:
				pc.sendPackets(new S_SkillIconAura(148, _getBuffIconDuration));
				break;
			case DANCING_BLADES:
				pc.sendPackets(new S_SkillIconAura(154, _getBuffIconDuration));
				break;
			/*
			 * case STORM_EYE: pc.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration)); break;
			 */
			/*
			 * case EARTH_GUARDIAN: pc.sendPackets(new S_SkillIconShield(7, _getBuffIconDuration)); break;
			 */
			case BURNING_WEAPON:
				pc.sendPackets(new S_SkillIconAura(162, _getBuffIconDuration));
				break;
			case STORM_SHOT:
				pc.sendPackets(new S_SkillIconAura(165, _getBuffIconDuration));
				break;
			case IRON_SKIN:
				pc.sendPackets(new S_SkillIconShield(10, _getBuffIconDuration));
				break;
			case FIRE_SHIELD:
				pc.sendPackets(new S_SkillIconShield(4, _getBuffIconDuration));
				break;
			case PHYSICAL_ENCHANT_STR:
				if (pc.getInventory().checkItem(30001398)) {
					pc.sendPackets(new S_Strup(pc, 6, _getBuffIconDuration));
				} else {
					pc.sendPackets(new S_Strup(pc, 5, _getBuffIconDuration));
				}
				break;
			case PHYSICAL_ENCHANT_DEX:
				if (pc.getInventory().checkItem(30001398)) {
					pc.sendPackets(new S_Dexup(pc, 6, _getBuffIconDuration));
				} else {
					pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration));
				}
				break;
			case 나루토감사캔디:
				if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
					pc.sendPackets(new S_Dexup(pc, 7, _getBuffIconDuration));
					pc.sendPackets(new S_Strup(pc, 7, _getBuffIconDuration));
				} else {
					pc.sendPackets(new S_Dexup(pc, 6, _getBuffIconDuration));
					pc.sendPackets(new S_Strup(pc, 6, _getBuffIconDuration));
				}
				break;
			case MOB_HASTE:
			case HASTE:
			case GREATER_HASTE:
				pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
				break;
			case HOLY_WALK:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
				break;
			case MOVING_ACCELERATION:
				if (_player.isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt())) {
					pc.setBraveSpeed(3);
					pc.sendPackets(new S_SkillBrave(pc.getId(), 3, _getBuffIconDuration));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, _getBuffIconDuration));
				} else {
					pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
					pc.broadcastPacket(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
				}
				break;
			case SLOW:
			case MOB_SLOW_1:
			case MOB_SLOW_18:
				pc.sendPackets(new S_SkillHaste(pc.getId(), 2, _getBuffIconDuration));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 2, 0));
				break;
			default:
				if (_skill.isInvenIconUse()) {
					SC_SPELL_BUFF_NOTI.sendDatabaseIcon(pc, _skill, _getBuffIconDuration, true);
				}
				break;
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private void sendGrfx(boolean isSkillAction) {
		try {
			int actionId = _skill.getActionId();
			int actionId2 = _skill.getActionId2();
			int actionId3 = _skill.getActionId3();
			int castgfx = _skill.getCastGfx();
			int castgfx2 = _skill.getCastGfx2();
			int castgfx3 = _skill.getCastGfx3();
			if (castgfx == 0) {
				return;
			}
			if (_skillId == TROGIR_MILPITAS1) {
				int xx = 0;
				int yy = 0;
				int xx1 = 0;
				int yy1 = 0;
				int xx2 = 0;
				int yy2 = 0;
				Random random = new Random();
				int randomxy = random.nextInt(8);
				int a1 = 3 + randomxy;
				int a2 = -3 - randomxy;
				int b1 = 2 + randomxy;
				int b2 = -2 - randomxy;
				int heading = _npc.getMoveState().getHeading();
				switch (heading) {
					case 1:
						xx = a1;
						yy = a2;
						yy1 = a2;
						xx2 = a1;
						break;
					case 2:
						xx = a1;
						xx1 = b1;
						yy1 = a2;
						xx2 = b1;
						yy2 = a1;
						break;
					case 3:
						xx = a1;
						yy = a1;
						xx1 = a1;
						yy2 = a1;
						break;
					case 4:
						yy = a1;
						xx1 = a1;
						yy1 = b1;
						xx2 = a2;
						yy2 = b1;
						break;
					case 5:
						xx = a2;
						yy = a1;
						yy1 = a1;
						xx2 = a2;
						break;
					case 6:
						xx = a2;
						xx1 = b2;
						yy1 = a1;
						xx2 = b2;
						yy2 = a2;
						break;
					case 7:
						xx = a2;
						yy = a2;
						xx1 = a2;
						yy2 = a2;
						break;
					case 0:
						yy = a2;
						xx1 = a2;
						yy1 = b2;
						xx2 = a1;
						yy2 = b2;
						break;
					default:
						break;
				}
				int x = _npc.getX() + xx;
				int y = _npc.getY() + yy;
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y + 3, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y - 4, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y - 6, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 1, y, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 2, y + 2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 3, y - 4, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 4, y - 8, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 5, y + 2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 6, y - 5, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 5, y, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 3, y - 4, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 5, y - 6, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 7, y - 4, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 3, y, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 1, y + 3, _user.getMapId());
				int x1 = _npc.getX() + xx1;
				int y1 = _npc.getY() + yy1;
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1 + 1, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1 - 2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1 - 3, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 6, y1, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 5, y1 + 7, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 4, y1 - 6, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 3, y1 - 1, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 5, y1 + 2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 4, y1 - 3, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 3, y1, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 2, y1 - 4, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 8, y1 - 5, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 7, y1 - 2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 6, y1, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 5, y1 + 3, _user.getMapId());
				int x2 = _npc.getX() + xx2;
				int y2 = _npc.getY() + yy2;
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2 + 3, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2 - 4, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2 - 6, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 2, y2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 8, y2 + 2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 6, y2 - 3, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 4, y2 - 7, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 5, y2 + 5, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 3, y2 - 3, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 7, y2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 6, y2 - 6, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 8, y2 - 5, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 3, y2 - 7, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 4, y2, _user.getMapId());
				L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 6, y2 + 2, _user.getMapId());
				return;
			}

			if (_isCriticalDamage) {
				switch (_skillId) {
					case CALL_LIGHTNING: // 콜라이트닝
						castgfx = 11737;
						break;
					case SUNBURST: // 선버스트
						castgfx = 11760;
						break;
					case CONE_OF_COLD_mob:
					case CONE_OF_COLD: // 콘 오브 콜드
						castgfx = 11742;
						break;
					case DISINTEGRATE: // 디스인티그레이트
						castgfx = 11748;
						break;
					case ERUPTION:
						castgfx = 11754;
						break;
				}
			} else {
				if (_skillId == UNCANNY_DODGE) {
					L1PcInstance pc = (L1PcInstance) _target;
					if (pc.getAC().getAc() <= -100) {
						castgfx = 11766;
					}
				} else {
					if (castgfx != _skill.getCastGfx()) {
						return; // 그래픽 번호가 다르다.
					}
				}
			}

			if(_user.isPassive(MJPassiveID.DISINTEGRATE_NEMESIS.toInt()) && _skillId == L1SkillId.DISINTEGRATE) {
				if (_isCriticalDamage) {
					castgfx = 20114;
				} else {
					castgfx = 20112;
				}
			}

			if (castgfx2 != _skill.getCastGfx2()) {
				return;
			}
			if (castgfx3 != _skill.getCastGfx3()) {
				return;
			}
			if (_user instanceof L1PcInstance) {
				if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM) {
					L1PcInstance pc = (L1PcInstance) _user;
					if (_skillId == FIRE_WALL) {

						pc.setHeading(pc.targetDirection(_targetX, _targetY));
						pc.sendPackets(new S_ChangeHeading(pc));
						pc.broadcastPacket(new S_ChangeHeading(pc));


					}
					S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
					pc.sendPackets(gfx);
					pc.broadcastPacket(gfx);
					return;
				}
				if (_skillId == VISION_TELEPORT){
					L1PcInstance pc = (L1PcInstance) _user;
					pc.setHeading(pc.targetDirection(_targetX, _targetY));
					pc.send_effect(20105);
					pc.start_teleport(_targetX, _targetY, pc.getMapId(), pc.getHeading(), 20105, false, false);
					pc.sendPackets(new S_ChangeHeading(pc));
					pc.broadcastPacket(new S_ChangeHeading(pc));
					L1EffectSpawn.getInstance().spawnEffect(20107, 1000, _targetX, _targetY, pc.getMapId());
					return;

				}


				int targetid = _target.getId();

				if (_skillId == EMPIRE) {
					_target.send_effect(17569);
					return;
				}
				if (_skillId == TEMPEST) {
					_player.send_effect(20608);
				}

				if (_skillId == SHOCK_STUN || _skillId == MOB_SHOCKSTUN_30 || _skillId == MOB_RANGESTUN_20 || _skillId == MOB_RANGESTUN_19 || _skillId == MOB_RANGESTUN_18 || _skillId == Mob_RANGESTUN_30
						|| _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8 || _skillId == OMAN_STUN || _skillId == BALOCH_STUN || _skillId == BOS_STUN18
						|| _skillId == BOS_STUN18 || _skillId == Maeno_STUN || _skillId == Besi_STUN || _skillId == fornos_STUN || _skillId == Moster_STUN || _skillId == MOSTER_STUN_1
						|| _skillId == DRAGON_HALPAS_STUN ) {
					if (_targetList.size() == 0) {// 실패 스턴 모션
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							pc.sendPackets(new S_SkillSound(pc.getId(), 4434));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 4434));
							pc.sendPackets(new S_ServerMessage(280));
						} else if (_target instanceof L1NpcInstance) {
							_target.broadcastPacket(new S_SkillSound(_target.getId(), 4434));
						}
						return;
					} /*
					 * else { if (_skillId != ETERNITI) { if (_target instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance) _target; pc.sendPackets(new S_SkillSound(pc.getId(), 4434)); pc.broadcastPacket(new S_SkillSound(pc.getId(), 4434)); } else if (_target
					 * instanceof L1NpcInstance) { _target.broadcastPacket(new S_SkillSound(_target.getId(), 4434)); } return; } }
					 */
				}

				/**
				 * 이터니티 원복시 삭제 이미지 처리
				 */
				if (_skillId == ETERNITI) {
					if (_targetList.size() == 0) {
						int targetDirection = _user.targetDirection(_targetX, _targetY);
						if (_user.getHeading() != targetDirection) {
							_user.setHeading(targetDirection);
							_user.sendPackets(new S_ChangeHeading(_user));
							_user.broadcastPacket(new S_ChangeHeading(_user));
						}

						S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
						_user.sendPackets(gfx);
						_user.broadcastPacket(gfx);

						if ((_target instanceof L1PcInstance)) {
							L1PcInstance pc = (L1PcInstance) _target;
							pc.broadcastPacket(new S_SkillSound(targetid, castgfx));
						}
						_target.broadcastPacket(new S_SkillSound(targetid, castgfx));

						L1Magic _magic = new L1Magic(_user, _target);
						_magic.setLeverage(getLeverage());

						int dmg = _magic.calcMagicDamage(_skillId);

						if (_target instanceof L1PcInstance) {
							L1PcInstance target = (L1PcInstance) _target;
							target.receiveDamage(_user, dmg);
						} else if (_target instanceof L1NpcInstance) {
							L1NpcInstance target = (L1NpcInstance) _target;
							target.receiveDamage(_user, dmg);
						}
					}
					return;
				}

				if (_skillId == LIGHT) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.sendPackets(new S_Sound(145));
				}
				if (_skillId == SOUL_OF_FLAME) {
					L1PcInstance pc = (L1PcInstance) _target;
					pc.sendPackets(new S_SkillSound(pc.getId(), 11778, 19));
					pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 11778, _getBuffIconDuration));
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 11778));
				}

				if (_skillId == UNCANNY_DODGE) {
					L1PcInstance pc = (L1PcInstance) _target;
					if (pc.getAC().getAc() <= -100) {
						pc.sendPackets(new S_SkillSound(pc.getId(), 11766, 19));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 11766, 19));
					} else {
						pc.sendPackets(new S_SkillSound(pc.getId(), 11765, 19));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 11765, 19));
					}
				}

				if (_targetList.size() == 0 && !(_skill.getTarget().equals("none"))) {
					int tempchargfx = _player.getCurrentSpriteId();
					if (tempchargfx == 5727 || tempchargfx == 5730) {
						actionId = ActionCodes.ACTION_SkillBuff;
					} else if (tempchargfx == 5733 || tempchargfx == 5736) {
						actionId = ActionCodes.ACTION_Attack;
					}
					if (isSkillAction) {
						S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), actionId);
						_player.sendPackets(gfx);
						_player.broadcastPacket(gfx);
					}
					return;
				}

				if (_skill.getTarget().equals("attack") && _skillId != 18) {
					if (isPcSummonPet(_target)) {
						if (_player.getZoneType() == 1 || _target.getZoneType() == 1 || _player.checkNonPvP(_player, _target)) {
							if (_skillId == THUNDER_GRAB && _player.isPassive(MJPassiveID.THUNDER_GRAP_BRAVE.toInt())) {
								_player.send_action(18);
								_target.send_effect(17229);
								return;
							}
							if (_skillId == BEHEMOTH) {
								_player.send_action(18);
								_target.send_effect(21957);
								return;
							}
							if (_skillId == CHAIN_REACTION) {
								_player.send_action(18);
								_target.send_effect(21961);
								return;
							}
							_player.sendPackets(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId));
							_player.broadcastPacket(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId));
							return;
						}
					}
					if (_skillId == THUNDER_GRAB && _player.isPassive(MJPassiveID.THUNDER_GRAP_BRAVE.toInt())) {
						_player.send_action(18);
						_target.send_effect(17229);
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _player);
					} else if (_skillId == BEHEMOTH) {
						_player.send_action(18);
						_target.send_effect(21957);
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _player);

					} else if (_skillId == CHAIN_REACTION) {
						_player.send_action(18);
						_target.send_effect(21961);

					} else if (_skill.getArea() == 0) {
						if (_skillId == SHOCK_ATTACK) {
							_player.send_action(actionId);
							_target.send_effect(castgfx);
							_target.send_action(ActionCodes.ACTION_Damage);
						} else {
							_player.sendPackets(new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId));
							_player.broadcastPacket(new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId), _target);
							_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _player);
						}
					} else {
						L1Character[] cha = new L1Character[_targetList.size()];
						int i = 0;
						for (TargetStatus ts : _targetList) {
							cha[i] = ts.getTarget();
							i++;
						}
						_player.sendPackets(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR));
						_player.broadcastPacket(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), cha);
					}
				} else if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK) {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						cha[i].broadcastPacketExceptTargetSight(new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _player);
						i++;
					}
					_player.sendPackets(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR));
					_player.broadcastPacket(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), cha);
				} else {
					if (_skillId != TELEPORT && _skillId != MASS_TELEPORT && _skillId != TELEPORT_TO_MATHER /*&& _skillId != VISION_TELEPORT*/) {
						if (isSkillAction) {
							S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), _skill.getActionId());
							_player.sendPackets(gfx);
							_player.broadcastPacket(gfx);
						}
						if (_skillId == COUNTER_MAGIC) {
							_player.sendPackets(new S_SkillSound(targetid, castgfx));
							_player.broadcastPacket(new S_SkillSound(targetid, castgfx));
						} else if (_skillId == TRUE_TARGET) {
							return;
						} else if (_skillId == CANCELLATION) {
							return;
						} else if (_skillId == THUNDER_GRAB) {
						} else if (_skillId == ARMOR_BRAKE) {
							if (_player.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
								castgfx = 17226;
							}
							_target.setSkillEffect(_skillId, 10000);
							_player.send_tarobj_party_effect(targetid, castgfx);
							int characterid =  _player.getId();
							_target.set_Armor_break_Attacker(characterid);
							if (_target instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) _target;
								on_icons(pc, _skillId, 10);
							}
						} else if (_skillId == DOUBLE_BRAKE) {
							if (_player.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
								castgfx = 17224;
								_player.send_effect(castgfx);
//							off_icons(_player, DOUBLE_BRAKE);
//							on_icons_passive(_player, MJPassiveID.DOUBLE_BREAK_DESTINY, _getBuffIconDuration);
							} else {
								_player.send_effect(castgfx);
//							on_icons(_player, DOUBLE_BRAKE, _getBuffIconDuration);
							}
						} else if (_skillId == COUNTER_BARRIER) {
							if (_player.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
								_player.send_effect(17219);
								off_icons(_player, COUNTER_BARRIER);
								on_icons(_player, COUNTER_BARRIER, _getBuffIconDuration);
							}
							if (_player.isPassive(MJPassiveID.COUNTER_BARRIER_MASTER.toInt())) {
								_player.send_effect(20473);
								on_icons_passive(_player, MJPassiveID.COUNTER_BARRIER_MASTER, _getBuffIconDuration);
								off_icons(_player, COUNTER_BARRIER);
							}
							if (_skillId == COUNTER_BARRIER) {
								if (!_player.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt()) && !_player.isPassive(MJPassiveID.COUNTER_BARRIER_MASTER.toInt())) {
									_player.send_effect(10709);
									on_icons(_player, COUNTER_BARRIER, _getBuffIconDuration);
								}
							}
						} else if (_skillId == DESPERADO) {
							if (_player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
								castgfx = 17233;
							}
							_target.send_effect(castgfx);
						} else if (_skillId == DEMOLITION) {
							int effectId = 18414;
							_target.send_effect(effectId);
							// 액션만 취하고자 할때 추가한다. 디비에는 이팩번호 아무거나 입력.
						} else if (_skillId == STORM_EYE) {
							// System.out.println("아이 오브 스톰 예외처리");
						} else if (_skillId == REDUCTION_ARMOR) {
							if (_player.isPassive(MJPassiveID.REDUCTION_ARMOR_VETERAN.toInt())) {
								castgfx = 18966;
							}
							_target.send_effect(castgfx);
						} else if (_skillId == DESTROY) {
							if (_player.isPassive(MJPassiveID.DESTROY_PIER.toInt()))
								_target.send_effect(18959);

							if (_player.isPassive(MJPassiveID.DESTROY_HORROR.toInt()))
								_target.send_effect(18961); // 18963

							_target.send_effect(castgfx);
						} else if (_skillId == SHADOW_ARMOR) {
							if (_player.isPassive(MJPassiveID.SHADOW_ARMOR_PASS.toInt())) {
								_target.send_effect(19596);
							} else {
								_target.send_effect(castgfx);
							}
						} else if (_skillId == HOLY_WALK) {
							if (_player.isPassive(MJPassiveID.HOLY_WALK_EVOLUTION.toInt())) {
								castgfx = 20109;
							}
							_player.send_effect(castgfx);
						} else if (_skillId == MOVING_ACCELERATION) {
							if (_player.isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt())) {
								_target.send_effect(19535);
							} else {
								_target.send_effect(castgfx);
							}
						} else if (_skillId == LUCIFER) {
							if (_player.isPassive(MJPassiveID.LUCIFER_DESTINY.toInt())) {
								castgfx = castgfx2;
							}
							_target.send_effect(castgfx);
						} else if (_skillId == IMMUNE_TO_HARM) {
							if (_player != null) {
								if (_player.getId() == _target.getId()) {
									if (_player.isPassive(MJPassiveID.IMMUNETOHARM_SAINT.toInt())) {
										_player.send_effect(castgfx2);
									} else {
										_player.send_effect(castgfx);
									}
								} else {
									_target.send_effect(castgfx);
								}
							}
						} else if (_skillId == SOUL_BARRIER) {
							if (_player.isPassive(MJPassiveID.SOUL_BARRIER_ARMOR.toInt()))
								castgfx = 20465;
							_player.send_effect(castgfx);
						} else if (_skillId == STRIKER_GALE) {
							if (_player.isPassive(MJPassiveID.STRIKER_GAIL_SHOT.toInt()))
								castgfx = 20411;
							_target.send_effect(castgfx);
						} else if (_skillId == BURNING_SHOT) {
							if (_player.hasSkillEffect(BURNING_SHOT))
								castgfx = 20455;
							_player.send_effect(castgfx);
						} else {
							_player.sendPackets(new S_SkillSound(targetid, castgfx));
							_player.broadcastPacket(new S_SkillSound(targetid, castgfx));
						}
					}
					for (TargetStatus ts : _targetList) {
						L1Character cha = ts.getTarget();
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_OwnCharStatus(pc));
						}
					}
				}
			} else if (_user instanceof L1NpcInstance) {
				int targetid = _target.getId();
				if (_skillId == BLACKELDER) {
					Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 4848));
					Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 2552));
				}

				if (_user instanceof L1MerchantInstance) {
					_user.broadcastPacket(new S_SkillSound(targetid, castgfx));
					return;
				}

				if (_targetList.size() == 0 && !(_skill.getTarget().equals("none"))) {
					S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
					_user.broadcastPacket(gfx);
					return;
				}

				if (_skill.getTarget().equals("attack") && _skillId != 18) {
					if (_skillId == WIDE_ARMORBREAK) {
						if (_targetList.size() > 0) {
							final int effect = castgfx;
							final L1Character[] targets = new L1Character[_targetList.size()];
							for (int i = _targetList.size() - 1; i >= 0; --i) {
								L1Character c = _targetList.get(i).getTarget();
								targets[i] = c == null || c.isDead() ? null : c;
							}
							GeneralThreadPool.getInstance().execute(new Runnable() {
								@Override
								public void run() {
									try {
										for (int i = targets.length - 1; i >= 0; --i) {
											L1Character t = targets[i];
											if (t == null || t.isDead())
												continue;
											t.send_effect(effect);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
					} else if (_skill.getArea() == 0) {
						if (_skillId == 20030)
							_target.send_effect(castgfx);
							// _user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx,
							// _targetX, _targetY, actionId), _target);
						else
							_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx, _targetX, _targetY, actionId), _target);
						if (actionId2 > 0 && castgfx2 > 0) {
							_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx2, _targetX, _targetY, actionId2), _target);
							_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
						}
						if (actionId3 > 0 && castgfx3 > 0) {
							_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx3, _targetX, _targetY, actionId3), _target);
							_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
						}
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
					} else {
						L1Character[] cha = new L1Character[_targetList.size()];
						int i = 0;
						for (TargetStatus ts : _targetList) {
							cha[i] = ts.getTarget();
							cha[i].broadcastPacketExceptTargetSight(new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _user);
							i++;
						}
						_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), cha);
						if (actionId2 > 0 && castgfx2 > 0) {
							_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx2, actionId2, S_RangeSkill.TYPE_DIR));
							_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
						}
						if (actionId3 > 0 && castgfx3 > 0) {
							_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx3, actionId3, S_RangeSkill.TYPE_DIR));
							_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
						}
					}
				} else if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK) {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						i++;
					}
					_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), cha);
				} else {
					if (_skillId != 5 && _skillId != 69 && _skillId != 131) {
						S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
						_user.broadcastPacket(gfx);

						if (_skillId == OMAN_CANCELLATION || _skillId == ANTA_MESSAGE_1 || _skillId == ANTA_CANCELLATION || _skillId == PAP_PREDICATE7 || _skillId == PAP_PREDICATE11
								|| _skillId == PAP_PREDICATE12 || _skillId == RINDVIOR_PREDICATE_CANCELLATION || _skillId == RINDVIOR_CANCELLATION || _skillId == BLACKELDER_DEATH_HELL
								|| _skillId == PHOENIX_CANCELLATION) {
							return;
						} else {
							_user.broadcastPacket(new S_SkillSound(targetid, castgfx));
						}

						if (actionId2 > 0 && castgfx2 > 0) {
							S_DoActionGFX gfx2 = new S_DoActionGFX(_user.getId(), _skill.getActionId2());
							_user.broadcastPacket(gfx2);
							_user.broadcastPacket(new S_SkillSound(targetid, castgfx2));
						}
						if (actionId3 > 0 && castgfx3 > 0) {
							S_DoActionGFX gfx3 = new S_DoActionGFX(_user.getId(), _skill.getActionId3());
							_user.broadcastPacket(gfx3);
							_user.broadcastPacket(new S_SkillSound(targetid, castgfx3));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("技能使用錯誤 sendGrfx: " + _skill.getId());
		}
	}

	private static final int[] SCALES = new int[] { SCALES_EARTH_DRAGON, SCALES_WATER_DRAGON, SCALES_FIRE_DRAGON, SCALES_RINDVIOR_DRAGON, };

	private boolean check_scales_skill(L1Character cha) {
		if (_skillId == SCALES[0] || _skillId == SCALES[1] || _skillId == SCALES[2] || _skillId == SCALES[3]) {
			cha.removeSkillEffect(_skillId);
			int[] remains = new int[] { cha.getSkillEffectTimeSec(SCALES[0]), cha.getSkillEffectTimeSec(SCALES[1]), cha.getSkillEffectTimeSec(SCALES[2]), cha.getSkillEffectTimeSec(SCALES[3]), };

			if (!cha.isPassive(MJPassiveID.AURAKIA.toInt())) {
				for (int i = remains.length - 1; i >= 0; --i) {
					int sid = SCALES[i];
					if (sid >= 0) {
						cha.removeSkillEffect(sid);
					}
				}
			} else {
				int count = (remains[0] == -1 ? 0 : 1) + (remains[1] == -1 ? 0 : 1) + (remains[2] == -1 ? 0 : 1) + (remains[3] == -1 ? 0 : 1);
				if (count >= 4) {
					int removed_skill_idx = 0;
					int previous_remains = Integer.MAX_VALUE;
					for (int i = remains.length - 1; i >= 0; --i) {
						if (remains[i] == -1)
							continue;

						if (remains[i] < previous_remains) {
							previous_remains = remains[i];
							removed_skill_idx = i;
						}
					}
					cha.removeSkillEffect(SCALES[removed_skill_idx]);
				}
			}
			return true;
		}
		return false;
	}

	private void deleteRepeatedSkills(L1Character cha) {
		if (check_scales_skill(cha))
			return;

		/**
		 * TODO 處理效果不重複（重疊），例如施法者專用效果，或者在同一行中應用，或在重疊使用時處理效果重複部分
		 **/
		final int[][] repeatedSkills = {
				{ EARTH_WEAPON, AQUA_SHOT, STORM_EYE, BURNING_WEAPON, STORM_SHOT }, // 0行
				{ SHIELD, FIRE_SHIELD, IRON_SKIN }, // 1行
				{ HOLY_WALK, BLOOD_LUST, MOVING_ACCELERATION, STATUS_BRAVE, STATUS_ELFBRAVE, FOCUS_WAVE, HURRICANE, SAND_STORM, DANCING_BLADES }, // 2行
				{ HASTE, MOB_HASTE, GREATER_HASTE, STATUS_HASTE }, // 3行
				{ PHYSICAL_ENCHANT_DEX, NarutoThankYouCandy, DRESS_DEXTERITY }, // 4行
				{ PHYSICAL_ENCHANT_STR, DRESS_MIGHTY }, // 5行
				{ COUNTER_MIRROR, DECREASE_WEIGHT, REDUCE_WEIGHT }, // 6行
				{ FAFU_MAAN, ANTA_MAAN, LIND_MAAN, VALA_MAAN, LIFE_MAAN, BIRTH_MAAN, SHAPE_MAAN, BLACK_DRAGON_MAAN, NAVER_BLACK_DRAGON_MAAN, BlessingOfNormality }, // 7行
				{ PAP_FIVEPEARLBUFF, PAP_MAGICALPEARLBUFF }, // 9行
				{ SIDE_OF_ME_BLESSING, RE_START_BLESSING, NEW_START_BLESSING, LIFE_BLESSING }, // 10行
				{ HUNTER_BLESS }, // 11行
				{ EXP_BUFF, EXP_POTION, EXP_POTION_Event }, // 12行
				{ ADVANCE_SPIRIT, GIGANTIC, PRIDE }, // 13行
				{ CUBE_OGRE, IllUSION_OGRE }, // 14行
				{ CUBE_AVATAR, IllUSION_AVATAR }, // 15行
				{ LUCIFER, IMMUNE_TO_HARM }, // 18行
				{ ELEMENTAL_FIRE, QUAKE }, // 19行
				{ BRAVE_UNION }, // 20行
		};
		for (int[] skills : repeatedSkills) {
			for (int id : skills) {
				if (id == _skillId) {
					stopSkillList(cha, skills);
				}
			}
		}

	}

	private void stopSkillList(L1Character cha, int[] repeat_skill) {
		for (int skillId : repeat_skill) {
			if (skillId != _skillId) {
				cha.removeSkillEffect(skillId);
			}
		}
	}

	private void setDelay() {
		long delay = SpriteInformationLoader.getInstance().getUseSpellInterval(_player, _skillId);
		int group_id = SpriteInformationLoader.getInstance().getUseSpellGroupId(_skillId);
		long global_delay = 0L;
		long last_delay = 0L;
		if (group_id > 0) {
			global_delay = SpriteInformationLoader.getInstance().getUseSpellGlobalInterval(_player, _skillId);
		}
		if (_player instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _player;
			int DCT = CalcStat.calcPureDecreaseCoolTime(pc.getAbility().getCha())+ CalcStat.calcDecreaseCoolTime(pc.getAbility().getTotalCha());
			if (DCT != 0){ //카리스텟으로 쿨타임 줄어듬
				delay -= DCT;
				//			long delay1 = delay;
				if (delay < 200){
					delay = 200;
				}
				global_delay -= DCT;
				if (global_delay <= 200){
					global_delay = 200;
				}
			}
		}

		last_delay = global_delay > 0 ? global_delay : delay;
		global_delay = global_delay > 0 ? global_delay : last_delay;




		if (_player != null) {
			if (last_delay > 0) {
				L1SkillDelay.onSkillUse(_user, last_delay, group_id > 0 ? true : false);
			}
		}
		/*
		 * int reuse = _skill.getReuseDelay(); if (_player != null) { if (_player.isHaste()) { reuse -= 250; } if (_player.isBrave() || _player.isElfBrave()) { reuse -= 300; } reuse -= 300; if (reuse < 500) { reuse = 500; } if (_skill.getReuseDelay() > 0) {
		 * L1SkillDelay.onSkillUse(_user, reuse); } }
		 */
	}

	/** 안타라스 파푸리온 Message */
	private void MonsterMessage(int type) {
		String MonMessage = " ";
		if (type == 1) { // 안타라스
			switch (_skillId) {
				case ANTA_MESSAGE_1:
					MonMessage = "$7861";
					break;
				case ANTA_MESSAGE_2:
					MonMessage = "$7911";
					break;
				case ANTA_MESSAGE_3:
					MonMessage = "$7905";
					break;
				case ANTA_MESSAGE_4:
					MonMessage = "$7907";
					break;
				case ANTA_MESSAGE_5:
					MonMessage = "$7863";
					break;
				case ANTA_MESSAGE_6:
					MonMessage = "$7903";
					break;
				case ANTA_MESSAGE_7:
					MonMessage = "$7909";
					break;
				case ANTA_MESSAGE_8:
					MonMessage = "$7915";
					break;
				case ANTA_MESSAGE_9:
					MonMessage = "$7862";
					break;
				case ANTA_MESSAGE_10:
					MonMessage = "$7913";
					break;
				default:
					break;
			}
		} else if (type == 2) { // 파푸리온
			switch (_skillId) {
				case PAP_PREDICATE1:
					MonMessage = "$8467";
					break;
				case PAP_PREDICATE3:
					MonMessage = "$8458";
					break;
				case PAP_PREDICATE5:
					MonMessage = "$8456";
					break;
				case PAP_PREDICATE6:
					MonMessage = "$8457";
					break;
				case PAP_PREDICATE7:
					MonMessage = "$8454";
					break;
				case PAP_PREDICATE8:
					MonMessage = "$8455";
					break;
				case PAP_PREDICATE9:
					MonMessage = "$8460";
					break;
				case PAP_PREDICATE11:
					MonMessage = "$8463";
					break;
				case PAP_PREDICATE12:
					MonMessage = "$8465";
					break;
				default:
					break;
			}
		}
		_user.broadcastPacket(new S_NpcChatPacket(_npc, MonMessage, 0));
		return;
	}

	/**
	 * 이미 버프가 있으면 삭제하고 초기화한다. 단일 스킬 종류 재사용 스킬 이쪽 메서드로 옮김
	 **/
	private static final HashSet<Integer> hasInitialize;
	static {
		hasInitialize = new HashSet<Integer>();
		hasInitialize.add(L1SkillId.STRIKER_GALE);
		hasInitialize.add(L1SkillId.EAGGLE_EYE);
		hasInitialize.add(L1SkillId.FOCUS_SPRITS);
		hasInitialize.add(L1SkillId.REDUCTION_ARMOR);
		hasInitialize.add(L1SkillId.LUCIFER);
		hasInitialize.add(L1SkillId.CYCLONE);
		hasInitialize.add(L1SkillId.AQUA_PROTECTER);
		hasInitialize.add(L1SkillId.SHINING_SHILD);
		hasInitialize.add(L1SkillId.IllUSION_OGRE);
		hasInitialize.add(L1SkillId.FREEZEENG_ARMOR);
		hasInitialize.add(L1SkillId.BLESSED_ARMOR);
		hasInitialize.add(L1SkillId.IllUSION_AVATAR);
		hasInitialize.add(L1SkillId.IMMUNE_TO_HARM);
		hasInitialize.add(L1SkillId.TITANL_RISING);
		hasInitialize.add(L1SkillId.ENCHANT_ACURUCY);
		hasInitialize.add(L1SkillId.BLOW_ATTACK);
		hasInitialize.add(L1SkillId.CLEAR_MIND);
		hasInitialize.add(L1SkillId.SHADOW_FANG);
		hasInitialize.add(L1SkillId.SOUL_BARRIER);
		hasInitialize.add(L1SkillId.ABSOLUTE_BLADE);
		hasInitialize.add(L1SkillId.GLOWING_WEAPON);
		hasInitialize.add(L1SkillId.MAFR);
		hasInitialize.add(L1SkillId.POTENTIAL);
		hasInitialize.add(L1SkillId.HALPAS);
		hasInitialize.add(BLACK_DRAGON_MAAN);
		hasInitialize.add(NAVER_BLACK_DRAGON_MAAN);
		hasInitialize.add(ANTA_MAAN);
		hasInitialize.add(FAFU_MAAN);
		hasInitialize.add(LIND_MAAN);
		hasInitialize.add(VALA_MAAN);
		hasInitialize.add(BIRTH_MAAN);
		hasInitialize.add(SHAPE_MAAN);
		hasInitialize.add(LIFE_MAAN);
		hasInitialize.add(BLOOD_LUST);
		hasInitialize.add(miso_Buff);
		hasInitialize.add(miso_Buff1);
		hasInitialize.add(miso_Buff2);
		hasInitialize.add(SHADOW_ARMOR);
		hasInitialize.add(L1SkillId.LIBERATION);
		hasInitialize.add(L1SkillId.MATH_IMMUNE_TO_HARM);
		hasInitialize.add(L1SkillId.VANGUARD);
		hasInitialize.add(L1SkillId.BERSERKERS);
		hasInitialize.add(L1SkillId.TARAS_ATTACK_SPEED);
		hasInitialize.add(L1SkillId.TARAS_MOVE_SPEED);
		hasInitialize.add(L1SkillId.BRAVE_UNION);

	}

	/**
	 * 현재 버프가 있던 없던 addMagicList메서드를 호출안할 스킬들. if (_user.hasSkillEffect(L1SkillId.HOLY_WEAPON)) { _user.removeSkillEffect(L1SkillId.HOLY_WEAPON); } pc.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000); 재사용 스킬 이쪽 메서드로 옮김(위 로직 상태였을때 밑에 추가) 하위
	 * 조건/파티원에게....그런 스킬류
	 **/
	private static final HashSet<Integer> nonAddMagicList;
	static {
		nonAddMagicList = new HashSet<Integer>();
		nonAddMagicList.add(L1SkillId.HOLY_WEAPON);
		nonAddMagicList.add(L1SkillId.BLESS_WEAPON);
		nonAddMagicList.add(L1SkillId.ENCHANT_WEAPON);
		nonAddMagicList.add(L1SkillId.ADVANCE_SPIRIT);
		nonAddMagicList.add(L1SkillId.GIGANTIC);
		nonAddMagicList.add(L1SkillId.PRIDE);
		nonAddMagicList.add(L1SkillId.GRACE);
		nonAddMagicList.add(L1SkillId.PRIME);
		nonAddMagicList.add(L1SkillId.STORM_EYE);

		nonAddMagicList.add(L1SkillId.CUBE_OGRE);
		nonAddMagicList.add(L1SkillId.CUBE_GOLEM);
		nonAddMagicList.add(L1SkillId.CUBE_RICH);
		nonAddMagicList.add(L1SkillId.CUBE_AVATAR);
		nonAddMagicList.add(L1SkillId.AREA_OF_SILENCE);
	}

	/**
	 * 현재 버프가 있을때만 addMagicList메서드를 호출안할 스킬들. 재사용 스킬 이쪽 메서드로 옮김
	 **/
	private static final HashSet<Integer> hasNonAddMagicList;
	static {
		hasNonAddMagicList = new HashSet<Integer>();
		hasNonAddMagicList.add(SHOCK_STUN);
		hasNonAddMagicList.add(EMPIRE);
		hasNonAddMagicList.add(DISINTEGRATE);
		hasNonAddMagicList.add(THUNDER_GRAB);
		hasNonAddMagicList.add(OMAN_STUN);
		hasNonAddMagicList.add(DRAGON_HALPAS_STUN);
		hasNonAddMagicList.add(BALOCH_STUN);
		hasNonAddMagicList.add(BOS_STUN18);
		hasNonAddMagicList.add(Maeno_STUN);
		hasNonAddMagicList.add(Besi_STUN);
		hasNonAddMagicList.add(ANTA_MESSAGE_6);
		hasNonAddMagicList.add(ANTA_MESSAGE_7);
		hasNonAddMagicList.add(ANTA_MESSAGE_8);
		hasNonAddMagicList.add(DRAGON_HALPAS);
		hasNonAddMagicList.add(fornos_STUN);
		hasNonAddMagicList.add(MOSTER_STUN_1);
		hasNonAddMagicList.add(DRAGON_SET);
		hasNonAddMagicList.add(HUNTER_BLESS);
		hasNonAddMagicList.add(Moster_STUN);
		hasNonAddMagicList.add(FOCUS_WAVE);
		hasNonAddMagicList.add(HURRICANE);
		hasNonAddMagicList.add(PANTHERA);
		hasNonAddMagicList.add(SHADOW_STEP);
		hasNonAddMagicList.add(PHANTOM);
		hasNonAddMagicList.add(FORCE_STUN);
		hasNonAddMagicList.add(FORCE_STUN_FAIL);
		hasNonAddMagicList.add(TEMPEST);
		hasNonAddMagicList.add(ENSNARE);
		hasNonAddMagicList.add(OSIRIS);
	}


	private void runSkill() {
//		System.out.println(_skillId);
		if (_player != null && _player.isInvisble()) {
			if (_skill.getType() == L1Skills.TYPE_ATTACK || _skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) {
				_player.delInvis();
			}
		}

		if (_skillId == METEOR_STRIKE || _skillId == ICE_METEOR_STRIKE && _target instanceof L1PcInstance) {
			if (_target.hasSkillEffect(ANTI_METEOR)) {
				return;
			}

			_target.setSkillEffect(ANTI_METEOR, 2000);
		}

		// XXX 디스중첩관련
//		if (_skillId == DISINTEGRATE || _skillId == ETERNITI && _target instanceof L1PcInstance) {
//			if (_target.hasSkillEffect(ANTI_DISINTEGRATE)) {
//				return;
//			}
//
//			_target.setSkillEffect(ANTI_DISINTEGRATE, 3000);
//		}

		if (_skillId == LIFE_STREAM) {
			L1EffectSpawn.getInstance().spawnEffect(81169, _skill.getBuffDuration() * 1000, _targetX, _targetY, _user.getMapId());
			return;
		}

		if (_skillId == FIRE_WALL) {
			L1EffectSpawn.getInstance().doSpawnFireWall(_user, _targetX, _targetY);
			return;
		}
		if (_skillId == VISION_TELEPORT) {

			L1EffectSpawn.getInstance().spawnEffect(20107, 1000, _targetX, _targetY, _user.getMapId());
			return;
		}


		if (_skillId == SHOCK_STUN || _skillId == EMPIRE || _skillId == BONE_BREAK || _skillId == FORCE_STUN || _skillId == AVENGER || _skillId == fornos_STUN || _skillId == PANTHERA
				|| _skillId == PHANTOM || _skillId == FORCE_STUN_FAIL /*|| _skillId == TEMPEST*/ && _user instanceof L1PcInstance) {
			_target.onAction(_player);
		}

		if (!isTargetCalc(_target)) {
			return;
		}

		// 독 구름
		if (_skillId == DESERT_SKILL4 || _skillId == ZENITH_Poison || _skillId == doctor_skills) {
			EffectSpawn();
		}

		/** MonsterMessage Type 1: 안타라스, Type 2: 파푸리온 **/
		if (_skillId >= ANTA_MESSAGE_1 && _skillId <= ANTA_MESSAGE_10) {
			MonsterMessage(1);
		}
		if (_skillId >= PAP_PREDICATE1 && _skillId <= PAP_PREDICATE12) {
			MonsterMessage(2);
		}
		try {
			TargetStatus ts = null;
			L1Character cha = null;
			int dmg = 0;
			int drainMana = 0;
			int heal = 0;
			boolean isSuccess = false;
			int undeadType = 0;

			for (Iterator<TargetStatus> iter = _targetList.iterator(); iter.hasNext();) {
				ts = null;
				cha = null;
				dmg = 0;
				heal = 0;
				isSuccess = false;
				undeadType = 0;

				ts = iter.next();
				cha = ts.getTarget();
				if (!ts.isCalc() || !isTargetCalc(cha)) {
					continue;
				}

				L1Magic _magic = new L1Magic(_user, cha);
				_magic.setLeverage(getLeverage());

				// 클라우디아 마법 대미지 상향
				if (_user.getMapId() == 3 || _user.getMapId() == 0 || _user.getMapId() == 7 || _user.getMapId() == 12146 || _user.getMapId() == 12147 || _user.getMapId() == 8 || _user.getMapId() == 9
						|| _user.getMapId() == 1 || _user.getMapId() == 2 || _user.getMapId() == 10 || _user.getMapId() == 12) {
				}

				if (_calcType == PC_NPC && _targetList.size() >= 1) {
					_magic.setSimSimLeverge(Config.ServerAdSetting.NewMagicDmg);
				}
				// PC->NPC 범위 마법 대미지 외부 상세
				if (_user.getLevel() >= Config.MagicAdSetting.SIMLEVELMIN1 && _user.getLevel() <= Config.MagicAdSetting.SIMLEVELMAX1) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.MagicAdSetting.SIMSIMDMG1);
				} else if (_user.getLevel() >= Config.MagicAdSetting.SIMLEVELMIN2 && _user.getLevel() <= Config.MagicAdSetting.SIMLEVELMAX2) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.MagicAdSetting.SIMSIMDMG2);
				} else if (_user.getLevel() >= Config.MagicAdSetting.SIMLEVELMIN3 && _user.getLevel() <= Config.MagicAdSetting.SIMLEVELMAX3) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.MagicAdSetting.SIMSIMDMG3);
				} else if (_user.getLevel() >= Config.MagicAdSetting.SIMLEVELMIN4 && _user.getLevel() <= Config.MagicAdSetting.SIMLEVELMAX4) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.MagicAdSetting.SIMSIMDMG4);
				} else if (_user.getLevel() >= Config.MagicAdSetting.SIMLEVELMIN5 && _user.getLevel() <= Config.MagicAdSetting.SIMLEVELMAX5) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.MagicAdSetting.SIMSIMDMG5);
				} else if (_user.getLevel() >= Config.MagicAdSetting.SIMLEVELMIN6 && _user.getLevel() <= Config.MagicAdSetting.SIMLEVELMAX6) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.MagicAdSetting.SIMSIMDMG6);
				} else {
					_magic.setSimSimLeverge(1);
				}

				if (cha instanceof L1MonsterInstance) {
					undeadType = ((L1MonsterInstance) cha).getNpcTemplate().get_undead();
				}

				if ((_skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) && isTargetFailure(cha)) {
					iter.remove();
					continue;
				}

				if (cha instanceof L1PcInstance) {
					if (_skillTime == 0) {
						_getBuffIconDuration = _skill.getBuffDuration();
					} else {
						_getBuffIconDuration = _skillTime;
					}
				}

				deleteRepeatedSkills(cha);
				L1PcInstance ownPc = null, tarPc = null;
				if (_user instanceof L1PcInstance) {
					ownPc = (L1PcInstance) _user;
					removeNewIcon(ownPc, _skillId);
				}
				if (_target instanceof L1PcInstance) {
					tarPc = (L1PcInstance) _target;
					removeNewIcon(tarPc, _skillId);
				}
				if (_skill.getType() == L1Skills.TYPE_ATTACK && _user.getId() != cha.getId()) {
					if(_skillId != L1SkillId.DISINTEGRATE) {
						if (isUseCounterMagic(cha)) {
							iter.remove();
							continue;
						}
					} else {
						if(_user.isPassive(MJPassiveID.DISINTEGRATE_NEMESIS.toInt())) {
							_isnemesis = _magic.calcProbabilityMagic(_skillId);
							if(!_isnemesis) {
								if (isUseCounterMagic(cha)) {
									iter.remove();
									continue;
								}
							}
						} else {
							if (isUseCounterMagic(cha)) {
								iter.remove();
								continue;
							}
						}
					}
					dmg = _magic.calcMagicDamage(_skillId);
					if (ownPc != null) {
						dmg += ownPc.get_lateral_magic_rate();
						if (tarPc != null) {
							dmg += ownPc.getResistance().getPVPweaponTotalDamage();
							dmg -= tarPc.get_pvp_defense();
						}
					}

					// 범위 마법 원본
//					if (dmg > _target.getCurrentHp()) {
//						if ((_target instanceof L1PcInstance) && _target.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
//							if (dmg > _target.getCurrentHp() + _target.getCurrentMp()) {
//								dmg = _target.getCurrentHp() + _target.getCurrentMp();
//							}
//						} else {
//							dmg = _target.getCurrentHp();
//						}
//					}

					// 대미지 보정처리(캐릭터 체력에 의한)
					// TODO 범위 마법 관련 재수정
					if (dmg > ts.getTarget().getCurrentHp()) {
						if ((ts.getTarget() instanceof L1PcInstance) && ts.getTarget().hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
							if (dmg > ts.getTarget().getCurrentHp() + ts.getTarget().getCurrentMp()) {
								dmg = ts.getTarget().getCurrentHp() + ts.getTarget().getCurrentMp();
							}
						} else {
							dmg = ts.getTarget().getCurrentHp();
						}
					}

					if (_calcType == PC_PC) {
						if (ownPc.getRedKnightClanId() != 0)
							ownPc.addRedKnightDamage(tarPc.getRedKnightClanId() == 0 ? dmg : -dmg);
					}

					if (_magic.isCriticalDamage()) {
						_isCriticalDamage = true;
					} else {
						_isCriticalDamage = false;
					}

					// 해당 스킬이 어택일경우 여부 판별후 삭제처리
					if (_skillId != SHOCK_STUN && _skillId != TRIPLE_ARROW && _skillId != EMPIRE && _skillId != FOU_SLAYER && _skillId != FORCE_STUN && _skillId != FORCE_STUN_FAIL
							&& _skillId != fornos_STUN) {
						if (cha instanceof L1PcInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA));
							}
						} else if (cha instanceof L1MonsterInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
							}
						}
					}
				} else if (_skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) {
					isSuccess = _magic.calcProbabilityMagic(_skillId);
					if (_skillId == SHOCK_STUN || _skillId == FORCE_STUN || _skillId == fornos_STUN) {
						if (_target != null) {
							_target.send_effect(4434);
						}
					}

					if (_skillId == EMPIRE) {
						if (_target != null) {
							_target.send_effect(17569);
						}
					}

					if (cha instanceof L1PcInstance && _user instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1PinkName.onAction(pc, _user);
					}

					/**
					 * 이터니티 원복시 삭제
					 */
					if (_skillId == ETERNITI) {
						dmg = _magic.calcMagicDamage(_skillId);
					}

					if (_skillId != ERASE_MAGIC && _skillId != EARTH_BIND) {
						if (cha instanceof L1PcInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA));
							}
						} else if (cha instanceof L1MonsterInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
							}
						}
					}
					if (_skillId != FOG_OF_SLEEPING) {
						cha.removeSkillEffect(FOG_OF_SLEEPING);
					}
					if (_skillId != PHANTASM) {
						cha.removeSkillEffect(PHANTASM);
					}
					// 失敗的情況下若再進行調整，請在下方添加。
					if (!isSuccess) {
						if (_skillId == FORCE_STUN) { // 失敗的技能是強力暈眩時
							// System.out.println("強力暈眩失敗了");
							_skillId = L1SkillId.SHOCK_STUN; // 中間改變 _skillId。
							isSuccess = _magic.calcProbabilityMagic(_skillId);
							if (!isSuccess) {
								// System.out.println("普通暈眩也失敗了");
							}
						}
					}
					if (isSuccess) {
						if (isUseCounterMagic(cha)) {
							iter.remove();
							continue;
						}
					} else {
						if (_skillId == FOG_OF_SLEEPING && cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_ServerMessage(297));
						} else if (_skillId == DESPERADO) {
							int effectId = 12758;
							if (_player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
								effectId = 17233;
							}
							_target.send_effect(effectId);
						}
						iter.remove();
						continue;
					}
				} else if (_skill.getType() == L1Skills.TYPE_HEAL) {
					dmg = -1 * _magic.calcHealing(_skillId);
					if (cha.hasSkillEffect(WATER_LIFE)) {
						dmg *= 2;
					}
					if (cha.hasSkillEffect(POLLUTE_WATER)) {
						dmg /= 2;
					}
					if (cha.hasSkillEffect(PAP_REDUCE_HELL)) {
						dmg /= 2;
					}
					if (cha.hasSkillEffect(BLACKELDER_DEATH_HELL) || cha.hasSkillEffect(DEATH_HEAL) || cha.hasSkillEffect(DEATH_HEAL_Mob)) {
						dmg = -dmg;
						if (cha.hasSkillEffect(WATER_LIFE)) {
							dmg *= 2;
						}
						if (cha.hasSkillEffect(POLLUTE_WATER)) {
							dmg /= 2;
						}
					}

					if (_calcType == PC_PC) {
						if (ownPc.getRedKnightClanId() != 0)
							ownPc.addRedKnightDamage(tarPc.getRedKnightClanId() == 0 ? -dmg : dmg);
					}
				}


				/**
				 * @link {L1SkillUse#hasInitialize}
				 **/
				// System.out.println(_skillId + " " + cha.hasSkillEffect(_skillId) + " " +
				//cha.getSkillEffectTimeSec(_skillId);

				if (hasInitialize.contains(_skillId)) {
					if (cha.hasSkillEffect(_skillId)) {
						cha.removeSkillEffect(_skillId);
					}
					cha.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000);
				} else {
					if (!nonAddMagicList.contains(_skillId)) {
						if (_skillId == BURNING_SHOT) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(BURNING_SHOT)) {
									pc.removeSkillEffect(STATUS_FREEZE);
									pc.removeSkillEffect(BURNING_SHOT);
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, false));
									pc.getResistance().addcalcPcDefense(-10);
									pc.addSpecialResistance(eKind.ALL, -3);
									SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
									L1SkillUse.off_icons(pc, BURNING_SHOT);
									pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_POSSIBLE_RANGE, pc, pc.getWeapon()), true);
									return;
								}
							}
						}
						// 공격스킬 위주로 넣어주면 된다.
						if (cha.hasSkillEffect(_skillId) && !hasNonAddMagicList.contains(_skillId)) {
							addMagicList(cha, true);
							if (_skillId != SHAPE_CHANGE) {
								continue;
							}
						}
					}
				}
				// ●●●● PC, NPC 양쪽 모두 효과가 있는 스킬 ●●●●
				// GFX Check (Made by HuntBoy)
//				System.out.println("1: "+_skillId);
				if (_target instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) _target;
					DCCD = CalcStat.calcPureDecreaseCCDuration(target.getAbility().getCha())+CalcStat.calcDecreaseCCDuration(target.getAbility().getTotalCha())
							+ target.get_status_time_reduce();
				}
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					ICCD = pc.get_CC_Increase();
				}
				switch (_skillId) {

					case ASURA:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(ASURA)) {
								pc.removeSkillEffect(ASURA);
							}
							pc.doAsura();
						}
						break;
					/** 셰이프 체인지 이동. **/
					case SHAPE_CHANGE:
						useShape_Change(cha);
						break;
					case N_BUFF_PVP_DMG:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getResistance().addPVPweaponTotalDamage(1);
							pc.getResistance().addcalcPcDefense(1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_HPMP:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addMaxHp(50);
							pc.addMaxMp(50);
							pc.addWeightReduction(3);
							pc.sendPackets(new S_HPUpdate(pc));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_DMG:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDmgup(1);
							pc.addBowDmgup(1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_REDUCT:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_SP:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addSp(1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_STUN:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpecialResistance(eKind.ABILITY, 2); // 옵션
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_HOLD:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpecialResistance(eKind.SPIRIT, 2); // 옵션
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_WATER_DMG:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_WIND_DMG:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_EARTH_DMG:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_FIRE_DMG:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_STR:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedStr((byte) 1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_DEX:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedDex((byte) 1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_INT:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedInt((byte) 1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_WIS:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedWis((byte) 1);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						}
						break;
					case N_BUFF_WATER:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getResistance().addWater(5);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
						break;
					case N_BUFF_WIND:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getResistance().addWind(5);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
						break;
					case N_BUFF_EARTH:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getResistance().addEarth(5);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
						break;
					case N_BUFF_FIRE:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getResistance().addFire(5);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
						break;
					case N_BUFF_ALL_RESIST:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getResistance().addFire(5);
							pc.getResistance().addEarth(5);
							pc.getResistance().addWater(5);
							pc.getResistance().addWind(5);
							pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
						break;
					case SOUL_BARRIER: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setSkillEffect(L1SkillId.SOUL_BARRIER, _skill.getBuffDuration() * 1000);
						}
					}
					break;
					case L1SkillId.FOCUS_SPRITS: {
						L1PcInstance pc = (L1PcInstance) _user;
						pc.add_magic_critical_rate(5);
						// on_icons(pc, L1SkillId.FOCUS_SPRITS, _getBuffIconDuration);
					}
					break;
					// 큐브 스킬 파티원 화면상에 있지 않을경우 들어가지 않게
					case CUBE_AVATAR:
					case CUBE_RICH:
					case CUBE_GOLEM:
					case CUBE_OGRE:
						useCubeMagic(_skillId);
						break;
					case IMPACT: {
						if (_user instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _user;
							L1Party party = pc.getParty();
							int impactUp = 5;
							int lvl = pc.getLevel();
							if (lvl >= 84)
								impactUp += ((lvl - 84) / 3 )* 2;
							if (impactUp >= 15){
								impactUp = 15;
							}

							ArrayList<L1PcInstance> members = new ArrayList<L1PcInstance>();
							members.add(pc);
							if (party != null) {
								for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
									if (party.isMember(player))
										members.add(player);
								}
							}

							for (int i = members.size() - 1; i >= 0; --i) {
								L1PcInstance member = members.get(i);
								if (member.hasSkillEffect(L1SkillId.IMPACT)) {
									member.removeSkillEffect(L1SkillId.IMPACT);
									on_icons(pc, _skillId, _skill.getBuffDuration());
									member.sendPackets(new S_SkillSound(member.getId(), 14513));
								} else {
									on_icons(pc, _skillId, _skill.getBuffDuration());
									member.sendPackets(new S_SkillSound(member.getId(), 14513));
								}

								member.setImpactUp(impactUp);
								member.addSpecialPierce(eKind.ALL, impactUp);
								member.setSkillEffect(L1SkillId.IMPACT, _skill.getBuffDuration() * 1000);
								Broadcaster.broadcastPacket(member, new S_SkillSound(member.getId(), 14513));
								SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(member);
							}
						}
					}
					break;
					case DRAGON_HALPAS: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.DRAGON_HALPAS)) {
							pc.removeSkillEffect(L1SkillId.DRAGON_HALPAS);
						}
						on_icons(pc, _skillId, _skill.getBuffDuration());
						pc.addDmgup(10);
					}
					break;
					case REDUCTION_ARMOR: {
						L1PcInstance pc = (L1PcInstance) cha;
						int reduc_armor_value = pc.getLevel() >= 50 ? (pc.getLevel() - 45) / 5 : 0;
						pc.addDamageReductionByArmor(reduc_armor_value);
						pc.set_reducreduction_value(reduc_armor_value);
						if (pc != null && pc.isPassive(MJPassiveID.REDUCTION_ARMOR_VETERAN.toInt())) {
							int bonus = 0;
							if (pc.getLevel() >= 80 && pc.getLevel() <= 100) {
								bonus = ((pc.getLevel() - 80) / 4) + 1;
							} else {
								bonus = 5;
							}
							pc.set_pvp_defense(bonus);
							pc.addSpecialResistance(eKind.FEAR, 3);
							pc.set_reduction_armor_veteran(true);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
					}
					break;
					case LUCIFER: {
						if (_user instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _user;
							if (pc.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
								pc.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
							}
							if (pc.isPassive(MJPassiveID.LUCIFER_DESTINY.toInt())) {
								int bonus = 0;
								if (pc.getLevel() >= 85 && pc.getLevel() <= 94)
									bonus = pc.getLevel() - 85;
								else if (pc.getLevel() >= 95)
									bonus = 10;

								pc.add_pvp_defense_per(bonus);
								pc.setLucifer_destiny(true);
								on_icons(pc, _skillId, _skill.getBuffDuration() * 2);
							} else {
								on_icons(pc, _skillId, _skill.getBuffDuration());
							}
						}
					}
					break;
					case TITANL_RISING: {
						if (_user instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _user;

							int upLevel = pc.getLevel() - 80;
							if (upLevel >= 5)
								upLevel = 5;
							pc.setRisingUp(5 + upLevel);

							// on_icons(pc, L1SkillId.TITANL_RISING, _getBuffIconDuration);
						}
					}
					break;
					case ABSOLUTE_BLADE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							// SC_SPELL_BUFF_NOTI.sendDatabaseIcon(pc, _skill, _getBuffIconDuration, true);
						}
					}
					break;
					case DEATH_HEAL_Mob:
					case DEATH_HEAL: {
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							int chance = random.nextInt(10) + 1;
							if (pc.hasSkillEffect(DEATH_HEAL)) {
								pc.sendPackets(S_InventoryIcon.icoReset(DEATH_HEAL, chance, false));
								pc.removeSkillEffect(DEATH_HEAL);
							} else
								pc.sendPackets(S_InventoryIcon.icoNew(DEATH_HEAL, chance, false));
							pc.setSkillEffect(DEATH_HEAL, chance * 1000);
							pc.sendPackets(new S_SkillSound(pc.getId(), 14501));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 14501));
						}
					}
					break;
					case GRACE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.GRACE)) {
								pc.removeSkillEffect(L1SkillId.GRACE);
							}
							pc.setGraceLv(pc.getLevel());

							int resistance = 1 + pc.getGraceLv();
							pc.addSpecialResistance(eKind.ALL, resistance);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
							pc.setSkillEffect(L1SkillId.GRACE, _getBuffIconDuration * 1000);

							for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
								if (pc.getParty() != null) {
									if (pc.getParty().isMember(player) && player != null) {
										if (player.hasSkillEffect(L1SkillId.GRACE)) {
											player.removeSkillEffect(L1SkillId.GRACE);
										}
										on_icons(player, _skillId, _getBuffIconDuration);
										pc.send_tarobj_party_effect(player.getId(), 14495);
										player.setGraceLv(pc.getLevel());
										player.addSpecialResistance(eKind.ALL, resistance);
										SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(player);
										player.setSkillEffect(L1SkillId.GRACE, _getBuffIconDuration * 1000);
									}
								}
							}
						}
						if (_user instanceof L1PcInstance) {
							L1PinkName.onHelp(cha, _user);
						}
					}
					break;
					case TROGIR_MILPITAS3: {
						S_Sound.broadcast(cha, 11002);
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case TROGIR_MILPITAS4: {
						S_Sound.broadcast(cha, 11003);
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case TROGIR_MILPITAS5: {
						S_Sound.broadcast(cha, 11004);
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case TROGIR_MILPITAS6: {
						S_Sound.broadcast(cha, 11008);
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case TROGIR_MILPITAS2: {
						S_Sound.broadcast(cha, 11009);
						S_Sound.broadcast(cha, 11006);
						S_SkillSound.broadcast(cha, 11473);
						if (cha instanceof L1PcInstance) {
							L1SpawnUtil.spawn((L1PcInstance) cha, 181163, 5, 6 * 2000, true);
						}
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case MOB_ETERNITI: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.send_effect(18418);
						}
					}
					break;
					case IMMUNE_TO_HARM_BLADE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;

							Random _rnd = new Random(System.nanoTime());
							int _dmgMin = 0;
							int _dmgMax = 0;

							if (pc.isDead())
								continue;

							if (pc.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)/* && _rnd.nextInt(100) <= 100 */) {
								S_OnlyEffect s_oe = new S_OnlyEffect(pc.getId(), 15961);
								pc.sendPackets(s_oe, false);
								Broadcaster.broadcastPacket(pc, s_oe, true);
								pc.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
							}

							double imm = pc.getImmuneReduction();
							int min = _dmgMin;
							int max = _dmgMax;
							if (imm != 0) {
								min -= (int) ((double) min * imm);
								max -= (int) ((double) max * imm);
							}
						}
					}
					break;
					case BRAVE_UNION:
						_player.setSkillEffect(_skillId, _getBuffDuration);
						on_icons(_player, _skillId, _getBuffIconDuration);
						break;
					case IMMUNE_TO_HARM:
						if (_target.hasSkillEffect(L1SkillId.LUCIFER)) {
							_target.removeSkillEffect(L1SkillId.LUCIFER);
						}

						if (_target != null) {
							if (_skill.getType() == L1SkillUse.TYPE_GMBUFF)
								((L1PcInstance) _target).setLastImmuneLevel(_player.getLevel());
							if (_player != null) {
								if (_player.getId() == _target.getId()) {
									if (_player.isPassive(MJPassiveID.IMMUNETOHARM_SAINT.toInt())) {
										int bonus = 0;
										int level90_bonus = _player.getLevel() >= 90 ? (_player.getLevel() - 90) : 0;
										if (_player.getLevel() >= 80 && _player.getLevel() <= 89)
											bonus = ((_player.getLevel() - 80) / 2) + 1;
										else if (_player.getLevel() >= 90)
											bonus = 5 + level90_bonus;

										_player.add_pvp_defense_per(bonus);
										_player.setImmunetoharm_saini(true);
										on_icons_passive(_player, MJPassiveID.IMMUNETOHARM_SAINT, _skill.getBuffDuration() * 2);
										((L1PcInstance) _target).setLastImmuneLevel(Config.MagicAdSetting_Wizard.IMMUNELEVEL);
//									System.out.println("+처리 bonus : " + bonus + "/ level90_bonus : " + level90_bonus + "/ pvpdefenseper : " + _player.get_pvp_defense_per());
									} else {
										((L1PcInstance) _target).setLastImmuneLevel(Config.MagicAdSetting_Wizard.IMMUNELEVEL);
										on_icons(_player, _skillId, _getBuffIconDuration);
									}
								} else if (_target instanceof L1PcInstance) {
									L1PcInstance targetPc = (L1PcInstance) _target;
									((L1PcInstance) _target).setLastImmuneLevel(_player.getLevel());
									on_icons(targetPc, _skillId, _getBuffIconDuration);
								}
							}
						}

						if (_user instanceof L1PcInstance) {
							L1PinkName.onHelp(cha, _user);
						}
						break;
					case MATH_IMMUNE_TO_HARM: {
						L1PcInstance pc = (L1PcInstance) cha;
						int skill_time = _getBuffIconDuration;
						L1Party party = pc.getParty();
						int castgfx = 15959;

						if (_skill.getType() == L1SkillUse.TYPE_GMBUFF)
							pc.setLastImmuneLevel(pc.getLevel());
						if (pc != null) {
							if (pc.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM))
								pc.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);

							pc.setLastImmuneLevel(Config.MagicAdSetting_Wizard.IMMUNELEVEL);
							if (pc.isPassive(MJPassiveID.IMMUNETOHARM_SAINT.toInt())) {
								castgfx = 19015;
								skill_time = 60;
								int bonus = 0;
								int level90_bonus = _player.getLevel() >= 90 ? (_player.getLevel() - 90) : 0;
								if (_player.getLevel() >= 80 && _player.getLevel() <= 89)
									bonus = ((_player.getLevel() - 80) / 2) + 1;
								else if (_player.getLevel() >= 90)
									bonus = 5 + level90_bonus;

								pc.add_pvp_defense_per(bonus);
								pc.setImmunetoharm_saini(true);
//							System.out.println("+처리 bonus : " + bonus + "/ level90_bonus : " + level90_bonus + "/ pvpdefenseper : " + pc.get_pvp_defense_per());
							}
							pc.setSkillEffect(L1SkillId.IMMUNE_TO_HARM, skill_time * 1000);
							pc.send_effect(castgfx);
//						on_icons(pc, L1SkillId.IMMUNE_TO_HARM, skill_time);
							on_icons_passive(_player, MJPassiveID.IMMUNETOHARM_SAINT, skill_time);

							S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
							_user.sendPackets(gfx);
							_user.broadcastPacket(gfx);
						}

						if (pc.getMapId() == 13006 || pc.getMapId() == 13007) {
							if (DeathMatchSystem.getInstance().getTeamRed().contains(pc)) {
								for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
									if (!L1World.getInstance().getVisiblePlayer(pc, 8).contains(player)) {
										continue;
									}
									skill_time = 32;
									if (player.hasSkillEffect(L1SkillId.LUCIFER)) {
										player.removeSkillEffect(L1SkillId.LUCIFER);
									} else if (player.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
										player.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
									}

									if (player != null) {
										player.setLastImmuneLevel(pc.getLevel());
										player.send_effect(15959);
									} // 일부러 break 걸지 않음. pink name 설정 해줘야 하기 때문.
									if (player instanceof L1PcInstance) {
										on_icons(player, L1SkillId.IMMUNE_TO_HARM, skill_time);
										player.setSkillEffect(L1SkillId.IMMUNE_TO_HARM, skill_time * 1000);
									}
								}
							} else if (DeathMatchSystem.getInstance().getTeamBlue().contains(pc)) {
								for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
									if (!L1World.getInstance().getVisiblePlayer(pc, 8).contains(player)) {
										continue;
									}
									skill_time = 32;
									if (player.hasSkillEffect(L1SkillId.LUCIFER)) {
										player.removeSkillEffect(L1SkillId.LUCIFER);
									} else if (player.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
										player.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
									}

									if (player != null) {
										player.setLastImmuneLevel(pc.getLevel());
										player.send_effect(15959);
									} // 일부러 break 걸지 않음. pink name 설정 해줘야 하기 때문.
									if (player instanceof L1PcInstance) {
										on_icons(player, L1SkillId.IMMUNE_TO_HARM, skill_time);
										player.setSkillEffect(L1SkillId.IMMUNE_TO_HARM, skill_time * 1000);
									}
								}
							}
						} else {
							if (party != null) {
								for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 8)) {
									if (pc.getClan() != player.getClan())
										continue;

									if (pc.getParty().isMember(player)) {
										skill_time = 32;
										if (player.hasSkillEffect(L1SkillId.LUCIFER)) {
											player.removeSkillEffect(L1SkillId.LUCIFER);
										} else if (player.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
											player.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
										}

										if (player != null) {
											player.setLastImmuneLevel(pc.getLevel());
											player.send_effect(15959);
										} // 일부러 break 걸지 않음. pink name 설정 해줘야 하기 때문.
										if (player instanceof L1PcInstance) {
											on_icons(player, L1SkillId.IMMUNE_TO_HARM, skill_time);
											player.setSkillEffect(L1SkillId.IMMUNE_TO_HARM, skill_time * 1000);
										}
									}
								}
							}
						}
					}

					if (_user instanceof L1PcInstance) {
						L1PinkName.onHelp(cha, _user);
					}
					break;
					case IllUSION_OGRE:
					case CONCENTRATION:
					case PATIENCE: {
						if (_user instanceof L1PcInstance) {
							L1PinkName.onHelp(cha, _user);
						}
					}
					break;
					case doctor_skills1: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillSound(pc.getId(), 20058));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 20058));
							dmg = _magic.calcMagicDamage(_skillId);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.sendPackets(new S_SkillSound(npc.getId(), 20058));
							npc.broadcastPacket(new S_SkillSound(npc.getId(), 20058));
						}
					}
					break;
					case Phoenix_Skill1: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillSound(pc.getId(), 16258));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 16258));
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case Phoenix_Skill2: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillSound(pc.getId(), 16299));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 16299));
							dmg = _magic.calcMagicDamage(_skillId);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.sendPackets(new S_SkillSound(npc.getId(), 16299));
							npc.broadcastPacket(new S_SkillSound(npc.getId(), 16299));
						}
					}
					break;
					case DRAKE_Skill1: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillSound(pc.getId(), 16333));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 16333));
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case DRAKE_Skill2: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillSound(pc.getId(), 16335));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 16335));
						dmg = _magic.calcMagicDamage(_skillId);
					}
					break;
					case miso_Buff: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(miso_Buff)) {
								pc.sendPackets(S_InventoryIcon.icoReset(miso_Buff, 4995, _skill.getBuffDuration(), true));
							} else {
								pc.sendPackets(S_InventoryIcon.icoNew(miso_Buff, 4995, _skill.getBuffDuration(), true));
							}
							pc.add_item_exp_bonus(10);
						}
					}
					break;
					case miso_Buff1: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
						/*if (pc.hasSkillEffect(miso_Buff1)) {
							pc.sendPackets(S_InventoryIcon.icoReset(miso_Buff1, 4996, _skill.getBuffDuration(), true));
						} else {*/
							pc.getResistance().addMr(10);
							pc.addDamageReductionByArmor(2);
							pc.addMaxHp(100);
							pc.addHpr(2);
							pc.sendPackets(S_InventoryIcon.icoNew(miso_Buff1, 4996, _skill.getBuffDuration(), true));
						}
					}
//				}
					break;
					case miso_Buff2: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
						/*if (pc.hasSkillEffect(miso_Buff2)) {
							pc.sendPackets(S_InventoryIcon.icoReset(miso_Buff2, 4997, _skill.getBuffDuration(), true));
						} else {*/
							pc.addDmgup(3);
							pc.addBowDmgup(3);
							pc.getAbility().addSp(2);
							pc.addMaxMp(50);
							pc.addMpr(2);
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(S_InventoryIcon.icoNew(miso_Buff2, 4997, _skill.getBuffDuration(), true));
						}
					}
//				}
					break;
					case DRAGON_SET: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(DRAGON_SET)) {
								pc.sendPackets(S_InventoryIcon.icoReset(DRAGON_SET, 5016, _skill.getBuffDuration(), true));
							} else {
								pc.addDmgup(3);
								pc.addDmgRate(3);
								pc.addBowDmgup(3);
								pc.addBowHitup(3);
								pc.getAC().addAc(-3);
								pc.getAbility().addSp(2);
								pc.add_item_exp_bonus(2);
								pc.sendPackets(new S_OwnCharAttrDef(pc));
								pc.sendPackets(S_InventoryIcon.icoNew(DRAGON_SET, 5016, _skill.getBuffDuration(), true));
							}
						}
					}
					break;
					case HUNTER_BLESS: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(HUNTER_BLESS)) {
								pc.sendPackets(S_InventoryIcon.icoReset(HUNTER_BLESS, 4992, _skill.getBuffDuration(), true));
							} else {
								pc.sendPackets(S_InventoryIcon.icoNew(HUNTER_BLESS, 4992, _skill.getBuffDuration(), true));
							}
							pc.add_item_exp_bonus(5);
						}
					}
					break;
					case HUNTER_BLESS3: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(HUNTER_BLESS3)) {
								pc.sendPackets(S_InventoryIcon.icoReset(HUNTER_BLESS3, 993, _skill.getBuffDuration(), true));
							} else {
								pc.sendPackets(S_InventoryIcon.icoNew(HUNTER_BLESS3, 993, _skill.getBuffDuration(), true));
							}
							pc.add_item_exp_bonus(2);
						}
					}
					break;
					case MOB_HASTE:
					case HASTE: {
						if (cha.getMoveSpeed() != 2) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.getHasteItemEquipped() > 0) {
									continue;
								}
								pc.setDrink(false);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration));
							}
							cha.broadcastPacket(new S_SkillHaste(cha.getId(), 1, 0));
							cha.setMoveSpeed(1);
						} else {
							int skillNum = 0;
							if (cha.hasSkillEffect(SLOW)) {
								skillNum = SLOW;
							} else if (cha.hasSkillEffect(MOB_SLOW_1)) {
								skillNum = MOB_SLOW_1;
							} else if (cha.hasSkillEffect(MOB_SLOW_18)) {
								skillNum = MOB_SLOW_18;
							}
							if (skillNum != 0) {
								cha.removeSkillEffect(skillNum);
								cha.removeSkillEffect(HASTE);
								cha.removeSkillEffect(MOB_HASTE);
								cha.setMoveSpeed(0);
								continue;
							}
						}
					}
					break;
					case CURE_POISON: {
						cha.curePoison();
					}
					break;
					/*
					 * case DRESS_EVASION: { L1PcInstance pc = (L1PcInstance) cha; pc.addEffectedER(18); pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER())); // on_icons(pc, _skillId, _getBuffIconDuration); } break;
					 */
					case AQUA_PROTECTER: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addEffectedER(5);
						// on_icons(pc, _skillId, _getBuffIconDuration);
					}
					break;
					case BOUNCE_ATTACK: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(6);
					}
					break;
					case STRIKER_GALE:
						if (_player instanceof L1PcInstance){
							if (_player.getPassive(MJPassiveID.STRIKER_GAIL_SHOT.toInt()) != null) {
								//						int[] stunTimeArray = null;
								int effectNpcId = 73201282;

								int[] stunTimeArray = Config.MagicAdSetting_Elf.STRIKERGAILSHOT_MS;

								//						stunTimeArray = new int[]{ 2100, 2300, 2500, 2700, 2900, 3100, 3300, 3500, 3700, 3900, 4000, 4200, 4400, 4600, 4800, 5000 };

								int rnd = random.nextInt(stunTimeArray.length);
								int time = stunTimeArray[rnd];
								_isElvenstrike = _magic.calcProbabilityMagic(_skillId);
								if (_target instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) _target;
									if (_isElvenstrike) {
										pc.setStrikerGailShot(true);
										L1EffectSpawn.getInstance().spawnEffect(effectNpcId, time - 500, _target.getX(), _target.getY(), _target.getMapId());
										pc.setSkillEffect(L1SkillId.POWERRIP, time);
										pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
									} else {
										pc.send_effect(20476);
									}
								} else if (_target instanceof L1MonsterInstance || _target instanceof L1NpcInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
									L1NpcInstance npc = (L1NpcInstance) _target;
									if (_isElvenstrike) {
										npc.setStrikerGailShot(true);
										L1EffectSpawn.getInstance().spawnEffect(effectNpcId, time - 500, _target.getX(), _target.getY(), _target.getMapId());
										npc.setSkillEffect(L1SkillId.POWERRIP, time);
										npc.set발묶임상태(true);
									} else {
										npc.send_effect(20476);
									}
								}
							}
						}
						break;
//				case SOLID_CARRIAGE: {
//					L1PcInstance pc = (L1PcInstance) cha;
//					pc.addEffectedER(15);
//				}
//					break;
					case REMOVE_CURSE: {
						cha.curePoison();
						if (cha.hasSkillEffect(STATUS_CURSE_PARALYZING) || cha.hasSkillEffect(STATUS_CURSE_PARALYZED) || cha.hasSkillEffect(ANTA_MESSAGE_1) || cha.hasSkillEffect(ANTA_MESSAGE_6)
								|| cha.hasSkillEffect(ANTA_MESSAGE_7) || cha.hasSkillEffect(ANTA_MESSAGE_8) || cha.hasSkillEffect(OMAN_STUN) || cha.hasSkillEffect(DRAGON_HALPAS_STUN)
								|| cha.hasSkillEffect(BALOCH_STUN) || cha.hasSkillEffect(BOS_STUN18) || cha.hasSkillEffect(Maeno_STUN) || cha.hasSkillEffect(Besi_STUN) || cha.hasSkillEffect(fornos_STUN)
								|| cha.hasSkillEffect(Moster_STUN) || cha.hasSkillEffect(MOSTER_STUN_1)) {
							cha.cureParalaysis();
						}
						if (cha.hasSkillEffect(CURSE_BLIND) || cha.hasSkillEffect(DARKNESS)) {
							if (cha.hasSkillEffect(CURSE_BLIND)) {
								cha.removeSkillEffect(CURSE_BLIND);
							} else if (cha.hasSkillEffect(DARKNESS)) {
								cha.removeSkillEffect(DARKNESS);
							}
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_CurseBlind(0));
							}
						}
					}
					break;
					case RESURRECTION:
					case GREATER_RESURRECTION: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_player.getId() != pc.getId()) {
								if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
									for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
										if (!visiblePc.isDead()) {
											_player.sendPackets(new S_ServerMessage(592));
											return;
										}
									}
								}
								/** 攻城戰場無法復活 **/
								int castle_id = L1CastleLocation.getCastleIdByArea(pc);
								if (castle_id != 0) {
									pc.sendPackets(new S_SystemMessage("無法復活的位置。"));
									return;
								}
								if (/* pc.getCurrentHp() == 0 && */ pc.isDead()) {
									if (pc.getMap().isUseResurrection()) {
										if (_skillId == RESURRECTION) {
											pc.setGres(false);
										} else if (_skillId == GREATER_RESURRECTION) {
											pc.setGres(true);
										}
										pc.setTempID(_player.getId());
										pc.sendPackets(new S_Message_YN(322, ""));
									}
								}
							}
						}
						if (cha instanceof L1NpcInstance) {
							if (!(cha instanceof L1TowerInstance)) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								if (npc.getNpcTemplate().isCantResurrect() && !(npc instanceof L1PetInstance) && !(npc instanceof MJCompanionInstance)) {
									return;
								}
								if ((npc instanceof MJCompanionInstance || npc instanceof L1PetInstance) && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
									for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
										if (!visiblePc.isDead()) {
											_player.sendPackets(new S_ServerMessage(592));
											return;
										}
									}
								}
								if (npc.getCurrentHp() == 0 && npc.isDead()) {
									npc.resurrect(npc.getMaxHp() / 4);
									npc.setResurrect(true);
								}
							}
						}
					}
					break;
					case CALL_OF_NATURE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_player.getId() != pc.getId()) {
								if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
									for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
										if (!visiblePc.isDead()) {
											_player.sendPackets(new S_ServerMessage(592));
											return;
										}
									}
								}
								if (pc.getCurrentHp() == 0 && pc.isDead()) {
									pc.setTempID(_player.getId());
									pc.sendPackets(new S_Message_YN(322, ""));
								}
							}
						}
						if (cha instanceof L1NpcInstance) {
							if (!(cha instanceof L1TowerInstance)) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								if ((npc instanceof L1PetInstance || npc instanceof MJCompanionInstance) && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
									for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
										if (!visiblePc.isDead()) {
											_player.sendPackets(new S_ServerMessage(592));
											return;
										}
									}
								}
								if (npc.getCurrentHp() == 0 && npc.isDead()) {
									npc.resurrect(cha.getMaxHp());
									npc.resurrect(cha.getMaxMp() / 100);
									npc.setResurrect(true);
								}
							}
						}
					}
					break;
					// UI DG표시
					case UNCANNY_DODGE: // 언케니닷지
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDg(30);
						}
						break;
					case DETECTION:
					case IZE_BREAK:
					case EYE_OF_DRAGON: {
						if (cha instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							int hiddenStatus = npc.getHiddenStatus();
							if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
								npc.appearOnGround(_player);
							}
						}
					}
					break;
					case COUNTER_DETECTION: {
						if (cha instanceof L1PcInstance) {
							dmg = _magic.calcMagicDamage(_skillId);
						} else if (cha instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							int hiddenStatus = npc.getHiddenStatus();
							if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
								npc.appearOnGround(_player);
							} else {
								dmg = 0;
							}
						} else {
							dmg = 0;
						}
					}
					break;
					case MIND_BREAK: {
						if (_target.getCurrentMp() >= 5) {
							_target.setCurrentMp(_target.getCurrentMp() - 5);
							dmg = 15;
						} else {
							return;
						}
					}
					break;
					case TRUE_TARGET: {
						if (_user instanceof L1PcInstance) {
							L1PcInstance pri = (L1PcInstance) _user;
							pri.sendPackets(new S_TrueTargetNew(_targetID, true));
							if (_target instanceof L1PcInstance) {
								int step = pri.getLevel() / 15;
								L1PcInstance target = (L1PcInstance) _target;
								if (step > 0) {
									if (_player.getClanid() != 0) {
										_target.setTrueTargetClan(_player.getClanid());
									}
									if (_player.getPartyID() != 0) {
										_target.setTrueTargetParty(_player.getPartyID());
									}
									target.setTrueTarget(step);
								}
							} else if (_target instanceof L1NpcInstance) {
								int step = pri.getLevel() / 15;
								L1NpcInstance target = (L1NpcInstance) _target;
								if (step > 0) {
									if (_player.getClanid() != 0) {
										_target.setTrueTargetClan(_player.getClanid());
									}
									if (_player.getPartyID() != 0) {
										_target.setTrueTargetParty(_player.getPartyID());
									}
									target.setTrueTarget(step);
								}
							}

							for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(_target)) {
								if (pri.getClanid() == pc.getClanid()) {
									pc.sendPackets(new S_TrueTargetNew(_targetID, true));
								}
							}
							// 이전에 시전한 트루타겟 찾아서 강제 종료 시키기.
							synchronized (_truetarget_list) {
								L1Object temp = _truetarget_list.remove(_user.getId());
								if (temp != null && temp instanceof L1Character) {
									L1Character temp2 = (L1Character) temp;
									temp2.removeSkillEffect(L1SkillId.TRUE_TARGET);
								}
							}
							_target.setSkillEffect(L1SkillId.TRUE_TARGET, _skill.getBuffDuration() * 1000);
							synchronized (_truetarget_list) {
								_truetarget_list.put(_user.getId(), _target);
							}
						}
					}
					break;
					case FREEZEENG_ARMOR:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							// on_icons(pc, FREEZEENG_ARMOR, _getBuffIconDuration);
							pc.addEffectedER(5);
						}
						break;
/*				case MOEBIUS:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.MOEBIUS)) {
							pc.removeSkillEffect(L1SkillId.MOEBIUS);
						}
						int bonus = (pc.getLevel() - 85) + 20;
						if (pc.getLevel() >= 95)
							bonus = 30;
						pc.add_pvp_defense_per(bonus);
					}
					break;*/
					case ELEMENTAL_FALL_DOWN: {
						if (_user instanceof L1PcInstance) {
							int playerAttr = _player.getElfAttr();
							int i = -50;
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								switch (playerAttr) {
									case 0:
										_player.sendPackets(new S_ServerMessage(79));
										break;
									case 1:
										pc.getResistance().addEarth(i);
										pc.setAddAttrKind(1);
										break;
									case 2:
										pc.getResistance().addFire(i);
										pc.setAddAttrKind(2);
										break;
									case 4:
										pc.getResistance().addWater(i);
										pc.setAddAttrKind(4);
										break;
									case 8:
										pc.getResistance().addWind(i);
										pc.setAddAttrKind(8);
										break;
									default:
										break;
								}
								pc.addSpecialPierce(eKind.SPIRIT, -10);
								SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
							} else if (cha instanceof L1MonsterInstance) {
								L1MonsterInstance mob = (L1MonsterInstance) cha;
								switch (playerAttr) {
									case 0:
										_player.sendPackets(new S_ServerMessage(79));
										break;
									case 1:
										mob.getResistance().addEarth(i);
										mob.setAddAttrKind(1);
										break;
									case 2:
										mob.getResistance().addFire(i);
										mob.setAddAttrKind(2);
										break;
									case 4:
										mob.getResistance().addWater(i);
										mob.setAddAttrKind(4);
										break;
									case 8:
										mob.getResistance().addWind(i);
										mob.setAddAttrKind(8);
										break;
									default:
										break;
								}
							}
						}
					}
					break;
					case HEAL:
					case EXTRA_HEAL:
					case GREATER_HEAL:
					case FULL_HEAL:
					case HEAL_ALL:
					case NATURES_BLESSING: {
						if (cha instanceof L1PcInstance) {
							cha.killSkillEffectTimer(WATER_LIFE);
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_PacketBox(S_PacketBox.DEL_ICON));
						}
					}
					break;
					case CHILL_TOUCH:
					case VAMPIRIC_TOUCH: {
						heal = dmg / 2;
					}
					break;
					case TRIPLE_ARROW: {
						int playerGFX = _player.getCurrentSpriteId();
						if (_player.getWeapon() == null)
							break;
						int weaponType = _player.getWeapon().getItem().getType1();
						if (weaponType != 20 || playerGFX == 3784)
							return;

						if (_player.getWeapon() != null) {
							if (_player.getWeapon().getItemId() != 202011 && _player.getWeapon().getItemId() != 190 && _player.getWeapon().getItem().getType2() == 1
									&& (_player.getWeapon().getItem().getType() == 4 || _player.getWeapon().getItem().getType() == 10 || _player.getWeapon().getItem().getType() == 13)
									&& _player.getInventory().getArrow() == null) {
								_player.sendPackets(4492);
								return;
							}
						}

						_player.TripleArrow = true;
						if (_player.isPassive(MJPassiveID.TRIPLE_BOOST.toInt())){
							for (int i = 4; i>0 ; i--){
								_target.onAction(_player);
							}
						} else {
							for (int i =3 ;i > 0; i--) {
								_target.onAction(_player);
							}
						}

						_player.TripleArrow = false;

						if (_player.isPassive(MJPassiveID.TRIPLE_BOOST.toInt())){
							_player.send_effect(20750);
						} else {
							if (_player.isPassive(MJPassiveID.GLORY_EARTH.toInt())) {
								_player.send_effect(19317);
							} else {
								_player.send_effect(15103);
							}
						}

						// 트리플 스턴
						Random random = new Random(System.nanoTime());
						L1Magic magic = new L1Magic(_player, _target);
						if (_player.getWeapon() != null) {
							L1ItemInstance weapon = _player.getWeapon();
							if (weapon.getItemId() == 7000263) {
								if (_player.getLocation().getTileLineDistance(new Point(_target.getLocation())) <= 3) {
									if (!_player.hasSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME)) {
										int timeMillis = 6000;
										_player.setSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME, timeMillis);
										if (magic.calcProbabilityMagic(TRIPLE_STUN)) {
											int StunDuration = 0;
											int SpawnEffect = 8503099;// 스폰 이펙트

											int[] stunTimeArray = Config.MagicAdSetting_Elf.TRIPLE_STUN_TIME_ARRAY;
											StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
											if (DCCD != 0) {
												StunDuration -=DCCD;
											}
											if (ICCD != 0) {
												StunDuration += ICCD;
											}
											if (StunDuration <= 0) {
												StunDuration = 100;
											}
											if (StunDuration <= 0) {
												System.out.println(_skillId + " 持續時間錯誤");
												return ;
											}
											L1EffectSpawn.getInstance().spawnEffect2(SpawnEffect, L1SkillId.TRIPLE_STUN, _target.getX(), _target.getY(), _target.getMapId(), _target);
											//									SC_WORLD_PUT_OBJECT_NOTI.make_stream(_target, 18604, true);
											if (_target instanceof L1PcInstance) {
												L1PcInstance pc = (L1PcInstance) _target;
												pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
												//										pc.setSkillEffect(L1SkillId.SHOCK_STUN, StunDuration);
												pc.setSkillEffect(L1SkillId.TRIPLE_STUN, StunDuration);
												on_icons(pc, TRIPLE_STUN, StunDuration/1000);
												pc.send_effect(18539);
											} else if (_target instanceof L1MonsterInstance
													|| _target instanceof L1SummonInstance || _target instanceof L1PetInstance
													|| _target instanceof MJCompanionInstance) {
												L1NpcInstance npc = (L1NpcInstance) _target;
												npc.send_effect(18539);
												npc.setParalyzed(true);
												npc.setParalysisTime(StunDuration);
												npc.setSkillEffect(L1SkillId.TRIPLE_STUN, StunDuration);
											}
											//_player.setSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME, ReSkilldelay);// 쿨타임
										}
									}
								}
							}
						}
					}
					break;
					case MOB_TRIPLE_ARROW: {
						_user.MoBTripleArrow = true;
						for (int i = 3; i > 0; i--) {
							_target.onAction(_user);
						}
						_user.MoBTripleArrow = false;
						_user.sendPackets(new S_SkillSound(_user.getId(), 11764));// 11764
						Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 11764));
					}
					break;
					case MOB_TRIPLE_ARROW_PRISON: {
						_user.MoBTripleArrow_PRISON = true;
						for (int i = 3; i > 0; i--) {
							_target.onAction(_user);
						}
						_user.MoBTripleArrow_PRISON = false;
						_user.sendPackets(new S_SkillSound(_user.getId(), 11764));
						Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 11764));
					}
					break;
					case erzabe_worms: // 에르자베 이럽션
					case Sand_worms: { // 샌드웜 이럽션
						for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(_user, _skill.getRanged())) {
							if (!target.isDead())
								_user.broadcastPacket(new S_UseAttackSkill(_user, target.getId(), 10145, target.getX(), target.getY(), _skill.getActionId()));
						}
					}
					break;
					case Sand_worms1: { // 샌드웜 범위공격1
						_user.broadcastPacket(new S_UseAttackSkill(_user, _target.getId(), 10195, _targetX, _targetY, _skill.getActionId()));
					}
					break;
					case Sand_worms2: { // 샌드웜 범위공격2
						_user.broadcastPacket(new S_UseAttackSkill(_user, _target.getId(), 10194, _targetX, _targetY, _skill.getActionId()));
					}
					break;
					case Sand_worms3: { // 샌드웜 범위공격3
						_user.broadcastPacket(new S_UseAttackSkill(_user, _target.getId(), 10191, _targetX, _targetY, _skill.getActionId()));
					}
					break;
					case CHAIN_REACTION: {
						//스턴이나 귀환불가 시간 +1초
						//사람한테만
						//개별 쿨
						if (!(_target instanceof L1PcInstance)) {
							return ;
						}
						L1PcInstance target = (L1PcInstance) _target;

						boolean success = _magic.calcProbabilityMagic(_skillId);
						if (success) {
							int[] STUN_TYPE_SKILL = { SHOCK_STUN, FORCE_STUN, FORCE_STUN_FAIL, SHOCK_ATTACK,
									TEMPEST, DESPERADO,
									EMPIRE, CONQUEROR_STUN,
									TRIPLE_STUN,
									OSIRIS, BONE_BREAK,
									CRUEL,
									PANTHERA, PHANTOM_DEATH, PHANTOM_DEATH, PHANTOM_REQUIEM,
									BEHEMOTH, FOU_SLAYER_BRAVE, FOU_SLAYER_FORCE,
									SHADOW_STEP_CHASER,
									DISINTEGRATE, ETERNITI };
							for (int i : STUN_TYPE_SKILL) {
								if (target.hasSkillEffect(i)) {
									//							long remaintime = target.getSkilleffect(i).timeMillis();
									target.addSkillEffectTime(i, 1000);
								}
							}
						}
					}
					break;
					case BEHEMOTH:{
						//사람한테만
						//일정확률 귀환불가(이동가능)+이속감소
						//개별 쿨

						int diffLevel = 0;
						int duration  = 0;
						if (!(_target instanceof L1PcInstance)) {
							return ;
						}


						L1PcInstance target = (L1PcInstance) _target;
						target.setBehemoth_Attacker(null);
						diffLevel = _user.getLevel() - _target.getLevel();

						if (_player != null) {
							if (diffLevel < Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL) {
								int[] SkillTimeArray = Config.MagicAdSetting_DragonKnight.BEHEMOTH_MS;
								duration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL1 && diffLevel <= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL2) {
								int[] SkillTimeArray = Config.MagicAdSetting_DragonKnight.BEHEMOTH_MS1;
								duration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL3 && diffLevel <= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL4) {
								int[] SkillTimeArray = Config.MagicAdSetting_DragonKnight.BEHEMOTH_MS2;
								duration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL5 && diffLevel <= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL6) {
								int[] SkillTimeArray = Config.MagicAdSetting_DragonKnight.BEHEMOTH_MS3;
								duration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL7 && diffLevel <= Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL8) {
								int[] SkillTimeArray = Config.MagicAdSetting_DragonKnight.BEHEMOTH_MS4;
								duration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_DragonKnight.BEHEMOTH_LVL9) {
								int[] SkillTimeArray = Config.MagicAdSetting_DragonKnight.BEHEMOTH_MS5;
								duration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							}
						}

						if (DCCD != 0) {
							duration -=DCCD;
						}
						if (ICCD != 0) {
							duration += ICCD;
						}
						if (duration <= 0) {
							duration = 100;
						}
						if (duration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						boolean success = _magic.calcProbabilityMagic(_skillId);

						if (success) {
							on_icons(target, _skillId, duration/1000);
							target.setMoveDelayRate(-50);
							SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(target, 21964, true);
							target.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(_target, 21964, true));
							target.setSkillEffect(BEHEMOTH, duration);
						}

						int healing = _player.get_Behemoth_Heal();
						int currenthp = _player.getCurrentHp();
						int finalhealing = healing+currenthp;
//					System.out.println("힐량: "+healing);
						if (finalhealing >= _player.getMaxHp()) {
							finalhealing = _player.getMaxHp();
						}
						_player.setCurrentHp(finalhealing);
						_player.clear_Behemoth_Damage();
					}
					break;
					case FOU_SLAYER: { // 포우슬레이어
						if (_player.getWeapon() == null) {
							return;
						}
						int weapontype = _player.getWeapon().getItem().getType1();
						if (weapontype != 4 && weapontype != 11 && weapontype != 24 && weapontype != 50) {
							return;
						}
						_player.FouSlayer = true;
						for (int i = 3; i > 0; i--) {
							_target.onAction(_player);
						}
						_player.FouSlayer = false;
						_player.sendPackets(new S_SkillSound(_player.getId(), 7020));// 추가함
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 7020));// 추가함

						int effect = 6509;
						if (_player.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
							effect = 17231;
						}
						if (_player.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
							effect = 21928;
						}
						_target.send_effect(effect);
					}
					break;
					/** 혈맹버프 **/
					case CLAN_BUFF1: {// 일반 공격 태세
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgupByArmor(2);
						pc.addBowDmgupByArmor(2);
						pc.sendPackets(new S_ACTION_UI2(2724, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7233, 4650));
						pc.sendPackets(new S_ServerMessage(4618, "$22503"));
						pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
					}
					break;
					case CLAN_BUFF2: {// 일반 방어 태세
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-3);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_ACTION_UI2(2725, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7235, 4651));
						pc.sendPackets(new S_ServerMessage(4618, "$22504"));
						pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
					}
					break;
					case CLAN_BUFF3: {// 전투 공격 태세
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_ACTION_UI2(2726, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7237, 4652));
						pc.sendPackets(new S_ServerMessage(4618, "$22505"));
						pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
					}
					break;
					case CLAN_BUFF4: {// 전투 방어 태세
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_ACTION_UI2(2727, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7239, 4653));
						pc.sendPackets(new S_ServerMessage(4618, "$22506"));
						pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
					}
					break;
		/*		case L1SkillId.MOB_BERSERKERS: {
					if (_user instanceof L1NpcInstance) {
						_user.setMoveSpeed(1);
						_user.setBraveSpeed(1);
					}
				}
					break;*/
					case 10026:
					case 10027:
					case 10028:
					case 10029: {
						if (_user instanceof L1NpcInstance) {
							_user.broadcastPacket(new S_NpcChatPacket(_npc, "$3717", 0));
						} else {
							_player.broadcastPacket(new S_ChatPacket(_player, "$3717", 0, 0));
						}
						dmg = cha.getCurrentHp();
					}
					break;
					case 10057: {
						if (_npc.getLocation().getTileLineDistance(_user.getLocation()) < 18)
							L1Teleport.getInstance().teleportToTargetFront(cha, _user, 3, true);
					}
					break;
					case SLOW:
					case MOB_SLOW_1:
					case MOB_SLOW_18: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getHasteItemEquipped() > 0) {
								continue;
							}

						}

						if (cha.getMoveSpeed() == 0) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillHaste(pc.getId(), 2, _getBuffIconDuration));
							}
							cha.broadcastPacket(new S_SkillHaste(cha.getId(), 2, _getBuffIconDuration));
							cha.setMoveSpeed(2);
						} else if (cha.getMoveSpeed() == 1) {
							int skillNum = 0;
							if (cha.hasSkillEffect(HASTE)) {
								skillNum = HASTE;
							} else if (cha.hasSkillEffect(MOB_HASTE)) {
								skillNum = MOB_HASTE;
							} else if (cha.hasSkillEffect(GREATER_HASTE)) {
								skillNum = GREATER_HASTE;
							} else if (cha.hasSkillEffect(STATUS_HASTE)) {
								skillNum = STATUS_HASTE;
							}
							if (skillNum != 0) {
								cha.removeSkillEffect(skillNum);
								cha.killSkillEffectTimer(skillNum);
								cha.removeSkillEffect(_skillId);
								if (cha instanceof L1PcInstance)
									((L1PcInstance) cha).sendPackets(new S_SkillHaste(cha.getId(), 1, 0));
								cha.setMoveSpeed(0);
								continue;
							}
						}
					}
					break;
					case CURSE_BLIND: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_CurseBlind(1));
							// 케스트id를 skills 테이블에서 불러오면 15초가 됨
							pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 10703, _getBuffIconDuration));
						}
					}
					break;
					case DARKNESS: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
								pc.sendPackets(new S_CurseBlind(2));
							} else {
								pc.sendPackets(new S_CurseBlind(1));
							}
						}
					}
					break;
					case DARK_BLIND: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
							on_icons(pc, _skillId, -1);
						}
						int rnd = random.nextInt(4) + 1;
						cha.setSkillEffect(L1SkillId.DARK_BLIND, rnd * 1000);
						cha.setSleeped(true);
					}
					break;
					case CURSE_POISON:
					case TROGIR_MILPITAS1:
						L1DamagePoison.doInfection(_user, cha, 3000, 5, false);
						break;
				/*case BERSERK:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addSpecialResistance(eKind.ALL, 20);
						pc.addDmgup(20);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
					break;*/
					case TOMAHAWK:
						_target.send_effect(19537, true);
						if (_player.isPassive(MJPassiveID.TOMAHAWK_HUNTER.toInt())) {
							if (_target instanceof L1PcInstance) {
								L1PcInstance target = (L1PcInstance) _target;
								SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(target, 20597, true);
								on_icons(target, TOMAHAWK, 3);
							}
//						SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(_target, 20597, true);
							_target.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(_target, 20597, true));
							_target.setTomahawkHunter(_player);
							_target.setSkillEffect(TOMAHAWK, 3000);

						}
//					int DMG = 0;
//					if (DMG != 0) {
//						DMG = SkillsTable.getInstance().getTemplate(L1SkillId.TOMAHAWK).getDamageValue();
						// L1DamagePoison.doInfection(_user, cha, 1000, _user.getLevel() * 2 / DMG, true);
						// } else {
						// L1DamagePoison.doInfection(_user, cha, 1000, _user.getLevel() * 2 / 1, true);
						// System.out.println("SKills : 토마호크 DamageValue = 1(포함) 이상으로 설정하세요.(강제:1중)");
//					}
						break;
					case CURSE_PARALYZE:
					case CURSE_PARALYZE2:
					case MOB_CURSEPARALYZ1:
					case MOB_CURSEPARALYZ_18:
					case MOB_CURSEPARALYZ_19: {
						L1CurseParalysis.curse(cha, _skillId);
					}
					break;
					case WEAKNESS:
					case MOB_WEAKNESS_1: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDmgup(-5);
							pc.addHitup(-1);
						}
					}
					break;
					case DISEASE:
					case MOB_DISEASE_1:
					case MOB_DISEASE_30: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addHitup(-6);
							pc.getAC().addAc(12);
						}
					}
					break;
					case DESTROY: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(DESTROY)) {
								pc.removeSkillEffect(DESTROY);
							}
							pc.setSkillEffect(DESTROY, 8000);

							pc.getAC().addAc(5);
							if (_player.isPassive(MJPassiveID.DESTROY_PIER.toInt())) {
								pc.addDg(-10);
								pc.setDestroy_pier(true);
							}
							if (_player.isPassive(MJPassiveID.DESTROY_HORROR.toInt())) {
								pc.getAbility().addAddedStr((byte) -5);
								pc.getAbility().addAddedInt((byte) -5);
								pc.setDestroy_horror(true);
							}
						}
					}
					break;
					case PANIC: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedStr((byte) -1);
							pc.getAbility().addAddedDex((byte) -1);
							pc.getAbility().addAddedCon((byte) -1);
							pc.getAbility().addAddedInt((byte) -1);
							pc.getAbility().addAddedWis((byte) -1);
							pc.getAbility().addAddedCha((byte) -1);
							pc.resetBaseMr();
						}
					}
					break;
					case ICE_LANCE: {
						_isFreeze = _magic.calcProbabilityMagic(_skillId);
						if (_isFreeze) {
							int time = _skill.getBuffDuration() * 1000;
							L1EffectSpawn.getInstance().spawnEffect2(81168, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(81168, time, cha.getX(), cha.getY(), cha.getMapId(), cha);
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_Poison(pc.getId(), 2));
								pc.broadcastPacket(new S_Poison(pc.getId(), 2));
								pc.setSkillEffect(_skillId, time);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
							} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								npc.broadcastPacket(new S_Poison(npc.getId(), 2));
								npc.setParalyzed(true);
								npc.setSkillEffect(_skillId, time);
							}
						}
					}
					break;
					/** 어바지속시간 본섭화 **/
					case EARTH_BIND: {
						int[] ebTimeArray = { 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000 };
						_earthBindDuration = ebTimeArray[random.nextInt(ebTimeArray.length)];
						if (DCCD != 0) {
							_earthBindDuration -= DCCD;
						}
						if (ICCD != 0) {
							_earthBindDuration += ICCD;
						}
						if (_earthBindDuration <= 0) {
							_earthBindDuration = 100;
						}
						if (_earthBindDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;

							pc.sendPackets(new S_Poison(pc.getId(), 2));
							pc.broadcastPacket(new S_Poison(pc.getId(), 2));
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.broadcastPacket(new S_Poison(npc.getId(), 2));
							npc.setParalyzed(true);
							npc.setParalysisTime(_earthBindDuration);
						}
					}
					break;
					case MOB_BASILL:
					case MOB_COCA: {
						if (!cha.hasSkillEffect(MOB_COCA) || !cha.hasSkillEffect(MOB_BASILL))
							L1CurseParalysis.curse(cha, _skillId);
					}
					break;
					case SHOCK_STUN: {
						int targetLevel = 0;
						int diffLevel = 0;

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						diffLevel = _user.getLevel() - targetLevel;

						// TODO 예)시전자 레벨:80 상대방 레벨:86~격차)
						if (diffLevel < Config.MagicAdSetting_Knight.SHOCKSTUN_LVL) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.SHOCKSTUN_MS;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:83~85까지 (-3~격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL1 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL2) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.SHOCKSTUN_MS1;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:78~82까지 (-2~+2격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL3 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL4) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.SHOCKSTUN_MS2;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:75~77까지 (-1~-2격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL5 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL6) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.SHOCKSTUN_MS3;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:70~74까지 (-1~-4격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL7 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKSTUN_LVL8) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.SHOCKSTUN_MS4;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:-69~(이하)
						} else if (diffLevel > Config.MagicAdSetting_Knight.SHOCKSTUN_LVL9) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.SHOCKSTUN_MS5;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
						}
						if (_player.getWeapon() != null) {
							L1ItemInstance weapon = _player.getWeapon();
							if (weapon.getItemId() == 203065) {
								_shockStunDuration += 1000;
							}
						}
						if (DCCD != 0) {
							_shockStunDuration -= DCCD;
						}
						if (ICCD != 0) {
							_shockStunDuration += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}
						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(81162, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//					L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
//					SC_WORLD_PUT_OBJECT_NOTI.make_stream(_target, 15101, true);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							on_icons(pc, _skillId, _shockStunDuration/1000);
//						pc.setSkillEffect(SHOCK_STUN, _shockStunDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
							pc.setSkillEffect(_skillId, _shockStunDuration);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
							npc.setSkillEffect(_skillId, _shockStunDuration);
						}
					}
					break;
					case EMPIRE:{
						int targetLevel = 0;
						int diffLevel = 0;
						int effect_id = 50000066;
//					int effect_id = 17570;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null && _player.isPassive(MJPassiveID.EMPIRE_OVERLORD.toInt())) {
							if (diffLevel < Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_MS;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:83~85까지 (-3~격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL1 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL2) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_MS1;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:78~82까지 (-2~+2격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL3 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL4) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_MS2;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:75~77까지 (-1~-2격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL5 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL6) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_MS3;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:70~74까지 (-1~-4격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL7 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL8) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_MS4;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:-69~(이하)
							} else if (diffLevel > Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_LVL9) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_OVERLORD_MS5;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							}
							effect_id = 50000073;
//						effect_id = 20403;
						} else {
							if (diffLevel < Config.MagicAdSetting_Prince.EMPIRE_LVL) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_MS;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:83~85까지 (-3~격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_LVL1 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_LVL2) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_MS1;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:78~82까지 (-2~+2격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_LVL3 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_LVL4) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_MS2;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:75~77까지 (-1~-2격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_LVL5 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_LVL6) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_MS3;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:70~74까지 (-1~-4격차)
							} else if (diffLevel >= Config.MagicAdSetting_Prince.EMPIRE_LVL7 && diffLevel <= Config.MagicAdSetting_Prince.EMPIRE_LVL8) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_MS4;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								// TODO 예)시전자 레벨:80 상대방 레벨:-69~(이하)
							} else if (diffLevel > Config.MagicAdSetting_Prince.EMPIRE_LVL9) {
								int[] stunTimeArray = Config.MagicAdSetting_Prince.EMPIRE_MS5;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							}
						}
						//TODO 엠파이어 시간 1~6초 최대치 사이사이 시간초를 중복으로 넣기
						if (_player.getWeapon().getItemId() == 7000239) {
							_shockStunDuration += 1000;
						}

						if (DCCD != 0) {
							_shockStunDuration -= DCCD;
						}
						if (ICCD != 0) {
							_shockStunDuration += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}
						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//					L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect(effect_id, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
//					SC_WORLD_PUT_OBJECT_NOTI.make_stream(_target, effect_id, true);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
							pc.setSkillEffect(_skillId, _shockStunDuration);
							if (_player != null && _player.isPassive(MJPassiveID.EMPIRE_OVERLORD.toInt())) {
								pc.addDg(-10);
								pc.setEmpireOverlord(true);
								on_icons(pc, EMPIRE_OVERLOAD, _shockStunDuration/1000);
							} else {
								on_icons(pc, _skillId, _shockStunDuration/1000);
							}
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
							npc.setSkillEffect(_skillId, _shockStunDuration);
						}
					}
					break;
					case MOB_RANGESTUN_18:
					case MOB_RANGESTUN_19:
					case MOB_SHOCKSTUN_30: {
						int levelDiff = _user.getLevel() - cha.getLevel();
						int duration = 2250 + levelDiff * 80;
						duration += random.nextInt(1600) - 800;

						if (duration < 1000) {
							duration = 1000;
						} else if (duration > 5000) {
							duration = 5000;
						}
						if (DCCD != 0) {
							_shockStunDuration -= DCCD;
						}
						if (ICCD != 0) {
							_shockStunDuration += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}
						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						_shockStunDuration = duration;
						L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(8500315, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//					L1EffectSpawn.getInstance().spawnEffect2(8500315, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
						}
					}
					break;
					case THUNDER_GRAB: {
						_isFreeze = _magic.calcProbabilityMagic(_skillId);
						if (_isFreeze) {
							int[] grabTime = null;
							if (_player != null && _player.isPassive(MJPassiveID.THUNDER_GRAP_BRAVE.toInt())) {
								grabTime = new int[] { 3000, 3000, 3000, 4000, 4000};
							} else {
								grabTime = new int[] { 2000, 2000, 2000, 3000, 3000 };
							}
							int rnd = random.nextInt(grabTime.length);
							int time = grabTime[rnd]; // 시간 랜덤을 위해
							if (DCCD != 0) {
								time -= DCCD;
							}
							if (ICCD != 0) {
								time += ICCD;
							}
							if (time <= 0) {
								time = 100;
							}
							if (time <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(81182, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(81182, time, cha.getX(), cha.getY(), cha.getMapId(), cha);

							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
//							pc.setSkillEffect(L1SkillId.STATUS_FREEZE, time);
								pc.setSkillEffect(_skillId, time);
								pc.sendPackets(new S_SkillSound(pc.getId(), 4184));
								pc.broadcastPacket(new S_SkillSound(pc.getId(), 4184));
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
							} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								npc.broadcastPacket(new S_SkillSound(npc.getId(), 4184));
								npc.set발묶임상태(true);
								npc.setSkillEffect(_skillId, time);
							}
						}
					}
					break;
					case BONE_BREAK: {
						int eff = 200020;
						int eff_rast = 200019;
/*					int eff = 13119;
					int eff_rast = 19599;*/
						int targetLevel = 0;
						int diffLevel = 0;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}
						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null) {
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

						if (DCCD != 0) {
							_shockStunDuration -= DCCD;
						}
						if (ICCD != 0) {
							_shockStunDuration += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}
						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						if (_player.isPassive(MJPassiveID.RAST.toInt())) {
							int bonetime_RAST = Config.MagicAdSetting_Illusion.BONE_BREAK_RAST_MS;
							_shockStunDuration += bonetime_RAST;
							L1EffectSpawn.getInstance().spawnEffect2(eff_rast, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(eff_rast, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, eff_rast, true);
						} else {
							L1EffectSpawn.getInstance().spawnEffect2(eff, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(eff, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, eff, true);
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setSkillEffect(_skillId, _shockStunDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
							if (_player.isPassive(MJPassiveID.RAST.toInt())) {
								on_icons(pc, L1SkillId.BONE_BREAK_LAST, _shockStunDuration / 1000);
							}else {
								on_icons(pc, _skillId, _shockStunDuration / 1000);
							}

						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
							npc.setSkillEffect(_skillId, _shockStunDuration);
						}
					}
					break;
					case OSIRIS: {
						int targetLevel = 0;
						int diffLevel = 0;
						int effectNpcId = 8502108; //캐스팅 이미지
//					int effectNpcId2 = 8502107; //스턴 이미지
						int effectNpcId2 = 21550; //스턴 이미지

						L1EffectSpawn.getInstance().spawnEffect(effectNpcId, 2000, _targetX, _targetY, _target.getMapId());
//					L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, 2000, _target.getX(), _target.getY(), _target.getMapId(), cha);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							return;
						}
//					System.out.println("1");
						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null) {
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

						if (_magic.calcProbabilityMagic(_skillId)) {
							if (DCCD != 0) {
								_shockStunDuration -= DCCD;
							}
							if (ICCD != 0) {
								_shockStunDuration += ICCD;
							}
							if (_shockStunDuration <= 0) {
								_shockStunDuration = 100;
							}
							if (_shockStunDuration <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId2, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(effectNpcId2, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);
//						SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, effectNpcId2, true);

							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(L1SkillId.OSIRIS)){
									pc.removeSkillEffect(L1SkillId.OSIRIS);
								}
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_OSIRIS, true));
								_target.setSkillEffect(L1SkillId.OSIRIS, _shockStunDuration);
								on_icons(pc, L1SkillId.OSIRIS, _shockStunDuration / 1000);
//							_target.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
								//pc.setSkillEffect(_skillId, _shockStunDuration);
							}
						}
						_target.setSkillEffect(L1SkillId.OSIRIS_TICK, 3000);
						L1Osiris.doInfection(_user, _target, 300, 0);
					}


					break;
					case ENSNARE:{
						int targetLevel = 0;
						int diffLevel = 0;
						int skill_time = 0;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
								|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}
						_target.send_effect(21079);
						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null) {
							if (diffLevel < Config.MagicAdSetting_Illusion.ENSNARE_LVL) {
								int[] SkillTimeArray = Config.MagicAdSetting_Illusion.ENSNARE_MS;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Illusion.ENSNARE_LVL1 && diffLevel <= Config.MagicAdSetting_Illusion.ENSNARE_LVL2) {
								int[] SkillTimeArray = Config.MagicAdSetting_Illusion.ENSNARE_MS1;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Illusion.ENSNARE_LVL3 && diffLevel <= Config.MagicAdSetting_Illusion.ENSNARE_LVL4) {
								int[] SkillTimeArray = Config.MagicAdSetting_Illusion.ENSNARE_MS2;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Illusion.ENSNARE_LVL5 && diffLevel <= Config.MagicAdSetting_Illusion.ENSNARE_LVL6) {
								int[] SkillTimeArray = Config.MagicAdSetting_Illusion.ENSNARE_MS3;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Illusion.ENSNARE_LVL7 && diffLevel <= Config.MagicAdSetting_Illusion.ENSNARE_LVL8) {
								int[] SkillTimeArray = Config.MagicAdSetting_Illusion.ENSNARE_MS4;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_Illusion.ENSNARE_LVL9) {
								int[] SkillTimeArray = Config.MagicAdSetting_Illusion.ENSNARE_MS5;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							}
						}
						if (_player.getWeapon() != null) {
							L1ItemInstance weapon = _player.getWeapon();
							if (weapon.getItemId() == 7000265) {
								skill_time += 1000;
							}
						}
						if (DCCD != 0) {
							skill_time -= DCCD;
						}
						if (ICCD != 0) {
							skill_time += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}
						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						if (!_magic.calcProbabilityMagic(_skillId)) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.addMoveDelayRate(-50);
								SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
								pc.setSkillEffect(L1SkillId.ENSNARE, skill_time);
								on_icons(pc, _skillId, (skill_time) / 1000);
							}
						}
					}
					break;

					case WIND_SHACKLE:
					case MOB_WINDSHACKLE_1: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
							pc.setSkillEffect(pc.getId(), _getBuffIconDuration);
						}
					}
					break;
					case CANCELLATION: {
						try { // for test
							if (cha instanceof L1PcInstance) {
								((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), _skill.getCastGfx()));
							}

							cha.broadcastPacket(new S_SkillSound(cha.getId(), _skill.getCastGfx()));

							if (cha instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								int npcId = npc.getNpcTemplate().get_npcId();
								if (npcId == 71092) {
									if (!npc.equalsCurrentSprite(1314)) {
										npc.setCurrentSprite(1314);
										npc.sendShape(1314);
										return;
									} else {
										return;
									}
								}
								if (npcId == 45640) {
									if (npc.equalsCurrentSprite(2755)) {
										npc.setCurrentHp(npc.getMaxHp());
										npc.setCurrentSprite(2332);
										npc.sendShape(2332);
										npc.setName("$2103");
										npc.setNameId("$2103");
										npc.broadcastPacket(new S_ChangeName(npc.getId(), "$2103"));
									} else {
										npc.setCurrentHp(npc.getMaxHp());
										npc.setCurrentSprite(2755);
										npc.sendShape(2755);
										npc.setName("$2488");
										npc.setNameId("$2488");
										npc.broadcastPacket(new S_ChangeName(npc.getId(), "$2488"));
									}
								}
								if (npcId == 81209) {
									if (!npc.equalsCurrentSprite(4310)) {
										npc.setCurrentSprite(4310);
										npc.sendShape(4310);
										return;
									} else {
										return;
									}
								}
							}
							if (!(cha instanceof L1PcInstance)) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								npc.setMoveSpeed(0);
								npc.setBraveSpeed(0);
								npc.broadcastPacket(new S_SkillHaste(cha.getId(), 0, 0));
								npc.broadcastPacket(new S_SkillBrave(cha.getId(), 0, 0));
								npc.setWeaponBreaked(false);
								npc.setParalyzed(false);
							}

							if (cha instanceof L1PcInstance) {
								detection((L1PcInstance) cha, false);
							}
							// 캔슬레이션 유저스킬 밑에 사이에서만 캔슬됨 1~240번
							for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
								if (isNotCancelable(skillNum) && !cha.isDead()) {
									continue;
								}

								if (skillNum == SHAPE_CHANGE || skillNum == POLY_RING_MASTER || skillNum == POLY_RING_MASTER2) {
									if (_player == null) {
										continue;
									}
									if (cha instanceof L1PcInstance) {
										L1PcInstance pc = (L1PcInstance) cha;
										if (pc.getId() != _player.getId() && (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER) || pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2))) {
											pc.send_effect(15846, true);
											_player.sendPackets(new S_ServerMessage(280));
											return;
										}
										if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId())) {
											continue;
										}
										if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)){
											continue;
										}
										if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)){
											continue;
										}
									}
								}
								cha.removeSkillEffect(skillNum);
							}

							for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_END; skillNum++) {
								if (skillNum == STATUS_CHAT_PROHIBITED || skillNum == STATUS_CURSE_BARLOG || skillNum == STATUS_CURSE_YAHEE) {
									continue;
								}
								cha.removeSkillEffect(skillNum);
							}
							cha.removeSkillEffect(STATUS_FRUIT);

							cha.curePoison();
							cha.cureParalaysis();

							for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
								if (isNotCancelable(skillNum) && !cha.isDead()) {
									continue;
								}
								cha.removeSkillEffect(skillNum);
							}

							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.getHasteItemEquipped() > 0) {
									continue;
								}
							}
							cha.removeSkillEffect(STATUS_FREEZE);

							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;

								getTreePotionBuff(pc);

								pc.sendPackets(new S_CharVisualUpdate(pc));
								pc.broadcastPacket(new S_CharVisualUpdate(pc));
								if (pc.isPrivateShop()) {
									pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()));
									pc.broadcastPacket(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()));
								}
								if (_user instanceof L1PcInstance) {
									L1PinkName.onAction(pc, _user);
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
					case TURN_UNDEAD: {
						if (undeadType == 1 || undeadType == 3) {
							dmg = cha.getCurrentHp();
							if (_target instanceof L1MonsterInstance) {
								L1MonsterInstance m = (L1MonsterInstance) _target;
								if (_user instanceof L1PcInstance)
									m.setHate(_user, 1);
							}
						}
					}
					break;
					case MANA_DRAIN: {
						int chance = random.nextInt(5) + 10;
						drainMana = chance + (_user.getAbility().getTotalInt() / 2);
						if (cha.getCurrentMp() < drainMana) {
							drainMana = cha.getCurrentMp();
							_user.send_effect(2171);
						}
					}
					break;
					case WEAPON_BREAK: {
						if (_calcType == PC_PC || _calcType == NPC_PC) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								L1ItemInstance weapon = pc.getWeapon();
								if (weapon != null) {
									int weaponDamage = random.nextInt(_user.getAbility().getTotalInt() / 3) + 1;
									pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
									pc.getInventory().receiveDamage(weapon, weaponDamage);
								}
							}
						} else {
							((L1NpcInstance) cha).setWeaponBreaked(true);
						}
					}
					break;
					case PHANTASM: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
							int rnd = random.nextInt(4) + 1;
							pc.setSkillEffect(_skillId, rnd * 1000);
							L1SkillUse.on_icons(pc, _skillId, rnd);
							pc.setSleeped(true);
						}
					}
					break;
					case FOG_OF_SLEEPING: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
							pc.setSleeped(true);
							int rnd = random.nextInt(4) + 1;
							pc.setSkillEffect(_skillId, rnd * 1000);
							L1SkillUse.on_icons(pc, _skillId, rnd);
						}
					}
					break;
					case STATUS_FREEZE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
						}
					}
					break;
					case BALOCH_STUN:
					case BOS_STUN18:
					case DRAGON_HALPAS_STUN:
					case OMAN_STUN: {
						int[] stunTimeArray = { 2000, 3000, 4000 };
						int rnd = random.nextInt(stunTimeArray.length);
						_shockStunDuration = stunTimeArray[rnd];
						L1EffectSpawn.getInstance().spawnEffect2(8500315, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//					L1EffectSpawn.getInstance().spawnEffect2(8500315, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
							pc.setSkillEffect(_skillId, _shockStunDuration);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
							npc.setSkillEffect(_skillId, _shockStunDuration);
						}
					}
					break;
					case MOSTER_STUN_1:
					case fornos_STUN:
					case Besi_STUN:
					case Moster_STUN:
					case Maeno_STUN: {
						int[] stunTimeArray = { 2000, 3000, 4000 };
						int rnd = random.nextInt(stunTimeArray.length);
						_shockStunDuration = stunTimeArray[rnd];
						L1EffectSpawn.getInstance().spawnEffect2(8500315, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//					L1EffectSpawn.getInstance().spawnEffect2(8500315, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
							pc.setSkillEffect(_skillId, _shockStunDuration);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
							npc.setSkillEffect(_skillId, _shockStunDuration);
						}
					}
					break;
					case OMAN_CANCELLATION: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (cha instanceof L1PcInstance) {
								((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
							}
							cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

							for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
								if (isNotCancelable(skillNum) && !pc.isDead()) {
									continue;
								}
								if (skillNum == SHAPE_CHANGE) {
									if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
										continue;
									if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0){
										continue;
									}
									if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0){
										continue;
									}
								}
								pc.removeSkillEffect(skillNum);
							}
							for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
								if (skillNum == STATUS_CHAT_PROHIBITED) {
									continue;
								}
								pc.removeSkillEffect(skillNum);
							}
							for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
								if (isNotCancelable(skillNum) && !pc.isDead()) {
									continue;
								}
								pc.removeSkillEffect(skillNum);
							}
							pc.curePoison();
							pc.cureParalaysis();
							if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
								L1PolyMorph.undoPoly(pc);
								pc.sendPackets(new S_CharVisualUpdate(pc));
								pc.broadcastPacket(new S_CharVisualUpdate(pc));
							}
							if (pc.getHasteItemEquipped() > 0) {
								pc.setMoveSpeed(0);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
								pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
							}
							if (pc != null && pc.isInvisble()) {
								if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
									pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
									pc.sendPackets(new S_Invis(pc.getId(), 0));
									pc.broadcastPacket(new S_Invis(pc.getId(), 0));
									pc.sendPackets(new S_Sound(147));
								}
								if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
									pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
									pc.sendPackets(new S_Invis(pc.getId(), 0));
									pc.broadcastPacket(new S_Invis(pc.getId(), 0));
								}
							}
							pc.removeSkillEffect(STATUS_FREEZE);
							getTreePotionBuff(pc);
						}
					}
					break;
					case ANTA_MESSAGE_1: // 안타[용언1 / 캔슬 -> 오브 모크! 케 네시]
					case ANTA_MESSAGE_2: // 안타[용언2 / 블레스+독/ 오브 모크! 켄 로우]
					case ANTA_MESSAGE_3: // 안타[용언3 / 왼손+오른펀치+고함 / 오브 모크! 티기르]
					case ANTA_MESSAGE_4: // 안타[용언4 / 펀치+블레스 / 오브 모크! 켄 티기르]
					case ANTA_MESSAGE_5: // 안타[용언5 / 고함+블레스 / 오브 모크! 루오타]
					case ANTA_MESSAGE_6: // 안타[용언6 / 스턴+점프/ 오브 모크! 뮤즈삼]
					case ANTA_MESSAGE_7: // 안타[용언7 / 스턴+발작/ 오브 모크! 너츠삼]
					case ANTA_MESSAGE_8: // 안타[용언8 / 스턴+발+점/ 오브 모크! 티프삼]
					case ANTA_MESSAGE_9: // 안타[용언9 / 웨폰+블레스 / 오브 모크! 리라프]
					case ANTA_MESSAGE_10: // 안타[용언10 / 웨폰+마비 / 오브 모크! 세이 라라프]
					case ANTA_CANCELLATION:
					case ANTA_WEAPON_BREAK:
					case ANTA_SHOCKSTUN: {
						int npcId = _npc.getNpcTemplate().get_npcId();
						if (npcId == 900011 || npcId == 900012 || npcId == 900013) {
							if (_skillId == ANTA_MESSAGE_1 || _skillId == ANTA_CANCELLATION) { // 캔슬
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (cha instanceof L1PcInstance) {
										((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
									}
									cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

									for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										if (skillNum == SHAPE_CHANGE) {
											if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
												continue;
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0){
												continue;
											}
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0){
												continue;
											}
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
										if (skillNum == STATUS_CHAT_PROHIBITED) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									pc.curePoison();
									pc.cureParalaysis();
									if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
										L1PolyMorph.undoPoly(pc);
										pc.sendPackets(new S_CharVisualUpdate(pc));
										pc.broadcastPacket(new S_CharVisualUpdate(pc));
									}
									if (pc.getHasteItemEquipped() > 0) {
										pc.setMoveSpeed(0);
										pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
										pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
									}
									if (pc != null && pc.isInvisble()) {
										if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
											pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											pc.broadcastPacket(new S_Invis(pc.getId(), 0));
											pc.sendPackets(new S_Sound(147));
										}
										if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
											pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											pc.broadcastPacket(new S_Invis(pc.getId(), 0));
										}
									}
									pc.removeSkillEffect(STATUS_FREEZE);
									getTreePotionBuff(pc);
								}
							}

							if (_skillId == ANTA_MESSAGE_1 || _skillId == ANTA_MESSAGE_10) {// 마비독
								Random random = new Random();
								int time = random.nextInt(5) + 1;
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (time > 10)
										L1ParalysisPoison.doInfection(pc, _skillId);
								}
							}

							if (_skillId == ANTA_MESSAGE_2 || _skillId == ANTA_MESSAGE_5 || _skillId == ANTA_MESSAGE_9 || _skillId == DRAGON_HALPAS_POISON) { // 대미지독
								Random random = new Random();
								int PoisonDmg = random.nextInt(50) + 1;
								int PoisonTime = random.nextInt(15) + 1;
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (PoisonTime > 2)
										L1DamagePoison.doInfection(pc, _target, PoisonTime * 1000, PoisonDmg, _skillId == TOMAHAWK);
								}
							}
							if (_skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8 || _skillId == ANTA_SHOCKSTUN) {// 스턴
								int[] stunTimeArray = { 1000, 2000, 3000 };
								int rnd = random.nextInt(stunTimeArray.length);
								_shockStunDuration = stunTimeArray[rnd];
								L1EffectSpawn.getInstance().spawnEffect2(8500315, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							L1EffectSpawn.getInstance().spawnEffect2(8500315, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
									pc.setSkillEffect(_skillId, _shockStunDuration);
								} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
									L1NpcInstance npc = (L1NpcInstance) cha;
									npc.setParalyzed(true);
									npc.setParalysisTime(_shockStunDuration);
									npc.setSkillEffect(_skillId, _shockStunDuration);
								}
							}
							if (_skillId == ANTA_MESSAGE_9 || _skillId == ANTA_MESSAGE_10 || _skillId == ANTA_WEAPON_BREAK) { // 웨폰
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									L1ItemInstance weapon = pc.getWeapon();
									if (weapon != null) {
										int weaponDamage = random.nextInt(3) + 1;
										pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
										pc.getInventory().receiveDamage(weapon, weaponDamage);
										pc.sendPackets(new S_SkillSound(pc.getId(), 172));
										pc.broadcastPacket(new S_SkillSound(pc.getId(), 172));
									}
								}
							}
						}
					}
					break;
					case PAP_PREDICATE1: // 파푸[용언1:리오타! 피로이 나! [오색 진주3 / 신비한 오색 진주1
						// / 토르나 소환5]
					case PAP_PREDICATE3: // 파푸[용언3:리오타! 라나 오이므! [데스포션 -> 오른손 ->
						// 아이스이럽션]
					case PAP_PREDICATE5: // 파푸[용언5:리오타! 네나 우누스! [리듀스 힐 + 머리 공격 + 아이스
						// 브레스]
					case PAP_PREDICATE6: // 파푸[용언6:리오타! 테나 웨인라크! [데스 힐 + 꼬리 공격 + 아이스
						// 브레스]
					case PAP_PREDICATE7: // 파푸[용언7:리오타! 라나 폰폰! [캔슬레이션 + 오른속 2번 ] [범위
						// X]
					case PAP_PREDICATE8: // 파푸[용언8:리오타! 레포 폰폰! [웨폰브레이크 + 왼손 2번 ] [범위
						// X]
					case PAP_PREDICATE9: // 파푸[용언9:리오타! 테나 론디르 ! [꼬리 2연타 + 아이스
						// 브레스][범위 X]
					case PAP_PREDICATE11: // 파푸[용언11:리오타! 오니즈 웨인라크! [매스 캔슬레이션 + 데스 힐
						// + 아이스 미티어 + 아이스 이럽션] [범위 O]
					case PAP_PREDICATE12: { // 파푸[용언12:리오타! 오니즈 쿠스온 웨인라크! [매스 캔슬레이션
						// + 데스힐 + 아이스 미티어 + 발작] [범위 0]
						int npcId = _npc.getNpcTemplate().get_npcId();
						if (npcId == 900038 || npcId == 900039 || npcId == 900040) {
							if (_skillId == PAP_PREDICATE1) { // 리콜 소환(사엘-진주-토르나)
								int i;
								for (i = 0; i < 2; i++) { // 타이머 테이크 부분의 for 문으로
									// 돌리게되면 쓰레드 오류 동작이
									// 발생한다.
									L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900049, 8, 60 * 1000, 0);
									L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900050, 8, 60 * 1000, 0);
									L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900051, 8, 60 * 1000, 0);
									L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900052, 8, 120 * 1000, 0);
								}
							}

							if (_skillId == PAP_PREDICATE7 || _skillId == PAP_PREDICATE11 || _skillId == PAP_PREDICATE12) { // 캔슬
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (cha instanceof L1PcInstance) {
										((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
									}
									cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

									for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										if (skillNum == SHAPE_CHANGE) {
											if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
												continue;
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0){
												continue;
											}
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0){
												continue;
											}
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
										if (skillNum == STATUS_CHAT_PROHIBITED) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									pc.curePoison();
									pc.cureParalaysis();
									if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
										L1PolyMorph.undoPoly(pc);
										pc.sendPackets(new S_CharVisualUpdate(pc));
										pc.broadcastPacket(new S_CharVisualUpdate(pc));
									}
									if (pc.getHasteItemEquipped() > 0) {
										pc.setMoveSpeed(0);
										pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
										pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
									}
									if (pc != null && pc.isInvisble()) {
										if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
											pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											pc.broadcastPacket(new S_Invis(pc.getId(), 0));
											pc.sendPackets(new S_Sound(147));
										}
										if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
											pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											pc.broadcastPacket(new S_Invis(pc.getId(), 0));
										}
									}
									pc.removeSkillEffect(STATUS_FREEZE);
									getTreePotionBuff(pc);
								}
							}

							if (_skillId == PAP_PREDICATE8) { // 웨폰
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									L1ItemInstance weapon = pc.getWeapon();
									Random random = new Random();
									int rnd = random.nextInt(100) + 1;
									if (weapon != null && rnd > 33) {
										int weaponDamage = random.nextInt(2) + 1;
										pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
										pc.getInventory().receiveDamage(weapon, weaponDamage);
										pc.sendPackets(new S_SkillSound(pc.getId(), 172));
										pc.broadcastPacket(new S_SkillSound(pc.getId(), 172));
									}
								}
							}
							if (_skillId == PAP_PREDICATE3) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									pc.sendPackets(new S_SkillSound(pc.getId(), 7781));
									pc.broadcastPacket(new S_SkillSound(pc.getId(), 7781));
									pc.setSkillEffect(L1SkillId.PAP_DEATH_PORTION, 12 * 1000);
								}
							}
							if (_skillId == PAP_PREDICATE5) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									pc.sendPackets(new S_SkillSound(pc.getId(), 7782));
									pc.broadcastPacket(new S_SkillSound(pc.getId(), 7782));
									pc.setSkillEffect(L1SkillId.PAP_REDUCE_HELL, 12 * 1000);
								}
							}
							if (_skillId == PAP_PREDICATE6) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									pc.sendPackets(new S_SkillSound(pc.getId(), 7780));
									pc.setSkillEffect(L1SkillId.PAP_DEATH_HELL, 12 * 1000);
									pc.broadcastPacket(new S_SkillSound(pc.getId(), 7780));
								}
							}
							if (_skillId == PAP_PREDICATE11 || _skillId == PAP_PREDICATE12) {// 데스
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									pc.sendPackets(new S_SkillSound(pc.getId(), 7780));
									pc.setSkillEffect(L1SkillId.PAP_DEATH_HELL, 12 * 1000);
									pc.broadcastPacket(new S_SkillSound(pc.getId(), 7780));
								}
							}
						}
					}
					break;
					case DRAGON_HALPAS_WISH:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							on_icons(pc, _skillId, _skill.getBuffDuration());
							// _user.broadcastPacket(new S_UseAttackSkill(_user, _target.getId(), 19251, _targetX, _targetY, _skill.getActionId()));
						}
						break;
					case DRAGON_HALPAS_WATER:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							on_icons(pc, _skillId, _skill.getBuffDuration());
							// _user.broadcastPacket(new S_UseAttackSkill(_user, _target.getId(), 19247, _targetX, _targetY, _skill.getActionId()));
						}
						break;
					case DRAGON_HALPAS_FIRE:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							on_icons(pc, _skillId, _skill.getBuffDuration());
							// _user.broadcastPacket(new S_UseAttackSkill(_user, _target.getId(), 19245, _targetX, _targetY, _skill.getActionId()));
						}
						break;
					case JUDGEMENT: {
						int skill_time = 8000;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.JUDGEMENT))
								pc.removeSkillEffect(L1SkillId.JUDGEMENT);

							int j_point = (_player.getAbility().getStr() / 10) + 1;
							pc.setJudgementPoint(j_point);
							pc.addSpecialResistance(eKind.ALL, -pc.getJudgementPoint());

							if (DCCD != 0) {
								skill_time -= DCCD;
							}
							if (ICCD != 0) {
								skill_time += ICCD;
							}
							if (_shockStunDuration <= 0) {
								_shockStunDuration = 100;
							}
							if (_shockStunDuration <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							pc.setSkillEffect(L1SkillId.JUDGEMENT, skill_time);
							pc.sendPackets(new S_SkillSound(pc.getId(), 18490));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 18490));
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
							on_icons(pc, JUDGEMENT, 8);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							if (npc.hasSkillEffect(L1SkillId.JUDGEMENT))
								npc.removeSkillEffect(L1SkillId.JUDGEMENT);

							npc.broadcastPacket(new S_SkillSound(npc.getId(), 18490));
							npc.setSkillEffect(L1SkillId.JUDGEMENT, skill_time);
						}
					}
					break;
					case DOLL_JUDGEMENT: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.DOLL_JUDGEMENT))
								pc.removeSkillEffect(L1SkillId.DOLL_JUDGEMENT);

							pc.setSkillEffect(L1SkillId.DOLL_JUDGEMENT, 8 * 1000);
							pc.sendPackets(new S_SkillSound(pc.getId(), 18490));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 18490));
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
							on_icons(pc, JUDGEMENT, 8);
						}
					}
					break;
					case PHANTOM: {
						_isPhantom = _magic.calcProbabilityMagic(_skillId);
						if (_isPhantom) {
							int[] PhantomTime = new int[] { 1000, 2000, 3000, 4000 };
							int effectid = 8502098;
//						int effectid = 18524;
							int skill_effect = 18578;
							ArrayList<Integer> sub_skill_list = new ArrayList<Integer>();
							int sub_skill_id = PHANTOM;
							int sub_skill_id1 = MJPassiveID.PHANTOM_REAPER.toInt();


							if (_player != null) {
								if (_player.isPassive(MJPassiveID.PHANTOM_REAPER.toInt())) {
									sub_skill_list.add(L1SkillId.PHANTOM_RIPER);
								}
								if (_player.isPassive(MJPassiveID.PHANTOM_REQUEM.toInt())) {
									sub_skill_list.add(L1SkillId.PHANTOM_REQUIEM);
								}
								if (_player.isPassive(MJPassiveID.PHANTOM_DEATH.toInt())) {
									sub_skill_list.add(L1SkillId.PHANTOM_DEATH);
								}
							}

							if (sub_skill_list.size() != 0) {
								int size = sub_skill_list.size();
								// size-1 처리는 get에서 0으로 받아야하기 때문에 사이즈가 0보다 클경우 -1처리..
								sub_skill_id = sub_skill_list.get(MJRnd.next(0, size > 0 ? size - 1 : size));
								if (sub_skill_id == L1SkillId.PHANTOM_RIPER) {
									PhantomTime = new int[] { 3000, 4000, 4500, 5000, 5500, 6000 };
//								effectid = 18526;
									effectid = 8502099;
								} else if(sub_skill_id  == L1SkillId.PHANTOM_DEATH){
									PhantomTime = new int[] { 3000, 4000, 4500, 5000, 5500, 6000 };
									effectid = 8502106;
//								effectid = 18528;
								} else if (sub_skill_id == L1SkillId.PHANTOM_REQUIEM) {
									PhantomTime = new int[] { 3000, 4000, 4500, 5000, 5500, 6000 };
									effectid = 8502100;
//								effectid = 20315;
									skill_effect = 18580;
								}
							}

							int rnd = random.nextInt(PhantomTime.length);
							int time = PhantomTime[rnd];
							if (DCCD != 0) {
								time -= DCCD;
							}
							if (ICCD != 0) {
								time += ICCD;
							}
							if (_shockStunDuration <= 0) {
								_shockStunDuration = 100;
							}
							if (_shockStunDuration <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectid, sub_skill_id, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(effectid, time, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, effectid, true);

							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;

								if (pc.hasSkillEffect(L1SkillId.PHANTOM))
									pc.removeSkillEffect(L1SkillId.PHANTOM);

								for (int skillid : sub_skill_list) {
									if (pc.hasSkillEffect(skillid)) {
										pc.removeSkillEffect(skillid);
									}
								}


								pc.sendPackets(new S_SkillSound(pc.getId(), skill_effect));
								pc.broadcastPacket(new S_SkillSound(pc.getId(), skill_effect));
								pc.setSkillEffect(L1SkillId.PHANTOM, time);

								if (sub_skill_id == L1SkillId.PHANTOM_RIPER || sub_skill_id == L1SkillId.PHANTOM_REQUIEM || sub_skill_id == L1SkillId.PHANTOM_DEATH) {
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
									if (sub_skill_id == L1SkillId.PHANTOM_REQUIEM) {
										pc.setSkillEffect(PHANTOM_REQUIEM, time);
									} else if (sub_skill_id == L1SkillId.PHANTOM_DEATH) {
										pc.setSkillEffect(DEATH_HEAL, time);
										pc.setSkillEffect(DEATH_POTION, time);
										pc.setSkillEffect(PHANTOM_DEATH, time);
									} else {
										pc.setSkillEffect(PHANTOM_RIPER, time);
									}
									on_icons(pc, sub_skill_id, time / 1000);
								}
							} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								npc.broadcastPacket(new S_SkillSound(npc.getId(), skill_effect));
								if (_player != null && _player.isPassive(MJPassiveID.PHANTOM_REAPER.toInt()) || _player.isPassive(MJPassiveID.PHANTOM_DEATH.toInt())
										|| _player.isPassive(MJPassiveID.PHANTOM_REQUEM.toInt())) {
									npc.set발묶임상태(true);
									npc.setSkillEffect(L1SkillId.PHANTOM, time);
								}
							}
						}
					}
					break;
					case PANTHERA: {
						int targetLevel = 0;
						int diffLevel = 0;
						int effect_id = 8502101;
//					int effect_id = 18604;
						int skill_effect = 18503;
						int attack_effect = 18505;

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null && _player.isPassive(MJPassiveID.PANTERA_SHOCK.toInt())) {
							if (diffLevel < Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_MS;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL1 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL2) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_MS1;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL3 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL4) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_MS2;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL5 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL6) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_MS3;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL7 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL8) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_MS4;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_LVL9) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_SHOCK_MS5;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							}
							effect_id = 8502102;
//						effect_id = 18602;
							skill_effect = 18606;
							attack_effect = 18608;
						} else {
							if (diffLevel < Config.MagicAdSetting_Fencer.PANTHERA_LVL) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_MS;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_LVL1 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_LVL2) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_MS1;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_LVL3 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_LVL4) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_MS2;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_LVL5 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_LVL6) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_MS3;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Fencer.PANTHERA_LVL7 && diffLevel <= Config.MagicAdSetting_Fencer.PANTHERA_LVL8) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_MS4;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_Fencer.PANTHERA_LVL9) {
								int[] stunTimeArray = Config.MagicAdSetting_Fencer.PANTHERA_MS5;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							_player.setHeading(_player.targetDirection(pc.getX(), pc.getY()));

							if (_player != null && _player.isPassive(MJPassiveID.PANTERA_SHOCK.toInt())) {
								_player.Panthera_attack(pc, skill_effect, attack_effect, PANTHERA);
							} else {
								_player.Panthera_attack(pc, skill_effect, attack_effect, PANTHERA);
							}
							if (_player.getWeapon().getItemId() == 7000239) {
								_shockStunDuration += 1000;
							}
							if (DCCD != 0) {
								_shockStunDuration -= DCCD;
							}
							if (ICCD != 0) {
								_shockStunDuration += ICCD;
							}
							if (_shockStunDuration <= 0) {
								_shockStunDuration = 100;
							}
							if (_shockStunDuration <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							_isPanthera = _magic.calcProbabilityMagic(_skillId);
							if (_isPanthera) {
								L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, effect_id, true);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
								pc.setSkillEffect(PANTHERA, _shockStunDuration);
								on_icons(pc, _skillId, _shockStunDuration/1000);
							}
						} else if (cha instanceof L1NpcInstance || cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance
								|| cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							_player.setHeading(_player.targetDirection(npc.getX(), npc.getY()));

							if (_player != null && _player.isPassive(MJPassiveID.PANTERA_SHOCK.toInt())) {
								_player.Panthera_attack(npc, skill_effect, attack_effect, PANTHERA);
							} else {
								_player.Panthera_attack(npc, skill_effect, attack_effect, PANTHERA);
							}

							_isPanthera = _magic.calcProbabilityMagic(_skillId);
							if (_isPanthera) {
//							SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, effect_id, true);
								L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
								npc.setParalyzed(true);
								npc.setParalysisTime(_shockStunDuration);
								npc.setSkillEffect(_skillId, _shockStunDuration);
							}
						}
					}
					break;
					case SHADOW_STEP: {
						int targetLevel = 0;
						int diffLevel = 0;
						int skill_effect = 18947;
						int attack_effect = 18949;
						int PowerRipTimeArray[] = null;


						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null) {
							if (diffLevel < -5) {
								PowerRipTimeArray = new int[] { 1000, 2000 };
							} else if (diffLevel >= -5 && diffLevel <= -3) {
								PowerRipTimeArray = new int[] { 1000, 2000 };
							} else if (diffLevel >= -2 && diffLevel <= 2) {
								PowerRipTimeArray = new int[] { 2000, 3000 };
							} else if (diffLevel >= 3 && diffLevel <= 5) {
								PowerRipTimeArray = new int[] { 2000, 3000, 4000 };
							} else if (diffLevel >= 5 && diffLevel <= 10) {
								PowerRipTimeArray = new int[] { 2000, 3000, 4000 };
							} else if (diffLevel > 10) {
								PowerRipTimeArray = new int[] { 3000, 4000 };
							}
						}

						int rnd = random.nextInt(PowerRipTimeArray.length);

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							_player.setHeading(_player.targetDirection(pc.getX(), pc.getY()));
							_player.Panthera_attack(pc, skill_effect, attack_effect, SHADOW_STEP);
							_isShadow_step = _magic.calcProbabilityMagic(_skillId);

							_PowerRipDuration = PowerRipTimeArray[rnd];
							if (_player.getWeapon() != null) {
								L1ItemInstance weapon = _player.getWeapon();
								if (weapon.getItemId() == 7000264) {
									_PowerRipDuration += 1000;
								}
							}
							if (DCCD != 0) {
								_PowerRipDuration -= DCCD;
							}
							if (ICCD != 0) {
								_PowerRipDuration += ICCD;
							}
							if (_PowerRipDuration <= 0) {
								_PowerRipDuration = 100;
							}
							if (_PowerRipDuration <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							if (_isShadow_step) {

								if (_player.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
									_PowerRipDuration += 1000;
									L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(21072, L1SkillId.SHADOW_STEP_CHASER, cha.getX(), cha.getY(), cha.getMapId(), cha);
//								L1EffectSpawn.getInstance().spawnEffect2(21072, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);
//								SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 20778, true);
									pc.setSkillEffect(L1SkillId.SHADOW_STEP_CHASER, _PowerRipDuration);
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
									pc.setShadowstepchaser(true);
									on_icons(pc, SHADOW_STEP_CHASER, _PowerRipDuration / 1000);
								} else {
//								SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 12533, true);
									L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(9415, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//								L1EffectSpawn.getInstance().spawnEffect2(9415, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);
//								pc.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
									pc.setSkillEffect(_skillId, _PowerRipDuration);
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
									on_icons(pc, SHADOW_STEP, _PowerRipDuration / 1000);

								}
							}
						} else if (cha instanceof L1NpcInstance || cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance
								|| cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							_player.setHeading(_player.targetDirection(npc.getX(), npc.getY()));
							_player.Panthera_attack(npc, skill_effect, attack_effect, SHADOW_STEP);
//						System.out.println(_isGlanceCheckFail);
							_isShadow_step = _magic.calcProbabilityMagic(_skillId);
							if (_isShadow_step) {
								if (_player.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
									_PowerRipDuration = PowerRipTimeArray[rnd];
//								SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 20778, true);
									L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(21072, L1SkillId.SHADOW_STEP_CHASER, cha.getX(), cha.getY(), cha.getMapId(), cha);
//								L1EffectSpawn.getInstance().spawnEffect2(21072, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);
									npc.setSkillEffect(L1SkillId.SHADOW_STEP_CHASER, _PowerRipDuration);
									npc.set발묶임상태(true);
								} else {
									_PowerRipDuration = PowerRipTimeArray[rnd];
//								SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 12533, true);
									L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(9415, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//								L1EffectSpawn.getInstance().spawnEffect2(9415, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);
//								npc.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
									npc.setSkillEffect(_skillId, _PowerRipDuration);
									npc.set발묶임상태(true);
								}
							}
						}
					}
					break;




					case BLADE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.send_effect(18514, true);
							pc.broadcastPacket(SC_DAMAGE_OF_TIME_NOTI.attack_send(_player, pc), true, true);
						} else if (cha instanceof L1NpcInstance || cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance
								|| cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.broadcastPacket(new S_SkillSound(npc.getId(), 18514));
							npc.broadcastPacket(SC_DAMAGE_OF_TIME_NOTI.attack_send(_player, npc), true, true);
						}
					}
					break;
					case TYRANT:{
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_player.isPassive(MJPassiveID.TYRANT_EXCUTION.toInt())) {
								L1Magic magic = new L1Magic(_player, _target);
								boolean isSuc = magic.calcProbabilityMagic(L1SkillId.TYRANT_EXCUTION);
								if (isSuc) {
									if (_target.hasSkillEffect(L1SkillId.TYRANT_EXCUTION)) {
										_target.killSkillEffectTimer(L1SkillId.TYRANT_EXCUTION);
									}
									_target.setSkillEffect(L1SkillId.TYRANT_EXCUTION, 4000);
									_target.addAttackDelayRate(-50);
//								on_icons(pc, _skillId, presher_time / 1000);
									pc.set_Tyrant_Excute(true);
									on_icons(pc, _skillId, 4);
								}
								pc.send_effect(21437, true);
							} else {
								pc.send_effect(20726, true);
							}
						} else if (cha instanceof L1NpcInstance || cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance
								|| cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.broadcastPacket(new S_SkillSound(npc.getId(), 20726));
						}
					}

					break;

					case PRIME: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.PRIME)) {
								pc.removeSkillEffect(L1SkillId.PRIME);
								pc.setPrime_War_Zone(false);
							}

							if (_player.getId() == pc.getId()) {
								pc.addDmgup(9);
								pc.addBowDmgup(9);
								pc.addHitup(9);
								pc.addBowHitup(9);
								pc.getAbility().addSp(6);
								pc.addBaseMagicHitUp(6);
								pc.addMaxHp(500);
								if (pc.getLevel() >= 85 && pc.getLevel() <= 89) {
									pc.set_pvp_defense(5);
								} else if (pc.getLevel() >= 90 && pc.getLevel() <= 94) {
									pc.set_pvp_defense(10);
								} else if (pc.getLevel() >= 95) {
									pc.set_pvp_defense(15);
								}
								pc.addSpecialPierce(eKind.ABILITY, 15);
								pc.send_effect(18582, true);
								SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
								pc.setIsPrimeCast(true);
								MJNotiSkillModel model = MJNotiSkillService.service().model(L1SkillId.PRIME_SELF);
								model.icons(pc, _getBuffIconDuration, true);
								pc.setSkillEffect(L1SkillId.PRIME, _getBuffIconDuration * 1000);
//								System.out.println(pc.getName()+": 효과받음");

							} else {
								if (_player.getClanid() == pc.getClanid()) {
									int spellId = 0;
									boolean spellcheck = SkillsTable.getInstance().spellCheck(pc.getId(), PRIME);
									if (pc.isCrown() && spellcheck) {
										pc.addDmgup(9);
										pc.addBowDmgup(9);
										pc.addHitup(9);
										pc.addBowHitup(9);
										pc.getAbility().addSp(6);
										pc.addBaseMagicHitUp(6);
										pc.addMaxHp(500);
										if (pc.getLevel() >= 85 && pc.getLevel() <= 89) {
											pc.set_pvp_defense(5);
										} else if (pc.getLevel() >= 90 && pc.getLevel() <= 94) {
											pc.set_pvp_defense(10);
										} else if (pc.getLevel() >= 95) {
											pc.set_pvp_defense(15);
										}
										pc.addSpecialPierce(eKind.ABILITY, 15);
										pc.send_effect(18582, true);
										SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
										pc.setIsPrimeCast(true);
										MJNotiSkillModel model = MJNotiSkillService.service().model(L1SkillId.PRIME_SELF);
										model.icons(pc, _getBuffIconDuration, true);
										pc.setSkillEffect(L1SkillId.PRIME, _getBuffIconDuration * 1000);
//										System.out.println(pc.getName()+": 효과받음");
									} else {
										if (MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.getCastleIdByArea(pc))) {
											if (0 < L1CastleLocation.getCastleIdByArea(pc)) { // 공성존
												spellId = L1SkillId.PRIME_SIEGE;
												pc.addDmgup(9);
												pc.addBowDmgup(9);
												pc.addHitup(9);
												pc.addBowHitup(9);
												pc.getAbility().addSp(6);
												pc.addBaseMagicHitUp(6);
												pc.addMaxHp(500);
												if (pc.getLevel() >= 85 && pc.getLevel() <= 89) {
													pc.set_pvp_defense(5);
												} else if (pc.getLevel() >= 90 && pc.getLevel() <= 94) {
													pc.set_pvp_defense(10);
												} else if (pc.getLevel() >= 95) {
													pc.set_pvp_defense(15);
												}
												//										player.send_effect(_skill.getCastGfx());
												pc.setPrime_War_Zone(true);

											}
										} else { // 일반
											spellId = L1SkillId.PRIME_NO_SIEGE;
											pc.addDmgup(3);
											pc.addBowDmgup(3);
											pc.addHitup(3);
											pc.addBowHitup(3);
											pc.getAbility().addSp(2);
											pc.addBaseMagicHitUp(2);
											//									pc.send_effect(_skill.getCastGfx());
										}

										pc.send_effect(18406, true);
										pc.setSkillEffect(L1SkillId.PRIME, _getBuffIconDuration * 1000);

										MJNotiSkillModel model = MJNotiSkillService.service().model(spellId);
										model.icons(pc, _getBuffIconDuration, true);
									}
									// on_icons(pc, L1SkillId.PRIME, _getBuffIconDuration);
								}
//								System.out.println(pc.getName()+": 효과받음");
							}

						}
						if (_user instanceof L1PcInstance) {
							L1PinkName.onHelp(cha, _user);
						}
						break;
					}
					case DEVINE_PROTECTION:{
						_player.set_divine_protection(Config.MagicAdSetting_Wizard.DEVINE_PROTECTION_BARRIER);
						_player.sendPackets(SC_INSTANCE_HP_NOTI.make_stream(_player), true);
					}
					break;
					case DISINTEGRATE: {
						try {
							if(_user.isPassive(MJPassiveID.DISINTEGRATE_NEMESIS.toInt()) && _isnemesis) {
//							System.out.println("네메");
								int targetLevel = 0;
								int diffLevel = 0;

								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									targetLevel = pc.getLevel();
								} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
										|| cha instanceof L1PetInstance) {
									L1NpcInstance npc = (L1NpcInstance) cha;
									targetLevel = npc.getLevel();
								}
								diffLevel = _user.getLevel() - targetLevel;
								int ran = random.nextInt(100) + 1;

								if (diffLevel < -5) {
									int[] stunTimeArray = Config.MagicAdSetting_Wizard.DISINTEGRATEMS;
									_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								} else if (diffLevel >= -5 && diffLevel <= -3) {
									int[] stunTimeArray = Config.MagicAdSetting_Wizard.DISINTEGRATEMS1;
									_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								} else if (diffLevel >= -2 && diffLevel < 0) {
									int[] stunTimeArray = Config.MagicAdSetting_Wizard.DISINTEGRATEMS2;
									_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								} else if (diffLevel == 0) {
									int[] stunTimeArray = Config.MagicAdSetting_Wizard.DISINTEGRATEMS3;
									_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								} else if (diffLevel >= 0 && diffLevel <= 2) {
									int[] stunTimeArray = Config.MagicAdSetting_Wizard.DISINTEGRATEMS4;
									_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								} else if (diffLevel >= 3 && diffLevel <= 5) {
									int[] stunTimeArray = Config.MagicAdSetting_Wizard.DISINTEGRATEMS5;
									_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								} else if (diffLevel > 5) {
									int[] stunTimeArray = Config.MagicAdSetting_Wizard.DISINTEGRATEMS6;
									_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
								}
								if (_player.getWeapon() != null) {
									L1ItemInstance weapon = _player.getWeapon();

									if (weapon.getItemId() == 203041) {
										_shockStunDuration += 1000;
									}
								}

								if (DCCD != 0) {
									_shockStunDuration -= DCCD;
								}
								if (ICCD != 0) {
									_shockStunDuration += ICCD;
								}
								if (_shockStunDuration <= 0) {
									_shockStunDuration = 100;
								}
								if (_shockStunDuration <= 0) {
									System.out.println(_skillId + " 持續時間錯誤");
									return ;
								}
								L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(120855, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							L1EffectSpawn.getInstance().spawnEffect2(120855, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 20116, true);
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true), true);
									pc.setSkillEffect(DISINTEGRATE, _shockStunDuration);
									on_icons(pc, L1SkillId.DISINTEGRATE, _shockStunDuration / 1000);
								} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
										|| cha instanceof L1PetInstance) {
									L1NpcInstance npc = (L1NpcInstance) cha;
									npc.setParalyzed(true);
									npc.setSkillEffect(DISINTEGRATE, _shockStunDuration);
									npc.setParalysisTime(_shockStunDuration);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					break;
					case FORCE_STUN: {
						int targetLevel = 0;
						int diffLevel = 0;

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						diffLevel = _user.getLevel() - targetLevel;

						// TODO 예)시전자 레벨:80 상대방 레벨:86~격차)
						if (diffLevel < Config.MagicAdSetting_Knight.FORCESTUN_LVL) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.FORCESTUN_MS;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:83~85까지 (-3~격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.FORCESTUN_LVL1 && diffLevel <= Config.MagicAdSetting_Knight.FORCESTUN_LVL2) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.FORCESTUN_MS1;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:78~82까지 (-2~+2격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.FORCESTUN_LVL3 && diffLevel <= Config.MagicAdSetting_Knight.FORCESTUN_LVL4) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.FORCESTUN_MS2;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:75~77까지 (-1~-2격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.FORCESTUN_LVL5 && diffLevel <= Config.MagicAdSetting_Knight.FORCESTUN_LVL6) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.FORCESTUN_MS3;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:70~74까지 (-1~-4격차)
						} else if (diffLevel >= Config.MagicAdSetting_Knight.FORCESTUN_LVL7 && diffLevel <= Config.MagicAdSetting_Knight.FORCESTUN_LVL8) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.FORCESTUN_MS4;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							// TODO 예)시전자 레벨:80 상대방 레벨:-69~(이하)
						} else if (diffLevel > Config.MagicAdSetting_Knight.FORCESTUN_LVL9) {
							int[] stunTimeArray = Config.MagicAdSetting_Knight.FORCESTUN_MS5;
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
						}

						if (_player.getWeapon() != null) {
							L1ItemInstance weapon = _player.getWeapon();
							if (weapon.getItemId() == 7000240) {
								_shockStunDuration += 1000;
							}
						}
						if (DCCD != 0) {
							_shockStunDuration -= DCCD;
						}
						if (ICCD != 0) {
							_shockStunDuration += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}
						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(8502103, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							on_icons(pc, _skillId, _shockStunDuration / 1000);
//						SC_TOGGLE_EFFECT_NOTI.sendfromPc(_player, pc, true, 3);
//						SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(pc, 18588, true);
//						pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 18588, true));
//						pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 18588, true));
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
							pc.setSkillEffect(_skillId, _shockStunDuration);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
							npc.setSkillEffect(_skillId, _shockStunDuration);
//						npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 18588, true));
						}
					}
					break;
					case ETERNITI: {
						int targetDirection = _user.targetDirection(_targetX, _targetY);
						if (_user.getHeading() != targetDirection) {
							_user.setHeading(targetDirection);
							_user.sendPackets(new S_ChangeHeading(_user));
							_user.broadcastPacket(new S_ChangeHeading(_user));
						}

						S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
						_user.sendPackets(gfx);
						_user.broadcastPacket(gfx);

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							int h = MJCommons.calcheading(_user.getX(), _user.getY(), pc.getX(), pc.getY());
							_user.setHeading(h);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							int h = MJCommons.calcheading(_user.getX(), _user.getY(), npc.getX(), npc.getY());
							_user.setHeading(h);
						}

						// int[] stunTimeArray_ETERNITI = null;
						int effectNpcId = 8502104;
						int[] stunTimeArray = Config.MagicAdSetting_Wizard.ETERNITI_MS;
						int rnd = random.nextInt(stunTimeArray.length);
						_shockStunDuration = stunTimeArray[rnd];

						if (DCCD != 0) {
							_shockStunDuration -= DCCD;
						}
						if (ICCD != 0) {
							_shockStunDuration += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}
						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						// System.out.println(stunTimeArray[rnd]);
						int ran = random.nextInt(100) + 1;
						_isEterniti = _magic.calcProbabilityMagic(_skillId);
//					System.out.println(_isEterniti);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.ETERNITI))
								pc.removeSkillEffect(L1SkillId.ETERNITI);
							if (_isEterniti) {
//							SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 18562, true);
//							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							System.out.println(eff.getId());
								pc.broadcastPacket(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, pc.getId(), 18562, true), true);
								pc.sendPackets(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, pc.getId(), 18562, true), true);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, true));
								pc._EternitiAttacker = _player;
								_target.setSkillEffect(L1SkillId.ETERNITI, _shockStunDuration);
								on_icons(pc, L1SkillId.ETERNITI, _shockStunDuration / 1000);
							} else {
								pc.send_effect(18418);
							}
						} else if (_target instanceof L1MonsterInstance || _target instanceof L1NpcInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance
								|| _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							if (_isEterniti) {
//							SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, 18562, true);
//							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _shockStunDuration, npc.getX(), npc.getY(), npc.getMapId(), npc);
								npc.broadcastPacket(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, npc.getId(), 18562, true), true);
								npc._EternitiAttacker = _player;
								npc.setSkillEffect(L1SkillId.ETERNITI, _shockStunDuration);
								npc.set발묶임상태(true);
							} else {
								npc.send_effect(18418);
							}
						}
					}
					break;
				/*case ELVEN_STRIKE: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						int h = MJCommons.calcheading(_user.getX(), _user.getY(), pc.getX(), pc.getY());
						_user.setHeading(h);
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						int h = MJCommons.calcheading(_user.getX(), _user.getY(), npc.getX(), npc.getY());
						_user.setHeading(h);
					}
					int[] stunTimeArray = null;
					int effectNpcId = 73201282;
					stunTimeArray = new int[]{ 2100, 2300, 2500, 2700, 2900, 3100, 3300, 3500, 3700, 3900, 4000, 4200, 4400, 4600, 4800, 5000 };
					int rnd = random.nextInt(stunTimeArray.length);
					int time = stunTimeArray[rnd];
					_isElvenstrike = _magic.calcProbabilityMagic(_skillId);
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						if (_isElvenstrike) {
							L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, time - 500, _target.getX(), _target.getY(), _target.getMapId(), cha);
							pc.setSkillEffect(L1SkillId.STATUS_FREEZE, time);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
						} else {
							pc.send_effect(19578);
						}
					} else if (_target instanceof L1MonsterInstance || _target instanceof L1NpcInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) _target;
						if (_isElvenstrike) {
							L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, time - 500, _target.getX(), _target.getY(), _target.getMapId(), cha);
							npc.setSkillEffect(L1SkillId.STATUS_FREEZE, time);
							npc.set발묶임상태(true);
						} else {
							npc.send_effect(19578);
						}
					}
				}
					break;*/
					case POTENTIAL: {
						L1PcInstance pc = (L1PcInstance) cha;
						on_icons(pc, _skillId, _getBuffIconDuration);

						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_OwnCharStatus(pc));
						pc.sendPackets(new S_SPMR(pc));
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
					break;
/*				case DEMOLITION: {
					int targetLevel = 0;
					int diffLevel = 0;
					int TicTime = 1000;
					int Magicdmg = SkillsTable.getInstance().getTemplate(L1SkillId.DEMOLITION).getDamageValue();

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						targetLevel = pc.getLevel();
						int h = MJCommons.calcheading(_user.getX(), _user.getY(), pc.getX(), pc.getY());
						_user.setHeading(h);
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						targetLevel = npc.getLevel();
						int h = MJCommons.calcheading(_user.getX(), _user.getY(), npc.getX(), npc.getY());
						_user.setHeading(h);
					}

					diffLevel = _user.getLevel() - targetLevel;

					// int[] stunTimeArray = null;
					int effectNpcId = 8502105;
					// 밑으로 갈수록 레벨 차이가 많이남 즉 시간초가 더 길어짐
					int[] stunTimeArray = Config.MagicAdSetting_Warrior.DEMOLITION_MS;
					int rnd = random.nextInt(stunTimeArray.length);
					_shockStunDuration = stunTimeArray[rnd];

					// System.out.println(stunTimeArray[rnd]);

					L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);

					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						if (pc.hasSkillEffect(L1SkillId.DEMOLITION))
							pc.removeSkillEffect(L1SkillId.DEMOLITION);
						pc.Desperadolevel = _user.getLevel();
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, true));
						_target.setSkillEffect(L1SkillId.DEMOLITION, _shockStunDuration);
						on_icons(pc, L1SkillId.DEMOLITION, -1);
						L1DemolitionDamage.doInfection(_user, _target, TicTime, Magicdmg);
					} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) _target;
						npc.setSkillEffect(L1SkillId.DEMOLITION, _shockStunDuration);
						npc.set발묶임상태(true);
						L1DemolitionDamage.doInfection(_user, _target, TicTime, Magicdmg);
					}
				}
					break;*/
					case MAFR: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							on_icons(pc, MAFR, _getBuffIconDuration);
						}
					}
					break;
					case ALTERNATE:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setSpear_Mode_Type(pc.isSpearModeType() ? false : true);
							pc.send_effect(pc.isSpearModeType() ? 19354 : 19351);
							SC_USER_FORM_NOTI.user_form_send(pc, pc.isSpearModeType());
							int position = 0;
							position = pc.isSpearModeType() ? 121 : 24;
							pc.sendPackets(new S_CharVisualUpdate(pc, position));
							Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, pc, pc.getWeapon()), true);
						}
						break;
					case FORCE_WAVE:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							int h = MJCommons.calcheading(pc.getX(), pc.getY(), _target.getX(), _target.getY());
							if (pc.getHeading() != h) {
								pc.setHeading(h);
								pc.sendPackets(new S_ChangeHeading(pc));
								pc.broadcastPacket(new S_ChangeHeading(pc));
							}

							if (!pc.isSpearModeType()) {
								List<L1Object> objects = L1World.getInstance().getVisibleObjects(cha, 3);
								for (L1Object tgobj : objects) {
									if (tgobj == null) {
										continue;
									}
									if (!(tgobj instanceof L1Character)) {
										continue;
									}

									L1Character target = (L1Character) tgobj;
									if (target instanceof L1PcInstance) {
										L1PcInstance target_pc = (L1PcInstance) target;
										if (target_pc.getSafetyZone()) {
											continue;
										}
										if (target_pc.getClanid() > 0 && target_pc.getClan() == pc.getClan()) {
											continue;
										}
										if (target_pc.getParty() != null && target_pc.getParty() == pc.getParty()) {
											continue;
										}
									}

									_magic = new L1Magic(pc, target);
									dmg = _magic.calcMagicDamage(L1SkillId.FORCE_WAVE);
									_magic.commit(dmg, 0);

									target.broadcastPacket(new S_DoActionGFX(target.getId(), 2));
									target.sendPackets(new S_DoActionGFX(target.getId(), 2));
								}

								pc.broadcastPacket(new S_DoActionGFX(pc.getId(), _skill.getActionId()));
								pc.sendPackets(new S_DoActionGFX(pc.getId(), _skill.getActionId()));
								pc.send_effect(19378);
							} else {
								List<L1Object> objects = L1World.getInstance().getVisibleObjects(cha, 5);
								for (L1Object tgobj : objects) {
									if (tgobj == null) {
										continue;
									}
									if (!(tgobj instanceof L1Character)) {
										continue;
									}

									L1Character target = (L1Character) tgobj;
									if (target instanceof L1PcInstance) {
										L1PcInstance target_pc = (L1PcInstance) target;
										if (target_pc.getSafetyZone()) {
											continue;
										}
										if (target_pc.getClanid() > 0 && target_pc.getClan() == pc.getClan()) {
											continue;
										}
										if (target_pc.getParty() != null && target_pc.getParty() == pc.getParty()) {
											continue;
										}
									}

									int oh = pc.getHeading();
									int th = MJCommons.calcheading(pc.getX(), pc.getY(), target.getX(), target.getY());
									if (!(oh == th))
										continue;

									_magic = new L1Magic(pc, target);
									dmg = _magic.calcMagicDamage(L1SkillId.FORCE_WAVE);
									_magic.commit(dmg, 0);

									target.broadcastPacket(new S_DoActionGFX(target.getId(), 2));
									target.sendPackets(new S_DoActionGFX(target.getId(), 2));
								}
								pc.broadcastPacket(new S_DoActionGFX(pc.getId(), _skill.getActionId()));
								pc.sendPackets(new S_DoActionGFX(pc.getId(), _skill.getActionId()));
								pc.send_effect(19381);
							}
						}
						break;
					case VANGUARD:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (!pc.isSpearModeType()) {
								pc.addMoveDelayRate(25);
								pc.addAttackDelayRate(10);
								pc.setVanguardType(false);
							} else {
								pc.addAttackDelayRate(10);
								pc.setVanguardType(true);
							}
							on_icons(pc, _skillId, _getBuffIconDuration);
						}
						break;
					case RECOVERY:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (remove_stun_skills(pc,
									new Integer[] { L1SkillId.SHOCK_STUN, L1SkillId.EMPIRE, L1SkillId.PANTHERA, L1SkillId.BONE_BREAK, L1SkillId.CHAINSWORD_STUN,
											L1SkillId.MOB_SHOCKSTUN_30,	L1SkillId.Mob_RANGESTUN_30, L1SkillId.MOB_RANGESTUN_20, L1SkillId.MOB_RANGESTUN_19,
											L1SkillId.MOB_RANGESTUN_18, L1SkillId.Maeno_STUN, L1SkillId.fornos_STUN, L1SkillId.Moster_STUN, L1SkillId.ANTA_SHOCKSTUN,
											L1SkillId.OMAN_STUN, L1SkillId.BOS_STUN18, L1SkillId.Besi_STUN, L1SkillId.EARTH_BIND, L1SkillId.ICE_LANCE,
											L1SkillId.MOB_BASILL, L1SkillId.MOB_COCA })) {

								List<L1Object> objects = L1World.getInstance().getVisibleObjects(cha, 0);
								for (L1Object tgobj : objects) {
									if (tgobj instanceof L1EffectInstance) {
										L1EffectInstance effect = (L1EffectInstance) tgobj;
										effect.deleteMe();
									}
								}

								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
								L1SkillId.onFreezeAfterDelay(pc);
								pc.send_effect(19339);
							}
						}
						break;
					case PRESHER: {
						int targetLevel = 0;
						int diffLevel = 0;
						int presher_time = _skill.getBuffDuration();
						int effect_id = 73201285;
//					int effect_id = 19329;

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						diffLevel = _user.getLevel() - _target.getLevel();
//					System.out.println(diffLevel);
						if (_player != null) {
//						effect_id = 73201284;
//						effect_id = 19716;
							if (diffLevel < Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_MS;
								presher_time = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL1 && diffLevel <= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL2) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_MS1;
								presher_time = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL3 && diffLevel <= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL4) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_MS2;
								presher_time = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL5 && diffLevel <= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL6) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_MS3;
								presher_time = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL7 && diffLevel <= Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL8) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_MS4;
								presher_time = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_LVL9) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.PRESHER_DEATH_RECALL_MS5;
								presher_time = stunTimeArray[random.nextInt(stunTimeArray.length)];
							}
						}
//					System.out.println(presher_time);
						if (DCCD != 0) {
							presher_time -= DCCD;
						}
						if (ICCD != 0) {
							presher_time += ICCD;
						}
						if (presher_time <= 0) {
							presher_time = 100;
						}
						if (presher_time <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						if (_magic.calcProbabilityMagic(_skillId)) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.setPresherPc(_player);
								if (_player != null && _player.isPassive(MJPassiveID.PRESHER_DEATH_RECALL.toInt())) {
									if (_magic.calcProbabilityMagic(_skillId)) {
										effect_id = 73201284;
										pc.setPresherDeathRecall(true);
//									SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, effect_id, true);
										L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, PRESHER, cha.getX(), cha.getY(), cha.getMapId(), cha);
//									L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, presher_time, cha.getX(), cha.getY(), cha.getMapId(), cha);
										pc.setSkillEffect(PRESHER, presher_time);
										pc.setSkillEffect(DECAY_POTION, presher_time); // 디케이
										pc.setSkillEffect(L1SkillId.STATUS_FREEZE, presher_time);
										pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
										on_icons(pc, _skillId, presher_time / 1000);
										return;
									}
								} else if (_player != null && !_player.isPassive(MJPassiveID.PRESHER_DEATH_RECALL.toInt())) {
									pc.setPresherPc(_player);
									pc.setSkillEffect(PRESHER, presher_time);
									pc.setSkillEffect(L1SkillId.STATUS_FREEZE, presher_time);
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
									on_icons(pc, _skillId, presher_time / 1000);
									return;
								}
							} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) _target;
								npc.setPresherPc(_player);
								npc.setSkillEffect(PRESHER, presher_time);
								npc.setSkillEffect(L1SkillId.STATUS_FREEZE, presher_time);
								npc.set발묶임상태(true);
								return;
							}
						}
					}
					break;
					case CRUEL: {
						int targetLevel = 0;
						int diffLevel = 0;
						int effect_id = 73201283;
//					int effect_id = 19752;

						int h = MJCommons.calcheading(_player.getX(), _player.getY(), _target.getX(), _target.getY());
						if (_player.getHeading() != h) {
							_player.setHeading(h);
							_player.sendPackets(new S_ChangeHeading(_player));
							_player.broadcastPacket(new S_ChangeHeading(_player));
						}

						if (cha instanceof L1MonsterInstance) {
							int th = MJCommons.calcheading(_target.getX(), _target.getY(), _player.getX(), _player.getY());
							if (_target.getHeading() != th) {
								_target.setHeading(th);
								_target.sendPackets(new S_ChangeHeading(_target));
								_target.broadcastPacket(new S_ChangeHeading(_target));
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 18);
						_player.sendPackets(gfx);
						_player.broadcastPacket(gfx);

						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null && _player.isPassive(MJPassiveID.CRUEL_CONBICTION.toInt())) {
							effect_id = 73201286;
//						effect_id = 18602;
							if (diffLevel < Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_MS;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL1 && diffLevel < Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL2) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_MS1;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL3 && diffLevel < Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL4) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_MS2;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL5 && diffLevel < Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL6) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_MS3;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL7 && diffLevel < Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL8) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_MS4;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_LVL9) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_CONBICTION_MS5;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							}
						} else {
							if (diffLevel < Config.MagicAdSetting_Lancer.CRUEL_LVL) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_MS;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_LVL1 && diffLevel <= Config.MagicAdSetting_Lancer.CRUEL_LVL2) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_MS1;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_LVL3 && diffLevel <= Config.MagicAdSetting_Lancer.CRUEL_LVL4) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_MS2;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_LVL5 && diffLevel <= Config.MagicAdSetting_Lancer.CRUEL_LVL6) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_MS3;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Lancer.CRUEL_LVL7 && diffLevel <= Config.MagicAdSetting_Lancer.CRUEL_LVL8) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_MS4;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_Lancer.CRUEL_LVL9) {
								int[] stunTimeArray = Config.MagicAdSetting_Lancer.CRUEL_MS5;
								_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							}
						}
						if (_player.getWeapon() != null) {
							L1ItemInstance weapon = _player.getWeapon();
							if (weapon.getItemId() == 203042) {
								_shockStunDuration += 1000;
							}
						}
						if (DCCD != 0) {
							_shockStunDuration -= DCCD;
						}
						if (ICCD != 0) {
							_shockStunDuration += ICCD;
						}
						if (_shockStunDuration <= 0) {
							_shockStunDuration = 100;
						}

						if (_shockStunDuration <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						if (_magic.calcProbabilityMagic(_skillId)) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (!_player.isSpearModeType()) {
//								System.out.println("근거리모드");
									pc.Cruel_attack_Short(_player, 19320);
								} else {
									pc.Cruel_attack_Long(_player, 19323);
								}
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
//							pc.setSkillEffect(L1SkillId.SHOCK_STUN, _shockStunDuration);
								pc.setSkillEffect(L1SkillId.CRUEL, _shockStunDuration);
								L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
								//L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
//							SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, effect_id, true);
								on_icons(pc, _skillId, _shockStunDuration / 1000);
//							System.out.println("크루얼 시간: "+_shockStunDuration);
							} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
									|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								npc.setParalyzed(true);
								npc.setParalysisTime(_shockStunDuration);
								npc.setSkillEffect(_skillId, _shockStunDuration);
								if (!_player.isSpearModeType()) {
									npc.send_effect(19320);
								} else {
									npc.send_effect(19323);
								}
//							SC_WORLD_PUT_OBJECT_NOTI.make_stream(cha, effect_id, true);
								L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
								//L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effect_id, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId(), cha);
							}
						}
					}
					break;
					case SHOCK_ATTACK: {
						int targetLevel = 0;
						int diffLevel = 0;
						int skill_time = 0;

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
								|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						diffLevel = _user.getLevel() - targetLevel;

						if (_player != null) {
							if (diffLevel < Config.MagicAdSetting_Knight.SHOCKATTACK_LVL) {
								int[] SkillTimeArray = Config.MagicAdSetting_Knight.SHOCKATTACK_MS;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL1 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL2) {
								int[] SkillTimeArray = Config.MagicAdSetting_Knight.SHOCKATTACK_MS1;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL3 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL4) {
								int[] SkillTimeArray = Config.MagicAdSetting_Knight.SHOCKATTACK_MS2;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL5 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL6) {
								int[] SkillTimeArray = Config.MagicAdSetting_Knight.SHOCKATTACK_MS3;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel >= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL7 && diffLevel <= Config.MagicAdSetting_Knight.SHOCKATTACK_LVL8) {
								int[] SkillTimeArray = Config.MagicAdSetting_Knight.SHOCKATTACK_MS4;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							} else if (diffLevel > Config.MagicAdSetting_Knight.SHOCKATTACK_LVL9) {
								int[] SkillTimeArray = Config.MagicAdSetting_Knight.SHOCKATTACK_MS5;
								skill_time = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
							}
						}
						if (DCCD != 0) {
							skill_time -= DCCD;
						}
						if (ICCD != 0) {
							skill_time += ICCD;
						}
						if (skill_time <= 0) {
							skill_time = 100;
						}
						if (skill_time <= 0) {
							System.out.println(_skillId + " 持續時間錯誤");
							return ;
						}
						if (_magic.calcProbabilityMagic(_skillId)) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.addMoveDelayRate(-50);
								pc.addSpecialResistance(eKind.ABILITY, -10);
								SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
								pc.setSkillEffect(L1SkillId.SHOCK_ATTACK, skill_time);
								on_icons(pc, _skillId, (skill_time) / 1000);
							}
						}
					}
					break;
					case BURNING_SHOT: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(BURNING_SHOT)) {
								System.out.println("버닝샷 있음?");
								pc.removeSkillEffect(BURNING_SHOT);
/*							pc.removeSkillEffect(STATUS_FREEZE);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, false));
							pc.getResistance().addcalcPcDefense(-10);
							pc.addSpecialResistance(eKind.ALL, -3);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
							L1SkillUse.off_icons(pc, BURNING_SHOT);
							pc.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, pc, pc.getWeapon()), true);
							return;*/
							} else {
								pc.setSkillEffect(L1SkillId.STATUS_FREEZE, -1);
								pc.setSkillEffect(L1SkillId.BURNING_SHOT, -1);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, true));
								pc.getResistance().addcalcPcDefense(10);
								pc.addSpecialResistance(eKind.ALL, 3);
								SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
								on_icons(pc, _skillId, -1);
								pc.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, pc, pc.getWeapon()), true);
							}
							return;
						}
					}




//
//				return;
//				case VISION_TELEPORT: {
//					L1PcInstance pc = (L1PcInstance) _user;
//					int skill_effect = 20105;
//					System.out.println(_targetX);
//					System.out.println(_targetY);
//					System.out.println(pc.getMapId());

//					pc.start_teleport(_targetX, _targetY, pc.getMapId(), pc.getHeading(), skill_effect, false, false);
//					pc.setHeading(pc.targetDirection(_targetX, _targetY));
//					pc.sendPackets(new S_ChangeHeading(pc));
//					pc.broadcastPacket(new S_ChangeHeading(pc));

					//	_user.start_teleport(_targetX, _targetY, _user.getMapId(), _user.getHeading(), 20105, false, false);


					//L1Teleport.getInstance().doTeleport(pc, _targetX, _targetY, pc.getMapId());
					//pc.send_effect(skill_effect);

					//pc.start_teleport(_targetX, _targetY, pc.getMapId(), pc.getHeading(), 18339, false, false);
					//	pc.sendPackets(new S_ChangeHeading(pc));
					//	pc.broadcastPacket(new S_ChangeHeading(pc));
					//pc.send_effect(2235, false);//2235 124461
					//	_player.VISION_TELEPORT(skill_effect);

						/*int readed_short_1 = 0;
						int readed_short_2 = 0;
						L1PcInstance pc = (L1PcInstance) cha;
						pc.send_effect(20105, false);//비전 텔레포트 시작
						pc.start_teleport(readed_short_1, readed_short_2, pc.getMapId(), pc.getHeading(), 18339, false, false);
						pc.send_effect(20107, false);//비전 텔레포트 도착*/
//					}
//						break;
					default:
						break;
				}
				if (_calcType == PC_PC || _calcType == NPC_PC) {
					switch (_skillId) {

						case TELEPORT:
						case MASS_TELEPORT: {
							_player.set_MassTel(true);
							L1PcInstance pc = (L1PcInstance) cha;
//						ArrayList<L1PcInstance> Mass_Tel_member = new ArrayList<L1PcInstance>();
							Random random = new Random();
							if (_bookmark_x != 0) {
								if (pc.getMap().isEscapable() || pc.isGm()) {
									L1Map map = L1WorldMap.getInstance().getMap(_bookmark_mapid);
									if (_skillId == MASS_TELEPORT) {

										for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 3)) {
											if (pc.getClanid() != 0 && member.getClanid() == pc.getClanid() && member.getId() != pc.getId() && !member.isPrivateShop()) {
												int newX2 = _bookmark_x + random.nextInt(3) + 1;
												int newY2 = _bookmark_y + random.nextInt(3) + 1;
												member.set_MassTel(true);
												if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
													member.start_teleport(newX2, newY2, _bookmark_mapid, member.getHeading(), 18339, true, true);
												} else {
													member.start_teleport(_bookmark_x, _bookmark_y, _bookmark_mapid, member.getHeading(), 18339, true, true);
												}
											}
										}
									}
									if (pc.getInventory().checkEquippedAtOnce(new int[] { 20288, 900111 })) {
										pc.start_teleport(_bookmark_x, _bookmark_y, _bookmark_mapid, pc.getHeading(), 18339, true, true);
									} else {
										int newX2 = _bookmark_x + random.nextInt(15);
										int newY2 = _bookmark_y + random.nextInt(15);
										if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
											pc.start_teleport(newX2, newY2, _bookmark_mapid, pc.getHeading(), 18339, true, true);
										} else {
											pc.start_teleport(_bookmark_x, _bookmark_y, _bookmark_mapid, pc.getHeading(), 18339, true, true);
										}
									}
								} else {
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
									pc.sendPackets(new S_ServerMessage(79));
								}
							} else {
								if (pc.getMapId() >= 101 && pc.getMapId() <= 110) {
									int find_item_ids[] = { 830022, // 1층
											830023, // 2층
											830024, // 3층
											830025, // 4층
											830026, // 5층
											830027, // 6층
											830028, // 7층
											830029, // 8층
											830030, // 9층
											830031 // 10층
									};
									L1ItemInstance findItem = pc.getInventory().findItemId(find_item_ids[pc.getMapId() - 101]);
									L1ItemInstance findItem2 = pc.getInventory().findItemId(560028);
									if (findItem != null || findItem2 != null) {
										Telbookitem.toActive(pc, 0, null, _skillId);
									} else {
										pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
										pc.sendPackets(new S_ServerMessage(276));
									}
								} else if (pc.getMapId() >= 12852 && pc.getMapId() <= 12861) {
									int find_item_ids[] = {
											830022, // 1층
											830023, // 2층
											830024, // 3층
											830025, // 4층
											830026, // 5층
											830027, // 6층
											830028, // 7층
											830029, // 8층
											830030, // 9층
											830031 // 10층
									};
									L1ItemInstance findItem = pc.getInventory().findItemId(find_item_ids[pc.getMapId() - 12852]);
									L1ItemInstance findItem1 = pc.getInventory().findItemId(4100135); // 환상지배의탑 지배부적
									L1ItemInstance findItem2 = pc.getInventory().findItemId(560028);
									if (findItem != null || findItem1 != null || findItem2 != null) {
										Telbookitem.toActive(pc, 0, null, _skillId);
									} else {
										pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
										pc.sendPackets(new S_ServerMessage(276));
									}
								} else {
									L1Map map = pc.getMap();
									if (map.isTeleportable() || pc.isGm() || (pc.getInventory().checkItem(900111)) && map.isRuler()) {
										Telbookitem.toActive(pc, 0, null, _skillId);
									} else {
										pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
										pc.sendPackets(new S_ServerMessage(276));
									}
								}
							}
						}
						break;
						case TELEPORT_TO_MATHER: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getMap().isEscapable() || pc.isGm()) {
								pc.start_teleport(33051, 32337, 4, 5, 18339, true, true);
							} else {
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
								pc.sendPackets(new S_ServerMessage(647));
							}
						}
						break;
						case BRING_STONE: {
							L1PcInstance pc = (L1PcInstance) cha;
							Random random = new Random();
							L1ItemInstance item = pc.getInventory().getItem(_itemobjid);
							if (item != null) {
								int dark = (int) (10 + (pc.getLevel() * 0.8) + (pc.getAbility().getTotalWis() - 6) * 1.2);
								int brave = (int) (dark / 2.1);
								int wise = (int) (brave / 2.0);
								int kayser = (int) (wise / 1.9);
								int chance = random.nextInt(100) + 1;

								if (item.getItem().getItemId() == 40320) {
									pc.getInventory().removeItem(item, 1);
									if (dark >= chance) {
										pc.getInventory().storeItem(40321, 1);
										pc.sendPackets(new S_ServerMessage(403, "$2475"));
									} else {
										pc.sendPackets(new S_ServerMessage(280));
									}
								} else if (item.getItem().getItemId() == 40321) {
									pc.getInventory().removeItem(item, 1);
									if (brave >= chance) {
										pc.getInventory().storeItem(40322, 1);
										pc.sendPackets(new S_ServerMessage(403, "$2476"));
									} else {
										pc.sendPackets(new S_ServerMessage(280));
									}
								} else if (item.getItem().getItemId() == 40322) {
									pc.getInventory().removeItem(item, 1);
									if (wise >= chance) {
										pc.getInventory().storeItem(40323, 1);
										pc.sendPackets(new S_ServerMessage(403, "$2477"));
									} else {
										pc.sendPackets(new S_ServerMessage(280));
									}
								} else if (item.getItem().getItemId() == 40323) {
									pc.getInventory().removeItem(item, 1);
									if (kayser >= chance) {
										pc.getInventory().storeItem(40324, 1);
										pc.sendPackets(new S_ServerMessage(403, "$2478"));
									} else {
										pc.sendPackets(new S_ServerMessage(280));
									}
								}
							}
						}
						break;
						case SUMMON_MONSTER: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isSM()) {
								pc.sendPackets(new S_ServerMessage(319));
								break;
							}
							if ((pc.getMap().isRecallPets() && !pc.isInWarArea() && pc.getMapId() != 781 && pc.getMapId() != 782) || pc.isGm()) {
								if (pc.getInventory().checkItem(20284)) {
									summonMonster(pc, _bookmark_x);
								} else {
									L1Npc npcTemp = NpcTable.getInstance().getTemplate(120856);
									if (pc.isPassive(MJPassiveID.SUMMONMONSER_GREATE.toInt())) {
										npcTemp = NpcTable.getInstance().getTemplate(120862);
									}
									L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
									summon.setPetcost(0);
								}
								pc.setSM(true);
							} else {
								pc.sendPackets(new S_ServerMessage(79));
							}
						}
						break;
						case LESSER_ELEMENTAL:
						case GREATER_ELEMENTAL: {
							L1PcInstance pc = (L1PcInstance) cha;
							int attr = pc.getElfAttr();
							if (attr != 0) {
								if ((pc.getMap().isRecallPets() && !pc.isInWarArea()) || pc.isGm()) {
									int petcost = 0;
									Object[] petlist = pc.getPetList().values().toArray();
									for (Object pet : petlist) {
										if (pet instanceof L1SummonInstance) {
											petcost += 1;
										}
									}

									if (petcost == 0) {
										int summonid = 0;
										int summons[];
										if (_skillId == LESSER_ELEMENTAL) {
											summons = new int[] { 45306, 45303, 45304, 45305 };
										} else {
											summons = new int[] { 81053, 81050, 81051, 81052 };
										}
										int npcattr = 1;
										for (int i = 0; i < summons.length; i++) {
											if (npcattr == attr) {
												summonid = summons[i];
												i = summons.length;
											}
											npcattr *= 2;
										}
										if (summonid == 0) {
											Random random = new Random();
											int k3 = random.nextInt(4);
											summonid = summons[k3];
										}

										L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
										L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
									} else {
										pc.sendPackets(new S_ServerMessage(319));
									}
								} else {
									pc.sendPackets(new S_ServerMessage(79));
								}
							}
						}
						break;
						/*
						 * case ABSOLUTE_BARRIER: { L1PcInstance pc = (L1PcInstance) cha; on_icons(pc, _skillId, _getBuffIconDuration); } break;
						 */
						case GLOWING_WEAPON: {
							L1PcInstance pc = (L1PcInstance) cha;
							cha.addDmgup(5);
							cha.addBowDmgup(5);
							pc.addHitup(5);
							pc.addBowHitup(5);
							pc.send_action(ActionCodes.ACTION_SkillBuff);
							pc.send_effect(11772, true);
							// pc.sendPackets(new S_SkillSound(pc.getId(), 11772));// 선처리가 되야함
							pc.sendPackets(new S_NewSkillIcon(11772, _getBuffIconDuration));
							// pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 11772,
							// _getBuffIconDuration));
						}
						break;
						case SHINING_SHILD: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance target = (L1PcInstance) cha;

								if (target.getId() == _user.getId()) {
									target.getAC().addAc(-8);
									target.send_effect(3941, true);
									target.send_action(ActionCodes.ACTION_SkillBuff);
									target.sendPackets(new S_OwnCharStatus(target));
									target.sendPackets(new S_NewSkillIcon(3941, _getBuffIconDuration));
								} else {
									target.getAC().addAc(-4);
									target.send_effect(3941, true);
									target.sendPackets(new S_OwnCharStatus(target));
									target.sendPackets(new S_NewSkillIcon(3941, _getBuffIconDuration));
								}
								target.set_shining_shild_obj_id(_user.getId());
							}
							if (_user instanceof L1PcInstance) {
								L1PinkName.onHelp(cha, _user);
							}
						}
						break;
						case SHIELD: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAC().addAc(-2);
							pc.sendPackets(new S_SkillIconShield(2, _getBuffIconDuration));
						}
						break;
						case SHADOW_ARMOR: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc != null && pc.isPassive(MJPassiveID.SHADOW_ARMOR_PASS.toInt())) {
								int bonus = 0;
								if (pc.getLevel() >= 85 && pc.getLevel() <= 94)
									bonus = ((pc.getLevel() - 85) / 2) + 10;
								else if (pc.getLevel() >= 95)
									bonus = 15;

//							pc.add_Magic_defense_per(bonus);
								pc.getInventory().consumeItem(40321, 1);
								pc.getResistance().addMr(bonus);
								pc.setSHADOW_ARMOR_destiny(true);
							} else {
								pc.getResistance().addMr(5);
								pc.sendPackets(new S_SPMR(pc));
							}
						}
						break;
						case DRESS_DEXTERITY: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedDex((byte) 3);
							pc.sendPackets(new S_Dexup(pc, 3, _getBuffIconDuration));
						}
						break;
						case DRESS_MIGHTY: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedStr((byte) 3);
							pc.sendPackets(new S_Strup(pc, 3, _getBuffIconDuration));
						}
						break;
						case SHADOW_FANG: {
							if (!(cha instanceof L1PcInstance)) {
								return;
							}
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDmgup(5);
						}
						break;
						case DOUBLE_BRAKE:{
							if(!(cha instanceof L1PcInstance)) {
								return;
							}
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setSkillEffect(_skillId, _getBuffDuration);

						}
						break;
						case ENCHANT_WEAPON: {
							if (!(cha instanceof L1PcInstance)) {
								return;
							}
							if (_user.hasSkillEffect(L1SkillId.HOLY_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.HOLY_WEAPON);
							}

							if (_user.hasSkillEffect(L1SkillId.BLESS_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.BLESS_WEAPON);
							}

							if (_user.hasSkillEffect(L1SkillId.ENCHANT_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.ENCHANT_WEAPON);
							}

							L1PcInstance pc = (L1PcInstance) cha;
							if (check_skills(pc, new Integer[] { L1SkillId.BLESS_WEAPON }))
								return;

							remove_skills(pc, new Integer[] { _skillId, L1SkillId.HOLY_WEAPON });
							pc.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000);
							pc.addDmgup(2);
						}
						break;
						case HOLY_WEAPON: {
							if (!(cha instanceof L1PcInstance)) {
								return;
							}
							if (_user.hasSkillEffect(L1SkillId.HOLY_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.HOLY_WEAPON);
							}

							if (_user.hasSkillEffect(L1SkillId.BLESS_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.BLESS_WEAPON);
							}

							if (_user.hasSkillEffect(L1SkillId.ENCHANT_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.ENCHANT_WEAPON);
							}

							L1PcInstance pc = (L1PcInstance) cha;
							if (check_skills(pc, new Integer[] { L1SkillId.BLESS_WEAPON, L1SkillId.ENCHANT_WEAPON }))
								return;

							// on_icons(pc, _skillId, _skill.getBuffDuration());
							pc.addDmgup(1);
							pc.addHitup(1);
							pc.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000);
						}
						break;
						case BLESS_WEAPON: {
							if (!(cha instanceof L1PcInstance)) {
								return;
							}
							if (_user.hasSkillEffect(L1SkillId.HOLY_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.HOLY_WEAPON);
							}

							if (_user.hasSkillEffect(L1SkillId.BLESS_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.BLESS_WEAPON);
							}

							if (_user.hasSkillEffect(L1SkillId.ENCHANT_WEAPON)) {
								_user.removeSkillEffect(L1SkillId.ENCHANT_WEAPON);
							}

							L1PcInstance pc = (L1PcInstance) cha;
							remove_skills(pc, new Integer[] { _skillId, L1SkillId.HOLY_WEAPON, L1SkillId.ENCHANT_WEAPON });
							pc.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000);
							pc.addDmgup(2);
							pc.addHitup(2);
							// on_icons(pc, _skillId, _skill.getBuffDuration());
						}
						break;
						case BLESSED_ARMOR: {
							if (!(cha instanceof L1PcInstance)) {
								return;
							}

							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAC().addAc(-3);
							pc.sendPackets(new S_OwnCharStatus(pc));
						}
						break;
						case RESIST_MAGIC: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getResistance().addMr(10);
							pc.sendPackets(new S_SPMR(pc));
						}
						break;
						case CLEAR_MIND:
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								// on_icons(pc, _skillId, _getBuffIconDuration);

								pc.getAbility().addAddedStr((byte) 1);
								pc.getAbility().addAddedDex((byte) 1);
								pc.getAbility().addAddedInt((byte) 1);
							}
							break;
						case ENCHANT_ACURUCY:
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								// on_icons(pc, _skillId, _getBuffIconDuration);
								pc.addHitup(5);
							}
							break;
						case BODY_TO_MIND: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setCurrentMp(pc.getCurrentMp() + 2);
						}
						break;
						/*
						 * case BLOODY_SOUL: { L1PcInstance pc = (L1PcInstance) cha; pc.setCurrentMp(pc.getCurrentMp() + 15); } break;
						 */
						case ELEMENTAL_PROTECTION: {
							L1PcInstance pc = (L1PcInstance) cha;
							int attr = pc.getElfAttr();
							// 1 불 2 물 3 불/물 4 바람 5 바람/불 6 물/바람 7 물/바람/불 8 땅 9 땅/불 10 땅/물 11 땅/물/불 12 땅/바람 13 땅/바람/불 14 땅/물/바람 15 전체
							// 1 불 2 물 4 바말 8 땅 NPC속성
							if (attr == 8) {
								pc.getResistance().addEarth(50);
							} else if (attr == 1) {
								pc.getResistance().addFire(50);
							} else if (attr == 2) {
								pc.getResistance().addWater(50);
							} else if (attr == 4) {
								pc.getResistance().addWind(50);
							}
						}
						break;
						case INVISIBILITY: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.is_combat_field())
								return;

							if (pc.isLock())
								return;

							pc.setSkillEffect(L1SkillId.INVISIBILITY, -1);
							pc.sendPackets(new S_Invis(pc.getId(), 1));
							pc.broadcastPacketForFindInvis(new S_Invis(pc.getId(), 1), true);
							pc.broadcastPacketForFindInvis(new S_RemoveObject(pc), false);
							pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));

							L1DollInstance doll = pc.getMagicDoll();
							if (doll != null) {
								for (L1PcInstance tar : L1World.getInstance().getRecognizePlayer(doll)) {
									doll.onPerceive(tar);
								}
							}


//						if (!pc.isGm()) {
							// pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
//						}
						}
						break;
						case BLIND_HIDING: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.is_combat_field())
								return;
							if (pc.isLock())
								return;

							L1DollInstance doll = pc.getMagicDoll();
							if (doll != null) {
								doll.deleteDoll();
							}

							if (_player.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())){
								pc.setSkillEffect(L1SkillId.BLIND_HIDING_ASSASSIN, 3000);
								pc.setSkillEffect(L1SkillId.BLIND_HIDING, _getBuffIconDuration);
								pc.sendPackets(new S_Invis(pc.getId(), 1));
								pc.broadcastPacketForFindInvis(new S_Invis(pc.getId(), 1), true);
								pc.broadcastPacketForFindInvis(new S_RemoveObject(pc), false);
								pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
								pc.addMoveDelayRate(50);
								for (L1Object monster : L1World.getInstance().getVisibleObjects(_player)) {
									if (monster instanceof L1MonsterInstance) {
										L1MonsterInstance mob = (L1MonsterInstance) monster;
										mob.allTargetClear();
										mob.searchTarget();
									} else {
										continue;
									}
								}
							} else{
								pc.setSkillEffect(L1SkillId.BLIND_HIDING, _getBuffIconDuration);
								pc.sendPackets(new S_Invis(pc.getId(), 1));
								pc.broadcastPacketForFindInvis(new S_Invis(pc.getId(), 1), true);
								pc.broadcastPacketForFindInvis(new S_RemoveObject(pc), false);
								pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
								if (!pc.isGm()) {
								}
							}
						}
						break;
						case BUFF_SAEL: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(L1SkillId.BUFF_SAEL)) {
									pc.removeSkillEffect(L1SkillId.BUFF_SAEL);
								}
								pc.getAC().addAc(-8);
								pc.addBowHitup(6);
								pc.addBowDmgup(3);
								pc.addMaxHp(80);
								pc.addMaxMp(10);
								pc.addHpr(8);
								pc.addMpr(1);
								pc.getResistance().addWater(30);
								pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
								pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
								pc.sendPackets(new S_OwnCharAttrDef(pc));
								pc.sendPackets(new S_SPMR(pc));
							}
						}
						break;
						case BUFF_GUNTER: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(L1SkillId.BUFF_GUNTER)) {
									pc.removeSkillEffect(L1SkillId.BUFF_GUNTER);
								}
								pc.getAbility().addAddedDex((byte) 5);
								pc.addBowHitup(7);
								pc.addBowDmgup(5);
								pc.addMaxHp(100);
								pc.addMaxMp(40);
								pc.addHpr(10);
								pc.addMpr(3);
								pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
								pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
								pc.sendPackets(new S_OwnCharAttrDef(pc));
								pc.sendPackets(new S_SPMR(pc));
							}
						}
						break;
						case God_buff: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAC().addAc(-2);
							pc.addHitup(3);
							pc.addMaxHp(20);
							pc.addMaxMp(13);
							pc.addSpecialResistance(eKind.SPIRIT, 10);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case IRON_SKIN: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAC().addAc(-10);
							pc.sendPackets(new S_SkillIconShield(10, _getBuffIconDuration));
						}
						break;
						case FIRE_SHIELD: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAC().addAc(-4);
							pc.sendPackets(new S_SkillIconShield(4, _getBuffIconDuration));
						}
						break;
						case TARAS_ATTACK_SPEED:{
							L1PcInstance pc = (L1PcInstance) cha;
/*						if (pc.hasSkillEffect(L1SkillId.TARAS_ATTACK_SPEED)) {
							pc.removeSkillEffect(L1SkillId.TARAS_ATTACK_SPEED);
//							pc.addAttackDelayRate(-10);
						}*/
							//System.out.println(_getBuffDuration+"+"+_getBuffIconDuration);
							pc.addAttackDelayRate(10);
							pc.setSkillEffect(L1SkillId.TARAS_ATTACK_SPEED, _getBuffDuration);
							on_icons(pc, L1SkillId.TARAS_ATTACK_SPEED, _getBuffIconDuration);
						}

						break;
						case TARAS_MOVE_SPEED:{
							L1PcInstance pc = (L1PcInstance) cha;
/*						if (pc.hasSkillEffect(L1SkillId.TARAS_MOVE_SPEED)) {
							pc.removeSkillEffect(L1SkillId.TARAS_MOVE_SPEED);
//							pc.addMoveDelayRate(-10);
						}*/
							pc.addMoveDelayRate(10);
							pc.setSkillEffect(L1SkillId.TARAS_MOVE_SPEED, _getBuffDuration);
							on_icons(pc, L1SkillId.TARAS_MOVE_SPEED, _getBuffIconDuration);
						}
						break;
						case PHYSICAL_ENCHANT_STR: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getInventory().checkItem(30001398)) {
								pc.getAbility().addAddedStr((byte) 6);
								pc.sendPackets(new S_Strup(pc, 6, _getBuffIconDuration));
							} else {
								pc.getAbility().addAddedStr((byte) 5);
								pc.sendPackets(new S_Strup(pc, 5, _getBuffIconDuration));
							}
						}
						break;
						case PHYSICAL_ENCHANT_DEX: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getInventory().checkItem(30001398)) {
								pc.getAbility().addAddedDex((byte) 6);
								pc.sendPackets(new S_Dexup(pc, 6, _getBuffIconDuration));
							} else {
								pc.getAbility().addAddedDex((byte) 5);
								pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration));
							}
						}
						break;
						case PC_EXP_UP:{
							L1PcInstance pc = (L1PcInstance) cha;
							on_icons(pc, PC_EXP_UP, 7200);
							pc.add_item_exp_bonus(10);
						}
						break;
						case 나루토감사캔디: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
								pc.getAbility().addAddedDex((byte) 7);
								pc.sendPackets(new S_Dexup(pc, 7, _getBuffIconDuration));
								pc.getAbility().addAddedStr((byte) 7);
								pc.sendPackets(new S_Strup(pc, 7, _getBuffIconDuration));
							} else {
								pc.getAbility().addAddedDex((byte) 6);
								pc.sendPackets(new S_Dexup(pc, 6, _getBuffIconDuration));
								pc.getAbility().addAddedStr((byte) 6);
								pc.sendPackets(new S_Strup(pc, 6, _getBuffIconDuration));
							}
						}
						break;
						case EARTH_WEAPON: {
							L1PcInstance pc = (L1PcInstance) cha;
							remove_skills(pc, new Integer[] { L1SkillId.HOLY_WEAPON });
							pc.addHitup(4);
							pc.addDmgup(2);
						}
						break;
						case REDUCE_WEIGHT:// 환술사,마법사 스킬같이 사용해라
						case DECREASE_WEIGHT: {// 마법사 마법
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addWeightReduction(180);
						}
						break;
						case COUNTER_MIRROR: {// 엘븐 그레비티
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addWeightReduction(300);
						}
						break;
						case HURRICANE: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setBraveSpeed(9);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 9, _getBuffIconDuration));
							Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 9, 0));
							pc.setAttackSpeed();
							break;
						}
						case FOCUS_WAVE: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setBraveSpeed(1);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 10, _getBuffIconDuration));
							Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 10, 0));
							pc.setAttackSpeed();
							break;
						}
						case SAND_STORM: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setBraveSpeed(1);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 1, _getBuffIconDuration));
							Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0));
							pc.setAttackSpeed();
							break;
						}
						case DANCING_BLADES: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setBraveSpeed(1);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 8, _getBuffIconDuration));
							Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 8, 0));
							pc.sendPackets(new S_SkillIconAura(154, _getBuffIconDuration));
							pc.setAttackSpeed();
						}
						break;
						case BURNING_WEAPON: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDmgup(6);
							pc.addHitup(6);
							pc.sendPackets(new S_SkillIconAura(162, _getBuffIconDuration));
						}
						break;
						case MIRROR_IMAGE: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDg(30);
						}
						break;
						case AQUA_SHOT: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addBowHitup(4);
						}
						break;
						case STORM_EYE: {
							L1PcInstance pc = (L1PcInstance) cha;
							L1Party party = pc.getParty();
							if (party != null) {
								if (pc.getParty().isMember(pc)) {
									if (pc.hasSkillEffect(L1SkillId.STORM_EYE)) {
										pc.removeSkillEffect(L1SkillId.STORM_EYE);
									}
									pc.send_effect(_skill.getCastGfx());
									pc.addBowHitup(2);
									pc.addBowDmgup(3);
									pc.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration));
									pc.setSkillEffect(STORM_EYE, _getBuffIconDuration * 1000);
								}
								break;
							} else {
								if (_player.hasSkillEffect(L1SkillId.STORM_EYE)) {
									_player.removeSkillEffect(L1SkillId.STORM_EYE);
								}
								_player.send_effect(_skill.getCastGfx());
								_player.addBowHitup(2);
								_player.addBowDmgup(3);
								_player.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration));
								_player.setSkillEffect(STORM_EYE, _getBuffIconDuration * 1000);
							}
						}
						break;
						case STORM_SHOT: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addBowDmgup(5);
							pc.addBowHitup(3);
							pc.sendPackets(new S_SkillIconAura(165, _getBuffIconDuration));
						}
						break;
						case BERSERKERS: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isWizard()) {
								pc.addDmgup(2);
								pc.addHitup(8);
							} else {
								pc.getAC().addAc(10);
								pc.addDmgup(2);
								pc.addHitup(8);
							}
							L1SkillUse.on_icons(pc, _skillId, _getBuffIconDuration);
						}
						break;
						case SCALES_WATER_DRAGON: {
							L1PcInstance pc = (L1PcInstance) cha;
							// on_icons(pc, _skillId, _getBuffIconDuration);
							int er = 0;
							if (pc.getLevel() >= 87){
								er = ((pc.getLevel() - 87) / 3) * 3 + 3;
								if (er >= 15){
									er = 15;
								}
							}
							pc.addEffectedER(er);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case SCALES_EARTH_DRAGON: {
							L1PcInstance pc = (L1PcInstance) cha;
							// on_icons(pc, _skillId, _getBuffIconDuration);
							int ac = 0;
							int mr = 0;
							if (pc.getLevel() >= 87){
								ac = ((pc.getLevel() - 87) / 3) * -1 - 1;
								mr = ((pc.getLevel() - 87 ) / 3) * 2 + 2;
								if (ac <= -5){
									ac = -5;
								}
								if (mr >= 10){
									mr = 10;
								}
							}
							pc.getAC().addAc(ac);
							pc.getResistance().addMr(mr);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case SCALES_RINDVIOR_DRAGON: {
							L1PcInstance pc = (L1PcInstance) cha;
							// on_icons(pc, _skillId, _getBuffIconDuration);
							int dg = 0;
							if (pc.getLevel()>87){
								dg = ((pc.getLevel() - 87) / 3 ) * 3 + 3;
								if (dg>=15){
									dg = 15;
								}
							}
							pc.addDg(dg);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case SCALES_FIRE_DRAGON: {
							L1PcInstance pc = (L1PcInstance) cha;
							// on_icons(pc, _skillId, _getBuffIconDuration);
							int a = 0;
							if (pc.getLevel()>=87 ){
								a = (pc.getLevel() - 87)/3;
								if (a >=5){
									a = 5;
								}
							}
							pc.addSpecialResistance(eKind.ABILITY, a);
							pc.addSpecialResistance(eKind.FEAR, a);
							pc.addSpecialPierce(eKind.DRAGON_SPELL, a);
							pc.addHitup(5);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case IllUSION_OGRE: {
							L1PcInstance pc = (L1PcInstance) cha;
							// on_icons(pc, _skillId, _getBuffIconDuration);
							pc.addDmgup(4);
							pc.addHitup(4);
						}
						break;
						/*
						 * case IllUSION_LICH: { L1PcInstance pc = (L1PcInstance) cha; // on_icons(pc, _skillId, _getBuffIconDuration); pc.getAbility().addSp(2); pc.sendPackets(new S_SPMR(pc)); } break; case IllUSION_DIAMONDGOLEM: { L1PcInstance pc = (L1PcInstance) cha; //
						 * on_icons(pc, _skillId, _getBuffIconDuration); pc.getAC().addAc(-8); pc.sendPackets(new S_OwnCharAttrDef(pc)); } break;
						 */
						case IllUSION_AVATAR: {
							L1PcInstance pc = (L1PcInstance) cha;
							// on_icons(pc, _skillId, _getBuffIconDuration);
							pc.addDmgup(10);
						}
						break;
						case INSIGHT: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedStr((byte) 1);
							pc.getAbility().addAddedDex((byte) 1);
							pc.getAbility().addAddedCon((byte) 1);
							pc.getAbility().addAddedInt((byte) 1);
							pc.getAbility().addAddedWis((byte) 1);
							pc.resetBaseMr();
						}
						break;
						case ADVANCE_SPIRIT: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.GIGANTIC) || pc.hasSkillEffect(L1SkillId.PRIDE)) {
								pc.removeSkillEffect(L1SkillId.GIGANTIC);
								pc.removeSkillEffect(L1SkillId.PRIDE);
							}

							if (pc.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
								if (pc.isInParty()) {
									// 파티 프로토
									pc.getParty().refreshPartyMemberStatus(pc);
								}
								// on_icons(pc, _skillId, _skill.getBuffDuration());
								pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
								pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
								break;
							}

							pc.setAdvenHp(pc.getBaseMaxHp() / 5);
							pc.setAdvenMp(pc.getBaseMaxMp() / 5);
							pc.addMaxHp(pc.getAdvenHp());
							pc.addMaxMp(pc.getAdvenMp());
							if (pc.isInParty()) {
								// 파티 프로토
								pc.getParty().refreshPartyMemberStatus(pc);
							}
							on_icons(pc, _skillId, _skill.getBuffDuration());
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						}
						break;
						case GIGANTIC: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
								pc.removeSkillEffect(L1SkillId.ADVANCE_SPIRIT);
							}

							if (pc.hasSkillEffect(L1SkillId.GIGANTIC)) {
								if (pc.isInParty()) {
									// 파티 프로토
									pc.getParty().refreshPartyMemberStatus(pc);
								}
								// on_icons(pc, _skillId, _skill.getBuffDuration());
								pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
								pc.setSkillEffect(L1SkillId.GIGANTIC, _skill.getBuffDuration() * 1000);
								break;
							}

							double percent = pc.getLevel() / 2;
							int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
							pc.setMagicBuffHp(addHp);
							pc.addMaxHp(pc.getMagicBuffHp());
							if (pc.isInParty()) {
								// 파티 프로토
								pc.getParty().refreshPartyMemberStatus(pc);
							}
							on_icons(pc, _skillId, _skill.getBuffDuration());
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.setSkillEffect(L1SkillId.GIGANTIC, _skill.getBuffDuration() * 1000);
						}
						break;
						case PRIDE: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
								pc.removeSkillEffect(L1SkillId.ADVANCE_SPIRIT);
							}

							if (pc.hasSkillEffect(L1SkillId.PRIDE)) {
								if (pc.isInParty()) {
									// 파티 프로토
									pc.getParty().refreshPartyMemberStatus(pc);
								}
								// on_icons(pc, _skillId, _skill.getBuffDuration());
								pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
								pc.setSkillEffect(L1SkillId.PRIDE, _skill.getBuffDuration() * 1000);
								break;
							}
							double percent = pc.getLevel() / 4;
							int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
							pc.setMagicBuffHp(addHp);
							pc.addMaxHp(pc.getMagicBuffHp());
							if (pc.isInParty()) {
								// 파티 프로토
								pc.getParty().refreshPartyMemberStatus(pc);
							}
							on_icons(pc, _skillId, _skill.getBuffDuration());
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.setSkillEffect(L1SkillId.PRIDE, _skill.getBuffDuration() * 1000);
						}
						break;
						case POWERRIP: {
							int[] PowerRipTimeArray = Config.MagicAdSetting_Warrior.POWERRIP_MS;
							int rnd = random.nextInt(PowerRipTimeArray.length);
							_PowerRipDuration = PowerRipTimeArray[rnd];
							L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(9415, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(9415, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);
//						SC_WORLD_PUT_OBJECT_NOTI.make_stream(_target, 12533, true);
							if (DCCD != 0) {
								_PowerRipDuration -= DCCD;
							}
							if (ICCD != 0) {
								_PowerRipDuration += ICCD;
							}
							if (_PowerRipDuration <= 0) {
								_PowerRipDuration = 100;
							}
							if (_PowerRipDuration <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							if (_target instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) _target;
								pc.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
							} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) _target;
								npc.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
								npc.set발묶임상태(true);
							}
						}
						break;
						case DESPERADO: {
							int targetLevel = 0;
							int diffLevel = 0;
							int Magicdmg = SkillsTable.getInstance().getTemplate(L1SkillId.DESPERADO).getDamageValue();
							int TicTime = 1000;

							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								targetLevel = pc.getLevel();
								int h = MJCommons.calcheading(_user.getX(), _user.getY(), pc.getX(), pc.getY());
								_user.setHeading(h);
							} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								targetLevel = npc.getLevel();
								int h = MJCommons.calcheading(_user.getX(), _user.getY(), npc.getX(), npc.getY());
								_user.setHeading(h);
							}

							diffLevel = _user.getLevel() - targetLevel;

							// int[] stunTimeArray = null;
							int effectNpcId = 9416;
							// 밑으로 갈수록 레벨 차이가 많이남 즉 시간초가 더 길어짐
							if (_player != null && _player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
								int[] stunTimeArray = Config.MagicAdSetting_Warrior.DESPERADO_ABSOLUTE_MS;
								effectNpcId = 9418;
								int rnd = random.nextInt(stunTimeArray.length);
								_shockStunDuration = stunTimeArray[rnd];
							} else {
								int[] stunTimeArray = Config.MagicAdSetting_Warrior.DESPERADO_MS;
								int rnd = random.nextInt(stunTimeArray.length);
								_shockStunDuration = stunTimeArray[rnd];
							}
							// int rnd = random.nextInt(stunTimeArray.length);
							// _shockStunDuration = stunTimeArray[rnd];
							if (DCCD != 0) {
								_shockStunDuration -= DCCD;
							}
							if (ICCD != 0) {
								_shockStunDuration += ICCD;
							}
							if (_shockStunDuration <= 0) {
								_shockStunDuration = 100;
							}
							if (_shockStunDuration <= 0) {
								System.out.println(_skillId + " 持續時間錯誤");
								return ;
							}
							L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, DESPERADO, _target.getX(), _target.getY(), _target.getMapId(), cha);

							if (_target instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) _target;
								if (pc.hasSkillEffect(L1SkillId.DESPERADO))
									pc.removeSkillEffect(L1SkillId.DESPERADO);
								pc.Desperadolevel = _user.getLevel();
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, true));
								L1DemolitionDamage.doInfection(_user, _target, TicTime, Magicdmg);
								pc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
								on_icons(pc, L1SkillId.DESPERADO, _shockStunDuration);
							} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) _target;
								npc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
								L1DemolitionDamage.doInfection(_user, _target, TicTime, Magicdmg);
								npc.set발묶임상태(true);
							}
							dmg = _magic.calcMagicDamage(_skillId);
							// 시전시 상대방 보도록 해딩 변경
							_player.setHeading(_player.targetDirection(_targetX, _targetY));
							_player.sendPackets(new S_ChangeHeading(_player));
							_player.broadcastPacket(new S_ChangeHeading(_player));
						}
						break;
						case GREATER_HASTE: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getHasteItemEquipped() > 0) {
								continue;
							}
							if (pc.getMoveSpeed() != 2) {
								pc.setDrink(false);
								pc.setMoveSpeed(1);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration));
								pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
							} else {
								int skillNum = 0;
								if (pc.hasSkillEffect(SLOW)) {
									skillNum = SLOW;
								} else if (pc.hasSkillEffect(MOB_SLOW_1)) {
									skillNum = MOB_SLOW_1;
								} else if (pc.hasSkillEffect(MOB_SLOW_18)) {
									skillNum = MOB_SLOW_18;
								}
								if (skillNum != 0) {
									pc.removeSkillEffect(skillNum);
									pc.removeSkillEffect(GREATER_HASTE);
									pc.setMoveSpeed(0);
									continue;
								}
							}
						}
						break;
						case EAGGLE_EYE: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.add_missile_critical_rate(2);
						}
						break;
						case HOLY_WALK: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_player.isPassive(MJPassiveID.HOLY_WALK_EVOLUTION.toInt())) {
								pc.setBraveSpeed(3);
								pc.sendPackets(new S_SkillBrave(pc.getId(), 3, _getBuffIconDuration));
								pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, _getBuffIconDuration));
							} else {
								pc.getInventory().consumeItem(40318, 1);
								pc.setBraveSpeed(4);
								pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
								pc.broadcastPacket(new S_SkillBrave(pc.getId(), 4, 0));
							}
						}
						break;
						case MOVING_ACCELERATION: {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_player.isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt())) {
								pc.setBraveSpeed(3);
								pc.sendPackets(new S_SkillBrave(pc.getId(), 3, _getBuffIconDuration));
								pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, _getBuffIconDuration));
							} else {
								pc.setBraveSpeed(4);
								pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
								pc.broadcastPacket(new S_SkillBrave(pc.getId(), 4, 0));
							}
						}
						break;
						case BLOOD_LUST: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setBraveSpeed(1);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration));
							pc.broadcastPacket(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration));
						}
						break;
						case CALL_CLAN_ADVENCE: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) _user;
								L1PcInstance target = (L1PcInstance) cha;
								pc.call_clan_advence(target);
							}
						}
						break;
						case LIBERATION: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								on_icons(pc, LIBERATION, _getBuffIconDuration);
							}
							break;
						}
						case BUFF_CRAY: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.addHitup(5);
								pc.addDmgup(1);
								pc.addBowHitup(5);
								pc.addBowDmgup(1);
								pc.add_exp(30);
								pc.addMaxHp(100);
								pc.addMaxMp(50);
								pc.addHpr(3);
								pc.addMpr(3);
								pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
								pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
								pc.sendPackets(new S_SPMR(pc));
							}
						}
						break;
						case BUFF_Vala: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.addHitup(5);
								pc.addDmgup(1);
								pc.addBowHitup(5);
								pc.addBowDmgup(1);
								pc.add_exp(30);
								pc.addMaxHp(100);
								pc.addMaxMp(50);
								pc.addHpr(3);
								pc.addMpr(3);
								pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
								pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
								pc.sendPackets(new S_SPMR(pc));
							}
						}
						break;
						case COMA_A:
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.getAbility().addAddedCon(1);
								pc.getAbility().addAddedDex(5);
								pc.getAbility().addAddedStr(5);
								pc.addHitRate(3);
								pc.getAC().addAc(-3);
							}
							break;
						case COMA_B:
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.getAbility().addSp(1);
								pc.getAbility().addAddedCon(3);
								pc.getAbility().addAddedDex(5);
								pc.getAbility().addAddedStr(5);
								pc.addHitRate(5);
								pc.getAC().addAc(-8);
								pc.add_item_exp_bonus(20);
							}
							break;
						case FEATHER_BUFF_A: { // 운세버프 (매우 좋은)
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addHpr(3);
							pc.addMpr(3);
							pc.addDmgup(2);
							pc.addHitup(2);
							pc.addMaxHp(50);
							pc.addMaxMp(30);
							pc.getAbility().addSp(2);
							if (pc.isInParty()) {
								// 파티 프로토
								pc.getParty().refreshPartyMemberStatus(pc);
							}
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						}
						break;
						case FEATHER_BUFF_B: { // 운세버프 (좋은)
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addHitup(2);
							// pc.addSp(1);
							pc.getAbility().addSp(1);
							pc.addMaxHp(50);
							pc.addMaxMp(30);
							if (pc.isInParty()) {
								// 파티 프로토
								pc.getParty().refreshPartyMemberStatus(pc);
							}
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						}
						break;
						case FEATHER_BUFF_C: { // 운세버프 (보통)
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addMaxHp(50);
							pc.addMaxMp(30);
							pc.getAC().addAc(-2);
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							if (pc.isInParty()) {
								// 파티 프로토
								pc.getParty().refreshPartyMemberStatus(pc);
							}
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						}
						break;
						case FEATHER_BUFF_D: { // 운세버프 (나쁜)
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAC().addAc(-1);
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
						break;
						case 정상의가호: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(8);
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
						break;
						case ANTA_MAAN: {// 지룡의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case FAFU_MAAN: {// 수룡의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpecialResistance(eKind.SPIRIT, 5);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case LIND_MAAN: {// 풍룡의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.add_magic_critical_rate(2);
							pc.addSpecialResistance(eKind.FEAR, 5);
							pc.addEffectedER(10);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case VALA_MAAN: {// 화룡의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDmgup(2);
							pc.addBowDmgup(2);
							pc.addSpecialResistance(eKind.ABILITY, 5);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case BIRTH_MAAN: {// 탄생의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpecialResistance(eKind.SPIRIT, 5);
							pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case SHAPE_MAAN: {// 형상의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.add_magic_critical_rate(1);
							pc.addEffectedER(10);
							pc.addSpecialResistance(eKind.SPIRIT, 5);
							pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
							pc.addSpecialResistance(eKind.FEAR, 5);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
//						sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, getTotalER()));
						}
						break;
						case LIFE_MAAN: {// 생명의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.add_magic_critical_rate(1);
							pc.addDmgup(2);
							pc.addEffectedER(10);
							pc.addSpecialResistance(eKind.ALL, 5);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
						break;
						case BLACK_DRAGON_MAAN: {// 흑룡의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(5);
							// pc.send_effect(19076, true);
							pc.sendPackets(new S_SPMR(pc));
						}
						break;
						case NAVER_BLACK_DRAGON_MAAN: {// 절대의 마안
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(5);
							pc.add_magic_critical_rate(1);
							pc.addDmgup(2);
							pc.addBowDmgup(2);
							pc.addSpecialResistance(eKind.ALL, 5);
							// pc.send_effect(19078, true);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
							pc.sendPackets(new S_SPMR(pc));
						}
						break;
						case SIDE_OF_ME_BLESSING: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(5);
							pc.addDmgup(5);
							pc.addBowDmgup(5);
						}
						break;
						case RE_START_BLESSING: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(3);
							pc.addDmgup(3);
							pc.addBowDmgup(3);
						}
						break;
						case NEW_START_BLESSING: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(2);
							pc.addDmgup(2);
							pc.addBowDmgup(2);
						}
						break;
						case LIFE_BLESSING: {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(1);
							pc.addDmgup(1);
							pc.addBowDmgup(1);
						}
						break;
						/*
						 * case EXP_POTION: { L1PcInstance pc = (L1PcInstance) cha; on_icons(pc, _skillId, _getBuffIconDuration); } break;
						 */
						case ANTA_BUFF: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(L1SkillId.ANTA_BUFF))
									pc.removeSkillEffect(L1SkillId.ANTA_BUFF);
								pc.getAC().addAc(-2);
								pc.getResistance().addWater(50);
								pc.sendPackets(new S_OwnCharStatus(pc));
								pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, _getBuffIconDuration / 60));
							}
						}
						break;
						case FAFU_BUFF: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(L1SkillId.FAFU_BUFF))
									pc.removeSkillEffect(L1SkillId.FAFU_BUFF);
								pc.addHpr(3);
								pc.addMpr(1);
								pc.getResistance().addWind(50);
								pc.sendPackets(new S_OwnCharStatus(pc));
								pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, _getBuffIconDuration / 60));
							}
						}
						break;
						case RIND_BUFF: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(L1SkillId.RIND_BUFF))
									pc.removeSkillEffect(L1SkillId.RIND_BUFF);
								pc.addHitup(3);
								pc.addBowHitup(3);
								pc.getResistance().addFire(50);
								pc.sendPackets(new S_OwnCharStatus(pc));
								pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, _getBuffIconDuration / 60));
							}
						}
						break;
						// 린드비오르
						case RINDVIOR_SUMMON_MONSTER_CLOUD: {
							L1SpawnUtil.spawn(_npc, 5110, 10); // 구름대정령
						}
						break;
						case RINDVIOR_PREDICATE: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (_npc.getLocation().getTileLineDistance(pc.getLocation()) > 4) {
									L1Location newLoc = null;
									for (int count = 0; count < 10; count++) {
										newLoc = _npc.getLocation().randomLocation(3, 4, false);
										if (_npc.glanceCheck(newLoc.getX(), newLoc.getY()) == true) {
											pc.start_teleport(newLoc.getX(), newLoc.getY(), _npc.getMapId(), 5, 18339, true, true);
											break;
										}
									}
								}
							}
						}
						break;
						case RINDVIOR_SUMMON_MONSTER: {
							Random _random = new Random();
							int[] MobId = new int[] { 5106, 5107, 5108, 5109 }; // 광물
							// 골렘
							int rnd = _random.nextInt(100);
							for (int i = 0; i < _random.nextInt(2) + 1; i++) {
								L1SpawnUtil.spawn(_npc, MobId[rnd % MobId.length], _random.nextInt(3) + 8);
							}
						}
						break;
						case RINDVIOR_SILENCE: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead()) {
									continue;
								}
								pc.setSkillEffect(L1SkillId.SILENCE, 12 * 1000);
								pc.sendPackets(new S_SkillSound(pc.getId(), 2177));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2177));
							}
						}
						break;
						case RINDVIOR_BOW: {
							if (_target instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) _target;
								if (pc.isDead()) {
									continue;
								}
								int SprNum = 0;
								int pcX = pc.getX();
								int pcY = pc.getY();
								int npcId = _npc.getNpcTemplate().get_npcId();
								switch (npcId) {
									case 5097:
										pcY -= 6;
										SprNum = 7987;
										break;
									case 5098:
										pcX += 4;
										pcY -= 4;
										SprNum = 8050;
										break;
									case 5099:
										pcX += 5;
										SprNum = 8051;
										break;
									default:
										break;
								}
								S_EffectLocation packet = new S_EffectLocation(pcX, pcY, SprNum);
								pc.sendPackets(packet);
								Broadcaster.broadcastPacket(pc, packet);
							}
						}
						break;
						case RINDVIOR_WIND_SHACKLE:
						case RINDVIOR_WIND_SHACKLE_1: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead()) {
									continue;
								}
								pc.setSkillEffect(L1SkillId.WIND_SHACKLE, 12 * 1000);
								if (pc.getCurrentWeapon() == 88) {

								} else {
									pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
								}
								pc.sendPackets(new S_SkillSound(pc.getId(), 1799));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 1799));
							}
						}
						break;
						case RINDVIOR_PREDICATE_CANCELLATION: {
							Random random = new Random();
							int Chance = random.nextInt(100) + 1;
							if (Chance > 33) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (_npc.getLocation().getTileLineDistance(pc.getLocation()) > 4) {
										L1Location newLoc = null;
										for (int count = 0; count < 10; count++) {
											newLoc = _npc.getLocation().randomLocation(3, 4, false);
											if (_npc.glanceCheck(newLoc.getX(), newLoc.getY()) == true) {
												// L1Teleport.teleport(pc,
												// newLoc.getX(), newLoc.getY(),
												// _npc.getMapId(), 5, true);
												pc.start_teleport(newLoc.getX(), newLoc.getY(), _npc.getMapId(), 5, 18339, true, true);
												break;
											}
										}
									}
									if (cha instanceof L1PcInstance) {
										((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
									}
									cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

									for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										if (skillNum == SHAPE_CHANGE) {
											if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
												continue;
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0){
												continue;
											}
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0){
												continue;
											}
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
										if (skillNum == STATUS_CHAT_PROHIBITED) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									pc.curePoison();
									pc.cureParalaysis();
									if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
										L1PolyMorph.undoPoly(pc);
										pc.sendPackets(new S_CharVisualUpdate(pc));
										Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
									}
									if (pc.getHasteItemEquipped() > 0) {
										pc.setMoveSpeed(0);
										pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
										Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
									}
									if (pc != null && pc.isInvisble()) {
										if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
											pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
											pc.sendPackets(new S_Sound(147));
										}
										if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
											pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										}
									}
									pc.removeSkillEffect(STATUS_FREEZE);
									getTreePotionBuff(pc);
								}
							}
						}
						break;
						case RINDVIOR_CANCELLATION: {
							Random random = new Random();
							int Chance = random.nextInt(100) + 1;
							if (Chance > 33) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (cha instanceof L1PcInstance) {
										((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
									}
									cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));
									for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										if (skillNum == SHAPE_CHANGE) {
											if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
												continue;
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0){
												continue;
											}
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0){
												continue;
											}
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
										if (skillNum == STATUS_CHAT_PROHIBITED) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									pc.curePoison();
									pc.cureParalaysis();
									if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
										L1PolyMorph.undoPoly(pc);
										pc.sendPackets(new S_CharVisualUpdate(pc));
										Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
									}
									if (pc.getHasteItemEquipped() > 0) {
										pc.setMoveSpeed(0);
										pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
										Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
									}
									if (pc != null && pc.isInvisble()) {
										if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
											pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
											pc.sendPackets(new S_Sound(147));
										}
										if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
											pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										}
									}
									pc.removeSkillEffect(STATUS_FREEZE);
									getTreePotionBuff(pc);
								}
							}
						}
						break;
						case RINDVIOR_WEAPON:
						case RINDVIOR_WEAPON_2: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								L1ItemInstance weapon = pc.getWeapon();
								Random random = new Random();
								int rnd = random.nextInt(100) + 1;
								if (weapon != null && rnd > 33) {
									int weaponDamage = random.nextInt(3) + 1;
									if (pc.isDead()) {
										continue;
									}
									pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
									pc.getInventory().receiveDamage(weapon, weaponDamage);
									pc.sendPackets(new S_SkillSound(pc.getId(), 172));
									Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 172));
								}
							}
						}
						break;
						// 흑장로 데스 힐 / 캔슬레이션
						case BLACKELDER_DEATH_HELL: {
							Random random = new Random();
							int Chance = random.nextInt(100) + 1;
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 7780));
								pc.setSkillEffect(L1SkillId.PAP_DEATH_HELL, 12 * 1000);
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7780));
							}
							if (Chance > 33) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (cha instanceof L1PcInstance) {
										((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), _skill.getCastGfx()));
									}
									cha.broadcastPacket(new S_SkillSound(cha.getId(), _skill.getCastGfx()));
									for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										if (skillNum == SHAPE_CHANGE) {
											if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
												continue;
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0){
												continue;
											}
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0){
												continue;
											}

										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									pc.curePoison();
									pc.cureParalaysis();
									if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
										L1PolyMorph.undoPoly(pc);
										pc.sendPackets(new S_CharVisualUpdate(pc));
										Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
									}
									if (pc.getHasteItemEquipped() > 0) {
										pc.setMoveSpeed(0);
										pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
										Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
									}
									if (pc != null && pc.isInvisble()) {
										if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
											pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
											pc.sendPackets(new S_Sound(147));
										}
										if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
											pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										}
									}
									pc.removeSkillEffect(STATUS_FREEZE);
									pc.sendPackets(new S_SkillSound(pc.getId(), 870));
									Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 870));
								}
							}
						}
						break;
						// 드레이크 매스텔레포트
						case DRAKE_MASSTELEPORT: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								L1Location newLocation = pc.getLocation().randomLocation(5, true);
								int newX = newLocation.getX();
								int newY = newLocation.getY();
								if (pc.isDead())
									continue;
								pc.start_teleport(newX, newY, pc.getMapId(), pc.getHeading(), 18339, true, true);
							}
						}
						break;
						// 드레이크 윈드세클
						case DRAKE_WIND_SHACKLE: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead()) {
									continue;
								}
								pc.setSkillEffect(L1SkillId.WIND_SHACKLE, 12 * 1000);
								if (pc.getCurrentWeapon() == 88) {
								} else {
									pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
								}
								pc.sendPackets(new S_SkillSound(pc.getId(), 1799));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 1799));
							}
						}
						break;
						case MOB_DEATH_POTION: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 7781));
								pc.setSkillEffect(L1SkillId.MOB_DEATH_POTION, 12 * 1000);
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7781));
							}
						}
						break;
						case BLACKELDER_DEATH_POTION: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 7781));
								pc.setSkillEffect(L1SkillId.PAP_DEATH_PORTION, 12 * 1000);
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7781));
							}
						}
						break;
						// 이프리트 서먼 몬스터
						case EFRETE_SUMMON_MONSTER: {
							Random _random = new Random();
							for (int i = 0; i < 2; i++) {
								L1SpawnUtil.spawn(_npc, 5121, _random.nextInt(3) + 8);
							}
						}
						break;
						// 피닉스 서먼 몬스터
						case PHOENIX_SUMMON_MONSTER: {
							Random _random = new Random();
							for (int i = 0; i < 2; i++) {
								L1SpawnUtil.spawn(_npc, 900177, _random.nextInt(3) + 8);
							}
						}
						break;
						// 피닉스 캔슬레이션
						case PHOENIX_CANCELLATION: {
							Random random = new Random();
							int Chance = random.nextInt(100) + 1;
							if (Chance > 33) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (cha instanceof L1PcInstance) {
										((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
									}
									cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));
									for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										if (skillNum == SHAPE_CHANGE) {
											if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
												continue;
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0){
												continue;
											}
											if (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0){
												continue;
											}
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
										if (skillNum == STATUS_CHAT_PROHIBITED) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
										if (isNotCancelable(skillNum) && !pc.isDead()) {
											continue;
										}
										pc.removeSkillEffect(skillNum);
									}
									pc.curePoison();
									pc.cureParalaysis();
									if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
										L1PolyMorph.undoPoly(pc);
										pc.sendPackets(new S_CharVisualUpdate(pc));
										Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
									}
									if (pc.getHasteItemEquipped() > 0) {
										pc.setMoveSpeed(0);
										pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
										Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
									}
									if (pc != null && pc.isInvisble()) {
										if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
											pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
											pc.sendPackets(new S_Sound(147));
										}
										if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
											pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
											pc.sendPackets(new S_Invis(pc.getId(), 0));
											Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										}
									}
									pc.removeSkillEffect(STATUS_FREEZE);
									getTreePotionBuff(pc);
								}
							}
						}
						break;
						case AREA_OF_SILENCE: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.hasSkillEffect(L1SkillId.AREA_OF_SILENCE)) {
									pc.removeSkillEffect(L1SkillId.AREA_OF_SILENCE);
								}
								pc.send_effect(10708, true);
								// pc.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, pc, 6, _skill.getBuffDuration()));
							}
						}
						break;
						case DESERT_SKILL1: { // 광역 커스 패럴라이즈
							if (!cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE) && !cha.hasSkillEffect(DESERT_SKILL1) && !cha.hasSkillEffect(DESERT_SKILL2)) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (pc.isDead())
										continue;
									L1CurseParalysis.curse(pc, _skillId);
								}
							}
						}
						break;
						case DESERT_SKILL2: { // 광역 어스 바인드
							if (!cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE) && !cha.hasSkillEffect(DESERT_SKILL1) && !cha.hasSkillEffect(DESERT_SKILL2)) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									if (pc.isDead())
										continue;

									pc.setSkillEffect(EARTH_BIND, 12 * 1000); // 디케이포션
									pc.sendPackets(new S_Poison(pc.getId(), 2));
									pc.broadcastPacket(new S_Poison(pc.getId(), 2));
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));

									pc.sendPackets(new S_SkillSound(pc.getId(), 2251));
									Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2251));
								}
							}
						}
						break;
						case DESERT_SKILL3: { // 광역 마나 드레인
							int ranMp = random.nextInt(20);
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.getCurrentMp() <= ranMp || pc.isDead())
									continue;
								pc.setCurrentMp(pc.getCurrentMp() - ranMp);
								pc.sendPackets(new S_SkillSound(pc.getId(), 2172));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2172));
							}
						}
						break;
						case doctor_skills:
						case DESERT_SKILL4: {
							Random random = new Random();
							int PoisonTime = random.nextInt(5) + 1;
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (PoisonTime > 2)
									L1DamagePoison.doInfection(_user, pc, PoisonTime * 1000, 500, _skillId == TOMAHAWK);
							}
						}
						break;
						case DRAGON_HALPAS_POISON:
						case ZENITH_Poison: {
							Random random = new Random();
							int PoisonTime = random.nextInt(5) + 1;
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (PoisonTime > 2)
									L1DamagePoison.doInfection(_user, pc, PoisonTime * 1000, 500, _skillId == TOMAHAWK);
							}
						}
						break;
						case DESERT_SKILL5: { // 커스/디케이/다크니스/디지즈/위크니스
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead() || pc.hasSkillEffect(CURSE_PARALYZE)) {
									continue;
								}
								L1CurseParalysis.curse(cha, _skillId); // 커스 패럴라이즈
								pc.setSkillEffect(CURSE_PARALYZE, 4 * 1000); // 커스
								pc.sendPackets(new S_SkillSound(pc.getId(), 10704));
								Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 10704));
							}
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead() || pc.hasSkillEffect(DECAY_POTION)) {
									continue;
								}
								pc.setSkillEffect(DECAY_POTION, 16 * 1000); // 디케이
								pc.sendPackets(new S_SkillSound(pc.getId(), 2232));
								Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2232));
							}
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead() || pc.hasSkillEffect(DARKNESS)) {
									continue;
								}
								if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
									pc.sendPackets(new S_CurseBlind(2));
								} else {
									pc.sendPackets(new S_CurseBlind(1));
								}
								pc.setSkillEffect(DARKNESS, 32 * 1000); // 다크니스
								pc.sendPackets(new S_SkillSound(pc.getId(), 2175));
								Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2175));
							}
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead() || pc.hasSkillEffect(DISEASE)) {
									continue;
								}
								pc.addDmgup(-6);
								pc.getAC().addAc(12);
								pc.setSkillEffect(DISEASE, 64 * 1000); // 디지즈
								pc.sendPackets(new S_SkillSound(pc.getId(), 2230));
								Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2230));
							}
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead() || pc.hasSkillEffect(WEAKNESS)) {
									continue;
								}
								pc.addDmgup(-5);
								pc.addHitup(-1);
								pc.setSkillEffect(WEAKNESS, 64 * 1000); // 위크니스
								pc.sendPackets(new S_SkillSound(pc.getId(), 2228));
								Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2228));
							}
						}
						break;
						case DECAY_POTION:
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								on_icons(pc, _skillId, _getBuffIconDuration);
							}
							break;
						case DESERT_SKILL6: { // 광역 다크니스
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead() || pc.hasSkillEffect(DARKNESS)) {
									continue;
								}
								if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
									pc.sendPackets(new S_CurseBlind(2));
								} else {
									pc.sendPackets(new S_CurseBlind(1));
								}
								pc.setSkillEffect(DARKNESS, 32 * 1000); // 다크니스
								pc.sendPackets(new S_SkillSound(pc.getId(), 2175));
								Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2175));
							}
						}
						break;
						case DESERT_SKILL7: { // 광역 포그
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead() || pc.hasSkillEffect(FOG_OF_SLEEPING)) {
									continue;
								}
								pc.setSkillEffect(FOG_OF_SLEEPING, 32 * 1000);
								// L1SkillUse.on_icons(pc, L1SkillId.FOG_OF_SLEEPING, 32 * 1000);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
								pc.sendPackets(new S_SkillSound(pc.getId(), 760));
								Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 760));
							}
							cha.setSleeped(true);
						}
						break;
						case DESERT_SKILL8: { // 에르자베 토네이도 대미지
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 10082));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 10082));
							}
						}
						break;
						case DESERT_SKILL9: { // 에르자베 서먼 몬스터
							for (int i = 0; i < 4; i++) {
								L1SpawnUtil.spawn(_npc, 5138, 6, 120 * 1000); // 그라카스
								L1SpawnUtil.spawn(_npc, 5139, 6, 120 * 1000); // 베이카스
								L1SpawnUtil.spawn(_npc, 5140, 6, 120 * 1000); // 호루카스
								L1SpawnUtil.spawn(_npc, 5141, 6, 120 * 1000); // 아르카스
								L1SpawnUtil.spawn(_npc, 5142, 6, 120 * 1000); // 여왕
								L1SpawnUtil.spawn(_npc, 5143, 6, 120 * 1000); // 여왕
								L1SpawnUtil.spawn(_npc, 5144, 6, 120 * 1000); // 여왕
								L1SpawnUtil.spawn(_npc, 5145, 6, 120 * 1000); // 여왕
							}
						}
						break;
						case DESERT_SKILL10: { // 에르자베 모래 폭풍
							for (int i = 0; i < random.nextInt(3) + 1; i++) {
								L1SpawnUtil.spawn(_npc, 5095, 6, 3 * 1000); // 모래 폭풍
							}
						}
						break;
						case DESERT_SKILL11: { // 에르자베 토네이도 리뉴얼
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.send_effect(12256);
							}
						}
						break;
						case TEMPEST: {
							if (cha.isDead()) {
								continue;
							}
							int effectNpcId = 8502105;
//						int effect_id = 18416;
							int TicTime = 1000;
							long weapondmg;
							int weapondmgint = 0;
							int TEMPdmg = 0;

							int max_hp = cha.getMaxHp() / 10;
							if (max_hp < 100) {
								max_hp = 100;
							}
							if (_player.getWeapon() != null) {
								L1ItemInstance weapon = _player.getWeapon();
								int Pdmg = 0;
								int enchant = weapon.getEnchantLevel();
								if (C_ItemUSe.is_legend_weapon(weapon.getItemId())) {
									Pdmg = enchant * 2;
								} else if(C_ItemUSe.is_ancient_weapon(weapon.getItemId())) {
									Pdmg = enchant * 4;
								} else {
									if (enchant >= 1 && enchant <= 9)
										Pdmg = enchant;
									else if (enchant > 9)
										Pdmg = 9 + ((enchant - 9) * 2);
								}

								weapondmg = Math.round((weapon.getItem().getDmgLarge() + Pdmg) + weapon.getItem().getDmgModifier()) * Config.MagicAdSetting_Warrior.TEMPEST_DAMAGE_RATE / 100;
								weapondmgint = Long.valueOf(Optional.ofNullable(weapondmg).orElse(0L)).intValue();
							}
							TEMPdmg = MJRnd.next(50, max_hp) + weapondmgint;

							cha.send_effect(18414);

							int[] stunTimeArray = Config.MagicAdSetting_Warrior.TEMPEST_SKILL_RND_TIME;
							int rnd = random.nextInt(stunTimeArray.length);
							_shockStunDuration = stunTimeArray[rnd];

							int Magicdmg = _magic.calcMagicDamage(_skillId);


							if (_magic.calcProbabilityMagic(_skillId)) {
								if (cha.hasSkillEffect(L1SkillId.TEMPEST)) {
									cha.removeSkillEffect(L1SkillId.TEMPEST);
								}

								if (_player.getWeapon() != null) {
									L1ItemInstance weapon = _player.getWeapon();
									if (weapon.getItemId() == 203065) {
										_shockStunDuration += 1000;
									}
								}
								if (_shockStunDuration <= 0) {
									System.out.println(_skillId + " 持續時間錯誤");
									return ;
								}
								int currentHP = 0;
								int finalHp = 0;
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
//								L1EffectSpawn.getInstance().spawnEffect(effectNpcId, _shockStunDuration, pc.getX(), pc.getY(), pc.getMapId());
									L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//								SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc, effect_id, true);
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
									pc.setSkillEffect(L1SkillId.TEMPEST, _shockStunDuration);
									L1DemolitionDamage.doInfection(_user, pc, TicTime, Magicdmg);
									on_icons(pc, L1SkillId.TEMPEST, _shockStunDuration / 1000);
//								currentHP = pc.getCurrentHp();
//								pc.setCurrentHp(currentHP - TEMPdmg);
									pc.receiveDamage(_player, TEMPdmg);

								} else if  (cha instanceof L1NpcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
									L1NpcInstance npc = (L1NpcInstance) cha;
									L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//								L1EffectSpawn.getInstance().spawnEffect(effectNpcId, _shockStunDuration, npc.getX(), npc.getY(), npc.getMapId());
//								SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc, effect_id, true);
									npc.setSkillEffect(L1SkillId.TEMPEST, _shockStunDuration);
									L1DemolitionDamage.doInfection(_user, _target, TicTime, Magicdmg);
									currentHP = npc.getCurrentHp();
//								npc.setCurrentHp(currentHP - TEMPdmg);
									npc.receiveDamage(_player, TEMPdmg);
									npc.setParalyzed(true);

								}
							}
						}
						break;
						case MOB_SKILL_NEW: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.send_effect(18677);
							}
						}
						break;
						case MOB_SKILL_EGG: {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.send_effect(4167);
							}
						}
						break;
						case BOSS_COUNTER_BARRIER: { // 보스 카운터배리어
							if (cha != null && cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.send_effect(10709);
							}
						}
						case DETECTION:
						case IZE_BREAK:
						case EYE_OF_DRAGON:
						case COUNTER_DETECTION:
							if (cha instanceof L1PcInstance){
								L1PcInstance pc = (L1PcInstance)cha;
								if (pc.hasSkillEffect(BLIND_HIDING)){
//								System.out.println("하이딩");
									detection(pc, true);
								} else if (pc.hasSkillEffect(INVISIBILITY)) {
//								System.out.println("인비지");
									detection(pc, true);
								}
							}
							break;
						default:
							break;
					}
				}

				if (_calcType == PC_NPC || _calcType == NPC_NPC) {
					if (_skillId == TAMING_MONSTER && ((L1MonsterInstance) cha).getNpcTemplate().isTaming()) {
						int petcost = 0;
						Object[] petlist = _user.getPetList().values().toArray();
						for (Object pet : petlist) {
							petcost += ((L1NpcInstance) pet).getPetcost();
						}
						int charisma = _user.getAbility().getTotalCha();
						if (_player.isElf()) {
							charisma += 12;
						} else if (_player.isWizard()) {
							charisma += 6;
						}
						charisma -= petcost;
						if (charisma >= 6) {
							L1SummonInstance summon = new L1SummonInstance(_targetNpc, _user, false);
							_target = summon;
						} else {
							_player.sendPackets(new S_ServerMessage(319));
						}
					} else if (_skillId == CREATE_ZOMBIE) {
						int petcost = 0;
						Object[] petlist = _user.getPetList().values().toArray();
						for (Object pet : petlist) {
							petcost += ((L1NpcInstance) pet).getPetcost();
						}
						int charisma = _user.getAbility().getTotalCha();
						if (_player.isElf()) {
							charisma += 12;
						} else if (_player.isWizard()) {
							charisma += 6;
						}
						charisma -= petcost;
						if (charisma >= 6) {
							L1SummonInstance summon = new L1SummonInstance(_targetNpc, _user, true);
							_target = summon;
						} else {
							_player.sendPackets(new S_ServerMessage(319));
						}
					} else if (_skillId == WEAK_ELEMENTAL) {
						if (cha instanceof L1MonsterInstance) {
							L1Npc npcTemp = ((L1MonsterInstance) cha).getNpcTemplate();
							int weakAttr = npcTemp.get_weakAttr();
							if ((weakAttr & 1) == 1) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2169));
							}
							if ((weakAttr & 2) == 2) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2167));
							}
							if ((weakAttr & 4) == 4) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2166));
							}
							if ((weakAttr & 8) == 8) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2168));
							}
						}
					} else if (_skillId == RETURN_TO_NATURE) {
						if (cha instanceof L1SummonInstance) {
							L1SummonInstance summon = (L1SummonInstance) cha;
							summon.broadcastPacket(new S_SkillSound(summon.getId(), 2245));
							summon.returnToNature();
						} else {
							if (_user instanceof L1PcInstance) {
								_player.sendPackets(new S_ServerMessage(79));
							}
						}
					} else if (_skillId == POWERRIP) {
						int[] PowerRipTimeArray = Config.MagicAdSetting_Warrior.POWERRIP_MS;
						int rnd = random.nextInt(PowerRipTimeArray.length);
						_PowerRipDuration = PowerRipTimeArray[rnd];
						L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect2(9415, _skillId, cha.getX(), cha.getY(), cha.getMapId(), cha);
//						L1EffectSpawn.getInstance().spawnEffect2(9415, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);
//						SC_WORLD_PUT_OBJECT_NOTI.make_stream(_target, 12533, true);
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							_target.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
						} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							npc.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
							npc.set발묶임상태(true);
						}
					} else if (_skillId == DESPERADO) {
						// int[] stunTimeArray = null;
						int effectNpcId = 9416;
						if (_player != null && _player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
							int[] stunTimeArray = Config.MagicAdSetting_Warrior.DESPERADO_ABSOLUTE_MS;
							effectNpcId = 9418;
							int rnd = random.nextInt(stunTimeArray.length);
							_shockStunDuration = stunTimeArray[rnd];
							// System.out.println("데앱: "+stunTimeArray[rnd]);
						} else {
							int[] stunTimeArray = Config.MagicAdSetting_Warrior.DESPERADO_MS;
							int rnd = random.nextInt(stunTimeArray.length);
							_shockStunDuration = stunTimeArray[rnd];
							// System.out.println("데: "+stunTimeArray[rnd]);
						}

						// int rnd = random.nextInt(stunTimeArray.length);
						// _shockStunDuration = stunTimeArray[rnd];
						L1EffectInstance effectInstance = L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _skillId, _target.getX(), _target.getY(), _target.getMapId(), cha);
						effectInstance.broadcastPacket(new S_MoveCharPacket(effectInstance));
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, true));
							_target.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
						} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							npc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
							npc.set발묶임상태(true);
						}
						dmg = _magic.calcMagicDamage(_skillId);
						// 시전시 상대방 보도록 해딩 변경
						_player.setHeading(_player.targetDirection(_targetX, _targetY));
						_player.sendPackets(new S_ChangeHeading(_player));
						_player.broadcastPacket(new S_ChangeHeading(_player));
					} /*else if (_skillId == DEMOLITION) {
						int targetLevel = 0;
						int diffLevel = 0;
						int Magicdmg = SkillsTable.getInstance().getTemplate(L1SkillId.DEMOLITION).getDamageValue();

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							int h = MJCommons.calcheading(_user.getX(), _user.getY(), pc.getX(), pc.getY());
							_user.setHeading(h);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							int h = MJCommons.calcheading(_user.getX(), _user.getY(), npc.getX(), npc.getY());
							_user.setHeading(h);
						}

						diffLevel = _user.getLevel() - targetLevel;

						// int[] stunTimeArray = null;
						int effectNpcId = 8502105;
						// 밑으로 갈수록 레벨 차이가 많이남 즉 시간초가 더 길어짐
						int[] stunTimeArray = Config.MagicAdSetting_Warrior.DEMOLITION_MS;
						// stunTimeArray = new int[] { 1000, 2000, 2000, 2000, 3000, 3000, 3000, 4000, 4000, 5000 };
						int rnd = random.nextInt(stunTimeArray.length);
						_shockStunDuration = stunTimeArray[rnd];
						L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId(), cha);

						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							if (pc.hasSkillEffect(L1SkillId.DEMOLITION))
								pc.removeSkillEffect(L1SkillId.DEMOLITION);
							pc.Desperadolevel = _user.getLevel();
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, true));
							_target.setSkillEffect(L1SkillId.DEMOLITION, _shockStunDuration);
							L1DemolitionDamage.doInfection(_user, _target, 1000, Magicdmg);
						} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							npc.setSkillEffect(L1SkillId.DEMOLITION, _shockStunDuration);
							npc.set발묶임상태(true);
							L1DemolitionDamage.doInfection(_user, _target, 1000, Magicdmg);
						}
						dmg = _magic.calcMagicDamage(_skillId);
						// 시전시 상대방 보도록 해딩 변경
						_player.setHeading(_player.targetDirection(_targetX, _targetY));
						_player.sendPackets(new S_ChangeHeading(_player));
						_player.broadcastPacket(new S_ChangeHeading(_player));
					}*/
				}

				if (_skill.getType() == L1Skills.TYPE_HEAL && _calcType == PC_NPC && undeadType == 1) {
					dmg *= -1;
				}

				if (_skill.getType() == L1Skills.TYPE_HEAL && _calcType == PC_NPC && undeadType == 3) {
					dmg = 0;
				}

				if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) && dmg < 0) {
					dmg = 0;
				}

				if (dmg != 0 || drainMana != 0) {
					if (_skillId == DISINTEGRATE) { //디스 최대 최소 대미지
						if (dmg > Config.MagicAdSetting_Wizard.DISINTMAXDMG)
							dmg = Config.MagicAdSetting_Wizard.DISINTMAXDMG;
						else if (dmg < Config.MagicAdSetting_Wizard.DISINTMINDMG)
							dmg = Config.MagicAdSetting_Wizard.DISINTMINDMG;
					}
					if (_skillId == ETERNITI) { //이터 최대 최소 대미지
						if (dmg > Config.MagicAdSetting_Wizard.ETERNITIMAXDMG) {
							dmg = Config.MagicAdSetting_Wizard.ETERNITIMAXDMG;
						} else if (dmg <Config.MagicAdSetting_Wizard.ETERNITIMINDMG) {
							dmg = Config.MagicAdSetting_Wizard.ETERNITIMINDMG;
						}
					}
					// XXX 디스중첩관련 (3초이내 해당 마법이 중첩으로 들어오면 대미지를 낮춰준다)
					if (_skillId == DISINTEGRATE || _skillId == ETERNITI && _target instanceof L1PcInstance) {
						if (_target.hasSkillEffect(ANTI_DISINTEGRATE)) {
							dmg /= Config.MagicAdSetting_Wizard.ANTIDISINTEGRATE;
//							System.out.println(dmg);
							// return;
						}

						_target.setSkillEffect(ANTI_DISINTEGRATE, Config.MagicAdSetting_Wizard.ANTIDISINTEGRATETIME);
					}

					// 포우는 무시하기
					if (_skillId != FOU_SLAYER)
						_magic.commit(dmg, drainMana);
				}

				if (heal > 0) {
					if ((heal + _user.getCurrentHp()) > _user.getMaxHp()) {
						_user.setCurrentHp(_user.getMaxHp());
					} else {
						_user.setCurrentHp(heal + _user.getCurrentHp());
					}
				}

				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_OwnCharStatus(pc));
					sendHappenMessage(pc);
				}

				addMagicList(cha, false);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(String.format("[技能錯誤(L1Skilluse)] : [角色名:%s] [NPCID:%s] [目標ID:%s] [技能ID:%d]", _player == null ? "角色名稱" : _player.getName(), _npc == null ? "" : _npc.getName(), _target == null ? "" : _target.getName(), _skillId));
		}
	}
	private void useCubeMagic(int skillid) {
		L1PcInstance pc = (L1PcInstance) _user;
		L1Party party = pc.getParty();

		if (pc.hasSkillEffect(skillid)) {
			pc.removeSkillEffect(skillid);
		}
		pc.send_effect(_skill.getCastGfx());
		pc.setSkillEffect(skillid, _skill.getBuffDuration() * 1000);
		if (party != null) {

			switch(skillid) {
				case L1SkillId.CUBE_AVATAR:
					on_icons(pc, skillid, _getBuffIconDuration);
					for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
						if (pc.getParty().isMember(player)) {
							if (player.hasSkillEffect(L1SkillId.CUBE_AVATAR)) {
								player.removeSkillEffect(L1SkillId.CUBE_AVATAR);
							}
							player._CubeEffect = true;
							player.send_effect(_skill.getCastGfx());
							player.setSkillEffect(L1SkillId.CUBE_AVATAR, _skill.getBuffDuration() * 1000);
							on_icons(player, L1SkillId.CUBE_AVATAR, _getBuffIconDuration);
						}
					}
					break;
				case L1SkillId.CUBE_RICH:
					pc.getAbility().addSp(2);
					for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
						if (pc.getParty().isMember(player)) {
//						if (!player.isPassive(MJPassiveID.IllUSION_LICH_PASSIVE.toInt())) {
							if (player.hasSkillEffect(L1SkillId.CUBE_RICH)) {
								player.removeSkillEffect(L1SkillId.CUBE_RICH);
							}
							player._CubeEffect = true;
							player.send_effect(_skill.getCastGfx());
							player.getAbility().addSp(2);
							on_icons(player, L1SkillId.CUBE_RICH, _getBuffIconDuration);
							player.setSkillEffect(L1SkillId.CUBE_RICH, _skill.getBuffDuration() * 1000);
						}
					}
					break;
				case L1SkillId.CUBE_OGRE:
					on_icons(pc, skillid, _getBuffIconDuration);

					pc.addDmgup(4);
					pc.addHitup(4);
					for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
						if (pc.getParty().isMember(player)) {
							if (player.hasSkillEffect(L1SkillId.CUBE_OGRE)) {
								player.removeSkillEffect(L1SkillId.CUBE_OGRE);
							}
							player._CubeEffect = true;
							player.addDmgup(4);
							player.addHitup(4);
							player.send_effect(_skill.getCastGfx());
							on_icons(player, L1SkillId.CUBE_OGRE, _getBuffIconDuration);
							player.setSkillEffect(L1SkillId.CUBE_OGRE, _skill.getBuffDuration() * 1000);
						}
					}
					break;

			}
		}
	}


	private void useShape_Change(L1Character cha){
		boolean isSameClan = false;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getClanid() != 0 && _player.getClanid() == pc.getClanid()) {
				isSameClan = true;
			}
			if (pc.getId() != _player.getId() && (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0 || pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0)) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 15846));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 15846));
				_player.sendPackets(new S_ServerMessage(280));
				return;
			}
		}
		if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mon = (L1MonsterInstance) cha;
			if (!mon.getNpcTemplate().isShapeChange())
				return;
		}

		if (_player.getId() != cha.getId() && !isSameClan) {
			int probability = 80;
			int rnd = random.nextInt(100) + 1;
			if (rnd > probability) {
				return;
			}
		}

		int pid = random.nextInt(Config.MagicAdSetting.POLYARRAY.length);
		int polyId = Config.MagicAdSetting.POLYARRAY[pid];
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getInventory().checkItem(4100500) || pc.getInventory().checkItem(4100610) ) {
				pc.ShapePolyRingMaster();
			} else if (pc.getInventory().checkEquipped(20281)) {
				pc.sendPackets(new S_Message_YN(180));
				// pc.sendPackets(new S_Message_YN(pc.getId(), 289, ""));
			} else {
				L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
				L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
				if (_player.getId() != pc.getId()) {
					pc.sendPackets(new S_ServerMessage(241, _player.getName()));
				}
			}
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mon = (L1MonsterInstance) cha;
			L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
			L1PolyMorph.doPoly(mon, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
		}
	}


	private void summonMonster(L1PcInstance pc, int order) {
		int summonid = 0;
		int level = 0;

		switch (order) {
			case 1:
				summonid = 120856;
				level = 86;
				break;
			case 2:
				summonid = 120857;
				level = 86;
				break;
			case 3:
				summonid = 120858;
				level = 86;
				break;
			case 4:
				summonid = 120861;
				level = 88;
				break;
			case 5:
				summonid = 120860;
				level = 88;
				break;
			case 6:
				summonid = 120859;
				level = 88;
				break;
			case 7:
				summonid = 120862;
				level = 90;
				break;
			case 8:
				summonid = 120864;
				level = 90;
				break;
			case 9:
				summonid = 120863;
				level = 90;
				break;
		}

		if (pc.getLevel() < level) {
			pc.sendPackets(new S_ServerMessage(743));
			return;
		}

		if((order >= 7 && order <= 9) && !pc.isPassive(MJPassiveID.SUMMONMONSER_GREATE.toInt())){
			pc.sendPackets(new S_ServerMessage(743));
			return;
		}

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
		if (npcTemp == null) {
			System.out.println("npcTemp = null " + summonid);
		}
		L1SummonInstance summon = null;
		summon = new L1SummonInstance(npcTemp, pc);
		summon.setPetcost(0);
	}

	/** 3단 가속 버프창 띄우기 */
	private void getTreePotionBuff(L1PcInstance target) {
		if (target.hasSkillEffect(STATUS_DRAGON_PEARL)) {
			int reminingtime = target.getSkillEffectTimeSec(STATUS_DRAGON_PEARL);
			target.sendPackets(new S_ServerMessage(1065, reminingtime / 1000));
			target.sendPackets(new S_Liquor(target.getId(), 8));
			target.setPearl(1);
		}
	}

	/**
	 * 캔슬레이션 마법으로 해당 버프를 캔슬 할 수없도록 한다.
	 **/
	public static boolean isNotCancelable(int skillNum) {
		return skillNum == ABSOLUTE_BARRIER || skillNum == ADVANCE_SPIRIT || skillNum == EMPIRE || skillNum == SHOCK_STUN || skillNum == CHAINSWORD_STUN || skillNum == REDUCTION_ARMOR
				|| skillNum == SOLID_CARRIAGE || skillNum == PRIDE || skillNum == BLOW_ATTACK || skillNum == DECIDING_BUFF || skillNum == HERO_GAHO_BUFF || skillNum == COUNTER_BARRIER
				|| skillNum == COMA_A || skillNum == COMA_B || skillNum == ANTA_MAAN || skillNum == FAFU_MAAN || skillNum == EXP_BUFF || skillNum == LIND_MAAN
				|| skillNum == VALA_MAAN || skillNum == BIRTH_MAAN || skillNum == SHAPE_MAAN || skillNum == LIFE_MAAN || skillNum == BLACK_DRAGON_MAAN || skillNum == NAVER_BLACK_DRAGON_MAAN
				|| skillNum == ANTA_BUFF || skillNum == FAFU_BUFF || skillNum == Moster_STUN || skillNum == fornos_STUN || skillNum == MOSTER_STUN_1 || skillNum == ANTA_MESSAGE_6
				|| skillNum == ANTA_MESSAGE_7 || skillNum == ANTA_MESSAGE_8 || skillNum == PREDICATEDELAY || skillNum == FEATHER_BUFF_A || skillNum == FEATHER_BUFF_B || skillNum == FEATHER_BUFF_C
				|| skillNum == FEATHER_BUFF_D || skillNum == PAP_DEATH_PORTION || skillNum == MOB_DEATH_POTION || skillNum == PAP_DEATH_HELL || skillNum == PAP_REDUCE_HELL || skillNum == STATUS_DRAGON_PEARL
				|| skillNum == UNCANNY_DODGE || skillNum == DRESS_EVASION || skillNum == SHADOW_ARMOR || skillNum == OMAN_STUN || skillNum == DRAGON_HALPAS_STUN || skillNum == BALOCH_STUN
				|| skillNum == BOS_STUN18 || skillNum == Maeno_STUN || skillNum == Besi_STUN || skillNum == SCALES_EARTH_DRAGON || skillNum == SCALES_WATER_DRAGON || skillNum == SCALES_FIRE_DRAGON
				|| skillNum == SCALES_RINDVIOR_DRAGON || skillNum == COOK_STR || skillNum == COOK_DEX || skillNum == COOK_INT || skillNum == ARMOR_BRAKE || skillNum == SHADOW_FANG
				|| skillNum == CLANBUFF_YES
				|| skillNum == God_buff
				|| skillNum == COOK_GROW
				|| skillNum == DESPERADO
				|| skillNum == MIRROR_IMAGE
				|| skillNum == TOP_RANKER
				|| skillNum == Blessing_of_the_Peak
				|| skillNum == GIGANTIC
				|| skillNum == Methis_Homemade_Soup
				|| skillNum == Methis_Homemade_Cuisine
				|| skillNum == Methis_Blessing_Scroll
				|| skillNum == POLY_RING_MASTER
				|| skillNum == POLY_RING_MASTER2
				|| skillNum == RANK_BUFF_1 || skillNum == RANK_BUFF_2 || skillNum == RANK_BUFF_3 || skillNum == RANK_BUFF_4
				|| skillNum == RANK_BUFF_5_STR || skillNum == RANK_BUFF_5_DEX || skillNum == RANK_BUFF_5_INT
				|| skillNum == RANK_BUFF_6_STR || skillNum == RANK_BUFF_6_DEX || skillNum == RANK_BUFF_6_INT
				|| skillNum == RANK_BUFF_7_STR || skillNum == RANK_BUFF_7_DEX || skillNum == RANK_BUFF_7_INT
				|| skillNum == RANK_BUFF_8_STR || skillNum == RANK_BUFF_8_DEX || skillNum == RANK_BUFF_8_INT
				|| skillNum == AURA || skillNum == FORCE_STUN || skillNum == ETERNITI || skillNum == DEMOLITION
				|| skillNum == FORCE_STUN_FAIL || skillNum == CLAN_EXP_BUFF_1ST || skillNum == CLAN_EXP_BUFF_2ND || skillNum == CLAN_PVP_BUFF_1ST || skillNum == CLAN_PVP_BUFF_2ND
				|| skillNum == CLAN_DEFENCE_BUFF_1ST || skillNum == CLAN_DEFENCE_BUFF_2ND
				|| skillNum == CLASS_RANK_BLESS_PRINCE_1 || skillNum == CLASS_RANK_BLESS_KNIGHT_1 || skillNum == CLASS_RANK_BLESS_ELF_1 || skillNum == CLASS_RANK_BLESS_WIZARD_1
				|| skillNum == CLASS_RANK_BLESS_DARKELF_1 || skillNum == CLASS_RANK_BLESS_DRAGONKNIGHT_1 || skillNum == CLASS_RANK_BLESS_BLACKWIZARD_1 || skillNum == CLASS_RANK_BLESS_WARRIOR_1
				|| skillNum == CLASS_RANK_BLESS_FENCER_1 || skillNum == DISINTEGRATE;
	}

	private void detection(L1PcInstance pc, boolean detectAll) {
		if (pc == null) {
			return;
		}
		if (pc.isGmInvis()){
			return;
		}
		if (pc.isGhost()){
			return;
		}
		if (pc.hasSkillEffect(INVISIBILITY)) {
			pc.delInvis();
		}
		if (pc.hasSkillEffect(BLIND_HIDING)){
			if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING_ASSASSIN)){
				return;
			} else {
				pc.delInvis();
			}
		}
		if (detectAll) {
			for (L1PcInstance tgt : L1World.getInstance().getVisiblePlayer(pc)) {
				if (tgt.isGm()){
					continue;
				}
				if (tgt.isGhost()){
					continue;
				}
				if (tgt.hasSkillEffect(INVISIBILITY)) {
					tgt.delInvis();
				}
				if (tgt.hasSkillEffect(BLIND_HIDING)){
					if (tgt.hasSkillEffect(L1SkillId.BLIND_HIDING_ASSASSIN)){
						continue;
					}
					else {
						tgt.delBlindHiding();
					}
				}
			}
			L1WorldTraps.getInstance().onDetection(pc);
		}
	}

	private boolean isTargetCalc(L1Character cha) {
		if (_skill.getTarget().equals("attack") && _skillId != 18) {
			if (isPcSummonPet(cha)) {
				if (_player.getZoneType() == 1 || cha.getZoneType() == 1 || _player.checkNonPvP(_player, cha)) {
					return false;
				}
			}
		}

		if (_skillId == FOG_OF_SLEEPING && _user.getId() == cha.getId()) {
			return false;
		}
		if (cha instanceof L1PetInstance) {
			L1PetInstance pet = (L1PetInstance) cha;
			if (_user.getId() == pet.getMaster().getId()) {
				return false;
			}
		}
		if (cha instanceof MJCompanionInstance) {
			if (_user.getId() == ((MJCompanionInstance) cha).get_master_id())
				return false;
		}

		if (_skillId == MASS_TELEPORT) {
			if (_user.getId() != cha.getId()) {
				return false;
			}
		}

		return true;
	}

	private boolean isPcSummonPet(L1Character cha) {
		if (_calcType == PC_PC) {
			return true;
		}

		if (_calcType == PC_NPC) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.isExsistMaster()) {
					return true;
				}
			}
			if (cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				return true;
			}
		}
		return false;
	}

	private boolean isUseCounterMagic(L1Character cha) {
		if (!_skill.isIgnoresCounterMagic()) {
			if (cha.hasSkillEffect(COUNTER_MAGIC)) {
				cha.removeSkillEffect(COUNTER_MAGIC);
				cha.broadcastPacket(new S_SkillSound(cha.getId(), 10702));
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillSound(pc.getId(), 10702));
				}
				return true;
			}
		}
		return false;
	}

	private boolean isTargetFailure(L1Character cha) {
		boolean isTU = false;
		boolean isErase = false;
		boolean isManaDrain = false;
		int undeadType = 0;

		if (cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) {
			return true;
		}

		if (cha instanceof L1PcInstance) {
			if (_calcType == PC_PC && _player.checkNonPvP(_player, cha)) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (_player.getId() == pc.getId() || (pc.getClanid() != 0 && _player.getClanid() == pc.getClanid())) {
					return false;
				}
				return true;
			}
			return false;
		}

		if (cha instanceof L1MonsterInstance) {
			isTU = ((L1MonsterInstance) cha).getNpcTemplate().get_IsTU();
		}

		if (cha instanceof L1MonsterInstance) {
			isErase = ((L1MonsterInstance) cha).getNpcTemplate().get_IsErase();
		}

		if (cha instanceof L1MonsterInstance) {
			undeadType = ((L1MonsterInstance) cha).getNpcTemplate().get_undead();
		}

		if (cha instanceof L1MonsterInstance) {
			isManaDrain = true;
		}
		if ((_skillId == TURN_UNDEAD && (undeadType == 0 || undeadType == 2)) || (_skillId == TURN_UNDEAD && isTU == false)
				|| ((_skillId == ERASE_MAGIC || _skillId == SLOW || _skillId == MOB_SLOW_1 || _skillId == MOB_SLOW_18 || _skillId == MANA_DRAIN || _skillId == WIND_SHACKLE) && isErase == false)
				|| (_skillId == MANA_DRAIN && isManaDrain == false)) {
			return true;
		}
		return false;
	}

	public void removeNewIcon(L1PcInstance pc, int skillid) {
		switch (skillid) {
			case DEATH_HEAL:
			case DEATH_HEAL_Mob:
			case IMPACT:
				pc.removeSkillEffect(skillid);
				break;
			default:
				break;
		}
	}

	/**
	 * TODO 온아이콘 패시브 전용 on_icons_passive(pc, MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE, -1);
	 **/
	public static void on_icons_passive(L1PcInstance pc, MJPassiveID passiveId, int duration) {
		SC_SPELL_BUFF_NOTI noti = null;
		switch (passiveId) {
			case DOUBLE_BREAK_DESTINY:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(8839);
				noti.set_off_icon_id(8839);
				noti.set_tooltip_str_id(821);
				noti.set_new_str_id(821);
				noti.set_end_str_id(829);
				noti.set_is_good(true);
				break;
			case COUNTER_BARRIER_MASTER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10545);
				noti.set_off_icon_id(10545);
				noti.set_tooltip_str_id(8511);
				noti.set_new_str_id(8511);
				noti.set_end_str_id(8505);
				noti.set_is_good(true);
				break;
			case PHANTOM_REQUEM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10501);
				noti.set_off_icon_id(10501);
				noti.set_tooltip_str_id(8460);
				noti.set_new_str_id(8460);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case ADVANCE_SPIRIT_PA:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1607);
				noti.set_off_icon_id(1607);
				noti.set_tooltip_str_id(982);
				noti.set_new_str_id(982);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case IMMUNETOHARM_SAINT:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9835);
				noti.set_off_icon_id(9835);
				noti.set_tooltip_str_id(966);
				noti.set_new_str_id(966);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case THUNDER_GRAP_BRAVE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9898);
				noti.set_off_icon_id(9898);
				noti.set_tooltip_str_id(7456);
				noti.set_new_str_id(7456);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case FOU_SLAYER_BRAVE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9898);
				noti.set_off_icon_id(9898);
				noti.set_tooltip_str_id(7456);
				noti.set_new_str_id(7456);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DARK_HORSE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9897);
				noti.set_off_icon_id(9897);
				noti.set_tooltip_str_id(7455);
				noti.set_new_str_id(7455);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case IllUSION_DIAMONDGOLEM_PASSIVE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3113);
				noti.set_off_icon_id(3113);
				noti.set_tooltip_str_id(1347);
				noti.set_new_str_id(1347);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case IllUSION_LICH_PASSIVE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3115);
				noti.set_off_icon_id(3115);
				noti.set_tooltip_str_id(1343);
				noti.set_new_str_id(1343);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			default:
				break;
		}

		if (noti != null) {
			noti.set_spell_id(revisionPassiveId(passiveId));
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}

	/**
	 * on_icons 설정 부분 noti.set_icon_priority(숫자); (0) 맨 앞에 고정으로 정렬된다. (1이상) 정확히 위치는 모르지만 0 옆에 정렬한다. set_is_good true:디버프가 아니다 false 디버프다
	 **/
	public static void on_icons(L1PcInstance pc, int skillId, int duration) {
		SC_SPELL_BUFF_NOTI noti = null;
		duration = (int) pc.Liberation_Time(pc, skillId, duration);
		switch (skillId) {
			case PANTHERA:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1626);
				noti.set_tooltip_str_id(7061);
				noti.set_new_str_id(7061);
				noti.set_is_good(false);
			}
			break;
			case PHANTOM_DEATH:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9701);
				noti.set_tooltip_str_id(7065);
				noti.set_new_str_id(7065);
				noti.set_is_good(false);
			}
			break;
			case PHANTOM_RIPER:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9702);
				noti.set_tooltip_str_id(7064);
				noti.set_new_str_id(7064);
				noti.set_is_good(false);
			}
			break;
			case PHANTOM_REQUIEM:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10501);
				noti.set_tooltip_str_id(8460);
				noti.set_new_str_id(8460);
				noti.set_is_good(false);
			}
			break;
			case ENSNARE:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10719);
				noti.set_tooltip_str_id(8615);
				noti.set_new_str_id(8615);
				noti.set_is_good(false);
			}
			break;
			case OSIRIS:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(11047);
				noti.set_tooltip_str_id(9085);
				noti.set_new_str_id(9085);
				noti.set_is_good(false);
			}
			break;

			case TEMPEST:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10719);
				noti.set_tooltip_str_id(8615);
				noti.set_new_str_id(8615);
				noti.set_is_good(false);
			}
			break;
			case CONQUEROR_STUN:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1626);
				noti.set_tooltip_str_id(9421);
				noti.set_new_str_id(9421);
				noti.set_is_good(false);
			}
			break;
/*		case TYRANT_EXCUTION:{
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_duration(duration);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(11440);
			noti.set_tooltip_str_id(9418);
			noti.set_new_str_id(9418);
			noti.set_is_good(false);
		}
		break;*/
			case EMPIRE:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1626);
				noti.set_tooltip_str_id(5444);
				noti.set_new_str_id(5444);
				noti.set_is_good(false);
			}
			break;
			case EMPIRE_OVERLOAD:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10518);
				noti.set_tooltip_str_id(8479);
				noti.set_new_str_id(8479);
				noti.set_is_good(false);
			}
			break;
			case SHADOW_STEP:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(6176);
				noti.set_tooltip_str_id(8625);
				noti.set_new_str_id(8625);
				noti.set_is_good(false);
			}
			break;
			case SHADOW_STEP_CHASER:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10862);
				noti.set_tooltip_str_id(8663);
				noti.set_new_str_id(8663);
				noti.set_is_good(false);
			}
			break;
			case ARMOR_BRAKE:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(4473);
				noti.set_tooltip_str_id(3141);
				noti.set_new_str_id(3141);
				noti.set_is_good(false);
			}
			break;
			case BONE_BREAK:
			case BONE_BREAK_LAST:
			case TRIPLE_STUN:
			case FORCE_STUN_FAIL:
			case FOU_SLAYER_BRAVE:
			case CRUEL:
			case SHOCK_STUN:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1626);
				noti.set_tooltip_str_id(7982);
				noti.set_new_str_id(7982);
				noti.set_is_good(false);
			}
			break;
			case FORCE_STUN:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9609);
				noti.set_tooltip_str_id(7007);
				noti.set_new_str_id(7007);
				noti.set_is_good(false);
			}
			break;
			case BEHEMOTH:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(11550);
				noti.set_tooltip_str_id(9659);
				noti.set_new_str_id(9659);
				noti.set_is_good(false);
			}
			break;

			case FOU_SLAYER_FORCE:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1626);
				noti.set_tooltip_str_id(9655);
				noti.set_new_str_id(9655);
				noti.set_is_good(false);
			}
			break;
	/*	case CHAINSWORD1:{
			int icon_id = 0;
			if (pc.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {

			} else if (pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt()){

			}


			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_duration(duration);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(icon_id);
			noti.set_tooltip_str_id(1393);
			noti.set_new_str_id(1393);
			noti.set_is_good(true);
		}
		break;*/
			case STR_ADEN_SCROLL_BUFF:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3814);
				noti.set_tooltip_str_id(1720);
				noti.set_new_str_id(1720);
				noti.set_is_good(true);
			}
			break;
			case DEX_ADEN_SCROLL_BUFF:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3814);
				noti.set_tooltip_str_id(1719);
				noti.set_new_str_id(1719);
				noti.set_is_good(true);
			}
			break;
			case INT_ADEN_SCROLL_BUFF:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3814);
				noti.set_tooltip_str_id(1721);
				noti.set_new_str_id(1721);
				noti.set_is_good(true);
			}
			break;



			case BRAVE_UNION:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(11425);
				noti.set_tooltip_str_id(9416);
				noti.set_new_str_id(9416);
				noti.set_is_good(true);
			}
			break;
			case DOUBLE_BRAKE:{

				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
//			noti.set_on_icon_id(L1SkillId.TARAS_ATTACK_SPEED);
				noti.set_on_icon_id(1122);
				noti.set_tooltip_str_id(821);
				noti.set_new_str_id(821);
				noti.set_is_good(true);
			}
			break;
			case TARAS_ATTACK_SPEED:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
//			noti.set_on_icon_id(L1SkillId.TARAS_ATTACK_SPEED);
				noti.set_on_icon_id(7444);
				noti.set_tooltip_str_id(9290);
				noti.set_new_str_id(9290);
				noti.set_end_str_id(829);
				noti.set_is_good(true);
			}
			break;
			case TARAS_MOVE_SPEED:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
//			noti.set_on_icon_id(L1SkillId.TARAS_MOVE_SPEED);
				noti.set_on_icon_id(5241);
				noti.set_tooltip_str_id(8711);
				noti.set_new_str_id(8711);
				noti.set_is_good(true);
			}
			break;
			case PC_EXP_UP:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(L1SkillId.PC_EXP_UP);
				noti.set_tooltip_str_id(9277);
				noti.set_new_str_id(9277);
				noti.set_is_good(true);
			}
			break;
			case MANADECREASEPOTION:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(L1SkillId.MANADECREASEPOTION);
				noti.set_tooltip_str_id(8603); //마나 절감: MP 소모량 30% 감소
				noti.set_new_str_id(8603);
				noti.set_is_good(true);
			}
			break;
			case 수경:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(L1SkillId.수경);
				noti.set_tooltip_str_id(9168);
				noti.set_new_str_id(9168);
				noti.set_is_good(true);
			}
			break;
			case 먹음직스러운복어:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(11176);
				noti.set_tooltip_str_id(9169);
				noti.set_new_str_id(9169);
				noti.set_is_good(false);
			}
			break;
			case HALPAS: {
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9612);
				noti.set_off_icon_id(9612);
				noti.set_tooltip_str_id(7009);
				noti.set_new_str_id(7009);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			}
			case INFERNO: {
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9197);
				noti.set_off_icon_id(9197);
				noti.set_tooltip_str_id(5445);
				noti.set_new_str_id(5445);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			}
			case COUNTER_BARRIER: {
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(2345);
				noti.set_off_icon_id(2345);
				noti.set_tooltip_str_id(1088);
				noti.set_new_str_id(1088);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			}
			case BURNING_SHOT: {
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10535);
				noti.set_off_icon_id(10535);
				noti.set_tooltip_str_id(8489);
				noti.set_new_str_id(8489);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			}
			case SHOCK_ATTACK: {
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10553);
				noti.set_off_icon_id(10553);
				noti.set_tooltip_str_id(8509);
				noti.set_new_str_id(8509);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			}
			case MAGIC_RAGE1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10388);
				noti.set_off_icon_id(10388);
				noti.set_tooltip_str_id(8386);
				noti.set_new_str_id(8386);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10389);
				noti.set_off_icon_id(10389);
				noti.set_tooltip_str_id(8387);
				noti.set_new_str_id(8387);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10390);
				noti.set_off_icon_id(10390);
				noti.set_tooltip_str_id(8388);
				noti.set_new_str_id(8388);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE4:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10391);
				noti.set_off_icon_id(10391);
				noti.set_tooltip_str_id(8389);
				noti.set_new_str_id(8389);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE5:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10392);
				noti.set_off_icon_id(10392);
				noti.set_tooltip_str_id(8390);
				noti.set_new_str_id(8390);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DISINTEGRATE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1626);
				noti.set_off_icon_id(1626);
				noti.set_tooltip_str_id(7982);
				noti.set_new_str_id(7982);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case DECAY_POTION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10398);
				noti.set_off_icon_id(10398);
				noti.set_tooltip_str_id(8409);
				noti.set_new_str_id(8409);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case BERSERKERS: {
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1556);
				noti.set_off_icon_id(1556);
				if (pc.isWizard()) {
					noti.set_tooltip_str_id(7839);
					noti.set_new_str_id(7839);
				} else {
					noti.set_tooltip_str_id(965);
					noti.set_new_str_id(965);
				}
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case PRESHER:{
				int str_id = !pc.getPresherDeathRecall() ? 7747 : 7751;
				int icon_id = !pc.getPresherDeathRecall() ? 10155 : 10153;
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(icon_id);
				noti.set_off_icon_id(icon_id);
				noti.set_tooltip_str_id(str_id);
				noti.set_new_str_id(str_id);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
			}
			break;
			case TYRANT:{
				if (pc.is_Tyrant_Excute()) {
					noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_duration(duration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(11440);
					noti.set_off_icon_id(11440);
					noti.set_tooltip_str_id(9418);
					noti.set_new_str_id(9418);
					noti.set_end_str_id(0);
					noti.set_is_good(false);
				}
			}
			break;
			case VANGUARD: {
				int str_id = !pc.isSpearModeType() ? 7743 : 7744;
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10152);
				noti.set_off_icon_id(10152);
				noti.set_tooltip_str_id(str_id);
				noti.set_new_str_id(str_id);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case LIBERATION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10205);
				noti.set_off_icon_id(10205);
				noti.set_tooltip_str_id(8021);
				noti.set_new_str_id(8021);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.PHYSICAL_ENCHANT_DEX:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(483);
				noti.set_off_icon_id(483);
				noti.set_tooltip_str_id(855);
				noti.set_new_str_id(855);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10044);
				noti.set_off_icon_id(10044);
				noti.set_tooltip_str_id(7657);
				noti.set_new_str_id(7657);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10045);
				noti.set_off_icon_id(10045);
				noti.set_tooltip_str_id(7658);
				noti.set_new_str_id(7658);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_ELF_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10048);
				noti.set_off_icon_id(10048);
				noti.set_tooltip_str_id(7659);
				noti.set_new_str_id(7659);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10047);
				noti.set_off_icon_id(10047);
				noti.set_tooltip_str_id(7660);
				noti.set_new_str_id(7660);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10046);
				noti.set_off_icon_id(10046);
				noti.set_tooltip_str_id(7661);
				noti.set_new_str_id(7661);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10049);
				noti.set_off_icon_id(10049);
				noti.set_tooltip_str_id(7662);
				noti.set_new_str_id(7662);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10051);
				noti.set_off_icon_id(10051);
				noti.set_tooltip_str_id(7663);
				noti.set_new_str_id(7663);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10050);
				noti.set_off_icon_id(10050);
				noti.set_tooltip_str_id(7664);
				noti.set_new_str_id(7664);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_FENCER_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10043);
				noti.set_off_icon_id(10043);
				noti.set_tooltip_str_id(7665);
				noti.set_new_str_id(7665);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_LANCER_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10242);
				noti.set_off_icon_id(10242);
				noti.set_tooltip_str_id(7666);
				noti.set_new_str_id(7666);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10044);
				noti.set_off_icon_id(10044);
				noti.set_tooltip_str_id(8542);
				noti.set_new_str_id(8542);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10045);
				noti.set_off_icon_id(10045);
				noti.set_tooltip_str_id(8543);
				noti.set_new_str_id(8543);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_ELF_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10048);
				noti.set_off_icon_id(10048);
				noti.set_tooltip_str_id(8544);
				noti.set_new_str_id(8544);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10047);
				noti.set_off_icon_id(10047);
				noti.set_tooltip_str_id(8545);
				noti.set_new_str_id(8545);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10046);
				noti.set_off_icon_id(10046);
				noti.set_tooltip_str_id(8546);
				noti.set_new_str_id(8546);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10049);
				noti.set_off_icon_id(10049);
				noti.set_tooltip_str_id(8547);
				noti.set_new_str_id(8547);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10051);
				noti.set_off_icon_id(10051);
				noti.set_tooltip_str_id(8548);
				noti.set_new_str_id(8548);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10050);
				noti.set_off_icon_id(10050);
				noti.set_tooltip_str_id(8549);
				noti.set_new_str_id(8549);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_FENCER_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10043);
				noti.set_off_icon_id(10043);
				noti.set_tooltip_str_id(8550);
				noti.set_new_str_id(8550);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_LANCER_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10242);
				noti.set_off_icon_id(10242);
				noti.set_tooltip_str_id(8551);
				noti.set_new_str_id(8551);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10044);
				noti.set_off_icon_id(10044);
				noti.set_tooltip_str_id(8552);
				noti.set_new_str_id(8552);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10045);
				noti.set_off_icon_id(10045);
				noti.set_tooltip_str_id(8553);
				noti.set_new_str_id(8553);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_ELF_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10048);
				noti.set_off_icon_id(10048);
				noti.set_tooltip_str_id(8554);
				noti.set_new_str_id(8554);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10047);
				noti.set_off_icon_id(10047);
				noti.set_tooltip_str_id(8555);
				noti.set_new_str_id(8555);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10046);
				noti.set_off_icon_id(10046);
				noti.set_tooltip_str_id(8556);
				noti.set_new_str_id(8556);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10049);
				noti.set_off_icon_id(10049);
				noti.set_tooltip_str_id(8557);
				noti.set_new_str_id(8557);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10051);
				noti.set_off_icon_id(10051);
				noti.set_tooltip_str_id(8558);
				noti.set_new_str_id(8558);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10050);
				noti.set_off_icon_id(10050);
				noti.set_tooltip_str_id(8559);
				noti.set_new_str_id(8559);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_FENCER_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10043);
				noti.set_off_icon_id(10043);
				noti.set_tooltip_str_id(8560);
				noti.set_new_str_id(8560);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLASS_RANK_BLESS_LANCER_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10242);
				noti.set_off_icon_id(10242);
				noti.set_tooltip_str_id(8561);
				noti.set_new_str_id(8561);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;

			case L1SkillId.CLAN_EXP_BUFF_1ST:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10164);
				noti.set_off_icon_id(10164);
				noti.set_tooltip_str_id(7974);
				noti.set_new_str_id(7974);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLAN_EXP_BUFF_2ND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10166);
				noti.set_off_icon_id(10166);
				noti.set_tooltip_str_id(7975);
				noti.set_new_str_id(7975);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLAN_DEFENCE_BUFF_1ST:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10931);
				noti.set_off_icon_id(10931);
				noti.set_tooltip_str_id(8955);
				noti.set_new_str_id(8955);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLAN_DEFENCE_BUFF_2ND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10933);
				noti.set_off_icon_id(10933);
				noti.set_tooltip_str_id(8956);
				noti.set_new_str_id(8956);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLAN_PVP_BUFF_1ST:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10168);
				noti.set_off_icon_id(10168);
				noti.set_tooltip_str_id(7976);
				noti.set_new_str_id(7976);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.CLAN_PVP_BUFF_2ND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10170);
				noti.set_off_icon_id(10170);
				noti.set_tooltip_str_id(7977);
				noti.set_new_str_id(7977);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case TOP_RANKER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(7090);
				noti.set_off_icon_id(7090);
				noti.set_tooltip_str_id(4562);
				noti.set_new_str_id(4562);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case GRACE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(7427);
				noti.set_off_icon_id(7427);
				noti.set_tooltip_str_id(4734);
				noti.set_new_str_id(4734);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case HIGH_CLASS_GAHO_BUFF:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(8599);
				noti.set_off_icon_id(8599);
				noti.set_tooltip_str_id(4647);
				noti.set_new_str_id(4647);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DRAGON_HALPAS_POWER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9972);
				noti.set_off_icon_id(9972);
				noti.set_tooltip_str_id(5011);
				noti.set_new_str_id(5011);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DRAGON_HALPAS_WISH:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9971);
				noti.set_off_icon_id(9971);
				noti.set_tooltip_str_id(7566);
				noti.set_new_str_id(7566);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case DRAGON_HALPAS_WATER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9969);
				noti.set_off_icon_id(9969);
				noti.set_tooltip_str_id(7617);
				noti.set_new_str_id(7617);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case DRAGON_HALPAS_FIRE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9968);
				noti.set_off_icon_id(9968);
				noti.set_tooltip_str_id(7618);
				noti.set_new_str_id(7618);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case DRAGON_HALPAS:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3622);
				noti.set_off_icon_id(3622);
				noti.set_tooltip_str_id(7434);
				noti.set_new_str_id(7434);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case STATUS_WISDOM_POTION_POWER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(484);
				noti.set_off_icon_id(484);
				noti.set_tooltip_str_id(3154);
				noti.set_new_str_id(3154);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case STATUS_WISDOM_POTION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(484);
				noti.set_off_icon_id(484);
				noti.set_tooltip_str_id(3154);
				noti.set_new_str_id(3154);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case IMMUNE_TO_HARM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1562);
				noti.set_off_icon_id(1562);
				noti.set_tooltip_str_id(966);
				noti.set_new_str_id(966);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DARK_BLIND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1116);
				noti.set_off_icon_id(1116);
				noti.set_tooltip_str_id(147);
				noti.set_new_str_id(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case DESPERADO:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(6499);
				noti.set_off_icon_id(6499);
				noti.set_tooltip_str_id(4119);
				noti.set_new_str_id(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case DEMOLITION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9606);
				noti.set_off_icon_id(9606);
				noti.set_tooltip_str_id(7011);
				noti.set_new_str_id(7011);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case TOMAHAWK:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(10702);
				noti.set_off_icon_id(6456);
				noti.set_tooltip_str_id(3992);
				noti.set_new_str_id(3992);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case LUCIFER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(4503);
				noti.set_off_icon_id(4503);
				noti.set_tooltip_str_id(5268);
				noti.set_new_str_id(5268);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DRAGON_ARMOR_BLESSING:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9944);
				noti.set_off_icon_id(9944);
				noti.set_tooltip_str_id(7437);
				noti.set_new_str_id(7437);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DRAGON_ARMOR_EQUIP:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9943);
				noti.set_off_icon_id(9943);
				noti.set_tooltip_str_id(7465);
				noti.set_new_str_id(7465);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DRAGON_ARMOR_BLESSING_REDUC:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9943);
				noti.set_off_icon_id(9943);
				noti.set_tooltip_str_id(7436);
				noti.set_new_str_id(7436);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAFR:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9837);
				noti.set_off_icon_id(9837);
				noti.set_tooltip_str_id(7448);
				noti.set_new_str_id(7449);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case REDUCTION_ARMOR:// 패시브
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1889);// 9831
				noti.set_off_icon_id(1889);// 9831
				noti.set_tooltip_str_id(1043);
				noti.set_new_str_id(1043);
				noti.set_is_good(true);
				break;
			case ETERNITI:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9608);
				noti.set_off_icon_id(9608);
				noti.set_tooltip_str_id(7008);
				noti.set_new_str_id(7008);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case POTENTIAL:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9610);
				noti.set_off_icon_id(9610);
				noti.set_tooltip_str_id(7010);
				noti.set_new_str_id(7010);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case PRIME:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9611);
				noti.set_off_icon_id(9611);
				noti.set_tooltip_str_id(7006);
				noti.set_new_str_id(7006);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case JUDGEMENT:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9711);
				noti.set_off_icon_id(9711);
				noti.set_tooltip_str_id(7066);
				noti.set_new_str_id(7066);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case PHANTOM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9700);
				noti.set_off_icon_id(9700);
				noti.set_tooltip_str_id(7063);
				noti.set_new_str_id(7063);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case 50002: //팬텀
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9702);
				noti.set_off_icon_id(9702);
				noti.set_tooltip_str_id(7064);
				noti.set_new_str_id(7064);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case 50003: //판테라
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9701);
				noti.set_off_icon_id(9701);
				noti.set_tooltip_str_id(7065);
				noti.set_new_str_id(7065);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case EXP_POTION:
				pc.send_effect(pc.getMap().isSafetyZone(pc.getX(), pc.getY()), 134, duration);
				break;
			case L1SkillId.IMPACT:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(7456);
				noti.set_off_icon_id(7456);
				noti.set_tooltip_str_id(4761);
				noti.set_new_str_id(4761);
				noti.set_end_str_id(4754);
				noti.set_is_good(true);
				break;
			case L1SkillId.CUBE_AVATAR:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3101);
				noti.set_off_icon_id(3101);
				// noti.set_icon_priority(10);
				noti.set_tooltip_str_id(3073);
				noti.set_new_str_id(3073);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case CUBE_RICH:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(5308);
				noti.set_off_icon_id(5309);
				// noti.set_icon_priority(10);
				noti.set_tooltip_str_id(1348);
				noti.set_new_str_id(1348);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case CUBE_GOLEM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(5313);
				noti.set_off_icon_id(5313);
				// noti.set_icon_priority(10);
				noti.set_tooltip_str_id(3075);
				noti.set_new_str_id(3075);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case CUBE_OGRE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(5311);
				noti.set_off_icon_id(5311);
				// noti.set_icon_priority(10);
				noti.set_tooltip_str_id(3074);
				noti.set_new_str_id(3074);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.PC_CAFE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9653);
				noti.set_off_icon_id(9653);
				noti.set_icon_priority(0);
				noti.set_tooltip_str_id(2156);
				noti.set_new_str_id(2156);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.DECIDING_BUFF:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9654);
				noti.set_off_icon_id(9654);
				noti.set_icon_priority(10);
				noti.set_tooltip_str_id(7021);
				noti.set_new_str_id(7021);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.HERO_GAHO_BUFF:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(9651);
				noti.set_off_icon_id(9651);
				noti.set_icon_priority(0);
				noti.set_tooltip_str_id(4347);
				noti.set_new_str_id(4347);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.FOG_OF_SLEEPING:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(1560);
				noti.set_off_icon_id(1560);
				// noti.set_icon_priority(5);
				noti.set_tooltip_str_id(296);
				// noti.set_new_str_id(296);
				// noti.set_end_str_id(2218);
				noti.set_is_good(false);// 디버프다
				break;
			case L1SkillId.PHANTASM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_duration(duration);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(3129);
				noti.set_off_icon_id(3129);
				// noti.set_icon_priority(5);
				noti.set_tooltip_str_id(296);
				// noti.set_new_str_id(296);
				// noti.set_end_str_id(2218);
				noti.set_is_good(false);// 디버프다
				break;
			default:
				break;
		}

		if (noti != null) {
			noti.set_spell_id(revisionSkillId(skillId));
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}

	/**
	 * TODO 오프아이콘 관련해서 MjSkillStopper에서 추가후 효과가 안빠지는 현상 발생 스킬류 아니면 추가하지말것(아이콘은 자동으로 빠짐)!
	 **/
	public static void off_icons(L1PcInstance pc, int skillId) {
		SC_SPELL_BUFF_NOTI noti = null;
		switch (skillId) {
/*		case POLY_RING_MASTER2:{
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_duration(0);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
			break;
		}*/

			case STR_ADEN_SCROLL_BUFF:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case DEX_ADEN_SCROLL_BUFF:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case INT_ADEN_SCROLL_BUFF:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case BRAVE_UNION:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case PC_EXP_UP:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case MANADECREASEPOTION:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case 수경:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
			}
			break;
			case 먹음직스러운복어:{
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
			}
			break;

			case HALPAS:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case INFERNO:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case COUNTER_BARRIER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case BURNING_SHOT:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case SHOCK_ATTACK:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case MAGIC_RAGE1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE4:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAGIC_RAGE5:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DEVINE_PROTECTION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(10385);
				noti.set_end_str_id(8404);
				noti.set_is_good(true);
				break;
			case DISINTEGRATE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(L1SkillId.DISINTEGRATE);
				noti.set_off_icon_id(0x00);
				noti.set_end_str_id(0);
				break;
			case PRESHER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case VANGUARD:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.MEDITATION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.PHYSICAL_ENCHANT_DEX:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_ELF_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_FENCER_1:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_ELF_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_FENCER_2:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_PRINCE_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_KNIGHT_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_ELF_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WIZARD_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DARKELF_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_WARRIOR_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLASS_RANK_BLESS_FENCER_3:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLAN_EXP_BUFF_1ST:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(7978);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLAN_EXP_BUFF_2ND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(7978);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLAN_DEFENCE_BUFF_1ST:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(8957);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLAN_DEFENCE_BUFF_2ND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(8957);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLAN_PVP_BUFF_1ST:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(7979);
				noti.set_is_good(false);
				break;
			case L1SkillId.CLAN_PVP_BUFF_2ND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(7979);
				noti.set_is_good(false);
				break;
			case GRACE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(4741);
				noti.set_is_good(true);
				break;
			case HIGH_CLASS_GAHO_BUFF:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(3802);
				noti.set_is_good(false);
				break;
			case DRAGON_HALPAS_WISH:
			case DRAGON_HALPAS_WATER:
			case DRAGON_HALPAS_FIRE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(7567);
				noti.set_is_good(false);
				break;
			case DRAGON_HALPAS:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case STATUS_WISDOM_POTION_POWER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(2224);
				noti.set_is_good(true);
				break;
			case STATUS_WISDOM_POTION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(2224);
				noti.set_is_good(true);
				break;
			case IMMUNE_TO_HARM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(315);
				noti.set_is_good(true);
				break;
			case DARK_BLIND:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case DESPERADO:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(4120);
				noti.set_is_good(false);
				break;
			case DEMOLITION:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(false);
				break;
			case TOMAHAWK:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(3993);
				noti.set_is_good(false);
				break;
			case LUCIFER:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(5269);
				noti.set_is_good(true);
				break;
			case DRAGON_ARMOR_BLESSING:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DRAGON_ARMOR_EQUIP:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case DRAGON_ARMOR_BLESSING_REDUC:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case MAFR:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case L1SkillId.PC_CAFE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				break;
			case EXP_POTION:
				pc.send_effect(pc.getMap().isSafetyZone(pc.getX(), pc.getY()), 86, 0);
				break;
			case L1SkillId.IMPACT:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(7456);
				noti.set_end_str_id(4754);
				noti.set_is_good(true);
				break;
			case L1SkillId.CUBE_AVATAR:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(5322);
				noti.set_is_good(true);
				break;
			case CUBE_RICH:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(5309);
				noti.set_is_good(true);
				break;
			case CUBE_GOLEM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(5314);
				noti.set_is_good(true);
				break;
			case CUBE_OGRE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(5312);
				noti.set_is_good(true);
				break;
			case DECIDING_BUFF:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(9654);
				noti.set_is_good(true);
				break;
			case HERO_GAHO_BUFF:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(9651);
				noti.set_is_good(true);
				break;
			case FOG_OF_SLEEPING:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(1560);
				noti.set_is_good(true);
				break;
			case PHANTASM:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(3129);
				noti.set_is_good(true);
				break;
			case PRIME:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_off_icon_id(3129);
				noti.set_is_good(true);
				break;
			default:
				break;
		}

		if (noti != null) {
			noti.set_spell_id(revisionSkillId(skillId));
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}

	/**
	 * TODO 오프아이콘 패시브 전용 off_icons_passive(pc, MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE, -1);
	 **/
	public static void off_icons_passive(L1PcInstance pc, MJPassiveID passiveId) {
		SC_SPELL_BUFF_NOTI noti = null;
		switch (passiveId) {
			case IllUSION_DIAMONDGOLEM_PASSIVE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(2214);
				noti.set_is_good(true);
				break;
			case IllUSION_LICH_PASSIVE:
				noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_duration(0);
				noti.set_end_str_id(2213);
				noti.set_is_good(true);
				break;
			default:
				break;
		}

		if (noti != null) {
			noti.set_spell_id(revisionPassiveId(passiveId));
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}

	private static int revisionPassiveId(MJPassiveID passiveid) {
		switch (passiveid) {
			case IMMUNETOHARM_SAINT:
				return 67;
			default:
				break;
		}
		return passiveid.toInt();
	}

	private static int revisionSkillId(int skillId) {
		/**
		 * TODO spell_info(라이브) 하고 스킬ID가 다를경우 강제로 설정 그렇지 않을 경우 PSS시 계속 사용한다. STATUS_WISDOM_POTION 종류 같은경우 스킬ID가 그대로이니 -처리하지 말라고 넣어놨음
		 **/
		switch (skillId) {
			case STATUS_WISDOM_POTION:
				return skillId;
			case STATUS_WISDOM_POTION_POWER:
				return skillId;
			case L1SkillId.PC_CAFE:
				return skillId;
		}
		return skillId - 1;
	}

	public static void remove_skills(L1PcInstance pc, Collection<Integer> skills) {
		for (Integer skillId : skills) {
			if (pc.hasSkillEffect(skillId))
				pc.removeSkillEffect(skillId);
		}
	}

	public static void remove_skills(L1PcInstance pc, Integer[] skills) {
		for (Integer skillId : skills) {
			if (pc.hasSkillEffect(skillId)) {
				pc.removeSkillEffect(skillId);
			}
		}
	}

	public static boolean check_skills(L1PcInstance pc, Integer[] skills) {
		for (Integer skillId : skills) {
			if (pc.hasSkillEffect(skillId))
				return true;
		}
		return false;
	}

	public static boolean remove_stun_skills(L1PcInstance pc, Integer[] skills) {
		for (Integer skillId : skills) {
			if (pc.hasSkillEffect(skillId)) {
				pc.removeSkillEffect(skillId);
				return true;
			}
		}
		return false;
	}

}

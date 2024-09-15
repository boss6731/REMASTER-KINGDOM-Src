package l1j.server.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import l1j.server.Config;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJKDASystem.MJKDA;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPEED_BONUS_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.itemdelay.ItemDelayTimer;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.poison.L1Poison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.timer.L1SkillTimer;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_PetCtrlMenu;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.IntRange;

public class L1Character extends L1Object {
	private static final long serialVersionUID = 1L;
	// 캐릭터 기본
	private String _name;
	private String _title;
	private int _level;
	private long m_exp;
	private int _currentHp;
	private int _currentMp;
	private int _prevHp;
	private int _prevMp;
	private int _maxHp = 0;
	private int _trueMaxHp = 0;
	private int _maxMp = 0;
	private int _trueMaxMp = 0;
	private int _lawful;
	private int _karma;

	// 狀態
	private int _tempCharGfx; // ● 基本圖形 ID
	private int _gfxid; // ● 圖形 ID
	private int _heading; // ● 方向 0. 左上 1. 上 2. 右上 3. 右 4. 右下 5. 下 6. 左下 7. 左
	private int _moveSpeed; // ● 速度 0. 通常 1. 加速 2. 減速
	private int _braveSpeed; // ● 勇敢狀態 0. 通常 1. 勇敢
	private L1Poison _poison = null;
	private boolean _paralyzed;
	private boolean _sleeped;
	private L1Paralysis _paralysis;
	private boolean _isDead;

	protected Light light = null; // 角色周圍的光
	private MoveState moveState; // 移動速度，面向方向
	protected Ability ability = null; // 能力值
	protected Resistance resistance = null; // 抵抗（魔法防禦，火，水，風，地，昏迷，凍結，睡眠，石化）
	protected AC ac = null; // AC 防禦

	// 未知
	private boolean _isSkillDelay = false;
	private boolean _isLinkSkillDelay = false;
	private int _addAttrKind;
	private int _status;

	// 傷害
	private int _dmgup = 0;
	private int _trueDmgup = 0;
	private int _bowDmgup = 0;
	private int _trueBowDmgup = 0;
	private int _hitup = 0;
	private int _trueHitup = 0;
	private int _bowHitup = 0;
	private int _trueBowHitup = 0;
	private int _Magicdmgup = 0;

	private int _missile_critical_rate = 0;
	private int _melee_critical_rate = 0;
	private int _magic_critical_rate = 0;
	private int _CC_Increase = 0;

	// 그외
	private static Random _rnd = new Random(System.nanoTime());
	private final Map<Integer, L1NpcInstance> _petlist = new HashMap<Integer, L1NpcInstance>();
	private final Map<Integer, L1DollInstance> _dolllist = new HashMap<Integer, L1DollInstance>();
	private final Map<Integer, L1SkillTimer> _skillEffect = new ConcurrentHashMap<Integer, L1SkillTimer>();
	private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap<Integer, L1ItemDelay.ItemDelayTimer>();
	private final Map<Integer, L1FollowerInstance> _followerlist = new HashMap<Integer, L1FollowerInstance>();

	// ■■■■■■■■■■ L1PcInstance에 이동하는 프롭퍼티 ■■■■■■■■■■
	private final Map<Integer, L1Object> _knownObjects = new ConcurrentHashMap<>();
	private final Map<Integer, L1PcInstance> _knownPlayer = new ConcurrentHashMap<>();

	public boolean isChangedHp() {
		return _currentHp != _prevHp;
	}

	public boolean isChangedMp() {
		return _currentMp != _prevMp;
	}

	public boolean isChangedHpAndUpdate() {
		boolean isChanged = isChangedHp();
		if (isChanged)
			_prevHp = _currentHp;
		return isChanged;
	}

	public boolean isChangedMpAndUpdate() {
		boolean isChanged = isChangedMp();
		if (isChanged)
			_prevMp = _currentMp;
		return isChanged;
	}

	private ConcurrentHashMap<Integer, Integer> m_classResistance; // 抵抗數據
	private ConcurrentHashMap<Integer, Integer> m_classPierce; // 命中數據
	/*
	 * - 技能: 君主, 騎士 // SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY
	 * - 精靈: 妖精, 黑暗妖精 // SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT
	 * - 龍語: 龍騎士, 幻術師 // SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL
	 * - 恐懼: 戰士 // SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR
	 */

	public boolean isPassive(int passiveId) {
		return false;
	}

	public void addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
		int val = getSpecialResistance(kind);

		if (m_classResistance == null)
			m_classResistance = new ConcurrentHashMap<Integer, Integer>(4);
		m_classResistance.put(kind.toInt(), value + val);
	}

	public int getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
		if (m_classResistance == null || !m_classResistance.containsKey(kind.toInt()))
			return 0;

		return m_classResistance.get(kind.toInt());
	}

	public ConcurrentHashMap<Integer, Integer> getSpecialResistanceMap() {
		return m_classResistance;
	}

	public void addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
		int val = getSpecialPierce(kind);
		if (m_classPierce == null)
			m_classPierce = new ConcurrentHashMap<Integer, Integer>(4);

		m_classPierce.put(kind.toInt(), value + val);
	}

	public int getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
		if (m_classPierce == null || !m_classPierce.containsKey(kind.toInt()))
			return 0;

		return m_classPierce.get(kind.toInt());
	}

	public ConcurrentHashMap<Integer, Integer> getSpecialPierceMap() {
		return m_classPierce;
	}

	public void dispose() {
		if (resistance != null) {
			resistance.dispose();
			resistance = null;
		}

		if (light != null) {
			light.dispose();// 오류
			light = null;
		}

		disposeShopInfo();
		_petlist.clear();
		_dolllist.clear();
		clearSkillEffectTimer();
		_followerlist.clear();
		_itemdelay.clear();
	}

	public L1Character() {
		_level = 1;
		ability = new Ability(this);
		resistance = new Resistance(this);
		ac = new AC(this);
		moveState = new MoveState();
		light = new Light(this);
	}

	public double getCurrentHpPercent() {
		return (100D / (double) getMaxHp()) * (double) getCurrentHp();
	}

	public double getCurrentMpPercent() {
		return (100D / (double) getMaxMp()) * (double) getCurrentMp();
	}

	public int getLongLocation() {
		int pt = (getX() << 16) & 0xffff0000;
		pt |= (getY() & 0x0000ffff);
		return pt;
	}

	public int getLongLocationReverse() {
		int pt = (getY() << 16) & 0xffff0000;
		pt |= (getX() & 0x0000ffff);
		return pt;
	}

	private int _effectedDG = 0;
	private int _characterDG = 0;
	private int _effectedER = 0;
	private int _characterER = 0;

	public void addDg(int i) {
		_effectedDG += i;
	}

	// TODO : 값 변경시 true 반환
	public boolean setCharacterDG(int i) {
		int old = _characterDG;
		_characterDG = i;
		return old != i;
	}

	public int getDg() {
		int Level = this.getLevel();
		int point = 0;
		if (this != null && this.isPassive(MJPassiveID.INFINITI_DODGE.toInt())) {
			if (Level < 70)
				Level = 70;
			point = ((Level - 70)/2) * 2 + 1;
			if (point > 15)
				point = 15;
		}

		if (hasSkillEffect(L1SkillId.POTENTIAL)) {
			point += (_effectedDG + _characterDG + point) * 0.2;
		}
		if (this != null && this.isPassive(MJPassiveID.SHINING_ARMOR.toInt())) {

			int levpoint = 0;
			if (Level > 90) {
				levpoint = (Level - 90) / 2;
			}
			if (levpoint > 5) {
				levpoint = 5;
			}
			point += levpoint + 5;
		}

		if (this != null && this.isPassive(MJPassiveID.TACTICAL_ADVANCE.toInt())) {
			if (Level < 90)
				Level = 90;

			point += 5 + ((Level - 90) / 2) * Config.MagicAdSetting_Lancer.TACTICAL_ADVANCE_VAL;

			if (point > 15)
				point = 15;
		}

		return _effectedDG + _characterDG + point;
	}

	// TODO : 값 변경시 true 반환
	public boolean setCharacterER(int i) {
		int old = _characterER;
		_characterER = i;
		return old != i;
	}

	public int getStatER() {
		int er = 0;
//		er = (getAbility().getTotalDex() - 8) / 2;
		er = (getAbility().getTotalDex()) / 2;
		return er;
	}

	public int getDefaultER() {
		int er = 0;
		int BaseEr = CalcStat.ER(getAbility().getTotalDex());
		er += BaseEr;
		return er;
	}

	public int getEffectedER() {
		return _effectedER;
	}

	public void addEffectedER(int i) {
		_effectedER += i;
		sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, getTotalER()));
	}

	public int getTotalER() {
		int Level = this.getLevel();
		int er = 0;
		int point = 0;
		er += getDefaultER();
		er += getEffectedER();
		er += _characterER;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			er += CalcStat.calcErByLevel(pc.getType(), pc.getLevel());
		}

		if (er < 0) {
			er = 0;
		}
		if (this != null && this.isPassive(MJPassiveID.INFINITI_BULLET.toInt())) {
			if (Level < 75)
				Level = 75;

			point = (Level - 75) + 1;
			if (point > 15)
				point = 15;
		}

		if (hasSkillEffect(L1SkillId.POTENTIAL)) {
			er += (er + point) * 0.2;
		}

		if (this != null && this.isPassive(MJPassiveID.DRESS_EVASION_PASSIVE.toInt())) {
			point = 18;
		}

		if (this != null && this.isPassive(MJPassiveID.TACTICAL_ADVANCE.toInt())) {
			if (Level < 80)
				Level = 80;

			point += 3 + ((Level - 80) / 3) * 2;

			if (point > 15)
				point = 15;
		}

		if (this != null && this.isPassive(MJPassiveID.SHINING_ARMOR.toInt())) {

			int levpoint = 0;
			if (Level > 90) {
				levpoint = (Level - 90) / 2;
			}
			if (levpoint > 5) {
				levpoint = 5;
			}
			point += levpoint + 5;
		}

		if (hasSkillEffect(L1SkillId.STRIKER_GALE)) {
			er = er / 3;
		}

		return er + point;
	}

	/**
	 * 使角色復活。
	 *
	 * @param hp
	 *            復活後的 HP
	 */
	public void resurrect(int hp) {
		if (!isDead())
			return;
		if (hp <= 0)
			hp = 1;

		setCurrentHp(hp);
		setDead(false);
		setStatus(0);
		L1PolyMorph.undoPoly(this);

		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(new S_RemoveObject(this));
			pc.removeKnownObject(this);
			pc.updateObject();
		}
	}

	/**
	 * 返回角色當前的 HP。
	 *
	 * @return 當前的 HP
	 */
	public int getCurrentHp() {
		return _currentHp;
	}

	/**
	 * 設置角色的 HP。
	 *
	 * @param i
	 *            角色的新 HP
	 */
	public void setCurrentHp(int i) {
		if (i >= getMaxHp()) {
			i = getMaxHp();
		}
		if (i < 0)
			i = 0;

		_prevHp = _currentHp;
		_currentHp = i;
	}
	/**
	 * 返回數據庫中的當前 HP
	 * 由於重生時 HP 設置為基礎體力，所以進行修改
	 **/
	private int _currentHpDB;
	public void setCurrentHpDB(int i){
		_currentHpDB = i;
	}
	public int getCurrentHpDB(){
		return _currentHpDB;
	}

	private int _currentMpDB;
	public void setCurrentMpDB(int i){
		_currentMpDB = i;
	}
	public int getCurrentMpDB(){
		return _currentMpDB;
	}

	/**
	 * 返回角色當前的 MP。
	 *
	 * @return 當前的 MP
	 */
	public int getCurrentMp() {
		return _currentMp;
	}

	/**
	 * 設置角色的 MP。
	 *
	 * @param i
	 *            角色的新 MP
	 */
	public void setCurrentMp(int i) {
		if (i >= getMaxMp()) {
			i = getMaxMp();
		}
		if (i < 0)
			i = 0;

		_prevMp = _currentMp;
		_currentMp = i;
	}

	/**
	 * 返回角色的睡眠狀態。
	 *
	 * @return 如果處於睡眠狀態，返回 true。
	 */
	public boolean isSleeped() {
		return _sleeped;
	}

	/**
	 * 設置角色的睡眠狀態。
	 *
	 * @param sleeped
	 *            如果處於睡眠狀態，設置為 true。
	 */
	public void setSleeped(boolean sleeped) {
		_sleeped = sleeped;
	}

	/**
	 * 返回角色的麻痺狀態。
	 *
	 * @return 麻痺狀態。如果處於麻痺狀態，返回 true。
	 */
	public boolean isParalyzed() {
		return _paralyzed;
	}

	/**
	 * 設置角色的麻痺狀態。
	 *
	 * @param paralyzed
	 *            麻痺狀態。如果處於麻痺狀態，設置為 true。
	 */
	public void setParalyzed(boolean paralyzed) {
		_paralyzed = paralyzed;
	}

	private boolean _thunderGrab;

	public boolean isThunderGrab() {
		return _thunderGrab;
	}

	public void setThunderGrab(boolean thunderGrab) {
		_thunderGrab = thunderGrab;
	}

	public L1Paralysis getParalysis() {
		return _paralysis;
	}

	public void setParalaysis(L1Paralysis p) {
		_paralysis = p;
	}

	public void cureParalaysis() {
		if (_paralysis != null) {
			_paralysis.cure();
		}
	}

	/**
	 * 向角色視野範圍內的玩家發送封包。
	 *
	 * @param packet
	 *            要發送的封包，表示為 ServerBasePacket 對象。
	 */
	public void broadcastPacket(MJIProtoMessage message, MJEProtoMessages message_id, boolean is_clear, boolean is_this_send) {
		if (message.isInitialized()) {
			broadcastPacket(message.writeTo(message_id), is_clear, is_this_send);
		} else {
			MJEProtoMessages.printNotInitialized("", message_id.toInt(), message.getInitializeBit());
		}
	}

	public void broadcastPacket(ProtoOutputStream stream) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
//			System.out.println(pc.getName()+": "+this.getName());
			pc.sendPackets(stream, false);
//			pc.sendPackets(stream, true);
		}
		stream.dispose();
	}

	public void broadcastPacket(ProtoOutputStream stream, boolean is_clear, boolean is_this_send) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(stream, false);
		}
		if (is_this_send) {
			sendPackets(stream, false);
		}
		if (is_clear)
			stream.dispose();
	}

	public void broadcastPacket(ProtoOutputStream[] streams, boolean is_clear, boolean is_this_send) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			for (ProtoOutputStream stream : streams)
				pc.sendPackets(stream, false);
		}
		if (is_this_send) {
			for (ProtoOutputStream stream : streams)
				sendPackets(stream, false);
		}
		if (is_clear) {
			for (ProtoOutputStream stream : streams)
				stream.dispose();
		}
	}

	public void broadcastPacket(ServerBasePacket[] pcs) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			for (int i = 0; i < pcs.length; i++)
				pc.sendPackets(pcs[i], false);
		}

		for (int i = 0; i < pcs.length; i++)
			pcs[i].clear();
	}

	public void broadcastPacket(ServerBasePacket[] pcs, boolean isClear) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			for (int i = 0; i < pcs.length; i++)
				pc.sendPackets(pcs[i], false);
		}

		if (isClear) {
			for (int i = 0; i < pcs.length; i++)
				pcs[i].clear();
		}
	}

	public void broadcastPacket(ServerBasePacket packet) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}

	public void broadcastPacket(ServerBasePacket packet, boolean isClear) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		if (isClear)
			packet.clear();
	}

	public void broadcastPacket(ServerBasePacket packet, L1Character target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}

	public void broadcastPacket(ServerBasePacket packet, L1Character[] target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}

	/**
	 * 向角色視野範圍內的玩家發送封包，但不發送到目標的屏幕內。
	 *
	 * @param packet
	 *            要發送的封包，表示為 ServerBasePacket 對象。
	 */
	public void broadcastPacketExceptTargetSight(ServerBasePacket packet, L1Character target) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayerExceptTargetSight(this, target)) {
			if (pc.knownsObject(this)) {
				pc.sendPackets(packet, false);
			}
		}
		packet.clear();
	}

	/**
	 * 向角色50格內的玩家發送封包。
	 *
	 * @param packet
	 *            要發送的封包，表示為 ServerBasePacket 對象。
	 */
	public void wideBroadcastPacket(ServerBasePacket packet) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 50)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}

	public int calcheading(int myx, int myy, int tx, int ty) {
		int newheading = 0;
		if(tx > myx && ty > myy)	newheading = 3;
		if(tx < myx && ty < myy)	newheading = 7;
		if(tx > myx && ty == myy)	newheading = 2;
		if(tx < myx && ty == myy)	newheading = 6;
		if(tx == myx && ty < myy)	newheading = 0;
		if(tx == myx && ty > myy)	newheading = 4;
		if(tx < myx && ty > myy)	newheading = 5;
		if(tx > myx && ty < myy)	newheading = 1;
		return newheading;
	}



	/**
	 * 返回角色正面的座標。
	 *
	 * @return 正面的座標
	 */
	public int[] getFrontLoc() {
		int[] loc = new int[2];
		int x = getX();
		int y = getY();
		int heading = getHeading();
		switch (heading) {
			case 0: {
				y--;
			}
			break;
			case 1: {
				x++;
				y--;
			}
			break;
			case 2: {
				x++;
			}
			break;
			case 3: {
				x++;
				y++;
			}
			break;
			case 4: {
				y++;
			}
			break;
			case 5: {
				x--;
				y++;
			}
			break;
			case 6: {
				x--;
			}
			break;
			case 7: {
				x--;
				y--;
			}
			break;
			default:
				break;
		}
		loc[0] = x;
		loc[1] = y;
		return loc;
	}

	/**
	 * 返回角色面對指定座標的方向。
	 *
	 * @param tx
	 *            指定座標的 X 值
	 * @param ty
	 *            指定座標的 Y 值
	 * @return 面對指定座標的方向
	 */
	public int targetDirection(int tx, int ty) {
		float dis_x = Math.abs(getX() - tx); // X 方向到目標的距離
		float dis_y = Math.abs(getY() - ty); // Y 方向到目標的距離
		float dis = Math.max(dis_x, dis_y); // 到目標的距離

		if (dis == 0)
			return getHeading();

		int avg_x = (int) Math.floor((dis_x / dis) + 0.59f); // 優先上下左右的圓形
		int avg_y = (int) Math.floor((dis_y / dis) + 0.59f); // 優先上下左右的圓形

		int dir_x = 0;
		int dir_y = 0;

		if (getX() < tx)
			dir_x = 1;
		if (getX() > tx)
			dir_x = -1;

		if (getY() < ty)
			dir_y = 1;
		if (getY() > ty)
			dir_y = -1;

		if (avg_x == 0)
			dir_x = 0;
		if (avg_y == 0)
			dir_y = 0;

		/**
		 * 返回角色面對指定座標的方向。
		 *
		 * @param tx
		 *            指定座標的 X 值
		 * @param ty
		 *            指定座標的 Y 值
		 * @return 面對指定座標的方向
		 */
		public int targetDirection(int tx, int ty) {
			float dis_x = Math.abs(getX() - tx); // X 方向到目標的距離
			float dis_y = Math.abs(getY() - ty); // Y 方向到目標的距離
			float dis = Math.max(dis_x, dis_y); // 到目標的距離

			if (dis == 0)
				return getHeading();

			int avg_x = (int) Math.floor((dis_x / dis) + 0.59f); // 優先上下左右的圓形
			int avg_y = (int) Math.floor((dis_y / dis) + 0.59f); // 優先上下左右的圓形

			// 計算方向
			int dir_x = Integer.compare(tx, getX());
			int dir_y = Integer.compare(ty, getY());

			if (dir_x == 1 && dir_y == -1)
				return 1; // 上
			if (dir_x == 1 && dir_y == 0)
				return 2; // 右上
			if (dir_x == 1 && dir_y == 1)
				return 3; // 右
			if (dir_x == 0 && dir_y == 1)
				return 4; // 右下
			if (dir_x == -1 && dir_y == 1)
				return 5; // 下
			if (dir_x == -1 && dir_y == 0)
				return 6; // 左下
			if (dir_x == -1 && dir_y == -1)
				return 7; // 左
			if (dir_x == 0 && dir_y == -1)
				return 0; // 左上

			return getHeading();
		}

		/**
		 * 返回指定座標的直線路徑上是否存在障礙物。
		 *
		 * @param tx
		 *            指定座標的 X 值
		 * @param ty
		 *            指定座標的 Y 值
		 * @return 如果沒有障礙物，返回 true；否則返回 false。
		 */
	public boolean glanceCheck(int tx, int ty) {
		L1Map map = getMap();
		int chx = getX();
		int chy = getY();
		int heading = targetDirection(tx, ty);
		for (int i = 0; i < 15; i++) {
			/*
			 * if(chx == tx && chy == ty) break;
			 *
			 * if(!map.isArrowPassable(chx, chy, MJBotUtil.calcheading(chx, chy, tx, ty)))
			 * return false;
			 */
			/*
			 * if ((chx == tx && chy == ty) || (chx == tx && chy + 1 == ty) || (chx == tx &&
			 * chy - 1 == ty) || (chx + 1 == tx && chy == ty) || (chx + 1 == tx && chy + 1
			 * == ty) || (chx + 1 == tx && chy - 1 == ty) || (chx - 1 == tx && chy == ty) ||
			 * (chx - 1 == tx && chy + 1 == ty) || (chx - 1 == tx && chy - 1 == ty)) { // -1
			 * -1 break; }
			 */

			int cx = Math.abs(chx - tx);
			int cy = Math.abs(chy - ty);
			if (cx <= 1 && cy <= 1)
				break;

			if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty)))
				return false;

			if (chx < tx)
				chx++;
			else if (chx > tx)
				chx--;
			if (chy < ty)
				chy++;
			else if (chy > ty)
				chy--;

			/*
			 * if (chx < tx && chy == ty) { chx++; } else if (chx > tx && chy == ty) {
			 * chx--; } else if (chx == tx && chy < ty) { chy++; } else if (chx == tx && chy
			 * > ty) { chy--; } else if (chx < tx && chy < ty) { chx++; chy++; } else if
			 * (chx < tx && chy > ty) { chx++; chy--; } else if (chx > tx && chy < ty) {
			 * chx--; chy++; } else if (chx > tx && chy > ty) { chx--; chy--; }
			 */
		}
		return true;
	}




		/**
		 * 返回是否可以攻擊指定座標。
		 *
		 * @param x
		 *            指定座標的 X 值。
		 * @param y
		 *            指定座標的 Y 值。
		 * @param range
		 *            可攻擊範圍（格數）
		 * @return 如果可以攻擊，返回 true；否則返回 false。
		 */
		public boolean isAttackPosition(int x, int y, int range) {
			if (range >= 7) { // 遠程武器（考慮到斜率，範圍超過 7 的情況下，超出屏幕）
				if (getLocation().getTileDistance(new Point(x, y)) > range)
					return false;
			} else {
				if (getLocation().getTileLineDistance(new Point(x, y)) > range)
					return false;
			}

			return glanceCheck(x, y);
		}

		/**
		 * 返回是否可以攻擊指定角色。
		 *
		 * @param target
		 *            指定的目標角色。
		 * @param range
		 *            可攻擊範圍（格數）
		 * @return 如果可以攻擊，返回 true；否則返回 false。
		 */
		public boolean isAttackPosition(L1Character target, int range) {
			if (range >= 7) { // 遠程武器（考慮到斜率，範圍超過 7 的情況下，超出屏幕）
				if (getLocation().getTileDistance(target.getLocation()) > range)
					return false;
			} else {
				if (getLocation().getTileLineDistance(target.getLocation()) > range)
					return false;
			}

			return glanceCheck(target.getX(), target.getY()) && target.glanceCheck(getX(), getY());
		}

		/**
		 * 返回角色的物品清單。
		 *
		 * @return 角色的物品清單，表示為 L1Inventory 對象。
		 */
		public L1Inventory getInventory() {
			return null;
		}

		/**
		 * 為角色新增一個新技能效果。
		 *
		 * @param skillId
		 *            新增效果的技能 ID。
		 * @param timeMillis
		 *            新增效果的持續時間。若為無限，則設為 0。
		 */
				private void addSkillEffect(int skillId, long timeMillis) {
					L1SkillTimer timer = null;
		// if(timeMillis > 0) {
					timeMillis = Liberation_Time(this, skillId, timeMillis);
					timer = L1SkillTimer.newTimer(this, skillId, timeMillis);
					_skillEffect.put(skillId, timer);
					timer.begin();
		// }
				}

	public long Liberation_Time(L1Character cha, int skillId, long time) {
		L1Skills debuff_skill = SkillsTable.getInstance().getTemplate(skillId);
		if (debuff_skill != null) {
			if (debuff_skill.isDebuff()) {
				if (hasSkillEffect(L1SkillId.LIBERATION)) {
					time /= Config.MagicAdSetting_Elf.LIBERATION_CO;
					sendPackets(new S_SkillSound(cha.getId(), 19539));
					broadcastPacket(new S_SkillSound(cha.getId(), 19539));
				}
			}
		}
		return time;
	}

	/**
	 * 캐릭터에, 스킬 효과를 설정한다. <br>
	 * 중복 하는 스킬이 없는 경우는, 새롭게 스킬 효과를 추가한다. <br>
	 * 중복 하는 스킬이 있는 경우는, 나머지 효과 시간과 파라미터의 효과 시간의 긴 (분)편을 우선해 설정한다.
	 *
	 * @param skillId
	 *            설정하는 효과의 스킬 ID.
	 * @param timeMillis
	 *            설정하는 효과의 지속 시간. 무한의 경우는 0.
	 */
	public void setSkillEffect(int skillId, long timeMillis) {
		if (hasSkillEffect(skillId)) {
			long remainingTimeMills = getSkillEffectTimeSec(skillId) * 1000L;
			if (remainingTimeMills >= 0 && (remainingTimeMills < timeMillis || timeMillis == 0)) {
				if (skillId == L1SkillId.DESPERADO || skillId == L1SkillId.TEMPEST)
					removeSkillEffect(skillId);
				else
					killSkillEffectTimer(skillId);
				addSkillEffect(skillId, timeMillis);
			}
		} else {
			addSkillEffect(skillId, timeMillis);
		}
	}
	public void addSkillEffectTime(int skillId, long timeMillis) {
		long skilltime = 0;
		if (hasSkillEffect(skillId)) {
			skilltime = getSkilleffect(skillId).timeMillis();
			killSkillEffectTimer(skillId);
			addSkillEffect(skillId, skilltime+timeMillis);
		}
	}

		/**
		 * 從角色中移除技能效果。
		 *
		 * @param skillId
		 *            要移除效果的技能 ID
		 */
		public void removeSkillEffect(int skillId) {
			L1SkillTimer timer = _skillEffect.remove(skillId);
			if (timer != null) {
				timer.end();
			}
		}

		/**
		 * 從角色中刪除技能效果的計時器，但不刪除技能效果。
		 *
		 * @param skillId
		 *            要刪除計時器的技能 ID
		 */
		public void killSkillEffectTimer(int skillId) {
			L1SkillTimer timer = _skillEffect.remove(skillId);
			if (timer != null)
				timer.kill();
		}

		/**
		 * 從角色中刪除所有技能效果的計時器，但不刪除技能效果。
		 */
		public void clearSkillEffectTimer() {
			for (L1SkillTimer timer : _skillEffect.values()) {
				if (timer != null) {
					timer.kill();
				}
			}
			_skillEffect.clear();
		}

		/**
		 * 確認角色是否具有特定的技能效果。
		 *
		 * @param skillId
		 *            技能 ID
		 * @return 如果有技能效果，返回 true；否則返回 false。
		 */
		public boolean hasSkillEffect(int skillId) {
			return _skillEffect.containsKey(skillId);
		}

	public boolean hasSkillEffect(int[] skills) {
		for (int skill_id : skills) {
			if (hasSkillEffect(skill_id))
				return true;
		}
		return false;
	}

	public boolean hasSkillEffect(Collection<Integer> skills) {
		for (int skill_id : skills) {
			if (hasSkillEffect(skill_id))
				return true;
		}
		return false;
	}

	/**
	 * 캐릭터의 스킬 효과의 지속 시간을 돌려준다.
	 *
	 * @param skillId
	 *            조사하는 효과의 스킬 ID
	 * @return 스킬 효과의 남은 시간(초). 스킬이 걸리지 않은가 효과 시간이 무한의 경우,-1.
	 */
	public int getSkillEffectTimeSec(int skillId) {
		L1SkillTimer timer = _skillEffect.get(skillId);
		if (timer == null) {
			return -1;
		}
		return timer.remainingSeconds();
//		return timer.timeMillis();
	}

	public L1SkillTimer getSkilleffect(int skillId) {
		return _skillEffect.get(skillId);
	}

	public Set<Entry<Integer, L1SkillTimer>> hasSkills() {
		return _skillEffect.entrySet();
	}

	/**
	 * 캐릭터에, skill delay 추가
	 *
	 * @param flag
	 */
	public void setSkillDelay(boolean flag) {
		_isSkillDelay = flag;
	}

	/**
	 * 캐릭터의 독 상태를 돌려준다.
	 *
	 * @return 스킬 지연중인가.
	 */
	public boolean isSkillDelay() {
		return _isSkillDelay;
	}

	/**
	 * 캐릭터에, skill delay 추가
	 *
	 * @param flag
	 */
	public void setLinkSkillDelay(boolean flag) {
		_isLinkSkillDelay = flag;
	}

	/**
	 * 캐릭터의 독 상태를 돌려준다.
	 *
	 * @return 스킬 지연중인가.
	 */
	public boolean isLinkSkillDelay() {
		return _isLinkSkillDelay;
	}

	/**
	 * 캐릭터에, Item delay 추가
	 *
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 * @param timer
	 *            지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer 오브젝트.
	 */
	public void addItemDelay(int delayId, L1ItemDelay.ItemDelayTimer timer) {
		_itemdelay.put(delayId, timer);
	}

	/**
	 * 캐릭터로부터, Item delay 삭제
	 *
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 */
	public void removeItemDelay(int delayId) {
		_itemdelay.remove(delayId);
	}

	/**
	 * 캐릭터에, Item delay 이 있을까
	 *
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연이 있으면 true, 없으면 false.
	 */
	public boolean hasItemDelay(int delayId) {
		return _itemdelay.containsKey(delayId);
	}

	/**
	 * 캐릭터의 item delay 시간을 나타내는, L1ItemDelay.ItemDelayTimer를 돌려준다.
	 *
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer.
	 */
	public L1ItemDelay.ItemDelayTimer getItemDelayTimer(int delayId) {
		return _itemdelay.get(delayId);
	}

	/**
	 * 캐릭터에, pet, summon monster, tame monster, created zombie 를 추가한다.
	 *
	 * @param npc
	 *            추가하는 Npc를 나타내는, L1NpcInstance 오브젝트.
	 */
	public void addPet(L1NpcInstance npc) {
		_petlist.put(npc.getId(), npc);
		sendPetCtrlMenu(npc, true);
	}

	/**
	 * 캐릭터로부터, pet, summon monster, tame monster, created zombie 를 삭제한다.
	 *
	 * @param npc
	 *            삭제하는 Npc를 나타내는, L1NpcInstance 오브젝트.
	 */
	public void removePet(L1NpcInstance npc) {
		_petlist.remove(npc.getId());
		sendPetCtrlMenu(npc, true);
	}

	/**
	 * 캐릭터의 애완동물 리스트를 돌려준다.
	 *
	 * @return 캐릭터의 애완동물 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는 L1NpcInstance.
	 */
	public Map<Integer, L1NpcInstance> getPetList() {
		return _petlist;
	}

	/**
	 * 캐릭터에 이벤트 NPC(캐릭터를 따라다니는)를 추가한다.
	 *
	 * @param follower
	 *            추가하는 follower를 나타내는, L1FollowerInstance 오브젝트.
	 */
	public void addFollower(L1FollowerInstance follower) {
		_followerlist.put(follower.getId(), follower);
	}

	/**
	 * 캐릭터로부터 이벤트 NPC(캐릭터를 따라다니는)를 삭제한다.
	 *
	 * @param follower
	 *            삭제하는 follower를 나타내는, L1FollowerInstance 오브젝트.
	 */
	public void removeFollower(L1FollowerInstance follower) {
		_followerlist.remove(follower.getId());
	}

	/**
	 * 캐릭터의 이벤트 NPC(캐릭터를 따라다니는) 리스트를 돌려준다.
	 *
	 * @return 캐릭터의 종자 리스트를 나타내는, HashMap 오브젝트. 이 오브젝트의 Key는 오브젝트 ID, Value는 L1FollowerInstance.
	 */
	public Map<Integer, L1FollowerInstance> getFollowerList() {
		return _followerlist;
	}

	/**
	 * 캐릭터에, 독을 추가한다.
	 *
	 * @param poison
	 *            독을 나타내는, L1Poison 오브젝트.
	 */
	public void setPoison(L1Poison poison) {
		_poison = poison;
	}

	/**
	 * 캐릭터의 독을 치료한다.
	 */
	public void curePoison() {
		if (_poison == null) {
			return;
		}
		_poison.cure();
	}

	/**
	 * 캐릭터의 독상태를 돌려준다.
	 *
	 * @return 캐릭터의 독을 나타내는, L1Poison 오브젝트.
	 */
	public L1Poison getPoison() {
		return _poison;
	}

	/**
	 * 캐릭터에 독의 효과를 부가한다
	 *
	 * @param effectId
	 * @see S_Poison#S_Poison(int, int)
	 */
	public void setPoisonEffect(int effectId) {
		broadcastPacket(new S_Poison(getId(), effectId));
	}

	/**
	 * 캐릭터가 존재하는 좌표가, 어느 존에 속하고 있을까를 돌려준다.
	 *
	 * @return 좌표의 존을 나타내는 값. 세이프티 존이면 1, 컴배트 존이면 -1, 노멀 존이면 0.
	 */

	public int getZoneType() {
		if (getMapId() == 800 || getMapId() == 5490) {
			return 1;
		}
		if (getMap().isSafetyZone(getLocation())) {
			/** 배틀존 **/
			if (getMapId() == 5153) {
				return -1;
			} else {
				return 1;
			}
		} else if (getMap().isCombatZone(getLocation())) {
			return -1;
		} else { // 노멀존
			return 0;
		}
	}

	public long get_exp() {
		return m_exp;
	}

	public void set_exp(long exp) {
		m_exp = exp;
	}

	/**
	 * 지정된 오브젝트를, 캐릭터가 인식하고 있을까를 돌려준다.
	 *
	 * @param obj
	 *            조사하는 오브젝트.
	 * @return 오브젝트를 캐릭터가 인식하고 있으면 true, 하고 있지 않으면 false. 자기 자신에 대해서는 false를 돌려준다.
	 */
	public boolean knownsObject(L1Object obj) {
		return _knownObjects.containsKey(obj.getId());
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 오브젝트를 돌려준다.
	 *
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1Object>.
	 */
	public Collection<L1Object> getKnownObjects() {
		return _knownObjects.values();
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 플레이어를 돌려준다.
	 *
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1PcInstance>
	 */
	public Collection<L1PcInstance> getKnownPlayers() {
		return _knownPlayer.values();
	}

	/**
	 * 캐릭터에, 새롭게 인식하는 오브젝트를 추가한다.
	 *
	 * @param obj
	 *            새롭게 인식하는 오브젝트.
	 */
	public boolean addKnownObject(L1Object obj, ServerBasePacket serverbasepacket) {
		if(!_knownObjects.containsValue(obj)){
			_knownObjects.put(obj.getId(),obj);
			if(obj instanceof L1PcInstance)
				_knownPlayer.put(obj.getId(),(L1PcInstance) obj);
			if(this instanceof L1PcInstance && serverbasepacket != null)
				((L1PcInstance)this).sendPackets(serverbasepacket);
			return true;
		}
		return false;
	}

	public void addKnownObject(L1Object obj) {
		_knownObjects.put(obj.getId(), obj);
		if (obj instanceof L1PcInstance) {
			_knownPlayer.put(obj.getId(), (L1PcInstance) obj);
		}
	}

	/**
	 * 캐릭터로부터, 인식하고 있는 오브젝트를 삭제한다.
	 *
	 * @param obj
	 *            삭제하는 오브젝트.
	 */
	public void removeKnownObject(L1Object obj) {
		_knownObjects.remove(obj.getId());
		_knownPlayer.remove(obj.getId());
	}

	/**
	 * 캐릭터로부터, 모든 인식하고 있는 오브젝트를 삭제한다.
	 */
	/*
	 * public void removeAllKnownObjects() { _knownObjects.clear(); _knownPlayer.clear(); }
	 */

	public void removeAllKnownObjects() {
		_knownObjects.clear();
		_knownPlayer.clear();
	}

	public String getName() {
		return _name;
	}

	public void setName(String s) {
		_name = s;
	}

	public int getLevel() {
		/*
		 * if(this instanceof L1PcInstance) { try { throw new Exception(); }catch(Exception e) { e.printStackTrace(); } }
		 */
		return _level;
	}

	public synchronized void setLevel(long level) {
		_level = (int) level;
	}

	public int getMaxHp() {
		if (getAbility() == null)
			return _maxHp;

		int maxhp = _maxHp;
		if (this instanceof L1PcInstance) {
			if (getAbility().getTotalCon() >= 25){
				maxhp += 200;
//				maxhp += 50;
			}
			if (getAbility().getTotalCon() >= 35){
//				maxhp += 100;
				maxhp += 400;
			}
			if (getAbility().getTotalCon() >= 45){
//				maxhp += 150;
				maxhp += 600;
			}
			if (getAbility().getTotalCon() >= 55){
//				maxhp += 200;
				maxhp += 800;
			}
			if (getAbility().getTotalCon() >= 60){
//				maxhp += 400;
				maxhp += 1000;
			}
		}

		int point = 0;
		if (this != null && this.isPassive(MJPassiveID.INFINITI_BLOOD.toInt())) {
			int Level = this.getLevel();
			if (Level < 60) {
				Level = 60;
			}
			point = (((Level - 60) / 3) + 1) * 50;
		}


		if (this != null && this.isPassive(MJPassiveID.MORTAL_BODY.toInt())) {
			int Level = this.getLevel();
			if (Level < 80) {
				Level = 80;
			}
			point = (((Level - 80) / 4) + 1) * 100;
			if (point >= 600){
				point = 600;
			}
		}


		if (hasSkillEffect(L1SkillId.POTENTIAL)) {
			maxhp += maxhp * 0.2;
		}

		return maxhp + point;
	}

	/*
	 * public int getMaxHp() { return _maxHp; }
	 */

	public void addMaxHp(int i) {
		setMaxHp(_trueMaxHp + i);

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}
	}

	public void setMaxHp(int hp) {
		_trueMaxHp = hp;
		_maxHp = (int) IntRange.ensure(_trueMaxHp, 1, 10000000);
		_currentHp = Math.min(_currentHp, _maxHp);

	}

	// TODO 스텟에 따른 MAXMP 증가
	public int getMaxMp() {
		if (getAbility() == null)
			return _maxMp;

		int maxmp = _maxMp;
		if (this instanceof L1PcInstance) {
			int wis = getAbility().getTotalWis();
			if (wis >= 25)
				maxmp += 50;
			if (wis >= 35)
				maxmp += 100;
			if (wis >= 45)
				maxmp += 150;
			if (wis >= 55)
				maxmp += 200;
			if (wis >= 60)
				maxmp += 400;

			if (hasSkillEffect(L1SkillId.POTENTIAL)) {
				maxmp += maxmp * 0.2;
			}
		}

		return maxmp;
	}

	/*
	 * public int getMaxMp() { //원본 return _maxMp; }
	 */

	public void setMaxMp(int mp) {
		_trueMaxMp = mp;
		_maxMp = (int) IntRange.ensure(_trueMaxMp, 0, 10000000);
		_currentMp = Math.min(_currentMp, _maxMp);
	}

	public void addMaxMp(int i) {
		setMaxMp(_trueMaxMp + i);

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}
	}

	public void healHp(int pt) {
		setCurrentHp(getCurrentHp() + pt);
	}

	public int getAddAttrKind() {
		return _addAttrKind;
	}

	public void setAddAttrKind(int i) {
		_addAttrKind = i;
	}

	public int getDmgup() {
		return _dmgup;
	}

	public void addDmgup(int i) {
		_trueDmgup += i;
		if (_trueDmgup >= 127) {
			_dmgup = 127;
		} else if (_trueDmgup <= -128) {
			_dmgup = -128;
		} else {
			_dmgup = _trueDmgup;
		}
	}

	public int getBowDmgup() {
		return _bowDmgup;
	}

	public int getMagicDmgup() {
		return _Magicdmgup;
	}

	public void addMagicDmgup(int i) {
		if (_Magicdmgup + i >= 127) {
			_Magicdmgup = 127;
		} else if (_Magicdmgup + i <= -128) {
			_Magicdmgup = -128;
		} else {
			_Magicdmgup += i;
		}
	}

	public void addBowDmgup(int i) {
		_trueBowDmgup += i;
		if (_trueBowDmgup >= 127) {
			_bowDmgup = 127;
		} else if (_trueBowDmgup <= -128) {
			_bowDmgup = -128;
		} else {
			_bowDmgup = _trueBowDmgup;
		}
	}

	public int getHitup() {
		return _hitup;
	}

	public void addHitup(int i) {
		_trueHitup += i;
		if (_trueHitup >= 127) {
			_hitup = 127;
		} else if (_trueHitup <= -128) {
			_hitup = -128;
		} else {
			_hitup = _trueHitup;
		}
	}

	public int getBowHitup() {
		return _bowHitup;
	}

	public int _reduction_per;

	public int get_reduction_per() {
		return _reduction_per;
	}

	public int add_reduction_per(int i) {
		_reduction_per += i;
		return _reduction_per;
	}

	public void addBowHitup(int i) {
		_trueBowHitup += i;
		if (_trueBowHitup >= 127) {
			_bowHitup = 127;
		} else if (_trueBowHitup <= -128) {
			_bowHitup = -128;
		} else {
			_bowHitup = _trueBowHitup;
		}
	}

	public int getMagicLevel() {
		return getLevel() / 4;
	}

	public int getMagicBonus() {
		int i = getAbility().getTotalInt();
		if (i <= 5)
			return -2;
		else if (i <= 8)
			return -1;
		else if (i <= 11)
			return 0;
		else if (i <= 14)
			return 1;
		else if (i <= 17)
			return 2;
		else
			return i - 15;
	}

	// 분신 스킬 중첩 방지
	private long _doppeltime = 0;

	public long getDoppelTime() {
		return _doppeltime;
	}

	public void setDoppelTime(long l) {
		_doppeltime = l;
	}

	public boolean isDead() {
		return _isDead;
	}

	public void setDead(boolean flag) {
		_isDead = flag;
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int i) {
		_status = i;
	}



	public String getTitle() {
		return _title;
	}

	public void setTitle(String s) {
		_title = s;
	}

	public int getLawful() {
		return _lawful;
	}

	public void setLawful(int i) {
		_lawful = i;
	}

	public synchronized void addLawful(int i) {
		if (Config.Login.StandbyServer)
			return;

		_lawful += i;
		if (_lawful > 32767) {
			_lawful = 32767;
		} else if (_lawful < -32768) {
			_lawful = -32768;
		}
	}

	public int getHeading() {
		return _heading;
	}

	public void setHeading(int i) {
		_heading = i;
	}

	public int getMoveSpeed() {
		return _moveSpeed;
	}

	public void setMoveSpeed(int i) {
		_moveSpeed = i;
	}

	public int getBraveSpeed() {
		return _braveSpeed;
	}

	public void setBraveSpeed(int i) {
		_braveSpeed = i;
	}

	private boolean _invisble;

	public boolean isInvisble() {
		return (hasSkillEffect(L1SkillId.INVISIBILITY) || hasSkillEffect(L1SkillId.BLIND_HIDING) || _invisble);
	}

	public void setInvisble(boolean b) {
		_invisble = b;
	}

	/** 캐릭터의 업을 돌려준다. */
	public int getKarma() {
		return _karma;
	}

	/** 캐릭터의 업을 설정한다. */
	public void setKarma(int karma) {
		_karma = karma;
	}

	private MJKDA _kda;

	public MJKDA getKDA() {
		return _kda;
	}

	public void setKDA(MJKDA kda) {
		_kda = kda;
	}

	// ** 도우너 딜레이 타이머 수정 **// by 도우너
	private long _skilldelay2;

	public long getSkilldelay2() {
		return _skilldelay2;
	}

	public void setSkilldelay2(long skilldelay2) {
		_skilldelay2 = skilldelay2;
	}

	// **지엠 버프 따로 저장 **// by 도우너
	private int _buffnoch;

	public int getBuffnoch() {
		return _buffnoch;
	}

	public void setBuffnoch(int buffnoch) {
		_buffnoch = buffnoch;
	}

	public static Random getRnd() {
		return _rnd;
	}

	public Light getLight() {
		return light;
	}

	public Ability getAbility() {
		return ability;
	}

	public Resistance getResistance() {
		return resistance;
	}

	public void resetResistance() {
		resistance = new Resistance(this);
	}

	public AC getAC() {
		return ac;
	}

	public MoveState getMoveState() {
		return moveState;
	}

		private int _mr = 0; // ● 魔法防禦(0)
		private int _trueMr = 0; // ● 真正的魔法防禦

		/**
	 * 獲取魔法防禦值。
	 *
	 * @return 如果具有 ERASE_MAGIC 效果，返回魔法防禦值的四分之一，否則返回魔法防禦值。
	 */
			public int getMr() {
				if (hasSkillEffect(L1SkillId.ERASE_MAGIC) == true) {
					return _mr / 4;
				} else {
					return _mr;
				}
			} // 使用時

	/**
	 * 獲取真正的魔法防禦值。
	 *
	 * @return 真正的魔法防禦值。
	 */
		public int getTrueMr() {
			return _trueMr;
		} // 設置時

	public void addMr(int i) {
		_trueMr += i;
		if (_trueMr <= 0) {
			_mr = 0;
		} else {
			_mr = _trueMr;
		}
	}

		private int lockIntervalIncrease; // 鎖定區間上升

		/**
		 * 獲取鎖定區間上升的值。
		 *
		 * @return 鎖定區間上升的值。
		 */
				public int getLockIntervalIncrease() {
					return lockIntervalIncrease;
				}

		/**
		 * 設置鎖定區間上升的值。
		 *
		 * @param i
		 *            要設置的值。
		 */
				public void setLockIntervalIncrease(int i) {
					lockIntervalIncrease = i;
				}

		/**
		 * 增加鎖定區間上升的值。
		 *
		 * @param i
		 *            增加的值。
		 */
				public void addLockIntervalIncrease(int i) {
					lockIntervalIncrease += i;
				}

	/** ui6 관련 펫파티,컨트롤 **/
	public void sendPetCtrlMenu(L1NpcInstance npc, boolean type) {
		if (npc instanceof L1PetInstance) {
			L1PetInstance pet = (L1PetInstance) npc;
			L1Character cha = pet.getMaster();
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PetCtrlMenu(pc, npc, type));
			}
		} else if (npc instanceof L1SummonInstance) {
			L1SummonInstance summon = (L1SummonInstance) npc;
			L1Character cha = summon.getMaster();
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PetCtrlMenu(pc, npc, type));
			}
		}
	}

		/** 2016.11.24 MJ 應用中心 市場價格 **/
		private ArrayList<MJDShopItem> _sellings; // 銷售清單
		private ArrayList<MJDShopItem> _purchasings; // 購買清單

	public void disposeShopInfo() {
		disposeSellings();
		disposePurchasings();
	}

	private MJDShopItem findDShopItem(ArrayList<MJDShopItem> list, int objid) {
		if (list == null)
			return null;

		int size = list.size();
		MJDShopItem item = null;
		for (int i = 0; i < size; i++) {
			item = list.get(i);
			if (item.objId == objid)
				return item;
		}
		return null;
	}

	public MJDShopItem findSellings(int objid) {
		return findDShopItem(_sellings, objid);
	}

	public void updateSellings(int objid, int count) {
		MJDShopItem item = findSellings(objid);
		if (item == null)
			return;

		if (item.count <= count) {
			_sellings.remove(item);
			MJDShopStorage.deleteProcess(this, item.objId);
		} else {
			item.count -= count;
			MJDShopStorage.updateProcess(this, item);
		}
	}

	public ArrayList<MJDShopItem> getSellings() {
		return _sellings;
	}

	public void setSellings(ArrayList<MJDShopItem> list) {
		_sellings = list;
	}

	public void addSellings(MJDShopItem item) {
		if (_sellings == null)
			_sellings = new ArrayList<MJDShopItem>(7);
		_sellings.add(item);

	}

	public void disposeSellings() {
		if (_sellings != null) {
			_sellings.clear();
			_sellings = null;
		}
	}

	public MJDShopItem findPurchasings(int objid) {
		return findDShopItem(_purchasings, objid);
	}

	public void updatePurchasings(int objid, int count) {
		MJDShopItem item = findPurchasings(objid);
		if (item == null)
			return;

		if (item.count <= count) {
			_purchasings.remove(item);
			MJDShopStorage.deleteProcess(this, item.objId);
		} else {
			item.count -= count;
			MJDShopStorage.updateProcess(this, item);
		}
	}

	public ArrayList<MJDShopItem> getPurchasings() {
		return _purchasings;
	}

	public void setPurchasings(ArrayList<MJDShopItem> list) {
		_purchasings = list;
	}

	public void addPurchasings(MJDShopItem item) {
		if (_purchasings == null)
			_purchasings = new ArrayList<MJDShopItem>(7);
		_purchasings.add(item);
	}

	public void disposePurchasings() {
		if (_purchasings != null) {
			_purchasings.clear();
			_purchasings = null;
		}
	}

	/** 2016.11.24 MJ 앱센터 시세 **/

	private MJBotAI _botAI;

	public MJBotAI getAI() {
		return _botAI;
	}

	public void setAI(MJBotAI ai) {
		_botAI = ai;
	}

	public boolean isHaste() {
		return (hasSkillEffect(L1SkillId.STATUS_HASTE) || hasSkillEffect(L1SkillId.HASTE) || hasSkillEffect(L1SkillId.MOB_HASTE) || hasSkillEffect(L1SkillId.GREATER_HASTE) || getMoveSpeed() == 1);
	}

	public boolean isSlow() {
		return hasSkillEffect(L1SkillId.SLOW) || hasSkillEffect(L1SkillId.MOB_SLOW_1) || hasSkillEffect(L1SkillId.MOB_SLOW_18);
	}

	public int getRemainSlowSeconds() {
		if (hasSkillEffect(L1SkillId.SLOW)) {
			return getSkillEffectTimeSec(L1SkillId.SLOW);
		}
		if (hasSkillEffect(L1SkillId.MOB_SLOW_1)) {
			return getSkillEffectTimeSec(L1SkillId.MOB_SLOW_1);
		}
		if (hasSkillEffect(L1SkillId.MOB_SLOW_18)) {
			return getSkillEffectTimeSec(L1SkillId.MOB_SLOW_18);
		}
		return 0;
	}

	public int _truetarget = 0;

	public int getTrueTarget() {
		return _truetarget;
	}

	public void setTrueTarget(int i) {
		_truetarget = i;
	}

	public int _truetarget_clan = 0;

	public int getTrueTargetClan() {
		return _truetarget_clan;
	}

	public void setTrueTargetClan(int i) {
		_truetarget_clan = i;
	}

	public int _truetarget_party = 0;

	public int getTrueTargetParty() {
		return _truetarget_party;
	}

	public void setTrueTargetParty(int i) {
		_truetarget_party = i;
	}

	public void sendPackets(ServerBasePacket pck, boolean clear) {
		if (clear)
			pck.clear();
	}

	public void sendPackets(ProtoOutputStream stream, boolean is_clear) {
		if (is_clear)
			stream.dispose();
	}

	public void receiveDamage(L1Character attacker, int damage) {

	}

	public int Desperadolevel = 0;

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_CHARACTER : _instanceType;
	}

	public void sendPackets(ServerBasePacket sbp) {
		sbp.clear();
		sbp = null;
	}

	protected SpriteInformation _currentSpriteInfo;

	public int getCurrentSpriteId() {
		return _currentSpriteInfo == null ? 1120 : _currentSpriteInfo.getSpriteId();
	}

	public SpriteInformation getCurrentSprite() {
		return _currentSpriteInfo;
	}

	public void setCurrentSprite(int spriteId) {
		if (!equalsCurrentSprite(spriteId))
//			System.out.println(spriteId);
			_currentSpriteInfo = SpriteInformationLoader.getInstance().get(spriteId);
	}

	public boolean equalsCurrentSprite(int compareSpriteId) {
//		System.out.println(getCurrentSpriteId()+"+"+compareSpriteId+"+"+ SpriteInformationLoader.getInstance().get(compareSpriteId).getSpritType());
		return getCurrentSpriteId() == compareSpriteId;
	}

	public long getCurrentSpriteInterval(EActionCodes actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}

	public long getCurrentSpriteInterval(int actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}

	public void sendShape(int poly) {
		S_ChangeShape shape = new S_ChangeShape(getId(), poly, 0);
		sendPackets(shape, false);
		broadcastPacket(shape);
	}

	private boolean _isLock;

	public boolean isLock() {
		return _isLock;
	}

	public void setLock(boolean b) {
		_isLock = b;
	}

	/*
	 * public int add_missile_critical_rate(int rate) { return _missile_critical_rate += rate; }
	 */

	/*
	 * public int add_missile_critical_rate(int rate) { _missile_critical_rate += rate; if (_missile_critical_rate < 0) { new Throwable(_missile_critical_rate + "").printStackTrace(); } return _missile_critical_rate; }
	 */

	public void set_missile_critical_rate(int rate) {
		_missile_critical_rate = rate;
	}

	public int add_missile_critical_rate(int rate) {
		int old_missile_critical_rate = _missile_critical_rate;
		if (_missile_critical_rate < 0) {
			_missile_critical_rate = old_missile_critical_rate;
		}
		return _missile_critical_rate += rate;
	}

	public int get_missile_critical_rate() {
		return _missile_critical_rate;
	}

	public void set_melee_critical_rate(int rate) {
		_melee_critical_rate = rate;
	}

	/*
	 * public int add_melee_critical_rate(int rate) { return _melee_critical_rate += rate; }
	 */

	public int get_melee_critical_rate() {
		return _melee_critical_rate;
	}

	public int add_melee_critical_rate(int rate) {
		int old_melee_critical_rate = _melee_critical_rate;
		if (_melee_critical_rate < 0) {
			_melee_critical_rate = old_melee_critical_rate;
		}
		return _melee_critical_rate += rate;
	}

	public void set_magic_critical_rate(int rate) {
		_magic_critical_rate = rate;
	}

	public int get_magic_critical_rate() {
		return _magic_critical_rate;
	}

	public int add_magic_critical_rate(int rate) {
		int old_magic_critical_rate = _magic_critical_rate;
		if (_magic_critical_rate < 0) {
			_magic_critical_rate = old_magic_critical_rate;
		}
		return _magic_critical_rate += rate;
	}

	public int get_CC_Increase() {
		return _CC_Increase;
	}
	public int add_CC_Increase(int i) {
		return _CC_Increase += i;
	}

	public int get_final_burn_critical_rate() {
		if (!isPassive(MJPassiveID.FINAL_BURN.toInt()) || getCurrentHpPercent() > 70)
			return 0;
		int level = getLevel();
		int rate = 5;
		if (level >= 92) {
			rate += (getLevel()-90)/2 + 2;
		}
		if (rate >= 15) {
			rate = 15;
		}
		return rate;
	}

	public void send_party_effect(int effect_id) {
		L1PcInstance _owner = (L1PcInstance) this;
		L1Party party = _owner.getParty();
		if (effect_id > 0) {
			S_SkillSound sound = new S_SkillSound(getId(), effect_id);
			sendPackets(sound, false);

			if (party != null) {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
					if (party == pc.getParty())
						pc.sendPackets(sound, false);
				}
			}
			sound.clear();
		}
	}

	public void send_other_party_effect(L1PcInstance attacker, int effect_id) {
		L1Party party = attacker.getParty();
		if (effect_id > 0) {
			S_SkillSound sound = new S_SkillSound(getId(), effect_id);
			attacker.sendPackets(sound, false);

			if (party != null) {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
					if (party == pc.getParty())
						pc.sendPackets(sound, false);
				}
			}
			sound.clear();
		}
	}

	public void send_tarobj_party_effect(int target, int effect_id) {
		L1PcInstance _owner = (L1PcInstance) this;
		L1Party party = _owner.getParty();
		if (effect_id > 0) {
			S_SkillSound sound = new S_SkillSound(target, effect_id);
			sendPackets(sound, false);

			if (party != null) {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
					if (party == pc.getParty())
						pc.sendPackets(sound, false);
				}
			}
			sound.clear();
		}
	}

	// TODO true 혹은 빈칸 = 다같이 보임 false = 자신만 보임
	public void send_effect(int effect_id) {
		send_effect(effect_id, true);
	}

	public void send_effect(int effect_id, boolean check) {
		if (effect_id > 0) {
			S_SkillSound sound = new S_SkillSound(getId(), effect_id);
			sendPackets(sound, false);

			if (check != false) {
				broadcastPacket(sound, true);
			} else {
				sound.clear();
			}
		}
	}

	public void send_effect(boolean run, int effect_id, int remaining_seconds) {
		sendPackets(new S_PacketBox(run, effect_id, remaining_seconds), true);
	}

	public void send_action(int action_id) {
		if (action_id > 0) {
			S_DoActionGFX gfx = new S_DoActionGFX(getId(), action_id);
			sendPackets(gfx, false);
			broadcastPacket(gfx, true);
		}
	}

	public void send_pink_name(int remain_seconds) {
		S_PinkName pnk = new S_PinkName(getId(), remain_seconds);
		sendPackets(pnk, false);
		broadcastPacket(pnk, true);
	}

	public void send_lawful() {
		S_Lawful pck = new S_Lawful(getId(), getLawful());
		sendPackets(pck, false);
		broadcastPacket(pck);
	}

	private static final int[] _elf_skill_braves = new int[] { L1SkillId.DANCING_BLADES, L1SkillId.SAND_STORM, L1SkillId.HURRICANE, L1SkillId.FOCUS_WAVE };

	public void remove_elf_second_brave() {
		for (int skillId : _elf_skill_braves) {
			if (hasSkillEffect(skillId)) {
				removeSkillEffect(skillId);
				S_SkillBrave brave = new S_SkillBrave(getId(), 0, 0);
				sendPackets(brave, false);
				broadcastPacket(brave, true);
				setBraveSpeed(0);
			}
		}
	}

	public boolean is_assassination_level2() {
		return getKDA() == null ? false : getKDA().is_assassination_level2();
	}

	public boolean is_assassination_level1() {
		return getKDA() == null ? false : getKDA().is_assassination_level1();
	}

	private L1DollInstance _doll;

	public L1DollInstance getMagicDoll() {
		return _doll;
	}

	public void setMagicDoll(L1DollInstance doll) {
		_doll = doll;
	}

	private int _foudmg;

	public int getFouDmg() {
		return _foudmg;
	}

	public void addFouDmg(int i) {
		_foudmg += i;
	}

	private int _reduc_cancel;

	public int getReducCancel() {
		return _reduc_cancel;
	}

	public void addReducCancel(int i) {
		_reduc_cancel += i;
	}

	// TODO 몬스터 트리플 에로우
	public boolean MoBTripleArrow = false;
	public boolean MoBTripleArrow_PRISON = false;

	public int getBlessAinEfficiency() { return _blessAinEfficiency; }

	public void addBlessAinEfficiency(int i) {
		try {
			_blessAinEfficiency += i;
			if ((this instanceof L1PcInstance)) {
				L1PcInstance pc = (L1PcInstance) this;
				if (pc.getAI() != null) {
					return;
				}
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EINHASAD, pc));
				SC_EXP_BOOSTING_INFO_NOTI.send(pc);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onAction(L1Character attacker) {
		if (attacker == null) {
			return;
		}

		if (getZoneType() == 1 || attacker.getZoneType() == 1) {
			L1Attack attack_mortion = new L1Attack(attacker, this);
			attack_mortion.action();
			return;
		}

		if (getCurrentHp() > 0 && !isDead()) {
			boolean isMortalBody = false;
			L1Attack attack = new L1Attack(attacker, this);
			L1Magic magic = null;

			if (attack.calcHit()) {
				if (hasSkillEffect(L1SkillId.MORTAL_BODY)) {
					magic = new L1Magic(this, attacker);
					boolean isProbability = magic.calcProbabilityMagic(L1SkillId.MORTAL_BODY);
					boolean isShortDistance1 = attack.isShortDistance1();
					if (isProbability && isShortDistance1) {
						isMortalBody = true;
					}
				}
				if (!isMortalBody) {
					attack.calcDamage();
					attack.addPcPoisonAttack(attacker, this);
				}
			}

			if (isMortalBody) {
				attack.calcDamage();
				attack.actionMortalBody();
				attack.commitMortalBody();
				attack.commit();
			} else {
				attack.action();
				attack.commit();
			}
		}
	}

	private double _move_delay_rate;
	private double _attack_delay_rate;
	private double _magic_delay_rate;

	public double getMoveDelayRate() {
		return _move_delay_rate;
	}

	public void setMoveDelayRate(double move_delay_rate) {
		_move_delay_rate = move_delay_rate;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED, (int) _move_delay_rate), true,  true);
		}
	}

	public void addMoveDelayRate(double move_delay_rate) {
		_move_delay_rate += move_delay_rate;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED, (int) _move_delay_rate), true,  true);
		}
	}

	public double getAttackDelayRate() {
		return _attack_delay_rate;
	}

	public void setAttackDelayRate(double attack_delay_rate) {
		_attack_delay_rate = attack_delay_rate;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED, (int) _attack_delay_rate), true,  true);
		}
	}

	public void addAttackDelayRate(double attack_delay_rate) {
		_attack_delay_rate += attack_delay_rate;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED, (int) _attack_delay_rate), true,  true);
		}
	}

	public double getMagicDelayRate() {
		return _magic_delay_rate;
	}

	public void setMagicDelayRate(double magic_delay_rate) {
		_magic_delay_rate = magic_delay_rate;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.SPELL_SPEED, (int) _magic_delay_rate), true,  true);
		}
	}

	public void addMagicDelayRate(double magic_delay_rate) {
		_magic_delay_rate += magic_delay_rate;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.SPELL_SPEED, (int) _magic_delay_rate), true,  true);
		}
	}

	private int _emblemId;

	public int getEmblemId() {
		return _emblemId;
	}

	public void setEmblemId(int i) {
		_emblemId = i;
	}

	private int _blessAinEfficiency;

	public L1PcInstance _EternitiAttacker;

	private boolean _RedknightType;

	public boolean getRedknightType() {
		return _RedknightType;
	}

	public void setRedknightType(boolean flag) {
		_RedknightType = flag;
	}

	private int _halpas_faith_pvp_reduc;

	private boolean _halpas_armor;

	private int _halpas_armor_enchant;

	public int get_halpas_faith_pvp_reduc() {
		return _halpas_faith_pvp_reduc;
	}

	public void set_halpas_faith_pvp_reduc(int _halpas_faith_pvp_reduc) {
		this._halpas_faith_pvp_reduc = _halpas_faith_pvp_reduc;
	}

	public boolean is_halpas_armor() {
		return _halpas_armor;
	}

	public void set_halpas_armor(boolean _halpas_armor) {
		this._halpas_armor = _halpas_armor;
	}

	public int get_halpas_armor_enchant() {
		return _halpas_armor_enchant;
	}

	public void set_halpas_armor_enchant(int _halpas_armor_enchant) {
		this._halpas_armor_enchant = _halpas_armor_enchant;
	}

	private boolean _reduction_armor_veteran;

	public boolean is_reduction_armor_veteran() {
		return _reduction_armor_veteran;
	}

	public void set_reduction_armor_veteran(boolean _reduction_armor_veteran) {
		this._reduction_armor_veteran = _reduction_armor_veteran;
	}

	private int _reducreduction_value;

	public int get_reducreduction_value() {
		return _reducreduction_value;
	}

	public void set_reducreduction_value(int _reducreduction_value) {
		this._reducreduction_value = _reducreduction_value;
	}

	private boolean lucifer_destiny;

	public boolean isLucifer_destiny() {
		return lucifer_destiny;
	}

	public void setLucifer_destiny(boolean lucifer_destiny) {
		this.lucifer_destiny = lucifer_destiny;
	}

	public int getTempCharGfx() {return _tempCharGfx;}
	public void setTempCharGfx(int i) {_tempCharGfx = i;}

	public int getGfxId() {return _gfxid;}
	public void setGfxId(int i) {_gfxid = i;}



	private boolean SHADOW_ARMOR_destiny;

	public boolean isSHADOW_ARMOR_destiny() {
		return SHADOW_ARMOR_destiny;
	}

	public void setSHADOW_ARMOR_destiny(boolean SHADOW_ARMOR_destiny) {
		this.SHADOW_ARMOR_destiny = SHADOW_ARMOR_destiny;
	}

	private boolean immunetoharm_saini;

	public boolean isImmunetoharm_saini() {
		return immunetoharm_saini;
	}

	public void setImmunetoharm_saini(boolean immunetoharm_saini) {
		this.immunetoharm_saini = immunetoharm_saini;
	}

	private int _acurucy_meister;

	public int get_acurucy_meister() {
		return _acurucy_meister;
	}

	public void set_acurucy_meister(int _acurucy_meister) {
		this._acurucy_meister = _acurucy_meister;
	}

	public int get_shining_shild_obj_id() {
		return _shining_shild_obj_id;
	}

	public void set_shining_shild_obj_id(int _shining_shild_obj_id) {
		this._shining_shild_obj_id = _shining_shild_obj_id;
	}

	private int _shining_shild_obj_id;

	private int _dead_count = 0;

	public int get_dead_count() {
		return _dead_count;
	}

	public int add_dead_count(int rate) {
		return _dead_count += rate;
	}

	public void set_dead_count(int i) {
		_dead_count = i;
	}

	private int _exp_count = 0;

	public int get_exp_count() {
		return _exp_count;
	}

	public int add_exp_count(int rate) {
		return _exp_count += rate;
	}

	public void set_exp_count(int i) {
		_exp_count = i;
	}

	private int monsterkill = 0;

	public int getMonsterkill() {
		return monsterkill;
	}

	public void setMonsterkill(int monster) {
		monsterkill = monster;
	}

	public void addMonsterKill(int i) {
		monsterkill += i;
	}

	public void broadcastPacketForFindInvis(ServerBasePacket packet, boolean isFindInvis) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (isFindInvis) {
				if (pc.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)) {
					pc.sendPackets(packet);
				} else if (this instanceof L1PcInstance) {
					L1PcInstance owner = (L1PcInstance) this;
					if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) {
						pc.sendPackets(packet);
					}
				} else if (this instanceof L1DollInstance) {
					L1DollInstance doll = (L1DollInstance) this;
					L1PcInstance owner = (L1PcInstance) doll.getMaster();
					if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) {
						pc.sendPackets(packet);
					}
				}
			} else if (this instanceof L1PcInstance) {
				L1PcInstance owner = (L1PcInstance) this;
				if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) {
					continue;
				}
				if (!pc.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE))
					pc.sendPackets(packet);
			} else if (this instanceof L1DollInstance) {
				L1DollInstance doll = (L1DollInstance) this;
				L1PcInstance owner = (L1PcInstance) doll.getMaster();
				if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) {
					continue;
				}
				if (!pc.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE))
					pc.sendPackets(packet);
			} else if (!pc.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)) {
				pc.sendPackets(packet);
			} else if (pc.getMapId() == 63){
				if (pc.hasSkillEffect(L1SkillId.WATER_MIRROR)){
					if (this instanceof L1NpcInstance || this instanceof L1MonsterInstance){
						continue;
					}
				}
			}
		}
	}

	public void broadcastPacketForFindInvis(ProtoOutputStream packet, boolean isFindInvis) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (isFindInvis) {
				if (pc.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)) {
					pc.sendPackets(packet);
				} else if (this instanceof L1PcInstance) {
					L1PcInstance owner = (L1PcInstance) this;
					if (pc.isInParty() && pc.getParty().isMember(owner)) {
						pc.sendPackets(packet);
					}
					if (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())
						pc.sendPackets(packet);
				}
			} else if (this instanceof L1PcInstance) {
				L1PcInstance owner = (L1PcInstance) this;
				if (pc.isInParty() && pc.getParty().isMember(owner))
					continue;
				if (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())
					continue;
				if (!pc.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE))
					pc.sendPackets(packet);
			} else if (!pc.hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)) {
				pc.sendPackets(packet);
			}
		}
	}

	private L1PcInstance _presher_pc = null;

	public L1PcInstance getPresherPc() {
		return _presher_pc;
	}

	public void setPresherPc(L1PcInstance pc) {
		_presher_pc = pc;
	}

	private int _presher_dmg = 0;

	public int getPresherDamage() {
		return _presher_dmg;
	}

	public void setPresherDamage(int dmg) {
		_presher_dmg = dmg;
	}

	public void addPresherDamage(int dmg) {
		_presher_dmg += dmg;
	}

	private boolean _resher_death_recall = false;

	public boolean getPresherDeathRecall() {
		return _resher_death_recall;
	}

	public void setPresherDeathRecall(boolean flag) {
		_resher_death_recall = flag;
	}

	private boolean _shadow_step_chaser = false;

	public boolean getshadowstepchaser() {
		return _shadow_step_chaser;
	}

	public void setShadowstepchaser(boolean flag) {
		_shadow_step_chaser = flag;
	}



	private boolean _Maelstrom = false;

	public boolean get_Maelstrom() {
		return _Maelstrom;
	}

	public void set_Maelstrom(boolean flag) {
		_Maelstrom = flag;
	}

	private final Map<Integer, ItemDelayTimer> _item_delay = new ConcurrentHashMap<Integer, ItemDelayTimer>();

	public boolean addItemDelayTime(L1ItemInstance item) {
		if (item == null)
			return false;

		if (item.getItem().get_delaytime() > 0) {
			int itemId = SameTypeCheck(item);
			int delayTime = item.getItem().get_delaytime();

			if (item.getItem().getType2() == 0 && item.getItem().getType() == 6) {
				AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(getId());
				if (Info != null) {
					if (Info.get_potion() != 0) {
						delayTime = Info.get_potion_val_2();
					}
				}
			}

			L1PcInstance pc = (L1PcInstance) this;
			if (item.getItem().getType2() == 1) {
				return false;
			} else if (item.getItem().getType2() == 2) {
				if (item.getItem().getItemId() == 20077 || item.getItem().getItemId() == 20062 || item.getItem().getItemId() == 120077) {
					if (!pc.isInvisble()) {
						L1ItemInstance clock = pc.getInventory().getItemEquipped(2, 4);
						if (clock == null) {
							pc.beginInvisTimer();
						}
					}
				} else {
					return false;
				}
			}

			ItemDelayTimer timer = ItemDelayTimer.newTimer(this, itemId, item.getName(), delayTime);
			_item_delay.put(itemId, timer);
			timer.begin();
			return true;
		}
		return false;
	}

	public boolean addItemDelayTime(int itemid, String itemName, long time) {
		if (time > 0) {
			ItemDelayTimer timer = ItemDelayTimer.newTimer(this, itemid, itemName, time);
			_item_delay.put(itemid, timer);
			timer.begin();
			return true;
		}
		return false;
	}

	public void removeItemDelayTime(int itemid) {
		ItemDelayTimer timer = _item_delay.remove(itemid);
		if (timer != null) {
			timer.end();
		}
	}

	public boolean hasItemDelayTime(int ItemId) {
		return _item_delay.containsKey(ItemId);
	}

	public boolean hasItemDelayTime(L1ItemInstance item) {
		if (item == null)
			return false;

		int itemId = SameTypeCheck(item);
		return _item_delay.containsKey(itemId);
	}

	public int getItemDelayTimeSec(int ItemId) {
		ItemDelayTimer timer = _item_delay.get(ItemId);
		if (timer == null) {
			return 0;
		}
		return timer.remainingSeconds();
	}

	public Timestamp getItemDelayLogTime(int ItemId) {
		ItemDelayTimer timer = _item_delay.get(ItemId);
		if (timer == null) {
			return null;
		}
		return timer.LogDelayTime();
	}

	// c_itemuse 에서는 이걸로 사용한다.
	public Timestamp getItemDelayLogTime(L1ItemInstance item) {
		int itemId = SameTypeCheck(item);
		ItemDelayTimer timer = _item_delay.get(itemId);
		if (timer == null) {
			return null;
		}
		return timer.LogDelayTime();
	}

	public Collection<ItemDelayTimer> hasItemDelayTimeList() {
		if (_item_delay == null)
			return null;

		return _item_delay.values();
	}

	// 아이템 타입에 따른 조건.
	public int SameTypeCheck(L1ItemInstance item) {
		int itemid = item.getId();

		if (item.getItem().getType2() == 0 && item.getItem().getType() == 6) {
			// 물약 계열 아이템들은 아이템아이디를 체력 회복제로 판별한다.
			itemid = 40010;
		}

		return itemid;
	}

	private boolean _striker_gail_shot = false;

	public boolean isStrikerGailShot() {
		return _striker_gail_shot;
	}

	public void setStrikerGailShot(boolean value) {
		_striker_gail_shot = value;
	}

	private L1PcInstance _TomaHawkHunter = null;

	public L1PcInstance getTomahawkHunter() {
		return _TomaHawkHunter;
	}

	public void setTomahawkHunter(L1PcInstance value) {
		_TomaHawkHunter = value;
	}
	public boolean _dominion_tel;

	public void set_dominion_tel(int i){
		if (i == 1){
			_dominion_tel = true;
		}else {
			_dominion_tel = false;
		}
	}

	public boolean is_dominion_tel(){
		return _dominion_tel;
	}

	private int _armor_break_attacker_id;
	public void set_Armor_break_Attacker(int i) {
		_armor_break_attacker_id = i;
	}
	public int get_Armor_break_Attacker() {
		return _armor_break_attacker_id;
	}

	public boolean isShokAttackTeleport(){
		return hasSkillEffect(L1SkillId.SHOCK_ATTACK);
	}

	public boolean isEternity() {
		return hasSkillEffect(L1SkillId.ETERNITI);
	}

	public boolean isShadowStepChaser() {
		return hasSkillEffect(L1SkillId.SHADOW_STEP_CHASER);
	}

	public boolean isDesperado() {
		return hasSkillEffect(L1SkillId.DESPERADO);
	}

	public boolean isDeathRecall(){
		if (!getPresherDeathRecall()) {
			return false;
		}
		if(this instanceof L1PcInstance) {
			//_pressureDmg *= (Config.데스리콜대미지 * 0.01);
			((L1PcInstance) this).receiveDamage(this, 500);
		}
		return true;
	}

	/*public boolean isPhantom() {
		return (hasSkillEffect(L1SkillId.STATUS_PHANTOM_NOMAL) || getSkill().hasSkillEffect(L1SkillId.STATUS_PHANTOM_RIPER) || getSkill().hasSkillEffect(L1SkillId.STATUS_PHANTOM_DEATH) || getSkill().hasSkillEffect(L1SkillId.STATUS_PHANTOM_REQUIEM));
	}*/
	public boolean isStop(){
		return hasSkillEffect(L1SkillId.SHOCK_STUN) /*|| hasSkillEffect(L1SkillId.STATUS_STUN)*/ || hasSkillEffect(L1SkillId.FORCE_STUN) || hasSkillEffect(L1SkillId.BONE_BREAK) || hasSkillEffect(L1SkillId.EMPIRE)
				|| hasSkillEffect(L1SkillId.PANTHERA) || hasSkillEffect(L1SkillId.PANTERA_SHOCK)/*|| hasSkillEffect(L1SkillId.STATUS_PANTERA) || hasSkillEffect(L1SkillId.STATUS_PANTERA_SHOCK)*/ || hasSkillEffect(L1SkillId.DISINTEGRATE)
				|| hasSkillEffect(L1SkillId.CRUEL) || hasSkillEffect(L1SkillId.TEMPEST) || hasSkillEffect(L1SkillId.TEMPEST)
				|| hasSkillEffect(L1SkillId.EARTH_BIND) || hasSkillEffect(L1SkillId.ICE_LANCE) || hasSkillEffect(L1SkillId.MOB_COCA) || hasSkillEffect(L1SkillId.MOB_BASILL)
				|| hasSkillEffect(L1SkillId.MOB_SHOCKSTUN_30) || hasSkillEffect(L1SkillId.MOB_RANGESTUN_18) || hasSkillEffect(L1SkillId.MOB_RANGESTUN_19) || hasSkillEffect(L1SkillId.MOB_RANGESTUN_20)
				|| hasSkillEffect(L1SkillId.ANTA_MESSAGE_6) || hasSkillEffect(L1SkillId.ANTA_MESSAGE_7) || hasSkillEffect(L1SkillId.ANTA_MESSAGE_8) || hasSkillEffect(L1SkillId.ANTA_SHOCKSTUN)
				;
	}
	public boolean isNotTeleport(){
		return isDead() || isStop() || isParalyzed() || isSleeped() || isDeathRecall() || isDesperado() || /*isPhantom() ||*/ isShokAttackTeleport() || isEternity() || isShadowStepChaser();
	}


	public Object getId() {
		return null;
	}
}

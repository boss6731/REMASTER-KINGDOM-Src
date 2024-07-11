 package l1j.server.server.model;

 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Random;
 import java.util.Set;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
 import l1j.server.Config;
 import l1j.server.MJ3SEx.EActionCodes;
 import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
 import l1j.server.MJ3SEx.SpriteInformation;
 import l1j.server.MJBotSystem.AI.MJBotAI;
 import l1j.server.MJDShopSystem.MJDShopItem;
 import l1j.server.MJDShopSystem.MJDShopStorage;
 import l1j.server.MJKDASystem.MJKDA;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPEED_BONUS_NOTI;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1FollowerInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;
 import l1j.server.server.model.item.itemdelay.ItemDelayTimer;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.poison.L1Poison;
 import l1j.server.server.model.skill.timer.L1SkillTimer;
 import l1j.server.server.serverpackets.S_ACTION_UI;
 import l1j.server.server.serverpackets.S_ChangeShape;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_HPUpdate;
 import l1j.server.server.serverpackets.S_Lawful;
 import l1j.server.server.serverpackets.S_MPUpdate;
 import l1j.server.server.serverpackets.S_PacketBox;
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









 public class L1Character
   extends L1Object
 {
   private static final long serialVersionUID = 1L;
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
   private int _tempCharGfx;
   private int _gfxid;
   private int _heading;
   private int _moveSpeed;
   private int _braveSpeed;
   private L1Poison _poison = null;

   private boolean _paralyzed;

   private boolean _sleeped;
   private L1Paralysis _paralysis;
   private boolean _isDead;
   protected Light light = null;
   private MoveState moveState;
   protected Ability ability = null;
   protected Resistance resistance = null;

   protected AC ac = null;

   private boolean _isSkillDelay = false;

   private boolean _isLinkSkillDelay = false;

   private int _addAttrKind;

   private int _status;
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


   private static Random _rnd = new Random(System.nanoTime());
   private final Map<Integer, L1NpcInstance> _petlist = new HashMap<>();
   private final Map<Integer, L1DollInstance> _dolllist = new HashMap<>();
   private final Map<Integer, L1SkillTimer> _skillEffect = new ConcurrentHashMap<>();
   private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap<>();
   private final Map<Integer, L1FollowerInstance> _followerlist = new HashMap<>();


   private final Map<Integer, L1Object> _knownObjects = new ConcurrentHashMap<>();
   private final Map<Integer, L1PcInstance> _knownPlayer = new ConcurrentHashMap<>(); private ConcurrentHashMap<Integer, Integer> m_classResistance; private ConcurrentHashMap<Integer, Integer> m_classPierce; private int _effectedDG; private int _characterDG; private int _effectedER; private int _characterER; private int _currentHpDB; private int _currentMpDB; private boolean _thunderGrab; public int _reduction_per; private long _doppeltime; private boolean _invisble; private MJKDA _kda; private long _skilldelay2; private int _buffnoch; private int _mr; private int _trueMr; private int 락구간상승; private ArrayList<MJDShopItem> _sellings; private ArrayList<MJDShopItem> _purchasings; private MJBotAI _botAI; public int _truetarget; public int _truetarget_clan; public int _truetarget_party; public int Desperadolevel;

   public boolean isChangedHp() {
     return (this._currentHp != this._prevHp);
   }

   public boolean isChangedMp() {
     return (this._currentMp != this._prevMp);
   }

   public boolean isChangedHpAndUpdate() {
     boolean isChanged = isChangedHp();
     if (isChanged)
       this._prevHp = this._currentHp;
     return isChanged;
   }

   public boolean isChangedMpAndUpdate() {
     boolean isChanged = isChangedMp();
     if (isChanged)
       this._prevMp = this._currentMp;
     return isChanged;
   }










   public boolean isPassive(int passiveId) {
     return false;
   }

   public void addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
     int val = getSpecialResistance(kind);

     if (this.m_classResistance == null)
       this.m_classResistance = new ConcurrentHashMap<>(4);
     this.m_classResistance.put(Integer.valueOf(kind.toInt()), Integer.valueOf(value + val));
   }

   public int getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
     if (this.m_classResistance == null || !this.m_classResistance.containsKey(Integer.valueOf(kind.toInt()))) {
       return 0;
     }
     return ((Integer)this.m_classResistance.get(Integer.valueOf(kind.toInt()))).intValue();
   }

   public ConcurrentHashMap<Integer, Integer> getSpecialResistanceMap() {
     return this.m_classResistance;
   }

   public void addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {
     int val = getSpecialPierce(kind);
     if (this.m_classPierce == null) {
       this.m_classPierce = new ConcurrentHashMap<>(4);
     }
     this.m_classPierce.put(Integer.valueOf(kind.toInt()), Integer.valueOf(value + val));
   }

   public int getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
     if (this.m_classPierce == null || !this.m_classPierce.containsKey(Integer.valueOf(kind.toInt()))) {
       return 0;
     }
     return ((Integer)this.m_classPierce.get(Integer.valueOf(kind.toInt()))).intValue();
   }

   public ConcurrentHashMap<Integer, Integer> getSpecialPierceMap() {
     return this.m_classPierce;
   }

   public void dispose() {
     if (this.resistance != null) {
       this.resistance.dispose();
       this.resistance = null;
     }

     if (this.light != null) {
       this.light.dispose();
       this.light = null;
     }

     disposeShopInfo();
     this._petlist.clear();
     this._dolllist.clear();
     clearSkillEffectTimer();
     this._followerlist.clear();
     this._itemdelay.clear();
   }










   public double getCurrentHpPercent() {
     return 100.0D / getMaxHp() * getCurrentHp();
   }

   public double getCurrentMpPercent() {
     return 100.0D / getMaxMp() * getCurrentMp();
   }

   public int getLongLocation() {
     int pt = getX() << 16 & 0xFFFF0000;
     pt |= getY() & 0xFFFF;
     return pt;
   }

   public int getLongLocationReverse() {
     int pt = getY() << 16 & 0xFFFF0000;
     pt |= getX() & 0xFFFF;
     return pt;
   }

   public L1Character() { this._effectedDG = 0;
     this._characterDG = 0;
     this._effectedER = 0;
     this._characterER = 0;





     this._doppeltime = 0L;






     this._mr = 0;
     this._trueMr = 0;






     this._truetarget = 0;









     this._truetarget_clan = 0;









     this._truetarget_party = 0;




     this.Desperadolevel = 0;





     this.MoBTripleArrow = false;
     this.MoBTripleArrow_PRISON = false;





     this._dead_count = 0;






     this._exp_count = 0;







     this.monsterkill = 0;





     this._presher_pc = null;




     this._presher_dmg = 0;






     this._resher_death_recall = false;





     this._shadow_step_chaser = false;





     this._Maelstrom = false;





     this._item_delay = new ConcurrentHashMap<>();



     this._striker_gail_shot = false;









     this._TomaHawkHunter = null; this._level = 1; this.ability = new Ability(this); this.resistance = new Resistance(this); this.ac = new AC(this); this.moveState = new MoveState(); this.light = new Light(this); }
   public void addDg(int i) { this._effectedDG += i; }
   public boolean setCharacterDG(int i) { int old = this._characterDG; this._characterDG = i; return (old != i); }
   public int getDg() { int Level = getLevel(); int point = 0; if (this != null && isPassive(MJPassiveID.INFINITI_DODGE.toInt())) { if (Level < 70) Level = 70;  point = (Level - 70) / 2 * 2 + 1; if (point > 15) point = 15;  }  if (hasSkillEffect(246)) point = (int)(point + (this._effectedDG + this._characterDG + point) * 0.2D);  if (this != null && isPassive(MJPassiveID.SHINING_ARMOR.toInt())) { int levpoint = 0; if (Level > 90) levpoint = (Level - 90) / 2;  if (levpoint > 5) levpoint = 5;  point += levpoint + 5; }  if (this != null && isPassive(MJPassiveID.TACTICAL_ADVANCE.toInt())) { if (Level < 90) Level = 90;  point = (int)(point + 5.0D + ((Level - 90) / 2) * Config.MagicAdSetting_Lancer.TACTICAL_ADVANCE_VAL); if (point > 15) point = 15;  }  return this._effectedDG + this._characterDG + point; } public boolean setCharacterER(int i) { int old = this._characterER; this._characterER = i; return (old != i); } public int getStatER() { int er = 0; er = getAbility().getTotalDex() / 2; return er; } public int getDefaultER() { int er = 0; int BaseEr = CalcStat.ER(getAbility().getTotalDex()); er += BaseEr; return er; } public int getEffectedER() { return this._effectedER; } public void addEffectedER(int i) { this._effectedER += i; sendPackets((ServerBasePacket)new S_PacketBox(132, getTotalER())); } public int getTotalER() { int Level = getLevel(); int er = 0; int point = 0; er += getDefaultER(); er += getEffectedER(); er += this._characterER; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; er += CalcStat.calcErByLevel(pc.getType(), pc.getLevel()); }  if (er < 0) er = 0;  if (this != null && isPassive(MJPassiveID.INFINITI_BULLET.toInt())) { if (Level < 75) Level = 75;  point = Level - 75 + 1; if (point > 15) point = 15;  }  if (hasSkillEffect(246)) er = (int)(er + (er + point) * 0.2D);  if (this != null && isPassive(MJPassiveID.DRESS_EVASION_PASSIVE.toInt())) point = 18;  if (this != null && isPassive(MJPassiveID.TACTICAL_ADVANCE.toInt())) { if (Level < 80) Level = 80;  point += 3 + (Level - 80) / 3 * 2; if (point > 15) point = 15;  }  if (this != null && isPassive(MJPassiveID.SHINING_ARMOR.toInt())) { int levpoint = 0; if (Level > 90) levpoint = (Level - 90) / 2;  if (levpoint > 5) levpoint = 5;  point += levpoint + 5; }  if (hasSkillEffect(174)) er /= 3;  return er + point; } public void resurrect(int hp) { if (!isDead()) return;  if (hp <= 0) hp = 1;  setCurrentHp(hp); setDead(false); setStatus(0); L1PolyMorph.undoPoly(this); for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) { pc.sendPackets((ServerBasePacket)new S_RemoveObject(this)); pc.removeKnownObject(this); pc.updateObject(); }  } public int getCurrentHp() { return this._currentHp; } public void setCurrentHp(int i) { if (i >= getMaxHp()) i = getMaxHp();  if (i < 0) i = 0;  this._prevHp = this._currentHp; this._currentHp = i; } public void setCurrentHpDB(int i) { this._currentHpDB = i; } public int getCurrentHpDB() { return this._currentHpDB; } public void setCurrentMpDB(int i) { this._currentMpDB = i; } public int getCurrentMpDB() { return this._currentMpDB; } public int getCurrentMp() { return this._currentMp; } public void setCurrentMp(int i) { if (i >= getMaxMp()) i = getMaxMp();  if (i < 0) i = 0;  this._prevMp = this._currentMp; this._currentMp = i; } public boolean isSleeped() { return this._sleeped; } public void setSleeped(boolean sleeped) { this._sleeped = sleeped; } public boolean isParalyzed() { return this._paralyzed; } public void setParalyzed(boolean paralyzed) { this._paralyzed = paralyzed; } public boolean isThunderGrab() { return this._thunderGrab; } public void setThunderGrab(boolean thunderGrab) { this._thunderGrab = thunderGrab; } public L1Paralysis getParalysis() { return this._paralysis; } public void setParalaysis(L1Paralysis p) { this._paralysis = p; } public void cureParalaysis() { if (this._paralysis != null) this._paralysis.cure();  } public void broadcastPacket(MJIProtoMessage message, MJEProtoMessages message_id, boolean is_clear, boolean is_this_send) { if (message.isInitialized()) { broadcastPacket(message.writeTo(message_id), is_clear, is_this_send); } else { MJEProtoMessages.printNotInitialized("", message_id.toInt(), message.getInitializeBit()); }  } public void broadcastPacket(ProtoOutputStream stream) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) pc.sendPackets(stream, false);  stream.dispose(); } public void broadcastPacket(ProtoOutputStream stream, boolean is_clear, boolean is_this_send) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) pc.sendPackets(stream, false);  if (is_this_send) sendPackets(stream, false);  if (is_clear) stream.dispose();  } public void broadcastPacket(ProtoOutputStream[] streams, boolean is_clear, boolean is_this_send) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { for (ProtoOutputStream stream : streams) pc.sendPackets(stream, false);  }  if (is_this_send) for (ProtoOutputStream stream : streams) sendPackets(stream, false);   if (is_clear) for (ProtoOutputStream stream : streams) stream.dispose();   } public void broadcastPacket(ServerBasePacket[] pcs) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { for (int j = 0; j < pcs.length; j++) pc.sendPackets(pcs[j], false);  }  for (int i = 0; i < pcs.length; i++) pcs[i].clear();  } public void broadcastPacket(ServerBasePacket[] pcs, boolean isClear) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { for (int i = 0; i < pcs.length; i++) pc.sendPackets(pcs[i], false);  }  if (isClear) for (int i = 0; i < pcs.length; i++) pcs[i].clear();   } public void broadcastPacket(ServerBasePacket packet) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) pc.sendPackets(packet, false);  packet.clear(); } public void broadcastPacket(ServerBasePacket packet, boolean isClear) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) pc.sendPackets(packet, false);  if (isClear) packet.clear();  } public void broadcastPacket(ServerBasePacket packet, L1Character target) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) pc.sendPackets(packet, false);  packet.clear(); } public void broadcastPacket(ServerBasePacket packet, L1Character[] target) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) pc.sendPackets(packet, false);  packet.clear(); } public void broadcastPacketExceptTargetSight(ServerBasePacket packet, L1Character target) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayerExceptTargetSight(this, target)) { if (pc.knownsObject(this)) pc.sendPackets(packet, false);  }  packet.clear(); } public void wideBroadcastPacket(ServerBasePacket packet) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this, 50)) pc.sendPackets(packet, false);  packet.clear(); } public int calcheading(int myx, int myy, int tx, int ty) { int newheading = 0; if (tx > myx && ty > myy) newheading = 3;  if (tx < myx && ty < myy) newheading = 7;  if (tx > myx && ty == myy) newheading = 2;  if (tx < myx && ty == myy) newheading = 6;  if (tx == myx && ty < myy) newheading = 0;  if (tx == myx && ty > myy) newheading = 4;  if (tx < myx && ty > myy) newheading = 5;  if (tx > myx && ty < myy) newheading = 1;  return newheading; } public int[] getFrontLoc() { int[] loc = new int[2]; int x = getX(); int y = getY(); int heading = getHeading(); switch (heading) { case 0: y--; break;case 1: x++; y--; break;case 2: x++; break;case 3: x++; y++; break;case 4: y++; break;case 5: x--; y++; break;case 6: x--; break;case 7: x--; y--; break; }  loc[0] = x; loc[1] = y; return loc; } public int targetDirection(int tx, int ty) { float dis_x = Math.abs(getX() - tx); float dis_y = Math.abs(getY() - ty); float dis = Math.max(dis_x, dis_y); if (dis == 0.0F) return getHeading();  int avg_x = (int)Math.floor((dis_x / dis + 0.59F)); int avg_y = (int)Math.floor((dis_y / dis + 0.59F)); int dir_x = 0; int dir_y = 0; if (getX() < tx) dir_x = 1;  if (getX() > tx) dir_x = -1;  if (getY() < ty) dir_y = 1;  if (getY() > ty) dir_y = -1;  if (avg_x == 0) dir_x = 0;  if (avg_y == 0) dir_y = 0;  if (dir_x == 1 && dir_y == -1) return 1;  if (dir_x == 1 && dir_y == 0) return 2;  if (dir_x == 1 && dir_y == 1) return 3;  if (dir_x == 0 && dir_y == 1) return 4;  if (dir_x == -1 && dir_y == 1) return 5;  if (dir_x == -1 && dir_y == 0) return 6;  if (dir_x == -1 && dir_y == -1) return 7;  if (dir_x == 0 && dir_y == -1) return 0;  return getHeading(); } public boolean glanceCheck(int tx, int ty) { L1Map map = getMap(); int chx = getX(); int chy = getY(); int heading = targetDirection(tx, ty); for (int i = 0; i < 15; i++) { int cx = Math.abs(chx - tx); int cy = Math.abs(chy - ty); if (cx <= 1 && cy <= 1) break;  if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) return false;  if (chx < tx) { chx++; } else if (chx > tx) { chx--; }  if (chy < ty) { chy++; } else if (chy > ty) { chy--; }  }  return true; } public boolean isAttackPosition(int x, int y, int range) { if (range >= 7) { if (getLocation().getTileDistance(new Point(x, y)) > range) return false;  } else if (getLocation().getTileLineDistance(new Point(x, y)) > range) { return false; }  return glanceCheck(x, y); } public boolean isAttackPosition(L1Character target, int range) { if (range >= 7) { if (getLocation().getTileDistance(target.getLocation()) > range) return false;  } else if (getLocation().getTileLineDistance(target.getLocation()) > range) { return false; }  return (glanceCheck(target.getX(), target.getY()) && target.glanceCheck(getX(), getY())); } public L1Inventory getInventory() { return null; } private void addSkillEffect(int skillId, long timeMillis) { L1SkillTimer timer = null; timeMillis = Liberation_Time(this, skillId, timeMillis); timer = L1SkillTimer.newTimer(this, skillId, timeMillis); this._skillEffect.put(Integer.valueOf(skillId), timer); timer.begin(); } public long Liberation_Time(L1Character cha, int skillId, long time) { L1Skills debuff_skill = SkillsTable.getInstance().getTemplate(skillId); if (debuff_skill != null && debuff_skill.isDebuff() && hasSkillEffect(5113)) { time = (long)(time / Config.MagicAdSetting_Elf.LIBERATION_CO); sendPackets((ServerBasePacket)new S_SkillSound(cha.getId(), 19539)); broadcastPacket((ServerBasePacket)new S_SkillSound(cha.getId(), 19539)); }  return time; } public void setSkillEffect(int skillId, long timeMillis) { if (hasSkillEffect(skillId)) { long remainingTimeMills = getSkillEffectTimeSec(skillId) * 1000L; if (remainingTimeMills >= 0L && (remainingTimeMills < timeMillis || timeMillis == 0L)) { if (skillId == 230 || skillId == 5027) { removeSkillEffect(skillId); } else { killSkillEffectTimer(skillId); }  addSkillEffect(skillId, timeMillis); }  } else { addSkillEffect(skillId, timeMillis); }  } public void addSkillEffectTime(int skillId, long timeMillis) { long skilltime = 0L; if (hasSkillEffect(skillId)) { skilltime = getSkilleffect(skillId).timeMillis(); killSkillEffectTimer(skillId); addSkillEffect(skillId, skilltime + timeMillis); }  } public void removeSkillEffect(int skillId) { L1SkillTimer timer = this._skillEffect.remove(Integer.valueOf(skillId)); if (timer != null) timer.end();  } public void killSkillEffectTimer(int skillId) { L1SkillTimer timer = this._skillEffect.remove(Integer.valueOf(skillId)); if (timer != null) timer.kill();  } public void clearSkillEffectTimer() { for (L1SkillTimer timer : this._skillEffect.values()) { if (timer != null) timer.kill();  }  this._skillEffect.clear(); } public boolean hasSkillEffect(int skillId) { return this._skillEffect.containsKey(Integer.valueOf(skillId)); } public boolean hasSkillEffect(int[] skills) { for (int skill_id : skills) { if (hasSkillEffect(skill_id)) return true;  }  return false; } public boolean hasSkillEffect(Collection<Integer> skills) { for (Iterator<Integer> iterator = skills.iterator(); iterator.hasNext(); ) { int skill_id = ((Integer)iterator.next()).intValue(); if (hasSkillEffect(skill_id)) return true;  }  return false; } public int getSkillEffectTimeSec(int skillId) { L1SkillTimer timer = this._skillEffect.get(Integer.valueOf(skillId)); if (timer == null) return -1;  return timer.remainingSeconds(); } public L1SkillTimer getSkilleffect(int skillId) { return this._skillEffect.get(Integer.valueOf(skillId)); } public Set<Map.Entry<Integer, L1SkillTimer>> hasSkills() { return this._skillEffect.entrySet(); } public void setSkillDelay(boolean flag) { this._isSkillDelay = flag; } public boolean isSkillDelay() { return this._isSkillDelay; } public void setLinkSkillDelay(boolean flag) { this._isLinkSkillDelay = flag; } public boolean isLinkSkillDelay() { return this._isLinkSkillDelay; } public void addItemDelay(int delayId, L1ItemDelay.ItemDelayTimer timer) { this._itemdelay.put(Integer.valueOf(delayId), timer); } public void removeItemDelay(int delayId) { this._itemdelay.remove(Integer.valueOf(delayId)); } public boolean hasItemDelay(int delayId) { return this._itemdelay.containsKey(Integer.valueOf(delayId)); } public L1ItemDelay.ItemDelayTimer getItemDelayTimer(int delayId) { return this._itemdelay.get(Integer.valueOf(delayId)); } public void addPet(L1NpcInstance npc) { this._petlist.put(Integer.valueOf(npc.getId()), npc); sendPetCtrlMenu(npc, true); } public void removePet(L1NpcInstance npc) { this._petlist.remove(Integer.valueOf(npc.getId())); sendPetCtrlMenu(npc, true); } public Map<Integer, L1NpcInstance> getPetList() { return this._petlist; } public void addFollower(L1FollowerInstance follower) { this._followerlist.put(Integer.valueOf(follower.getId()), follower); } public void removeFollower(L1FollowerInstance follower) { this._followerlist.remove(Integer.valueOf(follower.getId())); } public Map<Integer, L1FollowerInstance> getFollowerList() { return this._followerlist; } public void setPoison(L1Poison poison) { this._poison = poison; } public void curePoison() { if (this._poison == null) return;  this._poison.cure(); } public L1Poison getPoison() { return this._poison; } public void setPoisonEffect(int effectId) { broadcastPacket((ServerBasePacket)new S_Poison(getId(), effectId)); } public int getZoneType() { if (getMapId() == 800 || getMapId() == 5490) return 1;  if (getMap().isSafetyZone(getLocation())) { if (getMapId() == 5153) return -1;  return 1; }  if (getMap().isCombatZone(getLocation())) return -1;  return 0; } public long get_exp() { return this.m_exp; } public void set_exp(long exp) { this.m_exp = exp; } public boolean knownsObject(L1Object obj) { return this._knownObjects.containsKey(Integer.valueOf(obj.getId())); } public Collection<L1Object> getKnownObjects() { return this._knownObjects.values(); } public Collection<L1PcInstance> getKnownPlayers() { return this._knownPlayer.values(); } public boolean addKnownObject(L1Object obj, ServerBasePacket serverbasepacket) { if (!this._knownObjects.containsValue(obj)) { this._knownObjects.put(Integer.valueOf(obj.getId()), obj); if (obj instanceof L1PcInstance) this._knownPlayer.put(Integer.valueOf(obj.getId()), (L1PcInstance)obj);  if (this instanceof L1PcInstance && serverbasepacket != null) ((L1PcInstance)this).sendPackets(serverbasepacket);  return true; }  return false; } public void addKnownObject(L1Object obj) { this._knownObjects.put(Integer.valueOf(obj.getId()), obj); if (obj instanceof L1PcInstance) this._knownPlayer.put(Integer.valueOf(obj.getId()), (L1PcInstance)obj);  } public void removeKnownObject(L1Object obj) { this._knownObjects.remove(Integer.valueOf(obj.getId())); this._knownPlayer.remove(Integer.valueOf(obj.getId())); } public void removeAllKnownObjects() { this._knownObjects.clear(); this._knownPlayer.clear(); } public String getName() { return this._name; } public void setName(String s) { this._name = s; } public int getLevel() { return this._level; } public synchronized void setLevel(long level) { this._level = (int)level; } public int getMaxHp() { if (getAbility() == null) return this._maxHp;  int maxhp = this._maxHp; if (this instanceof L1PcInstance) { if (getAbility().getTotalCon() >= 25) maxhp += 200;  if (getAbility().getTotalCon() >= 35) maxhp += 400;  if (getAbility().getTotalCon() >= 45) maxhp += 600;  if (getAbility().getTotalCon() >= 55) maxhp += 800;  if (getAbility().getTotalCon() >= 60) maxhp += 1000;  }  int point = 0; if (this != null && isPassive(MJPassiveID.INFINITI_BLOOD.toInt())) { int Level = getLevel(); if (Level < 60) Level = 60;  point = ((Level - 60) / 3 + 1) * 50; }  if (this != null && isPassive(MJPassiveID.MORTAL_BODY.toInt())) { int Level = getLevel(); if (Level < 80) Level = 80;  point = ((Level - 80) / 4 + 1) * 100; if (point >= 600) point = 600;  }  if (hasSkillEffect(246)) maxhp = (int)(maxhp + maxhp * 0.2D);  return maxhp + point; } public void addMaxHp(int i) { setMaxHp(this._trueMaxHp + i); if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp())); }  } public void setMaxHp(int hp) { this._trueMaxHp = hp; this._maxHp = IntRange.ensure(this._trueMaxHp, 1, 10000000); this._currentHp = Math.min(this._currentHp, this._maxHp); } public int getMaxMp() { if (getAbility() == null) return this._maxMp;  int maxmp = this._maxMp; if (this instanceof L1PcInstance) { int wis = getAbility().getTotalWis(); if (wis >= 25) maxmp += 50;  if (wis >= 35) maxmp += 100;  if (wis >= 45) maxmp += 150;  if (wis >= 55) maxmp += 200;  if (wis >= 60) maxmp += 400;  if (hasSkillEffect(246)) maxmp = (int)(maxmp + maxmp * 0.2D);  }  return maxmp; } public void setMaxMp(int mp) { this._trueMaxMp = mp; this._maxMp = IntRange.ensure(this._trueMaxMp, 0, 10000000); this._currentMp = Math.min(this._currentMp, this._maxMp); } public void addMaxMp(int i) { setMaxMp(this._trueMaxMp + i); if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp())); }  } public void healHp(int pt) { setCurrentHp(getCurrentHp() + pt); } public int getAddAttrKind() { return this._addAttrKind; } public void setAddAttrKind(int i) { this._addAttrKind = i; } public int getDmgup() { return this._dmgup; } public void addDmgup(int i) { this._trueDmgup += i; if (this._trueDmgup >= 127) { this._dmgup = 127; } else if (this._trueDmgup <= -128) { this._dmgup = -128; } else { this._dmgup = this._trueDmgup; }  } public int getBowDmgup() { return this._bowDmgup; } public int getMagicDmgup() { return this._Magicdmgup; } public void addMagicDmgup(int i) { if (this._Magicdmgup + i >= 127) { this._Magicdmgup = 127; } else if (this._Magicdmgup + i <= -128) { this._Magicdmgup = -128; } else { this._Magicdmgup += i; }  } public void addBowDmgup(int i) { this._trueBowDmgup += i; if (this._trueBowDmgup >= 127) { this._bowDmgup = 127; } else if (this._trueBowDmgup <= -128) { this._bowDmgup = -128; } else { this._bowDmgup = this._trueBowDmgup; }  } public int getHitup() { return this._hitup; } public void addHitup(int i) { this._trueHitup += i; if (this._trueHitup >= 127) { this._hitup = 127; } else if (this._trueHitup <= -128) { this._hitup = -128; } else { this._hitup = this._trueHitup; }  } public int getBowHitup() { return this._bowHitup; } public int get_reduction_per() { return this._reduction_per; } public int add_reduction_per(int i) { this._reduction_per += i; return this._reduction_per; } public void addBowHitup(int i) { this._trueBowHitup += i; if (this._trueBowHitup >= 127) { this._bowHitup = 127; } else if (this._trueBowHitup <= -128) { this._bowHitup = -128; } else { this._bowHitup = this._trueBowHitup; }  } public int getMagicLevel() { return getLevel() / 4; } public int getMagicBonus() { int i = getAbility().getTotalInt(); if (i <= 5) return -2;  if (i <= 8) return -1;  if (i <= 11) return 0;  if (i <= 14) return 1;  if (i <= 17) return 2;  return i - 15; } public long getDoppelTime() { return this._doppeltime; } public void setDoppelTime(long l) { this._doppeltime = l; } public boolean isDead() { return this._isDead; } public void setDead(boolean flag) { this._isDead = flag; } public int getStatus() { return this._status; } public void setStatus(int i) { this._status = i; } public String getTitle() { return this._title; } public void setTitle(String s) { this._title = s; } public int getLawful() { return this._lawful; } public void setLawful(int i) { this._lawful = i; } public synchronized void addLawful(int i) { if (Config.Login.StandbyServer) return;  this._lawful += i; if (this._lawful > 32767) { this._lawful = 32767; } else if (this._lawful < -32768) { this._lawful = -32768; }  } public int getHeading() { return this._heading; } public void setHeading(int i) { this._heading = i; } public int getMoveSpeed() { return this._moveSpeed; } public void setMoveSpeed(int i) { this._moveSpeed = i; } public int getBraveSpeed() { return this._braveSpeed; } public void setBraveSpeed(int i) { this._braveSpeed = i; } public boolean isInvisble() { return (hasSkillEffect(60) || hasSkillEffect(97) || this._invisble); } public void setInvisble(boolean b) { this._invisble = b; } public int getKarma() { return this._karma; } public void setKarma(int karma) { this._karma = karma; } public MJKDA getKDA() { return this._kda; } public void setKDA(MJKDA kda) { this._kda = kda; } public long getSkilldelay2() { return this._skilldelay2; } public void setSkilldelay2(long skilldelay2) { this._skilldelay2 = skilldelay2; } public int getBuffnoch() { return this._buffnoch; } public void setBuffnoch(int buffnoch) { this._buffnoch = buffnoch; } public static Random getRnd() { return _rnd; } public Light getLight() { return this.light; } public Ability getAbility() { return this.ability; } public Resistance getResistance() { return this.resistance; } public void resetResistance() { this.resistance = new Resistance(this); } public AC getAC() { return this.ac; } public MoveState getMoveState() { return this.moveState; } public int getMr() { if (hasSkillEffect(153) == true) return this._mr / 4;  return this._mr; } public int getTrueMr() { return this._trueMr; } public void addMr(int i) { this._trueMr += i; if (this._trueMr <= 0) { this._mr = 0; } else { this._mr = this._trueMr; }  } public int get락구간상승() { return this.락구간상승; } public void set락구간상승(int i) { this.락구간상승 = i; } public void add락구간상승(int i) { this.락구간상승 += i; } public void sendPetCtrlMenu(L1NpcInstance npc, boolean type) { if (npc instanceof L1PetInstance) { L1PetInstance pet = (L1PetInstance)npc; L1PcInstance l1PcInstance = pet.getMaster(); if (l1PcInstance instanceof L1PcInstance) { L1PcInstance pc = l1PcInstance; pc.sendPackets((ServerBasePacket)new S_PetCtrlMenu(pc, npc, type)); }  } else if (npc instanceof L1SummonInstance) { L1SummonInstance summon = (L1SummonInstance)npc; L1Character cha = summon.getMaster(); if (cha instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)cha; pc.sendPackets((ServerBasePacket)new S_PetCtrlMenu(pc, npc, type)); }  }  } public void disposeShopInfo() { disposeSellings(); disposePurchasings(); } private MJDShopItem findDShopItem(ArrayList<MJDShopItem> list, int objid) { if (list == null) return null;  int size = list.size(); MJDShopItem item = null; for (int i = 0; i < size; i++) { item = list.get(i); if (item.objId == objid) return item;  }  return null; } public MJDShopItem findSellings(int objid) { return findDShopItem(this._sellings, objid); } public void updateSellings(int objid, int count) { MJDShopItem item = findSellings(objid); if (item == null) return;  if (item.count <= count) { this._sellings.remove(item); MJDShopStorage.deleteProcess(this, item.objId); } else { item.count -= count; MJDShopStorage.updateProcess(this, item); }  } public ArrayList<MJDShopItem> getSellings() { return this._sellings; } public void setSellings(ArrayList<MJDShopItem> list) { this._sellings = list; } public void addSellings(MJDShopItem item) { if (this._sellings == null) this._sellings = new ArrayList<>(7);  this._sellings.add(item); } public void disposeSellings() { if (this._sellings != null) { this._sellings.clear(); this._sellings = null; }  } public MJDShopItem findPurchasings(int objid) { return findDShopItem(this._purchasings, objid); } public void updatePurchasings(int objid, int count) { MJDShopItem item = findPurchasings(objid); if (item == null) return;  if (item.count <= count) { this._purchasings.remove(item); MJDShopStorage.deleteProcess(this, item.objId); } else { item.count -= count; MJDShopStorage.updateProcess(this, item); }  } public ArrayList<MJDShopItem> getPurchasings() { return this._purchasings; } public void setPurchasings(ArrayList<MJDShopItem> list) { this._purchasings = list; } public void addPurchasings(MJDShopItem item) { if (this._purchasings == null) this._purchasings = new ArrayList<>(7);  this._purchasings.add(item); } public L1PcInstance getTomahawkHunter() { return this._TomaHawkHunter; }
   public void disposePurchasings() { if (this._purchasings != null) { this._purchasings.clear(); this._purchasings = null; }  }
   public MJBotAI getAI() { return this._botAI; }
   public void setAI(MJBotAI ai) { this._botAI = ai; }
   public boolean isHaste() { return (hasSkillEffect(1001) || hasSkillEffect(43) || hasSkillEffect(707114) || hasSkillEffect(54) || getMoveSpeed() == 1); } public boolean isSlow() { return (hasSkillEffect(29) || hasSkillEffect(30001) || hasSkillEffect(30000)); } public int getRemainSlowSeconds() { if (hasSkillEffect(29)) return getSkillEffectTimeSec(29);  if (hasSkillEffect(30001)) return getSkillEffectTimeSec(30001);  if (hasSkillEffect(30000)) return getSkillEffectTimeSec(30000);  return 0; } public int getTrueTarget() { return this._truetarget; } public void setTrueTarget(int i) { this._truetarget = i; } public int getTrueTargetClan() { return this._truetarget_clan; } public void setTrueTargetClan(int i) { this._truetarget_clan = i; } public int getTrueTargetParty() { return this._truetarget_party; } public void setTrueTargetParty(int i) { this._truetarget_party = i; } public void sendPackets(ServerBasePacket pck, boolean clear) { if (clear) pck.clear();  } public void sendPackets(ProtoOutputStream stream, boolean is_clear) { if (is_clear) stream.dispose();  } public void receiveDamage(L1Character attacker, int damage) {} private static int _instanceType = -1; protected SpriteInformation _currentSpriteInfo; private boolean _isLock; public int getL1Type() { return (_instanceType == -1) ? (_instanceType = super.getL1Type() | 0x2) : _instanceType; } public void sendPackets(ServerBasePacket sbp) { sbp.clear(); sbp = null; } public int getCurrentSpriteId() { return (this._currentSpriteInfo == null) ? 1120 : this._currentSpriteInfo.getSpriteId(); } public SpriteInformation getCurrentSprite() { return this._currentSpriteInfo; } public void setCurrentSprite(int spriteId) { if (!equalsCurrentSprite(spriteId)) this._currentSpriteInfo = SpriteInformationLoader.getInstance().get(spriteId);  } public boolean equalsCurrentSprite(int compareSpriteId) { return (getCurrentSpriteId() == compareSpriteId); } public long getCurrentSpriteInterval(EActionCodes actionCode) { return (long)this._currentSpriteInfo.getInterval(this, actionCode); } public long getCurrentSpriteInterval(int actionCode) { return (long)this._currentSpriteInfo.getInterval(this, actionCode); } public void sendShape(int poly) { S_ChangeShape shape = new S_ChangeShape(getId(), poly, 0); sendPackets((ServerBasePacket)shape, false); broadcastPacket((ServerBasePacket)shape); } public boolean isLock() { return this._isLock; } public void setLock(boolean b) { this._isLock = b; } public void set_missile_critical_rate(int rate) { this._missile_critical_rate = rate; } public int add_missile_critical_rate(int rate) { int old_missile_critical_rate = this._missile_critical_rate; if (this._missile_critical_rate < 0) this._missile_critical_rate = old_missile_critical_rate;  return this._missile_critical_rate += rate; } public int get_missile_critical_rate() { return this._missile_critical_rate; } public void set_melee_critical_rate(int rate) { this._melee_critical_rate = rate; } public int get_melee_critical_rate() { return this._melee_critical_rate; } public int add_melee_critical_rate(int rate) { int old_melee_critical_rate = this._melee_critical_rate; if (this._melee_critical_rate < 0) this._melee_critical_rate = old_melee_critical_rate;  return this._melee_critical_rate += rate; } public void set_magic_critical_rate(int rate) { this._magic_critical_rate = rate; } public int get_magic_critical_rate() { return this._magic_critical_rate; } public int add_magic_critical_rate(int rate) { int old_magic_critical_rate = this._magic_critical_rate; if (this._magic_critical_rate < 0) this._magic_critical_rate = old_magic_critical_rate;  return this._magic_critical_rate += rate; } public int get_CC_Increase() { return this._CC_Increase; } public int add_CC_Increase(int i) { return this._CC_Increase += i; } public int get_final_burn_critical_rate() { if (!isPassive(MJPassiveID.FINAL_BURN.toInt()) || getCurrentHpPercent() > 70.0D) return 0;  int level = getLevel(); int rate = 5; if (level >= 92) rate += (getLevel() - 90) / 2 + 2;  if (rate >= 15) rate = 15;  return rate; } public void send_party_effect(int effect_id) { L1PcInstance _owner = (L1PcInstance)this; L1Party party = _owner.getParty(); if (effect_id > 0) { S_SkillSound sound = new S_SkillSound(getId(), effect_id); sendPackets((ServerBasePacket)sound, false); if (party != null) for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { if (party == pc.getParty()) pc.sendPackets((ServerBasePacket)sound, false);  }   sound.clear(); }  } public void send_other_party_effect(L1PcInstance attacker, int effect_id) { L1Party party = attacker.getParty(); if (effect_id > 0) { S_SkillSound sound = new S_SkillSound(getId(), effect_id); attacker.sendPackets((ServerBasePacket)sound, false); if (party != null) for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { if (party == pc.getParty()) pc.sendPackets((ServerBasePacket)sound, false);  }   sound.clear(); }  } public void send_tarobj_party_effect(int target, int effect_id) { L1PcInstance _owner = (L1PcInstance)this; L1Party party = _owner.getParty(); if (effect_id > 0) { S_SkillSound sound = new S_SkillSound(target, effect_id); sendPackets((ServerBasePacket)sound, false); if (party != null) for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { if (party == pc.getParty()) pc.sendPackets((ServerBasePacket)sound, false);  }   sound.clear(); }  } public void send_effect(int effect_id) { send_effect(effect_id, true); } public void send_effect(int effect_id, boolean check) { if (effect_id > 0) { S_SkillSound sound = new S_SkillSound(getId(), effect_id); sendPackets((ServerBasePacket)sound, false); if (check) { broadcastPacket((ServerBasePacket)sound, true); } else { sound.clear(); }  }  } public void send_effect(boolean run, int effect_id, int remaining_seconds) { sendPackets((ServerBasePacket)new S_PacketBox(run, effect_id, remaining_seconds), true); } public void send_action(int action_id) { if (action_id > 0) { S_DoActionGFX gfx = new S_DoActionGFX(getId(), action_id); sendPackets((ServerBasePacket)gfx, false); broadcastPacket((ServerBasePacket)gfx, true); }  } public void send_pink_name(int remain_seconds) { S_PinkName pnk = new S_PinkName(getId(), remain_seconds); sendPackets((ServerBasePacket)pnk, false); broadcastPacket((ServerBasePacket)pnk, true); } public void send_lawful() { S_Lawful pck = new S_Lawful(getId(), getLawful()); sendPackets((ServerBasePacket)pck, false); broadcastPacket((ServerBasePacket)pck); } private static final int[] _elf_skill_braves = new int[] { 155, 179, 178, 177 }; private L1DollInstance _doll; private int _foudmg; private int _reduc_cancel; public boolean MoBTripleArrow; public boolean MoBTripleArrow_PRISON; private double _move_delay_rate; private double _attack_delay_rate; private double _magic_delay_rate; private int _emblemId; private int _blessAinEfficiency; public L1PcInstance _EternitiAttacker; private boolean _RedknightType; private int _halpas_faith_pvp_reduc; private boolean _halpas_armor; private int _halpas_armor_enchant; private boolean _reduction_armor_veteran; private int _reducreduction_value; private boolean lucifer_destiny; private boolean SHADOW_ARMOR_destiny; private boolean immunetoharm_saini; private int _acurucy_meister; private int _shining_shild_obj_id; private int _dead_count; private int _exp_count; private int monsterkill; private L1PcInstance _presher_pc; private int _presher_dmg; private boolean _resher_death_recall; private boolean _shadow_step_chaser; private boolean _Maelstrom; private final Map<Integer, ItemDelayTimer> _item_delay; private boolean _striker_gail_shot; private L1PcInstance _TomaHawkHunter; public boolean _dominion_tel; private int _armor_break_attacker_id; public void remove_elf_second_brave() { for (int skillId : _elf_skill_braves) { if (hasSkillEffect(skillId)) { removeSkillEffect(skillId); S_SkillBrave brave = new S_SkillBrave(getId(), 0, 0); sendPackets((ServerBasePacket)brave, false); broadcastPacket((ServerBasePacket)brave, true); setBraveSpeed(0); }  }  } public boolean is_assassination_level2() { return (getKDA() == null) ? false : getKDA().is_assassination_level2(); } public boolean is_assassination_level1() { return (getKDA() == null) ? false : getKDA().is_assassination_level1(); } public L1DollInstance getMagicDoll() { return this._doll; } public void setMagicDoll(L1DollInstance doll) { this._doll = doll; } public int getFouDmg() { return this._foudmg; } public void addFouDmg(int i) { this._foudmg += i; } public int getReducCancel() { return this._reduc_cancel; } public void addReducCancel(int i) { this._reduc_cancel += i; } public int getBlessAinEfficiency() { return this._blessAinEfficiency; } public void addBlessAinEfficiency(int i) { try { this._blessAinEfficiency += i; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; if (pc.getAI() != null) return;  pc.sendPackets((ServerBasePacket)new S_ACTION_UI(1020, pc)); SC_EXP_BOOSTING_INFO_NOTI.send(pc); }  } catch (Exception exception) {} } public void onAction(L1Character attacker) { if (attacker == null) return;  if (getZoneType() == 1 || attacker.getZoneType() == 1) { L1Attack attack_mortion = new L1Attack(attacker, this); attack_mortion.action(); return; }  if (getCurrentHp() > 0 && !isDead()) { boolean isMortalBody = false; L1Attack attack = new L1Attack(attacker, this); L1Magic magic = null; if (attack.calcHit()) { if (hasSkillEffect(394)) { magic = new L1Magic(this, attacker); boolean isProbability = magic.calcProbabilityMagic(394); boolean isShortDistance1 = attack.isShortDistance1(); if (isProbability && isShortDistance1) isMortalBody = true;  }  if (!isMortalBody) { attack.calcDamage(); attack.addPcPoisonAttack(attacker, this); }  }  if (isMortalBody) { attack.calcDamage(); attack.actionMortalBody(); attack.commitMortalBody(); attack.commit(); } else { attack.action(); attack.commit(); }  }  } public double getMoveDelayRate() { return this._move_delay_rate; } public void setMoveDelayRate(double move_delay_rate) { this._move_delay_rate = move_delay_rate; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED, (int)this._move_delay_rate), true, true); }  } public void addMoveDelayRate(double move_delay_rate) { this._move_delay_rate += move_delay_rate; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED, (int)this._move_delay_rate), true, true); }  } public double getAttackDelayRate() { return this._attack_delay_rate; } public void setAttackDelayRate(double attack_delay_rate) { this._attack_delay_rate = attack_delay_rate; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED, (int)this._attack_delay_rate), true, true); }  } public void addAttackDelayRate(double attack_delay_rate) { this._attack_delay_rate += attack_delay_rate; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED, (int)this._attack_delay_rate), true, true); }  } public double getMagicDelayRate() { return this._magic_delay_rate; } public void setMagicDelayRate(double magic_delay_rate) { this._magic_delay_rate = magic_delay_rate; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.SPELL_SPEED, (int)this._magic_delay_rate), true, true); }  } public void addMagicDelayRate(double magic_delay_rate) { this._magic_delay_rate += magic_delay_rate; if (this instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)this; pc.broadcastPacket(SC_SPEED_BONUS_NOTI.speed_send(pc, SC_SPEED_BONUS_NOTI.Bonus.eKind.SPELL_SPEED, (int)this._magic_delay_rate), true, true); }  } public int getEmblemId() { return this._emblemId; } public void setEmblemId(int i) { this._emblemId = i; } public boolean getRedknightType() { return this._RedknightType; } public void setRedknightType(boolean flag) { this._RedknightType = flag; } public int get_halpas_faith_pvp_reduc() { return this._halpas_faith_pvp_reduc; } public void set_halpas_faith_pvp_reduc(int _halpas_faith_pvp_reduc) { this._halpas_faith_pvp_reduc = _halpas_faith_pvp_reduc; } public boolean is_halpas_armor() { return this._halpas_armor; } public void set_halpas_armor(boolean _halpas_armor) { this._halpas_armor = _halpas_armor; } public int get_halpas_armor_enchant() { return this._halpas_armor_enchant; } public void set_halpas_armor_enchant(int _halpas_armor_enchant) { this._halpas_armor_enchant = _halpas_armor_enchant; } public boolean is_reduction_armor_veteran() { return this._reduction_armor_veteran; } public void set_reduction_armor_veteran(boolean _reduction_armor_veteran) { this._reduction_armor_veteran = _reduction_armor_veteran; } public int get_reducreduction_value() { return this._reducreduction_value; } public void set_reducreduction_value(int _reducreduction_value) { this._reducreduction_value = _reducreduction_value; } public boolean isLucifer_destiny() { return this.lucifer_destiny; } public void setLucifer_destiny(boolean lucifer_destiny) { this.lucifer_destiny = lucifer_destiny; } public int getTempCharGfx() { return this._tempCharGfx; } public void setTempCharGfx(int i) { this._tempCharGfx = i; } public int getGfxId() { return this._gfxid; } public void setGfxId(int i) { this._gfxid = i; } public boolean isSHADOW_ARMOR_destiny() { return this.SHADOW_ARMOR_destiny; } public void setSHADOW_ARMOR_destiny(boolean SHADOW_ARMOR_destiny) { this.SHADOW_ARMOR_destiny = SHADOW_ARMOR_destiny; } public boolean isImmunetoharm_saini() { return this.immunetoharm_saini; } public void setImmunetoharm_saini(boolean immunetoharm_saini) { this.immunetoharm_saini = immunetoharm_saini; } public int get_acurucy_meister() { return this._acurucy_meister; } public void set_acurucy_meister(int _acurucy_meister) { this._acurucy_meister = _acurucy_meister; } public int get_shining_shild_obj_id() { return this._shining_shild_obj_id; } public void set_shining_shild_obj_id(int _shining_shild_obj_id) { this._shining_shild_obj_id = _shining_shild_obj_id; } public int get_dead_count() { return this._dead_count; } public int add_dead_count(int rate) { return this._dead_count += rate; } public void set_dead_count(int i) { this._dead_count = i; } public int get_exp_count() { return this._exp_count; } public int add_exp_count(int rate) { return this._exp_count += rate; } public void set_exp_count(int i) { this._exp_count = i; } public int getMonsterkill() { return this.monsterkill; } public void setMonsterkill(int monster) { this.monsterkill = monster; } public void addMonsterKill(int i) { this.monsterkill += i; } public void broadcastPacketForFindInvis(ServerBasePacket packet, boolean isFindInvis) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { if (isFindInvis) { if (pc.hasSkillEffect(1012)) { pc.sendPackets(packet); continue; }  if (this instanceof L1PcInstance) { L1PcInstance owner = (L1PcInstance)this; if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) pc.sendPackets(packet);  continue; }  if (this instanceof L1DollInstance) { L1DollInstance doll = (L1DollInstance)this; L1PcInstance owner = (L1PcInstance)doll.getMaster(); if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) pc.sendPackets(packet);  }  continue; }  if (this instanceof L1PcInstance) { L1PcInstance owner = (L1PcInstance)this; if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) continue;  if (!pc.hasSkillEffect(1012)) pc.sendPackets(packet);  continue; }  if (this instanceof L1DollInstance) { L1DollInstance doll = (L1DollInstance)this; L1PcInstance owner = (L1PcInstance)doll.getMaster(); if ((pc.isInParty() && pc.getParty().isMember(owner)) || (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid())) continue;  if (!pc.hasSkillEffect(1012)) pc.sendPackets(packet);  continue; }  if (!pc.hasSkillEffect(1012)) { pc.sendPackets(packet); continue; }  if (pc.getMapId() != 63 || !pc.hasSkillEffect(11177) || this instanceof L1NpcInstance || this instanceof l1j.server.server.model.Instance.L1MonsterInstance); }  } public void broadcastPacketForFindInvis(ProtoOutputStream packet, boolean isFindInvis) { for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) { if (isFindInvis) { if (pc.hasSkillEffect(1012)) { pc.sendPackets(packet); continue; }  if (this instanceof L1PcInstance) { L1PcInstance owner = (L1PcInstance)this; if (pc.isInParty() && pc.getParty().isMember(owner)) pc.sendPackets(packet);  if (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid()) pc.sendPackets(packet);  }  continue; }  if (this instanceof L1PcInstance) { L1PcInstance owner = (L1PcInstance)this; if (pc.isInParty() && pc.getParty().isMember(owner)) continue;  if (pc.getClanid() != 0 && pc.getClanid() == owner.getClanid()) continue;  if (!pc.hasSkillEffect(1012)) pc.sendPackets(packet);  continue; }  if (!pc.hasSkillEffect(1012)) pc.sendPackets(packet);  }  } public L1PcInstance getPresherPc() { return this._presher_pc; } public void setPresherPc(L1PcInstance pc) { this._presher_pc = pc; } public int getPresherDamage() { return this._presher_dmg; } public void setPresherDamage(int dmg) { this._presher_dmg = dmg; } public void addPresherDamage(int dmg) { this._presher_dmg += dmg; } public boolean getPresherDeathRecall() { return this._resher_death_recall; } public void setPresherDeathRecall(boolean flag) { this._resher_death_recall = flag; } public boolean getshadowstepchaser() { return this._shadow_step_chaser; } public void setShadowstepchaser(boolean flag) { this._shadow_step_chaser = flag; } public boolean get_Maelstrom() { return this._Maelstrom; } public void set_Maelstrom(boolean flag) { this._Maelstrom = flag; } public boolean addItemDelayTime(L1ItemInstance item) { if (item == null) return false;  if (item.getItem().get_delaytime() > 0) { int itemId = SameTypeCheck(item); int delayTime = item.getItem().get_delaytime(); if (item.getItem().getType2() == 0 && item.getItem().getType() == 6) { AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(getId()); if (Info != null && Info.get_potion() != 0) delayTime = Info.get_potion_val_2();  }  L1PcInstance pc = (L1PcInstance)this; if (item.getItem().getType2() == 1) return false;  if (item.getItem().getType2() == 2) if (item.getItem().getItemId() == 20077 || item.getItem().getItemId() == 20062 || item.getItem().getItemId() == 120077) { if (!pc.isInvisble()) { L1ItemInstance clock = pc.getInventory().getItemEquipped(2, 4); if (clock == null) pc.beginInvisTimer();  }  } else { return false; }   ItemDelayTimer timer = ItemDelayTimer.newTimer(this, itemId, item.getName(), delayTime); this._item_delay.put(Integer.valueOf(itemId), timer); timer.begin(); return true; }  return false; } public boolean addItemDelayTime(int itemid, String itemName, long time) { if (time > 0L) { ItemDelayTimer timer = ItemDelayTimer.newTimer(this, itemid, itemName, time); this._item_delay.put(Integer.valueOf(itemid), timer); timer.begin(); return true; }  return false; } public void removeItemDelayTime(int itemid) { ItemDelayTimer timer = this._item_delay.remove(Integer.valueOf(itemid)); if (timer != null) timer.end();  } public boolean hasItemDelayTime(int ItemId) { return this._item_delay.containsKey(Integer.valueOf(ItemId)); } public boolean hasItemDelayTime(L1ItemInstance item) { if (item == null) return false;  int itemId = SameTypeCheck(item); return this._item_delay.containsKey(Integer.valueOf(itemId)); } public int getItemDelayTimeSec(int ItemId) { ItemDelayTimer timer = this._item_delay.get(Integer.valueOf(ItemId)); if (timer == null) return 0;  return timer.remainingSeconds(); } public Timestamp getItemDelayLogTime(int ItemId) { ItemDelayTimer timer = this._item_delay.get(Integer.valueOf(ItemId)); if (timer == null) return null;  return timer.LogDelayTime(); } public Timestamp getItemDelayLogTime(L1ItemInstance item) { int itemId = SameTypeCheck(item); ItemDelayTimer timer = this._item_delay.get(Integer.valueOf(itemId)); if (timer == null) return null;  return timer.LogDelayTime(); } public Collection<ItemDelayTimer> hasItemDelayTimeList() { if (this._item_delay == null) return null;  return this._item_delay.values(); } public int SameTypeCheck(L1ItemInstance item) { int itemid = item.getId(); if (item.getItem().getType2() == 0 && item.getItem().getType() == 6) itemid = 40010;  return itemid; } public boolean isStrikerGailShot() { return this._striker_gail_shot; } public void setStrikerGailShot(boolean value) { this._striker_gail_shot = value; } public void setTomahawkHunter(L1PcInstance value) { this._TomaHawkHunter = value; }



   public void set_dominion_tel(int i) {
     if (i == 1) {
       this._dominion_tel = true;
     } else {
       this._dominion_tel = false;
     }
   }

   public boolean is_dominion_tel() {
     return this._dominion_tel;
   }


   public void set_Armor_break_Attacker(int i) {
     this._armor_break_attacker_id = i;
   }
   public int get_Armor_break_Attacker() {
     return this._armor_break_attacker_id;
   }

   public boolean isShokAttackTeleport() {
     return hasSkillEffect(5157);
   }

   public boolean isEternity() {
     return hasSkillEffect(243);
   }

   public boolean isShadowStepChaser() {
     return hasSkillEffect(395);
   }

   public boolean isDesperado() {
     return hasSkillEffect(230);
   }

   public boolean isDeathRecall() {
     if (!getPresherDeathRecall()) {
       return false;
     }
     if (this instanceof L1PcInstance)
     {
       ((L1PcInstance)this).receiveDamage(this, 500);
     }
     return true;
   }




   public boolean isStop() {
     return (hasSkillEffect(87) || hasSkillEffect(242) || hasSkillEffect(208) || hasSkillEffect(123) ||
       hasSkillEffect(5003) || hasSkillEffect(334) || hasSkillEffect(77) ||
       hasSkillEffect(5056) || hasSkillEffect(5027) || hasSkillEffect(5027) ||
       hasSkillEffect(157) || hasSkillEffect(70705) || hasSkillEffect(30003) || hasSkillEffect(30004) ||
       hasSkillEffect(30081) || hasSkillEffect(30006) || hasSkillEffect(30005) || hasSkillEffect(30010) ||
       hasSkillEffect(22025) || hasSkillEffect(22026) || hasSkillEffect(22027) || hasSkillEffect(22031));
   }

   public boolean isNotTeleport() {
     return (isDead() || isStop() || isParalyzed() || isSleeped() || isDeathRecall() || isDesperado() || isShokAttackTeleport() || isEternity() || isShadowStepChaser());
   }
 }



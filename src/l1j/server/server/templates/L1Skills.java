 package l1j.server.server.templates;

 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;




















 public class L1Skills
 {
   public static final int ATTR_NONE = 0;
   public static final int ATTR_EARTH = 1;
   public static final int ATTR_FIRE = 2;
   public static final int ATTR_WATER = 4;
   public static final int ATTR_WIND = 8;
   public static final int ATTR_RAY = 16;
   public static final int TYPE_PROBABILITY = 1;
   public static final int TYPE_CHANGE = 2;
   public static final int TYPE_CURSE = 4;
   public static final int TYPE_DEATH = 8;
   public static final int TYPE_HEAL = 16;
   public static final int TYPE_RESTORE = 32;
   public static final int TYPE_ATTACK = 64;
   public static final int TYPE_OTHER = 128;
   public static final int TARGET_TO_ME = 0;
   public static final int TARGET_TO_PC = 1;
   public static final int TARGET_TO_NPC = 2;
   public static final int TARGET_TO_CLAN = 4;
   public static final int TARGET_TO_PARTY = 8;
   public static final int TARGET_TO_PET = 16;
   public static final int TARGET_TO_PLACE = 32;
   private String _name;
   private String _target;
   private String _nameId;
   private boolean _isThrough;
   private int _skillId;
   private int _skillLevel;
   private int _skillNumber;
   private int _damageValue;
   private int _damageDice;
   private int _damageDiceCount;
   private int _probabilityValue;
   private int _probabilityDice;
   private int _attr;
   private int _type;
   private int _ranged;
   private int _area;
   private int _id;
   private int _actionId;

   public int getSkillId() {
     return this._skillId;
   }
   private int _actionId2; private int _actionId3; private int _castGfx; private int _castGfx2; private int _castGfx3; private int _sysmsgIdHappen; private int _sysmsgIdStop; private int _sysmsgIdFail; private int _lawful; private int _mpConsume; private int _hpConsume; private int _itmeConsumeId; private int _itmeConsumeCount; private int _reuseDelay; private int _buffDuration; private int _targetTo; private int _plus_prob; private boolean _isbuff; private boolean _canCastWithInvis; private boolean _ignoresCounterMagic; private int _inveniconStart; private int _inveniconEnd; private int _inveniconStringNo; private int _inveniconnewstrid; private int _inveniconendstrid; private int _inveniconsort; private boolean _isinvenicon; private int _invenicon_overlap_buff_icon; private int _invenicon_main_tooltip_str_id; private int _invenicon_buff_icon_priority; private int _invenicon_buff_group_id; private int _invenicon_buff_group_priority; private SC_SPELL_BUFF_NOTI.eDurationShowType _duration_show_type; private boolean _is_save; private boolean _is_auto_skill_err; private boolean _debuff; private boolean _magic_dmg_mr_impact; private boolean _magic_dmg_int_impact; private boolean _Castle_Magic; private boolean _SafetyZone_Magic;
   public void setSkillId(int i) {
     this._skillId = i;
   }

   public String getName() {
     return this._name;
   }

   public void setName(String s) {
     this._name = s;
   }

   public int getSkillLevel() {
     return this._skillLevel;
   }

   public void setSkillLevel(int i) {
     this._skillLevel = i;
   }

   public int getSkillNumber() {
     return this._skillNumber;
   }

   public void setSkillNumber(int i) {
     this._skillNumber = i;
   }

   public int getMpConsume() {
     return this._mpConsume;
   }

   public void setMpConsume(int i) {
     this._mpConsume = i;
   }

   public int getHpConsume() {
     return this._hpConsume;
   }

   public void setHpConsume(int i) {
     this._hpConsume = i;
   }

   public int getItemConsumeId() {
     return this._itmeConsumeId;
   }

   public void setItemConsumeId(int i) {
     this._itmeConsumeId = i;
   }

   public int getItemConsumeCount() {
     return this._itmeConsumeCount;
   }

   public void setItemConsumeCount(int i) {
     this._itmeConsumeCount = i;
   }

   public int getReuseDelay() {
     return this._reuseDelay;
   }

   public void setReuseDelay(int i) {
     this._reuseDelay = i;
   }

   public int getBuffDuration() {
     return this._buffDuration;
   }

   public void setBuffDuration(int i) {
     this._buffDuration = i;
   }

   public String getTarget() {
     return this._target;
   }

   public void setTarget(String s) {
     this._target = s;
   }

   public int getTargetTo() {
     return this._targetTo;
   }

   public void setTargetTo(int i) {
     this._targetTo = i;
   }

   public int getDamageValue() {
     return this._damageValue;
   }

   public void setDamageValue(int i) {
     this._damageValue = i;
   }

   public int getDamageDice() {
     return this._damageDice;
   }

   public void setDamageDice(int i) {
     this._damageDice = i;
   }

   public int getDamageDiceCount() {
     return this._damageDiceCount;
   }

   public void setDamageDiceCount(int i) {
     this._damageDiceCount = i;
   }

   public int getProbabilityValue() {
     return this._probabilityValue;
   }

   public void setProbabilityValue(int i) {
     this._probabilityValue = i;
   }

   public int getProbabilityDice() {
     return this._probabilityDice;
   }

   public void setProbabilityDice(int i) {
     this._probabilityDice = i;
   }

   public int getAttr() {
     return this._attr;
   }

   public void setAttr(int i) {
     this._attr = i;
   }

   public int getType() {
     return this._type;
   }

   public void setType(int i) {
     this._type = i;
   }

   public int getLawful() {
     return this._lawful;
   }

   public void setLawful(int i) {
     this._lawful = i;
   }

   public int getRanged() {
     return this._ranged;
   }

   public void setRanged(int i) {
     this._ranged = i;
   }

   public int getArea() {
     return this._area;
   }

   public void setArea(int i) {
     this._area = i;
   }

   public boolean getIsThrough() {
     return this._isThrough;
   }

   public void setIsThrough(int flag) {
     if (flag == 0) {
       this._isThrough = false;
     } else {
       this._isThrough = true;
     }
   }

   public int getId() {
     return this._id;
   }

   public void setId(int i) {
     this._id = i;
   }

   public String getNameId() {
     return this._nameId;
   }

   public void setNameId(String s) {
     this._nameId = s;
   }

   public int getActionId() {
     return this._actionId;
   }

   public void setActionId(int i) {
     this._actionId = i;
   }

   public int getActionId2() {
     return this._actionId2;
   }

   public void setActionId2(int i) {
     this._actionId2 = i;
   }

   public int getActionId3() {
     return this._actionId3;
   }

   public void setActionId3(int i) {
     this._actionId3 = i;
   }

   public int getCastGfx() {
     return this._castGfx;
   }

   public void setCastGfx(int i) {
     this._castGfx = i;
   }

   public int getCastGfx2() {
     return this._castGfx2;
   }

   public void setCastGfx2(int i) {
     this._castGfx2 = i;
   }

   public int getCastGfx3() {
     return this._castGfx3;
   }

   public void setCastGfx3(int i) {
     this._castGfx3 = i;
   }

   public int getSysmsgIdHappen() {
     return this._sysmsgIdHappen;
   }

   public void setSysmsgIdHappen(int i) {
     this._sysmsgIdHappen = i;
   }

   public int getSysmsgIdStop() {
     return this._sysmsgIdStop;
   }

   public void setSysmsgIdStop(int i) {
     this._sysmsgIdStop = i;
   }

   public int getSysmsgIdFail() {
     return this._sysmsgIdFail;
   }

   public void setSysmsgIdFail(int i) {
     this._sysmsgIdFail = i;
   }



   public int getPlusProbility() {
     return this._plus_prob;
   }

   public void setPlusProbility(int i) {
     this._plus_prob = i;
   }





   public void setCanCastWithInvis(boolean flag) {
     this._canCastWithInvis = flag;
   }

   public boolean isCanCastWithInvis() {
     return this._canCastWithInvis;
   }

   public void setIgnoresCounterMagic(boolean flag) {
     this._ignoresCounterMagic = flag;
   }

   public boolean isIgnoresCounterMagic() {
     return this._ignoresCounterMagic;
   }

   public boolean isBuff() {
     return this._isbuff;
   }

   public void setBuff(boolean flag) {
     this._isbuff = flag;
   }









   public boolean isInvenIconUse() {
     return this._isinvenicon;
   }

   public void setInvenIconUse(boolean flag) {
     this._isinvenicon = flag;
   }

   public int getInvenIconSort() {
     return this._inveniconsort;
   }

   public int getInvenIconStart() {
     return this._inveniconStart;
   }

   public int getInvenIconEnd() {
     return this._inveniconEnd;
   }

   public int getInvenIconStringNo() {
     return this._inveniconStringNo;
   }

   public int getInvenIconStartStrId() {
     return this._inveniconnewstrid;
   }

   public int getInvenIconEndStrId() {
     return this._inveniconendstrid;
   }

   public void setInvenIconStart(int i) {
     this._inveniconStart = i;
   }

   public void setInvenIconEnd(int i) {
     this._inveniconEnd = i;
   }

   public void setInvenIconStringNo(int i) {
     this._inveniconStringNo = i;
   }

   public void setInvenIconStartStrId(int i) {
     this._inveniconnewstrid = i;
   }

   public void setInvenIconEndStrId(int i) {
     this._inveniconendstrid = i;
   }

   public void setInvenIconSort(int i) {
     this._inveniconsort = i;
   }



   public int getInvenIconOverLapBuffIcon() {
     return this._invenicon_overlap_buff_icon;
   }

   public void setInvenIconOverLapBuffIcon(int i) {
     this._invenicon_overlap_buff_icon = i;
   }



   public int getInvenIconMainTooltipStrId() {
     return this._invenicon_main_tooltip_str_id;
   }

   public void setInvenIconMainTooltipStrId(int i) {
     this._invenicon_main_tooltip_str_id = i;
   }



   public int getInvenIconBuffIconPriority() {
     return this._invenicon_buff_icon_priority;
   }

   public void setInvenIconBuffIconPriority(int i) {
     this._invenicon_buff_icon_priority = i;
   }



   public int getInvenIconBuffGroupId() {
     return this._invenicon_buff_group_id;
   }

   public void setInvenIconBuffGroupId(int i) {
     this._invenicon_buff_group_id = i;
   }



   public int getInvenIconBuffGroupPriority() {
     return this._invenicon_buff_group_priority;
   }

   public void setInvenIconBuffGroupPriority(int i) {
     this._invenicon_buff_group_priority = i;
   }



   public SC_SPELL_BUFF_NOTI.eDurationShowType getDurationShowType() {
     return this._duration_show_type;
   }

   public void setDurationShowType(SC_SPELL_BUFF_NOTI.eDurationShowType i) {
     this._duration_show_type = i;
   }



   public boolean isSave() {
     return this._is_save;
   }

   public void setSave(boolean flag) {
     this._is_save = flag;
   }



   public boolean is_auto_skill_err() {
     return this._is_auto_skill_err;
   }

   public void setis_auto_skill_err(boolean flag) {
     this._is_auto_skill_err = flag;
   }



   public boolean isDebuff() {
     return this._debuff;
   }

   public void setDebuff(boolean flag) {
     this._debuff = flag;
   }



   public boolean is_magic_dmg_mr_impact() {
     return this._magic_dmg_mr_impact;
   }

   public void set_magic_dmg_mr_impact(boolean _magic_dmg_mr_impact) {
     this._magic_dmg_mr_impact = _magic_dmg_mr_impact;
   }



   public boolean is_magic_dmg_int_impact() {
     return this._magic_dmg_int_impact;
   }

   public void set_magic_dmg_int_impact(boolean _magic_dmg_int_impact) {
     this._magic_dmg_int_impact = _magic_dmg_int_impact;
   }


   public boolean is_Castle_Magic() {
     return this._Castle_Magic;
   }

   public void set_Castle_Magic(boolean _Castle_Magic) {
     this._Castle_Magic = _Castle_Magic;
   }

   public boolean is_SafetyZone_Magic() {
     return this._SafetyZone_Magic;
   }

   public void set_SafetyZone_Magic(boolean _SafetyZone_Magic) {
     this._SafetyZone_Magic = _SafetyZone_Magic;
   }
 }



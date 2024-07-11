 package l1j.server.server.templates;public class L1Npc extends L1Object implements Cloneable { private static final long serialVersionUID = 1L; public static final int USE_ACTION_NONE = 0; public static final int USE_ACTION_MOVABLE = 1; public static final int USE_ACTION_ACTIONABLE = 2; private int _useAction; private int _npcid; private int _npc_class_id; private String _name; private String _impl; private int _level; private int _hp; private int _mp; private int _ac; private byte _str; private byte _con; private byte _dex; private byte _wis; private byte _int; private int _mr; private long _exp; private int _lawful; private String _size; private int _weakAttr; private int _weakwater; private int _weakwind; private int _weakfire; private int _weakearth; private int _ranged; private boolean _agrososc; private boolean _agrocoi; private boolean _Taming; private boolean _agro; private int _gfxid; private String _nameid; private int _undead; private String _poisonatk; private int _poisonatk_dmg; private int _poisonatk_chance; private int _poisonatk_ms; private int _poisonatk_Silence_ms; private int _paralysisatk; private int _family; private int _agrofamily; private int _agrogfxid1; private int _agrogfxid2; private boolean _picupitem; private int _digestitem; private int _hprinterval; private int _hpr; private int _mprinterval; private int _mpr; private boolean _teleport_run; private boolean _teleport; private int _randomlevel; private int _randomhp; private int _randommp; private int _randomac; private int _randomexp; private int _randomlawful; private int _damagereduction; private boolean _hard; private boolean _doppel; private boolean _tu; private boolean _erase; private int bowActId; private int _karma; private int _transformId; private int _transformGfxId; private int _lightSize;
   private boolean _amountFixed;
   private boolean _changeHead;
   private int doorId;
   private int countId;
   private boolean _isCantResurrect;
   private boolean _isShapeChange;
   private boolean boss;
   private BornAction _born;
   private int _transform_probability;
   private boolean _transform_hard;
   private boolean _transform_drop;
   private boolean _movement;
   private boolean agro_lvl;
   private int infoNumber;

   public L1Npc clone() {
     try {
       return (L1Npc)super.clone();
     } catch (Exception e) {
       e.printStackTrace();
       throw new InternalError(e.getMessage());
     }
   }



   public L1Npc() {
     this.bowActId = 0;
   }
   public int getUseAction() { return this._useAction; }
   public void setUseAction(String s) { if (s.equalsIgnoreCase("move")) { this._useAction = 1; } else if (s.equalsIgnoreCase("action")) { this._useAction = 2; } else if (s.equalsIgnoreCase("move and action")) { this._useAction = 3; } else { this._useAction = 0; }  } public boolean isMovable() { return ((this._useAction & 0x1) > 0); } public boolean isActionable() { return ((this._useAction & 0x2) > 0); } public boolean isDynamic() { return (isMovable() && isActionable()); } public int get_npcId() { return this._npcid; } public void set_npcId(int i) { this._npcid = i; } public int get_npc_class_id() { return this._npc_class_id; } public void set_class_id(int npc_class_id) { this._npc_class_id = npc_class_id; } public String get_name() { return this._name; } public void set_name(String s) { this._name = s; } public String getImpl() { return this._impl; } public void setImpl(String s) { this._impl = s; } public int get_level() { return this._level; } public void set_level(int i) { this._level = i; } public int get_hp() { return this._hp; } public void set_hp(int i) { this._hp = i; } public int get_mp() { return this._mp; } public void set_mp(int i) { this._mp = i; } public int get_ac() { return this._ac; } public void set_ac(int i) { this._ac = i; } public byte get_str() { return this._str; } public void set_str(byte i) { this._str = i; } public byte get_con() { return this._con; } public void set_con(byte i) { this._con = i; } public byte get_dex() { return this._dex; } public void set_dex(byte i) { this._dex = i; } public byte get_wis() { return this._wis; } public void set_wis(byte i) { this._wis = i; } public byte get_int() { return this._int; } public void set_int(byte i) { this._int = i; } public int get_mr() { return this._mr; } public void set_mr(int i) { this._mr = i; } public long get_exp() { return this._exp; } public void set_exp(long i) { this._exp = i; } public int get_lawful() { return this._lawful; } public void set_lawful(int i) { this._lawful = i; } public String get_size() { return this._size; } public void set_size(String s) { this._size = s; } public int get_weakAttr() { return this._weakAttr; } public void set_weakAttr(int i) { this._weakAttr = i; } public int get_weakwater() { return this._weakwater; } public void set_weakwater(int i) { this._weakwater = i; } public int get_weakwind() { return this._weakwind; } public void set_weakwind(int i) { this._weakwind = i; } public int get_weakfire() { return this._weakfire; } public void set_weakfire(int i) { this._weakfire = i; } public int get_weakearth() { return this._weakearth; } public void set_weakearth(int i) { this._weakearth = i; } public int get_ranged() { return this._ranged; } public void set_ranged(int i) { this._ranged = i; } public boolean is_agrososc() { return this._agrososc; } public void set_agrososc(boolean flag) { this._agrososc = flag; } public boolean is_agrocoi() { return this._agrocoi; } public void set_agrocoi(boolean flag) { this._agrocoi = flag; } public boolean isTaming() { return this._Taming; } public void setTaming(boolean flag) { this._Taming = flag; } public boolean is_agro() { return this._agro; } public void set_agro(boolean flag) { this._agro = flag; } public int get_gfxid() { return this._gfxid; } public void set_gfxid(int i) { this._gfxid = i; } public int getBowActId() { return this.bowActId; }
   public String get_nameid() { return this._nameid; }
   public void set_nameid(String s) { this._nameid = s; }
   public int get_undead() { return this._undead; }
   public void set_undead(int i) { this._undead = i; } public String get_poisonatk() { return this._poisonatk; } public void set_poisonatk(String i) { this._poisonatk = i; } public int get_poisonatkdmg() { return this._poisonatk_dmg; } public void set_poisonatkdmg(int i) { this._poisonatk_dmg = i; } public int get_poisonatkchance() { return this._poisonatk_chance; } public void set_poisonatkchance(int i) { this._poisonatk_chance = i; } public int get_poisonatkms() { return this._poisonatk_ms; } public void set_poisonatkms(int i) { this._poisonatk_ms = i; } public int get_poisonatkSilencems() { return this._poisonatk_Silence_ms; } public void set_poisonatkSilencems(int i) { this._poisonatk_Silence_ms = i; } public int get_paralysisatk() { return this._paralysisatk; } public void set_paralysisatk(int i) { this._paralysisatk = i; } public int get_family() { return this._family; } public void set_family(int i) { this._family = i; } public int get_agrofamily() { return this._agrofamily; } public void set_agrofamily(int i) { this._agrofamily = i; } public int is_agrogfxid1() { return this._agrogfxid1; } public void set_agrogfxid1(int i) { this._agrogfxid1 = i; } public int is_agrogfxid2() { return this._agrogfxid2; } public void set_agrogfxid2(int i) { this._agrogfxid2 = i; } public boolean is_picupitem() { return this._picupitem; } public void set_picupitem(boolean flag) { this._picupitem = flag; } public int get_digestitem() { return this._digestitem; } public void set_digestitem(int i) { this._digestitem = i; } public int get_hprinterval() { return this._hprinterval; } public void set_hprinterval(int i) { this._hprinterval = i; } public int get_hpr() { return this._hpr; } public void set_hpr(int i) { this._hpr = i; } public int get_mprinterval() { return this._mprinterval; } public void set_mprinterval(int i) { this._mprinterval = i; } public int get_mpr() { return this._mpr; } public void set_mpr(int i) { this._mpr = i; } public boolean is_teleport_run() { return this._teleport_run; } public void set_teleport_run(boolean flag) { this._teleport_run = flag; } public boolean is_teleport() { return this._teleport; } public void set_teleport(boolean flag) { this._teleport = flag; } public int get_randomlevel() { return this._randomlevel; } public void set_randomlevel(int i) { this._randomlevel = i; } public int get_randomhp() { return this._randomhp; } public void set_randomhp(int i) { this._randomhp = i; } public int get_randommp() { return this._randommp; } public void set_randommp(int i) { this._randommp = i; } public int get_randomac() { return this._randomac; } public void set_randomac(int i) { this._randomac = i; } public int get_randomexp() { return this._randomexp; } public void set_randomexp(int i) { this._randomexp = i; } public int get_randomlawful() { return this._randomlawful; } public void set_randomlawful(int i) { this._randomlawful = i; } public int get_damagereduction() { return this._damagereduction; } public void set_damagereduction(int i) { this._damagereduction = i; } public boolean is_hard() { return this._hard; } public void set_hard(boolean flag) { this._hard = flag; } public boolean is_doppel() { return this._doppel; } public void set_doppel(boolean flag) { this._doppel = flag; } public void set_IsTU(boolean i) { this._tu = i; } public boolean get_IsTU() { return this._tu; } public void set_IsErase(boolean i) { this._erase = i; } public boolean get_IsErase() { return this._erase; } public void setBowActId(int i) { this.bowActId = i; }




   public int getKarma() {
     return this._karma;
   }

   public void setKarma(int i) {
     this._karma = i;
   }



   public int getTransformId() {
     return this._transformId;
   }

   public void setTransformId(int transformId) {
     this._transformId = transformId;
   }



   public int getTransformGfxId() {
     return this._transformGfxId;
   }

   public void setTransformGfxId(int i) {
     this._transformGfxId = i;
   }



   public int getLightSize() {
     return this._lightSize;
   }

   public void setLightSize(int lightSize) {
     this._lightSize = lightSize;
   }



   public boolean isAmountFixed() {
     return this._amountFixed;
   }

   public void setAmountFixed(boolean fixed) {
     this._amountFixed = fixed;
   }



   public boolean getChangeHead() {
     return this._changeHead;
   }

   public void setChangeHead(boolean changeHead) {
     this._changeHead = changeHead;
   }



   public void setDoor(int doorId) {
     this.doorId = doorId;
   }

   public int getDoor() {
     return this.doorId;
   }



   public void setCountId(int countId) {
     this.countId = countId;
   }

   public int getCountId() {
     return this.countId;
   }



   public boolean isCantResurrect() {
     return this._isCantResurrect;
   }

   public void setCantResurrect(boolean isCantResurrect) {
     this._isCantResurrect = isCantResurrect;
   }



   public boolean isShapeChange() {
     return this._isShapeChange;
   }

   public void setShapeChange(boolean a) {
     this._isShapeChange = a;
   }



   public boolean isboss() {
     return this.boss;
   }

   public void setboss(boolean Boss) {
     this.boss = Boss;
   }



   public void createBorn(ResultSet rs) throws SQLException {
     this._born = new BornAction();
     this._born.bornAction = rs.getInt("action");
     this._born.probability_byMillion = rs.getInt("probability") * 10000;
   }

   public void doBornNpc(L1NpcInstance npc) {
     if (this._born != null)
       this._born.doBorn(npc);
   }

   public long doProbabilityBornNpc(L1NpcInstance npc) {
     return (this._born != null && MJRnd.isWinning(1000000, this._born.probability_byMillion)) ? this._born.doBornAndGetInterval(npc) : 0L;
   }

   public class BornAction {
     public int bornAction;
     public int probability_byMillion;

     public void doBorn(L1NpcInstance npc) {
       npc.setParalysisTime((int)doBornAndGetInterval(npc));
     }



     public long doBornAndGetInterval(L1NpcInstance npc) {
       npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc));
       npc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(npc.getId(), this.bornAction));
       return npc.getCurrentSpriteInterval(this.bornAction);
     }
   }



   public int getTransformProbability() {
     return this._transform_probability;
   }

   public void setTransformProbability(int i) {
     this._transform_probability = i;
   }



   public boolean isTransformHard() {
     return this._transform_hard;
   }

   public void setTransformHard(boolean flag) {
     this._transform_hard = flag;
   }



   public boolean isTransformdrop() {
     return this._transform_drop;
   }

   public void setTransformdrop(boolean flag) {
     this._transform_drop = flag;
   }



   public boolean isMoveMent() {
     return this._movement;
   }

   public void setMoveMent(boolean flag) {
     this._movement = flag;
   }



   public boolean isAgro_lvl() {
     return this.agro_lvl;
   }

   public void setAgro_lvl(boolean flag) {
     this.agro_lvl = flag;
   }

   public void setInfoNumber(int num) {
     this.infoNumber = num;
   }
   public int getInfoNumber() {
     return this.infoNumber;
   } }



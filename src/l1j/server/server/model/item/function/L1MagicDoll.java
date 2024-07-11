 package l1j.server.server.model.item.function;

 import java.io.File;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javax.xml.bind.JAXBContext;
 import javax.xml.bind.Unmarshaller;
 import javax.xml.bind.annotation.XmlAccessType;
 import javax.xml.bind.annotation.XmlAccessorType;
 import javax.xml.bind.annotation.XmlAttribute;
 import javax.xml.bind.annotation.XmlElement;
 import javax.xml.bind.annotation.XmlRootElement;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SUMMON_PET_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_UPDATE_INVENTORY_NOTI;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1Skills;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.MJBytesOutputStream;

 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1MagicDoll
 {
   private static Logger _log = Logger.getLogger(L1MagicDoll.class.getName());

   private static final String _path = "./data/xml/Item/MagicDoll.xml";
   private static HashMap<Integer, L1MagicDoll> _dataMap = new HashMap<>();

   private static HashMap<Integer, ArrayList<Integer>> _gradeMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlElement(name = "Info")
   private Info _info;

   @XmlElement(name = "Effect")
   private Effect _effect;

   public static L1MagicDoll get(int itemId) {
     return _dataMap.get(Integer.valueOf(itemId));
   }

   public static ArrayList<Integer> getGrade(int grade) {
     return _gradeMap.get(Integer.valueOf(grade));
   }

   public int getItemId() {
     return this._itemId;
   }

   public Info getInfo() {
     return this._info;
   }

   public Effect getEffect() {
     return this._effect;
   }

     // 初始化方法，檢查魔法娃娃的物品 ID 是否有效
     private boolean init() {
            // 檢查物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
                // 如果物品 ID 不存在，輸出錯誤信息並返回 false
             System.out.println("MagicDoll：不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }

        // 如果物品 ID 存在，返回 true 表示初始化成功
         return true;
     }



   private static void loadXml(HashMap<Integer, L1MagicDoll> dataMap, HashMap<Integer, ArrayList<Integer>> gradeMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/MagicDoll.xml");
       ItemList list = (ItemList)um.unmarshal(file);

       for (L1MagicDoll each : list) {
         if (each.init()) {
           dataMap.put(Integer.valueOf(each.getItemId()), each);
           ArrayList<Integer> gradeList = gradeMap.get(
               Integer.valueOf(each.getInfo().getGrade()));
           if (gradeList == null) {
             gradeList = new ArrayList<>();
           }
           gradeList.add(Integer.valueOf(each.getItemId()));
           gradeMap.put(Integer.valueOf(each.getInfo().getGrade()), gradeList);
         }
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/MagicDoll.xml載入失敗.", e);
     }
   }


   public static void load() {
     loadXml(_dataMap, _gradeMap);
   }

   public static void reload() {
     HashMap<Integer, L1MagicDoll> dataMap = new HashMap<>();
     HashMap<Integer, ArrayList<Integer>> gradeMap = new HashMap<>();
     loadXml(dataMap, gradeMap);
     _dataMap = dataMap;
     _gradeMap = gradeMap;
   }

   public boolean use(L1PcInstance pc, L1ItemInstance item) {
     if (item == null) {
       return false;
     }
     if (item.getLastUsed() != null &&
       System.currentTimeMillis() - item.getLastUsed().getTime() < 3000L) {
       return false;
     }


     if (!pc.getMap().isRecallPets()) {
       pc.sendPackets(4451);
       return false;
     }

     if (!pc.getMap().isTakePets()) {
       pc.sendPackets(5276);
       return false;
     }

     int itemObjectId = item.getId();
     Info info = getInfo();
     Effect effect = getEffect();

     if (info != null && effect != null) {
       L1DollInstance doll = pc.getMagicDoll();
       if (doll != null && doll.getItemObjId() == itemObjectId) {
         item.setLastUsed(new Timestamp(System.currentTimeMillis()));
         SC_SUMMON_PET_NOTI.off_summoned(pc);
         doll.deleteDoll();
         return false;
       }

       if (!pc.getInventory().checkItem(41246, info.getCrystalCount())) {
         SC_SUMMON_PET_NOTI.off_summoned(pc);
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(337, "$5240"));
         return false;
       }

       if (doll != null) {
         SC_SUMMON_PET_NOTI.off_summoned(pc);
         doll.deleteDoll();
       }

       item.setLastUsed(new Timestamp(System.currentTimeMillis()));
       L1Npc template = NpcTable.getInstance().getTemplate(80106);
       template.set_name(info.getName());
       template.set_nameid(info.getNameId());
       template.set_gfxid(info.getCurrentSpriteId());
       doll = new L1DollInstance(template, pc, item, info.getClassId());



       SC_SUMMON_PET_NOTI.on_summoned(pc, item, info.getSummonTime(), info.getClassId());

       SC_UPDATE_INVENTORY_NOTI.on_doll_summoned(pc, item.getId(), true);

       pc.getInventory().consumeItem(41246, info.getCrystalCount());
       return true;
     }
     return false;
   }

   public static int getItemIdByName(String name) {
     String dollName = null;
     for (L1MagicDoll dollItem : _dataMap.values()) {
       dollName = dollItem.getInfo().getName().replaceAll(" ", "");
       if (dollName.contains(name)) {
         return dollItem.getItemId();
       }
     }
     return -1;
   }

   public static int getDamageAddByDoll(L1Character _master) {
     int dmg = 0;
     if (_master.getMagicDoll() != null) {

       int prob = _master.getMagicDoll().getDoll().getEffect().getDmgChance();
       if (prob > 0 && prob > CommonUtil.random(100)) {
         dmg += _master.getMagicDoll().getDoll().getEffect().getDmg();
         if (_master instanceof L1PcInstance) {
           L1PcInstance pc = (L1PcInstance)_master;
           pc.sendPackets((ServerBasePacket)new S_SkillSound(_master.getId(), 6319));
         }
         _master.broadcastPacket((ServerBasePacket)new S_SkillSound(_master.getId(), 6319));
       }
     }
     return dmg;
   }


   public static int getDamageReductionByDoll(L1Character _attacker, L1Character _master) {
     int dmg = 0;
     if (_master.getMagicDoll() != null) {
       if (_attacker instanceof L1PcInstance && _master
         .getMagicDoll().getItemId() == 49326) {
         return 0;
       }

       int prob = _master.getMagicDoll().getDoll().getEffect().getDmgReductionChance();
       if (prob > 0 && prob > CommonUtil.random(100)) {
         dmg += _master.getMagicDoll().getDoll().getEffect()
           .getDmgReduction();
         if (_master instanceof L1PcInstance) {
           L1PcInstance pc = (L1PcInstance)_master;
           pc.sendPackets((ServerBasePacket)new S_SkillSound(_master.getId(), 6320));
         }
         _master.broadcastPacket((ServerBasePacket)new S_SkillSound(_master.getId(), 6320));
       }
     }
     return dmg;
   }

   public static boolean isDamageEvasionByDoll(L1Character _master) {
     if (_master.getMagicDoll() != null) {

       int prob = _master.getMagicDoll().getDoll().getEffect().getDmgEvasionChance();
       if (prob > 0 && prob > CommonUtil.random(100)) {
         if (_master instanceof L1PcInstance) {
           L1PcInstance pc = (L1PcInstance)_master;
           pc.sendPackets((ServerBasePacket)new S_SkillSound(_master.getId(), 6320));
         }
         _master.broadcastPacket((ServerBasePacket)new S_SkillSound(_master.getId(), 6320));

         return true;
       }
     }
     return false;
   }

   public static int useSkillByDoll(L1Character _master, L1Character _target) {
     int dmg = 0;
     if (_master.getMagicDoll() != null) {
       L1MagicDoll magicDoll = _dataMap.get(
           Integer.valueOf(_master.getMagicDoll().getItemId()));
       if (magicDoll != null) {
         int skillId = magicDoll.getEffect().getSkillId();
         if (skillId > 0 && magicDoll
           .getEffect().getSkillChance() >
           CommonUtil.random(100)) {
           L1DollInstance doll = _master.getMagicDoll();
           L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);


           if (_target == null || doll == null || _master == null) {
             return dmg;
           }

           dmg = skill.getDamageValue() + CommonUtil.random(skill.getDamageDice() + 1);

           if (skill.getTarget().equals("attack")) {
             if (_target instanceof L1PcInstance) {
               ((L1PcInstance)_target)
                 .sendPackets((ServerBasePacket)new S_SkillSound(_target
                     .getId(), skill.getCastGfx()));
             }
             doll.setHeading(doll.targetDirection(_target.getX(), _target
                   .getY()));
             _master.sendPackets((ServerBasePacket)new S_ChangeHeading((L1Character)doll));
             _master.sendPackets((ServerBasePacket)new S_DoActionGFX(doll.getId(), 1));
             _target.broadcastPacket((ServerBasePacket)new S_SkillSound(_target
                   .getId(), skill.getCastGfx()));
           } else if (skill.getTarget().equals("none")) {
             doll.setHeading(doll.targetDirection(_target.getX(), _target
                   .getY()));
             _master.sendPackets((ServerBasePacket)new S_ChangeHeading((L1Character)doll));
             _master.sendPackets((ServerBasePacket)new S_DoActionGFX(doll.getId(), 1));
             doll.broadcastPacket((ServerBasePacket)new S_SkillSound(doll.getId(), skill
                   .getCastGfx()));
           }
         }
       }
     }
     return dmg;
   }


   @XmlAccessorType(XmlAccessType.FIELD)
   public static class Effect
   {
     @XmlAttribute(name = "Ac")
     private int _ac;

     @XmlAttribute(name = "Str")
     private int _str;

     @XmlAttribute(name = "Con")
     private int _con;

     @XmlAttribute(name = "Dex")
     private int _dex;

     @XmlAttribute(name = "Int")
     private int _int;

     @XmlAttribute(name = "Wis")
     private int _wis;

     @XmlAttribute(name = "Cha")
     private int _cha;

     @XmlAttribute(name = "Hp")
     private int _hp;

     @XmlAttribute(name = "Mp")
     private int _mp;

     @XmlAttribute(name = "Hpr")
     private int _hpr;

     @XmlAttribute(name = "Mpr")
     private int _mpr;

     @XmlAttribute(name = "HprTime")
     private int _hprTime;

     @XmlAttribute(name = "MprTime")
     private int _mprTime;

     @XmlAttribute(name = "Mr")
     private int _mr;

     @XmlAttribute(name = "Sp")
     private int _sp;

     @XmlAttribute(name = "Dmg")
     private int _dmg;

     @XmlAttribute(name = "BowDmg")
     private int _bowDmg;

     @XmlAttribute(name = "DmgChance")
     private int _dmgChance;

     @XmlAttribute(name = "Hit")
     private int _hit;

     @XmlAttribute(name = "BowHit")
     private int _bowHit;

     @XmlAttribute(name = "DmgReduction")
     private int _dmgReduction;

     @XmlAttribute(name = "DmgReductionChance")
     private int _dmgReductionChance;

     @XmlAttribute(name = "DmgEvasionChance")
     private int _dmgEvasionChance;
     @XmlAttribute(name = "WeightReduction")
     private int _weightReduction;
     @XmlAttribute(name = "TechniqueTolerance")
     private int _technique_tolerance = 0;

     @XmlAttribute(name = "SpiritTolerance")
     private int _spirit_tolerance = 0;

     @XmlAttribute(name = "DragonlangTolerance")
     private int _dragonlang_tolerance = 0;

     @XmlAttribute(name = "FearTolerance")
     private int _fear_tolerance = 0;

     @XmlAttribute(name = "AllTolerance")
     private int _all_tolerance = 0;

     @XmlAttribute(name = "TechniqueHit")
     private int _technique_hit = 0;

     @XmlAttribute(name = "SpiritHit")
     private int _spirit_hit = 0;

     @XmlAttribute(name = "DragonlangHit")
     private int _dragonlang_hit = 0;

     @XmlAttribute(name = "FearHit")
     private int _fear_hit = 0;

     @XmlAttribute(name = "AllHit")
     private int _all_hit = 0;

     @XmlAttribute(name = "Exp")
     private int _expBonus;

     @XmlAttribute(name = "Make")
     private int _makeItemId;

     @XmlAttribute(name = "MakeTime")
     private int _makeTime;

     @XmlAttribute(name = "SkillId")
     private int _skillId;

     @XmlAttribute(name = "SkillChance")
     private int _skillChance;

     @XmlAttribute(name = "FoeDmg")
     private int _foeDmg;

     @XmlAttribute(name = "TitanPercent")
     private int _titanPercent;

     @XmlAttribute(name = "Eva")
     private boolean _isBlessOfEva;

     @XmlAttribute(name = "Haste")
     private boolean _isHaste;

     @XmlAttribute(name = "MagicHit")
     private int _magicHit;

     @XmlAttribute(name = "PvpDmg")
     private int _pvpDmg;

     @XmlAttribute(name = "PvpReduction")
     private int _pvpReduction;

     @XmlAttribute(name = "SkillNameId")
     private String _skillnameId;

     @XmlAttribute(name = "AinEfficiency")
     private int _ainEfficiency;

     @XmlAttribute(name = "DragonDmgDecrease")
     private int _dragonDmgDecrease;

     @XmlAttribute(name = "AdenBonus")
     private double _AdenBonus;

     @XmlAttribute(name = "ItemBonus")
     private double _ItemBonus;

     @XmlAttribute(name = "fourthgear")
     private boolean _fourthgear = false;

     @XmlAttribute(name = "ShortCritical")
     private int _ShortCritical;

     @XmlAttribute(name = "LongCritical")
     private int _LongCritical;

     @XmlAttribute(name = "MagicCritical")
     private int _MagicCritical;


     public int getAc() {
       return this._ac;
     }

     public int getStr() {
       return this._str;
     }

     public int getCon() {
       return this._con;
     }

     public int getDex() {
       return this._dex;
     }

     public int getInt() {
       return this._int;
     }

     public int getWis() {
       return this._wis;
     }

     public int getCha() {
       return this._cha;
     }

     public int getHp() {
       return this._hp;
     }

     public int getMp() {
       return this._mp;
     }

     public int getHpr() {
       return this._hpr;
     }

     public int getMpr() {
       return this._mpr;
     }

     public int getHprTime() {
       return this._hprTime;
     }

     public int getMprTime() {
       return this._mprTime;
     }

     public int getMr() {
       return this._mr;
     }

     public int getSp() {
       return this._sp;
     }

     public int getDmg() {
       return this._dmg;
     }

     public int getBowDmg() {
       return this._bowDmg;
     }

     public int getDmgChance() {
       return this._dmgChance;
     }

     public int getHit() {
       return this._hit;
     }

     public int getBowHit() {
       return this._bowHit;
     }

     public int getDmgReduction() {
       return this._dmgReduction;
     }

     public int getDmgReductionChance() {
       return this._dmgReductionChance;
     }

     public int getDmgEvasionChance() {
       return this._dmgEvasionChance;
     }

     public int getWeightReduction() {
       return this._weightReduction;
     }

     public int getTechniqueTolerance() {
       return this._technique_tolerance;
     }

     public int getSpiritTolerance() {
       return this._spirit_tolerance;
     }

     public int getDragonLangTolerance() {
       return this._dragonlang_tolerance;
     }

     public int getFearTolerance() {
       return this._fear_tolerance;
     }

     public int getAllTolerance() {
       return this._all_tolerance;
     }

     public int getTechniqueHit() {
       return this._technique_hit;
     }

     public int getSpiritHit() {
       return this._spirit_hit;
     }

     public int getDragonLangHit() {
       return this._dragonlang_hit;
     }

     public int getFearHit() {
       return this._fear_hit;
     }

     public int getAllHit() {
       return this._all_hit;
     }

     public int getExpBonus() {
       return this._expBonus;
     }

     public int getMakeItemId() {
       return this._makeItemId;
     }

     public int getMakeTime() {
       return this._makeTime;
     }

     public int getSkillId() {
       return this._skillId;
     }

     public int getSkillChance() {
       return this._skillChance;
     }

     public int getFoeDmg() {
       return this._foeDmg;
     }

     public int getTitanPercent() {
       return this._titanPercent;
     }

     public boolean isBlessOfEva() {
       return this._isBlessOfEva;
     }

     public boolean isHaste() {
       return this._isHaste;
     }

     public int getMagicHit() {
       return this._magicHit;
     }

     public String getSkillNameId() {
       return this._skillnameId;
     }

     public int getAinEfficiency() {
       return this._ainEfficiency;
     }

     public int getPvpDmg() {
       return this._pvpDmg;
     }

     public int getPvpReduction() {
       return this._pvpReduction;
     }

     public int getDragonDmgDecrease() {
       return this._dragonDmgDecrease;
     }

     public double getAdenBonus() {
       return this._AdenBonus;
     }

     public double getItemBonus() {
       return this._ItemBonus;
     }

     public boolean getfourgear() {
       return this._fourthgear;
     }

     public int getShortCritical() {
       return this._ShortCritical;
     }

     public int getLongCritical() {
       return this._LongCritical;
     }

     public int getMagicCritical() {
       return this._MagicCritical;
     }
   }


   @XmlAccessorType(XmlAccessType.FIELD)
   public static class Info
   {
     @XmlAttribute(name = "Name")
     private String _name;

     @XmlAttribute(name = "NameId")
     private String _nameId;

     @XmlAttribute(name = "Time")
     private int _summonTime;

     @XmlAttribute(name = "Crystal")
     private int _crystalCount;

     @XmlAttribute(name = "ClassId")
     private int _class_id;

     @XmlAttribute(name = "Spriteid")
     private int _Spriteid;

     @XmlAttribute(name = "Grade")
     private int _grade;

     @XmlAttribute(name = "Bless")
     private int _bless;


     public int getBless() {
       return this._bless;
     }
     public String getName() {
       return this._name;
     }

     public String getNameId() {
       return this._nameId;
     }

     public int getSummonTime() {
       return this._summonTime;
     }

     public int getCrystalCount() {
       return this._crystalCount;
     }

     public int getClassId() {
       return this._class_id;
     }

     public int getCurrentSpriteId() {
       return this._Spriteid;
     }

     public int getGrade() {
       return this._grade;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemList
     implements Iterable<L1MagicDoll> {
     @XmlElement(name = "Item")
     private List<L1MagicDoll> _list;

     public Iterator<L1MagicDoll> iterator() {
       return this._list.iterator();
     }
   }

   public static byte[] getItemView(L1MagicDoll magicDoll, L1ItemInstance item) {
     byte[] data = null;
     MJBytesOutputStream os = new MJBytesOutputStream(256);
     try {
       int dmg = magicDoll.getEffect().getDmg();
       int dmg_chance = magicDoll.getEffect().getDmgChance();
       if (dmg != 0) {
         if (dmg_chance != 0) {
           os.write(103);
           os.write(dmg);
         } else {
           os.write(47);
           os.write(dmg);
         }
       }

       int hit = magicDoll.getEffect().getHit();
       if (hit != 0) {
         os.write(48);
         os.write(hit);
       }

       int bowDmg = magicDoll.getEffect().getBowDmg();
       if (bowDmg != 0) {
         os.write(35);
         os.write(bowDmg);
       }

       int bowHit = magicDoll.getEffect().getBowHit();
       if (bowHit != 0) {
         os.write(24);
         os.write(bowHit);
       }

       int mr = magicDoll.getEffect().getMr();
       if (mr != 0) {
         os.write(15);
         os.writeH(mr);
       }

       int sp = magicDoll.getEffect().getSp();
       if (sp != 0) {
         os.write(17);
         os.write(sp);
       }

       if (magicDoll.getEffect().isHaste()) {
         os.write(18);
       }

       int regenHp = magicDoll.getEffect().getHpr();
       if (regenHp != 0) {
         os.write(87);
         os.write(regenHp);
       }

       int regenMp = magicDoll.getEffect().getMpr();
       if (regenMp != 0) {
         os.write(88);
         os.write(regenMp);
       }

       int acBonus = magicDoll.getEffect().getAc();
       if (acBonus != 0) {
         os.write(56);
         os.write(-acBonus);
       }

       int maxHp = magicDoll.getEffect().getHp();
       if (maxHp != 0) {
         os.write(14);
         os.writeH(maxHp);
       }

       int maxMp = magicDoll.getEffect().getMp();
       if (maxMp != 0) {
         os.write(32);
         os.writeH(maxMp);
       }

       int reduction = magicDoll.getEffect().getDmgReduction();
       if (reduction != 0) {
         os.write(63);
         os.write(reduction);
       }

       int weight = magicDoll.getEffect().getWeightReduction();
       if (weight != 0) {
         os.write(90);
         os.writeH(weight);
       }

       int str = magicDoll.getEffect().getStr();
       if (str != 0) {
         os.write(8);
         os.write(str);
       }

       int dex = magicDoll.getEffect().getDex();
       if (dex != 0) {
         os.write(9);
         os.write(dex);
       }

       int con = magicDoll.getEffect().getCon();
       if (con != 0) {
         os.write(10);
         os.write(con);
       }

       int intel = magicDoll.getEffect().getInt();
       if (intel != 0) {
         os.write(12);
         os.write(intel);
       }

       int wis = magicDoll.getEffect().getWis();
       if (wis != 0) {
         os.write(11);
         os.write(wis);
       }

       int cha = magicDoll.getEffect().getCha();
       if (cha != 0) {
         os.write(10);
         os.write(cha);
       }

       int magicHit = magicDoll.getEffect().getMagicHit();
       if (magicHit != 0) {
         os.write(40);
         os.write(magicHit);
       }

       int pvpDmg = magicDoll.getEffect().getPvpDmg();
       if (pvpDmg != 0) {
         os.write(59);
         os.write(pvpDmg);
       }

       int pvpReduction = magicDoll.getEffect().getPvpReduction();
       if (pvpReduction != 0) {
         os.write(60);
         os.write(pvpReduction);
       }

       int foeDmg = magicDoll.getEffect().getFoeDmg();
       if (foeDmg > 0) {
         os.write(101);
         os.write(foeDmg);
       }


       int technique_tolerance = magicDoll.getEffect().getTechniqueTolerance();
       if (technique_tolerance != 0) {
         os.write(117);
         os.write(technique_tolerance);
       }

       int spirit_tolerance = magicDoll.getEffect().getSpiritTolerance();
       if (spirit_tolerance != 0) {
         os.write(118);
         os.write(spirit_tolerance);
       }


       int dragonlang_tolerance = magicDoll.getEffect().getDragonLangTolerance();
       if (dragonlang_tolerance != 0) {
         os.write(119);
         os.write(dragonlang_tolerance);
       }

       int fear_tolerance = magicDoll.getEffect().getFearTolerance();
       if (fear_tolerance != 0) {
         os.write(120);
         os.write(fear_tolerance);
       }

       int all_tolerance = magicDoll.getEffect().getAllTolerance();
       if (all_tolerance != 0) {
         os.write(121);
         os.write(all_tolerance);
       }

       int technique_hit = magicDoll.getEffect().getTechniqueHit();
       if (technique_hit != 0) {
         os.write(122);
         os.write(technique_hit);
       }

       int spirit_hit = magicDoll.getEffect().getSpiritHit();
       if (spirit_hit != 0) {
         os.write(123);
         os.write(spirit_hit);
       }

       int dragonlang_hit = magicDoll.getEffect().getDragonLangHit();
       if (dragonlang_hit != 0) {
         os.write(124);
         os.write(dragonlang_hit);
       }

       int fear_hit = magicDoll.getEffect().getFearHit();
       if (fear_hit != 0) {
         os.write(125);
         os.write(fear_hit);
       }

       int all_hit = magicDoll.getEffect().getAllHit();
       if (all_hit != 0) {
         os.write(126);
         os.write(all_hit);
       }

       int exp = magicDoll.getEffect().getExpBonus();
       if (exp != 0) {
         os.write(36);
         os.write(exp);
       }

       int titanPercent = magicDoll.getEffect().getTitanPercent();
       if (titanPercent > 0) {
         os.write(102);
         os.write(titanPercent);
       }

       int skillId = magicDoll.getEffect().getSkillId();
       if (skillId > 0) {
         os.write(74);
         os.writeS(magicDoll.getEffect().getSkillNameId());
       }

       int AinEfficiency = magicDoll.getEffect().getAinEfficiency();
       if (AinEfficiency > 0) {
         os.write(116);
         os.writeH(AinEfficiency);
       }


            // 獲取魔法娃娃對龍的傷害減少效果
         int dragonDmgDecrease = magicDoll.getEffect().getDragonDmgDecrease();
         if (dragonDmgDecrease > 0) {
                // 如果減少效果大於 0，寫入效果編碼和描述信息
             os.write(39);
             os.writeS("對龍的傷害減少 " + dragonDmgDecrease + "%");
         }

            // 獲取魔法娃娃的阿德納獎勵效果
         double AdenBonus = magicDoll.getEffect().getAdenBonus();
         if (AdenBonus > 0.0D) {
                // 如果獎勵效果大於 0.0，寫入效果編碼和描述信息
             os.write(39);
             os.writeS("額外獲得金幣: +" + (int)(AdenBonus * 100.0D) + "%");
         }

            // 獲取魔法娃娃的物品掉落機率獎勵效果
         double ItemBonus = magicDoll.getEffect().getItemBonus();
         if (ItemBonus > 0.0D) {
                // 如果獎勵效果大於 0.0，寫入效果編碼和描述信息
             os.write(39);
             os.writeS("額外物品掉落機率: +" + (int)(ItemBonus * 100.0D) + "%");
         }

       if (item.get_Doll_Bonus_Value() > 0) {
         if ((item.get_Doll_Bonus_Value() >= 144 && item.get_Doll_Bonus_Value() <= 146) || (item
           .get_Doll_Bonus_Value() >= 148 && item.get_Doll_Bonus_Value() <= 163)) {
           int special_bonus_value = 0;
           switch (item.get_Doll_Bonus_Value()) {
             case 144:
               special_bonus_value = 37865;
               break;
             case 145:
               special_bonus_value = 37866;
               break;
             case 146:
               special_bonus_value = 33913;
               break;
             case 148:
               special_bonus_value = 40575;
               break;
             case 149:
               special_bonus_value = 40576;
               break;
             case 150:
               special_bonus_value = 40577;
               break;
             case 151:
               special_bonus_value = 40578;
               break;
             case 152:
               special_bonus_value = 40579;
               break;
             case 153:
               special_bonus_value = 40580;
               break;
             case 154:
               special_bonus_value = 40581;
               break;
             case 155:
               special_bonus_value = 40585;
               break;
             case 156:
               special_bonus_value = 40586;
               break;
             case 157:
               special_bonus_value = 40587;
               break;
             case 158:
               special_bonus_value = 40588;
               break;
             case 159:
               special_bonus_value = 40589;
               break;
             case 160:
               special_bonus_value = 40590;
               break;
             case 161:
               special_bonus_value = 40591;
               break;
             case 162:
               special_bonus_value = 40592;
               break;
             case 163:
               special_bonus_value = 40593;
               break;
           }




             // 檢查特殊獎勵值是否為 0
             if (special_bonus_value == 0) {
            // 如果特殊獎勵值為 0，輸出錯誤信息
                 System.out.println("魔法娃娃選項錯誤");
             }
           os.write(161);
           os.writeD(item.get_Doll_Bonus_Level() + 1);
           os.writeD(special_bonus_value);
         } else {
           os.write(161);
           os.writeD(item.get_Doll_Bonus_Level() + 1);
           os.writeD(item.get_Doll_Bonus_Value() + 32065);
         }
       }

            // 獲取魔法娃娃的四級加速效果
         boolean fourgear = magicDoll.getEffect().getfourgear();
         if (fourgear) {
            // 如果有四級加速效果，寫入效果編碼和描述信息
             os.write(74);
             os.writeS("4級加速");
         }

                // 獲取魔法娃娃的近戰暴擊率效果
         int ShortCritical = magicDoll.getEffect().getShortCritical();
         if (ShortCritical > 0) {
                    // 如果近戰暴擊率大於 0，寫入效果編碼和描述信息
             os.write(39);
             os.writeS("\fI近戰暴擊率: \aA+" + ShortCritical);
         }

                // 獲取魔法娃娃的遠程暴擊率效果
         int LongCritical = magicDoll.getEffect().getLongCritical();
         if (LongCritical > 0) {
                // 如果遠程暴擊率大於 0，寫入效果編碼和描述信息
             os.write(39);
             os.writeS("\fI遠程暴擊率: \aA+" + LongCritical);
         }

                // 獲取魔法娃娃的魔法暴擊率效果
         int MagicCritical = magicDoll.getEffect().getMagicCritical();
         if (MagicCritical > 0) {
                // 如果魔法暴擊率大於 0，寫入效果編碼和描述信息
             os.write(39);
             os.writeS("\fI魔法暴擊率: \aA+" + MagicCritical);
         }

       data = os.toArray();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       os.close();
       os.dispose();
     }
     return data;
   }
 }



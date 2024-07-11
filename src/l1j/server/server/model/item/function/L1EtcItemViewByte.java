 package l1j.server.server.model.item.function;

 import java.io.File;
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
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.utils.MJBytesOutputStream;


 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1EtcItemViewByte
 {
   private static Logger _log = Logger.getLogger(L1EtcItemViewByte.class.getName());

   private static final String _path = "./data/xml/Item/EtcItemView.xml";
   private static HashMap<Integer, L1EtcItemViewByte> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlElement(name = "Effect")
   private Effect _effect;

   public static L1EtcItemViewByte get(int itemId) {
     return _dataMap.get(Integer.valueOf(itemId));
   }

   public int getItemId() {
     return this._itemId;
   }

   public Effect getEffect() {
     return this._effect;
   }

     // 初始化方法，檢查物品的合法性
     private boolean init() {
    // 檢查物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
    // 如果物品 ID 不存在，輸出錯誤信息並返回 false
             System.out.println("EtcItemViewByte：不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }

    // 如果物品 ID 存在，返回 true 表示初始化成功
         return true;
     }

   private static void loadXml(HashMap<Integer, L1EtcItemViewByte> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/EtcItemView.xml");
       ItemList list = (ItemList)um.unmarshal(file);

       for (L1EtcItemViewByte each : list) {
         if (each.init())
           dataMap.put(Integer.valueOf(each.getItemId()), each);
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/EtcItemView.xml載入失敗.", e);
     }
   }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1EtcItemViewByte> dataMap = new HashMap<>();
     loadXml(dataMap);
     _dataMap = dataMap;
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

     @XmlAttribute(name = "CustomMsg")
     private String _customMsg;


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

     public String getCustomMsg() {
       return this._customMsg;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemList
     implements Iterable<L1EtcItemViewByte> {
     @XmlElement(name = "Item")
     private List<L1EtcItemViewByte> _list;

     public Iterator<L1EtcItemViewByte> iterator() {
       return this._list.iterator();
     }
   }

   public static byte[] getItemView(L1EtcItemViewByte etcview, L1ItemInstance item) {
     byte[] data = null;
     MJBytesOutputStream os = new MJBytesOutputStream(256);
     try {
       int dmg = etcview.getEffect().getDmg();
       int dmg_chance = etcview.getEffect().getDmgChance();
       if (dmg != 0) {
         if (dmg_chance != 0) {
           os.write(103);
           os.write(dmg);
         } else {
           os.write(47);
           os.write(dmg);
         }
       }

       int hit = etcview.getEffect().getHit();
       if (hit != 0) {
         os.write(48);
         os.write(hit);
       }

       int bowDmg = etcview.getEffect().getBowDmg();
       if (bowDmg != 0) {
         os.write(35);
         os.write(bowDmg);
       }

       int bowHit = etcview.getEffect().getBowHit();
       if (bowHit != 0) {
         os.write(24);
         os.write(bowHit);
       }

       int sp = etcview.getEffect().getSp();
       if (sp != 0) {
         os.write(17);
         os.write(sp);
       }

       if (etcview.getEffect().isHaste()) {
         os.write(18);
       }

       int regenHp = etcview.getEffect().getHpr();
       if (regenHp != 0) {
         os.write(87);
         os.write(regenHp);
       }

       int regenMp = etcview.getEffect().getMpr();
       if (regenMp != 0) {
         os.write(88);
         os.write(regenMp);
       }

       int acBonus = etcview.getEffect().getAc();
       if (acBonus != 0) {
         os.write(56);
         os.write(-acBonus);
       }

       int maxHp = etcview.getEffect().getHp();
       if (maxHp != 0) {
         os.write(14);
         os.writeH(maxHp);
       }

       int maxMp = etcview.getEffect().getMp();
       if (maxMp != 0) {
         os.write(32);
         os.writeH(maxMp);
       }

       int reduction = etcview.getEffect().getDmgReduction();
       if (reduction != 0) {
         os.write(63);
         os.write(reduction);
       }

       int weight = etcview.getEffect().getWeightReduction();
       if (weight != 0) {
         os.write(90);
         os.writeH(weight);
       }

       int str = etcview.getEffect().getStr();
       if (str != 0) {
         os.write(8);
         os.write(str);
       }

       int dex = etcview.getEffect().getDex();
       if (dex != 0) {
         os.write(9);
         os.write(dex);
       }

       int con = etcview.getEffect().getCon();
       if (con != 0) {
         os.write(10);
         os.write(con);
       }

       int intel = etcview.getEffect().getInt();
       if (intel != 0) {
         os.write(12);
         os.write(intel);
       }

       int wis = etcview.getEffect().getWis();
       if (wis != 0) {
         os.write(11);
         os.write(wis);
       }

       int cha = etcview.getEffect().getCha();
       if (cha != 0) {
         os.write(10);
         os.write(cha);
       }

       int pvpDmg = etcview.getEffect().getPvpDmg();
       if (pvpDmg != 0) {
         os.write(59);
         os.write(pvpDmg);
       }

       int pvpReduction = etcview.getEffect().getPvpReduction();
       if (pvpReduction != 0) {
         os.write(60);
         os.write(pvpReduction);
       }

       int technique_tolerance = etcview.getEffect().getTechniqueTolerance();
       if (technique_tolerance != 0) {
         os.write(117);
         os.write(technique_tolerance);
       }

       int spirit_tolerance = etcview.getEffect().getSpiritTolerance();
       if (spirit_tolerance != 0) {
         os.write(118);
         os.write(spirit_tolerance);
       }

       int dragonlang_tolerance = etcview.getEffect().getDragonLangTolerance();
       if (dragonlang_tolerance != 0) {
         os.write(119);
         os.write(dragonlang_tolerance);
       }

       int fear_tolerance = etcview.getEffect().getFearTolerance();
       if (fear_tolerance != 0) {
         os.write(120);
         os.write(fear_tolerance);
       }

       int all_tolerance = etcview.getEffect().getAllTolerance();
       if (all_tolerance != 0) {
         os.write(121);
         os.write(all_tolerance);
       }

       int technique_hit = etcview.getEffect().getTechniqueHit();
       if (technique_hit != 0) {
         os.write(122);
         os.write(technique_hit);
       }

       int spirit_hit = etcview.getEffect().getSpiritHit();
       if (spirit_hit != 0) {
         os.write(123);
         os.write(spirit_hit);
       }

       int dragonlang_hit = etcview.getEffect().getDragonLangHit();
       if (dragonlang_hit != 0) {
         os.write(124);
         os.write(dragonlang_hit);
       }

       int fear_hit = etcview.getEffect().getFearHit();
       if (fear_hit != 0) {
         os.write(125);
         os.write(fear_hit);
       }

       int all_hit = etcview.getEffect().getAllHit();
       if (all_hit != 0) {
         os.write(126);
         os.write(all_hit);
       }

       int exp = etcview.getEffect().getExpBonus();
       if (exp != 0) {
         os.write(36);
         os.write(exp);
       }

       int foeDmg = etcview.getEffect().getFoeDmg();
       if (foeDmg > 0) {
         os.write(101);
         os.write(foeDmg);
       }

       int titanPercent = etcview.getEffect().getTitanPercent();
       if (titanPercent > 0) {
         os.write(102);
         os.write(titanPercent);
       }

       int skillId = etcview.getEffect().getSkillId();
       if (skillId > 0) {
         os.write(74);
         os.writeS(etcview.getEffect().getSkillNameId());
       }

       int AinEfficiency = etcview.getEffect().getAinEfficiency();
       if (AinEfficiency > 0) {
         os.write(116);
         os.writeH(AinEfficiency);
       }

       String customMsg = etcview.getEffect().getCustomMsg();
       if (customMsg != null) {
         String[] customMsgs = customMsg.split(";");
         for (int i = 0; i < customMsgs.length; i++) {
           String msg = customMsgs[i];
           os.write(39);
           os.writeS(msg);
         }
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



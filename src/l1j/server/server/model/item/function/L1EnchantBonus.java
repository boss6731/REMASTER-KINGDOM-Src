 package l1j.server.server.model.item.function;

 import java.io.File;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.concurrent.CopyOnWriteArrayList;
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


 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1EnchantBonus
 {
   private static Logger _log = Logger.getLogger(L1EnchantBonus.class.getName());
   private static final String _path = "./data/xml/Item/EnchantBonus.xml";
   private static HashMap<Integer, L1EnchantBonus> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlElement(name = "Effect")
   private CopyOnWriteArrayList<Effect> _effect;

   public static L1EnchantBonus get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }

   private int getItemId() {
     return this._itemId;
   }

   private List<Effect> getEffects() {
     return this._effect;
   }

     // 初始化方法，檢查物品的合法性
     private boolean init() {
    // 檢查物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
    // 如果物品 ID 不存在，輸出錯誤信息並返回 false
             System.out.println("EnchantBonus：不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }
    // 如果物品 ID 存在，返回 true 表示初始化成功
         return true;
     }

   private static void loadXml(HashMap<Integer, L1EnchantBonus> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/EnchantBonus.xml");
       ItemEffectList list = (ItemEffectList)um.unmarshal(file);

       for (L1EnchantBonus each : list) {
         if (each.init()) {
           dataMap.put(Integer.valueOf(each.getItemId()), each);
         }
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/EnchantBonus.xml載入失敗.", e);
     }
   }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1EnchantBonus> dataMap = new HashMap<>();
     loadXml(dataMap);
     _dataMap = dataMap;
   }

   public int getaddAc(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result -= effect.getaddAc();

           break;
         }
       }
     }
     return result;
   }

   public int getAc(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result -= effect.getAc();
         }
       }
     }

     return result;
   }

   public int getStr(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getStr();
         }
       }
     }

     return result;
   }

   public int getDex(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDex();
         }
       }
     }

     return result;
   }

   public int getCon(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getCon();
         }
       }
     }

     return result;
   }

   public int getInt(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getInt();
         }
       }
     }

     return result;
   }

   public int getWis(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getWis();
         }
       }
     }

     return result;
   }

   public int getCha(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getCha();
         }
       }
     }

     return result;
   }

   public int getaddHp(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getaddHp();
         }
       }
     }

     return result;
   }

   public int getHp(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getHp();
         }
       }
     }
     return result;
   }

   public int getHpr(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getHpr();
         }
       }
     }

     return result;
   }

   public int getMp(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMp();
         }
       }
     }
     return result;
   }

   public int getMpr(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMpr();
         }
       }
     }
     return result;
   }

   public int getMr(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMr();
         }
       }
     }
     return result;
   }

   public int getSp(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getSp();
         }
       }
     }

     return result;
   }

   public int getHitModifier(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getHitModifier();
         }
       }
     }
     return result;
   }

   public int getDmgModifier(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDmgModifier();
         }
       }
     }
     return result;
   }

   public int getBowHitModifier(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getBowHitModifier();
         }
       }
     }
     return result;
   }

   public int getBowDmgModifier(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getBowDmgModifier();
         }
       }
     }
     return result;
   }

   public int getWeightReduction(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getWeightReduction();
         }
       }
     }
     return result;
   }

   public int getDamageReduction(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDamageReduction();
         }
       }
     }
     return result;
   }

   public int getDefenseEarth(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDefenseEarth();
         }
       }
     }

     return result;
   }

   public int getDefenseWater(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDefenseWater();
         }
       }
     }

     return result;
   }

   public int getDefenseFire(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDefenseFire();
         }
       }
     }

     return result;
   }

   public int getDefenseWind(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDefenseWind();
         }
       }
     }

     return result;
   }

   public int getDefenseAll(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDefenseAll();
         }
       }
     }

     return result;
   }

   public int getTechniqueTolerance(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getTechniqueTolerance();
         }
       }
     }

     return result;
   }

   public int getSpiritTolerance(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getSpiritTolerance();
         }
       }
     }

     return result;
   }

   public int getDragonLangTolerance(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDragonLangTolerance();
         }
       }
     }
     return result;
   }

   public int getFearTolerance(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getFearTolerance();
         }
       }
     }

     return result;
   }

   public int getAllTolerance(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getAllTolerance();
         }
       }
     }

     return result;
   }

   public int getTechniqueHit(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getTechniqueHit();
         }
       }
     }
     return result;
   }

   public int getSpiritHit(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getSpiritHit();
         }
       }
     }
     return result;
   }

   public int getDragonLangHit(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDragonLangHit();
         }
       }
     }
     return result;
   }

   public int getFearHit(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getFearHit();
         }
       }
     }
     return result;
   }

   public int getMagicDmgModifier(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMagicDmg();
         }
       }
     }
     return result;
   }

   public int getAllHit(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getAllHit();
         }
       }
     }
     return result;
   }

   public int getExpBonus(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getExpBonus();
         }
       }
     }
     return result;
   }

   public int getPotionRecoveryRate(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getPotionRecoveryRate();
         }
       }
     }
     return result;
   }

   public int getTotalER(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getTotalER();
         }
       }
     }

     return result;
   }

   public int getPotionRecoveryRateCancel(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getPotionRecoveryRateCancel();
         }
       }
     }
     return result;
   }

   public int getMagicHit(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMagicHit();
         }
       }
     }

     return result;
   }

   public int getReductionCancel(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getReductionCancel();
         }
       }
     }
     return result;
   }

   public int getShortCritical(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getShortCritical();
         }
       }
     }
     return result;
   }

   public int getLongCritical(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getLongCritical();
         }
       }
     }
     return result;
   }

   public int getMagicCritical(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMagicCritical();
         }
       }
     }
     return result;
   }

   public int getTitanRate(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getTitanRate();
         }
       }
     }
     return result;
   }

   public int getFoeDmg(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getFoeDmg();
         }
       }
     }
     return result;
   }

   public int getMagicDodge(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMagicDodge();
         }
       }
     }
     return result;
   }

   public int getPvpDmg(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getPvpDmg();
         }
       }
     }
     return result;
   }

   public int getPvpReduction(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getPvpReduction();
         }
       }
     }
     return result;
   }

   public int getPVPDmgReducIgnore(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getPvpReducIgnore();
         }
       }
     }
     return result;
   }

   public int getPvpMdmgReduction(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getPvpMdmgReduction();
         }
       }
     }
     return result;
   }

   public int getPvpMdmgReducIgnore(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getPvpMdmgReducIgnore();
         }
       }
     }

     return result;
   }

   public int getDG(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getDG();
         }
       }
     }

     return result;
   }

   public int getHpPercent(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getHpPercent();
         }
       }
     }

     return result;
   }

   public int getMpPercent(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getMpPercent();
         }
       }
     }
     return result;
   }

   public int getImmuneIgnore(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getIIg();
         }
       }
     }
     return result;
   }

   public int getCCIncrease(int i) {
     int result = 0;
     List<Effect> effects = getEffects();

     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getCCIncrease();
         }
       }
     }
     return result;
   }

   public int getEinhasadEfficiency(int i) {
     int result = 0;
     List<Effect> effects = getEffects();
     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getEinhasadEfficiency();
         }
       }
     }
     return result;
   }
   public int getAbnormalStatusPvpDamageReduction(int i) {
     int result = 0;
     List<Effect> effects = getEffects();
     if (effects != null) {
       for (Effect effect : effects) {
         if (effect.getEnchantLevel() == i) {
           result += effect.getAbnormalStatusPvpDamageReduction();
         }
       }
     }
     return result;
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Effect
   {
     @XmlAttribute(name = "Ac")
     private int _ac = 0;

     @XmlAttribute(name = "addAc")
     private int _addac = 0;

     @XmlAttribute(name = "Str")
     private int _str = 0;

     @XmlAttribute(name = "Dex")
     private int _dex = 0;

     @XmlAttribute(name = "Con")
     private int _con = 0;

     @XmlAttribute(name = "Int")
     private int _int = 0;

     @XmlAttribute(name = "Wis")
     private int _wis = 0;

     @XmlAttribute(name = "Cha")
     private int _cha = 0;

     @XmlAttribute(name = "addHp")
     private int _addhp = 0;

     @XmlAttribute(name = "Hp")
     private int _hp = 0;

     @XmlAttribute(name = "Mp")
     private int _mp = 0;

     @XmlAttribute(name = "Hpr")
     private int _hpr = 0;

     @XmlAttribute(name = "Mpr")
     private int _mpr = 0;

     @XmlAttribute(name = "Mr")
     private int _mr;

     @XmlAttribute(name = "Sp")
     private int _sp;

     @XmlAttribute(name = "Hit")
     private int _hit = 0;

     @XmlAttribute(name = "Dmg")
     private int _dmg = 0;

     @XmlAttribute(name = "BowHit")
     private int _bowHit = 0;

     @XmlAttribute(name = "BowDmg")
     private int _bowDmg = 0;

     @XmlAttribute(name = "WeightReduction")
     private int _wr = 0;

     @XmlAttribute(name = "DamageReduction")
     private int _dr = 0;

     @XmlAttribute(name = "Earth")
     private int _earth = 0;

     @XmlAttribute(name = "Water")
     private int _water = 0;

     @XmlAttribute(name = "Fire")
     private int _fire = 0;

     @XmlAttribute(name = "Wind")
     private int _wind = 0;

     @XmlAttribute(name = "All")
     private int _all = 0;

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
     private int _exp = 0;

     @XmlAttribute(name = "TotalER")
     private int _TotalER = 0;

     @XmlAttribute(name = "PotRate")
     private int _potRate = 0;

     @XmlAttribute(name = "PotRateCancel")
     private int _potRateCancel = 0;

     @XmlAttribute(name = "MagicHit")
     private int _magicHit = 0;

     @XmlAttribute(name = "MagicDmg")
     private int _magicDmg = 0;

     @XmlAttribute(name = "ReductionCancel")
     private int _reductionCancel = 0;

     @XmlAttribute(name = "ShortCritical")
     private int _shortCritical = 0;

     @XmlAttribute(name = "LongCritical")
     private int _longCritical = 0;

     @XmlAttribute(name = "MagicCritical")
     private int _magicCritical = 0;

     @XmlAttribute(name = "TitanRate")
     private int _titanRate = 0;

     @XmlAttribute(name = "FoeDmg")
     private int _foeDmg = 0;

     @XmlAttribute(name = "MagicDodge")
     private int _magicDodge = 0;

     @XmlAttribute(name = "PvpDmg")
     private int _pvpDmg = 0;

     @XmlAttribute(name = "PvpReduction")
     private int _pvpReduction = 0;

     @XmlAttribute(name = "Enchant")
     private int _enchantLevel = 0;

     @XmlAttribute(name = "PvpReducIgnore")
     private int _pvpReducignore = 0;

     @XmlAttribute(name = "PVPMdmgReduction")
     private int _pvpMdmgReduction = 0;

     @XmlAttribute(name = "PVPMdmgReducIgnore")
     private int _pvpMdmgReducIgnore = 0;

     @XmlAttribute(name = "DG")
     private int _DG = 0;

     @XmlAttribute(name = "HpPercent")
     private int _HpPercent = 0;

     @XmlAttribute(name = "MpPercent")
     private int _MpPercent = 0;

     @XmlAttribute(name = "ImmuneIgnore")
     private int _Immune_Ignore = 0;

     @XmlAttribute(name = "CCIncrease")
     private int _CC_Increase = 0;

     @XmlAttribute(name = "EinhasadEfficiency")
     private int _Einhasad_Efficiency = 0;

     @XmlAttribute(name = "StunPvPDmgReduction")
     private int _abnormal_status_pvp_damage_reduction = 0;


     public int getaddAc() {
       return this._addac;
     }

     public int getAc() {
       return this._ac;
     }

     public int getStr() {
       return this._str;
     }

     public int getDex() {
       return this._dex;
     }

     public int getCon() {
       return this._con;
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

     public int getaddHp() {
       return this._addhp;
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

     public int getMr() {
       return this._mr;
     }

     public int getSp() {
       return this._sp;
     }

     public int getHitModifier() {
       return this._hit;
     }

     public int getDmgModifier() {
       return this._dmg;
     }

     public int getBowHitModifier() {
       return this._bowHit;
     }

     public int getBowDmgModifier() {
       return this._bowDmg;
     }

     public int getWeightReduction() {
       return this._wr;
     }

     public int getDamageReduction() {
       return this._dr;
     }

     public int getDefenseEarth() {
       return this._earth;
     }

     public int getDefenseWater() {
       return this._water;
     }

     public int getDefenseFire() {
       return this._fire;
     }

     public int getDefenseWind() {
       return this._wind;
     }

     public int getDefenseAll() {
       return this._all;
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
       return this._exp;
     }

     public int getTotalER() {
       return this._TotalER;
     }

     public int getPotionRecoveryRate() {
       return this._potRate;
     }

     public int getPotionRecoveryRateCancel() {
       return this._potRateCancel;
     }

     public int getMagicHit() {
       return this._magicHit;
     }

     public int getMagicDmg() {
       return this._magicDmg;
     }

     public int getReductionCancel() {
       return this._reductionCancel;
     }

     public int getShortCritical() {
       return this._shortCritical;
     }

     public int getLongCritical() {
       return this._longCritical;
     }

     public int getMagicCritical() {
       return this._magicCritical;
     }

     public int getTitanRate() {
       return this._titanRate;
     }

     public int getFoeDmg() {
       return this._foeDmg;
     }

     public int getMagicDodge() {
       return this._magicDodge;
     }

     public int getPvpDmg() {
       return this._pvpDmg;
     }

     public int getPvpReduction() {
       return this._pvpReduction;
     }

     public int getPvpReducIgnore() {
       return this._pvpReducignore;
     }

     public int getPvpMdmgReduction() {
       return this._pvpMdmgReduction;
     }

     public int getPvpMdmgReducIgnore() {
       return this._pvpMdmgReducIgnore;
     }

     public int getDG() {
       return this._DG;
     }

     public int getHpPercent() {
       return this._HpPercent;
     }

     public int getMpPercent() {
       return this._MpPercent;
     }

     public int getIIg() {
       return this._Immune_Ignore;
     }

     public int getCCIncrease() {
       return this._CC_Increase;
     }

     public int getEnchantLevel() {
       return this._enchantLevel;
     }

     public int getEinhasadEfficiency() {
       return this._Einhasad_Efficiency;
     }

     public int getAbnormalStatusPvpDamageReduction() {
       return this._abnormal_status_pvp_damage_reduction;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1EnchantBonus> {
     @XmlElement(name = "Item")
     private List<L1EnchantBonus> _list;

     public Iterator<L1EnchantBonus> iterator() {
       return this._list.iterator();
     }
   }
 }



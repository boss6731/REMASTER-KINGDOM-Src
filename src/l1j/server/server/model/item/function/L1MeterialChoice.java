 package l1j.server.server.model.item.function;

 import java.io.File;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Random;
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
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;


 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1MeterialChoice
 {
   private static Logger _log = Logger.getLogger(L1MeterialChoice.class.getName());

   private static Random _random = new Random(System.nanoTime());
   private static final String _path = "./data/xml/Item/MaterialChoice.xml";
   private static HashMap<Integer, L1MeterialChoice> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlAttribute(name = "NeedRemoveSafeOnFail")
   private int _needRemoveSafeOnFail;

   @XmlAttribute(name = "Remove")
   private int _remove;

   @XmlElement(name = "Effect")
   private CopyOnWriteArrayList<Effect> _effects;

   public static L1MeterialChoice get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }

   private int getItemId() {
     return this._itemId;
   }

   private int getNeedRemoveSafeOnFail() {
     return this._needRemoveSafeOnFail;
   }

   private int getRemove() {
     return this._remove;
   }

   private List<Effect> getEffects() {
     return this._effects;
   }

     // 初始化方法，檢查物品和效果的合法性
     private boolean init() {
        // 檢查物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
            // 如果物品 ID 不存在，輸出錯誤信息並返回 false
             System.out.println("不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }

            // 遍歷所有效果並檢查效果的物品 ID 是否存在於 ItemTable 中
         for (Effect each : getEffects()) {
             if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
                // 如果效果的物品 ID 不存在，輸出錯誤信息並返回 false
                 System.out.println("不存在的物品編號: " + getItemId());
                 return false; // 返回 false 表示初始化失敗
             }
         }

            // 如果所有檢查都通過，返回 true 表示初始化成功
         return true;
     }

   private static void loadXml(HashMap<Integer, L1MeterialChoice> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/MaterialChoice.xml");
       ItemEffectList list = (ItemEffectList)um.unmarshal(file);

       for (L1MeterialChoice each : list)
       { if (each.init())
           dataMap.put(Integer.valueOf(each.getItemId()), each);  }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/MaterialChoice.xml 載入失敗.", e);
     }
   }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1MeterialChoice> dataMap = new HashMap<>();
     loadXml(dataMap);
     _dataMap = dataMap;
   }


     // 使用物品的方法
     public boolean use(L1PcInstance pc, L1ItemInstance item, L1ItemInstance target) {
         Effect effect = null; // 初始化效果變數為空
         // 遍歷所有效果
         for (Effect each : getEffects()) {
             // 檢查每個效果所需的附魔等級和物品 ID 是否匹配目標物品
             if (each.getNeedEnchant() == target.getEnchantLevel() && each.getNeedItemId() == target.getItemId()) {
                 effect = each; // 如果匹配，將效果賦值給變數
                 break; // 跳出循環
             }
         }
         // 如果沒有找到匹配的效果
         if (effect == null) {
             pc.sendPackets("沒有可交換的物品。"); // 發送提示信息
             return false; // 返回 false，表示操作失敗
         }

         boolean change_succes = true; // 初始化操作成功標誌為 true

         // 檢查效果的機率並進行隨機判斷
         if (effect.getChance() != 0 && _random.nextInt(100) > effect.getChance()) {
             change_succes = false; // 如果隨機值大於效果的機率，設置成功標誌為 false
         }


     if (change_succes) {
       L1ItemInstance give_item = ItemTable.getInstance().createItem(effect.getItemId());
       give_item.setBless(target.getBless());
       give_item.set_bless_level(target.get_bless_level());
       give_item.setAttrEnchantLevel(target.getAttrEnchantLevel());
       give_item.set_Doll_Bonus_Value(target.get_Doll_Bonus_Value());
       give_item.set_Doll_Bonus_Level(target.get_Doll_Bonus_Level());

       give_item.setBlessType(target.getBlessType());
       give_item.setBlessTypeValue(target.getBlessTypeValue());

       give_item.setEnchantLevel(effect.getEnchantLevel());
       give_item.setIdentified((effect.getIden() == 1));

       pc.getInventory().storeItem(give_item);

       if (effect.getSuccess() != null) {
         if (isStringDouble(effect.getSuccess())) {
           pc.sendPackets("$" + effect.getSuccess());
         } else {
           pc.sendPackets(effect.getSuccess());
         }
       }

       pc.getInventory().removeItem(target);
     }

     if (!change_succes) {
       if (effect.getKeepStatus() == 0) {
         pc.getInventory().removeItem(target);
       }

       if (effect.getFailure() != null) {
         if (isStringDouble(effect.getFailure())) {
           pc.sendPackets("$" + effect.getFailure());
         } else {
           pc.sendPackets(effect.getFailure());
         }
       }
     }

     if (getRemove() != 0) {
       if (getNeedRemoveSafeOnFail() == 1) {
         pc.getInventory().removeItem(item, getRemove());
       }
       else if (change_succes) {
         pc.getInventory().removeItem(item, getRemove());
       }
     }


     return true;
   }


   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Effect
   {
     @XmlAttribute(name = "ItemId")
     private int _itemId;

     @XmlAttribute(name = "Enchant")
     private int _enchantLevel;

     @XmlAttribute(name = "NeedItemId")
     private int _needItemId;

     @XmlAttribute(name = "Chance")
     private int _chance;

     @XmlAttribute(name = "KeepStatus")
     private int _keepStatus;

     @XmlAttribute(name = "NeedEnchant")
     private int _needEnchant;

     @XmlAttribute(name = "Success")
     private String _success;

     @XmlAttribute(name = "Iden")
     private int _iden;
     @XmlAttribute(name = "Failure")
     private String _failure;

     private int getItemId() {
       return this._itemId;
     }

     private int getEnchantLevel() {
       return this._enchantLevel;
     }

     private int getNeedItemId() {
       return this._needItemId;
     }

     private int getChance() {
       return this._chance;
     }

     private int getKeepStatus() {
       return this._keepStatus;
     }

     private int getNeedEnchant() {
       return this._needEnchant;
     }

     public String getSuccess() {
       return this._success;
     }

     private int getIden() {
       return this._iden;
     }

     public String getFailure() {
       return this._failure;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1MeterialChoice> {
     @XmlElement(name = "Item")
     private List<L1MeterialChoice> _list;

     public Iterator<L1MeterialChoice> iterator() {
       return this._list.iterator();
     }
   }

   private boolean isStringDouble(String s) {
     try {
       Double.parseDouble(s);
       return true;
     } catch (NumberFormatException e) {
       return false;
     }
   }
 }



 package l1j.server.server.model.item.function;

 import java.io.File;
 import java.sql.Timestamp;
 import java.util.ArrayList;
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
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1BoxItem
 {
   private static Logger _log = Logger.getLogger(L1BoxItem.class.getName());
   private static final String _path = "./data/xml/Item/BoxItem.xml";
   private static HashMap<Integer, L1BoxItem> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlAttribute(name = "Remove")
   private int _remove;

   @XmlElement(name = "Effect")
   private CopyOnWriteArrayList<Effect> _effects;

   public static L1BoxItem get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }

   private int getItemId() {
     return this._itemId;
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
           System.out.println("BoxItem : 不存在的物品編號: " + getItemId());
           return false;
       }


       // 遍歷所有效果，檢查每個效果的物品 ID 是否存在

       for (Effect each : getEffects()) {
           if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {

               // 如果效果的物品 ID 不存在，輸出錯誤信息並返回 false
               System.out.println("BoxItem : 不存在的物品編號: " + each.getItemId());
               return false;
           }
       }

       // 如果所有物品 ID 都存在於 ItemTable 中，返回 true
       return true;
   }

   private static void loadXml(HashMap<Integer, L1BoxItem> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/BoxItem.xml");
       ItemEffectList list = (ItemEffectList)um.unmarshal(file);
       for (L1BoxItem each : list) {
         if (each.init()) {
           dataMap.put(Integer.valueOf(each.getItemId()), each);
         }
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/BoxItem.xml 載入失敗.", e);
     }
   }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1BoxItem> dataMap = new HashMap<>();
     loadXml(dataMap);
     _dataMap = dataMap;
   }

   public boolean use(L1PcInstance pc, L1ItemInstance item) {
     int maxChargeCount = item.getItem().getMaxChargeCount();
     int chargeCount = item.getChargeCount();
     if (maxChargeCount > 0 && chargeCount <= 0) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
       return false;
     }

     List<Effect> effect = new ArrayList<>();
     for (Effect each : getEffects()) {
       if (pc.getClassFeature().getClassInitial().equalsIgnoreCase(each.getClassInitial()) || each.getClassInitial().equalsIgnoreCase("A")) {
         effect.add(each);
       }
     }

     if (effect.size() > 0) {
       for (Effect ec : effect) {
         L1PcInventory targetInven = pc.getInventory();
         L1ItemInstance new_item = ItemTable.getInstance().createItem(ec.getItemId());
         new_item.setEnchantLevel(ec.getEnchant());
         new_item.setCount(ec.getCount());
         new_item.setBless(ec.getBless());
         new_item.setAttrEnchantLevel(ec.getAttr());
         new_item.setSupportItem((ec.getSupportItem() == 1));
         new_item.set_Carving(ec.get_Carving());
         new_item.set_Cantunseal(ec.get_Cantunseal());

         if (ec.getUseTime() != 0L) {
           SetDeleteTime(new_item, ec.getUseTime());
         }
         new_item.setIdentified(true);
         targetInven.storeItem(new_item, ec.getBless());

         if (ec.getBless() >= 128) {
           int st = 0;
           if (new_item.isIdentified())
             st++;
           if (!new_item.getItem().isTradable())
             st += 2;
           if (!new_item.getItem().isCantDelete())
             st += 4;
           if (new_item.getItem().get_safeenchant() < 0)
             st += 8;
           if (new_item.getBless() >= 128) {
             st = 32;
             if (new_item.isIdentified()) {
               st += 15;
             } else {
               st += 14;
             }
           }

           pc.sendPackets((ServerBasePacket)new S_PacketBox(149, new_item, st));
           pc.getInventory().updateItem(new_item, 2);
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, new_item.getLogName()));
       }

            // 如果需要移除的數量大於 0
         if (getRemove() > 0) {
            // 如果物品有剩餘的充能次數
             if (chargeCount > 0) {
                // 減少物品的充能次數
                 item.setChargeCount(chargeCount - getRemove());
                // 更新物品的狀態
                 pc.getInventory().updateItem(item, 128);
             } else {
                // 否則從玩家的物品欄中移除指定數量的物品
                 pc.getInventory().removeItem(item, getRemove());
             }
         }
     } else {
            // 如果沒有需要移除的物品，發送系統消息通知玩家
         pc.sendPackets("沒有符合該類別的物品。");
     }
       return true; // 返回 true 表示操作成功
   }


   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Effect
   {
     @XmlAttribute(name = "ItemId")
     private int _itemId;

     @XmlAttribute(name = "Enchant")
     private int _enchant;

     @XmlAttribute(name = "Count")
     private int _count;

     @XmlAttribute(name = "Attr")
     private int _attr;

     @XmlAttribute(name = "Bless")
     private int _bless;

     @XmlAttribute(name = "ClassInitial")
     private String _classInitial;

     @XmlAttribute(name = "UseTime")
     private long _usetime;

     @XmlAttribute(name = "SupportItem")
     private int _supportItem;

     @XmlAttribute(name = "Carving")
     private int _Carving;
     @XmlAttribute(name = "Cantunseal")
     private int _Cantunseal;

     public int getItemId() {
       return this._itemId;
     }

     public int getEnchant() {
       return this._enchant;
     }

     public int getCount() {
       return this._count;
     }

     public int getBless() {
       return this._bless;
     }

     public int getAttr() {
       return this._attr;
     }

     private String getClassInitial() {
       return this._classInitial;
     }

     public long getUseTime() {
       return this._usetime;
     }

     public int getSupportItem() {
       return this._supportItem;
     }

     public int get_Carving() {
       return this._Carving;
     }

     public int get_Cantunseal() {
       return this._Cantunseal;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1BoxItem> {
     @XmlElement(name = "Item")
     private List<L1BoxItem> _list;

     public Iterator<L1BoxItem> iterator() {
       return this._list.iterator();
     }
   }

   private void SetDeleteTime(L1ItemInstance item, long minute) {
     Timestamp deleteTime = null;
     deleteTime = new Timestamp(System.currentTimeMillis() + 60000L * minute);





     item.setEndTime(deleteTime);
   }
 }



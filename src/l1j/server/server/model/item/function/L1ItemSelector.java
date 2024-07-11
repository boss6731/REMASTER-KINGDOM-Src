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
 public class L1ItemSelector
 {
   private static Logger _log = Logger.getLogger(L1ItemSelector.class.getName());
   private static final String _path = "./data/xml/Item/ItemSelector.xml";
   private static HashMap<Integer, L1ItemSelector> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlAttribute(name = "Remove")
   private int _remove;

   @XmlElement(name = "Effect")
   private CopyOnWriteArrayList<Effect> _effects;

   public static L1ItemSelector get(int id) {
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
             System.out.println("BoxItem：不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }

        // 遍歷所有效果，檢查效果中每個物品 ID 是否存在
         for (Effect each : getEffects()) {
             if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
        // 如果效果中的物品 ID 不存在，輸出錯誤信息並返回 false
                 System.out.println("BoxItem：不存在的物品編號: " + each.getItemId());
                 return false; // 返回 false 表示初始化失敗
             }
         }

        // 如果所有檢查都通過，返回 true 表示初始化成功
         return true;
     }

   private static void loadXml(HashMap<Integer, L1ItemSelector> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/ItemSelector.xml");
       ItemEffectList list = (ItemEffectList)um.unmarshal(file);
       for (L1ItemSelector each : list) {
         if (each.init()) {
           dataMap.put(Integer.valueOf(each.getItemId()), each);
         }
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/ItemSelector.xml 載入失敗.", e);
     }
   }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1ItemSelector> dataMap = new HashMap<>();
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

         if (ec.getUseTime() != 0) {
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

            // 向玩家發送 S_PacketBox 包，更新新物品
             pc.sendPackets((ServerBasePacket)new S_PacketBox(149, new_item, st));
             pc.getInventory().updateItem(new_item, 2);
         }
                // 向玩家發送 S_ServerMessage 消息，通知新物品日誌名稱
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, new_item.getLogName()));
       }

            // 檢查是否需要移除物品
         if (getRemove() > 0) {
                // 如果物品有剩餘可用次數
             if (chargeCount > 0) {
                    // 減少物品的剩餘次數
                 item.setChargeCount(chargeCount - getRemove());
                    // 更新物品狀態
                 pc.getInventory().updateItem(item, 128);
             } else {
                    // 如果物品無剩餘次數，直接移除物品
                 pc.getInventory().removeItem(item, getRemove());
             }
         }
     } else {
            // 如果玩家沒有對應的物品，發送無物品消息
         pc.sendPackets("沒有對應的物品可用於此角色。");
     }
       return true;
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
     private int _usetime;

     @XmlAttribute(name = "SupportItem")
     private int _supportItem;
     @XmlAttribute(name = "Carving")
     private int _Carving;

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

     public int getUseTime() {
       return this._usetime;
     }

     public int getSupportItem() {
       return this._supportItem;
     }

     public int get_Carving() {
       return this._Carving;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1ItemSelector> {
     @XmlElement(name = "Item")
     private List<L1ItemSelector> _list;

     public Iterator<L1ItemSelector> iterator() {
       return this._list.iterator();
     }
   }

   private void SetDeleteTime(L1ItemInstance item, int minute) {
     Timestamp deleteTime = null;
     deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
     item.setEndTime(deleteTime);
   }
 }



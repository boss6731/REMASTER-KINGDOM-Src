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
 import l1j.server.Config;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;


 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1Material
 {
   private static Logger _log = Logger.getLogger(L1Material.class.getName());

   private static final String _path = "./data/xml/Item/Material.xml";
   private static HashMap<Integer, L1Material> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlAttribute(name = "Remove")
   private int _remove;

   @XmlElement(name = "Effect")
   private Effect _effect;

   public static L1Material get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }

   private int getItemId() {
     return this._itemId;
   }

   private int getRemove() {
     return this._remove;
   }

   private Effect getEffect() {
     return this._effect;
   }

     // 初始化方法，檢查物品和效果的合法性
     private boolean init() {
        // 檢查物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
            // 如果物品 ID 不存在，輸出錯誤信息並返回 false
             System.out.println("不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }

            // 獲取效果
         Effect effect = getEffect();

                // 檢查效果的物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(effect.getItemId()) == null) {
                // 如果效果的物品 ID 不存在，輸出錯誤信息並返回 false
             System.out.println("不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }

                // 檢查效果所需的每個物品 ID 是否存在於 ItemTable 中
         for (String Itemid : effect.getNeedItemId().split(",")) {
             if (ItemTable.getInstance().getTemplate(Integer.valueOf(Itemid).intValue()) == null) {
                // 如果某個物品 ID 不存在，輸出錯誤信息並返回 false
                 System.out.println("不存在的物品編號: " + Itemid);
                 return false; // 返回 false 表示初始化失敗
             }
         }

            // 如果所有檢查都通過，返回 true 表示初始化成功
         return true;
     }

   private static void loadXml(HashMap<Integer, L1Material> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/Material.xml");
       ItemEffectList list = (ItemEffectList)um.unmarshal(file);

       for (L1Material each : list)
       { if (each.init())
           dataMap.put(Integer.valueOf(each.getItemId()), each);  }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/Material.xml 載入失敗.", e);
     }
   }

     // 加載數據的方法
     public static void load() {
         // 調用 loadXml 方法並傳入 _dataMap
         loadXml(_dataMap);
     }

     // 重新加載數據的方法
     public static void reload() {
         // 創建一個新的 HashMap 來存儲數據
         HashMap<Integer, L1Material> dataMap = new HashMap<>();
         // 調用 loadXml 方法並傳入新的 dataMap
         loadXml(dataMap);
         // 將 _dataMap 設置為新的 dataMap
         _dataMap = dataMap;
     }

     // 使用物品的方法
     public boolean use(L1PcInstance pc, L1ItemInstance item) {
         // 檢查是否在待機服務器狀態
         if (Config.Login.StandbyServer) {
             // 如果在待機狀態，發送系統消息並返回 false
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("開放待機中不可執行此操作。"));
             return false;
         }
         // 其他邏輯省略
         return true;
     }

     Effect effect = getEffect();

     String[] needItem = effect.getNeedItemId().split(",");
     String[] needCount = effect.getNeedCount().split(",");
     int need_size = needItem.length;
     boolean need_item_check = true; // 初始化檢查標誌為真
if (need_size != 0) { // 如果需要的物品數量不為零
     for (int j = 0; j < need_size; j++) { // 遍歷所有需要的物品
         // 檢查玩家的背包中是否有足夠數量的指定物品
         if (!pc.getInventory().checkItem(Integer.valueOf(needItem[j]).intValue(), Integer.valueOf(needCount[j]).intValue())) {
             need_item_check = false; // 如果缺少物品，設置檢查標誌為假
             break; // 跳出循環
         }
     }
 }
if (!need_item_check) { // 如果檢查標誌為假，表示有缺少的物品
     for (int j = 0; j < need_size; j++) { // 再次遍歷所有需要的物品
         // 檢查玩家的背包中是否有足夠數量的指定物品
         if (!pc.getInventory().checkItem(Integer.valueOf(needItem[j]).intValue(), Integer.valueOf(needCount[j]).intValue())) {
             L1Item tem = ItemTable.getInstance().getTemplate(Integer.valueOf(needItem[j]).intValue());
             // 向玩家發送缺少物品的消息
             pc.sendPackets("\aG使用所需的材料不足。 - [" + tem.getName() + " (" + Integer.valueOf(needCount[j]) + ")個]");
         }
     }
     return false; // 返回 false，表示操作失敗
 }

// 消耗玩家背包中的指定數量的物品
for (int i = 0; i < need_size; i++) {

     L1ItemInstance give_item = pc.getInventory().storeItem(effect.getItemId(), effect.getItemCount());
     pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, give_item.getLogName()));
     give_item.setIdentified(true);
     pc.getInventory().updateItem(give_item, 2);

     if (getRemove() > 0) {
       pc.getInventory().removeItem(item, getRemove());
     }

     return true;
   }


   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Effect
   {
     @XmlAttribute(name = "ItemId")
     private int _itemId;

     @XmlAttribute(name = "ItemCount")
     private int _itemCount;

     @XmlAttribute(name = "NeedItemId")
     private String _needItemId;
     @XmlAttribute(name = "NeedCount")
     private String _needCount;

     public int getItemId() {
       return this._itemId;
     }

     public int getItemCount() {
       return this._itemCount;
     }

     public String getNeedItemId() {
       return this._needItemId;
     }

     public String getNeedCount() {
       return this._needCount;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1Material> {
     @XmlElement(name = "Item")
     private List<L1Material> _list;

     public Iterator<L1Material> iterator() {
       return this._list.iterator();
     }
   }
 }



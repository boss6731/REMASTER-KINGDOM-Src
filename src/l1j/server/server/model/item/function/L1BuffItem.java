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
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;

 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1BuffItem
 {
   private static Logger _log = Logger.getLogger(L1BuffItem.class.getName());

   private static final String _path = "./data/xml/Item/BuffItem.xml";
   private static HashMap<Integer, L1BuffItem> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlAttribute(name = "Remove")
   private int _remove;

   @XmlAttribute(name = "NeedItemId")
   private int _needitemId;

   @XmlAttribute(name = "NeedCount")
   private int _needCount;

   @XmlElement(name = "Effect")
   private Effect _effect;

   public static L1BuffItem get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }

   private int getItemId() {
     return this._itemId;
   }

   private int getRemove() {
     return this._remove;
   }

   private int getNeedItemId() {
     return this._needitemId;
   }

   private int getNeedCount() {
     return this._needCount;
   }

   private Effect getEffect() {
     return this._effect;
   }

         // 初始化方法，檢查物品和技能的合法性
     private boolean init() {
        // 檢查物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
        // 如果物品 ID 不存在，輸出錯誤信息並返回 false
             System.out.println("不存在的物品編號: " + getItemId());
             return false;
         }

            // 獲取效果對象
         Effect effect = getEffect();

            // 遍歷效果中的技能 ID 列表，檢查每個技能 ID 是否存在
         for (String skillId : effect.getSkillIds().split(",")) {
             if (SkillsTable.getInstance().getTemplate(Integer.valueOf(skillId).intValue()) == null) {
                    // 如果技能 ID 不存在，輸出錯誤信息並返回 false
                 System.out.println("不存在的技能編號: " + skillId);
                 return false;
             }
         }

        // 如果所有物品 ID 和技能 ID 都存在於對應的表中，返回 true
         return true;
     }

   private static void loadXml(HashMap<Integer, L1BuffItem> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/BuffItem.xml");
       ItemEffectList list = (ItemEffectList)um.unmarshal(file);

       for (L1BuffItem each : list)
       { if (each.init())
           dataMap.put(Integer.valueOf(each.getItemId()), each);  }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/BuffItem.xml 載入失敗.", e);
     }
   }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1BuffItem> dataMap = new HashMap<>();
     loadXml(dataMap);
     _dataMap = dataMap;
   }

     // 使用物品的方法
     public boolean use(L1PcInstance pc, L1ItemInstance item) {
        // 檢查伺服器是否處於待機模式
         if (Config.Login.StandbyServer) {
        // 發送系統消息通知玩家在伺服器待機期間無法進行此操作
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("伺服器待機中，無法進行此操作。"));
             return false; // 返回 false 表示操作失敗
         }

        // 檢查玩家是否處於停止狀態
         if (pc.isstop()) {
             return false; // 返回 false 表示操作失敗
         }

        // 檢查是否需要消耗特定物品
         if (getNeedItemId() != 0 &&
        // 嘗試從玩家的物品欄中消耗指定數量的物品
                 !pc.getInventory().consumeItem(getNeedItemId(), getNeedCount())) {
        // 如果物品不足，獲取所需物品的模板
             L1Item needItem = ItemTable.getInstance().getTemplate(getNeedItemId());
        // 發送系統消息通知玩家所需物品不足
             pc.sendPackets("\aG無法使用：缺少 " + needItem + "(" + getNeedCount() + ")");
        // 返回 false 表示操作失敗
             return false;
         }
     }


     Effect effect = getEffect();

     pc.setBuffnoch(1);
     for (String skillId : effect.getSkillIds().split(",")) {
       (new L1SkillUse()).handleCommands(pc, Integer.valueOf(skillId).intValue(), pc.getId(), pc.getX(), pc.getY(), null, 0, 4);
     }
     pc.setBuffnoch(0);

     if (getRemove() > 0) {
       pc.getInventory().removeItem(item, getRemove());
     }

     return true;
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Effect
   {
     @XmlAttribute(name = "SkillId")
     private String _skillId;

     public String getSkillIds() {
       return this._skillId;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1BuffItem> {
     @XmlElement(name = "Item")
     private List<L1BuffItem> _list;

     public Iterator<L1BuffItem> iterator() {
       return this._list.iterator();
     }
   }
 }



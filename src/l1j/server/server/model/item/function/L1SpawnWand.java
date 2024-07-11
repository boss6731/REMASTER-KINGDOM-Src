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
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.L1SpawnUtil;

 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1SpawnWand
 {
   private static Logger _log = Logger.getLogger(L1SpawnWand.class.getName());

   private static Random _random = new Random();
   private static final String _path = "./data/xml/Item/SpawnWand.xml";
   private static HashMap<Integer, L1SpawnWand> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlAttribute(name = "Type")
   private String _type;

   @XmlAttribute(name = "Remove")
   private int _remove;

   @XmlElement(name = "Effect")
   private CopyOnWriteArrayList<Effect> _effects;
   private int _totalChance;

   public static L1SpawnWand get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }

   private int getItemId() {
     return this._itemId;
   }

   private String getType() {
     return this._type;
   }

   private int getRemove() {
     return this._remove;
   }

   private List<Effect> getEffects() {
     return this._effects;
   }

   private int getChance() {
     return this._totalChance;
   }

     private boolean init() {
         // 檢查物品 ID 是否存在於 ItemTable 中
         if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
             System.out.println("不存在的物品編號: " + getItemId());
             return false; // 返回 false 表示初始化失敗
         }

         // 遍歷所有效果
         for (Effect each : getEffects()) {
             // 如果類型為 "RANDOM"，累加效果的機率
             if (getType() != null && getType().equalsIgnoreCase("RANDOM")) {
                 this._totalChance += each.getChance();
             }

             // 檢查 NPC ID 是否存在於 NpcTable 中
             if (NpcTable.getInstance().getTemplate(each.getNpcId()) == null) {
                 System.out.println("不存在的 NPC 編號: " + each.getNpcId());
                 return false; // 返回 false 表示初始化失敗
             }
         }

         // 檢查機率的總和是否為 100%
         if (getType() != null && getType().equalsIgnoreCase("RANDOM") && getChance() != 0 && getChance() != 100) {
             System.out.println("機率的總和不是 100%: " + getItemId() + " / " + getChance() + "%");
             return false; // 返回 false 表示初始化失敗
         }

         return true; // 返回 true 表示初始化成功
     }

   private static void loadXml(HashMap<Integer, L1SpawnWand> dataMap) {
     try {
       JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

       Unmarshaller um = context.createUnmarshaller();

       File file = new File("./data/xml/Item/SpawnWand.xml");
       ItemEffectList list = (ItemEffectList)um.unmarshal(file);

       for (L1SpawnWand each : list)
       { if (each.init())
           dataMap.put(Integer.valueOf(each.getItemId()), each);  }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "./data/xml/Item/SpawnWand.xml載入失敗.", e);
     }
   }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1SpawnWand> dataMap = new HashMap<>();
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

     Effect effect = null;
     if (getType() != null && getType().equalsIgnoreCase("RANDOM")) {
       int chance = 0;
       int r = _random.nextInt(getChance());
       Iterator<Effect> iterator = getEffects().iterator(); if (iterator.hasNext()) { Effect each = iterator.next();
         chance += each.getChance();
         if (r < chance) {
           effect = each;
         } else {

           if (getRemove() > 0) {
             if (chargeCount > 0) {
               item.setChargeCount(chargeCount - getRemove());
               pc.getInventory().updateItem(item, 128);
             } else {
               pc.getInventory().removeItem(item, getRemove());
             }
           }
// 檢查並發送失敗訊息
             if (pc.sendPackets("使用失敗")) {
                 return false;
             }

// 進行其他處理
         } else {
                 Effect effect2 = null;
                 // 遍歷效果清單
                 for (Iterator<Effect> chance = getEffects().iterator(); chance.hasNext(); ) {
                     effect2 = chance.next();
                     // 根據條件檢查效果是否適用於當前玩家位置和地圖
                     if ((effect2.getX1() > 0 && effect2.getX2() > 0 && pc.getX() < effect2.getX1() && pc.getX() > effect2.getX2()) ||
                             (effect2.getY1() > 0 && effect2.getY2() > 0 && pc.getY() < effect2.getY1() && pc.getY() > effect2.getY2()) ||
                             (effect2.getCurMapId() > 0 && pc.getMapId() != effect2.getCurMapId()) ||
                             (effect2.getClassInitial() != null && !effect2.getClassInitial().contains(pc.getClassFeature().getClassInitial()))) {
                         continue; // 如果不符合條件，跳過此效果
                     }
                     effect = effect2; // 找到合適的效果
                 }
             }

// 如果沒有找到合適的效果，發送失敗訊息
             if (effect == null) {
                 pc.sendPackets("無法在當前區域召喚");
                 return false;
             }

     int radius = (effect.getRadius() > 0) ? effect.getRadius() : -1;
     if (radius > 0) {
       for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)pc, radius)) {
         if (object instanceof L1NpcInstance) {
           L1NpcInstance npc = (L1NpcInstance)object;
           if (npc.getNpcTemplate().get_npcId() == effect.getNpcId()) {
             return false;
           }
         }
       }
     }

     L1SpawnUtil.spawn(pc, effect.getNpcId(), 0, effect.getTime() * 1000);

     if (getRemove() > 0) {
       if (chargeCount > 0) {
         item.setChargeCount(chargeCount - getRemove());
         pc.getInventory().updateItem(item, 128);
       } else {
         pc.getInventory().removeItem(item, getRemove());
       }
     }

     return true;
   }


   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Effect
   {
     @XmlAttribute(name = "NpcId")
     private int _npcId;

     @XmlAttribute(name = "Time")
     private int _time;

     @XmlAttribute(name = "Chance")
     private int _chance;

     @XmlAttribute(name = "Radius")
     private int _radius;

     @XmlAttribute(name = "X1")
     private int _x1;

     @XmlAttribute(name = "Y1")
     private int _y1;

     @XmlAttribute(name = "X2")
     private int _x2;

     @XmlAttribute(name = "Y2")
     private int _y2;

     @XmlAttribute(name = "CurMapId")
     private int _curMapId;
     @XmlAttribute(name = "ClassInitial")
     private String _classInitial;

     private int getNpcId() {
       return this._npcId;
     }

     private int getTime() {
       return this._time;
     }

     private int getChance() {
       return this._chance;
     }

     private int getRadius() {
       return this._radius;
     }

     private int getX1() {
       return this._x1;
     }

     private int getY1() {
       return this._y1;
     }

     private int getX2() {
       return this._x2;
     }

     private int getY2() {
       return this._y2;
     }

     private int getCurMapId() {
       return this._curMapId;
     }

     private String getClassInitial() {
       return this._classInitial;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1SpawnWand> {
     @XmlElement(name = "Item")
     private List<L1SpawnWand> _list;

     public Iterator<L1SpawnWand> iterator() {
       return this._list.iterator();
     }
   }
 }



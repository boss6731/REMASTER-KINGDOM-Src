 package l1j.server.server.model.item.function;

 import java.io.File;
 import java.text.DecimalFormat;
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
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.L1BuffUtil;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.random.RandomGenerator;
 import l1j.server.server.utils.random.RandomGeneratorFactory;




 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1BlessTypeEnchant
 {
   public static final int Ac = 1;
   public static final int Str = 2;
   public static final int Con = 3;
   public static final int Dex = 4;
   public static final int Int = 5;
   public static final int Wis = 6;
   public static final int Cha = 7;
   public static final int Hp = 8;
   public static final int Mp = 9;
   public static final int Hpr = 10;
   public static final int Mpr = 11;
   public static final int Mr = 12;
   public static final int Sp = 13;
   public static final int Dmg = 14;
   public static final int Hit = 15;
   public static final int BowDmg = 16;
   public static final int BowHit = 17;
   public static final int DmgReduction = 18;
   public static final int WeightReduction = 19;
   public static final int MagicHit = 20;
   public static final int TechniqueTolerance = 21;
   public static final int SpiritTolerance = 22;
   public static final int DragonlangTolerance = 23;
   public static final int FearTolerance = 24;
   public static final int AllTolerance = 25;
   public static final int TechniqueHit = 26;
   public static final int SpiritHit = 27;
   public static final int DragonlangHit = 28;
   public static final int FearHit = 29;
   public static final int AllHit = 30;
   public static final int TitanPercent = 31;
   public static final int AinEfficiency = 32;
   public static final int Exp = 33;
   public static final int Er = 34;
   public static final int Dg = 35;
   public static final int MagicDodge = 36;
   public static final int PvpDmg = 37;
   public static final int PvpReduction = 38;
   public static final int PvpReducIgnore = 39;
   public static final int PVPMdmgReduction = 40;
   public static final int PVPMdmgReducIgnore = 41;
   public static final int HpPercent = 42;
   public static final int MpPercent = 43;
   public static final int ImmuneIgnore = 44;
   public static final int addAc = 45;
   public static final int addHp = 46;
   public static final int PvpMagicDamage = 99;
   private static RandomGenerator _random = RandomGeneratorFactory.newRandom();

   private static Logger _log = Logger.getLogger(L1BlessTypeEnchant.class.getName());

   @XmlAttribute(name = "Remove")
   private int _remove;

   @XmlElement(name = "Effect")
   private Effect _effect;
   private static final String _path = "./data/xml/Item/BlessTypeEnchant.xml";
   private static HashMap<Integer, L1BlessTypeEnchant> _dataMap = new HashMap<>();

   @XmlAttribute(name = "ItemId")
   private int _itemId;

   @XmlAttribute(name = "CantUseItemId")
   private String _cantUseItemId;

   @XmlAttribute(name = "UseItemId")
   private String _UseItemId;

   @XmlAttribute(name = "TypeID")
   private String _TypeID;

   private int getRemove() {
     return this._remove;
   }

   private Effect getEffect() {
     return this._effect;
   }

   private String getCantUseItemId() {
     return this._cantUseItemId;
   }

   private String getUseItemId() {
     return this._UseItemId;
   }

   private String getTypeID() {
     return this._TypeID;
   }

   public static L1BlessTypeEnchant get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }

   private int getItemId() {
     return this._itemId;
   }

     // 從 XML 文件加載數據到 dataMap
     private static void loadXml(HashMap<Integer, L1BlessTypeEnchant> dataMap) {
         try {
             // 創建 JAXB 上下文，指定需要解析的類
             JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

             // 創建解組器 (Unmarshaller)
             Unmarshaller um = context.createUnmarshaller();

             // 加載 XML 文件
             File file = new File("./data/xml/Item/BlessTypeEnchant.xml");
             ItemEffectList list = (ItemEffectList)um.unmarshal(file); // 將 XML 文件解組成對象

             // 遍歷解組得到的列表
             for (L1BlessTypeEnchant each : list) {
                 // 檢查物品模板是否存在
                 if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
                     System.out.println("不存在的物品編號: " + each.getItemId());
                     continue; // 跳過這個物品
                 }
                 // 將物品添加到 dataMap 中
                 dataMap.put(Integer.valueOf(each.getItemId()), each);
             }
         } catch (Exception e) {
             // 記錄錯誤信息
             _log.log(Level.SEVERE, "./data/xml/Item/BlessTypeEnchant.xml 加載失敗。", e);
         }
     }

   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1BlessTypeEnchant> dataMap = new HashMap<>();
     loadXml(dataMap);
     _dataMap = dataMap;
   }

     // 使用物品的方法
     public boolean use(L1PcInstance pc, L1ItemInstance item, L1ItemInstance target) {
// 檢查目標物品的類型，如果類型為 0
         if (target.getItem().getType2() == 0) {
// 發送系統消息通知玩家，只能在武器、防具或飾品上使用
             pc.sendPackets("只能用於武器、防具或飾品。");
             return false; // 返回 false 表示使用失敗
         }

     if (getCantUseItemId() != null) {
       String[] items = getCantUseItemId().split(",");
       int size = items.length;
       int itemid = 0;
       for (int i = 0; i < size; i++) {
         itemid = Integer.valueOf(items[i]).intValue();
         if (itemid != 0)
         {

        // 如果目標物品的 ID 等於 itemid
             if (target.getItem().getItemId() == itemid) {
        // 發送系統消息通知玩家該物品無法使用
                 pc.sendPackets("無法在該物品上使用。");
                 return false; // 返回 false 表示使用失敗
             }
         }
       }
     }
         if (getUseItemId() != null) {
        // 取得可使用物品的 ID 列表
             String[] items = getUseItemId().split(",");
             int size = items.length;
             int itemid = 0;
             for (int i = 0; i < size; i++) {
                 itemid = Integer.valueOf(items[i]).intValue();
                 if (itemid != 0)
                 {

        // 如果目標物品的 ID 不等於當前檢查的 itemid
                     if (target.getItem().getItemId() != itemid) {
        // 發送系統消息通知玩家該物品無法使用
                         pc.sendPackets("無法在該物品上使用。");
                         return false; // 返回 false 表示使用失敗
                     }
                 }
             }
         }
     if (getTypeID() != null) {
       String[] items = getTypeID().split(",");
       int size = items.length;
       int Type = 0;
       for (int i = 0; i < size; i++) {
         Type = Integer.valueOf(items[i]).intValue();
         if (Type != 0)
         {


             // 檢查物品類型是否為 1 或 2
             if (Type == 1 || Type == 2) {

                 // 如果類型為 2
                 if (Type == 2) {

                     // 檢查目標物品的類型是否不為 2 或者類型在 8 到 12 之間
                     if (target.getItem().getType2() != Type || (target.getItem().getType() >= 8 && target.getItem().getType() <= 12)) {

                         // 發送系統消息通知玩家該卷軸無法使用在該物品上
                         pc.sendPackets("請按照該卷軸的類型使用。");
                         return false; // 返回 false 表示使用失敗
                     }
                 } else if (target.getItem().getType2() != Type) {

                     // 如果目標物品的類型不為 1
                     pc.sendPackets("請按照該卷軸的類型使用。");
                     return false; // 返回 false 表示使用失敗
                 }
             } else if (Type == 3 && (
                     // 如果類型為 3，檢查目標物品的條件
                     target.getItem().getType2() != 2 || target.getItem().getType() < 8 || target.getItem().getType() > 12)) {
                 // 發送系統消息通知玩家該卷軸無法使用在該物品上
                 pc.sendPackets("請按照該卷軸的類型使用。");
                 return false; // 返回 false 表示使用失敗
             }
         }
       }
     }

     L1BuffUtil.cancelBarrier(pc);

     int maxChargeCount = item.getItem().getMaxChargeCount();
     int chargeCount = item.getChargeCount();
     if (maxChargeCount > 0 && chargeCount <= 0) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
       return false;
     }

     Effect effect = getEffect();

     int rnd = _random.nextInt(1000000) + 1;

     if (rnd < effect.getChance()) {

       String[] types = effect.getType().split(",");
       String[] addValues = effect.getAddValue().split("~");
       int add_value = _random.range(Integer.valueOf(addValues[0]).intValue(), Integer.valueOf(addValues[1]).intValue());

       int real_type = Integer.valueOf(types[_random.range(0, Integer.valueOf(types.length - 1).intValue())]).intValue();
       if (target.isEquipped()) {
         pc.getEquipSlot().remove(target);
         pc.getEquipSlot().removeSetItemsbless(target.getItemId());
         target.setBlessType(real_type);
         target.setBlessTypeValue(add_value);
         target.setBless(0);
         pc.getEquipSlot().set(target);
       } else {
         target.setBlessType(real_type);
         target.setBlessTypeValue(add_value);
         target.setBless(0);
       }

       pc.getInventory().updateItem(target, 512);
       pc.getInventory().saveItem(target, 512);
       pc.getInventory().updateItem(target, 4);


       // 發送技能音效給玩家
         pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 9268));


         // 發送系統消息通知玩家目標物品成功吸收了祝福的氣息
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aH" + target.getLogName() + " 已經吸收了祝福的氣息 [+" + add_value + "]。"));
     } else {
         // 發送系統消息通知玩家祝福的氣息未能成功吸收
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("祝福的氣息未能成功吸收。"));
     }


     // 如果玩家是管理員（GM）
         if (pc.isGm()) {
             // 格式化小數點後兩位數
             DecimalFormat format = new DecimalFormat(".##");
             // 發送系統消息顯示成功率和隨機值
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("成功率: " + format.format(effect.getChance()) + " >= 隨機值: " + rnd));
         }

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
     @XmlAttribute(name = "AddValue")
     private String _addvalue;

     @XmlAttribute(name = "Type")
     private String _type;
     @XmlAttribute(name = "Chance")
     private int _chance;

     public String getAddValue() {
       return this._addvalue;
     }

     public String getType() {
       return this._type;
     }

     public int getChance() {
       return this._chance;
     }
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList
     implements Iterable<L1BlessTypeEnchant> {
     @XmlElement(name = "Item")
     private List<L1BlessTypeEnchant> _list;

     public Iterator<L1BlessTypeEnchant> iterator() {
       return this._list.iterator();
     }
   }
 }



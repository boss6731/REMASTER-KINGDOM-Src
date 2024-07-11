 package l1j.server.server.model.item;

 import java.io.File;
 import java.sql.Timestamp;
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
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
 import l1j.server.server.datatables.ItemMessageBoxTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1ItemMessageBox;
 import l1j.server.server.utils.CommonUtil;





 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1TreasureBox
 {
   private static Logger _log = Logger.getLogger(L1TreasureBox.class.getName());
   private static final String PATH = "./data/xml/Item/TreasureBox.xml";

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "TreasureBoxList")
   private static class TreasureBoxList implements Iterable<L1TreasureBox> { @XmlElement(name = "TreasureBox")
     private List<L1TreasureBox> _list;

     public Iterator<L1TreasureBox> iterator() {
       return this._list.iterator();
     } }



   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Item
   {
     @XmlAttribute(name = "ItemId")
     private int _itemId;

     @XmlAttribute(name = "Count")
     private int _count;

     @XmlAttribute(name = "Enchant")
     private int _enchant;

     @XmlAttribute(name = "Attr")
     private int _attr;
     @XmlAttribute(name = "Identi")
     private boolean _identified;
     @XmlAttribute(name = "Time")
     private int _time;
     @XmlAttribute(name = "Bless")
     private int _bless = 1;

     @XmlAttribute(name = "Carving")
     private int _Carving;

     private int _chance;


     @XmlAttribute(name = "Chance")
     private void setChance(double chance) {
       this._chance = (int)(chance * 10000.0D);
     }

     public int getItemId() {
       return this._itemId;
     }

     public int getCount() {
       return this._count;
     }


     public int getEnchant() {
       return this._enchant;
     }


     public int getAttr() {
       return this._attr;
     }


     public boolean getIdentified() {
       return this._identified;
     }

     public double getChance() {
       return this._chance;
     }

     public int getTime() {
       return this._time;
     }

     public int getBless() {
       return this._bless;
     }

     public int get_Carving() {
       return this._Carving;
     }
   }

   private enum TYPE {
     RANDOM, SPECIFIC, RANDOM_SPECIFIC;
   }



   private static final HashMap<Integer, L1TreasureBox> _dataMap = new HashMap<>();

   public static L1TreasureBox get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }
   @XmlAttribute(name = "ItemId")
   private int _boxId; @XmlAttribute(name = "Type")
   private TYPE _type;
   @XmlElement(name = "Item")
   private CopyOnWriteArrayList<Item> _items;
   private int _totalChance;

   private int getBoxId() {
     return this._boxId;
   }

   private TYPE getType() {
     return this._type;
   }




   private List<Item> getItems() {
     return this._items;
   }



   private int getTotalChance() {
     return this._totalChance;
   }

     private void init() {
         for (Item each : getItems()) {
             this._totalChance = (int)(this._totalChance + each.getChance());
             if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
                 getItems().remove(each);
                 _log.warning("未發現物品 ID " + each.getItemId() + " 的模板。");
                 System.out.println("未發現物品 ID " + each.getItemId() + " 的模板。");
             }
         }
         if (getType() == TYPE.RANDOM && getTotalChance() != 1000000) {
             _log.warning("ID " + getBoxId() + " 的總機率不等於100%。");
             System.out.println("ID " + getBoxId() + " 的總機率不等於100%。");
         }
     }


   public static boolean load() {
       try {
           JAXBContext context = JAXBContext.newInstance(TreasureBoxList.class);// 創建JAXB上下文

           Unmarshaller um = context.createUnmarshaller();// 創建解組器

           File file = new File("./data/xml/Item/TreasureBox.xml");// 指定XML文件路徑

           TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);// 從XML文件解組數據

           for (L1TreasureBox each : list.getTreasureBoxes()) {// 假設TreasureBoxList有個getTreasureBoxes方法返回L1TreasureBox列表
               each.init();// 初始化每個寶箱
               _dataMap.put(each.getBoxId(), each);// 將每個寶箱加入數據地圖
           }
           return true;
       } catch (Exception e) {
           _log.log(Level.SEVERE, "./data/xml/Item/TreasureBox.xml的加載失敗。", e);// 日誌記錄加載失敗
           return false;
       }}



     public boolean open(L1PcInstance pc) {
         L1ItemInstance item = null;
         Random random = null;

         if (pc._ErzabeBox == true && getBoxId() == 30102) {
             Random boxrandom = new Random();
             int[] itemrnd = { 3000090, 3000096, 41148 };
             int ran1 = boxrandom.nextInt(3);
             item = pc.getInventory().storeItem(itemrnd[ran1], 1);
             L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, "有人從艾爾扎貝的蛋中獲得了 " + item.getName() + "。"));
             pc._ErzabeBox = false;
         } else if (pc._SandwormBox == true && getBoxId() == 30103) {
             Random boxrandom = new Random();
             int[] itemrnd = { 3000092, 3000091, 3000095, 3000094, 210125, 5559 };
             int ran1 = boxrandom.nextInt(6);
             item = pc.getInventory().storeItem(itemrnd[ran1], 1);
             L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, "有人從沙蟲的沙袋中獲得了 " + item.getName() + "。"));
             pc._SandwormBox = false;
         } else if (getType().equals(TYPE.SPECIFIC)) {
             for (Item each : getItems()) {






         item = ItemTable.getInstance().createItem(each.getItemId());
         if (item != null && !isOpen(pc)) {
           item.setCount(each.getCount());
           item.setEnchantLevel(each.getEnchant());
           item.setAttrEnchantLevel(each.getAttr());
           item.setIdentified(each.getIdentified());
           item.setBless(each.getBless());
           item.set_Carving(each.get_Carving());

           if (each.getTime() > 0) {
             Timestamp deleteTime = null;
             deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
             item.setEndTime(deleteTime);
           }

           if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
             L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
             String men = "";
             if (temp != null) {
               String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
               String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
               for (int i = 0; i < bonusitem.length; i++) {
                 int bitem = Integer.parseInt(bonusitem[i]);
                 int benchant = Integer.parseInt(enchant[i]);

                 if (item.getItemId() == bitem)
                 {

                   if (item.getEnchantLevel() == benchant) {


                       if (temp.getMentoption().equalsIgnoreCase("某人")) {
                           men = "有人 ";
                       } else {
                           men = "" + pc.getName() + " 您 ";
                       }
                     if (temp.getMent() != null) {

                       String BoxName = temp.getItemName();
                       String itemName = item.getViewName();
                       String message = String.format(men + temp.getMent(), new Object[] { BoxName, itemName });
                       L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(message));
                       L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, message));
                     } else {

                       String BoxName = temp.getItemName();
                       String itemName = item.getViewName();
                       if (itemName == null)
                         itemName = item.getName();
                        // 獲取物品的消息字符串，使用 String.format 方法來格式化字符串
                       String message = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });

                        // 定義另一個消息字符串，與第一個消息字符串相同
                       String message2 = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });

                       L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(message), (ServerBasePacket)new S_PacketBox(84, message2) });
                     }
                   }  }
               }
             }
           }  storeItem(pc, item);
         }

       }
     } else if (getType().equals(TYPE.RANDOM)) {

       int chance = 0;

       int r = CommonUtil.random(getTotalChance());
       for (Item each : getItems()) {
         chance = (int)(chance + each.getChance());
         if (r < chance) {
           item = ItemTable.getInstance().createItem(each.getItemId());
           if (item != null && !isOpen(pc)) {
             item.setCount(each.getCount());
             item.setBless(each.getBless());
             item.setEnchantLevel(each.getEnchant());
             item.set_Carving(each.get_Carving());

             if (each.getTime() > 0) {
               Timestamp deleteTime = null;
               deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
               item.setEndTime(deleteTime);
             }
             storeItem(pc, item);

             if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
               L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
               String men = "";
               if (temp != null) {
                 String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
                 String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
                 for (int i = 0; i < bonusitem.length; i++) {
                   int bitem = Integer.parseInt(bonusitem[i]);
                   int benchant = Integer.parseInt(enchant[i]);

                   if (item.getItemId() == bitem)
                   {

                     if (item.getEnchantLevel() == benchant) {


                       // 如果 Mentoption 為 "누군가가"，則設置 men 為 "누군가가 "
                       if (temp.getMentoption().equalsIgnoreCase("某人")) {
                         men = "有人 "; // 設置為 "有人 "
                       } else {
                         // 否則，設置 men 為玩家名稱加上 " 님께서 "
                         men = "" + pc.getName() + " 您 "; // 設置為 "玩家名稱 您 "
                       }
                       if (temp.getMent() != null) {

                         String BoxName = temp.getItemName();
                         String itemName = item.getViewName();
                         String message = String.format(men + temp.getMent(), new Object[] { BoxName, itemName });
                         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(message));
                         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, message));
                       } else {

                         String BoxName = temp.getItemName();
                         String itemName = item.getViewName();
                         if (itemName == null)
                           itemName = item.getName();
                            // 使用 String.format 方法來格式化字符串，將 men、itemName 和 BoxName 插入到消息字符串中
                         String message = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });

                           // 定義另一個消息字符串，與第一個消息字符串相同，使用 String.format 方法來格式化字符串
                         String message2 = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });

                         L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(message), (ServerBasePacket)new S_PacketBox(84, message2) });
                       }
                     }  }
                 }
               }
             }
           }
           break;
         }
       }
     } else if (getType().equals(TYPE.RANDOM_SPECIFIC)) {
       random = new Random(System.nanoTime());
       int chance = 0;

       int r = random.nextInt(getTotalChance());

       for (Item each : getItems()) {
         if (each.getChance() == 0.0D) {
           item = ItemTable.getInstance().createItem(each.getItemId());
           if (item != null && !isOpen(pc)) {
             item.setCount(each.getCount());
             item.setBless(each.getBless());
             item.setEnchantLevel(each.getEnchant());
             item.set_Carving(each.get_Carving());

             if (each.getTime() > 0) {
               Timestamp deleteTime = null;
               deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
               item.setEndTime(deleteTime);
             }

             if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
               L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
               String men = "";
               if (temp != null) {
                 String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
                 String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
                 for (int i = 0; i < bonusitem.length; i++) {
                   int bitem = Integer.parseInt(bonusitem[i]);
                   int benchant = Integer.parseInt(enchant[i]);

                   if (item.getItemId() == bitem)
                   {

                     if (item.getEnchantLevel() == benchant) {


                       // 如果 Mentoption 為 "누군가가"，則設置 men 為 "누군가가 "
                       if (temp.getMentoption().equalsIgnoreCase("某人")) {
                         men = "有人 "; // 設置為 "有人 "
                       } else {
                         // 否則，設置 men 為玩家名稱加上 " 님께서 "
                         men = "" + pc.getName() + " 您 "; // 設置為 "玩家名稱 您 "
                       }
                       if (temp.getMent() != null) {

                         String BoxName = temp.getItemName();
                         String itemName = item.getViewName();
                         String message = String.format(men + temp.getMent(), new Object[] { BoxName, itemName });
                         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(message));
                         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, message));
                       } else {

                         String BoxName = temp.getItemName();
                         String itemName = item.getViewName();
                         if (itemName == null)
                           itemName = item.getName();
                         // 使用 String.format 方法來格式化字符串，將 men、itemName 和 BoxName 插入到消息字符串中
                         String message = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });

                         // 定義另一個消息字符串，與第一個消息字符串相同，使用 String.format 方法來格式化字符串
                         String message2 = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });


                         L1World.getInstance()
                           .broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(message), (ServerBasePacket)new S_PacketBox(84, message2) });
                       }
                     }
                   }
                 }
               }
             }
             storeItem(pc, item);
           }
           continue;
         }
         chance = (int)(chance + each.getChance());
         if (r < chance) {
           item = ItemTable.getInstance().createItem(each.getItemId());
           if (item != null && !isOpen(pc)) {
             item.setCount(each.getCount());
             item.setBless(each.getBless());
             item.setEnchantLevel(each.getEnchant());
             item.set_Carving(each.get_Carving());

             if (each.getTime() > 0) {
               Timestamp deleteTime = null;
               deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
               item.setEndTime(deleteTime);
             }
             storeItem(pc, item);

             if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
               L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
               String men = "";
               if (temp != null) {
                 String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
                 String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
                 for (int i = 0; i < bonusitem.length; i++) {
                   int bitem = Integer.parseInt(bonusitem[i]);
                   int benchant = Integer.parseInt(enchant[i]);

                   if (item.getItemId() == bitem)
                   {

                     if (item.getEnchantLevel() == benchant) {


                       // 如果 Mentoption 為 "누군가가"，則設置 men 為 "누군가가 "
                       if (temp.getMentoption().equalsIgnoreCase("某人")) {
                         men = "有人 "; // 設置為 "有人 "
                       } else {
                         // 否則，設置 men 為玩家名稱加上 " 님께서 "
                         men = "" + pc.getName() + " 您 "; // 設置為 "玩家名稱 您 "
                       }
                       if (temp.getMent() != null) {

                         String BoxName = temp.getItemName();
                         String itemName = item.getViewName();
                         String message = String.format(men + temp.getMent(), new Object[] { BoxName, itemName });
                         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(message));
                         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, message));
                       } else {

                         String BoxName = temp.getItemName();
                         String itemName = item.getViewName();
                         if (itemName == null)
                           itemName = item.getName();
                         // 使用 String.format 方法來格式化字符串，將 men、itemName 和 BoxName 插入到消息字符串中
                         String message = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });

                         // 定義另一個消息字符串，與第一個消息字符串相同，使用 String.format 方法來格式化字符串
                         String message2 = String.format("" + men + "%s 已在 " + BoxName + " 中獲得。", new Object[] { itemName });

                         L1World.getInstance()
                           .broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(message), (ServerBasePacket)new S_PacketBox(84, message2) });
                       }
                     }
                   }
                 }
               }
             }

                // 檢查條件，如果寶箱ID為 4100145 且 itemId 為指定的值之一
             if (getBoxId() == 4100145 && (
                     each.getItemId() == 505011 || each.getItemId() == 505015 || each.getItemId() == 505012 || each.getItemId() == 620 || each.getItemId() == 625 || each
                             .getItemId() == 626 || each.getItemId() == 623 || each.getItemId() == 618 || each.getItemId() == 619 || each.getItemId() == 616 || each
                             .getItemId() == 622 || each.getItemId() == 617)) {

               // 廣播消息給所有玩家，通知他們某個玩家獲得了物品
               L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, "某位阿登勇士獲得了真·死亡騎士遺物箱中的 \f3(" + item.getName() + ")\f2。"));
               L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage("某位阿登勇士獲得了真·死亡騎士遺物箱中的 (" + item.getName() + ")。"));
             }
           }


           break;
         }
       }
     }

     if (item == null) {
       return false;
     }
     int itemId = getBoxId();

     if (itemId == 40576 || itemId == 40577 || itemId == 40578 || itemId == 40411 || itemId == 49013);


     if (itemId == 3000045) {
       int[] enchantrnd = { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 6, 3, 3, 3, 1, 2, 3, 4, 4, 5, 1, 2, 3, 7 };

       int RandomEchant = random.nextInt(enchantrnd.length);
       item.setEnchantLevel(enchantrnd[RandomEchant]);
     }
     if (itemId >= 3000038 && itemId <= 3000044) {
       int[] enchantrnd = { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 5 };

       int RandomEchant = random.nextInt(enchantrnd.length);
       item.setEnchantLevel(enchantrnd[RandomEchant]);
     }

     if (L1FavorBookLoader.isFavorItem(item.getItemId())) {
       pc.getFavorBook().setCraftBoxOpenResult(item);
     }

     if (itemId >= 30001834 && itemId <= 30001842) {
       pc.getInventory().setCraftBoxOpenResult(item);
     }
     return true;
   }


   // 檢查玩家是否可以打開物品欄
   private boolean isOpen(L1PcInstance pc) {
     int totalCount = 0;
     totalCount = pc.getInventory().getSize(); // 獲取物品欄的物品數量

     // 如果物品欄重量超過82%或物品數量超過165件
     if (pc.getInventory().getWeight100() >= 82 || totalCount > 165) {
       // 發送系統消息通知玩家物品欄超重或超量，限制行動
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("檢查物品欄：重量/數量超過限制，行動被限制。"));
       return true; // 返回 true 表示無法打開物品欄
     }
     return false; // 返回 false 表示可以打開物品欄
   }


   // 儲存物品到玩家的物品欄中
   private static void storeItem(L1PcInstance pc, L1ItemInstance item) {
      // 檢查是否可以添加物品到玩家的物品欄中
     if (pc.getInventory().checkAddItem(item, item.getCount()) != 0) {
      // 如果無法添加，發送系統消息通知玩家
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("您持有的物品太多了。"));
       return; // 結束方法，不儲存物品
     }
     L1PcInventory l1PcInventory = pc.getInventory();


     if (item.getItem().isEndedTimeMessage()) {
       item.setOpenEffect(1);
     } else {
       item.setOpenEffect(32);
     }  item.setIdentified(true);

     l1PcInventory.storeItemTrea(item);
     pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getLogName()));
   }
 }



 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;




 public class L1CreateItem
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1CreateItem();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer st = new StringTokenizer(arg);

       String nameid = st.nextToken();
       int count = 1;
       if (st.hasMoreTokens()) {
         count = Integer.parseInt(st.nextToken());
       }
       int enchant = 0;
       if (st.hasMoreTokens()) {
         enchant = Integer.parseInt(st.nextToken());
       }
       int attrenchant = 0;
       if (st.hasMoreTokens()) {
         attrenchant = Integer.parseInt(st.nextToken());
       }

       int isId = 0;
       if (st.hasMoreTokens()) {
         isId = Integer.parseInt(st.nextToken());
       }

       int itemid = 0;
       try {
         itemid = Integer.parseInt(nameid);
       } catch (NumberFormatException e) {
         itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);

         if (itemid == 0) {
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("找不到該項目。"));
           return;
         }
       }
       L1Item temp = ItemTable.getInstance().getTemplate(itemid);
       if (temp != null) {
         if (temp.isStackable()) {
           L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
           item.setEnchantLevel(0);
           item.setCount(count);
           if (isId == 1) {
             item.setIdentified(true);
           }
           if (pc.getInventory().checkAddItem(item, count) == 0) {
             pc.getInventory().storeItem(item);
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\fY物品：[\\aA" + item.getLogName() + "\\fY] 商品編號：[\\aA" + itemid + "\\fY] 魅力：[\\aA" + enchant + "\\fY] \\aG生產"));
           }
         } else {
           L1ItemInstance item = null;
           int createCount;
           for (createCount = 0; createCount < count; ) {
             item = ItemTable.getInstance().createItem(itemid);
             item.setEnchantLevel(enchant);
             item.setAttrEnchantLevel(attrenchant);
             if (isId == 1) {
               item.setIdentified(true);
             }
             if (pc.getInventory().checkAddItem(item, 1) == 0) {
               pc.getInventory().storeItem(item);

               createCount++;
             }
           }
           if (createCount > 0) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\fY物品：[\\aA" + item.getLogName() + "\\fY] 商品編號：[\\aA" + itemid + "\\fY] 魅力：[\\aA" + enchant + "\\fY] \\aG生產"));
           }
         }
       } else {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("指定ID的項目不存在"));
       }

     } catch (Exception e) {

       pc.sendPackets((ServerBasePacket)new S_SystemMessage(".物品[名稱][數量][附魔][屬性1~20][確認0~1]"));
     }
   }
 }



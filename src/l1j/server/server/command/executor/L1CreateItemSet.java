 package l1j.server.server.command.executor;

 import java.util.List;
 import java.util.StringTokenizer;
 import l1j.server.server.GMCommandsConfig;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.templates.L1ItemSetItem;


 public class L1CreateItemSet
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1CreateItemSet();
   }


   @override
   public void execute(L1PcInstance pc, String cmdName, String arg) {
       try {// 解析命令參數以獲取物品組名稱
           String name = new StringTokenizer(arg).nextToken();
           List<L1ItemSetItem> list = GMCommandsConfig.ITEM_SETS.get(name);
           // 如果物品組不存在，向玩家發送錯誤訊息並返回
           if (list == null) {
               pc.sendPackets(new S_SystemMessage(name + "不存在。"));
               return;
           }

           L1Item temp = null;
           L1ItemInstance inst = null;
           // 遍歷物品組中的每個物品
           for (L1ItemSetItem item : list) {
               temp = ItemTable.getInstance().getTemplate(item.getId());
               // 處理不可堆疊且有附魔等級的物品
               if (!temp.isStackable() && item.getEnchant() != 0) {
                   for (int i = 0; i < item.getAmount(); i++) {
                       inst = ItemTable.getInstance().createItem(item.getId());
                       inst.setEnchantLevel(item.getEnchant());
                       pc.getInventory().storeItem(inst);
                   }
               } else {// 處理可堆疊或無附魔等級的物品
                   pc.getInventory().storeItem(item.getId(), item.getAmount());
               }
           }

       } catch (Exception e) {// 捕捉異常並向玩家發送錯誤訊息，提示正確的命令格式
           pc.sendPackets(new S_SystemMessage(".設定項目 請輸入設定名稱。"));
       }
   }



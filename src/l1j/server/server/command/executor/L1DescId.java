 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import java.util.logging.Logger;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1DescId
   implements L1CommandExecutor {
   private static Logger _log = Logger.getLogger(L1DescId.class.getName());




   public static L1CommandExecutor getInstance() {
     return new L1DescId();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer st = new StringTokenizer(arg);
       int descid = Integer.parseInt(st.nextToken(), 10);
       int count = Integer.parseInt(st.nextToken(), 10);
       int countA = -1;
       L1ItemInstance item = null;
       for (int i = 0; i < count; i++) {
         if (pc.getInventory().getSize() > 255) {
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aG庫存項目的最大數量為 256。"));
           break;
         }
         countA++;
         item = ItemTable.getInstance().createItem(40005);
         item.getItem().setType2(0);
         item.getItem().setItemDescId(descid + i);
         item.getItem().setGfxId(1945);
         item.setCount(1);
         item.getItem().setName(String.valueOf(descid + i));
         item.getItem().setNameId(String.valueOf(descid + i));
         item.setIdentified(true);
         pc.getInventory().storeItem(item);
       }
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aHETC" + descid + "~" + String.valueOf(descid + countA) + " 已經被創造了。"));
     } catch (Exception exception) {
       int count = 0;
       for (L1ItemInstance item : pc.getInventory().getItems()) {
         if (item.getItemId() == 40005) {
           pc.getInventory().deleteItem(item);
           count++;
         }
       }
       if (count > 0) {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aAETC確認用單品（" + count + "） 已刪除。"));
       } else {

         pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[id]作為[出現的號碼]。"));
       }
     }
   }
 }



 package l1j.server.server.clientpackets;

 import java.util.ArrayList;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;

 public class C_EQCShop
   extends ClientBasePacket {
   public C_EQCShop(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);
     try {
       int PcObjectId = readD();
       readC();
       readC();
       readC();

       L1PcInstance pc = clientthread.getActiveChar();
       L1Object findObject = L1World.getInstance().findObject(PcObjectId);

       if (findObject instanceof L1PcInstance &&
         findObject.getId() == pc.getId()) {
         int objid = readD();
         int count = readD();
         if (pc.getEQCList() == null || pc.getEQCList().size() <= 0 || count > 1) {
           return;
         }
         ArrayList<L1ItemInstance> list = pc.getEQCList();

         int lsz = list.size();
         for (int i = 0; i < lsz; i++) {
           L1ItemInstance item = list.get(objid);

           if (pc.isTwoLogin(pc)) {
             return;
           }

           if (item == null) {
             return;
           }

           if (!item.isStackable() && count != 1) {
             return;
           }
           if (count <= 0 || item.getCount() <= 0) {
             return;
           }

           if (count > 1) {
             count = 1;
           }

             // 檢查物品ID是否匹配
             if (objid == i) {
                 // 獲取目標裝備項
                 L1ItemInstance targetItem = pc.getEquipmentChangeItem();
                 if (targetItem == null)
                     return;

                 // 檢查玩家是否擁有目標裝備和裝備交換券
                 if (!pc.getInventory().checkItem(
                         targetItem.getItem().getItemId(),
                         1,
                         targetItem.getEnchantLevel(),
                         targetItem.getItem().getBless(),
                         targetItem.getAttrEnchantLevel()
                 ) ||
                         !pc.getInventory().checkItem(pc.getEquipmentChangeUseItemId(), 1)) {

                     // 如果玩家沒有裝備交換券，發送系統消息通知玩家
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("你沒有裝備交換券。"));
                     return;
                 }

                 // 這裡可以添加進一步的裝備交換邏輯
             }
             pc.getInventory().consumeItem(pc.getEquipmentChangeUseItemId(), 1);
             pc.getInventory().removeItem(pc.getEquipmentChangeItem());
             pc.getInventory().storeTradeItem(item);
             break;
           }
           L1World.getInstance().removeObject((L1Object)item);
         }

         list.clear();
         pc.setEQCList(null);

         return;
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       clear();
     }
   }


   public String getType() {
     return "[C] C_EQCShop";
   }
 }



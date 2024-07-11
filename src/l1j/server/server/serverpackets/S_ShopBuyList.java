     package l1j.server.server.serverpackets;

     import java.util.List;
     import l1j.server.server.datatables.ShopTable;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1World;
     import l1j.server.server.model.shop.L1AssessedItem;
     import l1j.server.server.model.shop.L1Shop;


     public class S_ShopBuyList
       extends ServerBasePacket
     {
       private static final String S_SHOP_BUY_LIST = "[S] S_ShopBuyList";

       public S_ShopBuyList(int objid, L1PcInstance pc) {
         L1Object object = L1World.getInstance().findObject(objid);
         if (!(object instanceof L1NpcInstance)) {
           throw new IllegalArgumentException(String.format("未知物件已被傳送到商店購買清單。[物件ID：%d，L1物件：%d]", new Object[] { Integer.valueOf(objid), Integer.valueOf((object == null) ? 0 : object.getL1Type()) }));
         }
           L1NpcInstance npc = (L1NpcInstance)object; // 將物件轉型為 L1NpcInstance
           int npcId = npc.getNpcTemplate().get_npcId(); // 從NPC模板獲取NPC ID
           L1Shop shop = ShopTable.getInstance().get(npcId); // 嘗試獲取對應的商店實例
           if (shop == null) { // 如果找不到對應的商店
               pc.sendPackets(new S_NoSell(npc)); // 向玩家發送無法銷售的信息包
               System.out.println(String.format("[忽略錯誤:確認] 無法找到銷售商店。[npcid：%d]", new Object[] { Integer.valueOf(npcId) })); // 在系統日誌中輸出錯誤信息
               throw new IllegalArgumentException(String.format("[忽略錯誤:確認] 無法找到銷售商店。[npcid：%d]", new Object[] { Integer.valueOf(npcId) })); // 拋出異常
           }

         List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());

         writeC(40);
         writeD(objid);
         writeH(assessedItems.size());

         int real_count = 0;
         for (L1AssessedItem item : assessedItems) {
           writeD(item.getTargetId());
           writeD(item.getAssessedPrice());
           real_count++;
         }
         if (real_count <= 0) {
           pc.sendPackets(new S_NoSell(npc));
           throw new IllegalArgumentException(String.format("無法找到銷售物品清單。[npcid：%d]", new Object[] { Integer.valueOf(npcId) }));
         }
         if (npcId == 8502050) {
           writeH(26532);
         } else {
           writeH(7);
         }
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_ShopBuyList";
       }
     }



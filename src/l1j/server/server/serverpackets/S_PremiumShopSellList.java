     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.util.ArrayList;
     import java.util.List;
     import l1j.server.server.datatables.ItemTable;
     import l1j.server.server.datatables.ShopTable;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1World;
     import l1j.server.server.model.shop.L1Shop;
     import l1j.server.server.templates.L1Item;
     import l1j.server.server.templates.L1ShopItem;





     public class S_PremiumShopSellList
       extends ServerBasePacket
     {
       public S_PremiumShopSellList(int objId, L1PcInstance pc) {
         writeC(131);
         writeD(objId);
         writeC(0);
         L1Object npcObj = L1World.getInstance().findObject(objId);
         if (!(npcObj instanceof L1NpcInstance)) {
           writeH(0);
           return;
         }
         int npcId = ((L1NpcInstance)npcObj).getNpcTemplate().get_npcId();

         L1Shop shop = ShopTable.getInstance().get(npcId);
         List<L1ShopItem> shopItems = null;
         List<L1ShopItem> passList = new ArrayList<>();

           try {
               // 嘗試獲取商店正在出售的物品列表
               shopItems = shop.getSellingItems();
           } catch (Exception e) {
               // 如果發生異常，打印錯誤信息
               System.out.println("點擊商店（NPC）時：NPCID（無物品或更多） : " + npcId);
           }

         if (shopItems != null) {
           for (L1ShopItem si : shopItems) {
             if (si.get_classType() != 10 &&
               si.get_classType() != pc.getType()) {
               passList.add(si);
             }


             if (si.getBuyLevel() != 0 &&
               pc.getLevel() > si.getBuyLevel()) {
               passList.add(si);
             }
           }

           writeH(shopItems.size() - passList.size());
         } else {
           writeH(0);

           return;
         }

         L1ItemInstance dummy = new L1ItemInstance();
         L1ShopItem shopItem = null;
         L1Item item = null;
         L1Item template = null;
         for (int i = 0; i < shopItems.size(); i++) {
           shopItem = shopItems.get(i);

           if (!passList.contains(shopItem)) {


             item = shopItem.getItem();
             int price = shopItem.getPrice();
             writeD(i);
             writeD(shopItem.getItem().getItemDescId());
             try {
               writeH(shopItem.getItem().getGfxId());
             } catch (Exception e) {
               System.out.println("點擊商店（NPC）時：NPCID（無物品或更多） : " + npcId);
             }
             writeD(price);
             if (shopItem.getPackCount() > 1) {
               writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
             } else if (shopItem.getEnchant() > 0) {
               writeS("+" + shopItem.getEnchant() + " " + item.getName());
             } else if (shopItem.getItem().getMaxUseTime() > 0) {
               writeS(item.getName() + " [" + item.getMaxUseTime() + "]");
             } else {
               writeS(item.getName());
             }
             int type = shopItem.getItem().getUseType();
             if (type < 0) {
               type = 0;
             }

             writeD(type);
             template = ItemTable.getInstance().getTemplate(item.getItemId());
             if (template == null) {
               writeC(0);
             } else {
               dummy.setItem(template);
               dummy.setEnchantLevel(shopItem.getEnchant());
               byte[] status = dummy.getStatusBytes();
               if (status != null) {

                 int bit = 0;
                 if (!template.isTradable())
                   bit += 2;
                 if (template.isCantDelete())
                   bit += 4;
                 if (template.get_safeenchant() < 0)
                   bit += 8;
                 if (item.getBless() >= 128) {
                   bit = 14;
                 } else {
                   bit |= 0x80;
                 }
                 writeD(bit);
                 writeD(0);
                 writeC(0);
                 writeC(status.length);
                 for (byte b : status) {
                   writeC(b);
                 }
               } else {
                 System.out.println("物品錯誤訊息通知 : " + dummy.getItemId());
               }
             }
           }
         }

         if (npcId == 900107) {
           writeC(255);
           writeC(255);
           writeC(0);
           writeC(0);
         }
         else if (npcId == 6000002) {
           writeH(15567);


         }
         else if (npcId == 5073 || npcId == 523) {
           writeH(15567);



         }
         else if (npcId == 7320121 || npcId == 7320085 || npcId == 73201211 || npcId == 73201212 || npcId == 73201213 || npcId == 73201214 || npcId == 73201215 || npcId == 73201216 || npcId == 73201217 || npcId == 73201218 || npcId == 73201219 || npcId == 202056 || npcId == 8502049 || npcId == 2020700 || npcId == 2020701 || npcId == 2020702 || npcId == 2020703 || npcId == 2020704 || npcId == 2020705 || npcId == 2020706 || npcId == 2020707 || npcId == 2020708 || npcId == 2020709) {




           writeH(26532);
         } else {
           writeC(111);
           writeC(10);
         }
       }


       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }



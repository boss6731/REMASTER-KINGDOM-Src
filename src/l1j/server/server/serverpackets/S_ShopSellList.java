     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.util.ArrayList;
     import java.util.Date;
     import java.util.List;
     import l1j.server.Config;
     import l1j.server.NpcShopCash.NpcShopCashTable;
     import l1j.server.server.datatables.ItemTable;
     import l1j.server.server.datatables.ShopBuyLimitInfo;
     import l1j.server.server.datatables.ShopTable;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1TaxCalculator;
     import l1j.server.server.model.L1World;
     import l1j.server.server.model.shop.L1Shop;
     import l1j.server.server.templates.L1Item;
     import l1j.server.server.templates.L1ShopItem;
     import l1j.server.server.templates.ShopBuyLimit;
     import l1j.server.server.templates.eShopBuyLimitType;


     public class S_ShopSellList
       extends ServerBasePacket
     {
       public static class ShopSellListException
         extends Exception
       {
         private static final long serialVersionUID = 1L;

         public ShopSellListException() {}

         public ShopSellListException(String message) {
           super(message);
         }
       }

       public static class MinigameSellListException
         extends Exception
       {
         private static final long serialVersionUID = 1L;

         public MinigameSellListException() {}

         public MinigameSellListException(String message) {
           super(message);
         }
       }


       public S_ShopSellList(int objId, L1PcInstance pc) throws MinigameSellListException {
         writeC(131);
         writeD(objId);
         writeC(0);
         L1Object npcObj = L1World.getInstance().findObject(objId);
         if (!(npcObj instanceof L1NpcInstance)) {
           writeH(0);
           return;
         }
         int npcId = ((L1NpcInstance)npcObj).getNpcTemplate().get_npcId();
         L1TaxCalculator calc = new L1TaxCalculator(npcId);
         L1Shop shop = ShopTable.getInstance().get(npcId);

         if (shop == null) {
           writeH(0);

           return;
         }
         List<L1ShopItem> shopItems = null;
         List<L1ShopItem> passList = new ArrayList<>();
         try {
           shopItems = shop.getSellingItems(); // 嘗試從shop獲取銷售物品列表
         } catch (Exception e) {
           e.printStackTrace(); // 捕捉異常並打印堆疊軌跡
           if (npcId == 8502074 || npcId == 70041 || npcId == 170041 || npcId == 370041) {
             pc.sendPackets(new S_NPCTalkReturn(objId, "maeno3")); // 向玩家發送消息
             throw new MinigameSellListException(String.format("無法找到購買店鋪。[npcid : %d]", new Object[] { Integer.valueOf(npcId) })); // 拋出自定義異常並傳遞錯誤信息
           }
           System.out.println("[NPC點擊時] : NPCID(無物品/異常)(忽略特定NPC) : " + npcId); // 打印錯誤信息

           writeH(0); // 設置狀態或標誌
           return; // 結束方法執行
         }
         if (shopItems != null) {

           Date date = new Date(0L);
           for (L1ShopItem si : shopItems) {
             if (!si.isTimeLimit()) {
               continue;
             }
             date.setTime(pc.getFishingShopBuyTime_1());
             int item_day = date.getDate();
             date.setTime(System.currentTimeMillis());
             int current_day = date.getDate();
             if (item_day == current_day) {
               passList.add(si);
             }
           }
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


           for (L1ShopItem st : shopItems) {
             ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(st.getItemId());
             if (sbl != null) {
               if (sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT || sbl
                 .get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
                 ShopBuyLimit char_sbl = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(pc.getId(), st
                     .getItemId());
                 if (char_sbl != null && char_sbl.get_end_time() != null &&
                   char_sbl.get_count() <= 0 && char_sbl
                   .get_end_time().getTime() > System.currentTimeMillis()) {
                   passList.add(st);
                 }
               }

               if (sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || sbl
                 .get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {

                 ShopBuyLimit char_sbl_account = ShopBuyLimitInfo.getInstance().findShopBuyLimitByAccount(pc.getAccount().getName(), st.getItemId());
                 if (char_sbl_account != null && char_sbl_account.get_end_time() != null &&
                   char_sbl_account.get_count() <= 0 && char_sbl_account
                   .get_end_time().getTime() > System.currentTimeMillis()) {
                   passList.add(st);
                 }
               }
             }
           }


           writeH(shopItems.size() - passList.size());
         } else {
           writeH(0);

           return;
         }
         if (shopItems != null && shopItems.size() <= 0 && (
                 npcId == 8502074 || npcId == 70041 || npcId == 170041 || npcId == 370041)) {
           // 如果shopItems不為空且其大小小於或等於0，並且npcId在指定範圍內
           pc.sendPackets(new S_NPCTalkReturn(objId, "maeno3")); // 向玩家發送消息，可能是顯示對話或提示
           throw new MinigameSellListException(String.format("票券是空的。 [npcid : %d]", new Object[] { Integer.valueOf(npcId) })); // 拋出自定義異常並傳遞錯誤信息
         }

         L1ItemInstance dummy = new L1ItemInstance();
         L1ShopItem shopItem = null;
         L1Item item = null;
         L1Item template = null;
         for (int i = 0; i < shopItems.size(); i++) {
           shopItem = shopItems.get(i);
           String itemname = "";
           if (!passList.contains(shopItem)) {


             item = shopItem.getItem();
             int price = calc.layTax((int)(shopItem.getPrice() * Config.ServerRates.RateShopSellingPrice));

             int price1 = shopItem.getPrice();
             writeD(i);
             writeD(0);

             try {
               writeH(shopItem.getItem().getGfxId()); // 嘗試寫入物品的圖形ID
             } catch (Exception e) {
               System.out.println("NPC商店錯誤，NPC編號：" + npcId); // 捕捉異常並打印錯誤信息
             }

             if (npcId == 8502074 || npcId == 70041 || npcId == 170041 || npcId == 70042) {
               writeD(price1); // 根據NPC編號寫入價格 price1
             } else {
               writeD(price); // 否則寫入價格 price
             }

               if (shopItem.getPackCount() > 1) {
                   itemname = item.getNameId() + " (" + shopItem.getPackCount() + ")";
               } else if (shopItem.getEnchant() > 0) {
                   itemname = "+" + shopItem.getEnchant() + " " + item.getName();
               } else if (shopItem.getItem().getMaxUseTime() > 0) {
                   itemname = item.getName() + " [" + item.getMaxUseTime() + "]";
               } else if (item.getItemId() >= 140074 && item.getItemId() <= 140100) {
                   itemname = "受祝福的 " + item.getName();

               } else if (item.getItemId() >= 240074 && item.getItemId() <= 240087) {
                   itemname = "被詛咒的 " + item.getName();
               } else {
                   itemname = item.getName();
               }

               if (shopItem.getAttrEnchant() > 0) {
                   itemname = getAttrToDisplay(shopItem.getAttrEnchant()) + itemname;
               }

               if (shopItem.isCarving()) {
                   writeS(itemname + "(刻印)");
               } else {
                   writeS(itemname);
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
               ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(dummy.getItemId());
               if (sbl != null) {
                 dummy._cha = pc;
               }
               byte[] status = dummy.getStatusBytes(pc);
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
                   System.out.println("物品錯誤信息通知 : " + dummy.getItemId()); // 輸出物品錯誤信息及其 ID
               }
             }
           }
         }  NpcShopCashTable.L1CashType at = NpcShopCashTable.getInstance().getNpcCashType(npcId);
         if (at != null) {
           writeH(at.getCashDesc());
         } else {
           writeH(7);
         }
       }

         private static final String[] _attrToDisplay = new String[] {
                 "", // 無屬性或默認值
                 "火靈:1段 ", "火靈:2段 ", "火靈:3段 ", "火靈:4段 ", "火靈:5段 ", // 火靈 1-5 級
                 "水靈:1段 ", "水靈:2段 ", "水靈:3段 ", "水靈:4段 ", "水靈:5段 ", // 水靈 1-5 級
                 "風靈:1段 ", "風靈:2段 ", "風靈:3段 ", "風靈:4段 ", "風靈:5段 ", // 風靈 1-5 級
                 "地靈:1段 ", "地靈:2段 ", "地靈:3段 ", "地靈:4段 ", "地靈:5段 "  // 地靈 1-5 級
         };


       public static String getAttrToDisplay(int attr) {
         if (attr < 0 || attr >= _attrToDisplay.length)
           return "";
         return _attrToDisplay[attr];
       }

       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }



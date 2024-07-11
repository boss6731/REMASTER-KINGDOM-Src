     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.util.ArrayList;
     import java.util.List;
     import l1j.server.NpcShopCash.NpcShopCashTable;
     import l1j.server.server.datatables.ItemTable;
     import l1j.server.server.datatables.ShopBuyLimitInfo;
     import l1j.server.server.datatables.ShopTable;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.shop.L1Shop;
     import l1j.server.server.templates.L1Item;
     import l1j.server.server.templates.L1ShopItem;
     import l1j.server.server.templates.ShopBuyLimit;
     import l1j.server.server.templates.eShopBuyLimitType;

     public class S_亞丁商店
       extends ServerBasePacket
     {
       public enum Currency {
         ADENA(7),
         NCOIN(-2);
         private int val;

         Currency(int val) {
           this.val = val;
         }

         int val() {
           return this.val;
         }
       }

       public S_亞丁商店(L1PcInstance pc, Currency currency) {
         writeC(131);
         writeD(7626);
         writeC(0);

         int npcId = 5;

         L1Shop shop = ShopTable.getInstance().get(npcId);
         List<L1ShopItem> shopItems = shop.getSellingItems();
         List<L1ShopItem> passList = new ArrayList<>();


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


           if (st.getBuyLevel() != 0 &&
             pc.getLevel() > st.getBuyLevel()) {
             passList.add(st);
           }
         }


         writeH(shopItems.size() - passList.size());

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
             writeH(shopItem.getItem().getGfxId());
             writeD(price);
             if (shopItem.getPackCount() > 1) {
               writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
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
                 System.out.println("項目錯誤訊息通知： " + dummy.getItemId());
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

       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }



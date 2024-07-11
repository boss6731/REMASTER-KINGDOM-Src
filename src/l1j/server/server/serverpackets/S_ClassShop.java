     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.util.ArrayList;
     import java.util.List;
     import l1j.server.server.datatables.ItemTable;
     import l1j.server.server.datatables.ShopBuyLimitInfo;
     import l1j.server.server.datatables.ShopTable;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1World;
     import l1j.server.server.model.shop.L1Shop;
     import l1j.server.server.templates.L1Item;
     import l1j.server.server.templates.L1ShopItem;
     import l1j.server.server.templates.ShopBuyLimit;
     import l1j.server.server.templates.eShopBuyLimitType;

     public class S_ClassShop
       extends ServerBasePacket
     {
       public S_ClassShop(L1PcInstance pc, int objId) {
         writeC(131);
         L1Object npcObj = L1World.getInstance().findObject(objId);
         int npcId = ((L1NpcInstance)npcObj).getNpcTemplate().get_npcId();


         if (npcId == 7320121) {
           if (pc.isCrown()) {
             npcId = 73201211;
           } else if (pc.isWizard()) {
             npcId = 73201212;
           } else if (pc.isElf()) {
             npcId = 73201213;
           } else if (pc.isDarkelf()) {
             npcId = 73201214;
           } else if (pc.isDragonknight()) {
             npcId = 73201215;
           } else if (pc.isBlackwizard()) {
             npcId = 73201216;
           } else if (pc.is전사()) {
             npcId = 73201217;
           } else if (pc.isKnight()) {
             npcId = 73201218;
           } else if (pc.isFencer()) {
             npcId = 73201219;
           } else if (pc.isLancer()) {
             npcId = 73201220;




           }



         }
         else if (npcId == 8502049) {
           if (pc.isCrown()) {
             npcId = 2020700;
           } else if (pc.isWizard()) {
             npcId = 2020701;
           } else if (pc.isElf()) {
             npcId = 2020702;
           } else if (pc.isDarkelf()) {
             npcId = 2020703;
           } else if (pc.isDragonknight()) {
             npcId = 2020704;
           } else if (pc.isBlackwizard()) {
             npcId = 2020705;
           } else if (pc.is전사()) {
             npcId = 2020706;
           } else if (pc.isKnight()) {
             npcId = 2020707;
           } else if (pc.isFencer()) {
             npcId = 2020708;
           } else if (pc.isLancer()) {
             npcId = 2020709;
           }
         }

         writeD(npcId);
         writeC(0);
         L1Shop shop = ShopTable.getInstance().get(npcId);
         List<L1ShopItem> shopItems = shop.getSellingItems();

         List<L1ShopItem> passList = new ArrayList<>();

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
         } else {

           writeH(0);

           return;
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
                 System.out.println("通知物品錯誤信息 : " + dummy.getItemId());
               }
             }
           }
         }  if (npcId == 7320121 || npcId == 7320085 || npcId == 73201211 || npcId == 73201212 || npcId == 73201213 || npcId == 73201214 || npcId == 73201215 || npcId == 73201216 || npcId == 73201217 || npcId == 73201218 || npcId == 73201219 || npcId == 8502049 || npcId == 2020700 || npcId == 2020701 || npcId == 2020702 || npcId == 2020703 || npcId == 2020704 || npcId == 2020705 || npcId == 2020706 || npcId == 2020707 || npcId == 2020708 || npcId == 2020709) {




           writeH(26532);
         } else {
           writeH(7);
         }
       }


       public byte[] getContent() throws IOException {
         return this._bao.toByteArray();
       }
     }



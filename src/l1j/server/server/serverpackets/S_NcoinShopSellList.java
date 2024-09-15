package l1j.server.server.serverpackets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopBuyLimitInfo;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.templates.eShopBuyLimitType;

public class S_NcoinShopSellList extends ServerBasePacket {

   /**
    * 顯示商店的物品列表。當角色按下購買按鈕時發送。
    */
   public S_NcoinShopSellList(L1PcInstance pc, int objId) {
      writeC(Opcodes.S_BUY_LIST);
      writeD(objId);
      writeC(0);
      L1Object npcObj = L1World.getInstance().findObject(objId);
      if (!(npcObj instanceof L1NpcInstance)) {
         writeH(0);
         return;
      }
      int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

      L1Shop shop = ShopTable.getInstance().get(npcId);
      List<L1ShopItem> shopItems = null;      
            
      try {
         shopItems = shop.getSellingItems();
      } catch (Exception e) {
         System.out.println("商店(NPC)點擊時 : NPCID(物品無/異常) : " + npcId);
      }
      if (shopItems != null) {
         writeH(shopItems.size());
      } else {
         writeH(0);
         return;
      }

      // 因為要使用 L1ItemInstance 的 getStatusBytes
      L1ItemInstance dummy = new L1ItemInstance();
      L1ShopItem shopItem = null;
      L1Item item = null;
      L1Item template = null;
      DecimalFormat _decF = new DecimalFormat("#,###");
      
      for (int i = 0; i < shopItems.size(); i++) {
         shopItem = (L1ShopItem) shopItems.get(i);
         item = shopItem.getItem();
         int price = shopItem.getPrice();         
                  
         /*         if (i == 0) {
         writeD(i);
         writeD(0);
         writeH(1864);// 只需更改圖標編號
         writeD(price);
         writeS(String.format("\a2N幣持有量:\f3 %s元", _decF.format(pc.getAccount().Ncoin_point)));//這裡的說明
         writeD(0);

         writeD(0x81);
         writeD(0x00);
         writeC(0x00);
         writeC(0);

         } else {*/
            writeD(i);
            writeD(0);
            try {
               writeH(shopItem.getItem().getGfxId());
            } catch (Exception e) {
               System.out.println("商店(NPC)點擊時 : NPCID(物品無/異常) : " + npcId);
            }
            writeD(price);
            if (i == 0){
               writeS(String.format("\\a2N幣持有量:\f3 %s元", _decF.format(pc.getAccount().Ncoin_point)));//這裡的說明
            } else if (shopItem.getPackCount() > 1) {
               writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
            } else if (shopItem.getEnchant() > 0) {
               writeS("+" + shopItem.getEnchant() + " " + item.getName());
            } else if (shopItem.getItem().getMaxUseTime() > 0) {
               writeS(item.getName() + " [" + item.getMaxUseTime() + "]");
            }else {
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
               writeD(dummy.getStatusBit());
               writeD(0x00);
               writeC(0x00);
               // 添加封包
               dummy.setEnchantLevel(shopItem.getEnchant());
               byte[] status = dummy.getStatusBytes();
               writeC(status.length);
               for (byte b : status) {
                  writeC(b);
 //              }
            }
         }
      }
      writeH(-2);
   }

   @Override
   public byte[] getContent() throws IOException {
      return getBytes();
   }
}
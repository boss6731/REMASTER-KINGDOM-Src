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
      writeC(Opcodes.S_BUY_LIST); // 寫入操作碼，表示購買列表
      writeD(objId); // 寫入NPC對象ID
      writeC(0); // 寫入初始值

      L1Object npcObj = L1World.getInstance().findObject(objId); // 查找NPC對象
      if (!(npcObj instanceof L1NpcInstance)) {
         writeH(0); // 如果不是L1NpcInstance，寫入0並返回
         return;
      }

      int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId(); // 獲取NPC ID
      L1Shop shop = ShopTable.getInstance().get(npcId); // 根據NPC ID獲取商店
      List<L1ShopItem> shopItems = null;

      try {
         shopItems = shop.getSellingItems(); // 嘗試獲取商店物品列表
      } catch (Exception e) {
         System.out.println("點擊商店(NPC)時發生錯誤: NPCID(無物品/異常): " + npcId); // 錯誤處理
      }

      if (shopItems != null) {
         writeH(shopItems.size()); // 寫入物品列表的大小
      } else {
         writeH(0); // 如果列表為空，寫入0並返回
         return;
      }
   }
      
      // 使用 L1ItemInstance 的 getStatusBytes
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
         writeD(i); // 寫入當前物品的索引值
         writeD(0); // 寫入初始值
         writeH(1864); // 替換圖標編號
         writeD(price); // 寫入價格
         writeS(String.format("\a2N코인 보유量:\f3 %s원", _decF.format(pc.getAccount().Ncoin_point))); // 顯示玩家的 NCoin 餘額
         writeD(0); // 寫入初始值

         writeD(0x81); // 寫入固定值
         writeD(0x00); // 寫入固定值
         writeC(0x00); // 寫入初始值
         writeC(0); // 寫入初始值

         } else {*/
      writeD(i); // 寫入當前物品的索引值
      writeD(0); // 寫入初始值
      try {
         writeH(shopItem.getItem().getGfxId()); // 嘗試寫入物品的圖像ID
      } catch (Exception e) {
         System.out.println("點擊商店(NPC)時發生錯誤: NPCID(無物品/異常): " + npcId); // 捕獲異常並打印錯誤信息
      }
      writeD(price); // 寫入價格
      if (i == 0){
         writeS(String.format("\a2N幣 擁有:\f3 %s元", _decF.format(pc.getAccount().Ncoin_point))); // 顯示玩家的 NCoin 餘額
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
               // 新增資料包
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
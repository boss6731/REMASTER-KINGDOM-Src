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
     * 가게의 물건 리스트를 표시한다. 캐릭터가 BUY 버튼을 눌렀을 때에 보낸다.
     * 用於顯示店鋪的物品列表，當角色點擊「BUY」按鈕時發送。
     */
    public S_NcoinShopSellList(L1PcInstance pc, int objId) {
        // 寫入封包操作碼，用於表示購買列表
        writeC(Opcodes.S_BUY_LIST);

        // 寫入物品的對象 ID
        writeD(objId);

        // 寫入一個字節值 0
        writeC(0);

        // 根據對象 ID 查找對應的 NPC 對象
        L1Object npcObj = L1World.getInstance().findObject(objId);

        // 檢查找到的對象是否是 NPC 實例
        if (!(npcObj instanceof L1NpcInstance)) {
        // 如果不是 NPC 實例，寫入 0 並返回
            writeH(0);
            return;
        }

        // 獲取 NPC 的 ID
        int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

        // 獲取店鋪對象
        L1Shop shop = ShopTable.getInstance().get(npcId);

        // 定義一個列表來存放店鋪的物品
        List<L1ShopItem> shopItems = null;

        try {
        // 嘗試獲取店鋪的銷售物品列表
            shopItems = shop.getSellingItems();
        } catch (Exception e) {
        // 捕獲異常並輸出錯誤信息
            System.out.println("點擊商店（NPC）時 : NPCID（物品不存在或異常） : " + npcId);
        }

        // 檢查物品列表是否為空
        if (shopItems != null) {
        // 如果物品列表不為空，寫入物品列表的大小
            writeH(shopItems.size());
        } else {
        // 如果物品列表為空，寫入 0 並返回
            writeH(0);
            return;
        }
    }
      
      // L1ItemInstance의 getStatusBytes를 이용하기 위해(때문에)
      L1ItemInstance dummy = new L1ItemInstance();
      L1ShopItem shopItem = null;
      L1Item item = null;
      L1Item template = null;
      DecimalFormat _decF = new DecimalFormat("#,###");
      
      for (int i = 0; i < shopItems.size(); i++) {
         shopItem = (L1ShopItem) shopItems.get(i);
         item = shopItem.getItem();
         int price = shopItem.getPrice();         
                  
/*  如果 i 等於 0，執行以下代碼塊
if (i == 0) {
    // 寫入整數值 i
    writeD(i);
    // 寫入整數值 0
    writeD(0);
    // 寫入短整數值 1864 (可能是圖標號)
    writeH(1864); // 아이콘번호만 바꿔주세영 意為「請只更改圖標號」
    // 寫入價格
    writeD(price);
    // 寫入格式化的字符串，表示 N 幣持有量
    writeS(String.format("\a2N코인 보유량:\f3 %s원", _decF.format(pc.getAccount().Ncoin_point))); // 여기 맨트랑 意為「這裡的文字」
    // 寫入整數值 0
    writeD(0);
    // 寫入一組固定的整數和字節值
    writeD(0x81);
    writeD(0x00);
    writeC(0x00);
    writeC(0);
} else {
*/
        // 寫入整數 i
        writeD(i);

        // 寫入整數 0
        writeD(0);

        // 嘗試寫入物品的圖標 ID，如果發生異常則輸出錯誤信息
        try {
            writeH(shopItem.getItem().getGfxId());
        } catch (Exception e) {
            System.out.println("點擊商店（NPC）時 : NPCID（物品不存在或異常） : " + npcId);
        }

        // 寫入價格
        writeD(price);

        // 根據 i 的值和物品屬性寫入相應的字符串
        if (i == 0) {
            writeS(String.format("\a2N硬幣 持有量:\f3 %s元", _decF.format(pc.getAccount().Ncoin_point))); // 여기 맨트랑 意為「這裡的文字」
        } else if (shopItem.getPackCount() > 1) {
            writeS(item.getName() + " (" + shopItem.getPackCount() + ")"); // 如果物品包數量大於 1，寫入物品名稱和包數量
        } else if (shopItem.getEnchant() > 0) {
            writeS("+" + shopItem.getEnchant() + " " + item.getName()); // 如果物品有附魔，寫入附魔等級和物品名稱
        } else if (shopItem.getItem().getMaxUseTime() > 0) {
            writeS(item.getName() + " [" + item.getMaxUseTime() + "]"); // 如果物品有使用時間限制，寫入物品名稱和最大使用時間
        } else {
            writeS(item.getName()); // 否則，只寫入物品名稱
        }

// 獲取物品的使用類型
        int type = shopItem.getItem().getUseType();
        if (type < 0) {
            type = 0; // 如果使用類型小於 0，設置為 0
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
               // 패킷 추가 
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
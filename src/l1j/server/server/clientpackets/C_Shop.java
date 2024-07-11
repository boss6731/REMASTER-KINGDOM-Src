package l1j.server.server.clientpackets;

import java.io.UnsupportedEncodingException;
import javolution.util.FastMap;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;



public class C_Shop extends ClientBasePacket {

    private static final String C_SHOP = "[C] C_Shop";

    public C_Shop(byte[] abyte0, GameClient clientthread) {
        super(abyte0);

        // 獲取當前玩家實例
        L1PcInstance pc = clientthread.getActiveChar();

        // 如果玩家為空或處於幽靈狀態，則返回
        if (pc == null || pc.isGhost()) {
            return;
        }

        // 如果玩家處於隱形狀態，則發送提示信息並返回
        if (pc.isInvisble()) {
            pc.sendPackets((ServerBasePacket)new S_ServerMessage(755));
            return;
        }

        // 如果玩家不在地圖ID為800的地圖上，則發送提示信息並返回
        if (pc.getMapId() != 800) {
            pc.sendPackets((ServerBasePacket)new S_SystemMessage("個人商店只能在市場中開啟。"));
            return;
        }
    }
}
        if (pc.getMapId() != 800) {
            if (pc.isFishing()) {
                try {
                    pc.setFishing(false);
                    pc.setFishingTime(0L);
                    pc.setFishingReady(false);
                    pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
                    Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_CharVisualUpdate(pc));
                    FishingTimeController.getInstance().removeMember(pc);
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(2120));
                    return;
                } catch (Exception exception) {}
            } else {

                pc.sendPackets((ServerBasePacket)new S_ServerMessage(3405));

                return;
            }
        }
        // 檢查玩家是否裝備了指定的物品ID，如果裝備了，發送提示信息並返回
        if (pc.getInventory().checkEquipped(22232) || pc.getInventory().checkEquipped(22234) ||
                pc.getInventory().checkEquipped(22233) || pc.getInventory().checkEquipped(22235) ||
                pc.getInventory().checkEquipped(22236) || pc.getInventory().checkEquipped(22237) ||
                pc.getInventory().checkEquipped(22238) || pc.getInventory().checkEquipped(22239) ||
                pc.getInventory().checkEquipped(22240) || pc.getInventory().checkEquipped(22241) ||
                pc.getInventory().checkEquipped(22242) || pc.getInventory().checkEquipped(22243) ||
                pc.getInventory().checkEquipped(22244) || pc.getInventory().checkEquipped(22245) ||
                pc.getInventory().checkEquipped(22246) || pc.getInventory().checkEquipped(22247) ||
                pc.getInventory().checkEquipped(22248) || pc.getInventory().checkEquipped(22249)) {
            pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "請解除裝備所有符文。"));
            return;
        }

// 檢查玩家當前的精靈ID是否與類別ID不同，並且技能效果時間是否小於等於0
        if (pc.getCurrentSpriteId() != pc.getClassId() && pc.getSkillEffectTimeSec(67) <= 0) {
// 向玩家發送系統信息，提示解除變身物品
            pc.sendPackets((ServerBasePacket)new S_SystemMessage("請解除變身物品。"));
            return;
        }

        // 初始化變量 tradable 為 true，表示物品可交易
        boolean tradable = true;

// 讀取操作類型
        int type = readC();
        if (type == 0) { // 如果操作類型為 0

            // 讀取賣出物品的總數量
            int sellTotalCount = readH();

            // 初始化寵物列表變量
            Object[] petlist = null;

            // 循環處理每個賣出的物品
            for (int i = 0; i < sellTotalCount; i++) {
                // 讀取每個賣出物品的相關信息
                int sellObjectId = readD();
                int sellPrice = readD();
                int sellCount = readD();

                // 如果賣出物品的總數量達到 8（超過允許的 7 個），向玩家發送提示信息並返回
                if (sellTotalCount == 8) {
                    pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "物品註冊最多只能達到7個。"));
                    return;
                }
            }
        }

                L1ItemInstance checkItem = pc.getInventory().getItem(sellObjectId);
                if (sellObjectId != checkItem.getId() || (!checkItem.isStackable() && sellCount != 1) || checkItem.getCount() < sellCount || checkItem.getCount() <= 0 || sellCount <= 0) {

                    pc.disposeShopInfo();

                    pc.sendPackets((ServerBasePacket)new S_Disconnect());
                    return;
                }
                if (!checkItem.isStackable() && sellCount != 1) {
                    pc.sendPackets((ServerBasePacket)new S_Disconnect());

                    pc.disposeShopInfo();

                    return;
                }
                if (sellCount > checkItem.getCount()) {
                    sellCount = checkItem.getCount();
                }

                if (checkItem.getBless() >= 128) {
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, checkItem.getItem().getName()));

                    pc.disposeShopInfo();

                    return;
                }
                // 檢查物品是否可交易
                if (!checkItem.getItem().isTradable()) {
                    tradable = false;
                    // 向玩家發送提示信息，告知該物品無法交易
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "此物品無法交易。"));
                }

// 檢查物品是否刻印
                if (checkItem.get_Carving() != 0) {
                    tradable = false;
                    // 向玩家發送提示信息，告知刻印的物品無法交易
                    pc.sendPackets((ServerBasePacket)new S_SystemMessage("刻印的物品無法交易。"), true);
                }

// 檢查物品是否為同伴遺忘物
                if (!MJCompanionInstanceCache.is_companion_oblivion(checkItem.getId())) {
                    // 清理商店信息並發送提示信息
                    pc.disposeShopInfo();
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "此物品無法交易。"));
                    return;
                }

// 獲取玩家的寵物列表並轉換為數組
                petlist = pc.getPetList().values().toArray();
                for (Object petObject : petlist) {
                    if (petObject instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance)petObject;
                        // 檢查物品是否綁定於寵物
                        if (checkItem.getId() == pet.getItemObjId()) {
                            tradable = false;
                            // 向玩家發送提示信息，告知該物品無法交易
                            pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "此物品無法交易。"));
                            break;
                        }
                    }
                }

// 獲取玩家的魔法娃娃並檢查是否存在
                L1DollInstance doll = pc.getMagicDoll();
                if (doll != null && checkItem.getId() == doll.getItemObjId()) {
                    tradable = false;
                    // 向玩家發送提示信息，告知該物品無法交易
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "此物品無法交易。"));
                }


            // 讀取購買物品的總數量
            int buyTotalCount = readH();

// 循環處理每個購買的物品
            for (int j = 0; j < buyTotalCount; j++) {
                // 讀取每個購買物品的相關信息
                int buyObjectId = readD();
                int buyPrice = readD();
                int buyCount = readD();

                // 如果已經達到了賣出物品的最大數量限制（7個），發送提示信息並返回
                if (sellTotalCount == 8) {
                    pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "物品註冊最多只能達到7個。"));
                    return;
                }

                // 從玩家背包中獲取購買的物品
                L1ItemInstance checkItem = pc.getInventory().getItem(buyObjectId);

                // 檢查物品的有效性
                if (buyObjectId != checkItem.getId() ||
                        (!checkItem.isStackable() && buyCount != 1) ||
                        buyCount <= 0 ||
                        checkItem.getCount() <= 0) {

                    // 當物品無效時，清理商店信息並斷開連接
                    pc.disposeShopInfo();
                    pc.sendPackets((ServerBasePacket)new S_Disconnect());
                    return;
                }

                // 如果購買數量大於物品數量，將購買數量設置為物品的實際數量
                if (buyCount > checkItem.getCount()) {
                    buyCount = checkItem.getCount();
                }
            }


                // 從玩家的背包中根據 buyObjectId 獲取物品
                checkItem = pc.getInventory().getItem(buyObjectId);

// 如果物品無法交易，設置 tradable 為 false 並發送提示信息
                if (!checkItem.getItem().isTradable()) {
                    tradable = false;
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "此物品無法交易。"));
                }

// 如果物品已經刻印，設置 tradable 為 false 並發送提示信息
                if (checkItem.get_Carving() != 0) {
                    tradable = false;
                    pc.sendPackets((ServerBasePacket)new S_SystemMessage("刻印的物品無法交易。"), true);
                }

// 檢查物品是否為同伴遺忘物，如果不是，處理商店信息並發送提示信息
                if (!MJCompanionInstanceCache.is_companion_oblivion(checkItem.getId())) {
                    pc.disposeShopInfo();
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "此物品無法交易。"));

                    return;
                }

// 獲取玩家的寵物列表並轉換為數組
                petlist = pc.getPetList().values().toArray();

// 遍歷寵物列表檢查是否有寵物與物品綁定
                for (Object petObject : petlist) {
                    if (petObject instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance)petObject;
                        // 如果物品ID與寵物的物品ObjID相同，設置 tradable 為 false 並發送提示信息
                        if (checkItem.getId() == pet.getItemObjId()) {
                            tradable = false;
                            pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "此物品無法交易。"));

                            break;
                        }
                    }
                }


            if (!tradable) {

                pc.disposeShopInfo();

                pc.setPrivateShop(false);
                pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));
                pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));
                return;
            }
            byte[] chat = readByte();


            String test = null;
            try {
                // 將 chat 字節數組轉換為 "MS949" 編碼的字符串
                test = new String(chat, 0, chat.length, "MS949");
            } catch (UnsupportedEncodingException e) {
                // 如果發生不支持的編碼異常，打印堆棧跟踪
                e.printStackTrace();
            }

// 更新玩家賬戶的商店開設次數
            pc.getNetConnection().getAccount().updateShopOpenCount();

// 向玩家發送包含最新商店開設次數的數據包
            pc.sendPackets((ServerBasePacket)new S_PacketBox(198, (pc.getNetConnection().getAccount()).Shop_open_count), true);

// 設置私人商店的聊天內容
            pc.setShopChat(chat);

// 將玩家狀態設置為私人商店模式
            pc.setPrivateShop(true);

// 向玩家發送商店動作數據包
            pc.sendPackets((ServerBasePacket)new S_DoActionShop(pc.getId(), 70, chat));

// 廣播商店動作數據包，讓其他玩家也能看到
            pc.broadcastPacket((ServerBasePacket)new S_DoActionShop(pc.getId(), 70, chat));

// 向玩家發送聊天信息，提示無人商店開設後可以用其他角色登錄
            pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, ".無人商店開設後，您可以用其他角色登錄。", 1));

            int poly = 0;
            if (test.matches(".*tradezone1.*")) {
                poly = 11479;
            } else if (test.matches(".*tradezone2.*")) {
                poly = 11483;
            } else if (test.matches(".*tradezone3.*")) {
                poly = 11480;
            } else if (test.matches(".*tradezone4.*")) {
                poly = 11485;
            } else if (test.matches(".*tradezone5.*")) {
                poly = 11482;
            } else if (test.matches(".*tradezone6.*")) {
                poly = 11486;
            } else if (test.matches(".*tradezone7.*")) {
                poly = 11481;
            } else if (test.matches(".*tradezone8.*")) {
                poly = 11484;
            }
            test = null;
            pc.商店轉換 = poly;
            pc.setCurrentSprite(poly);
            pc.sendPackets((ServerBasePacket)new S_ChangeShape(pc.getId(), poly, 70));
            Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_ChangeShape(pc.getId(), poly, 70));
            pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));

            pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));

            Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_CharVisualUpdate(pc));
            pc.curePoison();

            GeneralThreadPool.getInstance().execute((Runnable)new MJDShopStorage((L1Character)pc, false));
        }
        else if (type == 1) {
            pc.setPrivateShop(false);
            pc.商店轉換 = 0;
            pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));
            pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));
            L1PolyMorph.undoPolyPrivateShop((L1Character)pc);

            GeneralThreadPool.getInstance().execute((Runnable)new MJDShopStorage((L1Character)pc, true));
        }
    }


    // 定義一個靜態的 FastMap 用於記錄每個賬戶的商店開設次數
    private static FastMap<String, Integer> 商店開設帳號次數 = new FastMap();

    public static boolean get商店開設帳號次數(String account) {
        synchronized (商店開設帳號次數) {
            int time = 0;
            try {
// 獲取指定賬戶的商店開設次數
                time = ((Integer)商店開設帳號次數.get(account)).intValue();
            } catch (Exception exception) {}

// 如果開設次數超過或等於 50，返回 false
            if (time >= 50)
                return false;

// 更新賬戶的商店開設次數並存入 Map
            商店開設帳號次數.put(account, Integer.valueOf(time + 1));
            return true;
        }
    }

    public static void reset商店開設帳號次數() {
        synchronized (商店開設帳號次數) {
// 清空所有賬戶的商店開設次數記錄
            商店開設帳號次數.clear();
        }
    }


    public String getType() {
        return "[C] C_Shop";
    }
}



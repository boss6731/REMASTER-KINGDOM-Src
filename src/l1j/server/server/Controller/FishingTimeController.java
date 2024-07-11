package l1j.server.server.Controller;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.MJExpRevision.MJEFishingType;
import l1j.server.MJExpRevision.MJFishingExpInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_釣魚;
import l1j.server.server.serverpackets.ServerBasePacket;

public class FishingTimeController implements Runnable {

    public static final int SLEEP_TIME = 1000;

    private static FishingTimeController _instance;

    private final List<L1PcInstance> _fishingList = new FastTable<L1PcInstance>();

    private static Random _random = new Random(System.nanoTime());

    public static FishingTimeController getInstance() {
        if (_instance == null) {
            _instance = new FishingTimeController();
        }
        return _instance;
    }

    public void run() {
        try {
            fishing();
        } catch (Exception e1) {
            e1.printStackTrace();// 例外處理輸出
        } finally {
        }
    }

    public void addMember(L1PcInstance pc) {
        if (pc == null || _fishingList.contains(pc)) {
            return;
        }
        _fishingList.add(pc);
    }

    public boolean isMember(L1PcInstance pc) {
        if (_fishingList.contains(pc)) {
            return true;
        }
        return false;
    }

    public void removeMember(L1PcInstance pc) {
        if (pc == null || !_fishingList.contains(pc)) {
            return;
        }
        _fishingList.remove(pc);
    }

    public boolean 成長釣魚 = false; // 註解: 表示是否處於成長釣魚狀態，預設為假
    public boolean 高級成長釣魚 = false; // 註解: 表示是否處於高級成長釣魚狀態，預設為假
    public boolean 古代銀色釣魚 = false; // 註解: 表示是否處於古代銀色釣魚狀態，預設為假
    public boolean 古代金色釣魚 = false; // 註解: 表示是否處於古代金色釣魚狀態，預設為假

    private void fishing() {
        if (_fishingList.size() > 0) { // 註解: 如果釣魚列表中有玩家
            long currentTime = System.currentTimeMillis(); // 註解: 獲取當前時間
            L1PcInstance pc = null; // 註解: 宣告玩家實例變數
            Iterator<L1PcInstance> iter = _fishingList.iterator(); // 註解: 獲取釣魚列表的迭代器
            while (iter.hasNext()) { // 註解: 遍歷釣魚列表
                pc = iter.next(); // 註解: 獲取下一個玩家實例
                if (pc == null) // 註解: 如果玩家實例為空
                    continue; // 註解: 跳過本次循環
                if (pc.getMapId() != 5490 || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0) // 註解: 如果玩家不在地圖 5490，或者玩家已死亡，或者玩家沒有網絡連接，或者玩家當前 HP 為 0
                    continue; // 註解: 跳過本次循環

                if (pc.isFishing()) { // 註解: 如果玩家正在釣魚
                    if (pc.getLevel() >= Config.CharSettings.MaxLevel) { // 註解: 如果玩家等級大於等於最大等級設定
                        endFishing(pc); // 註解: 結束釣魚狀態
                        pc.sendPackets("由於等級限制，無法繼續使用釣魚功能。"); // 註解: 發送訊息給玩家，告知因等級限制無法釣魚
                        continue; // 註解: 跳過本次循環，繼續下一次循環
                    }
                    long time = pc.getFishingTime(); // 註解: 獲取玩家的釣魚時間
                    if (currentTime > (time + 1000)) { // 註解: 如果當前時間大於釣魚時間加 1000 毫秒
                        // TODO 如果有餌
                        if (pc._fishingRod.getItemId() == 600229 || pc._fishingRod.getItemId() == 87058
                                || pc._fishingRod.getItemId() == 87059 || pc._fishingRod.getItemId() == 4100293
                                || pc.getInventory().consumeItem(41295, 1)) { // 註解: 如果釣竿 ID 為 600229、87058、87059 或 4100293，或者玩家消耗了一個 41295 ID 的物品
                            // TODO 配備高彈力釣竿
                            if (pc._fishingRod.getItemId() == 41294) { // 註解: 如果釣竿 ID 為 41294
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(41293, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 41293）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 80000); // 註解: 設置釣魚時間為當前時間加 80 秒
                                        pc.sendPackets(new S_釣魚(80)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 80 秒
                                        高彈力釣竿(pc); // 註解: 調用配備高彈力釣竿的函數
                                    }
                                }
                                // TODO 配備銀色釣竿
                            } else if (pc._fishingRod.getItemId() == 41305) { // 註解: 如果釣竿 ID 為 41305
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(41293, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 41293）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 80000); // 註解: 設置釣魚時間為當前時間加 80 秒
                                        pc.sendPackets(new S_釣魚(80)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 80 秒
                                        銀色釣竿(pc); // 註解: 調用配備銀色釣竿的函數
                                    }
                                }
                                // TODO 配備金色釣竿
                            } else if (pc._fishingRod.getItemId() == 41306) { // 註解: 如果釣竿 ID 為 41306
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(41293, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 41293）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 80000); // 註解: 設置釣魚時間為當前時間加 80 秒
                                        pc.sendPackets(new S_釣魚(80)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 80 秒
                                        金色釣竿(pc); // 註解: 調用配備金色釣竿的函數
                                    }
                                }
                                // TODO 黃金青蛙釣竿
                            } else if (pc._fishingRod.getItemId() == 9991) { // 註解: 如果釣竿 ID 為 9991
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(9993, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 9993，表示壞掉的釣竿）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的壞掉的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 80000); // 註解: 設置釣魚時間為當前時間加 80 秒
                                        pc.sendPackets(new S_釣魚(80)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 80 秒
                                        黃金青蛙釣竿(pc); // 註解: 調用黃金青蛙釣竿的函數
                                    }
                                }
                                // TODO 古代銀色釣竿
                            } else if (pc._fishingRod.getItemId() == 87058) { // 註解: 如果釣竿 ID 為 87058
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(41293, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 41293）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 60000); // 註解: 設置釣魚時間為當前時間加 60 秒
                                        pc.sendPackets(new S_釣魚(60)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 60 秒
                                        古代銀色釣魚 = true; // 註解: 設置古代銀色釣魚狀態為真
                                        Ancientsilver(pc); // 註解: 調用古代銀色釣竿函數
                                    }
                                }
                                // TODO 古代金色釣竿
                            } else if (pc._fishingRod.getItemId() == 87059) { // 註解: 如果釣竿 ID 為 87059
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(41293, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 41293）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 60000); // 註解: 設置釣魚時間為當前時間加 60 秒
                                        pc.sendPackets(new S_釣魚(60)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 60 秒
                                        古代金色釣魚 = true; // 註解: 設置古代金色釣魚狀態為真
                                        Ancientgold(pc); // 註解: 調用古代金色釣竿函數
                                    }
                                }
                                // TODO 成長的釣竿
                            } else if (pc._fishingRod.getItemId() == 600229) { // 註解: 如果釣竿 ID 為 600229
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(41293, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 41293）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 20000); // 註解: 設置釣魚時間為當前時間加 20 秒
                                        pc.sendPackets(new S_釣魚(20)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 20 秒
                                        成長釣魚 = true; // 註解: 設置成長釣魚狀態為真
                                        成長的釣竿(pc); // 註解: 調用成長的釣竿函數
                                    }
                                }
                                // TODO 高級成長的釣竿
                            } else if (pc._fishingRod.getItemId() == 4100293) { // 註解: 如果釣竿 ID 為 4100293
                                L1ItemInstance item = pc._fishingRod; // 註解: 獲取釣竿物品實例
                                if (item != null) { // 註解: 如果釣竿物品實例不為空
                                    if (item.getChargeCount() <= 0) { // 註解: 如果釣竿的充電次數小於等於 0
                                        L1ItemInstance newfishingRod = null; // 註解: 宣告新的釣竿變數
                                        pc.getInventory().removeItem(item, 1); // 註解: 從玩家的背包中移除釣竿
                                        newfishingRod = pc.getInventory().storeItem(41293, 1); // 註解: 給玩家背包中添加新的釣竿（ID 為 41293）
                                        pc._fishingRod = newfishingRod; // 註解: 設置玩家的釣竿為新的釣竿
                                        endFishing(pc); // 註解: 結束釣魚狀態
                                    } else {
                                        item.setChargeCount(item.getChargeCount() - 1); // 註解: 將釣竿的充電次數減 1
                                        pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT); // 註解: 更新玩家背包中的釣竿物品，屬性代碼為充電次數
                                        pc.setFishingTime(System.currentTimeMillis() + 20000); // 註解: 設置釣魚時間為當前時間加 20 秒
                                        pc.sendPackets(new S_釣魚(20)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 20 秒
                                        高級成長釣魚 = true; // 註解: 設置高級成長釣魚狀態為真
                                        高級成長的釣竿(pc); // 註解: 調用高級成長的釣竿函數
                                    }
                                }
                                // TODO 一般釣竿
                            } else if (pc._fishingRod.getItemId() == 41293) { // 註解: 如果釣竿 ID 為 41293
                                pc.setFishingTime(System.currentTimeMillis() + 240000); // 註解: 設置釣魚時間為當前時間加 240 秒
                                pc.sendPackets(new S_釣魚(240)); // 註解: 發送釣魚計時器開始消息給玩家，時間為 240 秒
                                高彈性釣竿(pc); // 註解: 調用高彈性釣竿函數
                            }
                        } else {
                            // 沒有餌料所以結束釣魚的區域。
                            endFishing(pc); // 註解: 結束釣魚狀態
                        }
                    }
                }
            }
        }
    }

    public void endFishing(L1PcInstance pc) {
        //new Throwable().printStackTrace(); // TODO 錯誤輸出異常測試用
        pc.setFishingTime(0); // 註解: 設置釣魚時間為 0
        pc.setFishingReady(false); // 註解: 設置釣魚準備狀態為否
        pc.setFishing(false); // 註解: 設置釣魚狀態為否
        pc._fishingRod = null; // 註解: 將釣竿設置為空

        if (成長釣魚) {
            成長釣魚 = false; // 註解: 如果成長釣魚為真，設置成長釣魚為假
        } else if (高級成長釣魚) {
            高級成長釣魚 = false; // 註解: 如果高級成長釣魚為真，設置高級成長釣魚為假
        } else if (古代銀色釣魚) {
            古代銀色釣魚 = false; // 註解: 如果古代銀色釣魚為真，設置古代銀色釣魚為假
        } else if (古代金色釣魚) {
            古代金色釣魚 = false; // 註解: 如果古代金色釣魚為真，設置古代金色釣魚為假
        }

        pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Fishing_etc)); // 註解: 發送結束釣魚圖示消息
        pc.sendPackets(new S_CharVisualUpdate(pc)); // 註解: 發送角色視覺更新消息
        Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc)); // 註解: 廣播角色視覺更新消息給其他玩家
        removeMember(pc); // 註解: 移除玩家作為成員
    }

    // TODO 古代銀色釣竿
    private void Ancientsilver(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 6000) { // 註解: 機率小於 6000，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8000) { // 註解: 機率介於 6000 到 7999 之間，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8040) { // 註解: 機率介於 8000 到 8039 之間，獲得 紅色貝里安娜
            successFishing(pc, 49092, "紅色貝里安娜"); // 註解: 釣魚成功，獲得物品 49092，消息 "紅色貝里安娜"
        } else if (chance < 8070) { // 註解: 機率介於 8040 到 8069 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8070 到 8349 之間，獲得 古代銀色釣竿
            successFishing(pc, 87058, "古代銀色釣竿"); // 註解: 釣魚成功，獲得物品 87058，消息 "古代銀色釣竿"
        } else if (chance < 8351) { // 註解: 機率介於 8350 到 8350 之間，獲得 小銀色貝里安娜
            successFishing(pc, 87056, "古代銀色捲線器 300 次"); // 註解: 釣魚成功，獲得物品 87056，消息 "古代銀色捲線器 300 次"
        } else if (chance < 8352) { // 註解: 機率介於 8351 到 8351 之間，獲得 小金色貝里安娜
            successFishing(pc, 41300, "$17523"); // 註解: 釣魚成功，獲得物品 41300，消息 "$17523"
        } else {
            pc.sendPackets(new S_ServerMessage(1136)); // 註解: 發送釣魚失敗消息，消息代碼 1136
            // 낚시에 실패했습니다。 // 註解: 釣魚失敗的消息
        }
    }

    // TODO 古代金色釣竿
    private void Ancientgold(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 6000) { // 註解: 機率小於 6000，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8000) { // 註解: 機率介於 6000 到 7999 之間，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8020) { // 註解: 機率介於 8000 到 8019 之間，獲得 紅色貝里安娜
            successFishing(pc, 49092, "紅色貝里安娜"); // 註解: 釣魚成功，獲得物品 49092，消息 "紅色貝里安娜"
        } else if (chance < 8050) { // 註解: 機率介於 8020 到 8049 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8050 到 8349 之間，獲得 古代金色釣竿
            successFishing(pc, 87059, "古代金色釣竿"); // 註解: 釣魚成功，獲得物品 87059，消息 "古代金色釣竿"
        } else if (chance < 8351) { // 註解: 機率介於 8350 到 8350 之間，獲得 古代金色捲線器 300 次
            successFishing(pc, 87057, "古代金色捲線器 300 次"); // 註解: 釣魚成功，獲得物品 87057，消息 "古代金色捲線器 300 次"
        } else if (chance < 8352) { // 註解: 機率介於 8351 到 8351 之間，獲得 小金色貝里安娜
            successFishing(pc, 41300, "$17523"); // 註解: 釣魚成功，獲得物品 41300，消息 "$17523"
        } else {
            pc.sendPackets(new S_ServerMessage(1136)); // 註解: 發送釣魚失敗消息，消息代碼 1136
            // 낚시에 실패했습니다。 // 註解: 釣魚失敗的消息
        }
    }

    private void 成長的釣竿(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 6000) { // 註解: 機率小於 6000，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8000) { // 註解: 機率介於 6000 到 7999 之間，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8020) { // 註解: 機率介於 8000 到 8019 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8020 到 8349 之間，獲得 純淨的藥水
            successFishing(pc, 820018, "$20462"); // 註解: 釣魚成功，獲得物品 820018，消息 "$20462"
        } else if (chance < 8360) { // 註解: 機率介於 8350 到 8359 之間，獲得 龍的寶箱
            successFishing(pc, 1000006, "$24608"); // 註解: 釣魚成功，獲得物品 1000006，消息 "$24608"
        } else if (chance < 8370) { // 註解: 機率介於 8360 到 8369 之間，獲得 艾恩哈薩德的禮物
            successFishing(pc, 600230, "$20909"); // 註解: 釣魚成功，獲得物品 600230，消息 "$20909"
        } else
            successFishing(pc, 41296, "$15564"); // 註解: 其餘情況下，獲得 貝里安娜
    }

    private void 高級成長釣竿(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 6000) { // 註解: 機率小於 6000，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8000) { // 註解: 機率介於 6000 到 7999 之間，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8020) { // 註解: 機率介於 8000 到 8019 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8020 到 8349 之間，獲得 純淨的藥水
            successFishing(pc, 820018, "$20462"); // 註解: 釣魚成功，獲得物品 820018，消息 "$20462"
        } else if (chance < 8360) { // 註解: 機率介於 8350 到 8359 之間，獲得 巨型貝里安娜
            successFishing(pc, 49093, "$20462"); // 註解: 釣魚成功，獲得物品 49093，消息 "$20462"
        } else if (chance < 8370) { // 註解: 機率介於 8360 到 8369 之間，獲得 龍的寶箱
            successFishing(pc, 1000006, "$24608"); // 註解: 釣魚成功，獲得物品 1000006，消息 "$24608"
        } else if (chance < 8390) { // 註解: 機率介於 8370 到 8389 之間，獲得 艾恩哈薩德的禮物

            successFishing(pc, 600230, "$20909"); // 註解: 釣魚成功，獲得物品 600230，消息 "$20909"
        } else
            successFishing(pc, 41296, "$15564"); // 註解: 其餘情況下，獲得 貝里安娜
    }

    private void 黃金青蛙釣竿(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 6000) { // 註解: 機率小於 6000，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8000) { // 註解: 機率介於 6000 到 7999 之間，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8020) { // 註解: 機率介於 8000 到 8019 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8020 到 8349 之間，獲得 濕濕的釣魚包
            successFishing(pc, 41301, "$15815"); // 註解: 釣魚成功，獲得物品 41301，消息 "$15815"
        } else if (chance < 8351) { // 註解: 機率介於 8350 到 8350 之間，獲得 小銀色貝里安娜
            successFishing(pc, 41299, "$17521"); // 註解: 釣魚成功，獲得物品 41299，消息 "$17521"
        } else if (chance < 8352) { // 註解: 機率介於 8351 到 8351 之間，獲得 小金色貝里安娜
            successFishing(pc, 41300, "$17523"); // 註解: 釣魚成功，獲得物品 41300，消息 "$17523"
        } else {
            pc.sendPackets(new S_ServerMessage(1136)); // 註解: 發送釣魚失敗消息，消息代碼 1136
            // 낚시에 실패했습니다。// 註解: 釣魚失敗的消息
        }
    }

    private void 配有捲線器的高彈力釣竿(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 6000) { // 註解: 機率小於 6000，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8000) { // 註解: 機率介於 6000 到 7999 之間，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8020) { // 註解: 機率介於 8000 到 8019 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8020 到 8349 之間，獲得 濕濕的釣魚包
            successFishing(pc, 41301, "$15815"); // 註解: 釣魚成功，獲得物品 41301，消息 "$15815"
        } else if (chance < 8351) { // 註解: 機率介於 8350 到 8350 之間，獲得 小銀色貝里安娜
            successFishing(pc, 41299, "$17521"); // 註解: 釣魚成功，獲得物品 41299，消息 "$17521"
        } else if (chance < 8352) { // 註解: 機率介於 8351 到 8351 之間，獲得 小金色貝里安娜
            successFishing(pc, 41300, "$17523"); // 註解: 釣魚成功，獲得物品 41300，消息 "$17523"
        } else {
            pc.sendPackets(new S_ServerMessage(1136)); // 註解: 發送釣魚失敗消息，消息代碼 1136
            // 낚시에 실패했습니다。// 註解: 釣魚失敗的消息
        }
    }

    private void 配有捲線器的銀色釣竿(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 4000) { // 註解: 機率小於 4000，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8000) { // 註解: 機率介於 4000 到 7999 之間，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8040) { // 註解: 機率介於 8000 到 8039 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8040 到 8349 之間，獲得 濕濕的釣魚包
            successFishing(pc, 41301, "$15815"); // 註解: 釣魚成功，獲得物品 41301，消息 "$15815"
        } else if (chance < 8352) { // 註解: 機率介於 8350 到 8351 之間，獲得 小銀色貝里安娜
            successFishing(pc, 41299, "$17521"); // 註解: 釣魚成功，獲得物品 41299，消息 "$17521"
        } else if (chance < 8353) { // 註解: 機率介於 8352 到 8352 之間，獲得 大銀色貝里安娜
            successFishing(pc, 41303, "$17522"); // 註解: 釣魚成功，獲得物品 41303，消息 "$17522"
        } else {
            pc.sendPackets(new S_ServerMessage(1136)); // 註解: 發送釣魚失敗消息，消息代碼 1136
            // 낚시에 실패했습니다。// 註解: 釣魚失敗的消息
        }
    }

    private void 配有捲線器的金色釣竿(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數

        if (chance < 3500) { // 註解: 機率小於 3500，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565"); // 註解: 釣魚成功，獲得物品 41297，消息 "$15565"
        } else if (chance < 8000) { // 註解: 機率介於 3500 到 7999 之間，獲得 貝里安娜
            successFishing(pc, 41296, "$15564"); // 註解: 釣魚成功，獲得物品 41296，消息 "$15564"
        } else if (chance < 8050) { // 註解: 機率介於 8000 到 8049 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566"); // 註解: 釣魚成功，獲得物品 41298，消息 "$15566"
        } else if (chance < 8350) { // 註解: 機率介於 8050 到 8349 之間，獲得 濕濕的釣魚包
            successFishing(pc, 41301, "$15815"); // 註解: 釣魚成功，獲得物品 41301，消息 "$15815"
        } else if (chance < 8352) { // 註解: 機率介於 8350 到 8351 之間，獲得 小金色貝里安娜
            successFishing(pc, 41300, "$17523"); // 註解: 釣魚成功，獲得物品 41300，消息 "$17523"
        } else if (chance < 8354) { // 註解: 機率介於 8352 到 8353 之間，獲得 大金色貝里安娜
            successFishing(pc, 41304, "$17524"); // 註解: 釣魚成功，獲得物品 41304，消息 "$17524"
        } else {

            pc.sendPackets(new S_ServerMessage(1136)); // 註解: 發送釣魚失敗消息，消息代碼 1136
            // 낚시에 실패했습니다。// 註解: 釣魚失敗的消息
        }
    }

    private void 高彈力釣竿(L1PcInstance pc) {
        int chance = _random.nextInt(10000) + 1; // 註解: 生成 1 到 10000 的隨機數，代表機率

        // 貝里安娜
        if (chance < 4000) { // 註解: 機率小於 4000，獲得 貝里安娜
            successFishing(pc, 41296, "$15564");

            // 藍色貝里安娜
        } else if (chance < 8000) { // 註解: 機率介於 4000 到 7999 之間，獲得 藍色貝里安娜
            successFishing(pc, 41297, "$15565");

            // 鸚鵡貝里安娜
        } else if (chance < 8010) { // 註解: 機率介於 8000 到 8009 之間，獲得 鸚鵡貝里安娜
            successFishing(pc, 41298, "$15566");

            // 濕濕的釣魚包
        } else if (chance < 8350) { // 註解: 機率介於 8010 到 8349 之間，獲得 濕濕的釣魚包
            successFishing(pc, 41301, "$15815");

        } else { // 註解: 其餘情況下，釣魚失敗
            pc.sendPackets(new S_ServerMessage(1136)); // 註解: 發送釣魚失敗消息，消息代碼 1136
            // 낚시에 실패했습니다。// 註解: 釣魚失敗的消息
        }

        // 註解: 發送釣魚成功消息，消息代碼 1147
        pc.sendPackets(new S_ServerMessage(1147));
    }

    private boolean check_weight(L1PcInstance pc) {
//		if (pc.getInventory().getSize() > (180 - 16)) {
        if (pc.getInventory().getSize() > (200 - 16)) {
            pc.sendPackets(new S_ServerMessage(263));
            return false;
        }
        return true;
    }

    private void distribute_message(int owner_object_id, String message, int effect_id) {
        ServerBasePacket[] pcks = new ServerBasePacket[] { new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message),
                new S_SkillSound(owner_object_id, effect_id), };
        L1World.getInstance().broadcast_map(5490, pcks);
    }

    private void distribute_message(int owner_object_id, int itemid) {
        switch (itemid) {
            case 41303: // 註解: 如果物品ID是 41303
                distribute_message(owner_object_id, "有人釣到了大銀色貝里安娜!", 13641); // 註解: 發送消息 "有人釣到了大銀色貝里安娜!"，消息代碼 13641
                break;
            case 41304: // 註解: 如果物品ID是 41304
                distribute_message(owner_object_id, "有人釣到了大金色貝里安娜!", 13639); // 註解: 發送消息 "有人釣到了大金色貝里安娜!"，消息代碼 13639
                break;
            case 49094: // 註解: 如果物品ID是 49094
                distribute_message(owner_object_id, "有人釣到了古代銀色貝里安娜!", 13641); // 註解: 發送消息 "有人釣到了古代銀色貝里安娜!"，消息代碼 13641
                break;
            case 49095: // 註解: 如果物品ID是 49095
                distribute_message(owner_object_id, "有人釣到了古代金色貝里安娜!", 13639); // 註解: 發送消息 "有人釣到了古代金色貝里安娜!"，消息代碼 13639
                break;
        }
    }

    private void calculate_exp(L1PcInstance pc, MJEFishingType f_type) {
        try {
            int level = Math.max(pc.getLevel(), 2);
            MJFishingExpInfo eInfo = MJFishingExpInfo.find_fishing_exp_info(f_type, level);
            if (eInfo == null)
                return;

            long need_exp = ExpTable.getNeedExpNextLevel(52);
            double exp = need_exp * eInfo.get_default_exp();
            int ain = pc.getAccount().getBlessOfAin();
            if (ain >= 10000) {
                double ain_effect = 1D;
                if (ain > 20000)
                    ain_effect += 0.3D;
                if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ) && ain >= 20000)
                    ain_effect += 0.8D;
                exp += (exp * ain_effect);
                pc.getAccount().addBlessOfAin(
                        -(int) SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp * eInfo.get_ain_ration()),
                        pc);
            }

//			if (pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
            if (pc.isPcBuff()) {
                exp *= 1.1;
            }

            exp = exp * eInfo.get_addition_exp();
            exp = Math.max(1, exp);

            // System.out.println("패널티 이전 경험치 : " + exp);
            /**
             * 레벨별 패널티
             */
            exp = exp - ((exp * (level * 0.01)) * 0.5);
            // System.out.println("패널티 부여후 경험치 : " + exp);

            if (pc.getLevel() >= GameServerSetting.getInstance().get_maxLevel()) {
                long maxexp = ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1);
                if (pc.get_exp() + exp >= maxexp)
                    return;
            }

            if ((exp + pc.get_exp()) > ExpTable.getExpByLevel((level + 1))) {
                exp = (ExpTable.getExpByLevel((level + 1)) - pc.get_exp());
            }
            pc.add_exp((long) exp);
            pc.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void successFishing(L1PcInstance pc, int itemid, String message) {
        if (!check_weight(pc)) // 註解: 如果檢查重量失敗，則返回
            return;

        L1ItemInstance item = pc.getInventory().storeItem(itemid, 1); // 註解: 存儲釣到的物品
        if (item != null) { // 註解: 如果物品不為空
            pc.sendPackets(new S_ServerMessage(1185, message)); // 註解: 發送釣魚成功的消息，消息代碼 1185
            // 낚시에 성공해%0%o를 낚시했습니다。// 註解: 釣魚成功消息
            ServerBasePacket pck = new S_SkillSound(pc.getId(), 763); // 註解: 創建釣魚成功的音效包，音效代碼 763
            pc.sendPackets(pck, false); // 註解: 發送音效包
            pc.broadcastPacket(pck); // 註解: 向其他玩家廣播音效包
        }
        distribute_message(pc.getId(), itemid); // 註解: 分發釣魚成功的消息
        if (成長型釣魚) { // 註解: 如果是成長型釣魚
            calculate_exp(pc, MJEFishingType.GROWN_UP); // 註解: 計算成長型釣魚的經驗值
        } else if (高級成長型釣魚) { // 註解: 如果是高級成長型釣魚
            calculate_exp(pc, MJEFishingType.HIGH_GROWN_UP); // 註解: 計算高級成長型釣魚的經驗值
        } else if (古代銀色釣魚) { // 註解: 如果是古代銀色釣魚
            calculate_exp(pc, MJEFishingType.ACIENT_SILVER); // 註解: 計算古代銀色釣魚的經驗值
        } else if (古代金色釣魚) { // 註解: 如果是古代金色釣魚
            calculate_exp(pc, MJEFishingType.ACIENT_GOLD); // 註解: 計算古代金色釣魚的經驗值
        }
    }
}
package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1DeleteGroundItem implements L1CommandExecutor {

    private L1DeleteGroundItem() {	}

    public static L1CommandExecutor getInstance() {
        return new L1DeleteGroundItem();
    }

    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        L1ItemInstance l1iteminstance = null; // 初始化物品實例變量

        try {
            StringTokenizer st = new StringTokenizer(arg);
            String type = st.nextToken(); // 解析參數，獲取命令類型

            if (type.equalsIgnoreCase("全部")) { // 如果命令類型是 "전체" (全部)
                L1Inventory groundInventory = null;
                LetterTable lettertable = null;
                L1FurnitureInstance furniture = null;

// 遍歷世界中的所有對象
                for (L1Object l1object : L1World.getInstance().getObject()) {
                    if (l1object instanceof L1ItemInstance) { // 如果對象是物品實例
                        l1iteminstance = (L1ItemInstance) l1object;

// 檢查物品是否在地面上，如果不是，則跳過
                        if (l1iteminstance.getX() == 0 && l1iteminstance.getY() == 0) {
                            continue;
                        }

// 獲取地面上的物品庫存
                        groundInventory = L1World.getInstance().getInventory(l1iteminstance.getX(), l1iteminstance.getY(), l1iteminstance.getMapId());
                        int itemId = l1iteminstance.getItem().getItemId();

// 處理特定的物品類型
                        if (itemId == 40314 || itemId == 40316) { // 寵物護身符
                            PetTable.getInstance().deletePet(l1iteminstance.getId());
                        } else if (itemId >= 49016 && itemId <= 49025) { // 信件
                            lettertable = new LetterTable();
                            lettertable.deleteLetter(l1iteminstance.getId());
                        } else if (itemId >= 41383 && itemId <= 41400) { // 家具
                            if (l1object instanceof L1FurnitureInstance) {
                                furniture = (L1FurnitureInstance) l1object;
                                if (furniture.getItemObjId() == l1iteminstance.getId()) { // 已經取出的家具
                                    FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
                                }
                            }
                        }

                        // 從地面庫存和世界中刪除物品
                        groundInventory.deleteItem(l1iteminstance);
                        L1World.getInstance().removeVisibleObject(l1iteminstance);
                        L1World.getInstance().removeObject(l1iteminstance);
                    }
                }

            } else if (type.equalsIgnoreCase("地圖")) { // 如果命令類型是 "맵" (地圖)
                L1Inventory groundInventory = null;
                LetterTable lettertable = null;
                L1FurnitureInstance furniture = null;

                // 遍歷世界中的所有對象
                for (L1Object l1object : L1World.getInstance().getObject()) {
                    if (l1object.getMapId() == pc.getMapId()) { // 檢查對象是否位於玩家當前地圖
                        if (l1object instanceof L1ItemInstance) { // 如果對象是物品實例
                            l1iteminstance = (L1ItemInstance) l1object;

                            // 檢查物品是否在地面上，如果不是，則跳過
                            if (l1iteminstance.getX() == 0 && l1iteminstance.getY() == 0) {
                                continue;
                            }

                            // 獲取地面上的物品庫存
                            groundInventory = L1World.getInstance().getInventory(l1iteminstance.getX(), l1iteminstance.getY(), l1iteminstance.getMapId());
                            int itemId = l1iteminstance.getItem().getItemId();

                            // 處理特定的物品類型
                            if (itemId == 40314 || itemId == 40316) { // 寵物護身符
                                PetTable.getInstance().deletePet(l1iteminstance.getId());
                            } else if (itemId >= 49016 && itemId <= 49025) { // 信件
                                lettertable = new LetterTable();
                                lettertable.deleteLetter(l1iteminstance.getId());
                            } else if (itemId >= 41383 && itemId <= 41400) { // 家具
                                if (l1object instanceof L1FurnitureInstance) {
                                    furniture = (L1FurnitureInstance) l1object;
                                    if (furniture.getItemObjId() == l1iteminstance.getId()) { // 已經取出的家具
                                        FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
                                    }
                                }
                            }

                            // 從地面庫存和世界中刪除物品
                            groundInventory.deleteItem(l1iteminstance);
                            L1World.getInstance().removeVisibleObject(l1iteminstance);
                            L1World.getInstance().removeObject(l1iteminstance);
                        }
                    }
                }
            } else {
                pc.sendPackets((ServerBasePacket)new S_SystemMessage("清掃 [地圖, 全部]"));
            }
        } catch(Exception e){
            // 捕捉任何異常並向玩家發送錯誤訊息，提示正確的命令格式
                pc.sendPackets(new S_SystemMessage(".命令類型 請輸入正確的參數。"));
            }
        }

    }
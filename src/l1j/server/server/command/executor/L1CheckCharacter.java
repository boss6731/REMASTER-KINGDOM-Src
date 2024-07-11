package l1j.server.server.command.executor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvService;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1CheckCharacter implements L1CommandExecutor {
    public static L1CommandExecutor getInstance() {
        return new L1CheckCharacter();
    }

    private CharacterCheckerModel characterModel(final String characterName) {
        final CharacterCheckerModel model = new CharacterCheckerModel();
        Selector.exec("SELECT objid, account_name FROM characters WHERE char_name=? limit 1", new SelectorHandler() {
            public void handle(PreparedStatement pstm) throws Exception {
                pstm.setString(1, characterName);
            }

            public void result(ResultSet rs) throws Exception {
                if (rs.next()) {
                    model.objectId = rs.getInt("objid");
                    model.accountName = rs.getString("account_name");
                }
            }
        });
        return model;
    }

    private static class CharacterCheckerModel {
        public int objectId;

        public String accountName;

        private CharacterCheckerModel() {
        }
    }

    private static class MJMyCharInvItemCompare implements Comparator<MJMyCharInvItemInfo> {
        private MJMyCharInvItemCompare() {
        }

        public int compare(MJMyCharInvItemInfo o1, MJMyCharInvItemInfo o2) {
            if (o1.equipped)
                return -1;
            if (o2.equipped)
                return 1;
            if (o1.enchantLevel > o2.enchantLevel)
                return -1;
            if (o2.enchantLevel > o1.enchantLevel)
                return 1;
            if (o1.itemId < o2.itemId)
                return -1;
            if (o2.itemId < o1.itemId)
                return 1;
            return 0;
        }
    }
    private void onCheckedCharacter(L1PcInstance gm, String characterName, String type) {
    // 獲取角色模型
        CharacterCheckerModel model = characterModel(characterName);

        // 如果角色不存在，發送錯誤訊息並返回
        if (model.objectId <= 0) {
            gm.sendPackets(String.format("\fW** [%s]是不存在的角色。 **", characterName));
            return;
        }

        // 發送檢查角色信息
        gm.sendPackets(String.format("\fW** 檢查: %s 角色: %s **", type, characterName));

        try {
        // 定義物品列表
            List<MJMyCharInvItemInfo> items;

            // 根據檢查類型，獲取對應的物品信息
            switch (type) {
                case "背包":
                    items = MJMyCharInvService.service().allInventoryItems(model.objectId);
                    onDisplayCheckedCharacter(gm, characterName, type, items);
                    return;
                case "倉庫":
                    items = MJMyCharInvService.service().accountWarehouseItems(model.accountName);
                    onDisplayCheckedCharacter(gm, characterName, type, items);
                    return;
                case "裝備":
                    items = MJMyCharInvService.service().equippedItems(model.objectId);
                    onDisplayCheckedCharacter(gm, characterName, type, items);
                    return;
                case "精靈倉庫":
                    items = MJMyCharInvService.service().elfWarehouseItems(model.accountName);
                    onDisplayCheckedCharacter(gm, characterName, type, items);
                    return;
                case "附加倉庫":
                    items = MJMyCharInvService.service().characterWarehouseItems(model.accountName);
                    onDisplayCheckedCharacter(gm, characterName, type, items);
                    return;
            }

            // 如果type不匹配任何情況，拋出異常
            throw new Exception();

        } catch (Exception e) {
        // 捕捉異常並發送錯誤訊息
            gm.sendPackets(".檢查 [角色名] [裝備,背包,倉庫,精靈倉庫]");
        }
    }

    private void onDisplayCheckedCharacter(L1PcInstance gm, String characterName, String type, List<MJMyCharInvItemInfo> items) {
        int number = 0;
        // 將物品列表進行排序
        items.sort(new MJMyCharInvItemCompare());
        // 遍歷每個物品信息
        for (MJMyCharInvItemInfo iInfo : items) {
            gm.sendPackets(String.format("\fU%d. %s", ++number, iInfo.display)); // 向管理員發送物品信息
        }
        // 向管理員發送總計物品數量信息
        gm.sendPackets(String.format("\fW** 總共 %d 件物品[%s]已被檢索到 **", items.size(), type));
    }

    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg); // 使用 StringTokenizer 解析參數
            String characterName = st.nextToken(); // 獲取角色名稱
            String type = st.nextToken(); // 獲取檢查類型
            onCheckedCharacter(pc, characterName, type); // 調用 onCheckedCharacter 方法檢查角色
        } catch (Exception e) {
            // 捕捉異常並向玩家發送錯誤訊息，提示正確的命令格式
            pc.sendPackets(".檢查 [角色名] [裝備,背包,倉庫,精靈倉庫,附加倉庫]");
        }
    }

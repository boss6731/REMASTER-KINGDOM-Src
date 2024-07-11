package l1j.server.MJ3SEx;

import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Character;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * SpriteInformation
 * 由 mjsoft 於 2017 年製作。
 * 精靈資訊
 **/
public class SpriteInformation {
    // 創建 SpriteInformation 實例的工廠方法
    public static SpriteInformation create(ResultSet rs) throws SQLException {
        SpriteInformation info = new SpriteInformation();
        // 從 ResultSet 中讀取資訊並初始化 SpriteInformation 實例
        info.sprId = rs.getInt("spr_id");
        info.type = rs.getInt("type");
        info.width = rs.getInt("width");
        info.height = rs.getInt("height");
        info.numOfAction = rs.getInt("action_count");
        info.initialize();
        return info;
    }

    // 成員變數
    private int sprId; // 精靈ID
    private int width; // 寬度
    private int height; // 高度
    private int numOfAction; // 動作數量
    private int type; // 精靈類型

    private HashMap<Integer, ActionInformation> actions; // 動作信息映射

    // 私有構造函數
    private SpriteInformation() {}

    // 初始化方法
    private void initialize() {
        actions = new HashMap<Integer, ActionInformation>(numOfAction);
    }

    // 獲取精靈類型
    public int getSpritType() {
        return type;
    }

    // 獲取精靈ID
    public int getSpriteId() {
        return sprId;
    }

    // 獲取寬度
    public int getWidth() {
        return width;
    }

    // 獲取高度
    public int getHeight() {
        return height;
    }

    // 獲取動作數量
    public int getNumOfAction() {
        return numOfAction;
    }

    // 將動作信息放入映射中
    public void put(ActionInformation aInfo) {
        actions.put(aInfo.getActionId(), aInfo);
    }

    // 根據動作ID獲取動作信息
    public ActionInformation get(int actId) {
        return actions.get(actId);
    }

    // 註冊用戶動作
    public void registerUserActions(int actId, Double[] rates) {
        ActionInformation aInfo = get(actId);
        if (aInfo == null) {
            aInfo = ActionInformation.fromBasicAction(actId);
            put(aInfo);
        }
        aInfo.setUserFrames(rates);
    }

    // 釋放資源
    public void dispose() {
        if (actions != null) {
            actions.clear();
            actions = null;
        }
    }
    
    // 獲取動作間隔
    public double getInterval(L1Character c, int actionCode) {
        return getInterval(c, EActionCodes.fromInt(actionCode));
    }
    
    // 獲取動作間隔
    public double getInterval(L1Character c, EActionCodes actionCode) {
        // 根據特定 sprId 修改動作代碼
        // 如果 sprId 符合指定的值，則修改 actionCode
        // 這裡的 sprId 是一個數字，用於識別不同的精靈
        // 修改後的 actionCode 用於確保正確的動作間隔計算
        if (sprId == 3479 || sprId == 3497 || sprId == 3498 || sprId == 3499 || sprId == 3500 || sprId == 3479
                || sprId == 3501 || sprId == 3502 || sprId == 3503 || sprId == 3504 || sprId == 3480 || sprId == 3505
                || sprId == 3506 || sprId == 3507 || sprId == 3508 || sprId == 3481 || sprId == 3509 || sprId == 3510
                || sprId == 3511 || sprId == 3512) {
            if (actionCode.is_walk()) {
                actionCode = EActionCodes.walk;
            } else if (actionCode.is_attack()) {
                actionCode = EActionCodes.attack;
            } else if (actionCode.is_damage()) {
                actionCode = EActionCodes.damage;
            } else if (actionCode.is_breath()) {
                actionCode = EActionCodes.breath;
            } else if (actionCode.is_alt_attack()) {
                actionCode = EActionCodes.alt_attack;
            }
        }
        ActionInformation aInfo = get(actionCode.toInt());
        if (aInfo == null) {
            aInfo = ActionInformation.fromBasicAction(actionCode);
            put(aInfo);
        }
        return actionCode.decoration(c, aInfo.getFramePerSecond().doubleValue());        
    }

    // 獲取動作間隔（玩家角色）
    public double getInterval(L1PcInstance pc, int actionCode) {
        return getInterval(pc, EActionCodes.fromInt(actionCode));
    }
    
    // 獲取動作間隔（玩家角色）
    public double getInterval(L1PcInstance pc, EActionCodes actionCode) {
        // 根據特定 sprId 修改動作代碼
        if (sprId == 12015 || sprId == 5641 || sprId == 11685) {
            if (actionCode.is_walk()) {
                actionCode = EActionCodes.walk;
            } else if (actionCode.is_attack()) {
                actionCode = EActionCodes.attack;
            } else if (actionCode.is_damage()) {
                actionCode = EActionCodes.damage;
            } else if (actionCode.is_breath()) {
                actionCode = EActionCodes.breath;
            }
        }
        
        // 如果玩家處於槍模式，修改動作代碼
        if (pc.isSpearModeType()) {
            if (actionCode == EActionCodes.walk_spear) {
                actionCode = EActionCodes.walk_spear_throw;
            } else if (actionCode == EActionCodes.attack_spear) {
                actionCode = EActionCodes.attack_spear_throw;
            } else if (actionCode == EActionCodes.damage_spear) {
                actionCode = EActionCodes.damage_spear_throw;
            } else if (actionCode == EActionCodes.breath_spear) {
                actionCode = EActionCodes.breath_spear_throw;
            }
        }

        ActionInformation aInfo = get(actionCode.toInt());
        if (aInfo == null) {
            aInfo = ActionInformation.fromBasicAction(actionCode);
            put(aInfo);
        }
        
        return actionCode.decorationForPc(pc, aInfo.getFramePerSecond(SpriteInformationLoader.levelToIndex(pc.getLevel(), pc.getCurrentSpriteId())).doubleValue());    
    }
}

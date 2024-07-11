package l1j.server.MJ3SEx;

import l1j.server.MJTemplate.Frame.MJFrameElement;

import java.sql.ResultSet;
import java.sql.SQLException;
/** 
 * ActionInformation
 * 由 mjsoft 於 2017 年製作。
 **/
public class ActionInformation {
    // 從基本動作獲取ActionInformation對象
    public static ActionInformation fromBasicAction(int actId) {
        return fromBasicAction(EActionCodes.fromInt(actId));
    }

    public static ActionInformation fromBasicAction(EActionCodes actionCode) {
        return create(actionCode.toInt(), actionCode.getAmount(), 24D);
    }

    // 根據ResultSet創建ActionInformation對象
    public static ActionInformation create(ResultSet rs) throws SQLException {
        return create(rs.getInt("act_id"), rs.getDouble("framecount"), rs.getDouble("framerate"));
    }

    // 創建ActionInformation對象
    public static ActionInformation create(int actId, double framecount, double framerate) {

        return create(actId, MJFrameElement.fromRate(framecount, framerate));
    }

    public static ActionInformation create(int actId, MJFrameElement frame) {
        ActionInformation info = new ActionInformation();
        info.actId = actId;
        info.frame = frame;
        return info;
    }

    private int actId;
    private MJFrameElement frame;
    private Double[] userFrames;
    private ActionInformation() {
    }

    // 獲取動作ID
    public int getActionId() {
        return actId;
    }

    // 是否有自定義幀數
    public boolean isUserFrames() {
        return userFrames != null;
    }

    // 設置自定義幀數
    public void setUserFrames(Double[] frames) {
        userFrames = frames;
    }

    // 獲取MJFrameElement對象
    public MJFrameElement getFrame() {
        return frame;
    }

    // 獲取每秒幀數
    public Double getFramePerSecond() {
        return frame.getFramePerSecond();
    }

    public Double getFramePerSecond(double rps) {
        return frame.getFramePerSecond(rps);
    }

    public Double getFramePerSecond(int idx) {
        return userFrames == null ? getFramePerSecond() : frame.getFramePerSecond(userFrames[idx].doubleValue());
    }
}

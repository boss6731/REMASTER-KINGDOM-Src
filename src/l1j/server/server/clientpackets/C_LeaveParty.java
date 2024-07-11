package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_LeaveParty extends ClientBasePacket {
    private static final String C_LEAVE_PARTY = "[C] C_LeaveParty";

    // 構造方法，處理玩家離開隊伍的請求
    public C_LeaveParty(byte[] decrypt, GameClient client) throws Exception {
        super(decrypt);
        L1PcInstance player = client.getActiveChar();
        if (player == null) {
            return; // 如果無法獲取活動角色，直接返回
        }

        // 檢查玩家是否在特定地圖中，並且不能在該地圖中離開隊伍
        if (!player.is_world()) {
            player.sendPackets("在此地圖無法離開隊伍。");
            return; // 如果玩家在特定地圖中，直接返回
        }

        // 檢查玩家是否在隊伍中
        if (player.isInParty()) {
        // 如果玩家在隊伍中，將玩家從隊伍中移除
            player.getParty().leaveMember(player);
        }
    }

    // 返回類型字符串
    public String getType() {
        return "[C] C_LeaveParty";
    }
}
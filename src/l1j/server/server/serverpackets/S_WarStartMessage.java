package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;

public class S_WarStartMessage extends ServerBasePacket {

	// 定義城堡名稱的陣列
	private static final String[] clanNames = new String[]{"肯特城堡", "獸人堡壘", "溫達烏德城堡", "吉蘭城堡", "海尼城堡", "地下城堡", "亞丁城堡"};

	// 獲取戰爭開始訊息的實例
	public static S_WarStartMessage get() {
		S_WarStartMessage p = new S_WarStartMessage();
		// 遍歷所有的血盟，並根據城堡 ID 更新城堡名稱
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getCastleId() - 1 > 0 && clan.getCastleId() - 1 < clanNames.length)
				clanNames[clan.getCastleId() - 1] = clan.getClanName();
		}

		// 將城堡名稱寫入封包
		for (int i = 0; i < clanNames.length; i++)
			p.writeS(clanNames[i]);

		return p;
	}

	// 私有構造函數，初始化封包
	private S_WarStartMessage() {
		writeC(Opcodes.S_EVENT); // 寫入操作碼
		writeC(0x4E); // 寫入事件類型
		writeC(0x07); // 寫入事件參數
	}
}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return "S_WarStartMessage";
	}
}



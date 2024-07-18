package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Strup extends ServerBasePacket {

	private static final String _S__25_S_Strup = "[S] S_Strup";

	// 構造函數，接受玩家對象、類型和時間參數
	public S_Strup(L1PcInstance pc, int type, int time) {
		writeC(Opcodes.S_MAGE_STRENGTH); // 寫入操作碼，表示增強力量
		writeH(time); // 寫入時間參數
		writeC(pc.getAbility().getTotalStr()); // 寫入玩家的總力量屬性
		writeC(pc.getInventory().getWeight100()); // 寫入玩家的負重百分比
		writeC(0); // 額外標記，設置為 0
		writeD(type); // 寫入類型參數
	}

	// 實現父類的 getContent 方法，返回封包的字節數組
	@override
	public byte[] getContent() {
		return getBytes();
	}

	// 實現父類的 getType 方法，返回封包的類型
	@override
	public String getType() {
		return _S__25_S_Strup;
	}
}



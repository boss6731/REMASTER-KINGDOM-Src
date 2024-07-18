package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

import l1j.server.server.Opcodes;

public class S_SPMR extends ServerBasePacket {

	private static final String S_SPMR = "[S] S_S_SPMR";

	// 構造函數，接受 L1PcInstance 對象
	public S_SPMR(L1PcInstance pc) {
		buildPacket(pc); // 構建封包
	}

	// 構建封包的私有方法
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_MAGIC_STATUS); // 寫入操作碼，表示魔法狀態

		// 寫入玩家的 SP（技能點）數據
		writeH(pc.getAbility().getSp() - pc.getAbility().getTrueSp()); // 220906 變更

		// 檢查玩家的抗性，如果抗性為空則寫入 0，否則寫入計算後的 MR（魔法抗性）
		if (pc.getResistance() == null) {
			writeH(0);
		} else {
			writeH(pc.getResistance().getMr() - pc.getResistance().getBaseMr() - pc.getResistance().getLevelMr());
		}
	}

	// 實現父類的 getContent 方法，返回封包的字節數組
	@override
	public byte[] getContent() {
		return getBytes();
	}

	// 實現父類的 getType 方法，返回封包的類型
	@override
	public String getType() {
		return S_SPMR;
	}
}



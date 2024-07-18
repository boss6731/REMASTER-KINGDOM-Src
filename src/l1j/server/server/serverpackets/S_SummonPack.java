package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

// 封包類，用於處理召喚物的數據封包
public class S_SummonPack extends ServerBasePacket {



	private static final String _S__1F_SUMMONPACK = "[S] S_SummonPack";


	private static final int STATUS_POISON = 1; // 中毒狀態常量

	// 構造函數，接受召喚物實例和玩家實例
	public S_SummonPack(L1SummonInstance pet, L1PcInstance pc) {
		buildPacket(pet, pc, true); // 構建封包數據，並檢查主人
	}

	// 構造函數，接受召喚物實例、玩家實例和是否檢查主人的標記
	public S_SummonPack(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
		buildPacket(pet, pc, isCheckMaster); // 構建封包數據，根據標記決定是否檢查主人
	}

	// 私有方法，用於構建封包數據
	private void buildPacket(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
		writeC(Opcodes.S_PUT_OBJECT); // 寫入操作碼，表示放置對象
		writeH(pet.getX()); // 寫入召喚獸的X坐標
		writeH(pet.getY()); // 寫入召喚獸的Y坐標
		writeD(pet.getId()); // 寫入召喚獸的ID
		writeH(pet.getCurrentSpriteId()); // 寫入召喚獸在List.spr中的SpriteID
		writeC(pet.getStatus()); // 寫入召喚獸在List.spr中的模式
		writeC(pet.getHeading()); // 寫入召喚獸的面向
		writeC(pet.getLight().getChaLightSize()); // 寫入召喚獸的亮度（明亮度），範圍是0~15
		writeC(pet.getMoveSpeed()); // 寫入召喚獸的移動速度 - 0:正常, 1:快速, 2:慢速
		writeD(0); // 未知的數據，寫入默認值
		writeH(0); // 未知的數據，寫入默認值
		writeS(pet.getNameId()); // 寫入召喚獸的名稱ID
		writeS(pet.getTitle()); // 寫入召喚獸的稱號
		int status = 0;
		// 檢查召喚獸是否中毒
		if (pet.getPoison() != null && pet.getPoison().getEffectId() == 1) {
			status |= STATUS_POISON; // 如果中毒，添加中毒狀態
		}
		writeC(status); // 寫入召喚獸的狀態
		writeD(0); // 未知的數據，寫入默認值
		writeS(null); // 寫入null值，代表無額外信息
		// 檢查是否需要檢查召喚獸的主人
		if (isCheckMaster && pet.isExsistMaster()) {
			writeS(pet.getMaster().getName()); // 如果需要，寫入主人的名字
		} else {
			writeS(""); // 如果不需要，寫入空字符串
		}
		writeC(0); // 未知的數據，寫入默認值

		// 寫入召喚獸的HP百分比
		if (pet.getMaster() != null && pet.getMaster().getId() == pc.getId()) {
			int percent = pet.getMaxHp() != 0 ? 100 * pet.getCurrentHp() / pet.getMaxHp() : 100;
			writeC(percent);
		} else {
			writeC(0xFF); // 如果不是主人，或者HP最大值為0，寫入0xFF
		}
		writeC(0); // 未知的數據，寫入默認值
		writeC(pet.getLevel()); // 寫入召喚獸的等級，PC為0, Monster為等級
		writeC(0); // 未知的數據，寫入默認值
		writeC(0xFF); // 未知的數據，寫入0xFF
		writeC(0xFF); // 未知的數據，寫入0xFF
		writeC(0); // 未知的數據，寫入默認值
		writeC(0); // 未知的數據，寫入默認值

		// 寫入召喚獸的MP百分比
		if (pet.getMaster() != null && pet.getMaster().getId() == pc.getId()) {
			int percent = pet.getMaxMp() != 0 ? 100 * pet.getCurrentMp() / pet.getMaxMp() : 100;
			writeC(percent);
		} else {
			writeC(0xFF); // 如果不是主人，或者MP最大值為0，寫入0xFF
		}
		writeH(0); // 未知的數據，寫入默認值
	}

	// 實現父類的 getContent 方法，返回封包的字節數組
	@override
	public byte[] getContent() {
		return getBytes();
	}

	// 實現父類的 getType 方法，返回封包的類型
	@override
	public String getType() {
		return _S__1F_SUMMONPACK;
	}
}



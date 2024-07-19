package l1j.server.Beginner.View;

import java.util.List;

import l1j.server.Beginner.MJBeginnerService;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerViewProvider {

	// 靜態實例，用於單例模式
	private static final MJBeginnerViewProvider provider = new MJBeginnerViewProvider();

	// 提供靜態方法以獲取單例實例
	public static MJBeginnerViewProvider provider() {
		return provider;
	}

	// 私有成員變量，用於存儲開發模式狀態
	private boolean developMode;

	// 私有構造函數，防止外部實例化
	private MJBeginnerViewProvider() {
		developMode = false;
	}

	// 初始化方法，目前未實現具體邏輯
	public void initialize(MJBeginnerService service) {
	// 初始化邏輯，如果有的話
	}

	// 當開發模式改變時執行
	public void onDevelopModeChanged(boolean developMode) {
		this.developMode = developMode;
	}

	// 根據開發模式創建新的揭示視圖
	public MJBeginnerRevealView newRevealView(L1PcInstance pc, int questId) {
		return developMode ? new MJBeginnerRevealView.MJBeginnerRevealDevelopView(pc, questId) : new MJBeginnerRevealView(pc, questId);
	}

	// 根據開發模式創建新的進度視圖
	public MJBeginnerProgressView newProgressView(L1PcInstance pc) {
		return developMode ? new MJBeginnerProgressView.MJBeginnerProgressDevelopView(pc) : new MJBeginnerProgressView(pc);
	}

	// 根據開發模式創建新的啟動視圖
	public MJBeginnerStartView newStartView(L1PcInstance pc, int questId) {
		return developMode ? new MJBeginnerStartView.MJBeginnerStartDevelopView(pc, questId) : new MJBeginnerStartView(pc, questId);
	}

	// 根據開發模式創建新的完成視圖
	public MJBeginnerFinishedView newFinishedView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
		return developMode ? new MJBeginnerFinishedView.MJBeginnerFinishedDevelopView(pc, questId, optionalRewardIndexes) : new MJBeginnerFinishedView(pc, questId, optionalRewardIndexes);
	}

	// 根據開發模式創建新的傳送視圖
	public MJBeginnerTeleportView newTeleportView(L1PcInstance pc, int questId) {
		return developMode ? new MJBeginnerTeleportView.MJBeginnerTeleportDevelopView(pc, questId) : new MJBeginnerTeleportView(pc, questId);
	}

	// 添加 closing 大括號
}

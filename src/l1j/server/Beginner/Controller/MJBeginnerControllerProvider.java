package l1j.server.Beginner.Controller;

import l1j.server.Beginner.MJBeginnerService;

public class MJBeginnerControllerProvider {

	// 靜態實例，用於單例模式
	private static final MJBeginnerControllerProvider provider = new MJBeginnerControllerProvider();

	// 提供靜態方法以獲取單例實例
	public static MJBeginnerControllerProvider provider() {
		return provider;
	}

	// 私有成員變量，用於存儲客戶端控制器
	private MJBeginnerClientController clientController;

	// 公有構造函數
	public MJBeginnerControllerProvider() {
	}

	// 獲取客戶端控制器
	public MJBeginnerClientController clientController() {
		return clientController;
	}

	// 初始化方法，目前未實現具體邏輯
	public void initialize(MJBeginnerService service) {
	}

	// 當開發模式改變時執行
	public void onDevelopModeChanged(boolean developMode) {
		clientController = developMode ? new MJBeginnerClientController.MJBeginnerDeveloperController() : new MJBeginnerClientController();
	}
}

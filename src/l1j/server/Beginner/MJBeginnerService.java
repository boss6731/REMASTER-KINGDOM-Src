package l1j.server.Beginner;

import l1j.server.Beginner.Controller.MJBeginnerControllerProvider;
import l1j.server.Beginner.Model.MJBeginnerModelProvider;
import l1j.server.Beginner.View.MJBeginnerViewProvider;

public class MJBeginnerService {

	// 靜態實例，用於單例模式
	private static final MJBeginnerService service = new MJBeginnerService();

	// 提供靜態方法以獲取單例實例
	public static MJBeginnerService service() {
		return service;
	}

	// 應用啟動時執行的方法
	public static void onApplicationStartup() {
		service.initialize();
	}

	// 私有構造函數，防止外部實例化
	private MJBeginnerService() {
	}

	// 初始化方法
	private void initialize() {
		MJBeginnerModelProvider.provider().initialize(this);
		MJBeginnerControllerProvider.provider().initialize(this);
		MJBeginnerViewProvider.provider().initialize(this);
	}
}

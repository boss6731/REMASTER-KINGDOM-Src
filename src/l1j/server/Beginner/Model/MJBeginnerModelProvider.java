package l1j.server.Beginner.Model;

import l1j.server.Beginner.MJBeginnerService;
import l1j.server.Beginner.View.MJBeginnerFinishedView;
import l1j.server.Beginner.View.MJBeginnerProgressView;
import l1j.server.Beginner.View.MJBeginnerRevealView;
import l1j.server.Beginner.View.MJBeginnerStartView;
import l1j.server.Beginner.View.MJBeginnerTeleportView;
import l1j.server.MJTemplate.Command.MJCommandService;

public class MJBeginnerModelProvider {
	// 定義模型文件的路徑
	private static final String settingModelPath = "./data/beginner/mj-beginner-setting.json";
	private static final String collectModelPath = "./data/beginner/mj-beginner-collect.json";
	private static final String questModelPath = "./data/beginner/mj-beginner-model.json";
	private static final String subEventModelPath = "./data/beginner/mj-beginner-sub-event.json";

	// 創建一個靜態的MJBeginnerModelProvider實例
	private static final MJBeginnerModelProvider provider = new MJBeginnerModelProvider();

	// 獲取MJBeginnerModelProvider實例的方法
	public static final MJBeginnerModelProvider provider() {
		return provider;
	}

	// 定義各種模型的私有實例變量
	private MJBeginnerSettingModel settingModel;
	private MJBeginnerQuestDataModel questDataModel;
	private MJBeginnerCollectModel collectDataModel;
	private MJBeginnerSubEventModel subEventModel;

	// 私有構造函數，防止外部創建實例
	private MJBeginnerModelProvider() {
	}


	public void initialize(MJBeginnerService service) {
		// 初始化各種模型，傳入對應的文件路徑
		settingModel = new MJBeginnerSettingModel(settingModelPath);
		questDataModel = new MJBeginnerQuestDataModel(questModelPath);
		collectDataModel = new MJBeginnerCollectModel(collectModelPath);
		subEventModel = new MJBeginnerSubEventModel(subEventModelPath);
		// 添加初學者命令模型到GM服務中
		MJCommandService.gmService().append(new MJBeginnerCommandModel().createCommand());
	}

	// 獲取設置模型
	MJBeginnerSettingModel settingModel() {
		return settingModel;
	}

	// 獲取任務數據模型
	MJBeginnerQuestDataModel questDataModel() {

		return questDataModel;
	}

	// 獲取收集數據模型
	MJBeginnerCollectModel collectDataModel() {
		return collectDataModel;
	}

	// 獲取子事件數據模型
	MJBeginnerSubEventModel subEventModel() {
		return subEventModel;
	}

	// 獲取進度模型
	public MJBeginnerModel<MJBeginnerProgressView> progressModel() {
		return settingModel().model().modeHandler().progressModel(this);
	}

	// 獲取揭示模型
	public MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId) {
		return settingModel().model().modeHandler().revealModel(questId, this);
	}

	// 獲取開始模型
	public MJBeginnerModel<MJBeginnerStartView> startModel(int questId) {
		return settingModel().model().modeHandler().startModel(questId, this);
	}

	// 獲取完成模型
	public MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId) {
		return settingModel().model().modeHandler().finishedModel(questId, this);
	}

	// 獲取傳送模型
	public MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId) {
		return settingModel().model().modeHandler().teleportModel(questId, this);
	}

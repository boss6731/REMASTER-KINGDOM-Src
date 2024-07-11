package l1j.server.Beginner.Model;

import l1j.server.Beginner.MJBeginnerService;
import l1j.server.Beginner.View.MJBeginnerFinishedView;
import l1j.server.Beginner.View.MJBeginnerProgressView;
import l1j.server.Beginner.View.MJBeginnerRevealView;
import l1j.server.Beginner.View.MJBeginnerStartView;
import l1j.server.Beginner.View.MJBeginnerTeleportView;
import l1j.server.MJTemplate.Command.MJCommandService;

public class MJBeginnerModelProvider{
	private static final String settingModelPath = "./data/beginner/mj-beginner-setting.json";
	private static final String collectModelPath = "./data/beginner/mj-beginner-collect.json";
	private static final String questModelPath = "./data/beginner/mj-beginner-model.json";
	private static final String subEventModelPath = "./data/beginner/mj-beginner-sub-event.json";
	private static final MJBeginnerModelProvider provider = new MJBeginnerModelProvider();
	public static final MJBeginnerModelProvider provider(){
		return provider;
	}
	
	private MJBeginnerSettingModel settingModel;
	private MJBeginnerQuestDataModel questDataModel;
	private MJBeginnerCollectModel collectDataModel;
	private MJBeginnerSubEventModel subEventModel;
	private MJBeginnerModelProvider(){
	}
	
	public void initialize(MJBeginnerService service){
		settingModel = new MJBeginnerSettingModel(settingModelPath);
		questDataModel = new MJBeginnerQuestDataModel(questModelPath);
		collectDataModel = new MJBeginnerCollectModel(collectModelPath);
		subEventModel = new MJBeginnerSubEventModel(subEventModelPath);
		MJCommandService.gmService().append(new MJBeginnerCommandModel().createCommand());
	}
	
	MJBeginnerSettingModel settingModel(){
		return settingModel;
	}
	
	MJBeginnerQuestDataModel questDataModel(){
		return questDataModel;
	}
	
	MJBeginnerCollectModel collectDataModel(){
		return collectDataModel;
	}
	
	MJBeginnerSubEventModel subEventModel(){
		return subEventModel;
	}
	
	public MJBeginnerModel<MJBeginnerProgressView> progressModel(){
		return settingModel().model().modeHandler().progressModel(this);
	}
	
	public MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId){
		return settingModel().model().modeHandler().revealModel(questId, this);
	}
	
	public MJBeginnerModel<MJBeginnerStartView> startModel(int questId){
		return settingModel().model().modeHandler().startModel(questId, this);
	}
	
	public MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId){
		return settingModel().model().modeHandler().finishedModel(questId, this);
	}
	
	public MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId){
		return settingModel().model().modeHandler().teleportModel(questId, this);
	}
}

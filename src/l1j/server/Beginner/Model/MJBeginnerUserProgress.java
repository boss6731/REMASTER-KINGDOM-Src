package l1j.server.Beginner.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerObjectiveListData.MJBeginnerObjective;
import l1j.server.Beginner.Model.MJBeginnerSubEventData.MJBeginnerSubEventListener;
import l1j.server.MJTemplate.MJProto.MainServer_Client.QuestProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client.QuestProgress.ObjectiveProgress;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerUserProgress {
	static MJBeginnerUserProgress newInstance(int objectId, int questId){
		MJBeginnerUserProgress progress = new MJBeginnerUserProgress();
		progress.objectId = objectId;
		progress.questId = questId;
		progress.startTime = System.currentTimeMillis();
		return progress;
	}
	
	static MJBeginnerUserProgress newInstance(int objectId, ResultSet rs) throws SQLException{
		MJBeginnerUserProgress progress = new MJBeginnerUserProgress();
		progress.objectId = objectId;
		progress.questId = rs.getInt("quest_id");
		progress.startTime = convertTimestamp(rs.getTimestamp("start_time"));
		progress.finishTime = convertTimestamp(rs.getTimestamp("finish_time"));
		return progress;
	}
	
	static long convertTimestamp(Timestamp ts){
		return ts == null ? 0 : ts.getTime();
	}
	
	private int objectId;
	private int questId;
	private long startTime;
	private long finishTime;
	private HashMap<Integer, MJBeginnerUserProgressObjective> objectives;
	
	private MJBeginnerUserProgress(){
	}
	
	int objectId(){
		return objectId;
	}
	
	int questId(){
		return questId;
	}
	
	long startTime(){
		return startTime;
	}
	
	long finishTime(){
		return finishTime;
	}

	void objectives(List<MJBeginnerUserProgressObjective> objectives){
		this.objectives = new HashMap<>(objectives.size());
		for(MJBeginnerUserProgressObjective objective : objectives){
			this.objectives.put(objective.objectiveId(), objective);
		}
	}

	boolean finished(){
		return finishTime() > 0;
	}
	
	boolean started(){
		return startTime() > 0;
	}
	
	boolean hasObjective(){
		return objectives != null && objectives.size() > 0;
	}
	
	void allocateObjective(){
		MJBeginnerQuestData questData = mappedObjectiveData();
		objectives = new HashMap<>(questData.objectiveList().objective().size());
		for(MJBeginnerObjective objectiveData : questData.objectiveList().objective()){
			MJBeginnerUserProgressObjective objective = MJBeginnerUserProgressObjective.newInstance(questId(), objectiveData.id(), 0);
			objectives.put(objective.objectiveId(), objective);
			objective.insertDatabase(objectId());
		}
	}
	
	void registeredEvent(L1PcInstance pc){
		for(MJBeginnerUserProgressObjective objective : objectives.values()){
			MJBeginnerEventProvider.provider().registeredEvent(pc, this, objective);
		}
	}
	
	void registeredSubEvent(L1PcInstance pc){
		MJBeginnerSubEventListener subEvent = MJBeginnerModelProvider.provider().subEventModel().model(questId());
		if(subEvent != null){
			subEvent.onQuest(pc, questId());
		}
	}
	
	void finishProgress(){
		finishTime = System.currentTimeMillis();
		updateDatabase();
	}
	
	void onRevealedCompleteProgress(){
		MJBeginnerQuestData questData = mappedObjectiveData();
		if(questData.hasObjectiveList()){
			for(MJBeginnerObjective objectiveData : questData.objectiveList().objective()){
				MJBeginnerUserProgressObjective objective = objectives.get(objectiveData.id());
				objective.quantity(objectiveData.requiredQuantity());
			}	
		}
	}
	
	boolean completed(){
		for(MJBeginnerUserProgressObjective objective : objectives.values()){
			MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
			if(objective.quantity() < objectiveData.requiredQuantity()){
				return false;
			}
		}
		return true;
	}
	
	QuestProgress convertClientModel(){
		QuestProgress questProgress = QuestProgress.newInstance();
		questProgress.set_id(questId());
		questProgress.set_start_time(startTime());
		questProgress.set_is_shown_in_quest_window(true);
		if(finished()){
			questProgress.set_finish_time(finishTime());
		}
		if(hasObjective()){
			for(MJBeginnerUserProgressObjective objective : objectives.values()){
				int requiredQuantity = objective.mappedObjectiveData().requiredQuantity();
				ObjectiveProgress objectiveProgress = ObjectiveProgress.newInstance();
				objectiveProgress.set_id(objective.objectiveId());
				objectiveProgress.set_quantity(Math.min(objective.quantity(), requiredQuantity));
				objectiveProgress.set_required_quantity(requiredQuantity);
				questProgress.add_objectives(objectiveProgress);
			}
		}
		return questProgress;
	}
	
	QuestProgress convertClientSKipModel(){
		QuestProgress questProgress = QuestProgress.newInstance();
		questProgress.set_id(questId());
		questProgress.set_start_time(startTime());
		questProgress.set_is_shown_in_quest_window(true);
		if(finished()){
			questProgress.set_finish_time(finishTime());
		}
		return questProgress;
	}
	
	MJBeginnerQuestData mappedObjectiveData(){
		return MJBeginnerModelProvider.provider()
				.questDataModel()
				.questData(questId());
	}
	
	void insertDatabase(){
		MJBeginnerUserDatabaseProvider.provider().insertUserProgress(objectId(), questId(), startTime());
	}
	
	void updateDatabase(){
		MJBeginnerUserDatabaseProvider.provider().updateUserProgress(objectId(), questId(), startTime(), finishTime());
	}
}

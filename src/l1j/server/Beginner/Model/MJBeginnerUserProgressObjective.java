package l1j.server.Beginner.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerObjectiveListData.MJBeginnerObjective;

class MJBeginnerUserProgressObjective {
	static MJBeginnerUserProgressObjective newInstance(int questId, int objectiveId, int quantity){
		MJBeginnerUserProgressObjective objective = new MJBeginnerUserProgressObjective();
		objective.questId(questId);
		objective.objectiveId(objectiveId);
		objective.quantity(quantity);
		return objective;
	}
	
	static MJBeginnerUserProgressObjective newInstance(ResultSet rs) throws SQLException{
		return newInstance(
				rs.getInt("quest_id"),
				rs.getInt("objective_id"),
				rs.getInt("quantity")
				);
	}
	
	private int questId;
	private int objectiveId;
	private int quantity;
	
	int questId(){
		return questId;
	}
	
	void questId(int questId){
		this.questId = questId;
	}
	
	int objectiveId(){
		return objectiveId;
	}
	
	void objectiveId(int objectiveId){
		this.objectiveId = objectiveId;
	}
	
	int quantity(){
		return quantity;
	}
	
	int addedQuantity(int added){
		return quantity += added;
	}
	
	void quantity(int quantity){
		this.quantity = quantity;
	}
	
	MJBeginnerObjective mappedObjectiveData(){
		return MJBeginnerModelProvider.provider()
				.questDataModel()
				.questData(questId())
				.objectiveList()
				.find(objectiveId());
	}
	
	void updateDatabase(int objectId){
		MJBeginnerUserDatabaseProvider.provider().updateUserProgressObjective(objectId, questId(), objectiveId(), quantity());
	}
	
	void insertDatabase(int objectId){
		MJBeginnerUserDatabaseProvider.provider().insertUserProgressObjective(objectId, questId(), objectiveId());
	}
}

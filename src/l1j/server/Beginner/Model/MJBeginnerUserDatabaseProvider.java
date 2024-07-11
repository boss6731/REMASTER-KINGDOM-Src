package l1j.server.Beginner.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

class MJBeginnerUserDatabaseProvider {
	private static final MJBeginnerUserDatabaseProvider provider = new MJBeginnerUserDatabaseProvider();
	static MJBeginnerUserDatabaseProvider provider(){
		return provider;
	}
	
	private MJBeginnerUserDatabaseProvider(){
	}
	
	MJBeginnerUser selectUserProgress(final int objectId){
		MJBeginnerUser user = MJBeginnerUser.newInstance(objectId);
		Selector.exec("select * from beginner_user_progress where object_id=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, objectId);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJBeginnerUserProgress progress = MJBeginnerUserProgress.newInstance(objectId, rs);
					user.append(progress);
					if(progress.finished()){
						continue;
					}
					progress.objectives(selectUserProgressObjective(objectId, progress.questId()));
				}
			}
		});
		return user;
	}
	
	private List<MJBeginnerUserProgressObjective> selectUserProgressObjective(final int objectId, final int questId){
		final List<MJBeginnerUserProgressObjective> objectives = new LinkedList<>();
		Selector.exec("select quest_id, objective_id, quantity from beginner_user_progress_objective where object_id=? and quest_id=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, objectId);
				pstm.setInt(2, questId);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJBeginnerUserProgressObjective objective = MJBeginnerUserProgressObjective.newInstance(rs);
					objectives.add(objective);
				}
			}
		});
		return objectives;
	}

	void updateUserProgress(final int objectId, final int questId, final long startTime, final long finishTime){
		Updator.exec("update beginner_user_progress set start_time=?, finish_time=? where object_id=? and quest_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setTimestamp(++idx, new Timestamp(startTime));
				pstm.setTimestamp(++idx, new Timestamp(finishTime));
				pstm.setInt(++idx, objectId);
				pstm.setInt(++idx, questId);
			}
		});
	}
	
	void insertUserProgress(final int objectId, final int questId, final long startTime){
		Updator.exec("insert into beginner_user_progress set object_id=?, quest_id=?, start_time=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, objectId);
				pstm.setInt(++idx, questId);
				pstm.setTimestamp(++idx, new Timestamp(startTime));
			}
		});
	}
	
	void updateUserProgressObjective(final int objectId, final int questId, final int objectiveId, final int quantity){
		Updator.exec("update beginner_user_progress_objective set quantity=? where object_id=? and quest_id=? and objective_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, quantity);
				pstm.setInt(++idx, objectId);
				pstm.setInt(++idx, questId);
				pstm.setInt(++idx, objectiveId);
			}
		});
	}
	
	void insertUserProgressObjective(final int objectId, final int questId, final int objectiveId){
		Updator.exec("insert into beginner_user_progress_objective set object_id=?, quest_id=?, objective_id=?, quantity=0", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, objectId);
				pstm.setInt(++idx, questId);
				pstm.setInt(++idx, objectiveId);
			}
		});
	}
}

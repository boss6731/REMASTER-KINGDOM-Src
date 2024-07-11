package l1j.server.revenge.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.revenge.MJRevengeService;

class MJRevengeDatabasaeProvider {
	private static final MJRevengeDatabasaeProvider winner = new MJRevengeDatabasaeProvider("revenge_winners", MJRevengeModelFactory.winner());
	private static final MJRevengeDatabasaeProvider loser = new MJRevengeDatabasaeProvider("revenge_loser", MJRevengeModelFactory.loser());

	static MJRevengeDatabasaeProvider winner() {
		return winner;
	}
	
	static MJRevengeDatabasaeProvider loser() {
		return loser;
	}
	
	private String tableName;
	private MJRevengeModelFactory factory;
	private MJRevengeDatabasaeProvider(String tableName, MJRevengeModelFactory factory) {
		this.tableName = tableName;
		this.factory = factory;
	}
	
	MJRevengeModelFactory factory() {
		return factory;
	}
	
	List<MJRevengeModel> select(final int ownerId){
		final MJObjectWrapper<List<MJRevengeModel>> wrapper = new MJObjectWrapper<>();
		wrapper.value = new LinkedList<>();
		int list_value = MJRevengeService.service().viewInRows();
		Selector.exec(String.format("select * from %s where owner_id=? order by register_timestamp desc limit %s", tableName, list_value), new SelectorHandler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, ownerId);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					MJRevengeModel model = factory.newModel();
					model.readToDatabase(rs);
					wrapper.value.add(model);
				}
			}		
		});
		return wrapper.value;
	}
	
	void onRevengeRegister(final MJRevengeModel model) {
		Updator.exec(String.format("insert into %s set owner_id=?, target_id=?, target_name=?, register_timestamp=?, action_timestamp=?, action_remain_count=?, action_count=?", tableName), new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, model.ownerId());
				pstm.setInt(++idx, model.targetId());
				pstm.setString(++idx, model.targetName());
				pstm.setTimestamp(++idx, new Timestamp(model.registerTimestamp()));
				pstm.setNull(++idx, Types.TIMESTAMP);
				pstm.setInt(++idx, model.actionRemainCount());
				pstm.setInt(++idx, model.actionCount());
			}
		});
	}
	
	void onRevengeDelete(final int ownerId, final int targetId) {
		Updator.exec(String.format("delete from %s where owner_id=? and target_id=?", tableName), new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, ownerId);
				pstm.setInt(2, targetId);
			}
		});
	}
	
	void onRevengeExpiration(final int ownerId, final long registerTimestamp) {
		if(registerTimestamp <= 0) {
			throw new IllegalArgumentException(String.format("invalid register timestamp... onwerId=%d", ownerId));
		}
		
		Updator.exec(String.format("delete from %s where owner_id=? and register_timestamp <=?", tableName), new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, ownerId);
				pstm.setTimestamp(2, new Timestamp(registerTimestamp));
			}
		});
	}
	
	void onRevengeAction(final MJRevengeModel model) {
		Updator.exec(String.format("update %s set action_timestamp=?, action_remain_count=?, action_count=?", tableName), new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				if(model.actionTimestamp() <= 0) {
					pstm.setNull(1, Types.TIMESTAMP);
				}else {
					pstm.setTimestamp(1, new Timestamp(model.actionTimestamp()));
				}
				pstm.setInt(2, model.actionRemainCount());
				pstm.setInt(3, model.actionCount());
			}
		});
	}
	
	
}

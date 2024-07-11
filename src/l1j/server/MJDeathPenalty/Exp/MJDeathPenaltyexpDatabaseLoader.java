package l1j.server.MJDeathPenalty.Exp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;

import l1j.server.MJDeathPenalty.MJDeathPenaltyProvider;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJDeathPenaltyexpDatabaseLoader {
	
	private static MJDeathPenaltyexpDatabaseLoader _instance;
	
	public static MJDeathPenaltyexpDatabaseLoader getInstance(){
		if(_instance == null)
			_instance = new MJDeathPenaltyexpDatabaseLoader();
		return _instance;
	}
	
	public void do_Select(L1PcInstance pc){
		pc.attribute().getNotExistsNew(L1PcInstance.deathpenaltyexpModelKey).set(new LinkedList<MJDeathPenaltyExpModel>());
		Selector.exec("select * from characters_deathpenalty_exp where owner_id=? order by Delete_time desc limit 20", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJDeathPenaltyExpModel info = MJDeathPenaltyExpModel.readToDatabase(rs);
					pc.attribute().getNotExistsNew(L1PcInstance.deathpenaltyexpModelKey).get().add(info);
				}
				MJDeathPenaltyProvider.provider().sendexpinfo(pc);
			}
		});
	}
	
	public void update(L1PcInstance pc, MJDeathPenaltyExpModel model){
		try{
			Updator.exec(String.format("insert into %s set owner_id=?, exp_ratio=?, recovery_cost=?, Delete_time=? , death_level=?", "characters_deathpenalty_exp"), new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, pc.getId());
					pstm.setInt(++idx, model.getExp_ratio());
					pstm.setInt(++idx, model.getRecovery_cost());
					pstm.setTimestamp(++idx, new Timestamp(model.getDelete_time() * 1000));
					pstm.setInt(++idx, model.getDeathLevel());
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		};
	}
}

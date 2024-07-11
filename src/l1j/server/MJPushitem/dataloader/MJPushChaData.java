package l1j.server.MJPushitem.dataloader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.MJPushitem.model.MJChaPushModel;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJPushChaData {
	
	private static MJPushChaData _instance;
	
	public static MJPushChaData getInstance(){
		if(_instance == null)
			_instance = new MJPushChaData();
		return _instance;
	}
	
	public void do_Select(L1PcInstance pc){
		pc.attribute().getNotExistsNew(L1PcInstance.pcpushmodelkey).set(new ArrayList<MJChaPushModel>());
		Selector.exec("select * from character_pushlist where owner_id=? order by push_id desc", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJChaPushModel info = MJChaPushModel.readToDatabase(rs);
					long currentMillis = System.currentTimeMillis();
					if(MJPushItemData.getlist() == null) {
			            continue;
			          }
			          if(MJPushItemData.getlist().get(info.getPushId()) == null) {
			            continue;
			          }
					if (MJPushItemData.getlist().get(info.getPushId()).getExpiredate() < currentMillis / 1000) {
						MJPushChaData.getInstance().DeleteInfo(pc, info.getPushId());
						continue;
					}
					pc.attribute().getNotExistsNew(L1PcInstance.pcpushmodelkey).get().add(info);
				}
			}
		});
	}
	
	public void Update_Info(final int ownerId, final MJChaPushModel model){
		try{
			Updator.exec(String.format("insert into %s set owner_id=?, push_id=?, state=? on duplicate key update state=?", "character_pushlist"), new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, ownerId);
					pstm.setInt(++idx, model.getPushId());
					pstm.setInt(++idx, model.getPushstate());
					pstm.setInt(++idx, model.getPushstate());
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		};
	}
	
	public void DeleteInfo(L1PcInstance pc, int pushid){
		Updator.exec("delete from character_pushlist where owner_id=? and push_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, pushid);
			}
		});
	}
}

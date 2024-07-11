package l1j.server.CPMWBQSystem.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.CPMWBQSystem.info.CPMWBQinfo;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;
public class CPMWBQUserTable {
	private static CPMWBQUserTable _instance;

	public static CPMWBQUserTable getInstance(){
		if(_instance == null)
			_instance = new CPMWBQUserTable();
		return _instance;
	}

	public void UserInfo(L1PcInstance pc){
		Selector.exec("select * from cpmw_bookquest_userinfo where owner_id=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				pc.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo).set(new ArrayList<CPMWBQinfo>(3));
				while(rs.next()){
					CPMWBQinfo model = CPMWBQinfo.newInstance(rs);
					pc.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo).get().add(model);
				}
			}
		});
	}

	public void Update_Info(final int ownerId, final CPMWBQinfo UserInfo){
		try{
			Updator.exec(String.format("insert into %s set owner_id=?, map_id=?, map_desc=?, mon_count=?, isclear=? on duplicate key update mon_count=?, isclear=?", "cpmw_bookquest_userinfo"), new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, ownerId);
					pstm.setInt(++idx, UserInfo.getMapid());
					pstm.setInt(++idx, UserInfo.getMapdesc());
					pstm.setInt(++idx, UserInfo.getMoncount());
					pstm.setInt(++idx, UserInfo.isIsclear() ? 1 : 0);
					pstm.setInt(++idx, UserInfo.getMoncount());
					pstm.setInt(++idx, UserInfo.isIsclear() ? 1 : 0);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		};
	}
	
	public void DeleteInfo(L1PcInstance pc, int mapid, int mapdesc){
		Updator.exec("delete from cpmw_bookquest_userinfo where owner_id=? and map_id=? and map_desc=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, mapid);
				pstm.setInt(++idx, mapdesc);
			}
		});
	}
	
	public void InnituserInfo(){
		Updator.exec("delete from cpmw_bookquest_userinfo", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
			}
		});
	}
}

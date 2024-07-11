package l1j.server.MJDungeonTimer.Loader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Progress.AccountTimeProgress;
import l1j.server.MJDungeonTimer.Progress.CharacterTimeProgress;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class DungeonTimeProgressLoader{
	public static void load(final L1PcInstance pc){
		Selector.exec("select * from tb_dungeon_time_char_information where owner_info=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					CharacterTimeProgress progress = CharacterTimeProgress.newInstance(rs);
					pc.put_dungeon_progress(progress.get_timer_id(), progress);
				}
			}
		});
		Selector.exec("select * from tb_dungeon_time_account_information where owner_info=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, pc.getAccountName());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					AccountTimeProgress progress = AccountTimeProgress.newInstance(rs);
					pc.put_dungeon_progress(progress.get_timer_id(), progress);
				}
			}
		});
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_map_id(pc.getMapId());
				if(dtInfo != null){
					pc.send_dungeon_progress(dtInfo);
				}				
			}
		}, 2000L);
	}

	public static void delete(L1PcInstance pc){
		Updator.exec("delete from tb_dungeon_time_char_information where owner_info=? and charge='false'", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}
		});
		Updator.exec("delete from tb_dungeon_time_account_information where owner_info=? and charge='false'", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, pc.getAccountName());
			}
		});
		Updator.exec("UPDATE tb_dungeon_time_char_information SET charge_count=0 where owner_info=? and charge='true'", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}
		});
		Updator.exec("UPDATE tb_dungeon_time_account_information SET charge_count=0 where owner_info=? and charge='true'", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, pc.getAccountName());
			}
		});
	}
	
	public static void delete(L1PcInstance pc, int timer_id, boolean is_account){
		String table = is_account ? "tb_dungeon_time_account_information" : "tb_dungeon_time_char_information";
		Updator.exec(String.format("delete from %s where owner_info=? and timer_id=?", table), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, pc.getAccountName());
				pstm.setInt(2,  timer_id);
			}
		});
	}
	
	public static void update(L1PcInstance pc){
		update_character(pc);
		update_account(pc);
	}
	
	private static void update_character(L1PcInstance pc){
		Collection<DungeonTimeProgress<?>> col = pc.get_character_progresses();
		int size = col.size();
		if(size <= 0)
			return;
		
		ArrayList<DungeonTimeProgress<?>> list = new ArrayList<DungeonTimeProgress<?>>(col);
		Updator.batch("insert into tb_dungeon_time_char_information set owner_info=?, timer_id=?,  remain_seconds=?, charge_count=?, charge=? "
				+ "on duplicate key update  remain_seconds=?, charge_count=?, charge=?", new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				DungeonTimeProgress<?> progress = list.get(callNumber);
				pstm.setObject(1, progress.get_owner_info());
				pstm.setInt(2, progress.get_timer_id());
				if (progress.get_remain_seconds() < 0) {
					DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(progress.get_timer_id());
					String type = dtInfo.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, 0);
					pstm.setInt(4, 0);
					pstm.setString(5, type);
					pstm.setInt(6, 0);
					pstm.setInt(7, 0);
					pstm.setString(8, type);
				} else {
					DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(progress.get_timer_id());
					String type = dtInfo.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, progress.get_remain_seconds());
					pstm.setInt(4, progress.get_charge_count());
					pstm.setString(5, type);
					pstm.setInt(6, progress.get_remain_seconds());
					pstm.setInt(7, progress.get_charge_count());
					pstm.setString(8, type);
				}
			}
		}, size);
	}
	
	private static void update_account(L1PcInstance pc){
		Collection<DungeonTimeProgress<?>> col = pc.get_account_progresses();
		int size = col.size();
		if(size <= 0)
			return;
		
		ArrayList<DungeonTimeProgress<?>> list = new ArrayList<DungeonTimeProgress<?>>(col);
		Updator.batch("insert into tb_dungeon_time_account_information set owner_info=?, timer_id=?, remain_seconds=?, charge_count=?, charge=? "
				+ "on duplicate key update remain_seconds=?, charge_count=?, charge=?", new BatchHandler() {
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				DungeonTimeProgress<?> progress = list.get(callNumber);
				pstm.setObject(1, progress.get_owner_info());
				pstm.setInt(2, progress.get_timer_id());
				if (progress.get_remain_seconds() < 0) {
					DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(progress.get_timer_id());
					String type = dtInfo.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, 0);
					pstm.setInt(4, 0);
					pstm.setString(5, type);
					pstm.setInt(6, 0);
					pstm.setInt(7, 0);
					pstm.setString(8, type);
				} else {
					DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(progress.get_timer_id());
					String type = dtInfo.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, progress.get_remain_seconds());
					pstm.setInt(4, progress.get_charge_count());
					pstm.setString(5, type);
					pstm.setInt(6, progress.get_remain_seconds());
					pstm.setInt(7, progress.get_charge_count());
					pstm.setString(8, type);
				}
			}
		}, size);
	}
	
	public static void update(DungeonTimeProgress<?> progress){
		if(progress instanceof CharacterTimeProgress)
			update((CharacterTimeProgress)progress);
		else
			update((AccountTimeProgress)progress);
	}
	
	public static void update(CharacterTimeProgress progress){
		Updator.exec("insert into tb_dungeon_time_char_information set owner_info=?, timer_id=?,  remain_seconds=?, charge_count=?, charge=? "
				+ "on duplicate key update  remain_seconds=?, charge_count=?, charge=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, progress.get_owner_info());
				pstm.setInt(2, progress.get_timer_id());
				if (progress.get_remain_seconds() < 0) {
					String type = progress.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, 0);
					pstm.setInt(4, 0);
					pstm.setString(5, type);
					pstm.setInt(6, 0);
					pstm.setInt(7, 0);
					pstm.setString(8, type);
				} else {
					String type = progress.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, progress.get_remain_seconds());
					pstm.setInt(4, progress.get_charge_count());
					pstm.setString(5, type);
					pstm.setInt(6, progress.get_remain_seconds());
					pstm.setInt(7, progress.get_charge_count());
					pstm.setString(8, type);
				}
			}			
		});
	}
	
	public static void update(AccountTimeProgress progress){
		Updator.exec("insert into tb_dungeon_time_account_information set owner_info=?, timer_id=?, remain_seconds=?, charge_count=?, charge=? "
				+ "on duplicate key update remain_seconds=?, charge_count=?, charge=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, progress.get_owner_info());
				pstm.setInt(2, progress.get_timer_id());
				if (progress.get_remain_seconds() < 0) {
					String type = progress.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, 0);
					pstm.setInt(4, 0);
					pstm.setString(5, type);
					pstm.setInt(6, 0);
					pstm.setInt(7, 0);
					pstm.setString(8, type);
				} else {
					String type = progress.get_charge_count() > 0 ? "true" : "false";
					pstm.setInt(3, progress.get_remain_seconds());
					pstm.setInt(4, progress.get_charge_count());
					pstm.setString(5, type);
					pstm.setInt(6, progress.get_remain_seconds());
					pstm.setInt(7, progress.get_charge_count());
					pstm.setString(8, type);
				}
			}
		});
	}
}

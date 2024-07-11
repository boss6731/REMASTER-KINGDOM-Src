package l1j.server.MJTemplate.MJSqlHelper.Executors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJTime;
import l1j.server.MJTemplate.MJSqlHelper.Executor;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.utils.SQLUtil;

public class Selector implements Executor{
	public static List<MJTableNameInfo> tableList(final String like, final TableListSortOption option){
		final List<MJTableNameInfo> names = new LinkedList<>();
		Selector.exec("show table status where name like ?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, String.format("%%%s%%", like));
			}
			
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					names.add(new MJTableNameInfo(rs.getString("Name"), rs.getTimestamp("Create_time").getTime()));
				}
			}			
		});
		if(option != TableListSortOption.NONE){
			Collections.sort(names, new TableNameSorter(option));
		}
		return names;
	}
	
	public enum TableListSortOption{
		NONE,
		ASC,
		DESC,
	};
	
	public static class MJTableNameInfo{
		public final String name;
		public final long createTimeMillis;
		MJTableNameInfo(String name, long createTimeMillis){
			this.name = name;
			this.createTimeMillis = createTimeMillis;
		}
		
		public LocalDateTime toDateTime(){
			return MJTime.toDateTime(createTimeMillis);
		}
		
		public boolean equalsDay(LocalDateTime dateTime){
			LocalDateTime createTime = toDateTime();
			return createTime.getYear() == dateTime.getYear() &&
					createTime.getMonthValue() == dateTime.getMonthValue() &&
					createTime.getDayOfMonth() == dateTime.getDayOfMonth();
		}
	}
	
	private static class TableNameSorter implements Comparator<MJTableNameInfo>{
		private TableListSortOption option;
		TableNameSorter(TableListSortOption option){
			if(option == TableListSortOption.NONE)
				throw new IllegalArgumentException(String.format("無效的排序選項...%s", option.name()));
			
			this.option = option;
		}
		
		@Override
		public int compare(MJTableNameInfo o1, MJTableNameInfo o2) {
			int comp = 0;
			if(o1.createTimeMillis < o2.createTimeMillis) {
				comp = -1;
			}else if(o1.createTimeMillis > o2.createTimeMillis){
				comp = 1;
			}
			return option == TableListSortOption.DESC ? comp * -1 : comp;
		}
	}
	
	public static boolean hasTable(final String tableName){
		final MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper<>();
		wrapper.value = false;
		exec("show tables like ?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, tableName);
			}
			
			@Override
			public void result(ResultSet rs) throws Exception {
				wrapper.value = rs.next();
			}
		});
		return wrapper.value;
	}
	
	public static void exec(String query, SelectorHandler handler){
		new Selector().execute(query, handler);
	}
	
	@Override
	public int execute(String query, Handler handler){
		if(!(handler instanceof SelectorHandler))
			throw new IllegalArgumentException("handler is not SelectorHandler...!");
		
		SelectorHandler sHandler = (SelectorHandler)handler;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(query);
			sHandler.handle(pstm);
			rs = pstm.executeQuery();
			sHandler.result(rs);
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}
}

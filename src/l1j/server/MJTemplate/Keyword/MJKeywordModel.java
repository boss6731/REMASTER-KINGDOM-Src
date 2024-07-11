package l1j.server.MJTemplate.Keyword;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

public abstract class MJKeywordModel {
	public static MJKeywordModel newEmptyModel(String tableName){
		return new MJKeywordEmptyModel(tableName);
	}
	
	public static MJKeywordModel newMapModel(String tableName, Map<String, MJKeywordRankModel> mapModel){
		return new MJKeywordMapModel(tableName, mapModel);
	}
	
	public static MJKeywordModel newDatabaseModel(String tableName){
		return new MJKeywordDatabaseModel(tableName);
	}
	
	private final String tableName;
	protected MJKeywordModel(String tableName){
		this.tableName = tableName;
	}
	
	String tableName(){
		return tableName;
	}
	
	abstract void newTable();
	abstract void dropTable();
	public abstract void insert(final String keyword, final String who);
	public abstract void insert(final List<String> keywords, final String who);
	abstract int numOfKeywordAll(final String keyword);
	abstract int numOfKeywordAny(final String keyword);
	abstract List<MJKeywordRankModel> selectRanksKeywords(final int limit);
	
	private static class MJKeywordEmptyModel extends MJKeywordModel{
		private MJKeywordEmptyModel(String tableName){
			super(tableName);
		}

		@Override
		void newTable() {
		}

		@Override
		void dropTable() {
		}

		@Override
		public void insert(String keyword, String who) {
		}

		@Override
		public void insert(List<String> keywords, String who) {
		}

		@Override
		int numOfKeywordAll(String keyword) {
			return 0;
		}

		@Override
		int numOfKeywordAny(String keyword) {
			return 0;
		}
		
		@Override
		List<MJKeywordRankModel> selectRanksKeywords(int limit) {
			return Collections.emptyList();
		}
	}
	
	private static class MJKeywordMapModel extends MJKeywordModel{
		private Map<String, MJKeywordRankModel> mapModel;
		private MJKeywordMapModel(String tableName, Map<String, MJKeywordRankModel> mapModel){
			super(tableName);
			this.mapModel = mapModel;
		}
		@Override
		void newTable() {
		}
		@Override
		void dropTable() {
		}
		@Override
		public void insert(String keyword, String who) {
		}
		@Override
		public void insert(List<String> keywords, String who) {
		}
		@Override
		int numOfKeywordAll(String keyword) {
			MJKeywordRankModel model = mapModel.get(keyword);
			return model == null ? 0 : model.accessCount();
		}

		@Override
		int numOfKeywordAny(String keyword) {
			return numOfKeywordAll(keyword);
		}
		
		@Override
		List<MJKeywordRankModel> selectRanksKeywords(int limit) {
			ArrayList<MJKeywordRankModel> models = new ArrayList<>(mapModel.values());
			Collections.sort(models);
			return models.size() > limit ? models.subList(0, limit) : models;
		}
	}
	
	private static class MJKeywordDatabaseModel extends MJKeywordModel{
		private static final String CreateTable =
				"CREATE TABLE `%s` (" +
				"`keyword` varchar(128) NOT NULL," +
				"`who` varchar(64) NOT NULL," +
				"`access_time` int(0) DEFAULT NULL," +
				"PRIMARY KEY (`keyword`,`who`)," +
				"KEY `keyworkd-index` (`keyword`, `who`, `access_time`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=euckr;";
		
		private static final String Insert = 
				"insert ignore into `%s` set keyword=?, who=?, access_time=?";
		
		private static final String SelectRanksKeywords = 
				"select keyword, count(*) as cnt from `%s` group by keyword order by cnt desc limit ?";
		
		private static final String NumOfKeywordAll =
				"select count(*) as cnt from `%s` where keyword=?";

		private static final String NumOfKeywordAny =
				"select count(*) as cnt from `%s` where keyword=? and access_time<=?";
		
		private static String sql(String source, String tableName){
			return String.format(source, tableName);
		}
		
		private MJKeywordDatabaseModel(String tableName){
			super(tableName);
		}
		
		@Override
		void newTable(){
			if(!Selector.hasTable(tableName())){
				Updator.exec(sql(CreateTable, tableName()), new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {}
				});
			}
		}

		@Override
		void dropTable(){
			Updator.drop(tableName());
		}
		
		@Override
		public void insert(final String keyword, final String who){
			if(MJString.isNullOrEmpty(keyword)) {
				return;
			}
			
			final int time = convertInt(LocalDateTime.now());
			Updator.exec(sql(Insert, tableName()), new Handler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, keyword);
					pstm.setString(2, who);
					pstm.setInt(3, time);
				}			
			});
		}
		
		@Override
		public void insert(final List<String> keywords, final String who){
			final int time = convertInt(LocalDateTime.now());
			Updator.batch(sql(Insert, tableName()), new BatchHandler(){
				@Override
				public void handle(PreparedStatement pstm, int callNumber) throws Exception {
					pstm.setString(1, keywords.get(callNumber));
					pstm.setString(2, who);
					pstm.setInt(3, time);
				}
			}, keywords.size());
		}
		
		@Override
		List<MJKeywordRankModel> selectRanksKeywords(final int limit){
			final List<MJKeywordRankModel> models = new ArrayList<>(limit);
			Selector.exec(sql(SelectRanksKeywords, tableName()), new SelectorHandler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, limit);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					int rank = 0;
					while(rs.next()){
						MJKeywordRankModel model = new MJKeywordRankModel(
								rs.getString("keyword"), ++rank, rs.getInt("cnt"));
						models.add(model);
					}
				}
			}); 
			return models;
		}
		
		@Override
		int numOfKeywordAll(final String keyword){
			final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<>();
			wrapper.value = 0;
			Selector.exec(sql(NumOfKeywordAll, tableName()), new SelectorHandler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, keyword);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					if(rs.next()){
						wrapper.value = rs.getInt("cnt");
					}
				}
			});
			return wrapper.value;
		}

		@Override
		int numOfKeywordAny(String keyword) {
			final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<>();
			wrapper.value = 0;
			Selector.exec(sql(NumOfKeywordAny, tableName()), new SelectorHandler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					LocalDateTime dateTime = LocalDateTime.now();
					pstm.setString(1, keyword);
					pstm.setInt(2, convertInt(dateTime));
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					if(rs.next()){
						wrapper.value = rs.getInt("cnt");
					}
				}
			});
			return wrapper.value;
		}
		
		private static int convertInt(LocalDateTime dateTime){
			return (dateTime.getHour() * 10000) +
					(dateTime.getMinute() * 100) +
					(dateTime.getSecond());
		}
	}
}

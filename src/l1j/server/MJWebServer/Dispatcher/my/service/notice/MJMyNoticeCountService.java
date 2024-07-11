package l1j.server.MJWebServer.Dispatcher.my.service.notice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

class MJMyNoticeCountService {
	private static final MJMyNoticeCountService likeService = new MJMyNoticeCountService("my_notice_like");
	private static final MJMyNoticeCountService hitService = new MJMyNoticeCountService("my_notice_hit");
	
	static MJMyNoticeCountService like(){
		return likeService;
	}
	
	static MJMyNoticeCountService hit(){
		return hitService;
	}
	
	private String selectCountTotal;
	private String selectCount;
	private String already;
	private String insert;
	private MJMyNoticeCountService(String table){
		this.selectCountTotal = String.format("select articleId, count(*) as numOfCount from %s group by articleId", table);
		this.selectCount = String.format("select count(*) as numOfCount from %s where articleId=?", table);
		this.already = String.format("select articleId from %s where articleId=? and account=? limit 1", table);
		this.insert = String.format("insert ignore into %s set articleId=?, account=?, millis=?", table);
	}
	
	Map<Integer, Integer> selectCountTotal(){
		final Map<Integer, Integer> map = new HashMap<>();
		Selector.exec(selectCountTotal, new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					map.put(rs.getInt("articleId"), rs.getInt("numOfCount"));
				}
			}
		});
		return map;
	}
	
	int selectCount(final int articleId){
		final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<>();
		wrapper.value = 0;
		Selector.exec(selectCount, new SelectorHandler(){

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, articleId);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					wrapper.value = rs.getInt("numOfCount");
				}
			}
		});
		return wrapper.value;
	}
	
	boolean insert(final int articleId, final String account){
		return Updator.exec(insert, new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, articleId);
				pstm.setString(2, account);
				pstm.setLong(3, System.currentTimeMillis());
			}
		}) != 0;
	}
	
	boolean already(final int articleId, final String account){
		final MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper<>();
		wrapper.value = false;
		Selector.exec(already, new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, articleId);
				pstm.setString(2, account);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				wrapper.value = rs.next();
			}
			
		});
		return wrapper.value;
	}
	
	
}

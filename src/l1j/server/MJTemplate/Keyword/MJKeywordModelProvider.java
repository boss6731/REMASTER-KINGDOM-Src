package l1j.server.MJTemplate.Keyword;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector.MJTableNameInfo;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector.TableListSortOption;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.gametime.RealTimeClock;

public class MJKeywordModelProvider {
	private static final HashMap<String, MJKeywordModelProvider> deduplications = new HashMap<>();
	public static MJKeywordModelProvider newProvider(String name, MJKeywordModelFactory modelFactory, int limit, int updateTickMinute){
		synchronized(deduplications){
			MJKeywordModelProvider provider = deduplications.get(name);
			if(provider != null){
				return provider;
			}

			provider = new MJKeywordModelProvider(name, modelFactory, limit, updateTickMinute);
			provider.newView();
			deduplications.put(name, provider);
			return provider;
		}
	}
	
	private final String name;
	private final MJKeywordModelFactory modelFactory;
	private final int limit;
	private final int updateTickMinute;
	private MJKeywordView view;
	private MJKeywordModelProvider(String name, MJKeywordModelFactory modelFactory, int limit, int updateTickMinute){
		this.name = name;
		this.modelFactory = modelFactory;
		this.limit = limit;
		this.updateTickMinute = updateTickMinute;
	}
	
	String name(){
		return name;
	}
	
	String tableName(LocalDateTime dateTime){
		return String.format("%s[%04d%02d%02d]", 
				name, dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
	}
	
	MJKeywordModelFactory modelFactory(){
		return modelFactory;
	}
	
	MJKeywordModel newNormalModel(LocalDateTime dateTime){
		MJKeywordModel model = modelFactory.newNormalModel(tableName(dateTime));
		model.newTable();
		return model;
	}
	
	MJKeywordModel newEmptyModel(LocalDateTime dateTime){
		MJKeywordModel model = modelFactory.newEmptyModel(tableName(dateTime));
		model.newTable();
		return model;
	}
	
	public MJKeywordView view(){
		return view;
	}
	
	void newView(){	
		if(view != null){
			return;
		}
		newView0();
	}
	
	private void newView0(){
		List<MJTableNameInfo> names = Selector.tableList(name, TableListSortOption.DESC);
		LocalDateTime now = LocalDateTime.now();
		MJKeywordModel currentModel = newNormalModel(now);
		MJKeywordModel previousModel = null;		
		MJTableNameInfo nInfo = safeList(names, 0);
		if(nInfo == null){
			previousModel = newEmptyModel(now.minusDays(1));
		}else{
			if(!nInfo.equalsDay(now)){
				previousModel = newNormalModel(nInfo.toDateTime());
			}else{
				nInfo = safeList(names, 1);
				if(nInfo == null){
					previousModel = newEmptyModel(now.minusDays(1));
				}else{
					previousModel = newNormalModel(nInfo.toDateTime());
				}
			}
		}
		if(names.size() > 1) {
			try {
				for(int i=names.size() - 1; i>1; --i) {
					Updator.drop(names.get(i).name);
				}
			}catch(Exception e) {
			}
		}
		view = new MJKeywordView(this, previousModel, currentModel, limit);
		RealTimeClock.getInstance().addListener(view, Calendar.DAY_OF_MONTH);
		executeScheduler();
	}
	
	private void executeScheduler(){
		long millis = updateTickMinute * 60000L;
		GeneralThreadPool.getInstance().scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				view.onTick();
			}
		}, 0, millis);
	}
	
	private static <T> T safeList(List<T> list, int index){
		if(list == null || list.size() >= index){
			return null;
		}
		return list.get(index);
	}
	
	
}

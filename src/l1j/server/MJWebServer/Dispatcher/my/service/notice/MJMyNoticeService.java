package l1j.server.MJWebServer.Dispatcher.my.service.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyNotice;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyNotice.MJMyNoticeCategory;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyNotice.MJMyNoticeItem;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;

public class MJMyNoticeService {
	private static final int COUNT_PER_PAGE = 20;
	private static final MJMyNoticeService service = new MJMyNoticeService();
	
	public static MJMyNoticeService service(){
		return service;
	}

	private ArrayList<MJMyNoticeCategory> categories;
	private HashMap<Integer, MJMyNoticeServiceItem> items;
	private ArrayList<MJMyNoticeServiceItemInfo> itemsInfo;
	private MJMyNoticeService(){
	}
	
	private Map<Integer, MJMyNoticeServiceItem> safeItems(){
		return items;
	}
	
	private List<MJMyNoticeServiceItemInfo> safeItemsInfo(){
		return itemsInfo;
	}
	
	public void onNoticeInfoChanged(MJMyNotice newNotice){
		updateCategory(newNotice);
		updateArticle(newNotice);
	}
	
	private void updateCategory(MJMyNotice newNotice){
		this.categories = newNotice.noticeCategories();
	}
	
	private void updateArticle(MJMyNotice newNotice){
		HashMap<Integer, MJMyNoticeServiceItem> items = new HashMap<>(newNotice.items().size());
		ArrayList<MJMyNoticeServiceItemInfo> itemsInfo = new ArrayList<>(newNotice.items().size());
		
		for(MJMyNoticeItem noticeItem : newNotice.items()){
			MJMyNoticeServiceItem item = newNoticeItem(noticeItem);
			items.put(item.articleId(), item);
			itemsInfo.add(item.noticeInfo);
		}
		updateArticleData(items);
		this.items = items;
		this.itemsInfo = itemsInfo;
	}
	
	private MJMyNoticeServiceItem newNoticeItem(MJMyNoticeItem nItem){
		MJMyNoticeServiceItem item = new MJMyNoticeServiceItem();
		item.noticeInfo.articleId = nItem.articleId();
		item.noticeInfo.category = nItem.noticeCategory();
		MJMyNoticeCategory category = findCategory(nItem.noticeCategory());
		if(category != null){
			item.noticeInfo.categoryTitle = category.categoryTitle();
		}
		item.noticeInfo.subject = nItem.subject();
		item.noticeInfo.uri = String.format("/my/noticeRead?articleId=%d", nItem.articleId());
		MJMonitorCacheModel<String> model = MJMonitorCacheProvider.newTextFileCacheModel(item.noticeInfo.uri, nItem.localPath(), MJEncoding.MS949);
		model.cacheListener(item);
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
		return item;
	}
	
	private void updateArticleData(final HashMap<Integer, MJMyNoticeServiceItem> items){
		Map<Integer, Integer> like = MJMyNoticeCountService.like().selectCountTotal();
		Map<Integer, Integer> hit = MJMyNoticeCountService.hit().selectCountTotal();
		for(MJMyNoticeServiceItem item : items.values()){
			int articleId = item.articleId();
			if(like.containsKey(articleId)){
				item.noticeInfo.articleData.like = like.get(articleId);
			}
			if(hit.containsKey(articleId)){
				item.noticeInfo.articleData.hit = hit.get(articleId);
			}
		}
	}
	
	public int totalPage(){
		return MJMyServiceHelper.calculateTotalPage(itemsInfo.size(), COUNT_PER_PAGE);
	}
	
	public int totalPage(MJMyNoticeCategory category){
		if(category.allView()){
			return totalPage();
		}

		List<MJMyNoticeServiceItemInfo> itemsInfo = safeItemsInfo();
		int categoryIndex = category.category();
		int totalCount = 0;
		for(int i = itemsInfo.size() - 1; i>=0; --i){
			MJMyNoticeServiceItemInfo item = itemsInfo.get(i);
			if(item.category != categoryIndex){
				continue;
			}
			
			++totalCount;
		}
		return MJMyServiceHelper.calculateTotalPage(totalCount, COUNT_PER_PAGE);
	}
	
	public int countPerPage(){
		return COUNT_PER_PAGE;
	}
	
	public MJMyNoticeSelectResult selectItemsInfo(MJMyNoticeCategory category, int page, String query, boolean onlyTitle){
		return selectItemsInfo(category, page, query, onlyTitle, COUNT_PER_PAGE);
	}
	
	public MJMyNoticeSelectResult selectItemsInfo(MJMyNoticeCategory category, int page, String query, boolean onlyTitle, int countPerPage){
		return selectItemsInfo(MJMyNoticeMatcherService.createMatcher(category, query, onlyTitle), page, countPerPage);
	}
	
	public MJMyNoticeSelectResult selectItemsInfo(Matcher<MJMyNoticeServiceItemInfo> matcher, int page){
		return selectItemsInfo(matcher, page, COUNT_PER_PAGE);
	}
	
	public MJMyNoticeSelectResult selectItemsInfo(Matcher<MJMyNoticeServiceItemInfo> matcher, int page, int countPerPage){
		List<MJMyNoticeServiceItemInfo> itemsInfo = safeItemsInfo();
		int size = itemsInfo.size();
		int offset = MJMyServiceHelper.calculateOffset(page, countPerPage);
		MJMyNoticeSelectResult result = new MJMyNoticeSelectResult();
		result.items = new ArrayList<>(Math.min(countPerPage, size));
		int append = 0;
		for(int i=0; i<size; ++i){
			MJMyNoticeServiceItemInfo item = itemsInfo.get(i);
			if(!matcher.matches(item)){
				continue;
			}
			++result.totalCount;
			if(i < offset){
				continue;
			}
			if(++append <= countPerPage){
				result.items.add(item);
			}
		}
		result.totalPage = MJMyServiceHelper.calculateTotalPage(result.totalCount, countPerPage);
		return result;
	}
	
	public MJMyNoticeServiceItem getItem(int articleId){
		return items.get(articleId);
	}
	
	public List<MJMyNoticeCategory> categories(){
		return categories;
	}
	
	public MJMyNoticeCategory findCategory(int categoryIndex){
		for(MJMyNoticeCategory category : categories){
			if(category.category() == categoryIndex){
				return category;
			}
		}
		return null;
	}
	
	public boolean onHit(int articleId, String account){
		MJMyNoticeServiceItem item = safeItems().get(articleId);
		if(item == null){
			return false;
		}
		if(MJMyNoticeCountService.hit().insert(articleId, account)){
			item.noticeInfo.articleData().increaseHit();			
			return true;
		}
		return false;
	}
	
	public boolean alreadyLike(int articleId, String account){
		return MJMyNoticeCountService.like().already(articleId, account);
	}
	
	public boolean onLike(int articleId, String account){
		MJMyNoticeServiceItem item = safeItems().get(articleId);
		if(item == null){
			return false;
		}
		if(MJMyNoticeCountService.like().insert(articleId, account)){
			item.noticeInfo.articleData().increaseLike();
			return true;
		}
		return false;
	}
	
	public static class MJMyNoticeServiceItem implements MJMonitorCacheConverter<String>{
		private MJMyNoticeServiceItemInfo noticeInfo;
		String noticeData;
		
		private MJMyNoticeServiceItem(){
			noticeInfo = new MJMyNoticeServiceItemInfo();
			noticeData = null;
		}
		
		public int articleId(){
			return noticeInfo.articleId;
		}
		
		public MJMyNoticeServiceItemInfo noticeInfo(){
			return noticeInfo;
		}
		
		public String noticeData(){
			return noticeData;
		}
		
		@Override
		public String onNewCached(String t, long modifiedMillis) {
			noticeData = t;
			noticeInfo.lastModified = modifiedMillis;
			return null;
		}
	}
	
	public static class MJMyNoticeServiceItemInfo{
		private int articleId;
		private int category;
		private String categoryTitle;
		private String subject;
		private String uri;
		private long lastModified;
		private MJMyNoticeArticleData articleData;
		private MJMyNoticeServiceItemInfo(){
			articleId = -1;
			category = -1;
			categoryTitle = MJString.EmptyString;
			subject = MJString.EmptyString;
			uri = MJString.EmptyString;
			lastModified = 0L;
			articleData = new MJMyNoticeArticleData();
		}
		
		public int articleId(){
			return articleId;
		}
		
		public int category(){
			return category;
		}
		
		public String categoryTitle(){
			return categoryTitle;
		}
		
		public String subject(){
			return subject;
		}
		
		public String uri(){
			return uri;
		}

		public long lastModified(){
			return lastModified;
		}
		
		public MJMyNoticeArticleData articleData(){
			return articleData;
		}
	}
	
	public static class MJMyNoticeArticleData{
		private int hit;
		private int like;
		private MJMyNoticeArticleData(){
			hit = 0;
			like = 0;
		}

		public int hit(){
			return hit;
		}
		
		public int like(){
			return like;
		}
		
		void increaseLike(){
			synchronized(this){
				++like;
			}
		}
		
		void increaseHit(){
			synchronized(this){
				++hit;
			}
		}
	}
}

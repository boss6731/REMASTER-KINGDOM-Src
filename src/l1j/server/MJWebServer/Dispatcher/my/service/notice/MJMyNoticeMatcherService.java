package l1j.server.MJWebServer.Dispatcher.my.service.notice;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyNotice.MJMyNoticeCategory;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService.MJMyNoticeServiceItem;
import l1j.server.MJWebServer.Dispatcher.my.service.notice.MJMyNoticeService.MJMyNoticeServiceItemInfo;

class MJMyNoticeMatcherService {
	static Matcher<MJMyNoticeServiceItemInfo> createMatcher(MJMyNoticeCategory category, String query, boolean onlyTitle){
		return Matchers.pairs(createCategoryMatcher(category), createContainsMatcher(query, onlyTitle), true);
	}
	
	static Matcher<MJMyNoticeServiceItemInfo> createCategoryMatcher(MJMyNoticeCategory category){
		return category.allView() ? Matchers.all() : new CategoryMatcher(category.category());
	}
	
	static Matcher<MJMyNoticeServiceItemInfo> createContainsMatcher(String query, boolean onlyTitle){
		if(MJString.isNullOrEmpty(query)){
			return Matchers.all();
		}
		return onlyTitle ? 
				new SubjectContainsMatcher(query) :
				Matchers.pairs(new SubjectContainsMatcher(query), new ContentContainsMatcher(query), false);
	}
	
	private static class CategoryMatcher implements Matcher<MJMyNoticeServiceItemInfo>{
		private int category;
		private CategoryMatcher(int category){
			this.category = category;
		}
		@Override
		public boolean matches(MJMyNoticeServiceItemInfo t) {
			return category == t.category();
		}
	}
	
	private static class SubjectContainsMatcher implements Matcher<MJMyNoticeServiceItemInfo>{
		private String query;
		private SubjectContainsMatcher(String query){
			this.query = query;
		}
		
		@Override
		public boolean matches(MJMyNoticeServiceItemInfo t) {
			return t.subject().contains(query);
		}
	}
	
	private static class ContentContainsMatcher implements Matcher<MJMyNoticeServiceItemInfo>{
		private String query;
		private ContentContainsMatcher(String query){
			this.query = query;
		}
		
		@Override
		public boolean matches(MJMyNoticeServiceItemInfo t) {
			MJMyNoticeServiceItem item = MJMyNoticeService.service().getItem(t.articleId());
			return item != null && item.noticeData.contains(query);
		}
	}
}

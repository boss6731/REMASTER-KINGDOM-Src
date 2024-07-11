package l1j.server.MJWebServer.Dispatcher.my.resource;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJString;

public class MJMyNotice {
	private ArrayList<MJMyNoticeCategory> noticeCategories;
	private ArrayList<MJMyNoticeItem> items;
	MJMyNotice(){
	}
	
	public MJMyNoticeCategory findCategory(int categoryIndex){
		for(MJMyNoticeCategory category : noticeCategories){
			if(category.category == categoryIndex){
				return category;
			}
		}
		return null;
	}
	
	public ArrayList<MJMyNoticeCategory> noticeCategories(){
		return noticeCategories;
	}
	
	public ArrayList<MJMyNoticeItem> items(){
		return items;
	}
	
	public static class MJMyNoticeCategory{
		private int category;
		private String categoryTitle;
		private boolean allView;
		private MJMyNoticeCategory(){
			category = -1;
			categoryTitle = MJString.EmptyString;
			allView = false;
		}
		
		public int category(){
			return category;
		}
		
		public String categoryTitle(){
			return categoryTitle;
		}
		
		public boolean allView(){
			return allView;
		}
	}
	
	public static class MJMyNoticeItem{
		private int articleId;
		private int noticeCategory;
		private String subject;
		private String localPath;
		private MJMyNoticeItem(){
			articleId = -1;
			noticeCategory = -1;
			subject = MJString.EmptyString;
			localPath = MJString.EmptyString;
		}
		
		public int articleId(){
			return articleId;
		}
		
		public int noticeCategory(){
			return noticeCategory;
		}
		
		public String subject(){
			return subject;
		}
		
		public String localPath(){
			return localPath;
		}
	}
}

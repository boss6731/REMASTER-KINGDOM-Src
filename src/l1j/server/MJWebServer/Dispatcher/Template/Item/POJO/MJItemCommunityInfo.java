package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;

public class MJItemCommunityInfo {
	public ArrayList<ItemCommunityInfo> list;
	public MJItemCommunityInfo(){
		list = new ArrayList<ItemCommunityInfo>();
	}
	
	public static class ItemCommunityInfo{
		public int articleId;
		public String emoticonUrl;
		public int categoryId;
		public String categoryName;
		public int serverId;
		public String serverName;
		public String title;
		public String summary;
		public String link;
	}
}

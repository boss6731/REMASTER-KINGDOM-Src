package l1j.server.MJWebServer.Dispatcher.my.page;

import java.util.ArrayList;

public class MJMyPageMapped {
	private String accountsURL;
	private ArrayList<MJMyPageInfo> categories;
	public String accountsURL(){
		return accountsURL;
	}
	
	public ArrayList<MJMyPageInfo> categories(){
		return categories;
	}
	
	public static class MJMyPageInfo{
		byte[] pageBinary;
		String pageText;
		private String controller;
		private boolean binaryCache;
		private int category;
		private String name;
		private String uri;
		private String desc;
		private String localPath;
		private boolean notice;
		private boolean menu;
		private boolean gm;
		
		public String controller(){
			return controller;
		}
		
		public boolean binaryCache(){
			return binaryCache;
		}
		
		public int category(){
			return category;
		}
		
		public String name(){
			return name;
		}
		
		public String uri(){
			return uri;
		}
		
		public String desc(){
			return desc;
		}
		
		public String localPath(){
			return localPath;
		}
		
		public boolean notice(){
			return notice;
		}
		
		public boolean menu(){
			return menu;
		}
		
		public boolean gm(){
			return gm;
		}
	}
}

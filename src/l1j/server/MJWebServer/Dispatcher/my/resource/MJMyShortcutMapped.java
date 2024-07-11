package l1j.server.MJWebServer.Dispatcher.my.resource;

import java.util.ArrayList;

public class MJMyShortcutMapped {
	private ArrayList<MJMyShortcutInfo> categories;
	public ArrayList<MJMyShortcutInfo> categories(){
		return categories;
	}
	
	public static class MJMyShortcutInfo{
		private String name;
		private boolean newWindow;
		private String uri;
		private String shortcutClassName;
		private String id;
		public String name(){
			return name;
		}
		
		public boolean newWindow(){
			return newWindow;
		}
		
		public String uri(){
			return uri;
		}
		
		public String shortcutClassName(){
			return shortcutClassName;
		}
		
		public String id(){
			return id;
		}
	}
}

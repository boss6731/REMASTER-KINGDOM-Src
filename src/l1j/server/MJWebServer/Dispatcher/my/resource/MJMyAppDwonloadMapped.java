package l1j.server.MJWebServer.Dispatcher.my.resource;

import java.util.ArrayList;

public class MJMyAppDwonloadMapped {
	private ArrayList<MJMyAppDwonloadInfo> apps;
	public ArrayList<MJMyAppDwonloadInfo> apps(){
		return apps;
	}
	
	public static class MJMyAppDwonloadInfo{		
		private String name;
		private String uri;
		private String img;
		public String name(){
			return name;
		}
		
		public String uri(){
			return uri;
		}
		
		public String img(){
			return img;
		}
	}
}

package l1j.server.MJWebServer.Dispatcher.my.outside;

import java.util.ArrayList;

public class MJMyOutsideMapped {
	private ArrayList<MJMyOutsideInfo> categories;
	public ArrayList<MJMyOutsideInfo> categories(){
		return categories;
	}
	
	public static class MJMyOutsideInfo{
		byte[] pageBinary;
		private String name;
		private String uri;
		private String localPath;
		
		public String name(){
			return name;
		}
		
		public String uri(){
			return uri;
		}
		
		public String localPath(){
			return localPath;
		}
	}
}

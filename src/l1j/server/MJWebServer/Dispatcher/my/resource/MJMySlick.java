package l1j.server.MJWebServer.Dispatcher.my.resource;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJString;

public class MJMySlick {
	private ArrayList<MJMySlickItem> items;
	public ArrayList<MJMySlickItem> items(){
		return items;
	}
	
	public static class MJMySlickItem{
		private String mtit;
		private String dtit;
		private String desc;
		private String link;
		private String small;
		private String large;
		MJMySlickItem(){
			mtit = MJString.EmptyString;
			dtit = MJString.EmptyString;
			desc = MJString.EmptyString;
			link = MJString.EmptyString;
			small = MJString.EmptyString;
			large = MJString.EmptyString;
		}
		
		public String mtit(){
			return mtit;
		}
		
		public String dtit(){
			return dtit;
		}
		
		public String desc(){
			return desc;
		}
		
		public String link(){
			return link;
		}
		
		public String small(){
			return small;
		}
		
		public String large(){
			return large;
		}
	}
}

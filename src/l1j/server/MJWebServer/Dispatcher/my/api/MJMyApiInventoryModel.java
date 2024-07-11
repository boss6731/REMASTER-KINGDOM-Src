package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemCategory;

class MJMyApiInventoryModel extends MJMyApiModel{
	int numOfItems;
	List<ItemInfo> items;
	MJMyApiInventoryModel(){
		super();
		numOfItems = 0;
		items = new LinkedList<>();
	}
	
	static class ItemInfo{
		String name;
		String display;
		int iconId;
		int count;
		String category;
		ItemInfo(){
			name = MJString.EmptyString;
			display = MJString.EmptyString;
			count = 1;
			category = MJMyCharInvItemCategory.etc.name();
		}
	}
}

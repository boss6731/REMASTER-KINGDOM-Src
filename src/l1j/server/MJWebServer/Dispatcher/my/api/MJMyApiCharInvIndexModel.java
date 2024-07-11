package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemCategory;

class MJMyApiCharInvIndexModel extends MJMyApiModel{
	private static final List<InvCategoryInfo> CATEGORY;
	static{
		CATEGORY = new ArrayList<InvCategoryInfo>();
		for(MJMyCharInvItemCategory category : MJMyCharInvItemCategory.values()){
			CATEGORY.add(new InvCategoryInfo(category.index(), category.name(), category.text()));
		}
	}
	
	List<InvCategoryInfo> category;
	InvIndexSizeInfo size;
	MJMyApiCharInvIndexModel(){
		super();
		category = CATEGORY;
		size = new InvIndexSizeInfo();
	}
	
	static class InvIndexSizeInfo{
		int inventory;
		int account;
		int character;
		private InvIndexSizeInfo(){
			inventory = account = character = 0;
		}
	}
	
	static class InvCategoryInfo{
		int categoryIndex;
		String categoryName;
		String categoryText;
		InvCategoryInfo(int categoryIndex, String categoryName, String categoryText){
			this.categoryIndex = categoryIndex;
			this.categoryName = categoryName;
			this.categoryText = categoryText;
		}
	}
}

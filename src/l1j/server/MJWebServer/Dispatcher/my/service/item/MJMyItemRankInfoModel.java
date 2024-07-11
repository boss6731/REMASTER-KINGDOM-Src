package l1j.server.MJWebServer.Dispatcher.my.service.item;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;

public class MJMyItemRankInfoModel {
	static List<MJMyItemRankInfoModel> fromItemsId(int[] itemsId){
		List<MJMyItemRankInfoModel> models = new ArrayList<>(itemsId.length);
		for(int i : itemsId){
			MJMyItemRankInfoModel model = fromItemId(i);
			if(model == null){
				continue;
			}
			
			models.add(model);
		}
		return models;
	}
	
	static MJMyItemRankInfoModel fromItemId(int itemId){
		L1Item item = ItemTable.getInstance().getTemplate(itemId);
		if(item == null){
			return null;
		}
		
		MJMyItemRankInfoModel model = new MJMyItemRankInfoModel();
		model.itemName = item.getName();
		model.display = item.getName();
		model.iconId = item.getGfxId();
		model.rankFluctuations = 0;
		model.isNew = true;
		return model;
	}
	
	String itemName;
	String display;
	int iconId;
	int rankFluctuations;
	boolean isNew;
	MJMyItemRankInfoModel(){
	}
	
	public String itemName(){
		return itemName;
	}
	
	public String display(){
		return display;
	}
	
	public int iconId(){
		return iconId;
	}
	
	public int rankFluctuations(){
		return rankFluctuations;
	}
	
	public boolean isNew(){
		return isNew;
	}
	
}

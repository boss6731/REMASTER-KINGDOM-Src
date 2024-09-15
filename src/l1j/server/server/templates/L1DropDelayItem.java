package l1j.server.server.templates;

public class L1DropDelayItem {
	private int _itemid;
	private String _itemName;
	
	public int getItemId(){
		return _itemid;
	}
	
	public void setItemId(int id){
		_itemid = id;
	}
	
	public String getItemName(){
		return _itemName;
	}
	public void setItemName(String name){
		_itemName = name;
	}
}
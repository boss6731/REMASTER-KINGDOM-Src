package l1j.server.server.templates;

public class L1ItemMessage {
	private int _itemid;
	private String _itemName;
	private int _option;
	private int _type;
	private boolean _isment;
	private String _ment;
	
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
	
	public int getOption(){
		return _option;
	}
	public void setOption(int i){
		_option = i;
	}
	
	public int getType(){
		return _type;
	}
	public void setType(int i){
		_type = i;
	}
	
	public boolean isMentuse(){
		return _isment;
	}
	
	public L1ItemMessage setMentuse(boolean flag){
		_isment = flag;
		return this;
	}
	
	public String getMent(){
		return _ment;
	}
	
	public void setMent(String ment){
		_ment = ment;
	}

}
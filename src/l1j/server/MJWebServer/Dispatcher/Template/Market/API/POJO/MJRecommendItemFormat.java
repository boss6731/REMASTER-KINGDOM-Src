package l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO;

public class MJRecommendItemFormat {
	private int 	_itemId;
	private String 	_name;
	private int		_gfx;
	
	public MJRecommendItemFormat(){
		_itemId=0;
		_name=null;
		_gfx=0;
	}
	
	public int getItemId(){
		return _itemId;
	}
	
	public void setItemId(int i){
		_itemId = i;
	}
	
	public String getName(){
		return _name;
	}
	
	public void setName(String s){
		_name = s;
	}
	
	public int getGfx(){
		return _gfx;
	}
	
	public void setGfx(int i){
		_gfx = i;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder(64);
		sb.append("Id : ").append(_itemId).append("\r\nname : ").append(_name).append("\r\nGFX : ").append(_gfx);
		sb.append("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
		return sb.toString();
	}
}

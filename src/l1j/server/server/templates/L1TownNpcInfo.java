package l1j.server.server.templates;

public class L1TownNpcInfo {
	private int _id;
	private int _npcid;
	private String _npcName;
	private int _sprid;
	private int _townid;
	
	public int getId(){
		return _id;
	}
	
	public void setId(int id){
		_id = id;
	}
	
	public int getNpcId(){
		return _npcid;
	}
	
	public void setNpcId(int id){
		_npcid = id;
	}
	
	public int geSprId(){
		return _sprid;
	}
	
	public void setSprId(int id){
		_sprid = id;
	}
	
	public String getNpcName(){
		return _npcName;
	}
	public void setNpcName(String name){
		_npcName = name;
	}
	
	public int getTownId(){
		return _townid;
	}
	
	public void setTownId(int town){
		_townid = town;
	}

}
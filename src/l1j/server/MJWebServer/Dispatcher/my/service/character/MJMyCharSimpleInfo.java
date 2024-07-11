package l1j.server.MJWebServer.Dispatcher.my.service.character;

public class MJMyCharSimpleInfo {
	private int objectId;
	private String nick;
	private int level;
	private int characterClass;
	private String gender;
	private boolean gm;
	
	void objectId(int objectId){
		this.objectId = objectId;
	}
	
	void nick(String nick){
		this.nick = nick;
	}
	
	void level(int level){
		this.level = level;
	}
	
	void characterClass(int characterClass){
		this.characterClass = characterClass;
	}
	
	void gender(String gender){
		this.gender = gender;
	}
	
	void gm(boolean gm){
		this.gm = gm;
	}
	
	public int objectId(){
		return objectId;
	}
	
	public String nick(){
		return nick;
	}
	
	public int level(){
		return level;
	}
	
	public int characterClass(){
		return characterClass;
	}
	
	public String gender(){
		return gender;
	}
	
	public boolean gm(){
		return gm;
	}
}

package l1j.server.server.model.item.collection.time.construct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 실렉티스 전시회 대분류 타입
 * @author LinOffice
 */
public enum L1TimeCollectionType {
	NORMAL(1,	"NORMAL"),
	SPECIAL(2,	"SPECIAL");
	private int _type;
	private String _name;
	private L1TimeCollectionType(int type, String name) {
		_type	= type;
		_name	= name;
	}
	public int getType(){
		return _type;
	}
	public String getName(){
		return _name;
	}
	
	private static final ConcurrentHashMap<Integer, L1TimeCollectionType> TYPE_DATA;
	private static final ConcurrentHashMap<String, L1TimeCollectionType> NAME_DATA;
	static {
		TYPE_DATA = new ConcurrentHashMap<>();
		NAME_DATA = new ConcurrentHashMap<>();
		for(L1TimeCollectionType type : L1TimeCollectionType.values()){
			TYPE_DATA.put(type._type, type);
			NAME_DATA.put(type._name, type);
		}
	}
	
	public static L1TimeCollectionType getType(int type){
		return TYPE_DATA.get(type);
	}
	
	public static L1TimeCollectionType getType(String name){
		return NAME_DATA.get(name);
	}
	
}

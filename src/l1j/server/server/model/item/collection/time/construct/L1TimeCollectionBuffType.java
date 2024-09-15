package l1j.server.server.model.item.collection.time.construct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 실렉티스 전시회 버프 타입
 * @author LinOffice
 */
public enum L1TimeCollectionBuffType {
	SHORT(1,	"SHORT"),
	LONG(2,		"LONG"),
	MAGIC(3,	"MAGIC");
	private int _type;
	private String _name;
	private L1TimeCollectionBuffType(int type, String name) {
		_type	= type;
		_name	= name;
	}
	public int getType(){
		return _type;
	}
	public String getName(){
		return _name;
	}
	
	private static final ConcurrentHashMap<Integer, L1TimeCollectionBuffType> TYPE_DATA;
	private static final ConcurrentHashMap<String, L1TimeCollectionBuffType> NAME_DATA;
	static {
		TYPE_DATA = new ConcurrentHashMap<>();
		NAME_DATA = new ConcurrentHashMap<>();
		for(L1TimeCollectionBuffType type : L1TimeCollectionBuffType.values()){
			TYPE_DATA.put(type._type, type);
			NAME_DATA.put(type._name, type);
		}
	}
	
	public static L1TimeCollectionBuffType getType(int type){
		return TYPE_DATA.get(type);
	}
	
	public static L1TimeCollectionBuffType getType(String name){
		return NAME_DATA.get(name);
	}
}

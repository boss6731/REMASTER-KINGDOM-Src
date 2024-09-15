package l1j.server.server.model.item.collection.favor.construct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 성물 목록 타입
 * @author LinOffice
 */
public enum L1FavorBookListType {
	ALL(0,		"ALL"),		// 전체
	RELIC(1,	"RELIC"),	// 성물
	EVENT(2,	"EVENT");	// 이벤트
	private int _type;
	private String _name;
	private L1FavorBookListType(int type, String name) {
		_type	= type;
		_name	= name;
	}
	public int getType(){
		return _type;
	}
	public String getName(){
		return _name;
	}
	
	private static final ConcurrentHashMap<Integer, L1FavorBookListType> NUMBER_DATA;
	private static final ConcurrentHashMap<String, L1FavorBookListType> NAME_DATA;
	static {
		NUMBER_DATA	= new ConcurrentHashMap<>();
		NAME_DATA	= new ConcurrentHashMap<>();
		for(L1FavorBookListType type : L1FavorBookListType.values()){
			NUMBER_DATA.put(type._type,	type);
			NAME_DATA.put(type._name,	type);
		}
	}
	public static L1FavorBookListType getListType(int type){
		return NUMBER_DATA.get(type);
	}
	public static L1FavorBookListType getListType(String str){
		return NAME_DATA.get(str);
	}
}

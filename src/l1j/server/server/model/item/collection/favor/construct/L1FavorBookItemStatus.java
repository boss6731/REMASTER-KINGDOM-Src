package l1j.server.server.model.item.collection.favor.construct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 聖物背包物品狀態
 * @author LinOffice
 */
public enum L1FavorBookItemStatus {
	CRAFT(0),    // 製作
	ENCHANT(1),  // 強化
	BLESS(2),    // 祝福
	CHANGE(3);   // 變更
	private int status;
	private L1FavorBookItemStatus(int status) {
		this.status	= status;
	}
	public int getStatus(){
		return status;
	}
	
	private static final ConcurrentHashMap<Integer, L1FavorBookItemStatus> DATA;
	static {
		DATA = new ConcurrentHashMap<>();
		for(L1FavorBookItemStatus obj : L1FavorBookItemStatus.values()){
			DATA.put(obj.status, obj);
		}
	}
	
	public static L1FavorBookItemStatus getStatus(int value){
		return DATA.get(value);
	}
}

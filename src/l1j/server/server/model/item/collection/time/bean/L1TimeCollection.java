package l1j.server.server.model.item.collection.time.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 오브젝트
 * @author LinOffice
 */
public class L1TimeCollection {
	private int flag;
	private L1TimeCollectionType type;
	private int collectionIndex;
	private HashMap<Integer, L1TimeCollectionMaterial> material;// Key: slotIndex, Value: Material
	private HashMap<Integer, ConcurrentHashMap<Integer, L1TimeCollectionDuration>> duration;// Key: slotIndex, Value: (enchant, L1TimeCollectionDuration)
	private HashMap<Integer, ConcurrentHashMap<L1TimeCollectionBuffType, L1TimeCollectionAblity>> ablity;// Key: sum, Value: (Key: buffType, Value: ablity)
	private HashMap<Integer, L1TimeCollectionDuration> lastDuration;// Key: slotIndex, Value: L1TimeCollectionDuration
	private int lastAblitySum;
	
	public L1TimeCollection(ResultSet rs) throws SQLException {
		this(rs.getInt("flag"), L1TimeCollectionType.getType(rs.getString("type")), rs.getInt("collectionIndex"));
	}
	
	public L1TimeCollection(int flag, L1TimeCollectionType type, int collectionIndex) {
		this.flag				= flag;
		this.type				= type;
		this.collectionIndex	= collectionIndex;
		this.material			= new HashMap<>();
		this.duration			= new HashMap<>();
		this.ablity				= new HashMap<>();
	}	

	public int getFlag() {
		return flag;
	}
	
	public L1TimeCollectionType getType() {
		return type;
	}
	
	public int getCollectionIndex() {
		return collectionIndex;
	}
	
	public L1TimeCollectionMaterial getMaterial(int slotIndex) {
		return material.get(slotIndex);
	}
	
	public void putMaterial(L1TimeCollectionMaterial obj){
		this.material.put(obj.getSlotIndex(), obj);
	}
	
	public int getSlotSize(){
		return material.size();
	}
	
	public int getDuration(int slotIndex, int enchant) {
		L1TimeCollectionDuration last = lastDuration.get(slotIndex);// 슬롯의 최대 인챈트 오브젝트
		if (last != null && enchant > last.getEnchant()) {// 조사하는 강화 수치가 마지막 강화 수치보다 크면 마지막 강화 수치로 선택한다. 
			return last.getHour();
		}
		ConcurrentHashMap<Integer, L1TimeCollectionDuration> map = duration.get(slotIndex);
		if (map == null) {
			return 0;
		}
		L1TimeCollectionDuration obj = map.get(enchant);
		if (obj == null) {
			return 0;
		}
		return obj.getHour();
	}
	
	public void putDuration(L1TimeCollectionDuration obj){
		ConcurrentHashMap<Integer, L1TimeCollectionDuration> map = duration.get(obj.getSlotIndex());
		if (map == null) {
			map = new ConcurrentHashMap<Integer, L1TimeCollectionDuration>();
			duration.put(obj.getSlotIndex(), map);
		}
		map.put(obj.getEnchant(), obj);
	}
	
	public L1TimeCollectionAblity getAblity(int sum, L1TimeCollectionBuffType buffType){
		ConcurrentHashMap<L1TimeCollectionBuffType, L1TimeCollectionAblity> map = ablity.get(sum);
		if (map == null){
			if (sum > lastAblitySum) {// 조사하는 합산 강화 수치가 마지막 강화 수치보다 크면 마지막 강화 수치로 선택한다. 
				map = ablity.get(lastAblitySum);
			}
		}
		if (map == null || map.isEmpty()) {
			return null;
		}
		return map.get(buffType);
	}
	
	public void putAblity(L1TimeCollectionAblity obj){
		ConcurrentHashMap<L1TimeCollectionBuffType, L1TimeCollectionAblity> map = ablity.get(obj.getSum());
		if (map == null) {
			map = new ConcurrentHashMap<>();
			ablity.put(obj.getSum(), map);
		}
		map.put(obj.getBuffType(), obj);
	}
	
	public void setLastValue(){
		lastDuration 	= calcLastDuration();
		lastAblitySum	= calcLastAblity();
	}
	
	private int calcLastAblity(){
		Iterator<Integer> it = ablity.keySet().iterator();
		int last = 0;
		while (it.hasNext()) {
			int current = it.next();
			if (current > last) {
				last = current;
			}
		}
		return last;
	}
	
	private HashMap<Integer, L1TimeCollectionDuration> calcLastDuration(){
		HashMap<Integer, L1TimeCollectionDuration> result = new HashMap<Integer, L1TimeCollectionDuration>();
		
		for (Map.Entry<Integer, ConcurrentHashMap<Integer, L1TimeCollectionDuration>> entry : duration.entrySet()) {
			int lastEnchant = 0;
			L1TimeCollectionDuration lastObj = null;
			for (Map.Entry<Integer, L1TimeCollectionDuration> entry2 : entry.getValue().entrySet()) {
				int enchant = entry2.getKey();
				if (enchant > lastEnchant) {
					lastEnchant	= enchant;
					lastObj		= entry2.getValue();
				}
			}
			if (lastEnchant > 0 && lastObj != null) {
				result.put(entry.getKey(), lastObj);
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flag: ").append(flag).append(StringUtil.LineString);
		sb.append("type: ").append(type.getName()).append(StringUtil.LineString);
		sb.append("collectionIndex: ").append(collectionIndex).append(StringUtil.LineString);
		sb.append("material size: ").append(material.size()).append(StringUtil.LineString);
		sb.append("duration size: ").append(duration.size()).append(StringUtil.LineString);
		sb.append("ablity size: ").append(ablity.size()).append(StringUtil.LineString);
		return sb.toString();
	}
	
}

package l1j.server.server.model.item.collection.time.bean;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.collection.time.L1TimeCollectionTimer;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 유저 오브젝트
 * @author LinOffice
 */
public class L1TimeCollectionUser {
	private int charObjId;
	private int flag;
	private L1TimeCollectionType type;
	private int collectionIndex;
	private ConcurrentHashMap<Integer, L1ItemInstance> registItem;// Key: slotIndex Value: item
	private boolean registComplet;
	private int sumEnchant;
	private L1TimeCollectionBuffType buffType;
	private Timestamp buffTime;
	private L1TimeCollectionTimer buffTimer;
	private L1TimeCollectionAblity ablity;
	private int buffIndex;
	private L1TimeCollection obj;
	private int refill_count;
	
	public L1TimeCollectionUser(int charObjId, int flag, L1TimeCollectionType type, int collectionIndex,
			ConcurrentHashMap<Integer, L1ItemInstance> registItem, boolean registComplet, int sumEnchant,
			L1TimeCollectionBuffType buffType, Timestamp buffTime, L1TimeCollection obj, int refill_count) {
		this.charObjId			= charObjId;
		this.flag				= flag;
		this.type				= type;
		this.collectionIndex	= collectionIndex;
		this.registItem			= registItem;
		this.registComplet		= registComplet;
		this.sumEnchant			= sumEnchant;
		this.buffType			= buffType;
		this.buffTime			= buffTime;
		this.obj				= obj;
		this.refill_count		= refill_count;
	}
	
	public int getRefill_count() {
		return refill_count;
	}
	public void setRefill_count(int i) {
		refill_count = i;
	}

	public int getCharObjId() {
		return charObjId;
	}
	public void setCharObjId(int charObjId) {
		this.charObjId = charObjId;
	}

	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}

	public L1TimeCollectionType getType() {
		return type;
	}
	public void setType(L1TimeCollectionType type) {
		this.type = type;
	}

	public int getCollectionIndex() {
		return collectionIndex;
	}
	public void setCollectionIndex(int collectionIndex) {
		this.collectionIndex = collectionIndex;
	}

	public ConcurrentHashMap<Integer, L1ItemInstance> getRegistItem() {
		return registItem;
	}
	public void setRegistItem(ConcurrentHashMap<Integer, L1ItemInstance> registItem) {
		this.registItem = registItem;
	}
	public void putRegistItem(int slotIndex, L1ItemInstance item){
		if (registItem == null) {
			registItem = new ConcurrentHashMap<>();
		}
		registItem.put(slotIndex, item);
	}

	public boolean isRegistComplet() {
		return registComplet;
	}
	public void setRegistComplet(boolean registComplet) {
		this.registComplet = registComplet;
	}

	public int getSumEnchant() {
		return sumEnchant;
	}
	public void setSumEnchant(int sumEnchant) {
		this.sumEnchant = sumEnchant;
	}

	public L1TimeCollectionBuffType getBuffType() {
		return buffType;
	}
	public void setBuffType(L1TimeCollectionBuffType buffType) {
		this.buffType = buffType;
	}

	public Timestamp getBuffTime() {
		return buffTime;
	}
	public void setBuffTime(Timestamp buffTime) {
		this.buffTime = buffTime;
	}
	public void addBuffTime(long time) {
//		System.out.println(time);
		long bufftimelong = Timestamp.valueOf(buffTime.toString()).getTime(); 
		long addedTimelong = bufftimelong + time;
		Timestamp addedTime = new Timestamp(addedTimelong);
		buffTime = addedTime;
	}
	
	public boolean isBuffActive(){
		return buffTime != null && buffTimer != null;
	}
	public long restBuffTime(){
		if (buffTime == null) {
			return 0L;
		}
		return buffTime.getTime() - System.currentTimeMillis();
	}
	
	public L1TimeCollectionTimer getBuffTimer() {
		return buffTimer;
	}
	public void setBuffTimer(L1TimeCollectionTimer buffTimer) {
		this.buffTimer = buffTimer;
	}
	
	public L1TimeCollectionAblity getAblity() {
		return ablity;
	}
	public void setAblity(L1TimeCollectionAblity ablity) {
		this.ablity = ablity;
	}
	
	public int getBuffIndex() {
		return buffIndex;
	}
	public void setBuffIndex(int buffIndex) {
		this.buffIndex = buffIndex;
	}

	public L1TimeCollection getObj(){
		return obj;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("charObjId: ").append(charObjId).append(StringUtil.LineString);
		sb.append("flag: ").append(flag).append(StringUtil.LineString);
		sb.append("type: ").append(type.getName()).append(StringUtil.LineString);
		sb.append("collectionIndex: ").append(collectionIndex).append(StringUtil.LineString);
		sb.append("registItem size: ").append(registItem.size()).append(StringUtil.LineString);
		sb.append("registComplet: ").append(registComplet).append(StringUtil.LineString);
		sb.append("sumEnchant: ").append(sumEnchant).append(StringUtil.LineString);
		sb.append("buffType: ").append(buffType.getName()).append(StringUtil.LineString);
		sb.append("buffTime: ").append(buffTime).append(StringUtil.LineString);
		sb.append("isBuffActive: ").append(isBuffActive()).append(StringUtil.LineString);
		sb.append("ablity: ").append(ablity).append(StringUtil.LineString);
		sb.append("buffIndex: ").append(buffIndex).append(StringUtil.LineString);
		return sb.toString();
	}
	
}

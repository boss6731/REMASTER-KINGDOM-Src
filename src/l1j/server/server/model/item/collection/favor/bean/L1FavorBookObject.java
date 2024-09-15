package l1j.server.server.model.item.collection.favor.bean;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
import l1j.server.server.utils.StringUtil;

public class L1FavorBookObject {
	private L1FavorBookListType listType;
	private L1FavorBookTypeObject type;
	private int index;
	private ConcurrentHashMap<Integer, L1FavorBookRegistObject> register;
	private ArrayList<Integer> itemIds;
	
	public L1FavorBookObject(L1FavorBookListType listType, L1FavorBookTypeObject type,
			int index,
			ConcurrentHashMap<Integer, L1FavorBookRegistObject> register,
			ArrayList<Integer> itemIds) {
		this.listType	= listType;
		this.type		= type;
		this.index		= index;
		this.register	= register;
		this.itemIds	= itemIds;
	}

	public L1FavorBookListType getListType() {
		return listType;
	}
	public L1FavorBookTypeObject getType() {
		return type;
	}
	public int getIndex() {
		return index;
	}
	public ConcurrentHashMap<Integer, L1FavorBookRegistObject> getRegister() {
		return register;
	}
	public L1FavorBookRegistObject getRegister(int descId, int bless) {
		if (register == null) {
			return null;
		}
		switch (type.getType()) {
		case 1:// 축복의 성물
			return register.get(bless);
		default:
			return register.get(descId);
		}
	}
	public ArrayList<Integer> getItemIds() {
		return itemIds;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("listType : ").append(listType.getName()).append(StringUtil.LineString);
		sb.append("type : ").append(type.getType()).append(StringUtil.LineString);
		sb.append("index : ").append(index).append(StringUtil.LineString);
		sb.append("register size : ").append(register == null ? 0 : register.size()).append(StringUtil.LineString);
		sb.append("itemIds size : ").append(itemIds == null ? 0 : itemIds.size()).append(StringUtil.LineString);
		return sb.toString();
	}
}

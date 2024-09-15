package l1j.server.server.model.item.collection.favor.bean;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
import l1j.server.server.utils.StringUtil;

public class L1FavorBookUserObject {
	private L1FavorBookListType listType;
	private L1FavorBookTypeObject type;
	private int index;
	private L1ItemInstance currentItem;
	private L1FavorBookObject obj;
	
	public L1FavorBookUserObject(L1FavorBookListType listType, L1FavorBookTypeObject type, int index, L1FavorBookObject obj) {
		this(listType, type, index, null, obj);
	}
	
	public L1FavorBookUserObject(L1FavorBookListType listType, L1FavorBookTypeObject type, int index, L1ItemInstance currentItem, L1FavorBookObject obj) {
		this.listType		= listType;
		this.type			= type;
		this.index			= index;
		this.currentItem	= currentItem;
		this.obj			= obj;
	}

	public L1FavorBookListType getListType() {
		return listType;
	}
	public void setListType(L1FavorBookListType listType) {
		this.listType = listType;
	}

	public L1FavorBookTypeObject getType() {
		return type;
	}
	public void setType(L1FavorBookTypeObject type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public L1ItemInstance getCurrentItem() {
		return currentItem;
	}
	public void setCurrentItem(L1ItemInstance currentItem) {
		this.currentItem = currentItem;
	}
	
	public L1FavorBookObject getObj() {
		return obj;
	}
	public void setObj(L1FavorBookObject obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("listType: ").append(listType.getName()).append(StringUtil.LineString);
		sb.append("type: ").append(type.getType()).append(StringUtil.LineString);
		sb.append("index: ").append(index).append(StringUtil.LineString);
		sb.append("itemObjId: ").append(currentItem == null ? 0 : currentItem.getId()).append(StringUtil.LineString);
		sb.append("itemId: ").append(currentItem == null ? 0 : currentItem.getItemId()).append(StringUtil.LineString);
		return sb.toString();
	}
}

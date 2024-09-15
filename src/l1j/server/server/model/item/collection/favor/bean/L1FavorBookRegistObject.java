package l1j.server.server.model.item.collection.favor.bean;

import l1j.server.server.model.item.collection.favor.construct.L1FavorBookItemStatus;
import l1j.server.server.utils.StringUtil;

public class L1FavorBookRegistObject {
	private int descId;
	private int craftId;
	private L1FavorBookItemStatus status;
	
	public L1FavorBookRegistObject(String[] array) {
		this(
				Integer.parseInt(array[0].trim()),// descId or bless
				Integer.parseInt(array[1].trim()),// craftId
				L1FavorBookItemStatus.getStatus(Integer.parseInt(array[2].trim()))// status
				);
	}
	
	public L1FavorBookRegistObject(int descId, int craftId, L1FavorBookItemStatus status) {
		this.descId		= descId;
		this.craftId	= craftId;
		this.status		= status;
	}

	public int getDescId() {
		return descId;
	}
	public int getCraftId() {
		return craftId;
	}
	public L1FavorBookItemStatus getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("descId: ").append(descId).append(StringUtil.LineString);
		sb.append("craftId: ").append(craftId).append(StringUtil.LineString);
		sb.append("status: ").append(status.name()).append(StringUtil.LineString);
		return sb.toString();
	}
	
}

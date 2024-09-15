package l1j.server.server.model.item.collection.time.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 재료 오브젝트
 * @author LinOffice
 */
public class L1TimeCollectionMaterial {
	private int flag;
	private int slotIndex;
	private ArrayList<Integer> descIds;
	private int enchant;
	
	public L1TimeCollectionMaterial(ResultSet rs) throws SQLException {
		this(rs.getInt("flag"), rs.getInt("slotIndex"), new ArrayList<Integer>(), rs.getInt("enchant"));
		String[] array = rs.getString("descIds").split(StringUtil.CommaString);
		for (String id : array) {
			descIds.add(Integer.parseInt(id.trim()));
		}
	}
	
	public L1TimeCollectionMaterial(int flag, int slotIndex, ArrayList<Integer> descIds, int enchant) {
		this.flag		= flag;
		this.slotIndex	= slotIndex;
		this.descIds	= descIds;
		this.enchant	= enchant;
	}
	
	public int getFlag() {
		return flag;
	}
	public int getSlotIndex() {
		return slotIndex;
	}
	public ArrayList<Integer> getDescIds() {
		return descIds;
	}
	public int getEnchant() {
		return enchant;
	}
	
	/**
	 * 등록가능한 아이템인지 조사한다.
	 * @param item
	 * @return boolean
	 */
	public boolean isMaterial(L1ItemInstance item){
		if (item.getEnchantLevel() < enchant) {// 최소 인챈트 수치를 체크한다.
			return false;
		}
		return descIds.contains(item.getItem().getItemDescId());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flag: ").append(flag).append(StringUtil.LineString);
		sb.append("slotIndex: ").append(slotIndex).append(StringUtil.LineString);
		sb.append("enchant: ").append(enchant).append(StringUtil.LineString);
		sb.append("descIds: ").append(descIds).append(StringUtil.LineString);
//		sb.append("----- descIds -----\r\n");
		for(int descid : descIds){
			sb.append(descid).append(StringUtil.LineString);
		}
		sb.append("---------------\r\n");
		return sb.toString();
	}
}

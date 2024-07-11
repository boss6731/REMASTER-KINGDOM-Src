package l1j.server.MJDeathPenalty.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.MJTemplate.MJTime;

public class MJDeathPenaltyItemModel {
	private int ownerId;
	private int itemobjid;
	private int item_id;
	private int count;
	private int enchantlvl;
	private int is_id;
	private int durability;
	private int charge_count;
	private int remaining_time;
	private Timestamp last_used;
	private int attr_enchantlvl;
	private int special_enchant;
	private int bless;
	private int bless_level;
	private int item_level;
	private String Hotel_Town;
	private Timestamp end_time;
	private int carving;
	private int dollpoten;
	private long Delete_time;
	
	public static MJDeathPenaltyItemModel newInstance(){
		return new MJDeathPenaltyItemModel();
	}
	
	public static MJDeathPenaltyItemModel readToDatabase(ResultSet rs) throws SQLException {
		MJDeathPenaltyItemModel info = newInstance();
		info.ownerId = rs.getInt("owner_id");
		info.itemobjid = rs.getInt("itemobjid");
		info.item_id = rs.getInt("item_id");
		info.count = rs.getInt("count");
		info.enchantlvl = rs.getInt("enchantlvl");
		info.is_id = rs.getInt("is_id");
		info.durability = rs.getInt("durability");
		info.charge_count  = rs.getInt("charge_count");
		info.remaining_time  = rs.getInt("remaining_time");
		info.last_used  = rs.getTimestamp("last_used");
		info.attr_enchantlvl  = rs.getInt("attr_enchantlvl");
		info.special_enchant  = rs.getInt("special_enchant");
		info.bless  = rs.getInt("bless");
		info.bless_level  = rs.getInt("bless_level");
		info.item_level  = rs.getInt("item_level");
		info.Hotel_Town  = rs.getString("Hotel_Town");
		info.end_time  = rs.getTimestamp("end_time");
		info.carving  = rs.getInt("carving");
		info.dollpoten = rs.getInt("dollpoten");
		info.Delete_time = MJTime.convertLong(rs.getTimestamp("delete_time")) / 1000;
		return info;
	}
	
	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getItemobjid() {
		return itemobjid;
	}

	public void setItemobjid(int itemobjid) {
		this.itemobjid = itemobjid;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getEnchantlvl() {
		return enchantlvl;
	}

	public void setEnchantlvl(int enchantlvl) {
		this.enchantlvl = enchantlvl;
	}

	public int getIs_id() {
		return is_id;
	}

	public void setIs_id(int is_id) {
		this.is_id = is_id;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public int getCharge_count() {
		return charge_count;
	}

	public void setCharge_count(int charge_count) {
		this.charge_count = charge_count;
	}

	public int getRemaining_time() {
		return remaining_time;
	}

	public void setRemaining_time(int remaining_time) {
		this.remaining_time = remaining_time;
	}

	public Timestamp getLast_used() {
		return last_used;
	}

	public void setLast_used(Timestamp last_used) {
		this.last_used = last_used;
	}

	public int getAttr_enchantlvl() {
		return attr_enchantlvl;
	}

	public void setAttr_enchantlvl(int attr_enchantlvl) {
		this.attr_enchantlvl = attr_enchantlvl;
	}

	public int getSpecial_enchant() {
		return special_enchant;
	}

	public void setSpecial_enchant(int special_enchant) {
		this.special_enchant = special_enchant;
	}

	public int getBless() {
		return bless;
	}

	public void setBless(int bless) {
		this.bless = bless;
	}

	public int getBless_level() {
		return bless_level;
	}

	public void setBless_level(int bless_level) {
		this.bless_level = bless_level;
	}

	public int getItem_level() {
		return item_level;
	}

	public void setItem_level(int item_level) {
		this.item_level = item_level;
	}

	public String getHotel_Town() {
		return Hotel_Town;
	}

	public void setHotel_Town(String hotel_Town) {
		Hotel_Town = hotel_Town;
	}

	public Timestamp getEnd_time() {
		return end_time;	
	}

	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}

	public int getCarving() {
		return carving;
	}

	public void setCarving(int carving) {
		this.carving = carving;
	}

	public int getDollpoten() {
		return dollpoten;
	}

	public void setDollpoten(int dollpoten) {
		this.dollpoten = dollpoten;
	}

	public long getDelete_time() {
		return Delete_time;
	}

	public void setDelete_time(long delete_time) {
		Delete_time = delete_time;
	}

}

package l1j.server.MJItemEnchantSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;

public class MJItemEnchanter {
	public static MJItemEnchanter newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_enchanter_id(rs.getInt("enchanter_id"))
				.set_item_id(rs.getInt("item_id"))
				.set_item_amount(rs.getInt("item_amount"))
				.set_vaporization_probability_by_millions(rs.getInt("vaporization_probability_by_millions"))
				.set_success_format_string(rs.getString("success_format_string"))
				.set_failure_format_string(rs.getString("failure_format_string"));
	}

	public static MJItemEnchanter newInstance() {
		return new MJItemEnchanter();
	}

	private int _enchanter_id;
	private int _item_id;
	private int _item_amount;
	private int _vaporization_probability_by_millions;
	private String _success_format_string;
	private String _failure_format_string;

	private MJItemEnchanter() {
	}

	public MJItemEnchanter set_enchanter_id(int enchanter_id) {
		_enchanter_id = enchanter_id;
		return this;
	}

	public int get_enchanter_id() {
		return _enchanter_id;
	}

	public MJItemEnchanter set_item_id(int item_id) {
		_item_id = item_id;
		return this;
	}

	public int get_item_id() {
		return _item_id;
	}

	public MJItemEnchanter set_item_amount(int item_amount) {
		_item_amount = item_amount;
		return this;
	}

	public int get_item_amount() {
		return _item_amount;
	}

	public MJItemEnchanter set_vaporization_probability_by_millions(int vaporization_probability_by_millions) {
		_vaporization_probability_by_millions = vaporization_probability_by_millions;
		return this;
	}

	public int get_vaporization_probability() {
		return _vaporization_probability_by_millions;
	}

	public MJItemEnchanter set_success_format_string(String success_format_string) {
		_success_format_string = success_format_string;
		return this;
	}

	public String get_success_format_string() {
		return _success_format_string;
	}

	public MJItemEnchanter set_failure_format_string(String failure_format_string) {
		_failure_format_string = failure_format_string;
		return this;
	}

	public String get_failure_format_string() {
		return _failure_format_string;
	}

	public void do_enchant(L1PcInstance pc, L1ItemInstance enchanter_item, L1ItemInstance enchantee_item, MJItemEnchantee enchantee) {
		L1Item item = enchantee.to_item_temaplte();
		L1Item reward_item = enchantee.to_reward_item_template();
		if (item == null || reward_item == null) {
			pc.sendPackets(String.format("%s已被管理員暫時停止強化。", enchantee_item.getName()));
			return;
		}
	
		if (pc.getInventory().checkAddItem(reward_item, enchantee.get_reward_item_amount()) != L1Inventory.OK) {
			pc.sendPackets("背包空間不足，無法進行強化。");
			return;
		}
	
		L1Inventory inv = pc.getInventory();
		if (MJRnd.isWinning(1000000, enchantee.get_enchant_probability_by_millions()) || pc._Blessleaf) {
			if (get_success_format_string() != null) {
				String success_message = String.format(get_success_format_string(), enchantee_item.getName());
				if (enchantee.is_broadcast()) {
					String broadcast_message = String.format("某位亞丁勇士獲得了 %s", success_message);
					L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, broadcast_message),
						new S_SystemMessage(broadcast_message),
					});
				}
				pc.sendPackets(success_message);
			}
		}
			pc._Blessleaf = false;
			pc.send_effect(enchantee.get_success_effect_id());
			inv.EnchanteestoreItem(enchantee.get_reward_item_id(), enchantee.get_reward_item_amount(), enchantee.is_carving(), true);
			inv.removeItem(enchantee_item, enchantee.get_item_amount());
			inv.removeItem(enchanter_item, get_item_amount());
		}else

	{
		if (get_failure_format_string() != null)
			pc.sendPackets(String.format(get_failure_format_string(), enchantee_item.getName()));
		pc.send_effect(enchantee.get_failure_effect_id());
		int probability = get_vaporization_probability();
		if (probability >= 1000000 || (probability > 0 && MJRnd.isWinning(1000000, probability))) {
			inv.removeItem(enchantee_item, enchantee.get_item_amount());
		}
		inv.removeItem(enchanter_item, get_item_amount());
	}
}}

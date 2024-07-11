package l1j.server.MJItemExChangeSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ItemExSelectPacket extends ServerBasePacket {
	private static AtomicInteger _select_id = new AtomicInteger(0);

	public static S_ItemExSelectPacket newInstance() {
		return new S_ItemExSelectPacket().set_id(_select_id.getAndIncrement());
	}

	private int _id;
	private int _key_item_id;
	private boolean _is_copy_level;
	private boolean _is_copy_bless;
	private boolean _is_copy_enchant;
	private boolean _is_copy_elemental;
	private boolean _is_copy_carving;
	private boolean _is_copy_doll_bonus_level;
	private boolean _is_copy_doll_bonus_value;
	private boolean _is_copy_BlessType;
	private boolean _is_copy_BlessTypeValue;
	private ArrayList<Integer> _rewards;
	private L1ItemInstance _key;
	private L1ItemInstance _target;
	private SC_WAREHOUSE_ITEM_LIST_NOTI m_packet;

	private S_ItemExSelectPacket() {
		// writeC(Opcodes.S_RETRIEVE_LIST);
	}

	public S_ItemExSelectPacket set_id(int val) {
		_id = val;
		return this;
	}

	public int get_id() {
		return _id;
	}

	public S_ItemExSelectPacket set_key_item_id(int val) {
		_key_item_id = val;
		return this;
	}

	public int get_key_item_id() {
		return _key_item_id;
	}

	public S_ItemExSelectPacket set_is_copy_bless(boolean val) {
		_is_copy_bless = val;
		return this;
	}

	public boolean get_is_copy_bless() {
		return _is_copy_bless;
	}

	/** 步數交換系統 **/
	public S_ItemExSelectPacket set_is_copy_level(boolean val) {
		_is_copy_level = val;
		return this;
	}

	public boolean get_is_copy_level() {
		return _is_copy_level;
	}

	public S_ItemExSelectPacket set_is_copy_enchant(boolean val) {
		_is_copy_enchant = val;
		return this;
	}

	public boolean get_is_copy_enchant() {
		return _is_copy_enchant;
	}

	public S_ItemExSelectPacket set_is_copy_elemental(boolean val) {
		_is_copy_elemental = val;
		return this;
	}

	public boolean get_is_copy_elemental() {
		return _is_copy_elemental;
	}

	public S_ItemExSelectPacket set_is_copy_carving(boolean val) {
		_is_copy_carving = val;
		return this;
	}

	public boolean get_is_copy_carving() {
		return _is_copy_carving;
	}

	public S_ItemExSelectPacket set_is_copy_doll_bonus_level(boolean val) {
		_is_copy_doll_bonus_level = val;
		return this;
	}

	public boolean get_is_copy_doll_bonus_level() {
		return _is_copy_doll_bonus_level;
	}

	public S_ItemExSelectPacket set_is_copy_doll_bonus_value(boolean val) {
		_is_copy_doll_bonus_value = val;
		return this;
	}

	public boolean get_is_copy_doll_bonus_value() {
		return _is_copy_doll_bonus_value;
	}

	public S_ItemExSelectPacket set_is_copy_BlessType(boolean val) {
		_is_copy_BlessType = val;
		return this;
	}

	public boolean get_is_copy_BlessType() {
		return _is_copy_BlessType;
	}

	public S_ItemExSelectPacket set_is_copy_BlessTypeValue(boolean val) {
		_is_copy_BlessTypeValue = val;
		return this;
	}

	public boolean get_is_copy_BlessTypeValue() {
		return _is_copy_BlessTypeValue;
	}

	public S_ItemExSelectPacket set_rewards(ArrayList<Integer> val) {
		_rewards = val;
		return this;
	}

	public S_ItemExSelectPacket add_rewards(Integer val) {
		_rewards.add(val);
		return this;
	}

	public ArrayList<Integer> get_rewards() {
		return _rewards;
	}

	public S_ItemExSelectPacket set_key(L1ItemInstance val) {
		_key = val;
		return this;
	}

	public L1ItemInstance get_key() {
		return _key;
	}

	public S_ItemExSelectPacket set_target(L1ItemInstance val) {
		_target = val;
		return this;
	}

	public L1ItemInstance get_target() {
		return _target;
	}

	public void do_select(L1PcInstance pc, int index) {
		if (index < 0 || index >= _rewards.size()) {
			pc.sendPackets("未知的選擇。");
			return;
		}

		if (_target.isEquipped()) {
			pc.sendPackets("無法在裝備中的物品上使用。");
			return;
		}

		boolean isAppear = true;
		L1DollInstance doll = pc.getMagicDoll();
		if (isAppear) {
			if (doll != null) {
				pc.sendPackets("在使用物品前，請先解除召喚魔法娃娃。");
				return;
			}
		}

		int reward_id = _rewards.get(index);
		L1ItemInstance reward = ItemTable.getInstance().createItem(reward_id);
		if (reward == null) {
			pc.sendPackets("目前轉換已被管理員暫時停止。");
			return;
		}

		if (get_is_copy_carving())
			reward.set_Carving(_target.get_Carving());
		if (get_is_copy_doll_bonus_level())
			reward.set_Doll_Bonus_Level(_target.get_Doll_Bonus_Level());
		if (get_is_copy_doll_bonus_value())
			reward.set_Doll_Bonus_Value(_target.get_Doll_Bonus_Value());
		if (get_is_copy_BlessType())
			reward.setBlessType(_target.getBlessType());
		if (get_is_copy_BlessTypeValue())
			reward.setBlessTypeValue(_target.getBlessTypeValue());
		if (get_is_copy_enchant())
			reward.setEnchantLevel(_target.getEnchantLevel());
		if (get_is_copy_elemental())
			reward.setAttrEnchantLevel(_target.getAttrEnchantLevel());
		reward.setIdentified(true);

		if (!pc.getInventory().consumeItem(new L1ItemInstance[] { _key, _target }, new int[] { 1, 1 })) {
			pc.sendPackets("找不到材料物品。");
			L1World.getInstance().removeObject(reward);
			return;
		}

		pc.getInventory().storeItem(reward);
		pc.sendPackets(String.format("%s 已經被交換。", reward.getLogName()));
		if (get_is_copy_bless()) {
			reward.setBless(_target.getBless());
			reward.set_bless_level(_target.get_bless_level());
			pc.getInventory().updateItem(reward, L1PcInventory.COL_BLESS_LEVEL);
			pc.getInventory().saveItem(reward, L1PcInventory.COL_BLESS_LEVEL);
			reward.setIdentified(true);
		}
		if (get_is_copy_level()) {
			reward.set_item_level(_target.get_item_level());
			pc.getInventory().updateItem(reward, L1PcInventory.COL_BLESS_LEVEL);
			pc.getInventory().saveItem(reward, L1PcInventory.COL_BLESS_LEVEL);
			reward.setIdentified(true);
		}
	}

	public S_ItemExSelectPacket serialize(L1PcInstance pc) {
		m_packet = SC_WAREHOUSE_ITEM_LIST_NOTI.create_select_item_info(this, pc.getId());
		return this;
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

	@Override
	public byte[] getBytes() {
		if (m_packet == null)
			return null;

		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(m_packet.getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE,
						MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI.toInt());
		try {
			m_packet.writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!stream.isCreated())
			stream.createProtoBytes();
		return stream.getProtoBytes();
	}

	public void dispose() {
		if (_rewards != null) {
			_rewards.clear();
			_rewards = null;
		}
		_key = null;
		_target = null;
	}

}

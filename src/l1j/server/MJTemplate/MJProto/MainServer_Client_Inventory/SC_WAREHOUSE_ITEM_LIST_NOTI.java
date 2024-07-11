package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import l1j.server.MJCTSystem.MJCTItem;
import l1j.server.MJCTSystem.MJCTSpell;
import l1j.server.MJItemExChangeSystem.S_ItemExSelectPacket;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.TJ.TJLostItemModel;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;


// TODO: 自動生成的 PROTO 代碼。由 MJSoft 製作。
public class SC_WAREHOUSE_ITEM_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	/**
	 * 發送玩家的血盟倉庫物品列表
	 *
	 * @param pc 玩家實例
	 * @param npc_object_id NPC對象ID
	 */
	public static void send_user_pledge_warehouse_items(L1PcInstance pc, int npc_object_id) {
// 如果玩家的背包已滿，則發送錯誤信息並返回
// if (pc.getInventory().getSize() >= 180){
		if (pc.getInventory().getSize() >= 200) {
			pc.sendPackets(new S_ServerMessage(263)); // 背包已滿
			return;
		}

// 獲取玩家的血盟實例
		L1Clan clan = pc.getClan();
		if (clan == null) {
			pc.sendPackets("需要加入一個血盟才能使用血盟倉庫。"); // 玩家沒有血盟
			return;
		}

// 獲取血盟倉庫實例
		ClanWarehouse whouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
// 如果倉庫內沒有物品，則發送沒有物品的提示並返回
		if (whouse.getSize() <= 0) {
			pc.sendPackets(new S_NPCTalkReturn(npc_object_id, "noitemret", null));
			return;
		}

// 檢查倉庫是否正在被其他玩家使用
		if (!whouse.setWarehouseUsingChar(pc.getId(), 0)) {
			int id = whouse.getWarehouseUsingChar();
			L1Object prev_user = L1World.getInstance().findObject(id);
			if (prev_user instanceof L1PcInstance) {
				L1PcInstance using_pc = (L1PcInstance) prev_user;
				if (using_pc.getClan() == clan) {
					pc.sendPackets(String.format("血盟成員 %s 正在使用血盟倉庫。請稍後再使用。", using_pc.getName()));
					return;
				}
			}
			if (!whouse.setWarehouseUsingChar(pc.getId(), id)) {
				pc.sendPackets(String.format("血盟成員 %s 正在使用血盟倉庫。請稍後再使用。", whouse.getName()));
				return;
			}
		}

// TODO: 在此處插入處理代碼。由 MJSoft 製作。

	}
}
		
		
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_checker(npc_object_id);
		int real_size = 0;
		for(Object obj : whouse.getItems()){
			L1ItemInstance item = (L1ItemInstance)obj;
			ItemInfo iInfo = ItemInfo.newInstance(pc, item);
			WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
			wInfo.set_index(item.getId());
			wInfo.set_item_info(iInfo);
			noti.add_item_list(wInfo);
			++real_size;
		}
		noti.set_count(real_size);
		noti.set_price(500);
		noti.set_serial_last(true);
		noti.set_warehouse_size(real_size);
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE_PLEDGE);
		pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, true);
	}
	
	public static void send_user_supplementary_service(L1PcInstance pc, int npc_object_id){
//		if (pc.getInventory().getSize() >= 180){
		if (pc.getInventory().getSize() >= 200){
			pc.sendPackets(new S_ServerMessage(263));
			return;
		}

		SupplementaryService whouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
		if(whouse.getSize() <= 0){
			pc.sendPackets(new S_ServerMessage(263));
			return;
		}
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_checker(npc_object_id);
		int real_size = 0;
		for(Object obj : whouse.getItems()){
			L1ItemInstance item = (L1ItemInstance)obj;
			ItemInfo iInfo = ItemInfo.newInstance(pc, item);
			WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
			wInfo.set_index(item.getId());
			wInfo.set_item_info(iInfo);
			noti.add_item_list(wInfo);
			++real_size;
		}
		noti.set_count(real_size);
		noti.set_price(0x00);
		noti.set_serial_last(true);
		noti.set_warehouse_size(real_size);
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE_CHAR);
		pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, true);
	}
	
	public static void send_user_warehouse_items(L1PcInstance pc, int npc_object_id){
//		if (pc.getInventory().getSize() >= 180){
		if (pc.getInventory().getSize() >= 200){
			pc.sendPackets(new S_ServerMessage(263));
			return;
		}
		
		PrivateWarehouse whouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
		if(whouse.getSize() <= 0){
			pc.sendPackets(new S_NPCTalkReturn(npc_object_id, "noitemret", null));
			return;
		}
		
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_checker(npc_object_id);
		int real_size = 0;
		for(Object obj : whouse.getItems()){
			L1ItemInstance item = (L1ItemInstance)obj;
			ItemInfo iInfo = ItemInfo.newInstance(pc, item);
			WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
			wInfo.set_index(item.getId());
			wInfo.set_item_info(iInfo);
			noti.add_item_list(wInfo);
			++real_size;
		}
		noti.set_count(real_size);
		noti.set_price(300);
		noti.set_serial_last(true);
		noti.set_warehouse_size(real_size);
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE);
		pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, true);
	}
	
	public static SC_WAREHOUSE_ITEM_LIST_NOTI createTJCoupon(List<TJLostItemModel> models) {
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_count(models.size());
		noti.set_warehouse_size(models.size());
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE_CONTRACT);
		noti.set_price(0);
		noti.set_serial_last(true);

		for(TJLostItemModel model : models) {
			L1Item template = ItemTable.getInstance().getTemplate(model.itemId());
			if(template == null) {
				continue;
			}
			L1ItemInstance item = new L1ItemInstance();
			item.setId(model.itemObjectId());
			item.setItem(template);
			item.setBless(model.instanceBless());
			item.set_bless_level(model.instanceCustomBless());
			item.setIdentified(true);
			item.setCount(1);
			item.setEnchantLevel(model.enchant());
			item.setAttrEnchantLevel(model.elementalEnchant());
			ItemInfo iInfo = null;
			try {
				iInfo = ItemInfo.newInstance(null, item, item.getItem().getUseType(), L1ItemInstance.to_simple_description(item).getBytes("MS949"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			iInfo.set_icon_id(template.getGfxId());
			WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
			wInfo.set_index(model.itemObjectId());
			wInfo.set_item_info(iInfo);
			noti.add_item_list(wInfo);
		}
		return noti;
	}
	
	public static SC_WAREHOUSE_ITEM_LIST_NOTI create_ct_sp_info(ArrayDeque<MJCTSpell> spQ){
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_checker(1);
		int real_size = 0;

		L1ItemInstance item = ItemTable.getInstance().createItem(40005);
		if(spQ != null){
			while(!spQ.isEmpty()){
				MJCTSpell ct_spell = spQ.poll();
				item.setId(item.getId());
				item.setBless(item.getItem().getType2());
				item.setIdentified(true);
				item.setCount(1);
				ItemInfo iInfo = null;
				try {
					iInfo = ItemInfo.newInstance(null, item, item.getItem().getUseType(), ct_spell.name.toString().getBytes("MS949"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				iInfo.set_icon_id(ct_spell.xicon);
				WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
				wInfo.set_index(item.getId());
				wInfo.set_item_info(iInfo);
				noti.add_item_list(wInfo);
				++real_size;
			}
		}
		noti.set_count(real_size);
		noti.set_price(0);
		noti.set_serial_last(true);
		noti.set_warehouse_size(real_size);
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE_PLEDGE);
		return noti;
	}
	
	public static SC_WAREHOUSE_ITEM_LIST_NOTI create_select_item_info(S_ItemExSelectPacket packet, int id) {
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_checker(id);
		int real_size = 0;
		ArrayList<Integer> rewards = packet.get_rewards();
		L1ItemInstance target = packet.get_target();
		if(rewards != null && rewards.size() > 0) {
			int size = rewards.size();
			for(int i=0; i<size; ++i) {
				L1Item item_tamplate = ItemTable.getInstance().getTemplate(rewards.get(i));
				if(item_tamplate == null) {
					item_tamplate = ItemTable.getInstance().getTemplate(40005);
				}
				L1ItemInstance item = new L1ItemInstance();
				item.setItem(item_tamplate);
				item.setId(i);
				item.setBless(packet.get_is_copy_bless() ? target.getBless() : 1);
				item.setIdentified(true);
				item.setCount(1);
				item.setEnchantLevel(packet.get_is_copy_enchant() ? target.getEnchantLevel() : 0);
				item.setAttrEnchantLevel(packet.get_is_copy_elemental() ? target.getAttrEnchantLevel() : 0);
				item.set_item_level(packet.get_is_copy_level() ? target.get_item_level() : 0);
				item.set_Carving(packet.get_is_copy_carving() ? target.get_Carving() : 0);
				item.set_Doll_Bonus_Level(packet.get_is_copy_doll_bonus_level() ? target.get_Doll_Bonus_Level() : 0);
				item.set_Doll_Bonus_Value(packet.get_is_copy_doll_bonus_value() ? target.get_Doll_Bonus_Value() : 0);
				item.setBlessType(packet.get_is_copy_BlessType() ? target.getBlessType() : 0);
				item.setBlessTypeValue(packet.get_is_copy_BlessTypeValue() ? target.getBlessTypeValue() : 0);

		ItemInfo iInfo = null;
		try {
// 根據物品的雕刻狀態獲取物品名稱
		String item_name = item.get_Carving() > 0 ? L1ItemInstance.to_simple_description(item) + "(雕刻)" : L1ItemInstance.to_simple_description(item);

// 使用 MS949 字符編碼將物品名稱轉換為字節數組
		iInfo = ItemInfo.newInstance(null, item, item_tamplate.getUseType(), item_name.getBytes("MS949"));
		} catch (UnsupportedEncodingException e) {
// 捕獲並打印不支持的編碼異常
		e.printStackTrace();
		}
				WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
				wInfo.set_index(item.getId());
				wInfo.set_item_info(iInfo);
				noti.add_item_list(wInfo);
				++real_size;
			}
		}
		noti.set_count(real_size);
		noti.set_price(0);
		noti.set_serial_last(true);
		noti.set_warehouse_size(real_size);
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE_ELVEN);
		return noti;
		
	}
	
	public static SC_WAREHOUSE_ITEM_LIST_NOTI create_ct_item_info(ArrayDeque<MJCTItem> itemQ){
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_checker(1);
		int real_size = 0;
		
		if(itemQ != null){
			while(!itemQ.isEmpty()){
				MJCTItem ct_item = itemQ.poll();
				L1Item temp = ct_item.item;
				L1ItemInstance item = new L1ItemInstance();
				item.setItem(temp);

				item.setId(ct_item.id);
				item.setBless(ct_item.bless);
				item.setIdentified(ct_item.iden != 0);
				item.setCount(ct_item.count);
				temp.setGfxId(temp.getGfxId());
				ItemInfo iInfo = null;
				try {
					iInfo = ItemInfo.newInstance(null, item, temp.getUseType(), ct_item.toString().getBytes("MS949"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
				wInfo.set_index(item.getId());
				wInfo.set_item_info(iInfo);
				noti.add_item_list(wInfo);
				++real_size;
			}
		}
		noti.set_count(real_size);
		noti.set_price(0);
		noti.set_serial_last(true);
		noti.set_warehouse_size(real_size);
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE_PLEDGE);
		return noti;
	}
	
	public static SC_WAREHOUSE_ITEM_LIST_NOTI newDisplayInstance(int object_id, int index, int start, int count, boolean is_serial_last){
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance();
		noti.set_checker(object_id);
		int real_size = 0;
		for(int i=0; i<count; ++i){
			L1Item temp = ItemTable.getInstance().getTemplate(40005);
			L1ItemInstance item = new L1ItemInstance();
			int real_number = start + i;
			int real_index = index + i;
			item.setId(real_index);
			item.setItem(temp);
			item.setBless(temp.getBless());
			item.setIdentified(false);
			item.setCount(real_number);
			temp.setGfxId(real_number);
			temp.setName(String.valueOf(real_number));
			temp.setNameId(String.valueOf(real_number));
			
			ItemInfo iInfo = ItemInfo.newInstance(null, item);
			
			WarehouseItemInfo wInfo = WarehouseItemInfo.newInstance();
			wInfo.set_index(real_index);
			wInfo.set_item_info(iInfo);
			noti.add_item_list(wInfo);
			++real_size;
		}
		noti.set_count(real_size);
		noti.set_price(300);
		noti.set_serial_last(is_serial_last);
		noti.set_warehouse_size(real_size);
		noti.set_warehouse_type(eWarehouseType.TRADE_RETRIEVE);
		return noti;
	}
	
	public static SC_WAREHOUSE_ITEM_LIST_NOTI newInstance(){
		return new SC_WAREHOUSE_ITEM_LIST_NOTI();
	}
	private int _checker;
	private int _count;
	private eWarehouseType _warehouse_type;
	private int _price;
	private int _warehouse_size;
	private java.util.LinkedList<WarehouseItemInfo> _item_list;
	private boolean _serial_last;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_WAREHOUSE_ITEM_LIST_NOTI(){
	}
	public int get_checker(){
		return _checker;
	}
	public void set_checker(int val){
		_bit |= 0x1;
		_checker = val;
	}
	public boolean has_checker(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x2;
		_count = val;
	}
	public boolean has_count(){
		return (_bit & 0x2) == 0x2;
	}
	public eWarehouseType get_warehouse_type(){
		return _warehouse_type;
	}
	public void set_warehouse_type(eWarehouseType val){
		_bit |= 0x4;
		_warehouse_type = val;
	}
	public boolean has_warehouse_type(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_price(){
		return _price;
	}
	public void set_price(int val){
		_bit |= 0x8;
		_price = val;
	}
	public boolean has_price(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_warehouse_size(){
		return _warehouse_size;
	}
	public void set_warehouse_size(int val){
		_bit |= 0x10;
		_warehouse_size = val;
	}
	public boolean has_warehouse_size(){
		return (_bit & 0x10) == 0x10;
	}
	public java.util.LinkedList<WarehouseItemInfo> get_item_list(){
		return _item_list;
	}
	public void add_item_list(WarehouseItemInfo val){
		if(!has_item_list()){
			_item_list = new java.util.LinkedList<WarehouseItemInfo>();
			_bit |= 0x20;
		}
		_item_list.add(val);
	}
	public boolean has_item_list(){
		return (_bit & 0x20) == 0x20;
	}
	public boolean get_serial_last(){
		return _serial_last;
	}
	public void set_serial_last(boolean val){
		_bit |= 0x40;
		_serial_last = val;
	}
	public boolean has_serial_last(){
		return (_bit & 0x40) == 0x40;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_checker())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _checker);
		if (has_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
		if (has_warehouse_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _warehouse_type.toInt());
		if (has_price())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _price);
		if (has_warehouse_size())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _warehouse_size);
		if (has_item_list()){
			for(WarehouseItemInfo val : _item_list)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
		}
		if (has_serial_last())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _serial_last);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_checker()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_warehouse_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_price()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_item_list()){
			for(WarehouseItemInfo val : _item_list){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_checker()){
			output.wirteInt32(1, _checker);
		}
		if (has_count()){
			output.wirteInt32(2, _count);
		}
		if (has_warehouse_type()){
			output.writeEnum(3, _warehouse_type.toInt());
		}
		if (has_price()){
			output.wirteInt32(4, _price);
		}
		if (has_warehouse_size()){
			output.wirteInt32(5, _warehouse_size);
		}
		if (has_item_list()){
			for(WarehouseItemInfo val : _item_list){
				output.writeMessage(6, val);
			}
		}
		if (has_serial_last()){
			output.writeBool(7, _serial_last);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x00000008:{
					set_checker(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_count(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_warehouse_type(eWarehouseType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020:{
					set_price(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_warehouse_size(input.readInt32());
					break;
				}
				case 0x00000032:{
					add_item_list((WarehouseItemInfo)input.readMessage(WarehouseItemInfo.newInstance()));
					break;
				}
				case 0x00000038:{
					set_serial_last(input.readBool());
					break;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new SC_WAREHOUSE_ITEM_LIST_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_list()){
			for(WarehouseItemInfo val : _item_list)
				val.dispose();
			_item_list.clear();
			_item_list = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class WarehouseItemInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static WarehouseItemInfo newInstance(){
			return new WarehouseItemInfo();
		}
		private int _index;
		private ItemInfo _item_info;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private WarehouseItemInfo(){
		}
		public int get_index(){
			return _index;
		}
		public void set_index(int val){
			_bit |= 0x1;
			_index = val;
		}
		public boolean has_index(){
			return (_bit & 0x1) == 0x1;
		}
		public ItemInfo get_item_info(){
			return _item_info;
		}
		public void set_item_info(ItemInfo val){
			_bit |= 0x2;
			_item_info = val;
		}
		public boolean has_item_info(){
			return (_bit & 0x2) == 0x2;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_index())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
			if (has_item_info())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _item_info);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_index()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_info()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_index()){
				output.wirteInt32(1, _index);
			}
			if (has_item_info()){
				output.writeMessage(2, _item_info);
			}
		}
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try{
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					default:{
						return this;
					}
					case 0x00000008:{
						set_index(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_item_info((ItemInfo)input.readMessage(ItemInfo.newInstance()));
						break;
					}
				}
			}
			return this;
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try{
				readFrom(is);

				if (!isInitialized())
					return this;
				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch(Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public MJIProtoMessage copyInstance(){
			return new WarehouseItemInfo();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_item_info() && _item_info != null){
				_item_info.dispose();
				_item_info = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public enum eWarehouseType{
		TRADE_RETRIEVE(3),
		TRADE_RETRIEVE_PLEDGE(5),
		TRADE_RETRIEVE_ELVEN(9),
		TRADE_RETRIEVE_CHAR(18),
		TRADE_RETRIEVE_CONTRACT(20);
		private int value;
		eWarehouseType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eWarehouseType v){
			return value == v.value;
		}
		public static eWarehouseType fromInt(int i){
			switch(i){
			case 3:
				return TRADE_RETRIEVE;
			case 5:
				return TRADE_RETRIEVE_PLEDGE;
			case 9:
				return TRADE_RETRIEVE_ELVEN;
			case 18:
				return TRADE_RETRIEVE_CHAR;
			case 20:
				return TRADE_RETRIEVE_CONTRACT;
			default:
				return null;
			}
		}
	}
}

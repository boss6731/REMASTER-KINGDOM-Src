package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.datatables.ItemSelectorTable.SelectorData;
import l1j.server.server.templates.L1Item;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send (L1PcInstance pc, L1ItemInstance item) {
		SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI noti = SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.newInstance();
		noti.set_bag_obj_id(item.getId());
		noti.set_bag_name_id(item.getItemDescId());
//		System.out.println(item.getId());

		L1Item temp = null;
		int useitemId = item.getItemId();

		if (ItemSelectorTable.getSelectorInfo(useitemId) != null){
			for(SelectorData data : ItemSelectorTable.getSelectorInfo(useitemId)){
				ItemInfo info = ItemInfo.newInstance();
//				System.out.println(data._selectItemId);
//				System.out.println(data._itemId);
//				System.out.println(data._enchantLevel);
				temp = ItemTable.getInstance().getTemplate(data._selectItemId);
				if(temp == null){
					System.out.println("物品信息為空的物品ID： " + data._selectItemId);
					continue;
				}
				info.set_name_id(temp.getItemDescId()); //0x08
//				info.set_elemental_enchant_type(data._attrType); //0x10
				info.set_elemental_enchant_value(data._attrLevel); //0x18
				info.set_enchanct(data._enchantLevel); //0x20
				info.set_item_count(data._count); // 0x28
				noti.add_item_infos(info);
			}
		} else {
			System.out.println(ItemSelectorTable.getSelectorInfo(useitemId));
		}


		pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI), true);
	}
	
	public static SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI newInstance(){
		return new SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI();
	}
	private java.util.LinkedList<SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo> _item_infos;
	private int _bag_obj_id;
	private int _bag_name_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI(){
	}
	public java.util.LinkedList<SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo> get_item_infos(){
		return _item_infos;
	}
	public void add_item_infos(SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo val){
		if(!has_item_infos()){
			_item_infos = new java.util.LinkedList<SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo>();
			_bit |= 0x1;
		}
		_item_infos.add(val);
	}
	public boolean has_item_infos(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_bag_obj_id(){
		return _bag_obj_id;
	}
	public void set_bag_obj_id(int val){
		_bit |= 0x2;
		_bag_obj_id = val;
	}
	public boolean has_bag_obj_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_bag_name_id(){
		return _bag_name_id;
	}
	public void set_bag_name_id(int val){
		_bit |= 0x4;
		_bag_name_id = val;
	}
	public boolean has_bag_name_id(){
		return (_bit & 0x4) == 0x4;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_item_infos()){
			for(SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo val : _item_infos){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_bag_obj_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _bag_obj_id);
		}
		if (has_bag_name_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _bag_name_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_item_infos()){
			for(SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo val : _item_infos){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_bag_obj_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bag_name_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_item_infos()){
			for (SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo val : _item_infos){
				output.writeMessage(1, val);
			}
		}
		if (has_bag_obj_id()){
			output.wirteInt32(2, _bag_obj_id);
		}
		if (has_bag_name_id()){
			output.wirteInt32(3, _bag_name_id);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					add_item_infos((SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo)input.readMessage(SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI.ItemInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_bag_obj_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_bag_name_id(input.readInt32());
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_ITEMS_NAME_ID_IN_SELECTION_BAG_NOTI();
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class ItemInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static ItemInfo newInstance(){
			return new ItemInfo();
		}
		private int _name_id;
		private int _elemental_enchant_type;
		private int _elemental_enchant_value;
		private int _enchanct;
		private int _item_count;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ItemInfo(){
		}
		public int get_name_id(){
			return _name_id;
		}
		public void set_name_id(int val){
			_bit |= 0x1;
			_name_id = val;
		}
		public boolean has_name_id(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_elemental_enchant_type(){
			return _elemental_enchant_type;
		}
		public void set_elemental_enchant_type(int val){
			_bit |= 0x2;
			_elemental_enchant_type = val;
		}
		public boolean has_elemental_enchant_type(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_elemental_enchant_value(){
			return _elemental_enchant_value;
		}
		public void set_elemental_enchant_value(int val){
			_bit |= 0x4;
			_elemental_enchant_value = val;
		}
		public boolean has_elemental_enchant_value(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_enchanct(){
			return _enchanct;
		}
		public void set_enchanct(int val){
			_bit |= 0x8;
			_enchanct = val;
		}
		public boolean has_enchanct(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_item_count(){
			return _item_count;
		}
		public void set_item_count(int val){
			_bit |= 0x10;
			_item_count = val;
		}
		public boolean has_item_count(){
			return (_bit & 0x10) == 0x10;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_name_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
			}
			if (has_elemental_enchant_type()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _elemental_enchant_type);
			}
			if (has_elemental_enchant_value()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _elemental_enchant_value);
			}
			if (has_enchanct()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _enchanct);
			}
			if (has_item_count()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _item_count);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_name_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_name_id()){
				output.wirteInt32(1, _name_id);
			}
			if (has_elemental_enchant_type()){
				output.wirteInt32(2, _elemental_enchant_type);
			}
			if (has_elemental_enchant_value()){
				output.wirteInt32(3, _elemental_enchant_value);
			}
			if (has_enchanct()){
				output.wirteInt32(4, _enchanct);
			}
			if (has_item_count()){
				output.wirteInt32(5, _item_count);
			}
		}
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try{
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_name_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_elemental_enchant_type(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_elemental_enchant_value(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_enchanct(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_item_count(input.readInt32());
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try{
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null){
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

			} catch (Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
			return new ItemInfo();
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}

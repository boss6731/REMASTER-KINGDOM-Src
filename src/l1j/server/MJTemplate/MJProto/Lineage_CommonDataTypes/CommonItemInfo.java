package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import com.sun.prism.Material;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CommonItemInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CommonItemInfo newInstance(){
		return new CommonItemInfo();
	}
	private int _name_id;
	private int _icon_id;
	private int _sprite_id;
	private String _desc;
	private String _real_desc;
	private Material _material;
	private int _weight_1000ea;
	private int _level_limit_min;
	private int _level_limit_max;
	private java.util.LinkedList<CharacterClass> _class_permit;
	private java.util.LinkedList<CommonBonusDescription> _equip_bonus_list;
	private int _interaction_type;
	private CommonItemInfo.eItemCategory _item_category;
	private BodyPart _body_part;
	private int _ac;
	private ExtendedWeaponType _extended_weapon_type;
	private int _large_damage;
	private int _small_damage;
	private int _hit_bonus;
	private int _damage_bonus;
	private CommonItemInfo.ArmorSeriesInfo _armor_series_info;
	private int _cost;
	private boolean _can_set_mage_enchant;
	private boolean _merge;
	private boolean _pss_event_item;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private long _bit;
	private CommonItemInfo(){
	}
	public int get_name_id(){
		return _name_id;
	}
	public void set_name_id(int val){
		_bit |= 0x1L;
		_name_id = val;
	}
	public boolean has_name_id(){
		return (_bit & 0x1L) == 0x1L;
	}
	public int get_icon_id(){
		return _icon_id;
	}
	public void set_icon_id(int val){
		_bit |= 0x2L;
		_icon_id = val;
	}
	public boolean has_icon_id(){
		return (_bit & 0x2L) == 0x2L;
	}
	public int get_sprite_id(){
		return _sprite_id;
	}
	public void set_sprite_id(int val){
		_bit |= 0x4L;
		_sprite_id = val;
	}
	public boolean has_sprite_id(){
		return (_bit & 0x4L) == 0x4L;
	}
	public String get_desc(){
		return _desc;
	}
	public void set_desc(String val){
		_bit |= 0x8L;
		_desc = val;
	}
	public boolean has_desc(){
		return (_bit & 0x8L) == 0x8L;
	}
	public String get_real_desc(){
		return _real_desc;
	}
	public void set_real_desc(String val){
		_bit |= 0x10L;
		_real_desc = val;
	}
	public boolean has_real_desc(){
		return (_bit & 0x10L) == 0x10L;
	}
	public Material get_material(){
		return _material;
	}
	public void set_material(Material val){
		_bit |= 0x20L;
		_material = val;
	}
	public boolean has_material(){
		return (_bit & 0x20L) == 0x20L;
	}
	public int get_weight_1000ea(){
		return _weight_1000ea;
	}
	public void set_weight_1000ea(int val){
		_bit |= 0x40L;
		_weight_1000ea = val;
	}
	public boolean has_weight_1000ea(){
		return (_bit & 0x40L) == 0x40L;
	}
	public int get_level_limit_min(){
		return _level_limit_min;
	}
	public void set_level_limit_min(int val){
		_bit |= 0x80L;
		_level_limit_min = val;
	}
	public boolean has_level_limit_min(){
		return (_bit & 0x80L) == 0x80L;
	}
	public int get_level_limit_max(){
		return _level_limit_max;
	}
	public void set_level_limit_max(int val){
		_bit |= 0x100L;
		_level_limit_max = val;
	}
	public boolean has_level_limit_max(){
		return (_bit & 0x100L) == 0x100L;
	}
	public java.util.LinkedList<CharacterClass> get_class_permit(){
		return _class_permit;
	}
	public void add_class_permit(CharacterClass val){
		if(!has_class_permit()){
			_class_permit = new java.util.LinkedList<CharacterClass>();
			_bit |= 0x200L;
		}
		_class_permit.add(val);
	}
	public boolean has_class_permit(){
		return (_bit & 0x200L) == 0x200L;
	}
	public java.util.LinkedList<CommonBonusDescription> get_equip_bonus_list(){
		return _equip_bonus_list;
	}
	public void add_equip_bonus_list(CommonBonusDescription val){
		if(!has_equip_bonus_list()){
			_equip_bonus_list = new java.util.LinkedList<CommonBonusDescription>();
			_bit |= 0x400L;
		}
		_equip_bonus_list.add(val);
	}
	public boolean has_equip_bonus_list(){
		return (_bit & 0x400L) == 0x400L;
	}
	public int get_interaction_type(){
		return _interaction_type;
	}
	public void set_interaction_type(int val){
		_bit |= 0x800L;
		_interaction_type = val;
	}
	public boolean has_interaction_type(){
		return (_bit & 0x800L) == 0x800L;
	}
	public CommonItemInfo.eItemCategory get_item_category(){
		return _item_category;
	}
	public void set_item_category(CommonItemInfo.eItemCategory val){
		_bit |= 0x40000L;
		_item_category = val;
	}
	public boolean has_item_category(){
		return (_bit & 0x40000L) == 0x40000L;
	}
	public BodyPart get_body_part(){
		return _body_part;
	}
	public void set_body_part(BodyPart val){
		_bit |= 0x80000L;
		_body_part = val;
	}
	public boolean has_body_part(){
		return (_bit & 0x80000L) == 0x80000L;
	}
	public int get_ac(){
		return _ac;
	}
	public void set_ac(int val){
		_bit |= 0x100000L;
		_ac = val;
	}
	public boolean has_ac(){
		return (_bit & 0x100000L) == 0x100000L;
	}
	public ExtendedWeaponType get_extended_weapon_type(){
		return _extended_weapon_type;
	}
	public void set_extended_weapon_type(ExtendedWeaponType val){
		_bit |= 0x20000000L;
		_extended_weapon_type = val;
	}
	public boolean has_extended_weapon_type(){
		return (_bit & 0x20000000L) == 0x20000000L;
	}
	public int get_large_damage(){
		return _large_damage;
	}
	public void set_large_damage(int val){
		_bit |= 0x40000000L;
		_large_damage = val;
	}
	public boolean has_large_damage(){
		return (_bit & 0x40000000L) == 0x40000000L;
	}
	public int get_small_damage(){
		return _small_damage;
	}
	public void set_small_damage(int val){
		_bit |= 0x80000000L;
		_small_damage = val;
	}
	public boolean has_small_damage(){
		return (_bit & 0x80000000L) == 0x80000000L;
	}
	public int get_hit_bonus(){
		return _hit_bonus;
	}
	public void set_hit_bonus(int val){
		_bit |= 0x100000000L;
		_hit_bonus = val;
	}
	public boolean has_hit_bonus(){
		return (_bit & 0x100000000L) == 0x100000000L;
	}
	public int get_damage_bonus(){
		return _damage_bonus;
	}
	public void set_damage_bonus(int val){
		_bit |= 0x200000000L;
		_damage_bonus = val;
	}
	public boolean has_damage_bonus(){
		return (_bit & 0x200000000L) == 0x200000000L;
	}
	public CommonItemInfo.ArmorSeriesInfo get_armor_series_info(){
		return _armor_series_info;
	}
	public void set_armor_series_info(CommonItemInfo.ArmorSeriesInfo val){
		_bit |= 0x8000000000L;
		_armor_series_info = val;
	}
	public boolean has_armor_series_info(){
		return (_bit & 0x8000000000L) == 0x8000000000L;
	}
	public int get_cost(){
		return _cost;
	}
	public void set_cost(int val){
		_bit |= 0x10000000000L;
		_cost = val;
	}
	public boolean has_cost(){
		return (_bit & 0x10000000000L) == 0x10000000000L;
	}
	public boolean get_can_set_mage_enchant(){
		return _can_set_mage_enchant;
	}
	public void set_can_set_mage_enchant(boolean val){
		_bit |= 0x20000000000L;
		_can_set_mage_enchant = val;
	}
	public boolean has_can_set_mage_enchant(){
		return (_bit & 0x20000000000L) == 0x20000000000L;
	}
	public boolean get_merge(){
		return _merge;
	}
	public void set_merge(boolean val){
		_bit |= 0x40000000000L;
		_merge = val;
	}
	public boolean has_merge(){
		return (_bit & 0x40000000000L) == 0x40000000000L;
	}
	public boolean get_pss_event_item(){
		return _pss_event_item;
	}
	public void set_pss_event_item(boolean val){
		_bit |= 0x80000000000L;
		_pss_event_item = val;
	}
	public boolean has_pss_event_item(){
		return (_bit & 0x80000000000L) == 0x80000000000L;
	}
	@Override
	public long getInitializeBit(){
		return _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_name_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _name_id);
		}
		if (has_icon_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _icon_id);
		}
		if (has_sprite_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _sprite_id);
		}
		if (has_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _desc);
		}
		if (has_real_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _real_desc);
		}
//		if (has_material()){
//			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _material.toInt());
//		}
		if (has_weight_1000ea()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _weight_1000ea);
		}
		if (has_level_limit_min()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _level_limit_min);
		}
		if (has_level_limit_max()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _level_limit_max);
		}
		if (has_class_permit()){
			for(CharacterClass val : _class_permit){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(10, val.toInt());
			}
		}
		if (has_equip_bonus_list()){
			for(CommonBonusDescription val : _equip_bonus_list){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(11, val);
			}
		}
		if (has_interaction_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(12, _interaction_type);
		}
		if (has_item_category()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(19, _item_category.toInt());
		}
		if (has_body_part()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(20, _body_part.toInt());
		}
		if (has_ac()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(21, _ac);
		}
		if (has_extended_weapon_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(30, _extended_weapon_type.toInt());
		}
		if (has_large_damage()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(31, _large_damage);
		}
		if (has_small_damage()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(32, _small_damage);
		}
		if (has_hit_bonus()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(33, _hit_bonus);
		}
		if (has_damage_bonus()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(34, _damage_bonus);
		}
		if (has_armor_series_info()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(40, _armor_series_info);
		}
		if (has_cost()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(41, _cost);
		}
		if (has_can_set_mage_enchant()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(42, _can_set_mage_enchant);
		}
		if (has_merge()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(43, _merge);
		}
		if (has_pss_event_item()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(44, _pss_event_item);
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
//		if (has_class_permit()){
//			for(CharacterClass val : _class_permit){
//				if (!val.isInitialized()){
//					_memorizedIsInitialized = -1;
//					return false;
//				}
//			}
//		}
		if (has_equip_bonus_list()){
			for(CommonBonusDescription val : _equip_bonus_list){
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_name_id()){
			output.writeUInt32(1, _name_id);
		}
		if (has_icon_id()){
			output.writeUInt32(2, _icon_id);
		}
		if (has_sprite_id()){
			output.writeUInt32(3, _sprite_id);
		}
		if (has_desc()){
			output.writeString(4, _desc);
		}
		if (has_real_desc()){
			output.writeString(5, _real_desc);
		}
//		if (has_material()){
//			output.writeEnum(6, _material.toInt());
//		}
		if (has_weight_1000ea()){
			output.writeUInt32(7, _weight_1000ea);
		}
		if (has_level_limit_min()){
			output.writeUInt32(8, _level_limit_min);
		}
		if (has_level_limit_max()){
			output.writeUInt32(9, _level_limit_max);
		}
		if (has_class_permit()){
			for (CharacterClass val : _class_permit){
				output.writeEnum(10, val.toInt());
			}
		}
		if (has_equip_bonus_list()){
			for (CommonBonusDescription val : _equip_bonus_list){
				output.writeMessage(11, val);
			}
		}
		if (has_interaction_type()){
			output.writeUInt32(12, _interaction_type);
		}
		if (has_item_category()){
			output.writeEnum(19, _item_category.toInt());
		}
		if (has_body_part()){
			output.writeEnum(20, _body_part.toInt());
		}
		if (has_ac()){
			output.writeUInt32(21, _ac);
		}
		if (has_extended_weapon_type()){
			output.writeEnum(30, _extended_weapon_type.toInt());
		}
		if (has_large_damage()){
			output.writeUInt32(31, _large_damage);
		}
		if (has_small_damage()){
			output.writeUInt32(32, _small_damage);
		}
		if (has_hit_bonus()){
			output.writeUInt32(33, _hit_bonus);
		}
		if (has_damage_bonus()){
			output.writeUInt32(34, _damage_bonus);
		}
		if (has_armor_series_info()){
			output.writeMessage(40, _armor_series_info);
		}
		if (has_cost()){
			output.wirteInt32(41, _cost);
		}
		if (has_can_set_mage_enchant()){
			output.writeBool(42, _can_set_mage_enchant);
		}
		if (has_merge()){
			output.writeBool(43, _merge);
		}
		if (has_pss_event_item()){
			output.writeBool(44, _pss_event_item);
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
					set_name_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_icon_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_sprite_id(input.readUInt32());
					break;
				}
				case 0x00000022:{
					set_desc(input.readString());
					break;
				}
				case 0x0000002A:{
					set_real_desc(input.readString());
					break;
				}
//				case 0x00000030:{
//					set_material(Material.fromInt(input.readEnum()));
//					break;
//				}
				case 0x00000038:{
					set_weight_1000ea(input.readUInt32());
					break;
				}
				case 0x00000040:{
					set_level_limit_min(input.readUInt32());
					break;
				}
				case 0x00000048:{
					set_level_limit_max(input.readUInt32());
					break;
				}
				case 0x00000050:{
					add_class_permit(CharacterClass.fromInt(input.readEnum()));
					break;
				}
				case 0x0000005A:{
					add_equip_bonus_list((CommonBonusDescription)input.readMessage(CommonBonusDescription.newInstance()));
					break;
				}
				case 0x00000060:{
					set_interaction_type(input.readUInt32());
					break;
				}
				case 0x00000098:{
					set_item_category(CommonItemInfo.eItemCategory.fromInt(input.readEnum()));
					break;
				}
				case 0x000000A0:{
					set_body_part(BodyPart.fromInt(input.readEnum()));
					break;
				}
				case 0x000000A8:{
					set_ac(input.readUInt32());
					break;
				}
				case 0x000000F0:{
					set_extended_weapon_type(ExtendedWeaponType.fromInt(input.readEnum()));
					break;
				}
				case 0x000000F8:{
					set_large_damage(input.readUInt32());
					break;
				}
				case 0x00000100:{
					set_small_damage(input.readUInt32());
					break;
				}
				case 0x00000108:{
					set_hit_bonus(input.readUInt32());
					break;
				}
				case 0x00000110:{
					set_damage_bonus(input.readUInt32());
					break;
				}
				case 0x00000142:{
					set_armor_series_info((CommonItemInfo.ArmorSeriesInfo)input.readMessage(CommonItemInfo.ArmorSeriesInfo.newInstance()));
					break;
				}
				case 0x00000148:{
					set_cost(input.readInt32());
					break;
				}
				case 0x00000150:{
					set_can_set_mage_enchant(input.readBool());
					break;
				}
				case 0x00000158:{
					set_merge(input.readBool());
					break;
				}
				case 0x00000160:{
					set_pss_event_item(input.readBool());
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
		return new CommonItemInfo();
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
	public static class ArmorSeriesInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static ArmorSeriesInfo newInstance(){
			return new ArmorSeriesInfo();
		}
		private int _series_number;
		private boolean _is_main;
		private java.util.LinkedList<CommonBonusDescription> _series_bonus_list;
		private int _series_part;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ArmorSeriesInfo(){
		}
		public int get_series_number(){
			return _series_number;
		}
		public void set_series_number(int val){
			_bit |= 0x1;
			_series_number = val;
		}
		public boolean has_series_number(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_is_main(){
			return _is_main;
		}
		public void set_is_main(boolean val){
			_bit |= 0x2;
			_is_main = val;
		}
		public boolean has_is_main(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<CommonBonusDescription> get_series_bonus_list(){
			return _series_bonus_list;
		}
		public void add_series_bonus_list(CommonBonusDescription val){
			if(!has_series_bonus_list()){
				_series_bonus_list = new java.util.LinkedList<CommonBonusDescription>();
				_bit |= 0x4;
			}
			_series_bonus_list.add(val);
		}
		public boolean has_series_bonus_list(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_series_part(){
			return _series_part;
		}
		public void set_series_part(int val){
			_bit |= 0x8;
			_series_part = val;
		}
		public boolean has_series_part(){
			return (_bit & 0x8) == 0x8;
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
			if (has_series_number()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _series_number);
			}
			if (has_is_main()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_main);
			}
			if (has_series_bonus_list()){
				for(CommonBonusDescription val : _series_bonus_list){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_series_part()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _series_part);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_series_number()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_is_main()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_series_bonus_list()){
				for(CommonBonusDescription val : _series_bonus_list){
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
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_series_number()){
				output.writeUInt32(1, _series_number);
			}
			if (has_is_main()){
				output.writeBool(2, _is_main);
			}
			if (has_series_bonus_list()){
				for (CommonBonusDescription val : _series_bonus_list){
					output.writeMessage(3, val);
				}
			}
			if (has_series_part()){
				output.wirteInt32(4, _series_part);
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
						set_series_number(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_is_main(input.readBool());
						break;
					}
					case 0x0000001A:{
						add_series_bonus_list((CommonBonusDescription)input.readMessage(CommonBonusDescription.newInstance()));
						break;
					}
					case 0x00000020:{
						set_series_part(input.readInt32());
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
			return new ArmorSeriesInfo();
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
	public enum eItemCategory{
		WEAPON(1),
		ARMOR(19),
		FOOD(21),
		LIGHT(22),
		ITEM(23),
		POTION(1000),
		POTION_HEAL(1001),
		SCROLL_TELEPORT_TOWN(1002),
		SCROLL_TELEPORT_HOME(1003),
		SCROLL(1004),
		ARMOR_SERIES(1005),
		ARMOR_SERIES_MAIN(1006),
		WAND_CALL_LIGHTNING(1007),
		LUCKY_BAG(1008),
		POTION_MANA(1009),
		ARROW(1010),
		;
		private int value;
		eItemCategory(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eItemCategory v){
			return value == v.value;
		}
		public static eItemCategory fromInt(int i){
			switch(i){
			case 1:
				return WEAPON;
			case 19:
				return ARMOR;
			case 21:
				return FOOD;
			case 22:
				return LIGHT;
			case 23:
				return ITEM;
			case 1000:
				return POTION;
			case 1001:
				return POTION_HEAL;
			case 1002:
				return SCROLL_TELEPORT_TOWN;
			case 1003:
				return SCROLL_TELEPORT_HOME;
			case 1004:
				return SCROLL;
			case 1005:
				return ARMOR_SERIES;
			case 1006:
				return ARMOR_SERIES_MAIN;
			case 1007:
				return WAND_CALL_LIGHTNING;
			case 1008:
				return LUCKY_BAG;
			case 1009:
				return POTION_MANA;
			case 1010:
				return ARROW;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eItemCategory, %d", i));
			}
		}
	}
}

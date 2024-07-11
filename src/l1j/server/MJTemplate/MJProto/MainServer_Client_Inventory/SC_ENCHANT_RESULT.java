package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_ENCHANT_RESULT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, L1ItemInstance item, SC_ENCHANT_RESULT.eResult eResult, boolean isBm, int amount, int effect, boolean high, boolean element) {
		SC_ENCHANT_RESULT result = newInstance();
		result.set_object_id(item.getId());
		result.set_bless(item.getBless());
		result.set_enchant_result(eResult);
		result.set_bm_scroll(isBm ? 1 : 0);
		if (eResult.toInt() == SC_ENCHANT_RESULT.eResult.FAIL_REMAIN.toInt()) {
			result.set_enchant_amount(0);
		} else {
			result.set_enchant_amount(amount);
		}
		result.set_special_enchant_effect(effect); //0~5 //이건 아니면 안쏜다.. // 0(한번) / 1,2,3(두번) / 4,5
		
		//아이템의 인챈트할때 인챈트 수에의한거니깐
		result.set_high_enchant(high); //true /false
		//result.set_is_element_enchant(element); // true /false
		
		pc.sendPackets(result, MJEProtoMessages.SC_ENCHANT_RESULT, true);
	}
	
	public static void send_attr_enchant(L1PcInstance pc, L1ItemInstance item, int enchant_type, int enchant_amount, int enchant_floor){
		SC_ENCHANT_RESULT noti = SC_ENCHANT_RESULT.newInstance();
		noti.set_object_id(item.getId());
		noti.set_bless(item.getBless());
		noti.set_enchant_result(eResult.fromInt(enchant_type));
		noti.set_bm_scroll(0);
		noti.set_enchant_amount(enchant_type == 0 ? enchant_floor : 0);
		if (enchant_floor >= 3)
			noti.set_special_enchant_effect(0);
		else
			noti.set_special_enchant_effect(1);
		noti.set_high_enchant(false);
		noti.set_element_enchant(enchant_amount);
		pc.sendPackets(noti, MJEProtoMessages.SC_ENCHANT_RESULT, true);
	}
	
	public static SC_ENCHANT_RESULT newInstance(){
		return new SC_ENCHANT_RESULT();
	}
	private int _object_id;
	private int _bless;
	private SC_ENCHANT_RESULT.eResult _enchant_result;
	private int _bm_scroll;
	private int _enchant_amount;
	private int _special_enchant_effect;
	private boolean _high_enchant;
	private int _element_enchant;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ENCHANT_RESULT(){
	}
	public int get_object_id(){
		return _object_id;
	}
	public void set_object_id(int val){
		_bit |= 0x1;
		_object_id = val;
	}
	public boolean has_object_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_bless(){
		return _bless;
	}
	public void set_bless(int val){
		_bit |= 0x2;
		_bless = val;
	}
	public boolean has_bless(){
		return (_bit & 0x2) == 0x2;
	}
	public SC_ENCHANT_RESULT.eResult get_enchant_result(){
		return _enchant_result;
	}
	public void set_enchant_result(SC_ENCHANT_RESULT.eResult val){
		_bit |= 0x4;
		_enchant_result = val;
	}
	public boolean has_enchant_result(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_bm_scroll(){
		return _bm_scroll;
	}
	public void set_bm_scroll(int val){
		_bit |= 0x8;
		_bm_scroll = val;
	}
	public boolean has_bm_scroll(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_enchant_amount(){
		return _enchant_amount;
	}
	public void set_enchant_amount(int val){
		_bit |= 0x10;
		_enchant_amount = val;
	}
	public boolean has_enchant_amount(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_special_enchant_effect(){
		return _special_enchant_effect;
	}
	public void set_special_enchant_effect(int val){
		_bit |= 0x20;
		_special_enchant_effect = val;
	}
	public boolean has_special_enchant_effect(){
		return (_bit & 0x20) == 0x20;
	}
	public boolean get_high_enchant(){
		return _high_enchant;
	}
	public void set_high_enchant(boolean val){
		_bit |= 0x40;
		_high_enchant = val;
	}
	public boolean has_high_enchant(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_element_enchant(){
		return _element_enchant;
	}
	public void set_element_enchant(int val){
		_bit |= 0x80;
		_element_enchant = val;
	}
	public boolean has_element_enchant(){
		return (_bit & 0x80) == 0x80;
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
		if (has_object_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _object_id);
		}
		if (has_bless()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _bless);
		}
		if (has_enchant_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _enchant_result.toInt());
		}
		if (has_bm_scroll()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _bm_scroll);
		}
		if (has_enchant_amount()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _enchant_amount);
		}
		if (has_special_enchant_effect()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _special_enchant_effect);
		}
		if (has_high_enchant()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _high_enchant);
		}
		if (has_element_enchant()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _element_enchant);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_object_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bless()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bm_scroll()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant_amount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_object_id()){
			output.writeUInt32(1, _object_id);
		}
		if (has_bless()){
			output.wirteInt32(2, _bless);
		}
		if (has_enchant_result()){
			output.writeEnum(3, _enchant_result.toInt());
		}
		if (has_bm_scroll()){
			output.wirteInt32(4, _bm_scroll);
		}
		if (has_enchant_amount()){
			output.wirteInt32(5, _enchant_amount);
		}
		if (has_special_enchant_effect()){
			output.wirteInt32(6, _special_enchant_effect);
		}
		if (has_high_enchant()){
			output.writeBool(7, _high_enchant);
		}
		if (has_element_enchant()){
			output.wirteInt32(8, _element_enchant);
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
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_bless(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_enchant_result(SC_ENCHANT_RESULT.eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020:{
					set_bm_scroll(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_enchant_amount(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_special_enchant_effect(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_high_enchant(input.readBool());
					break;
				}
				case 0x00000040:{
					set_element_enchant(input.readInt32());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_ENCHANT_RESULT();
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
	public enum eResult{
		SUCCESS(0),
		FAIL_DESTROY(1),
		FAIL_REMAIN(2),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return FAIL_DESTROY;
			case 2:
				return FAIL_REMAIN;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 eResult, %d", i));
			}
		}
	}
}

package l1j.server.MJTemplate.MJProto.MainServer_Client_PolymorphBook;

import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ENCHANT_RESULT;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_POLYMORPHBOOK_ENCHANT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_POLYMORPHBOOK_ENCHANT_NOTI newInstance(){
		return new SC_POLYMORPHBOOK_ENCHANT_NOTI();
	}
	private int _Id;
	private int _Slot;
	private SC_ENCHANT_RESULT.eResult _Result;
	private int _Before;
	private int _After;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_POLYMORPHBOOK_ENCHANT_NOTI(){
	}
	public int get_Id(){
		return _Id;
	}
	public void set_Id(int val){
		_bit |= 0x1;
		_Id = val;
	}
	public boolean has_Id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_Slot(){
		return _Slot;
	}
	public void set_Slot(int val){
		_bit |= 0x2;
		_Slot = val;
	}
	public boolean has_Slot(){
		return (_bit & 0x2) == 0x2;
	}
	public SC_ENCHANT_RESULT.eResult get_Result(){
		return _Result;
	}
	public void set_Result(SC_ENCHANT_RESULT.eResult val){
		_bit |= 0x4;
		_Result = val;
	}
	public boolean has_Result(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_Before(){
		return _Before;
	}
	public void set_Before(int val){
		_bit |= 0x8;
		_Before = val;
	}
	public boolean has_Before(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_After(){
		return _After;
	}
	public void set_After(int val){
		_bit |= 0x10;
		_After = val;
	}
	public boolean has_After(){
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
		if (has_Id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Id);
		}
		if (has_Slot()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _Slot);
		}
		if (has_Result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _Result.toInt());
		}
		if (has_Before()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _Before);
		}
		if (has_After()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _After);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_Id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_Slot()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_Result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_Before()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_After()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_Id()){
			output.wirteInt32(1, _Id);
		}
		if (has_Slot()){
			output.wirteInt32(2, _Slot);
		}
		if (has_Result()){
			output.writeEnum(3, _Result.toInt());
		}
		if (has_Before()){
			output.wirteInt32(4, _Before);
		}
		if (has_After()){
			output.wirteInt32(5, _After);
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
					set_Id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_Slot(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_Result(SC_ENCHANT_RESULT.eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020:{
					set_Before(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_After(input.readInt32());
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
		return new SC_POLYMORPHBOOK_ENCHANT_NOTI();
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

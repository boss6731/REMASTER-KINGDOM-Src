package l1j.server.MJTemplate.MJProto.MainServer_Client_Potential;

import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ENCHANT_POTENTIAL_UPDATE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_ENCHANT_POTENTIAL_UPDATE_REQ newInstance(){
		return new CS_ENCHANT_POTENTIAL_UPDATE_REQ();
	}
	private int _target_id;
	private boolean _isChange;
	private int _bonus_grade;
	private int _bonus_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_ENCHANT_POTENTIAL_UPDATE_REQ(){
	}
	public int get_target_id(){
		return _target_id;
	}
	public void set_target_id(int val){
		_bit |= 0x1;
		_target_id = val;
	}
	public boolean has_target_id(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_isChange(){
		return _isChange;
	}
	public void set_isChange(boolean val){
		_bit |= 0x2;
		_isChange = val;
	}
	public boolean has_isChange(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_bonus_grade(){
		return _bonus_grade;
	}
	public void set_bonus_grade(int val){
		_bit |= 0x4;
		_bonus_grade = val;
	}
	public boolean has_bonus_grade(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_bonus_id(){
		return _bonus_id;
	}
	public void set_bonus_id(int val){
		_bit |= 0x8;
		_bonus_id = val;
	}
	public boolean has_bonus_id(){
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
		if (has_target_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _target_id);
		}
		if (has_isChange()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _isChange);
		}
		if (has_bonus_grade()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _bonus_grade);
		}
		if (has_bonus_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _bonus_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_target_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isChange()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_target_id()){
			output.wirteInt32(1, _target_id);
		}
		if (has_isChange()){
			output.writeBool(2, _isChange);
		}
		if (has_bonus_grade()){
			output.wirteInt32(3, _bonus_grade);
		}
		if (has_bonus_id()){
			output.wirteInt32(4, _bonus_id);
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
					set_target_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_isChange(input.readBool());
					break;
				}
				case 0x00000018:{
					set_bonus_grade(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_bonus_id(input.readInt32());
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
//			System.out.println(this.get_bonus_grade()+"+"+pc.doll_update_option[0]);
//			System.out.println(this.get_bonus_id()+" + "+pc.doll_update_option[1]);
			if (pc.doll_update_option[0] > 0 && pc.doll_update_option[1] > 0) {
				if (this.get_bonus_grade() != pc.doll_update_option[0]) {
					this.set_isChange(false);
				}
				if (this.get_bonus_id() != pc.doll_update_option[1]) {
					this.set_isChange(false);
				}
			}
//			System.out.println("오나1"+get_isChange());
			if (get_isChange()) {
				int bonus_level = 0;
				if (this.get_bonus_id() >= 1 && this.get_bonus_id() <= 19)
					bonus_level = 0;
				else if (this.get_bonus_id() >= 20 && this.get_bonus_id() <= 52)
					bonus_level = 1;
				else if (this.get_bonus_id() >= 53 && this.get_bonus_id() <= 93)
					bonus_level = 2;
				else if (this.get_bonus_id() >= 94 && this.get_bonus_id() <= 131 || this.get_bonus_id() >= 148 && this.get_bonus_id() <= 154) 
					bonus_level = 3;
				else if (this.get_bonus_id() >= 132 && this.get_bonus_id() <= 146 || this.get_bonus_id() >= 155 && this.get_bonus_id() <= 163)
					bonus_level = 4;
				L1ItemInstance doll_item = pc.getInventory().getItem(this.get_target_id());
				doll_item.set_Doll_Bonus_Level(bonus_level);
				doll_item.set_Doll_Bonus_Value(this.get_bonus_id());
				pc.getInventory().updateItem(doll_item, L1PcInventory.COL_DOLL_LEVEL);
				pc.getInventory().updateItem(doll_item, L1PcInventory.COL_DOLL_VALUE);
				pc.save();
				pc.saveInventory();
//				System.out.println("오나2");
//			} else {
//				new Throwable().printStackTrace(); // 임시 오류 확인용 추후 확인후 주석할것
//				System.out.println(String.format("비정상 적인 방법으로 인형 잠재력 강화를 시도 하였습니다. 캐릭명 : %s", pc.getName()));
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_ENCHANT_POTENTIAL_UPDATE_REQ();
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

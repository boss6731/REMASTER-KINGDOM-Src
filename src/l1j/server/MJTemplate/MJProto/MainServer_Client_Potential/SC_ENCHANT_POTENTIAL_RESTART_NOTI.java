package l1j.server.MJTemplate.MJProto.MainServer_Client_Potential;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.PotentialBonusT;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ENCHANT_POTENTIAL_RESTART_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ENCHANT_POTENTIAL_RESTART_NOTI newInstance(){
		return new SC_ENCHANT_POTENTIAL_RESTART_NOTI();
	}
	private int _target_id;
	private int _bonus_grade;
	private java.util.LinkedList<PotentialBonusT> _select_bonuslist;
	private int _curbonus_desc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ENCHANT_POTENTIAL_RESTART_NOTI(){
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
	public int get_bonus_grade(){
		return _bonus_grade;
	}
	public void set_bonus_grade(int val){
		_bit |= 0x2;
		_bonus_grade = val;
	}
	public boolean has_bonus_grade(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<PotentialBonusT> get_select_bonuslist(){
		return _select_bonuslist;
	}
	public void add_select_bonuslist(PotentialBonusT val){
		if(!has_select_bonuslist()){
			_select_bonuslist = new java.util.LinkedList<PotentialBonusT>();
			_bit |= 0x4;
		}
		_select_bonuslist.add(val);
	}
	public boolean has_select_bonuslist(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_curbonus_desc(){
		return _curbonus_desc;
	}
	public void set_curbonus_desc(int val){
		_bit |= 0x8;
		_curbonus_desc = val;
	}
	public boolean has_curbonus_desc(){
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
		if (has_bonus_grade()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _bonus_grade);
		}
		if (has_select_bonuslist()){
			for(PotentialBonusT val : _select_bonuslist){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_curbonus_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _curbonus_desc);
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
		if (!has_bonus_grade()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_select_bonuslist()){
			for(PotentialBonusT val : _select_bonuslist){
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
		if (has_target_id()){
			output.wirteInt32(1, _target_id);
		}
		if (has_bonus_grade()){
			output.wirteInt32(2, _bonus_grade);
		}
		if (has_select_bonuslist()){
			for (PotentialBonusT val : _select_bonuslist){
				output.writeMessage(3, val);
			}
		}
		if (has_curbonus_desc()){
			output.wirteInt32(4, _curbonus_desc);
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
					set_bonus_grade(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_select_bonuslist((PotentialBonusT)input.readMessage(PotentialBonusT.newInstance()));
					break;
				}
				case 0x00000020:{
					set_curbonus_desc(input.readInt32());
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
		return new SC_ENCHANT_POTENTIAL_RESTART_NOTI();
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

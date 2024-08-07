package l1j.server.MJTemplate.MJProto.MainServer_Client_Spell;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

public class SC_SPELL_PASSIVE_ONOFF_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_SPELL_PASSIVE_ONOFF_ACK newInstance(){
		return new SC_SPELL_PASSIVE_ONOFF_ACK();
	}

	public static void send(L1PcInstance pc, int _passive_id, boolean _onoff) {
		if (pc == null)
			return;

		SC_SPELL_PASSIVE_ONOFF_ACK noti = SC_SPELL_PASSIVE_ONOFF_ACK.newInstance();
		noti.set_passive_id(_passive_id);
		noti.set_onoff(_onoff);
		noti.set_result(eRES.eRES_OK);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_PASSIVE_ONOFF_ACK, true);
	}

	private SC_SPELL_PASSIVE_ONOFF_ACK.eRES _result;
	private int _passive_id;
	private boolean _onoff;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SPELL_PASSIVE_ONOFF_ACK(){
	}
	public SC_SPELL_PASSIVE_ONOFF_ACK.eRES get_result(){
		return _result;
	}
	public void set_result(SC_SPELL_PASSIVE_ONOFF_ACK.eRES val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_passive_id(){
		return _passive_id;
	}
	public void set_passive_id(int val){
		_bit |= 0x2;
		_passive_id = val;
	}
	public boolean has_passive_id(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_onoff(){
		return _onoff;
	}
	public void set_onoff(boolean val){
		_bit |= 0x4;
		_onoff = val;
	}
	public boolean has_onoff(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_passive_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _passive_id);
		}
		if (has_onoff()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _onoff);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
		if (has_passive_id()){
			output.wirteInt32(2, _passive_id);
		}
		if (has_onoff()){
			output.writeBool(3, _onoff);
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
					set_result(SC_SPELL_PASSIVE_ONOFF_ACK.eRES.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_passive_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_onoff(input.readBool());
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

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_SPELL_PASSIVE_ONOFF_ACK();
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
	public enum eRES {
		// 操作成功
		eRES_OK(0),
		// 切換ID失敗
		eRES_FAIL_TOGGLEID(1),
		// 被動ID失敗
		eRES_FAIL_PASSIVEID(2),

		;

		private int value;

		// 枚舉類型的構造函數，用於初始化枚舉值
		eRES(int val) {
			value = val;
		}

		// 返回枚舉值的整數表示
		public int toInt() {
			return value;
		}

		// 比較當前枚舉值與其他枚舉值是否相等
		public boolean equals(eRES v) {
			return value == v.value;
		}

		// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
		public static eRES fromInt(int i) {
			switch (i) {
				case 0:
					return eRES_OK; // 操作成功
				case 1:
					return eRES_FAIL_TOGGLEID; // 切換ID失敗
				case 2:
					return eRES_FAIL_PASSIVEID; // 被動ID失敗
				default:
					throw new IllegalArgumentException(String.format("無效的 eRES 參數: %d", i));
			}
		}
	}

package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_TIME_COLLECTION_RESET_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, L1TimeCollectionUser user) {
		SC_TIME_COLLECTION_RESET_ACK ack = SC_TIME_COLLECTION_RESET_ACK.newInstance();
		ack.set_result(0);
		ack.set_groupID(user.getType().getType());
		ack.set_setID(user.getCollectionIndex());
		
		TimeCollectionSetData data = TimeCollectionSetData.newInstance();
		data.set_setId(user.getCollectionIndex());
		data.set_useRecycle(user.getBuffIndex());
		data.set_buffType(user.getBuffType().getType());
		data.set_completeTime(0);
		data.set_enchantSum(0);
		data.set_state(TIME_COLLECTION_STATE.fromInt(0));
		ack.set_setData(data);
		
		pc.sendPackets(ack, MJEProtoMessages.SC_TIME_COLLECTION_RESET_ACK, true);
		
	}
	public static SC_TIME_COLLECTION_RESET_ACK newInstance(){
		return new SC_TIME_COLLECTION_RESET_ACK();
	}
	private int _result;
	private int _groupID;
	private int _setID;
	private TimeCollectionSetData _setData;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TIME_COLLECTION_RESET_ACK(){
	}
	public int get_result(){
		return _result;
	}
	public void set_result(int val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_groupID(){
		return _groupID;
	}
	public void set_groupID(int val){
		_bit |= 0x2;
		_groupID = val;
	}
	public boolean has_groupID(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_setID(){
		return _setID;
	}
	public void set_setID(int val){
		_bit |= 0x4;
		_setID = val;
	}
	public boolean has_setID(){
		return (_bit & 0x4) == 0x4;
	}
	public TimeCollectionSetData get_setData(){
		return _setData;
	}
	public void set_setData(TimeCollectionSetData val){
		_bit |= 0x8;
		_setData = val;
	}
	public boolean has_setData(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _result);
		}
		if (has_groupID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _groupID);
		}
		if (has_setID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _setID);
		}
		if (has_setData()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _setData);
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
		if (!has_groupID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_setID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_setData()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.wirteInt32(1, _result);
		}
		if (has_groupID()){
			output.wirteInt32(2, _groupID);
		}
		if (has_setID()){
			output.wirteInt32(3, _setID);
		}
		if (has_setData()){
			output.writeMessage(4, _setData);
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
					set_result(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_groupID(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_setID(input.readInt32());
					break;
				}
				case 0x00000022:{
					set_setData((TimeCollectionSetData)input.readMessage(TimeCollectionSetData.newInstance()));
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
		return new SC_TIME_COLLECTION_RESET_ACK();
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

package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_TIME_COLLECTION_SET_DATA_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, L1TimeCollectionUser user) {
		SC_TIME_COLLECTION_SET_DATA_NOTI noti = SC_TIME_COLLECTION_SET_DATA_NOTI.newInstance();
		noti.set_groupId(user.getType().getType());
		noti.set_setId(user.getCollectionIndex());
		noti.set_setData(TimeCollectionSetData.send(pc, user.getObj(), user));
		
		pc.sendPackets(noti, MJEProtoMessages.SC_TIME_COLLECTION_SET_DATA_NOTI, true);
		
	}
	public static SC_TIME_COLLECTION_SET_DATA_NOTI newInstance(){
		return new SC_TIME_COLLECTION_SET_DATA_NOTI();
	}
	private int _groupId;
	private int _setId;
	private TimeCollectionSetData _setData;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TIME_COLLECTION_SET_DATA_NOTI(){
	}
	public int get_groupId(){
		return _groupId;
	}
	public void set_groupId(int val){
		_bit |= 0x1;
		_groupId = val;
	}
	public boolean has_groupId(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_setId(){
		return _setId;
	}
	public void set_setId(int val){
		_bit |= 0x2;
		_setId = val;
	}
	public boolean has_setId(){
		return (_bit & 0x2) == 0x2;
	}
	public TimeCollectionSetData get_setData(){
		return _setData;
	}
	public void set_setData(TimeCollectionSetData val){
		_bit |= 0x4;
		_setData = val;
	}
	public boolean has_setData(){
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
		if (has_groupId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _groupId);
		}
		if (has_setId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _setId);
		}
		if (has_setData()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _setData);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_groupId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_setId()){
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
		if (has_groupId()){
			output.wirteInt32(1, _groupId);
		}
		if (has_setId()){
			output.wirteInt32(2, _setId);
		}
		if (has_setData()){
			output.writeMessage(3, _setData);
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
					set_groupId(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_setId(input.readInt32());
					break;
				}
				case 0x0000001A:{
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
		return new SC_TIME_COLLECTION_SET_DATA_NOTI();
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

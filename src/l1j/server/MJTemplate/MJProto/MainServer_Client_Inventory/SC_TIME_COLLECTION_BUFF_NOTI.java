package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionStatus;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_TIME_COLLECTION_BUFF_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, L1TimeCollectionUser user, L1TimeCollectionStatus status) {
		SC_TIME_COLLECTION_BUFF_NOTI noti = SC_TIME_COLLECTION_BUFF_NOTI.newInstance();
		noti.set_notiType(eNotiType.fromInt(status.getStstus()));
		noti.set_groupId(user.getType().getType());
		noti.set_setId(user.getCollectionIndex());
		noti.set_buffCount(user.getBuffIndex());
		
		pc.sendPackets(noti, MJEProtoMessages.SC_TIME_COLLECTION_BUFF_NOTI, true);
	}
	public static SC_TIME_COLLECTION_BUFF_NOTI newInstance(){
		return new SC_TIME_COLLECTION_BUFF_NOTI();
	}
	private SC_TIME_COLLECTION_BUFF_NOTI.eNotiType _notiType;
	private int _groupId;
	private int _setId;
	private int _buffCount;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TIME_COLLECTION_BUFF_NOTI(){
	}
	public SC_TIME_COLLECTION_BUFF_NOTI.eNotiType get_notiType(){
		return _notiType;
	}
	public void set_notiType(SC_TIME_COLLECTION_BUFF_NOTI.eNotiType val){
		_bit |= 0x1;
		_notiType = val;
	}
	public boolean has_notiType(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_groupId(){
		return _groupId;
	}
	public void set_groupId(int val){
		_bit |= 0x2;
		_groupId = val;
	}
	public boolean has_groupId(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_setId(){
		return _setId;
	}
	public void set_setId(int val){
		_bit |= 0x4;
		_setId = val;
	}
	public boolean has_setId(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_buffCount(){
		return _buffCount;
	}
	public void set_buffCount(int val){
		_bit |= 0x8;
		_buffCount = val;
	}
	public boolean has_buffCount(){
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
		if (has_notiType()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _notiType.toInt());
		}
		if (has_groupId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _groupId);
		}
		if (has_setId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _setId);
		}
		if (has_buffCount()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _buffCount);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_notiType()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_groupId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_setId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_notiType()){
			output.writeEnum(1, _notiType.toInt());
		}
		if (has_groupId()){
			output.wirteInt32(2, _groupId);
		}
		if (has_setId()){
			output.wirteInt32(3, _setId);
		}
		if (has_buffCount()){
			output.wirteInt32(4, _buffCount);
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
					set_notiType(SC_TIME_COLLECTION_BUFF_NOTI.eNotiType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_groupId(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_setId(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_buffCount(input.readInt32());
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
		return new SC_TIME_COLLECTION_BUFF_NOTI();
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
	public enum eNotiType{
		NEW(1),
		END(2),
		SOON_END(3),
		REFRESH(4),
		;
		private int value;
		eNotiType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eNotiType v){
			return value == v.value;
		}
		public static eNotiType fromInt(int i){
			switch(i){
			case 1:
				return NEW;
			case 2:
				return END;
			case 3:
				return SOON_END;
			case 4:
				return REFRESH;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eNotiType, %d", i));
			}
		}
	}
}

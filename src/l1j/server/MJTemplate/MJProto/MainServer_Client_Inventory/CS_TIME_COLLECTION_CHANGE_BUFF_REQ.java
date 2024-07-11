package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionStatus;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_TIME_COLLECTION_CHANGE_BUFF_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_TIME_COLLECTION_CHANGE_BUFF_REQ newInstance(){
		return new CS_TIME_COLLECTION_CHANGE_BUFF_REQ();
	}
	private int _groupID;
	private int _setID;
	private int _buffType;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_TIME_COLLECTION_CHANGE_BUFF_REQ(){
	}
	public int get_groupID(){
		return _groupID;
	}
	public void set_groupID(int val){
		_bit |= 0x1;
		_groupID = val;
	}
	public boolean has_groupID(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_setID(){
		return _setID;
	}
	public void set_setID(int val){
		_bit |= 0x2;
		_setID = val;
	}
	public boolean has_setID(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_buffType(){
		return _buffType;
	}
	public void set_buffType(int val){
		_bit |= 0x4;
		_buffType = val;
	}
	public boolean has_buffType(){
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
		if (has_groupID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _groupID);
		}
		if (has_setID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _setID);
		}
		if (has_buffType()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _buffType);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_groupID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_setID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_buffType()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_groupID()){
			output.wirteInt32(1, _groupID);
		}
		if (has_setID()){
			output.wirteInt32(2, _setID);
		}
		if (has_buffType()){
			output.wirteInt32(3, _buffType);
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
					set_groupID(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_setID(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_buffType(input.readInt32());
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

// TODO: 從這裡開始插入處理代碼。由 Nature 編寫。

// 檢查是否存在集合
			L1TimeCollectionType type = L1TimeCollectionType.getType(_groupID);
			L1TimeCollection obj = L1TimeCollectionLoader.getData(type, _setID);
			if (obj == null) {
				System.out.println(String.format(
						"[A_TimeCollectionChange] 集合為空：類型(%s)，索引(%d)，名稱(%s)",
						type.getName(), _setID, pc.getName()));
				return this;
			}

// 檢查處理程序
			L1TimeCollectionHandler handler = pc.getTimeCollection();
			if (handler == null) {
				System.out.println(String.format(
						"[A_TimeCollectionChange] 處理程序為空：標誌(%d)，名稱(%s)",
						obj.getFlag(), pc.getName()));
				return this;
			}

// TODO: 從這裡開始插入處理代碼。由 Nature 編寫。
			
			// 버프 검사
			L1TimeCollectionUser user			= handler.getUser(obj.getFlag());
			if (user == null || user.getAblity() == null || user.getBuffTimer() == null || user.getBuffType().equals(_buffType)) {
				return this;
			}
			
			// 기존 능력치 제거
			user.getAblity().ablity(pc, false);
			SC_TIME_COLLECTION_BUFF_NOTI.send(pc, user, L1TimeCollectionStatus.CHANGE);
//			pc.sendPackets(new S_TimeCollection(user, L1TimeCollectionStatus.CHANGE), true);
			
			// 새 버프 부여
			
			L1TimeCollectionBuffType buffType = L1TimeCollectionBuffType.getType(_buffType);
			
			user.setBuffType(buffType);
			handler.activeAblity(pc, user);
			SC_TIME_COLLECTION_CHANGE_BUFF_ACK.send(pc, user);
//			pc.sendPackets(new S_TimeCollection(S_TimeCollection.CHANGE, user), true);
			
			

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_TIME_COLLECTION_CHANGE_BUFF_REQ();
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

package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class TimeCOllectionGroupData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static TimeCOllectionGroupData send(L1PcInstance pc, L1TimeCollectionHandler handler, L1TimeCollectionType type, ConcurrentHashMap<Integer, L1TimeCollection> map) {
		TimeCOllectionGroupData group_data = TimeCOllectionGroupData.newInstance();
		group_data.set_groupId(type.getType());
		
		L1TimeCollection obj		= null;
		L1TimeCollectionUser user	= null;
		for (Map.Entry<Integer, L1TimeCollection> entry : map.entrySet()) {
			obj		= entry.getValue();
			user	= handler.getUser(obj.getFlag());
			group_data.add_setData(TimeCollectionSetData.send(pc, obj, user));
		}
		return group_data;
	}
	public static TimeCOllectionGroupData newInstance(){
		return new TimeCOllectionGroupData();
	}
	private int _groupId;
	private java.util.LinkedList<TimeCollectionSetData> _setData;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TimeCOllectionGroupData(){
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
	public java.util.LinkedList<TimeCollectionSetData> get_setData(){
		return _setData;
	}
	public void add_setData(TimeCollectionSetData val){
		if(!has_setData()){
			_setData = new java.util.LinkedList<TimeCollectionSetData>();
			_bit |= 0x2;
		}
		_setData.add(val);
	}
	public boolean has_setData(){
		return (_bit & 0x2) == 0x2;
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
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _groupId);
		}
		if (has_setData()){
			for(TimeCollectionSetData val : _setData){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
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
		if (has_setData()){
			for(TimeCollectionSetData val : _setData){
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
		if (has_groupId()){
			output.writeUInt32(1, _groupId);
		}
		if (has_setData()){
			for (TimeCollectionSetData val : _setData){
				output.writeMessage(2, val);
			}
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
					set_groupId(input.readUInt32());
					break;
				}
				case 0x00000012:{
					add_setData((TimeCollectionSetData)input.readMessage(TimeCollectionSetData.newInstance()));
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
		return new TimeCOllectionGroupData();
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

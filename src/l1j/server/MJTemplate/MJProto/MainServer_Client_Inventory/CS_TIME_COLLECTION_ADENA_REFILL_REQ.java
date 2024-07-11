package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_TIME_COLLECTION_ADENA_REFILL_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_TIME_COLLECTION_ADENA_REFILL_REQ newInstance(){
		return new CS_TIME_COLLECTION_ADENA_REFILL_REQ();
	}
	private int _groupId;
	private int _setId;
	private int _count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_TIME_COLLECTION_ADENA_REFILL_REQ(){
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
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x4;
		_count = val;
	}
	public boolean has_count(){
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
		if (has_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _count);
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
		if (!has_count()){
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
		if (has_count()){
			output.wirteInt32(3, _count);
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
				case 0x00000018:{
					set_count(input.readInt32());
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
			int groupId = get_groupId();
			int setId = get_setId();
			int count = get_count();
			int result = 0;

// 檢查是否存在集合
			L1TimeCollectionType type = L1TimeCollectionType.getType(groupId);
			L1TimeCollection obj = L1TimeCollectionLoader.getData(type, setId);
			if (obj == null) {
				System.out.println(String.format(
						"[A_TimeCollectionChange] 集合為空：類型(%s)，索引(%d)，名稱(%s)",
						type.getName(), setId, pc.getName()));
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
			// 버프 검사
			L1TimeCollectionUser user			= handler.getUser(obj.getFlag());
			if (user == null || user.getAblity() == null || user.getBuffTimer() == null) {
				result = 4;
			}
			
			if (setId == 1) {
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 500000 * count)) {
					result = 5;
				} else {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 500000 * count);
					result = 1;
				}
			} else if (setId == 2) {
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 1000000 * count)) {
					result = 5;
				} else {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000000 * count);
					result = 1;
				}
			} else if (setId == 3) {
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, 1500000 * count)) {
					result = 5;
				} else {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1500000 * count);
					result = 1;
				}
			}

			int countmax = 0;
			switch (setId) {
				case 1:
					switch (user.getSumEnchant()) {
						case 13:
							countmax = 5;
							break;
						case 14:
							countmax = 10;
							break;
						case 15:
							countmax = 20;
							break;
						case 16:
							countmax = 40;
							break;
						case 17:
							countmax = 60;
							break;
						case 18:
							countmax = 80;
							break;
						default:
							if (user.getSumEnchant() < 13) {
								break;
							} else if (user.getSumEnchant() >18) {
								countmax = 80;
							}
						}
					break;
				case 2:
					switch (user.getSumEnchant()) {
						case 9:
							countmax = 5;
							break;
						case 10:
							countmax = 10;
							break;
						case 11:
							countmax = 20;
							break;
						case 12:
							countmax = 40;
							break;
						case 13:
							countmax = 60;
							break;
						case 14:
							countmax = 80;
							break;
						default:
							if (user.getSumEnchant() < 9) {
								break;
							} else if (user.getSumEnchant() > 14) {
								countmax = 80;
							}
						}
					break;
				case 3:
					switch (user.getSumEnchant()) {
					case 3:
						countmax = 5;
						break;
					case 4:
						countmax = 10;
						break;
					case 5:
						countmax = 20;
						break;
					case 6:
						countmax = 40;
						break;
					case 7:
						countmax = 60;
						break;
					case 8:
						countmax = 80;
						break;
					default:
						if (user.getSumEnchant() < 3) {
							break;
						} else if (user.getSumEnchant() > 8) {
							countmax = 80;
						}
					}
				break;
			}
			
			if (user.getRefill_count() + count > countmax) {
				result = 7;
				count = 0;
			}
			
			if (result == 0) {
				result = 1;
			}
			
			user.setRefill_count(user.getRefill_count() + count);
			user.addBuffTime((long)count * 24 * 60 * 60 * 1000);
			L1TimeCollectionUserLoader.getInstance().insert(user);
			SC_TIME_COLLECTION_ADENA_REFILL_ACK.send(pc, result, user, count);
			SC_TIME_COLLECTION_SET_DATA_NOTI.send(pc, user);
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_TIME_COLLECTION_ADENA_REFILL_REQ();
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

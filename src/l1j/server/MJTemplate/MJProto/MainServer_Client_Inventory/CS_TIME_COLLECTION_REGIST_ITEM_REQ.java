package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionMaterial;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.serverpackets.S_ServerMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_TIME_COLLECTION_REGIST_ITEM_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_TIME_COLLECTION_REGIST_ITEM_REQ newInstance(){
		return new CS_TIME_COLLECTION_REGIST_ITEM_REQ();
	}
	private int _targetID;
	private int _groupID;
	private int _setID;
	private int _slotNo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_TIME_COLLECTION_REGIST_ITEM_REQ(){
	}
	public int get_targetID(){
		return _targetID;
	}
	public void set_targetID(int val){
		_bit |= 0x1;
		_targetID = val;
	}
	public boolean has_targetID(){
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
	public int get_slotNo(){
		return _slotNo;
	}
	public void set_slotNo(int val){
		_bit |= 0x8;
		_slotNo = val;
	}
	public boolean has_slotNo(){
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
		if (has_targetID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _targetID);
		}
		if (has_groupID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _groupID);
		}
		if (has_setID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _setID);
		}
		if (has_slotNo()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _slotNo);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_targetID()){
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
		if (!has_slotNo()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_targetID()){
			output.wirteInt32(1, _targetID);
		}
		if (has_groupID()){
			output.wirteInt32(2, _groupID);
		}
		if (has_setID()){
			output.wirteInt32(3, _setID);
		}
		if (has_slotNo()){
			output.wirteInt32(4, _slotNo);
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
					set_targetID(input.readInt32());
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
				case 0x00000020:{
					set_slotNo(input.readInt32());
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

// 檢查擁有的物品
			L1PcInventory inv = pc.getInventory();
			L1ItemInstance registItem = inv.findItemObjId(_targetID);
			if (registItem == null) {
				System.out.println(String.format(
						"[A_TimeCollectionRegist] 註冊物品為空：物品ID(%d)，角色名稱(%s)",
						_targetID, pc.getName()));
				return this;
			}

			if (registItem.isEquipped() || registItem.getBless() >= 128 || registItem.getEndTime() != null) {
				pc.sendPackets(new S_ServerMessage(9023), true); // 無法將所選裝備註冊展示。
				return this;
			}

// 檢查是否存在集合
			L1TimeCollectionType type = L1TimeCollectionType.getType(_groupID);
			L1TimeCollection collection = L1TimeCollectionLoader.getData(type, _setID);
			if (collection == null) {
				System.out.println(String.format(
						"[A_TimeCollectionRegist] 無集合：類型(%s)，集合索引(%d)，物品ID(%d)，角色名稱(%s)",
						type.getName(), _setID, registItem.getItemId(), pc.getName()));
				return this;
			}

// 檢查材料
			L1TimeCollectionMaterial material = collection.getMaterial(_slotNo);
			if (material == null || !material.isMaterial(registItem)) {
				System.out.println(String.format(
						"[A_TimeCollectionRegist] 無材料：槽位索引(%d)，物品ID(%d)，角色名稱(%s)",
						_slotNo, registItem.getItemId(), pc.getName()));
				return this;
			}

			try {
				if (!pc.getTimeCollection().regist(pc, collection, registItem, _slotNo)) {
					System.out.println(String.format(
							"[A_TimeCollectionRegist] 註冊失敗：槽位索引(%d)，物品ID(%d)，角色名稱(%s)",
							_slotNo, registItem.getItemId(), pc.getName()));
					return this;
				}
				inv.removeItem(registItem);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_TIME_COLLECTION_REGIST_ITEM_REQ();
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

package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.templates.L1Npc;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_TIME_COLLECTION_DATA_LOAD_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, L1TimeCollectionHandler handler) {
		SC_TIME_COLLECTION_DATA_LOAD_NOTI noti = SC_TIME_COLLECTION_DATA_LOAD_NOTI.newInstance();

		ConcurrentHashMap<L1TimeCollectionType, ConcurrentHashMap<Integer, L1TimeCollection>> map = L1TimeCollectionLoader.getAllData();
		if (map != null && !map.isEmpty()) {
			ConcurrentHashMap<Integer, L1TimeCollection> value = null;
			for (Map.Entry<L1TimeCollectionType, ConcurrentHashMap<Integer, L1TimeCollection>> entry : map.entrySet()) {
				value	= entry.getValue();
				if (value == null || value.isEmpty()) {
					continue;
				}
				noti.add_groupData(TimeCOllectionGroupData.send(pc, handler, entry.getKey(), value));
			}
		}

		int[] npcId = Config.ServerAdSetting.TIME_COLLECTION_NPC_IDS; 
		
		
		for (int i = 0; i < npcId.length; i++) {
			L1Object npcObj = L1World.getInstance().findNpc(npcId[i]);
//			System.out.println("NPCID"+npcId[i]);
			L1NpcInstance npc = (L1NpcInstance) npcObj;
			if (npc == null) {
				continue;
			}
			TCNPCDialogInfoT info = TCNPCDialogInfoT.newInstance();
			info.set_LinkReq(0x0d);
			info.set_Index(i+1);
			info.set_npcId(npc.getId());
			noti.add_npcInfo(info);
		}
		
		pc.sendPackets(noti, MJEProtoMessages.SC_TIME_COLLECTION_DATA_LOAD_NOTI, true);
	}


	public static SC_TIME_COLLECTION_DATA_LOAD_NOTI newInstance(){
		return new SC_TIME_COLLECTION_DATA_LOAD_NOTI();
	}
	private java.util.LinkedList<TimeCOllectionGroupData> _groupData;
	private java.util.LinkedList<SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT> _npcInfo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TIME_COLLECTION_DATA_LOAD_NOTI(){
	}
	public java.util.LinkedList<TimeCOllectionGroupData> get_groupData(){
		return _groupData;
	}
	public void add_groupData(TimeCOllectionGroupData val){
		if(!has_groupData()){
			_groupData = new java.util.LinkedList<TimeCOllectionGroupData>();
			_bit |= 0x1;
		}
		_groupData.add(val);
	}
	public boolean has_groupData(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT> get_npcInfo(){
		return _npcInfo;
	}
	public void add_npcInfo(SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT val){
		if(!has_npcInfo()){
			_npcInfo = new java.util.LinkedList<SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT>();
			_bit |= 0x2;
		}
		_npcInfo.add(val);
	}
	public boolean has_npcInfo(){
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
		if (has_groupData()){
			for(TimeCOllectionGroupData val : _groupData){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_npcInfo()){
			for(SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT val : _npcInfo){
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
		if (has_groupData()){
			for(TimeCOllectionGroupData val : _groupData){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_npcInfo()){
			for(SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT val : _npcInfo){
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
		if (has_groupData()){
			for (TimeCOllectionGroupData val : _groupData){
				output.writeMessage(1, val);
			}
		}
		if (has_npcInfo()){
			for (SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT val : _npcInfo){
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
				case 0x0000000A:{
					add_groupData((TimeCOllectionGroupData)input.readMessage(TimeCOllectionGroupData.newInstance()));
					break;
				}
				case 0x00000012:{
					add_npcInfo((SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT)input.readMessage(SC_TIME_COLLECTION_DATA_LOAD_NOTI.TCNPCDialogInfoT.newInstance()));
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
		return new SC_TIME_COLLECTION_DATA_LOAD_NOTI();
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
	public static class TCNPCDialogInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static TCNPCDialogInfoT newInstance(){
			return new TCNPCDialogInfoT();
		}
		private int _LinkReq;
		private int _Index;
		private int _npcId;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TCNPCDialogInfoT(){
		}
		public int get_LinkReq(){
			return _LinkReq;
		}
		public void set_LinkReq(int val){
			_bit |= 0x1;
			_LinkReq = val;
		}
		public boolean has_LinkReq(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Index(){
			return _Index;
		}
		public void set_Index(int val){
			_bit |= 0x2;
			_Index = val;
		}
		public boolean has_Index(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_npcId(){
			return _npcId;
		}
		public void set_npcId(int val){
			_bit |= 0x4;
			_npcId = val;
		}
		public boolean has_npcId(){
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
			if (has_LinkReq()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _LinkReq);
			}
			if (has_Index()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _Index);
			}
			if (has_npcId()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _npcId);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_LinkReq()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Index()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_npcId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_LinkReq()){
				output.wirteInt32(1, _LinkReq);
			}
			if (has_Index()){
				output.wirteInt32(2, _Index);
			}
			if (has_npcId()){
				output.wirteInt32(3, _npcId);
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
						set_LinkReq(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_Index(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_npcId(input.readInt32());
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
			return new TCNPCDialogInfoT();
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
}

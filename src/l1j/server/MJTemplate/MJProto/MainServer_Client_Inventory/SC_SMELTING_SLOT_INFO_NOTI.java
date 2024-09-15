package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_SMELTING_SLOT_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	
	public static SC_SMELTING_SLOT_INFO_NOTI newInstance(){
		return new SC_SMELTING_SLOT_INFO_NOTI();
	}
	private int _target_object_id;
	private int _slot_total_count;
	private java.util.LinkedList<SC_SMELTING_SLOT_INFO_NOTI.SlotInfo> _slot_infos;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SMELTING_SLOT_INFO_NOTI(){
	}
	public int get_target_object_id(){
		return _target_object_id;
	}
	public void set_target_object_id(int val){
		_bit |= 0x1;
		_target_object_id = val;
	}
	public boolean has_target_object_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_slot_total_count(){
		return _slot_total_count;
	}
	public void set_slot_total_count(int val){
		_bit |= 0x2;
		_slot_total_count = val;
	}
	public boolean has_slot_total_count(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<SC_SMELTING_SLOT_INFO_NOTI.SlotInfo> get_slot_infos(){
		return _slot_infos;
	}
	public void add_slot_infos(SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val){
		if(!has_slot_infos()){
			_slot_infos = new java.util.LinkedList<SC_SMELTING_SLOT_INFO_NOTI.SlotInfo>();
			_bit |= 0x4;
		}
		_slot_infos.add(val);
	}
	public boolean has_slot_infos(){
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
		if (has_target_object_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _target_object_id);
		}
		if (has_slot_total_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _slot_total_count);
		}
		if (has_slot_infos()){
			for(SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val : _slot_infos){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_target_object_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slot_total_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_slot_infos()){
			for(SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val : _slot_infos){
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
		if (has_target_object_id()){
			output.writeUInt32(1, _target_object_id);
		}
		if (has_slot_total_count()){
			output.writeUInt32(2, _slot_total_count);
		}
		if (has_slot_infos()){
			for (SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val : _slot_infos){
				output.writeMessage(3, val);
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
					set_target_object_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_slot_total_count(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					add_slot_infos((SC_SMELTING_SLOT_INFO_NOTI.SlotInfo)input.readMessage(SC_SMELTING_SLOT_INFO_NOTI.SlotInfo.newInstance()));
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
		return new SC_SMELTING_SLOT_INFO_NOTI();
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
	public static class SlotInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SlotInfo newInstance(){
			return new SlotInfo();
		}
		private int _slot_no;
		private int _slot_scroll_nameId;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SlotInfo(){
		}
		public int get_slot_no(){
			return _slot_no;
		}
		public void set_slot_no(int val){
			_bit |= 0x1;
			_slot_no = val;
		}
		public boolean has_slot_no(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_slot_scroll_nameId(){
			return _slot_scroll_nameId;
		}
		public void set_slot_scroll_nameId(int val){
			_bit |= 0x2;
			_slot_scroll_nameId = val;
		}
		public boolean has_slot_scroll_nameId(){
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
			if (has_slot_no()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _slot_no);
			}
			if (has_slot_scroll_nameId()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _slot_scroll_nameId);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_slot_no()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_slot_scroll_nameId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_slot_no()){
				output.writeUInt32(1, _slot_no);
			}
			if (has_slot_scroll_nameId()){
				output.writeUInt32(2, _slot_scroll_nameId);
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
						set_slot_no(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_slot_scroll_nameId(input.readUInt32());
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
			return new SlotInfo();
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

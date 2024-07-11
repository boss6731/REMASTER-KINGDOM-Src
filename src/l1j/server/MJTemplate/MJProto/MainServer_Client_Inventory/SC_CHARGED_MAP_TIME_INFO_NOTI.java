package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.DungeonTimeUserInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_CHARGED_MAP_TIME_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send (L1PcInstance pc, L1ItemInstance item) {
		SC_CHARGED_MAP_TIME_INFO_NOTI noti = new SC_CHARGED_MAP_TIME_INFO_NOTI();

		DungeonTimeInformation dtInfo15 = DungeonTimeInformationLoader.getInstance().from_timer_id(15);
		DungeonTimeProgress<?> progress15 = pc.get_progress(dtInfo15);

			InfoT info = InfoT.newInstance();
			info.set_group(1);		
			info.set_charged_time(progress15.get_remain_seconds());
			info.set_charged_count(progress15.get_charge_count());
			info.set_extra_charged_time(0);
			noti.add_info(info);
	//	}
		
		DungeonTimeInformation dtInfo23 = DungeonTimeInformationLoader.getInstance().from_timer_id(23);
		DungeonTimeProgress<?> progress23 = pc.get_progress(dtInfo23);

			InfoT info1 = InfoT.newInstance();
			info1.set_group(3);		
			info1.set_charged_time(progress23.get_remain_seconds());
			info1.set_charged_count(progress23.get_charge_count());
			info1.set_extra_charged_time(0);
			noti.add_info(info1);
//		}
		
//		System.out.println(item.getItem().getItemNameIdInt());
		
		noti.set_used_item_name_id(item.getItem().getItemDescId());
		noti.set_used_item_group_id(1);
		
		
		
		pc.sendPackets(noti, MJEProtoMessages.SC_CHARGED_MAP_TIME_INFO_NOTI, true);
		
	}
	
	
	public static SC_CHARGED_MAP_TIME_INFO_NOTI newInstance(){
		return new SC_CHARGED_MAP_TIME_INFO_NOTI();
	}
	private java.util.LinkedList<SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT> _info;
	private int _used_item_name_id;
	private int _used_item_group_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_CHARGED_MAP_TIME_INFO_NOTI(){
	}
	public java.util.LinkedList<SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT> get_info(){
		return _info;
	}
	public void add_info(SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT val){
		if(!has_info()){
			_info = new java.util.LinkedList<SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT>();
			_bit |= 0x1;
		}
		_info.add(val);
	}
	public boolean has_info(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_used_item_name_id(){
		return _used_item_name_id;
	}
	public void set_used_item_name_id(int val){
		_bit |= 0x2;
		_used_item_name_id = val;
	}
	public boolean has_used_item_name_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_used_item_group_id(){
		return _used_item_group_id;
	}
	public void set_used_item_group_id(int val){
		_bit |= 0x4;
		_used_item_group_id = val;
	}
	public boolean has_used_item_group_id(){
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
		if (has_info()){
			for(SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT val : _info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_used_item_name_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _used_item_name_id);
		}
		if (has_used_item_group_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _used_item_group_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_info()){
			for(SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT val : _info){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_used_item_name_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_used_item_group_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_info()){
			for (SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT val : _info){
				output.writeMessage(1, val);
			}
		}
		if (has_used_item_name_id()){
			output.wirteInt32(2, _used_item_name_id);
		}
		if (has_used_item_group_id()){
			output.wirteInt32(3, _used_item_group_id);
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
					add_info((SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT)input.readMessage(SC_CHARGED_MAP_TIME_INFO_NOTI.InfoT.newInstance()));
					break;
				}
				case 0x00000010:{
					set_used_item_name_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_used_item_group_id(input.readInt32());
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
		return new SC_CHARGED_MAP_TIME_INFO_NOTI();
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
	public static class InfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static InfoT newInstance(){
			return new InfoT();
		}
		private int _group;
		private int _charged_time;
		private int _charged_count;
		private int _extra_charged_time;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private InfoT(){
		}
		public int get_group(){
			return _group;
		}
		public void set_group(int val){
			_bit |= 0x1;
			_group = val;
		}
		public boolean has_group(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_charged_time(){
			return _charged_time;
		}
		public void set_charged_time(int val){
			_bit |= 0x2;
			_charged_time = val;
		}
		public boolean has_charged_time(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_charged_count(){
			return _charged_count;
		}
		public void set_charged_count(int val){
			_bit |= 0x4;
			_charged_count = val;
		}
		public boolean has_charged_count(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_extra_charged_time(){
			return _extra_charged_time;
		}
		public void set_extra_charged_time(int val){
			_bit |= 0x8;
			_extra_charged_time = val;
		}
		public boolean has_extra_charged_time(){
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
			if (has_group()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _group);
			}
			if (has_charged_time()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _charged_time);
			}
			if (has_charged_count()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _charged_count);
			}
			if (has_extra_charged_time()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _extra_charged_time);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_group()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_charged_time()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_charged_count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_group()){
				output.wirteInt32(1, _group);
			}
			if (has_charged_time()){
				output.writeUInt32(2, _charged_time);
			}
			if (has_charged_count()){
				output.wirteInt32(3, _charged_count);
			}
			if (has_extra_charged_time()){
				output.writeUInt32(4, _extra_charged_time);
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
						set_group(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_charged_time(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_charged_count(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_extra_charged_time(input.readUInt32());
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
			return new InfoT();
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

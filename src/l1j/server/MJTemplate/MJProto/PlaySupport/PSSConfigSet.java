package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PSSConfigSet implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PSSConfigSet newInstance(){
		return new PSSConfigSet();
	}
	private java.util.LinkedList<PSSConfigSet.ConfigInfo> _ConfigInfos;
	private java.util.LinkedList<UsingCondition> _StopConditions;
	private int _CurrentSlotIndex;
	private int _SetID;
	private String _Name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PSSConfigSet(){
		set_CurrentSlotIndex(0);
	}
	public java.util.LinkedList<PSSConfigSet.ConfigInfo> get_ConfigInfos(){
		return _ConfigInfos;
	}
	public void add_ConfigInfos(PSSConfigSet.ConfigInfo val){
		if(!has_ConfigInfos()){
			_ConfigInfos = new java.util.LinkedList<PSSConfigSet.ConfigInfo>();
			_bit |= 0x1;
		}
		_ConfigInfos.add(val);
	}
	public boolean has_ConfigInfos(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<UsingCondition> get_StopConditions(){
		return _StopConditions;
	}
	public void add_StopConditions(UsingCondition val){
		if(!has_StopConditions()){
			_StopConditions = new java.util.LinkedList<UsingCondition>();
			_bit |= 0x2;
		}
		_StopConditions.add(val);
	}
	public boolean has_StopConditions(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_CurrentSlotIndex(){
		return _CurrentSlotIndex;
	}
	public void set_CurrentSlotIndex(int val){
		_bit |= 0x4;
		_CurrentSlotIndex = val;
	}
	public boolean has_CurrentSlotIndex(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_SetID(){
		return _SetID;
	}
	public void set_SetID(int val){
		_bit |= 0x8;
		_SetID = val;
	}
	public boolean has_SetID(){
		return (_bit & 0x8) == 0x8;
	}
	public String get_Name(){
		return _Name;
	}
	public void set_Name(String val){
		_bit |= 0x10;
		_Name = val;
	}
	public boolean has_Name(){
		return (_bit & 0x10) == 0x10;
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
		if (has_ConfigInfos()){
			for(PSSConfigSet.ConfigInfo val : _ConfigInfos){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_StopConditions()){
			for(UsingCondition val : _StopConditions){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_CurrentSlotIndex()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _CurrentSlotIndex);
		}
		if (has_SetID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _SetID);
		}
		if (has_Name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _Name);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_ConfigInfos()){
			for(PSSConfigSet.ConfigInfo val : _ConfigInfos){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_StopConditions()){
			for(UsingCondition val : _StopConditions){
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
		if (has_ConfigInfos()){
			for (PSSConfigSet.ConfigInfo val : _ConfigInfos){
				output.writeMessage(1, val);
			}
		}
		if (has_StopConditions()){
			for (UsingCondition val : _StopConditions){
				output.writeMessage(2, val);
			}
		}
		if (has_CurrentSlotIndex()){
			output.writeUInt32(3, _CurrentSlotIndex);
		}
		if (has_SetID()){
			output.wirteInt32(4, _SetID);
		}
		if (has_Name()){
			output.writeString(5, _Name);
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
					add_ConfigInfos((PSSConfigSet.ConfigInfo)input.readMessage(PSSConfigSet.ConfigInfo.newInstance()));
					break;
				}
				case 0x00000012:{
					add_StopConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
					break;
				}
				case 0x00000018:{
					set_CurrentSlotIndex(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_SetID(input.readInt32());
					break;
				}
				case 0x0000002A:{
					set_Name(input.readString());
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
		return new PSSConfigSet();
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
	public static class ConfigInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static ConfigInfo newInstance(){
			return new ConfigInfo();
		}
		private int _ConfigID;
		private UsingCondition _PathRepeatCondition;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ConfigInfo(){
		}
		public int get_ConfigID(){
			return _ConfigID;
		}
		public void set_ConfigID(int val){
			_bit |= 0x1;
			_ConfigID = val;
		}
		public boolean has_ConfigID(){
			return (_bit & 0x1) == 0x1;
		}
		public UsingCondition get_PathRepeatCondition(){
			return _PathRepeatCondition;
		}
		public void set_PathRepeatCondition(UsingCondition val){
			_bit |= 0x2;
			_PathRepeatCondition = val;
		}
		public boolean has_PathRepeatCondition(){
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
			if (has_ConfigID()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _ConfigID);
			}
			if (has_PathRepeatCondition()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _PathRepeatCondition);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_ConfigID()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_ConfigID()){
				output.writeUInt32(1, _ConfigID);
			}
			if (has_PathRepeatCondition()){
				output.writeMessage(2, _PathRepeatCondition);
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
						set_ConfigID(input.readUInt32());
						break;
					}
					case 0x00000012:{
						set_PathRepeatCondition((UsingCondition)input.readMessage(UsingCondition.newInstance()));
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
			return new ConfigInfo();
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

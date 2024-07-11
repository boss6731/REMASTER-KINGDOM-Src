package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PSSConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PSSConfig newInstance(){
		return new PSSConfig();
	}
	private ClassID _Class;
	private PSSConfig.SetType _UsingSet;
	private PSSConfig.MainConfig _MainCfg;
	private PSSConfig.BuffConfig _BuffCfg;
	private PSSConfig.AttackConfig _AttackCfg;
	private PSSConfig.ItemConfig _ItemCfg;
	private PSSConfig.PathConfig _PathCfg;
	private String _version;
	private PSSConfig.ReturnConfig _ReturnCfg;
	private int _ID;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PSSConfig(){
		set_Class(ClassID.UNKNOWN);
		set_UsingSet(PSSConfig.SetType.ALL);
	}
	public ClassID get_Class(){
		return _Class;
	}
	public void set_Class(ClassID val){
		_bit |= 0x1;
		_Class = val;
	}
	public boolean has_Class(){
		return (_bit & 0x1) == 0x1;
	}
	public PSSConfig.SetType get_UsingSet(){
		return _UsingSet;
	}
	public void set_UsingSet(PSSConfig.SetType val){
		_bit |= 0x2;
		_UsingSet = val;
	}
	public boolean has_UsingSet(){
		return (_bit & 0x2) == 0x2;
	}
	public PSSConfig.MainConfig get_MainCfg(){
		return _MainCfg;
	}
	public void set_MainCfg(PSSConfig.MainConfig val){
		_bit |= 0x4;
		_MainCfg = val;
	}
	public boolean has_MainCfg(){
		return (_bit & 0x4) == 0x4;
	}
	public PSSConfig.BuffConfig get_BuffCfg(){
		return _BuffCfg;
	}
	public void set_BuffCfg(PSSConfig.BuffConfig val){
		_bit |= 0x8;
		_BuffCfg = val;
	}
	public boolean has_BuffCfg(){
		return (_bit & 0x8) == 0x8;
	}
	public PSSConfig.AttackConfig get_AttackCfg(){
		return _AttackCfg;
	}
	public void set_AttackCfg(PSSConfig.AttackConfig val){
		_bit |= 0x10;
		_AttackCfg = val;
	}
	public boolean has_AttackCfg(){
		return (_bit & 0x10) == 0x10;
	}
	public PSSConfig.ItemConfig get_ItemCfg(){
		return _ItemCfg;
	}
	public void set_ItemCfg(PSSConfig.ItemConfig val){
		_bit |= 0x20;
		_ItemCfg = val;
	}
	public boolean has_ItemCfg(){
		return (_bit & 0x20) == 0x20;
	}
	public PSSConfig.PathConfig get_PathCfg(){
		return _PathCfg;
	}
	public void set_PathCfg(PSSConfig.PathConfig val){
		_bit |= 0x40;
		_PathCfg = val;
	}
	public boolean has_PathCfg(){
		return (_bit & 0x40) == 0x40;
	}
	public String get_version(){
		return _version;
	}
	public void set_version(String val){
		_bit |= 0x80;
		_version = val;
	}
	public boolean has_version(){
		return (_bit & 0x80) == 0x80;
	}
	public PSSConfig.ReturnConfig get_ReturnCfg(){
		return _ReturnCfg;
	}
	public void set_ReturnCfg(PSSConfig.ReturnConfig val){
		_bit |= 0x100;
		_ReturnCfg = val;
	}
	public boolean has_ReturnCfg(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_ID(){
		return _ID;
	}
	public void set_ID(int val){
		_bit |= 0x200;
		_ID = val;
	}
	public boolean has_ID(){
		return (_bit & 0x200) == 0x200;
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
		if (has_Class()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _Class.toInt());
		}
		if (has_UsingSet()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _UsingSet.toInt());
		}
		if (has_MainCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _MainCfg);
		}
		if (has_BuffCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _BuffCfg);
		}
		if (has_AttackCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _AttackCfg);
		}
		if (has_ItemCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _ItemCfg);
		}
		if (has_PathCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _PathCfg);
		}
		if (has_version()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(8, _version);
		}
		if (has_ReturnCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(9, _ReturnCfg);
		}
		if (has_ID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _ID);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_Class()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_UsingSet()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_MainCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_BuffCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_AttackCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_ItemCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_PathCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_Class()){
			output.writeEnum(1, _Class.toInt());
		}
		if (has_UsingSet()){
			output.writeEnum(2, _UsingSet.toInt());
		}
		if (has_MainCfg()){
			output.writeMessage(3, _MainCfg);
		}
		if (has_BuffCfg()){
			output.writeMessage(4, _BuffCfg);
		}
		if (has_AttackCfg()){
			output.writeMessage(5, _AttackCfg);
		}
		if (has_ItemCfg()){
			output.writeMessage(6, _ItemCfg);
		}
		if (has_PathCfg()){
			output.writeMessage(7, _PathCfg);
		}
		if (has_version()){
			output.writeString(8, _version);
		}
		if (has_ReturnCfg()){
			output.writeMessage(9, _ReturnCfg);
		}
		if (has_ID()){
			output.writeUInt32(10, _ID);
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
					set_Class(ClassID.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_UsingSet(PSSConfig.SetType.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A:{
					set_MainCfg((PSSConfig.MainConfig)input.readMessage(PSSConfig.MainConfig.newInstance()));
					break;
				}
				case 0x00000022:{
					set_BuffCfg((PSSConfig.BuffConfig)input.readMessage(PSSConfig.BuffConfig.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_AttackCfg((PSSConfig.AttackConfig)input.readMessage(PSSConfig.AttackConfig.newInstance()));
					break;
				}
				case 0x00000032:{
					set_ItemCfg((PSSConfig.ItemConfig)input.readMessage(PSSConfig.ItemConfig.newInstance()));
					break;
				}
				case 0x0000003A:{
					set_PathCfg((PSSConfig.PathConfig)input.readMessage(PSSConfig.PathConfig.newInstance()));
					break;
				}
				case 0x00000042:{
					set_version(input.readString());
					break;
				}
				case 0x0000004A:{
					set_ReturnCfg((PSSConfig.ReturnConfig)input.readMessage(PSSConfig.ReturnConfig.newInstance()));
					break;
				}
				case 0x00000050:{
					set_ID(input.readUInt32());
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
		return new PSSConfig();
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
	public static class AutoUsingSlot implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static AutoUsingSlot newInstance(){
			return new AutoUsingSlot();
		}
		private UsingCondition _condition;
		private SlotInfo _slot;
		private int _priority;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AutoUsingSlot(){
			set_priority(255);
		}
		public UsingCondition get_condition(){
			return _condition;
		}
		public void set_condition(UsingCondition val){
			_bit |= 0x1;
			_condition = val;
		}
		public boolean has_condition(){
			return (_bit & 0x1) == 0x1;
		}
		public SlotInfo get_slot(){
			return _slot;
		}
		public void set_slot(SlotInfo val){
			_bit |= 0x2;
			_slot = val;
		}
		public boolean has_slot(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_priority(){
			return _priority;
		}
		public void set_priority(int val){
			_bit |= 0x4;
			_priority = val;
		}
		public boolean has_priority(){
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
			if (has_condition()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _condition);
			}
			if (has_slot()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _slot);
			}
			if (has_priority()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _priority);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_condition()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_slot()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_condition()){
				output.writeMessage(1, _condition);
			}
			if (has_slot()){
				output.writeMessage(2, _slot);
			}
			if (has_priority()){
				output.writeUInt32(3, _priority);
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
						set_condition((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000012:{
						set_slot((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x00000018:{
						set_priority(input.readUInt32());
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
			return new AutoUsingSlot();
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
	public static class MainConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static MainConfig newInstance(){
			return new MainConfig();
		}
		private boolean _RecvVillageBuff;
		private boolean _RecoverExp;
		private boolean _StartFullHP;
		private boolean _StartFullMP;
		private java.util.LinkedList<UsingCondition> _StopConditions;
		private UsingCondition _StartWeightCondition;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MainConfig(){
		}
		public boolean get_RecvVillageBuff(){
			return _RecvVillageBuff;
		}
		public void set_RecvVillageBuff(boolean val){
			_bit |= 0x1;
			_RecvVillageBuff = val;
		}
		public boolean has_RecvVillageBuff(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_RecoverExp(){
			return _RecoverExp;
		}
		public void set_RecoverExp(boolean val){
			_bit |= 0x2;
			_RecoverExp = val;
		}
		public boolean has_RecoverExp(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_StartFullHP(){
			return _StartFullHP;
		}
		public void set_StartFullHP(boolean val){
			_bit |= 0x4;
			_StartFullHP = val;
		}
		public boolean has_StartFullHP(){
			return (_bit & 0x4) == 0x4;
		}
		public boolean get_StartFullMP(){
			return _StartFullMP;
		}
		public void set_StartFullMP(boolean val){
			_bit |= 0x8;
			_StartFullMP = val;
		}
		public boolean has_StartFullMP(){
			return (_bit & 0x8) == 0x8;
		}
		public java.util.LinkedList<UsingCondition> get_StopConditions(){
			return _StopConditions;
		}
		public void add_StopConditions(UsingCondition val){
			if(!has_StopConditions()){
				_StopConditions = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x10;
			}
			_StopConditions.add(val);
		}
		public boolean has_StopConditions(){
			return (_bit & 0x10) == 0x10;
		}
		public UsingCondition get_StartWeightCondition(){
			return _StartWeightCondition;
		}
		public void set_StartWeightCondition(UsingCondition val){
			_bit |= 0x20;
			_StartWeightCondition = val;
		}
		public boolean has_StartWeightCondition(){
			return (_bit & 0x20) == 0x20;
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
			if (has_RecvVillageBuff()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _RecvVillageBuff);
			}
			if (has_RecoverExp()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _RecoverExp);
			}
			if (has_StartFullHP()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _StartFullHP);
			}
			if (has_StartFullMP()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _StartFullMP);
			}
			if (has_StopConditions()){
				for(UsingCondition val : _StopConditions){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
				}
			}
			if (has_StartWeightCondition()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _StartWeightCondition);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_RecvVillageBuff()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_RecoverExp()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_StartFullHP()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_StartFullMP()){
				_memorizedIsInitialized = -1;
				return false;
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
			if (has_RecvVillageBuff()){
				output.writeBool(1, _RecvVillageBuff);
			}
			if (has_RecoverExp()){
				output.writeBool(2, _RecoverExp);
			}
			if (has_StartFullHP()){
				output.writeBool(3, _StartFullHP);
			}
			if (has_StartFullMP()){
				output.writeBool(4, _StartFullMP);
			}
			if (has_StopConditions()){
				for (UsingCondition val : _StopConditions){
					output.writeMessage(5, val);
				}
			}
			if (has_StartWeightCondition()){
				output.writeMessage(6, _StartWeightCondition);
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
						set_RecvVillageBuff(input.readBool());
						break;
					}
					case 0x00000010:{
						set_RecoverExp(input.readBool());
						break;
					}
					case 0x00000018:{
						set_StartFullHP(input.readBool());
						break;
					}
					case 0x00000020:{
						set_StartFullMP(input.readBool());
						break;
					}
					case 0x0000002A:{
						add_StopConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000032:{
						set_StartWeightCondition((UsingCondition)input.readMessage(UsingCondition.newInstance()));
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
			return new MainConfig();
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
	public static class BuffConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static BuffConfig newInstance(){
			return new BuffConfig();
		}
		private PSSConfig.AutoUsingSlot _SpeedStep1;
		private PSSConfig.AutoUsingSlot _SpeedStep2;
		private PSSConfig.AutoUsingSlot _ExpBoost;
		private PSSConfig.AutoUsingSlot _Soup;
		private PSSConfig.AutoUsingSlot _Cook;
		private PSSConfig.AutoUsingSlot _PowerUp;
		private PSSConfig.AutoUsingSlot _DragonDiamond;
		private PSSConfig.AutoUsingSlot _Polymorph;
		private PSSConfig.AutoUsingSlot _MagicDoll;
		private PSSConfig.AutoUsingSlot _Sink;
		private PSSConfig.AutoUsingSlot _Detox;
		private java.util.LinkedList<PSSConfig.AutoUsingSlot> _BuffSpells;
		private java.util.LinkedList<PSSConfig.AutoUsingSlot> _HPItems;
		private java.util.LinkedList<PSSConfig.AutoUsingSlot> _MPItems;
		private PSSConfig.AutoUsingSlot _Food;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BuffConfig(){
		}
		public PSSConfig.AutoUsingSlot get_SpeedStep1(){
			return _SpeedStep1;
		}
		public void set_SpeedStep1(PSSConfig.AutoUsingSlot val){
			_bit |= 0x1;
			_SpeedStep1 = val;
		}
		public boolean has_SpeedStep1(){
			return (_bit & 0x1) == 0x1;
		}
		public PSSConfig.AutoUsingSlot get_SpeedStep2(){
			return _SpeedStep2;
		}
		public void set_SpeedStep2(PSSConfig.AutoUsingSlot val){
			_bit |= 0x2;
			_SpeedStep2 = val;
		}
		public boolean has_SpeedStep2(){
			return (_bit & 0x2) == 0x2;
		}
		public PSSConfig.AutoUsingSlot get_ExpBoost(){
			return _ExpBoost;
		}
		public void set_ExpBoost(PSSConfig.AutoUsingSlot val){
			_bit |= 0x4;
			_ExpBoost = val;
		}
		public boolean has_ExpBoost(){
			return (_bit & 0x4) == 0x4;
		}
		public PSSConfig.AutoUsingSlot get_Soup(){
			return _Soup;
		}
		public void set_Soup(PSSConfig.AutoUsingSlot val){
			_bit |= 0x8;
			_Soup = val;
		}
		public boolean has_Soup(){
			return (_bit & 0x8) == 0x8;
		}
		public PSSConfig.AutoUsingSlot get_Cook(){
			return _Cook;
		}
		public void set_Cook(PSSConfig.AutoUsingSlot val){
			_bit |= 0x10;
			_Cook = val;
		}
		public boolean has_Cook(){
			return (_bit & 0x10) == 0x10;
		}
		public PSSConfig.AutoUsingSlot get_PowerUp(){
			return _PowerUp;
		}
		public void set_PowerUp(PSSConfig.AutoUsingSlot val){
			_bit |= 0x20;
			_PowerUp = val;
		}
		public boolean has_PowerUp(){
			return (_bit & 0x20) == 0x20;
		}
		public PSSConfig.AutoUsingSlot get_DragonDiamond(){
			return _DragonDiamond;
		}
		public void set_DragonDiamond(PSSConfig.AutoUsingSlot val){
			_bit |= 0x40;
			_DragonDiamond = val;
		}
		public boolean has_DragonDiamond(){
			return (_bit & 0x40) == 0x40;
		}
		public PSSConfig.AutoUsingSlot get_Polymorph(){
			return _Polymorph;
		}
		public void set_Polymorph(PSSConfig.AutoUsingSlot val){
			_bit |= 0x80;
			_Polymorph = val;
		}
		public boolean has_Polymorph(){
			return (_bit & 0x80) == 0x80;
		}
		public PSSConfig.AutoUsingSlot get_MagicDoll(){
			return _MagicDoll;
		}
		public void set_MagicDoll(PSSConfig.AutoUsingSlot val){
			_bit |= 0x100;
			_MagicDoll = val;
		}
		public boolean has_MagicDoll(){
			return (_bit & 0x100) == 0x100;
		}
		public PSSConfig.AutoUsingSlot get_Sink(){
			return _Sink;
		}
		public void set_Sink(PSSConfig.AutoUsingSlot val){
			_bit |= 0x200;
			_Sink = val;
		}
		public boolean has_Sink(){
			return (_bit & 0x200) == 0x200;
		}
		public PSSConfig.AutoUsingSlot get_Detox(){
			return _Detox;
		}
		public void set_Detox(PSSConfig.AutoUsingSlot val){
			_bit |= 0x400;
			_Detox = val;
		}
		public boolean has_Detox(){
			return (_bit & 0x400) == 0x400;
		}
		public java.util.LinkedList<PSSConfig.AutoUsingSlot> get_BuffSpells(){
			return _BuffSpells;
		}
		public void add_BuffSpells(PSSConfig.AutoUsingSlot val){
			if(!has_BuffSpells()){
				_BuffSpells = new java.util.LinkedList<PSSConfig.AutoUsingSlot>();
				_bit |= 0x800;
			}
			_BuffSpells.add(val);
		}
		public boolean has_BuffSpells(){
			return (_bit & 0x800) == 0x800;
		}
		public java.util.LinkedList<PSSConfig.AutoUsingSlot> get_HPItems(){
			return _HPItems;
		}
		public void add_HPItems(PSSConfig.AutoUsingSlot val){
			if(!has_HPItems()){
				_HPItems = new java.util.LinkedList<PSSConfig.AutoUsingSlot>();
				_bit |= 0x1000;
			}
			_HPItems.add(val);
		}
		public boolean has_HPItems(){
			return (_bit & 0x1000) == 0x1000;
		}
		public java.util.LinkedList<PSSConfig.AutoUsingSlot> get_MPItems(){
			return _MPItems;
		}
		public void add_MPItems(PSSConfig.AutoUsingSlot val){
			if(!has_MPItems()){
				_MPItems = new java.util.LinkedList<PSSConfig.AutoUsingSlot>();
				_bit |= 0x2000;
			}
			_MPItems.add(val);
		}
		public boolean has_MPItems(){
			return (_bit & 0x2000) == 0x2000;
		}
		public PSSConfig.AutoUsingSlot get_Food(){
			return _Food;
		}
		public void set_Food(PSSConfig.AutoUsingSlot val){
			_bit |= 0x4000;
			_Food = val;
		}
		public boolean has_Food(){
			return (_bit & 0x4000) == 0x4000;
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
			if (has_SpeedStep1()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _SpeedStep1);
			}
			if (has_SpeedStep2()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _SpeedStep2);
			}
			if (has_ExpBoost()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _ExpBoost);
			}
			if (has_Soup()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _Soup);
			}
			if (has_Cook()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _Cook);
			}
			if (has_PowerUp()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _PowerUp);
			}
			if (has_DragonDiamond()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _DragonDiamond);
			}
			if (has_Polymorph()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(8, _Polymorph);
			}
			if (has_MagicDoll()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(9, _MagicDoll);
			}
			if (has_Sink()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(10, _Sink);
			}
			if (has_Detox()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(11, _Detox);
			}
			if (has_BuffSpells()){
				for(PSSConfig.AutoUsingSlot val : _BuffSpells){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(12, val);
				}
			}
			if (has_HPItems()){
				for(PSSConfig.AutoUsingSlot val : _HPItems){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(13, val);
				}
			}
			if (has_MPItems()){
				for(PSSConfig.AutoUsingSlot val : _MPItems){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(14, val);
				}
			}
			if (has_Food()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(15, _Food);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_SpeedStep1()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_SpeedStep2()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ExpBoost()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Soup()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Cook()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_PowerUp()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_DragonDiamond()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Polymorph()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_MagicDoll()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Sink()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Detox()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_BuffSpells()){
				for(PSSConfig.AutoUsingSlot val : _BuffSpells){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_HPItems()){
				for(PSSConfig.AutoUsingSlot val : _HPItems){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_MPItems()){
				for(PSSConfig.AutoUsingSlot val : _MPItems){
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
			if (has_SpeedStep1()){
				output.writeMessage(1, _SpeedStep1);
			}
			if (has_SpeedStep2()){
				output.writeMessage(2, _SpeedStep2);
			}
			if (has_ExpBoost()){
				output.writeMessage(3, _ExpBoost);
			}
			if (has_Soup()){
				output.writeMessage(4, _Soup);
			}
			if (has_Cook()){
				output.writeMessage(5, _Cook);
			}
			if (has_PowerUp()){
				output.writeMessage(6, _PowerUp);
			}
			if (has_DragonDiamond()){
				output.writeMessage(7, _DragonDiamond);
			}
			if (has_Polymorph()){
				output.writeMessage(8, _Polymorph);
			}
			if (has_MagicDoll()){
				output.writeMessage(9, _MagicDoll);
			}
			if (has_Sink()){
				output.writeMessage(10, _Sink);
			}
			if (has_Detox()){
				output.writeMessage(11, _Detox);
			}
			if (has_BuffSpells()){
				for (PSSConfig.AutoUsingSlot val : _BuffSpells){
					output.writeMessage(12, val);
				}
			}
			if (has_HPItems()){
				for (PSSConfig.AutoUsingSlot val : _HPItems){
					output.writeMessage(13, val);
				}
			}
			if (has_MPItems()){
				for (PSSConfig.AutoUsingSlot val : _MPItems){
					output.writeMessage(14, val);
				}
			}
			if (has_Food()){
				output.writeMessage(15, _Food);
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
						set_SpeedStep1((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000012:{
						set_SpeedStep2((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_ExpBoost((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000022:{
						set_Soup((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000002A:{
						set_Cook((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000032:{
						set_PowerUp((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000003A:{
						set_DragonDiamond((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000042:{
						set_Polymorph((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000004A:{
						set_MagicDoll((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000052:{
						set_Sink((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000005A:{
						set_Detox((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000062:{
						add_BuffSpells((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000006A:{
						add_HPItems((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000072:{
						add_MPItems((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000007A:{
						set_Food((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
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
			return new BuffConfig();
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
	public static class AttackConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static AttackConfig newInstance(){
			return new AttackConfig();
		}
		private boolean _NormalAttack;
		private int _SearchRange;
		private boolean _MannerMode;
		private boolean _PriorQuestNpc;
		private boolean _PriorAggresiveNpc;
		private int _TryingToggleItem;
		private java.util.LinkedList<PSSConfig.AutoUsingSlot> _HPSpell;
		private java.util.LinkedList<PSSConfig.AutoUsingSlot> _MPSpell;
		private java.util.LinkedList<SlotInfo> _SkillList;
		private SlotInfo _EventAttack;
		private java.util.LinkedList<UsingCondition> _ReturnConditions;
		private java.util.LinkedList<UsingCondition> _ItemReturnConditions;
		private PSSConfig.AttackConfig.GetOption _GetItemOption;
		private int _SwapEquipmentSet;
		private int _ChasingTime;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AttackConfig(){
			set_SearchRange(5);
			set_TryingToggleItem(0);
			set_GetItemOption(PSSConfig.AttackConfig.GetOption.ITEM);
			set_SwapEquipmentSet(1);
			set_ChasingTime(10);
		}
		public boolean get_NormalAttack(){
			return _NormalAttack;
		}
		public void set_NormalAttack(boolean val){
			_bit |= 0x1;
			_NormalAttack = val;
		}
		public boolean has_NormalAttack(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_SearchRange(){
			return _SearchRange;
		}
		public void set_SearchRange(int val){
			_bit |= 0x2;
			_SearchRange = val;
		}
		public boolean has_SearchRange(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_MannerMode(){
			return _MannerMode;
		}
		public void set_MannerMode(boolean val){
			_bit |= 0x4;
			_MannerMode = val;
		}
		public boolean has_MannerMode(){
			return (_bit & 0x4) == 0x4;
		}
		public boolean get_PriorQuestNpc(){
			return _PriorQuestNpc;
		}
		public void set_PriorQuestNpc(boolean val){
			_bit |= 0x8;
			_PriorQuestNpc = val;
		}
		public boolean has_PriorQuestNpc(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_PriorAggresiveNpc(){
			return _PriorAggresiveNpc;
		}
		public void set_PriorAggresiveNpc(boolean val){
			_bit |= 0x10;
			_PriorAggresiveNpc = val;
		}
		public boolean has_PriorAggresiveNpc(){
			return (_bit & 0x10) == 0x10;
		}
		public int get_TryingToggleItem(){
			return _TryingToggleItem;
		}
		public void set_TryingToggleItem(int val){
			_bit |= 0x20;
			_TryingToggleItem = val;
		}
		public boolean has_TryingToggleItem(){
			return (_bit & 0x20) == 0x20;
		}
		public java.util.LinkedList<PSSConfig.AutoUsingSlot> get_HPSpell(){
			return _HPSpell;
		}
		public void add_HPSpell(PSSConfig.AutoUsingSlot val){
			if(!has_HPSpell()){
				_HPSpell = new java.util.LinkedList<PSSConfig.AutoUsingSlot>();
				_bit |= 0x40;
			}
			_HPSpell.add(val);
		}
		public boolean has_HPSpell(){
			return (_bit & 0x40) == 0x40;
		}
		public java.util.LinkedList<PSSConfig.AutoUsingSlot> get_MPSpell(){
			return _MPSpell;
		}
		public void add_MPSpell(PSSConfig.AutoUsingSlot val){
			if(!has_MPSpell()){
				_MPSpell = new java.util.LinkedList<PSSConfig.AutoUsingSlot>();
				_bit |= 0x80;
			}
			_MPSpell.add(val);
		}
		public boolean has_MPSpell(){
			return (_bit & 0x80) == 0x80;
		}
		public java.util.LinkedList<SlotInfo> get_SkillList(){
			return _SkillList;
		}
		public void add_SkillList(SlotInfo val){
			if(!has_SkillList()){
				_SkillList = new java.util.LinkedList<SlotInfo>();
				_bit |= 0x100;
			}
			_SkillList.add(val);
		}
		public boolean has_SkillList(){
			return (_bit & 0x100) == 0x100;
		}
		public SlotInfo get_EventAttack(){
			return _EventAttack;
		}
		public void set_EventAttack(SlotInfo val){
			_bit |= 0x200;
			_EventAttack = val;
		}
		public boolean has_EventAttack(){
			return (_bit & 0x200) == 0x200;
		}
		public java.util.LinkedList<UsingCondition> get_ReturnConditions(){
			return _ReturnConditions;
		}
		public void add_ReturnConditions(UsingCondition val){
			if(!has_ReturnConditions()){
				_ReturnConditions = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x400;
			}
			_ReturnConditions.add(val);
		}
		public boolean has_ReturnConditions(){
			return (_bit & 0x400) == 0x400;
		}
		public java.util.LinkedList<UsingCondition> get_ItemReturnConditions(){
			return _ItemReturnConditions;
		}
		public void add_ItemReturnConditions(UsingCondition val){
			if(!has_ItemReturnConditions()){
				_ItemReturnConditions = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x800;
			}
			_ItemReturnConditions.add(val);
		}
		public boolean has_ItemReturnConditions(){
			return (_bit & 0x800) == 0x800;
		}
		public PSSConfig.AttackConfig.GetOption get_GetItemOption(){
			return _GetItemOption;
		}
		public void set_GetItemOption(PSSConfig.AttackConfig.GetOption val){
			_bit |= 0x1000;
			_GetItemOption = val;
		}
		public boolean has_GetItemOption(){
			return (_bit & 0x1000) == 0x1000;
		}
		public int get_SwapEquipmentSet(){
			return _SwapEquipmentSet;
		}
		public void set_SwapEquipmentSet(int val){
			_bit |= 0x2000;
			_SwapEquipmentSet = val;
		}
		public boolean has_SwapEquipmentSet(){
			return (_bit & 0x2000) == 0x2000;
		}
		public int get_ChasingTime(){
			return _ChasingTime;
		}
		public void set_ChasingTime(int val){
			_bit |= 0x4000;
			_ChasingTime = val;
		}
		public boolean has_ChasingTime(){
			return (_bit & 0x4000) == 0x4000;
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
			if (has_NormalAttack()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _NormalAttack);
			}
			if (has_SearchRange()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _SearchRange);
			}
			if (has_MannerMode()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _MannerMode);
			}
			if (has_PriorQuestNpc()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _PriorQuestNpc);
			}
			if (has_PriorAggresiveNpc()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _PriorAggresiveNpc);
			}
			if (has_TryingToggleItem()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _TryingToggleItem);
			}
			if (has_HPSpell()){
				for(PSSConfig.AutoUsingSlot val : _HPSpell){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, val);
				}
			}
			if (has_MPSpell()){
				for(PSSConfig.AutoUsingSlot val : _MPSpell){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(8, val);
				}
			}
			if (has_SkillList()){
				for(SlotInfo val : _SkillList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(9, val);
				}
			}
			if (has_EventAttack()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(10, _EventAttack);
			}
			if (has_ReturnConditions()){
				for(UsingCondition val : _ReturnConditions){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(11, val);
				}
			}
			if (has_ItemReturnConditions()){
				for(UsingCondition val : _ItemReturnConditions){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(12, val);
				}
			}
			if (has_GetItemOption()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(13, _GetItemOption.toInt());
			}
			if (has_SwapEquipmentSet()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _SwapEquipmentSet);
			}
			if (has_ChasingTime()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(15, _ChasingTime);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_NormalAttack()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_SearchRange()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_MannerMode()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_PriorQuestNpc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_PriorAggresiveNpc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_TryingToggleItem()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_HPSpell()){
				for(PSSConfig.AutoUsingSlot val : _HPSpell){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_MPSpell()){
				for(PSSConfig.AutoUsingSlot val : _MPSpell){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_SkillList()){
				for(SlotInfo val : _SkillList){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_ReturnConditions()){
				for(UsingCondition val : _ReturnConditions){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_ItemReturnConditions()){
				for(UsingCondition val : _ItemReturnConditions){
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
			if (has_NormalAttack()){
				output.writeBool(1, _NormalAttack);
			}
			if (has_SearchRange()){
				output.wirteInt32(2, _SearchRange);
			}
			if (has_MannerMode()){
				output.writeBool(3, _MannerMode);
			}
			if (has_PriorQuestNpc()){
				output.writeBool(4, _PriorQuestNpc);
			}
			if (has_PriorAggresiveNpc()){
				output.writeBool(5, _PriorAggresiveNpc);
			}
			if (has_TryingToggleItem()){
				output.wirteInt32(6, _TryingToggleItem);
			}
			if (has_HPSpell()){
				for (PSSConfig.AutoUsingSlot val : _HPSpell){
					output.writeMessage(7, val);
				}
			}
			if (has_MPSpell()){
				for (PSSConfig.AutoUsingSlot val : _MPSpell){
					output.writeMessage(8, val);
				}
			}
			if (has_SkillList()){
				for (SlotInfo val : _SkillList){
					output.writeMessage(9, val);
				}
			}
			if (has_EventAttack()){
				output.writeMessage(10, _EventAttack);
			}
			if (has_ReturnConditions()){
				for (UsingCondition val : _ReturnConditions){
					output.writeMessage(11, val);
				}
			}
			if (has_ItemReturnConditions()){
				for (UsingCondition val : _ItemReturnConditions){
					output.writeMessage(12, val);
				}
			}
			if (has_GetItemOption()){
				output.writeEnum(13, _GetItemOption.toInt());
			}
			if (has_SwapEquipmentSet()){
				output.writeUInt32(14, _SwapEquipmentSet);
			}
			if (has_ChasingTime()){
				output.writeUInt32(15, _ChasingTime);
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
						set_NormalAttack(input.readBool());
						break;
					}
					case 0x00000010:{
						set_SearchRange(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_MannerMode(input.readBool());
						break;
					}
					case 0x00000020:{
						set_PriorQuestNpc(input.readBool());
						break;
					}
					case 0x00000028:{
						set_PriorAggresiveNpc(input.readBool());
						break;
					}
					case 0x00000030:{
						set_TryingToggleItem(input.readInt32());
						break;
					}
					case 0x0000003A:{
						add_HPSpell((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x00000042:{
						add_MPSpell((PSSConfig.AutoUsingSlot)input.readMessage(PSSConfig.AutoUsingSlot.newInstance()));
						break;
					}
					case 0x0000004A:{
						add_SkillList((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x00000052:{
						set_EventAttack((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x0000005A:{
						add_ReturnConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000062:{
						add_ItemReturnConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000068:{
						set_GetItemOption(PSSConfig.AttackConfig.GetOption.fromInt(input.readEnum()));
						break;
					}
					case 0x00000070:{
						set_SwapEquipmentSet(input.readUInt32());
						break;
					}
					case 0x00000078:{
						set_ChasingTime(input.readUInt32());
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
			return new AttackConfig();
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
		public enum GetOption{
			OFF(0),
			ITEM(1),
			ATTACK(2),
			;
			private int value;
			GetOption(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(GetOption v){
				return value == v.value;
			}
			public static GetOption fromInt(int i){
				switch(i){
				case 0:
					return OFF;
				case 1:
					return ITEM;
				case 2:
					return ATTACK;
				default:
					throw new IllegalArgumentException(String.format("無效參數 GetOption，%d", i));
				}
			}
		}
	}
	public static class ItemConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static ItemConfig newInstance(){
			return new ItemConfig();
		}
		private boolean _DepositObtainItems;
		private boolean _Melting;
		private java.util.LinkedList<SlotInfo> _MeltingItems;
		private java.util.LinkedList<PSSConfig.ItemConfig.PrepareSetting> _InventorySetting;
		private boolean _ObtainMyItemsOnly;
		private boolean _DepositExceedItemsOnly;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ItemConfig(){
			set_ObtainMyItemsOnly(true);
			set_DepositExceedItemsOnly(true);
		}
		public boolean get_DepositObtainItems(){
			return _DepositObtainItems;
		}
		public void set_DepositObtainItems(boolean val){
			_bit |= 0x1;
			_DepositObtainItems = val;
		}
		public boolean has_DepositObtainItems(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_Melting(){
			return _Melting;
		}
		public void set_Melting(boolean val){
			_bit |= 0x2;
			_Melting = val;
		}
		public boolean has_Melting(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<SlotInfo> get_MeltingItems(){
			return _MeltingItems;
		}
		public void add_MeltingItems(SlotInfo val){
			if(!has_MeltingItems()){
				_MeltingItems = new java.util.LinkedList<SlotInfo>();
				_bit |= 0x4;
			}
			_MeltingItems.add(val);
		}
		public boolean has_MeltingItems(){
			return (_bit & 0x4) == 0x4;
		}
		public java.util.LinkedList<PSSConfig.ItemConfig.PrepareSetting> get_InventorySetting(){
			return _InventorySetting;
		}
		public void add_InventorySetting(PSSConfig.ItemConfig.PrepareSetting val){
			if(!has_InventorySetting()){
				_InventorySetting = new java.util.LinkedList<PSSConfig.ItemConfig.PrepareSetting>();
				_bit |= 0x8;
			}
			_InventorySetting.add(val);
		}
		public boolean has_InventorySetting(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_ObtainMyItemsOnly(){
			return _ObtainMyItemsOnly;
		}
		public void set_ObtainMyItemsOnly(boolean val){
			_bit |= 0x10;
			_ObtainMyItemsOnly = val;
		}
		public boolean has_ObtainMyItemsOnly(){
			return (_bit & 0x10) == 0x10;
		}
		public boolean get_DepositExceedItemsOnly(){
			return _DepositExceedItemsOnly;
		}
		public void set_DepositExceedItemsOnly(boolean val){
			_bit |= 0x20;
			_DepositExceedItemsOnly = val;
		}
		public boolean has_DepositExceedItemsOnly(){
			return (_bit & 0x20) == 0x20;
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
			if (has_DepositObtainItems()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _DepositObtainItems);
			}
			if (has_Melting()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _Melting);
			}
			if (has_MeltingItems()){
				for(SlotInfo val : _MeltingItems){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_InventorySetting()){
				for(PSSConfig.ItemConfig.PrepareSetting val : _InventorySetting){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
				}
			}
			if (has_ObtainMyItemsOnly()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _ObtainMyItemsOnly);
			}
			if (has_DepositExceedItemsOnly()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _DepositExceedItemsOnly);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_DepositObtainItems()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Melting()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_MeltingItems()){
				for(SlotInfo val : _MeltingItems){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_InventorySetting()){
				for(PSSConfig.ItemConfig.PrepareSetting val : _InventorySetting){
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
			if (has_DepositObtainItems()){
				output.writeBool(1, _DepositObtainItems);
			}
			if (has_Melting()){
				output.writeBool(2, _Melting);
			}
			if (has_MeltingItems()){
				for (SlotInfo val : _MeltingItems){
					output.writeMessage(3, val);
				}
			}
			if (has_InventorySetting()){
				for (PSSConfig.ItemConfig.PrepareSetting val : _InventorySetting){
					output.writeMessage(4, val);
				}
			}
			if (has_ObtainMyItemsOnly()){
				output.writeBool(5, _ObtainMyItemsOnly);
			}
			if (has_DepositExceedItemsOnly()){
				output.writeBool(6, _DepositExceedItemsOnly);
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
						set_DepositObtainItems(input.readBool());
						break;
					}
					case 0x00000010:{
						set_Melting(input.readBool());
						break;
					}
					case 0x0000001A:{
						add_MeltingItems((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x00000022:{
						add_InventorySetting((PSSConfig.ItemConfig.PrepareSetting)input.readMessage(PSSConfig.ItemConfig.PrepareSetting.newInstance()));
						break;
					}
					case 0x00000028:{
						set_ObtainMyItemsOnly(input.readBool());
						break;
					}
					case 0x00000030:{
						set_DepositExceedItemsOnly(input.readBool());
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
			return new ItemConfig();
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
		public static class PrepareSetting implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static PrepareSetting newInstance(){
				return new PrepareSetting();
			}
			private SlotInfo _itemInfo;
			private int _retrieveCount;
			private boolean _needToBuy;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private PrepareSetting(){
				set_retrieveCount(0);
				set_needToBuy(false);
			}
			public SlotInfo get_itemInfo(){
				return _itemInfo;
			}
			public void set_itemInfo(SlotInfo val){
				_bit |= 0x1;
				_itemInfo = val;
			}
			public boolean has_itemInfo(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_retrieveCount(){
				return _retrieveCount;
			}
			public void set_retrieveCount(int val){
				_bit |= 0x2;
				_retrieveCount = val;
			}
			public boolean has_retrieveCount(){
				return (_bit & 0x2) == 0x2;
			}
			public boolean get_needToBuy(){
				return _needToBuy;
			}
			public void set_needToBuy(boolean val){
				_bit |= 0x4;
				_needToBuy = val;
			}
			public boolean has_needToBuy(){
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
				if (has_itemInfo()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _itemInfo);
				}
				if (has_retrieveCount()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _retrieveCount);
				}
				if (has_needToBuy()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _needToBuy);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_itemInfo()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_retrieveCount()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_needToBuy()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
				if (has_itemInfo()){
					output.writeMessage(1, _itemInfo);
				}
				if (has_retrieveCount()){
					output.writeUInt32(2, _retrieveCount);
				}
				if (has_needToBuy()){
					output.writeBool(3, _needToBuy);
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
							set_itemInfo((SlotInfo)input.readMessage(SlotInfo.newInstance()));
							break;
						}
						case 0x00000010:{
							set_retrieveCount(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_needToBuy(input.readBool());
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
				return new PrepareSetting();
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
	public static class PathConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static PathConfig newInstance(){
			return new PathConfig();
		}
		private boolean _AutoMove;
		private int _RecommendAreaIndex;
		private int _RecordingSelectIndex;
		private int _CustomReturnIndex;
		private boolean _PvpWarning;
		private java.util.LinkedList<PathRecord> _pathRecords;
		private java.util.LinkedList<UsingCondition> _EscapeConditions;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PathConfig(){
			set_AutoMove(true);
			set_RecommendAreaIndex(-1);
			set_RecordingSelectIndex(-1);
			set_CustomReturnIndex(-1);
		}
		public boolean get_AutoMove(){
			return _AutoMove;
		}
		public void set_AutoMove(boolean val){
			_bit |= 0x1;
			_AutoMove = val;
		}
		public boolean has_AutoMove(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_RecommendAreaIndex(){
			return _RecommendAreaIndex;
		}
		public void set_RecommendAreaIndex(int val){
			_bit |= 0x2;
			_RecommendAreaIndex = val;
		}
		public boolean has_RecommendAreaIndex(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_RecordingSelectIndex(){
			return _RecordingSelectIndex;
		}
		public void set_RecordingSelectIndex(int val){
			_bit |= 0x4;
			_RecordingSelectIndex = val;
		}
		public boolean has_RecordingSelectIndex(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_CustomReturnIndex(){
			return _CustomReturnIndex;
		}
		public void set_CustomReturnIndex(int val){
			_bit |= 0x8;
			_CustomReturnIndex = val;
		}
		public boolean has_CustomReturnIndex(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_PvpWarning(){
			return _PvpWarning;
		}
		public void set_PvpWarning(boolean val){
			_bit |= 0x10;
			_PvpWarning = val;
		}
		public boolean has_PvpWarning(){
			return (_bit & 0x10) == 0x10;
		}
		public java.util.LinkedList<PathRecord> get_pathRecords(){
			return _pathRecords;
		}
		public void add_pathRecords(PathRecord val){
			if(!has_pathRecords()){
				_pathRecords = new java.util.LinkedList<PathRecord>();
				_bit |= 0x20;
			}
			_pathRecords.add(val);
		}
		public boolean has_pathRecords(){
			return (_bit & 0x20) == 0x20;
		}
		public java.util.LinkedList<UsingCondition> get_EscapeConditions(){
			return _EscapeConditions;
		}
		public void add_EscapeConditions(UsingCondition val){
			if(!has_EscapeConditions()){
				_EscapeConditions = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x40;
			}
			_EscapeConditions.add(val);
		}
		public boolean has_EscapeConditions(){
			return (_bit & 0x40) == 0x40;
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
			if (has_AutoMove()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _AutoMove);
			}
			if (has_RecommendAreaIndex()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _RecommendAreaIndex);
			}
			if (has_RecordingSelectIndex()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _RecordingSelectIndex);
			}
			if (has_CustomReturnIndex()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _CustomReturnIndex);
			}
			if (has_PvpWarning()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _PvpWarning);
			}
			if (has_pathRecords()){
				for(PathRecord val : _pathRecords){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
				}
			}
			if (has_EscapeConditions()){
				for(UsingCondition val : _EscapeConditions){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_AutoMove()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_RecommendAreaIndex()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_RecordingSelectIndex()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_CustomReturnIndex()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_PvpWarning()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_pathRecords()){
				for(PathRecord val : _pathRecords){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_EscapeConditions()){
				for(UsingCondition val : _EscapeConditions){
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
			if (has_AutoMove()){
				output.writeBool(1, _AutoMove);
			}
			if (has_RecommendAreaIndex()){
				output.wirteInt32(2, _RecommendAreaIndex);
			}
			if (has_RecordingSelectIndex()){
				output.wirteInt32(3, _RecordingSelectIndex);
			}
			if (has_CustomReturnIndex()){
				output.wirteInt32(4, _CustomReturnIndex);
			}
			if (has_PvpWarning()){
				output.writeBool(5, _PvpWarning);
			}
			if (has_pathRecords()){
				for (PathRecord val : _pathRecords){
					output.writeMessage(6, val);
				}
			}
			if (has_EscapeConditions()){
				for (UsingCondition val : _EscapeConditions){
					output.writeMessage(7, val);
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
						set_AutoMove(input.readBool());
						break;
					}
					case 0x00000010:{
						set_RecommendAreaIndex(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_RecordingSelectIndex(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_CustomReturnIndex(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_PvpWarning(input.readBool());
						break;
					}
					case 0x00000032:{
						add_pathRecords((PathRecord)input.readMessage(PathRecord.newInstance()));
						break;
					}
					case 0x0000003A:{
						add_EscapeConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
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
			return new PathConfig();
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
	public static class ReturnConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static ReturnConfig newInstance(){
			return new ReturnConfig();
		}
		private boolean _Melting;
		private java.util.LinkedList<SlotInfo> _MeltingItems;
		private java.util.LinkedList<UsingCondition> _EscapeConditions;
		private java.util.LinkedList<UsingCondition> _ReturnConditions;
		private java.util.LinkedList<UsingCondition> _ItemReturnConditions;
		private boolean _PvpWarning;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ReturnConfig(){
		}
		public boolean get_Melting(){
			return _Melting;
		}
		public void set_Melting(boolean val){
			_bit |= 0x1;
			_Melting = val;
		}
		public boolean has_Melting(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<SlotInfo> get_MeltingItems(){
			return _MeltingItems;
		}
		public void add_MeltingItems(SlotInfo val){
			if(!has_MeltingItems()){
				_MeltingItems = new java.util.LinkedList<SlotInfo>();
				_bit |= 0x2;
			}
			_MeltingItems.add(val);
		}
		public boolean has_MeltingItems(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<UsingCondition> get_EscapeConditions(){
			return _EscapeConditions;
		}
		public void add_EscapeConditions(UsingCondition val){
			if(!has_EscapeConditions()){
				_EscapeConditions = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x4;
			}
			_EscapeConditions.add(val);
		}
		public boolean has_EscapeConditions(){
			return (_bit & 0x4) == 0x4;
		}
		public java.util.LinkedList<UsingCondition> get_ReturnConditions(){
			return _ReturnConditions;
		}
		public void add_ReturnConditions(UsingCondition val){
			if(!has_ReturnConditions()){
				_ReturnConditions = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x8;
			}
			_ReturnConditions.add(val);
		}
		public boolean has_ReturnConditions(){
			return (_bit & 0x8) == 0x8;
		}
		public java.util.LinkedList<UsingCondition> get_ItemReturnConditions(){
			return _ItemReturnConditions;
		}
		public void add_ItemReturnConditions(UsingCondition val){
			if(!has_ItemReturnConditions()){
				_ItemReturnConditions = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x10;
			}
			_ItemReturnConditions.add(val);
		}
		public boolean has_ItemReturnConditions(){
			return (_bit & 0x10) == 0x10;
		}
		public boolean get_PvpWarning(){
			return _PvpWarning;
		}
		public void set_PvpWarning(boolean val){
			_bit |= 0x20;
			_PvpWarning = val;
		}
		public boolean has_PvpWarning(){
			return (_bit & 0x20) == 0x20;
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
			if (has_Melting()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _Melting);
			}
			if (has_MeltingItems()){
				for(SlotInfo val : _MeltingItems){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_EscapeConditions()){
				for(UsingCondition val : _EscapeConditions){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_ReturnConditions()){
				for(UsingCondition val : _ReturnConditions){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
				}
			}
			if (has_ItemReturnConditions()){
				for(UsingCondition val : _ItemReturnConditions){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
				}
			}
			if (has_PvpWarning()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _PvpWarning);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_MeltingItems()){
				for(SlotInfo val : _MeltingItems){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_EscapeConditions()){
				for(UsingCondition val : _EscapeConditions){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_ReturnConditions()){
				for(UsingCondition val : _ReturnConditions){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_ItemReturnConditions()){
				for(UsingCondition val : _ItemReturnConditions){
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
			if (has_Melting()){
				output.writeBool(1, _Melting);
			}
			if (has_MeltingItems()){
				for (SlotInfo val : _MeltingItems){
					output.writeMessage(2, val);
				}
			}
			if (has_EscapeConditions()){
				for (UsingCondition val : _EscapeConditions){
					output.writeMessage(3, val);
				}
			}
			if (has_ReturnConditions()){
				for (UsingCondition val : _ReturnConditions){
					output.writeMessage(4, val);
				}
			}
			if (has_ItemReturnConditions()){
				for (UsingCondition val : _ItemReturnConditions){
					output.writeMessage(5, val);
				}
			}
			if (has_PvpWarning()){
				output.writeBool(6, _PvpWarning);
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
						set_Melting(input.readBool());
						break;
					}
					case 0x00000012:{
						add_MeltingItems((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_EscapeConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000022:{
						add_ReturnConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x0000002A:{
						add_ItemReturnConditions((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000030:{
						set_PvpWarning(input.readBool());
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
			return new ReturnConfig();
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
	public enum SetType{
		SEMI(0),
		ALL(1),
		SURROUND(2),
		;
		private int value;
		SetType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SetType v){
			return value == v.value;
		}
		public static SetType fromInt(int i){
			switch(i){
			case 0:
				return SEMI;
			case 1:
				return ALL;
			case 2:
				return SURROUND;
			default:
				throw new IllegalArgumentException(String.format("參數 SetType 無效，%d", i));
			}
		}
	}
}

package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PlaySupportConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PlaySupportConfig newInstance(){
		return new PlaySupportConfig();
	}
	private ClassID _Class;
	private PlaySupportConfig.BuffConfig _BuffCfg;
	private PlaySupportConfig.RecoveryConfig _RecoverCfg;
	private PlaySupportConfig.EscapeConfig _EscapeCfg;
	private PlaySupportConfig.AttackConfig _AttackCfg;
	private PlaySupportConfig.ReturnConfig _ReturnCfg;
	private PlaySupportConfig.RecommendConfig _RecommendCfg;
	private PlaySupportConfig.HuntingConfig _HuntingCfg;
	private PlaySupportConfig.PrepareConfig _PrepareCfg;
	private PlaySupportConfig.StopConfig _StopCfg;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PlaySupportConfig(){
		set_Class(ClassID.UNKNOWN);
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
	public PlaySupportConfig.BuffConfig get_BuffCfg(){
		return _BuffCfg;
	}
	public void set_BuffCfg(PlaySupportConfig.BuffConfig val){
		_bit |= 0x2;
		_BuffCfg = val;
	}
	public boolean has_BuffCfg(){
		return (_bit & 0x2) == 0x2;
	}
	public PlaySupportConfig.RecoveryConfig get_RecoverCfg(){
		return _RecoverCfg;
	}
	public void set_RecoverCfg(PlaySupportConfig.RecoveryConfig val){
		_bit |= 0x4;
		_RecoverCfg = val;
	}
	public boolean has_RecoverCfg(){
		return (_bit & 0x4) == 0x4;
	}
	public PlaySupportConfig.EscapeConfig get_EscapeCfg(){
		return _EscapeCfg;
	}
	public void set_EscapeCfg(PlaySupportConfig.EscapeConfig val){
		_bit |= 0x8;
		_EscapeCfg = val;
	}
	public boolean has_EscapeCfg(){
		return (_bit & 0x8) == 0x8;
	}
	public PlaySupportConfig.AttackConfig get_AttackCfg(){
		return _AttackCfg;
	}
	public void set_AttackCfg(PlaySupportConfig.AttackConfig val){
		_bit |= 0x10;
		_AttackCfg = val;
	}
	public boolean has_AttackCfg(){
		return (_bit & 0x10) == 0x10;
	}
	public PlaySupportConfig.ReturnConfig get_ReturnCfg(){
		return _ReturnCfg;
	}
	public void set_ReturnCfg(PlaySupportConfig.ReturnConfig val){
		_bit |= 0x20;
		_ReturnCfg = val;
	}
	public boolean has_ReturnCfg(){
		return (_bit & 0x20) == 0x20;
	}
	public PlaySupportConfig.RecommendConfig get_RecommendCfg(){
		return _RecommendCfg;
	}
	public void set_RecommendCfg(PlaySupportConfig.RecommendConfig val){
		_bit |= 0x40;
		_RecommendCfg = val;
	}
	public boolean has_RecommendCfg(){
		return (_bit & 0x40) == 0x40;
	}
	public PlaySupportConfig.HuntingConfig get_HuntingCfg(){
		return _HuntingCfg;
	}
	public void set_HuntingCfg(PlaySupportConfig.HuntingConfig val){
		_bit |= 0x80;
		_HuntingCfg = val;
	}
	public boolean has_HuntingCfg(){
		return (_bit & 0x80) == 0x80;
	}
	public PlaySupportConfig.PrepareConfig get_PrepareCfg(){
		return _PrepareCfg;
	}
	public void set_PrepareCfg(PlaySupportConfig.PrepareConfig val){
		_bit |= 0x100;
		_PrepareCfg = val;
	}
	public boolean has_PrepareCfg(){
		return (_bit & 0x100) == 0x100;
	}
	public PlaySupportConfig.StopConfig get_StopCfg(){
		return _StopCfg;
	}
	public void set_StopCfg(PlaySupportConfig.StopConfig val){
		_bit |= 0x200;
		_StopCfg = val;
	}
	public boolean has_StopCfg(){
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
		if (has_BuffCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _BuffCfg);
		}
		if (has_RecoverCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _RecoverCfg);
		}
		if (has_EscapeCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _EscapeCfg);
		}
		if (has_AttackCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _AttackCfg);
		}
		if (has_ReturnCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _ReturnCfg);
		}
		if (has_RecommendCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _RecommendCfg);
		}
		if (has_HuntingCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(8, _HuntingCfg);
		}
		if (has_PrepareCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(9, _PrepareCfg);
		}
		if (has_StopCfg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(10, _StopCfg);
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
		if (!has_BuffCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_RecoverCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_EscapeCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_AttackCfg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_ReturnCfg()){
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
		if (has_BuffCfg()){
			output.writeMessage(2, _BuffCfg);
		}
		if (has_RecoverCfg()){
			output.writeMessage(3, _RecoverCfg);
		}
		if (has_EscapeCfg()){
			output.writeMessage(4, _EscapeCfg);
		}
		if (has_AttackCfg()){
			output.writeMessage(5, _AttackCfg);
		}
		if (has_ReturnCfg()){
			output.writeMessage(6, _ReturnCfg);
		}
		if (has_RecommendCfg()){
			output.writeMessage(7, _RecommendCfg);
		}
		if (has_HuntingCfg()){
			output.writeMessage(8, _HuntingCfg);
		}
		if (has_PrepareCfg()){
			output.writeMessage(9, _PrepareCfg);
		}
		if (has_StopCfg()){
			output.writeMessage(10, _StopCfg);
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
				case 0x00000012:{
					set_BuffCfg((PlaySupportConfig.BuffConfig)input.readMessage(PlaySupportConfig.BuffConfig.newInstance()));
					break;
				}
				case 0x0000001A:{
					set_RecoverCfg((PlaySupportConfig.RecoveryConfig)input.readMessage(PlaySupportConfig.RecoveryConfig.newInstance()));
					break;
				}
				case 0x00000022:{
					set_EscapeCfg((PlaySupportConfig.EscapeConfig)input.readMessage(PlaySupportConfig.EscapeConfig.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_AttackCfg((PlaySupportConfig.AttackConfig)input.readMessage(PlaySupportConfig.AttackConfig.newInstance()));
					break;
				}
				case 0x00000032:{
					set_ReturnCfg((PlaySupportConfig.ReturnConfig)input.readMessage(PlaySupportConfig.ReturnConfig.newInstance()));
					break;
				}
				case 0x0000003A:{
					set_RecommendCfg((PlaySupportConfig.RecommendConfig)input.readMessage(PlaySupportConfig.RecommendConfig.newInstance()));
					break;
				}
				case 0x00000042:{
					set_HuntingCfg((PlaySupportConfig.HuntingConfig)input.readMessage(PlaySupportConfig.HuntingConfig.newInstance()));
					break;
				}
				case 0x0000004A:{
					set_PrepareCfg((PlaySupportConfig.PrepareConfig)input.readMessage(PlaySupportConfig.PrepareConfig.newInstance()));
					break;
				}
				case 0x00000052:{
					set_StopCfg((PlaySupportConfig.StopConfig)input.readMessage(PlaySupportConfig.StopConfig.newInstance()));
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
		return new PlaySupportConfig();
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
	public static class RecommendConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static RecommendConfig newInstance(){
			return new RecommendConfig();
		}
		private boolean _useAll;
		private boolean _useHuntingArea;
		private int _selectIndex;
		private boolean _useHold;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RecommendConfig(){
			set_useAll(true);
			set_useHuntingArea(false);
			set_selectIndex(0);
			set_useHold(false);
		}
		public boolean get_useAll(){
			return _useAll;
		}
		public void set_useAll(boolean val){
			_bit |= 0x1;
			_useAll = val;
		}
		public boolean has_useAll(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_useHuntingArea(){
			return _useHuntingArea;
		}
		public void set_useHuntingArea(boolean val){
			_bit |= 0x2;
			_useHuntingArea = val;
		}
		public boolean has_useHuntingArea(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_selectIndex(){
			return _selectIndex;
		}
		public void set_selectIndex(int val){
			_bit |= 0x4;
			_selectIndex = val;
		}
		public boolean has_selectIndex(){
			return (_bit & 0x4) == 0x4;
		}
		public boolean get_useHold(){
			return _useHold;
		}
		public void set_useHold(boolean val){
			_bit |= 0x8;
			_useHold = val;
		}
		public boolean has_useHold(){
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
			if (has_useAll()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _useAll);
			}
			if (has_useHuntingArea()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _useHuntingArea);
			}
			if (has_selectIndex()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _selectIndex);
			}
			if (has_useHold()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _useHold);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_useAll()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_useHuntingArea()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_selectIndex()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_useAll()){
				output.writeBool(1, _useAll);
			}
			if (has_useHuntingArea()){
				output.writeBool(2, _useHuntingArea);
			}
			if (has_selectIndex()){
				output.writeUInt32(3, _selectIndex);
			}
			if (has_useHold()){
				output.writeBool(4, _useHold);
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
						set_useAll(input.readBool());
						break;
					}
					case 0x00000010:{
						set_useHuntingArea(input.readBool());
						break;
					}
					case 0x00000018:{
						set_selectIndex(input.readUInt32());
						break;
					}
					case 0x00000020:{
						set_useHold(input.readBool());
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
			return new RecommendConfig();
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
	public static class PrepareConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static PrepareConfig newInstance(){
			return new PrepareConfig();
		}
		private boolean _enable;
		private boolean _activateSetting;
		private boolean _activateStore;
		private boolean _activateStartOption;
		private boolean _activateBuff;
		private java.util.LinkedList<PrepareOption> _prepareItems;
		private PlaySupportConfig.PrepareConfig.StartOption _startOption;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PrepareConfig(){
			set_enable(false);
			set_activateSetting(false);
			set_activateStore(false);
			set_activateStartOption(false);
			set_activateBuff(false);
		}
		public boolean get_enable(){
			return _enable;
		}
		public void set_enable(boolean val){
			_bit |= 0x1;
			_enable = val;
		}
		public boolean has_enable(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_activateSetting(){
			return _activateSetting;
		}
		public void set_activateSetting(boolean val){
			_bit |= 0x2;
			_activateSetting = val;
		}
		public boolean has_activateSetting(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_activateStore(){
			return _activateStore;
		}
		public void set_activateStore(boolean val){
			_bit |= 0x4;
			_activateStore = val;
		}
		public boolean has_activateStore(){
			return (_bit & 0x4) == 0x4;
		}
		public boolean get_activateStartOption(){
			return _activateStartOption;
		}
		public void set_activateStartOption(boolean val){
			_bit |= 0x8;
			_activateStartOption = val;
		}
		public boolean has_activateStartOption(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_activateBuff(){
			return _activateBuff;
		}
		public void set_activateBuff(boolean val){
			_bit |= 0x10;
			_activateBuff = val;
		}
		public boolean has_activateBuff(){
			return (_bit & 0x10) == 0x10;
		}
		public java.util.LinkedList<PrepareOption> get_prepareItems(){
			return _prepareItems;
		}
		public void add_prepareItems(PrepareOption val){
			if(!has_prepareItems()){
				_prepareItems = new java.util.LinkedList<PrepareOption>();
				_bit |= 0x20;
			}
			_prepareItems.add(val);
		}
		public boolean has_prepareItems(){
			return (_bit & 0x20) == 0x20;
		}
		public PlaySupportConfig.PrepareConfig.StartOption get_startOption(){
			return _startOption;
		}
		public void set_startOption(PlaySupportConfig.PrepareConfig.StartOption val){
			_bit |= 0x40;
			_startOption = val;
		}
		public boolean has_startOption(){
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
			if (has_enable()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable);
			}
			if (has_activateSetting()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _activateSetting);
			}
			if (has_activateStore()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _activateStore);
			}
			if (has_activateStartOption()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _activateStartOption);
			}
			if (has_activateBuff()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _activateBuff);
			}
			if (has_prepareItems()){
				for(PrepareOption val : _prepareItems){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
				}
			}
			if (has_startOption()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _startOption);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_enable()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activateSetting()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activateStore()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activateStartOption()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activateBuff()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_prepareItems()){
				for(PrepareOption val : _prepareItems){
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
			if (has_enable()){
				output.writeBool(1, _enable);
			}
			if (has_activateSetting()){
				output.writeBool(2, _activateSetting);
			}
			if (has_activateStore()){
				output.writeBool(3, _activateStore);
			}
			if (has_activateStartOption()){
				output.writeBool(4, _activateStartOption);
			}
			if (has_activateBuff()){
				output.writeBool(5, _activateBuff);
			}
			if (has_prepareItems()){
				for (PrepareOption val : _prepareItems){
					output.writeMessage(6, val);
				}
			}
			if (has_startOption()){
				output.writeMessage(7, _startOption);
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
						set_enable(input.readBool());
						break;
					}
					case 0x00000010:{
						set_activateSetting(input.readBool());
						break;
					}
					case 0x00000018:{
						set_activateStore(input.readBool());
						break;
					}
					case 0x00000020:{
						set_activateStartOption(input.readBool());
						break;
					}
					case 0x00000028:{
						set_activateBuff(input.readBool());
						break;
					}
					case 0x00000032:{
						add_prepareItems((PrepareOption)input.readMessage(PrepareOption.newInstance()));
						break;
					}
					case 0x0000003A:{
						set_startOption((PlaySupportConfig.PrepareConfig.StartOption)input.readMessage(PlaySupportConfig.PrepareConfig.StartOption.newInstance()));
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
			return new PrepareConfig();
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
		public static class StartOption implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static StartOption newInstance(){
				return new StartOption();
			}
			private int _hp;
			private int _mp;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private StartOption(){
				set_hp(0);
				set_mp(0);
			}
			public int get_hp(){
				return _hp;
			}
			public void set_hp(int val){
				_bit |= 0x1;
				_hp = val;
			}
			public boolean has_hp(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_mp(){
				return _mp;
			}
			public void set_mp(int val){
				_bit |= 0x2;
				_mp = val;
			}
			public boolean has_mp(){
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
				if (has_hp()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _hp);
				}
				if (has_mp()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _mp);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_hp()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_mp()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
				if (has_hp()){
					output.writeUInt32(1, _hp);
				}
				if (has_mp()){
					output.writeUInt32(2, _mp);
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
							set_hp(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_mp(input.readUInt32());
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
				return new StartOption();
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
		public enum ItemArrange{
			Default(0),
			Ascending(1),
			;
			private int value;
			ItemArrange(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(ItemArrange v){
				return value == v.value;
			}
			public static ItemArrange fromInt(int i){
				switch(i){
				case 0:
					return Default;
				case 1:
					return Ascending;
				default:
					throw new IllegalArgumentException(String.format("無效參數 ItemArrange，%d", i));
				}
			}
		}
	}
	public static class HuntingConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static HuntingConfig newInstance(){
			return new HuntingConfig();
		}
		private boolean _enable;
		private boolean _activateCustomMove;
		private int _selectIndex;
		private java.util.LinkedList<PathRecord> _pathRecords;
		private java.util.LinkedList<SlotInfo> _teleportSlotList;
		private boolean _activateHoldHunting;
		private boolean _activateTeleport;
		private int _teleportSec;
		private int _searchRange;
		private boolean _exceptHuntedNpc;
		private boolean _priorQuestNpc;
		private boolean _priorAggresiveNpc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private HuntingConfig(){
			set_activateHoldHunting(false);
			set_activateTeleport(false);
			set_teleportSec(0);
			set_searchRange(10);
			set_exceptHuntedNpc(false);
			set_priorQuestNpc(false);
			set_priorAggresiveNpc(false);
		}
		public boolean get_enable(){
			return _enable;
		}
		public void set_enable(boolean val){
			_bit |= 0x1;
			_enable = val;
		}
		public boolean has_enable(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_activateCustomMove(){
			return _activateCustomMove;
		}
		public void set_activateCustomMove(boolean val){
			_bit |= 0x2;
			_activateCustomMove = val;
		}
		public boolean has_activateCustomMove(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_selectIndex(){
			return _selectIndex;
		}
		public void set_selectIndex(int val){
			_bit |= 0x4;
			_selectIndex = val;
		}
		public boolean has_selectIndex(){
			return (_bit & 0x4) == 0x4;
		}
		public java.util.LinkedList<PathRecord> get_pathRecords(){
			return _pathRecords;
		}
		public void add_pathRecords(PathRecord val){
			if(!has_pathRecords()){
				_pathRecords = new java.util.LinkedList<PathRecord>();
				_bit |= 0x8;
			}
			_pathRecords.add(val);
		}
		public boolean has_pathRecords(){
			return (_bit & 0x8) == 0x8;
		}
		public java.util.LinkedList<SlotInfo> get_teleportSlotList(){
			return _teleportSlotList;
		}
		public void add_teleportSlotList(SlotInfo val){
			if(!has_teleportSlotList()){
				_teleportSlotList = new java.util.LinkedList<SlotInfo>();
				_bit |= 0x10;
			}
			_teleportSlotList.add(val);
		}
		public boolean has_teleportSlotList(){
			return (_bit & 0x10) == 0x10;
		}
		public boolean get_activateHoldHunting(){
			return _activateHoldHunting;
		}
		public void set_activateHoldHunting(boolean val){
			_bit |= 0x20;
			_activateHoldHunting = val;
		}
		public boolean has_activateHoldHunting(){
			return (_bit & 0x20) == 0x20;
		}
		public boolean get_activateTeleport(){
			return _activateTeleport;
		}
		public void set_activateTeleport(boolean val){
			_bit |= 0x40;
			_activateTeleport = val;
		}
		public boolean has_activateTeleport(){
			return (_bit & 0x40) == 0x40;
		}
		public int get_teleportSec(){
			return _teleportSec;
		}
		public void set_teleportSec(int val){
			_bit |= 0x80;
			_teleportSec = val;
		}
		public boolean has_teleportSec(){
			return (_bit & 0x80) == 0x80;
		}
		public int get_searchRange(){
			return _searchRange;
		}
		public void set_searchRange(int val){
			_bit |= 0x100;
			_searchRange = val;
		}
		public boolean has_searchRange(){
			return (_bit & 0x100) == 0x100;
		}
		public boolean get_exceptHuntedNpc(){
			return _exceptHuntedNpc;
		}
		public void set_exceptHuntedNpc(boolean val){
			_bit |= 0x200;
			_exceptHuntedNpc = val;
		}
		public boolean has_exceptHuntedNpc(){
			return (_bit & 0x200) == 0x200;
		}
		public boolean get_priorQuestNpc(){
			return _priorQuestNpc;
		}
		public void set_priorQuestNpc(boolean val){
			_bit |= 0x400;
			_priorQuestNpc = val;
		}
		public boolean has_priorQuestNpc(){
			return (_bit & 0x400) == 0x400;
		}
		public boolean get_priorAggresiveNpc(){
			return _priorAggresiveNpc;
		}
		public void set_priorAggresiveNpc(boolean val){
			_bit |= 0x800;
			_priorAggresiveNpc = val;
		}
		public boolean has_priorAggresiveNpc(){
			return (_bit & 0x800) == 0x800;
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
			if (has_enable()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable);
			}
			if (has_activateCustomMove()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _activateCustomMove);
			}
			if (has_selectIndex()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _selectIndex);
			}
			if (has_pathRecords()){
				for(PathRecord val : _pathRecords){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
				}
			}
			if (has_teleportSlotList()){
				for(SlotInfo val : _teleportSlotList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
				}
			}
			if (has_activateHoldHunting()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _activateHoldHunting);
			}
			if (has_activateTeleport()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _activateTeleport);
			}
			if (has_teleportSec()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _teleportSec);
			}
			if (has_searchRange()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _searchRange);
			}
			if (has_exceptHuntedNpc()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(10, _exceptHuntedNpc);
			}
			if (has_priorQuestNpc()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _priorQuestNpc);
			}
			if (has_priorAggresiveNpc()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(12, _priorAggresiveNpc);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_enable()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activateCustomMove()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_selectIndex()){
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
			if (has_teleportSlotList()){
				for(SlotInfo val : _teleportSlotList){
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
			if (has_enable()){
				output.writeBool(1, _enable);
			}
			if (has_activateCustomMove()){
				output.writeBool(2, _activateCustomMove);
			}
			if (has_selectIndex()){
				output.wirteInt32(3, _selectIndex);
			}
			if (has_pathRecords()){
				for (PathRecord val : _pathRecords){
					output.writeMessage(4, val);
				}
			}
			if (has_teleportSlotList()){
				for (SlotInfo val : _teleportSlotList){
					output.writeMessage(5, val);
				}
			}
			if (has_activateHoldHunting()){
				output.writeBool(6, _activateHoldHunting);
			}
			if (has_activateTeleport()){
				output.writeBool(7, _activateTeleport);
			}
			if (has_teleportSec()){
				output.wirteInt32(8, _teleportSec);
			}
			if (has_searchRange()){
				output.wirteInt32(9, _searchRange);
			}
			if (has_exceptHuntedNpc()){
				output.writeBool(10, _exceptHuntedNpc);
			}
			if (has_priorQuestNpc()){
				output.writeBool(11, _priorQuestNpc);
			}
			if (has_priorAggresiveNpc()){
				output.writeBool(12, _priorAggresiveNpc);
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
						set_enable(input.readBool());
						break;
					}
					case 0x00000010:{
						set_activateCustomMove(input.readBool());
						break;
					}
					case 0x00000018:{
						set_selectIndex(input.readInt32());
						break;
					}
					case 0x00000022:{
						add_pathRecords((PathRecord)input.readMessage(PathRecord.newInstance()));
						break;
					}
					case 0x0000002A:{
						add_teleportSlotList((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x00000030:{
						set_activateHoldHunting(input.readBool());
						break;
					}
					case 0x00000038:{
						set_activateTeleport(input.readBool());
						break;
					}
					case 0x00000040:{
						set_teleportSec(input.readInt32());
						break;
					}
					case 0x00000048:{
						set_searchRange(input.readInt32());
						break;
					}
					case 0x00000050:{
						set_exceptHuntedNpc(input.readBool());
						break;
					}
					case 0x00000058:{
						set_priorQuestNpc(input.readBool());
						break;
					}
					case 0x00000060:{
						set_priorAggresiveNpc(input.readBool());
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
			return new HuntingConfig();
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
		private boolean _activateBasicAttack;
		private java.util.LinkedList<SlotInfo> _skillList;
		private java.util.LinkedList<AutoUsingInfo> _customSkillList;
		private boolean _activateSkillAttack;
		private boolean _activateCustomSkillAttack;
		private boolean _activateGetItem;
		private int _tryCount;
		private boolean _giveUpItem;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AttackConfig(){
			set_activateSkillAttack(false);
			set_activateCustomSkillAttack(false);
			set_activateGetItem(false);
			set_tryCount(0);
			set_giveUpItem(false);
		}
		public boolean get_activateBasicAttack(){
			return _activateBasicAttack;
		}
		public void set_activateBasicAttack(boolean val){
			_bit |= 0x1;
			_activateBasicAttack = val;
		}
		public boolean has_activateBasicAttack(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<SlotInfo> get_skillList(){
			return _skillList;
		}
		public void add_skillList(SlotInfo val){
			if(!has_skillList()){
				_skillList = new java.util.LinkedList<SlotInfo>();
				_bit |= 0x2;
			}
			_skillList.add(val);
		}
		public boolean has_skillList(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<AutoUsingInfo> get_customSkillList(){
			return _customSkillList;
		}
		public void add_customSkillList(AutoUsingInfo val){
			if(!has_customSkillList()){
				_customSkillList = new java.util.LinkedList<AutoUsingInfo>();
				_bit |= 0x4;
			}
			_customSkillList.add(val);
		}
		public boolean has_customSkillList(){
			return (_bit & 0x4) == 0x4;
		}
		public boolean get_activateSkillAttack(){
			return _activateSkillAttack;
		}
		public void set_activateSkillAttack(boolean val){
			_bit |= 0x8;
			_activateSkillAttack = val;
		}
		public boolean has_activateSkillAttack(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_activateCustomSkillAttack(){
			return _activateCustomSkillAttack;
		}
		public void set_activateCustomSkillAttack(boolean val){
			_bit |= 0x10;
			_activateCustomSkillAttack = val;
		}
		public boolean has_activateCustomSkillAttack(){
			return (_bit & 0x10) == 0x10;
		}
		public boolean get_activateGetItem(){
			return _activateGetItem;
		}
		public void set_activateGetItem(boolean val){
			_bit |= 0x20;
			_activateGetItem = val;
		}
		public boolean has_activateGetItem(){
			return (_bit & 0x20) == 0x20;
		}
		public int get_tryCount(){
			return _tryCount;
		}
		public void set_tryCount(int val){
			_bit |= 0x40;
			_tryCount = val;
		}
		public boolean has_tryCount(){
			return (_bit & 0x40) == 0x40;
		}
		public boolean get_giveUpItem(){
			return _giveUpItem;
		}
		public void set_giveUpItem(boolean val){
			_bit |= 0x80;
			_giveUpItem = val;
		}
		public boolean has_giveUpItem(){
			return (_bit & 0x80) == 0x80;
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
			if (has_activateBasicAttack()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _activateBasicAttack);
			}
			if (has_skillList()){
				for(SlotInfo val : _skillList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_customSkillList()){
				for(AutoUsingInfo val : _customSkillList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_activateSkillAttack()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _activateSkillAttack);
			}
			if (has_activateCustomSkillAttack()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _activateCustomSkillAttack);
			}
			if (has_activateGetItem()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _activateGetItem);
			}
			if (has_tryCount()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _tryCount);
			}
			if (has_giveUpItem()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(8, _giveUpItem);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_activateBasicAttack()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_skillList()){
				for(SlotInfo val : _skillList){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_customSkillList()){
				for(AutoUsingInfo val : _customSkillList){
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
			if (has_activateBasicAttack()){
				output.writeBool(1, _activateBasicAttack);
			}
			if (has_skillList()){
				for (SlotInfo val : _skillList){
					output.writeMessage(2, val);
				}
			}
			if (has_customSkillList()){
				for (AutoUsingInfo val : _customSkillList){
					output.writeMessage(3, val);
				}
			}
			if (has_activateSkillAttack()){
				output.writeBool(4, _activateSkillAttack);
			}
			if (has_activateCustomSkillAttack()){
				output.writeBool(5, _activateCustomSkillAttack);
			}
			if (has_activateGetItem()){
				output.writeBool(6, _activateGetItem);
			}
			if (has_tryCount()){
				output.wirteInt32(7, _tryCount);
			}
			if (has_giveUpItem()){
				output.writeBool(8, _giveUpItem);
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
						set_activateBasicAttack(input.readBool());
						break;
					}
					case 0x00000012:{
						add_skillList((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_customSkillList((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
						break;
					}
					case 0x00000020:{
						set_activateSkillAttack(input.readBool());
						break;
					}
					case 0x00000028:{
						set_activateCustomSkillAttack(input.readBool());
						break;
					}
					case 0x00000030:{
						set_activateGetItem(input.readBool());
						break;
					}
					case 0x00000038:{
						set_tryCount(input.readInt32());
						break;
					}
					case 0x00000040:{
						set_giveUpItem(input.readBool());
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
	}
	public static class RecoveryConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static RecoveryConfig newInstance(){
			return new RecoveryConfig();
		}
		private boolean _enable;
		private AutoUsingInfo _hp1;
		private AutoUsingInfo _mp;
		private AutoUsingInfo _hp2;
		private AutoUsingInfo _detox;
		private AutoUsingInfo _sink;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RecoveryConfig(){
		}
		public boolean get_enable(){
			return _enable;
		}
		public void set_enable(boolean val){
			_bit |= 0x1;
			_enable = val;
		}
		public boolean has_enable(){
			return (_bit & 0x1) == 0x1;
		}
		public AutoUsingInfo get_hp1(){
			return _hp1;
		}
		public void set_hp1(AutoUsingInfo val){
			_bit |= 0x2;
			_hp1 = val;
		}
		public boolean has_hp1(){
			return (_bit & 0x2) == 0x2;
		}
		public AutoUsingInfo get_mp(){
			return _mp;
		}
		public void set_mp(AutoUsingInfo val){
			_bit |= 0x4;
			_mp = val;
		}
		public boolean has_mp(){
			return (_bit & 0x4) == 0x4;
		}
		public AutoUsingInfo get_hp2(){
			return _hp2;
		}
		public void set_hp2(AutoUsingInfo val){
			_bit |= 0x8;
			_hp2 = val;
		}
		public boolean has_hp2(){
			return (_bit & 0x8) == 0x8;
		}
		public AutoUsingInfo get_detox(){
			return _detox;
		}
		public void set_detox(AutoUsingInfo val){
			_bit |= 0x10;
			_detox = val;
		}
		public boolean has_detox(){
			return (_bit & 0x10) == 0x10;
		}
		public AutoUsingInfo get_sink(){
			return _sink;
		}
		public void set_sink(AutoUsingInfo val){
			_bit |= 0x20;
			_sink = val;
		}
		public boolean has_sink(){
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
			if (has_enable()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable);
			}
			if (has_hp1()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _hp1);
			}
			if (has_mp()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _mp);
			}
			if (has_hp2()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _hp2);
			}
			if (has_detox()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _detox);
			}
			if (has_sink()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _sink);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_enable()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_hp1()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_mp()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_enable()){
				output.writeBool(1, _enable);
			}
			if (has_hp1()){
				output.writeMessage(2, _hp1);
			}
			if (has_mp()){
				output.writeMessage(3, _mp);
			}
			if (has_hp2()){
				output.writeMessage(4, _hp2);
			}
			if (has_detox()){
				output.writeMessage(5, _detox);
			}
			if (has_sink()){
				output.writeMessage(6, _sink);
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
						set_enable(input.readBool());
						break;
					}
					case 0x00000012:{
						set_hp1((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_mp((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
						break;
					}
					case 0x00000022:{
						set_hp2((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
						break;
					}
					case 0x0000002A:{
						set_detox((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
						break;
					}
					case 0x00000032:{
						set_sink((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
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
			return new RecoveryConfig();
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
		private boolean _enable;
		private boolean _activateAutoUsing;
		private boolean _activatePolymoprh;
		private AutoUsingInfo _polymorphList;
		private boolean _activateTimerUsing;
		private java.util.LinkedList<AutoUsingInfo> _autoUsingList;
		private java.util.LinkedList<AutoUsingInfo> _timerUsingList;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BuffConfig(){
		}
		public boolean get_enable(){
			return _enable;
		}
		public void set_enable(boolean val){
			_bit |= 0x1;
			_enable = val;
		}
		public boolean has_enable(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_activateAutoUsing(){
			return _activateAutoUsing;
		}
		public void set_activateAutoUsing(boolean val){
			_bit |= 0x2;
			_activateAutoUsing = val;
		}
		public boolean has_activateAutoUsing(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_activatePolymoprh(){
			return _activatePolymoprh;
		}
		public void set_activatePolymoprh(boolean val){
			_bit |= 0x4;
			_activatePolymoprh = val;
		}
		public boolean has_activatePolymoprh(){
			return (_bit & 0x4) == 0x4;
		}
		public AutoUsingInfo get_polymorphList(){
			return _polymorphList;
		}
		public void set_polymorphList(AutoUsingInfo val){
			_bit |= 0x8;
			_polymorphList = val;
		}
		public boolean has_polymorphList(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_activateTimerUsing(){
			return _activateTimerUsing;
		}
		public void set_activateTimerUsing(boolean val){
			_bit |= 0x10;
			_activateTimerUsing = val;
		}
		public boolean has_activateTimerUsing(){
			return (_bit & 0x10) == 0x10;
		}
		public java.util.LinkedList<AutoUsingInfo> get_autoUsingList(){
			return _autoUsingList;
		}
		public void add_autoUsingList(AutoUsingInfo val){
			if(!has_autoUsingList()){
				_autoUsingList = new java.util.LinkedList<AutoUsingInfo>();
				_bit |= 0x20;
			}
			_autoUsingList.add(val);
		}
		public boolean has_autoUsingList(){
			return (_bit & 0x20) == 0x20;
		}
		public java.util.LinkedList<AutoUsingInfo> get_timerUsingList(){
			return _timerUsingList;
		}
		public void add_timerUsingList(AutoUsingInfo val){
			if(!has_timerUsingList()){
				_timerUsingList = new java.util.LinkedList<AutoUsingInfo>();
				_bit |= 0x40;
			}
			_timerUsingList.add(val);
		}
		public boolean has_timerUsingList(){
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
			if (has_enable()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable);
			}
			if (has_activateAutoUsing()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _activateAutoUsing);
			}
			if (has_activatePolymoprh()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _activatePolymoprh);
			}
			if (has_polymorphList()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _polymorphList);
			}
			if (has_activateTimerUsing()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _activateTimerUsing);
			}
			if (has_autoUsingList()){
				for(AutoUsingInfo val : _autoUsingList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
				}
			}
			if (has_timerUsingList()){
				for(AutoUsingInfo val : _timerUsingList){
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
			if (!has_enable()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activateAutoUsing()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activatePolymoprh()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_polymorphList()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_activateTimerUsing()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_autoUsingList()){
				for(AutoUsingInfo val : _autoUsingList){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_timerUsingList()){
				for(AutoUsingInfo val : _timerUsingList){
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
			if (has_enable()){
				output.writeBool(1, _enable);
			}
			if (has_activateAutoUsing()){
				output.writeBool(2, _activateAutoUsing);
			}
			if (has_activatePolymoprh()){
				output.writeBool(3, _activatePolymoprh);
			}
			if (has_polymorphList()){
				output.writeMessage(4, _polymorphList);
			}
			if (has_activateTimerUsing()){
				output.writeBool(5, _activateTimerUsing);
			}
			if (has_autoUsingList()){
				for (AutoUsingInfo val : _autoUsingList){
					output.writeMessage(6, val);
				}
			}
			if (has_timerUsingList()){
				for (AutoUsingInfo val : _timerUsingList){
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
						set_enable(input.readBool());
						break;
					}
					case 0x00000010:{
						set_activateAutoUsing(input.readBool());
						break;
					}
					case 0x00000018:{
						set_activatePolymoprh(input.readBool());
						break;
					}
					case 0x00000022:{
						set_polymorphList((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
						break;
					}
					case 0x00000028:{
						set_activateTimerUsing(input.readBool());
						break;
					}
					case 0x00000032:{
						add_autoUsingList((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
						break;
					}
					case 0x0000003A:{
						add_timerUsingList((AutoUsingInfo)input.readMessage(AutoUsingInfo.newInstance()));
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
	public static class EscapeConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static EscapeConfig newInstance(){
			return new EscapeConfig();
		}
		private boolean _enable;
		private java.util.LinkedList<SlotInfo> _usingSlot;
		private java.util.LinkedList<UsingCondition> _conditionList;
		private boolean _usePvpWarning;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EscapeConfig(){
		}
		public boolean get_enable(){
			return _enable;
		}
		public void set_enable(boolean val){
			_bit |= 0x1;
			_enable = val;
		}
		public boolean has_enable(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<SlotInfo> get_usingSlot(){
			return _usingSlot;
		}
		public void add_usingSlot(SlotInfo val){
			if(!has_usingSlot()){
				_usingSlot = new java.util.LinkedList<SlotInfo>();
				_bit |= 0x2;
			}
			_usingSlot.add(val);
		}
		public boolean has_usingSlot(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<UsingCondition> get_conditionList(){
			return _conditionList;
		}
		public void add_conditionList(UsingCondition val){
			if(!has_conditionList()){
				_conditionList = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x4;
			}
			_conditionList.add(val);
		}
		public boolean has_conditionList(){
			return (_bit & 0x4) == 0x4;
		}
		public boolean get_usePvpWarning(){
			return _usePvpWarning;
		}
		public void set_usePvpWarning(boolean val){
			_bit |= 0x8;
			_usePvpWarning = val;
		}
		public boolean has_usePvpWarning(){
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
			if (has_enable()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable);
			}
			if (has_usingSlot()){
				for(SlotInfo val : _usingSlot){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_conditionList()){
				for(UsingCondition val : _conditionList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_usePvpWarning()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _usePvpWarning);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_enable()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_usingSlot()){
				for(SlotInfo val : _usingSlot){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_conditionList()){
				for(UsingCondition val : _conditionList){
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
			if (has_enable()){
				output.writeBool(1, _enable);
			}
			if (has_usingSlot()){
				for (SlotInfo val : _usingSlot){
					output.writeMessage(2, val);
				}
			}
			if (has_conditionList()){
				for (UsingCondition val : _conditionList){
					output.writeMessage(3, val);
				}
			}
			if (has_usePvpWarning()){
				output.writeBool(4, _usePvpWarning);
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
						set_enable(input.readBool());
						break;
					}
					case 0x00000012:{
						add_usingSlot((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_conditionList((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000020:{
						set_usePvpWarning(input.readBool());
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
			return new EscapeConfig();
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
		private boolean _activateReturn;
		private java.util.LinkedList<UsingCondition> _conditionList;
		private boolean _activateCustomReturn;
		private int _customReturnIndex;
		private SlotInfo _returnSlot;
		private boolean _activateRecoverExp;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ReturnConfig(){
			set_activateCustomReturn(false);
			set_customReturnIndex(0);
			set_activateRecoverExp(false);
		}
		public boolean get_activateReturn(){
			return _activateReturn;
		}
		public void set_activateReturn(boolean val){
			_bit |= 0x1;
			_activateReturn = val;
		}
		public boolean has_activateReturn(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<UsingCondition> get_conditionList(){
			return _conditionList;
		}
		public void add_conditionList(UsingCondition val){
			if(!has_conditionList()){
				_conditionList = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x2;
			}
			_conditionList.add(val);
		}
		public boolean has_conditionList(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_activateCustomReturn(){
			return _activateCustomReturn;
		}
		public void set_activateCustomReturn(boolean val){
			_bit |= 0x4;
			_activateCustomReturn = val;
		}
		public boolean has_activateCustomReturn(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_customReturnIndex(){
			return _customReturnIndex;
		}
		public void set_customReturnIndex(int val){
			_bit |= 0x8;
			_customReturnIndex = val;
		}
		public boolean has_customReturnIndex(){
			return (_bit & 0x8) == 0x8;
		}
		public SlotInfo get_returnSlot(){
			return _returnSlot;
		}
		public void set_returnSlot(SlotInfo val){
			_bit |= 0x10;
			_returnSlot = val;
		}
		public boolean has_returnSlot(){
			return (_bit & 0x10) == 0x10;
		}
		public boolean get_activateRecoverExp(){
			return _activateRecoverExp;
		}
		public void set_activateRecoverExp(boolean val){
			_bit |= 0x20;
			_activateRecoverExp = val;
		}
		public boolean has_activateRecoverExp(){
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
			if (has_activateReturn()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _activateReturn);
			}
			if (has_conditionList()){
				for(UsingCondition val : _conditionList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_activateCustomReturn()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _activateCustomReturn);
			}
			if (has_customReturnIndex()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _customReturnIndex);
			}
			if (has_returnSlot()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _returnSlot);
			}
			if (has_activateRecoverExp()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _activateRecoverExp);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_activateReturn()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_conditionList()){
				for(UsingCondition val : _conditionList){
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
			if (has_activateReturn()){
				output.writeBool(1, _activateReturn);
			}
			if (has_conditionList()){
				for (UsingCondition val : _conditionList){
					output.writeMessage(2, val);
				}
			}
			if (has_activateCustomReturn()){
				output.writeBool(3, _activateCustomReturn);
			}
			if (has_customReturnIndex()){
				output.wirteInt32(4, _customReturnIndex);
			}
			if (has_returnSlot()){
				output.writeMessage(5, _returnSlot);
			}
			if (has_activateRecoverExp()){
				output.writeBool(6, _activateRecoverExp);
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
						set_activateReturn(input.readBool());
						break;
					}
					case 0x00000012:{
						add_conditionList((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000018:{
						set_activateCustomReturn(input.readBool());
						break;
					}
					case 0x00000020:{
						set_customReturnIndex(input.readInt32());
						break;
					}
					case 0x0000002A:{
						set_returnSlot((SlotInfo)input.readMessage(SlotInfo.newInstance()));
						break;
					}
					case 0x00000030:{
						set_activateRecoverExp(input.readBool());
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
	public static class StopConfig implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static StopConfig newInstance(){
			return new StopConfig();
		}
		private boolean _enable;
		private java.util.LinkedList<UsingCondition> _conditionList;
		private boolean _activateMobile;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StopConfig(){
		}
		public boolean get_enable(){
			return _enable;
		}
		public void set_enable(boolean val){
			_bit |= 0x1;
			_enable = val;
		}
		public boolean has_enable(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<UsingCondition> get_conditionList(){
			return _conditionList;
		}
		public void add_conditionList(UsingCondition val){
			if(!has_conditionList()){
				_conditionList = new java.util.LinkedList<UsingCondition>();
				_bit |= 0x2;
			}
			_conditionList.add(val);
		}
		public boolean has_conditionList(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_activateMobile(){
			return _activateMobile;
		}
		public void set_activateMobile(boolean val){
			_bit |= 0x4;
			_activateMobile = val;
		}
		public boolean has_activateMobile(){
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
			if (has_enable()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable);
			}
			if (has_conditionList()){
				for(UsingCondition val : _conditionList){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_activateMobile()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _activateMobile);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_enable()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_conditionList()){
				for(UsingCondition val : _conditionList){
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
			if (has_enable()){
				output.writeBool(1, _enable);
			}
			if (has_conditionList()){
				for (UsingCondition val : _conditionList){
					output.writeMessage(2, val);
				}
			}
			if (has_activateMobile()){
				output.writeBool(3, _activateMobile);
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
						set_enable(input.readBool());
						break;
					}
					case 0x00000012:{
						add_conditionList((UsingCondition)input.readMessage(UsingCondition.newInstance()));
						break;
					}
					case 0x00000018:{
						set_activateMobile(input.readBool());
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
			return new StopConfig();
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

package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class EinhasadPointStatInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static EinhasadPointStatInfoT newInstance(){
		return new EinhasadPointStatInfoT();
	}
	private int _eachStatMax;
	private int _totalStatMax;
	private java.util.LinkedList<EinhasadPointStatInfoT.EnchantCostT> _EnchantCost;
	private java.util.LinkedList<EinhasadPointStatInfoT.StatMetaDataT> _StatMetaData;
	private java.util.LinkedList<EinhasadPointStatInfoT.StatT> _Stat;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EinhasadPointStatInfoT(){
	}
	public int get_eachStatMax(){
		return _eachStatMax;
	}
	public void set_eachStatMax(int val){
		_bit |= 0x1;
		_eachStatMax = val;
	}
	public boolean has_eachStatMax(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_totalStatMax(){
		return _totalStatMax;
	}
	public void set_totalStatMax(int val){
		_bit |= 0x2;
		_totalStatMax = val;
	}
	public boolean has_totalStatMax(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.EnchantCostT> get_EnchantCost(){
		return _EnchantCost;
	}
	public void add_EnchantCost(EinhasadPointStatInfoT.EnchantCostT val){
		if(!has_EnchantCost()){
			_EnchantCost = new java.util.LinkedList<EinhasadPointStatInfoT.EnchantCostT>();
			_bit |= 0x4;
		}
		_EnchantCost.add(val);
	}
	public boolean has_EnchantCost(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.StatMetaDataT> get_StatMetaData(){
		return _StatMetaData;
	}
	public void add_StatMetaData(EinhasadPointStatInfoT.StatMetaDataT val){
		if(!has_StatMetaData()){
			_StatMetaData = new java.util.LinkedList<EinhasadPointStatInfoT.StatMetaDataT>();
			_bit |= 0x8;
		}
		_StatMetaData.add(val);
	}
	public boolean has_StatMetaData(){
		return (_bit & 0x8) == 0x8;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.StatT> get_Stat(){
		return _Stat;
	}
	public void add_Stat(EinhasadPointStatInfoT.StatT val){
		if(!has_Stat()){
			_Stat = new java.util.LinkedList<EinhasadPointStatInfoT.StatT>();
			_bit |= 0x10;
		}
		_Stat.add(val);
	}
	public boolean has_Stat(){
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
		if (has_eachStatMax()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _eachStatMax);
		}
		if (has_totalStatMax()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _totalStatMax);
		}
		if (has_EnchantCost()){
			for(EinhasadPointStatInfoT.EnchantCostT val : _EnchantCost){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_StatMetaData()){
			for(EinhasadPointStatInfoT.StatMetaDataT val : _StatMetaData){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		if (has_Stat()){
			for(EinhasadPointStatInfoT.StatT val : _Stat){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_eachStatMax()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_totalStatMax()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_EnchantCost()){
			for(EinhasadPointStatInfoT.EnchantCostT val : _EnchantCost){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_StatMetaData()){
			for(EinhasadPointStatInfoT.StatMetaDataT val : _StatMetaData){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_Stat()){
			for(EinhasadPointStatInfoT.StatT val : _Stat){
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
		if (has_eachStatMax()){
			output.wirteInt32(1, _eachStatMax);
		}
		if (has_totalStatMax()){
			output.wirteInt32(2, _totalStatMax);
		}
		if (has_EnchantCost()){
			for (EinhasadPointStatInfoT.EnchantCostT val : _EnchantCost){
				output.writeMessage(3, val);
			}
		}
		if (has_StatMetaData()){
			for (EinhasadPointStatInfoT.StatMetaDataT val : _StatMetaData){
				output.writeMessage(4, val);
			}
		}
		if (has_Stat()){
			for (EinhasadPointStatInfoT.StatT val : _Stat){
				output.writeMessage(5, val);
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
					set_eachStatMax(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_totalStatMax(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_EnchantCost((EinhasadPointStatInfoT.EnchantCostT)input.readMessage(EinhasadPointStatInfoT.EnchantCostT.newInstance()));
					break;
				}
				case 0x00000022:{
					add_StatMetaData((EinhasadPointStatInfoT.StatMetaDataT)input.readMessage(EinhasadPointStatInfoT.StatMetaDataT.newInstance()));
					break;
				}
				case 0x0000002A:{
					add_Stat((EinhasadPointStatInfoT.StatT)input.readMessage(EinhasadPointStatInfoT.StatT.newInstance()));
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
		return new EinhasadPointStatInfoT();
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
	public static class EnchantCostT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static EnchantCostT newInstance(){
			return new EnchantCostT();
		}
		private int _value;
		private int _point;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EnchantCostT(){
		}
		public int get_value(){
			return _value;
		}
		public void set_value(int val){
			_bit |= 0x1;
			_value = val;
		}
		public boolean has_value(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_point(){
			return _point;
		}
		public void set_point(int val){
			_bit |= 0x2;
			_point = val;
		}
		public boolean has_point(){
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
			if (has_value()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _value);
			}
			if (has_point()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _point);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_value()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_point()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_value()){
				output.wirteInt32(1, _value);
			}
			if (has_point()){
				output.wirteInt32(2, _point);
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
						set_value(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_point(input.readInt32());
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
			return new EnchantCostT();
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
	public static class StatMetaDataT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static StatMetaDataT newInstance(){
			return new StatMetaDataT();
		}
		private int _index;
		private EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT _AbilityMetaData1;
		private EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT _AbilityMetaData2;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StatMetaDataT(){
		}
		public int get_index(){
			return _index;
		}
		public void set_index(int val){
			_bit |= 0x1;
			_index = val;
		}
		public boolean has_index(){
			return (_bit & 0x1) == 0x1;
		}
		public EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT get_AbilityMetaData1(){
			return _AbilityMetaData1;
		}
		public void set_AbilityMetaData1(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT val){
			_bit |= 0x2;
			_AbilityMetaData1 = val;
		}
		public boolean has_AbilityMetaData1(){
			return (_bit & 0x2) == 0x2;
		}
		public EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT get_AbilityMetaData2(){
			return _AbilityMetaData2;
		}
		public void set_AbilityMetaData2(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT val){
			_bit |= 0x4;
			_AbilityMetaData2 = val;
		}
		public boolean has_AbilityMetaData2(){
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
			if (has_index()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
			}
			if (has_AbilityMetaData1()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _AbilityMetaData1);
			}
			if (has_AbilityMetaData2()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _AbilityMetaData2);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_index()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_AbilityMetaData1()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_AbilityMetaData2()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_index()){
				output.wirteInt32(1, _index);
			}
			if (has_AbilityMetaData1()){
				output.writeMessage(2, _AbilityMetaData1);
			}
			if (has_AbilityMetaData2()){
				output.writeMessage(3, _AbilityMetaData2);
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
						set_index(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_AbilityMetaData1((EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT)input.readMessage(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_AbilityMetaData2((EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT)input.readMessage(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.newInstance()));
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
			return new StatMetaDataT();
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
		public static class AbilityMetaDataT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static AbilityMetaDataT newInstance(){
				return new AbilityMetaDataT();
			}
			private String _token;
			private boolean _x100;
			private EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit _unit;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private AbilityMetaDataT(){
			}
			public String get_token(){
				return _token;
			}
			public void set_token(String val){
				_bit |= 0x1;
				_token = val;
			}
			public boolean has_token(){
				return (_bit & 0x1) == 0x1;
			}
			public boolean get_x100(){
				return _x100;
			}
			public void set_x100(boolean val){
				_bit |= 0x2;
				_x100 = val;
			}
			public boolean has_x100(){
				return (_bit & 0x2) == 0x2;
			}
			public EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit get_unit(){
				return _unit;
			}
			public void set_unit(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit val){
				_bit |= 0x4;
				_unit = val;
			}
			public boolean has_unit(){
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
				if (has_token()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _token);
				}
				if (has_x100()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _x100);
				}
				if (has_unit()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _unit.toInt());
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_token()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_x100()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_unit()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
				if (has_token()){
					output.writeString(1, _token);
				}
				if (has_x100()){
					output.writeBool(2, _x100);
				}
				if (has_unit()){
					output.writeEnum(3, _unit.toInt());
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
							set_token(input.readString());
							break;
						}
						case 0x00000010:{
							set_x100(input.readBool());
							break;
						}
						case 0x00000018:{
							set_unit(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit.fromInt(input.readEnum()));
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
				return new AbilityMetaDataT();
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
			public enum Unit{
				None(1),
				Percent(2),
				;
				private int value;
				Unit(int val){
					value = val;
				}
				public int toInt(){
					return value;
				}
				public boolean equals(Unit v){
					return value == v.value;
				}
				public static Unit fromInt(int i){
					switch(i){
					case 1:
						return None;
					case 2:
						return Percent;
					default:
						throw new IllegalArgumentException(String.format("invalid arguments Unit, %d", i));
					}
				}
			}
		}
	}
	public static class StatT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static StatT newInstance(){
			return new StatT();
		}
		private int _index;
		private int _value;
		private EinhasadPointStatInfoT.StatT.AbilityT _Ability1;
		private EinhasadPointStatInfoT.StatT.AbilityT _Ability2;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StatT(){
		}
		public int get_index(){
			return _index;
		}
		public void set_index(int val){
			_bit |= 0x1;
			_index = val;
		}
		public boolean has_index(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_value(){
			return _value;
		}
		public void set_value(int val){
			_bit |= 0x2;
			_value = val;
		}
		public boolean has_value(){
			return (_bit & 0x2) == 0x2;
		}
		public EinhasadPointStatInfoT.StatT.AbilityT get_Ability1(){
			return _Ability1;
		}
		public void set_Ability1(EinhasadPointStatInfoT.StatT.AbilityT val){
			_bit |= 0x4;
			_Ability1 = val;
		}
		public boolean has_Ability1(){
			return (_bit & 0x4) == 0x4;
		}
		public EinhasadPointStatInfoT.StatT.AbilityT get_Ability2(){
			return _Ability2;
		}
		public void set_Ability2(EinhasadPointStatInfoT.StatT.AbilityT val){
			_bit |= 0x8;
			_Ability2 = val;
		}
		public boolean has_Ability2(){
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
			if (has_index()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
			}
			if (has_value()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _value);
			}
			if (has_Ability1()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _Ability1);
			}
			if (has_Ability2()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _Ability2);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_index()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_value()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Ability1()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Ability2()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_index()){
				output.wirteInt32(1, _index);
			}
			if (has_value()){
				output.wirteInt32(2, _value);
			}
			if (has_Ability1()){
				output.writeMessage(3, _Ability1);
			}
			if (has_Ability2()){
				output.writeMessage(4, _Ability2);
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
						set_index(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_value(input.readInt32());
						break;
					}
					case 0x0000001A:{
						set_Ability1((EinhasadPointStatInfoT.StatT.AbilityT)input.readMessage(EinhasadPointStatInfoT.StatT.AbilityT.newInstance()));
						break;
					}
					case 0x00000022:{
						set_Ability2((EinhasadPointStatInfoT.StatT.AbilityT)input.readMessage(EinhasadPointStatInfoT.StatT.AbilityT.newInstance()));
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
			return new StatT();
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
		public static class AbilityT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static AbilityT newInstance(){
				return new AbilityT();
			}
			private int _minIncValue;
			private int _maxIncValue;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private AbilityT(){
			}
			public int get_minIncValue(){
				return _minIncValue;
			}
			public void set_minIncValue(int val){
				_bit |= 0x1;
				_minIncValue = val;
			}
			public boolean has_minIncValue(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_maxIncValue(){
				return _maxIncValue;
			}
			public void set_maxIncValue(int val){
				_bit |= 0x2;
				_maxIncValue = val;
			}
			public boolean has_maxIncValue(){
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
				if (has_minIncValue()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _minIncValue);
				}
				if (has_maxIncValue()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _maxIncValue);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_minIncValue()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_maxIncValue()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
				if (has_minIncValue()){
					output.wirteInt32(1, _minIncValue);
				}
				if (has_maxIncValue()){
					output.wirteInt32(2, _maxIncValue);
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
							set_minIncValue(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_maxIncValue(input.readInt32());
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
				return new AbilityT();
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
}

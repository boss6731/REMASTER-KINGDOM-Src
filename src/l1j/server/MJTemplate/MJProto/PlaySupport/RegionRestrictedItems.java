package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class RegionRestrictedItems implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static RegionRestrictedItems newInstance(){
		return new RegionRestrictedItems();
	}
	private java.util.LinkedList<RegionRestrictedItems.ItemT> _Item;
	private RegionRestrictedItems.CommonT _Common;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RegionRestrictedItems(){
	}
	public java.util.LinkedList<RegionRestrictedItems.ItemT> get_Item(){
		return _Item;
	}
	public void add_Item(RegionRestrictedItems.ItemT val){
		if(!has_Item()){
			_Item = new java.util.LinkedList<RegionRestrictedItems.ItemT>();
			_bit |= 0x1;
		}
		_Item.add(val);
	}
	public boolean has_Item(){
		return (_bit & 0x1) == 0x1;
	}
	public RegionRestrictedItems.CommonT get_Common(){
		return _Common;
	}
	public void set_Common(RegionRestrictedItems.CommonT val){
		_bit |= 0x2;
		_Common = val;
	}
	public boolean has_Common(){
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
		if (has_Item()){
			for(RegionRestrictedItems.ItemT val : _Item){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_Common()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _Common);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_Item()){
			for(RegionRestrictedItems.ItemT val : _Item){
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
		if (has_Item()){
			for (RegionRestrictedItems.ItemT val : _Item){
				output.writeMessage(1, val);
			}
		}
		if (has_Common()){
			output.writeMessage(2, _Common);
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
					add_Item((RegionRestrictedItems.ItemT)input.readMessage(RegionRestrictedItems.ItemT.newInstance()));
					break;
				}
				case 0x00000012:{
					set_Common((RegionRestrictedItems.CommonT)input.readMessage(RegionRestrictedItems.CommonT.newInstance()));
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
		return new RegionRestrictedItems();
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
	public static class ItemT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static ItemT newInstance(){
			return new ItemT();
		}
		private int _NameId;
		private boolean _ForceDelete;
		private boolean _UsedInPSS;
		private java.util.LinkedList<RegionRestrictedItems.AllowRegionT> _AllowRegion;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ItemT(){
		}
		public int get_NameId(){
			return _NameId;
		}
		public void set_NameId(int val){
			_bit |= 0x1;
			_NameId = val;
		}
		public boolean has_NameId(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_ForceDelete(){
			return _ForceDelete;
		}
		public void set_ForceDelete(boolean val){
			_bit |= 0x2;
			_ForceDelete = val;
		}
		public boolean has_ForceDelete(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_UsedInPSS(){
			return _UsedInPSS;
		}
		public void set_UsedInPSS(boolean val){
			_bit |= 0x4;
			_UsedInPSS = val;
		}
		public boolean has_UsedInPSS(){
			return (_bit & 0x4) == 0x4;
		}
		public java.util.LinkedList<RegionRestrictedItems.AllowRegionT> get_AllowRegion(){
			return _AllowRegion;
		}
		public void add_AllowRegion(RegionRestrictedItems.AllowRegionT val){
			if(!has_AllowRegion()){
				_AllowRegion = new java.util.LinkedList<RegionRestrictedItems.AllowRegionT>();
				_bit |= 0x8;
			}
			_AllowRegion.add(val);
		}
		public boolean has_AllowRegion(){
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
			if (has_NameId()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _NameId);
			}
			if (has_ForceDelete()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _ForceDelete);
			}
			if (has_UsedInPSS()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _UsedInPSS);
			}
			if (has_AllowRegion()){
				for(RegionRestrictedItems.AllowRegionT val : _AllowRegion){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_NameId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_AllowRegion()){
				for(RegionRestrictedItems.AllowRegionT val : _AllowRegion){
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
			if (has_NameId()){
				output.writeUInt32(1, _NameId);
			}
			if (has_ForceDelete()){
				output.writeBool(2, _ForceDelete);
			}
			if (has_UsedInPSS()){
				output.writeBool(3, _UsedInPSS);
			}
			if (has_AllowRegion()){
				for (RegionRestrictedItems.AllowRegionT val : _AllowRegion){
					output.writeMessage(4, val);
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
						set_NameId(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_ForceDelete(input.readBool());
						break;
					}
					case 0x00000018:{
						set_UsedInPSS(input.readBool());
						break;
					}
					case 0x00000022:{
						add_AllowRegion((RegionRestrictedItems.AllowRegionT)input.readMessage(RegionRestrictedItems.AllowRegionT.newInstance()));
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
			return new ItemT();
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
	public static class AllowRegionT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static AllowRegionT newInstance(){
			return new AllowRegionT();
		}
		private int _Map;
		private int _Left;
		private int _Top;
		private int _Right;
		private int _Bottom;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AllowRegionT(){
		}
		public int get_Map(){
			return _Map;
		}
		public void set_Map(int val){
			_bit |= 0x1;
			_Map = val;
		}
		public boolean has_Map(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Left(){
			return _Left;
		}
		public void set_Left(int val){
			_bit |= 0x2;
			_Left = val;
		}
		public boolean has_Left(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_Top(){
			return _Top;
		}
		public void set_Top(int val){
			_bit |= 0x4;
			_Top = val;
		}
		public boolean has_Top(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_Right(){
			return _Right;
		}
		public void set_Right(int val){
			_bit |= 0x8;
			_Right = val;
		}
		public boolean has_Right(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_Bottom(){
			return _Bottom;
		}
		public void set_Bottom(int val){
			_bit |= 0x10;
			_Bottom = val;
		}
		public boolean has_Bottom(){
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
			if (has_Map()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _Map);
			}
			if (has_Left()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _Left);
			}
			if (has_Top()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _Top);
			}
			if (has_Right()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _Right);
			}
			if (has_Bottom()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _Bottom);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Map()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_Map()){
				output.writeUInt32(1, _Map);
			}
			if (has_Left()){
				output.writeUInt32(2, _Left);
			}
			if (has_Top()){
				output.writeUInt32(3, _Top);
			}
			if (has_Right()){
				output.writeUInt32(4, _Right);
			}
			if (has_Bottom()){
				output.writeUInt32(5, _Bottom);
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
						set_Map(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_Left(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_Top(input.readUInt32());
						break;
					}
					case 0x00000020:{
						set_Right(input.readUInt32());
						break;
					}
					case 0x00000028:{
						set_Bottom(input.readUInt32());
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
			return new AllowRegionT();
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
	public static class CommonT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CommonT newInstance(){
			return new CommonT();
		}
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CommonT(){
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
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
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
			return new CommonT();
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

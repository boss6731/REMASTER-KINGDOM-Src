package l1j.server.MJTemplate.MJProto.MainServer_Client_PolymorphBook;

import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PolymorphBookInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PolymorphBookInfo newInstance(){
		return new PolymorphBookInfo();
	}
	private int _Id;
	private int _Life;
	private java.util.LinkedList<PolymorphBookInfo.SlotInfo> _Slot;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PolymorphBookInfo(){
	}
	public int get_Id(){
		return _Id;
	}
	public void set_Id(int val){
		_bit |= 0x1;
		_Id = val;
	}
	public boolean has_Id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_Life(){
		return _Life;
	}
	public void set_Life(int val){
		_bit |= 0x2;
		_Life = val;
	}
	public boolean has_Life(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<PolymorphBookInfo.SlotInfo> get_Slot(){
		return _Slot;
	}
	public void add_Slot(PolymorphBookInfo.SlotInfo val){
		if(!has_Slot()){
			_Slot = new java.util.LinkedList<PolymorphBookInfo.SlotInfo>();
			_bit |= 0x4;
		}
		_Slot.add(val);
	}
	public boolean has_Slot(){
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
		if (has_Id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Id);
		}
		if (has_Life()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _Life);
		}
		if (has_Slot()){
			for(PolymorphBookInfo.SlotInfo val : _Slot){
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
		if (!has_Id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_Slot()){
			for(PolymorphBookInfo.SlotInfo val : _Slot){
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
		if (has_Id()){
			output.wirteInt32(1, _Id);
		}
		if (has_Life()){
			output.wirteInt32(2, _Life);
		}
		if (has_Slot()){
			for (PolymorphBookInfo.SlotInfo val : _Slot){
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
					set_Id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_Life(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_Slot((PolymorphBookInfo.SlotInfo)input.readMessage(PolymorphBookInfo.SlotInfo.newInstance()));
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
		return new PolymorphBookInfo();
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
	public static class SlotMetaInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SlotMetaInfo newInstance(){
			return new SlotMetaInfo();
		}
		private int _ItemNameId;
		private int _BlessCode;
		private int _Enchant;
		private int _Elemental;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SlotMetaInfo(){
		}
		public int get_ItemNameId(){
			return _ItemNameId;
		}
		public void set_ItemNameId(int val){
			_bit |= 0x1;
			_ItemNameId = val;
		}
		public boolean has_ItemNameId(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_BlessCode(){
			return _BlessCode;
		}
		public void set_BlessCode(int val){
			_bit |= 0x2;
			_BlessCode = val;
		}
		public boolean has_BlessCode(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_Enchant(){
			return _Enchant;
		}
		public void set_Enchant(int val){
			_bit |= 0x4;
			_Enchant = val;
		}
		public boolean has_Enchant(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_Elemental(){
			return _Elemental;
		}
		public void set_Elemental(int val){
			_bit |= 0x8;
			_Elemental = val;
		}
		public boolean has_Elemental(){
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
			if (has_ItemNameId()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _ItemNameId);
			}
			if (has_BlessCode()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _BlessCode);
			}
			if (has_Enchant()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _Enchant);
			}
			if (has_Elemental()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _Elemental);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_ItemNameId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_BlessCode()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Enchant()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Elemental()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_ItemNameId()){
				output.wirteInt32(1, _ItemNameId);
			}
			if (has_BlessCode()){
				output.wirteInt32(2, _BlessCode);
			}
			if (has_Enchant()){
				output.wirteInt32(3, _Enchant);
			}
			if (has_Elemental()){
				output.wirteInt32(4, _Elemental);
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
						set_ItemNameId(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_BlessCode(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_Enchant(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_Elemental(input.readInt32());
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
			return new SlotMetaInfo();
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
	public static class SlotItemInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SlotItemInfo newInstance(){
			return new SlotItemInfo();
		}
		private ItemInfo _Item;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SlotItemInfo(){
		}
		public ItemInfo get_Item(){
			return _Item;
		}
		public void set_Item(ItemInfo val){
			_bit |= 0x1;
			_Item = val;
		}
		public boolean has_Item(){
			return (_bit & 0x1) == 0x1;
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
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _Item);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Item()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_Item()){
				output.writeMessage(1, _Item);
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
						set_Item((ItemInfo)input.readMessage(ItemInfo.newInstance()));
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
			return new SlotItemInfo();
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
	public static class SlotInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SlotInfo newInstance(){
			return new SlotInfo();
		}
		private int _SlotNo;
		private PolymorphBookInfo.SlotMetaInfo _Meta;
		private PolymorphBookInfo.SlotItemInfo _Item;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SlotInfo(){
		}
		public int get_SlotNo(){
			return _SlotNo;
		}
		public void set_SlotNo(int val){
			_bit |= 0x1;
			_SlotNo = val;
		}
		public boolean has_SlotNo(){
			return (_bit & 0x1) == 0x1;
		}
		public PolymorphBookInfo.SlotMetaInfo get_Meta(){
			return _Meta;
		}
		public void set_Meta(PolymorphBookInfo.SlotMetaInfo val){
			_bit |= 0x2;
			_Meta = val;
		}
		public boolean has_Meta(){
			return (_bit & 0x2) == 0x2;
		}
		public PolymorphBookInfo.SlotItemInfo get_Item(){
			return _Item;
		}
		public void set_Item(PolymorphBookInfo.SlotItemInfo val){
			_bit |= 0x4;
			_Item = val;
		}
		public boolean has_Item(){
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
			if (has_SlotNo()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _SlotNo);
			}
			if (has_Meta()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _Meta);
			}
			if (has_Item()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _Item);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_SlotNo()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_SlotNo()){
				output.wirteInt32(1, _SlotNo);
			}
			if (has_Meta()){
				output.writeMessage(2, _Meta);
			}
			if (has_Item()){
				output.writeMessage(3, _Item);
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
						set_SlotNo(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_Meta((PolymorphBookInfo.SlotMetaInfo)input.readMessage(PolymorphBookInfo.SlotMetaInfo.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_Item((PolymorphBookInfo.SlotItemInfo)input.readMessage(PolymorphBookInfo.SlotItemInfo.newInstance()));
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

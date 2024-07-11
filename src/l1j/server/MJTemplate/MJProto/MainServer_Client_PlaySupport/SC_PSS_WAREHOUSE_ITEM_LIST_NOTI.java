package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PSS_WAREHOUSE_ITEM_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_PSS_WAREHOUSE_ITEM_LIST_NOTI newInstance(){
		return new SC_PSS_WAREHOUSE_ITEM_LIST_NOTI();
	}
	private int _count;
	private java.util.LinkedList<PSSItem> _item_list;
	private boolean _serial_last;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PSS_WAREHOUSE_ITEM_LIST_NOTI(){
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x1;
		_count = val;
	}
	public boolean has_count(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<PSSItem> get_item_list(){
		return _item_list;
	}
	public void add_item_list(PSSItem val){
		if(!has_item_list()){
			_item_list = new java.util.LinkedList<PSSItem>();
			_bit |= 0x2;
		}
		_item_list.add(val);
	}
	public boolean has_item_list(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_serial_last(){
		return _serial_last;
	}
	public void set_serial_last(boolean val){
		_bit |= 0x4;
		_serial_last = val;
	}
	public boolean has_serial_last(){
		return (_bit & 0x4) == 0x4;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _count);
		if (has_item_list()){
			for(PSSItem val : _item_list)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		if (has_serial_last())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _serial_last);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_item_list()){
			for(PSSItem val : _item_list){
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_count()){
			output.wirteInt32(1, _count);
		}
		if (has_item_list()){
			for(PSSItem val : _item_list){
				output.writeMessage(2, val);
			}
		}
		if (has_serial_last()){
			output.writeBool(3, _serial_last);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x00000008:{
					set_count(input.readInt32());
					break;
				}
				case 0x00000012:{
					add_item_list((PSSItem)input.readMessage(PSSItem.newInstance()));
					break;
				}
				case 0x00000018:{
					set_serial_last(input.readBool());
					break;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new SC_PSS_WAREHOUSE_ITEM_LIST_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_list()){
			for(PSSItem val : _item_list)
				val.dispose();
			_item_list.clear();
			_item_list = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class PSSItem implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static PSSItem newInstance(){
			return new PSSItem();
		}
		private int _index;
		private byte[] _desc;
		private int _icon;
		private int _bless_code;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PSSItem(){
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
		public byte[] get_desc(){
			return _desc;
		}
		public void set_desc(byte[] val){
			_bit |= 0x2;
			_desc = val;
		}
		public boolean has_desc(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_icon(){
			return _icon;
		}
		public void set_icon(int val){
			_bit |= 0x4;
			_icon = val;
		}
		public boolean has_icon(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_bless_code(){
			return _bless_code;
		}
		public void set_bless_code(int val){
			_bit |= 0x8;
			_bless_code = val;
		}
		public boolean has_bless_code(){
			return (_bit & 0x8) == 0x8;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_index())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
			if (has_desc())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _desc);
			if (has_icon())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _icon);
			if (has_bless_code())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _bless_code);
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
			if (!has_desc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_icon()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_bless_code()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_index()){
				output.wirteInt32(1, _index);
			}
			if (has_desc()){
				output.writeBytes(2, _desc);
			}
			if (has_icon()){
				output.wirteInt32(3, _icon);
			}
			if (has_bless_code()){
				output.wirteInt32(4, _bless_code);
			}
		}
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try{
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					default:{
						return this;
					}
					case 0x00000008:{
						set_index(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_desc(input.readBytes());
						break;
					}
					case 0x00000018:{
						set_icon(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_bless_code(input.readInt32());
						break;
					}
				}
			}
			return this;
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try{
				readFrom(is);

				if (!isInitialized())
					return this;
				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch(Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public MJIProtoMessage copyInstance(){
			return new PSSItem();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}

package l1j.server.MJTemplate.MJProto.MainServer_Client_PolymorphBook;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_POLYMORPH_COUNT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_POLYMORPH_COUNT_NOTI newInstance(){
		return new SC_POLYMORPH_COUNT_NOTI();
	}
	private int _Count;
	private boolean _Advanced;
	private SC_POLYMORPH_COUNT_NOTI.PolymorphBookBuffT _PolymorphBookBuff;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_POLYMORPH_COUNT_NOTI(){
	}
	public int get_Count(){
		return _Count;
	}
	public void set_Count(int val){
		_bit |= 0x1;
		_Count = val;
	}
	public boolean has_Count(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_Advanced(){
		return _Advanced;
	}
	public void set_Advanced(boolean val){
		_bit |= 0x2;
		_Advanced = val;
	}
	public boolean has_Advanced(){
		return (_bit & 0x2) == 0x2;
	}
	public SC_POLYMORPH_COUNT_NOTI.PolymorphBookBuffT get_PolymorphBookBuff(){
		return _PolymorphBookBuff;
	}
	public void set_PolymorphBookBuff(SC_POLYMORPH_COUNT_NOTI.PolymorphBookBuffT val){
		_bit |= 0x4;
		_PolymorphBookBuff = val;
	}
	public boolean has_PolymorphBookBuff(){
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
		if (has_Count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Count);
		}
		if (has_Advanced()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _Advanced);
		}
		if (has_PolymorphBookBuff()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _PolymorphBookBuff);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_Count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_Advanced()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_Count()){
			output.wirteInt32(1, _Count);
		}
		if (has_Advanced()){
			output.writeBool(2, _Advanced);
		}
		if (has_PolymorphBookBuff()){
			output.writeMessage(3, _PolymorphBookBuff);
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
					set_Count(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_Advanced(input.readBool());
					break;
				}
				case 0x0000001A:{
					set_PolymorphBookBuff((SC_POLYMORPH_COUNT_NOTI.PolymorphBookBuffT)input.readMessage(SC_POLYMORPH_COUNT_NOTI.PolymorphBookBuffT.newInstance()));
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
		return new SC_POLYMORPH_COUNT_NOTI();
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
	public static class PolymorphBookBuffT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static PolymorphBookBuffT newInstance(){
			return new PolymorphBookBuffT();
		}
		private byte[] _CommonBuff;
		private byte[] _BookBuff;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PolymorphBookBuffT(){
		}
		public byte[] get_CommonBuff(){
			return _CommonBuff;
		}
		public void set_CommonBuff(byte[] val){
			_bit |= 0x1;
			_CommonBuff = val;
		}
		public boolean has_CommonBuff(){
			return (_bit & 0x1) == 0x1;
		}
		public byte[] get_BookBuff(){
			return _BookBuff;
		}
		public void set_BookBuff(byte[] val){
			_bit |= 0x2;
			_BookBuff = val;
		}
		public boolean has_BookBuff(){
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
			if (has_CommonBuff()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _CommonBuff);
			}
			if (has_BookBuff()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _BookBuff);
			}
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
			if (has_CommonBuff()){
				output.writeBytes(1, _CommonBuff);
			}
			if (has_BookBuff()){
				output.writeBytes(2, _BookBuff);
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
						set_CommonBuff(input.readBytes());
						break;
					}
					case 0x00000012:{
						set_BookBuff(input.readBytes());
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
			return new PolymorphBookBuffT();
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

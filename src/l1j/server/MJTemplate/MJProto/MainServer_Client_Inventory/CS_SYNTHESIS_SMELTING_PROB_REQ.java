package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_SYNTHESIS_SMELTING_PROB_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_SYNTHESIS_SMELTING_PROB_REQ newInstance(){
		return new CS_SYNTHESIS_SMELTING_PROB_REQ();
	}
	private int _alchemy_id;
	private java.util.LinkedList<CS_SYNTHESIS_SMELTING_PROB_REQ.Input> _inputs;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_SYNTHESIS_SMELTING_PROB_REQ(){
	}
	public int get_alchemy_id(){
		return _alchemy_id;
	}
	public void set_alchemy_id(int val){
		_bit |= 0x1;
		_alchemy_id = val;
	}
	public boolean has_alchemy_id(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<CS_SYNTHESIS_SMELTING_PROB_REQ.Input> get_inputs(){
		return _inputs;
	}
	public void add_inputs(CS_SYNTHESIS_SMELTING_PROB_REQ.Input val){
		if(!has_inputs()){
			_inputs = new java.util.LinkedList<CS_SYNTHESIS_SMELTING_PROB_REQ.Input>();
			_bit |= 0x2;
		}
		_inputs.add(val);
	}
	public boolean has_inputs(){
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
		if (has_alchemy_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _alchemy_id);
		}
		if (has_inputs()){
			for(CS_SYNTHESIS_SMELTING_PROB_REQ.Input val : _inputs){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_alchemy_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_inputs()){
			for(CS_SYNTHESIS_SMELTING_PROB_REQ.Input val : _inputs){
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
		if (has_alchemy_id()){
			output.wirteInt32(1, _alchemy_id);
		}
		if (has_inputs()){
			for (CS_SYNTHESIS_SMELTING_PROB_REQ.Input val : _inputs){
				output.writeMessage(2, val);
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
					set_alchemy_id(input.readInt32());
					break;
				}
				case 0x00000012:{
					add_inputs((CS_SYNTHESIS_SMELTING_PROB_REQ.Input)input.readMessage(CS_SYNTHESIS_SMELTING_PROB_REQ.Input.newInstance()));
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
		return new CS_SYNTHESIS_SMELTING_PROB_REQ();
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
	public static class Input implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Input newInstance(){
			return new Input();
		}
		private int _slot_no;
		private int _item_name_id;
		private int _item_id;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Input(){
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
		public int get_item_name_id(){
			return _item_name_id;
		}
		public void set_item_name_id(int val){
			_bit |= 0x2;
			_item_name_id = val;
		}
		public boolean has_item_name_id(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_item_id(){
			return _item_id;
		}
		public void set_item_id(int val){
			_bit |= 0x4;
			_item_id = val;
		}
		public boolean has_item_id(){
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
			if (has_slot_no()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_no);
			}
			if (has_item_name_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _item_name_id);
			}
			if (has_item_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _item_id);
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
			if (!has_item_name_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_slot_no()){
				output.wirteInt32(1, _slot_no);
			}
			if (has_item_name_id()){
				output.wirteInt32(2, _item_name_id);
			}
			if (has_item_id()){
				output.wirteInt32(3, _item_id);
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
						set_slot_no(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_item_name_id(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_item_id(input.readInt32());
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
			return new Input();
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

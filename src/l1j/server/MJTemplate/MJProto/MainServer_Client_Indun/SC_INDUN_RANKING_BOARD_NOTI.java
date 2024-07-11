package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_INDUN_RANKING_BOARD_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_INDUN_RANKING_BOARD_NOTI newInstance(){
		return new SC_INDUN_RANKING_BOARD_NOTI();
	}
	private java.util.LinkedList<Integer> _leader_server_numbers;
	private java.util.LinkedList<SC_INDUN_RANKING_BOARD_NOTI.RankerData> _ranker_data;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INDUN_RANKING_BOARD_NOTI(){
	}
	public java.util.LinkedList<Integer> get_leader_server_numbers(){
		return _leader_server_numbers;
	}
	public void add_leader_server_numbers(int val){
		if(!has_leader_server_numbers()){
			_leader_server_numbers = new java.util.LinkedList<Integer>();
			_bit |= 0x1;
		}
		_leader_server_numbers.add(val);
	}
	public boolean has_leader_server_numbers(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<SC_INDUN_RANKING_BOARD_NOTI.RankerData> get_ranker_data(){
		return _ranker_data;
	}
	public void add_ranker_data(SC_INDUN_RANKING_BOARD_NOTI.RankerData val){
		if(!has_ranker_data()){
			_ranker_data = new java.util.LinkedList<SC_INDUN_RANKING_BOARD_NOTI.RankerData>();
			_bit |= 0x2;
		}
		_ranker_data.add(val);
	}
	public boolean has_ranker_data(){
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
		if (has_leader_server_numbers()){
			for(int val : _leader_server_numbers){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, val);
			}
		}
		if (has_ranker_data()){
			for(SC_INDUN_RANKING_BOARD_NOTI.RankerData val : _ranker_data){
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
	//	if (has_leader_server_numbers()){
	//		for(int val : _leader_server_numbers){
	//			if (!val.isInitialized()){
	//				_memorizedIsInitialized = -1;
	//				return false;
	//			}
	//		}
	//	}
		if (has_ranker_data()){
			for(SC_INDUN_RANKING_BOARD_NOTI.RankerData val : _ranker_data){
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
		if (has_leader_server_numbers()){
			for (int val : _leader_server_numbers){
				output.wirteInt32(1, val);
			}
		}
		if (has_ranker_data()){
			for (SC_INDUN_RANKING_BOARD_NOTI.RankerData val : _ranker_data){
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
					add_leader_server_numbers(input.readInt32());
					break;
				}
				case 0x00000012:{
					add_ranker_data((SC_INDUN_RANKING_BOARD_NOTI.RankerData)input.readMessage(SC_INDUN_RANKING_BOARD_NOTI.RankerData.newInstance()));
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
		return new SC_INDUN_RANKING_BOARD_NOTI();
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
	public static class RankerData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static RankerData newInstance(){
			return new RankerData();
		}
		private String _ranker_name;
		private int _kill_count;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RankerData(){
		}
		public String get_ranker_name(){
			return _ranker_name;
		}
		public void set_ranker_name(String val){
			_bit |= 0x1;
			_ranker_name = val;
		}
		public boolean has_ranker_name(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_kill_count(){
			return _kill_count;
		}
		public void set_kill_count(int val){
			_bit |= 0x2;
			_kill_count = val;
		}
		public boolean has_kill_count(){
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
			if (has_ranker_name()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _ranker_name);
			}
			if (has_kill_count()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _kill_count);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_ranker_name()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_kill_count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_ranker_name()){
				output.writeString(1, _ranker_name);
			}
			if (has_kill_count()){
				output.wirteInt32(2, _kill_count);
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
						set_ranker_name(input.readString());
						break;
					}
					case 0x00000010:{
						set_kill_count(input.readInt32());
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
			return new RankerData();
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

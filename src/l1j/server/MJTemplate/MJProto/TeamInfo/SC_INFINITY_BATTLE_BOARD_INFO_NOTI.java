package l1j.server.MJTemplate.MJProto.TeamInfo;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_INFINITY_BATTLE_BOARD_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void team_send(L1PcInstance pc, int team1_progress, int team2_progress, int team3_progress, int team4_progress) {
		SC_INFINITY_BATTLE_BOARD_INFO_NOTI noti = SC_INFINITY_BATTLE_BOARD_INFO_NOTI.newInstance();
		for (int i = 1; i < 5; i++) {
			INFO info = INFO.newInstance();
			info.set_team_id(i);
			if (i == 1)
				info.set_progress_rate(team1_progress);
			if (i == 2)
				info.set_progress_rate(team2_progress);
			if (i == 3)
				info.set_progress_rate(team3_progress);
			if (i == 4)
				info.set_progress_rate(team4_progress);
			noti.add_board_info(info);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_INFINITY_BATTLE_BOARD_INFO_NOTI, true);
	}
	
	public static SC_INFINITY_BATTLE_BOARD_INFO_NOTI newInstance(){
		return new SC_INFINITY_BATTLE_BOARD_INFO_NOTI();
	}
	private java.util.LinkedList<SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO> _board_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INFINITY_BATTLE_BOARD_INFO_NOTI(){
	}
	public java.util.LinkedList<SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO> get_map_time_limit_info(){
		return _board_info;
	}
	public void add_board_info(SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO val){
		if(!has_board_info()){
			_board_info = new java.util.LinkedList<SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO>();
			_bit |= 0x1;
		}
		_board_info.add(val);
	}
	public boolean has_board_info(){
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
		if (has_board_info()){
			for(SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO val : _board_info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_board_info()){
			for(SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO val : _board_info){
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
		if (has_board_info()){
			for (SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO val : _board_info){
				output.writeMessage(1, val);
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
			switch (tag) {
				case 0x0000000A: {
					add_board_info((SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO) input.readMessage(SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO.newInstance()));
					break;
				}
				default: {
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_INFINITY_BATTLE_BOARD_INFO_NOTI();
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_board_info()){
			for(SC_INFINITY_BATTLE_BOARD_INFO_NOTI.INFO val : _board_info)
				val.dispose();
			_board_info.clear();
			_board_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static INFO newInstance(){
			return new INFO();
		}
		private int _team_id;
		private int _progress_rate;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private INFO(){
		}
		public int get_team_id(){
			return _team_id;
		}
		public void set_team_id(int val){
			_bit |= 0x1;
			_team_id = val;
		}
		public boolean has_team_id(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_progress_rate(){
			return _progress_rate;
		}
		public void set_progress_rate(int val){
			_bit |= 0x2;
			_progress_rate = val;
		}
		public boolean has_progress_rate(){
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
			if (has_team_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _team_id);
			}
			if (has_progress_rate()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _progress_rate);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_team_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_progress_rate()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_team_id()){
				output.writeUInt32(1, _team_id);
			}
			if (has_progress_rate()){
				output.wirteInt32(2, _progress_rate);
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
						set_team_id(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_progress_rate(input.readInt32());
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

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
			return new INFO();
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

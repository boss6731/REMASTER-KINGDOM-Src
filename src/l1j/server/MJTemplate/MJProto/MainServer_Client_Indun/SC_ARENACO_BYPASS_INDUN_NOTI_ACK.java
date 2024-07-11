package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunNotiInfo;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENACO_BYPASS_INDUN_NOTI_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ARENACO_BYPASS_INDUN_NOTI_ACK newInstance(){
		return new SC_ARENACO_BYPASS_INDUN_NOTI_ACK();
	}
	private SC_ARENACO_BYPASS_INDUN_NOTI_ACK.eResult _result;
	private java.util.LinkedList<IndunNotiInfo> _noti_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ARENACO_BYPASS_INDUN_NOTI_ACK(){
	}
	public SC_ARENACO_BYPASS_INDUN_NOTI_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_ARENACO_BYPASS_INDUN_NOTI_ACK.eResult val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<IndunNotiInfo> get_noti_info(){
		return _noti_info;
	}
	public void add_noti_info(IndunNotiInfo val){
		if(!has_noti_info()){
			_noti_info = new java.util.LinkedList<IndunNotiInfo>();
			_bit |= 0x2;
		}
		_noti_info.add(val);
	}
	public boolean has_noti_info(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_noti_info()){
			for(IndunNotiInfo val : _noti_info){
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
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_noti_info()){
			for(IndunNotiInfo val : _noti_info){
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
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
		if (has_noti_info()){
			for (IndunNotiInfo val : _noti_info){
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
					set_result(SC_ARENACO_BYPASS_INDUN_NOTI_ACK.eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_noti_info((IndunNotiInfo)input.readMessage(IndunNotiInfo.newInstance()));
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
		return new SC_ARENACO_BYPASS_INDUN_NOTI_ACK();
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
	public enum eResult{
		SUCCESS(1),
		ERROR_INTENAL(2),
		ERROR_NOT_EXIST_USER(3),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return ERROR_INTENAL;
			case 3:
				return ERROR_NOT_EXIST_USER;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 eResult, %d", i));
			}
		}
	}
}

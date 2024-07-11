package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.GameClient;

public class CS_CUSTOM_MSGBOX_RESPONSE implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_CUSTOM_MSGBOX_RESPONSE newInstance(){
		return new CS_CUSTOM_MSGBOX_RESPONSE();
	}
	
	private int _message_id;
	private MessageBoxResult _result_code;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_CUSTOM_MSGBOX_RESPONSE(){}
	
	public void set_message_id(int message_id){
		_bit |= 0x01;
		_message_id = message_id;
	}
	public boolean has_message_id(){
		return (_bit & 0x01) == 0x01;
	}
	public int get_message_id(){
		return _message_id;
	}
	public void set_result_code(MessageBoxResult result_code){
		_bit |= 0x02;
		_result_code = result_code;
	}
	public boolean has_result_code(){
		return (_bit & 0x02) == 0x02;
	}
	public MessageBoxResult get_result_code(){
		return _result_code;
	}
	@Override
	public int getSerializedSize() {
		int size = 0;
		if(has_message_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _message_id);
		if(has_result_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _result_code.toInt());
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public boolean isInitialized() {
		if(_memorizedIsInitialized == 1)
			return true;
		
		if(!has_message_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if(!has_result_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public long getInitializeBit() {
		return (long)_bit;
	}

	@Override
	public ProtoOutputStream writeTo(MJEProtoMessages message) {
		return null;
	}

	@Override
	public void writeTo(ProtoOutputStream stream) throws IOException {
	}

	@Override
	public MJIProtoMessage readFrom(ProtoInputStream stream) throws IOException {
		while(!stream.isAtEnd()){
			int tag = stream.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x00000008:{
					set_message_id(stream.readUInt32());
					break;
				}
				case 0x00000010:{
					set_result_code(MessageBoxResult.fromInt(stream.readEnum()));
					break;					
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);
			
			if (!isInitialized())
				return this;
			
		}catch(Exception e){
			System.out.println(clnt.getIp());
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_CUSTOM_MSGBOX_RESPONSE();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public enum MessageBoxResult {
		NONE(-1),
		OK(0x01),
		CANCEL(0x02),
		ABORT(0x00),
		RETRY(0x04),
		IGNORE(0x05),
		YES(0x06),
		NO(0x07);
		private int m_val;
		MessageBoxResult(int val){
			m_val = val;
		}
		
		public int toInt(){
			return m_val;
		}
		
		public static MessageBoxResult fromInt(int i){
			switch(i){
			case 0x01:
				return OK;
			case 0x02:
				return CANCEL;
			case 0x00:
				return ABORT;
			case 0x04:
				return RETRY;
			case 0x05:
				return IGNORE;
			case 0x06:
				return YES;
			case 0x07:
				return NO;
			}
			return NONE;
		}
	};
}

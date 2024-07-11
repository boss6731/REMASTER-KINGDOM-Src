package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;

public class SC_CUSTOM_MSGBOX implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void do_kick(GameClient clnt, String message){
		SC_CUSTOM_MSGBOX box = newInstance();
		if (clnt != null) {
			box.set_button_type(ButtonType.MB_OK);
			box.set_icon_type(IconType.MB_ICONHAND);
			box.set_title(Config.Message.GameServerName);
			box.set_message(message);
			box.set_message_id(0);
			clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt(), true);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						clnt.kick();
					} catch (Exception exception) {
					}
				}
			}, 1500L);
		}
	}
	public static void do_send(GameClient clnt, String message){
		SC_CUSTOM_MSGBOX box = newInstance();
		if (clnt != null) {
			/*MB_OK(0x00),
			MB_OKCANCEL(0x01),
			MB_ABORTRETRYIGNORE(0x02),
			MB_YESNOCANCEL(0x03),
			MB_YESNO(0x04),
			MB_RETRYCANCEL(0x05);*/
			box.set_button_type(ButtonType.MB_OK);
			/*MB_ICONHAND(0x10),
			MB_ICONQUESTION(0x20),
			MB_ICONEXCLAMATION(0x30),
			MB_ICONASTERISK(0x40);*/
			box.set_icon_type(IconType.MB_ICONQUESTION);
			box.set_title("경고!");
			box.set_message(message);
			box.set_message_id(0);
			box.set_is_helper_on(true);
			box.set_is_right(true);
			box.set_is_top_most(true);
			clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt(), true);
			/*GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						// clnt.kick();
						SC_CUSTOM_MSGBOX.do_send(clnt, String.format("확인 버튼을 클릭후 30초안에 자동방지 코드를 입력해주세요.(컴퓨터 과부하걸림)"));
					} catch (Exception exception) {
					}
				}
			}, 10L);*/
		}
	}
	
	public static SC_CUSTOM_MSGBOX newInstance(){
		return new SC_CUSTOM_MSGBOX();
	}
	
	private int _message_id;
	private String _title;
	private String _message;
	private ButtonType _button_type;
	private IconType _icon_type;
	private boolean _is_helper_on;
	private boolean _is_top_most;
	private boolean _is_right;
	private boolean _is_right_to_left_reading;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	private SC_CUSTOM_MSGBOX(){
		set_button_type(ButtonType.MB_OK);
	}
	public void set_message_id(int message_id){
		_bit |= 0x01;
		_message_id = message_id;
	}
	public boolean has_message_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_message_id(){
		return _message_id;
	}
	
	public void set_title(String title){
		_bit |= 0x02;
		_title = title;
	}
	public boolean has_title(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_title(){
		return _title;
	}
	public void set_message(String message){
		_bit |= 0x04;
		_message = message;
	}
	public boolean has_message(){
		return (_bit & 0x4) == 0x4;
	}
	public String get_message(){
		return _message;
	}
	public void set_button_type(ButtonType button_type){
		_bit |= 0x08;
		_button_type = button_type;
	}
	public boolean has_button_type(){
		return (_bit & 0x8) == 0x8;
	}
	public ButtonType get_button_type(){
		return _button_type;
	}
	public void set_icon_type(IconType icon_type){
		_bit |= 0x010;
		_icon_type = icon_type;
	}
	public boolean has_icon_type(){
		return (_bit & 0x10) == 0x10;
	}
	public IconType get_icon_type(){
		return _icon_type;
	}
	public void set_is_helper_on(boolean is_helper_on){
		_bit |= 0x20;
		_is_helper_on = is_helper_on;
	}
	public boolean has_is_helper_on(){
		return (_bit & 0x20) == 0x20;
	}
	public boolean get_is_helper_on(){
		return _is_helper_on;
	}
	public void set_is_top_most(boolean is_top_most){
		_bit |= 0x40;
		_is_top_most = is_top_most;
	}
	public boolean has_is_top_most(){
		return (_bit & 0x40) == 0x40;
	}
	public boolean get_is_top_most(){
		return _is_top_most;
	}
	public void set_is_right(boolean is_right){
		_bit |= 0x80;
		_is_right = is_right;
	}
	public boolean has_is_right(){
		return (_bit & 0x80) == 0x80;
	}
	public boolean get_is_right(){
		return _is_right;
	}
	public void set_is_right_to_left_reading(boolean is_right_to_left_reading){
		_bit |= 0x100;
		_is_right_to_left_reading = is_right_to_left_reading;
	}
	public boolean has_is_right_to_left_reading(){
		return (_bit & 0x100) == 0x100;
	}
	public boolean get_is_right_to_left_reading(){
		return _is_right_to_left_reading;
	}
	
	@Override
	public int getSerializedSize() {
		int size = 0;
		if(has_message_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _message_id);
		if(has_title())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _title);
		if(has_message())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _message);
		
		int flag = _button_type.toInt();
		if(has_icon_type())
			flag |= _icon_type.toInt();
		if(has_is_helper_on() && get_is_helper_on())
			flag |= 0x00004000;
		if(has_is_top_most() && get_is_top_most())
			flag |= 0x00040000;
		if(has_is_right() && get_is_right())
			flag |= 0x00080000;
		if(has_is_right_to_left_reading() && get_is_right_to_left_reading())
			flag |= 0x00100000;
		size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, flag);
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
			
		if(!has_title()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if(!has_message()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if(!has_button_type()){
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
	public void writeTo(ProtoOutputStream stream) throws IOException {
		if(has_message_id())
			stream.writeUInt32(1, _message_id);
		if(has_title())
			stream.writeString(2, _title);
		if(has_message())			
			stream.writeString(3, _message);
		
		int flag = _button_type.toInt();
		if(has_icon_type())
			flag |= _icon_type.toInt();
		if(has_is_helper_on() && get_is_helper_on())
			flag |= 0x00004000;
		if(has_is_top_most() && get_is_top_most())
			flag |= 0x00040000;
		if(has_is_right() && get_is_right())
			flag |= 0x00080000;
		if(has_is_right_to_left_reading() && get_is_right_to_left_reading())
			flag |= 0x00100000;
		stream.writeUInt32(4, flag);
	}

	@Override
	public MJIProtoMessage readFrom(ProtoInputStream stream) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MJIProtoMessage readFrom(GameClient clnt, byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_CUSTOM_MSGBOX();
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

	public enum IconType{
		MB_ICONHAND(0x10),
		MB_ICONQUESTION(0x20),
		MB_ICONEXCLAMATION(0x30),
		MB_ICONASTERISK(0x40);
		
		private int m_val;
		IconType(int val){
			m_val = val;
		}
		
		public int toInt(){
			return m_val;
		}
		
		public static IconType fromInt(int i){
			switch(i){
			case 0x10:
				return MB_ICONHAND;
			case 0x20:
				return MB_ICONQUESTION;
			case 0x30:
				return MB_ICONEXCLAMATION;
			case 0x40:
				return MB_ICONASTERISK;
			}
			return null;
		}
	}
	
	public enum ButtonType {
		MB_OK(0x00),
		MB_OKCANCEL(0x01),
		MB_ABORTRETRYIGNORE(0x02),
		MB_YESNOCANCEL(0x03),
		MB_YESNO(0x04),
		MB_RETRYCANCEL(0x05);
		private int m_val;
		ButtonType(int val){
			m_val = val;
		}
		
		public int toInt(){
			return m_val;
		}
		
		public static ButtonType fromInt(int i){
			switch(i){
			case 0x00:
				return MB_OK;
			case 0x01:
				return MB_OKCANCEL;
			case 0x02:
				return MB_ABORTRETRYIGNORE;
			case 0x03:
				return MB_YESNOCANCEL;
			case 0x04:
				return MB_YESNO;
			case 0x05:
				return MB_RETRYCANCEL;
			}
			return null;
		}
	};
	
}

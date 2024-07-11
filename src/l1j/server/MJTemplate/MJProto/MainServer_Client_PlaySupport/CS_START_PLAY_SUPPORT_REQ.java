package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;

import l1j.server.Config;
import l1j.server.MJAutoSystem.MJAutoMapInfo;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
	
// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_START_PLAY_SUPPORT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_START_PLAY_SUPPORT_REQ newInstance(){
		return new CS_START_PLAY_SUPPORT_REQ();
	}
	private CS_START_PLAY_SUPPORT_REQ.eMode _mode;
	private PSSSettingInfo _setting_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_START_PLAY_SUPPORT_REQ(){
	}
	public CS_START_PLAY_SUPPORT_REQ.eMode get_mode(){
		return _mode;
	}
	public void set_mode(CS_START_PLAY_SUPPORT_REQ.eMode val){
		_bit |= 0x1;
		_mode = val;
	}
	public boolean has_mode(){
		return (_bit & 0x1) == 0x1;
	}
	public PSSSettingInfo get_setting_info(){
		return _setting_info;
	}
	public void set_setting_info(PSSSettingInfo val){
		_bit |= 0x2;
		_setting_info = val;
	}
	public boolean has_setting_info(){
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
		if (has_mode()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _mode.toInt());
		}
		if (has_setting_info()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _setting_info);
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
		if (has_mode()){
			output.writeEnum(1, _mode.toInt());
		}
		if (has_setting_info()){
			output.writeMessage(2, _setting_info);
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
					set_mode(CS_START_PLAY_SUPPORT_REQ.eMode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_setting_info((PSSSettingInfo)input.readMessage(PSSSettingInfo.newInstance()));
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
			MJAutoMapInfo mInfo = MJAutoMapInfo.get_map_info(pc.getMapId());
			if(mInfo != null) {
				if (mInfo.type_check(pc, get_setting_info().get_mode().toInt()))
					return this;
			}
			pc.sendPackets(6553);
//			System.out.println(get_setting_info().get_tel_condition_pvp());
			if (!pc.getInventory().checkItem(4100121) && !pc.getInventory().checkItem(4100529)) {
				pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream(Config.Message.GahoMont, MJSimpleRgb.green(), 1));
				pc.sendPackets(Config.Message.GahoMont);
			}
			pc.do_start_client_auto_ack(get_setting_info().get_mode().toInt());
		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
// 返回一個新的 CS_START_PLAY_SUPPORT_REQ 實例
		return new CS_START_PLAY_SUPPORT_REQ();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
// 調用 newInstance 方法，返回一個新的 CS_START_PLAY_SUPPORT_REQ 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	// 定義枚舉 eMode
	public enum eMode {
		SUPPORT(1),  // 支援模式
		LOCAL(2),    // 本地模式
		GLOBAL(3);   // 全球模式

		private int value;

		// 枚舉的構造函數
		eMode(int val) {
			value = val;
		}

		// 返回枚舉的整數值
		public int toInt() {
			return value;
		}

		// 比較兩個枚舉是否相等
		public boolean equals(eMode v) {
			return value == v.value;
		}

		// 根據整數值返回對應的枚舉
		public static eMode fromInt(int i) {
			switch (i) {
				case 1:
					return SUPPORT;
				case 2:
					return LOCAL;
				case 3:
					return GLOBAL;
				default:
					throw new IllegalArgumentException(String.format("無效的 eMode，%d", i));
			}
		}
	}

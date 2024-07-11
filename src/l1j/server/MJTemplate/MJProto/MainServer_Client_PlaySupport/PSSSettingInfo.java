package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PSSSettingInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PSSSettingInfo newInstance(){
		return new PSSSettingInfo();
	}
	private PSSSettingInfo.eMode _mode;
	private int _off_condition_death;
	private int _off_condition_peace;
	private int _off_condition_comeback;
	private int _tel_condition_hp;
	private int _tel_condition_peace;
	private int _tel_condition_surrounded;
	private int _tel_condition_pvp;
	private int _exp_recovery;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PSSSettingInfo(){
		set_off_condition_death(0);
		set_off_condition_peace(0);
		set_off_condition_comeback(0);
		set_tel_condition_hp(0);
		set_tel_condition_peace(0);
		set_tel_condition_surrounded(0);
		set_tel_condition_pvp(0);
		set_exp_recovery(0);
	}
	public PSSSettingInfo.eMode get_mode(){
		return _mode;
	}
	public void set_mode(PSSSettingInfo.eMode val){
		_bit |= 0x1;
		_mode = val;
	}
	public boolean has_mode(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_off_condition_death(){
		return _off_condition_death;
	}
	public void set_off_condition_death(int val){
		_bit |= 0x2;
		_off_condition_death = val;
	}
	public boolean has_off_condition_death(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_off_condition_peace(){
		return _off_condition_peace;
	}
	public void set_off_condition_peace(int val){
		_bit |= 0x4;
		_off_condition_peace = val;
	}
	public boolean has_off_condition_peace(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_off_condition_comeback(){
		return _off_condition_comeback;
	}
	public void set_off_condition_comeback(int val){
		_bit |= 0x8;
		_off_condition_comeback = val;
	}
	public boolean has_off_condition_comeback(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_tel_condition_hp(){
		return _tel_condition_hp;
	}
	public void set_tel_condition_hp(int val){
		_bit |= 0x10;
		_tel_condition_hp = val;
	}
	public boolean has_tel_condition_hp(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_tel_condition_peace(){
		return _tel_condition_peace;
	}
	public void set_tel_condition_peace(int val){
		_bit |= 0x20;
		_tel_condition_peace = val;
	}
	public boolean has_tel_condition_peace(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_tel_condition_surrounded(){
		return _tel_condition_surrounded;
	}
	public void set_tel_condition_surrounded(int val){
		_bit |= 0x40;
		_tel_condition_surrounded = val;
	}
	public boolean has_tel_condition_surrounded(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_tel_condition_pvp(){
		return _tel_condition_pvp;
	}
	public void set_tel_condition_pvp(int val){
		_bit |= 0x80;
		_tel_condition_pvp = val;
	}
	public boolean has_tel_condition_pvp(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_exp_recovery(){
		return _exp_recovery;
	}
	public void set_exp_recovery(int val){
		_bit |= 0x100;
		_exp_recovery = val;
	}
	public boolean has_exp_recovery(){
		return (_bit & 0x100) == 0x100;
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
		if (has_off_condition_death()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _off_condition_death);
		}
		if (has_off_condition_peace()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _off_condition_peace);
		}
		if (has_off_condition_comeback()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _off_condition_comeback);
		}
		if (has_tel_condition_hp()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _tel_condition_hp);
		}
		if (has_tel_condition_peace()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _tel_condition_peace);
		}
		if (has_tel_condition_surrounded()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _tel_condition_surrounded);
		}
		if (has_tel_condition_pvp()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _tel_condition_pvp);
		}
		if (has_exp_recovery()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _exp_recovery);
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
		if (has_off_condition_death()){
			output.wirteInt32(2, _off_condition_death);
		}
		if (has_off_condition_peace()){
			output.wirteInt32(3, _off_condition_peace);
		}
		if (has_off_condition_comeback()){
			output.wirteInt32(4, _off_condition_comeback);
		}
		if (has_tel_condition_hp()){
			output.wirteInt32(5, _tel_condition_hp);
		}
		if (has_tel_condition_peace()){
			output.wirteInt32(6, _tel_condition_peace);
		}
		if (has_tel_condition_surrounded()){
			output.wirteInt32(7, _tel_condition_surrounded);
		}
		if (has_tel_condition_pvp()){
			output.wirteInt32(8, _tel_condition_pvp);
		}
		if (has_exp_recovery()){
			output.wirteInt32(9, _exp_recovery);
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
					set_mode(PSSSettingInfo.eMode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_off_condition_death(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_off_condition_peace(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_off_condition_comeback(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_tel_condition_hp(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_tel_condition_peace(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_tel_condition_surrounded(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_tel_condition_pvp(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_exp_recovery(input.readInt32());
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

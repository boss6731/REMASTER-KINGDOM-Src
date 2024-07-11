package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO£ºí»ÔÑ?ßæ PROTO ïïãÒØ§¡£ë¦ MJSoft ð²íÂ¡£
public class BloodPledgeListInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static BloodPledgeListInfoT newInstance(){
		return new BloodPledgeListInfoT();
	}
	private byte[] _pledge_name;
	private byte[] _master_name;
	private int _emblem_id;
	private int _member_count;
	private byte[] _introduction_message;
	private int _weekly_contribution_total;
	private ePLEDGE_JOIN_REQ_TYPE_COMMON _join_type;
	private boolean _enable_join;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private BloodPledgeListInfoT(){
	}
	public byte[] get_pledge_name(){
		return _pledge_name;
	}
	public void set_pledge_name(byte[] val){
		_bit |= 0x1;
		_pledge_name = val;
	}
	public boolean has_pledge_name(){
		return (_bit & 0x1) == 0x1;
	}
	public byte[] get_master_name(){
		return _master_name;
	}
	public void set_master_name(byte[] val){
		_bit |= 0x2;
		_master_name = val;
	}
	public boolean has_master_name(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_emblem_id(){
		return _emblem_id;
	}
	public void set_emblem_id(int val){
		_bit |= 0x4;
		_emblem_id = val;
	}
	public boolean has_emblem_id(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_member_count(){
		return _member_count;
	}
	public void set_member_count(int val){
		_bit |= 0x8;
		_member_count = val;
	}
	public boolean has_member_count(){
		return (_bit & 0x8) == 0x8;
	}
	public byte[] get_introduction_message(){
		return _introduction_message;
	}
	public void set_introduction_message(byte[] val){
		_bit |= 0x10;
		_introduction_message = val;
	}
	public boolean has_introduction_message(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_weekly_contribution_total(){
		return _weekly_contribution_total;
	}
	public void set_weekly_contribution_total(int val){
		_bit |= 0x20;
		_weekly_contribution_total = val;
	}
	public boolean has_weekly_contribution_total(){
		return (_bit & 0x20) == 0x20;
	}
	public ePLEDGE_JOIN_REQ_TYPE_COMMON get_join_type(){
		return _join_type;
	}
	public void set_join_type(ePLEDGE_JOIN_REQ_TYPE_COMMON val){
		_bit |= 0x40;
		_join_type = val;
	}
	public boolean has_join_type(){
		return (_bit & 0x40) == 0x40;
	}
	public boolean get_enable_join(){
		return _enable_join;
	}
	public void set_enable_join(boolean val){
		_bit |= 0x80;
		_enable_join = val;
	}
	public boolean has_enable_join(){
		return (_bit & 0x80) == 0x80;
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
		if (has_pledge_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _pledge_name);
		}
		if (has_master_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _master_name);
		}
		if (has_emblem_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _emblem_id);
		}
		if (has_member_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _member_count);
		}
		if (has_introduction_message()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(5, _introduction_message);
		}
		if (has_weekly_contribution_total()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _weekly_contribution_total);
		}
		if (has_join_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(7, _join_type.toInt());
		}
		if (has_enable_join()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(8, _enable_join);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_pledge_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_master_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_emblem_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_member_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_introduction_message()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weekly_contribution_total()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_join_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enable_join()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_pledge_name()){
			output.writeBytes(1, _pledge_name);
		}
		if (has_master_name()){
			output.writeBytes(2, _master_name);
		}
		if (has_emblem_id()){
			output.writeUInt32(3, _emblem_id);
		}
		if (has_member_count()){
			output.writeUInt32(4, _member_count);
		}
		if (has_introduction_message()){
			output.writeBytes(5, _introduction_message);
		}
		if (has_weekly_contribution_total()){
			output.wirteInt32(6, _weekly_contribution_total);
		}
		if (has_join_type()){
			output.writeEnum(7, _join_type.toInt());
		}
		if (has_enable_join()){
			output.writeBool(8, _enable_join);
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
					set_pledge_name(input.readBytes());
					break;
				}
				case 0x00000012:{
					set_master_name(input.readBytes());
					break;
				}
				case 0x00000018:{
					set_emblem_id(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_member_count(input.readUInt32());
					break;
				}
				case 0x0000002A:{
					set_introduction_message(input.readBytes());
					break;
				}
				case 0x00000030:{
					set_weekly_contribution_total(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_join_type(ePLEDGE_JOIN_REQ_TYPE_COMMON.fromInt(input.readEnum()));
					break;
				}
				case 0x00000040:{
					set_enable_join(input.readBool());
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

			// TODO : ¾Æ·¡ºÎÅÍ Ã³¸® ÄÚµå¸¦ »ðÀÔÇÏ½Ê½Ã¿À. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new BloodPledgeListInfoT();
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

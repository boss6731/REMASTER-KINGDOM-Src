package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CenterMessageT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CenterMessageT newInstance(){
		return new CenterMessageT();
	}
	private int _suffix_number;
	private String _center_msg;
	private String _rgb;
	private int _duration;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CenterMessageT(){
	}
	public int get_suffix_number(){
		return _suffix_number;
	}
	public void set_suffix_number(int val){
		_bit |= 0x1;
		_suffix_number = val;
	}
	public boolean has_suffix_number(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_center_msg(){
		return _center_msg;
	}
	public void set_center_msg(String val){
		_bit |= 0x2;
		_center_msg = val;
	}
	public boolean has_center_msg(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_rgb(){
		return _rgb;
	}
	public void set_rgb(String val){
		_bit |= 0x4;
		_rgb = val;
	}
	public boolean has_rgb(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_duration(){
		return _duration;
	}
	public void set_duration(int val){
		_bit |= 0x8;
		_duration = val;
	}
	public boolean has_duration(){
		return (_bit & 0x8) == 0x8;
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
		if (has_suffix_number()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeSInt32Size(1, _suffix_number);
		}
		if (has_center_msg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _center_msg);
		}
		if (has_rgb()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _rgb);
		}
		if (has_duration()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeSInt32Size(4, _duration);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_suffix_number()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_center_msg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_rgb()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_duration()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_suffix_number()){
			output.writeSInt32(1, _suffix_number);
		}
		if (has_center_msg()){
			output.writeString(2, _center_msg);
		}
		if (has_rgb()){
			output.writeString(3, _rgb);
		}
		if (has_duration()){
			output.writeSInt32(4, _duration);
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
					set_suffix_number(input.readSInt32());
					break;
				}
				case 0x00000012:{
					set_center_msg(input.readString());
					break;
				}
				case 0x0000001A:{
					set_rgb(input.readString());
					break;
				}
				case 0x00000020:{
					set_duration(input.readSInt32());
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
		return new CenterMessageT();
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

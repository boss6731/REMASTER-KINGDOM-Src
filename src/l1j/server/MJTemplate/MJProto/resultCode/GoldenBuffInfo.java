package l1j.server.MJTemplate.MJProto.resultCode;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class GoldenBuffInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static GoldenBuffInfo newInstance(){
		return new GoldenBuffInfo();
	}
	private int _index;
	private int _type;
	private java.util.LinkedList<Integer> _grade;
	private int _remain_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private GoldenBuffInfo(){
	}
	public int get_index(){
		return _index;
	}
	public void set_index(int val){
		_bit |= 0x1;
		_index = val;
	}
	public boolean has_index(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_type(){
		return _type;
	}
	public void set_type(int val){
		_bit |= 0x2;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<Integer> get_grade(){
		return _grade;
	}
	public void add_grade(int val){
		if(!has_grade()){
			_grade = new java.util.LinkedList<Integer>();
			_bit |= 0x4;
		}
		_grade.add(val);
	}
	public boolean has_grade(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_remain_time(){
		return _remain_time;
	}
	public void set_remain_time(int val){
		_bit |= 0x8;
		_remain_time = val;
	}
	public boolean has_remain_time(){
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
		if (has_index()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
		}
		if (has_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _type);
		}
		if (has_grade()){
			for(int val : _grade){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, val);
			}
		}
		if (has_remain_time()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _remain_time);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
/*		if (has_grade()){
			for(int val : _grade){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}*/
		if (!has_remain_time()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_index()){
			output.wirteInt32(1, _index);
		}
		if (has_type()){
			output.wirteInt32(2, _type);
		}
		if (has_grade()){
			for (int val : _grade){
				output.wirteInt32(3, val);
			}
		}
		if (has_remain_time()){
			output.wirteInt32(4, _remain_time);
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
					set_index(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_type(input.readInt32());
					break;
				}
				case 0x00000018:{
					add_grade(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_remain_time(input.readInt32());
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
		return new GoldenBuffInfo();
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

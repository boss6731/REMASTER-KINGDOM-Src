package l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_WEAPON_MASTERY_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_WEAPON_MASTERY_INFO_NOTI newInstance(){
		return new SC_WEAPON_MASTERY_INFO_NOTI();
	}
	private SC_WEAPON_MASTERY_INFO_NOTI _weapon_mastery;
	private int _mastery_gauge;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_WEAPON_MASTERY_INFO_NOTI(){
	}
	public SC_WEAPON_MASTERY_INFO_NOTI get_weapon_mastery(){
		return _weapon_mastery;
	}
	public void set_weapon_mastery(SC_WEAPON_MASTERY_INFO_NOTI val){
		_bit |= 0x1;
		_weapon_mastery = val;
	}
	public boolean has_weapon_mastery(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_mastery_gauge(){
		return _mastery_gauge;
	}
	public void set_mastery_gauge(int val){
		_bit |= 0x2;
		_mastery_gauge = val;
	}
	public boolean has_mastery_gauge(){
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
		if (has_weapon_mastery()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _weapon_mastery);
		}
		if (has_mastery_gauge()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _mastery_gauge);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_weapon_mastery()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_weapon_mastery()){
			output.writeMessage(1, _weapon_mastery);
		}
		if (has_mastery_gauge()){
			output.wirteInt32(2, _mastery_gauge);
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
					set_weapon_mastery((SC_WEAPON_MASTERY_INFO_NOTI)input.readMessage(SC_WEAPON_MASTERY_INFO_NOTI.newInstance()));
					break;
				}
				case 0x00000010:{
					set_mastery_gauge(input.readInt32());
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
		return new SC_WEAPON_MASTERY_INFO_NOTI();
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

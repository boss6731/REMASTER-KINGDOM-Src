package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanStorageTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.storage.CharacterStorage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc) {
		SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK ack = SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK.newInstance();

		if (ClanStorageTable.getInstance().get_StorageList(pc) != null) {
			for (String name : ClanStorageTable.getInstance().get_StorageList(pc)) {
				ack.add_limit_user_names(name);
	//			System.out.println(name);
			}
		}
		
		pc.sendPackets(ack, MJEProtoMessages.SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK, true);
		
	}
	
	public static SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK();
	}
	private java.util.LinkedList<String> _limit_user_names;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK(){
	}
	public java.util.LinkedList<String> get_limit_user_names(){
		return _limit_user_names;
	}
	public void add_limit_user_names(String val){
		if(!has_limit_user_names()){
			_limit_user_names = new java.util.LinkedList<String>();
			_bit |= 0x1;
		}
		_limit_user_names.add(val);
	}
	public boolean has_limit_user_names(){
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
		if (has_limit_user_names()){
			for(String val : _limit_user_names){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		/*if (has_limit_user_names()){
			for(String val : _limit_user_names){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}*/
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_limit_user_names()){
			for (String val : _limit_user_names){
				output.writeString(1, val);
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
				case 0x0000000A:{
					add_limit_user_names(input.readString());
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
		return new SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_ACK();
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

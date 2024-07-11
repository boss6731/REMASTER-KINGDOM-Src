package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.ClanStorageTable;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc) {
		SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI noti = SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI.newInstance();

		if (ClanStorageTable.getInstance().is_ClanStorageUse(pc, pc.getName())) {
			noti.set_store_allow(true);
		} else {
			noti.set_store_allow(false);
		}
		
		
//		pc.sendPackets(noti, MJEProtoMessages.SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI, true);
	}
	public static SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI newInstance(){
		return new SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI();
	}
	private boolean _store_allow;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI(){
	}
	public boolean get_store_allow(){
		return _store_allow;
	}
	public void set_store_allow(boolean val){
		_bit |= 0x1;
		_store_allow = val;
	}
	public boolean has_store_allow(){
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
		if (has_store_allow()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _store_allow);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_store_allow()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_store_allow()){
			output.writeBool(1, _store_allow);
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
					set_store_allow(input.readBool());
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
		return new SC_BLOOD_PLEDGE_STORE_ALLOW_INFO_NOTI();
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

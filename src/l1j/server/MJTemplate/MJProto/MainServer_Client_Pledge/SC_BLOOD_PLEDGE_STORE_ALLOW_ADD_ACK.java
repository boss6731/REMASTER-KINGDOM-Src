package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.ClanStorageTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, String name) {
		SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK ack = SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK.newInstance();
		/*eRESULT_OK(0),
		eRESULT_NO_USER(1),
		eRESULT_MAX_USER(2),*/
		ack.set_result(eRESULT.eRESULT_OK);
		ClanStorageTable.getInstance().add_Storage_list(pc, name);

		
		L1PcInstance targetpc = L1World.getInstance().findPlayer(name);
		if (targetpc != null) {
			targetpc.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(targetpc.getClanname(), targetpc.getClanRank(), targetpc));
		}
		
		pc.sendPackets(ack, MJEProtoMessages.SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK, true);
		
	}
	public static SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK();
	}
	private SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK.eRESULT _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK(){
	}
	public SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK.eRESULT get_result(){
		return _result;
	}
	public void set_result(SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK.eRESULT val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
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
					set_result(SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK.eRESULT.fromInt(input.readEnum()));
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
		return new SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK();
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
	public enum eRESULT{
		eRESULT_OK(0),
		eRESULT_NO_USER(1),
		eRESULT_MAX_USER(2),
		;
		private int value;
		eRESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eRESULT v){
			return value == v.value;
		}
		public static eRESULT fromInt(int i){
			switch(i){
			case 0:
				return eRESULT_OK;
			case 1:
				return eRESULT_NO_USER;
			case 2:
				return eRESULT_MAX_USER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eRESULT, %d", i));
			}
		}
	}
}

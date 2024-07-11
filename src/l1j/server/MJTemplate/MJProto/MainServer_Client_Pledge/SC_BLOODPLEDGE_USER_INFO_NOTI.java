package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.datatables.ClanStorageTable;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOODPLEDGE_USER_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ProtoOutputStream sendClanInfo(String clanname, int rank, L1PcInstance pc){
		SC_BLOODPLEDGE_USER_INFO_NOTI noti = SC_BLOODPLEDGE_USER_INFO_NOTI.newInstance();
		noti.set_bloodpledge_name(clanname.getBytes(MJEncoding.MS949));
		noti.set_rank(rank);
		noti.set_store_allow(ClanStorageTable.getInstance().is_ClanStorageUse(pc, pc.getName()));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_BLOODPLEDGE_USER_INFO_NOTI);
		noti.dispose();
		return stream;
	}
	
	public static void sendClanInfo(L1PcInstance pc, String clanName, int rank) {
		SC_BLOODPLEDGE_USER_INFO_NOTI noti = SC_BLOODPLEDGE_USER_INFO_NOTI.newInstance();
		noti.set_bloodpledge_name(pc.getClanname().getBytes(MJEncoding.MS949));
		noti.set_rank(pc.getClanRank());
		noti.set_store_allow(ClanStorageTable.getInstance().is_ClanStorageUse(pc, pc.getName()));
		pc.sendPackets(noti, MJEProtoMessages.SC_BLOODPLEDGE_USER_INFO_NOTI);
	}
	
	public static SC_BLOODPLEDGE_USER_INFO_NOTI newInstance(){
		return new SC_BLOODPLEDGE_USER_INFO_NOTI();
	}
	private byte[] _bloodpledge_name;
	private int _rank;
	private Boolean _store_allow;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOODPLEDGE_USER_INFO_NOTI(){
	}
	public byte[] get_bloodpledge_name(){
		return _bloodpledge_name;
	}
	public void set_bloodpledge_name(byte[] val){
		_bit |= 0x1;
		_bloodpledge_name = val;
	}
	public boolean has_bloodpledge_name(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_rank(){
		return _rank;
	}
	public void set_rank(int val){
		_bit |= 0x2;
		_rank = val;
	}
	public boolean has_rank(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_store_allow(){
		return _store_allow;
	}
	public void set_store_allow(boolean val){
		_bit |= 0x4;
		_store_allow = val;
	}
	public boolean has_store_allow(){
		return (_bit & 0x4) == 0x4;
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
		if (has_bloodpledge_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _bloodpledge_name);
		}
		if (has_rank()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _rank);
		}
		if (has_store_allow()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _store_allow);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_bloodpledge_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_rank()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_bloodpledge_name()){
			output.writeBytes(1, _bloodpledge_name);
		}
		if (has_rank()){
			output.writeUInt32(2, _rank);
		}
		if (has_store_allow()){
			output.writeBool(3, _store_allow);
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
					set_bloodpledge_name(input.readBytes());
					break;
				}
				case 0x00000010:{
					set_rank(input.readUInt32());
					break;
				}
				case 0x00000018:{
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_BLOODPLEDGE_USER_INFO_NOTI();
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

package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.ClanBanListTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc) {
		SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK ack = SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK.newInstance();

		
		ack.set_limit_level(ClanBanListTable.getInstance().getLimitLevel(pc.getClanname()));
		
		
		ArrayList<String> banlist = ClanBanListTable.getInstance().getBanList(pc.getClanname());
		if (banlist != null) {
			for (String name : banlist) {
				ack.add_limit_user_names(name);		
			}
		}
		
		
		
		
		pc.sendPackets(ack, MJEProtoMessages.SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK, true);
	}
	public static SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK();
	}
	private int _limit_level;
	private java.util.LinkedList<String> _limit_user_names;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK(){
	}
	public int get_limit_level(){
		return _limit_level;
	}
	public void set_limit_level(int val){
		_bit |= 0x1;
		_limit_level = val;
	}
	public boolean has_limit_level(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<String> get_limit_user_names(){
		return _limit_user_names;
	}
	public void add_limit_user_names(String val){
		if(!has_limit_user_names()){
			_limit_user_names = new java.util.LinkedList<String>();
			_bit |= 0x2;
		}
		_limit_user_names.add(val);
	}
	public boolean has_limit_user_names(){
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
		if (has_limit_level()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _limit_level);
		}
		if (has_limit_user_names()){
			for(String val : _limit_user_names){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_limit_level()){
			_memorizedIsInitialized = -1;
			return false;
		}
/*		if (has_limit_user_names()){
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
		if (has_limit_level()){
			output.writeUInt32(1, _limit_level);
		}
		if (has_limit_user_names()){
			for (String val : _limit_user_names){
				output.writeString(2, val);
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
				case 0x00000008:{
					set_limit_level(input.readUInt32());
					break;
				}
				case 0x00000012:{
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
		return new SC_BLOOD_PLEDGE_JOIN_LIMIT_INFO_ACK();
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

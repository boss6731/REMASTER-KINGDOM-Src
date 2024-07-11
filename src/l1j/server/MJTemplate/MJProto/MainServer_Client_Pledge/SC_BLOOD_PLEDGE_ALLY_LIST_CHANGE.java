package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void ally_list_change_send(L1PcInstance pc, int clanid, boolean insert_type, boolean dismiss) {
		SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE noti = SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.newInstance();
		PledgeInfo info = PledgeInfo.newInstance();			
		L1Clan alliance_clan = L1World.getInstance().getClan(clanid);
		info.set_pledge_id(alliance_clan.getClanId());
		info.set_pledge_name(alliance_clan.getClanName());
		info.set_master_name(alliance_clan.getLeaderName());
		info.set_emblem_id(alliance_clan.getEmblemId());
		if (insert_type)
			noti.set_add_pledge(info);
		else
			noti.set_remove_pledge(info);		
		noti.set_dismiss_ally(dismiss);
		pc.sendPackets(noti, MJEProtoMessages.SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE, true);
	}
	
	public static SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE newInstance(){
		return new SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE();
	}
	private PledgeInfo _add_pledge;
	private PledgeInfo _remove_pledge;
	private boolean _dismiss_ally;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE(){
	}
	public PledgeInfo get_add_pledge(){
		return _add_pledge;
	}
	public void set_add_pledge(PledgeInfo val){
		_bit |= 0x1;
		_add_pledge = val;
	}
	public boolean has_add_pledge(){
		return (_bit & 0x1) == 0x1;
	}
	public PledgeInfo get_remove_pledge(){
		return _remove_pledge;
	}
	public void set_remove_pledge(PledgeInfo val){
		_bit |= 0x2;
		_remove_pledge = val;
	}
	public boolean has_remove_pledge(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_dismiss_ally(){
		return _dismiss_ally;
	}
	public void set_dismiss_ally(boolean val){
		_bit |= 0x4;
		_dismiss_ally = val;
	}
	public boolean has_dismiss_ally(){
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
		if (has_add_pledge()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _add_pledge);
		}
		if (has_remove_pledge()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _remove_pledge);
		}
		if (has_dismiss_ally()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _dismiss_ally);
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
		if (has_add_pledge()){
			output.writeMessage(1, _add_pledge);
		}
		if (has_remove_pledge()){
			output.writeMessage(2, _remove_pledge);
		}
		if (has_dismiss_ally()){
			output.writeBool(3, _dismiss_ally);
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
					set_add_pledge((PledgeInfo)input.readMessage(PledgeInfo.newInstance()));
					break;
				}
				case 0x00000012:{
					set_remove_pledge((PledgeInfo)input.readMessage(PledgeInfo.newInstance()));
					break;
				}
				case 0x00000018:{
					set_dismiss_ally(input.readBool());
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
		return new SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE();
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

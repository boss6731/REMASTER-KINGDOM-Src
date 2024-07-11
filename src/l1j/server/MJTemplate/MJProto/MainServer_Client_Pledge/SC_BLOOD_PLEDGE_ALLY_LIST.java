package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOOD_PLEDGE_ALLY_LIST implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void ally_list_send(L1PcInstance pc) {
		SC_BLOOD_PLEDGE_ALLY_LIST noti = SC_BLOOD_PLEDGE_ALLY_LIST.newInstance();
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan.getAllianceList() != null) {
			String  alliance_clanlist = clan.getAllianceList().toString();
			int first_idx = alliance_clanlist.indexOf("[") + 1;
			int last_idx = alliance_clanlist.lastIndexOf("]");
			if (first_idx > -1 && last_idx > -1) {
				String claninfo = alliance_clanlist.substring(first_idx, last_idx);
				String[] alliance_clan_id = (String[]) MJArrangeParser.parsing(claninfo, ", ", MJArrangeParseeFactory.createStringArrange()).result();
				for (int i = 0; i < alliance_clan_id.length; i++) {
					PledgeInfo info = PledgeInfo.newInstance();
					L1Clan alliance_clan = L1World.getInstance().getClan(Integer.parseInt(alliance_clan_id[i]));
					if (alliance_clan.isAlliance_leader()) {
						noti.set_leader_pledge_foundation_day((int) (alliance_clan.getClanBirthDay().getTime() / 1000));
						info.set_pledge_id(alliance_clan.getClanId());
						info.set_pledge_name(alliance_clan.getClanName());
						info.set_master_name(alliance_clan.getLeaderName());
						info.set_emblem_id(alliance_clan.getEmblemId());
						noti.set_master(info);
					} else {
						noti.set_leader_pledge_foundation_day((int) (alliance_clan.getClanBirthDay().getTime() / 1000));
						info.set_pledge_id(alliance_clan.getClanId());
						info.set_pledge_name(alliance_clan.getClanName());
						info.set_master_name(alliance_clan.getLeaderName());
						info.set_emblem_id(alliance_clan.getEmblemId());
						noti.add_allys(info);
					}
				}
			}
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_BLOOD_PLEDGE_ALLY_LIST, true);
	}
	
	public static SC_BLOOD_PLEDGE_ALLY_LIST newInstance(){
		return new SC_BLOOD_PLEDGE_ALLY_LIST();
	}
	private PledgeInfo _master;
	private int _leader_pledge_foundation_day;
	private java.util.LinkedList<PledgeInfo> _allys;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_ALLY_LIST(){
	}
	public PledgeInfo get_master(){
		return _master;
	}
	public void set_master(PledgeInfo val){
		_bit |= 0x1;
		_master = val;
	}
	public boolean has_master(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_leader_pledge_foundation_day(){
		return _leader_pledge_foundation_day;
	}
	public void set_leader_pledge_foundation_day(int val){
		_bit |= 0x2;
		_leader_pledge_foundation_day = val;
	}
	public boolean has_leader_pledge_foundation_day(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<PledgeInfo> get_allys(){
		return _allys;
	}
	public void add_allys(PledgeInfo val){
		if(!has_allys()){
			_allys = new java.util.LinkedList<PledgeInfo>();
			_bit |= 0x4;
		}
		_allys.add(val);
	}
	public boolean has_allys(){
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
		if (has_master()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _master);
		}
		if (has_leader_pledge_foundation_day()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _leader_pledge_foundation_day);
		}
		if (has_allys()){
			for(PledgeInfo val : _allys){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_master()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_leader_pledge_foundation_day()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_allys()){
			for(PledgeInfo val : _allys){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_master()){
			output.writeMessage(1, _master);
		}
		if (has_leader_pledge_foundation_day()){
			output.wirteInt32(2, _leader_pledge_foundation_day);
		}
		if (has_allys()){
			for (PledgeInfo val : _allys){
				output.writeMessage(3, val);
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
					set_master((PledgeInfo)input.readMessage(PledgeInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_leader_pledge_foundation_day(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_allys((PledgeInfo)input.readMessage(PledgeInfo.newInstance()));
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
		return new SC_BLOOD_PLEDGE_ALLY_LIST();
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

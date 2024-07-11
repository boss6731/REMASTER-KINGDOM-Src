package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.BloodPledgeListInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ePLEDGE_JOIN_REQ_TYPE_COMMON;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO：此為自動生成的 PROTO 代碼，由 MJSoft 製作。
public class SC_BLOOD_PLEDGE_LIST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	public static void clan_list_send(L1PcInstance pc) {
		SC_BLOOD_PLEDGE_LIST_ACK noti = SC_BLOOD_PLEDGE_LIST_ACK.newInstance();

		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan != null) {
				if (clan.getClanName().equalsIgnoreCase("紅騎士團")) // 跳過名字為"RED KNIGHTS"的公會
					continue;

				BloodPledgeListInfoT info = BloodPledgeListInfoT.newInstance();
				info.set_pledge_name(clan.getClanName().getBytes()); // 設置公會名稱
				info.set_master_name(clan.getLeaderName().getBytes()); // 設置會長名稱
				info.set_emblem_id(clan.getEmblemId()); // 設置公會徽章ID
				info.set_member_count(clan.getClanMemberList().size()); // 設置公會成員數量
				info.set_introduction_message(clan.getIntroduction() != null ? clan.getIntroduction().getBytes() : "".getBytes()); // 設置介紹消息
				info.set_weekly_contribution_total(0); // 設置每週貢獻總額（暫時為0）
				info.set_join_type(ePLEDGE_JOIN_REQ_TYPE_COMMON.fromInt(clan.getJoinSetting() == 1 ? clan.getJoinType() : 3)); // 設置加入類型
				info.set_enable_join(clan.getJoinSetting() == 1); // 設置是否允許加入
				noti.add_blood_pledge_list_info(info); // 添加公會信息到通知
			}
		}

		noti.set_is_last(true); // 設置是否為最後一個通知
		pc.sendPackets(noti, MJEProtoMessages.SC_BLOOD_PLEDGE_LIST_ACK, true); // 發送通知給玩家
	}
}
	public static SC_BLOOD_PLEDGE_LIST_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_LIST_ACK();
	}
	private java.util.LinkedList<BloodPledgeListInfoT> _blood_pledge_list_info;
	private boolean _is_last;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_LIST_ACK(){
	}
	public java.util.LinkedList<BloodPledgeListInfoT> get_blood_pledge_list_info(){
		return _blood_pledge_list_info;
	}
	public void add_blood_pledge_list_info(BloodPledgeListInfoT val){
		if(!has_blood_pledge_list_info()){
			_blood_pledge_list_info = new java.util.LinkedList<BloodPledgeListInfoT>();
			_bit |= 0x1;
		}
		_blood_pledge_list_info.add(val);
	}
	public boolean has_blood_pledge_list_info(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_is_last(){
		return _is_last;
	}
	public void set_is_last(boolean val){
		_bit |= 0x2;
		_is_last = val;
	}
	public boolean has_is_last(){
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
		if (has_blood_pledge_list_info()){
			for(BloodPledgeListInfoT val : _blood_pledge_list_info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_is_last()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_last);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_blood_pledge_list_info()){
			for(BloodPledgeListInfoT val : _blood_pledge_list_info){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_is_last()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_blood_pledge_list_info()){
			for (BloodPledgeListInfoT val : _blood_pledge_list_info){
				output.writeMessage(1, val);
			}
		}
		if (has_is_last()){
			output.writeBool(2, _is_last);
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
					add_blood_pledge_list_info((BloodPledgeListInfoT)input.readMessage(BloodPledgeListInfoT.newInstance()));
					break;
				}
				case 0x00000010:{
					set_is_last(input.readBool());
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
		return new SC_BLOOD_PLEDGE_LIST_ACK();
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

package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_ACK.ePLEDGE_JOIN_ACK_RESULT;
import l1j.server.server.datatables.ClanBanListTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EinhasadClanBuff;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_BLOOD_PLEDGE_JOIN_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_BLOOD_PLEDGE_JOIN_REQ newInstance(){
		return new CS_BLOOD_PLEDGE_JOIN_REQ();
	}
	private String _pledge_name;
	private ePLEDGE_JOIN_REQ_TYPE _join_type;
	private String _join_msg;
	private byte[] _hashed_password;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_BLOOD_PLEDGE_JOIN_REQ(){
	}
	public String get_pledge_name(){
		return _pledge_name;
	}
	public void set_pledge_name(String val){
		_bit |= 0x1;
		_pledge_name = val;
	}
	public boolean has_pledge_name(){
		return (_bit & 0x1) == 0x1;
	}
	public ePLEDGE_JOIN_REQ_TYPE get_join_type(){
		return _join_type;
	}
	public void set_join_type(ePLEDGE_JOIN_REQ_TYPE val){
		_bit |= 0x2;
		_join_type = val;
	}
	public boolean has_join_type(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_join_msg(){
		return _join_msg;
	}
	public void set_join_msg(String val){
		_bit |= 0x4;
		_join_msg = val;
	}
	public boolean has_join_msg(){
		return (_bit & 0x4) == 0x4;
	}
	public byte[] get_hashed_password(){
		return _hashed_password;
	}
	public void set_hashed_password(byte[] val){
		_bit |= 0x8;
		_hashed_password = val;
	}
	public boolean has_hashed_password(){
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
		if (has_pledge_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _pledge_name);
		}
		if (has_join_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _join_type.toInt());
		}
		if (has_join_msg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _join_msg);
		}
		if (has_hashed_password()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _hashed_password);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_pledge_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_join_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_join_msg()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_pledge_name()){
			output.writeString(1, _pledge_name);
		}
		if (has_join_type()){
			output.writeEnum(2, _join_type.toInt());
		}
		if (has_join_msg()){
			output.writeString(3, _join_msg);
		}
		if (has_hashed_password()){
			output.writeBytes(4, _hashed_password);
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
					set_pledge_name(input.readString());
					break;
				}
				case 0x00000010:{
					set_join_type(ePLEDGE_JOIN_REQ_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A:{
					set_join_msg(input.readString());
					break;
				}
				case 0x00000022:{
					set_hashed_password(input.readBytes());
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

			if (get_hashed_password() == null)
				set_hashed_password("".getBytes());
			
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			String clanname = get_pledge_name();
			L1Clan clan = L1World.getInstance().findClan(clanname);
			if (clan == null) {
				pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, get_pledge_name(), get_join_type().toInt(), 4));
				return this;
			}
			
			if (clan.getJoinSetting() == 0) {
				SC_BLOOD_PLEDGE_JOIN_ACK.ack(pc, ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_EXIST);
				return this;
			}
			
			if (clan.getJoinType() == 0)
				set_join_type(ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY);
			else if (clan.getJoinType() == 1)
				set_join_type(ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_CONFIRMATION);
			else if (clan.getJoinType() == 2)
				set_join_type(ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_PASSWORD);
			
			if (!isInitialized())
				return this;

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			
			if (MJShiftBattlePlayManager.is_shift_battle(pc))
				return this;	

			// 이미 혈맹에 가입한 상태 입니다.
			if (pc.getClanid() != 0) {
				L1Clan Sendclan = pc.getClan();
				if (Sendclan.getLeaderId() != pc.getId()) {
					pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, get_pledge_name(), get_join_type().toInt(), 9));
					return this;
				} else {
					if (Sendclan.getCastleId() > 0) {
						pc.sendPackets(new S_ServerMessage(ServerMessage.HAVING_NEST_OF_CLAN));
						return this;
					}

					if (Sendclan.getCurrentWar() != null) {
						pc.sendPackets(new S_ServerMessage(ServerMessage.CANNOT_BREAK_CLAN));
						return this;
					}

					if (Sendclan.AllianceSize() > 0) {
						pc.sendPackets(new S_ServerMessage(ServerMessage.CANNOT_BREAK_CLAN_HAVING_FRIENDS));
						return this;
					}
				}
			} 
			int limit_level = ClanBanListTable.getInstance().getLimitLevel(clanname);

			if (ClanBanListTable.getInstance().checkClanBanlist(clanname, pc.getName())) {
				pc.sendPackets("檢查該血盟是否被封鎖了加入資格。");
				return this;
			}

// 請與軍主見面後加入血盟
			try {
				L1PcInstance crown = clan.getonline간부(); // 獲取在線的血盟軍主
				switch (clan.getJoinType()) {
					case 1:
						// 檢?玩家等級是否達到加入血盟的最低要求
						if (pc.getLevel() < limit_level) {
							pc.sendPackets("該血盟需要 " + limit_level + " 級以上才能加入。");
							return this;
						}
						set_join_type(ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_CONFIRMATION);
						if (crown == null) {
							pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, get_pledge_name(), 1, 11));
							return this;
						}
						crown.setTempID(pc.getId()); // 保存對方的對象 ID
						S_Message_YN myn = new S_Message_YN(97, pc.getName());
						crown.sendPackets(myn, true);
						pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, get_pledge_name(), get_join_type().toInt(), 1));
						break;

					case 2:
						set_join_type(ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_PASSWORD);
						String code = new String(get_hashed_password());
						if (clan.getJoinPassword() == null || !clan.getJoinPassword().equalsIgnoreCase(code)) {
							pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 3), true);
							return this;
						}
						// If the join type is password, the code goes here (case 2 falls through to case 0 if password is correct)

					case 0:
						// 檢?是否?新手保護血盟
						if (clan.getClanName().equalsIgnoreCase("新手保護")) { // "신규보호" 代表新手保護
							if (pc.getLevel() >= Config.ServerAdSetting.NEWPLAYERLEVELPURGE) {
								pc.sendPackets(new S_SystemMessage(Config.ServerAdSetting.NEWPLAYERLEVELPURGE + " 級以上無法加入新手保護血盟。"));
								return this;
							}
						}
						// 經過上述檢?後，繼續處理邏輯
				}
			} catch (Exception e) {
				e.printStackTrace();
				// 處理異常
			}
						L1ClanJoin.getInstance().tutorialJoin(clan, pc);
						pc.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), pc.getClanRank(), pc));
					} else {
						set_join_type(ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_IMMEDIATLY);
						if (L1ClanJoin.getInstance().ClanJoin(clan, pc)) {
							pc.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), pc.getClanRank(), pc));
						} else
							pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, get_pledge_name(), get_join_type().toInt(), 1));
					}
					break;
				default:
					pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, get_pledge_name(), get_join_type().toInt(), 11));
					break;
				}
				
				pc.sendPackets(new S_EinhasadClanBuff(pc), true);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_BLOOD_PLEDGE_JOIN_REQ();
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

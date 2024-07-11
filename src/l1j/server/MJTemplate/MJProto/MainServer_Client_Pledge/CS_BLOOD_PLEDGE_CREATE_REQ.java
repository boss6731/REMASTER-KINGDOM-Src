package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.BadNamesList;
import l1j.server.server.datatables.ClanBuffTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_EinhasadClanBuff;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Pledge;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.storage.mysql.MySqlCharacterStorage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_BLOOD_PLEDGE_CREATE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_BLOOD_PLEDGE_CREATE_REQ newInstance(){
		return new CS_BLOOD_PLEDGE_CREATE_REQ();
	}
	private byte[] _name;
	private ePLEDGE_JOIN_REQ_TYPE _join_type;
	private byte[] _hashed_password;
	private byte[] _introduction_message;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_BLOOD_PLEDGE_CREATE_REQ(){
	}
	public byte[] get_name(){
		return _name;
	}
	public void set_name(byte[] val){
		_bit |= 0x1;
		_name = val;
	}
	public boolean has_name(){
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
	public byte[] get_hashed_password(){
		return _hashed_password;
	}
	public void set_hashed_password(byte[] val){
		_bit |= 0x4;
		_hashed_password = val;
	}
	public boolean has_hashed_password(){
		return (_bit & 0x4) == 0x4;
	}
	public byte[] get_introduction_message(){
		return _introduction_message;
	}
	public void set_introduction_message(byte[] val){
		_bit |= 0x8;
		_introduction_message = val;
	}
	public boolean has_introduction_message(){
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
		if (has_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _name);
		}
		if (has_join_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _join_type.toInt());
		}
		if (has_hashed_password()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _hashed_password);
		}
		if (has_introduction_message()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _introduction_message);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_join_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_introduction_message()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_name()){
			output.writeBytes(1, _name);
		}
		if (has_join_type()){
			output.writeEnum(2, _join_type.toInt());
		}
		if (has_hashed_password()){
			output.writeBytes(3, _hashed_password);
		}
		if (has_introduction_message()){
			output.writeBytes(4, _introduction_message);
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
					set_name(input.readBytes());
					break;
				}
				case 0x00000010:{
					set_join_type(ePLEDGE_JOIN_REQ_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A:{
					set_hashed_password(input.readBytes());
					break;
				}
				case 0x00000022:{
					set_introduction_message(input.readBytes());
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
// 創建 ProtoInputStream 實例，用於從字節數組中讀取數據
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(
				bytes,
				l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE
		);

		try {
			// 從流中讀取數據
			readFrom(is);

			// 檢查對象是否已初始化
			if (!isInitialized()) {
				return this;
			}

			// 獲取玩家實例
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// 初始化變量
			String clan_name = "";
			String password = "";
			String introduction = "";

			// 檢查加入類型是否為 2（假設 2 是不允許的類型）
			if (get_join_type().toInt() == 2) {
				pc.sendPackets("目前無法設置密碼加入。");
				return this;
			}

			// 獲取並轉換名字、密碼和介紹消息
			if (get_name() != null) {
				clan_name = new String(get_name());
			}
			if (get_hashed_password() != null) {
				password = new String(get_hashed_password());
			}
			if (get_introduction_message() != null) {
				introduction = new String(get_introduction_message());
			}

			// 檢查名字是否合法
			if (isInvalidName(clan_name)) {
				pc.sendPackets(new S_SystemMessage("不正確的血盟名稱。"));
				return this;
			}

// 這裡可以繼續添加其他邏輯代碼...

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
			
			if (pc.isCrown()) { // 프린스 또는 프린세스
				if (pc.getClanid() == 0 && pc.getLevel() >= Config.ServerAdSetting.CROWNBLOODLEVEL) {
					if (!pc.getInventory().checkItem(40308, 10000)) {
						pc.sendPackets(new S_ServerMessage(337, "$4")); // \f1%0이 부족합니다.
						return this;
					}
					for (L1Clan clan : L1World.getInstance().getAllClans()) { // \f1 같은 이름의 혈맹이 존재합니다.
						if (clan.getClanName().toLowerCase().equals(clan_name.toLowerCase())) {
							pc.sendPackets(new S_ServerMessage(99)); // \f1 같은 이름의 혈맹이 존재합니다.
							return this;
						}
					}
					L1Clan clan = ClanTable.getInstance().createClan(pc, clan_name); // 크란창설
					pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
					if (clan != null) {
						clan.setEinhasadBlessBuff(0);
						clan.setBuffFirst(ClanBuffTable.getRandomBuff(clan));
						clan.setBuffSecond(ClanBuffTable.getRandomBuff(clan));
						clan.setBuffThird(ClanBuffTable.getRandomBuff(clan));
						
						clan.setJoinType(get_join_type().toInt());
						
//						if (get_join_type().toInt() == 2) {
							//clan.setJoinPassword(password);
//						}
						
						clan.setIntroduction(introduction);
						
						pc.sendPackets(new S_ServerMessage(84, clan_name)); // \f1%0 혈맹이 창설되었습니다.
						pc.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), pc.getClanRank(), pc));
						pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));
						pc.sendPackets(new S_Pledge(pc.getClanid()));
						pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, clan.getClanName(), 0, 0));
						pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false);
						
						pc.sendPackets(new S_EinhasadClanBuff(pc), true);
						Timestamp time = new Timestamp(System.currentTimeMillis());
						pc.setClanJoinDate(time);
						MySqlCharacterStorage.storeClanJoinTime(pc);
						ClanTable.getInstance().updateClan(clan);
					}
				} else {
					pc.sendPackets(new S_SystemMessage("您已經有血盟或只有等級達到 " + Config.ServerAdSetting.CROWNBLOODLEVEL + " 級以上才能創建血盟。"));
				}
			} else {
				pc.sendPackets(new S_ServerMessage(85)); // \f1프린스와 프린세스만이 혈맹을 창설할 수 있습니다.
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	private static boolean isAlphaNumeric(String s) {
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		return flag;
	}

	private static boolean isInvalidName(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ' || // 한문자(char)단위로 비교.
					name.charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ' || name.charAt(i) == 'ㅂ' || // 한문자(char)단위로 비교
					name.charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ' || name.charAt(i) == 'ㅇ' || // 한문자(char)단위로 비교
					name.charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ' || name.charAt(i) == 'ㅋ' || // 한문자(char)단위로 비교.
					name.charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ' || name.charAt(i) == 'ㅛ' || // 한문자(char)단위로 비교.
					name.charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ' || name.charAt(i) == 'ㅔ' || // 한문자(char)단위로 비교.
					name.charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ' || name.charAt(i) == 'ㅣ' || // 한문자(char)단위로 비교.
					name.charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ' || name.charAt(i) == 'ㅒ' || // 한문자(char)단위로 비교.
					name.charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || // 한문자(char)단위로 비교.
					name.charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ' || name.charAt(i) == 'ㅘ' || // 한문자(char)단위로 비교.
					name.charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆' || name.charAt(i) == 'ㅤ') {
				return false;
			}
		}

		if (name.length() == 0) {
			return false;
		}

		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("MS949").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		if (isAlphaNumeric(name)) {
			return false;
		}

		// XXX - 본청의 사양과 동등한가 미확인
		// 전각 문자가 5 문자를 넘는지, 전체로 12바이트를 넘으면(자) 무효인 이름으로 한다
		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			return false;
		}
		return true;
	}
	
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_BLOOD_PLEDGE_CREATE_REQ();
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

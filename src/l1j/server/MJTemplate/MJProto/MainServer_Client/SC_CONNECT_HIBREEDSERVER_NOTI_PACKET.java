package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginInfo;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginManager;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_Paralysis;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CONNECT_HIBREEDSERVER_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static final int RESERVED_FISL_TO_GIRAN = 210;
	public static final int RESERVED_DOM_TO_GIRAN = 211;
	public static final int RESERVED_FISLAND = 111;
	public static final int RESERVED_DOM_TOWER = 112;
	public static final int RESERVED_INTER_MAGIC_DOLL_RACE = 310;
	public static final int RESERVED_INTER_GLUDIO_SIENCE = 10;
	public static final int RESERVED_INTER_GMTEST = 0;
	public static final int RESERVED_INTER_BACK = 99;
	public static final int RESERVED_INTER_BACK_RESTART = 100;
	public static final int RESERVED_INTER_TEST = 8;

	public static SC_CONNECT_HIBREEDSERVER_NOTI_PACKET newInstanceNoti(GameClient clnt, String accountName,
			String accountPassword, int characterId, String characterName, int x, int y, int mapid,
			int backX, int backY, int backMapId, int reserved_no, int back_reserved_no) {
		SC_CONNECT_HIBREEDSERVER_NOTI_PACKET noti = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance();
		try {// 추가
			String ip = Config.Login.ExternalAddress;
			if (clnt.getIp().equalsIgnoreCase("127.0.0.1")) {
				ip = "127.0.0.1";
			}
			clnt.sendPacket(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, true));

			StringTokenizer tok = new StringTokenizer(ip);
			int server_address_bigendian = 0;

			for (int i = 3; i >= 0; --i) {
				int bit = i * 8;
				server_address_bigendian |= (Integer.parseInt(tok.nextToken(".")) << bit) & (0xff << bit);
			}
			noti.set_destIP(server_address_bigendian);
			noti.set_destPort(Config.Login.GameserverPort);
			// noti.set_destPort(2000);
			noti.set_domainname(ip);

			if (reserved_no == RESERVED_DOM_TOWER) {
				noti.set_interkind(7);// 지배의 탑 카인드
			} else if (reserved_no == RESERVED_FISLAND) {
				noti.set_interkind(4);// 잊혀진 섬 카인드
			} else if (reserved_no == RESERVED_INTER_MAGIC_DOLL_RACE) {
				noti.set_interkind(9);
			} else if (reserved_no == RESERVED_INTER_GLUDIO_SIENCE) {
				noti.set_interkind(10);// 글루디오 연구소
			} else if (reserved_no == RESERVED_INTER_BACK || reserved_no == RESERVED_INTER_BACK_RESTART) {
				noti.set_interkind(99); // 귀환용
			} else if (reserved_no == RESERVED_INTER_TEST) {
				noti.set_interkind(8); // 인터공성
			} else if (reserved_no == RESERVED_INTER_GMTEST) {// GM커멘드 테스트용
				noti.set_interkind(0); // GM커멘드 테스트용
			}
			// noti.set_worldmove(true);

			noti.set_onetimetoken(new StringBuilder().append(accountName).append("|").append(characterName).append("|")
					.append(characterId).append("|").append(x).append("|").append(y).append("|")
					.append(mapid).append("|").append(backX).append("|").append(backY).append("|").append(backMapId)
					.append("|").append(back_reserved_no).toString().getBytes());

			CS_HIBREED_AUTH_REQ_PACKET.LOCAL_TEMP_MAP.put(characterId, clnt);
			if (clnt.get_login_info() == null) {
				MJHttpLoginInfo lInfo = MJHttpLoginInfo.newInstance();
				lInfo.set_account(accountName);
				lInfo.set_auth_token(MJString.EmptyString);
				lInfo.set_client_ip(clnt.getIp());
				lInfo.set_client_port(0);
				lInfo.set_hdd_id(MJString.EmptyString);
				lInfo.set_mac_address(MJString.EmptyString);
				lInfo.set_nic_id(MJString.EmptyString);
				lInfo.set_password(accountPassword);
				lInfo.make_auth_token();
				clnt.set_login_info(lInfo);
			}
			MJHttpLoginManager.getInstance().put_login_info(clnt.get_login_info());
			noti.set_reservednumber(reserved_no);
		} catch (Exception e) {// 추가
			e.printStackTrace();// 추가
		}
		return noti;
	}

	public static void do_send(L1PcInstance pc, int x, int y, int mapid, int reserved_no) {
		Object[] petList = pc.getPetList().values().toArray();
		if (petList != null) {
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					summon.onFinalAction(pc, "dismiss");
				}
			}
		}
		if (pc.get_is_client_auto()) {
			pc.do_finish_client_auto(eReason.INVALID_MAP);
		}

		try {
			if (pc.isInParty() || pc.getParty() != null) { // 파티중
				pc.getParty().leaveMember(pc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SC_CONNECT_HIBREEDSERVER_NOTI_PACKET noti = newInstanceNoti(pc.getNetConnection(), pc.getAccountName(),
				pc.getAccount().get_Password(), pc.getId(), pc.getName(), x, y, mapid, pc.getX(),
				pc.getY(), pc.getMapId(), reserved_no, RESERVED_INTER_BACK);
		pc.sendPackets(noti, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET, true);
	}

	public static void do_send_jump(L1PcInstance pc, int x, int y, int mapid, int local) {
		Object[] petList = pc.getPetList().values().toArray();
		if (petList != null) {
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					summon.onFinalAction(pc, "dismiss");
				}
			}
		}

		try {
			if (pc.isInParty() || pc.getParty() != null) { // 파티중
				pc.getParty().leaveMember(pc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (pc.get_is_client_auto()) {
			pc.do_finish_client_auto(eReason.INVALID_MAP);
		}
		SC_CONNECT_HIBREEDSERVER_NOTI_PACKET noti = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance();
		String IP = "";
		if (local == 1)
			IP = Config.Login.LocalAddress;
		else if (local == 2)
			IP = Config.Login.CoderAddress;
		else if (local == 3)
			IP = Config.Login.ExternalAddress;
		StringTokenizer tok = new StringTokenizer(IP);
		int server_address_bigendian = 0;

		for (int i = 3; i >= 0; --i) {
			int bit = i * 8;
			server_address_bigendian |= (Integer.parseInt(tok.nextToken(".")) << bit) & (0xff << bit);
		}
		noti.set_destIP(server_address_bigendian);
		noti.set_destPort(Config.Login.GameserverPort);
		// noti.set_destPort(2000);
		noti.set_domainname(IP);
		noti.set_interkind(RESERVED_INTER_BACK); // 99 가 원상태복귀
		// noti.set_worldmove(true);

		noti.set_onetimetoken(new StringBuilder().append(pc.getAccountName()).append("|").append(pc.getName())
				.append("|").append(pc.getId()).append("|").append(x).append("|").append(y).append("|")
				.append(mapid).append(x).append("|").append(y).append("|").append(mapid).append("|")
				.append(RESERVED_INTER_BACK).toString().getBytes());

		GameClient clnt = pc.getNetConnection();
		CS_HIBREED_AUTH_REQ_PACKET.LOCAL_TEMP_MAP.put(pc.getId(), clnt);
		MJHttpLoginInfo lInfo = MJHttpLoginInfo.newInstance();
		lInfo.set_account(pc.getAccountName());
		lInfo.set_auth_token(MJString.EmptyString);
		lInfo.set_client_ip(clnt.getIp());
		lInfo.set_client_port(0);
		lInfo.set_hdd_id(MJString.EmptyString);
		lInfo.set_mac_address(MJString.EmptyString);
		lInfo.set_nic_id(MJString.EmptyString);
		lInfo.set_password(pc.getAccount().get_Password());
		MJHttpLoginManager.getInstance().put_login_info(lInfo);

		noti.set_reservednumber(RESERVED_INTER_BACK);
		pc.sendPackets(noti, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET, true);
	}

	public static SC_CONNECT_HIBREEDSERVER_NOTI_PACKET newInstance() {
		return new SC_CONNECT_HIBREEDSERVER_NOTI_PACKET();
	}

	private int _destIP;
	private int _destPort;
	private int _reservednumber;
	private byte[] _onetimetoken;
	private int _interkind;
	private String _domainname;
	private boolean _worldmove;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_CONNECT_HIBREEDSERVER_NOTI_PACKET() {
		set_worldmove(false);
	}

	public int get_destIP() {
		return _destIP;
	}

	public void set_destIP(int val) {
		_bit |= 0x1;
		_destIP = val;
	}

	public boolean has_destIP() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_destPort() {
		return _destPort;
	}

	public void set_destPort(int val) {
		_bit |= 0x2;
		_destPort = val;
	}

	public boolean has_destPort() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_reservednumber() {
		return _reservednumber;
	}

	public void set_reservednumber(int val) {
		_bit |= 0x4;
		_reservednumber = val;
	}

	public boolean has_reservednumber() {
		return (_bit & 0x4) == 0x4;
	}

	public byte[] get_onetimetoken() {
		return _onetimetoken;
	}

	public void set_onetimetoken(byte[] val) {
		_bit |= 0x8;
		_onetimetoken = val;
	}

	public boolean has_onetimetoken() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_interkind() {
		return _interkind;
	}

	public void set_interkind(int val) {
		_bit |= 0x10;
		_interkind = val;
	}

	public boolean has_interkind() {
		return (_bit & 0x10) == 0x10;
	}

	public String get_domainname() {
		return _domainname;
	}

	public void set_domainname(String val) {
		_bit |= 0x20;
		_domainname = val;
	}

	public boolean has_domainname() {
		return (_bit & 0x20) == 0x20;
	}

	public boolean get_worldmove() {
		return _worldmove;
	}

	public void set_worldmove(boolean val) {
		_bit |= 0x40;
		_worldmove = val;
	}

	public boolean has_worldmove() {
		return (_bit & 0x40) == 0x40;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_destIP()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _destIP);
		}
		if (has_destPort()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _destPort);
		}
		if (has_reservednumber()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _reservednumber);
		}
		if (has_onetimetoken()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _onetimetoken);
		}
		if (has_interkind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _interkind);
		}
		if (has_domainname()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(6, _domainname);
		}
		if (has_worldmove()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _worldmove);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_destIP()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_destPort()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_reservednumber()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_onetimetoken()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_destIP()) {
			output.writeUInt32(1, _destIP);
		}
		if (has_destPort()) {
			output.wirteInt32(2, _destPort);
		}
		if (has_reservednumber()) {
			output.writeUInt32(3, _reservednumber);
		}
		if (has_onetimetoken()) {
			output.writeBytes(4, _onetimetoken);
		}
		if (has_interkind()) {
			output.wirteInt32(5, _interkind);
		}
		if (has_domainname()) {
			output.writeString(6, _domainname);
		}
		if (has_worldmove()) {
			output.writeBool(7, _worldmove);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				case 0x00000008: {
					set_destIP(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_destPort(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_reservednumber(input.readUInt32());
					break;
				}
				case 0x00000022: {
					set_onetimetoken(input.readBytes());
					break;
				}
				case 0x00000028: {
					set_interkind(input.readInt32());
					break;
				}
				case 0x00000032: {
					set_domainname(input.readString());
					break;
				}
				case 0x00000038: {
					set_worldmove(input.readBool());
					break;
				}
				default: {
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_CONNECT_HIBREEDSERVER_NOTI_PACKET();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}

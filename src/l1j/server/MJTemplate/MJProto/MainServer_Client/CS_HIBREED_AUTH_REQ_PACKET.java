package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import MJShiftObject.MJShiftObjectManager;
import l1j.server.Config;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.clientpackets.C_LoginToServer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_HIBREED_AUTH_REQ_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static final ConcurrentHashMap<Integer, GameClient> LOCAL_TEMP_MAP = new ConcurrentHashMap<Integer, GameClient>();

	public static CS_HIBREED_AUTH_REQ_PACKET newInstance() {
		return new CS_HIBREED_AUTH_REQ_PACKET();
	}

	private long _reservednumber;
	private String _onetimetoken;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_HIBREED_AUTH_REQ_PACKET() {
	}

	public long get_reservednumber() {
		return _reservednumber;
	}

	public void set_reservednumber(long val) {
		_bit |= 0x1;
		_reservednumber = val;
	}

	public boolean has_reservednumber() {
		return (_bit & 0x1) == 0x1;
	}

	public String get_onetimetoken() {
		return _onetimetoken;
	}

	public void set_onetimetoken(String val) {
		_bit |= 0x2;
		_onetimetoken = val;
	}

	public boolean has_onetimetoken() {
		return (_bit & 0x2) == 0x2;
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
		if (has_reservednumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(1, _reservednumber);
		if (has_onetimetoken())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _onetimetoken);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_reservednumber()) {
			output.wirteUInt64(1, _reservednumber);
		}
		if (has_onetimetoken()) {
			output.writeString(2, _onetimetoken);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					set_reservednumber(input.readUInt64());
					break;
				}
				case 0x00000012: {
					set_onetimetoken(input.readString());
					break;
				}
			}
		}
		return this;
	}

	private static final ConcurrentHashMap<Integer, Integer> failureMembers = new ConcurrentHashMap<>();

	@Override
	public MJIProtoMessage readFrom(final l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);
			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			if (Config.Login.UseShiftServer)
				MJShiftObjectManager.getInstance().do_receive(clnt, (int) get_reservednumber(), get_onetimetoken());
			else {
				final int reserved_no = (int) get_reservednumber();
				if (reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_FISLAND || reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_DOM_TOWER
						|| reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_FISL_TO_GIRAN || reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_DOM_TO_GIRAN
						|| reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_MAGIC_DOLL_RACE || reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_GLUDIO_SIENCE
						|| reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_GMTEST || reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK
						|| reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK_RESTART || reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST) {
					String[] array = _onetimetoken.split("\\|");
					if (array.length != 10) {
						System.out.println(String.format("[跨服务器：無效的本地移位資訊... 踢出。 保留字：%d, 客戶端 IP：%s]", get_reservednumber(), clnt.getIp()));
						clnt.kick();
						return this;
					}

					final byte[] datas = bytes;
					final String account_name = array[0];
					final String character_name = array[1];
					final int object_id = Integer.parseInt(array[2]);
					final int x = Integer.parseInt(array[3]);
					final int y = Integer.parseInt(array[4]);
					final int mid = Integer.parseInt(array[5]);
					final int backX = Integer.parseInt(array[6]);
					final int backY = Integer.parseInt(array[7]);
					final int backMapId = Integer.parseInt(array[8]);
					final int backReservedNo = Integer.parseInt(array[9]);

					final Account account = Account.load(account_name);
					if (account == null) {
						System.out.println(String.format("[跨伺服器：無效的本地移位賬戶資訊... 踢出。 保留字：%d, 客戶端 IP：%s, 賬戶名稱：%s, 角色名稱：%s]", get_reservednumber(),
								clnt.getIp(), account_name, character_name));
						clnt.kick();
						return this;
					}

					GameClient old_client = LOCAL_TEMP_MAP.remove(object_id);
					GameClient restart_save_old_client = old_client;
					// /TODO 여기서 팅기는건데..왜..?
					if (old_client == null) {
						System.out.println(String.format("[跨伺服器：無效的本地临时地图客户端....踢出。 保留字：%d, 客戶端 IP：%s, 賬戶名稱：%s, 角色名稱：%s]", get_reservednumber(),
								clnt.getIp(), account_name, character_name));
						clnt.kick();
						return this;
					}
					clnt.set_login_info(old_client.get_login_info());
					old_client.close();

					L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
					if (pc != null) {
						pc.logout();
					}

					SC_HIBREED_AUTH_ACK_PACKET packet = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
					packet.set_result(Result.Result_sucess);
					clnt.sendPacket(packet, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt());
					if (LoginController.getInstance().containsAccount(account_name)) {
						GeneralThreadPool.getInstance().execute(new Runnable() {
							@Override
							public void run() {
								try {
									/**
									 * TODO 인터서버 지연 수정 TODO 강제 지연 테스트(10->1) 10번 시연 시도를하고 실패했을때 팅기지 않고, 마을로 롤백시킨다.
									 * 테스트 GameClient.java 에서 지연 검색후 3000[3초]로 고정 그 이후 카운트 10->1로 변경 Thread.sleep 1000L을 2000L로 수정 
									 * TODO 10->1로 수정함 이유는 10까지 가기전에 팅긴다.
									 **/
									for (int i = 0; i < 1; ++i) {
										Thread.sleep(1000L);
										System.out.println("[跨伺服器連線延遲，重試處理：帳號 (" + account_name + ") 嘗試次數：" + (i + 1) + "次]");
										if (!LoginController.getInstance().containsAccount(account_name)) {
											LOCAL_TEMP_MAP.put(object_id, restart_save_old_client);
											clnt.getStatus().process(clnt, datas);
											return;
										}
									}
									if (reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK || reserved_no == SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK_RESTART) {
										SC_CONNECT_HIBREEDSERVER_NOTI_PACKET noti = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstanceNoti(clnt, account_name, account.get_Password(), object_id,
												character_name, x, y, mid, x, y, mid, reserved_no, reserved_no);
										clnt.sendPacket(noti, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.toInt());
									} else {
										failureMembers.put(object_id, object_id);
										SC_CONNECT_HIBREEDSERVER_NOTI_PACKET noti = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstanceNoti(clnt, account_name, account.get_Password(), object_id,
												character_name, backX, backY, backMapId, x, y, mid, backReservedNo, reserved_no);
										clnt.sendPacket(noti, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.toInt());
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						return this;
					}

					account.setValid(true);
					try {
						LoginController.getInstance().login(clnt, account);
						Account.updateLastActive(account, clnt.getIp());
						clnt.setAccount(account);
						C_LoginToServer.doEnterWorld(character_name, clnt, false, x, y, mid);
						if (failureMembers.containsKey(object_id)) {
							GeneralThreadPool.getInstance().schedule(new Runnable() {

	@Override
	public void run() {
		failureMembers.remove(object_id);
		L1PcInstance pc = L1World.getInstance().findpc(character_name);
		if (pc != null) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\f3瞬間同步用户数過多，移動失敗。\r\n\\f3請稍後再試。"));
			pc.sendPackets("\\f3瞬間同步用户数過多，移動失敗。請稍後再試。");
			System.out.println("[跨伺服器連線延遲，重試處理提示（確認用）：瞬間同步用户数過多，移動失敗]");
		}
	}},2000L);}}catch(

	GameServerFullException e)
	{
		String s = String.format("跨伺服器：超過最大連線人數：(%s) 已中斷登入。", clnt.getIp());
		System.out.println(s);
		clnt.close();
	}catch(
	AccountAlreadyLoginException e)
	{
		String s = String.format("跨伺服器：相同 ID 登入：(%s) 已強制中斷。", clnt.getIp());
		System.out.println(s);
		clnt.close();
	}catch(
	Exception e)
	{
		e.printStackTrace();
		String s = String.format("跨伺服器：非正常登入錯誤。 account=%s host=%s", account_name, clnt.getHostname());
		System.out.println(s);
		clnt.close();
	}}else{System.out.println(String.format("跨伺服器：錯誤使用移位伺服器：%s",Config.Login.UseShiftServer));}}}catch(
	AccountAlreadyLoginException e)
	{
		System.out.println(String.format("跨伺服器：【帳號】%s 【%s】【IP】%s 戰役中角色重複登入", clnt.getAccountName(),
				MJNSHandler.getLocalTime(), clnt.getIp()));
	}catch(Exception e)
	{
		e.printStackTrace();
	}return this;

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_HIBREED_AUTH_REQ_PACKET();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

}

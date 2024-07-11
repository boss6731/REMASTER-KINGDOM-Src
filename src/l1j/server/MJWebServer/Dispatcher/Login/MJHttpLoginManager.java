package l1j.server.MJWebServer.Dispatcher.Login;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginInfo;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Builder.MJServerPacketBuilder;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.ButtonType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.IconType;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;

public class MJHttpLoginManager {
	private static MJHttpLoginManager m_instance;
	public static MJHttpLoginManager getInstance(){
		if(m_instance == null)
			m_instance = new MJHttpLoginManager();
		return m_instance;
	}

	private ConcurrentHashMap<String, MJHttpLoginInfo> m_login_groups;
	private ConcurrentHashMap<String, MJHttpLoginInfo> m_address_group;
	
	private MJHttpLoginManager(){
		m_login_groups = new ConcurrentHashMap<String, MJHttpLoginInfo>();
		m_address_group = new ConcurrentHashMap<String, MJHttpLoginInfo>();
	}
	
	public MJHttpLoginInfo get_login_info(String auth_token) {
		return m_login_groups.get(auth_token);
	}
	
	public MJHttpLoginInfo remove_and_get(String auth_token) {
		return m_login_groups.remove(auth_token);
	}

	public MJHttpLoginInfo get_login_info_from_address(String address) {
		return m_address_group.get(address);
	}
	
	public MJHttpLoginInfo remove_and_get_from_address(String address) {
		return m_address_group.remove(address);
		
	}
	
	public void put_login_info(final MJHttpLoginInfo lInfo) {
		m_login_groups.put(lInfo.get_auth_token(), lInfo);
		m_address_group.put(lInfo.get_client_ip(), lInfo);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
//				System.out.println("remove");
				remove_and_get(lInfo.get_auth_token());
				remove_and_get_from_address(lInfo.get_client_ip());
			}
		}, 1000 * 60 * 10);//10분 뒤 세션 종료
	}

	private static final String SESSION_KEY = "saddasd324";
	public static String check_hmac(String hdd_id, String mac_address, String path) {
		try {
			String message = String.format("%s.%s@%s",
					hdd_id,
					mac_address,
					path
                    );
			
			Mac sha256 = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(SESSION_KEY.getBytes(MJEncoding.UTF8), "HmacSHA256");
			sha256.init(secret_key);
			return Base64.getEncoder().encodeToString(sha256.doFinal(message.getBytes(MJEncoding.UTF8)));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return MJString.EmptyString;
	}
	
	public boolean onAuthLogin(final GameClient clnt, final String authToken) throws IOException {
		final MJHttpLoginInfo lInfo = get_login_info(authToken);
		if(lInfo == null) {
			return false;
		}
		
		clnt.set_login_info(lInfo);
		// 우선 로그인은 기존의 C_LOGIN 처리로 넣어둠. 나중에 적절히 빼서 메이킹할것
		MJServerPacketBuilder builder = new MJServerPacketBuilder(128);
		builder.addC(Opcodes.C_LOGIN);
		builder.addS(lInfo.get_account());
		builder.addS(lInfo.get_password());
		clnt.getStatus().process(clnt, builder.toArray());
		builder.close();
		return true;
	}
	
	/**public void onAuthLogin(final GameClient clnt, final String authToken) throws IOException {
		final MJHttpLoginInfo lInfo = get_login_info(authToken);
		if(lInfo == null) {
			SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
			box.set_button_type(ButtonType.MB_OK);
			box.set_icon_type(IconType.MB_ICONHAND);
			box.set_message("세션이 만료되어 로그인 정보를 받아올 수 없습니다.(접속기를 재실행 바랍니다)");
			box.set_title(Config.Message.GameServerName);
			box.set_message_id(0);
			clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
			clnt.close(5000L);
			return;
		}
		
		clnt.set_login_info(lInfo);
		// 우선 로그인은 기존의 C_LOGIN 처리로 넣어둠. 나중에 적절히 빼서 메이킹할것
		MJServerPacketBuilder builder = new MJServerPacketBuilder(128);
		builder.addC(Opcodes.C_LOGIN);
		builder.addS(lInfo.get_account());
		builder.addS(lInfo.get_password());
		clnt.getStatus().process(clnt, builder.toArray());
		builder.close();
	}**/
	
	public void onRecycleLogin(final GameClient clnt) throws Exception {
		MJHttpLoginInfo lInfo = clnt.get_login_info();
		if(lInfo == null) {
			clnt.close();
			return;
		}
		
		MJServerPacketBuilder builder = new MJServerPacketBuilder(4);
		clnt.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
		builder.addC(Opcodes.C_LOGOUT);
		clnt.getStatus().process(clnt, builder.toArray());
		builder.close();
		
		builder = new MJServerPacketBuilder(128);
		builder.addC(Opcodes.C_LOGIN);
		builder.addS(lInfo.get_account());
		builder.addS(lInfo.get_password());

		clnt.getStatus().process(clnt, builder.toArray());
		builder.close();
	}
}


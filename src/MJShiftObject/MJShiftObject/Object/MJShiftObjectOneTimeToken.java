package MJShiftObject.Object;

import MJShiftObject.MJEShiftObjectType;
import l1j.server.MJTemplate.Aes.MJAes;

public class MJShiftObjectOneTimeToken {
	private String server_identity;
	private boolean is_returner;
	private boolean is_reconnected;
	private MJShiftObject shift_object;
	private String home_server_identity;

	/**
	 * 構造函數，用於初始化 MJShiftObjectOneTimeToken 實例。
	 *
	 * @param server_identity 伺服器身份
	 * @param is_returner 是否為返回者
	 * @param shift_object 轉移對象
	 * @param home_server_identity 主伺服器身份
	 * @param is_reconnected 是否重新連接
	 */
	public MJShiftObjectOneTimeToken(String server_identity, boolean is_returner, MJShiftObject shift_object, String home_server_identity, boolean is_reconnected) {
		this.server_identity = server_identity;
		this.is_returner = is_returner;
		this.shift_object = shift_object;
		this.home_server_identity = home_server_identity;
		this.is_reconnected = is_reconnected;
	}

// Getter 和 Setter 方法

	public String getServerIdentity() {
		return server_identity;
	}

	public void setServerIdentity(String server_identity) {
		this.server_identity = server_identity;
	}

	public boolean isReturner() {
		return is_returner;
	}

	public void setReturner(boolean is_returner) {
		this.is_returner = is_returner;
	}

	public boolean isReconnected() {
		return is_reconnected;
	}

	public void setReconnected(boolean is_reconnected) {
		this.is_reconnected = is_reconnected;
	}

	public MJShiftObject getShiftObject() {
		return shift_object;
	}

	public void setShiftObject(MJShiftObject shift_object) {
		this.shift_object = shift_object;
	}

	public String getHomeServerIdentity() {
		return home_server_identity;
	}

	public void setHomeServerIdentity(String home_server_identity) {
		this.home_server_identity = home_server_identity;
	}

	/**
	 * 將當前對象轉換為一次性令牌字符串。
	 *
	 * @return 加密的一次性令牌字符串
	 * @throws Exception 如果加密過程中出現錯誤
	 */
	public String to_onetime_token() throws Exception {
		MJAes aes = new MJAes("[MJSoftAesCoder]");
		String s = this.server_identity + "|"
				+ this.is_returner + "|"
				+ this.shift_object.getSourceId() + "|"
				+ this.shift_object.getDestinationId() + "|"
				+ this.shift_object.getShiftType().toName() + "|"
				+ this.shift_object.getSourceCharacterName() + "|"
				+ this.shift_object.getSourceAccountName() + "|"
				+ this.shift_object.getDestinationCharacterName() + "|"
				+ this.shift_object.getDestinationAccountName() + "|"
				+ this.shift_object.getConvertParameters() + "|"
				+ this.home_server_identity + "|"
				+ this.is_reconnected;
		s = aes.encrypt(s);
		return s;
	}
}



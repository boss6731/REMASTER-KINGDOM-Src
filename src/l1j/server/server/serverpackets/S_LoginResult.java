package l1j.server.server.serverpackets;

import java.util.HashMap;

import l1j.server.server.Opcodes;

public class S_LoginResult extends ServerBasePacket {
	public static final String S_LOGIN_RESULT = "[S] S_LoginResult";

	public static final int BANNED_REASON_HACK = 95;  //  95-您的帳戶因使用非法程式而被限制遊戲服務。
	public static final int BANNED_REASON_NOMANNER = 62; //  62-您的帳戶因有害公共安寧、秩序或善良風俗的行為而被限制遊戲服務。
	public static final int BANNED_REASON_COMMERCE = 87; //  87-因您進行商業目的的廣告行為或嘗試現金/其他遊戲的交易，被限制遊戲服務。
	public static final HashMap<Integer, Integer> banServerCodes;
	static{
		banServerCodes = new HashMap<Integer, Integer>(8);
		banServerCodes.put(1, BANNED_REASON_HACK);
		banServerCodes.put(2, BANNED_REASON_NOMANNER);
		banServerCodes.put(3, BANNED_REASON_COMMERCE);
	}
	
	public static final int REASON_LOGIN_OK = 0x00; // 0x33
	public static final int REASON_ACCOUNT_IN_USE = 0x16;
	public static final int REASON_ACCOUNT_ALREADY_EXISTS = 100;
	public static final int REASON_ACCESS_FAILED = 0x08;
	public static final int REASON_USER_OR_PASS_WRONG = 0x08;
	public static final int REASON_BUG_WRONG = 0x39;
	public static final int REASON_WRONG_ACCOUNT = 0x09;
	public static final int REASON_WRONG_PASSWORD = 0x0A;
	public static final int REASON_BENNED = 62;
	public static final int REASON_MAX_USER = 217;
	public static final int REASON_SUCCESS = 104;

	// 62-您的帳戶從事有害公共安寧、秩序或善良風俗的行為....
	// 63-您的帳戶從事有害公共安寧、秩序或善良風俗的行為....
	// 64-您的帳戶從事有害公共安寧、秩序或善良風俗的行為....
	// 65-您的帳戶確認使用了不當語言....
	// 66-客戶不得以非服務目的使用公司提供的服務，包括下列各項行為。
	// 67-根據客戶本人的請求，該帳戶已根據我們的條款和條件受到限制。
	// 68-您的帳戶因信用卡盜用而根據我們的服務條款受到限制。
	// 69-您的帳戶因手機盜用而根據我們的服務條款受到限制。
	// 71-因註冊在您的身份證號碼下的多個帳戶違反了使用條款和運營政策，已被統一限制。
	// 72-您的帳戶因盜用他人名義而根據我們的使用條款受到限制。
	// 73-您的帳戶因一年內未登錄一次，根據客戶條款將被刪除。
	// 75-因指定的時間已過，遊戲已結束。
	// 79-所選伺服器是PC房專用伺服器。只能在天堂加盟PC房中連接。
	// 86-您的帳戶需要認證密封。
	// 87-確認您進行了商業目的的廣告行為或嘗試現金/其他遊戲的交易....
	// 95-您的帳戶因使用非法程式而被限制使用遊戲服務。
	// 100-被封鎖的IP。
	// 115-確認您的帳戶濫用了與NC Coin相關的漏洞，已被限制使用。
	// 117-您的帳戶所有權限已被限制。
	// 126-由於推測您在遊戲中使用了異常方法進行遊戲，您的帳戶被暫時限制。
	// 127-目前無法在此伺服器上創建角色。

	public S_LoginResult(int reason) {
		buildPacket(reason);
	}

	private void buildPacket(int reason) {
		writeC(Opcodes.S_LOGIN_CHECK);
		writeC(reason);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}

	public static S_LoginResult newLoginAccept() {
		S_LoginResult result = new S_LoginResult();
		result.writeC(Opcodes.S_LOGIN_CHECK);
		result.writeC(0x33);
		result.writeD(0x00);
		result.writeD(0x1b00);
		result.writeD(0x00);
		result.writeD(0xFFFFFFFF);
		result.writeD(0xC8);
		result.writeD(0x00);		
		result.writeD(0x00);		
		result.writeD(0x00);		
		result.writeD(0x00);		
		return result;
	}
	
	private S_LoginResult() {
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_LOGIN_RESULT;
	}
}

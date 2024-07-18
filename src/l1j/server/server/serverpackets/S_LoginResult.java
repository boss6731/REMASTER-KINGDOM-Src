package l1j.server.server.serverpackets;

import java.util.HashMap;

import l1j.server.server.Opcodes;

public class S_LoginResult extends ServerBasePacket {
	public static final String S_LOGIN_RESULT = "[S] S_LoginResult";

	public static final int BANNED_REASON_HACK = 95; // 95-您的帳號因使用非法程式而被限制使用遊戲服務。
	public static final int BANNED_REASON_NOMANNER = 62; // 62-您的帳號因進行影響公共秩序或風紀的不當行為而被限制使用。
	public static final int BANNED_REASON_COMMERCE = 87; // 87-您的帳號因進行商業廣告行為或現金/其他遊戲交易嘗試而被限制使用。
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

//  06-已存在或無法使用的角色名稱。
//  07-已有同名的帳戶。請輸入其他名稱。
//  10-密碼錯誤。密碼應包括……
//  11-電子郵件地址有誤。
//  12-電子郵件地址有誤。
//  13-身分證號碼錯誤。
//  17-國家名稱錯誤。
//  31-連接失敗。請於3分鐘後再試。
//  38-使用虛擬IP等方式多重連接時，可能會限制連接。
//  39-該伺服器無法連接或該國家禁止連接此伺服器。
//  40-當前的密碼已無法使用。請立即更改密碼。
//  43-您的帳戶因帳戶盜用申報而被凍結。
//  52-此角色已被禁用。請通過電子郵件聯繫該伺服器的遊戲管理員。
//  55-由於帳戶盜用相關問題，您的帳戶使用受限。
//  57-您的帳戶因確認到帳戶交易行為，遊戲服務使用受限。
//  58-因確認到現金/伺服器/其他遊戲的交易內容，您的遊戲服務已被停止。
//  59-您的帳戶因虛假帳戶盜用申報，根據本公司的帳戶盜用申報處理原則，使用受限。
//  60-因確認到現金/伺服器/其他遊戲的交易內容，您的遊戲服務已被停止。
//  61-您的帳戶因利用遊戲內系統漏洞或錯誤……
//  62-您的帳戶因從事妨害公共安全、秩序或公序良俗的行為……
//  63-您的帳戶因從事妨害公共安全、秩序或公序良俗的行為……
//  64-您的帳戶因從事妨害公共安全、秩序或公序良俗的行為……
//  65-您的帳戶因使用遊戲內不正當言語而被確認……
//  66-用戶不得將公司提供的服務用於服務目的以外的行為，包括但不限於以下各項。
//  67-該帳戶根據用戶本人的要求，依本公司條款及限制使用。
//  68-您的帳戶因信用卡盜用依本公司的服務使用條款限制使用。
//  69-您的帳戶因手機盜用依本公司的服務使用條款限制使用。
//  71-因確認到多個用戶的身分證號碼違反使用條款及運營政策，您的帳戶被一併限制使用。
//  72-您的帳戶因未經他人同意盜用名義使用本公司的服務，依使用條款限制使用。
//  73-您的帳戶因一年內未曾登入，依用戶條款即將被刪除。
//  75-由於超過指定時間，遊戲已結束。
//  79-您選擇的伺服器是PC房專用伺服器。只能從連鎖PC房連接。
//  86-您的帳戶需要進行封印認證。
//  87-因確認到您試圖進行商業目的的廣告行為或現金/其他遊戲的交易……
//  95-您的帳戶因使用非法程式，遊戲服務使用受限。
// 100-此IP已被封鎖。
// 115-確認到您利用NC Coin相關漏洞，帳戶使用受限。
// 117-您的帳戶已被限制所有權限。
// 126-因確認到您以非正常方式進行遊戲，您的帳戶被臨時限制。
// 127-目前此伺服器無法創建角色。


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



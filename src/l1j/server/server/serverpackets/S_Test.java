package l1j.server.server.serverpackets;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class S_Test extends ServerBasePacket {

	public S_Test(int OpCodeID, int TestLevel, L1PcInstance Player) {
		_opcodeid = OpCodeID;
		_testlevel = TestLevel;
		_gm = Player;
	}
	
	public S_Test(byte[] data) {
		writeByte(data);
	}

	@Override
	public byte[] getContent() {
		writeC(_opcode[_testlevel][_opcodeid]);
		// ■■■■ 由 jump 鯖魚等調查的測試用數據 ■■■■
		int objid = 0;
		Object[] petList = _gm.getPetList().values().toArray();
		L1SummonInstance summon = null;
		for (Object pet : petList) {
			if (pet instanceof L1SummonInstance) {
				summon = (L1SummonInstance) pet;
				objid = summon.getId();
				break;
			}
		}
		writeD(objid);
		writeC(25);
		// ■■■■■■■■■■■■■■■■■■■■■■■■
		return getBytes();
	}

	public String getInfo() {
		StringBuilder info = new StringBuilder();
		info.append(".opc 請輸入操作代碼 ID。\n");
		info.append("[版本] ").append(_version);
		info.append(" [等級] ").append(_testlevel);
		info.append(" [ID範圍] 0 - ").append(_opcode[_testlevel].length - 1).append("\n");
		info.append("[之前的行動] ").append(_action).append("\n");
		info.append("[預期的狀態] ").append(_status).append("\n");
		return info.toString();
	}

	public String getCode() {
		StringBuilder info = new StringBuilder();
		info.append("[OpCodeId] ").append(_opcodeid).append(" [OpCode] ")
				.append(_opcode[_testlevel][_opcodeid]);
		return info.toString();
	}

	public String getCodeList() {
		StringBuilder info = new StringBuilder();
		info.append(".opcid 請輸入操作代碼 ID。\n");
		info.append("Lv").append(_testlevel).append(
				"　0　　1　　2　　3　　4　　5　　6　　7　　8　　9\n");
		int t = 0;
		int tc = 10;
		for (int i = 0; i < _opcode[_testlevel].length; i++) {
			if (tc == 10) {
				if (t > 0) {
					info.append("\n");
				}
				info.append(padt(t));
				t++;
				tc = 0;
			}
			info.append(pad(_opcode[_testlevel][i]));
			tc++;
		}
		return info.toString();
	}

	private String pad(int i) {
		if (i < 10) {
			return (new StringBuilder()).append(" 00").append(i).toString();
		} else if (i < 100) {
			return (new StringBuilder()).append(" 0").append(i).toString();
		}
		return (new StringBuilder()).append(" ").append(i).toString();
	}

	private String padt(int i) {
		if (i < 10) {
			return (new StringBuilder()).append("0").append(i).append(" ")
					.toString();
		}
		return (new StringBuilder()).append(i).append(" ").toString();
	}
	@Override
	public String getType() {
		return "[S]  S_Test";
	}

	private int _opcodeid;
	private int _testlevel;
	private L1PcInstance _gm;

	// ■■■■ 為了消除報告中的灰色和嘈雜(無效內容)的版本 ■■■■
	private final String _version = "S_HPMeter1.0";

	// ■■■■ 放置於之前的行動 ■■■■
	private final String _action = "只放置一隻召喚獸";

	// ■■■■ 當能夠發送正確的操作代碼時預期的狀態 ■■■■
	private final String _status = "召喚獸的HP計量器會變動到25%";

	// 已經解釋完的操作代碼設定為下層
	// 上層(Level0) 目前不使用的代碼，用於137鯖魚 .opc .opcid
	// 中層(Level1) 目前由137鯖魚定義，但尚未確認是否真的是用於此代碼，
	// 並且不存在於 "etc/操作代碼參考資料.txt" 中或未被知曉的代碼，
	// 以及用于 130~139 區域代碼（因為認為0~129間的可能性較大） .opc2 .opcid2
	// 下層(Level2) 目前由137鯖魚使用且正在運行的代碼（以防萬一的準備） .opc3 .opcid3
	// 若上層無法使用則使用中層，若中層也無法使用則使用下層。
	int[][] _opcode = {
			{ 2, 3, 4, 6, 8, 16, 17, 18, 19, 22, 24, 27, 31, 33, 34, 35, 37,
					38, 40, 43, 47, 48, 49, 52, 54, 62, 65, 70, 72, 73, 74, 75,
					76, 78, 80, 83, 84, 86, 87, 88, 89, 90, 91, 92, 93, 95, 98,
					99, 101, 102, 104, 105, 107, 110, 112, 113, 114, 116, 117,
					118, 119, 120, 121, 122, 124, 127, 128 },

			{ 0, 5, 9, 13,

			42, 44, 50, 53, 55, 58, 60, 64, 77,

			111, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139 },

			{ 1, 7, 10, 11, 12, 15, 20, 21, 23, 25, 26, 28, 29, 30, 32, 36, 39,
					41, 45, 46, 51, 56, 57, 59, 61, 63, 66, 67, 68, 69, 71, 79,
					81, 82, 85, 94, 96, 97, 100, 103, 106, 108, 109, 115, 123,
					125, 126 } };
}

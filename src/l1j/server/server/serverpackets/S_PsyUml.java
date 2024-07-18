package l1j.server.server.serverpackets;

import l1j.server.MJTemplate.MJString;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.PowerBall.PowerBallInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.Controller.BugRaceController;

public class S_PsyUml extends ServerBasePacket {
	public static S_PsyUml bugRaceUml(boolean is_viewed_gm) {
		PsyUmlArgs args = new PsyUmlArgs();
		args.pageTitle = "樂蒂";  // 樂蒂
		args.pageDescription = "來吧~來吧，這次運動會上魔法人偶們的狀態是這樣的...";  // 來吧~來吧，這次運動會上魔法人偶們的狀態是這樣的...

		if (is_viewed_gm) {  // 如果是管理員查看
			int index = 0;
			args.pageArguments[index++] = "名稱  ";  // 名字
			args.pageArguments[index++] = "狀態  ";  // 狀態
			args.pageArguments[index++] = "勝率  ";  // 勝率
			for (int i = 0; i < 5; ++i) {
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._littleBugBear[i].getName());
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._bugCondition[i]);
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._winRate[i]);
			}
		} else {  // 如果不是管理員查看
			int index = 0;
			args.pageArguments[index++] = "名稱  ";  // 名字
			args.pageArguments[index++] = "狀態  ";  // 狀態
			args.pageArguments[index++] = "勝率  ";  // 勝率
			for (int i = 0; i < 5; ++i) {
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._littleBugBear[i].getName());
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._bugCondition[i]);
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._winRate[i]);
//                args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._winViewRate[i]); // 觀察值假
			}

		}
		return new S_PsyUml(args);
	}


	public static S_PsyUml powerBallUml() {
		PsyUmlArgs args = new PsyUmlArgs();
		args.pageTitle = "朱諾";  // 朱諾

		PowerBallInfo pInfo = PowerBallController.getinfo();
		if(pInfo == null) {
			args.pageDescription = "當前強力球遊戲被遊戲管理員暫停。";  // 當前強力球遊戲被遊戲管理員暫停。
		} else {
			args.pageDescription = String.format("[%s] 第%d次 [普通球]", pInfo.getDate(), pInfo.getNum());  // [%s] 第%d次 [普通球]

			args.pageArguments[3] = "(數字)組合號碼  ";  // (數字)組合號碼
			args.pageArguments[4] = String.format("%d[%s]  ", pInfo.getPlusNum(), pInfo.getoddEven());  // %d[%s]
			args.pageArguments[5] = String.format("[%s]  ", pInfo.getUnderOver());  // [%s]
		}
		return new S_PsyUml(args);
	}
	
	
	private static final int PageArgumentsLength = 18;
	public S_PsyUml(PsyUmlArgs args) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(args.objectId);
		writeS("psy");
		writeC(0x00);
		writeH(PageArgumentsLength + 2);
		safeWriteS(args.pageTitle);
		safeWriteS(args.pageDescription);
		for(String s : args.pageArguments) {
			safeWriteS(s);
		}
		writeH(0x00);
	}
	
	private void safeWriteS(String s) {
		writeS(MJString.isNullOrEmpty(s) ? MJString.EmptyString : s);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public static class PsyUmlArgs {
		// 物件 ID
		public int objectId;

		// 頁面名稱（NPC 名稱）
		public String pageTitle;

		// 頁面描述文字
		public String pageDescription;

		// 0~2 白色，3~5 紅色，6~8 綠色
		public String[] pageArguments;

		public PsyUmlArgs() {
			pageArguments = new String[PageArgumentsLength];
		}
	}



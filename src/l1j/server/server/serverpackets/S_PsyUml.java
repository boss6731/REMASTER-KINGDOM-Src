package l1j.server.server.serverpackets;

import l1j.server.MJTemplate.MJString;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.PowerBall.PowerBallInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.Controller.BugRaceController;

public class S_PsyUml extends ServerBasePacket {
	public static S_PsyUml bugRaceUml(boolean is_viewed_gm) {
		PsyUmlArgs args = new PsyUmlArgs();
		args.pageTitle = "蕾蒂";
		args.pageDescription = "來~ 來，這次運動會將要參加的魔法人偶們的狀態是這樣的...";

		if (is_viewed_gm) {
			int index = 0;
			args.pageArguments[index++] = "名稱  ";
			args.pageArguments[index++] = "狀態  ";
			args.pageArguments[index++] = "勝率  ";
			for (int i = 0; i < 5; ++i) {
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._littleBugBear[i].getName());
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._bugCondition[i]);
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._winRate[i]);
			}
		} else {
			int index = 0;
			args.pageArguments[index++] = "名稱  ";
			args.pageArguments[index++] = "狀態  ";
			args.pageArguments[index++] = "勝率  ";
			for (int i = 0; i < 5; ++i) {
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._littleBugBear[i].getName());
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._bugCondition[i]);
				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._winRate[i]);
//				args.pageArguments[index++] = String.format("%s ", BugRaceController.getInstance()._winViewRate[i]);// 檢視器值 假的
			}
		}
		return new S_PsyUml(args);
	}
	
	
	public static S_PsyUml powerBallUml() {
		PsyUmlArgs args = new PsyUmlArgs();
		args.pageTitle = "朱諾";

		PowerBallInfo pInfo = PowerBallController.getinfo();
		if(pInfo == null) {
			args.pageDescription = "目前的威力球遊戲已被遊戲主宰停止。";
		} else {
			args.pageDescription = String.format("[%s] %d回 [一般球]", pInfo.getDate(), pInfo.getNum());

		args.pageArguments[3] = "(數字)組合編號  ";
			args.pageArguments[4] = String.format("%d[%s]  ", pInfo.getPlusNum(), pInfo.getoddEven());
			args.pageArguments[5] = String.format("[%s]  ", pInfo.getUnderOver());
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
	
	public static class PsyUmlArgs{
		// 物件 ID
		public int objectId;

		// 頁面名稱 (NPC 名稱)
		public String pageTitle;

		// 頁面描述
		public String pageDescription;

		// 0~2 白色, 3~5 紅色, 6~8 綠色
		public String[] pageArguments;
		public PsyUmlArgs() {
			pageArguments = new String[PageArgumentsLength];
		}
	}
}
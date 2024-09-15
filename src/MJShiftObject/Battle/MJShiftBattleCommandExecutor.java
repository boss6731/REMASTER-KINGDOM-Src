package MJShiftObject.Battle;

import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.DomTower.MJDomTowerPlayManager;
import MJShiftObject.Battle.ForgottenIsland.MJFIslandPlayManager;
import MJShiftObject.Battle.Thebe.MJThebePlayManager;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;

public class MJShiftBattleCommandExecutor implements MJCommand{
	public MJShiftBattleCommandExecutor(){}

	@Override
	public void execute(MJCommandArgs args) {
		try{
			switch(args.nextInt()){
				case 1:
					MJShiftObjectManager.getInstance().do_reload_whitelist();
					args.notify("白名單表已重新加載。");
					break;
				case 2:
					MJShiftBattleArgs.load();
					args.notify("mj_shiftbattle.properties 已重新加載。");
					break;
			case 3:
				MJShiftBattleMessage.do_test(args.getOwner(), MJShiftBattleMessage.DBNAME_THEBES, true);
				break;
			case 4:
				MJShiftBattleMessage.do_test(args.getOwner(), MJShiftBattleMessage.DBNAME_DOM_TOWER, true);
				break;				
			case 5:
				MJShiftBattleMessage.do_test(args.getOwner(), MJShiftBattleMessage.DBNAME_FOR_ISLAND, true);
				break;
				case 6:
				{
					try {
						int next_id = args.nextInt();
						MJThebePlayManager.REVISION_TEAM_ID = next_id;
						args.notify(String.format("已將特貝隊調整ID修改為 %d。", MJThebePlayManager.REVISION_TEAM_ID));
					} catch (Exception e) {
						args.notify(".對抗戰 6 [調整 ID]");
						args.notify(String.format("當前 %d", MJThebePlayManager.REVISION_TEAM_ID));
					}
				}
				break;
				case 7:
				{
					try {
						int next_id = args.nextInt();
						MJDomTowerPlayManager.REVISION_TEAM_ID = next_id;
						args.notify(String.format("已將支配之塔隊調整ID修改為 %d。", MJDomTowerPlayManager.REVISION_TEAM_ID));
					} catch (Exception e) {
						args.notify(".對抗戰 7 [調整 ID]");
						args.notify(String.format("當前 %d", MJDomTowerPlayManager.REVISION_TEAM_ID));
					}
				}
				break;
				case 8:
				{
					try {
						int next_id = args.nextInt();
						MJFIslandPlayManager.REVISION_TEAM_ID = next_id;
						args.notify(String.format("已將遺忘之島隊調整ID修改為 %d。", MJFIslandPlayManager.REVISION_TEAM_ID));
					} catch (Exception e) {
						args.notify(".對抗戰 8 [調整 ID]");
						args.notify(String.format("當前 %d", MJFIslandPlayManager.REVISION_TEAM_ID));
					}
				}
				break;
				default:
					throw new Exception();
			}
		}catch(Exception e){
			args.notify(".對抗戰 [1.重新加載白名單][2.重新加載配置]");
			args.notify(".對抗戰 [3.特貝拉斯訊息測試]");
			args.notify(".對抗戰 [4.支配之塔訊息測試]");
			args.notify(".對抗戰 [5.遺忘之島訊息測試]");
			args.notify(".對抗戰 [6.特貝拉斯隊ID調整]");
			args.notify(".對抗戰 [7.支配之塔隊ID調整]");
			args.notify(".對抗戰 [8.遺忘之島隊ID調整]");
		}finally{
			args.dispose();
		}
	}
}

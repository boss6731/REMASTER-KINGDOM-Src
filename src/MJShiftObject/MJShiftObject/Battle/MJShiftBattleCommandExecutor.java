package MJShiftObject.Battle;

import MJShiftObject.Battle.DomTower.MJDomTowerPlayManager;
import MJShiftObject.Battle.ForgottenIsland.MJFIslandPlayManager;
import MJShiftObject.Battle.Thebe.MJThebePlayManager;
import MJShiftObject.MJShiftObjectManager;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;


public class MJShiftBattleCommandExecutor
		implements MJCommand {
	public void execute(MJCommandArgs args) {
		try {
			switch (args.nextInt()) {
				case 1:
					MJShiftObjectManager.getInstance().do_reload_whitelist();
					args.notify("白名單表已重新載入。");
					break;
				case 2:
					MJShiftBattleArgs.load();
					args.notify("重新載入 mj_shiftbattle.properties。");
					break;
				case 3:
					MJShiftBattleMessage.do_test(args.getOwner(), "server_battle_message_thebes", true);
					break;
				case 4:
					MJShiftBattleMessage.do_test(args.getOwner(), "server_battle_message_domtower", true);
					break;
				case 5:
					MJShiftBattleMessage.do_test(args.getOwner(), "server_battle_message_forisland", true);
					break;

				case 6:
					try {
						int next_id = args.nextInt();
						MJThebePlayManager.REVISION_TEAM_ID = next_id;
						args.notify(String.format("hebe 團隊修正 ID 已更改為 %d。", new Object[]{Integer.valueOf(MJThebePlayManager.REVISION_TEAM_ID)}));
					} catch (Exception e) {
						args.notify(".battlesteam 6 [修改團隊名稱 ID]");
						args.notify(String.format("目前%d", new Object[]{Integer.valueOf(MJThebePlayManager.REVISION_TEAM_ID)}));
					}
					break;


				case 7:
					try {
						int next_id = args.nextInt();
						MJDomTowerPlayManager.REVISION_TEAM_ID = next_id;
						args.notify(String.format("統治之塔團隊修正 ID 已更改為 %d.", new Object[]{Integer.valueOf(MJDomTowerPlayManager.REVISION_TEAM_ID)}));
					} catch (Exception e) {
						args.notify(".battlesteam 7 [修改團隊名稱 ID]");
						args.notify(String.format("目前%d", new Object[]{Integer.valueOf(MJDomTowerPlayManager.REVISION_TEAM_ID)}));
					}
					break;


				case 8:
					try {
						int next_id = args.nextInt();
						MJFIslandPlayManager.REVISION_TEAM_ID = next_id;
						args.notify(String.format("忘記的團隊修正 ID 已更改為 %d。", new Object[]{Integer.valueOf(MJFIslandPlayManager.REVISION_TEAM_ID)}));
					} catch (Exception e) {
						args.notify(".battlesteam 8 [忘記的團隊修正 ID]");
						args.notify(String.format("目前%d", new Object[]{Integer.valueOf(MJFIslandPlayManager.REVISION_TEAM_ID)}));
					}
					break;

				default:
					throw new Exception();
			}
		} catch (Exception e) {
			args.notify(".battle [1.白名單重新加載][2.Conpick重新加載]");
			args.notify(".battlepvp 【3.Teveras訊息測試】");
			args.notify(".battlepvp 【4.主宰之塔留言測試】");
			args.notify(".battlepvp 【5.遺忘島消息測試】");
			args.notify(".battle 【6.Teveras戰隊ID修正】");
			args.notify(".battle 【7.統治之塔隊伍ID修正】");
			args.notify(".battle 【8.遺忘島隊伍ID修正】");
		} finally {
			args.dispose();
		}
	}
}



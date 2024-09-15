package l1j.server.Beginner.Model;

import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandComposite;
import l1j.server.MJTemplate.Command.MJCommandEx;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;

import java.util.Collection;

class MJBeginnerCommandModel {

	// 構造方法
	MJBeginnerCommandModel() {
	}

	// 創建命令的方法
	MJCommandEx createCommand() {
		return new MJCommandComposite("任務")
				.append(new SelectCompleteCommand())
				.append(new RevealedCompleteCommand());
	}

	// 內部類 SelectCompleteCommand 實現 MJCommandEx 接口
	private class SelectCompleteCommand implements MJCommandEx {

		//@override
		public boolean execute(MJCommandArgs args) {
			try {
				// 獲取參數
				String target = args.nextString();
				int questId = args.nextInt();

				// 判斷是否選擇所有玩家
				if (target.equalsIgnoreCase("*")) {
					onSelectQuestComplete(args, L1World.getInstance().getAllPlayers(), questId);
				} else {
					L1PcInstance pc = L1World.getInstance().getPlayer(target);
					if (pc == null) {
						args.notify(String.format("無法找到玩家 %s。", target));
					} else {
						onSelectQuestComplete(args, pc, questId);
					}
				}
				return true;
			} catch (Exception e) {
				// 處理異常，並提供幫助信息
			}
			args.notify(".任務 選擇完成 [玩家名稱 or *(所有人)] [任務ID]");
			return true;
		}

		//@override
		public String commandKey() {
			return "選擇完成";
		}
	}


	private class RevealedCompleteCommand implements MJCommandEx {
		//@override
		public boolean execute(MJCommandArgs args) {
			try {
				String target = args.nextString();
				if (target.equalsIgnoreCase("*")) {
					onRevealedQuestComplete(args, L1World.getInstance().getAllPlayers());
				} else {
					L1PcInstance pc = L1World.getInstance().getPlayer(target);
					if (pc == null) {
						args.notify(String.format("無法找到玩家 %s。", target));
					} else {
						onRevealedQuestComplete(args, pc);
					}
				}
				return true;
			} catch (Exception e) {
				// 處理異常，並提供幫助信息
			}
			args.notify(".任務 啟動完成 [玩家名稱 or *(所有人)]");
			return true;
		}

		//@override
		public String commandKey() {
			return "啟動完成";
		}
	}

	private void onSelectQuestComplete(MJCommandArgs args, L1PcInstance target, int questId) {
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
		if (user == null) {
			args.notify(String.format("無法找到玩家 %s 的任務信息。", target.getName()));
			return;
		}
		if (user.onRevealedComplete(target, questId)) {
			target.sendPackets("當前任務已由管理員強制完成。");
			args.notify(String.format("已強制完成玩家 %s 的 %d 號任務。", target.getName(), questId));
		} else {
			args.notify(String.format("玩家 %s 的 %d 號任務尚未啟動或已經結束。", target.getName(), questId));
		}
	}

	private void onSelectQuestComplete(final MJCommandArgs args, final Collection<L1PcInstance> targets, final int questId) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			//@override
			public void run() {
				int skipped = 0;
				for (L1PcInstance target : targets) {
					MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
					if (user.onRevealedComplete(target, questId)) {
						target.sendPackets("當前任務已由管理員強制完成。");
						++skipped;
					}
				}
				args.notify(String.format("已強制完成 %d 名玩家的 %d 號任務。", skipped, questId));
				return new L1PcInstance[0];
			}
		});
	}

	private void onRevealedQuestComplete(MJCommandArgs args, L1PcInstance target) {
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
		if (user == null) {
			args.notify(String.format("無法找到玩家 %s 的任務信息。", target.getName()));
			return;
		}
		int skipped = user.onRevealedComplete(target);
		if (skipped <= 0) {
			args.notify(String.format("玩家 %s 沒有正在啟動的任務。", target.getName()));
		} else {
			target.sendPackets("當前啟動的任務已由管理員強制完成。");
			args.notify(String.format("已強制完成玩家 %s 的 %d 個任務。", target.getName(), skipped));
		}
	}

	private void onRevealedQuestComplete(final MJCommandArgs args, final Collection<L1PcInstance> targets) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			//@override
			public void run() {
				int skipped = 0;
				for (L1PcInstance target : targets) {
					MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
					if (user.onRevealedComplete(target) > 0) {
						target.sendPackets("當前啟動的任務已由管理員強制完成。");
						++skipped;
					}
				}
				args.notify(String.format("在 %d 名目標中，已強制完成 %d 名玩家的任務。", targets.size(), skipped));
				return new L1PcInstance[0];
			}
		});
	}
}
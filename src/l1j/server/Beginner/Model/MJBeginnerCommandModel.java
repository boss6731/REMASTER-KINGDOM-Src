package l1j.server.Beginner.Model;

import java.util.Collection;

import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandComposite;
import l1j.server.MJTemplate.Command.MJCommandEx;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerCommandModel {
	MJBeginnerCommandModel() {
	}

	MJCommandEx createCommand() {
		return new MJCommandComposite("任務")
				.append(new SelectCompleteCommand())
				.append(new RevealedCompleteCommand());
	}

	private class SelectCompleteCommand implements MJCommandEx {
		@Override
		public boolean execute(MJCommandArgs args) {
			try {
				String target = args.nextString();
				int questId = args.nextInt();
				if (target.equalsIgnoreCase("*")) {
					onSelectQuestComplete(args, L1World.getInstance().getAllPlayers(), questId);
				} else {
					L1PcInstance pc = L1World.getInstance().getPlayer(target);
					if (pc == null) {
						args.notify(String.format("無法找到%s。", target));
					} else {
						onSelectQuestComplete(args, pc, questId);
					}
				}
				return true;
			} catch (Exception e) {
			}
			args.notify(".任務 選擇完成 [目標名稱 或 *(全部)] [任務ID]");
			return true;
		}

		@Override
		public String commandKey() {
			return "選擇完成";
		}
	}

	private class RevealedCompleteCommand implements MJCommandEx {
		@Override
		public boolean execute(MJCommandArgs args) {
			try {
				String target = args.nextString();
				if (target.equalsIgnoreCase("*")) {
					onRevealedQuestComplete(args, L1World.getInstance().getAllPlayers());
				} else {
					L1PcInstance pc = L1World.getInstance().getPlayer(target);
					if (pc == null) {
						args.notify(String.format("無法找到%s先生/女士。", target));
					} else {
						onRevealedQuestComplete(args, pc);
					}
				}
				return true;
			} catch (Exception e) {
			}
			args.notify(".任務 啟動完成 [目標名稱 或 *(全部)]");
			return true;
		}

		@Override
		public String commandKey() {
			return "啟動完成";
		}
	}

	private void onSelectQuestComplete(MJCommandArgs args, L1PcInstance target, int questId) {
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
		if (user == null) {
			args.notify(String.format("無法找到%s的任務信息。", target.getName()));
			return;
		}
		if (user.onRevealedComplete(target, questId)) {
			target.sendPackets("當前任務已由管理員完成。");
			args.notify(String.format("已強制完成%s的第%d號任務。", target.getName(), questId));
		} else {
			args.notify(String.format("%s的第%d號任務尚未激活或已經結束。", target.getName(), questId));
		}
	}

	private void onSelectQuestComplete(final MJCommandArgs args, final Collection<L1PcInstance> targets,
			final int questId) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				int skipped = 0;
				for (L1PcInstance target : targets) {
					MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
					if (user.onRevealedComplete(target, questId)) {
						target.sendPackets("當前任務已由管理員完成。");
						++skipped;
					}
				}
				args.notify(String.format("已對%d人強制完成第%d號任務。", skipped, questId));
			}
		});
	}

	private void onRevealedQuestComplete(MJCommandArgs args, L1PcInstance target) {
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
		if (user == null) {
			args.notify(String.format("無法找到%s的任務信息。", target.getName()));
			return;
		}
		int skipped = user.onRevealedComplete(target);
		if (skipped <= 0) {
			args.notify(String.format("%s沒有正在啟動的任務。", target.getName()));
		} else {
			target.sendPackets("由管理員完成了正在啟動的任務。");
			args.notify(String.format("已強制完成%s的%d個任務。", target.getName(), skipped));
		}
	}

	private void onRevealedQuestComplete(final MJCommandArgs args, final Collection<L1PcInstance> targets) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				int skipped = 0;
				for (L1PcInstance target : targets) {
					MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
					if (user.onRevealedComplete(target) > 0) {
						target.sendPackets("正在進行的任務已由管理員完成。");
						++skipped;
					}
				}
				args.notify(String.format("針對%d人中的%d人強制完成了任務。", targets.size(), skipped));
			}
		});
	}
}

package l1j.server.Beginner.Model;

import java.util.Collection;

import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandComposite;
import l1j.server.MJTemplate.Command.MJCommandEx;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// 初學者命令模型類
class MJBeginnerCommandModel {
	public MJBeginnerCommandModel() {
	}

	public MJCommandEx createCommand() {
		return new MJCommandComposite("任務") // "퀘스트" 是韩文，意為 "任務"
				.append(new SelectCompleteCommand())
				.append(new RevealedCompleteCommand());
	}

	// 私有內部類，實現了 MJCommandEx 接口
	private class SelectCompleteCommand implements MJCommandEx {
		@override
		public boolean execute(MJCommandArgs args) {
			try {
				String target = args.nextString();
				int questId = args.nextInt();
				if (target.equalsIgnoreCase("*")) {
					onSelectQuestComplete(args, L1World.getInstance().getAllPlayers(), questId);
				} else {
					L1PcInstance pc = L1World.getInstance().getPlayer(target);
					if (pc == null) {
						args.notify(String.format("找不到 %s。", target)); // "%s님을 찾을 수 없습니다。" 是韩文，意為 "找不到 %s"
					} else {
						onSelectQuestComplete(args, pc, questId);
					}
				}
				return true;
			} catch (Exception e) {
				// 在異常情況下返回正確的命令格式
				args.notify(".任務完成 [目標名稱 或 *(全部)] [任務ID]"); // ".퀘스트 선택완료 [대상이름 or *(전체)] [퀘스트아이디]" 是韩文，意為 ".任務完成 [目標名稱 或 *(全部)] [任務ID]"
				return true;
			}
		}

		@override
		public String commandKey() {
			return "選擇完成"; // "선택완료" 是韩文，意為 "選擇完成"
		}
	}

	// 私有內部類，實現了 MJCommandEx 接口
	private class RevealedCompleteCommand implements MJCommandEx {
		@override
		public boolean execute(MJCommandArgs args) {
			try {
				String target = args.nextString();
				if (target.equalsIgnoreCase("*")) {
					onRevealedQuestComplete(args, L1World.getInstance().getAllPlayers());
				} else {
					L1PcInstance pc = L1World.getInstance().getPlayer(target);
					if (pc == null) {
						args.notify(String.format("找不到 %s。", target)); // "%s님을 찾을 수 없습니다。" 是韩文，意為 "找不到 %s"
					} else {
						onRevealedQuestComplete(args, pc);
					}
				}
				return true;
			} catch (Exception e) {
				// 在異常情況下返回正確的命令格式
				args.notify(".任務激活完成 [目標名稱 或 *(全部)]"); // ".퀘스트 활성완료 [대상이름 or *(전체)]" 是韩文，意為 ".任務激活完成 [目標名稱 或 *(全部)]"
				return true;
			}
		}

		@override
		public String commandKey() {
			return "激活完成"; // "활성완료" 是韩文，意為 "激活完成"
		}
	}
}

	// 私有方法，處理選擇任務完成的邏輯
	private void onSelectQuestComplete(MJCommandArgs args, L1PcInstance target, int questId) {
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
		if (user == null) {
			args.notify(String.format("找不到 %s 的任務信息。", target.getName())); // "%s님의 퀘스트 정보를 찾을 수 없습니다。" 是韩文，意為 "找不到 %s 的任務信息"
			return;
		}
		if (user.onRevealedComplete(target, questId)) {
			target.sendPackets("當前任務已由管理員完成。"); // "운영자에 의해 현재 퀘스트가 완료되었습니다。" 是韩文，意為 "當前任務已由管理員完成"
			args.notify(String.format("強制完成了 %s 的 %d 號任務。", target.getName(), questId)); // "%s님의 %d번 퀘스트를 강제 완료시켰습니다。" 是韩文，意為 "強制完成了 %s 的 %d 號任務"
		} else {
			args.notify(String.format("%s 的 %d 號任務尚未激活或已完成。", target.getName(), questId)); // "%s님의 %d번 퀘스트는 아직 활성화되지 않았거나 이미 종료되었습니다。" 是韩文，意為 "%s 的 %d 號任務尚未激活或已完成"
		}
	}

	// 私有方法，處理選定任務完成的邏輯
	private void onSelectQuestComplete(final MJCommandArgs args, final Collection<L1PcInstance> targets, final int questId) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@override
			public void run() {
				int skipped = 0;
				for (L1PcInstance target : targets) {
					MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
					if (user.onRevealedComplete(target, questId)) {
						target.sendPackets("當前任務已由管理員完成。"); // "운영자에 의해 현재 퀘스트가 완료되었습니다。" 是韩文，意為 "當前任務已由管理員完成"
						++skipped;
					}
				}
				args.notify(String.format("強制完成了 %d 人的 %d 號任務。", skipped, questId)); // "%d명을 대상으로 %d번 퀘스트를 강제 완료시켰습니다。" 是韩文，意為 "強制完成了 %d 人的 %d 號任務"
			}
		});
	}

	// 私有方法，處理激活任務完成的邏輯
	private void onRevealedQuestComplete(MJCommandArgs args, L1PcInstance target) {
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
		if (user == null) {
			args.notify(String.format("找不到 %s 的任務信息。", target.getName())); // "%s님의 퀘스트 정보를 찾을 수 없습니다。" 是韩文，意為 "找不到 %s 的任務信息"
			return;
		}
		int skipped = user.onRevealedComplete(target);
		if (skipped <= 0) {
			args.notify(String.format("%s 沒有激活中的任務。", target.getName())); // "%s님은 활성화 중인 퀘스트가 없습니다。" 是韩文，意為 "%s 沒有激活中的任務"
		} else {
			target.sendPackets("激活中的任務已由管理員完成。"); // "운영자에 의해 활성화중인 퀘스트가 완료되었습니다。" 是韩文，意為 "激活中的任務已由管理員完成"
			args.notify(String.format("強制完成了 %s 的 %d 個任務。", target.getName(), skipped)); // "%s님의 %d개 퀘스트를 강제 완료시켰습니다。" 是韩文，意為 "強制完成了 %s 的 %d 個任務"
		}
	}

	// 私有方法，處理激活任務完成的邏輯
	private void onRevealedQuestComplete(final MJCommandArgs args, final Collection<L1PcInstance> targets) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@override
			public void run() {
				int skipped = 0;
				for (L1PcInstance target : targets) {
					MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(target);
					if (user.onRevealedComplete(target) > 0) {
						target.sendPackets("激活中的任務已由管理員完成。"); // "운영자에 의해 활성화중인 퀘스트가 완료되었습니다。" 是韩文，意為 "激活中的任務已由管理員完成"
						++skipped;
					}
				}
				args.notify(String.format("在 %d 名目標中強制完成了 %d 名玩家的任務。", targets.size(), skipped)); // "%d명을 대상 중 %d명의 퀘스트를 강제 완료시켰습니다。" 是韩文，意為 "在 %d 名目標中強制完成了 %d 名玩家的任務"
			}
		});
	}
}
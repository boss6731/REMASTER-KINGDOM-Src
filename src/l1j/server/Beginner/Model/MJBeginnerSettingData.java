package l1j.server.Beginner.Model;

import java.util.HashSet;

import l1j.server.Beginner.View.MJBeginnerFinishedView;
import l1j.server.Beginner.View.MJBeginnerProgressView;
import l1j.server.Beginner.View.MJBeginnerRevealView;
import l1j.server.Beginner.View.MJBeginnerStartView;
import l1j.server.Beginner.View.MJBeginnerTeleportView;

class MJBeginnerSettingData {
	private static MJBeginnerSettingData settingHolder;

	static void changedSettingsHolder(MJBeginnerSettingData data) {
		settingHolder = data;
	}

	private boolean developMode;
	private int mode;
	private HashSet<Integer> skippedQuests;
	private HashSet<Integer> hipassedQuests;

	boolean developMode() {
		return developMode;
	}

	MJBeginnerMode mode() {
		return MJBeginnerMode.fromInt(mode);
	}

	MJBeginnerModeHandler modeHandler() {
		return mode().modeHandler();
	}

	HashSet<Integer> skippedQuests() {
		return skippedQuests;
	}

	HashSet<Integer> hipassedQuests() {
		return hipassedQuests;
	}

	boolean skipped(int questId) {
		return skippedQuests().contains(questId);
	}

	boolean hispassed(int questId) {
		return hipassedQuests().contains(questId);
	}

	static enum MJBeginnerMode {
		NORMAL(0, new MJBeginnerNormalModeHandler()),
		HIPASS(1, new MJBeginnerHiPassModeHandler()),
		SKIP(2, new MJBeginnerSkipModeHandler()),
		;

		private int value;
		private MJBeginnerModeHandler modeHandler;

		MJBeginnerMode(int value, MJBeginnerModeHandler modeHandler) {
			this.value = value;
			this.modeHandler = modeHandler;
		}

		int toInt() {
			return value;
		}

		MJBeginnerModeHandler modeHandler() {
			return modeHandler;
		}

		boolean equals(MJBeginnerMode v) {
			return value == v.value;
		}

		static MJBeginnerMode fromInt(int i) {
			switch (i) {
				case 0:
					return NORMAL;
				case 1:
					return HIPASS;
				case 2:
					return SKIP;
				default:
					System.out.println(String.format("無效的 MJBeginnerMode：%d", i));
					return null;
			}
		}
	}

	static abstract class MJBeginnerModeHandler {
		abstract int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity);

		abstract MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider);

		abstract MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider);

		abstract MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider);

		abstract MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider);

		abstract MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider);
	}

	private static class MJBeginnerNormalModeHandler extends MJBeginnerModeHandler {
		@Override
		int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().calculateAddedStepCount(questId, addedQuantity,
						requiredQuantity);
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().calculateAddedStepCount(questId, addedQuantity,
						requiredQuantity);
			}
			return addedQuantity;
		}

		@Override
		MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider) {
			return new MJBeginnerProgressModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().revealModel(questId, provider);
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().revealModel(questId, provider);
			}
			return new MJBeginnerRevealModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().startModel(questId, provider);
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().startModel(questId, provider);
			}
			return new MJBeginnerStartModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().finishedModel(questId, provider);
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().finishedModel(questId, provider);
			}
			return new MJBeginnerFinishModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().teleportModel(questId, provider);
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().teleportModel(questId, provider);
			}
			return new MJBeginnerTeleportModel(provider);
		}
	}

	private static class MJBeginnerHiPassModeHandler extends MJBeginnerModeHandler {
		@Override
		int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().calculateAddedStepCount(questId, addedQuantity,
						requiredQuantity);
			}
			return requiredQuantity;
		}

		@Override
		MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider) {
			return new MJBeginnerProgressModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().revealModel(questId, provider);
			}
			return new MJBeginnerRevealModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().startModel(questId, provider);
			}
			return new MJBeginnerStartModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().finishedModel(questId, provider);
			}
			return new MJBeginnerFinishModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider) {
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().teleportModel(questId, provider);
			}
			return new MJBeginnerTeleportModel(provider);
		}
	}

	private static class MJBeginnerSkipModeHandler extends MJBeginnerModeHandler {
		@Override
		int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
			return requiredQuantity;
		}

		@Override
		MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider) {
			return new MJBeginnerProgressModel.MJBeginnerProgressSkipModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider) {
			return new MJBeginnerRevealModel.MJBeginnerRevealSkipModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider) {
			return new MJBeginnerStartModel.MJBeginnerStartSkipModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider) {
			return new MJBeginnerFinishModel.MJBeginnerFinishSkipModel(provider);
		}

		@Override
		MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider) {
			return new MJBeginnerTeleportModel.MJBeginnerTeleportSkipModel(provider);
		}
	}
}

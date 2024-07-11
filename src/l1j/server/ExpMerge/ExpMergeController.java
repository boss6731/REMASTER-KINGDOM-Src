package l1j.server.ExpMerge;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class ExpMergeController {
	private static final ExpMergeController controller = new ExpMergeController();

	public static ExpMergeController controller() {
		return controller;
	}

	private ExpMergeSystemModel systemModel;
	private ExpMergeDatabaseService databaseService;

	private ExpMergeController() {
		databaseService = new ExpMergeDatabaseService(this);
		systemModel = MJJsonUtil.fromFile("./config/exp-merge.json", ExpMergeSystemModel.class);

		MJMonitorCacheModel<ExpMergeSystemModel> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"mj-merge-system-model", "./config/exp-merge.json", ExpMergeSystemModel.class, MJEncoding.MS949);

		model.cacheListener(new ExpMergeModelConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}

	ExpMergeSystemModel systemModel() {
		return systemModel;
	}

	private class ExpMergeModelConverter implements MJMonitorCacheConverter<ExpMergeSystemModel> {
		@Override
		public ExpMergeSystemModel onNewCached(ExpMergeSystemModel t, long modifiedMillis) {
			systemModel = t;
			return null;
		}
	}

	public void onMerge(L1PcInstance useCharacter, L1ItemInstance mergeItem, int targetObjectId) {
		if (useCharacter.getId() == targetObjectId) {
			useCharacter.sendPackets("\\f3當前登入的角色無法合併經驗值.");
			return;
		}

		if (!databaseService.possibleMerge(useCharacter)) {
			return;
		}

		ExpMergeTargetModel targetModel = databaseService.selectMergeTarget(targetObjectId);
		if (targetModel == null) {
			useCharacter.sendPackets("\\f3無法找到目標角色.");
			return;
		}

		if (!databaseService.possibleMergeTarget(useCharacter, targetModel)) {
			return;
		}

		if (!useCharacter.getInventory().consumeItem(mergeItem, 1)) {
			return;
		}
		onMerge0(useCharacter, targetModel);
	}

	private void onMerge0(final L1PcInstance pc, final ExpMergeTargetModel targetModel) {
		double remainExp = (double) targetModel.exp * systemModel.mergeExpRation;
		long appendExp = 0;
		while (remainExp > 0) {
			int currentLevel = ExpTable.getLevelByExp(pc.get_exp());
			if (currentLevel >= systemModel.mergeTargetMaxLevel) {
				break;
			}
			double currentPenalty = ExpTable.getPenaltyRate(currentLevel);
			double needExp = (ExpTable.getNeedExpNextLevel(currentLevel)) / currentPenalty;
			if (needExp > remainExp) {
				needExp = remainExp;
			}
			remainExp -= needExp;

			long append = (long) (needExp * currentPenalty);
			appendExp += append;
			pc.add_exp(append);
		}
		databaseService.initializeMergeTarget(targetModel);
		databaseService.increaseMerge(pc);
		pc.sendPackets(String.format("\\f3已發放 %d 的經驗值.", appendExp));
	}
}

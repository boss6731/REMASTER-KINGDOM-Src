package l1j.server.revenge.model;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT.eAction;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_DELETE_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_INFO_ACK;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.revenge.MJRevengeService;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJRevengeProvider {
	
	private static final MJRevengeProvider provider = new MJRevengeProvider();
	public static MJRevengeProvider provider() {
		return provider;
	}
	
	private MJRevengeProvider() {
	}
	
	private SC_REVENGE_INFO_ACK newAck() {
		SC_REVENGE_INFO_ACK ack = SC_REVENGE_INFO_ACK.newInstance(); 
		ack.set_pursuit_cost(MJRevengeService.service().actionCost());
		ack.set_list_duration(MJRevengeService.service().expirationDuration());
		return ack;
	}
	
	private void mappedAckModels(SC_REVENGE_INFO_ACK ack, List<MJRevengeModel> models) {
		if(models == null) {
			ack.set_result(eRevengeResult.FAIL_SERVER);
		}else {
			ack.set_result(eRevengeResult.SUCCESS);
			long currentMillis = System.currentTimeMillis();
			for(MJRevengeModel model : models) {
				if(model.expirationRevengeMillis() < currentMillis) {
					models.remove(model);
					continue;
				}
				RevengeInfoT rInfo = model.newNetworkModel();
				if(rInfo == null) {
					continue;
				}
				ack.add_revenge_info(rInfo);
			}
		}
	}
	
	public void ackRevengeModel(L1PcInstance pc) {
		SC_REVENGE_INFO_ACK ack = newAck();
		List<MJRevengeModel> models = pc.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get();
		mappedAckModels(ack, models);
		pc.sendPackets(ack, MJEProtoMessages.SC_REVENGE_INFO_ACK);	
	}
	
	public void onNewCharacter(L1PcInstance pc) {
		SC_REVENGE_INFO_ACK ack = newAck();
		List<MJRevengeModel> models = pc.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get();
		boolean initialized = false;
		if(models == null) {
			models = onWorldCharacter(pc);
			initialized = true;
		}
		mappedAckModels(ack, models);
		pc.sendPackets(ack, MJEProtoMessages.SC_REVENGE_INFO_ACK);
		if(initialized) {
			for(MJRevengeModel model : models) {
				model.onLoad(pc);
			}
		}
		
		/*if(pc.attribute().has(L1PcInstance.revengeModelKey)) {
			ack.set_result(eRevengeResult.FAIL_UPDATE_PERIOD);
		}else {
			models = MJRevengeProvider.provider().onWorldCharacter(pc);
			mappedAckModels(ack, models);
		}
		pc.sendPackets(ack, MJEProtoMessages.SC_REVENGE_INFO_ACK);
		if(models != null) {
			for(MJRevengeModel model : models) {
				model.onLoad(pc);
			}
		}*/
	}
	
	public MJRevengeFindResult findRevengeModel(final L1PcInstance pc, final String name, final eAction action) {
		MJRevengeFindResult result = new MJRevengeFindResult();
		result.owner = pc;
		
		List<MJRevengeModel> models = pc.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get();
		if(models == null || models.size() <= 0) {
			result.result = eRevengeResult.FAIL_LIST;
			return result;
		}
		
		result.result = eRevengeResult.FAIL_USER;
		for(MJRevengeModel model : models) {
			if(model.targetName().equalsIgnoreCase(name)) {
				if(eAction.UNKNOWN != action && model.action() == action) {
					result.model = model;
					result.target = model.target();
					result.result = eRevengeResult.SUCCESS;
					break;
				}
			}
		}
		return result;
	}
	
	public List<MJRevengeModel> onWorldCharacter(final L1PcInstance pc){
		List<MJRevengeModel> models = pc.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get();
		if(models != null) {
			return models;
		}

		final int characterId = pc.getId();
		models = MJRevengeDatabasaeProvider.winner().select(characterId);
		models.addAll(MJRevengeDatabasaeProvider.loser().select(characterId));
		models.sort(new MJRevengeComparator(false));
		
		List<MJRevengeModel> viewModels = new CopyOnWriteArrayList<>();
		long latestTimestamp = 0;
		int currentOffset = 0;
		int viewInRows = MJRevengeService.service().viewInRows();
		long currentMillis = System.currentTimeMillis();
		for(MJRevengeModel model : models) {
			if(model.expirationRevengeMillis() < currentMillis) {
				latestTimestamp = Math.max(model.registerTimestamp(), latestTimestamp);
				break;
			}
			if(currentOffset++ < viewInRows) {
				viewModels.add(model);
				//model.noti(pc);
			}else{
				latestTimestamp = Math.max(model.registerTimestamp(), latestTimestamp);
				break;
			}
		}
		if(latestTimestamp > 0) {
			MJRevengeDatabasaeProvider.winner().onRevengeExpiration(characterId, latestTimestamp);
			MJRevengeDatabasaeProvider.loser().onRevengeExpiration(characterId, latestTimestamp);
		}

		pc.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).set(viewModels);
		return viewModels;
	}
	
	public static void onDelete(L1PcInstance owner, L1PcInstance target, int targetid) {
		List<MJRevengeModel> models;
		MJRevengeModel WinnerModel = null;
		List<MJRevengeModel> WinnerModels = MJRevengeDatabasaeProvider.winner().select(owner.getId());
		String Loser_Name = null;
		
		MJRevengeModel LoserModel = null;
		List<MJRevengeModel> LoserModels = MJRevengeDatabasaeProvider.loser().select(targetid);
		String WinnerName = null;
		
		int winner_size = WinnerModels.size();
		if (winner_size > 0) {
			for (int i = 0; i < winner_size; i++) {
				if (WinnerModels.get(i).targetId() != targetid)
					continue;
				Loser_Name = WinnerModels.get(i).targetName();
				models = owner.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get();
				for (MJRevengeModel model : models) {
					if (!model.targetName().equalsIgnoreCase(Loser_Name)) {
						continue;
					}
					WinnerModel = model;
					models.remove(model);
				}
				if (WinnerModel != null) {
					Updator.exec(String.format("delete from revenge_winners where owner_id=? and target_id=?"), new Handler() {
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setInt(1, owner.getId());
							pstm.setInt(2, targetid);
						}
					});
				}
			}
		}
		
		int loser_size = LoserModels.size();
		if (loser_size > 0) {
			for (int i = 0; i < loser_size; i++) {
				if (LoserModels.get(i).targetId() != owner.getId())
					continue;
				WinnerName = LoserModels.get(i).targetName();
				
				if (target != null)
					models = target.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get();
				else
					models = LoserModels;
				
				for (MJRevengeModel model : models) {
					if (!model.targetName().equalsIgnoreCase(WinnerName)) {
						continue;
					}
					LoserModel = model;
					models.remove(model);
				}
				if (LoserModel != null) {
					Updator.exec(String.format("delete from revenge_loser where owner_id=? and target_id=?"), new Handler() {
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setInt(1, targetid);
							pstm.setInt(2, owner.getId());
						}
					});
				}
			}
		}
		
		if (owner != null) {
			SC_REVENGE_DELETE_ACK.send(owner);
		}
		if (target != null) {
			SC_REVENGE_DELETE_ACK.send(target);
		}
	}

	private MJRevengeModel onAlreadyDelete(final L1PcInstance owner, final L1PcInstance target, final eAction action, final MJRevengeDatabasaeProvider provider) {
		final String targetName = target.getName();
		List<MJRevengeModel> models = owner.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get();
		MJRevengeModel alreadyModel = null;
		for(MJRevengeModel model : models) {
			if(model.action() != action) {
				continue;
			}
			if(!model.targetName().equalsIgnoreCase(targetName)) {
				continue;
			}
			alreadyModel = model;
			models.remove(model);
		}
		if(alreadyModel != null) {
			provider.onRevengeDelete(owner.getId(), target.getId());
		}
		return alreadyModel;
	}

	private static final S_SystemMessage failureMessage = new S_SystemMessage("\f2若自身或對方等級在75以下，將無法進行復仇登記。");

	public void onNewKill(final L1PcInstance killer, final L1PcInstance target) {
		if (target.noPlayerCK || target.noPlayerRobot || target.noPlayerck2) {
			if (target.getAI().getBotType() == MJBotType.HUNT)
				killer.sendPackets(failureMessage, false);
			return;
		}
		
		if(killer.getLevel() < MJRevengeService.service().useLowLevel()) {
			killer.sendPackets(failureMessage, false);
			target.sendPackets(failureMessage, false);
			return;
		}
		if(target.getLevel() < MJRevengeService.service().useLowLevel()) {
			target.sendPackets(failureMessage, false);
			killer.sendPackets(failureMessage, false);
			return;
		}
		if(killer.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get() == null) {
			killer.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).set(new CopyOnWriteArrayList<>() );
		}
		if(target.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get() == null) {
			target.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).set(new CopyOnWriteArrayList<>() );
		}
		onNewWinner(killer, target);
		onNewLoser(target, killer);
	}

	// owner 獲勝
	private MJRevengeModel onNewWinner(final L1PcInstance owner, final L1PcInstance target) {
// 如果我贏了，刪除我之前輸的記錄。
		onAlreadyDelete(owner, target, eAction.PURSUIT, MJRevengeDatabasaeProvider.loser());

		int accumulateActionCount = 1;
// 刪除我戰勝目標的記錄並獲取信息。
		MJRevengeModel alreadyModel = onAlreadyDelete(owner, target, eAction.TAUNT, MJRevengeDatabasaeProvider.winner());
		if(alreadyModel != null) {
			accumulateActionCount += alreadyModel.actionCount();
		}

		MJRevengeModel model = MJRevengeDatabasaeProvider.winner().factory().newModel();
		model.ownerId(owner.getId());
		model.targetId(target.getId());
		model.targetName(target.getName());
		model.registerTimestamp(System.currentTimeMillis());
		model.actionTimestamp(0L);
		model.actionRemainCount(MJRevengeService.service().tauntCount());
		model.actionCount(accumulateActionCount);
		MJRevengeDatabasaeProvider.winner().onRevengeRegister(model);



		// owner 輸了
		private MJRevengeModel onNewLoser(final L1PcInstance owner, final L1PcInstance target) {
		// 如果我輸了，刪除我之前贏的記錄。
			onAlreadyDelete(owner, target, eAction.TAUNT, MJRevengeDatabasaeProvider.winner());

			int accumulateActionCount = 1;
			// 刪除我對目標輸的記錄並獲取信息。
			MJRevengeModel alreadyModel = onAlreadyDelete(owner, target, eAction.PURSUIT, MJRevengeDatabasaeProvider.loser());
			if(alreadyModel != null) {
				accumulateActionCount += alreadyModel.actionCount();
			}
			MJRevengeModel pursuitModel = owner.attribute().getNotExistsNew(L1PcInstance.revengePursuitModelKey).get();
			if(pursuitModel != null && pursuitModel.targetId() == target.getId()) {
				owner.attribute().getNotExistsNew(L1PcInstance.revengePursuitModelKey).set(null);
			}

			MJRevengeModel model = MJRevengeDatabasaeProvider.loser().factory().newModel();
			model.ownerId(owner.getId());
			model.targetId(target.getId());
			model.targetName(target.getName());
			model.registerTimestamp(System.currentTimeMillis());
			model.actionTimestamp(0L);
			model.actionRemainCount(MJRevengeService.service().pursuitCount());
			model.actionCount(accumulateActionCount);
			MJRevengeDatabasaeProvider.loser().onRevengeRegister(model);
			owner.attribute().getNotExistsNew(L1PcInstance.revengeModelKey).get().add(0, model);
			model.noti(owner);
			return model;
		}
	
	public static class MJRevengeFindResult{
		public L1PcInstance owner;
		public L1PcInstance target;
		public MJRevengeModel model;
		public eRevengeResult result;
		MJRevengeFindResult(){}
	}
}

package l1j.server.revenge.model;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT.eAction;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT.eResult;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharDetailInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.revenge.MJRevengeService;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;

class MJRevengeLoserModel extends MJRevengeModel {
	@Override
	public eAction action() {
		return eAction.PURSUIT;
	}

	@Override
	public void onLoad(final L1PcInstance pc) {
		if (!alivePursuit()) {
			return;
		}
		pc.attribute().getNotExistsNew(L1PcInstance.revengePursuitModelKey).set(this);
		// noti(pc);
	}

	@Override
	public eRevengeResult onRevengeAction(final L1PcInstance pc) {
		eRevengeResult result = availableAction0();
		if (result != eRevengeResult.SUCCESS) {
			return result;
		}

		final L1PcInstance target = target();
		if (target == null) {
			return eRevengeResult.FAIL_USER;
		}

		MJRevengeModel model = pc.attribute().getNotExistsNew(L1PcInstance.revengePursuitModelKey).get();
		if (model != null && model.alivePursuit()) {
			return eRevengeResult.FAIL_ALREADY_PURSUITING;
		}

		try {
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, MJRevengeService.service().actionCost())) {
				return eRevengeResult.FAIL_COST;
			}
			onRevengeAction0();
			pc.attribute().getNotExistsNew(L1PcInstance.revengePursuitModelKey).set(this);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					MJRevengeService.service().sendCrossMessage(pc, target, 6970, null);
					L1Map map = L1WorldMap.getInstance().getMap(target.getMapId());

					if (pc.isDead()) {
						pc.sendPackets("你在死亡狀態下無法使用這個功能。");
						return;
					}

					/** 對方位於攻城區 **/
					int castle_id = L1CastleLocation.getCastleIdByArea(target);
					if (castle_id != 0) {
						pc.sendPackets("\f3對方位於無法傳送的區域，無法進行傳送。");
						return;
					}

					if (map == null || !map.isTeleportable() || pc.getMapId() != MJRevengeService.service().pursuitnoTeleport().length) {
						pc.sendPackets("\f3對方位於無法傳送的區域，無法進行傳送。");
					} else {
						MJPoint pt = MJPoint.newInstance(target.getX(), target.getY(), MJRevengeService.service().pursuitTeleportRadius(), target.getMapId());
						pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true, true);
					}
				}
			}, 300L);
			return eRevengeResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eRevengeResult.FAIL_SERVER;
	}

	private void onRevengeAction0() {
		actionRemainCount(actionRemainCount() - 1);
		actionTimestamp(System.currentTimeMillis());
		MJRevengeDatabasaeProvider.loser().onRevengeAction(this);
		/*
		 * if(actionRemainCount() <= 0) { MJRevengeDatabasaeProvider.loser().onRevengeDelete(ownerId(), targetId()); }else { MJRevengeDatabasaeProvider.loser().onRevengeAction(this); }
		 */
	}

	@Override
	public boolean alivePursuit() {
		if (actionTimestamp() <= 0) {
			return false;
		}

		long diffMillis = System.currentTimeMillis() - actionTimestamp();
		return diffMillis < MJRevengeService.service().actionDuration() * 1000L;
	}

	@Override
	public RevengeInfoT newNetworkModel() {
		MJMyCharDetailInfo charModel = MJMyCharService.service().fromCharacterNameDetail(targetName());
		if (charModel == null) {
			return null;
		}

		RevengeInfoT rInfo = RevengeInfoT.newInstance();
		rInfo.set_crimescene_server_no(0);
		rInfo.set_server_no(0);
		rInfo.set_register_timestamp((int) (registerTimestamp() / 1000));
		rInfo.set_user_name(targetName());
		rInfo.set_user_uid(targetId());
		rInfo.set_game_class(charModel.characterClass);
		rInfo.set_pledge_name(null);
		rInfo.set_pledge_id(-1);
		if (!MJString.isNullOrEmpty(charModel.pledge)) {
			L1Clan clan = ClanTable.getInstance().find(charModel.pledge);
			if (clan != null) {
				rInfo.set_pledge_id(clan.getClanId());
				rInfo.set_pledge_name(clan.getClanName());
			}
		}
		int currentSeconds = (int) (System.currentTimeMillis() / 1000);

		rInfo.set_active(target() != null);
		rInfo.set_unregister_duration((rInfo.get_register_timestamp() + MJRevengeService.service().expirationDuration()) - currentSeconds);
		rInfo.set_action_timestamp(actionTimestamp() <= 0 ? 0 : (int) (actionTimestamp() / 1000));
		rInfo.set_action_type(action());
		rInfo.set_action_count(actionCount());
		rInfo.set_action_remain_count(actionRemainCount());
		rInfo.set_action_result(eResult.LOSE);
		int diffSeconds = currentSeconds - rInfo.get_action_timestamp();
		if (diffSeconds < MJRevengeService.service().actionDuration()) {
			rInfo.set_action_duration(MJRevengeService.service().actionDuration() - diffSeconds);
		} else {
			rInfo.set_action_duration(0);
		}

		int activate_duration = currentSeconds - rInfo.get_register_timestamp(); // 현재시간 10 , 발생시간 3 = 7;
		if (activate_duration > MJRevengeService.service().actionDurationTo()) {
			activate_duration = 0;
		} else {
			activate_duration = MJRevengeService.service().actionDurationTo() - activate_duration;
		}
		rInfo.set_activate_duration(activate_duration); // 2시간동안 클릭이 가능하게 활성화됨
		return rInfo;
	}

}

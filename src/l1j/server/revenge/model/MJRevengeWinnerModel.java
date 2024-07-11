package l1j.server.revenge.model;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT.eAction;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT.eResult;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharDetailInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.revenge.MJRevengeService;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

class MJRevengeWinnerModel extends MJRevengeModel{
	@Override
	public eAction action() {
		return eAction.TAUNT;
	}

	@Override
	public void onLoad(final L1PcInstance pc) {
		
	}
	
	@Override
	public eRevengeResult onRevengeAction(final L1PcInstance pc) {
		eRevengeResult result = availableAction0();
		if(result != eRevengeResult.SUCCESS) {
			return result;
		}

		final L1PcInstance target = target();
		if(target == null) {
			return eRevengeResult.FAIL_USER;
		}

		try {
			if(!pc.getInventory().consumeItem(L1ItemId.ADENA, MJRevengeService.service().actionCost())) {
				return eRevengeResult.FAIL_COST;
			}
			onRevengeAction0();
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@override
				public void run() {
					pc.send_effect(18021, true);
					target.send_effect(8113, true);
					SC_NOTIFICATION_MESSAGE_NOT noti = SC_NOTIFICATION_MESSAGE_NOT.newInstance();
					noti.set_duration(1);
					noti.set_messageRGB(MJSimpleRgb.red());
					noti.set_notificationMessage(String.format("\f3%s 已經擊敗 %s。", pc.getName(), target.getName()));
					noti.set_suffileNumber(38 * 2);
					ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_NOTIFICATION_MESSAGE_NOT);
					MJRevengeService.service().sendCrossMessage(pc, target, 6971, stream);
					ServerBasePacket[] pcks = new ServerBasePacket[] { new S_SystemMessage(String.format("\aD挑釁: [%s] 的攻擊使 [%s] 死亡。", pc.getName(), target.getName(), Opcodes.S_MESSAGE)), };
					L1World.getInstance().broadcastPacketToAll(pcks, true);
				}
			}, 100);
			return eRevengeResult.SUCCESS;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return eRevengeResult.FAIL_SERVER;
	}

	private void onRevengeAction0() {
		actionRemainCount(actionRemainCount() - 1);
		actionTimestamp(System.currentTimeMillis());
		MJRevengeDatabasaeProvider.winner().onRevengeAction(this);
	}

	@Override
	public RevengeInfoT newNetworkModel() {
		MJMyCharDetailInfo charModel = MJMyCharService.service().fromCharacterNameDetail(targetName());
		if(charModel == null) {
			return null;
		}
		
		RevengeInfoT rInfo = RevengeInfoT.newInstance();
		rInfo.set_crimescene_server_no(0);
		rInfo.set_server_no(0);
		rInfo.set_register_timestamp((int)(registerTimestamp() / 1000));
		rInfo.set_user_name(targetName());
		rInfo.set_user_uid(targetId());
		rInfo.set_game_class(charModel.characterClass);
		rInfo.set_pledge_name(MJString.EmptyString);
		rInfo.set_pledge_id(0);
		if(!MJString.isNullOrEmpty(charModel.pledge)) {
			L1Clan clan = ClanTable.getInstance().find(charModel.pledge);
			if(clan != null) {
				rInfo.set_pledge_id(clan.getClanId());
				rInfo.set_pledge_name(clan.getClanName());
			}
		}
		int currentSeconds = (int)(System.currentTimeMillis() / 1000);
		
		rInfo.set_active(target() != null);
		rInfo.set_unregister_duration((rInfo.get_register_timestamp() + MJRevengeService.service().expirationDuration()) - currentSeconds);
		rInfo.set_action_timestamp(actionTimestamp() <= 0 ? 0 : (int)(actionTimestamp() / 1000));
		rInfo.set_action_type(action());
		rInfo.set_action_count(actionCount());
		rInfo.set_action_remain_count(actionRemainCount());
		rInfo.set_action_duration(0);
		rInfo.set_action_result(eResult.WIN);
		return rInfo;
	}

	@Override
	public boolean alivePursuit() {
		return false;
	}
}

package l1j.server.revenge;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class MJRevengeService {
	private static final MJRevengeService service = new MJRevengeService();
	public static MJRevengeService service() {
		return service;
	}
	
	private MJRevengeSettingModel model;

	private MJRevengeService() {
		MJMonitorCacheModel<MJRevengeSettingModel> model = MJMonitorCacheProvider.newJsonFileCacheModel("mj-revenge-setting-model", "./config/revenge-service.json", MJRevengeSettingModel.class,
				MJEncoding.MS949);
		model.cacheListener(new MJRevengeSettingModelConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}

	// 系統使用與否
	public boolean use() {
		return model.use;
	}

	// 顯示在畫面上的列表數量（可能會調整）
	public int viewInRows() {
		return model.viewInRows;
	}

	// 進入復仇系統的最低等級
	public int useLowLevel() {
		return model.useLowLevel;
	}

	// 追蹤時的傳送範圍（對方）
	public int pursuitTeleportRadius() {
		return model.pursuitTeleportRadius;
	}

	// （PVP復仇）無法追蹤的區域（地圖ID）
	public int[] pursuitnoTeleport() {
		return model.pursuitnoTeleport;
	}

	// 追蹤/挑釁時的花費
	public int actionCost() {
		return model.actionCost;
	}

	// 列表中數據過期的時間（秒）
	public int expirationDuration() {
		return model.expirationDuration;
	}

	// 動作（追蹤）持續的時間（秒）
	public int actionDuration() {
		return model.actionDuration;
	}

	// 追蹤激活的時間（秒）
	public int actionDurationTo() {
		return model.actionDurationTo;
	}

	/**
	 * 返回追蹤次數。
	 * @return int
	 **/
	public int pursuitCount() {
		return model.pursuitCount;
	}

	/**
	 * 返回挑釁次數。
	 * @return int
	 **/
	public int tauntCount() {
		return model.tauntCount;
	}
	
	
	public void sendCrossMessage(final L1PcInstance killer, final L1PcInstance target, int stringId, ProtoOutputStream stream) {
		L1Clan ownerClan = killer.getClan();
		L1Clan targetClan = target.getClan();
		S_ServerMessage message = new S_ServerMessage(stringId, 
				ownerClan == null ? "-" : ownerClan.getClanName(), 
				killer.getName(), 
				targetClan == null ? "-" : targetClan.getClanName(), 
				target.getName());
		
		if(ownerClan == null) {
			if(stream != null) {
				killer.sendPackets(stream, false);
			}
			killer.sendPackets(message, false);
		}else {
			if(stream != null) {
				ownerClan.broadcast(stream, false);
			}
			ownerClan.broadcast(message, false);
		}
		if(targetClan == null) {
			if(stream != null) {
				target.sendPackets(stream, false);
			}
			target.sendPackets(message, false);
		}else {
			if(stream != null) {
				targetClan.broadcast(stream, false);
			}
			targetClan.broadcast(message, false);
		}
		message.clear();
		if(stream != null) {
			stream.dispose();
		}
	}
	
	static class MJRevengeSettingModel {
		boolean use;
		int viewInRows;
		int useLowLevel;
		int pursuitTeleportRadius;
		int[] pursuitnoTeleport;
		int actionCost;
		int expirationDuration;
		int actionDuration;
		int actionDurationTo;
		int pursuitCount;
		int tauntCount;
		MJRevengeSettingModel(){
			use = true;
			viewInRows = 20;
			useLowLevel = 75;
			pursuitTeleportRadius = 10;
			pursuitnoTeleport = new int[] { 4, 99};
			actionCost = 10000;
			expirationDuration = 3600 * 24;	
			actionDuration = 60 * 10;
			actionDurationTo = 7200;
			pursuitCount = 3;
			tauntCount = 1;
		}
	}
	
	private class MJRevengeSettingModelConverter implements MJMonitorCacheConverter<MJRevengeSettingModel>{
		@Override
		public MJRevengeSettingModel onNewCached(MJRevengeSettingModel t, long modifiedMillis) {
			model = t;
			return null;
		}
	}
}

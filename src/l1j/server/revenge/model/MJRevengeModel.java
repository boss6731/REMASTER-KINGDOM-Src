package l1j.server.revenge.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJTime;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT.eAction;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_REVENGE_INFO_NOTI;
import l1j.server.revenge.MJRevengeService;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public abstract class MJRevengeModel {
	private int ownerId;
	private int targetId;
	private String targetName;
	private long registerTimestamp;
	private long actionTimestamp;
	private int actionRemainCount;
	private int actionCount;
	
	protected MJRevengeModel() {}
	
	public void readToDatabase(ResultSet rs) throws SQLException {
		ownerId = rs.getInt("owner_id");
		targetId = rs.getInt("target_id");
		targetName = rs.getString("target_name");
		registerTimestamp = MJTime.convertLong(rs.getTimestamp("register_timestamp"));
		actionTimestamp = MJTime.convertLong(rs.getTimestamp("action_timestamp"));
		actionRemainCount = rs.getInt("action_remain_count");
		actionCount = rs.getInt("action_count");
	}
	
	public void noti(L1PcInstance pc) {
		SC_REVENGE_INFO_NOTI noti = SC_REVENGE_INFO_NOTI.newInstance();
		noti.set_target_info(newNetworkModel());
		pc.sendPackets(noti, MJEProtoMessages.SC_REVENGE_INFO_NOTI);
	}
	
	public L1PcInstance target() {
		L1Object obj = L1World.getInstance().findObject(targetId);
		return (obj != null && obj instanceof L1PcInstance) ?
				(L1PcInstance)obj :
				null;
	}
	
	public long expirationRevengeMillis() {
		return registerTimestamp + ((long)MJRevengeService.service().expirationDuration() * 1000L);
	}
	
	public boolean aliveRevenge() {
		return aliveRevenge(System.currentTimeMillis());
	}
	
	public boolean aliveRevenge(final long currentMillis) {
		// expiration.
		return currentMillis < expirationRevengeMillis();
	}
	
	
	eRevengeResult availableAction0() {
		long currentMillis = System.currentTimeMillis();
		if (!aliveRevenge(currentMillis)) {
			return eRevengeResult.FAIL_UPDATE_PERIOD;
		}

		// empty action count
		if (actionRemainCount() <= 0) {
			return eRevengeResult.FAIL_COUNT;
		}

		// already action time
		if (currentMillis < actionTimestamp() + ((long) MJRevengeService.service().actionDuration() * 1000L)) {
			return eRevengeResult.FAIL_ALREADY_PURSUITING;
		}
		return eRevengeResult.SUCCESS;
	}
	
	
	public int ownerId() {
		return ownerId;
	}
	
	public void ownerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	public int targetId() {
		return targetId;
	}
	
	public void targetId(int targetId) {
		this.targetId = targetId;
	}
	
	public String targetName() {
		return targetName;
	}
	
	public void targetName(String targetName) {
		this.targetName = targetName;
	}
	
	public long registerTimestamp() {
		return registerTimestamp;
	}
	
	public void registerTimestamp(long registerTimestamp) {
		this.registerTimestamp = registerTimestamp;
	}
	
	public long actionTimestamp() {
		return actionTimestamp;
	}
	
	public void actionTimestamp(long actionTimestamp) {
		this.actionTimestamp = actionTimestamp;
	}
	
	public int actionRemainCount() {
		return actionRemainCount;
	}
	
	public void actionRemainCount(int actionRemainCount) {
		this.actionRemainCount = actionRemainCount;
	}
	
	public int actionCount() {
		return actionCount;
	}
	
	public void actionCount(int actionCount) {
		this.actionCount = actionCount;
	}
	public abstract void onLoad(final L1PcInstance pc);
	public abstract boolean alivePursuit();
	public abstract eAction action();
	public abstract eRevengeResult onRevengeAction(final L1PcInstance pc);
	public abstract RevengeInfoT newNetworkModel();
	
	

}

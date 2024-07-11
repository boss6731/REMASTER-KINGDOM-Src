package l1j.server.CPMWBQSystem.info;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

public class CPMWBQReward {
	private static final CPMWBQReward service = new CPMWBQReward();

	public static CPMWBQReward service() {
		return service;
	}

	private RewardInfo model;

	private CPMWBQReward() {
		MJMonitorCacheModel<RewardInfo> model = MJMonitorCacheProvider.newJsonFileCacheModel(
				"cpmw-bq-setting", "./config/bookquest-service.json", RewardInfo.class, MJEncoding.MS949);
		model.cacheListener(new RewardInfoConverter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}

	/**
	 * 基本經驗資訊*
	 ** 
	 * @傳回整數
	 */
	public int basicexp(int type) {
		switch (type) {
			case 1:
				return model.RG_NORMAL_EXP;
			case 2:
				return model.RG_DRAGON_EXP;
			case 3:
				return model.RG_HIGH_DRAGON_EXP;
		}
		return 0;
	}

	/**
	 * 經驗助推器項目 ID*
	 ** 
	 * @傳回整數
	 */
	public int boosteritem() {
		return model.BOOSTER_ITEM_ID;
	}

	/**
	 * 加速器經驗值資訊*
	 * * @return int
	 */
	public int boosterexp(int type) {
		switch (type) {
			case 1:
				return model.RG_NORMAL_BOOSTER_EXP;
			case 2:
				return model.RG_DRAGON_BOOSTER_EXP;
			case 3:
				return model.RG_HIGH_DRAGON_BOOSTER_EXP;
		}
		return 0;
	}

	/**
	 * 等級附加經驗資訊*
	 * * @return int
	 */
	public int levelexp(int type) {
		switch (type) {
			case 1:
				return model.RG_NORMAL_LEVEL_ADD_EXP;
			case 2:
				return model.RG_DRAGON_LEVEL_ADD_EXP;
			case 3:
				return model.RG_HIGH_DRAGON_LEVEL_ADD_EXP;
		}
		return 0;
	}

	/**
	 * 賠償完成後所需項目信息*
	 * * @return int
	 */
	public int needitem(int type) {
		switch (type) {
			case 2:
				return model.RG_DRAGON_NEED_ITEMID;
			case 3:
				return model.RG_HIGH_DRAGON_NEED_ITEMID;
		}
		return 0;
	}

	/**
	 * 完成後收費*
	 * * @return int
	 */
	public int addeinper(int type) {
		switch (type) {
			case 1:
				return model.RG_NORMAL_ADD_AIN;
			case 2:
				return model.RG_DRAGON_ADD_AIN;
			case 3:
				return model.RG_HIGH_DRAGON_ADD_AIN;
		}
		return 0;
	}

	/**
	 * 獎勵物品ID*
	 * * @return int
	 */
	public int rewardid(int type) {
		switch (type) {
			case 1:
				return model.RG_NORMAL_REWARD;
			case 2:
				return model.RG_DRAGON_REWARD;
			case 3:
				return model.RG_HIGH_DRAGON_REWARD;
		}
		return 0;
	}

	/**
	 * 獎勵物品數量
	 * 
	 * @return int
	 */
	public int rewardcount(int type) {
		switch (type) {
			case 1:
				return model.RG_NORMAL_REWARD_COUNT;
			case 2:
				return model.RG_DRAGON_REWARD_COUNT;
			case 3:
				return model.RG_HIGH_DRAGON_REWARD_COUNT;
		}
		return 0;
	}

	public int addexplevel() {
		return model.ADD_EXP_LEVEL;
	}

	public int gethour() {
		return model.RENEW_TIME;
	}

	public int getminiute() {
		return model.RENEW_MINUTE;
	}

	public boolean use() {
		return model.ISUSE;
	}

	static class RewardInfo {
		boolean ISUSE;
		int RENEW_TIME;
		int RENEW_MINUTE;
		int RG_NORMAL_EXP;
		int RG_DRAGON_EXP;
		int RG_HIGH_DRAGON_EXP;
		int BOOSTER_ITEM_ID;
		int RG_NORMAL_BOOSTER_EXP;
		int RG_DRAGON_BOOSTER_EXP;
		int RG_HIGH_DRAGON_BOOSTER_EXP;
		int RG_DRAGON_NEED_ITEMID;
		int RG_HIGH_DRAGON_NEED_ITEMID;
		int ADD_EXP_LEVEL;
		int RG_NORMAL_LEVEL_ADD_EXP;
		int RG_DRAGON_LEVEL_ADD_EXP;
		int RG_HIGH_DRAGON_LEVEL_ADD_EXP;
		int RG_NORMAL_ADD_AIN;
		int RG_DRAGON_ADD_AIN;
		int RG_HIGH_DRAGON_ADD_AIN;
		int RG_NORMAL_REWARD;
		int RG_DRAGON_REWARD;
		int RG_HIGH_DRAGON_REWARD;
		int RG_NORMAL_REWARD_COUNT;
		int RG_DRAGON_REWARD_COUNT;
		int RG_HIGH_DRAGON_REWARD_COUNT;

		RewardInfo() {
			ISUSE = false;
			RENEW_TIME = 9;
			RENEW_MINUTE = 10;
			RG_NORMAL_EXP = 1000;
			RG_DRAGON_EXP = 1000;
			RG_HIGH_DRAGON_EXP = 1000;
			BOOSTER_ITEM_ID = 40308;
			RG_NORMAL_BOOSTER_EXP = 1000;
			RG_DRAGON_BOOSTER_EXP = 1000;
			RG_HIGH_DRAGON_BOOSTER_EXP = 1000;
			RG_DRAGON_NEED_ITEMID = 1000007;
			RG_HIGH_DRAGON_NEED_ITEMID = 3000231;
			ADD_EXP_LEVEL = 90;
			RG_NORMAL_LEVEL_ADD_EXP = 1000;
			RG_DRAGON_LEVEL_ADD_EXP = 1000;
			RG_HIGH_DRAGON_LEVEL_ADD_EXP = 1000;
			RG_NORMAL_ADD_AIN = 10;
			RG_DRAGON_ADD_AIN = 100;
			RG_HIGH_DRAGON_ADD_AIN = 500;
			RG_NORMAL_REWARD = 40308;
			RG_DRAGON_REWARD = 40308;
			RG_HIGH_DRAGON_REWARD = 40308;
			RG_NORMAL_REWARD_COUNT = 1000;
			RG_DRAGON_REWARD_COUNT = 10000;
			RG_HIGH_DRAGON_REWARD_COUNT = 100000;
		}
	}

	private class RewardInfoConverter implements MJMonitorCacheConverter<RewardInfo> {
		@Override
		public RewardInfo onNewCached(RewardInfo t, long modifiedMillis) {
			model = t;
			return null;
		}
	}
}

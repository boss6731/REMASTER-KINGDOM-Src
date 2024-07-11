package l1j.server.MJDungeonTimer;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJSurveySystem.MJInterfaceSurvey;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_CHARGED_MAP_TIME_INFO_NOTI;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class DungeonTimePotion {
	public static DungeonTimePotion newInstance(ResultSet rs) throws SQLException {
		return newInstance().set_item_id(rs.getInt("item_id")).set_timer_id(rs.getInt("timer_id")).set_timer_time(rs.getInt("timer_time")).set_ment_use(MentUse_Type(rs.getString("use_ment")));
	}

	public static DungeonTimePotion newInstance() {
		return new DungeonTimePotion();
	}

	public static boolean MentUse_Type(String type) {
		if (type.equalsIgnoreCase("true"))
			return true;
		return false;
	}

	private int _item_id;
	private int _timer_id;
	private int _timer_time;
	private boolean _ment_use;

	private DungeonTimePotion() {
	}

	public DungeonTimePotion set_item_id(int val) {
		_item_id = val;
		return this;
	}

	public int get_item_id() {
		return _item_id;
	}

	public DungeonTimePotion set_timer_id(int val) {
		_timer_id = val;
		return this;
	}

	public int get_timer_id() {
		return _timer_id;
	}

	public DungeonTimePotion set_timer_time(int val) {
		_timer_time = val;
		return this;
	}

	public int get_timer_time() {
		return _timer_time;
	}

	public DungeonTimePotion set_ment_use(boolean flag) {
		_ment_use = flag;
		return this;
	}

	public boolean get_ment_use() {
		return _ment_use;
	}

	public void use_potion(L1PcInstance pc, L1ItemInstance item) {
		ServerBasePacket pck = MJSurveySystemLoader.getInstance().registerSurvey(
				String.format("使用 %s 來充值時間嗎?", item.getName()),
				item.getId(),
				new MJInterfaceSurvey() {
					@override
					public void onSurveyResponse(boolean accepted) {
						if (accepted) {
							// 處理接受情況
						} else {
							// 處理拒絕情況
						}
					}
				}
		);
			@Override
		public void survey(L1PcInstance pc, int num, boolean isYes) {
			if (!isYes)
				return;

			L1ItemInstance item = pc.getInventory().findItemObjId(num);
			if (item == null) {
				pc.sendPackets("無法找到充值道具。");
				return;
			}

			DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(get_timer_id());
			if (dtInfo == null) {
				pc.sendPackets("這個道具已被管理員禁用。");
				return;
			}

			DungeonTimeProgress<?> progress = pc.get_progress(dtInfo);

			if (progress == null) {
				pc.sendPackets(String.format("在使用充值石之前，您必須首先進入一次地下城（%s）。", dtInfo.get_description()));
				return;
			/*
			DungeonTimeInformation Info = DungeonTimeInformationLoader.getInstance().from_timer_id(get_timer_id());
			if (Info != null) {
			pc.send_dungeon_progress(Info, false);
			progress = pc.get_progress(Info);
			pc.getInventory().removeItem(item, 1);
			progress.set_remain_seconds(get_timer_time());
			progress.inc_charge_count();
			DungeonTimeProgressLoader.update(progress);
			pc.sendPackets(String.format("%s已被充值。", dtInfo.get_description()));
			return;
			}
			*/
			}





			int remain_time = 0;
			if (get_timer_time() > 0) {
				remain_time = progress.get_remain_seconds() + get_timer_time();
				if (dtInfo.get_amount_seconds() > 0) {
					if ((progress.get_remain_seconds() + get_timer_time()) >= dtInfo.get_amount_seconds()) {
						pc.sendPackets("已超過最大充值次數。");
						return;
					}
				} else {
					if ((progress.get_remain_seconds() + get_timer_time()) > dtInfo.get_charge_count() * get_timer_time()) {
						pc.sendPackets("已超過最大充值時間。");
						return;
					}
					if (progress.get_charge_count() >= dtInfo.get_charge_count()) {
						pc.sendPackets("已超過最大充值次數。");
						return;
					}
				}
			} else {
				remain_time = dtInfo.get_amount_seconds();
				if (progress == null || progress.get_remain_seconds() > 0) {
					pc.sendPackets("地下城使用時間還未結束。");
					return;
				}
			}
			pc.getInventory().removeItem(item, 1);
			progress.set_remain_seconds(remain_time);
			progress.inc_charge_count();
			DungeonTimeProgressLoader.update(progress);
			pc.sendPackets(String.format("%s已被充值。", dtInfo.get_description()));

			Integer[] mapids = (Integer[]) MJArrangeParser.parsing(dtInfo.get_map_ids(), ",", MJArrangeParseeFactory.createIntArrange()).result();
			boolean progress_check = false;
			for (Integer i : mapids) {
				if (pc.getMapId() == i)
					progress_check = true;
			}

			if (progress_check) {
				pc.send_dungeon_progress(dtInfo, true);
			} else {
				pc.send_dungeon_progress(dtInfo, false);
			}

/*
if (DungeonTimeInformationLoader.getInstance().from_map_id(pc.getMapId()) != null)
pc.send_dungeon_progress(dtInfo, true);
else
pc.send_dungeon_progress(dtInfo, false);
*/
		}
	}, 3000L);

		if (get_ment_use()) {
		if (pck == null) {
			pc.sendPackets("請在首次點擊後3秒再使用。");
		} else {
			pc.sendPackets(pck);
		}
	} else {
		if (pc.getInventory().findItemObjId(item.getId()) == null) {
			pc.sendPackets("無法找到充值道具。");
			return;
		}

		DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(get_timer_id());
		if (dtInfo == null) {
			pc.sendPackets("這個道具已被管理員禁用。");
			return;
		}

		DungeonTimeProgress<?> progress = pc.get_progress(dtInfo);

		if (progress == null) {
			pc.sendPackets(String.format("在使用充值石之前，您必須首先進入一次地下城（%s）。", dtInfo.get_description()));
			return;
/*
DungeonTimeInformation Info = DungeonTimeInformationLoader.getInstance().from_timer_id(get_timer_id());
if (Info != null) {
pc.send_dungeon_progress(Info, false);
progress = pc.get_progress(Info);
pc.getInventory().removeItem(item, 1);
progress.set_remain_seconds(get_timer_time());
progress.inc_charge_count();
DungeonTimeProgressLoader.update(progress);
pc.sendPackets(String.format("%s已被充值。", dtInfo.get_description()));
return;
}
*/
		}

		int remain_time = 0;
		if (get_timer_time() > 0) {
			remain_time = progress.get_remain_seconds() + get_timer_time();
			if (dtInfo.get_amount_seconds() > 0) {
				if ((progress.get_remain_seconds() + get_timer_time()) >= dtInfo.get_amount_seconds()) {
					pc.sendPackets("已超過最大充值次數。");
					return;
				}
			} else {
				if ((progress.get_remain_seconds() + get_timer_time()) > dtInfo.get_charge_count() * get_timer_time()) {
					pc.sendPackets("已超過最大充值時間。");
					return;
				}
				if (progress.get_charge_count() >= dtInfo.get_charge_count()) {
					pc.sendPackets("已超過最大充值次數。");
					return;
				}
			}
		} else {
			remain_time = dtInfo.get_amount_seconds();
			if (progress == null || progress.get_remain_seconds() > 0) {
				pc.sendPackets("地下城使用時間還未結束。");
				return;
			}
		}
		pc.getInventory().removeItem(item, 1);
		progress.set_remain_seconds(remain_time);
		progress.inc_charge_count();
		DungeonTimeProgressLoader.update(progress);
		pc.sendPackets(String.format("%s已被充值。", dtInfo.get_description()));

		Integer[] mapids = (Integer[]) MJArrangeParser.parsing(dtInfo.get_map_ids(), ",", MJArrangeParseeFactory.createIntArrange()).result();
		boolean progress_check = false;
		for (Integer i : mapids) {
			if (pc.getMapId() == i)
				progress_check = true;
		}

		if (progress_check) {
			pc.send_dungeon_progress(dtInfo, true);
		} else {
			pc.send_dungeon_progress(dtInfo, false);
		}

/*
if (DungeonTimeInformationLoader.getInstance().from_map_id(pc.getMapId()) != null)
pc.send_dungeon_progress(dtInfo, true);
else
pc.send_dungeon_progress(dtInfo, false);
*/
	}
}
}
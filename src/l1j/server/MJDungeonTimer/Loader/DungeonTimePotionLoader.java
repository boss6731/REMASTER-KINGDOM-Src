package l1j.server.MJDungeonTimer.Loader;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.DungeonTimePotion;
import l1j.server.MJDungeonTimer.DungeonTimeUserInformation;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_CHARGED_MAP_TIME_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class DungeonTimePotionLoader {
	private static DungeonTimePotionLoader _instance;

	public static DungeonTimePotionLoader getInstance() {
		if (_instance == null)
			_instance = new DungeonTimePotionLoader();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		DungeonTimePotionLoader old = _instance;
		_instance = new DungeonTimePotionLoader();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	private HashMap<Integer, DungeonTimePotion> _potions;

	private DungeonTimePotionLoader() {
		_potions = load();
	}

	private HashMap<Integer, DungeonTimePotion> load() {
		HashMap<Integer, DungeonTimePotion> potions = new HashMap<Integer, DungeonTimePotion>(16);
		Selector.exec("select * from tb_dungeon_time_potion", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					DungeonTimePotion potion = DungeonTimePotion.newInstance(rs);
					potions.put(potion.get_item_id(), potion);
				}
			}
		});

		return potions;
	}

	public DungeonTimePotion get_potion(int i) {
		if (_potions.containsKey(i)) {
			return _potions.get(i);
		}
		return null;
	}

	public boolean use_potion(L1PcInstance pc, L1ItemInstance item) {
		DungeonTimePotion potion = _potions.get(item.getItemId());
		if (potion != null) {
			DungeonTimeInformation dtInfo15 = DungeonTimeInformationLoader.getInstance().from_timer_id(15);
			DungeonTimeProgress<?> progress15 = pc.get_progress(dtInfo15);
			DungeonTimeInformation dtInfo23 = DungeonTimeInformationLoader.getInstance().from_timer_id(23);
			DungeonTimeProgress<?> progress23 = pc.get_progress(dtInfo23);
			/*
			 * if (progress == null) {
			 * pc.sendPackets(String.format("在使用充值石之前，您必須至少進入地下城（%s）一次。",
			 * dtInfo.get_description()));
			 * return false;
			 * }
			 */
			if (potion.get_timer_id() == 15) {
				if (progress15 == null) {
					DungeonTimeUserInformation.newInstance().insert_dungeon_progress(pc, dtInfo15);
				}
				if (progress23 == null) {
					DungeonTimeUserInformation.newInstance().insert_dungeon_progress(pc, dtInfo23);
				}

				SC_CHARGED_MAP_TIME_INFO_NOTI.send(pc, item);
				return true;
			} else {
				potion.use_potion(pc, item);
				return true;
			}
		}
		return false;
	}

	public void dispose() {
		if (_potions != null) {
			_potions.clear();
			_potions = null;
		}
	}
}

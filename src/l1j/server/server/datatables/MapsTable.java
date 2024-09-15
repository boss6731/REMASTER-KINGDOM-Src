package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.SQLUtil;

public final class MapsTable {
	public class MapData {
		public String mapName = null;
		public int startX = 0;
		public int endX = 0;
		public int startY = 0;
		public int endY = 0;
		public double monster_amount = 1;
		public double dropRate = 1;
		public boolean isUnderwater = false;
		public boolean markable = false;
		public boolean teleportable = false;
		public boolean escapable = false;
		public boolean isUseResurrection = false;
		public boolean isUsePainwand = false;
		public boolean isEnabledDeathPenalty = false;
		public boolean isTakePets = false;
		public boolean isRecallPets = false;
		public boolean isUsableItem = false;
		public boolean isUsableSkill = false;
		public boolean is_ruler = false;
		public long monster_respawn_seconds = 40000L;
		public boolean PC_TEL = false;

		public String getMapName() {
			return mapName;
		}
		public int getStartX() {
			return startX;
		}
		public int getStartY() {
			return startY;
		}
		public int getEndX() {
			return endX;
		}
		public int getEndY() {
			return endY;
		}
		public double getMonster_amount() {
			return monster_amount;
		}
		public double getDropRate() {
			return dropRate;
		}
		public boolean isUnderwater() {
			return isUnderwater;
		}
		public boolean isMarkable() {
			return markable;
		}
		public boolean isTeleportable() {
			return teleportable;
		}
		public boolean isEscapable() {
			return escapable;
		}
		public boolean isUseResurrection() {
			return isUseResurrection;
		}
		public boolean isUsePainwand() {
			return isUsePainwand;
		}
		public boolean isEnabledDeathPenalty() {
			return isEnabledDeathPenalty;
		}
		public boolean isTakePets() {
			return isTakePets;
		}
		public boolean isRecallPets() {
			return isRecallPets;
		}
		public boolean isUsableItem() {
			return isUsableItem;
		}
		public boolean isUsableSkill() {
			return isUsableSkill;
		}

	}

	private static Logger _log = Logger.getLogger(MapsTable.class.getName());

	private static MapsTable _instance;

	/**
	 * Key為地圖ID，Value為傳送允許標誌的HashMap。
	 */
	private final Map<Integer, MapData> _maps = new HashMap<Integer, MapData>();

	/**
	 * 創建一個新的MapsTable對象，並讀取地圖的傳送允許標誌。
	 */
	private MapsTable() {
		loadMapsFromDatabase();
	}

	public static void reload() {
		MapsTable oldInstance = _instance;
		_instance = new MapsTable();
		oldInstance._maps.clear();
	}

	/**
	 * 從數據庫中讀取地圖的傳送允許標誌，並將其存儲在HashMap _maps中。
	 */
	private void loadMapsFromDatabase() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM mapids");
			MapData data = null;
			for (rs = pstm.executeQuery(); rs.next();) {
				data = new MapData();
				int mapId = rs.getInt("mapid");
				data.mapName = rs.getString("locationname");
				data.startX = rs.getInt("startX");
				data.endX = rs.getInt("endX");
				data.startY = rs.getInt("startY");
				data.endY = rs.getInt("endY");
				data.monster_amount = rs.getDouble("monster_amount");
				data.dropRate = rs.getDouble("drop_rate");
				data.isUnderwater = rs.getBoolean("underwater");
				data.markable = rs.getBoolean("markable");
				data.teleportable = rs.getBoolean("teleportable");
				data.escapable = rs.getBoolean("escapable");
				data.isUseResurrection = rs.getBoolean("resurrection");
				data.isUsePainwand = rs.getBoolean("painwand");
				data.isEnabledDeathPenalty = rs.getBoolean("penalty");
				data.isTakePets = rs.getBoolean("take_pets");
				data.isRecallPets = rs.getBoolean("recall_pets");
				data.isUsableItem = rs.getBoolean("usable_item");
				data.isUsableSkill = rs.getBoolean("usable_skill");
				data.is_ruler = rs.getBoolean("is_ruler");
				data.monster_respawn_seconds = rs.getLong("monster_respawn_second") * 1000L;
				data.PC_TEL = rs.getBoolean("PC_TEL");
				_maps.put(new Integer(mapId), data);
			}

			_log.config("Maps " + _maps.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 返回 MapsTable 的實例。
	 *
	 * @return MapsTable 的實例
	 */
	public static MapsTable getInstance() {
		if (_instance == null) {
			_instance = new MapsTable();
		}
		return _instance;
	}

	/**
	 * 返回地圖的X起始座標。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return X起始座標
	 */
	public int getStartX(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).startX;
	}

	/**
	 * 返回地圖的X結束座標。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return X結束座標
	 */
	public int getEndX(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).endX;
	}

	/**
	 * 返回地圖的Y起始座標。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return Y起始座標
	 */
	public int getStartY(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).startY;
	}

	/**
	 * 返回地圖的Y結束座標。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return Y結束座標
	 */
	public int getEndY(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return _maps.get(mapId).endY;
	}

	/**
	 * 返回地圖中怪物數量的倍數。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return 怪物數量的倍數
	 */
	public double getMonsterAmount(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return map.monster_amount;
	}

	/**
	 * 返回地圖的掉落倍數。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return 掉落倍數
	 */
	public double getDropRate(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return 0;
		}
		return map.dropRate;
	}

	/**
	 * 返回地圖是否在水下。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果在水下則為 true
	 */
	public boolean isUnderwater(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUnderwater;
	}

	/**
	 * 返回地圖是否可以加入書籤。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return 如果可以加入書籤則為 true
	 */
	public boolean isMarkable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).markable;
	}

	/**
	 * 返回地圖是否允許隨機傳送。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return 如果允許隨機傳送則為 true
	 */
	public boolean isTeleportable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).teleportable;
	}

	/**
	 * 返回地圖是否允許跨地圖傳送。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 * @return 如果允許跨地圖傳送則為 true
	 */
	public boolean isEscapable(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).escapable;
	}

	/**
	 * 返回地圖是否可以恢復。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果可以恢復則為 true
	 */
	public boolean isUseResurrection(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUseResurrection;
	}

	/**
	 * 返回地圖是否可以使用 Pine Juice 魔杖。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果可以使用 Pine Juice 魔杖則為 true
	 */
	public boolean isUsePainwand(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsePainwand;
	}

	/**
	 * 返回地圖是否有死亡懲罰。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果有死亡懲罰則為 true
	 */
	public boolean isEnabledDeathPenalty(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isEnabledDeathPenalty;
	}

	/**
	 * 返回地圖是否允許攜帶寵物和召喚獸。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果允許攜帶寵物和召喚獸則為 true
	 */
	public boolean isTakePets(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isTakePets;
	}

	/**
	 * 返回地圖是否可以召喚寵物和召喚獸。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果可以召喚寵物和召喚獸則為 true
	 */
	public boolean isRecallPets(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isRecallPets;
	}

	/**
	 * 返回地圖是否可以使用物品。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果可以使用物品則為 true
	 */
	public boolean isUsableItem(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsableItem;
	}

	/**
	 * 返回地圖是否可以使用技能。
	 *
	 * @param mapId
	 *            要查詢的地圖的地圖ID
	 *
	 * @return 如果可以使用技能則為 true
	 */
	public boolean isUsableSkill(int mapId) {
		MapData map = _maps.get(mapId);
		if (map == null) {
			return false;
		}
		return _maps.get(mapId).isUsableSkill;
	}

	public String getMapName(int mapId) {
		L1Map map = L1WorldMap.getInstance().getMap((short) mapId);
		MapData data = _maps.get(map == null ? mapId : map.getBaseMapId());
		if (data == null) {
			return null;
		}
		return data.mapName;
	}

	public boolean isRuler(int mapId) {
		MapData map = _maps.get(mapId);
		return map == null ? false : map.is_ruler;
	}

	public boolean isPCTEL(int mapId) {
		MapData map = _maps.get(mapId);
		return map == null ? false : map.PC_TEL;
	}

	public long get_monster_respawn_seconds(int mapId) {
		MapData map = _maps.get(mapId);
		return map == null ? 40000L : map.monster_respawn_seconds;
	}
	
	public Map<Integer, MapData> getMaps(){
		return _maps;
	}
	
	public MapData getMap(int mapId){
		return _maps.get(mapId);
	}
	
}

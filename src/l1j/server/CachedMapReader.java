package l1j.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.utils.FileUtil;

/**
 * 文本地圖的快取，以減少讀取時間。
 */
public class CachedMapReader extends MapReader {

	/** 文本地圖目錄。 */
	private static final String MAP_DIR = "./maps/";

	/** 快取地圖目錄。 */
	private static final String CACHE_DIR = "./data/mapcache/";

	/**
	 * 返回所有地圖ID的列表。
	 *
	 * @return ArrayList
	 */
	private ArrayList<Integer> listMapIds() {

		File mapDir = new File(MAP_DIR);
		File mapFile = null;
		ArrayList<Integer> ids = new ArrayList<Integer>(mapDir.list().length);
		for (String name : mapDir.list()) {
			mapFile = new File(mapDir, name);
			if (!mapFile.exists()) {
				continue;
			}
			if (!FileUtil.getExtension(mapFile).toLowerCase().equals("txt")) {
				continue;
			}
			int id = 0;
			try {
				String idStr = FileUtil.getNameWithoutExtension(mapFile);
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
				continue;
			}
			ids.add(id);
		}
		return ids;
	}

	/**
	 * 將指定地圖編號的文本地圖緩存為快取地圖。
	 *
	 * @param mapId
	 *            地圖編號
	 * @return L1V1Map
	 * @throws IOException
	 */
	private L1V1Map cacheMap(final int mapId) throws IOException {
		File file = new File(CACHE_DIR);
		if (!file.exists()) {
			file.mkdir();
		}

		L1V1Map map = (L1V1Map) new TextMapReader().read(mapId);

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(CACHE_DIR + mapId + ".map")));

		out.writeInt(map.getId());
		out.writeInt(map.getX());
		out.writeInt(map.getY());
		out.writeInt(map.getWidth());
		out.writeInt(map.getHeight());

		for (byte[] line : map.getRawTiles()) {
			for (byte tile : line) {
				out.writeByte(tile);
			}
		}
		out.flush();
		out.close();

		return map;
	}

	/**
	 * 讀取指定地圖編號的快取地圖。
	 *
	 * @param mapId
	 *            地圖編號
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int mapId) throws IOException {
		File file = new File(CACHE_DIR + mapId + ".map");
		if (!file.exists()) {
			return cacheMap(mapId);
		}

		DataInputStream in = new DataInputStream(new BufferedInputStream(
				new FileInputStream(CACHE_DIR + mapId + ".map")));

		int id = in.readInt();
		if (mapId != id) {
			extracted();
		}

		int xLoc = in.readInt();
		int yLoc = in.readInt();
		int width = in.readInt();
		int height = in.readInt();

		byte[][] tiles = new byte[width][height];
		for (byte[] line : tiles) {
			in.read(line);
		}

		in.close();
		L1V1Map map = new L1V1Map(id, tiles, xLoc, yLoc,
				MapsTable.getInstance().isUnderwater(mapId),
				MapsTable.getInstance().isMarkable(mapId),
				MapsTable.getInstance().isTeleportable(mapId),
				MapsTable.getInstance().isEscapable(mapId),
				MapsTable.getInstance().isUseResurrection(mapId),
				MapsTable.getInstance().isUsePainwand(mapId),
				MapsTable.getInstance().isEnabledDeathPenalty(mapId),
				MapsTable.getInstance().isTakePets(mapId),
				MapsTable.getInstance().isRecallPets(mapId),
				MapsTable.getInstance().isUsableItem(mapId),
				MapsTable.getInstance().isUsableSkill(mapId),
				MapsTable.getInstance().isRuler(mapId),
				MapsTable.getInstance().isPCTEL(mapId),
				false);
		return map;
	}

	private void extracted() throws FileNotFoundException {
		throw new FileNotFoundException();
	}

	/**
	 * 讀取所有文本地圖。
	 *
	 * @return Map
	 * @throws IOException
	 */
	@Override
	public Map<Integer, L1Map> read() throws IOException {
		Map<Integer, L1Map> maps = new HashMap<Integer, L1Map>();
		for (int id : listMapIds()) {
			maps.put(id, read(id));
		}
		return maps;
	}
}

package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.SQLUtil;

public class Dungeon {

	private static Logger _log = Logger.getLogger(Dungeon.class.getName());

	private static Dungeon _instance = null;

	private Map<String, NewDungeon> _dungeonMap = null;

	private enum DungeonType {
		NONE, SHIP_FOR_FI, SHIP_FOR_HEINE, SHIP_FOR_PI, SHIP_FOR_HIDDENDOCK, SHIP_FOR_GLUDIN, SHIP_FOR_TI
	};

	public static Dungeon getInstance() {
		if (_instance == null) {
			_instance = new Dungeon();
		}
		return _instance;
	}
	
	public static void reload() {
		Dungeon oldInstance = _instance;
		_instance = new Dungeon();
		oldInstance._dungeonMap.clear();
	}

	private Dungeon() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM dungeon");
			rs = pstm.executeQuery();
			_dungeonMap = new HashMap<String, NewDungeon>(SQLUtil.calcRows(rs));
			NewDungeon newDungeon = null;
			while (rs.next()) {
				int srcMapId = rs.getInt("src_mapid");
				int srcX = rs.getInt("src_x");
				int srcY = rs.getInt("src_y");
				String key = new StringBuilder().append(srcMapId).append(srcX).append(srcY).toString();
				int newX = rs.getInt("new_x");
				int newY = rs.getInt("new_y");
				int newMapId = rs.getInt("new_mapid");
				int heading = rs.getInt("new_heading");
				boolean Effect = rs.getBoolean("Effect");
				int min_lvl = rs.getInt("min_lvl");
				int max_lvl = rs.getInt("max_lvl");
				String Ment = rs.getString("Ment");
				int Effect_ID = rs.getInt("Effect_ID");
				
				DungeonType dungeonType = DungeonType.NONE;
				if ((srcX == 33423 || srcX == 33424 || srcX == 33425 || srcX == 33426) && srcY == 33502 && srcMapId == 4 // Heine碼頭->FI行的船
						|| (srcX == 32733 || srcX == 32734 || srcX == 32735 || srcX == 32736) && srcY == 32794 && srcMapId == 83) { // FI行的船->Heine碼頭
					dungeonType = DungeonType.SHIP_FOR_FI;
				} else if ((srcX == 32935 || srcX == 32936 || srcX == 32937) && srcY == 33058 && srcMapId == 70 // FI碼頭->Heine行的船
						|| (srcX == 32732 || srcX == 32733 || srcX == 32734 || srcX == 32735) && srcY == 32796 && srcMapId == 84) { // Heine行的船->FI碼頭
					dungeonType = DungeonType.SHIP_FOR_HEINE;
				} else if ((srcX == 32750 || srcX == 32751 || srcX == 32752) && srcY == 32874 && srcMapId == 445 // 隱藏碼頭->海盜島行的船
						|| (srcX == 32731 || srcX == 32732 || srcX == 32733) && srcY == 32796 && srcMapId == 447) { // 海盜島行的船->隱藏碼頭
					dungeonType = DungeonType.SHIP_FOR_PI;
				} else if ((srcX == 32296 || srcX == 32297 || srcX == 32298) && srcY == 33087 && srcMapId == 440 // 海盜島碼頭->隱藏碼頭行的船
						|| (srcX == 32735 || srcX == 32736 || srcX == 32737) && srcY == 32794 && srcMapId == 446) { // 隱藏碼頭行的船->海盜島碼頭
					dungeonType = DungeonType.SHIP_FOR_HIDDEN;
					dungeonType = DungeonType.SHIP_FOR_HIDDENDOCK;
				} else if ((srcX == 32630 || srcX == 32631 || srcX == 32632) && srcY == 32983 && srcMapId == 0 // TalkingIsland->TalkingIslandShiptoAdenMainland
						|| (srcX == 32733 || srcX == 32734 || srcX == 32735) && srcY == 32796 && srcMapId == 5) { // TalkingIslandShiptoAdenMainland->TalkingIsland
					dungeonType = DungeonType.SHIP_FOR_GLUDIN;
				} else if ((srcX == 32540 || srcX == 32542 || srcX == 32543 || srcX == 32544 || srcX == 32545) && srcY == 32728 && srcMapId == 4 // AdenMainland->AdenMainlandShiptoTalkingIsland
						|| (srcX == 32734 || srcX == 32735 || srcX == 32736 || srcX == 32737) && srcY == 32794 && srcMapId == 6) { // AdenMainlandShiptoTalkingIsland->AdenMainland
					dungeonType = DungeonType.SHIP_FOR_TI;
				}
				newDungeon = new NewDungeon(newX, newY, (short) newMapId, heading, Effect_ID, Effect, min_lvl, max_lvl, Ment, dungeonType);
				if (_dungeonMap.containsKey(key)) {
					_log.log(Level.WARNING, "存在相同鍵的地下城數據。鍵=" + key);
				}
				_dungeonMap.put(key, newDungeon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private static class NewDungeon {
		int _newX;
		int _newY;
		short _newMapId;
		int _heading;
		int _Effect_ID;
		boolean _Effect;
		int _min_lvl;
		int _max_lvl;
		String _Ment;
		DungeonType _dungeonType;

		private NewDungeon(int newX, int newY, short newMapId, int heading, int Effect_ID, boolean Effect, int min_lvl, int max_lvl, String Ment, DungeonType dungeonType) {
			_newX = newX;
			_newY = newY;
			_newMapId = newMapId;
			_heading = heading;
			_Effect = Effect;
			_Effect_ID = Effect_ID;
			_min_lvl = min_lvl;
			_max_lvl = max_lvl;
			_Ment	 = Ment;
			_dungeonType = dungeonType;

		}
	}

	public boolean dg(int locX, int locY, int mapId, L1PcInstance pc) {
		int servertime = GameTimeClock.getInstance().getGameTime().getSeconds();
		int nowtime = servertime % 86400;
		String key = new StringBuilder().append(mapId).append(locX).append(locY).toString();
		if (_dungeonMap.containsKey(key)) {
			NewDungeon newDungeon = _dungeonMap.get(key);
			short newMap = newDungeon._newMapId;
			int newX = newDungeon._newX;
			int newY = newDungeon._newY;
			int heading = newDungeon._heading;
			boolean Effect = newDungeon._Effect;
			int Effect_ID = newDungeon._Effect_ID;
			int min_lvl = newDungeon._min_lvl;
			int max_lvl = newDungeon._max_lvl;
			String Ment = newDungeon._Ment;
			DungeonType dungeonType = newDungeon._dungeonType;
			boolean teleportable = false;

			if (dungeonType == DungeonType.NONE) {
				teleportable = true;
			} else {
				if (nowtime >= 15 * 360
						&& nowtime < 25 * 360 // 1.30~2. 30
						|| nowtime >= 45 * 360
						&& nowtime < 55 * 360 // 4.30~5. 30
						|| nowtime >= 75 * 360
						&& nowtime < 85 * 360 // 7.30~8. 30
						|| nowtime >= 105 * 360
						&& nowtime < 115 * 360 // 10.30~11. 30
						|| nowtime >= 135 * 360 && nowtime < 145 * 360 || nowtime >= 165 * 360 && nowtime < 175 * 360 || nowtime >= 195 * 360 && nowtime < 205 * 360
						|| nowtime >= 225 * 360 && nowtime < 235 * 360) {
					if ((pc.getInventory().checkItem(40299, 1) && dungeonType == DungeonType.SHIP_FOR_GLUDIN) // TalkingIslandShiptoAdenMainland
							|| (pc.getInventory().checkItem(40301, 1) && dungeonType == DungeonType.SHIP_FOR_HEINE) // AdenMainlandShiptoForgottenIsland
							|| (pc.getInventory().checkItem(40302, 1) && dungeonType == DungeonType.SHIP_FOR_PI)) { // ShipPirateislandtoHiddendock
						teleportable = true;
					}
				} else if (nowtime >= 0 && nowtime < 360 || nowtime >= 30 * 360 && nowtime < 40 * 360 || nowtime >= 60 * 360 && nowtime < 70 * 360 || nowtime >= 90 * 360
						&& nowtime < 100 * 360 || nowtime >= 120 * 360 && nowtime < 130 * 360 || nowtime >= 150 * 360 && nowtime < 160 * 360 || nowtime >= 180 * 360
						&& nowtime < 190 * 360 || nowtime >= 210 * 360 && nowtime < 220 * 360) {
					if ((pc.getInventory().checkItem(40298, 1) && dungeonType == DungeonType.SHIP_FOR_TI) // AdenMainlandShiptoTalkingIsland
							|| (pc.getInventory().checkItem(40300, 1) && dungeonType == DungeonType.SHIP_FOR_FI) // ForgottenIslandShiptoAdenMainland
							|| (pc.getInventory().checkItem(40303, 1) && dungeonType == DungeonType.SHIP_FOR_HIDDENDOCK)) { // ShipHiddendocktoPirateisland
						teleportable = true;
					}
				}
			}
			
			if (Ment != null) {
				String message = String.format(Ment);
				String message2 = String.format(Ment);
				L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { new S_SystemMessage(message), new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2) });
			}
			if (pc.getLevel() >= min_lvl && pc.getLevel() <= max_lvl) {
				if (teleportable && newMap != 15492 && newMap != 15482) {
					pc.start_teleport(newX, newY, newMap, heading, Effect_ID, Effect, true);
					return true;
				} else {
//					EventTimeTemp ett = null;
//					ett = new EventTimeTemp();
//					if (ett.get_type() == 1) {
						pc.send_effect(Effect_ID, Effect);
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, newMap, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
						return true;
//					} else {
//						System.out.println("aaaaaa");
//						return false;
//					}
				}
			} else {
				pc.sendPackets("只有等級從 " + min_lvl + " 到 " + max_lvl + " 的角色可以進入。");
				return false;
			}
		}
		return false;
	}
}

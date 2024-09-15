package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.ElfWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.SpecialWarehouse;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.server.storage.CharacterStorage;
import l1j.server.server.server.storage.mysql.MySqlCharacterStorage;
import l1j.server.server.serverpackets.S_SystemMessage;

import l1j.server.server.templates.L1CharName;
import l1j.server.server.utils.SQLUtil;

public class CharacterTable {
	private CharacterStorage _charStorage;

	private static CharacterTable _instance;

	private static Logger _log = Logger.getLogger(CharacterTable.class.getName());

	private final Map<String, L1CharName> _charNameList =
			new ConcurrentHashMap<String, L1CharName>();

	private CharacterTable() {
		_charStorage = (CharacterStorage) new MySqlCharacterStorage();
	}

	public static CharacterTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterTable();
		}
		return _instance;
	}

	public void storeNewCharacter(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.createCharacter(pc);
			if (!_charNameList.containsKey(pc.getName())) {
				L1CharName cn = new L1CharName();
				cn.setName(pc.getName());
				cn.setId(pc.getId());
				_charNameList.put(pc.getName(), cn);
			}
			_log.finest("storeNewCharacter");
		}
	}

	public void updateCharacterAccount(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.updateAccountName(pc);
			_log.finest("updateCharacterAccount");
		}
	}

	/**
	 * 0705 修改為在命令提示符窗口中輸出異常錯誤
	 * @param pc
	 */
	public void storeCharacter(L1PcInstance pc) {
		synchronized (pc) {
			try {
				_charStorage.storeCharacter(pc);
				String name = pc.getName();
				if (!_charNameList.containsKey(name)) {
					L1CharName cn = new L1CharName();
					cn.setName(name);
					cn.setId(pc.getId());
					_charNameList.put(name, cn);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void deleteCharacter(String accountName, String charName)
			throws Exception {

		_charStorage.deleteCharacter(accountName, charName);
		if (_charNameList.containsKey(charName)) {
			_charNameList.remove(charName);
		}
		_log.finest("deleteCharacter");
	}

	public boolean isContainNameList(String name) {
		return _charNameList.containsKey(name);
	}
	
	public L1CharName getCharName(String name) {
		return _charNameList.get(name);
	}
	
	public void getChangeName(String name, String changeName) {
		L1CharName cn = _charNameList.get(name);
		L1CharName new_cn = new L1CharName();
		if(cn != null) {
			new_cn.setName(changeName);
			new_cn.setId(cn.getId());
			
			_charNameList.remove(name);
			_charNameList.put(changeName, new_cn);
		}
	}

	public L1PcInstance restoreCharacter(String charName) throws Exception {
		L1PcInstance pc = _charStorage.loadCharacter(charName);
		return pc;
	}

	public L1PcInstance loadCharacter(String charName) throws Exception {
		L1PcInstance pc = null;
		try {
			pc = restoreCharacter(charName);

			if (pc == null) {
				return null;
			}

			L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());

			if (!map.isInMap(pc.getX(), pc.getY())) {
				pc.setX(33087);
				pc.setY(33396);
				pc.setMap((short) 4);
			}
			_log.finest("加載角色: " + pc.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pc;

	}

	public static void clearOnlineStatus() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=0");
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void updateOnlineStatus(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=1, lastLoginTime=now() WHERE objid=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}


	public void restoreInventory(L1PcInstance pc) {
		pc.getInventory().loadItems();
		PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
		warehouse.getItems().clear();
		warehouse.loadItems();
		
		ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
		elfwarehouse.getItems().clear();
		elfwarehouse.loadItems();
		
		pc.getDwarfForPackageInventory().getItems().clear();
		pc.getDwarfForPackageInventory().loadItems();

		SupplementaryService supplementaryservice = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
		supplementaryservice.getItems().clear();
		supplementaryservice.loadItems();

		SpecialWarehouse specialwarehose = WarehouseManager.getInstance().getSpecialWarehouse(pc.getName());
		specialwarehose.getItems().clear();
		specialwarehose.loadItems();
	}

	public void loadAllCharName() {
		L1CharName cn = null;
		String name = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters");
			rs = pstm.executeQuery();
			while (rs.next()) {
				cn = new L1CharName();
				name = rs.getString("char_name");
				cn.setName(name);
				cn.setId(rs.getInt("objid"));
				_charNameList.put(name, cn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int PcLevelInDB(int pcid) {
		int result = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT level FROM characters WHERE objid=?");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			_log.warning("無法檢查現有角色名：" + e.getMessage());
			System.out.println("無法檢查現有角色名：" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public L1CharName[] getCharNameList() {
		return _charNameList.values().toArray(new L1CharName[_charNameList.size()]);
	}

	public void updateLoc(int castleid, int a, int b, int c, int d, int f) {
		Connection con = null;
		PreparedStatement pstm = null;
		int[] loc = new int[3];
		loc = L1CastleLocation.getGetBackLoc(castleid);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET LocX=?, LocY=?, MapID=? WHERE OnlineStatus=0 AND (MapID=? OR MapID=? OR MapID=? OR MapID=? OR MapID=?)");
			pstm.setInt(1, loc[0]);
			pstm.setInt(2, loc[1]);
			pstm.setInt(3, loc[2]);
			pstm.setInt(4, a);
			pstm.setInt(5, b);
			pstm.setInt(6, c);
			pstm.setInt(7, d);
			pstm.setInt(8, f);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void CharacterAccountCheck(L1PcInstance pc, String charName) {
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm2 = null;
		ResultSet loginRs = null;
		ResultSet characterRs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT login, password, CharPassword FROM accounts WHERE ip = ");
			sb.append("(SELECT ip FROM accounts WHERE login = ");
			sb.append("(SELECT account_name FROM characters WHERE char_name = ?))");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, charName);
			loginRs = pstm.executeQuery();
			
			while (loginRs.next()) {
				pstm2 = con.prepareStatement("SELECT char_name, level, highlevel, clanname, onlinestatus FROM characters WHERE account_name = ?");
				pstm2.setString(1, loginRs.getString("login"));
				characterRs = pstm2.executeQuery();

				pc.sendPackets(String.valueOf(new S_SystemMessage("-------------------------------------------------")));
				pc.sendPackets(String.valueOf(new S_SystemMessage("\fY帳號 : " + loginRs.getString("login") + ", 密碼 : " + loginRs.getString("password") + ", 角色密碼 : " + loginRs.getString("CharPassword"))));
				String onlineStatus;
				while (characterRs.next()) {
					onlineStatus = characterRs.getInt("onlinestatus") == 0 ? "" : "(在線中)";
					pc.sendPackets(String.valueOf(new S_SystemMessage("* " + characterRs.getString("char_name") + " (等級:" + characterRs.getInt("level") + ") (最高等級:" + characterRs.getInt("highlevel") + ") " + "(血盟:" + characterRs.getString("clanname") + ") " + "\fY" + onlineStatus)));

				}

				characterRs.close();
				pstm2.close();
			}
			pc.sendPackets(String.valueOf(new S_SystemMessage("-------------------------------------------------")));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(loginRs);
			SQLUtil.close(characterRs);
			SQLUtil.close(pstm);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}
	public void CharacterAccountCheck1(L1PcInstance pc, String charName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet loginRs = null;
		ResultSet characterRs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT login, password, CharPassword FROM accounts WHERE ip = ");
			sb.append("(SELECT account_name FROM characters WHERE char_name = ?)");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, charName);
			loginRs = pstm.executeQuery();
			
			while (loginRs.next()) {
				pstm = con.prepareStatement("SELECT char_name, level, highlevel, clanname, onlinestatus FROM characters WHERE account_name = ?");
				pstm.setString(1, loginRs.getString("login"));
				characterRs = pstm.executeQuery();
				pc.sendPackets(String.valueOf(new S_SystemMessage("\\aD-------------------------------------------------")));
				pc.sendPackets(String.valueOf(new S_SystemMessage("\\aH帳號: " + loginRs.getString("login") + ", 密碼: " + loginRs.getString("password") + ", 角色密碼: " + loginRs.getString("CharPassword") + ", IP: " + loginRs.getString("ip"))));
				String onlineStatus;
				while (characterRs.next()) {
					onlineStatus = characterRs.getInt("onlinestatus") == 0 ? "" : "(在線中)";
					pc.sendPackets(String.valueOf(new S_SystemMessage("* " + characterRs.getString("char_name") + " (等級:" + characterRs.getInt("level") + ") (最高等級:" + characterRs.getInt("highlevel") + ") " + "(血盟:" + characterRs.getString("clanname") + ") " + "\\aG" + onlineStatus)));
				}
				}	
				SQLUtil.close(characterRs);
			} catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        pc.sendPackets(String.valueOf(new S_SystemMessage("\\aD-------------------------------------------------")));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(characterRs);
			SQLUtil.close(loginRs);			
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
}

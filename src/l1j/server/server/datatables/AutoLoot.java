package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class AutoLoot {

	private static AutoLoot _instance;

	private static ArrayList<Integer> _idlist = new ArrayList<Integer>();

	public static AutoLoot getInstance() {
		if (_instance == null) {
			_instance = new AutoLoot();
		}
		return _instance;
	}

	private AutoLoot() {
		_idlist = allIdList();
	}

	public static void reload() {
		AutoLoot oldInstance = _instance;
		_instance = new AutoLoot();
		if (oldInstance != null);
	}
	
	private ArrayList<Integer> allIdList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		ArrayList<Integer> idlist = new ArrayList<Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from autoloot");
			rs = pstm.executeQuery();
			while (rs.next()) {
				idlist.add(rs.getInt("item_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return idlist;
	}

	public void storeId(int itemid) {
		int index = _idlist.indexOf(itemid);
		if (index != -1)
			return;

		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO autoloot SET item_id=?");
			pstm.setInt(1, itemid);
			pstm.execute();
			_idlist.add(itemid);
		} catch (Exception e) {
			Object NpcTable = new Object();
			NpcTable._log.log(Level.SEVERE, "AutoLoot[:storeId:]Error", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void deleteId(int itemid) {
		Connection con = null;
		PreparedStatement pstm = null;
		int index = _idlist.indexOf(itemid);
		if (index == -1)
			return;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM autoloot WHERE item_id=?");
			pstm.setInt(1, itemid);
			pstm.execute();
			_idlist.remove(index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void reload1() {
		_idlist.clear();
		_idlist = allIdList();
	}

	public ArrayList<Integer> getIdList() {
		return _idlist;
	}

	public boolean isAutoLoot(int itemId) {
		for (int id : _idlist) {
			if (itemId == id) {
				return true;
			}
		}
		return false;
	}
}

package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class NoTradable {

	private static NoTradable _instance;

	private static ArrayList<Integer> _idlist = new ArrayList<Integer>();

	public static NoTradable getInstance() {
		if (_instance == null) {
			_instance = new NoTradable();
		}
		return _instance;
	}

	private NoTradable() {
		_idlist = allIdList();
	}

	private ArrayList<Integer> allIdList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		ArrayList<Integer> idlist = new ArrayList<Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from NoTradable");
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
			pstm = con.prepareStatement("INSERT INTO NoTradable SET item_id=?");
			pstm.setInt(1, itemid);
			pstm.execute();
			_idlist.add(itemid);
		} catch (Exception e) {
			e.printStackTrace();
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
			pstm = con.prepareStatement("DELETE FROM NoTradable WHERE item_id=?");
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

	public void reload() {
		_idlist.clear();
		_idlist = allIdList();
	}

	public ArrayList<Integer> getIdList() {
		return _idlist;
	}

	public boolean isNoTradable(int itemId) {
		for (int id : _idlist) {
			if (itemId == id) {
				return true;
			}
		}
		return false;
	}
}

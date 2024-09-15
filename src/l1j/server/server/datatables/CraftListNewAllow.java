package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class CraftListNewAllow {

	private static CraftListNewAllow _instance;

	private ArrayList<Integer> _idlist = new ArrayList<Integer>();

	public static CraftListNewAllow getInstance() {
		if (_instance == null) {
			_instance = new CraftListNewAllow();
		}
		return _instance;
	}

	// 重載格式如下所示建立源碼
	public void reload() {
		CraftListNewAllow old = _instance;
		_instance = new CraftListNewAllow();
		old._idlist.clear();
		old = null;
	}

	private CraftListNewAllow() {
		_idlist = allIdList();
	}

	private ArrayList<Integer> allIdList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		ArrayList<Integer> idlist = new ArrayList<Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from craftlist_new_allow");
			rs = pstm.executeQuery();
			while (rs.next()) {
				idlist.add(rs.getInt("craft_id"));
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

	public boolean isCraft(int craft_id) {
		for (int id : _idlist) {
			if (craft_id == id) {
				return true;
			}
		}
		return false;
	}
}

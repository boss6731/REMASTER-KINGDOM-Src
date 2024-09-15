package l1j.server.server.model.item.collection.favor.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.cj.protocol.Resultset;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.utils.SQLUtil;

public class FavorBookCraftLoader {
	
	private static FavorBookCraftLoader _instance;
	
	private ArrayList<Integer> _favorCraft;
	
	public static FavorBookCraftLoader getInstance() {
		if (_instance == null) {
			_instance = new FavorBookCraftLoader();
		}
		return _instance;
	}
	private int _craftid;
	
	public void reload() {
		FavorBookCraftLoader old = _instance;
		_instance = new FavorBookCraftLoader();
		old._favorCraft.clear();
		old = null;
	}
	
	private FavorBookCraftLoader() {
		_favorCraft = load();
	}

	
	public ArrayList<Integer> load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "SELECT * FROM favorbook_craftlist";
			pstm = con.prepareStatement(sqlstr);
			rs = pstm.executeQuery();
			while(rs.next()) {
				list.add(rs.getInt("craft_id"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return list;
	}
	
	public boolean isFavorCraft(int craft_id) {
		for(int id : _favorCraft) {
			if (craft_id == id) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList getFavorCraft() {
		return _favorCraft;
	}
	
	

}

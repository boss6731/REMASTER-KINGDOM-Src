package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.L1Clan_BanList;
import l1j.server.server.utils.SQLUtil;

public class ClanBanListTable {
	private static ClanBanListTable _instance;
	
	public static ClanBanListTable getInstance() {
		if (_instance == null) {
			_instance = new ClanBanListTable();
		}
		return _instance;
	}
	private Map<String, ArrayList<String>> _clan_banlist = new HashMap<String, ArrayList<String>>();
	private Map<String, Integer> _clan_limit_level = new HashMap<String, Integer>();
	
	private ClanBanListTable() {
		loadClanBanList();
	}
	public void reload() {
		
	}
	
	private void loadClanBanList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			ArrayList<String> banlistarray = new ArrayList<String>();
			String bandb ="";
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_ban");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1Clan_BanList banlist = new L1Clan_BanList();
				banlist.set_ClanName(rs.getString("clan_name"));
				banlist.set_LimitLevel(rs.getInt("limit_level"));
				bandb = rs.getString("clan_ban_data");
				if (bandb != null) {
					String[] array = bandb.split(",");
					for (int i = 0; i < array.length; i++) {
						banlistarray.add(array[i]);
					}
					banlist.setBanlist(banlistarray);
				}
				_clan_banlist.put(banlist.get_ClanName(), banlist.getBanList());
				_clan_limit_level.put(banlist.get_ClanName(), banlist.get_LimitLevel());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	public int getLimitLevel(String clanname) {
		if (_clan_limit_level.get(clanname) != null) {
			return _clan_limit_level.get(clanname);
		}
		return 0;
	}
	
	public void SetLimitLevel(String clanname, int level) {
		if (_clan_limit_level.get(clanname) != null) {
			_clan_limit_level.remove(clanname);
			
		} else {
//			System.out.println("確");
			if (_clan_banlist.get(clanname) == null) {
				_clan_banlist.put(clanname, null);
				insertClanBanList(clanname);
			}
		}
		_clan_limit_level.put(clanname, level);
		uploadClanLimitLevel(clanname);
	}

	public void updateClanBanlist(String clanname, String name) {
		if (!checkClanBanlist(clanname, name)) {
			if (_clan_banlist.get(clanname) == null) {
				ArrayList<String> array = new ArrayList<String>();
				array.add(name);
				_clan_banlist.put(clanname, array);
			} else {
			_clan_banlist.get(clanname).add(name);
			}
		}
		uploadClanBanList(clanname);
	}
	
	public ArrayList<String> getBanList(String clanname){
		if (_clan_banlist.get(clanname) != null) {
			return _clan_banlist.get(clanname);
		}else {
			return null;
		}
		
	}
	
	public void deleteClanBanlist(String clanname, String name) {
		if (checkClanBanlist(clanname, name)) {
			if (_clan_banlist.get(clanname) == null) {
				_clan_banlist.get(clanname).remove(name);
			} 
		}
		uploadClanBanList(clanname);
	}
	
	public boolean checkClanBanlist(String clanname, String name) {
		if (_clan_banlist.get(clanname) == null) {
			if (_clan_limit_level.get(clanname) == null) {
				insertClanBanList(clanname);
				_clan_limit_level.put(clanname, 30);
			}
			return false;
		} else {
			String[] list = _clan_banlist.get(clanname).toArray(new String[_clan_banlist.get(clanname).size()]);
			boolean contains = Arrays.stream(list).anyMatch(x -> x.equalsIgnoreCase(name));
			return contains;
		}
	}

	
	public void uploadClanBanList(String clanname) {
		StringBuilder list = new StringBuilder();
		String comma = "";
		for (String g: _clan_banlist.get(clanname)) {
			list.append(comma);
			list.append(g);
			comma = ",";
		}
		Updator.exec("update clan_ban set clan_ban_data=?, limit_level=? where clan_name=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, list.toString());
				pstm.setInt(++idx, _clan_limit_level.get(clanname));
				pstm.setString(++idx, clanname);
				
			}
		});
	}
	
	public void uploadClanLimitLevel(String clanname) {
//		System.out.println("確認");
		Updator.exec("update clan_ban set limit_level=? where clan_name=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, _clan_limit_level.get(clanname));
				pstm.setString(++idx, clanname);
				
			}
		});
	}
	
	public void insertClanBanList(String clanname) {
		Updator.exec("insert into clan_ban set clan_name =?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, clanname);
			}
		});
	}

	
	
}

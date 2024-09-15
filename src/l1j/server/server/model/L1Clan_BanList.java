package l1j.server.server.server.model;

import java.util.ArrayList;

public class L1Clan_BanList {
	private String _clanname;
	ArrayList<String> banlist = new ArrayList<String>();
	private int _limit_level;
	
	public void set_ClanName(String clanname) {
		_clanname = clanname;
	}
	public String get_ClanName() {
		return _clanname;
	}
	
	public void add_Banlist(String name) {
		banlist.add(name);
	}
	public void del_Banlist(String name) {
		banlist.remove(name);
	}
	public boolean isBanlist(String name) {
		if (banlist.contains(name)) {
			return true;
		}
		return false;
	}
	public void setBanlist (ArrayList<String> list) {
		banlist = list;
	}
	public ArrayList<String> getBanList(){
		return banlist;
	}
	public void set_LimitLevel(int level) {
		_limit_level = level;
	}
	public int get_LimitLevel() {
		return _limit_level;
	}
	
	
	
	
}

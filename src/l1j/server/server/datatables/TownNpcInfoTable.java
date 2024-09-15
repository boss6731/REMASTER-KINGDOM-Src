package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.templates.L1TownNpcInfo;
import l1j.server.server.utils.SQLUtil;

public class TownNpcInfoTable {
	
	public static TownNpcInfoTable _instance;
	
	public Map<String, L1TownNpcInfo> _list = new HashMap<String, L1TownNpcInfo>();
	
	public static TownNpcInfoTable getInstance() {
		if (_instance == null) {
			_instance = new TownNpcInfoTable();
		}
		return _instance;
	}
	
	public static void reload() {
		TownNpcInfoTable oldInstance = _instance;
		_instance = new TownNpcInfoTable();
		oldInstance._list.clear();
	}
	
	private TownNpcInfoTable(){
		loadTownNpcName();
	}
	
	private void loadTownNpcName(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM town_npc_info");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1TownNpcInfo TN = new L1TownNpcInfo();
				TN.setNpcId(rs.getInt("npc_id"));
				TN.setNpcName(rs.getString("npc_name"));
				TN.setSprId(rs.getInt("sprite_id"));
				TN.setTownId(parseTown(rs.getString("town_id")));
				String key = new StringBuilder().append(TN.getNpcId()).append(":").append(TN.getTownId()).toString();
				_list.put(key, TN);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public L1TownNpcInfo getTownNpcInfo(String npcid){
		return _list.get(npcid);
	}
	
	public boolean isTownNpcInfo(String npcid){
		Set<String> keys = _list.keySet();
		String townnpc;
		boolean OK = false;
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			townnpc = iterator.next();
			if(townnpc.equalsIgnoreCase(npcid)){
				OK = true;
				break;
			}
		}
		return OK;
	}
	
	private int parseTown(String type){
		if(type.equalsIgnoreCase("TalkingIsland"))
			return L1TownLocation.TOWNID_TALKING_ISLAND;
		else if(type.equalsIgnoreCase("SilverKnight"))
			return L1TownLocation.TOWNID_SILVER_KNIGHT_TOWN;
		else if(type.equalsIgnoreCase("Gludio"))
			return L1TownLocation.TOWNID_GLUDIO;
		else if(type.equalsIgnoreCase("OrcForest"))
			return L1TownLocation.TOWNID_ORCISH_FOREST;
		else if(type.equalsIgnoreCase("Windawood"))
			return L1TownLocation.TOWNID_WINDAWOOD;
		else if(type.equalsIgnoreCase("Kent"))
			return L1TownLocation.TOWNID_KENT;
		else if(type.equalsIgnoreCase("Giran"))
			return L1TownLocation.TOWNID_GIRAN;
		else if(type.equalsIgnoreCase("Heine"))
			return L1TownLocation.TOWNID_HEINE;
		else if(type.equalsIgnoreCase("Weldern"))
			return L1TownLocation.TOWNID_WERLDAN;
		else if(type.equalsIgnoreCase("Oren"))
			return L1TownLocation.TOWNID_OREN;
		else if(type.equalsIgnoreCase("Aden"))
			return L1TownLocation.TOWNID_ADEN;
		return 0;
	}

}
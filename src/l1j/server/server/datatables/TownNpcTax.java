package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.utils.SQLUtil;

public class TownNpcTax {
	
	public static TownNpcTax _instance;
	
	public Map<Integer, L1TownNpcTax> _list = new HashMap<Integer, L1TownNpcTax>();
	
	public static TownNpcTax getInstance() {
		if (_instance == null) {
			_instance = new TownNpcTax();
		}
		return _instance;
	}
	
	public static void reload() {
		TownNpcTax oldInstance = _instance;
		_instance = new TownNpcTax();
		oldInstance._list.clear();
	}
	
	private TownNpcTax(){
		loadTownNpcName();
	}
	
	private void loadTownNpcName(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM town_npc_tax");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1TownNpcTax TN = new L1TownNpcTax();
				TN.setNpcId(rs.getInt("npc_id"));
				TN.setTownId(parseTown(rs.getString("town_id")));
				_list.put(TN.getNpcId(), TN);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public L1TownNpcTax getTownNpcInfo(int npcid){
		return _list.get(npcid);
	}
	
	public boolean getTownNpcTaxCheck(int npcid){
		_list.get(npcid);
		if (_list != null)
			return true;
		return false;
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
	
	public class L1TownNpcTax {
		private int _id;
		private int _npcid;
		private int _townid;
		
		public int getId(){
			return _id;
		}
		
		public void setId(int id){
			_id = id;
		}
		
		public int getNpcId(){
			return _npcid;
		}
		
		public void setNpcId(int id){
			_npcid = id;
		}
		
		public int getTownId(){
			return _townid;
		}
		
		public void setTownId(int town){
			_townid = town;
		}

	}

}
package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1NCoinMonster;
import l1j.server.server.utils.SQLUtil;

public class NCoinGiveMonsterTable {
	
	public static NCoinGiveMonsterTable _instance;
	
	public Map<Integer, L1NCoinMonster> _list = new HashMap<Integer, L1NCoinMonster>();
	
	public static NCoinGiveMonsterTable getInstance() {
		if (_instance == null) {
			_instance = new NCoinGiveMonsterTable();
		}
		return _instance;
	}
	
	public static void reload() {
		NCoinGiveMonsterTable oldInstance = _instance;
		_instance = new NCoinGiveMonsterTable();
		oldInstance._list.clear();
	}
	
	private NCoinGiveMonsterTable(){
		loadGiveMonster();
	}
	
	private void loadGiveMonster(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ncoin_give_monster");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1NCoinMonster CM = new L1NCoinMonster();
				int npcid = rs.getInt("NPC_ID");
				CM.setNpcName(rs.getString("NPC_名稱"));
				CM.setNCoin(rs.getInt("給予的N幣"));
				CM.setEffectNum(rs.getInt("效果編號"));
				CM.setAllEffect(rs.getInt("全部顯示效果") == 1 ? true : false); // 1表示全部顯示，0表示僅自己顯示
				CM.setMent(rs.getInt("訊息狀態") == 1 ? true : false);
				CM.setGiveItem(rs.getInt("給予物品狀態") == 1 ? true : false);
				CM.setItemId(rs.getInt("物品編號"));
				CM.setItemCount(rs.getInt("物品數量"));
				_list.put(npcid, CM);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public L1NCoinMonster getNCoinGiveMonster(int npcid){
		return _list.get(npcid);
	}
	
	public boolean isNCoinMonster(int npcid){
		Set<Integer> keys = _list.keySet();
		int givemon;
		boolean OK = false;
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			givemon = iterator.next();
			if(givemon == npcid){
				OK = true;
				break;
			}
		}
		return OK;
	}
}
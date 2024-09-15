/*package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.mchange.v1.db.sql.SqlUtils;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1QuestScroll;
import l1j.server.server.utils.SQLUtil;

public class QuestScrollTable {
	private static QuestScrollTable _instance;
	private final HashMap<Integer, ArrayList<L1QuestScroll>> _droplists;

	public static QuestScrollTable getInstance() {
		if(_instance == null) {
			_instance = new QuestScrollTable();
		}
		return _instance;
	}

	private QuestScrollTable() {
		_droplists = allDropList();
	}

	public static void reload() {
		QuestScrollTable oldInstance = _instance;
		_instance = new QuestScrollTable();
		oldInstance._droplists.clear();
	}

	public ArrayList<L1QuestScroll> getDropList(int mobId){
		return _droplists.get(mobId);
	}

	public boolean isDropListItem(int mobId, int itemId) {
		ArrayList<L1QuestScroll> drop = getDropList(mobId);
		for (L1QuestScroll d : drop) {
			if (d.getItemId() == itemId) {
				return true;
			}
		}
		return false;
	}

	public L1QuestScroll getDrop(int mobId, int itemId) {
		ArrayList<L1QuestScroll> drop = getDropList(mobId);
		for (L1QuestScroll d : drop) {
			if (d.getItemId() == itemId) {
				return d;
			}
		}
		return null;
	}

	private HashMap<Integer, ArrayList<L1QuestScroll>> allDropList(){
		HashMap<Integer, ArrayList<L1QuestScroll>> droplistMap = new HashMap<Integer, ArrayList<L1QuestScroll>>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from questscroll");
			rs = pstm.executeQuery();
			L1QuestScroll drop = null;

			while(rs.next()) {
				int scrollId = rs.getInt("scrollId");
				int mobId = rs.getInt("mobId");
				int itemId = rs.getInt("itemId");
				int chance = rs.getInt("chance");

				drop = new L1QuestScroll(scrollId, mobId, itemId, chance);
				L1Npc npc = NpcTable.getInstance().getTemplate(mobId);
				if (npc == null) {
					System.out.println(String.format("[QuestScroll]：NPC表中沒有 npcid 為 %s 的怪物。", mobId ));
					continue;
				}

				L1Item items = ItemTable.getInstance().getTemplate(itemId);
				if (items == null) {
					System.out.println(String.format("[QuestScroll]：item_id: %s 是資料庫中沒有的物品。", itemId ));
					continue;
				}

				ArrayList<L1QuestScroll> dropList = droplistMap.get(drop.getScrollId());
				if (dropList == null) {
					dropList = new ArrayList<L1QuestScroll>();
					droplistMap.put(new Integer(drop.getScrollId()), dropList);
				}
				dropList.add(drop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return droplistMap;
	}

	public void setDrop(L1PcInstance pc, L1NpcInstance npc, L1Inventory inventory) {
		try {
			if (Config.Login.StandbyServer) {
				return;
			}
			int mobId = npc.getNpcTemplate().get_npcId();

			ArrayList<L1QuestScroll> scrollinfo = _droplists.get();





		} catch(Exception e) {
			e.printStackTrace();
		}

	}



}
*/
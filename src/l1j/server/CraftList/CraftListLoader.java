package l1j.server.CraftList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParsee;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.CraftIdList;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.eCraftIdListReqResultType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.eCraftNpcType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_LIMITED_CRAFT_INFO_ACK;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.Instance.L1PcInstance;

public class CraftListLoader {
	private static CraftListLoader _instance;

	public static CraftListLoader getInstance() {
		if (_instance == null) {
			_instance = new CraftListLoader();
		}
		return _instance;
	}

	private void doCraftItemListCaches() {
		try {
			if (_craftItemListcaches == null) {
				_craftItemListcaches = new ArrayList<ProtoOutputStream>(20);
			}
			_craftItemListcaches.clear();
			for (int craftId : _craft_item_list.keySet()) {
				SC_LIMITED_CRAFT_INFO_ACK ack = SC_LIMITED_CRAFT_INFO_ACK.newInstance();

				SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo cii = SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo.newInstance();

				CraftItemInfo limit = getCraftId(craftId);

				int max_count = 0;
				int current_count = 0;
				if (limit != null) {
					max_count = limit.get_craft_max_count();
					current_count = limit.get_craft_current_count();
				}

				cii.set_craft_id(craftId);
				cii.set_max_success_cnt(max_count);
				cii.set_cur_success_cnt(current_count);

				// System.out.println("craftId:" + craftId);
				// System.out.println("max_count:" + max_count);
				// System.out.println("current_count:" + current_count);
				// System.out.println("_craft_item_list.keySet(): "+_craft_item_list.keySet());

				SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType result = SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType.RP_SUCCESS;

				// SC_CRAFT_LIST_ALL_ACK.Craft craft = SC_CRAFT_LIST_ALL_ACK.getCraft(craftId);
				// if (craft == null) {
				// result =
				// SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType.RP_ERROR_CRAFT_ID;
				// }

				ack.set_eResult(result);
				ack.set_craft_id_info(cii);
				_craftItemListcaches.add(SC_LIMITED_CRAFT_INFO_ACK.doMakeCreateProto(ack));

				// 添加
				ack.dispose();
				ack = null;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reloadSend(L1PcInstance pc) {
		try {
			for (ProtoOutputStream stream : _craftItemListcaches) {
				pc.sendPackets(stream, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		CraftListLoader oldInstance = _instance;
		_instance = new CraftListLoader();
		oldInstance._craft_npc_list.clear();
		oldInstance._craft_item_list.clear();
		oldInstance._craftItemListcaches.clear();
		oldInstance = null;
	}

	private HashMap<Integer, CraftNpcInfo> _craft_npc_list = new HashMap<Integer, CraftNpcInfo>(256);
	private HashMap<Integer, CraftItemInfo> _craft_item_list = new HashMap<Integer, CraftItemInfo>(256);
	private ArrayList<ProtoOutputStream> _craftItemListcaches = new ArrayList<ProtoOutputStream>(20);

	private CraftListLoader() {
		load();
		doCraftItemListCaches();
	}

	private void load() {
		try {
			Selector.exec("select * from craftlist", new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					while (rs.next()) {
						CraftNpcInfo pInfo = CraftNpcInfo.newInstance(rs);
						if (pInfo == null)
							continue;
						_craft_npc_list.put(pInfo.get_npcid(), pInfo);
					}
				}
			});

			Selector.exec("select * from craftlist_limit_item", new FullSelectorHandler() {
				@Override
				public void result(ResultSet rs) throws Exception {
					while (rs.next()) {
						CraftItemInfo pInfo = CraftItemInfo.newInstance(rs);
						if (pInfo == null)
							continue;
						_craft_item_list.put(pInfo.get_craft_id(), pInfo);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLimitItem(int craftid, int current_count) {
		Updator.exec("update craftlist_limit_item set craft_id=?, buy_current_count=? where craft_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, craftid);
				pstm.setInt(++idx, current_count);
				pstm.setInt(++idx, craftid);
			}
		});
	}

	public void updateLimitItem() {
		Updator.exec("update craftlist_limit_item set buy_current_count=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, 0);
			}
		});
	}

	public CraftNpcInfo getCraftNpc(int npcid) {
		return _craft_npc_list.get(npcid);
	}

	public CraftItemInfo getCraftId(int craftid) {
		return _craft_item_list.get(craftid);
	}

	public static ProtoOutputStream getNpcCraftList(int npcid) {
		ProtoOutputStream stream = null;
		try {
			MJArrangeParsee<Integer> parsee = MJArrangeParseeFactory.createIntArrange();
			CraftNpcInfo cInfo = CraftListLoader.getInstance().getCraftNpc(npcid);
			if (cInfo != null) {
				SC_CRAFT_ID_LIST_ACK ack = (SC_CRAFT_ID_LIST_ACK) MJEProtoMessages.SC_CRAFT_ID_LIST_ACK.copyInstance();
				ack.set_eResult(eCraftIdListReqResultType.RP_SUCCESS);
				ack.set_eBlindType(0);
				ack.set_npc_type(eCraftNpcType.DEFAULT);

				if (cInfo.get_craft_list() != null && cInfo.get_craft_list().length() > 1) {
					Integer[] ids = (Integer[]) MJArrangeParser.parsing(cInfo.get_craft_list(), ",", parsee).result();
					for (Integer i : ids) {
						CraftIdList idList = CraftIdList.newInstance();
						idList.set_craft_id(i);
						CraftItemInfo limit = CraftListLoader.getInstance().getCraftId(i);
						if (limit != null) {
							idList.set_cur_success_cnt(limit.get_craft_current_count());
							idList.set_max_success_cnt(limit.get_craft_max_count());
						} else {
							idList.set_cur_success_cnt(0);
							idList.set_max_success_cnt(0);
						}
						ack.add_craft_id_list(idList);
					}
				}
				stream = ack.writeTo(MJEProtoMessages.SC_CRAFT_ID_LIST_ACK);
				ack.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stream;
	}
}

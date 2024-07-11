package l1j.server.CPMWBQSystem;

import l1j.server.CPMWBQSystem.Database.CPMWBQmapInfoTable;
import l1j.server.CPMWBQSystem.Database.CPMWBQNpcInfoTable;
import l1j.server.CPMWBQSystem.Database.CPMWBQUserTable;
import l1j.server.CPMWBQSystem.info.CPMWBQReward;
import l1j.server.CPMWBQSystem.info.CPMWBQinfo;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2RewardGrade;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_GUIDE_BOOK_MAP_INFO_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_QUEST_MAP_SELECT_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_QUEST_REWARD_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.CS_HUNTING_TELEPORT_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.HUNTING_QUEST_MAP_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_QUEST_MAP_LIST_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest.SC_HUNTING_QUEST_MAP_UPDATE_AMOUNT_NOTI;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.MJCommons;

public class CPMWBQSystemProvider {

	private static final CPMWBQSystemProvider provider = new CPMWBQSystemProvider();

	public static CPMWBQSystemProvider provider() {
		return provider;
	}

	private CPMWBQSystemProvider() {
	}

	public void BQload(L1PcInstance pc) {
		if (!CPMWBQReward.service().use()) {
			bqinfo(pc);
			return;
		}
		SC_HUNTING_QUEST_MAP_LIST_NOTI noti = SC_HUNTING_QUEST_MAP_LIST_NOTI.newInstance();
		HUNTING_QUEST_MAP_INFO mapinfo = HUNTING_QUEST_MAP_INFO.newInstance();
		if (!In_User_Info(pc, noti)) {
			return;
		}
		bookinfo(pc, mapinfo, noti);
		noti.set_remain_quest_count(remainBQ(pc));
		pc.sendPackets(noti, MJEProtoMessages.SC_HUNTING_QUEST_MAP_LIST_NOTI);
		dipose(noti, mapinfo);
	}

	public void bqinfo(L1PcInstance pc) {
		SC_HUNTING_QUEST_MAP_LIST_NOTI noti = SC_HUNTING_QUEST_MAP_LIST_NOTI.newInstance();
		for (int i = 0; i < 3; i++) {
			HUNTING_QUEST_MAP_INFO mapinfo = HUNTING_QUEST_MAP_INFO.newInstance();
			mapinfo.set_map_number(10 + i);
			mapinfo.set_location_desc(0);
			mapinfo.set_kill_count(0);
			mapinfo.set_is_complete(true);
			noti.add_hunting_quest_map_list(mapinfo);
		}
		noti.set_remain_quest_count(150);
		pc.sendPackets(noti, MJEProtoMessages.SC_HUNTING_QUEST_MAP_LIST_NOTI);
	}

	public void BQMapInfo(L1PcInstance pc, CS_HUNTING_GUIDE_BOOK_MAP_INFO_REQ req) {
		if (!CPMWBQReward.service().use()) {
			return;
		}
		SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK ack = SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK.newInstance();
		if (req.get_map_number() < 0)
			return;
		String mapname = CPMWBQmapInfoTable.getMapInfo(req.get_map_number());
		ack.set_map_number(req.get_map_number());
		if (mapname == null) {
			ack.set_is_can_random_teleport(false);
		} else if (CPMWBQmapInfoTable.Get_Map_Info(mapname).isTeleport()) {
			ack.set_is_can_random_teleport(true);
		} else {
			ack.set_is_can_random_teleport(false);
		}
		if (mapname != null && CPMWBQmapInfoTable.Get_Map_Info(mapname).limitTime() != 0) {
			ack.set_map_limit_time(60 * CPMWBQmapInfoTable.Get_Map_Info(mapname).limitTime());
		}

		pc.sendPackets(ack, MJEProtoMessages.SC_HUNTING_GUIDE_BOOK_MAP_INFO_ACK);
		if (ack != null) {
			ack.dispose();
		}
	}

	public void BQTeleport(L1PcInstance pc, CS_HUNTING_TELEPORT_REQ req) {
		if (!CPMWBQReward.service().use()) {
			return;
		}
		if (req.get_action_string() == null) {
			return;
		}
		String mapname = CPMWBQmapInfoTable.getMapInfo(req.get_action_string());
		if (mapname == null) {
			pc.sendPackets("目前無法移動到該地圖。");
			return;
		}
		if (CPMWBQmapInfoTable.Get_Map_Info(mapname).costItem() == 0) {
			pc.sendPackets("目前無法移動到該地圖。");
			return;
		}
		if (!CPMWBQmapInfoTable.Get_Map_Info(mapname).isTeleport()) {
			pc.sendPackets("目前無法移動到該地圖。");
			return;
		}
		if (MJCommons.islockteleport(pc)) {
			pc.sendPackets("不可在這個狀態下使用");
			return;
		}
		if (pc.getInventory().checkItem(CPMWBQmapInfoTable.Get_Map_Info(mapname).costItem())) {
			pc.getInventory().consumeItem(CPMWBQmapInfoTable.Get_Map_Info(mapname).costItem(), 1);
			pc.start_teleport(CPMWBQmapInfoTable.Get_Map_Info(mapname).locX(),
					CPMWBQmapInfoTable.Get_Map_Info(mapname).locY(),
					CPMWBQmapInfoTable.Get_Map_Info(mapname).LocMapid(), pc.getHeading(), 169, true, false);
		} else {
			pc.sendPackets("進入所需的條件不符。");
		}
	}

	public void BQdelete(L1PcInstance pc, CS_HUNTING_QUEST_MAP_SELECT_REQ req) {
		if (!CPMWBQReward.service().use()) {
			return;
		}
		SC_HUNTING_QUEST_MAP_LIST_NOTI noti = SC_HUNTING_QUEST_MAP_LIST_NOTI.newInstance();
		HUNTING_QUEST_MAP_INFO mapinfo = HUNTING_QUEST_MAP_INFO.newInstance();
		CPMWBQUserTable Uinfo = CPMWBQUserTable.getInstance();
		if (req.get_is_select() == true) {
			sendmessage(pc, 9999);
			return;
		}
		if (pc.Get_BQ_Size() <= 0) {
			sendmessage(pc, 9999);
			return;
		}
		if (!InBookinfo(pc, req, true)) {
			sendmessage(pc, 9999);
			return;
		}
		bookinfo(pc, mapinfo, noti);
		Uinfo.DeleteInfo(pc, req.get_map_number(), req.get_location_desc());
		noti.set_remain_quest_count(remainBQ(pc));
		pc.sendPackets(noti, MJEProtoMessages.SC_HUNTING_QUEST_MAP_LIST_NOTI);
		dipose(noti, mapinfo);
	}

	public void BQselect(L1PcInstance pc, CS_HUNTING_QUEST_MAP_SELECT_REQ req) {
		if (!CPMWBQReward.service().use()) {
			return;
		}
		SC_HUNTING_QUEST_MAP_LIST_NOTI noti = SC_HUNTING_QUEST_MAP_LIST_NOTI.newInstance();
		HUNTING_QUEST_MAP_INFO mapinfo = HUNTING_QUEST_MAP_INFO.newInstance();
		CPMWBQUserTable Uinfo = CPMWBQUserTable.getInstance();
		CPMWBQinfo model = new CPMWBQinfo();

		if (req.get_is_select() == false) {
			sendmessage(pc, 9999);
			return;
		}

		if (pc.Get_BQ_Size() >= 3) {
			sendmessage(pc, 2);
			return;
		}

		if (InBookinfo(pc, req, false)) {
			sendmessage(pc, 4);
			return;
		}
		bookinfo(pc, mapinfo, noti);
		mapinfo = HUNTING_QUEST_MAP_INFO.newInstance();
		mapinfo.set_map_number(req.get_map_number());
		mapinfo.set_location_desc(req.get_location_desc());
		mapinfo.set_kill_count(0);
		mapinfo.set_is_complete(false);
		noti.add_hunting_quest_map_list(mapinfo);
		pc.Add_BQ_Info(req.get_map_number(), req.get_location_desc(), model);
		Uinfo.Update_Info(pc.getId(), model);
		noti.set_remain_quest_count(remainBQ(pc));
		pc.sendPackets(noti, MJEProtoMessages.SC_HUNTING_QUEST_MAP_LIST_NOTI);
		dipose(noti, mapinfo);
	}

	public void BQQuestComplete(L1PcInstance pc, CS_HUNTING_QUEST_REWARD_REQ req) {
		if (!CPMWBQReward.service().use()) {
			return;
		}
		if (!Is_Complete(pc, req)) {
			System.out.println(String.format("[怪物圖鑑]要求與狀態不符的獎勵！角色名：%s", pc.getName()));
			return;
		}
		if (req.get_reward_grade() == eMonsterBookV2RewardGrade.RG_NORMAL) {
			GiveReward(pc, req, eMonsterBookV2RewardGrade.RG_NORMAL);
		} else if (req.get_reward_grade() == eMonsterBookV2RewardGrade.RG_DRAGON) {
			GiveReward(pc, req, eMonsterBookV2RewardGrade.RG_DRAGON);
		} else if (req.get_reward_grade() == eMonsterBookV2RewardGrade.RG_HIGH_DRAGON) {
			GiveReward(pc, req, eMonsterBookV2RewardGrade.RG_HIGH_DRAGON);
		} else {
			System.out.println(String.format("[怪物圖鑑]要求與狀態不符的獎勵！角色名：%s", pc.getName()));
		}
	}

	private void GiveReward(L1PcInstance pc, CS_HUNTING_QUEST_REWARD_REQ req, eMonsterBookV2RewardGrade val) {
		if (!CPMWBQReward.service().use()) {
			return;
		}
		CPMWBQUserTable Uinfo = CPMWBQUserTable.getInstance();
		if (pc.Get_BQ_Info() == null) {
			return;
		}
		if (pc.Get_BQ_Info().size() <= 0) {
			return;
		}
		if (consumitemid(pc, val) != 0) {
			if (!pc.getInventory().checkItem(consumitemid(pc, val))) {
				L1Item item = ItemTable.getInstance().getTemplate(consumitemid(pc, val));
				pc.sendPackets(String.format("缺少%s。", item.getName()));
				return;
			} else if (pc.getInventory().checkItem(consumitemid(pc, val))) {
				pc.getInventory().consumeItem(consumitemid(pc, val), 1);
			}
		}
		for (CPMWBQinfo BQinfo : pc.Get_BQ_Info()) {
			if (req.get_map_number() == BQinfo.getMapid() && req.get_location_desc() == BQinfo.getMapdesc()) {
				BQinfo.setIsclear(true);
				Uinfo.Update_Info(pc.getId(), BQinfo);
			}
		}
		AddExp(pc, val);
		AddItem(pc, val);
		AddEinPer(pc, val);
		BQload(pc);
	}

	private int consumitemid(L1PcInstance pc, eMonsterBookV2RewardGrade val) {
		int itmeid = CPMWBQReward.service().needitem(val.toInt());
		L1Item item = ItemTable.getInstance().getTemplate(itmeid);
		if (item == null) {
			return 0;
		}
		if (item.getItemId() == 0) {
			return 0;
		}
		return itmeid;
	}

	private void AddEinPer(L1PcInstance pc, eMonsterBookV2RewardGrade val) {
		int per = CPMWBQReward.service().addeinper(val.toInt());
		if (0 >= per) {
			return;
		}
		pc.getAccount().addBlessOfAin((per * 10000), pc, "圖鑑");
	}

	private void AddItem(L1PcInstance pc, eMonsterBookV2RewardGrade val) {
		int itemid = CPMWBQReward.service().rewardid(val.toInt());
		int count = CPMWBQReward.service().rewardcount(val.toInt());
		L1Item item = ItemTable.getInstance().getTemplate(itemid);
		if (item == null) {
			return;
		}
		if (item.getItemId() == 0) {
			return;
		}
		if (count == 0) {
			return;
		}
		pc.getInventory().storeItem(itemid, count);
	}

	private void AddExp(L1PcInstance pc, eMonsterBookV2RewardGrade val) {
		double exp = 0;
		int level = CPMWBQReward.service().addexplevel();
		int itemid = CPMWBQReward.service().boosteritem();
		L1Item item = ItemTable.getInstance().getTemplate(itemid);
		if (CPMWBQReward.service().boosterexp(val.toInt()) > 0 && item != null) {
			if (pc.getInventory().checkItem(itemid))
				exp += CPMWBQReward.service().boosterexp(val.toInt());
		}
		if (CPMWBQReward.service().levelexp(val.toInt()) > 0 && level <= pc.getLevel()) {
			exp += CPMWBQReward.service().levelexp(val.toInt());
		}
		if (CPMWBQReward.service().basicexp(val.toInt()) > 0) {
			exp += CPMWBQReward.service().basicexp(val.toInt());
		}
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		pc.add_exp((long) (exp * exppenalty));
	}

	private boolean Is_Complete(L1PcInstance pc, CS_HUNTING_QUEST_REWARD_REQ req) {
		for (CPMWBQinfo BQinfo : pc.Get_BQ_Info()) {
			if (BQinfo.getMapid() == req.get_map_number()
					&& BQinfo.getMapdesc() == req.get_location_desc()
					&& BQinfo.getMoncount() >= 150
					&& !BQinfo.isIsclear()) {
				return true;
			}
		}
		return false;
	}

	public void BQUpdateAmount(L1PcInstance pc, L1MonsterInstance mon) {
		if (!CPMWBQReward.service().use()) {
			return;
		}
		if (pc.Get_BQ_Info() == null) {
			return;
		}

		if (pc.Get_BQ_Info().size() <= 0) {
			return;
		}

		for (CPMWBQinfo info : pc.Get_BQ_Info()) {
			if (mon.getMapId() == 4) {
				if (info.getMapid() == mon.getMapId()
						&& info.getMapdesc() == CPMWBQNpcInfoTable.Get_Desc(mon.getNpcClassId())) {
					info.setMoncount(150 > info.getMoncount() ? info.getMoncount() + 1 : info.getMoncount());
					makestream(pc, info);
				}
			} else {
				if (info.getMapid() == mon.getMapId()) {
					info.setMoncount(150 > info.getMoncount() ? info.getMoncount() + 1 : info.getMoncount());
					makestream(pc, info);
				}
			}
		}
	}

	private void makestream(L1PcInstance pc, CPMWBQinfo BQinfo) {
		HUNTING_QUEST_MAP_INFO mapinfo = HUNTING_QUEST_MAP_INFO.newInstance();
		SC_HUNTING_QUEST_MAP_UPDATE_AMOUNT_NOTI noti = SC_HUNTING_QUEST_MAP_UPDATE_AMOUNT_NOTI.newInstance();
		mapinfo.set_map_number(BQinfo.getMapid());
		mapinfo.set_location_desc(BQinfo.getMapdesc());
		mapinfo.set_kill_count(BQinfo.getMoncount());
		mapinfo.set_is_complete(150 <= BQinfo.getMoncount() ? true : false);
		noti.set_hunting_quest_map(mapinfo);
		pc.sendPackets(noti, MJEProtoMessages.SC_HUNTING_QUEST_MAP_UPDATE_AMOUNT_NOTI);
		if (noti != null) {
			noti.dispose();
		}
		if (mapinfo != null) {
			mapinfo.dispose();
		}
	}

	private boolean In_User_Info(L1PcInstance pc, SC_HUNTING_QUEST_MAP_LIST_NOTI noti) {
		CPMWBQUserTable Uinfo = CPMWBQUserTable.getInstance();
		Uinfo.UserInfo(pc);
		if (pc.Get_BQ_Size() == 0) {
			noti.set_remain_quest_count(3);
			pc.sendPackets(noti, MJEProtoMessages.SC_HUNTING_QUEST_MAP_LIST_NOTI);
			if (noti != null) {
				noti.dispose();
			}
			return false;
		}
		return true;
	}

	private void bookinfo(L1PcInstance pc, HUNTING_QUEST_MAP_INFO mapinfo, SC_HUNTING_QUEST_MAP_LIST_NOTI noti) {
		for (CPMWBQinfo BQinfo : pc.Get_BQ_Info()) {
			if (!BQinfo.isIsclear()) {
				mapinfo = HUNTING_QUEST_MAP_INFO.newInstance();
				mapinfo.set_map_number(BQinfo.getMapid());
				mapinfo.set_location_desc(BQinfo.getMapdesc());
				mapinfo.set_kill_count(BQinfo.getMoncount());
				mapinfo.set_is_complete(150 <= BQinfo.getMoncount() ? true : false);
				noti.add_hunting_quest_map_list(mapinfo);
			}
		}
	}

	private boolean InBookinfo(L1PcInstance pc, CS_HUNTING_QUEST_MAP_SELECT_REQ req, boolean delete) {
		for (int i = 0; i < pc.Get_BQ_Size(); i++) {
			CPMWBQinfo BQinfo = pc.Get_BQ_Info().get(i);
			if (BQinfo.getMapid() == req.get_map_number() && BQinfo.getMapdesc() == req.get_location_desc()) {
				if (delete) {
					pc.Get_BQ_Info().remove(i);
				}
				return true;
			}
		}
		return false;
	}

	private void sendmessage(L1PcInstance pc, int type) {
		SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK noti = SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK.newInstance();
		noti.set_fail_reason(SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK.eResult.fromInt(type));
		pc.sendPackets(noti, MJEProtoMessages.SC_HUNTING_QUEST_MAP_SELECT_FAIL_ACK);
		if (noti != null) {
			noti.dispose();
		}
	}

	public int remainBQ(L1PcInstance pc) {
		int remaincounter = 3;
		for (CPMWBQinfo BQinfo : pc.Get_BQ_Info()) {
			if (BQinfo.isIsclear()) {
				remaincounter--;
			}
		}
		return remaincounter;
	}

	private void dipose(SC_HUNTING_QUEST_MAP_LIST_NOTI noti, HUNTING_QUEST_MAP_INFO mapinfo) {
		if (noti != null) {
			noti.dispose();
		}
		if (mapinfo != null) {
			mapinfo.dispose();
		}
	}
}

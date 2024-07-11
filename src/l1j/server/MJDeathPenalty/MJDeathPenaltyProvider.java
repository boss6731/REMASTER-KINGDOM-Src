package l1j.server.MJDeathPenalty;

import java.sql.Timestamp;

import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyExpModel;
import l1j.server.MJDeathPenalty.Item.MJDeathPenaltyItemDatabaseLoader;
import l1j.server.MJDeathPenalty.Item.MJDeathPenaltyItemModel;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.CS_DEATH_PENALTY_RECOVERY_EXP_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.CS_DEATH_PENALTY_RECOVERY_ITEM_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.DeathPenaltyExpListT;
import l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.DeathPenaltyItemListT;
import l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.SC_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY.SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class MJDeathPenaltyProvider {

	private static final MJDeathPenaltyProvider provider = new MJDeathPenaltyProvider();

	public static MJDeathPenaltyProvider provider() {
		return provider;
	}

	private MJDeathPenaltyProvider() {
	}

	public void senditeminfo(L1PcInstance pc) {
		if (isEmpty_item(pc)) {
			return;
		}

		SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI Ditemlist = SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI.newInstance();
		for (MJDeathPenaltyItemModel model : pc.get_deathpenalty_item()) {
			DeathPenaltyItemListT Ditem = DeathPenaltyItemListT.newInstance();
			L1Item l1item = null;
			L1ItemInstance item = new L1ItemInstance();
			l1item = ItemTable.getInstance().getTemplate(model.getItem_id());
			item.setItem(l1item);
			make_item(item, model);
			Ditem.set_item_info(ItemInfo.newInstance(pc, item, item.getItem().getUseType()));
			Ditem.set_recovery_cost(MJDeathPenaltyService.service().price_itempenalty()
					+ MJDeathPenaltyService.service().price_itempenalty() * item.getEnchantLevel());
			Ditem.set_delete_time(model.getDelete_time());
			Ditemlist.add_death_penalty_item_list(Ditem);
		}
		// 由於客戶端錯誤，先評論
		// pc.sendPackets(Ditemlist,
		// MJEProtoMessages.SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI);
	}

	public void sendexpinfo(L1PcInstance pc) {
		SC_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI expinfo = SC_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI.newInstance();
		if (isEmpty_exp(pc)) {
			pc.sendPackets(expinfo, MJEProtoMessages.SC_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI);
			return;
		}

		int idx = 0;
		long time = System.currentTimeMillis();
		for (MJDeathPenaltyExpModel model : pc.get_deathpenalty_exp()) {
			DeathPenaltyExpListT info = DeathPenaltyExpListT.newInstance();
			if (model.getDelete_time() - time / 1000 + 24 * 60 * 60 < 0) {
				pc.delete_deathpenalty_exp(model.getId());
				continue;
			}
			if (idx > 20) {
				continue;
			}

			float ration = model.getExp_ratio();
			double needExp = ExpTable.getNeedExpNextLevel(model.getDeathLevel())
					/ ExpTable.getPenaltyRate(model.getDeathLevel());
			double needExp2 = ExpTable.getNeedExpNextLevel(pc.getLevel()) / ExpTable.getPenaltyRate(pc.getLevel());
			double levelDif = 1;
			int recoverRation = 70;
			if (pc.getLevel() > 50) {
				recoverRation += (pc.getLevel() - 50) / 2;
			}
			if (recoverRation >= 90) {
				recoverRation = 90;
			}
			ration *= (double) recoverRation / 100;
			if (model.getDeathLevel() < pc.getLevel()) {
				levelDif = needExp / needExp2;
				ration *= levelDif;
			}

			info.set_index(idx);
			// info.set_exp_ratio(model.getExp_ratio()/10000);
			info.set_exp_ratio(ration / 10000);
			info.set_recovery_cost(model.getRecovery_cost());
			// info.set_recovery_cost(MJDeathPenaltyService.service().price_exppenalty());
			info.set_delete_time(model.getDelete_time());
			expinfo.add_death_penalty_exp_list(info);
			idx++;
			// System.out.println(idx + "+"+model.getDelete_time()+"+"+model.getId());
		}
		// 由於客戶端錯誤，先評論
		pc.sendPackets(expinfo, MJEProtoMessages.SC_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI);
	}

	public void recovery_exp(L1PcInstance pc, CS_DEATH_PENALTY_RECOVERY_EXP_REQ req) {
		if (isEmpty_exp(pc)) {
			return;
		}
		if (req.get_index() >= 20) {
			return;
		}
		MJDeathPenaltyExpModel model = pc.get_deathpenalty_exp().get(req.get_index());

		if (!pc.getInventory().checkItem(MJDeathPenaltyService.service().need_itemid_expenalty(),
				MJDeathPenaltyService.service().price_exppenalty())) {
			return;
		}

		// System.out.println(model.getId());

		// if (pc.get_exp_res() == 1) {
		int cost = 0;
		int level = pc.getLevel();
		int lawful = pc.getLawful();
		if (level < 45) {
			cost = level * level * 50;
		} else {
			cost = level * level * 150;
		}
		if (lawful >= 0) {
			cost = (int) (cost * 0.7);
		}
		pc.sendPackets(new S_Message_YN(738, String.valueOf(cost)));

		/*
		 * } else {
		 * pc.sendPackets(new S_ServerMessage(739));
		 * }
		 */

	}

	public void recovery_item(L1PcInstance pc, CS_DEATH_PENALTY_RECOVERY_ITEM_REQ req) {
		if (isEmpty_item(pc)) {
			return;
		}

		if (req.get_db_id() >= 20) {
			return;
		}

		MJDeathPenaltyItemModel model = pc.get_deathpenalty_item().get(req.get_db_id());
		if (!pc.getInventory().checkItem(MJDeathPenaltyService.service().need_itemid_itempenalty(),
				MJDeathPenaltyService.service().price_itempenalty() * model.getEnchantlvl()
						+ MJDeathPenaltyService.service().price_itempenalty())) {
			return;
		}

		L1ItemInstance RecoveryItem = ItemTable.getInstance().createItem(model.getItem_id());
		make_item(RecoveryItem, model);
		if (RecoveryItem == null) {
			return;
		}

		pc.getInventory().consumeItem(MJDeathPenaltyService.service().need_itemid_itempenalty(),
				MJDeathPenaltyService.service().price_itempenalty() * model.getEnchantlvl()
						+ MJDeathPenaltyService.service().price_itempenalty());
		MJDeathPenaltyItemDatabaseLoader.getInstance().Delete(pc, model.getItemobjid(), model.getItem_id());
		pc.getInventory().storeItem(RecoveryItem);
		pc.get_deathpenalty_item().remove(req.get_db_id());
		senditeminfo(pc);
	}

	public void make_item(L1ItemInstance item, MJDeathPenaltyItemModel model) {
		item.setEnchantLevel(model.getEnchantlvl());
		item.setIdentified(model.getIs_id() == 1 ? true : false);
		item.set_durability(model.getDurability());
		item.setChargeCount(model.getCharge_count());
		item.setRemainingTime(model.getRemaining_time());
		item.setLastUsed(model.getLast_used());
		item.setAttrEnchantLevel(model.getAttr_enchantlvl());
		item.setBless(model.getBless());
		item.setSpecialEnchant(model.getSpecial_enchant());
		item.set_item_level(model.getItem_level());
		item.setHotel_Town(model.getHotel_Town());
		item.setEndTime(model.getEnd_time());
		item.set_Carving(model.getCarving());
		item.set_bless_level(model.getBless_level());
		// item.get_Doll_Bonus_Level();
		// item.get_Doll_Bonus_Value();
		// item.setBlessType(i);
		// item.setBlessTypeValue(i);
	}

	public boolean isEmpty_item(L1PcInstance pc) {
		if (pc.get_deathpenalty_item() == null) {
			return true;
		}

		if (pc.get_deathpenalty_item().size() <= 0) {
			return true;
		}

		return false;
	}

	public boolean isEmpty_exp(L1PcInstance pc) {
		if (pc.get_deathpenalty_exp() == null) {
			return true;
		}

		if (pc.get_deathpenalty_exp().size() <= 0) {
			return true;
		}

		return false;
	}
}

package l1j.server.server.Controller;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
import l1j.server.AinhasadSpecialStat2.L1AinhasadFaithUserLoader;
import l1j.server.AinhasadSpecialStat2.L1AinhasadFaithUserObject;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SystemMessage;

public class PcInventoryDeleteController implements Runnable {

	private static PcInventoryDeleteController _instance;

	public static final int SLEEP_TIME = 60*1000;// 每60秒

	public static PcInventoryDeleteController getInstance() {
		if (_instance == null)
			_instance = new PcInventoryDeleteController();
		return _instance;
	}

	private Collection<L1PcInstance> _list = null;

	public void run() {
		long currentTimeMillis = System.currentTimeMillis();
		try {
			_list = L1World.getInstance().getAllPlayers();
			for (L1PcInstance pc : _list) {
				if (pc == null)
					continue;

				L1Inventory pcInventory = pc.getInventory();
				for (L1ItemInstance item : pcInventory.getItems()) {
					if (item == null)
						continue;

					if (item.getEndTime() == null)
						continue;

					if (currentTimeMillis > item.getEndTime().getTime()) {

						int itemId = item.getItemId();

						if (itemId == L1ItemId.MERIN_CONTRACT){
							pc.sendPackets(new S_ServerMessage(1823));
							pc.getInventory().storeItem(L1ItemId.MERIN_PIPE, 1);
						}else if (itemId == L1ItemId.KILLTON_CONTRACT){
							pc.sendPackets(new S_ServerMessage(1823));
							pc.getInventory().storeItem(L1ItemId.KILLTON_PIPE, 1);
						}else if (itemId == 3000048){
							pc.sendPackets(new S_ServerMessage(1823));
							pc.getInventory().consumeItem(3000048, 1);
						}else if (itemId == 80500){
							KeyTable.DeleteKeyId(item.getKeyId());
						}else if (itemId >= 30022 && itemId <= 30025) {
							// 如果該物品娃娃正在使用中，則從列表中刪除。
							L1DollInstance doll = pc.getMagicDoll();
							if(doll != null) {
								if (item.getId() == doll.getItemObjId()) {
									doll.deleteDoll();
									pc.sendPackets(new S_SkillIconGFX(56, 0));
									pc.sendPackets(new S_OwnCharStatus(pc));
								}
							}
						}
						pc.sendPackets(new S_SystemMessage(item.getName() + "的使用時間已過期，已消失。"));
						pcInventory.removeItem(item);
					}
				}
				AinhasadFaithTimeOut(pc, currentTimeMillis);
				favorBookInventoryTimeOut(pc, currentTimeMillis);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			_list = null;
		}
	}

	/**
	 * 調查用戶的聖物背包限時物品
	 * @param pc 玩家角色
	 * @param currentTime 當前時間
	 */
	private void favorBookInventoryTimeOut(L1PcInstance pc, long cuurrentTime){
		L1FavorBookInventory favorBook = pc.getFavorBook();
		if (favorBook == null) {
			return;
		}
		LinkedList<L1FavorBookUserObject> delList = null;

		for (L1FavorBookUserObject user : favorBook.getList()) {
			if (user == null || user.getType().getEndTime() == null || user.getType().getEndTime().getTime() > cuurrentTime) {
				continue;
			}
			if (delList == null) {
				delList = new LinkedList<L1FavorBookUserObject>();
			}
			delList.add(user);
//			favorBook.deleteFavor(user);
		}
		if (delList == null) {
			return;
		}
		for (L1FavorBookUserObject user : delList) {
			favorBook.deleteFavor(user);
		}

	}

	private void AinhasadFaithTimeOut(L1PcInstance pc, long currentTime) {
		ArrayList<Integer> list = new ArrayList<>(pc.getAinHasd_faith().keySet());
		HashMap<Integer, Timestamp> map = pc.getAinHasd_faith();
		ArrayList<Integer> deletlist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == 101 || list.get(i)== 102) {
				continue;
			}

			String TimeStr = map.get(list.get(i)).toString();
			long time = Timestamp.valueOf(TimeStr).getTime();
			int index = list.get(i);
			AinhasadSpecialStat2Info info= AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
			if (time < currentTime) {
				if(info.get_type() != 1) {
					AinhasadSpecialStat2Info.einhasad_faith_option(pc, index, null, false);
					L1AinhasadFaithUserLoader.getInstance().delete(pc, index);
					SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send(pc, index);
					deletlist.add(list.get(i));
					//break;
				}
			}
		}

		if (list.contains(101)) {
			if (deletlist.contains(1) || deletlist.contains(2) || deletlist.contains(3) || deletlist.contains(4)) {
				AinhasadSpecialStat2Info.einhasad_faith_option(pc, 101, null, false);
				L1AinhasadFaithUserLoader.getInstance().delete(pc, 101);
				SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send_group(pc, 1);
			}
		}
		if (list.contains(102)) {
			if (deletlist.contains(5) || deletlist.contains(6) || deletlist.contains(7) || deletlist.contains(8)) {
				AinhasadSpecialStat2Info.einhasad_faith_option(pc, 102, null, false);
				L1AinhasadFaithUserLoader.getInstance().delete(pc, 102);
				SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send_group(pc, 2);
			}
		}
		list.clear();
		map.clear();
		deletlist.clear();
	}
}
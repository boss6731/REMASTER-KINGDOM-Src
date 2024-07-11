/*package l1j.server.AinhasadSpecialStat2;

import java.util.Collection;

import l1j.server.server.Controller.PcInventoryDeleteController;
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

public class AinhasadFaithTimeCheck {

	private static AinhasadFaithTimeCheck _instance;

	public static final int SLEEP_TIME = 60000;

	public static AinhasadFaithTimeCheck getInstance() {
		if (_instance == null)
			_instance = new AinhasadFaithTimeCheck();
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
				
				for (Integer index : pc.getAinHasd_faith()) {
					AinhasadSpecialStat2Info info = 
					if (index == null)
						continue;

					if (index.getEndTime() == null)
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
							// 해당 아이템 인형이 사용중이라면 리스트 삭제.
							L1DollInstance doll = pc.getMagicDoll();
							if(doll != null) {
								if (item.getId() == doll.getItemObjId()) {
									doll.deleteDoll();
									pc.sendPackets(new S_SkillIconGFX(56, 0));
									pc.sendPackets(new S_OwnCharStatus(pc));
								}
							}
						}
						pc.sendPackets(new S_SystemMessage(item.getName() + "의 사용시간이 만료 되어 소멸되었습니다."));
						pcInventory.removeItem(item);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			_list = null;
		}
	}
}*/
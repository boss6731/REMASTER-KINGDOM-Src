package l1j.server.server.server.model.Instance;

import l1j.server.CPMWReNewClan.ClanDungeon.ClanDugeon;
import l1j.server.CPMWReNewClan.ClanDungeon.L1ClanDugeon;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

class L1FieldObjectInstance extends l1j.server.server.model.Instance.L1NpcInstance {

	private static final long serialVersionUID = 1L;
	private int moveMapId;

	public L1FieldObjectInstance(L1Npc template) {
		super(template);
	}

	@Override
	public <L1PcInstance> void onAction(L1PcInstance player) {

	}

	@Override
	public void onAction(l1j.server.server.model.Instance.L1PcInstance pc) {
	}

	@Override
	public void onTalkAction(l1j.server.server.model.Instance.L1PcInstance pc) {
		int npcid = getNpcTemplate().get_npcId();
		switch (npcid) {
			case 120620:{
				L1ClanDugeon LCD = ClanDugeon.getInstance().getClanDugeon(moveMapId);
				int randomX = 0;
				int randomY = 0;
				if(npcid == 120620) {
					randomX = 32747 + (int) (Math.random() * 2) - (int) (Math.random() * 2);
					randomY = 32805 + (int) (Math.random() * 2) - (int) (Math.random() * 2);
				} else {
					randomX = 33537 + (int) (Math.random() * 20) - (int) (Math.random() * 20);
					randomY = 32702 + (int) (Math.random() * 20) - (int) (Math.random() * 20);
				}
				if (pc.getLevel() < ClanDugeon.getInstance().ClanDugeonInfo.minlv) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("由於血盟地下城等級限制，無法進入傳送門。")));
					pc.sendPackets(String.valueOf(new S_SystemMessage("血盟地下城的最低進入等級是 " + ClanDugeon.getInstance().ClanDugeonInfo.minlv + " 級。")));
					return;
				}
				if(ClanDugeon.getInstance().ClanDugeonInfo.checkitem) {
					if (pc.getInventory().checkItem(4101007) && npcid == 120600) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("持有誓言之證時無法進入。")));
						pc.sendPackets(String.valueOf(new S_SystemMessage("每天上午 6 點重置血盟每日地下城。")));
						pc.sendPackets(String.valueOf(new S_SystemMessage("每週三上午 6 點重置血盟每週地下城。")));
						return;
					} else if (pc.getInventory().checkItem(4101007) && (npcid == 120601 || npcid == 120620) && ClanDugeon.getInstance().ClanDugeonInfo.dayplay) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("持有誓言之證時無法進入。")));
						pc.sendPackets(String.valueOf(new S_SystemMessage("每天上午 6 點重置血盟每日地下城。")));
						return;
					} else if (pc.getInventory().checkItem(4101008) && (npcid == 120601 || npcid == 120620) && !ClanDugeon.getInstance().ClanDugeonInfo.dayplay) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("持有誓言之證時無法進入。")));
						pc.sendPackets(String.valueOf(new S_SystemMessage("每週三上午 6 點重置血盟每週地下城。")));
						return;
					}
				}

				if (ClanDugeon.getInstance().ClanDugeonInfo.maxuser < LCD.getMembersCount()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("最大進入人數為 " + ClanDugeon.getInstance().ClanDugeonInfo.maxuser + " 人。")));
					return;
				}
				if (LCD.isNowCD()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("血盟地下城實例已經開始。")));
					return;
				}
				LCD.addMember(pc);
				pc.start_teleport(randomX, randomY, moveMapId, pc.getHeading(), 169, true);
			}
			break;
			default:
				break;
		}
	}

	/** 設定要移動的地圖。
	 * @param id
	 */
	public void setMoveMapId(int id) {
		moveMapId = id;
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		removeAllKnownObjects();
	}
}

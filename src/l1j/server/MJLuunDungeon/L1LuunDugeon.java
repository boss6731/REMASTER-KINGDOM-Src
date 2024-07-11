package l1j.server.MJLuunDungeon;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1LuunDugeon implements Runnable {
	private boolean _isNowLuun;
	private boolean _active = false;
	private boolean _close = false;
	private int counter = 0;
	L1NpcInstance stageBoss;
	ArrayList<L1NpcInstance> stageunit;
	int[] lunnnum = new int[] { 4001, 4002 };

	private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();

	public void Start() {
		LuunDugeonSpawn.getInstance().fillSpawnTable(4001, 1);
		GeneralThreadPool.getInstance().execute(this);
	}

	public L1LuunDugeon() {
		GeneralThreadPool.getInstance().schedule(new timer(), 60000);
	}

	class timer implements Runnable {
		@Override
		public void run() {
			try {
				if (_close) {
					return;
				}
				checkMember();
				if (_members == null) {
					quit();
				} else if (_members != null && _members.size() <= 0) {
					quit();
				}

				removeRetiredMembers();

				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try {
			setActive(true);
			setNowLuun(true);
			for (int round = 1; round <= 4; round++) {
				if (_close) {
					break;
				}
				if (round == 1 || round == 3 || round == 4) {
					while (!stageBoss.isDead() && counter <= 6000) {
						if (_close) {
							break;
						}

						if (counter >= 6000) {
							sendMessage("該輪限制時間超過10分鐘，將被強制返回。", 5000);
							quit();
							break;
						}
						Thread.sleep(100);
						removeRetiredMembers();
						counter++;
					}
					counter = 0;
					Thread.sleep(5000);
				} else {
					for (int i = 0; i < stageunit.size(); i++) {
						while ((!stageBoss.isDead() || !stageunit.get(i).isDead()) && counter <= 6000) {
							if (_close) {
								break;
							}

							if (counter >= 6000) {
								sendMessage("該輪限制時間超過10分鐘，將強制返回。", 5000);
								quit();
								break;
							}

							Thread.sleep(100);
							removeRetiredMembers();
							counter++;
						}
					}
					counter = 0;
					Thread.sleep(5000);
				}
				if (!_close) {
					bosskill(round);
				}
				if (round == 4) {
					sendMessage("30秒後將移動到村莊。", 5000);
					Thread.sleep(30000);
				}
			}
			quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bosskill(int round) throws InterruptedException {
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;
				if (door.getDoorId() == 2110 + round) {
					if (door.getOpenStatus() != ActionCodes.ACTION_Open) {
						door.open();
					}
				}
			}
		}
		switch (round) {
			case 1:
				LuunDugeonSpawn.getInstance().fillSpawnTable(4001, 2);
				break;
			case 2:
				LuunDugeonSpawn.getInstance().fillSpawnTable(4002, 3);
				break;
			case 3:
				LuunDugeonSpawn.getInstance().fillSpawnTable(4002, 4);
				break;
			default:
				break;
		}
	}

	private void checkMember() {
		for (int i = 0; i < lunnnum.length; i++) {
			for (L1Object ob : L1World.getInstance().getVisibleObjects(lunnnum[i]).values()) {
				if (ob instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) ob;
					if (!isMember(pc)) {
						addMember(pc);
					}
				}
			}
		}
	}

	public void addMember(L1PcInstance pc) {
		if (!_members.contains(pc)) {
			_members.add(pc);
		}
	}

	public void removeMember(L1PcInstance pc) {
		_members.remove(pc);
	}

	public void clearMembers() {
		_members.clear();
	}

	public boolean isMember(L1PcInstance pc) {
		return _members.contains(pc);
	}

	public L1PcInstance[] getMembersArray() {

		return _members.toArray(new L1PcInstance[_members.size()]);
	}

	public int getMembersCount() {
		return _members.size();
	}

	private void setNowLuun(boolean i) {
		_isNowLuun = i;
	}

	public boolean isNowLuun() {
		return _isNowLuun;
	}

	private void setActive(boolean f) {
		_active = f;
	}

	public boolean isActive() {
		return _active;
	}

	private void removeRetiredMembers() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == null || (temp[i].getMapId() != lunnnum[0] && temp[i].getMapId() != lunnnum[1])) {
				removeMember(temp[i]);
			}
		}
		temp = null;
	}

	private void sendMessage(String msg, int time) {
		L1PcInstance[] temp = getMembersArray();
		S_SystemMessage sm = new S_SystemMessage(msg);
		for (L1PcInstance pc : temp) {
			SC_NOTIFICATION_MESSAGE_NOT noti = SC_NOTIFICATION_MESSAGE_NOT.newInstance();
			noti.set_suffileNumber(-1);
			noti.set_notificationMessage("\\f=" + msg);
			noti.set_messageRGB(new MJSimpleRgb(0, 250, 0));
			noti.set_duration(time / 1000);
			pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_MESSAGE_NOT, true);
			pc.sendPackets(sm);
		}
		temp = null;
	}

	private void clearLuunDugeon() {
		L1MonsterInstance mob = null;
		L1PcInstance pc = null;
		L1Inventory inventory = null;
		L1DoorInstance door = null;
		for (int m = 0; m < lunnnum.length; m++) {
			for (Object obj : L1World.getInstance().getVisibleObjects(lunnnum[m]).values()) {
				if (obj instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) obj;
					if (!mob.isDead()) {
						mob.setDead(true);
						mob.setStatus(ActionCodes.ACTION_Die);
						mob.setCurrentHp(0);
						mob.deleteMe();
					}
				} else if (obj instanceof L1PcInstance) {
					pc = (L1PcInstance) obj;
					pc.start_teleport(34464, 32193, 4, pc.getHeading(), 169, true, true);
				} else if (obj instanceof L1Inventory) {
					inventory = (L1Inventory) obj;
					inventory.clearItems();
				} else if (obj instanceof L1DoorInstance) {
					door = (L1DoorInstance) obj;
					door.close();
				}
			}
		}
	}

	private void quit() {
		_close = true;
		for (L1PcInstance pc : getMembersArray()) {
			pc.start_teleport(34464, 32193, 4, pc.getHeading(), 169, true);
			removeMember(pc);
		}
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			removeMember(temp[i]);
		}
		temp = null;
		Object_Delete();
		clearLuunDugeon();
		clearMembers();
		LuunDugeon.getInstance().Reset(4001);
		LuunDugeon.getInstance().Reset(4002);
		LuunDugeon.getInstance().isactive = false;
	}

	private void Object_Delete() {
		for (int m = 0; m < lunnnum.length; m++) {
			for (L1Object ob : L1World.getInstance().getVisibleObjects(lunnnum[m]).values()) {
				if (ob == null || ob instanceof L1DollInstance
						|| ob instanceof L1SummonInstance
						|| ob instanceof L1PetInstance
						|| ob instanceof L1DoorInstance)
					continue;
				if (ob instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) ob;
					npc.deleteMe();
				}
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			for (int m = 0; m < lunnnum.length; m++) {
				if (obj.getMapId() != lunnnum[m]) {
					continue;
				}
			}

			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}

	public void appearaction(L1NpcInstance npc) {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != null || temp[i].getMapId() == npc.getMapId()) {
				npc.onPerceive(temp[i]);
				S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Appear);
				temp[i].sendPackets(gfx);
			}
		}
		temp = null;
	}
}
package l1j.server.DeathMatch;

import l1j.server.Config;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.L1SpawnUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DeathMatch implements Runnable{
	private Random rnd = new Random(System.currentTimeMillis());
	
	private final ArrayList<L1PcInstance> playmember = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> playmemberZone = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> TeamRed = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> TeamBlue = new ArrayList<L1PcInstance>();
	private ArrayList<L1PcInstance> TeamRedCopy = new ArrayList<L1PcInstance>();
	private ArrayList<L1PcInstance> TeamBlueCopy = new ArrayList<L1PcInstance>();
	
	private L1PcInstance pc;
	
	private boolean start = false;
	private boolean game = false;
	
	private int step = 0;
	private int sub_step = 0;
	private int sub_step1 = 0;
	private int checkcount = 0;
	private String msg ="";
	L1NpcInstance[] _firewall = new L1NpcInstance[4];
	private L1NpcInstance shop;
	
	public DeathMatch(int id, L1PcInstance pc) {
		this.pc = pc;
	}
	
	public void Start() {
//		System.out.println("��?");
		game = true;
		GeneralThreadPool.getInstance().execute(this);
	}
	
	public DeathMatch(int mapid) {
		spawnNpc();
		GeneralThreadPool.getInstance().schedule(new timer(), 1000);
	}
	
	
	
	class timer implements Runnable{
		@Override
		public void run() {
			try {
//				System.out.println("��2?");
				if (!game) {
                    return new L1PcInstance[0];
				}
				checkMember();
				GeneralThreadPool.getInstance().schedule(this, 1000);
			}catch (Exception e) {
				e.printStackTrace();
			}
            return new L1PcInstance[0];
        }
	}
	
	public void run() {
		try {
			if (!game) {
                return new L1PcInstance[0];
			}
//			System.out.println("������ġ ������ ����"+step+"-"+sub_step);
			switch(step) {
			case 0: //�غ�ܰ�
				if (sub_step == 0) {
					sub_step = 1;
//					GeneralThreadPool.getInstance().schedule(this, 5 * 60 * 1000);//5�д��
				}
				if (sub_step == 1) {
//					if (getPlayMembersCount() >= 2) {
					if (checkcount == 1 * 6) {
						GREEN_MSG("���� ���� 4�� ���Դϴ�.");
					} else if (checkcount == 2 * 6) {
						GREEN_MSG("���� ���� 3�� ���Դϴ�.");
					} else if (checkcount == 3 * 6) {
						GREEN_MSG("���� ���� 2�� ���Դϴ�.");
					} else if (checkcount == 4 * 6) {
						GREEN_MSG("���� ���� 1�� ���Դϴ�.");
					} else if (checkcount > 5 * 6) {//60ī��Ʈ ���� ��������� ����
						if (getPlayMembersCount() >= 2) {
							sub_step = 2;
							GREEN_MSG("�� ������ġ�� ���Ű��� ȯ���մϴ�.");
							GeneralThreadPool.getInstance().schedule(this, 15 * 1000);
                            return new L1PcInstance[0];
						} else {
							GREEN_MSG("���� �ο� �������� ������ġ�� ����˴ϴ�.");
							msg = "(�ο� ����)";
							step = 5;
							GeneralThreadPool.getInstance().schedule(this, 15 * 1000);
                            return new L1PcInstance[0];
						}
					}
//					System.out.println(checkcount);
					checkcount++;
					GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                    return new L1PcInstance[0];

/*					GREEN_MSG("�� ������ġ�� ���Ű��� ȯ���մϴ�.");
					sub_step = 2;
					GeneralThreadPool.getInstance().schedule(this, 15 * 1000);
					return;
*/				}
				if (sub_step == 2) {
					GREEN_MSG("�� ������ ���۵ǿ��� �غ� �Ͻñ� �ٶ��ϴ�.");
					sub_step = 3;
					GeneralThreadPool.getInstance().schedule(this, 15 * 1000);
                    return new L1PcInstance[0];
				}
				if (sub_step == 3) {
					GREEN_MSG("������ ������ ������ �� �����̸� ������ �ڷ���Ʈ �� ��ȯ�� �Ұ��� �մϴ�.");
					sub_step = 4;
//					GeneralThreadPool.getInstance().schedule(this, 5 * 60 * 1000);
					GeneralThreadPool.getInstance().schedule(this, 5 * 1000);
                    return new L1PcInstance[0];
				}
				if (sub_step == 4 && !start) {
					start = true;
					sub_step = 5;
				}
				if (sub_step == 5) {
					GREEN_MSG("���� �� �뽺��ġ�� �����մϴ�. ��� ������ ���ϴ�.");
					step = 1;
					TeamSelect();
					startTeleport();
					GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                    return new L1PcInstance[0];
				}
				GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                return new L1PcInstance[0];
			case 1:
				if (sub_step1 == 0) {
					GREEN_MSG_TR("����� RED�� �Դϴ�. �ֺ� ������ �Բ� Blue���� �������");
					GREEN_MSG_TB("����� Blue�� �Դϴ�. �ֺ� ������ �Բ� Red���� �������");
					sub_step1 = 1;
					GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                    return new L1PcInstance[0];
				}
				if (sub_step1 == 1) {
					deleteNpc();
					step = 2;
					GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                    return new L1PcInstance[0];
				}
			case 2:
				DeathCheck();
//				System.out.println(TeamRed.size()+" : "+TeamBlue.size());
//				if (TeamRed.size() == 0) {
				if (TeamRed.isEmpty()) {
					step = 3;
					GeneralThreadPool.getInstance().schedule(this, 5 * 1000);
                    return new L1PcInstance[0];
				}
//				if (TeamBlue.size() == 0) {
				if (TeamBlue.isEmpty()) {
					step = 4;
					GeneralThreadPool.getInstance().schedule(this, 5 * 1000);
                    return new L1PcInstance[0];
				}
				
				GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
                return new L1PcInstance[0];
			case 3:
				GREEN_MSG_Zone("�����մϴ�. BLUE ���� �¸� �Ͽ����ϴ�.");
				msg = "(BLUE�� ��)";
//				System.out.println("3");
				RewardBlue();
				step = 5;
				GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                return new L1PcInstance[0];
			case 4:
				GREEN_MSG_Zone("�����մϴ�. RED ���� �¸� �Ͽ����ϴ�.");
				msg = "(RED��)";
//				System.out.println("4");
				RewardRed();
				step = 5;
				GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                return new L1PcInstance[0];
			case 5:
//				System.out.println("5");
				RewardAll();
				clear();
				GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
                return new L1PcInstance[0];
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
        return new L1PcInstance[0];
    }
	
	private void clear() {
		
		checkcount = 0;
		teleport_home();
		TeamRed.clear();
		TeamRedCopy.clear();
		TeamBlue.clear();
		TeamBlueCopy.clear();
		playmember.clear();
		playmemberZone.clear();
		start = false;
		game = false;
		step = 0;
		sub_step = 0;
		sub_step1 = 0;
		System.out.println("������ġ ���� "+msg);
		DeathMatchSystem.getInstance().Reset();
	}
	
	
	
	
	public int getPlayMembersTRCopyCount() {
		return TeamRedCopy.size();
	}
	public L1PcInstance[] getPlayMemberTRCopyArray() {
		return (L1PcInstance[]) TeamRedCopy.toArray(new L1PcInstance[getPlayMembersTRCopyCount()]);
	}
	
	public int getPlayMembersTBCopyCount() {
		return TeamBlueCopy.size();
	}
	public L1PcInstance[] getPlayMemberTBCopyArray() {
		return (L1PcInstance[]) TeamBlueCopy.toArray(new L1PcInstance[getPlayMembersTBCopyCount()]);
	}
	
	
	private void RewardBlue() {
		for (L1PcInstance pc : getPlayMemberTBCopyArray()) {
			pc.getInventory().storeItem(Config.DeathMatch.WINNER_REWARD_ITEM_ID, Config.DeathMatch.WINNER_REWARD_ITEM_COUNT);
		}
	}
	
	private void RewardRed() {
		for (L1PcInstance pc : getPlayMemberTRCopyArray()) {
			pc.getInventory().storeItem(Config.DeathMatch.WINNER_REWARD_ITEM_ID, Config.DeathMatch.WINNER_REWARD_ITEM_COUNT);
		}
	}
	private void RewardAll() {
		for (L1PcInstance pc : getPlayMemberZoneArray()) {
			pc.getInventory().storeItem(Config.DeathMatch.DEATH_MATCH_REWARD_ITEM_ID, Config.DeathMatch.DEATH_MATCH_REWARD_ITEM_COUNT);
		}
	}
	
	private void teleport_home() {
//		System.out.println("����������?");
//		System.out.println(playmember.size());
		for (L1PcInstance pc : getPlayMemberZoneArray()) {
			if (pc.getMapId() == 13006 || pc.getMapId() == 13005) {
				pc.start_teleport(33445 + rnd.nextInt(5), 32798 + rnd.nextInt(5), 4, 5, 18339, true);
			}
		}
	}
	
	private void DeathCheck() {
		for (L1PcInstance player : getPlayMemberArrayTR()) {
			if (player.isDead()) {
				TeamRed.remove(player);
			}
			if (player.getMapId() != 13006) {
				TeamRed.remove(player);
			}
		}
		for (L1PcInstance player : getPlayMemberArrayTB()) {
			if (player.isDead()) {
				TeamBlue.remove(player);
			}
			if (player.getMapId() != 13006) {
				TeamBlue.remove(player);
			}
			
		}
		
		
	}
	private void spawnNpc() {
		_firewall[0] = L1SpawnUtil.spawnnpc(32799, 32835, (short)13006, 50000270, 0, 0, 0);
		_firewall[1] = L1SpawnUtil.spawnnpc(32805, 32835, (short)13006, 50000270, 0, 0, 0);
		_firewall[2] = L1SpawnUtil.spawnnpc(32805, 32828, (short)13006, 50000270, 0, 0, 0);
		_firewall[3] = L1SpawnUtil.spawnnpc(32799, 32828, (short)13006, 50000270, 0, 0, 0);
		shop = L1SpawnUtil.spawn4(32734, 32700, (short) 13005, 4, 7800100, 0, 0, 0);//�Է�
	}
	
	private void deleteNpc() {
		for (int i = 0; i < 4; i++) {
			if (_firewall[i] != null) {
				_firewall[i].deleteMe();
			}
		}
	}
	
	private void TeamSelect() {
		Collections.shuffle(playmember);
		int player_number = playmember.size();
		
		for (L1PcInstance player : getPlayMemberArray()) {
			L1Party party = player.getParty();
			if (party != null) {
				party.leaveMember(player);
			}
		}
		
		for (int i = 0; i < player_number / 2; i++) {
			TeamRed.add(playmember.get(i));
			DeathMatchSystem.getInstance().addPlayMemberTR(playmember.get(i));
		}
		for (int i = 0; i < player_number; i++) {
			if (TeamRed.contains(playmember.get(i))){
				continue;
			} else {
				TeamBlue.add(playmember.get(i));	
				DeathMatchSystem.getInstance().addPlayMemberTB(playmember.get(i));
			}
		}
		
		TeamBlueCopy = TeamBlue;
		TeamRedCopy = TeamRed;
		playmemberZone.addAll(TeamBlue);
		playmemberZone.addAll(TeamRed);
		
	}
	public void startTeleport() {
		
		for (L1PcInstance pc : getPlayMemberArrayTR()) {
			MJPoint pt_TR = MJPoint.newInstance(32801, 32821, 3, (short)13006, 50);
			pc.start_teleport(pt_TR.x, pt_TR.y, pt_TR.mapId, pc.getHeading(), 18339, true);
		}
		
		for (L1PcInstance pc : getPlayMemberArrayTB()) {
			MJPoint pt_TB = MJPoint.newInstance(32801, 32843, 3, (short)13006, 50);
			pc.start_teleport(pt_TB.x, pt_TB.y, pt_TB.mapId, pc.getHeading(), 18339, true);
		}
	}
	
		
	
	public void addPlayMember(L1PcInstance pc) {	
		playmember.add(pc);
	}

	public int getPlayMembersCount() {
		return playmember.size();
	}

	public int getPlayMembersZoneCount() {
		return playmemberZone.size();
	}
	public void removePlayMember(L1PcInstance pc) {
		playmember.remove(pc);
	}

	public void clearPlayMember() {
		playmember.clear();
	}

	public boolean isPlayMember(L1PcInstance pc) {
		return playmember.contains(pc);
	}
	
	public L1PcInstance[] getPlayMemberArray() {
		return (L1PcInstance[]) playmember.toArray(new L1PcInstance[getPlayMembersCount()]);
	}
	
	public L1PcInstance[] getPlayMemberZoneArray() {
		return (L1PcInstance[]) playmemberZone.toArray(new L1PcInstance[getPlayMembersZoneCount()]);
	}
	
	private void checkMember() {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(13005).values()) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (!isPlayMember(pc)){
					addPlayMember(pc);
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "������ġ�� �����ϱ� ������ �غ��ϸ鼭 ��ٷ� �ֽñ� �ٶ��ϴ�."));
				}
			}
		}
		if (getPlayMembersCount() > 0){
			for (L1PcInstance pc : getPlayMemberArray()){
				if (pc.getMapId() != 13005){
					removePlayMember(pc);
				}
			}
		}
//		System.out.println(getPlayMembersCount()+"��");
	}
	
	private void GREEN_MSG(String msg) {
		for (L1PcInstance pc : getPlayMemberArray()) {
			try {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void GREEN_MSG_Zone(String msg) {
		for (L1PcInstance pc : getPlayMemberZoneArray()) {
			try {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public L1PcInstance[] getPlayMemberArrayTR() {
		return (L1PcInstance[]) TeamRed.toArray(new L1PcInstance[getPlayMembersCountTR()]);
	}
	public int getPlayMembersCountTR() {
		return TeamRed.size();
	}

	public L1PcInstance[] getPlayMemberArrayTB() {
		return (L1PcInstance[]) TeamBlue.toArray(new L1PcInstance[getPlayMembersCountTB()]);
	}
	public int getPlayMembersCountTB() {
		return TeamBlue.size();
	}
	
	private void GREEN_MSG_TR(String msg) {
		for (L1PcInstance pc : getPlayMemberArrayTR()) {
			try {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void GREEN_MSG_TB(String msg) {
		for (L1PcInstance pc : getPlayMemberArrayTB()) {
			try {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

package l1j.server.server.Controller;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Pledge;

public class AuraController implements Runnable {

	private static AuraController _instance;

	public static AuraController getInstance() {
		if (_instance == null) {
			_instance = new AuraController();
		}
		return _instance;
	}

	public void run() {
		try {
			//Aura();
			Clanbuff();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GeneralThreadPool.getInstance().schedule(this, 2000);
		}
        return new L1PcInstance[0];
    }

	private void Clanbuff() {
		try {
			for (L1Clan clan : L1World.getInstance().getAllClans()) {
				int bless = clan.getBless();
				int[] time = clan.getBuffTime();
				if (bless != 0) {
					clan.setBuffTime(bless - 1, time[bless - 1] - 1);
					if (clan.getBuffTime()[bless - 1] == 0) {
						for (L1PcInstance member : clan.getOnlineClanMember()) {
							member.sendPackets(new S_Pledge(clan, bless));
							member.removeSkillEffect(bless + 504);
						}
						clan.setBless(0);
						ClanTable.getInstance().updateBless(clan.getClanId(), 0);
					}
				}
			}
		} catch (Exception e) {
		}
	}
/*
	public void addMember(L1PcInstance pc) {
		try {
			if (pc == null || _pbalist.contains(pc)) {
				return;
			}
		} catch (Exception e) {
		}
		_pbalist.add(pc);
	}

	public void removeMember(L1PcInstance pc) {
		try {
			if (pc == null || !_pbalist.contains(pc)) {
				return;
			}
		} catch (Exception e) {
		}
		_pbalist.remove(pc);
	}

	public boolean getMember(L1PcInstance pc) {
		return _pbalist.contains(pc);
	}

	private void Aura() {
		try {
			if (_pbalist.size() > 0) {
				L1PcInstance pc = null;
				for (int i = 0; i < _pbalist.size(); i++) {
					pc = _pbalist.get(i);
					if (pc != null) {
						int count = partycount(pc);
						if (!pc.getPbavatar()) {
							if (count >= 2) {
								Aurastart(pc, count);
							}
						} else {
							Auracheck(pc, count);
							if (count < 2) {
								Aura_end(pc);
								L1Party party = pc.getParty();
								for (L1PcInstance m : party.getMembers()) {
									if (m == null || !m.getPbavatar())
										continue;
									Aura_end(m);
								}
							}
						}
					}

				}
			}
		} catch (Exception e) {
		}
	}

	private void Aurastart(L1PcInstance pc, int count) {
		try {
			if (!pc.getPbavatar()) {
				AuraState(pc);
				pc.setPbacount(count);
			}
			for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
				if (pc.getParty().isMember(player)) {
					AuraState(player);
					player.setPbacount(count);
				}
			}
		} catch (Exception e) {
		}
	}

	private void Auracheck(L1PcInstance pc, int count) {
		try {
			L1Party party = pc.getParty();
			for (L1PcInstance player : party.getMembers()) {
				player.setPbavataron(false);
				for (L1PcInstance pc2 : L1World.getInstance().getVisiblePlayer(player, 18)) {
					if (party.isMember(pc2)) {
						player.setPbavataron(true);
					}
				}

				if (pc != player) {
					if (player.getPbavataron()) {
						if (!player.getPbavatar()) {
							AuraState(player);
							player.setPbacount(count);
						} else {
							if (count != player.getPbacount()) {
								Aura_end(player);
								AuraState(player);
								player.setPbacount(count);
							}
						}
					} else {
						Aura_end(player);
					}
				} else {
					if (count != player.getPbacount()) {
						Aura_end(player);
						AuraState(player);
						player.setPbacount(count);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	private int partycount(L1PcInstance pc) {
		int count = 0;
		try {
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (player == null)
					continue;
				if (pc.getParty() == null)
					continue;
				if (pc.getParty().isMember(player)) {
					count += 1;
				}
			}
		} catch (Exception e) {
		}
		
		return count;

	}

	private void AuraState(L1PcInstance pc) {
		try {
			if (!pc.hasSkillEffect(L1SkillId.AURA)) {
				pc.setSkillEffect(L1SkillId.AURA, -1);
				pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 479));
				pc.getResistance().addMr(10);
				pc.addSpecialResistance(eKind.ALL, 2);
				pc.getAbility().addAddedInt((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedStr((byte) 1);
				pc.resetBaseMr();
				pc.setPbavatar(true);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus2(pc));
			}
		} catch (Exception e) {
		}
		SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
	}

	public void Aura_end(L1PcInstance pc) {
		try {
			if (pc.hasSkillEffect(L1SkillId.AURA)) {
				pc.killSkillEffectTimer(L1SkillId.AURA);
				pc.getResistance().addMr(-10);
				pc.addSpecialResistance(eKind.ALL, -2);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedStr((byte) -1);
				pc.setPbavatar(false);
				pc.setPbacount(0);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 479));
			}
		} catch (Exception e) {
		}
		SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
	}

	public void allAura_end(L1PcInstance pc) {
		try {
			for (L1PcInstance player : pc.getParty().getMembers()) {
				if (player.getPbavatar()) {
					Aura_end(player);
				}
			}
		} catch (Exception e) {
		}
	}
*/
}

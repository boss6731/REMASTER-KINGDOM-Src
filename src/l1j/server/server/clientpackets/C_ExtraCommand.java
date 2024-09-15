package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;


import l1j.server.server.model.Broadcaster;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class C_ExtraCommand extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_EXTRA_COMMAND = "[C] C_ExtraCommand";

	public C_ExtraCommand(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int actionId = readC();
		
//		System.out.println(actionId);
		
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		if (pc.isInvisble()) { // 隱身中，盲目隱藏中
			return;
		}
		if (pc.get_teleport()) { // 傳送處理中
			return;
		}

		// TODO 變身中使用 ALT+1~4
		int gfxId_Sub = pc.getCurrentSpriteId();
		if (gfxId_Sub != L1PcInstance.CLASSID_PRINCE && gfxId_Sub != L1PcInstance.CLASSID_PRINCESS && gfxId_Sub != L1PcInstance.CLASSID_KNIGHT_MALE && gfxId_Sub != L1PcInstance.CLASSID_KNIGHT_FEMALE
				&& gfxId_Sub != L1PcInstance.CLASSID_ELF_MALE && gfxId_Sub != L1PcInstance.CLASSID_ELF_FEMALE && gfxId_Sub != L1PcInstance.CLASSID_WIZARD_MALE
				&& gfxId_Sub != L1PcInstance.CLASSID_WIZARD_FEMALE && gfxId_Sub != L1PcInstance.CLASSID_DARK_ELF_MALE && gfxId_Sub != L1PcInstance.CLASSID_DARK_ELF_FEMALE
				&& gfxId_Sub != L1PcInstance.CLASSID_DRAGONKNIGHT_MALE && gfxId_Sub != L1PcInstance.CLASSID_DRAGONKNIGHT_FEMALE && gfxId_Sub != L1PcInstance.CLASSID_BLACKWIZARD_MALE
				&& gfxId_Sub != L1PcInstance.CLASSID_BLACKWIZARD_FEMALE
				&& gfxId_Sub != L1PcInstance.CLASSID_WARRIOR_MALE
				&& gfxId_Sub != L1PcInstance.CLASSID_WARRIOR_FEMALE
				&& gfxId_Sub != L1PcInstance.CLASSID_FENCER_MALE && gfxId_Sub != L1PcInstance.CLASSID_FENCER_FEMALE) {
			Poly_Sub_Action(pc, actionId);
		} else {
			return;
		}
		
		if (pc.hasSkillEffect(SHAPE_CHANGE)) { // 為以防萬一，變身中不向其他玩家發送
			int gfxId = pc.getCurrentSpriteId();
			if (gfxId != 6080 && gfxId != 6094) {
				return;
			}
		} else {
			S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
			Broadcaster.broadcastPacket(pc, gfx);
		}

		/*if ((pc.getMapId() == 9101) && (pc.isInParty()))
			if (((pc.getParty().isLeader(pc) & pc.getX() == 32799)) && (pc.getY() == 32808) && (pc.getHeading() == 4)) {
				if (!pc.hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
					pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 3206));
				}
				if (actionId == 68) {
					OrimController.getInstance().explain = true;
				} else {
					int localL1PcInstance3;
					if ((actionId == 66) && (OrimController.getInstance().attackTrap().booleanValue()) && (pc.getHeading() == 4)) {
						L1PcInstance[] arrayOfL1PcInstance1;
						localL1PcInstance3 = (arrayOfL1PcInstance1 = OrimController.getInstance().getPlayMemberArray()).length;
						for (int localL1PcInstance1 = 0; localL1PcInstance1 < localL1PcInstance3; localL1PcInstance1++) {
							L1PcInstance pc1 = arrayOfL1PcInstance1[localL1PcInstance1];
							pc1.sendPackets(new S_SkillSound(pc1.getId(), 2029));
							pc1.broadcastPacket(new S_SkillSound(pc1.getId(), 2029));
						}
						L1PcInstance[] arrayOfL1PcInstance3;
						int i;
						if (OrimController.getInstance().getAtCount() % 2 == 0) {
							S_DoActionGFX gfxShell1 = new S_DoActionGFX(OrimController.getInstance().getShell1().getId(), 10242);
							Broadcaster.broadcastPacket(OrimController.getInstance().getShell1(), gfxShell1);
							i = (arrayOfL1PcInstance3 = OrimController.getInstance().getPlayMemberArray()).length;
							for (localL1PcInstance3 = 0; localL1PcInstance3 < i; localL1PcInstance3++) {
								L1PcInstance pc1 = arrayOfL1PcInstance3[localL1PcInstance3];
								pc1.sendPackets(new S_EffectLocation(32789, 32817, 8233));
							}
						} else {
							S_DoActionGFX gfxShell2 = new S_DoActionGFX(OrimController.getInstance().getShell2().getId(), 10242);
							Broadcaster.broadcastPacket(OrimController.getInstance().getShell2(), gfxShell2);
							i = (arrayOfL1PcInstance3 = OrimController.getInstance().getPlayMemberArray()).length;
							for (localL1PcInstance3 = 0; localL1PcInstance3 < i; localL1PcInstance3++) {
								L1PcInstance pc1 = arrayOfL1PcInstance3[localL1PcInstance3];
								pc1.sendPackets(new S_EffectLocation(32795, 32817, 8233));
							}
						}
						OrimController.getInstance().addScore(50);
						OrimController.getInstance().addAtCount();
					} else if ((actionId == 69) && (OrimController.getInstance().dependTrap().booleanValue()) && (pc.getHeading() == 4)) {
						L1PcInstance[] arrayOfL1PcInstance2;
						localL1PcInstance3 = (arrayOfL1PcInstance2 = OrimController.getInstance().getPlayMemberArray()).length;
						for (int localL1PcInstance2 = 0; localL1PcInstance2 < localL1PcInstance3; localL1PcInstance2++) {
							L1PcInstance pc1 = arrayOfL1PcInstance2[localL1PcInstance2];
							pc1.sendPackets(new S_SkillSound(pc1.getId(), 10165));
							pc1.sendPackets(new S_SkillSound(pc1.getId(), 2030));
							pc1.broadcastPacket(new S_SkillSound(pc1.getId(), 2030));
						}
						OrimController.getInstance().addDeCount();
					}
				}
			}*/
	}
	
	public void Poly_Sub_Action(L1PcInstance pc, int actionId) {
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime2() + 5 > curtime) {
			// long time = (pc.getQuizTime2() + 3) - curtime;
			// pc.sendPackets(time + "秒後可使用。");
			return;
		}
		if (actionId == 68) {
			int gfxid = 3204;
			pc.send_effect(gfxid, true);
			pc.setQuizTime2(curtime);
		} else if (actionId == 69) {
			int gfxid = 3205;
			pc.send_effect(gfxid, true);
			pc.setQuizTime2(curtime);
		} else if (actionId == 67) {
			int gfxid = 3206;
			pc.send_effect(gfxid, true);
			pc.setQuizTime2(curtime);
		} else if (actionId == 66) {
			int gfxid = 3207;
			pc.send_effect(gfxid, true);
			pc.setQuizTime2(curtime);
		}
	}
	

	@Override
	public String getType() {
		return C_EXTRA_COMMAND;
	}
}

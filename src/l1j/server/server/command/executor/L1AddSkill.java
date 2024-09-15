package l1j.server.server.command.executor;

import java.util.ArrayList;
import java.util.StringTokenizer;

import l1j.server.GameSystem.SkillBook.SkillBookInfo;
import l1j.server.GameSystem.SkillBook.SkillBookLoader;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveInfo;
import l1j.server.MJPassiveSkill.MJPassiveLoader;
import l1j.server.MJPassiveSkill.MJPassiveUserLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.SkillCheck;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1AddSkill implements L1CommandExecutor {

	private L1AddSkill() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1AddSkill();
	}

	@Override
	public void execute(L1Object gm, String cmdName, String arg) {
		try {
			int cnt = 0; // 迴圈計數器
			String skill_name = ""; // 技能名稱
			int skill_id = 0; // 技能 ID
			
			StringTokenizer st = new StringTokenizer(arg);
			String charname = st.nextToken();
			L1PcInstance pc = L1World.getInstance().getPlayer(charname);
			
			if(pc == null){
				gm.sendPackets(String.valueOf(new S_SystemMessage("\f3[" + charname + "] 不是在線上的角色。")));
			}
			int object_id = pc.getId(); // 獲取角色的 objectid
			pc.sendPackets(String.valueOf(new S_SkillSound(object_id, 'ã'))); // 播放學習魔法的音效
			pc.broadcastPacket(new S_SkillSound(object_id, '\343'));
			ArrayList<MJPassiveInfo> passives = null;
			ArrayList<SkillBookInfo> skills = null;
			ArrayList<SkillBookInfo> skills1 = null;
			SkillBookLoader.getInstance().setMaster(true);
			if (pc.isCrown()) {
				passives = MJPassiveLoader.getInstance().fromClassType(0);
				skills = SkillBookLoader.getInstance().fromClassType(0);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			} else if (pc.isKnight()) {
				passives = MJPassiveLoader.getInstance().fromClassType(1);
				skills = SkillBookLoader.getInstance().fromClassType(1);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			} else if (pc.isElf()) {
				passives = MJPassiveLoader.getInstance().fromClassType(2);
				skills = SkillBookLoader.getInstance().fromClassType(2);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			} else if (pc.isWizard()) {
				passives = MJPassiveLoader.getInstance().fromClassType(3);
				skills = SkillBookLoader.getInstance().fromClassType(3);				
			} else if (pc.isDarkelf()) {
				passives = MJPassiveLoader.getInstance().fromClassType(4);
				skills = SkillBookLoader.getInstance().fromClassType(4);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			} else if (pc.isDragonknight()) {
				passives = MJPassiveLoader.getInstance().fromClassType(5);
				skills = SkillBookLoader.getInstance().fromClassType(5);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			} else if (pc.isBlackwizard()) {
				passives = MJPassiveLoader.getInstance().fromClassType(6);
				skills = SkillBookLoader.getInstance().fromClassType(6);
			} else if (pc.is전사()) {
				passives = MJPassiveLoader.getInstance().fromClassType(7);
				skills = SkillBookLoader.getInstance().fromClassType(7);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			} else if (pc.isFencer()) {
				passives = MJPassiveLoader.getInstance().fromClassType(8);
				skills = SkillBookLoader.getInstance().fromClassType(8);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			} else if (pc.isLancer()) {
				passives = MJPassiveLoader.getInstance().fromClassType(9);
				skills = SkillBookLoader.getInstance().fromClassType(9);
				skills1 = SkillBookLoader.getInstance().fromClassType(3);
			}
			if (skills != null) {
				for (SkillBookInfo sInfo : skills) {
					if (pc.isSkillMastery(sInfo.getSkillId())) {
						continue;
					}
					if (!sInfo.isMasterPossible()) {
						continue;
					}
					SkillBookLoader.getInstance().masterSkill(pc, sInfo, false);
				}
			}
			
			if (skills1 != null) {
				for (SkillBookInfo sInfo : skills1) {
					if (pc.isSkillMastery(sInfo.getSkillId())) {
						continue;
					}
					if (!sInfo.isMasterPossible()) {
						continue;
					}
					SkillBookLoader.getInstance().masterSkill(pc, sInfo, false);
				}
			}
			SkillBookLoader.getInstance().setMaster(false);
			if(passives != null){
				for(MJPassiveInfo pInfo : passives){
					if(pc.isPassive(pInfo.getPassiveId()))
						continue;
					
					int passiveId = pInfo.getPassiveId();
					if(passiveId == MJPassiveID.DOUBLE_BREAK_DESTINY.toInt()){
						if(pc.hasSkillEffect(L1SkillId.DOUBLE_BRAKE))
							pc.removeSkillEffect(L1SkillId.DOUBLE_BRAKE);
					}
					
					if(passiveId == MJPassiveID.TACTICAL_ADVANCE.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
						buff.set_on_icon_id(10154);
						buff.set_tooltip_str_id(8030);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
						
						pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_SPMR(pc));
					}
					pc.addPassive(pInfo);
					MJPassiveUserLoader.store(pc, pInfo, false);
				}
			}		
			SkillCheck.getInstance().sendAllSkillList(pc);
			gm.sendPackets(String.valueOf(new S_SystemMessage("\f3[" + charname + "] 角色技能掌握完成")));
		} catch (Exception e) {
			gm.sendPackets(String.valueOf(new S_SystemMessage("\f3請輸入命令：.技能掌握 [角色名]")));
		}
	}
}

package l1j.server.MJItemSkillSystem.Model.Attack;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_ChainSpot extends MJItemSkillModel {

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if (MJCommons.isUnbeatable(defender) || !isPlay(attacker, t_item))
			return 0D;

		int pure = getProbability(attacker, t_item);
		if (is_magic) {
			if (!MJCommons.isMagicSuccess(attacker, defender, pure))
				return 0D;

			if (isCounterMagic(defender))
				return 0D;
		} else {
			if (!isInPercent(pure))
				return 0D;
		}

		if (defender.hasSkillEffect(L1SkillId.CHAINSWORD1)) {
			defender.killSkillEffectTimer(L1SkillId.CHAINSWORD1);// 暴露弱點
		}

		defender.setSkillEffect(L1SkillId.CHAINSWORD1, 8000);
		defender.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
		defender.send_effect(21932);
		if (defender instanceof L1PcInstance) {
			L1PcInstance defenderpc = (L1PcInstance) defender;
			defenderpc.getAC().addAc(-5);
			defenderpc.addDg(-10);
			defenderpc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
			defenderpc.sendPackets(new S_OwnCharAttrDef(defenderpc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(defenderpc);
			L1SkillUse.on_icons(defenderpc, L1SkillId.CHAINSWORD1, 8);
		}

		if (eff_id > 0) {
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
		}
		return 0D;
	}
}

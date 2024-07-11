package l1j.server.MJCharacterActionSystem;

import static l1j.server.server.model.skill.L1SkillId.FIRE_WALL;
import static l1j.server.server.model.skill.L1SkillId.LIFE_STREAM;
import static l1j.server.server.model.skill.L1SkillId.MASS_TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.SUMMON_MONSTER;
import static l1j.server.server.model.skill.L1SkillId.TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.TRUE_TARGET;
import static l1j.server.server.model.skill.L1SkillId.VISION_TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.SHADOW_STEP;

import java.util.HashMap;

import l1j.server.MJCharacterActionSystem.Spell.BackgroundActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.DirectionActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.ShadowStepActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.SpellActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.SummonMonsterActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.TeleportActionHandler;
import l1j.server.MJCharacterActionSystem.Spell.TruetargetActionHandler;

public class SpellActionHandlerFactory {
	private static final HashMap<Integer, SpellActionHandler> _handlers;
	static {
		// TODO技能最大數量（技能表註冊後必須增加）
		_handlers = new HashMap<Integer, SpellActionHandler>(5159);
		for (int i = 5159; i > 0; --i) {// 5105
			// System.out.println("스펠번호: "+i);
			switch (i) {
				case TRUE_TARGET:
					_handlers.put(i, new TruetargetActionHandler());
					break;
				case TELEPORT:
				case MASS_TELEPORT:
					_handlers.put(i, new TeleportActionHandler());
					break;
				case SUMMON_MONSTER:
					_handlers.put(i, new SummonMonsterActionHandler());
					break;
				case FIRE_WALL:
				case LIFE_STREAM:
				case VISION_TELEPORT:
					_handlers.put(i, new BackgroundActionHandler());
					break;
				default:
					_handlers.put(i, new DirectionActionHandler());
					break;
			}
		}
	}

	public static SpellActionHandler create(int skillId) {
		SpellActionHandler default_handler = _handlers.get(skillId);
		if (default_handler == null)
			return null;

		return default_handler.copy().setSkillId(skillId);
	}
}

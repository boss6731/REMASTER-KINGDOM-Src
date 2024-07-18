package l1j.server.server.serverpackets;

import java.util.concurrent.atomic.AtomicInteger;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.Opcodes;

public class S_UseAttackSkill extends ServerBasePacket {



	private static final String S_USE_ATTACK_SKILL = "[S] S_UseAttackSkill";
	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

	// 構造函數
	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, 6, true);
	}

	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, boolean motion) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, 0, motion);
	}

	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, isHit, true);
	}

	public S_UseArrowSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, boolean isHit) {
		buildPacket(cha, targetobj, spellgfx, x, y, determineActionId(cha), isHit ? 6 : 0, true);
	}

	// buildPacket 方法
	private void buildPacket(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit, boolean withCastMotion) {
		if (cha instanceof L1PcInstance) {
// 圖影系變身中使用攻擊魔法，客戶端會崩潰，因此采取臨時措施
			if (cha.hasSkillEffect(L1SkillId.SHAPE_CHANGE) && actionId == ActionCodes.ACTION_SkillAttack) {
				int tempchargfx = cha.getCurrentSpriteId();
				if (tempchargfx == 5727 || tempchargfx == 5730) {
					actionId = ActionCodes.ACTION_SkillBuff;
				} else if (tempchargfx == 5733 || tempchargfx == 5736) {
					actionId = ActionCodes.ACTION_Attack;
				}
			}
		}
		if (cha.equalsCurrentSprite(4013))
			actionId = ActionCodes.ACTION_Attack;

		int newheading = calcheading(cha.getX(), cha.getY(), x, y);
		cha.setHeading(newheading);

		writeC(Opcodes.S_ATTACK);
		writeC(actionId);
		writeD(withCastMotion ? cha.getId() : 0);
		writeD(targetobj);
		writeC(isHit);
		writeC(0);
		writeC(newheading);
		writeD(_sequentialNumber.incrementAndGet());
		writeH(spellgfx);
		writeC(6); // 目標種類:6, 範圍&目標種類:8, 範圍:0
		writeH(cha.getX());
		writeH(cha.getY());
		writeH(x);
		writeH(y);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
	}

	// determineActionId 方法
	private int determineActionId(L1Character cha) {
		int spriteId = cha.getCurrentSpriteId();
		if (!(cha instanceof L1PcInstance)) {
			if (spriteId == 15659) {
				return cha.isMobTripleArrow_PRISON() ? 75 : 1;
			}
		}
		if (spriteId == 3860 || spriteId == 11382) {
			return 21;
		}
		return 1;
	}

	// calcheading 方法
	private static int calcheading(int myx, int myy, int tx, int ty) {
		int newheading = 0;
		if (tx > myx && ty > myy) {
			newheading = 3;
		}
		if (tx < myx && ty < myy) {
			newheading = 7;
		}
		if (tx > myx && ty == myy) {
			newheading = 2;
		}
		if (tx < myx && ty == myy) {
			newheading = 6;
		}
		if (tx == myx && ty < myy) {
			newheading = 0;
		}
		if (tx == myx && ty > myy) {
			newheading = 4;
		}
		if (tx < myx && ty > myy) {
			newheading = 5;
		}
		if (tx > myx && ty < myy) {
			newheading = 1;
		}
		return newheading;
	}

	// 覆蓋 getContent 方法
	@override
	public byte[] getContent() {
		byte[] b = getBytes();
/**
		7月 26日 方向錯誤修正，本服方向值已更改
				**/
		/*
		int seq = _sequentialNumber.incrementAndGet();
		b[12] = (byte) (seq & 0xff);
		b[13] = (byte) (seq >> 8 & 0xff);
		b[14] = (byte) (seq >> 16 & 0xff);
		b[15] = (byte) (seq >> 24 & 0xff);
		*/
		return b;
	}

	// 覆蓋 getType 方法
	@override
	public String getType() {
		return S_USE_ATTACK_SKILL;
	}
}



package l1j.server.MJCharacterActionSystem.Wand;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class WandActionHandler extends AbstractActionHandler implements MJPacketParser {
	public static WandActionHandler create(L1ItemInstance item) {
		switch (item.getItemId()) {
			case 40007:
			case 40412:
				return new WandActionHandler(item, 10, 0.16D);
			case 40006:
			case 140006:
				return new WandActionHandler(item, 11736, 0.5D);
			case 420104:
				return new WandActionHandler(item, 762, 0.5D);
			case 420108:
				return new WandActionHandler(item, 16060, 0.5D);
			case 420111:
				return new WandActionHandler(item, 3924, 0.5D);
		}

		return null;
	}

	protected L1Character target;
	protected int tId;
	protected int tX;
	protected int tY;
	protected L1ItemInstance item;
	protected int spriteId;
	protected double damageRevision;

	public WandActionHandler(L1ItemInstance item, int spriteId, double damageRevision) {
		this.item = item;
		this.spriteId = spriteId;
		this.damageRevision = damageRevision;
	}

	@Override
	public void dispose() {
		target = null;
		item = null;
		super.dispose();
	}

	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tId = pck.readD();
		tX = pck.readH();
		tY = pck.readH();
	}

	@Override
	public void doWork() {
		int delay = item.getItem().get_delaytime();
		if (delay > 0) {
			if (owner.hasItemDelayTime(item)) {
				dispose();
				return;
			}
			owner.addItemDelayTime(item);
		}

		if (!owner.isPassive(MJPassiveID.MEDITATION_BEYOND.toInt())) {
			owner.removeSkillEffect(L1SkillId.MEDITATION);
		}

		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) {
			owner.removeSkillEffect(ABSOLUTE_BARRIER);
		}
		owner.delInvis();
		register();
	}

	@Override
	public void handle() {
		if (!validation()) {
			sendEffect(tX, tY, false);
			return;
		}

		int dmg = 0;

		if (item.getItemId() == 420104 || item.getItemId() == 420108 || item.getItemId() == 420111) {
			int range = 0;
			int damage = 0;
			switch (item.getItemId()) {
				case 420104:
					range = 4;
					damage = 100;
					break;
				case 420108:
					range = 4;
					damage = 100;
					break;
				case 420111:
					range = 4;
					damage = 100;
					break;
				default:
					break;
			}
			boolean targetLocType = target != null;
			L1Location targetLoc = targetLocType ? target.getLocation() : new L1Location(tX, tY, owner.getMapId());
			L1MonsterInstance mon = null;
			for (L1Object obj : L1World.getInstance().getVisiblePoint(targetLoc, range)) {
				if (obj == null) {
					continue;
				}
				if (!(obj instanceof L1Character)) {
					continue;
				}
				if (obj instanceof L1MonsterInstance) {
					mon = (L1MonsterInstance) obj;
					if ((!targetLocType && !mon.isDead())
							|| (targetLocType && !mon.isDead() && mon.getId() != target.getId())) {
						mon.setStatus(ActionCodes.ACTION_Damage);
						Broadcaster.broadcastPacket(mon, new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage),
								true);
						// Broadcaster.broadcastPacket(mon, new S_DoActionGFX(mon.getId(), 2), true);

					}
					mon.receiveDamage(owner, damage);
				}
			}
			sendEffect(tX, tY, dmg > 0);
			if (!targetLocType) {
				targetLoc = null;
			}
		} else {
			try {
				if (target.instanceOf(MJL1Type.L1TYPE_PC)) {
					if (!MJCommons.isCounterMagic(target)) {
						dmg = 60 - (target.getResistance().getMrAfterEraseRemove() / 10);
						// dmg = 60 - (target.getResistance().getMr() / 10);
						dmg = (int) (dmg * damageRevision);
						dmg += MJRnd.next(5);
						dmg = Math.max(1, dmg);
						target.receiveDamage(owner, dmg);
					}
					L1PinkName.onAction(target, owner);
				} else if (target.instanceOf(MJL1Type.L1TYPE_MONSTER)) {
					dmg = 10 + (owner.getLevel() / 4);
					dmg = (int) (dmg * damageRevision);
					dmg += MJRnd.next(5);
					target.receiveDamage(owner, dmg);
				} else {
					dmg = 1;
				}
			} finally {
				sendEffect(tX, tY, dmg > 0);
			}
		}
	}

	@Override
	public boolean validation() {
		if (owner == null || owner.is_non_action() || owner.isDead() || MJCommons.isNonAction(owner) || owner.isGhost()
				|| owner.get_teleport() || owner.isInvisDelay())
			return false;
		if ((item.getItemId() == 420104 || item.getItemId() == 420108 || item.getItemId() == 420111)
				&& !(owner.getMapId() >= 732 && owner.getMapId() <= 776)) {
			owner.sendPackets(new S_ServerMessage(563)); // 這裡不能使用它。
			return false;
		}

		try {
			if (owner.isOnTargetEffect()) {
				long cur = System.currentTimeMillis();
				if (cur - owner.getLastNpcClickMs() > 1000) {
					owner.setLastNpcClickMs(cur);
					owner.sendPackets(new S_SkillSound(tId, 14486));
				}
			}

			L1Object obj = L1World.getInstance().findObject(tId);
			if (obj == null || !obj.instanceOf(MJL1Type.L1TYPE_CHARACTER))
				return false;

			if (!owner.glanceCheck(tX, tY) || owner.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY)
				return false;

			target = (L1Character) obj;
			if (target.isDead() || MJCommons.isUnbeatable(target) || target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
				return false;

			if (target.instanceOf(MJL1Type.L1TYPE_PC) && (owner.getZoneType() == 1 || target.getZoneType() == 1))
				return false;
			else if (target.instanceOf(MJL1Type.L1TYPE_MONSTER) && target.getKarma() > 0)
				return false;
		} finally {
			consumeWand();
		}
		return true;
	}

	/*
	 * private void sendEffect(int targetId){
	 * S_UseAttackSkill s = new S_UseAttackSkill(owner, targetId, spriteId, tX, tY,
	 * EActionCodes.wand.toInt());
	 * owner.sendPackets(s, false);
	 * owner.broadcastPacket(s);
	 * }
	 */

	private void sendEffect(int x, int y, boolean is_hit) {
		if (target == null) {
			S_DoActionGFX gfx = new S_DoActionGFX(owner.getId(), EActionCodes.wand.toInt());
			owner.sendPackets(gfx, false);
			owner.broadcastPacket(gfx);
		} else {
			S_AttackPacket pck = new S_AttackPacket(target, owner.getId(), EActionCodes.wand.toInt());
			owner.sendPackets(pck, false);
			owner.broadcastPacket(pck);
		}

		if (target != null) {
			target.send_effect(spriteId);
		} else {
			S_EffectLocation s = new S_EffectLocation(x, y, spriteId);
			owner.sendPackets(s, false);
			owner.broadcastPacket(s);
		}

		if (is_hit && target != null) {
			target.send_action(EActionCodes.damage.toInt());
		}
	}

	private void consumeWand() {
		if (item.getItemId() == 40412)
			owner.getInventory().removeItem(item, 1);
		else {
			item.setChargeCount(item.getChargeCount() - 1);
			if (item.getChargeCount() <= 0)
				owner.getInventory().removeItem(item);
			else
				owner.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_WAND;
	}

	@Override
	public long getInterval() {
		// return 0;
		return owner.hasSkillEffect(L1SkillId.SHAPE_CHANGE) || owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER)
				|| owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER2) ? 10L
						: owner.getCurrentSpriteInterval(EActionCodes.wand);
	}

	@Override
	public boolean empty() {
		return false;
	}
}

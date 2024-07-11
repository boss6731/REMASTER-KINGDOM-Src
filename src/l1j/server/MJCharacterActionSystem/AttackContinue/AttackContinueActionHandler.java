package l1j.server.MJCharacterActionSystem.AttackContinue;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.io.IOException;

import l1j.server.ForgottenIsland.FIController;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Attack.AttackActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.QueenAntSystem.QueenAntController;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.SkillCheck;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.MJCommons;

public class AttackContinueActionHandler extends AbstractActionHandler implements MJPacketParser {
	protected int tId;
	protected L1Character target;
	protected boolean isUnregister;

	public AttackContinueActionHandler() {
		isUnregister = false;
	}

	@Override
	public void dispose() {
		target = null;
		super.dispose();
	}

	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tId = pck.readD();

		if (tId == owner.getId()) {
			dispose();
			return;
		}

		if (owner.isOnTargetEffect()) {
			long cur = System.currentTimeMillis();
			if (cur - owner.getLastNpcClickMs() > 1000) {
				owner.setLastNpcClickMs(cur);
				owner.sendPackets(new S_SkillSound(tId, 14486));
			}
		}
	}

	@Override
	public void doWork() {
		try {
			if (owner == null) {
				return;
			}
			if (!owner.isGm() && owner.isInvisDelay()) {
				return;
			}

			if (!owner.isPassive(MJPassiveID.MEDITATION_BEYOND.toInt())) {
				owner.removeSkillEffect(L1SkillId.MEDITATION);
			}

			/*
			 * if (owner.hasSkillEffect(L1SkillId.MOEBIUS)) {
			 * owner.removeSkillEffect(L1SkillId.MOEBIUS); }
			 */

			if (owner.hasSkillEffect(L1SkillId.DARK_BLIND)) {
				owner.removeSkillEffect(L1SkillId.DARK_BLIND);
			}

			owner.setRegenState(REGENSTATE_ATTACK);
			// owner.setTelDelay(1000);
			register();
		} catch (Exception e) {
			System.out.println("owner2: " + owner);
			System.out.println("owner.isInvisDelay()2: " + owner.isInvisDelay());
			e.printStackTrace();
		}
	}

	@Override
	public void handle() {

		if (!validation())
			return;

		if (owner.getWeapon() != null && owner.getWeapon().getItemId() != 190 && owner.getWeapon().getItemId() != 10000
				&& owner.getWeapon().getItemId() != 202011) {
			if (owner.getWeapon().getItem().getType2() == 1
					&& (owner.getWeapon().getItem().getType() == 4 || owner.getWeapon().getItem().getType() == 10
							|| owner.getWeapon().getItem().getType() == 13)
					&& owner.getInventory().getArrow() == null) {
				owner.sendPackets(4492);
				return;
			}
		}

		if (!QueenAntController.isReady() && owner.getMapId() >= 15871 && owner.getMapId() <= 15899) {
			owner.start_teleport(33444, 32799, 4, owner.getHeading(), 18339, false);
		}
		if (!FIController.isReady() && owner.getMapId() == 70) {
			owner.start_teleport(33444, 32799, 4, owner.getHeading(), 18339, false);
		}
		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) {
			owner.removeSkillEffect(ABSOLUTE_BARRIER);
		}

		/*
		 * System.out.println((System.currentTimeMillis() -
		 * owner.getLastMoveActionMillis()) + " " + getInterval());
		 * owner.setLastMoveActionMillis(System.currentTimeMillis());
		 */
		target.onAction(owner);
		if (target instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) target;
			if (!pc.isGmInvis() && pc.isInvisble() && !pc.isGhost()) {
				pc.delInvis();
				pc.beginInvisTimer();
			}
		}
		if (owner.isElf() && owner.isAutoTreeple && !target.isDead()
				&& !owner.hasAction(CharacterActionExecutor.ACTION_IDX_SPELL)) {
			if (owner.getCurrentMp() >= 15 && SkillCheck.getInstance().CheckSkill(owner, L1SkillId.TRIPLE_ARROW)) {
				if (owner.lastSpellUseMillis < System.currentTimeMillis() - 300L) {
					BinaryOutputStream bos = new BinaryOutputStream();
					bos.writeC(Opcodes.C_USE_SPELL);
					bos.writeC(16);
					bos.writeC(3);
					bos.writeD(target.getId());
					bos.writeH(target.getX());
					bos.writeH(target.getY());
					final byte[] buff = bos.getBytes();
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					GeneralThreadPool.getInstance().execute(new Runnable() {
						@Override
						public void run() {
							if (target != null && !target.isDead() && owner != null && !owner.isDead()) {
								try {
									owner.getNetConnection().handle(buff);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
			}
		}
	}

	@Override
	public boolean validation() {
		if (tId <= 0) {
			unregister();
			return false;
		}

		if (owner.isGmInvis() || owner.is_non_action() || owner.isFishing()
				|| owner.isDead() /* || (owner.isInvisble()) */ || owner.isGhost() || owner.get_teleport()
				|| owner.isInvisDelay()) {
			unregister();
			return false;
		}

		if (MJCommons.isNonAction(owner))
			return false;

		if (!owner.is_top_ranker() && owner.getInventory().getWeight100() > 82) {
			owner.sendPackets(new S_ServerMessage(110));
			return false;
		}

		L1Object obj = null;
		if (tId > 0) {
			obj = L1World.getInstance().findObject(tId);
		} else {
			// obj = L1World.getInstance().findVisiblePlayerFromPosition(x, y,
			// owner.getMapId());
		}
		if (!owner.isGm()) {
			if (obj != null && obj instanceof L1PcInstance) {
				if (owner.getCurrentSpriteId() == 12015 || owner.getCurrentSpriteId() == 5641
						|| owner.getCurrentSpriteId() == 11685 || owner.getCurrentSpriteId() == 11620
						|| owner.getCurrentSpriteId() == 11621) {
					owner.sendPackets("傳說/神話變身狀態下無法進行PVP。");
					return false;
				}
			}
		}

		if (obj == null || !obj.instanceOf(MJL1Type.L1TYPE_CHARACTER)) {
			if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_CLANJOIN)) {
				AttackActionHandler.do_clanjoin(owner, (L1ClanJoinInstance) obj);
			}
			unregister();
			return false;
		}

		target = (L1Character) obj;
		if (target.isDead() || owner.getMapId() != target.getMapId()) {
			unregister();
			return false;
		}

		if (target instanceof L1PcInstance) {
			L1PcInstance targetPc = (L1PcInstance) target;
			if (targetPc.isGm() && target.isInvisble()) {
				unregister();
				return false;
			}
		}

		int tx = target.getX();
		int ty = target.getY();
		int mid = owner.getMapId();
		int ox = owner.getX();
		int oy = owner.getY();
		if (!(mid >= 2600 && mid <= 2699) && owner.getInventory().checkEquipped(203003)) {
			unregister();
			return false;
		}

		SpriteInformation sInfo = target.getCurrentSprite();
		int cx = Math.abs(ox - tx);
		int cy = Math.abs(oy - ty);
		int r = owner.getAttackRang();
		if (cx > sInfo.getWidth() + r || cy > sInfo.getHeight() + r) {
			if (owner.getWeapon() != null)
				unregister();// 因中斷而引起的注意
			return false;
		}

		int previous_heading = owner.getHeading();
		int next_heading = owner.targetDirection(tx, ty);
		if (previous_heading != next_heading) {
			owner.setHeading(next_heading);
			// TODO 拍攝仙女時，隨著航向的變化，動作會變得奇怪（下面的來源似乎是魔術師關鍵連結的方向）
			if (owner.isBlackwizard()) {
				S_ChangeHeading heading = new S_ChangeHeading(owner);
				owner.sendPackets(heading, false);
				owner.broadcastPacket(heading, true);
			}
		}

		return true;
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_ATTACKCONTINUE;
	}

	@Override
	public void unregister() {
		isUnregister = true;
		super.unregister();
	}

	@Override
	public boolean empty() {
		return false;
	}

	@Override
	public long getInterval() {
		return isUnregister ? 10L : owner.getCurrentSpriteInterval(EActionCodes.fromInt(owner.getCurrentWeapon() + 1));
	}
}

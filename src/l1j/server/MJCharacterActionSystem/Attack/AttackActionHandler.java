package l1j.server.MJCharacterActionSystem.Attack;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.io.IOException;

import l1j.server.IndunSystem.MiniGame.L1Gambling3;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
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
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.MJCommons;

public class AttackActionHandler extends AbstractActionHandler implements MJPacketParser {
	public static void do_clanjoin(L1PcInstance pc, L1ClanJoinInstance cj) {
		int tx = cj.getX();
		int ty = cj.getY();
		int ox = pc.getX();
		int oy = pc.getY();
		int cx = Math.abs(ox - tx);
		int cy = Math.abs(oy - ty);
		int r = pc.getAttackRang();
		if (cx > r || cy > r)
			return;

		int h = MJCommons.calcheading(ox, oy, tx, ty);
		if (!pc.getMap().isUserPassable(ox, oy, h))
			return;

		cj.receiveDamage(pc, 0);
		pc.setHeading(pc.targetDirection(tx, ty));
		S_AttackStatus s = new S_AttackStatus(pc, 0, pc.getCurrentWeapon() + 1);
		pc.sendPackets(s, false);
		pc.broadcastPacket(s);
	}

	protected int tId;
	protected int x;
	protected int y;
	protected L1Character c;

	@Override
	public void dispose() {
		super.dispose();
		c = null;
	}

	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tId = pck.readD();
		x = pck.readH();
		y = pck.readH();

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

			if (owner.isDarkelf()) {
				if (owner.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
					if (owner.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
						owner.delBlindHiding();
					}
				}
			}

			/*
			 * if (owner.hasSkillEffect(L1SkillId.MOEBIUS)) {
			 * owner.removeSkillEffect(L1SkillId.MOEBIUS);
			 * }
			 */

			if (owner.hasSkillEffect(L1SkillId.DARK_BLIND)) {
				owner.removeSkillEffect(L1SkillId.DARK_BLIND);
			}

			owner.setRegenState(REGENSTATE_ATTACK);
			// owner.setTelDelay(1000);
			register();
		} catch (Exception e) {
			System.out.println("owner1: " + owner);
			System.out.println("owner.isInvisDelay()1: " + owner.isInvisDelay());
			e.printStackTrace();
		}
	}

	@Override
	public void handle() {
		if (!validation())
			return;

		/*
		 * System.out.println((System.currentTimeMillis() -
		 * owner.getLastMoveActionMillis()) + " " + getInterval());
		 * owner.setLastMoveActionMillis(System.currentTimeMillis());
		 */
		if (owner.getWeapon() != null && owner.getWeapon().getItemId() != 190 && owner.getWeapon().getItemId() != 10000
				&& owner.getWeapon().getItemId() != 202011) {
			if (owner.getWeapon().getItem().getType2() == 1
					&& (owner.getWeapon().getItem().getType() == 4 || owner.getWeapon().getItem().getType() == 10
							|| owner.getWeapon().getItem().getType() == 13)
					&& owner.getInventory().getArrow() == null) {
				owner.sendPackets(4492); // 彈丸不夠。
				return;
			}
		}

		if (!QueenAntController.isReady() && owner.getMapId() >= 15871 && owner.getMapId() <= 15899) {
			owner.start_teleport(33444, 32799, 4, owner.getHeading(), 18339, false);
		}

		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) {
			owner.removeSkillEffect(ABSOLUTE_BARRIER);
		}
		owner.delInvis();
		int previous_heading = owner.getHeading();
		int next_heading = owner.targetDirection(x, y);
		if (previous_heading != next_heading) {
			owner.setHeading(next_heading);
			if (owner.isBlackwizard()) {
				S_ChangeHeading heading = new S_ChangeHeading(owner);
				owner.sendPackets(heading, false);
				owner.broadcastPacket(heading, true);
			}
		}
		if (c != null) {
			if (c.hasSkillEffect(L1SkillId.INVISIBILITY)) {
				if (c instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) c;
					pc.removeSkillEffect(L1SkillId.INVISIBILITY);
					pc.delInvis();
				} else if (c instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) c;
					mon.removeSkillEffect(L1SkillId.INVISIBILITY);
					mon.delInvis();
				}
			}
			if (c.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
				if (c instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) c;
					pc.delBlindHiding();
				}
			}
		}

		if (c == null || c.isDead()) {
			S_AttackStatus s = new S_AttackStatus(owner, 0, owner.getCurrentWeapon() + 1);
			owner.sendPackets(s, false);
			owner.broadcastPacket(s);
		} else {
			c.onAction(owner);
			if (c instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) c;
				if (!pc.isGmInvis() && pc.isInvisble() && !pc.isGhost()) {
					pc.delInvis();
					pc.beginInvisTimer();
				}
			}
			if (owner.isElf() && owner.isAutoTreeple && !c.isDead()
					&& !owner.hasAction(CharacterActionExecutor.ACTION_IDX_SPELL)) {
				if (owner.getCurrentMp() >= 15 && SkillCheck.getInstance().CheckSkill(owner, L1SkillId.TRIPLE_ARROW)) {
					if (owner.lastSpellUseMillis < System.currentTimeMillis() - 300L) {
						BinaryOutputStream bos = new BinaryOutputStream();
						bos.writeC(Opcodes.C_USE_SPELL);
						bos.writeC(16);
						bos.writeC(3);
						bos.writeD(tId);
						bos.writeH(x);
						bos.writeH(y);
						final byte[] buff = bos.getBytes();
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						GeneralThreadPool.getInstance().execute(new Runnable() {
							@Override
							public void run() {
								if (c != null && !c.isDead() && owner != null && !owner.isDead()) {
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
	}

	@Override
	public boolean validation() {
		if (owner == null)
			return false;

		if (owner.isGmInvis() || owner.is_non_action() || MJCommons.isNonAction(owner) || owner.isDead()
				|| owner.isGhost()
				|| owner.get_teleport() || owner.isInvisDelay())
			return false;

		// if (owner.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())){
		// if (owner.hasSkillEffect(L1SkillId.BLIND_HIDING))
		// return true;
		// }

		if (!owner.is_top_ranker() && owner.getInventory().getWeight100() > 82) {
			owner.sendPackets(new S_ServerMessage(110));
			return false;
		}

		int mid = owner.getMapId();
		int ox = owner.getX();
		int oy = owner.getY();
		int oh = owner.getHeading();
		if (!(mid >= 2600 && mid <= 2699) && owner.getInventory().checkEquipped(203003)) {
			owner.sendPackets("只能在火龍的安息處使用。");
			return false;
		}

		owner.offFishing();
		L1Object obj = null;
		if (tId > 0) {
			obj = L1World.getInstance().findObject(tId);
		} else {
			obj = L1World.getInstance().findVisiblePlayerFromPosition(x, y, owner.getMapId());
		}
		// 뉴 방패 뽕데스 : 12015
		// 뉴 뽕데스: 5641 / 11685
		// 88:11620/헬바인 86:11621
		if (!owner.isGm()) {
			if (obj != null && obj instanceof L1PcInstance) {
				if (owner.getCurrentSpriteId() == 12015 || owner.getCurrentSpriteId() == 5641
						|| owner.getCurrentSpriteId() == 11685
						|| owner.getCurrentSpriteId() == 11620 || owner.getCurrentSpriteId() == 11621) {
					owner.sendPackets("傳說/神話變身狀態下無法進行PVP。");
					return false;
				}
			}
		}

		if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_NPC)) {
			if (((L1NpcInstance) obj).getHiddenStatus() != 0)
				return false;
		}

		if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_CHARACTER)) {
			if (obj.instanceOf(MJL1Type.L1TYPE_NPC)) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (ox == 33515 && oy == 32851 && mid == 4) {
					if (npc.getNpcId() == 300027) {
						L1Gambling3 gam3 = new L1Gambling3();
						gam3.dealerTrade(owner);
					}
				}

				if (npc.getHiddenStatus() != 0)
					return false;
			}

			this.c = (L1Character) obj;
			SpriteInformation sInfo = c.getCurrentSprite();
			int cx = Math.abs(ox - x);
			int cy = Math.abs(oy - y);
			int r = owner.getAttackRang();

			if (r <= 3) {
				if (cx > sInfo.getWidth() + r || cy > sInfo.getHeight() + r) {
					if (System.currentTimeMillis() - owner.getLastMoveActionMillis() > 1000) {
						return false;
					}
					int diff_h = Math.abs(MJCommons.calcheading(ox, oy, c.getX(), c.getY()) - oh);
					if (diff_h <= 1 && cx < sInfo.getWidth() + r + 5 && cy < sInfo.getHeight() + r + 5)
						return true;
					return false;
				}
			}

			if (c.getLocation().getTileLineDistance(x, y) > 1)
				return false;

			if (!owner.glanceCheck(x, y)) {
				return false;
			}

		} else if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_CLANJOIN)) {
			do_clanjoin(owner, (L1ClanJoinInstance) obj);
			return false;
		}

		return true;
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_ATTACK;
	}

	@Override
	public long getInterval() {
		return owner.getCurrentSpriteInterval(EActionCodes.fromInt(owner.getCurrentWeapon() + 1));
	}

	@Override
	public boolean empty() {
		return false;
	}
}

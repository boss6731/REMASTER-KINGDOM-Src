package l1j.server.MJ3SEx.IntervalDecorator;

import l1j.server.Config;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.skill.L1SkillId;

public class ActionIntervalDecoratorFactory {
	public static final double MOVE_HASTE_RETARDATION = 0.50D;
	public static final double MOVE_BRAVE_RETARDATION = 0.40D;
	public static final double MOVE_THIRD_RETARDATION = 0.10D;
	public static final double MOVE_DRAGON_PEARL_RETARDATION = 0.20D;
	public static final double MOVE_FOUR_GEAR_RETARDATION = 0.20D;
	public static final double MOVE_HASTE_ACCELERATION = MOVE_HASTE_RETARDATION / 2D;
	public static final double MOVE_BRAVE_ACCELERATION = MOVE_BRAVE_RETARDATION / 2D;
	public static final double MOVE_THIRD_ACCELERATION = MOVE_THIRD_RETARDATION / 2D;
	public static final double MOVE_DRAGON_PEARL_ACCELERATION = MOVE_DRAGON_PEARL_RETARDATION / 2D;
	public static final double MOVE_FOUR_GEAR_ACCELERATION = MOVE_FOUR_GEAR_RETARDATION / 2D;

	public static final ActionIntervalDecorator<L1PcInstance> PC_WALK_DECORATOR = new ActionIntervalDecorator<L1PcInstance>() {
		@Override
		public double decoration(L1PcInstance owner, double interval) {
			int accelerate_check = 0;
			double accelerate = 0;
			double complete_accelerate = 0;
			boolean dragon_pearl = false;
			if (owner != null) {
				if (owner.isHaste())
					accelerate_check++;
				else if (owner.hasSkillEffect(L1SkillId.SLOW))
					accelerate_check--;
				if (owner.isBrave() || owner.isBlood_lust() || owner.isElfBraveMagicShort()
						|| owner.isElfBraveMagicLong() || owner.isElfBrave() || owner.isFastMovable()
						|| owner.hasSkillEffect(L1SkillId.STATUS_FRUIT))
					accelerate_check++;
				if (owner.isDragonPearl()) {
					dragon_pearl = true;
					accelerate_check++;
				}
				if (owner.isFourgear()) {
					accelerate_check++;
				}
			}

			switch (accelerate_check) {
				case -1:
					accelerate -= MOVE_HASTE_ACCELERATION;
					break;
				case 1:
					if (dragon_pearl)
						accelerate += MOVE_DRAGON_PEARL_ACCELERATION;
					else
						accelerate += MOVE_HASTE_ACCELERATION;
					break;
				case 2:
					if (dragon_pearl)
						accelerate += (MOVE_HASTE_ACCELERATION / 1.15D) + (MOVE_DRAGON_PEARL_ACCELERATION / 1.15D);
					else
						accelerate += MOVE_BRAVE_ACCELERATION + MOVE_HASTE_ACCELERATION;
					break;
				case 3:
					accelerate += MOVE_HASTE_ACCELERATION + MOVE_BRAVE_ACCELERATION + MOVE_THIRD_ACCELERATION;
					break;
				case 4:
					accelerate += MOVE_HASTE_ACCELERATION + MOVE_BRAVE_ACCELERATION + MOVE_THIRD_ACCELERATION
							+ MOVE_FOUR_GEAR_ACCELERATION;
					break;
				default:
					break;
			}

			complete_accelerate = interval - (interval * accelerate);

			if (owner.getMoveDelayRate() > 0) {
				complete_accelerate = complete_accelerate - (complete_accelerate * (owner.getMoveDelayRate() * 0.01));
				// complete_accelerate -= complete_accelerate * ((owner.getMoveDelayRate() /
				// 100));
			}

			if (Config.LogStatus.USEACTIONTIMELOGGING) {
				System.out.println("移動速度： " + complete_accelerate);
			}
			return complete_accelerate;
		}
	};

	public static final double ATT_HASTE_RETARDATION = 0.33D;
	public static final double ATT_BRAVE_RETARDATION = 0.44D;
	public static final double ATT_THIRD_RETARDATION = 0.13D;
	public static final double ATT_FOUR_GEAR_RETARDATION = 0.13D;
	public static final double ATT_HASTE_ACCELERATION = ATT_HASTE_RETARDATION / 2D;
	public static final double ATT_BRAVE_ACCELERATION = ATT_BRAVE_RETARDATION / 2D;
	public static final double ATT_THIRD_ACCELERATION = ATT_THIRD_RETARDATION / 2D;
	public static final double ATT_FOUR_GEAR_ACCELERATION = ATT_FOUR_GEAR_RETARDATION / 2D;

	public static final ActionIntervalDecorator<L1PcInstance> PC_ATT_DECORATOR = new ActionIntervalDecorator<L1PcInstance>() {
		@Override
		public double decoration(L1PcInstance owner, double interval) {
			int accelerate_check = 0;
			double accelerate = 0;
			double complete_accelerate = 0;
			boolean dragon_pearl = false;
			if (owner != null) {
				if (owner.getWeapon() != null
						&& (owner.getCurrentSpriteId() == 18551 || owner.getCurrentSpriteId() == 18555)) {
					switch (owner.getWeapon().getItem().getType()) {
						case 1: // 一隻手劍
							interval = 450.1607717041801;
							break;
						case 2: // 匕首
							interval = 411.3924050632911;
							break;
						case 3: // 雙手劍
							interval = 526.3157894736843;
							break;
						case 4: // 弓
							interval = 606.060606060606;
							break;
						case 5: // 槍
							interval = 487.01298701298697;
							break;
						case 6: // 斧頭
							interval = 526.3157894736843;
							break;
						case 7: // 魔杖
							interval = 707.395498392283;
							break;
						case 11: // 爪
							interval = 643.0868167202573;
							break;
						case 12: // 二刀流
							interval = 836.0128617363345;
							break;
						case 13: // 單手弓
							interval = 606.060606060606;
							break;
						case 14: // 單手槍
							interval = 487.01298701298697;
							break;
						case 15: // 雙手斧頭
							interval = 526.3157894736843;
							break;
						case 16: // 雙手魔杖
							interval = 707.395498392283;
							break;
						case 17: // 關鍵環節
							interval = 643.0868167202573;
							break;
						case 18: // 鏈鋸劍
							interval = 487.01298701298697;
							break;
						default:
							interval = 450.1607717041801;
							System.out.println("[類型錯誤]： " + owner.getWeapon().getItem().getType());
							break;
					}
				}

				if (owner.isHaste())
					accelerate_check += 1;
				else if (owner.hasSkillEffect(L1SkillId.SLOW))
					accelerate_check--;

				if (owner.hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
					if (owner.isPassive(MJPassiveID.DARK_HORSE.toInt())) {
						accelerate_check++;
					}
				} else if (owner.isBrave() || owner.isBlood_lust() || owner.isElfBraveMagicShort()
						|| owner.isElfBraveMagicLong())
					accelerate_check++;
				else if (owner.isElfBrave())
					accelerate_check++;
				else if (owner.hasSkillEffect(L1SkillId.MOVING_ACCELERATION)
						&& owner.isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt()))
					accelerate_check++;
				else if (owner.hasSkillEffect(L1SkillId.HOLY_WALK)
						&& owner.isPassive(MJPassiveID.HOLY_WALK_EVOLUTION.toInt()))
					accelerate_check++;

				if (owner.isDragonPearl()) {
					dragon_pearl = true;
					accelerate_check++;
				}

				if (owner.isFourgear()) {
					accelerate_check++;
				}

				if (owner.hasSkillEffect(L1SkillId.WIND_SHACKLE))
					accelerate_check--;
			}

			switch (accelerate_check) {
				case -1:
					accelerate -= ATT_HASTE_ACCELERATION;
					break;
				case 1:
					if (dragon_pearl)
						accelerate += ATT_THIRD_ACCELERATION;
					else
						accelerate += ATT_HASTE_ACCELERATION;
					break;
				case 2:
					if (dragon_pearl)
						accelerate += ATT_HASTE_ACCELERATION + ATT_THIRD_ACCELERATION;
					else
						accelerate += ATT_HASTE_ACCELERATION + ATT_BRAVE_ACCELERATION;
					break;
				case 3:
					accelerate += ATT_HASTE_ACCELERATION + ATT_BRAVE_ACCELERATION + ATT_THIRD_ACCELERATION;
					break;
				case 4:
					accelerate += ATT_HASTE_ACCELERATION + ATT_BRAVE_ACCELERATION + ATT_THIRD_ACCELERATION
							+ ATT_FOUR_GEAR_ACCELERATION;
					break;
				default:
					break;
			}

			complete_accelerate = interval - (interval * accelerate);

			if (owner.getAttackDelayRate() > 0) {
				complete_accelerate = complete_accelerate - (complete_accelerate * (owner.getAttackDelayRate() * 0.01));
			}

			if (Config.LogStatus.USEACTIONTIMELOGGING) {
				System.out.println("攻擊速度： " + complete_accelerate);
			}

			owner.setAttackDelayChecker(interval);

			return complete_accelerate;
		}
	};

	/**
	 * SP_HASTE_RETARDATION = 第一次加速
	 * SP_BRAVE_RETARDATION = 第二次加速
	 * SP_THIRD_RETARDATION = 第三速度加速度
	 * SP_DRAGON_PEARL_RETARDATION = 當只吃珍珠時
	 ***/
	public static double SP_HASTE_RETARDATION = 0.085D;
	public static double SP_BRAVE_RETARDATION = 0.085D;
	public static double SP_THIRD_RETARDATION = 0.105D;
	public static double SP_DRAGON_PEARL_RETARDATION = 0.065D;
	public static double SP_FOUR_GEAR_RETARDATION = 0.065D;

	public static double SP_HASTE_ACCELERATION = SP_HASTE_RETARDATION / 2D;
	public static double SP_BRAVE_ACCELERATION = SP_BRAVE_RETARDATION / 2D;
	public static double SP_THIRD_ACCELERATION = SP_THIRD_RETARDATION / 2D;
	public static double SP_DRAGON_PEARL_ACCELERATION = SP_DRAGON_PEARL_RETARDATION / 2D;
	public static double SP_FOUR_GEAR_ACCELERATION = SP_FOUR_GEAR_RETARDATION / 2D;

	public static final ActionIntervalDecorator<L1PcInstance> PC_MAGIC_DECORATOR = new ActionIntervalDecorator<L1PcInstance>() {
		@Override
		public double decoration(L1PcInstance owner, double interval) {
			int accelerate_check = 0;
			double accelerate = 0;
			double complete_accelerate = 0;
			boolean dragon_pearl = false;
			if (owner != null) {
				// 透過 TODO 類別減少魔法延遲
				switch (owner.getClassId()) {
					case 0: // 王族
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 1: // 騎士
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 2: // 妖精
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 3: // 法師
						SP_HASTE_RETARDATION = 0.085D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 4: // 黑暗妖精
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 5: // 龍騎士
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 6: // 幻術師
						SP_HASTE_RETARDATION = 0.085D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 7: // 戰士
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 8: // 劍士
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
					case 9: // 黃金槍騎
						SP_HASTE_RETARDATION = 0.095D;
						SP_BRAVE_RETARDATION = 0.085D;
						SP_THIRD_RETARDATION = 0.105D;
						SP_DRAGON_PEARL_RETARDATION = 0.065D;
						break;
				}

				if (owner.isHaste())
					accelerate_check++;
				else if (owner.hasSkillEffect(L1SkillId.SLOW))
					accelerate_check--;

				if (owner.hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
					if (owner.isPassive(MJPassiveID.DARK_HORSE.toInt()))
						accelerate_check++;
				} else if (owner.isBrave() || owner.isBlood_lust() || owner.isElfBraveMagicShort()
						|| owner.isElfBraveMagicLong() || owner.isElfBrave())
					accelerate_check++;

				if (owner.isDragonPearl()) {
					dragon_pearl = true;
					accelerate_check++;
				}

				if (owner.isFourgear()) {
					accelerate_check++;
				}

				if (owner.hasSkillEffect(L1SkillId.WIND_SHACKLE))
					accelerate_check--;
			}

			switch (accelerate_check) {
				case -1:
					accelerate -= SP_HASTE_ACCELERATION;
					break;
				case 1:
					if (dragon_pearl)
						accelerate += SP_DRAGON_PEARL_ACCELERATION;
					else
						accelerate += SP_HASTE_ACCELERATION;
					break;
				case 2:
					if (dragon_pearl)
						accelerate += (SP_HASTE_ACCELERATION / 1.072) + (SP_DRAGON_PEARL_ACCELERATION / 1.072);
					else
						accelerate += SP_BRAVE_ACCELERATION + SP_HASTE_ACCELERATION;
					break;
				case 3:
					accelerate += SP_HASTE_ACCELERATION + SP_BRAVE_ACCELERATION + SP_THIRD_ACCELERATION;
					break;
				case 4:
					accelerate += SP_HASTE_ACCELERATION + SP_BRAVE_ACCELERATION + SP_THIRD_ACCELERATION
							+ SP_FOUR_GEAR_ACCELERATION;
					break;
				default:
					break;
			}

			complete_accelerate = interval - (interval * accelerate);

			if (owner != null) {
				if (owner.getMagicDelayRate() > 0) {
					complete_accelerate -= complete_accelerate * ((owner.getMagicDelayRate() / 100));
				}
			}

			if (Config.LogStatus.USEACTIONTIMELOGGING) {
				System.out.println("魔法速度： " + complete_accelerate);
			}
			// System.out.println("魔法速度： " + complete_accelerate);

			return complete_accelerate;
		}
	};

	public static final double CHA_HASTE_RETARDATION = 1.33D;
	public static final double CHA_BRAVE_RETARDATION = 1.33D;
	public static final double CHA_THIRD_RETARDATION = 1.13D;
	public static final double CHA_HASTE_ACCELERATION = 1D / CHA_HASTE_RETARDATION;
	public static final double CHA_BRAVE_ACCELERATION = 1D / CHA_BRAVE_RETARDATION;
	public static final double CHA_THIRD_ACCELERATION = 1D / CHA_THIRD_RETARDATION;

	public static final ActionIntervalDecorator<L1Character> CHA_WALK_DECORATOR = new ActionIntervalDecorator<L1Character>() {
		@Override
		public double decoration(L1Character owner, double interval) {
			if (owner instanceof MJCompanionInstance) {
				interval -= (interval * ((MJCompanionInstance) owner).get_movedelay_reduce() * 0.01);
			} else {
				if (owner.isHaste())
					interval *= CHA_HASTE_ACCELERATION;
				else if (owner.hasSkillEffect(L1SkillId.SLOW))
					interval *= CHA_HASTE_RETARDATION;
				if (owner.getBraveSpeed() == 1)
					interval *= CHA_BRAVE_ACCELERATION;
				if (owner.getMoveDelayRate() != 0) {
					interval -= interval * (owner.getMoveDelayRate() * 0.01);
					return (int) interval;
				}
			}
			return interval;
		}
	};

	public static final ActionIntervalDecorator<L1Character> CHA_ATT_DECORATOR = new ActionIntervalDecorator<L1Character>() {
		@Override
		public double decoration(L1Character owner, double interval) {
			if (owner instanceof MJCompanionInstance) {
				interval -= (interval * ((MJCompanionInstance) owner).get_attackdelay_reduce() * 0.01);
			} else {
				if (owner.isHaste())
					interval *= CHA_HASTE_ACCELERATION;
				else if (owner.hasSkillEffect(L1SkillId.SLOW))
					interval *= CHA_HASTE_RETARDATION;
				if (owner.getBraveSpeed() == 1)
					interval *= CHA_BRAVE_ACCELERATION;
				if (owner.hasSkillEffect(L1SkillId.WIND_SHACKLE))
					interval *= CHA_HASTE_RETARDATION;
				if (owner.getAttackDelayRate() != 0) {
					interval -= interval * (owner.getAttackDelayRate() * 0.01);
					return (int) interval;
				}
			}
			return interval;
		}
	};

	public static final ActionIntervalDecorator<L1Character> NULL_DECORATOR = new ActionIntervalDecorator<L1Character>() {
		@Override
		public double decoration(L1Character owner, double interval) {
			return interval;
		}
	};
}

package l1j.server.server.utils;

import java.util.ArrayList;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.MJCaptchaSystem.MJCaptcha;
import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJCompanion.MJCompanionSettings;
import l1j.server.MJCompanion.Basic.HuntingGround.MJCompanionHuntingGround;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.Account;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.BonusExpTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.PartyMapInfoTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1BonusExp;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1SpecialMap;

public class CalcExp {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(CalcExp.class.getName());

	private CalcExp() {
	}

	public static void calcExp(L1PcInstance l1pcinstance, int targetid, ArrayList<?> acquisitorList, ArrayList<?> hateList, long exp) {
		if (l1pcinstance == null) {
			return;
		}

		int i = 0;
		double party_level = 0;
		double dist = 0;
		long member_exp = 0;
		int member_lawful = 0;
		L1Object l1object = L1World.getInstance().findObject(targetid);
		L1NpcInstance npc = (L1NpcInstance) l1object;

		if (l1pcinstance.getLevel() < 55) {
			int add_lawful = (int) (npc.getLawful() * Config.ServerRates.RateLawful) * -1;// -1
			l1pcinstance.addLawful(add_lawful);
			double exppenalty = ExpTable.getPenaltyRate(l1pcinstance.getLevel());
			long add_exp = (long) (exp * exppenalty * Config.ServerRates.RateXpClaudia);
			if (l1pcinstance.getLevel() < 5)
				add_exp *= 5;

			if (BonusExpTable.getInstance().isExpBonusLv(l1pcinstance.getLevel())) {
				L1BonusExp temp = BonusExpTable.getInstance().getExpBonusLv(l1pcinstance.getLevel());
				if (temp != null) {
					add_exp *= temp.getExpBonus();
				}
			}

			l1pcinstance.add_exp(add_exp);
			return;
		}

		// 獲取仇恨值的總和
		L1Character acquisitor;
		int hate = 0;
		long acquire_exp = 0;
		int acquire_lawful = 0;
		long party_exp = 0;
		int party_lawful = 0;
		long totalHateExp = 0;
		int totalHateLawful = 0;
		long partyHateExp = 0;
		int partyHateLawful = 0;
		long ownHateExp = 0;

		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		for (i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = (L1Character) acquisitorList.get(i);
			if (acquisitor instanceof MJCompanionInstance)
				acquisitor = ((MJCompanionInstance) acquisitor).get_master();
			hate = (Integer) hateList.get(i);
			if (acquisitor != null && !acquisitor.isDead()) {
				totalHateExp += hate;
				if (acquisitor instanceof L1PcInstance) {
					totalHateLawful += hate;
				}
			} else { // 如果為 null 或已死亡則排除
				acquisitorList.remove(i);
				hateList.remove(i);
			}
		}
		if (totalHateExp == 0) { // 沒有獲取者的情況
			return;
		}

		if (l1object != null && !(npc instanceof L1PetInstance) && !(npc instanceof L1SummonInstance)) {
			// int exp = npc.get_exp();
			if (!L1World.getInstance().isProcessingContributionTotal() && l1pcinstance.getHomeTownId() > 0) {
				int contribution = npc.getLevel() / 10;
				l1pcinstance.addContribution(contribution);
			}
			int lawful = npc.getLawful();

			if (l1pcinstance.isInParty() && getPartyIsinScreen(l1pcinstance) > 1) { // 在派對中
				partyHateExp = 0;
				partyHateLawful = 0;
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = (L1Character) acquisitorList.get(i);
					if (acquisitor instanceof MJCompanionInstance)
						acquisitor = ((MJCompanionInstance) acquisitor).get_master();

					hate = (Integer) hateList.get(i);
					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						if (pc == l1pcinstance) {
							partyHateExp += hate;
							partyHateLawful += hate;
						} else if (l1pcinstance.getParty().isMember(pc)) {
							partyHateExp += hate;
							partyHateLawful += hate;
						} else {
							if (totalHateExp > 0) {
								acquire_exp = (exp * hate / totalHateExp);
							}
							if (totalHateLawful > 0) {
								acquire_lawful = (lawful * hate / totalHateLawful);
							}
							AddExp(pc, acquire_exp, acquire_lawful);
						}
					} else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) pet.getMaster();
						if (master == l1pcinstance) {
							partyHateExp += hate;
						} else if (l1pcinstance.getParty().isMember(master)) {
							partyHateExp += hate;
						} else {
							if (totalHateExp > 0) {
								acquire_exp = (exp * hate / totalHateExp);
							}
							AddExpPet(pet, acquire_exp);
						}
					} else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) summon.getMaster();
						if (master == l1pcinstance) {
							partyHateExp += hate;
						} else if (l1pcinstance.getParty().isMember(master)) {
							partyHateExp += hate;
						} else {
						}
					}

				}
				if (totalHateExp > 0) {
					party_exp = (exp * partyHateExp / totalHateExp);
				}
				if (totalHateLawful > 0) {
					party_lawful = (lawful * partyHateLawful / totalHateLawful);
				}

				// 分配經驗值和戰利品
				double pri_bonus = 0;
				L1PcInstance leader = l1pcinstance.getParty().getLeader();
				if (leader.isCrown() && (l1pcinstance.knownsObject(leader) || l1pcinstance.equals(leader))) {
					pri_bonus = 0.059;
				}

				// 計算隊伍經驗值
				L1PcInstance[] ptMembers = l1pcinstance.getParty().getMembers();
				double pt_bonus = 0;
				for (L1PcInstance each : l1pcinstance.getParty().getMembers()) {
					if (l1pcinstance.knownsObject(each) || l1pcinstance.equals(each)) {
						party_level += each.getLevel() * each.getLevel();
					}
					if (l1pcinstance.knownsObject(each)) {
						pt_bonus += 0.04;
					}
				}

				party_exp = (long) (party_exp * (0.5 + pt_bonus + pri_bonus));
				if (Config.ServerAdSetting.IsPartyExp) {
					party_exp *= (long) Config.ServerAdSetting.AddPartyExp;
				}

				// 計算角色及其寵物/召喚物的仇恨值總和
				if (party_level > 0) {
					dist = ((l1pcinstance.getLevel() * l1pcinstance.getLevel()) / party_level);
				}
				member_exp = (long) (party_exp * dist);
				member_lawful = (int) (party_lawful * dist);

				ownHateExp = 0;
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = (L1Character) acquisitorList.get(i);
					if (acquisitor instanceof MJCompanionInstance)
						acquisitor = ((MJCompanionInstance) acquisitor).get_master();
					hate = (Integer) hateList.get(i);
					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						if (pc == l1pcinstance) {
							ownHateExp += hate;
						}
					} else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) pet.getMaster();
						if (master == l1pcinstance) {
							ownHateExp += hate;
						}
					} else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) acquisitor;
						L1PcInstance master = (L1PcInstance) summon.getMaster();
						if (master == l1pcinstance) {
							ownHateExp += hate;
						}
					}
				}
				// 分配給角色及其寵物/召喚物
				if (ownHateExp != 0) { // 參與了攻擊
					for (i = hateList.size() - 1; i >= 0; i--) {
						acquisitor = (L1Character) acquisitorList.get(i);
						if (acquisitor instanceof MJCompanionInstance)
							acquisitor = ((MJCompanionInstance) acquisitor).get_master();
						hate = (Integer) hateList.get(i);
						if (acquisitor instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) acquisitor;
							if (pc == l1pcinstance) {
								if (ownHateExp > 0) {
									acquire_exp = (member_exp * hate / ownHateExp);
								}
								AddExp(pc, acquire_exp, member_lawful);
							}
						} else if (acquisitor instanceof L1PetInstance) {
							L1PetInstance pet = (L1PetInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) pet.getMaster();
							if (master == l1pcinstance) {
								if (ownHateExp > 0) {
									acquire_exp = (member_exp * hate / ownHateExp);
								}
								AddExpPet(pet, acquire_exp);
							}
						} else if (acquisitor instanceof L1SummonInstance) {
							L1SummonInstance sum = (L1SummonInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) sum.getMaster();
							if (master == l1pcinstance) {
								if (ownHateExp > 0) {
									acquire_exp = (member_exp * hate / ownHateExp);
								}
								AddExp(master, acquire_exp, member_lawful);
							}
						}
					}
				} else { // 沒有參與攻擊
					// 只分配給角色
					AddExp(l1pcinstance, member_exp, member_lawful);
				}

				// 計算隊伍成員及其寵物/召喚物的仇恨值總和
				for (int cnt = 0; cnt < ptMembers.length; cnt++) {
					if (l1pcinstance.knownsObject(ptMembers[cnt])) {
						if (party_level > 0) {
							dist = ((ptMembers[cnt].getLevel() * ptMembers[cnt].getLevel()) / party_level);
						}
						member_exp = (long) (party_exp * dist);
						member_lawful = (int) (party_lawful * dist);

						ownHateExp = 0;
						for (i = hateList.size() - 1; i >= 0; i--) {
							acquisitor = (L1Character) acquisitorList.get(i);
							if (acquisitor instanceof MJCompanionInstance)
								acquisitor = ((MJCompanionInstance) acquisitor).get_master();
							hate = (Integer) hateList.get(i);
							if (acquisitor instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) acquisitor;
								if (pc == ptMembers[cnt]) {
									ownHateExp += hate;
								}
							} else if (acquisitor instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) pet.getMaster();
								if (master == ptMembers[cnt]) {
									ownHateExp += hate;
								}
							} else if (acquisitor instanceof L1SummonInstance) {
								L1SummonInstance summon = (L1SummonInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) summon.getMaster();
								if (master == ptMembers[cnt]) {
									ownHateExp += hate;
								}
							}
						}
						// 分配給隊伍成員及其寵物/召喚物
						if (ownHateExp != 0) { // 參與了攻擊
							for (i = hateList.size() - 1; i >= 0; i--) {
								acquisitor = (L1Character) acquisitorList.get(i);
								if (acquisitor instanceof MJCompanionInstance)
									acquisitor = ((MJCompanionInstance) acquisitor).get_master();
								hate = (Integer) hateList.get(i);
								if (acquisitor instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) acquisitor;
									if (pc == ptMembers[cnt]) {
										if (ownHateExp > 0) {
											acquire_exp = (member_exp * hate / ownHateExp);
										}
										AddExp(pc, acquire_exp, member_lawful);
									}
								} else if (acquisitor instanceof L1PetInstance) {
									L1PetInstance pet = (L1PetInstance) acquisitor;
									L1PcInstance master = (L1PcInstance) pet.getMaster();
									if (master == ptMembers[cnt]) {
										if (ownHateExp > 0) {
											acquire_exp = (member_exp * hate / ownHateExp);
										}
										AddExpPet(pet, acquire_exp);
									}
								} else if (acquisitor instanceof L1SummonInstance) {
									L1SummonInstance sum = (L1SummonInstance) acquisitor;
									L1PcInstance pc = (L1PcInstance) sum.getMaster();
									AddExp(pc, acquire_exp, acquire_lawful);
								}
							}
						} else { // 沒有參與攻擊
							// 只分配給隊伍成員
							AddExp(ptMembers[cnt], member_exp, member_lawful);
						}
					}
				}
			} else { // 沒有組隊
				// 分配經驗值和戰利品
				for (i = hateList.size() - 1; i >= 0; i--) {
					acquisitor = (L1Character) acquisitorList.get(i);
					if (acquisitor instanceof MJCompanionInstance)
						acquisitor = ((MJCompanionInstance) acquisitor).get_master();
					hate = (Integer) hateList.get(i);
					acquire_exp = (exp * hate / totalHateExp);
					if (acquisitor instanceof L1PcInstance) {
						if (totalHateLawful > 0) {
							acquire_lawful = (lawful * hate / totalHateLawful);
						}
					}

					if (acquisitor instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) acquisitor;
						AddExp(pc, acquire_exp, acquire_lawful);
					} else if (acquisitor instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) acquisitor;
						AddExpPet(pet, acquire_exp);
					} else if (acquisitor instanceof L1SummonInstance) {
						L1SummonInstance sum = (L1SummonInstance) acquisitor;
						L1PcInstance pc = (L1PcInstance) sum.getMaster();
						AddExp(pc, acquire_exp, acquire_lawful);
					}
				}
			}
		}
	}

	private static void AddExp(L1PcInstance pc, long exp, int lawful) {
		if (Config.Login.StandbyServer)
			return;

		if (pc.isGhost()) {
			return;
		}

		int add_lawful = (int) (lawful * Config.ServerRates.RateLawful) * -1;
		pc.addLawful(add_lawful);
		MJCompanionHuntingGround hground = MJCompanionHuntingGround.get_hunting_ground(pc.getMapId());
		if (hground != null) {
			double magnification_exp = (double) exp * hground.get_magnification_by_exp();
			exp -= magnification_exp;
			MJCompanionInstance companion = pc.get_companion();
			if (companion != null) {
				companion.update_exp((long) magnification_exp);
			}
			if (exp <= 0) {
				return;
			}
		}

		/** 機器人系統 **/
		if (pc.getAI() != null) {
			return;
		}

		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		// System.out.println(String.format(" penalty : %.8f", exppenalty));
		double expposion = 1; // 成長藥水及PC房
		double EXP_TAM = 1;
		double levelupBonus = 1;
		double clanBonus = 1;
		double comboBonus = 1; // 連擊系統
		double Arka = 1; // 阿爾卡的遺物
		double special_map = 1;
		double party_map_rate = 1;
		double exp_posion = 1;

		if (SpecialMapTable.getInstance().isSpecialMap(pc.getMapId())) {
			L1SpecialMap SM = SpecialMapTable.getInstance().getSpecialMap(pc.getMapId());
			if (SM != null) {
				special_map += SM.getExpRate();
			}
		}

		if (pc.isInParty()) {
			Double pmr = PartyMapInfoTable.getInstance().getPartyMapExpRate(pc.getMapId());
			if (pmr != 0) {
				int member_count = 0;
				for (L1PcInstance member : pc.getParty().getList()) {
					if (pc.getId() == member.getId()) {
						continue;
					}
					if (pc.getLocation().isInScreen(member.getLocation())) {
						member_count++;
					}
				}

				if (member_count != 0) {
					party_map_rate += pmr;
				}
			}
		}

		if (pc.hasSkillEffect(L1SkillId.EXP_POTION)) {
//			expposion += pc.hasSkillEffect(L1SkillId.PC_CAFE) ? 0.4D : 0.2D;
			expposion += pc.isPcBuff() ? 0.4D : 0.2D;
		}
		if (pc.hasSkillEffect(L1SkillId.EXP_POTION_Event)) {
//			expposion += pc.hasSkillEffect(L1SkillId.PC_CAFE) ? 0.9D : 0.7D;
			expposion += pc.isPcBuff() ? 0.9D : 0.7D;
		}
		if (pc.hasSkillEffect(L1SkillId.Tam_Fruit5)) {
			EXP_TAM += 0.05;
		}

		if (pc.hasSkillEffect(L1SkillId.EXP_BUFF) && pc.getAccount().getBlessOfAin() >= 10000) {
			int lvl = pc.getLevel();
			if (lvl <= 79)
				exp_posion += 1.30D;
			else if (lvl <= 81)
				exp_posion += 1.20D;
			else if (lvl <= 83)
				exp_posion += 1.10D;
			else if (lvl <= 85)
				exp_posion += 1.00D;
			else if (lvl <= 89)
				exp_posion += 0.90D;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

		if (pc.getInventory().checkEquipped(900033) || pc.getInventory().checkEquipped(9961)
				|| pc.getInventory().checkEquipped(900116)) {
			Arka += 0.2;// 2%
		}

		if (pc.hasSkillEffect(L1SkillId.LevelUpBonus))
			levelupBonus += 1.23;

		try {
			if (pc.getAccount().getBlessOfAin() > 0) {
				if (pc.hasSkillEffect(L1SkillId.COMBO_BUFF)) {
					if (pc.getComboCount() <= 11) {
						comboBonus += (pc.getComboCount() * 0.1);
					}
				}
			}

			double einhasadBonus = 1;
			if (pc.getAccount().getBlessOfAin() > 0) {
				int ein_level = pc.getAccount().get_ein_level();
				if (pc.getClan() != null)
					clan.addBlessCount((int) exp);
				einhasadBonus += SC_REST_EXP_INFO_NOTI.expRation(ein_level);
				if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ) || pc.hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
					einhasadBonus += SC_REST_EXP_INFO_NOTI.expExtra(pc, ein_level);
				}
			} else {
				einhasadBonus += SC_REST_EXP_INFO_NOTI.expExtra(pc, pc.getAccount().get_ein_level());
			}

			if (pc.getExpAmplifier() != null)
				einhasadBonus += pc.getExpAmplifier().getMagnifier();

			double emeraldBonus = 1;

			if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) && pc.getAccount().getBlessOfAin() > 10000) {
				emeraldBonus += 0.80;
				if (pc.getClan() != null)
					clan.addBlessCount((int) exp);

			} else if (pc.hasSkillEffect(L1SkillId.DRAGON_PUPLE) && pc.getAccount().getBlessOfAin() > 10000) {
				if (pc.getLevel() >= 49 && pc.getLevel() <= 54)
					einhasadBonus += 0.53;
				else if (pc.getLevel() >= 55 && pc.getLevel() <= 59)
					einhasadBonus += 0.43;
				else if (pc.getLevel() >= 60 && pc.getLevel() <= 64)
					einhasadBonus += 0.33;
				else if (pc.getLevel() >= 65)
					einhasadBonus += 0.23;
				if (pc.getClan() != null)
					clan.addBlessCount((int) exp);
				if (pc.getAccount().getBlessOfAin() <= 10000) {
					pc.removeSkillEffect(L1SkillId.DRAGON_PUPLE);
				}
			} else if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ) && pc.getAccount().getBlessOfAin() > 10000) {
				einhasadBonus += 1.5;// 重製 0.8
				if (pc.getClan() != null)
					clan.addBlessCount((int) exp);
				if (pc.hasSkillEffect(L1SkillId.COMBO_BUFF)) {
					if (pc.getComboCount() <= 11) {
						comboBonus += (pc.getComboCount() * 0.1);
					}
				}
				if (pc.getAccount().getBlessOfAin() <= 10000) {
					pc.removeSkillEffect(L1SkillId.DRAGON_TOPAZ);
				}
			}

			if (pc.getAccount().getBlessOfAin() > 10000 || einhasadBonus != 0) {
				pc.getAccount().addBlessOfAin(-(int) SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp), pc);
				SC_REST_EXP_INFO_NOTI.send(pc);
			}

			double clanOnlineBonus = 1;

			/** 血盟Buff 額外經驗值 20% **/
			if (pc.getClanid() != 0) {
				if (pc.getClan().getOnlineClanMember().length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT) {
					clanOnlineBonus += 0.10;
				}
			}

			/** 城血額外經驗值給予 **/
			double BloodBonus = 1;
			if (clan != null && clan.getCastleId() != 0) {
				BloodBonus += Config.ServerRates.BloodBonus;
			}

			/** 特定地圖從該等級開始不再獲得經驗值 **/
			if ((pc.getLevel() >= Config.ServerAdSetting.NewCha1) && (pc.getMapId() >= 1911 && pc.getMapId() <= 1912)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "在該狩獵場不再能獲得經驗值。"));
				return;
			}
			if ((pc.getLevel() >= 75) && (pc.getMapId() >= 25 && pc.getMapId() <= 28)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "在該狩獵場無法再獲得經驗值。"));
				return;
			}

			int rank_level = pc.getRankLevel();
			double rank_rate = 1;
			if (rank_level >= 1 && rank_level <= 5) {
				if (pc.getAccount().getBlessOfAin() > 2000000) {
					rank_rate += 0.10;
				}
			}

			/**
			 * 物品經驗值獎勵
			 */
			double item_bonus_exp = 1 + (pc.get_item_exp_bonus() * 0.01);
			double bonus_exp = 0;
			if (pc.getLevel() <= Config.ServerAdSetting.LineageBuff){
				bonus_exp += Config.ServerAdSetting.LineageBuffExpRation;
			}
			if (pc.hasSkillEffect(L1SkillId.PC_EXP_UP)) {
				bonus_exp += 10;
			}
			if (bonus_exp != 0) {
				item_bonus_exp += (double) (bonus_exp / 100);
			}

			long previousExp = (long) exp;
			long add_exp = (long) (exp * exppenalty * Config.ServerRates.RateXp * levelupBonus
					* clanBonus * expposion * EXP_TAM * comboBonus * einhasadBonus
					* emeraldBonus * clanOnlineBonus * BloodBonus * Arka * special_map
					* party_map_rate * exp_posion * rank_rate * item_bonus_exp);
			if (add_exp < 0) {
				System.out.println("[經驗值負數處理確認1] : " + pc.getName() + " / " + add_exp + "(進入 : " + exp + ", " + previousExp + ")");
				return;
			}

			// 防止暴力升級
			if (pc.getLevel() >= 1) {
				if ((add_exp + pc.get_exp()) > ExpTable.getExpByLevel((pc.getLevel() + 1))) {
					add_exp = (ExpTable.getExpByLevel((pc.getLevel() + 1)) - pc.get_exp());
				}
			}

			if (pc.getLevel() >= Config.CharSettings.MaxLevel /*&& !pc.isGm()*/) {
				add_exp = 0;
				pc.sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
			}

			if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
				add_exp = 0;
				pc.sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
			}

			// 等級限制
			if (pc.getLevel() >= GameServerSetting.getInstance().get_maxLevel()) {
				// 到達下一等級所需的經驗值
				long maxexp = ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1);
				if (pc.get_exp() + add_exp >= maxexp) {
					// return;
					add_exp = 0;
				}
			}

			if (add_exp < 0) {
				System.out.println("[經驗值負數處理確認2] : " + pc.getName() + " / " + add_exp + "(進入 : " + exp + ")");
				return;
			}
			// TODO 自動防止代碼在該地圖中忽略
			if (!pc.isGm() && pc.getAI() == null && !pc.getSafetyZone() && !pc.get_is_client_auto()
					&& !(pc.getMapId() == 1936 || pc.getMapId() == 107 || pc.getMapId() == 2101 || pc.getMapId() == 2151
					|| pc.getMapId() == 612 || pc.getMapId() == 254 || pc.getMapId() == 1930)
					&& !pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
				MJCaptcha captcha = pc.get_captcha();
				if (captcha == null)
					captcha = pc.create_captcha();

				if (captcha.is_keep_captcha()) {
					if (captcha.inc_relay_count() >= MJCaptchaLoadManager.CAPTCHA_RELAY_COUNT)
						captcha.do_fail(pc);
				} else {
					if (MJCaptchaLoadManager.CAPTCHA_IS_RUNNING) {
						if (!captcha.is_pass_captcha() && MJRnd.isWinning(1000000, MJCaptchaLoadManager.CAPTCHA_SHOW_PROBABILITY_BYMILLIMON))
							captcha.drain_captcha(pc);
					}
				}
			}

			if (pc.get_companion() != null) {
				pc.get_companion().update_exp((int) (add_exp * MJCompanionSettings.EXP_BY_MASTER_EXP));
			}
			Account account = pc.getAccount();
			if (account != null && FatigueProperty.getInstance().use_fatigue() && pc.getAI() == null) {
				if (account.has_fatigue()) {
					add_exp -= (add_exp * FatigueProperty.getInstance().get_fatigue_effect_exp());
				}
			}
//			 System.out.println(String.format("up : %d, source : %d, only penalty : %.8f", add_exp, exp, exp * exppenalty));

			if (BonusExpTable.getInstance().isExpBonusLv(pc.getLevel())) {
				L1BonusExp temp = BonusExpTable.getInstance().getExpBonusLv(pc.getLevel());
				if (temp != null) {
					add_exp *= temp.getExpBonus();
				}
			}

			if (pc != null && pc.isPassive(MJPassiveID.GROWS.toInt())) {
				int Level = pc.getLevel();
				int point = MJRnd.next(4) + 2;
				int probability = Config.MagicAdSetting_Fencer.GROWS_PASSIVE;

				if (Level < 85) {
					if (MJRnd.isWinning(100, probability)) {
						add_exp *= point;
						if (point == 2) {
							pc.send_effect(18572);
						} else if (point == 3) {
							pc.send_effect(18574);
						} else if (point >= 4) {
							pc.send_effect(18576);
						}
					}
				}
			}

			pc.add_exp(add_exp);
//			pc.add_exp_count(1);
		} catch (Exception e) {
			if (!(e instanceof NullPointerException))
				e.printStackTrace();
		}
	}

	private static void AddExpPet(L1PetInstance pet, long exp) {
		L1PcInstance pc = (L1PcInstance) pet.getMaster();

		// int petNpcId = pet.getNpcTemplate().get_npcId();
		int petItemObjId = pet.getItemObjId();

		int levelBefore = pet.getLevel();
		long totalExp = (long) (exp * 50 + pet.get_exp());
		if (totalExp >= ExpTable.getExpByLevel(51)) {
			totalExp = ExpTable.getExpByLevel(51) - 1;
		}
		pet.set_exp(totalExp);
		pet.setLevel(ExpTable.getLevelByExp(totalExp));

		int expPercentage = ExpTable.getExpPercentage(pet.getLevel(), totalExp);

		int gap = pet.getLevel() - levelBefore;
		for (int i = 1; i <= gap; i++) {
			IntRange hpUpRange = pet.getPetType().getHpUpRange();
			IntRange mpUpRange = pet.getPetType().getMpUpRange();
			pet.addMaxHp(hpUpRange.randomValue());
			pet.addMaxMp(mpUpRange.randomValue());
		}

		pet.setExpPercent(expPercentage);
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(pet);
		pc.broadcastPacket(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);

		// pc.sendPackets(new S_PetPack(pet, pc));

		if (gap != 0) { // 等級提升後記錄到資料庫
			// pc.sendPackets(new S_SkillSound(pet.getId(), 6353));
			// Broadcaster.broadcastPacket(pc, new S_SkillSound(pet.getId(), 6353));
			L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
			if (petTemplate == null) { // PetTable中不存在
				_log.warning("L1Pet == null");
				return;
			}
			petTemplate.set_exp(pet.get_exp());
			petTemplate.set_level(pet.getLevel());
			petTemplate.set_hp(pet.getMaxHp());
			petTemplate.set_mp(pet.getMaxMp());
			PetTable.getInstance().storePet(petTemplate); // 記錄到資料庫
			pc.sendPackets(new S_ServerMessage(320, pet.getName())); // 1%0的
		}
	}

	/**
	 * 給克勞迪亞發放經驗值
	 *
	 * @param pc 玩家角色
	 * @param exp 經驗值
	 */
	public static void AddExp(L1PcInstance pc, long exp) {
		/** 伺服器開放等待中 */
		if (Config.Login.StandbyServer) {
			return;
		}

		if (pc.getLevel() > GameServerSetting.getInstance().get_maxLevel()) {
			return;
		}
		if (pc.isDead())
			return;

		long add_exp = exp; // 排除因倍率提升的經驗值

		if (add_exp < 0) {
			return;
		}

		if (ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1) <= pc.get_exp() + add_exp) {
			pc.set_exp(ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1) - 1);
		} else {
			pc.add_exp(add_exp);
		}
		pc.onChangeExp();// 強制更新經驗值
	}

	private static int getPartyIsinScreen(L1PcInstance pc) {
		int count = 0;

		for (L1PcInstance member : pc.getParty().getMembers()) {
			if (pc.getLocation().isInScreen(member.getLocation())) {
				count++;
			}
		}

		return count;
	}

}
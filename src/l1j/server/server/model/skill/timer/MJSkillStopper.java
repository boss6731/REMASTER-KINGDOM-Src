package l1j.server.server.model.skill.timer;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.ClanBuffList.ClanBuffListLoader;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_INSTANCE_HP_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.noti.MJNotiSkillModel;
import l1j.server.server.model.skill.noti.MJNotiSkillService;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_IvenBuffIcon;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_NewSkillIcon;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.templates.L1Skills;

class MJSkillStopper {
	static void stopSkill(L1Character cha, int skillId) {
		L1Skills _skill = SkillsTable.getInstance().getTemplate(skillId);
		if (cha instanceof MJCompanionInstance) {
			if (((MJCompanionInstance) cha).on_buff_stopped(skillId))
				return;
		}
		switch (skillId) {
/*		case MANADECREASEPOTION:{
			if (cha instanceof L1PcInstance){
				L1PcInstance pc = (L1PcInstance) cha;
				pc.
			}
		}*/
		case BEHEMOTH:{
			cha.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(cha, 21964, false));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance)cha;
				pc.addMoveDelayRate(50);
				SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(pc, 21964, false);
			}
		}
		break;
		case STR_ADEN_SCROLL_BUFF:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr(-1);
				pc.addDmgup(-3);
				pc.addHitup(-5);
			}
		}
		break;
		case DEX_ADEN_SCROLL_BUFF:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedDex(-1);
				pc.addBowDmgup(-3);
				pc.addBowHitup(-5);
			}
		}
		break;		
		case INT_ADEN_SCROLL_BUFF:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedInt(-1);
				pc.getAbility().addSp(-1);
				pc.addBaseMagicHitUp(-3);
			}
		}
		break;	
		
		
		case BRAVE_UNION:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
		}
		break;
		case TYRANT_EXCUTION:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance)cha;
				pc.addAttackDelayRate(50);
				pc.set_Tyrant_Excute(false);
			}
			
		}
			break;
		case TARAS_ATTACK_SPEED:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc =(L1PcInstance) cha;
				pc.addAttackDelayRate(-10);
			}
		}
			break;
		case TARAS_MOVE_SPEED:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMoveDelayRate(-10);
			}
		}
			break;
		case PC_EXP_UP:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance)cha;
				L1SkillUse.off_icons(pc, skillId);
			}
		}
		break;
		case ENSNARE:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMoveDelayRate(50);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		}
		case HALPAS: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		}
		case SHOCK_ATTACK: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
				pc.addSpecialResistance(eKind.ABILITY, 10);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			cha.addMoveDelayRate(50);
			break;
		}
		case DISINTEGRATE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
				L1SkillId.onFreezeAfterDelay(pc);
				L1SkillUse.off_icons(pc, L1SkillId.DISINTEGRATE);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case MAGIC_RAGE1:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_magic_add_count(0);
				L1SkillUse.off_icons(pc, MAGIC_RAGE1);
			}
			break;
		case MAGIC_RAGE2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_magic_add_count(0);
				L1SkillUse.off_icons(pc, MAGIC_RAGE2);
			}
			break;
		case MAGIC_RAGE3:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_magic_add_count(0);
				L1SkillUse.off_icons(pc, MAGIC_RAGE3);
			}
			break;
		case MAGIC_RAGE4:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_magic_add_count(0);
				L1SkillUse.off_icons(pc, MAGIC_RAGE4);
			}
			break;
		case MAGIC_RAGE5:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_magic_add_count(0);
				L1SkillUse.off_icons(pc, MAGIC_RAGE5);
			}
			break;
		case DEVINE_PROTECTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_divine_protection(0);
				pc.sendPackets(SC_INSTANCE_HP_NOTI.make_stream(pc), true);
				L1SkillUse.off_icons(pc, L1SkillId.DEVINE_PROTECTION);
			}
			break;
		case CRUEL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
				L1SkillId.onFreezeAfterDelay(pc);
				pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
				
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case PRESHER: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}

			L1Magic _magic = new L1Magic(cha.getPresherPc(), cha);
			int dmg = cha.getPresherDamage();
			_magic.commit(dmg, 0);
			cha.send_effect(19335);

			cha.setPresherPc(null);
			cha.setPresherDamage(0);
			
			if (cha.getPresherDeathRecall()) {
				cha.setPresherDeathRecall(false);
			}
//			System.out.println("壓力傷害: " + dmg);
		}
			break;
		case VANGUARD:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (!pc.getVanguardType()) {
					pc.addMoveDelayRate(-25);
					pc.addAttackDelayRate(-10);
				} else {
					pc.addAttackDelayRate(-10);
				}
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case L1SkillId.MEDITATION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case L1SkillId.CLASS_RANK_BLESS_PRINCE_1:
		case L1SkillId.CLASS_RANK_BLESS_KNIGHT_1:
		case L1SkillId.CLASS_RANK_BLESS_ELF_1:
		case L1SkillId.CLASS_RANK_BLESS_WIZARD_1:
		case L1SkillId.CLASS_RANK_BLESS_DARKELF_1:
		case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_1:
		case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_1:
		case L1SkillId.CLASS_RANK_BLESS_WARRIOR_1:
		case L1SkillId.CLASS_RANK_BLESS_FENCER_1:
		case L1SkillId.CLASS_RANK_BLESS_LANCER_1:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-200);
				pc.getResistance().addcalcPcDefense(-10);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case L1SkillId.CLASS_RANK_BLESS_PRINCE_2:
		case L1SkillId.CLASS_RANK_BLESS_KNIGHT_2:
		case L1SkillId.CLASS_RANK_BLESS_ELF_2:
		case L1SkillId.CLASS_RANK_BLESS_WIZARD_2:
		case L1SkillId.CLASS_RANK_BLESS_DARKELF_2:
		case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_2:
		case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_2:
		case L1SkillId.CLASS_RANK_BLESS_WARRIOR_2:
		case L1SkillId.CLASS_RANK_BLESS_FENCER_2:
		case L1SkillId.CLASS_RANK_BLESS_LANCER_2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-150);
				pc.getResistance().addcalcPcDefense(-5);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case L1SkillId.CLASS_RANK_BLESS_PRINCE_3:
		case L1SkillId.CLASS_RANK_BLESS_KNIGHT_3:
		case L1SkillId.CLASS_RANK_BLESS_ELF_3:
		case L1SkillId.CLASS_RANK_BLESS_WIZARD_3:
		case L1SkillId.CLASS_RANK_BLESS_DARKELF_3:
		case L1SkillId.CLASS_RANK_BLESS_DRAGONKNIGHT_3:
		case L1SkillId.CLASS_RANK_BLESS_BLACKWIZARD_3:
		case L1SkillId.CLASS_RANK_BLESS_WARRIOR_3:
		case L1SkillId.CLASS_RANK_BLESS_FENCER_3:
		case L1SkillId.CLASS_RANK_BLESS_LANCER_3:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-100);
				pc.getResistance().addcalcPcDefense(-3);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
			case L1SkillId.CLAN_EXP_BUFF_1ST:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getClanRank() == L1Clan.MONARCH || pc.getClanRank() == L1Clan.VICE_MONARCH) {
						pc.add_item_exp_bonus(-15);
					} else if (pc.getClanRank() == L1Clan.GUARDIAN) {
						pc.add_item_exp_bonus(-14);
					} else if (pc.getClanRank() == L1Clan.ELITE) {
						pc.add_item_exp_bonus(-13);
					} else if (pc.getClanRank() == L1Clan.REGULAR) {
						pc.add_item_exp_bonus(-10);
					}
					L1SkillUse.off_icons(pc, skillId);
					ClanBuffListLoader.getInstance().remove_skill(pc, skillId);
				}
				break;
			case L1SkillId.CLAN_EXP_BUFF_2ND:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getClanRank() == L1Clan.MONARCH || pc.getClanRank() == L1Clan.VICE_MONARCH) {
						pc.add_item_exp_bonus(-35);
					} else if (pc.getClanRank() == L1Clan.GUARDIAN) {
						pc.add_item_exp_bonus(-34);
					} else if (pc.getClanRank() == L1Clan.ELITE) {
						pc.add_item_exp_bonus(-33);
					} else if (pc.getClanRank() == L1Clan.REGULAR) {
						pc.add_item_exp_bonus(-30);
					}
					L1SkillUse.off_icons(pc, skillId);
					ClanBuffListLoader.getInstance().remove_skill(pc, skillId);
				}
			break;
		case L1SkillId.CLAN_DEFENCE_BUFF_1ST:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// 防禦+1, 減傷+1, 魔防+5
				if (pc.getClanRank() == L1Clan.MONARCH || pc.getClanRank() == L1Clan.VICE_MONARCH || pc.getClanRank() == L1Clan.ELITE || pc.getClanRank() == L1Clan.GUARDIAN) {
					pc.addDamageReductionByArmor(1);
					pc.addMaxHp(-50);
				}
				// 防禦+1, 減傷+1, 魔防+5
				pc.getAC().addAc(1);
				pc.addDamageReductionByArmor(-1);
				pc.addMr(-5);
				
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case L1SkillId.CLAN_DEFENCE_BUFF_2ND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// 防禦+1, 減傷+1, 魔防+5
				if (pc.getClanRank() == L1Clan.MONARCH || pc.getClanRank() == L1Clan.VICE_MONARCH || pc.getClanRank() == L1Clan.ELITE || pc.getClanRank() == L1Clan.GUARDIAN) {
					pc.addDamageReductionByArmor(1);
					pc.addMaxHp(-50);
				}
				pc.getAC().addAc(2);
				pc.addDamageReductionByArmor(-2);
				pc.addMr(-10);
				pc.addSpecialResistance(eKind.ALL, -1); 
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}			
			break;
		case L1SkillId.CLAN_PVP_BUFF_1ST:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// HP+50, 命中 +1, 追加傷害 +1, 魔防 +3
				if (pc.getClanRank() == L1Clan.MONARCH || pc.getClanRank() == L1Clan.VICE_MONARCH || pc.getClanRank() == L1Clan.ELITE || pc.getClanRank() == L1Clan.GUARDIAN) {
					// HP+50, 命中 +2, 追加傷害 +1, 魔防 +3, 魔法命中 +1
					pc.addMagicHit(-1);
				}
				// HP+50, 命中 +2, 追加傷害 +1, 魔防 +3
				pc.addMaxHp(-50);
				pc.addDmgup(-1);
				pc.addBowDmgup(-1);
				pc.addHitup(-2);
				pc.addBowHitup(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				L1SkillUse.off_icons(pc, skillId);
				ClanBuffListLoader.getInstance().remove_skill(pc, skillId);
			}
			break;
		case L1SkillId.CLAN_PVP_BUFF_2ND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// HP+100, 命中 +2, 追加傷害 +2, 魔防 +5, 所有命中 +1
				if (pc.getClanRank() == L1Clan.MONARCH || pc.getClanRank() == L1Clan.VICE_MONARCH || pc.getClanRank() == L1Clan.ELITE || pc.getClanRank() == L1Clan.GUARDIAN) {
					pc.addMagicHit(-1);
					pc.addHitup(-1);
					pc.addBowHitup(-1);
				}
				// HP+100, 命中 +3, 追加傷害 +2, 所有命中 +1
				pc.addMaxHp(-100);
				pc.addDmgup(-2);
				pc.addBowDmgup(-2);
				pc.addHitup(-2);
				pc.addBowHitup(-2);
				pc.addSpecialPierce(eKind.ALL, -1);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.addSpecialPierce(eKind.ALL, -1);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				L1SkillUse.off_icons(pc, skillId);
				ClanBuffListLoader.getInstance().remove_skill(pc, skillId);
			}
			break;
		case STATUS_POISON_SILENCE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.setPoisonEffect(0);
				pc.sendPackets(new S_ServerMessage(311));
				pc.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, pc, 0, 0));
				pc.setPoison(null);
			}
			break;
		case DOLL_JUDGEMENT: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.getdoll_judgement_type() == 1) {
					pc.addSpecialResistance(eKind.ABILITY, 15);
				} else if (pc.getdoll_judgement_type() == 2) {
					pc.addSpecialResistance(eKind.SPIRIT, 15);
				} else if (pc.getdoll_judgement_type() == 3) {
					pc.addSpecialResistance(eKind.DRAGON_SPELL, 15);
				} else if (pc.getdoll_judgement_type() == 4) {
					pc.addSpecialResistance(eKind.FEAR, 15);
				} else if (pc.getdoll_judgement_type() == 5) {
					pc.getResistance().addMr(15);
				}
				pc.setdoll_judgement_type(0);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
		}
			break;
		case BLACK_DRAGON_MAAN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-5);
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case NAVER_BLACK_DRAGON_MAAN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-5);
				pc.add_magic_critical_rate(-1);
				pc.addDmgup(-2);
				pc.addBowDmgup(-2);
				pc.addSpecialResistance(eKind.ALL, -5);
//				L1SkillUse.off_icons(pc, skillId);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case DRAGON_HALPAS_WISH:
		case DRAGON_HALPAS_WATER:
		case DRAGON_HALPAS_FIRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case DRAGON_HALPAS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-10);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case ASURA:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.disposeAsura();
			}
			break;
		case DRAGON_ARMOR_BLESSING_REDUC: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance defen = (L1PcInstance) cha;
				int gap = 12 + defen.get_halpas_faith_pvp_reduc();
				defen.getResistance().addcalcPcDefense(-gap);
				defen.set_halpas_faith_pvp_reduc(0);
			}
		}
			break;
		case MAFR:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
		}
			break;
		case DESTROY: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(-5);
				
				if (pc.isDestroy_pier()) {
					pc.addDg(10);
					pc.setDestroy_pier(false);
				}
				
				if (pc.isDestroy_horror()) {
					pc.getAbility().addAddedStr((byte) 5);
					pc.getAbility().addAddedInt((byte) 5);
					pc.setDestroy_horror(false);
				}
			}
		}
			break;
		case DECIDING_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(5);
				pc.addBowHitup(-5);
				pc.addHitup(-5);
				pc.addBaseMagicHitUp(-2);
				pc.getResistance().addcalcPcDefense(-5);
				pc.getResistance().addPVPweaponTotalDamage(-5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case HERO_GAHO_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr(-1);
				pc.getAbility().addAddedDex(-1);
				pc.getAbility().addAddedInt(-1);
				pc.addBowHitup(-3);
				pc.addHitup(-3);
				pc.addBaseMagicHitUp(-3);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.getResistance().addPVPweaponTotalDamage(-3);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
//		case BERSERK:
//			if (cha instanceof L1PcInstance) {
//				L1PcInstance pc = (L1PcInstance) cha;
//				pc.addSpecialResistance(eKind.ALL, -20);
//				pc.addDmgup(-20);
//				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
//			}
//			break;
			case PC_CAFE:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					L1SkillId.offPcCafeBuff(pc);
					pc.getAccount().setBuff_PcCafe(null);
					pc.getAccount().updatePcCafe();
				}
				break;
		case EINHASAD_PRIMIUM_FLAT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillId.offEinhasadPrimiumFlat(pc);
			}
			break;
		case EINHASAD_GREAT_FLAT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillId.offEinhasadGreatFlat(pc);
			}
			break;
		case N_BUFF_PVP_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addPVPweaponTotalDamage(-1);
				pc.getResistance().addcalcPcDefense(-1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4346, 0));
			}
			break;
		case N_BUFF_HPMP:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addMaxMp(-50);
				pc.addWeightReduction(-3);
				pc.sendPackets(new S_HPUpdate(pc));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4347, 0));
			}
			break;
		case N_BUFF_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-1);
				pc.addBowDmgup(-1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4348, 0));
			}
			break;
		case N_BUFF_REDUCT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4349, 0));
			}
			break;
		case N_BUFF_SP:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4350, 0));
			}
			break;
		case N_BUFF_STUN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.ABILITY, -2); // 옵션
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4351, 0));
			}
			break;
		case N_BUFF_HOLD:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.SPIRIT, -2); // 옵션
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4352, 0));
			}
			break;
		case N_BUFF_WATER_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4342, 0));
			}
			break;
		case N_BUFF_WIND_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4343, 0));
			}
			break;
		case N_BUFF_EARTH_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4344, 0));
			}
			break;
		case N_BUFF_FIRE_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4345, 0));
			}
			break;
		case N_BUFF_STR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4338, 0));
			}
			break;
		case N_BUFF_DEX:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedDex(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4339, 0));
			}
			break;
		case N_BUFF_INT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedInt(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4340, 0));
			}
			break;
		case N_BUFF_WIS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedWis(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4341, 0));
			}
			break;
		case N_BUFF_WATER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addWater(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4333, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_WIND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addWind(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4334, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_EARTH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addEarth(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4335, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_FIRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addFire(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4336, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_ALL_RESIST:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addFire(-5);
				pc.getResistance().addEarth(-5);
				pc.getResistance().addWater(-5);
				pc.getResistance().addWind(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4337, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case L1SkillId.EXP_POTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case L1SkillId.EXP_POTION_Event:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(L1SkillId.EXP_POTION_Event);
				noti.set_duration(0);
				noti.set_off_icon_id(6768);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				// pc.sendPackets(S_InventoryIcon.icoEnd(EXP_POTION_Event));
			}
			break;
		case L1SkillId.SHADOW_FANG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, L1SkillId.SHADOW_FANG);
				pc.addDmgup(-5);
			}
			break;
		case L1SkillId.EXP_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				long hasad = pc.getAccount().getBlessOfAin();
				if (hasad <= 10000) {
					pc.sendPackets(S_InventoryIcon.icoEnd(EXP_BUFF + 1));
				} else {
					pc.sendPackets(S_InventoryIcon.icoEnd(EXP_BUFF));
				}
			}
			break;
		case IMMUNE_TO_HARM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_pvp_defense_per(0);
				pc.setImmunetoharm_saini(false);
				pc.setLastImmuneLevel(0);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case REDUCTION_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-pc.get_reducreduction_value());
				pc.set_reducreduction_value(0);
				if (pc != null && pc.is_reduction_armor_veteran()) {
					int bonus = 0;
					if (pc.getLevel() >= 80 && pc.getLevel() <= 100) {
						bonus = ((pc.getLevel() - 80) / 4) + 1;
					} else {
						bonus = 5;
					}
					pc.set_pvp_defense(-bonus);
					pc.addSpecialResistance(eKind.FEAR, -3);
					pc.set_reduction_armor_veteran(false);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				}
			}
			break;
/*		case MOEBIUS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.hasSkillEffect(L1SkillId.MOEBIUS)) {
					pc.removeSkillEffect(L1SkillId.MOEBIUS);
				}
				int bonus = (pc.getLevel() - 85) + 20;
				if (pc.getLevel() >= 95)
					bonus = 30;
				pc.add_pvp_defense_per(-bonus);
			}
			break;*/
		case LUCIFER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set_pvp_defense_per(0);
				pc.setLucifer_destiny(false);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case miso_Buff: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-10);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				pc.sendPackets(S_InventoryIcon.icoEnd(miso_Buff));
			}
		}
			break;
		case miso_Buff1: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addMr(-10);
				pc.addDamageReductionByArmor(-2);
				pc.addMaxHp(-100);
				pc.addHpr(-2);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				pc.sendPackets(S_InventoryIcon.icoEnd(miso_Buff1));
			}
		}
			break;
		case miso_Buff2: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-3);
				pc.addBowDmgup(-3);
				pc.getAbility().addSp(-2);
				pc.addMaxMp(-50);
				pc.addMpr(-2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));

				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				pc.sendPackets(S_InventoryIcon.icoEnd(miso_Buff2));
			}
		}
			break;
		case DRAGON_SET: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-3);
				pc.addDmgRate(-3);
				pc.addBowDmgup(-3);
				pc.addBowHitup(-3);
				pc.getAC().addAc(3);
				pc.getAbility().addSp(-2);
				pc.add_item_exp_bonus(-2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
			break;
		case HUNTER_BLESS: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-5);
			}
		}
			break;
		case HUNTER_BLESS3: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-2);
			}
		}
			break;
		case POLY_RING_MASTER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.getAbility().addAddedCha((byte) -1);
				pc.addMaxHp(-200);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));
				L1PolyMorph.undoPoly(pc);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case POLY_RING_MASTER2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) -5);
				pc.getAbility().addAddedDex((byte) -5);
				pc.getAbility().addAddedCon((byte) -5);
				pc.getAbility().addAddedInt((byte) -5);
				pc.getAbility().addAddedWis((byte) -5);
				pc.getAbility().addAddedCha((byte) -5);
				pc.addMaxHp(-500);
				pc.addSpecialResistance(eKind.ALL, -5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));
				L1PolyMorph.undoPoly(pc);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
			case TRAINEE_STEAK:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHitup(-1);
					pc.addDmgup(-2);
					pc.addHpr(-2);
					pc.addMpr(-2);
					pc.getResistance().addMr(-10);
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;

			case TRAINEE_SALMON:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addBowHitup(-1);
					pc.addBowDmgup(-2);
					pc.addHpr(-2);
					pc.addMpr(-2);
					pc.getResistance().addMr(-10);
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;

			case TRAINEE_TURKEY:
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addHpr(-2);
					pc.addMpr(-3);
					pc.getAbility().addSp(-2);
					pc.getResistance().addMr(-10);
					pc.getResistance().addAllNaturalResistance(-10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				}
				break;

		case THUNDER_GRAB:
			if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.set발묶임상태(false);
			} else if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
			}
			break;
		case DOUBLE_BRAKE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 2949, 0, false, true));
			}
			break;
		case FOCUS_SPRITS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_magic_critical_rate(-5);
				// L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case CUBE_RICH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-2);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case CUBE_GOLEM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(8);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case CUBE_OGRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-4);
				pc.addHitup(-4);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case CUBE_AVATAR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case IMPACT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialPierce(eKind.ALL, -pc.getImpactUp());
				pc.setImpactUp(0);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
			
		case TOMAHAWK:
			if (cha.getTomahawkHunter() != null) {
				cha.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(cha, 20597, false));
				cha.setTomahawkHunter(null);
				if (cha instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) cha;
					SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(target, 20597, false);
				}
			}
		case ARMOR_BRAKE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(ARMOR_BRAKE);
				noti.set_duration(0);
				noti.set_off_icon_id(4473);
				noti.set_is_good(false);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case TITANL_RISING:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.setRisingUp(0);
				// L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case DEATH_HEAL_Mob:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(DEATH_HEAL_Mob);
			}
			break;
		case DEATH_HEAL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(DEATH_HEAL);
			}
			break;
		case GRACE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int resistance = 1 + pc.getGraceLv();
				pc.addSpecialResistance(eKind.ALL, -resistance);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				// L1SkillUse.off_icons(pc, skillId);
			}
			break;
		/** 혈맹버프 **/
		case CLAN_BUFF1: {// 일반 공격 태세
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDmgupByArmor(-2);
			pc.addBowDmgupByArmor(-2);
			pc.sendPackets(new S_ServerMessage(4619, "$22503"));
		}
			break;
		case CLAN_BUFF2: {// 일반 방어 태세
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getAC().addAc(3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_ServerMessage(4619, "$22504"));
		}
			break;
		case CLAN_BUFF3: {// 전투 공격 태세
			L1PcInstance pc = (L1PcInstance) cha;
			// pc.addPvPDmgup(-1);
			pc.sendPackets(new S_ServerMessage(4619, "$22505"));
		}
			break;
		case CLAN_BUFF4: {// 전투 방어 태세
			L1PcInstance pc = (L1PcInstance) cha;
			// pc.addDmgReducPvp(-1);
			pc.sendPackets(new S_ServerMessage(4619, "$22506"));
		}
			break;
		case 정상의가호:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-8);
			}
			break;
		case L1SkillId.레벨업보너스:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, true, true));
			}
			break;
		case L1SkillId.DRAGON_PUPLE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, 1, true, true));
			}
			break;
		case L1SkillId.DRAGON_TOPAZ:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, 2, true, true));
			}
			break;
		case 나루토감사캔디:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
					pc.getAbility().addAddedDex((byte) -7);
					pc.sendPackets(new S_Dexup(pc, 1, 0));
					pc.getAbility().addAddedStr((byte) -7);
					pc.sendPackets(new S_Strup(pc, 1, 0));
				} else {
					pc.getAbility().addAddedDex((byte) -6);
					pc.sendPackets(new S_Dexup(pc, 1, 0));
					pc.getAbility().addAddedStr((byte) -6);
					pc.sendPackets(new S_Strup(pc, 1, 0));
				}
			}
			break;
		/*case DRESS_EVASION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.addEffectedER(-18);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			break;*/
		case AQUA_PROTECTER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addEffectedER(-5);
				// L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case BOUNCE_ATTACK: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addHitup(-6);
		}
			break;
//		case SOLID_CARRIAGE:
//			if (cha instanceof L1PcInstance) {
//				L1PcInstance pc = (L1PcInstance) cha;
//
//				pc.addEffectedER(-15);
//			}
//			break;
		case BLESS_WEAPON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, L1SkillId.BLESS_WEAPON);
				pc.addDmgup(-2);
				pc.addHitup(-2);
			}
			break;
		case HOLY_WEAPON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, L1SkillId.HOLY_WEAPON);
				pc.addDmgup(-1);
				pc.addHitup(-1);
			}
			break;
		case ENCHANT_WEAPON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.addDmgup(-2);
			}
			break;
		case BLESSED_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.getAC().addAc(3);
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			break;
		case GIGANTIC:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				// L1SkillUse.off_icons(pc, skillId);

				pc.addMaxHp(-pc.getMagicBuffHp());
				pc.setMagicBuffHp(0);
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			}
			break;
		case PRIDE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.addMaxHp(-pc.getMagicBuffHp());
				pc.setMagicBuffHp(0);
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			}
			break;
		case STRIKER_GALE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			
			if (cha.isStrikerGailShot()) {
				cha.setStrikerGailShot(false);
			}
			break;
		case LIGHT:
			if (cha instanceof L1PcInstance) {
				if (!cha.isInvisble()) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
				}
			}
			break;
		case TRUE_TARGET:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.setTrueTarget(0);
				pc.setTrueTargetClan(0);
				pc.setTrueTargetParty(0);
			} else if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setTrueTarget(0);
				npc.setTrueTargetClan(0);
				npc.setTrueTargetParty(0);
			}

			Broadcaster.broadcastPacket(cha, new S_TrueTargetNew(cha.getId(), false));
			synchronized (L1SkillUse._truetarget_list) {
				List<Integer> remove_list = new ArrayList<Integer>();
				for (Integer id : L1SkillUse._truetarget_list.keySet()) {
					L1Object o = L1SkillUse._truetarget_list.get(id);
					if (o.getId() != cha.getId())
						continue;
					remove_list.add(id);
				}
				for (Integer id : remove_list)
					L1SkillUse._truetarget_list.remove(id);
			}
			break;
		case God_buff: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getAC().addAc(2);
			pc.addHitup(-3);
			pc.addMaxHp(-20);
			pc.addMaxMp(-13);
			pc.addSpecialResistance(eKind.SPIRIT, -10);
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
		}
			break;
		case DELAY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (!pc.hasSkillEffect(L1SkillId.DELAY)) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_END));
				}
			}
			break;
		case BUFF_SAEL: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.hasSkillEffect(L1SkillId.BUFF_SAEL)) {
					pc.removeSkillEffect(L1SkillId.BUFF_SAEL);
				}
				pc.getAC().addAc(8);
				pc.addBowHitup(-6);
				pc.addBowDmgup(-3);
				pc.addMaxHp(-80);
				pc.addMaxMp(-10);
				pc.addHpr(-8);
				pc.addMpr(-1);
				pc.getResistance().addWater(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_SPMR(pc));
			}
		}
			break;
		case SHINING_SHILD:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if(pc.get_shining_shild_obj_id() == pc.getId()) {
					pc.getAC().addAc(8);
					pc.sendPackets(new S_OwnCharStatus(pc));
					pc.sendPackets(new S_NewSkillIcon(3941, 0));
				} else {
					pc.getAC().addAc(4);
					pc.sendPackets(new S_OwnCharStatus(pc));
					pc.sendPackets(new S_NewSkillIcon(3941, 0));
				}
			}
			break;
		case GLOWING_WEAPON:
			cha.addDmgup(-5);
			cha.addBowDmgup(-5);
			cha.addHitup(-5);
			cha.addBowHitup(-5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 25, false));
			}
			break;
		case BRAVE_MENTAL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// pc.sendPackets(new S_SkillIconAura(116, 0));
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 27, false));
			}
			break;
		case SHIELD:
			cha.getAC().addAc(2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(1, 0));
			}
			break;
		case BLIND_HIDING:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())){
					pc.delBlindHiding();
				} else {
				pc.delBlindHiding();
				}
			}
			break;
		case BLIND_HIDING_ASSASSIN:
			if (cha instanceof L1PcInstance){
				L1PcInstance pc = (L1PcInstance)cha;
				pc.removeSkillEffect(L1SkillId.BLIND_HIDING_ASSASSIN);
			}
			break;
		case SHADOW_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				if (pc != null && pc.isSHADOW_ARMOR_destiny()) {
					int bonus = 0;
					if (pc.getLevel() >= 85 && pc.getLevel() <= 94)
						bonus = ((pc.getLevel() - 85) / 2) + 10;
					else if (pc.getLevel() >= 95)
						bonus = 15;

					pc.getResistance().addMr(-bonus);
					pc.setSHADOW_ARMOR_destiny(false);
				} else {
					pc.getResistance().addMr(-5);
					pc.sendPackets(new S_SPMR(pc));
				}
			}
			break;
		case DRESS_DEXTERITY:
			cha.getAbility().addAddedDex((byte) -3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Dexup(pc, 3, 0));
			}
			break;
		case DRESS_MIGHTY:
			cha.getAbility().addAddedStr((byte) -3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Strup(pc, 3, 0));
			}
			break;
		/*case EARTH_GUARDIAN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(7, 0));
			}
			break;*/
		case RESIST_MAGIC:
			cha.getResistance().addMr(-10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case CLEAR_MIND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
			}
			break;
		case ENCHANT_ACURUCY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.addHitup(-5);
			}
			break;
		case FREEZEENG_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.addEffectedER(-5);
			}
			break;
		case ELEMENTAL_PROTECTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int attr = pc.getElfAttr();
				if (attr == 8) {
					pc.getResistance().addEarth(-50);
				} else if (attr == 1) {
					pc.getResistance().addFire(-50);
				} else if (attr == 2) {
					pc.getResistance().addWater(-50);
				} else if (attr == 4) {
					pc.getResistance().addWind(-50);
				}
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case ELEMENTAL_FALL_DOWN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int attr = pc.getAddAttrKind();
				int i = 50;
				switch (attr) {
				case 1:
					pc.getResistance().addEarth(i);
					break;
				case 2:
					pc.getResistance().addFire(i);
					break;
				case 4:
					pc.getResistance().addWater(i);
					break;
				case 8:
					pc.getResistance().addWind(i);
					break;
				default:
					break;
				}
				pc.setAddAttrKind(0);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.addSpecialPierce(eKind.SPIRIT, 10);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			} else if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				int attr = npc.getAddAttrKind();
				int i = 50;
				switch (attr) {
				case 1:
					npc.getResistance().addEarth(i);
					break;
				case 2:
					npc.getResistance().addFire(i);
					break;
				case 4:
					npc.getResistance().addWater(i);
					break;
				case 8:
					npc.getResistance().addWind(i);
					break;
				default:
					break;
				}
				npc.setAddAttrKind(0);
			}
			break;
		case IRON_SKIN:
			cha.getAC().addAc(10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(10, 0));
			}
			break;
		case FIRE_SHIELD:
			cha.getAC().addAc(4);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(4, 0));
			}
			break;
		case PHYSICAL_ENCHANT_STR:
			if (cha.getInventory().checkItem(30001398)) {
				cha.getAbility().addAddedStr((byte) -6);
			} else {
				cha.getAbility().addAddedStr((byte) -5);	
			}
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Strup(pc, 1, 0));
			}
			break;
		case PHYSICAL_ENCHANT_DEX:
			if (cha.getInventory().checkItem(30001398)) {
				cha.getAbility().addAddedDex((byte) -6);	
			} else {
				cha.getAbility().addAddedDex((byte) -5);	
			}
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
//				L1SkillUse.off_icons(pc, skillId);
				pc.sendPackets(new S_Dexup(pc, 1, 0));
			}
			break;
		case EARTH_WEAPON:
			cha.addHitup(-4);
			cha.addDmgup(-2);
			break;
		case INFERNO: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		}
		case FOCUS_WAVE:
		case HURRICANE:
		case SAND_STORM:
		case DANCING_BLADES:
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setAttackSpeed();
			}
			break;
		case BUFF_CRAY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-5);
				pc.addDmgup(-1);
				pc.addBowHitup(-5);
				pc.addBowDmgup(-1);
				pc.addMaxHp(-100);
				pc.addMaxMp(-50);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case BUFF_Vala:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-5);
				pc.addDmgup(-1);
				pc.addBowHitup(-5);
				pc.addBowDmgup(-1);
				pc.addMaxHp(-100);
				pc.addMaxMp(-50);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case BUFF_GUNTER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedDex((byte) -5);
				pc.addBowHitup(-7);
				pc.addBowDmgup(-5);
				pc.addMaxHp(-100);
				pc.addMaxMp(-40);
				pc.addHpr(-10);
				pc.addMpr(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		// UI DG표시
		case UNCANNY_DODGE: // 언케니닷지
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDg(-30);
			}
			break;
		case BURNING_WEAPON:
			cha.addDmgup(-6);
			cha.addHitup(-6);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(162, 0));
			}
			break;
		case BURNING_SHOT:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(STATUS_FREEZE);
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, false));
				pc.getResistance().addcalcPcDefense(-10);
				pc.addSpecialResistance(eKind.ALL, -3);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				L1SkillUse.off_icons(pc, BURNING_SHOT);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACKABLE_DISTANCE, pc, pc.getWeapon()), true);
			}
		}
		break;
		case MIRROR_IMAGE: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDg(-30);
		}
			break;
		case AQUA_SHOT:
			cha.addBowHitup(-4);
			break;
		case STORM_EYE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowHitup(-2);
				pc.addBowDmgup(-3);
				pc.sendPackets(new S_SkillIconAura(155, 0));
			}
			break;
		case STORM_SHOT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowDmgup(-5);
				pc.addBowHitup(-3);
				pc.sendPackets(new S_SkillIconAura(165, 0));
			}
			break;
		case BERSERKERS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.isWizard()) {
					pc.addDmgup(-2);
					pc.addHitup(-8);
				} else {
					pc.getAC().addAc(-10);
					pc.addDmgup(-2);
					pc.addHitup(-8);
				}
			}
			break;
		case SCALES_WATER_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int er = 0;
				if (pc.getLevel() >= 87){
					er = ((pc.getLevel() - 87) / 3) * 3 + 3;
					if (er >= 15){
						er = 15;
					}
				}
				
				pc.addEffectedER(-er);
			}
			break;
		case SCALES_EARTH_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				int ac = 0;
				int mr = 0;
				if (pc.getLevel() >= 87){
					ac = ((pc.getLevel() - 87) / 3) * -1 - 1;
					mr = ((pc.getLevel() - 87 ) / 3) * 2 + 2;
					if (ac <= -5){
						ac = -5;
					}
					if (mr >= 10){
						mr = 10;
					}
				}
				
				pc.getAC().addAc(-ac);
				pc.getResistance().addMr(-mr);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case SCALES_RINDVIOR_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				int dg = 0;
				if (pc.getLevel()>87){
					dg = ((pc.getLevel() - 87) / 3 ) * 3 + 3;
					if (dg>=15){
						dg = 15;
					}
				}
				pc.addDg(-dg);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case SCALES_FIRE_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int a = 0;
				if (pc.getLevel()>=87 ){
					a = (pc.getLevel() - 87)/3;
					if (a >=5){
						a = 5;
					}
				}
				
				pc.addSpecialResistance(eKind.ABILITY, -a);
				pc.addSpecialResistance(eKind.FEAR, -a);
				pc.addSpecialPierce(eKind.DRAGON_SPELL, -a);
				pc.addHitup(-5);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case IllUSION_OGRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.addDmgup(-4);
				pc.addHitup(-4);
			}
			break;
		/*case IllUSION_LICH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.getAbility().addSp(-2);
				pc.sendPackets(new S_SPMR(pc));
			}
			break;*/
		/*case IllUSION_DIAMONDGOLEM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.getAC().addAc(8);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;*/
		case IllUSION_AVATAR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// L1SkillUse.off_icons(pc, skillId);
				pc.addDmgup(-10);
			}
			break;
		case INVISIBILITY: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1DollInstance doll = pc.getMagicDoll();
				pc.delInvis();
				if (doll != null) {
					for (L1PcInstance tar : L1World.getInstance().getRecognizePlayer(doll)) {
						doll.onPerceive(tar);
					}
				}
			}
		}
			break;
		case TRIPLE_STUN:{
			if(cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance)cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			}
		}
			break;
		case INSIGHT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.resetBaseMr();
			}
			break;
		case Tam_Fruit1:
		case Tam_Fruit2:
		case Tam_Fruit3:
		case Tam_Fruit4:
		case Tam_Fruit5:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				updateTam(pc, skillId, false);
				recycleTam(pc);
			}
			break;
		case SHAPE_CHANGE:
			L1PolyMorph.undoPoly(cha);
			break;
			case GREAT_WARRIOR_BUFF: {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-5);
				pc.add_item_exp_bonus(-20);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, pc, 187, 0));
				pc.setDessertId(0);
			}
			break;

			case TRAINEE_CHICKEN_SOUP: {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-4);
			}
			break;
		case POWERRIP:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setImmobilized(false);
			}
			break;
		case DESPERADO:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, false));
				L1SkillUse.off_icons(pc, skillId);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setImmobilized(false);
			}
			cha.Desperadolevel = 0;
			break;
		case DEMOLITION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, false));
				L1SkillUse.off_icons(pc, skillId);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setImmobilized(false);
			}
			cha.Desperadolevel = 0;
			break;
		case ADVANCE_SPIRIT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				// L1SkillUse.off_icons(pc, skillId);

				pc.addMaxHp(-pc.getAdvenHp());
				pc.addMaxMp(-pc.getAdvenMp());
				pc.setAdvenHp(0);
				pc.setAdvenMp(0);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case MOB_HASTE:
		case HASTE:
		case GREATER_HASTE:
			cha.setMoveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			break;
		case EAGGLE_EYE:
			cha.add_missile_critical_rate(-2);
			break;
		case HOLY_WALK:
		case MOVING_ACCELERATION:
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
			}
			break;
		case BLOOD_LUST:
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
			}
			break;
		case DARK_BLIND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false));
				L1SkillUse.off_icons(pc, skillId);
			}
			cha.setSleeped(false);
			break;
		case CURSE_BLIND:
		case DARKNESS:
		case LINDBIOR_SPIRIT_EFFECT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_CurseBlind(0));
			}
			break;
		case CURSE_PARALYZE:
		case DESERT_SKILL1:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				L1SkillId.onFreezeAfterDelay(pc);
			}
			break;
		case WEAKNESS:
		case MOB_WEAKNESS_1:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(5);
				pc.addHitup(1);
			}
			break;
		case DISEASE:
		case MOB_DISEASE_1:
		case MOB_DISEASE_30:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(6);
				pc.getAC().addAc(-12);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case PANIC:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedCon((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				pc.getAbility().addAddedWis((byte) 1);
				pc.getAbility().addAddedCha((byte) 1);
				pc.resetBaseMr();
			}
			break;
		case ICE_LANCE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
				L1SkillId.onFreezeAfterDelay(pc);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.broadcastPacket(new S_Poison(npc.getId(), 0));
				npc.setParalyzed(false);
			}
			break;
//		case L1SkillId.MOB_BERSERKERS: {
//			if (cha instanceof L1NpcInstance) {
//				cha.setMoveSpeed(0);
//				cha.setBraveSpeed(0);
//			}
//			break;
//		}
		case EARTH_BIND:
		case MOB_BASILL:
		case MOB_COCA:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				if (skillId == EARTH_BIND)
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
				L1SkillId.onFreezeAfterDelay(pc);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.broadcastPacket(new S_Poison(npc.getId(), 0));
				// npc.setParalyzed(false);
			}
			break;
		case CHAINSWORD2:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(5);
				pc.addDg(10);
				pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
		}
			break;
		case CHAINSWORD1:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance)cha;
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0), true);
				pc.setChainSwordExposed(false);
				pc.setChainSwordStep(0);
			}
		}
			break;
		case FOU_SLAYER_BRAVE:
		case FOU_SLAYER_FORCE:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			}
			
		}
			break;
		case CHAINSWORD_STUN:
		case FORCE_STUN:
		case FORCE_STUN_FAIL:
		case SHOCK_STUN:
		case MOB_SHOCKSTUN_30:
		case MOB_RANGESTUN_18:
		case MOB_RANGESTUN_19:
		case MOB_RANGESTUN_20:
		case Mob_RANGESTUN_30:
		case ANTA_MESSAGE_6:
		case fornos_STUN:
		case MOSTER_STUN_1:
		case Moster_STUN:
		case ANTA_MESSAGE_7:
		case ANTA_MESSAGE_8:
		case ANTA_SHOCKSTUN:
		case OMAN_STUN:
		case DRAGON_HALPAS_STUN:
		case BALOCH_STUN:
		case BOS_STUN18:
		case Maeno_STUN:
		case Besi_STUN:
		case BONE_BREAK:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
				L1SkillId.onFreezeAfterDelay(pc);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case EMPIRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
				L1SkillId.onFreezeAfterDelay(pc);
				if (pc.isEmpireOverlord()) {
					pc.addDg(10);
					pc.setEmpireOverlord(false);
				}
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case OSIRIS:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_OSIRIS, false));
			}
		}
			break;
		case CONQUEROR_STUN:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			}
		}
			break;
		case PHANTASM:
		case FOG_OF_SLEEPING:
			cha.setSleeped(false);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false));
				pc.sendPackets(new S_OwnCharStatus(pc));
				L1SkillId.onFreezeAfterDelay(pc);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case SLOW:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			cha.setMoveSpeed(0);
			break;
		case STATUS_FREEZE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
				L1SkillId.onFreezeAfterDelay(pc);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
				npc.setImmobilized(false);
			}
			break;
		case STATUS_IGNITION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addFire(-30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case STATUS_QUAKE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addEarth(-30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case STATUS_SHOCK:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addWind(-30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case STATUS_BRAVE:
		case STATUS_ELFBRAVE:
		case STATUS_FRUIT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);// 군주 용기 추가
			}
			break;
		case STATUS_HASTE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			cha.setMoveSpeed(0);
			break;
		case STATUS_BLUE_POTION:
		case STATUS_BLUE_POTION2:
			break;
		case STATUS_UNDERWATER_BREATH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0));
			}
			break;
		case STATUS_WISDOM_POTION_POWER:
		case STATUS_WISDOM_POTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-2);
				pc.addMpr(-2);
				L1SkillUse.off_icons(pc, skillId);
			}
			break;
		case STATUS_CHAT_PROHIBITED:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_ServerMessage(288));
			}
			break;
		case STATUS_CASHSCROLL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case STATUS_CASHSCROLL2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-40);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case STATUS_CASHSCROLL3:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-3);
				pc.addHitup(-3);
				pc.getAbility().addSp(-3);
			}
			break;
		case STATUS_CASHSCROLL4:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-3);
				pc.addBaseMagicHitUp(-5);
				pc.getResistance().addcalcPcDefense(-3);
			}
			break;
		case STATUS_CASHSCROLL5:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowDmgup(-3);
				pc.addBowHitup(-5);
				pc.getResistance().addcalcPcDefense(-3);
			}
			break;
		case STATUS_CASHSCROLL6:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgRate(-3);
				pc.addHitup(-5);
				pc.getResistance().addcalcPcDefense(-3);
			}
			break;
		case STATUS_POISON:
			cha.curePoison();
			break;

		case COOKING_1_0_N:
		case COOKING_1_0_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 0, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_1_N:
		case COOKING_1_1_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_PacketBox(53, 1, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_2_N:
		case COOKING_1_2_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 2, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_3_N:
		case COOKING_1_3_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(1);
				pc.sendPackets(new S_PacketBox(53, 3, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_4_N:
		case COOKING_1_4_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-20);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(53, 4, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_5_N:
		case COOKING_1_5_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 5, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_6_N:
		case COOKING_1_6_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addMr(-5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 6, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_7_N:
		case COOKING_1_7_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-1);
				pc.sendPackets(new S_PacketBox(53, 7, 0));
				pc.setDessertId(0);
			}
			break;
		case COOKING_1_8_N:
		case COOKING_1_8_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 16, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_9_N:
		case COOKING_1_9_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-30);
				pc.addMaxHp(-30);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_PacketBox(53, 17, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_10_N:
		case COOKING_1_10_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(2);
				pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_PacketBox(53, 18, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_11_N:
		case COOKING_1_11_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 19, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_12_N:
		case COOKING_1_12_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 20, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_13_N:
		case COOKING_1_13_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addMr(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 21, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_14_N:
		case COOKING_1_14_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-1);
				pc.sendPackets(new S_PacketBox(53, 22, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_15_N:
		case COOKING_1_15_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-5);
				pc.sendPackets(new S_PacketBox(53, 7, 0));
				pc.setDessertId(0);
			}
			break;
		case COOKING_1_16_N:
		case COOKING_1_16_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowHitRate(-2);
				pc.addBowDmgup(-1);
				pc.sendPackets(new S_PacketBox(53, 45, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_17_N:
		case COOKING_1_17_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addMaxMp(-50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(53, 46, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_18_N:
		case COOKING_1_18_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-2);
				pc.addDmgup(-1);
				pc.sendPackets(new S_PacketBox(53, 47, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_19_N:
		case COOKING_1_19_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(3);
				pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_PacketBox(53, 48, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_20_N:
		case COOKING_1_20_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addAllNaturalResistance(-10);
				pc.getResistance().addMr(-15);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 49, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_21_N:
		case COOKING_1_21_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-2);
				pc.sendPackets(new S_PacketBox(53, 50, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_22_N:
		case COOKING_1_22_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_PacketBox(53, 51, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_23_N:
		case COOKING_1_23_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-9);
				pc.sendPackets(new S_PacketBox(53, 7, 0));
				pc.setDessertId(0);
			}
			break;
		case COMA_A:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedCon(-1);
				pc.getAbility().addAddedDex(-5);
				pc.getAbility().addAddedStr(-5);
				pc.addHitRate(-3);
				pc.getAC().addAc(3);
			}
			break;
		case COMA_B:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-1);
				pc.getAbility().addAddedCon(-3);
				pc.getAbility().addAddedDex(-5);
				pc.getAbility().addAddedStr(-5);
				pc.addHitRate(-5);
				pc.getAC().addAc(8);
				pc.add_item_exp_bonus(-20);
			}
			break;
		case FEATHER_BUFF_A:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.addDmgup(-2);
				pc.addHitup(-2);
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				pc.getAbility().addSp(-2);
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case FEATHER_BUFF_B:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-2);
				pc.getAbility().addSp(-1);
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case FEATHER_BUFF_C:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				pc.getAC().addAc(2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				if (pc.isInParty()) {
					// 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case FEATHER_BUFF_D:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(1);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case ANTA_MAAN:// 지룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case FAFU_MAAN:// 수룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.SPIRIT, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case LIND_MAAN:// 풍룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_magic_critical_rate(-2);
				pc.addSpecialResistance(eKind.FEAR, -5);
				pc.addEffectedER(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case VALA_MAAN:// 화룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-2);
				pc.addBowDmgup(-2);
				pc.addSpecialResistance(eKind.ABILITY, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case BIRTH_MAAN:// 탄생의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.SPIRIT, -5);
				pc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case SHAPE_MAAN:// 형상의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_magic_critical_rate(-1);
				pc.addEffectedER(-10);
				pc.addSpecialResistance(eKind.SPIRIT, -5);
				pc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				pc.addSpecialResistance(eKind.FEAR, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case LIFE_MAAN:// 생명의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_magic_critical_rate(-1);
				pc.addDmgup(-2);
				pc.addEffectedER(-10);
				pc.addSpecialResistance(eKind.ALL, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case SIDE_OF_ME_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-5);
			pc.addDmgup(-5);
			pc.addBowDmgup(-5);
		}
			break;
		case RE_START_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-3);
			pc.addDmgup(-3);
			pc.addBowDmgup(-3);
		}
			break;
		case NEW_START_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-2);
			pc.addDmgup(-2);
			pc.addBowDmgup(-2);
		}
			break;
		case LIFE_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-1);
			pc.addDmgup(-1);
			pc.addBowDmgup(-1);
		}
			break;
		case ANTA_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(2);
				pc.getResistance().addWater(-50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, 0));
			}
			break;
		case FAFU_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHpr(-3);
				pc.addMpr(-1);
				pc.getResistance().addWind(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, 0));
			}
			break;
		case RIND_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-3);
				pc.addBowHitup(-3);
				pc.getResistance().addFire(-50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, 0));
			}
			break;
		case STATUS_DRAGON_PEARL: // 드래곤의 진주
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				S_Liquor packet = new S_Liquor(pc.getId(), 0);
				pc.sendPackets(packet, false);
				pc.broadcastPacket(packet);
				pc.sendPackets(new S_ServerMessage(185));
				pc.setPearl(0);
			}
			break;
		case COOK_STR: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-2);
				pc.addDmgup(-2);
				pc.addHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.getResistance().addWater(-10);
				pc.getResistance().addFire(-10);
				pc.getResistance().addWind(-10);
				pc.getResistance().addEarth(-10);
				pc.add_item_exp_bonus(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 157, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.setCookingId(0);
			}
		}
			break;
		case COOK_DEX: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-2);
				pc.addBowDmgup(-2);
				pc.addBowHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addWater(-10);
				pc.getResistance().addFire(-10);
				pc.getResistance().addWind(-10);
				pc.getResistance().addEarth(-10);
				pc.getResistance().addMr(-10);
				pc.add_item_exp_bonus(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 158, 0));
				pc.setCookingId(0);
			}
		}
			break;
		case COOK_INT: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-2);
				pc.getAbility().addSp(-2);
				pc.addHpr(-2);
				pc.addMpr(-3);
				pc.getResistance().addMr(-10);
				pc.getResistance().addWater(-10);
				pc.getResistance().addFire(-10);
				pc.getResistance().addWind(-10);
				pc.getResistance().addEarth(-10);
				pc.add_item_exp_bonus(-2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 159, 0));
				pc.setCookingId(0);
			}
		}
			break;
		case 메티스정성요리:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-2);
				pc.addDmgup(-2);
				pc.addBowHitup(-2);
				pc.addBowDmgup(-2);
				pc.getAbility().addSp(-2);
				pc.addHpr(-3);
				pc.addMpr(-4);
				pc.getResistance().addMr(-15);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case 메티스정성스프:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-5);
			}
			break;
		case COUNTER_MIRROR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addWeightReduction(-300);
			}
			break;
		case DECREASE_WEIGHT:
		case REDUCE_WEIGHT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addWeightReduction(-180);
			}
			break;
		case COOK_GROW: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 160, 0));
				pc.add_item_exp_bonus(-4);
				pc.setDessertId(0);
			}
		}
			break;
		case COOK_STR_Bless: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-2);
				pc.addHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.getResistance().addWater(-10);
				pc.getResistance().addFire(-10);
				pc.getResistance().addWind(-10);
				pc.getResistance().addEarth(-10);
				pc.add_item_exp_bonus(-2);
				pc.addSpecialPierce(eKind.ALL, -3);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		}
		case COOK_DEX_Bless: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowDmgup(-2);
				pc.addBowHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.getResistance().addWater(-10);
				pc.getResistance().addFire(-10);
				pc.getResistance().addWind(-10);
				pc.getResistance().addEarth(-10);
				pc.add_item_exp_bonus(-2);
				pc.addSpecialPierce(eKind.ALL, -3);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		}
		case COOK_INT_Bless: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-2);
				pc.addHpr(-2);
				pc.addMpr(-3);
				pc.getResistance().addMr(-10);
				pc.getResistance().addWater(-10);
				pc.getResistance().addFire(-10);
				pc.getResistance().addWind(-10);
				pc.getResistance().addEarth(-10);
				pc.add_item_exp_bonus(-2);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		}
		case COOK_GROW_Bless: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addcalcPcDefense(-2);
				pc.add_item_exp_bonus(-4);
				pc.addSpecialResistance(eKind.ALL, -2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		}
		case 아덴의특제스테이크:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReduction(-2);
				pc.addDmgup(-3);
				pc.addHitup(-2);
				pc.addHpr(-5);
				pc.addMpr(-2);
				pc.addMaxHp(-50);
				pc.add_item_exp_bonus(-4);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case 아덴의특제카나페:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReduction(-2);
				pc.addBowDmgup(-3);
				pc.addBowHitup(-2);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.addMaxHp(-25);
				pc.addMaxMp(-25);
				pc.add_item_exp_bonus(-4);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case 아덴의특제샐러드:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReduction(-2);
				pc.getAbility().addSp(-3);
				pc.addHpr(-2);
				pc.addMpr(-5);
				pc.addMaxMp(-50);
				pc.add_item_exp_bonus(-4);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case 아덴의토마토스프:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReduction(-3);
				pc.add_item_exp_bonus(-6);
				pc.addMaxHp(-50);
				pc.getResistance().addcalcPcDefense(-2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case 축복받은아덴의특제스테이크:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReduction(-2);
				pc.addDmgup(-3);
				pc.addHitup(-2);
				pc.addHpr(-5);
				pc.addMpr(-2);
				pc.addMaxHp(-50);
				pc.add_item_exp_bonus(-4);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case 축복받은아덴의특제카나페:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReduction(-2);
				pc.addBowDmgup(-3);
				pc.addBowHitup(-2);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.addMaxHp(-25);
				pc.addMaxMp(-25);
				pc.add_item_exp_bonus(-4);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case 축복받은아덴의특제샐러드:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReduction(-2);
				pc.getAbility().addSp(-3);
				pc.addHpr(-2);
				pc.addMpr(-5);
				pc.addMaxMp(-50);
				pc.add_item_exp_bonus(-4);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case 축복받은아덴의토마토스프:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_item_exp_bonus(-6);
				pc.addMaxHp(-50);
				pc.getResistance().addcalcPcDefense(-2);
				pc.addSpecialResistance(eKind.ALL, -2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		
		
		case RINDVIOR_WIND_SHACKLE:
		case RINDVIOR_WIND_SHACKLE_1:
		case DRAKE_WIND_SHACKLE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), 0));
			}
			break;
		case L1SkillId.STR_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				pc.addHitup(-5);
				pc.addDmgup(-3);
				pc.getAbility().addAddedStr(-1);
				pc.sendPackets(new S_OwnCharStatus(pc), true);

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(STR_BUFF);
				noti.set_duration(0);
				noti.set_off_icon_id(4354);
				noti.set_end_str_id(2854);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case L1SkillId.DEX_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowHitup(-5);
				pc.addBowDmgup(-3);
				pc.getAbility().addAddedDex(-1);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(DEX_BUFF);
				noti.set_duration(0);
				noti.set_off_icon_id(4354);
				noti.set_end_str_id(2854);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case L1SkillId.INT_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-50);
				pc.getAbility().addSp(-2);
				pc.getAbility().addAddedInt(-1);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
				pc.sendPackets(new S_OwnCharStatus(pc), true);

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(INT_BUFF);
				noti.set_duration(0);
				noti.set_off_icon_id(4354);
				noti.set_end_str_id(2854);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case JUDGEMENT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.ALL, pc.getJudgementPoint());
				pc.setJudgementPoint(0);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case PHANTOM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setImmobilized(false);
			}
			break;
		case SHADOW_STEP://파워 그립으로 사용되므로 사용안하는데 놔둠
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
				pc.setShadowstepchaser(false);
				L1SkillId.onFreezeAfterDelay(pc);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
				npc.setImmobilized(false);
			}
			break;
		case SHADOW_STEP_CHASER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, false));
				pc.setShadowstepchaser(false);
				L1SkillId.onFreezeAfterDelay(pc);
			}
			break;
		case PANTHERA:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, false));
				L1SkillId.onFreezeAfterDelay(pc);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case PRIME:
			if (cha instanceof L1PcInstance) {
				int spellId = 0;
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.isPrimeCast()) {
					spellId = L1SkillId.PRIME_SIEGE;
					pc.addDmgup(-9);
					pc.addBowDmgup(-9);
					pc.addHitup(-9);
					pc.addBowHitup(-9);
					pc.getAbility().addSp(-6);
					pc.addBaseMagicHitUp(-6);
					pc.addMaxHp(-500);
					if (pc.getLevel() >= 85 && pc.getLevel() <= 89) {
						pc.set_pvp_defense(-5);
					} else if (pc.getLevel() >= 90 && pc.getLevel() <= 94) {
						pc.set_pvp_defense(-10);
					} else if (pc.getLevel() >= 95) {
						pc.set_pvp_defense(-15);
					}
					pc.addSpecialPierce(eKind.ABILITY, -15);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					pc.setIsPrimeCast(false);
				} else if (pc.isPrime_War_Zone()) {
					spellId = L1SkillId.PRIME_SIEGE;
					pc.addDmgup(-9);
					pc.addBowDmgup(-9);
					pc.addHitup(-9);
					pc.addBowHitup(-9);
					pc.getAbility().addSp(-6);
					pc.addBaseMagicHitUp(-6);
					pc.addMaxHp(-500);
					if (pc.getLevel() >= 85 && pc.getLevel() <= 89) {
						pc.set_pvp_defense(-5);
					} else if (pc.getLevel() >= 90 && pc.getLevel() <= 94) {
						pc.set_pvp_defense(-10);
					} else if (pc.getLevel() >= 95) {
						pc.set_pvp_defense(-15);
					}
				} else {
					spellId = L1SkillId.PRIME_NO_SIEGE;
					pc.addDmgup(-3);
					pc.addBowDmgup(-3);
					pc.addHitup(-3);
					pc.addBowHitup(-3);
					pc.getAbility().addSp(-2);
					pc.addBaseMagicHitUp(-2);
					if (pc.getLevel() >= 85 && pc.getLevel() <= 89) {
						pc.set_pvp_defense(-5);
					} else if (pc.getLevel() >= 90 && pc.getLevel() <= 94) {
						pc.set_pvp_defense(-10);
					} else if (pc.getLevel() >= 95) {
						pc.set_pvp_defense(-15);
					}
					
				}
				MJNotiSkillModel model = MJNotiSkillService.service().model(spellId);
				model.icons(pc, 0, false);
				pc.setPrime_War_Zone(false);
			}
			break;
		case ETERNITI:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PHANTOM, false));

				if (pc.hasSkillEffect(COUNTER_MAGIC)) {
					pc.removeSkillEffect(COUNTER_MAGIC);
					pc.send_effect(10702);
					return;
				}
				if (!cha.isDead() && cha.getZoneType() != 1) {
					L1Magic _magic = new L1Magic(pc._EternitiAttacker, cha);

					int dmg = _magic.calcMagicDamage(skillId);
					if (dmg > Config.MagicAdSetting_Wizard.ETERNITIMAXDMG)
						dmg = Config.MagicAdSetting_Wizard.ETERNITIMAXDMG;
					else if (dmg < Config.MagicAdSetting_Wizard.ETERNITIMINDMG)
						dmg = Config.MagicAdSetting_Wizard.ETERNITIMINDMG;

					_magic.commit(dmg, 0);
				}
				pc._EternitiAttacker = null;
				pc.broadcastPacket(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, pc.getId(), 18562, false), true);
				pc.sendPackets(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, pc.getId(), 18562, false), true);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setImmobilized(false);
				
				if (!cha.isDead() && cha.getZoneType() != 1) {
					L1Magic _magic = new L1Magic(npc._EternitiAttacker, cha);

					int dmg = _magic.calcMagicDamage(skillId);
					if (dmg > Config.MagicAdSetting_Wizard.ETERNITIMAXDMG)
						dmg = Config.MagicAdSetting_Wizard.ETERNITIMAXDMG;
					else if (dmg < Config.MagicAdSetting_Wizard.ETERNITIMINDMG)
						dmg = Config.MagicAdSetting_Wizard.ETERNITIMINDMG;

					_magic.commit(dmg, 0);
				}
				npc.broadcastPacket(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, npc.getId(), 18562, false), true);
				npc._EternitiAttacker = null;
			}
			break;
		case POTENTIAL: {
			L1PcInstance pc = (L1PcInstance) cha;

			L1SkillUse.off_icons(pc, skillId);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_OwnCharStatus(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
		}
			break;
		case TEMPEST:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
				L1SkillUse.off_icons(pc, skillId);
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		default:
			break;
		}

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;

			if (_skill != null) {
				if (_skill.isInvenIconUse()) {
					SC_SPELL_BUFF_NOTI.sendDatabaseIcon(pc, _skill, 0, false);
				}
			}

			sendStopMessage(pc, skillId);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	private static void sendStopMessage(L1PcInstance charaPc, int skillid) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillid);
		if (l1skills == null || charaPc == null) {
			return;
		}

		int msgID = l1skills.getSysmsgIdStop();
		if (msgID > 0) {
			charaPc.sendPackets(new S_ServerMessage(msgID));
		}
	}
}

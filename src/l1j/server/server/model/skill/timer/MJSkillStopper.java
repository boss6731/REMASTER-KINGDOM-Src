package l1j.server.server.model.skill.timer;

import java.util.ArrayList;
import java.util.List;
import l1j.server.ClanBuffList.ClanBuffListLoader;
import l1j.server.Config;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_INSTANCE_HP_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
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
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Skills;

class MJSkillStopper {
    static void stopSkill(L1Character cha, int skillId) {
        L1Magic _magic;
        L1PcInstance pc;
        int dmg;
        L1Skills _skill = SkillsTable.getInstance().getTemplate(skillId);
        if (cha instanceof MJCompanionInstance && (
                (MJCompanionInstance)cha).on_buff_stopped(skillId))
            return;
        switch (skillId) {
            case 5057:
                cha.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(cha, 21964, false));
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addMoveDelayRate(50.0D);
                    SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(l1PcInstance, 21964, false);
                }
                break;
            case 60211:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedStr(-1);
                    l1PcInstance.addDmgup(-3);
                    l1PcInstance.addHitup(-5);
                }
                break;
            case 60212:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedDex(-1);
                    l1PcInstance.addBowDmgup(-3);
                    l1PcInstance.addBowHitup(-5);
                }
                break;
            case 60213:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedInt(-1);
                    l1PcInstance.getAbility().addSp(-1);
                    l1PcInstance.addBaseMagicHitUp(-3);
                }
                break;
            case 5047:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 995050:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addAttackDelayRate(50.0D);
                    l1PcInstance.set_Tyrant_Excute(false);
                }
                break;
            case 5531:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addAttackDelayRate(-10.0D);
                }
                break;
            case 5033:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addMoveDelayRate(-10.0D);
                }
                break;
            case 11223:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 5036:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addMoveDelayRate(50.0D);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 245:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 5157:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                    l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, 10);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                }
                cha.addMoveDelayRate(50.0D);
                break;
            case 77:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                    L1SkillId.onFreezeAfterDelay(l1PcInstance);
                    L1SkillUse.off_icons(l1PcInstance, 77);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.setParalyzed(false);
                }
                break;
            case 3087:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_magic_add_count(0);
                    L1SkillUse.off_icons(l1PcInstance, 3087);
                }
                break;
            case 3088:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_magic_add_count(0);
                    L1SkillUse.off_icons(l1PcInstance, 3088);
                }
                break;
            case 3089:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_magic_add_count(0);
                    L1SkillUse.off_icons(l1PcInstance, 3089);
                }
                break;
            case 3090:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_magic_add_count(0);
                    L1SkillUse.off_icons(l1PcInstance, 3090);
                }
                break;
            case 3091:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_magic_add_count(0);
                    L1SkillUse.off_icons(l1PcInstance, 3091);
                }
                break;
            case 5152:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_divine_protection(0);
                    l1PcInstance.sendPackets(SC_INSTANCE_HP_NOTI.make_stream(l1PcInstance), true);
                    L1SkillUse.off_icons(l1PcInstance, 5152);
                }
                break;
            case 5056:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                    L1SkillId.onFreezeAfterDelay(l1PcInstance);
                    l1PcInstance.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(l1PcInstance));
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.setParalyzed(false);
                }
                break;
            case 5055:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                _magic = new L1Magic((L1Character)cha.getPresherPc(), cha);
                dmg = cha.getPresherDamage();
                _magic.commit(dmg, 0);
                cha.send_effect(19335);
                cha.setPresherPc(null);
                cha.setPresherDamage(0);
                if (cha.getPresherDeathRecall())
                    cha.setPresherDeathRecall(false);
                break;
            case 5053:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (!l1PcInstance.getVanguardType()) {
                        l1PcInstance.addMoveDelayRate(-25.0D);
                        l1PcInstance.addAttackDelayRate(-10.0D);
                    } else {
                        l1PcInstance.addAttackDelayRate(-10.0D);
                    }
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 32:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 888810:
            case 888811:
            case 888812:
            case 888813:
            case 888814:
            case 888815:
            case 888816:
            case 888817:
            case 888818:
            case 888819:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addMaxHp(-200);
                    l1PcInstance.getResistance().addcalcPcDefense(-10);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 888822:
            case 888823:
            case 888824:
            case 888825:
            case 888826:
            case 888827:
            case 888828:
            case 888829:
            case 888830:
            case 888831:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addMaxHp(-150);
                    l1PcInstance.getResistance().addcalcPcDefense(-5);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 888832:
            case 888833:
            case 888834:
            case 888835:
            case 888836:
            case 888837:
            case 888838:
            case 888839:
            case 888840:
            case 888841:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addMaxHp(-100);
                    l1PcInstance.getResistance().addcalcPcDefense(-3);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 888806:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.getClanRank() == 10 || l1PcInstance.getClanRank() == 14) {
                        l1PcInstance.add_item_exp_bonus(-15.0D);
                    } else if (l1PcInstance.getClanRank() == 9) {
                        l1PcInstance.add_item_exp_bonus(-14.0D);
                    } else if (l1PcInstance.getClanRank() == 13) {
                        l1PcInstance.add_item_exp_bonus(-13.0D);
                    } else if (l1PcInstance.getClanRank() == 8) {
                        l1PcInstance.add_item_exp_bonus(-10.0D);
                    }
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                    ClanBuffListLoader.getInstance().remove_skill(l1PcInstance, skillId);
                }
                break;
            case 888807:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.getClanRank() == 10 || l1PcInstance.getClanRank() == 14) {
                        l1PcInstance.add_item_exp_bonus(-35.0D);
                    } else if (l1PcInstance.getClanRank() == 9) {
                        l1PcInstance.add_item_exp_bonus(-34.0D);
                    } else if (l1PcInstance.getClanRank() == 13) {
                        l1PcInstance.add_item_exp_bonus(-33.0D);
                    } else if (l1PcInstance.getClanRank() == 8) {
                        l1PcInstance.add_item_exp_bonus(-30.0D);
                    }
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                    ClanBuffListLoader.getInstance().remove_skill(l1PcInstance, skillId);
                }
                break;
            case 888820:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.getClanRank() == 10 || l1PcInstance.getClanRank() == 14 || l1PcInstance.getClanRank() == 13 || l1PcInstance.getClanRank() == 9) {
                        l1PcInstance.addDamageReductionByArmor(1);
                        l1PcInstance.addMaxHp(-50);
                    }
                    l1PcInstance.getAC().addAc(1);
                    l1PcInstance.addDamageReductionByArmor(-1);
                    l1PcInstance.addMr(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                }
                break;
            case 888821:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.getClanRank() == 10 || l1PcInstance.getClanRank() == 14 || l1PcInstance.getClanRank() == 13 || l1PcInstance.getClanRank() == 9) {
                        l1PcInstance.addDamageReductionByArmor(1);
                        l1PcInstance.addMaxHp(-50);
                    }
                    l1PcInstance.getAC().addAc(2);
                    l1PcInstance.addDamageReductionByArmor(-2);
                    l1PcInstance.addMr(-10);
                    l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                }
                break;
            case 888808:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.getClanRank() == 10 || l1PcInstance.getClanRank() == 14 || l1PcInstance.getClanRank() == 13 || l1PcInstance.getClanRank() == 9)
                        l1PcInstance.addMagicHit(-1);
                    l1PcInstance.addMaxHp(-50);
                    l1PcInstance.addDmgup(-1);
                    l1PcInstance.addBowDmgup(-1);
                    l1PcInstance.addHitup(-2);
                    l1PcInstance.addBowHitup(-2);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_HPUpdate(l1PcInstance.getCurrentHp(), l1PcInstance.getMaxHp()));
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                    ClanBuffListLoader.getInstance().remove_skill(l1PcInstance, skillId);
                }
                break;
            case 888809:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.getClanRank() == 10 || l1PcInstance.getClanRank() == 14 || l1PcInstance.getClanRank() == 13 || l1PcInstance.getClanRank() == 9) {
                        l1PcInstance.addMagicHit(-1);
                        l1PcInstance.addHitup(-1);
                        l1PcInstance.addBowHitup(-1);
                    }
                    l1PcInstance.addMaxHp(-100);
                    l1PcInstance.addDmgup(-2);
                    l1PcInstance.addBowDmgup(-2);
                    l1PcInstance.addHitup(-2);
                    l1PcInstance.addBowHitup(-2);
                    l1PcInstance.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_HPUpdate(l1PcInstance.getCurrentHp(), l1PcInstance.getMaxHp()));
                    l1PcInstance.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -1);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                    ClanBuffListLoader.getInstance().remove_skill(l1PcInstance, skillId);
                }
                break;
            case 1007:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.setPoisonEffect(0);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_ServerMessage(311));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_PacketBox(161, l1PcInstance, 0, 0));
                    l1PcInstance.setPoison(null);
                }
                break;
            case 9598:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.getdoll_judgement_type() == 1) {
                        l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, 15);
                    } else if (l1PcInstance.getdoll_judgement_type() == 2) {
                        l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, 15);
                    } else if (l1PcInstance.getdoll_judgement_type() == 3) {
                        l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, 15);
                    } else if (l1PcInstance.getdoll_judgement_type() == 4) {
                        l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, 15);
                    } else if (l1PcInstance.getdoll_judgement_type() == 5) {
                        l1PcInstance.getResistance().addMr(15);
                    }
                    l1PcInstance.setdoll_judgement_type(0);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                }
                break;
            case 7687:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDamageReductionByArmor(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                }
                break;
            case 7688:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDamageReductionByArmor(-5);
                    l1PcInstance.add_magic_critical_rate(-1);
                    l1PcInstance.addDmgup(-2);
                    l1PcInstance.addBowDmgup(-2);
                    l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -5);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                }
                break;
            case 707156:
            case 707160:
            case 707161:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 2100000:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDmgup(-10);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 5017:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.disposeAsura();
                }
                break;
            case 8002:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance defen = (L1PcInstance)cha;
                    int gap = 12 + defen.get_halpas_faith_pvp_reduc();
                    defen.getResistance().addcalcPcDefense(-gap);
                    defen.set_halpas_faith_pvp_reduc(0);
                }
                break;
            case 139:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 183:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAC().addAc(-5);
                    if (l1PcInstance.isDestroy_pier()) {
                        l1PcInstance.addDg(10);
                        l1PcInstance.setDestroy_pier(false);
                    }
                    if (l1PcInstance.isDestroy_horror()) {
                        l1PcInstance.getAbility().addAddedStr(5);
                        l1PcInstance.getAbility().addAddedInt(5);
                        l1PcInstance.setDestroy_horror(false);
                    }
                }
                break;
            case 85000:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAC().addAc(5);
                    l1PcInstance.addBowHitup(-5);
                    l1PcInstance.addHitup(-5);
                    l1PcInstance.addBaseMagicHitUp(-2);
                    l1PcInstance.getResistance().addcalcPcDefense(-5);
                    l1PcInstance.getResistance().addPVPweaponTotalDamage(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharStatus(l1PcInstance));
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 85001:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedStr(-1);
                    l1PcInstance.getAbility().addAddedDex(-1);
                    l1PcInstance.getAbility().addAddedInt(-1);
                    l1PcInstance.addBowHitup(-3);
                    l1PcInstance.addHitup(-3);
                    l1PcInstance.addBaseMagicHitUp(-3);
                    l1PcInstance.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    l1PcInstance.getResistance().addPVPweaponTotalDamage(-3);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharStatus(l1PcInstance));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 80002:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillId.offPcCafeBuff(l1PcInstance);
                    l1PcInstance.getAccount().setBuff_PC(null);
                    l1PcInstance.getAccount().update();
                }
                break;
            case 80000:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillId.offEinhasadPrimiumFlat(l1PcInstance);
                }
                break;
            case 80001:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillId.offEinhasadGreatFlat(l1PcInstance);
                }
                break;
            case 4075:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getResistance().addPVPweaponTotalDamage(-1);
                    l1PcInstance.getResistance().addcalcPcDefense(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4346, 0));
                }
                break;
            case 4076:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addMaxHp(-50);
                    l1PcInstance.addMaxMp(-50);
                    l1PcInstance.addWeightReduction(-3);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_HPUpdate(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_MPUpdate(l1PcInstance.getCurrentMp(), l1PcInstance.getMaxMp()));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4347, 0));
                }
                break;
            case 4077:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDmgup(-1);
                    l1PcInstance.addBowDmgup(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4348, 0));
                }
                break;
            case 4078:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDamageReductionByArmor(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4349, 0));
                }
                break;
            case 4079:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addSp(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4350, 0));
                }
                break;
            case 4080:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -2);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4351, 0));
                }
                break;
            case 4081:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -2);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4352, 0));
                }
                break;
            case 4082:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4342, 0));
                }
                break;
            case 4083:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4343, 0));
                }
                break;
            case 4084:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4344, 0));
                }
                break;
            case 4085:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4345, 0));
                }
                break;
            case 4086:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedStr(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4338, 0));
                }
                break;
            case 4087:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedDex(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4339, 0));
                }
                break;
            case 4088:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedInt(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4340, 0));
                }
                break;
            case 4089:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedWis(-1);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4341, 0));
                }
                break;
            case 4090:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getResistance().addWater(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4333, 0));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 4091:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getResistance().addWind(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4334, 0));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 4092:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getResistance().addEarth(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4335, 0));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 4093:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getResistance().addFire(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4336, 0));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 4094:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getResistance().addFire(-5);
                    l1PcInstance.getResistance().addEarth(-5);
                    l1PcInstance.getResistance().addWater(-5);
                    l1PcInstance.getResistance().addWind(-5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_IvenBuffIcon(skillId, false, 4337, 0));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 23069:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 13069:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
                    noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
                    noti.set_spell_id(13069);
                    noti.set_duration(0);
                    noti.set_off_icon_id(6768);
                    noti.set_end_str_id(0);
                    noti.set_is_good(true);
                    l1PcInstance.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
                }
                break;
            case 107:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDmgup(-5);
                }
                break;
            case 8382:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    long hasad = l1PcInstance.getAccount().getBlessOfAin();
                    if (hasad <= 10000L) {
                        l1PcInstance.sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(8383));
                        break;
                    }
                    l1PcInstance.sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(8382));
                }
                break;
            case 68:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_pvp_defense_per(0);
                    l1PcInstance.setImmunetoharm_saini(false);
                    l1PcInstance.setLastImmuneLevel(0);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 88:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDamageReductionByArmor(-l1PcInstance.get_reducreduction_value());
                    l1PcInstance.set_reducreduction_value(0);
                    if (l1PcInstance != null && l1PcInstance.is_reduction_armor_veteran()) {
                        int bonus = 0;
                        if (l1PcInstance.getLevel() >= 80 && l1PcInstance.getLevel() <= 100) {
                            bonus = (l1PcInstance.getLevel() - 80) / 4 + 1;
                        } else {
                            bonus = 5;
                        }
                        l1PcInstance.set_pvp_defense(-bonus);
                        l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -3);
                        l1PcInstance.set_reduction_armor_veteran(false);
                        SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    }
                }
                break;
            case 234:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.set_pvp_defense_per(0);
                    l1PcInstance.setLucifer_destiny(false);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 8132:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.add_item_exp_bonus(-10.0D);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    l1PcInstance.sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(8132));
                }
                break;
            case 8133:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getResistance().addMr(-10);
                    l1PcInstance.addDamageReductionByArmor(-2);
                    l1PcInstance.addMaxHp(-100);
                    l1PcInstance.addHpr(-2);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    l1PcInstance.sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(8133));
                }
                break;
            case 8134:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDmgup(-3);
                    l1PcInstance.addBowDmgup(-3);
                    l1PcInstance.getAbility().addSp(-2);
                    l1PcInstance.addMaxMp(-50);
                    l1PcInstance.addMpr(-2);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    l1PcInstance.sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(8134));
                }
                break;
            case 3281:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDmgup(-3);
                    l1PcInstance.addDmgRate(-3);
                    l1PcInstance.addBowDmgup(-3);
                    l1PcInstance.addBowHitup(-3);
                    l1PcInstance.getAC().addAc(3);
                    l1PcInstance.getAbility().addSp(-2);
                    l1PcInstance.add_item_exp_bonus(-2.0D);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 5934:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.add_item_exp_bonus(-5.0D);
                }
                break;
            case 9904:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.add_item_exp_bonus(-2.0D);
                }
                break;
            case 80012:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedStr(-1);
                    l1PcInstance.getAbility().addAddedDex(-1);
                    l1PcInstance.getAbility().addAddedCon(-1);
                    l1PcInstance.getAbility().addAddedInt(-1);
                    l1PcInstance.getAbility().addAddedWis(-1);
                    l1PcInstance.getAbility().addAddedCha(-1);
                    l1PcInstance.addMaxHp(-200);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharStatus(l1PcInstance));
                    L1PolyMorph.undoPoly((L1Character)l1PcInstance);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                }
                break;
            case 80013:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addAddedStr(-5);
                    l1PcInstance.getAbility().addAddedDex(-5);
                    l1PcInstance.getAbility().addAddedCon(-5);
                    l1PcInstance.getAbility().addAddedInt(-5);
                    l1PcInstance.getAbility().addAddedWis(-5);
                    l1PcInstance.getAbility().addAddedCha(-5);
                    l1PcInstance.addMaxHp(-500);
                    l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -5);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharStatus(l1PcInstance));
                    L1PolyMorph.undoPoly((L1Character)l1PcInstance);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                }
                break;
            case 3080:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addHitup(-1);
                    l1PcInstance.addDmgup(-2);
                    l1PcInstance.addHpr(-2);
                    l1PcInstance.addMpr(-2);
                    l1PcInstance.getResistance().addMr(-10);
                    l1PcInstance.getResistance().addAllNaturalResistance(-10);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 3081:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addBowHitup(-1);
                    l1PcInstance.addBowDmgup(-2);
                    l1PcInstance.addHpr(-2);
                    l1PcInstance.addMpr(-2);
                    l1PcInstance.getResistance().addMr(-10);
                    l1PcInstance.getResistance().addAllNaturalResistance(-10);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_SPMR(l1PcInstance));
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 3082:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addHpr(-2);
                    l1PcInstance.addMpr(-3);
                    l1PcInstance.getAbility().addSp(-2);
                    l1PcInstance.getResistance().addMr(-10);
                    l1PcInstance.getResistance().addAllNaturalResistance(-10);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                }
                break;
            case 192:
                if (cha instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.set(false);
                    break;
                }
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_Paralysis(6, false));
                }
                break;
            case 105:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.sendPackets((ServerBasePacket)new S_PacketBox(154, 2949, 0, false, true));
                }
                break;
            case 223:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.add_magic_critical_rate(-5);
                }
                break;
            case 215:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAbility().addSp(-2);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 210:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.getAC().addAc(8);
                    l1PcInstance.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(l1PcInstance));
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 205:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addDmgup(-4);
                    l1PcInstance.addHitup(-4);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 220:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 222:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -l1PcInstance.getImpactUp());
                    l1PcInstance.setImpactUp(0);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                    L1SkillUse.off_icons(l1PcInstance, skillId);
                }
                break;
            case 229:
                if (cha.getTomahawkHunter() != null) {
                    cha.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(cha, 20597, false));
                    cha.setTomahawkHunter(null);
                    if (cha instanceof L1PcInstance) {
                        L1PcInstance target = (L1PcInstance)cha;
                        SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(target, 20597, false);
                    }
                }
            case 112:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
                    noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
                    noti.set_spell_id(112);
                    noti.set_duration(0);
                    noti.set_off_icon_id(4473);
                    noti.set_is_good(false);
                    l1PcInstance.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
                }
                break;
            case 231:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.setRisingUp(0);
                }
                break;
            case 707073:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.removeSkillEffect(707073);
                }
                break;
            case 73:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    l1PcInstance.removeSkillEffect(73);
                }
                break;
            case 122:
                if (cha instanceof L1PcInstance) {
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    int resistance = 1 + l1PcInstance.getGraceLv();
                    l1PcInstance.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -resistance);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                }
                break;
            case 505:
                pc = (L1PcInstance)cha;
                pc.addDmgupByArmor(-2);
                pc.addBowDmgupByArmor(-2);
                pc.sendPackets((ServerBasePacket)new S_ServerMessage(4619, "$22503"));
                break;
            case 506:
                pc = (L1PcInstance)cha;
                pc.getAC().addAc(3);
                pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                pc.sendPackets((ServerBasePacket)new S_ServerMessage(4619, "$22504"));
                break;
            case 507:
                pc = (L1PcInstance)cha;
                pc.sendPackets((ServerBasePacket)new S_ServerMessage(4619, "$22505"));
                break;
            case 508:
                pc = (L1PcInstance)cha;
                pc.sendPackets((ServerBasePacket)new S_ServerMessage(4619, "$22506"));
                break;
            case 80004:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReductionByArmor(-8);
                }
                break;
            case 80007:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(0, true, true));
                }
                break;
            case 80008:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(0, 1, true, true));
                }
                break;
            case 80009:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(0, 2, true, true));
                }
                break;
            case 80005:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
                        pc.getAbility().addAddedDex(-7);
                        pc.sendPackets((ServerBasePacket)new S_Dexup(pc, 1, 0));
                        pc.getAbility().addAddedStr(-7);
                        pc.sendPackets((ServerBasePacket)new S_Strup(pc, 1, 0));
                        break;
                    }
                    pc.getAbility().addAddedDex(-6);
                    pc.sendPackets((ServerBasePacket)new S_Dexup(pc, 1, 0));
                    pc.getAbility().addAddedStr(-6);
                    pc.sendPackets((ServerBasePacket)new S_Strup(pc, 1, 0));
                }
                break;
            case 160:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addEffectedER(-5);
                }
                break;
            case 89:
                pc = (L1PcInstance)cha;
                pc.addHitup(-6);
                break;
            case 48:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    L1SkillUse.off_icons(pc, 48);
                    pc.addDmgup(-2);
                    pc.addHitup(-2);
                }
                break;
            case 8:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(-1);
                    pc.addHitup(-1);
                }
                break;
            case 12:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(-2);
                }
                break;
            case 21:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAC().addAc(3);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                }
                break;
            case 383:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-pc.getMagicBuffHp());
                    pc.setMagicBuffHp(0);
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                }
                break;
            case 362:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-pc.getMagicBuffHp());
                    pc.setMagicBuffHp(0);
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                }
                break;
            case 174:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    L1SkillUse.off_icons(pc, skillId);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(132, pc.getTotalER()));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                }
                if (cha.isStrikerGailShot())
                    cha.setStrikerGailShot(false);
                break;
            case 2:
                if (cha instanceof L1PcInstance &&
                        !cha.isInvisble()) {
                    pc = (L1PcInstance)cha;
                    pc.getLight().turnOnOffLight();
                }
                break;
            case 113:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.setTrueTarget(0);
                    pc.setTrueTargetClan(0);
                    pc.setTrueTargetParty(0);
                } else if (cha instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.setTrueTarget(0);
                    npc.setTrueTargetClan(0);
                    npc.setTrueTargetParty(0);
                }
                Broadcaster.broadcastPacket(cha, (ServerBasePacket)new S_TrueTargetNew(cha.getId(), false));
                synchronized (L1SkillUse._truetarget_list) {
                    List<Integer> remove_list = new ArrayList<>();
                    for (Integer id : L1SkillUse._truetarget_list.keySet()) {
                        L1Object o = (L1Object)L1SkillUse._truetarget_list.get(id);
                        if (o.getId() != cha.getId())
                            continue;
                        remove_list.add(id);
                    }
                    for (Integer id : remove_list)
                        L1SkillUse._truetarget_list.remove(id);
                }
                break;
            case 4914:
                pc = (L1PcInstance)cha;
                pc.getAC().addAc(2);
                pc.addHitup(-3);
                pc.addMaxHp(-20);
                pc.addMaxMp(-13);
                pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -10);
                SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                break;
            case 9999:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    if (!pc.hasSkillEffect(9999))
                        pc.sendPackets((ServerBasePacket)new S_PacketBox(70));
                }
                break;
            case 10499:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    if (pc.hasSkillEffect(10499))
                        pc.removeSkillEffect(10499);
                    pc.getAC().addAc(8);
                    pc.addBowHitup(-6);
                    pc.addBowDmgup(-3);
                    pc.addMaxHp(-80);
                    pc.addMaxMp(-10);
                    pc.addHpr(-8);
                    pc.addMpr(-1);
                    pc.getResistance().addWater(-30);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                }
                break;
            case 115:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    if (pc.get_shining_shild_obj_id() == pc.getId()) {
                        pc.getAC().addAc(8);
                        pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                        pc.sendPackets((ServerBasePacket)new S_NewSkillIcon(3941, 0));
                        break;
                    }
                    pc.getAC().addAc(4);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                    pc.sendPackets((ServerBasePacket)new S_NewSkillIcon(3941, 0));
                }
                break;
            case 114:
                cha.addDmgup(-5);
                cha.addBowDmgup(-5);
                cha.addHitup(-5);
                cha.addBowHitup(-5);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 25, false));
                }
                break;
            case 117:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 27, false));
                }
                break;
            case 3:
                cha.getAC().addAc(2);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillIconShield(1, 0));
                }
                break;
            case 97:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    if (pc.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
                        pc.delBlindHiding();
                        break;
                    }
                    pc.delBlindHiding();
                }
                break;
            case 396:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.removeSkillEffect(396);
                }
                break;
            case 99:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    if (pc != null && pc.isSHADOW_ARMOR_destiny()) {
                        int bonus = 0;
                        if (pc.getLevel() >= 85 && pc.getLevel() <= 94) {
                            bonus = (pc.getLevel() - 85) / 2 + 10;
                        } else if (pc.getLevel() >= 95) {
                            bonus = 15;
                        }
                        pc.getResistance().addMr(-bonus);
                        pc.setSHADOW_ARMOR_destiny(false);
                        break;
                    }
                    pc.getResistance().addMr(-5);
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                }
                break;
            case 110:
                cha.getAbility().addAddedDex(-3);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Dexup(pc, 3, 0));
                }
                break;
            case 109:
                cha.getAbility().addAddedStr(-3);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Strup(pc, 3, 0));
                }
                break;
            case 129:
                cha.getResistance().addMr(-10);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                }
                break;
            case 137:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addAddedStr(-1);
                    pc.getAbility().addAddedDex(-1);
                    pc.getAbility().addAddedInt(-1);
                }
                break;
            case 56:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-5);
                }
                break;
            case 50:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addEffectedER(-5);
                }
                break;
            case 147:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
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
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 133:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
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
                    }
                    pc.setAddAttrKind(0);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, 10);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                    break;
                }
                if (cha instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
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
                    }
                    npc.setAddAttrKind(0);
                }
                break;
            case 168:
                cha.getAC().addAc(10);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillIconShield(10, 0));
                }
                break;
            case 151:
                cha.getAC().addAc(4);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillIconShield(4, 0));
                }
                break;
            case 42:
                if (cha.getInventory().checkItem(30001398)) {
                    cha.getAbility().addAddedStr(-6);
                } else {
                    cha.getAbility().addAddedStr(-5);
                }
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Strup(pc, 1, 0));
                }
                break;
            case 26:
                if (cha.getInventory().checkItem(30001398)) {
                    cha.getAbility().addAddedDex(-6);
                } else {
                    cha.getAbility().addAddedDex(-5);
                }
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Dexup(pc, 1, 0));
                }
                break;
            case 148:
                cha.addHitup(-4);
                cha.addDmgup(-2);
                break;
            case 136:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    L1SkillUse.off_icons(pc, skillId);
                }
                break;
            case 155:
            case 177:
            case 178:
            case 179:
                cha.setBraveSpeed(0);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 0, 0));
                    Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_SkillBrave(pc.getId(), 0, 0));
                    pc.setAttackSpeed();
                }
                break;
            case 50011:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-5);
                    pc.addDmgup(-1);
                    pc.addBowHitup(-5);
                    pc.addBowDmgup(-1);
                    pc.addMaxHp(-100);
                    pc.addMaxMp(-50);
                    pc.addHpr(-3);
                    pc.addMpr(-3);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                }
                break;
            case 50013:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-5);
                    pc.addDmgup(-1);
                    pc.addBowHitup(-5);
                    pc.addBowDmgup(-1);
                    pc.addMaxHp(-100);
                    pc.addMaxMp(-50);
                    pc.addHpr(-3);
                    pc.addMpr(-3);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                }
                break;
            case 120384:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addAddedDex(-5);
                    pc.addBowHitup(-7);
                    pc.addBowDmgup(-5);
                    pc.addMaxHp(-100);
                    pc.addMaxMp(-40);
                    pc.addHpr(-10);
                    pc.addMpr(-3);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                }
                break;
            case 106:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDg(-30);
                }
                break;
            case 163:
                cha.addDmgup(-6);
                cha.addHitup(-6);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillIconAura(162, 0));
                }
                break;
            case 5158:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.removeSkillEffect(10071);
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(12, false));
                    pc.getResistance().addcalcPcDefense(-10);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                    L1SkillUse.off_icons(pc, 5158);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(160, pc, pc.getWeapon()), true);
                }
                break;
            case 201:
                pc = (L1PcInstance)cha;
                pc.addDg(-30);
                break;
            case 149:
                cha.addBowHitup(-4);
                break;
            case 156:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addBowHitup(-2);
                    pc.addBowDmgup(-3);
                    pc.sendPackets((ServerBasePacket)new S_SkillIconAura(155, 0));
                }
                break;
            case 166:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addBowDmgup(-5);
                    pc.addBowHitup(-3);
                    pc.sendPackets((ServerBasePacket)new S_SkillIconAura(165, 0));
                }
                break;
            case 55:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    if (pc.isWizard()) {
                        pc.addDmgup(-2);
                        pc.addHitup(-8);
                        break;
                    }
                    pc.getAC().addAc(-10);
                    pc.addDmgup(-2);
                    pc.addHitup(-8);
                }
                break;
            case 190:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    int er = 0;
                    if (pc.getLevel() >= 87) {
                        er = (pc.getLevel() - 87) / 3 * 3 + 3;
                        if (er >= 15)
                            er = 15;
                    }
                    pc.addEffectedER(-er);
                }
                break;
            case 185:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    int ac = 0;
                    int mr = 0;
                    if (pc.getLevel() >= 87) {
                        ac = (pc.getLevel() - 87) / 3 * -1 - 1;
                        mr = (pc.getLevel() - 87) / 3 * 2 + 2;
                        if (ac <= -5)
                            ac = -5;
                        if (mr >= 10)
                            mr = 10;
                    }
                    pc.getAC().addAc(-ac);
                    pc.getResistance().addMr(-mr);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 197:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    int dg = 0;
                    if (pc.getLevel() > 87) {
                        dg = (pc.getLevel() - 87) / 3 * 3 + 3;
                        if (dg >= 15)
                            dg = 15;
                    }
                    pc.addDg(-dg);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 195:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    int a = 0;
                    if (pc.getLevel() >= 87) {
                        a = (pc.getLevel() - 87) / 3;
                        if (a >= 5)
                            a = 5;
                    }
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -a);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -a);
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -a);
                    pc.addHitup(-5);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 204:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(-4);
                    pc.addHitup(-4);
                }
                break;
            case 219:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(-10);
                }
                break;
            case 60:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    L1DollInstance doll = pc.getMagicDoll();
                    pc.delInvis();
                    if (doll != null)
                        for (L1PcInstance tar : L1World.getInstance().getRecognizePlayer((L1Object)doll))
                            doll.onPerceive(tar);
                }
                break;
            case 7320183:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                }
                break;
            case 216:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addAddedStr(-1);
                    pc.getAbility().addAddedDex(-1);
                    pc.getAbility().addAddedCon(-1);
                    pc.getAbility().addAddedInt(-1);
                    pc.getAbility().addAddedWis(-1);
                    pc.resetBaseMr();
                }
                break;
            case 7791:
            case 7792:
            case 7793:
            case 7794:
            case 7795:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    L1SkillId.updateTam(pc, skillId, false);
                    L1SkillId.recycleTam(pc);
                }
                break;
            case 67:
                L1PolyMorph.undoPoly(cha);
                break;
            case 1541:
                pc = (L1PcInstance)cha;
                pc.addDamageReductionByArmor(-5);
                pc.add_item_exp_bonus(-20.0D);
                pc.sendPackets((ServerBasePacket)new S_PacketBox(53, pc, 187, 0));
                pc.setDessertId(0);
                break;
            case 3083:
                pc = (L1PcInstance)cha;
                pc.add_item_exp_bonus(-4.0D);
                break;
            case 228:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(8, false));
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.set(false);
                }
                break;
            case 230:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(9, false));
                    L1SkillUse.off_icons(pc, skillId);
                } else if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.set(false);
                }
                cha.Desperadolevel = 0;
                break;
            case 388:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(12, false));
                    L1SkillUse.off_icons(pc, skillId);
                } else if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.set(false);
                }
                cha.Desperadolevel = 0;
                break;
            case 376:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-pc.getAdvenHp());
                    pc.addMaxMp(-pc.getAdvenMp());
                    pc.setAdvenHp(0);
                    pc.setAdvenMp(0);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                }
                break;
            case 43:
            case 54:
            case 707114:
                cha.setMoveSpeed(0);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillHaste(pc.getId(), 0, 0));
                    pc.broadcastPacket((ServerBasePacket)new S_SkillHaste(pc.getId(), 0, 0));
                }
                break;
            case 150:
                cha.add_missile_critical_rate(-2);
                break;
            case 52:
            case 101:
                cha.setBraveSpeed(0);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 0, 0));
                    pc.broadcastPacket((ServerBasePacket)new S_SkillBrave(pc.getId(), 0, 0));
                }
                break;
            case 186:
                cha.setBraveSpeed(0);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 0, 0));
                    pc.broadcastPacket((ServerBasePacket)new S_SkillBrave(pc.getId(), 1, 0));
                }
                break;
            case 103:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(3, false));
                    L1SkillUse.off_icons(pc, skillId);
                }
                cha.setSleeped(false);
                break;
            case 20:
            case 40:
            case 50009:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_CurseBlind(0));
                }
                break;
            case 33:
            case 7041:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Poison(pc.getId(), 0));
                    pc.broadcastPacket((ServerBasePacket)new S_Poison(pc.getId(), 0));
                    L1SkillId.onFreezeAfterDelay(pc);
                }
                break;
            case 47:
            case 30009:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(5);
                    pc.addHitup(1);
                }
                break;
            case 30008:
            case 30079:
            case 70704:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(6);
                    pc.getAC().addAc(-12);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 217:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addAddedStr(1);
                    pc.getAbility().addAddedDex(1);
                    pc.getAbility().addAddedCon(1);
                    pc.getAbility().addAddedInt(1);
                    pc.getAbility().addAddedWis(1);
                    pc.getAbility().addAddedCha(1);
                    pc.resetBaseMr();
                }
                break;
            case 70705:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Poison(pc.getId(), 0));
                    pc.broadcastPacket((ServerBasePacket)new S_Poison(pc.getId(), 0));
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(4, false));
                    L1SkillId.onFreezeAfterDelay(pc);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.broadcastPacket((ServerBasePacket)new S_Poison(npc.getId(), 0));
                    npc.setParalyzed(false);
                }
                break;
            case 157:
            case 30003:
            case 30004:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Poison(pc.getId(), 0));
                    pc.broadcastPacket((ServerBasePacket)new S_Poison(pc.getId(), 0));
                    if (skillId == 157)
                        pc.sendPackets((ServerBasePacket)new S_Paralysis(4, false));
                    L1SkillId.onFreezeAfterDelay(pc);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.broadcastPacket((ServerBasePacket)new S_Poison(npc.getId(), 0));
                }
                break;
            case 51003:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAC().addAc(5);
                    pc.addDg(10);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, 5);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 51002:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 0), true);
                    pc.setChainSwordExposed(false);
                    pc.setChainSwordStep(0);
                }
                break;
            case 315:
            case 7320184:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                }
                break;
            case 87:
            case 208:
            case 242:
            case 22025:
            case 22026:
            case 22027:
            case 22031:
            case 22055:
            case 30005:
            case 30006:
            case 30010:
            case 30081:
            case 40007:
            case 51006:
            case 100242:
            case 707041:
            case 707054:
            case 707056:
            case 707099:
            case 707113:
            case 707119:
            case 707152:
            case 707159:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                    L1SkillId.onFreezeAfterDelay(pc);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.setParalyzed(false);
                }
                break;
            case 123:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                    L1SkillId.onFreezeAfterDelay(pc);
                    if (pc.isEmpireOverlord()) {
                        pc.addDg(10);
                        pc.setEmpireOverlord(false);
                    }
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.setParalyzed(false);
                }
                break;
            case 5037:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(13, false));
                }
                break;
            case 995049:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                }
                break;
            case 66:
            case 212:
                cha.setSleeped(false);
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(3, false));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                    L1SkillId.onFreezeAfterDelay(pc);
                    L1SkillUse.off_icons(pc, skillId);
                }
                break;
            case 29:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillHaste(pc.getId(), 0, 0));
                    pc.broadcastPacket((ServerBasePacket)new S_SkillHaste(pc.getId(), 0, 0));
                }
                cha.setMoveSpeed(0);
                break;
            case 10071:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(6, false));
                    L1SkillId.onFreezeAfterDelay(pc);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.sendPackets((ServerBasePacket)new S_Paralysis(6, false));
                    npc.set(false);
                }
                break;
            case 20075:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addFire(-30);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 20076:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addEarth(-30);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 20077:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addWind(-30);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 1000:
            case 1016:
            case 20079:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 0, 0));
                    pc.broadcastPacket((ServerBasePacket)new S_SkillBrave(pc.getId(), 0, 0));
                    pc.setBraveSpeed(0);
                }
                break;
            case 1001:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillHaste(pc.getId(), 0, 0));
                    pc.broadcastPacket((ServerBasePacket)new S_SkillHaste(pc.getId(), 0, 0));
                }
                cha.setMoveSpeed(0);
                break;
            case 1003:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillIconBlessOfEva(pc.getId(), 0));
                }
                break;
            case 4516:
            case 4547:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addSp(-2);
                    pc.addMpr(-2);
                    L1SkillUse.off_icons(pc, skillId);
                }
                break;
            case 1005:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(288));
                }
                break;
            case 7893:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-50);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                }
                break;
            case 7894:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxMp(-40);
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                }
                break;
            case 7895:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(-3);
                    pc.addHitup(-3);
                    pc.getAbility().addSp(-3);
                }
                break;
            case 16553:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addSp(-3);
                    pc.addBaseMagicHitUp(-5);
                    pc.getResistance().addcalcPcDefense(-3);
                }
                break;
            case 16552:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addBowDmgup(-3);
                    pc.addBowHitup(-5);
                    pc.getResistance().addcalcPcDefense(-3);
                }
                break;
            case 16551:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgRate(-3);
                    pc.addHitup(-5);
                    pc.getResistance().addcalcPcDefense(-3);
                }
                break;
            case 1006:
                cha.curePoison();
                break;
            case 3000:
            case 3050:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 0, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3001:
            case 3051:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-30);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 1, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3002:
            case 3052:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 2, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3003:
            case 3053:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAC().addAc(1);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 3, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3004:
            case 3054:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxMp(-20);
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 4, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3005:
            case 3055:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 5, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3006:
            case 3056:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addMr(-5);
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 6, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3007:
            case 3057:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_item_exp_bonus(-1.0D);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 7, 0));
                    pc.setDessertId(0);
                }
                break;
            case 3008:
            case 3058:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 16, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3009:
            case 3059:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxMp(-30);
                    pc.addMaxHp(-30);
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 17, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3010:
            case 3060:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAC().addAc(2);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus2(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 18, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3011:
            case 3061:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 19, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3012:
            case 3062:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 20, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3013:
            case 3063:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addMr(-10);
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 21, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3014:
            case 3064:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addSp(-1);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 22, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3015:
            case 3065:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_item_exp_bonus(-5.0D);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 7, 0));
                    pc.setDessertId(0);
                }
                break;
            case 3016:
            case 3066:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addBowHitRate(-2);
                    pc.addBowDmgup(-1);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 45, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3017:
            case 3067:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-50);
                    pc.addMaxMp(-50);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 46, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3018:
            case 3068:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-2);
                    pc.addDmgup(-1);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 47, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3019:
            case 3069:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAC().addAc(3);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus2(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 48, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3020:
            case 3070:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.getResistance().addMr(-15);
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 49, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3021:
            case 3071:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addSp(-2);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 50, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3022:
            case 3072:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-30);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 51, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3023:
            case 3073:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_item_exp_bonus(-9.0D);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 7, 0));
                    pc.setDessertId(0);
                }
                break;
            case 50006:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addAddedCon(-1);
                    pc.getAbility().addAddedDex(-5);
                    pc.getAbility().addAddedStr(-5);
                    pc.addHitRate(-3);
                    pc.getAC().addAc(3);
                }
                break;
            case 50007:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addSp(-1);
                    pc.getAbility().addAddedCon(-3);
                    pc.getAbility().addAddedDex(-5);
                    pc.getAbility().addAddedStr(-5);
                    pc.addHitRate(-5);
                    pc.getAC().addAc(8);
                    pc.add_item_exp_bonus(-20.0D);
                }
                break;
            case 22000:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHpr(-3);
                    pc.addMpr(-3);
                    pc.addDmgup(-2);
                    pc.addHitup(-2);
                    pc.addMaxHp(-50);
                    pc.addMaxMp(-30);
                    pc.getAbility().addSp(-2);
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                }
                break;
            case 22001:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-2);
                    pc.getAbility().addSp(-1);
                    pc.addMaxHp(-50);
                    pc.addMaxMp(-30);
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                }
                break;
            case 22002:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxHp(-50);
                    pc.addMaxMp(-30);
                    pc.getAC().addAc(2);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    if (pc.isInParty())
                        pc.getParty().refreshPartyMemberStatus(pc);
                    pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
                }
                break;
            case 22003:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAC().addAc(1);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 7671:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -5);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7672:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -5);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7673:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_magic_critical_rate(-2);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -5);
                    pc.addEffectedER(-10);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7674:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(-2);
                    pc.addBowDmgup(-2);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -5);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7675:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -5);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -5);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7676:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_magic_critical_rate(-1);
                    pc.addEffectedER(-10);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -5);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -5);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -5);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7678:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_magic_critical_rate(-1);
                    pc.addDmgup(-2);
                    pc.addEffectedER(-10);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -5);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7679:
                pc = (L1PcInstance)cha;
                pc.addDamageReductionByArmor(-5);
                pc.addDmgup(-5);
                pc.addBowDmgup(-5);
                break;
            case 7680:
                pc = (L1PcInstance)cha;
                pc.addDamageReductionByArmor(-3);
                pc.addDmgup(-3);
                pc.addBowDmgup(-3);
                break;
            case 7681:
                pc = (L1PcInstance)cha;
                pc.addDamageReductionByArmor(-2);
                pc.addDmgup(-2);
                pc.addBowDmgup(-2);
                break;
            case 7682:
                pc = (L1PcInstance)cha;
                pc.addDamageReductionByArmor(-1);
                pc.addDmgup(-1);
                pc.addBowDmgup(-1);
                break;
            case 22015:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAC().addAc(2);
                    pc.getResistance().addWater(-50);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 82, 0));
                }
                break;
            case 22016:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHpr(-3);
                    pc.addMpr(-1);
                    pc.getResistance().addWind(50);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 85, 0));
                }
                break;
            case 22060:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-3);
                    pc.addBowHitup(-3);
                    pc.getResistance().addFire(-50);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 88, 0));
                }
                break;
            case 22017:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    S_Liquor packet = new S_Liquor(pc.getId(), 0);
                    pc.sendPackets((ServerBasePacket)packet, false);
                    pc.broadcastPacket((ServerBasePacket)packet);
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(185));
                    pc.setPearl(0);
                }
                break;
            case 3074:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
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
                    pc.add_item_exp_bonus(-2.0D);
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 157, 0));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.setCookingId(0);
                }
                break;
            case 3075:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
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
                    pc.add_item_exp_bonus(-2.0D);
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 158, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3076:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReductionByArmor(-2);
                    pc.getAbility().addSp(-2);
                    pc.addHpr(-2);
                    pc.addMpr(-3);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addWater(-10);
                    pc.getResistance().addFire(-10);
                    pc.getResistance().addWind(-10);
                    pc.getResistance().addEarth(-10);
                    pc.add_item_exp_bonus(-2.0D);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 159, 0));
                    pc.setCookingId(0);
                }
                break;
            case 3000129:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-2);
                    pc.addDmgup(-2);
                    pc.addBowHitup(-2);
                    pc.addBowDmgup(-2);
                    pc.getAbility().addSp(-2);
                    pc.addHpr(-3);
                    pc.addMpr(-4);
                    pc.getResistance().addMr(-15);
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 3000130:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_item_exp_bonus(-5.0D);
                }
                break;
            case 134:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addWeightReduction(-300);
                }
                break;
            case 14:
            case 218:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addWeightReduction(-180);
                }
                break;
            case 3077:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(53, 160, 0));
                    pc.add_item_exp_bonus(-4.0D);
                    pc.setDessertId(0);
                }
                break;
            case 3100:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDmgup(-2);
                    pc.addHitup(-1);
                    pc.addHpr(-2);
                    pc.addMpr(-2);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addWater(-10);
                    pc.getResistance().addFire(-10);
                    pc.getResistance().addWind(-10);
                    pc.getResistance().addEarth(-10);
                    pc.add_item_exp_bonus(-2.0D);
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                }
                break;
            case 3101:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addBowDmgup(-2);
                    pc.addBowHitup(-1);
                    pc.addHpr(-2);
                    pc.addMpr(-2);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addWater(-10);
                    pc.getResistance().addFire(-10);
                    pc.getResistance().addWind(-10);
                    pc.getResistance().addEarth(-10);
                    pc.add_item_exp_bonus(-2.0D);
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                }
                break;
            case 3102:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getAbility().addSp(-2);
                    pc.addHpr(-2);
                    pc.addMpr(-3);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addWater(-10);
                    pc.getResistance().addFire(-10);
                    pc.getResistance().addWind(-10);
                    pc.getResistance().addEarth(-10);
                    pc.add_item_exp_bonus(-2.0D);
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3103:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.getResistance().addcalcPcDefense(-2);
                    pc.add_item_exp_bonus(-4.0D);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -2);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3104:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReduction(-2);
                    pc.addDmgup(-3);
                    pc.addHitup(-2);
                    pc.addHpr(-5);
                    pc.addMpr(-2);
                    pc.addMaxHp(-50);
                    pc.add_item_exp_bonus(-4.0D);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3105:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReduction(-2);
                    pc.addBowDmgup(-3);
                    pc.addBowHitup(-2);
                    pc.addHpr(-3);
                    pc.addMpr(-3);
                    pc.addMaxHp(-25);
                    pc.addMaxMp(-25);
                    pc.add_item_exp_bonus(-4.0D);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3106:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReduction(-2);
                    pc.getAbility().addSp(-3);
                    pc.addHpr(-2);
                    pc.addMpr(-5);
                    pc.addMaxMp(-50);
                    pc.add_item_exp_bonus(-4.0D);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3107:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReduction(-3);
                    pc.add_item_exp_bonus(-6.0D);
                    pc.addMaxHp(-50);
                    pc.getResistance().addcalcPcDefense(-2);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3108:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReduction(-2);
                    pc.addDmgup(-3);
                    pc.addHitup(-2);
                    pc.addHpr(-5);
                    pc.addMpr(-2);
                    pc.addMaxHp(-50);
                    pc.add_item_exp_bonus(-4.0D);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3109:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReduction(-2);
                    pc.addBowDmgup(-3);
                    pc.addBowHitup(-2);
                    pc.addHpr(-3);
                    pc.addMpr(-3);
                    pc.addMaxHp(-25);
                    pc.addMaxMp(-25);
                    pc.add_item_exp_bonus(-4.0D);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3110:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addDamageReduction(-2);
                    pc.getAbility().addSp(-3);
                    pc.addHpr(-2);
                    pc.addMpr(-5);
                    pc.addMaxMp(-50);
                    pc.add_item_exp_bonus(-4.0D);
                    pc.getResistance().addMr(-10);
                    pc.getResistance().addAllNaturalResistance(-10);
                    pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -3);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 3111:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.add_item_exp_bonus(-6.0D);
                    pc.addMaxHp(-50);
                    pc.getResistance().addcalcPcDefense(-2);
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -2);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 7001:
            case 7007:
            case 7035:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_SkillIconWindShackle(pc.getId(), 0));
                }
                break;
            case 60208:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addHitup(-5);
                    pc.addDmgup(-3);
                    pc.getAbility().addAddedStr(-1);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc), true);
                    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
                    noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
                    noti.set_spell_id(60208);
                    noti.set_duration(0);
                    noti.set_off_icon_id(4354);
                    noti.set_end_str_id(2854);
                    noti.set_is_good(true);
                    pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
                }
                break;
            case 60209:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addBowHitup(-5);
                    pc.addBowDmgup(-3);
                    pc.getAbility().addAddedDex(-1);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc), true);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(132, pc.getTotalER()), true);
                    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
                    noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
                    noti.set_spell_id(60209);
                    noti.set_duration(0);
                    noti.set_off_icon_id(4354);
                    noti.set_end_str_id(2854);
                    noti.set_is_good(true);
                    pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
                }
                break;
            case 60210:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addMaxMp(-50);
                    pc.getAbility().addSp(-2);
                    pc.getAbility().addAddedInt(-1);
                    pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
                    pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc), true);
                    SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
                    noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
                    noti.set_spell_id(60210);
                    noti.set_duration(0);
                    noti.set_off_icon_id(4354);
                    noti.set_end_str_id(2854);
                    noti.set_is_good(true);
                    pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
                }
                break;
            case 5001:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, pc.getJudgementPoint());
                    pc.setJudgementPoint(0);
                    SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                }
                break;
            case 5002:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(8, false));
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.set(false);
                }
                break;
            case 199:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(6, false));
                    pc.setShadowstepchaser(false);
                    L1SkillId.onFreezeAfterDelay(pc);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.sendPackets((ServerBasePacket)new S_Paralysis(6, false));
                    npc.set(false);
                }
                break;
            case 395:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(8, false));
                    pc.setShadowstepchaser(false);
                    L1SkillId.onFreezeAfterDelay(pc);
                }
                break;
            case 5003:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(11, false));
                    L1SkillId.onFreezeAfterDelay(pc);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.setParalyzed(false);
                }
                break;
            case 241:
                if (cha instanceof L1PcInstance) {
                    int spellId = 0;
                    L1PcInstance l1PcInstance = (L1PcInstance)cha;
                    if (l1PcInstance.isPrimeCast()) {
                        spellId = 4399;
                        l1PcInstance.addDmgup(-9);
                        l1PcInstance.addBowDmgup(-9);
                        l1PcInstance.addHitup(-9);
                        l1PcInstance.addBowHitup(-9);
                        l1PcInstance.getAbility().addSp(-6);
                        l1PcInstance.addBaseMagicHitUp(-6);
                        l1PcInstance.addMaxHp(-500);
                        if (l1PcInstance.getLevel() >= 85 && l1PcInstance.getLevel() <= 89) {
                            l1PcInstance.set_pvp_defense(-5);
                        } else if (l1PcInstance.getLevel() >= 90 && l1PcInstance.getLevel() <= 94) {
                            l1PcInstance.set_pvp_defense(-10);
                        } else if (l1PcInstance.getLevel() >= 95) {
                            l1PcInstance.set_pvp_defense(-15);
                        }
                        l1PcInstance.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -15);
                        SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(l1PcInstance);
                        l1PcInstance.setIsPrimeCast(false);
                    } else if (l1PcInstance.isPrime_War_Zone()) {
                        spellId = 4399;
                        l1PcInstance.addDmgup(-9);
                        l1PcInstance.addBowDmgup(-9);
                        l1PcInstance.addHitup(-9);
                        l1PcInstance.addBowHitup(-9);
                        l1PcInstance.getAbility().addSp(-6);
                        l1PcInstance.addBaseMagicHitUp(-6);
                        l1PcInstance.addMaxHp(-500);
                        if (l1PcInstance.getLevel() >= 85 && l1PcInstance.getLevel() <= 89) {
                            l1PcInstance.set_pvp_defense(-5);
                        } else if (l1PcInstance.getLevel() >= 90 && l1PcInstance.getLevel() <= 94) {
                            l1PcInstance.set_pvp_defense(-10);
                        } else if (l1PcInstance.getLevel() >= 95) {
                            l1PcInstance.set_pvp_defense(-15);
                        }
                    } else {
                        spellId = 4398;
                        l1PcInstance.addDmgup(-3);
                        l1PcInstance.addBowDmgup(-3);
                        l1PcInstance.addHitup(-3);
                        l1PcInstance.addBowHitup(-3);
                        l1PcInstance.getAbility().addSp(-2);
                        l1PcInstance.addBaseMagicHitUp(-2);
                        if (l1PcInstance.getLevel() >= 85 && l1PcInstance.getLevel() <= 89) {
                            l1PcInstance.set_pvp_defense(-5);
                        } else if (l1PcInstance.getLevel() >= 90 && l1PcInstance.getLevel() <= 94) {
                            l1PcInstance.set_pvp_defense(-10);
                        } else if (l1PcInstance.getLevel() >= 95) {
                            l1PcInstance.set_pvp_defense(-15);
                        }
                    }
                    MJNotiSkillModel model = MJNotiSkillService.service().model(spellId);
                    model.icons(l1PcInstance, 0, false);
                    l1PcInstance.setPrime_War_Zone(false);
                }
                break;
            case 243:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(12, false));
                    if (pc.hasSkillEffect(31)) {
                        pc.removeSkillEffect(31);
                        pc.send_effect(10702);
                        return;
                    }
                    if (!cha.isDead() && cha.getZoneType() != 1) {
                        L1Magic l1Magic = new L1Magic((L1Character)pc._EternitiAttacker, cha);
                        int i = l1Magic.calcMagicDamage(skillId);
                        if (i > Config.MagicAdSetting_Wizard.ETERNITIMAXDMG) {
                            i = Config.MagicAdSetting_Wizard.ETERNITIMAXDMG;
                        } else if (i < Config.MagicAdSetting_Wizard.ETERNITIMINDMG) {
                            i = Config.MagicAdSetting_Wizard.ETERNITIMINDMG;
                        }
                        l1Magic.commit(i, 0);
                    }
                    pc._EternitiAttacker = null;
                    pc.broadcastPacket((ServerBasePacket)new S_PacketBox(194, pc.getId(), 18562, false), true);
                    pc.sendPackets((ServerBasePacket)new S_PacketBox(194, pc.getId(), 18562, false), true);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance || cha instanceof MJCompanionInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.set(false);
                    if (!cha.isDead() && cha.getZoneType() != 1) {
                        L1Magic l1Magic = new L1Magic((L1Character)npc._EternitiAttacker, cha);
                        int i = l1Magic.calcMagicDamage(skillId);
                        if (i > Config.MagicAdSetting_Wizard.ETERNITIMAXDMG) {
                            i = Config.MagicAdSetting_Wizard.ETERNITIMAXDMG;
                        } else if (i < Config.MagicAdSetting_Wizard.ETERNITIMINDMG) {
                            i = Config.MagicAdSetting_Wizard.ETERNITIMINDMG;
                        }
                        l1Magic.commit(i, 0);
                    }
                    npc.broadcastPacket((ServerBasePacket)new S_PacketBox(194, npc.getId(), 18562, false), true);
                    npc._EternitiAttacker = null;
                }
                break;
            case 246:
                pc = (L1PcInstance)cha;
                L1SkillUse.off_icons(pc, skillId);
                pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
                pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
                pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                break;
            case 5027:
                if (cha instanceof L1PcInstance) {
                    pc = (L1PcInstance)cha;
                    pc.sendPackets((ServerBasePacket)new S_Paralysis(5, false));
                    L1SkillUse.off_icons(pc, skillId);
                    break;
                }
                if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance) {
                    L1NpcInstance npc = (L1NpcInstance)cha;
                    npc.setParalyzed(false);
                }
                break;
        }
        if (cha instanceof L1PcInstance) {
            pc = (L1PcInstance)cha;
            if (_skill != null &&
                    _skill.isInvenIconUse())
                SC_SPELL_BUFF_NOTI.sendDatabaseIcon(pc, _skill, 0, false);
            sendStopMessage(pc, skillId);
            pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
        }
    }

    private static void sendStopMessage(L1PcInstance charaPc, int skillid) {
        L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillid);
        if (l1skills == null || charaPc == null)
            return;
        int msgID = l1skills.getSysmsgIdStop();
        if (msgID > 0)
            charaPc.sendPackets((ServerBasePacket)new S_ServerMessage(msgID));
    }
}

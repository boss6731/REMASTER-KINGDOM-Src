package l1j.server.MJPassiveSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_SPELL_PASSIVE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALL_SPELL_PASSIVE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eBoostType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.SkillCheck;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.noti.MJNotiSkillModel;
import l1j.server.server.model.skill.noti.MJNotiSkillService;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;

public class MJPassiveUserLoader {
	public static void load(L1PcInstance pc) {
		MJPassiveLoader loader = MJPassiveLoader.getInstance();
		Selector.exec("select * from passive_user_info where character_id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int passiveId = rs.getInt("passive_id");
					MJPassiveInfo pInfo = loader.fromPassiveId(passiveId);
					if (pInfo == null)
						continue;
//					System.out.println(pInfo.getPassiveId());

					pc.addPassive(pInfo);

					
					if (pInfo.getPassiveId() == MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE.toInt()) {
						L1SkillUse.on_icons_passive(pc, MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE, -1);
					}
					if (pInfo.getPassiveId() == MJPassiveID.IllUSION_LICH_PASSIVE.toInt()) {
						L1SkillUse.on_icons_passive(pc, MJPassiveID.IllUSION_LICH_PASSIVE, -1);
					}
					if (pInfo.getPassiveId() == MJPassiveID.DARK_HORSE.toInt()) {
						L1SkillUse.on_icons_passive(pc, MJPassiveID.DARK_HORSE, -1);
					}
					if (pInfo.getPassiveId() == MJPassiveID.SOLID_CARRIAGE_PASS.toInt()) {
						pc.addEffectedER(10);
					}
					if (pInfo.getPassiveId() == MJPassiveID.PRIDE_PASS.toInt()) {
						if (pc.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
							pc.removeSkillEffect(L1SkillId.ADVANCE_SPIRIT);
						}

						double percent = pc.getLevel() / 4;
						int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
						pc.setMagicBuffHp(addHp);
						pc.addMaxHp(pc.getMagicBuffHp());
						if (pc.isInParty()) {
							// 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					}
					
					if (passiveId == MJPassiveID.DRAGON_SKIN_PASS.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(MJPassiveID.DRAGON_SKIN_PASS.toInt());
						buff.set_on_icon_id(3080);
						buff.set_tooltip_str_id(7853);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
					}
					
					if(passiveId == MJPassiveID.TACTICAL_ADVANCE.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
						buff.set_on_icon_id(10154);
						buff.set_tooltip_str_id(8030);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
						
						pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_SPMR(pc));
					}
					if (pInfo.getPassiveId() == MJPassiveID.ADVANCE_SPIRIT_PA.toInt()) {
						pc.setAdvenHp(pc.getBaseMaxHp() / 5);
						pc.setAdvenMp(pc.getBaseMaxMp() / 5);
						pc.addMaxHp(pc.getAdvenHp());
						pc.addMaxMp(pc.getAdvenMp());
						if (pc.isInParty()) {
							// 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						L1SkillUse.on_icons_passive(pc, MJPassiveID.ADVANCE_SPIRIT_PA, -1);
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					}
					
					if(passiveId == MJPassiveID.SHINING_ARMOR.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(116 - 1);
						buff.set_on_icon_id(9483);
						buff.set_tooltip_str_id(5892);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
						
						pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
					}
					if (pInfo.getPassiveId() == MJPassiveID.MORTAL_BODY.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
						buff.set_on_icon_id(10856);
						buff.set_tooltip_str_id(1361);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					}
					if(passiveId == MJPassiveID.MAJESTY.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(118 - 1);
						buff.set_on_icon_id(9518);
						buff.set_tooltip_str_id(5893);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
					}
					
					if (passiveId == MJPassiveID.GIGANTIC.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(226 - 1);
						buff.set_on_icon_id(6168);
						buff.set_tooltip_str_id(3918);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
						
						double percent = pc.getLevel() / 2;
						int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
						pc.setMagicBuffHp(addHp);
						pc.addMaxHp(pc.getMagicBuffHp());
						if (pc.isInParty()) {
							// TODO 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					}
					
					if(pInfo.getPassiveId() == MJPassiveID.BERSERK.toInt()) {		
						int percent = (int) Math.round(((double) pc.getCurrentHp() / (double) pc.getMaxHp()) * 100);
						if (percent < 50) {
							SC_SPELL_BUFF_NOTI.sendBerserk(pc, true);
							pc.setTitanBerserk(true);
							pc.addSpecialResistance(eKind.ALL, 10);
							pc.getResistance().addPVPweaponTotalDamage(10);
							pc.getResistance().addcalcPcDefense(16);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
							
						} else {
							SC_SPELL_BUFF_NOTI.sendBerserk(pc, false);
							pc.setTitanBerserk(false);
							pc.addSpecialResistance(eKind.ALL, 5);
							pc.getResistance().addPVPweaponTotalDamage(5);
							pc.getResistance().addcalcPcDefense(8);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						}
					}
					
					if(pInfo.getPassiveId() == MJPassiveID.DEMOLITION.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(10090);
						buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
						buff.set_on_icon_id(10660); // 10650
						buff.set_tooltip_str_id(8609);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
					}
					
					if(passiveId == MJPassiveID.TITAN_BEAST.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(10092);
						buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
						buff.set_on_icon_id(10652); // 10650
						buff.set_tooltip_str_id(8610);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
					}
					if (passiveId == MJPassiveID.CONQUEROR.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
						buff.set_spell_id(10653);
						buff.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
						buff.set_on_icon_id(11441);
						buff.set_tooltip_str_id(9420);
						buff.set_is_good(true);
						pc.sendPackets((MJIProtoMessage)buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
					}
					
					if(passiveId == MJPassiveID.SLAYER.toInt()) {
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(10003);
						buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
						buff.set_on_icon_id(10650); // 10650
						buff.set_tooltip_str_id(8611);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
					}
					
					if (passiveId == MJPassiveID.MOEBIUS.toInt()){
						SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
						buff.set_noti_type(eNotiType.NEW);
						buff.set_spell_id(224 - 1);
						buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
						buff.set_on_icon_id(11050);
						buff.set_tooltip_str_id(5550);
						buff.set_is_good(true);
						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
//						int range_reduction = 0;
//						if (pc.getLevel()>=85){
//							range_reduction = ((pc.getLevel() - 85) / 2) + 9;
//						if (range_reduction >= 15){
//								range_reduction = 15;
//							}
//						} else {
//							range_reduction = 9;
//						}
//						pc.getResistance().addcalcPcDefense(range_reduction);
//						pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
					}
				}
			}
		});

		final ArrayList<MJPassiveInfo> passives = pc.getPassives();
		List<Integer> passiveList	= new ArrayList<Integer>();
		SC_ALL_SPELL_PASSIVE_NOTI noti = SC_ALL_SPELL_PASSIVE_NOTI.newInstance();
//		System.out.println(passives);
		if (passives != null){
			for (MJPassiveInfo pInfo : passives) {
				int passiveId = pInfo.getPassiveId();
				passiveList.add(passiveId);
				if (passiveId == MJPassiveID.ARMOR_GUARD.toInt()) {
					
					noti.add_passives(pInfo.getPassiveId(), 10);
				} else if (passiveId == MJPassiveID.BLOODY_SOUL_NEW.toInt()) {
					noti.add_passives(pInfo.getPassiveId());
					SC_ADD_SPELL_PASSIVE_NOTI.send_onoff(pc, pInfo.getPassiveId(), 0, false);
				} else {
					noti.add_passives(pInfo.getPassiveId());
				}
//				SC_ADD_SPELL_PASSIVE_NOTI.send(pc, pInfo.getPassiveId());
			}
//			pc.sendPackets(noti, MJEProtoMessages.SC_ALL_SPELL_PASSIVE_NOTI, true);
			pc.sendPackets(noti, MJEProtoMessages.SC_ALL_SPELL_PASSIVE_NOTI, true);
		}

	}

	public static void store(L1PcInstance pc, MJPassiveInfo pInfo, boolean temp) {
		final int characterId = pc.getId();
		final int passvieId = pInfo.getPassiveId();
		final String passiveName = pInfo.getPassiveName();
		if (!temp) {
			Updator.exec("insert into passive_user_info set character_id=?, passive_id=?, passive_name=?", new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, characterId);
					pstm.setInt(++idx, passvieId);
					pstm.setString(++idx, passiveName);
				}
			});
		}
		
		if (passvieId == MJPassiveID.ARMOR_GUARD.toInt()) {
			SC_ADD_SPELL_PASSIVE_NOTI.send(pc, pInfo.getPassiveId(), 10);
		} else {
			if (passvieId == MJPassiveID.BLOODY_SOUL_NEW.toInt()) {
				SC_ADD_SPELL_PASSIVE_NOTI.send_onoff(pc, pInfo.getPassiveId(), 0, false);
			} else {
				SC_ADD_SPELL_PASSIVE_NOTI.send(pc, pInfo.getPassiveId());
//				SC_ADD_SPELL_PASSIVE_NOTI.send(pc, pInfo.getPassiveId(), 0);
			}
			if (pc.numOfSpellMastery() <= 0) {
				SkillCheck.getInstance().sendAllSkillList(pc);
			}
		}
		
		if (pInfo.getPassiveId() == MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE.toInt()) {
			L1SkillUse.on_icons_passive(pc, MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE, -1);
		}
		if (pInfo.getPassiveId() == MJPassiveID.IllUSION_LICH_PASSIVE.toInt()) {
			L1SkillUse.on_icons_passive(pc, MJPassiveID.IllUSION_LICH_PASSIVE, -1);
		}
		if (pInfo.getPassiveId() == MJPassiveID.DARK_HORSE.toInt()) {
			L1SkillUse.on_icons_passive(pc, MJPassiveID.DARK_HORSE, -1);
		}
		if (pInfo.getPassiveId() == MJPassiveID.SOLID_CARRIAGE_PASS.toInt()) {
			pc.addEffectedER(10);
		}
		if (pInfo.getPassiveId() == MJPassiveID.PRIDE_PASS.toInt()) {
			if (pc.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
				pc.removeSkillEffect(L1SkillId.ADVANCE_SPIRIT);
			}

			double percent = pc.getLevel() / 4;
			int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
			pc.setMagicBuffHp(addHp);
			pc.addMaxHp(pc.getMagicBuffHp());
			if (pc.isInParty()) {
				// 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}
		
		if (pInfo.getPassiveId() == MJPassiveID.DRAGON_SKIN_PASS.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(MJPassiveID.DRAGON_SKIN_PASS.toInt());
			buff.set_on_icon_id(3080);
			buff.set_tooltip_str_id(7853);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		
		if(pInfo.getPassiveId() == MJPassiveID.TACTICAL_ADVANCE.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
			buff.set_on_icon_id(10154);
			buff.set_tooltip_str_id(8030);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_SPMR(pc));
		}
		if(pInfo.getPassiveId() == MJPassiveID.TITAN_BEAST.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(10652);
			buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(8610); // 10650
			buff.set_tooltip_str_id(8613);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		if (pInfo.getPassiveId() == MJPassiveID.CONQUEROR.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
			buff.set_spell_id(10653);
			buff.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(11441);
			buff.set_tooltip_str_id(9420);
			buff.set_is_good(true);
			pc.sendPackets((MJIProtoMessage)buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		
		if (pInfo.getPassiveId() == MJPassiveID.MORTAL_BODY.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
			buff.set_on_icon_id(10856);
			buff.set_tooltip_str_id(1361);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}
		if(pInfo.getPassiveId() == MJPassiveID.BERSERK.toInt()) {		
			int percent = (int) Math.round(((double) pc.getCurrentHp() / (double) pc.getMaxHp()) * 100);
			if (percent < 50) {
				SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
				buff.set_noti_type(eNotiType.NEW);
				buff.set_spell_id(MJPassiveID.BERSERK.toInt());
				buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
				buff.set_on_icon_id(10717);
				buff.set_tooltip_str_id(8614);
				buff.set_is_good(true);
				buff.set_boost_type(eBoostType.HP_UI_CHANGE);
				buff.set_is_passive_spell(true);
				pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
				pc.setTitanBerserk(true);
				
				pc.addSpecialResistance(eKind.ALL, 10);
				pc.getResistance().addPVPweaponTotalDamage(10);
				pc.getResistance().addcalcPcDefense(16);
				
			} else {
				SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
				buff.set_noti_type(eNotiType.NEW);
				buff.set_spell_id(MJPassiveID.BERSERK.toInt());
				buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
				buff.set_on_icon_id(10654);
				buff.set_tooltip_str_id(8613);
				buff.set_is_good(true);
				buff.set_boost_type(eBoostType.BOOST_NONE);
				pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
				pc.setTitanBerserk(false);
				buff.set_is_passive_spell(true);
				pc.addSpecialResistance(eKind.ALL, 5);
				pc.getResistance().addPVPweaponTotalDamage(5);
				pc.getResistance().addcalcPcDefense(8);
			}
		}
		if(pInfo.getPassiveId() == MJPassiveID.DEMOLITION.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(10090);
			buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(10660); // 10650
			buff.set_tooltip_str_id(8609);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		if(pInfo.getPassiveId() == MJPassiveID.SLAYER.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(10003);
			buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(10650); // 10650
			buff.set_tooltip_str_id(8611);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			
			if (pc.getWeapon() != null) {
				int type = pc.getWeapon().getItem().getType1();
				if (type == 11) {
					pc.addAttackDelayRate(10);
				}
			}
		}
		if (pInfo.getPassiveId() == MJPassiveID.SHADOW_STEP_CHASER.toInt()){
			SkillsTable.getInstance().getTemplate(L1SkillId.SHADOW_STEP).setIsThrough(1);
		}
		
		if (pInfo.getPassiveId() == MJPassiveID.MOEBIUS.toInt()){
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(224 - 1);
			buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(11050);
			buff.set_tooltip_str_id(5550);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
//			int range_reduction = 0;
//			if (pc.getLevel()>=85){
//				range_reduction = ((pc.getLevel() - 85) / 2) + 9;
//			if (range_reduction >= 15){
//					range_reduction = 15;
//				}
//			} else {
//				range_reduction = 9;
//			}
//			pc.getResistance().addcalcPcDefense(range_reduction);
//			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}

	}

	public static void store(L1PcInstance pc, boolean temp) {
		final ArrayList<MJPassiveInfo> passives = pc.getPassives();
		if (passives == null || passives.size() <= 0)
			return;

		final int characterId = pc.getId();
		Updator.batch("insert ignore into passive_user_info set character_id=?, passive_id=?, passive_name=?", new BatchHandler() {
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				MJPassiveInfo pInfo = passives.get(callNumber);
				int idx = 0;
				pstm.setInt(++idx, characterId);
				pstm.setInt(++idx, pInfo.getPassiveId());
				pstm.setString(++idx, pInfo.getPassiveName());
			}
		}, passives.size());
	}
	
	public static void spellLost(L1PcInstance pc, int skillid) {
		Updator.exec("delete from passive_user_info where character_id=? and passive_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, skillid);
			}
		});
		pc.delPassive(skillid);	
	}
}

package l1j.server.MJTemplate.MJProto.MainServer_Client;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.Config;
import l1j.server.DeathMatch.DeathMatchSystem;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJInstanceSystem.MJInstanceObject;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Chain.Action.MJTeleportChain;
import l1j.server.MJTemplate.L1Instance.MJEffectTriggerInstance;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ePolymorphAnonymityType;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.datatables.MJNpcMarkTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Light;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SignboardInstance;
import l1j.server.server.model.skill.L1SkillId;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_WORLD_PUT_OBJECT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	// 是否顯示忘記標記...（True：已使用，Pulse：未使用）（重組並良好使用：true）
	public static final boolean IS_PRESENTATION_MARK = false;

	public static String parse_name(L1PcInstance pc) {
		String name = MJString.EmptyString;
		/**
		 * TODO GM은 투명상태 일 경우 클릭이 안되도록!
		 * 앱센터 에서 문제가 발생 될 수도있다. 오브젝트 인식 불가 현상 발생!
		 **/
		/*
		 * if (pc.isGm() && pc.hasSkillEffect(L1SkillId.INVISIBILITY))
		 * return name;
		 * else {
		 */
		if (pc.is_shift_battle()) {
			String server_description = pc.get_server_description();
			MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(server_description) || cInfo == null) {
				name = "身份不明的人";
			} else {
				name = cInfo.to_name_pair();
			}
		} else if (pc.is_shift_transfer()) {
			name = "伺服器遷移中";
		} else if (pc.getRedknightType()) {
			name = "$16458";
		} else if (pc.getMapId() == 13006 || pc.getMapId() == 13005) {
			if (pc.getMapId() == 13005) {
				/*
				 * if (isGm() || pc.isGm()) {
				 * pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
				 * } else {
				 */
				name = pc.getClassName();
				// }
			} else if (pc.getMapId() == 13006) {
				/*
				 * if (isGm() || pc.isGm()) {
				 * pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
				 * } else {
				 */
				for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
					if (player == pc) {

						name = Config.DeathMatch.TEAM_RED_NAME;
					}
				}

				for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
					if (player == pc) {
						name = Config.DeathMatch.TEAM_BLUE_NAME;
					}
				}
			}
			// }

		} else
			name = pc.getName();
		// }
		return name;
	}

	public static String Parse_title(L1PcInstance pc) {
		String title = MJString.EmptyString;
		if (pc.getMapId() == 13006) {
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
				if (player == pc) {
					title = Config.DeathMatch.TEAM_RED_TITLE;
				}
			}
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
				if (player == pc) {
					title = Config.DeathMatch.TEAM_BLUE_TITLE;
				}
			}
		} else if (pc.getMapId() == 13006) {
			title = MJString.EmptyString;
		} else {
			if (pc.hasSkillEffect(L1SkillId.USER_WANTED1)) {
				title = "\fe[緊急通緝中][1級]";
			} else if (pc.hasSkillEffect(L1SkillId.USER_WANTED2)) {
				title = "\fe[緊急通緝中][2級]";
			} else if (pc.hasSkillEffect(L1SkillId.USER_WANTED3)) {
				title = "\fe[緊急通緝中][3級]";
			} else {
				title = pc.getTitle();
			}
		}
		return title;
	}

	public static int Parse_alignment(L1PcInstance pc) {
		int aligment = 0;
		if (pc.getMapId() == 13006) {
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
				if (player == pc) {
					aligment = -32767;
				}
			}
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
				if (player == pc) {
					aligment = 32767;
				}
			}
		} else {
			aligment = pc.getLawful();
		}
		return aligment;
	}

	public static int Parse_team_id(L1PcInstance pc, boolean isclan) {
		int team_id = 0;
		if (pc.getMapId() == 13006) {
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
				if (player == pc) {
					team_id = 12;
				}
			}
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
				if (player == pc) {
					team_id = 13;// 붉은기사단 혈마크 13
				}
			}
		} else if (pc.getMapId() == 13005) {
			team_id = 0;
		} else {
			team_id = pc.get_mark_status();
			if (team_id > 0 && !pc.is_combat_field()) {
				pc.set_instance_status(MJEPcStatus.WORLD);
				team_id = 0;
			} else {
				team_id = writeTeamId(pc, isclan, team_id);
			}
		}
		return team_id;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(L1Character c, int emotion, String shop_title, int weapon_sprite,
			boolean isclan) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance();
		if (c instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) c;
			noti.set_point(((c.getY() << 16) & 0xFFFF0000) | (c.getX() & 0x0000FFFF));
			noti.set_objectnumber(c.getId());
			noti.set_objectsprite(c.getCurrentSpriteId());
			// noti.set_action(c.getStatus());

			if (pc.isDead())
				noti.set_action(c.getStatus());
			else if (pc.isPrivateShop())
				noti.set_action(70);
			else if (pc.isFishing())
				noti.set_action(71);
			else
				noti.set_action(pc.isSpearModeType() ? 121 : pc.getCurrentWeapon());

			noti.set_direction(c.getHeading());
			Light light = c.getLight();
			noti.set_lightRadius(light == null ? 0 : light.getOwnLightSize());
			noti.set_objectcount(1);
			noti.set_alignment(Parse_alignment(pc));

			noti.set_desc(parse_name(pc));
			noti.set_title(Parse_title(pc));
			/*
			 * if (pc.hasSkillEffect(L1SkillId.USER_WANTED1)) {
			 * noti.set_title("\fe[緊急通緝中][1級]");
			 * } else if (pc.hasSkillEffect(L1SkillId.USER_WANTED2)) {
			 * noti.set_title("\fe[緊急通緝中][2級]");
			 * } else if (pc.hasSkillEffect(L1SkillId.USER_WANTED3)) {
			 * noti.set_title("\fe[緊急通緝中][3級]");
			 * } else {
			 * noti.set_title(c.getTitle());
			 * }
			 */
			int speedData = c.isHaste() ? 1 : 0;
			speedData += (c.isSlow() ? -1 : 0);
			noti.set_speeddata(speedData);
			noti.set_emotion(emotion);
			noti.set_forth_gear(pc.isFourgear());

			if (pc.hasSkillEffect(L1SkillId.DEVINE_PROTECTION)) {
				pc.sendPackets(SC_INSTANCE_HP_NOTI.make_stream(pc), true);
			}

			if (pc.isDragonPearl())
				noti.set_drunken(8);
			else
				noti.set_drunken(0);

			noti.set_isghost(false);
			noti.set_isparalyzed(c.isParalyzed());
			noti.set_isinvisible(c.isInvisble());
			noti.set_ispoisoned(c.getPoison() != null);
			noti.set_emblemid(0);
			noti.set_pledgename(null);
			// System.out.println(pc.getClassId());
			// noti.set_user_game_class(pc.getClassId());
			// System.out.println(pc.getType());
			/*
			 * if (pc.getType() == 7) { //전사
			 * noti.set_user_game_class(pc.getClassId());
			 * } else {
			 */
			noti.set_user_game_class(pc.getType());
			// }

			noti.set_team_id(Parse_team_id(pc, isclan));
			noti.set_homeserverno(0);
			noti.set_mastername(null);
			noti.set_altitude(0);
			// noti.set_altitude(-1);
			// if(pc.isInParty() && pc.getParty().isMember(pc))
			noti.set_hitratio(-1);
			noti.set_safelevel(0);
			if (shop_title != null)
				noti.set_shoptitle(shop_title);
			noti.set_weaponsprite(weapon_sprite);
			noti.set_couplestate(0);
			int value1 = 0;
			if (pc.getLevel() >= 80)
				value1 = 11;
			else if (pc.getLevel() >= 55)
				value1 = (pc.getLevel() - 25) / 5;
			else if (pc.getLevel() >= 52)
				value1 = 5;
			else if (pc.getLevel() >= 50)
				value1 = 4;
			else if (pc.getLevel() >= 15)
				value1 = pc.getLevel() / 15;
			// noti.set_boundarylevelindex(value1);
			noti.set_boundarylevelindex(SpriteInformationLoader.levelToIndex(pc.getLevel(), pc.getCurrentSpriteId()));
			if (pc.getClanRank() == L1Clan.군주) {
				if (MJWar.isCastleOffenseClan(pc.getClan())) {
					noti.set_proclamation_siege_mark(true);
				}
			}

			int castle_id = MJCastleWarBusiness.getInstance().NowCastleWarState();
			boolean check = MJCastleWarBusiness.getInstance().isNowWar(castle_id);
			MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
			if (check) {
				if (war.getDefenseClan() == pc.getClan()) {
					noti.set_proclamation_siege_pledge(false);
					noti.set_castle_owner_group(true);
				} else if (MJWar.isCastleOffenseClan(pc.getClan())) {
					noti.set_proclamation_siege_pledge(true);
					noti.set_castle_owner_group(false);
				} else {
					noti.set_proclamation_siege_pledge(false);
					noti.set_castle_owner_group(false);
				}
			}

			SC_SPEED_BONUS_NOTI.Bonus bonus = SC_SPEED_BONUS_NOTI.Bonus.newInstance();
			if (pc.getMoveDelayRate() != 0) {
				SC_SPEED_BONUS_NOTI.Bonus.Value value = SC_SPEED_BONUS_NOTI.Bonus.Value.newInstance();
				value.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED);
				value.set_value((int) pc.getMoveDelayRate());
				bonus.add_speedBonus(value);
			}
			if (pc.getAttackDelayRate() != 0) {
				SC_SPEED_BONUS_NOTI.Bonus.Value value = SC_SPEED_BONUS_NOTI.Bonus.Value.newInstance();
				value.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED);
				value.set_value((int) pc.getAttackDelayRate());
				bonus.add_speedBonus(value);
			}
			if (pc.getMagicDelayRate() != 0) {
				SC_SPEED_BONUS_NOTI.Bonus.Value value = SC_SPEED_BONUS_NOTI.Bonus.Value.newInstance();
				value.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.SPELL_SPEED);
				value.set_value((int) pc.getMagicDelayRate());
				bonus.add_speedBonus(value);
			}
			noti.set_speed_bonus(bonus);

			/*
			 * noti.set_is_apc(true);
			 * noti.set_category(10);
			 * noti.set_is_excavated(true);
			 */
		}
		return noti;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(L1Character c, int emotion, String shop_title, int weapon_sprite, int alig) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance();
		if (c instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) c;
			noti.set_point(((c.getY() << 16) & 0xFFFF0000) | (c.getX() & 0x0000FFFF));
			noti.set_objectnumber(c.getId());
			noti.set_objectsprite(c.getCurrentSpriteId());
			// noti.set_action(c.getStatus());

			if (pc.isDead())
				noti.set_action(c.getStatus());
			else if (pc.isPrivateShop())
				noti.set_action(70);
			else if (pc.isFishing())
				noti.set_action(71);
			else
				noti.set_action(pc.isSpearModeType() ? 121 : pc.getCurrentWeapon());
			
			noti.set_direction(c.getHeading());
			Light light = c.getLight();
			noti.set_lightRadius(light == null ? 0 : light.getOwnLightSize());
			noti.set_objectcount(1);
			noti.set_alignment(alig);
			
			noti.set_desc(parse_name(pc));
			if (alig > 0) {	
				noti.set_title("\aj週六_最終");
			} else if (alig <= 0) {
				noti.set_title("\ag週六_最終");
			}
			int speedData = c.isHaste() ? 1 : 0;
			speedData += (c.isSlow() ? -1 : 0);
			noti.set_speeddata(speedData);
			noti.set_emotion(emotion);
			noti.set_forth_gear(pc.isFourgear());
			
			if(pc.hasSkillEffect(L1SkillId.DEVINE_PROTECTION)){
				pc.sendPackets(SC_INSTANCE_HP_NOTI.make_stream(pc), true);
			}
			
			if (pc.isDragonPearl())
				noti.set_drunken(8);
			else
				noti.set_drunken(0);
			
			noti.set_isghost(false);
			noti.set_isparalyzed(c.isParalyzed());
			noti.set_isinvisible(c.isInvisble());
			noti.set_ispoisoned(c.getPoison() != null);
			noti.set_emblemid(0);
			noti.set_pledgename(null);
//			System.out.println(pc.getClassId());
//			noti.set_user_game_class(pc.getClassId());
//			System.out.println(pc.getType());
			noti.set_user_game_class(pc.getType());
			

			noti.set_team_id(Parse_team_id(pc, true));
			noti.set_homeserverno(0);	
			noti.set_mastername(null);
			noti.set_altitude(0);
//			noti.set_altitude(-1);
//			if(pc.isInParty() && pc.getParty().isMember(pc))
			noti.set_hitratio(-1);
			noti.set_safelevel(0);
			if (shop_title != null)
				noti.set_shoptitle(shop_title);
			noti.set_weaponsprite(weapon_sprite);
			noti.set_couplestate(0);
			int value1 = 0;
			if(pc.getLevel() >= 80)		value1 = 11;
			else if(pc.getLevel() >= 55)value1 = (pc.getLevel() - 25) / 5;
			else if(pc.getLevel() >= 52)value1 = 5;
			else if(pc.getLevel() >= 50)value1 = 4;
			else if(pc.getLevel() >= 15)value1 = pc.getLevel() / 15;
//			noti.set_boundarylevelindex(value1);
			noti.set_boundarylevelindex(SpriteInformationLoader.levelToIndex(pc.getLevel(), pc.getCurrentSpriteId()));
			if (pc.getClanRank() == L1Clan.王族) {
				if (MJWar.isCastleOffenseClan(pc.getClan())) {
					noti.set_proclamation_siege_mark(true);
				}
			}
			
			int castle_id = MJCastleWarBusiness.getInstance().NowCastleWarState();
			boolean check = MJCastleWarBusiness.getInstance().isNowWar(castle_id);
			MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
			if(check){
				if(war.getDefenseClan() == pc.getClan()) {
					noti.set_proclamation_siege_pledge(false);
					noti.set_castle_owner_group(true);
				} else if (MJWar.isCastleOffenseClan(pc.getClan())) {
					noti.set_proclamation_siege_pledge(true);
					noti.set_castle_owner_group(false);
				}else{
					noti.set_proclamation_siege_pledge(false);
					noti.set_castle_owner_group(false);
				}
			}
			
			SC_SPEED_BONUS_NOTI.Bonus bonus = SC_SPEED_BONUS_NOTI.Bonus.newInstance();
			if (pc.getMoveDelayRate() != 0) {
				SC_SPEED_BONUS_NOTI.Bonus.Value value = SC_SPEED_BONUS_NOTI.Bonus.Value.newInstance();
				value.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.MOVE_SPEED);
				value.set_value((int) pc.getMoveDelayRate());
				bonus.add_speedBonus(value);
			}
			if (pc.getAttackDelayRate() != 0) {
				SC_SPEED_BONUS_NOTI.Bonus.Value value = SC_SPEED_BONUS_NOTI.Bonus.Value.newInstance();
				value.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.ATTACK_SPEED);
				value.set_value((int) pc.getAttackDelayRate());
				bonus.add_speedBonus(value);
			}
			if (pc.getMagicDelayRate() != 0) {
				SC_SPEED_BONUS_NOTI.Bonus.Value value = SC_SPEED_BONUS_NOTI.Bonus.Value.newInstance();
				value.set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.SPELL_SPEED);
				value.set_value((int) pc.getMagicDelayRate());
				bonus.add_speedBonus(value);
			}
			noti.set_speed_bonus(bonus);
			
			/*noti.set_is_apc(true);
			noti.set_category(10);
			noti.set_is_excavated(true);*/
		}
		return noti;
	}

	private static int writeTeamId(L1PcInstance pc, boolean isClan, int mark) {
		MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
		if (cInfo != null)
			return cInfo.team_info.team_id;

		int new_mark = mark;
		MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
		if (instobj != null) {
			// new_mark = instobj.getMarkStatus(pc);
		} else if (IS_PRESENTATION_MARK) {
			if (pc.getMapId() >= 1708 && pc.getMapId() <= 1710 || pc.getMapId() >= 12852 && pc.getMapId() <= 12862
					|| pc.getMapId() == 15871 || pc.getMapId() == 15881 || pc.getMapId() == 15891
					|| pc.getMapId() == 10500 || pc.getMapId() == 10502) {// 다른사람이 보여지는 기준
				new_mark = MJTeleportChain.select_mark_id(pc, isClan);
			} else {
				new_mark = 0;
			}
		} else {
			if (pc.getRedKnightClanId() != 0) {// 붉은기사단
				new_mark = pc.getEmblemId();
			} else {
				if (pc.getMapId() == 15482) {
					new_mark = pc.getClanid();
				} else {
					new_mark = 0;
				}
			}
		}
		return new_mark;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(L1PcInstance pc, String name, boolean isclan) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(pc, pc.getBraveSpeed(), pc.to_shop_title(), 0, isclan);
		noti.set_isghost(pc.isGhost());
		noti.set_isuser(true);
		noti.set_isinvisible(pc.isInvisble() || pc.isGmInvis());

		noti.set_desc(parse_name(pc));
		if (pc.getMapId() == 13005) {
			noti.set_emblemid(0);
			noti.set_pledgename(null);
		} else if (pc.getMapId() == 13006) {
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamRedList()) {
				noti.set_emblemid(332027080);
				noti.set_pledgename("紅隊");
			}
			for (L1PcInstance player : DeathMatchSystem.getInstance().getTeamBlueList()) {
				noti.set_emblemid(332027079);
				noti.set_pledgename("藍隊");
			}
		} else {
			L1Clan clan = pc.getClan();
			if (clan != null) {
				noti.set_emblemid(clan.getEmblemId());
				noti.set_pledgename(clan.getClanName());
			}
		}
		if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
			if (pc.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
				noti.set_lightRadius(0);
			}
			noti.set_emblemid(0);
		}
		if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
			noti.set_emblemid(0);
		}

		return noti;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(L1PcInstance pc, String name, int alig) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(pc, pc.getBraveSpeed(), pc.to_shop_title(), 0, alig);
		noti.set_isghost(pc.isGhost());
		noti.set_isuser(true);
		noti.set_isinvisible(pc.isInvisble() || pc.isGmInvis());

		noti.set_desc(parse_name(pc));

		return noti;
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(pc, pc.getName(), false);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc, String name) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(pc, name, false);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc, String name, boolean isclan) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(pc, name, isclan);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc, String name, int alig) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(pc, name, alig);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI RedKnight_newInstance(L1PcInstance pc) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(pc, pc.getBraveSpeed(), pc.to_shop_title(), 0, false);
		noti.set_isghost(pc.isGhost());
		noti.set_isuser(true);
		noti.set_isinvisible(pc.isInvisble() || pc.isGmInvis());
		if (pc.getAI().getBotType() == MJBotType.PROTECTOR)
			noti.set_desc(pc.getName());
		else
			noti.set_desc("$16458");
		noti.set_emblemid(1);// 1
		noti.set_pledgename("紅色騎士團");// 紅騎士
		noti.set_team_id(13);// 붉은기사단 혈마크 13
		int value = 0;
		if (pc.getLevel() >= 80)
			value = 11;
		else if (pc.getLevel() >= 55)
			value = (pc.getLevel() - 25) / 5;
		else if (pc.getLevel() >= 52)
			value = 5;
		else if (pc.getLevel() >= 50)
			value = 4;
		else if (pc.getLevel() >= 15)
			value = pc.getLevel() / 15;
		// noti.set_boundarylevelindex(value);

		noti.set_boundarylevelindex(SpriteInformationLoader.levelToIndex(pc.getLevel(), pc.getCurrentSpriteId()));

		int mark_status = pc.get_mark_status();
		if (mark_status > 0)
			noti.set_team_id(mark_status);

		return noti;
	}

	public static ProtoOutputStream redknight_make_stream(L1PcInstance pc) {
		SC_WORLD_PUT_OBJECT_NOTI noti = RedKnight_newInstance(pc);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream(L1NpcInstance npc) {
		SC_WORLD_PUT_OBJECT_NOTI noti = newInstance(npc);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI);
		noti.dispose();
		return stream;
	}

	private static int get_presentation_npc_emblem(L1NpcInstance npc) {
		if (npc.getNpcId() == 6200008) {
			for (L1Clan checkClan : L1World.getInstance().getAllClans()) {
				/** 1. 肯特 2. 獸人 3. 文城 4. 基蘭 5. 海涅 6. 矮人 7. 亞丁 8 迪亞德 **/
				if (checkClan.getCastleId() == 4) {
					return checkClan.getEmblemId();
				}
			}
		}
		return 0;
	}

	private static String get_presentation_npc_title(L1NpcInstance npc) {
		if ((npc instanceof L1FieldObjectInstance)) {
			L1NpcTalkData talkdata = NPCTalkDataTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
			return talkdata != null ? talkdata.getNormalAction() : "";
		}
		if ((npc instanceof L1SignboardInstance))
			return npc.getNameId();
		if ((npc.getTitle() != null) && (!npc.getTitle().isEmpty()))
			return npc.getTitle();
		return null;
	}

	private static String get_presentation_npc_desc(L1NpcInstance npc) {
		if (npc.getNpcId() == 6200008) {
			String clan_name = ((L1MerchantInstance) npc).getClanname();
			return l1j.server.MJTemplate.MJString.isNullOrEmpty(clan_name) ? null : clan_name;
		} else if ((npc instanceof L1SignboardInstance))
			return null;
		else if ((npc.getNameId() != null) && (!npc.getNameId().isEmpty()))
			return npc.getNameId();
		return null;
	}

	private static int get_presentation_npc_action(L1NpcInstance npc) {
		int npcid = npc.getNpcId();
		int sprid = npc.getCurrentSpriteId();
		if (npc.getShopName() != null)
			return EActionCodes.act_shop.toInt();

		// TODO 없는 액션을 강제로 실행되게 해준다(활,오궁)(걷는 액션인듯)
		if (sprid == 111 || sprid == 94)
			return 4;
		if (sprid == 816 || sprid == 57)
			return 20;
		if (sprid == 110 || sprid == 51 || npcid == 148)
			return 24;

		if (npc.getNpcTemplate().is_doppel()) {
			if (sprid == 727 || sprid == 985 || sprid == 98 || sprid == 6632 || sprid == 6634 || sprid == 6636
					|| sprid == 6638) {
				return 4;
			}
		}
		return npc.getStatus();
	}

	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(MJEffectTriggerInstance trigger) {
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance();
		noti.set_point(trigger.get_long_location_reverse());
		noti.set_objectnumber(trigger.getId());
		noti.set_objectsprite(trigger.get_sprite());
		noti.set_direction(1);
		noti.set_action(trigger.get_action());
		noti.set_lightRadius(0);
		noti.set_objectcount(0);
		noti.set_alignment(0);
		noti.set_desc("");
		noti.set_title("");
		noti.set_speeddata(1);
		noti.set_emotion(1);
		noti.set_drunken(0);
		noti.set_isghost(false);
		noti.set_isparalyzed(false);
		noti.set_isuser(false);
		noti.set_isinvisible(false);
		noti.set_ispoisoned(false);
		noti.set_emblemid(0);
		noti.set_pledgename("");
		noti.set_mastername("");
		noti.set_altitude(0);
		noti.set_hitratio(-1);
		noti.set_safelevel(1);
		noti.set_weaponsprite(-1);
		noti.set_couplestate(0);
		noti.set_boundarylevelindex(1);
		noti.set_speed_value_flag(1);
		noti.set_second_speed_type(1);
		return noti;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI new_namechat_isntance(L1NpcInstance npc, String name, boolean is_ghost) {
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance();
		noti.set_point(npc.getLongLocationReverse());
		noti.set_objectnumber(npc.getId());
		noti.set_objectsprite(npc.getCurrentSpriteId());
		noti.set_action(npc.getStatus());
		noti.set_direction(npc.getHeading());
		noti.set_lightRadius(npc.getNpcTemplate().getLightSize());
		noti.set_objectcount(1);
		noti.set_alignment(3000);
		noti.set_desc(name);
		noti.set_title("");
		noti.set_speeddata(1);
		noti.set_emotion(1);
		noti.set_drunken(0);
		noti.set_isghost(is_ghost);
		noti.set_isparalyzed(false);
		noti.set_isuser(true);
		noti.set_isinvisible(false);
		noti.set_ispoisoned(false);
		noti.set_emblemid(0);
		noti.set_pledgename(null);
		noti.set_mastername("");
		noti.set_altitude(0);
		// noti.set_altitude(5);
		noti.set_hitratio(-1);
		noti.set_shoptitle("");
		noti.set_safelevel(1);
		noti.set_weaponsprite(1);
		noti.set_couplestate(0);

		int value = 0;
		if (npc.getLevel() >= 80)
			value = 11;
		else if (npc.getLevel() >= 55)
			value = (npc.getLevel() - 25) / 5;
		else if (npc.getLevel() >= 52)
			value = 5;
		else if (npc.getLevel() >= 50)
			value = 4;
		else if (npc.getLevel() >= 15)
			value = npc.getLevel() / 15;
		noti.set_boundarylevelindex(value);

		// noti.set_boundarylevelindex(0);
		return noti;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI newInstance(L1NpcInstance npc) {
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance();
		boolean is_companion = npc instanceof MJCompanionInstance;
		MJCompanionInstance companion_instance = is_companion ? (MJCompanionInstance) npc : null;
		noti.set_point(npc.getLongLocationReverse()); // 0x08
		noti.set_objectnumber(npc.getId()); // 0x10
		noti.set_objectsprite(npc.getCurrentSpriteId()); // 0x18

		noti.set_action(get_presentation_npc_action(npc)); // 0x20
		noti.set_direction(npc.getHeading()); // 0x28

		noti.set_lightRadius(npc.getNpcTemplate().getLightSize()); // 0x30
		noti.set_objectcount(1); // 0x38
		noti.set_alignment(npc.getTempLawful()); // 0x40
		noti.set_desc(is_companion ? companion_instance.getName() : get_presentation_npc_desc(npc)); // 0x4a
		noti.set_title(get_presentation_npc_title(npc)); // 0x52
		noti.set_speeddata(npc.getMoveSpeed()); // 0x58
		noti.set_emotion(npc.getBraveSpeed()); // 0x60
		noti.set_drunken(0); // 0x68
		noti.set_isghost(false); // 0x70
		noti.set_isparalyzed(npc.getParalysis() != null); // 0x78
		// noti.set_isuser(false);
		noti.set_isuser(npc.getShopName() != null || !npc.isPassObject()); // 0x0180
		noti.set_isinvisible(npc.isInvisble()); // 0x0188
		noti.set_ispoisoned(npc.getPoison() != null); // 0x0190
		noti.set_emblemid(get_presentation_npc_emblem(npc)); // 0x0198
		noti.set_pledgename(null); // 0x01a2
		if (!is_companion)
			noti.set_mastername(npc.getMaster() != null ? npc.getMaster().getName() : null); // 0x01aa
		else
			noti.set_mastername(companion_instance.get_master_name()); // 0x01aa
		noti.set_altitude(0); // 0x01b0
		// noti.set_altitude(5);
		// noti.set_hitratio(is_companion ? (int)npc.getCurrentHpPercent() : -1);
		noti.set_hitratio(-1); // 0x01b8
		noti.set_manaratio(-1); // 0x01f0
		noti.set_weaponsprite(-1); // 0X01D0
		noti.set_safelevel(npc.getLevel()); // 0x01c0
		if (npc.getShopName() != null) {
			noti.set_shoptitle(npc.getShopName());
		}
		noti.set_couplestate(0); // 0x01d8

		boolean occupyPortal = npc.getId() == 800817 || npc.getId() == 800818;
		int value = 0;
		if (npc.getLevel() >= 80) {
			value = 11;
		} else if (npc.getLevel() >= 55) {
			value = (npc.getLevel() - 25) / 5;
		} else if (npc.getLevel() >= 52) {
			value = 5;
		} else if (npc.getLevel() >= 50) {
			value = 4;
		} else if (npc.getLevel() >= 15) {
			value = npc.getLevel() / 15;
		}
		noti.set_boundarylevelindex(occupyPortal ? 0x0f : value); // 0x01e0

		noti.set_weakelemental(npc.getNpcTemplate().get_weakAttr());
		// noti.set_manaratio((int) npc.getCurrentMpPercent());
		noti.set_homeserverno(0); // 0x0280
		noti.set_botindex(0);
		noti.set_team_id(MJNpcMarkTable.getInstance().get(npc.getNpcId()));
		noti.set_dialog_radius(occupyPortal ? 0x0b : npc instanceof L1MerchantInstance ? 0x0f : 0x09); // 0x0290
		// noti.set_second_speed_type(0);//1031주석

		if (is_companion) {
			noti.set_npc_class_id(companion_instance.get_class_info().get_class_id());
			Companion companion = Companion.newInstance();
			companion.set_attack_delay_reduce(companion_instance.get_attackdelay_reduce());
			companion.set_move_delay_reduce(companion_instance.get_movedelay_reduce());
			companion.set_pvp_dmg_ratio(companion_instance.get_pvp_dmg_ratio());
			noti.set_companion(companion);
		}

		if (!(npc instanceof L1MerchantInstance || npc instanceof L1NpcShopInstance)) {
			noti.set_speed_value_flag(0); // 0x0298
			noti.set_npc_class_id(npc.getNpcTemplate().get_npc_class_id());
			if (npc.getExplosionTime() > 0) {
				noti.set_explosion_remain_time_ms((int) npc.getExplosionTime());
			}
			if (npc instanceof L1DollInstance) {
				L1DollInstance doll = (L1DollInstance) npc;
				L1PcInstance pc = (L1PcInstance) doll.getMaster();
				if (doll != null) {
					L1ItemInstance item = pc.getInventory().getItem(doll.getItemObjId());
					if (item != null && item.get_Doll_Bonus_Value() > 0)
						noti.set_potential_grade(item.get_Doll_Bonus_Level() + 1);
				}
			} else {
				noti.set_potential_grade(0);
			}
		}

		/*
		 * if (!(npc instanceof L1MerchantInstance || npc instanceof
		 * L1NpcShopInstance)){
		 * noti.set_npc_class_id(npc.getNpcTemplate().get_npc_class_id());
		 * 
		 * }
		 * if (npc instanceof L1DollInstance) {
		 * L1DollInstance doll = (L1DollInstance) npc;
		 * L1PcInstance pc = (L1PcInstance) doll.getMaster();
		 * if (doll != null) {
		 * L1ItemInstance item = pc.getInventory().getItem(doll.getItemObjId());
		 * if (item != null && item.get_Doll_Bonus_Value() > 0)
		 * noti.set_potential_grade(item.get_Doll_Bonus_Level() + 1);
		 * }
		 * }
		 * if(!(npc instanceof L1MerchantInstance || npc instanceof L1NpcShopInstance)){
		 * // noti.set_npc_class_id(npc.getNpcClassId()); //0x02b8
		 * noti.set_npc_class_id(0); //0x02b8
		 * }
		 */
		// eNone(0),
		// eRandom(1),
		// eSpecialChar(2),
		// eNormal(3),
		// noti.set_anonymity_name("메티스");
		// noti.set_anonymity_name(noti.get_anonymity_name());
		// noti.set_anonymity_type(ePolymorphAnonymityType.fromInt(2));
		// System.out.println(noti.get_anonymity_name());
		// System.out.println(ePolymorphAnonymityType.eSpecialChar);

		// noti.set_potential_grade(100);
		// noti.set_potential_grade(noti.get_potential_grade());
		// System.out.println(noti.get_potential_grade());

		return noti;
	}

	public static SC_WORLD_PUT_OBJECT_NOTI newInstance() {
		return new SC_WORLD_PUT_OBJECT_NOTI();
	}

	private int _point;
	private int _objectnumber;
	private int _objectsprite;
	private int _action;
	private int _direction;
	private int _lightRadius;
	private int _objectcount;
	private int _alignment;
	private String _desc;
	private String _title;
	private int _speeddata;
	private int _emotion;
	private int _drunken;
	private boolean _isghost;
	private boolean _isparalyzed;
	private boolean _isuser;
	private boolean _isinvisible;
	private boolean _ispoisoned;
	private int _emblemid;
	private String _pledgename;
	private String _mastername;
	private int _altitude;
	private int _hitratio;
	private int _safelevel;
	private String _shoptitle;
	private int _weaponsprite;
	private int _couplestate;
	private int _boundarylevelindex;
	private int _weakelemental;
	private int _manaratio;
	private int _botindex;
	private int _homeserverno;
	private int _team_id;
	private int _dialog_radius;
	private int _speed_value_flag;
	private int _second_speed_type;
	private int _explosion_remain_time_ms;
	private boolean _proclamation_siege_mark;
	private int _npc_class_id;
	private SC_WORLD_PUT_OBJECT_NOTI.Companion _companion;
	private String _force_haction;
	private boolean _is_portal;
	private boolean _proclamation_siege_pledge;
	private SC_WORLD_PUT_OBJECT_NOTI.DropItemInfo _drop_info;
	private boolean _castle_owner_group;
	private int _user_game_class;
	private String _anonymity_name;
	private ePolymorphAnonymityType _anonymity_type;
	private int _potential_grade;
	private boolean _forth_gear;
	private SC_SPEED_BONUS_NOTI.Bonus _speed_bonus;
	private SC_SPEED_BONUS_NOTI.Bonus _speed_change_rate;
	private boolean _is_apc;
	private int _category;
	private boolean _is_excavated;
	private int _apc_pledge_icon;
	private int _above_head_mark_id;
	private int _fix_frame_level;
	private int _polymorph_effect_id;
	private int _invisible_level;

	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private long _bit;

	private SC_WORLD_PUT_OBJECT_NOTI() {
		set_is_apc(false);
		set_is_excavated(false);
	}

	public int get_point() {
		return _point;
	}

	public void set_point(int val) {
		_bit |= 0x1L;
		_point = val;
	}

	public boolean has_point() {
		return (_bit & 0x1L) == 0x1L;
	}

	public int get_objectnumber() {
		return _objectnumber;
	}

	public void set_objectnumber(int val) {
		_bit |= 0x2L;
		_objectnumber = val;
	}

	public boolean has_objectnumber() {
		return (_bit & 0x2L) == 0x2L;
	}

	public int get_objectsprite() {
		return _objectsprite;
	}

	public void set_objectsprite(int val) {
		_bit |= 0x4L;
		_objectsprite = val;
	}

	public boolean has_objectsprite() {
		return (_bit & 0x4L) == 0x4L;
	}

	public int get_action() {
		return _action;
	}

	public void set_action(int val) {
		_bit |= 0x8L;
		_action = val;
	}

	public boolean has_action() {
		return (_bit & 0x8L) == 0x8L;
	}

	public int get_direction() {
		return _direction;
	}

	public void set_direction(int val) {
		_bit |= 0x10L;
		_direction = val;
	}

	public boolean has_direction() {
		return (_bit & 0x10L) == 0x10L;
	}

	public int get_lightRadius() {
		return _lightRadius;
	}

	public void set_lightRadius(int val) {
		_bit |= 0x20L;
		_lightRadius = val;
	}

	public boolean has_lightRadius() {
		return (_bit & 0x20L) == 0x20L;
	}

	public int get_objectcount() {
		return _objectcount;
	}

	public void set_objectcount(int val) {
		_bit |= 0x40L;
		_objectcount = val;
	}

	public boolean has_objectcount() {
		return (_bit & 0x40L) == 0x40L;
	}

	public int get_alignment() {
		return _alignment;
	}

	public void set_alignment(int val) {
		_bit |= 0x80L;
		_alignment = val;
	}

	public boolean has_alignment() {
		return (_bit & 0x80L) == 0x80L;
	}

	public String get_desc() {
		return _desc;
	}

	public void set_desc(String val) {
		_bit |= 0x100L;
		_desc = val;
	}

	public boolean has_desc() {
		return (_bit & 0x100L) == 0x100L;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String val) {
		_bit |= 0x200L;
		_title = val;
	}

	public boolean has_title() {
		return (_bit & 0x200L) == 0x200L;
	}

	public int get_speeddata() {
		return _speeddata;
	}

	public void set_speeddata(int val) {
		_bit |= 0x400L;
		_speeddata = val;
	}

	public boolean has_speeddata() {
		return (_bit & 0x400L) == 0x400L;
	}

	public int get_emotion() {
		return _emotion;
	}

	public void set_emotion(int val) {
		_bit |= 0x800L;
		_emotion = val;
	}

	public boolean has_emotion() {
		return (_bit & 0x800L) == 0x800L;
	}

	public int get_drunken() {
		return _drunken;
	}

	public void set_drunken(int val) {
		_bit |= 0x1000L;
		_drunken = val;
	}

	public boolean has_drunken() {
		return (_bit & 0x1000L) == 0x1000L;
	}

	public boolean get_isghost() {
		return _isghost;
	}

	public void set_isghost(boolean val) {
		_bit |= 0x2000L;
		_isghost = val;
	}

	public boolean has_isghost() {
		return (_bit & 0x2000L) == 0x2000L;
	}

	public boolean get_isparalyzed() {
		return _isparalyzed;
	}

	public void set_isparalyzed(boolean val) {
		_bit |= 0x4000L;
		_isparalyzed = val;
	}

	public boolean has_isparalyzed() {
		return (_bit & 0x4000L) == 0x4000L;
	}

	public boolean get_isuser() {
		return _isuser;
	}

	public void set_isuser(boolean val) {
		_bit |= 0x8000L;
		_isuser = val;
	}

	public boolean has_isuser() {
		return (_bit & 0x8000L) == 0x8000L;
	}

	public boolean get_isinvisible() {
		return _isinvisible;
	}

	public void set_isinvisible(boolean val) {
		_bit |= 0x10000L;
		_isinvisible = val;
	}

	public boolean has_isinvisible() {
		return (_bit & 0x10000L) == 0x10000L;
	}

	public boolean get_ispoisoned() {
		return _ispoisoned;
	}

	public void set_ispoisoned(boolean val) {
		_bit |= 0x20000L;
		_ispoisoned = val;
	}

	public boolean has_ispoisoned() {
		return (_bit & 0x20000L) == 0x20000L;
	}

	public int get_emblemid() {
		return _emblemid;
	}

	public void set_emblemid(int val) {
		_bit |= 0x40000L;
		_emblemid = val;
	}

	public boolean has_emblemid() {
		return (_bit & 0x40000L) == 0x40000L;
	}

	public String get_pledgename() {
		return _pledgename;
	}

	public void set_pledgename(String val) {
		_bit |= 0x80000L;
		_pledgename = val;
	}

	public boolean has_pledgename() {
		return (_bit & 0x80000L) == 0x80000L;
	}

	public String get_mastername() {
		return _mastername;
	}

	public void set_mastername(String val) {
		_bit |= 0x100000L;
		_mastername = val;
	}

	public boolean has_mastername() {
		return (_bit & 0x100000L) == 0x100000L;
	}

	public int get_altitude() {
		return _altitude;
	}

	public void set_altitude(int val) {
		_bit |= 0x200000L;
		_altitude = val;
	}

	public boolean has_altitude() {
		return (_bit & 0x200000L) == 0x200000L;
	}

	public int get_hitratio() {
		return _hitratio;
	}

	public void set_hitratio(int val) {
		_bit |= 0x400000L;
		_hitratio = val;
	}

	public boolean has_hitratio() {
		return (_bit & 0x400000L) == 0x400000L;
	}

	public int get_safelevel() {
		return _safelevel;
	}

	public void set_safelevel(int val) {
		_bit |= 0x800000L;
		_safelevel = val;
	}

	public boolean has_safelevel() {
		return (_bit & 0x800000L) == 0x800000L;
	}

	public String get_shoptitle() {
		return _shoptitle;
	}

	public void set_shoptitle(String val) {
		_bit |= 0x1000000L;
		_shoptitle = val;
	}

	public boolean has_shoptitle() {
		return (_bit & 0x1000000L) == 0x1000000L;
	}

	public int get_weaponsprite() {
		return _weaponsprite;
	}

	public void set_weaponsprite(int val) {
		_bit |= 0x2000000L;
		_weaponsprite = val;
	}

	public boolean has_weaponsprite() {
		return (_bit & 0x2000000L) == 0x2000000L;
	}

	public int get_couplestate() {
		return _couplestate;
	}

	public void set_couplestate(int val) {
		_bit |= 0x4000000L;
		_couplestate = val;
	}

	public boolean has_couplestate() {
		return (_bit & 0x4000000L) == 0x4000000L;
	}

	public int get_boundarylevelindex() {
		return _boundarylevelindex;
	}

	public void set_boundarylevelindex(int val) {
		_bit |= 0x8000000L;
		_boundarylevelindex = val;
	}

	public boolean has_boundarylevelindex() {
		return (_bit & 0x8000000L) == 0x8000000L;
	}

	public int get_weakelemental() {
		return _weakelemental;
	}

	public void set_weakelemental(int val) {
		_bit |= 0x10000000L;
		_weakelemental = val;
	}

	public boolean has_weakelemental() {
		return (_bit & 0x10000000L) == 0x10000000L;
	}

	public int get_manaratio() {
		return _manaratio;
	}

	public void set_manaratio(int val) {
		_bit |= 0x20000000L;
		_manaratio = val;
	}

	public boolean has_manaratio() {
		return (_bit & 0x20000000L) == 0x20000000L;
	}

	public int get_botindex() {
		return _botindex;
	}

	public void set_botindex(int val) {
		_bit |= 0x40000000L;
		_botindex = val;
	}

	public boolean has_botindex() {
		return (_bit & 0x40000000L) == 0x40000000L;
	}

	public int get_homeserverno() {
		return _homeserverno;
	}

	public void set_homeserverno(int val) {
		_bit |= 0x80000000L;
		_homeserverno = val;
	}

	public boolean has_homeserverno() {
		return (_bit & 0x80000000L) == 0x80000000L;
	}

	public int get_team_id() {
		return _team_id;
	}

	public void set_team_id(int val) {
		_bit |= 0x100000000L;
		_team_id = val;
	}

	public boolean has_team_id() {
		return (_bit & 0x100000000L) == 0x100000000L;
	}

	public int get_dialog_radius() {
		return _dialog_radius;
	}

	public void set_dialog_radius(int val) {
		_bit |= 0x200000000L;
		_dialog_radius = val;
	}

	public boolean has_dialog_radius() {
		return (_bit & 0x200000000L) == 0x200000000L;
	}

	public int get_speed_value_flag() {
		return _speed_value_flag;
	}

	public void set_speed_value_flag(int val) {
		_bit |= 0x400000000L;
		_speed_value_flag = val;
	}

	public boolean has_speed_value_flag() {
		return (_bit & 0x400000000L) == 0x400000000L;
	}

	public int get_second_speed_type() {
		return _second_speed_type;
	}

	public void set_second_speed_type(int val) {
		_bit |= 0x800000000L;
		_second_speed_type = val;
	}

	public boolean has_second_speed_type() {
		return (_bit & 0x800000000L) == 0x800000000L;
	}

	public int get_explosion_remain_time_ms() {
		return _explosion_remain_time_ms;
	}

	public void set_explosion_remain_time_ms(int val) {
		_bit |= 0x1000000000L;
		_explosion_remain_time_ms = val;
	}

	public boolean has_explosion_remain_time_ms() {
		return (_bit & 0x1000000000L) == 0x1000000000L;
	}

	public boolean get_proclamation_siege_mark() {
		return _proclamation_siege_mark;
	}

	public void set_proclamation_siege_mark(boolean val) {
		_bit |= 0x2000000000L;
		_proclamation_siege_mark = val;
	}

	public boolean has_proclamation_siege_mark() {
		return (_bit & 0x2000000000L) == 0x2000000000L;
	}

	public int get_npc_class_id() {
		return _npc_class_id;
	}

	public void set_npc_class_id(int val) {
		_bit |= 0x4000000000L;
		_npc_class_id = val;
	}

	public boolean has_npc_class_id() {
		return (_bit & 0x4000000000L) == 0x4000000000L;
	}

	public SC_WORLD_PUT_OBJECT_NOTI.Companion get_companion() {
		return _companion;
	}

	public void set_companion(SC_WORLD_PUT_OBJECT_NOTI.Companion val) {
		_bit |= 0x8000000000L;
		_companion = val;
	}

	public boolean has_companion() {
		return (_bit & 0x8000000000L) == 0x8000000000L;
	}

	public String get_force_haction() {
		return _force_haction;
	}

	public void set_force_haction(String val) {
		_bit |= 0x10000000000L;
		_force_haction = val;
	}

	public boolean has_force_haction() {
		return (_bit & 0x10000000000L) == 0x10000000000L;
	}

	public boolean get_is_portal() {
		return _is_portal;
	}

	public void set_is_portal(boolean val) {
		_bit |= 0x20000000000L;
		_is_portal = val;
	}

	public boolean has_is_portal() {
		return (_bit & 0x20000000000L) == 0x20000000000L;
	}

	public boolean get_proclamation_siege_pledge() {
		return _proclamation_siege_pledge;
	}

	public void set_proclamation_siege_pledge(boolean val) {
		_bit |= 0x40000000000L;
		_proclamation_siege_pledge = val;
	}

	public boolean has_proclamation_siege_pledge() {
		return (_bit & 0x40000000000L) == 0x40000000000L;
	}

	public SC_WORLD_PUT_OBJECT_NOTI.DropItemInfo get_drop_info() {
		return _drop_info;
	}

	public void set_drop_info(SC_WORLD_PUT_OBJECT_NOTI.DropItemInfo val) {
		_bit |= 0x80000000000L;
		_drop_info = val;
	}

	public boolean has_drop_info() {
		return (_bit & 0x80000000000L) == 0x80000000000L;
	}

	public boolean get_castle_owner_group() {
		return _castle_owner_group;
	}

	public void set_castle_owner_group(boolean val) {
		_bit |= 0x100000000000L;
		_castle_owner_group = val;
	}

	public boolean has_castle_owner_group() {
		return (_bit & 0x100000000000L) == 0x100000000000L;
	}

	public int get_user_game_class() {
		return _user_game_class;
	}

	public void set_user_game_class(int val) {
		_bit |= 0x200000000000L;
		_user_game_class = val;
	}

	public boolean has_user_game_class() {
		return (_bit & 0x200000000000L) == 0x200000000000L;
	}

	public String get_anonymity_name() {
		return _anonymity_name;
	}

	public void set_anonymity_name(String val) {
		_bit |= 0x400000000000L;
		_anonymity_name = val;
	}

	public boolean has_anonymity_name() {
		return (_bit & 0x400000000000L) == 0x400000000000L;
	}

	public ePolymorphAnonymityType get_anonymity_type() {
		return _anonymity_type;
	}

	public void set_anonymity_type(ePolymorphAnonymityType val) {
		_bit |= 0x800000000000L;
		_anonymity_type = val;
	}

	public boolean has_anonymity_type() {
		return (_bit & 0x800000000000L) == 0x800000000000L;
	}

	public int get_potential_grade() {
		return _potential_grade;
	}

	public void set_potential_grade(int val) {
		_bit |= 0x1000000000000L;
		_potential_grade = val;
	}

	public boolean has_potential_grade() {
		return (_bit & 0x1000000000000L) == 0x1000000000000L;
	}

	public boolean get_forth_gear() {
		return _forth_gear;
	}

	public void set_forth_gear(boolean val) {
		_bit |= 0x2000000000000L;
		_forth_gear = val;
	}

	public boolean has_forth_gear() {
		return (_bit & 0x2000000000000L) == 0x2000000000000L;
	}

	public SC_SPEED_BONUS_NOTI.Bonus get_speed_bonus() {
		return _speed_bonus;
	}

	public void set_speed_bonus(SC_SPEED_BONUS_NOTI.Bonus val) {
		_bit |= 0x4000000000000L;
		_speed_bonus = val;
	}

	public boolean has_speed_bonus() {
		return (_bit & 0x4000000000000L) == 0x4000000000000L;
	}

	public SC_SPEED_BONUS_NOTI.Bonus get_speed_change_rate() {
		return _speed_change_rate;
	}

	public void set_speed_change_rate(SC_SPEED_BONUS_NOTI.Bonus val) {
		_bit |= 0x8000000000000L;
		_speed_change_rate = val;
	}

	public boolean has_speed_change_rate() {
		return (_bit & 0x8000000000000L) == 0x8000000000000L;
	}

	public boolean get_is_apc() {
		return _is_apc;
	}

	public void set_is_apc(boolean val) {
		_bit |= 0x10000000000000L;
		_is_apc = val;
	}

	public boolean has_is_apc() {
		return (_bit & 0x10000000000000L) == 0x10000000000000L;
	}

	public int get_category() {
		return _category;
	}

	public void set_category(int val) {
		_bit |= 0x20000000000000L;
		_category = val;
	}

	public boolean has_category() {
		return (_bit & 0x20000000000000L) == 0x20000000000000L;
	}

	public boolean get_is_excavated() {
		return _is_excavated;
	}

	public void set_is_excavated(boolean val) {
		_bit |= 0x40000000000000L;
		_is_excavated = val;
	}

	public boolean has_is_excavated() {
		return (_bit & 0x40000000000000L) == 0x40000000000000L;
	}

	public int get_apc_pledge_icon() {
		return _apc_pledge_icon;
	}

	public void set_apc_pledge_icon(int val) {
		_bit |= 0x80000000000000L;
		_apc_pledge_icon = val;
	}

	public boolean has_apc_pledge_icon() {
		return (_bit & 0x80000000000000L) == 0x80000000000000L;
	}

	public int get_above_head_mark_id() {
		return _above_head_mark_id;
	}

	public void set_above_head_mark_id(int val) {
		_bit |= 0x100000000000000L;
		_above_head_mark_id = val;
	}

	public boolean has_above_head_mark_id() {
		return (_bit & 0x100000000000000L) == 0x100000000000000L;
	}

	public int get_fix_frame_level() {
		return _fix_frame_level;
	}

	public void set_fix_frame_level(int val) {
		_bit |= 0x200000000000000L;
		_fix_frame_level = val;
	}

	public boolean has_fix_frame_level() {
		return (_bit & 0x200000000000000L) == 0x200000000000000L;
	}

	public int get_polymorph_effect_id() {
		return _polymorph_effect_id;
	}

	public void set_polymorph_effect_id(int val) {
		_bit |= 0x400000000000000L;
		_polymorph_effect_id = val;
	}

	public boolean has_polymorph_effect_id() {
		return (_bit & 0x400000000000000L) == 0x400000000000000L;
	}

	public int get_invisible_level() {
		return _invisible_level;
	}

	public void set_invisible_level(int val) {
		_bit |= 0x800000000000000L;
		_invisible_level = val;
	}

	public boolean has_invisible_level() {
		return (_bit & 0x800000000000000L) == 0x800000000000000L;
	}

	@Override
	public long getInitializeBit() {
		return _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_point()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _point);
		}
		if (has_objectnumber()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _objectnumber);
		}
		if (has_objectsprite()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _objectsprite);
		}
		if (has_action()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _action);
		}
		if (has_direction()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _direction);
		}
		if (has_lightRadius()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _lightRadius);
		}
		if (has_objectcount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _objectcount);
		}
		if (has_alignment()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _alignment);
		}
		if (has_desc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(9, _desc);
		}
		if (has_title()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(10, _title);
		}
		if (has_speeddata()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _speeddata);
		}
		if (has_emotion()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(12, _emotion);
		}
		if (has_drunken()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(13, _drunken);
		}
		if (has_isghost()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(14, _isghost);
		}
		if (has_isparalyzed()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(15, _isparalyzed);
		}
		if (has_isuser()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(16, _isuser);
		}
		if (has_isinvisible()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(17, _isinvisible);
		}
		if (has_ispoisoned()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(18, _ispoisoned);
		}
		if (has_emblemid()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(19, _emblemid);
		}
		if (has_pledgename()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(20, _pledgename);
		}
		if (has_mastername()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(21, _mastername);
		}
		if (has_altitude()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(22, _altitude);
		}
		if (has_hitratio()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(23, _hitratio);
		}
		if (has_safelevel()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(24, _safelevel);
		}
		if (has_shoptitle()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(25, _shoptitle);
		}
		if (has_weaponsprite()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(26, _weaponsprite);
		}
		if (has_couplestate()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(27, _couplestate);
		}
		if (has_boundarylevelindex()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(28, _boundarylevelindex);
		}
		if (has_weakelemental()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(29, _weakelemental);
		}
		if (has_manaratio()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(30, _manaratio);
		}
		if (has_botindex()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(31, _botindex);
		}
		if (has_homeserverno()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(32, _homeserverno);
		}
		if (has_team_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(33, _team_id);
		}
		if (has_dialog_radius()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(34, _dialog_radius);
		}
		if (has_speed_value_flag()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(35, _speed_value_flag);
		}
		if (has_second_speed_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(36, _second_speed_type);
		}
		if (has_explosion_remain_time_ms()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(37, _explosion_remain_time_ms);
		}
		if (has_proclamation_siege_mark()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(38, _proclamation_siege_mark);
		}
		if (has_npc_class_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(39, _npc_class_id);
		}
		if (has_companion()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(40, _companion);
		}
		if (has_force_haction()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(41, _force_haction);
		}
		if (has_is_portal()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(42, _is_portal);
		}
		if (has_proclamation_siege_pledge()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(43, _proclamation_siege_pledge);
		}
		if (has_drop_info()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(44, _drop_info);
		}
		if (has_castle_owner_group()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(45, _castle_owner_group);
		}
		if (has_user_game_class()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(46, _user_game_class);
		}
		if (has_anonymity_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(47, _anonymity_name);
		}
		if (has_anonymity_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(48, _anonymity_type.toInt());
		}
		if (has_potential_grade()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(49, _potential_grade);
		}
		if (has_forth_gear()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(50, _forth_gear);
		}
		if (has_speed_bonus()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(51, _speed_bonus);
		}
		if (has_speed_change_rate()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(52, _speed_change_rate);
		}
		if (has_is_apc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(53, _is_apc);
		}
		if (has_category()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(54, _category);
		}
		if (has_is_excavated()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(55, _is_excavated);
		}
		if (has_apc_pledge_icon()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(56, _apc_pledge_icon);
		}
		if (has_above_head_mark_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(57, _above_head_mark_id);
		}
		if (has_fix_frame_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(58, _fix_frame_level);
		}
		if (has_polymorph_effect_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(59, _polymorph_effect_id);
		}
		if (has_invisible_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(60, _invisible_level);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_point()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_objectnumber()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_objectsprite()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_direction()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_lightRadius()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_objectcount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_alignment()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_desc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_title()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_speeddata()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_emotion()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_drunken()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isghost()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isparalyzed()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isuser()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isinvisible()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_ispoisoned()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_emblemid()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_pledgename()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_mastername()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_altitude()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_hitratio()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_safelevel()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weaponsprite()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_couplestate()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_boundarylevelindex()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_point()) {
			output.writeUInt32(1, _point);
		}
		if (has_objectnumber()) {
			output.writeUInt32(2, _objectnumber);
		}
		if (has_objectsprite()) {
			output.wirteInt32(3, _objectsprite);
		}
		if (has_action()) {
			output.wirteInt32(4, _action);
		}
		if (has_direction()) {
			output.wirteInt32(5, _direction);
		}
		if (has_lightRadius()) {
			output.wirteInt32(6, _lightRadius);
		}
		if (has_objectcount()) {
			output.wirteInt32(7, _objectcount);
		}
		if (has_alignment()) {
			output.wirteInt32(8, _alignment);
		}
		if (has_desc()) {
			output.writeString(9, _desc);
		}
		if (has_title()) {
			output.writeString(10, _title);
		}
		if (has_speeddata()) {
			output.wirteInt32(11, _speeddata);
		}
		if (has_emotion()) {
			output.wirteInt32(12, _emotion);
		}
		if (has_drunken()) {
			output.wirteInt32(13, _drunken);
		}
		if (has_isghost()) {
			output.writeBool(14, _isghost);
		}
		if (has_isparalyzed()) {
			output.writeBool(15, _isparalyzed);
		}
		if (has_isuser()) {
			output.writeBool(16, _isuser);
		}
		if (has_isinvisible()) {
			output.writeBool(17, _isinvisible);
		}
		if (has_ispoisoned()) {
			output.writeBool(18, _ispoisoned);
		}
		if (has_emblemid()) {
			output.writeUInt32(19, _emblemid);
		}
		if (has_pledgename()) {
			output.writeString(20, _pledgename);
		}
		if (has_mastername()) {
			output.writeString(21, _mastername);
		}
		if (has_altitude()) {
			output.wirteInt32(22, _altitude);
		}
		if (has_hitratio()) {
			output.wirteInt32(23, _hitratio);
		}
		if (has_safelevel()) {
			output.wirteInt32(24, _safelevel);
		}
		if (has_shoptitle()) {
			output.writeString(25, _shoptitle);
		}
		if (has_weaponsprite()) {
			output.wirteInt32(26, _weaponsprite);
		}
		if (has_couplestate()) {
			output.wirteInt32(27, _couplestate);
		}
		if (has_boundarylevelindex()) {
			output.wirteInt32(28, _boundarylevelindex);
		}
		if (has_weakelemental()) {
			output.wirteInt32(29, _weakelemental);
		}
		if (has_manaratio()) {
			output.wirteInt32(30, _manaratio);
		}
		if (has_botindex()) {
			output.writeUInt32(31, _botindex);
		}
		if (has_homeserverno()) {
			output.wirteInt32(32, _homeserverno);
		}
		if (has_team_id()) {
			output.writeUInt32(33, _team_id);
		}
		if (has_dialog_radius()) {
			output.writeUInt32(34, _dialog_radius);
		}
		if (has_speed_value_flag()) {
			output.wirteInt32(35, _speed_value_flag);
		}
		if (has_second_speed_type()) {
			output.wirteInt32(36, _second_speed_type);
		}
		if (has_explosion_remain_time_ms()) {
			output.writeUInt32(37, _explosion_remain_time_ms);
		}
		if (has_proclamation_siege_mark()) {
			output.writeBool(38, _proclamation_siege_mark);
		}
		if (has_npc_class_id()) {
			output.wirteInt32(39, _npc_class_id);
		}
		if (has_companion()) {
			output.writeMessage(40, _companion);
		}
		if (has_force_haction()) {
			output.writeString(41, _force_haction);
		}
		if (has_is_portal()) {
			output.writeBool(42, _is_portal);
		}
		if (has_proclamation_siege_pledge()) {
			output.writeBool(43, _proclamation_siege_pledge);
		}
		if (has_drop_info()) {
			output.writeMessage(44, _drop_info);
		}
		if (has_castle_owner_group()) {
			output.writeBool(45, _castle_owner_group);
		}
		if (has_user_game_class()) {
			output.wirteInt32(46, _user_game_class);
		}
		if (has_anonymity_name()) {
			output.writeString(47, _anonymity_name);
		}
		if (has_anonymity_type()) {
			output.writeEnum(48, _anonymity_type.toInt());
		}
		if (has_potential_grade()) {
			output.wirteInt32(49, _potential_grade);
		}
		if (has_forth_gear()) {
			output.writeBool(50, _forth_gear);
		}
		if (has_speed_bonus()) {
			output.writeMessage(51, _speed_bonus);
		}
		if (has_speed_change_rate()) {
			output.writeMessage(52, _speed_change_rate);
		}
		if (has_is_apc()) {
			output.writeBool(53, _is_apc);
		}
		if (has_category()) {
			output.wirteInt32(54, _category);
		}
		if (has_is_excavated()) {
			output.writeBool(55, _is_excavated);
		}
		if (has_apc_pledge_icon()) {
			output.wirteInt32(56, _apc_pledge_icon);
		}
		if (has_above_head_mark_id()) {
			output.wirteInt32(57, _above_head_mark_id);
		}
		if (has_fix_frame_level()) {
			output.wirteInt32(58, _fix_frame_level);
		}
		if (has_polymorph_effect_id()) {
			output.wirteInt32(59, _polymorph_effect_id);
		}
		if (has_invisible_level()) {
			output.wirteInt32(60, _invisible_level);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				case 0x00000008: {
					set_point(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_objectnumber(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_objectsprite(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_action(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_direction(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_lightRadius(input.readInt32());
					break;
				}
				case 0x00000038: {
					set_objectcount(input.readInt32());
					break;
				}
				case 0x00000040: {
					set_alignment(input.readInt32());
					break;
				}
				case 0x0000004A: {
					set_desc(input.readString());
					break;
				}
				case 0x00000052: {
					set_title(input.readString());
					break;
				}
				case 0x00000058: {
					set_speeddata(input.readInt32());
					break;
				}
				case 0x00000060: {
					set_emotion(input.readInt32());
					break;
				}
				case 0x00000068: {
					set_drunken(input.readInt32());
					break;
				}
				case 0x00000070: {
					set_isghost(input.readBool());
					break;
				}
				case 0x00000078: {
					set_isparalyzed(input.readBool());
					break;
				}
				case 0x00000080: {
					set_isuser(input.readBool());
					break;
				}
				case 0x00000088: {
					set_isinvisible(input.readBool());
					break;
				}
				case 0x00000090: {
					set_ispoisoned(input.readBool());
					break;
				}
				case 0x00000098: {
					set_emblemid(input.readUInt32());
					break;
				}
				case 0x000000A2: {
					set_pledgename(input.readString());
					break;
				}
				case 0x000000AA: {
					set_mastername(input.readString());
					break;
				}
				case 0x000000B0: {
					set_altitude(input.readInt32());
					break;
				}
				case 0x000000B8: {
					set_hitratio(input.readInt32());
					break;
				}
				case 0x000000C0: {
					set_safelevel(input.readInt32());
					break;
				}
				case 0x000000CA: {
					set_shoptitle(input.readString());
					break;
				}
				case 0x000000D0: {
					set_weaponsprite(input.readInt32());
					break;
				}
				case 0x000000D8: {
					set_couplestate(input.readInt32());
					break;
				}
				case 0x000000E0: {
					set_boundarylevelindex(input.readInt32());
					break;
				}
				case 0x000000E8: {
					set_weakelemental(input.readInt32());
					break;
				}
				case 0x000000F0: {
					set_manaratio(input.readInt32());
					break;
				}
				case 0x000000F8: {
					set_botindex(input.readUInt32());
					break;
				}
				case 0x00000100: { // 0x0280
					set_homeserverno(input.readInt32());
					break;
				}
				case 0x00000108: {
					set_team_id(input.readUInt32());
					break;
				}
				case 0x00000110: { // 0x0290
					set_dialog_radius(input.readUInt32());
					break;
				}
				case 0x00000118: { // 0x0298
					set_speed_value_flag(input.readInt32());
					break;
				}
				case 0x00000120: {
					set_second_speed_type(input.readInt32());
					break;
				}
				case 0x00000128: {
					set_explosion_remain_time_ms(input.readUInt32());
					break;
				}
				case 0x00000130: {
					set_proclamation_siege_mark(input.readBool());
					break;
				}
				case 0x00000138: { // 0x02b8
					set_npc_class_id(input.readInt32());
					break;
				}
				case 0x00000142: {
					set_companion((SC_WORLD_PUT_OBJECT_NOTI.Companion) input
							.readMessage(SC_WORLD_PUT_OBJECT_NOTI.Companion.newInstance()));
					break;
				}
				case 0x0000014A: {
					set_force_haction(input.readString());
					break;
				}
				case 0x00000150: {
					set_is_portal(input.readBool());
					break;
				}
				case 0x00000158: {
					set_proclamation_siege_pledge(input.readBool());
					break;
				}
				case 0x00000162: {
					set_drop_info((SC_WORLD_PUT_OBJECT_NOTI.DropItemInfo) input
							.readMessage(SC_WORLD_PUT_OBJECT_NOTI.DropItemInfo.newInstance()));
					break;
				}
				case 0x00000168: {
					set_castle_owner_group(input.readBool());
					break;
				}
				case 0x00000170: {
					set_user_game_class(input.readInt32());
					break;
				}
				case 0x0000017A: {
					set_anonymity_name(input.readString());
					break;
				}
				case 0x00000180: {
					set_anonymity_type(ePolymorphAnonymityType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000188: { // 0x0388
					set_potential_grade(input.readInt32());
					break;
				}
				case 0x00000190: {
					set_forth_gear(input.readBool());
					break;
				}
				case 0x0000019A: {
					set_speed_bonus(
							(SC_SPEED_BONUS_NOTI.Bonus) input.readMessage(SC_SPEED_BONUS_NOTI.Bonus.newInstance()));
					break;
				}
				case 0x000001A2: {
					set_speed_change_rate(
							(SC_SPEED_BONUS_NOTI.Bonus) input.readMessage(SC_SPEED_BONUS_NOTI.Bonus.newInstance()));
					break;
				}
				case 0x000001A8: {
					set_is_apc(input.readBool());
					break;
				}
				case 0x000001B0: {
					set_category(input.readInt32());
					break;
				}
				case 0x000001B8: {
					set_is_excavated(input.readBool());
					break;
				}
				case 0x000001C0: {
					set_apc_pledge_icon(input.readInt32());
					break;
				}
				case 0x000001C8: {
					set_above_head_mark_id(input.readInt32());
					break;
				}
				case 0x000001D0: {
					set_fix_frame_level(input.readInt32());
					break;
				}
				case 0x000001D8: {
					set_polymorph_effect_id(input.readInt32());
					break;
				}
				case 0x000001E0: {
					set_invisible_level(input.readInt32());
					break;
				}
				default: {
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_WORLD_PUT_OBJECT_NOTI();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class Companion implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static Companion newInstance() {
			return new Companion();
		}

		private double _move_delay_reduce;
		private double _attack_delay_reduce;
		private int _pvp_dmg_ratio;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private Companion() {
		}

		public double get_move_delay_reduce() {
			return _move_delay_reduce;
		}

		public void set_move_delay_reduce(double val) {
			_bit |= 0x1;
			_move_delay_reduce = val;
		}

		public boolean has_move_delay_reduce() {
			return (_bit & 0x1) == 0x1;
		}

		public double get_attack_delay_reduce() {
			return _attack_delay_reduce;
		}

		public void set_attack_delay_reduce(double val) {
			_bit |= 0x2;
			_attack_delay_reduce = val;
		}

		public boolean has_attack_delay_reduce() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_pvp_dmg_ratio() {
			return _pvp_dmg_ratio;
		}

		public void set_pvp_dmg_ratio(int val) {
			_bit |= 0x4;
			_pvp_dmg_ratio = val;
		}

		public boolean has_pvp_dmg_ratio() {
			return (_bit & 0x4) == 0x4;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_move_delay_reduce()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(1, _move_delay_reduce);
			}
			if (has_attack_delay_reduce()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(2, _attack_delay_reduce);
			}
			if (has_pvp_dmg_ratio()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _pvp_dmg_ratio);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_move_delay_reduce()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_attack_delay_reduce()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_pvp_dmg_ratio()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_move_delay_reduce()) {
				output.writeDouble(1, _move_delay_reduce);
			}
			if (has_attack_delay_reduce()) {
				output.writeDouble(2, _attack_delay_reduce);
			}
			if (has_pvp_dmg_ratio()) {
				output.writeUInt32(3, _pvp_dmg_ratio);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000009: {
						set_move_delay_reduce(input.readDouble());
						break;
					}
					case 0x00000011: {
						set_attack_delay_reduce(input.readDouble());
						break;
					}
					case 0x00000018: {
						set_pvp_dmg_ratio(input.readUInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new Companion();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}

	public static class DropItemInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static DropItemInfo newInstance() {
			return new DropItemInfo();
		}

		private int _get_available_time;
		private int _reward_prob;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private DropItemInfo() {
		}

		public int get_get_available_time() {
			return _get_available_time;
		}

		public void set_get_available_time(int val) {
			_bit |= 0x1;
			_get_available_time = val;
		}

		public boolean has_get_available_time() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_reward_prob() {
			return _reward_prob;
		}

		public void set_reward_prob(int val) {
			_bit |= 0x2;
			_reward_prob = val;
		}

		public boolean has_reward_prob() {
			return (_bit & 0x2) == 0x2;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_get_available_time()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _get_available_time);
			}
			if (has_reward_prob()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _reward_prob);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_get_available_time()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_get_available_time()) {
				output.writeUInt32(1, _get_available_time);
			}
			if (has_reward_prob()) {
				output.wirteInt32(2, _reward_prob);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_get_available_time(input.readUInt32());
						break;
					}
					case 0x00000010: {
						set_reward_prob(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new DropItemInfo();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
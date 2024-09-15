package l1j.server.server.model.Instance;

import java.util.Arrays;
import java.util.Timer;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FOURTH_GEAR_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SUMMON_PET_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_SPELL_LATE_HANDLING_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_SPELL_LATE_HANDLING_NOTI.eLevel;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_UPDATE_INVENTORY_NOTI;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.model.HpRegenerationByDoll;
import l1j.server.server.model.L1World;
import l1j.server.server.model.MakeItemByDollTimer;
import l1j.server.server.model.MpRegenerationByDoll;
import l1j.server.server.model.item.function.L1MagicDoll;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DollPack;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_ItemColor;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.Teleportation;

public class L1DollInstance extends L1NpcInstance {
	private static final int[] DollAction = { 66, 67, 68, 69 };
	private static final long serialVersionUID = 1L;
	private L1MagicDoll _magicDoll;
	private static Timer _timer = new Timer();
	private HpRegenerationByDoll _hprTimer;
	private MpRegenerationByDoll _mprTimer;
	private MakeItemByDollTimer _makeTimer;
	private int sleeptime_PT = 64;
	private int _itemId;
	private int _itemObjId;
	private int _doll_class_id;

	public boolean noTarget() {
		if (_master == null) {
			deleteDoll();
			return true;
		}
		if (_master.isDead()) {
			deleteDoll();
			return true;
		}
		if (_master != null) {
			int distance = getLocation().getTileLineDistance(_master.getLocation());
			if (_master.getMapId() != getMapId() || distance > 15) {
				Teleportation.teleport(this, _master.getX(), _master.getY(), _master.getMapId(), 5);
			} else if (distance > 2) {
				int dir = targetDirection(_master.getX(), _master.getY());
				dir = checkObject(getX(), getY(), getMapId(), dir);
				setSleepTime(setDirectionMoveSpeed(dir));
				
				/*
				
				int dir = moveDirection(_master.getMapId(), _master.getX(), _master.getY());
				System.out.println(dir+"+"+_master.getMapId()+"+"+_master.getX()+"+"+_master.getY());
				setDirectionMoveSpeed(dir);
				setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));*/
			} else if (this.sleeptime_PT == 0) {
				/*L1ItemInstance item = _master.getInventory().getItem(_itemObjId);
				if (item.get_Doll_Bonus_Value() > 0) {
					int[] doll_action = { 19198, 19200, 19202, 19204, 19208 };
					broadcastPacket(new S_SkillSound(getId(), doll_action[item.get_Doll_Bonus_Level()]));
				}*/
				broadcastPacket(new S_DoActionGFX(getId(), DollAction[CommonUtil.random(2)]));
				sleeptime_PT = 64;
			} else {
				sleeptime_PT -= 1;
				setSleepTime(500);
			}

		} else {
			deleteDoll();
			return true;
		}
		return false;
	}

	public int moveDirection(int mapid,  int x, int y, double d) {
		int dir = 0;
		if (d > courceRange) {
			dir = targetDirection(x, y);
			dir = checkObject(getX(), getY(), getMapId(), dir);
		} else {
			dir = _serchCource(mapid, x, y);
			if (dir == -1) {
				dir = targetDirection(x, y);
				if (!isExsistCharacterBetweenTarget(dir)) {
					dir = checkObject(getX(), getY(), getMapId(), dir);
				}
			}
		}
		return dir;
	}

	public L1DollInstance(L1Npc template, L1PcInstance master, L1ItemInstance item, int doll_class_id) {
		super(template);

		setId(IdFactory.getInstance().nextId());
		setItemId(item.getItemId());
		setItemObjId(item.getId());
		setMagicDollBless(item.getBless());
		_doll_class_id = doll_class_id;
		_magicDoll = L1MagicDoll.get(item.getItemId());

		int summonTime = getDoll().getInfo().getSummonTime() * 1000;
		GeneralThreadPool.getInstance().schedule(new DollTimer(), summonTime);

		setMaster(master);
		setX(CommonUtil.random(5) + master.getX() - 2);
		setY(CommonUtil.random(5) + master.getY() - 2);
		setMap(master.getMapId());
		setHeading(CommonUtil.random(8));
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.setMagicDoll(this);

		if(master.isInvisble()) {
			master.sendPackets(new S_SkillSound(getId(), 5935));
			master.broadcastPacketForFindInvis(new S_SkillSound(getId(), 5935), true);
		} else {
			broadcastPacket(new S_SkillSound(getId(), 5935));
		}

		if (!isAiRunning()) {
			startAI();
		}

		startHprTimer();

		startMprTimer();

		startMakeTimer();

		if (_master != null) {
			effectToMaster(1, item);
		}
	}

	public void deleteDoll() {
		L1PcInstance pc = (L1PcInstance) _master;
		L1ItemInstance item =  pc.getInventory().findItemObjId(_itemObjId);

		//broadcastPacket(new S_EffectLocation(getX(), getY(), 5936));

		stopHprTimer();
		stopMprTimer();
		stopMakeTimer();

		if (_master != null) {
			effectToMaster(-1, item);
			_master.setMagicDoll(null);
		}
		deleteMe();
		if (_master != null && _master instanceof L1PcInstance) {
			if(item != null) {
				SC_SUMMON_PET_NOTI.off_summoned(pc, item, getNpcClassId());
				SC_UPDATE_INVENTORY_NOTI.on_doll_summoned(pc, getItemObjId(), false);
				SC_SPELL_LATE_HANDLING_NOTI.send(pc, true, eLevel.NOT_CORRECTION);
				pc.sendPackets(new S_ItemColor(item));
			}
			pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));

			if(pc.isInvisble()) {
				pc.sendPackets(new S_EffectLocation(getX(), getY(), 5936));
				pc.broadcastPacketForFindInvis(new S_EffectLocation(getX(), getY(), 5936), true);
			} else {
				broadcastPacket(new S_EffectLocation(getX(), getY(), 5936));
			}
		}
	}

	private void effectToMaster(int type, L1ItemInstance item) {
		if (_master instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _master;
			if (getDoll().getEffect().getHp() != 0) {
				pc.addMaxHp(type * getDoll().getEffect().getHp());
				pc.sendPackets(new S_HPUpdate(pc));
			}

			if (getDoll().getEffect().getHpr() != 0 && getDoll().getEffect().getHprTime() == 0) {
				pc.addHpr(type * getDoll().getEffect().getHpr());
			}

			if (getDoll().getEffect().getMp() != 0) {
				pc.addMaxMp(type * getDoll().getEffect().getMp());
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}

			if (getDoll().getEffect().getMpr() != 0 && getDoll().getEffect().getMprTime() == 0) {
				pc.addMpr(type * getDoll().getEffect().getMpr());
			}

			if (getDoll().getEffect().getAc() != 0) {
				pc.getAC().addAc(type * getDoll().getEffect().getAc());
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}

			if (getDoll().getEffect().getStr() != 0) {
				pc.getAbility().addAddedStr(type * getDoll().getEffect().getStr());
			}

			if (getDoll().getEffect().getCon() != 0) {
				pc.getAbility().addAddedCon(type * getDoll().getEffect().getCon());
			}

			if (getDoll().getEffect().getDex() != 0) {
				pc.getAbility().addAddedDex(type * getDoll().getEffect().getDex());
			}

			if (getDoll().getEffect().getCha() != 0)
				pc.getAbility().addAddedCha(type * getDoll().getEffect().getCha());

			if (getDoll().getEffect().getInt() != 0) {
				pc.getAbility().addAddedInt(type * getDoll().getEffect().getInt());
			}

			if (getDoll().getEffect().getWis() != 0) {
				pc.getAbility().addAddedWis(type * getDoll().getEffect().getWis());
			}

			if (getDoll().getEffect().getTechniqueTolerance() != 0) {
				pc.addSpecialResistance(eKind.ABILITY, type * getDoll().getEffect().getTechniqueTolerance());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getSpiritTolerance() != 0) {
				pc.addSpecialResistance(eKind.SPIRIT, type * getDoll().getEffect().getSpiritTolerance());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getDragonLangTolerance() != 0) {
				pc.addSpecialResistance(eKind.DRAGON_SPELL, type * getDoll().getEffect().getDragonLangTolerance());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getFearTolerance() != 0) {
				pc.addSpecialResistance(eKind.FEAR, type * getDoll().getEffect().getFearTolerance());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getAllTolerance() != 0) {
				pc.addSpecialResistance(eKind.ALL, type * getDoll().getEffect().getAllTolerance());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}

			if (getDoll().getEffect().getTechniqueHit() != 0) {
				pc.addSpecialPierce(eKind.ABILITY, type * getDoll().getEffect().getTechniqueHit());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getSpiritHit() != 0) {
				pc.addSpecialPierce(eKind.SPIRIT, type * getDoll().getEffect().getSpiritHit());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getDragonLangHit() != 0) {
				pc.addSpecialPierce(eKind.DRAGON_SPELL, type * getDoll().getEffect().getDragonLangHit());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getFearHit() != 0) {
				pc.addSpecialPierce(eKind.FEAR, type * getDoll().getEffect().getFearHit());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (getDoll().getEffect().getAllHit() != 0) {
				pc.addSpecialPierce(eKind.ALL, type * getDoll().getEffect().getAllHit());
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}

			if (getDoll().getEffect().getDmgChance() == 0) {
				if (getDoll().getEffect().getDmg() != 0)
					pc.addDmgup(type * getDoll().getEffect().getDmg());
			}

			if (getDoll().getEffect().getBowDmg() != 0)
				pc.addBowDmgup(type * getDoll().getEffect().getBowDmg());
			if (getDoll().getEffect().getHit() != 0)
				pc.addHitup(type * getDoll().getEffect().getHit());
			if (getDoll().getEffect().getBowHit() != 0)
				pc.addBowHitup(type * getDoll().getEffect().getBowHit());

			if (getDoll().getEffect().getMr() != 0) {
//				pc.addMr(type * getDoll().getEffect().getMr());
				if(pc.getResistance() != null) {
					pc.getResistance().addMr(type * getDoll().getEffect().getMr());
				}
				pc.sendPackets(new S_SPMR(pc));
			}
			if (getDoll().getEffect().getSp() != 0) {
				pc.getAbility().addSp(type * getDoll().getEffect().getSp());
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}

			if (getDoll().getEffect().getDmgReductionChance() == 0) {
				if (getDoll().getEffect().getDmgReduction() != 0)
					((L1PcInstance) pc).addDamageReductionByArmor(type * getDoll().getEffect().getDmgReduction());
			}

			if (getDoll().getEffect().getFoeDmg() != 0)
				pc.addFouDmg(type * getDoll().getEffect().getFoeDmg());

			if (getDoll().getEffect().getTitanPercent() != 0)
				pc.add락구간상승(type * getDoll().getEffect().getTitanPercent());

			if (getDoll().getEffect().getWeightReduction() != 0)
				pc.addWeightReduction(type * getDoll().getEffect().getWeightReduction());

			if (getDoll().getEffect().getAinEfficiency() != 0)
				pc.addEinhasadBlessper(type * getDoll().getEffect().getAinEfficiency());

			if (getDoll().getEffect().getAdenBonus() != 0)
				pc.addAdenBonus(type * getDoll().getEffect().getAdenBonus());

			if (getDoll().getEffect().getItemBonus() != 0)
				pc.addItemBonus(type * getDoll().getEffect().getItemBonus());

			if(pc.getResistance() != null) {
				if (getDoll().getEffect().getPvpDmg() != 0) {
					pc.getResistance().addPVPweaponTotalDamage(type * getDoll().getEffect().getPvpDmg());
				}

				if (getDoll().getEffect().getPvpReduction() != 0) {
					pc.getResistance().addcalcPcDefense(type * getDoll().getEffect().getPvpReduction());
				}
			}

			if (getDoll().getEffect().getMagicHit() != 0) {
				pc.addBaseMagicHitUp(type * getDoll().getEffect().getMagicHit());// 마법 적중
			}

			if (getDoll().getEffect().getExpBonus() != 0) {
				pc.add_item_exp_bonus(type * getDoll().getEffect().getExpBonus());
				if(pc != null) {
					SC_EXP_BOOSTING_INFO_NOTI.send(pc);
				}
			}

			if (getDoll().getEffect().getShortCritical() != 0) {
				pc.add_melee_critical_rate(type * getDoll().getEffect().getShortCritical());
			}

			if (getDoll().getEffect().getLongCritical() != 0) {
				pc.add_missile_critical_rate(type * getDoll().getEffect().getLongCritical());
			}

			if (getDoll().getEffect().getMagicCritical() != 0) {
				pc.add_magic_critical_rate(type * getDoll().getEffect().getMagicCritical());
			}

			if (getDoll().getEffect().isHaste()) {
				if (type == 1) {
					pc.addHasteItemEquipped(1);
					pc.removeHasteSkillEffect();
					pc.setMoveSpeed(1);
					pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
					pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
				} else {
					pc.addHasteItemEquipped(-1);
					if (pc.getHasteItemEquipped() == 0) {
						pc.setMoveSpeed(0);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
					}
				}
			}

			if (getDoll().getEffect().getfourgear()) {
				four_gear(pc, type);
			}

			if (item != null) {
				if (item.get_Doll_Bonus_Value() > 0) {
					doll_bonus_option(pc, type, item.get_Doll_Bonus_Value());
				}
			}
		}
	}

	public void onPerceive(L1PcInstance perceivedFrom) {
		//perceivedFrom.addKnownObject(this);
		//perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
//		perceivedFrom.sendPackets(new S_DollPack(this, perceivedFrom));

		if(getMaster() != null) {
			L1PcInstance master = (L1PcInstance) getMaster();
			if(master != null) {
				if(master.isInvisble()) {
					if ((perceivedFrom.isInParty() && perceivedFrom.getParty().isMember(master)) ||
							(perceivedFrom.getClanid() != 0 && perceivedFrom.getClanid() == master.getClanid())) {
						perceivedFrom.addKnownObject(this);
						broadcastPacketForFindInvis(new S_DollPack(this, perceivedFrom), true);
					} else {
						perceivedFrom.sendPackets(new S_RemoveObject(this));
					}
					//master.sendPackets(new S_DollPack(this, perceivedFrom));
					//perceivedFrom.broadcastPacketForFindInvis(new S_DollPack(this, perceivedFrom), false);
				} else {
					perceivedFrom.addKnownObject(this);
					perceivedFrom.sendPackets(new S_DollPack(this, perceivedFrom));
				}
			}
		}
	}

	public void onItemUse() {
		if (!isActived()) {
			useItem(1, 100);
		}
	}

	public void onGetItem(L1ItemInstance item, int count) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0)
			useItem(1, 100);
	}

	public void startMakeTimer() {
		if (getDoll().getEffect().getMakeTime() > 0 && getDoll().getEffect().getMakeItemId() > 0) {
			int interval = getDoll().getEffect().getMakeTime() * 1000;

			if (_makeTimer == null) {
				_makeTimer = new MakeItemByDollTimer((L1PcInstance) _master, this);
				_timer.scheduleAtFixedRate(_makeTimer, interval, interval);
			}
		}
	}

	public void stopMakeTimer() {
		if (this._makeTimer != null) {
			this._makeTimer.cancel();
			this._makeTimer = null;
		}
	}

	public void startHprTimer() {
		if (getDoll().getEffect().getHprTime() > 0 && getDoll().getEffect().getHpr() > 0) {
			int regen_hpr = getDoll().getEffect().getHpr();
			int interval = getDoll().getEffect().getHprTime() * 1000;

			if (_hprTimer == null) {
				_hprTimer = new HpRegenerationByDoll((L1PcInstance) this._master, this, regen_hpr);
				_timer.scheduleAtFixedRate(_hprTimer, interval, interval);
			}
		}
	}

	public void startBonusHprTimer(int hpr, int interval) {
		if (_hprTimer == null) {
			_hprTimer = new HpRegenerationByDoll((L1PcInstance) this._master, this, hpr);
			_timer.scheduleAtFixedRate(_hprTimer, interval, interval);
		}
	}

	public void stopHprTimer() {
		if (_hprTimer != null) {
			_hprTimer.cancel();
			_hprTimer = null;
		}
	}

	public void startMprTimer() {
		if (getDoll().getEffect().getMprTime() > 0 && getDoll().getEffect().getMpr() > 0) {
			int regen_hpr = getDoll().getEffect().getMpr();
			int interval = getDoll().getEffect().getMprTime() * 1000;

			if (_mprTimer == null) {
				_mprTimer = new MpRegenerationByDoll((L1PcInstance) this._master, this, regen_hpr);
				_timer.scheduleAtFixedRate(_mprTimer, interval, interval);
			}
		}
	}

	public void startBonusMprTimer(int mpr, int inteval) {
		if (_mprTimer == null) {
			_mprTimer = new MpRegenerationByDoll((L1PcInstance) this._master, this, mpr);
			_timer.scheduleAtFixedRate(_mprTimer, inteval, inteval);
		}
	}

	public void stopMprTimer() {
		if (this._mprTimer != null) {
			this._mprTimer.cancel();
			this._mprTimer = null;
		}
	}

	public int getItemObjId() {
		return this._itemObjId;
	}

	public void setItemObjId(int i) {
		this._itemObjId = i;
	}

	public int getItemId() {
		return this._itemId;
	}

	public void setItemId(int i) {
		this._itemId = i;
	}

	public L1MagicDoll getDoll() {
		return this._magicDoll;
	}

	private int _magic_doll_bless;

	public int getMagicDollBless() {
		return _magic_doll_bless;
	}

	public void setMagicDollBless(int i) {
		_magic_doll_bless = i;
	}

	class DollTimer implements Runnable {
		DollTimer() {
		}

		public void run() {
			if (_destroyed) {
				return;
			}

			if (_master instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) _master;
				pc.sendPackets(new S_SkillIconGFX(56, 0, 0, 0));
			}

			deleteDoll();
		}
	}

	/**
	 * TODO 魔法人偶潛力增強效果（自定義不可字符串）屬性如同，CON應該首先更新，STR應該第三個更新才能正確刷新數值（類型）
	 **/
	private void doll_bonus_option(L1PcInstance pc, int type, int bonus) {
		if (pc != null) {
			switch (bonus) {
				case 1:
					pc.add_melee_critical_rate(type * 1);
					break;
				case 2:
					pc.add_missile_critical_rate(type * 1);
					break;
				case 3:
					pc.add_magic_critical_rate(type * 1);
					break;
				case 4:
					pc.addDmgup(type * 1);
					break;
				case 5:
					pc.addBowDmgup(type * 1);
					break;
				case 6:
					pc.addHitup(type * 1);
					break;
				case 7:
					pc.addBowHitup(type * 1);
					break;
				case 8:
					pc.addDamageReductionByArmor(type * 1);
					break;
				case 9:
					pc.getAC().addAc(type * -1);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 10:
					pc.getResistance().addMr(type * 3);
					pc.sendPackets(new S_SPMR(pc));
					break;
				case 11:
					pc.addMaxHp(type * 30);
					pc.sendPackets(new S_HPUpdate(pc));
					break;
				case 12:
					pc.addMaxMp(type * 20);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					break;
				case 13:
					pc.getResistance().addPVPweaponTotalDamage(type * 1);
					break;
				case 14:
					pc.getResistance().addcalcPcDefense(type * 1);
					break;
				case 15:
					pc.getResistance().addFire(type * 10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 16:
					pc.getResistance().addEarth(type * 10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 17:
					pc.getResistance().addWater(type * 10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 18:
					pc.getResistance().addWind(type * 10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 19:
					pc.addWeightReduction(type * 100);
					break;
				case 20:
					pc.add_melee_critical_rate(type * 3);
					break;
				case 21:
					pc.add_missile_critical_rate(type * 3);
					break;
				case 22:
					pc.add_magic_critical_rate(type * 3);
					break;
				case 23:
					pc.addDmgup(type * 2);
					break;
				case 24:
					pc.addBowDmgup(type * 2);
					break;
				case 25:
					pc.addHitup(type * 2);
					break;
				case 26:
					pc.addBowHitup(type * 2);
					break;
				case 27:
					pc.addDamageReductionByArmor(type * 2);
					break;
				case 28:
					pc.getAC().addAc(type * -2);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 29:
					pc.getResistance().addMr(type * 6);
					pc.sendPackets(new S_SPMR(pc));
					break;
				case 30:
					pc.addMaxHp(type * 60);
					pc.sendPackets(new S_HPUpdate(pc));
					break;
				case 31:
					pc.addMaxMp(type * 40);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					break;
				case 32:
					pc.addDg(type * 3);
					break;
				case 33:
					if (type == 1)
						startBonusHprTimer(30, 32 * 1000);
					else
						stopHprTimer();
					break;
				case 34:
					if (type == 1)
						startBonusMprTimer(10, 64 * 1000);
					else
						stopMprTimer();
					break;
				case 35:
					pc.addEffectedER(type * 3);
					break;
				case 36:
					pc.getResistance().addPVPweaponTotalDamage(type * 2);
					break;
				case 37:
					pc.getResistance().addcalcPcDefense(type * 2);
					break;
				case 38:
					pc.add_pvp_mdmg(type * 2);
					break;
				case 39:
					pc.addSpecialResistance(eKind.ABILITY, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 40:
					pc.addSpecialResistance(eKind.SPIRIT, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 41:
					pc.addSpecialResistance(eKind.DRAGON_SPELL, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 42:
					pc.addSpecialResistance(eKind.FEAR, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 43:
					pc.getResistance().addFire(type * 20);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				case 44:
					pc.getResistance().addEarth(type * 20);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 45:
					pc.getResistance().addWater(type * 20);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 46:
					pc.getResistance().addWind(type * 20);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 47:
					pc.getAbility().addAddedStr(type * 1);
					break;
				case 48:
					pc.getAbility().addAddedDex(type * 1);
					break;
				case 49:
					pc.getAbility().addAddedCon(type * 1);
					break;
				case 50:
					pc.getAbility().addAddedWis(type * 1);
					break;
				case 51:
					pc.getAbility().addAddedInt(type * 1);
					break;
				case 52:
					pc.addWeightReduction(type * 200);
					break;
				case 53:
					pc.add_melee_critical_rate(type * 5);
					break;
				case 54:
					pc.add_missile_critical_rate(type * 5);
					break;
				case 55:
					pc.add_magic_critical_rate(type * 5);
					break;
				case 56:
					pc.addDmgup(type * 3);
					break;
				case 57:
					pc.addBowDmgup(type * 3);
					break;
				case 58:
					pc.addHitup(type * 3);
					break;
				case 59:
					pc.addBowHitup(type * 3);
					break;
				case 60:
					pc.getAbility().addSp(type * 3);
					pc.sendPackets(new S_SPMR(pc));
					break;
				case 61:
					pc.addBaseMagicHitUp(type * 3);
					break;
				case 62:
					pc.addDamageReductionByArmor(type * 3);
					break;
				case 63:
					pc.getAC().addAc(type * -3);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 64:
					pc.getResistance().addMr(type * 10);
					pc.sendPackets(new S_SPMR(pc));
					break;
				case 65:
					pc.addMaxHp(type * 150);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					break;
				case 66:
					pc.addMaxMp(type * 100);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					break;
				case 67:
					pc.addDg(type * 5);
					break;
				case 68:
					if (type == 1)
						startBonusHprTimer(50, 32 * 1000);
					else
						stopHprTimer();
					break;
				case 69:
					if (type == 1)
						startBonusMprTimer(12, 64 * 1000);
					else
						stopMprTimer();
					break;
				case 70:
					pc.addEffectedER(type * 5);
					break;
				case 71:
					pc.getResistance().addPVPweaponTotalDamage(type * 3);
					break;
				case 72:
					pc.getResistance().addcalcPcDefense(type * 3);
					break;
				case 73:
					pc.add_pvp_mdmg(type * 5);
					break;
				case 74:
					pc.addSpecialResistance(eKind.ABILITY, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 75:
					pc.addSpecialResistance(eKind.SPIRIT, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 76:
					pc.addSpecialResistance(eKind.DRAGON_SPELL, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 77:
					pc.addSpecialResistance(eKind.FEAR, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 78:
					pc.addSpecialPierce(eKind.ABILITY, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 79:
					pc.addSpecialPierce(eKind.SPIRIT, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 80:
					pc.addSpecialPierce(eKind.DRAGON_SPELL, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 81:
					pc.addSpecialPierce(eKind.FEAR, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 82:
					pc.getResistance().addFire(type * 30);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
				case 83:
					pc.getResistance().addEarth(type * 30);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 84:
					pc.getResistance().addWater(type * 30);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 85:
					pc.getResistance().addWind(type * 30);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 86:
					pc.getResistance().addAllNaturalResistance(type * 10);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 87:
					pc.getAbility().addAddedStr(type * 2);
					break;
				case 88:
					pc.getAbility().addAddedDex(type * 2);
					break;
				case 89:
					pc.getAbility().addAddedCon(type * 2);
					break;
				case 90:
					pc.getAbility().addAddedWis(type * 2);
					break;
				case 91:
					pc.getAbility().addAddedInt(type * 2);
					break;
				case 92:
					pc.addWeightReduction(type * 300);
					break;
				case 93:
					if (type == 1) {
						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					} else {
						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}
					break;
				case 94:
					pc.addDmgup(type * 5);
					break;
				case 95:
					pc.addBowDmgup(type * 5);
					break;
				case 96:
					pc.addHitup(type * 5);
					break;
				case 97:
					pc.addBowHitup(type * 5);
					break;
				case 98:
					pc.getAbility().addSp(type * 5);
					pc.sendPackets(new S_SPMR(pc));
					break;
				case 99:
					pc.addBaseMagicHitUp(type * 5);
					break;
				case 100:
					pc.addDamageReductionByArmor(type * 5);
					break;
				case 101:
					pc.getAC().addAc(type * -5);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 102:
					pc.getResistance().addMr(type * 15);
					pc.sendPackets(new S_SPMR(pc));
					break;
				case 103:
					pc.addMaxHp(type * 300);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					break;
				case 104:
					pc.addMaxMp(type * 200);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					break;
				case 105:
					pc.addDg(type * 10);
					break;
				case 106:
					if (type == 1)
						startBonusHprTimer(150, 32 * 1000);
					else
						stopHprTimer();
					break;
				case 107:
					if (type == 1)
						startBonusMprTimer(30, 64 * 1000);
					else
						stopMprTimer();
					break;
				case 108:
					pc.addEffectedER(type * 12);
					break;
				case 109:
					pc.getResistance().addPVPweaponTotalDamage(type * 7);
					break;
				case 110:
					pc.getResistance().addcalcPcDefense(type * 7);
					break;
				case 111:
					pc.add_pvp_mdmg(type * 10);
					break;
				case 112:
					pc.add_pvp_mdmg_ignore(10);
					break;
				case 113:
					pc.addSpecialResistance(eKind.ABILITY, type * 8);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 114:
					pc.addSpecialResistance(eKind.SPIRIT, type * 8);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 115:
					pc.addSpecialResistance(eKind.DRAGON_SPELL, type * 8);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 116:
					pc.addSpecialResistance(eKind.FEAR, type * 8);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 117:
					pc.addSpecialResistance(eKind.ALL, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 118:
					pc.addSpecialPierce(eKind.ABILITY, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 119:
					pc.addSpecialPierce(eKind.SPIRIT, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 120:
					pc.addSpecialPierce(eKind.DRAGON_SPELL, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 121:
					pc.addSpecialPierce(eKind.FEAR, type * 5);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 122:
					pc.addSpecialPierce(eKind.ALL, type * 6);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 123:
					pc.getResistance().addAllNaturalResistance(type * 30);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 124:
					pc.getAbility().addAddedStr(type * 3);
					break;
				case 125:
					pc.getAbility().addAddedDex(type * 3);
					break;
				case 126:
					pc.getAbility().addAddedCon(type * 3);
					break;
				case 127:
					pc.getAbility().addAddedWis(type * 3);
					break;
				case 128:
					pc.getAbility().addAddedInt(type * 3);
					break;
				case 129:
					pc.getAbility().addAddedStr(type * 1);
					pc.getAbility().addAddedDex(type * 1);
					pc.getAbility().addAddedInt(type * 1);
					pc.getAbility().addAddedWis(type * 1);
					pc.getAbility().addAddedCon(type * 1);
					break;
				case 130:
					// mp흡수(소량) (완료)
					break;
				case 131:
					pc.addWeightReduction(type * 500);
					break;





				case 132:
					pc.add_pvp_dmg_ignore(type * Config.MagicDollInfo.DOLL_PVP);
					break;
				case 133:
					pc.add_immune_minus_per(type * 30);
					break;
				/*
				 * case 134: case 135: break;
				 */
				case 136:
					pc.getAbility().addAddedStr(type * 3);
					pc.getAbility().addAddedDex(type * 3);
					pc.getAbility().addAddedInt(type * 3);
					pc.getAbility().addAddedWis(type * 3);
					pc.getAbility().addAddedCon(type * 3);
					break;
				case 137:
					// hp 흡수 (완료)
					break;
				case 138:
					// mp 흡수 (완료)
					break;
				case 139:
					// 소울 오브 프레임 발동 (완료)
					break;
				case 140:
					// 저지먼트 발동 (완료)
					break;
				case 141:
					// 디케이 포션 발동 (완료)
					break;
				case 142:
					four_gear(pc, type);
					break;
				case 143://1,3단가속
					if (type == 1) {
						if (pc.hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
							pc.killSkillEffectTimer(L1SkillId.STATUS_DRAGON_PEARL);
						}
						pc.setSkillEffect(L1SkillId.STATUS_DRAGON_PEARL, -1);
						pc.sendPackets(new S_Liquor(pc.getId(), 8));
						pc.broadcastPacket(new S_Liquor(pc.getId(), 8));
						pc.setPearl(1);
						pc.sendPackets(new S_ServerMessage(1065, -1));

						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					} else {
						pc.killSkillEffectTimer(L1SkillId.STATUS_DRAGON_PEARL);
						pc.sendPackets(new S_Liquor(pc.getId(), 0));
						pc.setPearl(0);

						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}
					break;
				case 144: //pvp대미지감소무시/내성2
					pc.add_pvp_dmg_ignore(type * Config.MagicDollInfo.DOLL_PVP);
					pc.addSpecialResistance(eKind.ALL, type * 2);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 145: //이뮨효과감소/내성2
					pc.add_immune_minus_per(type * 30);
					pc.addSpecialResistance(eKind.ALL, type * 2);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 146: //4단가속/경험치15
					four_gear(pc, type);
					pc.add_item_exp_bonus(type * 15);
					break;

				case 148://1단 피흡(작업전)
					if (type == 1) {
						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					}else {
						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}


					break;
				case 149://1단 겸치10
					if (type == 1) {
						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					}else {
						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}
					pc.add_item_exp_bonus(type * 10);
					break;
				case 150://1단, 댐감5
					if (type == 1) {
						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					}else {
						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}
					pc.addDamageReductionByArmor(type * 5);
					break;
				case 151: //1단 방5
					if (type == 1) {
						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					}else {
						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}
					pc.getAC().addAc(type * -5);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;
				case 152: //피흡소,겸치10
					pc.add_item_exp_bonus(type * 10);
					break;
				case 153: //피흡소 , 댐감5
					pc.addDamageReductionByArmor(type * 5);
					break;
				case 154://피흡소 방5
					pc.getAC().addAc(type * -5);
					break;

				case 155: //1단가속 피흡대(작업완료)
					if (type == 1) {
						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					}else {
						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}
					break;
				case 156://1단가속 엠흡대(작업완료)
					if (type == 1) {
						pc.addHasteItemEquipped(1);
						pc.removeHasteSkillEffect();
						pc.setMoveSpeed(1);
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));
						pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
					}else {
						pc.addHasteItemEquipped(-1);
						if (pc.getHasteItemEquipped() == 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
					}
					break;
				case 157://겸치25
					pc.add_item_exp_bonus(type * 25);
					break;
				case 158://대미지감소 5%(작업완료)
					break;
				case 159://모적3, 내성3
					pc.addSpecialPierce(eKind.ALL, type * 3);
					pc.addSpecialResistance(eKind.ALL, type * 3);
					SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					break;
				case 160: //힘3,근명5
					pc.getAbility().addAddedStr(type * 3);
					pc.addHitup(type * 5);
					break;
				case 161: //덱3, 원명5
					pc.getAbility().addAddedDex(type * 3);
					pc.addBowHitup(type * 5);
					break;
				case 162: //인3, sp5
					pc.getAbility().addAddedInt(type * 3);
					pc.getAbility().addSp(type * 5);
					break;
				case 163://dg10 er12 mr15
					pc.addDg(type * 10);
					pc.addEffectedER(type * 12);
					pc.getResistance().addMr(type * 10);
					pc.sendPackets(new S_SPMR(pc));
					break;

			}
		}
	}

	private void four_gear(L1PcInstance pc, int type) {
		if (type == 1) {
			pc.setFourgear(true);
			pc.sendPackets(SC_FOURTH_GEAR_NOTI.Fourth_Gear(pc));
			pc.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(pc));
		} else {
			pc.setFourgear(false);
			pc.sendPackets(SC_FOURTH_GEAR_NOTI.Fourth_Gear(pc));
			pc.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(pc));
		}
	}

	@Override
	public int getNpcClassId() {
		return _doll_class_id;
	}
}
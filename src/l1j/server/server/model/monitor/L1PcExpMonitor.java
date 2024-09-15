package l1j.server.server.model.monitor;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PING_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI;
import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI;
import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Info;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Loader;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.CommonUtil;

public class L1PcExpMonitor extends L1PcMonitor {
	private int _old_lawful;
	private long _old_exp;

	private boolean _old_ruler_item;
	private boolean _old_ruler_zone;

	private boolean _PolyMaster_item;
	private boolean _PolyMaster_check;

	private PingCheckHandler pingHandler;
	
	private TitanBeastChaList chalist;
	private PcGoldenBuff pcgoldenbuff;

	

	public L1PcExpMonitor(L1PcInstance pc, int oId) {
		super(oId);
		pingHandler = new PingCheckHandler(pc);
		chalist = new TitanBeastChaList(pc);
		pcgoldenbuff = new PcGoldenBuff(pc); 
	}

	public void onPingResponse() {
		pingHandler.onResponse();
	}

	@Override
	public void execTask(L1PcInstance pc) {
		try {
			pingHandler.onTick();
			chalist.onTick();
			if (pc.isPcBuff()) {
				pcgoldenbuff.onTick();	
			}
			
			
			if (_old_lawful != pc.getLawful()) {
				_old_lawful = pc.getLawful();
				S_Lawful s_lawful = new S_Lawful(pc.getId(), _old_lawful);
				pc.sendPackets(s_lawful);
				pc.broadcastPacket(s_lawful);
				// lawful_bonus(pc);
			}

			if (_old_exp != pc.get_exp()) {
				_old_exp = pc.get_exp();
				pc.onChangeExp();
			}

			if (_old_ruler_item != pc.getInventory().checkItem(900111)) {
				_old_ruler_item = pc.getInventory().checkItem(900111);
				RulerZone(pc);
			}

			if (_old_ruler_zone != pc.getMap().isRuler()) {
				_old_ruler_zone = pc.getMap().isRuler();
				RulerZone(pc);
			}
			
			if (pc.getMapId()==111 ||pc.getMapId() == 12862){
				if (pc.is_dominion_tel()){
					Dominion_Tel(pc);	
				}
			} else if (pc.getMapId() != 111 & pc.getMapId() !=12862){
				Dominion_Tel(pc);	
			}
			
			if (_PolyMaster_item != pc.getInventory().checkItem(4100500)) {
				_PolyMaster_item = pc.getInventory().checkItem(4100500);
				_PolyMaster_check = false;
				PolyMaster(pc);
			}
			if (!_PolyMaster_check && pc.getInventory().checkItem(4100500)) {
				_PolyMaster_check = true;
				// pc.sendPackets(new S_Ability(7, true));
				// pc.sendPackets(new S_OwnCharStatus(pc));
			}
			if (_PolyMaster_item != pc.getInventory().checkItem(4100610)) {
				_PolyMaster_item = pc.getInventory().checkItem(4100610);
				_PolyMaster_check = false;
				PolyMaster(pc);
			}
			if (!_PolyMaster_check && pc.getInventory().checkItem(4100610)) {
				_PolyMaster_check = true;
				// pc.sendPackets(new S_Ability(7, true));
				// pc.sendPackets(new S_OwnCharStatus(pc));
			}
/*			if (pc.getMapId() == 63){
				if (pc.hasSkillEffect(L1SkillId.수경)){
					}
				}
			}*/
			
			Pc_Golden_Buff_Enable_Check(pc);
			if (pc.getMapId() != 5166) {
				if (pc.getLevel() >= 100 || pc.getHighLevel() >= 100) {
					if (pc.getAbility().getCon() > 60 || pc.getAbility().getStr() > 60 || pc.getAbility().getDex() > 60 || pc.getAbility().getCha() > 60 || pc.getAbility().getInt() > 60
							|| pc.getAbility().getWis() > 60) {
						int locx2 = 32723 + CommonUtil.random(10);
						int locy2 = 32851 + CommonUtil.random(10);
						pc.start_teleport(locx2, locy2, 5166, 5, 18339, true, false);
						//pc.setStatReset(true);
						pc.resetStats();
						System.out.println("▶ 檢測到屬性異常，已重置屬性(0):" + pc.getName() + "/[等級]:" + pc.getLevel() + "/[最高等級]:" + pc.getHighLevel() + "/[體質]:" + pc.getAbility().getCon() + "/[力量]:" + pc.getAbility().getStr()
								+ "/[敏捷]:" + pc.getAbility().getDex() + "/[魅力]:" + pc.getAbility().getCha() + "/[智力]:" + pc.getAbility().getInt() + "/[精神]:" + pc.getAbility().getWis());
					}
				} else if (pc.getLevel() >= 90 || pc.getHighLevel() >= 90) {
					if (pc.getAbility().getCon() > 55 || pc.getAbility().getStr() > 55 || pc.getAbility().getDex() > 55 || pc.getAbility().getCha() > 55 || pc.getAbility().getInt() > 55
							|| pc.getAbility().getWis() > 55) {
						int locx2 = 32723 + CommonUtil.random(10);
						int locy2 = 32851 + CommonUtil.random(10);
						pc.start_teleport(locx2, locy2, 5166, 5, 18339, true, false);
						//pc.setStatReset(true);
						pc.resetStats();
						System.out.println("▶ 檢測到屬性異常，已重置屬性(1):" + pc.getName() + "/[等級]:" + pc.getLevel() + "/[最高等級]:" + pc.getHighLevel() + "/[體質]:" + pc.getAbility().getCon() + "/[力量]:" + pc.getAbility().getStr()
								+ "/[敏捷]:" + pc.getAbility().getDex() + "/[魅力]:" + pc.getAbility().getCha() + "/[智力]:" + pc.getAbility().getInt() + "/[精神]:" + pc.getAbility().getWis());
					}
				} else {
					if (pc.getAbility().getCon() > 50 || pc.getAbility().getStr() > 50 || pc.getAbility().getDex() > 50 || pc.getAbility().getCha() > 50 || pc.getAbility().getInt() > 50
							|| pc.getAbility().getWis() > 50) {
						int locx2 = 32723 + CommonUtil.random(10);
						int locy2 = 32851 + CommonUtil.random(10);
						pc.start_teleport(locx2, locy2, 5166, 5, 18339, true, false);
						//pc.setStatReset(true);
						pc.resetStats();
						System.out.println("▶ 檢測到屬性異常，已重置屬性(2):" + pc.getName() + "/[等級]:" + pc.getLevel() + "/[最高等級]:" + pc.getHighLevel() + "/[體質]:" + pc.getAbility().getCon() + "/[力量]:" + pc.getAbility().getStr()
								+ "/[敏捷]:" + pc.getAbility().getDex() + "/[魅力]:" + pc.getAbility().getCha() + "/[智力]:" + pc.getAbility().getInt() + "/[精神]:" + pc.getAbility().getSp());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			onClanBuff(pc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onClanBuff(L1PcInstance pc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null && clan.getOnlineClanMember().length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT && !pc.isClanBuff()) {
			pc.setSkillEffect(L1SkillId.CLANBUFF_YES, 0);
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, true));
			pc.setClanBuff(true);
		} else if (clan != null && clan.getOnlineClanMember().length < Config.ServerAdSetting.CLANBUFFUSERCOUNT && pc.isClanBuff()) {
			pc.killSkillEffectTimer(L1SkillId.CLANBUFF_YES);
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, false));
			pc.setClanBuff(false);
		}
	}
	
	public static void Pc_Golden_Buff_Enable_Check(L1PcInstance pc) {
		if (!pc.isPcBuff()) {
			return;
		}
		ArrayList<Integer> MapList1 = new ArrayList<Integer>();
		int[] maplist1 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP1;
		for (int i = 0; i < maplist1.length;i++) {
			MapList1.add(maplist1[i]);
		}
		ArrayList<Integer> MapList2 = new ArrayList<Integer>();
		int[] maplist2 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP2;
		for (int i = 0; i < maplist2.length;i++) {
			MapList2.add(maplist2[i]);
		}
		ArrayList<Integer> bufflist = new ArrayList<Integer>();
		
		int mapid = pc.getMapId();
//		System.out.println(MapList1);
//		System.out.println(MapList2);
//		System.out.println(mapid);
		if (!pc.isPcGoldenStatus()) {
			if (MapList1.contains(mapid)) {
				if (pc.getAccount().get_Index0_Remain_Time() > 0) {

					int buff1type = pc.getAccount().get_Index0_type(pc);
					int buffgrade = 0;
					int buffindex = 0;
					int buffgroup = 0;
					if (pc.getAccount().get_Index0_1() != 0) {
						buffindex = 1;
						switch(pc.getAccount().get_Index0_1()) {
						case 1:
							buffgrade = 1;
							break;
						case 2:
							buffgrade = 2;
							break;
						case 3:
							buffgrade = 3;
							break;
						}
						int buffid = buffgroup*1000 + buffindex*100 + buff1type * 10 + buffgrade;
						bufflist.add(buffid);
					}
					
					if (pc.getAccount().get_Index0_2() != 0) {
						buffindex = 2;
						switch(pc.getAccount().get_Index0_2()) {
						case 1:
							buffgrade = 1;
							break;
						case 2:
							buffgrade = 2;
							break;
						case 3:
							buffgrade = 3;
							break;
						}
						int buffid = buffgroup*1000 + buffindex*100 + buff1type * 10 + buffgrade;
						bufflist.add(buffid);
					}
					if (pc.getAccount().get_Index0_3() != 0) {
						buffindex = 3;
						switch(pc.getAccount().get_Index0_3()) {
						case 1:
							buffgrade = 1;
							break;
						case 2:
							buffgrade = 2;
							break;
						case 3:
							buffgrade = 3;
							break;
						}
						int buffid = buffgroup*1000 + buffindex*100 + buff1type * 10 + buffgrade;
						bufflist.add(buffid);
					}
					if (bufflist.size() > 0 && !bufflist.isEmpty()) {
						for (int i = 0; i < bufflist.size(); i++) {
							int index = bufflist.get(i);
							Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
							if (info != null) {
								Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, true);
							}
						}
					}
					ArrayList<Integer> hasBufflist = pc.getPcGoldenBuffList();
					for (int i = 0; i < hasBufflist.size() ; i++) {
						if (!bufflist.contains(hasBufflist.get(i))) {
							int index = hasBufflist.get(i);
							Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
							if (info != null) {
								Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
							}
						}
					}
					bufflist = null;
					SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc,0,true);
					SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.send(pc, eUpdateReason.UPDATE);
					pc.set_PcGoldenSstatus(true);
				}
			} else if (MapList2.contains(mapid)) {
				if (pc.getAccount().get_Index1_Remain_Time() > 0) {
					int bufftype = pc.getAccount().get_Index0_type(pc);
					int buffgrade = 0;
					int buffindex = 0;
					int buffgroup = 1;
					if (pc.getAccount().get_Index0_1() != 0) {
						buffindex = 1;
						switch(pc.getAccount().get_Index0_1()) {
						case 1:
							buffgrade = 1;
							break;
						case 2:
							buffgrade = 2;
							break;
						case 3:
							buffgrade = 3;
							break;
						}
						int buffid = buffgroup*1000 + buffindex*100 + bufftype * 10 + buffgrade;
						bufflist.add(buffid);
					}
					
					if (pc.getAccount().get_Index0_2() != 0) {
						buffindex = 2;
						switch(pc.getAccount().get_Index0_2()) {
						case 1:
							buffgrade = 1;
							break;
						case 2:
							buffgrade = 2;
							break;
						case 3:
							buffgrade = 3;
							break;
						}
						int buffid = buffgroup*1000 + buffindex*100 + bufftype * 10 + buffgrade;
						bufflist.add(buffid);
					}
					if (pc.getAccount().get_Index0_3() != 0) {
						buffindex = 3;
						switch(pc.getAccount().get_Index0_3()) {
						case 1:
							buffgrade = 1;
							break;
						case 2:
							buffgrade = 2;
							break;
						case 3:
							buffgrade = 3;
							break;
						}
						int buffid = buffgroup*1000 + buffindex*100 + bufftype * 10 + buffgrade;
						bufflist.add(buffid);
					}
					if (bufflist.size() > 0 && !bufflist.isEmpty()) {
						for (int i = 0; i < bufflist.size(); i++) {
							int index = bufflist.get(i);
							Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
							if (info != null) {
							Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, true);
							}
						}
					}
					ArrayList<Integer> hasBufflist = pc.getPcGoldenBuffList();
					for (int i = 0; i < hasBufflist.size() ; i++) {
						if (!bufflist.contains(hasBufflist.get(i))) {
							int index = hasBufflist.get(i);
							Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
							if (info != null) {
								Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
							}
						}
					}
					bufflist = null;
					SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc,1,true);
					SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.send(pc, eUpdateReason.UPDATE);
					pc.set_PcGoldenSstatus(true);

				}
			}
		}
		if (pc.isPcGoldenStatus()) {
			if (!MapList1.contains(mapid) && !MapList2.contains(mapid) ) {
				SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc, 0, false);
				pc.set_PcGoldenSstatus(false);
				ArrayList<Integer> bufflistdel = pc.getPcGoldenBuffList();
				if (bufflistdel.size() !=0 && !bufflistdel.isEmpty()) {
					for (int i = 0; i < bufflistdel.size() ;i++) {
						int index = bufflistdel.get(i);
						Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
						if (info != null) {
							Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
						}
					}
				}
			} else {
				
			}
		}
	}

	private void Dominion_Tel(L1PcInstance pc) {
		if (pc.getMapId() == 111 || pc.getMapId() == 12862){
			if (pc.is_dominion_tel()){
//				pc.setSkillEffect(L1SkillId.DOMINION_TEL, -1);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.NEW);
				noti.set_spell_id(L1SkillId.DOMINION_TEL);
				noti.set_duration(1);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
				noti.set_on_icon_id(L1SkillId.DOMINION_TEL);
				noti.set_off_icon_id(0x00);
				noti.set_icon_priority(3);
				noti.set_tooltip_str_id(9051);//^EXP +20%^오만의 탑 정상 텔레포트 가능
				noti.set_new_str_id(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
			} 
		} else {
			pc.removeSkillEffect(L1SkillId.DOMINION_TEL);
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.DOMINION_TEL);
			noti.set_off_icon_id(0x00);
			noti.set_end_str_id(0);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		}
	}
	
	private void RulerZone(L1PcInstance pc) {
		if (pc.getMap().isRuler() && pc.getInventory().checkItem(900111)) {
			pc.setSkillEffect(L1SkillId.TELEPORT_RULER, -1);
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.NEW);
			noti.set_spell_id(L1SkillId.TELEPORT_RULER);
			noti.set_duration(1);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
			noti.set_on_icon_id(L1SkillId.TELEPORT_RULER);
			noti.set_off_icon_id(0x00);
			noti.set_icon_priority(3);
			noti.set_tooltip_str_id(5119);
			noti.set_new_str_id(0);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		} else {
			pc.removeSkillEffect(L1SkillId.TELEPORT_RULER);
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.TELEPORT_RULER);
			noti.set_off_icon_id(0x00);
			noti.set_end_str_id(0);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);
		}
	}

	private void PolyMaster(L1PcInstance pc) {
		// pc.sendPackets(new S_Ability(7, false));
		pc.sendPackets(new S_OwnCharStatus(pc));
		if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER) && !pc.getInventory().checkItem(4100500)) {
			pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
		}
		if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2) && !pc.getInventory().checkItem(4100610)) {
			pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
		}
	}
	
	

	static class LawfulBonusInfo {
		int ac;
		int mr;
		int sp;
		int add_damage;
		int min_lawful;
		int max_lawful;
		int level_num;

		LawfulBonusInfo(int ac, int mr, int sp, int add_damage, int min_lawful, int max_lawful, int level_num) {
			this.ac = ac;
			this.mr = mr;
			this.sp = sp;
			this.add_damage = add_damage;
			this.min_lawful = min_lawful;
			this.max_lawful = max_lawful;
			this.level_num = level_num;
		}

		boolean is_range(L1PcInstance pc) {
			int lawful = pc.getLawful();
			return lawful >= min_lawful && lawful <= max_lawful;
		}

		void off_update(L1PcInstance pc) {
			pc.getAC().addAc(ac * -1);
			pc.getResistance().addMr(mr * -1);
			pc.getAbility().addSp(sp * -1);
			pc.addDmgRate(add_damage * -1);
			pc.addBowDmgRate(add_damage * -1);
			// do off
			pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, level_num, false));
		}

		void on_update(L1PcInstance pc) {
			int current_bapo_level = pc.get_bapo_level();
			if (current_bapo_level == level_num)
				return;

			LawfulBonusInfo prev_bonus = m_lawful_bonuses[current_bapo_level];
			prev_bonus.off_update(pc);

			// do on
			pc.set_bapo_level(level_num);
			pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, level_num, true));

			pc.getAC().addAc(ac);
			pc.getResistance().addMr(mr);
			pc.getAbility().addSp(sp);
			pc.addDmgRate(add_damage);
			pc.addBowDmgRate(add_damage);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	private static final LawfulBonusInfo[] m_lawful_bonuses;

	public static final int NONE_STATE_BAPO_LEVEL = 6;
	static {
		// ac, mr, sp, add_damage, min_lawful, max_lawful, level_num
		m_lawful_bonuses = new LawfulBonusInfo[] { new LawfulBonusInfo(-2, 3, 0, 0, 1001, 10000, 0), new LawfulBonusInfo(-4, 6, 0, 0, 10001, 20000, 1),
				new LawfulBonusInfo(-6, 9, 0, 0, 20000, 99999, 2), new LawfulBonusInfo(0, 0, 1, 1, -10000, -1, 3), new LawfulBonusInfo(0, 0, 2, 3, -20000, -10001, 4),
				new LawfulBonusInfo(0, 0, 3, 5, -99999, -20001, 5), new LawfulBonusInfo(0, 0, 0, 0, 1000, 0, NONE_STATE_BAPO_LEVEL), };

	}

	/*
	 * private void lawful_bonus(L1PcInstance pc){ if(pc.getLevel() < Config.ServerAdSetting.NEWPLAYERPROTECTION){ m_lawful_bonuses[NONE_STATE_BAPO_LEVEL].on_update(pc); return; }
	 * 
	 * for(LawfulBonusInfo bInfo : m_lawful_bonuses){ if(bInfo.is_range(pc)){ bInfo.on_update(pc); break; } } }
	 */

	private static class PingCheckHandler {
		private long requestMillis;
		private final L1PcInstance pc;
		private int remainTick;

		PingCheckHandler(L1PcInstance pc) {
			this.pc = pc;
			this.onInitializeTick();
		}

		private void onInitializeTick() {
			// remainTick = 600;//10분 외부화시 *2로 해주면 편하다.
			remainTick = Config.Login.PINGCHECK_SECOND;
		}

		private void onTick() {
			if (remainTick <= 0) {
				return;
			}

			if (--remainTick != 0) {
				return;
			}
			try {
				onRequest();
				/**
				 * 상점강제저장 5분마다..렉발생되서 주석처리함
				 */
				// ShopBuyLimitInfo.getInstance().save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onRequest() {
			if (pc.getNetConnection() == null || pc.getNetConnection().isClosed()) {
				return;
			}

			requestMillis = System.currentTimeMillis();
			SC_PING_REQ req = SC_PING_REQ.newInstance();
			req.set_transaction_id(pc.getId());
			pc.sendPackets(req, MJEProtoMessages.SC_PING_REQ);
		}

		private void onResponse() {
			try {
				long responseMillis = System.currentTimeMillis() - requestMillis;
				if (responseMillis >= Config.Login.PINGCHECK) { // 檢查延遲
					pc.sendPackets(String.format("\f3您的網路連接狀態瞬間不穩定。(網路延遲: %,dms)", responseMillis));
				}
			} finally {
				onInitializeTick();
			}

		}
		
		
	}
	private static class PcGoldenBuff{
		private final L1PcInstance pc;
		private int remainTick;
		private int saveTick;
		
		PcGoldenBuff(L1PcInstance pc){
			this.pc = pc;
			this.onInitializeTick();
			this.onInitializeSaveTick();
		}
		
		private void onInitializeTick() {
			remainTick = 10;
		}
		private void onInitializeSaveTick() {
			saveTick = 60;
		}
		private void onTick() {
			if (remainTick <= 0) {
				onInitializeTick();
				return;
			}
			if (--remainTick != 0) {
				return ;
			}
			
			try {
				onMap();
				saveTick--;
				if (saveTick <= 0) {
//					System.out.println("패킷확인");
					pc.getAccount().update_Index0_Remain_Time();
					pc.getAccount().update_Index1_Remain_Time();
					onInitializeSaveTick();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void onMap() {
//			System.out.println("패킷확인");
			ArrayList<Integer> MapList1 = new ArrayList<Integer>();
			int[] maplist1 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP1;
			for (int i = 0; i < maplist1.length; i++) {
				MapList1.add(maplist1[i]);
			}
			ArrayList<Integer> MapList2 = new ArrayList<Integer>();
			int[] maplist2 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP2;
			for (int i = 0; i < maplist2.length; i++) {
				MapList2.add(maplist2[i]);
			}
			int mapId = pc.getMapId();
			if (MapList1.contains(mapId)) {
				if (pc.getAccount().get_Index0_Remain_Time() >= 10) {
					pc.getAccount().use_Index0_Time(10);
//					pc.getAccount().get_Index0_Remain_Time());
				} else {
					pc.getAccount().use_Index0_Time(pc.getAccount().get_Index0_Remain_Time());
					if(pc.isPcGoldenStatus()) {
						SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc, 0, false);
						pc.set_PcGoldenSstatus(false);
						ArrayList<Integer> bufflistdel = pc.getPcGoldenBuffList();
						if (bufflistdel.size() !=0 && !bufflistdel.isEmpty()) {
							for (int i = 0; i < bufflistdel.size() ;i++) {
								int index = bufflistdel.get(i);
								Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
								if (info != null) {
									Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
								}
							}
						}
					}
				}
			} else if (MapList2.contains(mapId)) {
				if (pc.getAccount().get_Index1_Remain_Time() >= 10) {
					pc.getAccount().use_Index1_Time(10);
//					System.out.println("2"+pc.getAccount().get_Index1_Remain_Time());
				} else {
					pc.getAccount().use_Index1_Time(pc.getAccount().get_Index1_Remain_Time());
					if(pc.isPcGoldenStatus()) {
						SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.send(pc, 0, false);
						pc.set_PcGoldenSstatus(false);
						ArrayList<Integer> bufflistdel = pc.getPcGoldenBuffList();
						if (bufflistdel.size() !=0 && !bufflistdel.isEmpty()) {
							for (int i = 0; i < bufflistdel.size() ;i++) {
								int index = bufflistdel.get(i);
								Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
								if (info != null) {
									Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
								}
							}
						}
					}
				}
			}
			
			
		}
	}
	
	private static class TitanBeastChaList {
		private final L1PcInstance pc;
		private int remainTick;

		TitanBeastChaList(L1PcInstance pc) {
			this.pc = pc;
			this.onInitializeTick();
		}

		private void onInitializeTick() {
			remainTick = 2;
		}

		private void onTick() {
			if (remainTick <= 0) {
				onInitializeTick();
				return;
			}

			if (--remainTick != 0) {
				return;
			}

			try {
				onTitanBeast();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onTitanBeast() {
			if (pc.getTitanBeastChaList() != null) {
				if (pc.getTitanBeastChaList().size() > 1) {
					pc.setTitanBeast(true);
				} else {
					pc.setTitanBeast(false);
				}
				pc.getTitanBeastChaList().clear();
			}

		}
	}
	
	
	
}

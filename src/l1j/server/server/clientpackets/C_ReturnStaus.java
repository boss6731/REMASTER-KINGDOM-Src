package l1j.server.server.clientpackets;

import java.util.Random;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1ReturnStatTemp;
import l1j.server.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CheckInitStat;
import l1j.server.server.utils.CommonUtil;

public class C_ReturnStaus extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final int SICODE_ENDED = 8;

	// 分配靈藥後升級1級
	private static final int SICODE_ELIXIR_CONTINUE = 14;

	// 含靈藥在內完成2個屬性時
	private static final int SICODE_ELIXIR_ENDED = 15;
	
	
	public C_ReturnStaus(byte[] decrypt, GameClient client) {
		super(decrypt);
		int type = readC();
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.getReturnStat() == 0) {
			return;
		}

		if (type == 1) {
			pc.rst = new L1ReturnStatTemp();
			pc.rst.remainElixirAmount = pc.getElixirStats();
			short init_hp = 0, init_mp = 0;

			byte str = (byte) readC();
			byte intel = (byte) readC();
			byte wis = (byte) readC();
			byte dex = (byte) readC();
			byte con = (byte) readC();
			byte cha = (byte) readC();

			if (isMinus(pc, str, dex, con, wis, intel, cha) || (str + intel + wis + dex + con + cha > 75)) {
				client.kick();
				try {
					client.kick();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("▶ 屬性錯誤驅逐: " + pc.getName());
			}
			
			pc.rst.basestr = str;
			pc.rst.baseint = intel;
			pc.rst.basewis = wis;
			pc.rst.basedex = dex;
			pc.rst.basecon = con;
			pc.rst.basecha = cha;
			
			pc.getAbility().init();
			
			pc.getAbility().setBaseStr(str);
			pc.getAbility().setBaseInt(intel);
			pc.getAbility().setBaseWis(wis);
			pc.getAbility().setBaseDex(dex);
			pc.getAbility().setBaseCon(con);
			pc.getAbility().setBaseCha(cha);
					
			pc.rst.level = 1;

			if (pc.isCrown()) { // CROWN
				init_hp = 14;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 11:
					init_mp = 2;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 3;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 4;
					break;
				default:
					init_mp = 2;
				break;
				}
			} else if (pc.isKnight()) { // KNIGHT
				init_hp = 16;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
				break;
				}
			} else if (pc.isElf()) { // ELF
				init_hp = 15;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 4;
				break;
				}
			} else if (pc.isWizard()) { // WIZ
				init_hp = 12;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 6;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 8;
					break;
				default:
					init_mp = 6;
				break;
				}
			} else if (pc.isDarkelf()) { // DE
				init_hp = 12;
			//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 10:
				case 11:
					init_mp = 3;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 3;
				break;
				}
			} else if (pc.isDragonknight()) { // 龍騎士
				init_hp = 16;
				init_mp = 2;
			} else if (pc.isBlackwizard()) { // 幻術師
				init_hp = 14;
			//	switch (pc.getAbility().getBaseWis()) {
			switch (pc.rst.basewis) {
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 5;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 5;
				break;
				}
			} else if (pc.isWarrior()) { // 戰士
				init_hp = 16;
	//	switch (pc.getAbility().getBaseWis()) {
				switch (pc.rst.basewis) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			} else if (pc.isFencer()) {
				init_hp = 16;
				switch (pc.rst.basewis) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			} else if (pc.isLancer()) {
				init_hp = 16;
				switch (pc.rst.basewis) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			}

			pc.rst.baseHp = init_hp;
			pc.rst.baseMp = init_mp;
			pc.rst.upMp = 0;
			pc.rst.upHp = 0;
			pc.rst.ac = 10;
			
			if(pc.getHighLevel() == 1) {
				onEndStatus(client, pc);
			}else {
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP), true);
			}
		} else if (type == 2) {
			int levelup = readC();
			try{
				if((pc.rst.level == pc.getHighLevel() && !latestScene(levelup))) {
					//System.out.println("1 " + levelup);
					//System.out.println(String.format("1 %s 因為錯誤的等級而重新開始重置。(原始等級 : %d, 嘗試等級 : %d)", pc.getName(), pc.getHighLevel(), pc.rst.level));
					//pc.sendPackets(new S_SystemMessage("錯誤的升級。"));
					return;
				}
				
				if (pc.rst.level > pc.getHighLevel() || (pc.rst.level == pc.getHighLevel() && !latestScene(levelup))) {
					/*System.out.println(String.format("2 %s 因為錯誤的等級而重新開始重置。(原始等級 : %d, 嘗試等級 : %d) %d", pc.getName(), pc.getHighLevel(), pc.rst.level, levelup));
					pc.sendPackets(new S_SystemMessage("錯誤的屬性。"));
					client.kick();*/
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			if (pc.rst.level <= L1PcInstance.STAT_INCREASE_LEVEL
					&& levelup != 0
					&& levelup != 7
					&& levelup != SICODE_ENDED 
					&&  pc.getElixirStats() == 0)
			{
				System.out.println(String.format("%s 嘗試在 %d 級以下添加屬性。嘗試等級: %d", pc.getName(), L1PcInstance.STAT_INCREASE_LEVEL, pc.rst.level));
				pc.sendPackets(String.valueOf(new S_SystemMessage("錯誤的屬性。")));
				try {
					client.kick();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			switch (levelup) {
			case 0:
				onLevelUp(pc);
				break;
			case 1:
				pc.rst.str = pc.rst.str+ 1;
				if(checkingExternal(client, pc, pc.rst.str, "str")) {
					return;
				}
				onLevelUp(pc);
				break;
			case 2:
				pc.rst.Int =pc.rst.Int+ 1;
				if(checkingExternal(client, pc, pc.rst.Int, "Int")) {
					return;
				}
				onLevelUp(pc);
				break;
			case 3:
				pc.rst.wis =pc.rst.wis+ 1;
				if(checkingExternal(client, pc, pc.rst.wis, "wis")) {
					return;
				}
				onLevelUp(pc);
				break;
			case 4:
				pc.rst.dex = pc.rst.dex + 1;
				if(checkingExternal(client, pc, pc.rst.dex, "dex")) {
					return;
				}
				onLevelUp(pc);
				break;
			case 5:
				pc.rst.con =pc.rst.con+ 1;
				if(checkingExternal(client, pc, pc.rst.con, "con")) {
					return;
				}
				onLevelUp(pc);
				break;
			case 6:
				pc.rst.cha =pc.rst.cha+ 1;
				if(checkingExternal(client, pc, pc.rst.cha, "cha")) {
					return;
				}
				onLevelUp(pc);
				break;
			case 7:
				if (pc.rst.level > 40) {
					return;
				}
				if ((pc.rst.level + 10) < pc.getHighLevel()) {
					for (int m = 0; m < 10; m++)
						statup(pc);
					pc.sendPackets(new S_ReturnedStat(pc,S_ReturnedStat.LEVELUP), true);
				}
				break;
			case SICODE_ENDED:
				onSiEnded(client, pc);
				break;
			case SICODE_ELIXIR_CONTINUE: {
				onSiElixirContinue(client, pc);
				break;
			}
			case SICODE_ELIXIR_ENDED:{
				onSiElixirEnded(client, pc);
				break;
			}
			
			}
		}
		
		
		
		
		
		
	
	}
	
	private boolean latestScene(int sicode) {
		return sicode == SICODE_ENDED || sicode == SICODE_ELIXIR_ENDED;
	}

	private void onSiEnded(GameClient client, L1PcInstance pc) {
		int statusup = readC();
		if (pc.rst.level > L1PcInstance.STAT_INCREASE_LEVEL )	{
			switch (statusup) {
			case 1:
				pc.rst.str = pc.rst.str + 1;
				if(checkingExternal(client, pc, pc.rst.str, "str")) {
					return;
				}
				break;
			case 2:
				pc.rst.Int = pc.rst.Int + 1;
				if(checkingExternal(client, pc, pc.rst.Int, "Int")) {
					return;
				}
				break;
			case 3:
				pc.rst.wis = pc.rst.wis + 1;
				if(checkingExternal(client, pc, pc.rst.wis, "wis")) {
					return;
				}
				break;
			case 4:
				pc.rst.dex = pc.rst.dex + 1;
				if(checkingExternal(client, pc, pc.rst.dex, "dex")) {
					return;
				}
				break;
			case 5:
				pc.rst.con = pc.rst.con + 1;
				if(checkingExternal(client, pc, pc.rst.con, "con")) {
					return;
				}
				break;
			case 6:
				pc.rst.cha = pc.rst.cha + 1;
				if(checkingExternal(client, pc, pc.rst.cha, "cha")) {
					return;
				}
				break;
			}
		}
		onEndStatus(client, pc);
	}
	
	private void onSiElixirEnded(GameClient client, L1PcInstance pc) {
		int str = readC();
		int intel = readC();
		int wis = readC();
		int dex = readC();
		int con = readC();
		int cha = readC();
		onElixirLevelup(pc, str, dex, con, intel, wis, cha, false);
		onEndStatus(client, pc);
	}
	
	private void onSiElixirContinue(GameClient client, L1PcInstance pc) {
		int str = readC();
		int intel = readC();
		int wis = readC();
		int dex = readC();
		int con = readC();
		int cha = readC();
		onElixirLevelup(pc, str, dex, con, intel, wis, cha, true);
	}
	
	private void onEndStatus(GameClient client, L1PcInstance pc) {
		try {
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.END, 0), true);
			
			pc.set_exp(pc.getReturnStat());
			pc.addBaseMaxHp((short) (pc.rst.baseHp - pc.getBaseMaxHp()));
			pc.addBaseMaxMp((short) (pc.rst.baseMp - pc.getBaseMaxMp()));

			pc.addBaseMaxHp((short) pc.rst.upHp);
			pc.addBaseMaxMp((short) pc.rst.upMp);
			
			pc.setCurrentHp(pc.getMaxHp());
			pc.setCurrentMp(pc.getMaxHp());
			
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));

			int locx2 = 32625 + CommonUtil.random(5);
			int locy2 = 32776 + CommonUtil.random(5);
			pc.start_teleport(locx2, locy2, 4, pc.getHeading(), 169, true, false);
			
			pc.setStatReset(false);
			pc.getAbility().init();
			
			pc.getAbility().setBaseStr(pc.rst.basestr);
			pc.getAbility().setBaseInt(pc.rst.baseint);
			pc.getAbility().setBaseWis(pc.rst.basewis);
			pc.getAbility().setBaseDex(pc.rst.basedex);
			pc.getAbility().setBaseCon(pc.rst.basecon);
			pc.getAbility().setBaseCha(pc.rst.basecha);
			
			pc.getAbility().addStr((byte) pc.rst.str);
			pc.getAbility().addInt((byte) pc.rst.Int);
			pc.getAbility().addWis((byte) pc.rst.wis);
			pc.getAbility().addDex((byte) pc.rst.dex);
			pc.getAbility().addCon((byte) pc.rst.con);
			pc.getAbility().addCha((byte) pc.rst.cha);
			
			pc.resetBaseAc();
			pc.getAC().setAc(pc.getBaseAc());
			
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.RenewStat();
			
			pc.checkStatus();
			if(!CheckInitStat.CheckPcStat(pc)){
				client.kick();
				return;
			}
			
//			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
//			if (Info != null) {
//				Info.set_bless(0);
//				Info.set_lucky(0);
//				Info.set_vital(0);
//				Info.set_invoke(0);
//				Info.set_restore(0);
//				Info.set_potion(0);
//				Info.set_invoke_val_1(0);
//				Info.set_invoke_val_2(0);
//				Info.set_potion_val_1(0);
//				Info.set_potion_val_2(0);
//				AinhasadSpecialStatLoader.getInstance().updateSpecialStat(pc);
//			}
			
			pc.setReturnStat(0);
			pc.save();
			pc.rst = null;
//			System.out.println("테스트1");
//			C_NewCharSelect.restartProcess(pc);
//			System.out.println("테스트2");
			Restar_World(pc);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private boolean elixirlevel(int level) {
		switch(level) {
		case 50:case 52:case 54:case 56:case 58:
		case 60:case 62:case 64:case 66:case 68:
		case 70:case 72:case 74:case 76:case 78:
		case 80:case 82:case 84:case 86:case 88:
		case 90:case 92:case 94:case 96:case 98:
		case 100:
			return true;
		}
		return false;
	}

	private void onLevelUp(L1PcInstance pc) { // 靈藥額外應用
		int elixerCount = pc.getElixirStats();
//		if (elixerCount <= 0) {
//			return;
//		}
		if(elixirlevel(pc.rst.level + 1)) {
//			System.out.println(pc.rst.level + 1);
			if(pc.rst.remainElixirAmount > 0) {
//				System.out.println(pc.rst.remainElixirAmount);
				if(pc.rst.level == 99 && elixerCount > 27) {
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.ELIXER, elixerCount > 29 ? 3 : elixerCount > 28 ? 2 : 1), true);
				} else if (pc.rst.level == 89 && elixerCount > 21) {
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.ELIXER, elixerCount > 21 ? 2 : 1), true);
				} else if (pc.rst.level == 79 && elixerCount > 15) {
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.ELIXER, elixerCount > 16 ? 2 : 1), true);
				} else if (pc.rst.level == 49 && elixerCount > 1){
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.ELIXER, 1), true);	
				} else {
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.ELIXER, 1), true);
				}
			}
		}
		
		statup(pc);
		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP), true);
		
		
//				statup(pc);
//				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
//				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.NEW_ADDSTAT));	
//				return;
//			}
//		}
//		statup(pc);
//		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
	}
	
	
	
/*	private void onLevelUp(L1PcInstance pc) {
		if(elixirlevel(pc.rst.level + 1)) {
			if(pc.rst.remainElixirAmount > 0) {
				statup(pc);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.NEW_ADDSTAT));	
				return;
			}
		}
		statup(pc);
		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP));
	}
*/	
	private boolean checking(L1PcInstance pc, int stat){
		int maximum = 50;
		if(pc.getHighLevel() >= 90){
			maximum = 55;
		} else if(pc.getHighLevel() >= 100){
			maximum = 60;
		}
		return stat > maximum || stat < 1;
	}
	
	private boolean checkingExternal(GameClient clnt, L1PcInstance pc, int stat, String name) {
		if(checking(pc, stat)) {
			System.out.print(String.format("%s 屬性超過檢測.. %s : %d", pc.getName(), name, stat));
			try {
				clnt.kick();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			return true;
		}
		return false;
	}

	private static boolean isMinus(L1PcInstance pc, int str, int dex, int con, int wis, int intel, int cha){
		return str <= 0 || 
				dex <= 0 || 
				con <= 0 || 
				wis <= 0 || 
				intel <= 0 || 
				cha <= 0 ||
				str < pc.rst.str + pc.rst.basestr ||
				dex < pc.rst.dex + pc.rst.basedex ||
				con < pc.rst.con + pc.rst.basecon ||
				wis < pc.rst.wis + pc.rst.basewis ||
				intel < pc.rst.Int + pc.rst.baseint ||
				cha < pc.rst.cha + pc.rst.basecha;
	}
	
	private static int remainStat(L1PcInstance pc, int str, int dex, int con, int wis, int intel, int cha) {
		return (str - (pc.rst.str + pc.rst.basestr)) +
				(dex - (pc.rst.dex + pc.rst.basedex)) +
				(con - (pc.rst.con + pc.rst.basecon)) +
				(wis - (pc.rst.wis + pc.rst.basewis)) +
				(intel - (pc.rst.Int + pc.rst.baseint)) +
				(cha - (pc.rst.cha + pc.rst.basecha));
		
	}
	
	private void safeKick(GameClient clnt, String message) {
		try {
			clnt.kick();
			clnt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(message);
	}

	private void onElixirLevelup(L1PcInstance pc, int str, int dex, int con, int intel, int wis, int cha, boolean next) {
		try {

			if(isMinus(pc, str, dex, con, wis, intel, cha)) {
				safeKick(pc.getNetConnection(), "▶ 屬性錯誤踢出 : " + pc.getName());
				return;
			}
			if(pc.rst.remainElixirAmount <= 0) {
				safeKick(pc.getNetConnection(), "▶ 靈藥數量錯誤踢出 : " + pc.getName());
				return;
			}
			if(!elixirlevel(pc.rst.level)) {
				safeKick(pc.getNetConnection(), "▶ 靈藥等級錯誤踢出 : " + pc.getName() + ", " + pc.rst.level);
				return;
			}

			if(remainStat(pc, str, dex, con, wis, intel, cha) > 5) {
				safeKick(pc.getNetConnection(), String.format("▶ 靈藥屬性錯誤踢出 : %s\r\n舊: %d %d %d %d %d %d\r\n新: %d %d %d %d %d %d",
				// 這裡應該會有更多的程式碼，請提供完整的程式碼以便翻譯。
			}
						pc.getName(),
						pc.rst.str + pc.rst.basestr, pc.rst.dex + pc.rst.basedex, pc.rst.con + pc.rst.basecon, pc.rst.wis + pc.rst.basewis, pc.rst.Int + pc.rst.baseint, pc.rst.cha + pc.rst.basecha,
						str, dex, con, wis, intel, cha
						));
				return;
			}



/*		safeKick(pc.getNetConnection(), String.format("▶ 靈藥屬性錯誤踢出 : %s\r\n舊: %d %d %d %d %d %d\r\n新: %d %d %d %d %d %d",
			pc.getName(),
			pc.rst.str + pc.rst.basestr, pc.rst.dex + pc.rst.basedex, pc.rst.con + pc.rst.basecon, pc.rst.wis + pc.rst.basewis, pc.rst.Int + pc.rst.baseint, pc.rst.cha + pc.rst.basecha,
							str, dex, con, wis, intel, cha
			));
					return;
				}
			} else if(remainStat(pc, str, dex, con, wis, intel, cha) > 3) {
					System.out.println(pc.rst.level);
			safeKick(pc.getNetConnection(), String.format("▶ 靈藥屬性錯誤踢出 : %s\r\n舊: %d %d %d %d %d %d\r\n新: %d %d %d %d %d %d",
			pc.getName(),
			pc.rst.str + pc.rst.basestr, pc.rst.dex + pc.rst.basedex, pc.rst.con + pc.rst.basecon, pc.rst.wis + pc.rst.basewis, pc.rst.Int + pc.rst.baseint, pc.rst.cha + pc.rst.basecha,
			str, dex, con, wis, intel, cha
			));
					return;
					}
		*/
	
			--pc.rst.remainElixirAmount;
			pc.rst.str = str - pc.rst.basestr;
			pc.rst.dex = dex - pc.rst.basedex;
			pc.rst.con = con - pc.rst.basecon;
			pc.rst.wis = wis - pc.rst.basewis;
			pc.rst.Int = intel - pc.rst.baseint;
			pc.rst.cha = cha - pc.rst.basecha;
			statup(pc);
			if(next) {
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LEVELUP), true);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void statup(L1PcInstance pc) {
		Random random = new Random();
		pc.rst.level += 1;

		int minmp = CalcStat.MinincreaseMp(pc.getType(), (byte) (pc.rst.wis + pc.rst.basewis));
		int maxmp = CalcStat.MaxincreaseMp(pc.getType(), (byte) (pc.rst.wis + pc.rst.basewis));
		short randomHp = (short) (CalcStat.PureHp(pc.getType(), (byte) (pc.rst.con + pc.rst.basecon)) + random.nextInt(2));
		int randomMp = (int) ((Math.random() * (maxmp - minmp)) + minmp);
		if (minmp == 0)
			randomMp = random.nextInt(maxmp + 1);

		pc.rst.upHp = pc.rst.upHp + randomHp;
		pc.rst.upMp = pc.rst.upMp + randomMp;
	}

	private static void Restar_World(final L1PcInstance pc) {
		pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
		pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("\f3請稍候，屬性將被應用。", MJSimpleRgb.green(), 5));
		pc.sendPackets("\f3請稍候，屬性將被應用。");
		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				GameClient clnt = pc.getNetConnection();
				String name = pc.getName();
				int x = pc.getX();
				int y = pc.getY();
				int mapId = pc.getMapId();
				C_NewCharSelect.restartProcess(pc);// 先重置
				try {
					Thread.sleep(700L);//0.5秒
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
					l1j.server.server.clientpackets.C_LoginToServer.doEnterWorld(name, clnt, false, x, y, mapId);// 再次嘗試連接
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1500);//1.5秒
	}
}
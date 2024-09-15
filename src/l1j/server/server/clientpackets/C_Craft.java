package l1j.server.server.server.clientpackets;

import java.io.IOException;


import l1j.server.server.server.datatables.CharacterSlotItemTable;
import l1j.server.server.server.datatables.ClanBuffTable;
import l1j.server.server.server.datatables.ClanBuffTable.ClanBuff;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_CharStat;
import l1j.server.server.serverpackets.S_EinhasadClanBuff;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Pledge;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SlotChange;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_Craft extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final int NewStat = 228;
	private static final int CLAN_BUFF = 140; // 血盟增益
	//    private static final int CLAN_RANKING = 146;
	private static final int EQUIPMENT = 33; // 設備交換

	// TODO 血盟增益更新 2017-11-12
	private static final int CLAN_BUFF_CHANGE = 0xf9;
	private static final int CLAN_BUFF_CHOICE = 0xf8;

	public C_Craft(byte[] data, GameClient client) throws IOException {
		super(data);
		if (client == null) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		int type = readC();

		if (type != NewStat && pc == null)
			return;

//        System.out.println("C_Craft 類型: " +type);

		switch (type) {
// 			TODO 血盟增益更新 2017-11-12
		case CLAN_BUFF_CHANGE: {
			L1Clan clan = pc.getClan();
			if (clan == null || !pc.getInventory().checkItem(40308, 300000))
				return;
			pc.getInventory().consumeItem(40308, 300000);
			clan.setEinhasadBlessBuff(0);
			clan.setBuffFirst(ClanBuffTable.getRandomBuff(clan));
			clan.setBuffSecond(ClanBuffTable.getRandomBuff(clan));
			clan.setBuffThird(ClanBuffTable.getRandomBuff(clan));
			pc.sendPackets(new S_EinhasadClanBuff(pc), true);
			ClanTable.getInstance().updateClan(clan);
			break;
		}
		case CLAN_BUFF_CHOICE: {
			readH();
			readD();
			L1Clan clan = pc.getClan();
			if (clan == null)
				return;
			int buffnum = readBit();
			readC();
			/** 1號選擇 2號移動 3號變更 */
			int BuffCheck = readC();
			switch (BuffCheck) {
			case 1:
				clan.setEinhasadBlessBuff(buffnum);
				pc.sendPackets(new S_EinhasadClanBuff(pc), true);
				break;
			case 2:
				if (!pc.getMap().isEscapable()) {
					pc.sendPackets(new S_SystemMessage("此處不可使用。"), true);
					pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
					return;
				}
				ClanBuff Buff = ClanBuffTable.getBuffList(buffnum);
				if (Buff == null || !pc.getInventory().checkItem(40308, 1000))
					return;
				pc.getInventory().consumeItem(40308, 1000);
				pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
				pc.start_teleport(Buff.teleportX, Buff.teleportY, (short) Buff.teleportM, pc.getHeading(), 18339, true, false);
				break;
			case 3:
				if (!pc.getInventory().checkItem(40308, 500000))
					return;
				pc.getInventory().consumeItem(40308, 500000);
				clan.setEinhasadBlessBuff(buffnum);
				pc.sendPackets(new S_EinhasadClanBuff(pc), true);
				break;
			}
			ClanTable.getInstance().updateClan(clan);
			break;
		}
			case EQUIPMENT:
			readH();
			readC();
			int _type = readC();
			int _slot = readC();
			readC();
			if (_slot >= 0 && _slot <= 3) {
				if (_type == 16) {
					pc.sendPackets(String.valueOf(new S_SlotChange(S_SlotChange.SLOT_CHANGE, _slot)));
					pc.setSlotNumber(_slot);
					pc.getChangeSlot(_slot);
				} else if (_type == 8) {
					int _save = readC();
					readC();
					if(_save == 0) {
						pc.addSlotItem(_slot, 0, true);
					} else if(_save == 1) {
						int Namelength = readC();
						String Name = readS(Namelength);
						int color = readC();
						long time = System.currentTimeMillis();
						if(color < 0 || color > 5) {
							System.out.println(String.format("%s 中繼器可疑用戶（與交換相關）。", pc.getName()));
							return;
						}
						if(Name.getBytes().length < 0 || Name.getBytes().length > 20) {
							System.out.println(String.format("%s 中繼器可疑用戶（與交換相關）。", pc.getName()));
							return;
						}
						if(pc.get_slotsavetime() > time) {
							System.out.println(String.format("%s 中繼器可疑用戶（與交換相關）。", pc.getName()));
							return;
						}
						pc.addslotsetting(_slot, color, Name);
						CharacterSlotItemTable.getInstance().update_CharSlotcolor(pc, _slot, pc.get_slot_info(_slot));
						pc.set_slotsavetime(time + 1000);
					}
				}
			}
			break;
		case CLAN_BUFF: {
			readH();
			readH();// 08
			L1Clan clan = pc.getClan();
			int buffId = read4(read_size()) - 2724; // 2724:一般攻擊 2725:一般防禦
													// 2726:戰鬥攻擊 2727:戰鬥防禦
			int consume = 300000000;
			int time = 172800;
			if (clan.getBuffTime()[buffId] != 0) {
				consume = 10000000;
				time = clan.getBuffTime()[buffId];
			}
			if (!pc.isGm() && !pc.isCrown() && !(pc.getClanRank() == 9) && !(pc.getClanRank() == 3)) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(4648)));
				return;
			}
			if (pc.isGm() || clan.getBlessCount() >= consume) {
				int oldbless = clan.getBless();
				clan.setBless(buffId + 1);
				clan.setBuffTime(buffId, time);
				int[] times = clan.getBuffTime();
				ClanTable.getInstance().updateBless(clan.getClanId(), buffId + 1);
				ClanTable.getInstance().updateBuffTime(times[0], times[1], times[2], times[3], clan.getClanId());
				if (!pc.isGm()) {
					clan.setBlessCount(clan.getBlessCount() - consume);
					ClanTable.getInstance().updateBlessCount(clan.getClanId(), clan.getBlessCount());
				}
				for (L1PcInstance member : clan.getOnlineClanMember()) {
					if (oldbless != 0 && member.hasSkillEffect(504 + oldbless)) {
						member.removeSkillEffect(504 + oldbless);
						member.sendPackets(String.valueOf(new S_ACTION_UI2(2723 + oldbless, 1, 0, 7231 + (oldbless * 2), 0)));
					}
					member.sendPackets(String.valueOf(new S_Pledge(clan, buffId + 1)));
					new L1SkillUse().handleCommands(member, buffId + 505, member.getId(), member.getX(), member.getY(), null, time, L1SkillUse.TYPE_GMBUFF);
				}
			} else
				pc.sendPackets(String.valueOf(new S_ServerMessage(4620)));
		}
			break;
		case NewStat:
			boolean isStr = false;
			boolean isInt = false;
			boolean isWis = false;
			boolean isDex = false;
			boolean isCon = false;
			boolean isCha = false;
			readC();
			int totallength = readH(); // size
			readH(); // 08 01
			readC(); // 0X10 類別區分
			int Classtype = readC();
			if (pc != null) {
				Classtype = pc.getType();
			}
			readC(); // 0x18 初始化區分
			int value = readC(); // 0x01:首次創建, 初始化 0x08:混合, 個別 0x10:獎勵屬性
			for (int i = 0; i < (totallength - 6) / 2; i++) {
				int charstat = readC();
				if (charstat == 0 || (charstat % 8) != 0) {
					break;
				}
				int stat = readC();
				// System.out.println(stat);
				switch (charstat) {
				case 0x30:
					boolean check = false;
					try {
						if (pc != null) {
							if (value == 0x10) {
								if (stat == pc.getAbility().getTotalStr()) {
									client.charStat[0] = stat;
									check = true;
								}
							}
						}
						if (!check) {
							client.charStat[0] = stat;
							isStr = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 0x38:
					client.charStat[1] = stat;
					isInt = true;
					break;
				case 0x40:
					client.charStat[2] = stat;
					isWis = true;
					break;
				case 0x48:
					client.charStat[3] = stat;
					isDex = true;
					break;
				case 0x50:
					if (value == 0x10 && stat == pc.getAbility().getTotalCon()) {
						client.charStat[4] = stat;
					} else {
						client.charStat[4] = stat;
						isCon = true;
					}
					break;
				case 0x58:
					client.charStat[5] = stat;
					isCha = true;
					break;
				}
			}
			if (value == 0x10 && !isStr && !isInt && !isWis && !isDex && !isCon && !isCha) {
				if (!isStr)
					isStr = true;
				if (!isCon)
					isCon = true;
			}
			if (isStr) {
				client.sendPacket(new S_CharStat(client, 1, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
				isStr = false;
			}
			if (isInt) {
				client.sendPacket(new S_CharStat(client, 2, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
				isInt = false;
			}
			if (isWis) {
				client.sendPacket(new S_CharStat(client, 3, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
				isWis = false;
			}
			if (isDex) {
				client.sendPacket(new S_CharStat(client, 4, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
				isDex = false;
			}
			if (isCon) {
				client.sendPacket(new S_CharStat(client, 5, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
				isCon = false;
			}
			if (isCha) {
				client.sendPacket(new S_CharStat(client, 6, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
				isCha = false;
			}
			break;
		default:
			break;
		}
	}

	public String getType() {
		return "[C] C_Craft";
	}

}

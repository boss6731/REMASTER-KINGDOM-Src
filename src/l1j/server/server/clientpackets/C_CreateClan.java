package l1j.server.server.clientpackets;

import java.io.UnsupportedEncodingException;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_USER_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_ACK;
import l1j.server.server.BadNamesList;

import l1j.server.server.server.datatables.ClanBuffTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_EinhasadClanBuff;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Pledge;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_CreateClan extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_CREATE_CLAN = "[C] C_CreateClan";

	private static boolean isAlphaNumeric(String s) {
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		return flag;
	}

	private static boolean isInvalidName(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ' || // 逐字符單位比較.
				name.charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ' || name.charAt(i) == 'ㅂ' || // 逐字符單位比較
				name.charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ' || name.charAt(i) == 'ㅇ' || // 逐字符單位比較
				name.charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ' || name.charAt(i) == 'ㅋ' || // 逐字符單位比較.
				name.charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ' || name.charAt(i) == 'ㅛ' || // 逐字符單位比較.
				name.charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ' || name.charAt(i) == 'ㅔ' || // 逐字符單位比較.
				name.charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ' || name.charAt(i) == 'ㅣ' || // 逐字符單位比較.
				name.charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ' || name.charAt(i) == 'ㅒ' || // 逐字符單位比較.
				name.charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || // 逐字符單位比較.
				name.charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ' || name.charAt(i) == 'ㅘ' || // 逐字符單位比較.
				name.charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆' || name.charAt(i) == 'ㅤ') {
				return false;
			}
		}

		if (name.length() == 0) {
			return false;
		}

		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		if (isAlphaNumeric(name)) {
			return false;
		}

		// XXX - 尚未確認是否與本廠規格相同
		// 如果全角字符超過5個字符，或總長度超過12個字節，則視為無效名稱
		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			return false;
		}
		return true;
	}

	public C_CreateClan(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		String s = readS().toLowerCase();
		L1PcInstance l1pcinstance = clientthread.getActiveChar();

		if (l1pcinstance == null)
			return;
		if (isInvalidName(s)) {
			l1pcinstance.sendPackets(String.valueOf(new S_SystemMessage("無效的血盟名稱。")));
			return;
		}
		if (l1pcinstance.isCrown()) { // 王子 或 公主
			if (l1pcinstance.getClanid() == 0 && l1pcinstance.getLevel() >= Config.ServerAdSetting.CROWNBLOODLEVEL) {
				if (!l1pcinstance.getInventory().checkItem(40308, 10000)) {
					l1pcinstance.sendPackets(String.valueOf(new S_ServerMessage(337, "$4"))); // 1缺少 %0。
					return;
				}
				for (L1Clan clan : L1World.getInstance().getAllClans()) { // 1 已存在相同名稱的血盟。
					if (clan.getClanName().toLowerCase().equals(s.toLowerCase())) {
						l1pcinstance.sendPackets(new S_ServerMessage(99)); // 1 已存在相同名稱的血盟。
						return;
					}
				}
				L1Clan clan = ClanTable.getInstance().createClan(l1pcinstance, s); // 創建血盟
				l1pcinstance.getInventory().consumeItem(L1ItemId.ADENA, 10000); // 消耗 10000 金幣
				if (clan != null) {
					l1pcinstance.sendPackets(String.valueOf(new S_ServerMessage(84, s))); // 1%0 血盟已創建。
					l1pcinstance.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), l1pcinstance.getClanRank(), l1pcinstance));
					l1pcinstance.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, l1pcinstance.getClan().getEmblemStatus()));
					l1pcinstance.sendPackets(String.valueOf(new S_Pledge(l1pcinstance.getClanid())));
					l1pcinstance.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(l1pcinstance, clan.getClanName(), 0, 0));
					l1pcinstance.start_teleport(l1pcinstance.getX(), l1pcinstance.getY(), l1pcinstance.getMapId(), l1pcinstance.getHeading(), 18339, false);
				}

				//TODO 血盟Buff更新 2017-11-12
				clan.setEinhasadBlessBuff(0);
				clan.setBuffFirst(ClanBuffTable.getRandomBuff(clan));
				clan.setBuffSecond(ClanBuffTable.getRandomBuff(clan));
				clan.setBuffThird(ClanBuffTable.getRandomBuff(clan));
				l1pcinstance.sendPackets(new S_EinhasadClanBuff(l1pcinstance), true);
				ClanTable.getInstance().updateClan(clan);
			} else {
				l1pcinstance.sendPackets(String.valueOf(new S_SystemMessage("已經有血盟或只有達到等級 "+ Config.ServerAdSetting.CROWNBLOODLEVEL +" 的玩家可以創建血盟。"))); // 添加
			}
		} else {
			l1pcinstance.sendPackets(String.valueOf(new S_ServerMessage(85))); // 1只有王子和公主才能創建血盟。
		}
	}

	@Override
	public String getType() {
		return C_CREATE_CLAN;
	}

}

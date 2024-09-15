/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Title extends ClientBasePacket {

	public static final int CLAN_RANK_GUARDIAN = 9; // 守護變更
	public static final int CLAN_RANK_SUBLEADER = 3; // 副盟主增加

	private static final String C_TITLE = "[C] C_Title";

	public C_Title(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		String charName = readS();
		String title = readS();
		if (title.length() > 16) {
			pc.sendPackets(new S_SystemMessage("稱號的字數超過限制。"));
			return;
		}

		if (charName.isEmpty() || title.isEmpty()) {
			//pc.sendPackets(new S_SystemMessage("\f1 請按照以下格式輸入：「/title \f0角色名 稱號\f1」"));
			pc.sendPackets(new S_ServerMessage(196));
			return;
		}

		L1PcInstance target = L1World.getInstance().getPlayer(charName);
		if (target == null) {
			return;
		}

		if (target.hasSkillEffect(L1SkillId.USER_WANTED1) || target.hasSkillEffect(L1SkillId.USER_WANTED2) || target.hasSkillEffect(L1SkillId.USER_WANTED3)) {
			pc.sendPackets("在被通緝時無法更改稱號。");
			return;
		}
		if (pc.isGm()) {
			changeTitle(target, title);
			return;
		}

		if (isClanLeader(pc) || (pc.getClanid() == target.getClanid() && (pc.getClanRank() == L1Clan.CLAN_RANK_GUARDIAN) || (pc.getClanRank() == L1Clan.CLAN_RANK_SUBLEADER))) { // 血盟主
			if (pc.getId() == target.getId()) { // 自己
				if (pc.getLevel() < 10) {
					// \f1血盟成員的等級必須在10級以上才能擁有稱號。
					pc.sendPackets(new S_ServerMessage(197));
					return;
				}
				changeTitle(pc, title);
			} else {
				if (pc.getClanid() != target.getClanid()) {
					// \f1如果不是血盟成員，無法給予他人稱號。
					pc.sendPackets(new S_ServerMessage(199));
					return;
				}
				if (target.getLevel() < 10) {
					// \f1由於%0的等級低於10，無法給予稱號。
					pc.sendPackets(new S_ServerMessage(202, charName));
					return;
				}
				changeTitle(target, title);
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null) {
					for (L1PcInstance clanPc : clan.getOnlineClanMember()) {
						// \f1%0給予%1「%2」稱號。
						clanPc.sendPackets(new S_ServerMessage(203, pc.getName(), charName, title));
					}
				}
			}
			///////////血盟重整//////////////
		} else if (pc.getClanRank() == 6 || pc.getClanRank() == 3) {
			if (pc.getId() == target.getId()) { // 自身
				if (pc.getLevel() < 10) {
					// \f1對於血盟成員，要持有稱號必須達到10級或以上。
					pc.sendPackets(new S_ServerMessage(197));
					return;
				}
				changeTitle(pc, title);
			} else {
				if (pc.getClanid() != target.getClanid()) {
					// \f1如果不是血盟成員，無法給予他人稱號。
					pc.sendPackets(new S_ServerMessage(199));
					return;
				}
				if (target.getLevel() < 10) {
					// \f1由於%0的等級低於10，無法給予稱號。
					pc.sendPackets(new S_ServerMessage(202, charName));
					return;
				}
				changeTitle(target, title);
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null) {
					for (L1PcInstance clanPc : clan.getOnlineClanMember()) {
						// \f1%0給予%1「%2」稱號。
						clanPc.sendPackets(new S_ServerMessage(203, pc.getName(), charName, title));
					}
				}
			}
			///////////血盟重整//////////////
		} else {
			if (pc.getId() == target.getId()) { // 自身
				if (pc.getClanid() != 0 && !Config.ServerAdSetting.CLANPCTITLESETTING) {
					// \f1僅有王子和公主可以給予血盟成員稱號。
					pc.sendPackets(new S_ServerMessage(198));
					return;
				}
				if (target.getLevel() < 40) {
					// \f1如果不是血盟成員，要持有稱號必須達到40級或以上。
					pc.sendPackets(new S_SystemMessage("如果不是新手，要持有稱號必須達到40級或以上。"));
					return;
				}
				changeTitle(pc, title);
			} else { // 他人
				if (pc.isCrown()) { // 屬於聯盟的君主
					if (pc.getClanid() == target.getClanid()) {
						// \f1%0不是你的血盟成員。
						pc.sendPackets(new S_ServerMessage(201, pc.getClanname()));
						return;
					}
				}
			}
		}
	}

	private void changeTitle(L1PcInstance pc, String title) {
		int objectId = pc.getId();
		pc.setTitle(title);
		String broadcastTitle = title;
		if(pc.hasSkillEffect(L1SkillId.USER_WANTED1)){
			broadcastTitle = L1PcInstance.WANTED_TITLE1;
		} else if(pc.hasSkillEffect(L1SkillId.USER_WANTED2)){
			broadcastTitle = L1PcInstance.WANTED_TITLE2;
		} else if(pc.hasSkillEffect(L1SkillId.USER_WANTED2)){
			broadcastTitle = L1PcInstance.WANTED_TITLE2;
		}
		pc.sendPackets(new S_CharTitle(objectId, broadcastTitle));
		Broadcaster.broadcastPacket(pc, new S_CharTitle(objectId, broadcastTitle));
		try {
			pc.save(); // DB에 캐릭터 정보를 써 우
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isClanLeader(L1PcInstance pc) {
		boolean isClanLeader = false;
		if (pc.getClanid() != 0) { // 크란 소속
			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			if (clan != null) {
				if (pc.isCrown() && pc.getId() == clan.getLeaderId()) { // 군주,
					// 한편,
					// 혈맹주
					isClanLeader = true;
				}
			}
		}
		return isClanLeader;
	}


	@Override
	public String getType() {
		return C_TITLE;
	}

}
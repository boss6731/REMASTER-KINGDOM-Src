/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.server.server.server.clientpackets;

import l1j.server.MJTemplate.MJEncoding;

import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Pledge;
import l1j.server.server.serverpackets.S_SystemMessage;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

public class C_PledgeContent extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_PledgeContent = "[C] C_PledgeContent";

	public C_PledgeContent(byte[] decrypt, GameClient clientthread) {
		super(decrypt);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		
		if (pc.getClanid() == 0) {
			return;
		}
		
		int data = readC();
		if (data == 15) { // 公告
			String announce = readS(MJEncoding.EUCKR);
			L1Clan clan = ClanTable.getInstance().getTemplate(pc.getClanid());
			if (clan != null) {
				clan.setAnnouncement(announce);
				ClanTable.getInstance().updateClan(clan);
				pc.sendPackets(new S_PacketBox(S_PacketBox.HTML_PLEDGE_REALEASE_ANNOUNCE, announce));
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("不存在的血盟。")));
			}
		} else if(data == 16){ //備忘錄
			String notes = readS();
			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			if (clan != null) {
				clan.removeClanMember(pc.getName());
				clan.addClanMember(pc.getName(), pc.getClanRank() , pc.getLevel(), notes, pc.getId(), pc.getType(), pc.getOnlineStatus(), pc);
				pc.setClanMemberNotes(notes);
				pc.sendPackets(String.valueOf(new S_Pledge(pc.getName(), notes)));
				try {
					pc.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("不存在的血盟。")));
			}
		}
	}

	@Override
	public String getType() {
		return C_PledgeContent;
	}

}

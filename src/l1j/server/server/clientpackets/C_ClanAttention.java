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

import java.io.File;


import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

/**
 * 來自此包的處理客戶端數據包
 */
public class C_ClanAttention extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_PledgeRecommendation = "[C] C_PledgeRecommendation";

	public <GameClient> C_ClanAttention(byte[] decrypt, GameClient client){
		super(decrypt);
		
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		int data = readC();
		// 0 添加血盟, 1 刪除清單, 2 血盟清單
		
		//System.out.println("C_ClanAttention - readC타입 : [ " + data + " ]");
		L1Clan targetClan = null;
		L1Clan clan = null;
		switch(data) {
			case 0: // 添加血盟紋章觀察
			/*
			**
			* 3348    血盟觀察: 您是否接受觀察 %0 血盟的紋章？
			* 3323    血盟觀察: 您是否要取消觀察 %s 血盟的紋章？ (Y/N)
			* 3324    血盟觀察: 無法觀察紋章，目標血盟處於戰爭狀態
			*/

				String pcClanName = pc.getClanname();
				String targetClanName = readS();
				clan = L1World.getInstance().findClan(pcClanName);
				if (clan == null) { // 沒有發現血盟
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG請先創建一個血盟。")));
					return;
				}

				if (pcClanName.toLowerCase().equals(targetClanName.toLowerCase())) { // 指定血盟
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG不能觀察自己的血盟。")));
					return;
				}

				for (int i = 0; i < clan.getGazeList().size(); i++) {
					if (clan.getGazeList().get(i).toLowerCase().equals(targetClanName.toLowerCase())) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG您已經在觀察對方的血盟。")));
						return;
					}
				}

				if (clan.getGazeList().size() >= 5) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG觀察紋章最多只能觀察5個血盟。")));
					return;
				}


				for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // 檢查血盟名稱
					if (checkClan.getClanName().toLowerCase().equals(targetClanName.toLowerCase())) {
						targetClan = checkClan;
						break;
					}
				}

				if (targetClan == null) { // 沒有發現對方血盟
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG不存在對方血盟。")));
					return;
				}

				File file = new File(System.getProperty("user.dir") + "/emblem/" + clan.getEmblemId());

				if (!file.exists()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("沒有血盟標誌無法請求觀察紋章。")));
					return;
				}

				file = new File(System.getProperty("user.dir") + "/emblem/" + targetClan.getEmblemId());
				if (!file.exists()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("對方血盟沒有血盟標誌。")));
					return;
				}

				L1PcInstance target = L1World.getInstance().getPlayer(targetClan.getLeaderName());
				if (target != null) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("血盟觀察: 請求中，請稍候。")));
					target.setTempID(pc.getId());
					target.sendPackets(String.valueOf(new S_Message_YN(3348, pc.getClanname())));// 您是否接受觀察 %0 血盟的紋章？
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(3349)));// 無法觀察紋章，血盟不存在、是聯盟血盟或君主離線
				}

				break;
			case 1: // 刪除觀察紋章
					// 3323    血盟觀察: 您是否要取消觀察 %s 血盟的紋章？ (Y/N)
				String targetClanName2 = readS();
				if (!pc.isCrown()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG只有君主才能取消觀察紋章。")));
					return;
				}
				clan = L1World.getInstance().findClan(pc.getClanname());
				if (clan == null) { // 沒有發現血盟
					return;
				}

				for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // 檢查血盟名稱
					if (checkClan.getClanName().toLowerCase().equals(targetClanName2.toLowerCase())) {
						targetClan = checkClan;
						break;
					}
				}

				if (targetClan == null) { // 沒有發現對方血盟
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG不存在對方血盟。")));
					return;
				}

				// 從觀察列表中刪除
				clan.removeGazelist(targetClan.getClanName());
				targetClan.removeGazelist(clan.getClanName());

				// 更新觀察紋章列表
				for (L1PcInstance member : clan.getOnlineClanMember()) {
					member.sendPackets(String.valueOf(new S_ClanAttention(clan.getGazeSize(), clan.getGazeList())));
				}

				for (L1PcInstance member : targetClan.getOnlineClanMember()) {
					member.sendPackets(String.valueOf(new S_ClanAttention(targetClan.getGazeSize(), targetClan.getGazeList())));
				}
				break;
			case 2:// 觀察紋章的血盟列表

				break;
		}
	}

	@Override
	public String getType() {
		return C_PledgeRecommendation;
	}
}

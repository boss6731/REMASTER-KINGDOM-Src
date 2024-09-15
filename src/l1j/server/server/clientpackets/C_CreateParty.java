package l1j.server.server.server.clientpackets;


import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_CreateParty extends l1j.server.server.clientpackets.ClientBasePacket {

	//private static final String C_CREATE_PARTY = "[C] C_CreateParty";

	public C_CreateParty(byte[] data, GameClient client) throws Exception {
		super(data);
		if (client == null) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		
		if ( pc == null) {
			return;
		}
		
		if(pc.getMapId() == 621 || !pc.is_world()){
			pc.sendPackets(String.valueOf(new S_SystemMessage("在該地圖中無法使用隊伍功能。")));
			return;
		}

		int type = readC();
		if (type == 0 || type == 1 || type == 4 || type == 5) {// 0.一般 1.分配
			int targetId = 0;
			L1Object temp = null;
			if (type == 4 || type == 5) {
				String name = readS();
				
				if (name == null) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(109)));
					return;
				}
				L1PcInstance tar = L1World.getInstance().getPlayer(name);
				if(tar != null){
					temp = tar;					
					targetId = tar.getId();// 錯誤附近
				}else{
					pc.sendPackets(String.valueOf(new S_ServerMessage(109 , name)));
					return;
				}
			} else {
				targetId = readD();
				temp = L1World.getInstance().findObject(targetId);
			}
			if (temp instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) temp;
				if (pc.getId() == targetPc.getId()) {
					return;
				}
				if (targetPc.isInParty()) {
					// 因為已經屬於其他隊伍所以無法邀請
					pc.sendPackets(String.valueOf(new S_ServerMessage(415)));
					return;
				}
				if (pc.isInParty()) {
					if (pc.getParty().isLeader(pc)) {
						targetPc.setPartyID(pc.getId());
						// 2%0>%s 從 U隊伍 > 邀請您加入。 接受嗎? (Y/N)
						targetPc.sendPackets(String.valueOf(new S_Message_YN(953, pc.getName())));
					} else {
						// 只有隊伍的隊長才能邀請。
						pc.sendPackets(String.valueOf(new S_ServerMessage(416)));
					}
				} else {
					targetPc.setPartyID(pc.getId());
					switch (type) {
						case 4:
						case 0:
							pc.setPartyType(0);
						// 2%0>%s 從 U隊伍 > 邀請您加入。 接受嗎? (Y/N)
						targetPc.sendPackets(String.valueOf(new S_Message_YN(953, pc.getName())));
						break;
					case 5:
					case 1:
						pc.setPartyType(1);
						// 2%0>%s 邀請您加入 U自動分配隊伍>。接受嗎? (Y/N)
						targetPc.sendPackets(String.valueOf(new S_Message_YN(954, pc.getName())));
						break;
					}
				}
			}
		} else if (type == 2) { // 聊天隊伍
			String name = readS();
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				// 沒有叫 %0 的人。
				pc.sendPackets(String.valueOf(new S_ServerMessage(109)));
				return;
			}
			if (pc.getId() == targetPc.getId()) {
				return;
			}
			if (targetPc.isInChatParty()) {
				// 因為已經屬於其他隊伍所以無法邀請
				pc.sendPackets(String.valueOf(new S_ServerMessage(415)));
				return;
			}
//            if (!pc.isGm() && ((name.compareTo("Metis") == 0) || (name.compareTo("Cassiopeia") == 0) || (name.compareTo("Misophia") == 0))) {
//                pc.sendPackets("無法邀請管理員。");
//                return;
//            }

			if (pc.isInChatParty()) {
				if (pc.getChatParty().isLeader(pc)) {
					targetPc.setPartyID(pc.getId());
					// 2%0>%s 從 U聊天隊伍 > 邀請您加入。 接受嗎? (Y/N)
					targetPc.sendPackets(String.valueOf(new S_Message_YN(951, pc.getName())));
				} else {
					// 只有隊伍的隊長才能邀請。
					pc.sendPackets(String.valueOf(new S_ServerMessage(416)));
				}
			} else {
				targetPc.setPartyID(pc.getId());
				// 2%0>%s 從 U聊天隊伍 > 邀請您加入。 接受嗎? (Y/N)
				targetPc.sendPackets(String.valueOf(new S_Message_YN(951, pc.getName())));
			}
		} else if (type == 3) {
			int targetId = readD();
			L1Object temp = L1World.getInstance().findObject(targetId);
			if (temp instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) temp;
				if (pc.getId() == targetPc.getId()) {
					return;
				}

				if (pc.isInParty()) {
					if (targetPc.isInParty()) {
						if (pc.getParty().isLeader(pc)) {
							if (pc.getLocation().getTileLineDistance(targetPc.getLocation()) < 16) {
								pc.getParty().passLeader(targetPc);
							} else {
								// 沒有可以委任的隊員在附近
								pc.sendPackets(String.valueOf(new S_ServerMessage(1695)));
							}
						} else {
							// 因為不是隊長所以不能行使權限
							pc.sendPackets(String.valueOf(new S_ServerMessage(1697)));
						}
					} else {
						// 您不是目前隊伍的成員
						pc.sendPackets(String.valueOf(new S_ServerMessage(1696)));
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return "[C] C_CreateParty";
	}

}

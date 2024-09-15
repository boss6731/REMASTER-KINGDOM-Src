package l1j.server.server.server.clientpackets;


import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Party;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_ChatParty extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_CHAT_PARTY = "[C] C_ChatParty";

	public <GameClient> C_ChatParty(byte abyte0[], GameClient clientthread) {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost() ) {
			return;
		}
	    int type = readC();
		if (type == 0) { // /chatbanish 命令
			String name = readS();
			if (!pc.isInChatParty()) { // 您沒有加入聊天隊伍。
				pc.sendPackets(String.valueOf(new S_ServerMessage(425)));
				return;
			}
			if (!pc.getChatParty().isLeader(pc)) { // 只有隊伍領袖可以驅逐成員。
				pc.sendPackets(String.valueOf(new S_ServerMessage(427)));
				return;
			}
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) { // 沒有名為 %0 的玩家。
				pc.sendPackets(String.valueOf(new S_ServerMessage(109)));
				return;
			}
			if (pc.getId() == targetPc.getId()) {
				return;
			}

			for (L1PcInstance member : pc.getChatParty().getMembers()) {
				if (member.getName().toLowerCase().equals(name.toLowerCase())) {
					pc.getChatParty().kickMember(member);
					return;
				}
			}
			// 未發現
			// %0不是隊伍成員。
			pc.sendPackets(String.valueOf(new S_ServerMessage(426, name)));
		} else if (type == 1) { // /chatoutparty 命令
			if (pc.isInChatParty()) {
				pc.getChatParty().leaveMember(pc);
			}
		} else if (type == 2 || type == 4 || type == 5) { // /chatparty 命令
			L1ChatParty chatParty = pc.getChatParty();
			if (pc.isInChatParty()) {
				pc.sendPackets(new S_Party("party", pc.getId(), chatParty.getLeader().getName(), chatParty.getMembersNameList()));
			} else {
				pc.sendPackets(String.valueOf(new S_Party("party", pc.getId())));
			}
		}
	}

	@Override
	public String getType() {
		return C_CHAT_PARTY;
	}

}

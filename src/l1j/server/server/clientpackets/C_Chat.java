package l1j.server.server.server.clientpackets;

import l1j.server.Config;

import l1j.server.server.server.datatables.SpamTable;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_NewChat;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

public class C_Chat extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_CHAT = "[C] C_Chat";

	public static final int MACRO = 0x0c;

	public <GameClient> C_Chat(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		if (clientthread == null)
			return;

		readC();
		L1PcInstance pc = clientthread.getActiveChar();

		if (pc == null)
			return;

		chatWorld(pc, readS());
	}

	private void chatWorld(L1PcInstance pc, String chatText) {
		if (pc.isGm() || pc.getLevel() >= Config.ServerAdSetting.GLOBALCHATLEVEL) {
			if (pc.isGm() || L1World.getInstance().isWorldChatElabled()) {
				if (pc.get_food() >= 12) {
					S_PacketBox pb = new S_PacketBox(S_PacketBox.FOOD, pc.get_food());
					pc.sendPackets(pb, true);
					S_PacketBox pb2 = new S_PacketBox(S_PacketBox.FOOD, pc.get_food());
					pc.sendPackets(pb2, true);
					if (pc.isGm()) {
						L1World.getInstance().broadcastPacketToAll(new S_NewChat(pc, 4, 3, chatText, "[******] "));
						return;
					}
					if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED) && !pc.isGm()) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(242)));
						return;
					}
					for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
						L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList15.contains(0, pc.getName())) {
							S_NewChat cp = new S_NewChat(pc, 4, 12, chatText, "");
							S_NewChat cp2 = new S_NewChat(pc, 4, 3, chatText, ""); // 商業全頻道聊天室添加
							listner.sendPackets(cp, true);
							listner.sendPackets(cp2, true); // 商業全頻道聊天室添加
						}
					}
				} else {
					S_ServerMessage sm = new S_ServerMessage(462);
					pc.sendPackets(sm, true);
				}
			} else {
				S_ServerMessage sm = new S_ServerMessage(510);
				pc.sendPackets(sm, true);
			}
		} else {
			S_ServerMessage sm = new S_ServerMessage(195, String.valueOf(Config.ServerAdSetting.GLOBALCHATLEVEL));
			pc.sendPackets(sm, true);
		}
	}

	@Override
	public String getType() {
		return C_CHAT;
	}
}

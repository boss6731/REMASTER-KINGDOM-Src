package l1j.server.server.server.clientpackets;

import l1j.server.server.server.datatables.BuddyTable;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1CharName;

public class C_AddBuddy extends ClientBasePacket {

	private static final String C_ADD_BUDDY = "[C] C_AddBuddy";

	public C_AddBuddy(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		String charName = readS();
		L1Buddy buddyList = BuddyTable.getInstance().getBuddy(pc.getId(), charName);

		if (charName.equalsIgnoreCase(pc.getName())) {
			pc.sendPackets("無法將自己添加為好友。");
			return;
		} else if (buddyList != null) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1052, charName))); // 已經註冊。
			return;
		}

		for (L1CharName cn : CharacterTable.getInstance().getCharNameList()) {
			if (charName.equalsIgnoreCase(cn.getName())) {
				String name = cn.getName();
				if (cn.getName().equalsIgnoreCase("Cassiopeia") || cn.getName().equalsIgnoreCase("Administrator") || cn.getName().equalsIgnoreCase("Metis") || cn.getName().equalsIgnoreCase("Misopia")) {
					continue;
				}

				BuddyTable.getInstance().addAndSetBuddy(pc.getId(), name, "");
				return;
			}
		}
		pc.sendPackets(String.valueOf(new S_ServerMessage(109, charName))); // 沒有名為 %0 的人。
	}

	@Override
	public String getType() {
		return C_ADD_BUDDY;
	}
}
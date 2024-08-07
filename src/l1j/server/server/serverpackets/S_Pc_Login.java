package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharacterFreeShieldTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1FreeShield;

public class S_Pc_Login extends ServerBasePacket {
	public S_Pc_Login(L1PcInstance pc) {
		L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShield(pc);
		
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x81);
		writeC(0x0a);
		writeC(0x08);
		writeC(pc.isPcBuff() ? 1 : 0 );
		writeC(0x10);
		writeC(shield.get_Pc_Gaho() - shield.get_Pc_Gaho_use() > 3 ? 3: shield.get_Pc_Gaho());
		writeC(0x18);
		writeC(0);
		writeC(0x20);
		writeC(pc.getInventory().checkItemCount(41921) > 200 ? 200 : pc.getInventory().checkItemCount(41921));
//		writeC(pc.getAccount().getFeatherCount() > 200 ? 200 : pc.getAccount().getFeatherCount());
		writeH(0);
	}
	public byte[] getContent() {
		return getBytes();
	}
}



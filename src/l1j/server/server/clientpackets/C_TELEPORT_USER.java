package l1j.server.server.clientpackets;

import l1j.server.MJTemplate.MJString;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_TELEPORT_USER extends ClientBasePacket {

	public C_TELEPORT_USER(byte[] data, GameClient client) throws Exception {
		super(data);
		try {
			L1PcInstance pc = client.getActiveChar();
			if(pc == null || !pc.isGm()) {
				return;
			}
			String targetName = readS();
			if(MJString.isNullOrEmpty(targetName)) {
				return;
			}
			
			L1PcInstance target = L1World.getInstance().getPlayer(targetName);
			if(target == null) {
				pc.sendPackets(String.format("無法找到 %s。", targetName));
				return;
			}
			pc.start_teleport(target.getX(), target.getY(), target.getMapId(), pc.getHeading(), 18339, false, true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType() {
		return "[C] C_TELEPORT_USER";
	}
}
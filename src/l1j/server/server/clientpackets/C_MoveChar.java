package l1j.server.server.server.clientpackets;

import l1j.server.MJCharacterActionSystem.WalkActionHandlerFactory;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;

import l1j.server.server.server.model.Instance.L1PcInstance;

public class C_MoveChar extends l1j.server.server.clientpackets.ClientBasePacket {
	public C_MoveChar(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.get_teleport() || pc.isDead())
			return;
		
		if (pc.isstop())
			return;

		//System.out.println("黑客檢查: " + (System.currentTimeMillis() - pc.getLastMoveActionMillis()));
		//pc.setLastMoveActionMillis(System.currentTimeMillis());
		MJPacketParser parser = WalkActionHandlerFactory.create();
		parser.parse(pc, this);
		parser.doWork();
	}
}
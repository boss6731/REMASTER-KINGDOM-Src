package l1j.server.server.server.clientpackets;

import l1j.server.MJCharacterActionSystem.AttackContinueActionHandlerFactory;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.GameClient;
import l1j.server.server.server.model.Instance.L1PcInstance;

public class C_AttackNew extends l1j.server.server.clientpackets.ClientBasePacket {
	public C_AttackNew(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		
		if (pc.isstop())
			return;
		
		MJPacketParser parser = AttackContinueActionHandlerFactory.create();
		if(parser == null)
			return;
		
		parser.parse(pc, this);
		parser.doWork();
	}
}


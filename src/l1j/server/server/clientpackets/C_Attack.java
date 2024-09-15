package l1j.server.server.server.clientpackets;

import l1j.server.MJCharacterActionSystem.AttackActionHandlerFactory;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.GameClient;
import l1j.server.server.server.model.Instance.L1PcInstance;

public class C_Attack extends l1j.server.server.clientpackets.ClientBasePacket {

	public C_Attack(byte[] decrypt, GameClient client) {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		
		if (pc.isstop())
			return;
		
		MJPacketParser parser = AttackActionHandlerFactory.create();
		if(parser == null)
			return;

		//TODO 切割速度測試（在解除自動切割狀態下點擊兩次會獲得兩個）
		//System.out.println(System.currentTimeMillis());
		parser.parse(pc, this);
		parser.doWork();
	}
}

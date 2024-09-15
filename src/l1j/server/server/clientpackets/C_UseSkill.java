package l1j.server.server.clientpackets;

import l1j.server.MJCharacterActionSystem.SpellActionHandlerFactory;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_UseSkill extends ClientBasePacket {
	public C_UseSkill(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
//		System.out.println("使用技能");
		if (pc == null || pc.get_teleport() || pc.isDead()){
			return;
		}
		int skillId = readH() + 1;
		
		MJPacketParser parser = SpellActionHandlerFactory.create(skillId);
		if (parser == null){
			return;
		}
		
		parser.parse(pc, this);
		parser.doWork();
	}
}

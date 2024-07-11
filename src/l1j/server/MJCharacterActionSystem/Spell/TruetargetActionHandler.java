package l1j.server.MJCharacterActionSystem.Spell;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1PcInstance;

public class TruetargetActionHandler extends DirectionActionHandler{
	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tId = pck.readD();
		tX = pck.readH();
		tY = pck.readH();
		message = pck.readS();
	}
	
	@Override
	public SpellActionHandler copy() {
		return new TruetargetActionHandler();
	}
}

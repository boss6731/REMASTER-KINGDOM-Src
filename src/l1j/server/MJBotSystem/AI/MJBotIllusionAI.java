package l1j.server.MJBotSystem.AI;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.map.L1Map;

public class MJBotIllusionAI extends MJBotAI{
	public MJBotIllusionAI() {
		super();
	}
	
	@Override
	public L1Character getCurrentTarget() {
		return null;
	}
	
	@Override
	public void dispose() {
		if(_body != null && _body.getMap() != null) {
			L1Map m = _body.getMap();
			m.setPassable(_body.getX(), _body.getY(), true);			
		}
		super.dispose();
	}
}

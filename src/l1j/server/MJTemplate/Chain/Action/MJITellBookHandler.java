package l1j.server.MJTemplate.Chain.Action;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public interface MJITellBookHandler {
	public boolean is_teleport(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance, int skillId, int mapid, int next_x, int next_y);
}

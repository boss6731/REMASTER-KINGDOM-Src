package l1j.server.server.model.item.function;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class OmanAmulet {

	public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
		if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
			return;
		}
		if (!pc.getMap().isEscapable()) {
			return;
		}
		if (pc.getX() >= 33922 && pc.getX() <= 33933 || pc.getY() >= 33339 && pc.getY() <= 33351 || pc.getX() >= 33935 && pc.getX() <= 33920 || pc.getY() >= 33337 && pc.getY() <= 33352) {
			switch (itemId) {
				case 830012: // 傲慢之塔1層移動符咒
				case 830022: // 傲慢之塔1層統治符咒
					pc.start_teleport(32735, 32798, 101, pc.getHeading(), 18339, true, false);
					break;
				case 830013: // 傲慢之塔2層移動符咒
				case 830023: // 傲慢之塔2層統治符咒
					pc.start_teleport(32727, 32803, 102, pc.getHeading(), 18339, true, false);
					break;
				case 830014: // 傲慢之塔3層移動符咒
				case 830024: // 傲慢之塔3層統治符咒
					pc.start_teleport(32726, 32803, 103, pc.getHeading(), 18339, true, false);
					break;
				case 830015: // 傲慢之塔4層移動符咒
				case 830025: // 傲慢之塔4層統治符咒
					pc.start_teleport(32620, 32859, 104, pc.getHeading(), 18339, true, false);
					break;
				case 830016: // 傲慢之塔5層移動符咒
				case 830026: // 傲慢之塔5層統治符咒
					pc.start_teleport(32601, 32866, 105, pc.getHeading(), 18339, true, false);
					break;
				case 830017: // 傲慢之塔6層移動符咒
				case 830027: // 傲慢之塔6層統治符咒
					pc.start_teleport(32611, 32863, 106, pc.getHeading(), 18339, true, false);
					break;
				case 830018: // 傲慢之塔7層移動符咒
				case 830028: // 傲慢之塔7層統治符咒
					pc.start_teleport(32618, 32866, 107, pc.getHeading(), 18339, true, false);
					break;
				case 830019: // 傲慢之塔8層移動符咒
				case 830029: // 傲慢之塔8層統治符咒
					pc.start_teleport(32602, 32867, 108, pc.getHeading(), 18339, true, false);
					break;
				case 830020: // 傲慢之塔9層移動符咒
				case 830030: // 傲慢之塔9層統治符咒
					pc.start_teleport(32613, 32866, 109, pc.getHeading(), 18339, true, false);
					break;
				case 830021: // 傲慢之塔10層移動符咒
				case 830031: // 傲慢之塔10層統治符咒
				// 此處應該有相應的代碼，但您沒有提供完整的代碼。請提供相應的代碼以完成翻譯。
				pc.start_teleport(32730, 32803, 110, pc.getHeading(), 18339, true, false);
				break;
			}
		} else {
			pc.sendPackets(3236);
		}
	}
}

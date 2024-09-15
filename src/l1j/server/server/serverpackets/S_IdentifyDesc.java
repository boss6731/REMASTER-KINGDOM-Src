package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_IdentifyDesc extends ServerBasePacket {

	/**
	 * 顯示使用確認卷軸時的訊息
	 */
	public S_IdentifyDesc(L1ItemInstance item) {
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		writeC(Opcodes.S_IDENTIFY_CODE);
		writeH(item.getItem().getItemDescId());
		StringBuilder name = new StringBuilder();

		if (item.getBless() == 0) {
			name.append("$227 "); // 受到祝福
		} else if (item.getBless() == 2) {
			name.append("$228 "); // 被詛咒
		}

		name.append(item.getItem().getNameId());
		if (item.getItem().getType2() == 1) { // 武器
			writeH(134); // 1%0：對小型怪物傷害%1 對大型怪物傷害%2
			writeC(3);
			writeS(name.toString());
			writeS(item.getItem().getDmgSmall() + "+" + item.getEnchantLevel());
			writeS(item.getItem().getDmgLarge() + "+" + item.getEnchantLevel());
		} else if (item.getItem().getType2() == 2) { // 防具
			if (item.getItem().getItemId() == 20383) { // 騎馬用頭盔
				writeH(137); // 1%0：可用次數%1［重量%2］
				writeC(3);
				writeS(name.toString());
				writeS(String.valueOf(item.getChargeCount()));
			} else {
				writeH(135); // 1%0：防禦力%1 防具
			}

				writeC(2);
				writeS(name.toString());
				writeS(Math.abs(item.getItem().get_ac()) + "+" + item.getEnchantLevel());
			}

	} else if (item.getItem().getType2() == 0) { // 其他物品
		if (item.getItem().getType() == 1) { // 魔杖
			writeH(137); // 1%0：可使用次數%1［重量%2］
			writeC(3);
			writeS(name.toString());
			writeS(String.valueOf(item.getChargeCount()));
		} else if (item.getItem().getType() == 2) {
			writeH(138);
			writeC(2);
			name.append(": $231 "); // 剩餘的燃料
			name.append(String.valueOf(item.getRemainingTime()));
			writeS(name.toString());
		} else if (item.getItem().getType() == 7) { // 食物
			writeH(136);  // 1%0：飽食度%1［重量%2］
			writeC(3);
			writeS(name.toString());
			writeS(String.valueOf(item.getItem().getFoodVolume()));
		} else {
			writeH(138); // 1%0：［重量%1］
			writeC(2);
			writeS(name.toString());
		}
		writeS(String.valueOf(item.getWeight()));
	}


	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

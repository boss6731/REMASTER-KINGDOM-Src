package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_IdentifyDesc extends ServerBasePacket {

	/**
	 * 確認滾動使用時顯示訊息
	 */
	public S_IdentifyDesc(L1ItemInstance item) {
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		writeC(Opcodes.S_IDENTIFY_CODE); // 寫入識別操作碼
		writeH(item.getItem().getItemDescId()); // 寫入物品描述 ID
		StringBuilder name = new StringBuilder();

		// 確認物品的祝福狀態
		if (item.getBless() == 0) {
			name.append("$227 "); // 祝福的
		} else if (item.getBless() == 2) {
			name.append("$228 "); // 被詛咒的
		}

		name.append(item.getItem().getNameId()); // 添加物品名稱

		if (item.getItem().getType2() == 1) { // 武器
			writeH(134); // 1%0：小型怪物攻擊%1 大型怪物攻擊%2
			writeC(3);
			writeS(name.toString());
			writeS(item.getItem().getDmgSmall() + "+" + item.getEnchantLevel()); // 小型怪物攻擊加成
			writeS(item.getItem().getDmgLarge() + "+" + item.getEnchantLevel()); // 大型怪物攻擊加成

		} else if (item.getItem().getType2() == 2) { // 防具
			if (item.getItem().getItemId() == 20383) { // 騎馬用頭盔
				writeH(137); // 1%0：可使用次數%1［重量%2］
				writeC(3);
				writeS(name.toString());
				writeS(String.valueOf(item.getChargeCount())); // 可使用次數
			} else {
				writeH(135); // 1%0：防禦力%1 防具
				writeC(2);
				writeS(name.toString());
				writeS(Math.abs(item.getItem().get_ac()) + "+" + item.getEnchantLevel()); // 防禦力
			}

		} else if (item.getItem().getType2() == 0) { // 其他物品
			if (item.getItem().getType() == 1) { // 魔杖
				writeH(137); // 1%0：可使用次數%1［重量%2］
				writeC(3);
				writeS(name.toString());
				writeS(String.valueOf(item.getChargeCount())); // 可使用次數
			} else if (item.getItem().getType() == 2) { // 其他類型
				writeH(138);
				writeC(2);
				name.append(": $231 "); // 剩餘的燃料
				name.append(String.valueOf(item.getRemainingTime())); // 剩餘時間
				writeS(name.toString());
			} else if (item.getItem().getType() == 7) { // 食物
				writeH(136);  // 1%0：飽食度%1［重量%2］
				writeC(3);
				writeS(name.toString());
				writeS(String.valueOf(item.getItem().getFoodVolume())); // 食物量
			} else {
				writeH(138); // 1%0：［重量%1］
				writeC(2);
				writeS(name.toString());
			}
			writeS(String.valueOf(item.getWeight())); // 寫入重量
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}



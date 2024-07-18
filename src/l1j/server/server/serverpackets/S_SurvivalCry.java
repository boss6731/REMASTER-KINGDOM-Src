package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1AdenShopItem;

package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1AdenShopItem;

// 封包類，用於處理生存呼喊的數據封包
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1AdenShopItem;

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1AdenShopItem;

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1AdenShopItem;

public class S_SurvivalCry extends ServerBasePacket {

	private static final String S_SURVIVAL_CRY = "[S] S_SurvivalCry";

	public static final int LIST = 0;
	public static final int EMAIL = 1;
	public static final int POINT = 2;
	public static final int STORED_EMAIL = 3;
	public static final int OTP_WINDOW = 4;
	public static final int OTP_CHECK_MSG = 5;
	public static final int UNKNOWN_CONNECTION = 15;

	public S_SurvivalCry(int value) {
		buildPacket(value);
	}

	public S_SurvivalCry(int value, boolean ck) {
		buildPacket(value, ck);
	}

	public S_SurvivalCry(long value) {
		buildPacket(value);
	}

	public S_SurvivalCry(int value, L1PcInstance pc) {
		try {
			if (value == LIST) {
// 如果 value 是 LIST (0)，表示請求的是列表
				writeC(Opcodes.S_EXTENDED);
				writeC(0x02);
				writeH(0x00);
				writeD(0x00);
				writeH(AdenShopTable.getInstance().Size());
				writeH(AdenShopTable.data_length);
				writeH(AdenShopTable.data_length);

				for (L1AdenShopItem item : AdenShopTable.getInstance().toArray()) {
					writeD(item.getItemId());
					writeH(item.get_icon_id() > 0 ? item.get_icon_id() : item.getItem().getGfxId());
					writeH(0x00);

					String name = item.getItem().getName();
					if (item.getPackCount() > 1)
						name = name + "(" + item.getPackCount() + ")";
					if (item.getItem().getMaxUseTime() > 0)
						name = name + " [" + item.getItem().getMaxUseTime() + "]";

					writeH(name.getBytes("UTF-16LE").length + 2); // 名字字節大小
					writeSU16(name); // 名字
					String html = item.getHtml();

					int ii = 2;
					if (html != null && !html.equalsIgnoreCase("")) {
						byte[] test = html.getBytes("UTF-8");
						for (int i = 0; i < test.length;) {
							if ((test[i] & 0xff) >= 0x7F)
								i += 2;
							else
								i += 1;
							ii += 2;
						}
					}
					writeH(ii); // html 大小
					writeSS(html); // html 內容
					writeD(item.getPrice()); // 價格
					writeH(item.getType()); // 類型 (2-裝備, 3-增益, 4-便利, 5-其他)
					writeH(item.getStatus()); // 狀態 (0-正常, 1-new, 2-hot, 3-sale)
					writeD(0x000C0DBF);
					writeD(0x000063);
				}
			} else if (value == EMAIL) {
// 如果 value 是 EMAIL (1)，表示請求的是已保存的電子郵件
				writeC(Opcodes.S_EXTENDED);
				String s = "0c 00 26 00 6e 00 75 00 6c 00 6c 00 40 00 6e 00 75 00 6c 00 6c 00 2e 00 63 00 6f 00 6d 00 00 00 20 b8";
				StringTokenizer st = new StringTokenizer(s);
				while (st.hasMoreTokens()) {
					writeC(Integer.parseInt(st.nextToken(), 16)); // 逐個字節寫入
				}
			} else if (value == POINT) {
// 如果 value 是 POINT (2)，表示請求的是當前點數
				writeC(Opcodes.S_EXTENDED);
				writeH(0x03);
				writeH(0x01);
				writeH(0x04);
				writeD(pc.getNcoin()); // 寫入玩家的點數
				writeH(0x00);
			} else if (value == STORED_EMAIL) {
// 如果 value 是 STORED_EMAIL (3)，表示請求的是保存的電子郵件
				writeC(Opcodes.S_EXTENDED);
				String s = "02 00 00 f4 ff ff ff 00 00 00 00 00 00 99 17";
				StringTokenizer st = new StringTokenizer(s);
				while (st.hasMoreTokens()) {
					writeC(Integer.parseInt(st.nextToken(), 16)); // 逐個字節寫入
				}
			} else if (value == OTP_WINDOW) {
// 如果 value 是 OTP_WINDOW (4)，表示請求顯示 OTP 窗口
				writeC(Opcodes.S_EXTENDED);
				writeD(0x33);
				writeH(0x00);
			} else if (value == OTP_CHECK_MSG) {
// 如果 value 是 OTP_CHECK_MSG (5)，表示請求顯示 OTP 檢查訊息
				writeC(Opcodes.S_EXTENDED);
				writeH(0x05);
// 如果 OTP 錯誤，可以寫入錯誤訊息
// writeH(0x0ED0B);
// writeD(0x29FFFFFF);
				writeH(0x00);
				writeD(0x00);
				writeC(0x00);
			}
		} catch (Exception e) {
			e.printStackTrace(); // 捕捉並打印任何異常
		}
	}

	// 私有方法，用於構建封包數據，接受一個整數值和一個布爾值
	private void buildPacket(int value, boolean ck) {
		writeC(Opcodes.S_EXTENDED);
		writeC(UNKNOWN_CONNECTION); // 寫入未知連接狀態
		writeD(0x00); // 寫入默認值
		writeC(0x00); // 寫入默認值
		writeC(0x70); // 寫入默認值
		writeC(0x17); // 寫入默認值
		writeH(0x00); // 寫入默認值
		writeH(0x00); // 寫入默認值
	}

	// 私有方法，用於構建封包數據，接受一個整數值
	private void buildPacket(int value) {
		writeC(Opcodes.S_EXTENDED);
		writeD(0x0F); // 寫入常數值 0x0F
		writeH(0x00); // 寫入默認值
		writeD(value); // 寫入剩餘時間
		writeH(0x00); // 寫入默認值
	}

	// 私有方法，用於構建封包數據，接受一個長整數值
	private void buildPacket(long value) {
		writeC(Opcodes.S_EXTENDED);
		writeD(0x0F); // 寫入常數值 0x0F
		writeH(0x00); // 寫入默認值
		writeD(value); // 寫入剩餘時間
		writeH(0x00); // 寫入默認值
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return S_SURVIVAL_CRY;
	}
}



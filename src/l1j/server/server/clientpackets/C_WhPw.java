
package l1j.server.server.clientpackets;

import java.util.HashMap;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_WhPw extends ClientBasePacket {

	private static final String C_WhPw = "[C] C_WhPw";

	/**
	 * (0e) (00) (0e 64 03) 00 (0e 64 03) 00 00 00 選項 類型 當前 下一個
	 */
	public C_WhPw(byte[] data, GameClient client) {
		super(data);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		int gamepassword = client.getAccount().getGamePassword();
		int shoppassword = client.getAccount().getShopPassword();
		int type = readC();

		if (type == 0) {
			pc.sendPackets("\f3倉庫密碼無法使用。（因內存篡改導致稍後會斷線）請重新連接。");
			return;
		}
	}

		/*if (pc._create_password) {
			StringBuilder old_password = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int password_change = PASSWORD_CHANGE.get(readC());
				if (password_change < 0) {
					old_password.append(0);
				} else {
					old_password.append(password_change);
				}
				readP(3);
			}
			int newpass = Integer.valueOf(old_password.toString());
			pc.getAccount().setShopPassword(newpass);
			pc.sendPackets("密碼已成功註冊。");
			pc.getAccount().UpdateShopPassword();
			pc._create_password = false;
			pc._seal_scroll_count = 0;
			return;
		} else if (pc._seal_scroll) {
			StringBuilder old_password = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int password_change = PASSWORD_CHANGE.get(readC());
				if (password_change < 0) {
					old_password.append(0);
				} else {
					old_password.append(password_change);
				}
				readP(3);
			}
			int newpass = Integer.valueOf(old_password.toString());
			if (shoppassword == newpass) {
				pc.getInventory().storeItem(50021, pc._seal_scroll_count);
				pc._seal_scroll = false;
				pc._seal_scroll_count = 0;
				pc.sendPackets("封印解除卷軸已發放。");
				return;
		} else {
				pc.sendPackets("密碼錯誤。");
				pc._seal_scroll = false;
				pc._seal_scroll_count = 0;
				return;
			}
		}
			if (type == 0) { *//** 設定 *//*
			StringBuilder old_password = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int password_change = PASSWORD_CHANGE.get(readC());
				if (password_change < 0) {
					old_password.append(0);
				} else {
					old_password.append(password_change);
				}
				readP(3);
			}

			StringBuilder new_password = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int password_change = PASSWORD_CHANGE.get(readC());
				if (password_change < 0) {
					new_password.append(0);
				} else {
					new_password.append(password_change);
				}
				readP(3);
			}

			int oldpass = 0;
			if (gamepassword != 0) {
				oldpass = Integer.valueOf(old_password.toString());
			}
			int newpass = Integer.valueOf(new_password.toString());

			if (gamepassword == 0 || gamepassword == oldpass) {
				Account.setGamePassword(client, newpass);
				pc.sendPackets("密碼已成功註冊。");
			} else {
				pc.sendPackets(new S_ServerMessage(835));
			}
		} else if (type == 1) { *//** 找到倉庫 *//*
			StringBuilder password = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int password_change = PASSWORD_CHANGE.get(readC());
				if (password_change < 0) {
					password.append(0);
				} else {
					password.append(password_change);
				}
				readP(3);
			}

			int chkpass = Integer.valueOf(password.toString());

			int objId = readD();
			if (gamepassword == 0 || gamepassword == chkpass) {
				if (pc.getLevel() >= 5)
					SC_WAREHOUSE_ITEM_LIST_NOTI.send_user_warehouse_items(pc, objId);
			} else {
				pc.sendPackets(new S_ServerMessage(835));
			}
		}
	}*/

	public String getType() {
		return C_WhPw;
	}

	public static final HashMap<Integer, Integer> PASSWORD_CHANGE;

	static {
		PASSWORD_CHANGE = new HashMap<Integer, Integer>();

		PASSWORD_CHANGE.put(83, 1);
		PASSWORD_CHANGE.put(80, 2);
		PASSWORD_CHANGE.put(81, 3);
		PASSWORD_CHANGE.put(86, 4);
		PASSWORD_CHANGE.put(87, 5);
		PASSWORD_CHANGE.put(84, 6);
		PASSWORD_CHANGE.put(85, 7);
		PASSWORD_CHANGE.put(90, 8);
		PASSWORD_CHANGE.put(91, 9);
		PASSWORD_CHANGE.put(82, 0);
		PASSWORD_CHANGE.put(173, -1);
	}
}
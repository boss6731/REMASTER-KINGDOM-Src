package l1j.server.MJNetSafeSystem.Distribution;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.ClientBasePacket;

public class HandShakeDistributor extends Distributor{

	@Override
	public ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception {
		if (isBanned(clnt))
			return null;

// System.out.printf("[客戶端] 操作碼:%d, 類型:%d, 大小:%d
%s
		", data[0] & 0xff, data[1] & 0xff, data.length, DataToPacket(data, data.length));

		if (op == Opcodes.C_EXTENDED_PROTOBUF) {
			if (MJEProtoMessages.existsProto(clnt, data)) {
				clnt.setStatus(MJClientStatus.CLNT_STS_CONNECTED);
				return null;
			}
		}

		toInvalidOp(clnt, op, data.length, "握手階段。", true);

//TODO 當不使用加密(側鍵)時解除註解
// toInvalidOp(clnt, op, data.length, "握手階段。", false);
		return null;
	}

	public String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}
			result.append(HexToDex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}
		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
	
	private String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);
		for (int i = number.length(); i < digits; i++)
			number = "0" + number;
		return number;
	}
}

package l1j.server.server.serverpackets;

import javolution.util.FastMap;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_NewCreateItem extends ServerBasePacket {

	private static final String S_NEWCREATEITEM = "[S] S_NewCreateItem";

	public static final int Buff 窗口 = 0x6e; // Buff 窗口的識別碼
	public static final int TAM_POINT = 0x01c2; // 特定功能或計算的識別碼
	public static final int CASTLE_WAR_TIME_END = 0x44; // 城堡戰爭結束時間的識別碼（尚未使用）
	public static final int CASTLE_WAR_TIME = 0x4C;
	public static final int CLAN_JOIN_MESSAGE = 0x43;
	public static final int CLAN_JOIN_SETTING = 0x4D;
	public static final int EMOTICON = 0x40;
	
	/** 1020之後新增的數據包 **/
	public static final int 死亡懲罰 = 0xCF;
	public static final int 圖鑑= 0x30;
	public static final int 任務對話 = 0xb;
	public static final int 任務 = 0xd;
	public static final int 任務2 = 0x9;
	public static final int 任務3 = 0x3e;
	public static final int 任務4 = 0x6;
	public static final int 新數據包2 = 0x20;
	public static final int 新數據包3= 0xe3;
	public static final int 新數據包4= 0xe5;
	public static final int 新數據包5= 0xe7;
	public static final int 新數據包6= 0xe9;
	public static final int 新數據包7= 0xea;
	public static final int 新數據包8= 0x2f;
	public static final int 新數據包10= 0x7e;
	public static final int 新數據包11= 0x76;
	public static final int 新數據包12= 0x77;
	public static final int 新數據包13 = 0x07;
	/** 1020之後新增的數據包 **/
	
	public S_NewCreateItem(int type, boolean ck) {
		buildPacket(type, ck);
	}
	
	private void buildPacket(int type, boolean ck) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {

		case Buff 窗口:
			writeC(0);
			writeC(8);
			if (ck) {
				writeC(1);
			} else {
				writeC(3);
			}
			writeC(0x10);
			writeC(0xba);
			writeC(0x04);
			writeH(0);
			break;
		case 사망패널티://
			if (ck) {
				writeC(1);
				writeC(8);
				writeC(0x80);
				writeC(1);
				writeC(0x10);
				writeC(00);
				writeC(0x18);
				writeC(0);
				writeH(0);
			} else {
				writeC(1);
				writeC(8);
				writeC(0);
				writeC(0x10);
				writeC(0);
				writeC(0x18);
				writeC(0);
				writeH(0);
			}
			break;
		case 新數據包2: // 0x20
			writeC(2);
			writeC(8);// 母雞抖
			writeC(144);
			writeC(28);
			writeC(16);
			writeC(128);
			writeC(163);
			writeC(5);
			writeC(24);
			writeC(1);
			writeC(32);
			writeC(1);
			writeC(40);
			writeC(2);
			writeC(0);
			writeC(0);
			break;
		case 新數據包3: // 0xe3
			writeC(1);
			writeC(8);
			writeC(2);
			writeC(0x00);
			writeC(0x00);
			writeC(8);
			writeC(0x00);
			writeC(0x10);
			writeC(0x00);
			writeC(0x18);
			writeC(0x00);
			writeC(0x20);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			break;

		case 新數據包4: // 0xe5
			writeC(1);
			writeC(8);
			writeC(0x00);
			writeC(0x10);
			writeC(0x00);
			writeC(0x00);
			writeC(0x18);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			break;

		case 新數據包5: // 0xe7
			writeC(1);
			writeC(0x0a);
			writeC(0);
			writeC(8);
			writeC(0x00);
			writeC(0x10);
			writeC(0x00);
			writeC(0x18);
			writeC(0);
			break;

		case 新數據包6: // 0xea
			writeC(1);
			writeC(8);
			writeC(0x14);
			writeC(0x10);
			writeC(0x0a);
			writeC(0x18);
			writeC(7);
			writeC(0x20);
			writeC(0x12);
			writeC(0x28);
			writeC(0x10);
			writeC(0x30);
			writeC(9);
			writeC(0x00);
			writeC(0x00);
			break;

		case 新數據包7: // 0xe9
			writeC(1);
			writeC(8);
			writeC(0);
			writeC(0x00);
			writeC(0x00);

			break;
		case 新數據包8: // 0x2f
			writeC(2);
			writeC(8);
			writeC(0);
			writeC(0x10);
			writeC(0);
			writeC(0x00);
			writeC(0x00);
			break;

			writeC(2);
			writeC(8);
			writeC(0);
			writeC(0x10);
			writeC(0);
			writeC(0x1a);
			writeC(0x04);
			writeC(0x08);
			writeC(0x6d);
			writeC(0x10);
			writeC(0x02);
			writeC(0x10);
			writeC(0);
			writeC(0x1a);
			writeC(0x05);
			writeC(0x08);
			writeC(0xAA);
			writeC(4);
			writeC(0x10);
			writeC(2);
			writeC(0);
			writeC(0);
				// 寫入一個數據值，可能是一個操作碼或特定功能的標識。
				// 寫入另一個數值，具體意義取決於上下文。
				// 寫入一個0值，通常表示初始化或分隔。
				// 寫入16進位的10，轉換為10進制為16。
				// 再次寫入0。
				// 寫入26，可能是一個特定的標識或參數。
				// 寫入4，根據注釋，這可能表示還有更多的項目需要捕獲。
				// 寫入8。
				// 寫入109，注釋表明這可能與遊戲中的某個編號相關。
				// 再次寫入16。
				// 寫入2，注釋指出這可能是已經捕獲的數量。
				// 再次寫入16。
				// 寫入0。
				// 再次寫入26。
				// 寫入5，根據注釋，這可能表示所有的項目都已經捕獲。
				// 再次寫入8。
				// 寫入170，可能與某個編號相關。
				// 寫入4。
				// 再次寫入16。
				// 寫入2。
				// 再次寫入0。
				// 再次寫入0。
		case 新數據包10: // 0x7e
			writeC(0);
			writeC(8);
			writeC(0);
			writeC(0x10);
			writeC(1);
			writeC(0x00);
			writeC(0x00);
			break;
		case 新數據包11: // 0x77
			writeC(0);
			writeC(8);
			writeC(0x00);
			writeC(0x80);
			writeC(0x00);
			writeC(0x83);
			writeC(8);
			writeC(0x10);
			writeC(0x00);
			writeC(0x00);
			break;
		case 新數據包12: // 0x76
			writeC(0);
			writeC(8);
			writeC(0x00);
			writeC(0x00);
			writeC(0x10);
			writeC(0x00);
			writeC(0x18);
			writeC(0);
			writeC(0x20);
			writeC(0x00);
			break;

		case 任務:
			writeC(2);
			writeC(8);
			writeC(0);
			writeC(16);
			writeC(00);
			writeC(2);
			writeC(0);
			writeC(0);
			break;
		case 任務2: // 0x09
			writeC(2);
			writeC(8);
			writeC(0);
			writeC(16);
			writeC(0);
			writeC(2);
			writeC(0);
			writeC(0);
			break;
		case 任務3: // 0x3e
			writeC(1);
			writeC(10);
			writeC(38);
			writeC(8);
			writeC(1);
			writeC(18);
			writeC(4);
			writeC(0);
			writeC(0);
			break;

		case 任務4: // 0x06
			writeC(2);
			writeC(10);
			writeC(0);
			writeC(8);
			writeC(0);
			writeC(2);
			writeC(16);
			writeC(0);
			writeC(0);
			break;

		case 任務對話:
			writeC(2);
			writeC(8);
			writeC(0);
			writeC(16);
			writeC(0);
			writeC(2);
			writeC(0);
			writeC(0);
			break;

		case 新數據包13:
			writeC(2);
			writeC(0x0a);
			writeC(0x00);
			writeC(8);
			writeC(0x00);
			writeC(2);
			writeC(0x10);
			writeC(0);
			writeC(0x00);
			writeC(0x00);
			break;

		}
	}
	
	public String check = // 13 02
			"08 90 1c 10 d0 81 87 a5 05 18 80 a3 05 "
					+ "20 01 28 03 32 43 08 17 12 3f 08 02 10 d3 61 18 "
					+ "03 22 14 67 72 6f 77 74 68 20 63 72 79 73 74 61 "
					+ "6c 20 70 69 65 63 65 28 00 30 af 10 38 01 42 06 "
					+ "24 31 32 38 32 39 4a 06 17 14 00 00 00 00 50 87 "
					+ "ff ff ff ff ff ff ff ff 01 32 44 08 05 12 40 08 "
					+ "02 10 f1 6d 18 03 22 0e 70 73 79 20 73 6f 66 74 "
					+ "20 64 72 69 6e 6b 28 00 30 bc 26 38 01 42 06 24 "
					+ "31 30 39 33 37 4a 0d 15 78 00 03 02 00 00 00 3d "
					+ "e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 "
					+ "4b 08 1c 12 47 08 02 10 95 75 18 01 22 15 66 61 "
					+ "6e 74 61 73 79 20 63 72 79 73 74 61 6c 20 70 69 "
					+ "65 63 65 28 00 30 aa 10 38 01 42 0d 24 31 37 35 "
					+ "33 31 20 24 31 37 38 30 31 4a 06 17 14 00 00 00 "
					+ "00 50 87 ff ff ff ff ff ff ff ff 01 32 42 08 0a "
					+ "12 3e 08 02 10 c8 1f 18 64 22 0e 67 6d 20 70 6f "
					+ "74 69 6f 6e 20 31 34 74 68 28 33 30 c1 26 38 01 "
					+ "42 06 24 31 30 39 33 35 4a 0b 17 13 00 00 00 00 "
					+ "3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 "
					+ "32 3f 08 21 12 3b 08 02 10 ea 6b 18 01 22 10 62 "
					+ "61 67 20 6f 66 20 73 61 6e 64 20 77 6f 72 6d 28 "
					+ "00 30 95 09 38 01 42 06 24 31 34 32 39 30 4a 06 "
					+ "17 04 00 00 00 00 50 87 ff ff ff ff ff ff ff ff "
					+ "01 32 3f 08 0f 12 3b 08 02 10 80 7a 18 14 22 0b "
					+ "64 72 75 77 61 20 63 61 6e 64 79 28 33 30 8e 1a "
					+ "38 01 42 06 24 31 30 39 34 36 4a 0b 17 03 00 00 "
					+ "00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff "
					+ "ff 01 32 43 08 26 12 3f 08 02 10 b8 7b 18 01 22 "
					+ "0f 65 76 20 69 76 6f 72 79 20 63 68 61 72 67 65 "
					+ "28 00 30 e0 0f 38 01 42 06 24 32 30 34 35 35 4a "
					+ "0b 17 07 00 00 00 00 3d e0 33 4d cb 50 97 ff ff "
					+ "ff ff ff ff ff ff 01 32 41 08 14 12 3d 08 02 10 "
					+ "c8 20 18 01 22 13 72 75 62 79 20 6f 66 20 64 72 "
					+ "61 67 6f 6e 20 32 30 30 39 28 33 30 8d 1d 38 01 "
					+ "42 05 24 37 39 37 31 4a 06 17 14 00 00 00 00 50 "
					+ "97 ff ff ff ff ff ff ff ff 01 32 44 08 02 12 40 "
					+ "08 02 10 f1 6d 18 03 22 0e 70 73 79 20 73 6f 66 "
					+ "74 20 64 72 69 6e 6b 28 00 30 bc 26 38 01 42 06 "
					+ "24 31 30 39 33 37 4a 0d 15 78 00 03 02 00 00 00 "
					+ "3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 "
					+ "32 40 08 19 12 3c 08 02 10 c6 7b 18 01 22 0c 69 "
					+ "63 65 20 74 65 61 72 20 62 61 67 28 00 30 be 07 "
					+ "38 01 42 06 24 32 30 34 37 39 4a 0b 17 07 00 00 "
					+ "00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff "
					+ "ff 01 32 3f 08 07 12 3b 08 02 10 80 7a 18 05 22 "
					+ "0b 64 72 75 77 61 20 63 61 6e 64 79 28 33 30 8e "
					+ "1a 38 01 42 06 24 31 30 39 34 36 4a 0b 17 03 00 "
					+ "00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff "
					+ "ff ff 01 32 4b 08 1e 12 47 08 02 10 95 75 18 01 "
					+ "22 15 66 61 6e 74 61 73 79 20 63 72 79 73 74 61 "
					+ "6c 20 70 69 65 63 65 28 00 30 aa 10 38 01 42 0d "
					+ "24 31 37 35 33 31 20 24 31 37 38 30 31 4a 06 17 "
					+ "14 00 00 00 00 50 87 ff ff ff ff ff ff ff ff 01 "
					+ "32 45 08 0c 12 41 08 02 10 ee 6d 18 0a 22 0f 70 "
					+ "73 79 20 73 70 69 63 79 20 72 61 6d 65 6e 28 00 "
					+ "30 be 26 38 01 42 06 24 31 30 39 33 36 4a 0d 15 "
					+ "78 00 03 02 00 00 00 3d e0 33 4d cb 50 97 ff ff "
					+ "ff ff ff ff ff ff 01 32 3e 08 23 12 3a 08 02 10 "
					+ "bd 6f 18 01 22 0f 6b 69 72 74 61 73 20 73 69 6e "
					+ "69 73 74 65 72 28 00 30 93 2c 38 01 42 06 24 31 "
					+ "35 33 38 34 4a 06 17 0e 00 00 00 00 50 87 ff ff "
					+ "ff ff ff ff ff ff 01 32 48 08 11 12 44 08 02 10 "
					+ "84 1a 18 03 22 10 62 6d 20 6d 61 67 69 63 20 73 "
					+ "63 72 6f 6c 6c 33 28 33 30 ae 2f 38 01 42 05 24 "
					+ "35 38 32 35 4a 10 17 05 00 00 00 00 11 03 18 03 "
					+ "05 03 06 03 23 03 50 97 ff ff ff ff ff ff ff ff "
					+ "01 32 3b 08 28 12 37 08 02 10 b9 77 18 01 22 0c "
					+ "66 69 72 65 20 63 72 79 73 74 61 6c 28 00 30 cc "
					+ "19 38 01 42 06 24 31 38 36 31 37 4a 06 17 15 00 "
					+ "00 00 00 50 83 ff ff ff ff ff ff ff ff 01 32 44 "
					+ "08 16 12 40 08 02 10 c6 20 18 01 22 16 64 69 61 "
					+ "6d 6f 6e 64 20 6f 66 20 64 72 61 67 6f 6e 20 32 "
					+ "30 30 39 28 33 30 89 1d 38 01 42 05 24 37 39 36 "
					+ "39 4a 06 17 14 00 00 00 00 50 97 ff ff ff ff ff "
					+ "ff ff ff 01 32 45 08 04 12 41 08 02 10 ee 6d 18 "
					+ "03 22 0f 70 73 79 20 73 70 69 63 79 20 72 61 6d "
					+ "65 6e 28 00 30 be 26 38 01 42 06 24 31 30 39 33 "
					+ "36 4a 0d 15 78 00 03 02 00 00 00 3d e0 33 4d cb "
					+ "50 97 ff ff ff ff ff ff ff ff 01 32 36 08 1b 12 "
					+ "32 08 02 10 d8 61 18 01 22 07 69 63 71 20 6b 65 "
					+ "79 28 00 30 d7 17 38 01 42 06 24 31 32 38 34 38 "
					+ "4a 06 17 0c 00 00 00 00 50 83 ff ff ff ff ff ff "
					+ "ff ff 01 32 44 08 09 12 40 08 02 10 f1 6d 18 05 "
					+ "22 0e 70 73 79 20 73 6f 66 74 20 64 72 69 6e 6b "
					+ "28 00 30 bc 26 38 01 42 06 24 31 30 39 33 37 4a "
					+ "0d 15 78 00 03 02 00 00 00 3d e0 33 4d cb 50 97 "
					+ "ff ff ff ff ff ff ff ff 01 32 3f 08 20 12 3b 08 "
					+ "02 10 eb 6b 18 01 22 10 62 61 67 20 6f 66 20 61 "
					+ "6e 74 20 71 75 65 65 6e 28 00 30 bb 0e 38 01 42 "
					+ "06 24 31 34 32 38 39 4a 06 17 04 00 00 00 00 50 "
					+ "87 ff ff ff ff ff ff ff ff 01 32 43 08 0e 12 3f "
					+ "08 02 10 c8 1f 18 c8 01 22 0e 67 6d 20 70 6f 74 "
					+ "69 6f 6e 20 31 34 74 68 28 33 30 c1 26 38 01 42 "
					+ "06 24 31 30 39 33 35 4a 0b 17 13 00 00 00 00 3d "
					+ "e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 "
					+ "46 08 25 12 42 08 02 10 b7 7b 18 01 22 12 65 76 "
					+ "20 73 6f 75 6c 74 6f 6d 62 20 63 68 61 72 67 65 "
					+ "28 00 30 e0 0f 38 01 42 06 24 32 30 34 35 34 4a "
					+ "0b 17 07 00 00 00 00 3d e0 33 4d cb 50 97 ff ff "
					+ "ff ff ff ff ff ff 01 32 42 08 13 12 3e 08 02 10 "
					+ "b5 7b 18 0a 22 0f 65 76 20 6f 6d 61 6e 20 74 65 "
					+ "6c 62 6f 6f 6b 28 00 30 e3 1e 38 01 42 05 24 39 "
					+ "33 38 31 4a 0b 17 05 00 00 00 00 3d e0 33 4d cb "
					+ "50 97 ff ff ff ff ff ff ff ff 01 32 3e 08 2a 12 "
					+ "3a 08 02 10 8c 6e 18 01 22 0f 70 63 20 69 76 6f "
					+ "72 79 20 65 6c 69 78 69 72 28 00 30 8e 20 38 01 "
					+ "42 06 24 32 30 34 36 32 4a 06 17 13 00 00 00 00 "
					+ "50 93 ff ff ff ff ff ff ff ff 01 32 45 08 01 12 "
					+ "41 08 02 10 ee 6d 18 03 22 0f 70 73 79 20 73 70 "
					+ "69 63 79 20 72 61 6d 65 6e 28 00 30 be 26 38 01 "
					+ "42 06 24 31 30 39 33 36 4a 0d 15 78 00 03 02 00 "
					+ "00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff "
					+ "ff 01 32 40 08 18 12 3c 08 02 10 c6 7b 18 01 22 "
					+ "0c 69 63 65 20 74 65 61 72 20 62 61 67 28 00 30 "
					+ "be 07 38 01 42 06 24 32 30 34 37 39 4a 0b 17 07 "
					+ "00 00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff "
					+ "ff ff ff 01 32 42 08 06 12 3e 08 02 10 c8 1f 18 "
					+ "32 22 0e 67 6d 20 70 6f 74 69 6f 6e 20 31 34 74 "
					+ "68 28 33 30 c1 26 38 01 42 06 24 31 30 39 33 35 "
					+ "4a 0b 17 13 00 00 00 00 3d e0 33 4d cb 50 97 ff "
					+ "ff ff ff ff ff ff ff 01 32 4b 08 1d 12 47 08 02 "
					+ "10 95 75 18 01 22 15 66 61 6e 74 61 73 79 20 63 "
					+ "72 79 73 74 61 6c 20 70 69 65 63 65 28 00 30 aa "
					+ "10 38 01 42 0d 24 31 37 35 33 31 20 24 31 37 38 "
					+ "30 31 4a 06 17 14 00 00 00 00 50 87 ff ff ff ff "
					+ "ff ff ff ff 01 32 3f 08 0b 12 3b 08 02 10 80 7a "
					+ "18 0a 22 0b 64 72 75 77 61 20 63 61 6e 64 79 28 "
					+ "33 30 8e 1a 38 01 42 06 24 31 30 39 34 36 4a 0b "
					+ "17 03 00 00 00 00 3d e0 33 4d cb 50 97 ff ff ff "
					+ "ff ff ff ff ff 01 32 3d 08 22 12 39 08 02 10 f7 "
					+ "75 18 01 22 0e 62 61 67 20 6f 66 20 61 73 74 61 "
					+ "72 6f 74 28 00 30 a9 10 38 01 42 06 24 31 37 36 "
					+ "35 38 4a 06 17 07 00 00 00 00 50 83 ff ff ff ff "
					+ "ff ff ff ff 01 32 3e 08 10 12 3a 08 02 10 96 1a "
					+ "18 03 22 10 62 6d 20 6c 61 77 66 75 6c 20 74 69 "
					+ "63 6b 65 74 28 00 30 c2 18 38 01 42 05 24 35 38 "
					+ "34 30 4a 06 17 05 00 00 00 00 50 97 ff ff ff ff "
					+ "ff ff ff ff 01 32 46 08 27 12 42 08 02 10 b9 7b "
					+ "18 01 22 0d 65 76 20 67 69 61 6e 74 20 64 6f 6c "
					+ "6c 28 00 30 d8 33 38 01 42 10 24 32 30 34 36 36 "
					+ "20 5b 32 35 39 32 30 30 30 5d 4a 0f 17 08 01 00 "
					+ "00 00 3d e0 33 4d cb 3f 01 24 0a 50 17 32 45 08 "
					+ "15 12 41 08 02 10 c7 20 18 01 22 17 73 61 70 70 "
					+ "68 69 72 65 20 6f 66 20 64 72 61 67 6f 6e 20 32 "
					+ "30 30 39 28 33 30 8f 1d 38 01 42 05 24 37 39 37 "
					+ "30 4a 06 17 14 00 00 00 00 50 97 ff ff ff ff ff "
					+ "ff ff ff 01 32 3f 08 03 12 3b 08 02 10 80 7a 18 "
					+ "03 22 0b 64 72 75 77 61 20 63 61 6e 64 79 28 33 "
					+ "30 8e 1a 38 01 42 06 24 31 30 39 34 36 4a 0b 17 "
					+ "03 00 00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff "
					+ "ff ff ff ff 01 32 40 08 1a 12 3c 08 02 10 c6 7b "
					+ "18 01 22 0c 69 63 65 20 74 65 61 72 20 62 61 67 "
					+ "28 00 30 be 07 38 01 42 06 24 32 30 34 37 39 4a "
					+ "0b 17 07 00 00 00 00 3d e0 33 4d cb 50 97 ff ff "
					+ "ff ff ff ff ff ff 01 32 45 08 08 12 41 08 02 10 "
					+ "ee 6d 18 05 22 0f 70 73 79 20 73 70 69 63 79 20 "
					+ "72 61 6d 65 6e 28 00 30 be 26 38 01 42 06 24 31 "
					+ "30 39 33 36 4a 0d 15 78 00 03 02 00 00 00 3d e0 "
					+ "33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 32 "
					+ "08 1f 12 2e 08 02 10 91 78 18 01 22 0c 6f 74 68 "
					+ "65 72 20 73 65 6c 66 20 33 28 44 30 b0 30 38 01 "
					+ "42 06 24 31 38 37 32 34 4a 06 17 03 00 00 00 00 "
					+ "50 17 32 44 08 0d 12 40 08 02 10 f1 6d 18 0a 22 "
					+ "0e 70 73 79 20 73 6f 66 74 20 64 72 69 6e 6b 28 "
					+ "00 30 bc 26 38 01 42 06 24 31 30 39 33 37 4a 0d "
					+ "15 78 00 03 02 00 00 00 3d e0 33 4d cb 50 97 ff "
					+ "ff ff ff ff ff ff ff 01 32 43 08 24 12 3f 08 02 "
					+ "10 b6 7b 18 01 22 0f 65 76 20 67 69 72 61 6e 20 "
					+ "63 68 61 72 67 65 28 00 30 e0 0f 38 01 42 06 24 "
					+ "32 30 34 35 33 4a 0b 17 07 00 00 00 00 3d e0 33 "
					+ "4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 44 08 "
					+ "12 12 40 08 02 10 b4 7b 18 0a 22 10 65 76 20 6a "
					+ "6f 77 6f 6f 20 74 65 6c 62 6f 6f 6b 28 00 30 ff "
					+ "17 38 01 42 06 24 31 35 39 39 34 4a 0b 17 05 00 "
					+ "00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff "
					+ "ff ff 01 32 44 08 29 12 40 08 02 10 f0 73 18 01 "
					+ "22 10 31 35 74 68 20 72 65 73 63 75 65 20 63 6f "
					+ "69 6e 28 00 30 e2 0f 38 01 42 06 24 31 30 39 33 "
					+ "38 4a 0b 17 05 00 00 00 00 3d e0 33 4d cb 50 97 "
					+ "ff ff ff ff ff ff ff ff 01 00 00";

	public S_NewCreateItem(FastMap<Integer, L1PcInstance> list) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x01cd);
		for (L1PcInstance pc : list.values()) {
			writeC(0x0a);
			int time = 0;
			long now = System.currentTimeMillis();
			if (pc.getTamTime() != null && pc.getTamTime().getTime() > now)
				time = (int) ((pc.getTamTime().getTime() - now) / 1000);
			writeC(pc.getName().getBytes().length + 14 + bitlengh(pc.getId()) + bitlengh(time));
			writeC(0x08);
			writeC(0); 
			writeC(0x10);
			write7B(pc.getId());
			writeC(0x18);
			write7B(time);
			writeC(0x20);
			writeC(pc.tamcount());
			writeC(0x2a);
			writeS2(pc.getName());
			writeC(0x30);
			writeC(pc.getLevel());
			writeC(0x38);
			writeC(pc.getType());
			writeC(0x40);
			writeC(pc.get_sex());
		}
		writeC(0x10);
		writeC(3);
		writeC(0x18);
		writeC(0);
		writeC(0x20);
		writeC(0);
		writeH(0);
	}

	/**
	 * 城堡戰爭計時器數據包
	 * @param warType 戰爭類型
	 * @param second  戰爭時間
	 * @param castle  城堡名稱
	 */
	public S_NewCreateItem(int warType, int second, String castle) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);  // 寫入操作碼，表明這是一個擴展的 Protobuf 數據包
		writeH(CASTLE_WAR_TIME);              // 寫入城堡戰爭時間的操作碼或常量
		writeC(0x08);                         // 寫入一個標記字節
		writeC(warType);                      // 寫入戰爭類型；1 表示防禦，2 表示進攻
		writeC(0x10);                         // 寫入一個標記字節
		if (second > 0) {
			writeBit(second * 2);             // 寫入剩餘時間的兩倍
			writeC(0x1a);                     // 寫入一個標記字節
			writeS2(castle);                  // 寫入城堡名稱
			writeH(0);                        // 寫入結束標記
		} else {
			writeC(0x00);                     // 寫入 0 值
			writeH(0x00);                     // 寫入 0 值作為結束標記
		}
	}
	
	public S_NewCreateItem(int type, int subtype) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case CLAN_JOIN_MESSAGE:
			writeH(0x0801);
			writeC(subtype);
			writeH(0x00);
			break;
		default:
			break;
		}
	}

	public S_NewCreateItem(int type, int order, int sub_order, int count) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case Buff 窗口:// 255 / 65025 / 16581375 / 4228250625  // 이건 110
			writeC(8);
			writeC(2);
			writeC(16);
			writeC(224);
			writeC(17);
			writeC(24);
			writeBit(order);
			writeC(32);
			writeC(8);
			writeC(40);
			writeBit(sub_order);
			writeC(48);
			writeC(0);
			writeC(56);
			writeC(1);
			writeC(64);
			writeBit(count);
			writeC(72);
			writeC(0);
			writeC(80);
			writeC(0);
			writeC(88);
			writeC(1);
			break;
		default:
			break;
		}
	}
	
	private void byteWrite(long value) {
		long temp = value / 128;
		if (temp > 0) {
			writeC(hextable[(int) value % 128]);
			while (temp >= 128) {
				writeC(hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				writeC((int) temp);
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(hextable[(int) value]);
				writeC(0);
			}
		}
	}
	
	public S_NewCreateItem(int type, GameClient client) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);		
		writeH(type);
		switch (type) {
		case TAM_POINT:
			writeC(0x08);
			if(client.getAccount() == null){
				writeC(0x00);
			}else{
				int value = client.getAccount().tam_point;
				if (value <= 0)
					writeC(0x00);
				byteWrite(value);
			}
			writeH(0x00);
			break;
		}
	}

	public S_NewCreateItem(int type, int subtype, int objid) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
			case EMOTICON:
				writeC(0x01);
				writeC(0x08);
				int temp = objid / 128;
				if (temp > 0) {
					writeC(hextable[objid % 128]);
					while (temp > 128) {
						writeC(hextable[temp % 128]);
						temp = temp / 128;
					}
					writeC(temp);
				} else {
					if (objid == 0) {
						writeC(0);
					} else {
						writeC(hextable[objid]);
						writeC(0);
					}
				}
				// byteWrite(value);
				writeC(0x10);
				writeC(0x02);
				writeC(0x18);
				writeC(subtype);
				writeH(0);
				break;

			case CLAN_JOIN_SETTING:
				writeD(0x10010801); // 寫入數據 0x10010801
				writeC(subtype);    // 寫入加入設置的子類型
				writeC(0x18);       // 寫入數據 0x18
				writeC(objid);      // 寫入加入類型的對象 ID
				writeD(0x00001422); // 寫入數據 0x00001422
				writeD(0x00);       // 寫入數據 0x00
				writeD(0x00);       // 寫入數據 0x00
				writeD(0x00);       // 寫入數據 0x00
				writeD(0x00);       // 寫入數據 0x00
				writeD(0x00);       // 寫入數據 0x00
				break;
			default:
				break;
		}
	}
	
	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85,
			0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90,
			0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b,
			0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6,
			0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
			0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc,
			0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7,
			0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2,
			0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd,
			0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8,
			0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3,
			0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe,
			0xff };
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_NEWCREATEITEM;
	}
}



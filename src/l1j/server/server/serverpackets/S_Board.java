package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1BoardPost;

public class S_Board extends ServerBasePacket {
	private static final String S_BOARD = "[S] S_Board";
	private static final int TOPIC_LIMIT = 8;


	public S_Board(L1NpcInstance board) {
		switch (board.getNpcId()) {
			case 4200015: // 伺服器信息公告板
				buildPacketNotice(board, 0);
				break;
			case 4200020: // 營運者1
				buildPacketNotice1(board, 0);
				break;
			case 4200021: // 營運者2
				buildPacketNotice2(board, 0);
				break;
			case 71008: // NPC ID 71008
			case 4200022: // 營運者3
				buildPacketNotice3(board, 0);
				break;
			case 500002: // 建議事項
				buildPacketPhone(board, 0);
				break;
			case 900006: // 龍之鑰匙公告板
				buildPacketKey(board, 0);
				break;
			case 999999: // 事件公告板
				buildPacket1(board, 0);
				break;
			case 500001: // 全體排名
				buildPacket2(board, 0);
				break;
			case 4200013: // 錯誤回報公告板
				buildPacket3(board, 0);
				break;
			default: // 預設處理
				buildPacket(board, 0);
				break;
		}
	}

	public S_Board(L1NpcInstance board, int number) {
		switch (board.getNpcId()) {
			case 4200015: // 伺服器信息公告板
				buildPacketNotice(board, number);
				break;
			case 42000162: // 營運者1
				buildPacketNotice1(board, number);
				break;
			case 42000163: // 營運者2
				buildPacketNotice2(board, number);
				break;
			case 4200099: // 營運者3
				buildPacketNotice3(board, number);
				break;
			case 500002: // 建議事項
				buildPacketPhone(board, number);
				break;
			case 900006: // 龍之鑰匙公告板
				buildPacketKey(board, number);
				break;
			case 999999: // 事件公告板
				buildPacket1(board, number);
				break;
			case 500001: // 全體排名
				buildPacket2(board, number);
				break;
			case 4200013: // 錯誤回報公告板
				buildPacket3(board, number);
				break;
			default: // 預設處理
				buildPacket(board, number);
				break;
		}
	}

	private void buildPacket1(L1NpcInstance board, int number) { // BugBear 比賽選手狀態
		writeC(Opcodes.S_HYPERTEXT); // 寫入操作碼
		writeD(board.getId()); // 寫入 NPC ID
		writeS("maeno4"); // 寫入固定字串 "maeno4"
		writeC(0); // 寫入一個字節的值 0
		writeH(15); // 寫入兩個字節的值 15

		for (int i = 0; i < 5; ++i) {
			writeS(BugRaceController.getInstance()._littleBugBear[i].getName()); // 寫入 BugBear 的名字
			writeS(BugRaceController.getInstance()._bugCondition[i]); // 寫入 BugBear 的狀態
			writeS(Double.toString(BugRaceController.getInstance()._winRate[i]) + "%"); // 寫入 BugBear 的勝率，加上百分號
		}
	}
	private void buildPacket2(L1NpcInstance board, int number) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[9][3];
		id = new int[9];

		// 初始化數組
		while (count < 9) {
			id[count] = count + 1;
			db[count][0] = "排名"; // 排名
			db[count][1] = "名稱"; // 名字
			count++;
		}

		// 初始化類別名稱
		db[0][2] = "--------- 戰\t士"; // 	전     사
		db[1][2] = "--------- 王\t族"; // 	군     주
		db[2][2] = "--------- 騎\t士"; // 	기     사
		db[3][2] = "--------- 精\t靈"; // 	요     정
		db[4][2] = "--------- 法\t師"; // 	법     사
		db[5][2] = "--------- 黑暗\t精靈"; // 다     엘
		db[6][2] = "--------- 龍\t騎士"; // 	용 기 사
		db[7][2] = "--------- 幻\t術師"; // 	환 술 사

		// 寫入操作碼和NPC ID
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0);
		writeD(board.getId());
		writeC(0xFF); // 寫入 0xFF (未知意圖)
		writeC(0xFF); // 寫入 0xFF (未知意圖)
		writeC(0xFF); // 寫入 0xFF (未知意圖)
		writeC(0x7F); // 寫入 0x7F (未知意圖)
		writeH(9); // 寫入數量 9
		writeH(300); // 寫入300 (可能是某長度或大小)

		// 寫入每個類別的詳細信息
		for (int i = 0; i < 8; ++i) {
			writeD(id[i]); // 寫入ID
			writeS(db[i][0]); // 寫入排名
			writeS(db[i][1]); // 寫入名字
			writeS(db[i][2]); // 寫入類別名稱
		}
	}
	private void buildPacket3(L1NpcInstance board, int number) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[8][3];
		id = new int[8];

		// 初始化數組
		while (count < 8) {
			id[count] = count + 1;
			db[count][0] = "錯誤監視"; //버그감시
			db[count][1] = "名稱"; // 名字
			count++;
		}

		// 初始化類別名稱
		db[0][2] = "1.武器\t排名 "; // 1.무기 랭킹
		db[1][2] = "2.防具\t排名 "; // 2.방어구 랭킹
		db[2][2] = "3.金幣\t排名 "; // 3.아덴 랭킹
		db[3][2] = "4.等級\t排名"; // 4.레벨 랭킹
		db[4][2] = "5.神秘羽毛\t排名"; // 5.신비깃털 랭킹
		db[5][2] = "6.倉庫金幣\t排名"; // 6.창고아덴랭킹
		db[6][2] = "7.HP\t排名"; // 7.HP랭킹
		db[7][2] = "8.MP\t排名"; // 8.MP랭킹

		// 寫入操作碼和NPC ID
		writeC(Opcodes.S_BOARD_LIST);
		// writeC(0x00); // 10月18日添加
		writeC(0);
		writeD(board.getId());
		writeC(0xFF); // 寫入 0xFF (未知意圖)
		writeC(0xFF); // 寫入 0xFF (未知意圖)
		writeC(0xFF); // 寫入 0xFF (未知意圖)
		writeC(0x7F); // 寫入 0x7F (未知意圖)
		writeH(8); // 寫入數量 8
		writeH(300); // 寫入300 (可能是某長度或大小)

// 寫入每個類別的詳細信息
		for (int i = 0; i < 8; ++i) {
			writeD(id[i]); // 寫入ID
			writeS(db[i][0]); // 寫入錯誤監視
			writeS(db[i][1]); // 寫入名字
			writeS(db[i][2]); // 寫入類別名稱
		}
	}
	
	

	private void buildPacket(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.index(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		if (number == 0) {
			writeD(0x7FFFFFFF);
		} else {
			writeD(number);
		}
		writeC(topics.size());
		if (number == 0) {
			writeC(0);
			writeH(300);
		}
		for (L1BoardPost topic : topics) {
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		if (number == 0) {
			writeD(0x7FFFFFFF);
		} else {
			writeD(number);
		}
		writeC(topics.size());
		if (number == 0) {
			writeC(0);
			writeH(300);
		}
		for (L1BoardPost topic : topics) {
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice1(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM1(number, TOPIC_LIMIT);
		
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		if (number == 0) {
			writeD(0x7FFFFFFF);
		} else {
			writeD(number);
		}
		writeC(topics.size());
		if (number == 0) {
			writeC(0);
			writeH(300);
		}
		for (L1BoardPost topic : topics) {
			if(topic == null){
				System.out.println(String.format("[L1BoardPost 錯誤] %d", number));
				continue;
			}
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice2(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM2(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		if (number == 0) {
			writeD(0x7FFFFFFF);
		} else {
			writeD(number);
		}
		writeC(topics.size());
		if (number == 0) {
			writeC(0);
			writeH(300);
		}
		for (L1BoardPost topic : topics) {

			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketNotice3(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexGM3(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		if (number == 0) {
			writeD(0x7FFFFFFF);
		} else {
			writeD(number);
		}
		writeC(topics.size());
		if (number == 0) {
			writeC(0);
			writeH(300);
		}
		for (L1BoardPost topic : topics) {
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	private void buildPacketPhone(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexPhone(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		if (number == 0) {
			writeD(0x7FFFFFFF);
		} else {
			writeD(number);
		}
		writeC(topics.size());
		if (number == 0) {
			writeC(0);
			writeH(300);
		}
		for (L1BoardPost topic : topics) {
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	
	private void buildPacketKey(L1NpcInstance board, int number) {
		List<L1BoardPost> topics = L1BoardPost.indexKey(number, TOPIC_LIMIT);
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0); // DragonKeybbs = 1
		writeD(board.getId());
		if (number == 0) {
			writeD(0x7FFFFFFF);
		} else {
			writeD(number);
		}
		writeC(topics.size());
		if (number == 0) {
			writeC(0);
			writeH(300);
		}
		for (L1BoardPost topic : topics) {
			writeD(topic.getId());
			writeS(topic.getName());
			writeS(topic.getDate());
			writeS(topic.getTitle());
		}
	}
	
	

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_BOARD;
	}
}



package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1BoardPost;

public class S_Board extends l1j.server.server.serverpackets.ServerBasePacket {
	private static final String S_BOARD = "[S] S_Board";
	private static final int TOPIC_LIMIT = 8;
	

	public S_Board(L1NpcInstance board) {
		switch (board.getNpcId()) {
			case 4200015:// 伺服器信息公告板
				buildPacketNotice(board, 0);
				break;
			case 4200020:// 管理員1
				buildPacketNotice1(board, 0);
				break;
			case 4200021: // 管理員2
				buildPacketNotice2(board, 0);
				break;
			case 71008:
			case 4200022:// 管理員3
				buildPacketNotice3(board, 0);
				break;
			case 500002:// 建議事項
				buildPacketPhone(board, 0);
				break;
			case 900006:// 龍鑰匙通知公告板
				buildPacketKey(board, 0);
				break;
			case 999999:// 錯誤公告板
				buildPacket1(board, 0);
				break;
			case 500001:// 全體排名
				buildPacket2(board, 0);
				break;
			case 4200013:// 錯誤報告公告板
				buildPacket3(board, 0);
				break;
			default:// 默認值
				buildPacket(board, 0);
				break;
		}
	}

	public S_Board(L1NpcInstance board, int number) {
		switch (board.getNpcId()) {
			case 4200015:// 伺服器信息公告板
				buildPacketNotice(board, number);
				break;
			case 42000162: // 管理員1
				buildPacketNotice1(board, number);
				break;
			case 42000163:// 管理員2
				buildPacketNotice2(board, number);
				break;
			case 4200099:// 管理員3
				buildPacketNotice3(board, number);
				break;
			case 500002:// 建議事項
				buildPacketPhone(board, number);
				break;
			case 900006:// 龍鑰匙通知公告板
				buildPacketKey(board, number);
				break;
			case 999999:// 錯誤公告板
				buildPacket1(board, number);
				break;
			case 500001:// 全體排名
				buildPacket2(board, number);
				break;
			case 4200013:// 錯誤報告公告板
				buildPacket3(board, number);
				break;
			default:// 默認值
				buildPacket(board, number);
				break;
		}
	}
	
	private void buildPacket1(L1NpcInstance board, int number) {	// 錯誤公告比賽參賽者狀態
		writeC(Opcodes.S_HYPERTEXT);
		writeD(board.getId());
		writeS("maeno4");
		writeC(0);                        
		writeH(15);
		for( int i = 0; i < 5; ++i ) {
			writeS(BugRaceController.getInstance()._littleBugBear[i].getName()); // 錯誤公告比賽小蟲熊的名字
			writeS(BugRaceController.getInstance()._bugCondition[i]); // 狀態
			writeS(Double.toString(BugRaceController.getInstance()._winRate[i]) + "%"); // 勝率
		}
	}
	private void buildPacket2(L1NpcInstance board,int number) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[9][3];
		id = new int[9];
		while (count < 9) {
			id[count] = count + 1;
			db[count][0] = "";// Ranking
			db[count][1] = "";
			count++;
		}
		//db[0][2] = "--------- 全體排名";
		db[0][2] = "--------- 戰     士";
		db[1][2] = "--------- 王     族";
		db[2][2] = "--------- 騎     士";
		db[3][2] = "--------- 妖     精";
		db[4][2] = "--------- 法     師";
		db[5][2] = "--------- 黑 暗 精 靈";
		db[6][2] = "--------- 龍 騎 士";
		db[7][2] = "--------- 幻 術 師";
		db[8][2] = "--------- 劍     士";
		db[9][2] = "--------- 黃 金 槍 騎";
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0);
		writeD(board.getId());
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0x7F); // ?
		writeH(9);
		writeH(300);
		for (int i = 0; i < 8; ++i) {
			writeD(id[i]);
			writeS(db[i][0]);
			writeS(db[i][1]);
			writeS(db[i][2]);
		}
	}
	private void buildPacket3(L1NpcInstance board,int number) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[8][3];
		id = new int[8];
		while(count < 8) {
			id[count] = count + 1;
			db[count][0] = "錯誤監視";
			db[count][1] = "";
			count++;
		}
		db[0][2] = "1. 武器排名";
		db[1][2] = "2. 防具排名";
		db[2][2] = "3. 金幣排名";
		db[3][2] = "4. 等級排名";
		db[4][2] = "5. 神秘羽毛排名";
		db[5][2] = "6. 倉庫金幣排名";
		db[6][2] = "7. HP排名";
		db[7][2] = "8. MP排名";


		writeC(Opcodes.S_BOARD_LIST);
		//writeC(0x00);// 10月18日添加
		writeC(0);
		writeD(board.getId());
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0x7F); // ?
		writeH(8);
		writeH(300);
		for (int i = 0; i < 8; ++i) {
			writeD(id[i]);
			writeS(db[i][0]);
			writeS(db[i][1]);
			writeS(db[i][2]);
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
				System.out.println(String.format("[L1BoardPost Error] %d", number));
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


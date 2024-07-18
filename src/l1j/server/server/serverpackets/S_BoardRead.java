/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1BoardPost;

public class S_BoardRead extends ServerBasePacket {
	private static final String S_BoardRead = "[S] S_BoardRead";

	public S_BoardRead(L1NpcInstance board, int number) {
		if (board.getNpcId() == 4200015){ //伺服器通知
			buildPacketNotice(board, number);
		}else if(board.getNpcId() == 4200020){//Gm1
			buildPacketNotice1(board, number);
		}else if(board.getNpcId() == 4200021){//gm2
			buildPacketNotice2(board, number);
		}else if(board.getNpcId() == 4200022 || board.getNpcId() == 71008){//gm3
			buildPacketNotice3(board, number);
		}else if(board.getNpcId() == 71008){//gm4
			buildPacketNotice4(board, number);
		} else if (board.getNpcId() == 900006) { // 龍之鑰匙公告板
			buildPacketKey(board, number);
		} else if (board.getNpcId() == 500002) { // 建議事項公告板
			buildPacketPhone(board, number);
		} else {
			buildPacket(board, number); // 預設處理
		}
	}

	private void buildPacket(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findById(number);
		writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());

		
		/*
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}
	private void buildPacketNotice(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM(number);
		writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
		
		
		/*writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}
	private void buildPacketNotice1(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM1(number);
		if(topic == null){
			System.out.println(String.format("[L1BoardPost 錯誤] %d", number));
			return;
		}
		
		writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
		
		/*writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName() != null ? topic.getName() : "");
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}
	private void buildPacketNotice2(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM2(number);
		writePacket(number, topic.getName() != null ? topic.getName() : "", topic.getTitle(), topic.getDate(), topic.getContent());
		
		/*writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName() != null ? topic.getName() : "");
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}
	private void buildPacketNotice4(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM3(number);
		writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
		
		/*writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}
	private void buildPacketNotice3(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdGM3(number);
		writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
		
		/*writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}
	private void buildPacketPhone(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdPhone(number);
		writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
		
		/*writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}

	private void buildPacketKey(L1NpcInstance board, int number) {
		L1BoardPost topic = L1BoardPost.findByIdKey(number);
		writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
		/*writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(topic.getName());
		writeS(topic.getTitle());
		writeS(topic.getDate());
		writeS(topic.getContent());*/
	}
	
	
	private static final int CONTENT_MIN_LENGTH = 100;
	private void writePacket(int number, String name, String title, String date, String content){
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS(name);
		writeS(title);
		writeS(date);
		
		try {
			byte[] contentBytes = content.getBytes("UTF-8");
			int addedLen = CONTENT_MIN_LENGTH - contentBytes.length;
			writeByte(contentBytes);
			if(addedLen > 0){
				byte[] paddings = new byte[addedLen];
				for(int i=addedLen - 1; i>=0; --i)
					paddings[i] = 0x20;
				writeByte(paddings);
			}
			writeS(".");
			writeC(0x00);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_BoardRead;
	}
}



package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ChatPacket extends ServerBasePacket {

	private static final String _S__1F_NORMALCHATPACK = "[S] S_ChatPacket";

	public S_ChatPacket(String targetname, String chat, int opcode) {
		writeC(opcode);
		writeC(9);
		writeS("-> (" + targetname + ") " + chat);
	}
	
	public S_ChatPacket(String targetname, int type, String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(type);
		writeS("[" + targetname + "] " + chat);
	}

	// 對管理耳語
	public S_ChatPacket(String from, String chat) {
		writeC(Opcodes.S_TELL);
		writeS(from);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(12);//1~99
		writeS(chat);
	}

	public S_ChatPacket(String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(0x0F);
		writeD(000000000);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int a, int b, int c) {
		writeC(Opcodes.S_MESSAGE);
		writeC(4);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int test) {
		writeC(Opcodes.S_SAY);
		writeC(15);
		writeD(pc.getId());
		writeS(chat);
	}

	public S_ChatPacket(String chat, int opcode) {
		writeC(opcode);
		writeC(2);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int opcode, int type) {
		writeC(opcode); // 寫入操作碼

		switch (type) {
			case 0: // 通常聊天
				writeC(type); // 寫入聊天類型
				writeD(pc.getId()); // 寫入玩家 ID
				writeS(pc.getName() + ": " + chat); // 寫入玩家名稱和聊天內容
				break;
			case 2: // 絕叫（大聲呼喊）
				writeC(type); // 寫入聊天類型
				if (pc.isInvisble()) { // 如果玩家處於隱形狀態
					writeD(0); // 寫入 0 表示隱形
				} else {
					writeD(pc.getId()); // 否則寫入玩家 ID
				}
				writeS("<" + pc.getName() + "> " + chat); // 寫入玩家名稱和聊天內容
				writeH(pc.getX()); // 寫入玩家的 X 座標
				writeH(pc.getY()); // 寫入玩家的 Y 座標
				break;
			case 3: // 特殊處理
				writeC(type); // 寫入聊天類型
				// 如果玩家名稱是 "메티스" 且不是 "미소피아" 或 "카시오페아"
				if (pc.getName().equalsIgnoreCase("Métis") &&
						!pc.getName().equalsIgnoreCase("Misopia") &&
						!pc.getName().equalsIgnoreCase("cassiopeia")) {
					writeS("[******] " + chat); // 寫入隱藏的名稱和聊天內容
				}
				break;
		}
	}
		case 4: // 血盟聊天
			writeC(type);
			if (pc.getAge() == 0) {
				writeS("{" + pc.getName() + "} " + chat);
			} else {
				writeS("{" + pc.getName() + "(" + pc.getAge() + ")" + "} " + chat);
			}
			break;
		case 9: // 維斯帕
			writeC(type);
			writeS("-> (" + pc.getName() + ") " + chat);
			break;
		case 11: // 聚會聊天
			writeC(type);
			writeS("(" + pc.getName() + ") " + chat);
			break;
		case 12: // 연합 채팅
			writeC(type);
			writeS("[" + pc.getName() + "] " + chat);
			break;
		case 13:
			writeC(4);
			writeS("{{" + pc.getName() + "}} " + chat);
			break;
		case 14: // 채팅파티
			writeC(type);
			writeD(pc.getId());
			writeS("\\fU(" + pc.getName() + ") " + chat); // #
			break;
		case 15:
			writeC(type);
			writeS("[" + pc.getName() + "] " + chat);
			break;
		case 16: // 위스파
			writeS(pc.getName());
			writeS(chat);
			break;
		case 17: // 군주채팅 +
			writeC(type);
			writeS("{" + pc.getName() + "} " + chat);
			break;
		default:
			break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__1F_NORMALCHATPACK;
	}

}
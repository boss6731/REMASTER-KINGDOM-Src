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

	// 給管理員的私信
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
		writeC(opcode);
				
		switch (type) {
		case 0: // 普通聊天
			writeC(type);
			writeD(pc.getId());
			writeS(pc.getName() + ": " + chat);
			break;
		case 2: // 嚎叫
			writeC(type);
			if (pc.isInvisble()) {
				writeD(0);
			} else {
				writeD(pc.getId());
			}
			writeS("<" + pc.getName() + "> " + chat);
			writeH(pc.getX());
			writeH(pc.getY());
			break;
        case 3:
            writeC(type);
			if (pc.getName().equalsIgnoreCase("梅蒂斯") &&
					!pc.getName().equalsIgnoreCase("米索菲亞") &&
					!pc.getName().equalsIgnoreCase("卡西歐佩亞")) {
				writeS("[******] " + chat);
			}
			break;
			case 4: // 血盟聊天
			writeC(type);
			if (pc.getAge() == 0) {
				writeS("{" + pc.getName() + "} " + chat);
			} else {
				writeS("{" + pc.getName() + "(" + pc.getAge() + ")" + "} " + chat);
			}
			break;
			case 9: // 私聊
				writeC(type);
				writeS("-> (" + pc.getName() + ") " + chat);
				break;
			case 11: // 隊伍聊天
				writeC(type);
				writeS("(" + pc.getName() + ") " + chat);
				break;
			case 12: // 聯盟聊天
				writeC(type);
				writeS("[" + pc.getName() + "] " + chat);
				break;
		case 13:
			writeC(4);
			writeS("{{" + pc.getName() + "}} " + chat);
			break;
		case 14: // 隊伍聊天
			writeC(type);
			writeD(pc.getId());
			writeS("\\fU(" + pc.getName() + ") " + chat); // #
			break;
		case 15:
			writeC(type);
			writeS("[" + pc.getName() + "] " + chat);
			break;
			case 16: // 私聊
				writeS(pc.getName());
				writeS(chat);
				break;
			case 17: // 血盟聊天
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
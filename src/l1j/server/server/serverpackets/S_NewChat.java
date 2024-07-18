package l1j.server.server.serverpackets;

import java.io.UnsupportedEncodingException;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_NewChat extends ServerBasePacket {

	private static final String S_NewChat = "[S] S_NewChat";
	
	public S_NewChat(int chatType, int count, String chatText, String targetName, int unknown) {
		try {
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeC(0x03);
			writeC(0x02);
			writeC(0x08);
			writeBit(System.currentTimeMillis() / 1000L);
			writeC(0x10);
			writeC(chatType);
			writeC(0x1a);
			writeC(chatText.getBytes().length);
			writeByte(chatText.getBytes());
			writeC(0x2a);
			if ((chatType == 1) && (targetName != null) && (!targetName.equals(""))) {
				writeC(targetName.getBytes().length);
				writeByte(targetName.getBytes());
			} else {
				writeC(0);
			}
			writeC(0x28);
			writeC(unknown);
			writeC(0x30);
			writeC(chatType == 1 ? 0 : 24);
			writeH(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public S_NewChat(int type, String chatText, int count, L1PcInstance pc) {
		try {
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeC(0x04);
			writeC(0x02);
			writeC(0x08);
			writeBit(System.currentTimeMillis() / 1000L);
			writeC(0x10);
			writeC(type);
			writeC(0x1a);
			writeC(chatText.getBytes().length);
			writeByte(chatText.getBytes());
			writeC(0x2a);
			
			byte[] name = null;
			if (pc.isGm() && type == 3) {
				name = "******".getBytes("UTF-8");
			} else if(pc.is_shift_battle()){
				String server_description = pc.get_server_description();
				MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
				if(l1j.server.MJTemplate.MJString.isNullOrEmpty(server_description) || cInfo == null){
					name = "身份不明的人".getBytes("UTF-8");
				}else{
					name = cInfo.to_name_pair().getBytes("UTF-8");
				}
			} else if (pc.getAge() != 0 && type == 4) {
				String names = pc.getName() + "(" + pc.getAge() + ")";
				name = names.getBytes("UTF-8");
			} else {
				name = pc.getName().getBytes("UTF-8");
			}
			
			writeC(name.length);
			writeByte(name);

			writeC(48);
			writeC(0);

			if (type == 0) {
				writeC(0x38);
				writeBit(pc.getId());
				writeC(0x40);
				writeBit(pc.getX());
				writeC(0x48);
				writeBit(pc.getY());
			}
			int step = pc.getRankLevel();

			if (step >= 6) {
				writeC(0x50);
				writeC(step);
			}
			writeH(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public S_NewChat(L1PcInstance pc, int type, int chat_type, String chat_text, String target_name) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);

		switch (type) {
		case 3:
			writeC(0x03);
			break;
		case 4:
			writeC(0x04);
			break;
		}

		writeC(0x02);
		
		writeC(0x08);
		writeC(0x00);
		
		writeC(0x10);
		writeC(chat_type);

		writeC(0x1a);
		byte[] text_byte = chat_text.getBytes();
		writeC(text_byte.length);
		writeByte(text_byte);
		
		//		System.out.println(text_byte.length);
		
		switch (type) {
		case 3:
			writeC(0x22);

			if (chat_type == 0) {
				writeC(0x00);
				writeC(0x28);
				writeC(0x00);
				writeC(0x30);
				writeC(0x18);
				writeC(0x38);
				writeC(0x00);
			} else if (chat_type == 1) {
				byte[] name_byte = target_name.getBytes();
				writeC(name_byte.length);
				writeByte(name_byte);
				writeC(0x28);
				writeC(0x00);
				writeC(0x30);
				writeC(0x00);
				writeH(0);
			}
			break;
		case 4:
			writeC(0x2a);
			try {
				byte[] name = null;
				if (pc.isGm() && chat_type == 3) {
					name = "******".getBytes("UTF-8");
				} else if(pc.is_shift_battle()){
					String server_description = pc.get_server_description();
					MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
					if(l1j.server.MJTemplate.MJString.isNullOrEmpty(server_description) || cInfo == null){
						name = "身份不明的人".getBytes("UTF-8");
					}else{
						name = cInfo.to_name_pair().getBytes("UTF-8");
					}
				} else if (pc.getAge() != 0 && chat_type == 4) {
					String names = pc.getName() + "(" + pc.getAge() + ")";
					name = names.getBytes("UTF-8");
				} else {
					name = pc.getName().getBytes("UTF-8");
				}
				writeC(name.length);
				writeByte(name);
			} catch (UnsupportedEncodingException e) {
			}
			if (chat_type == 0) {
				writeC(0x38);
				writeK(pc.getId());
				writeC(0x40);
				writeK(pc.getX());
				writeC(0x48);
				writeK(pc.getY());
			}
			int step = pc.getRankLevel();
			if (step != 0) {
				writeC(0x50);
				writeC(step);
			}
			//TODO 操作員聊天時是否使用麥克風形狀
			/*if(chat_type == 3 && pc.isGm()){
				writeC(0x60);
				writeC(0x01);
			}*/
			writeH(0);

		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_NewChat;
	}
}



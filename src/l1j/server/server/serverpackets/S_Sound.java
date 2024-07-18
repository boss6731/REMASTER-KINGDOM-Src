package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;


public class S_Sound extends ServerBasePacket {





	private static final String S_SOUND = "[S] S_Sound";


	/**
	 * 播放效果音（sound 資料夾中的 wav 文件）。
	 * @param cha 角色對象
	 * @param id 聲音文件的 ID
	 */
	public static void broadcast(L1Character cha, int id) {
		S_Sound sound = new S_Sound(id);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(sound, false);
		}
		cha.broadcastPacket(sound);
	}

	// 構造函數，接受聲音文件的 ID
	public S_Sound(int sound) {
		buildPacket(sound);
	}

	// 私有方法，用於構建封包
	private void buildPacket(int sound) {
		writeC(Opcodes.S_SOUND_EFFECT); // 寫入操作碼，表示播放效果音
		writeC(0); // 重複次數，這裡設置為 0
		writeH(sound); // 寫入聲音文件的 ID
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SOUND;
	}
}



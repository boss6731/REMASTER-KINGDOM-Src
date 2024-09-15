package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;


public class S_Sound extends ServerBasePacket {

	private static final String S_SOUND = "[S] S_Sound";

	/**
	 * 播放音效（sound資料夾中的wav文件）。
	 * @param sound
	 */
	public static void broadcast(L1Character cha, int id){
		S_Sound sound = new S_Sound(id);
		if(cha instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance)cha;
			pc.sendPackets(sound, false);
		}
		cha.broadcastPacket(sound);
	}
	
	public S_Sound(int sound) {
		buildPacket(sound);
	}

	private void buildPacket(int sound) {
		writeC(Opcodes.S_SOUND_EFFECT);
		writeC(0); // repeat
		writeH(sound);
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

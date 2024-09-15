package l1j.server.server.serverpackets;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_AttackCritical extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_AttackCritical = "[S] S_AttackCritical";

	private byte[] _byte = null;

	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

	/** 非弓箭情況 **/
	public S_AttackCritical(L1PcInstance pc, int objid, int type, boolean is_double_weapon) {
		int gfxid = 0;
		writeC(Opcodes.S_ATTACK);
		writeC(1);
		writeD(pc.getId());
		writeD(objid);
		writeH(0x01);
		writeC(pc.getHeading());
		writeH(0x0000);
		writeH(0x0000);

		switch (type) {
			case 4: // 單手劍
				gfxid = 13411;
				break;
			case 11: // 鈍器
				if (is_double_weapon) {
					gfxid = 13415;
				} else {
					gfxid = 13414;
				}
				break;
			case 24: // 鏈鋸劍
				gfxid = 13402;
				break;
			case 40: // 法杖
				gfxid = 13413;
				break;
			case 46: // 匕首
				gfxid = 13412;
				break;
			case 50:// 鏈鋸劍
				gfxid = 13410;
				break;
			case 54:// 雙刀
				gfxid = 13417;
				break;
			case 58:// 爪
				gfxid = 13416;
				break;
			case 90:
				gfxid = 13409;
				break;
			case 91:// 鑰匙鏈
				gfxid = 21083;
				break;
			case 92:// 刺針
				gfxid = 13398;
				break;
			case 99:// 斧頭
				gfxid = 13415;
				break;
		}

		if(gfxid > 0) {
			writeC(2);
			writeD(gfxid);
		}
		writeH(0);
	}

	/** 弓箭動作 **/
	public S_AttackCritical(L1Character cha, int targetobj, int x, int y, int type, boolean isHit) {
		int gfxid = 0;
		int aid = 1;
		// 只適用於獸人弓箭手
		if (cha.getCurrentSpriteId() == 3860 || cha.getCurrentSpriteId() == 7959 || cha.getCurrentSpriteId() == 11382) {
			aid = 21;
		}
		writeC(Opcodes.S_ATTACK);
		writeC(aid);
		writeD(cha.getId());
		writeD(targetobj);
		writeC(isHit ? 6 : 0);
		writeC(0x00);
		writeC(cha.getHeading());
		writeD(_sequentialNumber.incrementAndGet());
		if (type == 20) {
			gfxid = 13392;
		} else if (type == 62) {
			gfxid = 13398;
		}
		
		writeH(gfxid);
		writeC(127);
		writeH(x);
		writeH(y);
		writeH(cha.getX());
		writeH(cha.getY());
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_AttackCritical;
	}
}

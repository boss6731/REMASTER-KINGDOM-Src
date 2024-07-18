package l1j.server.server.serverpackets;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_AttackCritical extends ServerBasePacket {

	private static final String S_AttackCritical = "[S] S_AttackCritical";

	private byte[] _byte = null;

	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

	/** 不使用弓箭的情況 **/
	public S_AttackCritical(L1PcInstance pc, int objid, int type, boolean is_double_weapon) {
		int gfxid = 0;
		writeC(Opcodes.S_ATTACK); // 攻擊操作碼
		writeC(1); // 固定值
		writeD(pc.getId()); // 寫入角色ID
		writeD(objid); // 寫入目標ID
		writeH(0x01); // 固定值
		writeC(pc.getHeading()); // 寫入角色面向方向
		writeH(0x0000); // 固定值
		writeH(0x0000); // 固定值

		switch (type) {
			case 4: // 單手劍
				gfxid = 13411;
				break;
			case 11: // 暗器
				if (is_double_weapon) {
					gfxid = 13415; // 雙持武器
				} else {
					gfxid = 13414; // 單持武器
				}
				break;
			case 24: // 鏈劍
				gfxid = 13402;
				break;
			case 40: // 法杖
				gfxid = 13413;
				break;
			case 46: // 匕首
				gfxid = 13412;
				break;
			case 50: // 鏈劍
				gfxid = 13410;
				break;
			case 54: // 雙刀
				gfxid = 13417;
				break;
			case 58: // 爪
				gfxid = 13416;
				break;
			case 90: // 不明武器類型（示範用途）
				gfxid = 13409;
				break;
			case 91: // 鍵鏈
				gfxid = 21083;
				break;
			case 92: // 刺針
				gfxid = 13398;
				break;
			case 99: // 斧頭
				gfxid = 13415;
				break;
			default:
				gfxid = 0; // 預設情況下，gfxid 為 0
				break;
		}

		// 這裡可以繼續寫其他封包處理邏輯

	}

		if(gfxid > 0) {
			writeC(2);
			writeD(gfxid);
		}
		writeH(0);
	}

	/** 弓箭攻擊動作 **/
	public S_AttackCritical(L1Character cha, int targetobj, int x, int y, int type, boolean isHit) {
		int gfxid = 0;
		int aid = 1;

		// 只有對特定的弓箭手進行更改
		if (cha.getCurrentSpriteId() == 3860 || cha.getCurrentSpriteId() == 7959 || cha.getCurrentSpriteId() == 11382) {
			aid = 21;
		}

		writeC(Opcodes.S_ATTACK); // 攻擊操作碼
		writeC(aid); // 攻擊動畫ID
		writeD(cha.getId()); // 寫入角色ID
		writeD(targetobj); // 寫入目標ID
		writeC(isHit ? 6 : 0); // 寫入命中狀態
		writeC(0x00); // 固定值
		writeC(cha.getHeading()); // 寫入角色面向方向
		writeD(_sequentialNumber.incrementAndGet()); // 寫入遞增的序列號

		// 設置弓箭攻擊動畫ID
		if (type == 20) {
			gfxid = 13392;
		} else if (type == 62) {
			gfxid = 13398;
		}

		writeH(gfxid); // 寫入動畫ID
		writeC(127); // 固定值
		writeH(x); // 寫入攻擊起點X座標
		writeH(y); // 寫入攻擊起點Y座標
		writeH(cha.getX()); // 寫入角色當前X座標
		writeH(cha.getY()); // 寫入角色當前Y座標
		writeC(0); // 固定值
		writeC(0); // 固定值
		writeC(0); // 固定值
		writeC(0); // 固定值
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

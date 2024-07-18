package l1j.server.server.serverpackets;

import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillIconGFX extends ServerBasePacket {



	public class S_SkillIconGFX extends ServerBasePacket {

		// 構造函數：用於處理技能圖標的顯示
		public S_SkillIconGFX(int i, int j) {
			writeC(Opcodes.S_EVENT);  // 寫入操作碼，表示事件
			writeC(i);               // 寫入技能或效果的代碼
			writeH(j);               // 寫入技能或效果的持續時間
			writeH(0);               // 寫入額外資訊，這裡為0，可能表示關閉圖標
		}

		// 構造函數重載：用於處理具有特定GFX ID的技能圖標的顯示
		public S_SkillIconGFX(int i, int j, int gfxid, int objid) {
			writeC(Opcodes.S_EVENT);  // 寫入操作碼，表示事件
			writeC(i);               // 寫入技能或效果的代碼
			writeH(j);               // 寫入技能或效果的持續時間
			// writeBit(gfxid);      // 原始代碼被注釋，寫入GFX ID的方法可能已更改
			writeH(gfxid);           // 寫入技能或效果的GFX ID，用於標記效果
			writeD(objid);           // 寫入對象ID，可能用於識別誰施放了技能或效果
		}

		// ... 其他方法和實作 ...

	}
	public class S_SkillIconGFX extends ServerBasePacket {

		// 構造函數：帶單個整數參數，用於設置特定圖標效果
		public S_SkillIconGFX(int i) {
			writeC(Opcodes.S_EVENT); // 寫入操作碼，表示事件
			writeC(0xa0);           // 寫入常量0xa0，可能表示特定效果
			writeC(1);              // 寫入1，可能表示啟用效果
			writeH(0);              // 寫入0，可能表示無額外數據
			writeC(2);              // 寫入2，可能表示效果類型
			writeH(i);              // 寫入效果或圖標ID
		}

		// 構造函數重載：帶兩個整數和布爾參數，用於控制圖標效果開關
		public S_SkillIconGFX(int i, int j, boolean on) {
			writeC(Opcodes.S_EVENT); // 寫入操作碼，表示事件
			writeC(i);              // 寫入效果或圖標ID
			writeH(j);              // 寫入效果的持續時間
			if (on) {
				writeC(1);              // 寫入1，表示效果開啟
			} else {
				writeC(0);              // 寫入0，表示效果關閉
			}
		}

		// 構造函數重載：帶兩個整數和兩個布爾參數，用於更細緻控制圖標效果開關
		public S_SkillIconGFX(int i, int j, boolean on, boolean on2) {
			writeC(Opcodes.S_EVENT); // 寫入操作碼，表示事件
			writeC(i);              // 寫入效果或圖標ID
			writeH(j);              // 寫入效果的持續時間
			int a;
			if (on) {
				a = 1;                  // 如果`on`為真，設置a為1
			} else if (on2) {
				a = 2;                  // 如果`on`為假且`on2`為真，設置a為2
			} else {
				a = 0;                  // 如果`on`和`on2`都為假，設置a為0
			}
			writeC(a);              // 寫入a，控制效果的開關狀態
		}

		// ... 其他方法和實作 ...

	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}



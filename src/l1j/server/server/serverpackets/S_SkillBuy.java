package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillBuy extends ServerBasePacket {



	public class S_SkillBuy extends ServerBasePacket {
		public S_SkillBuy(int o, L1PcInstance pc) {
			int count = Scount(pc); // 獲取該角色的技能數量
			int inCount = 0; // 初始化未掌握技能的計數

			// 計算角色還未掌握的技能數量
			for (int k = 0; k < count; k++) {
				if (!pc.isSkillMastery((k + 1))) {
					inCount++;
				}
			}

			try {
				writeC(Opcodes.S_BUYABLE_SPELL_LIST); // 寫入操作碼
				writeD(100); // 寫入固定整數 (這裡假設為100)
				writeH(inCount); // 寫入未掌握技能的數量

				// 寫入每個未掌握的技能的代碼
				for (int k = 0; k < count; k++) {
					if (!pc.isSkillMastery((k + 1))) {
						writeD(k); // 寫入技能代碼
					}
				}
			} catch (Exception e) {
				e.printStackTrace(); // 捕捉並打印異常
			}
		}

		public byte[] getContent() throws IOException {
			return getBytes(); // 獲取封包內容的字節數組
		}
	}

	public int Scount(L1PcInstance player) {
		int RC = 0;
// int TC = 0;
		switch (player.getType()) {
			case 0: // 軍主 (王族)
				/*
				 * if (player.get_level() >= 10 && player.get_level() <= 19) { RC = 8; }
				 * else if (player.get_level() >= 20) { RC = 16; }
				 */
				RC = 16;
				break;

			case 1: // 騎士
				/*
				 * if (player.get_level() >= 50) { RC = 8; }
				 */
				RC = 8;
				break;

			case 2: // 妖精
				/*
				 * if (player.get_level() >= 8 && player.get_level() <= 15) { RC = 8; }
				 * else if (player.get_level() >= 16 && player.get_level() <= 23) { RC = 16; }
				 * else if (player.get_level() >= 24) { RC = 23; }
				 */
				RC = 23;
				break;

			case 3: // 法師 (WIZ)
				/*
				 * if (player.get_level() >= 4 && player.get_level() <= 7) { RC = 8; }
				 * else if (player.get_level() >= 8 && player.get_level() <= 11) { RC = 16; }
				 * else if (player.get_level() >= 12) { RC = 23; }
				 */
				RC = 23;
				break;

			case 4: // 黑暗妖精 (DE)
				/*
				 * if (player.get_level() >= 12 && player.get_level() <= 23) { RC = 8; }
				 * else if (player.get_level() >= 24) { RC = 16; }
				 */
				RC = 16;
				break;

			case 5: // 龍騎士
				/*
				 * if (player.get_level() >= 12 && player.get_level() <= 23) { RC = 8; }
				 * else if (player.get_level() >= 24) { RC = 16; }
				 */
				RC = 16;
				break;

			case 6: // 幻術師
				/*
				 * if (player.get_level() >= 12 && player.get_level() <= 23) { RC = 8; }
				 * else if (player.get_level() >= 24) { RC = 16; }
				 */
				RC = 16;
				break;

			case 7: // 戰士 (Warrior)
				/*
				 * if (player.get_level() >= 50) { RC = 8; }
				 */
				RC = 8;
				break;

			case 8: // 劍士
				/*
				 * if (player.get_level() >= 12 && player.get_level() <= 23) { RC = 8; }
				 * else if (player.get_level() >= 24) { RC = 16; }
				 */
				RC = 16;
				break;

			case 9: // 黃金槍騎
				/*
				 * if (player.get_level() >= 12 && player.get_level() <= 23) { RC = 8; }
				 * else if (player.get_level() >= 24) { RC = 16; }
				 */
				RC = 16;
				break;

			default:
				break;
		}

		return RC;
		/*
		 * for(int i = 0 ; i < RC ; ++i) { if(chk(player, i) == false) { TC++; } }
		 * return TC;
		 */
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__1B_WAR;
	}

	private static final String _S__1B_WAR = "[S] S_SkillBuy";
}

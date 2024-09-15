/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class S_PetMenuPacket extends ServerBasePacket {

	public S_PetMenuPacket(L1NpcInstance npc, int exppercet) {
		buildpacket(npc, exppercet);
	}

	private void buildpacket(L1NpcInstance npc, int exppercet) {
		writeC(Opcodes.S_HYPERTEXT);

		if (npc instanceof L1PetInstance) { // 寵物
			L1PetInstance pet = (L1PetInstance) npc;
			writeD(pet.getId());
			writeS("anicom");
			writeC(0x00);
			writeH(10);
			switch (pet.getCurrentPetStatus()) {
				case 1:
					writeS("$469"); // 攻擊姿態
					break;
				case 2:
					writeS("$470"); // 防禦姿態
					break;
				case 3:
					writeS("$471"); // 休息
					break;
				case 5:
					writeS("$472"); // 警戒
					break;
				case 8:
					writeS("$613"); // 收集
					break;
				default:
					writeS("$471"); // 休息
					break;
			}
			writeS(Integer.toString(pet.getCurrentHp())); // 當前的 HP
			writeS(Integer.toString(pet.getMaxHp())); // 最大 HP
			writeS(Integer.toString(pet.getCurrentMp())); // 當前的 MP
			writeS(Integer.toString(pet.getMaxMp())); // 最大 MP
			writeS(Integer.toString(pet.getLevel())); // 等級

			// 如果名字的字符數超過8個，會顯示錯誤
			// 例如"聖伯納德犬"和"偏斜的伊芙兔"是可以的
			// String pet_name = pet.get_name();
			// if (pet_name.equalsIgnoreCase("海德貝爾曼")) {
			// pet_name = "海德貝爾瑪";
			// }
			// else if (pet_name.equalsIgnoreCase("高聖伯納德犬")) {
			// pet_name = "高聖伯納";
			// }
			// writeS(pet_name);
			writeS(pet.getName()); // 顯示寵物名字會不穩定，因此不顯示
			writeS("$612"); // 滿腹
			writeS(Integer.toString(exppercet)); // 經驗值
			writeS(Integer.toString(pet.getLawful())); // 合法性
		} else if (npc instanceof L1SummonInstance) { // 召喚怪物
			L1SummonInstance summon = (L1SummonInstance) npc;
			writeD(summon.getId());
			writeS("moncom");
			writeC(0x00);
			writeH(9); // 傳遞的角色數量的樣式
			switch (summon.get_currentPetStatus()) {
				case 1:
					writeS("$469"); // 攻擊姿態
					break;
				case 2:
					writeS("$470"); // 防禦姿態
					break;
				case 3:
					writeS("$471"); // 休息
					break;
				case 5:
					writeS("$472"); // 警戒
					break;
				default:
					writeS("$471"); // 休息
					break;
			}
			writeS(Integer.toString(summon.getCurrentHp())); // 當前的 HP
			writeS(Integer.toString(summon.getMaxHp())); // 最大 HP
			writeS(Integer.toString(summon.getCurrentMp())); // 當前的 MP
			writeS(Integer.toString(summon.getMaxMp())); // 最大 MP
			writeS(Integer.toString(summon.getLevel())); // 等級
			// writeS(summon.getNpcTemplate().get_nameid());
			// writeS(Integer.toString(0));
			// writeS(Integer.toString(790));
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

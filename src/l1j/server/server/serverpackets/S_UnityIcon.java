package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UnityIcon extends ServerBasePacket {

	public S_UnityIcon(byte[] datas) {
		writeC(Opcodes.S_EVENT);
		writeC(0x14);
		writeC(0x7E);
		writeByte(datas);
	}

	public S_UnityIcon(int DECREASE, int DECAY_POTION, int SILENCE, int VENOM_RESIST, int WEAKNESS, int DISEASE,
					   int DRESS_EVASION, int BERSERKERS, int NATURES_TOUCH, int WIND_SHACKLE,
					   int ERASE_MAGIC, int ADDITIONAL_FIRE, int ELEMENTAL_FALL_DOWN, int ELEMENTAL_FIRE,
					   int STRIKER_GALE, int SOUL_OF_FLAME, int POLLUTE_WATER,
					   int EXP_POTION, int SCROLL, int SCROLLTYPE,
					   int CONCENTRATION, int INSIGHT, int PANIC,
					   int MORTAL_BODY, int HORROR_OF_DEATH, int FEAR,
					   int PATIENCE, int GUARD_BREAK, int DRAGON_SKIN, int STATUS_FRUIT,
					   int COMA, int COMA_TYPE, int CRAY_TIME, int CRAY, int MAAN_TIME, int MAAN, int FEATHER_BUFF, int FEATHER_TYPE,
					   int SCROLL_TIME) {
		writeC(Opcodes.S_EVENT);
		writeC(0x14);
		writeC(0x7e);
		writeC(0x00);
		writeC(0x00);
		writeD(0);
		writeC(DECREASE);                 // 減少重量
		writeC(DECAY_POTION);             // 衰退藥水
		writeC(0x00);                     // 絕對屏障
		writeC(SILENCE);                  // 沉默
		writeC(VENOM_RESIST);             // 毒抗性
		writeC(WEAKNESS);                 // 虛弱
		writeC(DISEASE);                  // 疾病
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);                     // 阿凡達
		writeC(0x00);
		writeC(0x00);
		writeC(DRESS_EVASION);            // 裙擺迴避！
		writeC(BERSERKERS);               // 狂暴！
		writeC(NATURES_TOUCH);            // 大自然的觸碰
		writeC(WIND_SHACKLE);             // 風之枷鎖
		writeC(ERASE_MAGIC);              // 抹除魔法
		writeC(0x00);                     // 鏡像反射
		writeC(ADDITIONAL_FIRE);          // 額外火焰
		writeC(ELEMENTAL_FALL_DOWN);      // 元素墜落
		writeC(0x00);
		writeC(ELEMENTAL_FIRE);           // 元素火焰
		writeC(0x00);
		writeC(0x00);                     // 除去跡象
		writeC(0x00);
		writeC(STRIKER_GALE);             // 打擊風暴
		writeC(SOUL_OF_FLAME);            // 火焰之魂
		writeC(POLLUTE_WATER);            // 污水
		writeC(0x00);
		writeC(0x00);                     // 攻擊時間
		writeC(0x00);                     // 時間
		writeC(0x00);                     // 屬性抗性
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);                     // 智慧藥水
		writeC(EXP_POTION);               // 經驗藥水
		writeC(SCROLL);                   // 戰鬥增強卷軸
		writeC(SCROLLTYPE);               // 卷軸類型
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(CONCENTRATION);            // 專注
		writeC(INSIGHT);                  // 洞察
		writeC(PANIC);                    // 恐慌
		writeC(MORTAL_BODY);              // 致命身體
		writeC(HORROR_OF_DEATH);          // 死亡恐懼
		writeC(FEAR);                     // 恐懼
		writeC(PATIENCE);                 // 忍耐
		writeC(GUARD_BREAK);              // 護衛破壞
		writeC(DRAGON_SKIN);              // 龍之皮膚
		writeC(STATUS_FRUIT);             // 狀態果實
		writeC(0x14);
		writeC(0x00);
		writeC(COMA);                     // 時間
		writeC(COMA_TYPE);                // 類型
		writeC(0x00);
		writeC(0x00);
		writeC(0x26);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeD(System.currentTimeMillis());
		writeC(CRAY_TIME);                // (int)(codetest+0.5) / 32
		writeC(CRAY);                     // 45狂怒祝福, 60巫女祝福
		writeC(MAAN_TIME);                // (int)(codetest+0.5) / 32
		writeC(MAAN);                     // 46地龍, 47水龍, 48風龍, 49火龍, 50地龍,水龍 51地龍,水龍,風龍 52地龍,水龍,風龍,火龍
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(FEATHER_BUFF);             // (int)(codetest+0.5) / 16
		writeC(FEATHER_TYPE);             // 70=全部 71工事,咒術力量,最大HP/MP 傷害減少增加, 72最大HP, MP增加 AC提升, 73AC提升
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x86);
		writeC(0x01);
		writeC(SCROLL_TIME);              // 戰鬥增強卷軸 13 分鐘以上 1, 13 分鐘以下 0
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0xba);
		writeC(0x24);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0xe3);
		writeC(0x00);
// 160
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
// 170
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
// 180
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0xb4);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
// 190
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
// 200
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0xe3);
		writeC(0x06);
// 210
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeH(0x00);
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}



	@override
	public String getType() {
		return "[S] S_UnityIcon";
	}
}

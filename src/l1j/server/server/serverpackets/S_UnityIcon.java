package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UnityIcon extends l1j.server.server.serverpackets.ServerBasePacket {
	public S_UnityIcon(byte[] datas){
		writeC(Opcodes.S_EVENT);
		writeC(0x14);
		writeC(0x7E);
		writeByte(datas);
	}
	
	public S_UnityIcon(int DECREASE, int DECAY_POTION, int SILENCE, int VENOM_RESIST, int WEAKNESS, int DISEASE,
			int DRESS_EVASION, int BERSERKERS, int NATURES_TOUCH, int WIND_SHACKLE, 
			int ERASE_MAGIC, int ADDITIONAL_FIRE, int ELEMENTAL_FALL_DOWN, int ELEMENTAL_FIRE,
			int STRIKER_GALE, int SOUL_OF_FLAME, int POLLUTE_WATER,
			int EXP_POTION, int SCROLL, int SCROLLTPYE,
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
		writeC(DECREASE);                 // 降低重量 DECREASE
		writeC(DECAY_POTION);             // 腐朽藥水
		writeC(0x00);                     // 絕對屏障
		writeC(SILENCE);                  // 沉默
		writeC(VENOM_RESIST);             // 毒液抗性
			// 10
		writeC(WEAKNESS);                 // 虛弱
		writeC(DISEASE);                  // 疾病
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);                     // 狀態
		writeC(0x00);
		writeC(0x00);
		writeC(DRESS_EVASION);            // 迴避裝備！
		// 20
		writeC(BERSERKERS);               // 狂戰士！
		writeC(NATURES_TOUCH);            // 自然之觸
		writeC(WIND_SHACKLE);             // 風之枷鎖 10
		writeC(ERASE_MAGIC);              // 抹除魔法
		writeC(0x00);                     // 反射鏡
		writeC(ADDITIONAL_FIRE);          // 附加火焰
		writeC(ELEMENTAL_FALL_DOWN);      // 元素落下
		writeC(0x00);
		writeC(ELEMENTAL_FIRE);           // 元素火焰
		writeC(0x00);
		// 30
		writeC(0x00);                     // 隱身劑
		writeC(0x00);
		writeC(STRIKER_GALE);             // 狙擊疾風
		writeC(SOUL_OF_FLAME);            // 火焰之魂
		writeC(POLLUTE_WATER);            // 污染水
		writeC(0x00);
		writeC(0x00);                     // 攻擊可用時間
		writeC(0x00);                     // 1-巨大魔族 2-巨大魔族 3-巫女薩埃爾 4-怨恨幽靈 5-怨恨哈拉長 6-再生祭壇
		writeC(0x00);                     // 時間
		writeC(0x00);                     // 0-所有屬性抵抗 1-最大HP30 2-MP回復3 3-AC1 4-最大MP20 5-HP回復3 6-MR5
		// 40
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);                     // 藥水
		writeC(EXP_POTION);               // 經驗藥水
		writeC(SCROLL);                   // 戰鬥強化卷軸 123 都有嗎?
		writeC(SCROLLTPYE);               // 0-hp50hpr4, 1-mp40mpr4, 2-追加攻擊3攻城3sp3 20
		// 50
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(CONCENTRATION);            // 專注
		writeC(INSIGHT);                  // 洞察
		writeC(PANIC);                    // 恐慌
		writeC(MORTAL_BODY);              // 致命之體
		writeC(HORROR_OF_DEATH);          // 死亡恐懼
		writeC(FEAR);                     // 恐懼
		// 60
		writeC(PATIENCE);                 // 忍耐
		writeC(GUARD_BREAK);              // 防衛破壞
		writeC(DRAGON_SKIN);              // 龍之皮膚
		writeC(STATUS_FRUIT);             // 生命之果  30
		writeC(0x14);
		writeC(0x00);
		writeC(COMA);                     // 昏迷時間
		writeC(COMA_TYPE);                // 昏迷類型
		writeC(0x00);
		writeC(0x00);
		// 70
		writeC(0x26);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeD(System.currentTimeMillis());
		writeC(CRAY_TIME);                // (int)(codetest+0.5) / 32
		writeC(CRAY);                     // 45 克雷祝福, 60 巫女薩埃爾祝福
		// 80
		writeC(MAAN_TIME);                // (int)(codetest+0.5) / 32
		writeC(MAAN);                     // 46 地龍, 47 水龍, 48 風龍, 49 火龍, 50 地龍,水龍 51 地龍,水龍,風龍 52 地龍,水龍,風龍,火龍
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		//90
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(FEATHER_BUFF);            // (智力)(codetest+0.5) / 16
		writeC(FEATHER_TYPE);            // 70= 全部 71攻城,咒術力,最大HP/MP 傷害減少增加, 72最大HP,MP增加 AC提升, 73AC提升
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
// 100
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x86);
		writeC(0x01);
		writeC(SCROLL_TIME);             // 戰強 13分鐘以上 1, 13分鐘以下 0
		writeC(0x00);  
		// 110
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 120
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
		// 130
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
		// 140
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
		// 150
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

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

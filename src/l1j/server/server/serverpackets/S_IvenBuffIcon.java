package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_IvenBuffIcon extends ServerBasePacket {

	public static final int SHOW_INVEN_BUFFICON = 110;

	public S_IvenBuffIcon(int skillId, boolean on, int msgNum, int time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);

		writeC(0x08);
		writeBit(on ? 2 : 3); // 類型 開啟 : 關閉

		writeC(0x10);
		writeBit(skillId); // 技能 ID

		writeC(0x18);
		writeBit(time); // 時間

		writeC(0x20);
		/**
		 * 0,4,6,8,13 : 剩餘時間（以秒為單位） 1 : 剩餘祝福指數 2 : 剩餘時間（以分鐘為單位） 3,5 : 無反應
		 * 7 : 以日 時 分為單位 9 : 以秒為單位且無描述文本和空格 10 : 無限制 11 : 斷開 12 : 無效
		 */
		int duration_show_type = 8;
		if(skillId >= 4075 && skillId <= 4094)
			duration_show_type = 9;
		writeBit(duration_show_type); // duration_show_type（顯示剩餘時間的類型）

		writeC(0x28);

		int iven_icon = skillId;
		if(skillId >= 4075 && skillId <= 4094)
			iven_icon = 6679;
		writeBit(iven_icon); // 開始時顯示的圖標編號

		writeC(0x30);
		writeBit(0); // 結束時顯示的圖標編號

		writeC(0x38);
		/**
		 * 0 或 1 : 商店或安全類 2 或以上 : 其他增益類
		 */
		int skill_order_number = 3;
		if(skillId >= 4075 && skillId <= 4094)
			skill_order_number = 0;
		writeBit(skill_order_number); // icon_priority（圖標優先級）

		writeC(0x40);
		writeBit(msgNum); // 字串編號（圖標內的內容）

		writeC(0x48);
		writeBit(0); // 增益開始時的聊天消息

		writeC(0x50);
		writeBit(0); // 增益結束時的聊天消息

		writeC(0x58);
		writeBit(0x01); // is_good（區分增益 / 減益）

		writeC(0x60);
		int overlap_buff_icon = 0;
		if(skillId >= 4075 && skillId <= 4094)
			overlap_buff_icon = 1;
		writeBit(overlap_buff_icon); // overlap_buff_icon（重疊增益圖標）

		writeC(0x68);
		int main_tooltip_str_id = 0;
		if(skillId >= 4075 && skillId <= 4094)
			main_tooltip_str_id = 4328;
		writeBit(main_tooltip_str_id); // main_tooltip_str_id（主要工具提示字串 ID）

		writeC(0x70);
		int buff_icon_priority = 0;
		if(skillId >= 4075 && skillId <= 4094)
			buff_icon_priority = 1;
		writeBit(buff_icon_priority); // buff_icon_priority（增益圖標優先級）

		writeH(0x00);
	}

	/**
	 * 使用瞬間移動支配戒指
	 * @param on 狀態開啟或關閉
	 */
	public S_IvenBuffIcon(boolean on) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);
		writeC(0x08);
		writeBit(on ? 2 : 3); // 類型 開啟 : 關閉
		writeC(0x10);
		writeBit(8463); // 技能 ID
		if (on) {
			writeC(0x18);
			writeBit(60); // 時間
			writeC(0x20);
			/**
			 * 0,4,6,8,13 : 剩餘時間（以秒為單位） 1 : 剩餘祝福指數 2 : 剩餘時間（以分鐘為單位） 3,5 : 無反應
			 * 7 : 以日 時 分為單位 9 : 以秒為單位且無描述文本和空格 10 : 無限制 11 : 斷開 12 : 無效
			 */
			writeBit(10); // duration_show_type
			writeC(0x28);
			writeBit(8463); // 開始時顯示的圖標編號
			writeC(0x30);
			writeBit(0); // 結束時顯示的圖標編號
			writeC(0x38);
			/**
			 * 商店或安全類 : 0 或 1 其他增益類 : 2 或以上
			 */
			writeBit(3); // icon_priority（圖標優先級）
			writeC(0x40);
			writeBit(5119); // 字串編號（圖標內的內容）
			writeC(0x48);
			writeBit(0); // 增益開始時的聊天消息
			writeC(0x50);
			writeBit(0); // 增益結束時的聊天消息
			writeC(0x58);
			writeBit(0x01); // is_good（區分增益 / 減益）
			writeC(0x60);
			writeBit(0x00); // overlap_buff_icon（重疊增益圖標）
			writeC(0x68);
			writeBit(0x00); // main_tooltip_str_id（主要工具提示字串 ID）
			writeC(0x70);
			writeBit(0x00); // buff_icon_priority（增益圖標優先級）
		} else {
			writeC(0x30);
			writeBit(0); // 結束時顯示的圖標編號
			writeC(0x50);
			writeBit(0); // 增益結束時的聊天消息
		}
		writeH(0x00);
	}

	public byte[] getContent() {
		return getBytes();
	}
}



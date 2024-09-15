package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_IvenBuffIcon extends ServerBasePacket {

	public static final int SHOW_INVEN_BUFFICON = 110;

	public S_IvenBuffIcon(int skillId, boolean on, int msgNum, int time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);
		
		writeC(0x08);
		writeBit(on ? 2 : 3); // type on : off
		
		writeC(0x10);
		writeBit(skillId); // 技能ID

		writeC(0x18);
		writeBit(time); // 時間

		writeC(0x20);
		/**
		 * 0,4,6,8,13 : 剩餘時間以秒顯示 1 : 剩餘祝福值 2 : 以分鐘顯示 3,5 : 無反應 7 : 以天 小時 分
		 * 顯示 9 : 以秒顯示 無說明文字及空格 10 : 無限顯示 11 : 崩潰 12 : 虛擬
		 */
		int duration_show_type = 8;
		if(skillId >= 4075 && skillId <= 4094)
			duration_show_type = 9;
		writeBit(duration_show_type); // duration_show_type
		
		writeC(0x28);
		
		int iven_icon = skillId;
		if(skillId >= 4075 && skillId <= 4094)
			iven_icon = 6679;
		writeBit(iven_icon); // 開始時顯示的背包圖像編號

		writeC(0x30);
		writeBit(0); // 結束時顯示的背包圖像編號
		
		writeC(0x38);
		/**
		 * 類型商店、保安類：0 或 1 其他增益類：2 以上
		 */
		int skill_order_number = 3;
		if(skillId >= 4075 && skillId <= 4094)
			skill_order_number = 0;
		writeBit(skill_order_number); // icon_priority (優先順序)

		writeC(0x40);
		writeBit(msgNum); // 字串編號(圖標內的內容)

		writeC(0x48);
		writeBit(0); // 增益開始時的聊天訊息

		writeC(0x50);
		writeBit(0); // 增益結束時的聊天訊息

		writeC(0x58);
		writeBit(0x01); // is_good (區分增益/減益)
		
		writeC(0x60);
		int overlap_buff_icon = 0;
		if(skillId >= 4075 && skillId <= 4094)
			overlap_buff_icon = 1;
		writeBit(overlap_buff_icon); // overlap_buff_icon
		
		writeC(0x68);
		int main_tooltip_str_id = 0;
		if(skillId >= 4075 && skillId <= 4094)
			main_tooltip_str_id = 4328;
		writeBit(main_tooltip_str_id); // main_tooltip_str_id
		
		writeC(0x70);
		int buff_icon_priority = 0;
		if(skillId >= 4075 && skillId <= 4094)
			buff_icon_priority = 1;
		writeBit(buff_icon_priority); // buff_icon_priority

		writeH(0x00);
	}

	/**
	 * 使用瞬間移動支配戒指
	 * @param on
	 */
	public S_IvenBuffIcon(boolean on) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);
		writeC(0x08);
		writeBit(on ? 2 : 3); // type on : off
		writeC(0x10);
		writeBit(8463); // 技能ID
		if (on) {
			writeC(0x18);
			writeBit(60); // 時間
			writeC(0x20);
			/**
			 * 0,4,6,8,13 : 剩餘時間以秒顯示 1 : 剩餘祝福值 2 : 以分鐘顯示 3,5 : 無反應 7 : 以天 小時
			 * 分 顯示 9 : 以秒顯示 無說明文字及空格 10 : 無限顯示 11 : 崩潰 12 : 虛擬
			 */
			writeBit(10); // 顯示持續時間類型
			writeC(0x28);
			writeBit(8463); // 開始時顯示的背包圖像編號
			writeC(0x30);
			writeBit(0); // 結束時顯示的背包圖像編號
			writeC(0x38);
			/**
			 * 商店,保安類：0 或 1 其他增益類：2 以上
			 */
			writeBit(3); // icon_priority (優先順序)
			writeC(0x40);
			writeBit(5119); // 字串編號(圖標內的內容)
			writeC(0x48);
			writeBit(0); // 增益開始時的聊天訊息
			writeC(0x50);
			writeBit(0); // 增益結束時的聊天訊息
			writeC(0x58);
			writeBit(0x01); // is_good (區分增益/減益)
			writeC(0x60);
			writeBit(0x00); // 重疊增益圖標
			writeC(0x68);
			writeBit(0x00); // 主要提示字串編號
			writeC(0x70);
			writeBit(0x00); // 增益圖標優先順序
		} else {
			writeC(0x30);
			writeBit(0); // 結束時顯示的背包圖像編號
			writeC(0x50);
			writeBit(0); // 增益結束時的聊天訊息
		}
		writeH(0x00);
	}

	public byte[] getContent() {
		return getBytes();
	}
}
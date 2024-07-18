package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_DisplayEffect extends ServerBasePacket {
	public static final int     PUPLE_BORDER_DISPLAY            = 0x01;    // 紫色邊框顯示效果
	public static final int     QUAKE_DISPLAY                    = 0x02;    // 地震顯示效果
	public static final int     FIREWORK_DISPLAY                = 0x03;    // 煙火顯示效果
	public static final int     RINDVIOR_LIGHT_DISPLAY            = 0x04;    // 林德維奧閃電效果顯示
	public static final int     LIFECRY_DISPLAY                    = 0x05;    // 生存吶喊飽食效果
	public static final int        BLUE_BORDER_DISPLAY                = 0x06;    // 藍色邊框顯示效果
	public static final int     VALAKAS_BORDER_DISPLAY             = 0x07;    // 瓦拉卡斯火焰邊框顯示效果
	public static final int     RINDVIOR_BORDER_DISPLAY         = 0x08;    // 林德維奧藍灰色邊框顯示效果
	public static final int     BLACK_DISPLAY                     = 0x0A;    // 畫面變暗顯示效果
	public static final int     BLOOD_DUNGEON_WHITE             = 0x11;    // 血盟集結地效果
	public static final int     BLOOD_DUNGEON_GREEN             = 0x12;    // 血盟集結地效果
	public static final int     BLOOD_DUNGEON_BLUE               = 0x13;    // 血盟集結地效果
	public static final int     BLOOD_DUNGEON_RED                = 0x14;    // 血盟集結地效果
	public static final int     BLOOD_DUNGEON_PUPLE                = 0x15;    // 血盟集結地效果
	public static final int     BLOOD_DUNGEON_RAINBOW            = 0x16;    // 血盟集結地效果
}
	
	public static S_DisplayEffect newInstance(int value){
		S_DisplayEffect eff = newInstance();
		eff.writeD(value);
		eff.writeH(0x00);
		return eff;
	}
	
	public static S_DisplayEffect newInstance(){
		return new S_DisplayEffect();
	}
	
	/** 傳輸螢幕自身的效果. **/
	public S_DisplayEffect(int value){
		writeC(Opcodes.S_EVENT);
		writeC(0x53);
		writeD(value);
		writeH(0x00);
	}
	
	private S_DisplayEffect(){
		writeC(Opcodes.S_EVENT);
		writeC(0x53);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_DisplayEffect";
	}
}



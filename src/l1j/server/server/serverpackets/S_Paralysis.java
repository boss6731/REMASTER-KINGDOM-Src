package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Paralysis extends ServerBasePacket {
	public S_Paralysis(int type){
		writeC(Opcodes.S_PARALYSE);
		writeC(type);
	}
	public S_Paralysis(byte[] buff) {
		writeC(Opcodes.S_PARALYSE);
		writeByte(buff);
	}

	public S_Paralysis(int type, boolean flag) {
		writeC(Opcodes.S_PARALYSE);
		switch(type){
			case TYPE_PARALYSIS:{
				writeC(flag ? 2 : 3);
				break;
			}
			case TYPE_PARALYSIS2:{
				writeC(flag ? 4 : 5);
				break;        // 身體完全麻痹了。
			}
			case TYPE_TELEPORT_UNLOCK:{
				writeC(7);
				break;                // 解除傳送等待狀態
			}
			case TYPE_SLEEP:{
				writeC(flag ? 10 : 11);
				break;    // 被強大的睡魔襲擊，入睡了。
			}
			case TYPE_FREEZE:{
				writeC(flag ? 12 : 13);
				break;    // 身體凍結了。
			}
			case TYPE_STUN:{
				writeC(flag ? 22 : 23);
				break;    // 處於昏迷狀態。
			}
			case TYPE_BIND:{
				writeC(flag ? 24 : 25);
				break;    // 不能像雙腿被束縛一樣移動。
			}
			case TYPE_RIP:{
				writeC(flag ? 26 : 27);
				break;    // 力量的握持
			}
			case TYPE_PERADO:{
				writeC(flag ? 30 : 31);
				break;    // 絕望的反抗
			}
			case TYPE_FORCE_STUN:{
				writeC(flag ? 34 : 35);
				break;    // 力量昏迷
			}
			case TYPE_PANTHERA:{
				writeC(flag ? 36 : 37);
				break;    // 永恆
			}
			case TYPE_PHANTOM:{
				writeC(flag ? 38 : 39);
				break;    // 幻影
			}
			case TYPE_OSIRIS:{
				writeC(flag ? 40 : 41);
				break;    // 幻影
			}
			default:{
				writeH(0x00);
				break;
			}
		}
	}
	/*
	if (type == TYPE_PARALYSIS) // 身體完全麻痹了。
	{
	if (flag == true) {
	writeC(2);
	} else {
	writeC(3);
	}
	} else if (type == TYPE_PARALYSIS2) // 身體完全麻痹了。
	{
	if (flag == true) {
	writeC(4);
	} else {
	writeC(5);
	}
	} else if (type == TYPE_TELEPORT_UNLOCK) { // 解除傳送等待狀態
	writeC(7);
	} else if (type == TYPE_SLEEP) // 被強大的睡魔襲擊，入睡了。
	{
	if (flag == true) {
	writeC(10);
	} else {
	writeC(11);
	}
	} else if (type == TYPE_FREEZE)  // 身體凍結了。
	{
	if (flag == true) {
	writeC(12);
	} else {
	writeC(13);
	}
	} else if (type == TYPE_STUN) // 處於昏迷狀態。
	{
	if (flag == true) {
	writeC(22);
	} else {
	writeC(23);
	}
	} else if (type == TYPE_BIND) // 不能像雙腿被束縛一樣移動。
	{
	if (flag == true) {
	writeC(24);
	} else {
	writeC(25);
	}
	} else if (type == TYPE_RIP) // 力量的握持
	{
	if (flag == true) {
	writeC(26);
	} else {
	writeC(27);
	}
	} else if (type == TYPE_PERADO) // 絕望的反抗
	{
	if (flag == true) {
	writeC(30);
	} else {
	writeC(31);
	}
	} else if (type == TYPE_PHANTOM) // 幻影
	{
	if (flag == true) {
	writeC(38);
	} else {
	writeC(39);
	}
	} else if (type == TYPE_PANTHERA) // 潘瑟拉
	{
	if (flag == true) {
	writeC(34);
	} else {
	writeC(35);
	}
	}
	}
	*/
	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__2F_PARALYSIS;
	}
	
	public static final int TYPE_PARALYSIS 			= 1;

	public static final int TYPE_PARALYSIS2 		= 2;

	public static final int TYPE_SLEEP 				= 3;

	public static final int TYPE_FREEZE 			= 4;

	public static final int TYPE_STUN 				= 5;

	public static final int TYPE_BIND 				= 6;

	public static final int TYPE_TELEPORT_UNLOCK 	= 7;
	
	public static final int TYPE_TELEPORT_UNLOCK2	= 10;

	public static final int TYPE_RIP 				= 8;

	public static final int TYPE_PERADO				= 9;

	public static final int TYPE_FORCE_STUN 		= 10;

	public static final int TYPE_PANTHERA 			= 11; //此功能不能在PvP中使用

	public static final int TYPE_PHANTOM			= 12;
	
	public static final int TYPE_OSIRIS				= 13;
	
	

	private static final String _S__2F_PARALYSIS = "[S] S_Paralysis";
}

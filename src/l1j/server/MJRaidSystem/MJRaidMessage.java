package l1j.server.MJRaidSystem;

import java.util.ArrayList;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public enum MJRaidMessage {
	RAID_SMSG_DUMMY("", false),
	RAID_GMSG_DUMMY("", true),
	RAID_OPEN_FAIL_INVALID_TYPE("無法識別的 RAID 類型。", false),
	RAID_OPEN_FAIL_INVALID_ITEM("未滿足物品要求條件。", false),
	RAID_OPEN_FAIL_SIZEOVER("當前所有實例都在使用中。", false),
	//    RAID_OPEN_SUCCESS_MESSAGE("鋼鐵公會矮人：呃。。。龍的咆哮聲傳到了這裡。顯然有人打開了龍的傳送門！榮耀與祝福屬於準備好的龍之殺手！", true),
	RAID_OPEN_SUCCESS_MESSAGE("鋼鐵公會矮人：你們不是哈帕斯神父的對手。", true),
	RAID_INPUT_FAIL_BEGIN("RAID 已經開始，無法進入。", false),
	RAID_INPUT_FAIL_OVERUSER("超過可進入人數。", false),
	RAID_INPUT_FAIL_STILLBUFF("由於龍 RAID 的魔法，無法參加 RAID。", false),
	RAID_CLEAR_SUCCESS_MESSAGE("矮人的呼喊：制止了龍的黑暗氣息的勇士誕生了!!", true);

	// 成員變量
	private final String message; // 消息字符串
	private final boolean global; // 是否是全局消息

	// 構造函數
	MJRaidMessage(String message, boolean global) {
		this.message = message;
		this.global = global;
	}

	// 獲取消息字符串
	public String getMessage() {
		return message;
	}

	// 判斷是否是全局消息
	public boolean isGlobal() {
		return global;
	}
}
	
	private String 			_msg;
	private boolean 		_isSystemMessage;
	private S_SystemMessage _sMsg;
	private S_PacketBox		_sBox;
	MJRaidMessage(String msg, boolean isSystemMessage){
		_msg 				= msg;
		_isSystemMessage 	= isSystemMessage;
		_sMsg 				= new S_SystemMessage(msg);
		_sBox				= new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
	}
	
	public String get(){
		return _msg;
	}
	public void sendMessage(L1PcInstance pc){
		if(_isSystemMessage)
			sendSystemMessage(pc);
		else
			sendGreenMessage(pc);
	}
	public void sendSystemMessage(L1PcInstance pc){
		pc.sendPackets(_sMsg, false);
	}
	public void sendGreenMessage(L1PcInstance pc){
		pc.sendPackets(_sBox, false);
	}
	
	public void sendMessage(L1PcInstance pc, String[] arrs){
		if(_isSystemMessage)
			sendSystemMessage(pc, arrs);
		else
			sendGreenMessage(pc, arrs);
	}
	public void sendSystemMessage(L1PcInstance pc, String[] arrs){
		StringBuilder sb = new StringBuilder(_msg.length() + arrs.length * 5);
		sb.append(_msg);
		for(int i=0; i<arrs.length; i++)
			sb.append(arrs[i]);
		pc.sendPackets(new S_SystemMessage(sb.toString()), true);
	}
	public void sendGreenMessage(L1PcInstance pc, String[] arrs){
		StringBuilder sb = new StringBuilder(_msg.length() + arrs.length * 5);
		sb.append(_msg);
		for(int i=0; i<arrs.length; i++)
			sb.append(arrs[i]);
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, sb.toString()), true);
	}
	
	public void sendMessage(ArrayList<L1PcInstance> pcs){
		if(_isSystemMessage)
			sendSystemMessage(pcs);
		else
			sendGreenMessage(pcs);
	}
	public void sendSystemMessage(ArrayList<L1PcInstance> pcs){
		int size = pcs.size();
		L1PcInstance pc;
		for(int i=0; i<size; i++){
			pc = pcs.get(i);
			if(pc != null)
				pc.sendPackets(_sMsg, false);
		}
	}
	public void sendGreenMessage(ArrayList<L1PcInstance> pcs){
		int size = pcs.size();
		L1PcInstance pc;
		for(int i=0; i<size; i++){
			pc = pcs.get(i);
			if(pc != null)
				pc.sendPackets(_sBox, false);
		}
	}
}

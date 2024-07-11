package l1j.server.MJServerMacroSystem;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.serverpackets.S_ChatMessageNoti;
import l1j.server.server.serverpackets.S_IconMessage;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJServerMacroObject implements Comparable<MJServerMacroObject>{
	private static final int MACRO_TYPE_SYS 	= 0;
	private static final int MACRO_TYPE_ICO 	= 1;
	private static final int MACRO_TYPE_GRN 	= 2;
	private static final int MACRO_TYPE_NTCE 	= 3;
	private static final int MACRO_TYPE_WHSP 	= 4;
	
	public int							id;
	public int							curIdx;
	public ArrayList<ServerBasePacket> 	pcks;
	public long							delay;
	public long							nextMs;
	public MJServerMacroObject(int i, long d, long cur){
		id 		= i;
		delay	= d;
		nextMs	= cur + d;
		pcks	= new ArrayList<ServerBasePacket>();
		curIdx	= 0;
	}

	public void add(String msg, String stype) {
		int type = 0;

// 根據輸入的訊息類型字串設定對應的宏類型
		if(stype.equalsIgnoreCase("圖標")) //
			type = MACRO_TYPE_ICO;
		else if(stype.equalsIgnoreCase("綠色")) //
			type = MACRO_TYPE_GRN;
		else if(stype.equalsIgnoreCase("公告")) //
			type = MACRO_TYPE_NTCE;
		else if(stype.equalsIgnoreCase("悄悄話")) //
			type = MACRO_TYPE_WHSP;

// 根據宏類型添加相應的訊息包
		if(type == MACRO_TYPE_SYS)
			pcks.add(new S_SystemMessage(msg)); // 添加系統消息
		else if(type == MACRO_TYPE_ICO)
			pcks.add(S_IconMessage.getGmMessage(msg)); // 添加GM圖標消息
		else if(type == MACRO_TYPE_GRN)
			pcks.add(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg)); // 添加綠色消息框
		else if(type == MACRO_TYPE_NTCE)
			pcks.add(S_ChatMessageNoti.getNotice(msg, Config.Message.GameServerName)); // 添加公告消息
		else if(type == MACRO_TYPE_WHSP)
// pcks.add(new S_SystemMessage("(交易方法)"+msg)); //
			pcks.add(S_ChatMessageNoti.getWhisper(msg, "交易方法")); // 添加悄悄話消息，標題為
	}
	
	public ServerBasePacket get(){
		ServerBasePacket sbp = pcks.get(curIdx);
		if(++curIdx >= pcks.size())	curIdx = 0;
		return sbp;
	}

	@Override
	public int compareTo(MJServerMacroObject o) {
		if(nextMs > o.nextMs) 		return 1;
		else if(nextMs < o.nextMs)	return -1;
		if(id > o.id)				return 1;
		else if(id < o.id)			return -1;
		return 0;
	}
}

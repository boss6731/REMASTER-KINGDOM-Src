package l1j.server.MJInstanceSystem;

import java.util.ArrayList;

import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJInstanceEnums {
	/** 表示用戶的實例空間狀態 **/
	public enum InstStatus {, INST_USERSTATUS_LFC;
		public enum InstStatus{
		INST_USERSTATUS_NONE(1), // 當不在實例空間時
		INST_USERSTATUS_LFCREADY(2),
		INST_USERSTATUS_LFCINREADY(4),
		INST_USERSTATUS_LFC(8);		
		
		@SuppressWarnings("unused")
		private int _status;
		InstStatus(int i){
			_status = i;
		}
	}
	
	public enum InstSpcMessages{
		INSTANCE_SPACE_FULL("實例地下城已滿。請稍後再試。");
		
		private String _msg;		
		InstSpcMessages(String msg){
			_msg = msg;
		}
		
		public String get(Object hour){
			return _msg;
		}
		public void sendSystemMsg(L1PcInstance pc){
			pc.sendPackets(new S_SystemMessage(get(hour)));
		}
		public void sendSystemMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_SystemMessage(new StringBuilder(get(hour)).append(" ").append(msg).toString()));
		}
		public void sendGreenMsg(L1PcInstance pc){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get(hour)));
		}
		public void sendGreenMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, new StringBuilder(get(hour)).append(" ").append(msg).toString()));
		}
	}

		/** 註冊相關消息 **/
		public enum LFCMessages {
			REGIST_SUCCESS("註冊已完成。"),
			REGIST_ERR_ININST("實例狀態下無法使用。"),
			REGIST_ERR_NOADENA("缺少所需物品。"),
			REGIST_ERR_INTHEMILL("尚未準備完成。"),
			REGIST_ERR_ADENA("投注金額錯誤。"),
			REGIST_ERR_LEVEL("未達到等級要求。"),
			CREATE_ERR_TARGET_CANNOT("對方（隊伍）目前無法參與。"),
			CREATE_ERR_CANNOT_INPARTYPLAY("在隊伍中無法申請個人戰。"),
			CREATE_ERR_RVR("RVR內容需隊伍參與。"),
			CREATE_ERR_PVP("PVP內容需單人參與。"),
			CREATE_ERR_ONLYLEADER("只有隊長能申請。"),
			CREATE_ERR_PARTYMEMBER("有無法參加的隊伍成員。"),
			CREATE_ERR_PARTYMAXSIZE("超過最多隊伍人數。"),
			CREATE_ERR_PARTYMINSIZE("未達到最低隊伍人數要求。"),
			CREATE_NOTIFY_CANCEL_INPARTYPLAY("已申請進入競技場，但因為在隊伍中（個人戰）/不在隊伍中（團體戰）而被取消。"),
			CREATE_SUBSCRIBE("已申請進入競技場，若在15秒內未接受則視為取消。"),
			CREATE_SUCCESS("競技場申請已完成，若對方在15秒內未接受則視為取消。"),
			CREATE_CANCEL_OWNERUSER("對方拒絕了申請。"),
			CREATE_CANCEL("比賽已取消。"),
			INGAME_CLOSE("比賽已結束，稍後將進行結果判定並返回村莊。"),
			INGAME_CLOSE_FORGM("比賽已被GM強制結束。"),
			INGAME_NOTIFY_WINNER("恭喜獲勝，獎勵物品即將發放。"),
			INGAME_NOTIFY_LOSER("您在LFC戰中敗北。"),
			INGAME_NOTIFY_READY("[比賽準備] "),
			INGAME_NOTIFY_START("開始！"),
			INGAME_NOTIFY_CLOSETIME("[結束即將到來] "),
			INGAME_NOTIFY_LOTTO("恭喜，您中得隨機獎勵。"),;
		private String _msg;		
		LFCMessages(String msg){
			_msg = msg;
		}
		
		public String get(){
			return _msg;
		}
		public void sendSystemMsg(L1PcInstance pc){
			pc.sendPackets(new S_SystemMessage(get()));
		}
		public void sendSystemMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_SystemMessage(new StringBuilder(get()).append(" ").append(msg).toString()));			
		}
		public void sendSystemMsgToList(ArrayList<L1PcInstance> pcs){
			sendList(pcs, new S_SystemMessage(get()));
		}
		public void sendSystemMsgToList(ArrayList<L1PcInstance> pcs, String msg){
			sendList(pcs, new S_SystemMessage(new StringBuilder(get()).append(" ").append(msg).toString()));
		}
		public void sendGreenMsg(L1PcInstance pc){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}
		public void sendGreenMsg(L1PcInstance pc, String msg){
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, new StringBuilder(get()).append(" ").append(msg).toString()));
		}
		public void sendGreenMsgToList(ArrayList<L1PcInstance> pcs){
			sendList(pcs, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}
		public void sendGreenMsgToList(ArrayList<L1PcInstance> pcs, String msg){			
			sendList(pcs, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, new StringBuilder(get()).append(" ").append(msg).toString()));
		}
		
		private void sendList(ArrayList<L1PcInstance> pcs, ServerBasePacket pck){
			int size 		= pcs.size();
			for(int i=0; i<size; i++)
				pcs.get(i).sendPackets(pck, false);
			pck.clear();
		}
		
		public void sendSurvey(L1PcInstance pc){
			pc.sendPackets(new S_Message_YN(C_Attr.MSGCODE_6008_LFC, 6008, get()));
		}
	}
}}

package l1j.server.MJInstanceSystem;

import java.util.ArrayList;

import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJInstanceEnums {
	/** 表示使用者的實例空間狀態。 **/
	public enum InstStatus {
		INST_USERSTATUS_NONE(1), // instance space不在時
		INST_USERSTATUS_LFCREADY(2),
		INST_USERSTATUS_LFCINREADY(4),
		INST_USERSTATUS_LFC(8);

		@SuppressWarnings("unused")
		private int _status;

		InstStatus(int i) {
			_status = i;
		}
	}

	public enum InstSpcMessages {
		INSTANCE_SPACE_FULL("副本已滿。請稍後再試。");

		private String _msg;

		InstSpcMessages(String msg) {
			_msg = msg;
		}

		public String get() {
			return _msg;
		}

		public void sendSystemMsg(L1PcInstance pc) {
			pc.sendPackets(new S_SystemMessage(get()));
		}

		public void sendSystemMsg(L1PcInstance pc, String msg) {
			pc.sendPackets(new S_SystemMessage(new StringBuilder(get()).append(" ").append(msg).toString()));
		}

		public void sendGreenMsg(L1PcInstance pc) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}

		public void sendGreenMsg(L1PcInstance pc, String msg) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					new StringBuilder(get()).append(" ").append(msg).toString()));
		}
	}

	/** 註冊相關消息 **/
	public enum LFCMessages {
		REGIST_SUCCESS("註冊已完成。"),
		REGIST_ERR_ININST("無法在副本狀態下使用。"),
		REGIST_ERR_NOADENA("所需物品不足。"),
		REGIST_ERR_INTHEMILL("尚在準備中。"),
		REGIST_ERR_ADENA("投注金額錯誤。"),
		REGIST_ERR_LEVEL("未滿足等級條件。"),
		CREATE_ERR_TARGET_CANNOT("對方（隊伍）目前無法參加。"),
		CREATE_ERR_CANNOT_INPARTYPLAY("組隊中無法申請個人賽。"),
		CREATE_ERR_RVR("RVR內容是組隊內容。"),
		CREATE_ERR_PVP("PVP內容是非組隊內容。"),
		CREATE_ERR_ONLYLEADER("僅隊長可申請。"),
		CREATE_ERR_PARTYMEMBER("有無法參加的隊員。"),
		CREATE_ERR_PARTYMAXSIZE("超過最大組隊人數。"),
		CREATE_ERR_PARTYMINSIZE("未滿足最小組隊人數。"),
		CREATE_NOTIFY_CANCEL_INPARTYPLAY("已申請競技場，但因組隊中（個人賽）/非組隊中（團體賽）而取消。"),
		CREATE_SUBSCRIBE("已申請競技場。若15秒內未接受則視為取消。"),
		CREATE_SUCCESS("競技場申請已完成。若對方15秒內未接受則取消。"),
		CREATE_CANCEL_OWNERUSER("對方拒絕了申請。"),
		CREATE_CANCEL("比賽已取消。"),
		INGAME_CLOSE("比賽已結束。稍後將判定結果並移動至村莊。"),
		INGAME_CLOSE_FORGM("比賽已被GM強制終止。"),
		INGAME_NOTIFY_WINNER("您已獲勝。勝利獎勵將發放。"),
		INGAME_NOTIFY_LOSER("您在LFC賽中失敗。"),
		INGAME_NOTIFY_READY("[比賽準備]"),
		INGAME_NOTIFY_START("開始！"),
		INGAME_NOTIFY_CLOSETIME("[即將結束]"),
		INGAME_NOTIFY_LOTTO("恭喜！您已獲得隨機獎勵。");

		private String _msg;

		LFCMessages(String msg) {
			_msg = msg;
		}

		public String get() {
			return _msg;
		}

		public void sendSystemMsg(L1PcInstance pc) {
			pc.sendPackets(new S_SystemMessage(get()));
		}

		public void sendSystemMsg(L1PcInstance pc, String msg) {
			pc.sendPackets(new S_SystemMessage(new StringBuilder(get()).append(" ").append(msg).toString()));
		}

		public void sendSystemMsgToList(ArrayList<L1PcInstance> pcs) {
			sendList(pcs, new S_SystemMessage(get()));
		}

		public void sendSystemMsgToList(ArrayList<L1PcInstance> pcs, String msg) {
			sendList(pcs, new S_SystemMessage(new StringBuilder(get()).append(" ").append(msg).toString()));
		}

		public void sendGreenMsg(L1PcInstance pc) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}

		public void sendGreenMsg(L1PcInstance pc, String msg) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					new StringBuilder(get()).append(" ").append(msg).toString()));
		}

		public void sendGreenMsgToList(ArrayList<L1PcInstance> pcs) {
			sendList(pcs, new S_PacketBox(S_PacketBox.GREEN_MESSAGE, get()));
		}

		public void sendGreenMsgToList(ArrayList<L1PcInstance> pcs, String msg) {
			sendList(pcs, new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
					new StringBuilder(get()).append(" ").append(msg).toString()));
		}

		private void sendList(ArrayList<L1PcInstance> pcs, ServerBasePacket pck) {
			int size = pcs.size();
			for (int i = 0; i < size; i++)
				pcs.get(i).sendPackets(pck, false);
			pck.clear();
		}

		public void sendSurvey(L1PcInstance pc) {
			pc.sendPackets(new S_Message_YN(C_Attr.MSGCODE_6008_LFC, 6008, get()));
		}
	}
}

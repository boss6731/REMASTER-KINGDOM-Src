package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.npc.L1NpcHtml;

public class S_NPCTalkReturn extends ServerBasePacket {
	private static final String _S__25_TalkReturn = "[S] _S__25_TalkReturn";

	public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action, String[] data) {
		// 初始化 htmlid 為空字符串
		String htmlid = "";

		// 根據 action 設置 htmlid
		if (action == 1) {
			htmlid = npc.getNormalAction();
		} else if (action == 2) {
			htmlid = npc.getChaoticAction(); // 確認這個方法名是否應該是 Chaotic 而不是 Caotic
		} else {
			// 拋出帶有詳細信息的異常
			throw new IllegalArgumentException("Invalid action: " + action);
		}

		// 構建數據包
		buildPacket(objid, htmlid, data);
	}

	public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action) {
		this(npc, objid, action, null);
	}

	public S_NPCTalkReturn(int objid, String htmlid, String[] data) {
		buildPacket(objid, htmlid, data);
	}

	public S_NPCTalkReturn(int objid, String htmlid) {
		buildPacket(objid, htmlid, null);
	}

	public S_NPCTalkReturn(int objid, L1NpcHtml html) {
		buildPacket(objid, html.getName(), html.getArgs());
	}
	public S_NPCTalkReturn(int objid, L1NpcHtml html, String[] data) {
		buildPacket(objid, html.getName(), data);
	}

	private void buildPacket(int objid, String htmlid, String[] data) {
		//        System.out.println("檢查封包9" + htmlid + "+" + data);
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objid);
		writeS(htmlid);
		if (data != null && data.length >= 1) {
			writeH(0x01); // 進入的 NPC HTML ID 部分所需的字節
			writeH(data.length); // 參數的數量
			for (String datum : data) {
				writeS(datum);
			}
		} else {
			writeH(0x00);
			writeH(0x00);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__25_TalkReturn;
	}
}



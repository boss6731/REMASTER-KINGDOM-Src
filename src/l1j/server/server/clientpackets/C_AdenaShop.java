package l1j.server.server.server.clientpackets;

import l1j.server.Config;

import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1AdenShop;
import l1j.server.server.server.serverpackets.*;

public class C_AdenShop extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_AdenShop = "[C] C_AdenShop";

	public <GameClient> C_AdenShop(byte[] decrypt, GameClient client) {
		super(decrypt);
		try {
			int type = readH();
			L1PcInstance pc = client.getActiveChar();
			if (pc == null)
				return;
			switch (type) {
				case 1: // 打開商店
					/*
					 * if (Config.Login.StandbyServer) {
					 *    pc.sendPackets(new S_ChatPacket(pc, "由於目前為待機伺服器狀態，因此此功能不可用。", 1));
					 *    pc.sendPackets(new S_PacketBox(84, "由於目前為待機伺服器狀態，因此此功能不可用。"));
					 *    return;
					 * }
					 */
					if (Config.ServerAdSetting.AdenTo) {
						// pc.sendPackets(new S_TestPacket(S_TestPacket.a, 16, 3590, "00 ff ff"));
						pc.sendPackets(new S_PacketBox(84, "目前金幣商店正在維護中。請稍後再試。"));
						pc.sendPackets(String.valueOf(new l1j.server.server.serverpackets.S_ChatPacket(String.valueOf(pc), "目前金幣商店正在維護中。請稍後再試。", 1)));
						return;
					}

					if (Config.ServerAdSetting.Adentype) {
						client.sendPacket(new S_SurvivalCry(l1j.server.server.serverpackets.S_SurvivalCry.LIST, pc));
						client.sendPacket(new S_SurvivalCry(S_SurvivalCry.EMAIL, pc));
						client.sendPacket(new S_SurvivalCry(S_SurvivalCry.POINT, pc));
						pc.sendPackets(new S_PacketBox(84, "[重複消息] 如需N幣，請聯繫 '梅蒂斯'。"));
						pc.sendPackets("\\aG[重複消息] 如需N幣，請聯繫梅蒂斯。");
					} else {
						client.sendPacket(new S_SurvivalCry(S_SurvivalCry.POINT, pc));
						pc.sendPackets(new l1j.server.server.serverpackets.S_AdenShop(pc, l1j.server.server.serverpackets.S_AdenShop.Currency.ADENA));
					}
					break;
				case 4: { // 輸入OTP
					for (int i = 0; i < 1000; i++) {
						int ff = readH();
						if (ff == 0)
							break;
					}
					for (int i = 0; i < 16 * 8 + 1; i++) {
						readC();
					}
					int size = readH();
					if (size == 0)
						return;

					L1AdenShop as = new L1AdenShop();
					for (int i = 0; i < size; i++) {
						int id = readD();
						int count = readH();
						if (count <= 0 || count >= 10000) {
							return;
						}
						as.add(pc, id, count);
					}

					if (!as.BugOk()) {
						if (as.commit(pc))
							client.sendPacket(new S_SurvivalCry(S_SurvivalCry.OTP_CHECK_MSG, pc));
					}
				}
				break;
				case 6: { // 附加服務倉庫
					pc.sendPackets(new l1j.server.server.serverpackets.S_RetrieveSupplementaryService(pc.getId(), pc));
					//SC_WAREHOUSE_ITEM_LIST_NOTI.send_user_supplementary_service(pc, pc.getId());
				/*if (size > 0)
					SC_WAREHOUSE_ITEM_LIST_NOTI.send_user_supplementary_service(pc, pc.getId());
					//pc.sendPackets(new S_RetrieveSupplementaryService(pc.getId(), pc));
				else
					pc.sendPackets(new S_ServerMessage(1625));*/
				}
				break;
				case 0x32: // 同意及購買
					pc.sendPackets("\\aG[OTP]不需要輸入。");
					pc.sendPackets(new S_PacketBox(84, "\\aG[OTP]不需要輸入。"));
					client.sendPacket(new S_SurvivalCry(S_SurvivalCry.OTP_SHOW, pc));
					break;
			}
		} catch (Exception e) {
		} finally {
		}
	}

	@Override
	public String getType() {
		return "C_AdenShop";
	}

	private class S_SurvivalCry {
		public S_SurvivalCry(Object p0, L1PcInstance pc) {
		}
	}
}
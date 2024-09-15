package l1j.server.server.clientpackets;

import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_Ship extends ClientBasePacket {

	private static final String C_SHIP = "[C] C_Ship";

	public C_Ship(byte abyte0[], GameClient client) {
		super(abyte0);

		int shipMapId = readH();
		int locX = readH();
		int locY = readH();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		int mapId = pc.getMapId();

		switch (mapId) {
		case 5:
			pc.getInventory().consumeItem(40299, 1);
			break;
		case 6:
			pc.getInventory().consumeItem(40298, 1);
			break;
		case 83:
			pc.getInventory().consumeItem(40300, 1);
			break;
		case 84:
			pc.getInventory().consumeItem(40301, 1);
			break;
		case 446:
			pc.getInventory().consumeItem(40303, 1);
			break;
		case 447:
			pc.getInventory().consumeItem(40302, 1);
			break;
		default:
			Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
			client.kick();
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			pc.sendPackets(new S_SystemMessage("不要使用漏洞，該死的混蛋"));
			System.out.println("特定座標移動中繼器漏洞 > " + pc.getName() + " 嘗試移動到地圖 > " + mapId);
			break;
		}
		pc.start_teleport(locX, locY, shipMapId, 0, 18339, true, false);
		client.kick();// 驅逐
	}

	@Override
	public String getType() {
		return C_SHIP;
	}
}

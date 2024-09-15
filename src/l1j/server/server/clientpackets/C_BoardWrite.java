package l1j.server.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1BoardPost;

public class C_BoardWrite extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_BOARD_WRITE = "[C] C_BoardWrite";
	private static Logger _log = Logger.getLogger(C_BoardWrite.class.getName());

	public C_BoardWrite(byte decrypt[], GameClient client) {
		super(decrypt);
		int id = readD();
		String title = readS();
		String content = readS();
		L1PcInstance pc = client.getActiveChar();
		if (pc == null)return;
		L1Object tg = L1World.getInstance().findObject(id);
		if (tg == null) {
			_log.warning("Invalid NPC ID: " + id);
			System.out.println("Invalid NPC ID: " + id);
			return;
		}
		if (title.length() > 16) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("公告板標題字數已超過限制。")));
			return;
		}
		if (tg instanceof L1BoardInstance) {
			L1BoardInstance board = (L1BoardInstance) tg;
			if (board != null) {
				if (pc.getLevel() < Config.ServerAdSetting.BOARDLEVEL && board.getNpcId() != 900006 && board.getNpcId() != 80006 && board.getNpcId() != 500002) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("等級未達 " + Config.ServerAdSetting.BOARDLEVEL + " 或是 GM 公告板。")));
					return;
				}
			}
			switch (board.getNpcId()) {
				case 900006: // 龍鑰匙 board_posts_key
					if (pc.getInventory().checkItem(L1ItemId.DRAGON_KEY, 1)) {
						L1BoardPost.createKey(pc.getName(), title, content);
						pc.sendPackets(String.valueOf(new S_SystemMessage("請在村莊公告板出售龍鑰匙")));
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("您沒有持有龍鑰匙")));
					}
					break;
				case 4200015: // GM伺服器信息公告板 board_posts_notice
					if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
						L1BoardPost.createGM(pc.getName(), title, content);
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("該公告板是管理員專用")));
					return;
				}
				break;
				case 4200020: // GM公告板1 board_notice1
					if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
						L1BoardPost.createGM1(pc.getName(), title, content);
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("該公告板是管理員專用")));
						return;
					}
					break;
				case 4200021: // GM公告板2 board_notice2
					if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
						L1BoardPost.createGM2(pc.getName(), title, content);
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("該公告板是管理員專用")));
						return;
					}
					break;
				case 71008:
				case 4200022: // board_notice3
					if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
						L1BoardPost.createGM3(pc.getName(), title, content);
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("該公告板是管理員專用")));
					return;
				}
				break;
			case 500002:
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 500)) {
					pc.getInventory().consumeItem(L1ItemId.ADENA, 500);
					L1BoardPost.createPhone(pc.getName(), "[建議確認]", content);
					pc.sendPackets(String.valueOf(new S_SystemMessage("帖子已註冊。")));
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage("金幣不足。")));
				}
				break;
				default:
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 500)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 500);
						L1BoardPost.create(pc.getName(), title, content);
						pc.sendPackets(String.valueOf(new S_SystemMessage("帖子註冊完成。")));
					} else {
						pc.sendPackets(String.valueOf(new S_SystemMessage("金幣不足。")));
				}
				break;
			}

		}

	}

	@Override
	public String getType() {
		return C_BOARD_WRITE;
	}
}

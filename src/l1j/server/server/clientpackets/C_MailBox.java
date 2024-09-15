package l1j.server.server.server.clientpackets;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.Config;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.server.GMCommands;

import l1j.server.server.server.datatables.LetterTable;
import l1j.server.server.server.datatables.SpamTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_MailBox extends l1j.server.server.clientpackets.ClientBasePacket {

	public static final int TYPE_PRIVATE_MAIL = 0; // 個人信件
	public static final int TYPE_BLOODPLEDGE_MAIL = 1; // 血盟信件
	public static final int TYPE_KEPT_MAIL = 2; // 保管信件

	public static final int READ_PRIVATE_MAIL = 16; // 個人信件閱讀
	public static final int READ_BLOODPLEDGE_MAIL = 17; // 血盟信件閱讀
	public static final int READ_KEPT_MAIL_ = 18; // 保管信件閱讀

	public static final int WRITE_PRIVATE_MAIL = 32; // 個人信件寫作
	public static final int WRITE_BLOODPLEDGE_MAIL = 33; // 血盟信件寫作

	public static final int DEL_PRIVATE_MAIL = 48; // 個人信件刪除
	public static final int DEL_BLOODPLEDGE_MAIL = 49; // 血盟信件刪除
	public static final int DEL_KEPT_MAIL = 50; // 保管信件刪除

	public static final int TO_KEEP_MAIL = 64; // 信件保管
	public static final int PRICE_PRIVATEMAIL = 50; // 個人信件價格

	public static final int DEL_PRIVATE_LIST_MAIL = 96; // 個人信件列表刪除
	public static final int DEL_BLOODPLEDGE_LIST_MAIL = 97; // 血盟信件列表刪除
	public static final int DEL_KEEP_LIST = 98; // 保管信件列表刪除

	public static final int PRICE_BLOODPLEDGEMAIL = 1000; // 血盟信件價格

	public static final int SIZE_PRIVATE_MAILBOX = 40; // 個人信箱大小
	public static final int SIZE_BLOODPLEDGE_MAILBOX = 80; // 血盟信箱大小
	public static final int SIZE_KEPTMAIL_MAILBOX = 10; // 信件保管箱大小

	private static final String C_MailBox = "[C] C_MailBox";

	public C_MailBox(byte abyte0[], GameClient client) {
		super(abyte0);
		int type = readC();

		if (client == null) {
			return;
		}

		L1PcInstance pc = client.getActiveChar();

		if (pc == null) {
			return;
		}
		switch (type) {
		case TYPE_PRIVATE_MAIL:
			// if (pc.isGm())
			// LetterList(pc, TYPE_PRIVATE_MAIL, 1000);
			// else
			// LetterList(pc, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
			// LetterList(pc, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
			break;
		case TYPE_BLOODPLEDGE_MAIL:
			// LetterList(pc, TYPE_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX);
			break;
		case TYPE_KEPT_MAIL:
			// LetterList(pc, TYPE_KEPT_MAIL, SIZE_KEPTMAIL_MAILBOX);
			break;
		case READ_PRIVATE_MAIL:
			ReadLetter(pc, READ_PRIVATE_MAIL, 0);
			break;
		case READ_BLOODPLEDGE_MAIL:
			ReadLetter(pc, READ_BLOODPLEDGE_MAIL, 0);
			break;
		case READ_KEPT_MAIL_:
			ReadLetter(pc, READ_KEPT_MAIL_, 0);
			break;
		case WRITE_PRIVATE_MAIL:
			WritePrivateMail(pc);
			break;
		case WRITE_BLOODPLEDGE_MAIL:
			WriteBloodPledgeMail(pc);
			break;
		case DEL_PRIVATE_MAIL:
			DeleteLetter(pc, DEL_PRIVATE_MAIL, TYPE_PRIVATE_MAIL);
			break;
		case DEL_BLOODPLEDGE_MAIL:
			DeleteLetter(pc, DEL_BLOODPLEDGE_MAIL, TYPE_BLOODPLEDGE_MAIL);
			break;
		case DEL_KEPT_MAIL:
			DeleteLetter(pc, DEL_KEPT_MAIL, TYPE_KEPT_MAIL);
			break;
		case TO_KEEP_MAIL:
			SaveLetter(pc, TO_KEEP_MAIL, TYPE_KEPT_MAIL);
			break;
		case DEL_PRIVATE_LIST_MAIL:
			DeleteLetter_List(pc, DEL_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
			break;
		case DEL_BLOODPLEDGE_LIST_MAIL:
			DeleteLetter_List(pc, DEL_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX);
			break;
		case DEL_KEEP_LIST:
			DeleteLetter_List(pc, DEL_KEPT_MAIL, SIZE_KEPTMAIL_MAILBOX);
			break;
		default:
			// LetterList(pc,type);
		}
	}

	private void DeleteLetter_List(L1PcInstance pc, int deletetype, int type) {
		int delete_num = readD();
		for (int i = 0; i < delete_num; i++) {
			int id = readD();
			LetterTable.getInstance().deleteLetter(id);
			pc.sendPackets(new S_LetterList(pc, deletetype, id, true));
		}
	}

	private boolean payMailCost(final L1PcInstance RECEIVER, final int PRICE) {
		int AdenaCnt = RECEIVER.getInventory().countItems(L1ItemId.ADENA);
		if (AdenaCnt < PRICE) {
			RECEIVER.sendPackets(String.valueOf(new S_ServerMessage(189, "")));
			return false;
		}

		RECEIVER.getInventory().consumeItem(L1ItemId.ADENA, PRICE);
		return true;
	}

	private static final ConcurrentHashMap<Integer, Integer> letterPendingCount = new ConcurrentHashMap<>();

	private void WritePrivateMail(L1PcInstance sender, String receiver, String subject, String content, int paper) {
		/** 防止炸彈信件的代碼 **/
		long postdelaytime = System.currentTimeMillis() / 1000;
		if (!sender.isGm() && sender.getPostDelay() + 1 > postdelaytime) {
			long time = (sender.getPostDelay() + 1) - postdelaytime;
			sender.sendPackets(time + "秒後可以發送。(連續點擊時客戶端將關閉)");
			System.out.println("" + sender.getName() + " 這位用戶試圖發送炸彈信件。");
			int count = letterPendingCount.getOrDefault(sender.getId(), 0);
			if (count > 10) {
				try {
					letterPendingCount.remove(sender.getId());
					sender.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "瞬間發送過多信件，客戶端將關閉。"));
					sender.getNetConnection().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				letterPendingCount.put(sender.getId(), count + 1);
			}
			return;
		}
		letterPendingCount.remove(sender.getId());

		if (sender.isGm() && receiver.equalsIgnoreCase("公告")) {
			L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE.make_stream(content, MJSimpleRgb.green(), Integer.parseInt(subject)));
			return;
		}
		
		if (sender.getLevel() < Config.ServerAdSetting.LETTERLEVEL) {
			sender.sendPackets(String.valueOf(new S_SystemMessage("等級" + Config.ServerAdSetting.LETTERLEVEL + " 以下無法發送信件。")));
			return;
		}

		if (subject.length() > Config.ServerAdSetting.LETTER_SUBJECT) {
			sender.sendPackets("信件標題過長，無法發送。請在內容中輸入。");
			// System.err.println("標題長度: "+subject.length());
			return;
		}

		if (content.length() > Config.ServerAdSetting.LETTER_CONTENT) {
			sender.sendPackets("信件內容過長，無法發送。");
			// System.err.println("內容長度: "+content.length());
			return;
		}

		if (!payMailCost(sender, PRICE_PRIVATEMAIL)) {
			sender.sendPackets("金幣不足。");
			// System.out.println("信件阻止狀況: if(!payMailCost(sender, PRICE_PRIVATEMAIL)) { : Return" );
			return;
		}

		Timestamp dTime = new Timestamp(System.currentTimeMillis());
		if (!checkCountMail(sender, receiver, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX)) {
			// System.out.println("信件阻止情況: if (!checkCountMail(sender, receiver, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX)) { : Return" );
			return;
		}

		L1PcInstance target = L1World.getInstance().getPlayer(receiver);
		if (target != null) {
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(target.getId());
			if (exList.contains(1, sender.getName())) {
				sender.sendPackets(String.valueOf(new S_ServerMessage(3082)));
				return;
			}
		}
		int id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), receiver, TYPE_PRIVATE_MAIL, subject, content);
		MJUIAdapter.on_receive_letter(id, sender.getName(), subject, content, dTime, receiver);
		if (target != null && target.getOnlineStatus() != 0) {
			target.sendPackets(String.valueOf(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, sender.getName(), subject))); // 收件人

			if (target.isGm()) {
				if (GMCommands._sleepingMessage != null && !GMCommands._sleepingMessage.equalsIgnoreCase(""))
					WritePrivateMail(target, sender.getName(), GMCommands._sleepingTitle, GMCommands._sleepingMessage, paper);
			}
		}
		sender.sendPackets(String.valueOf(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_SEND, sender.getName(), subject)));
		sender.sendPackets(String.valueOf(new S_LetterList(WRITE_PRIVATE_MAIL, true)));
		sender.setPostDelay(postdelaytime + 5);
		sender.sendPackets(new S_LetterList(sender, DEL_PRIVATE_MAIL, id, true));
	}

	private void WritePrivateMail(L1PcInstance sender) {
		int paper = readH(); // 信紙
		String receiverName = readS();
		String subject = readSS();
		String content = readSS();

		WritePrivateMail(sender, receiverName, subject, content, paper);
	}

	private void WriteBloodPledgeMail(L1PcInstance sender) {
		if (!payMailCost(sender, PRICE_BLOODPLEDGEMAIL))
			return;

		if (sender == null)
			return;

		/** 防止炸彈信件的源代碼 **/
		long postdelaytime = System.currentTimeMillis() / 1000;
		if (!sender.isGm() && sender.getPostDelay() + 1 > postdelaytime) {
			long time = (sender.getPostDelay() + 1) - postdelaytime;
			sender.sendPackets(time + "秒後可以發送。");
			// System.out.println("" + sender.getName() + " 這傢伙試圖發送炸彈信件。");
			return;
		}
		/** 防止炸彈信件的源代碼 **/

		int paper = readH(); // 信紙

		Timestamp dTime = new Timestamp(System.currentTimeMillis());
		String receiverName = readS();
		String subject = readSS();
		String content = readSS();

		L1Clan targetClan = null;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(receiverName.toLowerCase())) {
				targetClan = clan;
				break;
			}
		}

		if (targetClan == null) {
			return;
		}

		String name;
		L1PcInstance target = null;
		CopyOnWriteArrayList<ClanMember> clanMemberList = targetClan.getClanMemberList();
		int id = 0;
		try {
			for (int i = 0, a = clanMemberList.size(); i < a; i++) {
				name = clanMemberList.get(i).name;
				target = L1World.getInstance().getPlayer(name);
				if (!checkCountMail(sender, name, TYPE_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX))
					continue;
				if (name.equalsIgnoreCase(sender.getName()))
					continue;
				id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), name, TYPE_BLOODPLEDGE_MAIL, subject, content);

				if (target != null && target.getOnlineStatus() != 0) {
					target.sendPackets(String.valueOf(new S_LetterList(S_LetterList.WRITE_TYPE_BLOODPLEDGE_MAIL, id, S_LetterList.TYPE_RECEIVE, sender.getName(), subject))); // 收件人
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sender.sendPackets(String.valueOf(new S_LetterList(S_LetterList.WRITE_TYPE_BLOODPLEDGE_MAIL, id, S_LetterList.TYPE_SEND, sender.getName(), subject)));
		sender.sendPackets(String.valueOf(new S_LetterList(WRITE_BLOODPLEDGE_MAIL, true)));
		sender.setPostDelay(postdelaytime + 5);
	}

	private void DeleteLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().deleteLetter(id);
		if (type == DEL_PRIVATE_MAIL)
			MJUIAdapter.on_delete_letter(id);
		pc.sendPackets(new S_LetterList(pc, type, id, true));
	}

	private void ReadLetter(L1PcInstance pc, int type, int read) {
		int id = readD();
		LetterTable.getInstance().CheckLetter(id);
		if (type == READ_PRIVATE_MAIL)
			MJUIAdapter.on_check_letter(id);
		pc.sendPackets(new S_LetterList(pc, type, id, read));
	}

	private void LetterList(L1PcInstance pc, int type, int count) {
		pc.sendPackets(new S_LetterList(pc, type, count));
	}

	private void SaveLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().SaveLetter(id, letterType);
		pc.sendPackets(new S_LetterList(pc, type, id, true));
	}

	private boolean checkCountMail(L1PcInstance from, String to, int type, int max) {
		int cntMailInMailBox = LetterTable.getInstance().getLetterCount(to, type);
		if (cntMailInMailBox >= max) { // 信箱已滿
			from.sendPackets(String.valueOf(new S_SystemMessage(to + "的信箱已滿，無法發送新信件。")));
			return false;
		}
		return true;
	}

	@Override
	public String getType() {
		return C_MailBox;
	}
}

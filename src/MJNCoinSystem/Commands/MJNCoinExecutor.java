package MJNCoinSystem.Commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;

public abstract class MJNCoinExecutor implements MJCommand{
	public abstract String get_command_name();
	
	protected void do_write_letter_command(L1PcInstance pc, final int notify_id){
		final MJObjectWrapper<String> subject = new MJObjectWrapper<String>();
		final MJObjectWrapper<String> content = new MJObjectWrapper<String>();
		Selector.exec("select * from letter_command where id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, notify_id);
			}
			
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()) {
					subject.value = rs.getString("subject");
					content.value = rs.getString("content");
				}else {
					subject.value = MJString.EmptyString;
					content.value = MJString.EmptyString;
				}
			}
		});
		if (MJString.isNullOrEmpty(subject.value) && MJString.isNullOrEmpty(content.value)) {
			try {
				throw new Exception(String.format("無法找到命令信件。ID: %d\r\n堆疊追蹤", notify_id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		String current_date = MJString.get_current_datetime();
		do_write_letter(pc.getName(), current_date, subject.value, content.value);
	}

	public static void do_write_letter_togm(String generate_date, String subject, String content) {
		do_write_letter("梅蒂斯", generate_date, subject, content);
	}

	public static void do_write_letter(String receiver, String generate_date, String subject, String content) {
		int id = LetterTable.getInstance().writeLetter(949, generate_date, "梅蒂斯", receiver, 0, subject, content);
		L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
		if (pc != null) {
			pc.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, "梅蒂斯", subject));
//            pc.sendPackets(new S_LetterList(pc, 0, 20));
			pc.send_effect(1091);
			pc.sendPackets(428);
		}
	}
}



package l1j.server.server.server.command.executor;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class L1RemoveLetter implements L1CommandExecutor {
	private L1RemoveLetter() {}
	public static <L1CommandExecutor> L1CommandExecutor getInstance() {
		return (L1CommandExecutor) new L1RemoveLetter();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {				
			checkLetter(pc.getName());	
			pc.sendPackets(new S_LetterList(pc, 0, 200));
			pc.sendPackets(String.valueOf(new S_SystemMessage("信件已刪除。")));
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入 .刪除信件。")));
		}
	}

	public void checkLetter(String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();			
			pstm = con.prepareStatement("DELETE FROM letter WHERE receiver = ?");
			pstm.setString(1, name);
			pstm.execute();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}

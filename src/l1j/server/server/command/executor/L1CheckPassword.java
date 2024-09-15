package l1j.server.server.server.command.executor;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Object;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class L1CheckPassword implements l1j.server.server.command.executor.L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1CheckPassword.class.getName());

	private L1CheckPassword() {
	}

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1CheckPassword();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			String target = stringtokenizer.nextToken();
			String login = null;
			String pass = null;
			String lastactive = null;
			String ip = null;
			String host = null;


			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?");
			pstm.setString(1, target);
			rs = pstm.executeQuery();

			if (rs.next()) {
				login = rs.getString(1);
			}
			pstm2 = con.prepareStatement("SELECT password, lastactive, ip, host FROM accounts WHERE login= '"+ login + "'");
			rs2 = pstm2.executeQuery();
			
			if (rs2.next()) {
				pass = rs2.getString(1);
				lastactive = rs2.getString(2);
				ip = rs2.getString(3);
				host = rs2.getString(4);
			}
			pc.sendPackets(String.valueOf(new S_SystemMessage("角色名稱: " + target + "\n帳號: " + login + "\n密碼: " + pass + "\n最近連線: " + lastactive + "\n連線IP: " + ip + "\n創建IP: " + host)));
		} catch (Exception exception) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名] 請輸入。")));
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(rs2);
			SQLUtil.close(pstm);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);			
		}
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {

	}
}

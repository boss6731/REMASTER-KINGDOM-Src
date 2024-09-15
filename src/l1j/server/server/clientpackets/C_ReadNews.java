package l1j.server.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;

import l1j.server.L1DatabaseFactory;

import l1j.server.server.server.clientpackets.C_CommonClick;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.utils.SQLUtil;

public class C_ReadNews extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_READ_NEWS = "[C] C_ReadNews";
	
	public C_ReadNews(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		try{
			int type = readC(); // 0: 確認, 1:跳過
			System.out.println(type);
			int noticeCount = S_CommonNews.NoticeCount(client.getAccountName());
			if(noticeCount > 0){ // 存在公告
				if(type == 1){ // 跳過
//					System.out.println(noticeCount);
//					passNotice(client.getAccountName(), noticeCount);
					new C_CommonClick(client);
					S_CommonNews sn = new S_CommonNews();
					sn.UpDate(client.getAccountName(), "0");
					return;
				}
				S_CommonNews notice = new S_CommonNews(client.getAccountName(), client);
				client.sendPacket(notice);
				notice.clear();
				notice = null;
				return;
			}
			new C_CommonClick(client);
			S_CommonNews sn = new S_CommonNews();
			sn.UpDate(client.getAccountName(), "0");
//			new C_CharacterSelect(client);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			clear();
		}
	}
	
	private void passNotice(String account, int noticeId){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET notice='" + noticeId + "' WHERE login='" + account + "'");
			pstm.execute();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	@Override
	public String getType() {
		return C_READ_NEWS;
	}

}

/**
 * 通知視窗
 * 
 * Made by 舍科
 */

package l1j.server.server.serverpackets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;


public class S_CommonNews extends ServerBasePacket{

	private StringBuffer sb = new StringBuffer();
	private static final String S_COMMON_NEWS = "[S] S_CommonNews";

	public S_CommonNews(String account, GameClient ct){
		String date = getDate(account);
		ResultSet r = null;
		Connection c = null;
		PreparedStatement p = null;
		try{
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("select * from notice where id > '" + date + "'");
			r = p.executeQuery();
			String sTemp = "";
			String sDate = "";
			if(r.next()){
				sDate = r.getString("id");
				sTemp = r.getString("message");
				StringTokenizer s = new StringTokenizer(sTemp, "-");
				while(s.hasMoreElements()){
					sb.append(s.nextToken()).append("\n");
				}
				writeC(Opcodes.S_NEWS);				// opcode
				writeS(sb.toString());								// Data
				sb.setLength(0);
				UpDate(account, sDate);
			}
		}catch(Exception e){
			System.out.println("notice Error : " + e.getMessage());
		}finally{
			try{
				SQLUtil.close(r);
				SQLUtil.close(p);
				SQLUtil.close(c);
			}catch(Exception e){}
		}
	}
	
	public S_CommonNews(String s) {
		writeC(Opcodes.S_NEWS);
		writeS(s);
	}
	
	public S_CommonNews() {		
	}
	
	/**
	 * 傳回按帳戶名稱閱讀的通知數
	 * @param account 帳戶名稱
	 * @return 通知數量
	 */
	public static int NoticeCount(String account){
		int Count = 0;
		ResultSet r = null;
		Connection c = null;
		PreparedStatement p = null;
		try{
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("select count(id) as cnt from notice where id > (select notice from accounts where login=?)");
			p.setString(1, account);
			r = p.executeQuery();
			if(r.next()) Count = r.getInt("cnt");
		}catch(Exception e){
			e.printStackTrace();
			Count = 0;
		}finally{
			SQLUtil.close(r);
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return Count;
	}
	
	/**
	 * 從帳戶表中檢索帳戶的最後通知日期.
	 * @param client
	 * @return 最後一次閱讀通知的日期(yyyy-MM-dd)
	 */
	private String getDate(String account){
		String sTemp = "";	
		ResultSet r = null;
		Connection c = null;
		PreparedStatement p = null;
		try{
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("select * from accounts where login=?");
			p.setString(1, account);
			//p = c.prepareStatement("select * from accounts where login='" + account + "'");
			r = p.executeQuery();
			if(r.next()) sTemp = r.getString("notice");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				SQLUtil.close(r);
				SQLUtil.close(p);
				SQLUtil.close(c);
			}catch(Exception e){}
		}
		return sTemp != "" ? sTemp : "";
	}
	
	/**
	 * 將帳戶名稱的最終通知日期更新為目前日期
	 * @param account
	 */
	public void UpDate(String account, String date){
		Connection c = null;
		PreparedStatement p = null;
		try{
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("update accounts set notice=? where login=?");
			//p = c.prepareStatement("update accounts set notice=? where login='" + account + "'");
			p.setString(1, date);
			p.setString(2, account);
			p.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				SQLUtil.close(p);
				SQLUtil.close(c);
			}catch(Exception e){}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_COMMON_NEWS;
	}
}



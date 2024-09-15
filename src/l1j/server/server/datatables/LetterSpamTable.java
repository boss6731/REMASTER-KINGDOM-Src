package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;

import l1j.server.server.Server;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class LetterSpamTable {
	private volatile static LetterSpamTable uniqueInstance = null;

	public LetterSpamTable() {
	}

	public static LetterSpamTable  getInstance() {
		if(uniqueInstance == null) {
			synchronized (Server.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new LetterSpamTable();
				}
			}
		}
		return uniqueInstance;
	}

	/**
	 * 檢查是否可以發送信件。
	 * @param senderName
	 * @param receiverName
	 * @return
	 */
	public boolean spamLetterCheck(String senderName, String receiverName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = ? AND spamname = ?");
			pstm.setString(1, receiverName);
			pstm.setString(2, senderName);
			rs = pstm.executeQuery();
			if (rs.next()) return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return true;
	}


	/*
	  保存信件屏蔽者。
	  @param pcName
	 * @param excludeName
	 */
    // public void spamLetterAdd(L1PcInstance pc, String excludeName) {
    //     Connection con = null;
    //     PreparedStatement pstm = null;
    //     ResultSet rs = null;
    //     int no = 0;
    //     try {
    //         con = L1DatabaseFactory.getInstance().getConnection();
    //         pstm = con.prepareStatement("SELECT Max(no)+1 as cnt FROM letter_spam ORDER BY no");
    //         rs = pstm.executeQuery();
    //         if (rs.next()) {
    //             no = rs.getInt("cnt");
    //         }
    //         if (no >= 50) {
    //             pc.sendPackets(new S_ServerMessage(472)); // 屏蔽的用戶太多了。
    //             return;
    //         }
    //
    //         pstm = con.prepareStatement("INSERT INTO letter_spam SET no=?, name=?, spamname=?");
    //         pstm.setInt(1, no);
    //         pstm.setString(2, pc.getName());
    //         pstm.setString(3, excludeName);
    //         pstm.execute();
    //         //pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, excludeName, 1));
    //
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         SQLUtil.close(rs);
    //         SQLUtil.close(pstm);
    //         SQLUtil.close(con);
    //     }
    // }

	/**
	 * 刪除信件屏蔽者。
	 * @param pc
	 * @param excludeName
	 */
	public void spamLetterDel(L1PcInstance pc, String excludeName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM letter_spam WHERE name = ? AND spamname = ?");
			pstm.setString(1, pc.getName());
			pstm.setString(2, excludeName);
			pstm.execute();
			//pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, excludeName, 1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 檢查對方是否在我的屏蔽名單中。
	 * @param PcName
	 * @param spamname
	 * @return
	 */
	public boolean spamList(String PcName, String spamname) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = ? AND spamname = ?");
			pstm.setString(1, PcName);
			pstm.setString(2, spamname);
			rs = pstm.executeQuery();
			if (rs.next()) return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}


	/**
	 * 連接到世界時加載並發送S封包。
	 * @param pc
     */
	public void loadSpamList(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String spamname = null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = ?");
			pstm.setString(1, pc.getName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				spamname = rs.getString("spamname");
				//pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, spamname, 1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}

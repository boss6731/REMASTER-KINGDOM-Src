package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class LetterTable {
	private volatile static LetterTable uniqueInstance = null;

	public LetterTable() {
	}

	public static LetterTable getInstance() {
		if (uniqueInstance == null) {
			synchronized (LetterTable.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new LetterTable();
				}
			}
		}
		return uniqueInstance;
	}

	// 模板ID一覽
	// 16:角色不存在
	// 32:行李太多
	// 48:血盟不存在
	// 64:※內容不顯示(白字)
	// 80:※內容不顯示(黑字)
	// 96:※內容不顯示(黑字)
	// 112:恭喜您。您參加的拍賣以%0阿德納的最終價格成交。
	// 128:很遺憾，因為出現了出價高於您的(分)方，所以您的出價失敗了。
	// 144:您參與的拍賣成功了，但您目前無法擁有房屋。
	// 160:您擁有的房屋以%1阿德納的最終價格賣出。
	// 176:您申請的拍賣因拍賣期內未有人出示超過您所指定金額的付款，(因此)最終被刪除。
	// 192:您申請的拍賣因拍賣期內未有人出示超過您所指定金額的付款，(因此)最終被刪除。
	// 208:您的血盟擁有的房子，因為歸屬於本領主的領地，(所以)如果您將來想要使用它，這邊必須繳納稅金。
	// 224:您尚未支付您房子所徵收的%0阿德納稅款。
	// 240:最終由於您未支付您房子所徵收的%0稅款，根據警告，您的房子所有權將被剝奪。

	public int getLetterCount(String name, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int cnt = 0;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(*) as cnt FROM letter WHERE receiver=? AND template_id = ? order by date");
			pstm.setString(1, name);
			pstm.setInt(2, type);
			rs = pstm.executeQuery();
			if (rs.next()) {
				cnt = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return cnt;
	}

	public int writeLetter(int code, Timestamp dTime, String sender, String receiver, int templateId, String subject, String content) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		int itemObjectId = 0;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			pstm1 = con.prepareStatement(" SELECT Max(item_object_id)+1 as cnt FROM letter ORDER BY item_object_id ");
			rs = pstm1.executeQuery();
			if (rs.next()) {
				itemObjectId = rs.getInt("cnt");
			}

			pstm2 = con.prepareStatement("INSERT INTO letter SET item_object_id=?, code=?, sender=?, receiver=?, date=?, template_id=?, subject=?, content=?, isCheck=? ");
			pstm2.setInt(1, itemObjectId);
			pstm2.setInt(2, code);
			pstm2.setString(3, sender);
			pstm2.setString(4, receiver);
			pstm2.setTimestamp(5, dTime);
			pstm2.setInt(6, templateId);
			pstm2.setString(7, subject);
			pstm2.setString(8, content);
			pstm2.setInt(9, 0);
			pstm2.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
		return itemObjectId;
	}

	public void writeLetter(int itemObjectId, int code, String sender, String receiver, String date, int templateId, byte[] subject, byte[] content) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM letter ORDER BY item_object_id");
			rs = pstm1.executeQuery();
			pstm2 = con.prepareStatement("INSERT INTO letter SET item_object_id=?, code=?, sender=?, receiver=?, date=?, template_id=?, subject=?, content=?");
			pstm2.setInt(1, itemObjectId);
			pstm2.setInt(2, code);
			pstm2.setString(3, sender);
			pstm2.setString(4, receiver);
			pstm2.setString(5, date);
			pstm2.setInt(6, templateId);
			pstm2.setBytes(7, subject);
			pstm2.setBytes(8, content);
			pstm2.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}

	public void writeLetter(int itemObjectId, int code, String sender, String receiver, String date, int templateId, String subject, String content) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM letter ORDER BY item_object_id");
			rs = pstm1.executeQuery();
			pstm2 = con.prepareStatement("INSERT INTO letter SET item_object_id=?, code=?, sender=?, receiver=?, date=?, template_id=?, subject=?, content=?");
			pstm2.setInt(1, itemObjectId);
			pstm2.setInt(2, code);
			pstm2.setString(3, sender);
			pstm2.setString(4, receiver);
			pstm2.setString(5, date);
			pstm2.setInt(6, templateId);
			pstm2.setString(7, subject);
			pstm2.setString(8, content);
			pstm2.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}

	public void deleteLetter(int itemObjectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM letter WHERE item_object_id=?");
			pstm.setInt(1, itemObjectId);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void SaveLetter(int id, int letterType) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE letter SET template_id = ? WHERE item_object_id=?");
			pstm.setInt(1, letterType);
			pstm.setInt(2, id);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int writeLetter(int code, String dTime, String sender, String receiver, int templateId, String subject, String content) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		int itemObjectId = 0;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			pstm1 = con.prepareStatement(" SELECT Max(item_object_id)+1 as cnt FROM letter ORDER BY item_object_id ");
			rs = pstm1.executeQuery();
			if (rs.next()) {
				itemObjectId = rs.getInt("cnt");
			}

			pstm2 = con.prepareStatement("INSERT INTO letter SET item_object_id=?, code=?, sender=?, receiver=?, date=?, template_id=?, subject=?, content=?, isCheck=? ");
			pstm2.setInt(1, itemObjectId);
			pstm2.setInt(2, code);
			pstm2.setString(3, sender);
			pstm2.setString(4, receiver);
			pstm2.setString(5, dTime);
			pstm2.setInt(6, templateId);
			pstm2.setString(7, subject);
			pstm2.setString(8, content);
			pstm2.setInt(9, 0);
			pstm2.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
		return itemObjectId;
	}

	public void CheckLetter(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE letter SET isCheck = 1 WHERE item_object_id=?");
			pstm.setInt(1, id);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}

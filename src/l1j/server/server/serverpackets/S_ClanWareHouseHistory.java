package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_ClanWareHouseHistory extends ServerBasePacket {

	private static final String S_ClanWareHouseHistory = "[C] S_ClanWareHouseHistory";

	public S_ClanWareHouseHistory(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		Connection con = null;
		Statement pstm = null;
		ResultSet rs = null;
		int time = 0;
		int realtime = (int) (System.currentTimeMillis() / 1000);
		String itemName = null;
		String itemIndex = null;
		String charName = null;
		int itemCount = 0;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.createStatement();
			rs = pstm.executeQuery("SELECT * FROM clan_warehousehistory WHERE clan_id=" + pc.getClanid() + " order by elapsed_time desc");
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			writeC(Opcodes.S_EVENT);
			writeC(117);
			writeD(count); // 文字數量。
			while (rs.next()) {
				time = (realtime - rs.getInt("elapsed_time")) / 60;
				charName = rs.getString("char_name");
				itemName = rs.getString("item_name");
				itemCount = rs.getInt("item_count");
				itemIndex = rs.getString("item_getorput");
				writeS(charName); // 名字
				if (itemIndex.equalsIgnoreCase("寄存了")) {
					writeC(0); // 1: 找到了 , 0: 寄存了
				} else {
					writeC(1);
				}
				writeS(itemName); // 物品名稱
				writeD(itemCount); // 物品數量
				writeD(time); // 經過時間
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ClanWareHouseHistory;
	}

}

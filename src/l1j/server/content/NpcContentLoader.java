package l1j.server.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.content.bean.L1NpcContent;
import l1j.server.server.utils.SQLUtil;

public class NpcContentLoader {// npc-common.bin
	private static Logger _log = Logger.getLogger(NpcContentLoader.class.getName());
	private final FastTable<L1NpcContent> _data;

	private static NpcContentLoader _instance;

	public static NpcContentLoader getInstance() {
		if (_instance == null)
			_instance = new NpcContentLoader();
		return _instance;
	}

	private NpcContentLoader() {
		_data = load();
	}

	private FastTable<L1NpcContent> load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		FastTable<L1NpcContent> data = new FastTable<>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM tb_npc_bin_info");
			rs = pstm.executeQuery();
			while (rs.next()) {
				data.add(new L1NpcContent(rs));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "Npc 內容載入器[]錯誤", e);
			e.printStackTrace();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "Npc 內容載入器[]錯誤", e);
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return data;
	}

	public L1NpcContent getProtoNpcInfo(String name, String nameId, int spriteId) {
		for (L1NpcContent obj : _data) {
			if (obj.getName() == null || obj.getNameId() == null || obj.getSpriteId() == 0)
				continue;
			if (obj.getName().equals(name) && obj.getNameId().equals(nameId) && obj.getSpriteId() == spriteId) {
				return obj;
			}
		}
		return null;
	}

	public int getProtoNpcClassId(String name, String nameId, int spriteId) {
		if (name == null || nameId == null || spriteId == 0)
			return 0;
		L1NpcContent obj = getProtoNpcInfo(name, nameId, spriteId);
		if (obj != null)
			return obj.getClassId();
		return 0;
	}

	public static void reload() {
		NpcContentLoader oldInstance = _instance;
		_instance = new NpcContentLoader();
		oldInstance._data.clear();
		oldInstance = null;
	}
}

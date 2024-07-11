package l1j.server.AinhasadSpecialStat2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import com.mchange.v1.db.sql.SqlUtils;

//import com.sun.istack.internal.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class L1AinhasadFaithUserLoader {
//	private static Logger _log = Logger.getLogger(L1AinhasadFaithUserLoader.class.getName());
	private static L1AinhasadFaithUserLoader _instance;

	private static final ConcurrentHashMap<Integer, ArrayList<L1AinhasadFaithUserObject>> DATA = new ConcurrentHashMap<>();
	
	public static ArrayList<L1AinhasadFaithUserObject> getAinhasadFaithUserList(int charObjId){
		return DATA.get(charObjId);
	}
	
	public static L1AinhasadFaithUserLoader getInstance() {
		if(_instance == null) {
			_instance = new L1AinhasadFaithUserLoader();
		}
		return _instance;
	}
	
	
	private L1AinhasadFaithUserLoader() {
		load();
	}
	
	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM character_special_stat2");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int charObjId			= rs.getInt("obj_id");
				int group				= rs.getInt("group_id");
				int index				= rs.getInt("index_id");
				Timestamp endTime 		= rs.getTimestamp("endTime");
				int type				= rs.getInt("type");
				ArrayList<L1AinhasadFaithUserObject> list = DATA.get(charObjId);
				if (list == null) {
					list = new ArrayList<>();
					DATA.put(charObjId, list);
				}
				list.add(new L1AinhasadFaithUserObject(charObjId, group, index, type, endTime));
			}
		} catch (SQLException e) {
			System.out.println(e);
//			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			System.out.println(e);
//			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void addSpecialStat2(int charId, String charname, int index) {
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			AinhasadSpecialStat2Info Info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
			long currentTime = System.currentTimeMillis();
			long endTime = currentTime + Info.get_hours() * 60 * 60 * 1000;
			
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "INSERT INTO character_special_stat2 SET obj_id=?, char_name=?, group_id=?, index_id=?, endTime=?, type=?";
			pstm = con.prepareStatement(sqlstr);
			
			pstm.setInt(1, charId);
			pstm.setString(2, charname);
			pstm.setInt(3, Info.get_group());
			pstm.setInt(4, Info.get_index());
			pstm.setTimestamp(5, new Timestamp(endTime));
			pstm.setInt(6, Info.get_type());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
	}
	public void delete(L1PcInstance pc, int index) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "delete from character_special_stat2 where obj_id=? and index_id=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, pc.getId());
			pstm.setInt(2, index);
			pstm.execute();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
			SQLUtil.close(pstm);
		}
	}
	
	public boolean delete(L1PcInstance pc, L1AinhasadFaithUserObject user) {
		int result = Updator.exec("DELETE FROM character_special_stat2 WHERE group=? AND index=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, user.getGroup());
				pstm.setInt(2, user.getIndex());
			}
		});
		if (result > 0) {
			ArrayList<L1AinhasadFaithUserObject> list = DATA.get(pc.getId());
			if (list == null) {
				return false;
			}
			list.remove(user);
			return true;
		}
		return false;
	}
	
	public void remove(int charId) {
		DATA.remove(charId);
	}
	
}

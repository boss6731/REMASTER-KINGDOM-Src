package l1j.server.MWautoBankerProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class MWautoBankerDataTable{
	
	private ConcurrentHashMap<Integer, MWautoBankerInfo> users;

	public MWautoBankerDataTable(){
		users = new ConcurrentHashMap<>();
	}

	/**정상 입금자 지급을 위함by명월이*/
	public synchronized void LoadDepositorInfo(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from mw_auto_banker where deposit_stat='true' and paid_ncoin='false'");
			rs		= pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				SetDepositorinfo(rs.getInt(1), rs.getString(4), rs.getInt(5), rs.getTimestamp(7), rs.getBoolean(8), rs.getBoolean(9));
				count++;
				if(count > users.size()){
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public synchronized void PaidDepositorNcoin(L1PcInstance pc) {
		if(pc == null || pc.getAI() != null) {
			return;
		}
		Connection 			con		= null;
		Connection          con2    = null;
		PreparedStatement 	pstm 	= null;
		PreparedStatement   pstm2   = null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("update mw_auto_banker set paid_ncoin='true' where char_objid=? and paid_ncoin='false'");
			pstm.setInt(1, pc.getId());
			pstm.executeUpdate();
			con2		= L1DatabaseFactory.getInstance().getConnection();
			pstm2 	= con2.prepareStatement("update ncoin_trade_deposit set is_deposit='1' where character_object_id=? and is_deposit='0'");
			pstm2.setInt(1, pc.getId());
			pstm2.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
			SQLUtil.close(pstm2, con2);
		}
	}

	public void StoreDepositorInfo(L1PcInstance pc, String keyworld, String name, int inmountcount){
		if(pc == null || pc.getAI() != null) {
			return;
		}
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("insert into mw_auto_banker set char_objid=?, char_name=?, apply_keyworld=?, depositor_name=?, deposit_inmount=?, applied_date=?");
			pstm.setInt(1, pc.getId());
			pstm.setString(2, pc.getName());
			pstm.setString(3, keyworld);
			pstm.setString(4, name);
			pstm.setInt(5, inmountcount);
			pstm.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}

	public boolean Depositorkeyworld(L1PcInstance pc, String keyworld) {
		if(pc == null || pc.getAI() != null) {
			return false;
		}
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 		    rs	    = null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from mw_auto_banker where apply_keyworld=? and paid_ncoin='false'");
			pstm.setString(1, keyworld);
			rs		= pstm.executeQuery();
			if(rs.next())
				return false;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return true;
	}
	/**정상 입금자 지급을 위함by명월이*/
	
	
	/**앱센터 신청시 키워드와 입금자 키워드가 맞지 않을경우 지급을 위함by명월이*/
	public boolean UnknownDepositor(L1PcInstance pc, String depositor_name, int deposit_inmount, Timestamp deposit_date) {
		if(pc == null || pc.getAI() != null) {
			return false;
		}
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 		    rs	    = null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from mw_auto_banker_unknown where depositor_name=? and deposit_inmount=? and deposit_date=? and paid_ncoin='false'");
			pstm.setString(1, depositor_name);
			pstm.setInt(2, deposit_inmount);
			pstm.setTimestamp(3, deposit_date);
			rs		= pstm.executeQuery();
			if(rs.next())
				return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	public void PaidUnknownNcoin(L1PcInstance pc, String name) {
		if(pc == null || pc.getAI() != null) {
			return;
		}
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("update mw_auto_banker_unknown set paid_ncoin='true' where depositor_name=? and paid_ncoin='false'");
			pstm.setString(1, name);
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	/**앱센터 신청시 키워드와 입금자 키워드가 맞지 않을경우 지급을 위함by명월이*/
	
	
	/**로그 저장을 위함*/
	public void DepositorInfoLog(L1PcInstance pc, String depositorname, int inmountcount, Timestamp date){
		if(pc == null || pc.getAI() != null) {
			return;
		}
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("insert into mw_auto_banker_log set char_name=?, depositor_name=?, deposit_inmount=?, deposit_date=?, Paid_date=?");
			pstm.setString(1, pc.getName());
			pstm.setString(2, depositorname);
			pstm.setInt(3, inmountcount);
			pstm.setTimestamp(4, date);
			pstm.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	
	/**입금자 정보를 넣기위한 맵 관련by명월이*/
	public void SetDepositorinfo(int pcid, String Depositor, int inmountcount, Timestamp Depositdate, boolean DepositStat, boolean PaidNcoinStat) {
		MWautoBankerInfo BankerInfo = MWautoBankerInfo.newInstance(pcid);
		BankerInfo.Depositor = Depositor;
		BankerInfo.inmountcount = inmountcount;
		BankerInfo.Depositdate = Depositdate;
		BankerInfo.DepositStat = DepositStat;
		BankerInfo.PaidNcoinStat = PaidNcoinStat;
		users.put(pcid, BankerInfo);
	}
	
	public Collection<MWautoBankerInfo> get_members(){
		return Collections.unmodifiableCollection(users.values());		
	}
	
	public boolean isDepositorinfo() {
		if(users.size() > 0)
			return true;
		else 
			return false;
	}
	
	public void Depositorinfoclear() {
		if(users.size() > 0) {
			users.clear();
		}
	}
	/**입금자 정보를 넣기위한 맵 관련by명월이*/
}

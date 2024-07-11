package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotMent;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class MJBotMentLoader {
	private static MJBotMentLoader _instance;
	public static MJBotMentLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotMentLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJBotMentLoader tmp = _instance;
		_instance = new MJBotMentLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private int[]									_idx;
	private HashMap<Integer, ArrayList<MJBotMent>> 	_ments;
	private MJBotMentLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		MJBotMent			ment	= null;
		_ments						= new HashMap<Integer, ArrayList<MJBotMent>>(6);
		_idx						= new int[6];
		for(int i=0; i<6; i++)
			_ments.put(i, new ArrayList<MJBotMent>(16));
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_ment");
			rs		= pstm.executeQuery();
			while(rs.next()){
				
				ment = new MJBotMent();
				byte[] b 		= rs.getBytes("message");
				ment.message 	= new String(b, "UTF-8");
		
				ment.status 	= MJCommons.parseToFirstInt(rs.getString("status"));
				ment.type		= MJCommons.parseToFirstInt(rs.getString("type"));
				ment.addition	= MJCommons.parseToFirstInt(rs.getString("addition"));
				ment.parameter	= MJCommons.parseToFirstInt(rs.getString("parameter"));
				_ments.get(ment.status).add(ment);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void clear(){
		if(_ments != null){
			for(ArrayList<MJBotMent> list : _ments.values())
				list.clear();
			_ments.clear();
			_ments = null;
		}
	}
	
	public MJBotMent get(int i){
		ArrayList<MJBotMent> list = _ments.get(i);
		if(list == null || list.size() <= 0)
			return null;
		
		int idx = (_idx[i]) % list.size();
		_idx[i] = idx + 1;
		return list.get(idx);

	}
}

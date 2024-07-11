package l1j.server.MJRaidSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJRaidSystem.MJRaidType;
import l1j.server.server.utils.SQLUtil;

/** 載入和管理 raid 模板的類別（MJRaidType 類別），它是 raid 實例的基礎. **/
public class MJRaidTypesLoader {
	private static MJRaidTypesLoader _instance;
	public static MJRaidTypesLoader getInstance(){
		if(_instance == null)
			_instance = new MJRaidTypesLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJRaidTypesLoader tmp = null;
		tmp 		= _instance;
		_instance 	= new MJRaidTypesLoader();	
		tmp.clear();
		tmp = null;
	}
	
	private HashMap<Integer, MJRaidType> _raidTypes;
	private MJRaidTypesLoader(){
		load();
	}
	
	private void load(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		MJRaidType			type	= null;
		StringBuilder 		sbQry	= new StringBuilder(256);
		_raidTypes 					= new HashMap<Integer, MJRaidType>(4);
		
		try{
			sbQry.append("select * from ").append(MJRaidType.TB_NAME);
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement(sbQry.toString());
			rs		= pstm.executeQuery();
			while(rs.next()){
				type = new MJRaidType();
				if(type.setInformation(rs))
					_raidTypes.put(type.getId(), type);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void clear(){
		if(_raidTypes != null){
			_raidTypes.clear();
			_raidTypes = null;
		}
	}
	
	public MJRaidType get(int i){
		return _raidTypes.get(i);
	}
}

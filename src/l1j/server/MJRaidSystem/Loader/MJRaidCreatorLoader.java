package l1j.server.MJRaidSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJRaidSystem.Creator.MJRaidCreator;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

/** 啟動 raid、載入和管理 MJRaidCreator 物件的類別 **/
public class MJRaidCreatorLoader {
	private static MJRaidCreatorLoader _instance;
	public static MJRaidCreatorLoader getInstance(){
		if(_instance == null)
			_instance = new MJRaidCreatorLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJRaidCreatorLoader tmp = _instance;
		_instance 				= new MJRaidCreatorLoader();
		tmp.clear();
		tmp = null;
	}
	
	private HashMap<Integer, MJRaidCreator> _creatorMaps;
	private MJRaidCreatorLoader(){
		load();
	}
	
	private void load(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		MJRaidCreator		creator	= null;
		StringBuilder 		sbQry	= new StringBuilder(256);
		_creatorMaps 				= new HashMap<Integer, MJRaidCreator>(4);

		try{
			sbQry.append("select * from ").append(MJRaidCreator.TB_NAME);
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement(sbQry.toString());
			rs		= pstm.executeQuery();
			while(rs.next()){
				creator = new MJRaidCreator();
				if(creator.setInformation(rs))
					_creatorMaps.put(creator.getNeedItemId(), creator);
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
		if(_creatorMaps != null){
			_creatorMaps.clear();
			_creatorMaps = null;
		}
	}
	
	public boolean createRaid(L1PcInstance pc, L1ItemInstance item){
		MJRaidCreator creator = _creatorMaps.get(item.getItemId());
		if(creator != null){
			int castle_id1 = L1CastleLocation.getCastleIdByArea(pc);
			if (castle_id1 != 0) {
				pc.sendPackets("攻城區域無法召喚。");
				return true;
			}
			if (pc.getZoneType() != 1) {// 安全區
				pc.sendPackets("僅可在安全區使用。");
				return true;
			}	
			creator.create(pc, item);
			return true;
		}
		return false;
	}
}

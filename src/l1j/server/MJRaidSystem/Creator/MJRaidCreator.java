package l1j.server.MJRaidSystem.Creator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import l1j.server.MJRaidSystem.MJRaidMessage;
import l1j.server.MJRaidSystem.MJRaidType;
import l1j.server.MJRaidSystem.Loader.MJRaidTypesLoader;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

/** 定義 RaidCreator 以實現未來的可擴展性和消除複雜性. **/
/** 獲得作為開啟raid的媒介的物品，並開啟與raid對應的raid系統.. **/
public class MJRaidCreator {
	public static final String TB_NAME 	= "tb_mjraid_creator_items";
	protected static Logger	_log		= Logger.getLogger(MJRaidCreator.class.getName());
	
	private int _needItemId;	// 打開A raid的物品ID
	private int _needCount;		// 發動突襲所需的物品數量.
	private int _raidId;		// 待開的raid ID.
	public MJRaidCreator(){
		_needItemId = -1;
		_needCount 	= -1;
		_raidId		= -1;
	}
	public int getNeedItemId(){
		return _needItemId;
	}
	public int getNeedCount(){
		return _needCount;
	}
	public int getRaidId(){
		return _raidId;
	}
	
	public boolean setInformation(ResultSet rs){
		String column = null;
		try{
			column		= "needItemId";
			_needItemId = rs.getInt(column);
			
			column 		= "needCount";
			_needCount	= rs.getInt(column);
			
			column		= "raidId";
			_raidId		= rs.getInt(column);
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("MJ 突襲創造者 在 設定訊息() 中...無效的列！ ").append(column);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void create(L1PcInstance pc, L1ItemInstance item){
		if(item.getCount() < _needCount){
			MJRaidMessage.RAID_OPEN_FAIL_INVALID_ITEM.sendMessage(pc);
			return;
		}
		
		MJRaidType type = MJRaidTypesLoader.getInstance().get(_raidId);
		if(type == null){
			MJRaidMessage.RAID_OPEN_FAIL_INVALID_TYPE.sendMessage(pc);
			return;
		}
		
		if(!pc.getInventory().checkItem(_needItemId, _needCount)){
			MJRaidMessage.RAID_OPEN_FAIL_INVALID_ITEM.sendMessage(pc);
			return;
		}
		pc.getInventory().removeItem(item, _needCount);
		type.create(pc);
		ArrayList<L1PcInstance> pcs = new ArrayList<L1PcInstance>();
		pcs.addAll(L1World.getInstance().getAllPlayers());
		MJRaidMessage.RAID_OPEN_SUCCESS_MESSAGE.sendMessage(pcs);
		pcs.clear();
	}
	
	
}

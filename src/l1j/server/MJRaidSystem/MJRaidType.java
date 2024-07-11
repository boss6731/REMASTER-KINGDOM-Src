package l1j.server.MJRaidSystem;

import java.sql.ResultSet;

import l1j.server.IndunEx.PlayInfo.CrocodileInDungeon;
import l1j.server.IndunSystem.Luun_Secret.Luun_Secret;
import l1j.server.IndunSystem.Luun_Secret.Luun_Secret_System;
import l1j.server.MJRaidSystem.Objects.MJRaidAntaras;
import l1j.server.MJRaidSystem.Objects.MJRaidDarkhalpas;
import l1j.server.MJRaidSystem.Objects.MJRaidFafurion;
import l1j.server.MJRaidSystem.Objects.MJRaidRindvior;
import l1j.server.MJRaidSystem.Objects.MJRaidValakas;
import l1j.server.server.model.Instance.L1PcInstance;

/** 定義突襲類型模板的類 **/
public class MJRaidType {
	public static final String TB_NAME = "tb_mjraid_types";

	/** 表示突襲ID **/
	public static final int RID_ANTARAS = 1;    // 安塔拉斯
	public static final int RID_FAFURION = 2;   // 巴普利恩
	public static final int RID_RINDVIOR = 3;   // 林德維爾
	public static final int RID_VALAKAS = 4;    // 巴拉卡斯
	public static final int RID_DARKHALPAS = 5; // 暗黑龍的哈爾帕斯
	public static final int LUUN_SECRET = 6;    // 魯運城的尋寶

	private int _id;         // 突襲ID
	private String _name;    // 突襲名稱
	private int _portalId;   // 進入突襲所需的中介NPC的ID
	private int _maxUser;    // 可以進入突襲的最大人數
	private int _inputMid;   // 進入時移動到的地圖ID
	private int _inputX;     // 進入時移動到的X座標
	private int _inputY;     // 進入時移動到的Y座標
	private int _outMid;     // 出突襲時移動到的地圖ID
	private int _outX;       // 出突襲時移動到的X座標
	private int _outY;       // 出突襲時移動到的Y座標
	private int _movX;       // 從等待室移動到突襲場所時移動到的X座標
	private int _movY;       // 從等待室移動到突襲場所時移動到的Y座標
	private long _raidTime;  // 突襲時間
}
	
	public boolean setInformation(ResultSet rs){
		String column = null;
		try{
			column		= "id";
			_id 		= rs.getInt(column);
			
			column 		= "name";
			_name		= rs.getString(column);
			
			column		= "portal_id";
			_portalId	= rs.getInt(column);
			
			column		= "max_user";
			_maxUser	= rs.getInt(column);
			
			column		= "input_mapId";
			_inputMid	= rs.getInt(column);
			
			column		= "input_x";
			_inputX		= rs.getInt(column);
			
			column		= "input_y";
			_inputY		= rs.getInt(column);
			
			column		= "out_mapId";
			_outMid		= rs.getInt(column);
			
			column		= "out_x";
			_outX		= rs.getInt(column);
			
			column		= "out_y";
			_outY		= rs.getInt(column);
			
			column		= "mov_x";
			_movX		= rs.getInt(column);
			
			column		= "mov_y";
			_movY		= rs.getInt(column);
			
			column		= "raid_time";
			_raidTime	= rs.getLong(column);
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("MJRaidType 在 setInformation()...無效的列！").append(column);
			System.out.println("MJRaidType:" +sb.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int getId(){
		return _id;
	}
	public void setId(int i){
		_id = i;
	}
	
	public String getName(){
		return _name;
	}
	public void setName(String s){
		_name = s;
	}
	
	public int getPortalId(){
		return _portalId;
	}
	
	public int getMaxUser(){
		return _maxUser;
	}
	
	public long getRaidTime(){
		return _raidTime;
	}
	
	public int getInputMapId(){
		return _inputMid;
	}
	
	public int getInputX(){
		return _inputX;
	}
	
	public int getInputY(){
		return _inputY;
	}
	
	public int getOutMapId(){
		return _outMid;
	}
	
	public int getOutX(){
		return _outX;
	}
	
	public int getOutY(){
		return _outY;
	}
	
	public void inputRaid(L1PcInstance pc, int mid){
		pc.start_teleport(_inputX, _inputY, mid, 5, 18339, true, false);
	}
	
	public void outRaid(L1PcInstance pc){
		pc.start_teleport(_outX, _outY, _outMid, 5, 18339, true, false);
	}
	
	public void move(L1PcInstance pc, int mid){
		pc.start_teleport(_movX, _movY, mid, 5, 18339, true, false);
	}
	
	public void create(L1PcInstance pc){
		MJRaidObject obj = null;
		switch(_id){
		case RID_ANTARAS:
			obj = new MJRaidAntaras(pc, this);
			break;
		case RID_FAFURION:
			obj = new MJRaidFafurion(pc, this);
			break;
		case RID_RINDVIOR:
			obj = new MJRaidRindvior(pc, this);
			break;
		case RID_VALAKAS:
			obj = new MJRaidValakas(pc, this);
			break;
		case RID_DARKHALPAS:
			obj = new MJRaidDarkhalpas(pc, this);
			break;
/*		case Luun_Secret:

			Luun_Secret_System.getInstance().Luun_SecretStart(pc);
			break;*/
		default:
			// error message.
			break;
		}
		if(obj != null)
			MJRaidSpace.getInstance().startInstance(obj);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(128);
		sb.append("raid id : ").append(_id).append("\n");
		sb.append("raid name : ").append(_name).append("\n");
		sb.append("portal id : ").append(_portalId).append("\n");
		sb.append("max user : ").append(_maxUser).append("\n");
		sb.append("input map id : ").append(_inputMid).append("\n");
		sb.append("input X : ").append(_inputX).append("\n");
		sb.append("input Y : ").append(_inputY).append("\n");
		sb.append("out map id : ").append(_outMid).append("\n");
		sb.append("out X : ").append(_outX).append("\n");
		sb.append("out y : ").append(_outY).append("\n");
		sb.append("raid time : ").append(_raidTime).append("\n");
		return sb.toString();
	}
}

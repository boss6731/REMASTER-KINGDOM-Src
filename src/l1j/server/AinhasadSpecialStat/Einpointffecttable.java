package l1j.server.AinhasadSpecialStat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class Einpointffecttable {
	private static Einpointffecttable _instance;

	public static Einpointffecttable getInstance(){
		if(_instance == null){
			_instance = new Einpointffecttable();
		}
		return _instance;
	}


	public static void reload() {
		Einpointffecttable oldInstance = _instance;
		_instance = new Einpointffecttable();
		oldInstance.EinpointffecttModel.clear();
	}

	private HashMap<Integer, EinpointffecttModel> EinpointffecttModel;

	private Einpointffecttable(){
		loadInfo();
	}

	private void loadInfo(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 				rs			= null;
		EinpointffecttModel			data		= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from einpoint_effect");
			rs		= pstm.executeQuery();
			EinpointffecttModel	= new HashMap<Integer, EinpointffecttModel>(SQLUtil.calcRows(rs));
//			System.out.println("몇줄?:"+SQLUtil.calcRows(rs));
			while(rs.next()){
				data = new EinpointffecttModel();
				int Level = rs.getInt("level");
				data.setBless(MJCommons.parseToIntArray(rs.getString("bless"), ","));
				data.setLucky(MJCommons.parseToIntArray(rs.getString("lucky"), ","));
				data.setVital(MJCommons.parseToIntArray(rs.getString("vital"), ","));
				data.setInvoke(MJCommons.parseToIntArray(rs.getString("invoke"), ","));
				data.setRestore(MJCommons.parseToIntArray(rs.getString("restore"), ","));
				data.setPotion(MJCommons.parseToIntArray(rs.getString("potion"), ","));
				EinpointffecttModel.put(Level, data);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public EinpointffecttModel getmodel(int level) {
		return EinpointffecttModel.get(level);
	}
	
	public int getstate(L1PcInstance pc, int index) {
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
		switch(index) {
		case 0:
			return Info.get_bless();
		case 1:
			return Info.get_lucky();
		case 2:
			return Info.get_vital();
		case 3:
			return Info.get_invoke();
		case 4:
			return Info.get_restore();
		case 5:
			return Info.get_potion();
		}
		return index;
	}
	
	public void update(L1PcInstance pc, int index) {
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
		switch(index) {
		case 0:
			Info.set_bless(Info.get_bless() + 1);
			break;
		case 1:
			Info.set_lucky(Info.get_lucky() + 1);
			break;
		case 2:
			Info.set_vital(Info.get_vital() + 1);
			break;
		case 3:
			Info.set_invoke(Info.get_invoke() + 1);
			break;
		case 4:
			Info.set_restore(Info.get_restore() + 1);
			break;
		case 5:
			Info.set_potion(Info.get_potion() + 1);
			break;
		}
	}
	
	public boolean isWeapon(int level) {
		if(EinpointffecttModel.get(level).getInvoke().get(1) != null) {
			return true;
		}
		return false;
	}
	
	public boolean isArmor(int level) {
		if(EinpointffecttModel.get(level).getInvoke().get(0) != null) {
			return true;
		}
		return false;
	}
	
	public int ishp(int level) {
/*		System.out.println(level);
		for (int i = 1 ; i<= EinpointffecttModel.size(); i++) {
			System.out.println(EinpointffecttModel.get(i).getRestore().get(0));	
		}*/
		if(EinpointffecttModel.get(level).getRestore().get(0) != null) {
			return EinpointffecttModel.get(level).getRestore().get(0);
		}
		return 0;
	}
	
	public int ismp(int level) {
		if(EinpointffecttModel.get(level).getRestore().get(1) != null) {
			return EinpointffecttModel.get(level).getRestore().get(1);
		}
		return 0;
	}
	
	public int potioncriper(int level) {
		if(EinpointffecttModel.get(level).getRestore().get(0) != null) {
			return EinpointffecttModel.get(level).getRestore().get(0);
		}
		return 0;
	}
	
	public int potiondelayper(int level) {
		if(EinpointffecttModel.get(level).getRestore().get(1) != null) {
			return EinpointffecttModel.get(level).getRestore().get(1);
		}
		return 0;
	}
	
	public int getvalue(int index, int level, int value) {
		switch(index) {
		case 0:
			return EinpointffecttModel.get(level).getBless().get(value);
		case 1:
			return EinpointffecttModel.get(level).getLucky().get(value);
		case 2:
			return EinpointffecttModel.get(level).getVital().get(value);
		case 3:
			return EinpointffecttModel.get(level).getInvoke().get(value);
		case 4:
			return EinpointffecttModel.get(level).getRestore().get(value);
		case 5:
			return EinpointffecttModel.get(level).getPotion().get(value);
		}
		return EinpointffecttModel.get(level).getBless().get(0);
	}
}

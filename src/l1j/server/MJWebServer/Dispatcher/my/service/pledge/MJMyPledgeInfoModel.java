package l1j.server.MJWebServer.Dispatcher.my.service.pledge;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJString;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.templates.L1House;

public class MJMyPledgeInfoModel {
	static MJMyPledgeInfoModel newInstance(ResultSet rs) throws SQLException{
		MJMyPledgeInfoModel model = new MJMyPledgeInfoModel();
		model.name = rs.getString("clan_name");
		model.leader = rs.getString("leader_name");
		model.currentMember = rs.getInt("current_m");
		model.totalMember = rs.getInt("total_m");
		model.point = rs.getInt("War_point");
		
		int houseId = rs.getInt("hashouse");
		if(houseId > 0){
			L1House house = HouseTable.getInstance().getHouseTable(houseId);
			if(house != null){
				model.azit = AzitInfoModel.newInstance(house);
			}
		}
		return model;
	}
	
	String name;
	String leader;
	int currentMember;
	int totalMember;
	int point;
	String already;
	AzitInfoModel azit;
	MJMyPledgeInfoModel(){
		name = MJString.EmptyString;
		leader = MJString.EmptyString;
		currentMember = 0;
		totalMember = 0;
		point = 0;
		already = MJString.EmptyString;
		azit = null;
	}
	
	public static class AzitInfoModel{
		static AzitInfoModel newInstance(L1House house){
			AzitInfoModel model = new AzitInfoModel();
			model.name = house.getHouseName();
			model.locationName = house.getLocation();
			if(!MJString.isNullOrEmpty(model.locationName)){
				int idx = model.locationName.indexOf(" ");
				if(idx != -1){
					model.locationName = model.locationName.substring(0, idx);
				}
			}
			model.squareMeter = house.getHouseArea();
			return model;
		}
		
		String name;
		String locationName;
		int squareMeter;
		AzitInfoModel(){
			name = MJString.EmptyString;
			locationName = MJString.EmptyString;
		}
	}
}

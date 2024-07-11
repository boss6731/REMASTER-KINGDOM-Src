package l1j.server.MJWebServer.Dispatcher.Template.Blood.POJO;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJFormatter;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.templates.L1House;

public class MJPledgesInfo {
	public String result;
	public ArrayList<PledgeExtension> pledgeExtensionList;
	public MJPaginationNavi paginationNavi;
	public MJPledgesInfo(){
		result = "success";
		pledgeExtensionList = new ArrayList<PledgeExtension>();
		paginationNavi = new MJPaginationNavi();
	}
	
	public static class PledgeExtension{
		public int memberCount;
		public int setting;
		public String alliance;
		public int siegePoint;
		public String foundationDay;
		public int castleId;
		public String castleName;
		public int recentLoginMemberCount;
		public int enableJoin;
		public int joinType;
		public boolean joinStatus;
		public int agitId;
		public String agitName;
		public String master;
		public int masterId;
		public String agitLocation;
		public String agitDescription;
		public int agitSize;
		public String name;
		public int id;
		public PledgeExtension(){}
		public PledgeExtension(L1Clan clan){
			memberCount = clan.getClanMemberList().size();
			setting = clan.getJoinSetting();
			alliance = null;
			siegePoint = clan.getWarPoint();
			foundationDay = MJFormatter.get_tdouble_formatter_time(clan.getClanBirthDay());
			castleId = clan.getCastleId();
			if(castleId > 0){
				MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
				if(war != null)
					castleName = war.getCastleName().replace("¼º", "");
			}else{
				agitId = clan.getHouseId();
				if(agitId > 0){
					L1House house = HouseTable.getInstance().getHouseTable(agitId);
					if(house != null){
						agitName = house.getHouseName();
						agitLocation = house.getLocation();
						agitDescription = house.getHouseName();
						agitSize = house.getHouseArea();
					}
				}
			}
			recentLoginMemberCount = clan.getCurrentOnlineMemebers();
			enableJoin = 1;
			joinType = clan.getJoinType();
			joinStatus = false;
			master = clan.getLeaderName();
			masterId = clan.getLeaderId();
			name = clan.getClanName();
			id = clan.getClanId();
		}
	}
}

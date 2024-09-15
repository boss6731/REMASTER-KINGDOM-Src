package l1j.server.MJWebServer.Dispatcher.Template.Siege.POJO;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJFormatter;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.model.L1Clan;

public class MJSiegeCastleInfo {
	public String result;
	public ArrayList<CastleInfo> siegeCastleList;
	public MJSiegeCastleInfo(){
		result = "success";
		siegeCastleList = new ArrayList<CastleInfo>();
	}
	
	public static class CastleInfo{
		public int castleId;
		public String pledge;
		public String pledgeMaster;
		public int tax;
		public int reward;
		public int siegePoint;
		public String occupyDate;
		public GameCastleInfo gameCastle;
		public boolean npcOccupied;
		public CastleInfo(){
			gameCastle = new GameCastleInfo();
		}
		public CastleInfo(int castle_id, String original_name_id, int order){
			gameCastle = new GameCastleInfo();
			castleId = castle_id;
			gameCastle.id = castle_id;
			MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
			
			gameCastle.engName = MJString.EmptyString;
			gameCastle.korName = war.getCastleName();
			gameCastle.activated = true;
			gameCastle.powerbookUrl = MJString.EmptyString;
			gameCastle.order = order;
			gameCastle.npcPledgeOriginalName = original_name_id;
			gameCastle.npcPledgeName = "紅色騎士團";
			gameCastle.npcPledgeMaster = "德波魯茲";
			
			L1Clan clan = war.getDefenseClan();
			if(clan != null){
				pledge = clan.getClanName();
				pledgeMaster = clan.getLeaderName() == null ? "德波羅茲" : clan.getLeaderName();
				siegePoint = clan.getWarPoint();
				tax = war.getPublicMoney();
				reward = war.getPublicMoney();
				occupyDate = MJFormatter.get_tdouble_formatter_time(clan.getCastleDate());
				npcOccupied = false;
			}else{
				pledge = "紅色騎士團";
				pledgeMaster = "德波羅茲";
				siegePoint = 0;
				tax = war.getPublicMoney();
				reward = war.getPublicMoney();
				//occupyDate = MJFormatter.get_tdouble_formatter_time(war.get_next_cal());
				npcOccupied = true;
			}
			
		}
	}
	
	public static class GameCastleInfo{
		public int id;
		public String engName;
		public String korName;
		public boolean activated;
		public String powerbookUrl;
		public int order;
		public String npcPledgeOriginalName;
		public String npcPledgeName;
		public String npcPledgeMaster;
	}
}

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

			gameCastle.engName = MJString.EmptyString; // 英文名稱設置為空字符串
			gameCastle.korName = war.getCastleName(); // 韓文名稱設置為城堡名稱
			gameCastle.activated = true; // 設置城堡為激活狀態
			gameCastle.powerbookUrl = MJString.EmptyString; // PowerBook URL 設置為空字符串
			gameCastle.order = order; // 設置城堡順序
			gameCastle.npcPledgeOriginalName = original_name_id; // 設置 NPC 的原始名稱
			gameCastle.npcPledgeName = "紅色騎士團"; // "붉은 기사단" => "紅色騎士團"
			gameCastle.npcPledgeMaster = "迪波羅"; // "데포로쥬" => "迪波羅"

			L1Clan clan = war.getDefenseClan(); // 獲取防守的家族（公會）
			if(clan != null) {
				pledge = clan.getClanName(); // 獲取家族名稱
				pledgeMaster = clan.getLeaderName() == null ? "迪波羅" : clan.getLeaderName(); // 獲取家族領袖名稱，默認為 "迪波羅"
				siegePoint = clan.getWarPoint(); // 獲取家族戰爭積分
				tax = war.getPublicMoney(); // 獲取稅收金額
				reward = war.getPublicMoney(); // 獲取獎勵金額
				occupyDate = MJFormatter.get_tdouble_formatter_time(clan.getCastleDate()); // 格式化佔領日期
				npcOccupied = false; // 設置 NPC 佔領狀態為 false
			} else {
				pledge = "紅色騎士團"; // 默認家族名稱
				pledgeMaster = "迪波羅"; // 默認家族領袖名稱
				siegePoint = 0; // 設置戰爭積分為 0
				tax = war.getPublicMoney(); // 獲取稅收金額
				reward = war.getPublicMoney(); // 獲取獎勵金額
				// occupyDate = MJFormatter.get_tdouble_formatter_time(war.get_next_cal()); // 註釋掉的代碼，用於計算下一次佔領日期
				npcOccupied = true; // 設置 NPC 佔領狀態為 true
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

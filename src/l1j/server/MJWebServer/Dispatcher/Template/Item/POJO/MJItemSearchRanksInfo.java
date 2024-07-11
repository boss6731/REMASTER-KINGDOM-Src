package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.server.model.gametime.RealTimeClock;

public class MJItemSearchRanksInfo {
	public String searchDate;
	public RanksInfo buyRank;
	public MJNameCodeInfo[] category;
	public RanksInfo sellRank;
	public MJItemSearchRanksInfo(){
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		searchDate = String.format("%d. %d. %d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
		category = MJNameCodeInfo.CATEGORY.ITEM_CATEGORIES;
		buyRank = new RanksInfo();
		sellRank = new RanksInfo();
	}
	public void append_rank_item(int itemId, MJNameCodeInfo trade_type, MJNameCodeInfo category){
		if(trade_type.code == MJNameCodeInfo.TRADE_TYPE.SELL.code)
			sellRank.append_rank_item(itemId, trade_type, category);
		else
			buyRank.append_rank_item(itemId, trade_type, category);
	}
	
	public static class RanksInfo{
		public ArrayList<MJItemSearchRankInfo> ALL;
		public ArrayList<MJItemSearchRankInfo> WEAPON;
		public ArrayList<MJItemSearchRankInfo> ACCESSORY;
		public ArrayList<MJItemSearchRankInfo> ARMOR;
		public ArrayList<MJItemSearchRankInfo> ETC;
		public RanksInfo(){
			ALL = new ArrayList<MJItemSearchRankInfo>(5);
			WEAPON = new ArrayList<MJItemSearchRankInfo>(5);
			ACCESSORY = new ArrayList<MJItemSearchRankInfo>(5);
			ARMOR = new ArrayList<MJItemSearchRankInfo>(5);
			ETC = new ArrayList<MJItemSearchRankInfo>(5);
		}
		
		public void append_rank_item(int itemId, MJNameCodeInfo trade_type, MJNameCodeInfo category){
			MJItemSearchRankInfo rInfo = new MJItemSearchRankInfo(itemId, trade_type, category);
			if(category == MJNameCodeInfo.CATEGORY.ALL){
				ALL.add(rInfo);
				rInfo.itemSearchRank.rank = ALL.size();
			}else if(category == MJNameCodeInfo.CATEGORY.WEAPON){
				WEAPON.add(rInfo);
				rInfo.itemSearchRank.rank = WEAPON.size();
			}else if(category == MJNameCodeInfo.CATEGORY.ACCESSORY){
				ACCESSORY.add(rInfo);
				rInfo.itemSearchRank.rank = ACCESSORY.size();
			}else if(category == MJNameCodeInfo.CATEGORY.ARMOR){
				ARMOR.add(rInfo);
				rInfo.itemSearchRank.rank = ARMOR.size();
			}else{
				ETC.add(rInfo);
				rInfo.itemSearchRank.rank = ETC.size();
			}
		}
	}
}

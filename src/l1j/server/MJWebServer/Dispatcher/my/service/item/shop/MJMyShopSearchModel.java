package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import l1j.server.MJTemplate.MJString;

public class MJMyShopSearchModel {
	String itemName;
	int iconId;
	int enchant;
	MJMyPrivateShopSearchStatistics sellingStatistics;
	MJMyPrivateShopSearchStatistics buyingStatistics;
	MJMyShopSearchModel(){
		itemName = MJString.EmptyString;
		sellingStatistics = new MJMyPrivateShopSearchStatistics();
		buyingStatistics = new MJMyPrivateShopSearchStatistics();
	}
	
	public String itemName(){
		return itemName;
	}
	
	public int iconId(){
		return iconId;
	}
	
	public int enchant(){
		return enchant;
	}
	
	public MJMyPrivateShopSearchStatistics sellingStatistics(){
		return sellingStatistics;
	}
	
	public MJMyPrivateShopSearchStatistics buyingStatistics(){
		return buyingStatistics;
	}
	
	MJMyPrivateShopSearchStatistics selectSearchStatistics(boolean selling){
		return selling ? sellingStatistics : buyingStatistics;
	}
	
	static class MJMyPrivateShopSearchStatistics{
		MJMyPrivateShopSearchStore all;
		MJMyPrivateShopSearchStore normal;
		MJMyPrivateShopSearchStore bless;
		MJMyPrivateShopSearchStore curse;
		MJMyPrivateShopSearchStore notIdent;
	
		public MJMyPrivateShopSearchStore all(){
			return all;
		}
		public MJMyPrivateShopSearchStore normal(){
			return normal;
		}
		public MJMyPrivateShopSearchStore bless(){
			return bless;
		}
		public MJMyPrivateShopSearchStore curse(){
			return curse;
		}
		public MJMyPrivateShopSearchStore notIdent(){
			return notIdent;
		}
		
		void onNewStore(int blessFlag, int priceLow, int priceHigh, int numOfStore){
			selectStatistics(blessFlag).onNewStore(priceLow, priceHigh, numOfStore);
			(all = safeAllocate(all)).onAppendStore(priceLow, priceHigh, numOfStore);
		}
		
		private MJMyPrivateShopSearchStore selectStatistics(int blessFlag){
			switch(blessFlag){
			case 0:
				return bless = safeAllocate(bless); 
			case 1:
				return normal = safeAllocate(normal); 
			case 2:
				return curse = safeAllocate(curse); 
			}
			return notIdent = safeAllocate(notIdent);
		}
		
		private MJMyPrivateShopSearchStore safeAllocate(MJMyPrivateShopSearchStore searchStore){
			return searchStore != null ? searchStore : new MJMyPrivateShopSearchStore();
		}
	}
	
	static class MJMyPrivateShopSearchStore{
		int priceLow;
		int priceHigh;
		int numOfStore;
		MJMyPrivateShopSearchStore(){
			priceLow = Integer.MAX_VALUE;
			priceHigh = Integer.MIN_VALUE;
		}
		
		public int priceLow(){
			return priceLow;
		}
		
		public int priceHigh(){
			return priceHigh;
		}
		
		public int numOfStore(){
			return numOfStore;
		}
		
		private void onNewStore(int priceLow, int priceHigh, int numOfStore){
			this.priceLow = priceLow;
			this.priceHigh = priceHigh;
			this.numOfStore = numOfStore;
		}
		
		private void onAppendStore(int priceLow, int priceHigh, int numOfStore){
			this.priceLow = Math.min(this.priceLow, priceLow);
			this.priceHigh = Math.max(this.priceHigh, priceHigh);
			this.numOfStore += numOfStore;
		}
	}
}

package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseBuilder;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseResult;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJGroupClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJLimitClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;

class MJMyPrivateShopProvider implements MJMyShopProvider<MJMyPrivateShopDetailModel>{
	private static final MJMyPrivateShopProvider provider = new MJMyPrivateShopProvider();
	static MJMyPrivateShopProvider provider(){
		return provider;
	}
	
	@Override
	public MJMyShopDetailResult<MJMyPrivateShopDetailModel> selectDetail(MJMyShopDetailArgs args, int page){
		MJMyShopDetailResult<MJMyPrivateShopDetailModel> result = new MJMyShopDetailResult<MJMyPrivateShopDetailModel>();
		result.models = selectDetailModel(args, page, COUNT_PER_PAGE);
		result.totalCount = page != 1 || result.models.size() >= COUNT_PER_PAGE ? numOfDetailModel(args) : result.models.size();
		result.totalPage = MJMyServiceHelper.calculateTotalPage(result.totalCount, COUNT_PER_PAGE);
		result.countPerPage = COUNT_PER_PAGE;
		return result;
	}

	@Override
	public MJMyShopSearchResult selectSearch(MJMyShopSearchArgs args, int page, String address){
		MJMyShopSearchResult result = new MJMyShopSearchResult();
		result.models = selectSearchModel(args, page, COUNT_PER_PAGE);
		result.totalCount = result.models.size();
		result.totalPage = 1;
		result.countPerPage = result.totalCount;
		if(result.totalCount > 0){
			MJMyResource.realTimeKeywordProvider().view().currentModel().insert(args.keyword, address);
		}
		return result;
	}
	
	@Override
	public MJMyShopStatisticsModel selectStatisticsModel(MJMyShopDetailArgs args){
		return new MJClauseBuilder<MJMyShopStatisticsModel>()
				.table("character_shop")
				.columns("count(*) as numOfStore")
				.columns("max(price) as priceHigh")
				.columns("min(price) as priceLow")
				.whereClause(MJWhereClause.stringClause("Item_name", args.itemName))
				.whereClause(args.enchant != -1 ? MJWhereClause.integerClause("enchant", args.enchant) : MJWhereClause.emptyClause())
				.whereClause(args.status.clause())
				.whereClause(args.elemental.clause())
				.whereClause(args.tradeType.whereClause())
				.result(new MJMyPrivateShopStatisticsResult())
				.build(true);
	}
	
	private static class MJMyPrivateShopStatisticsResult implements MJClauseResult<MJMyShopStatisticsModel>{
		@Override
		public MJMyShopStatisticsModel onResult(ResultSet rs) throws Exception {
			MJMyShopStatisticsModel model = new MJMyShopStatisticsModel();
			if(rs.next()){
				model.numOfStore = rs.getInt("numOfStore");
				model.priceHigh = rs.getInt("priceHigh");
				model.priceLow = rs.getInt("priceLow");
			}
			return model;
		}
	}
	
	private int numOfDetailModel(MJMyShopDetailArgs args){
		return new MJClauseBuilder<Integer>()
				.table("character_shop")
				.columns("count(*) as numOfItems")
				.whereClause(MJWhereClause.stringClause("Item_name", args.itemName))
				.whereClause(args.enchant != -1 ? MJWhereClause.integerClause("enchant", args.enchant) : MJWhereClause.emptyClause())
				.whereClause(args.status.clause())
				.whereClause(args.elemental.clause())
				.whereClause(args.tradeType.whereClause())
				.result(new MJMyPrivateShopDetailCountResult())
				.build(true);
	}
	
	private static class MJMyPrivateShopDetailCountResult implements MJClauseResult<Integer>{

		@Override
		public Integer onResult(ResultSet rs) throws Exception {
			return rs.next() ? rs.getInt("numOfItems") : 0;
		}
		
	}
	
	private List<MJMyPrivateShopDetailModel> selectDetailModel(MJMyShopDetailArgs args, int page, int countPerPage){
		
		return new MJClauseBuilder<List<MJMyPrivateShopDetailModel>>()
				.table("character_shop")
				.whereClause(MJWhereClause.stringClause("Item_name", args.itemName))
				.whereClause(args.enchant != -1 ? MJWhereClause.integerClause("enchant", args.enchant) : MJWhereClause.emptyClause())
				.whereClause(args.status.clause())
				.whereClause(args.elemental.clause())
				.whereClause(args.tradeType.whereClause())
				.limitClause(MJLimitClause.pagedClauseFromPage(page, countPerPage))
				.result(new MJMyPrivateShopDetailResult(countPerPage))
				.build(true);
	}
	
	private static class MJMyPrivateShopDetailResult implements MJClauseResult<List<MJMyPrivateShopDetailModel>>{
		private final int countPerPage;
		MJMyPrivateShopDetailResult(int countPerPage){
			this.countPerPage = countPerPage;
		}
		
		@Override
		public List<MJMyPrivateShopDetailModel> onResult(ResultSet rs) throws Exception {
			List<MJMyPrivateShopDetailModel> models = new ArrayList<>(countPerPage);
			while(rs.next()){
				MJMyPrivateShopDetailModel model = MJMyPrivateShopDetailModel.newShop(rs);
				models.add(model);
			}
			return models;
		}	
	}
	
	private List<MJMyShopSearchModel> selectSearchModel(MJMyShopSearchArgs args, int page, int countPerPage){
		return new MJClauseBuilder<List<MJMyShopSearchModel>>()
				.table("character_shop")
				.whereClause(MJWhereClause.stringLikeClause("Item_name", args.keyword))
				.columns("Item_name")
				.columns("invgfx")
				.columns("min(price) as low")
				.columns("max(price) as high")
				.columns("count(*) as numOfStore")
				.columns("iden")
				.columns("type")
				.columns("enchant")
				.groupClause(MJGroupClause.compositeClause("Item_name", "iden", "enchant", "type"))
				.orderClause(args.priceSort.orderClause())
				.result(new MJMyPrivateShopSearchResult())
				.build(true);
	}
	
	private static class MJMyPrivateShopSearchResult implements MJClauseResult<List<MJMyShopSearchModel>>{
		@Override
		public List<MJMyShopSearchModel> onResult(ResultSet rs) throws Exception {
			HashMap<String, MJMyShopSearchModel> models = new HashMap<>();
			while(rs.next()){
				String name = rs.getString("Item_name");
				int enchant = rs.getInt("enchant");
				String key = new StringBuilder(name.length() + 4).append(enchant).append(name).toString();
				int iden = MJDShopStorage.getAppIden2PackIden(rs.getInt("iden"));
				boolean selling = rs.getInt("type") == 0;
				MJMyShopSearchModel model = models.get(key);
				if(model == null){
					model = new MJMyShopSearchModel();
					model.itemName = name;
					model.iconId = rs.getInt("invgfx");
					model.enchant = enchant;
					models.put(key, model);
				}
				if(iden == 1){
					model.iconId = rs.getInt("invgfx");
				}
				model.selectSearchStatistics(selling).onNewStore(iden, rs.getInt("low"), rs.getInt("high"), rs.getInt("numOfStore"));
			}
			return new ArrayList<>(models.values());
		}
	}
}

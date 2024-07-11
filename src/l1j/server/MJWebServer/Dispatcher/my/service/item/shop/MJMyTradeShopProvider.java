package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseBuilder;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseResult;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJGroupClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJLimitClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJOrderClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel.MJMyTradeShopItemModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel.MJMyTradeShopUserModel;

class MJMyTradeShopProvider implements MJMyShopProvider<MJMyTradeShopDetailModel>{
	private static final MJMyTradeShopProvider provider = new MJMyTradeShopProvider();
	public static MJMyTradeShopProvider provider(){
		return provider;
	}

	private AtomicInteger tradeNo;
	private MJMyTradeShopProvider(){
		tradeNo = new AtomicInteger(tradeNo0());
	}
	
	int tradeNo(){
		return tradeNo.getAndIncrement();
	}
	
	private int tradeNo0(){
		return new MJClauseBuilder<Integer>()
				.table("ncoin_trade_item")
				.columns("max(trade_no)+1 as trade_no")
				.result(new MJClauseResult<Integer>(){
					@Override
					public Integer onResult(ResultSet rs) throws Exception {
						return rs.next() ? rs.getInt("trade_no") : 1;
					}
				})
				.build(true);
	}
	
	boolean onCancelTradeModel(final int tradeNo, final String account){
		return Updator.exec("update ncoin_trade_item set "
				+ "completed=2 where "
				+ "trade_no=? and reg_account=?", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, tradeNo);
						pstm.setString(2, account);
					}				
				}) != 0;
	}
	
	boolean onBuyTradeModel(final MJMyTradeShopModel model){
		return Updator.exec("update ncoin_trade_item set "
				+ "commission=?,"
				+ "cons_date=?,cons_addr=?,cons_char_id=?,cons_char_name=?,cons_account=?,completed=1 where "
				+ "trade_no=? and completed=?", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						final MJMyTradeShopUserModel userModel = model.consumerModel();
						pstm.setString(++idx, String.format("%.2f", model.commission()));
						pstm.setTimestamp(++idx, new Timestamp(userModel.actionMillis()));
						pstm.setString(++idx, userModel.address());
						pstm.setInt(++idx, userModel.characterId());
						pstm.setString(++idx, userModel.characterName());
						pstm.setString(++idx, userModel.accountName());
						pstm.setInt(++idx, model.tradeNo());
						pstm.setInt(++idx, MJMyTradeShopCompleteCode.ALIVE.val());
					}
				}) != 0;
	}
	
	void newTradeModel(final MJMyTradeShopModel model){
		Updator.exec("insert into ncoin_trade_item set "
				+ "trade_no=?,reg_date=?,reg_addr=?,reg_char_id=?,reg_char_name=?,reg_account=?,"
				+ "displayPart=?,subject=?,trade_type=?,price=?,pricePerUnit=?,item_id=?,item_name=?,icon_id=?,quantity=?,enchant=?,identified=?,bless=?,elemental_enchant=?,doll_bonus_level=?,doll_bonus_value=?,bless_level=?,bless_type=?, bless_type_value =? ", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						final MJMyTradeShopUserModel userModel = model.registeredModel();
						final MJMyTradeShopItemModel itemModel = model.itemModel();
						pstm.setInt(++idx, model.tradeNo());
						pstm.setTimestamp(++idx, new Timestamp(userModel.actionMillis()));
						pstm.setString(++idx, userModel.address());
						pstm.setInt(++idx, userModel.characterId());
						pstm.setString(++idx, userModel.characterName());
						pstm.setString(++idx, userModel.accountName());
						pstm.setString(++idx, model.displayPart());
						pstm.setString(++idx, model.subject());
						pstm.setString(++idx, model.tradeType().name());
						pstm.setInt(++idx, itemModel.price());
						pstm.setInt(++idx, itemModel.pricePerUnit());
						pstm.setInt(++idx, itemModel.itemId());
						pstm.setString(++idx, itemModel.itemName());
						pstm.setInt(++idx, itemModel.iconId());
						pstm.setInt(++idx, itemModel.quantity());
						pstm.setInt(++idx, itemModel.enchant());
						pstm.setBoolean(++idx, itemModel.identified());
						pstm.setInt(++idx, itemModel.bless());
						pstm.setInt(++idx, itemModel.elementalEnchant());
						
						pstm.setInt(++idx, itemModel.dollbonuslevel());
						pstm.setInt(++idx, itemModel.dollbonusvalue());
						pstm.setInt(++idx, itemModel.blesslevel());

						pstm.setInt(++idx, itemModel.BlessType());
						pstm.setInt(++idx, itemModel.BlessTypeValue());
					}
				});
	}
	
	MJMyTradeShopModel selectTradeShopModel(final int tradeNo){
		return new MJClauseBuilder<MJMyTradeShopModel>()
				.table("ncoin_trade_item")
				.whereClause(MJWhereClause.integerClause("trade_no", tradeNo))
				.limitClause(MJLimitClause.oneRowLimitClause())
				.result(new MJClauseResult<MJMyTradeShopModel>(){
					@Override
					public MJMyTradeShopModel onResult(ResultSet rs) throws Exception {
						return rs.next() ? MJMyTradeShopModel.newModel(rs) : null;
					}
				})
				.build(true);
	}
	
	@Override
	public MJMyShopDetailResult<MJMyTradeShopDetailModel> selectDetail(MJMyShopDetailArgs args, int page){
		MJMyShopDetailResult<MJMyTradeShopDetailModel> result = new MJMyShopDetailResult<MJMyTradeShopDetailModel>();
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
				.table("ncoin_trade_item")
				.columns("count(*) as numOfStore")
				.columns("max(pricePerUnit) as priceHigh")
				.columns("min(pricePerUnit) as priceLow")
				.columns("completed")
				.whereClause(MJWhereClause.isNullClause("cons_date"))
				.whereClause(MJWhereClause.stringClause("item_name", args.itemName))
				.whereClause(args.enchant != -1 ? MJWhereClause.integerClause("enchant", args.enchant) : MJWhereClause.emptyClause())
				.whereClause(args.status.tradeClause())
				.whereClause(args.elemental.tradeClause())
				.whereClause(args.tradeType.tradeWhereClause())
				.groupClause(MJGroupClause.simpleClause("completed"))
				.result(new MJMyTradeShopStatisticsResult())
				.build(true);
	}
	
	private static class MJMyTradeShopStatisticsResult implements MJClauseResult<MJMyShopStatisticsModel>{
		@Override
		public MJMyShopStatisticsModel onResult(ResultSet rs) throws Exception {
			MJMyShopStatisticsModel model = new MJMyShopStatisticsModel();
			while(rs.next()){
				MJMyTradeShopCompleteCode code = MJMyTradeShopCompleteCode.fromResultSet(rs);
				if(code == MJMyTradeShopCompleteCode.COMPLETE){
					model.statisticsTotal = rs.getInt("numOfStore");
					model.statisticsHigh = rs.getInt("priceHigh");
					model.statisticsLow = rs.getInt("priceLow");
				}else if(code == MJMyTradeShopCompleteCode.ALIVE){
					model.numOfStore = rs.getInt("numOfStore");
					model.priceHigh = rs.getInt("priceHigh");
					model.priceLow = rs.getInt("priceLow");
				}
			}
			return model;
		}
	}
	
	private int numOfDetailModel(MJMyShopDetailArgs args){
		return new MJClauseBuilder<Integer>()
				.table("ncoin_trade_item")
				.columns("count(*) as numOfItems")
				.whereClause(MJWhereClause.integerClause("completed", 0))
				.whereClause(MJWhereClause.isNullClause("cons_date"))
				.whereClause(MJWhereClause.stringClause("item_name", args.itemName))
				.whereClause(args.enchant != -1 ? MJWhereClause.integerClause("enchant", args.enchant) : MJWhereClause.emptyClause())
				.whereClause(args.status.tradeClause())
				.whereClause(args.elemental.tradeClause())
				.whereClause(args.tradeType.tradeWhereClause())
				.result(new MJMyTradeShopDetailCountResult())
				.build(true);
	}
	
	private static class MJMyTradeShopDetailCountResult implements MJClauseResult<Integer>{
		@Override
		public Integer onResult(ResultSet rs) throws Exception {
			return rs.next() ? rs.getInt("numOfItems") : 0;
		}
	}
	
	private List<MJMyTradeShopDetailModel> selectDetailModel(final MJMyShopDetailArgs args, final int page, final int countPerPage){
		return new MJClauseBuilder<List<MJMyTradeShopDetailModel>>()
				.table("ncoin_trade_item")
				.whereClause(MJWhereClause.integerClause("completed", 0))
				.whereClause(MJWhereClause.isNullClause("cons_date"))
				.whereClause(MJWhereClause.stringClause("item_name", args.itemName))
				.whereClause(args.enchant != -1 ? MJWhereClause.integerClause("enchant", args.enchant) : MJWhereClause.emptyClause())
				.whereClause(args.status.tradeClause())
				.whereClause(args.elemental.tradeClause())
				.whereClause(args.tradeType.tradeWhereClause())
				.limitClause(MJLimitClause.pagedClauseFromPage(page, countPerPage))
				.result(new MJMyTradeShopDetailResult(countPerPage))
				.build(true);
	}
	
	private static class MJMyTradeShopDetailResult implements MJClauseResult<List<MJMyTradeShopDetailModel>>{
		private final int countPerPage;
		MJMyTradeShopDetailResult(int countPerPage){
			this.countPerPage = countPerPage;
		}
		
		@Override
		public List<MJMyTradeShopDetailModel> onResult(ResultSet rs) throws Exception {
			List<MJMyTradeShopDetailModel> models = new ArrayList<>(countPerPage);
			while(rs.next()){
				MJMyTradeShopModel model = MJMyTradeShopModel.newModel(rs);
				models.add(MJMyTradeShopDetailModel.newShop(model));
			}
			return models;
		}	
	}
	
	private List<MJMyShopSearchModel> selectSearchModel(MJMyShopSearchArgs args, int page, int countPerPage){
		return new MJClauseBuilder<List<MJMyShopSearchModel>>()
				.table("ncoin_trade_item")
				.columns("item_name")
				.columns("icon_id")
				.columns("min(pricePerUnit) as low")
				.columns("max(pricePerUnit) as high")
				.columns("count(*) as numOfStore")
				.columns("identified")
				.columns("bless")
				.columns("trade_type")
				.columns("enchant")
				.whereClause(MJWhereClause.integerClause("completed", 0))
				.whereClause(MJWhereClause.isNullClause("cons_date"))
				.whereClause(MJWhereClause.stringLikeClause("item_name", args.keyword))
				.groupClause(MJGroupClause.compositeClause("item_name", "identified", "bless", "enchant", "trade_type"))
				.orderClause(args.priceSort.orderClause())
				.result(new MJMyTradeShopSearchResult())
				.build(true);
	}
	
	private static class MJMyTradeShopSearchResult implements MJClauseResult<List<MJMyShopSearchModel>> {
		@Override
		public List<MJMyShopSearchModel> onResult(ResultSet rs) throws Exception {
			HashMap<String, MJMyShopSearchModel> models = new HashMap<>();
			while(rs.next()){
				String name = rs.getString("item_name");
				int enchant = rs.getInt("enchant");
				boolean identified = rs.getBoolean("identified");
				int bless = rs.getInt("bless");
				boolean selling = rs.getString("trade_type").equalsIgnoreCase("SELL");
				String key = new StringBuilder(name.length() + 4).append(enchant).append(name).toString();
				MJMyShopSearchModel model = models.get(key);
				if(model == null){
					model = new MJMyShopSearchModel();
					model.itemName = name;
					model.iconId = rs.getInt("icon_id");
					model.enchant = enchant;
					models.put(key, model);
				}
				if(bless == 1){
					model.iconId = rs.getInt("icon_id");
				}
				model.selectSearchStatistics(selling).onNewStore(identified ? bless : -1, rs.getInt("low"), rs.getInt("high"), rs.getInt("numOfStore"));
			}
			return new ArrayList<>(models.values());
		}
	}
	
	List<MJMyTradeShopModel> selectMeList(final String account, MJMyShopTradeListType tradeListType){
		return new MJClauseBuilder<List<MJMyTradeShopModel>>()
				.table("ncoin_trade_item")
				.whereClause(tradeListType.whereClause(account))
				.whereClause(MJMyShopTradeType.SELL.tradeWhereClause())
				.orderClause(MJOrderClause.simpleClause("trade_no", false))
				.result(new MJMyTradeShopResult())
				.build(true);
	}
	
	private static class MJMyTradeShopResult implements MJClauseResult<List<MJMyTradeShopModel>>{
		MJMyTradeShopResult(){
		}
		
		@Override
		public List<MJMyTradeShopModel> onResult(ResultSet rs) throws Exception {
			List<MJMyTradeShopModel> models = new LinkedList<>();
			while(rs.next()){
				MJMyTradeShopModel model = MJMyTradeShopModel.newModel(rs);
				models.add(model);
			}
			return models;
		}	
	}
	
	MJMyShopStatisticsGmModel selectStatisticsGmModel(MJMyTradeShopCompleteCode code){
		return new MJClauseBuilder<MJMyShopStatisticsGmModel>()
			.table("ncoin_trade_item")
			.whereClause(MJWhereClause.integerClause("completed", code.val()))
			.result(new MJMyShopStatisticsGmResult())
			.build(true);
	}
	
	private static class MJMyShopStatisticsGmResult implements MJClauseResult<MJMyShopStatisticsGmModel>{

		@Override
		public MJMyShopStatisticsGmModel onResult(ResultSet rs) throws Exception {
			MJMyShopStatisticsGmModel result = new MJMyShopStatisticsGmModel();
			while(rs.next()){
				result.onAppendData(rs);
			}
			return result;
		}
	}
}

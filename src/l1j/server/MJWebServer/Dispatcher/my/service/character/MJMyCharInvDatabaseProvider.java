package l1j.server.MJWebServer.Dispatcher.my.service.character;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseBuilder;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseResult;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.Warehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.templates.L1Item;

abstract class MJMyCharInvDatabaseProvider<T, P> {
	private static final String numOfItemsColumns = "count(*) as numOfItems";
	private static final CharacterInventoryProvider characterInventoryProvider = new CharacterInventoryProvider();
	private static final AccountWarehouseProvider accountWarehouseProvider = new AccountWarehouseProvider();
	private static final CharacterWarehouseProvider characterWarehouseProvider = new CharacterWarehouseProvider();
	private static final ElfWarehouseProvider elfWarehouseProvider = new ElfWarehouseProvider();
	
	static MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, Integer> characterInventory(){
		return characterInventoryProvider;
	}
	
	static MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, String> accountWarehouse(){
		return accountWarehouseProvider;
	}
	
	static MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, String> characterWarehouse(){
		return characterWarehouseProvider;
	}
	
	static MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, String> elfWarehouseProvider(){
		return elfWarehouseProvider;
	}
	
	private static int numOfItems(List<L1ItemInstance> itemInstances, Matcher<L1ItemInstance> matcher){
		if(itemInstances == null || itemInstances.size() <= 0){
			return 0;
		}
		int numOfItems = 0;
		for(L1ItemInstance item : itemInstances){
			if(!matcher.matches(item)){
				continue;
			}
			++numOfItems;
		}
		return numOfItems;
	}
	
	private static List<MJMyCharInvItemInfo> convert(List<L1ItemInstance> itemInstances, Matcher<L1ItemInstance> matcher){
		if(itemInstances == null || itemInstances.size() <= 0){
			return Collections.emptyList();
		}
		
		List<MJMyCharInvItemInfo> items = new LinkedList<>();
		for(L1ItemInstance item : itemInstances){
			if(!matcher.matches(item)){
				continue;
			}
			MJMyCharInvItemInfo info = new MJMyCharInvItemInfo();
			info.objectId = item.getId();
			info.iconId = item.getItem().getGfxId();
			info.itemId = item.getItemId();
			info.name = item.getName();
			info.count = item.getCount();
			info.equipped = item.isEquipped();
			info.identified = item.isIdentified();
			info.bless = item.getBless();
			info.templateBless = item.getItem().getBless();
			info.enchantLevel = item.getEnchantLevel();
			info.elementalEnchantLevel = item.getAttrEnchantLevel();
			
			info.dollbonuslevel = item.get_Doll_Bonus_Level();
			info.dollbonusvalue = item.get_Doll_Bonus_Value();
			
			info.BlessType = item.getBlessType();
			info.BlessTypeValue = item.getBlessTypeValue();
			
			info.display = L1ItemInstance.to_simple_description(item).trim();
			info.category = MJMyCharInvItemCategory.fromTemplate(item.getItem());
			items.add(info);
		}
		return items;
	}
	
	protected MJMyCharInvDatabaseProvider(){	
	}

	abstract int numOfItemsFromDatabase(final P key, MJWhereClause clause, final boolean whereAnd);
	abstract int numOfItemsFromMemory(final L1PcInstance pc);
	abstract int numOfItemsFromMemory(final L1PcInstance pc, Matcher<L1ItemInstance> matcher);
	abstract List<T> fromMemory(final L1PcInstance pc, Matcher<L1ItemInstance> matcher);
	abstract List<T> fromDatabase(final P key, final MJWhereClause clause, final boolean whereAnd);
	
	private static class MJCharInvDatabaseNumResult implements MJClauseResult<Integer>{
		@Override
		public Integer onResult(ResultSet rs) throws Exception {
			if(rs.next()){
				return rs.getInt("numOfItems");
			}
			return 0;
		}
	}
	
	private static final List<String> databaseColumns = Arrays.asList(
			"id",
			"item_id",
			"item_name",
			"count",
			"is_equipped",
			"bless",
			"enchantlvl",
			"attr_enchantlvl",
			"doll_bonus_level",
			"doll_bonus_value",
			"bless_level",
			"bless_type",
			"bless_type_value",
			"item_level",
			"is_id"
			);

	private static class MJItemDatabaseResult implements MJClauseResult<List<MJMyCharInvItemInfo>>{
		@Override
		public List<MJMyCharInvItemInfo> onResult(ResultSet rs) throws Exception {
			LinkedList<MJMyCharInvItemInfo> items = new LinkedList<>();
			while(rs.next()){
				MJMyCharInvItemInfo item = new MJMyCharInvItemInfo();
				item.objectId = rs.getInt("id");
				item.itemId = rs.getInt("item_id");
				item.name = rs.getString("item_name");
				item.count = rs.getInt("count");
				item.equipped = rs.getInt("is_equipped") != 0;
				item.identified = rs.getBoolean("is_id");
				item.bless = rs.getInt("bless");
				item.enchantLevel = rs.getInt("enchantlvl");
				item.elementalEnchantLevel = rs.getInt("attr_enchantlvl");
				
				item.dollbonuslevel = rs.getInt("doll_bonus_level");
				item.dollbonusvalue = rs.getInt("doll_bonus_value");
				item.blesslevel = rs.getInt("bless_level");
				
				item.BlessType = rs.getInt("bless_type");
				item.BlessTypeValue = rs.getInt("bless_type_value");
				
				int itemLevel = rs.getInt("item_level");
				L1Item template = ItemTable.getInstance().getTemplate(item.itemId);
				item.iconId = template.getGfxId();	
				item.templateBless = template.getBless();
				item.display = L1ItemInstance.to_simple_description(item.name, item.bless, item.enchantLevel, item.elementalEnchantLevel, item.dollbonuslevel, item.dollbonusvalue, item.blesslevel, item.BlessType, item.BlessTypeValue, itemLevel, item.count).trim();
				item.category = MJMyCharInvItemCategory.fromTemplate(template);
				items.add(item);
			}
			return items;
		}
		
	}
	
	private static class AccountWarehouseProvider extends MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, String>{

		@Override
		int numOfItemsFromDatabase(String key, MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<Integer>()
					.table("character_warehouse")
					.columns(numOfItemsColumns)
					.whereClause("account_name", key)
					.whereClause(clause)
					.result(new MJCharInvDatabaseNumResult())
					.build(whereAnd);
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc) {
			Warehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			return warehouse == null ? 0 : warehouse.getSize();
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			Warehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			return numOfItems(warehouse.getItems(), matcher);
		}

		@Override
		List<MJMyCharInvItemInfo> fromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			Warehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			return convert(warehouse.getItems(), matcher);
		}

		@Override
		List<MJMyCharInvItemInfo> fromDatabase(String key, MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<List<MJMyCharInvItemInfo>>()
					.table("character_warehouse")
					.columns(databaseColumns)
					.whereClause("account_name", key)
					.whereClause(clause)
					.result(new MJItemDatabaseResult())
					.build(true);
		}
	}
	
	
	
	private static class ElfWarehouseProvider extends MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, String>{
		@Override
		int numOfItemsFromDatabase(String key, MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<Integer>()
					.table("character_elf_warehouse")
					.columns(numOfItemsColumns)
					.whereClause("account_name", key)
					.whereClause(clause)
					.result(new MJCharInvDatabaseNumResult())
					.build(whereAnd);
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc) {
			Warehouse warehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
			return warehouse == null ? 0 : warehouse.getSize();
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			Warehouse warehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
			return numOfItems(warehouse.getItems(), matcher);
		}

		@Override
		List<MJMyCharInvItemInfo> fromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			Warehouse warehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
			return convert(warehouse.getItems(), matcher);
		}

		@Override
		List<MJMyCharInvItemInfo> fromDatabase(String key, MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<List<MJMyCharInvItemInfo>>()
					.table("character_elf_warehouse")
					.columns(databaseColumns)
					.whereClause("account_name", key)
					.whereClause(clause)
					.result(new MJItemDatabaseResult())
					.build(true);
		}
	}
	
	
	private static class CharacterWarehouseProvider extends MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, String>{
		@Override
		int numOfItemsFromDatabase(String key, MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<Integer>()
					.table("character_supplementary_service")
					.columns(numOfItemsColumns)
					.whereClause("account_name", key)
					.whereClause(clause)
					.result(new MJCharInvDatabaseNumResult())
					.build(whereAnd);
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc) {
			Warehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			return warehouse == null ? 0 : warehouse.getSize();
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			Warehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			return numOfItems(warehouse.getItems(), matcher);
		}

		@Override
		List<MJMyCharInvItemInfo> fromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			Warehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			return convert(warehouse.getItems(), matcher);
		}

		@Override
		List<MJMyCharInvItemInfo> fromDatabase(String key, MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<List<MJMyCharInvItemInfo>>()
					.table("character_supplementary_service")
					.columns(databaseColumns)
					.whereClause("account_name", key)
					.whereClause(clause)
					.result(new MJItemDatabaseResult())
					.build(true);
		}
	}
	
	
	
	
	
	
	private static class CharacterInventoryProvider extends MJMyCharInvDatabaseProvider<MJMyCharInvItemInfo, Integer>{

		@Override
		int numOfItemsFromDatabase(Integer key, MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<Integer>()
					.table("character_items")
					.columns(numOfItemsColumns)
					.whereClause("char_id", key)
					.whereClause(clause)
					.result(new MJCharInvDatabaseNumResult())
					.build(whereAnd);
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc) {
			return pc.getInventory().getSize();
		}

		@Override
		int numOfItemsFromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			int numOfItems = 0;
			for(L1ItemInstance item : pc.getInventory().getItems()){
				if(matcher.matches(item)){
					++numOfItems;
				}
			}
			return numOfItems;
		}

		@Override
		List<MJMyCharInvItemInfo> fromMemory(L1PcInstance pc, Matcher<L1ItemInstance> matcher) {
			return convert(pc.getInventory().getItems(), matcher);
		}

		@Override
		List<MJMyCharInvItemInfo> fromDatabase(final Integer key, final MJWhereClause clause, boolean whereAnd) {
			return new MJClauseBuilder<List<MJMyCharInvItemInfo>>()
					.table("character_items")
					.columns(databaseColumns)
					.whereClause("char_id", key)
					.whereClause(clause)
					.result(new MJItemDatabaseResult())
					.build(true);
		}
	}
}

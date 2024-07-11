package l1j.server.TJ;

import l1j.server.Config;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TJLostItemDatabaseProvider {
	// 遺失物品數量限制
	private static final int LOST_ITEMS_LIMIT = Config.ServerEnchant.TJCount;

	// 單例模式實例
	private static final TJLostItemDatabaseProvider provider = new TJLostItemDatabaseProvider();

	// 返回實例
	public static TJLostItemDatabaseProvider provider() {
		return provider;
	}

	// 私有構造函式，單例模式
	private TJLostItemDatabaseProvider() {
	}

	// 創建新的遺失物品記錄
	public TJLostItemModel newLostItem(final L1PcInstance pc, final L1ItemInstance item) {
		TJLostItemModel model = new TJLostItemModel();
		model.characterId = pc.getId(); // 角色 ID
		model.characterName = pc.getName(); // 角色名稱
		model.itemObjectId = item.getId(); // 物品物件 ID
		model.itemId = item.getItemId(); // 物品 ID
		model.itemName = item.getName(); // 物品名稱
		model.enchant = item.getEnchantLevel(); // 物品強化等級
		model.elementalEnchant = item.getAttrEnchantLevel(); // 元素強化等級
		model.instanceBless = item.getBless(); // 實例祝福狀態
		model.instanceCustomBless = item.get_bless_level(); // 實例自訂祝福狀態
		model.lostTime = System.currentTimeMillis(); // 遺失時間
		model.recoveryCount = 0; // 恢復次數
		insertLostItem(model); // 插入遺失物品記錄
		return model;
	}

	// 更新恢復次數
	public void updateRecovery(final TJLostItemModel model) {
		Updator.exec("update tj_lost_items set recovery_count=? where character_id=? and item_object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, model.recoveryCount()); // 恢復次數
				pstm.setInt(++idx, model.characterId()); // 角色 ID
				pstm.setInt(++idx, model.itemObjectId()); // 物品物件 ID
			}
		});
	}

	// 插入遺失物品記錄
	private void insertLostItem(final TJLostItemModel model) {
		Updator.exec("insert into tj_lost_items set "
				+ "character_id=?, character_name=?, item_object_id=?, item_id=?, item_name=?, enchant=?, elemental_enchant=?, instance_bless=?, instance_custom_bless=?, lost_time=?, recovery_count=?",
				new Handler() {

					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, model.characterId());
						pstm.setString(++idx, model.characterName());
						pstm.setInt(++idx, model.itemObjectId());
						pstm.setInt(++idx, model.itemId());
						pstm.setString(++idx, model.itemName());
						pstm.setInt(++idx, model.enchant());
						pstm.setInt(++idx, model.elementalEnchant());
						pstm.setInt(++idx, model.instanceBless());
						pstm.setInt(++idx, model.instanceCustomBless());
						pstm.setLong(++idx, model.lostTime());
						pstm.setInt(++idx, model.recoveryCount());
					}
				});
	}

	// 檢查是否包含遺失物品記錄
	public boolean containsLostItem(final int characterId, final int itemObjectId) {
		MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper<>();
		wrapper.value = false;
		Selector.exec("select count(*) from tj_lost_items where character_id=? and item_object_id=? limit 1", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, characterId);
				pstm.setInt(++idx, itemObjectId);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				wrapper.value = rs.next();
			}
		});
		return wrapper.value;
	}

	// 查詢角色的單個遺失物品記錄
	public TJLostItemModel selectCharacterLostItem(final int characterId, final int itemObjectId) {
		MJObjectWrapper<TJLostItemModel> wrapper = new MJObjectWrapper<>();
		wrapper.value = null;
		Selector.exec("select * from tj_lost_items where character_id=? and item_object_id=? limit 1", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, characterId);
				pstm.setInt(++idx, itemObjectId);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					wrapper.value = TJLostItemModel.newModel(rs);
				}
			}
		});
		return wrapper.value;
	}

	// 查詢角色的多個遺失物品記錄
	public List<TJLostItemModel> selectCharacterLostItems(final int characterId) {
		List<TJLostItemModel> models = new ArrayList<>();
		Selector.exec("select * from tj_lost_items where character_id=? and recovery_count=0 order by lost_time desc limit ?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, characterId);
				pstm.setInt(++idx, LOST_ITEMS_LIMIT);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					TJLostItemModel model = TJLostItemModel.newModel(rs);
					models.add(model);
				}
			}
		});
		return models;
	}
}

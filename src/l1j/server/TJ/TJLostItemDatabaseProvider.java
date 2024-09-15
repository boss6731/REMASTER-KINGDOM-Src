package l1j.server.TJ;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class TJLostItemDatabaseProvider {
	private static final int LOST_ITEMS_LIMIT = Config.ServerEnchant.TJCount;

	private static final TJLostItemDatabaseProvider provider = new TJLostItemDatabaseProvider();

	public static TJLostItemDatabaseProvider provider() {
		return provider;
	}

	private TJLostItemDatabaseProvider() {
	}

	public TJLostItemModel newLostItem(final L1PcInstance pc, final L1ItemInstance item) {
		TJLostItemModel model = new TJLostItemModel();
		model.characterId = pc.getId();
		model.characterName = pc.getName();
		model.itemObjectId = item.getId();
		model.itemId = item.getItemId();
		model.itemName = item.getName();
		model.enchant = item.getEnchantLevel();
		model.elementalEnchant = item.getAttrEnchantLevel();
		model.instanceBless = item.getBless();
		model.instanceCustomBless = item.get_bless_level();
		model.lostTime = System.currentTimeMillis();
		model.recoveryCount = 0;
		insertLostItem(model);
		return model;
	}

	public void updateRecovery(final TJLostItemModel model) {
		Updator.exec("update tj_lost_items set recovery_count=? where character_id=? and item_object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, model.recoveryCount());
				pstm.setInt(++idx, model.characterId());
				pstm.setInt(++idx, model.itemObjectId());
			}
		});
	}

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

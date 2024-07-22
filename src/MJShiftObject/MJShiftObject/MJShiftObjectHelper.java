package MJShiftObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import MJShiftObject.DB.MJEShiftDBName;
import MJShiftObject.DB.Helper.MJShiftSelector;
import MJShiftObject.DB.Helper.MJShiftUpdator;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Template.CommonServerBattleInfo;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.Account;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import server.threads.pc.AutoSaveThread.ExpCache;

public class MJShiftObjectHelper {

	/**
	 * 更新緩存
	 * @param object_id 對象ID
	 * @param cache 緩存對象
	 * @param server_identity 伺服器標識
	 */
	public static void update_cache(final int object_id, final ExpCache cache, final String server_identity) {
		// 構建SQL查詢語句，插入或更新緩存數據
		MJShiftUpdator.exec(String.format("insert into %s set "
								+ "object_id=?, character_name=?, exp=?, lvl=? on duplicate key update character_name=?, exp=?, lvl=?",
						MJEShiftDBName.CHARACTERS_CACHE.get_table_name(server_identity)),
				new Handler() {
					@override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0; // 索引初始化為0
						pstm.setInt(++idx, object_id); // 設置對象ID
						pstm.setString(++idx, cache.character_name); // 設置角色名稱
						pstm.setLong(++idx, cache.exp); // 設置經驗值
						pstm.setInt(++idx, cache.lvl); // 設置等級
						pstm.setString(++idx, cache.character_name); // 更新角色名稱
						pstm.setLong(++idx, cache.exp); // 更新經驗值
						pstm.setInt(++idx, cache.lvl); // 更新等級
					}
				}
		);
	}


	public class MJShiftObjectHelper {

		/**
		 * 刪除緩存
		 * @param object_id 對象ID
		 * @param server_identity 伺服器標識
		 */
		public static void delete_cache(final int object_id, final String server_identity) {
			// 執行SQL查詢，刪除緩存數據
			MJShiftUpdator.exec(String.format("delete from %s where object_id=?", MJEShiftDBName.CHARACTERS_CACHE.get_table_name(server_identity)), new Handler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, object_id); // 設置對象ID參數
				}
			});
		}

		/**
		 * 查詢緩存
		 * @param object_id 對象ID
		 * @param server_identity 伺服器標識
		 * @return ExpCache 緩存對象
		 */
		public static ExpCache select_cache(final int object_id, final String server_identity) {
			MJObjectWrapper<ExpCache> wrapper = new MJObjectWrapper<ExpCache>(); // 包裝對象，用於存儲查詢結果

			// 執行SQL查詢，選擇緩存數據
			MJShiftSelector.exec(String.format("select * from %s where object_id=?", MJEShiftDBName.CHARACTERS_CACHE.get_table_name(server_identity)), new SelectorHandler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, object_id); // 設置對象ID參數
				}

				@override
				public void result(ResultSet rs) throws Exception {
					if (rs.next()) { // 如果有結果
						wrapper.value = new ExpCache(object_id, rs.getString("character_name"), rs.getLong("exp"), rs.getInt("lvl")); // 創建並設置緩存對象
					}
				}
			});
			return wrapper.value; // 返回查詢結果
		}
	}

	public static List<L1ItemInstance> select_pickup_items(final int object_id, final String server_identity) {
		MJObjectWrapper<ArrayList<L1ItemInstance>> wrapper = new MJObjectWrapper<ArrayList<L1ItemInstance>>();
		wrapper.value = new ArrayList<L1ItemInstance>();

		// 執行SQL查詢，選取角色拾取的物品
		MJShiftSelector.exec(String.format("select * from %s where char_id=?", MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity)), new SelectorHandler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置角色ID參數
			}

			@override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) { // 遍歷結果集
					int item_id = rs.getInt("item_id"); // 獲取物品ID
					L1Item template = ItemTable.getInstance().getTemplate(item_id); // 根據物品ID獲取物品模板
					if (template == null) { // 如果物品模板不存在
						System.out.println(String.format("在伺服器遷移過程中遺失了物品，該物品將被取消。物品ID: %d", item_id));
						continue; // 跳過該物品
					}

					// 創建並設置物品實例
					L1ItemInstance item = new L1ItemInstance();
					item.setId(rs.getInt("id"));
					item.setItem(template);
					item.setCount(rs.getInt("count"));
					item.setEquipped(rs.getInt("is_equipped") != 0);
					item.setEnchantLevel(rs.getInt("enchantlvl"));
					item.setIdentified(rs.getInt("is_id") != 0);
					item.set_durability(rs.getInt("durability"));
					item.setChargeCount(rs.getInt("charge_count"));
					item.setRemainingTime(rs.getInt("remaining_time"));
					item.setLastUsed(rs.getTimestamp("last_used"));
					item.setBless(rs.getInt("bless"));
					item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
					item.setEndTime(rs.getTimestamp("end_time"));
					item.setPackage(rs.getInt("package") != 0);
					item.set_bless_level(rs.getInt("bless_level"));
					item.set_item_level(rs.getInt("item_level"));
					item.setHotel_Town(rs.getString("Hotel_Town"));

					// 將物品實例添加到列表中
					wrapper.value.add(item);
				}
			}
		});

		return wrapper.value; // 返回拾取的物品列表
	}

	public static void delete_pickup_items(final int object_id, final String server_identity){
		MJShiftUpdator.exec(String.format("delete from %s where char_id=?", MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}

	public static void update_pickup_items(final int object_id, final L1ItemInstance item, final String server_identity) {
		// 構建SQL查詢語句
		MJShiftUpdator.exec(String.format("insert into %s set "
								+ "id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?, buy_time=?, package=?, bless_level=?, item_level=?, Hotel_Town=?",
						MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity)),
				new Handler() {
					@override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0; // 初始化索引

						// 設置各屬性到PreparedStatement
						pstm.setInt(++idx, item.getId());
						pstm.setInt(++idx, item.getItem().getItemId());
						pstm.setInt(++idx, object_id);
						pstm.setString(++idx, item.getItem().getName());
						pstm.setInt(++idx, item.getCount());
						pstm.setInt(++idx, item.isEquipped() ? 1 : 0); // 設置是否裝備，轉換為1或0
						pstm.setInt(++idx, item.getEnchantLevel());
						pstm.setInt(++idx, item.isIdentified() ? 1 : 0); // 設置是否識別，轉換為1或0
						pstm.setInt(++idx, item.get_durability());
						pstm.setInt(++idx, item.getChargeCount());
						pstm.setInt(++idx, item.getRemainingTime());
						pstm.setTimestamp(++idx, item.getLastUsed());
						pstm.setInt(++idx, item.getBless());
						pstm.setInt(++idx, item.getAttrEnchantLevel());
						pstm.setTimestamp(++idx, item.getEndTime());
						pstm.setTimestamp(++idx, item.getBuyTime()); // 設置購買時間
						pstm.setInt(++idx, item.isPackage() ? 1 : 0); // 設置是否為套裝，轉換為1或0
						pstm.setInt(++idx, item.get_bless_level());
						pstm.setInt(++idx, item.get_item_level());
						pstm.setString(++idx, item.getHotel_Town());
					}
				});
	}

	public class MJShiftObjectHelper {

		/**
		 * 刪除戰斗伺服器
		 * @param server_identity 伺服器標識
		 */
		public static void delete_battle_server(final String server_identity) {
			MJShiftUpdator.exec("delete from common_server_battle_reservation where server_identity=?", new Handler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, server_identity); // 設置伺服器標識參數
				}
			});
		}

		/**
		 * 獲取戰斗伺服器信息
		 * @return List<CommonServerBattleInfo> 戰斗伺服器信息列表
		 */
		public static List<CommonServerBattleInfo> get_battle_servers_info() {
			final MJObjectWrapper<ArrayList<CommonServerBattleInfo>> wrapper = new MJObjectWrapper<ArrayList<CommonServerBattleInfo>>();
			wrapper.value = new ArrayList<CommonServerBattleInfo>();

			// 執行SQL查詢，選取所有戰斗伺服器的預約信息
			MJShiftSelector.exec("select * from common_server_battle_reservation", new FullSelectorHandler() {
				@override
				public void result(ResultSet rs) throws Exception {
					while (rs.next()) { // 遍歷結果集
						CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance(rs); // 創建並設置戰斗伺服器信息實例
						wrapper.value.add(bInfo); // 將戰斗伺服器信息實例添加到列表中
					}
				}
			});

			return wrapper.value; // 返回戰斗伺服器信息列表
		}
	}

	public class MJShiftObjectHelper {

		/**
		 * 獲取特定戰斗伺服器信息
		 * @param server_identity 伺服器標識
		 * @return CommonServerBattleInfo 戰斗伺服器信息實例
		 */
		public static CommonServerBattleInfo get_battle_server_info(final String server_identity) {
			final MJObjectWrapper<CommonServerBattleInfo> wrapper = new MJObjectWrapper<CommonServerBattleInfo>();

			// 執行SQL查詢，選取特定伺服器的預約信息
			MJShiftSelector.exec("select * from common_server_battle_reservation where server_identity=?", new SelectorHandler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, server_identity); // 設置伺服器標識參數
				}

				@override
				public void result(ResultSet rs) throws Exception {
					while (rs.next()) { // 遍歷結果集
						CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance(rs); // 創建並設置戰斗伺服器信息實例
						wrapper.value = bInfo; // 設置獲取到的戰斗伺服器信息實例
					}
				}
			});

			return wrapper.value; // 返回戰斗伺服器信息實例
		}
	}

	public static void reservation_server_battle(final String server_identity, final long start_millis, final long ended_millis, final int current_kind, final String battle_name) {
		// 執行SQL查詢，插入或更新戰斗伺服器預約信息
		MJShiftUpdator.exec("insert into common_server_battle_reservation set "
				+ "server_identity=?, start_time=?, ended_time=?, current_kind=?, battle_name=? "
				+ "on duplicate key update "
				+ "start_time=?, ended_time=?, current_kind=?, battle_name=?", new Handler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0; // 初始化索引

				// 設置各屬性到PreparedStatement
				pstm.setString(++idx, server_identity); // 設置伺服器標識
				Timestamp start_ts = new Timestamp(start_millis); // 將開始時間轉換為Timestamp
				Timestamp ended_ts = new Timestamp(ended_millis); // 將結束時間轉換為Timestamp
				pstm.setTimestamp(++idx, start_ts); // 設置開始時間
				pstm.setTimestamp(++idx, ended_ts); // 設置結束時間
				pstm.setInt(++idx, current_kind); // 設置當前類型
				pstm.setString(++idx, battle_name); // 設置戰斗名稱

				// 設置更新時的屬性
				pstm.setTimestamp(++idx, start_ts); // 設置更新時的開始時間
				pstm.setTimestamp(++idx, ended_ts); // 設置更新時的結束時間
				pstm.setInt(++idx, current_kind); // 設置更新時的當前類型
				pstm.setString(++idx, battle_name); // 設置更新時的戰斗名稱
			}
		});
	}

	public class MJShiftObjectHelper {

		/**
		 * 獲取公共伺服器信息列表
		 * @param server_identity 當前伺服器的標識
		 * @param is_exclude_my_server 是否排除當前伺服器
		 * @return List<CommonServerInfo> 公共伺服器信息列表
		 */
		public static List<CommonServerInfo> get_commons_servers(String server_identity, boolean is_exclude_my_server) {
			final MJObjectWrapper<ArrayList<CommonServerInfo>> wrapper = new MJObjectWrapper<ArrayList<CommonServerInfo>>();
			wrapper.value = new ArrayList<CommonServerInfo>();

			// 執行SQL查詢，選取所有公共伺服器信息
			MJShiftSelector.exec("select * from common_shift_server_info", new FullSelectorHandler() {
				@override
				public void result(ResultSet rs) throws Exception {
					while (rs.next()) { // 遍歷結果集
						CommonServerInfo csInfo = CommonServerInfo.newInstance(rs); // 創建並設置公共伺服器信息實例

						if (is_exclude_my_server && csInfo.getServerIdentity().equals(server_identity)) {
							continue; // 如果需要排除當前伺服器，且當前伺服器標識和查詢結果中的伺服器標識相同，則跳過
						}

						wrapper.value.add(csInfo); // 將公共伺服器信息實例添加到列表中
					}
				}
			});

			return wrapper.value; // 返回公共伺服器信息列表
		}
	}

	public class MJShiftObjectHelper {

		/**
		 * 開啟伺服器信息狀態
		 * @param server_identity 伺服器標識
		 */
		public static void on_shift_server_info(final String server_identity) {
			update_shift_server_state(server_identity, true);
		}

		/**
		 * 關閉伺服器信息狀態
		 * @param server_identity 伺服器標識
		 */
		public static void off_shift_server_info(final String server_identity) {
			update_shift_server_state(server_identity, false);
		}

		/**
		 * 更新伺服器信息狀態
		 * @param server_identity 伺服器標識
		 * @param is_on 是否開啟
		 */
		public static void update_shift_server_state(final String server_identity, final boolean is_on) {
			// 執行SQL查詢，更新伺服器的啟用狀態
			MJShiftUpdator.exec("update common_shift_server_info set server_is_on=? where server_identity=?", new Handler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, String.valueOf(is_on)); // 設置伺服器啟用狀態（字符串形式）
					pstm.setString(2, server_identity); // 設置伺服器標識參數
				}
			});
		}
	}

	public class MJShiftObjectHelper {

		/**
		 * 刪除指定的Shift對象
		 * @param sobj 要刪除的Shift對象
		 * @param server_identity 伺服器標識
		 */
		public static void delete_shift_object(final MJShiftObject sobj, final String server_identity) {
			// 執行SQL查詢，刪除指定的Shift對象
			MJShiftUpdator.exec(String.format("delete from %s where source_id=?", MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity)),
					new Handler() {
						@override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setInt(1, sobj.get_source_id()); // 設置source_id參數
						}
					});
		}

		/**
		 * 清空Shift數據
		 * @param server_identity 伺服器標識
		 * @param is_truncate_pickup 是否清空拾取數據
		 * @param is_truncate_cache 是否清空緩存數據
		 */
		public static void truncate_shift_datas(String server_identity, boolean is_truncate_pickup, boolean is_truncate_cache) {
			for (MJEShiftDBName dbname : MJEShiftDBName.values()) {
				if (dbname.equals(MJEShiftDBName.CHARACTERS_SLOT_ITEMS))
					continue;
				if (dbname.equals(MJEShiftDBName.CHARACTERS_TAMS))
					continue;
				if (!is_truncate_pickup && dbname.equals(MJEShiftDBName.CHARACTERS_PICKUP))
					continue;
				if (!is_truncate_cache && dbname.equals(MJEShiftDBName.CHARACTERS_CACHE))
					continue;

					// 清空指定的數據表
				MJShiftUpdator.truncate(dbname.get_table_name(server_identity));
			}
		}
	}

	public class MJShiftObjectHelper {

		/**
		 * 選擇指定的Shift對象
		 * @param source_id Shift對象的源ID
		 * @param server_identity 伺服器標識
		 * @return MJShiftObject 選擇的Shift對象
		 */
		public static MJShiftObject select_shift_object(final int source_id, final String server_identity) {
			MJObjectWrapper<MJShiftObject> wrapper = new MJObjectWrapper<MJShiftObject>();

			// 執行SQL查詢，選擇指定的Shift對象
			MJShiftSelector.exec(String.format("select * from %s where source_id=?", MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity)), new SelectorHandler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, source_id); // 設置source_id參數
				}

				@override
				public void result(ResultSet rs) throws Exception {
					if (rs.next()) { // 如果有結果
						wrapper.value = MJShiftObject.newInstance(rs); // 創建並設置Shift對象實例
					}
				}
			});

			return wrapper.value; // 返回Shift對象實例
		}
	}

	public class MJShiftObjectHelper {

		/**
		 * 更新指定的Shift對象
		 * @param sobj 要更新的Shift對象
		 * @param server_identity 伺服器標識
		 */
		public static void update_shift_object(final MJShiftObject sobj, final String server_identity) {
			MJShiftUpdator.exec(String.format("insert into %s set "
									+ "source_id=?, destination_id=?, shift_type=?, source_character_name=?, source_account_name=?, destination_character_name=?, destination_account_name=?, convert_parameters=? "
									+ "on duplicate key update "
									+ "destination_id=?, shift_type=?, source_character_name=?, source_account_name=?, destination_character_name=?, destination_account_name=?, convert_parameters=?",
							MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity)),
					new Handler() {
						@override
						public void handle(PreparedStatement pstm) throws Exception {
							int idx = 0; // 初始化索引

							// 插入部分
							pstm.setInt(++idx, sobj.get_source_id()); // 設置source_id參數
							pstm.setInt(++idx, sobj.get_destination_id()); // 設置destination_id參數
							pstm.setString(++idx, sobj.get_shift_type().to_name()); // 設置shift_type參數
							pstm.setString(++idx, sobj.get_source_character_name()); // 設置source_character_name參數
							pstm.setString(++idx, sobj.get_source_account_name()); // 設置source_account_name參數
							pstm.setString(++idx, sobj.get_destination_character_name()); // 設置destination_character_name參數
							pstm.setString(++idx, sobj.get_destination_account_name()); // 設置destination_account_name參數
							pstm.setString(++idx, sobj.get_convert_parameters()); // 設置convert_parameters參數

							// 更新部分
							pstm.setInt(++idx, sobj.get_destination_id()); // 設置更新時的destination_id參數
							pstm.setString(++idx, sobj.get_shift_type().to_name()); // 設置更新時的shift_type參數
							pstm.setString(++idx, sobj.get_source_character_name()); // 設置更新時的source_character_name參數
							pstm.setString(++idx, sobj.get_source_account_name()); // 設置更新時的source_account_name參數
							pstm.setString(++idx, sobj.get_destination_character_name()); // 設置更新時的destination_character_name參數
							pstm.setString(++idx, sobj.get_destination_account_name()); // 設置更新時的destination_account_name參數
							pstm.setString(++idx, sobj.get_convert_parameters()); // 設置更新時的convert_parameters參數
						}
					});
		}
	}
	public class MJShiftObjectHelper {

		/**
		 * 改變帳戶密碼
		 * @param account_name 帳戶名
		 */
		public static void shuffle_account_password(final String account_name) {
			Updator.exec("update accounts set login=? where login=?", new Handler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, String.format("[%s]", account_name)); // 設置新的登入名稱，格式為 [帳戶名]
					pstm.setString(2, account_name); // 設置查詢條件為原帳戶名
				}
			});
		}

		/**
		 * 改變角色名稱和帳戶名
		 * @param object_id 角色ID
		 * @param char_name 角色名稱
		 * @param account_name 帳戶名
		 */
		public static void shuffle_character_name(final int object_id, final String char_name, final String account_name) {
			Updator.exec("update characters set char_name=?, account_name=? where objid=?", new Handler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, String.format("[%s]", char_name)); // 設置新的角色名稱，格式為 [角色名]
					pstm.setString(2, String.format("[%s]", account_name)); // 設置新的帳戶名，格式為 [帳戶名]
					pstm.setInt(3, object_id); // 設置角色ID查詢條件
				}
			});
		}
	}
	public class MJShiftObjectHelper {

		/**
		 * 更新帳戶的帳戶名
		 * @param account 要更新的帳戶對象
		 * @param new_account_name 新的帳戶名
		 */
		public static void update_account_name(final Account account, final String new_account_name) {
			Updator.exec("update accounts set login=? where login=?", new Handler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, new_account_name); // 設置新的帳戶名
					pstm.setString(2, account.getName()); // 設置當前帳戶名作為查詢條件
					account.setName(new_account_name); // 更新帳戶對象中的帳戶名
				}
			});
		}

		/**
		 * 更新角色的帳戶名
		 * @param pc 要更新的角色對象
		 * @param new_account_name 新的帳戶名
		 */
		public static void update_account_name(final L1PcInstance pc, final String new_account_name) {
			Updator.exec("update characters set account_name=? where objid=?", new Handler() {
				@override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, new_account_name); // 設置新的帳戶名
					pstm.setInt(2, pc.getId()); // 設置角色ID作為查詢條件
					pc.setAccountName(new_account_name); // 更新角色對象中的帳戶名
				}
			});
		}
	}



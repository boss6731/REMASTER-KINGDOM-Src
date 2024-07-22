package MJShiftObject.Object.Converter;

import MJShiftObject.DB.Helper.MJShiftSelector;
import MJShiftObject.DB.Helper.MJShiftUpdator;
import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.Object.Converter.Selector.MJShiftObjectSelector;
import MJShiftObject.Object.Converter.Updator.MJShiftObjectUpdator;
import MJShiftObject.Object.MJShiftObject;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftObjectDBConverterFactory {

	/**
	 * 創建一個資料庫發送器實例。
	 *
	 * @param server_identity 伺服器身份
	 * @return 初始化好的資料庫發送器實例
	 */
	public static IMJShiftObjectDBConverter create_sender(String server_identity) {
		return new MJShiftObjectDBSender(server_identity);
	}

	/**
	 * 創建一個資料庫接收器實例。
	 *
	 * @param server_identity 伺服器身份
	 * @return 初始化好的資料庫接收器實例
	 */
	public static IMJShiftObjectDBConverter create_receiver(String server_identity) {
		return new MJShiftObjectDBReceiver(server_identity);
	}

	/**
	 * 內部類: 資料庫發送器，實現 IMJShiftObjectDBConverter 接口，用於處理發送操作。
	 */
	private static class MJShiftObjectDBSender implements IMJShiftObjectDBConverter {
		private MJShiftObjectUpdator m_updator;
		private MJShiftObjectSelector m_selector;

		/**
		 * 資料庫發送器構造函數。
		 *
		 * @param server_identity 伺服器身份
		 */
		MJShiftObjectDBSender(String server_identity) {
			this.m_updator = MJShiftObjectUpdator.newInstance(server_identity, new MJShiftUpdator());
			this.m_selector = MJShiftObjectSelector.newInstance("", new Selector());
		}

		@override
		public int work(MJShiftObject sobject) {
			try {
				int object_id = sobject.get_source_id();
				int update_id = sobject.get_destination_id();

				// 選擇角色信息
				L1PcInstance pc = this.m_selector.select_character_info(object_id);
				if (pc == null) {
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_PC;
				}

				// 選擇帳戶信息
				Account account = this.m_selector.select_accounts(pc.getAccountName());
				if (account == null) {
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_ACCOUNT;
				}

				// 更新角色和帳戶信息
				pc.setId(update_id);
				pc.setName(sobject.get_destination_character_name());
				pc.setAccountName(sobject.get_destination_account_name());
				account.setName(sobject.get_destination_account_name());

				this.m_updator.delete_character_info(sobject.get_destination_character_name());
				this.m_updator.update_character_info(pc, update_id);
				this.m_updator.delete_accounts(sobject.get_destination_account_name());
				this.m_updator.update_accounts(account);

				// 更新角色其他信息

				this.m_updator.update_character_buff(this.m_selector.select_character_buff(object_id), update_id);
				this.m_updator.update_character_config(this.m_selector.select_character_config(object_id), update_id);
				this.m_updator.update_character_items(this.m_selector.select_character_items(object_id), update_id);
				this.m_updator.update_character_skills(this.m_selector.select_character_skills(object_id), update_id);
				this.m_updator.update_character_passive(this.m_selector.select_character_passive(object_id), update_id);
				this.m_updator.update_character_quest_info(this.m_selector.select_character_quest_info(object_id), update_id);
				this.m_updator.update_character_level_bonus(this.m_selector.select_character_level_bonus(object_id).intValue(), update_id);

				// 更新 Shift 對象信息
				MJShiftObjectHelper.update_shift_object(sobject, this.m_updator.get_server_identity());

				return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				return IMJShiftObjectDBConverter.CONVERT_FAIL_INVALID;
			}
		}

		@override
		public int delete(MJShiftObject sobject) {
			try {
				int object_id = sobject.get_source_id();

				// 刪除角色信息
				this.m_updator.delete_character_info(object_id);

				// 刪除帳戶信息
				this.m_updator.delete_accounts(sobject.get_source_account_name());

				// 刪除角色 Buff
				this.m_updator.delete_character_buff(object_id);

				// 刪除角色配置
				this.m_updator.delete_character_config(object_id);

				// 刪除角色物品
				this.m_updator.delete_character_items(object_id);

				// 刪除角色技能
				this.m_updator.delete_character_skills(object_id);

				// 刪除角色被動技能
				this.m_updator.delete_character_passive(object_id);

				// 刪除角色任務信息
				this.m_updator.delete_character_quest_info(object_id);

				// 刪除角色等級獎勵
				this.m_updator.delete_character_level_bonus(object_id);

				// 刪除 Shift 對象信息
				MJShiftObjectHelper.delete_shift_object(sobject, this.m_updator.get_server_identity());

				return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				return IMJShiftObjectDBConverter.CONVERT_FAIL_INVALID;
			}
		}
	}

	/**
	 * 內部類: 資料庫接收器，實現 IMJShiftObjectDBConverter 接口，用於處理接收操作。
	 */
	private static class MJShiftObjectDBReceiver implements IMJShiftObjectDBConverter {
		private MJShiftObjectUpdator m_updator;
		private MJShiftObjectSelector m_selector;

		/**
		 * 資料庫接收器構造函數。
		 *
		 * @param server_identity 伺服器身份
		 */
		public MJShiftObjectDBReceiver(String server_identity) {
			this.m_updator = MJShiftObjectUpdator.newInstance(server_identity, new MJShiftUpdator());
			this.m_selector = MJShiftObjectSelector.newInstance("", new Selector());
		}

		@override
		public int work(MJShiftObject paramMJShiftObject) {
			// 實現轉換邏輯
			// 使用 m_selector 和 m_updator 執行資料庫操作
			return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
		}

		@override
		public int delete(MJShiftObject paramMJShiftObject) {
			// 實現刪除邏輯
			// 使用 m_selector 和 m_updator 執行資料庫操作
			return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
		}
	}



		public int delete(MJShiftObject sobject) {
			return 0;
		}
	}
}



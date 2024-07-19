package l1j.server.Beginner.Model;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_PROGRESS_UPDATE_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerUserProvider {

	// 靜態常量，用於存儲 MJBeginnerUser 的鍵
	private static final MJAttrKey<MJBeginnerUser> beginnerUserKey = MJAttrKey.newInstance("mj-beginner-user");
	// 靜態實例，用於單例模式
	private static final MJBeginnerUserProvider provider = new MJBeginnerUserProvider();

	// 提供靜態方法以獲取單例實例
	public static final MJBeginnerUserProvider provider() {
		return provider;
	}

	// 私有構造函數，防止外部實例化
	private MJBeginnerUserProvider() {
	}

	// 當進度通知時執行
	public void onProgressNoti(L1PcInstance pc) {
		MJBeginnerUser user = convertUser(pc);  // 將 L1PcInstance 轉換為 MJBeginnerUser
		if (user == null) {
			return;  // 如果用戶為空，直接返回
		}
		int usage = 0;
		for (MJBeginnerUserProgress progress : user.values()) {  // 遍歷用戶的所有進度
			if (progress.finished()) {
				continue;  // 如果進度已完成，跳過
			}
			SC_QUEST_PROGRESS_UPDATE_NOTI noti = SC_QUEST_PROGRESS_UPDATE_NOTI.newInstance();
			noti.set_quest(progress.convertClientModel());  // 設置任務進度
			// 신규 퀘스트 주석 (新任務註釋)
			pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_PROGRESS_UPDATE_NOTI);  // 發送進度更新通知包
			System.out.println("MJBeginnerUserProvider:" + ++usage);  // 打印使用次數
		}
	}

	// 將 L1PcInstance 轉換為 MJBeginnerUser
	MJBeginnerUser convertUser(L1PcInstance pc) {
		if (!pc.attribute().has(beginnerUserKey)) {
			return null;  // 如果 L1PcInstance 不包含 beginnerUserKey 屬性，返回 null
		}
		return pc.attribute().get(beginnerUserKey).get();  // 獲取並返回 MJBeginnerUser
	}

	// 當有新用戶時執行
	private void onNewUserInternal(L1PcInstance pc, MJBeginnerUser user) {
		pc.attribute().getNotExistsNew(beginnerUserKey).set(user);  // 設置新的 MJBeginnerUser
	}


	public class MJBeginnerUserProvider {

		// 靜態常量，用於存儲 MJBeginnerUser 的鍵
		private static final MJAttrKey<MJBeginnerUser> beginnerUserKey = MJAttrKey.newInstance("mj-beginner-user");
		// 靜態實例，用於單例模式
		private static final MJBeginnerUserProvider provider = new MJBeginnerUserProvider();

		// 提供靜態方法以獲取單例實例
		public static final MJBeginnerUserProvider provider() {
			return provider;
		}

		// 私有構造函數，防止外部實例化
		private MJBeginnerUserProvider() {
		}

		// 當進度通知時執行
		public void onProgressNoti(L1PcInstance pc) {
			MJBeginnerUser user = convertUser(pc);  // 將 L1PcInstance 轉換為 MJBeginnerUser
			if (user == null) {
				return;  // 如果用戶為空，直接返回
			}
			int usage = 0;
			for (MJBeginnerUserProgress progress : user.values()) {  // 遍歷用戶的所有進度
				if (progress.finished()) {
					continue;  // 如果進度已完成，跳過
				}
				SC_QUEST_PROGRESS_UPDATE_NOTI noti = SC_QUEST_PROGRESS_UPDATE_NOTI.newInstance();
				noti.set_quest(progress.convertClientModel());  // 設置任務進度
				// 신규 퀘스트 주석 (新任務註釋)
				pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_PROGRESS_UPDATE_NOTI);  // 發送進度更新通知包
				System.out.println("MJBeginnerUserProvider:" + ++usage);  // 打印使用次數
			}
		}

		// 將 L1PcInstance 轉換為 MJBeginnerUser
		MJBeginnerUser convertUser(L1PcInstance pc) {
			if (!pc.attribute().has(beginnerUserKey)) {
				return null;  // 如果 L1PcInstance 不包含 beginnerUserKey 屬性，返回 null
			}
			return pc.attribute().get(beginnerUserKey).get();  // 獲取並返回 MJBeginnerUser
		}

		// 當有新用戶時執行
		private void onNewUserInternal(L1PcInstance pc, MJBeginnerUser user) {
			pc.attribute().getNotExistsNew(beginnerUserKey).set(user);  // 設置新的 MJBeginnerUser
		}

		// 新用戶處理邏輯
		public MJBeginnerUser onNewUser(L1PcInstance pc) {
			MJBeginnerUser user = MJBeginnerUserDatabaseProvider.provider().selectUserProgress(pc.getId());  // 從數據庫選擇用戶進度
			onNewUserInternal(pc, user);  // 設置新用戶
			return user;  // 返回新用戶
		}
	}
}
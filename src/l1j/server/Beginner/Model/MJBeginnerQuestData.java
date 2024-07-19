package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerQuestData extends MJBeginnerData {
	// 定義私有變量
	private int _ID; // 任務ID
	private boolean _AutoActive; // 是否自動激活
	private boolean _Obsolete; // 是否過時
	private MJBeginnerDescriptionData _Description; // 描述數據
	private MJBeginnerPrerequisiteData _Prerequisite; // 前提條件數據
	private MJBeginnerObjectiveListData _ObjectiveList; // 目標列表數據
	private MJBeginnerRewardListData _AdvanceRewardList; // 提前獎勵列表數據
	private MJBeginnerRewardListData _RewardList; // 獎勵列表數據
	private MJBeginnerOptionalRewardListData _OptionalRewardList; // 可選獎勵列表數據
	private MJBeginnerTeleportData _Teleport; // 傳送數據

	// 獲取任務ID
	int id() {
		return _ID;
	}

	// 獲取是否自動激活
	boolean autoActive() {
		return _AutoActive;
	}

	// 獲取是否過時
	boolean obsolete() {
		return _Obsolete;
	}

	// 獲取描述數據
	MJBeginnerDescriptionData description() {
		return _Description;
	}

	// 檢查是否有描述數據
	boolean hasDescription() {
		return _Description != null;
	}

	// 獲取前提條件數據

	MJBeginnerPrerequisiteData prerequisite() {
		return _Prerequisite;
	}

	// 檢查是否有前提條件數據
	boolean hasPrerequisite() {
		return _Prerequisite != null;
	}

	// 獲取目標列表數據
	MJBeginnerObjectiveListData objectiveList() {
		return _ObjectiveList;
	}

	// 檢查是否有目標列表數據
	boolean hasObjectiveList() {
		return _ObjectiveList != null;
	}

	// 獲取提前獎勵列表數據
	MJBeginnerRewardListData advanceRewardList() {
		return _AdvanceRewardList;
	}


	// 檢查是否有提前獎勵列表數據
	boolean hasAdvanceRewardList() {
		return _AdvanceRewardList != null;
	}

	// 獲取獎勵列表數據
	MJBeginnerRewardListData rewardList() {
		return _RewardList;
	}

	// 檢查是否有獎勵列表數據
	boolean hasRewardList() {
		return _RewardList != null;
	}

	// 獲取可選獎勵列表數據
	MJBeginnerOptionalRewardListData optionalRewardList() {
		return _OptionalRewardList;
	}

	// 檢查是否有可選獎勵列表數據
	boolean hasOptionalRewardList() {
		return _OptionalRewardList != null;
	}

	// 獲取傳送數據
	MJBeginnerTeleportData teleport() {
		return _Teleport;
	}

	// 檢查是否有傳送數據
	boolean hasTeleport() {
		return _Teleport != null;
	}

	// 覆蓋initialize方法，初始化各個屬性
	@override
	void initialize() {
		if (hasDescription()) { // 如果有描述數據，進行初始化
			_Description.initialize();
		}
		if (hasPrerequisite()) { // 如果有前提條件數據，進行初始化
			_Prerequisite.initialize();
		}
		if (hasObjectiveList()) { // 如果有目標列表數據，進行初始化
			_ObjectiveList.initialize();
		}
		if (hasAdvanceRewardList()) { // 如果有提前獎勵列表數據，進行初始化
			_AdvanceRewardList.initialize();
		}
		if (hasRewardList()) { // 如果有獎勵列表數據，進行初始化
			_RewardList.initialize();
		}
		if (hasOptionalRewardList()) { // 如果有可選獎勵列表數據，進行初始化

			_OptionalRewardList.initialize();
		}
		if (hasTeleport()) { // 如果有傳送數據，進行初始化
			_Teleport.initialize();
		}
	}

	static class MJBeginnerDescriptionData extends MJBeginnerData {
		@override
		void initialize() {
			// 初始化方法，目前尚未實現具體邏輯
		}
	}


	// 定義一個靜態內部類，繼承自MJBeginnerData，並實現Matcher<L1PcInstance>接口
	static class MJBeginnerPrerequisiteData extends MJBeginnerData implements Matcher<L1PcInstance> {
		private MJBeginnerPrerequisiteLevel _Level; // 等級前提條件
		private MJBeginnerPrerequisiteClasses _CharacterClass; // 職業前提條件
		private MJBeginnerPrerequisiteRequiredQuest _Quest; // 任務前提條件
		private MJBeginnerPrerequisiteRegion _Region; // 地區前提條件
		private MJBeginnerPrerequisiteBloodPledge _BloodPledge; // 血盟前提條件
		private Matcher<L1PcInstance> matcher; // 匹配器

		// 獲取等級前提條件
		MJBeginnerPrerequisiteLevel level() {
			return _Level;
		}

		// 檢查是否有等級前提條件
		boolean hasLevel() {
			return _Level != null;
		}

		// 獲取職業前提條件
		MJBeginnerPrerequisiteClasses characterClass() {
			return _CharacterClass;
		}

		// 檢查是否有職業前提條件
		boolean hasCharacterClass() {
			return _CharacterClass != null;
		}

		// 獲取任務前提條件
		MJBeginnerPrerequisiteRequiredQuest quest() {
			return _Quest;
		}

		// 檢查是否有任務前提條件
		boolean hasQuest() {
			return _Quest != null;
		}

		// 獲取地區前提條件

		MJBeginnerPrerequisiteRegion region() {
			return _Region;
		}

		// 檢查是否有地區前提條件
		boolean hasRegion() {
			return _Region != null;
		}

		// 獲取血盟前提條件
		MJBeginnerPrerequisiteBloodPledge bloodPledge() {
			return _BloodPledge;
		}

		// 檢查是否有血盟前提條件
		boolean hasBloodPledge() {
			return _BloodPledge != null;
		}


		// 覆蓋initialize方法，初始化匹配器
		@override
		void initialize() {
			ArrayList<Matcher<L1PcInstance>> matchers = new ArrayList<>(5); // 創建一個大小為5的ArrayList用於存儲匹配器

			// 檢查並添加各種前提條件的匹配器到列表中
			if (hasLevel()) {
				matchers.add(level()); // 如果有等級前提條件，添加到列表中
			}
			if (hasCharacterClass()) {
				matchers.add(characterClass()); // 如果有職業前提條件，添加到列表中
			}
			if (hasQuest()) {
				matchers.add(quest()); // 如果有任務前提條件，添加到列表中
			}
			if (hasRegion()) {
				matchers.add(region()); // 如果有地區前提條件，添加到列表中
			}
			if (hasBloodPledge()) {
				matchers.add(bloodPledge()); // 如果有血盟前提條件，添加到列表中
			}

			// 判斷匹配器列表是否為空
			if (matchers.isEmpty()) {
				matcher = Matchers.all(); // 如果列表為空，將匹配器設置為匹配所有條件
			} else {
				matcher = Matchers.composite(matchers, true); // 否則，將匹配器設置為匹配列表中的所有條件
			}
		}

		@override
		public boolean matches(L1PcInstance t) {
			return matcher.matches(t); // 使用內部匹配器來檢查給定的L1PcInstance是否符合所有前提條件
		}


		static class MJBeginnerPrerequisiteLevel implements Matcher<L1PcInstance> {
			private int _Minimum;
			private int _Maximum;

			MJBeginnerPrerequisiteLevel() {
				_Minimum = 1;
				_Maximum = 99; // 預設最小和最大等級
			}

			int minimum() {
				return _Minimum; // 獲取最小等級
			}

			int maximum() {
				return _Maximum; // 獲取最大等級
			}

			@override
			public boolean matches(L1PcInstance t) {
				final int level = t.getLevel(); // 獲取角色的等級
				return level >= _Minimum && level <= _Maximum; // 檢查角色等級是否在範圍內
			}
		}

		static class MJBeginnerPrerequisiteClasses implements Matcher<L1PcInstance> {
			private ArrayList<Integer> _Classes;

			List<Integer> classes() {
				return _Classes; // 獲取職業ID列表
			}

			@override
			public boolean matches(L1PcInstance t) {
				int characterClass = MJEClassesType.fromGfx(t.getClassId()).toInt(); // 獲取角色的職業ID
				for (Integer i : classes()) {
					if (characterClass == i.intValue()) { // 檢查職業ID是否匹配
						return true;
					}
				}
				return false; // 如果沒有匹配，返回false
			}
		}

		static class MJBeginnerPrerequisiteRequiredQuest implements Matcher<L1PcInstance> {
			private static final int CONNECTIVE_AND = 1; // 表示“與”的連接
			private static final int CONNECTIVE_OR = 2; // 表示“或”的連接

			private static final int CONDITION_NOT_REVEALED = 1; // 任務未揭示
			private static final int CONDITION_FINISHED = 5; // 任務已完成

			private ArrayList<Integer> _IDs; // 任務ID列表
			private int _Connective; // 連接條件
			private int _Condition; // 任務條件

			MJBeginnerPrerequisiteRequiredQuest() {
				_Connective = CONNECTIVE_AND; // 預設連接條件為“與”
				_Condition = CONDITION_FINISHED; // 預設任務條件為已完成
			}

			List<Integer> ids() {
				return _IDs; // 返回任務ID列表
			}

			boolean connectiveAnd() {
				return _Connective == CONNECTIVE_AND; // 檢查連接條件是否為“與”
			}

			boolean connectiveOr() {
				return _Connective == CONNECTIVE_OR; // 檢查連接條件是否為“或”
			}

			boolean conditionNotRevealed() {
				return _Condition == CONDITION_NOT_REVEALED; // 檢查任務條件是否為未揭示
			}

			boolean conditionFinished() {
				return _Condition == CONDITION_FINISHED; // 檢查任務條件是否為已完成
			}

			@override
			public boolean matches(L1PcInstance t) {
				MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(t);
				if (user == null) {
					return false; // 如果用戶為null，返回false
				}
				return conditionFinished() ?
						user.finished(ids(), connectiveAnd()) : // 檢查用戶是否完成任務
						!user.revealed(ids(), !connectiveAnd()); // 檢查用戶是否未揭示任務
			}
		}

		static class MJBeginnerPrerequisiteRegion implements Matcher<L1PcInstance> {
			private int _OriginalMapID;  // 原始地圖ID
			private int _SX;  // 區域起始X座標
			private int _SY;  // 區域起始Y座標
			private int _EX;  // 區域結束X座標
			private int _EY;  // 區域結束Y座標
			private int _MapID;  // 地圖ID

			// 獲取原始地圖ID
			int originalMapId() {
				return _OriginalMapID;
			}

			// 獲取區域起始X座標
			int startX() {
				return _SX;
			}

			// 獲取區域起始Y座標
			int startY() {
				return _SY;
			}

			// 獲取區域結束X座標
			int endX() {
				return _EX;
			}

			// 獲取區域結束Y座標
			int endY() {
				return _EY;
			}

			// 獲取地圖ID
			int mapId() {
				return _MapID;
			}

			@override
			public boolean matches(L1PcInstance t) {
				final int x = t.getX(); // 獲取角色的X座標
				final int y = t.getY(); // 獲取角色的Y座標
				// 檢查角色所在的地圖ID和座標是否在指定的區域內
				return t.getMapId() == originalMapId() &&
						(x >= startX() && x <= endX()) &&
						(y >= startY() && y <= endY());
			}
		}

		static class MJBeginnerPrerequisiteBloodPledge implements Matcher<L1PcInstance> {
			private static final int STATUS_HAS_BLOOD_PLEDGE = 1; // 有血盟狀態
			private static final int STATUS_NO_BLOOD_PLEDGE = 2; // 無血盟狀態

			private int _Status; // 當前狀態

			// 檢查是否有血盟
			boolean hasBloodPledge() {
				return _Status == STATUS_HAS_BLOOD_PLEDGE;
			}

			// 檢查是否無血盟
			boolean noBloodPledge() {
				return _Status == STATUS_NO_BLOOD_PLEDGE;
			}

			@override
			public boolean matches(L1PcInstance t) {
				boolean bloodPledge = t.getClanid() > 0; // 檢查角色是否有血盟
				return hasBloodPledge() ? bloodPledge : !bloodPledge; // 根據狀態檢查是否匹配
			}
		}

	static class MJBeginnerObjectiveListData extends MJBeginnerData {
		private ArrayList<MJBeginnerObjective> _Objective; // 目標列表

		// 獲取目標列表
		List<MJBeginnerObjective> objective() {
			return _Objective;
		}

		// 根據ID查找目標
		MJBeginnerObjective find(int objectiveId) {
			for (MJBeginnerObjective o : _Objective) {
				if (o.id() == objectiveId) {
					return o;
				}
			}
			return null; // 如果未找到，返回null
		}
	}

		static class MJBeginnerObjective {
			private int _ID; // 目標ID
			private int _Type; // 目標類型
			private int _AssetID; // 資產ID
			private int _RequiredQuantity; // 所需數量
			private int _SubType; // 子類型
			private String _HyperText; // 超文本
			private int _ExtraDesc; // 額外描述

			// 獲取目標ID
			int id() {
				return _ID;
			}

			// 獲取目標類型
			MJBeginnerObjectiveTypeData objectiveType() {
				return MJBeginnerObjectiveTypeData.fromInt(_Type);
			}

			// 獲取資產ID
			int assetId() {
				return _AssetID;
			}

			// 獲取所需數量
			int requiredQuantity() {
				return _RequiredQuantity;
			}

			// 獲取子類型
			int subType() {
				return _SubType;
			}

			// 獲取超文本
			String hyperText() {
				return _HyperText;
			}

			// 獲取額外描述
			int extraDesc() {
				return _ExtraDesc;
			}
		}

		static class MJBeginnerRewardListData extends MJBeginnerData {
			private ArrayList<MJBeginnerReward> _Reward; // 獎勵列表

			// 獲取獎勵列表
			List<MJBeginnerReward> rewards() {
				return _Reward;
			}

			// 處理所有獎勵
			void onReward(final L1PcInstance pc) {
				for (MJBeginnerReward reward : rewards()) {
					reward.onReward(pc); // 為給定的L1PcInstance發放獎勵
				}
			}

			@override
			void initialize() {
			}
		}

		static class MJBeginnerReward {
			private int _Type; // 獎勵類型
			private int _AssetID; // 資產ID
			private long _Amount; // 獎勵數量
			private boolean _HighlightReward; // 是否高亮顯示獎勵
			private int _WorldNumber; // 世界編號
			private int _X; // 獎勵位置X座標
			private int _Y; // 獎勵位置Y座標
			private int _Direction; // 方向
			private int _BuffDuration; // Buff持續時間
			private int _BuffGroupIndex; // Buff組索引
			private String _StatueHtml; // 紀念碑HTML內容
			private String _StatueObjectName; // 紀念碑對象名稱

			// 獲取獎勵類型
			MJBeginnerRewardTypeData rewardType() {
				return MJBeginnerRewardTypeData.fromInt(_Type);
			}

			// 獲取獎勵類型ID
			int eType() {
				return _Type;
			}

			// 獲取資產ID
			int assetId() {
				return _AssetID;
			}

			// 獲取獎勵數量
			long amount() {
				return _Amount;
			}

			// 是否高亮顯示獎勵
			boolean highlightReward() {
				return _HighlightReward;
			}

			// 獲取世界編號
			int worldNumber() {
				return _WorldNumber;
			}

			// 獲取獎勵位置的X座標
			int x() {
				return _X;
			}

			// 獲取獎勵位置的Y座標
			int y() {
				return _Y;
			}

			// 獲取方向
			int direction() {
				return _Direction;
			}

			// 獲取Buff持續時間
			int buffDuration() {
				return _BuffDuration;
			}

			// 獲取Buff組索引
			int buffGroupIndex() {
				return _BuffGroupIndex;
			}

			// 獲取紀念碑HTML內容
			String statueHtml() {
				return _StatueHtml;
			}

			// 獲取紀念碑對象名稱
			String statueObjectName() {
				return _StatueObjectName;
			}

			// 發放獎勵方法
			void onReward(L1PcInstance pc) {
				MJBeginnerRewardProvider.provider()
						.invoker(rewardType())
						.invoke(pc, this);
			}
		}

		static class MJBeginnerOptionalRewardListData extends MJBeginnerRewardListData {
			private int _Count; // 獎勵數量

			// 獲取獎勵數量
			public int count() {
				return _Count;
			}
		}

		static class MJBeginnerTeleportData extends MJBeginnerData {
			private int _Cost; // 傳送消耗
			private int _MapID; // 地圖ID
			private int _X; // X座標
			private int _Y; // Y座標
			private boolean _NoTeleport; // 是否禁止傳送

			// 獲取傳送消耗
			int cost() {
				return _Cost;
			}

			// 獲取地圖ID
			int mapId() {
				return _MapID;
			}

			// 獲取X座標
			int x() {
				return _X;
			}

			// 獲取Y座標
			int y() {
				return _Y;
			}

			// 是否禁止傳送
			boolean noTeleport() {
				return _NoTeleport;
			}

			@override
			void initialize() {
			}

			// 執行傳送方法
			boolean onTeleport(L1PcInstance pc) {
				pc.set_MassTel(true);
				pc.start_teleport(x(), y(), mapId(), pc.getHeading(), 18339, true, true);
				// pc.sendPackets(new S_PacketBox(S_PacketBox.攻擊範圍, pc, pc.getWeapon()), true);
				return true;
			}
		}

		static enum MJBeginnerObjectiveTypeData {
			KILL_NPC(1),
			COLLECT_ITEM(2),
			REACH_LEVEL(3),
			TUTORIAL_BLOOD_PLEDGE_JOIN(4),
			TUTORIAL_USE_ITEM(5),
			DESTROY_NOVICE_SIEGE_DOOR(6),
			DESTROY_NOVICE_SIEGE_TOWER(7),
			TUTORIAL_ENCHANT_MAX(8),
			TUTORIAL_BLOOD_PLEDGE_CREATE(9),
			QUEST_REVEAL(10),
			VIEW_DIALOGUE(11),
			START_PSS(12),
			TUTORIAL_OPEN_UI(13);

			private int value;

			// 枚舉構造函數
			MJBeginnerObjectiveTypeData(int val) {
				value = val;
			}

			// 返回整數值
			int toInt() {
				return value;
			}

			// 比較函數
			boolean equals(MJBeginnerObjectiveTypeData v) {
				return value == v.value;
			}

			// 從整數值獲取枚舉實例
			static MJBeginnerObjectiveTypeData fromInt(int i) {
				switch(i) {
					case 1:
						return KILL_NPC;
					case 2:
						return COLLECT_ITEM;
					case 3:
						return REACH_LEVEL;
					case 4:
						return TUTORIAL_BLOOD_PLEDGE_JOIN;
					case 5:
						return TUTORIAL_USE_ITEM;
					case 6:
						return DESTROY_NOVICE_SIEGE_DOOR;
					case 7:
						return DESTROY_NOVICE_SIEGE_TOWER;
					case 8:
						return TUTORIAL_ENCHANT_MAX;
					case 9:
						return TUTORIAL_BLOOD_PLEDGE_CREATE;
					case 10:
						return QUEST_REVEAL;
					case 11:
						return VIEW_DIALOGUE;
					case 12:
						return START_PSS;
					case 13:
						return TUTORIAL_OPEN_UI;
					default:
						System.out.println(String.format("無效的 MJ新手目標類型數據 : %d", i));
						return null;
				}
			}
		}

		static enum MJBeginnerRewardTypeData {
			ITEM(1),
			EXP(2),
			SPELL_BUFF(3),
			AUTO_USE_ITEM(4),
			BUFF_STATUE(5);

			private int value;

			// 枚举构造函数
			MJBeginnerRewardTypeData(int val) {
				value = val;
			}

			// 返回整数值
			int toInt() {
				return value;
			}

			// 比较函数
			boolean equals(MJBeginnerRewardTypeData v) {
				return value == v.value;
			}

			// 从整数值获取枚举实例
			static MJBeginnerRewardTypeData fromInt(int i) {
				switch(i) {
					case 1:
						return ITEM;
					case 2:
						return EXP;
					case 3:
						return SPELL_BUFF;
					case 4:
						return AUTO_USE_ITEM;
					case 5:
						return BUFF_STATUE;
					default:
						System.out.println("無效的 MJ新手獎勵類型數據： " + i);
						return null;
				}
			}
		}

package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerQuestData extends MJBeginnerData {
	private int _ID;
	private boolean _AutoActive;
	private boolean _Obsolete;
	private MJBeginnerDescriptionData _Description;
	private MJBeginnerPrerequisiteData _Prerequisite;
	private MJBeginnerObjectiveListData _ObjectiveList;
	private MJBeginnerRewardListData _AdvanceRewardList;
	private MJBeginnerRewardListData _RewardList;
	private MJBeginnerOptionalRewardListData _OptionalRewardList;
	private MJBeginnerTeleportData _Teleport;

	int id() {
		return _ID;
	}

	boolean autoActive() {
		return _AutoActive;
	}

	boolean obsolete() {
		return _Obsolete;
	}

	MJBeginnerDescriptionData description() {
		return _Description;
	}

	boolean hasDescription() {
		return _Description != null;
	}

	MJBeginnerPrerequisiteData prerequisite() {
		return _Prerequisite;
	}

	boolean hasPrerequisite() {
		return _Prerequisite != null;
	}

	MJBeginnerObjectiveListData objectiveList() {
		return _ObjectiveList;
	}

	boolean hasObjectiveList() {
		return _ObjectiveList != null;
	}

	MJBeginnerRewardListData advanceRewardList() {
		return _AdvanceRewardList;
	}

	boolean hasAdvanceRewardList() {
		return _AdvanceRewardList != null;
	}

	MJBeginnerRewardListData rewardList() {
		return _RewardList;
	}

	boolean hasRewardList() {
		return _RewardList != null;
	}

	MJBeginnerOptionalRewardListData optionalRewardList() {
		return _OptionalRewardList;
	}

	boolean hasOptionalRewardList() {
		return _OptionalRewardList != null;
	}

	MJBeginnerTeleportData teleport() {
		return _Teleport;
	}

	boolean hasTeleport() {
		return _Teleport != null;
	}

	@Override
	void initialize() {
		if (hasDescription()) {
			_Description.initialize();
		}
		if (hasPrerequisite()) {
			_Prerequisite.initialize();
		}
		if (hasObjectiveList()) {
			_ObjectiveList.initialize();
		}
		if (hasAdvanceRewardList()) {
			_AdvanceRewardList.initialize();
		}
		if (hasRewardList()) {
			_RewardList.initialize();
		}
		if (hasOptionalRewardList()) {
			_OptionalRewardList.initialize();
		}
		if (hasTeleport()) {
			_Teleport.initialize();
		}
	}

	static class MJBeginnerDescriptionData extends MJBeginnerData {

		@Override
		void initialize() {
		}
	}

	static class MJBeginnerPrerequisiteData extends MJBeginnerData implements Matcher<L1PcInstance> {
		private MJBeginnerPrerequisiteLevel _Level;
		private MJBeginnerPrerequisiteClasses _CharacterClass;
		private MJBeginnerPrerequisiteRequiredQuest _Quest;
		private MJBeginnerPrerequisiteRegion _Region;
		private MJBeginnerPrerequisiteBloodPledge _BloodPledge;
		private Matcher<L1PcInstance> matcher;

		MJBeginnerPrerequisiteLevel level() {
			return _Level;
		}

		boolean hasLevel() {
			return _Level != null;
		}

		MJBeginnerPrerequisiteClasses characterClass() {
			return _CharacterClass;
		}

		boolean hasCharacterClass() {
			return _CharacterClass != null;
		}

		MJBeginnerPrerequisiteRequiredQuest quest() {
			return _Quest;
		}

		boolean hasQuest() {
			return _Quest != null;
		}

		MJBeginnerPrerequisiteRegion region() {
			return _Region;
		}

		boolean hasRegion() {
			return _Region != null;
		}

		MJBeginnerPrerequisiteBloodPledge bloodPledge() {
			return _BloodPledge;
		}

		boolean hasBloodPledge() {
			return _BloodPledge != null;
		}

		@Override
		void initialize() {
			ArrayList<Matcher<L1PcInstance>> matchers = new ArrayList<>(5);
			if (hasLevel()) {
				matchers.add(level());
			}
			if (hasCharacterClass()) {
				matchers.add(characterClass());
			}
			if (hasQuest()) {
				matchers.add(quest());
			}
			if (hasRegion()) {
				matchers.add(region());
			}
			if (hasBloodPledge()) {
				matchers.add(bloodPledge());
			}
			if (matchers.isEmpty()) {
				matcher = Matchers.all();
			} else {
				matcher = Matchers.composite(matchers, true);
			}
		}

		@Override
		public boolean matches(L1PcInstance t) {
			return matcher.matches(t);
		}

		static class MJBeginnerPrerequisiteLevel implements Matcher<L1PcInstance> {
			private int _Minimum;
			private int _Maximum;

			MJBeginnerPrerequisiteLevel() {
				_Minimum = 1;
				_Maximum = 99;
			}

			int minimum() {
				return _Minimum;
			}

			int maximum() {
				return _Maximum;
			}

			@Override
			public boolean matches(L1PcInstance t) {
				final int level = t.getLevel();
				return level >= _Minimum && level <= _Maximum;
			}
		}

		static class MJBeginnerPrerequisiteClasses implements Matcher<L1PcInstance> {
			private ArrayList<Integer> _Classes;

			List<Integer> classes() {
				return _Classes;
			}

			@Override
			public boolean matches(L1PcInstance t) {
				int characterClass = MJEClassesType.fromGfx(t.getClassId()).toInt();
				for (Integer i : classes()) {
					if (characterClass == i.intValue()) {
						return true;
					}
				}
				return false;
			}
		}

		static class MJBeginnerPrerequisiteRequiredQuest implements Matcher<L1PcInstance> {
			private static final int CONNECTIVE_AND = 1;
			private static final int CONNECTIVE_OR = 2;

			private static final int CONDITION_NOT_REVEALED = 1;
			private static final int CONDITION_FINISHED = 5;

			private ArrayList<Integer> _IDs;
			private int _Connective;
			private int _Condition;

			MJBeginnerPrerequisiteRequiredQuest() {
				_Connective = CONNECTIVE_AND;
				_Condition = CONDITION_FINISHED;
			}

			List<Integer> ids() {
				return _IDs;
			}

			boolean connectiveAnd() {
				return _Connective == CONNECTIVE_AND;
			}

			boolean connectiveOr() {
				return _Connective == CONNECTIVE_OR;
			}

			boolean conditionNotRevealed() {
				return _Condition == CONDITION_NOT_REVEALED;
			}

			boolean conditionFinished() {
				return _Condition == CONDITION_FINISHED;
			}

			@Override
			public boolean matches(L1PcInstance t) {
				MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(t);
				if (user == null) {
					return false;
				}
				return conditionFinished() ? user.finished(ids(), connectiveAnd())
						: !user.revealed(ids(), !connectiveAnd());
			}
		}

		static class MJBeginnerPrerequisiteRegion implements Matcher<L1PcInstance> {
			private int _OriginalMapID;
			private int _SX;
			private int _SY;
			private int _EX;
			private int _EY;
			private int _MapID;

			int originalMapId() {
				return _OriginalMapID;
			}

			int startX() {
				return _SX;
			}

			int startY() {
				return _SY;
			}

			int endX() {
				return _EX;
			}

			int endY() {
				return _EY;
			}

			int mapId() {
				return _MapID;
			}

			@Override
			public boolean matches(L1PcInstance t) {
				final int x = t.getX();
				final int y = t.getY();
				return t.getMapId() == originalMapId() &&
						(x >= startX() && x <= endX()) &&
						(y >= startY() && y <= endY());
			}
		}

		static class MJBeginnerPrerequisiteBloodPledge implements Matcher<L1PcInstance> {
			private static final int STATUS_HAS_BLOOD_PLEDGE = 1;
			private static final int STATUS_NO_BLOOD_PLEDGE = 2;

			private int _Status;

			boolean hasBloodPledge() {
				return _Status == STATUS_HAS_BLOOD_PLEDGE;
			}

			boolean noBloodPledge() {
				return _Status == STATUS_NO_BLOOD_PLEDGE;
			}

			@Override
			public boolean matches(L1PcInstance t) {
				boolean bloodPledge = t.getClanid() > 0;
				return hasBloodPledge() ? bloodPledge : !bloodPledge;
			}
		}
	}

	static class MJBeginnerObjectiveListData extends MJBeginnerData {
		private ArrayList<MJBeginnerObjective> _Objective;

		List<MJBeginnerObjective> objective() {
			return _Objective;
		}

		MJBeginnerObjective find(int objectiveId) {
			for (MJBeginnerObjective o : _Objective) {
				if (o.id() == objectiveId) {
					return o;
				}
			}
			return null;
		}

		@Override
		void initialize() {
		}

		static class MJBeginnerObjective {
			private int _ID;
			private int _Type;
			private int _AssetID;
			private int _RequiredQuantity;
			private int _SubType;
			private String _HyperText;
			private int _ExtraDesc;

			int id() {
				return _ID;
			}

			MJBeginnerObjectiveTypeData objectiveType() {
				return MJBeginnerObjectiveTypeData.fromInt(_Type);
			}

			int assetId() {
				return _AssetID;
			}

			int requiredQuantity() {
				return _RequiredQuantity;
			}

			int subType() {
				return _SubType;
			}

			String hyperText() {
				return _HyperText;
			}

			int extraDesc() {
				return _ExtraDesc;
			}

		}
	}

	static class MJBeginnerRewardListData extends MJBeginnerData {
		private ArrayList<MJBeginnerReward> _Reward;

		List<MJBeginnerReward> rewards() {
			return _Reward;
		}

		void onReward(final L1PcInstance pc) {
			for (MJBeginnerReward reward : rewards()) {
				reward.onReward(pc);
			}
		}

		@Override
		void initialize() {
		}

		static class MJBeginnerReward {
			private int _Type;
			private int _AssetID;
			private long _Amount;
			private boolean _HighlightReward;
			private int _WorldNumber;
			private int _X;
			private int _Y;
			private int _Direction;
			private int _BuffDuration;
			private int _BuffGroupIndex;
			private String _StatueHtml;
			private String _StatueObjectName;

			MJBeginnerRewardTypeData rewardType() {
				return MJBeginnerRewardTypeData.fromInt(_Type);
			}

			int eType() {
				return _Type;
			}

			int assetId() {
				return _AssetID;
			}

			long amount() {
				return _Amount;
			}

			boolean highlightReward() {
				return _HighlightReward;
			}

			int worldNumber() {
				return _WorldNumber;
			}

			int x() {
				return _X;
			}

			int y() {
				return _Y;
			}

			int direction() {
				return _Direction;
			}

			int buffDuration() {
				return _BuffDuration;
			}

			int buffGroupIndex() {
				return _BuffGroupIndex;
			}

			String statueHtml() {
				return _StatueHtml;
			}

			String statueObjectName() {
				return _StatueObjectName;
			}

			void onReward(L1PcInstance pc) {
				MJBeginnerRewardProvider.provider()
						.invoker(rewardType())
						.invoke(pc, this);
			}
		}
	}

	static class MJBeginnerOptionalRewardListData extends MJBeginnerRewardListData {
		private int _Count;

		public int count() {
			return _Count;
		}
	}

	static class MJBeginnerTeleportData extends MJBeginnerData {
		private int _Cost;
		private int _MapID;
		private int _X;
		private int _Y;
		private boolean _NoTeleport;

		int cost() {
			return _Cost;
		}

		int mapId() {
			return _MapID;
		}

		int x() {
			return _X;
		}

		int y() {
			return _Y;
		}

		boolean noTeleport() {
			return _NoTeleport;
		}

		@Override
		void initialize() {
		}

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
		TURTORIAL_OPEN_UI(13);

		private int value;

		MJBeginnerObjectiveTypeData(int val) {
			value = val;
		}

		int toInt() {
			return value;
		}

		boolean equals(MJBeginnerObjectiveTypeData v) {
			return value == v.value;
		}

		static MJBeginnerObjectiveTypeData fromInt(int i) {
			switch (i) {
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
					return TURTORIAL_OPEN_UI;
				default:
					System.out.println(String.format("無效的 MJBeginnerObjectiveTypeData：%d", i));
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

		MJBeginnerRewardTypeData(int val) {
			value = val;
		}

		int toInt() {
			return value;
		}

		boolean equals(MJBeginnerRewardTypeData v) {
			return value == v.value;
		}

		static MJBeginnerRewardTypeData fromInt(int i) {
			switch (i) {
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
					System.out.println("無效的 MJBeginnerRewardTypeData ： " + i);
					return null;
			}
		}
	}
}

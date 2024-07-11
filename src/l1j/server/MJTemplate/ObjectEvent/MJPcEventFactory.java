package l1j.server.MJTemplate.ObjectEvent;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJPcEventFactory {
	MJPcEventFactory() {
	}


	/**
	 * 當任務完成時
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcQuestFinishedArgs>> pcQuestFinishedKey() {
		return pcQuestFinishedKey; // 返回任務完成的鍵
	}

	// 觸發任務完成事件
	public void fireQuestFinished(L1PcInstance pc, int questId) {
		pc.eventHandler().fire(pcQuestFinishedKey(), new MJPcQuestFinishedArgs(pc, questId));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJPcQuestFinishedArgs>> pcQuestFinishedKey = MJAttrKey.newInstance("mj-pc-quest-finished"); // 初始化任務完成的鍵

	// 任務完成事件參數的內部類
	public static class MJPcQuestFinishedArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例
		public final int questId; // 任務ID

		// 私有構造函數
		private MJPcQuestFinishedArgs(L1PcInstance pc, int questId) {
			this.pc = pc; // 初始化角色實例
			this.questId = questId; // 初始化任務ID
		}
	}


	/**
	 * 當等級變動時
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcLevelChangedArgs>> pcLevelChangedKey() {
		return pcLevelChangedKey; // 返回等級變動的鍵
	}

	// 觸發等級變動事件
	public void fireLevelChanged(L1PcInstance pc) {
		pc.eventHandler().fire(pcLevelChangedKey(), new MJPcLevelChangedArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJPcLevelChangedArgs>> pcLevelChangedKey = MJAttrKey.newInstance("mj-pc-level-changed"); // 初始化等級變動的鍵

	// 等級變動事件參數的內部類
	public static class MJPcLevelChangedArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例

		// 私有構造函數
		private MJPcLevelChangedArgs(L1PcInstance pc) {
			super(); // 調用父類的構造函數
			this.pc = pc; // 初始化角色實例
		}
	}


	/**
	 * 當血盟變動時
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcPledgeChangedArgs>> pcPledgeChangedKey() {
		return pcPledgeChangedKey; // 返回血盟變動的鍵
	}

	// 觸發血盟變動事件
	public void firePledgeChanged(L1PcInstance pc, String currentPledge, String previousPledge) {
		pc.eventHandler().fire(pcPledgeChangedKey(), new MJPcPledgeChangedArgs(pc, currentPledge, previousPledge));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJPcPledgeChangedArgs>> pcPledgeChangedKey = MJAttrKey.newInstance("mj-pc-pledge-changed"); // 初始化血盟變動的鍵

	// 血盟變動事件參數的內部類
	public static class MJPcPledgeChangedArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例
		public final String currentPledge; // 當前血盟
		public final String previousPledge; // 前一個血盟

		// 私有構造函數
		private MJPcPledgeChangedArgs(L1PcInstance pc, String currentPledge, String previousPledge) {
			super(); // 調用父類的構造函數
			this.pc = pc; // 初始化角色實例
			this.currentPledge = currentPledge; // 初始化當前血盟
			this.previousPledge = previousPledge; // 初始化前一個血盟
		}
	}


	/**
	 * 當 PSS 開始時
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcPssStartedArgs>> pcPssStartedKey() {
		return pcPssStartedKey; // 返回 PSS 開始的鍵
	}

	// 觸發 PSS 開始事件
	public void firePssStarted(L1PcInstance pc) {
		pc.eventHandler().fire(pcPssStartedKey(), new MJPcPssStartedArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJPcPssStartedArgs>> pcPssStartedKey = MJAttrKey.newInstance("mj-pc-pss-started"); // 初始化 PSS 開始的鍵

	// PSS 開始事件參數的內部類
	public static class MJPcPssStartedArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例

		// 私有構造函數
		private MJPcPssStartedArgs(L1PcInstance pc) {
			this.pc = pc; // 初始化角色實例
		}
	}


	/**
	 * 當圖鑑新增時
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcCPMWBQAddedKeyArgs>> pcCPMWBQAddedKey() {
//        System.out.println("確認是否進來 2");
		return pcCPMWBQAddedKey; // 返回圖鑑新增的鍵
	}

	// 觸發圖鑑新增事件
	public void fireCPMWBQAdded(L1PcInstance pc) {
//        System.out.println("確認是否進來 4");
		pc.eventHandler().fire(pcCPMWBQAddedKey(), new MJpcCPMWBQAddedKeyArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJpcCPMWBQAddedKeyArgs>> pcCPMWBQAddedKey = MJAttrKey.newInstance("mj-pc-CPMWBQ-added"); // 初始化圖鑑新增的鍵

	// 圖鑑新增事件參數的內部類
	public static class MJpcCPMWBQAddedKeyArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例

		// 私有構造函數
		private MJpcCPMWBQAddedKeyArgs(L1PcInstance pc) {
//            System.out.println("確認是否進來 3");
			this.pc = pc; // 初始化角色實例
		}
	}

	/**
	 * 當合成製作開啟時
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcCraftOpenKeyArgs>> pcCraftOpenKey() {
//        System.out.println("確認是否進來 2");
		return pcCraftOpenKey; // 返回合成製作開啟的鍵
	}

	// 觸發合成製作開啟事件
	public void fireCraftOpen(L1PcInstance pc) {
//        System.out.println("確認是否進來 4");
		pc.eventHandler().fire(pcCraftOpenKey(), new MJpcCraftOpenKeyArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJpcCraftOpenKeyArgs>> pcCraftOpenKey = MJAttrKey.newInstance("mj-pc-Craft-Open"); // 初始化合成製作開啟的鍵

	// 合成製作開啟事件參數的內部類
	public static class MJpcCraftOpenKeyArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例

		// 私有構造函數
		private MJpcCraftOpenKeyArgs(L1PcInstance pc) {
//            System.out.println("確認是否進來 3");
			this.pc = pc; // 初始化角色實例
		}
	}

	/**
	 * 潛力
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcMagicDollKeyArgs>> pcMagicDollKey() {
//        System.out.println("確認是否進來 2");
		return pcMagicDollKey; // 返回潛力（魔法玩偶）的鍵
	}

	// 觸發潛力（魔法玩偶）事件
	public void fireMagicDoll(L1PcInstance pc) {
//        System.out.println("確認是否進來 4");
		pc.eventHandler().fire(pcMagicDollKey(), new MJpcMagicDollKeyArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJpcMagicDollKeyArgs>> pcMagicDollKey = MJAttrKey.newInstance("mj-pc-Magic-Doll"); // 初始化潛力（魔法玩偶）的鍵

	// 潛力（魔法玩偶）事件參數的內部類
	public static class MJpcMagicDollKeyArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例

		// 私有構造函數
		private MJpcMagicDollKeyArgs(L1PcInstance pc) {
//            System.out.println("確認是否進來 3");
			this.pc = pc; // 初始化角色實例
		}
	}

	/**
	 * 副本
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcIndunKeyArgs>> pcIndunKey() {
//        System.out.println("確認是否進來 2");
		return pcIndunKey; // 返回副本的鍵
	}

	// 觸發副本事件
	public void fireIndun(L1PcInstance pc) {
//        System.out.println("確認是否進來 4");
		pc.eventHandler().fire(pcIndunKey(), new MJpcIndunKeyArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJpcIndunKeyArgs>> pcIndunKey = MJAttrKey.newInstance("mj-pc-Indun"); // 初始化副本的鍵

	// 副本事件參數的內部類
	public static class MJpcIndunKeyArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色實例

		// 私有構造函數
		private MJpcIndunKeyArgs(L1PcInstance pc) {
//            System.out.println("確認是否進來 3");
			this.pc = pc; // 初始化角色實例
		}
	}

/**
 * 艾因哈萨德点数
 **/
/*
public MJAttrKey<MJObjectEventComposite<MJpcEinhasadKeyArgs>> pcEinhasadKey() {
//        System.out.println("确认是否进来 2");
return pcEinhasadKey; // 返回艾因哈萨德点数的键
}

// 触发艾因哈萨德点数事件
public void fireEinhasad(L1PcInstance pc) {
//        System.out.println("确认是否进来 4");
if (pcEinhasadKey() == null) {
    // 处理pcEinhasadKey为空的情况
} else if (pcEinhasadKey() != null) {
    pc.eventHandler().fire(pcEinhasadKey(), new MJpcEinhasadKeyArgs(pc));
}
}

private static final MJAttrKey<MJObjectEventComposite<MJpcEinhasadKeyArgs>> pcEinhasadKey = MJAttrKey.newInstance("mj-pc-Einhasad"); // 初始化艾因哈萨德点数的键

// 艾因哈萨德点数事件参数的内部类
public static class MJpcEinhasadKeyArgs extends MJObjectEventArgs {
public final L1PcInstance pc; // 角色实例

// 私有构造函数
private MJpcEinhasadKeyArgs(L1PcInstance pc) {
//            System.out.println("确认是否进来 3");
this.pc = pc; // 初始化角色实例
}
}
*/

	/**
	 * 隐藏地牢
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcHiddenDungeonKeyArgs>> pcHiddenDungeonKey() {
//        System.out.println("确认是否进来 2");
		return pcHiddenDungeonKey; // 返回隐藏地牢的键
	}

	// 触发隐藏地牢事件
	public void fireHiddenDungeon(L1PcInstance pc) {
//        System.out.println("确认是否进来 4");
		pc.eventHandler().fire(pcHiddenDungeonKey(), new MJpcHiddenDungeonKeyArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJpcHiddenDungeonKeyArgs>> pcHiddenDungeonKey = MJAttrKey.newInstance("mj-pc-HiddenDungeon"); // 初始化隐藏地牢的键

	// 隐藏地牢事件参数的内部类
	public static class MJpcHiddenDungeonKeyArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色实例

		// 私有构造函数
		private MJpcHiddenDungeonKeyArgs(L1PcInstance pc) {
//            System.out.println("确认是否进来 3");
			this.pc = pc; // 初始化角色实例
		}
	}

	/**
	 * 与莫里亚对话
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcMoriaKeyArgs>> pcMoriaKey() {
//        System.out.println("确认是否进来 2");
		return pcMoriaKey; // 返回与莫里亚对话的键
	}

	// 触发与莫里亚对话事件
	public void fireMoria(L1PcInstance pc) {
//        System.out.println("确认是否进来 4");
		pc.eventHandler().fire(pcMoriaKey(), new MJpcMoriaKeyArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJpcMoriaKeyArgs>> pcMoriaKey = MJAttrKey.newInstance("mj-pc-Moria"); // 初始化与莫里亚对话的键

	// 与莫里亚对话事件参数的内部类
	public static class MJpcMoriaKeyArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色实例

		// 私有构造函数
		private MJpcMoriaKeyArgs(L1PcInstance pc) {
//            System.out.println("确认是否进来 3");
			this.pc = pc; // 初始化角色实例
		}
	}

	/**
	 * 与巴尔加对话
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcBargaKeyArgs>> pcBargaKey() {
//        System.out.println("确认是否进来 2");
		return pcBargaKey; // 返回与巴尔加对话的键
	}

	// 触发与巴尔加对话事件
	public void fireBarga(L1PcInstance pc) {
//        System.out.println("确认是否进来 4");
		pc.eventHandler().fire(pcBargaKey(), new MJpcBargaKeyArgs(pc));
	}

	private static final MJAttrKey<MJObjectEventComposite<MJpcBargaKeyArgs>> pcBargaKey = MJAttrKey.newInstance("mj-pc-Barga"); // 初始化与巴尔加对话的键

	// 与巴尔加对话事件参数的内部类
	public static class MJpcBargaKeyArgs extends MJObjectEventArgs {
		public final L1PcInstance pc; // 角色实例

		// 私有构造函数
		private MJpcBargaKeyArgs(L1PcInstance pc) {
//            System.out.println("确认是否进来 3");
			this.pc = pc; // 初始化角色实例
		}
	}
}


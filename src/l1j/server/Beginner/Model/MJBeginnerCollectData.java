package l1j.server.Beginner.Model;

/*### 改進建議

	*	1. **方法命名和訪問修飾符**：確保所有方法都使用 `public` 訪問修飾符，並且方法名稱符合 Java 的命名慣例。
	*	2. **邏輯清晰度**：確保邏輯清晰，特別是在方法 `selectProbability` 和 `collectItemsCount` 中，這些方法的邏輯應該明確且易於理解。
	*	3. **代碼格式**：保持代碼格式一致，這樣代碼更具可讀性。

		### 完整的繁體中文版本*/

		import java.util.ArrayList;
		import java.util.HashMap;

		// 初學者收集數據類
public class MJBeginnerCollectData {
	// 來自 NPC 對話的收集數據
	private HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks;
	// 來自怪物的收集數據
	private HashMap<Integer, MJBeginnerCollectDataFromMonsters> collectsFromMonsters;

	// 構造函數
	public MJBeginnerCollectData(HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks, HashMap<Integer, MJBeginnerCollectDataFromMonsters> collectsFromMonsters) {
		this.collectsFromNpcTalks = collectsFromNpcTalks;
		this.collectsFromMonsters = collectsFromMonsters;
	}

	// 檢查是否有來自 NPC 對話的收集數據
	public boolean hasCollectsFromNpcTalks() {
		return collectsFromNpcTalks != null;
	}

	// 獲取來自 NPC 對話的收集數據
	public HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> getCollectsFromNpcTalks() {
		return collectsFromNpcTalks;
	}

	// 檢查是否有來自怪物的收集數據
	public boolean hasCollectsFromMonsters() {
		return collectsFromMonsters != null;
	}

	// 獲取來自怪物的收集數據
	public HashMap<Integer, MJBeginnerCollectDataFromMonsters> getCollectsFromMonsters() {
		return collectsFromMonsters;
	}

	// 靜態內部類
	static class MJBeginnerCollectDataFromNpcTalk implements Matcher<Integer> {
		private int itemAssetId;
		private int talkingNpc;
		private int suppliesCount;

		// 獲取物品資產ID
		public int getItemAssetId() {
			return itemAssetId;
		}

		// 獲取對話的NPC
		public int getTalkingNpc() {
			return talkingNpc;
		}

		// 獲取供應數量
		public int getSuppliesCount() {
			return suppliesCount;
		}

		@override
		public boolean matches(Integer i) {
			return i.intValue() == getTalkingNpc();
		}
	}

	// 靜態內部類
	static class MJBeginnerCollectDataFromMonsters implements Matcher<Integer> {
		private int itemAssetId;
		private ArrayList<Integer> suppliersNpc;
		private double probability;
		private int suppliesMinimum;
		private int suppliesMaximum;

		// 獲取物品資產ID
		public int getItemAssetId() {
			return itemAssetId;
		}

		// 獲取供應的NPC列表
		public ArrayList<Integer> getSuppliersNpc() {
			return suppliersNpc;
		}

		// 獲取概率
		public double getProbability() {
			return probability;
		}

		// 獲取最小供應數量
		public int getSuppliesMinimum() {
			return suppliesMinimum;
		}

		// 獲取最大供應數量
		public int getSuppliesMaximum() {
			return suppliesMaximum;
		}

		@override
		public boolean matches(Integer i) {
			for (Integer supplierNpcId : getSuppliersNpc()) {
				if (i.intValue() == supplierNpcId.intValue()) {
					return true;
				}
			}
			return false;
		}

		// 檢查是否贏得概率
		public boolean selectProbability() {
			return getProbability() >= 1D || MJRnd.isWinning(1000000, (int)(getProbability() * 1000000));
		}

		// 獲取收集物品的數量
		public int collectItemsCount() {
			return getSuppliesMinimum() == getSuppliesMaximum() ?
					getSuppliesMinimum() : MJRnd.next(getSuppliesMinimum(), getSuppliesMaximum());
		}
	}
}

package l1j.server.MJWebServer.Dispatcher.my.service.rank;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJRankSystem.Loader.MJRankLoadManager;

public class MJMyRankService {
	private static final MJMyRankService service = new MJMyRankService();
	private static final int COUNT_PER_PAGE = 20;

	public static MJMyRankService service() {
		return service;
	}

	private ArrayList<ArrayList<MJMyRankServiceModel>> models;
	private long lastUpdateMillis;

	private MJMyRankService() {
		lastUpdateMillis = System.currentTimeMillis();
	}

	public void onNewRank(ArrayList<ArrayList<MJMyRankServiceModel>> models) {
		this.models = models;
		this.lastUpdateMillis = System.currentTimeMillis();
	}

	public ArrayList<ArrayList<MJMyRankServiceModel>> allocateModels() {
		//TODO 랭킹 클래스에 변화가 있다면 수정할것
		ArrayList<ArrayList<MJMyRankServiceModel>> models = new ArrayList<>(11);
		for (int i = 9; i >= 0; --i) {
			models.add(new ArrayList<>(MJRankLoadManager.MRK_SYS_CLASS_RANGE));
		}
		models.add(new ArrayList<>(MJRankLoadManager.MRK_SYS_TOTAL_RANGE));
		return models;
	}

	public long lastUpdateMillis() {
		return lastUpdateMillis;
	}

	public List<MJMyRankServiceModel> selectModels(int offset, int classId) {
		if (!MJRankLoadManager.MRK_SYS_ISON || models == null) {
			return null;
		}
		List<MJMyRankServiceModel> classModels = models.get(classId);
		int endIndex = offset + COUNT_PER_PAGE;
		endIndex = Math.min(classModels.size(), endIndex);
		return classModels.subList(offset, endIndex);
	}
}

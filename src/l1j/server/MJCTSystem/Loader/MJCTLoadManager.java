package l1j.server.MJCTSystem.Loader;

import java.util.ArrayDeque;

import l1j.server.MJCTSystem.MJCTObject;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

/**
 * MJCTCharInfo
 * MJSoft Character TradeSystem - Load Manager
 * CTSystem managed class.
 * made by mjsoft, 2016.
 **/
public class MJCTLoadManager {
	public static final int CTSYSTEM_STORE_ID = 3000468;
	public static final int CTSYSTEM_LOAD_ID = 3000469;

	private static MJCTLoadManager _instance;

	public static MJCTLoadManager getInstance() {
		if (_instance == null)
			_instance = new MJCTLoadManager();
		return _instance;
	}

	private static ArrayDeque<String> parseToStringArray(String s) {
		String[] arr = s.split(" ");
		int size = arr.length;
		ArrayDeque<String> argsQ = new ArrayDeque<String>(size);
		for (int i = 0; i < size; i++) {
			try {
				argsQ.offer(arr[i]);
			} catch (Exception e) {
				break;
			}
		}
		return argsQ;
	}

	private static final S_SystemMessage _basicMenus = new S_SystemMessage(
			".정보查詢 1~3 角色名" +
					"[1. 角色資訊] [2. 背包] [3. 技能]" // +
	// "[角色名稱]"
	);

	public static void commands(L1PcInstance pc, String param) {
		try {
			ArrayDeque<String> argsQ = parseToStringArray(param);
			if (argsQ == null || argsQ.isEmpty())
				throw new Exception("");

			int cmd = Integer.parseInt(argsQ.poll());

			switch (cmd) {
				case 1:
					infoAskCommands(pc, argsQ.poll());
					break;
				case 2:
					inventoryAskCommands(pc, argsQ.poll());
					break;
				case 3:
					skillsAskCommands(pc, argsQ.poll());
					break;
				default:
					throw new Exception("");
			}

		} catch (Exception e) {
			pc.sendPackets(_basicMenus, false);
		}
	}

	public static final S_SystemMessage _invalidCharacter = new S_SystemMessage("無法找到角色資訊。");

	public static void infoAskCommands(L1PcInstance pc, String cName) {
		MJCTObject obj = MJCTSystemLoader.getInstance().get(cName);
		if (obj == null) {
			pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
			return;
		}

		if (obj.infoPck != null)
			pc.sendPackets(obj.infoPck, false);
	}

	public static void inventoryAskCommands(L1PcInstance pc, String cName) {
		MJCTObject obj = MJCTSystemLoader.getInstance().get(cName);
		if (obj == null) {
			pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
			return;
		}

		if (obj.invPck != null)
			pc.sendPackets(obj.invPck, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, false);
	}

	public static void skillsAskCommands(L1PcInstance pc, String cName) {
		MJCTObject obj = MJCTSystemLoader.getInstance().get(cName);
		if (obj == null) {
			pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
			return;
		}

		if (obj.spPck != null)
			pc.sendPackets(obj.spPck, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, false);
	}

	private MJCTLoadManager() {
	}

	public void load() {
		MJCTSpellLoader.getInstance();
		MJCTSystemLoader.getInstance();
	}

	public void release() {
		MJCTSpellLoader.release();
		MJCTSystemLoader.release();
	}
}

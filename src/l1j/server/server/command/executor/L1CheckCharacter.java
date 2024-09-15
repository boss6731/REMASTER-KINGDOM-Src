package l1j.server.server.server.command.executor;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvService;
import l1j.server.server.model.L1Object;
import l1j.server.server.server.model.Instance.L1PcInstance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class L1CheckCharacter implements l1j.server.server.command.executor.L1CommandExecutor {
	private L1CheckCharacter() {
	}

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1CheckCharacter();
	}

	private CharacterCheckerModel characterModel(final String characterName) {
		final CharacterCheckerModel model = new CharacterCheckerModel();
		Selector.exec("SELECT objid, account_name FROM characters WHERE char_name=? limit 1", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, characterName);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					model.objectId = rs.getInt("objid");
					model.accountName = rs.getString("account_name");
				}
			}
		});
		return model;
	}


	@Override
	public void execute(L1Object pc, String cmdName, String arg) {

	}

	private static class CharacterCheckerModel {
		public int objectId;
		public String accountName;
	}

	private static class MJMyCharInvItemCompare implements Comparator<MJMyCharInvItemInfo> {
		@Override
		public int compare(MJMyCharInvItemInfo o1, MJMyCharInvItemInfo o2) {
			if (o1.equipped) {
				return -1;
			}
			if (o2.equipped) {
				return 1;
			}
			if (o1.enchantLevel > o2.enchantLevel) {
				return -1;
			}
			if (o2.enchantLevel > o1.enchantLevel) {
				return 1;
			}
			if (o1.itemId < o2.itemId) {
				return -1;
			}
			if (o2.itemId < o1.itemId) {
				return 1;
			}
			return 0;
		}
	}

	private void onCheckedCharacter(final L1PcInstance gm, final String characterName, final String type) {
		final CharacterCheckerModel model = characterModel(characterName);
		if (model.objectId <= 0) {
			gm.sendPackets(String.format("\fW** [%s] 是不存在的角色。 **", characterName));
			return;
		}
		gm.sendPackets(String.format("\fW** 檢查: %s 角色: %s **", type, characterName));
		try {
			switch (type) {
				case "背包": {
					List<MJMyCharInvItemInfo> items = MJMyCharInvService.service().allInventoryItems(model.objectId);
					onDisplayCheckedCharacter(gm, characterName, type, items);
					break;
				}
				case "倉庫": {
					List<MJMyCharInvItemInfo> items = MJMyCharInvService.service().accountWarehouseItems(model.accountName);
					onDisplayCheckedCharacter(gm, characterName, type, items);
					break;
				}
				case "裝備": {
					List<MJMyCharInvItemInfo> items = MJMyCharInvService.service().equippedItems(model.objectId);
					onDisplayCheckedCharacter(gm, characterName, type, items);
					break;
				}
				case "妖精倉庫": {
					List<MJMyCharInvItemInfo> items = MJMyCharInvService.service().elfWarehouseItems(model.accountName);
					onDisplayCheckedCharacter(gm, characterName, type, items);
					break;
				}
				case "附加倉庫": {
				List<MJMyCharInvItemInfo> items = MJMyCharInvService.service()
						.characterWarehouseItems(model.accountName);
				onDisplayCheckedCharacter(gm, characterName, type, items);
				break;
			}
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			gm.sendPackets(".檢查 [角色名稱] [裝備,背包,倉庫,妖精倉庫]");
		}
	}

	private void onDisplayCheckedCharacter(final L1PcInstance gm, final String characterName, final String type,
			final List<MJMyCharInvItemInfo> items) {
		int number = 0;
		items.sort(new MJMyCharInvItemCompare());
		for (MJMyCharInvItemInfo iInfo : items) {
			gm.sendPackets(String.format("\\fU%d. %s", ++number, iInfo.display));
		}
		gm.sendPackets(String.format("\fW** 總共找到 %d 件物品 [%s] **", items.size(), type));
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {

			StringTokenizer st = new StringTokenizer(arg);
			String characterName = st.nextToken();
			String type = st.nextToken();
			onCheckedCharacter(pc, characterName, type);
		} catch (Exception e) {
			pc.sendPackets(".檢查 [角色名稱] [裝備,背包,倉庫,妖精倉庫,附加倉庫]");
		}
	}
}

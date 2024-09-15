package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_GMHtml;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

import java.util.StringTokenizer;

public class L1Present implements L1CommandExecutor {

	private L1Present() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Present();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			String nameid = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
				target = CharacterTable.getInstance().restoreCharacter(name);

				if (target == null) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("不存在的角色。")));
					return;
				}

				CharacterTable.getInstance().restoreInventory(target);
			}

			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int itemid = 0;
			int Attrenchant = 0;
			if (st.hasMoreTokens()) {
				Attrenchant = Integer.parseInt(st.nextToken());
			}
			int bless = 0;
			if (st.hasMoreTokens()) {
				bless = Integer.parseInt(st.nextToken());
			}
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
				if (itemid == 0) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("未找到該物品。")));
					return;
				}
			}
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				if (temp.isStackable()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setEnchantLevel(0);
					item.setCount(count);
					if (target.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						target.getInventory().storeItem(item);
						// target.sendPackets(new S_GMHtml("禮物: " + target.getName() + "" , "" + item.getLogName() + " 獲得，請檢查您的背包。"));
						target.sendPackets(new S_GMHtml("梅蒂斯", "管理者", item.getName(), String.format("%,d個", item.getCount())).toString());
						target.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 4856)));
						pc.sendPackets("\\aD[" + target.getName() + "] " + item.getLogName() + " (ID:" + itemid + ") 已發送");
					}
				} else {
					L1ItemInstance item = null;
					int createCount;
					for (createCount = 0; createCount < count; createCount++) {
						item = ItemTable.getInstance().createItem(itemid);
						item.setEnchantLevel(enchant);
						item.setAttrEnchantLevel(Attrenchant);
						if (bless == 129) {
							item.setBless(bless);
						}
						target.getInventory().storeItem(item);
						if (bless == 129) {
							item.setBless(bless);
							target.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
							target.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
						}
					}
					if (createCount > 0) {
						// target.sendPackets(new S_GMHtml("禮物:" + target.getName() + "" , "" + item.getLogName() + "(" + count + ") 獲得。請檢查您的背包"));
						target.sendPackets(new S_GMHtml("梅蒂斯", "管理者", L1ItemInstance.to_simple_description(item), String.format("%,d個", item.getCount())).toString());
						target.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 4856)));
						pc.sendPackets(String.valueOf(new S_SystemMessage("\\aD[" + target.getName() + "] +" + enchant + " " + temp.getNameId() + "(ID:" + itemid + ") " + count + "個 已發送", true)));
					}
				}
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("指定ID的物品不存在。")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(".[禮物] [角色] [物品ID] [數量] [強化] [屬性] [封印129]")));
		}
	}
}
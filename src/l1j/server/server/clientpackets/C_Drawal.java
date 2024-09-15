package l1j.server.server.server.clientpackets;

import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;

import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;


public class C_Drawal extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_DRAWAL = "[C] C_Drawal";

	public C_Drawal(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		try {
			@SuppressWarnings("unused")
			int i = readD();
			int j = readD();

			L1PcInstance pc = clientthread.getActiveChar();
			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) {
					if (MJCastleWarBusiness.getInstance().isNowWar(clan.getCastleId())) {
						S_SystemMessage sm = new S_SystemMessage("攻城期間無法收取稅金。");
						pc.sendPackets(sm, true);
						return;
					}
					if (pc.getClanRank() != L1Clan.MONARCH || !pc.isCrown() || pc.getId() != pc.getClan().getLeaderId())
						return;

					MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
					int money = war.getPublicMoney();
					long _money = money;
					if (_money <= 0 || money < j) {//버그방지
						return;
					}
					money -= j;
					L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
					
					if (item != null) {
						war.setPublicMoney(money);
						MJCastleWarBusiness.getInstance().updateCastle(castle_id);
						if (pc.getInventory().checkAddItem(item, j) == L1Inventory.OK) {
							pc.getInventory().storeItem(L1ItemId.ADENA, j);
						} else {
							L1World.getInstance().getInventory(pc.getX(), pc.getY(),pc.getMapId()).storeItem(L1ItemId.ADENA, j);
						}
						pc.sendPackets(String.valueOf(new S_ServerMessage(143, "$457",String.format("$4(%d)", j))));
					}
				}
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_DRAWAL;
	}
}

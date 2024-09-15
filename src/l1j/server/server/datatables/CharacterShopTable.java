package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.Base64;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.utils.SQLUtil;

public class CharacterShopTable {

	private static CharacterShopTable _instance;

	public static CharacterShopTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterShopTable();
		}
		return _instance;
	}

	private CharacterShopTable() {
		load();
	}

	private L1PcInstance load2(String charName, byte[] shopTitle) {
		L1PcInstance pc = null;
		try {
			pc = L1PcInstance.load(charName);
			int currentHpAtLoad = (int) pc.getCurrentHp();
			int currentMpAtLoad = pc.getCurrentMp();

			pc.setOnlineStatus(1);
			l1j.server.server.datatables.CharacterTable.updateOnlineStatus(pc);
			L1World.getInstance().storeObject(pc);
			items(pc);
			L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());
			int tile = map.getTile(pc.getX(), pc.getY());
			if (Config.ServerAdSetting.GETBACKREST || !map.isInMap(pc.getX(), pc.getY()) || tile == 0 || tile == 4 || tile == 12) {
				int[] loc = Getback.GetBack_Location(pc, true);
				pc.setX(loc[0]);
				pc.setY(loc[1]);
				pc.setMap((short) loc[2]);
			}
			L1World.getInstance().addVisibleObject(pc);
			pc.beginGameTimeCarrier();
			pc.sendVisualEffectAtLogin(); // 顯示毒、水中、城堡主等的視覺效果

			pc.setSpeedHackCount(0);
			pc.getLight().turnOnOffLight();
			if (pc.getCurrentHp() > 0) {
				pc.setDead(false);
				pc.setStatus(0);
			} else {
				pc.setDead(true);
				pc.setStatus(ActionCodes.ACTION_Die);
			}
			MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
			if (pc.getClanid() != 0) { // 隸屬於血盟中
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null) {
					if (pc.getClanid() == clan.getClanId() && // 血盟解散後，再次創建同名血盟時的對策
							pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {

					} else {
						pc.setClanid(0);
						pc.setClanname("");
						pc.setClanRank(0);
						pc.save(); // 將角色信息寫入資料庫
					}
				}
			}
			if (currentHpAtLoad > pc.getCurrentHp()) {
				pc.setCurrentHp(currentHpAtLoad);
			}
			if (currentMpAtLoad > pc.getCurrentMp()) {
				pc.setCurrentMp(currentMpAtLoad);
			}
			pc.setNetConnection(null);

			pc.save(); // 將角色信息寫入資料庫
			byte[] text = shopTitle;
			pc.setShopChat(text);
			pc.setPrivateShop(true);
			Broadcaster.broadcastPacket(pc, new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, text));

		} catch (Exception e) {
		}
		return pc;
	}

	private void items(L1PcInstance pc) {
		// 從資料庫讀取角色和倉庫的物品
		l1j.server.server.datatables.CharacterTable.getInstance().restoreInventory(pc);
	}

	/** 2016.11.24 MJ 應用中心市價 **/
	private <FastTable> void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String name = null;
		FastTable _charlist = new FastTable<String>();
		L1PcInstance pc;
		int i = 0;
		try {
			System.out.print("角色商店表 加載中...");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_shop_store");
			rs = pstm.executeQuery();
			while (rs.next()) {
				name = rs.getString("char_Name");
				if (!_charlist.contains(name)) {
					pc = load2(name, Base64.decode(rs.getString("shop_Title")));
					_charlist.add(name);
					if (pc == null)
						continue;
					ShopListLoad(pc);
					i += 1;
				}
			}
			deleteShop();
			System.out.println("OK! Count: " + i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			_charlist.clear();
			pc = null;
			i = 0;
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/** 2016.11.24 MJ 應用中心市價 **/
	private void ShopListLoad(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		MJDShopItem ditem = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_shop WHERE objid=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			while (rs.next()) {
				ditem = MJDShopItem.create(rs);
				if (ditem.isPurchase)
					pc.addPurchasings(ditem);
				else
					pc.addSellings(ditem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void deleteShop() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_shop");
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}

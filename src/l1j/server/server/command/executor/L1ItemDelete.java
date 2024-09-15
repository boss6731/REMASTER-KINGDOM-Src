package l1j.server.server.server.command.executor;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Warehouse.Warehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class L1ItemDelete implements L1CommandExecutor {

	private L1ItemDelete() {	}

	public static L1CommandExecutor getInstance() {
		return new L1ItemDelete();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String type = tok.nextToken();				
			WarehouseManager wareHouse = WarehouseManager.getInstance();
			if (type.equalsIgnoreCase("全部")) {
				try {	
					int itemId = Integer.parseInt(tok.nextToken());
					ArrayList<String> accounts = loadAccount();
					String name;
					for(int i = 0; i < accounts.size(); i++) {
						name = accounts.get(i);
						deleteWareHouse(wareHouse, name, itemId); // 刪除倉庫
					}

					for(L1PcInstance players : L1World.getInstance().getAllPlayers()) {
						players.getInventory().consumeItem(itemId);
					}
					Warehouse _clanWarehouse;
					L1ItemInstance clanItem;
					for(L1Clan clans : L1World.getInstance().getAllClans()) {
						_clanWarehouse = wareHouse.getClanItems(clans.getClanName());
						clanItem = _clanWarehouse.findItemId(itemId);
						if(clanItem != null) {
							_clanWarehouse.deleteItem(clanItem);
						}
					}
				}catch(Exception e) {
					pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 [全部] [道具編號]。")));
				}
			}else if (type.equalsIgnoreCase("角色")) {
				try {
					String name = tok.nextToken();					
					int itemId = Integer.parseInt(tok.nextToken());
					
					L1PcInstance target = L1World.getInstance().getPlayer(name);
					if(target == null) {
						pc.sendPackets(String.valueOf(new S_SystemMessage(target + "目前未在線上。")));
						return;
					}
					String accountName = target.getAccountName();					
					deleteWareHouse(wareHouse, accountName, itemId);
					
					loadAccountCharacter(accountName, itemId);
					
					L1PcInstance player = L1World.getInstance().getPlayer(name);
					if(player != null) {
						player.getInventory().consumeItem(itemId);
					}
				}catch(Exception e) {
					pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 [角色] [角色名稱] [道具編號]")));
				}
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("\\aD" + cmdName + " 請輸入 [全部] [道具編號]")));
			pc.sendPackets(String.valueOf(new S_SystemMessage("\\aD" + cmdName + " 請輸入 [角色] [角色名稱] [道具編號]")));
		}
	}

	private void deleteWareHouse(WarehouseManager wareHouse, String accountName, int itemId) {
		// 刪除帳號倉庫物品
		Warehouse _privateWarehouse = null;
		Warehouse _elfWarehouse = null;				
		Warehouse _supplementaryService = null;

		L1ItemInstance itemPrivate;
		L1ItemInstance itemElf;
		L1ItemInstance itemSupple;				

		_privateWarehouse = wareHouse.getPrivateItems(accountName);
		_elfWarehouse =  wareHouse.getElfItems(accountName);
		_supplementaryService =  wareHouse.getSupplementaryItems(accountName);					
		itemPrivate = _privateWarehouse.findItemId(itemId);
		itemElf = _elfWarehouse.findItemId(itemId);
		itemSupple = _supplementaryService.findItemId(itemId);	
		if(itemPrivate != null) {
			_privateWarehouse.deleteItem(itemPrivate);
		}
		if(itemElf != null) {
			_elfWarehouse.deleteItem(itemElf);
		}
		if(itemSupple != null) {
			_supplementaryService.deleteItem(itemSupple);
		}
	}
	
	private ArrayList<String> loadAccountCharacter(String accountName, int itemId) {
		ArrayList<String> _list = new ArrayList<String>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name = ? AND OnlineStatus = 0");
			pstm.setString(1, accountName);
			rs = pstm.executeQuery();
			while(rs.next()) {
				deleteCharacterDB(rs.getInt("objid"), itemId );
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
		return _list;
	}
	
	private void deleteCharacterDB(int char_id, int itemId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE  FROM character_items WHERE char_id = ? AND item_id = ? ");
			pstm.setInt(1, char_id);
			pstm.setInt(2, itemId);
			pstm.execute();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private ArrayList<String> loadAccount() {
		ArrayList<String> _list = new ArrayList<String>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM accounts");
			rs = pstm.executeQuery();
			while(rs.next()) {
				_list.add(rs.getString("login"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
		return _list;
	}
}

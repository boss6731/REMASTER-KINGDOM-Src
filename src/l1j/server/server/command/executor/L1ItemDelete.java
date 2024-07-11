package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.Warehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1ItemDelete implements L1CommandExecutor  {

	private L1ItemDelete() {	}

	public static L1CommandExecutor getInstance() {
		return new L1ItemDelete();
	}

    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer tok = new StringTokenizer(arg);
            String type = tok.nextToken(); // 獲取命令類型
            WarehouseManager wareHouse = WarehouseManager.getInstance(); // 獲取倉庫管理器實例

            if (type.equalsIgnoreCase("全部")) { // 如果命令類型是 "전체"（全部）
                try {
                    int itemId = Integer.parseInt(tok.nextToken()); // 獲取並解析物品ID
                    ArrayList<String> accounts = loadAccount(); // 加載所有帳戶
                    String name;

// 刪除所有帳戶中的指定物品
                    for (int i = 0; i < accounts.size(); i++) {
                        name = accounts.get(i);
                        deleteWareHouse(wareHouse, name, itemId); // 刪除帳戶倉庫中的物品
                    }

// 刪除所有在線玩家的指定物品
                    for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
                        player.getInventory().consumeItem(itemId);
                    }

// 刪除所有血盟倉庫中的指定物品
                    Warehouse _clanWarehouse;
                    L1ItemInstance clanItem;
                    for (L1Clan clan : L1World.getInstance().getAllClans()) {
                        _clanWarehouse = wareHouse.getClanItems(clan.getClanName());
                        clanItem = _clanWarehouse.findItemId(itemId);
                        if (clanItem != null) {
                            _clanWarehouse.deleteItem(clanItem);
                        }
                    }
                } catch (Exception e) {
                    pc.sendPackets(new S_SystemMessage(cmdName + " 請輸入 [全部] [物品編號]."));
                }
            } else if (type.equalsIgnoreCase("角色")) { // 如果命令類型是 "케릭"（角色）
                try {
                    String name = tok.nextToken(); // 獲取角色名稱
                    int itemId = Integer.parseInt(tok.nextToken()); // 獲取並解析物品ID

                    L1PcInstance target = L1World.getInstance().getPlayer(name);
                    if (target == null) {
                        pc.sendPackets(new S_SystemMessage(name + "您目前未在線上。"));
                        return;
                    }
                    String accountName = target.getAccountName();
                    deleteWareHouse(wareHouse, accountName, itemId);

                    loadAccountCharacter(accountName, itemId);

                    L1PcInstance player = L1World.getInstance().getPlayer(name);
                    if (player != null) {
                        player.getInventory().consumeItem(itemId);
                    }
                } catch (Exception e) {
                    pc.sendPackets(new S_SystemMessage(cmdName + " 請輸入 [角色] [角色名稱] [物品編號]"));
                }
            }
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("\aD" + cmdName + " 請輸入[全部][物品編號]。"));

            pc.sendPackets(new S_SystemMessage("\aD" + cmdName + " 請輸入[角色][角色名稱][物品編號]"));
        }
    }

	private void deleteWareHouse(WarehouseManager wareHouse, String accountName, int itemId) {
		//계정창고 아이템 삭제..
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

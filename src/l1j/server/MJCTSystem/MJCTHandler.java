package l1j.server.MJCTSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCTSystem.Loader.MJCTSystemLoader;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.C_CommonClick;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
/** 
 * MJCTCharInfo
 * MJSoft Character TradeSystem - Handler
 * made by mjsoft, 2016.
 **/
public class MJCTHandler {
	public static boolean load(L1PcInstance pc, L1ItemInstance item) {
		if (!pc.getMap().isSafetyZone(pc.getLocation())) {
			pc.sendPackets(new S_SystemMessage("只能在村莊內使用。"));
			return false;
		}

		MJCTObject obj = MJCTSystemLoader.getInstance().remove(item.getId());
		if (obj == null) {
			pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
			return false;
		}

		Account acc = pc.getAccount();
		if (acc.countCharacters() > 8 || acc.getCharSlot() - acc.countCharacters() <= 0) {
			MJCTSystemLoader.getInstance().reset(obj);
			pc.sendPackets(new S_SystemMessage("角色槽位不足。"));
			return false;
		}

		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String charName = isValidCharacter(con, "[MJCTSYSTEM]", obj.charId);
			if (charName == null) {
				pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
				return false;
			}

			updateAccount(con, pc.getAccountName(), obj.charId);
			obj.dispose();
			pc.getInventory().removeItem(item);
			updateCharacterStatus(pc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}

		return true;
	}

	public static boolean store(L1PcInstance pc, L1ItemInstance item, int objid) {
		if (pc.getId() == objid) {
			pc.sendPackets(new S_SystemMessage("不能對自己使用。"));
			return false;
		}

		if (!pc.getMap().isSafetyZone(pc.getLocation())) {
			pc.sendPackets(new S_SystemMessage("只能在村莊內使用。"));
			return false;
		}

		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String charName = isValidCharacter(con, pc.getAccountName(), objid);
			if (charName == null) {
				pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
				return false;
			}

			updateAccount(con, "[MJCTSYSTEM]", objid);
			pc.setTamTime(null);
			L1PcInstance.tamdel(objid);
			pc.getInventory().removeItem(item, 1);
			L1ItemInstance item2 = pc.getInventory().storeItem(MJCTLoadManager.CTSYSTEM_LOAD_ID, 1, true);

			MJCTObject obj = new MJCTObject();
			obj.charId = objid;
			obj.marbleId = item2.getId();
			obj.name = charName;
			MJCTSystemLoader.getInstance().set(obj);
			updateCharacterStatus(pc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
		return true;
	}

	public static void updateCharacterStatus(L1PcInstance pc){
		GameClient clnt = pc.getNetConnection();
		C_NewCharSelect.restartProcess(pc);
		Account acc		= clnt.getAccount();
		clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
		if(acc.countCharacters() > 0)
			C_CommonClick.sendCharPacks(clnt);
	}
	
	private static void updateAccount(Connection con, String dst_account, int objid){
		PreparedStatement pstm	= null;
		try{
			pstm	= con.prepareStatement("update characters set account_name=?, TamEndTime=null where objid=?");
			pstm.setString(1, dst_account);
			pstm.setInt(2, objid);
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm);
		}
	}
	
	private static String isValidCharacter(Connection con, String account, int objid){
		PreparedStatement 	pstm	= null;
		ResultSet 			rs		= null;
		
		try{
			pstm	= con.prepareStatement("select char_name from characters where account_name=? and objid=?");
			pstm.setString(1, account);
			pstm.setInt(2, objid);
			rs		= pstm.executeQuery();
			if(rs.next())
				return rs.getString("char_name");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
		}
		return null;
	}
}

package l1j.server.MJNetSafeSystem.Distribution;

import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.*;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharPass;

import static l1j.server.server.Opcodes.*;

public class WorldDistributor extends Distributor{

	@Override
	public ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception {
		ClientBasePacket 	cbp	= null;
		L1PcInstance pc = clnt.getActiveChar();
		
		//System.out.printf("[Server] opcode:%d, type:%d, size:%d\r\n%s\r\n", data[0] & 0xff, data[1] & 0xff, data.length, DataToPacket(data, data.length));
		
		switch(op){
		case C_MAIL:
			return new C_MailBox(data, clnt);
		case C_CHANNEL:
			return new C_Report(data, clnt);
		case C_EXTENDED_PROTOBUF:	
			if(MJEProtoMessages.existsProto(clnt, data))
				return null;
			
			cbp = new C_ActionUi(data, clnt);				
			cbp.clear();
			cbp = null;
		
			cbp = new C_Craft(data, clnt);
			
			//return new C_ActionUi(data, clnt);		
			
			return cbp;
		case C_ATTACK: 
		case C_FAR_ATTACK: 
			return new C_Attack(data, clnt);
		case C_DIALOG: 
			return new C_NPCTalk(data, clnt);
		case C_USE_ITEM:
			try {
				C_ItemUSe c_itemuse = new C_ItemUSe(data, clnt);
				c_itemuse.invoke();
				c_itemuse.clear();

				C_ItemUSe2 c_itemuse2 = new C_ItemUSe2(data, clnt);
				return c_itemuse2;
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
			}
			return null;
		case C_VOICE_CHAT: 
			return new C_ReturnStaus(data, clnt);
		case C_PLEDGE_WATCH:
			return new C_ClanAttention(data, clnt);
		case C_SHUTDOWN:
			return new C_PledgeContent(data, clnt);
		case C_ATTACK_CONTINUE:
			return new C_AttackNew(data, clnt);
		case C_EXTENDED:
			return new C_AdenShop(data, clnt);
		case C_SAVEIO:
			return new C_CharcterConfig(data, clnt);
		case C_OPEN: 
			return new C_Door(data, clnt);
		case C_TITLE: 
			return new C_Title(data, clnt);
		case C_EMBLEM:
			return new C_Clan(data, clnt);
		case C_MATCH_MAKING: 
			return new C_ClanMatching(data, clnt);
		case C_BOARD_DELETE: 
			return new C_BoardDelete(data, clnt);
		case C_WHO_PLEDGE:
			return new C_Pledge(data, clnt);
		case C_CHANGE_DIRECTION: 
			return new C_ChangeHeading(data, clnt);
		case C_HACTION:
//            System.out.println("數據包檢查2");
			return new C_NPCAction(data, clnt);
		case C_USE_SPELL:
			return new C_UseSkill(data, clnt);
		case C_UPLOAD_EMBLEM: 
			return new C_Emblem(data, clnt);
		case C_CANCEL_XCHG: 
			return new C_TradeCancel(data, clnt);
		case C_BOOKMARK: 
			return new C_AddBookmark(data, clnt); 
		case C_CREATE_PLEDGE: 
			return new C_CreateClan(data, clnt); 
		case C_MARRIAGE: 
			return new C_Propose(data, clnt); 
		case C_BUYABLE_SPELL: 
			return new C_SkillBuy(data, clnt);
		case C_BOARD_LIST: 
			return new C_BoardBack(data, clnt); 
		case C_PERSONAL_SHOP:
			return new C_Shop(data, clnt); 
		case C_BOARD_READ: 
			return new C_BoardRead(data, clnt); 
		case C_ASK_XCHG: 
			return new C_Trade(data, clnt); 
		case C_ALIVE: 
			return new C_KeepALIVE(data, clnt); 
		case C_ANSWER: 
			return new C_Attr(data, clnt); 
		case C_BUY_SELL:
			cbp = new C_Result(data, clnt);	
			cbp.clear();
			cbp = null;
			cbp = new C_EQCShop(data, clnt);
			return cbp;
		case C_DEPOSIT: 
			return new C_Deposit(data, clnt); 
		case C_WITHDRAW: 
			return new C_Drawal(data, clnt); 
		case C_ONOFF: 
			return new C_LoginToServerOK(data, clnt); 
		case C_BUY_SPELL: 
			return new C_SkillBuyOK(data, clnt); 
		case C_ADD_XCHG:
			return new C_TradeAddItem(data, clnt); 
		case C_ADD_BUDDY: 
			return new C_AddBuddy(data, clnt); 
		case C_LOGOUT: 
			return new C_ReturnToLogin(data, clnt); 
		case C_ACCEPT_XCHG:
			return new C_TradeOK(data, clnt);
		case C_CHECK_PK: 
			return new C_CheckPK(data, clnt); 
		case C_TAX: 
			return new C_TaxRate(data, clnt); 
		case C_RESTART:  
			return new C_NewCharSelect(data, clnt); 
		case C_DEAD_RESTART:  
			return new C_Restart(data, clnt); 
		case C_QUERY_BUDDY: 
			return new C_Buddy(data, clnt); 
		case C_DROP: 
			return new C_DropItem(data, clnt);
		case C_LEAVE_PARTY:
			return new C_LeaveParty(data, clnt); 
		case C_BAN_MEMBER: 
			return new C_BanClan(data, clnt); 
		case C_PLATE: 
			return new C_Board(data, clnt); 
		case C_DESTROY_ITEM:
			return new C_DeleteInventoryItem(data, clnt); 
		case C_WHO_PARTY: 
			return new C_Party(data, clnt); 
		case C_GET:
			return new C_PickUpItem(data, clnt); 
		case C_WHO: 
			return new C_Who(data, clnt); 
		case C_GIVE:
			return new C_GiveItem(data, clnt);
		case C_MOVE: 
			return new C_MoveChar(data, clnt);
		case C_DELETE_BOOKMARK:
			return new C_DeleteBookmark(data, clnt); 	
		case C_LEAVE_PLEDGE: 
			return new C_LeaveClan(data, clnt); 
		case C_BANISH_PARTY: 
			return new C_BanParty(data, clnt); 
		case C_REMOVE_BUDDY: 
			return new C_DelBuddy(data, clnt); 
		case C_WAR: 
			return new C_War(data, clnt); 
		case C_QUERY_PERSONAL_SHOP: 
			return new C_ShopList(data, clnt); 
		case C_JOIN_PLEDGE:
			return new C_JoinClan(data, clnt); 
		case C_ACTION: 
			return new C_ExtraCommand(data, clnt);
		case C_BOARD_WRITE: 
			return new C_BoardWrite(data, clnt);
		case C_INVITE_PARTY_TARGET: 
			return new C_CreateParty(data, clnt); 
		case C_GOTO_PORTAL:
			return new C_EnterPortal(data, clnt);
		case C_HYPERTEXT_INPUT_RESULT: 
			return new C_Amount(data, clnt); 
		case C_FIXABLE_ITEM:
			return new C_FixWeaponList(data, clnt);
		case C_FIX:
			return new C_SelectList(data, clnt);
		case C_EXIT_GHOST:
			return new C_ExitGhost(data, clnt);
		case C_CHANGE_ACCOUNTINFO: 
		case C_SUMMON:
			return new C_CallPlayer(data, clnt); 
		case C_THROW: 
			return new C_FishClick(data, clnt); 
		case C_SLAVE_CONTROL:
			return new C_SelectTarget(data, clnt);
		case C_CHECK_INVENTORY: 
			return new C_PetMenu(data, clnt); 
		case C_NPC_ITEM_CONTROL: 
			return new C_UsePetItem(data, clnt); 
		case C_RETURN_SUMMON: 
			return new C_Teleport(data, clnt); 
		case C_RANK_CONTROL:
			return new C_Rank(data, clnt); 
		case C_CHAT:
			return new C_Chat(data, clnt);
		case C_CHAT_PARTY_CONTROL: 
			return new C_ChatParty(data, clnt);
		case C_DUEL: 
			return new C_Fight(data, clnt);
		case C_WAREHOUSE_CONTROL:
			return new C_WhPw(data, clnt);
		case C_EXCHANGEABLE_SPELL: 
			return new C_Horun(data, clnt); 
		case C_EXCHANGE_SPELL: 
			return new C_HorunOK(data, clnt); 
		case C_QUERY_CASTLE_SECURITY: 
			return new C_SecurityStatus(data, clnt); 
		case C_CHANGE_CASTLE_SECURITY: 
			return new C_SecurityStatusSet(data, clnt); 
		case C_QUIT:
			return new C_Quit(data, clnt);
			
		case Opcodes.C_SHIFT_SERVER:
			return new C_SHIFT_SERVER(data, clnt);
			
		case Opcodes.C_ENTER_WORLD:
			if(clnt.getStatus().toInt() == MJClientStatus.CLNT_STS_CHANGENAME.toInt() && pc != null){
				MJCopyMapObservable.getInstance().resetPosition(pc);
				MJRaidSpace.getInstance().getBackPc(pc);
				C_NewCharSelect.restartProcess(pc);
				
				if(clnt.getAccount().getCPW() != null && !clnt.isLoginRecord()){
					clnt.getAccount().setwaitpacket(data);
					clnt.sendPacket(new S_CharPass(S_CharPass._PASSWORD_INPUT_SCREEN), false);
					return null;
				}
				return new C_LoginToServerWrap(data, clnt);
			}else {
				return null;
			}
		case Opcodes.C_TELEPORT_USER:
			return new C_TELEPORT_USER(data, clnt);
		default:
			break;
		}
		toInvalidOp(clnt, op, data.length, "World", false);
		return null;
	}

	public String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}
			result.append(HexToDex(data[i] & 0xFF, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[(charpoint++)];
					if ((t1 > 31) && (t1 < 128))
						result.append((char) t1);
					else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}
		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[(charpoint++)];
				if ((t1 > 31) && (t1 < 128))
					result.append((char) t1);
				else {
					result.append('.');
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
	
	private String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);
		for (int i = number.length(); i < digits; i++)
			number = "0" + number;
		return number;
	}
}

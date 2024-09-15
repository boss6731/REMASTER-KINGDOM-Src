package l1j.server.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
import l1j.server.MJCombatSystem.MJCombatObserver;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.Chain.Action.MJRestartChain;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;

import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_Unknown2;

public class C_NewCharSelect extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_NEW_CHAR_SELECT = "[C] C_NewCharSelect";
	private static Logger _log = Logger.getLogger(C_NewCharSelect.class.getName());

	//TODO 當按下重生按鈕時進入
	public C_NewCharSelect(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		if (client.getActiveChar() != null) {
			final L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				return;
			}
			//TODO 如果處於絕望狀態，則無法重生
			if (pc.hasSkillEffect(L1SkillId.DESPERADO) || pc.hasSkillEffect(L1SkillId.ETERNITI) || pc.hasSkillEffect(L1SkillId.TEMPEST) || pc.hasSkillEffect(L1SkillId.PHANTOM)) {
				return;
			}

			//TODO 當不使用跨服時
			/*switch((int)pc.getMapId()) {
				case 1708:
				case 1709:
				case 1710:
				case 10500:
				case 10502:
				case 12852:
				case 12853:
				case 12854:
				case 12855:
				case 12856:
				case 12857:
				case 12858:
				case 12859:
				case 12860:
				case 12861:
				case 12862:
					SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, 33423, 32813, 4, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK_RESTART);
					return;
			}*/
			
			if(pc.is_combat_field()){
				MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(pc.get_current_combat_id());
				if(observer != null)
					observer.remove(pc);
			}
			
			MJPoint pt = MJPoint.newInstance(33437, 32813, 10, (short) 4, 50);
			if (pc.getMapId() >= 732 && pc.getMapId() <= 776) {
				pc.setX(pt.x);
				pc.setY(pt.y);
				pc.setMap((short) pt.mapId);
			}
			
			MJCopyMapObservable.getInstance().resetPosition(pc);
			MJRaidSpace.getInstance().getBackPc(pc);

			/* 處理實例空間中的重生 */
			if(MJInstanceSpace.isInInstance(pc)){
				/*if(pc.isDead() && pc.getInstStatus() == InstStatus.INST_USERSTATUS_NONE){
					C_Restart.processRestart(pc);
					return;
				}*/
				if(pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE)
					return;
			}
			C_NewCharSelect.restartProcess(pc);
			/*GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					System.out.println("restart 500");
					C_NewCharSelect.restartProcess(pc);
				}
			}, 500);*/
		} else {
		}
	}
	
	/** MJCTSystem **/
	public static void restartProcess(L1PcInstance pc){
		pc.isWorld = false;

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
			pc.setOnlineStatus(0);
			clan.updateClanMemberOnline(pc);
		}
		_log.fine("Disconnect from: " + pc.getName());
		GameClient client = pc.getNetConnection();
		if(client == null)
			return;

		client.latestRestartMillis(System.currentTimeMillis());

		synchronized (pc) {
			try{
				if(pc.get_is_client_auto()) {
					pc.do_finish_client_auto(eReason.USER_DEAD);
				}
				
				MJIndunRoomController.getInstance().end_user_room(pc, -1);
				/*if(pc.isPrivateShop() && pc.isPrivateReady()){
					UserCommands.privateShop(pc);
					return;
				}*/

				pc.logout();
				client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
				client.latestCharacterInstance(pc);
				client.setActiveChar(null);
				client.sendPacket(new S_Unknown2(1));
				if(client.getAccount().is_changed_slot()){
					int amountOfChars = client.getAccount().countCharacters();
					int slot = client.getAccount().getCharSlot();
					client.sendPacket(new S_CharAmount(amountOfChars, slot));

					C_CommonClick.sendCharPacks(client);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		MJRestartChain.getInstance().on_restarted(pc);
		//重生後在角色窗口中顯示角色
	}

	@Override
	public String getType() {
		return C_NEW_CHAR_SELECT;
	}
}

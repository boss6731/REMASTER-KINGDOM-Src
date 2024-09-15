package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;

public class C_SHIFT_SERVER extends ClientBasePacket{
	public C_SHIFT_SERVER(byte abyte0[], GameClient clnt) {
		super(abyte0);
		
		/*if (clnt.getActiveChar() != null) {
			final L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return;
			}
			//TODO 如果處於絕望狀態則無法重置
			if (pc.hasSkillEffect(L1SkillId.DESPERADO)) {
				return;
			}
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
			
			 instance space 處理重置
			if(MJInstanceSpace.isInInstance(pc)){
				if(pc.isDead() && pc.getInstStatus() == InstStatus.INST_USERSTATUS_NONE){
					C_Restart.processRestart(pc);
					return;
				}
				if(pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE)
					return;
			}
			C_NewCharSelect.restartProcess(pc);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					System.out.println("restart 500");
					C_NewCharSelect.restartProcess(pc);
				}
			}, 500);
		} else {
		}*/
	}
}
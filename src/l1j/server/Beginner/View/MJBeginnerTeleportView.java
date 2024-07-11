package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_TELEPORT_ACK;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerTeleportView implements MJBeginnerView{
	private L1PcInstance pc;
	private int questId;
	MJBeginnerTeleportView(L1PcInstance pc, int questId){
		this.pc = pc;
		this.questId = questId;
	}
	
	public L1PcInstance pc(){
		return pc;
	}
	
	public int questId(){
		return questId;
	}
	
	private void viewInternal(SC_QUEST_TELEPORT_ACK.eResultCode resultCode){
		SC_QUEST_TELEPORT_ACK ack = SC_QUEST_TELEPORT_ACK.newInstance();
		ack.set_id(questId());
		ack.set_result(resultCode);
		pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_TELEPORT_ACK);
	}
	
	public void onSuccess(){
		viewInternal(SC_QUEST_TELEPORT_ACK.eResultCode.SUCCESS);
	}
	
	public void onFail(){
		viewInternal(SC_QUEST_TELEPORT_ACK.eResultCode.FAIL);
	}
	
	public void onNotEnoughAdena(){
		viewInternal(SC_QUEST_TELEPORT_ACK.eResultCode.FAIL_NOT_ENOUGH_ADENA);
	}
	
	public void onWrongLocation(){
		viewInternal(SC_QUEST_TELEPORT_ACK.eResultCode.FAIL_WRONG_LOCATION);
	}
	
	public void onCantTeleportNow(){
		viewInternal(SC_QUEST_TELEPORT_ACK.eResultCode.FAIL_CANT_TELEPORT_NOW);
	}
	
	static class MJBeginnerTeleportDevelopView extends MJBeginnerTeleportView{
		MJBeginnerTeleportDevelopView(L1PcInstance pc, int questId) {
			super(pc, questId);
		}		
		
		@Override
		public void onSuccess(){
			System.out.println(String.format("MJBeginnerTeleportView -> onSuccess. %d", questId()));
			super.onSuccess();
		}
		
		@Override
		public void onFail(){
			System.out.println(String.format("MJBeginnerTeleportView -> onFail. %d", questId()));
			super.onFail();
		}
		
		@Override
		public void onNotEnoughAdena(){
			System.out.println(String.format("MJBeginnerTeleportView -> onFailNotEnoughAdena. %d", questId()));
			super.onNotEnoughAdena();
		}
		
		@Override
		public void onWrongLocation(){
			System.out.println(String.format("MJBeginnerTeleportView -> onWrongLocation. %d", questId()));
			super.onWrongLocation();
		}
		
		@Override
		public void onCantTeleportNow(){
			System.out.println(String.format("MJBeginnerTeleportView -> onCantTeleportNow. %d", questId()));
			super.onCantTeleportNow();
		}
	}
}

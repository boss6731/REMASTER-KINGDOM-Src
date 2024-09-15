package l1j.server.server.model;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_CHANGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI;
import l1j.server.server.RepeatTask;
import l1j.server.server.datatables.EventTimeTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class EventAlramTick extends RepeatTask{
	private final L1PcInstance _pc;
	public EventAlramTick(L1PcInstance pc, long inteval, boolean onOff) {
		super(inteval);
		_pc = pc;
	}
	@Override
	public void execute() {
		try {
//			System.out.println("確認");
			EventTimeTable.getInstance().reload();
			SC_NOTIFICATION_CHANGE_NOTI.reload(_pc);
			_pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(_pc, 0, false));

		} catch (Exception e) {
			
		}
	}
	
	
	
}

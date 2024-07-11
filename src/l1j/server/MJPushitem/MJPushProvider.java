package l1j.server.MJPushitem;

import java.util.LinkedList;

import l1j.server.MJPushitem.dataloader.MJPushChaData;
import l1j.server.MJPushitem.dataloader.MJPushItemData;
import l1j.server.MJPushitem.model.MJChaPushModel;
import l1j.server.MJPushitem.model.MJItemPushModel;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_ITEM_LIST_RECEIVE_REQ;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_LIST_STATUS_DELETE_REQ;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.CS_EVENT_PUSH_LIST_STATUS_READ_REQ;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_ADD_NOTI;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_INFO_LIST_ACK;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_ITEM_LIST_RECEIVE_ACK;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_LIST_STATUS_READ_ACK;
import l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush.SC_EVENT_PUSH_UPDATE_INTO_LIST_ACK;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class MJPushProvider {
	
	private static final MJPushProvider provider = new MJPushProvider();
	public static MJPushProvider provider() {
		return provider;
	}
	
	private MJPushProvider() {
	}
	
	public void messagenew(L1PcInstance pc, MJItemPushModel model) {
		L1Item item = ItemTable.getInstance().getTemplate(model.getItemId());
		if (item == null) {
			return;
		}
		
		MJChaPushModel info = new MJChaPushModel();
		info.setOwnerId(pc.getId());
		info.setPushId(model.getpushid());
		info.setPushstate(0);
		pc.get_push_info().add(info);
		MJPushChaData.getInstance().Update_Info(pc.getId(), info);
		pc.sendPackets(SC_EVENT_PUSH_ADD_NOTI.make_stream(pc, model, item));
	}
	
	public void userLoading(L1PcInstance pc) {
		MJPushChaData data = MJPushChaData.getInstance();
		data.do_Select(pc);
		if(pc.get_push_info() == null || pc.get_push_info().size() <= 0) {
			return;
		}
		
		pc.sendPackets(SC_EVENT_PUSH_INFO_LIST_ACK.make_stream(pc));
	}
	
	public void push_recieve(L1PcInstance pc, CS_EVENT_PUSH_ITEM_LIST_RECEIVE_REQ req) {
		if(req.get_event_push_key_list() == null) {
			return;
		}
		
		if(pc.get_push_info() == null) {
			return;
		}
		
		pc.sendPackets(SC_EVENT_PUSH_ITEM_LIST_RECEIVE_ACK.make_stream(pc, req));
	}
	
	public void push_read(L1PcInstance pc, CS_EVENT_PUSH_LIST_STATUS_READ_REQ req) {
		if(req.get_event_push_key_list() == null) {
			return;
		}
		
		if(pc.get_push_info() == null) {
			return;
		}
		
		pc.sendPackets(SC_EVENT_PUSH_LIST_STATUS_READ_ACK.make_stream(pc, req));
	}
	
	public void delete_push(L1PcInstance pc, CS_EVENT_PUSH_LIST_STATUS_DELETE_REQ req) {
		if(req.get_event_push_key_list() == null) {
			return;
		}
		
		if(pc.get_push_info() == null) {
			return;
		}
		
		pc.sendPackets(SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK.make_stream(pc, req));
	}
	
	public void push_renew(L1PcInstance pc) {
		if(pc.get_push_info() == null) {
			return;
		}
		
		if(pc.get_push_info().size() <= 0) {
			return;
		}
		LinkedList<MJChaPushModel> infocha = new LinkedList<MJChaPushModel>();
		infocha.clear();
		for(MJChaPushModel model : pc.get_push_info()) {
			pc.sendPackets(SC_EVENT_PUSH_UPDATE_INTO_LIST_ACK.make_stream(pc, model.getPushId(), model.getPushId(), model.getPushstate()));
			if(MJPushItemData.getlist().get(model.getPushId()).getExpiredate() - (System.currentTimeMillis() / 1000) <= 0) {
				pc.sendPackets(SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK.make_stream_timover(pc, model.getPushId(), model.getPushId()));
			} else {
				infocha.add(model);
			}
		}
		pc.get_push_info().clear();
		for(int i = 0; i < infocha.size(); i++) {
			pc.get_push_info().add(infocha.get(i));
		}
		infocha.clear();
	}
}

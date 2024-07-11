package l1j.server.MJAutoSystem;

import l1j.server.MJTemplate.Chain.Action.MJITeleportHandler;
import l1j.server.MJTemplate.Chain.Action.MJTeleportChain;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_FORCE_FINISH_PLAY_SUPPORT_NOTI.eReason;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;

public class MJAutoMapTeleportHandler implements MJITeleportHandler{

	private static MJAutoMapTeleportHandler m_instance;
	public static MJAutoMapTeleportHandler getInstance(){
		if(m_instance == null)
			m_instance = new MJAutoMapTeleportHandler();
		return m_instance;
	}

	private MJAutoMapTeleportHandler(){
		MJTeleportChain.getInstance().add_handler(this);
	}
	@Override
	public boolean on_teleported(L1PcInstance owner, int next_x, int next_y, int map_id, int old_mapid) {
		if(!owner.get_is_client_auto())
			return false;
		MJAutoMapInfo mInfo = MJAutoMapInfo.get_map_info(map_id);
		if(mInfo != null) {
			if (mInfo.type_check(owner, owner.get_client_auto_type())) {
				owner.do_finish_client_auto(eReason.INVALID_MAP);
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean is_teleport(L1PcInstance owner, int next_x, int next_y, int map_id) {
		if(map_id == 1501) {
			if(owner.getMapId() != 1501) {
				owner.getAC().addAc(-3);
				owner.sendPackets(new S_OwnCharAttrDef(owner));
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_spell_id(4066);
				noti.set_duration(-1);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
				noti.set_on_icon_id(7235);
				noti.set_off_icon_id(0);
				noti.set_icon_priority(-1);
				noti.set_tooltip_str_id(5402);
				noti.set_new_str_id(5402);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			}
		}else {
			if(owner.getMapId() == 1501) {
				owner.getAC().addAc(3);
				owner.sendPackets(new S_OwnCharAttrDef(owner));
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(4066);
				noti.set_duration(0);
				noti.set_off_icon_id(0);
				noti.set_end_str_id(0);
				noti.set_is_good(true);
				owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
		}
		
		return false;
	}

}

package l1j.server.server.model;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1PcInstance;

public class CastleEffect extends RepeatTask{
	private final L1PcInstance _pc;
	private boolean _onOff;
	private boolean _On = false;
	public CastleEffect(L1PcInstance pc, long inteval, boolean onOff) {
		super(inteval);
		_pc = pc;
		_onOff = onOff;
	}
	@Override
	public void execute() {
		try {
			if (_pc.getMapId()== 13005 || _pc.getMapId() == 13006) {
				SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
				_pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
				return ;
			}
			if (_onOff) {
				if (_On) {
					_On = false;
					SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
					_pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
				} 
				_On = true;
				SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, true);
				_pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, true));
			
			} else {
				SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
				_pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send(_pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
			}

		} catch (Exception e) {
			
		}
	}
	
	
	
}

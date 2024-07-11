package l1j.server.MJRankSystem;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class MJRankBuffLv0 extends MJRankBuff{
	private ProtoOutputStream _onBuffs;
	private ProtoOutputStream _offBuffs;
	
	public MJRankBuffLv0(){

	}
	
	@Override
	public void onBuff(SC_TOP_RANKER_NOTI rnk) {

	}

	@Override
	public void offBuff(SC_TOP_RANKER_NOTI rnk) {

	}
}

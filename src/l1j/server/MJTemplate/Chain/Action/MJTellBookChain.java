package l1j.server.MJTemplate.Chain.Action;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;

public class MJTellBookChain extends MJAbstractActionChain<MJITellBookHandler>{
	private static MJTellBookChain _instance;
	public static MJTellBookChain getInstance(){
		if(_instance == null)
			_instance = new MJTellBookChain();
		return _instance;
	}
	
	private MJTellBookChain(){
		super();
		add_handler(new DayAndNightHandler());
	}
	
	public boolean handle(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance, int skillId, int mapid, int next_x, int next_y){
		for(MJITellBookHandler handler : m_handlers){
			if(handler.is_teleport(pc, itemId, l1iteminstance, skillId, mapid, next_x, next_y))
				return true;
		}
		return false;
	}

	static class DayAndNightHandler implements MJITellBookHandler{
		// 20:00 ~ 05:59
		// 06:00 ~ 19:59
		// 20:00 = 72000
		// 05:00 = 18000 + 3540 = 21540
		// 59 分鐘 = 3540
		// 19 小時 = 68400
		// 50 分鐘 = 3540
		// 19:59 = 68400 + 3540 = 71940
		// 06 小時 = 21600
		@override
		public boolean is_teleport(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance, int skillId, int mapid, int next_x, int next_y) {
			if (mapid == 54) {
				return GameTimeClock.getInstance().is_night() ? true : false;    // 晚上不可
							}/*else if(mapid == 2222) {
				return GameTimeClock.getInstance().is_day() ? true : false; // 白天不可
				}*/
			return false;
		}
	}


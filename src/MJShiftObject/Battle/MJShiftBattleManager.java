package MJShiftObject.Battle;

import java.util.ArrayList;

import MJShiftObject.Battle.DomTower.MJDomTowerPlayManager;
import MJShiftObject.Battle.ForgottenIsland.MJFIslandPlayManager;
import MJShiftObject.Battle.Thebe.MJThebePlayManager;
import MJShiftObject.Template.CommonServerBattleInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftBattleManager implements Runnable{
	public static final int ENTER_TYPE_INTERNAL = 1;
	public static final int ENTER_TYPE_EXTERNAL = 2;
	public static final int ENTER_TYPE_THEBE = 4;
	public static final int ENTER_TYPE_DOMTOWER = 8;
	public static final int ENTER_TYPE_FISLAND = 16;
	
	private int m_enter_type;
	private CommonServerBattleInfo m_battle_info;
	private boolean m_is_cancel;
	private MJShiftBattlePlayManager<?> m_play_manager;
	private MJShiftBattleItemWhiteList m_white_list;
	private int m_loop_count;
	private ArrayList<MJIShiftBattleNotify> m_notifies;
	public MJShiftBattleManager(CommonServerBattleInfo battle_info, MJShiftBattlePlayManager<?> play_manager, MJShiftBattleItemWhiteList white_list){
		m_enter_type = ENTER_TYPE_INTERNAL;
		m_battle_info = battle_info;
		m_is_cancel = false;
		m_play_manager = play_manager;
		if(m_play_manager != null)
			m_loop_count = m_play_manager.next_update_tick();
		m_notifies = new ArrayList<MJIShiftBattleNotify>();
		m_white_list = white_list;
	}
	public MJShiftBattleManager(CommonServerBattleInfo battle_info, int enter_type){
		m_enter_type = ENTER_TYPE_EXTERNAL | enter_type;
		m_battle_info = battle_info;
		m_is_cancel = false;
		m_play_manager = null;
		m_notifies = new ArrayList<MJIShiftBattleNotify>();
		m_white_list = null;
	}
	public void set_cancel_state(boolean is_cancel){
		m_is_cancel = is_cancel;
	}
	
	public String get_battle_server_identity(){
		return m_battle_info.get_server_identity();
	}
	public boolean is_battle_server_running(){
		return m_battle_info.is_run();
	}
	public boolean is_battle_server_ready(){
		return m_battle_info.is_run() && m_play_manager != null && m_play_manager.is_ready();
	}
	public void add_notify(MJIShiftBattleNotify notify){
		m_notifies.add(notify);
	}
	public boolean is_battle_server_thebes(){
		return (m_enter_type & ENTER_TYPE_INTERNAL) == ENTER_TYPE_INTERNAL ?
			m_play_manager != null && m_play_manager instanceof MJThebePlayManager :
			(m_enter_type & ENTER_TYPE_THEBE) == ENTER_TYPE_THEBE;
	}
	public boolean is_battle_server_domtower() {
		return (m_enter_type & ENTER_TYPE_INTERNAL) == ENTER_TYPE_INTERNAL ?
				m_play_manager != null && m_play_manager instanceof MJDomTowerPlayManager :
				(m_enter_type & ENTER_TYPE_DOMTOWER) == ENTER_TYPE_DOMTOWER;
	}
	public boolean is_battle_server_fisland(){
		return (m_enter_type & ENTER_TYPE_INTERNAL) == ENTER_TYPE_INTERNAL ?
				m_play_manager != null && m_play_manager instanceof MJFIslandPlayManager :
				(m_enter_type & ENTER_TYPE_FISLAND) == ENTER_TYPE_FISLAND;
	}
	public void execute(){
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		try{
			while(!m_battle_info.is_ended()){
				if(m_is_cancel)
					break;
				
				do_tick_play_manager();
				do_update_play_manager();
				Thread.sleep(1000);
			}
			for(MJIShiftBattleNotify notify : m_notifies)
				notify.do_ended(m_battle_info);
				
			if(m_play_manager != null){
				m_play_manager.on_closed();
				m_play_manager = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
        return new L1PcInstance[0];
    }
	
	private void do_tick_play_manager(){
		if(m_play_manager != null){
			m_play_manager.on_tick();
		}
	}
	
	private void do_update_play_manager(){
		if(m_play_manager == null || m_loop_count == -1)
			return;
		
		if(--m_loop_count == 0){
			m_loop_count = m_play_manager.next_update_tick();
			GeneralThreadPool.getInstance().execute(new Runnable(){
				@Override
				public void run() {
					m_play_manager.on_update_tick();
                    return new L1PcInstance[0];
                }
			});
		}
	}
	
	public void do_enter_battle_character(L1PcInstance pc){
		if(m_play_manager != null)
			m_play_manager.on_enter(pc);
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item){
		return m_white_list != null ? m_white_list.use(pc, item) : true;
	}
	public void do_reload_whitelist(){
		if(m_white_list != null)
			m_white_list.do_reload();
	}
}

 package MJShiftObject.Battle;

 import MJShiftObject.Template.CommonServerBattleInfo;
 import java.util.ArrayList;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;



 public class MJShiftBattleManager
   implements Runnable
 {
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

   public MJShiftBattleManager(CommonServerBattleInfo battle_info, MJShiftBattlePlayManager<?> play_manager, MJShiftBattleItemWhiteList white_list) {
     this.m_enter_type = 1;
     this.m_battle_info = battle_info;
     this.m_is_cancel = false;
     this.m_play_manager = play_manager;
     if (this.m_play_manager != null)
       this.m_loop_count = this.m_play_manager.next_update_tick();
     this.m_notifies = new ArrayList<>();
     this.m_white_list = white_list;
   }
   public MJShiftBattleManager(CommonServerBattleInfo battle_info, int enter_type) {
     this.m_enter_type = 0x2 | enter_type;
     this.m_battle_info = battle_info;
     this.m_is_cancel = false;
     this.m_play_manager = null;
     this.m_notifies = new ArrayList<>();
     this.m_white_list = null;
   }
   public void set_cancel_state(boolean is_cancel) {
     this.m_is_cancel = is_cancel;
   }

   public String get_battle_server_identity() {
     return this.m_battle_info.get_server_identity();
   }
   public boolean is_battle_server_running() {
     return this.m_battle_info.is_run();
   }
   public boolean is_battle_server_ready() {
     return (this.m_battle_info.is_run() && this.m_play_manager != null && this.m_play_manager.is_ready());
   }
   public void add_notify(MJIShiftBattleNotify notify) {
     this.m_notifies.add(notify);
   }
   public boolean is_battle_server_thebes() {
     return ((this.m_enter_type & 0x1) == 1) ? ((this.m_play_manager != null && this.m_play_manager instanceof MJShiftObject.Battle.Thebe.MJThebePlayManager)) : (((this.m_enter_type & 0x4) == 4));
   }


   public boolean is_battle_server_domtower() {
     return ((this.m_enter_type & 0x1) == 1) ? ((this.m_play_manager != null && this.m_play_manager instanceof MJShiftObject.Battle.DomTower.MJDomTowerPlayManager)) : (((this.m_enter_type & 0x8) == 8));
   }


   public boolean is_battle_server_fisland() {
     return ((this.m_enter_type & 0x1) == 1) ? ((this.m_play_manager != null && this.m_play_manager instanceof MJShiftObject.Battle.ForgottenIsland.MJFIslandPlayManager)) : (((this.m_enter_type & 0x10) == 16));
   }


   public void execute() {
     GeneralThreadPool.getInstance().execute(this);
   }


   public void run() {
     try {
       while (!this.m_battle_info.is_ended() &&
         !this.m_is_cancel) {


         do_tick_play_manager();
         do_update_play_manager();
         Thread.sleep(1000L);
       }
       for (MJIShiftBattleNotify notify : this.m_notifies) {
         notify.do_ended(this.m_battle_info);
       }
       if (this.m_play_manager != null) {
         this.m_play_manager.on_closed();
         this.m_play_manager = null;
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private void do_tick_play_manager() {
     if (this.m_play_manager != null) {
       this.m_play_manager.on_tick();
     }
   }

   private void do_update_play_manager() {
     if (this.m_play_manager == null || this.m_loop_count == -1) {
       return;
     }
     if (--this.m_loop_count == 0) {
       this.m_loop_count = this.m_play_manager.next_update_tick();
       GeneralThreadPool.getInstance().execute(new Runnable()
           {
             public void run() {
               MJShiftBattleManager.this.m_play_manager.on_update_tick();
             }
           });
     }
   }

   public void do_enter_battle_character(L1PcInstance pc) {
     if (this.m_play_manager != null)
       this.m_play_manager.on_enter(pc);
   }

   public boolean use(L1PcInstance pc, L1ItemInstance item) {
     return (this.m_white_list != null) ? this.m_white_list.use(pc, item) : true;
   }
   public void do_reload_whitelist() {
     if (this.m_white_list != null)
       this.m_white_list.do_reload();
   }
 }



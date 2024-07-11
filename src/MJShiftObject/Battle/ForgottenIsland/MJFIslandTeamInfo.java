 package MJShiftObject.Battle.ForgottenIsland;

 import MJShiftObject.Battle.MJShiftBattleTeamInfo;
 import MJShiftObject.MJShiftObjectManager;
 import MJShiftObject.Object.MJShiftObject;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.MJTemplate.Lineage2D.MJRectangle;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class MJFIslandTeamInfo extends MJShiftBattleTeamInfo<MJFIslandCharacterInfo> {
   private static final MJRectangle m_entry_rectangle = MJRectangle.from_radius(32785, 32762, 3, 3, (short)1710);

   public static MJFIslandTeamInfo newInstance(int team_id) {
     return new MJFIslandTeamInfo(team_id);
   }

   private MJFIslandTeamInfo(int teamId) {
     super(teamId);
   }


   public void do_enter(L1PcInstance pc, int rank) {
     MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_object(pc);
     if (sobject == null) {
       return;
     }
     int object_id = pc.getId();
     MJFIslandCharacterInfo cInfo = (MJFIslandCharacterInfo)this.m_players.get(Integer.valueOf(object_id));
     if (cInfo == null) {
       String source_name = MJShiftObjectManager.getInstance().get_source_character_name(pc);
       if (MJString.isNullOrEmpty(source_name))
         source_name = pc.getName();
       cInfo = MJFIslandCharacterInfo.newInstance(pc, object_id, source_name, this);
       this.server_description = cInfo.home_server_name;
       this.m_players.put(Integer.valueOf(object_id), cInfo);
     } else {
       cInfo.owner = pc;
     }
     pc.set_battle_info(cInfo);
     MJPoint pt = to_rand_location();
     pc.start_teleportForGM(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
   }



   public void do_inner_enter(L1PcInstance pc) {}


   public int[] next_position(L1PcInstance pc) {
     MJPoint pt = to_rand_location();
     (new int[3])[0] = pt.x; (new int[3])[1] = pt.y; (new int[3])[2] = pt.mapId; return (pt == null) ? null : new int[3];
   }



   public void do_tick() {}



   public void do_revision_map(L1PcInstance pc) {
     int map_id = pc.getMapId();
     if (map_id != 1710 && map_id != 1709 && map_id != 1708) {
       MJPoint pt = to_rand_location();
       pc.start_teleportForGM(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
     }
   }
   private MJPoint to_rand_location() {
     return m_entry_rectangle.toRandPoint(50);
   }
 }



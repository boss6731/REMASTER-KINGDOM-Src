 package MJShiftObject.Battle.DomTower;

 import MJShiftObject.Battle.MJShiftBattleTeamInfo;
 import MJShiftObject.MJShiftObjectManager;
 import MJShiftObject.Object.MJShiftObject;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.MJTemplate.Lineage2D.MJRectangle;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class MJDomTowerTeamInfo extends MJShiftBattleTeamInfo<MJDomTowerCharacterInfo> {
   public static MJDomTowerTeamInfo newInstance(int team_id) {
     return new MJDomTowerTeamInfo(team_id);
   }
   private MJDomTowerTeamInfo(int team_id) {
     super(team_id);
   }

   public void do_enter(L1PcInstance pc, int rank) {
     MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_object(pc);
     if (sobject == null) {
       return;
     }
     int object_id = pc.getId();
     MJDomTowerCharacterInfo cInfo = (MJDomTowerCharacterInfo)this.m_players.get(Integer.valueOf(object_id));
     if (cInfo == null) {
       String source_name = MJShiftObjectManager.getInstance().get_source_character_name(pc);
       if (MJString.isNullOrEmpty(source_name))
         source_name = pc.getName();
       cInfo = MJDomTowerCharacterInfo.newInstance(pc, object_id, source_name, this);
       this.server_description = cInfo.home_server_name;
       this.m_players.put(Integer.valueOf(object_id), cInfo);
     } else {
       cInfo.owner = pc;
     }
     pc.set_battle_info(cInfo);
     int map_id = convert_mapid(sobject);
     MJPoint pt = to_rand_location(map_id);
     pc.start_teleportForGM(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
   }

   private int convert_mapid(MJShiftObject sobject) {
     String convert_parameters = sobject.get_convert_parameters();
     try {
       return Integer.parseInt(convert_parameters);
     } catch (Exception e) {
       e.printStackTrace();

       return 12852;
     }
   }


   public void do_inner_enter(L1PcInstance pc) {}


   public int[] next_position(L1PcInstance pc) {
     MJPoint pt = to_rand_location(pc.getMapId());
     (new int[3])[0] = pt.x; (new int[3])[1] = pt.y; (new int[3])[2] = pt.mapId; return (pt == null) ? null : new int[3];
   }


   public void do_tick() {}


   public void do_revision_map(L1PcInstance pc) {
     int mid = pc.getMapId();
     MJRectangle rt = MJDomTowerNpcActionInfo.entry_rectangles.get(Integer.valueOf(mid));
     if (rt == null) {
       MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_object(pc);
       int map_id = convert_mapid(sobject);
       MJPoint pt = to_rand_location(map_id);
       pc.start_teleportForGM(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
     }
   }
   private MJPoint to_rand_location(int map_id) {
     MJRectangle rt = MJDomTowerNpcActionInfo.entry_rectangles.get(Integer.valueOf(map_id));
     return (rt == null) ? null : rt.toRandPoint(50);
   }
 }



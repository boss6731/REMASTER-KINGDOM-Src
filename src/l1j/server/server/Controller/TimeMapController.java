 package l1j.server.server.Controller;

 import java.util.ArrayList;
 import l1j.server.server.datatables.DoorSpawnTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1TimeMap;


 public class TimeMapController
   implements Runnable
 {
   public static final int SLEEP_TIME = 1000;
   private ArrayList<L1TimeMap> mapList;
   private static TimeMapController instance;

   public static TimeMapController getInstance() {
     if (instance == null) instance = new TimeMapController();
     return instance;
   }



   private TimeMapController() {
     this.mapList = new ArrayList<>();
   }



   public void run() {
     try {
       for (L1TimeMap timeMap : array()) {
         if (timeMap.count()) {
           for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
             if (pc == null)
               continue;
             if (timeMap.getId() != pc.getMapId()) {
               continue;
             }

             switch (pc.getMapId()) {
               case 72:
               case 73:
               case 74:
                 pc.start_teleport(34056, 32279, 4, 5, 18339, true, false);

               case 460:
               case 461:
               case 462:
               case 463:
               case 464:
               case 465:
               case 466:
                 pc.start_teleport(32664, 32855, 457, 5, 18339, true, false);

               case 470:
               case 471:
               case 472:
               case 473:
               case 474:
                 pc.start_teleport(32663, 32853, 467, 5, 18339, true, false);

               case 475:
               case 476:
               case 477:
               case 478:
                 pc.start_teleport(32660, 32876, 468, 5, 18339, true, false);
             }



           }
           DoorSpawnTable.getInstance().getDoor(timeMap.getDoor()).close();
           remove(timeMap);
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }



   public void add(L1TimeMap map) {
     if (this.mapList.size() > 0) {
       boolean found = false;
       for (L1TimeMap m : array()) {
         if (m.getId() == map.getId()) {
           found = true;
           break;
         }
       }
       if (!found)
       {
         this.mapList.add(map); }
     } else {
       this.mapList.add(map);
     }
   }




   private void remove(L1TimeMap map) {
     for (L1TimeMap m : array()) {
       if (m.getId() == map.getId()) {
         this.mapList.remove(map);
         break;
       }
     }
     map = null;
   }




   private L1TimeMap[] array() {
     return this.mapList.<L1TimeMap>toArray(new L1TimeMap[this.mapList.size()]);
   }
 }



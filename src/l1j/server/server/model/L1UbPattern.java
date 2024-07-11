 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Map;


















 public class L1UbPattern
 {
   private boolean _isFrozen = false;
   private Map<Integer, ArrayList<L1UbSpawn>> _groups = new HashMap<>();

   public void addSpawn(int groupNumber, L1UbSpawn spawn) {
     if (this._isFrozen) {
       return;
     }

     ArrayList<L1UbSpawn> spawnList = this._groups.get(Integer.valueOf(groupNumber));
     if (spawnList == null) {
       spawnList = new ArrayList<>();
       this._groups.put(Integer.valueOf(groupNumber), spawnList);
     }

     spawnList.add(spawn);
   }

   public void freeze() {
     if (this._isFrozen) {
       return;
     }


     for (ArrayList<L1UbSpawn> spawnList : this._groups.values()) {
       Collections.sort(spawnList);
     }

     this._isFrozen = true;
   }

   public boolean isFrozen() {
     return this._isFrozen;
   }

   public ArrayList<L1UbSpawn> getSpawnList(int groupNumber) {
     if (!this._isFrozen) {
       return null;
     }

     return this._groups.get(Integer.valueOf(groupNumber));
   }
 }



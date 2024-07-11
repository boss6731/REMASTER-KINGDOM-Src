 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class L1HateList
 {
   private final Map<L1Character, Integer> _hateMap;

   private L1HateList(Map<L1Character, Integer> hateMap) {
     this._hateMap = hateMap;
   }






   public L1HateList() {
     this._hateMap = new ConcurrentHashMap<>();
   }

   public void add(L1Character cha, int hate) {
     if (cha == null) {
       return;
     }
     if (this._hateMap.containsKey(cha)) {
       this._hateMap.put(cha, Integer.valueOf(((Integer)this._hateMap.get(cha)).intValue() + hate));
     } else {
       this._hateMap.put(cha, Integer.valueOf(hate));
     }
   }

   public int get(L1Character cha) {
     return ((Integer)this._hateMap.get(cha)).intValue();
   }

   public boolean containsKey(L1Character cha) {
     return this._hateMap.containsKey(cha);
   }

   public void remove(L1Character cha) {
     if (cha == null)
       return;  this._hateMap.remove(cha);
   }

   public void clear() {
     this._hateMap.clear();
   }

   public boolean isEmpty() {
     return this._hateMap.isEmpty();
   }

   public L1Character getMaxHateCharacter() {
     L1Character cha = null;
     int hate = Integer.MIN_VALUE;

     for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
       if (hate < ((Integer)e.getValue()).intValue()) {
         cha = e.getKey();
         hate = ((Integer)e.getValue()).intValue();
       }
     }
     return cha;
   }

   public void removeInvalidCharacter(L1NpcInstance npc) {
     ArrayList<L1Character> invalidChars = new ArrayList<>();
     for (L1Character cha : this._hateMap.keySet()) {
       if (cha == null || cha.isDead() || !npc.knownsObject(cha)) {
         invalidChars.add(cha);
       }
     }

     for (L1Character cha : invalidChars) {
       this._hateMap.remove(cha);
     }
   }

   public int getTotalHate() {
     int totalHate = 0;
     for (Iterator<Integer> iterator = this._hateMap.values().iterator(); iterator.hasNext(); ) { int hate = ((Integer)iterator.next()).intValue();
       totalHate += hate; }

     return totalHate;
   }

   public int getTotalLawfulHate() {
     int totalHate = 0;
     for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {
       if (e.getKey() instanceof L1PcInstance) {
         totalHate += ((Integer)e.getValue()).intValue();
       }
     }
     return totalHate;
   }

   public int getPartyHate(L1Party party) {
     int partyHate = 0;
     L1PcInstance pc = null;
     L1Character cha = null;
     for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {

       if (e.getKey() instanceof L1PcInstance) {
         pc = (L1PcInstance)e.getKey();
       }
       if (e.getKey() instanceof L1NpcInstance) {
         cha = ((L1NpcInstance)e.getKey()).getMaster();
         if (cha instanceof L1PcInstance) {
           pc = (L1PcInstance)cha;
         }
       }

       if (pc != null && party.isMember(pc)) {
         partyHate += ((Integer)e.getValue()).intValue();
       }
     }
     return partyHate;
   }

   public int getPartyLawfulHate(L1Party party) {
     int partyHate = 0;
     L1PcInstance pc = null;
     for (Map.Entry<L1Character, Integer> e : this._hateMap.entrySet()) {

       if (e.getKey() instanceof L1PcInstance) {
         pc = (L1PcInstance)e.getKey();
       }

       if (pc != null && party.isMember(pc)) {
         partyHate += ((Integer)e.getValue()).intValue();
       }
     }
     return partyHate;
   }

   public L1HateList copy() {
     return new L1HateList(new HashMap<>(this._hateMap));
   }

   public Set<Map.Entry<L1Character, Integer>> entrySet() {
     return this._hateMap.entrySet();
   }

   public ArrayList<L1Character> toTargetArrayList() {
     ArrayList<L1Character> list = new ArrayList<>(this._hateMap.keySet());
     return list;
   }


   public ArrayList<Integer> toHateArrayList() {
     return new ArrayList<>(this._hateMap.values());
   }

   public Map<L1Character, Integer> getMap() {
     return this._hateMap;
   }
 }



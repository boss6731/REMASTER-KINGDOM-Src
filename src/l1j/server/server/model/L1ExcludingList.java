 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;

 public class L1ExcludingList
 {
   private final int _charId;
   public final HashMap<Integer, String> _excludes = new HashMap<>();

   private ArrayList<String> _nameList_Chat = new ArrayList<>();
   private ArrayList<String> _nameList_Letter = new ArrayList<>();

   public String[] getExcludeList(int type) {
     switch (type) {
       case 0:
         return this._nameList_Chat.<String>toArray(new String[this._nameList_Chat.size()]);
       case 1:
         return this._nameList_Letter.<String>toArray(new String[this._nameList_Letter.size()]);
     }
     return null;
   }

   public void add(int type, String name) {
     switch (type) {
       case 0:
         this._nameList_Chat.add(name);
         break;
       case 1:
         this._nameList_Letter.add(name);
         break;
     }
   }

   public void remove(int type, String name) {
     switch (type) {
       case 0:
         this._nameList_Chat.remove(name);
         break;
       case 1:
         this._nameList_Letter.remove(name);
         break;
     }
   }

   public boolean contains(int type, String name) {
     switch (type) {
       case 0:
         return this._nameList_Chat.contains(name);
       case 1:
         return this._nameList_Letter.contains(name);
     }
     return false;
   }

   public L1ExcludingList(int charId) {
     this._charId = charId;
   }

   public int getCharId() {
     return this._charId;
   }

   public boolean remove(int objId) {
     String result = this._excludes.remove(Integer.valueOf(objId));
     return (result != null);
   }

   public boolean remove(String name) {
     int id = 0;
     for (Map.Entry<Integer, String> exclude : this._excludes.entrySet()) {
       if (name.equalsIgnoreCase(exclude.getValue())) {
         id = ((Integer)exclude.getKey()).intValue();
         break;
       }
     }
     if (id == 0) {
       return false;
     }
     this._excludes.remove(Integer.valueOf(id));
     return true;
   }

   public boolean contains(String name) {
     for (String each : this._excludes.values()) {
       if (each.equalsIgnoreCase(name)) {
         return true;
       }
     }
     return false;
   }

   public int size() {
     return this._excludes.size();
   }
 }



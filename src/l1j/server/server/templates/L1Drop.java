 package l1j.server.server.templates;

 public class L1Drop {
   int _mobId;
   int _itemId;
   int _min;
   int _max;
   int _chance;
   int _range;

   public L1Drop(int mobId, int itemId, int min, int max, int chance, int range) {
     this._mobId = mobId;
     this._itemId = itemId;
     this._min = min;
     this._max = max;
     this._chance = chance;
     this._range = range;
   }

   public int getChance() {
     return this._chance;
   }

   public int getItemid() {
     return this._itemId;
   }

   public int getMax() {
     return this._max;
   }

   public int getMin() {
     return this._min;
   }

   public int getMobid() {
     return this._mobId;
   }

   public int getRange() {
     return this._range;
   }
 }



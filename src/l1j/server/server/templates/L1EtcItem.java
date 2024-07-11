 package l1j.server.server.templates;

 import java.util.HashMap;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;

































 public class L1EtcItem
   extends L1Item
 {
   private static final long serialVersionUID = 1L;
   private boolean _stackable;
   private int _locx;
   private int _locy;
   private short _mapid;
   private int _delay_id;
   private int _delay_time;
   private int _delay_effect;
   private int _maxChargeCount;

   public boolean isStackable() {
     return this._stackable;
   }

   public void set_stackable(boolean stackable) {
     this._stackable = stackable;
   }

   public void set_locx(int locx) {
     this._locx = locx;
   }


   public int get_locx() {
     return this._locx;
   }

   public void set_locy(int locy) {
     this._locy = locy;
   }


   public int get_locy() {
     return this._locy;
   }

   public void set_mapid(short mapid) {
     this._mapid = mapid;
   }


   public short get_mapid() {
     return this._mapid;
   }

   public void set_delayid(int delay_id) {
     this._delay_id = delay_id;
   }


   public int get_delayid() {
     return this._delay_id;
   }

   public void set_delaytime(int delay_time) {
     this._delay_time = delay_time;
   }


   public int get_delaytime() {
     return this._delay_time;
   }

   public void set_delayEffect(int delay_effect) {
     this._delay_effect = delay_effect;
   }

   public int get_delayEffect() {
     return this._delay_effect;
   }

   public void setMaxChargeCount(int i) {
     this._maxChargeCount = i;
   }


   public int getMaxChargeCount() {
     return this._maxChargeCount;
   }


   public void setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {}


   public int getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
     return 0;
   }


   public void setSpecialResistanceMap(HashMap<Integer, Integer> itemResistance) {}

   public HashMap<Integer, Integer> getSpecialResistanceMap() {
     return null;
   }


   public void setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value) {}

   public int getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind) {
     return 0;
   }


   public void setSpecialPierceMap(HashMap<Integer, Integer> itemPierce) {}

   public HashMap<Integer, Integer> getSpecialPierceMap() {
     return null;
   }


   public void equipmentItem(L1PcInstance pc, boolean isEquipped) {}


   public int getMagicHitup() {
     return 0;
   }

   public void setMagicHitup(int i) {}
 }



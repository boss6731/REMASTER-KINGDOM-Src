 package l1j.server.server.templates;

 import java.util.HashMap;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1PcInstance;























 public class L1RaceTicket
   extends L1Item
 {
   private static final long serialVersionUID = 1L;
   private boolean _stackable;

   public boolean isStackable() {
     return this._stackable;
   }

   public void set_stackable(boolean stackable) {
     this._stackable = stackable;
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



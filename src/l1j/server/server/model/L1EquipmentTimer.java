 package l1j.server.server.model;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;















 public class L1EquipmentTimer
   implements Runnable
 {
   private long _scheduleTime;
   private boolean _active;
   private final L1PcInstance _pc;
   private final L1ItemInstance _item;

   public L1EquipmentTimer(L1PcInstance pc, L1ItemInstance item, long scheduleTime) {
     this._pc = pc;
     this._item = item;
     this._scheduleTime = scheduleTime;
     this._active = true;
   }


   public void run() {
     if (!this._active) {
       return;
     }


     if (this._item.getRemainingTime() - 1 > 0) {
       this._item.setRemainingTime(this._item.getRemainingTime() - 1);
       this._pc.getInventory().updateItem(this._item, 256);
       GeneralThreadPool.getInstance().schedule(this, this._scheduleTime);
     } else {
       this._pc.getInventory().removeItem(this._item, 1);
     }
   }


   public void cancel() {
     this._active = false;
   }
 }



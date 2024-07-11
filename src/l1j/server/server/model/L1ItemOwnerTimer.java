 package l1j.server.server.model;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1ItemInstance;



 public class L1ItemOwnerTimer
   implements Runnable
 {
   private final L1ItemInstance _item;
   private final int _timeMillis;

   public L1ItemOwnerTimer(L1ItemInstance item, int timeMillis) {
     this._item = item;
     this._timeMillis = timeMillis;
   }


   public void run() {
     this._item.setItemOwner(null);
   }

   public void begin() {
     GeneralThreadPool.getInstance().schedule(this, this._timeMillis);
   }
 }



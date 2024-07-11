 package l1j.server.server;
 public abstract class RepeatTask implements Runnable {
   private boolean _active;

   public RepeatTask(long interval) {
     this._interval = interval;
     this._active = true;
   }
   private long _interval;
   public long getInterval() {
     return this._interval;
   }


   public abstract void execute();

   public final void run() {
     if (!this._active) {
       return;
     }
     execute();

     if (this._active) {
       GeneralThreadPool.getInstance().schedule(this, this._interval);
     }
   }

   public void cancel() {
     this._active = false;
   }
 }



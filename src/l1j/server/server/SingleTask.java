 package l1j.server.server;

 public abstract class SingleTask
   implements Runnable
 {
   public abstract void execute();

   public final void run() {
     if (!this._active) {
       return;
     }
     this._executed = true;
     execute();
   }

   public void cancel() {
     this._active = false;
   }

   public boolean isActive() {
     return this._active;
   }

   public boolean isExecuted() {
     return this._executed;
   }

   private boolean _active = true;
   private boolean _executed = false;
 }



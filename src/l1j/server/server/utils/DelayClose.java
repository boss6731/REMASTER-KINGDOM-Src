 package l1j.server.server.utils;

 import l1j.server.server.GameClient;

 public class DelayClose
   implements Runnable {
   public DelayClose(GameClient clnt) {
     this._clnt = clnt;
   }
   private GameClient _clnt;

   public void run() {
     try {
       if (this._clnt != null)
         this._clnt.close();
     } catch (Exception exception) {}
   }
 }



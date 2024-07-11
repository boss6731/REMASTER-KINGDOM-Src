 package server;

     import java.util.Timer;


















     public class TimerPool
     {
       private Timer[] _timers;
       private int _numOfTimers;
       private int _pointer = 0;

       public TimerPool(int numOfTimers) {
         this._timers = new Timer[numOfTimers];
         for (int i = 0; i < numOfTimers; i++) {
           this._timers[i] = new Timer();
         }
         this._numOfTimers = numOfTimers;
       }

       public synchronized Timer getTimer() {
         if (this._numOfTimers <= this._pointer) {
           this._pointer = 0;
         }
         return this._timers[this._pointer++];
       }
     }



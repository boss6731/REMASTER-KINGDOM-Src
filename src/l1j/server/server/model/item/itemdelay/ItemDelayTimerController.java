 package l1j.server.server.model.item.itemdelay;

 import java.sql.Timestamp;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.L1Character;

 public class ItemDelayTimerController extends ItemDelayTimer {
   private L1Character character;
   private final long timeMillis;
   private final int ItemId;
   private final String ItemName;
   private int remainingSeconds;
   private Timestamp log_delay_time;
   private boolean stop;

   ItemDelayTimerController(L1Character character, int itemId, String ItemName, long timeMillis, Timestamp logtime) {
     this.character = character;
     this.ItemId = itemId;
     this.ItemName = ItemName;
     this.timeMillis = timeMillis;
     this.remainingSeconds = (int)(timeMillis / 1000L);
     this.log_delay_time = logtime;
     this.stop = false;
   }


   public L1Character owner() {
     return this.character;
   }


   public int ItemId() {
     return this.ItemId;
   }


   public String ItemName() {
     return this.ItemName;
   }


   public long timeMillis() {
     return this.timeMillis;
   }


   public boolean stopped() {
     return this.stop;
   }


   public int remainingSeconds() {
     return this.remainingSeconds;
   }


   public void remainingSeconds(int remainingSeconds) {
     this.remainingSeconds = remainingSeconds;
   }


   public Timestamp LogDelayTime() {
     return this.log_delay_time;
   }


   public void begin() {
     GeneralThreadPool.getInstance().schedule(this, 100L);
   }


   public void end() {
     this.stop = true;
     this.character = null;
   }


   public void kill() {
     this.stop = true;
     this.character = null;
   }
 }



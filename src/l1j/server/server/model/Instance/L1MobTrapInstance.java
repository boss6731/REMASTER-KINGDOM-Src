 package l1j.server.server.model.Instance;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.IntRange;
 import l1j.server.server.utils.L1SpawnUtil;

 public class L1MobTrapInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private static Logger _log = Logger.getLogger(L1MobTrapInstance.class.getName()); private int SleepTime;

   public L1MobTrapInstance(L1Npc template) {
     super(template);


     this.SleepTime = 500;
     this.AIStart = false;
   }
   private boolean AIStart;

   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() == null) {
       perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this), true);
     }
     startTrapAI();
   }


   private synchronized void startTrapAI() {
     if (!this.AIStart) {
       this.AIStart = true;
       (new TrapAIThreadImpl()).start();
     }
   }




   class TrapAIThreadImpl
     implements Runnable, TrapAI
   {
     public void start() {
       GeneralThreadPool.getInstance().execute(this);
     }

       public void run() {
           try {
               while (this != null) {
                   Thread.sleep(L1MobTrapInstance.this.SleepTime); // 緩解一段時間
                   if (L1MobTrapInstance.this.onTrapAi()) { // 執行陷阱AI
                       L1MobTrapInstance.this.AIStart = false; // 停止AI
                       return; // 結束執行
                   }
               }
           } catch (Exception e) {
               L1MobTrapInstance._log.log(Level.WARNING, "在TrapAI中發生了異常。", e); // 記錄異常
           }
       }

   private boolean onTrapAi() {
     if (L1World.getInstance().getRecognizePlayer((L1Object)this).size() == 0) {
       return true;
     }
     for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)this, 0)) {
       if (pc != null) {
         int count = IntRange.random(1) + 3;
         int[] moblist = { 7000056, 7000057, 7000058, 7000059, 7000060 };
         for (int i = 0; i < count; i++) {
           L1SpawnUtil.spawn5(getX(), getY(), getMapId(), 5, moblist[IntRange.random(moblist.length)], 4, false);
         }
         deleteMe();
         return true;
       }
     }
     return false;
   }

   static interface TrapAI {
     void start();
   }
 }



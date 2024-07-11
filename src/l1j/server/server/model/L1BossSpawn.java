 package l1j.server.server.model;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.util.Calendar;
 import java.util.Random;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.gametime.RealTimeClock;
 import l1j.server.server.templates.L1Npc;

 public class L1BossSpawn
   extends L1Spawn
 {
   private static Logger _log = Logger.getLogger(L1BossSpawn.class.getName()); private int _spawnCount; private String _cycleType; private int _percentage;
   private L1BossCycle _cycle;
   private Calendar _activeSpawnTime;

   private class SpawnTask implements Runnable { private int _spawnNumber;

     private SpawnTask(int spawnNumber, int objectId) {
       this._spawnNumber = spawnNumber;
       this._objectId = objectId;
     }
     private int _objectId;

     public void run() {
       try {
         L1BossSpawn.this.doSpawn(this._spawnNumber, this._objectId);
       } catch (Exception exception) {}
     } }




   protected L1NpcInstance doSpawn(int spawnNumber, int objectId) {
     L1NpcInstance npc = super.doSpawn(spawnNumber, objectId);
     if (npc != null)
       MJUIAdapter.on_boss_append(npc.getNpcId(), npc.getName(), npc.getX(), npc.getY(), npc.getMapId());
     return npc;
   }

   public L1BossSpawn(L1Npc mobTemplate) throws SecurityException, ClassNotFoundException {
     super(mobTemplate);
   }









   public void executeSpawnTask(int spawnNumber, int objectId) {
     if (subAndGetCount() != 0) {
       return;
     }

     Calendar spawnTime = calcNextSpawnTime(RealTimeClock.getInstance().getRealTimeCalendar());







     spawnBoss(spawnTime, objectId);
   }



   private synchronized int subAndGetCount() {
     return --this._spawnCount;
   }



   public void setCycleType(String type) {
     this._cycleType = type;
   }



   public void setPercentage(int percentage) {
     this._percentage = percentage;
   }





   private static Random _rnd = new Random();

   public void init() {
     Calendar spawnTime;
     if (this._percentage <= 0) {
       return;
     }
     this._cycle = L1BossCycle.getBossCycle(this._cycleType);
     if (this._cycle == null) {
       throw new RuntimeException(this._cycleType + " not found");
     }
     Calendar now = Calendar.getInstance();


     if (Config.ServerAdSetting.INITBOSSSPAWN && this._percentage > _rnd.nextInt(100)) {
       spawnTime = this._cycle.calcSpawnTime(now);
     } else {

       spawnTime = calcNextSpawnTime(now);
     }
     spawnBoss(spawnTime, 0);
   }


   private Calendar calcNextSpawnTime(Calendar cal) {
     while (true) {
       cal = this._cycle.nextSpawnTime(cal);
       if (this._percentage > _rnd.nextInt(100))
         return cal;
     }
   }

   private void spawnBoss(Calendar spawnTime, int objectId) {
     this._activeSpawnTime = spawnTime;
     long delay = spawnTime.getTimeInMillis() - System.currentTimeMillis();

     int cnt = this._spawnCount;
     this._spawnCount = getAmount();
     while (cnt < getAmount()) {
       cnt++;
       GeneralThreadPool.getInstance().schedule(new SpawnTask(0, objectId), delay);
     }
     _log.log(Level.FINE, toString());
   }





     public String toString() {
         StringBuilder builder = new StringBuilder();

         // 添加 NPC 的 ID 信息
         builder.append("[MOB]npcid:" + getNpcId());

         // 添加 NPC 的名稱信息
         builder.append(" 名稱:" + getName());

         // 添加週期類型名稱
         builder.append("[類型]" + this._cycle.getName());

         // 添加當前的週期信息
         builder.append("[當前的週期]");
         builder.append(this._cycle.getSpawnStartTime(this._activeSpawnTime).getTime());
         builder.append(" - ");
         builder.append(this._cycle.getSpawnEndTime(this._activeSpawnTime).getTime());

         // 添加出現時間
         builder.append("[出現時間]");
         builder.append(this._activeSpawnTime.getTime());

         // 返回構建的字符串
         return builder.toString();
     }


 package l1j.server.server.Controller;

 import l1j.server.server.datatables.LightSpawnTable;
 import l1j.server.server.model.Instance.L1FieldObjectInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.gametime.BaseTime;
 import l1j.server.server.model.gametime.GameTime;
 import l1j.server.server.model.gametime.GameTimeClock;
 import l1j.server.server.model.gametime.TimeListener;

 public class LightTimeController implements TimeListener {
   private static LightTimeController _instance;
   private static boolean isSpawn = true;

   public static void start() {
     if (_instance == null) {
       _instance = new LightTimeController();
     }

     GameTimeClock.getInstance().addListener(_instance);

     if (GameTimeClock.getInstance().getGameTime().isNight()) {
       isSpawn = false;
     } else {
       isSpawn = true;
     }
     _instance.changeLight(GameTimeClock.getInstance().getGameTime());
   }

   private void changeLight(GameTime gameTime) {
     if (gameTime.isNight()) {
       turnOn();
     } else {
       turnOff();
     }
   }
   private void turnOn() {
     if (!isSpawn) {
       isSpawn = true;
       LightSpawnTable.getInstance().FillLightSpawnTable();
     }
   }

   private void turnOff() {
     if (isSpawn) {
       isSpawn = false;
       L1FieldObjectInstance npc = null;
       for (L1Object object : L1World.getInstance().getObject()) {
         if (object instanceof L1FieldObjectInstance) {
           npc = (L1FieldObjectInstance)object;

           if (((npc.getNpcTemplate().get_npcId() >= 81177 && npc
             .getNpcTemplate().get_npcId() <= 81181) || npc
             .getNpcTemplate().get_npcId() == 81160 || npc
             .getNpcTemplate().get_npcId() == 81153) && (npc
             .getMapId() == 0 || npc.getMapId() == 4 || npc.getMapId() == 5167)) {
             npc.deleteMe();
             npc = null;
           }
         }
       }
     }
   }




   public void onHourChanged(BaseTime time) {}



   public void onDayChanged(BaseTime time) {}



   public void onMinuteChanged(BaseTime time) {
     changeLight((GameTime)time);
   }

   public void onMonthChanged(BaseTime time) {}

   public void onSecondChanged(BaseTime time) {}
 }



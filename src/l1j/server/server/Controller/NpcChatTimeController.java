 package l1j.server.server.Controller;

 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.TimeZone;
 import l1j.server.Config;
 import l1j.server.server.datatables.NpcChatTable;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1NpcChat;




 public class NpcChatTimeController
   implements Runnable
 {
   public static final int SLEEP_TIME = 60000;
   private static final SimpleDateFormat _sdf = new SimpleDateFormat("HHmm");

   private static NpcChatTimeController _instance;


   public static NpcChatTimeController getInstance() {
     if (_instance == null) {
       _instance = new NpcChatTimeController();
     }
     return _instance;
   }


   public void run() {
     try {
       checkNpcChatTime();
     }
     catch (Exception e1) {
       e1.printStackTrace();
     }
   }

   private void checkNpcChatTime() {
     for (L1NpcChat npcChat : NpcChatTable.getInstance().getAllGameTime()) {
       if (isChatTime(npcChat.getGameTime())) {
         int npcId = npcChat.getNpcId();
         for (L1Object obj : L1World.getInstance().getObject()) {
           if (!(obj instanceof L1NpcInstance)) {
             continue;
           }
           L1NpcInstance npc = (L1NpcInstance)obj;
           if (npc.getNpcTemplate().get_npcId() == npcId) {
             npc.startChat(3);
           }
         }
       }
     }
   }


   private boolean isChatTime(int chatTime) {
     Calendar realTime = getRealTime();
     int nowTime = Integer.valueOf(_sdf.format(realTime.getTime())).intValue();
     return (nowTime == chatTime);
   }

   private static Calendar getRealTime() {
     TimeZone _tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
     Calendar cal = Calendar.getInstance(_tz);
     return cal;
   }
 }



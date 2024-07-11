 package l1j.server.server.Controller;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javolution.util.FastTable;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1NpcInstance;

 public class NpcDeleteController
   implements Runnable {
   private static Logger _log = Logger.getLogger(NpcDeleteController.class.getName());

   private static NpcDeleteController _instance;
   private FastTable<L1NpcInstance> list;
   private FastTable<L1NpcInstance> li;

   public static NpcDeleteController getInstance() {
     if (_instance == null)
       _instance = new NpcDeleteController();
     return _instance;
   }





   public NpcDeleteController() {
     this.li = null;
     this.list = new FastTable();
     GeneralThreadPool.getInstance().execute(this); } public void run() {
     while (true) {
       try {
         this.li = this.list;
         for (L1NpcInstance npc : this.li) {

           if (npc == null)
             continue;
           if (npc.NpcDeleteTime < System.currentTimeMillis()) {
             npc.NpcDeleteTime = 0L;
             npc.deleteMe();
             removeNpcDelete(npc);
           }

         }
       } catch (Exception e) {
         _log.log(Level.SEVERE, "NpcDeleteController[]Error", e);
       } finally {

         try {
           this.li = null;
           Thread.sleep(250L);
         } catch (Exception exception) {}
       }
     }
   }


   public void addNpcDelete(L1NpcInstance npc) {
     if (!this.list.contains(npc))
       this.list.add(npc);
   }

   public void removeNpcDelete(L1NpcInstance npc) {
     if (this.list.contains(npc))
       this.list.remove(npc);
   }

   public int getSize() {
     return this.list.size();
   }
 }



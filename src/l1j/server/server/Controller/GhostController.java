 package l1j.server.server.Controller;

 import javolution.util.FastTable;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class GhostController implements Runnable {
   private static GhostController _instance;
   private final FastTable<L1PcInstance> _list = new FastTable(128);

   public static synchronized GhostController getInstance() {
     if (_instance == null)
       _instance = new GhostController();
     return _instance;
   }





   public void run() {
     try {
       long now = System.currentTimeMillis();
       for (L1PcInstance pc : this._list) {
         if (pc == null) {
           removeMember(pc);
           continue;
         }
         if (pc.ghosttime <= now) {
           if (pc._ghostCount < 16) {
             pc._ghostCount = (byte)(pc._ghostCount + 1);
           } else {
             pc._ghostCount = 0;
           }  pc.makeReadyEndGhost();
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       GeneralThreadPool.getInstance().schedule(this, 1000L);
     }
   }

   public void addMember(L1PcInstance pc) {
     if (pc != null && !this._list.contains(pc))
       this._list.add(pc);
   }

   public void removeMember(L1PcInstance pc) {
     if (pc != null && this._list.contains(pc))
       this._list.remove(pc);
   }

   public boolean isPlayMember(L1PcInstance pc) {
     return this._list.contains(pc);
   }
 }



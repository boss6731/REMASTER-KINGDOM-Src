 package l1j.server.server.model.item.collection.time;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionAblity;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionStatus;


 public class L1TimeCollectionTimer
   implements Runnable
 {
   private final L1PcInstance owner;
   private L1TimeCollectionAblity ablity;
   private boolean active;

   public L1TimeCollectionTimer(L1PcInstance owner, L1TimeCollectionAblity ablity) {
     this.owner = owner;
     this.ablity = ablity;
     this.active = true;
   }


   public void run() {
     try {
       if (!this.active || this.owner == null || this.owner.getNetConnection() == null || this.owner.getTimeCollection() == null) {
         return;
       }
       L1TimeCollectionUser user = this.owner.getTimeCollection().getUser(this.ablity.getFlag());
       if (user == null || user.getAblity() == null || !isEquals(user.getAblity())) {
         return;
       }

       this.owner.getTimeCollection().delete(this.owner, user.getObj(), L1TimeCollectionStatus.CLOSE);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public boolean isEquals(L1TimeCollectionAblity ablity) {
     return this.ablity.equals(ablity);
   }

   public void trans(L1TimeCollectionAblity ablity) {
     this.ablity = ablity;
   }

   public void cancel() {
     this.active = false;
   }
 }



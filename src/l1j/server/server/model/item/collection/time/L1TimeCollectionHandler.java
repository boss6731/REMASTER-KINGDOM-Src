 package l1j.server.server.model.item.collection.time;

 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_BUFF_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_DATA_LOAD_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_REGIST_ITEM_ACK;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_RESET_ACK;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_SET_DATA_NOTI;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionAblity;
 import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionStatus;
 import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;



 public class L1TimeCollectionHandler
 {
   private final L1PcInstance owner;
   private final ConcurrentHashMap<Integer, L1TimeCollectionUser> DATA;
   private int buffSize;

   public ConcurrentHashMap<Integer, L1TimeCollectionUser> getData() {
     return this.DATA;
   }

   public L1TimeCollectionUser getUser(int flag) {
     return this.DATA.get(Integer.valueOf(flag));
   }

   public L1TimeCollectionHandler(L1PcInstance owner) {
     this.owner = owner;
     this.DATA = new ConcurrentHashMap<>();
     init(owner);
   }


   public boolean regist(L1PcInstance pc, L1TimeCollection obj, L1ItemInstance item, int slotIndex) {
     L1TimeCollectionUser user = this.DATA.get(Integer.valueOf(obj.getFlag()));
     if (user == null) {

       L1TimeCollectionBuffType type = null;
       switch (this.owner.getType()) {
         case 2:
           type = L1TimeCollectionBuffType.LONG;
           break;
         case 3:
         case 6:
           type = L1TimeCollectionBuffType.MAGIC;
           break;
         default:
           type = L1TimeCollectionBuffType.SHORT;
           break;
       }

       user = new L1TimeCollectionUser(this.owner.getId(), obj.getFlag(), obj.getType(), obj.getCollectionIndex(), new ConcurrentHashMap<>(), false, 0, type, null, obj, 0);
     }



     if (user.getBuffTimer() != null) {
       return false;
     }
     user.putRegistItem(slotIndex, item);

     ConcurrentHashMap<Integer, L1ItemInstance> registItems = user.getRegistItem();
     if (registItems.size() == obj.getSlotSize()) {

       user.setRegistComplet(true);
       int sum = 0;
       int buffDuration = 0;
       L1ItemInstance value = null;

       for (Map.Entry<Integer, L1ItemInstance> entry : registItems.entrySet()) {
         value = entry.getValue();
         sum += value.getEnchantLevel();
         buffDuration += obj.getDuration(((Integer)entry.getKey()).intValue(), value.getEnchantLevel());
       }


       user.setSumEnchant(sum);


       long currentTime = System.currentTimeMillis();
       if (user.getBuffTime() == null) {
         user.setBuffTime(new Timestamp(buffDuration * 3600000L + currentTime));

       }
       else {


         user.getBuffTime().setTime(buffDuration * 3600000L + currentTime);
       }

       activeAblity(pc, user);
       SC_TIME_COLLECTION_SET_DATA_NOTI.send(pc, user);
     }


     SC_TIME_COLLECTION_REGIST_ITEM_ACK.send(pc, user, slotIndex, item);


     if (L1TimeCollectionUserLoader.getInstance().insert(user)) {
       this.DATA.put(Integer.valueOf(user.getFlag()), user);

       return true;
     }
     return false;
   }


   public boolean delete(L1PcInstance pc, L1TimeCollection obj, L1TimeCollectionStatus status) {
     L1TimeCollectionUser user = this.DATA.remove(Integer.valueOf(obj.getFlag()));
     if (user == null) {
       System.out.println(String.format("[L1TimeCollectionHandler] DELETE RESON(user == null) NAME(%s) FLAG(%d)", new Object[] { this.owner

               .getName(), Integer.valueOf(obj.getFlag()) }));
       return false;
     }

     if (user.isBuffActive()) {
       user.getAblity().ablity(this.owner, false);
       user.setBuffTime(null);
       user.setSumEnchant(0);
       user.getRegistItem().clear();
       user.getBuffTimer().cancel();
       user.setAblity(null);
       user.setBuffTimer(null);
       user.setRegistComplet(false);
       user.setBuffIndex(--this.buffSize);
       user.setRefill_count(0);
       SC_TIME_COLLECTION_BUFF_NOTI.send(pc, user, status);
     }


     if (L1TimeCollectionUserLoader.getInstance().delete(user)) {
       SC_TIME_COLLECTION_RESET_ACK.send(pc, user);


       return true;
     }
     return false;
   }


   public void activeAblity(L1PcInstance pc, L1TimeCollectionUser user) {
     if (!user.isRegistComplet()) {
       return;
     }
     long interval = user.restBuffTime();
     if (interval <= 0L) {
       return;
     }

     L1TimeCollectionAblity ablity = user.getObj().getAblity(user.getSumEnchant(), user.getBuffType());
     if (ablity == null) {
       System.out.println(String.format("[L1TimeCollectionHandler] ABLITY NOT FOUND : SUM_ENCHANT(%d), BUFF_TYPE(%s), NAME(%s)", new Object[] {

               Integer.valueOf(user.getSumEnchant()), user.getBuffType().getName(), this.owner.getName() }));
       return;
     }
     user.setAblity(ablity);
     ablity.ablity(this.owner, true);


     if (user.getBuffTimer() == null) {

       user.setBuffIndex(++this.buffSize);
       user.setBuffTimer(new L1TimeCollectionTimer(this.owner, ablity));
       GeneralThreadPool.getInstance().schedule(user.getBuffTimer(), interval);

     }
     else if (!user.getBuffTimer().isEquals(ablity)) {
       user.getBuffTimer().trans(ablity);
     }

     SC_TIME_COLLECTION_BUFF_NOTI.send(pc, user, L1TimeCollectionStatus.START);
   }

   private void init(L1PcInstance pc) {
     ArrayList<L1TimeCollectionUser> list = L1TimeCollectionUserLoader.getUserList(this.owner.getId());
     if (list != null && !list.isEmpty()) {
       for (L1TimeCollectionUser obj : list) {
         this.DATA.put(Integer.valueOf(obj.getFlag()), obj);
         activeAblity(pc, obj);
       }
     }
     sendLoginPacket(pc);
   }

   private void sendLoginPacket(L1PcInstance pc) {
     SC_TIME_COLLECTION_DATA_LOAD_NOTI.send(pc, this);
   }


   public void dispose() {
     if (this.DATA != null && !this.DATA.isEmpty()) {
       for (L1TimeCollectionUser user : this.DATA.values()) {
         if (user.getBuffTimer() != null) {
           user.getBuffTimer().cancel();
           user.setBuffTimer(null);
         }
       }
     }
     this.DATA.clear();
   }
 }



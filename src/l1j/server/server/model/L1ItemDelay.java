 package l1j.server.server.model;

 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1EtcItem;


 public class L1ItemDelay
 {
   static class ItemDelayTimer
     implements Runnable
   {
     private int _delayId;
     private L1Character _cha;

     public ItemDelayTimer(L1Character cha, int id, int time) {
       this._cha = cha;
       this._delayId = id;
     }


     public void run() {
       stopDelayTimer(this._delayId);
     }

     public void stopDelayTimer(int delayId) {
       this._cha.removeItemDelay(delayId);
     }
   }

   public static void onItemUse(L1PcInstance pc, L1ItemInstance item) {
     int delayId = 0;
     int delayTime = 0;

     if (item.getItem().getType2() == 0) {
       delayId = ((L1EtcItem)item.getItem()).get_delayid();
       delayTime = ((L1EtcItem)item.getItem()).get_delaytime();
       if (item.getItem().getUseType() == 51) {
         AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
         int potioncri = 0;

         if (Info != null) {
           if (Info.get_potion() != 0) {
             potioncri = Info.get_potion_val_2();
           }
           if (potioncri != 0)
             delayTime -= potioncri;
         }
       }
     } else {
       if (item.getItem().getType2() == 1)
         return;
       if (item.getItem().getType2() == 2)
       {
         if (item.getItem().getItemId() == 20077 || item.getItem().getItemId() == 20062 || item.getItem().getItemId() == 120077) {
           if (!pc.isInvisble()) {
             L1ItemInstance clock = pc.getInventory().getItemEquipped(2, 4);
             if (clock == null)
               pc.beginInvisTimer();
           }
         } else {
           return;
         }
       }
     }
     ItemDelayTimer timer = new ItemDelayTimer((L1Character)pc, delayId, delayTime);

     pc.addItemDelay(delayId, timer);
     GeneralThreadPool.getInstance().schedule(timer, delayTime);
   }
 }



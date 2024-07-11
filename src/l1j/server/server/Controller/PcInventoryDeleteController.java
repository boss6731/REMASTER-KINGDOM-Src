 package l1j.server.server.Controller;

 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.LinkedList;
 import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
 import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
 import l1j.server.AinhasadSpecialStat2.L1AinhasadFaithUserLoader;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI;
 import l1j.server.server.datatables.KeyTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillIconGFX;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;





 public class PcInventoryDeleteController
   implements Runnable
 {
   private static PcInventoryDeleteController _instance;
   public static final int SLEEP_TIME = 60000;

   public static PcInventoryDeleteController getInstance() {
     if (_instance == null)
       _instance = new PcInventoryDeleteController();
     return _instance;
   }

   private Collection<L1PcInstance> _list = null;

   public void run() {
     long currentTimeMillis = System.currentTimeMillis();
     try {
       this._list = L1World.getInstance().getAllPlayers();
       for (L1PcInstance pc : this._list) {
         if (pc == null) {
           continue;
         }
         L1PcInventory l1PcInventory = pc.getInventory();
         for (L1ItemInstance item : l1PcInventory.getItems()) {
           if (item == null) {
             continue;
           }
           if (item.getEndTime() == null) {
             continue;
           }
           if (currentTimeMillis > item.getEndTime().getTime()) {

             int itemId = item.getItemId();

             if (itemId == 87051) {
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(1823));
               pc.getInventory().storeItem(87053, 1);
             } else if (itemId == 87050) {
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(1823));
               pc.getInventory().storeItem(87052, 1);
             } else if (itemId == 3000048) {
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(1823));
               pc.getInventory().consumeItem(3000048, 1);
             } else if (itemId == 80500) {
               KeyTable.DeleteKeyId(item.getKeyId());
             } else if (itemId >= 30022 && itemId <= 30025) {

               L1DollInstance doll = pc.getMagicDoll();
               if (doll != null &&
                 item.getId() == doll.getItemObjId()) {
                 doll.deleteDoll();
                 pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(56, 0));
                 pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
               }
             }

               pc.sendPackets((ServerBasePacket)new S_SystemMessage(item.getName() + " 的使用時間已過，已消失。")); // 註解: 發送系統消息，告知玩家物品的使用時間已過，物品已消失
               l1PcInventory.removeItem(item); // 註解: 從玩家的庫存中移除該物品
           }
         }
         AinhasadFaithTimeOut(pc, currentTimeMillis);
         favorBookInventoryTimeOut(pc, currentTimeMillis);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       this._list = null;
     }
   }






   private void favorBookInventoryTimeOut(L1PcInstance pc, long cuurrentTime) {
     L1FavorBookInventory favorBook = pc.getFavorBook();
     if (favorBook == null) {
       return;
     }
     LinkedList<L1FavorBookUserObject> delList = null;

     for (L1FavorBookUserObject user : favorBook.getList()) {
       if (user == null || user.getType().getEndTime() == null || user.getType().getEndTime().getTime() > cuurrentTime) {
         continue;
       }
       if (delList == null) {
         delList = new LinkedList<>();
       }
       delList.add(user);
     }

     if (delList == null) {
       return;
     }
     for (L1FavorBookUserObject user : delList) {
       favorBook.deleteFavor(user);
     }
   }


   private void AinhasadFaithTimeOut(L1PcInstance pc, long currentTime) {
     ArrayList<Integer> list = new ArrayList<>(pc.getAinHasd_faith().keySet());
     HashMap<Integer, Timestamp> map = pc.getAinHasd_faith();
     ArrayList<Integer> deletlist = new ArrayList<>();
     for (int i = 0; i < list.size(); i++) {
       if (((Integer)list.get(i)).intValue() != 101 && ((Integer)list.get(i)).intValue() != 102) {



         String TimeStr = ((Timestamp)map.get(list.get(i))).toString();
         long time = Timestamp.valueOf(TimeStr).getTime();
         int index = ((Integer)list.get(i)).intValue();
         AinhasadSpecialStat2Info info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
         if (time < currentTime &&
           info.get_type() != 1) {
           AinhasadSpecialStat2Info.einhasad_faith_option(pc, index, null, false);
           L1AinhasadFaithUserLoader.getInstance().delete(pc, index);
           SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send(pc, index);
           deletlist.add(list.get(i));
         }
       }
     }


     if (list.contains(Integer.valueOf(101)) && (
       deletlist.contains(Integer.valueOf(1)) || deletlist.contains(Integer.valueOf(2)) || deletlist.contains(Integer.valueOf(3)) || deletlist.contains(Integer.valueOf(4)))) {
       AinhasadSpecialStat2Info.einhasad_faith_option(pc, 101, null, false);
       L1AinhasadFaithUserLoader.getInstance().delete(pc, 101);
       SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send_group(pc, 1);
     }

     if (list.contains(Integer.valueOf(102)) && (
       deletlist.contains(Integer.valueOf(5)) || deletlist.contains(Integer.valueOf(6)) || deletlist.contains(Integer.valueOf(7)) || deletlist.contains(Integer.valueOf(8)))) {
       AinhasadSpecialStat2Info.einhasad_faith_option(pc, 102, null, false);
       L1AinhasadFaithUserLoader.getInstance().delete(pc, 102);
       SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send_group(pc, 2);
     }

     list.clear();
     map.clear();
     deletlist.clear();
   }
 }



 package l1j.server.server.model;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_DropItem;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1GroundInventory
   extends L1Inventory
 {
   private static final long serialVersionUID = 1L;

   private class DeletionTimer
     implements Runnable {
     private final L1ItemInstance _item;

     public DeletionTimer(L1ItemInstance item) {
       this._item = item;
     }


     public void run() {
       try {
         synchronized (L1GroundInventory.this) {
           if (!L1GroundInventory.this._items.contains(this._item)) {
             return;
           }
           L1GroundInventory.this.removeItem(this._item);
         }
       } catch (Throwable t) {
         L1GroundInventory._log.log(Level.SEVERE, t.getLocalizedMessage(), t);
       }
     }
   }

   private void setTimer(L1ItemInstance item) {
     if (!Config.ServerAdSetting.ITEMDELETIONTYPE.equalsIgnoreCase("std")) {
       return;
     }
     if (item.getItemId() == 40515) {
       return;
     }

     GeneralThreadPool.getInstance().schedule(new DeletionTimer(item), (Config.ServerAdSetting.ITEMDELETIONTIME * 60 * 1000));
   }

   public L1GroundInventory(int objectId, int x, int y, short map) {
     setId(objectId);
     setX(x);
     setY(y);
     setMap(map);
     L1World.getInstance().addVisibleObject(this);
   }


   public void onPerceive(L1PcInstance perceivedFrom) {
     for (L1ItemInstance item : getItems()) {
       if (!perceivedFrom.knownsObject((L1Object)item)) {
         perceivedFrom.addKnownObject((L1Object)item);
         if (perceivedFrom.getAI() == null) {
           perceivedFrom.sendPackets((ServerBasePacket)new S_DropItem(item));
         }
       }
     }
   }


   public void insertItem(L1ItemInstance item) {
     setTimer(item);

     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)item)) {
       pc.sendPackets((ServerBasePacket)new S_DropItem(item));
       pc.addKnownObject((L1Object)item);
     }
     this._items.add(item);
   }



   public void updateItem(L1ItemInstance item) {
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)item)) {
       pc.sendPackets((ServerBasePacket)new S_DropItem(item));
     }
   }



   public void deleteItem(L1ItemInstance item) {
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)item)) {
       pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)item));
       pc.removeKnownObject((L1Object)item);
     }

     this._items.remove(item);
     if (this._items.size() == 0) {
       L1World.getInstance().removeVisibleObject(this);
     }
   }

   private static Logger _log = Logger.getLogger(L1PcInventory.class.getName());
 }



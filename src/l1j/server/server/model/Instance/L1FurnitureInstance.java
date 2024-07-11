 package l1j.server.server.model.Instance;

 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

















 public class L1FurnitureInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private int _itemObjId;

   public L1FurnitureInstance(L1Npc template) {
     super(template);
   }


   public void onAction(L1PcInstance player) {}


   public void deleteMe() {
     this._destroyed = true;
     if (getInventory() != null) {
       getInventory().clearItems();
     }
     L1World.getInstance().removeVisibleObject((L1Object)this);
     L1World.getInstance().removeObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       if (pc == null)
         continue;
       pc.removeKnownObject((L1Object)this);
       pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
     }
     removeAllKnownObjects();
   }

   public int getItemObjId() {
     return this._itemObjId;
   }

   public void setItemObjId(int i) {
     this._itemObjId = i;
   }
 }



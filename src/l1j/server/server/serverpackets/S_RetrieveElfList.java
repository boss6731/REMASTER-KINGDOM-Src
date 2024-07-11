 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Warehouse.ElfWarehouse;
 import l1j.server.server.model.Warehouse.WarehouseManager;



 public class S_RetrieveElfList
   extends ServerBasePacket
 {
   public boolean NonValue = false;

   public S_RetrieveElfList(int objid, L1PcInstance pc) {
     if (pc.getInventory().getSize() < 200) {
       ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
       int size = elfwarehouse.getSize();
       if (size > 0) {
         writeC(127);
         writeD(objid);
         writeH(size);
         writeC(9);
         L1ItemInstance item = null;
         for (Object itemObject : elfwarehouse.getItems()) {
           item = (L1ItemInstance)itemObject;
           writeD(item.getId());
           writeC(item.getItem().getType2());
           writeH(item.get_gfxid());
           writeC(item.getItem().getBless());
           writeD(item.getCount());
           writeC(item.isIdentified() ? 1 : 0);
           writeS(item.getViewName());
           writeC(0);
         }
         writeD(30);
         writeD(0);
         writeH(0);
       } else {
         this.NonValue = true;
       }
     } else {
       pc.sendPackets(new S_ServerMessage(263));
     }
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }



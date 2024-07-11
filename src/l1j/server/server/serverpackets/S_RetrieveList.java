 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Warehouse.PrivateWarehouse;
 import l1j.server.server.model.Warehouse.WarehouseManager;



 public class S_RetrieveList
   extends ServerBasePacket
 {
   public boolean NonValue = false;

   public S_RetrieveList(int objid, L1PcInstance pc) {
     if (pc.getInventory().getSize() < 200) {

       PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
       int size = warehouse.getSize();
       if (size > 0) {
         writeC(127);
         writeD(objid);
         writeH(size);
         writeC(3);
         L1ItemInstance item = null;
         for (Object itemObject : warehouse.getItems()) {
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
         writeD(100);
         writeD(0);
         writeH(0);
       } else {
         this.NonValue = true;
       }
     } else {

       pc.sendPackets(new S_ServerMessage(263));
       return;
     }
   }


   public S_RetrieveList(int objid, L1PcInstance pc, int start, int count) {
     writeC(127);
     writeD(objid);
     writeH(count);
     writeC(3);
     L1ItemInstance item = ItemTable.getInstance().createItem(40005);
     for (int i = 0; i < count; i++) {
       item.getItem().setGfxId(start + i);
       item.getItem().setName(String.valueOf(start + i));
       item.getItem().setNameId(String.valueOf(start + i));

       writeD(item.getId());
       writeC(item.getItem().getType2());
       writeH(item.get_gfxid());
       writeC(1);
       writeD(start + i);
       writeC(item.isIdentified() ? 1 : 0);
       writeS(item.getViewName());
       writeC(0);
     }
     writeD(100);
     writeD(0);
     writeH(0);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }



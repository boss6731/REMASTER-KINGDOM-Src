 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Warehouse.SupplementaryService;
 import l1j.server.server.model.Warehouse.WarehouseManager;



 public class S_RetrieveSupplementaryService
   extends ServerBasePacket
 {
   private static final String _S_RetrieveSupplementaryService = "[S] S_RetrieveSupplementaryService";

   public S_RetrieveSupplementaryService(int objid, L1PcInstance pc) {
     SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
     writeC(127);
     writeD(objid);
     writeH(warehouse.getSize());
     writeC(21);
     writeC(1);
     writeC(1);
     for (L1ItemInstance item : warehouse.getItems()) {
       writeD(item.getId());
       writeD(item.getCount());
       writeC(1);
       writeC(1);
       writeC(0);
       writeC(1);
       writeD(-1);
       String viewName = item.getName();
       int length = viewName.length();
       writeH((length + 1) * 2);
       for (int i = 0; i < length; i++) {
         writeH(viewName.charAt(i));
       }
       writeH(0);


       writeD(0);
       writeH(0);
       writeC(0);
       writeH(item.get_gfxid());
       writeC(item.getItem().getBless());
       writeD(item.getCount());
       writeC(item.isIdentified() ? 1 : 0);
       writeC(1);
       writeS(item.getViewName());
     }
     writeD(0);
     writeD(0);
     writeH(0);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }


   public String getType() {
     return "[S] S_RetrieveSupplementaryService";
   }
 }



 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_RetrievePackageList
   extends ServerBasePacket
 {
   public boolean NonValue = false;

   public S_RetrievePackageList(int objid, L1PcInstance pc) {
     if (pc.getInventory().getSize() < 200) {
       int size = pc.getDwarfForPackageInventory().getSize();
       if (size > 0) {
         writeC(127);
         writeD(objid);
         writeH(size);
         writeC(15);
         L1ItemInstance item = null;
         for (Object itemObject : pc.getDwarfForPackageInventory().getItems()) {
           item = (L1ItemInstance)itemObject;
           writeD(item.getId());
           writeC(item.getItem().getType2());
           writeH(item.get_gfxid());
           writeC(item.getItem().getBless());
           writeD(item.getCount());
           writeC(item.isIdentified() ? 1 : 0);
           writeS(item.getViewName());
         }
         writeC(30);
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



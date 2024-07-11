 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.MJDShopSystem.MJDShopItem;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.utils.BinaryOutputStream;


 public class S_PrivateShop
   extends ServerBasePacket
 {
   public S_PrivateShop(L1PcInstance pc, int objectId, int type) {
     L1PcInstance shopPc = (L1PcInstance)L1World.getInstance().findObject(objectId);

     if (shopPc == null) {
       return;
     }

     writeC(29);
     writeC(type);
     writeD(objectId);

     if (type == 0) {

       ArrayList<MJDShopItem> list = shopPc.getSellings();
       if (list == null) {
         return;
       }
       MJDShopItem ditem = null;
       L1ItemInstance item = null;
       int size = list.size();
       pc.setPartnersPrivateShopItemCount(size);
       writeH(size);
       try {
         for (int i = 0; i < size; i++) {
           ditem = list.get(i);
           int itemObjectId = ditem.objId;
           int count = ditem.count;
           int price = ditem.price;
           item = shopPc.getInventory().getItem(itemObjectId);
           if (item != null) {
             writeC(i);
             writeD(count);
             writeD(price);
             writeH(item.getItem().getGfxId());
             writeC(item.getEnchantLevel());
             if (!item.isIdentified()) {
               writeC(0);
             } else {
               writeC(1);
             }
             writeC(item.getItem().getBless());
             writeS(item.getNumberedViewName(count));

             byte[] status = item.getStatusBytes(pc);
             writeC(status.length);
             for (byte b : status)
               writeC(b);
           }
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
       writeH(0);
     } else if (type == 1) {

       ArrayList<MJDShopItem> list = shopPc.getPurchasings();
       if (list == null) {
         return;
       }
       MJDShopItem ditem = null;
       L1ItemInstance item = null;
       L1PcInventory inv = pc.getInventory();
       int size = list.size();
       int seq = 0;
       BinaryOutputStream bos = new BinaryOutputStream();
       List<L1ItemInstance> ivlist = inv.getItems();
       int ivSize = ivlist.size();
       for (int i = 0; i < ivSize; i++) {
         item = ivlist.get(i);
         if (item != null && !item.isEquipped())
         {

           for (int j = 0; j < size; j++) {
             ditem = list.get(j);
             if (item.getItemId() == ditem.itemId)
             {

               if (item.getEnchantLevel() == ditem.enchant && item.getAttrEnchantLevel() == ditem.attr) {


                 bos.writeC(seq++);
                 if (ditem.count >= item.getCount()) {
                   bos.writeD(item.getCount());
                 } else {
                   bos.writeD(ditem.count);
                 }  bos.writeD(ditem.price);
                 bos.writeD(item.getId());
                 bos.writeC(0);
               }  }
           }  }
       }
       writeH(seq);
       writeByte(bos.getBytes());
       writeH(0);
       try {
         bos.close();
       } catch (IOException e) {
         e.printStackTrace();
       }
     }
   }


   public byte[] getContent() {
     return getBytes();
   }
 }



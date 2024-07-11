 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import java.util.Iterator;
 import javolution.util.FastMap;
 import javolution.util.FastTable;
 import l1j.server.server.datatables.ItemSelectorTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.BinaryOutputStream;







 public class S_ItemSelector
   extends ServerBasePacket
 {
   private static final String S_ITEM_SELECTOR = "[S] S_ItemSelector";
   private byte[] _byte = null;

   public static final int ITEM_SELECT = 2506;

   private static final FastMap<Integer, byte[]> TEMP_ITEM_BYTE = new FastMap();

   public S_ItemSelector(L1ItemInstance useItem) {
     writeC(19);
     writeH(2506);

     byte[] tempByte = (byte[])TEMP_ITEM_BYTE.get(Integer.valueOf(useItem.getItemId()));



     writeByte(putTempItemByte(useItem.getItemId()));


     writeC(16);
     writeBit(useItem.getId());

     writeC(24);
     writeBit(useItem.getItem().getItemDescId());

     writeH(0);
   }

   private byte[] putTempItemByte(int useitemId) {
     BinaryOutputStream os = new BinaryOutputStream();

     try {
       L1Item temp = null;
       if (ItemSelectorTable.getSelectorInfo(useitemId) != null) {
         for (ItemSelectorTable.SelectorData data : ItemSelectorTable.getSelectorInfo(useitemId)) {
           temp = ItemTable.getInstance().getTemplate(data._selectItemId);
           if (temp == null) {
             System.out.println("S_ItemSelector - SelectorData itemTempInfo empty itemId : " + data._selectItemId);
             continue;
           }
           int descId = temp.getItemDescId();
           int attrvalue = 0;
           int attrtype = 0;
           int length = 0;
           if (data._attrLevel != 0) {
             if (data._attrLevel >= 1 && data._attrLevel <= 5) {
               attrtype = 1;
               attrvalue = data._attrLevel;
             }
             if (data._attrLevel >= 6 && data._attrLevel <= 10) {
               attrtype = 2;
               attrvalue = data._attrLevel - 5;
             }
             if (data._attrLevel >= 11 && data._attrLevel <= 15) {
               attrtype = 3;
               attrvalue = data._attrLevel - 10;
             }
             if (data._attrLevel >= 16 && data._attrLevel <= 20) {
               attrtype = 4;
               attrvalue = data._attrLevel - 15;
             }
             length += 4;
           }
           if (data._enchantLevel != 0) {
             length += 2;
           }
           if (data._count != 0) {
             length += os.getBitSize(data._count) + 1;
           }
           os.writeC(10);
           os.writeC(os.getBitSize(descId) + 1 + length);
           os.writeC(8);
           os.writeBit(descId);
           if (data._attrLevel != 0) {
             os.writeC(16);
             os.writeC(attrtype);
             os.writeC(24);
             os.writeC(attrvalue);
           }
           if (data._enchantLevel != 0) {
             os.writeC(32);
             os.writeC(data._enchantLevel);
           }
           if (data._count != 0) {
             os.writeC(40);
             os.writeC(data._count);
           }
         }
         TEMP_ITEM_BYTE.put(Integer.valueOf(useitemId), os.getBytes());
         return os.getBytes();
       }
       System.out.println(ItemSelectorTable.getSelectorInfo(useitemId));
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       if (os != null) {
         try {
           os.close();
           os = null;
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
     }
     return null;
   }

   public S_ItemSelector(L1ItemInstance useItem, FastTable<Integer> list) {
     writeC(19);
     writeH(2506);

     writeByte(getTempItemByte(list));

     writeC(16);
     writeBit(useItem.getId());

     writeC(24);
     writeBit(useItem.getItem().getItemDescId());

     writeH(0);
   }

   private byte[] getTempItemByte(FastTable<Integer> list) {
     BinaryOutputStream os = new BinaryOutputStream();
     try {
       L1Item temp = null;
       for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext(); ) { int itemIds = ((Integer)iterator.next()).intValue();
         temp = ItemTable.getInstance().getTemplate(itemIds);
         if (temp == null) {
           System.out.println("S_ItemSelector - SelectorData melt empty itemId : " + itemIds);
           continue;
         }
         int descId = temp.getItemDescId();
         os.writeC(10);
         os.writeC(os.getBitSize(descId) + 1);

         os.writeC(8);
         os.writeBit(descId); }

       return os.getBytes();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       if (os != null) {
         try {
           os.close();
           os = null;
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
     }
     return null;
   }


   public S_ItemSelector(L1PcInstance pc, int useItemId, FastTable<ItemSelectorTable.SelectorData> list) {
     writeC(19);
     writeH(1032);

     writeC(8);
     writeBit(useItemId);

     writeC(16);
     writeBit(list.size());

     writeC(24);
     writeBit(3L);

     writeC(32);
     writeBit(1L);

     writeC(40);
     writeBit(180L);

     for (ItemSelectorTable.SelectorData data : list) {
       if (data == null || data._item == null) {
         System.out.println("S_ItemSelector - SelectorWarehouseData empty itemId : " + data._itemId);
         continue;
       }
       writeC(50);
       writeBytesWithLength(itemDetailBytes(pc, data._index, data._item));
     }

     writeC(56);
     writeC(1);

     writeH(0);
   }


   private byte[] itemDetailBytes(L1PcInstance pc, int index, L1ItemInstance item) {
     BinaryOutputStream os = new BinaryOutputStream();
     os.writeC(8);
     os.writeBit(index);
     os.writeC(18);
     os.writeBytesWithLength(pc.sendItemPacket(pc, item));
     try {
       os.close();
     } catch (IOException e) {
       e.printStackTrace();
     }
     return os.getBytes();
   }



   public byte[] getContent() {
     if (this._byte == null) this._byte = this._bao.toByteArray();
     return this._byte;
   }


   public String getType() {
     return "[S] S_ItemSelector";
   }
 }



     package l1j.server.server.serverpackets;
     import java.util.ArrayList;
     import java.util.Map;
     import java.util.concurrent.ConcurrentHashMap;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
     import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
     import l1j.server.server.model.item.collection.favor.bean.L1FavorBookRegistObject;
     import l1j.server.server.model.item.collection.favor.bean.L1FavorBookTypeObject;
     import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
     import l1j.server.server.model.item.collection.favor.construct.L1FavorBookInventoryStatus;
     import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
     import l1j.server.server.utils.BinaryOutputStream;
     import l1j.server.server.utils.StringUtil;

     public class S_FavorBook extends ServerBasePacket {
       private static final String S_FAVOR_BOOK = "[S] S_FavorBook";
       private byte[] _byte;

       public S_FavorBook(L1PcInstance pc, L1FavorBookInventory favorInv, L1FavorBookListType listType) {
         ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>>> all;
         ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> map;
         this._byte = null;





         writeC(19);
         writeH(2650);

         switch (listType) {
           case ALL:
             all = L1FavorBookLoader.getAllData();
             if (all != null && !all.isEmpty()) {
               ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> value = null;
               for (Map.Entry<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>>> entry : all.entrySet()) {
                 value = entry.getValue();
                 if (value == null || value.isEmpty()) {
                   continue;
                 }
                 ArrayList<L1FavorBookObject> list = null;
                 for (Map.Entry<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> entry2 : value.entrySet()) {
                   list = entry2.getValue();
                   if (list == null || list.isEmpty()) {
                     continue;
                   }
                   writeC(10);
                   writeBytesWithLength(getListBytes(pc, favorInv, entry.getKey(), entry2.getKey(), list));
                 }
               }
             }
             break;
           default:
             map = L1FavorBookLoader.getListType(listType);
             if (map != null && !map.isEmpty()) {
               for (Map.Entry<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> entry : map.entrySet()) {
                 writeC(10);
                 writeBytesWithLength(getListBytes(pc, favorInv, listType, entry.getKey(), entry.getValue()));
               }
             }
             break;
         }

         writeH(0);
       } public static final int LIST = 2650; public static final int INVENTORY = 2652;
       public S_FavorBook(L1PcInstance pc, L1FavorBookUserObject user, L1FavorBookInventoryStatus status) {
         this._byte = null;
         writeC(19);
         writeH(2652);

         writeC(8);
         writeC(0);

         writeC(18);
         writeBytesWithLength(getInventoryBytes(pc, user));

         writeC(24);
         writeC(status.getStatus());

         writeH(0);
       }

       private byte[] getListBytes(L1PcInstance pc, L1FavorBookInventory favorInv, L1FavorBookListType firstKey, L1FavorBookTypeObject type, ArrayList<L1FavorBookObject> list) {
         BinaryOutputStream os = null;
         try {
           os = new BinaryOutputStream();
           os.writeC(10);
           os.writeBytesWithLength(type.getDesc().getBytes());

           os.writeC(18);
           if (StringUtil.isNullOrEmpty(type.getStartTimeToString())) {
             os.writeC(0);
           } else {
             os.writeBytesWithLength(type.getStartTimeToString().getBytes());
           }

           os.writeC(26);
           if (StringUtil.isNullOrEmpty(type.getEndTimeToString())) {
             os.writeC(0);
           } else {
             os.writeBytesWithLength(type.getEndTimeToString().getBytes());
           }

           os.writeC(32);
           os.writeC(firstKey.getType());

           for (L1FavorBookObject obj : list) {
             os.writeC(42);
             os.writeBytesWithLength(getListItemBytes(pc, favorInv, obj));
           }
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

       private byte[] getListItemBytes(L1PcInstance pc, L1FavorBookInventory favorInv, L1FavorBookObject obj) {
         BinaryOutputStream os = null;
         try {
           os = new BinaryOutputStream();
           L1FavorBookUserObject user = favorInv.getFavorUser(obj.getListType(), obj.getType(), obj.getIndex());
           writeDetail(os, pc, obj, (user == null) ? null : user.getCurrentItem());
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

       private byte[] getInventoryBytes(L1PcInstance pc, L1FavorBookUserObject user) {
         BinaryOutputStream os = null;
         try {
           os = new BinaryOutputStream();
           writeDetail(os, pc, user.getObj(), user.getCurrentItem());
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

       private void writeDetail(BinaryOutputStream os, L1PcInstance pc, L1FavorBookObject obj, L1ItemInstance item) {
         int descId = (item == null) ? 0 : item.getItem().getItemDescId();
         int bless = (item == null) ? -1 : item.getBless();
         L1FavorBookRegistObject regist = obj.getRegister(descId, bless);

         os.writeC(8);
         os.writeC(obj.getType().getType());

         os.writeC(16);
         os.writeC(obj.getIndex());

         os.writeC(24);
         os.writeBit(descId);

         os.writeC(32);
         os.writeBit((regist == null) ? 0L : regist.getCraftId());

         if (item != null) {
           os.writeC(40);
           os.writeC((regist == null) ? L1FavorBookItemStatus.CRAFT.getStatus() : regist.getStatus().getStatus());

           byte[] itemBytes = pc.sendItemPacket(pc, item);
           int itemLength = itemBytes.length + 12 + os.getBitSize(item.getBless()) + os.getBitSize(item.getEnchantLevel()) + os.getBitSize(item.getItem().getWeight());
           os.writeC(58);
           os.writeBit(itemLength);
           os.writeByte(itemBytes);

           os.writeC(176);
           os.writeC(1);
           os.writeBit(item.getBless());

           os.writeC(184);
           os.writeC(1);
           os.writeBit(item.getEnchantLevel());

           os.writeC(192);
           os.writeC(1);
           os.writeC(item.getItem().isStackable() ? 1 : 0);

           os.writeC(200);
           os.writeC(1);
           os.writeBit(item.getItem().getWeight());

           os.writeC(208);
           os.writeC(1);
           os.writeC(item.isIdentified() ? 1 : 0);
         }
       }


       public byte[] getContent() {
         if (this._byte == null) {
           this._byte = this._bao.toByteArray();
         }
         return this._byte;
       }


       public String getType() {
         return "[S] S_FavorBook";
       }
     }



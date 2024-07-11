 package l1j.server.server.model.item.collection.favor;

 import java.util.ArrayList;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.Config;
 import l1j.server.InvenBonusItem.InvenBonusItemInfo;
 import l1j.server.InvenBonusItem.InvenBonusItemLoader;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookTypeObject;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
 import l1j.server.server.model.item.collection.favor.construct.L1FavorBookInventoryStatus;
 import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
 import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
 import l1j.server.server.serverpackets.S_FavorBook;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1FavorBookInventory
 {
   private final L1PcInstance owner;
   private final ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>>> mapData;
   private final ArrayList<L1FavorBookUserObject> listData;
   private final ConcurrentHashMap<L1FavorBookListType, ServerBasePacket> packets;
   private L1ItemInstance craftBoxOpenResult;

   public L1ItemInstance getCraftBoxOpenResult() {
     return this.craftBoxOpenResult;
   }
   public void setCraftBoxOpenResult(L1ItemInstance item) {
     this.craftBoxOpenResult = item;
   }

   public L1FavorBookInventory(L1PcInstance owner) {
     this.owner = owner;
     this.mapData = new ConcurrentHashMap<>();
     this.listData = new ArrayList<>();
     this.packets = new ConcurrentHashMap<>();
     init();
   }

   public ServerBasePacket getListPacket(L1FavorBookListType listType) {
     return this.packets.get(listType);
   }

   public ArrayList<L1FavorBookUserObject> getList() {
     return this.listData;
   }

   public ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>>> getData() {
     return this.mapData;
   }

   public ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> getListTypeMap(L1FavorBookListType listType) {
     return this.mapData.get(listType);
   }

   public ConcurrentHashMap<Integer, L1FavorBookUserObject> getTypeMap(L1FavorBookListType listType, L1FavorBookTypeObject type) {
     ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> listMap = getListTypeMap(listType);
     if (listMap == null) {
       return null;
     }
     return listMap.get(type);
   }

   public L1FavorBookUserObject getFavorUser(L1FavorBookListType listType, L1FavorBookTypeObject type, int index) {
     ConcurrentHashMap<Integer, L1FavorBookUserObject> typeMap = getTypeMap(listType, type);
     if (typeMap == null) {
       return null;
     }
     return typeMap.get(Integer.valueOf(index));
   }

   private L1FavorBookUserObject create(L1FavorBookObject favor) {
     L1FavorBookUserObject user = new L1FavorBookUserObject(favor.getListType(), favor.getType(), favor.getIndex(), favor);
     ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> listTypeMap = this.mapData.get(user.getListType());
     if (listTypeMap == null) {
       listTypeMap = new ConcurrentHashMap<>();
       this.mapData.put(user.getListType(), listTypeMap);
     }

     ConcurrentHashMap<Integer, L1FavorBookUserObject> typeMap = listTypeMap.get(user.getType());
     if (typeMap == null) {
       typeMap = new ConcurrentHashMap<>();
       listTypeMap.put(user.getType(), typeMap);
     }
     typeMap.put(Integer.valueOf(user.getIndex()), user);
     this.listData.add(user);
     return user;
   }


   public boolean registFavor(L1FavorBookObject favor, L1ItemInstance item) {
     if (!Config.ServerAdSetting.FavorSystem) {
       return false;
     }
     L1FavorBookUserObject user = getFavorUser(favor.getListType(), favor.getType(), favor.getIndex());
     if (user == null) {
       user = create(favor);
     }

     if (item.getBless() >= 128) {
       item.setBless(item.getBless() - 128);
     }
     item.setIdentified(true);
     user.setCurrentItem(item);
     if (L1FavorBookUserLoader.getInstance().insert(this.owner, user)) {
       this.owner.sendPackets((ServerBasePacket)new S_FavorBook(this.owner, user, L1FavorBookInventoryStatus.STORE), true);

       updateListPacket();
       return true;
     }
     return false;
   }


   public void pollFavor(L1FavorBookUserObject user, L1ItemInstance favorItem, int pollCount) {
     int afterCount = favorItem.getCount() - pollCount;
     if (afterCount > 0) {
       favorItem.setCount(afterCount);

       return;
     }
     InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(favorItem.getItemId());
     if (info != null) {
       InvenBonusItemInfo.inven_option(this.owner, info.get_item_id(), false);
     }
     if (L1FavorBookUserLoader.getInstance().delete(this.owner, user)) {
       ConcurrentHashMap<Integer, L1FavorBookUserObject> map = getTypeMap(user.getListType(), user.getType());
       if (map == null) {
         return;
       }
       map.remove(Integer.valueOf(user.getIndex()));
       this.listData.remove(user);

       updateListPacket();
     }
   }


   public void deleteFavor(L1FavorBookUserObject user) {
     L1ItemInstance favorItem = user.getCurrentItem();
     if (favorItem != null) {

       InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(favorItem.getItemId());
       if (info != null) {
         InvenBonusItemInfo.inven_option(this.owner, info.get_item_id(), false);
       }
     }
     if (L1FavorBookUserLoader.getInstance().delete(this.owner, user)) {
       ConcurrentHashMap<Integer, L1FavorBookUserObject> map = getTypeMap(user.getListType(), user.getType());
       if (map == null) {
         return;
       }
       map.remove(Integer.valueOf(user.getIndex()));
       this.listData.remove(user);

       updateListPacket();
     }
   }


   private void updateListPacket() {
     clearListPacket();
     this.packets.put(L1FavorBookListType.ALL, new S_FavorBook(this.owner, this, L1FavorBookListType.ALL));
     this.packets.put(L1FavorBookListType.RELIC, new S_FavorBook(this.owner, this, L1FavorBookListType.RELIC));
     this.packets.put(L1FavorBookListType.EVENT, new S_FavorBook(this.owner, this, L1FavorBookListType.EVENT));
   }


   private void clearListPacket() {
     ServerBasePacket pck = this.packets.get(L1FavorBookListType.ALL);
     if (pck != null) {
       pck.clear();
       pck = null;
     }
     pck = this.packets.get(L1FavorBookListType.RELIC);
     if (pck != null) {
       pck.clear();
       pck = null;
     }
     pck = this.packets.get(L1FavorBookListType.EVENT);
     if (pck != null) {
       pck.clear();
       pck = null;
     }
     this.packets.clear();
   }



   private void init() {
     ArrayList<L1FavorBookUserObject> userList = L1FavorBookUserLoader.getFavorUserList(this.owner.getId());
     if (userList != null && !userList.isEmpty()) {
       for (L1FavorBookUserObject user : userList) {
         ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> listTypeMap = this.mapData.get(user.getListType());
         if (listTypeMap == null) {
           listTypeMap = new ConcurrentHashMap<>();
           this.mapData.put(user.getListType(), listTypeMap);
         }

         ConcurrentHashMap<Integer, L1FavorBookUserObject> typeMap = listTypeMap.get(user.getType());
         if (typeMap == null) {
           typeMap = new ConcurrentHashMap<>();
           listTypeMap.put(user.getType(), typeMap);
         }


         InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(user.getCurrentItem().getItemId());
         if (info != null) {
           InvenBonusItemInfo.inven_option(this.owner, info.get_item_id(), true);
         }

         typeMap.put(Integer.valueOf(user.getIndex()), user);
         this.listData.add(user);
       }
     }
     updateListPacket();
     sendLoginPacket();
   }


   private void sendLoginPacket() {
     if (!this.listData.isEmpty()) {
       for (L1FavorBookUserObject user : this.listData) {
         this.owner.sendPackets((ServerBasePacket)new S_FavorBook(this.owner, user, L1FavorBookInventoryStatus.LIST), true);
       }
     }
   }



   public void dispose() {
     this.mapData.clear();
     this.listData.clear();
     clearListPacket();
   }
 }



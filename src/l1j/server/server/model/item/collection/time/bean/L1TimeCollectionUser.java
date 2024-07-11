 package l1j.server.server.model.item.collection.time.bean;

 import java.sql.Timestamp;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.item.collection.time.L1TimeCollectionTimer;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;


 public class L1TimeCollectionUser
 {
   private int charObjId;
   private int flag;
   private L1TimeCollectionType type;
   private int collectionIndex;
   private ConcurrentHashMap<Integer, L1ItemInstance> registItem;
   private boolean registComplet;
   private int sumEnchant;
   private L1TimeCollectionBuffType buffType;
   private Timestamp buffTime;
   private L1TimeCollectionTimer buffTimer;
   private L1TimeCollectionAblity ablity;
   private int buffIndex;
   private L1TimeCollection obj;
   private int refill_count;

   public L1TimeCollectionUser(int charObjId, int flag, L1TimeCollectionType type, int collectionIndex, ConcurrentHashMap<Integer, L1ItemInstance> registItem, boolean registComplet, int sumEnchant, L1TimeCollectionBuffType buffType, Timestamp buffTime, L1TimeCollection obj, int refill_count) {
     this.charObjId = charObjId;
     this.flag = flag;
     this.type = type;
     this.collectionIndex = collectionIndex;
     this.registItem = registItem;
     this.registComplet = registComplet;
     this.sumEnchant = sumEnchant;
     this.buffType = buffType;
     this.buffTime = buffTime;
     this.obj = obj;
     this.refill_count = refill_count;
   }

   public int getRefill_count() {
     return this.refill_count;
   }
   public void setRefill_count(int i) {
     this.refill_count = i;
   }

   public int getCharObjId() {
     return this.charObjId;
   }
   public void setCharObjId(int charObjId) {
     this.charObjId = charObjId;
   }

   public int getFlag() {
     return this.flag;
   }
   public void setFlag(int flag) {
     this.flag = flag;
   }

   public L1TimeCollectionType getType() {
     return this.type;
   }
   public void setType(L1TimeCollectionType type) {
     this.type = type;
   }

   public int getCollectionIndex() {
     return this.collectionIndex;
   }
   public void setCollectionIndex(int collectionIndex) {
     this.collectionIndex = collectionIndex;
   }

   public ConcurrentHashMap<Integer, L1ItemInstance> getRegistItem() {
     return this.registItem;
   }
   public void setRegistItem(ConcurrentHashMap<Integer, L1ItemInstance> registItem) {
     this.registItem = registItem;
   }
   public void putRegistItem(int slotIndex, L1ItemInstance item) {
     if (this.registItem == null) {
       this.registItem = new ConcurrentHashMap<>();
     }
     this.registItem.put(Integer.valueOf(slotIndex), item);
   }

   public boolean isRegistComplet() {
     return this.registComplet;
   }
   public void setRegistComplet(boolean registComplet) {
     this.registComplet = registComplet;
   }

   public int getSumEnchant() {
     return this.sumEnchant;
   }
   public void setSumEnchant(int sumEnchant) {
     this.sumEnchant = sumEnchant;
   }

   public L1TimeCollectionBuffType getBuffType() {
     return this.buffType;
   }
   public void setBuffType(L1TimeCollectionBuffType buffType) {
     this.buffType = buffType;
   }

   public Timestamp getBuffTime() {
     return this.buffTime;
   }
   public void setBuffTime(Timestamp buffTime) {
     this.buffTime = buffTime;
   }

   public void addBuffTime(long time) {
     long bufftimelong = Timestamp.valueOf(this.buffTime.toString()).getTime();
     long addedTimelong = bufftimelong + time;
     Timestamp addedTime = new Timestamp(addedTimelong);
     this.buffTime = addedTime;
   }

   public boolean isBuffActive() {
     return (this.buffTime != null && this.buffTimer != null);
   }
   public long restBuffTime() {
     if (this.buffTime == null) {
       return 0L;
     }
     return this.buffTime.getTime() - System.currentTimeMillis();
   }

   public L1TimeCollectionTimer getBuffTimer() {
     return this.buffTimer;
   }
   public void setBuffTimer(L1TimeCollectionTimer buffTimer) {
     this.buffTimer = buffTimer;
   }

   public L1TimeCollectionAblity getAblity() {
     return this.ablity;
   }
   public void setAblity(L1TimeCollectionAblity ablity) {
     this.ablity = ablity;
   }

   public int getBuffIndex() {
     return this.buffIndex;
   }
   public void setBuffIndex(int buffIndex) {
     this.buffIndex = buffIndex;
   }

   public L1TimeCollection getObj() {
     return this.obj;
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("charObjId: ").append(this.charObjId).append("\r\n");
     sb.append("flag: ").append(this.flag).append("\r\n");
     sb.append("type: ").append(this.type.getName()).append("\r\n");
     sb.append("collectionIndex: ").append(this.collectionIndex).append("\r\n");
     sb.append("registItem size: ").append(this.registItem.size()).append("\r\n");
     sb.append("registComplet: ").append(this.registComplet).append("\r\n");
     sb.append("sumEnchant: ").append(this.sumEnchant).append("\r\n");
     sb.append("buffType: ").append(this.buffType.getName()).append("\r\n");
     sb.append("buffTime: ").append(this.buffTime).append("\r\n");
     sb.append("isBuffActive: ").append(isBuffActive()).append("\r\n");
     sb.append("ablity: ").append(this.ablity).append("\r\n");
     sb.append("buffIndex: ").append(this.buffIndex).append("\r\n");
     return sb.toString();
   }
 }



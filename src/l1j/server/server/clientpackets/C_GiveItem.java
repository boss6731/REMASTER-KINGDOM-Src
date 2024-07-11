 package l1j.server.server.clientpackets;

 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import java.util.Calendar;
 import java.util.Random;
 import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.PetTypeTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1Inventory;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_ItemName;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1PetType;


 public class C_GiveItem
   extends ClientBasePacket
 {
   private static final String C_GIVE_ITEM = "[C] C_GiveItem";
   Calendar rightNow = Calendar.getInstance();
   int day = this.rightNow.get(5);
   int hour = this.rightNow.get(10);
   int min = this.rightNow.get(12);
   int year = this.rightNow.get(1);
   int month = this.rightNow.get(2) + 1;
   String totime = "[" + this.year + ":" + this.month + ":" + this.day + ":" + this.hour + ":" + this.min + "]";

   public C_GiveItem(byte[] decrypt, GameClient client) {
     super(decrypt);
     int targetId = readD();


     readH();
     readH();
     int itemId = readD();
     int count = readD();

     L1PcInstance pc = client.getActiveChar();

     if (pc == null || pc.isGhost() || pc.isTwoLogin(pc)) {
       return;
     }

     if (MJShiftBattlePlayManager.is_shift_battle(pc)) {
       return;
     }
     L1Object object = L1World.getInstance().findObject(targetId);
     if (object == null || !(object instanceof L1NpcInstance)) {
       return;
     }

     L1NpcInstance target = (L1NpcInstance)object;
     L1Inventory targetInv = target.getInventory();
     L1PcInventory l1PcInventory = pc.getInventory();
     L1ItemInstance item = l1PcInventory.getItem(itemId);
     if (item == null) {
       return;
     }
     if (!isNpcItemReceivable(target.getNpcTemplate()) && (
       item.getItem().getItemId() != 40499 || item.getItem().getItemId() != 40507)) {
       return;
     }

     if (item.isEquipped()) {
       pc.sendPackets(141);
       return;
     }
     if (item.getBless() >= 128) {
       pc.sendPackets(141);
       return;
     }
     if (item.getItemId() == 22229 || item.getItemId() == 22230 || item.getItemId() == 22231 || item
       .getItemId() == 22215 || item.getItemId() == 22216 || item.getItemId() == 22217 || item
       .getItemId() == 22218 || item.getItemId() == 22219 || item.getItemId() == 22220 || item
       .getItemId() == 22221 || item.getItemId() == 22225 || item.getItemId() == 22222 || item
       .getItemId() == 22226 || item.getItemId() == 22223 || item.getItemId() == 22227 || item
       .getItemId() == 22224 || item.getItemId() == 22228 || item.getItemId() == 900195) {
       return;
     }

       // 檢查物品ID是否匹配
       if (itemId != item.getId()) {
           pc.sendPackets((ServerBasePacket) new S_Disconnect());
           return;
       }

            // 檢查不可堆疊物品的數量是否為1
       if (!item.isStackable() && count != 1) {
           pc.sendPackets((ServerBasePacket) new S_Disconnect());
           return;
       }

            // 檢查物品數量和轉移數量是否大於0
       if (item.getCount() <= 0 || count <= 0) {
           pc.sendPackets((ServerBasePacket) new S_Disconnect());
           return;
       }

            // 如果轉移數量大於物品數量，則將轉移數量設置為物品數量
       if (count >= item.getCount()) {
           count = item.getCount();
       }

            // 檢查物品是否刻印，如果刻印則不允許轉移
       if (item.get_Carving() != 0) {
           pc.sendPackets((ServerBasePacket) new S_SystemMessage("刻印的物品不能轉移。"));
           return;
       }
     if (!item.getItem().isTradable() || item.getItemId() == 40308) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));
       return;
     }
     if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

       return;
     }
     L1PetInstance pet = null;
     for (Object petObject : pc.getPetList().values()) {
       if (petObject instanceof L1PetInstance) {
         pet = (L1PetInstance)petObject;
         if (item.getId() == pet.getItemObjId()) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           return;
         }
       }
     }
     L1DollInstance doll = pc.getMagicDoll();
     if (doll != null &&
       item.getId() == doll.getItemObjId()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

       return;
     }

     if (!pc.isGm() && targetInv.checkAddItem(item, count) != 0) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(942));

       return;
     }
     L1PetType petType = PetTypeTable.getInstance().get(target.getNpcTemplate().get_npcId());
     if (petType != null && (
       petType.getBaseNpcId() == 46046 || petType.getItemIdForTaming() != 0) && item
       .getItem().isUseHighPet()) {
       return;
     }


     item.setGiveItem(true);
     item = l1PcInventory.tradeItem(item, count, targetInv);
     target.onGetItem(item);
     target.getLight().turnOnOffLight();
     pc.getLight().turnOnOffLight();

     if (petType == null || target.isDead()) {
       return;
     }
     if ((item.getItemId() == petType.getItemIdForTaming() && item
       .getItemId() == 490026 && target.getNpcTemplate().get_npcId() == 45711) || (item
       .getItemId() == 490027 && target.getNpcTemplate().get_npcId() == 45313)) {
       Random _rnd = new Random();
       if (item.getItemId() >= 490024 && item.getItemId() <= 490027) {
         int value = _rnd.nextInt(100) + 1;
         value += (item.getItemId() == 490026 || item.getItemId() == 490027) ? 20 : 0;
         if (value > 75) {
           tamePet(pc, target);
         }
       }
     }

     if (item.getItemId() == petType.getItemIdForTaming()) {
       tamePet(pc, target);
     } else if (item.getItemId() == 40070 && petType.getItemIdForTaming() != 0) {
       evolvePet(pc, target, item.getItemId());
     } else if (item.getItemId() == 41310 && petType.canEvolve() && petType.getItemIdForTaming() == 0) {
       evolvePet(pc, target, item.getItemId());
     }
   }

   private static final String[] receivableImpls = new String[] { "L1Npc", "L1Monster", "L1Guardian", "L1Teleporter", "L1Guard" };





   private boolean isNpcItemReceivable(L1Npc npc) {
     for (String impl : receivableImpls) {
       if (npc.getImpl().equals(impl)) {
         return true;
       }
     }
     return false;
   }

   private void tamePet(L1PcInstance pc, L1NpcInstance target) {
     if (target instanceof L1PetInstance || target instanceof l1j.server.server.model.Instance.L1SummonInstance) {
       return;
     }

     int petcost = 0;
     Object[] petlist = pc.getPetList().values().toArray();
     for (Object pet : petlist) {
       petcost += ((L1NpcInstance)pet).getPetcost();
     }
     int charisma = pc.getAbility().getTotalCha();
     if (pc.isCrown()) {
       charisma += 6;
     } else if (pc.isElf()) {
       charisma += 12;
     } else if (pc.isWizard()) {
       charisma += 6;
     } else if (pc.isDarkelf()) {
       charisma += 6;
     } else if (pc.isDragonknight()) {
       charisma += 6;
     } else if (pc.isBlackwizard()) {
       charisma += 6;
     }
     charisma -= petcost;

       // 獲取玩家的物品欄
       L1PcInventory inv = pc.getInventory();
            // 獲取目標NPC的名字
       String npcname = target.getNpcTemplate().get_name();

            // 檢查玩家魅力值和物品欄大小是否滿足條件
       if (charisma >= 6 && inv.getSize() < 200) {
           // 檢查目標NPC是否可捕捉為寵物
           if (isTamePet(target)) {
               // 向玩家物品欄中添加寵物護身符
               L1ItemInstance petamu = inv.storeItem(40314, 1);
               if (petamu != null) {
                   // 創建新的寵物實例
                   new L1PetInstance(target, pc, petamu.getId());
                   // 發送寵物護身符的名稱信息給玩家
                   pc.sendPackets((ServerBasePacket) new S_ItemName(petamu));
                   // 發送系統消息通知玩家獲得了寵物護身符
                   pc.sendPackets((ServerBasePacket) new S_SystemMessage(npcname + " 的護身符已獲得。"));
               }
           } else {
               // 如果目標NPC不可捕捉，發送通知玩家的系統消息
               pc.sendPackets((ServerBasePacket) new S_ServerMessage(324)); // "You cannot tame that creature."
           }
       }

   private void evolvePet(L1PcInstance pc, L1NpcInstance target, int itemId) {
     if (!(target instanceof L1PetInstance)) {
       return;
     }
       // 獲取玩家的物品欄
       L1PcInventory inv = pc.getInventory();
            // 獲取目標寵物
       L1PetInstance pet = (L1PetInstance) target;
            // 獲取玩家擁有的對應寵物的護身符
       L1ItemInstance petamu = inv.getItem(pet.getItemObjId());
            // 獲取寵物的名字
       String npcname = target.getNpcTemplate().get_name();

            // 檢查寵物的進化條件
       if (pet.getLevel() >= 30 && pc == pet.getMaster() && petamu != null) {
           // 在玩家物品欄中添加進化後的新護身符
           L1ItemInstance highpetamu = inv.storeItem(40316, 1);
           if (highpetamu != null) {
               // 進化寵物並使用新護身符的ID
               pet.evolvePet(highpetamu.getId());
               // 發送更新後的新護身符信息給玩家
               pc.sendPackets((ServerBasePacket) new S_ItemName(highpetamu));
               // 移除舊的護身符
               inv.removeItem(petamu, 1);
               // 發送進化成功的系統消息給玩家
               pc.sendPackets((ServerBasePacket) new S_SystemMessage(npcname + " 的進化成功了。"));
           }
       } else {
           // 發送進化條件不足的系統消息給玩家
           pc.sendPackets((ServerBasePacket) new S_SystemMessage(npcname + " 的進化條件未滿足。"));
       }

   private boolean isTamePet(L1NpcInstance npc) {
     boolean isSuccess = false;
     int npcId = npc.getNpcTemplate().get_npcId();
     if (npcId == 45313 || npcId == 45711) {
       isSuccess = true;
     }
     else if (npc.getMaxHp() / 2 > npc.getCurrentHp()) {
       isSuccess = true;
     }


     if ((npcId == 45313 || npcId == 45044 || npcId == 45711) &&
       npc.isResurrect()) {
       isSuccess = false;
     }

     return isSuccess;
   }


   public String getType() {
     return "[C] C_GiveItem";
   }
 }



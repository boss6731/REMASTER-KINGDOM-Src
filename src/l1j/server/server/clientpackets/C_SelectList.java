 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.datatables.PetTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1Pet;





 public class C_SelectList
   extends ClientBasePacket
 {
   private static final String C_SELECT_LIST = "[C] C_SelectList";

   public C_SelectList(byte[] abyte0, GameClient clientthread) {
     super(abyte0);

     int itemObjectId = readD();
     int npcObjectId = readD();
     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null)
       return;
     if (npcObjectId != 0) {
       L1Object obj = L1World.getInstance().findObject(npcObjectId);
       if (obj != null &&
         obj instanceof L1NpcInstance) {
         L1NpcInstance npc = (L1NpcInstance)obj;
         int difflocx = Math.abs(pc.getX() - npc.getX());
         int difflocy = Math.abs(pc.getY() - npc.getY());

         if (difflocx > 3 || difflocy > 3) {
           return;
         }
       }


       L1PcInventory pcInventory = pc.getInventory();
       L1ItemInstance item = pcInventory.getItem(itemObjectId);
       int cost = item.get_durability() * 200;
       if (!pc.getInventory().consumeItem(40308, cost)) {
         return;
       }
       item.set_durability(0);
       pcInventory.updateItem(item, 1);
     } else {
       int petCost = 0;
       Object[] petList = pc.getPetList().values().toArray();
       for (Object pet : petList) {
         petCost += ((L1NpcInstance)pet).getPetcost();
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
       charisma -= petCost;
       int petCount = charisma / 6;
       if (petCount <= 0) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(489));

         return;
       }
       L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
       if (l1pet != null) {
         L1Npc npcTemp = NpcTable.getInstance().getTemplate(l1pet
             .get_npcid());
         L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
         pet.setPetcost(6);
       }
     }
   }


   public String getType() {
     return "[C] C_SelectList";
   }
 }



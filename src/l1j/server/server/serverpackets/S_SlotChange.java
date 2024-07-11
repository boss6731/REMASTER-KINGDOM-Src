 package l1j.server.server.serverpackets;

 import java.util.List;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_SlotChange
   extends ServerBasePacket {
   private static final String S_SlotChange = "S_SlotChange";
   public static final int SLOT_CHANGE = 32;

   public S_SlotChange(int type, L1PcInstance pc) {
     List<Integer> one, two, three, four;
     int slotsize, size, slotnum;
     writeC(19);
     writeC(type);
     switch (type) {
       case 32:
         one = pc.getSlotItems(0);
         two = pc.getSlotItems(1);
         three = pc.getSlotItems(2);
         four = pc.getSlotItems(3);
         slotsize = 4;
         size = 6;
         writeC(3);
         writeC(8);
         writeBit(pc.getSlotNumber());
         for (slotnum = 0; slotnum < slotsize; slotnum++) {
           int namesize = (pc.get_slot_color() != null && pc.get_slot_info(slotnum) != null) ? (pc.get_slot_info(slotnum).get_Slotname().getBytes()).length : 0;
           writeC(18);
           switch (slotnum) {
             case 0:
               writeBit((one.size() > 0) ? (size * one.size() + 6 + namesize) : (6 + namesize));
               writeC(8);
               writeC(slotnum);
               if (one.size() > 0) {
                 for (int i = 0; i < one.size(); i++) {
                   writeC(16);
                   writeBit(((Integer)one.get(i)).intValue());
                 }
               }
               break;
             case 1:
               writeBit((two.size() > 0) ? (size * two.size() + 6 + namesize) : (6 + namesize));
               writeC(8);
               writeC(slotnum);
               if (two.size() > 0) {
                 for (int i = 0; i < two.size(); i++) {
                   writeC(16);
                   writeBit(((Integer)two.get(i)).intValue());
                 }
               }
               break;
             case 2:
               writeBit((three.size() > 0) ? (size * three.size() + 6 + namesize) : (6 + namesize));
               writeC(8);
               writeC(slotnum);
               if (three.size() > 0) {
                 for (int i = 0; i < three.size(); i++) {
                   writeC(16);
                   writeBit(((Integer)three.get(i)).intValue());
                 }
               }
               break;
             case 3:
               writeBit((four.size() > 0) ? (size * four.size() + 6 + namesize) : (6 + namesize));
               writeC(8);
               writeC(slotnum);
               if (four.size() > 0) {
                 for (int i = 0; i < four.size(); i++) {
                   writeC(16);
                   writeBit(((Integer)four.get(i)).intValue());
                 }
               }
               break;
           }
           if (pc.get_slot_color() != null && pc.get_slot_info(slotnum) != null) {
             writeC(26);
             if (!pc.get_slot_info(slotnum).get_Slotname().equalsIgnoreCase("") && pc.get_slot_info(slotnum).get_Slotname() != null) {
               writeC((pc.get_slot_info(slotnum).get_Slotname().getBytes()).length);
               writeByte(pc.get_slot_info(slotnum).get_Slotname().getBytes());
             } else {
               writeS("");
             }
             writeC(32);
             writeC(pc.get_slot_info(slotnum).get_Color());
           } else {
             writeC(26);
             writeS("");
             writeC(32);
             writeC(0);
           }
         }
         writeC(24);
         writeC(2);
         writeC(32);
         writeC(70);
         writeH(0);
         break;
     }
   }

   public S_SlotChange(int type, int slot) {
     writeC(19);
     writeC(type);
     switch (type) {
       case 32:
         writeC(3);
         writeC(8);
         writeBit(slot);
         break;
     }

     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "S_SlotChange";
   }
 }



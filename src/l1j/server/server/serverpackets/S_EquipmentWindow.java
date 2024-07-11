     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_EquipmentWindow
       extends ServerBasePacket {
       private static final String S_EQUIPMENTWINDOWS = "[S] S_EquipmentWindow";
       public static final byte EQUIPMENT_INDEX_HEML = 1;
       public static final byte EQUIPMENT_INDEX_ARMOR = 2;
       public static final byte EQUIPMENT_INDEX_T = 3;
       public static final byte EQUIPMENT_INDEX_CLOAK = 4;
       public static final byte EQUIPMENT_INDEX_PAIR = 5;
       public static final byte EQUIPMENT_INDEX_BOOTS = 6;
       public static final byte EQUIPMENT_INDEX_GLOVE = 7;
       public static final byte EQUIPMENT_INDEX_SHIELD = 8;
       public static final byte EQUIPMENT_INDEX_WEAPON = 9;
       public static final byte EQUIPMENT_INDEX_NECKLACE = 11;
       public static final byte EQUIPMENT_INDEX_BELT = 12;
       public static final byte EQUIPMENT_INDEX_EARRING = 13;
       public static final byte EQUIPMENT_INDEX_EARRING_2ND = 28;
       public static final byte EQUIPMENT_INDEX_EARRING1 = 28;
       public static final byte EQUIPMENT_INDEX_EARRING2 = 34;
       public static final byte EQUIPMENT_INDEX_EARRING3 = 35;
       public static final byte EQUIPMENT_INDEX_RING = 19;
       public static final byte EQUIPMENT_INDEX_RUNE = 25;
       public static final byte EQUIPMENT_INDEX_SENTENCE = 30;
       public static final byte EQUIPMENT_INDEX_SHOULD = 31;
       public static final byte EQUIPMENT_INDEX_BADGE = 32;
       public static final byte EQUIPMENT_INDEX_PANDENT = 33;

       public S_EquipmentWindow(L1PcInstance pc, int itemObjId, int index, boolean isEq) {
         writeC(43);
         writeC(66);
         writeD(itemObjId);
         writeC(index);
         if (isEq) {
           writeC(1);
         } else {
           writeC(0);
         }  writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_EquipmentWindow";
       }
     }



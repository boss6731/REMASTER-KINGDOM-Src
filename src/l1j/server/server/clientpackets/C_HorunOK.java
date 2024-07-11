 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_HorunOK;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Skills;

 public class C_HorunOK
   extends ClientBasePacket {
   private static final String C_HORUN_OK = "[C] C_HorunOK";

   public C_HorunOK(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     int count = readH();
     int[] sid = new int[count];
     int price = 0;
     int level1 = 0;
     int level2 = 0;
     int level3 = 0;
     int level1_cost = 0;
     int level2_cost = 0;
     int level3_cost = 0;
     String skill_name = null;
     int skill_id = 0;

     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null || pc.isGhost()) {
       return;
     }
     for (int i = 0; i < count; i++) {
       sid[i] = readD();
       if (sid[i] > 24) {
         pc.sendPackets((ServerBasePacket)new S_Disconnect());

         return;
       }
       switch (sid[i]) {

         case 0:
           level1++;
           level1_cost += 10;
           break;
         case 1:
           level1 += 2;
           level1_cost += 10;
           break;
         case 2:
           level1 += 4;
           level1_cost += 10;
           break;
         case 3:
           level1 += 8;
           level1_cost += 10;
           break;
         case 4:
           level1 += 16;
           level1_cost += 10;
           break;
         case 5:
           level1 += 32;
           level1_cost += 10;
           break;
         case 6:
           level1 += 64;
           level1_cost += 10;
           break;
         case 7:
           level1 += 128;
           level1_cost += 10;
           break;


         case 8:
           level2++;
           level2_cost += 400;
           break;
         case 9:
           level2 += 2;
           level2_cost += 400;
           break;
         case 10:
           level2 += 4;
           level2_cost += 400;
           break;
         case 11:
           level2 += 8;
           level2_cost += 400;
           break;
         case 12:
           level2 += 16;
           level2_cost += 400;
           break;
         case 13:
           level2 += 32;
           level2_cost += 400;
           break;
         case 14:
           level2 += 64;
           level2_cost += 400;
           break;
         case 15:
           level2 += 128;
           level2_cost += 400;
           break;


         case 16:
           level3++;
           level3_cost += 900;
           break;
         case 17:
           level3 += 2;
           level3_cost += 900;
           break;
         case 18:
           level3 += 4;
           level3_cost += 900;
           break;
         case 19:
           level3 += 8;
           level3_cost += 900;
           break;
         case 20:
           level3 += 16;
           level3_cost += 900;
           break;
         case 21:
           level3 += 32;
           level3_cost += 900;
           break;
         case 22:
           level3 += 64;
           level3_cost += 900;
           break;
         case 23:
           level3 += 128;
           level3_cost += 900;
           break;
       }




     }
     switch (pc.getType()) {
       case 0:
         if (pc.getLevel() < 10) {
           level1 = 0;
           level1_cost = 0;
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 10 && pc.getLevel() <= 19) {
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 20) {
           level3 = 0;
           level3_cost = 0;
         }
         break;

       case 1:
         if (pc.getLevel() < 50) {
           level1 = 0;
           level1_cost = 0;
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 50) {
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0;
         }
         break;

       case 2:
         if (pc.getLevel() < 8) {
           level1 = 0;
           level1_cost = 0;
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 8 && pc.getLevel() <= 15) {
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 16 && pc.getLevel() <= 23) {
           level3 = 0;
           level3_cost = 0;
         }
         break;

       case 3:
         if (pc.getLevel() < 4) {
           level1 = 0;
           level1_cost = 0;
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 4 && pc.getLevel() <= 7) {
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 8 && pc.getLevel() <= 11) {
           level3 = 0;
           level3_cost = 0;
         }
         break;

       case 4:
         if (pc.getLevel() < 12) {
           level1 = 0;
           level1_cost = 0;
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 12 && pc.getLevel() <= 23) {
           level2 = 0;
           level2_cost = 0;
           level3 = 0;
           level3_cost = 0; break;
         }  if (pc.getLevel() >= 24) {
           level3 = 0;
           level3_cost = 0;
         }
         break;
     }




     if (level1 == 0 && level2 == 0 && level3 == 0) {
       return;
     }
     price = level1_cost + level2_cost + level3_cost;

     if ((level1 & 0x1) == 1) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(1);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }
     if ((level1 & 0x2) == 2) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(2);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }
     if ((level1 & 0x4) == 4) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(3);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }
     if ((level1 & 0x8) == 8) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(4);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }
     if ((level1 & 0x10) == 16) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(5);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }
     if ((level1 & 0x20) == 32) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(6);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }
     if ((level1 & 0x40) == 64) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(7);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }
     if ((level1 & 0x80) == 128) {
       if (pc.getInventory().checkItem(40519, price)) {
         pc.getInventory().consumeItem(40519, price);
         L1Skills l1skills = SkillsTable.getInstance().getTemplate(8);
         skill_name = l1skills.getName();
         skill_id = l1skills.getSkillId();
         SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
       } else {
         pc.sendPackets((ServerBasePacket)new S_HorunOK(skill_id, pc));
       }
     }

     if ((level2 & 0x1) == 1) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(9);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level2 & 0x2) == 2) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(10);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level2 & 0x4) == 4) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(11);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level2 & 0x8) == 8) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(12);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level2 & 0x10) == 16) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(13);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level2 & 0x20) == 32) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(14);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level2 & 0x40) == 64) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(15);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level2 & 0x80) == 128) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(16);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }

     if ((level3 & 0x1) == 1) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(17);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level3 & 0x2) == 2) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(18);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level3 & 0x4) == 4) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(19);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level3 & 0x8) == 8) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(20);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level3 & 0x10) == 16) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(21);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level3 & 0x20) == 32) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(22);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
     if ((level3 & 0x40) == 64) {
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(23);
       skill_name = l1skills.getName();
       skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(pc.getId(), skill_id, skill_name, 0, 0);
     }
   }



   public String getType() {
     return "[C] C_HorunOK";
   }
 }



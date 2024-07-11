 package l1j.server.server.model;

 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_EffectLocation;
 import l1j.server.server.serverpackets.S_HPUpdate;
 import l1j.server.server.serverpackets.S_MPUpdate;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_OwnCharStatus2;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1Cooking
 {
   public static void useCookingItem(L1PcInstance pc, L1ItemInstance item) {
     int cookingId, itemId = item.getItem().getItemId();
     if (itemId == 41284 || itemId == 49056 || itemId == 49064 || itemId == 41292 || itemId == 210055 || itemId == 210063) {





       if (pc.get_food() != 225) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(74, item.getNumberedName(1)));
       }
       pc.sendPackets((ServerBasePacket)new S_EffectLocation(pc.getX(), pc.getY(), 6392));
     }


     if ((itemId >= 41277 && itemId <= 41283) || (itemId >= 49049 && itemId <= 49056) || (itemId >= 210048 && itemId <= 210055) || (itemId >= 41285 && itemId <= 41291) || (itemId >= 49057 && itemId <= 49064) || (itemId >= 210056 && itemId <= 210062) || (itemId >= 30051 && itemId <= 30053) || (itemId >= 4100156 && itemId <= 4100158) || itemId == 3000129 || (itemId >= 30001858 && itemId <= 30001860) || (itemId >= 30001862 && itemId <= 30001864)) {








       pc.sendPackets((ServerBasePacket)new S_EffectLocation(pc.getX(), pc.getY(), 6392));
       cookingId = pc.getCookingId();
       if (cookingId != 0) {
         pc.removeSkillEffect(cookingId);
       }
     }


     if (itemId == 41284 || itemId == 49056 || itemId == 49064 || itemId == 41292 || itemId == 200021 || itemId == 200029 || itemId == 30054 || itemId == 4100159 || itemId == 3000130 || itemId == 30001861 || itemId == 30001865) {










       pc.sendPackets((ServerBasePacket)new S_EffectLocation(pc.getX(), pc.getY(), 6392));
       int dessertId = pc.getDessertId();
       if (dessertId != 0) {
         pc.removeSkillEffect(dessertId);
       }
     }
     if (itemId >= 42650 && itemId <= 42652) {
       pc.sendPackets((ServerBasePacket)new S_EffectLocation(pc.getX(), pc.getY(), 6392));
       int dessertId = pc.getDessertId();
       if (dessertId != 0) {
         pc.removeSkillEffect(dessertId);
       }
     }



     int time = 900;
     switch (itemId) {
       case 41277:
       case 41285:
         if (itemId == 41277) {
           cookingId = 3000;
         } else {
           cookingId = 3050;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 41278:
       case 41286:
         if (itemId == 41278) {
           cookingId = 3001;
         } else {
           cookingId = 3051;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 41279:
       case 41287:
         if (itemId == 41279) {
           cookingId = 3002;
         } else {
           cookingId = 3052;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 41280:
       case 41288:
         if (itemId == 41280) {
           cookingId = 3003;
         } else {
           cookingId = 3053;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 41281:
       case 41289:
         if (itemId == 41281) {
           cookingId = 3004;
         } else {
           cookingId = 3054;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 41282:
       case 41290:
         if (itemId == 41282) {
           cookingId = 3005;
         } else {
           cookingId = 3055;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 41283:
       case 41291:
         if (itemId == 41283) {
           cookingId = 3006;
         } else {
           cookingId = 3056;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 41284:
       case 41292:
         if (itemId == 41284) {
           cookingId = 3007;
         } else {
           cookingId = 3057;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49049:
       case 49057:
         if (itemId == 49049) {
           cookingId = 3008;
         } else {
           cookingId = 3058;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49050:
       case 49058:
         if (itemId == 49050) {
           cookingId = 3009;
         } else {
           cookingId = 3059;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49051:
       case 49059:
         if (itemId == 49051) {
           cookingId = 3010;
         } else {
           cookingId = 3060;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49052:
       case 49060:
         if (itemId == 49052) {
           cookingId = 3011;
         } else {
           cookingId = 3061;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49053:
       case 49061:
         if (itemId == 49053) {
           cookingId = 3012;
         } else {
           cookingId = 3062;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49054:
       case 49062:
         if (itemId == 49054) {
           cookingId = 3013;
         } else {
           cookingId = 3063;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49055:
       case 49063:
         if (itemId == 49055) {
           cookingId = 3014;
         } else {
           cookingId = 3064;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 49056:
       case 49064:
         if (itemId == 49056) {
           cookingId = 3015;
         } else {
           cookingId = 3065;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210048:
       case 210056:
         if (itemId == 210048) {
           cookingId = 3016;
         } else {
           cookingId = 3066;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210049:
       case 210057:
         if (itemId == 210049) {
           cookingId = 3017;
         } else {
           cookingId = 3067;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210050:
       case 210058:
         if (itemId == 210050) {
           cookingId = 3018;
         } else {
           cookingId = 3068;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210051:
       case 210059:
         if (itemId == 210051) {
           cookingId = 3019;
         } else {
           cookingId = 3069;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210052:
       case 210060:
         if (itemId == 210052) {
           cookingId = 3020;
         } else {
           cookingId = 3070;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210053:
       case 210061:
         if (itemId == 210053) {
           cookingId = 3021;
         } else {
           cookingId = 3071;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210054:
       case 210062:
         if (itemId == 210054) {
           cookingId = 3022;
         } else {
           cookingId = 3072;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 210055:
       case 210063:
         if (itemId == 210055) {
           cookingId = 3023;
         } else {
           cookingId = 3073;
         }
         eatCooking(pc, cookingId, time);
         break;
       case 4100156:
         eatCooking(pc, 3100, 3600);
         break;
       case 30051:
         eatCooking(pc, 3074, 1800);
         break;
       case 4100157:
         eatCooking(pc, 3101, 3600);
         break;
       case 30052:
         eatCooking(pc, 3075, 1800);
         break;
       case 4100158:
         eatCooking(pc, 3102, 3600);
         break;
       case 30053:
         eatCooking(pc, 3076, 1800);
         break;
       case 4100159:
         eatCooking(pc, 3103, 3600);
         break;
       case 30054:
         eatCooking(pc, 3077, 1800);
         break;
       case 3000129:
         eatCooking(pc, 3000129, 1800);
         break;
       case 3000130:
         eatCooking(pc, 3000130, 1800);
         break;
       case 42650:
         eatCooking(pc, 3080, 1800);
         break;
       case 42651:
         eatCooking(pc, 3081, 1800);
         break;
       case 42652:
         eatCooking(pc, 3082, 1800);
         break;
       case 42653:
         eatCooking(pc, 3083, 1800);
         break;
       case 30001858:
         eatCooking(pc, 3104, 1800);
         break;
       case 30001859:
         eatCooking(pc, 3105, 1800);
         break;
       case 30001860:
         eatCooking(pc, 3106, 1800);
         break;
       case 30001861:
         eatCooking(pc, 3107, 1800);
         break;
       case 30001862:
         eatCooking(pc, 3108, 1800);
         break;
       case 30001863:
         eatCooking(pc, 3109, 1800);
         break;
       case 30001864:
         eatCooking(pc, 3110, 1800);
         break;
       case 30001865:
         eatCooking(pc, 3111, 1800);
         break;
     }



     pc.sendPackets((ServerBasePacket)new S_ServerMessage(76, item.getNumberedName(1)));
     pc.getInventory().removeItem(item, 1);
   }



   public static void eatCooking(L1PcInstance pc, int cookingId, int time) {
     int cookingType = 0;

     if (cookingId == 3023 || cookingId == 3073 || cookingId == 3077 || cookingId == 3103 || cookingId == 3083 || cookingId == 1541 || cookingId == 3000130 || cookingId == 3111 || cookingId == 3107)
     {
       if (pc.getDessertId() > 0) {
         pc.removeSkillEffect(pc.getDessertId());
       }
     }

     switch (cookingId) {

       case 3104:
         cookingType = 226;
         pc.addDamageReduction(2);
         pc.addDmgup(3);
         pc.addHitup(2);
         pc.addHpr(5);
         pc.addMpr(2);
         pc.addMaxHp(50);
         pc.add_item_exp_bonus(4.0D);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;

       case 3105:
         cookingType = 227;
         pc.addDamageReduction(2);
         pc.addBowDmgup(3);
         pc.addBowHitup(2);
         pc.addHpr(3);
         pc.addMpr(3);
         pc.addMaxHp(25);
         pc.addMaxMp(25);
         pc.add_item_exp_bonus(4.0D);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;

       case 3106:
         cookingType = 228;
         pc.addDamageReduction(2);
         pc.getAbility().addSp(3);
         pc.addHpr(2);
         pc.addMpr(5);
         pc.addMaxMp(50);
         pc.add_item_exp_bonus(4.0D);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;

       case 3107:
         cookingType = 229;
         pc.addDamageReduction(3);
         pc.add_item_exp_bonus(6.0D);
         pc.addMaxHp(50);
         pc.getResistance().addcalcPcDefense(2);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;

       case 3108:
         cookingType = 230;
         pc.addDamageReduction(2);
         pc.addDmgup(3);
         pc.addHitup(2);
         pc.addHpr(5);
         pc.addMpr(2);
         pc.addMaxHp(50);
         pc.add_item_exp_bonus(4.0D);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 3);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;

       case 3109:
         cookingType = 231;
         pc.addDamageReduction(2);
         pc.addBowDmgup(3);
         pc.addBowHitup(2);
         pc.addHpr(3);
         pc.addMpr(3);
         pc.addMaxHp(25);
         pc.addMaxMp(25);
         pc.add_item_exp_bonus(4.0D);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 3);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;

       case 3110:
         cookingType = 232;
         pc.addDamageReduction(2);
         pc.getAbility().addSp(3);
         pc.addHpr(2);
         pc.addMpr(5);
         pc.addMaxMp(50);
         pc.add_item_exp_bonus(4.0D);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 3);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;

       case 3111:
         cookingType = 233;
         pc.addDamageReduction(3);
         pc.add_item_exp_bonus(6.0D);
         pc.addMaxHp(50);
         pc.getResistance().addcalcPcDefense(2);
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 2);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;
       case 3080:
         pc.setDessertId(cookingId);
         cookingType = 157;
         pc.addHitup(1);
         pc.addDmgup(2);
         pc.addHpr(2);
         pc.addMpr(2);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3081:
         pc.setDessertId(cookingId);
         cookingType = 158;
         pc.addBowHitup(1);
         pc.addBowDmgup(2);
         pc.addHpr(2);
         pc.addMpr(2);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3082:
         pc.setDessertId(cookingId);
         cookingType = 159;
         pc.addHpr(2);
         pc.addMpr(3);
         pc.getAbility().addSp(2);
         pc.getResistance().addMr(10);
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3083:
         cookingType = 160;
         pc.add_item_exp_bonus(4.0D);
         break;
       case 3000:
       case 3050:
         cookingType = 0;
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3078:
         if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
           pc.getAbility().addStr(7);
           pc.getAbility().addDex(7);
         } else {
           pc.getAbility().addStr(6);
           pc.getAbility().addDex(6);
         }
         pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
         break;
       case 3001:
       case 3051:
         cookingType = 1;
         pc.addMaxHp(30);
         pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
         if (pc.isInParty())
         {
           pc.getParty().refreshPartyMemberStatus(pc);
         }
         break;
       case 3002:
       case 3052:
         cookingType = 2;
         break;
       case 3003:
       case 3053:
         cookingType = 3;
         pc.getAC().addAc(-1);
         pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
         break;
       case 3004:
       case 3054:
         cookingType = 4;
         pc.addMaxMp(20);
         pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
         break;
       case 3005:
       case 3055:
         cookingType = 5;
         break;
       case 3006:
       case 3056:
         cookingType = 6;
         pc.getResistance().addMr(5);
         break;
       case 3007:
       case 3057:
         cookingType = 7;
         pc.add_item_exp_bonus(1.0D);
         break;

       case 3008:
       case 3058:
         cookingType = 16;
         pc.addBowHitRate(2);
         pc.addBowDmgup(1);
         break;
       case 3009:
       case 3059:
         cookingType = 17;
         pc.addMaxHp(30);
         pc.addMaxMp(30);

         pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
         if (pc.isInParty())
         {
           pc.getParty().refreshPartyMemberStatus(pc);
         }
         pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
         break;
       case 3010:
       case 3060:
         cookingType = 18;
         pc.getAC().addAc(-2);
         pc.sendPackets((ServerBasePacket)new S_OwnCharStatus2(pc));
         break;
       case 3011:
       case 3061:
         cookingType = 19;
         break;
       case 3012:
       case 3062:
         cookingType = 20;
         break;
       case 3013:
       case 3063:
         cookingType = 21;
         pc.getResistance().addMr(10);
         break;
       case 3014:
       case 3064:
         cookingType = 22;
         pc.getAbility().addSp(1);
         break;
       case 3015:
       case 3065:
         cookingType = 23;
         pc.add_item_exp_bonus(5.0D);
         break;

       case 3016:
       case 3066:
         cookingType = 45;
         pc.addBowHitRate(2);
         pc.addBowDmgup(1);
         break;
       case 3017:
       case 3067:
         cookingType = 46;
         pc.addMaxHp(50);
         pc.addMaxMp(50);
         pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
         if (pc.isInParty())
         {
           pc.getParty().refreshPartyMemberStatus(pc);
         }
         pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
         break;
       case 3018:
       case 3068:
         cookingType = 47;
         pc.addHitup(2);
         pc.addDmgup(1);
         break;
       case 3019:
       case 3069:
         cookingType = 48;
         pc.getAC().addAc(-3);
         pc.sendPackets((ServerBasePacket)new S_OwnCharStatus2(pc));
         break;
       case 3020:
       case 3070:
         cookingType = 49;
         pc.getResistance().addAllNaturalResistance(10);
         pc.getResistance().addMr(15);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3021:
       case 3071:
         cookingType = 50;
         pc.getAbility().addSp(2);
         break;
       case 3022:
       case 3072:
         cookingType = 51;
         pc.addMaxHp(30);
         pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
         if (pc.isInParty())
         {
           pc.getParty().refreshPartyMemberStatus(pc);
         }
         break;
       case 3023:
       case 3073:
         cookingType = 52;
         pc.add_item_exp_bonus(9.0D);
         break;
       case 3074:
         cookingType = 157;
         pc.addDamageReductionByArmor(2);
         pc.addDmgup(2);
         pc.addHitup(1);
         pc.addHpr(2);
         pc.addMpr(2);
         pc.getResistance().addWater(10);
         pc.getResistance().addFire(10);
         pc.getResistance().addWind(10);
         pc.getResistance().addEarth(10);
         pc.getResistance().addMr(10);
         pc.add_item_exp_bonus(2.0D);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3075:
         cookingType = 158;
         pc.addDamageReductionByArmor(2);
         pc.addBowDmgup(2);
         pc.addBowHitup(1);
         pc.addHpr(2);
         pc.addMpr(2);
         pc.getResistance().addWater(10);
         pc.getResistance().addFire(10);
         pc.getResistance().addWind(10);
         pc.getResistance().addEarth(10);
         pc.getResistance().addMr(10);
         pc.add_item_exp_bonus(2.0D);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3076:
         cookingType = 159;
         pc.addDamageReductionByArmor(2);
         pc.getAbility().addSp(2);
         pc.addHpr(2);
         pc.addMpr(3);
         pc.getResistance().addMr(10);
         pc.getResistance().addWater(10);
         pc.getResistance().addFire(10);
         pc.getResistance().addWind(10);
         pc.getResistance().addEarth(10);
         pc.add_item_exp_bonus(2.0D);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3077:
         cookingType = 160;
         pc.add_item_exp_bonus(4.0D);
         break;
       case 3100:
         cookingType = 215;
         pc.addDmgup(2);
         pc.addHitup(1);
         pc.addHpr(2);
         pc.addMpr(2);
         pc.getResistance().addMr(10);
         pc.getResistance().addWater(10);
         pc.getResistance().addFire(10);
         pc.getResistance().addWind(10);
         pc.getResistance().addEarth(10);
         pc.add_item_exp_bonus(2.0D);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 3);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;
       case 3101:
         cookingType = 216;
         pc.addBowDmgup(2);
         pc.addBowHitup(1);
         pc.addHpr(2);
         pc.addMpr(2);
         pc.getResistance().addMr(10);
         pc.getResistance().addWater(10);
         pc.getResistance().addFire(10);
         pc.getResistance().addWind(10);
         pc.getResistance().addEarth(10);
         pc.add_item_exp_bonus(2.0D);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 3);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;
       case 3102:
         cookingType = 217;
         pc.getAbility().addSp(2);
         pc.addHpr(2);
         pc.addMpr(3);
         pc.getResistance().addMr(10);
         pc.getResistance().addWater(10);
         pc.getResistance().addFire(10);
         pc.getResistance().addWind(10);
         pc.getResistance().addEarth(10);
         pc.add_item_exp_bonus(2.0D);
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 3);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;
       case 3103:
         cookingType = 218;
         pc.getResistance().addcalcPcDefense(2);
         pc.add_item_exp_bonus(4.0D);
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 2);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
         break;
       case 1541:
         if (pc.hasSkillEffect(cookingId))
           pc.removeSkillEffect(cookingId);
         cookingType = 187;
         pc.addDamageReductionByArmor(5);
         pc.add_item_exp_bonus(20.0D);
         break;
       case 3000129:
         cookingType = 151;
         pc.addHitup(2);
         pc.addDmgup(2);
         pc.addBowHitup(2);
         pc.addBowDmgup(2);
         pc.getAbility().addSp(2);
         pc.addHpr(3);
         pc.addMpr(4);
         pc.getResistance().addMr(15);
         pc.getResistance().addAllNaturalResistance(10);
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         break;
       case 3000130:
         cookingType = 162;
         pc.add_item_exp_bonus(5.0D);
         break;
     }






     pc.sendPackets((ServerBasePacket)new S_PacketBox(53, cookingType, time));
     pc.setSkillEffect(cookingId, (time * 1000));


     if ((cookingId >= 3000 && cookingId <= 3006) || (cookingId >= 3050 && cookingId <= 3056)) {
       pc.setCookingId(cookingId);
     } else if (cookingId == 3007 || cookingId == 3057 || cookingId == 3083) {
       pc.setDessertId(cookingId);

     }
     else if ((cookingId >= 3008 && cookingId <= 3014) || (cookingId >= 3058 && cookingId <= 3064)) {

       pc.setCookingId(cookingId);
     } else if (cookingId == 3015 || cookingId == 3065 || cookingId == 3083) {
       pc.setDessertId(cookingId);


     }
     else if ((cookingId >= 3016 && cookingId <= 3022) || (cookingId >= 3066 && cookingId <= 3072) || cookingId == 3100 || cookingId == 3101 || cookingId == 3102 || (cookingId >= 3074 && cookingId <= 3076) || cookingId == 3104 || cookingId == 3105 || cookingId == 3106 || cookingId == 3110 || cookingId == 3108 || cookingId == 3109) {


       pc.setCookingId(cookingId);
     } else if (cookingId == 3023 || cookingId == 3073 || cookingId == 3077 || cookingId == 3103 || cookingId == 1541 || cookingId == 3000130 || cookingId == 3107 || cookingId == 3111) {

       pc.setDessertId(cookingId);


     }
     else if (cookingId == 3000129) {
       pc.setCookingId(cookingId);
     } else if (cookingId == 3000130) {
       pc.setDessertId(cookingId);
     }

     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
   }
 }



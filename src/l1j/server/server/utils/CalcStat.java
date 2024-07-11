 package l1j.server.server.utils;

 public class CalcStat
 {
   public static int calcDmgup(int str) {
     int value = 0;
     if (IntRange.includes(str, 8, 127)) {
       value = -2;
       for (int i = 0; i < str; i++) {
         if (i % 2 > 0) {
           value++;
         }
       }
       return value;
     }
     return 2;
   }


   public static int calcHitup(int str) {
     int value = 0;
     if (IntRange.includes(str, 8, 127)) {
       for (int i = 0; i < str; i++) {
         if (i % 3 > 0) {
           value++;
         }
       }
       return value;
     }
     return 5;
   }


   public static int calcDmgCritical(int class_type, int str) {
     int value = 0;
     if (IntRange.includes(str, 8, 127)) {
       if (class_type == 9) {
         value = -2;
       } else {
         value = -4;
       }  for (int i = 0; i <= str; i++) {
         if (i % 10 < 1) {
           value++;
         }
       }
       return value;
     }
     return 0;
   }


   public static int calcPureMeleeDmgup(int str) {
     if (str < 25) {
       return 0;
     }
     if (IntRange.includes(str, 25, 34)) {
       return 1;
     }
     if (IntRange.includes(str, 35, 44)) {
       return 2;
     }
     if (IntRange.includes(str, 45, 54)) {
       return 5;
     }
     if (IntRange.includes(str, 55, 59)) {
       return 10;
     }
     return 15;
   }


   public static int calcPureMeleeHitup(int str) {
     if (str < 25) {
       return 0;
     }
     if (IntRange.includes(str, 25, 34)) {
       return 1;
     }
     if (IntRange.includes(str, 35, 44)) {
       return 2;
     }
     if (IntRange.includes(str, 45, 54)) {
       return 5;
     }
     if (IntRange.includes(str, 55, 59)) {
       return 10;
     }
     return 15;
   }


   public static int calcPureMeleeCritical(int str) {
     if (str < 45) {
       return 0;
     }
     if (IntRange.includes(str, 45, 54)) {
       return 1;
     }
     if (IntRange.includes(str, 55, 59)) {
       return 3;
     }
     return 5;
   }


   public static int calcBowDmgup(int dex) {
     int value = 0;
     if (IntRange.includes(dex, 8, 127)) {
       value = -1;
       for (int i = 0; i <= dex; i++) {
         if (i % 3 < 1) {
           value++;
         }
       }
       return value;
     }
     return 2;
   }

   public static int calcBowHitup(int dex) {
     int value = 0;
     if (IntRange.includes(dex, 8, 127)) {
       value = -11;
       for (int i = 0; i <= dex; i++) {
         value++;
       }
       return value;
     }
     return -3;
   }

   public static int calcBowCritical(int dex) {
     int value = 0;
     if (IntRange.includes(dex, 40, 127)) {
       value = -4;
       for (int i = 0; i <= dex; i++) {
         if (i % 10 < 1) {
           value++;
         }
       }
       return value;
     }
     return 0;
   }


   public static int calcPureMissileDmgup(int dex) {
     if (dex < 25) {
       return 0;
     }
     if (IntRange.includes(dex, 25, 34)) {
       return 1;
     }
     if (IntRange.includes(dex, 35, 44)) {
       return 2;
     }
     if (IntRange.includes(dex, 45, 54)) {
       return 5;
     }
     if (IntRange.includes(dex, 55, 59)) {
       return 10;
     }
     return 15;
   }


   public static int calcPureMissileHitup(int dex) {
     if (dex < 25) {
       return 0;
     }
     if (IntRange.includes(dex, 25, 34)) {
       return 1;
     }
     if (IntRange.includes(dex, 35, 44)) {
       return 2;
     }
     if (IntRange.includes(dex, 45, 54)) {
       return 5;
     }
     if (IntRange.includes(dex, 55, 59)) {
       return 10;
     }
     return 15;
   }


   public static int calcPureMissileCritical(int dex) {
     if (dex < 45) {
       return 0;
     }
     if (IntRange.includes(dex, 45, 54)) {
       return 1;
     }
     if (IntRange.includes(dex, 55, 59)) {
       return 3;
     }
     return 5;
   }


   public static int calcAc(int dex) {
     int value = 0;
     if (IntRange.includes(dex, 8, 127)) {
       value = 1;
       for (int i = 0; i <= dex; i++) {
         if (i % 3 < 1) {
           value--;
         }
       }
       return value;
     }
     return -2;
   }

   public static int ER(int dex) {
     int value = 0;
     if (IntRange.includes(dex, 8, 127)) {
       value = -1;
       for (int i = 0; i < dex; i++) {
         if (i % 2 == 0) {
           value++;
         }
       }
       return value;
     }
     return 3;
   }


   public static int calcMagicDmg(int Int) {
     int value = 0;
     if (IntRange.includes(Int, 15, 127)) {
       value = -3;
       for (int i = 0; i <= Int; i++) {
         if (i % 5 == 0) {
           value++;
         }
       }
       return value;
     }
     return 0;
   }


   public static int calcMagicHitUp(int Int) {
     int value = 0;
     if (Int < 9) {
       return -4;
     }
     if (IntRange.includes(Int, 9, 18)) {
       value = -7;
       for (int i = 0; i <= Int; i++) {
         if (i % 3 == 0) {
           value++;
         }
       }
       return value;
     }

     if (IntRange.includes(Int, 23, 127)) {
       value = -7;
       for (int i = 0; i <= Int - 2; i++) {
         if (i % 3 == 0) {
           value++;
         }
       }
       return value;
     }
     return 0;
   }


   public static int calcMagicCritical(int Int) {
     int value = 0;
     if (IntRange.includes(Int, 8, 127)) {
       value = -7;
       for (int i = 0; i <= Int; i++) {
         if (i % 5 < 1) {
           value++;
         }
       }
       if (value < 0) {
         value = 0;
       }
       return value;
     }
     return 0;
   }


   public static int calcMagicBonus(int Int) {
     int value = 0;
     if (IntRange.includes(Int, 8, 127)) {
       value = 0;
       for (int i = 0; i <= Int; i++) {
         if (i % 4 == 0) {
           value++;
         }
       }
       return value;
     }
     return 2;
   }


   public static int calcPureMagicDmgup(int intel) {
     if (intel < 25) {
       return 0;
     }
     if (IntRange.includes(intel, 25, 34)) {
       return 1;
     }
     if (IntRange.includes(intel, 35, 44)) {
       return 2;
     }
     if (IntRange.includes(intel, 45, 54)) {
       return 5;
     }
     if (IntRange.includes(intel, 55, 59)) {
       return 10;
     }
     return 15;
   }


   public static int calcPureMagicHitup(int intel) {
     if (intel < 25) {
       return 0;
     }
     if (IntRange.includes(intel, 25, 34)) {
       return 1;
     }
     if (IntRange.includes(intel, 35, 44)) {
       return 2;
     }
     if (IntRange.includes(intel, 45, 54)) {
       return 5;
     }
     if (IntRange.includes(intel, 55, 59)) {
       return 10;
     }
     return 15;
   }


   public static int calcPureMagicCritical(int intel) {
     if (intel < 45) {
       return 0;
     }
     if (IntRange.includes(intel, 45, 54)) {
       return 1;
     }
     if (IntRange.includes(intel, 55, 59)) {
       return 3;
     }
     return 5;
   }


   public static int calcDecreaseMp(int Int) {
     int value = 0;
     if (IntRange.includes(Int, 8, 127)) {
       for (int i = 0; i < Int; i++) {
         if (i % 3 > 0) {
           value++;
         }
       }
       if (value > 30) {
         value = 30;
       }

       return value;
     }
     return 5;
   }

   public static int calcPierceAll(int totalcha) {
     int value = 0;
     for (int i = 1; i <= totalcha &&
       i <= 120; i++) {


       if (i % 4 == 0) {
         value++;
       }
     }
     return value;
   }



   public static int calcPurePierceAll(int cha) {
     int value = 0;
     for (int i = 1; i <= cha; i++) {
       switch (i) {
         case 12:
         case 35:
         case 45:
         case 55:
         case 60:
           value++;
           break;
       }
     }
     return value;
   }


   public static int calcDecreaseCoolTime(int totalcha) {
     int value = 0;
     for (int i = 1; i <= totalcha &&
       i <= 120; i++) {


       if ((i + 1) % 8 == 0) {
         value++;
       }
     }
     return value;
   }

   public static int calcPureDecreaseCoolTime(int cha) {
     int value = 0;
     for (int i = 1; i <= cha; i++) {
       switch (i) {
         case 12:
         case 25:
         case 60:
           value++;
           break;
       }
     }
     return value * 100;
   }

   public static int calcDecreaseCCDuration(int totalcha) {
     int value = 0;
     for (int i = 1; i <= totalcha &&
       i <= 120; i++) {


       if ((i + 3) % 8 == 0) {
         value++;
       }
     }
     return value * 100;
   }

   public static int calcPureDecreaseCCDuration(int cha) {
     int value = 0;
     for (int i = 1; i <= cha; i++) {
       switch (i) {
         case 12:
         case 25:
         case 60:
           value++;
           break;
       }
     }
     return value * 100;
   }


   public static int MinincreaseMp(int type, int Wis) {
     int randommp = 0;
     switch (type) {
       case 0:
         if (Wis <= 14) {
           randommp = 3; break;
         }  if (Wis <= 19) {
           randommp = 4; break;
         }  if (Wis <= 24) {
           randommp = 5; break;
         }  if (Wis <= 29) {
           randommp = 6; break;
         }  if (Wis <= 34) {
           randommp = 7; break;
         }  if (Wis <= 39) {
           randommp = 8; break;
         }  if (Wis <= 44) {
           randommp = 9; break;
         }
         randommp = 10;
         break;
       case 1:
       case 7:
         if (Wis <= 9) {
           randommp = 0; break;
         }  if (Wis <= 14) {
           randommp = 1; break;
         }  if (Wis <= 24) {
           randommp = 2; break;
         }  if (Wis <= 28) {
           randommp = 3; break;
         }  if (Wis <= 39) {
           randommp = 4; break;
         }  if (Wis <= 44) {
           randommp = 5; break;
         }
         randommp = 6;
         break;
       case 2:
         if (Wis <= 14) {
           randommp = 4; break;
         }  if (Wis <= 19) {
           randommp = 5; break;
         }  if (Wis <= 24) {
           randommp = 7; break;
         }  if (Wis <= 29) {
           randommp = 8; break;
         }  if (Wis <= 34) {
           randommp = 10; break;
         }  if (Wis <= 39) {
           randommp = 11; break;
         }  if (Wis <= 44) {
           randommp = 13; break;
         }
         randommp = 14;
         break;
       case 3:
         if (Wis <= 14) {
           randommp = 6; break;
         }  if (Wis <= 19) {
           randommp = 8; break;
         }  if (Wis <= 24) {
           randommp = 10; break;
         }  if (Wis <= 29) {
           randommp = 12; break;
         }  if (Wis <= 34) {
           randommp = 14; break;
         }  if (Wis <= 39) {
           randommp = 16; break;
         }  if (Wis <= 44) {
           randommp = 18; break;
         }
         randommp = 20;
         break;
       case 4:
         if (Wis <= 14) {
           randommp = 4; break;
         }  if (Wis <= 19) {
           randommp = 5; break;
         }  if (Wis <= 24) {
           randommp = 7; break;
         }  if (Wis <= 29) {
           randommp = 8; break;
         }  if (Wis <= 34) {
           randommp = 10; break;
         }  if (Wis <= 39) {
           randommp = 11; break;
         }  if (Wis <= 44) {
           randommp = 13; break;
         }
         randommp = 14;
         break;
       case 5:
       case 8:
       case 9:
         if (Wis <= 14) {
           randommp = 2; break;
         }  if (Wis <= 24) {
           randommp = 3; break;
         }  if (Wis <= 29) {
           randommp = 4; break;
         }  if (Wis <= 39) {
           randommp = 5; break;
         }  if (Wis <= 44) {
           randommp = 6; break;
         }
         randommp = 7;
         break;
       case 6:
         if (Wis <= 14) {
           randommp = 4; break;
         }  if (Wis <= 19) {
           randommp = 6; break;
         }  if (Wis <= 24) {
           randommp = 7; break;
         }  if (Wis <= 29) {
           randommp = 9; break;
         }  if (Wis <= 34) {
           randommp = 11; break;
         }  if (Wis <= 39) {
           randommp = 12; break;
         }  if (Wis <= 44) {
           randommp = 14; break;
         }
         randommp = 16;
         break;
     }


     return randommp;
   }

   public static int MaxincreaseMp(int type, int Wis) {
     int randommp = 0;
     switch (type) {
       case 0:
         if (Wis <= 11) {
           randommp = 4; break;
         }  if (Wis <= 14) {
           randommp = 5; break;
         }  if (Wis <= 17) {
           randommp = 6; break;
         }  if (Wis <= 20) {
           randommp = 7; break;
         }  if (Wis <= 23) {
           randommp = 8; break;
         }  if (Wis <= 26) {
           randommp = 9; break;
         }  if (Wis <= 29) {
           randommp = 10; break;
         }  if (Wis <= 32) {
           randommp = 11; break;
         }  if (Wis <= 35) {
           randommp = 12; break;
         }  if (Wis <= 38) {
           randommp = 13; break;
         }  if (Wis <= 41) {
           randommp = 14; break;
         }  if (Wis <= 44) {
           randommp = 15; break;
         }
         randommp = 16;
         break;
       case 1:
       case 7:
         if (Wis <= 8) {
           randommp = 1; break;
         }  if (Wis <= 14) {
           randommp = 2; break;
         }  if (Wis <= 17) {
           randommp = 3; break;
         }  if (Wis <= 23) {
           randommp = 4; break;
         }  if (Wis <= 26) {
           randommp = 5; break;
         }  if (Wis <= 32) {
           randommp = 6; break;
         }  if (Wis <= 35) {
           randommp = 7; break;
         }  if (Wis <= 41) {
           randommp = 8; break;
         }  if (Wis <= 44) {
           randommp = 9; break;
         }
         randommp = 10;
         break;
       case 2:
         if (Wis <= 14) {
           randommp = 7; break;
         }  if (Wis <= 17) {
           randommp = 8; break;
         }  if (Wis <= 20) {
           randommp = 10; break;
         }  if (Wis <= 23) {
           randommp = 11; break;
         }  if (Wis <= 26) {
           randommp = 13; break;
         }  if (Wis <= 29) {
           randommp = 14; break;
         }  if (Wis <= 32) {
           randommp = 16; break;
         }  if (Wis <= 35) {
           randommp = 17; break;
         }  if (Wis <= 38) {
           randommp = 19; break;
         }  if (Wis <= 41) {
           randommp = 20; break;
         }  if (Wis <= 44) {
           randommp = 22; break;
         }
         randommp = 23;
         break;
       case 3:
         if (Wis <= 14) {
           randommp = 10; break;
         }  if (Wis <= 17) {
           randommp = 12; break;
         }  if (Wis <= 20) {
           randommp = 14; break;
         }  if (Wis <= 23) {
           randommp = 16; break;
         }  if (Wis <= 26) {
           randommp = 18; break;
         }  if (Wis <= 29) {
           randommp = 20; break;
         }  if (Wis <= 32) {
           randommp = 22; break;
         }  if (Wis <= 35) {
           randommp = 24; break;
         }  if (Wis <= 38) {
           randommp = 26; break;
         }  if (Wis <= 41) {
           randommp = 28; break;
         }  if (Wis <= 44) {
           randommp = 30; break;
         }
         randommp = 32;
         break;
       case 4:
         if (Wis <= 11) {
           randommp = 5; break;
         }  if (Wis <= 14) {
           randommp = 7; break;
         }  if (Wis <= 17) {
           randommp = 8; break;
         }  if (Wis <= 20) {
           randommp = 10; break;
         }  if (Wis <= 26) {
           randommp = 13; break;
         }  if (Wis <= 29) {
           randommp = 14; break;
         }  if (Wis <= 32) {
           randommp = 16; break;
         }  if (Wis <= 35) {
           randommp = 17; break;
         }  if (Wis <= 38) {
           randommp = 19; break;
         }  if (Wis <= 41) {
           randommp = 20; break;
         }
         randommp = 22;
         break;
       case 5:
       case 8:
       case 9:
         if (Wis <= 14) {
           randommp = 3; break;
         }  if (Wis <= 17) {
           randommp = 4; break;
         }  if (Wis <= 23) {
           randommp = 5; break;
         }  if (Wis <= 26) {
           randommp = 6; break;
         }  if (Wis <= 29) {
           randommp = 7; break;
         }  if (Wis <= 35) {
           randommp = 8; break;
         }  if (Wis <= 38) {
           randommp = 9; break;
         }  if (Wis <= 44) {
           randommp = 10; break;
         }
         randommp = 11;
         break;
       case 6:
         if (Wis <= 14) {
           randommp = 7; break;
         }  if (Wis <= 17) {
           randommp = 9; break;
         }  if (Wis <= 20) {
           randommp = 11; break;
         }  if (Wis <= 23) {
           randommp = 12; break;
         }  if (Wis <= 26) {
           randommp = 14; break;
         }  if (Wis <= 29) {
           randommp = 16; break;
         }  if (Wis <= 32) {
           randommp = 18; break;
         }  if (Wis <= 35) {
           randommp = 19; break;
         }  if (Wis <= 38) {
           randommp = 21; break;
         }  if (Wis <= 41) {
           randommp = 23; break;
         }  if (Wis <= 44) {
           randommp = 24; break;
         }
         randommp = 26;
         break;
     }


     return randommp;
   }

   public static int calcMpr(int wis) {
     if (wis <= 9)
       return 1;
     if (wis <= 14)
       return 2;
     if (wis <= 19)
       return 3;
     if (wis <= 24)
       return 4;
     if (wis <= 29)
       return 6;
     if (wis <= 34)
       return 7;
     if (wis <= 39)
       return 9;
     if (wis <= 44)
       return 10;
     if (wis <= 49)
       return 14;
     if (wis <= 54)
       return 15;
     if (wis <= 59)
       return 16;
     if (wis <= 64) {
       return 22;
     }
     return 24;
   }

   public static int calcMprPotion(int wis) {
     switch (wis) {
       case 12:
       case 13:
         return 2;
       case 14:
       case 15:
         return 3;
       case 16:
       case 17:
         return 4;
       case 18:
       case 19:
         return 6;
       case 20:
       case 21:
         return 7;
       case 22:
       case 23:
         return 8;
       case 24:
       case 25:
         return 9;
       case 26:
       case 27:
         return 10;
       case 28:
       case 29:
         return 11;
       case 30:
       case 31:
         return 12;
       case 32:
       case 33:
         return 13;
       case 34:
         return 14;
       case 35:
         return 15;
       case 36:
       case 37:
         return 16;
       case 38:
       case 39:
         return 17;
       case 40:
       case 41:
         return 18;
       case 42:
       case 43:
         return 19;
       case 44:
         return 20;
       case 45:
         return 23;
       case 46:
       case 47:
         return 24;
       case 48:
       case 49:
         return 25;
       case 50:
       case 51:
         return 26;
       case 52:
       case 53:
         return 27;
       case 54:
       case 55:
         return 28;
       case 56:
       case 57:
         return 29;
       case 58:
       case 59:
         return 30;
       case 60:
       case 61:
         return 31;
       case 62:
       case 63:
         return 32;
     }
     if (wis >= 64) {
       return 33;
     }
     return 1;
   }


   public static int calcStatMr(int type, int wis) {
     int mr = 0;
     switch (type) {
       case 0:
       case 8:
       case 9:
         if (11 <= wis && wis <= 65) {
           mr = 10 + (wis - 10) * 4; break;
         }  if (wis > 65)
           mr = 230;
         break;
       case 1:
       case 7:
         if (11 <= wis && wis <= 65) {
           mr = (wis - 10) * 4; break;
         }  if (wis > 65)
           mr = 220;
         break;
       case 2:
         if (12 <= wis && wis <= 65) {
           mr = 25 + (wis - 10) * 4; break;
         }  if (wis > 65)
           mr = 245;
         break;
       case 3:
         if (14 <= wis && wis <= 65) {
           mr = 15 + (wis - 10) * 4; break;
         }  if (wis > 65)
           mr = 235;
         break;
       case 4:
         if (10 <= wis && wis <= 65) {
           mr = 10 + (wis - 10) * 4; break;
         }  if (wis > 65)
           mr = 230;
         break;
       case 5:
         if (10 <= wis && wis <= 65) {
           mr = 18 + (wis - 10) * 4; break;
         }  if (wis > 65)
           mr = 238;
         break;
       case 6:
         if (14 <= wis && wis <= 65) {
           mr = 20 + (wis - 10) * 4; break;
         }  if (wis > 65) {
           mr = 240;
         }
         break;
     }

     return mr;
   }

   public static int calcHpr(int con) {
     switch (con) {
       case 12:
       case 13:
         return 6;
       case 14:
       case 15:
         return 7;
       case 16:
       case 17:
         return 8;
       case 18:
       case 19:
         return 9;
       case 20:
       case 21:
         return 10;
       case 22:
       case 23:
         return 11;
       case 24:
         return 12;
       case 25:
         return 13;
       case 26:
       case 27:
         return 14;
       case 28:
       case 29:
         return 15;
       case 30:
       case 31:
         return 16;
       case 32:
       case 33:
         return 17;
       case 34:
         return 18;
       case 35:
         return 19;
       case 36:
       case 37:
         return 20;
       case 38:
       case 39:
         return 21;
       case 40:
       case 41:
         return 22;
       case 42:
       case 43:
         return 23;
       case 44:
         return 24;
       case 45:
         return 27;
       case 46:
       case 47:
         return 28;
       case 48:
       case 49:
         return 29;
       case 50:
       case 51:
         return 30;
       case 52:
       case 53:
         return 31;
       case 54:
         return 32;
       case 55:
         return 33;
       case 56:
       case 57:
         return 34;
       case 58:
       case 59:
         return 35;
       case 60:
       case 61:
         return 41;
       case 62:
       case 63:
         return 42;
       case 64:
         return 43;
     }
     if (con >= 65) {
       return 44;
     }
     return 5;
   }


   public static int calcHprPotion(int con) {
     if (con >= 65)
       return 9;
     if (con >= 60)
       return 8;
     if (con >= 55)
       return 7;
     if (con >= 50)
       return 6;
     if (con >= 45)
       return 5;
     if (con >= 40)
       return 4;
     if (con >= 35)
       return 3;
     if (con >= 30)
       return 2;
     if (con >= 20)
       return 1;
     return 0;
   }

   public static short PureHp(int type, int Con) {
     int randomhp = 0;
     if (Con <= 12) {
       randomhp = 12;
     } else if (Con <= 25) {
       randomhp = Con;
     } else if (Con <= 26) {
       randomhp = 25;
     } else if (Con <= 28) {
       randomhp = 26;
     } else if (Con <= 30) {
       randomhp = 27;
     } else if (Con <= 32) {
       randomhp = 28;
     } else if (Con <= 34) {
       randomhp = 29;
     } else if (Con <= 36) {
       randomhp = 30;
     } else if (Con <= 38) {
       randomhp = 31;
     } else if (Con <= 40) {
       randomhp = 32;
     } else if (Con <= 42) {
       randomhp = 33;
     } else if (Con <= 44) {
       randomhp = 34;
     } else if (Con <= 46) {
       randomhp = 35;
     } else if (Con <= 48) {
       randomhp = 36;
     } else if (Con <= 50) {
       randomhp = 37;
     } else if (Con <= 52) {
       randomhp = 38;
     } else if (Con <= 54) {
       randomhp = 39;
     } else if (Con <= 56) {
       randomhp = 40;
     } else if (Con <= 58) {
       randomhp = 41;
     } else if (Con <= 60) {
       randomhp = 42;
     } else if (Con <= 62) {
       randomhp = 43;
     } else if (Con <= 64) {
       randomhp = 44;
     } else {
       randomhp = 45;
     }  if (type == 1 || type == 7 || type == 8) {
       randomhp += 5;
     } else if (type == 9) {
       randomhp += 15;
     } else if (type == 2) {
       randomhp -= 2;
     } else if (type == 3) {
       randomhp -= 5;
     } else if (type == 4) {
       randomhp--;
     } else if (type == 5) {
       randomhp++;
     } else if (type == 6) {
       randomhp -= 3;
     }  return (short)randomhp;
   }

   public static int getMaxWeight(int str, int con) {
     int total = str + con;
     if (total > 150) {
       total = 150;
     }
     if (total > 0)
       total /= 2;
     return 1000 + total * 100;
   }


   public static int calcErByLevel(int charType, int level) {
     int result = 0;

     switch (charType) {
       case 0:
       case 2:
         result += level / 6;
         break;
       case 1:
       case 4:
       case 7:
       case 8:
       case 9:
         result += level / 4;
         break;
       case 3:
         result += level / 10;
         break;
       case 5:
         result += level / 5;
         break;
       case 6:
         result += level / 9;
         break;
     }

     return result;
   }

   public static int calcAinhasadStatFirst(int value) {
     int point = 0;
     switch (value)
     { case 1:
         point = 1;


         return point;case 2: point = 2; return point;case 3: point = 3; return point;case 4: point = 3; return point;case 5: point = 3; return point;case 6: point = 4; return point;case 7: point = 5; return point;case 8: point = 6; return point;case 9: point = 6; return point;case 10: point = 6; return point;case 11: point = 7; return point;case 12: point = 8; return point;case 13: point = 9; return point;case 14: point = 9; return point;case 15: point = 9; return point;case 16: point = 10; return point;case 17: point = 11; return point;case 18: point = 12; return point;case 19: point = 12; return point;case 20: point = 12; return point;case 21: point = 13; return point;case 22: point = 14; return point;case 23: point = 15; return point;case 24: point = 15; return point;case 25: point = 15; return point; }  point = 0; return point;
   }

   public static int calcAinhasadStatSecond(int value) {
     int point = 0;
     switch (value)
     { case 1:
         point = 0;



         return point;case 2: point = 0; return point;case 3: point = 0; return point;case 4: point = 1; return point;case 5: point = 2; return point;case 6: point = 2; return point;case 7: point = 2; return point;case 8: point = 2; return point;case 9: point = 3; return point;case 10: point = 4; return point;case 11: point = 4; return point;case 12: point = 4; return point;case 13: point = 4; return point;case 14: point = 5; return point;case 15: point = 6; return point;case 16: point = 6; return point;case 17: point = 6; return point;case 18: point = 6; return point;case 19: point = 7; return point;case 20: point = 8; return point;case 21: point = 8; return point;case 22: point = 8; return point;case 23: point = 8; return point;case 24: point = 9; return point;case 25: point = 10; return point; }  point = 0; return point;
   }
 }



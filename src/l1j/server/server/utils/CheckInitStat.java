 package l1j.server.server.utils;

 import l1j.server.server.model.Instance.L1PcInstance;











 public class CheckInitStat
 {
   public static boolean CheckPcStat(L1PcInstance pc) {
     if (pc == null) {
       return false;
     }
     if (pc.isGm()) {
       return true;
     }

     int str = pc.getAbility().getBaseStr();
     int dex = pc.getAbility().getBaseDex();
     int cha = pc.getAbility().getBaseCha();
     int con = pc.getAbility().getBaseCon();
     int intel = pc.getAbility().getBaseInt();
     int wis = pc.getAbility().getBaseWis();
     int basestr = 0;
     int basedex = 0;
     int basecon = 0;
     int baseint = 0;
     int basewis = 0;
     int basecha = 0;
     switch (pc.getType()) {
       case 0:
         basestr = 13;
         basedex = 9;
         basecon = 11;
         basewis = 11;
         basecha = 11;
         baseint = 9;
         break;
       case 1:
         basestr = 16;
         basedex = 12;
         basecon = 16;
         basewis = 9;
         basecha = 10;
         baseint = 8;
         break;
       case 2:
         basestr = 10;
         basedex = 12;
         basecon = 12;
         basewis = 12;
         basecha = 9;
         baseint = 12;
         break;
       case 3:
         basestr = 8;
         basedex = 7;
         basecon = 12;
         basewis = 14;
         basecha = 8;
         baseint = 14;
         break;
       case 4:
         basestr = 15;
         basedex = 12;
         basecon = 12;
         basewis = 10;
         basecha = 7;
         baseint = 11;
         break;
       case 5:
         basestr = 13;
         basedex = 11;
         basecon = 14;
         basewis = 10;
         basecha = 8;
         baseint = 10;
         break;
       case 6:
         basestr = 9;
         basedex = 10;
         basecon = 12;
         basewis = 14;
         basecha = 8;
         baseint = 12;
         break;
       case 7:
         basestr = 16;
         basedex = 13;
         basecon = 16;
         basewis = 7;
         basecha = 9;
         baseint = 10;
         break;
       case 8:
         basestr = 16;
         basedex = 13;
         basecon = 15;
         basewis = 11;
         basecha = 5;
         baseint = 11;
         break;
       case 9:
         basestr = 14;
         basedex = 12;
         basecon = 16;
         basewis = 12;
         basecha = 6;
         baseint = 9;
         break;
     }

     if (str < basestr || dex < basedex || con < basecon || cha < basecha || intel < baseint || wis < basewis) {
       return false;
     }
     return true;
   }
 }



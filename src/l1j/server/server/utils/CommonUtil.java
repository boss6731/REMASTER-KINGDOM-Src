 package l1j.server.server.utils;

 import java.sql.Timestamp;
 import java.text.NumberFormat;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Locale;
 import java.util.Random;
 import l1j.server.CPMWReNewClan.ClanDungeon.ClanDugeon;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;














 public class CommonUtil
 {
   public static String numberFormat(int number) {
     try {
       NumberFormat nf = NumberFormat.getInstance();

       return nf.format(number);
     } catch (Exception e) {
       return Integer.toString(number);
     }
   }






   public static int random(int number) {
     Random rnd = new Random();
     return rnd.nextInt(number);
   }







   public static int random(int lbound, int ubound) {
     return (int)(Math.random() * (ubound - lbound + 1) + lbound);
   }






   public static String dateFormat(String type) {
     SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.KOREA);
     return sdf.format(Calendar.getInstance().getTime());
   }






   public static String dateFormat(String type, Timestamp date) {
     SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.KOREA);
     return sdf.format(Long.valueOf(date.getTime()));
   }






   public static void SetTodayDeleteTime(L1ItemInstance item, int minute) {
     Timestamp deleteTime = null;
     deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
     item.setEndTime(deleteTime);
   }






   public static void SetHotelTownName(L1ItemInstance item, String townname) {
     item.setHotel_Town(townname);
   }





   public static void SetTodayDeleteTime(L1ItemInstance item) {
     int hour = Integer.parseInt(dateFormat("HH"));
     int minute = Integer.parseInt(dateFormat("mm"));
     int time = 0;

     if (hour <= (ClanDugeon.getInstance()).ClanDugeonInfo.hour_Clan && minute < 30) {
       time = ((ClanDugeon.getInstance()).ClanDugeonInfo.hour_Clan - hour) * 60 + 60 - minute - 60;
     } else {
       time = ((ClanDugeon.getInstance()).ClanDugeonInfo.hour_Clan - hour) * 60 + 60 - minute - 60 + 1440;
     }

     Timestamp deleteTime = null;

     deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * time));
     item.setEndTime(deleteTime);
   }


   public static int getRestTime(int hh) {
     int hour = Integer.parseInt(dateFormat("HH"));
     int minute = Integer.parseInt(dateFormat("mm"));

     int time = 0;

     time = (hh - hour) * 60 - minute;

     return time;
   }



   public static int get_current(int i, int min, int max) {
     int current = i;
     if (current <= min) {
       current = min;
     } else if (current >= max) {
       current = max;
     }
     return current;
   }


   public static boolean teleport_check(L1PcInstance pc, L1ItemInstance item) {
     if (pc.isDead())
       return true;
     if (pc.isParalyzed() || pc.isSleeped())
       return true;
     if ((pc.hasSkillEffect(123) || pc.hasSkillEffect(87) || pc.hasSkillEffect(51006) || pc.hasSkillEffect(70705) || pc.hasSkillEffect(208) || pc
       .hasSkillEffect(243) || pc.hasSkillEffect(5037) || pc
       .hasSkillEffect(157) || pc.hasSkillEffect(230) || pc.hasSkillEffect(242) || pc
       .hasSkillEffect(5027) || pc.hasSkillEffect(5002) || pc.hasSkillEffect(5056)) && item.getItem().getType() == 17)
       return true;
     if (pc.isGm())
       return false;
     if ((!pc.getInventory().checkItem(900111) || !pc.getMap().isRuler()) && !pc.getMap().isEscapable() && item.getItem().getType() == 0 && item.getItem().getType() == 17) {
       return true;
     }
     return false;
   }

   public static boolean isNumber(String number) {
     boolean flag = true;
     if (number == null || "".equals(number)) {
       return false;
     }
     int size = number.length();
     int st_no = 0;

     if (number.charAt(0) == '-') {
       st_no = 1;
     }
     for (int i = st_no; i < size; i++) {
       if ('0' > number.charAt(i) || '9' < number.charAt(i)) {
         flag = false;

         break;
       }
     }
     return flag;
   }
 }



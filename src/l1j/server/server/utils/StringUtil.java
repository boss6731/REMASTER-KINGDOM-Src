     package l1j.server.server.utils;

     import java.text.SimpleDateFormat;
     import java.util.ArrayList;
     import java.util.Calendar;
     import java.util.Collection;
     import java.util.Date;
     import java.util.HashSet;
     import java.util.List;
     import java.util.Map;
     import java.util.Set;
     import l1j.server.server.model.gametime.RealTimeClock;

     public class StringUtil {
       public static final char DirectorySeparatorChar = '/';
       public static final char Eof = '￿';
       public static final String EmptyString = "";
       public static final String EmptyOneString = " ";
       public static final String PlusString = "+";
       public static final String MinusString = "-";
       public static final String ZeroString = "0";
       public static final String OneString = "1";
       public static final String SlushString = "/";
       public static final String CommaString = ",";
       public static final String ColonString = ":";
       public static final String PeriodString = ".";
       public static final String DollarString = "$";
       public static final String AndString = "&";
       public static final String EqualsString = "=";
       public static final String TrueString = "true";
       public static final String FalseString = "false";
       public static final String LineString = "\r\n";
       private static final int SyllablesKorMin = 44032;
       private static final int SyllablesKorMax = 55203;
       private static final int LowerAlphaMin = 97;
       private static final int LowerAlphaMax = 122;
       private static final int UpperAlphaMin = 65;
       private static final int UpperAlphaMax = 90;
       private static final int DigitMin = 48;
       private static final int DigitMax = 57;
       private static final HashSet<Integer> SpecialCharacters;
       public static final String DateFormatString = "yyyy-MM-dd";
       public static final String DateFormatStringMinut = "yyyy-MM-dd HH:mm";
       public static final String DateFormatStringSeconds = "yyyy-MM-dd HH:mm:ss";

       static {
         byte[] special_characters = { 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 64, 91, 92, 93, 94, 95, 96, 123, 124, 125, 126, 58, 59, 60, 61, 62, 63 };




         SpecialCharacters = new HashSet<>(special_characters.length + 1);
         for (byte b : special_characters)
           SpecialCharacters.add(new Integer(b));
       }

       public static boolean isDirectorySeparatorChar(char c) {
         return (c == '/');
       }

       public static boolean is_special_character(char c) {
         return is_special_character(c);
       }

       public static boolean is_special_character(int c) {
         return SpecialCharacters.contains(Integer.valueOf(c));
       }

       public static boolean is_digit(char c) {
         return is_digit(c);
       }

       public static boolean is_digit(int c) {
         return IntRange.includes(c, 48, 57);
       }

       public static boolean is_upper_alpha(char c) {
         return is_upper_alpha(c);
       }

       public static boolean is_upper_alpha(int c) {
         return IntRange.includes(c, 65, 90);
       }

       public static boolean is_lower_alpha(char c) {
         return is_lower_alpha(c);
       }

       public static boolean is_lower_alpha(int c) {
         return IntRange.includes(c, 97, 122);
       }

       public static boolean is_syllables_kor(char c) {
         return is_syllables_kor(c);
       }

       public static boolean is_syllables_kor(int c) {
         return IntRange.includes(c, 44032, 55203);
       }





         public static String parse_money_string(int price) {
             if (price >= 10000) {
                 int ten_thousand = price / 10000; // 計算萬位數
                 int thousand = price % 10000; // 計算剩餘的千位數
                // 如果千位數為0，則僅返回萬位數；否則返回萬位數和千位數
                 return (thousand == 0) ? String.format("%,d萬", new Object[] { Integer.valueOf(ten_thousand) }) : String.format("%,d萬%,d", new Object[] { Integer.valueOf(ten_thousand), Integer.valueOf(thousand) });
             }
            // 如果價格小於10000，直接返回格式化的價格
             return String.format("%,d", new Object[] { Integer.valueOf(price) });
         }

       public static boolean isNullOrEmpty(String s) {
         return (s == null || s.equals("") || s.length() <= 0);
       }

       public static String replace(String source_string, String old_string, String new_string) {
         if (isNullOrEmpty(source_string)) {
           return "";
         }
         int old_string_length = old_string.length();
         if (old_string_length <= 0) {
           return source_string;
         }
         int i = source_string.lastIndexOf(old_string);
         if (i < 0) {
           return source_string;
         }
         StringBuilder sb = new StringBuilder(source_string);
         while (i >= 0) {
           sb.replace(i, i + old_string_length, new_string);
           i = source_string.lastIndexOf(old_string, i - 1);
         }
         return sb.toString();
       }

       public static String replace(String source_string, ArrayList<KeyValuePair<String, String>> params) {
         if (isNullOrEmpty(source_string)) {
           return "";
         }
         StringBuilder sb = new StringBuilder(source_string);
         for (KeyValuePair<String, String> pair : params) {
           int old_string_length = ((String)pair.key).length();
           int i = sb.lastIndexOf((String)pair.key);
           if (i < 0) {
             continue;
           }
           while (i >= 0) {
             sb.replace(i, i + old_string_length, (String)pair.value);
             i = sb.lastIndexOf((String)pair.key, i - 1);
           }
         }
         return sb.toString();
       }

       public static long convert_datetime_millis(String dateString, String formatString) {
         SimpleDateFormat sdf = new SimpleDateFormat(formatString);
         try {
           Date date = sdf.parse(dateString);
           return date.getTime();
         } catch (Exception e) {
           e.printStackTrace();

           return 0L;
         }
       }
       public static String remainTimeString(int remainSeconds) {
         int remain = remainSeconds;
         int day = 0;
         int hour = 0;
         int minute = 0;
         int seconds = 0;
         if (remain >= 86400) {
           day = remain / 86400;
           remain %= 86400;
         }
         if (remain >= 3600) {
           hour = remain / 3600;
           remain %= 3600;
         }
         if (remain >= 60) {
           minute = remain / 60;
           remain %= 60;
         }
         if (remain > 0) {
           seconds = remain;
         }
           StringBuilder sb = new StringBuilder();
           if (day > 0) {
// 如果天數大於0
               sb.append(day).append("日 ").append(hour).append("小時 ").append(minute).append("分鐘 ").append(seconds).append("秒 剩餘。");
           } else if (hour > 0) {
// 如果小時數大於0
               sb.append(hour).append("小時 ").append(minute).append("分鐘 ").append(seconds).append("秒 剩餘。");
           } else if (minute > 0) {
// 如果分鐘數大於0
               sb.append(minute).append("分鐘 ").append(seconds).append("秒 剩餘。");
           } else {
// 如果僅剩秒數
               sb.append(seconds).append("秒 剩餘。");
           }
           return sb.toString();

       public static long convert_datetime_millis(String dateString) {
         return convert_datetime_millis(dateString, "yyyy-MM-dd HH:mm:ss");
       }

       public static String convert_datetime_string(Calendar cal) {
         return String.format("%04d-%02d-%02d %02d:%02d:%02d", new Object[] {
               Integer.valueOf(cal.get(1)), Integer.valueOf(cal.get(2) + 1), Integer.valueOf(cal.get(5)),
               Integer.valueOf(cal.get(11)), Integer.valueOf(cal.get(12)), Integer.valueOf(cal.get(13)) });
       }

       public static String convert_date_string(Calendar cal) {
         return String.format("%04d-%02d-%02d", new Object[] { Integer.valueOf(cal.get(1)), Integer.valueOf(cal.get(2) + 1), Integer.valueOf(cal.get(5)) });
       }

       public static String get_current_datetime() {
         return convert_datetime_string(RealTimeClock.getInstance().getRealTimeCalendar());
       }

       public static String get_current_date() {
         return convert_date_string(RealTimeClock.getInstance().getRealTimeCalendar());
       }







       public static String concatToken(String tok, Collection<String> args) {
         if (args == null || args.size() <= 0) {
           return "";
         }

         String t = "";
         StringBuilder sb = new StringBuilder(args.size() * (8 + tok.length()));
         for (String s : args) {
           sb.append(t).append(s);
           t = tok;
         }
         return sb.toString();
       }







       public static <T> String concatTokenGeneric(String tok, Collection<T> args) {
         if (args == null || args.size() <= 0) {
           return "";
         }

         String t = "";
         StringBuilder sb = new StringBuilder(args.size() * (8 + tok.length()));
         for (T s : args) {
           sb.append(t).append(String.valueOf(s));
           t = tok;
         }
         return sb.toString();
       }

       public static String joinAsOnce(CharSequence delimiter, String s1, String s2) {
         return (new StringBuilder(s1.length() + s2.length() + delimiter.length() + 4))
           .append(s1)
           .append(delimiter)
           .append(s2)
           .toString();
       }







       public static String join(CharSequence delimiter, String... args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
         for (String s : args) {
           sb.append(t).append(s);
           t = delimiter;
         }
         return sb.toString();
       }







       public static <T> String join(CharSequence delimiter, Collection<T> args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.size() * (8 + delimiter.length()));
         for (T s : args) {
           sb.append(t).append(s);
           t = delimiter;
         }
         return sb.toString();
       }







       public static <T> String join(CharSequence delimiter, T[] args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
         for (T arg : args) {
           sb.append(t).append(arg);
           t = delimiter;
         }
         return sb.toString();
       }







       public static String join(CharSequence delimiter, byte[] args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
         for (byte arg : args) {
           sb.append(t).append(arg);
           t = delimiter;
         }
         return sb.toString();
       }







       public static String join(CharSequence delimiter, boolean[] args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (5 + delimiter.length()));
         for (boolean arg : args) {
           sb.append(t).append(arg);
           t = delimiter;
         }
         return sb.toString();
       }







       public static String join(CharSequence delimiter, char[] args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
         for (char arg : args) {
           sb.append(t).append(arg);
           t = delimiter;
         }
         return sb.toString();
       }







       public static <T> String join(CharSequence delimiter, short[] args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
         for (short arg : args) {
           sb.append(t).append(arg);
           t = delimiter;
         }
         return sb.toString();
       }







       public static <T> String join(CharSequence delimiter, int[] args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (4 + delimiter.length()));
         for (int arg : args) {
           sb.append(t).append(arg);
           t = delimiter;
         }
         return sb.toString();
       }







       public static <T> String join(CharSequence delimiter, long[] args) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
         for (long arg : args) {
           sb.append(t).append(arg);
           t = delimiter;
         }
         return sb.toString();
       }








       public static String join(CharSequence delimiter, byte[] args, int radix) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (1 + delimiter.length()));
         for (byte arg : args) {
           sb.append(t);
           if (radix == 16) {
             sb.append(Integer.toHexString(Byte.toUnsignedInt(arg)));
           } else {
             sb.append(arg);
           }
           t = delimiter;
         }
         return sb.toString();
       }









       public static String join(CharSequence delimiter, char[] args, int radix) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
         for (char arg : args) {
           sb.append(t);
           if (radix == 16) {
             sb.append(Integer.toHexString(arg));
           } else {
             sb.append(arg);
           }
           t = delimiter;
         }
         return sb.toString();
       }








       public static String join(CharSequence delimiter, short[] args, int radix) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (2 + delimiter.length()));
         for (short arg : args) {
           sb.append(t);
           if (radix == 16) {
             sb.append(Integer.toHexString(arg));
           } else {
             sb.append(arg);
           }
           t = delimiter;
         }
         return sb.toString();
       }








       public static String join(CharSequence delimiter, int[] args, int radix) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (4 + delimiter.length()));
         for (int arg : args) {
           sb.append(t);
           if (radix == 16) {
             sb.append(Integer.toHexString(arg));
           } else {
             sb.append(arg);
           }
           t = delimiter;
         }
         return sb.toString();
       }








       public static String join(CharSequence delimiter, long[] args, int radix) {
         CharSequence t = "";
         StringBuilder sb = new StringBuilder(args.length * (8 + delimiter.length()));
         for (long arg : args) {
           sb.append(t);
           if (radix == 16) {
             sb.append(Long.toHexString(arg));
           } else {
             sb.append(arg);
           }
           t = delimiter;
         }
         return sb.toString();
       }









       public static <T> String join(CharSequence delimiter, CharSequence title, Iterable<T> iterable, int size) {
         StringBuilder sb = new StringBuilder(size * (32 + title.length()));
         CharSequence tok = "";
         for (T t : iterable) {
           sb.append(tok)
             .append(title)
             .append(t);
           tok = delimiter;
         }
         return sb.toString();
       }








       public static <T> String join(CharSequence delimiter, CharSequence title, Collection<T> collection) {
         return join(delimiter, title, collection, collection.size());
       }









       public static <K, V> String join(CharSequence delimiter, String title, Iterable<Map.Entry<K, V>> iterable, int size) {
         StringBuilder sb = new StringBuilder(size * (32 + title.length()));
         CharSequence tok = "";

         for (Map.Entry<K, V> entry : iterable) {
           sb.append(tok)
             .append(title)
             .append(String.valueOf(entry.getKey()))
             .append("=")
             .append(String.valueOf(entry.getValue()));
           tok = delimiter;
         }
         return sb.toString();
       }








       public static <K, V> String join(CharSequence delimiter, String title, Set<Map.Entry<K, V>> entrySet) {
         return join(delimiter, title, entrySet, entrySet.size());
       }








       public static <K, V> String join(CharSequence delimiter, String title, Map<K, V> map) {
         return join(delimiter, title, map.entrySet(), map.size());
       }










       public static <K, V> String join(CharSequence delimiter, String title, Iterable<Map.Entry<K, List<V>>> iterable, int size, CharSequence valuesDelimiter) {
         StringBuilder sb = new StringBuilder(size * (64 + title.length()));
         CharSequence tok = "";
         for (Map.Entry<K, List<V>> entry : iterable) {
           List<V> list = entry.getValue();
           sb.append(tok)
             .append(title)
             .append(String.valueOf(entry.getKey()))
             .append("=[")
             .append(join(valuesDelimiter, list))
             .append("]");
           tok = delimiter;
         }
         return sb.toString();
       }









       public static <K, V> String join(CharSequence delimiter, String title, Set<Map.Entry<K, List<V>>> entrySet, CharSequence valuesDelimiter) {
         return join(delimiter, title, entrySet, entrySet.size(), valuesDelimiter);
       }

       public static int tryParseInt(String s, int defaultValue) {
         try {
           return Integer.parseInt(s);
         } catch (Exception exception) {
           return defaultValue;
         }
       }
       public static long tryParseLong(String s, long defaultValue) {
         try {
           return Long.parseLong(s);
         } catch (Exception exception) {
           return defaultValue;
         }
       }
       public static boolean tryParseBool(String s, boolean defaultValue) {
         try {
           return Boolean.parseBoolean(s);
         } catch (Exception exception) {
           return defaultValue;
         }
       }





       public static String concat(String... args) {
         StringBuilder sb = new StringBuilder(args.length * 8);
         for (String s : args)
           sb.append(s);
         return sb.toString();
       }






       public static String concat(Collection<String> args) {
         StringBuilder sb = new StringBuilder(args.size() * 8);
         for (String s : args)
           sb.append(s);
         return sb.toString();
       }
     }



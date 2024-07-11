 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;

 public class L1TransFormCheck
 {
     private static Map<Integer, ArrayList<Integer>> _transform_list = new HashMap<>();
     private static ArrayList<Integer> list;

     public static void removeTransFormList(int mapid, int monid) {
         list = _transform_list.get(Integer.valueOf(mapid));
         if (list == null) {
             return;
         }
         if (list.contains(Integer.valueOf(monid))) {
             list.remove(Integer.valueOf(monid));
         }
     }

     public static void addTransFormList(int mapid, int monid) {
         list = _transform_list.get(Integer.valueOf(mapid));

         if (list == null) {
             list = new ArrayList<>();
         }
         list.add(Integer.valueOf(monid));
         _transform_list.put(Integer.valueOf(mapid), list);
     }


     public static boolean isTransFormList(int mapid, int monid) {
         list = _transform_list.get(Integer.valueOf(mapid));
         if (list == null) {
             return false;
         }

         for (Integer i : list) {
             if (i.intValue() == monid) {
                 return true;
             }
         }

         return false;
     }
 }



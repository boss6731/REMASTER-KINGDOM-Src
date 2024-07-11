 package l1j.server.server.model.npc.action;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import l1j.server.server.utils.IterableElementList;
 import org.w3c.dom.Element;
 import org.w3c.dom.NodeList;


 public class L1NpcXmlParser
 {
   public static List<L1NpcAction> listActions(Element element) {
     List<L1NpcAction> result = new ArrayList<>();
     NodeList list = element.getChildNodes();
     L1NpcAction action = null;
     for (Element elem : new IterableElementList(list)) {
       action = L1NpcActionFactory.newAction(elem);
       if (action != null) {
         result.add(action);
       }
     }
     return result;
   }

   public static Element getFirstChildElementByTagName(Element element, String tagName) {
     IterableElementList list = new IterableElementList(element.getElementsByTagName(tagName));
     Iterator<Element> iterator = list.iterator(); if (iterator.hasNext()) { Element elem = iterator.next();
       return elem; }

     return null;
   }

   public static int getIntAttribute(Element element, String name, int defaultValue) {
     int result = defaultValue;
     try {
       result = Integer.valueOf(element.getAttribute(name)).intValue();
     } catch (NumberFormatException numberFormatException) {}

     return result;
   }

   public static boolean getBoolAttribute(Element element, String name, boolean defaultValue) {
     boolean result = defaultValue;
     String value = element.getAttribute(name);
     if (!value.equals("")) {
       result = Boolean.valueOf(value).booleanValue();
     }
     return result;
   }

   private static final Map<String, Integer> _questIds = new HashMap<>();
   static {
     _questIds.put("firstquest", Integer.valueOf(40));
     _questIds.put("level15", Integer.valueOf(1));
     _questIds.put("level30", Integer.valueOf(2));
     _questIds.put("level45", Integer.valueOf(3));
     _questIds.put("level50", Integer.valueOf(4));
     _questIds.put("lyra", Integer.valueOf(10));
     _questIds.put("oilskinmant", Integer.valueOf(11));
     _questIds.put("doromond", Integer.valueOf(20));
     _questIds.put("ruba", Integer.valueOf(21));
     _questIds.put("lukein", Integer.valueOf(23));
     _questIds.put("tbox1", Integer.valueOf(24));
     _questIds.put("tbox2", Integer.valueOf(25));
     _questIds.put("tbox3", Integer.valueOf(26));
     _questIds.put("cadmus", Integer.valueOf(31));
     _questIds.put("resta", Integer.valueOf(30));
     _questIds.put("kamyla", Integer.valueOf(32));
     _questIds.put("lizard", Integer.valueOf(34));
     _questIds.put("desire", Integer.valueOf(36));
     _questIds.put("shadows", Integer.valueOf(37));
     _questIds.put("icequeenring", Integer.valueOf(41));
   }

   public static int parseQuestId(String questId) {
     if (questId.equals("")) {
       return -1;
     }
     Integer result = _questIds.get(questId.toLowerCase());
     if (result == null) {
       throw new IllegalArgumentException();
     }
     return result.intValue();
   }

   public static int parseQuestStep(String questStep) {
     if (questStep.equals("")) {
       return -1;
     }
     if (questStep.equalsIgnoreCase("End")) {
       return 255;
     }
     return Integer.parseInt(questStep);
   }
 }



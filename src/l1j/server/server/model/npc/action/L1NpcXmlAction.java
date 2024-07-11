 package l1j.server.server.model.npc.action;

 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.npc.L1NpcHtml;
 import l1j.server.server.utils.IntRange;
 import org.w3c.dom.Element;


 public abstract class L1NpcXmlAction
   implements L1NpcAction
 {
   private String _name;
   private final int[] _npcIds;
   private final IntRange _level;
   private final int _questId;
   private final int _questStep;
   private final int[] _classes;

   public L1NpcXmlAction(Element element) {
     this._name = element.getAttribute("Name");
     this._name = this._name.equals("") ? null : this._name;
     this._npcIds = parseNpcIds(element.getAttribute("NpcId"));
     this._level = parseLevel(element);
     this._questId = L1NpcXmlParser.parseQuestId(element.getAttribute("QuestId"));
     this._questStep = L1NpcXmlParser.parseQuestStep(element.getAttribute("QuestStep"));

     this._classes = parseClasses(element);
   }

   private int[] parseClasses(Element element) {
     String classes = element.getAttribute("Class").toUpperCase();
     int[] result = new int[classes.length()];
     int idx = 0; char[] arrayOfChar; int i; byte b;
     for (arrayOfChar = classes.toCharArray(), i = arrayOfChar.length, b = 0; b < i; ) { Character cha = Character.valueOf(arrayOfChar[b]);
       result[idx++] = ((Integer)_charTypes.get(cha)).intValue(); b++; }

     Arrays.sort(result);
     return result;
   }

   private IntRange parseLevel(Element element) {
     int level = L1NpcXmlParser.getIntAttribute(element, "Level", 0);
     int min = L1NpcXmlParser.getIntAttribute(element, "LevelMin", 1);
     int max = L1NpcXmlParser.getIntAttribute(element, "LevelMax", 127);
     return (level == 0) ? new IntRange(min, max) : new IntRange(level, level);
   }

   private static final Map<Character, Integer> _charTypes = new HashMap<>();
   static {
     _charTypes.put(Character.valueOf('P'), Integer.valueOf(0));
     _charTypes.put(Character.valueOf('K'), Integer.valueOf(1));
     _charTypes.put(Character.valueOf('E'), Integer.valueOf(2));
     _charTypes.put(Character.valueOf('W'), Integer.valueOf(3));
     _charTypes.put(Character.valueOf('D'), Integer.valueOf(4));
     _charTypes.put(Character.valueOf('T'), Integer.valueOf(5));
     _charTypes.put(Character.valueOf('B'), Integer.valueOf(6));
   }

   private int[] parseNpcIds(String npcIds) {
     StringTokenizer tok = new StringTokenizer(npcIds.replace(" ", ""), ",");
     int[] result = new int[tok.countTokens()];
     for (int i = 0; i < result.length; i++) {
       result[i] = Integer.parseInt(tok.nextToken());
     }
     Arrays.sort(result);
     return result;
   }

   private boolean acceptsNpcId(L1Object obj) {
     if (0 < this._npcIds.length) {
       if (!(obj instanceof L1NpcInstance)) {
         return false;
       }
       int npcId = ((L1NpcInstance)obj).getNpcTemplate().get_npcId();

       if (Arrays.binarySearch(this._npcIds, npcId) < 0) {
         return false;
       }
     }
     return true;
   }

   private boolean acceptsLevel(int level) {
     return this._level.includes(level);
   }

   private boolean acceptsCharType(int type) {
     if (0 < this._classes.length &&
       Arrays.binarySearch(this._classes, type) < 0) {
       return false;
     }

     return true;
   }

   private boolean acceptsActionName(String name) {
     if (this._name == null) {
       return true;
     }
     return name.equals(this._name);
   }

   private boolean acceptsQuest(L1PcInstance pc) {
     if (this._questId == -1) {
       return true;
     }
     if (this._questStep == -1) {
       return (0 < pc.getQuest().get_step(this._questId));
     }
     return (pc.getQuest().get_step(this._questId) == this._questStep);
   }

   public boolean acceptsRequest(String actionName, L1PcInstance pc, L1Object obj) {
     if (!acceptsNpcId(obj)) {
       return false;
     }
     if (!acceptsLevel(pc.getLevel())) {
       return false;
     }
     if (!acceptsQuest(pc)) {
       return false;
     }
     if (!acceptsCharType(pc.getType())) {
       return false;
     }
     if (!acceptsActionName(actionName)) {
       return false;
     }
     return true;
   }



   public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, int amount) {
     return null;
   }

   public abstract L1NpcHtml execute(String paramString, L1PcInstance paramL1PcInstance, L1Object paramL1Object, byte[] paramArrayOfbyte);
 }



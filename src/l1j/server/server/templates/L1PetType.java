 package l1j.server.server.templates;

 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.utils.IntRange;




















 public class L1PetType
 {
   private final int _baseNpcId;
   private final L1Npc _baseNpcTemplate;
   private final String _name;
   private final int _itemIdForTaming;
   private final IntRange _hpUpRange;
   private final IntRange _mpUpRange;
   private final int _npcIdForEvolving;
   private final int[] _msgIds;
   private final int _defyMsgId;

   public L1PetType(int baseNpcId, String name, int itemIdForTaming, IntRange hpUpRange, IntRange mpUpRange, int npcIdForEvolving, int[] msgIds, int defyMsgId) {
     this._baseNpcId = baseNpcId;
     this._baseNpcTemplate = NpcTable.getInstance().getTemplate(baseNpcId);
     this._name = name;
     this._itemIdForTaming = itemIdForTaming;
     this._hpUpRange = hpUpRange;
     this._mpUpRange = mpUpRange;
     this._npcIdForEvolving = npcIdForEvolving;
     this._msgIds = msgIds;
     this._defyMsgId = defyMsgId;
   }

   public int getBaseNpcId() {
     return this._baseNpcId;
   }

   public L1Npc getBaseNpcTemplate() {
     return this._baseNpcTemplate;
   }

   public String getName() {
     return this._name;
   }

   public int getItemIdForTaming() {
     return this._itemIdForTaming;
   }

   public boolean canTame() {
     return (this._itemIdForTaming != 0);
   }

   public IntRange getHpUpRange() {
     return this._hpUpRange;
   }

   public IntRange getMpUpRange() {
     return this._mpUpRange;
   }

   public int getNpcIdForEvolving() {
     return this._npcIdForEvolving;
   }

   public boolean canEvolve() {
     return (this._npcIdForEvolving != 0);
   }

   public int getMessageId(int num) {
     if (num == 0) {
       return 0;
     }
     return this._msgIds[num - 1];
   }

   public static int getMessageNumber(int level) {
     if (50 <= level) {
       return 5;
     }
     if (48 <= level) {
       return 4;
     }
     if (36 <= level) {
       return 3;
     }
     if (24 <= level) {
       return 2;
     }
     if (12 <= level) {
       return 1;
     }
     return 0;
   }

   public int getDefyMessageId() {
     return this._defyMsgId;
   }
 }



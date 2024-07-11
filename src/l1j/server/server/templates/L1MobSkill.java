 package l1j.server.server.templates;






 public class L1MobSkill
   implements Cloneable
 {
   public static final int TYPE_NONE = 0;
   public static final int TYPE_PHYSICAL_ATTACK = 1;
   public static final int TYPE_MAGIC_ATTACK = 2;
   public static final int TYPE_SUMMON = 3;
   public static final int TYPE_POLY = 4;
   public static final int CHANGE_TARGET_NO = 0;
   public static final int CHANGE_TARGET_COMPANION = 1;
   public static final int CHANGE_TARGET_ME = 2;
   public static final int CHANGE_TARGET_RANDOM = 3;
   private final int skillSize;
   private int mobid;
   private String mobName;
   private int[] type;
   private int[] triRnd;
   int[] triHp;

   public L1MobSkill clone() {
     try {
       return (L1MobSkill)super.clone();
     } catch (Exception e) {
       e.printStackTrace();
       throw new InternalError(e.getMessage());
     }
   }
   int[] triCompanionHp; int[] triRange; int[] triCount; int[] changeTarget; int[] range; int[] areaWidth; int[] areaHeight; int[] leverage; int[] skillId; int[] gfxid; int[] actid; int[] summon; int[] summonMin; int[] summonMax; int[] polyId; String[] spellMent;
   public int getSkillSize() {
     return this.skillSize;
   }

   public L1MobSkill(int sSize) {
     this.skillSize = sSize;

     this.type = new int[this.skillSize];
     this.triRnd = new int[this.skillSize];
     this.triHp = new int[this.skillSize];
     this.triCompanionHp = new int[this.skillSize];
     this.triRange = new int[this.skillSize];
     this.triCount = new int[this.skillSize];
     this.changeTarget = new int[this.skillSize];
     this.range = new int[this.skillSize];
     this.areaWidth = new int[this.skillSize];
     this.areaHeight = new int[this.skillSize];
     this.leverage = new int[this.skillSize];
     this.skillId = new int[this.skillSize];
     this.gfxid = new int[this.skillSize];
     this.actid = new int[this.skillSize];
     this.summon = new int[this.skillSize];
     this.summonMin = new int[this.skillSize];
     this.summonMax = new int[this.skillSize];
     this.polyId = new int[this.skillSize];
     this.spellMent = new String[this.skillSize];
   }



   public int get_mobid() {
     return this.mobid;
   }

   public void set_mobid(int i) {
     this.mobid = i;
   }



   public String getMobName() {
     return this.mobName;
   }

   public void setMobName(String s) {
     this.mobName = s;
   }



   public int getType(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.type[idx];
   }

   public void setType(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.type[idx] = i;
   }



   public int getTriggerRandom(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.triRnd[idx];
   }

   public void setTriggerRandom(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.triRnd[idx] = i;
   }



   public int getTriggerHp(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.triHp[idx];
   }

   public void setTriggerHp(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.triHp[idx] = i;
   }



   public int getTriggerCompanionHp(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.triCompanionHp[idx];
   }

   public void setTriggerCompanionHp(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.triCompanionHp[idx] = i;
   }



   public int getTriggerRange(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.triRange[idx];
   }

   public void setTriggerRange(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.triRange[idx] = i;
   }

   public boolean isTriggerDistance(int idx, int distance) {
     int triggerRange = getTriggerRange(idx);

     if ((triggerRange < 0 && distance <= Math.abs(triggerRange)) || (triggerRange > 0 && distance >= triggerRange))
     {
       return true;
     }
     return false;
   }



   public int getTriggerCount(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.triCount[idx];
   }

   public void setTriggerCount(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.triCount[idx] = i;
   }



   public int getChangeTarget(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.changeTarget[idx];
   }

   public void setChangeTarget(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.changeTarget[idx] = i;
   }



   public int getRange(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.range[idx];
   }

   public void setRange(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.range[idx] = i;
   }



   public int getAreaWidth(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.areaWidth[idx];
   }

   public void setAreaWidth(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.areaWidth[idx] = i;
   }



   public int getAreaHeight(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.areaHeight[idx];
   }

   public void setAreaHeight(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.areaHeight[idx] = i;
   }



   public int getLeverage(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.leverage[idx];
   }

   public void setLeverage(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.leverage[idx] = i;
   }



   public int getSkillId(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.skillId[idx];
   }

   public void setSkillId(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.skillId[idx] = i;
   }



   public int getGfxid(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.gfxid[idx];
   }

   public void setGfxid(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.gfxid[idx] = i;
   }



   public int getActid(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.actid[idx];
   }

   public void setActid(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.actid[idx] = i;
   }



   public int getSummon(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.summon[idx];
   }

   public void setSummon(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.summon[idx] = i;
   }



   public int getSummonMin(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.summonMin[idx];
   }

   public void setSummonMin(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.summonMin[idx] = i;
   }



   public int getSummonMax(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.summonMax[idx];
   }

   public void setSummonMax(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.summonMax[idx] = i;
   }



   public int getPolyId(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return 0;
     }
     return this.polyId[idx];
   }

   public void setPolyId(int idx, int i) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.polyId[idx] = i;
   }


   public String getSpellMent(int idx) {
     if (idx < 0 || idx >= getSkillSize()) {
       return "";
     }
     return this.spellMent[idx];
   }

   public void setSpellMent(int idx, String s) {
     if (idx < 0 || idx >= getSkillSize()) {
       return;
     }
     this.spellMent[idx] = s;
   }
 }



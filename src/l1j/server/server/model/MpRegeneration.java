 package l1j.server.server.model;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.CalcStat;

 public class MpRegeneration
   extends RepeatTask
 {
   private static Logger _log = Logger.getLogger(MpRegeneration.class.getName());

   private final L1PcInstance _pc;

   private int _regenPoint = 0;

   private int _curPoint = 4;

   public MpRegeneration(L1PcInstance pc, long interval) {
     super(interval);
     this._pc = pc;
   }

   public void setState(int state) {
     if (this._curPoint < state) {
       return;
     }

     this._curPoint = state;
   }


   public void execute() {
     try {
       if (this._pc.isDead()) {
         return;
       }
       if (this._pc.getCurrentMp() == this._pc.getMaxMp()) {
         return;
       }

       this._regenPoint += this._curPoint;
       this._curPoint = 4;

       if (40 <= this._regenPoint) {
         this._regenPoint = 0;
         regenMp();
       }
     } catch (Exception e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }

   public void regenMp() {
     try {
       int baseMpr = 1;
       int wis = this._pc.getAbility().getTotalWis();
       if (wis == 15 || wis == 16) {
         baseMpr = 2;
       } else if (wis == 17) {
         baseMpr = 3;
       } else if (wis >= 18) {
         baseMpr += wis - 14;
       }

       int baseStatMpr = CalcStat.calcMpr(this._pc.getAbility().getBaseWis());

       if (this._pc.hasSkillEffect(20082) == true) {
         baseMpr++;
       } else if (this._pc.hasSkillEffect(1002) == true) {
         baseMpr += 2;
       }
       if (this._pc.hasSkillEffect(32) == true) {
         baseMpr += 5;
       }
       if (this._pc.hasSkillEffect(206) == true) {
         baseMpr += 2;
       }
       if (L1HouseLocation.isInHouse(this._pc.getX(), this._pc.getY(), this._pc.getMapId())) {
         baseMpr += 3;
       }
       if (this._pc.hasSkillEffect(3002) || this._pc.hasSkillEffect(3052)) {
         baseMpr += 3;
       }
       if (this._pc.hasSkillEffect(3020) || this._pc
         .hasSkillEffect(3070)) {
         baseMpr += 2;
       }
       if (this._pc.hasSkillEffect(3012) || this._pc.hasSkillEffect(3062)) {
         baseMpr += 2;
       }
       if (this._pc.hasSkillEffect(7894) == true) {
         baseMpr += 4;
       }
       if (isInn(this._pc)) {
         baseMpr += 3;
       }
       if (L1HouseLocation.isRegenLoc(this._pc, this._pc.getX(), this._pc.getY(), this._pc.getMapId())) {
         baseMpr += 3;
       }
       if (this._pc.hasSkillEffect(10526) == true) {
         baseMpr += 5;
       }

       int itemMpr = this._pc.getInventory().mpRegenPerTick();
       itemMpr += this._pc.getMpr();

       if (this._pc.get_food() < 24 || isOverWeight(this._pc)) {
         baseMpr = 0;
         if (itemMpr > 0) {
           itemMpr = 0;
         }
       }

       int mpr = baseMpr + itemMpr + baseStatMpr;
       int newMp = this._pc.getCurrentMp() + mpr;

       this._pc.setCurrentMp(newMp);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private boolean isOverWeight(L1PcInstance pc) {
     if (pc.hasSkillEffect(169) || pc.hasSkillEffect(176) || pc
       .hasSkillEffect(190)) {
       return false;
     }
     if (isInn(pc)) {
       return false;
     }
     return (120 <= pc.getInventory().getWeight100());
   }

   private boolean isInn(L1PcInstance pc) {
     int mapId = pc.getMapId();
     return (mapId == 16384 || mapId == 16896 || mapId == 17408 || mapId == 17492 || mapId == 17820 || mapId == 17920 || mapId == 18432 || mapId == 18944 || mapId == 19456 || mapId == 19968 || mapId == 20480 || mapId == 20992 || mapId == 21504 || mapId == 22016 || mapId == 22528 || mapId == 23040 || mapId == 23552 || mapId == 24064 || mapId == 24576 || mapId == 25088);
   }
 }



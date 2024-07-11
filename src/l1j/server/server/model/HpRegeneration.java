 package l1j.server.server.model;

 import java.util.Random;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1EffectInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.CalcStat;

 public class HpRegeneration
   extends RepeatTask
 {
   private static Logger _log = Logger.getLogger(HpRegeneration.class.getName());

   private final L1PcInstance _pc;

   private int _regenMax = 0;

   private int _regenPoint = 0;

   private int _curPoint = 4;

   private static Random _random = new Random(System.nanoTime());

   public HpRegeneration(L1PcInstance pc, long interval) {
     super(interval);
     this._pc = pc;
     updateLevel();
   }

   public void setState(int state) {
     if (this._curPoint < state) {
       return;
     }
     this._curPoint = state;
   }


   public void execute() {
     try {
       this._regenPoint += this._curPoint;
       this._curPoint = 4;

       synchronized (this) {
         if (this._regenMax <= this._regenPoint) {
           this._regenPoint = 0;
           regenHp();
         }
       }
       DanteasBuff(this._pc);
       clanbuff(this._pc);
       GotobokBuff(this._pc);
     } catch (Exception e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }

   public void updateLevel() {
     int[] lvlTable = { 30, 25, 20, 16, 14, 12, 11, 10, 9, 3, 2 };

     int regenLvl = Math.min(10, this._pc.getLevel());
     if (30 <= this._pc.getLevel() && this._pc.isKnight()) {
       regenLvl = 11;
     }

     synchronized (this) {
       this._regenMax = lvlTable[regenLvl - 1] * 4;
     }
   }


   public void regenHp() {
     try {
       if (this._pc.isDead()) {
         return;
       }
       if (this._pc.getCurrentHp() == this._pc.getMaxHp() && !isUnderwater(this._pc)) {
         return;
       }

       int maxBonus = 1;


       if (11 < this._pc.getLevel() && 14 <= this._pc.getAbility().getTotalCon()) {
         maxBonus = this._pc.getAbility().getTotalCon() - 12;
         if (25 < this._pc.getAbility().getTotalCon()) {
           maxBonus = 14;
         }
       }

       int basebonus = CalcStat.calcHpr(this._pc.getAbility().getBaseCon());

       int equipHpr = this._pc.getInventory().hpRegenPerTick();
       equipHpr += this._pc.getHpr();
       int bonus = _random.nextInt(maxBonus) + 1;

       if (L1HouseLocation.isInHouse(this._pc.getX(), this._pc.getY(), this._pc.getMapId())) {
         bonus += 5;
       }
       if (this._pc.hasSkillEffect(3012) || this._pc.hasSkillEffect(3062)) {
         bonus += 2;
       }
       if (this._pc.hasSkillEffect(3019) || this._pc
         .hasSkillEffect(3069)) {
         bonus += 2;
       }
       if (this._pc.hasSkillEffect(7893)) {
         bonus += 4;
       }
       if (isInn(this._pc)) {
         bonus += 5;
       }
       if (L1HouseLocation.isRegenLoc(this._pc, this._pc.getX(), this._pc.getY(), this._pc.getMapId())) {
         bonus += 5;
       }

       boolean inLifeStream = false;
       if (isPlayerInLifeStream(this._pc)) {
         inLifeStream = true;

         bonus += 3;
       }


       if (this._pc.get_food() < 24 || isOverWeight(this._pc)) {
         bonus = 0;
         basebonus = 0;


         if (equipHpr > 0) {
           equipHpr = 0;
         }
       }

       int newHp = this._pc.getCurrentHp();
       newHp += bonus + equipHpr + basebonus;

       if (newHp < 1) {
         newHp = 1;
       }


       if (isUnderwater(this._pc)) {

         newHp -= 100;
         if (newHp < 1) {
           if (this._pc.isGm()) {
             newHp = 1;
           } else {
             this._pc.death(null, true);
           }
         }
       }


       newHp -= 10;
       if (isLv50Quest(this._pc) && !inLifeStream && newHp < 1) {
         if (this._pc.isGm()) {
           newHp = 1;
         } else {
           this._pc.death(null, true);
         }
       }



       newHp -= 10;
       if (this._pc.getMapId() == 410 && !inLifeStream && newHp < 1) {
         if (this._pc.isGm()) {
           newHp = 1;
         } else {
           this._pc.death(null, true);
         }
       }

       if (!this._pc.isDead()) {
         this._pc.setCurrentHp(Math.min(newHp, this._pc.getMaxHp()));
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
     private void DanteasBuff(L1PcInstance pc) {
         // 如果玩家目前沒有丹特斯的Buff
         if (!pc.isDanteasBuff) {
             // 並且玩家位於地圖ID 479
             if (pc.getMapId() == 479) {
                 // 增加玩家的傷害值和弓箭傷害值
                 pc.addDmgup(2);
                 pc.addBowDmgup(2);
                 // 增加玩家的魔法能力值和魔法恢復值
                 pc.getAbility().addSp(1);
                 pc.addMpr(2);
                 // 設置玩家的丹特斯Buff狀態為真
                 pc.isDanteasBuff = true;
                 // 向玩家發送Buff效果的數據包以及系統信息
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(147, 5219, true));
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("丹特斯的Buff: 近距離/遠距離傷害+2, SP+1, MP恢復+2 "));
             }
         } else {
             boolean DanteasOk = false;
             // 如果玩家位於地圖ID 479
             if (pc.getMapId() == 479) {
                 DanteasOk = true;
             }
             // 如果玩家不再符合Buff條件
             if (!DanteasOk) {
                 // 減少玩家的傷害值和弓箭傷害值
                 pc.addDmgup(-2);
                 pc.addBowDmgup(-2);
                 // 減少玩家的魔法能力值和魔法恢復值

                 pc.getAbility().addSp(-1);
                 pc.addMpr(-2);
                 // 設置玩家的丹特斯Buff狀態為假
                 pc.isDanteasBuff = false;
                 // 向玩家發送Buff移除的數據包以及系統信息
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(147, 5219, false));
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("丹特斯的Buff: Buff已消失"));
             }
         }
     }
   private void GotobokBuff(L1PcInstance pc) {
     if (!pc.isGotobokBuff) {
       if (pc.getMapId() == 1710) {
         this._pc.addMpr(20);
         this._pc.addHpr(30);
         pc.isGotobokBuff = true;
       }

     } else {

       boolean GotobokOk = false;
       if (pc.getMapId() == 1710) {
         GotobokOk = true;
       }
       if (!GotobokOk) {
         this._pc.addMpr(-20);
         this._pc.addHpr(-30);
         pc.isGotobokBuff = false;
       }
     }
   }



   private void clanbuff(L1PcInstance pc) {
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     if (pc.getClanid() != 0 && (clan.getOnlineClanMember()).length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT && !pc.isClanBuff()) {
       pc.setSkillEffect(7789, 0L);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 450, true));
       pc.setClanBuff(true);
     } else if (pc.getClanid() != 0 && (clan.getOnlineClanMember()).length < Config.ServerAdSetting.CLANBUFFUSERCOUNT && pc.isClanBuff()) {
       pc.killSkillEffectTimer(7789);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 450, false));
       pc.setClanBuff(false);
     }
   }


   private boolean isUnderwater(L1PcInstance pc) {
     if (pc.getInventory().checkEquipped(20207)) {
       return false;
     }
     if (pc.hasSkillEffect(1003)) {
       return false;
     }
     if (pc.getInventory().checkEquipped(21048) && pc.getInventory().checkEquipped(21049) && pc
       .getInventory().checkEquipped(21050)) {
       return false;
     }

     return pc.getMap().isUnderwater();
   }



   private boolean isOverWeight(L1PcInstance pc) {
     if (pc.hasSkillEffect(169) || pc.hasSkillEffect(176) || pc
       .hasSkillEffect(190)) {
       return false;
     }
     if (pc.getInventory().checkEquipped(20049) || pc.getInventory().checkEquipped(900057)) {
       return false;
     }
     if (isInn(pc)) {
       return false;
     }

     return (120 <= pc.getInventory().getWeight100());
   }

   private boolean isLv50Quest(L1PcInstance pc) {
     int mapId = pc.getMapId();
     return (mapId == 2000 || mapId == 2001);
   }








   private static boolean isPlayerInLifeStream(L1PcInstance pc) {
     L1EffectInstance effect = null;
     for (L1Object object : pc.getKnownObjects()) {
       if (!(object instanceof L1EffectInstance)) {
         continue;
       }
       effect = (L1EffectInstance)object;
       if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance(pc.getLocation()) < 4) {
         return true;
       }
     }
     return false;
   }

   private boolean isInn(L1PcInstance pc) {
     int mapId = pc.getMapId();
     return (mapId == 16384 || mapId == 16896 || mapId == 17408 || mapId == 17492 || mapId == 17820 || mapId == 17920 || mapId == 18432 || mapId == 18944 || mapId == 19456 || mapId == 19968 || mapId == 20480 || mapId == 20992 || mapId == 21504 || mapId == 22016 || mapId == 22528 || mapId == 23040 || mapId == 23552 || mapId == 24064 || mapId == 24576 || mapId == 25088);
   }
 }



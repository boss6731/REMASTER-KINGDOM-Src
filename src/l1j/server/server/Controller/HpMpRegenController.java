 package l1j.server.server.Controller;

 import java.util.Random;
 import java.util.TimerTask;
 import l1j.server.Config;
 import l1j.server.server.model.Instance.L1EffectInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1HouseLocation;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.CalcStat;

 public class HpMpRegenController extends TimerTask {
   private final L1PcInstance _pc;
   private final int attackSec = 6;
   private final int moveSec = 2;
   private int oldLevel = 0;
   private int oldLoc = 0;
   private int regenTimeCount = 0;
   private int levelBaseSec = 0;

   private int _regenHpPoint;

   private int _regenMpPoint;
   private int _curMpPoint;

   private void levelBaseSecRefresh(int lv) {
     if (lv < 55) {
       this.levelBaseSec = 12;
     } else if (lv >= 55 && lv <= 65) {
       this.levelBaseSec = 8;
     } else if (lv > 65) {
       this.levelBaseSec = 4;
     }
     this.oldLevel = lv;
   }

   private void regenStatusRefresh() {
     if (this.oldLevel != this._pc.getLevel())
     {
       levelBaseSecRefresh(this._pc.getLevel());
     }
     if (this._pc.isPinkName()) {

       this.regenTimeCount = this.levelBaseSec + 6;
     } else {
       int newLoc = this._pc.getX() + this._pc.getY();

       if (this.oldLoc != newLoc) {

         this.regenTimeCount = this.levelBaseSec + 2;
         this.oldLoc = newLoc;
       } else {
         this.regenTimeCount = this.levelBaseSec;
       }
     }
   }

   private void hpregen() {
     if (this._pc.getCurrentHp() == this._pc.getMaxHp() && !isUnderwater(this._pc))
       return;
     this._regenHpPoint++;
     synchronized (this) {
       if (this.regenTimeCount <= this._regenHpPoint) {
         this._regenHpPoint = 0;
         regenHp();
       }
     }
   }

   public HpMpRegenController(L1PcInstance pc) { this._regenHpPoint = 0;

     this._regenMpPoint = 0;

     this._curMpPoint = 4;
     this._pc = pc;
     levelBaseSecRefresh(pc.getLevel()); } private static Random _random = new Random();

   private boolean isPcCk(L1PcInstance pc) {
     if (pc == null || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.noPlayerCK || pc.noPlayerck2) return true;
     return false;
   }

   public void run() {
     try {
       regenStatusRefresh();
       if (!isPcCk(this._pc)) {
         hpregen();
       }
       if (!isPcCk(this._pc)) {
         mpregen();
       }
       if (!isPcCk(this._pc)) {
         DanteasBuff();
       }
       if (!isPcCk(this._pc)) {
         clanbuff();
       }
       if (!isPcCk(this._pc)) {
         GotobokBuff();
       }
     } catch (Exception e) {


       e.printStackTrace();
     }
   }

   private void mpregen() {
     int nowMaxMp = this._pc.getMaxMp();
     if (this._pc.isDead()) {
       return;
     }
     if (this._pc.getCurrentMp() == nowMaxMp) {
       return;
     }
     this._regenMpPoint += this._curMpPoint;
     this._curMpPoint = 4;
     if (64 <= this._regenMpPoint) {
       this._regenMpPoint = 0;
       regenMp();
     }
   }

   public void regenMp() {
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
       baseMpr += 4;
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

     int itemMpr = this._pc.getInventory().mpRegenPerTick();
     itemMpr += this._pc.getMpr();
     if (this._pc.get_food() < 40 || isOverWeight(this._pc)) {
       baseMpr = 0;
       if (itemMpr > 0) {
         itemMpr = 0;
       }
       return;
     }
     int mpr = baseMpr + itemMpr + baseStatMpr;
     int newMp = this._pc.getCurrentMp() + mpr;

     this._pc.setCurrentMp(newMp);
   }
   public void regenHp() {
     if (this._pc.isDead()) {
       return;
     }
     if (this._pc.getCurrentHp() == this._pc.getMaxHp() && !isUnderwater(this._pc)) {
       return;
     }
     int maxBonus = 1;


     if (this._pc.getLevel() > 11 && this._pc.getAbility().getTotalCon() >= 14) {
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


     if (this._pc.get_food() < 40 || isOverWeight(this._pc)) {
       bonus = 0;
       basebonus = 0;
       equipHpr = 0;
     }






     int newHp = this._pc.getCurrentHp();
     newHp += bonus + equipHpr + basebonus;

     if (newHp < 1) {
       newHp = 1;
     }



     newHp -= 20;
     if (isUnderwater(this._pc) && newHp < 1) {
       if (this._pc.isGm()) {
         newHp = 1;
       } else {
         this._pc.death(null, true);
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
   }


   private boolean isUnderwater(L1PcInstance pc) {
     if (pc.getInventory().checkEquipped(20207)) {
       return false;
     }
     if (pc.hasSkillEffect(1003)) {
       return false;
     }
     if (pc.getInventory().checkEquipped(21048) && pc.getInventory().checkEquipped(21049) && pc.getInventory().checkEquipped(21050)) {
       return false;
     }
     return pc.getMap().isUnderwater();
   }



   private boolean isOverWeight(L1PcInstance pc) {
     if (pc.hasSkillEffect(169) || pc.hasSkillEffect(176) || pc.hasSkillEffect(190)) {
       return false;
     }

     if (pc.is_top_ranker()) {
       return false;
     }
     if (isInn(pc)) {
       return false;
     }
     return (50 <= pc.getInventory().getWeight100());
   }

   private boolean isLv50Quest(L1PcInstance pc) {
     int mapId = pc.getMapId();
     return (mapId == 2000 || mapId == 2001);
   }

     private void DanteasBuff() {
         if (!this._pc.isDanteasBuff) { // 註解: 如果玩家沒有受到Danteas的增益
             if (this._pc.getMapId() == 479) { // 註解: 如果玩家在地圖ID為479的地圖上
                 this._pc.addDmgup(2); // 註解: 增加近戰傷害+2
                 this._pc.addBowDmgup(2); // 註解: 增加遠程傷害+2
                 this._pc.getAbility().addSp(1); // 註解: 增加SP+1
                 this._pc.addMpr(2); // 註解: 增加MP恢復+2
                 this._pc.isDanteasBuff = true; // 註解: 設置玩家狀態為已受到Danteas的增益
                 this._pc.sendPackets((ServerBasePacket)new S_PacketBox(147, 5219, true)); // 註解: 發送增益狀態數據包，狀態為真
                 this._pc.sendPackets((ServerBasePacket)new S_SystemMessage("丹泰斯的增益: 近戰/遠程傷害+2, SP+1, MP恢復+2")); // 註解: 發送系統消息告知增益效果
             }
         } else { // 註解: 如果玩家已經受到了Danteas的增益
             boolean DanteasOk = false;
             if (this._pc.getMapId() == 479) { // 註解: 如果玩家仍然在地圖ID為479的地圖上
                 DanteasOk = true;
             }
             if (!DanteasOk) { // 註解: 如果玩家不在地圖ID為479的地圖上
                 this._pc.addDmgup(-2); // 註解: 減少近戰傷害-2
                 this._pc.addBowDmgup(-2); // 註解: 減少遠程傷害-2
                 this._pc.getAbility().addSp(-1); // 註解: 減少SP-1
                 this._pc.addMpr(-2); // 註解: 減少MP恢復-2
                 this._pc.isDanteasBuff = false; // 註解: 設置玩家狀態為未受到Danteas的增益
                 this._pc.sendPackets((ServerBasePacket)new S_PacketBox(147, 5219, false)); // 註解: 發送增益狀態數據包，狀態為假
                 this._pc.sendPackets((ServerBasePacket)new S_SystemMessage("丹泰斯的增益: 增益效果消失")); // 註解: 發送系統消息告知增益效果消失
             }
         }
     }

   private void GotobokBuff() {
     if (!this._pc.isGotobokBuff) {
       if (this._pc.getMapId() == 1710) {
         this._pc.addMpr(20);
         this._pc.addHpr(30);
         this._pc.isGotobokBuff = true;
       }

     } else {

       boolean GotobokOk = false;
       if (this._pc.getMapId() == 1710) {
         GotobokOk = true;
       }
       if (!GotobokOk) {
         this._pc.addMpr(-20);
         this._pc.addHpr(-30);
         this._pc.isGotobokBuff = false;
       }
     }
   }


   private void clanbuff() {
     L1Clan clan = L1World.getInstance().getClan(this._pc.getClanid());
     if (this._pc.getClanid() != 0 && (clan.getOnlineClanMember()).length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT && !this._pc.isClanBuff()) {
       this._pc.setSkillEffect(7789, 0L);

       this._pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 450, true));
       this._pc.setClanBuff(true);
     } else if (this._pc.getClanid() != 0 && (clan.getOnlineClanMember()).length < Config.ServerAdSetting.CLANBUFFUSERCOUNT && this._pc.isClanBuff()) {
       this._pc.killSkillEffectTimer(7789);
       this._pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 450, false));
       this._pc.setClanBuff(false);
     }
   }








   private static boolean isPlayerInLifeStream(L1PcInstance pc) {
     for (L1Object object : pc.getKnownObjects()) {
       if (!(object instanceof L1EffectInstance)) {
         continue;
       }
       L1EffectInstance effect = (L1EffectInstance)object;
       if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance((Point)pc.getLocation()) < 4) {
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



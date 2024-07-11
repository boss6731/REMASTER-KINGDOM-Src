 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
 import l1j.server.Config;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJTemplate.SpellProp.MJSpellProbabilityLoader;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.datatables.CharacterBalance;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.datatables.SpecialMapTable;
 import l1j.server.server.datatables.UserProtectMonsterTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;
 import l1j.server.server.model.Instance.L1TowerInstance;
 import l1j.server.server.model.item.function.L1MagicDoll;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_OnlyEffect;
 import l1j.server.server.serverpackets.S_SkillHaste;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Skills;
 import l1j.server.server.templates.L1SpecialMap;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.CalcStat;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.IntRange;
 import l1j.server.server.utils.MJCommons;






 public class L1Magic
 {
   private int _calcType;
   private final int PC_PC = 1;

   private final int PC_NPC = 2;

   private final int NPC_PC = 3;

   private final int NPC_NPC = 4;

   private L1PcInstance _pc = null;

   private L1PcInstance _targetPc = null;

   private L1NpcInstance _npc = null;

   private L1NpcInstance _targetNpc = null;

   private L1Character _target = null;

   private int _leverage = 10;

   private static Random _random = new Random(System.nanoTime());

   public boolean _CriticalDamage = false;

   public boolean isCriticalDamage() {
     return this._CriticalDamage;
   }

   public void setLeverage(int i) {
     this._leverage = i;
   }

   private int getLeverage() {
     return this._leverage;
   }

   private double _simsimLeverage = 1.0D;

   public void setSimSimLeverge(double i) {
     this._simsimLeverage = i;
   }

   private double getSimSimLeverage() {
     return this._simsimLeverage;
   }

   public L1Magic(L1Character attacker, L1Character target) {
     if (attacker instanceof L1PcInstance) {
       if (target instanceof L1PcInstance) {
         this._calcType = 1;
         this._pc = (L1PcInstance)attacker;
         this._targetPc = (L1PcInstance)target;
       } else {
         this._calcType = 2;
         this._pc = (L1PcInstance)attacker;
         this._targetNpc = (L1NpcInstance)target;
       }

     } else if (target instanceof L1PcInstance) {
       this._calcType = 3;
       this._npc = (L1NpcInstance)attacker;
       this._targetPc = (L1PcInstance)target;
     } else {
       this._calcType = 4;
       this._npc = (L1NpcInstance)attacker;
       this._targetNpc = (L1NpcInstance)target;
     }

     this._target = target;
   }

   private int getSpellPower() {
     int spellPower = 0;
     if (this._calcType == 1 || this._calcType == 2) {
       spellPower = this._pc.getAbility().getSp();
     } else if (this._calcType == 3 || this._calcType == 4) {
       spellPower = this._npc.getAbility().getSp();
     }
     return spellPower;
   }

   private int getMagicLevel() {
     int magicLevel = 0;
     if (this._calcType == 1 || this._calcType == 2) {
       magicLevel = this._pc.getAbility().getMagicLevel();
     } else if (this._calcType == 3 || this._calcType == 4) {
       magicLevel = this._npc.getAbility().getMagicLevel();
     }
     return magicLevel;
   }

   private int getMagicBonus() {
     int magicBonus = 0;
     if (this._calcType == 1 || this._calcType == 2) {
       magicBonus = this._pc.getAbility().getMagicBonus();
     } else if (this._calcType == 3 || this._calcType == 4) {
       magicBonus = this._npc.getAbility().getMagicBonus();
     }
     return magicBonus;
   }

   private int getLawful() {
     int lawful = 0;
     if (this._calcType == 1 || this._calcType == 2) {
       lawful = this._pc.getLawful();
     } else if (this._calcType == 3 || this._calcType == 4) {
       lawful = this._npc.getLawful();
     }
     return lawful;
   }

   private int getTargetMr() {
     int mr = 0;
     if (this._calcType == 1 || this._calcType == 3) {
       mr = this._targetPc.getResistance().getEffectedMrBySkill();
     }
     else if (this._targetNpc.getResistance() == null) {
       mr = 0;
     } else {
       mr = this._targetNpc.getResistance().getEffectedMrBySkill();
     }

     return mr;
   }



     public boolean calcProbabilityMagic(int skillId) {
         double probability = 0.0;
         boolean isSuccess = false;

         // 檢查當前計算類型是否為1或3並且目標玩家不為null
         if ((this._calcType == 1 || this._calcType == 3) && this._targetPc != null) {
             // 獲取指定技能ID的技能模板
             L1Skills _skill = SkillsTable.getInstance().getTemplate(skillId);

             // 檢查技能是否存在且不忽略反魔法
             // 並且目標玩家有魔法躲避概率且成功觸發魔法躲避
             if (_skill != null &&
                     !_skill.isIgnoresCounterMagic() &&
                     this._targetPc.getMagicDodgeProbability() > 0 &&
                     MJRnd.isWinning(100, this._targetPc.getMagicDodgeProbability())) {

                 // 創建和發送反魔法效果
                 S_OnlyEffect eff = new S_OnlyEffect(this._targetPc.getId(), 10702);
                 this._targetPc.sendPackets("(ME) 確率魔法躲避力觸發反魔法效果。");
                 this._targetPc.sendPackets((ServerBasePacket) eff, false);
                 this._targetPc.broadcastPacket((ServerBasePacket) eff);

                 // 返回false表示計算魔法概率失敗
                 return false;
             }
         }

         // 對其他計算類型進行處理
         if (this._calcType == 2 && this._targetNpc != null) {
             // 對NPC目標進行處理的邏輯
             probability = calculateProbabilityForNpc(skillId);
         } else if (this._calcType == 1 || this._calcType == 3) {
             // 對玩家目標進行處理的邏輯
             probability = calculateProbabilityForPlayer(skillId);
         }

         // 決定是否成功
         isSuccess = (MJRnd.getRandom(100) < probability);

         // 返回計算結果
         return isSuccess;
     }

     // 此方法負責計算對NPC的概率
     private double calculateProbabilityForNpc(int skillId) {
         // 根據具體邏輯計算對NPC的概率
         // ...
         return 0.0; // 這裡應該返回實際計算的概率
     }

     // 此方法負責計算對玩家的概率
     private double calculateProbabilityForPlayer(int skillId) {
         // 根據具體邏輯計算對玩家的概率
         // ...
         return 0.0; // 這裡應該返回實際計算的概率
     }




     if (this._calcType == 2 && this._targetNpc != null) {
       int npcId = this._targetNpc.getNpcTemplate().get_npcId();
       if (npcId == 8500138)
         return false;
       if (npcId >= 45912 && npcId <= 45915 && !this._pc.hasSkillEffect(1013)) {
         return false;
       }
       if (npcId == 45916 && !this._pc.hasSkillEffect(1014)) {
         return false;
       }
       if (npcId == 45941 && !this._pc.hasSkillEffect(1015)) {
         return false;
       }
       if (npcId >= 46068 && npcId <= 46091 && this._pc.getCurrentSpriteId() == 6035) {
         return false;
       }
       if (npcId >= 46092 && npcId <= 46106 && this._pc.getCurrentSpriteId() == 6034) {
         return false;
       }
       if (this._targetNpc.getNpcTemplate().get_gfxid() == 7684 && !this._pc.hasSkillEffect(22035)) {
         return false;
       }
       if (this._targetNpc.getNpcTemplate().get_gfxid() == 7805 && !this._pc.hasSkillEffect(22036)) {
         return false;
       }
       if (this._targetNpc.getNpcTemplate().get_gfxid() == 7720) {
         return false;
       }
     }

     if (this._calcType == 4 &&
       this._targetNpc.getNpcTemplate().get_npcId() == 8500138) {
       return false;
     }

     if (!checkZone(skillId)) {
       return false;
     }

if (skillId == 44) { // 檢查技能ID是否為44（假設44代表取消魔法）
     if (this._calcType == 1 && this._pc != null && this._targetPc != null) { // 檢查計算類型是否為1並且當前玩家和目標玩家都不為null

         if (this._pc.getId() == this._targetPc.getId()) { // 檢查當前玩家ID是否等於目標玩家ID
             return true; // 如果相等，返回true，表示成功
         }

         if (this._pc.getClanid() > 0 && this._pc.getClanid() == this._targetPc.getClanid()) { // 檢查當前玩家的公會ID是否大於0並且是否等於目標玩家的公會ID
             this._targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("血盟成員 " + this._pc.getName() + " 使用了取消魔法。")); // 向目標玩家發送系統訊息，通知公會成員使用了取消魔法
             return true; // 返回true，表示成功
         }

         if (this._pc.isInParty() && this._pc.getParty().isMember(this._targetPc)) { // 檢查當前玩家是否在隊伍中並且目標玩家是否在同一隊伍中
             this._targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("隊伍成員 " + this._pc.getName() + " 使用了取消魔法。")); // 向目標玩家發送系統訊息，通知隊伍成員使用了取消魔法
             return true; // 返回true，表示成功
         }
     }


         if (this._targetPc.isInvisble()) {
           return false;
         }

         if (this._pc.getZoneType() == 1 || this._targetPc.getZoneType() == 1) {
           return false;
         }
       }
       if (this._calcType == 2 || this._calcType == 3 || this._calcType == 4) {
         return true;
       }
     }


     if (this._calcType == 1) { // 檢查計算類型是否為1
         if (Config.ServerAdSetting.CASTLEWAR) { // 檢查伺服器配置是否允許城堡戰爭

             int castle_id = L1CastleLocation.getCastleIdByArea((L1Character) this._pc); // 根據當前玩家的位置獲取城堡ID
             if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) { // 檢查該城堡ID是否正在進行戰爭
                 MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id); // 獲取正在進行戰爭的城堡戰爭實例
             L1Clan defense = war.getDefenseClan(); // 獲取防守方公會
             boolean Range = false; // 初始化是否在距離範圍內的標誌

             if (this._pc.getClan() != defense) { // 如果當前玩家的公會不是防守方
                 for (L1Object l1object : L1World.getInstance().getObject()) { // 遍歷世界中的所有物件
                     if (l1object instanceof L1TowerInstance) { // 檢查物件是否為守護塔實例
                         L1TowerInstance tower = (L1TowerInstance) l1object; // 將物件轉換為守護塔實例
             if (L1CastleLocation.checkInWarArea(castle_id, tower.getLocation())) { // 檢查守護塔是否在戰爭區域內
                 Range = (this._pc.getLocation().getTileLineDistance(new Point(l1object.getX(), l1object.getY())) > 10); // 檢查玩家與守護塔的距離是否大於10
             if (Range && this._targetPc.getClan() != defense && this._targetPc.getClan() != this._pc.getClan() && this._targetPc.getRedKnightClanId() == 0) { // 檢查目標玩家不屬於防守方且不屬於當前玩家的公會且不是紅騎士公會成員
                 this._pc.sendPackets("\fY[通知]只有在守護塔附近才能進行PK。"); // 向當前玩家發送訊息，通知只有在守護塔附近才能進行PK//공성 혈맹끼리는 수호탑 주변에서만 PK가 가능 합니다
             this._targetPc.sendPackets("\fY[通知]只有在守護塔附近才能進行PK。"); // 向目標玩家發送訊息，通知只有在守護塔附近才能進行PK//공성 혈맹끼리는 수호탑 주변에서만 PK가 가능 합니다
             return false; // 返回false，表示不允許進行PK
             }
             }
                     }
                 }
             }
             }
         }


         if ((this._pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION ||
             (this._targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION && !this._pc.isPinkName())) &&
             skillId != 19 && skillId != 1 && skillId != 35 && skillId != 49 && skillId != 57 && skillId != 164) {


             // 檢查玩家或目標玩家是否受到保護，且技能ID不為19, 1, 35, 49, 57, 164
             boolean attack_ok = false; // 初始化攻擊標誌為假
             for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object) this._targetPc)) {
                 if (!(obj instanceof L1MonsterInstance)) { // 如果物件不為怪物實例，跳過
                     continue;
                 }

                 if (obj instanceof L1MonsterInstance) { // 如果物件為怪物實例
                     L1MonsterInstance mon = (L1MonsterInstance) obj; // 將物件轉換為怪物實例
             int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId()); // 獲取怪物ID
             if (monid != 0) { // 如果怪物ID不為0
                 attack_ok = true; // 設置攻擊標誌為真
             break; // 跳出循環
             }
                 }
             }

             if (!attack_ok) { // 如果攻擊標誌為假
                 this._pc.sendPackets((ServerBasePacket) new S_SystemMessage("新手玩家受到來自其他玩家的魔法保護。")); // 向當前玩家發送系統訊息，通知新手玩家受到保護
             this._targetPc.sendPackets((ServerBasePacket) new S_SystemMessage("新手玩家受到來自其他玩家的魔法保護。")); // 向目標玩家發送系統訊息，通知新手玩家受到保護
             return false; // 返回false，表示不允許攻擊
             }

         }

     if (this._calcType == 2 && this._targetNpc.getNpcId() == 5042 &&
       skillId == 36) {
       return false;
     }


     if (this._calcType == 1 || this._calcType == 3) {
       if (this._targetPc.hasSkillEffect(157) && ((
         skillId != 27 && skillId != 44 && skillId != 19 && skillId != 1 && skillId != 35 && skillId != 49 && skillId != 57 && skillId != 164 && skillId == 39) || skillId == 33 || skillId == 192 || skillId == 153 || skillId == 123 || skillId == 87 || skillId == 157 || skillId == 208 || skillId == 243 || skillId == 242))
       {


         return false;

       }
     }
     else if (this._targetNpc.hasSkillEffect(157) &&
       skillId != 27 && skillId != 44) {
       return false;
     }






     if (this._target.hasSkillEffect(STUN_TYPE_SKILL)) {
       for (int stun_skill : STUN_TYPE_SKILL) {
         if (stun_skill == skillId) {
           return false;
         }
       }
     }
     if ((this._calcType == 2 || this._calcType == 4) && (
       skillId == 64 || skillId == 161) && (this._targetNpc.getNpcId() == 45684 || this._targetNpc.getNpcId() == 45683 || this._targetNpc.getNpcId() == 45681 || this._targetNpc
       .getNpcId() == 45682 || this._targetNpc.getNpcId() == 900011 || this._targetNpc.getNpcId() == 900012 || this._targetNpc.getNpcId() == 900013 || this._targetNpc.getNpcId() == 900038 || this._targetNpc
       .getNpcId() == 900039 || this._targetNpc.getNpcId() == 900040 || this._targetNpc.getNpcId() == 5096 || this._targetNpc.getNpcId() == 5097 || this._targetNpc.getNpcId() == 5098 || this._targetNpc
       .getNpcId() == 5099 || this._targetNpc.getNpcId() == 5100)) {
       return false;
     }



     if (skillId == 207 || skillId == 219) {
       return true;
     }

     probability = calcProbability(skillId);
     int rnd = CommonUtil.random(100);


     if (skillId == 243) {
       if (this._calcType == 2) {

         if (this._pc.getLocation().getTileLineDistance(this._targetNpc.getLocation()) > 4) {
           probability /= 2.0D;
         }
       } else if (this._calcType == 1) {

         if (this._pc.getLocation().getTileLineDistance(this._targetPc.getLocation()) > 4) {
           probability /= 2.0D;
         }
       }
     }


     if (probability > 90.0D) {
       probability = 90.0D;
     }
     if (probability >= rnd) {
       isSuccess = true;
     }
     else if (this._calcType == 3 || this._calcType == 1) {
       isSuccess = false;
     } else if (this._calcType == 2) {
       this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 13418));
       isSuccess = false;
     }

     if (((!isSuccess ? 1 : 0) & ((skillId == 18) ? 1 : 0)) != 0 &&
       this._calcType == 2) {
       this._targetNpc.setHate((L1Character)this._pc, 1);
       int ran = _random.nextInt(100) + 1;
       if (ran <= 50) {
         Broadcaster.broadcastPacket((L1Character)this._targetNpc, (ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 8987));
         Broadcaster.broadcastPacket((L1Character)this._targetNpc, (ServerBasePacket)new S_SkillHaste(this._targetNpc.getId(), 1, 0));
         this._targetNpc.setMoveSpeed(1);
       }
     }




     if (Config.LogStatus.GMATKMSG) { // 檢查是否啟用了GM攻擊日志記錄

         if (this._calcType == 1 || this._calcType == 2) { // 檢查計算類型是否為1或2
             if (this._pc.isGm()) { // 檢查當前玩家是否為GM
                 String 攻擊者 = this._pc.getName(); // 設置攻擊者名稱
             String 防守者 = null; // 初始化防守者名稱
             int 確率 = (int) probability; // 設置成功概率
             int 隨機值 = rnd; // 設置隨機值
             String 成功或失敗 = isSuccess ? "成功" : "失敗"; // 設置成功或失敗標記

             if (this._calcType == 1) {
                 防守者 = this._targetPc.getName(); // 設置防守者名稱為目標玩家名稱
             } else if (this._calcType == 2) {
                 防守者 = this._targetNpc.getName(); // 設置防守者名稱為目標NPC名稱
             }

             // 向GM發送系統訊息，包含攻擊者、防守者、成功概率、隨機值和成功或失敗標記
             this._pc.sendPackets((ServerBasePacket) new S_SystemMessage(
                     "\fR[" + 攻擊者 + "->" + 防守者 + "] " + 確率 + "(" + 隨機值 + ") / " + 成功或失敗
             ));
             }

         } else if (this._calcType == 3 && this._targetPc.isGm()) { // 如果計算類型為3且目標玩家為GM
             String 攻擊者 = this._npc.getName(); // 設置攻擊者名稱為NPC名稱
             String 防守者 = this._targetPc.getName(); // 設置防守者名稱為目標玩家名稱
             L1Skills skill = SkillsTable.getInstance().getTemplate(skillId); // 獲取技能模板
             String 技能名稱 = "無此技能"; // 初始化技能名稱

             if (skill != null) {
                 技能名稱 = skill.getName(); // 如果技能存在，設置技能名稱
             }

             int 確率 = (int) probability; // 設置成功概率
             int 隨機值 = rnd; // 設置隨機值
             String 成功或失敗 = isSuccess ? "成功" : "失敗"; // 設置成功或失敗標記

             // 向目標GM玩家發送系統訊息，包含攻擊者、防守者、成功概率、隨機值、成功或失敗標記和技能名稱
             this._targetPc.sendPackets((ServerBasePacket) new S_SystemMessage(
                     "\fR[" + 攻擊者 + "->" + 防守者 + "] " + 確率 + "(" + 隨機值 + ") / " + 成功或失敗 + " [" + 技能名稱 + "]"
             ));

         }
     }



     return isSuccess;
   }

   private boolean checkZone(int skillId) {
     if (this._pc != null && this._targetPc != null && (
       this._pc.getZoneType() == 1 || this._targetPc.getZoneType() == 1 || this._pc.getMapId() == 13005 || this._targetPc.getMapId() == 13005) && (
       skillId == 11 || skillId == 20 || skillId == 27 || skillId == 29 || skillId == 33 || skillId == 39 || skillId == 40 || skillId == 47 || skillId == 70704 || skillId == 64 || skillId == 66 || skillId == 71 || skillId == 87 || skillId == 123 || skillId == 153 || skillId == 157 || skillId == 161 || skillId == 777777 || skillId == 173 || skillId == 174 || skillId == 183 || skillId == 70705 || skillId == 133 || skillId == 145 || skillId == 212 || skillId == 202 || skillId == 230 || skillId == 228 || skillId == 243 || skillId == 242 || skillId == 5027))
     {



       return false;
     }


     return true; } private int calcProbability(int skillId) { L1PcInstance l1PcInstance1; L1NpcInstance l1NpcInstance1; L1PcInstance l1PcInstance3;
     L1NpcInstance l1NpcInstance3;
     L1PcInstance l1PcInstance2;
     L1NpcInstance l1NpcInstance2;
     L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
     int attackLevel = 0;
     int defenseLevel = 0;
     int probability = 0;
     int attackInt = 0;
     int defenseMr = 0;
     L1Character attacker = null;
     L1Character receiver = null;
     switch (this._calcType) {
       case 1:
         l1PcInstance1 = this._pc;
         l1PcInstance3 = this._targetPc;
         break;
       case 2:
         l1PcInstance1 = this._pc;
         l1NpcInstance3 = this._targetNpc;
         break;
       case 3:
         l1NpcInstance1 = this._npc;
         l1PcInstance2 = this._targetPc;
         break;
       case 4:
         l1NpcInstance1 = this._npc;
         l1NpcInstance2 = this._targetNpc;
         break;
     }

     if (this._calcType == 1 || this._calcType == 2) {
       attackLevel = this._pc.getLevel();
       attackInt = this._pc.getAbility().getTotalInt();
     } else {
       attackLevel = this._npc.getLevel();
       attackInt = this._npc.getAbility().getTotalInt();
     }

     if (this._calcType == 1 || this._calcType == 3) {
       defenseLevel = this._targetPc.getLevel();
       defenseMr = this._targetPc.getResistance().getEffectedMrBySkill();
     }
     else if (this._targetNpc != null) {
       defenseLevel = this._targetNpc.getLevel();
       defenseMr = (this._targetNpc.getResistance() != null) ? this._targetNpc.getResistance().getEffectedMrBySkill() : 0;
       if (skillId == 145 &&
         this._targetNpc instanceof L1SummonInstance) {
         L1SummonInstance summon = (L1SummonInstance)this._targetNpc;
         defenseLevel = summon.getMaster().getLevel();
       }
     }




     if ((this._calcType == 1 || this._calcType == 2) && MJSpellProbabilityLoader.getInstance().contains_probability(skillId)) {
       probability = MJSpellProbabilityLoader.getInstance().calc_probability(skillId, this._pc, (L1Character)l1NpcInstance2, attackInt, defenseMr);
     } else {
       int ERASEMAGICPoint; int EARTHBINDPoint; int STRIKERGALEPoint; int POLLUTEWATERPoint; int WINDSHACKLEPoint; int ProbabilityValue; int THUNDERGRABPoint; int probability_veteran; int horroPoint; int CONFUSION_PHANTASMPoint; int boneBreakPoint; int shockStunPoint; double MR_DRAIN_WEIGHT; double INT_DRAIN_WEIGHT; double MR_TURN_WEIGHT; double INT_TURN_WEIGHT; int dragonSpellPoint;
       int dice1;
       int diceCount1;
       switch (skillId) {
         case 133:
         case 153:
           ERASEMAGICPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (ERASEMAGICPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(ERASEMAGICPoint / Config.MagicAdSetting_Elf.ERASEMAGIC));
           }

           if (attackLevel >= defenseLevel) {
             probability = (attackLevel - defenseLevel) * 2 + 43;
           } else if (attackLevel < defenseLevel) {
             probability = (attackLevel - defenseLevel) * 3 + 43;
           }
           if (probability > 70) {
             probability = 70;
           }
           break;

         case 157:
           EARTHBINDPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (EARTHBINDPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(EARTHBINDPoint / Config.MagicAdSetting_Elf.EARTHBINDT));
           }

           if (attackLevel >= defenseLevel) {

             probability = (attackLevel - defenseLevel) * 2 + 40;
           } else if (attackLevel < defenseLevel) {


             probability = (attackLevel - defenseLevel) * 3 + 30;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;

         case 174:
           STRIKERGALEPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (STRIKERGALEPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(STRIKERGALEPoint / Config.MagicAdSetting_Elf.STRIKERGALET));
           }

           if (attackLevel >= defenseLevel) {
             probability = (attackLevel - defenseLevel) * 2 + 40;
           } else if (attackLevel < defenseLevel) {
             probability = (attackLevel - defenseLevel) * 3 + 30;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;

         case 173:
           POLLUTEWATERPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (POLLUTEWATERPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(POLLUTEWATERPoint / Config.MagicAdSetting_Elf.POLLUTEWATERT));
           }

           if (attackLevel >= defenseLevel) {
             probability = (attackLevel - defenseLevel) * 2 + 40;
           } else if (attackLevel < defenseLevel) {
             probability = (attackLevel - defenseLevel) * 3 + 30;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;

         case 777777:
           WINDSHACKLEPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (WINDSHACKLEPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(WINDSHACKLEPoint / Config.MagicAdSetting_Elf.WINDSHACKLET));
           }

           if (attackLevel >= defenseLevel) {
             probability = (attackLevel - defenseLevel) * 2 + 40;
           } else if (attackLevel < defenseLevel) {
             probability = (attackLevel - defenseLevel) * 3 + 30;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;


         case 11:
         case 20:
         case 27:
         case 29:
         case 33:
         case 44:
         case 64:
         case 66:
         case 67:
         case 71:
         case 73:
         case 103:
         case 70704:
         case 707073:
           if (attackInt > 25)
             attackInt = 25;
           probability = attackInt * 3 + l1skills.getProbabilityValue() - defenseMr;
           if (this._pc != null && this._pc.isElf() && (this._calcType == 1 || this._calcType == 2)) {
             probability -= 30;
           }
           if (probability < 1) {
             probability = 1;
           }
           if (probability > 80) {
             probability = 80;
           }
           break;



         case 40:
         case 47:
         case 70705:
           if (attackInt > 10)
             attackInt = 10;
           probability = attackInt * 3 + l1skills.getProbabilityValue() - defenseMr;
           if (this._pc != null && this._pc.isElf() && (this._calcType == 1 || this._calcType == 2)) {
             probability -= 10;
           }
           if (probability < 5) {
             probability = 5;
           }
           if (probability > 70) {
             probability = 70;
           }

           if (attackLevel < defenseLevel) {
             probability = 0;
           }
           break;





         case 192:
           ProbabilityValue = SkillsTable.getInstance().getTemplate(192).getProbabilityValue();
           THUNDERGRABPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (THUNDERGRABPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(THUNDERGRABPoint / Config.MagicAdSetting_DragonKnight.THUNDERGRABT));
           }
           probability = ProbabilityValue;
           if (this._calcType == 1 || this._calcType == 2) {
             probability += 2;
           }
           break;
         case 91:
           probability = l1skills.getProbabilityValue();
           probability_veteran = Config.MagicAdSetting_Knight.COUNTER_VETERAN;



           if (this._pc != null && this._pc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
             int lvl = this._pc.getLevel();
             if (lvl >= 85) {
               probability_veteran += lvl - 84;
             }
             if (probability_veteran >= 10) {
               probability_veteran = 10;
             }
             probability += probability_veteran;
           }
           break;

         case 230:
           horroPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (horroPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(horroPoint / Config.MagicAdSetting_DragonKnight.HORRORHITTOLEVEL));
           }

           probability = (int)Config.MagicAdSetting_Warrior.DESPERADO + (attackLevel - defenseLevel) * 9;
           probability += this._pc.getImpactUp();
           if (probability < 15) {
             probability = 15;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;


         case 394:
           probability = l1skills.getProbabilityValue();
           break;

         case 202:
         case 212:
           probability = l1skills.getProbabilityValue();
           CONFUSION_PHANTASMPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (CONFUSION_PHANTASMPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(CONFUSION_PHANTASMPoint / Config.MagicAdSetting_Illusion.CONFUSIONPHANTASMT));
           }
           break;
         case 145:
           probability = (int)(l1skills.getProbabilityDice() / 10.0D * (attackLevel - defenseLevel)) + l1skills.getProbabilityValue();
           break;
         case 208:
           boneBreakPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (boneBreakPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(boneBreakPoint / Config.MagicAdSetting_Knight.STUNHITTOLEVEL));
           }
           if (this._calcType == 1 &&
             this._pc instanceof L1PcInstance) {
             L1ItemInstance weapon = this._pc.getWeapon();
             if (weapon == null) {
               probability = 0;
               break;
             }
             int itemId = weapon.getItem().getItemId();
             if (itemId == 202012) {
               attackLevel += 5;
             }
           }

           probability = (int)Config.MagicAdSetting_Illusion.BONEBREAKPRO + attackLevel - defenseLevel;
           if (probability < 10) {
             probability = 10;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;
         case 123:
           shockStunPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (shockStunPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(shockStunPoint / Config.MagicAdSetting_Prince.EMPIREHITTOLEVEL));
           }


           probability = (int)Config.MagicAdSetting_Prince.EMPIRE + (attackLevel - defenseLevel) * 2;
           if (this._calcType == 1 || this._calcType == 2) {
             probability += this._pc.getImpactUp();
           }
           if (probability < 15) {
             probability = 15;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;

         case 22055:
         case 707041:
         case 707054:
         case 707056:
         case 707099:
         case 707113:
         case 707119:
         case 707152:
         case 707159:
           probability = SkillsTable.getInstance().getTemplate(skillId).getProbabilityValue();
           if (probability > 100) {
             probability = 100;
           }
           break;



         case 87:
         case 242:
           shockStunPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (shockStunPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(shockStunPoint / Config.MagicAdSetting_Knight.STUNHITTOLEVEL));
           }


           probability = (int)Config.MagicAdSetting_Knight.SHOCKSTUN + (attackLevel - defenseLevel) * 2;
           if (this._calcType == 1 || this._calcType == 2) {
             probability += this._pc.getImpactUp();
           }
           if (probability < 15) {
             probability = 15;
           }
           if (probability > 100) {
             probability = 100;
           }
           break;





         case 39:
           MR_DRAIN_WEIGHT = 0.5D;
           INT_DRAIN_WEIGHT = 1.3D;



           probability = (int)(attackInt * 1.3D - defenseMr * 0.5D);
           probability = Math.max(probability, 10);
           break;




         case 18:
           MR_TURN_WEIGHT = 0.5D;
           INT_TURN_WEIGHT = 1.3D;



           probability = (int)(attackInt * 1.3D - defenseMr * 0.5D);








           if ((this._calcType == 1 || this._calcType == 2) &&
             !this._pc.isElf()) {
             probability -= Config.MagicAdSetting_Elf.ELFTURNCHANT;
           }

           probability = Math.max(probability, 10);
           break;

         case 112:
           dragonSpellPoint = l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT) + l1NpcInstance1.getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL);
           if (dragonSpellPoint > 0) {
             attackLevel = (int)(attackLevel + Math.round(dragonSpellPoint / Config.MagicAdSetting_DragonKnight.DESTROYHITTOLEVEL));
           }


           probability = (int)(Config.MagicAdSetting_DarkElf.ARMORBRAKET + ((attackLevel - defenseLevel) * 3));
           probability += this._pc.getImpactUp();
           if (this._pc.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
             int lvl = this._pc.getLevel();
             if (lvl >= 85) {
               probability += (lvl - 84) * 3;
             }
           }
           probability = IntRange.ensure(probability, 10, 80);
           break;

         default:
           dice1 = l1skills.getProbabilityDice();
           diceCount1 = 0;
           if (this._calcType == 1 || this._calcType == 2) {
             if (this._pc.isWizard()) {
               diceCount1 = getMagicBonus() + getMagicLevel() + 1;
             } else if (this._pc.isElf()) {
               diceCount1 = getMagicBonus() + getMagicLevel() - 1;
             } else if (this._pc.isDragonknight()) {
               diceCount1 = getMagicBonus() + getMagicLevel();
             } else {
               diceCount1 = getMagicBonus() + getMagicLevel() - 1;
             }
           } else {
             diceCount1 = getMagicBonus() + getMagicLevel();
           }
           if (diceCount1 < 1) {
             diceCount1 = 1;
           }
           if (dice1 > 0) {
             for (int i = 0; i < diceCount1; i++) {
               probability += _random.nextInt(dice1) + 1;
             }
           }

           probability = probability * getLeverage() / 10;
           probability -= getTargetMr();

           if (skillId == 36) {
             double probabilityRevision = 1.0D;
             if (this._targetNpc.getMaxHp() * 1 / 4 > this._targetNpc.getCurrentHp()) {
               probabilityRevision = 1.3D;
             } else if (this._targetNpc.getMaxHp() * 2 / 4 > this._targetNpc.getCurrentHp()) {
               probabilityRevision = 1.2D;
             } else if (this._targetNpc.getMaxHp() * 3 / 4 > this._targetNpc.getCurrentHp()) {
               probabilityRevision = 1.1D;
             }
             probability = (int)(probability * probabilityRevision);
           }
           break;
       }

     }
     if (l1skills.getPlusProbility() != 0) {
       probability += l1skills.getPlusProbility();
     }

     if (this._calcType == 1) {
       probability += CharacterBalance.getInstance().getMagicHit(this._pc.getType(), this._targetPc.getType());
     } else if (this._calcType == 2) {
       probability += CharacterBalance.getInstance().getMagicHit(this._pc.getType(), 10);
     } else if (this._calcType == 3) {
       probability += CharacterBalance.getInstance().getMagicHit(10, this._targetPc.getType());
     } else if (this._calcType == 4) {
       probability += CharacterBalance.getInstance().getMagicHit(10, 10);
     }





     if (this._pc != null) {
       probability += this._pc.get_private_probability(skillId);
     }
     return calc_character_magic_hit(skillId, probability); }









   private int calc_character_magic_hit(int skill_id, int probability) {
     if (this._calcType == 1) {
       if (this._pc.getMapId() == 13005 || this._targetPc.getMapId() == 13005) {
         return 0;
       }
       if (skill_id != 5027 ||
         this._pc == this._targetPc);
     }




     if (this._calcType != 1 && this._calcType != 2) {
       return probability;
     }

     double result = probability;
     if (!MJSpellProbabilityLoader.getInstance().contains_probability(skill_id)) {
       double p = Config.MagicAdSetting.CHARACTERMAGICHITRATE * this._pc.getTotalMagicHitup();
       result += p;
     }








     return (int)result;
   }

   public int calcMagicDamage(int skillId) {
     int damage = 0;
     int DMG = SkillsTable.getInstance().getTemplate(244).getDamageValue();
     if (this._calcType == 1 && (
       this._pc.getMapId() == 13005 || this._targetPc.getMapId() == 13005)) {
       return 0;
     }

     if (skillId == 244) {
       boolean isavenger = calcProbabilityMagic(skillId);
       if (this._calcType == 1) {
         if (isavenger) {
           int currentHp = (int)Math.round(this._targetPc.getMaxHp() * 0.3D);
           if (this._targetPc.getCurrentHp() < currentHp) {
             damage = this._targetPc.getCurrentHp();
           } else {
             damage = DMG + this._targetPc.getCurrentHp() / 20;
           }
           S_SkillSound s_SkillSound = new S_SkillSound(this._targetPc.getId(), 18404);
           this._targetPc.sendPackets((ServerBasePacket)s_SkillSound, false);
           Broadcaster.broadcastPacket((L1Character)this._targetPc, (ServerBasePacket)s_SkillSound, false);
           s_SkillSound.clear();
         }
         S_SkillSound effect = new S_SkillSound(this._targetPc.getId(), 18402);
         this._targetPc.sendPackets((ServerBasePacket)effect, false);
         Broadcaster.broadcastPacket((L1Character)this._targetPc, (ServerBasePacket)effect, false);
         effect.clear();
       } else {

         damage += calcNpcMagicDamage(skillId) + this._targetNpc.getCurrentHp() / 20;
         S_SkillSound effect = new S_SkillSound(this._targetNpc.getId(), 18402);
         this._targetNpc.sendPackets((ServerBasePacket)effect, false);
         Broadcaster.broadcastPacket((L1Character)this._targetNpc, (ServerBasePacket)effect, false);
         effect.clear();

       }


     }
     else {


       if (this._calcType == 1)
         if (this._pc.is_assassination_level2()) {
           damage += 2;
         } else if (this._pc.is_assassination_level1()) {
           damage++;
         }
       if (this._calcType == 1 || this._calcType == 3) {
         damage = calcPcMagicDamage(skillId);
         if (skillId == 28 || skillId == 10) {
           damage = Math.max(2, damage);
         }
       }
       else if (this._calcType == 2 || this._calcType == 4) {
         damage = calcNpcMagicDamage(skillId);
       }

       if (skillId == 5028 &&
         this._pc.isPassive(MJPassiveID.TYRANT_EXCUTION.toInt())) {
         damage = (int)(damage * Config.MagicAdSetting_Prince.TYRANT_EXCUTE_DMG_RATE / 100.0D);
       }
     }



     L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
     if (skill != null) {
       if (skill.is_magic_dmg_mr_impact()) {
         damage = calcMrDefense(damage);
       }
     } else {
       damage = calcMrDefense(damage);
     }

     if (this._calcType == 1) {
       damage += CharacterBalance.getInstance().getMagicDmg(this._pc.getType(), this._targetPc.getType());
       damage = (int)(damage * CharacterBalance.getInstance().getMagicDmgRate(this._pc.getType(), this._targetPc.getType()));
     } else if (this._calcType == 2) {
       damage += CharacterBalance.getInstance().getMagicDmg(this._pc.getType(), 10);
       damage = (int)(damage * CharacterBalance.getInstance().getMagicDmgRate(this._pc.getType(), 10));
     } else if (this._calcType == 3) {
       damage += CharacterBalance.getInstance().getMagicDmg(10, this._targetPc.getType());
       damage = (int)(damage * CharacterBalance.getInstance().getMagicDmgRate(10, this._targetPc.getType()));
     } else if (this._calcType == 4) {
       damage += CharacterBalance.getInstance().getMagicDmg(10, 10);
       damage = (int)(damage * CharacterBalance.getInstance().getMagicDmgRate(10, 10));
     }

     if (this._calcType == 1 || this._calcType == 3) {
       if (skillId == 4 || skillId == 34 || skillId == 77) {
         for (L1ItemInstance armor : this._targetPc.getEquipSlot().getArmors()) {

           if (armor.getItemId() == 20230) {
             int probability = 1;

             if (armor.getEnchantLevel() >= 10) {
               probability = 5;
             } else if (armor.getEnchantLevel() < 6) {
               probability = 1;
             } else {
               probability = armor.getEnchantLevel() - 5;
             }

             if (_random.nextInt(100) < probability) {
               damage = (int)(damage * 0.8D);
             }

             break;
           }
         }
       }

       if (this._pc != null && this._pc.isPassive(MJPassiveID.SOLID_NOTE.toInt()) &&
         this._target.hasSkillEffect(STUN_TYPE_SKILL) &&
         MJRnd.isWinning(100, Config.MagicAdSetting_DragonKnight.RAMPAGE_P)) {
         damage = (int)(damage * Config.MagicAdSetting_DragonKnight.RAMPAGE_D);
       }




       if (this._targetPc.isPassive(MJPassiveID.TITAN_MAGIC.toInt())) {
         if (this._calcType == 1 && this._pc.isWizard()) {
           if (this._pc.hasSkillEffect(3087)) {
             this._pc.removeSkillEffect(3087);
             this._pc.set_magic_add_count(2);
             this._pc.setSkillEffect(3088, 5000L);
             L1SkillUse.on_icons(this._pc, 3088, 5);
           } else if (this._pc.hasSkillEffect(3088)) {
             this._pc.removeSkillEffect(3088);
             this._pc.set_magic_add_count(3);
             this._pc.setSkillEffect(3089, 5000L);
             L1SkillUse.on_icons(this._pc, 3089, 5);
           } else if (this._pc.hasSkillEffect(3089)) {
             this._pc.removeSkillEffect(3089);
             this._pc.set_magic_add_count(4);
             this._pc.setSkillEffect(3090, 5000L);
             L1SkillUse.on_icons(this._pc, 3090, 5);
           } else if (this._pc.hasSkillEffect(3090)) {
             this._pc.removeSkillEffect(3090);
             this._pc.set_magic_add_count(5);
             this._pc.setSkillEffect(3091, 5000L);
             L1SkillUse.on_icons(this._pc, 3091, 5);
           } else if (this._pc.hasSkillEffect(3091)) {
             this._pc.removeSkillEffect(3091);
             this._pc.set_magic_add_count(5);
             this._pc.setSkillEffect(3091, 5000L);
             L1SkillUse.on_icons(this._pc, 3091, 5);
           } else {
             this._pc.set_magic_add_count(1);
             this._pc.setSkillEffect(3087, 5000L);
             L1SkillUse.on_icons(this._pc, 3087, 5);
           }
           damage = (int)(damage + damage * this._pc.get_magic_add_count() * Config.MagicAdSetting_Wizard.MAGIC_CONTINUE_DMG_PER);
         }
         if (!this._targetPc.hasSkillEffect(87) && !this._targetPc.hasSkillEffect(123) && !this._targetPc.hasSkillEffect(208) &&
           !this._targetPc.hasSkillEffect(5056) &&
           !this._targetPc.hasSkillEffect(5003) &&
           !this._targetPc.hasSkillEffect(77)) {


           int percent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
           int chance = _random.nextInt(100) + 1;
           int titan_per = Config.MagicAdSetting_Warrior.TITANMAGICPRO;
           int titan_rising_per = 0;
           int targetlevel = this._targetPc.getLevel();

           if (targetlevel < 95) {
             titan_per -= (95 - targetlevel) * 2;
             if (titan_per <= 15) {
               titan_per = 15;
             }
           }
           if (!MJCommons.isUnbeatable((L1Character) this._targetPc)) { // 檢查目標玩家是否無敵
               if (percent < 50) { // 檢查百分比是否小於50
                   titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO; // 計算泰坦上升概率
         if (chance <= titan_rising_per) { // 檢查機率是否小於等於泰坦上升概率
             if (this._targetPc.getInventory().checkItem(41246, 5)) { // 檢查目標玩家的背包中是否有5個編號41246的物品
                 if (this._calcType == 1) { // 如果計算類型為1
                     this._pc.receiveCounterBarrierDamage((L1Character) this._targetPc, 泰坦傷害()); // 當前玩家接收反射傷害
                 } else if (this._calcType == 2) { // 如果計算類型為2
                     this._npc.receiveCounterBarrierDamage((L1Character) this._targetPc, 泰坦傷害()); // 當前NPC接收反射傷害
                 }
                 damage = 0; // 設置傷害為0
         this._targetPc.sendPackets((ServerBasePacket) new S_SkillSound(this._targetPc.getId(), 12559)); // 向目標玩家發送技能音效
         this._targetPc.getInventory().consumeItem(41246, 5); // 消耗5個編號41246的物品
         this._targetPc.send_effect(12559); // 向目標玩家發送特效
             } else {
                 this._targetPc.sendPackets((ServerBasePacket) new S_SystemMessage("泰坦魔法：催化劑不足。")); // 向目標玩家發送系統訊息，通知催化劑不足
             }
         }
               }
           }
               }
             } else if (chance < titan_per) {
               if (this._calcType == 1) {
                 this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 泰坦傷害());
               } else if (this._calcType == 2) {
                 this._npc.receiveCounterBarrierDamage((L1Character)this._targetPc, 泰坦傷害());
               }
               damage = 0;
               this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12559));
               this._targetPc.send_effect(12559);
               this._targetPc.getInventory().consumeItem(41246, 5);
             }
           }
         } else {

           int percent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
           int chance = _random.nextInt(100) + 1;
           int titan_per = Config.MagicAdSetting_Warrior.TITANMAGICPRO;
           int titan_rising_per = 0;
           int targetlevel = this._targetPc.getLevel();

           if (targetlevel < 95) {
             titan_per -= (95 - targetlevel) * 2;
             if (titan_per <= 15) {
               titan_per = 15;
             }
           }
         if (this._targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) != null &&
         !MJCommons.isUnbeatable((L1Character) this._targetPc)) { // 檢查目標玩家是否擁有拆除被動技能且不處於無敵狀態
         if (percent < 50) { // 檢查百分比是否小於50
         if (chance <= titan_rising_per) { // 檢查隨機機率是否小於等於泰坦上升概率
         if (this._targetPc.getInventory().checkItem(41246, 5)) { // 檢查目標玩家的背包中是否有5個編號41246的物品
         if (this._calcType == 1) { // 如果計算類型為1
         this._pc.receiveCounterBarrierDamage((L1Character) this._targetPc, 泰坦傷害()); // 當前玩家接收反射傷害
         } else if (this._calcType == 2) { // 如果計算類型為2
         this._npc.receiveCounterBarrierDamage((L1Character) this._targetPc, 泰坦傷害()); // 當前NPC接收反射傷害
         }
         damage = 0; // 設置傷害為0
         this._targetPc.sendPackets((ServerBasePacket) new S_SkillSound(this._targetPc.getId(), 12559)); // 向目標玩家發送技能音效
         this._targetPc.getInventory().consumeItem(41246, 5); // 消耗5個編號41246的物品
         this._targetPc.send_effect(12559); // 向目標玩家發送特效
         } else {
         this._targetPc.sendPackets((ServerBasePacket) new S_SystemMessage("泰坦魔法：催化劑不足。")); // 向目標玩家發送系統訊息，通知催化劑不足
         }
         }
         }
         }
               }
             } else if (chance < titan_per) {
               if (this._calcType == 1) {
                 this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 泰坦傷害());
               } else if (this._calcType == 2) {
                 this._npc.receiveCounterBarrierDamage((L1Character)this._targetPc, 泰坦傷害());
               }
               damage = 0;
               this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12559));
               this._targetPc.send_effect(12559);
               this._targetPc.getInventory().consumeItem(41246, 5);
             }
           }
         }










         return damage;
       }
     }





     if ((this._calcType == 1 || this._calcType == 2) && this._pc.isWizard()) {
       if (this._pc.hasSkillEffect(3087)) {
         this._pc.removeSkillEffect(3087);
         this._pc.set_magic_add_count(2);
         this._pc.setSkillEffect(3088, 5000L);
         L1SkillUse.on_icons(this._pc, 3088, 5);
       } else if (this._pc.hasSkillEffect(3088)) {
         this._pc.removeSkillEffect(3088);
         this._pc.set_magic_add_count(3);
         this._pc.setSkillEffect(3089, 5000L);
         L1SkillUse.on_icons(this._pc, 3089, 5);
       } else if (this._pc.hasSkillEffect(3089)) {
         this._pc.removeSkillEffect(3089);
         this._pc.set_magic_add_count(4);
         this._pc.setSkillEffect(3090, 5000L);
         L1SkillUse.on_icons(this._pc, 3090, 5);
       } else if (this._pc.hasSkillEffect(3090)) {
         this._pc.removeSkillEffect(3090);
         this._pc.set_magic_add_count(5);
         this._pc.setSkillEffect(3091, 5000L);
         L1SkillUse.on_icons(this._pc, 3091, 5);
       } else if (this._pc.hasSkillEffect(3091)) {
         this._pc.removeSkillEffect(3091);
         this._pc.set_magic_add_count(5);
         this._pc.setSkillEffect(3091, 5000L);
         L1SkillUse.on_icons(this._pc, 3091, 5);
       } else {
         this._pc.set_magic_add_count(1);
         this._pc.setSkillEffect(3087, 5000L);
         L1SkillUse.on_icons(this._pc, 3087, 5);
       }
       damage = (int)(damage + damage * this._pc.get_magic_add_count() * Config.MagicAdSetting_Wizard.MAGIC_CONTINUE_DMG_PER);
     }
     return damage;
   }

   public int calcPcFireWallDamage() {
     int dmg = 0;

     L1Skills l1skills = SkillsTable.getInstance().getTemplate(58);
     dmg = calcAttrDefence(l1skills.getDamageValue(), 2);

     if (this._targetPc.hasSkillEffect(78)) {
       dmg = 0;
     }
     if (this._targetPc.hasSkillEffect(70705)) {
       dmg = 0;
     }
     if (this._targetPc.hasSkillEffect(157)) {
       dmg = 0;
     }
     if (this._targetPc.hasSkillEffect(30004)) {
       dmg = 0;
     }
     if (this._targetPc.hasSkillEffect(30003)) {
       dmg = 0;
     }
     if (dmg < 0) {
       dmg = 0;
     }

     return dmg;
   }

   public int calcNpcFireWallDamage() {
     int dmg = 0;

     L1Skills l1skills = SkillsTable.getInstance().getTemplate(58);
     dmg = calcAttrDefence(l1skills.getDamageValue(), 2);

     if (this._targetNpc.hasSkillEffect(70705)) {
       dmg = 0;
     }
     if (this._targetNpc.hasSkillEffect(157)) {
       dmg = 0;
     }
     if (this._targetNpc.hasSkillEffect(30004)) {
       dmg = 0;
     }
     if (this._targetNpc.hasSkillEffect(30003)) {
       dmg = 0;
     }
     if (dmg < 0) {
       dmg = 0;
     }

     return dmg;
   }




   private int calcPcMagicDamage(int skillId) {
     int dmg = 0;

     dmg = calcMagicDiceDamage(skillId);
     dmg = dmg * getLeverage() / 10;

     if (this._targetPc.hasSkillEffect(3050) || this._targetPc.hasSkillEffect(3051) || this._targetPc.hasSkillEffect(3052) || this._targetPc.hasSkillEffect(3053) || this._targetPc
       .hasSkillEffect(3054) || this._targetPc.hasSkillEffect(3055) || this._targetPc.hasSkillEffect(3056) || this._targetPc.hasSkillEffect(3058) || this._targetPc
       .hasSkillEffect(3059) || this._targetPc.hasSkillEffect(3060) || this._targetPc.hasSkillEffect(3061) || this._targetPc.hasSkillEffect(3062) || this._targetPc
       .hasSkillEffect(3063) || this._targetPc.hasSkillEffect(3064) || this._targetPc.hasSkillEffect(3066) || this._targetPc.hasSkillEffect(3067) || this._targetPc
       .hasSkillEffect(3068) || this._targetPc.hasSkillEffect(3069) || this._targetPc.hasSkillEffect(3070) || this._targetPc.hasSkillEffect(3071) || this._targetPc
       .hasSkillEffect(3072)) {
       dmg -= 4;
     }
     if (this._targetPc.hasSkillEffect(3057) || this._targetPc.hasSkillEffect(3065) || this._targetPc.hasSkillEffect(3070)) {
       dmg -= 4;
     }

     if (this._targetPc.isPassive(MJPassiveID.MAJESTY.toInt())) {
       int targetPcLvl = this._targetPc.getLevel();
       int reduction = 0;
       if (targetPcLvl < 80) {
         targetPcLvl = 80;
       } else if (targetPcLvl >= 80) {
         reduction = (targetPcLvl - 80) / 2 + 2;
       }
       if (reduction >= 10) {
         reduction = 10;
       }

       dmg -= reduction;
     }
     if (this._targetPc.hasSkillEffect(211)) {
       int targetPcLvl = this._targetPc.getLevel();
       if (targetPcLvl < 80)
         targetPcLvl = 80;
       dmg -= (targetPcLvl - 80) / 4 + 1;
     }
     if (this._targetPc.hasSkillEffect(159)) {
       int targetPcLvl = this._targetPc.getLevel();
       if (targetPcLvl < 80) {
         targetPcLvl = 80;
       }
       dmg -= (targetPcLvl - 80) / 4 + 1;
     }
     if (this._calcType == 3) {
       boolean isNowWar = false;
       int castleId = L1CastleLocation.getCastleIdByArea((L1Character)this._targetPc);
       if (castleId > 0) {
         isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
       }
       if (!isNowWar) {
         if (this._npc instanceof l1j.server.server.model.Instance.L1PetInstance) {
           dmg /= 8;
         }
         if (this._npc instanceof L1SummonInstance) {
           L1SummonInstance summon = (L1SummonInstance)this._npc;
           if (summon.isExsistMaster()) {
             dmg /= 8;
           }
         }
       }
     }


     if (this._targetPc.isPassive(MJPassiveID.DRAGON_SKIN_PASS.toInt())) {
       dmg -= 5;
     }

     if (this._calcType == 1 &&
       this._targetPc.get_pvp_defense_per() > 0) {
       dmg = (int)(dmg - dmg * 0.01D * this._targetPc.get_pvp_defense_per());
     }


     if (this._targetPc.get_Magic_defense_per() > 0) {
       dmg = (int)(dmg - dmg * 0.01D * this._targetPc.get_Magic_defense_per());
     }






     if (this._targetPc.hasSkillEffect(22000)) {
       dmg -= 3;
     }
     if (this._targetPc.hasSkillEffect(22001)) {
       dmg -= 2;
     }





     if (this._calcType == 1) {
       if (Config.ServerAdSetting.CASTLEWAR) {

         int i = L1CastleLocation.getCastleIdByArea((L1Character)this._pc);
         if (MJCastleWarBusiness.getInstance().isNowWar(i)) {
           MJCastleWar war = MJCastleWarBusiness.getInstance().get(i);
           L1Clan defense = war.getDefenseClan();
           boolean Range = false;

           if (this._pc.getClan() != defense) { // 檢查當前玩家的公會是否不是防守公會
               for (L1Object l1object : L1World.getInstance().getObject()) { // 遍歷世界中的所有物件
                   if (l1object instanceof L1TowerInstance) { // 檢查物件是否為守護塔實例
                       L1TowerInstance tower = (L1TowerInstance) l1object; // 將物件轉換為守護塔實例
         if (L1CastleLocation.checkInWarArea(i, tower.getLocation())) { // 檢查守護塔是否在戰爭區域內
             Range = (this._pc.getLocation().getTileLineDistance(new Point(l1object.getX(), l1object.getY())) > 10); // 檢查玩家與守護塔的距離是否大於10
         if (Range && this._targetPc.getClan() != defense && this._targetPc.getClan() != this._pc.getClan() && this._targetPc.getRedKnightClanId() == 0) { // 檢查目標玩家不屬於防守公會且不屬於當前玩家的公會且不是紅騎士公會成員
             this._pc.sendPackets("\fY公會戰爭中，只有在守護塔附近才能進行PK。"); // 向當前玩家發送系統訊息，通知只有在守護塔附近才能進行PK
         this._targetPc.sendPackets("\fY公會戰爭中，只有在守護塔附近才能進行PK。"); // 向目標玩家發送系統訊息，通知只有在守護塔附近才能進行PK
         return 0; // 返回0，表示不允許進行PK
         }
         }
                   }
               }
           }
         }
       int castle_id = L1CastleLocation.getCastleIdByArea((L1Character)this._pc);
       if (castle_id == 0) {
         if (Config.ServerAdSetting.CLANSETTINGPROTECTION) {
           boolean attack_ok = false;
           for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)this._targetPc)) {
             if (!(obj instanceof L1MonsterInstance)) {
               continue;
             }

             if (obj instanceof L1MonsterInstance) {
               L1MonsterInstance mon = (L1MonsterInstance)obj;
               int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId());
               if (monid != 0) {
                 attack_ok = true;

                 break;
               }
             }
           }
           if (!attack_ok && (
                   this._pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION || this._targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION) &&
         skillId != 19 && skillId != 1 && skillId != 35 && skillId != 49 && skillId != 57 && skillId != 164) {
               // 如果攻擊不被允許，並且當前玩家或目標玩家處於保護公會，且技能ID不是19, 1, 35, 49, 57, 164

         this._pc.sendPackets((ServerBasePacket) new S_SystemMessage("新手玩家之間無法互相攻擊。")); // 向當前玩家發送系統訊息
         this._targetPc.sendPackets((ServerBasePacket) new S_SystemMessage("新手玩家之間無法互相攻擊。")); // 向目標玩家發送系統訊息
         return 0; // 返回0，表示不允許攻擊
           }

         } else if ((this._pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION || this._targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION) &&
         skillId != 19 && skillId != 1 && skillId != 35 && skillId != 49 && skillId != 57 && skillId != 164) {
             // 如果當前玩家或目標玩家處於保護公會，且技能ID不是19, 1, 35, 49, 57, 164

         dmg /= 2; // 將傷害減半
         this._pc.sendPackets((ServerBasePacket) new S_SystemMessage("新手玩家只受到50%的傷害。")); // 向當前玩家發送系統訊息
         this._targetPc.sendPackets((ServerBasePacket) new S_SystemMessage("新手玩家只受到50%的傷害。")); // 向目標玩家發送系統訊息
         }
        }
        }

        if (this._calcType == 1 && (
                this._pc.getMapId() == 254 || this._pc.getMapId() == 612 || this._pc.getMapId() == 1930)) {

            // 如果計算類型為1，且當前玩家位於特定地圖編號254, 612, 1930

         this._pc.sendPackets("\fY在育成狩獵區域內無法進行PK。"); // 向當前玩家發送系統訊息
         this._targetPc.sendPackets("\fY在育成狩獵區域內無法進行PK。"); // 向目標玩家發送系統訊息
         dmg = 0; // 設置傷害為0
         }


     dmg -= L1MagicDoll.getDamageReductionByDoll((L1Character)this._npc, (L1Character)this._targetPc);

     double total_reduction = 0.0D;


     total_reduction += this._targetPc.getDamageReductionByArmor();
     total_reduction += this._targetPc.getDamageReduction();

     double total_pvpreduction = 0.0D;
     total_pvpreduction += this._targetPc.get_pvp_mdmg();

     double total_pvpreductionignore = 0.0D;
     total_pvpreductionignore -= this._targetPc.get_pvp_dmg_ignore();
     dmg = (int)Math.max(dmg - total_reduction - total_pvpreduction + total_pvpreductionignore, 0.0D);

     if (dmg < 0) {
       dmg = 0;
     }











     if (this._targetPc.get_reduction_per() > 0) {
       int reduction_per = this._target.get_reduction_per();
       dmg -= dmg * reduction_per / 100;
     }
     if (this._targetPc.get_Magic_defense_per() > 0) {
       dmg = (int)(dmg * (100 - this._targetPc.get_Magic_defense_per()) / 100.0D);
     }

     if (dmg > 0 && this._targetPc.hasSkillEffect(68) &&
       this._pc != null) {
       if (!this._pc.isWizard()) {
         dmg = (int)(dmg - dmg * this._targetPc.getImmuneReduction() * this._pc.get_immune_ignore() / 100.0D);



       }
       else {



         dmg = (int)(dmg * 0.5D);
       }
     }


     if (dmg > 0 && this._targetPc.hasSkillEffect(5047)) {
       dmg = (int)(dmg * 0.95D);
       L1Party party = this._targetPc.getParty();

       int reduction = 0;
       double dmgorigin = dmg;
       double dmggap = 0.0D;

       ArrayList<L1PcInstance> partymember = new ArrayList<>();
       if (party != null) {
         for (L1PcInstance player : L1World.getInstance().getVisiblePlayer((L1Object)this._targetPc, 8)) {
           if (this._targetPc.getClan() != player.getClan()) {
             continue;
           }
           if (!party.getList().contains(this._targetPc)) {
             continue;
           }
           if (player.getCurrentHpPercent() <= 30.0D) {
             continue;
           }
           partymember.add(player);
         }
         if (partymember.size() >= 7) {
           reduction = 15;
         } else {
           reduction = partymember.size() * 2;
         }
         dmg = (int)(dmg * (100 - reduction) / 100.0D);
         dmggap = dmgorigin - dmg;
         int dmggapint = (int)dmggap;
         if (partymember.size() > 0) {
           int dmgdiv = dmggapint / partymember.size();

           if (dmgdiv > 0) {
             for (L1PcInstance player : partymember) {
               int hp = player.getCurrentHp();
               player.setCurrentHp(hp - dmgdiv);
               player.send_effect(21814);
             }
           }
           partymember.clear();
         }
       }
     }


     return dmg;
   }




   private int calcNpcMagicDamage(int skillId) {
     int dmg = 0;
     dmg = calcMagicDiceDamage(skillId);




     if (this._calcType == 2) {
       dmg = dmg * getLeverage() / 10;
     } else {
       dmg = dmg * getLeverage() / 10;
     }
     if (this._calcType == 2) {
       boolean isNowWar = false;
       int castleId = L1CastleLocation.getCastleIdByArea((L1Character)this._targetNpc);
       if (castleId > 0) {
         isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
       }
       if (!isNowWar) {
         if (this._targetNpc instanceof l1j.server.server.model.Instance.L1PetInstance) {
           dmg /= 8;
         }
         if (this._targetNpc instanceof L1SummonInstance) {
           L1SummonInstance summon = (L1SummonInstance)this._targetNpc;
           if (summon.isExsistMaster()) {
             dmg /= 8;
           }
         }
       }
     }

     if (this._calcType == 2 && this._targetNpc != null) {
       int npcId = this._targetNpc.getNpcTemplate().get_npcId();
       if (npcId >= 45912 && npcId <= 45915 && !this._pc.hasSkillEffect(1013)) {
         dmg = 0;
       }
       if (npcId == 45916 && !this._pc.hasSkillEffect(1014)) {
         dmg = 0;
       }
       if (npcId == 45941 && !this._pc.hasSkillEffect(1015)) {
         dmg = 0;
       }
       if (npcId >= 46068 && npcId <= 46091 && this._pc.getCurrentSpriteId() == 6035) {
         dmg = 0;
       }
       if (npcId >= 46092 && npcId <= 46106 && this._pc.getCurrentSpriteId() == 6034) {
         dmg = 0;
       }
       if (this._targetNpc.getNpcTemplate().get_gfxid() == 7684 && !this._pc.hasSkillEffect(22035)) {
         dmg = 0;
       }
       if (this._targetNpc.getNpcTemplate().get_gfxid() == 7805 && !this._pc.hasSkillEffect(22036)) {
         dmg = 0;
       }
       if (this._targetNpc.getNpcTemplate().get_gfxid() == 7864 || this._targetNpc.getNpcTemplate().get_gfxid() == 7869 || this._targetNpc.getNpcTemplate().get_gfxid() == 7870) {
         dmg = (int)(dmg * 1.5D);
       }

       L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(this._pc.getMapId());
       if (sm != null) {
         dmg = (int)(dmg * sm.getMdmgReduction() * 0.01D);
         if (dmg <= 0)
           dmg = 1;
       }
       if (skillId == 225) {
         dmg = 50;
       }
     }

     return dmg;
   }




   private int calcMagicDiceDamage(int skillId) {
     L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
     int dice = l1skills.getDamageDice();
     int diceCount = l1skills.getDamageDiceCount();
     int value = l1skills.getDamageValue();
     int magicDamage = 0;
     double PowerMr = 0.0D;

     Random random = new Random();

     if (l1skills != null) {
       if (!l1skills.is_magic_dmg_int_impact()) {
         for (int i = 0; i < diceCount; i++) {
           if (dice > 0) {
             int plus_dmg = _random.nextInt(dice + 1) + 1;
             magicDamage += plus_dmg;
           }
         }
         magicDamage += value;
       } else {
         dice += getSpellPower() / 2;
         for (int i = 0; i < diceCount; i++) {
           if (dice > 0) {
             int plus_dmg = _random.nextInt(dice + 1) + 1;
             magicDamage += plus_dmg;
           }
         }
         magicDamage += value * (1 + getSpellPower() / 10);
       }
     }




     double criticalCoefficient = 1.4D;
     int rnd = random.nextInt(100) + 1;

     if (this._calcType == 1 || this._calcType == 2) {
       int propCritical = CalcStat.calcMagicCritical(this._pc.ability.getTotalInt());
       switch (skillId) {

         case 4:
         case 6:
         case 7:
         case 10:
         case 15:
         case 16:
         case 28:
         case 34:
         case 38:
         case 77:
         case 203:
         case 707038:
           propCritical = Config.MagicAdSetting.PRORCRITICAL_MAGIC;
           break;
       }


       if (this._pc.hasSkillEffect(7673) || this._pc.hasSkillEffect(7676) || this._pc.hasSkillEffect(7678) || this._pc.hasSkillEffect(7688)) {
         propCritical++;
       }
       propCritical = (int)(propCritical + this._pc.getBaseMagicCritical() * Config.MagicAdSetting.CHARACTERMAGICCRIRATE);











     }
     else if ((this._calcType == 3 || this._calcType == 4) &&
       rnd <= 15) {
       magicDamage = (int)(magicDamage * criticalCoefficient);
     }





     if ((this._calcType == 1 || this._calcType == 2) &&
       skillId == 77) {
       int lawful = getLawful();
       if (lawful > 0) {
         magicDamage += lawful / Config.MagicAdSetting_Wizard.DISINTLAWFULWEIGHT;
       } else if (lawful < 0) {
         magicDamage += lawful / Config.MagicAdSetting_Wizard.DISINTCHAOTICWEIGHT;
       }
       if (this._pc.getPassive(MJPassiveID.DISINTEGRATE_NEMESIS.toInt()) != null) {
         magicDamage = (int)(magicDamage * (1 + Config.MagicAdSetting_Wizard.NEMESISDAMAGE / 100));
       }
     }



     if (l1skills != null &&
       l1skills.is_magic_dmg_mr_impact()) {







       if (getTargetMr() <= Config.MagicAdSetting.TargetMr) {

         PowerMr = getTargetMr() / Config.MagicAdSetting.TargetMr_vl;
       } else if (getTargetMr() > Config.MagicAdSetting.TargetMr && getTargetMr() <= Config.MagicAdSetting.TargetMr1) {


         PowerMr = (getTargetMr() - Config.MagicAdSetting.TargetMr) / Config.MagicAdSetting.TargetMr_v2;
         PowerMr += 0.5D;
       } else if (getTargetMr() > Config.MagicAdSetting.TargetMr1 && getTargetMr() <= Config.MagicAdSetting.TargetMr2) {

         PowerMr = (getTargetMr() - Config.MagicAdSetting.TargetMr1) / Config.MagicAdSetting.TargetMr_v3;
         PowerMr += 0.75D;
       } else if (getTargetMr() > Config.MagicAdSetting.TargetMr2 && getTargetMr() <= Config.MagicAdSetting.TargetMr3) {

         PowerMr = (getTargetMr() - Config.MagicAdSetting.TargetMr3) / Config.MagicAdSetting.TargetMr_v4;
         PowerMr += 0.9375D;
         } else if (getTargetMr() > Config.MagicAdSetting.TargetMr4 && getTargetMr() <= Config.MagicAdSetting.TargetMr5) {

         PowerMr = (getTargetMr() - Config.MagicAdSetting.TargetMr4) / Config.MagicAdSetting.TargetMr_v5;
         PowerMr += 0.96875D;
         } else if (getTargetMr() > Config.MagicAdSetting.TargetMr5 && getTargetMr() <= Config.MagicAdSetting.TargetMr6) {

         PowerMr = (getTargetMr() - Config.MagicAdSetting.TargetMr5) / Config.MagicAdSetting.TargetMr_v6;
         PowerMr += 0.984375D;
         } else {

         PowerMr = (getTargetMr() - Config.MagicAdSetting.TargetMr5) / Config.MagicAdSetting.TargetMr_v7;
         PowerMr += 0.9921875D;
         }

         if (PowerMr >= 1.0D) {
         System.out.println("(魔防異常懷疑): (帳號: " + this._targetPc.getAccountName() + ") + (ID: " + this._targetPc.getName() + ")");
         }


     magicDamage = (int)(magicDamage - magicDamage * PowerMr);


     double attrDeffence = calcAttrResistance(l1skills.getAttr());



     magicDamage = (int)(magicDamage - magicDamage * attrDeffence);

     if (this._calcType == 1 || this._calcType == 2) {
       magicDamage += this._pc.getBaseMagicDmg();
     }
     if (this._calcType == 1 || this._calcType == 2) {
       int weaponAddDmg = 0;
       L1ItemInstance weapon = this._pc.getWeapon();
       if (weapon != null) {
         weaponAddDmg = this._pc.getMagicDmgup();
       }
       magicDamage = (int)(magicDamage + magicDamage * 0.01D * weaponAddDmg);
     }

     return magicDamage;
   }

   public int calcHealing(int skillId) {
     L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
     int dice = l1skills.getDamageDice();
     int value = l1skills.getDamageValue();
     int magicDamage = 0;

     int magicBonus = getMagicBonus();
     if (magicBonus > 10) {
       magicBonus = 10;
     }

     int diceCount = value + magicBonus;
     for (int i = 0; i < diceCount; i++) {
       magicDamage = (int)(magicDamage + (_random.nextInt(dice) + 1) * 0.8D);
     }

     double alignmentRevision = 1.0D;
     if (getLawful() > 0) {
       alignmentRevision += getLawful() / 32768.0D;
     }

     magicDamage = (int)(magicDamage * alignmentRevision);

     if (this._calcType == 1 || this._calcType == 2) {
       magicDamage = magicDamage * getLeverage() / 10;
       AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(this._pc.getId());
       double special_point = 1.0D;
       if (Info != null) {
         special_point += CalcStat.calcAinhasadStatSecond(Info.get_lucky()) * 0.01D;
       }
       magicDamage = (int)(magicDamage * special_point);
     }
     return magicDamage;
   }

   public int calcMrDefense(int dmg) {
     int PInt = 0;
     int mrs = 0;
     int attackPcLvSp = 0;
     int targetPcLvMr = 0;
     int ran1 = 0;
     int mrset = 0;

     if (this._calcType == 1 || this._calcType == 2) {
       PInt = this._pc.getAbility().getSp() * 2;
     } else if (this._calcType == 3) {
       PInt = this._npc.getAbility().getSp() * 2;
     }
     if (this._calcType == 1 || this._calcType == 3) {
       mrs = (int)(this._targetPc.getMr() * 1.7D - 20.0D);
     } else {
       mrs = (int)(this._targetNpc.getMr() * 1.7D - 20.0D);
     }
     if (this._calcType == 1 || this._calcType == 2) {
       attackPcLvSp = this._pc.getLevel();
     } else if (this._calcType == 3) {
       attackPcLvSp = this._npc.getLevel();
     }
     if (this._calcType == 1 || this._calcType == 3) {
       targetPcLvMr = this._targetPc.getLevel();
     } else {
       targetPcLvMr = this._targetNpc.getLevel();
     }

     Random random = new Random();
     ran1 = random.nextInt(15) + 1;
     mrset = mrs - ran1;

     int PPPP = (int)(attackPcLvSp / 8.0D + 1.0D);
     int TTTT = (int)(targetPcLvMr / 10.0D + 1.0D);
     int fail = PInt + PPPP - TTTT;

     if (mrset - fail >= 151) {
       dmg = (int)(dmg * 0.01D);
     } else if (mrset - fail >= 146 && mrset - fail <= 150) {
       dmg = (int)(dmg * 0.03D);
     } else if (mrset - fail >= 141 && mrset - fail <= 145) {
       dmg = (int)(dmg * 0.07D);
     } else if (mrset - fail >= 136 && mrset - fail <= 140) {
       dmg = (int)(dmg * 0.1D);
     } else if (mrset - fail >= 131 && mrset - fail <= 135) {
       dmg = (int)(dmg * 0.13D);
     } else if (mrset - fail >= 126 && mrset - fail <= 130) {
       dmg = (int)(dmg * 0.17D);
     } else if (mrset - fail >= 121 && mrset - fail <= 125) {
       dmg = (int)(dmg * 0.2D);
     } else if (mrset - fail >= 116 && mrset - fail <= 120) {
       dmg = (int)(dmg * 0.23D);
     } else if (mrset - fail >= 111 && mrset - fail <= 115) {
       dmg = (int)(dmg * 0.27D);
     } else if (mrset - fail >= 106 && mrset - fail <= 110) {
       dmg = (int)(dmg * 0.3D);
     } else if (mrset - fail >= 101 && mrset - fail <= 105) {
       dmg = (int)(dmg * 0.33D);
     } else if (mrset - fail >= 96 && mrset - fail <= 100) {
       dmg = (int)(dmg * 0.37D);
     } else if (mrset - fail >= 91 && mrset - fail <= 95) {
       dmg = (int)(dmg * 0.4D);
     } else if (mrset - fail >= 86 && mrset - fail <= 90) {
       dmg = (int)(dmg * 0.43D);
     } else if (mrset - fail >= 81 && mrset - fail <= 85) {
       dmg = (int)(dmg * 0.47D);
     } else if (mrset - fail >= 76 && mrset - fail <= 80) {
       dmg = (int)(dmg * 0.5D);
     } else if (mrset - fail >= 71 && mrset - fail <= 75) {
       dmg = (int)(dmg * 0.53D);
     } else if (mrset - fail >= 66 && mrset - fail <= 70) {
       dmg = (int)(dmg * 0.57D);
     } else if (mrset - fail >= 60 && mrset - fail <= 65) {
       dmg = (int)(dmg * 0.6D);
     } else if (mrset - fail >= 51 && mrset - fail <= 56) {
       dmg = (int)(dmg * 0.63D);
     } else if (mrset - fail >= 46 && mrset - fail <= 50) {
       dmg = (int)(dmg * 0.67D);
     } else if (mrset - fail >= 41 && mrset - fail <= 45) {
       dmg = (int)(dmg * 0.7D);
     } else if (mrset - fail >= 36 && mrset - fail <= 40) {
       dmg = (int)(dmg * 0.73D);
     } else if (mrset - fail >= 31 && mrset - fail <= 35) {
       dmg = (int)(dmg * 0.77D);
     } else if (mrset - fail >= 26 && mrset - fail <= 30) {
       dmg = (int)(dmg * 0.8D);
     } else if (mrset - fail >= 21 && mrset - fail <= 25) {
       dmg = (int)(dmg * 0.85D);
     } else if (mrset - fail >= 16 && mrset - fail <= 20) {
       dmg = (int)(dmg * 0.9D);
     } else if (mrset - fail >= 11 && mrset - fail <= 15) {
       dmg = (int)(dmg * 0.95D);
     } else if (mrset - fail >= 6 && mrset - fail <= 10) {
       dmg = (int)(dmg * 1.0D);
     } else {
       dmg = (int)(dmg * 1.05D);
     }
     return dmg;
   }

   private boolean criticalOccur(int prop) {
     if (this._pc != null) {
       prop += this._pc.get_magic_critical_rate();
     }

     int num = _random.nextInt(100) + 1;

     if (prop == 0) {
       return false;
     }
     if (num <= prop) {
       this._CriticalDamage = true;
     }
     return this._CriticalDamage;
   }

   private double calcAttrResistance(int attr) {
     int resist = 0;
     int resistFloor = 0;
     if (this._calcType == 1 || this._calcType == 3) {
       switch (attr) {
         case 1:
           resist = this._targetPc.getResistance().getEarth();
           break;
         case 2:
           resist = this._targetPc.getResistance().getFire();
           break;
         case 4:
           resist = this._targetPc.getResistance().getWater();
           break;
         case 8:
           resist = this._targetPc.getResistance().getWind();
           break;
       }
     } else if (this._calcType == 2 || this._calcType == 4) {

     }  if (resist < 0) {
       resistFloor = (int)(-0.45D * Math.abs(resist));
     } else if (resist < 101) {
       resistFloor = (int)(0.45D * Math.abs(resist));
     } else {
       resistFloor = (int)(45.0D + 0.09D * Math.abs(resist));
     }

     double attrDeffence = (resistFloor / 100);
     return attrDeffence;
   }

   private int calcAttrDefence(int dmg, int attr) {
     if (dmg < 1) {
       return dmg;
     }

     int resist = 0;

     if (this._calcType == 1 || this._calcType == 3) {
       switch (attr) {
         case 1:
           resist = this._targetPc.getResistance().getEarth();
           break;
         case 2:
           resist = this._targetPc.getResistance().getFire();
           break;
         case 4:
           resist = this._targetPc.getResistance().getWater();
           break;
         case 8:
           resist = this._targetPc.getResistance().getWind();
           break;
       }
     } else if (this._calcType == 2 || this._calcType == 4) {

     }
     dmg -= resist / 2;

     if (dmg < 1) {
       dmg = 1;
     }

     return dmg;
   }

   public void commit(int damage, int drainMana) {
     if (this._calcType == 1 || this._calcType == 3) {
       commitPc(damage, drainMana);
     } else if (this._calcType == 2 || this._calcType == 4) {
       commitNpc(damage, drainMana);
     }

     if (!Config.LogStatus.GMATKMSG) {
       return;
     }
     if (Config.LogStatus.GMATKMSG) {
       if ((this._calcType == 1 || this._calcType == 2) && !this._pc.isGm()) {
         return;
       }
       if ((this._calcType == 1 || this._calcType == 3) && !this._targetPc.isGm()) {
         return;
       }
     }
     String msg0 = "";
     String msg2 = "";
     String msg3 = "";
     String msg4 = "";

     if (this._calcType == 1 || this._calcType == 2) {
       msg0 = this._pc.getName();
     } else if (this._calcType == 3) {
       msg0 = this._npc.getName();
     }

     if (this._calcType == 3 || this._calcType == 1) {
       msg4 = this._targetPc.getName();
       msg2 = "HP:" + this._targetPc.getCurrentHp();
     } else if (this._calcType == 2) {
       msg4 = this._targetNpc.getName();
       msg2 = "HP:" + this._targetNpc.getCurrentHp();
     }

     msg3 = "傷害:" + damage;

     if (this._calcType == 1 || this._calcType == 2)
     {
       this._pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aG[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2));
     }
     if (this._calcType == 3 || this._calcType == 1) {
       this._targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aH[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2));
     }
   }

   private void commitPc(int damage, int drainMana) {
     if (this._targetPc.hasSkillEffect(78)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetPc.hasSkillEffect(70705)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetPc.hasSkillEffect(157)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetPc.hasSkillEffect(30004)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetPc.hasSkillEffect(30003)) {
       damage = 0;
       drainMana = 0;
     }

     if (this._calcType == 1) {
       if (drainMana > 0 && this._targetPc.getCurrentMp() > 0) {
         if (drainMana > this._targetPc.getCurrentMp()) {
           drainMana = this._targetPc.getCurrentMp();
         }
         int newMp = this._pc.getCurrentMp() + drainMana;
         this._pc.setCurrentMp(newMp);
       }
       this._targetPc.receiveManaDamage((L1Character)this._pc, drainMana);
       this._targetPc.receiveDamage((L1Character)this._pc, damage);
     } else if (this._calcType == 3) {
       this._targetPc.receiveDamage((L1Character)this._npc, damage);
     }
   }

   private void commitNpc(int damage, int drainMana) {
     if (this._targetNpc.hasSkillEffect(70705)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetNpc.hasSkillEffect(157)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetNpc.hasSkillEffect(30004)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetNpc.hasSkillEffect(30003)) {
       damage = 0;
       drainMana = 0;
     }
     if (this._targetNpc.getNpcTemplate().get_gfxid() == 7684 && this._pc.hasSkillEffect(22035)) {
       damage = 1;
       drainMana = 0;
     }
     if (this._targetNpc.getNpcTemplate().get_gfxid() == 7805 && this._pc.hasSkillEffect(22036)) {
       damage = 1;
       drainMana = 0;
     }
     if (this._targetNpc.getNpcTemplate().get_gfxid() == 7720) {
       damage = 1;
       drainMana = 0;
     }

     if (this._calcType == 2) {
       if (drainMana > 0) {
         int drainValue = this._targetNpc.drainMana(drainMana);
         int newMp = this._pc.getCurrentMp() + drainValue;
         this._pc.setCurrentMp(newMp);
       }
       this._targetNpc.ReceiveManaDamage((L1Character)this._pc, drainMana);
       this._targetNpc.receiveDamage((L1Character)this._pc, damage);
     } else if (this._calcType == 4) {
       this._targetNpc.receiveDamage((L1Character)this._npc, damage);
     }
   }


   private int 泰坦傷害() {
     double damage = 0.0D;
     L1ItemInstance weapon = null;
     weapon = this._targetPc.getWeapon();
     if (weapon != null) {
       damage = Math.round(((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * 2));
     }
     return (int)damage;
   }

   private static final int[] STUN_TYPE_SKILL = new int[] { 87, 5003, 123, 208, 242, 100242, 30006, 30005, 30081, 707113, 22055, 707041, 707119, 707056, 707099, 707054, 22025, 22026, 22027, 22031, 51006, 707152, 707159, 5003, 334, 77, 5037, 5027, 7320183, 5056, 51006, 187 };
 }



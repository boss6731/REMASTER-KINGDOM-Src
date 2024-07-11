 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.ArmorClass.MJArmorClass;
 import l1j.server.Config;
 import l1j.server.MJInstanceSystem.MJInstanceEnums;
 import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
 import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
 import l1j.server.MJTemplate.Chain.Action.MJAttackChain;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.NpcStatusDamage.NpcStatusDamageInfo;
 import l1j.server.NpcStatusDamage.NpcStatusDamageType;
 import l1j.server.server.clientpackets.C_ItemUSe;
 import l1j.server.server.datatables.CharacterBalance;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.datatables.SpecialMapTable;
 import l1j.server.server.datatables.UserProtectMonsterTable;
 import l1j.server.server.datatables.WeaponAddHitRate;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;
 import l1j.server.server.model.Instance.L1TowerInstance;
 import l1j.server.server.model.gametime.GameTimeClock;
 import l1j.server.server.model.item.function.L1MagicDoll;
 import l1j.server.server.model.poison.L1DamagePoison;
 import l1j.server.server.model.poison.L1FlameDamage;
 import l1j.server.server.model.poison.L1ParalysisPoison;
 import l1j.server.server.model.poison.L1SilencePoison;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_Attack;
 import l1j.server.server.serverpackets.S_AttackCritical;
 import l1j.server.server.serverpackets.S_AttackMissPacket;
 import l1j.server.server.serverpackets.S_AttackPacket;
 import l1j.server.server.serverpackets.S_AttackPacketForNpc;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_UseArrowSkill;
 import l1j.server.server.serverpackets.S_UseAttackSkill;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1SpecialMap;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.CalcStat;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.MJCommons;












 public class L1Attack
 {
   private Random random = new Random(System.nanoTime());

   private L1PcInstance _pc = null;

   private L1Character _target = null;

   private L1PcInstance _targetPc = null;

   private L1NpcInstance _npc = null;

   private L1NpcInstance _targetNpc = null;

   private final int _targetId;

   private int _targetX;

   private int _targetY;

   private int _statusDamage = 0;

   private static final Random _random = new Random(System.nanoTime());

   private int _hitRate = 0;

   private int _calcType;

   private static final int PC_PC = 1;

   private static final int PC_NPC = 2;

   private static final int NPC_PC = 3;

   private static final int NPC_NPC = 4;

   public boolean _isHit = false;

   public boolean _isCritical = false;

   private int _damage = 0;

   private int _attckGrfxId = 0;

   private int _attckActId = 0;


   private L1ItemInstance weapon = null;


   private L1ItemInstance Sweapon = null;
   private int _SweaponAddDmg = 0;
   private int _SweaponSmall = 0;
   private int _SweaponLarge = 0;

   private int _weaponId = 0;

   private int _weaponType = 0;

   private int _weaponType2 = 0;

   private int _weaponAddDmg = 0;

   private int _weaponSmall = 0;

   private int _weaponLarge = 0;

   private int _weaponBless = 1;

   private int _weapon_bless_level = 0;

   private int _weaponMaterial = 0;

   private int _weaponDoubleDmgChance = 0;

   private int _weaponAttrLevel = 0;

   private int _attackType = 0;

   private L1ItemInstance _arrow = null;

   private L1ItemInstance _sting = null;

   private int _leverage = 10;

   private int ICCD = 0;
   private int DCCD = 0;

   public void setLeverage(int i) {
     this._leverage = i;
   }

   private int getLeverage() {
     return this._leverage;
   }

   public void setActId(int actId) {
     this._attckActId = actId;
   }

   public void setGfxId(int gfxId) {
     this._attckGrfxId = gfxId;
   }

   public int getActId() {
     return this._attckActId;
   }

   public int getGfxId() {
     return this._attckGrfxId;
   }

   public L1Attack(L1Character attacker, L1Character target) {
     if (attacker instanceof L1PcInstance) {
       this._pc = (L1PcInstance)attacker;
       if (target instanceof L1PcInstance) {
         this._targetPc = (L1PcInstance)target;
         this._calcType = 1;
       } else if (target instanceof L1NpcInstance) {
         this._targetNpc = (L1NpcInstance)target;
         this._calcType = 2;
       }

       this.weapon = this._pc.getWeaponSwap();
       this.Sweapon = this._pc.getSecondWeapon();
       if (this.Sweapon != null) {
         this._SweaponAddDmg = this.Sweapon.getItem().getDmgModifier() + this.Sweapon.getDmgByMagic();
         this._SweaponSmall = this.Sweapon.getItem().getDmgSmall();
         this._SweaponLarge = this.Sweapon.getItem().getDmgLarge();
       }
       if (this.weapon != null) {
         this._weaponId = this.weapon.getItem().getItemId();
         this._weaponType = this.weapon.getItem().getType1();
         this._weaponType2 = this.weapon.getItem().getType();
         this._weaponAddDmg = this.weapon.getItem().getDmgModifier() + this.weapon.getDmgByMagic();
         this._weaponSmall = this.weapon.getItem().getDmgSmall();
         this._weaponLarge = this.weapon.getItem().getDmgLarge();
         this._weaponBless = this.weapon.getItem().getBless();
         this._weapon_bless_level = this.weapon.get_bless_level();
         this._weaponMaterial = this.weapon.getItem().getMaterial();
         if (this._weaponType == 20) {
           this._arrow = this._pc.getInventory().getArrow();
           if (this._arrow != null) {
             this._weaponBless = this._arrow.getItem().getBless();
             this._weaponMaterial = this._arrow.getItem().getMaterial();
           }
         }
         if (this._weaponType == 62) {
           this._sting = this._pc.getInventory().getSting();
           if (this._sting != null) {
             this._weaponBless = this._sting.getItem().getBless();
             this._weaponMaterial = this._sting.getItem().getMaterial();
           }
         }
         this
           ._weaponDoubleDmgChance = this.weapon.getItem().getDoubleDmgChance() + this.weapon.getEnchantLevel() * this.weapon.getItem().get_double_dmg_enchant_value();
         this._weaponAttrLevel = this.weapon.getAttrEnchantLevel();
       }

     } else if (attacker instanceof L1NpcInstance) {
       this._npc = (L1NpcInstance)attacker;
       if (target instanceof L1PcInstance) {
         this._targetPc = (L1PcInstance)target;
         this._calcType = 3;
       } else if (target instanceof L1NpcInstance) {
         this._targetNpc = (L1NpcInstance)target;
         this._calcType = 4;
       }
     }
     this._target = target;
     this._targetId = target.getId();
     this._targetX = target.getX();
     this._targetY = target.getY();
   }








   public boolean calcHit() {
     if (this._calcType == 1 || this._calcType == 2)
     { if (this._pc == null || this._target == null) {
         return this._isHit;
       }



       if (this._weaponType2 == 17) {
         if (this._target.hasSkillEffect(78)) {
           this._isHit = false;
         } else {
           this._isHit = true;
         }
         if (!this._pc.glanceCheck(this._targetX, this._targetY)) {
           this._isHit = false;
         }

         return this._isHit;
       }

       if (this._target.hasSkillEffect(78)) {
         if (this._pc.hasSkillEffect(92) &&
           this._target.hasSkillEffect(78)) {

           int probability = SkillsTable.getInstance().getTemplate(92).getProbabilityValue();
           int chance = this._pc.getLevel() - 79;
           if (chance >= probability)
             chance = probability;
           if (chance >= _random.nextInt(100) + 1) {
             this._targetPc.removeSkillEffect(78);
             this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 14539));
             this._targetPc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 14539));
           }
         }

         return false;
       }







       if (this._pc instanceof L1PcInstance)
       {
         if (this._pc.getInstStatus() == MJInstanceEnums.InstStatus.INST_USERSTATUS_LFCINREADY) {
           return false;
         }
       }
       if (this._calcType == 1 || this._calcType == 2) {
         if (!this._pc.glanceCheck(this._targetX, this._targetY)) {
           return this._isHit = false;
         }
       } else if (this._npc.glanceCheck(this._targetX, this._targetY)) {
         return this._isHit = false;
       }

       if (this._weaponType == 20 && this._weaponId != 190 && this._weaponId != 10000 && this._weaponId != 202011 && this._arrow == null) {
         this._isHit = false;
       } else if (this._weaponType == 62 && this._sting == null) {
         this._isHit = false;
       } else if (this._weaponId == 247 || this._weaponId == 248 || this._weaponId == 249) {
         this._isHit = false;
       } else if (this._calcType == 1) {
         if (Config.ServerAdSetting.CASTLEWAR) {

           int castle_id = L1CastleLocation.getCastleIdByArea((L1Character)this._pc);
           if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
             MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
             L1Clan defense = war.getDefenseClan();
             boolean Range = false;

             if (this._pc.getClan() != defense) {
               for (L1Object l1object : L1World.getInstance().getObject()) {
                 if (l1object instanceof L1TowerInstance) {
                   L1TowerInstance tower = (L1TowerInstance)l1object;
                   if (L1CastleLocation.checkInWarArea(castle_id, tower.getLocation())) {

                       // 計算距離，判斷是否超過10
                       Range = (this._pc.getLocation().getTileLineDistance(new Point(l1object.getX(), l1object.getY())) > 10);

                                // 如果距離超過10並且目標玩家不屬於防守方也不屬於攻擊方，且紅騎士公會ID為0
                       if (Range && this._targetPc.getClan() != defense && this._targetPc
                               .getClan() != this._pc.getClan() && this._targetPc
                               .getRedKnightClanId() == 0) {
                           // 向玩家發送訊息，告知只有在守護塔附近才能進行PK
                           this._pc.sendPackets("\fY只有在守護塔附近才能進行PK.");
                           this._targetPc.sendPackets("\fY只有在守護塔附近才能進行PK.");

                           // 設置_isHit為false，並返回false
                           this._isHit = false;
                           return false;
                       }
                   }
                 }
               }
             }
           }
         }

         if (this._pc.get_current_combat_id() == this._targetPc.get_current_combat_team_id() && this._pc
           .get_current_combat_team_id() == this._targetPc.get_current_combat_team_id()) {
           this._isHit = false;
           return false;
         }

         this._isHit = calcPcPcHit();

         if (!this._isHit &&
           this._pc.isPassive(MJPassiveID.MEISTER_ACCURACY.toInt()) &&
           this._calcType == 1 &&
           this.weapon != null) {

                // 獲取攻擊用的 MJItemSkillModel
             MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getAtk(this.weapon.getItemId());

                        // 如果模型不為空且武器等級為0
             if (model != null && this.weapon.get_item_level() == 0) {
                        // 設置玩家的命中概率
                 this._pc.set_acurucy_meister(model.d_prob / 2);

                            // 計算傷害
                 int dmg = (int)model.get((L1Character)this._pc, (L1Character)this._targetPc, this.weapon, 0.0D);

                        // 讓目標玩家承受傷害
                 this._targetPc.receiveDamage((L1Character)this._pc, dmg);

                        // 向玩家發送技能音效數據包
                 this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 13418));

                        // 如果攻擊者是GM並且造成了傷害
                 if (this._pc.isGm() && dmg > 0) {
                        // 向GM發送傷害信息
                     this._pc.sendPackets("\f3[" + this._pc.getName() + " -> " + this._targetPc.getName() + "] : 傷害: " + dmg + " / HP: " + this._targetPc.getCurrentHp());
                 }
             }
         }




         if (!this._isHit &&
           this._pc.isPassive(MJPassiveID.DODGE_BREAK.toInt())) {
           int probability = Config.MagicAdSetting_Lancer.DODGE_BREAK;
           if (this._pc.getLevel() >= 81) {
             probability += (this._pc.getLevel() - 81) / 3 * 3;
           }
           if (probability > 30) {
             probability = 30;
           }
           if (MJRnd.isWinning(100, probability)) {
             this._isHit = true;
             this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._target.getId(), 19384));
           }
         }


         if (!this._isHit &&
           this._pc.isPassive(MJPassiveID.FINE_SIGHT.toInt())) {
           int proba = 18;
           if (this._pc.getLevel() >= 87) {
             proba += this._pc.getLevel() - 87;
           }
           if (proba > 30) {
             proba = 30;
           }
           if (MJRnd.isWinning(100, proba)) {
             this._isHit = true;
             this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._target.getId(), 20747));
           }

         }
       } else if (this._calcType == 2) {

         if (this._pc.바포방 != true && this._pc.getX() == 32758 && this._pc.getY() == 32878 && this._pc.getMapId() == 2)
           return this._isHit = false;
         if (this._pc.바포방 != true && this._pc.getX() == 32794 && this._pc.getY() == 32790 && this._pc.getMapId() == 2)
           return this._isHit = false;
         if (this._pc.바포방 != true && this._pc.getX() == 32781 && this._pc.getY() == 32881 && this._pc.getMapId() == 2)
           return this._isHit = false;
         if (this._pc.바포방 != true && this._pc.getX() == 32782 && this._pc.getY() == 32881 && this._pc.getMapId() == 2)
           return this._isHit = false;
         if (this._pc.바포방 != true && this._pc.getX() == 32781 && this._pc.getY() == 32880 && this._pc.getMapId() == 2)
           return this._isHit = false;
         if (this._pc.바포방 != true && this._pc.getX() == 32782 && this._pc.getY() == 32880 && this._pc.getMapId() == 2) {
           return this._isHit = false;
         }
         this._isHit = calcPcNpcHit();


         if (!this._isHit &&
           this._pc.isPassive(MJPassiveID.MEISTER_ACCURACY.toInt())) {
           this._isHit = true;
           return this._isHit;
         }


         if (!this._isHit &&
           this._pc.isPassive(MJPassiveID.DODGE_BREAK.toInt())) {
           int probability = Config.MagicAdSetting_Lancer.DODGE_BREAK;
           if (this._pc.getLevel() >= 80) {
             probability += (this._pc.getLevel() - 80) / 3;
           }
           if (probability > 100) {
             probability = 100;
           }
           if (MJRnd.isWinning(100, probability)) {
             this._isHit = true;
             this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._target.getId(), 19384));
           }
         }

         if (!this._isHit &&
           this._pc.isPassive(MJPassiveID.FINE_SIGHT.toInt())) {
           int proba = 18;
           if (this._pc.getLevel() >= 87) {
             proba += this._pc.getLevel() - 87;
           }
           if (proba > 30) {
             proba = 30;
           }
           if (MJRnd.isWinning(100, proba)) {
             this._isHit = true;
             this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._target.getId(), 20747));
           }

         }
       }  }
     else if (this._calcType == 3)
     { if (this._npc instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
         this._isHit = do_calc_hit_companion((L1Character)this._npc);
       } else {
         this._isHit = calcNpcPcHit();
       }  }
     else if (this._calcType == 4)
     { if (this._npc instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
         this._isHit = do_calc_hit_companion((L1Character)this._npc);
       } else {
         this._isHit = calcNpcNpcHit();
       }  }
     else { if (this._targetNpc.getNpcTemplate().get_gfxid() == 7684 && !this._pc.hasSkillEffect(22035)) {
         this._isHit = false;
         return this._isHit;
       }  if (this._targetNpc.getNpcTemplate().get_gfxid() == 7805 && !this._pc.hasSkillEffect(22036)) {
         this._isHit = false;
         return this._isHit;
       }  }
      return this._isHit;
   }

   private boolean do_calc_hit_companion(L1Character attacker) {
     return MJAttackChain.getInstance().do_calculate_hit(this, attacker, this._target);
   }

   private int do_calc_damage_companion(L1Character attacker) {
     int damage = MJAttackChain.getInstance().do_calculate_damage(this, attacker, this._target);
     if (damage <= 0)
       this._isHit = false;
     return damage;
   }




   private boolean calcPcPcHit() {
     if (this._targetPc.hasSkillEffect(78) || this._targetPc.hasSkillEffect(70705)) {
       return false;
     }

     this._hitRate += PchitAdd();
     if (this._weaponType == 20 && this._arrow != null) {
       this._hitRate += this._arrow.getItem().getHitModifier();
     }


     this._hitRate += toPcSkillHit();

     MJArmorClass armor_class = MJArmorClass.find_armor_class(this._targetPc.getAC().getAc());
     if (armor_class == null) {
       this._hitRate = (int)(this._hitRate + this._targetPc.getAC().getAc() * 0.01D);
     }
     else if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {
       this._hitRate -= armor_class.get_to_pc_dodge();
     } else {
       this._hitRate -= armor_class.get_to_pc_er();
     }

     if (this._pc.getLevel() < this._targetPc.getLevel()) {
       this._hitRate -= this._targetPc.getLevel() - this._pc.getLevel();
     }


     try {
       this._hitRate += CharacterBalance.getInstance().getHit(this._pc.getType(), this._targetPc.getType());
     } catch (Exception e) {
       System.out.println("Character Add Damege Error");
     }

     if (this._pc.getLocation().getLineDistance(this._targetPc.getLocation()) >= 3.0D && this._weaponType != 20 && this._weaponType != 62 &&
       !this._pc.isSpearModeType()) {
       this._hitRate = 0;
     }

     int _jX = this._pc.getX() - this._targetPc.getX();
     int _jY = this._pc.getY() - this._targetPc.getY();

     if (this._weaponType == 24) {
       if ((_jX > 3 || _jX < -3) && (_jY > 3 || _jY < -3)) {
         this._hitRate = 0;
       }
     } else if (this._weaponType == 20 || this._weaponType == 62) {
       if ((_jX > 15 || _jX < -15) && (_jY > 15 || _jY < -15)) {
         this._hitRate = 0;
       }
     } else if (this._weaponType2 == 17) {
       if ((_jX > 5 || _jX < -5) && (_jY > 5 || _jY < -5)) {
         this._hitRate = 0;
       }
     }
     else if ((_jX > 2 || _jX < -2) && (_jY > 2 || _jY < -2)) {
       this._hitRate = 0;
     }


     if (this._hitRate > 95) {
       this._hitRate = 95;
     } else if (this._hitRate < 5) {
       this._hitRate = 5;
     }

     if (this._weaponType != 20 && this._weaponType != 62) {
       int dg = this._targetPc.getDg();
       if (dg != 0) {
         if (this._target.hasSkillEffect(106)) {
           this._hitRate = (int)(this._hitRate - dg * Config.MagicAdSetting_DarkElf.UncannyDodgePercent);
         } else {
           this._hitRate = (int)(this._hitRate - dg * Config.CharSettings.DodgePercent);
         }
       }
     } else {
       int er = this._targetPc.getTotalER();
       if (er > 0) {
         this._hitRate = (int)(this._hitRate - er * Config.CharSettings.EvasionPercent);
       }
     }

     this._hitRate = (int)(this._hitRate + WeaponAddHitRate.getInstance().getWeaponAddHitRate(this._weaponId));


     if (MJRnd.isWinning(100, this._hitRate)) {
       return true;
     }
     this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 13418));
     this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 13418));
     return false;
   }





   private boolean calcPcNpcHit() {
     if ((this._targetNpc.getNpcId() == MJRaidLoadManager.MRS_SP_VALAKAS_HPABSORB_ID || this._targetNpc
       .getNpcId() == MJRaidLoadManager.MRS_SP_VALAKAS_MPABSORB_ID) &&
       !this._pc.isValakasProduct()) {
       return false;
     }

     if (this._targetNpc.getNpcTemplate().get_npcId() == 8500138) {
       return false;
     }
     if (this._targetNpc.getHiddenStatus() != 0) {
       return false;
     }

     int npcId = this._targetNpc.getNpcTemplate().get_npcId();

     if (npcId >= 45912 && npcId <= 45915 && !this._pc.hasSkillEffect(1013)) {
       this._hitRate = 0;
       return false;
     }
     if (npcId == 45916 && !this._pc.hasSkillEffect(1014)) {
       this._hitRate = 0;
       return false;
     }
     if (npcId == 45941 && !this._pc.hasSkillEffect(1015)) {
       this._hitRate = 0;
       return false;
     }
     if (npcId >= 46068 && npcId <= 46091 && this._pc.getCurrentSpriteId() == 6035) {
       this._hitRate = 0;
       return false;
     }
     if (npcId >= 46092 && npcId <= 46106 && this._pc.getCurrentSpriteId() == 6034) {
       this._hitRate = 0;
       return false;
     }
     if (this._targetNpc.getNpcTemplate().get_gfxid() == 7684 && !this._pc.hasSkillEffect(22035)) {
       this._hitRate = 0;
       return false;
     }
     if (this._targetNpc.getNpcTemplate().get_gfxid() == 7805 && !this._pc.hasSkillEffect(22036)) {
       this._hitRate = 0;
       return false;
     }


     this._hitRate += PchitAdd();

     if (this._weaponType == 20 && this._arrow != null) {
       this._hitRate += this._arrow.getItem().getHitModifier();
     }

     MJArmorClass armor_class = MJArmorClass.find_armor_class(this._targetNpc.getAC().getAc());
     if (armor_class == null) {
       this._hitRate = (int)(this._hitRate + this._targetNpc.getAC().getAc() * 0.01D);
     }
     else if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {
       this._hitRate -= armor_class.get_to_npc_dodge();
     } else {
       this._hitRate -= armor_class.get_to_npc_er();
     }

     if (this._pc.getLevel() < this._targetNpc.getLevel()) {
       this._hitRate -= this._targetNpc.getLevel() - this._pc.getLevel();
     }

     int _jX = this._pc.getX() - this._targetNpc.getX();
     int _jY = this._pc.getY() - this._targetNpc.getY();

     if (this._weaponType == 24) {
       if ((_jX > 3 || _jX < -3) && (_jY > 3 || _jY < -3)) {
         this._hitRate = 0;
       }
     } else if (this._weaponType == 20 || this._weaponType == 62) {
       if ((_jX > 15 || _jX < -15) && (_jY > 15 || _jY < -15)) {
         this._hitRate = 0;
       }
     } else if (this._weaponType2 == 17) {
       if ((_jX > 5 || _jX < -5) && (_jY > 5 || _jY < -5)) {
         this._hitRate = 0;
       }
     }
     else if ((_jX > 2 || _jX < -2) && (_jY > 2 || _jY < -2)) {
       this._hitRate = 0;
     }


     try {
       this._hitRate += CharacterBalance.getInstance().getHit(this._pc.getType(), 10);
     } catch (Exception e) {
       System.out.println("Character NpcAdd Damege Error");
     }

     if (this._hitRate > 95) {
       this._hitRate = 95;
     } else if (this._hitRate < 5) {
       this._hitRate = 5;
     }


     if (this._targetNpc.getMapId() == 750) {
       return Config.ServerAdSetting.InfinityBattle_hit;
     }


     if ((this._targetNpc.getMapId() == 1 || this._targetNpc.getMapId() == 2 || this._targetNpc.getMapId() == 3 || this._targetNpc
       .getMapId() == 7 || this._targetNpc.getMapId() == 8 || this._targetNpc.getMapId() == 9 || this._targetNpc
       .getMapId() == 10 || this._targetNpc.getMapId() == 11 || this._targetNpc.getMapId() == 12 || this._targetNpc
       .getMapId() == 12146 || this._targetNpc.getMapId() == 12147 || (this._targetNpc
       .getMapId() >= 24 && this._targetNpc.getMapId() <= 27)) &&
       this._pc.getLevel() <= 79) {
       return true;
     }

     this._hitRate = (int)(this._hitRate + WeaponAddHitRate.getInstance().getWeaponAddHitRate(this._weaponId));


     if (MJRnd.isWinning(100, this._hitRate)) {
       return true;
     }
     if (!this._pc.isPassive(MJPassiveID.MEISTER_ACCURACY.toInt())) {
       this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 13418));
     }
     return false;
   }











   private boolean calcNpcPcHit() {
     if (this._targetPc.hasSkillEffect(78)) {
       return false;
     }

     double status = 0.0D;
     int level = Math.max(this._npc.getLevel(), 2);
     if (this._npc.getNpcTemplate().getBowActId() > 0)
     { NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.LONG_HIT, level);
       if (eInfo != null) {
         status = this._npc.getAbility().getTotalDex() * eInfo.get_increase_dmg();
       } else {
         status = this._npc.getAbility().getTotalDex();
       }  }
     else { NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.SHORT_HIT, level);
       if (eInfo != null) {
         status = this._npc.getAbility().getTotalStr() * eInfo.get_increase_dmg();
       } else {
         status = this._npc.getAbility().getTotalStr();
       }  }

     if (status <= 0.0D) {
       status = 1.0D;
     }
     this._hitRate = (int)(this._hitRate + status + this._npc.getLevel());

     if (this._npc instanceof L1PetInstance) {
       this._hitRate += this._npc.getLevel() * 2;
       this._hitRate += ((L1PetInstance)this._npc).getHitByWeapon();
     }

     this._hitRate += this._npc.getHitup();


     this._hitRate += toPcSkillHit();
     MJArmorClass armor_class = MJArmorClass.find_armor_class(this._targetPc.getAC().getAc());
     if (armor_class == null) {
       this._hitRate = (int)(this._hitRate + this._targetPc.getAC().getAc() * 0.01D);
     }
     else if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {
       this._hitRate -= armor_class.get_to_pc_dodge();
     } else {
       this._hitRate -= armor_class.get_to_pc_er();
     }

     if (this._npc.getLevel() < this._targetPc.getLevel()) {
       this._hitRate -= this._targetPc.getLevel() - this._npc.getLevel();
     }

     if (this._hitRate > 95) {
       this._hitRate = 95;
     } else if (this._hitRate < 5) {
       this._hitRate = 5;
     }

     if (this._npc.getNpcTemplate().get_ranged() <= 2) {
       int dg = this._target.getDg();
       if (dg != 0) {
         this._hitRate = (int)(this._hitRate - dg * Config.CharSettings.DodgePercent);
       }
     } else {
       int er = this._target.getTotalER();
       if (er > 0) {
         this._hitRate = (int)(this._hitRate - er * Config.CharSettings.EvasionPercent);
       }
     }

     this._hitRate += CharacterBalance.getInstance().getHit(10, this._targetPc.getType());



     if (MJRnd.isWinning(100, this._hitRate)) {
       return true;
     }
     this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 13418));
     return false;
   }





   private boolean calcNpcNpcHit() {
     if (this._targetNpc.getHiddenStatus() != 0) {
       return false;
     }
     if (this._targetNpc.getNpcTemplate().get_npcId() == 8500138) {
       return false;
     }
     int target_ac = 10 - this._targetNpc.getAC().getAc();
     int attacker_lvl = this._npc.getNpcTemplate().get_level();

     if (target_ac != 0) {
       this._hitRate = 100 / target_ac * attacker_lvl;
     } else {
       this._hitRate = 100 * attacker_lvl;
     }

     if (this._npc instanceof L1PetInstance) {
       this._hitRate += this._npc.getLevel() * 2;
       this._hitRate += ((L1PetInstance)this._npc).getHitByWeapon();
     } else if (this._npc instanceof L1SummonInstance &&
       this._npc.getMaster() != null) {
       this
         ._hitRate = (int)(this._hitRate + this._npc.getMaster().getLevel() * Config.MagicAdSetting_Wizard.SUMMON_LEVEL_ADDHIT + this._npc.getMaster().getAbility().getSp() * Config.MagicAdSetting_Wizard.SUMMON_SP_ADDHIT);
     }


     if (this._npc.getLevel() < this._targetNpc.getLevel()) {
       this._hitRate -= this._targetNpc.getLevel() - this._npc.getLevel();
     }

     this._hitRate += CharacterBalance.getInstance().getHit(10, 10);

     if (this._hitRate > 95) {
       this._hitRate = 95;
     }
     if (this._hitRate < 5) {
       this._hitRate = 5;
     }

     if (MJRnd.isWinning(100, this._hitRate)) {
       return true;
     }
     this._targetNpc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 13418));
     return false;
   }

   public int calcDamage() {
     try {
       int bowactid;
       switch (this._calcType) {
         case 1:
           if (this._pc == this._targetPc) {
             this._isHit = false;
             return 0;
           }
           this._damage = calcPcPcDamage();
           this._damage += this._pc.get_lateral_damage();
           this._damage -= this._targetPc.get_lateral_reduction();
           this._damage = Math.max(this._damage, 1);

           if (this._targetPc.getPassive(MJPassiveID.TITAN_BEAST.toInt()) != null) {
             int chance = _random.nextInt(100) + 1;

             this._targetPc.addTitanBeastChaList((L1Character)this._pc);
             if (this._targetPc.getTitanBeastChaList().size() > 1) {
               this._targetPc.setTitanBeast(true);
             }
             if (this._targetPc.isTitanBeast()) {
               this._targetPc.send_effect(20571);
               this._damage = (int)(this._damage * Config.MagicAdSetting_Warrior.TITANBEAST_REDUC_PER);
             }
           }

           if (this._weaponType == 20 || this._weaponType == 62) {

             int Bowcritical = CalcStat.calcBowCritical(this._pc.getAbility().getTotalDex()) + 1 + CalcStat.calcPureMissileCritical(this._pc.getAbility().getDex());
             if ((Bowcritical > 0 || this._pc.get_missile_critical_rate() > 0) &&
               MJRnd.isWinning(100, Bowcritical + this._pc.get_missile_critical_rate())) {
               this._damage = (int)(this._damage * Config.MagicAdSetting.MISSILECRITICALDAMAGERATE);
               if (this._weaponType == 20) {
                 this._targetPc.send_effect(13392);
               } else if (this._weaponType == 62) {
                 this._targetPc.send_effect(13398);
               }
               this._isCritical = true;
             }
           } else if (this._weaponType2 == 17) {
             int magiccritical = CalcStat.calcMagicCritical(this._pc.getAbility().getTotalInt()) + 1;
             if ((magiccritical > 0 || this._pc.get_magic_critical_rate() > 0) &&
               MJRnd.isWinning(100, magiccritical + this._pc.get_magic_critical_rate())) {
               this._damage = (int)(this._damage * Config.MagicAdSetting.MAGICCRITICALDAMAGERATE);
               this._isCritical = true;
               this._targetPc.send_effect(21124);


             }
             else {



               this._targetPc.send_effect(21122);
             }

           } else if (this._weaponType2 != 0) {

             int Dmgcritical = CalcStat.calcDmgCritical(this._pc.getClassNumber(), this._pc.getAbility().getTotalStr()) + 1 + CalcStat.calcPureMeleeCritical(this._pc.getAbility().getStr());
             if ((Dmgcritical > 0 || this._pc.get_melee_critical_rate() > 0) && MJRnd.isWinning(100, Dmgcritical + this._pc
                 .get_melee_critical_rate() + this._pc.get_final_burn_critical_rate())) {
               this._damage = (int)(this._damage * Config.MagicAdSetting.MELEECRITICALDAMAGERATE);
               this._isCritical = true;

               if (!this._pc.isSpearModeType()) {
                 this._pc.sendPackets((ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, this._weaponType, (this.Sweapon != null)));
                 Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, this._weaponType, (this.Sweapon != null)));
               } else {

                 this._pc.sendPackets((ServerBasePacket)S_Attack.getSpear((L1Object)this._pc, this._target, this._weaponType, this._isHit));
                 this._pc.broadcastPacket((ServerBasePacket)S_Attack.getSpear((L1Object)this._pc, this._target, this._weaponType, this._isHit));
               }
             }
           }


           if (this._pc.getRedKnightClanId() != 0) {
             this._pc.addRedKnightDamage((this._targetPc.getRedKnightClanId() == 0) ? this._damage : -this._damage);
           }

           if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17 && this._weaponType2 != 19) {
             if (this._targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null) {

               int percent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
               int chance = _random.nextInt(100) + 1;
               int titan_per = Config.MagicAdSetting_Warrior.TITANROCKPRO;
               int titan_rising_per = 0;
               int targetlevel = this._targetPc.getLevel();
               int demol_per = Config.MagicAdSetting_Warrior.DEMOLITIONPRO;


               if (targetlevel < 95) {
                 titan_per -= (95 - targetlevel) * 2;
                 if (titan_per <= 15) {
                   titan_per = 15;
                 }
               }
               if ((!this._targetPc.hasSkillEffect(87) &&
                 !this._targetPc.hasSkillEffect(123) &&
                 !this._targetPc.hasSkillEffect(208)) || (
                 !this._targetPc.hasSkillEffect(5056) &&
                 !this._targetPc.hasSkillEffect(5056) &&
                 !this._targetPc.hasSkillEffect(5003) &&
                 !this._targetPc.hasSkillEffect(77))) {
                 if (!MJCommons.isUnbeatable((L1Character)this._targetPc)) {
                   if (percent < 50) {
                     titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                     if (chance <= titan_rising_per) {
                       if (this._targetPc.getInventory().checkItem(41246, 5)) {
                         if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                           MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                           this._pc.send_effect(18518); break;
                         }  if (this._targetPc != null && this._weaponId == 7000262 &&
                           MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {
                           this._pc.send_effect(18518); break;
                         }
                         this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                         this._damage = 0;
                         this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12555));
                         this._targetPc.send_effect(12555);
                         this._targetPc.getInventory().consumeItem(41246, 5);
                         break;
                       }
                       this._targetPc.sendPackets(299);
                     }
                     break;
                   }
                   if (chance <= titan_per) {
                     if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                       MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                       this._pc.send_effect(18518); break;
                     }  if (this._targetPc != null && this._weaponId == 7000262 && MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                       this._pc.send_effect(18518); break;
                     }
                     this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                     this._damage = 0;
                     this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12555));
                     this._targetPc.send_effect(12555);
                   }
                 }

                 break;
               }
               if (this._targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) != null &&
                 !MJCommons.isUnbeatable((L1Character)this._targetPc)) {
                 if (percent < 50) {
                   titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                   if (chance <= titan_rising_per) {
                     if (this._targetPc.getInventory().checkItem(41246, 5)) {
                       if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                         MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                         this._pc.send_effect(18518); break;
                       }  if (this._targetPc != null && this._weaponId == 7000262 && MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                         this._pc.send_effect(18518); break;
                       }
                       this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                       this._damage = 0;
                       this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12555));
                       this._targetPc.send_effect(12555);
                       this._targetPc.getInventory().consumeItem(41246, 5);
                       break;
                     }
                     this._targetPc.sendPackets(299);
                   }
                   break;
                 }
                 if (chance <= titan_per) {
                   if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                     MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                     this._pc.send_effect(18518); break;
                   }  if (this._targetPc != null && this._weaponId == 7000262 &&
                     MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {
                     this._pc.send_effect(18518); break;
                   }
                   this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                   this._damage = 0;
                   this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12555));
                   this._targetPc.send_effect(12555);
                 }
               }
             }


             break;
           }

           if (this._weaponType2 != 17 && this._weaponType2 != 19) {
             if (this._targetPc.getPassive(MJPassiveID.TITAN_BLITZ.toInt()) != null) {

               int percent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
               int chance = _random.nextInt(100) + 1;
               int titan_per = Config.MagicAdSetting_Warrior.TITANBULLETPRO;
               int titan_rising_per = 0;
               int targetlevel = this._targetPc.getLevel();
               int demol_per = Config.MagicAdSetting_Warrior.DEMOLITIONPRO;


               if (targetlevel < 95) {
                 titan_per -= (95 - targetlevel) * 2;
                 if (titan_per <= 15) {
                   titan_per = 15;
                 }
               }
               if (!this._targetPc.hasSkillEffect(87) &&
                 !this._targetPc.hasSkillEffect(123) &&
                 !this._targetPc.hasSkillEffect(208) &&
                 !this._targetPc.hasSkillEffect(5056) &&
                 !this._targetPc.hasSkillEffect(5003) &&
                 !this._targetPc.hasSkillEffect(77)) {

                 if (!MJCommons.isUnbeatable((L1Character)this._targetPc)) {
                   if (percent < 50) {
                     titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                     if (chance <= titan_rising_per) {
                       if (this._targetPc.getInventory().checkItem(41246, 5)) {
                         if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                           MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                           this._pc.send_effect(18518); break;
                         }  if (this._targetPc != null && this._weaponId == 7000262 && MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                           this._pc.send_effect(18518); break;
                         }
                         this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                         this._damage = 0;
                         this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12557));
                         this._targetPc.getInventory().consumeItem(41246, 5);
                         this._targetPc.send_effect(12557);
                         break;
                       }
                       this._targetPc.sendPackets(299);
                     }
                     break;
                   }
                   if (chance <= titan_per) {
                     if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                       MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                       this._pc.send_effect(18518); break;
                     }  if (this._targetPc != null && this._weaponId == 7000262 &&
                       MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {
                       this._pc.send_effect(18518); break;
                     }
                     this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                     this._damage = 0;
                     this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12557));
                     this._targetPc.send_effect(12557);
                   }
                 }

                 break;
               }
               if (this._targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) != null &&
                 !MJCommons.isUnbeatable((L1Character)this._targetPc)) {
                 if (percent < 50) {
                   titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                   if (chance <= titan_rising_per) {
                     if (this._targetPc.getInventory().checkItem(41246, 5)) {
                       if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                         MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                         this._pc.send_effect(18518); break;
                       }  if (this._targetPc != null && this._weaponId == 7000262 &&
                         MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                         this._pc.send_effect(18518); break;
                       }
                       this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                       this._damage = 0;
                       this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc
                             .getId(), 12557));
                       this._targetPc.getInventory().consumeItem(41246, 5);
                       this._targetPc.send_effect(12557);
                       break;
                     }
                     this._targetPc.sendPackets(299);
                   }
                   break;
                 }
                 if (chance <= titan_per) {
                   if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                     MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                     this._pc.send_effect(18518); break;
                   }  if (this._targetPc != null && this._weaponId == 7000262 && MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                     this._pc.send_effect(18518); break;
                   }
                   this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                   this._damage = 0;
                   this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12557));
                   this._targetPc.send_effect(12557);
                 }
               }
             }


             break;
           }

           if (this._targetPc.getPassive(MJPassiveID.TITAN_MAGIC.toInt()) != null) {

             int percent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
             int chance = _random.nextInt(100) + 1;
             int titan_per = Config.MagicAdSetting_Warrior.TITANMAGICPRO;
             int titan_rising_per = 0;
             int targetlevel = this._targetPc.getLevel();
             int demol_per = Config.MagicAdSetting_Warrior.DEMOLITIONPRO;

             if (targetlevel < 95) {
               titan_per -= (95 - targetlevel) * 2;
               if (titan_per <= 15) {
                 titan_per = 15;
               }
             }

             if (!this._targetPc.hasSkillEffect(87) &&
               !this._targetPc.hasSkillEffect(123) &&
               !this._targetPc.hasSkillEffect(208) &&
               !this._targetPc.hasSkillEffect(5056) &&
               !this._targetPc.hasSkillEffect(5003) &&
               !this._targetPc.hasSkillEffect(77)) {

               if (!MJCommons.isUnbeatable((L1Character)this._targetPc)) {
                 if (percent < 50) {
                   titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                   if (chance <= titan_rising_per) {
                     if (this._targetPc.getInventory().checkItem(41246, 5)) {
                       if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                         MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                         this._pc.send_effect(18518); break;
                       }  if (this._targetPc != null && this._weaponId == 7000262 && MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                         this._pc.send_effect(18518); break;
                       }
                       this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                       this._damage = 0;
                       this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12559));
                       this._targetPc.send_effect(12559);
                       this._targetPc.getInventory().consumeItem(41246, 5);
                       break;
                     }
                     this._targetPc.sendPackets(299);
                   }
                   break;
                 }
                 if (chance <= titan_per) {
                   if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                     MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                     this._pc.send_effect(18518); break;
                   }  if (this._targetPc != null && this._weaponId == 7000262 &&
                     MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {
                     this._pc.send_effect(18518); break;
                   }
                   this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                   this._damage = 0;
                   this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12559));
                   this._targetPc.send_effect(12559);
                 }
               }

               break;
             }
             if (this._targetPc.getPassive(MJPassiveID.DEMOLITION.toInt()) != null &&
               !MJCommons.isUnbeatable((L1Character)this._targetPc)) {
               if (percent < 50) {
                 titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                 if (chance <= titan_rising_per) {
                   if (this._targetPc.getInventory().checkItem(41246, 5)) {
                     if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                       MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                       this._pc.send_effect(18518); break;
                     }  if (this._targetPc != null && this._weaponId == 7000262 &&
                       MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                       this._pc.send_effect(18518); break;
                     }
                     this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                     this._damage = 0;
                     this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc
                           .getId(), 12559));
                     this._targetPc.send_effect(12559);
                     this._targetPc.getInventory().consumeItem(41246, 5);
                     break;
                   }
                   this._targetPc.sendPackets(299);
                 }
                 break;
               }
               if (chance <= titan_per) {
                 if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
                   MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY - demol_per)) {


                   this._pc.send_effect(18518); break;
                 }  if (this._targetPc != null && this._weaponId == 7000262 && MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro - demol_per)) {

                   this._pc.send_effect(18518); break;
                 }
                 this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, 타이탄대미지());
                 this._damage = 0;
                 this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12559));
                 this._targetPc.send_effect(12559);
               }
             }
           }
           break;






         case 2:
           try {
             this._damage = calcPcNpcDamage();
             this._damage += this._pc.get_lateral_damage();
             if (this._weaponType == 20 || this._weaponType == 62) {

               int Bowcritical = CalcStat.calcBowCritical(this._pc.getAbility().getTotalDex()) + 1 + CalcStat.calcPureMissileCritical(this._pc.getAbility().getDex());
               if ((Bowcritical > 0 || this._pc.get_missile_critical_rate() > 0) &&
                 MJRnd.isWinning(100, Bowcritical + this._pc.get_missile_critical_rate())) {
                 this._damage = (int)(this._damage * Config.MagicAdSetting.MISSILECRITICALDAMAGERATE);
                 if (this._weaponType == 20) {
                   this._targetNpc.send_effect(13392);
                 } else if (this._weaponType == 62) {
                   this._targetNpc.send_effect(13398);
                 }
                 this._isCritical = true;
               }  break;
             }  if (this._weaponType2 == 17) {
               int magiccritical = CalcStat.calcMagicCritical(this._pc.getAbility().getTotalInt()) + 1;
               if ((magiccritical > 0 || this._pc.get_magic_critical_rate() > 0) &&
                 MJRnd.isWinning(100, magiccritical + this._pc.get_magic_critical_rate())) {
                 this._isCritical = true;
                 this._damage = (int)(this._damage * Config.MagicAdSetting.MAGICCRITICALDAMAGERATE);
                 this._targetNpc.send_effect(21124);



                 break;
               }


               this._targetNpc.send_effect(21122);
               break;
             }
             if (this._weaponType2 != 0)
             {

               int Dmgcritical = CalcStat.calcDmgCritical(this._pc.getClassNumber(), this._pc.getAbility().getTotalStr()) + 1 + CalcStat.calcPureMeleeCritical(this._pc.getAbility().getStr());
               if ((Dmgcritical > 0 || this._pc.get_melee_critical_rate() > 0) && MJRnd.isWinning(100, Dmgcritical + this._pc
                   .get_melee_critical_rate() + this._pc.get_final_burn_critical_rate())) {
                 this._isCritical = true;
                 this._damage = (int)(this._damage * Config.MagicAdSetting.MELEECRITICALDAMAGERATE);
                 this._pc.sendPackets((ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, this._weaponType, (this.Sweapon != null)));
                 Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, this._weaponType, (this.Sweapon != null)));
               }

             }

           } catch (Exception e) {
             e.printStackTrace();
           }
           break;
         case 3:
           if (this._npc instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
             this._damage = do_calc_damage_companion((L1Character)this._npc);
           } else {
             this._damage = calcNpcPcDamage();
           }  this._damage -= this._targetPc.get_lateral_reduction();


           bowactid = this._npc.getNpcTemplate().getBowActId();
           if (bowactid != 66) {
             if (this._targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null &&
               !this._targetPc.hasSkillEffect(87) &&
               !this._targetPc.hasSkillEffect(123) &&
               !this._targetPc.hasSkillEffect(208) &&
               !this._targetPc.hasSkillEffect(5056) &&
               !this._targetPc.hasSkillEffect(5003) &&
               !this._targetPc.hasSkillEffect(77)) {

               int percent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
               int chance = _random.nextInt(100) + 1;
               int titan_per = Config.MagicAdSetting_Warrior.TITANROCKPRO;
               int titan_rising_per = 0;
               int targetlevel = this._targetPc.getLevel();


               if (targetlevel < 95) {
                 titan_per -= (95 - targetlevel) * 2;
                 if (titan_per <= 15) {
                   titan_per = 15;
                 }
               }

                    // 檢查目標角色是否不可擊敗
                 if (!MJCommons.isUnbeatable((L1Character)this._targetPc)) {
                        // 如果隨機百分比小於 50
                     if (percent < 50) {
                            // 計算泰坦上升的概率
                         titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                                // 如果機率小於或等於泰坦上升的概率
                         if (chance <= titan_rising_per) {
                                    // 檢查目標玩家的背包中是否有 ID 為 41246 的物品且數量不少於 5
                             if (this._targetPc.getInventory().checkItem(41246, 5)) {
                                        // NPC 受到反擊屏障傷害
                                 this._npc.receiveCounterBarrierDamage((L1Character)this._targetPc, 泰坦傷害());
                                    // 將傷害設置為 0
                                 this._damage = 0;
                                        // 向目標玩家發送技能音效數據包，音效 ID 為 12555
                                 this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12555));
                                        // 消耗目標玩家的 5 個 ID 為 41246 的物品
                                 this._targetPc.getInventory().consumeItem(41246, 5);
                                 break;
                             }
                                // 向目標玩家發送系統訊息，提示催化劑不足
                             this._targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("泰坦洛克: 催化劑不足。"));
                         }
                         break;
                     }
                 if (chance <= titan_per) {
                   this._npc.receiveCounterBarrierDamage((L1Character)this._targetPc, 泰坦傷害());
                   this._damage = 0;
                   this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12555));
                 }
               }
             }

             break;
           }

           if (this._targetPc.getPassive(MJPassiveID.TITAN_BLITZ.toInt()) != null &&
             !this._targetPc.hasSkillEffect(87) &&
             !this._targetPc.hasSkillEffect(123) &&
             !this._targetPc.hasSkillEffect(208) &&
             !this._targetPc.hasSkillEffect(5056) &&
             !this._targetPc.hasSkillEffect(5003) &&
             !this._targetPc.hasSkillEffect(77)) {

             int percent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
             int chance = _random.nextInt(100) + 1;
             int titan_per = Config.MagicAdSetting_Warrior.TITANROCKPRO;
             int titan_rising_per = 0;
             int targetlevel = this._targetPc.getLevel();


             if (targetlevel < 95) {
               titan_per -= (95 - targetlevel) * 2;
               if (titan_per <= 15) {
                 titan_per = 15;
               }
             }

               // 檢查目標角色是否不可擊敗並且隨機百分比是否小於等於 50
               if (!MJCommons.isUnbeatable((L1Character)this._targetPc) &&
                       percent <= 50) {
                        // 計算泰坦上升的概率
                   titan_rising_per = titan_per + Config.MagicAdSetting_Warrior.TITANRISINGPRO;
                        // 如果機率小於或等於泰坦上升的概率
                   if (chance <= titan_rising_per) {
                            // 檢查目標玩家的背包中是否有 ID 為 41246 的物品且數量不少於 5
                       if (this._targetPc.getInventory().checkItem(41246, 5)) {
                                // NPC 受到反擊屏障傷害
                           this._npc.receiveCounterBarrierDamage((L1Character)this._targetPc, 泰坦傷害());
                                // 將傷害設置為 0
                           this._damage = 0;
                                // 向目標玩家發送技能音效數據包，音效 ID 為 12557
                           this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12557));
                                // 消耗目標玩家的 5 個 ID 為 41246 的物品
                           this._targetPc.getInventory().consumeItem(41246, 5);
                           break;
                       }
                            // 向目標玩家發送系統訊息，提示催化劑不足
                       this._targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("泰坦彈: 催化劑不足。"));
                   }
               }
           }
             break;




         case 4:
           if (this._npc instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
             this._damage = do_calc_damage_companion((L1Character)this._npc); break;
           }
           this._damage = calcNpcNpcDamage();
           break;
       }


     } catch (Exception e) {
       e.printStackTrace();
     }

     if (this._calcType == 1 &&
       this._targetPc == this._pc) {
       this._damage = 0;
     }

     return this._damage;
   }





     public int calcPcPcDamage() {
         // 獲取武器對小目標的最大傷害
         int weaponMaxDamage = this._weaponSmall;
         // 初始化武器傷害為 0
         int weaponDamage = 0;

         // 檢查區域類型，如果攻擊者在安全區域而目標不在安全區域或在非安全區域
         if ((this._pc.getZoneType() == 1 && this._targetPc.getZoneType() == 0) ||
                 (this._pc.getZoneType() == 1 && this._targetPc.getZoneType() == -1)) {
             this._isHit = false;
         }

         // 檢查地圖ID，如果在禁止PK的地圖中
         if (this._pc.getMapId() == 612 || this._pc.getMapId() == 254 ||
                 this._pc.getMapId() == 1930 || this._pc.getMapId() == 13005) {
             weaponDamage = 0;
             this._isHit = false;
             this._pc.sendPackets("\fY此地圖 無法 進行PK 。");

             return weaponDamage;
         }

         // 其他代碼將在這裡填充...

         return weaponDamage;
     }

     boolean secondWeapon = false;
     if (this._weaponType == 0) {
       weaponDamage = 0;
     } else if (this._weaponType2 == 17) {
       weaponDamage = weaponMaxDamage + this._weaponAddDmg;
     } else if (this._pc.is전사() && this._pc.isPassive(MJPassiveID.SLAYER.toInt()) && this._pc.getSecondWeapon() != null) {
       int ran = _random.nextInt(100);
       if (ran < 50) {
         secondWeapon = true;
         weaponDamage = this._SweaponSmall + this._SweaponAddDmg;
       } else {
         weaponDamage = weaponMaxDamage + this._weaponAddDmg;
       }
     } else {
       weaponDamage = weaponMaxDamage + this._weaponAddDmg;
     }

     if (weaponDamage <= 0) {
       weaponDamage = 1;
     }
     int weaponTotalDamage = weaponDamage + ((this.weapon == null) ? 1 : getEnchantDmg(secondWeapon ? this.Sweapon : this.weapon)) + this._weapon_bless_level;


     if (this._weaponType == 54 && _random.nextInt(Config.MagicAdSetting_DarkElf.DOUBLEPCPCCHANCE) + 1 <= this._weaponDoubleDmgChance - this.weapon
       .get_durability() && this._pc.isDarkelf()) {
       weaponTotalDamage = (int)(weaponTotalDamage * Config.MagicAdSetting_DarkElf.DOUBLEDMG);
       this._pc.sendPackets((ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, 54, (this.Sweapon != null)));
       Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, 54, (this.Sweapon != null)));
       this._isCritical = true;
     }

     double dmg = (weaponTotalDamage + this._statusDamage);


     if (this._weaponType2 == 17) {
       dmg = MJCommons.getKeylinkPcPcDamage((L1Character)this._pc, (L1Character)this._targetPc, weaponTotalDamage);
     } else if (this._weaponType != 20 && this._weaponType != 62) {
       dmg += (this._pc.getDmgup() + this._pc.getDmgRate());
     } else {
       dmg += (this._pc.getBowDmgup() + this._pc.getBowDmgRate());
     }

     if (this._pc.hasSkillEffect(507)) {
       dmg++;
     }
     if (this._targetPc.hasSkillEffect(508)) {
       dmg--;
     }
     if (this._weaponType == 20) {
       if (this._arrow != null) {
         dmg += this._arrow.getItem().getDmgModifier();
       } else {
         dmg += (MJRnd.next(2) + 1);
       }
     } else if (this._weaponType == 62) {
       int add_dmg = this._sting.getItem().getDmgSmall();
       if (add_dmg == 0) {
         add_dmg = 1;
       }
       dmg = dmg + _random.nextInt(add_dmg) + 1.0D;
     }

     if (this._targetPc.hasSkillEffect(40005))
       dmg -= 8.0D;
     if (this._targetPc.hasSkillEffect(508))
       dmg--;
     if (this._targetPc.hasSkillEffect(3000130))
       dmg -= 5.0D;
     if (this._targetPc.hasSkillEffect(3000129))
       dmg -= 5.0D;
     if (this._targetPc.hasSkillEffect(3000128)) {
       dmg -= 3.0D;
     }

     if (this._weaponType != 0) {
       switch (this.weapon.get_item_level()) {
         case 1:
           WeaponLevelAttack(this._pc, this._target, 3740, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
         case 2:
           WeaponLevelAttack(this._pc, this._target, 16018, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
         case 3:
           WeaponLevelAttack(this._pc, this._target, 16024, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
         case 4:
           WeaponLevelAttack(this._pc, this._target, 4167, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
       }


     }
     if (this._targetPc.hasSkillEffect(158)) {
       int probability = SkillsTable.getInstance().getTemplate(158).getProbabilityValue();
       int bonus = 2;
       if (MJRnd.isWinning(100, probability)) {
         this._targetPc.setCurrentHp((int)(this._targetPc.getCurrentHp() + dmg * bonus));
         this._targetPc.send_effect(18930);
       }
     }


     MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getAtk(this._weaponId);
     if (model != null && this.weapon.get_item_level() == 0) {
       dmg += model.get((L1Character)this._pc, (L1Character)this._targetPc, this.weapon, dmg);
     }

     if (this._pc.getEquipSlot() != null &&
       this._pc.getEquipSlot().getArmors() != null) {
       for (L1ItemInstance item : this._pc.getEquipSlot().getArmors()) {
         model = MJItemSkillModelLoader.getInstance().getAtk(item.getItemId());
         if (model != null) {
           dmg += model.get((L1Character)this._pc, (L1Character)this._targetPc, item, dmg);
         }
       }
     }

     if (this._weaponType == 0) {
       dmg = 1.0D;
     }

     if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 14) {
       dmg += L1MagicDoll.getDamageAddByDoll((L1Character)this._pc);
     }

     L1DollInstance doll = this._pc.getMagicDoll();
     if (doll != null) {
       try {
         L1ItemInstance doll_item = this._pc.getInventory().getItem(doll.getItemObjId());
         int chance = MJRnd.next(100);
         int Absorption_point = 0;
         if (doll_item != null) {
           if (doll_item.get_Doll_Bonus_Value() == 130) {
             if (chance <= Config.MagicDollInfo.MP_SMALL_CHANCE_PC) {
               Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_SMALL_MIN_PC, Config.MagicDollInfo.MP_SMALL_MAX_PC);

               if (this._targetPc.getCurrentMp() > Absorption_point) {
                 this._pc.setCurrentMp(this._pc.getCurrentMp() + Absorption_point);
                 this._targetPc.setCurrentMp(this._targetPc.getCurrentMp() - Absorption_point);
               }
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 137 || doll_item.get_Doll_Bonus_Value() == 155) {
             if (chance <= Config.MagicDollInfo.HP_LARGE_CHANCE_PC) {
               Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_LARGE_MIN_PC, Config.MagicDollInfo.HP_LARGE_MAX_PC);

               if (this._targetPc.getCurrentHp() > Absorption_point) {
                 this._pc.setCurrentHp(this._pc.getCurrentHp() + Absorption_point);
                 this._targetPc.setCurrentHp(this._targetPc.getCurrentHp() - Absorption_point);
               }
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 138) {
             if (chance <= Config.MagicDollInfo.MP_LARGE_CHANCE_PC) {
               Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_LARGE_MIN_PC, Config.MagicDollInfo.MP_LARGE_MAX_PC);

               if (this._targetPc.getCurrentMp() > Absorption_point) {
                 this._pc.setCurrentMp(this._pc.getCurrentMp() + Absorption_point);
                 this._targetPc.setCurrentMp(this._targetPc.getCurrentMp() - Absorption_point);
               }
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 139) {
             Absorption_point = MJRnd.next(100);
             if (Absorption_point < Config.MagicDollInfo.DOLL_SOULOFFLAME) {
               this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._pc.getId(), 19264, 19));
               this._pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 19264, 8));
               Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_SkillSound(this._pc.getId(), 19264));
               this._pc.setSkillEffect(175, 8000L);
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 140) {
             Absorption_point = MJRnd.next(100);
             if (Absorption_point < Config.MagicDollInfo.DOLL_JUDGEMENT) {
               if (this._targetPc.hasSkillEffect(9598)) {
                 this._targetPc.removeSkillEffect(9598);
               }
               if (this._pc.isCrown() || this._pc.isKnight()) {
                 this._targetPc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -15);
                 this._targetPc.setdoll_judgement_type(1);
               } else if (this._pc.isElf() || this._pc.isDarkelf()) {
                 this._targetPc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -15);
                 this._targetPc.setdoll_judgement_type(2);
               } else if (this._pc.isDragonknight() || this._pc.isBlackwizard()) {
                 this._targetPc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -15);
                 this._targetPc.setdoll_judgement_type(3);
               } else if (this._pc.is전사() || this._pc.isFencer() || this._pc.isLancer()) {
                 this._targetPc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -15);
                 this._targetPc.setdoll_judgement_type(4);
               } else if (this._pc.isWizard()) {
                 this._targetPc.getResistance().addMr(-15);
                 this._targetPc.setdoll_judgement_type(5);
                 this._targetPc.sendPackets((ServerBasePacket)new S_SPMR(this._targetPc));
               }

               this._targetPc.setSkillEffect(9598, 8000L);
               this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 18490));
               this._targetPc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 18490));
               SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._targetPc);
               L1SkillUse.on_icons(this._targetPc, 5001, 8);
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 141) {
             Absorption_point = MJRnd.next(100);
             if (Absorption_point < Config.MagicDollInfo.DOLL_DECAY_POTION) {
               this._targetPc.setSkillEffect(71, 4000L);
               this._targetPc.send_other_party_effect(this._pc, 2232);
             }
           } else if ((doll_item.get_Doll_Bonus_Value() == 152 || doll_item.get_Doll_Bonus_Value() == 153 || doll_item
             .get_Doll_Bonus_Value() == 154) &&
             chance <= Config.MagicDollInfo.HP_SMALL_CHANCE_PC) {
             Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_SMALL_MIN_PC, Config.MagicDollInfo.HP_SMALL_MAX_PC);

             if (this._targetPc.getCurrentHp() > Absorption_point) {
               this._pc.setCurrentHp(this._pc.getCurrentHp() + Absorption_point);
               this._targetPc.setCurrentHp(this._targetPc.getCurrentHp() - Absorption_point);
             }

           }
         }
       } catch (Exception e) {
            // 從玩家的背包中獲取特定娃娃物品
           L1ItemInstance doll_item = this._pc.getInventory().getItem(doll.getItemObjId());

                // 初始化吸收點為 0
           int Absorption_point = 0;

            // 輸出調試信息，包含娃娃物品名稱、物品ID、娃娃的額外獎勵值以及吸收點
           System.out.println(String.format("[玩家->玩家（潛能效果錯誤）:（物品ID/實例ID: %s / %s）（潛能值: %d）（潛能概率: %d）]",
                   new Object[] {
                           doll_item.getName(), // 娃娃物品名稱
                           Integer.valueOf(doll.getItemObjId()), // 娃娃物品的物品實例ID
                           Integer.valueOf(doll_item.get_Doll_Bonus_Value()), // 娃娃的額外獎勵值
                           Integer.valueOf(Absorption_point) // 吸收點數
                   }));
       }
 }
     dmg += L1MagicDoll.useSkillByDoll((L1Character)this._pc, (L1Character)this._targetPc);



     if (this._pc.getPassive(MJPassiveID.CRASH.toInt()) != null) {
       int chance = _random.nextInt(100) + 1;
       if (Config.MagicAdSetting_Warrior.CRASHFO >= chance) {
         int crashdmg = (int)(2.0D + this._pc.getLevel() * Config.MagicAdSetting_Warrior.CRASHDMG);
         int furydmg = 0;

         if (this._pc.getPassive(MJPassiveID.FURY.toInt()) != null) {
           chance = _random.nextInt(100) + 1;
           if (Config.MagicAdSetting_Warrior.FURYFO >= chance) {
             furydmg = (int)(furydmg + crashdmg * Config.MagicAdSetting_Warrior.FURYDMG);

             this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12489));
             this._targetPc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12489));
           }
         }
         dmg += (crashdmg + furydmg);

         this._targetPc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12487));
         this._targetPc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 12487));
       }
     }

     if (this._pc.hasSkillEffect(10534) &&
       this._pc.getClanRank() >= 9) {
       dmg += 30.0D;
     }


     dmg += 釣魚屬性附魔效果();


     if (this._calcType == 1) {
       if (this._pc.getAddDamageRate() >= CommonUtil.random(100)) {
         dmg += this._pc.getAddDamage();
       }
       if (this._targetPc.getAddReductionRate() >= CommonUtil.random(100)) {
         dmg -= this._targetPc.getAddReduction();
       }
     }




     dmg += (Math.max(0, this._pc.getLevel() - Config.CharSettings.Level_Dmg) * Config.CharSettings.Level_Dmg_Count);

     if (this._pc.hasSkillEffect(220)) {
       dmg += 10.0D;
     }


     if (this._targetPc.getTrueTarget() > 0) {
       dmg *= (1 + this._targetPc.getTrueTarget() / 100);
     }


     if (this._calcType == 1) {
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
               if (!mon.isDead()) {

                 int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId());
                 if (monid != 0) {
                   attack_ok = true;

                   break;
                 }
               }
             }
           }
              // 如果攻擊不允許且玩家或目標玩家的公會ID等於保護公會ID
           if (!attack_ok && (
                   this._pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION || this._targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION)) {
                  // 向玩家發送系統消息：新玩家之間不能互相攻擊
             this._pc.sendPackets((ServerBasePacket)new S_SystemMessage("新用戶之間不能互相攻擊。"));
             this._targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("新用戶之間不能互相攻擊。"));
             return 0; // 返回 0 結束方法
           }

              // 如果玩家或目標玩家的公會ID等於保護公會ID
           else if (this._pc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION || this._targetPc.getClanid() == Config.ServerAdSetting.CLANIDPROTECTION) {
             dmg /= 2.0D; // 傷害減半
                // 向玩家發送系統消息：新用戶只能造成 50% 的傷害
             this._pc.sendPackets((ServerBasePacket)new S_SystemMessage("新用戶只能造成 50% 的傷害。"));
                  // 向目標玩家發送系統消息：新用戶只能受到 50% 的傷害
             this._targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("新用戶只能受到 50% 的傷害。"));
           }
         }
       }


     if (this._pc.hasSkillEffect(182) &&
       this._weaponType != 20) {
       dmg += 30.0D;
       this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 6591));
       this._pc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 6591));
       this._pc.removeSkillEffect(182);
     }



     if (this._pc.TripleArrow) {
       dmg *= Config.MagicAdSetting_Elf.TripleArrow_dmg_pc;
       if (this._pc.isPassive(MJPassiveID.TRIPLE_BOOST.toInt())) {
         dmg *= 1.0D + Config.MagicAdSetting_Elf.TripleArrow_boost_dmg_pc;
       }
     }


     try {
       dmg += CharacterBalance.getInstance().getDmg(this._pc.getType(), this._targetPc.getType());

       dmg *= CharacterBalance.getInstance().getDmgRate(this._pc.getType(), this._targetPc.getType());
     } catch (Exception e) {
       System.out.println("Character Enchant Per Damage for L1Attack");
     }


     if (this._pc.getResistance().getPVPweaponTotalDamage() > 0) {
       dmg += (this._pc.getResistance().getPVPweaponTotalDamage() + 1);
     }

     if (this._pc.is_assassination_level2()) {
       dmg += 2.0D;
     } else if (this._pc.is_assassination_level1()) {
       dmg++;
     }
     if (this._weaponType2 == 18) {
       L1WeaponSkill.체인소드(this._pc, (L1Character)this._targetPc);
       if (this._pc.hasSkillEffect(51002) &&
         !this._targetPc.hasSkillEffect(51003)) {
         this._targetPc.getAC().addAc(-5);
         this._targetPc.addDg(-10);
         this._targetPc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -5);
         this._targetPc.setSkillEffect(51003, (this._pc
             .getSkillEffectTimeSec(51002) * 1000));
         this._targetPc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._targetPc));
         SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._targetPc);
       }

       if (this._pc.isChainSwordExposed()) {
         if (this._pc.hasSkillEffect(51007)) {
           if (this._pc.getChainSwordStep() != 1) {
             this._pc.setChainSwordStep(1);
             this._pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 1), true);
           }

         } else if (this._pc.getChainSwordStep() == 1) {
           if (this._pc.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
             this._pc.setChainSwordStep(3);
             this._pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 3), true);
           } else if (this._pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
             this._pc.setChainSwordStep(2);
             this._pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 2), true);
           }
         }
       }
     }


     if (this._pc.FouSlayer) {
       dmg *= Config.MagicAdSetting_DragonKnight.FouSlayer_dmg_pc;

       if (this._pc.hasSkillEffect(51002)) {
         dmg += 3.0D;
         fouslayer_brave(this._pc, (L1Character)this._targetPc);
       }












       dmg += this._pc.getFouDmg();
     }

     if (this._targetPc.get_pvp_defense_per() > 0) {
       dmg -= dmg * 0.01D * this._targetPc.get_pvp_defense_per();
     }



     dmg = toPcBuffDmg(dmg);

     if (this._pc != null && this._pc.isPassive(MJPassiveID.RAMPAGE.toInt()) &&
       this._target.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL) &&
       MJRnd.isWinning(100, Config.MagicAdSetting_DragonKnight.RAMPAGE_P)) {
       dmg *= Config.MagicAdSetting_DragonKnight.RAMPAGE_D;
     }



     if (this._targetPc.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL)) {
       if (this._targetPc.isPassive(MJPassiveID.ARTERIAL_CIRCLE.toInt()) &&
         MJRnd.isWinning(100, Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_CHANCE)) {
         dmg -= Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_REDUC;
         this._targetPc.send_effect(20118, true);
       }
       if (this._targetPc.getAbnormalStatusPvPReduction() != 0) {
         dmg -= this._targetPc.getAbnormalStatusPvPReduction();
       }
     }











     MJArmorClass armor_class = MJArmorClass.find_armor_class(this._targetPc.getAC().getAc());
     if (armor_class != null) {
       if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {
         dmg -= armor_class.get_to_pc_reduction();
       } else {
         dmg -= armor_class.get_to_pc_long_reduction();
       }
     }
     dmg -= L1MagicDoll.getDamageReductionByDoll((L1Character)this._npc, (L1Character)this._targetPc);

     double total_reduction = 0.0D;
     total_reduction += getReductionIgnore(this._targetPc
         .getDamageReductionByArmor() + this._targetPc.getDamageReduction(), this._targetPc.get_pvp_defense() + this._targetPc
         .getResistance().getcalcPcDefense() + this._targetPc.get_class_level_pvp_reduction(), (L1Character)this._pc);

     if (this._targetPc.hasSkillEffect(136) && this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {

       L1ItemInstance target_weapon = this._targetPc.getWeapon();
       if (target_weapon != null && target_weapon.getItem().getType() == 1) {
         int probability = SkillsTable.getInstance().getTemplate(136).getProbabilityValue();

         if (MJRnd.isWinning(100, probability)) {
           int weapon_index = MJRnd.next(4);
           if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
             MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
             this._pc.send_effect(18518);
           } else if (this._targetPc != null && this._weaponId == 7000262 &&
             MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro)) {
             this._pc.send_effect(18518);
           } else {
             this._targetPc.send_effect(Config.MagicAdSetting_Elf.INFERNOEFFECTS[weapon_index]);

             int inferno_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel() + this._targetPc.getDmgRate() + this._targetPc.getDmgup() + this._targetPc.getDmgupByArmor();
             this._pc.receiveDamage((L1Character)this._targetPc, (weapon_index + 1) * inferno_damage);
             this._pc.send_action(2);
           }
         }
       }
     }

     if (!this._targetPc.hasSkillEffect(87) && !this._targetPc.hasSkillEffect(123) &&
       !this._targetPc.hasSkillEffect(5056) && !this._targetPc.hasSkillEffect(208) &&
       this._targetPc.hasSkillEffect(245) && this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {

       L1ItemInstance target_weapon = this._targetPc.getWeapon();
       if (target_weapon != null && target_weapon.getItem().getType() == 18) {
         int probability = SkillsTable.getInstance().getTemplate(245).getProbabilityValue();
         if (MJRnd.isWinning(100, probability)) {
           if (this._targetPc != null && this._pc.isPassive(MJPassiveID.PARADOX.toInt()) &&
             MJRnd.isWinning(1000000, Config.MagicAdSetting_Fencer.PARADOXPROBABILITY)) {
             this._pc.send_effect(18518);
           } else if (this._targetPc != null && this._weaponId == 7000262 &&
             MJRnd.isWinning(1000000, Config.MagicAdSetting.Silenpro)) {
             this._pc.send_effect(18518);
           } else {
             dmg = 0.0D;
             this._targetPc.send_effect(18410);

             int halpas_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel() + this._targetPc.getDmgRate() + this._targetPc.getDmgup() + this._targetPc.getDmgupByArmor();
             this._pc.receiveDamage((L1Character)this._targetPc, halpas_damage * Config.MagicAdSetting_DragonKnight.HALPASDMGX);
             this._pc.send_action(2);
           }
         }
       }
     }



     int chance41 = _random.nextInt(100) + 1;
     if (this._weaponType != 0 && this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17 && (
       this._pc.hasSkillEffect(171) || this._pc.hasSkillEffect(152) || this._pc
       .hasSkillEffect(117)) &&
       chance41 <= Config.MagicAdSetting_Elf.COMBINECHANCE) {
       dmg *= 1.5D;
       if (this._targetPc != null) {
         S_SkillSound ss = new S_SkillSound(this._targetPc.getId(), 7727);
         this._targetPc.sendPackets((ServerBasePacket)ss, false);
         this._targetPc.broadcastPacket((ServerBasePacket)ss);
       }
     }




     if (this._pc.hasSkillEffect(105) && (this._weaponType == 54 || this._weaponType == 58)) {
       int rate = 0;
       int ratetemp = 0;
       double doubledmg = 0.0D;
       if (this._pc.getLevel() >= 90) {
         ratetemp = (this._pc.getLevel() - 88) / 2;
         rate += ratetemp * 2;
       }

       if (this._pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
         int lvl = this._pc.getLevel();
         if (lvl >= 80)
           rate += this._pc.getLevel() - 79;
       }
       rate = (int)(rate + Config.MagicAdSetting_DarkElf.DOUBLEBREAKCHANCE);

       if (this._pc.hasSkillEffect(105) &&
         _random.nextInt(100) + 1 <= rate) {








         this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 6532));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 6532));
         doubledmg += Config.MagicAdSetting_DarkElf.DOUBLEBRAKEDMGPC;
       }


       if (doubledmg <= 0.0D) {
         doubledmg = 1.0D;
       }

       dmg *= doubledmg;
     }

     if (this._pc.isPassive(MJPassiveID.BURNING_SPIRIT_PASSIVE.toInt()) && (this._weaponType == 54 || this._weaponType == 58)) {
       int burningrate = 0;
       double doubledmg = 0.0D;
       if (this._pc.getLevel() >= 45) {
         burningrate += this._pc.getLevel() - 45;
       }
       if (burningrate >= 10) {
         burningrate = 10;
       }
       burningrate = (int)(burningrate + Config.MagicAdSetting_DarkElf.BURNINGSPIRITCHANCE);
       if (_random.nextInt(100) + 1 <= burningrate) {
         this._targetPc.send_effect(7727);
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 7727));
         doubledmg += Config.MagicAdSetting_DarkElf.BURNINGSPIRITNPC;
       }

       if (doubledmg <= 0.0D) {
         doubledmg = 1.0D;
       }
       dmg *= doubledmg;
     }

     if (this._weaponType != 0 && this._weaponType != 20 && this._weaponType != 62 &&
       this._pc.hasSkillEffect(94) &&
       this._pc.getEquipSlot().isWeapon(this.weapon)) {

       int probability = SkillsTable.getInstance().getTemplate(94).getProbabilityValue();
       if (this._pc.getLevel() >= 75)
         probability += (this._pc.getLevel() - 75) * 1;
       if (MJRnd.isWinning(100, probability)) {
         this._targetPc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetPc.getId(), 17223));
         dmg *= Config.MagicAdSetting_Knight.BLOWATTACKDMG;
       }
     }



     if ((this._weaponType == 20 || this._weaponType == 62) &&
       this._pc.hasSkillEffect(167)) {
       int probability = SkillsTable.getInstance().getTemplate(167).getProbabilityValue();
       if (this._pc.getLevel() >= 84)
         probability += (this._pc.getLevel() - 84) / 2 * 2;
       if (probability >= 20) {
         probability = 20;
       }
       if (MJRnd.isWinning(100, probability)) {
         dmg *= Config.MagicAdSetting_Elf.CYCLONEVAL;
         this._targetPc.send_effect(17557);
       }
     }


     if (this._pc != null && this._pc.isPassive(MJPassiveID.FLAME.toInt()) &&
       this._pc.getEquipSlot().isWeapon(this.weapon)) {
       int percent = Config.MagicAdSetting_Fencer.FLAME_PASSIVE;
       if (MJRnd.isWinning(1000000, percent)) {
         L1FlameDamage.doInfection((L1Character)this._pc, (L1Character)this._targetPc, 1000, this._pc
             .getLevel() * 2 / Config.MagicAdSetting_Fencer.FLAME_PASSIVE_DMG);
       }
     }


     if (this._pc != null && this._pc.isPassive(MJPassiveID.RAGE.toInt()) &&
       this._pc.getEquipSlot().isWeapon(this.weapon)) {
       int percent = Config.MagicAdSetting_Fencer.RAGEPROBABILITY;
       if (MJRnd.isWinning(1000000, percent)) {
         this._targetPc.send_effect(18517);
         dmg *= Config.MagicAdSetting_Fencer.RAGEDMG;
       }
     }


     if (this._targetPc.isPassive(MJPassiveID.GLORY_EARTH.toInt())) {
       int chance = MJRnd.next(100);
       if (chance <= Config.MagicAdSetting_Elf.GLORYEARTH_CHANCE) {
         dmg -= Config.MagicAdSetting_Elf.GLORYEARTH_DMG;
         this._targetPc.send_effect(19318);
       }
     }

     if (this._pc != null && this._pc.isPassive(MJPassiveID.DEADLY_STRIKE.toInt())) {
       int percent = Config.MagicAdSetting_Lancer.DEADLY_STRIKE_PRO * 10000;
       if (MJRnd.isWinning(1000000, percent)) {
         dmg *= Config.MagicAdSetting_Lancer.DEADLY_STRIKE;
         this._targetPc.send_effect(19367);
       }
     }

     if (this._pc.isSpearModeType() &&
       this._pc != null && this._pc.isPassive(MJPassiveID.INCREASE_RANGE.toInt())) {
       int pclevel = this._pc.getLevel();
       int rate = 0;
       int ratedmg = 0;
       if (pclevel < 90) {
         pclevel = 90;
       }
       if (pclevel >= 90) {
         rate = (pclevel - 90) / 2;
         ratedmg = rate * 2;
       }
       if (ratedmg >= 10) {
         ratedmg = 10;
       }
       dmg += dmg * (ratedmg + 10) / 100.0D;
     }


     if (this._pc != null && this._pc.hasSkillEffect(5158)) {
       dmg += dmg * 0.3D;
     }

     if (this._targetPc.getPassive(MJPassiveID.VENGEANCE.toInt()) != null &&
       this._targetPc != null) {
       int skill_percent = 10000;
       int reduction_percent = 0;

       int HPpercent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
       if (HPpercent < 50) {
         skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_PERCENT;
         reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_REDUCTION;
         if (MJRnd.isWinning(1000000, skill_percent)) {
           dmg -= dmg % reduction_percent;
           this._targetPc.send_effect(19695);
         }
       } else {
         skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_HIT_PERCENT;
         reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_HIT_REDUCTION;
         if (MJRnd.isWinning(1000000, skill_percent)) {
           dmg -= dmg % reduction_percent;
           this._targetPc.send_effect(19695);
         }
       }
     }


     if (this._pc.isSpearModeType()) {
       double distance = this._pc.getLocation().getLineDistance(this._targetPc.getLocation());
       if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_1) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_1;
       } else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_2) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_2;
       } else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_3) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_3;
       } else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_4;
       } else if (distance > Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_5;
       }

       dmg += dmg * Config.MagicAdSetting_Lancer.SPEARMODE_PVP_DMG / 100.0D;
     }

     if (this._pc.getHeading() == this._targetPc.getHeading()) {
       dmg += dmg * Config.CharSettings.PVP_BOUNS;
     }












     if (this._targetPc.getTomahawkHunter() == this._pc) {
       int tomahawk_hp = MJRnd.next(Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MIN_HP, Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MAX_HP);

       if (this._targetPc.getCurrentHp() > tomahawk_hp) {
         this._pc.setCurrentHp(this._pc.getCurrentHp() + tomahawk_hp);
         this._pc.send_effect(20600);
         this._targetPc.setCurrentHp(this._targetPc.getCurrentHp() - tomahawk_hp);
       }
     }

     if ((this._weaponType == 20 || this._weaponType == 62 || (this._weaponType == 24 && this._pc.isSpearModeType())) &&
       this._targetPc.getPassive(MJPassiveID.MOEBIUS.toInt()) != null) {
       int reduc = 0;
       if (this._target.getLevel() >= 85) {
         reduc = (this._target.getLevel() - 85) / 2 + 9;
         if (reduc >= 15) {
           reduc = 15;
         }
       }
       dmg *= (100 - reduc) / 100.0D;
     }


     if (this._targetPc.get_reduction_per() > 0) {
       int reduction_per = this._target.get_reduction_per();
       dmg -= dmg * reduction_per / 100.0D;
     }
     if (this._targetPc.get_pvp_defense_per() > 0) {
       dmg *= (100 - this._targetPc.get_pvp_defense_per()) / 100.0D;
     }


     if (this._targetPc.hasSkillEffect(112)) {
       if (this._targetPc.get_Armor_break_Attacker() == this._pc.getId()) {
         dmg *= 1.5D;
       } else {
         dmg *= 1.2D;
       }
     }






     if (this._targetPc.hasSkillEffect(7059) &&
       this._weaponType != 20 && this._weaponType != 62) {
       dmg *= 1.5D;
     }



     dmg = Math.max(dmg - total_reduction, 0.0D);


     if (dmg > 0.0D && this._targetPc.hasSkillEffect(68) &&
       this._pc != null) {
       if (!this._pc.isWizard()) {
         dmg -= dmg * this._targetPc.getImmuneReduction() * this._pc.get_immune_ignore() / 100.0D;



       }
       else {



         dmg *= 0.5D;
       }
     }




     if (dmg > 0.0D && this._targetPc.hasSkillEffect(5047)) {
       dmg *= 0.95D;
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
         dmg *= (100 - reduction) / 100.0D;
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


   L1DollInstance targetdoll = this._targetPc.getMagicDoll(); // 獲取目標玩家的魔法娃娃實例
   if (targetdoll != null) { // 如果目標娃娃不為空
     try {
       L1ItemInstance doll_item = this._targetPc.getInventory().getItem(targetdoll.getItemObjId()); // 從目標玩家的背包中獲取娃娃物品實例
       if (doll_item != null &&
               doll_item.get_Doll_Bonus_Value() == 158) { // 如果娃娃物品實例不為空且娃娃的獎勵值為158
         dmg *= 0.95D; // 將傷害乘以0.95
       }
     }
     catch (Exception e) { // 如果發生異常
       L1ItemInstance doll_item = this._targetPc.getInventory().getItem(targetdoll.getItemObjId()); // 再次從目標玩家的背包中獲取娃娃物品實例
       int Absorption_point = 0; // 定義吸收點變量
       System.out.println(String.format(
               [00:54]
       "[PC->PC(潛在效果錯誤) : (物品Id/Obj: %s / %s) (潛在值: %d) (吸收點 : %d)]",
               new Object[] {
                       doll_item.getName(), // 輸出娃娃物品名稱
                       Integer.valueOf(targetdoll.getItemObjId()), // 輸出娃娃物品對象ID
                       Integer.valueOf(doll_item.get_Doll_Bonus_Value()), // 輸出娃娃的獎勵值
                       Integer.valueOf(Absorption_point) // 輸出吸收點
               }
        ));
     }
   }

     if (dmg <= 0.0D) {
       this._isHit = false;
     }
     return (int)dmg;
   }




   private int calcPcNpcDamage() {
     if (this._targetNpc == null || this._pc == null) {
       this._isHit = false;
       return 0;
     }
     int weaponMaxDamage = 0;

     if (this._targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") && this._weaponSmall > 0) {
       weaponMaxDamage = this._weaponSmall;
     } else if (this._targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") && this._weaponLarge > 0) {
       weaponMaxDamage = this._weaponLarge;
     } else if (this._targetNpc instanceof l1j.server.server.model.Instance.L1PeopleInstance) {
       weaponMaxDamage = this._weaponSmall;
     }

     int weaponDamage = 0;

     boolean secondWeapon = false;
     if (this._weaponType == 0) {
       weaponDamage = 0;
     } else if (this._weaponType2 == 17) {
       weaponDamage = weaponMaxDamage + this._weaponAddDmg;
     } else if (this._pc.is전사() && this._pc.isPassive(MJPassiveID.SLAYER.toInt()) && this._pc.getSecondWeapon() != null) {
       int ran = _random.nextInt(100);
       if (ran < 50) {
         secondWeapon = true;
         if (this._targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") && this._SweaponSmall > 0) {
           weaponDamage = this._SweaponSmall + this._SweaponAddDmg;
         } else if (this._targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") && this._SweaponLarge > 0) {
           weaponDamage = this._SweaponLarge + this._SweaponAddDmg;
         }
       } else {
         weaponDamage = weaponMaxDamage + this._weaponAddDmg;
       }
     } else {
       weaponDamage = weaponMaxDamage + this._weaponAddDmg;
     }

     if (weaponDamage <= 0) {
       weaponDamage = 1;
     }
     int weaponTotalDamage = 1;

     if (this.weapon != null) {
       weaponTotalDamage = weaponDamage + ((this.weapon == null) ? 1 : getEnchantDmg(secondWeapon ? this.Sweapon : this.weapon)) + this._weapon_bless_level;
     }


     weaponTotalDamage += calcMaterialBlessDmg();

     if (this._weaponType == 54 && _random.nextInt(Config.MagicAdSetting_DarkElf.DOUBLEPCNPCCHANCE) + 1 <= this._weaponDoubleDmgChance - this.weapon
       .get_durability() && this._pc.isDarkelf()) {
       weaponTotalDamage = (int)(weaponTotalDamage * Config.MagicAdSetting_DarkElf.DOUBLEDMG);
       this._pc.sendPackets((ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, 54, (this.Sweapon != null)));
       Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, 54, (this.Sweapon != null)));
       this._isCritical = true;
     }

     double dmg = (weaponTotalDamage + this._statusDamage);

     if (this._weaponType2 == 17) {
       dmg = MJCommons.getKeylinkPcNpcDamage((L1Character)this._pc, (L1Character)this._targetNpc, weaponTotalDamage);
     } else if (this._weaponType != 20 && this._weaponType != 62) {
       dmg += (this._pc.getDmgup() + this._pc.getDmgRate());
     } else {
       dmg += (this._pc.getBowDmgup() + this._pc.getBowDmgRate());
     }

     dmg += 몬스터속성인첸트효과();

     if (this._weaponType == 20) {
       if (this._arrow != null) {
         dmg += this._arrow.getItem().getDmgModifier();
       } else {
         dmg += (MJRnd.next(2) + 1);
       }
       if (this._targetNpc.getNpcTemplate().is_hard()) {
         dmg /= 1.5D;
       }
     } else if (this._weaponType == 62) {
       int add_dmg = 0;
       if (this._targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large")) {
         add_dmg = this._sting.getItem().getDmgLarge();
       } else if (this._targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small")) {
         add_dmg = this._sting.getItem().getDmgSmall();
       }
       if (add_dmg == 0) {
         add_dmg = 1;
       }
       dmg = dmg + _random.nextInt(add_dmg) + 1.0D;
     }


     if (this._weaponType != 0) {
       switch (this.weapon.get_item_level()) {
         case 1:
           WeaponLevelAttack(this._pc, this._target, 3740, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
         case 2:
           WeaponLevelAttack(this._pc, this._target, 16018, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
         case 3:
           WeaponLevelAttack(this._pc, this._target, 16024, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
         case 4:
           WeaponLevelAttack(this._pc, this._target, 4167, this._pc.getWeapon().getEnchantLevel(), this.weapon.get_item_level());
           break;
       }




     }
     MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getAtk(this._weaponId);
     if (model != null && this.weapon.get_item_level() == 0) {
       dmg += model.get((L1Character)this._pc, (L1Character)this._targetNpc, this.weapon, dmg);
     }

     if (this._pc.getEquipSlot() != null &&
       this._pc.getEquipSlot().getArmors() != null) {
       for (L1ItemInstance item : this._pc.getEquipSlot().getArmors()) {
         model = MJItemSkillModelLoader.getInstance().getAtk(item.getItemId());
         if (model != null) {
           dmg += model.get((L1Character)this._pc, (L1Character)this._targetNpc, item, dmg);
         }
       }
     }

     if (this._weaponType == 0) {
       dmg = ((_random.nextInt(5) + 4) / 4);
     }

     if (this._pc.hasSkillEffect(182) &&
       this._weaponType != 20 && this._weaponType != 62) {
       dmg += 20.0D;
       this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 6591));
       this._pc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 6591));
       this._pc.removeSkillEffect(182);
     }


     if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 14) {
       dmg += L1MagicDoll.getDamageAddByDoll((L1Character)this._pc);
     }

     if (this._pc.getMapId() == 750)
     {
       if (this._targetNpc.getNpcId() == 73201274 || this._targetNpc.getNpcId() == 73201275 || this._targetNpc
         .getNpcId() == 73201276 || this._targetNpc.getNpcId() == 73201277 || this._targetNpc
         .getNpcId() == 73201278 || this._targetNpc.getNpcId() == 73201279) {
         dmg += Config.ServerAdSetting.InfinityBattle_boss_dmg;
       } else {

         dmg += Config.ServerAdSetting.InfinityBattle_dmg;
       }
     }

     L1DollInstance doll = this._pc.getMagicDoll();
     if (doll != null) {
       try {
         L1ItemInstance doll_item = this._pc.getInventory().getItem(doll.getItemObjId());
         int chance = MJRnd.next(100);
         int Absorption_point = 0;
         if (doll_item != null) {
           if (doll_item.get_Doll_Bonus_Value() == 130) {
             if (chance <= Config.MagicDollInfo.MP_SMALL_CHANCE_NPC) {
               Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_SMALL_MIN_NPC, Config.MagicDollInfo.MP_SMALL_MAX_NPC);

               if (this._targetNpc.getCurrentMp() > Absorption_point) {
                 this._pc.setCurrentMp(this._pc.getCurrentMp() + Absorption_point);
                 this._targetNpc.setCurrentMp(this._targetNpc.getCurrentMp() - Absorption_point);
               }
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 137 || doll_item.get_Doll_Bonus_Value() == 155) {
             if (chance <= Config.MagicDollInfo.HP_LARGE_CHANCE_NPC) {
               Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_LARGE_MIN_NPC, Config.MagicDollInfo.HP_LARGE_MAX_NPC);

               if (this._targetNpc.getCurrentHp() > Absorption_point) {
                 this._pc.setCurrentHp(this._pc.getCurrentHp() + Absorption_point);
                 this._targetNpc.setCurrentHp(this._targetNpc.getCurrentHp() - Absorption_point);
               }
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 138 || doll_item.get_Doll_Bonus_Value() == 156) {
             if (chance <= Config.MagicDollInfo.MP_LARGE_CHANCE_NPC) {
               Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_LARGE_MIN_NPC, Config.MagicDollInfo.MP_LARGE_MAX_NPC);

               if (this._targetNpc.getCurrentMp() > Absorption_point) {
                 this._pc.setCurrentMp(this._pc.getCurrentMp() + Absorption_point);
                 this._targetNpc.setCurrentMp(this._targetNpc.getCurrentMp() - Absorption_point);
               }
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 139) {
             Absorption_point = MJRnd.next(100);
             if (Absorption_point < Config.MagicDollInfo.DOLL_SOULOFFLAME) {
               if (this._pc.hasSkillEffect(175))
                 this._pc.removeSkillEffect(175);
               this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._pc.getId(), 19264, 19));
               this._pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 19264, 8));
               Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_SkillSound(this._pc.getId(), 19264));
               this._pc.setSkillEffect(175, 8000L);
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 140) {
             Absorption_point = MJRnd.next(100);
             if (Absorption_point < Config.MagicDollInfo.DOLL_JUDGEMENT) {
               if (this._targetNpc.hasSkillEffect(5001)) {
                 this._targetNpc.removeSkillEffect(5001);
               }
               this._targetNpc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 18490));
               this._targetNpc.setSkillEffect(5001, 8000L);
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 141) {
             Absorption_point = MJRnd.next(100);
             if (Absorption_point < Config.MagicDollInfo.DOLL_DECAY_POTION) {
               if (this._targetNpc.hasSkillEffect(71)) {
                 this._targetNpc.removeSkillEffect(71);
               }
               this._targetNpc.setSkillEffect(71, 4000L);
               this._targetNpc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 2232));
               Broadcaster.broadcastPacket((L1Character)this._targetNpc, (ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 2232));
             }
           } else if (doll_item.get_Doll_Bonus_Value() == 147) {
             if (chance <= Config.MagicDollInfo.MP_SMALL_CHANCE_NPC) {
               Absorption_point = MJRnd.next(Config.MagicDollInfo.MP_SMALL_MIN_NPC, Config.MagicDollInfo.MP_SMALL_MAX_NPC);

               if (this._targetNpc.getCurrentMp() > Absorption_point) {
                 this._pc.setCurrentMp(this._pc.getCurrentMp() + Absorption_point);
                 this._targetNpc.setCurrentMp(this._targetNpc.getCurrentMp() - Absorption_point);
               }
             }
           } else if ((doll_item.get_Doll_Bonus_Value() == 152 || doll_item.get_Doll_Bonus_Value() == 153 || doll_item
             .get_Doll_Bonus_Value() == 154) &&
             chance <= Config.MagicDollInfo.HP_SMALL_CHANCE_NPC) {
             Absorption_point = MJRnd.next(Config.MagicDollInfo.HP_SMALL_MIN_NPC, Config.MagicDollInfo.HP_SMALL_MAX_NPC);

             if (this._targetNpc.getCurrentHp() > Absorption_point) { // 如果目標NPC的當前HP大於吸收點
               this._pc.setCurrentHp(this._pc.getCurrentHp() + Absorption_point); // 增加玩家的當前HP
               this._targetNpc.setCurrentHp(this._targetNpc.getCurrentHp() - Absorption_point); // 減少目標NPC的當前HP
             }

           }

         }
       } catch (Exception e) { // 捕捉異常
         L1ItemInstance doll_item = this._pc.getInventory().getItem(doll.getItemObjId()); // 從玩家的背包中獲取娃娃物品實例
         int Absorption_point = 0; // 定義吸收點變量
         System.out.println(String.format(
                 "[PC->NPC(潛在效果錯誤) : (物品Id/Obj: %s / %s) (潛在值: %d) (吸收點: %d)]",
                 new Object[] {
                         doll_item.getName(), // 輸出娃娃物品名稱
                         Integer.valueOf(doll.getItemObjId()), // 輸出娃娃物品對象ID
                         Integer.valueOf(doll_item.get_Doll_Bonus_Value()), // 輸出娃娃的獎勵值
                         Integer.valueOf(Absorption_point) // 輸出吸收點
                 }
         ));
       }

     dmg += L1MagicDoll.useSkillByDoll((L1Character)this._pc, (L1Character)this._targetNpc);

     if (this._pc != null && this._pc.isPassive(MJPassiveID.RAMPAGE.toInt()) &&
       this._target.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL) &&
       MJRnd.isWinning(100, Config.MagicAdSetting_DragonKnight.RAMPAGE_P)) {
       dmg *= Config.MagicAdSetting_DragonKnight.RAMPAGE_D;
     }





     if (this._pc.getPassive(MJPassiveID.CRASH.toInt()) != null) {
       int chance = _random.nextInt(100) + 1;
       if (Config.MagicAdSetting_Warrior.CRASHFO >= chance) {
         int crashdmg = (int)(2.0D + this._pc.getLevel() * Config.MagicAdSetting_Warrior.CRASHDMG);
         int furydmg = 0;

         if (this._pc.getPassive(MJPassiveID.FURY.toInt()) != null) {
           chance = _random.nextInt(100) + 1;
           if (Config.MagicAdSetting_Warrior.FURYFO >= chance) {
             furydmg = (int)(furydmg + crashdmg * Config.MagicAdSetting_Warrior.FURYDMG);

             this._targetNpc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 12489));
           }
         }
         dmg += (crashdmg + furydmg);

         this._targetNpc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 12487));
       }
     }

     if (this._weaponType2 == 18) {
       L1WeaponSkill.체인소드(this._pc, this._target);
     }
     if (this._pc.FouSlayer) {
       dmg *= Config.MagicAdSetting_DragonKnight.FouSlayer_dmg_npc;

       if (this._pc.hasSkillEffect(51002)) {
         dmg += 3.0D;




         fouslayer_brave(this._pc, (L1Character)this._targetNpc);
       }

       dmg += this._pc.getFouDmg();
     }

     if (this._pc.hasSkillEffect(220)) {
       dmg += 10.0D;
     }

     MJArmorClass armor_class = MJArmorClass.find_armor_class(this._targetNpc.getAC().getAc());
     if (armor_class != null) {
       if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {
         dmg -= armor_class.get_to_npc_reduction();
       } else {
         dmg -= armor_class.get_to_npc_long_reduction();
       }
     }

     if (this._pc.TripleArrow) {
       dmg *= Config.MagicAdSetting_Elf.TripleArrow_dmg_npc;
       if (this._pc.isPassive(MJPassiveID.TRIPLE_BOOST.toInt())) {
         dmg *= 1.0D + Config.MagicAdSetting_Elf.TripleArrow_boost_dmg_npc;
       }
     }

     try {
       dmg += CharacterBalance.getInstance().getDmg(this._pc.getType(), 10);
       dmg *= CharacterBalance.getInstance().getDmgRate(this._pc.getType(), 10);
     } catch (Exception e) {
       System.out.println("Character NpcAdd Damege Error");
     }

     L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(this._pc.getMapId());

     if (sm != null && this._pc.getWeapon() != null && this._pc.getWeapon().getItem().getType() != 7) {
       dmg -= sm.getDmgReduction();
       if (dmg <= 0.0D) {
         dmg = 1.0D;
       }
     }
     dmg -= calcNpcDamageReduction();

     boolean isNowWar = false;
     int castleId = L1CastleLocation.getCastleIdByArea((L1Character)this._targetNpc);
     if (castleId > 0) {
       isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
     }

     if (!isNowWar) {
       if (this._targetNpc instanceof L1PetInstance) {
         dmg /= 8.0D;
       }

       if (this._targetNpc instanceof L1SummonInstance) {
         L1SummonInstance summon = (L1SummonInstance)this._targetNpc;
         if (summon.isExsistMaster()) {
           dmg /= 5.0D;
         }
       }
     }

     if (this._targetNpc.hasSkillEffect(70705)) {
       dmg = 0.0D;
     }
     if (this._targetNpc.hasSkillEffect(157)) {
       dmg = 0.0D;
     }
     if (this._targetNpc.hasSkillEffect(212)) {
       this._targetNpc.removeSkillEffect(212);
     }


     int chance41 = _random.nextInt(100) + 1;
     if (this._weaponType != 0 && this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17 && (
       this._pc.hasSkillEffect(171) || this._pc.hasSkillEffect(152) || this._pc
       .hasSkillEffect(117)) &&
       chance41 <= Config.MagicAdSetting_Elf.COMBINECHANCE) {
       dmg *= 1.5D;
       this._targetNpc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 7727));
     }




     if (this._pc.hasSkillEffect(105) && (this._weaponType == 54 || this._weaponType == 58)) {
       int rate = 0;
       int ratetemp = 0;
       double doubledmg = 0.0D;
       if (this._pc.getLevel() >= 90) {
         ratetemp = (this._pc.getLevel() - 88) / 2;
         rate += ratetemp * 2;
       }

       if (this._pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
         int lvl = this._pc.getLevel();
         if (lvl >= 80)
           rate += this._pc.getLevel() - 79;
       }
       rate = (int)(rate + Config.MagicAdSetting_DarkElf.DOUBLEBREAKCHANCE);

       if (this._pc.hasSkillEffect(105) &&
         _random.nextInt(100) + 1 <= rate) {








         this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 6532));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 6532));
         doubledmg += Config.MagicAdSetting_DarkElf.DOUBLEBRAKEDMGNPC;
       }


       if (doubledmg <= 0.0D) {
         doubledmg = 1.0D;
       }

       dmg *= doubledmg;
     }
     if (this._pc.isPassive(MJPassiveID.BURNING_SPIRIT_PASSIVE.toInt()) && (this._weaponType == 54 || this._weaponType == 58)) {
       int burningrate = 0;
       double doubledmg = 0.0D;
       if (this._pc.getLevel() >= 45) {
         burningrate += this._pc.getLevel() - 45;
       }
       if (burningrate >= 10) {
         burningrate = 10;
       }
       burningrate = (int)(burningrate + Config.MagicAdSetting_DarkElf.BURNINGSPIRITCHANCE);

       if (this._pc.isPassive(MJPassiveID.BURNING_SPIRIT_PASSIVE.toInt()) &&
         _random.nextInt(100) + 1 <= burningrate) {
         this._targetNpc.send_effect(7727);
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 7727));
         doubledmg += Config.MagicAdSetting_DarkElf.BURNINGSPIRITNPC;
       }

       if (doubledmg <= 0.0D) {
         doubledmg = 1.0D;
       }
       dmg *= doubledmg;
     }

     if (this._weaponType != 0 && this._weaponType != 20 && this._weaponType != 62 &&
       this._pc.hasSkillEffect(94) &&
       this._pc.getEquipSlot().isWeapon(this.weapon)) {

       int probability = SkillsTable.getInstance().getTemplate(94).getProbabilityValue();
       if (this._pc.getLevel() >= 75)
         probability += (this._pc.getLevel() - 75) * 1;
       if (MJRnd.isWinning(100, probability)) {
         this._targetNpc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._targetNpc.getId(), 17223));
         dmg *= Config.MagicAdSetting_Knight.BLOWATTACKDMG;
       }
     }



     if ((this._weaponType == 20 || this._weaponType == 62) &&
       this._pc.hasSkillEffect(167)) {
       int probability = SkillsTable.getInstance().getTemplate(167).getProbabilityValue();
       if (this._pc.getLevel() >= 85)
         probability += (this._pc.getLevel() - 85) * 1;
       if (MJRnd.isWinning(100, probability)) {
         dmg *= Config.MagicAdSetting_Elf.CYCLONEVAL;
         this._targetNpc.send_effect(17557);
       }
     }


     if (this._pc != null && this._pc.isPassive(MJPassiveID.FLAME.toInt()) &&
       this._pc.getEquipSlot().isWeapon(this.weapon)) {
       int percent = Config.MagicAdSetting_Fencer.FLAME_PASSIVE;
       if (MJRnd.isWinning(1000000, percent)) {
         L1FlameDamage.doInfection((L1Character)this._pc, (L1Character)this._targetNpc, 1000, this._pc
             .getLevel() * 2 / Config.MagicAdSetting_Fencer.FLAME_PASSIVE_DMG);
       }
     }


     if (this._pc != null && this._pc.isPassive(MJPassiveID.RAGE.toInt()) &&
       this._pc.getEquipSlot().isWeapon(this.weapon)) {
       int percent = Config.MagicAdSetting_Fencer.RAGEPROBABILITY;
       if (MJRnd.isWinning(1000000, percent)) {
         this._targetNpc.send_effect(18517);
         dmg *= Config.MagicAdSetting_Fencer.RAGEDMG;
       }
     }


     if (this._pc != null && this._pc.isPassive(MJPassiveID.DEADLY_STRIKE.toInt())) {
       int percent = Config.MagicAdSetting_Lancer.DEADLY_STRIKE_PRO * 10000;
       if (MJRnd.isWinning(1000000, percent)) {
         dmg *= Config.MagicAdSetting_Lancer.DEADLY_STRIKE;
         this._targetNpc.send_effect(19367);
       }
     }

     if (this._pc.isSpearModeType() &&
       this._pc != null && this._pc.isPassive(MJPassiveID.INCREASE_RANGE.toInt())) {
       int pclevel = this._pc.getLevel();
       int rate = 0;
       int ratedmg = 0;
       if (pclevel < 90) {
         pclevel = 90;
       }
       if (pclevel >= 90) {
         rate = (pclevel - 90) / 2;
         ratedmg = rate * 2;
       }
       if (ratedmg >= 10) {
         ratedmg = 10;
       }
       dmg += dmg * (ratedmg + 10) / 100.0D;
     }



     if (this._pc != null && this._pc.hasSkillEffect(5158)) {
       dmg += dmg * 0.3D;
     }

     boolean isCounterBarrier = false;
     L1Magic magic = null;
     if (this._targetNpc.hasSkillEffect(7060) && dmg > 0.0D) {
       magic = new L1Magic((L1Character)this._targetNpc, (L1Character)this._pc);
       boolean isProbability = magic.calcProbabilityMagic(91);
       if (isProbability) {
         isCounterBarrier = true;
       }
     }
     if (isCounterBarrier) {
       NpcactionCounterBarrier(this._target, this._pc);
       commitBossCounterBarrier(this._target, this._pc);
     }

     if (this._pc.isSpearModeType()) {
       double distance = this._pc.getLocation().getLineDistance(this._targetNpc.getLocation());
       if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_1) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_1;
       } else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_2) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_2;
       } else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_3) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_3;
       } else if (distance <= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_4;
       } else if (distance > Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_4) {
         dmg *= Config.MagicAdSetting_Lancer.SPEARMODE_DISTANCE_DMG_5;
       }
     }

     if (dmg <= 0.0D) {
       this._isHit = false;
     }

     if (this._targetNpc.hasSkillEffect(112)) {
       if (this._targetNpc.get_Armor_break_Attacker() == this._pc.getId()) {
         dmg *= 1.5D;
       } else {
         dmg *= 1.2D;
       }
     }
     if (this._targetNpc.getTomahawkHunter() == this._pc) {
       int tomahawk_hp = MJRnd.next(Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MIN_HP, Config.MagicAdSetting_Warrior.TOMAHAWK_HUNTER_MAX_HP);

       if (this._targetNpc.getCurrentHp() > tomahawk_hp) {
         this._pc.setCurrentHp(this._pc.getCurrentHp() + tomahawk_hp);
         this._pc.send_effect(20600);
         this._targetNpc.setCurrentHp(this._targetNpc.getCurrentHp() - tomahawk_hp);
       }
     }

     return (int)dmg;
   }




   private int calcNpcPcDamage() {
     if (this._npc == null || this._targetPc == null) {
       return 0;
     }
     int lvl = this._npc.getLevel();
     double dmg = 0.0D;
     double status = 0.0D;
     int level = Math.max(this._npc.getLevel(), 2);
     if (this._npc.getNpcTemplate().getBowActId() > 0)
     { NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.LONG_DMG, level);
       if (eInfo != null) {
         status = this._npc.getAbility().getTotalDex() * eInfo.get_increase_dmg();
       } else {
         status = this._npc.getAbility().getTotalDex();
       }  }
     else { NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.SHORT_DMG, level);
       if (eInfo != null) {
         status = this._npc.getAbility().getTotalStr() * eInfo.get_increase_dmg();
       } else {
         status = this._npc.getAbility().getTotalStr();
       }  }

     if (status <= 0.0D) {
       status = 1.0D;
     }
     if (this._npc instanceof L1PetInstance) {
       dmg += (lvl / 15);
       dmg += ((L1PetInstance)this._npc).getDamageByWeapon();
     }
     dmg += this._npc.getDmgup() + status - status / 2.0D + _random.nextInt((int)status) + 1.0D;

     if (isUndeadDamage()) {
       dmg *= 1.2D;
     }

     L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(this._npc.getMapId());
     if (sm != null) {
       dmg *= sm.getDmgRate();
     }


     dmg = dmg * getLeverage() / 15.0D;

     if (this._npc.isWeaponBreaked()) {
       dmg *= 0.5D;
     }

     if (this._targetPc.hasSkillEffect(3000130))
       dmg -= 5.0D;
     if (this._targetPc.hasSkillEffect(3000129))
       dmg -= 5.0D;
     if (this._targetPc.hasSkillEffect(3000128)) {
       dmg -= 3.0D;
     }
     try {
       dmg += CharacterBalance.getInstance().getDmg(10, this._targetPc.getType());

       dmg *= CharacterBalance.getInstance().getDmgRate(10, this._targetPc.getType());
     } catch (Exception e) {
       System.out.println("Character NpcAdd Reduction Error");
     }


     boolean isNowWar = false;
     int castleId = L1CastleLocation.getCastleIdByArea((L1Character)this._targetPc);
     if (castleId > 0) {
       isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
     }

     if (!isNowWar) {
       if (this._npc instanceof L1PetInstance) {
         dmg /= 8.0D;
       }

       if (this._npc instanceof L1SummonInstance) {
         L1SummonInstance summon = (L1SummonInstance)this._npc;
         if (summon.isExsistMaster()) {
           dmg /= 5.0D;
         }
       }
     }

     addNpcPoisonAttack((L1Character)this._npc, (L1Character)this._targetPc);

     if ((this._npc instanceof L1PetInstance || this._npc instanceof L1SummonInstance || this._npc instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) &&
       this._targetPc.getZoneType() == 1) {
       this._isHit = false;
     }



     if (this._targetPc.hasSkillEffect(7059) &&
       this._weaponType != 20 && this._weaponType != 62) {
       dmg *= 1.58D;
     }



     dmg = toPcBuffDmg(dmg);











     MJArmorClass armor_class = MJArmorClass.find_armor_class(this._targetPc.getAC().getAc());
     if (armor_class != null) {
       if (this._weaponType != 20 && this._weaponType != 62 && this._weaponType2 != 17) {
         dmg -= armor_class.get_to_pc_reduction();
       } else {
         dmg -= armor_class.get_to_pc_long_reduction();
       }
     }
     dmg -= L1MagicDoll.getDamageReductionByDoll((L1Character)this._npc, (L1Character)this._targetPc);

     double total_reduction = 0.0D;
     total_reduction += getReductionIgnore(this._targetPc.getDamageReductionByArmor() + this._targetPc.getDamageReduction(), 0, (L1Character)this._npc);

     if (this._targetPc.hasSkillEffect(136) && this._npc.getNpcTemplate().get_ranged() <= 1) {
       L1ItemInstance target_weapon = this._targetPc.getWeapon();
       if (target_weapon != null && target_weapon.getItem().getType() == 1) {
         int probability = SkillsTable.getInstance().getTemplate(136).getProbabilityValue();
         if (MJRnd.isWinning(100, probability)) {
           int weapon_index = MJRnd.next(4);
           this._targetPc.send_effect(Config.MagicAdSetting_Elf.INFERNOEFFECTS[weapon_index]);

           int inferno_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel() + this._targetPc.getDmgRate() + this._targetPc.getDmgup() + this._targetPc.getDmgupByArmor();
           this._npc.receiveDamage((L1Character)this._targetPc, (weapon_index + 1) * inferno_damage);
           this._npc.send_action(2);
         }
       }
     }

     if (!this._targetPc.hasSkillEffect(87) && !this._targetPc.hasSkillEffect(123) && this._targetPc
       .hasSkillEffect(5056) && !this._targetPc.hasSkillEffect(208) &&
       this._targetPc.hasSkillEffect(245) && this._npc.getNpcTemplate().get_ranged() <= 1) {
       L1ItemInstance target_weapon = this._targetPc.getWeapon();
       if (target_weapon != null && target_weapon.getItem().getType() == 18) {
         int probability = SkillsTable.getInstance().getTemplate(245).getProbabilityValue();
         if (MJRnd.isWinning(100, probability)) {
           dmg = 0.0D;
           this._targetPc.send_effect(18410);

           int halpas_damage = target_weapon.getItem().getDmgSmall() + target_weapon.getEnchantLevel() + this._targetPc.getDmgRate() + this._targetPc.getDmgup() + this._targetPc.getDmgupByArmor();
           this._npc.receiveDamage((L1Character)this._targetPc, halpas_damage * Config.MagicAdSetting_DragonKnight.HALPASDMGX);
           this._npc.send_action(2);
         }
       }
     }


     if (this._targetPc.hasSkillEffect(158)) {
       int probability = SkillsTable.getInstance().getTemplate(158).getProbabilityValue();
       int bonus = 2;
       if (MJRnd.isWinning(100, probability)) {
         this._targetPc.setCurrentHp((int)(this._targetPc.getCurrentHp() + dmg * bonus));
         this._targetPc.send_effect(18930);
       }
     }




     if ((this._npc.getNpcId() == 14212114 || this._npc.getNpcId() == 14212134 || this._npc.getNpcId() == 14212120 || this._npc
       .getNpcId() == 14212144 || this._npc.getNpcId() == 7310154 || this._npc.getNpcId() == 7310148 || this._npc
       .getNpcId() == 7310160 || this._npc.getNpcId() == 45684 || this._npc.getNpcId() == 73201240 || this._npc
       .getNpcId() == 73201233) &&
       this._targetPc.hasSkillEffect(2100000)) {
       if (this._targetPc.getMagicDoll() != null) {
         dmg -= dmg * this._targetPc.getMagicDoll().getDoll().getEffect().getDragonDmgDecrease() * 0.01D;
       } else {
         dmg -= dmg * 0.1D;
       }
     }

     if (this._targetPc.hasSkillEffect(STUN_TO_HOLD_TYPE_SKILL) &&
       this._targetPc.isPassive(MJPassiveID.ARTERIAL_CIRCLE.toInt()) &&
       MJRnd.isWinning(100, Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_CHANCE)) {
       dmg -= Config.MagicAdSetting_Wizard.ARTERIALCIRCLE_REDUC;
       this._targetPc.send_effect(20118, true);
     }



     if (this._targetPc.getPassive(MJPassiveID.VENGEANCE.toInt()) != null &&
       this._targetPc != null) {
       int skill_percent = 10000;
       int reduction_percent = 0;

       int HPpercent = (int)Math.round(this._targetPc.getCurrentHp() / this._targetPc.getMaxHp() * 100.0D);
       if (HPpercent < 50) {
         skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_PERCENT;
         reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_REDUCTION;
         if (MJRnd.isWinning(1000000, skill_percent)) {
           dmg -= dmg % reduction_percent;
           this._targetPc.send_effect(19695);
         }
       } else {
         skill_percent *= Config.MagicAdSetting_Lancer.VENGEANCE_HIT_PERCENT;
         reduction_percent = Config.MagicAdSetting_Lancer.VENGEANCE_HIT_REDUCTION;
         if (MJRnd.isWinning(1000000, skill_percent)) {
           dmg -= dmg % reduction_percent;
           this._targetPc.send_effect(19695);
         }
       }
     }


     dmg = Math.max(dmg - total_reduction, 0.0D);

     if ((this._weaponType == 20 || this._weaponType == 62 || (this._weaponType == 24 && this._pc.isSpearModeType())) &&
       this._targetPc.getPassive(MJPassiveID.MOEBIUS.toInt()) != null) {
       int reduc = 0;
       if (this._target.getLevel() >= 85) {
         reduc = (this._target.getLevel() - 85) / 2 + 9;
         if (reduc >= 15) {
           reduc = 15;
         }
       }
       dmg *= (100 - reduc) / 100.0D;
     }


     if (this._targetPc.get_reduction_per() > 0) {
       int reduction_per = this._target.get_reduction_per();
       dmg -= dmg * reduction_per / 100.0D;
     }

     if (dmg > 0.0D && this._targetPc.hasSkillEffect(68) &&
       this._targetPc != null) {
       if (!this._targetPc.isWizard()) {
         dmg -= dmg * this._targetPc.getImmuneReduction();
       } else {
         dmg *= 0.5D;
       }
     }



     L1DollInstance targetdoll = this._targetPc.getMagicDoll(); // 獲取目標玩家的魔法娃娃實例
       if (targetdoll != null) { // 如果目標娃娃不為空
         try {
           L1ItemInstance doll_item = this._targetPc.getInventory().getItem(targetdoll.getItemObjId()); // 從目標玩家的背包中獲取娃娃物品實例
           if (doll_item != null && doll_item.get_Doll_Bonus_Value() == 158) { // 如果娃娃物品實例不為空且娃娃的獎勵值為158
             dmg *= 0.95D; // 將傷害乘以0.95
           }
         } catch (Exception e) { // 如果發生異常
           L1ItemInstance doll_item = this._targetPc.getInventory().getItem(targetdoll.getItemObjId()); // 再次從目標玩家的背包中獲取娃娃物品實例
           int Absorption_point = 0; // 定義吸收點變量
           System.out.println(String.format(
                   "[PC->PC(潛在效果錯誤) : (物品Id/Obj: %s / %s) (潛在值: %d) (吸收點: %d)]",
                   new Object[] {
                           [01:03]
           doll_item.getName(), // 輸出娃娃物品名稱
                   Integer.valueOf(targetdoll.getItemObjId()), // 輸出娃娃物品對象ID
                   Integer.valueOf(doll_item.get_Doll_Bonus_Value()), // 輸出娃娃的獎勵值
                   Integer.valueOf(Absorption_point) // 輸出吸收點
                   }
                   ));
         }
       }
     if (dmg <= 0.0D) {
       this._isHit = false;
     }

     return (int)dmg;
   }




   private int calcNpcNpcDamage() {
     if (this._targetNpc == null || this._npc == null) {
       return 0;
     }
     int lvl = this._npc.getLevel();
     double dmg = 0.0D;
     double status = 0.0D;
     int level = Math.max(this._npc.getLevel(), 2);
     if (this._npc.getNpcTemplate().getBowActId() > 0)
     { NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.LONG_DMG, level);
       if (eInfo != null) {
         status = this._npc.getAbility().getTotalDex() * eInfo.get_increase_dmg();
       } else {
         status = this._npc.getAbility().getTotalDex();
       }  }
     else { NpcStatusDamageInfo eInfo = NpcStatusDamageInfo.find_npc_status_info(NpcStatusDamageType.SHORT_DMG, level);
       if (eInfo != null) {
         status = this._npc.getAbility().getTotalStr() * eInfo.get_increase_dmg();
       } else {
         status = this._npc.getAbility().getTotalStr();
       }  }

     if (status <= 0.0D) {
       status = 1.0D;
     }
     if (this._npc.getNpcId() >= 7320138 && this._npc.getNpcId() <= 7320147 &&
       this._npc.getCurrentHp() < this._npc.getMaxHp()) {
       int adddmg = (this._npc.getMaxHp() - this._npc.getCurrentHp()) / Config.ServerAdSetting.summonhpdmg;
       if (adddmg < 0) {
         adddmg = 1;
       }
       dmg += adddmg;
     }

     if (this._npc instanceof L1PetInstance) {
       dmg = (_random.nextInt(this._npc.getNpcTemplate().get_level()) + this._npc.getAbility().getTotalStr() / 2 + 1);
       dmg += (lvl / 16);
       dmg += ((L1PetInstance)this._npc).getDamageByWeapon();
     } else if (this._npc instanceof L1SummonInstance) {
       if (this._npc.getMaster() != null) {
         dmg += this._npc.getDmgup() + status - status / 2.0D + _random.nextInt((int)status) + 1.0D + this._npc
           .getMaster().getLevel() * Config.MagicAdSetting_Wizard.SUMMON_LEVEL_ADDDMG + this._npc
           .getMaster().getAbility().getSp() * Config.MagicAdSetting_Wizard.SUMMON_SP_ADDDMG;
       } else {
         dmg += this._npc.getDmgup() + status - status / 2.0D + _random.nextInt((int)status) + 1.0D;
       }
     } else {
       dmg += this._npc.getDmgup() + status - status / 2.0D + _random.nextInt((int)status) + 1.0D;
     }


     if (isUndeadDamage()) {
       dmg *= 1.2D;
     }

     dmg = dmg * getLeverage() / 10.0D;

     dmg += CharacterBalance.getInstance().getDmg(10, 10);

     dmg *= CharacterBalance.getInstance().getDmgRate(10, 10);

     dmg -= calcNpcDamageReduction();

     if (this._npc.isWeaponBreaked()) {
       dmg /= 2.0D;
     }

     addNpcPoisonAttack((L1Character)this._npc, (L1Character)this._targetNpc);

     if (this._targetNpc.hasSkillEffect(70705)) {
       dmg = 0.0D;
     }
     if (this._targetNpc.hasSkillEffect(157)) {
       dmg = 0.0D;
     }

     if (dmg <= 0.0D) {
       this._isHit = false;
     }

     return (int)dmg;
   }



   private double 피시속성인첸트효과() {
     int Attr = this._weaponAttrLevel;
     double AttrDmg = 0.0D;

     switch (this._weaponAttrLevel)
     { case 1:
       case 2:
       case 3:
       case 4:
       case 5:
         AttrDmg += (Attr - 1 + 1);
         if (this._arrow != null && this._arrow.getItemId() == 3000516) {
           AttrDmg += 3.0D;
         }
         AttrDmg -= AttrDmg * this._targetPc.getResistance().getFire() / 100.0D;





         return AttrDmg;case 6: case 7: case 8: case 9: case 10: AttrDmg += (Attr - 6 + 1); if (this._arrow != null && this._arrow.getItemId() == 3000516) AttrDmg += 3.0D;  AttrDmg -= AttrDmg * this._targetPc.getResistance().getWater() / 100.0D; return AttrDmg;case 11: case 12: case 13: case 14: case 15: AttrDmg += (Attr - 11 + 1); if (this._arrow != null && this._arrow.getItemId() == 3000516) AttrDmg += 3.0D;  AttrDmg -= AttrDmg * this._targetPc.getResistance().getWind() / 100.0D; return AttrDmg;case 16: case 17: case 18: case 19: case 20: AttrDmg += (Attr - 16 + 1); if (this._arrow != null && this._arrow.getItemId() == 3000516) AttrDmg += 3.0D;  AttrDmg -= AttrDmg * this._targetPc.getResistance().getEarth() / 100.0D; return AttrDmg; }  AttrDmg = 0.0D; return AttrDmg;
   }


   private int 몬스터속성인첸트효과() {
     int AttrDmg = 0;
     int Attr = this._weaponAttrLevel;
     int NpcWeakAttr = this._targetNpc.getNpcTemplate().get_weakAttr();
     switch (NpcWeakAttr)
     { case 1:
         if (Attr >= 15 && Attr <= 20) {
           AttrDmg += 1 + Attr - 15;
           if (this._arrow != null && this._arrow.getItemId() == 3000516) {
             AttrDmg += 3;
           }
         }







         return AttrDmg;case 2: if (Attr >= 6 && Attr <= 10) { AttrDmg += 1 + Attr - 6; if (this._arrow != null && this._arrow.getItemId() == 3000516) AttrDmg += 3;  }  return AttrDmg;case 4: if (Attr >= 1 && Attr <= 5) { AttrDmg += 1 + Attr - 1; if (this._arrow != null && this._arrow.getItemId() == 3000516) AttrDmg += 3;  }  return AttrDmg;case 8: if (Attr >= 11 && Attr <= 15) { AttrDmg += 1 + Attr - 11; if (this._arrow != null && this._arrow.getItemId() == 3000516) AttrDmg += 3;  }  return AttrDmg; }  AttrDmg = 0; return AttrDmg;
   }


   private int calcNpcDamageReduction() {
     return this._targetNpc.getNpcTemplate().get_damagereduction();
   }


   private int calcMaterialBlessDmg() {
     int damage = 0;
     int undead = this._targetNpc.getNpcTemplate().get_undead();
     if ((this._weaponMaterial == 14 || this._weaponMaterial == 17 || this._weaponMaterial == 22) && (undead == 1 || undead == 3)) {
       damage += _random.nextInt(20) + 1;
     }
     if (this._weaponBless == 0 && (undead == 1 || undead == 2 || undead == 3)) {
       damage += _random.nextInt(4) + 1;
     }
     if (this.weapon != null && this._weaponType != 20 && this._weaponType != 62 && this.weapon.getHolyDmgByMagic() != 0 && (undead == 1 || undead == 3))
     {
       damage += this.weapon.getHolyDmgByMagic();
     }
     return damage;
   }


   private boolean isUndeadDamage() {
     boolean flag = false;
     int undead = this._npc.getNpcTemplate().get_undead();
     boolean isNight = GameTimeClock.getInstance().getGameTime().isNight();
     if (isNight && (undead == 1 || undead == 3)) {
       flag = true;
     }
     return flag;
   }










     private void addNpcPoisonAttack(L1Character attacker, L1Character target) {
       // 如果NPC的毒攻擊類型不是「None(없음)」，且毒攻擊機率達標
       if (!this._npc.getNpcTemplate().get_poisonatk().equalsIgnoreCase("None(一般)") &&
               this._npc.getNpcTemplate().get_poisonatkchance() >= _random.nextInt(100) + 1) {

         // 根據毒攻擊類型進行相應的處理
         switch (this._npc.getNpcTemplate().get_poisonatk()) {
           case "Poison(一般毒)": // 一般毒
             L1DamagePoison.doInfection(attacker, target,
                     this._npc.getNpcTemplate().get_poisonatkms(), // 毒持續時間
                     this._npc.getNpcTemplate().get_poisonatkdmg(), // 毒傷害
                     false);
             break;

           case "Poison(沉默毒)": // 沉默毒
             L1SilencePoison.doInfection(target,
                     this._npc.getNpcTemplate().get_poisonatkSilencems()); // 沉默毒持續時間
             break;

           case "Poison(麻痹毒)": // 麻痹毒
             L1ParalysisPoison.doInfection(target, 9999); // 麻痹毒持續時間9999
             break;
         }
       }
     }



   public void addPcPoisonAttack(L1Character attacker, L1Character target) {
     int chance = _random.nextInt(100) + 1;
     if ((this._weaponId == 13 || this._weaponId == 44 || (this._weaponId != 0 && this._pc.hasSkillEffect(98))) && chance <= Config.MagicAdSetting.PoisonAttack_PC_Chance)
     {
       L1DamagePoison.doInfection(attacker, target, Config.MagicAdSetting.PoisonAttack_PC_Ms, Config.MagicAdSetting.PoisonAttack_PC_DMG, false);
     }
   }




   public void action() {
     try {
       if (this._calcType == 1 || this._calcType == 2) {
         actionPc();
       } else if (this._calcType == 3 || this._calcType == 4) {
         actionNpc();
       }
     } catch (Exception exception) {}
   }



   private void actionPc() {
     int spriteId = this._pc.getCurrentSpriteId();
     this._pc.setHeading(this._pc.targetDirection(this._targetX, this._targetY));
     if (this._weaponType == 20) {
       if (!this._pc.glanceCheck(this._targetX, this._targetY)) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 21, this._targetX, this._targetY, this._isHit));

         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 21, this._targetX, this._targetY, this._isHit));

         this._isHit = false;
         return;
       }
       if (this._arrow != null) {
         if (!this._pc.noPlayerCK)
           this._pc.getInventory().removeItem(this._arrow, 1);
         if (spriteId == 7967) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 7972, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 7972, this._targetX, this._targetY, this._isHit));
         }
         else if (spriteId == 11402 || spriteId == 8900) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8904, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8904, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
         else if (spriteId == 11406 || spriteId == 8913) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8916, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8916, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
         else if (spriteId == 13631) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13656, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13656, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
         else if (spriteId == 13635) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13658, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13658, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
         else if (spriteId == 15814) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 12243, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 12243, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
         else if (spriteId == 16002) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
         else if (spriteId == 16074) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
         else if (spriteId == 17535) {
           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 17539, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 17539, this._targetX, this._targetY, this._isHit));

           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         } else {

           this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 66, this._targetX, this._targetY, this._isHit));
           Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 66, this._targetX, this._targetY, this._isHit));
         }

         if (this._isHit) {
           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);

         }
       }
       else if (this._weaponId == 190) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 2349, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 2349, this._targetX, this._targetY, this._isHit));
         if (this._isHit) {
           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
         }
       }
       else if (this._weaponId == 202011) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8916, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8916, this._targetX, this._targetY, this._isHit));
         if (this._isHit) {
           Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);



         }


       }



     }
     else if (this._weaponType == 62 && this._sting != null) {
       this._pc.getInventory().removeItem(this._sting, 1);
       if (spriteId == 7967) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 7972, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 7972, this._targetX, this._targetY, this._isHit));
       } else if (spriteId == 11402 || spriteId == 8900) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8904, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8904, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
       else if (spriteId == 11406 || spriteId == 8913) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8916, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 8916, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
       else if (spriteId == 13631) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13656, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13656, this._targetX, this._targetY, this._isHit));

         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
       else if (spriteId == 13635) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13658, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 13658, this._targetX, this._targetY, this._isHit));

         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
       else if (spriteId == 15814) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 12243, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 12243, this._targetX, this._targetY, this._isHit));

         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
       else if (spriteId == 16002) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));

         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
       else if (spriteId == 16074) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 16078, this._targetX, this._targetY, this._isHit));

         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
       else if (spriteId == 17535) {
         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 17539, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 17539, this._targetX, this._targetY, this._isHit));

         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       } else {

         this._pc.sendPackets((ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 2989, this._targetX, this._targetY, this._isHit));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._pc, this._targetId, 2989, this._targetX, this._targetY, this._isHit));
       }
       if (this._isHit) {
         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);
       }
     }
     else if (this._weaponType2 == 17) {
       ServerBasePacket pck = null;
       if (!this._isCritical) {
         S_Attack s_Attack = S_Attack.getKeylink2((L1Object)this._pc, this._target, this._attackType, this._isHit);
         this._pc.sendPackets((ServerBasePacket)s_Attack, false);
         this._pc.broadcastPacket((ServerBasePacket)s_Attack);
       } else {
         S_Attack s_Attack = S_Attack.getKeylink_Critical((L1Object)this._pc, this._target, this._attackType, this._isHit);
         this._pc.sendPackets((ServerBasePacket)s_Attack, false);
         this._pc.broadcastPacket((ServerBasePacket)s_Attack);
       }
     } else {
       int actid = 1;
       if (this._isHit) {
         ServerBasePacket pck = null;
         if (!this._isCritical)
         {
           if (this._pc.isSpearModeType()) {



             this._pc.sendPackets((ServerBasePacket)new S_AttackPacket((L1Character)this._pc, this._targetId, 125, this._attackType));
             Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackPacket((L1Character)this._pc, this._targetId, 125, this._attackType));
           } else {
             this._pc.sendPackets((ServerBasePacket)new S_AttackPacket((L1Character)this._pc, this._targetId, actid, this._attackType));
             Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackPacket((L1Character)this._pc, this._targetId, actid, this._attackType));
           }
         }
         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._pc);

       }
       else if (this._targetId > 0) {
         this._pc.sendPackets((ServerBasePacket)new S_AttackMissPacket((L1Character)this._pc, this._targetId));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackMissPacket((L1Character)this._pc, this._targetId));
       } else {
         this._pc.sendPackets((ServerBasePacket)new S_AttackPacket((L1Character)this._pc, 0, actid));
         Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackPacket((L1Character)this._pc, 0, actid));
       }
     }
   }



   private void actionNpc() {
     int _npcObjectId = this._npc.getId();
     int bowActId = 0;
     int actId = 0;

     this._npc.setHeading(this._npc.targetDirection(this._targetX, this._targetY));


     boolean isLongRange = (this._npc.getLocation().getTileLineDistance(new Point(this._targetX, this._targetY)) > 1);
     bowActId = this._npc.getNpcTemplate().getBowActId();

     if (getActId() > 0) {
       actId = getActId();
     } else {
       actId = 1;
     }

     if (isLongRange && bowActId > 0) {
       Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_UseArrowSkill((L1Character)this._npc, this._targetId, bowActId, this._targetX, this._targetY, this._isHit));

     }
     else if (this._isHit) {
       if (getGfxId() > 0) {
         Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_UseAttackSkill(this._target, _npcObjectId,
               getGfxId(), this._targetX, this._targetY, actId));
         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._npc);
       } else {

         Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_AttackPacketForNpc(this._target, _npcObjectId, actId));
         Broadcaster.broadcastPacketExceptTargetSight(this._target, (ServerBasePacket)new S_DoActionGFX(this._targetId, 2), (L1Character)this._npc);
       }

     }
     else if (getGfxId() > 0) {
       Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_UseAttackSkill(this._target, _npcObjectId,
             getGfxId(), this._targetX, this._targetY, actId, 0));
     } else {
       Broadcaster.broadcastPacket((L1Character)this._npc, (ServerBasePacket)new S_AttackMissPacket((L1Character)this._npc, this._targetId, actId));
     }
   }





   public void calcOrbit(int cx, int cy, int head) {
     float dis_x = Math.abs(cx - this._targetX);
     float dis_y = Math.abs(cy - this._targetY);
     float dis = Math.max(dis_x, dis_y);
     float avg_x = 0.0F;
     float avg_y = 0.0F;
     if (dis == 0.0F) {
       switch (head) {
         case 1:
           avg_x = 1.0F;
           avg_y = -1.0F;
           break;
         case 2:
           avg_x = 1.0F;
           avg_y = 0.0F;
           break;
         case 3:
           avg_x = 1.0F;
           avg_y = 1.0F;
           break;
         case 4:
           avg_x = 0.0F;
           avg_y = 1.0F;
           break;
         case 5:
           avg_x = -1.0F;
           avg_y = 1.0F;
           break;
         case 6:
           avg_x = -1.0F;
           avg_y = 0.0F;
           break;
         case 7:
           avg_x = -1.0F;
           avg_y = -1.0F;
           break;
         case 0:
           avg_x = 0.0F;
           avg_y = -1.0F;
           break;
       }


     } else {
       avg_x = dis_x / dis;
       avg_y = dis_y / dis;
     }

     int add_x = (int)Math.floor((avg_x * 15.0F + 0.59F));
     int add_y = (int)Math.floor((avg_y * 15.0F + 0.59F));

     if (cx > this._targetX) {
       add_x *= -1;
     }
     if (cy > this._targetY) {
       add_y *= -1;
     }

     this._targetX += add_x;
     this._targetY += add_y;
   }



   public void commit() {
     if (this._isHit) {
       try {
         if (this._calcType == 1 || this._calcType == 3) {
           commitPc();
         } else if (this._calcType == 2 || this._calcType == 4) {
           commitNpc();
         }
       } catch (Exception exception) {}
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
       String PcName = ""; // 玩家名稱
       String HitRate = ""; // 命中率
       String DMG = ""; // 傷害
       String TargetName = ""; // 目標名稱

       if (this._calcType == 1 || this._calcType == 2) { // 如果計算類型為1或2
         PcName = this._pc.getName(); // 獲取玩家名稱
       } else if (this._calcType == 3) { // 如果計算類型為3
         PcName = this._npc.getName(); // 獲取NPC名稱
       }

       if (this._calcType == 3 || this._calcType == 1) { // 如果計算類型為3或1
         TargetName = this._targetPc.getName(); // 獲取目標玩家名稱
         HitRate = "HP: " + this._targetPc.getCurrentHp() + " / 命中率: " + this._hitRate; // 設置命中率字符串
       } else if (this._calcType == 2) { // 如果計算類型為2
         TargetName = this._targetNpc.getName(); // 獲取目標NPC名稱
         HitRate = "HP: " + this._targetNpc.getCurrentHp() + " / 命中率: " + this._hitRate; // 設置命中率字符串
       }
       DMG = "傷害： " + this._damage; // 設置傷害字符串

                    // 如果玩家不是目標玩家或者傷害不為0
       if (this._pc != this._targetPc || this._damage != 0) {
         if (this._calcType == 1 || this._calcType == 2) { // 如果計算類型為1或2
           this._pc.sendPackets("\aG[" + PcName + " -> " + TargetName + "] : " + DMG + " / " + HitRate); // 向玩家發送信息
         }
         if (this._calcType == 3 ||這._calcType == 1) { // 如果計算類型為3或1
           this._targetPc.sendPackets("\aH[" + PcName + " -> " + TargetName + "] : " + DMG + " / " + HitRate); // 向目標玩家發送信息
         }
       }
     }


   private void commitPc() {
     if (this._calcType == 1) {
       if (MJCommons.isUnbeatable((L1Character)this._targetPc))
         this._damage = 0;
       this._targetPc.receiveDamage((L1Character)this._pc, this._damage);
     } else if (this._calcType == 3) {
       if (MJCommons.isUnbeatable((L1Character)this._targetPc))
         this._damage = 0;
       this._targetPc.receiveDamage((L1Character)this._npc, this._damage);
     }
   }


   private void commitNpc() {
     if (this._calcType == 2) {
       if (MJCommons.isUnbeatable((L1Character)this._targetNpc))
         this._damage = 0;
       damageNpcWeaponDurability();
       this._targetNpc.receiveDamage((L1Character)this._pc, this._damage);
     } else if (this._calcType == 4) {
       if (MJCommons.isUnbeatable((L1Character)this._targetNpc))
         this._damage = 0;
       this._targetNpc.receiveDamage((L1Character)this._npc, this._damage);
     }
   }


   public void actionCounterBarrier() {
     if (this._calcType == 1) {
       if (this._pc == null)
         return;
       this._pc.setHeading(this._pc.targetDirection(this._targetX, this._targetY));
       this._pc.sendPackets((ServerBasePacket)new S_AttackMissPacket((L1Character)this._pc, this._targetId));
       this._pc.broadcastPacket((ServerBasePacket)new S_AttackMissPacket((L1Character)this._pc, this._targetId), this._target);
       this._pc.sendPackets((ServerBasePacket)new S_DoActionGFX(this._pc.getId(), 2));
       this._pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(this._pc.getId(), 2));
       if (this._targetPc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
         this._targetPc.send_effect(17220);
       } else {
         this._targetPc.send_effect(10710);
       }
     } else if (this._calcType == 3) {
       if (this._npc == null || this._target == null)
         return;
       int actId = 0;
       this._npc.setHeading(this._npc.targetDirection(this._targetX, this._targetY));
       if (getActId() > 0) {
         actId = getActId();
       } else {
         actId = 1;
       }
       if (getGfxId() > 0) {
         this._npc.broadcastPacket((ServerBasePacket)new S_UseAttackSkill(this._target, this._npc
               .getId(), getGfxId(), this._targetX, this._targetY, actId, 0), this._target);
       } else {
         this._npc.broadcastPacket((ServerBasePacket)new S_AttackMissPacket((L1Character)this._npc, this._targetId, actId), this._target);
       }
       this._npc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(this._npc.getId(), 2));
       if (this._targetPc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
         this._targetPc.send_effect(17220);
       } else {
         this._targetPc.send_effect(10710);
       }
     }
   }

   public void actionConqure() {
     if (this._calcType == 1) {
       if (this._pc == null)
         return;
       this._pc.setHeading(this._pc.targetDirection(this._targetX, this._targetY));
       this._pc.sendPackets((ServerBasePacket)new S_AttackMissPacket((L1Character)this._pc, this._targetId));
       this._pc.broadcastPacket((ServerBasePacket)new S_AttackMissPacket((L1Character)this._pc, this._targetId), this._target);
       this._pc.sendPackets((ServerBasePacket)new S_DoActionGFX(this._pc.getId(), 2));
       this._pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(this._pc.getId(), 2));
       this._targetPc.send_effect(21808);
     } else if (this._calcType == 3) {
       if (this._npc == null || this._target == null)
         return;
       int actId = 0;
       this._npc.setHeading(this._npc.targetDirection(this._targetX, this._targetY));
       if (getActId() > 0) {
         actId = getActId();
       } else {
         actId = 1;
       }
       if (getGfxId() > 0) {
         this._npc.broadcastPacket((ServerBasePacket)new S_UseAttackSkill(this._target, this._npc
               .getId(), getGfxId(), this._targetX, this._targetY, actId, 0), this._target);
       } else {
         this._npc.broadcastPacket((ServerBasePacket)new S_AttackMissPacket((L1Character)this._npc, this._targetId, actId), this._target);
       }
       this._npc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(this._npc.getId(), 2));
       this._targetPc.send_effect(21808);
     }
   }


   public void NpcactionCounterBarrier(L1Character attacker, L1PcInstance pc) {
     if (pc == null || this._target == null)
       return;
     int actId = 0;
     pc.setHeading(pc.targetDirection(this._targetX, this._targetY));
     if (getActId() > 0) {
       actId = getActId();
     } else {
       actId = 1;
     }
     if (getGfxId() > 0) {
       pc.broadcastPacket((ServerBasePacket)new S_UseAttackSkill(this._target, pc.getId(), getGfxId(), this._targetX, this._targetY, actId, 0), this._target);
     } else {

       pc.broadcastPacket((ServerBasePacket)new S_AttackMissPacket((L1Character)pc, this._targetId, actId), this._target);
     }
     pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(pc.getId(), 2));
     attacker.send_effect(17220);
   }


   public void actionMortalBody() {
     if (this._calcType == 1) {
       if (this._pc == null || this._target == null)
         return;
       this._pc.setHeading(this._pc.targetDirection(this._targetX, this._targetY));
       S_UseAttackSkill packet = new S_UseAttackSkill((L1Character)this._pc, this._target.getId(), 9802, this._targetX, this._targetY, 1, false);

       this._pc.sendPackets((ServerBasePacket)packet);
       this._pc.broadcastPacket((ServerBasePacket)packet, this._target);
       this._pc.sendPackets((ServerBasePacket)new S_DoActionGFX(this._pc.getId(), 2));
       this._pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(this._pc.getId(), 2));
     } else if (this._calcType == 3) {
       if (this._npc == null || this._target == null)
         return;
       this._npc.send_effect(9802);
       this._npc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(this._npc.getId(), 2));
     }
   }


   public boolean isShortDistance1() {
     boolean isShortDistance1 = true;




     if (this._calcType == 3) {
       boolean isLongRange = (this._npc.getLocation().getTileLineDistance(new Point(this._targetX, this._targetY)) <= 0);
       int bowActId = this._npc.getNpcTemplate().getBowActId();

       if (isLongRange && bowActId > 0) {
         isShortDistance1 = false;
       }
     }
     return isShortDistance1;
   }


   public boolean isShortDistance() {
     boolean isShortDistance = true;
     if (this._calcType == 1) {
       if (this._weaponType == 20 || this._weaponType == 62 || this._weaponType2 == 17 || this._weaponType2 == 19 || this._pc
         .hasSkillEffect(112)) {
         isShortDistance = false;
       }
     } else if (this._calcType == 3) {
       if (this._npc == null)
         return false;
       boolean isLongRange = (this._npc.getLocation().getTileLineDistance(new Point(this._targetX, this._targetY)) > 1);
       int bowActId = this._npc.getNpcTemplate().getBowActId();

       if (isLongRange && bowActId > 0) {
         isShortDistance = false;
       }
     }
     return isShortDistance;
   }


   public void commitCounterBarrier() {
     int damage = calcCounterBarrierDamage();
     if (damage == 0) {
       return;
     }
     if (this._calcType == 1) {
       this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, damage);
     } else if (this._calcType == 3) {
       this._npc.receiveCounterBarrierDamage((L1Character)this._targetPc, damage);
     } else if (this._calcType == 2) {
       this._pc.receiveCounterBarrierDamage((L1Character)this._targetPc, damage);
     }
   }

   public void commitConqure() {
     int damage = calcConqureDamage();
     int difflevel = 0;
     int targetlevel = 0;
     int attackerlevel = 0;
     if (damage == 0) {
       return;
     }

     int _shockStunDuration = 0;

     if (this._calcType == 1) {

       if (this._pc instanceof L1PcInstance) {
         L1PcInstance target = this._pc;
         this

           .DCCD = CalcStat.calcPureDecreaseCCDuration(target.getAbility().getCha()) + CalcStat.calcDecreaseCCDuration(target.getAbility().getTotalCha()) + target.get_status_time_reduce();
       }
       if (this._targetPc instanceof L1PcInstance) {
         L1PcInstance pc = this._targetPc;
         this.ICCD = pc.get_CC_Increase();
       }
       attackerlevel = this._pc.getLevel();
     } else if (this._calcType == 3) {

       attackerlevel = this._npc.getLevel();
     }


     difflevel = this._targetPc.getLevel() - attackerlevel;

     if (this._targetPc != null) {
       if (difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL) {
         int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS;
         _shockStunDuration = SkillTimeArray[this.random.nextInt(SkillTimeArray.length)];
       } else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL1 && difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL2) {

         int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS1;
         _shockStunDuration = SkillTimeArray[this.random.nextInt(SkillTimeArray.length)];
       } else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL3 && difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL4) {

         int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS2;
         _shockStunDuration = SkillTimeArray[this.random.nextInt(SkillTimeArray.length)];
       } else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL5 && difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL6) {

         int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS3;
         _shockStunDuration = SkillTimeArray[this.random.nextInt(SkillTimeArray.length)];
       } else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL7 && difflevel < Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL8) {

         int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS4;
         _shockStunDuration = SkillTimeArray[this.random.nextInt(SkillTimeArray.length)];
       } else if (difflevel >= Config.MagicAdSetting_Prince.CONQUEROR_STUN_LVL9) {

         int[] SkillTimeArray = Config.MagicAdSetting_Prince.CONQUEROR_STUN_MS5;

         _shockStunDuration = SkillTimeArray[this.random.nextInt(SkillTimeArray.length)];
       }
     }



     if (this.DCCD != 0) {
       _shockStunDuration -= this.DCCD;
     }
     if (this.ICCD != 0) {
       _shockStunDuration += this.ICCD;
     }
     L1ItemInstance weapon = this._targetPc.getWeapon();
     if (weapon.getItemId() == 7000239) {
       _shockStunDuration += 1000;
     }
     if (_shockStunDuration <= 0) {
       _shockStunDuration = 100;
     }

     if (this._calcType == 1) {
       L1Magic magic = new L1Magic((L1Character)this._targetPc, (L1Character)this._pc);
       boolean isStun = magic.calcProbabilityMagic(995049);

       if (isStun) {
         this._pc.sendPackets((ServerBasePacket)new S_Paralysis(5, true));
         this._pc.setSkillEffect(995049, _shockStunDuration);

         L1SkillUse.on_icons(this._pc, 995049, _shockStunDuration / 1000);
         L1EffectSpawn.getInstance().spawnEffect2(460000167, 995049, this._pc.getX(), this._pc.getY(), this._pc
             .getMapId(), (L1Character)this._pc);
       }
     } else if (this._calcType == 3) {
       L1Magic magic = new L1Magic((L1Character)this._targetPc, (L1Character)this._npc);
       boolean isStun = magic.calcProbabilityMagic(995049);
       if (isStun) {

         L1EffectSpawn.getInstance().spawnEffect2(460000167, 995049, this._npc.getX(), this._npc.getY(), this._npc
             .getMapId(), (L1Character)this._npc);
         this._npc.setSkillEffect(995049, _shockStunDuration);
         this._npc.setParalyzed(true);
         this._npc.setParalysisTime(_shockStunDuration);
       }
     }

     if (this._calcType == 1) {
       this._pc.receiveConqureDamage((L1Character)this._targetPc, damage);
     } else if (this._calcType == 3) {
       this._npc.receiveConqureDamage((L1Character)this._targetPc, damage);
     }
   }

   public void commitBossCounterBarrier(L1Character attacker, L1PcInstance pc) {
     int damage = 0;
     L1ItemInstance weapon = null;
     weapon = pc.getWeapon();
     if (weapon == null) {
       damage = 10;
     } else {
       damage = (int)Math.round((weapon
           .getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * Config.MagicAdSetting_Knight.COUNTER / 100.0D);
     }
     pc.receiveCounterBarrierDamage((L1Character)pc, damage);
   }


   public void commitMortalBody() {
     int ac = Math.max(0, 10 - this._targetPc.getAC().getAc());
     int damage = ac / 2;

     if (damage == 0) {
       return;
     }
     if (damage <= 40) {
       damage = 40;
     }
     if (this._calcType == 1) {
       this._pc.receiveDamage((L1Character)this._targetPc, damage);
     } else if (this._calcType == 3) {
       this._npc.receiveDamage((L1Character)this._targetPc, damage);
     }
   }


   private int calcCounterBarrierDamage() {
     double damage = 0.0D;
     L1ItemInstance weapon = this._targetPc.getWeapon();
     if (weapon != null &&
       weapon.getItem().getType() == 3) {
       damage = Math.round((weapon.getItem().getDmgLarge() + getEnchantPureDmg(weapon) + weapon
           .getItem().getDmgModifier())) * Config.MagicAdSetting_Knight.COUNTER / 100.0D;
     }

     return (int)damage;
   }

   private int calcConqureDamage() {
     double damage = 0.0D;
     L1ItemInstance weapon = this._targetPc.getWeapon();
     if (weapon != null && (
       weapon.getItem().getType() == 1 || weapon.getItem().getType() == 2))
     {
       damage = (Math.round((weapon.getItem().getDmgSmall() + getEnchantPureDmg(weapon) + weapon
           .getItem().getDmgModifier())) * Config.MagicAdSetting_Prince.CONQUEROR_DAMAGE_RATE / 100);
     }



     return (int)damage;
   }


   private int 타이탄대미지() {
     double damage = 0.0D;
     L1ItemInstance weapon = this._targetPc.getWeaponSwap();
     if (weapon != null) {
       damage = Math.round((weapon
           .getItem().getDmgLarge() + getEnchantPureDmg(weapon) + weapon.getItem().getDmgModifier())) * Config.MagicAdSetting_Warrior.ROCKDMG / 100.0D;
     }

     return (int)damage;
   }



   private void damageNpcWeaponDurability() {
     int chance = Config.ServerRates.DAMAGEENCHANT;
     int bchance = Config.ServerRates.DAMAGEBLESSENCHANT;


     if (this._pc.getAI() != null) {
       return;
     }

     if (this._pc != null && this._pc.isPassive(MJPassiveID.DAMASCUS.toInt())) {
       return;
     }

     if (this._pc != null && (
       this._weaponId == 7000239 || this._weaponId == 7000240 || this._weaponId == 7000262 || this._weaponId == 7000263 || this._weaponId == 203065 || this._weaponId == 7000265 || this._weaponId == 7000264 || this._weaponId == 203042)) {
       return;
     }





     if (this._calcType != 2 || !this._targetNpc.getNpcTemplate().is_hard() || this._weaponType == 0 || this.weapon
       .getItem().get_canbedmg() == 0 || this._pc.hasSkillEffect(175)) {
       return;
     }


     if (this._pc != null && !this._pc.isPassive(MJPassiveID.SOLID_NOTE.toInt()) &&
       this.weapon.isEquipped() && (
       this._weaponBless == 1 || this._weaponBless == 2) && _random.nextInt(100) + 1 < chance) {
       this._pc.sendPackets((ServerBasePacket)new S_ServerMessage(268, this.weapon.getLogName()));
       this._pc.getInventory().receiveDamage(this.weapon);
     }




     if (this._pc != null && !this._pc.isPassive(MJPassiveID.SOLID_NOTE.toInt()) &&
       this._weaponBless == 0 && _random.nextInt(100) + 1 < bchance) {
       this._pc.sendPackets((ServerBasePacket)new S_ServerMessage(268, this.weapon.getLogName()));
       this._pc.getInventory().receiveDamage(this.weapon);
     }
   }



   private int PchitAdd() {
     int value = 0;
     if (this._weaponType != 20 && this._weaponType != 62) {
       value += this._pc.getHitup() + this._pc.getHitRate();
     } else {
       value += this._pc.getBowHitup() + this._pc.getBowHitRate();
     }
     return value;
   }


   private int toPcSkillHit() {
     int value = 0;
     if (this._targetPc.hasSkillEffect(7671) || this._targetPc.hasSkillEffect(7675) || this._targetPc
       .hasSkillEffect(7676)) {
       int chance = _random.nextInt(100);
       if (chance < 15) {
         value -= 5;
       }
     }
     return value;
   }


   private double toPcBuffDmg(double dmg) {
     try {
       if (this._targetPc.hasSkillEffect(3050) || this._targetPc
         .hasSkillEffect(3051) || this._targetPc.hasSkillEffect(3052) || this._targetPc
         .hasSkillEffect(3053) || this._targetPc.hasSkillEffect(3054) || this._targetPc
         .hasSkillEffect(3055) || this._targetPc.hasSkillEffect(3056) || this._targetPc
         .hasSkillEffect(3058) || this._targetPc.hasSkillEffect(3059) || this._targetPc
         .hasSkillEffect(3060) || this._targetPc.hasSkillEffect(3061) || this._targetPc
         .hasSkillEffect(3062) || this._targetPc.hasSkillEffect(3063) || this._targetPc
         .hasSkillEffect(3064) || this._targetPc.hasSkillEffect(3065) || this._targetPc
         .hasSkillEffect(3066) || this._targetPc.hasSkillEffect(3067) || this._targetPc
         .hasSkillEffect(3068) || this._targetPc.hasSkillEffect(3069) || this._targetPc
         .hasSkillEffect(3070) || this._targetPc.hasSkillEffect(3071) || this._targetPc
         .hasSkillEffect(3072)) {
         dmg -= 5.0D;
       }
       if (this._targetPc.hasSkillEffect(3074) || this._targetPc.hasSkillEffect(3075) || this._targetPc
         .hasSkillEffect(3076) || this._targetPc.hasSkillEffect(3077) || this._targetPc
         .hasSkillEffect(3100) || this._targetPc.hasSkillEffect(3101) || this._targetPc
         .hasSkillEffect(3102) || this._targetPc.hasSkillEffect(3103))
       {
         dmg -= 2.0D;
       }
       if (this._targetPc.hasSkillEffect(3057) || this._targetPc.hasSkillEffect(3065) || this._targetPc
         .hasSkillEffect(3073))
       {
         dmg -= 5.0D;
       }


       if (this._targetPc.getPassive(MJPassiveID.ARMOR_GUARD.toInt()) != null) {
         int d = this._targetPc.getAC().getAc() / 10;
         if (d < 0) {
           dmg += d;
         } else {
           dmg -= d * 1.5D;
         }
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
         if (targetPcLvl < 80) {
           targetPcLvl = 80;
         }
         dmg -= ((targetPcLvl - 80) / 4 + 1);
       }


       if (this._targetPc.isPassive(MJPassiveID.DRAGON_SKIN_PASS.toInt())) {
         if (this._targetPc.getLevel() >= 80) {
           dmg -= (5 + (this._targetPc.getLevel() - 78) / 2);
         } else {
           dmg -= 5.0D;
         }
       }

       if (this._targetPc.hasSkillEffect(219)) {
         dmg += dmg / 5.0D;
       }
       if (this._targetPc.hasSkillEffect(22000)) {
         dmg -= 3.0D;
       }
       if (this._targetPc.hasSkillEffect(22001)) {
         dmg -= 2.0D;
       }
       if (this._targetPc.hasSkillEffect(159)) {
         int targetPcLvl = this._targetPc.getLevel();
         if (targetPcLvl < 80) {
           targetPcLvl = 80;
         }
         dmg -= ((targetPcLvl - 80) / 4 + 1);
       }
       if (this._targetPc.hasSkillEffect(78)) {
         dmg = 0.0D;
       }
       if (this._targetPc.hasSkillEffect(70705)) {
         dmg = 0.0D;
       }
       if (this._targetPc.hasSkillEffect(157)) {
         dmg = 0.0D;
       }
       if (this._targetPc.hasSkillEffect(212)) {
         this._targetPc.removeSkillEffect(212);
       }
     }
     catch (Exception e) {
       e.printStackTrace();
     }
     return dmg;
   }


   public static double WeaponLevelAttack(L1PcInstance pc, L1Character cha, int effect, int enchant, int weaponlevel) {
     double dmg = 0.0D;
     int Stat = 0;
     int chance = _random.nextInt(100) + 1;
     int enchatndmg = 0;

     if (pc.isWizard() || pc.isBlackwizard()) {
       Stat = pc.getAbility().getTotalInt();
     }
     if (pc.isElf()) {
       Stat = pc.getAbility().getTotalDex();
     } else {
       Stat = pc.getAbility().getTotalStr();
     }

     if (weaponlevel == 1) {
       enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl1 + Stat;
     } else if (weaponlevel == 2) {
       enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl2 + Stat;
     } else if (weaponlevel == 3) {
       enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl3 + Stat;
     } else if (weaponlevel == 4) {
       enchatndmg = Config.DollEnchant.WeaponEnchantDmglvl4 + Stat;
     }

     if (Config.DollEnchant.WeaponMagicPer + enchant + weaponlevel >= chance) {
       dmg = (_random.nextInt(enchatndmg) + 1);
       if (2 >= chance) {
         dmg += dmg * 0.1D;
       } else if (dmg <= 0.0D) {
         dmg = 0.0D;
       }
       broadcast(cha, (ServerBasePacket)new S_SkillSound(cha.getId(), effect));
     }
     return calcDamageReduction(cha, dmg, 8);
   }

   public static double calcDamageReduction(L1Character cha, double dmg, int attr) {
     if (isFreeze(cha)) {
       return 0.0D;
     }

     int ran1 = 0;
     int mrset = 0;
     int mrs = cha.getResistance().getEffectedMrBySkill();
     ran1 = _random.nextInt(5) + 1;
     mrset = mrs - ran1;
     double calMr = 0.0D;
     calMr = (220 - mrset) / 250.0D;
     dmg *= calMr;

     if (dmg < 0.0D) {
       dmg = 0.0D;
     }

     int resist = 0;
     if (attr == 1) {
       resist = cha.getResistance().getEarth();
     } else if (attr == 2) {
       resist = cha.getResistance().getFire();
     } else if (attr == 4) {
       resist = cha.getResistance().getWater();
     } else if (attr == 8) {
       resist = cha.getResistance().getWind();
     }
     int resistFloor = (int)(0.32D * Math.abs(resist));
     if (resist >= 0) {
       resistFloor *= 1;
     } else {
       resistFloor *= -1;
     }
     double attrDeffence = resistFloor / 32.0D;
     dmg = (1.0D - attrDeffence) * dmg;

     return dmg;
   }


   private static boolean isFreeze(L1Character cha) {
     if (cha.hasSkillEffect(10071)) {
       return true;
     }
     if (cha.hasSkillEffect(78)) {
       return true;
     }
     if (cha.hasSkillEffect(70705)) {
       return true;
     }
     if (cha.hasSkillEffect(157)) {
       return true;
     }

     if (cha.hasSkillEffect(31)) {
       cha.removeSkillEffect(31);
       int castgfx = SkillsTable.getInstance().getTemplate(31).getCastGfx();
       cha.broadcastPacket((ServerBasePacket)new S_SkillSound(cha.getId(), castgfx));
       if (cha instanceof L1PcInstance) {
         L1PcInstance pc = (L1PcInstance)cha;
         pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), castgfx));
       }
       return true;
     }
     return false;
   }

   protected static void broadcast(L1Character c, ServerBasePacket pck) {
     c.sendPackets(pck, false);
     c.broadcastPacket(pck);
   }

   public void set_hit_rate(int rate) {
     this._hitRate = rate;
   }

   public void set_hit(boolean hit) {
     this._isHit = hit;
   }

   public void set_is_critical(boolean is_critical) {
     this._isCritical = is_critical;
   }


   private int getReductionIgnore(int reduc, int pvpreduc, L1Character pc) {
     int canDmg = 0;
     int reduc_ignore = 0;

     if (pc instanceof L1PcInstance) {
       reduc_ignore = pc.getReducCancel() + ((L1PcInstance)pc).get_pvp_dmg_ignore();
     } else {
       reduc_ignore = pc.getReducCancel();
     }

     canDmg = reduc + pvpreduc - reduc_ignore;

     if (canDmg < 0) {
       canDmg = 0;
     }


     return canDmg;
   }

   public int getEnchantPureDmg(L1ItemInstance weapon) {
     if (weapon == null) {
       return 0;
     }
     int dmg = 0;
     int enchant = weapon.getEnchantLevel();
     if (C_ItemUSe.is_legend_weapon(weapon.getItemId())) {
       dmg = enchant * 2;
     } else if (C_ItemUSe.is_ancient_weapon(weapon.getItemId())) {
       dmg = enchant * 4;
     }
     else if (enchant >= 1 && enchant <= 9) {
       dmg = enchant;
     } else if (enchant > 9) {
       dmg = 9 + (enchant - 9) * 2;
     }
     if (this._weaponType != 20 && this._weaponType != 62) {
       dmg -= weapon.get_durability();
     }
     return dmg;
   }

   private int getEnchantDmg(L1ItemInstance weapon) {
     int dmg = getEnchantPureDmg(weapon);

     if (this._pc.hasSkillEffect(175)) {
       if (this._weaponType != 20 && this._weaponType != 62) {
         dmg += dmg / 2;
       }
     } else if (this._weaponType == 58 && _random.nextInt(100) + 1 <= this._weaponDoubleDmgChance) {
       this._pc.sendPackets((ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, 58, (this.Sweapon != null)));
       Broadcaster.broadcastPacket((L1Character)this._pc, (ServerBasePacket)new S_AttackCritical(this._pc, this._targetId, 58, (this.Sweapon != null)));
       this._attackType = 2;
       this._isCritical = true;
       dmg += dmg / 2;
     } else if (dmg > 1) {
       dmg = dmg - dmg / 2 + _random.nextInt(dmg) + 1;
     }
     return dmg;
   }

   public static void fouslayer_brave(L1PcInstance attacker, L1Character cha) {
     Random random = new Random(System.nanoTime());
     L1Magic magic = new L1Magic((L1Character)attacker, cha);
     int skillid = 315;
     int ReSkilldelay = Config.MagicAdSetting_DragonKnight.FOU_TURN_REUSE_TIME;
     if (attacker.hasSkillEffect(51007)) {
       return;
     }
     if (!attacker.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
       return;
     }
     if (attacker.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
       skillid = 7320184;
     }

     boolean success = magic.calcProbabilityMagic(skillid);
     attacker.sendPackets((ServerBasePacket)new S_PacketBox(75, 1), true);
     attacker.setSkillEffect(51007, ReSkilldelay);

     if (success) {
       int targetLevel = 0;
       int diffLevel = 0;
       int StunDuration = 0;

       int SpawnEffect = 8503099;
       if (attacker.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
         SpawnEffect = 460000170;
       }

       if (cha instanceof L1PcInstance) {
         L1PcInstance pc = (L1PcInstance)cha;
         targetLevel = pc.getLevel();
       } else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {

         L1NpcInstance npc = (L1NpcInstance)cha;
         targetLevel = npc.getLevel();
       }

       diffLevel = attacker.getLevel() - targetLevel;


       if (diffLevel < -5) {
         int[] stunTimeArray = { 1000, 2000, 3000 };
         StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
       }
       else if (diffLevel >= -5 && diffLevel <= -3) {
         int[] stunTimeArray = { 1000, 2000, 3000, 4000 };
         StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
       }
       else if (diffLevel >= -2 && diffLevel <= 2) {
         int[] stunTimeArray = { 1000, 2000, 3000, 4000 };
         StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
       }
       else if (diffLevel >= 3 && diffLevel <= 5) {
         int[] stunTimeArray = { 2000, 3000, 4000, 5000 };
         StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
       }
       else if (diffLevel >= 5 && diffLevel <= 10) {
         int[] stunTimeArray = { 3000, 5000, 6000 };
         StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
       }
       else if (diffLevel > 10) {
         int[] stunTimeArray = { 4000, 5000, 6000 };
         StunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
       }
       if (attacker.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
         StunDuration += 1000;
       }

       if (attacker.getWeapon() != null) {
         L1ItemInstance weapon = attacker.getWeapon();
         if (weapon.getItemId() == 7000267) {
           StunDuration += 1000;
         }
       }

       L1EffectSpawn.getInstance().spawnEffect2(SpawnEffect, skillid, cha.getX(), cha.getY(), cha.getMapId(), cha);

       if (cha instanceof L1PcInstance) {
         L1PcInstance pc = (L1PcInstance)cha;
         pc.sendPackets((ServerBasePacket)new S_Paralysis(5, true));
         L1SkillUse.on_icons(pc, skillid, StunDuration / 1000);
         pc.setSkillEffect(skillid, StunDuration);
       } else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {

         L1NpcInstance npc = (L1NpcInstance)cha;
         npc.setSkillEffect(skillid, StunDuration);
         npc.setParalyzed(true);
         npc.setParalysisTime(StunDuration);
       }
     }
   }

     private static final int[] STUN_TO_HOLD_TYPE_SKILL = new int[] { 87, 5056, 5003, 123, 208, 242, 100242, 30006, 30005, 30081, 707113, 22055, 707041, 707119, 707056, 707099, 707054, 22025, 22026, 22027, 22031, 51006, 707152, 707159, 228, 230, 199, 5002, 212, 103, 192, 10071, 77, 5037 };
     }



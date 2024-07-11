 package l1j.server.server.model;

 import java.util.Random;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.datatables.WeaponSkillTable;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_UseAttackSkill;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Skills;
















 public class L1WeaponSkill
 {
     private static Random _random = new Random(System.nanoTime());

     private int _weaponId;

     private int _probability;

     private int _fixDamage;

     private int _randomDamage;

     private int _area;

     private int _skillId;

     private int _skillTime;

     private int _effectId;

     private int _effectTarget;

     private boolean _isArrowType;

     private int _attr;


     public L1WeaponSkill(int weaponId, int probability, int fixDamage, int randomDamage, int area, int skillId, int skillTime, int effectId, int effectTarget, boolean isArrowType, int attr) {
         this._weaponId = weaponId;
         this._probability = probability;
         this._fixDamage = fixDamage;
         this._randomDamage = randomDamage;
         this._area = area;
         this._skillId = skillId;
         this._skillTime = skillTime;
         this._effectId = effectId;
         this._effectTarget = effectTarget;
         this._isArrowType = isArrowType;
         this._attr = attr;
     }

     public int getWeaponId() {
         return this._weaponId;
     }

     public int getProbability() {
         return this._probability;
     }

     public int getFixDamage() {
         return this._fixDamage;
     }

     public int getRandomDamage() {
         return this._randomDamage;
     }

     public int getArea() {
         return this._area;
     }

     public int getSkillId() {
         return this._skillId;
     }

     public int getSkillTime() {
         return this._skillTime;
     }

     public int getEffectId() {
         return this._effectId;
     }

     public int getEffectTarget() {
         return this._effectTarget;
     }

     public boolean isArrowType() {
         return this._isArrowType;
     }

     public int getAttr() {
         return this._attr;
     }

     public static double getWeaponSkillDamage(L1PcInstance pc, L1Character cha, int weaponId) {
         L1WeaponSkill weaponSkill = WeaponSkillTable.getInstance().getTemplate(weaponId);
         if (pc == null || cha == null || weaponSkill == null) {
             return 0.0D;
         }

         int chance = _random.nextInt(100) + 1;
         if (weaponSkill.getProbability() < chance) {
             return 0.0D;
         }

         int skillId = weaponSkill.getSkillId();

         if (skillId == 64 && cha instanceof L1NpcInstance) {
             L1NpcInstance npc = (L1NpcInstance)cha;

             if (npc.getNpcId() == 45684 || npc.getNpcId() == 45683 || npc.getNpcId() == 45681 || npc.getNpcId() == 45682 || npc
                     .getNpcId() == 900011 || npc.getNpcId() == 900012 || npc.getNpcId() == 900013 || npc
                     .getNpcId() == 900038 || npc.getNpcId() == 900039 || npc.getNpcId() == 900040 || npc
                     .getNpcId() == 5096 || npc.getNpcId() == 5097 || npc.getNpcId() == 5098 || npc
                     .getNpcId() == 5099 || npc.getNpcId() == 5100) {
                 return 0.0D;
             }
         }

         if (skillId != 0) {
             L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
             if (skill != null && skill.getTarget().equals("buff") &&
                     !isFreeze(cha)) {
                 if (skillId == 56 &&
                         !cha.hasSkillEffect(skillId)) {
                     cha.addDmgup(-6);
                     cha.getAC().addAc(12);
                     if (cha instanceof L1PcInstance) {
                         L1PcInstance target = (L1PcInstance)cha;
                         target.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(target));
                     }
                 }

                 cha.setSkillEffect(skillId, (weaponSkill.getSkillTime() * 1000));
             }
         }


         int effectId = weaponSkill.getEffectId();
         if (effectId != 0) {
             int chaId = 0;
             if (weaponSkill.getEffectTarget() == 0) {
                 chaId = cha.getId();
             } else {
                 chaId = pc.getId();
             }
             boolean isArrowType = weaponSkill.isArrowType();
             if (!isArrowType) {
                 pc.sendPackets((ServerBasePacket)new S_SkillSound(chaId, effectId));
                 pc.broadcastPacket((ServerBasePacket)new S_SkillSound(chaId, effectId));
             } else {
                 S_UseAttackSkill packet = new S_UseAttackSkill((L1Character)pc, cha.getId(), effectId, cha.getX(), cha.getY(), 1, false);

                 pc.sendPackets((ServerBasePacket)packet);
                 pc.broadcastPacket((ServerBasePacket)packet, cha);
             }
         }

         double damage = 0.0D;
         int randomDamage = weaponSkill.getRandomDamage();
         if (randomDamage != 0) {
             damage = _random.nextInt(randomDamage);
         }
         damage += weaponSkill.getFixDamage();

         if (effectId == 6985) {
             damage += (pc.getAbility().getTotalInt() * 3);
         } else {
             damage += (pc.getAbility().getTotalInt() * 2);
         }

         int area = weaponSkill.getArea();
         if (area > 0 || area == -1) {
             L1PcInstance targetPc = null;
             L1NpcInstance targetNpc = null;
             for (L1Object object : L1World.getInstance().getVisibleObjects(cha, area)) {
                 if (object == null) {
                     continue;
                 }
                 if (!(object instanceof L1Character)) {
                     continue;
                 }
                 if (object.getId() == pc.getId()) {
                     continue;
                 }
                 if (object.getId() == cha.getId()) {
                     continue;
                 }
                 if (object instanceof L1PcInstance) {
                     targetPc = (L1PcInstance)object;
                     if (targetPc.getZoneType() == 1) {
                         continue;
                     }
                 }

                 if (cha instanceof l1j.server.server.model.Instance.L1MonsterInstance &&
                         !(object instanceof l1j.server.server.model.Instance.L1MonsterInstance)) {
                     continue;
                 }

                 if ((cha instanceof L1PcInstance || cha instanceof l1j.server.server.model.Instance.L1SummonInstance || cha instanceof l1j.server.server.model.Instance.L1PetInstance) &&
                         !(object instanceof L1PcInstance) && !(object instanceof l1j.server.server.model.Instance.L1SummonInstance) && !(object instanceof l1j.server.server.model.Instance.L1PetInstance) && !(object instanceof l1j.server.server.model.Instance.L1MonsterInstance) && !(object instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance)) {
                     continue;
                 }



                 damage = calcDamageReduction((L1Character)object, damage, weaponSkill.getAttr());
                 if (damage <= 0.0D) {
                     continue;
                 }
                 if (object instanceof L1PcInstance) {
                     targetPc = (L1PcInstance)object;
                     targetPc.sendPackets((ServerBasePacket)new S_DoActionGFX(targetPc.getId(), 2));
                     targetPc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(targetPc.getId(), 2));
                     targetPc.receiveDamage((L1Character)pc, (int)damage); continue;
                 }  if (object instanceof l1j.server.server.model.Instance.L1SummonInstance || object instanceof l1j.server.server.model.Instance.L1PetInstance || object instanceof l1j.server.server.model.Instance.L1MonsterInstance || object instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {

                     targetNpc = (L1NpcInstance)object;
                     targetNpc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(targetNpc.getId(), 2));
                     targetNpc.receiveDamage((L1Character)pc, (int)damage);
                 }
             }
         }

         return calcDamageReduction(cha, damage, weaponSkill.getAttr());
     }

     public static void 鏈鋸劍(L1PcInstance pc, L1Character target) {
         int chance = 0;
         if (pc.getWeapon() != null) {
             chance = pc.getWeapon().getItem().get_weak_point_chance() + pc.getWeapon().getItem().get_weak_point_enchant_value() * pc.getWeapon().getEnchantLevel();
         }

         if (_random.nextInt(100) >= chance) {
             return;
         }
         if (pc.hasSkillEffect(51002)) {
             pc.killSkillEffectTimer(51002);
         }


         pc.setSkillEffect(51002, 8000L);
         target.send_effect(21932);

         if (!pc.hasSkillEffect(51007)) {
             if (pc.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
                 pc.setChainSwordExposed(true);
                 pc.setChainSwordStep(3);
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 3), true);
             } else if (pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
                 pc.setChainSwordExposed(true);
                 pc.setChainSwordStep(2);
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 2), true);
             } else {
                 pc.setChainSwordExposed(true);
                 pc.setChainSwordStep(1);
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 1), true);
             }
         } else {
             pc.setChainSwordExposed(true);
             pc.setChainSwordStep(1);
             pc.sendPackets((ServerBasePacket)new S_PacketBox(75, 1), true);
         }
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
 }



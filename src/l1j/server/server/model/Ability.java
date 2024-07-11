 package l1j.server.server.model;

 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.CalcStat;
 import l1j.server.server.utils.IntRange;

 public class Ability
 {
   private static final int LIMIT_MINUS_MIN = -128;
   private static final int LIMIT_MIN = 0;
   private static final int LIMIT_MAX = 127;
   private byte str = 0;
   private byte baseStr = 0;
   private byte addedStr = 0;

   private byte con = 0;
   private byte baseCon = 0;
   private byte addedCon = 0;

   private byte dex = 0;
   private byte baseDex = 0;
   private byte addedDex = 0;

   private byte cha = 0;
   private byte baseCha = 0;
   private byte addedCha = 0;

   private byte intel = 0;
   private byte baseInt = 0;
   private byte addedInt = 0;

   private byte wis = 0;
   private byte baseWis = 0;
   private byte addedWis = 0;
   private L1Character character;
   private int sp;

   public void dispose() {
     this.character = null;
   }
   private int _Hunt; private int _Survival; private int _Sacred;
   Ability(L1Character cha) {
     this.character = cha;
   }

   public void init() {
     this.str = this.baseStr = this.addedStr = 0;
     this.dex = this.baseDex = this.addedDex = 0;
     this.con = this.baseCon = this.addedCon = 0;
     this.intel = this.baseInt = this.addedInt = 0;
     this.wis = this.baseWis = this.addedWis = 0;
     this.cha = this.baseCha = this.addedCha = 0;
   }

   private byte checkRange(int i) {
     if (i == 0)
       return 0;
     return checkRange(i, 0);
   }

   private byte checkRange(int i, int base) {
     return (byte)IntRange.ensure(i, 0 + base, 127);
   }

   public int getBaseAmount() {
     return this.baseStr + this.baseCon + this.baseDex + this.baseCha + this.baseInt + this.baseWis;
   }

   public int getAmount() {
     return this.str + this.con + this.dex + this.cha + this.intel + this.wis;
   }



   public void addSp(int i) {
     this.sp += i;

     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
     }
   }

   public int getSp() {
     int total_sp = getTrueSp() + this.sp;

     if (this.character.hasSkillEffect(246)) {
       total_sp = (int)(total_sp + total_sp * 0.2D);
     }

     if (this.character != null && this.character.isPassive(MJPassiveID.IllUSION_LICH_PASSIVE.toInt())) {
       total_sp += 4;
     }
     return total_sp;
   }

   public int getTrueSp() {
     return getMagicLevel() + getMagicBonus();
   }

   public byte getStr() {
     return this.str;
   }

   public int getMagicLevel() {
     if (this.character instanceof L1PcInstance && ((L1PcInstance)this.character).getClassFeature() != null)
       return ((L1PcInstance)this.character).getClassFeature().getMagicLevel(this.character.getLevel());
     return this.character.getLevel() / 4;
   }

   public int getMagicBonus() {
     int i = getTotalInt();
     return CalcStat.calcMagicBonus(i);
   }







   public void setStr(int i) {
     this.str = checkRange(i, this.baseStr);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Str(true);
       }
     }
   }

   public void addStr(int i) {
     setStr(getStr() + i);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Str(true);
       }
     }
   }

   public byte getBaseStr() {
     return this.baseStr;
   }

   public void addBaseStr(int i) {
     setBaseStr(getBaseStr() + i);
   }

   public void setBaseStr(int i) {
     byte newBaseStr = checkRange(i);
     addStr(newBaseStr - this.baseStr);
     this.baseStr = newBaseStr;
   }

   public byte getAddedStr() {
     return this.addedStr;
   }

   public void addAddedStr(int i) {
     this.addedStr = checkRange(this.addedStr + i, -128);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Str(false);
       }
     }
   }

   public byte getTotalStr() {
     return checkRange(getStr() + getAddedStr());
   }

   public byte getCon() {
     return this.con;
   }

   public void setCon(int i) {
     this.con = checkRange(i, this.baseCon);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Con();
       }
     }
   }

   public void addCon(int i) {
     setCon(getCon() + i);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Con();
       }
     }
   }

   public byte getBaseCon() {
     return this.baseCon;
   }

   public void addBaseCon(int i) {
     setBaseCon(getBaseCon() + i);
   }

   public void setBaseCon(int i) {
     byte newBaseCon = checkRange(i);
     addCon(newBaseCon - this.baseCon);
     this.baseCon = newBaseCon;
   }

   public byte getAddedCon() {
     return this.addedCon;
   }

   public void addAddedCon(int i) {
     this.addedCon = checkRange(this.addedCon + i, -128);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Con();
       }
     }
   }

   public byte getTotalCon() {
     return checkRange(getCon() + getAddedCon());
   }

   public byte getDex() {
     return this.dex;
   }

   public void setDex(int i) {
     this.dex = checkRange(i, this.baseDex);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Dex(true);
       }
     }
   }

   public void addDex(int i) {
     setDex(getDex() + i);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Dex(false);
       }
     }
   }

   public byte getBaseDex() {
     return this.baseDex;
   }

   public void addBaseDex(int i) {
     setBaseDex(getBaseDex() + i);
   }

   public void setBaseDex(int i) {
     byte newBaseDex = checkRange(i);
     addDex(newBaseDex - this.baseDex);
     this.baseDex = newBaseDex;
   }

   public byte getAddedDex() {
     return this.addedDex;
   }

   public void addAddedDex(int i) {
     this.addedDex = checkRange(this.addedDex + i, -128);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Dex(false);
       }
     }
   }

   public byte getTotalDex() {
     return checkRange(getDex() + getAddedDex());
   }

   public byte getCha() {
     return this.cha;
   }

   public void setCha(int i) {
     this.cha = checkRange(i, this.baseCha);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Cha();
       }
     }
   }

   public void addCha(int i) {
     setCha(getCha() + i);
   }

   public byte getBaseCha() {
     return this.baseCha;
   }

   public void addBaseCha(int i) {
     setBaseCha(getBaseCha() + i);
   }

   public void setBaseCha(int i) {
     byte newBaseCha = checkRange(i);
     addCha(newBaseCha - this.baseCha);
     this.baseCha = newBaseCha;
   }

   public byte getAddedCha() {
     return this.addedCha;
   }

   public void addAddedCha(int i) {
     this.addedCha = checkRange(this.addedCha + i, -128);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Cha();
       }
     }
   }

   public byte getTotalCha() {
     return checkRange(getCha() + getAddedCha());
   }

   public byte getInt() {
     return this.intel;
   }

   public void setInt(int i) {
     this.intel = checkRange(i, this.baseInt);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Int();
       }
     }
   }

   public void addInt(int i) {
     setInt(getInt() + i);
   }

   public byte getBaseInt() {
     return this.baseInt;
   }

   public void addBaseInt(int i) {
     setBaseInt(getBaseInt() + i);
   }

   public void setBaseInt(int i) {
     byte newBaseInt = checkRange(i);
     addInt(newBaseInt - this.baseInt);
     this.baseInt = newBaseInt;
   }

   public byte getAddedInt() {
     return this.addedInt;
   }

   public void addAddedInt(int i) {
     this.addedInt = checkRange(this.addedInt + i, -128);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Int();
       }
     }
   }

   public byte getTotalInt() {
     return checkRange(getInt() + getAddedInt());
   }

   public byte getWis() {
     return this.wis;
   }

   public void setWis(int i) {
     this.wis = checkRange(i, this.baseWis);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Wis();
       }
     }
   }

   public void addWis(int i) {
     setWis(getWis() + i);
   }

   public byte getBaseWis() {
     return this.baseWis;
   }

   public void addBaseWis(int i) {
     setBaseWis(getBaseWis() + i);
   }

   public void setBaseWis(int i) {
     byte newBaseWis = checkRange(i);
     addWis(newBaseWis - this.baseWis);
     this.baseWis = newBaseWis;
   }

   public byte getAddedWis() {
     return this.addedWis;
   }

   public void addAddedWis(int i) {
     this.addedWis = checkRange(this.addedWis + i, -128);
     if (this.character instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)this.character;
       if (pc.getNetConnection() != null) {
         pc.Stat_Reset_Wis();
       }
     }
   }

   public byte getTotalWis() {
     return checkRange(getWis() + getAddedWis());
   }

   public int[] getBaseStatDiff(int[] value) {
     int[] returnValue = new int[6];
     returnValue[0] = getBaseStr() - value[0];
     returnValue[1] = getBaseDex() - value[1];
     returnValue[2] = getBaseCon() - value[2];
     returnValue[3] = getBaseWis() - value[3];
     returnValue[4] = getBaseCha() - value[4];
     returnValue[5] = getBaseInt() - value[5];

     return returnValue;
   }

   public int[] getMinStat(int classId) {
     int[] minabllity = new int[6];

     switch (classId) {
       case 0:
       case 1:
         minabllity[0] = 13;
         minabllity[1] = 9;
         minabllity[2] = 11;
         minabllity[3] = 11;
         minabllity[4] = 13;
         minabllity[5] = 9;
         break;
       case 48:
       case 20553:
         minabllity[0] = 16;
         minabllity[1] = 12;
         minabllity[2] = 16;
         minabllity[3] = 9;
         minabllity[4] = 10;
         minabllity[5] = 8;
         break;
       case 20278:
       case 20279:
         minabllity[0] = 8;
         minabllity[1] = 7;
         minabllity[2] = 12;
         minabllity[3] = 14;
         minabllity[4] = 8;
         minabllity[5] = 14;
         break;
       case 37:
       case 138:
         minabllity[0] = 10;
         minabllity[1] = 12;
         minabllity[2] = 12;
         minabllity[3] = 12;
         minabllity[4] = 9;
         minabllity[5] = 12;
         break;
       case 2786:
       case 2796:
         minabllity[0] = 15;
         minabllity[1] = 12;
         minabllity[2] = 12;
         minabllity[3] = 10;
         minabllity[4] = 8;
         minabllity[5] = 11;
         break;
       case 6658:
       case 6661:
         minabllity[0] = 13;
         minabllity[1] = 11;
         minabllity[2] = 14;
         minabllity[3] = 10;
         minabllity[4] = 8;
         minabllity[5] = 10;
         break;
       case 6650:
       case 6671:
         minabllity[0] = 9;
         minabllity[1] = 10;
         minabllity[2] = 12;
         minabllity[3] = 14;
         minabllity[4] = 8;
         minabllity[5] = 12;
         break;
       case 20567:
       case 20577:
         minabllity[0] = 16;
         minabllity[1] = 13;
         minabllity[2] = 16;
         minabllity[3] = 7;
         minabllity[4] = 9;
         minabllity[5] = 10;
         break;
       case 18499:
       case 18520:
         minabllity[0] = 16;
         minabllity[1] = 13;
         minabllity[2] = 15;
         minabllity[3] = 11;
         minabllity[4] = 5;
         minabllity[5] = 11;
         break;
     }


     minabllity = getBaseStatDiff(minabllity);
     return minabllity;
   }



   public int getHunt() {
     return this._Hunt;
   }

   public void setHunt(int i) {
     this._Hunt = i;
   }

   public void addHunt(int i) {
     setHunt(getHunt() + i);
   }



   public int getSurvival() {
     return this._Survival;
   }

   public void setSurvival(int i) {
     this._Survival = i;
   }

   public void addSurvival(int i) {
     setSurvival(getSurvival() + i);
   }



   public int getSacred() {
     return this._Sacred;
   }

   public void setSacred(int i) {
     this._Sacred = i;
   }

   public void addSacred(int i) {
     setSacred(getSacred() + i);
   }
 }



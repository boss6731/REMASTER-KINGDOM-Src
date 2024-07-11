     package l1j.server.server.serverpackets;

     import l1j.server.server.GameClient;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.utils.CalcStat;



     public class S_CharStat
       extends ServerBasePacket
     {
       private static final String S_CharStat = "[S] S_CharCreateSetting";
       public static final int Str = 1;
       public static final int Int = 2;
       public static final int Wis = 3;
       public static final int Dex = 4;
       public static final int Con = 5;
       public static final int Cha = 6;
       public static final int Stat_Str = 48;
       public static final int Stat_Int = 56;
       public static final int Stat_Wis = 64;
       public static final int Stat_Dex = 72;
       public static final int Stat_Con = 80;
       public static final int Stat_Cha = 88;
       public static final int STAT_REFRESH = 234;
       public static final int STAT_VIEW = 231;
       private static final byte[] MINUS_BYTES_1 = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, 1 };
       private static final byte[] MINUS_BYTES_2 = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, 1 };


       public S_CharStat(GameClient client, int type, int classType, int subType, int s, int i, int w, int d, int c, int cha) {
         int dmgup, hipup, critical, strweight, strsize, magicdmg, magichit, magiccritical, magicbonus, magicdecreasemp, intsize, mpr, mprpotion, statmr, minmp, maxmp, baseMaxMp, wissize, bowdmg, bowhitup, bowcritical, dexac, dexer, dexsize, hpr, hprpotion, conweight, purehp, baseHp, consize, pierceall, decreasecooltime, decreaseccduration, chasize, value = 0;
         value = subType * 2;
         writeC(19);
         writeC(227);
         writeC(1);
         writeC(8);
         writeC(value);
         switch (type) {
           case 1:
             dmgup = CalcStat.calcDmgup(s);
             hipup = CalcStat.calcHitup(s);
             critical = CalcStat.calcDmgCritical(classType, s);
             strweight = 0;
             if (client.getActiveChar() == null) {
               strweight = CalcStat.getMaxWeight(client.charStat[0], client.charStat[4]);
             }
             else if (value == 32) {
               strweight = CalcStat.getMaxWeight(client.charStat[0], client.getActiveChar().getAbility().getTotalCon());
             } else {
               strweight = CalcStat.getMaxWeight(client.getActiveChar().getAbility().getTotalStr(), client.getActiveChar().getAbility().getTotalCon());
             }

             strsize = size7B(dmgup) + size7B(hipup) + size7B(critical) + size7B(strweight) + 4;
             writeC(18);
             writeC(strsize);
             writeC(8);
             write7B(dmgup);
             writeC(16);
             write7B(hipup);
             writeC(24);
             write7B(critical);
             writeC(32);
             write7B(strweight);
             break;
           case 2:
             magicdmg = CalcStat.calcMagicDmg(i);
             magichit = CalcStat.calcMagicHitUp(i);
             magiccritical = CalcStat.calcMagicCritical(i);
             magicbonus = CalcStat.calcMagicBonus(i);
             magicdecreasemp = CalcStat.calcDecreaseMp(i);
             intsize = size7B(magicdmg) + size7B(magichit) + size7B(magiccritical) + size7B(magicbonus) + size7B(magicdecreasemp) + 5;
             writeC(26);
             writeC(intsize);
             writeC(8);
             write7B(magicdmg);
             writeC(16);
             write7B(magichit);
             writeC(24);
             write7B(magiccritical);
             writeC(32);
             write7B(magicbonus);
             writeC(40);
             write7B(magicdecreasemp);
             break;
           case 3:
             mpr = CalcStat.calcMpr(w);
             mprpotion = CalcStat.calcMprPotion(w);
             statmr = CalcStat.calcStatMr(classType, w);
             minmp = CalcStat.MinincreaseMp(classType, w);
             maxmp = CalcStat.MaxincreaseMp(classType, w);
             baseMaxMp = (client.getActiveChar() == null) ? 0 : client.getActiveChar().getBaseMaxMp();
             wissize = size7B(mpr) + size7B(mprpotion) + size7B(statmr) + size7B(minmp) + size7B(maxmp) + size7B(baseMaxMp) + 8;
             writeC(34);
             writeC(wissize);
             writeC(8);
             write7B(mpr);
             writeC(16);
             write7B(mprpotion);
             writeC(24);
             write7B(statmr);
             writeC(32);
             write7B(minmp);
             writeC(40);
             write7B(maxmp);
             writeC(48);
             writeC(0);
             writeC(56);
             writeBit(baseMaxMp);
             break;
           case 4:
             bowdmg = CalcStat.calcBowDmgup(d);
             bowhitup = CalcStat.calcBowHitup(d);
             bowcritical = CalcStat.calcBowCritical(d);
             dexac = CalcStat.calcAc(d);
             dexer = CalcStat.ER(d);
             dexsize = size7B(bowdmg) + size7B(bowhitup) + size7B(bowcritical) + size7B(dexac) + size7B(dexer) + 5;
             writeC(42);
             writeC(dexsize);
             writeC(8);
             write7B(bowdmg);
             writeC(16);
             write7B(bowhitup);
             writeC(24);
             write7B(bowcritical);
             writeC(32);
             write7B(dexac);
             writeC(40);
             write7B(dexer);
             break;
           case 5:
             hpr = CalcStat.calcHpr(c);
             hprpotion = CalcStat.calcHprPotion(c);
             conweight = 0;
             if (client.getActiveChar() == null) {
               conweight = CalcStat.getMaxWeight(client.charStat[0], client.charStat[4]);
             }
             else if (value == 32) {
               conweight = CalcStat.getMaxWeight(client.getActiveChar().getAbility().getTotalStr(), client.charStat[4]);
             } else {
               conweight = CalcStat.getMaxWeight(client.getActiveChar().getAbility().getTotalStr(), client.getActiveChar().getAbility().getTotalCon());
             }

             purehp = CalcStat.PureHp(classType, c);
             baseHp = (client.getActiveChar() == null) ? 0 : client.getActiveChar().getBaseMaxHp();
             consize = size7B(hpr) + size7B(hprpotion) + size7B(conweight) + size7B(purehp) + size7B(baseHp) + 8;
             writeC(50);
             writeC(consize);
             writeC(8);
             write7B(hpr);
             writeC(16);
             write7B(hprpotion);
             writeC(24);
             write7B(conweight);
             writeC(32);
             write7B(purehp);
             writeC(40);
             writeC(0);
             writeC(48);
             writeBit(baseHp);
             break;
           case 6:
             pierceall = CalcStat.calcPurePierceAll(cha);
             decreasecooltime = CalcStat.calcPureDecreaseCoolTime(cha);
             decreaseccduration = CalcStat.calcPureDecreaseCCDuration(cha);
             chasize = size7B(pierceall) + size7B(decreasecooltime) + size7B(decreaseccduration) + 6;
             writeC(58);
             writeC(chasize);
             writeC(8);
             writeC(1);
             writeC(16);
             write7B(pierceall);

             writeC(24);
             write7B(decreasecooltime);
             writeC(32);
             write7B(decreaseccduration);
             break;
         }


         writeH(0);
       }
       public S_CharStat(L1PcInstance pc, int settingType, int statType) {
         int calcDmg, calcHit, calcCritical, StatStrsize, calcMagicDmg, calcMagicHit, calcMagicCri, calcMagicBonus, calcMagicDecmp, StatIntsize, calcMpr, calcMprpotion, calcstatMr, calcMinmp, calcMaxmp, baseMaxMp, StatWissize, calcBowDmg, calcBowHit, calcBowCri, calcDexAc, calcDexEr, StatDexsize, calcHpr, calcHprpotion, calcPurehp, baseHp, StatConsize, pierceall, decreasecooltime, decreaseccduration, StatChasize;
         writeC(19);
         writeC(227);
         writeH(2049);
         writeC(settingType);
         int ChaStr = pc.getAbility().getTotalStr();
         int ChaInt = pc.getAbility().getTotalInt();
         int ChaWis = pc.getAbility().getTotalWis();
         int ChaDex = pc.getAbility().getTotalDex();
         int ChaCon = pc.getAbility().getTotalCon();
         int ChaCha = pc.getAbility().getTotalCon();
         int CalcWeight = CalcStat.getMaxWeight(pc.getAbility().getTotalStr(), pc.getAbility().getTotalCon());
         switch (statType) {
           case 48:
             calcDmg = CalcStat.calcDmgup(ChaStr);
             calcHit = CalcStat.calcHitup(ChaStr);
             calcCritical = CalcStat.calcDmgCritical(pc.getClassNumber(), ChaStr);

             StatStrsize = size7B(calcDmg) + size7B(calcHit) + size7B(calcCritical) + size7B(CalcWeight) + 4;
             writeC(18);
             writeC(StatStrsize);
             writeC(8);
             write7B(calcDmg);
             writeC(16);
             write7B(calcHit);
             writeC(24);
             write7B(calcCritical);
             writeC(32);
             write7B(CalcWeight);
             break;
           case 56:
             calcMagicDmg = CalcStat.calcMagicDmg(ChaInt);
             calcMagicHit = CalcStat.calcMagicHitUp(ChaInt);
             calcMagicCri = CalcStat.calcMagicCritical(ChaInt);
             calcMagicBonus = CalcStat.calcMagicBonus(ChaInt);
             calcMagicDecmp = CalcStat.calcDecreaseMp(ChaInt);
             StatIntsize = size7B(calcMagicDmg) + size7B(calcMagicHit) + size7B(calcMagicCri) + size7B(calcMagicBonus) + size7B(calcMagicDecmp) + 5;
             writeC(26);
             writeC(StatIntsize);
             writeC(8);
             write7B(calcMagicDmg);
             writeC(16);
             write7B(calcMagicHit);
             writeC(24);
             write7B(calcMagicCri);
             writeC(32);
             write7B(calcMagicBonus);
             writeC(40);
             write7B(calcMagicDecmp);
             break;
           case 64:
             calcMpr = CalcStat.calcMpr(ChaWis);
             calcMprpotion = CalcStat.calcMprPotion(ChaWis);
             calcstatMr = CalcStat.calcStatMr(pc.getType(), ChaWis);
             calcMinmp = CalcStat.MinincreaseMp(pc.getType(), ChaWis);
             calcMaxmp = CalcStat.MaxincreaseMp(pc.getType(), ChaWis);
             baseMaxMp = pc.getBaseMaxMp();
             StatWissize = size7B(calcMpr) + size7B(calcMprpotion) + size7B(calcstatMr) + size7B(calcMinmp) + size7B(calcMaxmp) + size7B(baseMaxMp) + 8;
             writeC(34);
             writeC(StatWissize);
             writeC(8);
             write7B(calcMpr);
             writeC(16);
             write7B(calcMprpotion);
             writeC(24);
             write7B(calcstatMr);
             writeC(32);
             write7B(calcMinmp);
             writeC(40);
             write7B(calcMaxmp);
             writeC(48);
             writeC(0);
             writeC(56);
             writeBit(baseMaxMp);
             break;
           case 72:
             calcBowDmg = CalcStat.calcBowDmgup(ChaDex);
             calcBowHit = CalcStat.calcBowHitup(ChaDex);
             calcBowCri = CalcStat.calcBowCritical(ChaDex);
             calcDexAc = CalcStat.calcAc(ChaDex);
             calcDexEr = CalcStat.ER(ChaDex);
             StatDexsize = size7B(calcBowDmg) + size7B(calcBowHit) + size7B(calcBowCri) + size7B(calcDexAc) + size7B(calcDexEr) + 5;
             writeC(42);
             writeC(StatDexsize);
             writeC(8);
             write7B(calcBowDmg);
             writeC(16);
             write7B(calcBowHit);
             writeC(24);
             write7B(calcBowCri);
             writeC(32);
             write7B(calcDexAc);
             writeC(40);
             write7B(calcDexEr);
             break;
           case 80:
             calcHpr = CalcStat.calcHpr(ChaCon);
             calcHprpotion = CalcStat.calcHprPotion(ChaCon);
             calcPurehp = CalcStat.PureHp(pc.getType(), ChaCon);
             baseHp = pc.getBaseMaxHp();
             StatConsize = size7B(calcHpr) + size7B(calcHprpotion) + size7B(CalcWeight) + size7B(calcPurehp) + size7B(baseHp) + 8;
             writeC(50);
             writeC(StatConsize);
             writeC(8);
             write7B(calcHpr);
             writeC(16);
             write7B(calcHprpotion);
             writeC(24);
             write7B(CalcWeight);
             writeC(32);
             write7B(calcPurehp);
             writeC(40);
             writeC(0);
             writeC(48);
             writeBit(baseHp);
             break;
           case 88:
             pierceall = CalcStat.calcPurePierceAll(ChaCha);
             decreasecooltime = CalcStat.calcPureDecreaseCoolTime(ChaCha);
             decreaseccduration = CalcStat.calcPureDecreaseCCDuration(ChaCha);
             StatChasize = size7B(pierceall) + size7B(decreasecooltime) + size7B(decreaseccduration) + 5;
             writeC(58);
             writeC(StatChasize);
             writeC(8);
             writeC(1);
             writeC(16);
             write7B(pierceall);

             writeC(24);
             write7B(decreasecooltime);
             writeC(32);
             write7B(decreaseccduration);
             break;
         }


         writeH(0);
       }


       public S_CharStat(L1PcInstance pc, int code) {
         writeC(19);
         writeC(code);
         switch (code) {
           case 234:
             writeC(1);
             writeC(8);
             writeC(pc.getAbility().getStr());
             writeC(16);
             writeC(pc.getAbility().getInt());
             writeC(24);
             writeC(pc.getAbility().getWis());
             writeC(32);
             writeC(pc.getAbility().getDex());
             writeC(40);
             writeC(pc.getAbility().getCon());
             writeC(48);
             writeC(pc.getAbility().getCha());
             writeH(0);
             break;
         }
       }
       public S_CharStat(int code, int value) {
         int shortDmg, shortHit, shortCri, strLength, magicDmg, magicHit, magicCri, intiLength, mpr, mpBonus, mp, wisLength, longDmg, longHit, longCri, dexLength, hpr, hpBonus, hp, conLength, allHit, coolTime, duration, chaLength;
         writeC(19);
         writeH(code);

         switch (code) {
           case 231:
             if (value == 12) {
               writeC(10);
               writeC(11);
               writeC(8);
               writeByte(MINUS_BYTES_2);
               writeC(18);
               writeC(11);
               writeC(8);
               writeByte(MINUS_BYTES_2);
               writeC(26);
               writeC(11);
               writeC(8);
               writeByte(MINUS_BYTES_2);

               writeC(34);
               writeC(11);
               writeC(8);
               writeByte(MINUS_BYTES_2);

               writeC(42);
               writeC(11);
               writeC(8);
               writeByte(MINUS_BYTES_2);

               writeC(50);
               writeC(8);
               writeC(8);
               writeC(value);
               writeC(16);
               writeC(1);
               writeC(24);
               writeC(100);
               writeC(32);
               writeC(100); break;
             }
             shortDmg = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             shortHit = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             shortCri = (value == 60) ? 5 : ((value == 55) ? 2 : ((value == 45) ? 1 : 0));
             strLength = 3 + getBitSize(value) + getBitSize(shortDmg) + getBitSize(shortHit) + ((shortCri > 0) ? (getBitSize(shortCri) + 1) : 0);
             writeC(10);
             writeC(strLength);
             writeC(8);
             writeC(value);
             writeC(16);
             writeC(shortDmg);
             writeC(24);
             writeC(shortHit);
             if (shortCri > 0) {
               writeC(32);
               writeC(shortCri);
             }

             magicDmg = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             magicHit = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             magicCri = (value == 60) ? 5 : ((value == 55) ? 2 : ((value == 45) ? 1 : 0));
             intiLength = 3 + getBitSize(value) + getBitSize(magicDmg) + getBitSize(magicHit) + ((magicCri > 0) ? (getBitSize(magicCri) + 1) : 0);
             writeC(18);
             writeC(intiLength);
             writeC(8);
             writeC(value);
             writeC(16);
             writeC(magicDmg);
             writeC(24);
             writeC(magicHit);
             if (magicCri > 0) {
               writeC(32);
               writeC(magicCri);
             }

             mpr = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             mpBonus = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             mp = (value == 60) ? 5 : ((value == 55) ? 200 : ((value == 45) ? 150 : ((value == 35) ? 100 : 50)));
             wisLength = 4 + getBitSize(value) + getBitSize(mpr) + getBitSize(mpBonus) + getBitSize(mp);
             writeC(26);
             writeC(wisLength);
             writeC(8);
             writeC(value);
             writeC(16);
             writeC(mpr);
             writeC(24);
             writeC(mpBonus);
             writeC(56);
             writeBit(mp);


             longDmg = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             longHit = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             longCri = (value == 60) ? 5 : ((value == 55) ? 2 : ((value == 45) ? 1 : 0));
             dexLength = 3 + getBitSize(value) + getBitSize(longDmg) + getBitSize(longHit) + ((longCri > 0) ? (getBitSize(longCri) + 1) : 0);
             writeC(34);
             writeC(dexLength);
             writeC(8);
             writeC(value);
             writeC(16);
             writeC(longDmg);
             writeC(24);
             writeC(longHit);
             if (longCri > 0) {
               writeC(32);
               writeC(longCri);
             }


             hpr = (value == 60) ? 5 : ((value == 55) ? 5 : ((value == 45) ? 3 : 1));
             hpBonus = (value == 60) ? 4 : ((value == 55) ? 4 : ((value == 45) ? 2 : ((value == 35) ? 1 : 0)));
             hp = (value == 60) ? 200 : ((value == 55) ? 200 : ((value == 45) ? 150 : ((value == 35) ? 100 : 50)));
             conLength = 3 + getBitSize(value) + getBitSize(hpr) + getBitSize(hp) + ((hpBonus > 0) ? (getBitSize(hpBonus) + 1) : 0);
             writeC(42);
             writeC(conLength);
             writeC(8);
             writeC(value);
             writeC(16);
             writeC(hpr);
             if (hpBonus > 0) {
               writeC(24);
               writeC(hpBonus);
             }
             writeC(48);
             writeBit(hp);


             allHit = (value == 60) ? 1 : ((value == 55) ? 1 : ((value == 45) ? 1 : ((value == 35) ? 1 : 1)));
             coolTime = (value == 60) ? 100 : ((value == 25) ? 100 : 100);
             duration = (value == 60) ? 100 : ((value == 25) ? 100 : 100);
             chaLength = 1 + getBitSize(value) + ((allHit > 0) ? (getBitSize(allHit) + 1) : 0) + ((coolTime > 0) ? (getBitSize(coolTime) + 1) : 0) + ((duration > 0) ? (getBitSize(duration) + 1) : 0);
             writeC(50);
             writeC(chaLength);
             writeC(8);
             writeC(value);
             if (allHit > 0) {
               writeC(16);
               writeC(allHit);
             }
             if (coolTime > 0) {
               writeC(24);
               writeBit(coolTime);
             }
             if (duration > 0) {
               writeC(32);
               writeBit(duration);
             }
             break;
         }

         writeH(0);
       }



       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_CharCreateSetting";
       }
     }



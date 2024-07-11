 package l1j.server.server.serverpackets;

 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1StatReset;



 public class S_ReturnedStat
   extends ServerBasePacket
 {
   private static final String S_ReturnedStat = "[S] S_ReturnedStat";
   private byte[] _byte = null;

   public static final int START = 1;

   public static final int LEVELUP = 2;

   public static final int END = 3;

   public static final int LOGIN = 4;
   public static final int PET_PARTY = 12;
   public static final int Unknown_LOGIN2 = 68;
   public static final int RING_RUNE_SLOT = 67;
   public static final int UI4 = 68;
   public static final int UI5 = 65;
   public static final int PowerBook搜索 = 25;
   public static final int CharNameChange = 29;
   public static final int ELIXER = 76;
   public static final int STATE_1 = 15;
   public static final int BOOKMARK = 42;
   public static final int CLAN_JOIN_LEAVE = 60;
   public static final int SUBTYPE_RING = 1;
   public static final int SUBTYPE_RUNE = 2;
   public static final int OPEN_SLOT_LRING = 4;
   public static final int OPEN_SLOT_RRING = 8;
   public static final int OPEN_SLOT_EARRING = 16;
   public static final int OPEN_SLOT_SHOULD = 64;
   public static final int OPEN_SLOT_BADGE = 128;
   public static final int OPEN_SLOT_LRING95 = 256;
   public static final int OPEN_SLOT_RRING100 = 512;
   public static final int OPEN_SLOT_LEARRING101 = 1024;
   public static final int OPEN_SLOT_REARRING103 = 2048;
   public static final int OPEN_SLOT_ALL = 988;

   public static S_ReturnedStat remainStatusPoint(int remainStatusPoint) {
     S_ReturnedStat pck = new S_ReturnedStat();
     pck.writeC(remainStatusPoint);
     pck.writeD(2);
     return pck;
   }

   private S_ReturnedStat() {
     writeC(43);
   }

   public S_ReturnedStat(int type) {
     writeC(43);
     writeC(type);
     switch (type) {


       case 68:
         writeD(1);
         writeC(12);
         writeH(2240);
         break;
       case 65:
         writeC(0);
         writeC(147);
         writeC(246);
         break;
     }
   }





   public S_ReturnedStat(L1PcInstance pc, int type) {
     buildPacket(pc, type);
   }


   public S_ReturnedStat(int itemId, boolean eq) {
     writeC(43);
     writeC(66);
     writeD(itemId);
     writeC(9);
     writeC(eq ? 1 : 0);
   }

   public S_ReturnedStat(int subCode, String val) {
     writeC(43);
     writeC(subCode);
     switch (subCode) {
       case 25:
         writeC(0);
         writeD(740598182);
         writeD(1177300544);
         writeD(274102657);
         writeD(1920408120);
         writeS(val);
         break;
     }
   }



   public S_ReturnedStat(L1StatReset sr) {
     writeC(43);
     writeC(2);
     writeC(sr.getNowLevel());
     writeC(sr.getEndLevel());
     writeH(sr.getMaxHp());
     writeH(sr.getMaxMp());
     writeH(sr.getAC());
     writeC(sr.getStr());
     writeC(sr.getIntel());
     writeC(sr.getWis());
     writeC(sr.getDex());
     writeC(sr.getCon());
     writeC(sr.getCha());
   }


   public S_ReturnedStat(int type, int count, int id, boolean ck) {
     writeC(43);
     writeC(type);
     switch (type) {
       case 12:
         if (ck) {
           writeC(count);
           writeC(0);
           writeD(0);
         } else {
           writeC(count);
           writeC(0);
           writeC(1);
           writeC(0);
           writeC(0);
           writeC(0);
         }
         writeD(id);
         break;
     }
   }



   public S_ReturnedStat(int type, int subType, int value) {
     writeC(43);
     writeC(type);

     switch (type) {
       case 67:
         writeD(subType);
         if (subType == 1) {
           if (value == 2) {
             value = 15;
           } else if (value == 1) {
             value = 7;
           } else if (value == 0) {
             value = 3;
           }  writeC(value);
         } else if (subType == 2) {
           writeC(1);
         }
         writeD(0);
         writeD(0);
         writeD(0);
         writeD(0);
         writeD(0);
         writeD(0);
         writeH(0);
         break;
       case 68:
         writeD(1);
         writeC(988);
         writeH(0);
         break;
     }
   }


   public S_ReturnedStat(int pcObjId, int emblemId) {
     writeC(43);
     writeC(60);
     writeD(pcObjId);
     writeD(emblemId);
   }



   public S_ReturnedStat(L1PcInstance pc, int type, int c) {
     writeC(43);
     writeC(type);
     switch (type) {
       case 3:
         writeC(c);
         if (pc.getLevel() > pc.getHighLevel()) {
           pc.getNetConnection().kick();
         }
         break;

       case 76:
         writeC(c);
         break;
     }
   }

   private void buildPacket(L1PcInstance pc, int type) {
     short init_hp, init_mp;
     int minStat[], first, second, third;
     writeC(43);
     writeC(type);

     switch (type) {
       case 1:
         init_hp = 0;
         init_mp = 0;
         if (pc.isCrown()) {
           init_hp = 14;
           switch (pc.getAbility().getBaseWis()) {
             case 11:
               init_mp = 2;
               break;
             case 12:
             case 13:
             case 14:
             case 15:
               init_mp = 3;
               break;
             case 16:
             case 17:
             case 18:
               init_mp = 4;
               break;
             default:
               init_mp = 2;
               break;
           }
         } else if (pc.isKnight()) {
           init_hp = 16;
           switch (pc.getAbility().getBaseWis()) {
             case 9:
             case 10:
             case 11:
               init_mp = 1;
               break;
             case 12:
             case 13:
               init_mp = 2;
               break;
             default:
               init_mp = 1;
               break;
           }
         } else if (pc.isElf()) {
           init_hp = 15;
           switch (pc.getAbility().getBaseWis()) {
             case 12:
             case 13:
             case 14:
             case 15:
               init_mp = 4;
               break;
             case 16:
             case 17:
             case 18:
               init_mp = 6;
               break;
             default:
               init_mp = 4;
               break;
           }
         } else if (pc.isWizard()) {
           init_hp = 12;
           switch (pc.getAbility().getBaseWis()) {
             case 12:
             case 13:
             case 14:
             case 15:
               init_mp = 6;
               break;
             case 16:
             case 17:
             case 18:
               init_mp = 8;
               break;
             default:
               init_mp = 6;
               break;
           }
         } else if (pc.isDarkelf()) {
           init_hp = 12;
           switch (pc.getAbility().getBaseWis()) {
             case 10:
             case 11:
               init_mp = 3;
               break;
             case 12:
             case 13:
             case 14:
             case 15:
               init_mp = 4;
               break;
             case 16:
             case 17:
             case 18:
               init_mp = 6;
               break;
             default:
               init_mp = 3;
               break;
           }
         } else if (pc.isDragonknight()) {
           init_hp = 16;
           init_mp = 2;
         } else if (pc.isBlackwizard()) {
           init_hp = 14;
           switch (pc.getAbility().getBaseWis()) {
             case 10:
             case 11:
             case 12:
             case 13:
             case 14:
             case 15:
               init_mp = 5;
               break;
             case 16:
             case 17:
             case 18:
               init_mp = 6;
               break;
             default:
               init_mp = 5;
               break;
           }
         } else if (pc.is전사()) {
           init_hp = 16;
           if (pc.getAbility().getBaseCon() >= 19) {
             init_hp = (short)(init_hp + 2);
           } else if (pc.getAbility().getBaseCon() >= 17) {
             init_hp = (short)(init_hp + 1);
           }  switch (pc.getAbility().getBaseWis()) {
             case 9:
             case 10:
             case 11:
               init_mp = 1;
               break;
             case 12:
             case 13:
               init_mp = 2;
               break;
             default:
               init_mp = 1;
               break;
           }
         } else if (pc.isFencer()) {
           init_hp = 16;
           switch (pc.getAbility().getBaseWis()) {
             case 11:
               init_mp = 2;
               break;
             case 12:
             case 13:
               init_mp = 3;
               break;
             case 14:
             case 15:
               init_mp = 4;
               break;
             default:
               init_mp = 2;
               break;
           }
         } else if (pc.isLancer()) {
           init_hp = 18;
           switch (pc.getAbility().getBaseWis()) {
             case 11:
               init_mp = 2;
               break;
             case 12:
             case 13:
               init_mp = 3;
               break;
             case 14:
             case 15:
               init_mp = 4;
               break;
             default:
               init_mp = 2;
               break;
           }
         }
         writeH(init_hp);
         writeH(init_mp);
         writeC(10);
         writeC(ExpTable.getLevelByExp(pc.getReturnStat()));
         break;

       case 2:
         writeC(pc.rst.level);
         writeC(ExpTable.getLevelByExp(pc.getReturnStat()));
         writeH(pc.rst.baseHp + pc.rst.upHp);
         writeH(pc.rst.baseMp + pc.rst.upMp);
         writeH(pc.rst.ac);
         writeC(pc.rst.str + pc.rst.basestr);
         writeC(pc.rst.Int + pc.rst.baseint);
         writeC(pc.rst.wis + pc.rst.basewis);
         writeC(pc.rst.dex + pc.rst.basedex);
         writeC(pc.rst.con + pc.rst.basecon);
         writeC(pc.rst.cha + pc.rst.basecha);
         break;
       case 3:
         writeC(pc.getElixirStats());
         break;

       case 4:
         minStat = new int[6];
         minStat = pc.getAbility().getMinStat(pc.getClassId());
         first = minStat[0] + minStat[5] * 16;
         second = minStat[3] + minStat[1] * 16;
         third = minStat[2] + minStat[4] * 16;

         writeC(first);
         writeC(second);
         writeC(third);
         writeC(0);
         break;
       case 68:
         writeD(1);
         writeC(12);
         writeH(2240);
         break;
       case 65:
         writeC(0);
         writeH(45500);
         break;
       case 29:
         writeC(0);
         break;
       case 60:
         writeD(pc.getId());
         writeD(pc.getClanid());
         writeH(0);
         break;
     }
   }



   public S_ReturnedStat(L1PcInstance pc, int c, boolean f) {
     writeC(43);
     writeC(3);
     writeC(c);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_ReturnedStat";
   }
 }



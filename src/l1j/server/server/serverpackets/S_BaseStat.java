     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_BaseStat
       extends ServerBasePacket {
       private static final String _TYPE = "[S] S_BaseStat";
       private static final int[][] baseStat = new int[][] { { 13, 10, 10, 11, 10, 13 }, { 16, 12, 14, 9, 8, 12 }, { 11, 12, 12, 12, 12, 9 }, { 8, 7, 12, 12, 12, 8 }, { 12, 15, 8, 10, 11, 9 }, { 13, 11, 14, 12, 11, 8 }, { 11, 10, 12, 12, 12, 8 } };



       public S_BaseStat(L1PcInstance cha) {
         writeC(4);

         int value = cha.getAbility().getBaseCha() - baseStat[cha.getType()][5];
         value *= 16;
         value += cha.getAbility().getBaseCon() - baseStat[cha.getType()][2];
         value *= 16;
         value += cha.getAbility().getBaseDex() - baseStat[cha.getType()][1];
         value *= 16;
         value += cha.getAbility().getBaseWis() - baseStat[cha.getType()][3];
         value *= 16;
         value += cha.getAbility().getBaseInt() - baseStat[cha.getType()][4];
         value *= 16;
         value += cha.getAbility().getBaseStr() - baseStat[cha.getType()][0];
         writeD(value);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_BaseStat";
       }
     }



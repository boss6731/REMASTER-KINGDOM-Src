     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_Dexup extends ServerBasePacket {
       private static final String _S__25_S_Dexup = "[S] S_Dexup";

       public S_Dexup(L1PcInstance pc, int type, int time) {
         writeC(196);
         writeH(time);
         writeC(pc.getAbility().getTotalDex());
         writeC(0);
         writeD(type);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_Dexup";
       }
     }



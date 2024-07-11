 package l1j.server.server.serverpackets;

 public class S_TelePortUi
   extends ServerBasePacket
 {
   private static final String S_TelePortUi = "[S] S_TelePortUi";

   public S_TelePortUi(int objid, String[] action, int[] price, int map) {
     writeC(19);
     writeH(579);
     writeC(8);
     writeBit(objid);
     for (int i = 0; i < map; i++) {
       int length = bitlengh(price[i]) + 5;
       int totallen = length + (action[i].getBytes()).length + 4;
       writeC(18);
       writeBit(totallen);
       writeC(10);
       writeS2(action[i]);
       writeC(18);
       writeBit(length);
       writeH(1800);
       writeC(16);
       writeBit(price[i]);
       writeH(280);
     }
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_TelePortUi";
   }
 }



 package l1j.server.server.serverpackets;

 import l1j.server.server.model.L1NpcTalkData;





 public class S_NPCTalkActionTPUrl
   extends ServerBasePacket
 {
   private static final String _S__25_TalkReturnAction = "[S] S_NPCTalkActionTPUrl";

   public S_NPCTalkActionTPUrl(L1NpcTalkData cha, Object[] prices, int objid) {
     buildPacket(cha, prices, objid);
   }

   private void buildPacket(L1NpcTalkData npc, Object[] prices, int objid) {
     String htmlid = "";
     htmlid = npc.getTeleportURL();
     writeC(144);
     writeD(objid);
     writeS(htmlid);
     writeH(1);
     writeH(prices.length);

     for (Object price : prices) {
       writeS(String.valueOf(((Integer)price).intValue()));
     }
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_NPCTalkActionTPUrl";
   }
 }



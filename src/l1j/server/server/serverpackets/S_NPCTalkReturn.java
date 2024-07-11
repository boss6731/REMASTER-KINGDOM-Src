 package l1j.server.server.serverpackets;

 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.npc.L1NpcHtml;

 public class S_NPCTalkReturn
   extends ServerBasePacket
 {
   private static final String _S__25_TalkReturn = "[S] _S__25_TalkReturn";

   public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action, String[] data) {
     String htmlid = "";

     if (action == 1) {
       htmlid = npc.getNormalAction();
     } else if (action == 2) {
       htmlid = npc.getCaoticAction();
     } else {
       throw new IllegalArgumentException();
     }
     buildPacket(objid, htmlid, data);
   }

   public S_NPCTalkReturn(L1NpcTalkData npc, int objid, int action) {
     this(npc, objid, action, (String[])null);
   }

   public S_NPCTalkReturn(int objid, String htmlid, String[] data) {
     buildPacket(objid, htmlid, data);
   }

   public S_NPCTalkReturn(int objid, String htmlid) {
     buildPacket(objid, htmlid, (String[])null);
   }

   public S_NPCTalkReturn(int objid, L1NpcHtml html) {
     buildPacket(objid, html.getName(), html.getArgs());
   }
   public S_NPCTalkReturn(int objid, L1NpcHtml html, String[] data) {
     buildPacket(objid, html.getName(), data);
   }

   private void buildPacket(int objid, String htmlid, String[] data) {
     writeC(144);
     writeD(objid);
     writeS(htmlid);
     if (data != null && 1 <= data.length) {
       writeH(1);
       writeH(data.length);
       for (String datum : data) {
         writeS(datum);
       }
     } else {
       writeH(0);
       writeH(0);
     }
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] _S__25_TalkReturn";
   }
 }



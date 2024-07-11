     package l1j.server.server.serverpackets;

     import l1j.server.MJBotSystem.AI.MJBotAI;
     import l1j.server.MJBotSystem.MJBotType;
     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_Bot
       extends ServerBasePacket
     {
       private static final int[][] _gfx = new int[][] { { 5088, 5089 }, { 5090, 5091 }, { 5096, 5097 }, { 5094, 5095 }, { 5092, 5093 }, { 5098, 5099 }, { 5100, 5101 }, { 5229, 5229 } };


       public static S_Bot getBotChat(MJBotAI ai, String s) {
         S_Bot pck = new S_Bot();
         pck.writeC(19);
         pck.writeC(4);
         pck.writeC(2);
         pck.writeC(8);
         pck.writeC(0);
         pck.writeC(16);
         pck.writeC(0);
         pck.writeC(26);
         pck.writeS2(s);
         pck.writeC(42);
         pck.writeS2(ai.getBody().getName());
         pck.writeC(56);
         pck.writeBit(ai.getBody().getId());
         pck.writeC(64);
         pck.writeBit(ai.getBody().getX());
         pck.writeC(72);
         pck.writeBit(ai.getBody().getY());
         pck.writeH(0);
         return pck;
       }

       public static S_Bot getBotWorldChat(MJBotAI ai, String s) {
         S_Bot pck = new S_Bot();
         pck.writeC(19);
         pck.writeC(4);
         pck.writeC(2);
         pck.writeC(8);
         pck.writeC(0);
         pck.writeC(16);
         pck.writeC(3);
         pck.writeC(26);
         pck.writeS2(s);
         pck.writeC(42);
         pck.writeS2(ai.getBody().getName());
         pck.writeH(0);
         return pck;
       }

       public static S_Bot getBotWhisperChat(MJBotAI ai, String s) {
         S_Bot pck = new S_Bot();
         pck.writeC(19);
         pck.writeC(4);
         pck.writeC(2);
         pck.writeC(8);
         pck.writeC(0);
         pck.writeC(16);
         pck.writeC(1);
         pck.writeC(26);
         pck.writeS2(s);
         pck.writeC(42);
         pck.writeS2(ai.getBody().getName());
         pck.writeH(0);
         return pck;
       }

       public static S_Bot getBotList(L1PcInstance gm, MJBotType type) {
         S_Bot pck = new S_Bot();
         pck.writeC(127);
         pck.writeD(gm.getId());
         try {
           MJBotAI[] ais = type.toArray();
           if (ais == null || ais.length <= 0) {
             pck.writeH(0);
             pck.writeC(3);
             pck.writeC(0);
             return pck;
           }

           MJBotAI ai = null;
           pck.writeH(ais.length);
           pck.writeC(3);
           for (int i = 0; i < ais.length; i++) {
             ai = ais[i];
             if (ai == null) {
               pck.writeD(gm.getId());
               pck.writeC(0);
               pck.writeH(_gfx[gm.getType()][gm.get_sex()]);
               pck.writeC(1);
               pck.writeD(1);
               pck.writeC(1);
               pck.writeS("dummy");
               pck.writeC(0);
             }
             else {

               L1PcInstance body = ai.getBody();
               if (body == null)
               { pck.writeD(gm.getId());
                 pck.writeC(0);
                 pck.writeH(_gfx[gm.getType()][gm.get_sex()]);
                 pck.writeC(1);
                 pck.writeD(1);
                 pck.writeC(1);
                 pck.writeS("dummy");
                 pck.writeC(0); }
               else

               { pck.writeD(body.getId());
                 pck.writeC(0);
                 pck.writeH(_gfx[body.getType()][body.get_sex()]);
                 pck.writeC(1);
                 pck.writeD(1);
                 pck.writeC(1);
                 pck.writeS(body.getName());
                 pck.writeC(0); }
             }
           }  pck.writeD(100);
           pck.writeD(0);
           pck.writeH(0);
         } catch (Exception e) {
           e.printStackTrace();
         }
         return pck;
       }






       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "S_BotList";
       }
     }



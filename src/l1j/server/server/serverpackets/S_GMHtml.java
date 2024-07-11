     package l1j.server.server.serverpackets;

     public class S_GMHtml
       extends ServerBasePacket
     {
       public S_GMHtml(String sender, String sender2, String presentName, String receiver) {
         writeC(144);
         writeD(0);
         writeS("giftmessage");
         writeH(1);
         writeH(4);
         writeS(sender);
         writeS(sender2);
         writeS(presentName);
         writeS(receiver);
       }

       public S_GMHtml(String s1, String s2) {
         String[] board = new String[2];
         board[0] = s1;
         board[1] = s2;
         writeC(144);
         writeD(0);
         writeS("withdraw");
         if (board != null && 1 <= board.length) {
           writeH(1);
           writeH(board.length);
           for (String datum : board) {
             writeS(datum);
           }
         }
       }

       public S_GMHtml(int _objid, String html) {
         writeC(144);
         writeD(_objid);
         writeS("hsiw");
         writeS(html);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }



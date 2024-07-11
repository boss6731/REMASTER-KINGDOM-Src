     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.templates.L1BoardPost;


     public class S_BoardRead
       extends ServerBasePacket
     {
       private static final String S_BoardRead = "[S] S_BoardRead";
       private static final int CONTENT_MIN_LENGTH = 100;

       public S_BoardRead(L1NpcInstance board, int number) {
         if (board.getNpcId() == 4200015) {
           buildPacketNotice(board, number);
         } else if (board.getNpcId() == 4200020) {
           buildPacketNotice1(board, number);
         } else if (board.getNpcId() == 4200021) {
           buildPacketNotice2(board, number);
         } else if (board.getNpcId() == 4200022 || board.getNpcId() == 71008) {
           buildPacketNotice3(board, number);
         } else if (board.getNpcId() == 71008) {
           buildPacketNotice4(board, number);
         } else if (board.getNpcId() == 900006) {
           buildPacketKey(board, number);
         } else if (board.getNpcId() == 500002) {
           buildPacketPhone(board, number);
         } else {
           buildPacket(board, number);
         }
       }

       private void buildPacket(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findById(number);
         writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void buildPacketNotice(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findByIdGM(number);
         writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void buildPacketNotice1(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findByIdGM1(number);
         if (topic == null) {
           System.out.println(String.format("[L1BoardPost 錯誤] %d", new Object[] { Integer.valueOf(number) }));

           return;
         }
         writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void buildPacketNotice2(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findByIdGM2(number);
         writePacket(number, (topic.getName() != null) ? topic.getName() : "", topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void buildPacketNotice4(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findByIdGM3(number);
         writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void buildPacketNotice3(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findByIdGM3(number);
         writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void buildPacketPhone(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findByIdPhone(number);
         writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void buildPacketKey(L1NpcInstance board, int number) {
         L1BoardPost topic = L1BoardPost.findByIdKey(number);
         writePacket(number, topic.getName(), topic.getTitle(), topic.getDate(), topic.getContent());
       }

       private void writePacket(int number, String name, String title, String date, String content) {
         writeC(248);
         writeD(number);
         writeS(name);
         writeS(title);
         writeS(date);

         try {
           byte[] contentBytes = content.getBytes("MS949");
           int addedLen = 100 - contentBytes.length;
           writeByte(contentBytes);
           if (addedLen > 0) {
             byte[] paddings = new byte[addedLen];
             for (int i = addedLen - 1; i >= 0; i--)
               paddings[i] = 32;
             writeByte(paddings);
           }
           writeS(".");
           writeC(0);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_BoardRead";
       }
     }



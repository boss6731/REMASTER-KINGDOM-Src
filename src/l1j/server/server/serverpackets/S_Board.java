     package l1j.server.server.serverpackets;

     import java.util.List;
     import l1j.server.server.Controller.BugRaceController;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.templates.L1BoardPost;


     public class S_Board
       extends ServerBasePacket
     {
       private static final String S_BOARD = "[S] S_Board";
       private static final int TOPIC_LIMIT = 8;

       public S_Board(L1NpcInstance board) {
         switch (board.getNpcId()) {
           case 4200015:
             buildPacketNotice(board, 0);
             return;
           case 4200020:
             buildPacketNotice1(board, 0);
             return;
           case 4200021:
             buildPacketNotice2(board, 0);
             return;
           case 71008:
           case 4200022:
             buildPacketNotice3(board, 0);
             return;
           case 500002:
             buildPacketPhone(board, 0);
             return;
           case 900006:
             buildPacketKey(board, 0);
             return;
           case 999999:
             buildPacket1(board, 0);
             return;
           case 500001:
             buildPacket2(board, 0);
             return;
           case 4200013:
             buildPacket3(board, 0);
             return;
         }
         buildPacket(board, 0);
       }



       public S_Board(L1NpcInstance board, int number) {
         switch (board.getNpcId()) {
           case 4200015:
             buildPacketNotice(board, number);
             return;
           case 42000162:
             buildPacketNotice1(board, number);
             return;
           case 42000163:
             buildPacketNotice2(board, number);
             return;
           case 4200099:
             buildPacketNotice3(board, number);
             return;
           case 500002:
             buildPacketPhone(board, number);
             return;
           case 900006:
             buildPacketKey(board, number);
             return;
           case 999999:
             buildPacket1(board, number);
             return;
           case 500001:
             buildPacket2(board, number);
             return;
           case 4200013:
             buildPacket3(board, number);
             return;
         }
         buildPacket(board, number);
       }



       private void buildPacket1(L1NpcInstance board, int number) {
         writeC(144);
         writeD(board.getId());
         writeS("maeno4");
         writeC(0);
         writeH(15);
         for (int i = 0; i < 5; i++) {
           writeS((BugRaceController.getInstance())._littleBugBear[i].getName());
           writeS((BugRaceController.getInstance())._bugCondition[i]);
           writeS(Double.toString((BugRaceController.getInstance())._winRate[i]) + "%");
         }
       }
       private void buildPacket2(L1NpcInstance board, int number) {
         int count = 0;
         String[][] db = (String[][])null;
         int[] id = null;
         db = new String[9][3];
         id = new int[9];
         while (count < 9) {
           id[count] = count + 1;
           db[count][0] = "";
           db[count][1] = "";
           count++;
         }

         db[0][2] = "--------- 전     사";
         db[1][2] = "--------- 군     주";
         db[2][2] = "--------- 기     사";
         db[3][2] = "--------- 요     정";
         db[4][2] = "--------- 법     사";
         db[5][2] = "--------- 다     엘";
         db[6][2] = "--------- 용 기 사";
         db[7][2] = "--------- 환 술 사";
         writeC(152);
         writeC(0);
         writeD(board.getId());
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(127);
         writeH(9);
         writeH(300);
         for (int i = 0; i < 8; i++) {
           writeD(id[i]);
           writeS(db[i][0]);
           writeS(db[i][1]);
           writeS(db[i][2]);
         }
       }
         private void buildPacket3(L1NpcInstance board, int number) {
             int count = 0;
             String[][] db = null;
             int[] id = null;

             // 初始化数组
             db = new String[8][3];
             id = new int[8];

             // 填充数组内容
             while (count < 8) {
                 id[count] = count + 1;
                 db[count][0] = "錯誤監控"; // 每个元素第一个位置都填充 "버그감시"
                 db[count][1] = ""; // 每个元素第二个位置留空
                 count++;
             }

             // 为每个元素第三个位置填充特定内容
             db[0][2] = "1. 武器排名";      // 1. 武器排名
             db[1][2] = "2. 防具排名";    // 2. 防具排名
             db[2][2] = "3. 金幣排名";    // 3. 艾登排名
             db[3][2] = "4. 等级排名";    // 4. 等级排名
             db[4][2] = "5. 神秘羽毛排名"; // 5. 神秘羽毛排名
             db[5][2] = "6. 倉庫金幣排名"; // 6. 仓库艾登排名
             db[6][2] = "7. HP排名";       // 7. HP排名
             db[7][2] = "8. MP排名";       // 8. MP排名
         }


         writeC(152);

         writeC(0);
         writeD(board.getId());
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(127);
         writeH(8);
         writeH(300);
         for (int i = 0; i < 8; i++) {
           writeD(id[i]);
           writeS(db[i][0]);
           writeS(db[i][1]);
           writeS(db[i][2]);
         }
       }



       private void buildPacket(L1NpcInstance board, int number) {
         List<L1BoardPost> topics = L1BoardPost.index(number, 8);
         writeC(152);
         writeC(0);
         writeD(board.getId());
         if (number == 0) {
           writeD(2147483647);
         } else {
           writeD(number);
         }
         writeC(topics.size());
         if (number == 0) {
           writeC(0);
           writeH(300);
         }
         for (L1BoardPost topic : topics) {
           writeD(topic.getId());
           writeS(topic.getName());
           writeS(topic.getDate());
           writeS(topic.getTitle());
         }
       }
       private void buildPacketNotice(L1NpcInstance board, int number) {
         List<L1BoardPost> topics = L1BoardPost.indexGM(number, 8);
         writeC(152);
         writeC(0);
         writeD(board.getId());
         if (number == 0) {
           writeD(2147483647);
         } else {
           writeD(number);
         }
         writeC(topics.size());
         if (number == 0) {
           writeC(0);
           writeH(300);
         }
         for (L1BoardPost topic : topics) {
           writeD(topic.getId());
           writeS(topic.getName());
           writeS(topic.getDate());
           writeS(topic.getTitle());
         }
       }
       private void buildPacketNotice1(L1NpcInstance board, int number) {
         List<L1BoardPost> topics = L1BoardPost.indexGM1(number, 8);

         writeC(152);
         writeC(0);
         writeD(board.getId());
         if (number == 0) {
           writeD(2147483647);
         } else {
           writeD(number);
         }
         writeC(topics.size());
         if (number == 0) {
           writeC(0);
           writeH(300);
         }
         for (L1BoardPost topic : topics) {
           if (topic == null) {
             System.out.println(String.format("[L1BoardPost 錯誤] %d", new Object[] { Integer.valueOf(number) }));
             continue;
           }
           writeD(topic.getId());
           writeS(topic.getName());
           writeS(topic.getDate());
           writeS(topic.getTitle());
         }
       }
       private void buildPacketNotice2(L1NpcInstance board, int number) {
         List<L1BoardPost> topics = L1BoardPost.indexGM2(number, 8);
         writeC(152);
         writeC(0);
         writeD(board.getId());
         if (number == 0) {
           writeD(2147483647);
         } else {
           writeD(number);
         }
         writeC(topics.size());
         if (number == 0) {
           writeC(0);
           writeH(300);
         }
         for (L1BoardPost topic : topics) {

           writeD(topic.getId());
           writeS(topic.getName());
           writeS(topic.getDate());
           writeS(topic.getTitle());
         }
       }
       private void buildPacketNotice3(L1NpcInstance board, int number) {
         List<L1BoardPost> topics = L1BoardPost.indexGM3(number, 8);
         writeC(152);
         writeC(0);
         writeD(board.getId());
         if (number == 0) {
           writeD(2147483647);
         } else {
           writeD(number);
         }
         writeC(topics.size());
         if (number == 0) {
           writeC(0);
           writeH(300);
         }
         for (L1BoardPost topic : topics) {
           writeD(topic.getId());
           writeS(topic.getName());
           writeS(topic.getDate());
           writeS(topic.getTitle());
         }
       }
       private void buildPacketPhone(L1NpcInstance board, int number) {
         List<L1BoardPost> topics = L1BoardPost.indexPhone(number, 8);
         writeC(152);
         writeC(0);
         writeD(board.getId());
         if (number == 0) {
           writeD(2147483647);
         } else {
           writeD(number);
         }
         writeC(topics.size());
         if (number == 0) {
           writeC(0);
           writeH(300);
         }
         for (L1BoardPost topic : topics) {
           writeD(topic.getId());
           writeS(topic.getName());
           writeS(topic.getDate());
           writeS(topic.getTitle());
         }
       }

       private void buildPacketKey(L1NpcInstance board, int number) {
         List<L1BoardPost> topics = L1BoardPost.indexKey(number, 8);
         writeC(152);
         writeC(0);
         writeD(board.getId());
         if (number == 0) {
           writeD(2147483647);
         } else {
           writeD(number);
         }
         writeC(topics.size());
         if (number == 0) {
           writeC(0);
           writeH(300);
         }
         for (L1BoardPost topic : topics) {
           writeD(topic.getId());
           writeS(topic.getName());
           writeS(topic.getDate());
           writeS(topic.getTitle());
         }
       }




       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_Board";
       }
     }



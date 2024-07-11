     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_ChatPacket
       extends ServerBasePacket
     {
       private static final String _S__1F_NORMALCHATPACK = "[S] S_ChatPacket";

       public S_ChatPacket(String targetname, String chat, int opcode) {
         writeC(opcode);
         writeC(9);
         writeS("-> (" + targetname + ") " + chat);
       }

       public S_ChatPacket(String targetname, int type, String chat) {
         writeC(153);
         writeC(type);
         writeS("[" + targetname + "] " + chat);
       }


       public S_ChatPacket(String from, String chat) {
         writeC(104);
         writeS(from);
         writeS(chat);
       }

       public S_ChatPacket(L1PcInstance pc, String chat) {
         writeC(153);
         writeC(12);
         writeS(chat);
       }

       public S_ChatPacket(String chat) {
         writeC(153);
         writeC(15);
         writeD(0);
         writeS(chat);
       }

       public S_ChatPacket(L1PcInstance pc, String chat, int a, int b, int c) {
         writeC(153);
         writeC(4);
         writeS(chat);
       }

       public S_ChatPacket(L1PcInstance pc, String chat, int test) {
         writeC(112);
         writeC(15);
         writeD(pc.getId());
         writeS(chat);
       }

       public S_ChatPacket(String chat, int opcode) {
         writeC(opcode);
         writeC(2);
         writeS(chat);
       }

         public S_ChatPacket(L1PcInstance pc, String chat, int opcode, int type) {
             // 写入操作码
             writeC(opcode);

             switch (type) {
                 case 0:
                     // 类型 0 的处理
                     writeC(type);
                     writeD(pc.getId());
                     writeS(pc.getName() + ": " + chat); // 写入格式化的聊天信息
                     break;
                 case 2:
                     // 类型 2 的处理
                     writeC(type);
                     if (pc.isInvisble()) {
                         writeD(0); // 如果玩家是隐形的，写入 0
                     } else {
                         writeD(pc.getId()); // 否则写入玩家 ID
                     }
                     writeS("<" + pc.getName() + "> " + chat); // 写入格式化的聊天信息
                     writeH(pc.getX()); // 写入玩家的 X 坐标
                     writeH(pc.getY()); // 写入玩家的 Y 坐标
                     break;
                 case 3:
                     // 类型 3 的处理
                     writeC(type);
                     // 如果玩家名字是 "메티스" 并且不是 "미소피아" 也不是 "카시오페아"
                     if (pc.getName().equalsIgnoreCase("梅蒂斯") && !pc.getName().equalsIgnoreCase("米斯皮") && !pc.getName().equalsIgnoreCase("仙后座")) {
                         writeS("[******] " + chat); // 写入格式化的聊天信息，名字隐藏
                     }
                     break;
                 // 其他情况的处理
                 default:
                     break;
             }

             break;
           case 4:
             writeC(type);
             if (pc.getAge() == 0) {
               writeS("{" + pc.getName() + "} " + chat); break;
             }
             writeS("{" + pc.getName() + "(" + pc.getAge() + ")} " + chat);
             break;

           case 9:
             writeC(type);
             writeS("-> (" + pc.getName() + ") " + chat);
             break;
           case 11:
             writeC(type);
             writeS("(" + pc.getName() + ") " + chat);
             break;
           case 12:
             writeC(type);
             writeS("[" + pc.getName() + "] " + chat);
             break;
           case 13:
             writeC(4);
             writeS("{{" + pc.getName() + "}} " + chat);
             break;
           case 14:
             writeC(type);
             writeD(pc.getId());
             writeS("\\fU(" + pc.getName() + ") " + chat);
             break;
           case 15:
             writeC(type);
             writeS("[" + pc.getName() + "] " + chat);
             break;
           case 16:
             writeS(pc.getName());
             writeS(chat);
             break;
           case 17:
             writeC(type);
             writeS("{" + pc.getName() + "} " + chat);
             break;
         }
       }




       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_ChatPacket";
       }
     }



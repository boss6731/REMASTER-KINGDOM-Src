     package l1j.server.server.serverpackets;
     
     import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
     import java.io.UnsupportedEncodingException;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.server.model.Instance.L1PcInstance;
     
     public class S_NewChat
       extends ServerBasePacket
     {
       private static final String S_NewChat = "[S] S_NewChat";
       
       public S_NewChat(int chatType, int count, String chatText, String targetName, int unknown) {
         try {
           writeC(19);
           writeC(3);
           writeC(2);
           writeC(8);
           writeBit(System.currentTimeMillis() / 1000L);
           writeC(16);
           writeC(chatType);
           writeC(26);
           writeC((chatText.getBytes()).length);
           writeByte(chatText.getBytes());
           writeC(42);
           if (chatType == 1 && targetName != null && !targetName.equals("")) {
             writeC((targetName.getBytes()).length);
             writeByte(targetName.getBytes());
           } else {
             writeC(0);
           } 
           writeC(40);
           writeC(unknown);
           writeC(48);
           writeC((chatType == 1) ? 0 : 24);
           writeH(0);
         } catch (Exception e) {
           e.printStackTrace();
         } 
       }
       
       public S_NewChat(int type, String chatText, int count, L1PcInstance pc) {
         try {
           writeC(19);
           writeC(4);
           writeC(2);
           writeC(8);
           writeBit(System.currentTimeMillis() / 1000L);
           writeC(16);
           writeC(type);
           writeC(26);
           writeC((chatText.getBytes()).length);
           writeByte(chatText.getBytes());
           writeC(42);
           
           byte[] name = null;
           if (pc.isGm() && type == 3) {
             name = "******".getBytes("UTF-8");
           } else if (pc.is_shift_battle()) {
             String server_description = pc.get_server_description();
             MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
             if (MJString.isNullOrEmpty(server_description) || cInfo == null) {
               name = "身份不明的人".getBytes("UTF-8");
             } else {
               name = cInfo.to_name_pair().getBytes("UTF-8");
             } 
           } else if (pc.getAge() != 0 && type == 4) {
             String names = pc.getName() + "(" + pc.getAge() + ")";
             name = names.getBytes("UTF-8");
           } else {
             name = pc.getName().getBytes("UTF-8");
           } 
           
           writeC(name.length);
           writeByte(name);
           
           writeC(48);
           writeC(0);
           
           if (type == 0) {
             writeC(56);
             writeBit(pc.getId());
             writeC(64);
             writeBit(pc.getX());
             writeC(72);
             writeBit(pc.getY());
           } 
           int step = pc.getRankLevel();
           
           if (step >= 6) {
             writeC(80);
             writeC(step);
           } 
           writeH(0);
         } catch (Exception e) {
           e.printStackTrace();
         } 
       }
       public S_NewChat(L1PcInstance pc, int type, int chat_type, String chat_text, String target_name) {
         int step;
         writeC(19);
         
         switch (type) {
           case 3:
             writeC(3);
             break;
           case 4:
             writeC(4);
             break;
         } 
         
         writeC(2);
         
         writeC(8);
         writeC(0);
         
         writeC(16);
         writeC(chat_type);
         
         writeC(26);
         byte[] text_byte = chat_text.getBytes();
         writeC(text_byte.length);
         writeByte(text_byte);
     
     
         
         switch (type) {
           case 3:
             writeC(34);
             
             if (chat_type == 0) {
               writeC(0);
               writeC(40);
               writeC(0);
               writeC(48);
               writeC(24);
               writeC(56);
               writeC(0); break;
             }  if (chat_type == 1) {
               byte[] name_byte = target_name.getBytes();
               writeC(name_byte.length);
               writeByte(name_byte);
               writeC(40);
               writeC(0);
               writeC(48);
               writeC(0);
               writeH(0);
             } 
             break;
           case 4:
             writeC(42);
             try {
               byte[] name = null;
               if (pc.isGm() && chat_type == 3) {
                 name = "******".getBytes("UTF-8");
               } else if (pc.is_shift_battle()) {
                 String server_description = pc.get_server_description();
                 MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
                 if (MJString.isNullOrEmpty(server_description) || cInfo == null) {
                   name = "身份不明的人".getBytes("UTF-8");
                 } else {
                   name = cInfo.to_name_pair().getBytes("UTF-8");
                 } 
               } else if (pc.getAge() != 0 && chat_type == 4) {
                 String names = pc.getName() + "(" + pc.getAge() + ")";
                 name = names.getBytes("UTF-8");
               } else {
                 name = pc.getName().getBytes("UTF-8");
               } 
               writeC(name.length);
               writeByte(name);
             } catch (UnsupportedEncodingException unsupportedEncodingException) {}
             
             if (chat_type == 0) {
               writeC(56);
               writeK(pc.getId());
               writeC(64);
               writeK(pc.getX());
               writeC(72);
               writeK(pc.getY());
             } 
             step = pc.getRankLevel();
             if (step != 0) {
               writeC(80);
               writeC(step);
             } 
     
     
     
     
             
             writeH(0);
             break;
         } 
       }
       
       public byte[] getContent() {
         return getBytes();
       }
     
       
       public String getType() {
         return "[S] S_NewChat";
       }
     }



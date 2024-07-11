     package l1j.server.server.serverpackets;

     import MJShiftObject.MJEShiftObjectType;
     import java.sql.PreparedStatement;
     import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
     import l1j.server.MJKDASystem.MJKDALoader;
     import l1j.server.MJNetServer.Codec.MJNSHandler;
     import l1j.server.MJRaidSystem.MJRaidSpace;
     import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
     import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
     import l1j.server.server.BadNamesList;
     import l1j.server.server.GameClient;
     import l1j.server.server.UserCommands;
     import l1j.server.server.clientpackets.C_NewCharSelect;
     import l1j.server.server.datatables.CharacterTable;
     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_ChangeCharName
       extends ServerBasePacket
     {
       public static S_ChangeCharName getChangedSuccess() {
         return getChangedResult(true);
       }

       public static S_ChangeCharName getChangedFailure() {
         return getChangedResult(false);
       }

       public static S_ChangeCharName getChangedResult(boolean isSuccess) {
         S_ChangeCharName s = new S_ChangeCharName();
         s.writeC(43);
         s.writeC(30);
         s.writeH(isSuccess ? 2 : 6);
         s.writeH(0);
         return s;
       }

       public static S_ChangeCharName getChangedStart() {
         S_ChangeCharName s = new S_ChangeCharName();
         s.writeC(43);
         s.writeC(29);


         s.writeD(0);
         return s;
       }

       public static ServerBasePacket doChangeCharName(GameClient client, final String sourceName, final String destinationName, boolean world_in) {
         try {
           L1PcInstance pc = client.getActiveChar();

           if (pc == null) {
             pc = L1PcInstance.load(sourceName);
           }

           byte[] buff = destinationName.getBytes("MS949");
           if (world_in) {
             if (buff.length <= 0)
               return getChangedFailure();
             if (CharacterTable.getInstance().isContainNameList(destinationName) || MJBotNameLoader.isAlreadyName(destinationName)) {
               return getChangedFailure();
             }
             if (BadNamesList.getInstance().isBadName(destinationName))
               return getChangedFailure();
             int i;
             for (i = 0; i < destinationName.length(); i++) {
               if (destinationName.charAt(i) == 'ㄱ' || destinationName.charAt(i) == 'ㄲ' || destinationName.charAt(i) == 'ㄴ' || destinationName.charAt(i) == 'ㄷ' || destinationName
                 .charAt(i) == 'ㄸ' || destinationName.charAt(i) == 'ㄹ' || destinationName.charAt(i) == 'ㅁ' || destinationName.charAt(i) == 'ㅂ' || destinationName
                 .charAt(i) == 'ㅃ' || destinationName.charAt(i) == 'ㅅ' || destinationName.charAt(i) == 'ㅆ' || destinationName.charAt(i) == 'ㅇ' || destinationName
                 .charAt(i) == 'ㅈ' || destinationName.charAt(i) == 'ㅉ' || destinationName.charAt(i) == 'ㅊ' || destinationName.charAt(i) == 'ㅋ' || destinationName
                 .charAt(i) == 'ㅌ' || destinationName.charAt(i) == 'ㅍ' || destinationName.charAt(i) == 'ㅎ' || destinationName.charAt(i) == 'ㅛ' || destinationName
                 .charAt(i) == 'ㅕ' || destinationName.charAt(i) == 'ㅑ' || destinationName.charAt(i) == 'ㅐ' || destinationName.charAt(i) == 'ㅔ' || destinationName
                 .charAt(i) == 'ㅗ' || destinationName.charAt(i) == 'ㅓ' || destinationName.charAt(i) == 'ㅏ' || destinationName.charAt(i) == 'ㅣ' || destinationName
                 .charAt(i) == 'ㅠ' || destinationName.charAt(i) == 'ㅜ' || destinationName.charAt(i) == 'ㅡ' || destinationName.charAt(i) == 'ㅒ' || destinationName
                 .charAt(i) == 'ㅖ' || destinationName.charAt(i) == 'ㅢ' || destinationName.charAt(i) == 'ㅟ' || destinationName.charAt(i) == 'ㅝ' || destinationName
                 .charAt(i) == 'ㅞ' || destinationName.charAt(i) == 'ㅙ' || destinationName.charAt(i) == 'ㅚ' || destinationName.charAt(i) == 'ㅘ' || destinationName
                 .charAt(i) == '씹' || destinationName.charAt(i) == '좃' || destinationName.charAt(i) == '좆' || destinationName.charAt(i) == 'ㅤ') {
                 return getChangedFailure();
               }
             }

             for (i = 0; i < destinationName.length(); i++) {
               if (!Character.isLetterOrDigit(destinationName.charAt(i))) {
                 return getChangedFailure();
               }
             }
             if (!UserCommands.isAlphaNumeric(destinationName)) {
               return getChangedFailure();
             }
           }








           Updator.exec("UPDATE characters SET char_name=? WHERE char_name=?", new Handler()
               {
                 public void handle(PreparedStatement pstm) throws Exception {
                   pstm.setString(1, destinationName);
                   pstm.setString(2, sourceName);
                 }
               });

           Updator.exec("UPDATE tb_kda SET name=? WHERE name=?", new Handler()
               {
                 public void handle(PreparedStatement pstm) throws Exception {
                   pstm.setString(1, destinationName);
                   pstm.setString(2, sourceName);
                 }
               });

           MJKDALoader.getInstance().getChangeName(pc.getId(), destinationName);

           pc.save();

           System.out.println(String.format("[%s][%s] 從 %s 更改角色名為 %s. \r\n", new Object[] { MJNSHandler.getLocalTime(), client.getIp(), sourceName, destinationName }));

           if (pc.is_shift_transfer()) {
             pc.set_shift_type(MJEShiftObjectType.NONE);
           } else {
             pc.getInventory().consumeItem(408991, 1);
           }

           UserCommands.buddys(pc);
           UserCommands.편지삭제(pc);
           MJCopyMapObservable.getInstance().resetPosition(pc);
           MJRaidSpace.getInstance().getBackPc(pc);

           C_NewCharSelect.restartProcess(pc);
           return getChangedSuccess();
         }
         catch (Exception e) {
           e.printStackTrace();

           return getChangedFailure();
         }
       }




       public byte[] getContent() {
         return getBytes();
       }
     }



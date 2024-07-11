 package MJNCoinSystem.Commands;

 import MJNCoinSystem.MJNCoinDepositInfo;
 import MJNCoinSystem.MJNCoinIdFactory;
 import MJNCoinSystem.MJNCoinSettings;
 import l1j.server.MJTemplate.Command.MJCommandArgs;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.Account;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class MJNCoinDepositExecutor
   extends MJNCoinExecutor
 {
   public void execute(MJCommandArgs args) {
     try {
       L1PcInstance pc = args.getOwner();
       if (pc == null) {
         return;
       }
       Account account = pc.getAccount();
       if (account == null) {
         return;
       }
       String current_date = MJString.get_current_datetime();







       MJNCoinDepositInfo dInfo = MJNCoinDepositInfo.newInstance().set_deposit_id(MJNCoinIdFactory.DEPOSIT.next_id()).set_character_object_id(pc.getId()).set_character_name(pc.getName()).set_account_name(pc.getAccountName()).set_deposit_name(args.nextString()).set_ncoin_value(args.nextInt()).set_generate_date(current_date);
       MJNCoinDepositInfo.do_store(dInfo);
       do_write_letter_command(pc, MJNCoinSettings.DEPOSIT_LETTER_ID);
         try {
             // 創建信件主題
             String subject = String.format("[充值申請] %s", new Object[] { pc.getName() });

             // 寫信通知 GM 充值申請詳情
             do_write_letter_togm(current_date, subject, dInfo.toString());
         } catch (Exception e) {
             // 當發生異常時，通知玩家正確的命令格式
             args.notify(".充值 [姓名] [金額]");
         }
     }

       public String get_command_name() {
           return "充值";
       }



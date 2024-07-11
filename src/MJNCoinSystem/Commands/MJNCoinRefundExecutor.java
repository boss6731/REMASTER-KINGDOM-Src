 package MJNCoinSystem.Commands;

 import MJNCoinSystem.MJNCoinIdFactory;
 import MJNCoinSystem.MJNCoinRefundInfo;
 import MJNCoinSystem.MJNCoinSettings;
 import l1j.server.MJTemplate.Command.MJCommandArgs;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.Account;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class MJNCoinRefundExecutor
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
       if (account.Ncoin_point < MJNCoinSettings.REFUND_GENERATE_MIN) {
         args.notify(String.format("您擁有的 N 個硬幣至少未達到 %,d 您的金額。", new Object[] { Integer.valueOf(MJNCoinSettings.REFUND_GENERATE_MIN) }));

         return;
       }
       String current_date = MJString.get_current_datetime();









       MJNCoinRefundInfo rInfo = MJNCoinRefundInfo.newInstance().set_character_object_id(pc.getId()).set_character_name(pc.getName()).set_account_name(pc.getAccountName()).set_refund_name(args.nextString()).set_bank_name(args.nextString()).set_bank_account_number(args.nextString()).set_ncoin_value(args.nextInt()).set_is_refund(false).set_generate_date(current_date);

       if (account.Ncoin_point < rInfo.get_ncoin_value()) {
         args.notify(String.format("由於N幣不足，提幣請求失敗。目前點數：%,d", new Object[] { Integer.valueOf(account.Ncoin_point) }));

         return;
       }
       account.Ncoin_point -= rInfo.get_ncoin_value();
       account.updateNcoin();
       MJNCoinRefundInfo.do_store(rInfo.set_refund_id(MJNCoinIdFactory.REFUND.next_id()));
       String subject = String.format("[提款請求] %s", new Object[] { pc.getName() });
       do_write_letter_togm(rInfo.get_generate_date(), subject, rInfo.toString());
       args.notify("提款請求已完成。處理過程會在 10 分鐘到 1 小時內完成。");
     } catch (Exception e) {
       args.notify(".tome [姓名] [銀行] [帳戶] [金額]");
     }
   }


   public String get_command_name() {
     return "提取";
   }
 }



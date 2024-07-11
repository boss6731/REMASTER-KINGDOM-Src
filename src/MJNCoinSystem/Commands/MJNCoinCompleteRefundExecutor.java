 package MJNCoinSystem.Commands;

 import MJNCoinSystem.MJNCoinRefundInfo;
 import l1j.server.MJTemplate.Command.MJCommandArgs;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.Account;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class MJNCoinCompleteRefundExecutor
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
       if (!pc.isGm()) {
         return;
       }
       int refund_id = args.nextInt();
       MJNCoinRefundInfo rInfo = MJNCoinRefundInfo.from_refund_id(refund_id);
       if (rInfo == null) {
         args.notify(String.format("未找到提現資訊。提款 ID：%d", new Object[] { Integer.valueOf(refund_id) }));
         return;
       }
       if (rInfo.get_is_refund()) {
         args.notify(String.format("該資訊已被撤回。提款 ID：%d", new Object[] { Integer.valueOf(refund_id) }));
         return;
       }
       String current_date = MJString.get_current_datetime();
       MJNCoinRefundInfo.update_is_refund(refund_id, true, current_date);
       String content = "提款已完成。希望您檢查一下";
       String subject = "【提現完成】";
       do_write_letter(rInfo.get_character_name(), current_date, subject, content);
       args.notify(String.format("檢查提款狀態。提款 ID：%d", new Object[] { Integer.valueOf(refund_id) }));
     } catch (Exception e) {
       args.notify(".tpM 【提幣ID】");
     }
   }


   public String get_command_name() {
     return "提現狀態";
   }
 }




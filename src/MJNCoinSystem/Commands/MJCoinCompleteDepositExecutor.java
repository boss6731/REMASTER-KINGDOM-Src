
 package MJNCoinSystem.Commands;



 import MJNCoinSystem.MJNCoinDepositInfo;

 import l1j.server.MJTemplate.Command.MJCommandArgs;

 import l1j.server.MJTemplate.MJString;

 import l1j.server.server.Account;

 import l1j.server.server.GMCommands;

 import l1j.server.server.model.Instance.L1PcInstance;



 public class MJCoinCompleteDepositExecutor

   extends MJNCoinExecutor {

   public static final int ALREADY = 0;

   public static final int COMPLETE = 1;

   public static final int CANCEL = 2;



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

       String type = args.nextString();

       MJNCoinDepositInfo rInfo = MJNCoinDepositInfo.from_deposit_id(refund_id);

       if (rInfo == null) {

         args.notify(String.format("未能到提現資訊。提款 ID：%d", new Object[] { Integer.valueOf(refund_id) }));



         return;

       }

       if (rInfo.is_deposit() == 1) {

         args.notify(String.format("該資訊已被撤回。提款 ID：%d", new Object[] { Integer.valueOf(refund_id) }));





         return;

       }



       int type_int = type.equalsIgnoreCase("等待") ? 0 : (type.equalsIgnoreCase("取消") ? 2 : 1);



       if (type_int != 0 && rInfo.is_deposit() == 2) {

         args.notify(String.format("此資訊已取消提款。提款 ID：%d", new Object[] { Integer.valueOf(refund_id) }));



         return;

       }

       String current_date = MJString.get_current_datetime();

       MJNCoinDepositInfo.update_is_deposit(refund_id, type_int, current_date);

       String content = (type_int == 2) ? "儲儲請求已取消。" : ((type_int == 0) ? "切換至充電待機狀態。" : "希望能檢查一下");

       String subject = (type_int == 2) ? "[取消充電]" : ((type_int == 0) ? "[待機充電]" : "[充電完成]");



       if (type_int == 1) {

         GMCommands.increaseNcoin(args.getOwner(), rInfo.get_character_name(), rInfo.get_ncoin_value());

       }



       do_write_letter(rInfo.get_character_name(), current_date, subject, content);

       args.notify(String.format("檢查充電狀態。提款 ID：%d", new Object[] { Integer.valueOf(refund_id) }));

     } catch (Exception e) {

       args.notify(".Chargingstatus [充電ID][等待/完成/取消]");

     }

   }





   public String get_command_name() {

     return "充電狀態";

   }

 }



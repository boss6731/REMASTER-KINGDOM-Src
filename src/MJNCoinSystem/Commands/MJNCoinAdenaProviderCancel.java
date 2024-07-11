 package MJNCoinSystem.Commands;

 import MJNCoinSystem.MJNCoinAdenaInfo;
 import MJNCoinSystem.MJNCoinIdFactory;
 import l1j.server.MJTemplate.Command.MJCommandArgs;
 import l1j.server.server.Account;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Warehouse.SupplementaryService;
 import l1j.server.server.model.Warehouse.WarehouseManager;


 public class MJNCoinAdenaProviderCancel
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
       int trade_id = args.nextInt();
       Object sync = MJNCoinIdFactory.ADENA.get_sync_object(trade_id);
       synchronized (sync) {
         MJNCoinAdenaInfo aInfo = MJNCoinAdenaInfo.from_trade_id(trade_id);
         if (aInfo == null) {
           args.notify(String.format("未找到 %d 筆交易資訊。", new Object[] { Integer.valueOf(trade_id) }));
           return;
         }
         if (aInfo.get_character_object_id() != pc.getId() || !aInfo.get_account_name().equalsIgnoreCase(account.getName())) {
           args.notify("這不是註冊交易。");

           return;
         }
         MJNCoinAdenaInfo.delete_trade_info(aInfo);

         L1ItemInstance adena = ItemTable.getInstance().createItem(40308);
         adena.setCount(aInfo.get_adena_amount());
         SupplementaryService service = WarehouseManager.getInstance().getSupplementaryService(account.getName());
         service.storeTradeItem(adena);
       }
       args.notify("銷售取消已處理。");
     } catch (Exception e) {
       args.notify(".Cancellation [交易編號]");
       args.notify("ex) .Cancellation 0017");
     }
   }


   public String get_command_name() {
     return "取消銷售";
   }
 }



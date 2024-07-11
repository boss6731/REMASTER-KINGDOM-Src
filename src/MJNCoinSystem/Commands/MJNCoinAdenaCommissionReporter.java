
 package MJNCoinSystem.Commands;



 import java.sql.ResultSet;

 import l1j.server.MJTemplate.Command.MJCommandArgs;

 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;

 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 import l1j.server.server.model.Instance.L1PcInstance;



 public class MJNCoinAdenaCommissionReporter

   extends MJNCoinExecutor

 {

   public void execute(final MJCommandArgs args) {

     L1PcInstance pc = args.getOwner();

     if (pc == null || !pc.isGm()) {

       return;

     }

     try {

       Selector.exec("select sum(commission_ncoin) as commission_report FROM `ncoin_trade_adena`", (SelectorHandler)new FullSelectorHandler()

           {

             public void result(ResultSet rs) throws Exception {

               if (rs.next()) {

                 args.notify(String.format("目前預計?金利潤總額?：%，d 韓元。", new Object[] { Integer.valueOf(rs.getInt("commission_report")) }));

               }

             }

           });

       Selector.exec("select sum(commission_ncoin) as commission_report FROM `ncoin_trade_adena` where complete_date is not null", (SelectorHandler)new FullSelectorHandler()

           {

             public void result(ResultSet rs) throws Exception {

               if (rs.next()) {

                 args.notify(String.format("目前委託完成利潤總額?：%,d 韓元。", new Object[] { Integer.valueOf(rs.getInt("commission_report")) }));

               }

             }

           });

     } catch (Exception e) {

       e.printStackTrace();

     }

   }





   public String get_command_name() {

     return "支票費";

   }

 }



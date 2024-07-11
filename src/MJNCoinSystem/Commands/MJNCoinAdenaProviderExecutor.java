 package MJNCoinSystem.Commands;

 import MJNCoinSystem.MJNCoinAdenaInfo;
 import MJNCoinSystem.MJNCoinCharacterReport;
 import MJNCoinSystem.MJNCoinCreditLoader;
 import MJNCoinSystem.MJNCoinIdFactory;
 import MJNCoinSystem.MJNCoinSettings;
 import l1j.server.MJSurveySystem.MJInterfaceSurvey;
 import l1j.server.MJSurveySystem.MJSurveySystemLoader;
 import l1j.server.MJTemplate.Command.MJCommandArgs;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.Account;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class MJNCoinAdenaProviderExecutor
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
             int adena_amount = args.nextInt(); // 取得下一個整數參數作為阿登數量
             int ncoin_amount = args.nextInt(); // 取得下一個整數參數作為N幣數量

// 檢查玩家的庫存中是否有ID為40308的項目 (阿登)
             if (!pc.getInventory().checkItem(40308)) {
                 args.notify("金幣不足。");
                 return;
             }
             if (adena_amount < MJNCoinSettings.ADENA_GENERATE_UNIT) {
                 args.notify(String.format("銷售註冊至少從 %,d元開始，並以 %,d 為單位註冊。", new Object[] { Integer.valueOf(MJNCoinSettings.ADENA_GENERATE_UNIT), Integer.valueOf(MJNCoinSettings.ADENA_GENERATE_UNIT) }));
                 return;
             }
             if (adena_amount > MJNCoinSettings.ADENA_GENERATE_UNIT * MJNCoinSettings.ADENA_GENERATE_MAX) {
                 args.notify(String.format("銷售註冊一次最多可以註冊至 %,d元。", new Object[] { Integer.valueOf(MJNCoinSettings.ADENA_GENERATE_UNIT * MJNCoinSettings.ADENA_GENERATE_MAX) }));
                 return;
             }
             adena_amount -= adena_amount % MJNCoinSettings.ADENA_GENERATE_UNIT;
             int amount = adena_amount / MJNCoinSettings.ADENA_GENERATE_UNIT;
             int ncoin_per_adena = ncoin_amount / amount;
             if (ncoin_per_adena < MJNCoinSettings.ADENA_MARKET_PRICE) {
                 args.notify(String.format("請至少以每 %,d元對應 %,d元的標準進行註冊。", new Object[] { Integer.valueOf(MJNCoinSettings.ADENA_GENERATE_UNIT), Integer.valueOf(MJNCoinSettings.ADENA_MARKET_PRICE) }));
                 return;
             }
             if (!pc.getInventory().checkItem(40308, adena_amount)) {
                 args.notify("金幣不足。");
                 return;
             }
             do_survey(pc, ncoin_amount, adena_amount, ncoin_per_adena);
         } catch (Exception e) {
             args.notify(".銷售登記 [金幣] [銷售價格]"); // 通知用戶正確的命令格式
             MJNCoinCharacterReport.MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(args.getOwner().getId());
             MJNCoinCreditLoader.MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info((cInfo != null) ? cInfo.get_selling_price() : 0L);
             args.notify(String.format("注意）銷售金額的 %d%% 將被扣除作為手續費。", new Object[] { Integer.valueOf((int)(credit.get_commission() * 100.0D)) }));
         }
     }




     public String get_command_name() {
         return "銷售登錄"; // 方法名称翻译为 "銷售登錄"
     }

     private void do_survey(L1PcInstance pc, final int ncoin_amount, final int adena_amount, final int ncoin_per_adena) {
         ServerBasePacket pck = MJSurveySystemLoader.getInstance().registerSurvey(String.format("%,d 萬金幣將被註冊。是否繼續？", new Object[] { Integer.valueOf(adena_amount / 10000) }), pc.getId(), new MJInterfaceSurvey() {
             // 此处添加了对调查的处理方法
             public void survey(L1PcInstance pc, int num, boolean isYes) {
                 if (!isYes) {
                     pc.sendPackets("您已取消銷售註冊。");
                     return;
                 try {
                     if (!pc.getInventory().consumeItem(40308, adena_amount)) {
                         pc.sendPackets("金幣不足。");
                         return;
                     }
                     // 这里可以继续添加其他逻辑
                 } catch (Exception e) {
                     pc.sendPackets("發生錯誤。請稍後再試。");
                 }
             }
         });
     }

              String current_date = MJString.get_current_datetime();
              MJNCoinCharacterReport.MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(pc.getId());
              MJNCoinCreditLoader.MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info((cInfo != null) ? cInfo.get_selling_price() : 0L);
              int commission_ncoin = (int)(ncoin_amount * credit.get_commission());
              int trade_id = MJNCoinIdFactory.ADENA.next_id();
              Object sync = MJNCoinIdFactory.ADENA.get_sync_object(trade_id);
              MJNCoinAdenaInfo aInfo = null;
              synchronized (sync) {



                 aInfo = MJNCoinAdenaInfo.newInstance().set_trade_id(trade_id).set_character_object_id(pc.getId()).set_character_name(pc.getName()).set_account_name(pc.getAccountName()).set_adena_amount(adena_amount).set_ncoin_value(ncoin_amount).set_commission(credit.get_commission()).set_commission_ncoin(commission_ncoin).set_drain_ncoin(ncoin_amount - commission_ncoin).set_ncoin_per_adena(ncoin_per_adena).set_ncoin_per_adena_unit(MJNCoinSettings.ADENA_GENERATE_UNIT).set_generate_date(current_date);
                 MJNCoinAdenaInfo.do_store(aInfo);
               }

         public void survey(L1PcInstance pc, int num, boolean isYes) {
             if (!isYes) {
                 pc.sendPackets("您已取消銷售註冊。");
                 return;
             }
             try {
                 if (!pc.getInventory().consumeItem(40308, adena_amount)) {
                     pc.sendPackets("金幣不足。");
                     return;
                 }
                 pc.sendPackets("銷售登錄已完成。");
                 MJNCoinExecutor.do_write_letter(pc.getName(), current_date, "[銷售登錄通知]", aInfo.to_provider());
             } catch (Exception e) {
                 pc.sendPackets(".銷售登錄 [金幣] [銷售金額]");
                 MJNCoinCharacterReport.MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(pc.getId());
                 MJNCoinCreditLoader.MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info((cInfo != null) ? cInfo.get_selling_price() : 0L);
                 pc.sendPackets(String.format("注意：銷售金額的 %d%% 將作為手續費被扣除。", new Object[] { Integer.valueOf((int)(credit.get_commission() * 100.0D)) }));
             }
         }
     }10000L);
if (pck == null) {
     pc.sendPackets("請在10秒後再試。");
 } else {
     pc.sendPackets(pck);
 }
 }
}



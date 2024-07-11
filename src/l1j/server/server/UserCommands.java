     package l1j.server.server;

     import MJNCoinSystem.MJNCoinDepositInfo;
     import MJNCoinSystem.MJNCoinIdFactory;
     import MJNCoinSystem.MJNCoinSettings;
     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.text.DecimalFormat;
     import java.text.SimpleDateFormat;
     import java.util.ArrayList;
     import java.util.StringTokenizer;
     import l1j.server.Config;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
     import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
     import l1j.server.MJKDASystem.Chart.MJKDAChartScheduler;
     import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
     import l1j.server.MJNetServer.Codec.MJNSHandler;
     import l1j.server.MJSurveySystem.MJSurveyFactory;
     import l1j.server.MJSurveySystem.MJSurveySystemLoader;
     import l1j.server.MJTemplate.Command.MJCommandArgs;
     import l1j.server.MJTemplate.MJObjectWrapper;
     import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHARATER_FOLLOW_EFFECT_NOTI;
     import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
     import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
     import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
     import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashResultDatabase;
     import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashUserInfo;
     import l1j.server.Payment.MJPaymentInfo;
     import l1j.server.lotto.lotto_system;
     import l1j.server.server.datatables.ItemTable;
     import l1j.server.server.datatables.LetterTable;
     import l1j.server.server.datatables.ShopBuyLimitInfo;
     import l1j.server.server.model.CastleEffect;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Character;
     import l1j.server.server.model.L1Clan;
     import l1j.server.server.model.L1World;
     import l1j.server.server.model.Warehouse.SupplementaryService;
     import l1j.server.server.model.Warehouse.WarehouseManager;
     import l1j.server.server.serverpackets.S_Ability;
     import l1j.server.server.serverpackets.S_ChatPacket;
     import l1j.server.server.serverpackets.S_LetterList;
     import l1j.server.server.serverpackets.S_MARK_SEE;
     import l1j.server.server.serverpackets.S_Message_YN;
     import l1j.server.server.serverpackets.S_PacketBox;
     import l1j.server.server.serverpackets.S_SystemMessage;
     import l1j.server.server.serverpackets.S_Unknown2;
     import l1j.server.server.serverpackets.ServerBasePacket;
     import l1j.server.server.templates.L1BoardPost;
     import l1j.server.server.templates.L1Item;
     import l1j.server.server.templates.ShopBuyLimit;
     import l1j.server.server.templates.eShopBuyLimitType;
     import l1j.server.server.utils.MJFullStater;
     import l1j.server.server.utils.SQLUtil;




     public class UserCommands
     {
       boolean spawnTF = false;
       private static UserCommands _instance;
       private CastleEffect ce;

       public static UserCommands getInstance() {
         if (_instance == null) {
           _instance = new UserCommands();
         }
         return _instance;
       }

         private void handleCommandsForShiftBattle(L1PcInstance pc, String cmd, String param) {
             switch (cmd) {
                 case "幫助":
                     showHelp(pc);
                     break;
                 case "光":
                     maphack(pc, param);
                     break;
                 case "血派對":
                     BloodParty(pc);
                     break;
                 case ".":
                 case "傳送":
                 case "傳送命令":
                 case "解除傳送":
                     tell(pc);
                     break;
                 case "宏":
                     macroSetting(pc, param);
                     break;
             }
         }




         public void handleCommands(L1PcInstance pc, String cmdLine) {
             if (pc == null) {
                 return;
             }

             StringTokenizer token = new StringTokenizer(cmdLine);

             String cmd = "";
             if (token.hasMoreTokens()) {
                 cmd = token.nextToken();
             } else {
                 cmd = cmdLine;
             }

             String param = "";
[22:16]
             while (token.hasMoreTokens()) {
                 param = param + token.nextToken() + ' ';
             }
             param = param.trim();

             if (pc.is_shift_battle()) {
                 handleCommandsForShiftBattle(pc, cmd, param);
                 return;
             }

             try {
                 switch (cmd) {
                     case "幫助":
                         showHelp(pc);
                         return;

                     case "宏":
                         macroSetting(pc, param);
                         return;

                     case "註冊優惠券":
                         쿠폰등록(pc, param);
                         return;

                     case "外部聊天":
                         outsideChat(pc, param);
                         return;

                     case "復原":
                         복구(pc);
                         return;

                     case "年齡":
                         age(pc, param);
                         return;

                     case "血派對":
                         BloodParty(pc);
                         return;

                     case "信息":
                         Ment(pc, param);
                         return;

                     case "光":
                         maphack(pc, param);
                         return;

                     case "血徽":
                         clanMark(pc, param);
                         return;

                     case "查詢框架":
                         MJCTLoadManager.commands(pc, param);
                         return;

                     case "通緝":
                         hunt(pc, param);
                         return;

                     case "解除傳送":
                     case "傳送命令":
                     case "傳送":
                     case ".":
                         tell(pc);
                         return;

                     case "老闆通知":
                         spawnNotifyOnOff(pc, param);
                         return;

                     case "更改密碼":
                     case "更改密碼":
                         changepassword(pc, param);
                         return;

                     case "購買限制":
                         viewShopBuyLimit(pc);
                         return;

                     case "存款人註冊":
                         AutoCashInfoAdd(pc, param);
                         return;

                     case "樂透":
                     case "查看樂透":
                         checklotto(pc);
                         return;

                     case "城堡效果":
                         castleEffect(pc, param);
                         return;

                     case "強制變身":
                         ForcePoly(pc, param);
                         return;
                 }
             } catch (Exception e) {
                 _log.severe("Error handling command: " + cmdLine + " from player: " + pc.getName());
             }
         }

           pc.sendPackets(261);

         }
         catch (Exception e) {
           e.printStackTrace();
         }
       }
     private void ForcePoly(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             String onoff = st.nextToken();

             if (pc == null) {
             return;
             }
             if (onoff.equalsIgnoreCase("關")) {
             pc.set_ForcePolyId(0);
             pc.sendPackets("強制變身功能已解除。");
             return;
             }
             if (onoff.equalsIgnoreCase("1")) {
             if (pc.getInventory().checkItem(30001887)) {
             pc.set_ForcePolyId(20442);
             pc.sendPackets("強制變身為亞利安已設置。");
             } else {
             pc.sendPackets("你沒有該變身卡。");
             return;
             }
             } else if (onoff.equalsIgnoreCase("2")) {
             if (pc.getInventory().checkItem(30001888)) {
             pc.set_ForcePolyId(20469);
             pc.sendPackets("強制變身為克里斯特已設置。");
             } else {
             pc.sendPackets("你沒有該變身卡。");
             return;
             }
             } else if (onoff.equalsIgnoreCase("3")) {
             if (pc.getInventory().checkItem(30001890)) {
             pc.set_ForcePolyId(20446);
             pc.sendPackets("強制變身為阿託恩已設置。");
             } else {
             pc.sendPackets("你沒有該變身卡。");
             return;
             }
             } else if (onoff.equalsIgnoreCase("4")) {
             if (pc.getInventory().checkItem(30001891)) {
             pc.set_ForcePolyId(20471);
             pc.sendPackets("強制變身為依希洛特已設置。");
             } else {
             pc.sendPackets("你沒有該變身卡。");
             return;
             }
             } else if (onoff.equalsIgnoreCase("5")) {
             if (pc.getInventory().checkItem(30001892)) {
             pc.set_ForcePolyId(20449);
             pc.sendPackets("強制變身為佐武已設置。");
             } else {
             pc.sendPackets("你沒有該變身卡。");
             return;
             }
             } else if (onoff.equalsIgnoreCase("6")) {
             if (pc.getInventory().checkItem(30001893)) {
             int polyid = 0;
             if (pc.isCrown()) {
             if (pc.get_sex() == 0) {
             polyid = 20085;
             } else {
             polyid = 20086;
             }
             } else if (pc.isKnight()) {
             if (pc.get_sex() == 0) {
             polyid = 20087;
             } else {
             polyid = 20088;
             }
             } else if (pc.isElf()) {
             if (pc.get_sex() == 0) {
             polyid = 20089;
             } else {
             polyid = 20090;
             }
             } else if (pc.isWizard()) {
             if (pc.get_sex() == 0) {
             polyid = 20091;
             } else {
             polyid = 20092;
             }
             } else if (pc.isDarkelf()) {
             if (pc.get_sex() == 0) {
             polyid = 20093;
             } else {
             polyid = 20094;
             }
             } else if (pc.isDragonknight()) {
             if (pc.get_sex() == 0) {
             polyid = 20095;
             } else {
             polyid = 20096;
             }
             } else if (pc.isBlackwizard()) {
             if (pc.get_sex() == 0) {
             polyid = 20097;
             } else {
             polyid = 20098;
             }
             } else if (pc.isWarrior()) {
             if (pc.get_sex() == 0) {
             polyid = 20099;
             } else {
             polyid = 20100;
             }
             } else if (pc.isFencer()) {
             if (pc.get_sex() == 0) {
             polyid = 20101;
             } else {
             polyid = 20102;
             }
             } else if (pc.isLancer()) {
             if (pc.get_sex() == 0) {
             polyid = 20103;
             } else {
             polyid = 20104;
             }
             pc.set_ForcePolyId(polyid);
             pc.sendPackets("強制變身為排名變身已設置。");
             } else {
             pc.sendPackets("你沒有該變身卡。");
             return;
             }
             if (onoff.equalsIgnoreCase("7")) {
             if (pc.getInventory().checkItem(30001894)) {
             pc.set_ForcePolyId(17541);
             pc.sendPackets("真·死亡騎士(紅)的強制變身設置完成。");
             } else {
             pc.sendPackets("您沒有該變身卡。");
             return;
             }
             } else if (onoff.equalsIgnoreCase("8")) {
             if (pc.getInventory().checkItem(30001895)) {
             pc.set_ForcePolyId(19689);
             pc.sendPackets("真·死亡騎士(黑)的強制變身設置完成。");
             } else {
             pc.sendPackets("您沒有該變身卡。");
             return;
             }
             } else if (onoff.equalsIgnoreCase("9")) {
             if (pc.getInventory().checkItem(30001889)) {
             pc.set_ForcePolyId(20438);
             pc.sendPackets("多佩爾剎那的強制變身設置完成。");
             } else {
             pc.sendPackets("您沒有該變身卡。");
             return;
             }
             } else {
             pc.sendPackets(".強制變身 數字/關");
             pc.sendPackets("1.亞利安 2.克里斯特 3.阿託恩 4.依希洛特 5.佐武 6.排名 7.真·死亡騎士(紅) 8.真·死亡騎士(黑) 9.多佩爾剎那");
             return;
             }
             } catch (Exception e) {
             pc.sendPackets(".強制變身 數字/關");
             pc.sendPackets("1.亞利安 2.克里斯特 3.阿託恩 4.依希洛特 5.佐武 6.排名 7.真·死亡騎士(紅) 8.真·死亡騎士(黑) 9.多佩爾剎那");
             }

             private void castleEffect(L1PcInstance pc, String param) {
                 try {
                     StringTokenizer st = new StringTokenizer(param);
                     String onoff = st.nextToken();

                     if (pc == null) {
                         return;
                     }
                     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
                     if (clan == null) {
                         pc.sendPackets("你尚未加入任何血盟。");
                         return;
                     }
                     if (onoff.equalsIgnoreCase("開")) {
                         if (clan.getCastleId() != 0) {
                             if (!pc.isCastleEffect()) {
                                 pc.setCastleEffect(true);
                                 startCastleEffect(pc);
                                 pc.sendPackets("開啟城堡效果。");
                                 return;
                             }
                             pc.sendPackets("城堡效果已經開啟。");
                             return;
                         }
                         pc.sendPackets("你所在的血盟不擁有任何城堡。");
                     } else if (onoff.equalsIgnoreCase("關")) {
                         pc.setCastleEffect(false);
                         stopCastleEffect(pc);
                         pc.sendPackets("關閉城堡效果。");
                     } else {
                         pc.sendPackets(".城堡效果 [開 / 關]");
                         return;
                     }
                 } catch (Exception e) {
                     pc.sendPackets(".城堡效果 開/關");
                 }
             }

       private void startCastleEffect(L1PcInstance pc) {
         long inteval = 30000L;
         this.ce = new CastleEffect(pc, 30000L, true);
         GeneralThreadPool.getInstance().schedule((Runnable)this.ce, 10L);
       }
       private void stopCastleEffect(L1PcInstance pc) {
         SC_CHARATER_FOLLOW_EFFECT_NOTI.follow_effect_send(pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false);
         pc.broadcastPacket(SC_CHARATER_FOLLOW_EFFECT_NOTI.broad_follow_effect_send((L1Character)pc, Config.ServerAdSetting.CASTLE_CLAN_EFFECT, false));
         this.ce.cancel();
         this.ce = null;
       }

       private void checklotto(L1PcInstance pc) {
         lotto_system.getInstance().checklotto(pc);
       }

     private void AutoCashInfoAdd(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             String name = st.nextToken();

             AutoCashUserInfo acui = AutoCashResultDatabase.getInstance().getAutoCashUserInfo(name);
             if (acui != null) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("已存在相同的存款者名稱。請輸入其他名稱。"));
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("** 注意事項：存款時需使用相同的名稱。 **"));
             return;
             }
             acui = new AutoCashUserInfo();
             acui.setAccountName(pc.getAccountName());
             acui.setCharName(pc.getName());

             AutoCashResultDatabase.getInstance().addAutoCashUserInfo(name, acui);
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("** 存款者名稱已成功註冊。 **"));
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("** 注意事項：存款時需使用相同的名稱。 **"));
             } catch (Exception e) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入指令：.存款者註冊 [存款者名稱]。"));
             }
             }

     private void registerCoupon(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             String code = st.nextToken();

             if (pc == null) {
             return;
             }
             MJPaymentInfo m_pInfo = MJPaymentInfo.newInstance(code.toUpperCase());
             if (m_pInfo == null) {
             pc.sendPackets(".註冊優惠券 [編號]");
             pc.sendPackets("該優惠券號碼不存在。");
             return;
             }
             if (Check_Coupon(code.toUpperCase())) {
             pc.sendPackets("該優惠券號碼已被使用。");
             return;
             }
           SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
           if (pwh == null) {
             return;
           }
           m_pInfo.set_account_name(pc.getAccountName()).set_character_name(pc.getName()).set_expire_date(MJNSHandler.getLocalTime()).set_is_use(true).do_update();

           L1Item tempItem = ItemTable.getInstance().getTemplate(m_pInfo.get_itemid());
           if (tempItem.isStackable()) {
             L1ItemInstance item = ItemTable.getInstance().createItem(tempItem.getItemId());
             item.setIdentified(true);
             item.setCount(m_pInfo.get_count());
             pwh.storeTradeItem(item);
           } else {
             L1ItemInstance item = null;

             for (int createCount = 0; createCount < m_pInfo.get_count(); createCount++) {
               item = ItemTable.getInstance().createItem(tempItem.getItemId());
               item.setIdentified(true);
               pwh.storeTradeItem(item);
             }
           }


             SC_GOODS_INVEN_NOTI.do_send(pc);
             pc.send_effect(2048);
             pc.sendPackets((ServerBasePacket)new S_PacketBox(84, String.format("\f3已發放 (%s) 個優惠券物品。請至附加物品倉庫領取。", new Object[] { (new DecimalFormat("#,##0")).format(m_pInfo.get_count()) })));
             pc.sendPackets(String.format("已發放 (%s) 個優惠券物品。", new Object[] { (new DecimalFormat("#,##0")).format(m_pInfo.get_count()) }));
             } catch (Exception e) {
             pc.sendPackets(".註冊優惠券 [編號]");
             }
             }

       private boolean Check_Coupon(String coupon) {
         PreparedStatement pstm = null;
         ResultSet rs = null;
         Connection con = null;
         try {
           int code = 0;
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("select is_use from payment_info where code Like '" + coupon + "'");
           rs = pstm.executeQuery();
           if (rs.next()) {
             code = rs.getInt(1);
           }
           if (code != 0) {
             return true;
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(rs, pstm, con);
         }
         return false;
       }

     private void viewShopBuyLimit(L1PcInstance pc) {
             ArrayList<ShopBuyLimit> sbl_list = ShopBuyLimitInfo.getInstance().getCharacterList(pc.getId());
             if (sbl_list != null && sbl_list.size() > 0) {
             for (ShopBuyLimit sbl : sbl_list) {
             if (sbl.get_count() <= 0) {
             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String sLatestLoginDate = formatter.format(sbl.get_end_time());
             pc.sendPackets(String.format("\f3" + sbl.get_item_name() + "\f3已達購買上限，" + ((sbl
             .get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) ? "\f3同一帳號 " : "\f3") + " %s 之後可再次購買。", new Object[] { sLatestLoginDate }));
             continue;
             }
             pc.sendPackets("\f2" + sbl.get_item_name() + "可額外購買 " + sbl.get_count() + " 件。");
             }

             pc.sendPackets("總共查詢到 " + sbl_list.size() + " 項購買限制。");
             } else {
             pc.sendPackets("沒有購買限制列表。");
             }
             }

     public static void setChart(L1PcInstance pc, String param) {
             boolean isOn;
             if (!MJKDAChartScheduler.isLoaded()) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("目前未使用。"));
             return;
             }
             if (param.equalsIgnoreCase("開")) {
             isOn = true;
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("將顯示擊殺排行圖表。稍後將出現在螢幕上。"));
             MJKDAChartScheduler.getInstance().onLoginUser(pc);
             } else if (param.equalsIgnoreCase("關")) {
             isOn = false;
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("將隱藏擊殺排行圖表。重新登入後生效。"));
             } else {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage(".圖表 [開/關] (左上角 PK圖表清單)"));
             return;
             }
             if (pc.getKDA() != null)
             pc.getKDA().isChartView = isOn;
             }

     private void clanMark(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             String onoff = st.nextToken();

             if ((pc.getMapId() >= 1708 && pc.getMapId() <= 1710) || (pc.getMapId() >= 12852 && pc.getMapId() <= 12862) || pc
             .getMapId() == 15871 || pc.getMapId() == 15881 || pc.getMapId() == 15891 || pc.getMapId() == 10500 || pc.getMapId() == 10502) {
             pc.sendPackets("無法在此地圖上使用。");
             return;
             }
             if (onoff.equalsIgnoreCase("開")) {
             pc.sendPackets((ServerBasePacket)new S_MARK_SEE(pc, 2, true), true);
             pc.sendPackets((ServerBasePacket)new S_MARK_SEE(pc, 0, true), true);
             pc.sendPackets("開始顯示血盟標記。");
             } else if (onoff.equalsIgnoreCase("關")) {
             pc.sendPackets((ServerBasePacket)new S_MARK_SEE(pc, 2, false), true);
             pc.sendPackets((ServerBasePacket)new S_MARK_SEE(pc, 1, false), true);
             pc.sendPackets("停止顯示血盟標記。");
             } else {
             pc.sendPackets(".血盟標記 [開 / 關]");
             return;
             }
             } catch (Exception e) {
             pc.sendPackets(".血盟標記 [開 / 關]");
             }
             }

     private void giftNCoin(L1PcInstance pc, String param) {
             try {
             StringTokenizer tok = new StringTokenizer(param);
             String targetName = tok.nextToken();
             int count = Integer.parseInt(tok.nextToken());
             L1PcInstance target = L1World.getInstance().getPlayer(targetName);
             if (target == null) {
             pc.sendPackets(targetName + " 目前未在線。");
             return;
             }
             if (target.getNetConnection() == null) {
             pc.sendPackets(target + " 的連接狀態不正確。");
             return;
             }
             if (pc == target) {
             pc.sendPackets("不能將 N幣轉移給自己。");
             return;
             }
             if (count > pc.getNcoin()) {
             pc.sendPackets("你的 N幣少於 " + count + "，無法轉移。");
             return;
             }
             pc.addNcoin1(count);
             target.addNcoin(count);
             pc.sendPackets("你已將 [" + count + "] 個 N幣贈送給 [" + targetName + "]。");
             target.sendPackets("你已收到 [" + count + "] 個 N幣，由 [" + pc.getName() + "] 贈送。");
             } catch (Exception e) {
             pc.sendPackets(".贈送N幣 [角色名稱] [金額]");
             }
             }

     private void showHelp(L1PcInstance pc) {
             if (pc.isShiftBattle()) {

             StringBuilder sb = new StringBuilder();
             sb.append("━━━━━━━━━━━━━ 幫助 ━━━━━━━━━━━━━━").append("\r\n");
             sb.append("..(傳送) .光源 .外窗 .血盟標記 .血盟派對").append("\r\n");
             sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").append("\r\n");
             pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, sb.toString()));
             } else {

             StringBuilder sb = new StringBuilder();
             sb.append("━━━━━━━━━━━━━ 幫助 ━━━━━━━━━━━━━━").append("\r\n");
             sb.append("..(傳送) .密碼更改 .年齡 .光源 .外窗 .框架查詢 .恢復").append("\r\n");
             sb.append(".老闆提醒 .公告 .血盟派對 .血盟標記  .宏 .購買限制").append("\r\n");
             sb.append(".優惠券登錄  .樂透查詢  .領地效果  .強制變身").append("\r\n");
             sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").append("\r\n");
             pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, sb.toString()));
             }
             }

     private void Ment(L1PcInstance pc, String param) {
             if (param.equalsIgnoreCase("關")) {
             pc.sendPackets("已關閉掉落提示，只顯示重要掉落物。");
             pc.RootMent = false;
             } else if (param.equalsIgnoreCase("開")) {
             pc.sendPackets("已開啟掉落提示，顯示所有掉落物。");
             pc.RootMent = true;
             } else {
             pc.sendPackets(".提示 [開/關] 請輸入。");
             }
             }

     private void Sealedoff(final L1PcInstance pc, String param) {
             try {
             StringTokenizer tok = new StringTokenizer(param);
             String param1 = tok.nextToken();
             int off = Integer.parseInt(param1);
             if (off > 10 || off < 0) {
             pc.sendPackets("\f2一次最多申請 (10) 張。");
             return;
             }
             if (pc._create_password) {
             pc.sendPackets("\f2密碼註冊失敗時，請等待 (30) 秒後再試。");
             return;
             }
             if (pc._seal_scroll) {
             pc.sendPackets("\f2密碼驗證失敗時，請等待 (30) 秒後再試。");
             return;
             }
             if (pc.getAccount().getShopPassword() == 0) {
             pc._create_password = true;
             pc.sendPackets("\f2首次使用時，請輸入密碼。\f3(更改密碼時會斷線)");
             } else {
             pc._seal_scroll = true;
             pc._seal_scroll_count = off;
             pc.sendPackets("\f230 秒內請驗證密碼。\f3(更改密碼時會斷線)");
             }
             } catch (Exception e) {
             pc.sendPackets("命令格式錯誤，請使用：.封印 [數量]");
             }
             }
     private void Sealedoff(final L1PcInstance pc, String param) {
             try {
             StringTokenizer tok = new StringTokenizer(param);
             String param1 = tok.nextToken();
             int off = Integer.parseInt(param1);
             if (off > 10 || off < 0) {
             pc.sendPackets("\f2一次最多申請 (10) 張。");
             return;
             }
             if (pc._create_password) {
             pc.sendPackets("\f2密碼註冊失敗時，請等待 (30) 秒後再試。");
             return;
             }
             if (pc._seal_scroll) {
             pc.sendPackets("\f2密碼驗證失敗時，請等待 (30) 秒後再試。");
             return;
             }
             if (pc.getAccount().getShopPassword() == 0) {
             pc._create_password = true;
             pc.sendPackets("\f2首次使用時，請輸入密碼。\f3(更改密碼時會斷線)");
             } else {
             pc._seal_scroll = true;
             pc._seal_scroll_count = off;
             pc.sendPackets("\f230 秒內請驗證密碼。\f3(更改密碼時會斷線)");
             }

            // 添加定時任務來處理超時
             GeneralThreadPool.getInstance().schedule(new Runnable() {
     public void run() {
             try {
             if (pc._seal_scroll) {
             pc._seal_scroll = false;
             [23:42]
             pc.sendPackets("\f2時間已過，密碼驗證失敗，請重新嘗試。");
             }
             if (pc._create_password) {
             pc._create_password = false;
             pc.sendPackets("\f2時間已過，密碼創建失敗，請重新嘗試。");
             }
             pc._seal_scroll_count = 0;
             } catch (Exception e) {
             e.printStackTrace();
             }
             }
             },  30000L); // 30秒後執行
             } catch (Exception e) {
             pc.sendPackets("命令格式錯誤，請使用：.封印 [數量]");
             }
             }







     public static void doTarget(MJCommandArgs args) {
             try {
             String cmd = args.nextString();
             if (cmd.equalsIgnoreCase("開")) {
             args.getOwner().setOnTargetEffect(true);
             args.notify("目標系統已啟用。");
             } else if (cmd.equalsIgnoreCase("關")) {
             args.getOwner().setOnTargetEffect(false);
             args.notify("目標系統已停用。");
             } else if (cmd.equalsIgnoreCase("狀態")) {
             args.notify(String.format("當前目標系統狀態 : %s", Boolean.valueOf(args.getOwner().isOnTargetEffect())));
             } else {
             throw new Exception();
             }
             } catch (Exception e) {
             args.notify(".目標 [開|關|狀態]");
             }
             }


       public static boolean isAlphaNumeric(String s) {
         char[] acArray = s.toCharArray();
         for (char ac : acArray) {
           if ((ac >= 'A' && ac <= 'z') || (ac >= 'a' && ac <= 'z'))
             return true;
           if (ac >= '0' && ac <= '9')
             return true;
           if (ac >= '가' && ac <= '힣') {
             return true;
           }
         }
         return false;
       }


       public static void buddys(L1PcInstance pc) {
         Connection con = null;
         PreparedStatement pstm = null;
         String aaa = pc.getName();
         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("DELETE FROM character_buddys WHERE buddy_name=?");

           pstm.setString(1, aaa);
           pstm.execute();
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
       }

     public static void 刪除信件(L1PcInstance pc) {
             Connection con = null;
             PreparedStatement pstm = null;

             String playerName = pc.getName();

             try {
             con = L1DatabaseFactory.getInstance().getConnection();
             pstm = con.prepareStatement("DELETE FROM letter WHERE receiver=?");
             pstm.setString(1, playerName);
             pstm.execute();
             } catch (Exception e) {
             e.printStackTrace();
             } finally {
             SQLUtil.close(pstm);
             SQLUtil.close(con);
             }
             }

       private void hunt(L1PcInstance pc, String cmd) {
         int price1 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[0];
         int price2 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[1];
         int price3 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[2];
         try {
           StringTokenizer tok = new StringTokenizer(cmd);
           String name = tok.nextToken();
           if (name == null || name.equals("")) {
             throw new Exception();
           }
           L1PcInstance target = L1World.getInstance().getPlayer(name);

             if (target == null) {
             pc.sendPackets(String.format("%s 使用者無法找到。", name));
             return;
             }

             if (target == pc) {
             pc.sendPackets("無法對自己設置懸賞。");
             return;
             }

             if (target.getClanid() == pc.getClanid()) {
             pc.sendPackets("無法對同一血盟成員設置懸賞。");
             return;
             }

             if (target.hasSkillEffect(32423425)) {
             pc.sendPackets(String.format("%s 使用者已經是三級懸賞目標。", name));
             return;
             }

             if (target.isGm()) {
             pc.sendPackets("無法對管理員設置懸賞。");
             return;
             }

             if (target.get_Wanted_Level() == 0) {
             if (!pc.getInventory().checkItem(40308, price1)) {
             pc.sendPackets("金幣不足 (" + price1 + ")");
             return;
             }
             } else if (target.get_Wanted_Level() == 1) {
             if (!pc.getInventory().checkItem(40308, price2)) {
             pc.sendPackets("金幣不足 (" + price2 + ")");
             return;
             }
             } else if (target.get_Wanted_Level() == 2 &&
             !pc.getInventory().checkItem(40308, price3)) {
             pc.sendPackets("金幣不足 (" + price3 + ")");
             return;
             }
             try {
             if (target.get_Wanted_Level() == 0) {
             String message = String.format("%s 玩家對 %s 玩家設置了一級懸賞。", pc.getName(), target.getName());
             pc.sendPackets(message);
             target.sendPackets(message);
             target.setSkillEffect(32423423, -1L);
             target.add_Wanted_Level();
             target.doWanted(true, false);
             pc.getInventory().consumeItem(40308, price1);
             } else if (target.hasSkillEffect(32423423)) {
             String message = String.format("%s 玩家對 %s 玩家設置了二級懸賞。", pc.getName(), target.getName());
             pc.sendPackets(message);
             target.sendPackets(message);
             target.removeSkillEffect(32423423);
             target.setSkillEffect(32423424, -1L);
             target.add_Wanted_Level();
             target.doWanted(true, false);
             [23:52]
             pc.getInventory().consumeItem(40308, price2);
             } else if (target.hasSkillEffect(32423424)) {
             String message = String.format("%s 玩家對 %s 玩家設置了三級懸賞。", pc.getName(), target.getName());
             pc.sendPackets(message);
             target.sendPackets(message);
             target.removeSkillEffect(32423424);
             target.setSkillEffect(32423425, -1L);
             target.add_Wanted_Level();
             target.doWanted(true, false);
             pc.getInventory().consumeItem(40308, price3);
             }

             } catch (Exception e) {
             pc.sendPackets(".懸賞 [角色名稱]");
             pc.sendPackets("效果階段累積：近戰傷害減少3，命中減少3，遠程傷害減少3，命中減少3，SP減少3，傷害減少3，AC減少3");
             pc.sendPackets("1級懸賞價格: " + price1 + ", 2級懸賞價格: " + price2 + ", 3級懸賞價格: " + price3);
             }

     private void phone(L1PcInstance pc, String param) {
             try {
             long curtime = System.currentTimeMillis() / 1000L;
             if (pc.getQuizTime() + 10L > curtime) {
             long sec = pc.getQuizTime() + 10L - curtime;
             pc.sendPackets(sec + " 秒後可使用。");
             return;
             }
             StringBuilder sb = new StringBuilder();
             sb.append("-----------------------------------------------------").append("\r\n");
             sb.append("\fT電報 ():1").append("\r\n");
             sb.append("\fY電報 (頻道):1").append("\r\n");
             sb.append("\f3請保持良好的聊天禮儀。").append("\r\n");
             sb.append("-----------------------------------------------------").append("\r\n");
             pc.sendPackets(new S_ChatPacket(pc, sb.toString()));

             } catch (Exception e) {
             pc.sendPackets("如有疑問，請重新開啟或提前查看公告。");
             }
             }

     private void spawnNotifyOnOff(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             String on = st.nextToken();

             if (on.equalsIgnoreCase("開")) {
             if (pc.isBossNotify()) {
             pc.sendPackets("老闆通知: 老闆召喚及通知已經是啟用狀態。");
             return;
             }
             pc.setBossNotify(true);
             pc.sendPackets("老闆通知: 開 (老闆召喚及通知已啟用)");
             } else if (on.equals("關")) {
             if (!pc.isBossNotify()) {
             pc.sendPackets("老闆通知: 老闆召喚及通知未啟用。");
             return;
             }
             pc.setBossNotify(false);
             pc.sendPackets("老闆通知: 關 (老闆召喚及通知已關閉)");
             } else {
             pc.sendPackets("無效的請求。");
             }
             } catch (Exception e) {
             pc.sendPackets(".老闆通知 [開, 關] 來設置。當前狀態: " + (pc.isBossNotify() ? "開" : "關") + ")");
             }
             }

     private void maphack(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             String on = st.nextToken();
             if (pc.getMapId() == 132) {
             pc.sendPackets(new S_Ability(3, false));
             pc.sendPackets("當前地圖中無法使用此功能。");
             return;
             }
             if (on.equalsIgnoreCase("開")) {
             pc.sendPackets(new S_Ability(3, true));
             pc.sendPackets("光源已啟用，變得明亮了。");
             } else if (on.equals("關")) {
             pc.sendPackets(new S_Ability(3, false));
             pc.sendPackets("光源已關閉，變得暗淡了。");
             }
             } catch (Exception e) {
             pc.sendPackets(".光源 [開, 關]");
             }
             }

     private void 復原(final L1PcInstance pc) {
             try {
             long curtime = System.currentTimeMillis() / 1000L;
             if (pc.getQuizTime2() + 5L > curtime) {
             long time = pc.getQuizTime2() + 5L - curtime;
             pc.sendPackets(time + " 秒後才能使用。");
             return;
             }
             Updator.exec("UPDATE characters SET LocX=33432, LocY=32807, MapID=4 WHERE account_name=? and MapID not in (34, 38, 5001, 99, 997, 5166, 39, 34, 701, 2000)", new Handler() {
     public void handle(PreparedStatement pstm) throws Exception {
             pstm.setString(1, pc.getAccountName());
             }
             });
             pc.sendPackets("所有角色的座標已正常復原。");
             pc.setQuizTime(curtime);
             } catch (Exception e) {
             pc.sendPackets("復原過程中發生錯誤。");
             }
             }


     private void tell(L1PcInstance pc) {
             long curtime = System.currentTimeMillis() / 1000L;
             if (pc.getQuizTime2() + 10L > curtime) {
             long time = pc.getQuizTime2() + 10L - curtime;
             pc.sendPackets(time + " 秒後才能使用。");
             return;
             }

             try {
             if (pc.getMapId() == 781 &&
             pc.getLocation().getX() <= 32998 && pc.getLocation().getX() >= 32988 && pc.getLocation().getY() <= 32758 && pc.getLocation().getY() >= 32736) {
             pc.sendPackets("在此位置無法使用此功能。");
             return;
             }

             if (pc.isPinkName() || pc.isDead() || pc.isParalyzed() || pc.isSleeped() || pc.getMapId() == 800 || pc.getMapId() == 12150 || pc.getMapId() == 12154 || pc.getMapId() == 5302 || pc
             .getMapId() == 5153 || pc.getMapId() == 5490 || pc.getMapId() == 213) {
             pc.sendPackets("在此位置無法使用此功能。");
             return;
             }
             [00:09]
             if (pc.hasSkillEffect(87)  pc.hasSkillEffect(123)  pc.hasSkillEffect(230)  pc.hasSkillEffect(157)  pc.hasSkillEffect(242)  pc
             .hasSkillEffect(243)  pc.hasSkillEffect(33)  pc.hasSkillEffect(70705)  pc.hasSkillEffect(29)  pc.hasSkillEffect(66)  pc
             .hasSkillEffect(208)  pc.hasSkillEffect(243)  pc.hasSkillEffect(5002)  pc.hasSkillEffect(51006)  pc
             .hasSkillEffect(77) || pc.hasSkillEffect(5056)) {
             pc.sendPackets("當前狀態下無法使用此功能。");
             return;
             }
           if (pc.getMapId() == 132) {
             pc.sendPackets((ServerBasePacket)new S_Ability(3, false));
           }




           pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
           pc.update_lastLocalTellTime();
           pc.setQuizTime2(curtime);
         } catch (Exception exception) {}
       }


     public void BloodParty(L1PcInstance pc) {
             if (pc.isDead()) {
             pc.sendPackets("死亡狀態下無法使用。");
             return;
             }
             int ClanId = pc.getClanid();
             if (ClanId != 0) {
             if (pc.getClanRank() == 10  pc.getClanRank() == 9  pc.getClanRank() == 14  pc.getClanRank() == 13  pc.getClanRank() == 8) {
             for (L1PcInstance SearchBlood : L1World.getInstance().getAllPlayers()) {
             if (SearchBlood.getClanid() != ClanId  SearchBlood.isPrivateShop()  SearchBlood.isInParty())
             continue;
             if (!SearchBlood.getName().equals(pc.getName())) {
             pc.setPartyType(1);
             SearchBlood.setPartyID(pc.getId());
             SearchBlood.sendPackets(new S_Message_YN(954, pc.getName()));
             pc.sendPackets(SearchBlood.getName() + " 已收到您的組隊邀請");
             }
             }
             }
             } else {
             pc.sendPackets("如果您是血盟成員且為盟主、副盟主、守護騎士、精英或一般成員，則可使用此功能。");
             }
             }

     private void age(L1PcInstance pc, String cmd) {
             try {
             StringTokenizer tok = new StringTokenizer(cmd);
             String AGE = tok.nextToken();
             int AGEint = Integer.parseInt(AGE);
             if (AGEint > 59 || AGEint < 14) {
             pc.sendPackets("請設置顯示給血盟的年齡。");
             return;
             }
             pc.setAge(AGEint);
             pc.save();
             pc.sendPackets("年齡 (" + AGEint + ") 已設置。");
             } catch (Exception e) {
             pc.sendPackets(".年齡 [數字]");
             }
             }

       private static boolean isDisitAlpha(String str) {
         boolean check = true;
         for (int i = 0; i < str.length(); i++) {
           if (!Character.isDigit(str.charAt(i)) &&
             !Character.isUpperCase(str.charAt(i)) &&
             !Character.isLowerCase(str.charAt(i))) {
             check = false;
             break;
           }
         }
         return check;
       }

     private void changepassword(L1PcInstance pc, String param) {
             try {
             if (pc.get_lastPasswordChangeTime() + 600000L > System.currentTimeMillis()) {
             pc.sendPackets("距離上次更改密碼未滿10分鐘，請稍後再試。");
             return;
             }
             StringTokenizer tok = new StringTokenizer(param);
             String newpasswd = tok.nextToken();
             if (newpasswd.length() < 6) {
             pc.sendPackets("請輸入6至16個字元的英文字母或數字。");
             return;
             }
             if (newpasswd.length() > 16) {
             pc.sendPackets("請輸入6至16個字元的英文字母或數字。");
             return;
             }
             if (!isDisitAlpha(newpasswd)) {
             pc.sendPackets("密碼僅能包含英文字母和數字。");
             return;
             }
             to_Change_Passwd(pc, newpasswd);
             }
             catch (Exception e) {
             pc.sendPackets("請輸入 .密碼變更 [新密碼] 來更改密碼。");
             }
             }

     private void to_Change_Passwd(final L1PcInstance pc, final String passwd) {
             Selector.exec("SELECT account_name FROM characters WHERE char_name=?", new SelectorHandler() {
     public void handle(PreparedStatement pstm) throws Exception {
             pstm.setString(1, pc.getName());
             }

     public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
     final String login = rs.getString("account_name");
             Updator.exec("UPDATE accounts SET password=? WHERE login=?", new Handler() {
     public void handle(PreparedStatement pstm) throws Exception {
             pstm.setString(1, passwd);
             pstm.setString(2, login);
             }
             });
             }
             }
             });
             pc.sendPackets(String.format("\f2您的帳號密碼已更改為 (%s)。", new Object[] { passwd }));
             pc.sendPackets("\f3部分功能將受到限制。請重新啟動遊戲客戶端以解除限制。");
             pc.sendPackets(new S_PacketBox(84, "\f3部分功能將受到限制。請重新啟動遊戲客戶端以解除限制。"));
             }


       public static boolean isPasswordTrue(String Password, String oldPassword) {
         String _rtnPwd = null;
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         boolean result = false;
         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("SELECT password(?) as pwd");

           pstm.setString(1, oldPassword);
           rs = pstm.executeQuery();
           if (rs.next()) {
             _rtnPwd = rs.getString("pwd");
           }
           if (_rtnPwd.equals(Password)) {
             result = true;
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
         return result;
       }

     public static void privateShop(L1PcInstance pc) {
             try {
             if (!pc.isPrivateShop()) {
             pc.sendPackets("僅在個人商店狀態下可使用此功能。");
             return;
             }
             for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
             if (target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
             pc.sendPackets("\f3您的另一角色已處於無人商店狀態。");
             pc.sendPackets("\f3請關閉商店。 /商店");
             return;
             }
             }
             GameClient client = pc.getNetConnection();
             pc.setNetConnection(null);
             pc.dispose_regenerator();

             pc.set무인상점(true);
             try {
             pc.save();
             pc.saveInventory();
             } catch (Exception exception) {}

             client.setActiveChar(null);
             client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
             client.sendPacket(new S_Unknown2(1));
             }
             catch (Exception exception) {}
             }


     private void outsideChat(L1PcInstance pc, String param) {
             try {
             if (param.equalsIgnoreCase("開")) {
             pc.setOutSideChat(true);
             } else if (param.equalsIgnoreCase("關")) {
             pc.setOutSideChat(false);
             } else if (!param.equalsIgnoreCase("狀態")) {
             throw new Exception();
             }
             pc.sendPackets(String.format("外部聊天 : %s", new Object[] { pc.isOutsideChat() ? "開啟" : "關閉" }));
             } catch (Exception e) {
             pc.sendPackets(".外部聊天 [開|關|狀態]");
             }
             }

     private String parseStat(String s) throws Exception {
             if (s.equalsIgnoreCase("力量"))
             return "str";
             if (s.equalsIgnoreCase("敏捷"))
             return "dex";
             if (s.equalsIgnoreCase("體質"))
             return "con";
             if (s.equalsIgnoreCase("智力"))
             return "int";
             if (s.equalsIgnoreCase("智慧"))
             return "wis";
             if (s.equalsIgnoreCase("魅力"))
             return "cha";
             throw new Exception(s);
             }

     public void fullstat(L1PcInstance pc, String param) {
             try {
             String[] arr = param.split(" ");
             if (arr == null || arr.length < 2) {
             throw new Exception();
             }
             String s = parseStat(arr[0]);
             MJFullStater.running(pc, s, Integer.parseInt(arr[1]));
             } catch (Exception e) {
             pc.sendPackets(String.format(".屬性 [力量/敏捷/體質/智力/智慧/魅力] [數值] 剩餘屬性點 %d", new Object[] { Integer.valueOf(pc.remainBonusStats()) }));
             }
             }



     private static void do_lfc(L1PcInstance pc, String param) {
             try {
             String name = param;
             L1PcInstance target = L1World.getInstance().findpc(name);
             if (target == null) {
             pc.sendPackets(String.format("找不到 %s 這位玩家。", new Object[] { name }));
             return;
             }
             L1BoardPost bp = L1BoardPost.createLfc(name, "-", String.format("3 %s", new Object[] { pc.getName() }));
             MJLFCCreator.registLfc(pc, 3);
             [00:40]
             target.sendPackets(String.format("已向 %s 申請決鬥。", new Object[] { name }));
             target.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(String.format("%s 向您申請決鬥。", new Object[] { pc.getName() }), bp.getId() + 1000, MJSurveyFactory.createLFCSurvey(), 30000L));
             } catch (Exception e) {
             e.printStackTrace();
             }
             }

       private void macroSetting(L1PcInstance pc, String param) {
         try {
           StringTokenizer tok = new StringTokenizer(param);
           String setting = tok.nextToken();

             if (setting.equalsIgnoreCase("設置")) {
             StringBuilder str = new StringBuilder();
             while (tok.hasMoreTokens()) {
             str.append(tok.nextToken() + " ");
             }

             String ment = str.toString();
             if (ment.length() > 1) {
             pc.addMacroList(ment);
             pc.sendPackets("宏已添加語句。");
             pc.sendPackets(" - [" + ment + "]");
             } else {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage(".宏 [設置/開始/停止/刪除/確認] [設置時添加的語句] [刪除時刪除的序號] 請輸入。"));
             }
             } else if (setting.equalsIgnoreCase("確認")) {
             pc.getMacroListIdentify();
             } else if (setting.equalsIgnoreCase("刪除")) {

             String index = tok.nextToken();
             if (index.equalsIgnoreCase("全部")) {
             pc.getMacroList().clear();
             pc.sendPackets("所有宏語句已刪除。");
             } else if (isStringDouble(index)) {
             [00:46]
             int real_index = Integer.valueOf(index).intValue();
             if (real_index > pc.getMacroList().size()) {
             pc.sendPackets("該序號的宏不存在。");
             return;
             }
             pc.getMacroList().remove(real_index);
             pc.sendPackets("[" + real_index + "號] 宏語句已刪除。");
             } else {
             pc.sendPackets("刪除序號必須是數字。");
             }
             }
             else if (setting.equalsIgnoreCase("開始")) {
             if (pc.getMacroList().size() <= 0) {
             pc.sendPackets("沒有已經輸入的宏。");

             return;
             }
             if (pc.isMacroTimerStart()) {
             pc.sendPackets("宏已經在運行。");

             return;
             }
             pc.startMacroTimer();
             pc.sendPackets("宏已開始。");
             } else if (setting.equalsIgnoreCase("停止")) {
             if (!pc.isMacroTimerStart()) {
             pc.sendPackets("當前沒有運行的宏。");

             return;
             }
             pc.stopMacroTimer();
             pc.sendPackets("宏已停止。");
             } else {
             pc.sendPackets("無效的請求。");
             }
             } catch (Exception e) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage(".宏 [設置/開始/停止/刪除/確認] [設置時添加的語句] [刪除時刪除的序號] 請輸入。"));
             }
             }

       private boolean isStringDouble(String s) {
         try {
           Double.parseDouble(s);
           return true;
         } catch (NumberFormatException e) {
           return false;
         }
       }

     public void insertCashInfo(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             int charge_count = Integer.parseInt(st.nextToken());

             if (pc == null) {
             return;
             }
             if (charge_count < 10000) {
             pc.sendPackets("充值的最低金額為10000韓元。");
             return;
             }
             if (charge_count > 500000) {
             pc.sendPackets("充值的最高金額為500000韓元。");
             return;
             }
             if (getDepositInfo(pc) == 0) {
             pc.sendPackets("已經有正在申請中的請求。");
             return;
             }
     public void insertCashInfo(L1PcInstance pc, String param) {
             try {
             StringTokenizer st = new StringTokenizer(param);
             int charge_count = Integer.parseInt(st.nextToken());

             if (pc == null) {
             return;
             }
             if (charge_count < 10000) {
             pc.sendPackets("充值的最低金額為10000韓元。");
             return;
             }
             if (charge_count > 500000) {
             pc.sendPackets("充值的最高金額為500000韓元。");
             return;
             }
             if (getDepositInfo(pc) == 0) {
             pc.sendPackets("已經有正在申請中的請求。");
             return;
             }

             String current_date = MJString.get_current_datetime();

             MJNCoinDepositInfo dInfo = MJNCoinDepositInfo.newInstance()
             .set_deposit_id(MJNCoinIdFactory.DEPOSIT.next_id())
             .set_character_object_id(pc.getId())
             .set_character_name(pc.getName())
             .set_account_name(pc.getAccountName())
             .set_deposit_name(pc.getAccountName())
             .set_ncoin_value(Integer.valueOf(charge_count).intValue())
             .set_generate_date(current_date)
             .set_is_deposit(0);

             MJNCoinDepositInfo.do_store(dInfo);
             do_write_letter_command(pc, MJNCoinSettings.DEPOSIT_LETTER_ID);

             String subject = String.format("[充值申請] %s", new Object[] { pc.getName() });
             do_write_letter_togm(current_date, subject, dInfo.toString());
             } catch (Exception e) {
             pc.sendPackets(".N幣充值 [金額]");
             }
             }

       private void do_write_letter_command(L1PcInstance pc, final int notify_id) {
         final MJObjectWrapper<String> subject = new MJObjectWrapper();
         final MJObjectWrapper<String> content = new MJObjectWrapper();
         Selector.exec("select * from letter_command where id=?", new SelectorHandler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setInt(1, notify_id);
               }


     public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
             subject.value = rs.getString("subject");
             content.value = rs.getString("content");
             } else {
             subject.value = "";
             content.value = "";
             }
             }
             });
             if (MJString.isNullOrEmpty((String)subject.value) && MJString.isNullOrEmpty((String)content.value)) {
             try {
             throw new Exception(String.format("找不到命令信件。ID: %d 堆疊跟蹤", new Object[] { Integer.valueOf(notify_id) }));
             } catch (Exception e) {
             e.printStackTrace();

             return;
             }
             }
             String current_date = MJString.get_current_datetime();
             do_write_letter(pc.getName(), current_date, (String)subject.value, (String)content.value);
             }

     private void do_write_letter_togm(String generate_date, String subject, String content) {

             do_write_letter("梅蒂斯", generate_date, subject, content);
             }

     private void do_write_letter(String receiver, String generate_date, String subject, String content) {
             int id = LetterTable.getInstance().writeLetter(949, generate_date, "梅蒂斯", receiver, 0, subject, content);
             L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
             if (pc != null) {
             pc.sendPackets((ServerBasePacket)new S_LetterList(80, id, 0, "梅蒂斯", subject));

             pc.send_effect(1091);
             pc.sendPackets(428);
             }
             }

       private int getDepositInfo(L1PcInstance pc) {
         int check = -1;
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("SELECT * FROM ncoin_trade_deposit WHERE account_name=?");
           pstm.setString(1, pc.getAccount().getName());
           rs = pstm.executeQuery();
           while (rs.next()) {
             check = rs.getInt("is_deposit");
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(rs, pstm, con);
         }

         return check;
       }
     }


